package hu.piller.enykp.datastore;

import hu.piller.enykp.alogic.kihatas.KihatasTableModel;
import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class Kihatasstore {
   CachedCollection cc;
   Integer ikey;
   Hashtable datas;
   BigDecimal brsz_azon;
   public static final int SUMCOLINDEX = 3;

   public Kihatasstore(CachedCollection var1, Integer var2, BigDecimal var3) {
      this.cc = var1;
      this.ikey = var2;
      this.brsz_azon = var3;
   }

   public void set(String var1, KihatasTableModel var2) {
      if (this.datas == null) {
         this.datas = (Hashtable)this.cc.all_kihatas_ht.get(var1);
         if (this.datas == null) {
            this.datas = new Hashtable();
         }
      }

      this.datas.put(var1, var2);
   }

   public KihatasTableModel get(String var1) {
      if (this.datas == null) {
         this.datas = (Hashtable)this.cc.all_kihatas_ht.get(this.ikey);
         if (this.datas == null) {
            this.datas = (Hashtable)this.cc.all_kihatas_ht.get(this.brsz_azon);
         }

         if (this.datas == null) {
            this.datas = new Hashtable();
         }
      }

      KihatasTableModel var3 = (KihatasTableModel)this.datas.get(var1);
      if (var3 == null) {
         var3 = new KihatasTableModel();
         var3.addEmptyRec();
         this.datas.put(var1, var3);
      } else if (!var3.hasEmptyRec()) {
         var3.addEmptyRec();
      }

      return var3;
   }

   public Vector getcols() {
      Vector var1 = new Vector();
      var1.add("fid");
      var1.add("Megállapítás sorsz.");
      var1.add("Adónemlista");
      var1.add("Módosító kihatás érték");
      var1.add("bimo_azon");
      var1.add("prn_azon");
      var1.add("lap");
      var1.add("brsz_azon");
      var1.add("ered. érték");
      var1.add("csoport id");
      var1.add("csoport flag");
      return var1;
   }

   private static BigDecimal getvalue(Object var0) {
      if (var0 == null) {
         return new BigDecimal(0);
      } else {
         BigDecimal var1 = null;

         try {
            var1 = new BigDecimal(var0.toString());
         } catch (Exception var3) {
            var1 = new BigDecimal(0);
         }

         return var1;
      }
   }

   public static void calculatedfield(DefaultTableModel var0, String var1) {
      BigDecimal var2;
      try {
         var2 = new BigDecimal(var1);
      } catch (Exception var4) {
         var2 = new BigDecimal(0);
      }

      var0.setValueAt(var2.toString(), 0, 3);
   }

   public static String getDifferent(String var0, String var1) {
      BigDecimal var2;
      try {
         var2 = new BigDecimal(var1);
      } catch (Exception var6) {
         var2 = new BigDecimal(0);
      }

      BigDecimal var3;
      try {
         var3 = new BigDecimal(var0);
      } catch (Exception var5) {
         var3 = new BigDecimal(0);
      }

      var3 = var2.subtract(var3);
      return var3.toString();
   }

   public static void updateRec(DefaultTableModel var0, int var1, String var2, String var3, int var4, String var5, String var6) {
      System.out.println("updateRec = " + var1 + " " + var2 + " " + var3 + " " + var4 + " " + var5 + " " + var6);
      var0.setValueAt(var2, var1, 8);
      var0.setValueAt(var5 + "@" + var3, var1, 0);
      var0.setValueAt(var4 + "", var1, 6);
   }

   public static void updateCsopRec(KihatasTableModel var0, int var1, String var2, String var3, int var4, String var5, String var6) {
      System.out.println("updateCsopRec = " + var1 + " " + var2 + " " + var3 + " " + var4 + " " + var5 + " " + var6);

      try {
         var0.setValueAt(var2, var1, 8);
         var0.setValueAt(var5 + "@" + var3, var1, 0);
         var0.setValueAt(var4 + "", var1, 6);
         var0.setValueAt(var6, var1, 2);
         var0.setValueAt("C", var1, 10);
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public static void seteredetiErtek(DefaultTableModel var0, String var1) {
      Vector var2 = var0.getDataVector();

      for(int var3 = 0; var3 < var0.getRowCount(); ++var3) {
         Vector var4 = (Vector)var2.elementAt(var3);
         var4.setElementAt(var1, 8);
      }

   }
}
