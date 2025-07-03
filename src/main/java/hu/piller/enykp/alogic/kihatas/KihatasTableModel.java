package hu.piller.enykp.alogic.kihatas;

import java.math.BigDecimal;
import java.util.Vector;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class KihatasTableModel extends Vector<KihatasRecord> implements TableModel {
   protected EventListenerList listenerList = new EventListenerList();
   private KihatasRecord kr0 = new KihatasRecord();

   public int getRowCount() {
      return this.size();
   }

   public int getColumnCount() {
      return KihatasRecord.fieldCount;
   }

   public String getColumnName(int var1) {
      return this.kr0.getFieldName(var1);
   }

   public Class<?> getColumnClass(int var1) {
      return String.class;
   }

   public boolean isCellEditable(int var1, int var2) {
      return false;
   }

   public Object getValueAt(int var1, int var2) {
      KihatasRecord var3 = (KihatasRecord)this.get(var1);
      Object var4 = var3.getValue(var2);
      return var4;
   }

   public void setValueAt(Object var1, int var2, int var3) {
      KihatasRecord var4 = (KihatasRecord)this.get(var2);
      var4.setValue(var1, var3);
      this.fireTableDataChanged();
   }

   public void addTableModelListener(TableModelListener var1) {
      this.listenerList.add(TableModelListener.class, var1);
   }

   public void removeTableModelListener(TableModelListener var1) {
      this.listenerList.remove(TableModelListener.class, var1);
   }

   public void addEmptyRec() {
      KihatasRecord var1 = new KihatasRecord();
      this.add(var1);
   }

   public boolean hasEmptyRec() {
      if (this.size() == 0) {
         return false;
      } else {
         KihatasRecord var1 = (KihatasRecord)this.get(this.size() - 1);
         return var1.isEmpty();
      }
   }

   public static KihatasTableModel make_copy(KihatasTableModel var0) {
      KihatasTableModel var1 = new KihatasTableModel();

      for(int var2 = 0; var2 < var0.size(); ++var2) {
         KihatasRecord var3 = (KihatasRecord)var0.get(var2);
         var1.add(var3.make_copy());
      }

      return var1;
   }

   public BigDecimal getSzumma() {
      BigDecimal var1 = new BigDecimal(0);

      for(int var2 = 0; var2 < this.size(); ++var2) {
         KihatasRecord var3 = (KihatasRecord)this.get(var2);
         if (var3.getMegallapitasVector().vannemtorolt()) {
            var1 = var1.add(var3.getModositoOsszegValue());
         }
      }

      return var1;
   }

   public void seteredetiErtek(String var1) {
      for(int var2 = 0; var2 < this.size(); ++var2) {
         KihatasRecord var3 = (KihatasRecord)this.get(var2);
         var3.setEredetiErtek(var1);
      }

   }

   public String getSzumma(String var1) {
      BigDecimal var2 = this.getSzumma();

      BigDecimal var3;
      try {
         var3 = new BigDecimal(var1);
      } catch (Exception var5) {
         var3 = new BigDecimal(0);
      }

      var3 = var3.add(var2);
      return var3.toString();
   }

   public void fireTableDataChanged() {
      this.fireTableChanged(new TableModelEvent(this));
   }

   public void fireTableChanged(TableModelEvent var1) {
      Object[] var2 = this.listenerList.getListenerList();

      for(int var3 = var2.length - 2; var3 >= 0; var3 -= 2) {
         if (var2[var3] == TableModelListener.class) {
            ((TableModelListener)var2[var3 + 1]).tableChanged(var1);
         }
      }

   }

   public void updateRec(int var1, String var2, String var3, int var4, String var5, String var6, String var7) {
      KihatasRecord var8 = (KihatasRecord)this.get(var1);
      var8.update(var2, var3, var4, var5, var6, var7);
   }

   public void updateRec(int var1, String var2, String var3, int var4, String var5, String var6, String var7, String var8, String var9) {
      KihatasRecord var10 = (KihatasRecord)this.get(var1);
      var10.update(var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public boolean vannemtorolt() {
      for(int var1 = 0; var1 < this.size(); ++var1) {
         KihatasRecord var2 = (KihatasRecord)this.get(var1);
         if (var2.vannemtorolt()) {
            return true;
         }
      }

      return false;
   }

   public void calculatedfield(String var1) {
      KihatasRecord var2 = (KihatasRecord)this.get(0);
      var2.setModositoOsszeg(var1);
   }

   public void done_delete(int var1) {
      KihatasRecord var2 = (KihatasRecord)this.get(var1);
      if (!var2.isEmpty()) {
         MegallapitasVector var3 = var2.getMegallapitasVector();
         var3.done_delete();
         if (var3.size() == 0) {
            this.remove(var1);
         }

         if (!this.hasEmptyRec()) {
            this.addEmptyRec();
         }

      }
   }

   public void setCsopjel(String var1) {
      for(int var2 = 0; var2 < this.size(); ++var2) {
         KihatasRecord var3 = (KihatasRecord)this.get(var2);
         var3.setCsoportFlag(var1);
      }

   }

   public String getEredeti(Vector var1) {
      return this.getEredeti(var1, 0);
   }

   public String getEredeti(Vector var1, int var2) {
      String var3 = this.getKihatasEredetiErtek();
      if (!"".equals(var3)) {
         return var3;
      } else {
         String var4 = (String)var1.get(1);
         if (var4 == null) {
            if (var1.get(0) != null) {
               return (String)var1.get(0);
            } else {
               return var2 == 1 ? "" : "0";
            }
         } else {
            return var4;
         }
      }
   }

   public String getKihatasEredetiErtek() {
      return ((KihatasRecord)this.get(0)).getEredetiErtek();
   }

   public String ComputeRevValue() {
      if (!this.vannemtorolt()) {
         return "";
      } else {
         String var1 = this.getKihatasEredetiErtek();
         if ("".equals(var1)) {
            var1 = "0";
         }

         BigDecimal var2 = this.getSzumma();
         BigDecimal var3 = new BigDecimal(var1);
         BigDecimal var4 = var3.add(var2);
         return var4.toString();
      }
   }

   public boolean maySimple() {
      for(int var1 = 0; var1 < this.size(); ++var1) {
         KihatasRecord var2 = (KihatasRecord)this.get(var1);
         if (1 < var2.getMegallapitasVector().size()) {
            return false;
         }
      }

      return true;
   }

   public String checkMertekegyseg(String var1) {
      String var2 = "";
      int var3 = this.hasEmptyRec() ? this.size() - 1 : this.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         KihatasRecord var5 = (KihatasRecord)this.get(var4);
         if (!var1.equals(var5.getMertekegyseg())) {
            return "A mezőhöz tartozó mértékegység nem egyezik meg a kihatásban található mértékegységgel!\nSablonérték: " + var1 + " Adatbázisérték: " + var5.getMertekegyseg() + "\n";
         }
      }

      return var2;
   }

   public String checkBtablajel(String var1) {
      String var2 = "";
      int var3 = this.hasEmptyRec() ? this.size() - 1 : this.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         KihatasRecord var5 = (KihatasRecord)this.get(var4);
         if (!var1.equals(var5.getBtablaJel())) {
            return "A mezőhöz tartozó btáblajel nem egyezik meg a kihatásban található btáblajellel!\nSablonérték: " + var1 + " Adatbázisérték: " + var5.getBtablaJel() + "\n";
         }
      }

      return var2;
   }

   public void setMertekegyseg(String var1) {
      for(int var2 = 0; var2 < this.size(); ++var2) {
         KihatasRecord var3 = (KihatasRecord)this.get(var2);
         var3.setMertekegyseg(var1);
      }

   }

   public void setBtablajel(String var1) {
      for(int var2 = 0; var2 < this.size(); ++var2) {
         KihatasRecord var3 = (KihatasRecord)this.get(var2);
         var3.setBtablaJel(var1);
      }

   }

   public void setHistory(Vector var1) {
      for(int var2 = 0; var2 < this.size(); ++var2) {
         KihatasRecord var3 = (KihatasRecord)this.get(var2);
         var3.setHistory(var1);
      }

   }

   public void setAdattipusKod(String var1) {
      for(int var2 = 0; var2 < this.size(); ++var2) {
         KihatasRecord var3 = (KihatasRecord)this.get(var2);
         var3.setAdattipusKod(var1);
      }

   }

   public String getAdattipusKod() {
      try {
         KihatasRecord var1 = (KihatasRecord)this.get(0);
         return var1.getAdattipusKod();
      } catch (Exception var2) {
         return "";
      }
   }

   public void deleteAll() {
      for(int var1 = 0; var1 < this.getRowCount(); ++var1) {
         this.done_delete(var1);
      }

   }
}
