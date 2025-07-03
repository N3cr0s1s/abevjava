package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.cachedExp;

import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.FunctionBodies;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IPropertyList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class CachedItems {
   public static final int PROCESSING_IS_ON = 1;
   public static final int PROCESSING_IS_OFF = 0;
   private int mode = 0;
   private Hashtable cacheTable = new Hashtable();
   private Hashtable targetTable = new Hashtable();

   public void setProcessMode(int var1) {
      this.mode = var1;
      this.init();
   }

   public int getMode() {
      return this.mode;
   }

   public void add(String var1, Object[] var2, String var3, String var4) {
      Object var5 = null;
      if (var1.equals("globsum")) {
         var5 = new CachedGlobsum(var1, var2);
      } else if (var1.equals("unique_m")) {
         var5 = new CachedUniquem(var1, var2);
      }

      this.createIndex(this.cacheTable, (ICachedItem)var5);
      this.addTargetItem(var3, var1, var4);
   }

   public void createIndex(Hashtable var1, ICachedItem var2) {
      String var3 = var2.getFormid();
      String var4 = var2.getFunction();
      String var5 = var2.getParameter();
      Hashtable var6 = (Hashtable)var1.get(var3);
      if (var6 == null) {
         var6 = new Hashtable();
         var1.put(var3, var6);
      }

      Hashtable var7 = (Hashtable)var6.get(var4);
      if (var7 == null) {
         var7 = new Hashtable();
         var6.put(var4, var7);
      }

      var7.put(var5, var2);
   }

   public ICachedItem getResult(String var1, String var2, Object var3) {
      Hashtable var4 = (Hashtable)this.cacheTable.get(var1);
      Hashtable var5 = (Hashtable)var4.get(var2);
      ICachedItem var6 = (ICachedItem)var5.get(var3);
      return var6;
   }

   public void exec(BookModel var1) {
      if (this.mode != 1) {
         this.init();
         IPropertyList var2 = (IPropertyList)var1.get_store_collection();
         int var4 = var1.getCalcelemindex();
         FunctionBodies.saveActiveParameters();

         try {
            Enumeration var5 = this.cacheTable.keys();

            while(var5.hasMoreElements()) {
               String var6 = (String)var5.nextElement();

               for(int var7 = 0; var7 < var1.cc.size(); ++var7) {
                  if (((Elem)((Vector)var2).get(var7)).getType().equals(var6)) {
                     Elem var3 = (Elem)((Vector)var2).get(var7);
                     var1.setCalcelemindex(var7);
                     IDataStore var8 = (IDataStore)var3.getRef();
                     FunctionBodies.setActiveParameters(var6, var8);
                     Hashtable var9 = (Hashtable)this.cacheTable.get(var6);
                     Enumeration var10 = var9.elements();

                     while(var10.hasMoreElements()) {
                        Hashtable var11 = (Hashtable)var10.nextElement();
                        Enumeration var12 = var11.elements();

                        while(var12.hasMoreElements()) {
                           ICachedItem var13 = (ICachedItem)var12.nextElement();
                           var13.exec();
                        }
                     }
                  }
               }
            }
         } catch (Exception var14) {
            var14.printStackTrace();
         }

         var1.setCalcelemindex(var4);
         FunctionBodies.restoreActiveParameters();
         this.releaseTmpData();
      }
   }

   public void exec() {
      if (this.mode != 0) {
         try {
            String var1 = FunctionBodies.g_active_form_id;
            Hashtable var2 = (Hashtable)this.cacheTable.get(var1);
            if (var2 != null) {
               Enumeration var3 = var2.elements();

               while(var3.hasMoreElements()) {
                  Hashtable var4 = (Hashtable)var3.nextElement();
                  Enumeration var5 = var4.elements();

                  while(var5.hasMoreElements()) {
                     ICachedItem var6 = (ICachedItem)var5.nextElement();
                     var6.exec();
                  }
               }
            }
         } catch (Exception var7) {
            var7.printStackTrace();
         }

      }
   }

   public void init() {
      Enumeration var1 = this.cacheTable.elements();

      while(var1.hasMoreElements()) {
         Hashtable var2 = (Hashtable)var1.nextElement();
         Enumeration var3 = var2.elements();

         while(var3.hasMoreElements()) {
            Hashtable var4 = (Hashtable)var3.nextElement();
            Enumeration var5 = var4.elements();

            while(var5.hasMoreElements()) {
               ICachedItem var6 = (ICachedItem)var5.nextElement();
               var6.init();
            }
         }
      }

   }

   public void releaseTmpData() {
      Enumeration var1 = this.cacheTable.elements();

      while(var1.hasMoreElements()) {
         Hashtable var2 = (Hashtable)var1.nextElement();
         Enumeration var3 = var2.elements();

         while(var3.hasMoreElements()) {
            Hashtable var4 = (Hashtable)var3.nextElement();
            Enumeration var5 = var4.elements();

            while(var5.hasMoreElements()) {
               ICachedItem var6 = (ICachedItem)var5.nextElement();
               var6.releaseTmpData();
            }
         }
      }

   }

   private void addTargetItem(String var1, String var2, String var3) {
      if (var3 != null && var3.length() > 0) {
         Hashtable var4 = (Hashtable)this.targetTable.get(var1);
         if (var4 == null) {
            var4 = new Hashtable();
            this.targetTable.put(var1, var4);
         }

         Hashtable var5 = (Hashtable)var4.get(var2);
         if (var5 == null) {
            var5 = new Hashtable();
            var4.put(var2, var5);
         }

         var5.put(var3, "");
      }

   }

   public boolean isCachedTargetId(String var1, String var2, String var3) {
      Hashtable var4 = (Hashtable)this.targetTable.get(var1);
      if (var4 != null) {
         Hashtable var5 = (Hashtable)var4.get(var2);
         if (var5 != null) {
            return var5.containsKey(var3);
         }
      }

      return false;
   }
}
