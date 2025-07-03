package hu.piller.enykp.util.base;

import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.IOsHandler;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.oshandler.OsFactory;
import me.necrocore.abevjava.NecroFile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class InitApplication {
   private boolean debug = true;
   char NL = '\n';
   private static final String MAIN_PROPERTY_FILE = "properties.enyk";
   private static final Integer ERR_ID_10000 = new Integer(10000);
   private static final String ERR_MSG_10000 = "Hiba történt a paraméterek felolvasása közben";
   private static final String CONST_PREFIX = "prop.const.";
   private static final String DYN_PREFIX = "prop.dynamic.";
   private static final String CHAR_SET_UTF_8 = "UTF-8";
   private static final String CHAR_SET_ISO_8859_1 = "ISO-8859-1";
   private static final String CHAR_SET_ISO_8859_2 = "ISO-8859-2";
   private static final String PROP_SEP = "=";
   private static final String COMMENT = "#";
   public static final String KRDIR = "KRDIR";
   public static final String DEFAULT_KRSUBDIR = "eKuldes";
   public static final String USER_PROFILE_FILE = "profabevjava";
   public static final String SYS_ROOT_FILE = "abevjavapath.cfg";
   public static final String SYS_ROOT_KEY = "abevjava.path";
   private static String[] envVariables = new String[]{"KRDIR"};
   public static final String KRDIRMSG = "Megjelölés elektronikus beküldésre, digitális aláírásra";
   private static String[] envVariablesMissing = new String[]{"Megjelölés elektronikus beküldésre, digitális aláírásra"};
   public static final String envVariablesPrefix = "prop.usr";
   private static InitApplication instance;
   private String cmdParameterFileName = "cfg.enyk";
   private File userOptionFile;
   private static String[] args;
   public static final String ARG_TRUE = "true";
   public static final String ARG_IGEN = "igen";
   public static final String ARG_SEPARATOR = "=";
   public static final String ARG_DEBUG = "debug";
   public static final String ARG_PUBLIC_MODE = "publicmode";
   public static final String ARG_ABEVLOG_MODE = "abevlog";
   public static final String ARG_RECORD_MODE = "record";
   public static final String ARG_PLAYBACK_MODE = "playback";
   public static final String ARG_CFG = "cfg";
   public static final String ARG_USER_OPTION_FILE = "useroptionfile";
   public static final String ABEVJAVA_FAJL_KIT_TARS_SCRIPT_XKR = "gen_abevjava_fajlkittars.vbs";
   public static final String ABEVJAVA_FAJL_KIT_TARS_SCRIPT_USER_XKR = "gen_abevjava_fajlkittars_user.vbs";
   public static final String ABEVJAVA_FAJL_KIT_TARS_ANCHOR_FILE_XKR = "xkr.info";
   public static final String ABEVJAVA_FAJL_KIT_TARS_SCRIPT_XCZ = "gen_abevjava_fajlkittars_xcz.vbs";
   public static final String ABEVJAVA_FAJL_KIT_TARS_SCRIPT_USER_XCZ = "gen_abevjava_fajlkittars_xcz_user.vbs";
   public static final String ABEVJAVA_FAJL_KIT_TARS_ANCHOR_FILE_XCZ = "xcz.info";
   public static final String ABEVJAVA_FAJL_CSAT_OSZLOP_ANCHOR_FILE = "csat_oszlop.info";
   public static final String ABEVJAVA_DEPRECATED_ENVIRONMENT_ANCHOR_FILE = "deprecated.info";
   public static final String ABEVJAVA_PUBLIC_MODE_ANCHOR_FILE = "anyk_nyilvanos_uzemmod.xml";
   public static final String ABEVJAVA_START_IMPORT_BAT_FILE = "abevjava_start_import.bat";
   public static final String ABEVJAVA_START_IMPORT_VBS_FILE = "gen_abevjava_import.vbs";
   public static final String ABEVJAVA_START_IMPORT_MAIN_JAR = "abevjava.jar";
   public static final String DEP_MESSAGE_LATER = "A figyelmeztetés jelenjen meg a későbbiekben is.";
   public static final String DEP_MESSAGE_UNDERSTOOD = "Tudomásul vettem, ne jelenjen meg többet ez a figyelmeztetés.";
   public static final String MSG_WARNING = "Figyelmeztetés";
   public static final String MSG_INFORMATION = "Tájékoztatás";
   public static final String SYSTEM_PROPERTY_FILE_ENCODING = "file.encoding";
   private String appRoot;
   private Logger log = Logger.getLogger(InitApplication.class.getName());
   public static final String[] KRDIRSUBDIRS = new String[]{"KR/digitalis_alairas", "KR/elkuldott", "KR/kuldendo", "KR/letoltott"};

   public String getAppRoot() {
      return this.appRoot;
   }

   public void setCmdParameterFileName(String var1) {
      if (var1 != null) {
         this.cmdParameterFileName = var1;
      }

   }

   public static InitApplication getInstance(String[] var0) {
      if (instance == null) {
         instance = new InitApplication(var0);
      }

      return instance;
   }

   private InitApplication(String[] var1) {
      args = var1;
      this.getProperties();
   }

   private synchronized void getProperties() {
      System.out.println("Operációs rendszer = " + IOsHandler.OS_NAME + "," + IOsHandler.OS_PATCH);
      System.out.println("Java verzió = " + System.getProperty("java.version"));
      System.out.println("abevjava 3.44.0-02");
      System.out.println("file.encoding = " + System.getProperty("file.encoding"));
      IOsHandler var2 = OsFactory.getOsHandler();
      IPropertyList var3 = PropertyList.getInstance();
      this.checkEnvironment();
      this.publicArgs(args, var3, "=", "debug", Boolean.FALSE);
      this.publicArgs(args, var3, "=", "publicmode", Boolean.FALSE);
      this.publicArgs(args, var3, "=", "abevlog", Boolean.FALSE);
      this.publicArgs(args, var3, "=", "record", Boolean.FALSE);
      this.publicArgs(args, var3, "=", "playback", Boolean.FALSE);
      this.publicArgs(args, var3, "=", "cfg", this.cmdParameterFileName);
      this.publicArgs(args, var3, "=", "useroptionfile", "");
      URI var4 = null;

      try {
         var4 = new URI(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
      } catch (URISyntaxException var9) {
         var9.printStackTrace();
         GuiUtil.showMessageDialog((Component)null, "A telepítési könyvtár meghatározása sikertelen" + this.NL + var9.getMessage() + this.NL + "Ellenőrizze a jogosultságokat, futtassa újra a telepítőt.", "Hiba", 0);
         System.exit(-1);
      }

      this.appRoot = var4.getPath();
      this.appRoot = (new NecroFile(this.appRoot)).getParent();
      this.setCmdParameterFileName((String)var3.get("prop.dynamic.cfg"));
      File var1 = new NecroFile(this.appRoot, this.cmdParameterFileName);
      this.checkExists(var1);
      this.loadConfiguration(var1.getAbsolutePath(), var3);
      var1 = new NecroFile(this.appRoot, "properties.enyk");
      this.loadExistingProperties(var1, var3, this.appRoot);
      this.setAppInfo(var3);
      this.populateCommandArgs(args);
      this.userOptionFile = this.getUserOptionFile(var3, var2);
      System.out.println("userOptionFile.getAbsolutePath() = " + this.userOptionFile.getAbsolutePath());
      if (!this.userOptionFile.exists()) {
         System.out.println("userOptionFile not exists");
         this.createUserPropertyFile(this.userOptionFile, var2);
      }

      this.loadProperties(this.userOptionFile, var3);
      System.out.println("prop.usr.root  = " + var3.get("prop.usr.root"));
      this.createUserSubDirs(var3);
      boolean var5 = this.checkParameterKeys(var3, "prop.usr.primaryaccounts", "torzsadatok");
      var5 = this.checkParameterKeys(var3, "prop.usr.naplo", "naplo") && var5;
      var5 = this.checkParameterKeys(var3, "prop.usr.frissitesek", "frissitesek") && var5;
      if (!var5) {
         this.reWriteUserPropertyFile(this.userOptionFile, var3);
      }

      this.getEnvironmentVariables(var3);
      this.createKRDIRenvVariable(var3, var2);
      this.createKRDIRSubdirectories(var3);
      System.out.println("KRDIR = " + var3.get("prop.usr.krdir"));
      this.log.info("Start InitApplication Trace");
      this.log.info("IATrace - createAppParamFile - start");
      this.createAppParamFile(var2);
      this.log.info("IATrace - createAppParamFile - ok");
      File var6 = new NecroFile((new NecroFile((String)var3.get("prop.usr.root"), (String)var3.get("prop.usr.settings"))).getAbsolutePath(), "settings.enyk");
      if (!var6.exists()) {
         this.saveDefaultSettings(var6);
      }

      SettingsStore var7 = SettingsStore.getInstance();
      if (var7.list() < 5) {
         this.saveDefaultSettings(var6);
         var7.load();
      }

      this.log.info("IATrace - initialize Calculator - start");
      Calculator.getInstance().initialize(var3);
      this.log.info("IATrace - initialize Calculator - ok");
      this.log.info("IATrace - deleteUpgradeFile - start");
      this.deleteUpgradeFile(var3);
      this.log.info("IATrace - deleteUpgradeFile - ok");
      this.log.info("IATrace - doExtAssoc - start");
      String var8 = this.getOpMode();
      if (var8 == null || var8.equals("0")) {
         this.doExtAssoc("gen_abevjava_fajlkittars.vbs", "xkr.info", "gen_abevjava_fajlkittars_user.vbs");
         this.doExtAssoc("gen_abevjava_fajlkittars_xcz.vbs", "xcz.info", "gen_abevjava_fajlkittars_xcz_user.vbs");
      }

      this.log.info("IATrace - doExtAssoc - ok");
      this.log.info("IATrace - doCsatOszlopBeallit - start");
      this.doCsatOszlopBeallit();
      this.log.info("IATrace - doCsatOszlopBeallit - ok");
      this.log.info("IATrace - setAdditionalProperties - start");
      this.setAdditionalProperties(var3);
      this.log.info("IATrace - setAdditionalProperties - ok");
      this.log.info("IATrace - setPublicModeProperties - start");
      this.setPublicModeProperties(var3);
      this.log.info("IATrace - setPublicModeProperties - ok");
   }

   private void deleteUpgradeFile(IPropertyList var1) {
      try {
         File var2 = new NecroFile(new NecroFile((String)var1.get("prop.sys.root"), "upgrade"), "abevjava_install.jar");
         if (var2.exists()) {
            System.out.println("Upgrade állomány törlése: " + var2.getAbsolutePath() + ", " + (var2.delete() ? "sikeres" : "sikertelen"));
         }
      } catch (Exception var3) {
         System.out.println("Hiba az upgrade állomány ellenőrzése során.");
      }

   }

   private String getOpMode() {
      Object var1 = PropertyList.getInstance().get("prop.const.opmode");
      return var1 != null ? (String)((Vector)var1).get(0) : null;
   }

   public void doCsatOszlopBeallit() {
      IPropertyList var1 = PropertyList.getInstance();
      File var2 = new NecroFile((new NecroFile((String)var1.get("prop.usr.root"), (String)var1.get("prop.usr.settings"))).getAbsolutePath(), "csat_oszlop.info");
      if (!var2.exists()) {
         try {
            GuiUtil.check_csatolmany_oszlop();
            var2.createNewFile();
         } catch (Exception var4) {
            var4.printStackTrace();
         }

      }
   }

   public void doExtAssoc(String var1, String var2, String var3) {
      IOsHandler var4 = OsFactory.getOsHandler();
      IPropertyList var5 = PropertyList.getInstance();
      if (var4.getOsPrefix().equals("win")) {
         this.createImportScript();
         String var6 = var1;
         String var7 = var2;
         if (IOsHandler.OS_NAME.indexOf("windows") != -1 && (IOsHandler.OS_NAME.indexOf("vista") != -1 || IOsHandler.OS_NAME.indexOf(" 7") != -1 || IOsHandler.OS_NAME.indexOf(" 8") != -1 || IOsHandler.OS_NAME.indexOf("10") != -1 || IOsHandler.OS_NAME.indexOf("11") != -1 || IOsHandler.OS_NAME.indexOf("xp") != -1)) {
            var6 = var3;
            var7 = System.getProperty("user.name") + "_" + var2;
         }

         File var8 = new NecroFile((new NecroFile((String)var5.get("prop.usr.root"), (String)var5.get("prop.usr.settings"))).getAbsolutePath(), var2);
         if (var8.exists()) {
            return;
         }

         var8 = new NecroFile((new NecroFile((String)var5.get("prop.usr.root"), (String)var5.get("prop.usr.settings"))).getAbsolutePath(), var7);
         if (var8.exists()) {
            return;
         }

         try {
            File var9 = null;

            try {
               String var10 = (String)PropertyList.getInstance().get("prop.sys.root");
               var9 = new NecroFile(var10);
               if (!var9.exists()) {
                  throw new Exception();
               }
            } catch (Exception var11) {
               this.log(var11);
            }

            if (var9 != null) {
               var4.execute(var6, (String[])null, var9);
            }

            var8.createNewFile();
         } catch (Exception var12) {
            var12.printStackTrace();
         }
      }

   }

   private void createImportScript() {
      File var1 = new NecroFile((String)PropertyList.getInstance().get("prop.sys.root"));
      if (var1.exists()) {
         File var2 = new NecroFile(var1, "abevjava_start_import.bat");
         if (!var2.exists()) {
            File var3 = new NecroFile(var1, "gen_abevjava_import.vbs");
            if (var3.exists()) {
               File var4 = new NecroFile(var1, "abevjava.jar");
               if (var4.exists()) {
                  String[] var5 = new String[]{"@cscript \"" + var3.getAbsolutePath().replaceAll("\\\\", "/") + "\" %1 \"" + var4.getAbsolutePath().replaceAll("\\\\", "/") + "\""};
                  if (!this.writeFile(var2, var5)) {
                     System.out.println("A " + var3.getAbsolutePath() + " állomány létrehozása sikertelen.");
                  }
               }
            }
         }
      }

   }

   private boolean writeFile(File var1, String[] var2) {
      PrintWriter var3 = null;

      try {
         var3 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var1), "ISO-8859-2")));

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3.println(var2[var4]);
         }

         var3.flush();
         var3.close();
         return true;
      } catch (Exception var5) {
         if (var3 != null) {
            var3.close();
         }

         return false;
      }
   }

   private File getUserOptionFile(IPropertyList var1, IOsHandler var2) {
      String var3 = (String)var1.get("prop.dynamic.useroptionfile");
      File var4;
      if (var3 != null && var3.length() != 0) {
         var4 = new NecroFile(var3);
         if (var4.exists() && var4.isFile()) {
            return var4;
         } else {
            GuiUtil.showMessageDialog((Component)null, "Hibás paraméterezés az alábbi felhasználói paraméter állomány nem létezik: " + this.NL + var3, "Hiba", 0);
            System.exit(-1);
            return null;
         }
      } else {
         var4 = new NecroFile(var2.getUserHomeDir(), ".abevjava");
         if (!var4.exists()) {
            var4.mkdirs();
         }

         return new NecroFile(var4.getAbsolutePath(), System.getProperty("user.name") + ".enyk");
      }
   }

   private void setAppInfo(IPropertyList var1) {
      Hashtable var2 = new Hashtable();
      var2.put("ver", "3.44.0");
      var2.put("org", "APEH");
      var2.put("name", "AbevJava Keretrendszer");
      var2.put("id", "abevjava");
      var1.set("APPLICATION_INFO", var2);
   }

   private void saveDefaultSettings(File var1) {
      try {
         var1.delete();
      } catch (Exception var7) {
         this.log(var7);
      }

      Properties var2 = new Properties();
      var2.put("gui.mezőkódkijelzés", "false");
      var2.put("gui.mezőszámítás", "true");
      var2.put("printer.print.settings.colors", "12");
      var2.put("file_maszk.file_maszk", "nyomtatvány azonosító#adószám vagy adóazonosító jel#név (cégnév vagy személynév)#");
      var2.put("gui.felülírásmód", "true");
      var2.put("gui.mezőellenőrzés", "true");
      var2.put("printer.print.settings.kozos", "0");
      var2.put("printer.print.settings.margin", "2");
      var2.put("file_maszk.file_maszk_hasznalva", "true");
      FileOutputStream var3 = null;

      try {
         var3 = new FileOutputStream(var1);
         var2.store(var3, "user properties");
         var3.close();
      } catch (IOException var8) {
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var6) {
               this.log(var8);
            }
         }

         var8.printStackTrace();
      }

   }

   private boolean checkParameterKeys(IPropertyList var1, String var2, String var3) {
      String var4 = (String)var1.get(var2);
      if (var4 != null && !var4.trim().equalsIgnoreCase(var3.trim())) {
         return true;
      } else {
         File var5 = new NecroFile((String)var1.get("prop.usr.root"), var3);
         var1.set(var2, var5.getAbsolutePath());
         if (!var5.exists()) {
            var5.mkdirs();
         }

         return false;
      }
   }

   private void reWriteUserPropertyFile(File var1, IPropertyList var2) {
      Properties var3 = new Properties();
      var3.put("prop.usr.saves", var2.get("prop.usr.saves"));
      var3.put("prop.usr.archive", var2.get("prop.usr.archive"));
      var3.put("prop.usr.import", var2.get("prop.usr.import"));
      var3.put("prop.usr.settings", var2.get("prop.usr.settings"));
      var3.put("prop.usr.kontroll", var2.get("prop.usr.kontroll"));
      var3.put("prop.usr.attachment", var2.get("prop.usr.attachment"));
      var3.put("prop.usr.tmp", var2.get("prop.usr.tmp"));
      var3.put("prop.usr.naplo", var2.get("prop.usr.naplo"));
      var3.put("prop.usr.frissitesek", var2.get("prop.usr.frissitesek"));
      var3.put("prop.usr.tmp_xml", var2.get("prop.usr.tmp_xml"));
      var3.put("prop.usr.tmp_calc", var2.get("prop.usr.tmp_calc"));
      var3.put("prop.usr.ds_src", var2.get("prop.usr.ds_src"));
      var3.put("prop.usr.ds_dest", var2.get("prop.usr.ds_dest"));
      var3.put("prop.usr.ds_sent", var2.get("prop.usr.ds_sent"));
      var3.put("prop.usr.root", var2.get("prop.usr.root"));
      var3.put("prop.usr.primaryaccounts", var2.get("prop.usr.primaryaccounts"));
      this.writeUserParamFile(var1, var3);
   }

   private void createUserPropertyFile(File var1, IOsHandler var2) {
      if (!var1.exists()) {
         Properties var3 = this.getUserEtalon(var2);
         this.writeUserParamFile(var1, var3);
      }

   }

   private Properties getUserEtalon(IOsHandler var1) {
      Properties var2 = new Properties();
      var2.put("prop.usr.saves", "mentesek");
      var2.put("prop.usr.archive", "archivum");
      var2.put("prop.usr.import", "import");
      var2.put("prop.usr.settings", "beallitasok");
      var2.put("prop.usr.kontroll", "kontroll");
      var2.put("prop.usr.attachment", "csatolmanyok");
      var2.put("prop.usr.tmp", "tmp");
      var2.put("prop.usr.naplo", "naplo");
      var2.put("prop.usr.frissitesek", "frissitesek");
      var2.put("prop.usr.tmp_xml", "xml");
      var2.put("prop.usr.tmp_calc", "calc");
      var2.put("prop.usr.ds_src", "KR/digitalis_alairas");
      var2.put("prop.usr.ds_dest", "KR/kuldendo");
      var2.put("prop.usr.ds_sent", "KR/elkuldott");
      var2.put("prop.usr.root", (new NecroFile(var1.getUserHomeDir(), "abevjava")).getAbsolutePath());
      var2.put("prop.usr.primaryaccounts", (new NecroFile((String)var2.get("prop.usr.root"), "torzsadatok")).getAbsolutePath());
      return var2;
   }

   private void createUserSubDirs(IPropertyList var1) {
      File var2 = new NecroFile((String)var1.get("prop.usr.root"));
      System.out.println("usrRoot = " + var2.getAbsolutePath());
      if (!var2.exists() && !var2.mkdirs()) {
         GuiUtil.showMessageDialog((Component)null, "Az alábbi könyvtár létrehozása sikertelen" + this.NL + var2.getAbsolutePath() + this.NL + "Ellenőrizze a jogosultságokat, futtassa újra a telepítőt.", "Hiba", 0);
         System.exit(-1);
      }

      this.createDirs(var2, "mentesek");
      this.createDirs(var2, "archivum");
      this.createDirs(var2, "import");
      this.createDirs(var2, "beallitasok");
      this.createDirs(var2, "kontroll");
      this.createDirs(var2, "csatolmanyok");
      this.createDirs(var2, "tmp");
      this.createDirs(var2, "tmp/xml");
      this.createDirs(var2, "tmp/calc");
      this.createDirs(var2, "torzsadatok");
      this.createDirs(var2, "naplo");
      this.createDirs(var2, "frissitesek");
   }

   private void createDirs(File var1, String var2) {
      File var3 = new NecroFile(var1, var2);
      if (!var3.exists()) {
         var3.mkdirs();
      }
   }

   private void checkExists(File var1) {
      if (!var1.exists() || !var1.isFile()) {
         GuiUtil.showMessageDialog((Component)null, "Hiányzik az alábbi paraméter állomány:" + this.NL + var1.getAbsolutePath() + this.NL + "Futtassa újra a telepítőt.", "Hiba", 0);
         System.exit(-1);
      }
   }

   private void loadExistingProperties(File var1, IPropertyList var2, String var3) {
      if (var1.exists() && var1.isFile()) {
         this.loadProperties(var1, var2);
      } else {
         this.setAndView(var2, "prop.sys.root", var3);
         this.setAndView(var2, "prop.sys.helps", "file:///" + (new NecroFile(var3, "segitseg")).getAbsolutePath());
         this.setAndView(var2, "prop.sys.templates", "nyomtatvanyok");
         this.setAndView(var2, "prop.sys.abev", "abev");
         this.setAndView(var2, "prop.sys.kontroll", "kontroll");
      }

   }

   private void setAndView(IPropertyList var1, String var2, String var3) {
      var1.set(var2, var3);
      System.out.println(var2 + " = " + var3);
   }

   private void getEnvironmentVariables(IPropertyList var1) {
      IOsHandler var2 = OsFactory.getOsHandler();

      for(int var3 = 0; var3 < envVariables.length; ++var3) {
         String var4 = envVariables[var3];
         if (var4.length() != 0) {
            String var5 = "prop.usr." + var4.toLowerCase();
            String var6 = "";

            try {
               var6 = var2.getEnvironmentVariable(var4);
            } catch (Exception var8) {
               this.log(var8);
            }

            var1.set(var5, var6);
         }
      }

   }

   private void createKRDIRenvVariable(IPropertyList var1, IOsHandler var2) {
      String var3 = "prop.usr." + "KRDIR".toLowerCase();
      String var4 = (String)var1.get(var3);
      if (var4 == null || var4.length() == 0) {
         var4 = var2.getDirtyEnvironmentVariable("KRDIR", "profabevjava");
         if (var4 == null || var4.length() == 0) {
            String var5 = (String)var1.get("prop.usr.root");
            var4 = (new NecroFile(var5, "eKuldes")).getAbsolutePath();
            var2.createEnvironmentVariable(var5, "profabevjava", "KRDIR", var4);
         }

         var1.set(var3, var4);
      }

   }

   private void createKRDIRSubdirectories(IPropertyList var1) {
      String var2 = "prop.usr." + "KRDIR".toLowerCase();
      String var3 = (String)var1.get(var2);
      if (var3 != null && var3.length() != 0) {
         var3 = this.removeWhiteChars(var3);
         var1.set(var2, var3);
         File var4 = new NecroFile(var3);
         if (!var4.exists()) {
            var4.mkdirs();
         }

         if (var4.exists()) {
            for(int var5 = 0; var5 < KRDIRSUBDIRS.length; ++var5) {
               String var6 = KRDIRSUBDIRS[var5];
               this.chkAndCreate(new NecroFile(var4, var6));
            }
         } else {
            GuiUtil.showMessageDialog((Component)null, "Hiba történt az alábbi könyvtár létrehozása során (KRDIR környezeti változó): " + this.NL + var4 + this.NL + "A " + "Megjelölés elektronikus beküldésre, digitális aláírásra" + " funkció nem fog működni.", "Hiba", 0);
         }

      }
   }

   private String removeWhiteChars(String var1) {
      StringBuffer var2 = new StringBuffer();
      char[] var3 = var1.toCharArray();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         char var5 = var3[var4];
         if (var5 != '"') {
            var2.append(var5);
         }
      }

      return var2.toString();
   }

   private void chkAndCreate(File var1) {
      if (!var1.exists()) {
         var1.mkdirs();
      }

   }

   private void loadProperties(File var1, IPropertyList var2) {
      try {
         Properties var9 = this.getExistingMultilangDirProperties(var1, "prop.usr.root");
         Enumeration var3 = var9.propertyNames();

         while(var3.hasMoreElements()) {
            Object var4 = var3.nextElement();
            var2.set(var4, var9.get(var4));
         }
      } catch (Exception var8) {
         Exception var5 = var8;

         try {
            this.writeError(var5, var1);
         } catch (Exception var7) {
            this.log(var7);
         }
      }

   }

   private void loadConfiguration(String var1, IPropertyList var2) {
      Object var3 = null;
      System.out.println("cfg = " + var1);
      if (var1 != null && var1.length() != 0) {
         String var4 = var1.trim();

         try {
            try {
               File var5 = new NecroFile(var4);
               var3 = new FileInputStream(var5);
            } catch (Exception var10) {
               URI var7;
               try {
                  var7 = new URI(var4);
               } catch (URISyntaxException var9) {
                  var7 = (new NecroFile((new NecroFile(var4)).getCanonicalPath())).toURI();
               }

               if (var7.toString().startsWith("http:") || var7.toString().startsWith("file:")) {
                  URLConnection var8 = var7.toURL().openConnection();
                  var3 = var8.getInputStream();
               }
            }

            if (var3 != null) {
               this.load((InputStream)var3, var2, "UTF-8", "prop.const.", true);
               ((InputStream)var3).close();
            }
         } catch (Exception var11) {
            this.writeError(var11, var1);
         }

      }
   }

   private boolean load(InputStream var1, IPropertyList var2, String var3, String var4, boolean var5) {
      String var9 = String.valueOf(new char[]{'\uefbb', '뼀'});
      String var10 = String.valueOf('\ufeff');
      String var11 = String.valueOf('\ufffe');
      String var12 = String.valueOf(new char[]{'\u0000', '\ufeff'});
      String var13 = String.valueOf(new char[]{'\ufffe', '\u0000'});

      boolean var6;
      try {
         boolean var7 = true;
         var6 = true;
         BufferedReader var8 = new BufferedReader(new InputStreamReader(var1, var3));

         while(true) {
            String var14 = var8.readLine();
            if (var14 == null) {
               return var6;
            }

            if (var7) {
               if (var14.startsWith(var10)) {
                  var14 = var14.substring(var10.length());
               } else if (var14.startsWith(var11)) {
                  var14 = var14.substring(var11.length());
               } else if (var14.startsWith(var12)) {
                  var14 = var14.substring(var12.length());
               } else if (var14.startsWith(var13)) {
                  var14 = var14.substring(var13.length());
               } else if (var14.startsWith(var9)) {
                  var14 = var14.substring(var9.length());
               }

               var7 = false;
            }

            var14 = var14.trim();
            if (var14.length() > 0 && !var14.startsWith("#")) {
               String[] var15 = var14.split("=", 2);
               String[] var16 = new String[]{"", ""};
               System.arraycopy(var15, 0, var16, 0, var15.length);
               var16[0] = this.getValidObject(var16[0]).toString();
               var16[1] = this.getValidObject(var16[1]).toString();
               if (var5) {
                  this.put(var4 + var16[0], var16[1], var2);
               } else {
                  var2.set(var4 + var16[0], var16[1]);
               }
            }
         }
      } catch (Exception var17) {
         var6 = false;
         return var6;
      }
   }

   private void populateCommandArgs(String[] var1) {
      if (var1 != null) {
         int var5 = 0;

         for(int var6 = var1.length; var5 < var6; ++var5) {
            if (var1[var5] != null && var1[var5].startsWith("cmd:")) {
               String[] var7 = var1[var5].split(":", 2);
               if (var7.length > 1) {
                  String[] var8 = var7[1].split(" ", 2);

                  for(int var9 = 0; var9 < var8.length; ++var9) {
                     String var10 = var8[var9];
                     var8[var9] = var10.trim();
                  }

                  IPropertyList var11 = PropertyList.getInstance();
                  var11.set("prop.command", var8);
                  return;
               }
            }
         }
      }

   }

   private void createAppParamFile(IOsHandler var1) {
      try {
         this.log.info("IATrace - createAppParamFile - start");
         this.log.info("IATrace - removeUacAppParamFile - start");
         this.removeUacAppParamFile(var1);
         this.log.info("IATrace - removeUacAppParamFile - ok");
         this.log.info("IATrace - oh.getInitDir() - start");
         File var2 = new NecroFile(var1.getInitDir());
         this.log.info("IATrace - oh.getInitDir() - ok: " + (var2 == null ? "null" : var2.getAbsolutePath()));
         if (this.isParamExist(var2, "abevjavapath.cfg", "abevjava.path")) {
            this.log.info("IATrace - abevjava.path param exists return");
            return;
         }

         File var3 = new NecroFile(var1.getUserHomeDir(), ".abevjava");
         if (this.isParamExist(var3, "abevjavapath.cfg", "abevjava.path")) {
            this.log.info("IATrace - abevjava.path param exists return");
            return;
         }

         String var4 = (new NecroFile(this.appRoot)).getAbsolutePath();
         System.out.println("rootPath = " + var4);
         this.log.info("IATrace - createParamFile (windir) - start");
         this.createParamFile(var2, "abevjavapath.cfg", "abevjava.path", var4);
         this.log.info("IATrace - createParamFile - ok");
         this.log.info("IATrace - removeUacAppParamFile - start");
         this.removeUacAppParamFile(var1);
         this.log.info("IATrace - removeUacAppParamFile - ok");
         if (this.isParamExist(var2, "abevjavapath.cfg", "abevjava.path")) {
            return;
         }

         this.log.info("IATrace - createParamFile (User) - start");
         this.createParamFile(var3, "abevjavapath.cfg", "abevjava.path", var4);
         this.log.info("IATrace - createParamFile - ok");
      } catch (Exception var5) {
         this.log(var5);
      }

   }

   private void removeUacAppParamFile(IOsHandler var1) {
      try {
         if (this.isUacOpSys()) {
            this.log.info("IATrace - getUacInitDir - start");
            File var2 = this.getUacInitDir(var1);
            this.log.info("IATrace - getUacInitDir - ok");
            if (var2 == null) {
               return;
            }

            File var3 = new NecroFile(var2, "abevjavapath.cfg");
            if (var3.exists()) {
               if (var3.delete()) {
                  System.out.println(var3.getAbsolutePath() + " állomány sikeresen törölve.");
               } else {
                  System.out.println(var3.getAbsolutePath() + " állomány törlése sikertelen.");
               }
            }
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   private boolean isUacOpSys() {
      return IOsHandler.OS_NAME.indexOf("windows") != -1 && (IOsHandler.OS_NAME.indexOf("vista") != -1 || IOsHandler.OS_NAME.indexOf(" 7") != -1 || IOsHandler.OS_NAME.indexOf(" 8") != -1 || IOsHandler.OS_NAME.indexOf("10") != -1 || IOsHandler.OS_NAME.indexOf("11") != -1);
   }

   private boolean deprecatedOpSys() {
      return IOsHandler.OS_NAME.indexOf("windows") != -1 && (IOsHandler.OS_NAME.indexOf("98") != -1 || IOsHandler.OS_NAME.indexOf("me") != -1 || IOsHandler.OS_NAME.indexOf("nt") != -1 || IOsHandler.OS_NAME.indexOf("2000") != -1);
   }

   private File getUacInitDir(IOsHandler var1) {
      this.log.info("IATrace - getEnvironmentVariable - start");
      String var2 = var1.getEnvironmentVariable("LOCALAPPDATA");
      this.log.info("IATrace - getEnvironmentVariable - ok :" + var2);
      if (var2 != null && var2.length() > 0) {
         var2 = var2 + "\\VirtualStore\\Windows";
         File var3 = new NecroFile(var2);
         if (var3.exists() && var3.isDirectory()) {
            return var3;
         }
      }

      return null;
   }

   private boolean isParamExist(File var1, String var2, String var3) {
      BufferedReader var4 = null;

      try {
         String var5 = "";
         File var6 = new NecroFile(var1, var2);
         if (var6.exists()) {
            var4 = new BufferedReader(new FileReader(var6));
            var5 = var4.readLine();
            String[] var7 = var5.split("=");
            if (var7.length == 2 && var7[0].trim().equalsIgnoreCase(var3) && var7[1].trim().length() > 0) {
               var4.close();
               return true;
            }

            var4.close();
         }

         return false;
      } catch (Exception var9) {
         try {
            if (var4 != null) {
               var4.close();
            }
         } catch (IOException var8) {
            this.log(var8);
         }

         return false;
      }
   }

   private boolean createParamFile(File var1, String var2, String var3, String var4) {
      PrintWriter var5 = null;

      try {
         String var6 = var3 + " = " + var4.replaceAll("\\\\", "\\\\\\\\");
         File var7 = new NecroFile(var1, var2);
         var5 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var7), "UTF-8")));
         var5.println(var6);
         var5.flush();
         var5.close();
         return true;
      } catch (Exception var8) {
         if (var5 != null) {
            var5.close();
         }

         return false;
      }
   }

   private boolean writeUserParamFile(File var1, Properties var2) {
      PrintWriter var3 = null;

      try {
         var3 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var1), "ISO-8859-2")));
         Enumeration var5 = var2.keys();

         while(var5.hasMoreElements()) {
            String var6 = (String)var5.nextElement();
            String var4 = var6 + "=" + var2.get(var6);
            var3.println(var4);
         }

         var3.flush();
         var3.close();
         return true;
      } catch (Exception var7) {
         if (var3 != null) {
            var3.close();
         }

         return false;
      }
   }

   private void put(Object var1, Object var2, IPropertyList var3) {
      Object var4 = var3.get(var1);
      Vector var5;
      if (var4 == null) {
         var5 = new Vector(32, 16);
         var3.set(var1, var5);
      } else {
         var5 = (Vector)var4;
      }

      var5.add(var2);
   }

   private Object getValidObject(Object var1) {
      return var1 == null ? "" : var1;
   }

   private void writeError(Exception var1, Object var2) {
      ErrorList.getInstance().store(ERR_ID_10000, "Hiba történt a paraméterek felolvasása közben", var1, var2);
      if (this.debug) {
         System.out.println("Hiba történt a paraméterek felolvasása közben:" + var2);
      }

   }

   private void publicArgs(String[] var1, IPropertyList var2, String var3, String var4, Boolean var5) {
      String var6 = "prop.dynamic." + var4;
      String var7 = this.getValueFromStringArrayByKey(var1, var3, var4);
      if (var7 != null && var7.length() > 0) {
         var2.set(var6, new Boolean(var7.equalsIgnoreCase("true") || var7.equalsIgnoreCase("igen")));
      } else {
         var2.set(var6, var5);
      }

   }

   private void publicArgs(String[] var1, IPropertyList var2, String var3, String var4, String var5) {
      String var6 = "prop.dynamic." + var4;
      String var7 = this.getValueFromStringArrayByKey(var1, var3, var4);
      if (var7 != null && var7.length() > 0) {
         var2.set(var6, var7);
      } else {
         var2.set(var6, var5);
      }

   }

   private String getValueFromStringArrayByKey(String[] var1, String var2, String var3) {
      try {
         if (var1 != null) {
            int var4 = 0;

            for(int var5 = var1.length; var4 < var5; ++var4) {
               if (var1[var4] != null && var1[var4].toUpperCase().startsWith((var3 + var2).toUpperCase())) {
                  String[] var6 = var1[var4].split(var2, 2);
                  if (var6.length > 1) {
                     return var6[1];
                  }
               }
            }
         }
      } catch (Exception var7) {
         this.log(var7);
      }

      return null;
   }

   private void checkEnvironment() {
      try {
         this.checkTotalMemory();
         this.checkJavaVersion();
      } catch (Exception var2) {
         GuiUtil.showMessageDialog((Component)null, var2.getMessage(), "Hiba", 0);
         System.exit(-1);
      }

   }

   private void checkDeprication(IPropertyList var1) {
      try {
         String var2 = this.checkDeprecatedOpSys();
         if (var2 == null) {
            var2 = this.checkSupportedJavaVersion();
         }

         if (var2 != null) {
            if (this.getActMonth() > 8) {
               GuiUtil.showMessageDialog((Component)null, var2, "Figyelmeztetés", 0);
            } else {
               File var3 = new NecroFile((new NecroFile((String)var1.get("prop.usr.root"), (String)var1.get("prop.usr.settings"))).getAbsolutePath(), "deprecated.info");
               if (var3.exists()) {
                  return;
               }

               Object var4 = GuiUtil.showInputDialog((Component)null, var2, "Tájékoztatás", 0, 0, (Icon)null, new String[]{"A figyelmeztetés jelenjen meg a későbbiekben is.", "Tudomásul vettem, ne jelenjen meg többet ez a figyelmeztetés."}, "A figyelmeztetés jelenjen meg a későbbiekben is.", new String[]{"Bezár"}, "Bezár");
               if (var4 != null && var4.equals("Tudomásul vettem, ne jelenjen meg többet ez a figyelmeztetés.")) {
                  var3.createNewFile();
               }
            }
         }
      } catch (Exception var5) {
         Tools.eLog(var5, 1);
      }

   }

   private int getActMonth() {
      GregorianCalendar var1 = new GregorianCalendar(new Locale("hu", "HU"));
      var1.setTimeInMillis(System.currentTimeMillis());
      return var1.get(2) + 1;
   }

   private void checkJavaVersion() throws Exception {
      Version var1 = new Version("1.6.0");
      String var2 = System.getProperty("java.version");
      Version var3 = new Version(var2.split("_")[0]);
      if (var1.compareTo(var3) > 0) {
         throw new Exception("A program futásához szükséges java verzió: " + var1.getNumstr() + this.NL + "A jelenlegi java verzió: " + var2 + this.NL + "Frissítse a java környezetet.");
      }
   }

   private void checkTotalMemory() throws Exception {
      long var1 = 128000000L;
      long var3 = Runtime.getRuntime().maxMemory();
      if (var1 >= var3) {
         throw new Exception("Kevés a rendelkezésre álló memória." + this.NL + "(Szükséges:" + var1 + " byte, Jelenlegi" + var3 + " byte)" + this.NL + "Az abevjava_start programmal indítsa az alkalmazást." + this.NL + "Ez a parancs állomány, tartalmazza a program futásához szükséges beállításokat.");
      }
   }

   private String checkDeprecatedOpSys() throws Exception {
      return this.deprecatedOpSys() ? "<html>Az Ön gépére telepített Java verzióval csak a 2015. október 1. előtti ÁNYK program verziók használhatóak. <br><br><center>Mivel az Ön által használt operációs rendszerre nem telepíthető a Java (JRE) 1.8-os verziója, <br>így a számítógépére a Microsoft Windows újabb verzióját <br>(Windows XP, Windows Vista, Windows 7,  Windows 8),<br>vagy bármely, a Java (JRE) 1.8 környezet futtatására alkalmas<br>egyéb operációs rendszert szükséges telepíteni (Linux, Mac OS X).<br><br></html>" : null;
   }

   private String checkSupportedJavaVersion() throws Exception {
      if (!this.isSupportedJavaVersion()) {
         Version var1 = new Version("1.8.0");
         String var2 = System.getProperty("java.version");
         return "<html><center>Az Ön gépére telepített Java verzióval csak a 2015. október 1. előtti ÁNYK program verziók használhatóak.<br>A program futásához szükséges minimális java verzió 2015. október 1. után: JRE " + var1.getNumstr() + "<br>" + "A jelenlegi java verzió: " + var2 + "<br>" + "Frissítse a java (JRE) környezetet." + "<br>" + "<br>" + "</html>";
      } else {
         return null;
      }
   }

   private boolean isSupportedJavaVersion() {
      Version var1 = new Version("1.8.0");
      String var2 = System.getProperty("java.version");
      Version var3 = new Version(var2.split("_")[0]);
      return var1.compareTo(var3) <= 0;
   }

   private Properties getExistingMultilangDirProperties(File var1, String var2) {
      Properties var3 = new Properties();

      try {
         Properties var4 = new Properties();

         try {
            var4.load(new FileInputStream(var1.getPath()));
         } catch (Exception var8) {
            this.log(var8);
         }

         Properties var5 = new Properties();
         this.loadPropertyFile(var1, var5, "UTF-8");
         Properties var6 = new Properties();
         this.loadPropertyFile(var1, var6, "ISO-8859-2");
         Properties var7 = new Properties();
         this.loadPropertyFile(var1, var7, "ISO-8859-1");
         var3 = this.getValidFilePathProp(var6, var4, var5, var7, var2);
         return var3;
      } catch (Exception var9) {
         return var3;
      }
   }

   private boolean loadPropertyFile(File var1, Properties var2, String var3) {
      BufferedReader var6 = null;
      String var7 = String.valueOf(new char[]{'\uefbb', '뼀'});
      String var8 = String.valueOf('\ufeff');
      String var9 = String.valueOf('\ufffe');
      String var10 = String.valueOf(new char[]{'\u0000', '\ufeff'});
      String var11 = String.valueOf(new char[]{'\ufffe', '\u0000'});

      boolean var4;
      try {
         boolean var5 = true;
         var4 = true;
         var6 = new BufferedReader(new InputStreamReader(new FileInputStream(var1.getPath()), var3));

         while(true) {
            String var12 = var6.readLine();
            if (var12 == null) {
               try {
                  var6.close();
               } catch (IOException var16) {
                  this.log(var16);
               }

               return var4;
            }

            if (var5) {
               if (var12.startsWith(var8)) {
                  var12 = var12.substring(var8.length());
               } else if (var12.startsWith(var9)) {
                  var12 = var12.substring(var9.length());
               } else if (var12.startsWith(var10)) {
                  var12 = var12.substring(var10.length());
               } else if (var12.startsWith(var11)) {
                  var12 = var12.substring(var11.length());
               } else if (var12.startsWith(var7)) {
                  var12 = var12.substring(var7.length());
               }

               var5 = false;
            }

            var12 = var12.trim();
            if (var12.length() > 0 && !var12.startsWith("#")) {
               String[] var13 = var12.split("=", 2);
               String[] var14 = new String[]{"", ""};
               System.arraycopy(var13, 0, var14, 0, var13.length);
               var14[0] = this.getValidObject(var14[0]).toString().trim();
               var14[1] = this.getValidObject(var14[1]).toString().trim();
               var2.put(var14[0], var14[1]);
            }
         }
      } catch (Exception var17) {
         try {
            var6.close();
         } catch (IOException var15) {
            this.log(var17);
         }

         var4 = false;
         return var4;
      }
   }

   private Properties getValidFilePathProp(Properties var1, Properties var2, Properties var3, Properties var4, String var5) {
      String var6 = (String)var1.get(var5);
      if (this.existsDir(var6)) {
         return var1;
      } else {
         String var7 = (String)var2.get(var5);
         if (this.existsDir(var7)) {
            return var2;
         } else {
            String var8 = (String)var3.get(var5);
            if (this.existsDir(var8)) {
               return var3;
            } else {
               String var9 = (String)var4.get(var5);
               return this.existsDir(var9) ? var4 : var1;
            }
         }
      }
   }

   private boolean existsDir(String var1) {
      if (var1 != null && var1.length() != 0) {
         File var2 = new NecroFile(var1);
         return var2.exists() && var2.isDirectory();
      } else {
         return false;
      }
   }

   private void setAdditionalProperties(IPropertyList var1) {
      File var2 = new NecroFile((String)var1.get("prop.sys.root"), (String)var1.get("prop.sys.templates"));
      var1.set("prop.dynamic.templates.absolutepath", var2.getAbsolutePath());
      System.out.println("Nyomtatvány sablonok teljes elérési útvonala = " + var2);
   }

   private void setPublicModeProperties(IPropertyList var1) {
      var1.set("prop.dynamic.public.mode", Boolean.FALSE);

      try {
         File var2 = new NecroFile((String)var1.get("prop.sys.root"), "anyk_nyilvanos_uzemmod.xml");
         if (var2.exists()) {
            var1.set("prop.dynamic.public.mode", Boolean.TRUE);
            System.out.println("\nPublikus üzemmód bekapcsolva!\n");
         }
      } catch (Exception var3) {
         Tools.eLog(var3, 1);
      }

   }

   private void log(Exception var1) {
      Tools.eLog(var1, 0);
   }

   public void alertOldNewAuth() {
      if (!this.selectionAlreadyDone("kau")) {
         int var1 = 2 * GuiUtil.getW("WWWEz a figyelmeztetés többet NE jelenjen meg!WWW");
         final JDialog var2 = new JDialog(MainFrame.thisinstance, "Figyelem!", true);
         final JCheckBox var3 = GuiUtil.getANYKCheckBox("Ez a figyelmeztetés többet NE jelenjen meg!");
         Color var4 = var3.getBackground();
         String var5 = String.format("#%02x%02x%02x", var4.getRed(), var4.getGreen(), var4.getBlue());
         Color var6 = var3.getForeground();
         String var7 = String.format("#%02x%02x%02x", var6.getRed(), var6.getGreen(), var6.getBlue());
         String var8 = "";

         try {
            var8 = "<html><body bgcolor=\"" + var5 + "\" text=\"" + var7 + "\" style=\"font-family:" + var2.getFont().getFamily() + "; font-size:" + GuiUtil.getCommonFontSize() + ";\" >Ha ön rendszeres felhasználója az ÁNYK programnak és legtöbbször az Ügyfélkapu felhasználónév/jelszó páros megadásával azonosítja magát a nyomtatványai beküldéséhez, akkor lehetősége van a bejelentkezéshez az ügyfélkapus azonosítást használni, a KAÜ portál megjelenítése helyett, melyet a <br><b>Szerviz/Beállítások/Működés</b> fülön állíthat be.<br><br>Mindegyik bejelentkezési mód továbbra is a KAÜ azonosítás igénybevételével történik.</body></html>";
         } catch (Exception var16) {
            var8 = "<html><body bgcolor=\"" + var5 + "\" text=\"" + var7 + "\">Ha ön rendszeres felhasználója az ÁNYK programnak és legtöbbször az Ügyfélkapu felhasználónév/jelszó páros megadásával azonosítja magát a nyomtatványai beküldéséhez, akkor lehetősége van a bejelentkezéshez az ügyfélkapus azonosítást használni, a KAÜ portál megjelenítése helyett, melyet a <br><b>Szerviz/Beállítások/Működés</b> fülön állíthat be.<br><br>Mindegyik bejelentkezési mód továbbra is a KAÜ azonosítás igénybevételével történik.</body></html>";
         }

         JEditorPane var9 = new JEditorPane("text/html", var8);
         var3.setAlignmentX(0.5F);
         var9.setFont(var2.getFont());
         var9.setEditable(false);
         var9.setBackground(var3.getBackground());
         var9.setForeground(var3.getForeground());
         var9.setSize(Math.max(600, var1), 20 * (GuiUtil.getCommonItemHeight() + 2));
         JScrollPane var10 = new JScrollPane(var9, 20, 30);
         JPanel var11 = new JPanel(new BorderLayout());
         JPanel var12 = new JPanel(new BorderLayout());
         JPanel var13 = new JPanel();
         var12.add(var10, "Center");
         var3.setSize(var1 / 2 + 20, GuiUtil.getCommonItemHeight() + 2);
         EmptyBorder var14 = new EmptyBorder(10, 10, 10, 10);
         var10.setBorder(var14);
         var3.setPreferredSize(var3.getSize());
         var3.setBorder(var14);
         var12.add(var3, "South");
         var11.add(var12, "Center");
         JButton var15 = new JButton("Ok");
         var15.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               if (var3.isSelected()) {
                  try {
                     InitApplication.this.doSelection("kau");
                  } catch (IOException var3x) {
                     var3x.printStackTrace();
                  }
               }

               var2.setVisible(false);
               var2.dispose();
            }
         });
         var13.add(var15);
         var11.add(var13, "South");
         var12.setSize(440, 60);
         var12.setPreferredSize(var12.getSize());
         var2.getContentPane().add(var11);
         var2.setSize(var1, 18 * GuiUtil.getCommonItemHeight());
         var2.setPreferredSize(new Dimension(var1, 18 * GuiUtil.getCommonItemHeight()));
         var2.setMinimumSize(new Dimension(var1, 18 * GuiUtil.getCommonItemHeight()));
         var2.setLocationRelativeTo(MainFrame.thisinstance);
         var2.setVisible(true);
      }
   }

   private boolean selectionAlreadyDone(String var1) {
      IPropertyList var2 = PropertyList.getInstance();
      File var3 = new NecroFile((new NecroFile((String)var2.get("prop.usr.root"), (String)var2.get("prop.usr.settings"))).getAbsolutePath(), var1 + ".info");
      return var3.exists();
   }

   private void doSelection(String var1) throws IOException {
      IPropertyList var2 = PropertyList.getInstance();
      File var3 = new NecroFile((new NecroFile((String)var2.get("prop.usr.root"), (String)var2.get("prop.usr.settings"))).getAbsolutePath(), var1 + ".info");
      FileOutputStream var4 = new FileOutputStream(var3);
      var4.close();
   }

   public void alertDAP() {
      if (!this.selectionAlreadyDone("dap")) {
         int var1 = 2 * GuiUtil.getW("WWWEz a figyelmeztetés többet NE jelenjen meg!WWW");
         final JDialog var2 = new JDialog(MainFrame.thisinstance, "Figyelem!", true);
         JCheckBox var3 = GuiUtil.getANYKCheckBox("A tájékoztatást elolvastam. *");
         final JCheckBox var4 = GuiUtil.getANYKCheckBox("Ez a figyelmeztetés többet NE jelenjen meg!");
         final JButton var5 = new JButton("Ok");
         Color var6 = var4.getBackground();
         String var7 = String.format("#%02x%02x%02x", var6.getRed(), var6.getGreen(), var6.getBlue());
         Color var8 = var4.getForeground();
         String var9 = String.format("#%02x%02x%02x", var8.getRed(), var8.getGreen(), var8.getBlue());
         String var10 = "";

         try {
            var10 = "<html><body bgcolor=\"" + var7 + "\" text=\"" + var9 + "\" style=\"font-family:" + var2.getFont().getFamily() + "; font-size:" + GuiUtil.getCommonFontSize() + ";\" >" + "A KAÜ portál 2024. december 1-jei karbantartását követően az ÁNYK-ban a \"KAÜ portál használatával\" történő felhasználóazonosítási opció már nem használható, az ÁNYK-ban előre beállítható azonosítási funkciók között már nem található meg.<br>" + "<ul><li>Ha Ön korábban ezt az azonosítási módot választotta, az ÁNYK automatikusan az \"Ügyfélkapu segítségével (KAÜ azonosítás háttérben történik)\" azonosítási módra váltott.</li>" + "<li>Ha Ön egyfaktoros Ügyfélkapu azonosítást használ, nincs teendője.</li>" + "<li>Ha Ügyfélkapu+ regisztrációval rendelkezik, a beállítást módosítania kell a Szerviz->Beállítások->Működés fülön \"Ügyfélkapu+ kétfaktoros hitelesítés használatával (KAÜ azonosítás a háttérben történik)\" beállításra. A változás aktiválásához az OK gombra kattintás szükséges!</li></ul>" + "</body></html>";
         } catch (Exception var19) {
            var10 = "<html><body bgcolor=\"" + var7 + "\" text=\"" + var9 + "\">" + "A KAÜ portál 2024. december 1-jei karbantartását követően az ÁNYK-ban a \"KAÜ portál használatával\" történő felhasználóazonosítási opció már nem használható, az ÁNYK-ban előre beállítható azonosítási funkciók között már nem található meg.<br>" + "<ul><li>Ha Ön korábban ezt az azonosítási módot választotta, az ÁNYK automatikusan az \"Ügyfélkapu segítségével (KAÜ azonosítás háttérben történik)\" azonosítási módra váltott.</li>" + "<li>Ha Ön egyfaktoros Ügyfélkapu azonosítást használ, nincs teendője.</li>" + "<li>Ha Ügyfélkapu+ regisztrációval rendelkezik, a beállítást módosítania kell a Szerviz->Beállítások->Működés fülön \"Ügyfélkapu+ kétfaktoros hitelesítés használatával (KAÜ azonosítás a háttérben történik)\" beállításra. A változás aktiválásához az OK gombra kattintás szükséges!</li></ul>" + "</body></html>";
         }

         JEditorPane var11 = new JEditorPane("text/html", var10);
         var4.setAlignmentX(0.5F);
         var11.setFont(var2.getFont());
         var11.setEditable(false);
         var11.setBackground(var4.getBackground());
         var11.setForeground(var4.getForeground());
         var11.setSize(Math.max(600, var1), 22 * (GuiUtil.getCommonItemHeight() + 2));
         JScrollPane var12 = new JScrollPane(var11, 20, 30);
         JPanel var13 = new JPanel(new BorderLayout());
         JPanel var14 = new JPanel(new BorderLayout());
         JPanel var15 = new JPanel();
         var14.add(var12, "Center");
         var4.setSize(var1 / 2 + 20, GuiUtil.getCommonItemHeight() + 2);
         var3.setSize(var1 / 2 + 20, GuiUtil.getCommonItemHeight() + 2);
         EmptyBorder var16 = new EmptyBorder(10, 10, 10, 10);
         var12.setBorder(var16);
         var4.setPreferredSize(var4.getSize());
         var4.setBorder(var16);
         var3.setPreferredSize(var4.getSize());
         var3.setBorder(var16);
         var3.setToolTipText("Jelölje be 'A tájékoztatást elolvastam.' jelölőnégyzetet");
         var3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent var1) {
               var5.setEnabled(((JCheckBox)var1.getSource()).isSelected());
            }
         });
         JPanel var17 = new JPanel(new BorderLayout());
         var17.setSize(var1, 80);
         var17.setPreferredSize(var17.getSize());
         var17.add(var4, "South");
         var17.add(var3, "Center");
         var14.add(var17, "South");
         var13.add(var14, "Center");
         var5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               if (var4.isSelected()) {
                  try {
                     InitApplication.this.doSelection("nouk");
                  } catch (IOException var3) {
                     var3.printStackTrace();
                  }
               }

               var2.setVisible(false);
               var2.dispose();
            }
         });
         var15.add(var5);
         var5.setEnabled(false);
         var5.setToolTipText("Jelölje be 'A tájékoztatást elolvastam.' jelölőnégyzetet");
         var13.add(var15, "South");
         var14.setSize(Math.max(600, var1), 22 * (GuiUtil.getCommonItemHeight() + 2));
         var14.setPreferredSize(var14.getSize());
         var2.getContentPane().add(var13);
         Dimension var18 = new Dimension(Math.max(600, var1), 22 * GuiUtil.getCommonItemHeight());
         var2.setSize(var18);
         var2.setPreferredSize(var18);
         var2.setMinimumSize(var18);
         var2.setLocationRelativeTo(MainFrame.thisinstance);
         var2.setVisible(true);
      }
   }

   public void alertNoUK() {
      if (!this.selectionAlreadyDone("nouk")) {
         int var1 = GuiUtil.getW("WWA KAÜ-portál 2025. január 15-i átállása után az ÁNYK-ban az Ügyfélkapu segítségével (KAÜ azonosítás háttérben történik)WW");
         final JDialog var2 = new JDialog(MainFrame.thisinstance, "Figyelem!", true);
         var2.setDefaultCloseOperation(0);
         JCheckBox var3 = GuiUtil.getANYKCheckBox("A tájékoztatást elolvastam. *");
         final JCheckBox var4 = GuiUtil.getANYKCheckBox("Ez a figyelmeztetés többet NE jelenjen meg!");
         final JButton var5 = new JButton("Ok");
         Color var6 = var4.getBackground();
         String var7 = String.format("#%02x%02x%02x", var6.getRed(), var6.getGreen(), var6.getBlue());
         Color var8 = var4.getForeground();
         String var9 = String.format("#%02x%02x%02x", var8.getRed(), var8.getGreen(), var8.getBlue());
         String var10 = "";

         try {
            var10 = "<html><body bgcolor=\"" + var7 + "\" text=\"" + var9 + "\" style=\"font-family:" + var2.getFont().getFamily() + "; font-size:" + GuiUtil.getCommonFontSize() + ";\" >" + "A KAÜ-portál 2025. január 15-i átállása után az ÁNYK-ban az \"Ügyfélkapu segítségével (KAÜ azonosítás háttérben történik)\" felhasználóazonosítási opció nem használható.<br>" + "<ul><li>Ha Ön korábban ezt az azonosítási módot választotta, az ÁNYK automatikusan az \"Ügyfélkapu+ hitelesítő alkalmazás használatával (KAÜ azonosítás háttérben történik)\" azonosítási módra vált.</li>" + "<li>Ha Ön 2025. január 15. előtt is az \"Ügyfélkapu+ hitelesítő alkalmazás használatával (KAÜ azonosítás háttérben történik)\" azonosítást állította be az ÁNYK-ban, nincs teendője.</li>" + "<li>Ha van DÁP regisztrációja, a beállítást módosítania kell a Szerviz->Beállítások->Működés fülön \"DÁP mobilalkalmazás használatával (KAÜ azonosítás háttérben történik)\" beállításra. A változás aktiválásához az OK gombra kell kattintani!</li>" + "<li>Az ÁNYK-ban újra elérhető a \"KAÜ portál használatával\" felhasználóazonosítási opció.</li></ul>" + "</body></html>";
         } catch (Exception var19) {
            var10 = "<html><body bgcolor=\"" + var7 + "\" text=\"" + var9 + "\">" + "A KAÜ-portál 2025. január 15-i átállása után az ÁNYK-ban az \"Ügyfélkapu segítségével (KAÜ azonosítás háttérben történik)\" felhasználóazonosítási opció nem használható.<br>" + "<ul><li>Ha Ön korábban ezt az azonosítási módot választotta, az ÁNYK automatikusan az \"Ügyfélkapu+ hitelesítő alkalmazás használatával (KAÜ azonosítás háttérben történik)\" azonosítási módra vált.</li>" + "<li>Ha Ön 2025. január 15. előtt is az \"Ügyfélkapu+ hitelesítő alkalmazás használatával (KAÜ azonosítás háttérben történik)\" azonosítást állította be az ÁNYK-ban, nincs teendője.</li>" + "<li>Ha van DÁP regisztrációja, a beállítást módosítania kell a Szerviz->Beállítások->Működés fülön \"DÁP mobilalkalmazás használatával (KAÜ azonosítás háttérben történik)\" beállításra. A változás aktiválásához az OK gombra kell kattintani!</li>" + "<li>Az ÁNYK-ban újra elérhető a \"KAÜ portál használatával\" felhasználóazonosítási opció.</li></ul>" + "</body></html>";
         }

         JEditorPane var11 = new JEditorPane("text/html", var10);
         var4.setAlignmentX(0.5F);
         var11.setFont(var2.getFont());
         var11.setEditable(false);
         var11.setBackground(var4.getBackground());
         var11.setForeground(var4.getForeground());
         var11.setSize(Math.max(600, var1), 20 * (GuiUtil.getCommonItemHeight() + 4));
         JScrollPane var12 = new JScrollPane(var11, 20, 30);
         JPanel var13 = new JPanel(new BorderLayout());
         JPanel var14 = new JPanel(new BorderLayout());
         JPanel var15 = new JPanel();
         var14.add(var12, "Center");
         var4.setSize(var1 / 2 + 20, GuiUtil.getCommonItemHeight() + 2);
         var3.setSize(var1 / 2 + 20, GuiUtil.getCommonItemHeight() + 2);
         EmptyBorder var16 = new EmptyBorder(10, 10, 10, 10);
         var12.setBorder(var16);
         var4.setPreferredSize(var4.getSize());
         var4.setBorder(var16);
         var3.setPreferredSize(var4.getSize());
         var3.setBorder(var16);
         var3.setToolTipText("Jelölje be 'A tájékoztatást elolvastam.' jelölőnégyzetet");
         var3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent var1) {
               var5.setEnabled(((JCheckBox)var1.getSource()).isSelected());
            }
         });
         JPanel var17 = new JPanel(new BorderLayout());
         var17.setSize(var1, 60);
         var17.setPreferredSize(var17.getSize());
         var17.add(var4, "South");
         var17.add(var3, "Center");
         var14.add(var17, "South");
         var13.add(var14, "Center");
         var5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               if (var4.isSelected()) {
                  try {
                     InitApplication.this.doSelection("nouk");
                  } catch (IOException var3) {
                     var3.printStackTrace();
                  }
               }

               var2.setVisible(false);
               var2.dispose();
            }
         });
         var15.add(var5);
         var5.setEnabled(false);
         var5.setToolTipText("Jelölje be 'A tájékoztatást elolvastam.' jelölőnégyzetet");
         var13.add(var15, "South");
         var14.setSize(Math.max(600, var1), 20 * (GuiUtil.getCommonItemHeight() + 4));
         var14.setPreferredSize(var14.getSize());
         var2.getContentPane().add(var13);
         Dimension var18 = new Dimension(Math.max(750, var1), 23 * GuiUtil.getCommonItemHeight());
         var2.setSize(var18);
         var2.setPreferredSize(var18);
         var2.setMinimumSize(var18);
         var2.setLocationRelativeTo(MainFrame.thisinstance);
         var2.setVisible(true);
      }
   }
}
