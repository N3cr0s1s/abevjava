package hu.piller.enykp.alogic.primaryaccount.common;

import hu.piller.enykp.util.base.Tools;
import java.awt.Component;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class DefaultRecord implements IRecord {
   private IRecordFactory factory;
   private File file;
   private DefaultEnvelope envelope;
   private final Hashtable data = new Hashtable(32);

   public DefaultRecord(IRecordFactory var1, File var2, DefaultEnvelope var3) {
      this.factory = var1;
      this.file = var2;
      this.envelope = var3;
   }

   public void delete() {
      if (this.factory != null) {
         this.factory.delete(this.file, this);
      }

   }

   public String getName() {
      Object var1 = this.data.get("name");
      return var1 == null ? "" : var1.toString();
   }

   public String getDescription(String var1) {
      return this.getName();
   }

   public Hashtable getData() {
      return this.data;
   }

   public void setData(Object var1, Object var2) {
      if (var1 != null && var2 != null) {
         this.data.put(var1, var2);
      }

   }

   public void reload() {
      try {
         if (this.factory != null) {
            this.factory.reload(this.file, this);
         }
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
      }

   }

   public void save() {
      if (this.factory != null) {
         Object var1 = this.data.get("id");
         if (var1 == null) {
            this.data.put("id", this.factory.getNewId());
         }

         this.factory.save(this.file, this);
      }

   }

   public void printEnvelope(Component var1) {
      if (this.envelope != null) {
         this.envelope.print(this, var1);
      }

   }

   public DefaultEnvelope getEnvelope() {
      return this.envelope;
   }

   public IRecord[] filter(String var1, String var2) {
      return (IRecord[])((IRecord[])this.filter(var1, var2, this.factory).toArray(new IRecord[0]));
   }

   private Vector filter(String var1, String var2, IRecordFactory var3) {
      Vector var4;
      if (var3 != null && var1 != null && var2 != null) {
         var4 = var3.getRecords();
         if (var4 != null) {
            Vector var6 = new Vector(var4.size());
            int var7 = 0;

            for(int var8 = var4.size(); var7 < var8; ++var7) {
               Object var5 = var4.get(var7);
               if (var5 instanceof IRecord) {
                  IRecord var9 = (IRecord)var5;
                  Hashtable var10 = var9.getData();
                  Object var11 = var10.get(var1);
                  if (var11 != null && var2.equalsIgnoreCase(var11.toString())) {
                     var6.add(var9);
                  }
               }
            }

            return var6;
         }

         var4 = new Vector();
      } else {
         var4 = new Vector();
      }

      return var4;
   }

   public IRecord[] filterOnAll(String var1, String var2) {
      Hashtable var3 = DefaultRecordFactoryStore.getFactories();
      Enumeration var4 = var3.keys();
      Vector var5 = new Vector(512, 4096);

      while(var4.hasMoreElements()) {
         var5.addAll(this.filter(var1, var2, (IRecordFactory)var4.nextElement()));
      }

      return (IRecord[])((IRecord[])var5.toArray(new IRecord[0]));
   }

   public String toString() {
      Object var1 = this.data.get("id");
      return var1 == null ? "?" : var1.toString();
   }

   protected String getBraced(String var1) {
      return var1.trim().length() > 0 ? "(" + var1 + ")" : var1;
   }

   protected String getString(Object var1) {
      return var1 == null ? "" : var1.toString();
   }
}
