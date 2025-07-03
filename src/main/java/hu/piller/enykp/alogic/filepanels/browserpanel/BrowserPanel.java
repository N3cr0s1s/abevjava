package hu.piller.enykp.alogic.filepanels.browserpanel;

import hu.piller.enykp.alogic.filepanels.filepanel.FileBusiness;
import hu.piller.enykp.alogic.filepanels.filepanel.FilePanel;
import hu.piller.enykp.alogic.filepanels.folderpanel.FolderBusiness;
import hu.piller.enykp.alogic.filepanels.folderpanel.FolderPanel;
import hu.piller.enykp.interfaces.IFileChooser;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import me.necrocore.abevjava.NecroFile;

import java.awt.Dimension;
import java.io.File;
import java.net.URI;
import java.util.Hashtable;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class BrowserPanel extends JPanel implements IFileChooser, IEventListener {
   private FolderPanel folder_panel;
   private FilePanel file_panel;
   private FileBusiness file_business;
   private FolderBusiness folder_business;
   public boolean is_started = false;

   public BrowserPanel() {
      this.build();
      this.prepare();
   }

   private void build() {
      this.setLayout(new BoxLayout(this, 0));
      this.add(this.get_FolderPanel(), (Object)null);
      this.add(Box.createGlue(), (Object)null);
      this.add(this.get_FilePanel(), (Object)null);
   }

   private JPanel get_FolderPanel() {
      if (this.folder_panel == null) {
         this.folder_panel = new FolderPanel();
         this.folder_panel.setAlignmentY(0.0F);
         this.folder_panel.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
      }

      return this.folder_panel;
   }

   private JPanel get_FilePanel() {
      if (this.file_panel == null) {
         this.file_panel = new FilePanel();
         this.file_panel.setAlignmentY(0.0F);
         this.file_panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
      }

      return this.file_panel;
   }

   private void prepare() {
      this.file_business = this.file_panel.getBusiness();
      this.folder_business = this.folder_panel.getBusiness();
      this.folder_business.setSelectedPath(new NecroFile("."));
      this.folder_business.addEventListener(this);
   }

   public void setSelectedFiles(File[] var1) {
      this.file_panel.getBusiness().setSelectedFiles(var1);
   }

   public Object[] getSelectedFiles() {
      return this.file_panel.getBusiness().getSelectedFiles();
   }

   public void setSelectedFilters(String[] var1) {
      this.file_panel.getBusiness().setSelectedFilters(var1);
   }

   public String[] getSelectedFilters() {
      return this.file_panel.getBusiness().getSelectedFilters();
   }

   public String[] getAllFilters() {
      return this.file_panel.getBusiness().getAllFilters();
   }

   public void addFilters(String[] var1, String[] var2) {
      this.file_panel.getBusiness().addFilters(var1, var2);
   }

   public void removeFilters(String[] var1) {
      this.file_panel.getBusiness().removeFilters(var1);
   }

   public void rescan() {
      this.file_panel.getBusiness().rescan();
   }

   public void hideFilters(String[] var1) {
      this.file_panel.getBusiness().hideFilters(var1);
   }

   public void showFilters(String[] var1) {
      this.file_panel.getBusiness().showFilters(var1);
   }

   public void setSelectedPath(URI var1) {
      this.folder_business.setSelectedPath(new NecroFile(var1.getPath()));
   }

   public boolean isFileSystemBrowserVisible() {
      return this.folder_panel.isVisible();
   }

   public void setFileSystemBrowserVisible(boolean var1) {
      this.folder_panel.setVisible(var1);
      if (var1) {
         this.folder_business.presentPath(this.folder_business.getSelectedPath());
      }

   }

   public Object eventFired(Event var1) {
      Object var2 = var1.getUserData();
      if (var2 instanceof Hashtable) {
         Hashtable var3 = (Hashtable)var2;
         String var4 = (String)var3.get("event");
         if ("path_changed".equalsIgnoreCase(var4)) {
            this.file_business.setSelectedPath((File)var3.get("path"));
         }
      }

      return null;
   }

   public FolderPanel getFolderPanel() {
      return this.folder_panel;
   }

   public FilePanel getFilePanel() {
      return this.file_panel;
   }

   public Hashtable showDialog(String[] var1) {
      return null;
   }
}
