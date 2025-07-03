package hu.piller.enykp.alogic.kontroll;

import hu.piller.enykp.util.base.Tools;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class DatTableModel extends AbstractTableModel {
   public static final int col_nyomt = 0;
   public static final int col_nev = 1;
   public static final int col_adoszam = 2;
   public static final int col_aazon = 3;
   public static final int col_verzio = 4;
   public static final int col_tol = 5;
   public static final int col_ig = 6;
   public static final int col_megj = 7;
   public static final int col_ment = 8;
   public static final int col_fnev = 9;
   public static final int col_info = 10;
   private String[] columnNames = new String[]{"Nyomt.", "Név", "Adószám", "Adóazonosító jel", "Verzió", "Dátumtól", "Dátumig", "Mejegyzés", "Elmentve", "Filenév", "Információ"};
   private Vector data = new Vector();
   private Vector fdata = new Vector();

   public boolean isCellEditable(int var1, int var2) {
      return false;
   }

   public int getColumnCount() {
      return this.columnNames.length;
   }

   public int getRowCount() {
      return this.fdata.size();
   }

   public String getColumnName(int var1) {
      return this.columnNames[var1];
   }

   public Object getValueAt(int var1, int var2) {
      return ((Vector)this.fdata.elementAt(var1)).elementAt(var2);
   }

   public void addRow(Vector var1) {
      this.data.add(var1);
      this.fdata.add(var1);
   }

   public void empty() {
      this.fdata = new Vector();
   }

   public void filter(Hashtable var1, String var2) {
      this.fdata = new Vector();

      for(int var3 = 0; var3 < this.data.size(); ++var3) {
         Enumeration var4 = var1.keys();
         boolean var5 = true;
         Vector var6 = (Vector)this.data.elementAt(var3);
         if (var2 != null && !((String)var6.elementAt(9)).endsWith(var2)) {
            var5 = false;
         }

         while(var4.hasMoreElements() && var5) {
            Object var7 = var4.nextElement();
            int var8 = Integer.parseInt((String)var7);
            if (!((String)var6.elementAt(var8)).equalsIgnoreCase((String)var1.get(var7))) {
               var5 = false;
            }
         }

         if (var5) {
            this.fdata.add(var6);
         }
      }

   }

   public void reset() {
      this.fdata = this.data;
   }

   public TreeSet getColValues(int var1) {
      TreeSet var2 = new TreeSet();

      try {
         for(int var3 = 0; var3 < this.data.size(); ++var3) {
            var2.add((String)((Vector)this.data.get(var3)).get(var1));
         }
      } catch (NullPointerException var4) {
         Tools.eLog(var4, 0);
      }

      return var2;
   }

   public HashSet getMoreColValues(int[] var1) {
      HashSet var2 = new HashSet();
      String[] var3 = new String[var1.length];

      try {
         for(int var4 = 0; var4 < this.data.size(); ++var4) {
            for(int var5 = 0; var5 < var1.length; ++var5) {
               var3[var5] = (String)((Vector)this.data.get(var4)).get(var1[var5]);
            }

            var2.add(var3);
         }
      } catch (NullPointerException var6) {
         Tools.eLog(var6, 0);
      }

      return var2;
   }
}
