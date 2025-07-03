package hu.piller.enykp.alogic.filepanels;

import hu.piller.enykp.alogic.filepanels.browserpanel.BrowserPanel;
import hu.piller.enykp.alogic.filepanels.datafileoperations.Operations;
import hu.piller.enykp.alogic.filepanels.filepanel.FileBusiness;
import hu.piller.enykp.alogic.filepanels.filepanel.FilePanel;
import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.TableFilterPanel;
import hu.piller.enykp.alogic.fileutil.DatastoreKeyToXml;
import hu.piller.enykp.alogic.fileutil.FileStatusChecker;
import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.IFileChooser;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.eventsupport.DefaultEventSupport;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import hu.piller.enykp.util.icon.ENYKIconSet;
import me.necrocore.abevjava.NecroFile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class ABEVOpenPanel extends JDialog implements IEventSupport, IFileChooser, IEventListener, AncestorListener {
   private DefaultEventSupport des = new DefaultEventSupport();
   private JButton btn12;
   private JButton btn_ok;
   private JButton btn11;
   private static final String S_CODE = "ABVOP8754";
   private static final String S_FILE = "abev_open_panel";
   private static final String OPEN_PANEL_NAME = "abev_open_panel";
   private static final String KEY_FILTER_VISIBLE = "filter_visible";
   private static final String KEY_FOLDER_VISIBLE = "folder_visible";
   private static final String VAL_YES = "igen";
   private static final String VAL_NO = "nem";
   public static final String UNDEFINED_MODE = "Definiálatlan állomány listázó mód";
   public static final String MODE_OPEN = "open";
   public static final String MODE_OPEN_MULTI = "open_multi";
   public static final String MODE_OPEN_IMPORT = "open_import";
   public static final String MODE_OPEN_WITHOUT_BUTTON_BAR = "open_without_button_bar";
   private static final Set<String> MODES = new HashSet();
   private boolean checkStatus = true;
   private JTable tbl_files;
   private IPropertyList gui_info;
   public static final String OPEN_DIALOG_TITLE = "Megnyitás";
   public static final String OPEN_ADD_MULTI_DIALOG_TITLE = "Nyomtatvány kiválasztása";
   public static final String OPEN_XML_CHECK_DIALOG_TITLE = "XML állomány kiválasztása";
   private Hashtable result;
   private File path = new NecroFile(this.getProperty("prop.sys.root"), this.getProperty("prop.usr.saves"));
   private String[] filters = new String[]{"inner_data_loader_v1"};
   private String mode = "open";
   private Dimension loadedDimension = null;
   private Point loadedStartingPoint = null;
   private Hashtable<Integer, String> rowMatch = null;
   private JPanel mainPanel = new JPanel();
   private BrowserPanel panel;

   public void setPath(File var1) {
      this.path = var1;
   }

   public void setFilters(String[] var1) {
      this.filters = var1;
   }

   public void setMode(String var1, Hashtable<Integer, String> var2) {
      if (!MODES.contains(var1)) {
         throw new RuntimeException("Definiálatlan állomány listázó mód");
      } else {
         this.mode = var1;
         this.setRowMatch(var2);
      }
   }

   public void setMode(String var1) {
      this.setMode(var1, (Hashtable)null);
   }

   public void setRowMatch(Hashtable<Integer, String> var1) {
      this.rowMatch = var1;
   }

   private String trimSuffix(String var1, String var2) {
      int var3 = var1.lastIndexOf(var2);
      return var3 == -1 ? var1 : var1.substring(0, var3);
   }

   private String getProperty(String var1) {
      String var4 = "";
      IPropertyList var2 = this.getMasterPropertyList();
      if (var2 != null) {
         Object var3 = var2.get(var1);
         if (var3 != null) {
            var4 = var3.toString();
         }
      }

      return var4;
   }

   private void setProperty(String var1, Object var2) {
      IPropertyList var3 = this.getMasterPropertyList();
      if (var3 != null) {
         var3.set(var1, var2);
      }

   }

   private void setButtonsEnabled(boolean var1) {
   }

   public void ancestorAdded(AncestorEvent var1) {
   }

   public void ancestorMoved(AncestorEvent var1) {
   }

   public void ancestorRemoved(AncestorEvent var1) {
      this.panel.getFilePanel().getBusiness().setFilesTitleLocked(false);
      this.panel.getFilePanel().getBusiness().setSelectedPathLocked(false);
   }

   public void addEventListener(IEventListener var1) {
      this.des.addEventListener(var1);
   }

   public void removeEventListener(IEventListener var1) {
      this.des.removeEventListener(var1);
   }

   public Vector fireEvent(Event var1) {
      return this.des.fireEvent(var1);
   }

   public Object eventFired(Event var1) {
      Object var2 = var1.getUserData();
      if (var2 instanceof Hashtable) {
         Hashtable var3 = (Hashtable)var2;
         var2 = var3.get("event");
         if (!var2.equals("after_start")) {
            if (var2.equals("double_click_on_file")) {
               this.btn_ok.doClick(0);
            } else if (var2.equals("single_click_on_file")) {
               this.changeReadOnlySelection();
            } else if (var2.equals("select_all_key_on_file")) {
               this.changeSelectAll();
            }
         }
      } else if (var2 instanceof String) {
         String var4 = (String)var2;
         if (!var4.equalsIgnoreCase("beforeopen") && !var4.equalsIgnoreCase("readonly")) {
            if (var4.equalsIgnoreCase("afteropen")) {
               this.btn_ok.setEnabled(true);
            } else if (var4.equalsIgnoreCase("nocheckbox")) {
               this.btn12.setVisible(false);
            }
         }
      }

      return null;
   }

   public void setSelectedFiles(File[] var1) {
      this.panel.setSelectedFiles(var1);
   }

   public Object[] getSelectedFiles() {
      return this.panel.getSelectedFiles();
   }

   public void setSelectedFilters(String[] var1) {
      this.panel.setSelectedFilters(var1);
   }

   public String[] getSelectedFilters() {
      return this.panel.getSelectedFilters();
   }

   public String[] getAllFilters() {
      return this.panel.getAllFilters();
   }

   public void addFilters(String[] var1, String[] var2) {
      this.panel.addFilters(var1, var2);
   }

   public void removeFilters(String[] var1) {
      this.panel.removeFilters(var1);
   }

   public void rescan() {
      this.panel.rescan();
   }

   public void hideFilters(String[] var1) {
      this.panel.hideFilters(var1);
   }

   public void showFilters(String[] var1) {
      this.panel.showFilters(var1);
   }

   public void setSelectedPath(URI var1) {
      this.path = new NecroFile(var1);
   }

   public ABEVOpenPanel() {
      super(MainFrame.thisinstance);
      Collections.addAll(MODES, new String[]{"open", "open_multi", "open_import", "open_without_button_bar"});
      this.setName("abevopenpanel");
      this.build();
      this.prepare();
      this.initLogic();
   }

   private void build() {
      this.setSize(new Dimension(700, 430));
      this.mainPanel.setLayout(new BoxLayout(this.mainPanel, 1));
      this.panel = new BrowserPanel();
      this.panel.getFilePanel().getBusiness().setTask(2);
      this.panel.getFilePanel().getBusiness().addEventListener(this);
      this.mainPanel.setLayout(new BorderLayout(5, 5));
      this.mainPanel.add(this.panel, "Center");
      this.mainPanel.add(this.getButtonsPanel(), "South");
      this.mainPanel.setPreferredSize(new Dimension(700, 430));
      this.getContentPane().add(this.mainPanel);
      this.setTitle("Megnyitás");
      this.btn11 = (JButton)this.panel.getFilePanel().getFPComponent("select_all");
      this.btn12 = (JButton)this.panel.getFilePanel().getFPComponent("deselect_all");
   }

   private void prepare() {
      this.panel.getFilePanel().getBusiness().setEnykFileFilters(TemplateChecker.getInstance());
      this.panel.getFilePanel().getBusiness().setNeedTemplateFilter(false);
      this.setModal(true);
   }

   private void initLogic() {
      BrowserPanel var1 = (BrowserPanel)this.getFileChooser();
      var1.is_started = true;
   }

   private void changeReadOnlySelection() {
      Object[] var1 = this.panel.getSelectedFiles();
      if (var1 != null && var1.length > 0 && var1[0] instanceof Object[]) {
         var1 = (Object[])((Object[])var1[0]);
         if (var1.length > 2 && var1[2] instanceof Hashtable) {
            Hashtable var2 = (Hashtable)var1[2];
            Object var3 = var2.get("state");
            if (var3 instanceof String) {
               String var4 = (String)var3;
               if (this.checkStatus) {
                  this.btn_ok.setEnabled(!var4.equalsIgnoreCase("Elküldött"));
               }
            }
         }
      }

   }

   private void changeSelectAll() {
      if (this.tbl_files.getSelectionModel().getSelectionMode() == 2 && this.tbl_files.getModel().getRowCount() > 0) {
         this.btn_ok.setEnabled(true);
      }

   }

   private JPanel getButtonsPanel() {
      this.btn_ok = new JButton("Megnyitás");
      this.btn_ok.setName("btn_ok");
      JButton var2 = new JButton("Mégsem");
      var2.setName("btn_cancel");
      JPanel var1 = new JPanel();
      FlowLayout var3 = new FlowLayout(2, 5, 5);
      var1.setLayout(var3);
      var1.add(this.btn_ok);
      var1.add(var2);
      this.btn_ok.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Object[] var2 = ABEVOpenPanel.this.getSelectedFiles();
            if (var2 != null && var2.length > 0) {
               ABEVOpenPanel.this.result = new Hashtable();
               ABEVOpenPanel.this.result.put("read_only", FileStatusChecker.getInstance().getFileReadOnlyState(ABEVOpenPanel.this.getSelectedFileState()));
               ABEVOpenPanel.this.result.put("file_status", ABEVOpenPanel.this.getSelectedFileState());
               ABEVOpenPanel.this.result.put("function_read_only", Boolean.FALSE);
               ABEVOpenPanel.this.result.put("selected_files", ABEVOpenPanel.this.getSelectedFiles());
               ABEVOpenPanel.this.setButtonsEnabled(false);
               ABEVOpenPanel.this.panel.getFilePanel().getBusiness().saveFilterSettings("abev_open_panel_" + ABEVOpenPanel.this.mode);
               ABEVOpenPanel.this.saveSettings();
               ABEVOpenPanel.this.setDialogVisible(false);
            } else {
               GuiUtil.showMessageDialog(ABEVOpenPanel.this, "Nem választott ki nyomtatványt !", "Nyomtatvány megnyitása", 1);
            }
         }
      });
      var2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ABEVOpenPanel.this.result = null;
            ABEVOpenPanel.this.panel.getFilePanel().getBusiness().saveFilterSettings("abev_open_panel_" + ABEVOpenPanel.this.mode);
            ABEVOpenPanel.this.saveSettings();
            ABEVOpenPanel.this.setDialogVisible(false);
         }
      });
      this.setDefaultCloseOperation(2);
      this.installKeyBindingForButton(var2, 27);
      ENYKIconSet var4 = ENYKIconSet.getInstance();
      this.setButtonIcon(this.btn_ok, "anyk_ellenorzes", var4);
      this.setButtonIcon(var2, "anyk_megse", var4);
      return var1;
   }

   private String getSelectedFileState() {
      String var1 = "(Állapot ismeretlen)";
      if (this.panel != null) {
         Object[] var2 = this.panel.getSelectedFiles();
         if (var2 != null && var2.length > 0) {
            Object var3 = var2[0];
            if (var3 instanceof Object[]) {
               var2 = (Object[])((Object[])var3);
               if (var2.length > 2) {
                  var3 = var2[2];
                  if (var3 instanceof Hashtable) {
                     Hashtable var4 = (Hashtable)var3;
                     var3 = var4.get("state");
                     var1 = var3 == null ? var1 : var3.toString();
                  }
               }
            }
         }
      }

      return var1;
   }

   private void installKeyBindingForButton(final JButton var1, int var2) {
      String var3 = KeyStroke.getKeyStroke(var2, 0).toString() + "Pressed";
      JPanel var4 = this.mainPanel;
      var4.getInputMap(2).put(KeyStroke.getKeyStroke(var2, 0), var3);
      var4.getActionMap().put(var3, new AbstractAction() {
         public void actionPerformed(ActionEvent var1x) {
            if (var1.isVisible() && var1.isEnabled()) {
               var1.doClick();
            }

         }
      });
   }

   private void setButtonIcon(JButton var1, String var2, ENYKIconSet var3) {
      var1.setIcon(var3.get(var2));
   }

   public IFileChooser getFileChooser() {
      return this.panel;
   }

   private IPropertyList getMasterPropertyList() {
      return PropertyList.getInstance();
   }

   private IPropertyList getGuiInfo() {
      if (this.gui_info == null) {
         IPropertyList var1 = this.getMasterPropertyList();
         if (var1 != null) {
            Object var2 = var1.get("gui_info");
            if (var2 instanceof IPropertyList) {
               this.gui_info = (IPropertyList)var2;
            }
         }
      }

      return this.gui_info;
   }

   private void setDialogVisible(boolean var1) {
      this.setVisible(var1);
   }

   public Hashtable showDialog() {
      this.checkStatus = true;
      if (this.mode.equalsIgnoreCase("open")) {
         this.setTitle("Megnyitás");
         this.btn11.setVisible(true);
         this.btn12.setVisible(true);
         this.panel.getFilePanel().getBusiness().setTask(2);
         this.panel.getFilePanel().getBusiness().setButtonExecutor(new ABEVOpenPanel.OpenButtonActions(this.panel.getFilePanel()));
         this.addFilters(this.filters, (String[])null);
         ((BrowserPanel)this.getFileChooser()).setSelectedPath(this.path.toURI());
      } else if (this.mode.equalsIgnoreCase("open_without_button_bar")) {
         this.setTitle("Megnyitás");
         this.btn11.setVisible(false);
         this.btn12.setVisible(false);
         this.panel.getFilePanel().getBusiness().setTask(2);
         this.panel.getFilePanel().getBusiness().setButtonExecutor(new ABEVOpenPanel.OpenButtonActions(this.panel.getFilePanel()));
         this.addFilters(this.filters, (String[])null);
         ((BrowserPanel)this.getFileChooser()).setSelectedPath(this.path.toURI());
      } else if (this.mode.equalsIgnoreCase("open_multi")) {
         this.checkStatus = false;
         this.setTitle("Nyomtatvány kiválasztása");
         this.btn11.setVisible(true);
         this.btn12.setVisible(false);
         this.panel.getFilePanel().getBusiness().setTask(9);
         this.panel.getFilePanel().getBusiness().setButtonExecutor(new ABEVOpenPanel.OpenButtonActions(this.panel.getFilePanel()));
         this.addFilters(this.filters, (String[])null);
         ((BrowserPanel)this.getFileChooser()).setSelectedPath(this.path.toURI());
      } else {
         if (!this.mode.equalsIgnoreCase("open_import")) {
            System.out.println("Definiálatlan állomány listázó mód");
            throw new RuntimeException("Definiálatlan állomány listázó mód");
         }

         this.checkStatus = false;
         this.setTitle("Nyomtatvány kiválasztása");
         this.btn11.setVisible(true);
         this.btn12.setVisible(false);
         this.panel.getFilePanel().getBusiness().setTask(9, this.rowMatch);
         this.panel.getFilePanel().getBusiness().setButtonExecutor(new ABEVOpenPanel.OpenButtonActions(this.panel.getFilePanel()));
         this.hideFilters((String[])null);
         this.addFilters(this.filters, (String[])null);
         this.setSelectedFilters(this.filters);
         File var1 = this.panel.getFilePanel().getBusiness().getSelectedPath();
         if (var1 != null && var1.getAbsolutePath().equalsIgnoreCase(this.path.getAbsolutePath())) {
            this.panel.getFilePanel().getBusiness().refreshFileInfos();
         } else {
            ((BrowserPanel)this.getFileChooser()).setSelectedPath(this.path.toURI());
         }
      }

      this.panel.getFilePanel().getBusiness().setDefaultFilterValues(0, "Módosítható");
      this.panel.getFilePanel().getBusiness().loadFilterSettings("abev_open_panel_" + this.mode);
      this.panel.getFolderPanel().setVisible(false);
      this.pack();
      MainFrame.thisinstance.setGlassLabel((String)null);
      this.loadSettings();
      if (this.loadedDimension == null) {
         this.loadedDimension = new Dimension(700, 500);
      }

      this.setSize(this.loadedDimension);
      this.setPreferredSize(this.loadedDimension);
      if (this.loadedStartingPoint != null) {
         this.setLocation(this.loadedStartingPoint);
      } else {
         this.setLocationRelativeTo(MainFrame.thisinstance);
      }

      this.setVisible(true);
      this.mainPanel.remove(this);
      this.dispose();
      return this.result;
   }

   private String getCopyFilename(File var1) {
      int var2 = var1.getAbsolutePath().toLowerCase().indexOf(".frm.enyk");
      if (var2 == -1) {
         return var1.getAbsolutePath();
      } else {
         int var4 = 0;

         String var3;
         File var5;
         do {
            ++var4;

            try {
               Thread.sleep(10L);
            } catch (InterruptedException var7) {
            }

            var3 = var1.getAbsolutePath().substring(0, var2) + "_" + var4 + ".frm.enyk";
            var5 = new NecroFile(var3);
         } while(var5.exists());

         return var3;
      }
   }

   private void saveSettings() {
      SettingsStore var1 = SettingsStore.getInstance();
      var1.set("filepanel_open_settings", "width", this.getWidth() + "");
      var1.set("filepanel_open_settings", "height", this.getHeight() + "");
      var1.set("filepanel_open_settings", "xPos", this.getLocation().x + "");
      var1.set("filepanel_open_settings", "yPos", this.getLocation().y + "");
   }

   private void loadSettings() {
      int var1;
      int var2;
      try {
         GraphicsDevice var7 = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
         var1 = var7.getDisplayMode().getWidth();
         var2 = var7.getDisplayMode().getHeight();
      } catch (Exception var11) {
         var1 = 700;
         var2 = 500;
         var11.printStackTrace();
      }

      Hashtable var12 = SettingsStore.getInstance().get("filepanel_open_settings");
      if (var12 != null) {
         int var5;
         int var6;
         try {
            var5 = Integer.parseInt((String)var12.get("width"));
            var6 = Integer.parseInt((String)var12.get("height"));
         } catch (Exception var10) {
            var5 = var1;
            var6 = var2;
            var10.printStackTrace();
         }

         int var3;
         int var4;
         try {
            var3 = Integer.parseInt((String)var12.get("xPos"));
            var4 = Integer.parseInt((String)var12.get("yPos"));
         } catch (Exception var9) {
            var3 = 5;
            var4 = 5;
            var9.printStackTrace();
         }

         if (var3 < 0) {
            var5 -= var3;
            var3 = 0;
         }

         if (var4 < 0) {
            var6 -= var4;
            var4 = 0;
         }

         if (var3 + var5 > var1) {
            var3 -= var3 + var5 - var1;
            if (var3 < 0) {
               var5 += var3;
               var3 = 0;
            }
         }

         if (var4 + var6 > var2) {
            var4 -= var4 + var6 - var2;
            if (var4 < 0) {
               var6 += var4;
               var4 = 0;
            }
         }

         this.loadedDimension = new Dimension(var5, var6);
         this.loadedStartingPoint = new Point(var3, var4);
      }
   }

   private class OpenButtonActions extends FileBusiness.ButtonExecutor {
      private FileBusiness business;

      public OpenButtonActions(FilePanel var2) {
         super(var2);
         this.business = var2.getBusiness();
         ABEVOpenPanel.this.tbl_files = (JTable)var2.getFPComponent("files");
         ABEVOpenPanel.this.tbl_files.setName("tbl_files");
         GuiUtil.setTableColWidth(ABEVOpenPanel.this.tbl_files);
      }

      public void b12Clicked() {
         Object[] var1 = ABEVOpenPanel.this.getSelectedFiles();
         if (var1 != null && var1.length > 0) {
            ABEVOpenPanel.this.result = new Hashtable();
            ABEVOpenPanel.this.result.put("read_only", Boolean.TRUE);
            ABEVOpenPanel.this.result.put("file_status", ABEVOpenPanel.this.getSelectedFileState() + " - Megnyitva csak olvasásra ");
            ABEVOpenPanel.this.result.put("function_read_only", Boolean.TRUE);
            ABEVOpenPanel.this.result.put("selected_files", ABEVOpenPanel.this.getSelectedFiles());
            ABEVOpenPanel.this.setButtonsEnabled(false);
            ABEVOpenPanel.this.panel.getFilePanel().getBusiness().saveFilterSettings("abev_open_panel_" + ABEVOpenPanel.this.mode);
            ABEVOpenPanel.this.saveSettings();
            ABEVOpenPanel.this.setDialogVisible(false);
         } else {
            GuiUtil.showMessageDialog(ABEVOpenPanel.this, "Nem választott ki nyomtatványt !", "Nyomtatvány megnyitása", 1);
         }
      }

      public void b11Clicked() {
         Object[] var1 = ABEVOpenPanel.this.getSelectedFiles();

         try {
            if (var1 != null && var1.length > 0) {
               Object[] var2 = (Object[])((Object[])var1[0]);
               String var3 = "";
               String var4 = "";
               int var5 = ((File)var2[0]).getAbsolutePath().toLowerCase().indexOf(".frm.enyk");
               if (var5 > -1) {
                  var3 = ABEVOpenPanel.this.getCopyFilename((File)var2[0]);

                  boolean var6;
                  do {
                     var6 = true;
                     var4 = (String)JOptionPane.showInputDialog(this.file_panel, "Indulhat a másolat készítés?\n A másolat neve:                                                                                   ", "Másolat készítés indítása", 3, (Icon)null, (Object[])null, var3);
                     if (var4 == null) {
                        return;
                     }

                     if (var4.equalsIgnoreCase(((File)var2[0]).getAbsolutePath())) {
                        GuiUtil.showMessageDialog(this.file_panel, "Önmagára nem másolható a fájl! Kérjük módosítsa a nevét!", "Hibás fájlnév", 0);
                        var6 = false;
                     } else if (!DatastoreKeyToXml.htmlCut(var4).equals(var4)) {
                        GuiUtil.showMessageDialog(this.file_panel, "A fájlnév nem megengedett karaktert tartalmaz ( &,<,>,',\" ), kérjük módosítsa!", "Hibás fájlnév", 0);
                        var6 = false;
                     } else if ((new NecroFile(var4)).exists()) {
                        if (JOptionPane.showConfirmDialog(this.file_panel, "Ilyen nevű állomány már létezik? Felülírja?", "Állománynév", 0) == 1) {
                           var6 = false;
                        } else {
                           var6 = true;
                        }
                     }
                  } while(!var6);
               }

               File var14 = new NecroFile(var4);
               Object[] var7 = (Object[])((Object[])var1[0]);
               Object var8 = var7[0];
               Object var9 = var7[1];
               if (var8 instanceof File) {
                  File var10 = (File)var8;
                  if (Tools.copyFile4Masolatkeszites(var10, var14) > 0) {
                     ABEVOpenPanel.this.panel.getFilePanel().getBusiness().saveFilterSettings("abev_open_panel_" + ABEVOpenPanel.this.mode);
                     this.business.storeFileInfo();
                     this.business.refreshFileInfos();
                     ABEVOpenPanel.this.panel.getFilePanel().getBusiness().loadFilterSettings("abev_open_panel_" + ABEVOpenPanel.this.mode);
                     this.business.setSelectedRow(var14);
                     GuiUtil.showMessageDialog(this.file_panel, "Másolás befejeződött.\nÚj állomány: " + var14, "Másolat készítés", 1);
                     if (ABEVOpenPanel.this.getSelectedFiles() == null) {
                        try {
                           TableFilterPanel var11 = (TableFilterPanel)ABEVOpenPanel.this.panel.getFilePanel().getFPComponent("file_filter_panel");
                           var11.getBusinessHandler().clearFilters();
                        } catch (Exception var12) {
                        }

                        ABEVOpenPanel.this.panel.getFilePanel().getBusiness().saveFilterSettings("abev_open_panel_" + ABEVOpenPanel.this.mode);
                        this.business.storeFileInfo();
                        this.business.refreshFileInfos();
                        ABEVOpenPanel.this.panel.getFilePanel().getBusiness().loadFilterSettings("abev_open_panel_" + ABEVOpenPanel.this.mode);
                        this.business.setSelectedRow(var14);
                     }

                     ABEVOpenPanel.this.btn_ok.setEnabled(true);
                     ABEVOpenPanel.this.btn_ok.doClick();
                     ABEVOpenPanel.this.btn_ok.setEnabled(false);
                     return;
                  }

                  GuiUtil.showMessageDialog(this.file_panel, "Másolás nem sikerült !", "Másolat készítés", 0);
                  ABEVOpenPanel.this.setButtonsEnabled(true);
                  return;
               }
            }

            GuiUtil.showMessageDialog(this.file_panel, "Másolat készítéshez válasszon ki állományt !", "Másolat készítése", 1);
            ABEVOpenPanel.this.setButtonsEnabled(true);
         } catch (Exception var13) {
            GuiUtil.showMessageDialog(this.file_panel, "Hiba a másolatkészítés közben: " + var13.getMessage(), "Másolat készítése", 0);
            ABEVOpenPanel.this.setButtonsEnabled(true);
         }

      }
   }

   private class Open2ButtonActions extends FileBusiness.ButtonExecutor {
      private JLabel lbl_file_list_title;
      private JTextField txt_path;
      private FileBusiness business;
      private File enyk_path = new NecroFile(ABEVOpenPanel.this.getProperty("prop.usr.root"), ABEVOpenPanel.this.getProperty("prop.usr.saves"));
      private File outer_path;
      private File current_path;
      private File other_path = null;

      public Open2ButtonActions(FilePanel var2) {
         super(var2);
         this.lbl_file_list_title = (JLabel)var2.getFPComponent("files_title_lbl");
         this.txt_path = (JTextField)var2.getFPComponent("path_txt");
         this.business = var2.getBusiness();
         if (this.txt_path.getText().length() > 0) {
            this.outer_path = new NecroFile(this.txt_path.getText());
            if (!this.enyk_path.toString().equalsIgnoreCase(this.outer_path.toString())) {
               this.lbl_file_list_title.setText("Külső mappa adat állományai");
            }
         }

      }

      public void b11Clicked() {
         if (this.enyk_path != null && this.enyk_path.exists()) {
            this.business.setFilesTitle("ENYK adat állományai");
            this.business.setSelectedPath(this.enyk_path);
            this.business.rescan();
            this.current_path = this.enyk_path;
            this.other_path = this.outer_path;
         } else {
            GuiUtil.showMessageDialog(ABEVOpenPanel.this, "ENYK útvonal nincs kiválasztva !", "Nézet váltás", 2);
         }
      }

      public void b12Clicked() {
         if (this.outer_path != null && this.outer_path.exists()) {
            this.lbl_file_list_title.setText("Külső mappa adat állományai");
            this.business.setSelectedPath(this.outer_path);
            this.business.rescan();
            this.current_path = this.outer_path;
            this.other_path = this.enyk_path;
         } else {
            GuiUtil.showMessageDialog(ABEVOpenPanel.this, "Külső mappa útvonal nincs kiválasztva !", "Nézet váltás", 2);
         }
      }

      public void b22PathClicked() {
         File var1 = Operations.getFolder(ABEVOpenPanel.this, this.outer_path);
         if (var1 != null) {
            if (var1.equals(this.enyk_path)) {
               GuiUtil.showMessageDialog(ABEVOpenPanel.this, "Válasszon másik mappát !\nAz ENYK mappával nem egyezhet meg a külső mappa !", "Mappa választás", 2);
            } else if (!var1.exists()) {
               GuiUtil.showMessageDialog(ABEVOpenPanel.this, "Válasszon másik mappát !\nA megadott mappa nem létezik !", "Mappa választás", 2);
            } else {
               this.outer_path = var1;
               this.txt_path.setText(this.outer_path.getPath());
               if (this.current_path == this.enyk_path) {
                  this.other_path = this.outer_path;
               } else {
                  this.current_path = this.outer_path;
               }
            }
         }

      }

      public void b31Clicked() {
         ABEVOpenPanel.this.setVisible(false);
      }

      public void b32Clicked() {
         ABEVOpenPanel.this.setVisible(false);
      }
   }
}
