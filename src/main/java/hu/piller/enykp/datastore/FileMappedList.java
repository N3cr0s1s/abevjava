package hu.piller.enykp.datastore;

import hu.piller.enykp.interfaces.IFileMappedListElement;
import hu.piller.enykp.util.base.Tools;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.Vector;

public class FileMappedList extends Vector {
   private String filename;
   private File file;
   private RandomAccessFile raf;
   private String mode = "rws";
   private int maxInMemory;
   private int inmemory;
   private int readcount;
   private int writecount;
   private long readtime;
   private long writetime;
   private int startindex = 0;

   public FileMappedList(int var1, int var2) {
      super(var1);
      this.maxInMemory = var2 < 1 ? 1 : var2;
      this.maxInMemory = (int)((double)Runtime.getRuntime().freeMemory() * 0.6D / 100000.0D);
      this.readcount = this.writecount = 0;
      this.readtime = this.writetime = 0L;
   }

   public void init(String var1) {
      this.filename = var1;

      try {
         this.destroy(true);
         this.inmemory = 0;
         this.file = new File(var1);
         this.file.delete();
         this.raf = new RandomAccessFile(this.file, this.mode);
      } catch (FileNotFoundException var3) {
         var3.printStackTrace();
      }

   }

   public void destroy(boolean var1) {
      try {
         if (this.raf != null) {
            this.raf.close();
         }

         if (var1 && this.file != null) {
            this.file.delete();
         }

         this.clear();
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public String getIOInfo() {
      return "Read=" + this.readcount + " " + this.readtime + " ms    Write=" + this.writecount + " " + this.writetime + " ms";
   }

   public boolean add(Object var1) {
      if (var1 instanceof IFileMappedListElement) {
         FileMappedList.FileMappedListElem var2 = new FileMappedList.FileMappedListElem((IFileMappedListElement)var1);
         boolean var3;
         if (this.inmemory == this.maxInMemory) {
            if (this.search_space()) {
               ++this.inmemory;
               var3 = super.add(var2);
               if (var3) {
                  ((IFileMappedListElement)var1).setIndex(this.size() - 1);
               }

               return var3;
            } else {
               return false;
            }
         } else {
            ++this.inmemory;
            var3 = super.add(var2);
            if (var3) {
               ((IFileMappedListElement)var1).setIndex(this.size() - 1);
            }

            return var3;
         }
      } else {
         return false;
      }
   }

   private boolean search_space() {
      int var1;
      for(var1 = this.startindex; var1 < this.size(); ++var1) {
         if (((FileMappedList.FileMappedListElem)super.get(var1)).write()) {
            this.startindex = var1;
            return true;
         }
      }

      this.startindex = 0;

      for(var1 = this.startindex; var1 < this.size(); ++var1) {
         if (((FileMappedList.FileMappedListElem)super.get(var1)).write()) {
            this.startindex = var1;
            return true;
         }
      }

      return false;
   }

   public Object getquick(int var1) {
      try {
         Object var2 = super.get(var1);
         return ((FileMappedList.FileMappedListElem)var2).user;
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
         return null;
      }
   }

   public Object get(int var1) {
      try {
         Object var2 = super.get(var1);
         if (((FileMappedList.FileMappedListElem)var2).inMemory) {
            return ((FileMappedList.FileMappedListElem)var2).user;
         } else if (this.inmemory == this.maxInMemory) {
            if (this.search_space()) {
               ((FileMappedList.FileMappedListElem)var2).load();
               return ((FileMappedList.FileMappedListElem)var2).user;
            } else {
               return null;
            }
         } else {
            ((FileMappedList.FileMappedListElem)var2).load();
            return ((FileMappedList.FileMappedListElem)var2).user;
         }
      } catch (Exception var3) {
         return null;
      }
   }

   public Object _get(int var1) {
      return super.get(var1);
   }

   public Object get(IFileMappedListElement var1) {
      for(int var2 = 0; var2 < this.size(); ++var2) {
         if (((FileMappedList.FileMappedListElem)this._get(var2)).user.equals(var1)) {
            this.get(var2);
            return var1;
         }
      }

      return var1;
   }

   public boolean remove(Object var1) {
      for(int var2 = 0; var2 < this.size(); ++var2) {
         if (((FileMappedList.FileMappedListElem)this._get(var2)).user.equals(var1)) {
            --this.inmemory;
            return super.remove(this._get(var2));
         }
      }

      return false;
   }

   public Object remove(int var1) {
      if (((FileMappedList.FileMappedListElem)super.get(var1)).inMemory) {
         --this.inmemory;
      }

      return super.remove(var1);
   }

   public String getUsage() {
      StringBuffer var1 = new StringBuffer(this.size() * 2 + 2);

      for(int var2 = 0; var2 < this.size(); ++var2) {
         var1.append(((FileMappedList.FileMappedListElem)super.get(var2)).inMemory ? ",1" : ",0");
      }

      return var1.toString().substring(1);
   }

   public void setMaxInMemory(int var1) {
      this.maxInMemory = var1;
   }

   class FileMappedListElem {
      long fp;
      int length;
      long maxlength;
      IFileMappedListElement user;
      boolean noMapped;
      boolean inMemory;

      public FileMappedListElem(IFileMappedListElement var2) {
         this.user = var2;
         this.fp = -1L;
         this.length = 0;
         this.maxlength = 0L;
         this.noMapped = var2.noMapped();
         this.inMemory = true;
      }

      public boolean write() {
         try {
            if (!this.noMapped && this.inMemory) {
               if (!this.user.isChanged() && this.fp != -1L) {
                  this.user.setMappedObject((Object)null);
                  this.inMemory = false;
                  FileMappedList.this.inmemory--;
                  return true;
               } else {
                  long var1 = System.currentTimeMillis();
                  int var3 = 0;
                  byte[] var4 = this.getBytes();
                  int var5 = var4.length;
                  long var6 = this.fp;
                  if (this.fp == -1L || (long)var5 > this.maxlength) {
                     var6 = FileMappedList.this.raf.length();
                     var3 = (int)((float)var5 * this.user.getLoadfactor());
                  }

                  FileMappedList.this.raf.seek(var6);
                  FileMappedList.this.raf.write(var4);
                  FileMappedList.this.writetime = FileMappedList.this.writetime + (System.currentTimeMillis() - var1);
                  FileMappedList.this.writecount++;
                  if (var3 != 0) {
                     FileMappedList.this.raf.write(new byte[var3]);
                     this.maxlength = (long)(var5 + var3);
                  }

                  this.length = var5;
                  this.fp = var6;
                  this.user.setMappedObject((Object)null);
                  this.inMemory = false;
                  FileMappedList.this.inmemory--;
                  return true;
               }
            } else {
               return false;
            }
         } catch (IOException var8) {
            var8.printStackTrace();
            return false;
         }
      }

      public boolean load() {
         if (this.inMemory) {
            return true;
         } else {
            try {
               long var1 = System.currentTimeMillis();
               byte[] var3 = new byte[this.length];
               FileMappedList.this.raf.seek(this.fp);
               FileMappedList.this.raf.readFully(var3);
               ByteArrayInputStream var4 = new ByteArrayInputStream(var3);
               ObjectInputStream var5 = new ObjectInputStream(var4);
               Object var6 = var5.readObject();
               FileMappedList.this.readtime = FileMappedList.this.readtime + (System.currentTimeMillis() - var1);
               FileMappedList.this.readcount++;
               this.user.clearChanged();
               this.user.setMappedObject(var6);
               this.inMemory = true;
               FileMappedList.this.inmemory++;
               return true;
            } catch (Exception var7) {
               var7.printStackTrace();
               return false;
            }
         }
      }

      private byte[] getBytes() {
         Object var1 = this.user.getMappedObject();
         if (var1 == null) {
            return new byte[0];
         } else {
            try {
               ByteArrayOutputStream var2 = new ByteArrayOutputStream();
               ObjectOutputStream var3 = new ObjectOutputStream(var2);
               var3.writeObject(var1);
               byte[] var4 = var2.toByteArray();
               var3.close();
               var2.close();
               return var4;
            } catch (Exception var5) {
               var5.printStackTrace();
               return new byte[0];
            }
         }
      }

      public String toString() {
         return this.user.toString();
      }
   }
}
