package hu.piller.enykp.alogic.fileloader.xml;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.filesaver.xml.ErrorListListener4XmlSave;
import hu.piller.enykp.alogic.fileutil.DataChecker;
import hu.piller.enykp.alogic.fileutil.DatastoreKeyToXml;
import hu.piller.enykp.alogic.fileutil.ExtendedTemplateData;
import hu.piller.enykp.alogic.fileutil.HeadChecker;
import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.settingspanel.upgrade.UpgradeFunction;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.alogic.templateutils.blacklist.BlacklistStore;
import hu.piller.enykp.datastore.CachedCollection;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.GUI_Datastore;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.gui.model.VisualFieldModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.ILoadManager;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import java.awt.Component;
import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.Icon;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLFlyCheckLoader implements ILoadManager {
   boolean debug = false;
   String loaderid = "xml_data_loader_v1";
   String suffix = ".xml";
   String description = "XML állomány";
   public File loadedfile;
   Elem lc_elem;
   public BookModel bm;
   public CachedCollection _this;
   public Hashtable head;
   public Hashtable docinfo;
   public Hashtable nyomtatvanyok;
   public String abev_hibakszama;
   public String abev_hash;
   public String abev_programverzio;
   public String l_nyomtatvanyazonosito;
   public String l_nyomtatvanyverzio;
   public String l_tol;
   public String l_ig;
   public String l_megjegyzes;
   public String l_formnote;
   public String a_adoszam;
   public String a_adoazonosito;
   public String a_nev;
   public String mv_adoszam;
   public String mv_adoazonosito;
   public String mv_nev;
   public String al_megnevezes;
   public String al_azonosito;
   String mezokey;
   String mezovalue;
   IDataStore newdatastore;
   FormModel lc_fm;
   int[] pc;
   private int curindex;
   private boolean single = true;
   private boolean onlyhead = false;
   private boolean in_type;
   private boolean in_saved;
   private boolean in_hibakszama;
   private boolean in_hash;
   private boolean in_programverzio;
   private boolean in_adozo;
   private boolean in_mezo;
   private boolean in_nyomtatvanyazonosito;
   private boolean in_nyomtatvanyverzio;
   private boolean in_tol;
   private boolean in_ig;
   private boolean in_megjegyzes;
   private boolean in_a_adoszam;
   private boolean in_a_adoazonosito;
   private boolean in_a_nev;
   private boolean in_mv_adoszam;
   private boolean in_mv_adoazonosito;
   private boolean in_mv_nev;
   private boolean in_albizonylat;
   private boolean in_al_megnevezes;
   private boolean in_al_azonosito;
   private boolean in_firstform;
   public boolean fatalerror;
   public boolean headcheckfatalerror;
   public boolean needwarninghead;
   public boolean hasError;
   public String errormsg;
   public Vector headerrorlist;
   public Vector warninglist;
   public boolean silent = false;
   public boolean silentheadcheck = false;
   private int skipcount;
   ErrorListListener4XmlSave ell4xs;
   Vector errorVector;

   public XMLFlyCheckLoader() {
      IErrorList var1 = ErrorList.getInstance();
      this.ell4xs = new ErrorListListener4XmlSave(-1);
      boolean var2 = false;
      this.ell4xs.clearErrorList();
      ((IEventSupport)var1).addEventListener(this.ell4xs);
   }

   public Vector getErrorVector() {
      return this.errorVector;
   }

   public boolean checksuffix(File var1) {
      return !var1.getAbsolutePath().toLowerCase().endsWith(this.suffix);
   }

   public String getId() {
      return this.loaderid;
   }

   public String getDescription() {
      return this.description;
   }

   public Hashtable getHeadData(File var1) {
      try {
         if (this.checksuffix(var1)) {
            return null;
         } else {
            FileInputStream var2 = new FileInputStream(var1);
            String var3 = this.getEncoding(var1);
            if (var3 == null) {
               var3 = "utf-8";
            }

            Hashtable var4 = this.getHeadData(var2, var3);

            try {
               SimpleDateFormat var5 = new SimpleDateFormat("yyyyMMddkkmmss");
               if (var1 != null) {
                  var4.put("saved", var5.format(new Date(var1.lastModified())));
               } else {
                  var4.put("saved", var5.format(new Date()));
               }
            } catch (Exception var6) {
               Tools.eLog(var6, 0);
            }

            return var4;
         }
      } catch (FileNotFoundException var7) {
         var7.printStackTrace();
         return null;
      }
   }

   public Hashtable getHeadData(InputStream var1, String var2) {
      this.onlyhead = true;
      this.hasError = false;
      this.clearall();
      this.coreLoad(var1, var2);

      try {
         Hashtable var3 = new Hashtable();
         if (this.l_nyomtatvanyazonosito == null) {
            this.l_nyomtatvanyazonosito = (String)this.head.get("nyomtatvanyazonosito");
         }

         if (this.l_nyomtatvanyazonosito != null) {
            var3.put("name", this.l_nyomtatvanyazonosito);
         }

         if (this.l_nyomtatvanyazonosito != null) {
            var3.put("id", this.l_nyomtatvanyazonosito);
         }

         if (this.l_megjegyzes != null) {
            var3.put("note", this.l_megjegyzes);
         }

         if (this.l_tol != null) {
            var3.put("from_date", this.l_tol);
         }

         if (this.l_ig != null) {
            var3.put("to_date", this.l_ig);
         }

         if (this.a_adoszam != null) {
            var3.put("tax_number", this.a_adoszam);
         }

         if (this.a_nev != null) {
            var3.put("person_name", this.a_nev);
         }

         if (this.l_nyomtatvanyverzio != null) {
            var3.put("templatever", this.l_nyomtatvanyverzio);
         } else {
            String var7 = (String)this.head.get("nyomtatvanyverzio");
            if (var7 == null) {
               var7 = "";
            }

            var3.put("ver", var7);
         }

         var3.put("org", "Nem definiált");
         var3.put("state", "Módosítható");
         if (this.hasError && this.errormsg != null) {
            var3.put("info", this.errormsg);
         }

         this.head.clear();
         this.head.put("head", "");
         this.head.put("type", "single");
         this.head.put("docinfo", var3);
         this.head.put("doc", new Vector());
         return this.head;
      } catch (Exception var6) {
         Hashtable var4 = new Hashtable();
         Hashtable var5 = new Hashtable();
         var5.put("name", "HIBÁS ÁLLOMÁNY !");
         var4.put("head", "");
         var4.put("type", "single");
         var4.put("docinfo", var5);
         var4.put("doc", new Vector());
         if (this.hasError && this.errormsg != null) {
            var5.put("info", this.errormsg);
         }

         return var4;
      }
   }

   public String getFileNameSuffix() {
      return this.suffix;
   }

   public String createFileName(String var1) {
      return var1 == null ? null : (var1.toLowerCase().trim().endsWith(this.suffix) ? var1.trim() : var1.trim() + this.suffix);
   }

   public BookModel load(String var1, String var2, String var3, String var4, BookModel var5) {
      if (var5 != null && var5.loadedfile != null) {
         this.silent = true;
         BookModel var6 = new BookModel();
         var6.hasError = true;

         try {
            File var7 = new File(var1);
            if (var2 == null) {
               var6.errormsg = "Nincs nyomtatványazonosító az xml állományban!";
               return var6;
            } else {
               ExtendedTemplateData var8 = TemplateChecker.getInstance().getTemplateFileNames(var2, var3, var4);
               String[] var9;
               if (var8.isTemplateDisaled()) {
                  var9 = BlacklistStore.getInstance().getErrorListMessage(var2, var4);
                  var6.errormsg = var9[0] + " bl_url " + var9[1];
                  var6.setDisabledTemplate(true);
                  return var6;
               } else {
                  var9 = var8.getTemplateFileNames();
                  File var10 = new File(var9[0]);
                  if (!var10.exists()) {
                     var6.errormsg = var9[2];
                     return var6;
                  } else {
                     if (var5.loadedfile.getAbsolutePath().equals(var10.getAbsolutePath())) {
                        this.bm = var5;
                        this.bm.reempty();
                     } else {
                        var5.destroy();
                        this.bm = new BookModel(var10);
                     }

                     if (this.bm.hasError) {
                        return this.bm;
                     } else {
                        this._this = this.bm.cc;
                        this.load(var7);
                        if (this.hasError) {
                           var6.errormsg = this.errormsg;
                           return var6;
                        } else {
                           this.bm.cc.setLoadedfile(var7);
                           this.bm.cc.l_megjegyzes = this.l_megjegyzes;
                           if (this.warninglist.size() != 0) {
                              if (this.silent) {
                                 this.bm.warninglist = this.warninglist;
                              } else {
                                 GuiUtil.showErrorDialog((Frame)null, "Hibák és figyelmeztetések", true, true, this.warninglist, var7);
                              }
                           }

                           return this.bm;
                        }
                     }
                  }
               }
            }
         } catch (Exception var11) {
            var6.errormsg = var11.getMessage();
            return var6;
         }
      } else {
         return this.load(var1, var2, var3, var4);
      }
   }

   public BookModel load(String var1, String var2, String var3, String var4) {
      long var5 = 0L;
      long var7 = 0L;
      if (this.debug) {
         var5 = System.currentTimeMillis();
      }

      BookModel var9 = this.load(var1, var2, var3, var4, true);
      if (this.debug) {
         var7 = System.currentTimeMillis();
         System.out.println("load time = " + (var7 - var5) / 1000L + " sec");
      }

      return var9;
   }

   public BookModel load(String var1, String var2, String var3, String var4, boolean var5) {
      this.silent = var5;
      BookModel var6 = new BookModel();
      var6.hasError = true;

      try {
         File var7 = new File(var1);
         if (var2 == null) {
            var6.errormsg = "Nincs nyomtatványazonosító az xml állományban!";
            return var6;
         } else {
            ExtendedTemplateData var8 = TemplateChecker.getInstance().getTemplateFileNames(var2, var3, var4, true, UpgradeFunction.XML_CHKUPL);
            String[] var9;
            if (var8.isTemplateDisaled()) {
               var6.setDisabledTemplate(true);
               var9 = BlacklistStore.getInstance().getErrorListMessage(var8.getTemplateId(), var8.getOrgId());
               var6.errormsg = var9[0] + " bl_url " + var9[1];
               return var6;
            } else {
               var9 = var8.getTemplateFileNames();
               File var10;
               if (!var5 && this.checkIfTemplatesAreDifferent(var9)) {
                  if (GuiUtil.showOptionDialog((Component)null, "A megnyitni kívánt nyomtatványból újabb verzió is létezik. Kívánja az újjal megnyitni az állományt?", "Nyomtatványverzió választás", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0) {
                     var10 = new File(var9[0]);
                     if (!var10.exists()) {
                        throw new Exception();
                     }

                     PropertyList.getInstance().set("prop.dynamic.hasNewTemplate", Boolean.TRUE);
                  } else {
                     if (var9[1].equals("")) {
                        PropertyList.getInstance().set("prop.dynamic.templateLoad.userTerminated", true);
                        return null;
                     }

                     var10 = new File(var9[1]);
                  }
               } else {
                  var10 = new File(var9[0]);
               }

               if (!var10.exists()) {
                  var6.errormsg = var9[2];
                  return var6;
               } else {
                  this.bm = new BookModel(var10);
                  if (this.bm.hasError) {
                     return this.bm;
                  } else {
                     this._this = this.bm.cc;
                     this.bm.cc.setLoadedfile(var7);
                     this.load(var7);
                     if (this.hasError) {
                        var6.errormsg = this.errormsg;
                        return var6;
                     } else {
                        this.bm.cc.l_megjegyzes = this.l_megjegyzes;
                        if (this.warninglist.size() != 0) {
                           if (var5) {
                              this.bm.warninglist = this.warninglist;
                           } else {
                              GuiUtil.showErrorDialog((Frame)null, "Hibák és figyelmeztetések", true, true, this.warninglist, var7);
                           }
                        }

                        return this.bm;
                     }
                  }
               }
            }
         }
      } catch (Exception var11) {
         var6.errormsg = var11.getMessage();
         return var6;
      }
   }

   public void load(File var1) {
      this.onlyhead = false;
      this._load(var1);

      try {
         if (this.head == null) {
            return;
         }

         SimpleDateFormat var2 = new SimpleDateFormat("yyyyMMddkkmmss");
         if (var1 != null) {
            this.head.put("saved", var2.format(new Date(var1.lastModified())));
         } else {
            this.head.put("saved", var2.format(new Date()));
         }
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
      }

   }

   private void _load(File var1) {
      String var3;
      try {
         this.clearall();
         this.warninglist = new Vector();
         this.hasError = false;
         this.loadedfile = var1;
         FileInputStream var2 = null;
         var3 = this.getEncoding(var1);
         if (var3 == null) {
            var3 = "utf-8";
         }

         var2 = new FileInputStream(var1);
         this.coreLoad(var2, var3);
      } catch (Exception var4) {
         var3 = var4.getMessage();
         if (var3 == null) {
            this.hasError = true;
            if (this.errormsg == null) {
               this.errormsg = var4.toString();
            }

            var4.printStackTrace();
         }
      }

   }

   public void coreLoad(InputStream var1, String var2) {
      this.clearall();
      this.warninglist = new Vector();
      this.hasError = false;
      this.curindex = 0;
      this.in_type = false;
      this.in_saved = false;
      this.in_hibakszama = false;
      this.in_hash = false;
      this.in_programverzio = false;
      this.in_adozo = false;
      this.in_mezo = false;
      this.in_nyomtatvanyazonosito = this.in_nyomtatvanyverzio = this.in_tol = this.in_ig = this.in_megjegyzes = false;
      this.in_a_adoszam = this.in_a_adoazonosito = this.in_a_nev = this.in_mv_adoszam = this.in_mv_adoazonosito = this.in_mv_nev = false;
      this.in_albizonylat = this.in_al_megnevezes = this.in_al_azonosito = false;
      this.in_firstform = true;
      this.fatalerror = false;
      this.headcheckfatalerror = false;

      try {
         Object var3;
         if (this.onlyhead) {
            var3 = new XMLFlyCheckLoader.headhandler();
         } else {
            var3 = new XMLFlyCheckLoader.bodyhandler();
         }

         InputSource var10 = new InputSource(var1);
         var10.setEncoding(var2);
         if (this.skipcount != 0) {
            var1.skip((long)this.skipcount);
         }

         SAXParser var6;
         SAXParserFactory var12;
         if (this.onlyhead) {
            var12 = SAXParserFactory.newInstance();
            var12.setNamespaceAware(true);
            var12.setValidating(true);
            var6 = var12.newSAXParser();
            var6.parse(var10, (DefaultHandler)var3);
            var1.close();
            return;
         }

         if (!this.needxsdcheck(this.bm.id)) {
            var12 = SAXParserFactory.newInstance();
            var12.setNamespaceAware(true);
            var12.setValidating(true);
            var6 = var12.newSAXParser();
            var6.parse(var10, (DefaultHandler)var3);
            var1.close();
            return;
         }

         DefaultXMLParser var11 = new DefaultXMLParser();
         DefaultXMLParser.silent = true;
         var11.setContentHandler((ContentHandler)var3);
         var11.parse(var10);
         var1.close();
      } catch (Exception var9) {
         String var4 = var9.getMessage();
         if (var4 == null) {
            this.hasError = true;
            if (this.errormsg == null) {
               this.errormsg = var9.toString();
            }

            var9.printStackTrace();
         } else if (!var4.equals("OUT")) {
            this.hasError = true;
            if (var4.equals("HEADCHECK")) {
               try {
                  this.errormsg = (String)this.headerrorlist.get(0);
               } catch (Exception var8) {
                  Tools.eLog(var9, 0);
               }
            } else {
               if (this.errormsg == null) {
                  this.errormsg = var9.getMessage();
               }

               if (var9 instanceof SAXParseException) {
                  SAXParseException var5 = (SAXParseException)var9;
                  this.errormsg = "Súlyos hiba az XML formai ellenőrzése során: " + this.errormsg + "##" + "sor: " + var5.getLineNumber() + "  oszlop: " + var5.getColumnNumber();
               }
            }
         }

         try {
            var1.close();
         } catch (IOException var7) {
         }
      }

   }

   private boolean needxsdcheck(String var1) {
      if ("1".equals(MainFrame.opmode)) {
         return false;
      } else {
         return !XMLPost.inxmldisplay;
      }
   }

   private void attributes_done(Attributes var1, Hashtable var2) {
      for(int var3 = 0; var3 < var1.getLength(); ++var3) {
         var2.put(var1.getQName(var3), var1.getValue(var3));
      }

   }

   public String getEncoding(File var1) {
      FileInputStream var2 = null;

      try {
         this.skipcount = 0;
         var2 = new FileInputStream(var1);
         int var3 = var2.read();
         if (var3 == 255) {
            return "UTF-16LE";
         } else if (var3 == 254) {
            return "UTF-16BE";
         } else {
            if (var3 == 239) {
               this.skipcount = 3;
            }

            byte[] var4 = new byte[256];
            var2.read(var4);
            String var5 = new String(var4);
            int var6 = var5.indexOf("encoding=");
            int var7 = var5.indexOf("\"", var6);
            int var8 = var5.indexOf("\"", var7 + 1);
            if (var7 == -1) {
               var7 = var5.indexOf("'", var6);
               var8 = var5.indexOf("'", var7 + 1);
            }

            var2.close();
            return var5.substring(var7 + 1, var8);
         }
      } catch (Exception var10) {
         try {
            var2.close();
         } catch (IOException var9) {
         }

         return null;
      }
   }

   private void clearall() {
      this.head = this.docinfo = this.nyomtatvanyok = null;
      this.clearvars();
      this.hasError = false;
      this.errormsg = null;
      this.l_megjegyzes = null;
      this.l_formnote = null;
   }

   private void clearvars() {
      this.abev_hibakszama = this.abev_hash = this.abev_programverzio = null;
      this.l_nyomtatvanyazonosito = this.l_nyomtatvanyverzio = this.l_tol = this.l_ig = null;
      this.a_adoszam = this.a_adoazonosito = this.a_nev = this.mv_adoszam = this.mv_adoazonosito = this.mv_nev = null;
      this.al_megnevezes = this.al_azonosito = null;
      this.l_formnote = null;
      this.needwarninghead = true;
   }

   public void destroy() {
      if (this.bm != null) {
         this.bm.destroy();
         this.bm = null;
      }

   }

   private boolean checkIfTemplatesAreDifferent(String[] var1) {
      if (var1.length == 1) {
         return false;
      } else if ("".equals(var1[1])) {
         return false;
      } else {
         return !var1[0].equals(var1[1]);
      }
   }

   boolean checkmezokod(String var1) {
      int var2 = var1.indexOf("XXXX");
      return var2 == -1;
   }

   private Vector saveErrorsFromELL() {
      Vector var1 = new Vector(this.ell4xs.errorList.size());

      for(int var2 = 0; var2 < this.ell4xs.errorList.size(); ++var2) {
         var1.add(this.ell4xs.errorList.get(var2));
      }

      return var1;
   }

   class bodyhandler extends DefaultHandler {
      private boolean roleerrorflag = false;
      HashSet codehs;

      public void characters(char[] var1, int var2, int var3) throws SAXException {
         if (XMLFlyCheckLoader.this.in_mezo) {
            XMLFlyCheckLoader.this.mezovalue = XMLFlyCheckLoader.this.mezovalue + DatastoreKeyToXml.plainConvert(new String(var1, var2, var3));
         } else if (XMLFlyCheckLoader.this.in_type) {
            String var4 = new String(var1, var2, var3);
            XMLFlyCheckLoader.this.head.put("type", var4);
            if (var4.equals("multi")) {
               XMLFlyCheckLoader.this.single = false;
            }
         } else if (XMLFlyCheckLoader.this.in_saved) {
            XMLFlyCheckLoader.this.head.put("saved", new String(var1, var2, var3));
         } else if (XMLFlyCheckLoader.this.in_hibakszama) {
            XMLFlyCheckLoader.this.abev_hibakszama = new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_hash) {
            XMLFlyCheckLoader.this.abev_hash = new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_programverzio) {
            XMLFlyCheckLoader.this.abev_programverzio = new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_nyomtatvanyazonosito) {
            XMLFlyCheckLoader.this.l_nyomtatvanyazonosito = XMLFlyCheckLoader.this.l_nyomtatvanyazonosito + new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_nyomtatvanyverzio) {
            XMLFlyCheckLoader.this.l_nyomtatvanyverzio = XMLFlyCheckLoader.this.l_nyomtatvanyverzio + new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_tol) {
            XMLFlyCheckLoader.this.l_tol = XMLFlyCheckLoader.this.l_tol + new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_ig) {
            XMLFlyCheckLoader.this.l_ig = XMLFlyCheckLoader.this.l_ig + new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_megjegyzes) {
            if (XMLFlyCheckLoader.this.in_firstform) {
               XMLFlyCheckLoader.this.l_megjegyzes = XMLFlyCheckLoader.this.l_megjegyzes + new String(var1, var2, var3);
            }

            XMLFlyCheckLoader.this.l_formnote = XMLFlyCheckLoader.this.l_formnote + new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_a_adoszam) {
            XMLFlyCheckLoader.this.a_adoszam = XMLFlyCheckLoader.this.a_adoszam + new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_a_adoazonosito) {
            XMLFlyCheckLoader.this.a_adoazonosito = XMLFlyCheckLoader.this.a_adoazonosito + new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_a_nev) {
            XMLFlyCheckLoader.this.a_nev = XMLFlyCheckLoader.this.a_nev + new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_mv_adoszam) {
            XMLFlyCheckLoader.this.mv_adoszam = XMLFlyCheckLoader.this.mv_adoszam + new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_mv_adoazonosito) {
            XMLFlyCheckLoader.this.mv_adoazonosito = XMLFlyCheckLoader.this.mv_adoazonosito + new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_mv_nev) {
            XMLFlyCheckLoader.this.mv_nev = XMLFlyCheckLoader.this.mv_nev + new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_al_megnevezes) {
            XMLFlyCheckLoader.this.al_megnevezes = XMLFlyCheckLoader.this.al_megnevezes + new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_al_azonosito) {
            XMLFlyCheckLoader.this.al_azonosito = XMLFlyCheckLoader.this.al_azonosito + new String(var1, var2, var3);
         }

      }

      public void endDocument() throws SAXException {
         XMLFlyCheckLoader.this.bm.cc.setActiveObject(XMLFlyCheckLoader.this.bm.cc.get(0));
         XMLFlyCheckLoader.this.bm.setCalcelemindex(0);
         Vector var1 = XMLFlyCheckLoader.this.saveErrorsFromELL();
         int var2 = XMLFlyCheckLoader.this.ell4xs.getRealError();
         int var3 = XMLFlyCheckLoader.this.ell4xs.getFatalError();
         XMLFlyCheckLoader.this.ell4xs.clearErrorList();
         CalculatorManager.getInstance().do_fly_check_all_one(XMLFlyCheckLoader.this.ell4xs);
         XMLFlyCheckLoader.this.ell4xs.restoreListener(var1, var2, var3);
         CalculatorManager.getInstance().do_fly_check_all_stop();
         IErrorList var4 = ErrorList.getInstance();
         ((IEventSupport)var4).removeEventListener(XMLFlyCheckLoader.this.ell4xs);
         XMLFlyCheckLoader.this.errorVector = XMLFlyCheckLoader.this.ell4xs.getErrorList4XmlFlyCheckLoader();
         XMLFlyCheckLoader.this.bm.setCalcelemindex(0);
         XMLFlyCheckLoader.this.bm.cc.setActiveObject(XMLFlyCheckLoader.this.bm.cc.get(0));
      }

      public void endElement(String var1, String var2, String var3) throws SAXException {
         if (var3.equals("mezo")) {
            try {
               if (this.roleerrorflag) {
                  this.roleerrorflag = false;
                  XMLFlyCheckLoader.this.in_mezo = false;
                  return;
               }

               String var4 = XMLFlyCheckLoader.this.mezokey.substring(XMLFlyCheckLoader.this.mezokey.indexOf("_") + 1);
               if (XMLFlyCheckLoader.this.lc_fm.fids_page.get(var4) != null && ((PageModel)XMLFlyCheckLoader.this.lc_fm.fids_page.get(var4)).role == 0) {
                  XMLFlyCheckLoader.this.fatalerror = true;
                  XMLFlyCheckLoader.this.hasError = true;
                  XMLFlyCheckLoader.this.errormsg = "A megadott állomány nem létező mezőkódot tartalmaz (" + var4 + ")!";
                  return;
               }

               boolean var5 = ((DataFieldModel)XMLFlyCheckLoader.this.bm.get(XMLFlyCheckLoader.this.lc_fm.id).fids.get(var4)).features.get("datatype").equals("check");
               if (var5) {
                  if (XMLFlyCheckLoader.this.mezovalue.equals("X")) {
                     XMLFlyCheckLoader.this.mezovalue = "true";
                  } else if (XMLPost.xmlbooleancheck) {
                     if (XMLFlyCheckLoader.this.mezovalue.equals("")) {
                        XMLFlyCheckLoader.this.mezovalue = "false";
                     } else {
                        XMLFlyCheckLoader.this.fatalerror = true;
                        XMLFlyCheckLoader.this.hasError = true;
                        XMLFlyCheckLoader.this.errormsg = "Érvénytelen logikai érték! Mezőkód = " + var4;
                     }
                  } else {
                     XMLFlyCheckLoader.this.mezovalue = "false";
                  }
               }

               int var6;
               try {
                  var6 = ((PageModel)XMLFlyCheckLoader.this.lc_fm.fids_page.get(var4)).maxpage;
               } catch (Exception var13) {
                  var6 = 1;
               }

               String var7 = XMLFlyCheckLoader.this.mezokey.substring(0, XMLFlyCheckLoader.this.mezokey.indexOf("_"));

               int var8;
               try {
                  var8 = Integer.parseInt(var7);
               } catch (NumberFormatException var12) {
                  var8 = 0;
               }

               if (var6 <= var8) {
                  System.out.println("NagyonSúlyos index hiba!!!!!!!! " + XMLFlyCheckLoader.this.mezokey);
               } else {
                  XMLFlyCheckLoader.this.newdatastore.set(XMLFlyCheckLoader.this.mezokey, XMLFlyCheckLoader.this.mezovalue);
               }
            } catch (Exception var14) {
               Tools.eLog(var14, 0);
            }

            XMLFlyCheckLoader.this.in_mezo = false;
         } else if (var3.equals("nyomtatvany")) {
            if (XMLFlyCheckLoader.this.in_firstform && !XMLFlyCheckLoader.this.bm.main_document_id.equals(XMLFlyCheckLoader.this.l_nyomtatvanyazonosito)) {
               throw new SAXException("Csak főnyomtatvány állhat az első helyen!");
            }

            CalculatorManager.getInstance().doBetoltErtekCalcs(true);
            CalculatorManager.getInstance().form_hidden_fields_calc();
            CalculatorManager.getInstance().multi_form_load();
            HeadChecker var15 = HeadChecker.getInstance();
            Hashtable var16 = new Hashtable();
            if (XMLFlyCheckLoader.this.a_nev != null) {
               var16.put("nev", XMLFlyCheckLoader.this.a_nev);
            }

            if (XMLFlyCheckLoader.this.a_adoszam != null) {
               var16.put("adoszam", XMLFlyCheckLoader.this.a_adoszam);
            }

            if (XMLFlyCheckLoader.this.a_adoazonosito != null) {
               var16.put("adoazonosito", XMLFlyCheckLoader.this.a_adoazonosito);
            }

            if (XMLFlyCheckLoader.this.mv_nev != null) {
               var16.put("munkavallalo/nev", XMLFlyCheckLoader.this.mv_nev);
            }

            if (XMLFlyCheckLoader.this.mv_adoazonosito != null) {
               var16.put("munkavallalo/adoazonosito", XMLFlyCheckLoader.this.mv_adoazonosito);
            }

            if (XMLFlyCheckLoader.this.al_azonosito != null) {
               var16.put("albizonylatazonositas/azonosito", XMLFlyCheckLoader.this.al_azonosito);
            }

            if (XMLFlyCheckLoader.this.al_megnevezes != null) {
               var16.put("albizonylatazonositas/megnevezes", XMLFlyCheckLoader.this.al_megnevezes);
            }

            if (XMLFlyCheckLoader.this.l_tol != null) {
               var16.put("tol", XMLFlyCheckLoader.this.l_tol);
            }

            if (XMLFlyCheckLoader.this.l_ig != null) {
               var16.put("ig", XMLFlyCheckLoader.this.l_ig);
            }

            String var17 = null;

            try {
               var17 = (String)XMLFlyCheckLoader.this.bm.docinfo.get("org");
            } catch (Exception var11) {
               var17 = "Nem definiált";
            }

            Object[] var18 = new Object[]{var16, var17, XMLFlyCheckLoader.this.l_nyomtatvanyazonosito, new Integer(XMLFlyCheckLoader.this.curindex - 1), XMLFlyCheckLoader.this.newdatastore};
            XMLFlyCheckLoader.this.headerrorlist = var15.headCheck(var18, XMLFlyCheckLoader.this.bm);
            if (XMLFlyCheckLoader.this.headerrorlist.size() != 0) {
               if (!XMLFlyCheckLoader.this.silent && !XMLFlyCheckLoader.this.silentheadcheck) {
                  GuiUtil.showErrorDialog((Frame)null, "HeadCheck", true, false, XMLFlyCheckLoader.this.headerrorlist);
               }

               if (!XMLFlyCheckLoader.this.headcheckfatalerror) {
                  XMLFlyCheckLoader.this.headcheckfatalerror = var15.hadFatalError();
               }

               XMLFlyCheckLoader.this.warninglist.addAll(XMLFlyCheckLoader.this.headerrorlist);
            }

            if (XMLFlyCheckLoader.this.in_firstform) {
               CalculatorManager.getInstance().do_fly_check_all_start();
            } else {
               XMLFlyCheckLoader.this.bm.cc.setActiveObject(XMLFlyCheckLoader.this.lc_elem);
               Result var19 = DataChecker.getInstance().superCheck(XMLFlyCheckLoader.this.bm, false, XMLFlyCheckLoader.this.bm.cc.size() - 1);
               if (var19.isOk()) {
                  CalculatorManager.getInstance().do_fly_check_all_one(XMLFlyCheckLoader.this.ell4xs);
               } else {
                  Vector var9 = var19.errorList;

                  for(int var10 = 0; var10 < var9.size(); ++var10) {
                     XMLFlyCheckLoader.this.ell4xs.errorList.add(var9.get(var10));
                  }

                  XMLFlyCheckLoader.this.hasError = true;
               }
            }

            if (XMLFlyCheckLoader.this.lc_elem != null && !XMLFlyCheckLoader.this.lc_elem.noMapped()) {
               XMLFlyCheckLoader.this.lc_elem.setMappedObject((Object)null);
               XMLFlyCheckLoader.this.lc_elem.getEtc().clear();
            }

            XMLFlyCheckLoader.this.in_firstform = false;
         } else if (var3.equals("head")) {
            if (XMLFlyCheckLoader.this.docinfo != null) {
               XMLFlyCheckLoader.this.head.put("docinfo", XMLFlyCheckLoader.this.docinfo);
            } else {
               XMLFlyCheckLoader.this.head.put("docinfo", new Hashtable());
            }
         } else if (var3.equals("type")) {
            XMLFlyCheckLoader.this.in_type = false;
         } else if (var3.equals("saved")) {
            XMLFlyCheckLoader.this.in_saved = false;
         } else if (var3.equals("hibakszama")) {
            XMLFlyCheckLoader.this.in_hibakszama = false;
         } else if (var3.equals("hash")) {
            XMLFlyCheckLoader.this.in_hash = false;
         } else if (var3.equals("programverzio")) {
            XMLFlyCheckLoader.this.in_programverzio = false;
         } else if (var3.equals("nyomtatvanyazonosito")) {
            XMLFlyCheckLoader.this.in_nyomtatvanyazonosito = false;
         } else if (var3.equals("nyomtatvanyverzio")) {
            XMLFlyCheckLoader.this.in_nyomtatvanyverzio = false;
         } else if (var3.equals("tol")) {
            XMLFlyCheckLoader.this.in_tol = false;
         } else if (var3.equals("ig")) {
            XMLFlyCheckLoader.this.in_ig = false;
         } else if (var3.equals("megjegyzes")) {
            XMLFlyCheckLoader.this.in_megjegyzes = false;
         } else if (var3.equals("adoszam")) {
            if (XMLFlyCheckLoader.this.in_adozo) {
               XMLFlyCheckLoader.this.in_a_adoszam = false;
            } else {
               XMLFlyCheckLoader.this.in_mv_adoszam = false;
            }
         } else if (var3.equals("adoazonosito")) {
            if (XMLFlyCheckLoader.this.in_adozo) {
               XMLFlyCheckLoader.this.in_a_adoazonosito = false;
            } else {
               XMLFlyCheckLoader.this.in_mv_adoazonosito = false;
            }
         } else if (var3.equals("nev")) {
            if (XMLFlyCheckLoader.this.in_adozo) {
               XMLFlyCheckLoader.this.in_a_nev = false;
            } else {
               XMLFlyCheckLoader.this.in_mv_nev = false;
            }
         } else if (var3.equals("adozo")) {
            XMLFlyCheckLoader.this.in_adozo = false;
         } else if (var3.equals("albizonylatazonositas")) {
            XMLFlyCheckLoader.this.in_albizonylat = false;
         } else if (var3.equals("megnevezes")) {
            XMLFlyCheckLoader.this.in_al_megnevezes = false;
         } else if (var3.equals("azonosito")) {
            XMLFlyCheckLoader.this.in_al_azonosito = false;
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
      }

      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         int var7;
         if (var3.equals("mezo")) {
            try {
               this.roleerrorflag = false;
               XMLFlyCheckLoader.this.mezovalue = "";
               XMLFlyCheckLoader.this.in_mezo = true;
               XMLFlyCheckLoader.this.mezokey = var4.getValue("eazon");
               String var5;
               if (!XMLFlyCheckLoader.this.mezokey.equals(XMLFlyCheckLoader.this.mezokey.toUpperCase())) {
                  XMLFlyCheckLoader.this.fatalerror = true;
                  this.roleerrorflag = true;
                  var5 = "A mezőkódban a kisbetű nem megengedett! (" + XMLFlyCheckLoader.this.mezokey + ")\nEz az adat nem kerül betöltésre. Ellenőrizze a nyomtatványt!";
                  String var6 = " > " + XMLFlyCheckLoader.this.curindex + ".alnyomtatvány " + XMLFlyCheckLoader.this.lc_fm.id + " Név:" + XMLFlyCheckLoader.this.mv_nev;
                  if (XMLFlyCheckLoader.this.needwarninghead) {
                     XMLFlyCheckLoader.this.warninglist.add(new TextWithIcon(var6, -1));
                     XMLFlyCheckLoader.this.needwarninghead = false;
                  }

                  XMLFlyCheckLoader.this.warninglist.add(new TextWithIcon(var5, 0));
               }

               if (!XMLFlyCheckLoader.this.checkmezokod(XMLFlyCheckLoader.this.mezokey)) {
                  throw new Exception("Mezőkód ('eazon') formátum hiba! Az XXXX helyett az xml állományba az kerüljön, hogy az adott lap hányadik dinamikus lapján szerepel a mező (az XXXX helyére az első dinamikus lap esetén 0001, második dinamikus lap esetén 0002 kerül stb.).");
               }

               if (XMLFlyCheckLoader.this.mezokey.indexOf("_") > -1) {
                  throw new Exception("Mezőkód ('eazon') hiba! A feladandó állományban az 'eazon' attributum értéke nem tartalmazhat '_' jelet.");
               }

               if (this.codehs.contains(XMLFlyCheckLoader.this.mezokey)) {
                  XMLFlyCheckLoader.this.fatalerror = true;
                  var5 = "";
                  if (XMLFlyCheckLoader.this.mv_nev != null) {
                     var5 = XMLFlyCheckLoader.this.mv_nev + " , " + XMLFlyCheckLoader.this.mv_adoazonosito;
                  } else {
                     var5 = XMLFlyCheckLoader.this.al_megnevezes + " , " + XMLFlyCheckLoader.this.al_azonosito;
                  }

                  throw new SAXException("Albizonylat azonosító : " + var5 + " |  Duplikált mezőkód! " + var4.getValue("eazon"));
               }

               this.codehs.add(XMLFlyCheckLoader.this.mezokey);
               switch(Tools.handleTervezetFieldFid(XMLFlyCheckLoader.this.mezokey, XMLFlyCheckLoader.this.bm)) {
               case 1:
                  throw new Exception("A tervezete ebben a formában nem küldhető be! \nKérjük importálja be az xml állományt vagy nyissa meg szerkesztésre!");
               default:
                  var5 = XMLFlyCheckLoader.this.mezokey;
                  XMLFlyCheckLoader.this.mezokey = TemplateUtils.getInstance().keyToDSId(XMLFlyCheckLoader.this.mezokey, XMLFlyCheckLoader.this.lc_fm.id, XMLFlyCheckLoader.this.bm);
                  String[] var15 = XMLFlyCheckLoader.this.mezokey.split("_", 2);
                  if (var15.length < 2) {
                     String var18 = XMLFlyCheckLoader.this.mezokey;
                     if (var18.length() < 3) {
                        var18 = "Üres a mezőkód!";
                     } else {
                        var18 = "Mezőkód: " + var18;
                     }

                     throw new SAXException("Érvénytelen mezőazonosító! " + var18);
                  }

                  if (XMLFlyCheckLoader.this.lc_fm.fids_page.get(var15[1]) != null && ((PageModel)XMLFlyCheckLoader.this.lc_fm.fids_page.get(var15[1])).role == 0) {
                     XMLFlyCheckLoader.this.fatalerror = true;
                     XMLFlyCheckLoader.this.hasError = true;
                     XMLFlyCheckLoader.this.errormsg = "A megadott állomány nem létező mezőkódot tartalmaz (" + var15[1] + ")!";
                     return;
                  }

                  var7 = 0;

                  try {
                     var7 = Integer.parseInt(var15[0]);
                  } catch (NumberFormatException var13) {
                  }

                  ++var7;
                  int var8 = XMLFlyCheckLoader.this.lc_fm.get((PageModel)XMLFlyCheckLoader.this.lc_fm.fids_page.get(var15[1]));
                  if (var8 != -1 && (((PageModel)XMLFlyCheckLoader.this.lc_fm.fids_page.get(var15[1])).getCalculatedHide() & VisualFieldModel.getmask()) == 0) {
                     var8 = -1;
                  }

                  if (var8 != -1) {
                     try {
                        DataFieldModel var9 = ((PageModel)XMLFlyCheckLoader.this.lc_fm.fids_page.get(var15[1])).getByCode(var15[1]);
                        if (var9 != null && (var9.role & VisualFieldModel.getmask()) == 0) {
                           var8 = -1;
                        }
                     } catch (Exception var12) {
                        var8 = -1;
                     }
                  }

                  if (var8 == -1) {
                     XMLFlyCheckLoader.this.fatalerror = true;
                     this.roleerrorflag = true;
                     String var19 = "A sablon nem tartalmazza az adatállományban található (" + var5 + " mezőkódú) mezőt.\nEz az adat nem kerül betöltésre. Ellenőrizze a nyomtatványt!";
                     String var10 = " > " + XMLFlyCheckLoader.this.curindex + ".alnyomtatvány " + XMLFlyCheckLoader.this.lc_fm.id + " Név:" + XMLFlyCheckLoader.this.mv_nev;
                     if (XMLFlyCheckLoader.this.needwarninghead) {
                        XMLFlyCheckLoader.this.warninglist.add(new TextWithIcon(var10, -1));
                        XMLFlyCheckLoader.this.needwarninghead = false;
                     }

                     XMLFlyCheckLoader.this.warninglist.add(new TextWithIcon(var19, 0));
                  } else {
                     int var20;
                     try {
                        var20 = ((PageModel)XMLFlyCheckLoader.this.lc_fm.fids_page.get(var15[1])).maxpage;
                     } catch (Exception var11) {
                        var20 = 1;
                     }

                     if (var20 < var7) {
                        throw new SAXException("Nem megengedett dinamikus lapszám!\n Mezőkód: " + var5 + "\n Maximális érték: " + var20);
                     }

                     if (XMLFlyCheckLoader.this.pc[var8] < var7) {
                        XMLFlyCheckLoader.this.pc[var8] = var7;
                     }
                  }
               }
            } catch (Exception var14) {
               if (var14 instanceof SAXException) {
                  throw (SAXException)var14;
               }

               throw new SAXException("Érvénytelen mezőkód! " + var4.getValue("eazon") + "\n " + (var14.getMessage() != null ? var14.getMessage() : var14.toString()));
            }
         } else if (var3.equals("mezok")) {
            XMLFlyCheckLoader.this.newdatastore = new GUI_Datastore();
            XMLFlyCheckLoader.this.lc_fm = XMLFlyCheckLoader.this.bm.get(XMLFlyCheckLoader.this.l_nyomtatvanyazonosito);
            if (XMLFlyCheckLoader.this.lc_fm == null) {
               XMLFlyCheckLoader.this.errormsg = "Hibás típusú adatfile! Az alnyomtatvány nem a főnyomtatványhoz tartozik!";
               throw new SAXException(XMLFlyCheckLoader.this.errormsg);
            }

            int var17 = XMLFlyCheckLoader.this.bm.getIndex(XMLFlyCheckLoader.this.bm.get(XMLFlyCheckLoader.this.l_nyomtatvanyazonosito));
            if (XMLFlyCheckLoader.this.bm.maxcreation[var17] < XMLFlyCheckLoader.this.bm.created[var17] + 1) {
               XMLFlyCheckLoader.this.errormsg = "Súlyos hiba! Az " + XMLFlyCheckLoader.this.l_nyomtatvanyazonosito + " típusú résznyomtatványból nem szerepelhet több! Max. = " + XMLFlyCheckLoader.this.bm.maxcreation[var17];
               throw new SAXException(XMLFlyCheckLoader.this.errormsg);
            }

            Boolean var16 = new Boolean(XMLFlyCheckLoader.this.l_nyomtatvanyazonosito.equals(XMLFlyCheckLoader.this.bm.main_document_id));
            XMLFlyCheckLoader.this.lc_elem = new Elem(XMLFlyCheckLoader.this.newdatastore, XMLFlyCheckLoader.this.l_nyomtatvanyazonosito, XMLFlyCheckLoader.this.lc_fm.name, var16);
            XMLFlyCheckLoader.this.pc = new int[XMLFlyCheckLoader.this.lc_fm.size()];

            for(var7 = 0; var7 < XMLFlyCheckLoader.this.pc.length; ++var7) {
               XMLFlyCheckLoader.this.pc[var7] = 1;
            }

            XMLFlyCheckLoader.this.lc_elem.getEtc().put("pagecounts", XMLFlyCheckLoader.this.pc);
            if (XMLFlyCheckLoader.this.l_formnote != null) {
               XMLFlyCheckLoader.this.lc_elem.getEtc().put("orignote", XMLFlyCheckLoader.this.l_formnote);
            }

            XMLFlyCheckLoader.this._this.add(XMLFlyCheckLoader.this.lc_elem);
            int var10002 = XMLFlyCheckLoader.this.bm.created[var17]++;
            XMLFlyCheckLoader.this.bm.setCalcelemindex(XMLFlyCheckLoader.this.curindex - 1);
            TemplateUtils.getInstance().setDefaultValues(XMLFlyCheckLoader.this.lc_fm.id, XMLFlyCheckLoader.this.bm);
         } else if (var3.equals("head")) {
            XMLFlyCheckLoader.this.head = new Hashtable();
            XMLFlyCheckLoader.this.attributes_done(var4, XMLFlyCheckLoader.this.head);
         } else if (var3.equals("docinfo")) {
            XMLFlyCheckLoader.this.docinfo = new Hashtable();
            XMLFlyCheckLoader.this.attributes_done(var4, XMLFlyCheckLoader.this.docinfo);
         } else if (var3.equals("type")) {
            XMLFlyCheckLoader.this.in_type = true;
         } else if (var3.equals("saved")) {
            XMLFlyCheckLoader.this.in_saved = true;
         } else if (var3.equals("nyomtatvanyok")) {
            XMLFlyCheckLoader.this.nyomtatvanyok = new Hashtable();
            XMLFlyCheckLoader.this.attributes_done(var4, XMLFlyCheckLoader.this.nyomtatvanyok);
         } else if (var3.equals("hibakszama")) {
            XMLFlyCheckLoader.this.in_hibakszama = true;
         } else if (var3.equals("hash")) {
            XMLFlyCheckLoader.this.in_hash = true;
         } else if (var3.equals("programverzio")) {
            XMLFlyCheckLoader.this.in_programverzio = true;
            XMLFlyCheckLoader.this.abev_programverzio = "";
         } else if (var3.equals("nyomtatvany")) {
            XMLFlyCheckLoader.this.clearvars();
            this.codehs = new HashSet(100);
            XMLFlyCheckLoader.this.lc_elem = null;
            ++XMLFlyCheckLoader.this.curindex;
            if (999 < XMLFlyCheckLoader.this.ell4xs.errorList.size()) {
               GuiUtil.setGlassLabel("Betöltve:" + XMLFlyCheckLoader.this.curindex + "  Hibák száma:" + XMLFlyCheckLoader.this.ell4xs.errorList.size());
               if (XMLFlyCheckLoader.this.silent && XMLFlyCheckLoader.this.ell4xs.errorList.size() % 1000 == 0) {
                  System.out.println("Hibák száma: " + XMLFlyCheckLoader.this.ell4xs.errorList.size());
               }
            } else {
               GuiUtil.setGlassLabel("Betöltve:" + XMLFlyCheckLoader.this.curindex);
            }
         } else if (var3.equals("nyomtatvanyazonosito")) {
            XMLFlyCheckLoader.this.in_nyomtatvanyazonosito = true;
            XMLFlyCheckLoader.this.l_nyomtatvanyazonosito = "";
         } else if (var3.equals("nyomtatvanyverzio")) {
            XMLFlyCheckLoader.this.in_nyomtatvanyverzio = true;
            XMLFlyCheckLoader.this.l_nyomtatvanyverzio = "";
         } else if (var3.equals("tol")) {
            XMLFlyCheckLoader.this.in_tol = true;
            XMLFlyCheckLoader.this.l_tol = "";
         } else if (var3.equals("ig")) {
            XMLFlyCheckLoader.this.in_ig = true;
            XMLFlyCheckLoader.this.l_ig = "";
         } else if (var3.equals("megjegyzes")) {
            XMLFlyCheckLoader.this.in_megjegyzes = true;
            if (XMLFlyCheckLoader.this.in_firstform) {
               XMLFlyCheckLoader.this.l_megjegyzes = "";
            }

            XMLFlyCheckLoader.this.l_formnote = "";
         } else if (var3.equals("adoszam")) {
            if (XMLFlyCheckLoader.this.in_adozo) {
               XMLFlyCheckLoader.this.in_a_adoszam = true;
               XMLFlyCheckLoader.this.a_adoszam = "";
            } else {
               XMLFlyCheckLoader.this.in_mv_adoszam = true;
               XMLFlyCheckLoader.this.mv_adoszam = "";
            }
         } else if (var3.equals("adoazonosito")) {
            if (XMLFlyCheckLoader.this.in_adozo) {
               XMLFlyCheckLoader.this.in_a_adoazonosito = true;
               XMLFlyCheckLoader.this.a_adoazonosito = "";
            } else {
               XMLFlyCheckLoader.this.in_mv_adoazonosito = true;
               XMLFlyCheckLoader.this.mv_adoazonosito = "";
            }
         } else if (var3.equals("nev")) {
            if (XMLFlyCheckLoader.this.in_adozo) {
               XMLFlyCheckLoader.this.in_a_nev = true;
               XMLFlyCheckLoader.this.a_nev = "";
            } else {
               XMLFlyCheckLoader.this.in_mv_nev = true;
               XMLFlyCheckLoader.this.mv_nev = "";
            }
         } else if (var3.equals("adozo")) {
            XMLFlyCheckLoader.this.in_adozo = true;
         } else if (var3.equals("albizonylatazonositas")) {
            XMLFlyCheckLoader.this.in_albizonylat = true;
         } else if (var3.equals("megnevezes")) {
            XMLFlyCheckLoader.this.in_al_megnevezes = true;
            XMLFlyCheckLoader.this.al_megnevezes = "";
         } else if (var3.equals("azonosito")) {
            XMLFlyCheckLoader.this.in_al_azonosito = true;
            XMLFlyCheckLoader.this.al_azonosito = "";
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
   }

   class headhandler extends DefaultHandler {
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         if (XMLFlyCheckLoader.this.in_nyomtatvanyazonosito) {
            String var4 = new String(var1, var2, var3);
            XMLFlyCheckLoader.this.head.put("nyomtatvanyazonosito", var4);
         } else if (XMLFlyCheckLoader.this.in_nyomtatvanyverzio) {
            XMLFlyCheckLoader.this.head.put("nyomtatvanyverzio", new String(var1, var2, var3));
         } else if (XMLFlyCheckLoader.this.in_tol) {
            XMLFlyCheckLoader.this.l_tol = new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_ig) {
            XMLFlyCheckLoader.this.l_ig = new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_megjegyzes) {
            XMLFlyCheckLoader.this.l_megjegyzes = new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_a_adoszam) {
            XMLFlyCheckLoader.this.a_adoszam = new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_a_adoazonosito) {
            XMLFlyCheckLoader.this.a_adoazonosito = new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_a_nev) {
            XMLFlyCheckLoader.this.a_nev = new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_mv_adoszam) {
            XMLFlyCheckLoader.this.mv_adoszam = new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_mv_adoazonosito) {
            XMLFlyCheckLoader.this.mv_adoazonosito = new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_mv_nev) {
            XMLFlyCheckLoader.this.mv_nev = new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_al_megnevezes) {
            XMLFlyCheckLoader.this.al_megnevezes = XMLFlyCheckLoader.this.al_megnevezes + new String(var1, var2, var3);
         } else if (XMLFlyCheckLoader.this.in_al_azonosito) {
            XMLFlyCheckLoader.this.al_azonosito = XMLFlyCheckLoader.this.al_azonosito + new String(var1, var2, var3);
         }

      }

      public void endDocument() throws SAXException {
         super.endDocument();
      }

      public void endElement(String var1, String var2, String var3) throws SAXException {
         if (var3.equals("nyomtatvanyinformacio")) {
            if (XMLFlyCheckLoader.this.docinfo != null) {
               XMLFlyCheckLoader.this.head.put("docinfo", XMLFlyCheckLoader.this.docinfo);
            } else {
               XMLFlyCheckLoader.this.head.put("docinfo", new Hashtable());
            }

            throw new SAXException("OUT");
         } else {
            if (var3.equals("nyomtatvanyazonosito")) {
               XMLFlyCheckLoader.this.in_nyomtatvanyazonosito = false;
            } else if (var3.equals("nyomtatvanyverzio")) {
               XMLFlyCheckLoader.this.in_nyomtatvanyverzio = false;
            } else if (var3.equals("tol")) {
               XMLFlyCheckLoader.this.in_tol = false;
            } else if (var3.equals("ig")) {
               XMLFlyCheckLoader.this.in_ig = false;
            } else if (var3.equals("megjegyzes")) {
               XMLFlyCheckLoader.this.in_megjegyzes = false;
            } else if (var3.equals("adoszam")) {
               if (XMLFlyCheckLoader.this.in_adozo) {
                  XMLFlyCheckLoader.this.in_a_adoszam = false;
               } else {
                  XMLFlyCheckLoader.this.in_mv_adoszam = false;
               }
            } else if (var3.equals("adoazonosito")) {
               if (XMLFlyCheckLoader.this.in_adozo) {
                  XMLFlyCheckLoader.this.in_a_adoazonosito = false;
               } else {
                  XMLFlyCheckLoader.this.in_mv_adoazonosito = false;
               }
            } else if (var3.equals("nev")) {
               if (XMLFlyCheckLoader.this.in_adozo) {
                  XMLFlyCheckLoader.this.in_a_nev = false;
               } else {
                  XMLFlyCheckLoader.this.in_mv_nev = false;
               }
            } else if (var3.equals("adozo")) {
               XMLFlyCheckLoader.this.in_adozo = false;
            } else if (var3.equals("albizonylatazonositas")) {
               XMLFlyCheckLoader.this.in_albizonylat = false;
            } else if (var3.equals("megnevezes")) {
               XMLFlyCheckLoader.this.in_al_megnevezes = false;
            } else if (var3.equals("azonosito")) {
               XMLFlyCheckLoader.this.in_al_azonosito = false;
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
         if (var3.equals("nyomtatvanyinformacio")) {
            XMLFlyCheckLoader.this.head = new Hashtable();
            XMLFlyCheckLoader.this.attributes_done(var4, XMLFlyCheckLoader.this.head);
         } else if (var3.equals("docinfo")) {
            XMLFlyCheckLoader.this.docinfo = new Hashtable();
            XMLFlyCheckLoader.this.attributes_done(var4, XMLFlyCheckLoader.this.docinfo);
         } else if (var3.equals("nyomtatvanyazonosito")) {
            XMLFlyCheckLoader.this.in_nyomtatvanyazonosito = true;
         } else if (var3.equals("nyomtatvanyverzio")) {
            XMLFlyCheckLoader.this.in_nyomtatvanyverzio = true;
         } else if (var3.equals("tol")) {
            XMLFlyCheckLoader.this.in_tol = true;
         } else if (var3.equals("ig")) {
            XMLFlyCheckLoader.this.in_ig = true;
         } else if (var3.equals("megjegyzes")) {
            XMLFlyCheckLoader.this.in_megjegyzes = true;
         } else if (var3.equals("adoszam")) {
            if (XMLFlyCheckLoader.this.in_adozo) {
               XMLFlyCheckLoader.this.in_a_adoszam = true;
            } else {
               XMLFlyCheckLoader.this.in_mv_adoszam = true;
            }
         } else if (var3.equals("adoazonosito")) {
            if (XMLFlyCheckLoader.this.in_adozo) {
               XMLFlyCheckLoader.this.in_a_adoazonosito = true;
            } else {
               XMLFlyCheckLoader.this.in_mv_adoazonosito = true;
            }
         } else if (var3.equals("nev")) {
            if (XMLFlyCheckLoader.this.in_adozo) {
               XMLFlyCheckLoader.this.in_a_nev = true;
            } else {
               XMLFlyCheckLoader.this.in_mv_nev = true;
            }
         } else if (var3.equals("adozo")) {
            XMLFlyCheckLoader.this.in_adozo = true;
         } else if (var3.equals("albizonylatazonositas")) {
            XMLFlyCheckLoader.this.in_albizonylat = true;
         } else if (var3.equals("megnevezes")) {
            XMLFlyCheckLoader.this.in_al_megnevezes = true;
            XMLFlyCheckLoader.this.al_megnevezes = "";
         } else if (var3.equals("azonosito")) {
            XMLFlyCheckLoader.this.in_al_azonosito = true;
            XMLFlyCheckLoader.this.al_azonosito = "";
         }

      }

      public void startPrefixMapping(String var1, String var2) throws SAXException {
         super.startPrefixMapping(var1, var2);
      }

      public void warning(SAXParseException var1) throws SAXException {
         super.warning(var1);
      }
   }
}
