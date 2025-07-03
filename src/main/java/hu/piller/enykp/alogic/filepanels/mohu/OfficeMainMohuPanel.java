package hu.piller.enykp.alogic.filepanels.mohu;

import hu.piller.enykp.alogic.ebev.Ebev;
import hu.piller.enykp.alogic.ebev.SendParams;
import hu.piller.enykp.alogic.fileloader.kr.KrHeadParser;
import hu.piller.enykp.alogic.filepanels.ABEVOpenPanel;
import hu.piller.enykp.alogic.filepanels.attachement.EJFileChooser;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.alogic.templateutils.blacklist.BlacklistStore;
import hu.piller.enykp.alogic.uploader.FeltoltesValasz;
import hu.piller.enykp.alogic.uploader.UploaderException;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.kauclient.KauAuthMethod;
import hu.piller.enykp.kauclient.KauAuthMethods;
import hu.piller.enykp.niszws.util.DapSessionHandler;
import hu.piller.enykp.niszws.util.GateType;
import hu.piller.enykp.niszws.util.KauAuthHelper;
import hu.piller.enykp.niszws.util.KauSessionTimeoutHandler;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.TableSorter;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.EJList;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import hu.piller.enykp.util.base.tabledialog.TooltipTableHeader;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class OfficeMainMohuPanel extends LoginVetoablePanel implements ActionListener, TableModelListener, KeyListener, WindowListener {
   private static final String ADD_BUTTON_NAME = "1";
   private static final String ADD_KR_BUTTON_NAME = "2";
   private static final String DEL_BUTTON_NAME = "3";
   private static final String IMP_BUTTON_NAME = "5";
   private static final String CANCEL_BUTTON_NAME = "6";
   private static final String LOG_OK_BUTTON_NAME = "7";
   private static final String LOG_SAVE_BUTTON_NAME = "8";
   private static final int ADD_BUTTON_VALUE = 1;
   private static final int ADD_KR_BUTTON_VALUE = 2;
   private static final int DEL_BUTTON_VALUE = 3;
   private static final int START_BUTTON_VALUE = 5;
   private static final int CANCEL_BUTTON_VALUE = 6;
   private static final int LOG_OK_BUTTON_VALUE = 7;
   private static final int LOG_SAVE_BUTTON_VALUE = 8;
   public static int MAIN_WIDTH = 790;
   public static final int MAIN_HEIGHT = 520;
   public static final int SUMMA_WIDTH = 400;
   public static final int SUMMA_HEIGHT = 400;
   public static final int OPEN_WIDTH = 600;
   public static final int OPEN_HEIGHT = 400;
   public static final String[] DI_KEYS = new String[]{"id", "tax_number", "person_name", "from_date", "to_date", "state", "info", "account_name", "save_date", "note", "ver", "templatever", "org"};
   private static final int CEGKAPU_COLUMN_INDEX = 2;
   private static final int ADOSZAM_COLUMN_INDEX_BEFORE_INSERT = 1;
   private String[] resFilter;
   private JDialog summaDialog;
   private LoginDialog authDialog;
   private ABEVOpenPanel openPanel;
   private EJList logLista;
   private JScrollPane logSP;
   private JFrame mainFrame;
   private Object[] defaultFileColumns;
   private ComboTableModel fileTableModel;
   private TableSorter sorter;
   private JTable fileTable;
   private EJFileChooser fc;
   private EJFileChooser fc4Save;
   private OfficeMainMohuPanel.TxtFileFileter tff;
   private OfficeMainMohuPanel.KrFileFileter krff;
   private JProgressBar status;
   private JLabel jl = new JLabel("     0 db fájl a listában");
   private JButton delButton;
   private JButton okButton;
   private JButton addButton;
   private JButton addKrButton;
   private JButton megsemButton;
   private JComboBox filters;
   private Vector postHibaLista;
   private static boolean elindult = false;
   private static boolean folyamatban = false;
   private static boolean outOfMemory = false;
   private static File defaultDirectory = null;
   boolean tovabb = false;
   private Object cmdObject;
   KauAuthHelper kauAuthHelper;
   private MouseAdapter stopEditMouseAdapter;
   static IPropertyList iplMaster = PropertyList.getInstance();
   private SendParams sp;
   Hashtable rowMatch;
   private HashSet<String> hibasCKAzonositok;
   private String externalCKMessage;
   private Vector allCKAzonData;
   private static final Runtime s_runtime = Runtime.getRuntime();

   public OfficeMainMohuPanel() throws HeadlessException {
      super((JFrame)MainFrame.thisinstance, "Nyomtatványok csoportos közvetlen beküldése Cég/Hivatali kapun keresztül", true);
      this.sp = new SendParams(iplMaster);
      this.rowMatch = new Hashtable();
      this.hibasCKAzonositok = new HashSet();
      this.externalCKMessage = "";
      this.allCKAzonData = new Vector();
      this.setDefaultCloseOperation(2);
      this.addWindowListener(this);
      this.mainFrame = MainFrame.thisinstance;
      this.kauAuthHelper = KauAuthHelper.getInstance();
      this.allCKAzonData.clear();
      this.allCKAzonData.add(this.getDefaultCegkapuAzon());
      this.externalCKMessage = "";
      this.news();
      MAIN_WIDTH = (int)(1.5D * (double)(GuiUtil.getW(this.addButton, this.addButton.getText()) + GuiUtil.getW(this.addKrButton, this.addKrButton.getText()) + GuiUtil.getW(this.delButton, this.delButton.getText())));
      double var1 = 0.8D * (double)GuiUtil.getScreenW();
      if (var1 < (double)MAIN_WIDTH) {
         MAIN_WIDTH = (int)var1;
      }

      try {
         this.init();
      } catch (NullPointerException var15) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "Alkalmazáshiba", "Hiba", 0);
         return;
      } catch (Exception var16) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiányzó paraméter : " + var16.getMessage().substring(1), "Hiba", 0);
         return;
      }

      this.initFcs();
      this.initLogDialog();
      this.summaDialog.setSize(400, 400);
      this.summaDialog.setLocationRelativeTo(MainFrame.thisinstance);
      if (this.authDialog != null) {
         this.authDialog.setLocationRelativeTo(MainFrame.thisinstance);
         this.openPanel = new ABEVOpenPanel();
         this.openPanel.setSize(600, 400);
         this.openPanel.setLocationRelativeTo(MainFrame.thisinstance);
         this.tableSettings();
         this.setButtonState(false);
         int var3 = this.mainFrame.getX() + this.mainFrame.getWidth() / 2 - MAIN_WIDTH / 2;
         if (var3 < 0) {
            var3 = 0;
         }

         int var4 = this.mainFrame.getY() + this.mainFrame.getHeight() / 2 - 260;
         if (var4 < 0) {
            var4 = 0;
         }

         this.setBounds(var3, var4, MAIN_WIDTH, 520);
         JPanel var5 = new JPanel(new BorderLayout());
         JPanel var6 = new JPanel(new BorderLayout(20, 0));
         JPanel var7 = new JPanel();
         var7.addMouseListener(this.stopEditMouseAdapter);
         JPanel var8 = new JPanel(new GridLayout(2, 1));
         var8.addMouseListener(this.stopEditMouseAdapter);
         JPanel var9 = new JPanel();
         var9.addMouseListener(this.stopEditMouseAdapter);
         JPanel var10 = new JPanel(new FlowLayout(0));
         this.addButton.setName("1");
         this.addKrButton.setName("2");
         this.addButton.addActionListener(this);
         this.addKrButton.addActionListener(this);
         this.addButton.setToolTipText("Nyomtatvány hozzáadása a listához");
         this.addKrButton.setToolTipText("kr fájl hozzáadása a listához a(z) " + this.sp.destPath + " mappából");
         this.delButton.setName("3");
         this.delButton.addActionListener(this);
         this.delButton.setToolTipText("A kijelölt nyomtatványok törlése a listából");
         var7.add(this.addButton);
         var7.add(this.addKrButton);
         var7.add(this.delButton);
         JScrollPane var11 = new JScrollPane(this.fileTable, 20, 30);
         var11.addMouseListener(this.stopEditMouseAdapter);
         Dimension var12 = new Dimension(MAIN_WIDTH, 350);
         var6.setPreferredSize(var12);
         var6.setMinimumSize(var12);
         var10.setBounds(5, 5, 590, 30);
         var11.setBounds(10, 40, 500, 250);
         var6.add(var11, "Center");
         var6.add(var7, "South");
         this.status.setString(" ");
         this.status.setIndeterminate(false);
         this.status.setStringPainted(true);
         this.status.setBorderPainted(false);
         var8.add(this.status);
         var5.add(var6, "Center");
         var5.add(var8, "South");
         this.getContentPane().add(var5, "Center");
         this.okButton.setName("5");
         this.okButton.addActionListener(this);
         this.megsemButton.setName("6");
         this.megsemButton.addActionListener(this);
         this.okButton.setSize(new Dimension(GuiUtil.getW(this.okButton, this.okButton.getText()), GuiUtil.getCommonItemHeight() + 2));
         this.megsemButton.setSize(new Dimension(GuiUtil.getW(this.megsemButton, this.megsemButton.getText()), GuiUtil.getCommonItemHeight() + 2));
         var9.add(this.okButton);
         var9.add(this.megsemButton);
         var9.setBorder(new EmptyBorder(5, 5, 5, 5));
         this.jl.setPreferredSize(new Dimension(300, 30));
         this.openPanel.setFilters(new String[]{this.resFilter[this.filters.getSelectedIndex()]});
         this.rowMatch.put(6, "Küldésre megjelölt");
         this.openPanel.setSelectedPath((new NecroFile(this.sp.dataPath)).toURI());
         this.openPanel.setMode("open_import", this.rowMatch);
         this.openPanel.setTitle("Nyomtatvány hozzáadása " + this.beauty(this.sp.dataPath));
         JComboBox var13 = new JComboBox();
         DefaultCellEditor var14 = new DefaultCellEditor(var13) {
            ComboBoxEditor currentComboBoxEditor;
            String defaultCKAzon = OfficeMainMohuPanel.this.getDefaultCegkapuAzon();

            public Object getCellEditorValue() {
               return this.currentComboBoxEditor.getItem();
            }

            public Component getTableCellEditorComponent(JTable var1, Object var2, boolean var3, int var4, int var5) {
               String var6 = OfficeMainMohuPanel.this.getFormattedAdoszam(var1, var4, var5);
               JComboBox var7;
               if (var6.equalsIgnoreCase(this.defaultCKAzon)) {
                  var7 = new JComboBox(new String[]{this.defaultCKAzon});
               } else {
                  var7 = new JComboBox(new String[]{this.defaultCKAzon, var6});
               }

               var7.setEditable(true);
               this.currentComboBoxEditor = var7.getEditor();
               var7.setSelectedItem(var2);
               return var7;
            }
         };
         this.fileTable.getTableHeader().getColumnModel().getColumn(2).setCellEditor(var14);
         this.getContentPane().add(var9, "South");
         this.pack();
         this.setVisible(true);
      }
   }

   private void init() throws Exception {
      iplMaster = PropertyList.getInstance();
      this.filters.addItem("Nyomtatványok");
      this.resFilter = new String[]{"inner_data_loader_v1"};
   }

   private void initLogDialog() {
      JPanel var1 = new JPanel();
      JButton var2 = new JButton("Rendben");
      JButton var3 = new JButton("Lista mentése");
      var2.setName("7");
      var3.setName("8");
      var2.addActionListener(this);
      var3.addActionListener(this);
      var1.add(var2);
      var1.add(var3);
      this.summaDialog.getContentPane().add(var1, "South");
   }

   private void initDialog() {
      if (defaultDirectory != null) {
         this.fc4Save.setCurrentDirectory(defaultDirectory);
      }

      try {
         ((BasicFileChooserUI)this.fc.getUI()).setFileName("");
      } catch (ClassCastException var6) {
         try {
            this.fc.setSelectedFile(new NecroFile(""));
         } catch (Exception var5) {
            Tools.eLog(var5, 0);
         }
      }

      this.fc.setSelectedFile((File)null);

      try {
         ((BasicFileChooserUI)this.fc4Save.getUI()).setFileName("csoportos_muveletek_uzenetek.txt");
      } catch (ClassCastException var4) {
         try {
            this.fc4Save.setSelectedFile(new NecroFile("csoportos_muveletek_uzenetek.txt"));
         } catch (Exception var3) {
            Tools.eLog(var3, 0);
         }
      }

      this.fc4Save.setSelectedFile((File)null);
   }

   private void doAdd() {
      Hashtable var1 = this.openPanel.showDialog();
      if (var1 != null) {
         Object[] var2 = (Object[])((Object[])var1.get("selected_files"));
         this.addFilesToList(var2);
         var1 = null;
      }
   }

   private void doAddKr() {
      this.initDialog();
      int var1 = this.fc.showOpenDialog(this);
      if (var1 == 0) {
         File[] var2 = this.fc.getSelectedFiles();
         this.addKrFileToList(var2);
      }

   }

   private void doDel() {
      if (this.fileTable.getSelectedRows().length > 0 && JOptionPane.showOptionDialog(this, "Biztosan törli a kijelölt fájlokat a listából?", "Kérdés", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0) {
         if (this.fileTable.getCellEditor() != null) {
            this.fileTable.getCellEditor().stopCellEditing();
         }

         int[] var1 = this.getOriginalIndexes(this.fileTable.getSelectedRows());

         for(int var2 = var1.length; var2 > 0; --var2) {
            this.fileTableModel.removeRow(var1[var2 - 1]);
         }
      }

   }

   private int[] getOriginalIndexes(int[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         Object var3 = this.fileTable.getModel().getValueAt(var1[var2], 0);
         int var4 = 0;

         for(boolean var5 = false; var4 < this.fileTableModel.getRowCount() && !var5; ++var4) {
            if (this.fileTableModel.getValueAt(var4, 0).equals(var3)) {
               var5 = true;
               var1[var2] = var4;
            }
         }
      }

      Arrays.sort(var1);
      return var1;
   }

   private String startPost() {
      try {
         if (this.fileTable.getCellEditor() != null) {
            this.fileTable.getCellEditor().stopCellEditing();
         }

         String var1;
         if (this.fileTableModel.getRowCount() == 0) {
            GuiUtil.showMessageDialog(this.mainFrame, "Nincs elem a listában!", "Üzenet", 1);
            return null;
         }

         if (!this.checkCKAzonFilled()) {
            GuiUtil.showMessageDialog(this.mainFrame, "Néhány Cég/Hivatali kapu azonosító nincs kitöltve!" + this.getInfoMessage(), "Hiba", 0);
            return null;
         }

         this.fileTableModel.setEditable(false);
         this.kauAuthHelper.setGateType(GateType.CEGKAPU_HIVATALIKAPU);
         this.kauAuthHelper.setUgyfelkapura(false);
         this.tovabb = this.authDialog.showIfNeed();
         if (this.tovabb) {
            if (this.authDialog.getState() != 3) {
               this.fileTableModel.setEditable(true);
               return null;
            }

            var1 = this.kauAuthHelper.getAnyGateId();
         } else {
             var1 = null;
         }

          this.postHibaLista.clear();
         this.enableAll(false);
         final JDialog var2 = new JDialog(this.mainFrame, "Küldése", true);
         var2.setDefaultCloseOperation(0);
         final boolean[] var3 = new boolean[]{false};
         SwingWorker var5 = new SwingWorker() {
            int postedFileCount = 0;
            int fileCount;

            {
               this.fileCount = OfficeMainMohuPanel.this.fileTableModel.getRowCount();
            }

            public Object doInBackground() {
               Result var1x = null;

               try {
                  OfficeMainMohuPanel.folyamatban = true;
                  OfficeMainMohuPanel.this.kauAuthHelper.setSaveAuthData(true);
                  DapSessionHandler.getInstance().setBatchDapUploadInProgress(KauAuthMethods.getSelected() == KauAuthMethod.KAU_DAP);
                  OfficeMainMohuPanel.this.kauAuthHelper.setSaveAuthData(true);
                  OfficeMainMohuPanel.this.filters.setEnabled(false);
                  OfficeMainMohuPanel.this.hibasCKAzonositok.clear();
                  if (OfficeMainMohuPanel.this.fileTableModel.getRowCount() != 0) {
                     OfficeMainMohuPanel.this.postHibaLista.add(new TextWithIcon("Start : " + OfficeMainMohuPanel.this.getTimeString("yyyy.MM.dd HH.mm.ss"), -1));
                     OfficeMainMohuPanel.this.postHibaLista.add(new TextWithIcon("-------------------------------------------", -1));
                  }

                  var1x = new Result();

                  while(OfficeMainMohuPanel.this.fileTableModel.getRowCount() != 0 && OfficeMainMohuPanel.folyamatban) {
                     OfficeMainMohuPanel.this.postHibaLista.add(new TextWithIcon(" ", -1));
                     OfficeMainMohuPanel.this.postHibaLista.add(new TextWithIcon("[" + OfficeMainMohuPanel.this.fileTableModel.getValueAt(0, 0) + "]", -1));
                     OfficeMainMohuPanel.this.status.setIndeterminate(true);
                     OfficeMainMohuPanel.this.status.setString("     " + OfficeMainMohuPanel.this.fileTableModel.getValueAt(0, 0) + " fájl küldése folyamatban...");

                     FeltoltesValasz[] var2x;
                     try {
                        String var3x = var1;
                        if (var3x == null) {
                           var3x = (String)OfficeMainMohuPanel.this.fileTableModel.getValueAt(0, 2);
                        }

                        if (var3x == null) {
                           var3x = "";
                        }

                        if (OfficeMainMohuPanel.this.kauAuthHelper.isUgyfelkapura()) {
                           var3x = "";
                        }

                        if (OfficeMainMohuPanel.this.hibasCKAzonositok.contains(var3x)) {
                           OfficeMainMohuPanel.this.postHibaLista.add(new TextWithIcon("A nyomtatvány beküldése sikertelen!", 1));
                           OfficeMainMohuPanel.this.postHibaLista.add(new TextWithIcon(OfficeMainMohuPanel.this.externalCKMessage + " - Cég/Hivatali kapu azonosító : " + var3x, -1));
                           OfficeMainMohuPanel.this.fileTableModel.removeRow(0);
                           continue;
                        }

                        var2x = OfficeMainMohuPanel.this.doPost((String)OfficeMainMohuPanel.this.fileTableModel.getValueAt(0, 0), var3x);
                        if (var2x == null) {
                           var1x.setOk(false);
                           var1x.errorList.add("A feltöltés sikertelen: " + OfficeMainMohuPanel.this.fileTableModel.getValueAt(0, 0) + " - " + var3x);
                           var1x.errorList.addAll(OfficeMainMohuPanel.this.postHibaLista);
                           OfficeMainMohuPanel.this.hibasCKAzonositok.add(var3x);
                           OfficeMainMohuPanel.this.fileTableModel.removeRow(0);
                           break;
                        }
                     } catch (Exception var8) {
                        var8.printStackTrace();
                        if (var8.getMessage() != null && var8.getMessage().indexOf("Megszakítva") > -1) {
                           break;
                        }

                        OfficeMainMohuPanel.this.postHibaLista.add(new TextWithIcon(" - Hiba történt a fájl küldésekor", 0));
                        Tools.eLog(var8, 0);
                        var1x.setOk(false);
                        var1x.errorList.add("Hiba történt a fájl küldésekor");
                        OfficeMainMohuPanel.this.fileTableModel.removeRow(0);
                        continue;
                     }

                     if (var2x[0].isStored()) {
                        ++this.postedFileCount;
                     }

                     OfficeMainMohuPanel.this.fileTableModel.removeRow(0);
                     OfficeMainMohuPanel.this.status.setString(" ");
                  }
               } catch (Exception var9) {
                  var2.setVisible(false);
                  var9.printStackTrace();
               } finally {
                  DapSessionHandler.getInstance().reset();
                  OfficeMainMohuPanel.this.kauAuthHelper.setGateType(GateType.UGYFELKAPU);
                  OfficeMainMohuPanel.this.kauAuthHelper.setUgyfelkapura(true);
                  KauSessionTimeoutHandler.getInstance().reset();
                  OfficeMainMohuPanel.this.kauAuthHelper.setSaveAuthData(false);
               }

               OfficeMainMohuPanel.this.status.setIndeterminate(false);
               OfficeMainMohuPanel.this.filters.setEnabled(true);
               var1x.errorList = OfficeMainMohuPanel.this.postHibaLista;
               OfficeMainMohuPanel.this.fileTableModel.setEditable(true);
               return var1x;
            }

            public void done() {
               var3[0] = true;

               try {
                  OfficeMainMohuPanel.this.cmdObject = this.get();
               } catch (Exception var3x) {
                  var3x.printStackTrace();
                  OfficeMainMohuPanel.this.cmdObject = null;
               }

               try {
                  var2.setVisible(false);
               } catch (Exception var2x) {
                  Tools.eLog(var2x, 0);
               }

               OfficeMainMohuPanel.folyamatban = false;
               OfficeMainMohuPanel.elindult = false;
               OfficeMainMohuPanel.this.status.setString(" ");
               OfficeMainMohuPanel.this.postHibaLista.add(new TextWithIcon(" ", -1));
               OfficeMainMohuPanel.this.postHibaLista.add(new TextWithIcon(" ", -1));
               OfficeMainMohuPanel.this.postHibaLista.add(new TextWithIcon("A küldésre kijelölt fájlok száma: " + this.fileCount, -1));
               if (this.postedFileCount != 0) {
                  OfficeMainMohuPanel.this.postHibaLista.add(new TextWithIcon(this.postedFileCount + " fájlt sikeresen beküldtünk.", -1));
               }

               if (this.fileCount - this.postedFileCount != 0) {
                  OfficeMainMohuPanel.this.postHibaLista.add(new TextWithIcon(this.fileCount - this.postedFileCount + " fájl küldése nem sikerült.", 0));
               }

               if (OfficeMainMohuPanel.outOfMemory) {
                  OfficeMainMohuPanel.this.postHibaLista.add(new TextWithIcon("A küldés elvégzéséhez kevés a memória! Kérjük indítsa újra az alkalmazást!", 0));
               }

               OfficeMainMohuPanel.this.enableAll(true);
               MainFrame.thisinstance.glasslock = false;
               MainFrame.thisinstance.setGlassLabel((String)null);
               Tools.resetLabels();
               OfficeMainMohuPanel.this.fillDialog("A küldés eredménye:", OfficeMainMohuPanel.this.postHibaLista);
               Result var1x = (Result)OfficeMainMohuPanel.this.cmdObject;
            }
         };
         elindult = true;
         var3[0] = false;
         var5.execute();

         try {
            if (var3[0]) {
               var2.setVisible(false);
            }
         } catch (Exception var8) {
            Tools.eLog(var8, 0);
         }
      } catch (Exception var9) {
         this.status.setString(" ");
         this.enableAll(true);
      }

      try {
         return ((File)((Result)this.cmdObject).errorList.elementAt(0)).getAbsolutePath();
      } catch (Exception var7) {
         return "";
      }
   }

   private void setButtonState(boolean var1) {
      if (!folyamatban) {
         this.delButton.setEnabled(var1);
         this.okButton.setEnabled(var1);
      }
   }

   private String getTimeString(String var1) {
      SimpleDateFormat var2 = new SimpleDateFormat(var1);
      return var2.format(Calendar.getInstance().getTime());
   }

   private void fillDialog(String var1, Vector var2) {
      this.summaDialog.setTitle(var1);
      this.logLista.removeAll();
      this.logLista.setListData(var2);
      this.logSP.setViewportView(this.logLista);
      Dimension var3 = new Dimension(650, 500);
      this.logSP.setPreferredSize(var3);
      this.logSP.setMinimumSize(var3);
      this.summaDialog.getContentPane().add(this.logSP, "Center");
      this.summaDialog.pack();
      this.summaDialog.setVisible(true);
   }

   private FeltoltesValasz[] doPost(String var1, String var2) throws Exception {
      FeltoltesValasz[] var3 = null;
      boolean var4 = true;

      try {
         MohuTools var5 = new MohuTools(this.sp);
         boolean var6 = !this.kauAuthHelper.isUgyfelkapura();

         boolean var7;
         do {
            var7 = false;

            try {
               System.out.println(var1 + " send to : " + (var6 ? "CK/HK" : "UK") + " with: " + var2);
               var3 = var5.callWS(new String[]{var1}, var6, var2);
            } catch (UploaderException var12) {
               var3 = null;
               if (this.isCKErrorMessage(var12.getMessage())) {
                  this.externalCKMessage = var12.getMessage();
                  if (this.externalCKMessage == null) {
                     this.externalCKMessage = "";
                  }

                  if ("null".equalsIgnoreCase(this.externalCKMessage)) {
                     this.externalCKMessage = "";
                  }

                  this.postHibaLista.add(new TextWithIcon("A nyomtatvány beküldése sikertelen!", 1));
                  this.postHibaLista.add(new TextWithIcon(this.externalCKMessage + " - Cég/Hivatali kapu azonosító : " + var2, -1));
               } else {
                  this.postHibaLista.add(new TextWithIcon("Nem sikerült kapcsolódni a fogadó szerverhez!", 1));
                  this.postHibaLista.add(new TextWithIcon(" - Cég/Hivatali kapu azonosító : " + var2, -1));
                  this.postHibaLista.add(new TextWithIcon(" - Részletes leírást a Szerviz/Üzenetek menüpontban talál.", -1));
               }
            } catch (Exception var13) {
               if (var13.getMessage() != null && var13.getMessage().indexOf("Munkamenet nem érvényes, lépjen be újra!") > -1) {
                  if (GuiUtil.showOptionDialog((Component)null, "Munkamenetének érvényességi ideje lejárt! A művelet megismétléséhez újra bejelentkezik?", "KAÜ bejelentkezés", 0, 3, (Icon)null, MohuTools.repeatCancel, MohuTools.repeatCancel[0]) == 0) {
                     var7 = true;
                     KauSessionTimeoutHandler.getInstance().reset();
                  } else {
                     var3 = null;
                     KauSessionTimeoutHandler.getInstance().reset();
                     this.postHibaLista.add(new TextWithIcon("A nyomtatvány beküldése sikertelen!", 1));
                     this.postHibaLista.add(new TextWithIcon(" - " + var13.getMessage(), -1));
                     this.postHibaLista.add(new TextWithIcon(" - Cég/Hivatali kapu azonosító : " + var2, -1));
                     this.postHibaLista.add(new TextWithIcon(" - Részletes leírást a Szerviz/Üzenetek menüpontban talál.", -1));
                  }
               } else {
                  var3 = null;
                  this.postHibaLista.add(new TextWithIcon("A nyomtatvány beküldése sikertelen!", 1));
                  this.postHibaLista.add(new TextWithIcon(" - " + var13.getMessage(), -1));
                  this.postHibaLista.add(new TextWithIcon(" - Cég/Hivatali kapu azonosító : " + var2, -1));
                  this.postHibaLista.add(new TextWithIcon(" - Részletes leírást a Szerviz/Üzenetek menüpontban talál.", -1));
                  if (var13.getMessage() != null && var13.getMessage().indexOf("Megszakítva") > -1) {
                     throw var13;
                  }
               }
            }
         } while(var7);

         if (var3 != null && var3.length != 0) {
            if (var3[0].isStored()) {
               var4 = var5.moveFile(this.sp.destPath + var3[0].getFileName());
            }

            if (var3[0].isStored()) {
               this.postHibaLista.add(new TextWithIcon(var1 + "  - sikeresen beküldve.", -1));
               if (!var4) {
                  this.postHibaLista.add(new TextWithIcon(var1 + "  - átmozgatása az elküldött mappába nem sikerült. Kérem mozgassa át a küldendő mappából az elküldöttbe!", 4));
               }

               if (var3[0].getFilingNumber() == null) {
                  this.postHibaLista.add(new TextWithIcon("A nyomtatvány a érkeztetési számát az értesítési tárhelyére belépve tudja megnézni. A továbbiakban azon a számon hivatkozhat rá.", 3));
               } else {
                  this.postHibaLista.add(new TextWithIcon("A nyomtatvány a " + var3[0].getFilingNumber() + " érkeztetési számot kapta. A továbbiakban ezen a számon hivatkozhat rá.\n", 3));

                  try {
                     Ebev.log(var6 ? 4 : 3, new NecroFile(this.sp.sentPath + var3[0].getFileName()), "Érkeztetési szám: " + var3[0].getFilingNumber());
                  } catch (Exception var11) {
                     try {
                        System.out.println("Figyelmeztetés! Nem sikerült a feladás naplózása. A nyomtatvány a " + var3[0].getFilingNumber() + " érkeztetési számot kapta");
                     } catch (Exception var10) {
                        Tools.eLog(var11, 0);
                     }
                  }
               }
            } else {
               this.postHibaLista.add(new TextWithIcon(var1 + "  - küldése nem sikerült.", 0));
               if (var3[0].getErrorMsg() != null) {
                  this.postHibaLista.add(new TextWithIcon("A sikertelenség oka a fogadó oldal szerint: " + var3[0].getErrorMsg() + "\n", 0));
               }
            }
         }
      } catch (Exception var14) {
         if (var14.getMessage() != null && var14.getMessage().indexOf("Megszakítva") > -1) {
            throw var14;
         }

         this.postHibaLista.add(new TextWithIcon(var1 + "  - Hiba történt a küldéskor.", 0));
         ErrorList.getInstance().writeError(new Long(4001L), "Csoportos Mohu küldés hiba", var14, (Object)null);
      }

      return var3;
   }

   private void enableAll(boolean var1) {
      this.addButton.setEnabled(var1);
      this.addKrButton.setEnabled(var1);
      this.okButton.setEnabled(var1 & this.fileTableModel.getRowCount() > 0);
      this.delButton.setEnabled(this.okButton.isEnabled());
      this.megsemButton.setEnabled(true);
      this.filters.setEnabled(true);
      this.setDefaultCloseOperation(var1 ? 2 : 0);
   }

   private String beauty(Object var1) {
      String var2 = var1.toString();
      if (var2 != null) {
         if (File.separator.equals("\\")) {
            var2 = var2.replaceAll("/", "\\\\");
         } else {
            var2 = var2.replaceAll("\\\\", "/");
         }
      }

      return var2;
   }

   private boolean alreadyInList(String[] var1) {
      boolean var2 = false;
      String var3 = "";

      int var4;
      for(var4 = 0; var4 < var1.length; ++var4) {
         var3 = var3 + var1[var4] + ";";
      }

      var4 = 0;

      for(Vector var5 = this.fileTableModel.getDataVector(); var4 < var5.size() && !var2; ++var4) {
         String var6 = this.parseVector((Vector)var5.get(var4));
         var2 = var3.equals(var6);
      }

      return var2;
   }

   private boolean alreadyInList(Vector var1) {
      boolean var2 = false;
      String var3 = this.parseVector(var1);
      int var4 = 0;

      for(Vector var5 = this.fileTableModel.getDataVector(); var4 < var5.size() && !var2; ++var4) {
         var2 = var3.toLowerCase().indexOf(((Vector)var5.get(var4)).get(0).toString().toLowerCase()) > -1;
      }

      return var2;
   }

   private String parseVector(Vector var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         if (var1.elementAt(var3) != null) {
            var2.append(var1.elementAt(var3)).append(";");
         }
      }

      return var2.toString();
   }

   private void tableSettings() {
      if (GuiUtil.modGui()) {
         this.fileTable.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
      }

      this.fileTable.setSelectionMode(2);
      this.fileTable.setRowSelectionAllowed(true);
      this.fileTableModel.addTableModelListener(this);
      this.fileTable.addKeyListener(this);
      this.fileTable.setAutoResizeMode(0);
      this.fileTable.addMouseMotionListener(new MouseMotionListener() {
         public void mouseDragged(MouseEvent var1) {
         }

         public void mouseMoved(MouseEvent var1) {
            try {
               OfficeMainMohuPanel.this.fileTable.setToolTipText((String)OfficeMainMohuPanel.this.fileTableModel.getValueAt(OfficeMainMohuPanel.this.fileTable.rowAtPoint(var1.getPoint()), OfficeMainMohuPanel.this.fileTable.columnAtPoint(var1.getPoint())));
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

         }
      });
      JLabel var2 = new JLabel();

      for(int var3 = 0; var3 < this.fileTable.getColumnModel().getColumnCount(); ++var3) {
         TableColumn var1 = this.fileTable.getColumnModel().getColumn(var3);
         if (var3 == 0) {
            var1.setPreferredWidth(GuiUtil.getW(var2, var1.getHeaderValue().toString()) + 80);
         } else {
            var1.setPreferredWidth(GuiUtil.getW(var2, var1.getHeaderValue().toString()) + 50);
         }
      }

      this.fileTable.setTableHeader(new TooltipTableHeader(this.fileTable.getColumnModel()));
      this.sorter.setTableHeader(this.fileTable.getTableHeader());
   }

   private void addFilesToList(Object[] var1) {
      Vector var2 = new Vector();
      Vector var3 = new Vector();

      for(int var4 = 0; var4 < var1.length; ++var4) {
         Object[] var5 = (Object[])((Object[])var1[var4]);
         Vector var6 = new Vector();
         String var8;
         File var9;
         if (var5.length > 3 && var5[3] != null && var5[3] instanceof Map) {
            Object var7 = ((Map)var5[3]).get("docinfo");
            if (var7 != null && var7 instanceof Map) {
               var8 = (String)((Map)var7).get("krfilename");
               if (var8 != null && var8.trim().length() > 0) {
                  var9 = new NecroFile(this.sp.destPath + var8);
                  if (var9.exists()) {
                     var6.add(this.sp.destPath + var8);
                  }
               }
            }
         }

         if (var6.size() == 0) {
            var6 = this.getMarkedFiles4EnykFile((File)var5[0]);
         }

         for(int var13 = 0; var13 < var6.size(); ++var13) {
            var8 = (String)var6.elementAt(var13);
            var9 = new NecroFile(var8);
            if (var9.length() > 524288000L) {
               var3.add(((File)var5[0]).getAbsolutePath() + " - A dokumentum mérete meghaladja a(z) " + "500 Mbyte" + "-ot. Ekkora állományt a Cég/Hivatali kapu nem tud fogadni!");
            } else {
               Vector var10 = new Vector();
               var10.add(var8);
               Hashtable var11 = (Hashtable)((Hashtable)var5[3]).get("docinfo");
               if (var11.size() == 0) {
                  var3.add(((File)var5[0]).getAbsolutePath() + " - Nem olvashatóak a nyomtatványadatok.");
                  var10 = null;
               } else if (var11.get("name").equals("HIBÁS ÁLLOMÁNY !")) {
                  var3.add(((File)var5[0]).getAbsolutePath() + " - " + "HIBÁS ÁLLOMÁNY !" + (var11.containsKey("info") ? " - " + var11.get("info") : ""));
                  var10 = null;
               } else if (BlacklistStore.getInstance().isBiztipDisabled((String)var11.get("id"), (String)var11.get("org"))) {
                  String[] var15 = BlacklistStore.getInstance().getErrorListMessage((String)var11.get("id"), (String)var11.get("org"));
                  var3.add(((File)var5[0]).getAbsolutePath() + " - " + var15[0]);
                  if (!"".equals(var15[1])) {
                     var3.add(var15[1]);
                  }

                  var10 = null;
               } else {
                  var11.put("state", ((Hashtable)var5[2]).get("state"));
                  var11.put("save_date", this.formatDate(((Hashtable)var5[3]).get("saved")));

                  for(int var12 = 0; var12 < DI_KEYS.length; ++var12) {
                     if (!var11.containsKey(DI_KEYS[var12])) {
                        var10.add("");
                     } else {
                        var10.add(var11.get(DI_KEYS[var12]));
                     }
                  }

                  String var14 = var11.containsKey(DI_KEYS[1]) ? (String)var11.get(DI_KEYS[1]) : "";
                  if (!this.alreadyInList(var10)) {
                     this.fileTableModel.addRow(var10);
                     this.allCKAzonData.add(this.getFormattedAdoszam(var14));
                  } else {
                     var2.add(var10.get(0));
                  }

                  this.handleCKAzon(var10, var14);
               }
            }
         }
      }

      if (var2.size() > 0) {
         new ErrorDialog(MainFrame.thisinstance, "Az alábbi fájlok már a listában vannak", true, false, var2);
      }

      if (var3.size() > 0) {
         new ErrorDialog(MainFrame.thisinstance, "Az alábbi fájlokat nem adtuk a listához", true, false, var3);
      }

      var2 = null;
      var3 = null;
   }

   private String formatDate(Object var1) {
      if (var1 == null) {
         return "";
      } else {
         char[] var3 = var1.toString().trim().toCharArray();
         String var2 = "";
         int var4 = 0;

         for(int var5 = var3.length; var4 < var5 && var4 < 14; ++var4) {
            if (var4 != 4 && var4 != 6 && var4 != 14) {
               if (var4 == 8) {
                  var2 = var2 + "   ";
               } else if (var4 == 10 || var4 == 12) {
                  var2 = var2 + ":";
               }
            } else {
               var2 = var2 + ".";
            }

            var2 = var2 + var3[var4];
         }

         return var2;
      }
   }

   public static void runGC() throws Exception {
      for(int var0 = 0; var0 < 4; ++var0) {
         _runGC();
      }

   }

   private static void _runGC() throws Exception {
      long var0 = usedMemory();
      long var2 = Long.MAX_VALUE;

      for(int var4 = 0; var0 < var2 && var4 < 500; ++var4) {
         s_runtime.runFinalization();
         s_runtime.gc();
         Thread.yield();
         var2 = var0;
         var0 = usedMemory();
      }

   }

   public static long usedMemory() {
      return s_runtime.totalMemory() - s_runtime.freeMemory();
   }

   public static long freeMemory() {
      return s_runtime.freeMemory();
   }

   public void actionPerformed(ActionEvent var1) {
      String var2;
      if (var1.getSource() instanceof JButton) {
         var2 = ((JButton)var1.getSource()).getName();
      } else {
         var2 = ((JRadioButton)var1.getSource()).getName();
      }

      int var3;
      try {
         var3 = Integer.parseInt(var2);
      } catch (NumberFormatException var6) {
         return;
      }

      switch(var3) {
      case 1:
         this.doAdd();
         break;
      case 2:
         this.doAddKr();
         break;
      case 3:
         this.doDel();
      case 4:
      default:
         break;
      case 5:
         this.startPost();
         break;
      case 6:
         if (!elindult) {
            Tools.resetLabels();

            try {
               this.release();
            } catch (Exception var5) {
               var5.printStackTrace();
            }

            this.setVisible(false);
            this.dispose();
            this.mainFrame = null;
         } else if (folyamatban) {
            folyamatban = false;
            this.postHibaLista.add(new TextWithIcon("Felhasználói megszakítás", -1));
            this.megsemButton.setEnabled(false);
         }
         break;
      case 7:
         this.logOkAction();
         break;
      case 8:
         this.logSaveAction();
      }

   }

   public void tableChanged(TableModelEvent var1) {
      this.jl.setText("     " + this.fileTableModel.getRowCount() + " db fájl a listában");
      this.setButtonState(this.fileTableModel.getRowCount() != 0);
   }

   public void keyPressed(KeyEvent var1) {
   }

   public void keyReleased(KeyEvent var1) {
   }

   public void keyTyped(KeyEvent var1) {
   }

   public void windowActivated(WindowEvent var1) {
   }

   public void windowClosed(WindowEvent var1) {
   }

   public void windowClosing(WindowEvent var1) {
      Tools.resetLabels();
   }

   public void windowDeactivated(WindowEvent var1) {
   }

   public void windowDeiconified(WindowEvent var1) {
   }

   public void windowIconified(WindowEvent var1) {
   }

   public void windowOpened(WindowEvent var1) {
   }

   private void logOkAction() {
      this.summaDialog.setVisible(false);
      this.summaDialog.dispose();
   }

   private void logSaveAction() {
      this.initDialog();

      boolean var1;
      do {
         var1 = true;
         int var2 = this.fc4Save.showSaveDialog(this.summaDialog);
         if (var2 == 0) {
            File var3 = this.fc4Save.getSelectedFile();
            if (var3.exists()) {
               var1 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Ilyen nevű fájl már létezik. Felülírjuk?", "Csoportos beküldés", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0;
            }

            if (var1) {
               FileOutputStream var4 = null;

               try {
                  var4 = new NecroFileOutputStream(var3);

                  for(int var5 = 0; var5 < this.logLista.getModel().getSize(); ++var5) {
                     if (this.logLista.getModel().getElementAt(var5) instanceof TextWithIcon) {
                        var4.write((((TextWithIcon)((TextWithIcon)this.logLista.getModel().getElementAt(var5))).text + "\r\n").getBytes());
                     } else {
                        var4.write((this.logLista.getModel().getElementAt(var5).toString() + "\r\n").getBytes());
                     }
                  }

                  var4.close();
               } catch (Exception var8) {
                  try {
                     var4.close();
                  } catch (Exception var7) {
                     Tools.eLog(var7, 0);
                  }

                  GuiUtil.showMessageDialog(this.mainFrame, "A lista mentése nem sikerült!", "Hiba", 0);
               }

               defaultDirectory = this.fc4Save.getCurrentDirectory();
            }
         }
      } while(!var1);

   }

   private void news() {
      this.delButton = new JButton("Töröl");
      this.okButton = new JButton("Indítás");
      this.addButton = new JButton();
      this.addKrButton = new JButton();
      this.addButton.setText("Nyomtatvány hozzáadás");
      this.addKrButton.setText("kr fájl hozzáadás");
      this.megsemButton = new JButton("Bezár");
      this.postHibaLista = new Vector();
      this.status = new JProgressBar(0);
      this.tff = new OfficeMainMohuPanel.TxtFileFileter();
      this.krff = new OfficeMainMohuPanel.KrFileFileter();
      this.defaultFileColumns = new Object[]{"Állomány", "Nyomtatvány neve", "Cég/Hivatali kapu azonosító", "Adószám", "Név", "Dátumtól", "Dátumig", "Státusz", "Információ", "Adóazonosító", "Mentve", "Megjegyzés", "Verzió", "Sablon verzió", "Szervezet"};
      this.filters = new JComboBox();
      this.summaDialog = new JDialog(this, "", true);
      this.authDialog = LoginDialogFactory.create(GateType.CEGKAPU_HIVATALIKAPU, 1, true, (String)null);
      this.fileTableModel = new ComboTableModel(this.defaultFileColumns, 0);
      this.fileTableModel.setEditableColumnIndex(2);
      String var1 = this.useFormDataCKAzon();
      this.fileTableModel.setDefaultCKAzonIndex("0".equalsIgnoreCase(var1) ? 0 : 1);
      this.sorter = new TableSorter(this.fileTableModel);
      this.fileTable = new JTable(this.sorter);
      this.fileTable.getTableHeader().setReorderingAllowed(false);
      this.stopEditMouseAdapter = new MouseAdapter() {
         public void mouseClicked(MouseEvent var1) {
            if (OfficeMainMohuPanel.this.fileTable.getCellEditor() != null) {
               OfficeMainMohuPanel.this.fileTable.getCellEditor().stopCellEditing();
            }

         }
      };
      this.logLista = new EJList();
      this.logSP = new JScrollPane(this.logLista, 20, 30);
   }

   private void release() throws Exception {
      this.summaDialog = null;
      this.authDialog = null;
      this.addButton.removeActionListener(this);
      this.addKrButton.removeActionListener(this);
      this.delButton.removeActionListener(this);
      this.okButton.removeActionListener(this);
      this.filters.removeActionListener(this);
      this.filters.removeAllItems();
      this.megsemButton.removeActionListener(this);
      this.fc.removeActionListener(this);
      this.fc = null;
      this.fc4Save.removeActionListener(this);
      this.fc4Save = null;
      this.fileTableModel.removeTableModelListener(this);
      this.fileTable.removeKeyListener(this);
      this.filters = null;
      this.tff = null;
      this.krff = null;
      this.logLista = null;

      for(int var1 = 0; var1 < this.resFilter.length; ++var1) {
         this.resFilter[var1] = null;
      }

      this.resFilter = null;
      this.openPanel.setFilters((String[])null);
      this.openPanel = null;

      try {
         this.fileTable.setModel(new DefaultTableModel());
         this.fileTableModel = null;
         this.sorter.setTableModel((TableModel)null);
         this.sorter = null;
         this.removeAll();
         this.fileTable = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      this.mainFrame = null;
   }

   private Vector getMarkedFiles4EnykFile(File var1) {
      Vector var2 = new Vector();
      String var3 = var1.getName().substring(0, var1.getName().toLowerCase().indexOf(".frm.enyk"));
      String var4 = this.sp.destPath + var3 + ".kr";
      var1 = new NecroFile(var4);
      if (var1.exists()) {
         var2.add(var4);
         return var2;
      } else {
         var4 = this.sp.destPath + var3 + "_0" + "_p" + ".kr";
         var1 = new NecroFile(var4);
         if (var1.exists()) {
            var2.add(var4);
         }

         var4 = this.sp.destPath + var3 + "_1" + "_p" + ".kr";
         var1 = new NecroFile(var4);
         if (var1.exists()) {
            var2.add(var4);
         }

         var4 = this.sp.destPath + var3 + "_2" + "_p" + ".kr";
         var1 = new NecroFile(var4);
         if (var1.exists()) {
            var2.add(var4);
         }

         return var2;
      }
   }

   private void addKrFileToList(File[] var1) {
      Vector var2 = new Vector();
      Vector var3 = new Vector();

      for(int var4 = 0; var4 < var1.length; ++var4) {
         if (var1[var4].length() > 524288000L) {
            var2.add(var1[var4].getAbsolutePath() + " - A dokumentum mérete meghaladja a(z) " + "500 Mbyte" + "-ot. Ekkora állományt a Cég/Hivatali kapu nem tud fogadni!");
         } else if (!this.alreadyInListKr(var1[var4])) {
            try {
               String[] var5 = BlacklistStore.getInstance().isKrTemplateInBlackList(var1[var4]);
               if (var5 != null) {
                  String[] var16 = BlacklistStore.getInstance().getErrorListMessage(var5[0], var5[1]);
                  var2.add(var1[var4].getAbsolutePath() + " - " + var16[0]);
                  if (!"".equals(var16[1])) {
                     var2.add(var16[1]);
                  }
                  continue;
               }
            } catch (IOException var13) {
               var13.printStackTrace();
            }

            KrHeadParser var15 = new KrHeadParser(var1[var4].getAbsolutePath(), (String)null);

            try {
               var15.parse();
            } catch (Exception var14) {
               if (var14.getMessage() != null && !"*FORCE_END*".equals(var14.getMessage())) {
                  var2.add(var1[var4].getAbsolutePath() + " - Nem sikerült a kr fájl felolvasása");
                  continue;
               }
            }

            Hashtable var6 = var15.getData();
            String var7 = "";
            if (!var6.containsKey("adoszam")) {
               var3.add(var1[var4].getAbsolutePath() + " - Nem található adószám adat a kr fájban, nem tudunk Cégkapu azonosítót ajánlani");
            } else {
               var7 = (String)var6.get("adoszam");
            }

            String var8 = "";
            if (var6.containsKey("DokTipusAzonosito")) {
               var8 = (String)var6.get("DokTipusAzonosito");
            }

            String var9 = "";
            if (var6.containsKey("DokTipusVerzio")) {
               var9 = (String)var6.get("DokTipusVerzio");
            }

            String var10 = "";
            if (var6.containsKey("abevverzio")) {
               var10 = (String)var6.get("abevverzio");
            }

            Vector var11 = new Vector();
            var11.add(var1[var4].getAbsolutePath());
            var11.add(var8);
            var11.add(var7);

            for(int var12 = 0; var12 < 6; ++var12) {
               var11.add("");
            }

            var11.add(var10);
            var11.add(var9);
            var11.add("");
            this.allCKAzonData.add(this.getFormattedAdoszam(var7));
            this.handleCKAzon(var11, var7);
            this.fileTableModel.addRow(var11);
         }
      }

      if (var2.size() > 0) {
         new ErrorDialog(MainFrame.thisinstance, "Az alábbi fájlokat nem adtuk a listához", true, false, var2);
      }

      if (var3.size() > 0) {
         new ErrorDialog(MainFrame.thisinstance, "Az alábbi fájlokból nem tudtuk kiolvasni a cégkapu azonosítót!", true, false, var3);
      }

   }

   private void initFcs() {
      this.fc = new EJFileChooser("MetalFileChooserUI$1");
      this.fc.setCurrentDirectory(new NecroFile(this.sp.destPath));
      FileFilter[] var1 = this.fc.getChoosableFileFilters();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.fc.removeChoosableFileFilter(var1[var2]);
      }

      this.fc.setDialogTitle("kr fájl hozzáadás");
      this.fc4Save = new EJFileChooser();
      this.fc4Save.setDialogTitle("Lista mentése");
      this.fc.setCurrentDirectory(new NecroFile(this.sp.destPath));
      this.fc.setMultiSelectionEnabled(true);
      this.fc.addChoosableFileFilter(this.krff);
      this.fc.setFileSelectionMode(0);
      enableChangeFolderButton(this.fc);

      try {
         this.fc4Save.setCurrentDirectory(new NecroFile((String)PropertyList.getInstance().get("prop.usr.naplo")));
      } catch (Exception var6) {
         Tools.eLog(var6, 0);
      }

      try {
         ((BasicFileChooserUI)this.fc4Save.getUI()).setFileName("csoportos_muveletek_uzenetek.txt");
      } catch (ClassCastException var5) {
         try {
            this.fc4Save.setSelectedFile(new NecroFile("csoportos_muveletek_uzenetek.txt"));
         } catch (Exception var4) {
            Tools.eLog(var4, 0);
         }
      }

      this.fc4Save.addChoosableFileFilter(this.tff);
      defaultDirectory = this.fc4Save.getCurrentDirectory();
   }

   private static void enableChangeFolderButton(Container var0) {
      int var1 = var0.getComponentCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         Component var3 = var0.getComponent(var2);
         if (var3 instanceof JButton) {
            JButton var4 = (JButton)var3;
            Icon var5 = var4.getIcon();
            if (var5 != null && (var5 == UIManager.getIcon("FileChooser.homeFolderIcon") || var5 == UIManager.getIcon("FileChooser.upFolderIcon"))) {
               var4.setEnabled(false);
            }
         } else if (var3 instanceof Container) {
            enableChangeFolderButton((Container)var3);
         }
      }

   }

   private boolean alreadyInListKr(File var1) {
      boolean var2 = false;
      String var3 = var1.getName().toLowerCase();
      int var4 = 0;

      for(Vector var5 = this.fileTableModel.getDataVector(); var4 < var5.size() && !var2; ++var4) {
         var2 = ((Vector)var5.get(var4)).get(0).toString().toLowerCase().endsWith(var3);
      }

      return var2;
   }

   private void handleCKAzon(Vector var1, String var2) {
      String var3 = this.getDefaultCegkapuAzon();
      String var4 = this.getFormattedAdoszam(var2);
      var3 = var3 == null ? "" : var3;
      var4 = var4 == null ? "" : var4;
      String[] var5 = new String[]{var3, var4};
      var1.insertElementAt(var5, 2);
      this.fileTable.getTableHeader().getColumnModel().getColumn(2).setCellRenderer(new OfficeMainMohuPanel.MyComboBoxRenderer(this.allCKAzonData));
   }

   private String getDefaultCegkapuAzon() {
      String var1 = SettingsStore.getInstance().get("gui", "defaultCKAzon");
      if (var1 == null) {
         var1 = "";
      }

      return var1;
   }

   private String useFormDataCKAzon() {
      String var1 = "1";

      try {
         var1 = SettingsStore.getInstance().get("gui", "usableCKAzon");
         if (var1 == null) {
            var1 = "1";
         }
      } catch (Exception var3) {
         var1 = "1";
      }

      return var1;
   }

   private String getFormattedAdoszam(JTable var1, int var2, int var3) {
      String var4 = "";

      try {
         var4 = (String)var1.getModel().getValueAt(var2, var3 + 1);
      } catch (Exception var6) {
         var4 = "";
      }

      return this.getFormattedAdoszam(var4);
   }

   private String getFormattedAdoszam(String var1) {
      if (var1.length() > 8) {
         var1 = var1.substring(0, 8);
      }

      return var1;
   }

   private boolean checkCKAzonFilled() {
      for(int var1 = 0; var1 < this.fileTableModel.getRowCount(); ++var1) {
         if ("".equals(this.fileTableModel.getValueAt(var1, 2))) {
            return false;
         }
      }

      return true;
   }

   private String getInfoMessage() {
      return "\nAmennyiben még nem adott meg alapértelmezett Cég/Hivatali kapu azonosítót, a\n'Szerviz/Beállítások' menüpontban a 'Megjelenés' fülön megteheti.";
   }

   private boolean isCKErrorMessage(String var1) {
      if (var1 == null) {
         return false;
      } else {
         return var1.contains("A felhasználó nincs hozzárendelve a megadott") || var1.contains("A felhasználó részére a dokumentum művelet nem engedélyezett a megadott");
      }
   }

   class MyComboBoxRenderer extends JComboBox implements TableCellRenderer {
      public MyComboBoxRenderer(Vector var2) {
         super(var2);
      }

      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         this.handleItem(var2);
         return this;
      }

      public void setBackground(Color var1) {
         super.setBackground(Color.WHITE);
      }

      private void handleItem(Object var1) {
         if (((DefaultComboBoxModel)this.getModel()).getIndexOf(var1) == -1) {
            ((DefaultComboBoxModel)this.getModel()).addElement(var1);
         }

         this.setSelectedItem(var1);
      }
   }

   private class KrFileFileter extends FileFilter implements java.io.FileFilter {
      private KrFileFileter() {
      }

      public boolean accept(File var1) {
         if (var1.isDirectory()) {
            return true;
         } else {
            return var1.isFile() && var1.getName().toLowerCase().endsWith(".kr");
         }
      }

      public String getDescription() {
         return "kr fájlok (*.kr)";
      }

      // $FF: synthetic method
      KrFileFileter(Object var2) {
         this();
      }
   }

   private class TxtFileFileter extends FileFilter implements java.io.FileFilter {
      private TxtFileFileter() {
      }

      public boolean accept(File var1) {
         if (var1.isDirectory()) {
            return true;
         } else {
            return var1.isFile() && var1.getName().toLowerCase().endsWith(".txt");
         }
      }

      public String getDescription() {
         return "szöveges fájlok (*.txt)";
      }

      // $FF: synthetic method
      TxtFileFileter(Object var2) {
         this();
      }
   }
}
