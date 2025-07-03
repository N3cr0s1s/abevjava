package hu.piller.enykp.alogic.upgrademanager_v2_0.gui;

import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import java.text.RuleBasedCollator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class UpgradeTableModel extends DefaultTableModel {
   private static RuleBasedCollator collator;
   private String[] header = new String[]{"Kibocsátó", "Név", "Típus", "Verzió", "Telepítendő", "Kategória"};
   private Vector<UpgradeTableEntry> rows = new Vector();
   public int col = -1;
   public int mod;

   public int getColumnCount() {
      return this.header.length;
   }

   public int getRowCount() {
      return this.rows == null ? 0 : this.rows.size();
   }

   public Class<?> getColumnClass(int var1) {
      return var1 == 4 ? Boolean.class : String.class;
   }

   public String getColumnName(int var1) {
      return this.header[var1];
   }

   public boolean isCellEditable(int var1, int var2) {
      if (((UpgradeTableEntry)this.rows.get(var1)).getVersionData().getCategory().equals("Orgresource")) {
         return false;
      } else {
         return var2 == 4;
      }
   }

   public Object getValueAt(int var1, int var2) {
      UpgradeTableEntry var3 = (UpgradeTableEntry)this.rows.get(var1);
      switch(var2) {
      case 0:
         return OrgInfo.getInstance().getOrgShortnameByOrgID(var3.getVersionData().getOrganization());
      case 1:
         return var3.getVersionData().getName();
      case 2:
         return this.mapCategory(var3.getVersionData().getCategory());
      case 3:
         return var3.getVersionData().getVersion();
      case 4:
         return var3.isInstall();
      case 5:
         return var3.getVersionData().getGroup();
      default:
         return "";
      }
   }

   public Vector<UpgradeTableEntry> getRows() {
      return this.rows;
   }

   public void setValueAt(Object var1, int var2, int var3) {
      if (var3 == 4) {
         ((UpgradeTableEntry)this.rows.get(var2)).setInstall((Boolean)var1);
      }

      this.fireTableCellUpdated(var2, var3);
   }

   public void fillTable(Vector<VersionData> var1) {
      this.rows.clear();
      Collections.sort(var1);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         VersionData var3 = (VersionData)var2.next();
         if (!"Framework".equals(var3.getCategory())) {
            UpgradeTableEntry var4 = new UpgradeTableEntry(var3);
            if (var3.getCategory().equals("Orgresource")) {
               var4.setInstall(true);
            }

            this.rows.add(var4);
         }
      }

      this.fireTableRowsInserted(0, this.rows.size());
   }

   public void sortByColumn(int var1, int var2) {
      UpgradeTableModel.UpgradeTableEntryComparator var3 = new UpgradeTableModel.UpgradeTableEntryComparator();
      var3.setField(var1);
      this.col = var1;
      var3.setMode(var2);
      this.mod = var2;
      Collections.sort(this.rows, var3);
   }

   public void reSort() {
      if (this.col != -1) {
         this.sortByColumn(this.col, this.mod);
      }

   }

   public void deselectAll() {
      Iterator var1 = this.rows.iterator();

      while(var1.hasNext()) {
         UpgradeTableEntry var2 = (UpgradeTableEntry)var1.next();
         var2.setInstall(false);
      }

      this.fireTableRowsUpdated(0, this.rows.size());
   }

   public void selectAll() {
      Iterator var1 = this.rows.iterator();

      while(var1.hasNext()) {
         UpgradeTableEntry var2 = (UpgradeTableEntry)var1.next();
         var2.setInstall(true);
      }

      this.fireTableRowsUpdated(0, this.rows.size());
   }

   public VersionData getVersionDataByRowIdx(int var1) {
      return var1 >= this.rows.size() ? null : ((UpgradeTableEntry)this.rows.get(var1)).getVersionData();
   }

   public Vector<VersionData> getVersionDataToInstall() {
      Vector var1 = new Vector();
      Iterator var2 = this.rows.iterator();

      while(var2.hasNext()) {
         UpgradeTableEntry var3 = (UpgradeTableEntry)var2.next();
         if (var3.isInstall()) {
            var1.add(var3.getVersionData());
         }
      }

      return var1;
   }

   public UpgradeTableModel clone() {
      UpgradeTableModel var1 = new UpgradeTableModel();
      var1.rows.addAll(this.rows);
      var1.col = this.col;
      var1.mod = this.mod;
      return var1;
   }

   private String mapCategory(String var1) {
      if ("Template".equals(var1)) {
         return "Nyomtatvány";
      } else if ("Help".equals(var1)) {
         return "Segédlet";
      } else {
         return "Orgresource".equals(var1) ? "Paraméter" : "";
      }
   }

   static {
      try {
         String var0 = "< a< á< b< c< cs< d< dz< dzs< e< é< f< g< gy< h< i< í< j< k< l< ly< m< n< ny< o< ó< ö< ő< p< q< r< s< sz< t< ty< u< ú< ü< ű< v< w< x< y< z< zs";
         collator = new RuleBasedCollator(var0);
         collator.setStrength(1);
      } catch (Exception var1) {
         System.err.println("--> TableSorter: A táblázat elemeinek rendezettsége eltérhet a magyar helyesírás szabályaitól");
         var1.printStackTrace();
         collator = null;
      }

   }

   class UpgradeTableEntryComparator implements Comparator<UpgradeTableEntry> {
      public int ASCENDING = 1;
      public int DESCENDING = -1;
      public int KIBOCSATO = 0;
      public int NEV = 1;
      public int TIPUS = 2;
      public int VERZIO = 3;
      private int field;
      private int mode;

      public void setField(int var1) {
         this.field = var1;
      }

      public int getField() {
         return this.field;
      }

      public void setMode(int var1) {
         this.mode = var1;
      }

      public int compare(UpgradeTableEntry var1, UpgradeTableEntry var2) {
         int var3 = 0;
         switch(this.field) {
         case 0:
            if (UpgradeTableModel.collator != null) {
               var3 = UpgradeTableModel.collator.compare(var1.getVersionData().getOrganization().toLowerCase(), var2.getVersionData().getOrganization().toLowerCase());
            } else {
               var3 = var1.getVersionData().getOrganization().toLowerCase().compareTo(var2.getVersionData().getOrganization().toLowerCase());
            }
            break;
         case 1:
            if (UpgradeTableModel.collator != null) {
               var3 = UpgradeTableModel.collator.compare(var1.getVersionData().getName().toLowerCase(), var2.getVersionData().getName().toLowerCase());
            } else {
               var3 = var1.getVersionData().getName().toLowerCase().compareTo(var2.getVersionData().getName().toLowerCase());
            }
            break;
         case 2:
            if (UpgradeTableModel.collator != null) {
               var3 = UpgradeTableModel.collator.compare(UpgradeTableModel.this.mapCategory(var1.getVersionData().getCategory()), UpgradeTableModel.this.mapCategory(var2.getVersionData().getCategory()));
            } else {
               var3 = UpgradeTableModel.this.mapCategory(var1.getVersionData().getCategory()).compareTo(UpgradeTableModel.this.mapCategory(var2.getVersionData().getCategory()));
            }
            break;
         case 3:
            var3 = var1.getVersionData().getVersion().compareTo(var2.getVersionData().getVersion());
         case 4:
         default:
            break;
         case 5:
            var3 = var1.getVersionData().getGroup().compareTo(var2.getVersionData().getGroup());
         }

         if (this.mode == this.DESCENDING) {
            var3 *= -1;
         }

         return var3;
      }
   }
}
