package hu.piller.enykp.datastore;

import java.util.Hashtable;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class Datastore_history {
   Hashtable datas = new Hashtable(100);
   Hashtable edatas = new Hashtable(100);
   String[] names = new String[]{"Adózói", "Adóügyi", "Revizori"};

   public void set(String var1, Vector var2) {
      this.datas.put(var1, var2);
   }

   public void sete(String var1, Vector var2) {
      this.edatas.put(var1, var2.clone());
   }

   public DefaultTableModel getDTM(String var1) {
      Vector var2 = (Vector)this.datas.get(var1);
      if (var2 == null) {
         return null;
      } else {
         Vector var3 = this.getcols();
         Vector var4 = new Vector();

         for(int var5 = 0; var5 < this.names.length; ++var5) {
            Vector var6 = new Vector();
            var6.add(this.names[var5]);

            try {
               var6.add(var2.get(var5));
            } catch (Exception var8) {
               var6.add("");
            }

            var4.add(var6);
         }

         DefaultTableModel var9 = new DefaultTableModel(var4, var3);
         return var9;
      }
   }

   public Vector get(String var1) {
      Vector var2 = (Vector)this.datas.get(var1);
      return var2;
   }

   private DefaultTableModel getDTM() {
      Vector var1 = new Vector();
      var1.add("Eredeti");
      var1.add("325");
      Vector var2 = new Vector();
      var2.add(var1);
      var1 = new Vector();
      var1.add("Javító");
      var1.add("456");
      var2.add(var1);
      var1 = new Vector();
      var1.add("Revizor1");
      var1.add("458");
      var2.add(var1);
      var1 = new Vector();
      var1.add("Revizor2");
      var1.add("459");
      var2.add(var1);
      Vector var3 = this.getcols();
      DefaultTableModel var4 = new DefaultTableModel(var2, var3);
      return var4;
   }

   private Vector getcols() {
      Vector var1 = new Vector();
      var1.add("Típus");
      var1.add("Érték");
      return var1;
   }

   public boolean check(String var1, Object var2, int var3) {
      Vector var4 = (Vector)this.edatas.get(var1);
      if (var4 == null) {
         return true;
      } else {
         return !var2.equals(var4.get(var3));
      }
   }

   public int getOldestFiller(String var1) {
      Vector var2 = (Vector)this.edatas.get(var1);
      if (var2 == null) {
         return -1;
      } else if (var2.elementAt(0) != null) {
         return 0;
      } else {
         return var2.elementAt(1) != null ? 1 : 2;
      }
   }

   public String getHistoryEredeti(String var1) {
      Vector var2 = (Vector)this.edatas.get(var1);
      if (var2 == null) {
         return "";
      } else {
         String var3 = (String)var2.get(1);
         if (var3 == null) {
            return var2.get(0) != null ? (String)var2.get(0) : "";
         } else {
            return var3;
         }
      }
   }

   public String getOriginalUserHistoryData(String var1) {
      try {
         Vector var2 = (Vector)this.edatas.get(var1);
         if (var2 == null) {
            return "";
         } else {
            String var3 = (String)var2.get(0);
            return var3 == null ? "" : var3;
         }
      } catch (Exception var4) {
         return "";
      }
   }
}
