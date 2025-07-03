package hu.piller.enykp.gui.model;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.fileloader.db.DbLoader;
import hu.piller.enykp.alogic.fileloader.db.DbPkgLoader;
import hu.piller.enykp.alogic.fileloader.docinfo.DocInfoLoader;
import hu.piller.enykp.alogic.fileloader.imp.ImpLoader;
import hu.piller.enykp.alogic.filepanels.batch.BatchFunctions;
import hu.piller.enykp.alogic.filepanels.filepanel.DownArrow;
import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.TableFilterPanel;
import hu.piller.enykp.alogic.filepanels.tablesorter.TableSorter;
import hu.piller.enykp.alogic.fileutil.DataChecker;
import hu.piller.enykp.alogic.fileutil.HeadChecker;
import hu.piller.enykp.alogic.fileutil.TemplateNameResolver;
import hu.piller.enykp.alogic.masterdata.gui.selector.EntitySelection;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.orghandler.OrgHandler;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.orghandler.OrgResource;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.alogic.templateutils.FieldsGroups;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.alogic.upgrademanager_v2_0.Hatalyossag;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeManager;
import hu.piller.enykp.datastore.CachedCollection;
import hu.piller.enykp.datastore.Datastore_history;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.GUI_Datastore;
import hu.piller.enykp.extensions.db.DbFactory;
import hu.piller.enykp.extensions.db.IDbHandler;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.ILoadManager;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.print.simpleprint.KPrintFormFeedType;
import hu.piller.enykp.print.simpleprint.KPrintPageType;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.EventLog;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.Version;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import hu.piller.enykp.util.base.tabledialog.TooltipTableHeader;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class BookModel implements ILoadManager {
   public static final int VISSZAVONVA = 0;
   public static final int HASZNALATBAN = 1;
   public static final int NOTHING_TO_DO = 2;
   public static final int HIBA_AZ_ELLENORZESKOR = 3;
   public static final int VAN_UJABB_A_KLIENSEN = 4;
   public static final int VAN_UJABB_A_SZERVEREN = 5;
   public static String[] CHECK_VALID_MESSAGES = new String[]{"A nyomtatványt a kibocsátó kivonta a használatból!", "", "", "", "A nyomtatvány ezen verzióját a kibocsátó kivonta a használatból!", "A nyomtatványnak létezik új verziója! Kérjük frissítsen!"};
   String loaderid;
   String suffix;
   String description;
   public File loadedfile;
   public boolean dirty;
   public DbLoader dbloader;
   public DbPkgLoader dbpkgloader;
   public Hashtable head;
   public Hashtable docinfo;
   public String id;
   public String main_document_id;
   public String name;
   public String help;
   public String epost;
   public String temtype;
   public String splitesaver;
   public Vector forms;
   public Vector metas;
   public Vector pagemetas;
   public Vector attachementsall;
   FormModel fm;
   PageModel pm;
   FormModel mainfm;
   Hashtable metasht;
   Hashtable pagemetasht;
   Hashtable attacht;
   Vector attachements;
   Hashtable kvprintht;
   public CachedCollection cc;
   public Calculator calculator;
   public int[] maxcreation;
   public int[] created;
   public Object pa;
   public Object te;
   private boolean single;
   private boolean onlyhead;
   private boolean in_type;
   private boolean in_saved;
   private boolean in_gui;
   private String textAreaFid;
   private ArrayList<String> textAreaFids;
   private HashMap<String, ArrayList<String>> textAreaFidMap;
   private HashMap<String, String> fidTextAreaMap;
   public boolean hasError;
   public boolean hasAddError;
   public String errormsg;
   public Vector errorlist;
   public Vector warninglist;
   public Vector impwarninglist;
   public boolean silent;
   public boolean no_tech_fields;
   private boolean xkr_mode;
   private boolean xcz_mode_modifier;
   private int calcelemindex;
   private String barcode;
   private String bizt_azon;
   private String event_azon;
   private String igazgatosagi_kod;
   private boolean tesztmode;
   public boolean from_rdb;
   public boolean from_xml;
   private String rowkey;
   public String helpverstr;
   public boolean autofill;
   public boolean emptyprint;
   private boolean newStylePageBreak;
   private boolean disabledTemplate;
   long n0;
   private EntitySelection[] es;

   public BookModel() {
      this.single = true;
      this.textAreaFid = null;
      this.textAreaFidMap = new HashMap();
      this.fidTextAreaMap = new HashMap();
      this.silent = false;
      this.no_tech_fields = false;
      this.xkr_mode = false;
      this.xcz_mode_modifier = false;
      this.tesztmode = false;
      this.from_rdb = false;
      this.from_xml = false;
      this.autofill = false;
      this.emptyprint = false;
      this.disabledTemplate = false;
      this.init();
   }

   public BookModel(File var1) {
      this(var1, false);
      long var2 = System.nanoTime();
   }

   public BookModel(File var1, boolean var2) {
      this.single = true;
      this.textAreaFid = null;
      this.textAreaFidMap = new HashMap();
      this.fidTextAreaMap = new HashMap();
      this.silent = false;
      this.no_tech_fields = false;
      this.xkr_mode = false;
      this.xcz_mode_modifier = false;
      this.tesztmode = false;
      this.from_rdb = false;
      this.from_xml = false;
      this.autofill = false;
      this.emptyprint = false;
      this.disabledTemplate = false;
      this.n0 = System.nanoTime();

      try {
         EventLog.getInstance().writeLog(var1);
      } catch (IOException var6) {
         Tools.eLog(var6, 0);
      }

      this.silent = var2;
      this.init();

      try {
         this.load(var1);
         if (!this.hasError) {
            this.makeempty();
            if (this.hasError) {
               GuiUtil.setcurrentpagename("");
               GuiUtil.showMessageDialog((Component)null, this.errormsg, "Hiba", 0);
               return;
            }
         }
      } catch (Exception var4) {
         this.exception_done(var4);
      } catch (Error var5) {
         this.error_done(var5);
      }

   }

   public BookModel(File var1, File var2) {
      this(var1, var2, false);
   }

   public BookModel(File var1, File var2, boolean var3) {
      this(var1, var2, var3, false);
   }

   public BookModel(File var1, File var2, boolean var3, boolean var4) {
      this.single = true;
      this.textAreaFid = null;
      this.textAreaFidMap = new HashMap();
      this.fidTextAreaMap = new HashMap();
      this.silent = false;
      this.no_tech_fields = false;
      this.xkr_mode = false;
      this.xcz_mode_modifier = false;
      this.tesztmode = false;
      this.from_rdb = false;
      this.from_xml = false;
      this.autofill = false;
      this.emptyprint = false;
      this.disabledTemplate = false;

      try {
         String var5 = System.getProperty("java.awt.headless");
         if (var5 != null && var5.equals("true")) {
            ErrorList.getInstance().clear();
         }

         ErrorList.getInstance().writeError(new Integer(23112), "[ " + (new SimpleDateFormat()).format(new Date()) + " ] " + var2.getAbsolutePath(), 0, (Exception)null, (Object)null);
         EventLog.getInstance().writeLog(var1);
         EventLog.getInstance().writeLog(var2);
      } catch (IOException var9) {
         Tools.eLog(var9, 0);
      }

      this.no_tech_fields = var4;
      this.silent = var3;
      this.init();

      try {
         this.load(var1);
         if (!this.hasError) {
            this.makeempty();
            if (this.hasError) {
               GuiUtil.setcurrentpagename("");
               if (!var3) {
                  GuiUtil.showMessageDialog((Component)null, this.errormsg, "Hiba", 0);
               }

               return;
            }

            this.cc.load(var2);
            if (this.cc.hasError) {
               this.errormsg = this.cc.errormsg;
               this.hasError = true;
               throw new Exception(this.errormsg);
            }

            if (!var3 && this.warninglist != null) {
               GuiUtil.showErrorDialog((Frame)null, "Figyelmeztetések", true, true, this.warninglist);
            }

            this.cc.setSavepoints();
            Result var12 = DataChecker.getInstance().superCheck(this, false);
            if (!var12.isOk()) {
               this.hasError = true;
               this.errorlist = var12.errorList;
               GuiUtil.setcurrentpagename("");
               if (var3) {
                  return;
               }

               GuiUtil.showErrorDialog((Frame)null, "Hibalista", true, true, var12.errorList);
               return;
            }

            int var6;
            for(var6 = 0; var6 < this.forms.size(); ++var6) {
               this.created[var6] = 0;
            }

            for(var6 = 0; var6 < this.cc.size(); ++var6) {
               String var7 = ((Elem)this.cc.get(var6)).getType();

               for(int var8 = 0; var8 < this.forms.size(); ++var8) {
                  if (((FormModel)this.forms.get(var8)).id.equals(var7)) {
                     int var10002 = this.created[var8]++;
                     break;
                  }
               }
            }

            this.dirty2_done();
         }
      } catch (Exception var10) {
         this.exception_done(var10);
      } catch (Error var11) {
         this.error_done(var11);
      }

   }

   private void exception_done(Exception var1) {
      GuiUtil.setcurrentpagename("");
      if (this.cc != null) {
         this.cc.destroy();
      }

      this.errormsg = var1.getMessage();
      if (this.errormsg == null) {
         this.errormsg = var1.toString();
      }

      if (this.errormsg == null) {
         this.errormsg = "Végzetes kivétel!";
         var1.printStackTrace();
      }

      this.hasError = true;
      GuiUtil.setcurrentpagename("");
      if (!this.silent) {
         GuiUtil.showMessageDialog((Component)null, this.errormsg, "Hiba", 0);
      }
   }

   private void error_done(Error var1) {
      GuiUtil.setcurrentpagename("");
      if (this.cc != null) {
         this.cc.destroy();
      }

      this.errormsg = var1.getMessage();
      if (this.errormsg == null) {
         this.errormsg = var1.toString();
      }

      if (this.errormsg == null) {
         this.errormsg = "Végzetes hiba!";
         var1.printStackTrace();
      }

      if (this.errormsg.indexOf("OutOfMemoryError") != -1) {
         this.errormsg = "A művelet elvégzéséhez kevés a memória!";
      }

      this.hasError = true;
      GuiUtil.setcurrentpagename("");
      if (!this.silent) {
         GuiUtil.showMessageDialog((Component)null, this.errormsg, "Hiba", 0);
      }
   }

   private void dirty2_done() {
      try {
         GuiUtil.done_menuextratext(true);
         String var1 = (String)this.cc.docinfo.get("calculated");
         if (var1.equals("false")) {
            SettingsStore.getInstance().set("gui", "mezőszámítás", "false");
            GuiUtil.calchelper(false, this);
         }
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
      }

   }

   public void reload(File var1, boolean var2) {
      try {
         EventLog.getInstance().writeLog(var1);
      } catch (IOException var7) {
         Tools.eLog(var7, 0);
      }

      this.silent = var2;

      try {
         this.reempty();
         if (!this.hasError) {
            this.cc.load(var1);
            if (this.cc.hasError) {
               this.errormsg = this.cc.errormsg;
               this.hasError = true;
               throw new Exception(this.errormsg);
            }

            Result var3 = DataChecker.getInstance().superCheck(this, false);
            if (!var3.isOk()) {
               this.hasError = true;
               this.errorlist = var3.errorList;
               if (var2) {
                  return;
               }

               GuiUtil.showErrorDialog((Frame)null, "Hibalista", true, true, var3.errorList);
               return;
            }

            if (!var2 && this.warninglist != null) {
               GuiUtil.showErrorDialog((Frame)null, "Figyelmeztetések", true, true, this.warninglist);
            }

            for(int var4 = 0; var4 < this.cc.size(); ++var4) {
               String var5 = ((Elem)this.cc.get(var4)).getType();

               for(int var6 = 0; var6 < this.forms.size(); ++var6) {
                  if (((FormModel)this.forms.get(var6)).id.equals(var5)) {
                     int var10002 = this.created[var6]++;
                     break;
                  }
               }
            }

            this.dirty2_done();
         }
      } catch (Exception var8) {
         this.exception_done(var8);
      } catch (Error var9) {
         this.error_done(var9);
      }

   }

   public void reempty() {
      long var1 = System.nanoTime();
      this.init();
      this.makeempty0();
      int var3 = this.forms.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         this.created[var4] = 0;
      }

      this.pa = null;
      this.te = null;
      this.calculator = Calculator.getInstance();
      this.calculator.setBookModel(this);
      long var6 = System.nanoTime();
   }

   private void makeempty() {
      this.makeempty0();
      int var1 = this.forms.size();
      this.maxcreation = new int[var1];
      this.created = new int[var1];
      MetaInfo var2 = MetaInfo.getInstance();
      var2.init();

      int var3;
      FormModel var4;
      for(var3 = 0; var3 < var1; ++var3) {
         var4 = (FormModel)this.forms.get(var3);
         this.maxcreation[var3] = var4.maxcreation;
         this.created[var3] = 0;
         var2.addForm(var4.id, this.getMetas(var4.id), this.getPageMetas(var4.id));
      }

      this.calculator = Calculator.getInstance();
      this.calculator.setBookModel(this);

      try {
         HeadChecker.getInstance().release();
         TemplateNameResolver.getInstance().createTeminfoList();
         this.calculator.build(this.loadedfile);

         for(var3 = 0; var3 < var1; ++var3) {
            var4 = (FormModel)this.forms.get(var3);
            FieldsGroups.getInstance().createModels(var4.id);
         }
      } catch (Exception var10) {
         this.hasError = true;
         this.errormsg = var10.getMessage();
         ErrorList.getInstance().writeError(new Integer(4000), var10.toString(), 0, (Exception)null, (Object)null);
         return;
      }

      for(var3 = 0; var3 < var1; ++var3) {
         var4 = (FormModel)this.forms.get(var3);
         Enumeration var5 = var4.fids.keys();

         while(var5.hasMoreElements()) {
            String var6 = (String)var5.nextElement();
            DataFieldModel var7 = (DataFieldModel)var4.fids.get(var6);

            try {
               var7.setcombomatrices(var4);
            } catch (Exception var9) {
               this.hasError = true;
               this.errormsg = var9.getMessage();
            }
         }
      }

   }

   private void makeempty0() {
      if (this.cc != null) {
         this.cc.destroy();
      }

      this.cc = new CachedCollection();
      this.cc.bm = this;

      try {
         IPropertyList var1 = PropertyList.getInstance();
         String var2 = System.getProperty("user.name", "");
         this.cc.init(var1.get("prop.usr.root") + File.separator + var1.get("prop.usr.tmp") + File.separator + (MainFrame.pid != null ? "cache_" + MainFrame.pid + ".cache" : "cache_" + var2 + ".cache"));
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   private void init() {
      this.loaderid = "template_loader_v1";
      this.suffix = ".tem.enyk";
      this.description = "Sablonok";
      this.onlyhead = false;
      this.calcelemindex = -1;
      this.dirty = false;
      this.errorlist = null;
      this.warninglist = null;
      this.hasError = false;
      this.hasAddError = false;
      this.errormsg = null;
      this.from_rdb = false;
      this.from_xml = false;
      this.xkr_mode = false;
      this.xcz_mode_modifier = false;
      PropertyList.getInstance().set("prop.dynamic.dirty2", Boolean.FALSE);
   }

   public boolean isXkr_mode() {
      return this.xkr_mode;
   }

   public void setXkr_mode(boolean var1) {
      this.xkr_mode = var1;
   }

   public boolean isXczModeModifier() {
      return this.xcz_mode_modifier;
   }

   public void setXczModeModifier(boolean var1) {
      this.xcz_mode_modifier = var1;
   }

   public BookModel load(String var1, String var2, String var3, String var4, BookModel var5) {
      return this;
   }

   public BookModel load(String var1, String var2, String var3, String var4) {
      return this;
   }

   public void load(File var1) {
      try {
         this.loadedfile = var1;
         FileInputStream var2 = new FileInputStream(var1);
         String var3 = "windows-1250";
         this.load(var2, var3);
      } catch (Exception var4) {
         this.hasError = true;
         this.errormsg = var4.toString();
      }

   }

   public void load(String var1) {
      try {
         this.loadedfile = null;
         ByteArrayInputStream var2 = new ByteArrayInputStream(var1.getBytes());
         String var3 = "windows-1250";
         this.load(var2, var3);
      } catch (Exception var4) {
         this.hasError = true;
         this.errormsg = var4.toString();
      }

   }

   public void load(InputStream var1, String var2) {
      this.hasError = false;
      this.errormsg = null;
      this.in_type = false;
      this.in_saved = false;
      this.in_gui = false;
      if (this.textAreaFids != null) {
         this.textAreaFids.clear();
      }

      this.textAreaFidMap.clear();
      this.fidTextAreaMap.clear();

      try {
         DefaultHandler var12;
         if (this.onlyhead) {
            var12 = new DefaultHandler() {
               public void characters(char[] var1, int var2, int var3) throws SAXException {
                  if (BookModel.this.in_type) {
                     String var4 = new String(var1, var2, var3);
                     BookModel.this.head.put("type", var4);
                     if (var4.equals("multi")) {
                        BookModel.this.single = false;
                     }
                  } else if (BookModel.this.in_saved) {
                     BookModel.this.head.put("saved", new String(var1, var2, var3));
                  }

               }

               public void endDocument() throws SAXException {
                  super.endDocument();
               }

               public void endElement(String var1, String var2, String var3) throws SAXException {
                  if (var3.equals("HEAD")) {
                     BookModel.this.head.put("docinfo", BookModel.this.docinfo);
                     throw new SAXException("OUT");
                  } else {
                     if (var3.equals("TYPE")) {
                        BookModel.this.in_type = false;
                     } else if (var3.equals("SAVED")) {
                        BookModel.this.in_saved = false;
                     }

                  }
               }

               public void endPrefixMapping(String var1) throws SAXException {
                  super.endPrefixMapping(var1);
               }

               public void error(SAXParseException var1) throws SAXException {
                  super.error(var1);
               }

               public void fatalError(SAXParseException var1) throws SAXException {
                  super.fatalError(var1);
               }

               public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
                  super.ignorableWhitespace(var1, var2, var3);
               }

               public void notationDecl(String var1, String var2, String var3) throws SAXException {
                  super.notationDecl(var1, var2, var3);
               }

               public void processingInstruction(String var1, String var2) throws SAXException {
                  super.processingInstruction(var1, var2);
               }

               public void setDocumentLocator(Locator var1) {
                  super.setDocumentLocator(var1);
               }

               public void skippedEntity(String var1) throws SAXException {
                  super.skippedEntity(var1);
               }

               public void startDocument() throws SAXException {
                  super.startDocument();
               }

               public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
                  if (var3.equals("HEAD")) {
                     BookModel.this.head = new Hashtable();
                     BookModel.this.attributes_done(var4, BookModel.this.head);
                  } else if (var3.equals("DOCINFO")) {
                     BookModel.this.docinfo = new Hashtable();
                     BookModel.this.attributes_done(var4, BookModel.this.docinfo);
                  } else if (var3.equals("TYPE")) {
                     BookModel.this.in_type = true;
                  } else if (var3.equals("SAVED")) {
                     BookModel.this.in_saved = true;
                  } else if (var3.equals("DOC")) {
                     try {
                        Hashtable var5 = new Hashtable();
                        BookModel.this.attributes_done(var4, var5);
                        if (BookModel.this.docinfo == null) {
                           BookModel.this.docinfo = new Hashtable();
                        }

                        Hashtable var6 = (Hashtable)BookModel.this.docinfo.get("docs");
                        if (var6 == null) {
                           var6 = new Hashtable();
                           BookModel.this.docinfo.put("docs", var6);
                        }

                        String var7 = (String)var5.get("id");
                        if (var7 != null) {
                           var6.put(var7, var5);
                        }
                     } catch (Exception var8) {
                        Tools.eLog(var8, 0);
                     }
                  }

               }

               public void startPrefixMapping(String var1, String var2) throws SAXException {
                  super.startPrefixMapping(var1, var2);
               }

               public void unparsedEntityDecl(String var1, String var2, String var3, String var4) throws SAXException {
                  super.unparsedEntityDecl(var1, var2, var3, var4);
               }

               public void warning(SAXParseException var1) throws SAXException {
                  super.warning(var1);
               }
            };
         } else {
            var12 = new DefaultHandler() {
               public void characters(char[] var1, int var2, int var3) throws SAXException {
                  if (BookModel.this.in_type) {
                     String var4 = new String(var1, var2, var3);
                     BookModel.this.head.put("type", var4);
                     if (var4.equals("multi")) {
                        BookModel.this.single = false;
                     }
                  } else if (BookModel.this.in_saved) {
                     BookModel.this.head.put("saved", new String(var1, var2, var3));
                  }

               }

               public void endDocument() throws SAXException {
                  super.endDocument();
               }

               public void endElement(String var1, String var2, String var3) throws SAXException {
                  if (var3.equals("METAS")) {
                     BookModel.this.metas.add(BookModel.this.metasht);
                     BookModel.this.pagemetas.add(BookModel.this.pagemetasht);
                     if (BookModel.this.single) {
                        throw new SAXException("OUT");
                     }
                  } else if (var3.equals("TYPE")) {
                     BookModel.this.in_type = false;
                  } else if (var3.equals("SAVED")) {
                     BookModel.this.in_saved = false;
                  } else if (var3.equals("HEAD")) {
                     BookModel.this.head.put("docinfo", BookModel.this.docinfo);
                  } else if (var3.equals("GUI")) {
                     BookModel.this.in_gui = false;
                     BookModel.this.fm.kvprintht = BookModel.this.kvprintht;
                  } else if (var3.equals("TextArea")) {
                     BookModel.this.textAreaFidMap.put(BookModel.this.textAreaFid, BookModel.this.textAreaFids);
                  }

               }

               public void endPrefixMapping(String var1) throws SAXException {
                  super.endPrefixMapping(var1);
               }

               public void error(SAXParseException var1) throws SAXException {
                  super.error(var1);
               }

               public void fatalError(SAXParseException var1) throws SAXException {
                  super.fatalError(var1);
               }

               public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
                  super.ignorableWhitespace(var1, var2, var3);
               }

               public void notationDecl(String var1, String var2, String var3) throws SAXException {
                  super.notationDecl(var1, var2, var3);
               }

               public void processingInstruction(String var1, String var2) throws SAXException {
                  super.processingInstruction(var1, var2);
               }

               public void setDocumentLocator(Locator var1) {
                  super.setDocumentLocator(var1);
               }

               public void skippedEntity(String var1) throws SAXException {
                  super.skippedEntity(var1);
               }

               public void startDocument() throws SAXException {
                  PropertyList.getInstance().set("prop.dynamic.tervezetFieldFid", (Object)null);
               }

               public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
                  if (var3.equals("HEAD")) {
                     BookModel.this.head = new Hashtable();
                     BookModel.this.attributes_done(var4, BookModel.this.head);
                  } else if (var3.equals("GUIITEM")) {
                     DataFieldModel var5;
                     if (var4.getValue("type").equals("data")) {
                        var5 = null;

                        try {
                           var5 = new DataFieldModel(var4);
                           var5.formmodel = BookModel.this.fm;
                           var5.pagemodel = BookModel.this.pm;
                           if (var4.getValue("kp_force") != null) {
                              try {
                                 var5.setKp_force(DataFieldModel.KpForceType.valueOf(var4.getValue("kp_force")));
                              } catch (Exception var10) {
                                 System.out.println("Hibás a 'kp_force' attributum értéke!" + var4.getValue("kp_force"));
                              }
                           }
                        } catch (Exception var11) {
                           BookModel.this.errormsg = "Hibás adat elem a sablonban!";
                           throw new SAXException(BookModel.this.errormsg);
                        }

                        BookModel.this.pm.addDF(var5);
                        BookModel.this.fm.fids.put(var5.key, var5);
                        BookModel.this.fm.fids_page.put(var5.key, BookModel.this.pm);
                     } else {
                        var5 = null;

                        Object var12;
                        try {
                           var12 = GuiUtil.getVisualFieldModel(var4, BookModel.this.fm);
                        } catch (Exception var9) {
                           var9.printStackTrace();
                           BookModel.this.errormsg = "Hibás vizuális elem a sablonban!\nA " + BookModel.this.pm.name + " oldalon, tipus=" + var4.getValue("type") + " x=" + var4.getValue("x") + " y=" + var4.getValue("y");
                           throw new SAXException(BookModel.this.errormsg);
                        }

                        BookModel.this.pm.addVF(var12);
                        BookModel.this.fm.addVF(var12);
                     }
                  } else {
                     String var6;
                     Hashtable var13;
                     if (var3.equals("META")) {
                        var13 = new Hashtable();
                        BookModel.this.attributes_done(var4, var13);
                        var6 = (String)var13.get("fid");
                        if (var13.containsKey("vid") && "E_bevallás".equals(var13.get("vid"))) {
                           PropertyList.getInstance().set("prop.dynamic.tervezetFieldFid", var6);
                        }

                        BookModel.this.metasht.put(var6, var13);
                     } else if (var3.equals("PAGEMETA")) {
                        var13 = new Hashtable();
                        BookModel.this.attributes_done(var4, var13);
                        var6 = (String)var13.get("pid");
                        BookModel.this.pagemetasht.put(var6, var13);
                     } else if (var3.equals("METAS")) {
                        BookModel.this.metasht = new Hashtable();
                        BookModel.this.pagemetasht = new Hashtable();
                     } else if (var3.equals("PAGE")) {
                        if (BookModel.this.in_gui) {
                           BookModel.this.pm = new PageModel(var4);
                           if (!BookModel.this.newStylePageBreak && (BookModel.this.pm.getKpLapDobas() != KPrintFormFeedType.NO || BookModel.this.pm.isKpLandscape() || BookModel.this.pm.getKpLapTipus() != KPrintPageType.NORMAL)) {
                              BookModel.this.newStylePageBreak = true;
                           }

                           BookModel.this.pm.setFormModel(BookModel.this.fm);
                           BookModel.this.fm.addPage(BookModel.this.pm);
                        }
                     } else {
                        String var15;
                        if (var3.equals("INPUTRULE")) {
                           var15 = var4.getValue("irule");
                           if ("[0-9+-,.]".equals(var15)) {
                              var15 = "[0-9+\\-,.]";
                           }

                           BookModel.this.fm.irids.put(var4.getValue("irid"), var15);
                        } else if (var3.equals("IMAGE")) {
                           var13 = new Hashtable();
                           BookModel.this.attributes_done(var4, var13);
                           BookModel.this.fm.images.put(var4.getValue("name"), var13);
                        } else if (var3.equals("ATTACHEMENT")) {
                           BookModel.this.attacht = new Hashtable();
                           BookModel.this.attributes_done(var4, BookModel.this.attacht);
                           BookModel.this.attachements.add(BookModel.this.attacht);
                        } else if (var3.equals("ATCSIZES")) {
                           BookModel.this.attacht = new Hashtable();
                           BookModel.this.attributes_done(var4, BookModel.this.attacht);
                           BookModel.this.attachements.add(BookModel.this.attacht);
                        } else if (var3.equals("KVPRINT")) {
                           BookModel.this.kvprintht = new Hashtable();
                           BookModel.this.attributes_done(var4, BookModel.this.kvprintht);
                           BookModel.this.kvprintht.put("hrows", new Vector());
                           BookModel.this.kvprintht.put("frows", new Vector());
                        } else if (var3.equals("FOOTER")) {
                           BookModel.this.attributes_done(var4, BookModel.this.kvprintht);
                           BookModel.this.rowkey = "frows";
                        } else if (var3.equals("HEADER")) {
                           BookModel.this.attributes_done(var4, BookModel.this.kvprintht);
                           BookModel.this.rowkey = "hrows";
                        } else if (var3.equals("ROW")) {
                           var13 = new Hashtable();
                           BookModel.this.attributes_done(var4, var13);
                           ((Vector)BookModel.this.kvprintht.get(BookModel.this.rowkey)).add(var13);
                        } else if (var3.equals("DOCINFO")) {
                           BookModel.this.docinfo = new Hashtable();
                           BookModel.this.attributes_done(var4, BookModel.this.docinfo);
                           if (BookModel.this.docinfo.get("org") == null) {
                              var15 = "A sablonban hiányzik a szervezet azonosító!\n" + BookModel.this.loadedfile;
                              ErrorList.getInstance().writeError(new Integer(76365), "[ " + (new SimpleDateFormat()).format(new Date()) + " ] " + var15, 0, (Exception)null, (Object)null);
                              throw new SAXException(var15);
                           }

                           if (BookModel.this.docinfo.get("org").toString().trim().length() == 0) {
                              var15 = "A sablonban üres a szervezet azonosító!\n" + BookModel.this.loadedfile;
                              ErrorList.getInstance().writeError(new Integer(76365), "[ " + (new SimpleDateFormat()).format(new Date()) + " ] " + var15, 0, (Exception)null, (Object)null);
                              throw new SAXException(var15);
                           }

                           var15 = (String)BookModel.this.docinfo.get("minkitoltoverzio");
                           if (var15 != null) {
                              Version var14 = new Version(var15);
                              Version var7 = new Version("3.44.0");
                              if (var7.compareTo(var14) < 0) {
                                 throw new SAXException("A nyomtatványhoz a keretprogram újabb verziója szükséges!");
                              }
                           }
                        } else if (var3.equals("TYPE")) {
                           BookModel.this.in_type = true;
                        } else if (var3.equals("SAVED")) {
                           BookModel.this.in_saved = true;
                        } else if (var3.equals("FORMS")) {
                           BookModel.this.forms = new Vector();
                           BookModel.this.metas = new Vector();
                           BookModel.this.pagemetas = new Vector();
                           BookModel.this.attachementsall = new Vector();
                           BookModel.this.id = var4.getValue("id");
                           BookModel.this.name = var4.getValue("name");
                           BookModel.this.main_document_id = var4.getValue("main_document_id");
                           if (BookModel.this.main_document_id == null) {
                              BookModel.this.main_document_id = var4.getValue("maindocumentid");
                           }

                           BookModel.this.help = var4.getValue("help");
                           BookModel.this.epost = var4.getValue("epost");
                           BookModel.this.temtype = var4.getValue("temtype");
                           BookModel.this.splitesaver = var4.getValue("splitesaver");
                           if (BookModel.this.splitesaver == null) {
                              BookModel.this.splitesaver = "false";
                           }
                        } else if (var3.equals("FORM")) {
                           BookModel.this.attachements = new Vector();
                           BookModel.this.attachementsall.add(BookModel.this.attachements);
                           BookModel.this.kvprintht = null;
                           BookModel.this.fm = new FormModel(var4);
                           BookModel.this.fm.setBookModel(BookModel.this);
                           BookModel.this.forms.add(BookModel.this.fm);
                           if (BookModel.this.main_document_id.equals(BookModel.this.fm.id)) {
                              BookModel.this.mainfm = BookModel.this.fm;
                           }
                        } else if (var3.equals("GUI")) {
                           BookModel.this.in_gui = true;
                        } else if (var3.equals("DOC")) {
                           try {
                              var13 = new Hashtable();
                              BookModel.this.attributes_done(var4, var13);
                              if (BookModel.this.docinfo == null) {
                                 BookModel.this.docinfo = new Hashtable();
                              }

                              Hashtable var16 = (Hashtable)BookModel.this.docinfo.get("docs");
                              if (var16 == null) {
                                 var16 = new Hashtable();
                                 BookModel.this.docinfo.put("docs", var16);
                              }

                              String var17 = (String)var13.get("id");
                              if (var17 != null) {
                                 var16.put(var17, var13);
                              }
                           } catch (Exception var8) {
                              Tools.eLog(var8, 0);
                           }
                        } else if (var3.equals("TextArea")) {
                           BookModel.this.textAreaFid = var4.getValue("fid");
                           BookModel.this.textAreaFids = new ArrayList();
                           BookModel.this.textAreaFids.add(var4.getValue("fid"));
                           BookModel.this.fidTextAreaMap.put(var4.getValue("fid"), var4.getValue("fid"));
                        } else if (var3.equals("Item") && BookModel.this.textAreaFid != null) {
                           BookModel.this.textAreaFids.add(var4.getValue("fid"));
                           BookModel.this.fidTextAreaMap.put(var4.getValue("fid"), BookModel.this.textAreaFid);
                        }
                     }
                  }

               }

               public void startPrefixMapping(String var1, String var2) throws SAXException {
                  super.startPrefixMapping(var1, var2);
               }

               public void unparsedEntityDecl(String var1, String var2, String var3, String var4) throws SAXException {
                  super.unparsedEntityDecl(var1, var2, var3, var4);
               }

               public void warning(SAXParseException var1) throws SAXException {
                  super.warning(var1);
               }
            };
         }

         SAXParserFactory var4 = SAXParserFactory.newInstance();
         SAXParser var5 = var4.newSAXParser();
         InputSource var6 = new InputSource(var1);
         var6.setEncoding(var2);
         var5.parse(var6, var12);
         var1.close();
      } catch (Exception var11) {
         Exception var3 = var11;

         try {
            if (!var3.getMessage().equals("OUT")) {
               this.hasError = true;
               if (this.errormsg == null) {
                  this.errormsg = var3.getMessage();
               }

               if (this.errormsg != null && "10000 >= 10000".equals(this.errormsg)) {
                  this.errormsg = "A sablonban lévő adatmezők száma elérte a maximumot (10 000), nem olvasható be!";
               }
            }

            try {
               var1.close();
            } catch (IOException var9) {
            }
         } catch (Exception var10) {
            this.hasError = true;
            if (this.errormsg == null) {
               this.errormsg = var11.toString();
            }

            try {
               var1.close();
            } catch (IOException var8) {
            }
         }
      }

      if (!this.hasError) {
         this.handleOrgCheckValid();
         if (!this.onlyhead) {
            this.finishload();
            String var13 = "";

            try {
               DocInfoLoader var14 = new DocInfoLoader();
               Hashtable var15 = var14.getHeadData((String)this.docinfo.get("org"), this.id);
               var13 = (String)((Hashtable)var15.get("docinfo")).get("ver");
            } catch (Exception var7) {
               Tools.eLog(var7, 0);
            }

            if (var13.length() != 0) {
               var13 = " súgó:" + var13;
            }

            this.helpverstr = var13;
            GuiUtil.setcurrentpagename(this.name + " v:" + this.docinfo.get("ver") + var13);
         }
      } else {
         if (this.silent) {
            return;
         }

         GuiUtil.showMessageDialog((Component)null, this.errormsg, "Hiba", 0);
      }

   }

   private void finishload() {
      int var1 = this.forms.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         FormModel var3 = (FormModel)this.forms.get(var2);
         Hashtable var4 = this.getMetas(var3.id);
         Enumeration var5 = var3.fids.keys();

         while(var5.hasMoreElements()) {
            String var6 = (String)var5.nextElement();
            DataFieldModel var7 = (DataFieldModel)var3.fids.get(var6);
            var7.setMeta((Hashtable)var4.get(var6), var3);
         }
      }

   }

   private void attributes_done(Attributes var1, Hashtable var2) {
      for(int var3 = 0; var3 < var1.getLength(); ++var3) {
         var2.put(var1.getQName(var3), var1.getValue(var3));
      }

   }

   public int size() {
      return this.forms.size();
   }

   private boolean checksuffix(File var1) {
      return !var1.getAbsolutePath().toLowerCase().endsWith(this.suffix);
   }

   public String getId() {
      return this.loaderid;
   }

   public String getDescription() {
      return this.description;
   }

   public Hashtable getHeadData(File var1) {
      if (this.checksuffix(var1)) {
         return null;
      } else {
         this.onlyhead = true;
         this.load(var1);
         return this.docinfo != null && this.docinfo.get("org") != null && this.docinfo.get("org").toString().trim().length() != 0 ? this.head : null;
      }
   }

   public String getFileNameSuffix() {
      return this.suffix;
   }

   public String createFileName(String var1) {
      return var1.toLowerCase().endsWith(this.suffix) ? var1 : var1 + this.suffix;
   }

   public FormModel get() {
      try {
         String var1 = ((Elem)this.cc.get(this.calcelemindex)).getType();
         return this.get(var1);
      } catch (Exception var2) {
         return null;
      }
   }

   public FormModel get(int var1) {
      try {
         return (FormModel)this.forms.get(var1);
      } catch (Exception var3) {
         return null;
      }
   }

   public FormModel get(String var1) {
      try {
         for(int var2 = 0; var2 < this.forms.size(); ++var2) {
            if (((FormModel)this.forms.get(var2)).id.equals(var1)) {
               return (FormModel)this.forms.get(var2);
            }
         }
      } catch (Exception var3) {
      }

      return null;
   }

   public int getIndex(FormModel var1) {
      try {
         for(int var2 = 0; var2 < this.forms.size(); ++var2) {
            if (this.forms.get(var2).equals(var1)) {
               return var2;
            }
         }
      } catch (Exception var3) {
      }

      return -1;
   }

   public void addForm(FormModel var1) {
      if (!MainFrame.rogzitomode && (this.from_rdb || this.from_xml)) {
         this.addForm_noPA(var1);
      } else {
         int var2 = this.getIndex(var1);
         if (var2 != -1) {
            this.pa = GuiUtil.setPa(this.pa);
            int var10002 = this.created[var2]++;
            GUI_Datastore var3 = new GUI_Datastore();
            Boolean var4 = new Boolean(var1.id.equals(this.main_document_id));
            Elem var5 = new Elem(var3, var1.id, var1.name, var4);
            Integer var6 = this.cc.getSequence();
            var5.getEtc().put("sn", var6);
            int[] var7 = new int[var1.size()];

            for(int var8 = 0; var8 < var7.length; ++var8) {
               var7[var8] = 1;
            }

            var5.getEtc().put("pagecounts", var7);
            this.cc.add(var5);
            this.cc.setActiveObject(var5);
            this.calcelemindex = this.cc.size() - 1;
            Hashtable var11 = new Hashtable();
            var11.put("id", var5.getType());
            var11.put("guiobject", var5);
            this.calculator.eventFired(var11);
            if (this.pa != null) {
               GuiUtil.apply_primary_account(this.pa, var1.id);
               GuiUtil.apply_primary_account(this.te, var1.id);
            } else {
               GuiUtil.apply_master_data(this.getSelectedEntities(), var1.id);
            }

            TemplateUtils.getInstance().setDefaultValues(var1.id, this);

            try {
               CalculatorManager.getInstance().doBetoltErtekCalcs(false);
               CalculatorManager.getInstance().form_calc();
               CalculatorManager.getInstance().do_dpage_count();
               CalculatorManager.getInstance().multi_form_load();
            } catch (Exception var10) {
               var10.printStackTrace();
            }

            this.dirty = true;
         }
      }
   }

   public void addForm_noPA(FormModel var1) {
      int var2 = this.getIndex(var1);
      if (var2 != -1) {
         int var10002 = this.created[var2]++;
         GUI_Datastore var3 = new GUI_Datastore();
         Boolean var4 = new Boolean(var1.id.equals(this.main_document_id));
         Elem var5 = new Elem(var3, var1.id, var1.name, var4);
         Integer var6 = this.cc.getSequence();
         var5.getEtc().put("sn", var6);
         int[] var7 = new int[var1.size()];

         for(int var8 = 0; var8 < var7.length; ++var8) {
            var7[var8] = 1;
         }

         var5.getEtc().put("pagecounts", var7);
         this.cc.add(var5);
         this.cc.setActiveObject(var5);
         this.calcelemindex = this.cc.size() - 1;
         Hashtable var9 = new Hashtable();
         var9.put("id", var5.getType());
         var9.put("guiobject", var5);
         this.calculator.eventFired(var9);
         TemplateUtils.getInstance().setDefaultValues(var1.id, this);
         this.dirty = true;
      }
   }

   public FormModel get_main_formmodel() {
      return this.mainfm;
   }

   public IDataStore get_main_document() {
      for(int var1 = 0; var1 < this.cc.size(); ++var1) {
         if (((Elem)this.cc.get(var1)).getType().equals(this.mainfm.id)) {
            return (IDataStore)((Elem)this.cc.get(var1)).getRef();
         }
      }

      return null;
   }

   public Elem get_main_elem() {
      for(int var1 = 0; var1 < this.cc.size(); ++var1) {
         if (((Elem)this.cc.get(var1)).getType().equals(this.mainfm.id)) {
            return (Elem)this.cc.get(var1);
         }
      }

      return null;
   }

   public int get_main_index() {
      for(int var1 = 0; var1 < this.cc.size(); ++var1) {
         if (((Elem)this.cc.get(var1)).getType().equals(this.mainfm.id)) {
            return var1;
         }
      }

      return -1;
   }

   public Hashtable getMetas(String var1) {
      try {
         for(int var2 = 0; var2 < this.forms.size(); ++var2) {
            if (((FormModel)this.forms.get(var2)).id.equals(var1)) {
               return (Hashtable)this.metas.get(var2);
            }
         }
      } catch (Exception var3) {
      }

      return null;
   }

   public Hashtable getPageMetas(String var1) {
      try {
         for(int var2 = 0; var2 < this.forms.size(); ++var2) {
            if (((FormModel)this.forms.get(var2)).id.equals(var1)) {
               return (Hashtable)this.pagemetas.get(var2);
            }
         }
      } catch (Exception var3) {
      }

      return null;
   }

   public int getCalcelemindex() {
      return this.calcelemindex;
   }

   public String getDbNumber() {
      try {
         if (this.isOnyaCheckMode()) {
            return "" + this.calcelemindex;
         } else {
            Elem var1 = (Elem)this.cc.get(this.calcelemindex);
            String[] var2 = (String[])((String[])var1.getEtc().get("dbparams"));
            return var2[4];
         }
      } catch (Exception var3) {
         return null;
      }
   }

   public boolean setCalcelemindex(int var1) {
      if (this.cc != null && var1 >= 0 && this.cc.size() - 1 >= var1) {
         this.calcelemindex = var1;
         return true;
      } else {
         return false;
      }
   }

   public Vector get_dfield_values(String var1) {
      try {
         Elem var2 = (Elem)this.cc.get(this.calcelemindex);
         IDataStore var3 = (IDataStore)var2.getRef();
         int[] var4 = (int[])((int[])var2.getEtc().get("pagecounts"));
         String var5 = var2.getType();
         FormModel var6 = this.get(var5);
         int var7 = var6.get((PageModel)var6.fids_page.get(var1));
         int var8 = var4[var7];
         Vector var9 = new Vector(var8);

         for(int var10 = 0; var10 < var8; ++var10) {
            String var11 = var3.get(var10 + "_" + var1);
            var9.add(var11 == null ? "" : var11);
         }

         return var9;
      } catch (Exception var12) {
         var12.printStackTrace();
         return null;
      }
   }

   public String get_formname() {
      try {
         Elem var1 = (Elem)this.cc.get(this.calcelemindex);
         String var2 = var1.getType();
         FormModel var3 = this.get(var2);
         return var3.name;
      } catch (Exception var4) {
         return null;
      }
   }

   public String get_formid() {
      try {
         Elem var1 = (Elem)this.cc.get(this.calcelemindex);
         String var2 = var1.getType();
         FormModel var3 = this.get(var2);
         return var3.id;
      } catch (Exception var4) {
         return null;
      }
   }

   public Vector get_store_collection() {
      return this.cc;
   }

   public IDataStore get_datastore() {
      try {
         Elem var1 = (Elem)this.cc.get(this.calcelemindex);
         IDataStore var2 = (IDataStore)var1.getRef();
         return var2;
      } catch (Exception var3) {
         return null;
      }
   }

   public void set_field_value(String var1, String var2) {
      try {
         int var3 = this.getIndex();
         Elem var4 = (Elem)this.cc.get(var3);
         IDataStore var5 = (IDataStore)var4.getRef();
         var5.set("0_" + var1, var2);
      } catch (Exception var6) {
      }
   }

   private int getIndex() {
      int var1;
      if (this.calcelemindex == -1) {
         var1 = this.cc.getActiveObjectindex();
      } else {
         var1 = this.calcelemindex;
      }

      return var1;
   }

   public void setFieldReadOnly(String var1, boolean var2) {
      try {
         int var3 = this.getIndex();
         Elem var4 = (Elem)this.cc.get(var3);
         FormModel var5 = this.get(var4.getType());
         ((DataFieldModel)var5.fids.get(var1)).readonly = var2;
      } catch (Exception var6) {
      }

   }

   public boolean getFieldReadOnly(String var1) {
      try {
         int var2 = this.getIndex();
         Elem var3 = (Elem)this.cc.get(var2);
         FormModel var4 = this.get(var3.getType());
         return ((DataFieldModel)var4.fids.get(var1)).readonly;
      } catch (Exception var5) {
         return false;
      }
   }

   public boolean getFieldVisible(String var1) {
      try {
         int var2 = this.getIndex();
         Elem var3 = (Elem)this.cc.get(var2);
         FormModel var4 = this.get(var3.getType());
         return ((DataFieldModel)var4.fids.get(var1)).visible;
      } catch (Exception var5) {
         return false;
      }
   }

   public boolean getFieldWriteable(String var1) {
      try {
         int var2 = this.getIndex();
         Elem var3 = (Elem)this.cc.get(var2);
         FormModel var4 = this.get(var3.getType());
         return ((DataFieldModel)var4.fids.get(var1)).writeable;
      } catch (Exception var5) {
         return false;
      }
   }

   public int[] get_pagecounts() {
      try {
         int var1 = this.getIndex();
         Elem var2 = (Elem)this.cc.get(var1);
         int[] var3 = (int[])((int[])var2.getEtc().get("pagecounts"));
         return var3;
      } catch (Exception var4) {
         return null;
      }
   }

   public Hashtable get_templateheaddata() {
      return this.head;
   }

   public void setPAInfo(Object var1, Object var2) {
      this.pa = var1;
      this.te = var2;
   }

   public void setSelectedEntities(EntitySelection[] var1) {
      this.es = var1;
   }

   public EntitySelection[] getSelectedEntities() {
      return this.es;
   }

   public Hashtable get_enabled_fields(String var1) {
      FormModel var2 = this.get(var1);
      Hashtable var3 = new Hashtable();
      Elem var4 = (Elem)this.cc.get(this.calcelemindex);

      for(int var5 = 0; var5 < var2.size(); ++var5) {
         String var6 = var2.get(var5).pid;
         Object[] var7 = new Object[]{var6, null, null, null};
         Hashtable var8 = new Hashtable();
         var8.put("id", var4.getType());
         var8.put("guiobject", var4);
         this.calculator.eventFired(var8);
         this.calculator.pageCheck(var7);
         boolean var9 = false;

         try {
            var9 = (Boolean)var7[1];
         } catch (Exception var12) {
         }

         if (var9) {
            Vector var10 = var2.get(var5).y_sorted_df;

            for(int var11 = 0; var11 < var10.size(); ++var11) {
               var3.put(((DataFieldModel)var10.get(var11)).key, "");
            }
         }
      }

      return var3;
   }

   public Hashtable get_enabled_fields(Elem var1) {
      String var2 = var1.getType();
      FormModel var3 = this.get(var2);
      int var4 = this.calcelemindex;
      this.calcelemindex = this.cc.getIndex(var1);
      Hashtable var5 = new Hashtable();

      for(int var6 = 0; var6 < var3.size(); ++var6) {
         boolean var7 = false;

         String var8;
         try {
            var8 = (String)this.fm.get(var6).xmlht.get("hwdisabled");
            if (var8.equals("yes")) {
               var7 = true;
            }
         } catch (Exception var16) {
            var7 = false;
         }

         try {
            int var17 = var3.get(var6).role;
            if ((var17 & var3.get(var6).getmask()) == 0) {
               var7 = true;
            }
         } catch (Exception var15) {
            var7 = false;
         }

         if (!var7) {
            var8 = var3.get(var6).pid;
            Object[] var9 = new Object[]{var8, null, null, null};
            Hashtable var10 = new Hashtable();
            var10.put("id", var1.getType());
            var10.put("guiobject", var1);
            this.calculator.eventFired(var10);
            this.calculator.pageCheck(var9);
            boolean var11 = false;

            try {
               var11 = (Boolean)var9[1];
            } catch (Exception var14) {
            }

            if (var11) {
               Vector var12 = var3.get(var6).y_sorted_df;

               for(int var13 = 0; var13 < var12.size(); ++var13) {
                  var5.put(((DataFieldModel)var12.get(var13)).key, "");
               }
            }
         }
      }

      this.calcelemindex = var4;
      return var5;
   }

   public void resetCalc() {
      Elem var1 = (Elem)this.cc.get(this.calcelemindex);
      Hashtable var2 = new Hashtable();
      var2.put("id", var1.getType());
      var2.put("guiobject", var1);
      this.calculator.eventFired(var2);
   }

   public void destroy() {
      try {
         if (this.calculator != null) {
            this.calculator.setBookModel((BookModel)null);
            this.calculator.release();
            this.calculator = null;
         }

         if (this.cc != null) {
            this.cc.destroy();
            this.cc = null;
         }

         this.fm = null;
         this.mainfm = null;

         for(int var1 = 0; var1 < this.forms.size(); ++var1) {
            ((FormModel)this.forms.get(var1)).destroy();
         }

         this.disabledTemplate = false;
      } catch (Exception var2) {
      }

   }

   public void delForm() {
      Elem var1 = (Elem)this.cc.getActiveObject();
      String var2 = var1.getType();
      int var3 = this.getIndex(this.get(var2));
      int var10002 = this.created[var3]--;
      this.cc.remove(var1);
      this.dirty = true;
   }

   public void callgui(String var1, String[] var2, Object[] var3) {
      try {
         String var4 = "";

         for(int var5 = 0; var5 < ((Object[])var3).length; ++var5) {
            String var6;
            try {
               var6 = ((Object[])var3)[var5].toString();
            } catch (Exception var8) {
               var6 = "";
            }

            var4 = var4 + (var6.length() == 0 ? "" : " ") + var6;
         }

         ((Elem)this.cc.get(this.calcelemindex)).setLabel(var4);
         ((Elem)this.cc.get(this.calcelemindex)).getEtc().put("callgui", var3);
         PropertyList.getInstance().set("prop.dynamic.callguinames", var2);
         GuiUtil.refreshdatacb();
      } catch (Exception var9) {
      }

   }

   public void add(File var1) {
      this.cc.addFile(var1);
   }

   public Vector add2(File var1, boolean var2) {
      this.hasAddError = false;
      this.errormsg = null;
      Vector var3 = new Vector();
      IPropertyList var4 = PropertyList.getInstance();
      if (var1.getAbsolutePath().toLowerCase().endsWith(".imp")) {
         return this.add2imp(var1, var2);
      } else if (var1.getAbsolutePath().toLowerCase().endsWith(".xml")) {
         try {
            BatchFunctions var5 = new BatchFunctions(true, false, true);
            String var6 = var5.cmdOne(var1.getAbsolutePath(), ".xml", false);
            if (var6 == null) {
               this.hasAddError = true;
               var3.add(new TextWithIcon(" Hiba az importáláskor!", 0));
               return var3;
            } else {
               File var7 = new File(var6);
               Vector var8 = this.add2_0(var7, var2);
               var7.delete();
               return var8;
            }
         } catch (Exception var9) {
            var9.printStackTrace();
            this.hasAddError = true;
            var3.add(new TextWithIcon(" Hiba:" + var9.toString(), 0));
            return var3;
         }
      } else {
         return this.add2_0(var1, var2);
      }
   }

   public Vector add2_0(File var1, boolean var2) {
      this.hasAddError = false;
      Vector var3 = new Vector();
      IPropertyList var4 = PropertyList.getInstance();
      CachedCollection var5 = new CachedCollection();
      var5.setNosettitle(true);

      try {
         String var6 = System.getProperty("user.name", "");
         var5.init(var4.get("prop.usr.root") + File.separator + var4.get("prop.usr.tmp") + File.separator + (MainFrame.pid != null ? "cache2_" + MainFrame.pid + ".cache" : "cache2_" + var6 + ".cache"));
      } catch (Exception var19) {
         var19.printStackTrace();
      }

      boolean var20 = this.silent;
      this.silent = var2;
      var5.bm = this;
      var5.bm.warninglist = null;
      int var7 = this.getCalcelemindex();
      CachedCollection var8 = this.cc;
      this.cc = var5;
      int[] var9 = new int[this.created.length];
      System.arraycopy(this.created, 0, var9, 0, this.created.length);
      var5.addmode = true;
      var5.load(var1);
      var5.addmode = false;
      System.arraycopy(var9, 0, this.created, 0, this.created.length);
      this.cc = var8;
      this.setCalcelemindex(var7);
      this.silent = var20;
      if (var5.hasError) {
         this.hasAddError = true;
         String var22 = var5.errormsg;
         var5.destroy();
         var3.add(new TextWithIcon("  " + var22));
         GuiUtil.setGlassLabel((String)null);
         return var3;
      } else {
         int var10;
         if (var5.bm.warninglist != null && var5.bm.warninglist.size() != 0) {
            for(var10 = 0; var10 < var5.bm.warninglist.size(); ++var10) {
               String var11 = var5.bm.warninglist.get(var10).toString();
               var3.add(new TextWithIcon("  " + var11, 4));
            }
         }

         for(int var21 = 0; var21 < var5.size(); ++var21) {
            Elem var12 = (Elem)var5.get(var21);
            String var13 = var12.getType();
            var10 = -1;

            for(int var14 = 0; var14 < this.forms.size(); ++var14) {
               if (((FormModel)this.forms.get(var14)).id.equals(var13)) {
                  var10 = var14;
                  break;
               }
            }

            if (var10 != -1) {
               if (this.created[var10] < this.maxcreation[var10]) {
                  Integer var23 = this.cc.getSequence();
                  var12.getEtc().put("sn", var23);
                  this.cc.add(var12);
                  int var10002 = this.created[var10]++;
                  String var15 = "";
                  String var16 = "";

                  try {
                     var15 = (String)var12.getFejlec().get("mvname");
                     var16 = (String)var12.getFejlec().get("mvtaxid");
                  } catch (Exception var18) {
                  }

                  if (var15 == null) {
                     var15 = "";
                  }

                  if (var16 == null) {
                     var16 = "";
                  }

                  if (var15.equals("") && var16.equals("")) {
                     var3.add(new TextWithIcon("  " + var13 + " Sikeresen hozzáadva!", 3));
                  } else {
                     var3.add(new TextWithIcon("  " + var13 + " " + var15 + " (" + var16 + ") Sikeresen hozzáadva!", 3));
                  }
               } else {
                  var3.add(new TextWithIcon("  " + var13 + " Limit túllépés miatt nem adható hozzá!"));
               }
            }
         }

         var5.destroy();
         GuiUtil.setGlassLabel((String)null);
         return var3;
      }
   }

   private Vector add2imp(File var1, boolean var2) {
      Vector var3 = new Vector();
      Object var4 = this.cc.getActiveObject();
      int var5 = this.getCalcelemindex();
      ImpLoader var6 = new ImpLoader();
      String var7 = var6.loadIntoBm(var1.getAbsolutePath(), this);
      if (var7 == null) {
         CalculatorManager.getInstance().form_calc();
      }

      this.setCalcelemindex(var5);
      this.cc.setActiveObject(var4);
      if (var7 == null) {
         if (this.errormsg != null) {
            try {
               String[] var8 = this.errormsg.split(";");

               for(int var9 = 0; var9 < var8.length; ++var9) {
                  var3.add(new TextWithIcon(var8[var9], 4));
               }
            } catch (Exception var10) {
            }
         }

         var3.add(new TextWithIcon(" Sikeresen hozzáadva!", 3));
      } else {
         var3.add(new TextWithIcon(" " + var7, 0));
      }

      return var3;
   }

   public void do_page_check(String var1) {
   }

   public String getHistorydata(String var1, int var2) {
      try {
         Elem var3 = (Elem)this.cc.get(this.calcelemindex);
         Datastore_history var4 = (Datastore_history)var3.getEtc().get("history");
         Vector var5 = var4.get(var1);
         return var5.get(var2).toString();
      } catch (Exception var6) {
         return null;
      }
   }

   public String getMainHistorydata(String var1, int var2) {
      try {
         Elem var3 = this.get_main_elem();
         Datastore_history var4 = (Datastore_history)var3.getEtc().get("history");
         Vector var5 = var4.get(var1);
         return var5.get(var2).toString();
      } catch (Exception var6) {
         return null;
      }
   }

   public String getBarcode() {
      return this.barcode;
   }

   public void setBarcode(String var1) {
      this.barcode = var1;
   }

   public String getOperationMode() {
      return MainFrame.opmode;
   }

   public String getRole() {
      return MainFrame.role;
   }

   public boolean isOnyaCheckMode() {
      return MainFrame.onyaCheckMode;
   }

   public boolean isBiz17EditMode() {
      return MainFrame.isBiz17EditMode();
   }

   public String getHasznalatiMod() {
      return MainFrame.hasznalati_mod;
   }

   public String getRole_kalkulalt() {
      return MainFrame.ubev_inditasi_kalkulalt;
   }

   public void setBizt_azon(String var1) {
      this.bizt_azon = var1;
   }

   public String getBizt_azon() {
      return this.bizt_azon;
   }

   public String getEvent_azon() {
      return this.event_azon;
   }

   public void setEvent_azon(String var1) {
      this.event_azon = var1;
   }

   public String getErkeztetesiDatum() {
      return MainFrame.erkdatum;
   }

   public String getAlairas() {
      return MainFrame.hasSignature;
   }

   public String getVersion() {
      try {
         return (String)this.docinfo.get("ver");
      } catch (Exception var2) {
         return null;
      }
   }

   public String getTemplateId() {
      try {
         return (String)this.docinfo.get("id");
      } catch (Exception var2) {
         return null;
      }
   }

   public String getBlacklistId() {
      try {
         return (String)this.docinfo.get("org") + (String)this.docinfo.get("id");
      } catch (Exception var2) {
         return null;
      }
   }

   public void setTesztmode(boolean var1) {
      this.tesztmode = var1;
   }

   public boolean getTesztmode() {
      return MainFrame.ubevtesztmode;
   }

   public String getIgazgatosagi_kod() {
      return this.igazgatosagi_kod;
   }

   public void setIgazgatosagi_kod(String var1) {
      this.igazgatosagi_kod = var1;
   }

   public String getAdozovaljavit() {
      return MainFrame.adozovaljavit;
   }

   public void setAdozovaljavit(String var1) {
      MainFrame.adozovaljavit = var1;
   }

   public void showDialog(JFrame var1) {
      if (!this.single) {
         String[] var2 = (String[])((String[])PropertyList.getInstance().get("prop.dynamic.callguinames"));
         Vector var3 = new Vector();
         String var4 = "TípusWWW";
         var3.add("Típus");

         for(int var5 = 1; var5 < var2.length; ++var5) {
            var3.add(var2[var5]);
            var4 = var4 + var2[var5] + "WWW";
         }

         var3.add("Megjegyzés");
         var4 = var4 + "MegjegyzésWWW";
         var3.add("Címke");
         var4 = var4 + "WWWWWWWWWWWWWWWWWWWWWWWWW";
         DefaultTableModel var24 = new DefaultTableModel(var3, 0);

         for(int var6 = 0; var6 < this.cc.size(); ++var6) {
            Elem var7 = (Elem)this.cc.getquick(var6);
            Vector var8 = new Vector();
            var8.add(var7.getType());
            Object[] var9 = (Object[])((Object[])var7.getEtc().get("callgui"));

            for(int var10 = 1; var10 < var2.length; ++var10) {
               Object var11 = null;

               try {
                  var11 = var9[var10];
               } catch (Exception var23) {
                  var11 = "";
               }

               var8.add(var11);
            }

            var8.add(var7.getEtc().get("orignote"));
            var8.add(var7);
            var24.addRow(var8);
         }

         if (var24 != null) {
            final JTable var25 = new JTable(var24) {
               public boolean isCellEditable(int var1, int var2) {
                  return false;
               }
            };
            var25.setTableHeader(new TooltipTableHeader(var25.getColumnModel()));
            var25.setSelectionMode(0);
            var25.addMouseListener(new MouseAdapter() {
               public void mouseClicked(MouseEvent var1) {
                  if (var1.getClickCount() == 2) {
                     BookModel.this.choose_done(var25);
                  }

               }
            });
            final TableSorter var26 = new TableSorter();
            var26.attachTable(var25);
            var26.setSortEnabled(true);
            JScrollPane var27 = new JScrollPane(var25);
            String var28 = "Nyomtatványok  ( " + var24.getRowCount() + " db. )";
            GuiUtil.setTableColWidth(var25);
            final JDialog var29 = new JDialog((Frame)(var1 == null ? MainFrame.thisinstance : var1), var28, true);
            var29.getContentPane().add(var27);
            final TableFilterPanel var30 = new TableFilterPanel((JTable)null);
            var30.getComponent("file_filters_lbl").setVisible(false);
            var30.getComponent("file_filters_scp").setVisible(false);
            final Component var13 = var30.getComponent("file_filters_toggle_btn");
            Component var14 = var30.getComponent("filter_title_lbl");
            final int var15 = GuiUtil.getW(var4);
            final MouseAdapter var16 = new MouseAdapter() {
               public void mouseClicked(MouseEvent var1) {
                  JLabel var2 = (JLabel)var13;
                  if (var2.getIcon() instanceof DownArrow) {
                     var30.setSize(new Dimension(var15, GuiUtil.getCommonItemHeight() + 8));
                     var30.getComponent("clear_filters").setVisible(false);
                  } else {
                     var30.setSize(new Dimension(var15, 6 * (GuiUtil.getCommonItemHeight() + 4)));
                     var30.getComponent("clear_filters").setVisible(true);
                  }

                  var30.setPreferredSize(var30.getSize());
                  var30.setMinimumSize(var30.getSize());
                  var29.getContentPane().invalidate();
                  var29.getContentPane().validate();
                  var29.getContentPane().repaint();
               }
            };
            var13.addMouseListener(var16);
            var14.addMouseListener(var16);
            GuiUtil.setTableColWidth(var25);
            var25.getColumnModel().getColumn(4).setMinWidth(GuiUtil.getW("WWWWWWWWWWWWWWWWWWWWWWWWW"));
            var25.getColumnModel().getColumn(4).setPreferredWidth(GuiUtil.getW("WWWWWWWWWWWWWWWWWWWWWWWWW"));
            var30.setPreferredSize(new Dimension(var15, 8 * GuiUtil.getCommonItemHeight()));
            Vector var17 = new Vector();
            var17.add(new Integer(1));
            var17.add(new Integer(2));
            Object[] var18 = new Object[]{var25, var17, new Integer(3)};
            var30.getBusinessHandler().initials(var18);
            var29.getContentPane().add(var30, "North");
            JButton var19 = new JButton("Bezár");
            var19.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent var1) {
                  try {
                     var13.removeMouseListener(var16);
                     var26.detachTable();
                     var30.destroy();
                     var29.setVisible(false);
                     var29.dispose();
                  } catch (Exception var3) {
                     Tools.eLog(var3, 0);
                  }

               }
            });
            JButton var20 = new JButton("Kiválaszt");
            var20.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent var1) {
                  BookModel.this.choose_done(var25);
               }
            });
            JButton var21 = new JButton("Táblázat nyomtatása");
            var21.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent var1) {
                  try {
                     var25.print();
                  } catch (PrinterException var3) {
                     var3.printStackTrace();
                  }

               }
            });
            JPanel var22 = new JPanel();
            var22.add(var21);
            var22.add(var20);
            var22.add(var19);
            var29.getContentPane().add(var22, "South");
            var29.setSize(var15, 20 * GuiUtil.getCommonItemHeight());
            var29.setPreferredSize(var29.getSize());
            var29.setMinimumSize(var29.getSize());
            var29.setLocationRelativeTo(MainFrame.thisinstance);
            var29.setVisible(true);
         }
      }
   }

   private void choose_done(JTable var1) {
      try {
         int var2 = var1.getSelectedRow();
         if (var2 == -1) {
            return;
         }

         Elem var3 = (Elem)var1.getValueAt(var2, var1.getColumnCount() - 1);
         if (var3.equals(this.cc.getActiveObject())) {
            return;
         }

         GuiUtil.showelem(var3);
      } catch (Exception var4) {
      }

   }

   public boolean isPartFormEditing() {
      return MainFrame.isPart;
   }

   public boolean isSingle() {
      return this.single;
   }

   public void setCcLoadedFile(File var1) {
      this.cc.setLoadedfile(var1);
   }

   public String getVeszprole() {
      return MainFrame.veszprole;
   }

   public String getMuvelet() {
      try {
         return this.cc.muvelet;
      } catch (Exception var2) {
         return null;
      }
   }

   public boolean getBatchRecalcMode() {
      return MainFrame.batch_recalc_mode;
   }

   public String getBatchRecalcRole() {
      return MainFrame.batch_recalc_role;
   }

   public Map<String, String> getPanidsValueFromDocument(String var1) {
      HashMap var2 = new HashMap();
      Vector var3 = new Vector();
      var3.add("panids");
      Vector var4 = MetaInfo.getInstance().getMetaStore(var1).getFilteredFieldMetas_And(var3);
      IDataStore var5 = null;

      int var6;
      for(var6 = 0; var6 < this.cc.size(); ++var6) {
         if (((Elem)this.cc.get(var6)).getType().equals(var1)) {
            var5 = (IDataStore)((Elem)this.cc.get(var6)).getRef();
            break;
         }
      }

      for(var6 = 0; var6 < var4.size(); ++var6) {
         Hashtable var7 = (Hashtable)var4.get(var6);
         String var8 = var5.get("0_" + var7.get("fid"));
         var8 = var8 == null ? "" : var8;
         var2.put((String)var7.get("panids"), var8);
      }

      return var2;
   }

   public String getBffsKod() {
      try {
         IDbHandler var1 = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.DefaultDbHandler");
         return var1.getBffsKod();
      } catch (Exception var2) {
         return null;
      }
   }

   public Vector getChangedBrszAzons() {
      Vector var1 = new Vector();

      try {
         for(int var2 = 0; var2 < this.cc.size(); ++var2) {
            Elem var3 = (Elem)this.cc.get(var2);
            GUI_Datastore var4 = (GUI_Datastore)var3.getRef();
            Hashtable var5 = var3.getEtc();
            String[] var6 = (String[])((String[])var5.get("dbparams"));
            String var7 = var6[1];
            if (var4.hasChangedValue()) {
               var1.add(var7);
            }
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      return var1;
   }

   public boolean isAvdhModel() {
      try {
         return this.docinfo.containsKey("avdh_cst");
      } catch (Exception var2) {
         return false;
      }
   }

   public boolean isCegkapuModel() {
      try {
         return this.docinfo.containsKey("avdh_cst");
      } catch (Exception var2) {
         return false;
      }
   }

   public int carryOnTemplate() {
      try {
         if (this.docinfo.containsKey("check_valid") && "true".equals(this.docinfo.get("check_valid"))) {
            Hatalyossag var1 = UpgradeManager.isOperative(this);
            switch(var1) {
            case HATALYOS:
               return 1;
            case NEM_HATALYOS:
               return 0;
            case NEM_MEGALLAPITHATO:
            }
         }

         return 2;
      } catch (Exception var2) {
         return 3;
      }
   }

   public boolean isCheckValid() {
      return Boolean.valueOf((String)this.docinfo.get("check_valid"));
   }

   public String getPublisherOrgId() {
      return (String)this.docinfo.get("org");
   }

   public String getOrgId() {
      return OrgHandler.getInstance().getReDirectedOrgId((String)this.docinfo.get("org"));
   }

   private void handleOrgCheckValid() {
      OrgResource var1 = (OrgResource)((Hashtable)OrgInfo.getInstance().getOrgList()).get(this.docinfo.get("org"));
      if ("true".equals(var1.getOrgCheckValid())) {
         this.docinfo.put("check_valid", "true");
      }

   }

   public boolean isNewStylePageBreak() {
      return this.newStylePageBreak;
   }

   public String getTaxNumberHKAzonFromDocInfo() {
      String var1 = "";

      try {
         Hashtable var2 = this.cc.getHeadData(this.cc.getLoadedfile());
         if (var2.containsKey("docinfo")) {
            var2 = (Hashtable)var2.get("docinfo");
            if (var2.containsKey("tax_number")) {
               var1 = (String)var2.get("tax_number");
            }
         }
      } catch (Exception var3) {
         var1 = "";
      }

      return var1.length() < 8 ? var1 : var1.substring(0, 8);
   }

   public boolean isInTextArea(String var1) {
      if (var1 == null) {
         return false;
      } else {
         int var2 = var1.indexOf("_");
         if (var2 > -1) {
            var1 = var1.substring(var2 + 1);
         }

         if (this.fidTextAreaMap.containsKey(var1)) {
            return true;
         } else {
            String var3 = var1.length() < 10 ? var1 : var1.substring(0, 2) + "XXXX" + var1.substring(6);
            return this.fidTextAreaMap.containsKey(var3);
         }
      }
   }

   public boolean isDisabledTemplate() {
      return this.disabledTemplate;
   }

   public void setDisabledTemplate(boolean var1) {
      this.disabledTemplate = var1;
   }

   public ArrayList<String> getTextAreaFidList(String var1) {
      ArrayList var2 = new ArrayList();
      if (var1 == null) {
         return var2;
      } else {
         int var3 = var1.indexOf("_");
         if (var3 > -1) {
            var1 = var1.substring(var3 + 1);
         }

         if (this.textAreaFidMap.containsKey(var1)) {
            var2 = (ArrayList)this.textAreaFidMap.get(var1);
         } else if (this.fidTextAreaMap.containsKey(var1)) {
            var2 = (ArrayList)this.textAreaFidMap.get(this.fidTextAreaMap.get(var1));
         } else {
            String var4 = var1.length() < 10 ? var1 : var1.substring(0, 2) + "XXXX" + var1.substring(6);
            if (this.textAreaFidMap.containsKey(var4)) {
               var2 = (ArrayList)this.textAreaFidMap.get(var4);
            }

            if (this.fidTextAreaMap.containsKey(var4)) {
               var2 = (ArrayList)this.textAreaFidMap.get(this.fidTextAreaMap.get(var4));
            }
         }

         if (var2.size() > 0) {
            var2.set(0, var1);
         }

         return var2;
      }
   }
}
