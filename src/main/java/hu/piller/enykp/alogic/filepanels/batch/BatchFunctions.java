package hu.piller.enykp.alogic.filepanels.batch;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.ebev.Ebev;
import hu.piller.enykp.alogic.ebev.KrApp4PdfXml;
import hu.piller.enykp.alogic.ebev.Send2Mohu;
import hu.piller.enykp.alogic.ebev.SendParams;
import hu.piller.enykp.alogic.fileloader.dat.DatLoader;
import hu.piller.enykp.alogic.fileloader.imp.ImpLoader;
import hu.piller.enykp.alogic.fileloader.xml.PackedDataLoader;
import hu.piller.enykp.alogic.fileloader.xml.XMLPost;
import hu.piller.enykp.alogic.fileloader.xml.XkrLoader;
import hu.piller.enykp.alogic.fileloader.xml.XmlLoader;
import hu.piller.enykp.alogic.filepanels.ABEVOpenPanel;
import hu.piller.enykp.alogic.filepanels.attachement.AttachementTool;
import hu.piller.enykp.alogic.filepanels.attachement.EJFileChooser;
import hu.piller.enykp.alogic.filepanels.mohu.AVDHQuestionDialog;
import hu.piller.enykp.alogic.filepanels.mohu.LoginDialog;
import hu.piller.enykp.alogic.filepanels.mohu.LoginDialogFactory;
import hu.piller.enykp.alogic.filesaver.enykinner.EnykInnerSaver;
import hu.piller.enykp.alogic.filesaver.xml.ErrorListListener4XmlSave;
import hu.piller.enykp.alogic.fileutil.ExtendedTemplateData;
import hu.piller.enykp.alogic.fileutil.FileNameResolver;
import hu.piller.enykp.alogic.fileutil.FileStatusChecker;
import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.fileutil.XConverter;
import hu.piller.enykp.alogic.fileutil.XsdChecker;
import hu.piller.enykp.alogic.kontroll.ReadOnlyTableModel;
import hu.piller.enykp.alogic.settingspanel.upgrade.UpgradeFunction;
import hu.piller.enykp.alogic.templateutils.blacklist.BlacklistStore;
import hu.piller.enykp.datastore.CachedCollection;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.framework.MainPanel;
import hu.piller.enykp.gui.framework.Menubar;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IHelperLoad;
import hu.piller.enykp.interfaces.ILoadManager;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.interfaces.IResult;
import hu.piller.enykp.interfaces.ISaveManager;
import hu.piller.enykp.kauclient.KauAuthMethod;
import hu.piller.enykp.kauclient.KauAuthMethods;
import hu.piller.enykp.niszws.util.DapSessionHandler;
import hu.piller.enykp.niszws.util.GateType;
import hu.piller.enykp.niszws.util.KauAuthHelper;
import hu.piller.enykp.niszws.util.KauSessionTimeoutHandler;
import hu.piller.enykp.print.MainPrinter;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.TableSorter;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.EJList;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import hu.piller.enykp.util.base.tabledialog.TooltipTableHeader;
import hu.piller.krtitok.KriptoApp;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.zip.DataFormatException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.print.PrintService;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class BatchFunctions extends JDialog implements ActionListener, ItemListener, TableModelListener, KeyListener, WindowListener {
   private static final int XKR_WITH_TAG = 0;
   private static final int XKR_WITHOUT_TAG = 1;
   private static final int XML_WITH_TAG = 2;
   private static final int XML_WITHOUT_TAG = 3;
   private static final String ADD_BUTTON_NAME = "1";
   private static final String ADD2_BUTTON_NAME = "10";
   private static final String LIST_LOAD_BUTTON_NAME = "2";
   private static final String DEL_BUTTON_NAME = "3";
   private static final String LIST_SAVE_BUTTON_NAME = "4";
   private static final String IMP_BUTTON_NAME = "5";
   private static final String CANCEL_BUTTON_NAME = "6";
   private static final String LOG_OK_BUTTON_NAME = "7";
   private static final String LOG_SAVE_BUTTON_NAME = "8";
   private static final int ADD_BUTTON_VALUE = 1;
   private static final int ADD2_BUTTON_VALUE = 10;
   private static final int LIST_LOAD_BUTTON_VALUE = 2;
   private static final int DEL_BUTTON_VALUE = 3;
   private static final int START_BUTTON_VALUE = 5;
   private static final int LIST_SAVE_BUTTON_VALUE = 4;
   private static final int CANCEL_BUTTON_VALUE = 6;
   private static final int LOG_OK_BUTTON_VALUE = 7;
   private static final int LOG_SAVE_BUTTON_VALUE = 8;
   public static int MAIN_WIDTH = 790;
   public static final int MAIN_HEIGHT = 520;
   public static final int SUMMA_WIDTH = 400;
   public static final int SUMMA_HEIGHT = 400;
   public static final int OPEN_WIDTH = 600;
   public static final int OPEN_HEIGHT = 400;
   public static final String[] DI_KEYS = new String[]{"name", "tax_number", "person_name", "from_date", "to_date", "state", "info", "account_name", "save_date", "note", "ver", "templatever", "category", "org", "krfilename", "avdh_cst", "id"};
   private static final int COL_KRFILE = 15;
   private static final int COL_ID = 17;
   private static final int COL_VER = 11;
   private static final int COL_TEMPLATE_VER = 12;
   private static final int COL_ORG = 14;
   private static final int COL_AVDH = 16;
   String chStr = "000000";
   String defaultextension = ".frm.enyk";
   static IPropertyList iplMaster;
   static FileStatusChecker fileStatusChecker;
   static ILoadManager impLoadManager;
   static ILoadManager templateLoadManager;
   static ISaveManager xmlsaveManager;
   static String sysroot;
   static String root;
   static String dataPath;
   static String templatePath;
   static String importPath;
   static String destPath;
   static String krdir;
   static IHelperLoad ihl;
   private String[] resFilter;
   private JDialog summaDialog;
   private EJList logLista;
   private JScrollPane logSP;
   private JFrame mainFrame;
   private Object[] defaultFileColumns;
   private ReadOnlyTableModel fileTableModel;
   private TableSorter sorter;
   private JTable fileTable;
   private EJFileChooser fc;
   private BatchFunctions.TxtFileFileter tff;
   private BatchFunctions.ImportFileFileter iff;
   private JProgressBar status;
   private JLabel jl = new JLabel("     0 db fájl a listában");
   private JButton delButton;
   private JButton okButton;
   private JButton saveButton;
   private JButton addButton;
   private JButton add2Button;
   private JButton listButton;
   private JButton megsemButton;
   private JComboBox filters;
   int actualSelectedIndex = 0;
   private JCheckBox jcb1;
   private JCheckBox jcb2;
   private JCheckBox jcb3;
   private JCheckBox jcb4;
   private JCheckBox jcb5;
   private JCheckBox jcb6;
   private JCheckBox fileListaMenteseCB;
   private JCheckBox simplePrButton;
   private JRadioButton prButton;
   private JRadioButton jpgButton;
   private JRadioButton pdfButton;
   private JRadioButton onePdfButton;
   private ButtonGroup group;
   private Vector impHibaLista;
   private Vector saveFileList;
   private static boolean elindult = false;
   private static boolean folyamatban = false;
   private static boolean outOfMemory = false;
   private static File defaultDirectory = null;
   boolean tovabb = false;
   boolean alertTovabb = false;
   private boolean importFunction;
   private DatLoader dl;
   private XmlLoader xl;
   private ImpLoader il;
   private XkrLoader xkl;
   private Hashtable loaders;
   private PrintService ps;
   private static int cmdMode = 0;
   private Object cmdObject;
   private BookModel bm2Show;
   private PackedDataLoader pdl;
   String targetDir4Attachments;
   private static final Runtime s_runtime = Runtime.getRuntime();
   private int xkrMode;
   public static final int FROM_COMMAND_LINE = 0;
   public static final int FROM_MENU_IMPORT_1 = 1;

   public BatchFunctions(boolean var1, boolean var2, boolean var3) throws HeadlessException {
      super(MainFrame.thisinstance, var1 ? "Importálás" : "Csoportos műveletek", true);
      this.targetDir4Attachments = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.attachment") + File.separator;
      this.xkrMode = 0;
      String var4 = "EscPressedKey";
      this.getRootPane().getInputMap(2).put(KeyStroke.getKeyStroke(27, 0), var4);
      this.getRootPane().getActionMap().put(var4, new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            BatchFunctions.this.setVisible(false);
            BatchFunctions.this.dispose();
         }
      });
      PropertyList.getInstance().set("prop.dynamic.ilyenkor", "");
      cmdMode = 0;
      this.setDefaultCloseOperation(2);
      this.addWindowListener(this);
      this.setEditStateForBetoltErtek(var1 && var3);
      this.importFunction = var1;
      this.mainFrame = MainFrame.thisinstance;
      double var5 = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
      if (var5 < (double)MAIN_WIDTH) {
         MAIN_WIDTH = (int)var5;
      }

      this.news();

      try {
         this.init();
      } catch (NullPointerException var23) {
         var23.printStackTrace();
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "Alkalmazáshiba", "Hiba", 0);
         return;
      } catch (Exception var24) {
         var24.printStackTrace();
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiányzó paraméter : " + var24.getMessage().substring(1), "Hiba", 0);
         return;
      }

      this.initLogDialog();
      this.summaDialog.setSize(400, 400);
      this.summaDialog.setLocation(100, 100);
      this.tableSettings();
      this.fc.setMultiSelectionEnabled(true);
      this.setButtonState(false);
      int var7 = this.mainFrame.getX() + this.mainFrame.getWidth() / 2 - MAIN_WIDTH / 2;
      if (var7 < 0) {
         boolean var25 = false;
      }

      int var8 = this.mainFrame.getY() + this.mainFrame.getHeight() / 2 - 260;
      if (var8 < 0) {
         boolean var26 = false;
      }

      this.setBounds(100, 100, MAIN_WIDTH, 520);
      JPanel var9 = new JPanel(new BorderLayout());
      JPanel var10 = new JPanel(new BorderLayout(20, 0));
      JPanel var11 = new JPanel();
      JPanel var12 = new JPanel(new GridLayout(2, 1));
      JPanel var13 = new JPanel();
      JPanel var14 = new JPanel(new FlowLayout(0));
      if (var1) {
         var10.add(var14, "North");
      }

      this.addButton.setName("1");
      this.addButton.addActionListener(this);
      this.addButton.setToolTipText("Nyomtatvány hozzáadása a listához" + (var1 ? " (import mappából)" : ""));
      if (var1) {
         this.add2Button.setName("10");
         this.add2Button.addActionListener(this);
         this.add2Button.setToolTipText("Nyomtatvány hozzáadása a listához");
      }

      this.listButton.setName("2");
      this.listButton.addActionListener(this);
      this.listButton.setToolTipText("Lista betöltése fájlból");
      this.delButton.setName("3");
      this.delButton.addActionListener(this);
      this.delButton.setToolTipText("A kijelölt nyomtatványok törlése a listából");
      this.saveButton.setName("4");
      this.saveButton.addActionListener(this);
      this.saveButton.setToolTipText("A lista mentése fájlba");
      if (!var1) {
         var11.add(this.addButton);
      }

      if (var1) {
         var11.add(this.add2Button);
      }

      var11.add(this.listButton);
      var11.add(this.delButton);
      var11.add(this.saveButton);
      if (GuiUtil.modGui()) {
         this.fileTable.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
      }

      this.fileTable.setTableHeader(new TooltipTableHeader(this.fileTable.getColumnModel()));
      GuiUtil.setTableColWidth(this.fileTable);
      JScrollPane var15 = new JScrollPane(this.fileTable, 20, 30);
      Dimension var16 = new Dimension(MAIN_WIDTH, 350);
      var10.setPreferredSize(var16);
      var10.setMinimumSize(var16);
      var14.setBounds(5, 5, 590, 30);
      var15.setBounds(10, 40, this.getWidth(), 250);
      var10.add(var15, "Center");
      var10.add(var11, "South");
      if (var1) {
         var12.add(this.fileListaMenteseCB);
      }

      this.status.setString(" ");
      this.status.setIndeterminate(false);
      this.status.setStringPainted(true);
      this.status.setBorderPainted(false);
      var12.add(this.status);
      var9.add(var10, "Center");
      var9.add(var12, "South");
      this.getContentPane().add(var9, "Center");
      this.okButton.setName("5");
      this.okButton.setSize(new Dimension(GuiUtil.getW(this.okButton, this.okButton.getText()), GuiUtil.getCommonItemHeight() + 6));
      this.okButton.setPreferredSize(this.okButton.getSize());
      this.okButton.addActionListener(this);
      this.megsemButton.setName("6");
      this.megsemButton.setSize(new Dimension(GuiUtil.getW(this.megsemButton, this.megsemButton.getText()), GuiUtil.getCommonItemHeight() + 6));
      this.megsemButton.setPreferredSize(this.megsemButton.getSize());
      this.megsemButton.addActionListener(this);
      var13.add(this.okButton);
      var13.add(this.megsemButton);
      var13.setPreferredSize(new Dimension(MAIN_WIDTH, GuiUtil.getCommonItemHeight() + 14));
      JPanel var17 = new JPanel(new BorderLayout());
      JPanel var18 = new JPanel(new FlowLayout(0));
      JPanel var19 = new JPanel(new FlowLayout(0));
      var18.setPreferredSize(new Dimension(MAIN_WIDTH, GuiUtil.getCommonItemHeight() + 10));
      var18.setMinimumSize(var18.getSize());
      var19.setPreferredSize(new Dimension(MAIN_WIDTH, GuiUtil.getCommonItemHeight() + 10));
      var19.setMinimumSize(var19.getSize());
      JLabel var20 = new JLabel("   Válasszon csoportos funkciót :");
      var20.setPreferredSize(new Dimension(MAIN_WIDTH, GuiUtil.getCommonItemHeight() + 4));
      this.jcb1.setFocusPainted(false);
      this.jcb1.setSelected(true);
      this.jcb2.setFocusPainted(false);
      this.jcb3.setFocusPainted(false);
      this.jcb4.setFocusPainted(false);
      this.jcb5.setFocusPainted(false);
      this.jcb6.setFocusPainted(false);
      this.addRemoveListeners(true);
      var18.add(this.jcb3);
      var18.add(this.jcb4);
      var18.add(this.jcb5);
      var18.add(this.jcb6);
      this.prButton = GuiUtil.getANYKRadioButton("nyomtatóra");
      this.jpgButton = GuiUtil.getANYKRadioButton("jpg fájlba");
      this.pdfButton = GuiUtil.getANYKRadioButton("pdf fájlba");
      this.pdfButton.setToolTipText("Több részbizonylatot tartalmazó ún. 'kötegelt nyomtatvány' esetén az egyes részbizonylatokat külön pdf fájlba írjuk.");
      this.onePdfButton = GuiUtil.getANYKRadioButton("kötegelt nyomtatványt egy pdf fájlba");
      this.onePdfButton.setToolTipText("Több részbizonylatot tartalmazó ún. 'kötegelt nyomtatvány' esetén a részbizonylatokat egyetlen pdf fájlba írjuk.");
      this.simplePrButton = GuiUtil.getANYKCheckBox("kivonatolt nyomtatás");
      this.simplePrButton.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent var1) {
            BatchFunctions.this.jpgButton.setVisible(BatchFunctions.this.jcb4.isSelected() && !BatchFunctions.this.simplePrButton.isSelected());
            BatchFunctions.this.onePdfButton.setVisible(BatchFunctions.this.jcb4.isSelected());
            BatchFunctions.this.prButton.setSelected(true);
         }
      });
      this.simplePrButton.setSelected(true);
      this.group = new ButtonGroup();
      this.group.add(this.prButton);
      this.group.add(this.pdfButton);
      this.group.add(this.onePdfButton);
      this.group.add(this.jpgButton);
      var19.add(this.simplePrButton);
      var19.add(this.prButton);
      var19.add(this.pdfButton);
      var19.add(this.onePdfButton);
      var19.add(this.jpgButton);
      this.prButton.setVisible(this.jcb4.isSelected());
      this.prButton.setSelected(true);
      this.jpgButton.setVisible(this.jcb4.isSelected() && !this.simplePrButton.isSelected());
      this.pdfButton.setVisible(this.jcb4.isSelected());
      this.onePdfButton.setVisible(this.jcb4.isSelected());
      this.simplePrButton.setVisible(this.jcb4.isSelected());
      JPanel var21 = new JPanel();
      var21.setLayout(new BoxLayout(var21, 1));
      var21.add(var18);
      var21.add(var19);
      var21.setSize(new Dimension((int)var18.getPreferredSize().getWidth(), (int)(var18.getPreferredSize().getHeight() + var19.getPreferredSize().getHeight())));
      var21.setPreferredSize(var21.getSize());
      var21.setMinimumSize(var21.getSize());
      this.jl.setPreferredSize(new Dimension(GuiUtil.getW(this.jl, "WWWW db fájl a listában"), GuiUtil.getCommonItemHeight() + 4));
      var17.add(var20, "North");
      var17.add(var21, "Center");
      var17.add(this.jl, "South");
      Dimension var22 = new Dimension(this.getCbPanelWidth(), (int)(var20.getPreferredSize().getHeight() + var21.getPreferredSize().getHeight() + this.jl.getPreferredSize().getHeight()));
      var17.setPreferredSize(var22);
      var17.setSize(var22);
      var17.setMinimumSize(var22);
      if (!var1) {
         this.getContentPane().add(var17, "North");
      } else {
         var14.add(this.jl);
         this.jcb1.setSelected(true);
         this.chStr = "100000";
      }

      this.getContentPane().add(var13, "South");
      if (var2) {
         this.pack();
         this.setVisible(true);
      }

   }

   private void init() throws Exception {
      iplMaster = PropertyList.getInstance();
      if (this.importFunction) {
         this.loaders.put(".dat", this.dl);
         this.loaders.put(".abv", this.dl);
         this.loaders.put(".kat", this.dl);
         this.loaders.put(".elk", this.dl);
         this.loaders.put(this.xl.getFileNameSuffix(), this.xl);
         this.loaders.put(this.il.getFileNameSuffix(), this.il);
         this.loaders.put(".xkr", this.xkl);
         this.loaders.put(".xcz", this.pdl);
         this.filters.addItem(this.dl.getDescription() + " (" + this.dl.getFileNameSuffix() + ")");
         this.filters.addItem(this.xl.getDescription() + " (" + this.xl.getFileNameSuffix() + ")");
         this.filters.addItem(this.il.getDescription() + " (" + this.il.getFileNameSuffix() + ")");
         this.filters.addItem(this.xkl.getDescription() + " (" + this.xkl.getFileNameSuffix() + ")");
         this.filters.addItem(this.pdl.getDescription() + " (" + this.pdl.getFileNameSuffix() + ")");
         this.resFilter = new String[]{this.dl.getId(), this.xl.getId(), this.il.getId(), this.xkl.getId(), this.pdl.getId()};
         this.filters.addItemListener(this);
      } else {
         this.loaders.put(".enyk", new CachedCollection());
         this.filters.addItem("Nyomtatványok");
         this.resFilter = new String[]{"inner_data_loader_v1"};
      }

      try {
         sysroot = (String)iplMaster.get("prop.sys.root");
         if (sysroot == null) {
            throw new Exception();
         }
      } catch (Exception var5) {
         throw new Exception("*prop.sys.root");
      }

      if (!sysroot.endsWith("\\") && !sysroot.endsWith("/")) {
         sysroot = sysroot + File.separator;
      }

      try {
         root = (String)iplMaster.get("prop.usr.root");
         if (root == null) {
            throw new Exception();
         }
      } catch (Exception var4) {
         throw new Exception("*prop.usr.root");
      }

      if (!root.endsWith("\\") && !root.endsWith("/")) {
         root = root + File.separator;
      }

      try {
         importPath = (String)iplMaster.get("prop.usr.import");
         if (importPath == null) {
            importPath = "";
         }

         importPath = root + importPath;
      } catch (Exception var3) {
         importPath = root + importPath;
      }

      if (!importPath.endsWith("\\") && !importPath.endsWith("/")) {
         importPath = importPath + File.separator;
      }

      try {
         dataPath = (String)iplMaster.get("prop.usr.saves");
         if (dataPath == null) {
            throw new Exception();
         }

         dataPath = root + dataPath;
      } catch (Exception var7) {
         throw new Exception("*prop.usr.saves");
      }

      if (!dataPath.endsWith("\\") && !dataPath.endsWith("/")) {
         dataPath = dataPath + File.separator;
      }

      try {
         templatePath = (String)iplMaster.get("prop.dynamic.templates.absolutepath");
      } catch (Exception var2) {
         throw new Exception("*prop.dynamic.templates.absolutepath");
      }

      if (!templatePath.endsWith("\\") && !templatePath.endsWith("/")) {
         templatePath = templatePath + File.separator;
      }

      if (this.importFunction) {
         this.fc.setCurrentDirectory(new File(importPath));
      } else {
         this.fc.setCurrentDirectory(new File(dataPath));
      }

      defaultDirectory = this.fc.getCurrentDirectory();
      krdir = (String)iplMaster.get("prop.usr.krdir");
      if (krdir.length() == 0) {
         throw new Exception("*KRDIR környezeti változó");
      } else {
         if (!krdir.endsWith("\\") && !krdir.endsWith("/")) {
            krdir = krdir + File.separator;
         }

         try {
            destPath = (String)iplMaster.get("prop.usr.ds_dest");
            if (!destPath.endsWith(File.separator)) {
               destPath = destPath + File.separator;
            }

            destPath = krdir + destPath;
         } catch (Exception var6) {
            throw new Exception("*prop.usr.ds_dest");
         }

         fileStatusChecker = FileStatusChecker.getInstance();
      }
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

   private void initDialog(String var1) {
      this.fc.setDialogTitle(var1);
      if (defaultDirectory != null) {
         this.fc.setCurrentDirectory(defaultDirectory);
      }

      try {
         ((BasicFileChooserUI)this.fc.getUI()).setFileName("");
      } catch (ClassCastException var5) {
         try {
            this.fc.setSelectedFile(new File(""));
         } catch (Exception var4) {
            Tools.eLog(var4, 0);
         }
      }

      this.fc.setSelectedFile((File)null);
   }

   private void doAdd() {
      ABEVOpenPanel var1 = new ABEVOpenPanel();

      try {
         var1.setSize(600, 400);
         var1.setLocationRelativeTo(MainFrame.thisinstance);
         var1.setFilters(new String[]{this.resFilter[this.filters.getSelectedIndex()]});
         String var2 = "";
         if (this.filters.getItemCount() != 1) {
            var1.setSelectedPath((new File(importPath)).toURI());
            var2 = importPath;
         } else {
            var1.setSelectedPath((new File(dataPath)).toURI());
            var2 = dataPath;
         }

         var1.setMode("open_import");
         var1.setTitle("Nyomtatvány hozzáadása " + this.beauty(var2));
         Hashtable var3 = var1.showDialog();
         if (var3 == null) {
            return;
         }

         Object[] var4 = (Object[])((Object[])var3.get("selected_files"));
         this.addFilesToList(var4);
         var3 = null;
      } finally {
         var1.setFilters((String[])null);
         var1 = null;
      }

   }

   private void doAdd2() {
      Vector var1 = new Vector();
      this.initDialog("Megnyitás");

      try {
         FileFilter[] var2 = this.fc.getChoosableFileFilters();
         this.fc.removeChoosableFileFilter(var2[0]);
      } catch (Exception var7) {
         Tools.eLog(var7, 0);
      }

      this.fc.addChoosableFileFilter(this.iff);
      int var8 = this.fc.showOpenDialog(this);
      if (var8 == 0) {
         defaultDirectory = this.fc.getCurrentDirectory();
         File[] var3 = this.fc.getSelectedFiles();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            try {
               this.addFileFromFChooser(var3[var4].getAbsolutePath(), var1);
            } catch (Exception var6) {
               Tools.eLog(var6, 0);
            }
         }

         if (var1.size() > 0) {
            this.fillDialog("Lista betöltése", var1);
         }
      }

      this.fc.removeChoosableFileFilter(this.iff);
   }

   private void addFileFromFChooser(String var1, Vector var2) throws Exception {
      var1 = this.beauty(var1);
      String[] var3 = var1.split(";");

      try {
         if (var3[0].indexOf(".") == -1) {
            var2.add(new TextWithIcon(var3[0] + " - nem megfelelő kiterjesztés", 0));
            return;
         }

         if (!this.loaders.containsKey(var3[0].substring(var3[0].lastIndexOf(".")).toLowerCase())) {
            var2.add(new TextWithIcon(var3[0] + " - nem megfelelő kiterjesztés", 0));
            return;
         }

         if (var3[0].indexOf(File.separator) == -1) {
            var3[0] = defaultDirectory.getAbsolutePath() + File.separator + var3[0];
         }

         File var4 = new File(var3[0]);
         if (!var4.exists()) {
            return;
         }

         if (var3.length == 1 || var3[1].length() == 0) {
            this.doLoadHeadData(var4, (String)null);
            return;
         }
      } catch (Exception var5) {
         var2.add(new TextWithIcon(var3[0] + " - hiba a beolvasáskor", 0));
         return;
      }

      this.addToList(var3, var2);
   }

   private boolean addToList(String[] var1, Vector var2) {
      Vector var3 = new Vector();

      int var4;
      for(var4 = 0; var4 < var1.length; ++var4) {
         var3.add(var1[var4]);
      }

      for(var4 = var1.length; var4 < DI_KEYS.length; ++var4) {
         var3.add("");
      }

      if (!this.alreadyInList(var1)) {
         this.fileTableModel.addRow(var1);
         return true;
      } else {
         var2.add(new TextWithIcon(var1[0] + " - a fájl már a listában van", 0));
         return false;
      }
   }

   private void doDel() {
      if (this.fileTable.getSelectedRows().length > 0 && JOptionPane.showOptionDialog(this, "Biztosan törli a kijelölt fájlokat a listából?", "Kérdés", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0) {
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

   private void doLoad() {
      Vector var1 = new Vector();
      this.initDialog("Megnyitás");
      this.fc.addChoosableFileFilter(this.tff);
      int var2 = this.fc.showOpenDialog(this);
      if (var2 == 0) {
         defaultDirectory = this.fc.getCurrentDirectory();
         File[] var3 = this.fc.getSelectedFiles();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            try {
               this.parseFile(var3[var4].getAbsolutePath(), var1, true);
            } catch (Exception var6) {
               Tools.eLog(var6, 0);
            }
         }

         if (var1.size() > 0) {
            this.fillDialog("Lista betöltése", var1);
         }
      }

      this.fc.removeChoosableFileFilter(this.tff);
   }

   private void doSaveList() {
      this.initDialog("Mentés");
      this.fc.addChoosableFileFilter(this.tff);
      if (this.importFunction) {
         try {
            this.fc.setCurrentDirectory(new File(importPath));
         } catch (Exception var13) {
            Tools.eLog(var13, 0);
         }

         try {
            ((BasicFileChooserUI)this.fc.getUI()).setFileName("import_lista.txt");
         } catch (ClassCastException var12) {
            try {
               this.fc.setSelectedFile(new File("import_lista.txt"));
            } catch (Exception var11) {
               Tools.eLog(var11, 0);
            }
         }
      } else {
         try {
            this.fc.setCurrentDirectory(new File(dataPath));
         } catch (Exception var10) {
            Tools.eLog(var10, 0);
         }

         try {
            ((BasicFileChooserUI)this.fc.getUI()).setFileName("csoportos_muveletek_lista.txt");
         } catch (ClassCastException var9) {
            try {
               this.fc.setSelectedFile(new File("csoportos_muveletek_lista.txt"));
            } catch (Exception var8) {
               Tools.eLog(var8, 0);
            }
         }
      }

      boolean var1;
      do {
         var1 = true;
         int var2 = this.fc.showSaveDialog(this);
         if (var2 == 0) {
            File var3 = this.fc.getSelectedFile();
            if (var3.exists()) {
               var1 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Ilyen nevű fájl már létezik. Felülírjuk?", "Csoportos műveletek", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0;
            }

            if (var1) {
               FileOutputStream var4 = null;

               try {
                  var4 = new FileOutputStream(var3);
                  Vector var5 = this.fileTableModel.getDataVector();

                  for(int var6 = 0; var6 < var5.size(); ++var6) {
                     var4.write((this.beauty(this.parseVector((Vector)var5.get(var6))) + "\r\n").getBytes());
                  }

                  var4.close();
               } catch (Exception var14) {
                  try {
                     var4.close();
                  } catch (Exception var7) {
                     Tools.eLog(var7, 0);
                  }

                  GuiUtil.showMessageDialog(this.mainFrame, "A lista mentése nem sikerült!", "Hiba", 0);
               }

               defaultDirectory = this.fc.getCurrentDirectory();
            }
         }
      } while(!var1);

      this.fc.removeChoosableFileFilter(this.tff);
   }

   private void parseFile(String var1, Vector var2, boolean var3) throws Exception {
      var1 = this.beauty(var1);
      File var4 = new File(var1);
      if (!var4.exists()) {
         if (var3) {
            GuiUtil.showMessageDialog(this.mainFrame, "Nem található a listafájl!", "Hiba", 0);
         }

      } else {
         BufferedReader var6 = new BufferedReader(new FileReader(var4));
         boolean var7 = false;

         while(true) {
            String[] var8;
            while(true) {
               String var5;
               do {
                  if ((var5 = var6.readLine()) == null) {
                     if (!var7) {
                        var2.add(new TextWithIcon("Üres listaállományt választott! (" + var1 + ")", 0));
                     }

                     return;
                  }
               } while(var5.trim().equals(""));

               var7 = true;
               var5 = this.beauty(var5);
               if (var5.endsWith(";;")) {
                  var5 = var5.substring(0, var5.length() - 1) + " ;";
               }

               var8 = var5.split(";");
               if (var8.length > 12 && " ".equals(var8[14])) {
                  var8[14] = "";
               }

               try {
                  if (var8[0].indexOf(".") == -1) {
                     var2.add(new TextWithIcon(var8[0] + " - nem megfelelő kiterjesztés", 0));
                  } else if (!this.loaders.containsKey(var8[0].substring(var8[0].lastIndexOf(".")).toLowerCase())) {
                     var2.add(new TextWithIcon(var8[0] + " - nem megfelelő kiterjesztés", 0));
                  } else {
                     if (var8[0].indexOf(File.separator) == -1) {
                        var8[0] = defaultDirectory.getAbsolutePath() + File.separator + var8[0];
                     }

                     File var9 = new File(var8[0]);
                     if (!var9.exists()) {
                        var9 = new File(var1.substring(0, var1.lastIndexOf(File.separator) + 1) + var8[0]);
                        if (!var9.exists()) {
                           var2.add(new TextWithIcon(var8[0] + " - nem található a fájl", 0));
                           continue;
                        }
                     }

                     if (var8.length != 1 && var8[1].length() != 0) {
                        break;
                     }

                     this.doLoadHeadData(var9, (String)null);
                  }
               } catch (Exception var10) {
                  var2.add(new TextWithIcon(var8[0] + " - hiba a beolvasáskor", 0));
               }
            }

            this.addToList(var8, var2);
         }
      }
   }

   private void parseFile2(String var1, Vector var2) throws Exception {
      var1 = this.beauty(var1);
      File var3 = new File(var1);
      String[] var4 = var3.list();

      for(int var6 = 0; var6 < var4.length; ++var6) {
         String var5 = var1 + File.separator + var4[var6];
         if (var5.toLowerCase().endsWith(".frm.enyk")) {
            var5 = this.beauty(var5);
            String[] var7 = var5.split(";");

            try {
               if (var7[0].indexOf(".") == -1) {
                  var2.add(new TextWithIcon(var7[0] + " - nem megfelelő kiterjesztés", 0));
                  continue;
               }

               if (!this.loaders.containsKey(var7[0].substring(var7[0].lastIndexOf(".")).toLowerCase())) {
                  var2.add(new TextWithIcon(var7[0] + " - nem megfelelő kiterjesztés", 0));
                  continue;
               }

               if (var7[0].indexOf(File.separator) == -1) {
                  var7[0] = defaultDirectory.getAbsolutePath() + File.separator + var7[0];
               }

               File var8 = new File(var7[0]);
               if (!var8.exists()) {
                  var8 = new File(var1.substring(0, var1.lastIndexOf(File.separator) + 1) + var7[0]);
                  if (!var8.exists()) {
                     var2.add(new TextWithIcon(var7[0] + " - nem található a fájl", 0));
                     continue;
                  }
               }

               if (var7.length == 1 || var7[1].length() == 0) {
                  this.doLoadHeadData(var8, (String)null);
                  continue;
               }
            } catch (Exception var9) {
               var2.add(new TextWithIcon(var7[0] + " - hiba a beolvasáskor", 0));
               continue;
            }

            this.addToList(var7, var2);
         }
      }

   }

   private String doImp() {
      try {
         if (this.fileTableModel.getRowCount() == 0) {
            GuiUtil.showMessageDialog(this.mainFrame, "Nincs elem a listában!", "Üzenet", 1);
            MainFrame.thisinstance.glasslock = false;
            MainFrame.thisinstance.setGlassLabel((String)null);
            return null;
         }

         if (this.chStr.equals("000000") || !this.importFunction && this.chStr.equals("100000")) {
            GuiUtil.showMessageDialog(this.mainFrame, "Nem választott ki műveletet!", "Üzenet", 1);
            MainFrame.thisinstance.glasslock = false;
            MainFrame.thisinstance.setGlassLabel((String)null);
            return null;
         }

         this.sortFileList();
         this.impHibaLista.clear();
         this.saveFileList.clear();
         Vector var1 = null;
         if (this.chStr.charAt(1) == '1' || this.chStr.charAt(2) == '1' || this.chStr.charAt(4) == '1') {
            var1 = this.checkStatuses(0);
            if (this.chStr.charAt(4) == '1') {
               if (var1.size() > 0) {
                  StringBuffer var12 = new StringBuffer();

                  for(int var14 = 0; var14 < var1.size(); ++var14) {
                     var12.append(var1.elementAt(var14)).append("\n");
                  }

                  GuiUtil.showMessageDialog(this.mainFrame, "Az alábbi fájlok nem \"módosítható\" állapotúak. A kijelölt műveletek nem végezhetőek el!\n" + var12.toString(), "Üzenet", 1);
                  return null;
               }

               var1 = null;
            }
         }

         if (this.jcb4.isSelected() || this.jcb5.isSelected()) {
            this.alertTovabb = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Amennyiben a nyomtatvány ellenőrzése hibát mutat, nyomtassuk/megjelöljük feladásra a nyomtatványt?", "Csoportos műveletek", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0;
         }

         if (this.jcb5.isSelected() && this.handleAVDH(this.fileTableModel)) {
            KauAuthHelper.getInstance().setSaveAuthData(true);
            new AVDHQuestionDialog(MainFrame.thisinstance);
            int var2 = (Integer)PropertyList.getInstance().get("prop.dynamic.AVDHQuestionDialogAnswer");
            PropertyList.getInstance().set("prop.dynamic.AVDHQuestionDialogAnswer", (Object)null);
            if (var2 != 0) {
               return null;
            }

            if (PropertyList.getInstance().get("prop.dynamic.avdhWithNoAuth") == null) {
               PropertyList.getInstance().set("prop.dynamic.avdh_direct_from_menu", "1");
               LoginDialog var3 = LoginDialogFactory.create(GateType.UGYFELKAPU, 1, false, (String)null);
               if (var3 == null) {
                  return null;
               }

               var3.setLocationRelativeTo(MainFrame.thisinstance);
               if (KauAuthMethods.getSelected() != KauAuthMethod.KAU_DAP) {
                  boolean var4 = var3.showIfNeed();
                  PropertyList.getInstance().set("prop.dynamic.avdh_direct_from_menu", (Object)null);
                  if (var4 && var3.getState() < 2) {
                     return null;
                  }
               }
            }

            PropertyList.getInstance().set("prop.dynamic.mohu_user_and_pass_from_batch", true);
         }

         if (this.jcb4.isSelected() && (this.prButton.isSelected() || this.simplePrButton.isSelected() && this.prButton.isSelected())) {
            PrinterJob var10 = PrinterJob.getPrinterJob();
            if (!var10.printDialog()) {
               return null;
            }

            this.ps = var10.getPrintService();
         }

         if (this.simplePrButton.isSelected() && (this.pdfButton.isSelected() || this.onePdfButton.isSelected())) {
            this.ps = PrinterJob.getPrinterJob().getPrintService();
         }

         this.enableAll(false);
         final JDialog var11 = new JDialog(this.mainFrame, "Importálás", true);
         var11.setDefaultCloseOperation(0);
         final boolean[] var13 = new boolean[]{false};
         Vector finalVar = var1;
         SwingWorker var5 = new SwingWorker() {
            int importedFileCount = 0;
            int fileCount;

            {
               this.fileCount = BatchFunctions.this.fileTableModel.getRowCount();
            }

            public Object doInBackground() {
               Result var1x = null;
               BookModel var2 = null;
               BookModel var3 = null;

               try {
                  if (BatchFunctions.cmdMode == 0) {
                     TemplateChecker.getInstance().setNeedDialog4Download(false);
                  }

                  IErrorList var4 = ErrorList.getInstance();
                  ErrorListListener4XmlSave var5 = new ErrorListListener4XmlSave(-1);
                  Vector var6 = null;
                  BatchFunctions.folyamatban = true;
                  BatchFunctions.this.filters.setEnabled(false);
                  if (BatchFunctions.this.fileTableModel.getRowCount() != 0) {
                     BatchFunctions.this.impHibaLista.add(new TextWithIcon("Start : " + BatchFunctions.this.getTimeString("yyyy.MM.dd HH.mm.ss"), -1));
                     BatchFunctions.this.impHibaLista.add(new TextWithIcon("Elvégzett műveletek :", -1));
                     if (BatchFunctions.this.jcb2.isSelected()) {
                        BatchFunctions.this.impHibaLista.add(new TextWithIcon("   - " + BatchFunctions.this.jcb2.getText(), -1));
                     }

                     if (BatchFunctions.this.jcb3.isSelected()) {
                        BatchFunctions.this.impHibaLista.add(new TextWithIcon("   - " + BatchFunctions.this.jcb3.getText(), -1));
                     }

                     if (BatchFunctions.this.jcb4.isSelected()) {
                        BatchFunctions.this.impHibaLista.add(new TextWithIcon("   - " + BatchFunctions.this.jcb4.getText(), -1));
                     }

                     if (BatchFunctions.this.jcb5.isSelected()) {
                        BatchFunctions.this.impHibaLista.add(new TextWithIcon("   - " + BatchFunctions.this.jcb5.getText(), -1));
                     }

                     if (BatchFunctions.this.jcb6.isSelected()) {
                        BatchFunctions.this.impHibaLista.add(new TextWithIcon("   - " + BatchFunctions.this.jcb6.getText(), -1));
                     }

                     BatchFunctions.this.impHibaLista.add(new TextWithIcon("-------------------------------------------", -1));
                  }

                  boolean var7 = true;
                  var1x = new Result();

                  while(BatchFunctions.this.fileTableModel.getRowCount() != 0 && BatchFunctions.folyamatban) {
                     var6 = null;
                     BatchFunctions.this.impHibaLista.add(new TextWithIcon(" ", -1));
                     BatchFunctions.this.impHibaLista.add(new TextWithIcon("[" + BatchFunctions.this.fileTableModel.getValueAt(0, 0) + "]", -1));
                     if (BatchFunctions.this.chStr.charAt(5) == '1') {
                        if (BatchFunctions.this.doVisszavonas((String)BatchFunctions.this.fileTableModel.getValueAt(0, 0), (String)BatchFunctions.this.fileTableModel.getValueAt(0, 15), (String)BatchFunctions.this.fileTableModel.getValueAt(0, 17))) {
                           ++this.importedFileCount;
                        }
                     } else {
                        BatchFunctions.this.status.setIndeterminate(true);
                        BatchFunctions.this.status.setString("     " + BatchFunctions.this.fileTableModel.getValueAt(0, 0) + " fájl betöltése folyamatban...");

                        String var8;
                        int var9;
                        int var10;
                        int var11x;
                        int var12;
                        int var13x;
                        String[] var105;
                        try {
                           var8 = ((String)BatchFunctions.this.fileTableModel.getValueAt(0, 0)).substring(((String)BatchFunctions.this.fileTableModel.getValueAt(0, 0)).lastIndexOf("."));
                           if (!BatchFunctions.this.loaders.containsKey(var8.toLowerCase())) {
                              BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - Hibás a fájl neve! (" + var8 + ")", 0));
                              BatchFunctions.this.fileTableModel.removeRow(0);
                              continue;
                           }

                           if (!BatchFunctions.this.importFunction) {
                              var2 = BatchFunctions.this.loadInnerData(var3);
                              var3 = var2;
                           } else {
                              if (BatchFunctions.cmdMode > 0) {
                                 PropertyList.getInstance().set("prop.dynamic.xml_loader_call", UpgradeFunction.CUST_IMP);
                              } else {
                                 PropertyList.getInstance().set("prop.dynamic.xml_loader_call", (Object)null);
                              }

                              var2 = ((ILoadManager)BatchFunctions.this.loaders.get(var8.toLowerCase())).load((String)BatchFunctions.this.fileTableModel.getValueAt(0, 0), (String)BatchFunctions.this.fileTableModel.getValueAt(0, 17), (String)BatchFunctions.this.fileTableModel.getValueAt(0, 11), (String)BatchFunctions.this.fileTableModel.getValueAt(0, 14), var3);
                              PropertyList.getInstance().set("prop.dynamic.xml_loader_call", (Object)null);
                              var3 = var2;
                           }

                           if (var2 == null) {
                              if (BatchFunctions.cmdMode > 0) {
                                 var1x.setOk(false);
                                 var1x.errorList.add("Hiba a fájl betöltésekor");
                              }

                              throw new Exception();
                           }

                           var9 = var2.carryOnTemplate();
                           switch(var9) {
                           case 0:
                              BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - Hiba történt a nyomtatvány betöltésekor!", 1));
                              BatchFunctions.this.impHibaLista.add(new TextWithIcon("    " + BookModel.CHECK_VALID_MESSAGES[var9], -1));
                              BatchFunctions.this.fileTableModel.removeRow(0);
                              if (BatchFunctions.cmdMode > 0) {
                                 var1x.setOk(false);
                                 var1x.errorList.add("Hiba a fájl betöltésekor: " + BookModel.CHECK_VALID_MESSAGES[var9]);
                                 throw new Exception();
                              }
                              continue;
                           case 1:
                              var10 = Tools.handleTemplateCheckerResult(var2);
                              if (var10 >= 4) {
                                 BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - Hiba történt a nyomtatvány betöltésekor!", 1));
                                 BatchFunctions.this.impHibaLista.add(new TextWithIcon("    " + BookModel.CHECK_VALID_MESSAGES[var10], -1));
                                 BatchFunctions.this.fileTableModel.removeRow(0);
                                 if (BatchFunctions.cmdMode > 0) {
                                    var1x.setOk(false);
                                    var1x.errorList.add("Hiba a fájl betöltésekor: " + BookModel.CHECK_VALID_MESSAGES[var10]);
                                    throw new Exception();
                                 }
                                 continue;
                              }
                           case 2:
                           default:
                              break;
                           case 3:
                              System.out.println("HIBA_AZ_ELLENORZESKOR");
                           }

                           if (var2.hasError) {
                              BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - Hiba történt a nyomtatvány betöltésekor!", 1));
                              if (var2.errorlist != null) {
                                 BatchFunctions.this.impHibaLista.addAll(var2.errorlist);
                              }

                              try {
                                 var105 = var2.errormsg.split("##");
                                 String[] var120 = var105[0].split(" bl_url ");
                                 BatchFunctions.this.impHibaLista.add(new TextWithIcon("           " + var120[0], -1));
                                 if (var120.length > 1) {
                                    BatchFunctions.this.impHibaLista.add(new TextWithIcon("           " + var120[1], -1));
                                 }

                                 if (var105.length > 1) {
                                    BatchFunctions.this.impHibaLista.add(new TextWithIcon("           " + var105[1], -1));
                                 }
                              } catch (Exception var85) {
                                 Tools.eLog(var85, 0);
                              }

                              BatchFunctions.this.fileTableModel.removeRow(0);
                              if (BatchFunctions.cmdMode > 0) {
                                 var1x.setOk(false);
                                 if (var2.errormsg != null) {
                                    var105 = var2.errormsg.split(" bl_url ");
                                    var1x.errorList.add("Hiba történt a nyomtatvány betöltésekor! (" + var105[0] + ")");
                                    if (var105.length > 1) {
                                       var1x.errorList.add(var105[1]);
                                    }
                                 }
                              }
                              continue;
                           }

                           if (var2.errormsg != null && var2.errormsg.length() > 0) {
                              var105 = var2.errormsg.split(";");

                              for(var11x = 0; var11x < var105.length; ++var11x) {
                                 BatchFunctions.this.impHibaLista.add(new TextWithIcon(var105[var11x], 4));
                                 if (BatchFunctions.cmdMode > 0) {
                                    var1x.setOk(false);
                                    var1x.errorList.add(var105[var11x]);
                                 }
                              }
                           }

                           if (var2.warninglist != null) {
                              BatchFunctions.this.impHibaLista.addAll(var2.warninglist);
                           }

                           if (BatchFunctions.this.importFunction || BatchFunctions.this.chStr.charAt(1) == '1') {
                              XConverter.convert(var2);
                              var10 = ErrorList.getInstance().getItems().length;
                              CalculatorManager.getInstance().doBetoltErtekCalcs(false);
                              CalculatorManager.getInstance().multiform_calc();
                              Object[] var110 = ErrorList.getInstance().getItems();
                              if (var110.length > var10) {
                                 BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - Hiba történt a mezők újraszámításakor", 0));
                                 if (BatchFunctions.cmdMode > 0) {
                                    var1x.setOk(false);
                                    var1x.errorList.add("Hiba történt a mezők újraszámításakor");
                                 }

                                 var12 = 0;

                                 for(var13x = var10; var13x < var110.length; ++var13x) {
                                    Object[] var14 = (Object[])((Object[])var110[var13x]);
                                    if (!((Object[])((Object[])var110[var13x]))[0].toString().equalsIgnoreCase("frissítés")) {
                                       ++var12;
                                       if (var14[1] != null) {
                                          BatchFunctions.this.impHibaLista.add(new TextWithIcon("    " + var14[1] + (var14[2] != null ? var14[2] : ""), 0));
                                       }

                                       if (BatchFunctions.cmdMode > 0) {
                                          var1x.errorList.add("" + var14[1] + (var14[2] != null ? var14[2] : ""));
                                       }
                                    }
                                 }

                                 if (var12 != 0) {
                                    BatchFunctions.this.fileTableModel.removeRow(0);
                                    continue;
                                 }

                                 BatchFunctions.this.impHibaLista.removeElementAt(BatchFunctions.this.impHibaLista.size() - 1);
                                 if (BatchFunctions.cmdMode > 0) {
                                    var1x.setOk(true);
                                    var1x.errorList.removeElementAt(var1x.errorList.size() - 1);
                                 }
                              }
                           }
                        } catch (Exception var99) {
                           BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - Hiba történt a fájl betöltésekor", 0));
                           if (BatchFunctions.this.fileTableModel.getRowCount() > 0) {
                              BatchFunctions.this.fileTableModel.removeRow(0);
                           }

                           var99.printStackTrace();
                           if (BatchFunctions.cmdMode > 0) {
                              var1x.setOk(false);
                              var1x.errorList.add("Hiba a fájl betöltésekor");
                           }
                           continue;
                        }

                        if (BatchFunctions.this.chStr.charAt(2) == '1') {
                           if (finalVar != null) {
                              var7 = !finalVar.contains(BatchFunctions.this.fileTableModel.getValueAt(0, 0));
                              if (!var7) {
                                 BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - A nyomtatvány nem 'módosítható' állapotú, az ellenőrzés nem hajtható végre!", 0));
                                 if (BatchFunctions.this.chStr.charAt(3) != '1') {
                                    --this.importedFileCount;
                                 }
                              }
                           }

                           if (var7) {
                              BatchFunctions.this.status.setString("     " + BatchFunctions.this.fileTableModel.getValueAt(0, 0) + " nyomtatvány ellenőrzése folyamatban...");
                              boolean var101 = false;

                              try {
                                 CalculatorManager var102 = CalculatorManager.getInstance();
                                 var5.clearErrorList();
                                 if (var6 != null) {
                                    var6.clear();
                                 }

                                 ((IEventSupport)var4).addEventListener(var5);
                                 if (BatchFunctions.this.importFunction && ((String)BatchFunctions.this.fileTableModel.getValueAt(0, 0)).toLowerCase().endsWith(".xml")) {
                                    CalculatorManager.xml = true;
                                 }

                                 var102.do_check_all((IResult)null, var5);
                                 CalculatorManager.xml = false;
                                 ((IEventSupport)var4).removeEventListener(var5);
                                 var6 = var5.getErrorList();
                                 if (var6.size() > 0) {
                                    for(var10 = 0; var10 < var6.size() && !var101; ++var10) {
                                       TextWithIcon var111 = (TextWithIcon)var6.get(var10);
                                       var101 = var111.ii != null;
                                    }
                                 }

                                 BatchFunctions.this.tovabb = BatchFunctions.this.alertTovabb || !var101;
                                 BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - A nyomtatvány ellenőrzése az alábbi eredményt adta: ", -1));
                                 if (var101) {
                                    BatchFunctions.this.tovabb = BatchFunctions.this.alertTovabb || !var101;

                                    for(var10 = 0; var10 < var6.size(); ++var10) {
                                       BatchFunctions.this.impHibaLista.add(var6.elementAt(var10));
                                    }

                                    if (BatchFunctions.cmdMode == 2) {
                                       BatchFunctions.this.saveFileList.add(BatchFunctions.this.fileTableModel.getValueAt(0, 0));
                                    }
                                 } else {
                                    BatchFunctions.this.impHibaLista.add(new TextWithIcon("    (Az ellenőrzés sikeres)", -1));
                                 }
                              } catch (Exception var98) {
                                 BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - Hiba történt a nyomtatvány ellenőrzésekor!", 0));
                                 BatchFunctions.this.tovabb = false;
                              }
                           } else {
                              BatchFunctions.this.tovabb = BatchFunctions.this.alertTovabb;
                           }
                        }

                        if (BatchFunctions.this.importFunction && BatchFunctions.this.xkrMode != 1) {
                           System.out.println("frm.enyk fajl mentese folyamatban");
                           BatchFunctions.this.status.setString("     " + BatchFunctions.this.fileTableModel.getValueAt(0, 0) + " fájl mentése folyamatban...");

                           try {
                              if (BatchFunctions.this.fileTableModel.getValueAt(0, 0).toString().toLowerCase().endsWith(".xcz") && var2.isAvdhModel()) {
                                 var8 = BatchFunctions.this.handleXCZImport(BatchFunctions.this.fileTableModel.getValueAt(0, 0).toString());
                              } else {
                                 FileNameResolver var103 = new FileNameResolver(var2, (String)BatchFunctions.this.fileTableModel.getValueAt(0, 0));
                                 var8 = var103.generateFileName();
                              }
                           } catch (Exception var97) {
                              BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - Hiba történt a kicsomagoláskor!", 0));
                              BatchFunctions.this.parseErrorMessage(var97.getMessage());
                              BatchFunctions.this.fileTableModel.removeRow(0);
                              var1x.setOk(false);
                              var1x.errorList.add("Hiba történt a kicsomagoláskor!");
                              continue;
                           }

                           try {
                              EnykInnerSaver var104 = new EnykInnerSaver(var2, true);
                              var1x = var104.save(var8, -1, true);
                              var105 = null;

                              String var113;
                              try {
                                 var113 = var2.cc.getLoadedfile().getAbsolutePath();
                              } catch (Exception var84) {
                                 var113 = "";
                              }

                              var2.cc.setLoadedfile((File)var1x.errorList.elementAt(0));

                              try {
                                 PropertyList.getInstance().set("prop.usr.xcz.batchOne", (Object)null);
                                 PropertyList.getInstance().set("prop.usr.xcz.batchOne.files2show", (Object)null);
                                 BatchFunctions.this.handleZippedData(BatchFunctions.this.fileTableModel.getValueAt(0, 0).toString(), var1x.errorList.elementAt(0).toString(), var8, var2, var113);

                                 try {
                                    Vector var112 = (Vector)PropertyList.getInstance().get("prop.usr.xcz.files");

                                    for(var12 = 0; var12 < var112.size(); ++var12) {
                                       String var116 = (String)var112.get(var12);
                                       File var119 = new File(var116);
                                       var119.delete();
                                    }
                                 } catch (Exception var93) {
                                 }

                                 try {
                                    PropertyList.getInstance().set("prop.usr.xcz.files", (Object)null);
                                    PropertyList.getInstance().set("prop.usr.xcz.dir", (Object)null);
                                 } catch (Exception var83) {
                                 }

                                 if (PropertyList.getInstance().get("prop.usr.xcz.batchOne") != null) {
                                    String var114 = "";
                                    if (BatchFunctions.this._deleteAttachmentFiles(var8)) {
                                       var114 = " - A hiba miatt az összes csatolmányt eltávolítottuk a csatolmányok mappából.";
                                    } else {
                                       var114 = " - A csatolmányok eltávolítása nem sikerült a csatolmányok\\" + var8 + " mappából";
                                    }

                                    if (var1x.isOk()) {
                                       if (BatchFunctions.this.importFunction) {
                                          BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - A mentett fájl neve : " + BatchFunctions.this.beauty(var2.cc.getLoadedfile()) + ".", -1));

                                          try {
                                             Vector var115 = (Vector)PropertyList.getInstance().get("prop.usr.xcz.batchOne.files2show");
                                             if (var115.size() > 0) {
                                                BatchFunctions.this.impHibaLista.add(new TextWithIcon("    Az importált csatolmányok : ", -1));
                                             }

                                             for(var13x = 0; var13x < var115.size(); ++var13x) {
                                                BatchFunctions.this.impHibaLista.add(new TextWithIcon("    - " + BatchFunctions.this.targetDir4Attachments + var8 + File.separator + ((String[])((String[])var115.elementAt(var13x)))[0], -1));
                                             }

                                             if (PropertyList.getInstance().get("prop.usr.xcz.batchOne.3rdPartySign") != null) {
                                                BatchFunctions.this.add3rdPartySignErrors();
                                                PropertyList.getInstance().set("prop.usr.xcz.batchOne.3rdPartySign", (Object)null);
                                             }
                                          } catch (Exception var86) {
                                             Tools.eLog(var86, 0);
                                          }
                                       }

                                       var2.cc.setLoadedfile((File)var1x.errorList.elementAt(0));
                                    }

                                    throw new DataFormatException((String)PropertyList.getInstance().get("prop.usr.xcz.batchOne") + var114);
                                 }
                              } catch (Exception var94) {
                                 if (var1x.isOk()) {
                                    BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - A mentett fájl neve : " + BatchFunctions.this.beauty(var2.cc.getLoadedfile()) + ".", -1));
                                 }

                                 throw new DataFormatException(var94.getMessage());
                              }

                              try {
                                 if (var1x.isOk()) {
                                    var2.cc.setLoadedfile((File)var1x.errorList.elementAt(0));
                                 }
                              } catch (Exception var82) {
                                 try {
                                    System.out.println("Hibas fajl eleresi ut: " + var2.cc.getLoadedfile().getAbsolutePath());
                                 } catch (Exception var81) {
                                    Tools.eLog(var82, 0);
                                 }
                              }
                           } catch (DataFormatException var95) {
                              BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - Hiba történt a leíró kicsomagolásakor! (" + var95.getMessage() + ")", 0));
                              BatchFunctions.this.fileTableModel.removeRow(0);
                              var1x.errorList.add("Hiba történt a leíró kicsomagolásakor!");
                              continue;
                           } catch (Exception var96) {
                              BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - Hiba történt a fájl mentésekor! (" + var8 + ")", 0));
                              BatchFunctions.this.fileTableModel.removeRow(0);
                              var1x.setOk(false);
                              var1x.errorList.add("Hiba történt a fájl mentésekor!");
                              continue;
                           }

                           if (!var1x.isOk()) {
                              BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - Hiba történt a fájl mentésekor! (" + var8 + ")", 0));

                              for(var9 = 0; var9 < var1x.errorList.size(); ++var9) {
                                 BatchFunctions.this.impHibaLista.add(var1x.errorList.get(var9));
                              }

                              BatchFunctions.this.fileTableModel.removeRow(0);
                              continue;
                           }

                           File var107 = var2.cc.getLoadedfile();
                           if (!var107.exists()) {
                              BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - Hiba történt a fájl mentésekor!", 0));
                              BatchFunctions.this.fileTableModel.removeRow(0);
                              continue;
                           }

                           if (BatchFunctions.cmdMode > 0) {
                              var1x.setOk(true);
                              var1x.errorList.clear();
                              var1x.errorList.add(var107);
                           }

                           if (BatchFunctions.this.importFunction) {
                              BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - A mentett fájl neve : " + BatchFunctions.this.beauty(var2.cc.getLoadedfile()) + ".", -1));
                              if (BlacklistStore.getInstance().isBiztipDisabled(var2.getTemplateId(), var2.getOrgId())) {
                                 var105 = BlacklistStore.getInstance().getErrorListMessage(var2.getTemplateId(), var2.getOrgId());
                                 BatchFunctions.this.impHibaLista.add(new TextWithIcon(var105[0], -1));
                                 if (var105.length > 1) {
                                    BatchFunctions.this.impHibaLista.add(new TextWithIcon(var105[1], -1));
                                 }
                              }

                              try {
                                 Vector var117 = (Vector)PropertyList.getInstance().get("prop.usr.xcz.batchOne.files2show");
                                 if (var117.size() > 0) {
                                    BatchFunctions.this.impHibaLista.add(new TextWithIcon("    Az importált csatolmányok : ", -1));
                                 }

                                 for(var11x = 0; var11x < var117.size(); ++var11x) {
                                    BatchFunctions.this.impHibaLista.add(new TextWithIcon("    - " + BatchFunctions.this.targetDir4Attachments + var8 + File.separator + ((String[])((String[])var117.elementAt(var11x)))[0], -1));
                                 }

                                 if (PropertyList.getInstance().get("prop.usr.xcz.batchOne.3rdPartySign") != null) {
                                    BatchFunctions.this.add3rdPartySignErrors();
                                    PropertyList.getInstance().set("prop.usr.xcz.batchOne.3rdPartySign", (Object)null);
                                 }
                              } catch (Exception var92) {
                                 Tools.eLog(var92, 0);
                              }
                           }

                           if (BatchFunctions.this.fileListaMenteseCB.isSelected()) {
                              BatchFunctions.this.saveFileList.add(var2.cc.getLoadedfile().getAbsolutePath());
                           }
                        }

                        if (BatchFunctions.this.tovabb) {
                           if (BatchFunctions.this.chStr.charAt(3) == '1') {
                              BatchFunctions.this.status.setString("     nyomtatvány nyomtatása folyamatban...");

                              try {
                                 PropertyList.getInstance().set("brCountError", (Object)null);
                                 var2.cc.setActiveObject(var2.cc.get(0));
                                 BatchFunctions.Hibaszam var106 = BatchFunctions.this.getErrorCounts(var6);
                                 String var108 = var2.epost;
                                 if (!var7) {
                                    try {
                                       if (!var2.cc.getLoadedfile().getName().toLowerCase().endsWith(".frm.enyk")) {
                                          var2.epost = "onlyinternet";
                                       }
                                    } catch (Exception var80) {
                                       var2.epost = "onlyinternet";
                                    }
                                 }

                                 MainPrinter var118 = new MainPrinter(var2, var106 != null ? var106.hszv : null, BatchFunctions.this.ps, true);
                                 var118.batchPrint2Jpg = BatchFunctions.this.jpgButton.isSelected();
                                 var118.batchPrint2Pdf = BatchFunctions.this.pdfButton.isSelected() || BatchFunctions.this.onePdfButton.isSelected();
                                 var118.batchPrint2OnePdf = BatchFunctions.this.onePdfButton.isSelected();
                                 if (BatchFunctions.this.simplePrButton.isSelected()) {
                                    var118.batchPrintSimpleMode = BatchFunctions.this.prButton.isSelected() ? 0 : 1;
                                 }

                                 MainPrinter.voltEEllenorzesNyomtatasElott = var7;

                                 try {
                                    var118.init(false, var106 == null ? false : var106.vhsz);
                                 } finally {
                                    try {
                                       if (var118.document.isOpen()) {
                                          var118.document.close();
                                       }
                                    } catch (Exception var77) {
                                       Tools.eLog(var77, 0);
                                    }

                                    var118.batchPrint2Pdf = false;
                                    var118.batchPrint2Jpg = false;
                                    var118.batchPrintSimpleMode = -1;
                                 }

                                 MainPrinter.voltEEllenorzesNyomtatasElott = true;
                                 if (!var7) {
                                    var2.epost = var108;
                                 }

                                 if (!MainPrinter.message4TheMasses.equals("")) {
                                    BatchFunctions.this.impHibaLista.add(new TextWithIcon(" ", -1));
                                    BatchFunctions.this.impHibaLista.add(new TextWithIcon(MainPrinter.message4TheMasses, 4));
                                    MainPrinter.message4TheMasses = "";
                                 }

                                 try {
                                    BatchFunctions.this.impHibaLista.addAll(var118.batchJpgPrint.errorList);
                                 } catch (Exception var78) {
                                    Tools.eLog(var78, 0);
                                 }

                                 try {
                                    if (PropertyList.getInstance().get("brCountError") != null) {
                                       BatchFunctions.this.impHibaLista.add(new TextWithIcon((String)PropertyList.getInstance().get("brCountError"), 1));
                                       if (BatchFunctions.this.chStr.charAt(4) != '1') {
                                          BatchFunctions.this.fileTableModel.removeRow(0);
                                          continue;
                                       }
                                    }
                                 } catch (Exception var89) {
                                    Tools.eLog(var89, 0);
                                 }

                                 PropertyList.getInstance().set("brCountError", (Object)null);
                              } catch (PrinterException var90) {
                                 BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - " + var90.getMessage(), 0));
                                 if (BatchFunctions.this.chStr.charAt(4) != '1') {
                                    BatchFunctions.this.fileTableModel.removeRow(0);
                                    continue;
                                 }
                              } catch (Exception var91) {
                                 var91.printStackTrace();
                                 BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - Hiba történt a nyomtatáskor.", 0));
                                 if (BatchFunctions.this.chStr.charAt(4) != '1') {
                                    BatchFunctions.this.fileTableModel.removeRow(0);
                                    continue;
                                 }
                              }
                           }

                           if (BatchFunctions.this.chStr.charAt(4) == '1') {
                              if (!BatchFunctions.this.hasFatalError(var6)) {
                                 try {
                                    PropertyList.getInstance().set("prop.dynamic.ebev_call_from_menu", Boolean.TRUE);
                                    var8 = BatchFunctions.this.doFeladas(var2);
                                    if (var8.length() <= 2) {
                                       if (var8.length() == 2) {
                                          BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - Hiba történt a feladáskor - az állomány nem \"Módosítható\" állapotú.", 0));
                                       } else if (var8.equals("5")) {
                                          BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - Hiba történt a feladáskor. (Csatolmányok)", 0));
                                       } else if (var8.equals("6")) {
                                          BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - Felhasználói megszakítás.", 0));
                                       } else {
                                          BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - Hiba történt a feladáskor.", 0));
                                       }

                                       throw new Exception();
                                    }

                                    BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - A feladásra megjelölt fájl: " + BatchFunctions.this.fileTableModel.getValueAt(0, 0), -1));
                                    BatchFunctions.this.handleNewLineInMessage(var8);
                                 } catch (Exception var87) {
                                    BatchFunctions.this.fileTableModel.removeRow(0);

                                    try {
                                       BatchFunctions.ihl.release();
                                    } catch (Exception var76) {
                                       Tools.eLog(var87, 0);
                                    }
                                    continue;
                                 } finally {
                                    PropertyList.getInstance().set("prop.dynamic.ebev_call_from_menu", (Object)null);
                                    if (BatchFunctions.this.fileTableModel.getRowCount() == 0) {
                                       KauAuthHelper.getInstance().setSaveAuthData(false);
                                       KauSessionTimeoutHandler.getInstance().reset();
                                       DapSessionHandler.getInstance().reset();
                                    }

                                 }
                              } else {
                                 BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - A nyomtatvány súlyos hibát is tartalmaz, a megjelölés nem folytatható.", 0));
                                 --this.importedFileCount;
                              }
                           }
                        } else if (BatchFunctions.this.chStr.charAt(3) == '1' || BatchFunctions.this.chStr.charAt(4) == '1') {
                           BatchFunctions.this.impHibaLista.add(new TextWithIcon("  - A nyomtatvány hibás, további műveleteket nem végzünk.", -1));
                        }

                        ++this.importedFileCount;
                        BatchFunctions.this.fileTableModel.removeRow(0);
                        BatchFunctions.this.status.setString(" ");
                     }

                     if (!BatchFunctions.this.importFunction && var2 != null && var2.isDisabledTemplate()) {
                        String[] var109 = BlacklistStore.getInstance().getErrorListMessage(var2.getTemplateId(), var2.getOrgId());
                        BatchFunctions.this.impHibaLista.add(new TextWithIcon(var109[0], -1));
                        if (!"".equals(var109[1])) {
                           BatchFunctions.this.impHibaLista.add(new TextWithIcon(var109[1], -1));
                        }
                     }
                  }
               } catch (Exception var100) {
                  BatchFunctions.this.closeDialog(var11);
                  var100.printStackTrace();
               }

               try {
                  if (BatchFunctions.cmdMode != 1) {
                     var2.destroy();
                  } else {
                     BatchFunctions.this.bm2Show = var2;
                  }
               } catch (Exception var75) {
                  Tools.eLog(var75, 0);
               }

               try {
                  if (BatchFunctions.cmdMode != 1) {
                     var3.destroy();
                  }
               } catch (Exception var74) {
                  Tools.eLog(var74, 0);
               }

               BatchFunctions.this.dl = null;
               BatchFunctions.this.xl = null;
               BatchFunctions.this.il = null;
               BatchFunctions.this.xkl = null;
               BatchFunctions.this.status.setIndeterminate(false);
               BatchFunctions.this.filters.setEnabled(true);
               if (BatchFunctions.cmdMode == 2) {
                  var1x.errorList = BatchFunctions.this.impHibaLista;
               }

               return var1x;
            }

            public void done() {
               TemplateChecker.getInstance().setNeedDialog4Download(true);
               var13[0] = true;

               try {
                  BatchFunctions.this.cmdObject = this.get();
               } catch (Exception var3) {
                  var3.printStackTrace();
                  BatchFunctions.this.cmdObject = null;
               }

               try {
                  BatchFunctions.this.closeDialog(var11);
               } catch (Exception var2) {
                  Tools.eLog(var2, 0);
               }

               BatchFunctions.folyamatban = false;
               BatchFunctions.elindult = false;
               BatchFunctions.this.status.setString(" ");
               BatchFunctions.this.impHibaLista.add(new TextWithIcon(" ", -1));
               BatchFunctions.this.impHibaLista.add(new TextWithIcon(" ", -1));
               BatchFunctions.this.impHibaLista.add(new TextWithIcon("A műveletekre kijelölt fájlok száma: " + this.fileCount, -1));
               if (this.importedFileCount != 0) {
                  BatchFunctions.this.impHibaLista.add(new TextWithIcon(this.importedFileCount + " fájlon sikeresen elvégeztük a kért műveletet", -1));
               }

               if (this.fileCount - this.importedFileCount != 0) {
                  BatchFunctions.this.impHibaLista.add(new TextWithIcon(this.fileCount - this.importedFileCount + " fájlon nem sikerült elvégezni az összes kijelölt műveletet", -1));
               }

               if (BatchFunctions.outOfMemory) {
                  BatchFunctions.this.impHibaLista.add(new TextWithIcon("A művelet elvégzéséhez kevés a memória! Kérjük indítsa újra az alkalmazást!", 0));
               }

               BatchFunctions.this.enableAll(true);
               if (BatchFunctions.this.fileListaMenteseCB.isEnabled() && BatchFunctions.this.fileListaMenteseCB.isSelected()) {
                  BatchFunctions.this.doSaveFileList(BatchFunctions.this.getTimeString("yyyyMMdd_HH_mm_ss"));
               }

               PropertyList.getInstance().set("prop.dynamic.importOne", (Object)null);
               MainFrame.thisinstance.glasslock = false;
               MainFrame.thisinstance.setGlassLabel((String)null);
               Tools.resetLabels();
               if (BatchFunctions.cmdMode == 0) {
                  BatchFunctions.this.fillDialog((BatchFunctions.this.importFunction ? "Az importálás" : "A csoportos művelet") + " eredménye:", BatchFunctions.this.impHibaLista);
               } else {
                  Result var1x = (Result)BatchFunctions.this.cmdObject;
                  if (BatchFunctions.cmdMode == 1) {
                     if (var1x.isOk()) {
                        Menubar.thisinstance.open_bookModel(BatchFunctions.this.bm2Show, BatchFunctions.this.xkrMode == 1);
                     } else {
                        GuiUtil.setcurrentpagename("");
                     }
                  } else {
                     BatchFunctions.this.batchCheckFinished(var1x);
                  }
               }

               PropertyList.getInstance().set("prop.dynamic.ilyenkor", (Object)null);
               if (BatchFunctions.cmdMode == 1) {
                  new ErrorDialog(MainFrame.thisinstance, "Az importálás eredménye", true, false, BatchFunctions.this.impHibaLista);
               }

            }
         };
         elindult = true;
         var13[0] = false;
         var5.execute();
         if (cmdMode == 3) {
            var11.pack();
            var11.setVisible(true);
         }

         try {
            if (var13[0]) {
               this.closeDialog(var11);
            }
         } catch (Exception var8) {
            Tools.eLog(var8, 0);
         }
      } catch (Exception var9) {
         var9.printStackTrace();
         this.impHibaLista.add(new TextWithIcon("Programhiba!", 1));
         new ErrorDialog(MainFrame.thisinstance, "Az importálás eredménye", true, false, this.impHibaLista);
         this.status.setString(" ");
         this.enableAll(true);
      }

      try {
         return ((File)((Result)this.cmdObject).errorList.elementAt(0)).getAbsolutePath();
      } catch (Exception var7) {
         return null;
      }
   }

   private void setButtonState(boolean var1) {
      if (!folyamatban) {
         this.delButton.setEnabled(var1);
         this.saveButton.setEnabled(var1);
         this.okButton.setEnabled(var1);
      }
   }

   private String getTimeString(String var1) {
      SimpleDateFormat var2 = new SimpleDateFormat(var1);
      return var2.format(Calendar.getInstance().getTime());
   }

   private void doSaveFileList(String var1) {
      if (this.saveFileList.size() != 0) {
         FileOutputStream var2 = null;

         try {
            String var3 = dataPath + (cmdMode == 2 ? "bc_" : "") + var1 + (cmdMode == 2 ? "_list" : "") + ".txt";
            var2 = new FileOutputStream(var3);

            for(int var4 = 0; var4 < this.saveFileList.size(); ++var4) {
               var2.write((this.saveFileList.get(var4) + "\r\n").getBytes());
            }

            var2.close();
            this.impHibaLista.add(new TextWithIcon("Az elkészült fájlok listáját " + this.beauty(var3) + " néven mentettük", -1));
         } catch (Exception var6) {
            try {
               var2.close();
            } catch (Exception var5) {
               Tools.eLog(var6, 0);
            }
         }

      }
   }

   private void fillDialog(String var1, Vector var2) {
      this.summaDialog.setTitle(var1);
      this.logLista.removeAll();
      this.logLista.setListData(var2);
      this.logSP.setViewportView(this.logLista);
      Dimension var3 = new Dimension((int)(0.8D * (double)GuiUtil.getScreenW()), (int)(0.6D * (double)GuiUtil.getScreenH()));
      this.logSP.setPreferredSize(var3);
      this.logSP.setMinimumSize(var3);
      this.summaDialog.getContentPane().add(this.logSP, "Center");
      this.summaDialog.pack();
      this.summaDialog.setVisible(true);
   }

   private void checkBoxStateChange() {
      this.addRemoveListeners(false);
      if (this.jcb4.isSelected() || this.jcb5.isSelected()) {
         this.jcb3.setSelected(true);
      }

      if (this.jcb5.isSelected()) {
         this.jcb6.setSelected(false);
      }

      if (this.jcb6.isSelected()) {
         this.jcb2.setSelected(false);
         this.jcb3.setSelected(false);
         this.jcb4.setSelected(false);
         this.jcb5.setSelected(false);
      }

      this.chStr = (this.jcb1.isSelected() ? "1" : "0") + (this.jcb2.isSelected() ? "1" : "0") + (this.jcb3.isSelected() ? "1" : "0") + (this.jcb4.isSelected() ? "1" : "0") + (this.jcb5.isSelected() ? "1" : "0") + (this.jcb6.isSelected() ? "1" : "0");
      if (!this.chStr.startsWith("00000")) {
         this.jcb1.setSelected(true);
      }

      this.addRemoveListeners(true);
      this.prButton.setVisible(this.jcb4.isSelected());
      this.jpgButton.setVisible(this.jcb4.isSelected() && !this.simplePrButton.isSelected());
      this.onePdfButton.setVisible(this.jcb4.isSelected());
      this.pdfButton.setVisible(this.jcb4.isSelected());
      this.simplePrButton.setVisible(this.jcb4.isSelected());
   }

   private int getStatus(String var1, String var2) {
      FileStatusChecker var3 = FileStatusChecker.getInstance();

      try {
         var3.set((Object)null, MainFrame.thisinstance.mp.getDMFV().bm.splitesaver.equalsIgnoreCase("true"));
      } catch (Exception var5) {
         var3.set((Object)null, false);
      }

      return var3.getStatus(var1, var2);
   }

   private String doFeladas(BookModel var1) throws Exception {
      int var2 = this.getStatus(var1.cc.getLoadedfile().getAbsolutePath(), (String)((String)(var1.cc.docinfo.containsKey("krfilename") ? var1.cc.docinfo.get("krfilename") : "")));
      return this.doFeladas(var2, var1);
   }

   private String doFeladas(int var1, BookModel var2) throws Exception {
      if (var1 == 0) {
         Ebev var3 = new Ebev(var2);
         Result var4 = var3.mark(true, false);
         if (var4.isOk()) {
            return (String)var4.errorList.get(0);
         } else {
            for(int var5 = 0; var5 < var4.errorList.size(); ++var5) {
               if (var4.errorList.get(var5) instanceof TextWithIcon) {
                  this.impHibaLista.add(var4.errorList.get(var5));
               } else {
                  this.impHibaLista.add(new TextWithIcon((String)var4.errorList.get(var5), 0));
               }
            }

            return "";
         }
      } else {
         return "0" + var1;
      }
   }

   private boolean handleAVDH(ReadOnlyTableModel var1) {
      for(int var2 = 0; var2 < var1.getRowCount(); ++var2) {
         if (!"".equals(var1.getValueAt(var2, 16).toString())) {
            return true;
         }
      }

      return false;
   }

   private boolean doVisszavonas(String var1, String var2, String var3) throws Exception {
      try {
         File var4 = new File(var1);

         try {
            var1 = var1.substring(var1.lastIndexOf(File.separator) + 1);
         } catch (Exception var8) {
            Tools.eLog(var8, 0);
         }

         File var5;
         if (var2.equals("")) {
            var5 = new File(destPath + PropertyList.USER_IN_FILENAME + (var1.endsWith(".frm.enyk") ? var1.substring(0, var1.toLowerCase().indexOf(".frm.enyk")) + ".kr" : var1 + ".kr"));
         } else {
            var5 = new File(destPath + PropertyList.USER_IN_FILENAME + var2);
         }

         if (var5.exists()) {
            if (var5.delete()) {
               this.impHibaLista.add(new TextWithIcon("  - Az elektronikus feladásra történt kijelölés megszűnt.", -1));
               this.deleteCstFile(var4.getAbsolutePath(), var3);
               this.fileTableModel.removeRow(0);

               try {
                  Ebev.log(1, var4);
               } catch (Exception var7) {
                  Tools.eLog(var7, 0);
               }

               return true;
            }

            this.impHibaLista.add(new TextWithIcon("  - Hiba történt a visszavonáskor - a megjelölt állomány nem törölhető.", 0));
         } else {
            this.impHibaLista.add(new TextWithIcon("  - Hiba történt a visszavonáskor - az állomány nem megfelelő állapotú.", 0));
         }
      } catch (Exception var9) {
         this.impHibaLista.add(new TextWithIcon("  - Hiba történt a visszavonáskor.", 0));
      }

      this.fileTableModel.removeRow(0);
      return false;
   }

   private void enableAll(boolean var1) {
      this.jcb1.setEnabled(var1);
      this.jcb2.setEnabled(var1);
      this.jcb3.setEnabled(var1);
      this.jcb4.setEnabled(var1);
      this.jcb5.setEnabled(var1);
      this.jcb6.setEnabled(var1);
      this.addButton.setEnabled(var1);
      this.add2Button.setEnabled(var1);
      this.listButton.setEnabled(var1);
      this.okButton.setEnabled(var1 & this.fileTableModel.getRowCount() > 0);
      this.saveButton.setEnabled(this.okButton.isEnabled());
      this.delButton.setEnabled(this.okButton.isEnabled());
      this.megsemButton.setEnabled(true);
      this.filters.setEnabled(true);
      this.fileListaMenteseCB.setEnabled(var1);
      this.prButton.setEnabled(var1);
      this.jpgButton.setEnabled(var1);
      this.pdfButton.setEnabled(var1);
      this.onePdfButton.setEnabled(var1);
      this.simplePrButton.setEnabled(var1);
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
         var2 = var3.startsWith(((Vector)var5.get(var4)).get(0).toString());
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
      this.fileTable.setSelectionMode(2);
      this.fileTable.setRowSelectionAllowed(true);
      this.fileTableModel.addTableModelListener(this);
      this.fileTable.addKeyListener(this);
      this.fileTable.setAutoResizeMode(0);

      for(int var2 = 0; var2 < 5; ++var2) {
         TableColumn var1 = this.fileTable.getColumnModel().getColumn(var2);
         if (var2 == 0) {
            var1.setPreferredWidth(300);
         } else {
            var1.setPreferredWidth(100);
         }
      }

      this.sorter.setTableHeader(this.fileTable.getTableHeader());
   }

   private void addFilesToList(Object[] var1) {
      Vector var2 = new Vector();
      Vector var3 = new Vector();

      for(int var4 = 0; var4 < var1.length; ++var4) {
         Object[] var5 = (Object[])((Object[])var1[var4]);
         Vector var6 = new Vector();
         var6.add(((File)var5[0]).getAbsolutePath());
         Hashtable var7 = (Hashtable)((Hashtable)var5[3]).get("docinfo");
         if (var7.size() == 0) {
            var3.add(((File)var5[0]).getAbsolutePath() + " - Nem olvashatóak a nyomtatványadatok.");
            var6 = null;
         } else if (var7.get("name").equals("HIBÁS ÁLLOMÁNY !")) {
            var3.add(((File)var5[0]).getAbsolutePath() + " - " + "HIBÁS ÁLLOMÁNY !" + (var7.containsKey("info") ? " - " + var7.get("info") : ""));
            var6 = null;
         } else {
            var7.put("state", ((Hashtable)var5[2]).get("state"));
            var7.put("save_date", this.formatDate(((Hashtable)var5[3]).get("saved")));

            for(int var8 = 0; var8 < DI_KEYS.length; ++var8) {
               if (!var7.containsKey(DI_KEYS[var8])) {
                  var6.add("");
               } else {
                  var6.add(var7.get(DI_KEYS[var8]));
               }
            }

            if (!this.alreadyInList(var6)) {
               this.fileTableModel.addRow(var6);
            } else {
               var2.add(var6.get(0));
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
         this.doLoad();
         break;
      case 3:
         this.doDel();
         break;
      case 4:
         this.doSaveList();
         break;
      case 5:
         this.xkrMode = 0;
         this.releasePropertyListParams();
         this.doImp();
         break;
      case 6:
         if (!elindult) {
            Tools.resetLabels();
            this.loaders.clear();

            try {
               this.release();
            } catch (Exception var5) {
               var5.printStackTrace();
            }

            this.setVisible(false);
            this.dispose();
            this.releasePropertyListParams();
            this.mainFrame = null;
         } else if (folyamatban) {
            folyamatban = false;
            this.impHibaLista.add(new TextWithIcon("Felhasználói megszakítás", -1));
            this.megsemButton.setEnabled(false);
         }
         break;
      case 7:
         this.logOkAction();
         break;
      case 8:
         this.logSaveAction();
      case 9:
      default:
         break;
      case 10:
         this.doAdd2();
      }

   }

   public void itemStateChanged(ItemEvent var1) {
      if (var1.getSource() instanceof JCheckBox) {
         this.checkBoxStateChange();
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
      TemplateChecker.getInstance().setNeedDialog4Download(true);
   }

   public void windowDeactivated(WindowEvent var1) {
   }

   public void windowDeiconified(WindowEvent var1) {
   }

   public void windowIconified(WindowEvent var1) {
   }

   public void windowOpened(WindowEvent var1) {
   }

   private void addRemoveListeners(boolean var1) {
      if (var1) {
         this.jcb1.addItemListener(this);
         this.jcb2.addItemListener(this);
         this.jcb3.addItemListener(this);
         this.jcb4.addItemListener(this);
         this.jcb5.addItemListener(this);
         this.jcb6.addItemListener(this);
      } else {
         this.jcb1.removeItemListener(this);
         this.jcb2.removeItemListener(this);
         this.jcb3.removeItemListener(this);
         this.jcb4.removeItemListener(this);
         this.jcb5.removeItemListener(this);
         this.jcb6.removeItemListener(this);
      }

   }

   private Vector checkStatuses(int var1) {
      Vector var2 = new Vector();

      for(int var3 = 0; var3 < this.fileTableModel.getRowCount(); ++var3) {
         if (this.getStatus((String)this.fileTableModel.getValueAt(var3, 0), (String)this.fileTableModel.getValueAt(var3, 15)) != var1) {
            var2.add(this.fileTableModel.getValueAt(var3, 0));
         }
      }

      return var2;
   }

   private BatchFunctions.Hibaszam getErrorCounts(Vector var1) {
      try {
         int var2 = 0;
         BatchFunctions.Hibaszam var3 = new BatchFunctions.Hibaszam();

         for(int var4 = 1; var4 < var1.size(); ++var4) {
            if (!(var1.elementAt(var4) instanceof String)) {
               TextWithIcon var5 = (TextWithIcon)var1.elementAt(var4);
               if (!var3.vhsz && ((TextWithIcon)var1.get(var4)).imageType == 1) {
                  var3.vhsz = true;
               }

               if (var5.ii == null) {
                  var3.hszv.add(var4 - var2 > 1);
                  var2 = var4;
               } else {
                  ++var3.ihsz;
               }
            }
         }

         var3.hszv.add(var2 != var1.size() - 1);
         return var3;
      } catch (Exception var6) {
         var6.printStackTrace();
         return null;
      }
   }

   private String doLoadHeadData(File var1, String var2) {
      if (var2 == null) {
         var2 = var1.getName().toLowerCase().substring(var1.getName().lastIndexOf("."));
      }

      if (!this.loaders.containsKey(var2)) {
         this.impHibaLista.add(new TextWithIcon("  - Hibás a fájl neve! (" + var2 + ")", 0));
      }

      Hashtable var3 = new Hashtable();
      var3.put("state", "Módosítható");
      Object[] var4 = new Object[]{var1, null, var3, ((ILoadManager)this.loaders.get(var2)).getHeadData(var1)};
      this.addFilesToList(new Object[]{var4});
      return this.getXkrNewData((Hashtable)var4[3]);
   }

   private String getXkrNewData(Hashtable var1) {
      try {
         return !var1.containsKey("muvelet") ? "default" : (String)var1.get("muvelet");
      } catch (Exception var3) {
         return "default";
      }
   }

   private BookModel loadInnerData(BookModel var1) {
      BookModel var2;
      try {
         ExtendedTemplateData var8 = TemplateChecker.getInstance().getTemplateFileNames((String)this.fileTableModel.getValueAt(0, 17), (String)this.fileTableModel.getValueAt(0, 12), (String)this.fileTableModel.getValueAt(0, 14), (UpgradeFunction)null);
         if (var8.isTemplateDisaled()) {
            var2 = new BookModel();
            var2.setDisabledTemplate(true);
            var2.hasError = true;
            String[] var9 = BlacklistStore.getInstance().getErrorListMessage((String)this.fileTableModel.getValueAt(0, 17), (String)this.fileTableModel.getValueAt(0, 14));
            var2.errormsg = var9[0] + " bl_url " + var9[1];
            var2.loadedfile = new File("disabled_template");
            return var2;
         }

         String var4 = var8.getTemplateFileNames()[0];
         if (var4.equals("")) {
            throw new Exception("*");
         }

         File var5 = new File(var4);
         if (!var5.exists()) {
            throw new Exception();
         }

         if (var1 != null && var1.loadedfile.getAbsolutePath().equals(var4)) {
            var1.reempty();
            var1.reload(new File((String)this.fileTableModel.getValueAt(0, 0)), true);
            var2 = var1;
         } else {
            var2 = new BookModel(var5, new File((String)this.fileTableModel.getValueAt(0, 0)), true);
         }
      } catch (Exception var7) {
         Exception var3 = var7;
         var2 = new BookModel();
         var2.hasError = true;

         try {
            if (var3.getMessage().startsWith("*")) {
               var2.errormsg = "A megadott nyomtatványhoz nem található sablon";
            } else {
               var2.errormsg = "Hiba a sablon betöltésekor";
            }
         } catch (Exception var6) {
            var2.errormsg = "Hiba a sablon betöltésekor";
         }

         var2.loadedfile = new File("hkszcspszv");
      }

      return var2;
   }

   private boolean hasFatalError(Vector var1) {
      int var2 = 0;

      boolean var3;
      for(var3 = false; var2 < var1.size() && !var3; ++var2) {
         if (((TextWithIcon)var1.get(var2)).imageType == 1) {
            var3 = true;
         }
      }

      return var3;
   }

   private void sortFileList() {
      this.sorter.setSortingStatus(1, 1);
      this.sorter.fireTableDataChanged();
      this.sortFTM();
   }

   public String cmdOne(String var1, String var2, boolean var3) {
      return this.cmdOne(var1, var2, var3, 0);
   }

   public String cmdOne(String var1, String var2, boolean var3, int var4) {
      if (var1.toLowerCase().endsWith(".xkr")) {
         PropertyList.getInstance().set("prop.dynamic.importOne", true);
      }

      if (var3) {
         PropertyList.getInstance().set("prop.dynamic.ilyenkor", (Object)null);
      }

      String var5;
      try {
         var5 = this.doLoadHeadData(new File(var1), var2);
      } catch (Exception var9) {
         String var7 = var1.toLowerCase().endsWith(".xcz") ? "A csomag nem tartalmaz megfelelő adatállományt" : "Hiba a fájl betöltésekor";
         GuiUtil.showMessageDialog(MainFrame.thisinstance, var7, "Hiba", 0);
         MainFrame.thisinstance.glasslock = false;
         MainFrame.thisinstance.setGlassLabel((String)null);
         return null;
      }

      try {
         if (this.fileTableModel.getRowCount() == 0) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiba a fájl betöltésekor (1)", "Hiba", 0);
            MainFrame.thisinstance.glasslock = false;
            MainFrame.thisinstance.setGlassLabel((String)null);
            return null;
         } else {
            this.chStr = "100000";
            if (var4 != 0) {
               this.xkrMode = 0;
            } else {
               this.xkrMode = this.xkr(var1, var5);
            }

            cmdMode = var3 ? 1 : 3;
            return this.doImp();
         }
      } catch (Exception var8) {
         MainFrame.thisinstance.glasslock = false;
         MainFrame.thisinstance.setGlassLabel((String)null);
         return null;
      }
   }

   private int xkr(String var1, String var2) {
      if (var1.toLowerCase().endsWith("xkr")) {
         return var2.equals("default") ? 1 : 0;
      } else {
         return var2.equals("default") ? 3 : 2;
      }
   }

   public void cmd(String var1) {
      cmdMode = 0;
      this.fileListaMenteseCB.setEnabled(true);
      this.fileListaMenteseCB.setSelected(true);
      defaultDirectory = new File((new File(var1)).getParent());
      Result var2 = this.startFromCommandLine(var1);
      if (!var2.isOk()) {
         new ErrorDialog(MainFrame.thisinstance, "Csoportos import", true, true, var2.errorList);
         MainFrame.thisinstance.glasslock = false;
         MainFrame.thisinstance.setGlassLabel((String)null);
      }

      this.setVisible(false);
      this.dispose();
   }

   private Result startFromCommandLine(String var1) {
      Result var2 = new Result();
      File var3 = new File(var1);
      if (!var3.exists()) {
         var2.setOk(false);
         var2.errorList.add(new TextWithIcon("Nem található a fájl", 0));
         return var2;
      } else {
         Vector var4 = new Vector();

         try {
            this.parseFile(var1, var4, true);
         } catch (Exception var6) {
            var2.setOk(false);
            var2.errorList.add(new TextWithIcon("Hiba a fájl értelmezésekor", 0));
            return var2;
         }

         if (var4.size() > 0) {
            var2.setOk(false);
            var2.errorList.add(new TextWithIcon("A fájl feldolgozása az alábbi hibákat eredményezte:", 1));
            var2.errorList.addAll(var4);
            var2.setOk(false);
            return var2;
         } else {
            this.xkrMode = 0;
            this.doImp();
            return var2;
         }
      }
   }

   public void cmdCheck(String var1) {
      cmdMode = 2;
      this.fileListaMenteseCB.setEnabled(true);
      this.fileListaMenteseCB.setSelected(true);
      defaultDirectory = this.fc.getCurrentDirectory();
      this.checkFromCommandLine(var1);
   }

   private void checkFromCommandLine(String var1) {
      Result var2 = new Result();
      this.chStr = "101000";
      this.loaders.put(".enyk", new CachedCollection());
      this.filters.addItem("Nyomtatványok");
      this.resFilter = new String[]{"inner_data_loader_v1"};
      this.importFunction = false;
      File var3 = new File(var1);
      if (!var3.isDirectory()) {
         var2.setOk(false);
         var2.errorList.add(new TextWithIcon("A megadott path nem könyvtár", 0));
         this.batchCheckFinished(var2);
      } else if (!var3.exists()) {
         var2.setOk(false);
         var2.errorList.add(new TextWithIcon("Nem található a fájl", 0));
         this.batchCheckFinished(var2);
      } else {
         Vector var4 = new Vector();

         try {
            this.parseFile2(var1, var4);
         } catch (Exception var6) {
            var6.printStackTrace();
            var2.setOk(false);
            var2.errorList.add(new TextWithIcon("Hiba a fájl értelmezésekor", 0));
            this.batchCheckFinished(var2);
            return;
         }

         if (var4.size() > 0) {
            var2.setOk(false);
            var2.errorList.add(new TextWithIcon("A fájl feldolgozása az alábbi hibákat eredményezte:", 1));
            var2.errorList.addAll(var4);
            var2.setOk(false);
            this.batchCheckFinished(var2);
         } else {
            this.xkrMode = 0;
            this.doImp();
         }
      }
   }

   private void batchCheckFinished(Result var1) {
      String var2 = dataPath + "bc_" + this.getTimeString("yyyy.MM.dd");
      if (var1.errorList.size() > 0) {
         FileOutputStream var3 = null;

         try {
            var3 = new FileOutputStream(var2 + "_errors.txt");

            for(int var4 = 0; var4 < var1.errorList.size(); ++var4) {
               if (var1.errorList.elementAt(var4) instanceof TextWithIcon) {
                  var3.write((((TextWithIcon)((TextWithIcon)var1.errorList.elementAt(var4))).text + "\r\n").getBytes());
               } else {
                  var3.write((var1.errorList.elementAt(var4).toString() + "\r\n").getBytes());
               }
            }

            var3.close();
         } catch (Exception var7) {
            var7.printStackTrace();

            try {
               var3.close();
            } catch (Exception var6) {
               Tools.eLog(var6, 0);
            }
         }
      }

      Tools.resetLabels();
      System.out.println("Batch Check END");
   }

   private void logOkAction() {
      this.summaDialog.setVisible(false);
      this.summaDialog.dispose();
   }

   private void logSaveAction() {
      this.initDialog("Lista mentése");

      try {
         this.fc.setCurrentDirectory(new File((String)PropertyList.getInstance().get("prop.usr.naplo")));
      } catch (Exception var12) {
         Tools.eLog(var12, 0);
      }

      if (this.importFunction) {
         try {
            ((BasicFileChooserUI)this.fc.getUI()).setFileName("import_uzenetek.txt");
         } catch (ClassCastException var11) {
            try {
               this.fc.setSelectedFile(new File("import_uzenetek.txt"));
            } catch (Exception var10) {
               Tools.eLog(var10, 0);
            }
         }
      } else {
         try {
            ((BasicFileChooserUI)this.fc.getUI()).setFileName("csoportos_muveletek_uzenetek.txt");
         } catch (ClassCastException var9) {
            try {
               this.fc.setSelectedFile(new File("csoportos_muveletek_uzenetek.txt"));
            } catch (Exception var8) {
               Tools.eLog(var8, 0);
            }
         }
      }

      this.fc.addChoosableFileFilter(this.tff);

      boolean var1;
      do {
         var1 = true;
         int var2 = this.fc.showSaveDialog(this.summaDialog);
         if (var2 == 0) {
            File var3 = this.fc.getSelectedFile();
            if (var3.exists()) {
               var1 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Ilyen nevű fájl már létezik. Felülírjuk?", "Csoportos műveletek", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0;
            }

            if (var1) {
               FileOutputStream var4 = null;

               try {
                  var4 = new FileOutputStream(var3);

                  for(int var5 = 0; var5 < this.logLista.getModel().getSize(); ++var5) {
                     if (this.logLista.getModel().getElementAt(var5) instanceof TextWithIcon) {
                        var4.write((((TextWithIcon)((TextWithIcon)this.logLista.getModel().getElementAt(var5))).text + "\r\n").getBytes());
                     } else {
                        var4.write((this.logLista.getModel().getElementAt(var5).toString() + "\r\n").getBytes());
                     }
                  }

                  var4.close();
               } catch (Exception var13) {
                  try {
                     var4.close();
                  } catch (Exception var7) {
                     Tools.eLog(var7, 0);
                  }

                  GuiUtil.showMessageDialog(this.mainFrame, "A lista mentése nem sikerült!", "Hiba", 0);
               }

               defaultDirectory = this.fc.getCurrentDirectory();
            }
         }
      } while(!var1);

      this.fc.removeChoosableFileFilter(this.tff);
   }

   private void news() {
      this.delButton = new JButton("Töröl");
      this.okButton = new JButton("Indítás");
      this.saveButton = new JButton("Lista mentése");
      this.addButton = new JButton();
      if (this.importFunction) {
         this.addButton.setText("Hozzáad (import mappából)");
      } else {
         this.addButton.setText("Hozzáad");
      }

      this.add2Button = new JButton("Hozzáad");
      this.listButton = new JButton("Lista betöltése");
      this.megsemButton = new JButton("Bezár");
      this.jcb1 = GuiUtil.getANYKCheckBox("Importálás/betöltés");
      this.jcb1.setName("jcb1");
      this.jcb2 = GuiUtil.getANYKCheckBox("Mező-újraszámítás");
      this.jcb2.setName("jcb2");
      this.jcb3 = GuiUtil.getANYKCheckBox("Ellenőrzés");
      this.jcb3.setName("jcb3");
      this.jcb4 = GuiUtil.getANYKCheckBox("Nyomtatás");
      this.jcb4.setName("jcb4");
      this.jcb5 = GuiUtil.getANYKCheckBox("Megjelölés elektr. feladásra");
      this.jcb5.setName("jcb5");
      this.jcb6 = GuiUtil.getANYKCheckBox("Megjelölés visszavonása");
      this.jcb6.setName("jcb6");
      this.fileListaMenteseCB = GuiUtil.getANYKCheckBox("Lista állomány készítése az elkészült állományokról");
      this.impHibaLista = new Vector();
      this.saveFileList = new Vector();
      this.dl = new DatLoader();
      this.xl = new XmlLoader();
      this.il = new ImpLoader();
      this.xkl = new XkrLoader();
      this.pdl = new PackedDataLoader();
      this.loaders = new Hashtable();
      this.status = new JProgressBar(0);
      this.tff = new BatchFunctions.TxtFileFileter();
      this.iff = new BatchFunctions.ImportFileFileter();
      this.defaultFileColumns = new Object[]{"Állomány", "Nyomtatvány neve", "Adószám", "Név", "Dátumtól", "Dátumig", "Státusz", "Információ", "Adóazonosító", "Mentve", "Megjegyzés", "Verzió", "Sablon verzió", "Kategória", "Szervezet", "kr fájl", "avdh aláírás", "nyomtatvány azonosító"};
      this.filters = new JComboBox();
      this.fc = new EJFileChooser();
      this.summaDialog = new JDialog(this, "", true);
      this.fileTableModel = new ReadOnlyTableModel(this.defaultFileColumns, 0);
      this.sorter = new TableSorter(this.fileTableModel);
      this.fileTable = new JTable(this.sorter);
      this.logLista = new EJList();
      this.logSP = new JScrollPane(this.logLista, 20, 30);
   }

   private void release() throws Exception {
      TemplateChecker.getInstance().setNeedDialog4Download(true);
      this.summaDialog = null;
      this.addButton.removeActionListener(this);
      this.add2Button.removeActionListener(this);
      this.listButton.removeActionListener(this);
      this.delButton.removeActionListener(this);
      this.saveButton.removeActionListener(this);
      this.okButton.removeActionListener(this);
      this.filters.removeActionListener(this);
      this.filters.removeAllItems();
      this.filters.removeItemListener(this);
      this.megsemButton.removeActionListener(this);
      this.fc.removeActionListener(this);
      this.fc = null;
      this.fileTableModel.removeTableModelListener(this);
      this.fileTable.removeKeyListener(this);
      this.addRemoveListeners(false);
      this.filters = null;
      this.tff = null;
      this.iff = null;
      this.logLista = null;

      for(int var1 = 0; var1 < this.resFilter.length; ++var1) {
         this.resFilter[var1] = null;
      }

      this.resFilter = null;

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

   public void cmdPrint2Jpg(String var1) {
      cmdMode = 2;
      this.fileListaMenteseCB.setEnabled(true);
      this.fileListaMenteseCB.setSelected(true);
      defaultDirectory = this.fc.getCurrentDirectory();
      this.jpgButton.setSelected(true);
      this.printFromCommandLine(var1);
   }

   private void printFromCommandLine(String var1) {
      Result var2 = new Result();
      this.chStr = "101100";
      this.loaders.put(".enyk", new CachedCollection());
      this.filters.addItem("Nyomtatványok");
      this.resFilter = new String[]{"inner_data_loader_v1"};
      this.importFunction = false;
      File var3 = new File(var1);
      if (!var3.isDirectory()) {
         var2.setOk(false);
         var2.errorList.add(new TextWithIcon("A megadott path nem könyvtár", 0));
         this.batchCheckFinished(var2);
      } else if (!var3.exists()) {
         var2.setOk(false);
         var2.errorList.add(new TextWithIcon("Nem található a fájl", 0));
         this.batchCheckFinished(var2);
      } else {
         Vector var4 = new Vector();

         try {
            this.parseFile2(var1, var4);
         } catch (Exception var6) {
            var6.printStackTrace();
            var2.setOk(false);
            var2.errorList.add(new TextWithIcon("Hiba a fájl értelmezésekor", 0));
            this.batchCheckFinished(var2);
            return;
         }

         if (var4.size() > 0) {
            var2.setOk(false);
            var2.errorList.add(new TextWithIcon("A fájl feldolgozása az alábbi hibákat eredményezte:", 1));
            var2.errorList.addAll(var4);
            var2.setOk(false);
            this.batchCheckFinished(var2);
         } else {
            this.alertTovabb = true;
            this.xkrMode = 0;
            this.doImp();
         }
      }
   }

   private void sortFTM() {
      Collections.sort(this.fileTableModel.getDataVector(), new Comparator() {
         public int compare(Object var1, Object var2) {
            return var1 instanceof Vector && var2 instanceof Vector ? ((String)((Vector)var1).get(1)).compareToIgnoreCase((String)((Vector)var2).get(1)) : 0;
         }
      });
   }

   public void cmdPrint2PdfFromList(String var1, String var2) {
      this.pdfPrintFromCommandLine(var1, var2, 1, (String)null);
   }

   public void cmdPrint2PdfFromXml(String var1, String var2) {
      this.pdfPrintFromCommandLine(var1, var2, 2, (String)null);
   }

   public void cmdPrint2PdfFromXml(String var1, String var2, String var3) {
      this.pdfPrintFromCommandLine(var1, var2, 2, var3);
   }

   private Result pdfPrintFromCommandLine(String var1, String var2, int var3, String var4) {
      if (PropertyList.getInstance().get("pdfxml.print.xml.silent") != null) {
         PropertyList.getInstance().set("pdfxml.print.xml.silent", var1);
      }

      Result var5 = new Result();
      this.pdfButton.setSelected(true);
      this.importFunction = true;
      Vector var6 = new Vector();
      this.loaders.put(".xml", new XmlLoader());
      defaultDirectory = new File((new File(var1)).getParent());
      switch(var3) {
      case 1:
      case 3:
         File var7 = new File(var1);
         if (!var7.exists()) {
            var6.add("Nem található a listafájl");
         } else {
            try {
               this.parseFile(var1, var6, false);
            } catch (Exception var10) {
               var5.setOk(false);
               var6.insertElementAt("Hiba a listafájl feldolgozásakor", 0);
            }
         }
         break;
      case 2:
      case 4:
         try {
            this.doLoadHeadData(new File(var1), (String)null);
         } catch (Exception var9) {
            var9.printStackTrace();
         }
      }

      this.alertTovabb = true;
      if (PropertyList.getInstance().get("html.create.xml.silent") == null) {
         var5 = this._doImp(-1, var1, var4);
      } else {
         var5 = this._doImp(1, var1, var4);
      }

      var5.errorList.addAll(var6);
      this.writeResultFile(var2, var5);
      return var5;
   }

   private Result _doImp(int var1, String var2, String var3) {
      Result var4 = new Result();

      try {
         if (this.fileTableModel.getRowCount() == 0) {
            var4.setOk(false);
            var4.errorList.add("Nincs nyomtatandó fájl!");
            return var4;
         }

         this.sortFileList();
         BookModel var5 = null;
         BookModel var6 = null;

         try {
            label131:
            while(true) {
               label120:
               while(true) {
                  if (this.fileTableModel.getRowCount() == 0) {
                     break label131;
                  }

                  try {
                     String var7 = ((String)this.fileTableModel.getValueAt(0, 0)).substring(((String)this.fileTableModel.getValueAt(0, 0)).lastIndexOf("."));
                     if (!this.loaders.containsKey(var7.toLowerCase())) {
                        var4.errorList.add("  - Hibás a fájl neve! (" + var7 + ")");
                        this.fileTableModel.removeRow(0);
                     } else {
                        if (!this.importFunction) {
                           var5 = this.loadInnerData(var6);
                           var6 = var5;
                        } else {
                           var5 = ((ILoadManager)this.loaders.get(var7.toLowerCase())).load((String)this.fileTableModel.getValueAt(0, 0), (String)this.fileTableModel.getValueAt(0, 17), (String)this.fileTableModel.getValueAt(0, 12), (String)this.fileTableModel.getValueAt(0, 14), var6);
                           var6 = var5;
                        }

                        if (var5 == null) {
                           var4.setOk(false);
                           var4.errorList.add("Hiba a fájl betöltésekor");
                           throw new Exception();
                        }

                        int var8 = var5.carryOnTemplate();
                        switch(var8) {
                        case 0:
                           this.fileTableModel.removeRow(0);
                           var4.setOk(false);
                           var4.errorList.add("Hiba a fájl betöltésekor: " + BookModel.CHECK_VALID_MESSAGES[var8]);
                           continue;
                        case 1:
                           int var9 = Tools.handleTemplateCheckerResult(var5);
                           if (var9 >= 4) {
                              this.fileTableModel.removeRow(0);
                              var4.setOk(false);
                              var4.errorList.add("Hiba a fájl betöltésekor: " + BookModel.CHECK_VALID_MESSAGES[var9]);
                           }
                        case 2:
                        default:
                           break;
                        case 3:
                           System.out.println("HIBA_A_SABLON_ERVENYESSEG_ELLENORZESKOR");
                        }

                        String[] var22;
                        if (!var5.hasError) {
                           if (var5.errormsg == null || var5.errormsg.length() <= 0) {
                              break;
                           }

                           var22 = var5.errormsg.split(";");
                           int var10 = 0;

                           while(true) {
                              if (var10 >= var22.length) {
                                 break label120;
                              }

                              var4.errorList.add(var22[var10]);
                              ++var10;
                           }
                        }

                        var4.setOk(false);
                        var4.errorList.add("  - Hiba történt a nyomtatvány betöltésekor!");
                        if (var5.errorlist != null) {
                           var4.errorList.addAll(var5.errorlist);
                        }

                        try {
                           var22 = var5.errormsg.split("##");
                           var4.errorList.add("           " + var22[0]);
                           var4.errorList.add("           " + var22[1]);
                        } catch (Exception var14) {
                           Tools.eLog(var14, 0);
                        }

                        this.fileTableModel.removeRow(0);
                     }
                  } catch (Exception var15) {
                     var4.errorList.add("  - Hiba történt a fájl betöltésekor");
                     this.fileTableModel.removeRow(0);
                  }
               }

               if (PropertyList.getInstance().get("pdfxml.print.xml.silent") != null) {
                  Result var19 = this._check();
                  if (var19.errorList.size() > 1) {
                     this.writeErroListToFile(var19.errorList, (String)PropertyList.getInstance().get("pdfxml.print.xml.silent"));
                  }
               }

               try {
                  var5.cc.setActiveObject(var5.cc.get(0));
                  var5.epost = "onlyinternet";
                  MainPrinter var20 = new MainPrinter(var5, (Vector)null, this.ps, true);
                  var20.batchPrint2Jpg = this.jpgButton.isSelected();
                  var20.batchPrint2Pdf = this.pdfButton.isSelected() || this.onePdfButton.isSelected();
                  var20.batchPrint2OnePdf = this.onePdfButton.isSelected();
                  var20.batchPrintSimpleMode = var1;
                  var20.init(false, false);
                  if (var3 != null) {
                     KrApp4PdfXml var21 = new KrApp4PdfXml(var2, var20.pdfFilename4Save, var3);
                     var21.createKrFile(var5);
                  }

                  var20.batchPrint2Pdf = false;
                  var20.batchPrint2Jpg = false;
                  var20.batchPrintSimpleMode = -1;

                  try {
                     var4.errorList.addAll(var20.batchJpgPrint.errorList);
                  } catch (Exception var13) {
                     Tools.eLog(var13, 0);
                  }
               } catch (Exception var16) {
                  var16.printStackTrace();
                  var4.errorList.add("  - Hiba történt a nyomtatáskor.");
               }

               this.fileTableModel.removeRow(0);
            }
         } catch (Exception var17) {
            var17.printStackTrace();
         }

         try {
            var5.destroy();
         } catch (Exception var12) {
            Tools.eLog(var12, 0);
         }

         try {
            var6.destroy();
         } catch (Exception var11) {
            Tools.eLog(var11, 0);
         }
      } catch (Exception var18) {
         var18.printStackTrace();
      }

      return var4;
   }

   private void writeResultFile(String var1, Result var2) {
      OutputStreamWriter var3 = null;
      OutputStreamWriter var4 = null;
      int var5 = 0;

      try {
         var3 = new OutputStreamWriter(new FileOutputStream(var1), "utf-8");
         var4 = new OutputStreamWriter(new FileOutputStream(var1 + "_hiba"), "utf-8");

         for(int var6 = 0; var6 < var2.errorList.size(); ++var6) {
            String var7 = var2.errorList.elementAt(var6).toString();
            if (var7.startsWith("A nyomtatvány képét a ")) {
               var3.write(var7.substring(22, var7.indexOf(" fájlba mentettük.")) + "\n");
            } else if (var7.startsWith("A PDF fájl ")) {
               var3.write(var7.substring(11, var7.indexOf(" néven elkészült.")) + "\n");
            } else if (var7.startsWith("A külön nyomtatandóként megjelölt lapokból a PDF fájl ")) {
               var3.write(var7.substring(54, var7.indexOf(" néven elkészült.")) + "\n");
            } else if (PropertyList.getInstance().get("html.create.xml.silent") != null) {
               var3.write(PropertyList.getInstance().get("html.create.xml.silent") + "\n");
            } else {
               var4.write(var7 + "\n");
               ++var5;
            }
         }
      } catch (Exception var22) {
         var22.printStackTrace();
      } finally {
         try {
            var3.close();
         } catch (Exception var20) {
            Tools.eLog(var20, 0);
         }

         try {
            var4.close();
         } catch (Exception var19) {
            Tools.eLog(var19, 0);
         }

      }

      if (var5 == 0) {
         try {
            File var24 = new File(var1 + "_hiba");
            var24.delete();
         } catch (Exception var21) {
            Tools.eLog(var21, 0);
         }
      }

   }

   private void handleZippedData(String var1, String var2, String var3, BookModel var4, String var5) throws Exception {
      if (var1.toLowerCase().endsWith(".xcz")) {
         String var6 = (new File(var2)).getName();
         var6 = var6.substring(0, var6.lastIndexOf("."));
         var6 = var6.substring(0, var6.lastIndexOf("."));
         File var7 = new File(this.getAttachPath() + var6);
         boolean var8 = var7.mkdir();
         if (!var8) {
            throw new Exception("nem sikerült a csatolmányok mappa létrehozása");
         } else {
            Result var9 = null;

            try {
               var9 = Tools.parseXCZFile(var1, var4, var3, var5);
            } catch (Exception var11) {
               this.writeAtcCountToFrmenyk(var4, var2);
               throw var11;
            }

            if (!var9.isOk()) {
               throw new Exception(var9.errorList.elementAt(0).toString());
            } else {
               this.writeAtcCountToFrmenyk(var4, var2);
            }
         }
      }
   }

   private void writeAtcCountToFrmenyk(BookModel var1, String var2) {
      try {
         int var3 = this.getAllCount(var1, var2);
         if (var3 == 0) {
            return;
         }

         var1.cc.docinfo.put("attachment_count", var3 + "");
         EnykInnerSaver var4 = new EnykInnerSaver(var1, true);
         var4.save(var2, -1, true);
      } catch (Exception var6) {
      }

   }

   private int getAllCount(BookModel var1, String var2) {
      int var3 = 0;

      try {
         String var5 = var2.substring(0, var2.toLowerCase().indexOf(".frm.enyk"));

         for(int var6 = 0; var6 < var1.forms.size(); ++var6) {
            FormModel var7 = (FormModel)var1.forms.get(var6);
            String var8 = var5 + "_" + var7.name + ".atc";
            File var9 = new File(var8);
            int var10 = 0;

            try {
               var10 = AttachementTool.loadAndCountAtcFile(var9);
            } catch (Exception var12) {
            }

            var3 += var10;
         }

         return var3;
      } catch (Exception var13) {
         return 0;
      }
   }

   private String getAttachPath() throws Exception {
      String var1 = (String)iplMaster.get("prop.usr.attachment");
      if (var1 == null) {
         throw new Exception("Nincs csatolmány mappa!");
      } else {
         var1 = root + var1;
         if (!var1.endsWith("\\") && !var1.endsWith("/")) {
            var1 = var1 + File.separator;
         }

         var1 = Tools.beautyPath(var1);
         return var1;
      }
   }

   private boolean _deleteAttachmentFiles(String var1) {
      HashSet var2 = new HashSet();
      File var3 = new File(PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.attachment"));
      File var4 = new File(PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.attachment") + File.separator + var1);
      if (!var3.equals(var4)) {
         var2.add(PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.attachment") + File.separator + var1);
         Tools.deleteFiles(var2);
         return true;
      } else {
         return false;
      }
   }

   public void passToDSign(String var1, String var2, String var3, String var4) {
      try {
         Result var5 = this.checkXsd(var1);
         if (!var5.isOk()) {
            this.doOutputP(var1, var2, (String)null, (String)null, var5.errorList, false);
            return;
         }

         FileLoader4BatchCheck var31 = new FileLoader4BatchCheck();
         var31.setFileToList(var1);
         XmlLoader var7 = new XmlLoader();
         Hashtable var8 = var31.loadHeadData(new File(var1), var7);

         Hashtable var9;
         try {
            var9 = (Hashtable)var8.get("docinfo");
            if (var9.size() == 0) {
               throw new Exception();
            }
         } catch (Exception var28) {
            var5.errorList.add("Nem sikerült az xml feldolgozása (docinfo)");
            this.doOutputP(var1, var2, (String)null, (String)null, var5.errorList, false);
            return;
         }

         String var10 = var9.containsKey("id") ? (String)var9.get("id") : "";
         String var11 = var9.containsKey("templatever") ? (String)var9.get("templatever") : "";
         String var12 = var9.containsKey("org") ? (String)var9.get("org") : "";
         BookModel var13 = var7.load(var1, var10, var11, var12);
         if (var7.headcheckfatalerror) {
            var13.hasError = true;
            var13.errormsg = var7.errormsg;
         }

         if (var7.fatalerror) {
            var13.hasError = true;
            var13.errormsg = var7.errormsg;
         }

         if (var13.hasError) {
            if (var13.errormsg != null) {
               var5.errorList.add(var13.errormsg);
            } else if (var7.warninglist != null) {
               var5.errorList.addAll(var7.warninglist);
            }

            if (BlacklistStore.getInstance().isBiztipDisabled(var13.getTemplateId(), var13.getOrgId())) {
               try {
                  String[] var32 = BlacklistStore.getInstance().getErrorListMessage(var13.getTemplateId(), var13.getOrgId());
                  var5.errorList.add(var32[0]);
                  if (var32.length > 1) {
                     var5.errorList.add(var32[1]);
                  }
               } catch (Exception var26) {
               }

               var5.setOk(false);
            }

            this.doOutputP(var1, var2, (String)null, (String)null, var5.errorList, false);
            return;
         }

         var5 = this._check();
         if (!var5.isOk()) {
            this.doOutputP(var1, var2, (String)null, (String)null, var5.errorList, false);
            return;
         }

         new BatchFunctions.Hibaszam();
         BatchFunctions.Hibaszam var14 = this.getErrorCounts(var5.errorList);
         if (!var3.equals("STOP_ON_ERROR") || var14.ihsz <= 0) {
            PropertyList.getInstance().set("batch.kr.pass2dsign", "");
            if ("NO_AVDH_SIGN".equals(var4)) {
               PropertyList.getInstance().set("prop.dynamic.avdhWithNoAuth", true);
            } else {
               Send2Mohu var15 = new Send2Mohu();
               Object[] var16 = var15.parseMohuFile(var4);
               PropertyList.getInstance().set("mohu_user", var16[0]);
               PropertyList.getInstance().set("mohu_pass", var16[1]);
               PropertyList.getInstance().set("mohu_reply2email", var16[2]);
               PropertyList.getInstance().set("prop.dynamic.mohu_user_and_pass_from_batch", true);
               PropertyList.getInstance().set("prop.dynamic.mohu_user_and_pass_from_batch_b64", true);
            }

            Ebev var33 = new Ebev(var13);
            Vector var34 = new Vector(var5.errorList);
            if (var13.isDisabledTemplate()) {
               try {
                  String[] var17 = BlacklistStore.getInstance().getErrorListMessage(var13.getTemplateId(), var13.getOrgId());
                  var5.errorList.add(var17[0]);
                  if (var17.length > 1) {
                     var5.errorList.add(var17[1]);
                  }
               } catch (Exception var27) {
               }

               var5.setOk(false);
            } else {
               var5 = var33.pass(true);
            }

            if (var5.isOk()) {
               if (var14.ihsz > 0) {
                  this.doOutputP(var1, var2, var10, var11, var34, true);
               } else {
                  this.doOutputP(var1, var2, var10, var11, (Vector)null, false);
               }

               return;
            } else {
               this.doOutputP(var1, var2, (String)null, (String)null, var5.errorList, false);
               return;
            }
         }

         this.doOutputP(var1, var2, (String)null, (String)null, var5.errorList, false);
      } catch (Exception var29) {
         Vector var6 = new Vector();
         var6.add("Programhiba: " + var29.toString());

         try {
            this.doOutputP(var1, var2, (String)null, (String)null, var6, false);
         } catch (IOException var25) {
            var25.printStackTrace();
         }

         return;
      } finally {
         PropertyList.getInstance().set("mohu_user", (Object)null);
         PropertyList.getInstance().set("mohu_pass", (Object)null);
         PropertyList.getInstance().set("mohu_reply2email", (Object)null);
         PropertyList.getInstance().set("prop.dynamic.mohu_user_and_pass_from_batch", (Object)null);
         PropertyList.getInstance().set("prop.dynamic.mohu_user_and_pass_from_batch_b64", (Object)null);
         PropertyList.getInstance().set("prop.dynamic.avdhWithNoAuth", (Object)null);
      }

   }

   public void encrypt(String var1, String var2, String var3, String var4) {
      Vector var5 = new Vector();

      try {
         File var6 = new File(var3);
         if (var6.exists()) {
            var5.add("A cél kr fájl (" + var3 + ") már létezik!");
            this.doOutputE(var2, var3, var4, var5);
            return;
         }

         String[] var7 = BlacklistStore.getInstance().isKrTemplateInBlackList(new File(var1));
         if (var7 != null && BlacklistStore.getInstance().isBiztipDisabled(var7[0], var7[1])) {
            String[] var14 = BlacklistStore.getInstance().getErrorListMessage(var7[0], var7[1]);
            var5.add(var14[0]);
            if (var14.length > 1) {
               var5.add(var14[1]);
            }

            this.doOutputE(var2, var3, var4, var5);
            return;
         }

         String var8 = "-cmd encrypt -src " + var2 + " -mf " + var1 + " -dest " + var3;
         KriptoApp var9 = new KriptoApp(var8);
         int var10 = var9.getExitCode();
         Properties var11 = this.getKriptoAppMessages();
         if (var10 == 0) {
            if ((new File(var3)).exists()) {
               this.doOutputE(var2, var3, var4, (Vector)null);
            } else {
               var5.add("hiba történt a titkosítás során!");
               this.doOutputE(var2, var3, var4, var5);
            }
         } else {
            var5.add(var11.getProperty(Integer.toString(var10)));
            this.doOutputE(var2, var3, var4, var5);
         }
      } catch (Exception var13) {
         var5.clear();
         var5.add("Programhiba: " + var13.toString());

         try {
            this.doOutputE(var3, var4, var3, var5);
         } catch (IOException var12) {
            var12.printStackTrace();
         }
      }

   }

   public void encryptPdf(String var1, String var2, String var3, String var4) {
      Vector var5 = new Vector();

      try {
         File var6 = new File(var3);
         if (var6.exists()) {
            var5.add("A cél kr fájl (" + var3 + ") már létezik!");
            this.doOutputE(var2, var3, var4, var5);
            return;
         }

         String var7 = "-cmd encrypt -src " + var2 + " -mf " + var1 + " -dest " + var3;
         KriptoApp var8 = new KriptoApp(var7);
         int var9 = var8.getExitCode();
         Properties var10 = this.getKriptoAppMessages();
         if (var9 == 0) {
            if ((new File(var3)).exists()) {
               this.doOutputE(var2, var3, var4, (Vector)null);
            } else {
               var5.add("hiba történt a titkosítás során!");
               this.doOutputE(var2, var3, var4, var5);
            }
         } else {
            var5.add(var10.getProperty(Integer.toString(var9)));
            this.doOutputE(var2, var3, var4, var5);
         }
      } catch (Exception var12) {
         var5.clear();
         var5.add("Programhiba: " + var12.toString());

         try {
            this.doOutputE(var3, var4, var3, var5);
         } catch (IOException var11) {
            var11.printStackTrace();
         }
      }

   }

   private Result _check() {
      IErrorList var1 = ErrorList.getInstance();
      ErrorListListener4XmlSave var2 = new ErrorListListener4XmlSave(-1);
      Vector var3 = null;
      boolean var4 = false;
      CalculatorManager var5 = CalculatorManager.getInstance();
      var2.clearErrorList();
      if (var3 != null) {
         var3.clear();
      }

      ((IEventSupport)var1).addEventListener(var2);
      CalculatorManager.xml = true;
      var5.do_check_all((IResult)null, var2);
      CalculatorManager.xml = false;
      ((IEventSupport)var1).removeEventListener(var2);
      var3 = var2.getErrorList();
      int var6 = var2.getFatalError();
      Result var7 = new Result();
      var7.setOk(var6 < 1);
      var7.setErrorList(var3);
      return var7;
   }

   private void doOutputP(String var1, String var2, String var3, String var4, Vector var5, boolean var6) throws IOException {
      FileOutputStream var7 = new FileOutputStream(var2);
      SendParams var8;
      String var9;
      if (var5 == null) {
         var7.write((var1 + ";" + "v.3.44.0" + ";" + var3 + ";" + var4 + "\n").getBytes());
         var8 = new SendParams(PropertyList.getInstance());
         var9 = (new File(var1)).getName();
         var7.write((var8.srcPath + var9 + ";" + var8.srcPath + var9.substring(0, var9.length() - 3) + "mf\n").getBytes());
      } else {
         if (var6) {
            var7.write((var1 + ";" + "v.3.44.0" + ";" + var3 + ";" + var4 + "\n").getBytes());
            var8 = new SendParams(PropertyList.getInstance());
            var9 = (new File(var1)).getName();
            var7.write((var8.srcPath + var9 + ";" + var8.srcPath + var9.substring(0, var9.length() - 3) + "mf\n").getBytes());
         } else {
            var7.write((var1 + "\n").getBytes());
         }

         var7.write("HIBA\n".getBytes());

         for(int var10 = 0; var10 < var5.size(); ++var10) {
            var7.write((var5.elementAt(var10).toString() + "\n").getBytes());
         }
      }

      var7.write("> Vége".getBytes());
      var7.close();
   }

   private void doOutputE(String var1, String var2, String var3, Vector var4) throws IOException {
      FileOutputStream var5 = new FileOutputStream(var3);
      if (var4 == null) {
         var5.write((var1 + ";" + "v.3.44.0" + "\n").getBytes());
         var5.write((var2 + "\n").getBytes());
      } else {
         var5.write((var1 + "\n").getBytes());
         var5.write("HIBA\n".getBytes());

         for(int var6 = 0; var6 < var4.size(); ++var6) {
            var5.write((var4.elementAt(var6).toString() + "\n").getBytes());
         }
      }

      var5.write("> Vége".getBytes());
      var5.close();
   }

   private void doOutputS(String var1, String var2, Result var3) throws IOException {
      FileOutputStream var4 = new FileOutputStream(var2);
      var4.write((var1 + ";" + "v.3.44.0" + "\n").getBytes());
      int var5;
      if (var3.isOk()) {
         var4.write(("RESULT:SUCCESS:" + var3.errorList.elementAt(0) + "\n").getBytes());

         try {
            for(var5 = 1; var5 < var3.errorList.size(); ++var5) {
               var4.write((var3.errorList.elementAt(var5).toString() + "\n").getBytes());
            }
         } catch (Exception var6) {
         }
      } else {
         var4.write("RESULT:ERROR\n".getBytes());

         for(var5 = 0; var5 < var3.errorList.size(); ++var5) {
            var4.write((var3.errorList.elementAt(var5).toString() + "\n").getBytes());
         }
      }

      var4.write("END".getBytes());
      var4.close();
   }

   private Result checkXsd(String var1) {
      Result var2 = new Result();
      Object var3 = null;

      try {
         XsdChecker var4 = new XsdChecker();
         File var5 = new File(var1);
         String var6 = var4.getEncoding(var5);
         var2 = var4._load(new FileInputStream(var5), var6);
      } catch (Exception var7) {
         var2.setOk(false);
         var2.errorList.add("Ebből a nyomtatvány-sablonból nem készíthető megfelelő formátumú xml állomány! A nyomtatvány nem jelölhető meg feladásra. XSD hiba: " + var7.getMessage());
      }

      return var2;
   }

   private Properties getKriptoAppMessages() {
      Properties var1 = new Properties();
      var1.setProperty(Integer.toString(0), "A titkosítás rendben befejeződött.");
      var1.setProperty(Integer.toString(-1), " titkosítási hiba.");
      var1.setProperty(Integer.toString(2), " naplózás sikertelen.");
      var1.setProperty(Integer.toString(10), " érvénytelen parancs.");
      var1.setProperty(Integer.toString(20), " metaállomány nem létezik");
      var1.setProperty(Integer.toString(21), " metaállomány nem állomány");
      var1.setProperty(Integer.toString(22), " metaállomány nem olvasható");
      var1.setProperty(Integer.toString(30), " forrásállomány nem létezik");
      var1.setProperty(Integer.toString(31), " forrásállomány nem állomány");
      var1.setProperty(Integer.toString(32), " forrásállomány nem olvasható");
      var1.setProperty(Integer.toString(40), " célállomány nem létezik");
      var1.setProperty(Integer.toString(41), " célállomány nem állomány");
      var1.setProperty(Integer.toString(42), " célállomány nem olvasható");
      var1.setProperty(Integer.toString(43), " célállomány nem írható");
      var1.setProperty(Integer.toString(50), " ideiglenes állomány nem létezik");
      var1.setProperty(Integer.toString(51), " ideiglenes állomány nem állomány");
      var1.setProperty(Integer.toString(52), " ideiglenes állomány nem olvasható");
      var1.setProperty(Integer.toString(53), " ideiglenes állomány nem írható");
      var1.setProperty(Integer.toString(70), " állomány átnevezése sikertelen");
      var1.setProperty(Integer.toString(83), " titkos kulcs nem írható");
      var1.setProperty(Integer.toString(93), " nyilvános kulcs nem írható");
      var1.setProperty(Integer.toString(100), " nem támogatott kulcstártípus");
      var1.setProperty(Integer.toString(110), " hiányzó kezdő címke");
      var1.setProperty(Integer.toString(111), " nem értelmezett záró címke");
      var1.setProperty(Integer.toString(120), " sikertelen a provider betöltése");
      return var1;
   }

   public void xczSend(String var1, String var2, String var3, String var4) {
      FileWriter var5 = null;

      try {
         File var43 = new File(var1);
         String var7 = var43.getParent();
         String var8 = (new File(var3)).getParent();
         if (var8 == null) {
            var3 = var7 + File.separator + var3;
         }

         SendParams var9 = new SendParams(PropertyList.getInstance());
         String var10 = Tools.beautyPath(var9.destPath + var43.getName().substring(0, var43.getName().length() - 3) + "kr");
         if (var1.equalsIgnoreCase(var3)) {
            throw new IOException("A log fájl felülírná a megadott xcz fájlt!");
         }

         var5 = new FileWriter(var3);
         var5.write((new SimpleDateFormat()).format(new Date()) + ";" + var1 + "\n");
         if ((new File(var10)).exists()) {
            var5.write("RESULT:ERROR:A létrehozandó kr fájl már létezik (" + var10 + ")\n");
            return;
         }

         PropertyList.getInstance().set("batch.kr.pass2dsign", "");
         if ("NO_AVDH_SIGN".equals(var4)) {
            PropertyList.getInstance().set("prop.dynamic.avdhWithNoAuth", true);
         } else {
            Send2Mohu var11 = new Send2Mohu();
            Object[] var12 = var11.parseMohuFile(var4);
            PropertyList.getInstance().set("mohu_user", var12[0]);
            PropertyList.getInstance().set("mohu_pass", var12[1]);
            PropertyList.getInstance().set("mohu_reply2email", var12[2]);
            PropertyList.getInstance().set("prop.dynamic.mohu_user_and_pass_from_batch", true);
            PropertyList.getInstance().set("prop.dynamic.mohu_user_and_pass_from_batch_b64", true);
         }

         XMLPost var44 = new XMLPost();
         var44.doneCMD(var1, var5, var2);
         if (PropertyList.getInstance().get("prop.dynamic.kr_filename_4_cmd_send") != null) {
            String var45 = (String)PropertyList.getInstance().get("prop.dynamic.kr_filename_4_cmd_send");
            File var13 = new File(var45);
            boolean var14 = var13.renameTo(new File(var10));
            if (!var14) {
               var14 = var13.renameTo(new File(var10));
            }

            if (!var14) {
               try {
                  var13.delete();
               } catch (Exception var36) {
               }

               var5.write("RESULT:ERROR:Nem sikerült a kr fájlt a megfelelő névre nevezni! (" + var45 + ")\n");
            } else {
               var5.write("RESULT:SUCCESS:" + var10 + "\n");
            }
         }
      } catch (FileNotFoundException var37) {
         FileNotFoundException var42 = var37;

         try {
            var5.write("RESULT:ERROR:" + var42.getMessage() + "\n");
         } catch (Exception var35) {
         }
      } catch (IOException var38) {
         IOException var41 = var38;

         try {
            var5.write("RESULT:ERROR:" + var41.getMessage() + "\n");
         } catch (Exception var34) {
         }
      } catch (Exception var39) {
         Exception var6 = var39;

         try {
            var5.write("RESULT:ERROR:" + var6.getMessage() + "\n");
         } catch (Exception var33) {
         }
      } finally {
         try {
            var5.write("END");
            var5.close();
         } catch (Exception var32) {
         }

         PropertyList.getInstance().set("prop.dynamic.avdhWithNoAuth", (Object)null);
         PropertyList.getInstance().set("mohu_user", (Object)null);
         PropertyList.getInstance().set("mohu_pass", (Object)null);
         PropertyList.getInstance().set("mohu_reply2email", (Object)null);
         PropertyList.getInstance().set("prop.dynamic.mohu_user_and_pass_from_batch", (Object)null);
         PropertyList.getInstance().set("prop.dynamic.mohu_user_and_pass_from_batch_b64", (Object)null);
      }

   }

   private void writeErroListToFile(Vector var1, String var2) throws IOException {
      var2 = var2 + ".errorList.txt";
      FileWriter var3 = null;

      try {
         boolean var4 = false;
         var3 = new FileWriter(var2);

         for(int var5 = 0; var5 < var1.size(); ++var5) {
            if (var1.elementAt(var5) instanceof TextWithIcon && ((TextWithIcon)var1.elementAt(var5)).imageType == 1) {
               var4 = true;
            }

            var3.write(var1.elementAt(var5).toString() + "\r\n");
         }

         if (var4) {
            var3.write("A nyomtatvány súlyos hibát tartalmaz, további ellenőrzést nem végzünk!\r\n");
         }
      } catch (Exception var14) {
         var14.printStackTrace();
      } finally {
         try {
            var3.close();
         } catch (Exception var13) {
         }

      }

   }

   private void deleteCstFile(String var1, String var2) {
      File var3 = new File(var1);
      String var4 = var3.getName();
      String var5 = var4.substring(0, var4.toLowerCase().indexOf(".frm.enyk"));
      String var6 = var3.getParent() + File.separator + var5 + ".cst";

      try {
         (new File(var6)).delete();
      } catch (Exception var15) {
         Tools.eLog(var15, 0);
      }

      var6 = var3.getParent() + File.separator + var5 + "_" + var2 + ".cst";

      try {
         (new File(var6)).delete();
      } catch (Exception var14) {
         Tools.eLog(var14, 0);
      }

      String var7 = PropertyList.getInstance().get("prop.usr.root") + "/" + PropertyList.getInstance().get("prop.usr.attachment") + "/" + var4;
      var7 = var7.substring(0, var7.toLowerCase().indexOf(".frm.enyk"));
      File var8 = new File(var7 + "/" + var2);
      if (var8.exists()) {
         if (var8.isDirectory()) {
            File[] var9 = var8.listFiles(new FilenameFilter() {
               public boolean accept(File var1, String var2) {
                  return var2.endsWith(".anyk.ASiC") || var2.endsWith(".urlap.anyk.xml");
               }
            });

            for(int var10 = 0; var10 < var9.length; ++var10) {
               File var11 = var9[var10];
               if (!var11.delete()) {
                  try {
                     Ebev.log(1, new File(var1), var11.getAbsolutePath() + " fájl törlése nem sikerült.");
                  } catch (Exception var13) {
                     System.out.println("Nem sikerült a log fájl írása. " + var11.getAbsolutePath() + " fájl törlése nem sikerült.");
                  }
               }
            }

            this.deleteUrlapAsic(var5);
         }
      }
   }

   private void deleteUrlapAsic(String var1) {
      String var2 = krdir + PropertyList.getInstance().get("prop.usr.ds_src") + File.separator + var1 + "/alairt";
      File var3 = new File(var2 + File.separator + var1 + ".urlap.anyk.ASiC");
      if (!var3.delete()) {
         try {
            Ebev.log(1, var3, var3.getAbsolutePath() + " fájl törlése nem sikerült.");
         } catch (Exception var5) {
            System.out.println("Nem sikerült a log fájl írása. " + var3.getAbsolutePath() + " fájl törlése nem sikerült.");
         }
      }

   }

   private void releasePropertyListParams() {
      PropertyList.getInstance().set("prop.dynamic.ilyenkor", (Object)null);
      PropertyList.getInstance().set("prop.dynamic.mohu_user_and_pass_from_batch", (Object)null);
      PropertyList.getInstance().set("prop.dynamic.avdhWithNoAuth", (Object)null);
   }

   private String checkIfAllAtcExists() {
      String var1 = "";

      for(int var4 = 0; var4 < this.fileTableModel.getRowCount(); ++var4) {
         if (!"".equals(this.fileTableModel.getValueAt(var4, 16).toString())) {
            String var2 = (String)this.fileTableModel.getValueAt(var4, 0);
            var2 = var2.substring(0, var2.length() - ".frm.enyk".length()) + "_" + this.fileTableModel.getValueAt(var4, 17) + ".atc";
            File var3 = new File(var2);
            if (!var3.exists()) {
               var1 = var1 + "\n" + this.fileTableModel.getValueAt(var4, 0);
            }
         }
      }

      return var1;
   }

   private void handleNewLineInMessage(String var1) {
      String[] var2 = var1.split("\n");

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.impHibaLista.add(new TextWithIcon("    " + var2[var3], -1));
      }

   }

   private String handleXCZImport(String var1) throws Exception {
      ZipFile var2 = new ZipFile(var1);
      Enumeration var3 = var2.entries();
      String var4 = null;

      while(var3.hasMoreElements() && var4 == null) {
         ZipEntry var5 = (ZipEntry)var3.nextElement();
         if (var5.getName().toLowerCase().endsWith(".xml") && !var5.getName().toLowerCase().endsWith("_lenyomat.xml")) {
            var4 = var5.getName();
         }
      }

      if (var4 == null) {
         throw new Exception("Hibás a csomag, nem tartalmaz xml fájlt!");
      } else {
         var4 = var4.substring(0, var4.length() - 4);
         File var6 = new File(PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.saves") + File.separator + var4 + ".frm.enyk");
         if (var6.exists()) {
            throw new Exception("Az xcz csomagokat a bennük lévő xml fájl nevén kell kicsomagolni. Ez jelenleg nem lehetséges, mert az alábbi fájl már létezik:\n" + var6.getAbsolutePath() + "\nKérjük nevezze át, majd próbálja újra kicsomagolni az xcz-t!");
         } else {
            return var4;
         }
      }
   }

   private void parseErrorMessage(String var1) {
      String[] var2 = var1.split("\n");

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.impHibaLista.add(new TextWithIcon("    " + var2[var3], -1));
      }

   }

   private void add3rdPartySignErrors() {
      ArrayList var1 = (ArrayList)PropertyList.getInstance().get("prop.usr.xcz.batchOne.3rdPartySign");

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         this.impHibaLista.add(new TextWithIcon("    - " + (String)var1.get(var2), -1));
      }

   }

   private void setEditStateForBetoltErtek(boolean var1) {
      PropertyList.getInstance().set("desktop_edit_state_for_betolt_ertek", var1);
   }

   private int getCbPanelWidth() {
      String var1 = "WWEllenőrzésWWWWNyomtatásWWWWMegjelölés elektr. feladásraWWWWMegjelölés visszavonásaWW";
      return GuiUtil.getW(var1);
   }

   private void closeDialog(JDialog var1) {
      var1.setVisible(false);
      MainFrame.thisinstance.mp.funcreadonly = false;
      MainPanel var10001 = MainFrame.thisinstance.mp;
      MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
      MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
   }

   private class Hibaszam {
      public Vector hszv;
      public int ihsz;
      public boolean vhsz;

      private Hibaszam() {
         this.hszv = new Vector();
         this.ihsz = 0;
         this.vhsz = false;
      }

      // $FF: synthetic method
      Hibaszam(Object var2) {
         this();
      }
   }

   private class ImportFileFileter extends FileFilter implements java.io.FileFilter {
      private ImportFileFileter() {
      }

      public boolean accept(File var1) {
         if (var1.isDirectory()) {
            return true;
         } else {
            if (var1.isFile()) {
               if (var1.getName().toLowerCase().endsWith(".imp")) {
                  return true;
               }

               if (var1.getName().toLowerCase().endsWith(".dat")) {
                  return true;
               }

               if (var1.getName().toLowerCase().endsWith(".abv")) {
                  return true;
               }

               if (var1.getName().toLowerCase().endsWith(".kat")) {
                  return true;
               }

               if (var1.getName().toLowerCase().endsWith(".elk")) {
                  return true;
               }

               if (var1.getName().toLowerCase().endsWith(".xml")) {
                  return true;
               }

               if (var1.getName().toLowerCase().endsWith(".xkr")) {
                  return true;
               }

               if (var1.getName().toLowerCase().endsWith(".xcz")) {
                  return true;
               }
            }

            return false;
         }
      }

      public String getDescription() {
         return "ányk import fájlok (*.imp, *.dat, *.abv, *kat, *elk, *.xml, *.xkr, *.xcz)";
      }

      // $FF: synthetic method
      ImportFileFileter(Object var2) {
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
