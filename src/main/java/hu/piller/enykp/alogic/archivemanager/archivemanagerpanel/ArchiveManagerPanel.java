package hu.piller.enykp.alogic.archivemanager.archivemanagerpanel;

import hu.piller.enykp.alogic.archivemanager.ArchiveManager;
import hu.piller.enykp.alogic.archivemanager.ArchiveManagerDialog;
import hu.piller.enykp.alogic.archivemanager.ArchiveManagerDialogPanel;
import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.abevfilechooser.ABEVArchivePanel;
import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.archivefilepanel.browserpanel.BrowserPanel;
import hu.piller.enykp.alogic.masterdata.sync.ui.entityfilter.SizeableCBRenderer;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.PropertyList;
import java.awt.Dimension;
import java.io.File;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ArchiveManagerPanel extends JPanel {
   public static final String COMPONENT_CHOOSER_PANEL = "chooser_panel";
   public static final String COMPONENT_ARCHIVE_PANEL = "archive_panel";
   public static final String COMPONENT_MARK_ALL_FILE_BUTTON = "mark_all_file_button";
   public static final String COMPONENT_UNMARK_ALL_FILE_BUTTON = "unmark_all_file_button";
   public static final String COMPONENT_COPY_FILE_BUTTON = "copy_file_button";
   public static final String COMPONENT_PUT_ARCHIVE_BUTTON = "put_archive_button";
   public static final String COMPONENT_GET_ARCHIVE_BUTTON = "get_archive_button";
   public static final String COMPONENT_MARKED_FILES_LIST = "marked_files_list";
   public static final String COMPONENT_ARCHIVED_FILES_LIST = "archived_files_list";
   public static final String COMPONENT_SCP_MARKED_FILES = "scp_marked_files";
   public static final String COMPONENT_SCP_ARCHIVED_FILES = "scp_archived_files";
   private JScrollPane scp_marked_files;
   private JScrollPane scp_archived_files;
   private ABEVArchivePanel chooser_panel;
   private JPanel selection_panel;
   private JPanel buttons_panel2;
   private JPanel selected_files_panel;
   private ABEVArchivePanel archive_panel;
   private JButton btn_mark_file;
   private JButton btn_unmark_file;
   private JButton btn_mark_all_file;
   private JButton btn_unmark_all_file;
   private JButton btn_copy_file;
   private JButton btn_put_archive;
   private JButton btn_get_archive;
   private JList lst_marked_files;
   private JList lst_archived_file;
   private ArchiveManagerBusiness am_business;
   private ArchiveManagerDialogPanel amdp;
   private ArchiveManagerDialog amd;
   private Dimension markBtnSize = new Dimension(100, 25);

   public ArchiveManagerPanel(ArchiveManagerDialogPanel var1, ArchiveManagerDialog var2) {
      this.amdp = var1;
      this.amd = var2;
      this.build();
      this.prepare();
   }

   private void build() {
      this.setLayout(new BoxLayout(this, 0));
      this.setMaximumSize(new Dimension((int)((double)GuiUtil.getScreenW() * 0.8D), (int)((double)GuiUtil.getScreenH() * 0.8D)));
      this.add(this.getFilePanel(), (Object)null);
      this.add(Box.createRigidArea(new Dimension(5, 0)), (Object)null);
      this.add(this.getButtonsPanel2(), (Object)null);
      this.add(Box.createRigidArea(new Dimension(5, 0)), (Object)null);
      this.add(this.getArchivePanel(), (Object)null);
   }

   private void prepare() {
      this.am_business = new ArchiveManagerBusiness(this, this.amdp, this.amd);
      this.customizeFilePanel(this.chooser_panel, ArchiveManager.getSavesRootPath(), "Állományok szűrési feltételei           ", "Állományok", false);
      this.customizeFilePanel(this.archive_panel, ArchiveManager.getArchiveRootPath(), "Archiv állományok szűrési feltételei", "Archív állományok", true);
      this.archive_panel.getFileChooser().getFilePanel().getBusiness().loadFilterSettings("archive_archive_panel");
      this.chooser_panel.getFileChooser().getFilePanel().getBusiness().loadFilterSettings("archive_select_panel");
   }

   private void customizeFilePanel(ABEVArchivePanel var1, String var2, String var3, String var4, boolean var5) {
      if (var1 != null) {
         BrowserPanel var6 = var1.getFileChooser();
         BrowserPanel var7 = (BrowserPanel)var6;
         var7.setFileSystemBrowserVisible(false);
         var7.getFolderPanel().getBusiness().setSelectedPath(new File(var2));
         ((JLabel)((JLabel)var1.getFileChooser().getFilePanel().getFPComponent("filter_title_lbl"))).setText(var3);
         ((JLabel)((JLabel)var1.getFileChooser().getFilePanel().getFPComponent("lbl_file_list"))).setText(var4);
         var1.getFileChooser().getFilePanel().getBusiness().setTask(2);
         var1.getFileChooser().getFilePanel().getBusiness().setArchivPanel(var5);
         ((JTable)var1.getFileChooser().getFilePanel().getFPComponent("files")).setRowSelectionAllowed(false);
         if (((JTable)var1.getFileChooser().getFilePanel().getFPComponent("files")).getColumnCount() > 1) {
            ((JTable)var1.getFileChooser().getFilePanel().getFPComponent("files")).getColumnModel().getColumn(1).setCellEditor(new SizeableCBRenderer());
            ((JTable)var1.getFileChooser().getFilePanel().getFPComponent("files")).getColumnModel().getColumn(1).setCellRenderer(new SizeableCBRenderer());
         }

      }
   }

   private String getProperty(String var1) {
      String var4 = "";
      IPropertyList var2 = PropertyList.getInstance();
      Object var3 = var2.get(var1);
      if (var3 != null) {
         var4 = var3.toString();
      }

      return var4;
   }

   private JPanel getFilePanel() {
      if (this.chooser_panel == null) {
         this.chooser_panel = new ABEVArchivePanel();
         this.chooser_panel.setName("chooser_panel");
      }

      return this.chooser_panel;
   }

   private JPanel getSelectionPanel() {
      if (this.selection_panel == null) {
         this.selection_panel = new JPanel();
         this.selection_panel.setLayout(new BoxLayout(this.getSelectionPanel(), 0));
         this.selection_panel.add(this.getButtonsPanel2(), (Object)null);
      }

      return this.selection_panel;
   }

   private JPanel getArchivePanel() {
      if (this.archive_panel == null) {
         this.archive_panel = new ABEVArchivePanel();
         this.archive_panel.setName("archive_panel");
      }

      return this.archive_panel;
   }

   private JButton getJButton3() {
      if (this.btn_put_archive == null) {
         this.btn_put_archive = new JButton();
         this.btn_put_archive.setText("Levéltárba helyezés");
         this.btn_put_archive.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.btn_put_archive.getHeight()));
         this.btn_put_archive.setAlignmentX(0.0F);
      }

      return this.btn_put_archive;
   }

   private JButton getJButton4() {
      if (this.btn_get_archive == null) {
         this.btn_get_archive = new JButton();
         this.btn_get_archive.setText("Másolat kivétele");
         this.btn_get_archive.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.btn_get_archive.getHeight()));
         this.btn_get_archive.setAlignmentX(0.0F);
      }

      return this.btn_get_archive;
   }

   private JScrollPane getJScrollPane5() {
      if (this.scp_archived_files == null) {
         this.scp_archived_files = new JScrollPane();
         this.scp_archived_files.setViewportView(this.getJList3());
         this.scp_archived_files.setAlignmentX(0.0F);
         this.scp_archived_files.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
      }

      return this.scp_archived_files;
   }

   private JList getJList3() {
      if (this.lst_archived_file == null) {
         this.lst_archived_file = new JList(new DefaultListModel());
      }

      return this.lst_archived_file;
   }

   private JPanel getButtonsPanel2() {
      if (this.buttons_panel2 == null) {
         this.buttons_panel2 = new JPanel();
         this.buttons_panel2.setLayout(new BoxLayout(this.getButtonsPanel2(), 1));
         this.buttons_panel2.add(this.getBtn_mark_all_file(), (Object)null);
         this.buttons_panel2.add(this.getBtn_unmark_all_file(), (Object)null);
      }

      return this.buttons_panel2;
   }

   private JPanel getSelectedFilesPanel() {
      if (this.selected_files_panel == null) {
         JLabel var1 = new JLabel();
         var1.setText("Levéltárba teendők");
         var1.setAlignmentX(0.0F);
         this.selected_files_panel = new JPanel();
         this.selected_files_panel.setLayout(new BoxLayout(this.getSelectedFilesPanel(), 1));
         this.selected_files_panel.add(var1, (Object)null);
         this.selected_files_panel.add(this.getJScrollPane4(), (Object)null);
      }

      return this.selected_files_panel;
   }

   private JScrollPane getJScrollPane4() {
      if (this.scp_marked_files == null) {
         this.scp_marked_files = new JScrollPane();
         this.scp_marked_files.setAlignmentX(0.0F);
         this.scp_marked_files.setViewportView(this.getJList2());
         this.scp_marked_files.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
         this.scp_marked_files.setMinimumSize(new Dimension(100, 100));
      }

      return this.scp_marked_files;
   }

   private JList getJList2() {
      if (this.lst_marked_files == null) {
         this.lst_marked_files = new JList(new DefaultListModel());
      }

      return this.lst_marked_files;
   }

   private JButton getBtn_mark_file() {
      if (this.btn_mark_file == null) {
         this.btn_mark_file = new JButton();
         this.btn_mark_file.setText(">");
         this.btn_mark_file.setToolTipText("Kijelölés");
         this.setBtnSize(this.btn_mark_file);
      }

      return this.btn_mark_file;
   }

   private JButton getBtn_unmark_file() {
      if (this.btn_unmark_file == null) {
         this.btn_unmark_file = new JButton();
         this.btn_unmark_file.setText("<");
         this.btn_unmark_file.setToolTipText("Kijelölés megszüntetése");
         this.setBtnSize(this.btn_unmark_file);
      }

      return this.btn_unmark_file;
   }

   private JButton getBtn_mark_all_file() {
      if (this.btn_mark_all_file == null) {
         this.btn_mark_all_file = new JButton();
         this.btn_mark_all_file.setText("Archiválás");
         this.btn_mark_all_file.setToolTipText("Kijelölt állományok arhiválása");
         this.setBtnSize(this.btn_mark_all_file);
      }

      return this.btn_mark_all_file;
   }

   private JButton getBtn_unmark_all_file() {
      if (this.btn_unmark_all_file == null) {
         this.btn_unmark_all_file = new JButton();
         this.btn_unmark_all_file.setText("Visszavétel");
         this.btn_unmark_all_file.setToolTipText("Kijelölt állományok visszavétele az arhivumból");
         this.setBtnSize(this.btn_unmark_all_file);
      }

      return this.btn_unmark_all_file;
   }

   private JButton getBtn_copy_file() {
      if (this.btn_copy_file == null) {
         this.btn_copy_file = new JButton();
         this.btn_copy_file.setText("Másolás");
         this.btn_copy_file.setToolTipText("Kijelölt állományok másolása az arhívumból");
         this.setBtnSize(this.btn_copy_file);
      }

      return this.btn_copy_file;
   }

   private void setBtnSize(JButton var1) {
      Dimension var2 = new Dimension(GuiUtil.getW(var1, "Visszavétel") + 4, GuiUtil.getCommonItemHeight() + 2);
      var1.setMinimumSize(var2);
      var1.setMaximumSize(var2);
      var1.setPreferredSize(var2);
      var1.setSize(var2);
   }

   public JComponent getAMDComponent(String var1) {
      if ("chooser_panel".equalsIgnoreCase(var1)) {
         return this.chooser_panel;
      } else if ("archive_panel".equalsIgnoreCase(var1)) {
         return this.archive_panel;
      } else if ("copy_file_button".equalsIgnoreCase(var1)) {
         return this.btn_copy_file;
      } else if ("mark_all_file_button".equalsIgnoreCase(var1)) {
         return this.btn_mark_all_file;
      } else if ("unmark_all_file_button".equalsIgnoreCase(var1)) {
         return this.btn_unmark_all_file;
      } else if ("put_archive_button".equalsIgnoreCase(var1)) {
         return this.btn_put_archive;
      } else if ("get_archive_button".equalsIgnoreCase(var1)) {
         return this.btn_get_archive;
      } else if ("marked_files_list".equalsIgnoreCase(var1)) {
         return this.lst_marked_files;
      } else if ("archived_files_list".equalsIgnoreCase(var1)) {
         return this.lst_archived_file;
      } else if ("scp_marked_files".equalsIgnoreCase(var1)) {
         return this.scp_marked_files;
      } else {
         return "scp_archived_files".equalsIgnoreCase(var1) ? this.scp_archived_files : null;
      }
   }
}
