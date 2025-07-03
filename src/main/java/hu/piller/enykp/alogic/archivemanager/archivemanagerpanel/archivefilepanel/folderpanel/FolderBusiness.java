package hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.archivefilepanel.folderpanel;

import hu.piller.enykp.alogic.filepanels.folderpanel.LeftArrow;
import hu.piller.enykp.alogic.filepanels.folderpanel.RightArrow;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.eventsupport.DefaultEventSupport;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import me.necrocore.abevjava.NecroFile;

import java.awt.Component;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class FolderBusiness implements IEventSupport {
   public static final boolean debugOn = false;
   private FolderPanel folder_panel;
   private final DefaultEventSupport des = new DefaultEventSupport();
   private File current_path;
   private final FileSystemView file_system_view = FileSystemView.getFileSystemView();
   private JComboBox cbo_drives;
   private JTree tre_folders;
   private final FolderBusiness.FolderRenderer folder_renderer = new FolderBusiness.FolderRenderer();
   private final FolderBusiness.FolderFileFilter folder_file_filter = new FolderBusiness.FolderFileFilter();
   private JTextField txt_path;
   private JLabel lbl_splitter;
   private JPanel folder_panel2;
   private boolean is_path_setting;
   private final Icon left_arrow = new LeftArrow(7);
   private final Icon right_arrow = new RightArrow(7);
   private Vector directories;
   private Hashtable icon_cache;
   private Hashtable drv_path_cache;
   private boolean is_acquiring_drive;

   public void addEventListener(IEventListener var1) {
      this.des.addEventListener(var1);
   }

   public void removeEventListener(IEventListener var1) {
      this.des.removeEventListener(var1);
   }

   public Vector fireEvent(Event var1) {
      return this.des.fireEvent(var1);
   }

   public FolderBusiness(FolderPanel var1) {
      this.folder_panel = var1;
      this.prepare();
   }

   private void prepare() {
      this.cbo_drives = (JComboBox)this.folder_panel.getFPComponent("drives");
      this.tre_folders = (JTree)this.folder_panel.getFPComponent("folders");
      this.txt_path = (JTextField)this.folder_panel.getFPComponent("path");
      this.lbl_splitter = (JLabel)this.folder_panel.getFPComponent("splitter");
      this.folder_panel2 = (JPanel)this.folder_panel.getFPComponent("folder_panel");
      this.setFolderVisibility(false);
   }

   public void setFolderVisibility(boolean var1) {
      this.folder_panel2.setVisible(var1);
      this.lbl_splitter.setIcon(var1 ? this.left_arrow : this.right_arrow);
   }

   private void acquireDriveList(JComboBox var1) {
      this.is_acquiring_drive = true;

      try {
         File[] var2 = File.listRoots();
         DefaultComboBoxModel var3 = (DefaultComboBoxModel)var1.getModel();
         var3.removeAllElements();
         int var5 = 0;

         for(int var6 = var2.length; var5 < var6; ++var5) {
            String var4;
            if (this.directories.contains(var2[var5])) {
               File var7 = (File)this.directories.get(this.directories.indexOf(var2[var5]));
               var4 = this.file_system_view.getSystemDisplayName(var7);
            } else if (this.file_system_view.isFloppyDrive(var2[var5])) {
               var4 = "Hajlékony lemez " + var2[var5].getPath();
            } else {
               var4 = this.file_system_view.getSystemDisplayName(var2[var5]);
               if (var4.equals("")) {
                  var4 = var2[var5].getPath();
               }
            }

            var3.addElement(new FolderBusiness.ListItem(var2[var5], this.getDriveIcon(var2[var5]), var4));
         }

         var1.repaint();
         this.showMessage("FolderBusiness.acquireDriveList end ");
      } finally {
         this.is_acquiring_drive = false;
      }
   }

   private Icon getDriveIcon(File var1) {
      File var2 = this.getDrive(var1);
      if (this.directories.contains(var2)) {
         File var3 = (File)this.directories.get(this.directories.indexOf(var2));
         if (this.icon_cache.get(var3) != null) {
            return (Icon)this.icon_cache.get(var3);
         } else {
            Icon var4 = this.file_system_view.getSystemIcon(var3);
            if (var4 != null) {
               this.icon_cache.put(var3, var4);
            }

            return var4;
         }
      } else {
         return this.file_system_view.isFloppyDrive(var2) ? UIManager.getIcon("FileView.floppyDriveIcon") : UIManager.getIcon("FileView.hardDriveIcon");
      }
   }

   public void presentPath(File var1) {
      if (this.folder_panel.isVisible()) {
         this.showDrive(this.cbo_drives, var1);
         this.showFolders(this.tre_folders, var1);
         this.showPath(this.txt_path, var1);
      }

   }

   private void showDrive(JComboBox var1, File var2) {
      File var3 = this.getDrive(var2);
      DefaultComboBoxModel var4 = (DefaultComboBoxModel)var1.getModel();
      int var6 = 0;

      for(int var7 = var4.getSize(); var6 < var7; ++var6) {
         Object var5;
         if ((var5 = var4.getElementAt(var6)).equals(var3)) {
            var4.setSelectedItem(var5);
            var1.repaint();
            break;
         }
      }

   }

   private File getDrive(File var1) {
      File var2 = var1;
      if (System.getProperty("os.name").toLowerCase().indexOf("win") < 0) {
         return var1;
      } else {
         while(!this.file_system_view.isDrive(var2)) {
            var2 = this.file_system_view.getParentDirectory(var2);
         }

         return var2;
      }
   }

   private void showFolders(JTree var1, File var2) {
      DefaultTreeModel var3 = (DefaultTreeModel)var1.getModel();
      Object var4 = var3.getRoot();
      if (var4 == null) {
         var3.setRoot(new DefaultMutableTreeNode(new FolderBusiness.ListItem(this.getDrive(var2), (Icon)null)));
         this.buildTree((DefaultMutableTreeNode)var3.getRoot(), var2);
      } else if (!this.getDrive(var2).equals(((FolderBusiness.ListItem)((DefaultMutableTreeNode)var4).getUserObject()).getItem())) {
         var3.setRoot(new DefaultMutableTreeNode(new FolderBusiness.ListItem(this.getDrive(var2), (Icon)null)));
         this.buildTree((DefaultMutableTreeNode)var3.getRoot(), var2);
      } else {
         this.parseTreeToLastSameAndBuild((DefaultMutableTreeNode)var3.getRoot(), var2);
      }

      var4 = var3.getRoot();
      var1.expandPath(new TreePath(((DefaultMutableTreeNode)var4).getPath()));
   }

   private void parseTreeToLastSameAndBuild(DefaultMutableTreeNode var1, File var2) {
      File var4 = null;
      DefaultMutableTreeNode var5 = var1;
      File[] var3 = this.getPathList(var2, (File)null);
      int var10 = 1;

      for(int var11 = var3.length; var10 < var11; ++var10) {
         var4 = var3[var10];
         Enumeration var7 = var5.children();
         boolean var9 = false;
         DefaultMutableTreeNode var6 = null;

         while(var7.hasMoreElements()) {
            Object var8;
            if ((var8 = var7.nextElement()) instanceof DefaultMutableTreeNode && (var8 = (var6 = (DefaultMutableTreeNode)var8).getUserObject()) instanceof FolderBusiness.ListItem && (var8 = ((FolderBusiness.ListItem)var8).getItem()) instanceof File && var8.equals(var4)) {
               var9 = true;
               break;
            }
         }

         if (!var9) {
            this.buildTree(var5, var4);
            break;
         }

         var5 = var6;
      }

      if (var5.getChildCount() > 0 && ((DefaultMutableTreeNode)var5.getFirstChild()).getUserObject() == null && var4 != null) {
         this.buildTree(var5, var4);
      } else {
         this.tre_folders.setSelectionPath(new TreePath(var5.getPath()));
         this.tre_folders.scrollPathToVisible(new TreePath(var5.getPath()));
      }
   }

   private void buildTree(DefaultMutableTreeNode var1, File var2) {
      DefaultMutableTreeNode var5 = var1;
      File[] var3 = this.getPathList(var2, (File)((FolderBusiness.ListItem)var1.getUserObject()).getItem());
      int var7 = 0;

      for(int var8 = var3.length; var7 < var8; ++var7) {
         var5.removeAllChildren();
         DefaultMutableTreeNode[] var4 = this.acquireNodeChilds(var3[var7]);
         DefaultMutableTreeNode var6 = null;
         int var9 = 0;

         for(int var10 = var4.length; var9 < var10; ++var9) {
            var5.add(var4[var9]);
            if (var7 + 1 < var8 && var3[var7 + 1].equals(((FolderBusiness.ListItem)var4[var9].getUserObject()).getItem())) {
               var6 = var4[var9];
            }
         }

         if (var6 == null) {
            break;
         }

         var5 = var6;
      }

      this.tre_folders.setSelectionPath(new TreePath(var5.getPath()));
      this.tre_folders.scrollPathToVisible(new TreePath(var5.getPath()));
   }

   private DefaultMutableTreeNode[] acquireNodeChilds(File var1) {
      File[] var2 = var1.listFiles(this.folder_file_filter);
      DefaultMutableTreeNode[] var3;
      if (var2 != null) {
         int var4;
         var3 = new DefaultMutableTreeNode[var4 = var2.length];
         int var6 = 0;

         for(int var7 = var4; var6 < var7; ++var6) {
            DefaultMutableTreeNode var8 = new DefaultMutableTreeNode(new FolderBusiness.ListItem(var2[var6], (Icon)null));
            File[] var5;
            if ((var5 = var2[var6].listFiles(this.folder_file_filter)) != null && var5.length > 0) {
               DefaultMutableTreeNode var9 = new DefaultMutableTreeNode((Object)null);
               var8.add(var9);
            }

            var3[var6] = var8;
         }
      } else {
         var3 = null;
      }

      return var3;
   }

   private void showPath(JTextField var1, File var2) {
      var1.setText(var2.getPath());
   }

   public void refreshFolderInfos() {
      this.acquireDriveList(this.cbo_drives);
      this.presentPath(this.getSelectedPath());
   }

   public File getSelectedPath() {
      return new NecroFile(this.current_path, "");
   }

   public void setSelectedPath(File var1) {
      if (!this.is_path_setting) {
         this.is_path_setting = true;
         if (var1 != null && !var1.equals(this.current_path)) {
            try {
               this.current_path = var1.getCanonicalFile();
               this.presentPath(this.current_path);
               this.des.fireEvent(this, "update", "path_changed", "path", this.current_path);
            } catch (IOException var3) {
               Tools.eLog(var3, 0);
            }
         }

         this.is_path_setting = false;
      }

   }

   private File[] getPathList(File var1, File var2) {
      Vector var3 = new Vector(16, 16);

      for(File var5 = var1; var5 != null; var5 = var5.getParentFile()) {
         var3.add(var5);
      }

      int var7;
      if (var2 != null) {
         for(var7 = var3.size() - 1; var7 >= 0 && !var3.get(var7).equals(var2); --var7) {
         }

         int var8 = var7 + 1;

         for(int var9 = var3.size(); var8 < var9; ++var8) {
            var3.remove(var7 + 1);
         }
      }

      File[] var4;
      int var6;
      if ((var6 = var3.size()) > 0) {
         var4 = new NecroFile[var6];

         for(var7 = 0; var7 < var6; ++var7) {
            var4[var7] = (File)var3.get(var6 - var7 - 1);
         }
      } else {
         var4 = new NecroFile[0];
      }

      return var4;
   }

   private void showMessage(String var1) {
   }

   private class FolderFileFilter implements FileFilter {
      private FolderFileFilter() {
      }

      public boolean accept(File var1) {
         return var1.isDirectory();
      }

      // $FF: synthetic method
      FolderFileFilter(Object var2) {
         this();
      }
   }

   private class FolderRenderer extends DefaultTreeCellRenderer {
      private FolderRenderer() {
      }

      public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
         super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
         if (var6 == 0) {
            this.setIcon(FolderBusiness.this.getDriveIcon(FolderBusiness.this.getSelectedPath()));
         } else if (var5) {
            this.setIcon(this.getClosedIcon());
         } else if (var4) {
            this.setIcon(this.getOpenIcon());
         } else {
            this.setIcon(this.getClosedIcon());
         }

         return this;
      }

      // $FF: synthetic method
      FolderRenderer(Object var2) {
         this();
      }
   }

   private class ListItemRenderer extends JLabel implements ListCellRenderer {
      public ListItemRenderer() {
         this.setHorizontalAlignment(2);
         this.setVerticalAlignment(0);
         this.setOpaque(true);
      }

      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         if (var2 instanceof FolderBusiness.ListItem) {
            FolderBusiness.ListItem var6 = (FolderBusiness.ListItem)var2;
            this.setIcon(var6.getIcon());
            this.setText(var6.toString());
            if (var4) {
               this.setBackground(var1.getSelectionBackground());
            } else {
               this.setBackground(var1.getBackground());
            }

            return this;
         } else {
            return null;
         }
      }
   }

   private class ListItem {
      private Object item;
      private Object item2;
      private Icon icon;
      private Object text;

      public ListItem(Object var2, Icon var3) {
         this.setItem(var2);
         this.setIcon(var3);
      }

      public ListItem(Object var2, Icon var3, Object var4) {
         this.setItem(var2);
         this.setIcon(var3);
         this.setText(var4);
      }

      public ListItem(Object var2, Icon var3, Object var4, Object var5) {
         this.setItem(var2);
         this.setSecondItem(var5);
         this.setIcon(var3);
         this.setText(var4);
      }

      public void setItem(Object var1) {
         this.item = var1;
      }

      public Object getItem() {
         return this.item;
      }

      public void setSecondItem(Object var1) {
         this.item2 = var1;
      }

      public Object getSecondItem() {
         return this.item2;
      }

      public void setIcon(Icon var1) {
         this.icon = var1;
      }

      public Icon getIcon() {
         return this.icon;
      }

      public void setText(Object var1) {
         this.text = var1;
      }

      public Object getText() {
         return this.text;
      }

      public boolean equals(Object var1) {
         if (this.item == null) {
            return var1 == null || var1.equals(this.item);
         } else if (!(var1 instanceof FolderBusiness.ListItem)) {
            return this.item.equals(var1);
         } else {
            return var1 == this || this.item.equals(((FolderBusiness.ListItem)var1).getItem());
         }
      }

      public String toString() {
         if (this.text != null) {
            return this.text.toString();
         } else if (this.item instanceof File) {
            File var1 = (File)this.item;
            return var1.getName().equals("") ? var1.getPath() : var1.getName();
         } else {
            return this.item != null ? this.item.toString() : "???";
         }
      }
   }
}
