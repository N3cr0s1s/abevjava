package hu.piller.enykp.alogic.filepanels.filepanel.filterpanel;

import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TableFilterPanelBusiness extends DefaultFilterPanelBusiness implements IFilterPanelBusiness {
   private boolean visibility = true;
   private boolean visible = true;
   protected JLabel lbl_file_filter;
   protected JScrollPane scp_file_filter;
   protected JButton btn_clear_filters;
   protected JLabel lbl_toggle_filter;
   protected JLabel lbl_filter_title;
   private Vector vct_files;
   JTableFilter table_filter;
   public static final String SETTINGS_TABLE_NAME = "tablefilter";
   public static final String ROW_PREFIX = "row_";
   public static final String COL_PREFIX = "col_";
   public static final String SORT_PREFIX = "sort_";
   public static final String FEATURES_VISIBLE = "visible";

   public TableFilterPanelBusiness(IFilterPanelLogic var1) {
      super(var1);
      this.prepare();
   }

   private void prepare() {
      this.lst_file_filters = (JList)this.filter_panel.getComponent("file_filter");
      this.btn_clear_filters = (JButton)this.filter_panel.getComponent("clear_filters");
      this.lbl_toggle_filter = (JLabel)this.filter_panel.getComponent("file_filters_toggle_btn");
      this.lbl_filter_title = (JLabel)this.filter_panel.getComponent("filter_title_lbl");
      this.lbl_file_filter = (JLabel)this.filter_panel.getComponent("file_filters_lbl");
      this.scp_file_filter = (JScrollPane)this.filter_panel.getComponent("file_filters_scp");
      this.table_filter = (JTableFilter)this.filter_panel.getComponent("table_filter");
      this.prepareFilters();
      this.prepareFilterTitle();
   }

   private void prepareFilterTitle() {
      this.lbl_filter_title.addMouseListener(new MouseListener() {
         public void mouseClicked(MouseEvent var1) {
         }

         public void mouseEntered(MouseEvent var1) {
         }

         public void mouseExited(MouseEvent var1) {
         }

         public void mousePressed(MouseEvent var1) {
            TableFilterPanelBusiness.this.toggleFilterVisibility();
         }

         public void mouseReleased(MouseEvent var1) {
         }
      });
      this.lbl_toggle_filter.setText("");
      this.lbl_toggle_filter.setIcon(this.up);
      this.lbl_toggle_filter.setFocusable(false);
      this.lbl_toggle_filter.addMouseListener(new MouseListener() {
         public void mouseClicked(MouseEvent var1) {
         }

         public void mouseEntered(MouseEvent var1) {
         }

         public void mouseExited(MouseEvent var1) {
         }

         public void mousePressed(MouseEvent var1) {
            TableFilterPanelBusiness.this.toggleFilterVisibility();
         }

         public void mouseReleased(MouseEvent var1) {
         }
      });
   }

   public void setFilterVisibility(boolean var1) {
      this.visibility = var1;
      this.visibility_controller.setComponentVisibility(this.btn_clear_filters, var1);
      this.visibility_controller.setComponentVisibility(this.table_filter, var1);
   }

   public boolean getFilterVisibility() {
      return this.visibility;
   }

   public void setFileFilterTypeVisibility(boolean var1) {
      this.filter_type_visible = var1;
      this.visibility_controller.setComponentVisibility(this.lbl_file_filter, var1 && this.needFilterVisible());
      this.visibility_controller.setComponentVisibility(this.scp_file_filter, var1 && this.needFilterVisible());
   }

   public boolean getFileFilterTypeVisibility() {
      return this.filter_type_visible;
   }

   public void initials(Object var1) {
      this.table_filter.init(var1);
   }

   public void setDefaultFilterValues(int var1, String var2) {
      this.table_filter.setInitialValue(var1, var2);
   }

   public void refresh(Object var1) {
      this.table_filter.setSourcemodel(var1);
   }

   public void saveLastFilterValues(String var1) {
      SettingsStore var2 = SettingsStore.getInstance();
      Vector var3 = this.table_filter.getFilterValues();

      int var4;
      for(var4 = 0; var4 < var3.size(); ++var4) {
         String var5 = (String)var3.elementAt(var4);
         var2.set("tablefilter_" + var1, "row_" + var4, var5);
      }

      var2.set("tablefilter_" + var1, "visible", Boolean.toString(this.visible));
      if (!"InstalledForms".equals(var1) && !"abevnewpanel".equals(var1) && !"emptyprint".equals(var1) && !"FormArchiver".equals(var1)) {
         var3 = this.table_filter.getColsWidth();

         for(var4 = 0; var4 < var3.size(); ++var4) {
            Object[] var8 = (Object[])((Object[])var3.elementAt(var4));
            String var6 = var8[0] + "|" + var8[1] + "|" + var8[2];
            var2.set("tablefilter_" + var1, "col_" + var4, var6);
         }

         String[] var7 = this.table_filter.getSortInfo();
         var2.set("tablefilter_" + var1, "sort_0", var7[0]);
         var2.set("tablefilter_" + var1, "sort_1", var7[1]);
      }
   }

   public void loadLastFilterValues(String var1) {
      try {
         Hashtable var2 = SettingsStore.getInstance().get("tablefilter_" + var1);
         if (var2 == null) {
            this.visible = true;
            this.setVisibleBySettings();
            if (!"InstalledForms".equals(var1) && !"abevnewpanel".equals(var1) && !"emptyprint".equals(var1) && !"FormArchiver".equals(var1)) {
               this.table_filter.setReorderingAllowed(true);
               return;
            }

            return;
         }

         Vector var3 = new Vector();
         var3.setSize(var2.size() - 1);
         Vector var4 = new Vector();
         var4.setSize(var2.size() - 1);
         Enumeration var5 = var2.keys();

         String var6;
         while(var5.hasMoreElements()) {
            var6 = (String)var5.nextElement();
            if (var6.equalsIgnoreCase("visible")) {
               this.visible = Boolean.valueOf((String)var2.get(var6));
            } else if (var6.startsWith("row_")) {
               var3.setElementAt((String)var2.get(var6), Integer.valueOf(var6.substring("row_".length())));
            } else if (var6.startsWith("col_")) {
               var4.setElementAt((String)var2.get(var6), Integer.valueOf(var6.substring("col_".length())));
            }
         }

         var6 = "tablefilter_" + var1;
         this.table_filter.setFilterValues(var3, var4, var6, "col_", var1);
         String[] var7 = new String[]{(String)var2.get("sort_0"), (String)var2.get("sort_1")};
         this.table_filter.setSortInfo(var7);
         this.setVisibleBySettings();
      } catch (Exception var8) {
         System.out.println("Szűrőpanel beállítása nem sikerült!");
      }

   }

   private void toggleFilterVisibility() {
      this.visible = !this.getVisible();
      this._setVisible(this.visible);
      this.setFilterIcon(this.visible);
      JPanel var1 = (JPanel)this.filter_panel;
      var1.setMaximumSize(new Dimension(Integer.MAX_VALUE, var1.getPreferredSize().height));
      var1.setMinimumSize(var1.getPreferredSize());
   }

   public void setFilterIcon(boolean var1) {
      this.lbl_toggle_filter.setIcon(var1 ? this.up : this.down);
   }

   public void storeFileInfo(Vector var1) {
      JTableFilter var10000 = this.table_filter;
      JTableFilter.flag = true;
      this.table_filter.clearFilters();
      var10000 = this.table_filter;
      JTableFilter.flag = false;
      this.table_filter.filter();
   }

   public void resetFileInfo(Vector var1) {
      this.vct_files = var1;
   }

   public void prepareFilters() {
      this.lst_file_filters.setModel(new DefaultListModel());
      this.lst_file_filters.setCellRenderer(new DefaultFilterPanelBusiness.FiltersCellRenderer());
      this.lst_file_filters.setLayoutOrientation(2);
      this.lst_file_filters.setVisibleRowCount(2);
      this.lst_file_filters.addMouseListener(new MouseListener() {
         public void mouseClicked(MouseEvent var1) {
            int var2;
            if ((var2 = TableFilterPanelBusiness.this.lst_file_filters.locationToIndex(var1.getPoint())) > -1) {
               DefaultFilterPanelBusiness.FilterItem var3 = (DefaultFilterPanelBusiness.FilterItem)TableFilterPanelBusiness.this.lst_file_filters.getModel().getElementAt(var2);
               var3.setSelected(!var3.isSelected());
               TableFilterPanelBusiness.this.lst_file_filters.repaint();
               TableFilterPanelBusiness.this.file_business.rescan();
            }

         }

         public void mouseEntered(MouseEvent var1) {
         }

         public void mouseExited(MouseEvent var1) {
         }

         public void mousePressed(MouseEvent var1) {
         }

         public void mouseReleased(MouseEvent var1) {
         }
      });
      this.btn_clear_filters.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TableFilterPanelBusiness.this.clearFilters();
         }
      });
   }

   public void clearFilters() {
      this.storeFileInfo(this.vct_files);
   }

   public Vector filterFileList(Vector var1) {
      Vector var2 = new Vector(256);
      return var2;
   }

   private String getString(Object var1) {
      return var1 == null ? "" : var1.toString();
   }

   public void setEnabled(boolean var1) {
      this.btn_clear_filters.setEnabled(var1);
   }

   public void setVisible(boolean var1) {
      this.visible = !var1;
      this.toggleFilterVisibility();
   }

   public void setVisibleBySettings() {
      this._setVisible(this.visible);
      this.setFilterIcon(this.visible);
      JPanel var1 = (JPanel)this.filter_panel;
      var1.setMaximumSize(new Dimension(Integer.MAX_VALUE, var1.getPreferredSize().height));
      var1.setMinimumSize(var1.getPreferredSize());
   }

   private void _setVisible(boolean var1) {
      this.setFileFilterTypeVisibility(this.filter_type_visible);
      this.visibility_controller.setVisible(var1);
   }

   private boolean needFilterVisible() {
      return this.lst_file_filters.getModel().getSize() > 1;
   }

   public boolean getVisible() {
      return this.visible;
   }
}
