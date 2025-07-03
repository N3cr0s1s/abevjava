package hu.piller.enykp.alogic.filepanels.filepanel.filterpanel;

import hu.piller.enykp.alogic.filepanels.filepanel.DownArrow;
import hu.piller.enykp.alogic.filepanels.filepanel.UpArrow;
import hu.piller.enykp.alogic.fileutil.FileStatusChecker;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.interfaces.ILoadManager;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.interfaces.ISaveManager;
import hu.piller.enykp.util.filelist.EnykFileList;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public abstract class DefaultFilterPanelBusiness implements IFilterPanelBusiness {
   protected boolean exclusive_filter_selection = false;
   protected final Icon up = new UpArrow(7);
   protected final Icon down = new DownArrow(7);
   protected DefaultFilterPanelBusiness.FilterChangeListener filter_listener;
   protected JList lst_file_filters;
   protected final Vector vct_filters = new Vector(16, 8);
   protected final Hashtable ht_filter_names = new Hashtable(16);
   protected DefaultFilterPanelBusiness.ToggleVisibilityController visibility_controller = new DefaultFilterPanelBusiness.ToggleVisibilityController();
   protected boolean filter_type_visible = true;
   protected IFilteredFilesRefresh file_business;
   IFilterPanelLogic filter_panel;
   private final Vector hided_filter_list = new Vector(16, 8);

   public DefaultFilterPanelBusiness(IFilterPanelLogic var1) {
      this.filter_panel = var1;
      this.initialize();
   }

   private void initialize() {
      this.prepare();
   }

   private void prepare() {
      this.filter_listener = new DefaultFilterPanelBusiness.FilterChangeListener();
   }

   public String getFileState(File var1, String var2) {
      FileStatusChecker var3 = FileStatusChecker.getInstance();
      Object var4 = ((IPropertyList)var3).get(new Object[]{"get_filestatus", var1, var2});
      return var4 instanceof Integer ? FileStatusChecker.getStringStatus((Integer)var4) : "(adat nem elérhető)";
   }

   public void setExclusiveFilterSelection(boolean var1) {
      this.exclusive_filter_selection = var1;
   }

   public void setSelectedFilters(String[] var1) {
      DefaultListModel var2 = (DefaultListModel)this.lst_file_filters.getModel();
      int var5 = 0;

      for(int var6 = var2.getSize(); var5 < var6; ++var5) {
         DefaultFilterPanelBusiness.FilterItem var3 = (DefaultFilterPanelBusiness.FilterItem)var2.get(var5);
         String var4 = var3.getFilter();
         int var7 = 0;

         for(int var8 = var1.length; var7 < var8; ++var7) {
            if (var1[var7] == var4) {
               if (this.exclusive_filter_selection) {
                  this.lst_file_filters.setSelectedValue(var3, true);
               }

               var3.setSelected(true);
               break;
            }

            var3.setSelected(false);
         }
      }

      this.lst_file_filters.revalidate();
      this.lst_file_filters.repaint();
   }

   public String[] getSelectedFilters() {
      DefaultListModel var1 = (DefaultListModel)this.lst_file_filters.getModel();
      int var2 = var1.getSize();
      String[] var5 = new String[var2];
      int var3 = 0;

      int var4;
      for(var4 = 0; var3 < var2; ++var3) {
         DefaultFilterPanelBusiness.FilterItem var7 = (DefaultFilterPanelBusiness.FilterItem)var1.get(var3);
         if (var7.isSelected()) {
            var5[var4++] = var7.getFilter();
         }
      }

      String[] var6 = new String[var4];

      for(var3 = 0; var3 < var4; ++var3) {
         var6[var3] = var5[var3];
      }

      return var6;
   }

   public void addFilters(String[] var1, String[] var2) {
      if (var1 != null) {
         String var5 = null;
         String var6 = null;
         int var3;
         if ((var3 = var1.length) > 0) {
            int var4 = 0;

            for(int var8 = 0; var8 < var3; ++var8) {
               if (!this.vct_filters.contains(var1[var8]) && var1[var8] != null) {
                  this.vct_filters.add(var1[var8]);
                  boolean var7 = true;
                  if (var2 == null) {
                     var7 = false;
                  } else if (var2.length - 1 < var8) {
                     var7 = false;
                  }

                  EnykFileList var9 = EnykFileList.getInstance();
                  int var10 = var9.getFilterType(var1[var8]);
                  Object var11 = var9.getFileManagerInstance(var1[var8], var10);
                  if (var10 == 1) {
                     var5 = ((ILoadManager)var11).getFileNameSuffix();
                     var6 = ((ILoadManager)var11).getDescription();
                  } else if (var10 == 2) {
                     var5 = ((ISaveManager)var11).getFileNameSuffix();
                     var6 = ((ISaveManager)var11).getDescription();
                  }

                  if (var5 != null && var5.length() > 0) {
                     var5 = " (" + var5 + ")";
                  } else {
                     var5 = "";
                  }

                  if (var7) {
                     this.ht_filter_names.put(var1[var8], var2[var8] + var5);
                  } else {
                     this.ht_filter_names.put(var1[var8], var6 + var5);
                  }

                  ++var4;
               }
            }

            if (var4 > 0) {
               this.showFilters();
            }
         }
      }

   }

   public void showFilters() {
      DefaultListModel var1 = (DefaultListModel)this.lst_file_filters.getModel();
      var1.clear();
      int var3 = 0;

      for(int var4 = this.vct_filters.size(); var3 < var4; ++var3) {
         String var2 = (String)this.vct_filters.get(var3);
         var1.addElement(new DefaultFilterPanelBusiness.FilterItem(var2, (String)this.ht_filter_names.get(var2), true));
      }

      if (this.vct_filters.size() == 1) {
         String var5 = (String)this.ht_filter_names.get(this.vct_filters.get(0));
         this.file_business.setFilesTitle(var5.substring(0, var5.indexOf("(")));
      } else {
         this.file_business.setFilesTitle("");
      }

      this.setFileFilterTypeVisibility(this.filter_type_visible);
   }

   public String[] getAllFilters() {
      int var1 = this.vct_filters.size();
      String[] var2 = new String[var1];

      for(int var3 = 0; var3 < var1; ++var3) {
         var2[var3] = (String)this.vct_filters.get(var3);
      }

      return var2;
   }

   public void removeFilters(String[] var1) {
      boolean var2 = false;
      int var3 = 0;

      for(int var4 = var1.length; var3 < var4; ++var3) {
         int var5 = 0;

         for(int var6 = this.vct_filters.size(); var5 < var6; ++var5) {
            if (var1[var3] == this.vct_filters.get(var5)) {
               this.vct_filters.remove(var5);
               var2 = true;
               break;
            }
         }
      }

      if (var2) {
         this.showFilters();
      }

   }

   public void setFileBusiness(IFilteredFilesRefresh var1) {
      this.file_business = var1;
   }

   public void hideFilters(String[] var1) {
      boolean var2 = false;
      int var3;
      int var4;
      if (var1 != null && var1.length != 0) {
         var3 = 0;

         for(var4 = var1.length; var3 < var4; ++var3) {
            int var5 = 0;

            for(int var6 = this.vct_filters.size(); var5 < var6; ++var5) {
               if (var1[var3] == this.vct_filters.get(var5)) {
                  if (!this.hided_filter_list.contains(var1[var3])) {
                     this.hided_filter_list.add(var1[var3]);
                  }

                  this.vct_filters.remove(var5);
                  var2 = true;
                  break;
               }
            }
         }
      } else {
         var2 = this.vct_filters.size() > 0;
         var3 = 0;

         for(var4 = this.vct_filters.size(); var3 < var4; ++var3) {
            if (!this.hided_filter_list.contains(this.vct_filters.get(var3))) {
               this.hided_filter_list.add(this.vct_filters.get(var3));
            }
         }

         this.vct_filters.clear();
      }

      if (var2) {
         this.showFilters();
      }

   }

   public void showFilters(String[] var1) {
      boolean var2 = false;
      int var3;
      int var4;
      if (var1 != null && var1.length != 0) {
         var3 = 0;

         for(var4 = var1.length; var3 < var4; ++var3) {
            int var5 = 0;

            for(int var6 = this.hided_filter_list.size(); var5 < var6; ++var5) {
               if (var1[var3] == this.hided_filter_list.get(var5)) {
                  if (!this.vct_filters.contains(var1[var3])) {
                     this.vct_filters.add(var1[var3]);
                  }

                  this.hided_filter_list.remove(var5);
                  var2 = true;
                  break;
               }
            }
         }
      } else {
         var2 = this.hided_filter_list.size() > 0;
         var3 = 0;

         for(var4 = this.hided_filter_list.size(); var3 < var4; ++var3) {
            if (!this.vct_filters.contains(this.hided_filter_list.get(var3))) {
               this.vct_filters.add(this.hided_filter_list.get(var3));
            }
         }

         this.hided_filter_list.clear();
      }

      if (var2) {
         this.showFilters();
      }

   }

   protected class ToggleVisibilityController implements ComponentListener {
      private final Hashtable visibilities = new Hashtable(32);
      private boolean is_visible;
      private boolean is_visible_all;

      public void setComponentVisibility(JComponent var1, boolean var2) {
         if (var1 != null) {
            this.visibilities.put(var1, var2 ? Boolean.TRUE : Boolean.FALSE);
         }

      }

      public boolean getComponentVisibility(JComponent var1) {
         if (var1 != null) {
            Object var2 = this.visibilities.get(var1);
            if (var2 instanceof Boolean) {
               return (Boolean)var2;
            }
         }

         return false;
      }

      public void setVisible(boolean var1) {
         this.setVisible(var1, false);
      }

      public void setVisibleAll(boolean var1) {
         this.setVisible(var1, true);
      }

      private void setVisible(boolean var1, boolean var2) {
         this.is_visible = var1;
         this.is_visible_all = var2;
         this.setVisibile();
      }

      public void setVisibile() {
         Iterator var2 = this.visibilities.keySet().iterator();

         while(true) {
            while(var2.hasNext()) {
               JComponent var1 = (JComponent)var2.next();
               if (this.is_visible_all) {
                  var1.setVisible(this.is_visible);
               } else {
                  var1.setVisible(this.is_visible && this.getComponentVisibility(var1));
               }
            }

            this.is_visible_all = false;
            return;
         }
      }

      public boolean isVisible() {
         return this.is_visible;
      }

      public void componentHidden(ComponentEvent var1) {
      }

      public void componentMoved(ComponentEvent var1) {
      }

      public void componentResized(ComponentEvent var1) {
         this.setVisibile();
      }

      public void componentShown(ComponentEvent var1) {
      }
   }

   protected class StatusItem {
      private String text;
      private Integer id;

      StatusItem(Integer var2, String var3) {
         this.id = var2;
         this.text = var3;
      }

      public Integer getId() {
         return this.id;
      }

      public String toString() {
         return this.text == null ? "(???)" : this.text;
      }
   }

   protected class FiltersCellRenderer implements ListCellRenderer {
      private JCheckBox renderer = GuiUtil.getANYKCheckBox();

      public FiltersCellRenderer() {
      }

      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         if (var2 instanceof DefaultFilterPanelBusiness.FilterItem) {
            DefaultFilterPanelBusiness.FilterItem var6 = (DefaultFilterPanelBusiness.FilterItem)var2;
            this.renderer.setSelected(var6.isSelected());
            this.renderer.setText(var6.toString());
            if (var4) {
               this.renderer.setBackground(var1.getSelectionBackground());
            } else {
               this.renderer.setBackground(var1.getBackground());
            }

            return this.renderer;
         } else {
            return null;
         }
      }
   }

   protected class FilterItem {
      private String filter;
      private String name;
      private boolean selected;

      public FilterItem(String var2) {
         this.setFilter(var2);
      }

      public FilterItem(String var2, String var3) {
         this.setFilter(var2);
         this.setName(var3);
      }

      public FilterItem(String var2, String var3, boolean var4) {
         this.setFilter(var2);
         this.setName(var3);
         this.setSelected(var4);
      }

      public void setFilter(String var1) {
         this.filter = var1;
      }

      public void setName(String var1) {
         this.name = var1;
      }

      public void setFilterName(String var1) {
         this.name = var1;
      }

      public void setSelected(boolean var1) {
         if (DefaultFilterPanelBusiness.this.exclusive_filter_selection) {
            if (var1) {
               this.clearAllSelection(this);
            } else if (DefaultFilterPanelBusiness.this.lst_file_filters.getSelectedValue() == this) {
               var1 = true;
            }
         }

         this.selected = var1;
      }

      private void clearAllSelection(DefaultFilterPanelBusiness.FilterItem var1) {
         if (DefaultFilterPanelBusiness.this.lst_file_filters != null) {
            DefaultListModel var3 = (DefaultListModel)DefaultFilterPanelBusiness.this.lst_file_filters.getModel();
            int var4 = 0;

            for(int var5 = var3.size(); var4 < var5; ++var4) {
               DefaultFilterPanelBusiness.FilterItem var2;
               if (var1 != (var2 = (DefaultFilterPanelBusiness.FilterItem)var3.get(var4))) {
                  var2.setSelected(false);
               }
            }
         }

      }

      public String getFilter() {
         return this.filter;
      }

      public boolean isSelected() {
         return this.selected;
      }

      public String toString() {
         return this.name == null ? "(Szűrő)" : this.name;
      }
   }

   protected class FilterChangeListener implements DocumentListener {
      public void changedUpdate(DocumentEvent var1) {
         this.filter();
      }

      public void insertUpdate(DocumentEvent var1) {
         this.filter();
      }

      public void removeUpdate(DocumentEvent var1) {
         this.filter();
      }

      public void filter() {
         Vector var1 = DefaultFilterPanelBusiness.this.filterFileList(DefaultFilterPanelBusiness.this.file_business.getVct_files());
         DefaultFilterPanelBusiness.this.file_business.setVct_filtered_files(var1);
         DefaultFilterPanelBusiness.this.file_business.showFileList(var1);
      }
   }
}
