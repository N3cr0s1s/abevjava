package hu.piller.enykp.alogic.fileloader.xml;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.fileutil.DatastoreKeyToXml;
import hu.piller.enykp.alogic.fileutil.ExtendedTemplateData;
import hu.piller.enykp.alogic.fileutil.HeadChecker;
import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.settingspanel.upgrade.UpgradeFunction;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
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
import hu.piller.enykp.interfaces.ILoadManager;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import java.awt.Frame;
import java.io.ByteArrayInputStream;
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
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import me.necrocore.abevjava.NecroFile;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlLoader implements ILoadManager {
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
   public String muvelet;
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
   private boolean in_muvelet;
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
   private boolean enable_inFid = true;

   public XmlLoader() {
      this.enable_inFid = true;
   }

   public XmlLoader(boolean var1) {
      this.enable_inFid = var1;
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
      this._load(var1, var2);

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
         if (this.muvelet != null) {
            this.head.put("muvelet", this.muvelet);
         }

         return this.head;
      } catch (Exception var6) {
         Hashtable var4 = new Hashtable();
         Hashtable var5 = new Hashtable();
         var5.put("name", "HIBÁS ÁLLOMÁNY !");
         var4.put("head", "");
         var4.put("type", "single");
         var4.put("docinfo", var5);
         var4.put("doc", new Vector());
         if (this.muvelet != null) {
            var4.put("muvelet", this.muvelet);
         }

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

   public BookModel load(InputStream var1, String var2, String var3, String var4, BookModel var5, String var6) {
      if (var5 != null && var5.loadedfile != null) {
         this.silent = true;
         BookModel var7 = new BookModel();
         var7.hasError = true;

         try {
            if (var2 == null) {
               var7.errormsg = "Nincs nyomtatványazonosító az xml állományban!";
               return var7;
            } else {
               ExtendedTemplateData var8 = TemplateChecker.getInstance().getTemplateFilenameWithDialog(var2, var3, var4);
               File var9 = var8.getTemplateFile();
               if (var9 != null && var9.exists()) {
                  if (var5.loadedfile.getAbsolutePath().equals(var9.getAbsolutePath())) {
                     this.bm = var5;
                     this.bm.reempty();
                  } else {
                     var5.destroy();
                     this.bm = new BookModel(var9);
                  }

                  if (this.bm.hasError) {
                     return this.bm;
                  } else {
                     this._this = this.bm.cc;
                     this._load(var1, var6);
                     if (this.hasError) {
                        var7.errormsg = this.errormsg;
                        return var7;
                     } else {
                        this.bm.cc.setLoadedfile((File)null);
                        this.bm.cc.l_megjegyzes = this.l_megjegyzes;
                        if (this.warninglist.size() != 0) {
                           if (this.silent) {
                              this.bm.warninglist = this.warninglist;
                           } else {
                              GuiUtil.showErrorDialog((Frame)null, "Hibák és figyelmeztetések", true, true, this.warninglist);
                           }
                        }

                        if (var8.isTemplateDisaled()) {
                           TemplateUtils.getInstance().handleDisabledTemplates(var8.getTemplateId(), var8.getOrgId());
                           this.bm.setDisabledTemplate(true);
                        }

                        return this.bm;
                     }
                  }
               } else {
                  var7.errormsg = "Nincs megfelelő sablon! Ismeretlen nyomtatványazonosító: " + var2;
                  return var7;
               }
            }
         } catch (Exception var10) {
            var7.errormsg = var10.getMessage();
            return var7;
         }
      } else {
         return this.load(var1, var2, var3, var4, var6);
      }
   }

   public BookModel load(String var1, String var2, String var3, String var4, BookModel var5) {
      if (var5 != null && var5.loadedfile != null) {
         this.silent = true;
         BookModel var6 = new BookModel();
         var6.hasError = true;
         ExtendedTemplateData var7 = new ExtendedTemplateData(var2, var4);

         try {
            File var8 = new NecroFile(var1);
            if (var2 == null) {
               var6.errormsg = "Nincs nyomtatványazonosító az xml állományban!";
               return var6;
            } else {
               var7 = TemplateChecker.getInstance().getTemplateFileNames(var2, var3, var4);
               String[] var9 = var7.getTemplateFileNames();
               File var10 = new NecroFile(var9[0]);
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
                     this.bm.setXkr_mode(this instanceof XkrLoader);
                     this.load(var8);
                     if (this.hasError) {
                        var6.errormsg = this.errormsg;
                        return var6;
                     } else {
                        this.bm.cc.setLoadedfile(var8);
                        this.bm.cc.l_megjegyzes = this.l_megjegyzes;
                        if (this.warninglist.size() != 0) {
                           if (this.silent) {
                              this.bm.warninglist = this.warninglist;
                           } else {
                              GuiUtil.showErrorDialog((Frame)null, "Hibák és figyelmeztetések", true, true, this.warninglist, var8);
                           }
                        }

                        if (var7.isTemplateDisaled()) {
                           TemplateUtils.getInstance().handleDisabledTemplates(var7.getTemplateId(), var7.getOrgId());
                           this.bm.setDisabledTemplate(true);
                        }

                        return this.bm;
                     }
                  }
               }
            }
         } catch (Exception var11) {
            var6.errormsg = var11.getMessage();
            if (var7.isTemplateDisaled()) {
               TemplateUtils.getInstance().handleDisabledTemplates(var7.getTemplateId(), var7.getOrgId());
               var6.setDisabledTemplate(true);
            }

            return var6;
         }
      } else {
         return this.load(var1, var2, var3, var4);
      }
   }

   public BookModel load(InputStream var1, String var2, String var3, String var4, String var5, boolean var6) {
      this.onlyhead = var6;
      return this.load(var1, var2, var3, var4, var5);
   }

   public BookModel load(InputStream var1, String var2, String var3, String var4, String var5) {
      this.silent = false;
      BookModel var6 = new BookModel();
      var6.hasError = true;
      ExtendedTemplateData var7 = new ExtendedTemplateData(var2, var4);

      try {
         if (var2 == null) {
            var6.errormsg = "Nincs nyomtatványazonosító az xml állományban!";
            return var6;
         } else {
            var7 = TemplateChecker.getInstance().getTemplateFilenameWithDialog(var2, var3, var4);
            File var8 = var7.getTemplateFile();
            if (var8 != null && var8.exists()) {
               this.bm = new BookModel(var8);
               if (this.bm.hasError) {
                  return this.bm;
               } else {
                  this.bm.setXkr_mode(this instanceof XkrLoader);
                  this._this = this.bm.cc;
                  this._load(var1, var5);
                  if (this.hasError) {
                     var6.errormsg = this.errormsg;
                     return var6;
                  } else {
                     this.bm.cc.setLoadedfile((File)null);
                     this.bm.cc.l_megjegyzes = this.l_megjegyzes;
                     if (this.warninglist.size() != 0) {
                        if (this.silent) {
                           this.bm.warninglist = this.warninglist;
                        } else {
                           GuiUtil.showErrorDialog((Frame)null, "Hibák és figyelmeztetések", true, true, this.warninglist);
                        }
                     }

                     if (var7.isTemplateDisaled()) {
                        TemplateUtils.getInstance().handleDisabledTemplates(var7.getTemplateId(), var7.getOrgId());
                        this.bm.setDisabledTemplate(true);
                     }

                     return this.bm;
                  }
               }
            } else {
               var6.errormsg = "Nincs megfelelő sablon! Ismeretlen nyomtatványazonosító: " + var2;
               return var6;
            }
         }
      } catch (Exception var9) {
         var6.errormsg = var9.getMessage();
         if (var7.isTemplateDisaled()) {
            TemplateUtils.getInstance().handleDisabledTemplates(var7.getTemplateId(), var7.getOrgId());
            var6.setDisabledTemplate(true);
         }

         return var6;
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
      ExtendedTemplateData var7 = new ExtendedTemplateData(var2, var4);

      try {
         File var8 = new NecroFile(var1);
         if (var2 == null) {
            var6.errormsg = "Nincs nyomtatványazonosító az xml állományban!";
            return var6;
         } else {
            if (!var5 && this instanceof XkrLoader) {
               TemplateChecker.getInstance().setGetTemplateIfHavent(0);
            }

            File var9;
            if (var5 && PropertyList.getInstance().get("prop.dynamic.importOne") == null) {
               var7 = TemplateChecker.getInstance().getTemplateFileNames(var2, var3, var4, PropertyList.getInstance().get("prop.dynamic.xml_loader_call") == null ? null : (UpgradeFunction)PropertyList.getInstance().get("prop.dynamic.xml_loader_call"));
               String[] var10 = var7.getTemplateFileNames();
               var9 = new NecroFile(var10[0]);
            } else {
               TemplateChecker.getInstance().setGetTemplateIfHavent(0);
               var7 = TemplateChecker.getInstance().getTemplateFilenameWithDialog(var2, var3, var4, UpgradeFunction.OPEN);
               var9 = var7.getTemplateFile();
            }

            TemplateChecker.getInstance().setGetTemplateIfHavent(1);
            if (!var9.exists()) {
               var6.errormsg = "Nincs megfelelő sablon!" + var2 + " - " + var3;
               return var6;
            } else {
               this.bm = new BookModel(var9);
               if (this.bm.hasError) {
                  return this.bm;
               } else {
                  this.bm.setXkr_mode(this instanceof XkrLoader);
                  this.bm.setXczModeModifier("packed_data_loader_v1".equals(this.loaderid));
                  this._this = this.bm.cc;
                  this.load(var8);
                  if (this.hasError) {
                     var6.errormsg = this.errormsg;
                     return var6;
                  } else {
                     this.bm.cc.setLoadedfile(var8);
                     this.bm.cc.l_megjegyzes = this.l_megjegyzes;
                     if (this.warninglist.size() != 0) {
                        if (var5) {
                           this.bm.warninglist = this.warninglist;
                        } else {
                           GuiUtil.showErrorDialog((Frame)null, "Hibák és figyelmeztetések", true, true, this.warninglist, var8);
                        }
                     }

                     if (var7.isTemplateDisaled()) {
                        TemplateUtils.getInstance().handleDisabledTemplates(var7.getTemplateId(), var7.getOrgId());
                        this.bm.setDisabledTemplate(true);
                     }

                     return this.bm;
                  }
               }
            }
         }
      } catch (Exception var11) {
         var11.printStackTrace();
         var6.errormsg = var11.getMessage();
         if (var7.isTemplateDisaled()) {
            TemplateUtils.getInstance().handleDisabledTemplates(var7.getTemplateId(), var7.getOrgId());
            var6.setDisabledTemplate(true);
         }

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
         this._load(var2, var3);
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

   public void load(String var1) {
      try {
         this.loadedfile = null;
         ByteArrayInputStream var2 = new ByteArrayInputStream(var1.getBytes());
         String var3 = "utf-8";
         this._load(var2, var3);
      } catch (Exception var4) {
         this.hasError = true;
         this.errormsg = var4.toString();
      }

   }

   public void _load(InputStream var1, String var2) {
      this.clearall();
      this.warninglist = new Vector();
      this.hasError = false;
      this.curindex = 0;
      this.in_type = false;
      this.in_saved = false;
      this.in_hibakszama = false;
      this.in_hash = false;
      this.in_muvelet = false;
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
            var3 = new XmlLoader.headhandler();
         } else {
            var3 = new XmlLoader.bodyhandler();
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

   boolean checkmezokod(String var1) {
      int var2 = var1.indexOf("XXXX");
      return var2 == -1;
   }

   class bodyhandler extends DefaultHandler {
      HashSet codehs;
      private boolean roleerrorflag = false;

      public void characters(char[] var1, int var2, int var3) throws SAXException {
         if (XmlLoader.this.in_mezo) {
            XmlLoader.this.mezovalue = XmlLoader.this.mezovalue + DatastoreKeyToXml.plainConvert(new String(var1, var2, var3));
         } else if (XmlLoader.this.in_type) {
            String var4 = new String(var1, var2, var3);
            XmlLoader.this.head.put("type", var4);
            if (var4.equals("multi")) {
               XmlLoader.this.single = false;
            }
         } else if (XmlLoader.this.in_saved) {
            XmlLoader.this.head.put("saved", new String(var1, var2, var3));
         } else if (XmlLoader.this.in_hibakszama) {
            XmlLoader.this.abev_hibakszama = new String(var1, var2, var3);
         } else if (XmlLoader.this.in_hash) {
            XmlLoader.this.abev_hash = new String(var1, var2, var3);
         } else if (XmlLoader.this.in_muvelet) {
            XmlLoader.this.muvelet = new String(var1, var2, var3);
            XmlLoader.this.bm.cc.muvelet = XmlLoader.this.muvelet;
         } else if (XmlLoader.this.in_programverzio) {
            XmlLoader.this.abev_programverzio = new String(var1, var2, var3);
         } else if (XmlLoader.this.in_nyomtatvanyazonosito) {
            XmlLoader.this.l_nyomtatvanyazonosito = XmlLoader.this.l_nyomtatvanyazonosito + new String(var1, var2, var3);
         } else if (XmlLoader.this.in_nyomtatvanyverzio) {
            XmlLoader.this.l_nyomtatvanyverzio = XmlLoader.this.l_nyomtatvanyverzio + new String(var1, var2, var3);
         } else if (XmlLoader.this.in_tol) {
            XmlLoader.this.l_tol = XmlLoader.this.l_tol + new String(var1, var2, var3);
         } else if (XmlLoader.this.in_ig) {
            XmlLoader.this.l_ig = XmlLoader.this.l_ig + new String(var1, var2, var3);
         } else if (XmlLoader.this.in_megjegyzes) {
            if (XmlLoader.this.in_firstform) {
               XmlLoader.this.l_megjegyzes = XmlLoader.this.l_megjegyzes + new String(var1, var2, var3);
            }

            XmlLoader.this.l_formnote = XmlLoader.this.l_formnote + new String(var1, var2, var3);
         } else if (XmlLoader.this.in_a_adoszam) {
            XmlLoader.this.a_adoszam = XmlLoader.this.a_adoszam + new String(var1, var2, var3);
         } else if (XmlLoader.this.in_a_adoazonosito) {
            XmlLoader.this.a_adoazonosito = XmlLoader.this.a_adoazonosito + new String(var1, var2, var3);
         } else if (XmlLoader.this.in_a_nev) {
            XmlLoader.this.a_nev = XmlLoader.this.a_nev + new String(var1, var2, var3);
         } else if (XmlLoader.this.in_mv_adoszam) {
            XmlLoader.this.mv_adoszam = XmlLoader.this.mv_adoszam + new String(var1, var2, var3);
         } else if (XmlLoader.this.in_mv_adoazonosito) {
            XmlLoader.this.mv_adoazonosito = XmlLoader.this.mv_adoazonosito + new String(var1, var2, var3);
         } else if (XmlLoader.this.in_mv_nev) {
            XmlLoader.this.mv_nev = XmlLoader.this.mv_nev + new String(var1, var2, var3);
         } else if (XmlLoader.this.in_al_megnevezes) {
            XmlLoader.this.al_megnevezes = XmlLoader.this.al_megnevezes + new String(var1, var2, var3);
         } else if (XmlLoader.this.in_al_azonosito) {
            XmlLoader.this.al_azonosito = XmlLoader.this.al_azonosito + new String(var1, var2, var3);
         }

      }

      public void endDocument() throws SAXException {
         super.endDocument();
      }

      public void endElement(String var1, String var2, String var3) throws SAXException {
         if (var3.equals("mezo")) {
            try {
               if (this.roleerrorflag) {
                  this.roleerrorflag = false;
                  XmlLoader.this.in_mezo = false;
                  return;
               }

               String var4 = XmlLoader.this.mezokey.substring(XmlLoader.this.mezokey.indexOf("_") + 1);
               if (XmlLoader.this.lc_fm.fids_page.get(var4) == null) {
                  return;
               }

               if (((PageModel)XmlLoader.this.lc_fm.fids_page.get(var4)).role == 0) {
                  return;
               }

               boolean var5 = ((DataFieldModel)XmlLoader.this.bm.get(XmlLoader.this.lc_fm.id).fids.get(var4)).features.get("datatype").equals("check");
               if (var5) {
                  if (XmlLoader.this.mezovalue.equals("X")) {
                     XmlLoader.this.mezovalue = "true";
                  } else if (XMLPost.xmlbooleancheck) {
                     if (XmlLoader.this.mezovalue.equals("")) {
                        XmlLoader.this.mezovalue = "false";
                     } else {
                        XmlLoader.this.fatalerror = true;
                        XmlLoader.this.hasError = true;
                        XmlLoader.this.errormsg = "Érvénytelen logikai érték! Mezőkód = " + var4;
                     }
                  } else {
                     XmlLoader.this.mezovalue = "false";
                  }
               }

               int var6;
               try {
                  var6 = ((PageModel)XmlLoader.this.lc_fm.fids_page.get(var4)).maxpage;
               } catch (Exception var12) {
                  var6 = 1;
               }

               String var7 = XmlLoader.this.mezokey.substring(0, XmlLoader.this.mezokey.indexOf("_"));

               int var8;
               try {
                  var8 = Integer.parseInt(var7);
               } catch (NumberFormatException var11) {
                  var8 = 0;
               }

               if (var6 <= var8) {
                  System.out.println("NagyonSúlyos index hiba!!!!!!!! " + XmlLoader.this.mezokey);
               } else {
                  XmlLoader.this.newdatastore.set(XmlLoader.this.mezokey, XmlLoader.this.mezovalue);
               }
            } catch (Exception var13) {
               Tools.eLog(var13, 0);
            }

            XmlLoader.this.in_mezo = false;
         } else if (var3.equals("nyomtatvany")) {
            CalculatorManager.getInstance().doBetoltErtekCalcs(true);
            CalculatorManager.getInstance().form_hidden_fields_calc();
            CalculatorManager.getInstance().multi_form_load();
            HeadChecker var14 = HeadChecker.getInstance();
            Hashtable var15 = new Hashtable();
            if (XmlLoader.this.a_nev != null) {
               var15.put("nev", XmlLoader.this.a_nev);
            }

            if (XmlLoader.this.a_adoszam != null) {
               var15.put("adoszam", XmlLoader.this.a_adoszam);
            }

            if (XmlLoader.this.a_adoazonosito != null) {
               var15.put("adoazonosito", XmlLoader.this.a_adoazonosito);
            }

            if (XmlLoader.this.mv_nev != null) {
               var15.put("munkavallalo/nev", XmlLoader.this.mv_nev);
            }

            if (XmlLoader.this.mv_adoazonosito != null) {
               var15.put("munkavallalo/adoazonosito", XmlLoader.this.mv_adoazonosito);
            }

            if (XmlLoader.this.al_azonosito != null) {
               var15.put("albizonylatazonositas/azonosito", XmlLoader.this.al_azonosito);
            }

            if (XmlLoader.this.al_megnevezes != null) {
               var15.put("albizonylatazonositas/megnevezes", XmlLoader.this.al_megnevezes);
            }

            if (XmlLoader.this.l_tol != null) {
               var15.put("tol", XmlLoader.this.l_tol);
            }

            if (XmlLoader.this.l_ig != null) {
               var15.put("ig", XmlLoader.this.l_ig);
            }

            String var16 = null;

            try {
               var16 = (String)XmlLoader.this.bm.docinfo.get("org");
            } catch (Exception var10) {
               var16 = "Nem definiált";
            }

            Object[] var17 = new Object[]{var15, var16, XmlLoader.this.l_nyomtatvanyazonosito, new Integer(XmlLoader.this.curindex - 1), XmlLoader.this.newdatastore};
            XmlLoader.this.headerrorlist = var14.headCheck(var17, XmlLoader.this.bm);
            if (XmlLoader.this.headerrorlist.size() != 0) {
               if (!XmlLoader.this.silent && !XmlLoader.this.silentheadcheck) {
                  GuiUtil.showErrorDialog((Frame)null, "HeadCheck", true, false, XmlLoader.this.headerrorlist);
               }

               if (!XmlLoader.this.headcheckfatalerror) {
                  XmlLoader.this.headcheckfatalerror = var14.hadFatalError();
               }

               XmlLoader.this.warninglist.addAll(XmlLoader.this.headerrorlist);
            }

            XmlLoader.this.in_firstform = false;
         } else if (var3.equals("head")) {
            if (XmlLoader.this.docinfo != null) {
               XmlLoader.this.head.put("docinfo", XmlLoader.this.docinfo);
            } else {
               XmlLoader.this.head.put("docinfo", new Hashtable());
            }
         } else if (var3.equals("type")) {
            XmlLoader.this.in_type = false;
         } else if (var3.equals("saved")) {
            XmlLoader.this.in_saved = false;
         } else if (var3.equals("hibakszama")) {
            XmlLoader.this.in_hibakszama = false;
         } else if (var3.equals("hash")) {
            XmlLoader.this.in_hash = false;
         } else if (var3.equals("muvelet")) {
            XmlLoader.this.in_muvelet = false;
         } else if (var3.equals("programverzio")) {
            XmlLoader.this.in_programverzio = false;
         } else if (var3.equals("nyomtatvanyazonosito")) {
            XmlLoader.this.in_nyomtatvanyazonosito = false;
         } else if (var3.equals("nyomtatvanyverzio")) {
            XmlLoader.this.in_nyomtatvanyverzio = false;
         } else if (var3.equals("tol")) {
            XmlLoader.this.in_tol = false;
         } else if (var3.equals("ig")) {
            XmlLoader.this.in_ig = false;
         } else if (var3.equals("megjegyzes")) {
            XmlLoader.this.in_megjegyzes = false;
         } else if (var3.equals("adoszam")) {
            if (XmlLoader.this.in_adozo) {
               XmlLoader.this.in_a_adoszam = false;
            } else {
               XmlLoader.this.in_mv_adoszam = false;
            }
         } else if (var3.equals("adoazonosito")) {
            if (XmlLoader.this.in_adozo) {
               XmlLoader.this.in_a_adoazonosito = false;
            } else {
               XmlLoader.this.in_mv_adoazonosito = false;
            }
         } else if (var3.equals("nev")) {
            if (XmlLoader.this.in_adozo) {
               XmlLoader.this.in_a_nev = false;
            } else {
               XmlLoader.this.in_mv_nev = false;
            }
         } else if (var3.equals("adozo")) {
            XmlLoader.this.in_adozo = false;
         } else if (var3.equals("albizonylatazonositas")) {
            XmlLoader.this.in_albizonylat = false;
         } else if (var3.equals("megnevezes")) {
            XmlLoader.this.in_al_megnevezes = false;
         } else if (var3.equals("azonosito")) {
            XmlLoader.this.in_al_azonosito = false;
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
               XmlLoader.this.mezovalue = "";
               XmlLoader.this.in_mezo = true;
               XmlLoader.this.mezokey = var4.getValue("eazon");
               String var5;
               if (!XmlLoader.this.mezokey.equals(XmlLoader.this.mezokey.toUpperCase())) {
                  XmlLoader.this.fatalerror = true;
                  this.roleerrorflag = true;
                  var5 = "A mezőkódban a kisbetű nem megengedett! (" + XmlLoader.this.mezokey + ")\nEz az adat nem kerül betöltésre. Ellenőrizze a nyomtatványt!";
                  String var6 = " > " + XmlLoader.this.curindex + ".alnyomtatvány " + XmlLoader.this.lc_fm.id + " Név:" + XmlLoader.this.mv_nev;
                  if (XmlLoader.this.needwarninghead) {
                     XmlLoader.this.warninglist.add(new TextWithIcon(var6, -1));
                     XmlLoader.this.needwarninghead = false;
                  }

                  XmlLoader.this.warninglist.add(new TextWithIcon(var5, 0));
               }

               if (Tools.handleTervezetFieldFid(XmlLoader.this.mezokey, XmlLoader.this.bm) == 1) {
                  System.out.println("XmlLoader TERVEZET MEZO KIHAGYVA");
                  return;
               }

               if (!XmlLoader.this.checkmezokod(XmlLoader.this.mezokey)) {
                  throw new Exception("A Kód formátum hiba! Az XXXX helyett az xml állományba az kerüljön, hogy az adott lap hányadik dinamikus lapján szerepel a mező (az XXXX helyére az első dinamikus lap esetén 0001, második dinamikus lap esetén 0002 kerül stb.).");
               }

               if (!XmlLoader.this.enable_inFid && XmlLoader.this.mezokey.indexOf("_") > -1) {
                  throw new Exception("Mezőkód ('eazon') hiba! A feladandó állományban az 'eazon' attributum értéke nem tartalmazhat '_' jelet.");
               }

               if (this.codehs.contains(XmlLoader.this.mezokey)) {
                  XmlLoader.this.fatalerror = true;
                  var5 = "";
                  if (XmlLoader.this.mv_nev != null) {
                     var5 = XmlLoader.this.mv_nev + " , " + XmlLoader.this.mv_adoazonosito;
                  } else {
                     var5 = XmlLoader.this.al_megnevezes + " , " + XmlLoader.this.al_azonosito;
                  }

                  throw new SAXException("Albizonylat azonosító : " + var5 + " |  Duplikált mezőkód! " + var4.getValue("eazon"));
               }

               this.codehs.add(XmlLoader.this.mezokey);
               var5 = XmlLoader.this.mezokey;
               XmlLoader.this.mezokey = TemplateUtils.getInstance().keyToDSId(XmlLoader.this.mezokey, XmlLoader.this.lc_fm.id, XmlLoader.this.bm);
               String[] var15 = XmlLoader.this.mezokey.split("_", 2);
               if (var15.length < 2) {
                  String var18 = XmlLoader.this.mezokey;
                  if (var18.length() < 3) {
                     var18 = "Üres a mezőkód!";
                  } else {
                     var18 = "Mezőkód: " + var18;
                  }

                  throw new SAXException("Érvénytelen mezőazonosító! " + var18);
               }

               var7 = 0;

               try {
                  var7 = Integer.parseInt(var15[0]);
               } catch (NumberFormatException var13) {
               }

               ++var7;
               int var8 = XmlLoader.this.lc_fm.get((PageModel)XmlLoader.this.lc_fm.fids_page.get(var15[1]));
               if (XmlLoader.this.lc_fm.fids_page.get(var15[1]) == null) {
                  var8 = -1;
               } else if (((PageModel)XmlLoader.this.lc_fm.fids_page.get(var15[1])).role == 0) {
                  var8 = -1;
               }

               if (var8 != -1 && (((PageModel)XmlLoader.this.lc_fm.fids_page.get(var15[1])).getCalculatedHide() & VisualFieldModel.getmask()) == 0) {
                  var8 = -1;
               }

               if (var8 != -1) {
                  try {
                     DataFieldModel var9 = ((PageModel)XmlLoader.this.lc_fm.fids_page.get(var15[1])).getByCode(var15[1]);
                     if (var9 != null && (var9.role & VisualFieldModel.getmask()) == 0) {
                        var8 = -1;
                     }
                  } catch (Exception var12) {
                     var8 = -1;
                  }
               }

               if (var8 == -1) {
                  XmlLoader.this.fatalerror = true;
                  this.roleerrorflag = true;
                  String var19 = "A sablon nem tartalmazza az adatállományban található (" + var5 + " mezőkódú) mezőt. \nEz az adat nem kerül betöltésre. Ellenőrizze a nyomtatványt!";
                  String var10 = " > " + XmlLoader.this.curindex + ".alnyomtatvány " + XmlLoader.this.lc_fm.id + " Név:" + XmlLoader.this.mv_nev;
                  if (XmlLoader.this.needwarninghead) {
                     XmlLoader.this.warninglist.add(new TextWithIcon(var10, -1));
                     XmlLoader.this.needwarninghead = false;
                  }

                  XmlLoader.this.warninglist.add(new TextWithIcon(var19, 0));
               } else {
                  int var20;
                  try {
                     var20 = ((PageModel)XmlLoader.this.lc_fm.fids_page.get(var15[1])).maxpage;
                  } catch (Exception var11) {
                     var20 = 1;
                  }

                  if (var20 < var7) {
                     throw new SAXException("Nem megengedett dinamikus lapszám!\n Mezőkód: " + var5 + "\n Maximális érték: " + var20);
                  }

                  if (XmlLoader.this.pc[var8] < var7) {
                     XmlLoader.this.pc[var8] = var7;
                  }
               }
            } catch (Exception var14) {
               if (var14 instanceof SAXException) {
                  throw (SAXException)var14;
               }

               throw new SAXException("Érvénytelen mezőkód! " + var4.getValue("eazon") + "\n " + (var14.getMessage() != null ? var14.getMessage() : var14.toString()));
            }
         } else if (var3.equals("mezok")) {
            XmlLoader.this.newdatastore = new GUI_Datastore();
            XmlLoader.this.lc_fm = XmlLoader.this.bm.get(XmlLoader.this.l_nyomtatvanyazonosito);
            if (XmlLoader.this.lc_fm == null) {
               XmlLoader.this.errormsg = "Hibás típusú adatfile! Az alnyomtatvány nem a főnyomtatványhoz tartozik!";
               throw new SAXException(XmlLoader.this.errormsg);
            }

            int var17 = XmlLoader.this.bm.getIndex(XmlLoader.this.bm.get(XmlLoader.this.l_nyomtatvanyazonosito));
            if (XmlLoader.this.bm.maxcreation[var17] < XmlLoader.this.bm.created[var17] + 1) {
               XmlLoader.this.errormsg = "Súlyos hiba! Az " + XmlLoader.this.l_nyomtatvanyazonosito + " típusú résznyomtatványból nem szerepelhet több! Max. = " + XmlLoader.this.bm.maxcreation[var17];
               throw new SAXException(XmlLoader.this.errormsg);
            }

            Boolean var16 = new Boolean(XmlLoader.this.l_nyomtatvanyazonosito.equals(XmlLoader.this.bm.main_document_id));
            XmlLoader.this.lc_elem = new Elem(XmlLoader.this.newdatastore, XmlLoader.this.l_nyomtatvanyazonosito, XmlLoader.this.lc_fm.name, var16);
            XmlLoader.this.pc = new int[XmlLoader.this.lc_fm.size()];

            for(var7 = 0; var7 < XmlLoader.this.pc.length; ++var7) {
               XmlLoader.this.pc[var7] = 1;
            }

            XmlLoader.this.lc_elem.getEtc().put("pagecounts", XmlLoader.this.pc);
            if (XmlLoader.this.l_formnote != null) {
               XmlLoader.this.lc_elem.getEtc().put("orignote", XmlLoader.this.l_formnote);
            }

            Integer var21 = XmlLoader.this._this.getSequence();
            XmlLoader.this.lc_elem.getEtc().put("sn", var21);
            XmlLoader.this._this.add(XmlLoader.this.lc_elem);
            int var10002 = XmlLoader.this.bm.created[var17]++;
            XmlLoader.this.bm.setCalcelemindex(XmlLoader.this.curindex - 1);
            TemplateUtils.getInstance().setDefaultValues(XmlLoader.this.lc_fm.id, XmlLoader.this.bm);
         } else if (var3.equals("head")) {
            XmlLoader.this.head = new Hashtable();
            XmlLoader.this.attributes_done(var4, XmlLoader.this.head);
         } else if (var3.equals("docinfo")) {
            XmlLoader.this.docinfo = new Hashtable();
            XmlLoader.this.attributes_done(var4, XmlLoader.this.docinfo);
         } else if (var3.equals("type")) {
            XmlLoader.this.in_type = true;
         } else if (var3.equals("saved")) {
            XmlLoader.this.in_saved = true;
         } else if (var3.equals("nyomtatvanyok")) {
            XmlLoader.this.nyomtatvanyok = new Hashtable();
            XmlLoader.this.attributes_done(var4, XmlLoader.this.nyomtatvanyok);
         } else if (var3.equals("hibakszama")) {
            XmlLoader.this.in_hibakszama = true;
         } else if (var3.equals("hash")) {
            XmlLoader.this.in_hash = true;
         } else if (var3.equals("muvelet")) {
            XmlLoader.this.in_muvelet = true;
         } else if (var3.equals("programverzio")) {
            XmlLoader.this.in_programverzio = true;
            XmlLoader.this.abev_programverzio = "";
         } else if (var3.equals("nyomtatvany")) {
            XmlLoader.this.clearvars();
            this.codehs = new HashSet(100);
            XmlLoader.this.lc_elem = null;
            ++XmlLoader.this.curindex;
            GuiUtil.setGlassLabel("Betöltve:" + XmlLoader.this.curindex);
         } else if (var3.equals("nyomtatvanyazonosito")) {
            XmlLoader.this.in_nyomtatvanyazonosito = true;
            XmlLoader.this.l_nyomtatvanyazonosito = "";
         } else if (var3.equals("nyomtatvanyverzio")) {
            XmlLoader.this.in_nyomtatvanyverzio = true;
            XmlLoader.this.l_nyomtatvanyverzio = "";
         } else if (var3.equals("tol")) {
            XmlLoader.this.in_tol = true;
            XmlLoader.this.l_tol = "";
         } else if (var3.equals("ig")) {
            XmlLoader.this.in_ig = true;
            XmlLoader.this.l_ig = "";
         } else if (var3.equals("megjegyzes")) {
            XmlLoader.this.in_megjegyzes = true;
            if (XmlLoader.this.in_firstform) {
               XmlLoader.this.l_megjegyzes = "";
            }

            XmlLoader.this.l_formnote = "";
         } else if (var3.equals("adoszam")) {
            if (XmlLoader.this.in_adozo) {
               XmlLoader.this.in_a_adoszam = true;
               XmlLoader.this.a_adoszam = "";
            } else {
               XmlLoader.this.in_mv_adoszam = true;
               XmlLoader.this.mv_adoszam = "";
            }
         } else if (var3.equals("adoazonosito")) {
            if (XmlLoader.this.in_adozo) {
               XmlLoader.this.in_a_adoazonosito = true;
               XmlLoader.this.a_adoazonosito = "";
            } else {
               XmlLoader.this.in_mv_adoazonosito = true;
               XmlLoader.this.mv_adoazonosito = "";
            }
         } else if (var3.equals("nev")) {
            if (XmlLoader.this.in_adozo) {
               XmlLoader.this.in_a_nev = true;
               XmlLoader.this.a_nev = "";
            } else {
               XmlLoader.this.in_mv_nev = true;
               XmlLoader.this.mv_nev = "";
            }
         } else if (var3.equals("adozo")) {
            XmlLoader.this.in_adozo = true;
         } else if (var3.equals("albizonylatazonositas")) {
            XmlLoader.this.in_albizonylat = true;
         } else if (var3.equals("megnevezes")) {
            XmlLoader.this.in_al_megnevezes = true;
            XmlLoader.this.al_megnevezes = "";
         } else if (var3.equals("azonosito")) {
            XmlLoader.this.in_al_azonosito = true;
            XmlLoader.this.al_azonosito = "";
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
         if (XmlLoader.this.in_nyomtatvanyazonosito) {
            String var4 = new String(var1, var2, var3);
            XmlLoader.this.head.put("nyomtatvanyazonosito", var4);
         } else if (XmlLoader.this.in_nyomtatvanyverzio) {
            XmlLoader.this.head.put("nyomtatvanyverzio", new String(var1, var2, var3));
         } else if (XmlLoader.this.in_tol) {
            XmlLoader.this.l_tol = new String(var1, var2, var3);
         } else if (XmlLoader.this.in_ig) {
            XmlLoader.this.l_ig = new String(var1, var2, var3);
         } else if (XmlLoader.this.in_megjegyzes) {
            XmlLoader.this.l_megjegyzes = new String(var1, var2, var3);
         } else if (XmlLoader.this.in_a_adoszam) {
            XmlLoader.this.a_adoszam = new String(var1, var2, var3);
         } else if (XmlLoader.this.in_a_adoazonosito) {
            XmlLoader.this.a_adoazonosito = new String(var1, var2, var3);
         } else if (XmlLoader.this.in_a_nev) {
            XmlLoader.this.a_nev = new String(var1, var2, var3);
         } else if (XmlLoader.this.in_mv_adoszam) {
            XmlLoader.this.mv_adoszam = new String(var1, var2, var3);
         } else if (XmlLoader.this.in_mv_adoazonosito) {
            XmlLoader.this.mv_adoazonosito = new String(var1, var2, var3);
         } else if (XmlLoader.this.in_mv_nev) {
            XmlLoader.this.mv_nev = new String(var1, var2, var3);
         } else if (XmlLoader.this.in_al_megnevezes) {
            XmlLoader.this.al_megnevezes = XmlLoader.this.al_megnevezes + new String(var1, var2, var3);
         } else if (XmlLoader.this.in_al_azonosito) {
            XmlLoader.this.al_azonosito = XmlLoader.this.al_azonosito + new String(var1, var2, var3);
         } else if (XmlLoader.this.in_muvelet) {
            XmlLoader.this.muvelet = new String(var1, var2, var3);
         }

      }

      public void endDocument() throws SAXException {
         super.endDocument();
      }

      public void endElement(String var1, String var2, String var3) throws SAXException {
         if (var3.equals("nyomtatvanyinformacio")) {
            if (XmlLoader.this.docinfo != null) {
               XmlLoader.this.head.put("docinfo", XmlLoader.this.docinfo);
            } else {
               XmlLoader.this.head.put("docinfo", new Hashtable());
            }

            throw new SAXException("OUT");
         } else {
            if (var3.equals("nyomtatvanyazonosito")) {
               XmlLoader.this.in_nyomtatvanyazonosito = false;
            } else if (var3.equals("nyomtatvanyverzio")) {
               XmlLoader.this.in_nyomtatvanyverzio = false;
            } else if (var3.equals("tol")) {
               XmlLoader.this.in_tol = false;
            } else if (var3.equals("ig")) {
               XmlLoader.this.in_ig = false;
            } else if (var3.equals("megjegyzes")) {
               XmlLoader.this.in_megjegyzes = false;
            } else if (var3.equals("adoszam")) {
               if (XmlLoader.this.in_adozo) {
                  XmlLoader.this.in_a_adoszam = false;
               } else {
                  XmlLoader.this.in_mv_adoszam = false;
               }
            } else if (var3.equals("adoazonosito")) {
               if (XmlLoader.this.in_adozo) {
                  XmlLoader.this.in_a_adoazonosito = false;
               } else {
                  XmlLoader.this.in_mv_adoazonosito = false;
               }
            } else if (var3.equals("nev")) {
               if (XmlLoader.this.in_adozo) {
                  XmlLoader.this.in_a_nev = false;
               } else {
                  XmlLoader.this.in_mv_nev = false;
               }
            } else if (var3.equals("adozo")) {
               XmlLoader.this.in_adozo = false;
            } else if (var3.equals("albizonylatazonositas")) {
               XmlLoader.this.in_albizonylat = false;
            } else if (var3.equals("megnevezes")) {
               XmlLoader.this.in_al_megnevezes = false;
            } else if (var3.equals("azonosito")) {
               XmlLoader.this.in_al_azonosito = false;
            } else if (var3.equals("muvelet")) {
               XmlLoader.this.in_muvelet = false;
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
            XmlLoader.this.head = new Hashtable();
            XmlLoader.this.attributes_done(var4, XmlLoader.this.head);
         } else if (var3.equals("docinfo")) {
            XmlLoader.this.docinfo = new Hashtable();
            XmlLoader.this.attributes_done(var4, XmlLoader.this.docinfo);
         } else if (var3.equals("nyomtatvanyazonosito")) {
            XmlLoader.this.in_nyomtatvanyazonosito = true;
         } else if (var3.equals("nyomtatvanyverzio")) {
            XmlLoader.this.in_nyomtatvanyverzio = true;
         } else if (var3.equals("tol")) {
            XmlLoader.this.in_tol = true;
         } else if (var3.equals("ig")) {
            XmlLoader.this.in_ig = true;
         } else if (var3.equals("megjegyzes")) {
            XmlLoader.this.in_megjegyzes = true;
         } else if (var3.equals("adoszam")) {
            if (XmlLoader.this.in_adozo) {
               XmlLoader.this.in_a_adoszam = true;
            } else {
               XmlLoader.this.in_mv_adoszam = true;
            }
         } else if (var3.equals("adoazonosito")) {
            if (XmlLoader.this.in_adozo) {
               XmlLoader.this.in_a_adoazonosito = true;
            } else {
               XmlLoader.this.in_mv_adoazonosito = true;
            }
         } else if (var3.equals("nev")) {
            if (XmlLoader.this.in_adozo) {
               XmlLoader.this.in_a_nev = true;
            } else {
               XmlLoader.this.in_mv_nev = true;
            }
         } else if (var3.equals("adozo")) {
            XmlLoader.this.in_adozo = true;
         } else if (var3.equals("albizonylatazonositas")) {
            XmlLoader.this.in_albizonylat = true;
         } else if (var3.equals("megnevezes")) {
            XmlLoader.this.in_al_megnevezes = true;
            XmlLoader.this.al_megnevezes = "";
         } else if (var3.equals("azonosito")) {
            XmlLoader.this.in_al_azonosito = true;
            XmlLoader.this.al_azonosito = "";
         } else if (var3.equals("muvelet")) {
            XmlLoader.this.in_muvelet = true;
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
