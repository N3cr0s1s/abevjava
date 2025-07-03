package hu.piller.enykp.alogic.upgrademanager_v2_0.gui;

import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.event.ComponentProcessingEvent;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class InstallProgressTableModel extends DefaultTableModel {
   private static final String[] header = new String[]{"Kibocsátó", "Név", "Típus", "Fázis", "Előrehaladás"};
   private static final String[][] emptyTable = new String[][]{{"", "", "", "", ""}, {"", "", "", "", ""}, {"", "", "", "", ""}, {"", "", "", "", ""}, {"", "", "", "", ""}, {"", "", "", "", ""}, {"", "", "", "", ""}, {"", "", "", "", ""}, {"", "", "", "", ""}, {"", "", "", "", ""}};

   public InstallProgressTableModel() {
      super(10, 10);
      this.setDataVector(emptyTable, header);
   }

   public int getColumnCount() {
      return header.length;
   }

   public int getRowCount() {
      return 10;
   }

   public Class<?> getColumnClass(int var1) {
      return String.class;
   }

   public String getColumnName(int var1) {
      return header[var1];
   }

   public boolean isCellEditable(int var1, int var2) {
      return false;
   }

   private int getRowIndexByOrgComp(String var1, String var2) {
      int var3 = -1;

      for(int var4 = 0; var4 < 10; ++var4) {
         String var5 = (String)((Vector)this.getDataVector().elementAt(var4)).elementAt(0);
         if (var5.equals(var1) && this.getValueAt(var4, 1).equals(var2)) {
            var3 = var4;
            break;
         }
      }

      return var3;
   }

   private int getFirstEmptyRowIndex() {
      int var1 = -1;

      for(int var2 = 0; var2 < 10; ++var2) {
         String var3 = (String)((Vector)this.getDataVector().elementAt(var2)).elementAt(0);
         if (var3.equals("")) {
            var1 = var2;
            break;
         }
      }

      return var1;
   }

   public synchronized void add(ComponentProcessingEvent var1) {
      int var2 = this.getFirstEmptyRowIndex();
      if (var2 != -1) {
         this.setValueAt(var1.getOrganization(), var2, 0);
         this.setValueAt(var1.getName(), var2, 1);
         this.setValueAt(this.translateCategory(var1.getCategory()), var2, 2);
         this.setValueAt("Letöltés", var2, 3);
         this.fireTableRowsUpdated(var2, var2);
      }

   }

   public synchronized void update(ComponentProcessingEvent var1) {
      int var2 = this.getRowIndexByOrgComp(var1.getOrganization(), var1.getName());
      if (var2 != -1) {
         if (var1.getState() == 3) {
            this.setValueAt("Telepítés", var2, 3);
            this.fireTableCellUpdated(var2, 3);
         } else if (var1.getState() == 99) {
            this.setValueAt(var1.getMessage(), var2, 4);
            this.fireTableCellUpdated(var2, 4);
         }

      }
   }

   public Object getValueAt(int var1, int var2) {
      if (var2 == 0) {
         String var3 = (String)((Vector)this.getDataVector().elementAt(var1)).elementAt(0);
         return OrgInfo.getInstance().getOrgShortnameByOrgID(var3);
      } else {
         return super.getValueAt(var1, var2);
      }
   }

   private String translateCategory(String var1) {
      String var2 = "";
      if ("Template".equals(var1)) {
         var2 = "Nyomtatvány";
      } else if ("Help".equals(var1)) {
         var2 = "Segédlet";
      } else if ("Framework".equals(var1)) {
         var2 = "Keretrendszer";
      } else if ("Orgresource".equals(var1)) {
         var2 = "Erőforrás";
      }

      return var2;
   }

   public synchronized void removeEvent(ComponentProcessingEvent var1) {
      int var2 = this.getRowIndexByOrgComp(var1.getOrganization(), var1.getName());
      if (var2 != -1) {
         for(int var3 = 0; var3 < 5; ++var3) {
            this.setValueAt("", var2, var3);
         }

         this.fireTableRowsUpdated(var2, var2);
      }
   }
}
