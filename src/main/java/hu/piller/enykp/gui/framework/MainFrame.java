package hu.piller.enykp.gui.framework;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.fileloader.db.DbCalculationLoader;
import hu.piller.enykp.alogic.fileloader.db.DbLoader;
import hu.piller.enykp.alogic.fileloader.db.OnyaDbLoader;
import hu.piller.enykp.alogic.fileloader.db.RDbLoader;
import hu.piller.enykp.alogic.fileloader.xml.XMLPost;
import hu.piller.enykp.alogic.fileloader.xml.XmlQuickloader;
import hu.piller.enykp.alogic.fileutil.DeleteInPublicMode;
import hu.piller.enykp.alogic.masterdata.converter.internal.PAConverter;
import hu.piller.enykp.alogic.masterdata.converter.internal.PARepositoryCorrection;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityError;
import hu.piller.enykp.alogic.masterdata.repository.xml.MDRepositoryImpl;
import hu.piller.enykp.alogic.masterdata.sync.download.MasterDataDownload;
import hu.piller.enykp.alogic.masterdata.sync.download.statemachine.IStateChangeListener;
import hu.piller.enykp.alogic.masterdata.sync.download.statemachine.State;
import hu.piller.enykp.alogic.masterdata.sync.syncdir.SyncDirException;
import hu.piller.enykp.alogic.masterdata.sync.syncdir.SyncDirHandler;
import hu.piller.enykp.alogic.masterdata.sync.syncdir.SyncDirStatus;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.orghandler.OrgResource;
import hu.piller.enykp.alogic.panels.FormArchiver;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.alogic.templateutils.blacklist.BlacklistStore;
import hu.piller.enykp.alogic.templateutils.blacklist.provider.BlacklistProviderException;
import hu.piller.enykp.alogic.templateutils.blacklist.provider.BlacklistProviderFactory;
import hu.piller.enykp.alogic.templateutils.blacklist.provider.BlacklistProviderLocal;
import hu.piller.enykp.alogic.upgrademanager_v2_0.Directories;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeBusinessException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeLogger;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeManager;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.DownloadableComponents;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.NewComponents;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.UpgradableComponents;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.event.ComponentProcessingEvent;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.event.ComponentProcessingEventListener;
import hu.piller.enykp.alogic.upgrademanager_v2_0.lookupupgrades.UILookUpEventListener;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.viewer.DefaultMultiFormViewer;
import hu.piller.enykp.gui.viewer.PageViewer;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.interfaces.IService;
import hu.piller.enykp.kauclient.KauAuthMethod;
import hu.piller.enykp.kauclient.KauAuthMethods;
import hu.piller.enykp.util.JavaInfo;
import hu.piller.enykp.util.TimeStamp;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.EventLog;
import hu.piller.enykp.util.base.InitApplication;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.icon.ENYKIconSet;
import hu.piller.enykp.util.oshandler.OsFactory;
import hu.piller.enykp.util.proxy.ProxySettings;
import hu.piller.enykp.util.ssl.AnykSSLConfigurator;
import hu.piller.enykp.util.ssl.AnykSSLConfiguratorException;
import hu.piller.enykp.util.trace.TraceConfig;
import hu.piller.enykp.util.trace.TraceHandler;
import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.logging.Handler;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MainFrame extends JFrame {
   public static boolean masterDataLockCleanUpNeeded = false;
   public static MainFrame thisinstance;
   public MainPanel mp;
   public static boolean publicmode;
   public static boolean record;
   public static boolean playback;
   public static File recordfile = new File("d:\\recorded.txt");
   public static PrintWriter PW;
   Menubar mb;
   public static InitApplication ia;
   Component oldglass;
   JPanel newglass;
   JLabel glasslabel;
   public boolean glasslock;
   public static boolean conditionvaluefunction = false;
   public String origtitle;
   public static Color signcolor;
   public static String opmode;
   public static String role;
   public static String hasznalati_mod;
   public static Boolean updateRequired;
   public static String pid;
   public static File runfile;
   public static boolean fromubevframe;
   public static boolean toubevframe_wassave;
   public static boolean readonlymodefromubev;
   public static String erkdatum;
   public static String hasSignature;
   public static boolean isPart;
   public static boolean isPartOnlyMain;
   public static boolean ubevtesztmode;
   public static boolean onyaCheckMode;
   private static boolean biz17EditMode;
   public static String veszprole;
   public static String ubev_inditasi_kalkulalt;
   public static boolean datastorehistorylive;
   public static String adozovaljavit;
   public static BigDecimal dokumentum_id;
   public static BigDecimal elug_azon;
   public static BigDecimal[] d_id;
   public static BookModel db_prev_bm;
   public static boolean ellvitamode;
   public static boolean rogzitomode;
   public static String fixfidcode;
   public static boolean xml_loaded_dialog;
   public static boolean batch_recalc_mode;
   public static boolean xmlCalculationMode;
   private static File xmlCalculationRunFile;
   public static String batch_recalc_role;
   public static String revizor_valasza_to_ubevframe;
   public static boolean javithatosagLekerdezve;
   private String message_1 = "A programból való kilépés előtt a megnyitott XML állományt zárja be az Adatok/XML állomány bezárása funkcióval!";
   private String title_1 = "Hiba";
   public static final String updatestr = "frissítések elérhetők a Szerviz->frissítések alatt";
   private static String optionstr1;
   private static String optionstr2;
   private static String publicstr1;
   private static String publicstr2;
   private static String publictitle;
   private static String playendstr;
   private static String playendtitle;
   private static String updateerrstr1;
   private static String updateerrstr2;
   private static String updateerrstr3;
   public static boolean FTRmode;
   public static Document FTRdoc;
   public static Element FTRroot;
   public static Element FTRmezok;
   public static boolean recalc_in_progress;
   private static boolean trace_enabled;
   private static boolean debug_info_enabled;
   private static boolean modGui;
   private static final String[] log4jFiles;
   private static Rectangle allDisplayDimension;

   public MainFrame(String[] var1) throws HeadlessException {
      thisinstance = this;
      this.setUpJaxwsStreamingMode();
      setUpAnykSSLConnection();
      ProxySettings.getInstance().activate();
      this.setUpMasterDataSync();
      this.setTableBorderOSX();
      this.setUIManagerlabels();

      try {
         handleGitInit("UAT");
      } catch (Exception var25) {
         PropertyList.getInstance().set("prop.usr.git_init_data", (Object)null);
      }

      this.setDefaultCloseOperation(0);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent var1) {
            if (MainFrame.PW != null) {
               MainFrame.PW.close();
            }

            CalculatorManager.getInstance().closeDialog();
            boolean var2 = true;
            if (XMLPost.xmleditnemjo && MainFrame.this.mp.getDMFV() != null && !"10".equals(MainFrame.hasznalati_mod)) {
               try {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, MainFrame.this.message_1, MainFrame.this.title_1, 0);
                  return;
               } catch (Exception var4) {
               }
            }

            if (var2) {
               Menubar.thisinstance.exitcmd.execute();
            }

         }

         public void windowDeactivated(WindowEvent var1) {
            DefaultMultiFormViewer var2 = MainFrame.this.mp.getDMFV();
            if (var2 != null) {
               if ((Boolean)PropertyList.getInstance().get("fieldcheckdialog")) {
                  return;
               }

               if ((Boolean)PropertyList.getInstance().get("foadatcalculation")) {
                  return;
               }

               boolean var3 = false;

               try {
                  var3 = "Ellenőrzési, számítási üzenet".equals(((JDialog)var1.getOppositeWindow()).getTitle());
               } catch (Exception var6) {
               }

               if (var3) {
                  MainFrame.conditionvaluefunction = true;
               }

               try {
                  var2.fv.pv.pv.leave_component_nocheck();
               } catch (Exception var5) {
               }

               if (var3) {
                  MainFrame.conditionvaluefunction = false;
               }
            }

         }
      });
      this.setSize(800, 600);
      this.setLocationRelativeTo((Component)null);
      String var2 = "ÁNYK";
      if (opmode.equals("0")) {
         if (role.equals("1")) {
            var2 = var2 + "  - Adóügyi mód -";
         }

         if (role.equals("2")) {
            var2 = var2 + "  - Revizori mód -";
         }

         if (role.equals("3")) {
            var2 = var2 + "  - Utólagos revizori mód -";
         }
      }

      this.setTitle(var2);
      this.origtitle = var2;
      this.setName("abevmainframe");
      this.handleResources();
      GuiUtil.setLabelCommandCode();
      this.setJMenuBar(this.createMenubar());
      this.mp = new MainPanel();
      int var3 = 100;
      String var4 = SettingsStore.getInstance().get("GUI", "zoom");

      try {
         var3 = Integer.parseInt(var4);
      } catch (NumberFormatException var24) {
      }

      thisinstance.mp.getStatuspanel().zoom_slider.setValue(var3);
      this.getContentPane().setLayout(new BorderLayout());
      this.getContentPane().add(this.mp);

      try {
         if (SyncDirHandler.getSyncDirStatus().equals(SyncDirStatus.UNSERVED_QUERY)) {
            StatusPane.thisinstance.syncMessage.setText("<html><body><font color=\"blue\"><u>Kiszolgálatlan törzsadatletöltési kérelme van!</u></font></body></html>");
         }
      } catch (SyncDirException var23) {
         ErrorList.getInstance().writeError("MainFrame", var23.getMessage(), var23, (Object)null);
      }

      KeyStroke var5 = KeyStroke.getKeyStroke(118, 0);
      ActionListener var6 = new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Menubar.thisinstance.calculatorcmd.execute();
         }
      };
      this.getRootPane().getRootPane().registerKeyboardAction(var6, var5, 2);
      var5 = KeyStroke.getKeyStroke(114, 0);
      var6 = new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            try {
               if (!"0".equals(MainFrame.role)) {
                  PageViewer var2 = MainFrame.thisinstance.mp.getDMFV().fv.pv.pv;
                  var2.hist_action.actionPerformed((ActionEvent)null);
               }
            } catch (Exception var3) {
            }

         }
      };
      this.getRootPane().getRootPane().registerKeyboardAction(var6, var5, 2);
      this.oldglass = this.getGlassPane();
      this.newglass = new MainFrame.EnykGlassPane();
      this.setGlassPane(this.newglass);
      this.glasslock = false;
      PropertyList.getInstance().set("fieldcheckdialog", Boolean.FALSE);
      PropertyList.getInstance().set("foadatcalculation", Boolean.FALSE);
      Runnable var7 = new Runnable() {
         public void run() {
            PAConverter var2 = new PAConverter();
            var2.convert();
            Iterator var3 = var2.getInvalid().iterator();

            while(var3.hasNext()) {
               Entity var4 = (Entity)var3.next();
               EntityError[] var5 = var4.getValidityStatus();
               StringBuilder var6 = new StringBuilder("Sikertelen törzsadat konverzió!\n");
               if ("Adótanácsadó".equals(var4.getName())) {
                  var6.append("Adótanácsadó\n").append("Neve          : ").append(var4.getBlock("Név").getMasterData("Adótanácsadó neve").getValue()).append("\n").append("Azonosítószám : ").append(var4.getBlock("Név").getMasterData("Adótanácsadó azonosítószáma").getValue()).append("\n").append("Bizonyítvány  : ").append(var4.getBlock("Név").getMasterData("Adótanácsadó Bizonyítvány").getValue()).append("\n").append("HIBÁK:").append("\n");
               } else if ("Magánszemély".equals(var4.getName())) {
                  var6.append("Magánszemély\n").append("Neve              : ").append(var4.getBlock("Törzsadatok").getMasterData("Vezetékneve").getValue()).append(" ").append(var4.getBlock("Törzsadatok").getMasterData("Keresztneve").getValue()).append("\n").append("Adóazonosító jele : ").append(var4.getBlock("Törzsadatok").getMasterData("Adózó adóazonosító jele").getValue()).append("\n").append("HIBÁK:").append("\n");
               } else if ("Egyéni vállalkozó".equals(var4.getName())) {
                  var6.append("Egyéni vállalkozó\n").append("Neve              : ").append(var4.getBlock("Törzsadatok").getMasterData("Vezetékneve").getValue()).append(" ").append(var4.getBlock("Törzsadatok").getMasterData("Keresztneve").getValue()).append("\n").append("Adóazonosító jele : ").append(var4.getBlock("Törzsadatok").getMasterData("Adózó adóazonosító jele").getValue()).append("\n").append("Adószáma          : ").append(var4.getBlock("Törzsadatok").getMasterData("Adózó adószáma").getValue()).append("\n").append("HIBÁK:").append("\n");
               } else if ("Társaság".equals(var4.getName())) {
                  var6.append("Társaság\n").append("Cégnév   : ").append(var4.getBlock("Törzsadatok").getMasterData("Adózó neve").getValue()).append("\n").append("Adószáma : ").append(var4.getBlock("Törzsadatok").getMasterData("Adózó adószáma").getValue()).append("\n").append("HIBÁK:").append("\n");
               }

               EntityError[] var7 = var5;
               int var8 = var5.length;

               for(int var9 = 0; var9 < var8; ++var9) {
                  EntityError var10 = var7[var9];
                  var6.append("  * ").append(var10.getErrorMsg()).append("\n");
               }

               System.out.println(var6.toString());
            }

         }
      };
      Thread var8 = new Thread(var7);
      var8.start();
      String var9;
      if (!(new File(Directories.getSettingsPath() + File.separator + "corr.enyk")).exists()) {
         try {
            System.out.println("Várakozás a törzsadat konverzió befejezésére");
            var8.join();
         } catch (InterruptedException var22) {
            var22.printStackTrace();
         }

         System.out.println("Torzsadat korrekcio");
         var9 = "\n";
         PARepositoryCorrection var10 = new PARepositoryCorrection();
         var10.convert();
         Iterator var11 = var10.getInvalid().iterator();

         while(var11.hasNext()) {
            Entity var12 = (Entity)var11.next();
            EntityError[] var13 = var12.getValidityStatus();
            StringBuilder var14 = new StringBuilder("Sikertelen törzsadat korrekció!\n");
            if ("Adótanácsadó".equals(var12.getName())) {
               var14.append("Adótanácsadó\n").append("Neve          : ").append(var12.getBlock("Név").getMasterData("Adótanácsadó neve").getValue()).append(var9).append("Azonosítószám : ").append(var12.getBlock("Név").getMasterData("Adótanácsadó azonosítószáma").getValue()).append(var9).append("Bizonyítvány  : ").append(var12.getBlock("Név").getMasterData("Adótanácsadó Bizonyítvány").getValue()).append(var9).append("HIBÁK:").append(var9);
            } else if ("Magánszemély".equals(var12.getName())) {
               var14.append("Magánszemély\n").append("Neve              : ").append(var12.getBlock("Törzsadatok").getMasterData("Vezetékneve").getValue()).append(" ").append(var12.getBlock("Törzsadatok").getMasterData("Keresztneve").getValue()).append(var9).append("Adóazonosító jele : ").append(var12.getBlock("Törzsadatok").getMasterData("Adózó adóazonosító jele").getValue()).append(var9).append("HIBÁK:").append(var9);
            } else if ("Egyéni vállalkozó".equals(var12.getName())) {
               var14.append("Egyéni vállalkozó\n").append("Neve              : ").append(var12.getBlock("Törzsadatok").getMasterData("Vezetékneve").getValue()).append(" ").append(var12.getBlock("Törzsadatok").getMasterData("Keresztneve").getValue()).append(var9).append("Adóazonosító jele : ").append(var12.getBlock("Törzsadatok").getMasterData("Adózó adóazonosító jele").getValue()).append(var9).append("Adószáma          : ").append(var12.getBlock("Törzsadatok").getMasterData("Adózó adószáma").getValue()).append(var9).append("HIBÁK:").append(var9);
            } else if ("Társaság".equals(var12.getName())) {
               var14.append("Társaság\n").append("Cégnév   : ").append(var12.getBlock("Törzsadatok").getMasterData("Adózó neve").getValue()).append(var9).append("Adószáma : ").append(var12.getBlock("Törzsadatok").getMasterData("Adózó adószáma").getValue()).append(var9).append("HIBÁK:").append(var9);
            }

            EntityError[] var15 = var13;
            int var16 = var13.length;

            for(int var17 = 0; var17 < var16; ++var17) {
               EntityError var18 = var15[var17];
               var14.append("  * ").append(var18.getErrorMsg()).append(var9);
            }

            System.out.println(var14.toString());
         }

         try {
            (new File(Directories.getSettingsPath() + File.separator + "corr.enyk")).createNewFile();
         } catch (IOException var21) {
            System.err.println(var21.getMessage());
         }
      }

      Runtime.getRuntime().addShutdownHook(new Thread() {
         public synchronized void start() {
            if (MainFrame.masterDataLockCleanUpNeeded && MDRepositoryImpl.getLockPolicy() == 1 && (new File(MDRepositoryImpl.getLockfileName())).exists()) {
               (new File(MDRepositoryImpl.getLockfileName())).delete();
               MainFrame.masterDataLockCleanUpNeeded = false;
            }

         }
      });
      xmlCalculationMode = false;

      try {
         var9 = null;
         if (fromubevframe || opmode != null && !"0".equals(opmode)) {
            var9 = "";
         } else {
            try {
               var9 = BlacklistProviderFactory.newInstance().get();
            } catch (BlacklistProviderException var20) {
               System.out.println("ÁNYK kitiltott nyomtatványok megszerzése sikertelen (url)");
               ErrorList.getInstance().writeError("MainFrame - getBL(url)", "ÁNYK kitiltott nyomtatványok megszerzése sikertelen (url)", var20, (Object)null);

               try {
                  BlacklistProviderLocal var27 = new BlacklistProviderLocal(new File(Directories.getSettingsPath() + File.separator + "blacklist.xml"));
                  var9 = var27.get();
               } catch (Exception var19) {
                  System.out.println("ÁNYK kitiltott nyomtatványok megszerzése sikertelen (file)");
                  ErrorList.getInstance().writeError("MainFrame - getBL(file)", "ÁNYK kitiltott nyomtatványok megszerzése sikertelen (file)", var20, (Object)null);
                  var9 = "";
               }
            }

            if (var9.length() > 0) {
               this.saveBlXml(var9);
            }
         }

         BlacklistStore.getInstance(var9);
      } catch (Exception var26) {
         var26.printStackTrace();
      }

   }

   private void setTableBorderOSX() {
      if (OsFactory.getOsHandler().getOsName().toUpperCase().contains("OS X")) {
         UIManager.put("Table.gridColor", new ColorUIResource(Color.GRAY));
         UIManager.put("TableHeader.cellBorder", new LineBorder(Color.GRAY));
      }

   }

   public Menubar createMenubar() {
      if (opmode.equals("0")) {
         this.mb = new Menubar();
      } else {
         this.mb = new Menubar(opmode);
      }

      return this.mb;
   }

   public void setGlassLabel(String var1) {
      if (!this.glasslock) {
         if (var1 == null) {
            this.newglass.setVisible(false);
         } else {
            this.glasslabel.setText(var1);
            this.newglass.setVisible(true);
         }
      }
   }

   public String getGlassText() {
      return this.newglass.isVisible() ? this.glasslabel.getText() : null;
   }

   private void setUIManagerlabels() {
      UIManager.put("OptionPane.yesButtonText", "Igen");
      UIManager.put("OptionPane.noButtonText", "Nem");
      UIManager.put("OptionPane.cancelButtonText", "Mégsem");
      UIManager.put("FileChooser.title", "Válasszon fájlt");
      UIManager.put("FileChooser.lookInLabelText", "Hely :");
      UIManager.put("FileChooser.saveInLabelText", "Hely :");
      UIManager.put("FileChooser.filesOfTypeLabelText", "Fájltípus :");
      UIManager.put("FileChooser.upFolderToolTipText", "Egy szinttel feljebb");
      UIManager.put("FileChooser.fileNameLabelText", "Fájlnév :");
      UIManager.put("FileChooser.homeFolderToolTipText", "Nyitómappa");
      UIManager.put("FileChooser.newFolderToolTipText", "Új mappa");
      UIManager.put("FileChooser.listViewButtonToolTipTextlist", "Nézet");
      UIManager.put("FileChooser.detailsViewButtonToolTipText", "Részletek");
      UIManager.put("FileChooser.listViewButtonToolTipText", "Lista");
      UIManager.put("FileChooser.saveButtonText", "Mentés");
      UIManager.put("FileChooser.openButtonText", "Megnyitás");
      UIManager.put("FileChooser.directoryOpenButtonText", "Megnyitás");
      UIManager.put("FileChooser.cancelButtonText", "Mégsem");
      UIManager.put("FileChooser.updateButtonText", "Frissítés");
      UIManager.put("FileChooser.helpButtonText", "Segítség !");
      UIManager.put("FileChooser.saveButtonToolTipText", "Mentés");
      UIManager.put("FileChooser.openButtonToolTipText", "A kijelölt fájlok megnyitása");
      UIManager.put("FileChooser.directoryOpenButtonToolTipText", "A kijelölt mappa megnyitása");
      UIManager.put("FileChooser.cancelButtonToolTipText", "Mégsem");
      UIManager.put("FileChooser.updateButtonToolTipText", "Frissítés");
      UIManager.put("FileChooser.helpButtonToolTipText", "Segítség");
      UIManager.put("FileChooser.acceptAllFileFilterText", "Minden fájl");
      UIManager.put("FileChooser.fileNameHeaderText", "Név");
      UIManager.put("FileChooser.fileSizeHeaderText", "Méret");
      UIManager.put("FileChooser.fileTypeHeaderText", "Típus");
      UIManager.put("FileChooser.fileDateHeaderText", "Módosítva");
      UIManager.put("FileChooser.fileAttrHeaderText", "Attributum");
      UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
   }

   protected void processKeyEvent(KeyEvent var1) {
      super.processKeyEvent(var1);
      if (var1.isShiftDown() && var1.isControlDown() && var1.isAltDown()) {
         PropertyList.showDialog();
      }
   }

   private void startFindUpgrades() {
      if (opmode.equals("0")) {
         if (SettingsStore.getInstance().get("upgrade") != null && Boolean.parseBoolean(SettingsStore.getInstance().get("upgrade", "autoarchive"))) {
            FormArchiver.getInstance().done_bg();
         }

         UpgradeManager.buildCacheAndNotifyWhenHasUpgrade(new UILookUpEventListener());
      }
   }

   public static void main(String[] var0) {
      try {
         System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
         System.out.println("java.util.Arrays.useLegacyMergeSort = true");
      } catch (Exception var33) {
         System.out.println("HIBA : System.setProperty(\"java.util.Arrays.useLegacyMergeSort\",\"true\")");
      }

      System.out.println("main args");

      for(int var1 = 0; var1 < var0.length; ++var1) {
         System.out.println(var1 + ". |" + var0[var1] + "|");
      }

      setAllDisplayDimension();
      ia = InitApplication.getInstance(var0);
      if (!fromubevframe) {
         initGuiRedraw();
      } else {
         GuiUtil.getScreenResolutions();
         JLabel var34 = new JLabel("dummy");
         Font var2 = (new JLabel()).getFont().deriveFont(0);
         PropertyList.getInstance().set("prop.gui.font", var2);
         PropertyList.getInstance().set("prop.gui.font.size", var2.getSize());
         int var3 = var34.getFontMetrics(var2).getHeight();
         PropertyList.getInstance().set("prop.gui.item.height", var3);
      }

      SettingsStore.getInstance().set("gui", "mezőszámítás", "true");
      IPropertyList var35 = PropertyList.getInstance();
      String[] var36 = (String[])((String[])var35.get("prop.command"));
      File var37 = new File((String)var35.get("prop.sys.root") + "/lib");
      removeLog4j(var37);

      String var4;
      try {
         var4 = (String)((Vector)var35.get("prop.const.filltestrecording")).get(0);
         if ("igen".equals(var4)) {
            FTRmode = true;
         }
      } catch (Exception var32) {
      }

      if (fromubevframe) {
         SettingsStore.getInstance().set("gui", "felülírásmód", "true");
      } else {
         try {
            opmode = (String)((Vector)var35.get("prop.const.opmode")).get(0);
         } catch (Exception var31) {
            opmode = null;
         }

         try {
            role = (String)((Vector)var35.get("prop.const.role")).get(0);
         } catch (Exception var30) {
            role = null;
         }
      }

      try {
         hasznalati_mod = (String)((Vector)var35.get("prop.const.hasznalati_mod")).get(0);
      } catch (Exception var29) {
         hasznalati_mod = "0";
      }

      try {
         updateRequired = ((Vector)var35.get("prop.const.updateRequired")).get(0).equals("1");
      } catch (Exception var28) {
         updateRequired = true;
      }

      System.out.println("!!! updateRequired = " + updateRequired);
      ubevtesztmode = false;

      try {
         var4 = (String)((Vector)var35.get("prop.const.ubevtesztmode")).get(0);
         if ("igen".equals(var4)) {
            ubevtesztmode = true;
         }
      } catch (Exception var27) {
         Tools.eLog(var27, 0);
      }

      xml_loaded_dialog = false;

      try {
         var4 = (String)((Vector)var35.get("prop.const.xml_loaded_dialog")).get(0);
         if ("igen".equals(var4)) {
            xml_loaded_dialog = true;
         }
      } catch (Exception var26) {
         Tools.eLog(var26, 0);
      }

      pid = System.getProperty("pid");
      if (opmode == null) {
         opmode = "0";
      }

      if (role == null) {
         role = "0";
      }

      if (opmode.equals("2")) {
         System.out.println("ubevtesztmode=" + ubevtesztmode);
         role = "1";
         System.out.println("pid=" + pid);
         if (pid != null) {
            try {
               var4 = var35.get("prop.usr.root") + File.separator + var35.get("prop.usr.tmp") + File.separator + pid + ".run";
               runfile = new File(var4);
               runfile.createNewFile();
               System.out.println("runfile=" + var4);
            } catch (IOException var25) {
               Tools.eLog(var25, 0);
            }
         }
      }

      if ("10".equals(hasznalati_mod)) {
         role = "0";
      }

      var4 = System.getProperty("java.awt.headless");
      if (var4 != null && var4.equals("true")) {
         headlessmode(var36);
      }

      MainFrame var5 = new MainFrame(var0);
      addResetFontSize();

      int var6;
      int var7;
      int var8;
      int var9;
      try {
         var6 = Integer.parseInt(SettingsStore.getInstance().get("gui", "x"));
         var7 = Integer.parseInt(SettingsStore.getInstance().get("gui", "y"));
         var8 = Integer.parseInt(SettingsStore.getInstance().get("gui", "w"));
         var9 = Integer.parseInt(SettingsStore.getInstance().get("gui", "h"));
      } catch (NumberFormatException var24) {
         var6 = 0;
         var7 = 0;
         var8 = (int)Math.max(800.0D, 0.6D * (double)GuiUtil.getScreenW());
         var9 = (int)Math.max(600.0D, 0.8D * (double)GuiUtil.getScreenH());
      }

      var5.setSize(var8, var9);
      var5.setLocation(var6, var7);
      Boolean var10 = (Boolean)var35.get("prop.dynamic.public.mode");
      publicmode = var10 == null ? false : var10;
      if (publicmode) {
         Object[] var11 = new Object[]{optionstr1, optionstr2};
         int var12 = JOptionPane.showOptionDialog(var5, publicstr1 + "\n" + publicstr2, "Figyelmeztetés!", 0, 2, (Icon)null, var11, var11[1]);
         if (var12 != 0) {
            System.exit(0);
         }

         try {
            EventLog.getInstance().setLoggingOff();
         } catch (IOException var23) {
         }

         DeleteInPublicMode.getInstance().exit(true);
         var5.setTitle(var5.getTitle() + " - " + publictitle);
         thisinstance.origtitle = var5.getTitle();
      }

      Boolean var38 = (Boolean)var35.get("prop.dynamic.record");
      record = var38 == null ? false : var38;
      if (record) {
         try {
            PW = new PrintWriter(new FileOutputStream(recordfile));
            final Robot var39 = new Robot();
            Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
               public void eventDispatched(AWTEvent var1) {
                  String var2 = null;
                  int var3;
                  if (var1 instanceof MouseEvent) {
                     switch(var1.getID()) {
                     case 501:
                        var3 = ((MouseEvent)var1).getButton();
                        Point var4 = ((MouseEvent)var1).getPoint();
                        SwingUtilities.convertPointToScreen(var4, (Component)var1.getSource());
                        int var5 = var39.getPixelColor(var4.x, var4.y).getRGB();
                        if (var3 == 1) {
                           var2 = "C " + var3 + " " + var4.x + " " + var4.y + " " + var5;
                        }
                     }
                  }

                  if (var1 instanceof KeyEvent) {
                     switch(var1.getID()) {
                     case 401:
                        var3 = ((KeyEvent)var1).getKeyCode();
                        var2 = "P " + var3;
                        break;
                     case 402:
                        var3 = ((KeyEvent)var1).getKeyCode();
                        var2 = "R " + var3;
                     }
                  }

                  if (var2 != null) {
                     MainFrame.PW.println(var2);
                  }

               }
            }, 24L);
         } catch (FileNotFoundException var21) {
            var21.printStackTrace();
         } catch (AWTException var22) {
            var22.printStackTrace();
         }
      }

      Boolean var40 = (Boolean)var35.get("prop.dynamic.playback");
      playback = var40 == null ? false : var40;
      if (playback && recordfile.exists()) {
         Thread var13 = new Thread(new Runnable() {
            public void run() {
               try {
                  Thread.sleep(2000L);
                  BufferedReader var1 = new BufferedReader(new FileReader(MainFrame.recordfile));
                  Robot var2 = new Robot();

                  while(true) {
                     String var3 = var1.readLine();
                     if (var3 == null || var3.length() == 0) {
                        Thread.currentThread().interrupt();
                        GuiUtil.showMessageDialog(MainFrame.thisinstance, MainFrame.playendstr, MainFrame.playendtitle, 1);
                        break;
                     }

                     MainFrame.playback_done(var2, var3);
                     var2.delay(300);
                  }
               } catch (FileNotFoundException var4) {
               } catch (IOException var5) {
                  var5.printStackTrace();
               } catch (Exception var6) {
                  var6.printStackTrace();
               }

            }
         });
         var13.start();
      }

      if (var36 != null && var36.length != 0) {
         if (!var36[0].endsWith(".silent")) {
            var5.setVisible(true);
         }

         if (var36[0].equals("file.new")) {
            if (var36.length < 2) {
               var5.mb.newcmd.execute();
            } else {
               var5.mb.cmd_file_new(var36[1]);
            }
         } else if (var36[0].equals("file.open")) {
            if (var36.length < 2) {
               var5.mb.opencmd.execute();
            } else {
               var5.mb.cmd_file_open(var36[1], Boolean.FALSE);
            }
         } else if (var36[0].equals("file.open.readonly")) {
            if (var36.length < 2) {
               var5.mb.opencmd.execute();
            } else {
               var5.mb.cmd_file_open(var36[1], Boolean.TRUE);
            }
         } else if (var36[0].equals("file.import")) {
            if (var36.length < 2) {
               var5.mb.opencmd.execute();
            } else {
               var5.mb.cmd_file_import(var36[1]);
            }
         } else if (var36[0].equals("file.multiimport")) {
            if (var36.length >= 2) {
               var5.mb.cmd_file_multiimport(var36[1]);
            }
         } else if (var36[0].equals("edit.imp")) {
            if (var36.length >= 2) {
               var5.mb.cmd_edit_imp(var36[1]);
            }
         } else if (var36[0].equals("edit.xml")) {
            if (var36.length >= 2) {
               var5.mb.cmd_edit_xml(var36[1]);
            }
         } else if (var36[0].equals("edit.dat")) {
            if (var36.length >= 2) {
               var5.mb.cmd_edit_dat(var36[1]);
            }
         } else if (var36[0].equals("check.dir")) {
            if (var36.length >= 2) {
               var5.mb.cmd_check_dir(var36[1]);
            }
         } else if (var36[0].equals("check.xml")) {
            if (var36.length >= 2) {
               var5.mb.cmd_check_xml(var36[1]);
            }
         } else if (var36[0].equals("dsign.xml")) {
            if (var36.length >= 2) {
               var5.mb.cmd_dsign_xml(var36[1]);
            }
         } else if (var36[0].equals("show.xml")) {
            if (var36.length < 2) {
               var5.mb.xmldisplaycmd.execute();
            } else {
               var5.mb.cmd_show_xml(var36[1]);
            }
         } else if (var36[0].equals("db.xml.edit")) {
            if (var36.length < 2) {
               System.out.println("Hiányzó paraméterek: " + var36[0]);
            } else {
               var5.mb.cmd_db_xml_edit(var36[1]);
            }
         } else if (var36[0].equals("db.open.xml")) {
            if (var36.length < 2) {
               var5.mb.cmd_db_open_xml((String)null, false);
            } else {
               var5.mb.cmd_db_open_xml(var36[1], false);
            }
         } else if (var36[0].equals("db.open.readonly.xml")) {
            if (var36.length >= 2) {
               var5.mb.cmd_db_open_xml(var36[1], true);
            }
         } else if (var36[0].equals("db.open.readonly.xml.doku")) {
            if (var36.length >= 2) {
               var5.mb.cmd_db_open_xml_doku(var36[1], true);
            }
         } else if (var36[0].equals("db.open.xml.sp")) {
            if (var36.length >= 2) {
               var5.mb.cmd_db_open_xml_sp(var36[1], false);
            }
         } else if (var36[0].equals("db.open.readonly.xml.sp")) {
            if (var36.length >= 2) {
               var5.mb.cmd_db_open_xml_sp(var36[1], true);
            }
         } else if (var36[0].equals("db.check.xml")) {
            if (var36.length < 2) {
               var5.mb.cmd_db_check_xml((String)null);
            } else {
               var5.mb.cmd_db_check_xml(var36[1]);
            }
         } else if (var36[0].equals("db.check.xml.silent")) {
            if (var36.length < 2) {
               var5.mb.cmd_db_check_xml_silent((String)null);
            } else {
               var5.mb.cmd_db_check_xml_silent(var36[1]);
            }
         } else if (var36[0].equals("db.parse.xml.silent")) {
            if (var36.length < 2) {
               var5.mb.cmd_db_parse_xml_silent((String)null);
            } else {
               var5.mb.cmd_db_parse_xml_silent(var36[1]);
            }
         } else if (var36[0].equals("db.parse.xml.loop.silent")) {
            if (var36.length < 2) {
               var5.mb.cmd_db_parse_xml_loop_silent((String)null);
            } else {
               var5.mb.cmd_db_parse_xml_loop_silent(var36[1]);
            }
         } else if (var36[0].equals("print.file.list")) {
            if (var36.length < 2) {
               var5.mb.cmd_print_file_list((String)null);
            } else {
               var5.mb.cmd_print_file_list(var36[1]);
            }
         } else if (var36[0].equals("file.new.silent")) {
            if (var36.length >= 2) {
               var5.mb.cmd_file_new_silent(var36[1]);
            }
         } else if (var36[0].equals("rdb.open.readonly")) {
            if (var36.length >= 2) {
               readonlymodefromubev = true;
               var5.mb.cmd_rdb_open_readonly(var36[1]);
            }
         } else if (var36[0].equals("open.xml")) {
            if (var36.length >= 2) {
               String[] var42 = var36[1].split(" ");
               if (var42.length == 2) {
                  if ("generalas".equals(var42[1])) {
                     PropertyList.getInstance().set("generalas_miatti_ujraszamitas", Boolean.TRUE);
                     hasznalati_mod = "10";
                  }

                  var5.mb.cmd_open_xml(var42[0]);
               } else {
                  var5.mb.cmd_open_xml(var36[1]);
               }
            }
         } else if (var36[0].equals("slowcheck.xml.silent")) {
            if (var36.length >= 2) {
               var5.mb.cmd_check_xml_silent(var36[1]);
            }
         } else if (var36[0].equals("check.xml.silent")) {
            if (var36.length >= 2) {
               var5.mb.cmd_qcheck_xml_silent(var36[1]);
            }
         } else if (var36[0].equals("pdf.print.xml.silent")) {
            if (var36.length >= 2) {
               var5.mb.cmd_pdf_print_xml_silent(var36[1], false);
            }
         } else if (var36[0].equals("html.create.xml.silent")) {
            if (var36.length >= 2) {
               var5.mb.cmd_pdf_print_xml_silent(var36[1], true);
            }
         } else if (var36[0].equals("atadas_alairashoz.xml.silent")) {
            if (var36.length >= 2) {
               var5.mb.cmd_atadas_alairashoz_xml_silent(var36[1]);
            }
         } else if (var36[0].equals("krkeszites.xml.silent")) {
            if (var36.length >= 2) {
               var5.mb.cmd_krkeszites_xml_silent(var36[1]);
            }
         } else if (var36[0].equals("bekuldes.kr.silent")) {
            if (var36.length >= 2) {
               System.out.println("A művelet támogatása az Ügyfélkapu használatának kivezetése miatt megszűnt!");
            }
         } else if (var36[0].equals("krkeszites.xcz.silent")) {
            if (var36.length >= 2) {
               var5.mb.cmd_bekuldes_xcz_silent(var36[1]);
            }
         } else if (var36[0].equals("pdfxml.print.xml.silent")) {
            if (var36.length >= 2) {
               PropertyList.getInstance().set("pdfxml.print.xml.silent", Boolean.TRUE);
               var5.mb.cmd_pdf_print_xml_silent(var36[1], false);
            }
         } else if (var36[0].equals("pdfxml.kr.silent")) {
            if (var36.length >= 2) {
               PropertyList.getInstance().set("pdfxml.print.xml.silent", Boolean.TRUE);
               var5.mb.cmd_pdf_kr_silent(var36[1]);
            }
         } else if (var36[0].equals("db.recalc.loop.silent")) {
            if (var36.length == 2) {
               cmd_db_recalc_barcode_loop_silent("teszt");
            } else {
               cmd_db_recalc_barcode_loop_silent("");
            }
         } else if (var36[0].equals("db.web_check.loop.silent")) {
            if (var36.length == 2) {
               cmd_db_web_check_loop_silent("teszt");
            } else {
               cmd_db_web_check_loop_silent("");
            }
         } else {
            System.out.println("Nem értelmezett parancs!   ( " + var36[0] + " )");

            try {
               runfile.delete();
            } catch (Exception var20) {
            }

            try {
               xmlCalculationRunFile.delete();
            } catch (Exception var19) {
            }

            System.exit(0);
         }

         if (var36[0].endsWith(".silent")) {
            boolean var43 = true;
            Object var14 = PropertyList.getInstance().get("no_exit");
            if (var14 != null) {
               var43 = false;
            }

            if (var43) {
               try {
                  runfile.delete();
               } catch (Exception var18) {
               }

               try {
                  xmlCalculationRunFile.delete();
               } catch (Exception var17) {
               }

               System.exit(0);
            }
         }
      } else {
         var5.setVisible(true);
         int var41 = ToolTipManager.sharedInstance().getDismissDelay();
         ToolTipManager.sharedInstance().setDismissDelay(8000);
         System.out.println("" + var41);
         if (updateRequired) {
            var5.startFindUpgrades();
         }
      }

      ia.alertNoUK();

      try {
         handleJavaFxMessage();
      } catch (Exception var16) {
         System.out.println("Hiba a browseres azonosítás ellenőrzésekor!");
         var16.printStackTrace();
      }

      long var44 = Runtime.getRuntime().freeMemory() / 1024L / 1024L;
      System.out.println("Free memory=" + var44 + " MB");
   }

   private static void delxmlandatcfromtmpdir() {
      File var0 = new File((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.tmp"));
      File[] var1 = var0.listFiles(new FilenameFilter() {
         public boolean accept(File var1, String var2) {
            if (var2.toLowerCase().endsWith(".xml")) {
               return true;
            } else {
               return var2.toLowerCase().endsWith(".atc");
            }
         }
      });

      for(int var2 = 0; var2 < var1.length; ++var2) {
         File var3 = var1[var2];
         var3.delete();
      }

   }

   private static void playback_done(Robot var0, String var1) {
      char var2 = var1.charAt(0);
      String[] var3 = var1.split(" ");
      int var4;
      switch(var2) {
      case 'C':
         try {
            var0.delay(300);
            var4 = Integer.parseInt(var3[1]);
            if (var4 == 1) {
               var4 = 16;
            } else if (var4 == 2) {
               var4 = 8;
            } else if (var4 == 3) {
               var4 = 4;
            }

            int var5 = Integer.parseInt(var3[2]);
            int var6 = Integer.parseInt(var3[3]);
            int var7 = Integer.parseInt(var3[4]);

            while(true) {
               int var8 = var0.getPixelColor(var5, var6).getRGB();
               if (var8 == var7) {
                  var0.mouseMove(var5, var6);
                  var0.mousePress(var4);
                  var0.mouseRelease(var4);
                  return;
               }

               var0.delay(200);
            }
         } catch (NumberFormatException var11) {
            var11.printStackTrace();
            break;
         }
      case 'P':
         try {
            var4 = Integer.parseInt(var3[1]);
            var0.keyPress(var4);
         } catch (NumberFormatException var10) {
            var10.printStackTrace();
         }
         break;
      case 'R':
         try {
            var4 = Integer.parseInt(var3[1]);
            var0.keyRelease(var4);
         } catch (NumberFormatException var9) {
            var9.printStackTrace();
         }
      }

   }

   private static void headlessmode(String[] var0) {
      if (var0 != null && var0.length != 0) {
         if (var0[0].equals("db.parse.xml.loop.silent")) {
            if (var0.length < 2) {
               cmd_db_parse_xml_loop_silent((String)null);
            } else {
               cmd_db_parse_xml_loop_silent(var0[1]);
            }
         } else if (var0[0].equals("db.check.xml.loop.silent")) {
            if (var0.length < 2) {
               cmd_db_check_xml_loop_silent((String)null);
            } else {
               cmd_db_check_xml_loop_silent(var0[1]);
            }
         } else if (var0[0].equals("db.check.barcode.silent")) {
            if (var0.length >= 2) {
               String[] var1 = var0[1].split(" ");
               String var2 = null;

               try {
                  var2 = var1[0];
               } catch (Exception var8) {
                  var2 = "";
               }

               String var3 = null;

               try {
                  var3 = var1[1];
               } catch (Exception var7) {
                  var3 = "";
               }

               cmd_db_check_barcode_silent(var2, var3);
            }
         } else if (var0[0].equals("db.service.run.silent")) {
            if (var0.length < 2) {
               cmd_db_service_run_silent("db_service_run_silent");
            } else {
               cmd_db_service_run_silent(var0[1]);
            }
         } else if (var0[0].equals("db.check.barcode.loop.silent")) {
            if (var0.length < 2) {
               cmd_db_check_barcode_loop_silent((String)null);
            } else {
               cmd_db_check_barcode_loop_silent(var0[1]);
            }
         } else if (var0[0].equals("db.recalc.loop.silent")) {
            if (var0.length == 2) {
               cmd_db_recalc_barcode_loop_silent("teszt");
            } else {
               cmd_db_recalc_barcode_loop_silent("");
            }
         } else if (var0[0].equals("db.web_check.loop.silent")) {
            if (var0.length == 2) {
               cmd_db_web_check_loop_silent("teszt");
            } else {
               cmd_db_web_check_loop_silent("");
            }
         } else if ("replicate.silent".equalsIgnoreCase(var0[0])) {
            setUpAnykSSLConnection();
            ProxySettings.getInstance().activate();
            if (var0.length >= 2) {
               cmd_replicate_silent(var0[1]);
            } else {
               cmd_replicate_silent("jar");
            }
         } else if ("instnew.silent".equalsIgnoreCase(var0[0])) {
            setUpAnykSSLConnection();
            ProxySettings.getInstance().activate();
            cmd_upgrade_and_new_silent();
         } else if ("instupgrade.silent".equalsIgnoreCase(var0[0])) {
            setUpAnykSSLConnection();
            ProxySettings.getInstance().activate();
            cmd_upgrade_silent();
         } else if ("pdfgen.silent".equalsIgnoreCase(var0[0])) {
            cmd_pdfgen_silent();
         }
      }

      boolean var9 = true;
      Object var10 = PropertyList.getInstance().get("no_exit");
      if (var10 != null) {
         var9 = false;
      }

      if (var9) {
         try {
            runfile.delete();
         } catch (Exception var6) {
         }

         try {
            xmlCalculationRunFile.delete();
         } catch (Exception var5) {
         }

         System.exit(0);
      }

   }

   private static void cmd_pdfgen_silent() {
      Object var0 = new Object();
      IService var1 = null;
      boolean[] var2 = new boolean[]{false};

      try {
         Class var3 = Class.forName("hu.piller.service.pdfgen.JmsPdfgenService");
         var1 = (IService)var3.newInstance();
      } catch (InstantiationException | IllegalAccessException | ClassNotFoundException var6) {
         var6.printStackTrace();
         return;
      }

      var1.startService();
      (new Thread(() -> {
         File var2x = new File("pdfgen.pid");
         System.out.println(TimeStamp.getNow() + " pid file " + var2x.getAbsolutePath());

         while(var2x.exists()) {
            try {
               Thread.sleep(5000L);
            } catch (InterruptedException var6) {
               var6.printStackTrace();
               break;
            }
         }

         var2[0] = true;
         synchronized(var0) {
            var0.notifyAll();
         }
      }, "pid-watcher")).start();
      System.out.println(TimeStamp.getNow() + " AbevJava PDFGEN service is running!");
      synchronized(var0) {
         while(!var2[0]) {
            try {
               var0.wait();
            } catch (InterruptedException var7) {
               System.out.println(TimeStamp.getNow() + " Oops!!!");
               var7.printStackTrace();
               break;
            }
         }
      }

      var1.shutdownService();
      System.out.println(TimeStamp.getNow() + " AbevJava PDFGEN service stopped!");
   }

   private static void cmd_db_service_run_silent(String var0) {
      RDbLoader var1 = new RDbLoader();
      var1.service_run(var0);
   }

   private static void cmd_replicate_silent(String var0) {
      try {
         DownloadableComponents var1 = UpgradeManager.getDonwloadableComponentsToReplicate();
         var1.setPreferredFormat(var0);
         var1.addComponentProcessedEventListener(new ComponentProcessingEventListener() {
            private UpgradeLogger logger = UpgradeLogger.getInstance();

            public void componentProcessed(ComponentProcessingEvent var1) {
               if (var1.getState() == 1) {
                  String var2 = " ";
                  StringBuilder var3 = new StringBuilder();
                  var3.append(MainFrame.updateerrstr1);
                  var3.append(var2);
                  var3.append(var1.getOrganization());
                  var3.append(var2);
                  var3.append(var1.getCategory());
                  var3.append(var2);
                  var3.append(var1.getName());
                  var3.append(var2);
                  var3.append(var1.getVersion());
                  if (var1.getMessage() != null) {
                     var3.append(var2);
                     var3.append(var1.getMessage());
                  }

                  this.logger.log(var3.toString());
               }

            }
         });
         var1.replicate();
      } catch (UpgradeBusinessException var2) {
         System.err.println(var2.getMessage());
      }

   }

   private static void cmd_upgrade_silent() {
      UpgradableComponents var0 = UpgradeManager.getUpgradableComponents();
      if (var0.hasUpgradeable()) {
         try {
            var0.addComponentProcessedEventListener(new ComponentProcessingEventListener() {
               private UpgradeLogger logger = UpgradeLogger.getInstance();

               public void componentProcessed(ComponentProcessingEvent var1) {
                  if (var1.getState() == 1) {
                     String var2 = " ";
                     StringBuilder var3 = new StringBuilder();
                     var3.append(MainFrame.updateerrstr2);
                     var3.append(var2);
                     var3.append(var1.getOrganization());
                     var3.append(var2);
                     var3.append(var1.getCategory());
                     var3.append(var2);
                     var3.append(var1.getName());
                     var3.append(var2);
                     var3.append(var1.getVersion());
                     if (var1.getMessage() != null) {
                        var3.append(var2);
                        var3.append(var1.getMessage());
                     }

                     this.logger.log(var3.toString());
                  }

               }
            });
            var0.install();
         } catch (Exception var2) {
            System.err.println(var2.getMessage());
         }
      }

   }

   private static void cmd_upgrade_and_new_silent() {
      NewComponents var0 = UpgradeManager.getNewAndUpgradableComponents();

      try {
         var0.addComponentProcessedEventListener(new ComponentProcessingEventListener() {
            private UpgradeLogger logger = UpgradeLogger.getInstance();

            public void componentProcessed(ComponentProcessingEvent var1) {
               if (var1.getState() == 1) {
                  String var2 = " ";
                  StringBuilder var3 = new StringBuilder();
                  var3.append(MainFrame.updateerrstr3);
                  var3.append(var2);
                  var3.append(var1.getOrganization());
                  var3.append(var2);
                  var3.append(var1.getCategory());
                  var3.append(var2);
                  var3.append(var1.getName());
                  var3.append(var2);
                  var3.append(var1.getVersion());
                  if (var1.getMessage() != null) {
                     var3.append(var2);
                     var3.append(var1.getMessage());
                  }

                  this.logger.log(var3.toString());
               }

            }
         });
         var0.install();
      } catch (Exception var2) {
         System.err.println(var2.getMessage());
      }

   }

   private static void cmd_db_parse_xml_loop_silent(String var0) {
      XmlQuickloader var1 = new XmlQuickloader(1, var0);
      var1.loop_qload();
   }

   private static void cmd_db_check_xml_loop_silent(String var0) {
      boolean var1 = true;
      int var2 = 0;
      System.out.println("Start:" + new Date());
      long var3 = System.currentTimeMillis();

      while(true) {
         DbLoader var5 = new DbLoader(1, var0);
         Hashtable var6 = var5.check();
         ++var2;
         if (var2 % 10 == 0 && var1) {
            System.out.println("count=" + var2 + "    time=" + (System.currentTimeMillis() - var3) / 1000L + " sec  alldb=" + DbLoader.db + "   maxM=" + DbLoader.max);
         }

         if (var6.get("stop") != null) {
            return;
         }

         if (var6.get("idle") != null) {
            try {
               int var7 = 600000;
               Integer var8 = (Integer)PropertyList.getInstance().get("prop.dynamic.db.idle");
               if (var8 != null) {
                  var7 = var8 * 60 * 1000;
               }

               Thread.sleep((long)var7);
            } catch (InterruptedException var9) {
            }
         }
      }
   }

   private static void cmd_db_check_barcode_silent(String var0, String var1) {
      long var2 = System.nanoTime();
      if ("teszt".equals(var1)) {
         RDbLoader.tesztmode = true;
      }

      RDbLoader var4 = new RDbLoader();
      var4.done3(var0);
      long var5 = System.nanoTime();
      System.out.println("time in sec: " + (var5 - var2) / 1000000000L);
   }

   private static void cmd_db_check_barcode_loop_silent(String var0) {
      if ("teszt".equals(var0)) {
         RDbLoader.tesztmode = true;
      } else {
         PropertyList.getInstance().set("prop.sys.dynamic.ubev.batch.biztip", var0);
      }

      RDbLoader var1 = new RDbLoader();
      var1.doneloop();
   }

   private static Hashtable done_check(String var0) {
      Hashtable var1 = new Hashtable();
      String var2 = "";
      String var3 = "";
      String var4 = "";
      DbLoader var5 = new DbLoader(1, var0);
      BookModel var6 = var5.load((String)null, var2, var3, var4);
      if (!var6.hasError) {
         try {
            Vector var7 = Tools.check();
            if (var5.save(var7)) {
               System.out.println("Az eredmény az adatbázisban!");
            }
         } catch (Exception var8) {
            var8.printStackTrace();
         }
      }

      return var1;
   }

   public String getOrigTitle() {
      return this.origtitle;
   }

   private static void cmd_db_recalc_barcode_loop_silent(String var0) {
      if ("teszt".equals(var0)) {
         RDbLoader.tesztmode = true;
      } else {
         PropertyList.getInstance().set("prop.sys.dynamic.ubev.batch.biztip", var0);
      }

      if (pid == null) {
         pid = "ReCalc_" + System.currentTimeMillis();
      }

      System.out.println("pid=" + pid);

      try {
         IPropertyList var1 = PropertyList.getInstance();
         String var2 = var1.get("prop.usr.root") + File.separator + var1.get("prop.usr.tmp") + File.separator + pid + ".run";
         xmlCalculationRunFile = new File(var2);
         xmlCalculationRunFile.createNewFile();
         System.out.println("xmlCalculationRunFile=" + var2);
      } catch (IOException var3) {
         Tools.eLog(var3, 0);
      }

      DbCalculationLoader var4 = new DbCalculationLoader();
      var4.doneLoop();
   }

   private static void cmd_db_web_check_loop_silent(String var0) {
      if ("teszt".equals(var0)) {
         RDbLoader.tesztmode = true;
      } else {
         PropertyList.getInstance().set("prop.sys.dynamic.ubev.batch.biztip", var0);
      }

      if (pid == null) {
         pid = "ReCalc_" + System.currentTimeMillis();
      }

      System.out.println("pid=" + pid);

      try {
         IPropertyList var1 = PropertyList.getInstance();
         String var2 = var1.get("prop.usr.root") + File.separator + var1.get("prop.usr.tmp") + File.separator + pid + ".run";
         xmlCalculationRunFile = new File(var2);
         xmlCalculationRunFile.createNewFile();
         System.out.println("xmlCheckRunFile=" + var2);
      } catch (IOException var3) {
         Tools.eLog(var3, 0);
      }

      OnyaDbLoader var4 = new OnyaDbLoader();
      onyaCheckMode = true;
      role = "1";
      var4.doneLoop();
   }

   public void writePrintLog() {
      Menubar.cmd_logprint("LOG");
   }

   private void handleResources() {
      OrgInfo var1 = OrgInfo.getInstance();
      Enumeration var2 = ((Hashtable)var1.getOrgList()).keys();
      boolean var3 = false;

      while(var2.hasMoreElements()) {
         Object var4 = var2.nextElement();
         OrgResource var5 = (OrgResource)((Hashtable)var1.getOrgList()).get(var4);
         if (!"".equals(var5.getEperGateOption())) {
            if ("".equals(SettingsStore.getInstance().get("gui", "epergate"))) {
               SettingsStore.getInstance().set("gui", "epergate", "true");
            }

            var3 = true;
         }
      }

      if (!var3) {
         SettingsStore.getInstance().set("gui", "epergate", "");
      }

   }

   private void setUpJaxwsStreamingMode() {
      System.setProperty("jaxws.transport.streaming", "true");
   }

   private static void setUpAnykSSLConnection() {
      try {
         AnykSSLConfigurator.initSSL();
      } catch (AnykSSLConfiguratorException var1) {
         ErrorList.getInstance().writeError(1000, "Megbízható, titkosított internet kapcsolat nem üzemkész", var1, (Object)null);
      }

   }

   private void setUpMasterDataSync() {
      System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
      SyncDirHandler.checkSyncDirExists();
      SyncDirHandler.checkSyncArchiveDirExists();
      MasterDataDownload.getInstance().addStateChangeListener(new IStateChangeListener() {
         public void stateChanged(State var1, State var2) {
            if (var1 == State.READY && var2 == State.WAITING) {
               StatusPane.thisinstance.syncMessage.setText("Törzsadatlekérdezési kérelem kiszolgálását figyelő folyamat fut");
               if (!MasterDataDownload.getInstance().isOperationRunning()) {
                  MasterDataDownload.getInstance().resumeOperation();
               }
            }

         }
      });
      MasterDataDownload.getInstance().addStateChangeListener(new IStateChangeListener() {
         public void stateChanged(State var1, State var2) {
            if (var1 == State.WAITING && var2 == State.PROCESSING) {
               StatusPane.thisinstance.syncMessage.setForeground(Color.RED);
               StatusPane.thisinstance.syncMessage.setText("<html><body><b>A NAV megválaszolta a törzsadat letöltési kérelmét!</b></body></html>");
               StatusPane.thisinstance.syncMessage.setToolTipText("Kérem, kattintson a Szerviz\\NAV Törzsadatok szinkronizálása menüre!");
            }

         }
      });
   }

   public static boolean isTraceEnabled() {
      return trace_enabled;
   }

   private static void setUpTracing() {
      try {
         Logger var0 = Logger.getLogger("");
         Handler[] var1 = var0.getHandlers();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Handler var4 = var1[var3];
            var0.removeHandler(var4);
         }

         String var6 = System.getProperty("anyk.trace");
         if (var6 != null) {
            TraceConfig var7 = new TraceConfig(var6);
            TraceHandler var8 = new TraceHandler(var7);
            var0.addHandler(var8);
            trace_enabled = !var7.getTracedClassesFQN().isEmpty();
         }
      } catch (Exception var5) {
         System.err.println("trace disabled");
         trace_enabled = false;
         var5.printStackTrace(System.err);
      }

   }

   public static boolean isDebugMode() {
      return debug_info_enabled;
   }

   public static void deleteCalculationRunFile() {
      if (xmlCalculationRunFile != null) {
         try {
            if (xmlCalculationRunFileExists()) {
               xmlCalculationRunFile.delete();
            }
         } catch (Exception var1) {
            Tools.eLog(var1, 1);
         }

      }
   }

   public static void createXmlCalculationRunFile() {
      if (xmlCalculationRunFile != null) {
         try {
            if (!xmlCalculationRunFileExists()) {
               xmlCalculationRunFile.createNewFile();
            }
         } catch (Exception var1) {
            Tools.eLog(var1, 1);
         }

      }
   }

   public static boolean xmlCalculationRunFileExists() {
      if (xmlCalculationRunFile == null) {
         return false;
      } else {
         try {
            return xmlCalculationRunFile.exists();
         } catch (Exception var1) {
            return false;
         }
      }
   }

   private static void handleGitInit(String var0) {
      String var1 = OsFactory.getOsHandler().getEnvironmentVariable("UBEV_GIT_SERVER_ADDRESS");
      String var2 = OsFactory.getOsHandler().getEnvironmentVariable("UBEV_GIT_REPO_NEV");
      String var3 = OsFactory.getOsHandler().getEnvironmentVariable("UBEV_GIT_PROD_REPO_NEV");
      Properties var4 = new Properties();
      if (!"".equals(var1)) {
         var4.setProperty("SERVER_PROTOCOL", "http");
         var4.setProperty("SERVER_ADDRESS", var1);
         var4.setProperty("SERVER_URL", "http://" + var1 + ":8080/r/" + var2 + ".git");
         var4.setProperty("SERVER_PORT", "8080");
         var4.setProperty("CONTENT_TYPE_ID", "content-type");
         var4.setProperty("CONTENT_TYPE", "text/plain");
         var4.setProperty("GIT_SERVLET", "/?getUtolsoVerzio=list&tipus=all&repo=");
         if ("UAT".equalsIgnoreCase(var0)) {
            var4.setProperty("GIT_REPO_NEV", var2);
            var4.setProperty("GIT_LOCAL_DIR", OsFactory.getOsHandler().getEnvironmentVariable("UBEV_GIT_LOCAL_DIR"));
         } else if ("PROD".equalsIgnoreCase(var0)) {
            var4.setProperty("GIT_REPO_NEV", var3);
            var4.setProperty("GIT_LOCAL_DIR", OsFactory.getOsHandler().getEnvironmentVariable("UBEV_GIT_LOCAL_PROD_DIR"));
         }

         PropertyList.getInstance().set("prop.usr.git_init_data", var4);
      }

   }

   public static boolean isBiz17EditMode() {
      return biz17EditMode;
   }

   public static void setBiz17EditMode(boolean var0) {
      biz17EditMode = var0;
   }

   private static void removeLog4j(File var0) {
      try {
         String[] var1 = log4jFiles;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            String var4 = var1[var3];
            File var5 = new File(var0, var4);
            removeFile(var5);
         }
      } catch (Exception var6) {
         Tools.eLog(var6, 0);
      }

   }

   public static boolean removeFile(File var0) {
      try {
         return var0.delete();
      } catch (Exception var2) {
         return false;
      }
   }

   public static boolean isModGui() {
      return modGui;
   }

   private static void initGuiRedraw() {
      GuiUtil.getScreenResolutions();

      try {
         SettingsStore var0 = SettingsStore.getInstance();
         String var1 = var0.get("gui", "LookAndFeelClassName");
         String var2 = var0.get("gui", "anykFontSize");
         if (var2 != null) {
            if (!"".equals(var2)) {
               modGui = true;
            }
         } else {
            saveOriginalGuiSettings();
            var2 = "12";
         }

         boolean var3 = true;

         int var15;
         try {
            var15 = Integer.parseInt(var2);
         } catch (Exception var10) {
            var15 = 12;
         }

         if (var1 != null) {
            LookAndFeelInfo[] var4 = UIManager.getInstalledLookAndFeels();
            LookAndFeelInfo[] var5 = var4;
            int var6 = var4.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               LookAndFeelInfo var8 = var5[var7];
               if (var1.equals(var8.getClassName())) {
                  UIManager.setLookAndFeel(var1);
               }
            }
         }

         Iterator var16 = UIManager.getDefaults().entrySet().iterator();

         while(true) {
            while(var16.hasNext()) {
               Entry var17 = (Entry)var16.next();
               Object var18 = var17.getKey();
               Object var19 = UIManager.get(var18);
               if (var19 != null && var19 instanceof FontUIResource) {
                  FontUIResource var21 = (FontUIResource)var19;
                  FontUIResource var22 = new FontUIResource(var21.getFamily(), var21.getStyle(), var15);
                  UIManager.put(var18, var22);
               } else if (var19 != null && var19 instanceof Font) {
                  Font var20 = (Font)var19;
                  Font var9 = new Font(var20.getFamily(), var20.getStyle(), var15);
                  UIManager.put(var18, var9);
               }
            }

            UIManager.put("CheckBox.rollover", Boolean.TRUE);
            break;
         }
      } catch (Exception var11) {
         System.out.println("LookAndFeel beállítás sikertelen: " + var11.toString());
      }

      JLabel var12 = new JLabel();
      Font var13 = var12.getFont().deriveFont(0);
      PropertyList.getInstance().set("prop.gui.font", var13);
      PropertyList.getInstance().set("prop.gui.font.size", var13.getSize());
      int var14 = var12.getFontMetrics(var13).getHeight();
      PropertyList.getInstance().set("prop.gui.item.height", var14);
      UIManager.getLookAndFeelDefaults().put("Menu.arrowIcon", ENYKIconSet.getInstance().get("page_egy_lapozas_jobbra"));
      GuiUtil.setDummyComp();
   }

   private static void addResetFontSize() {
      String var0 = "ResetFontSize";
      thisinstance.getRootPane().getInputMap(2).put(KeyStroke.getKeyStroke(70, 2), var0);
      thisinstance.getRootPane().getActionMap().put(var0, new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  if (JOptionPane.showConfirmDialog(MainFrame.thisinstance, "A betűméretet visszaállítjuk az eredeti 12-re.\nAz alkalmazásához újra kell indítani az ÁNYK-t\n\n Visszaállítsuk?", "Figyelmeztetés", 0) == 0) {
                     SettingsStore var1 = SettingsStore.getInstance();
                     if (var1.get("original_filepanel_new") != null) {
                        var1.set("filepanel_new_settings", var1.get("original_filepanel_new"));
                     }

                     if (var1.get("original_filepanel_open") != null) {
                        var1.set("filepanel_open_settings", var1.get("original_filepanel_open"));
                     }

                     if (var1.get("original_gui") != null) {
                        var1.set("gui", var1.get("original_gui"));
                     }

                     var1.set("gui", "anykFontSize", "12");
                  }

               }
            });
         }
      });
   }

   private static void saveOriginalGuiSettings() {
      SettingsStore var0 = SettingsStore.getInstance();
      Hashtable var1 = new Hashtable();
      Hashtable var2 = new Hashtable();
      Hashtable var3 = new Hashtable();
      Enumeration var4 = var0.get("gui").keys();

      String var5;
      while(var4.hasMoreElements()) {
         var5 = (String)var4.nextElement();
         var1.put(var5, var0.get("gui").get(var5));
      }

      if (!var1.containsKey("x")) {
         var1.put("x", "180");
      }

      if (!var1.containsKey("y")) {
         var1.put("y", "100");
      }

      if (!var1.containsKey("w")) {
         var1.put("w", "800");
      }

      if (!var1.containsKey("h")) {
         var1.put("h", "600");
      }

      if (var0.get("filepanel_new_settings") != null) {
         var4 = var0.get("filepanel_new_settings").keys();

         while(var4.hasMoreElements()) {
            var5 = (String)var4.nextElement();
            var2.put(var5, var0.get("filepanel_new_settings").get(var5));
         }
      } else {
         var2.put("xPos", "180");
         var2.put("yPos", "100");
         var2.put("width", "900");
         var2.put("height", "600");
      }

      if (var0.get("filepanel_open_settings") != null) {
         var4 = var0.get("filepanel_open_settings").keys();

         while(var4.hasMoreElements()) {
            var5 = (String)var4.nextElement();
            var3.put(var5, var0.get("filepanel_open_settings").get(var5));
         }
      } else {
         var3.put("xPos", "180");
         var3.put("yPos", "100");
         var3.put("width", "900");
         var3.put("height", "600");
      }

      var0.set("original_gui", var1);
      var0.set("original_filepanel_new", var2);
      var0.set("original_filepanel_open", var3);
   }

   private void saveBlXml(String var1) {
      try {
         File var2 = new File(Directories.getSettingsPath() + File.separator + "blacklist.xml");
         BufferedWriter var3 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var2), "UTF-8"));
         var3.write(var1);
         var3.close();
      } catch (Exception var4) {
         System.out.println("ÁNYK kitiltott nyomtatványok mentése sikertelen.");
         ErrorList.getInstance().writeError("MainFrame", "ÁNYK kitiltott nyomtatványok mentése sikertelen.", var4, (Object)null);
      }

   }

   private static void setAllDisplayDimension() {
      try {
         GraphicsEnvironment var0 = GraphicsEnvironment.getLocalGraphicsEnvironment();
         GraphicsDevice[] var1 = var0.getScreenDevices();
         double var2 = 0.0D;
         double var4 = 0.0D;
         double var6 = 0.0D;
         double var8 = 0.0D;

         for(int var10 = 0; var10 < var1.length; ++var10) {
            Rectangle var11 = var1[var10].getDefaultConfiguration().getBounds();
            if (var11.getX() < var2) {
               var2 = var11.getX();
            }

            if (var11.getX() + var11.getWidth() > var4) {
               var4 = var11.getX() + var11.getWidth();
            }

            if (var11.getY() < var6) {
               var6 = var11.getY();
            }

            if (var11.getY() + var11.getHeight() > var8) {
               var8 = var11.getY() + var11.getHeight();
            }
         }

         allDisplayDimension = new Rectangle((int)var2, (int)var6, (int)var4, (int)var8);
      } catch (Exception var12) {
         System.out.println("kepernyo meret reset 1280x720 (" + var12.toString() + ")");
         allDisplayDimension = new Rectangle(0, 0, 1280, 720);
      }

   }

   public static boolean visible(int var0, int var1) {
      return allDisplayDimension.contains(var0, var1);
   }

   private static void handleJavaFxMessage() {
      if (!isKeyFileExsits("javafx")) {
         KauAuthMethod var0 = KauAuthMethods.getSelected();
         if (var0 == KauAuthMethod.KAU_ALL) {
            if (!JavaInfo.isJavaFxAvailable()) {
               System.out.println("Figyelem! A nem érhető el a browseres azonosítás, válasszon más módot!");
               final JDialog var1 = new JDialog(thisinstance, "Figyelem!", true);
               final JCheckBox var2 = GuiUtil.getANYKCheckBox("Ez a figyelmeztetés többet NE jelenjen meg!");
               int var3 = GuiUtil.getW(var2, var2.getText()) + 20;
               var2.setAlignmentX(0.5F);
               var2.setSize(var3, GuiUtil.getCommonItemHeight() + 2);
               var2.setPreferredSize(var2.getSize());
               int var4 = Math.max((int)(0.6D * (double)GuiUtil.getScreenW()), var3);
               var4 = Math.min(GuiUtil.getW("WWWA 2025. április 15-én kiadott Java 1.8.451 verziótól az ÁNYK-ban nem használható a KAÜ portál használatával felhasználói azonosítás lehetőség.WWW"), var4);
               byte var5 = 9;
               if (GuiUtil.getCommonFontSize() > 32) {
                  var5 = 12;
               }

               JEditorPane var6 = new JEditorPane("text/html", getJFXInfoText());
               var6.setBackground(var2.getBackground());
               var6.setEditable(false);
               var6.setAlignmentX(0.0F);
               if (GuiUtil.getCommonFontSize() > 32) {
                  var5 = 12;
               }

               var6.setSize(var3, var5 * GuiUtil.getCommonItemHeight());
               var6.setPreferredSize(var6.getSize());
               JScrollPane var7 = new JScrollPane(var6, 20, 30);
               JButton var8 = new JButton("Ok");
               var8.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent var1x) {
                     if (var2.isSelected()) {
                        MainFrame.createKeyFile("javafx");
                     }

                     var1.setVisible(false);
                     var1.dispose();
                  }
               });
               JPanel var9 = new JPanel(new BorderLayout());
               var7.setBorder(new EmptyBorder(0, 10, 0, 0));
               var9.add(var7, "Center");
               JPanel var10 = new JPanel(new BorderLayout());
               var10.add(var2, "Center");
               JPanel var11 = new JPanel();
               var11.setLayout(new BoxLayout(var11, 0));
               var11.add(Box.createGlue());
               var11.add(var8);
               var11.add(Box.createGlue());
               var10.add(var11, "South");
               var9.add(var10, "South");
               var1.getContentPane().add(var9);
               var1.setSize(var4, (var5 + 4) * GuiUtil.getCommonItemHeight());
               var1.setPreferredSize(var1.getSize());
               var1.setLocationRelativeTo(thisinstance);
               var1.setResizable(true);
               var1.setVisible(true);
            }
         }
      }
   }

   private static boolean isKeyFileExsits(String var0) {
      IPropertyList var1 = PropertyList.getInstance();
      File var2 = new File((new File((String)var1.get("prop.usr.root"), (String)var1.get("prop.usr.settings"))).getAbsolutePath(), var0 + ".info");
      return var2.exists();
   }

   private static void createKeyFile(String var0) {
      IPropertyList var1 = PropertyList.getInstance();
      File var2 = new File((new File((String)var1.get("prop.usr.root"), (String)var1.get("prop.usr.settings"))).getAbsolutePath(), var0 + ".info");

      try {
         FileOutputStream var3 = new FileOutputStream(var2);
         var3.write("".getBytes());
         var3.close();
      } catch (Exception var4) {
         Tools.eLog(var4, 0);
      }

   }

   private static String getJFXInfoText() {
      JCheckBox var0 = GuiUtil.getANYKCheckBox("aaa");
      Color var1 = var0.getBackground();
      String var2 = String.format("#%02x%02x%02x", var1.getRed(), var1.getGreen(), var1.getBlue());
      Color var3 = var0.getForeground();
      String var4 = String.format("#%02x%02x%02x", var3.getRed(), var3.getGreen(), var3.getBlue());
      boolean var5 = true;
      StringBuilder var6 = (new StringBuilder("<html><body bgcolor=\"" + var2 + "\" text=\"" + var4 + "\"><p style=\"font-family:")).append((new JLabel()).getFont().getFamily()).append("; font-size:").append(GuiUtil.getCommonFontSize()).append("; \"text-align: left").append("\">");
      if (var5) {
         var6.append("<b>");
      }

      var6.append("A 2025. április 15-én kiadott Java 1.8.451 verziótól az ÁNYK-ban nem használható a \"KAÜ portál használatával\" felhasználói azonosítási lehetőség.<br>").append("Kérjük, a Szerviz->Beállítások->Működés fülön válasszon a felsorolt azonosítási módok közül:<br>").append("- DÁP-mobilalkalmazással (KAÜ-azonosítással háttérben)<br>").append("- Ügyfélkapu+ hitelesítő alkalmazással (KAÜ-azonosítással háttérben)<br>").append("- Ügyfélkapu+ e-mailes kóddal (KAÜ-azonosítással háttérben).<br>").append("<br");
      if (var5) {
         var6.append("</b>");
      }

      var6.append("</p></body></html>");
      return var6.toString();
   }

   static {
      signcolor = Color.WHITE;
      fromubevframe = false;
      toubevframe_wassave = false;
      readonlymodefromubev = false;
      isPart = false;
      isPartOnlyMain = false;
      onyaCheckMode = false;
      biz17EditMode = false;
      ellvitamode = false;
      rogzitomode = false;
      xml_loaded_dialog = false;
      javithatosagLekerdezve = false;
      optionstr1 = "Folytatja";
      optionstr2 = "Mégsem";
      publicstr1 = "Nyilvános módban indította a programot.";
      publicstr2 = "A program indításakor és kilépéskor törlődni fognak a felhasználói könyvtárakba mentett adatok!";
      publictitle = "Nyilvános üzemmód";
      playendstr = "Lejátszás végetért!";
      playendtitle = "Üzenet";
      updateerrstr1 = "Tükrözési hiba: ";
      updateerrstr2 = "Batch frissítési hiba: ";
      updateerrstr3 = "Batch frissítési hiba: ";
      FTRmode = false;
      recalc_in_progress = false;
      trace_enabled = false;
      debug_info_enabled = false;
      modGui = false;
      log4jFiles = new String[]{"log4j-1.2.16.jar", "log4j-api-2.16.0.jar", "log4j-core-2.16.0.jar"};
      allDisplayDimension = new Rectangle(0, 0, 400, 400);
      setUpTracing();
   }

   class EnykGlassPane extends JPanel implements MouseListener, MouseMotionListener, FocusListener {
      JProgressBar pb;

      public EnykGlassPane() {
         this.setLayout(new BorderLayout());
         this.setOpaque(false);
         MainFrame.this.glasslabel = new JLabel();
         MainFrame.this.glasslabel.setHorizontalAlignment(0);
         MainFrame.this.glasslabel.setVerticalAlignment(3);
         int var2 = Math.max(GuiUtil.getCommonFontSize(), 20);
         MainFrame.this.glasslabel.setFont(MainFrame.this.glasslabel.getFont().deriveFont(var2));
         this.add(MainFrame.this.glasslabel);
         JPanel var3 = new JPanel();
         var3.setOpaque(false);
         this.pb = new JProgressBar();
         this.pb.setIndeterminate(true);
         this.pb.setSize(new Dimension(90, GuiUtil.getCommonItemHeight()));
         var3.setPreferredSize(new Dimension(1, 100));
         var3.add(this.pb);
         this.add(var3, "South");
         this.addMouseListener(this);
         this.addMouseMotionListener(this);
         this.addFocusListener(this);
      }

      public void setVisible(boolean var1) {
         if (var1) {
            this.requestFocus();
         }

         super.setVisible(var1);
      }

      public void focusLost(FocusEvent var1) {
         if (this.isVisible()) {
            this.requestFocus();
         }

      }

      public void focusGained(FocusEvent var1) {
      }

      public void mouseDragged(MouseEvent var1) {
      }

      public void mouseMoved(MouseEvent var1) {
      }

      public void mouseClicked(MouseEvent var1) {
      }

      public void mouseEntered(MouseEvent var1) {
      }

      public void mouseExited(MouseEvent var1) {
      }

      public void mousePressed(MouseEvent var1) {
      }

      public void mouseReleased(MouseEvent var1) {
      }
   }
}
