package hu.piller.enykp.alogic.fileloader.db;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.fileloader.xml.XmlLoader;
import hu.piller.enykp.alogic.fileutil.HeadChecker;
import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.datastore.CachedCollection;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.GUI_Datastore;
import hu.piller.enykp.extensions.db.DbFactory;
import hu.piller.enykp.extensions.db.IDbHandler;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.interfaces.ILoadManager;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import java.awt.Frame;
import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class DbLoader implements ILoadManager {
   public static final int NONE = -1;
   public static final int CLOB = 0;
   public static final int BLOB = 1;
   public static final int FIELDS = 2;
   public static final int BLOB_DOKU = 3;
   String loaderid;
   String suffix;
   String description;
   IDbHandler dbhandler;
   public BookModel bm;
   static BookModel prev_bm = null;
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
   public String a_adoszam;
   public String a_adoazonosito;
   public String a_nev;
   public String mv_adoszam;
   public String mv_adoazonosito;
   public String mv_nev;
   public boolean hasError;
   public String errormsg;
   public Vector headerrorlist;
   public Vector warninglist;
   public boolean silent = false;
   public boolean silentheadcheck = false;
   private boolean onlyhead;
   private int type;
   private boolean fromdoku = false;
   String param;
   String storedproc;
   String sptype;
   String storedproc_w;
   Hashtable parht;
   String encoding;
   int skipcount;
   public static int db = 0;
   public static int max = 0;

   public DbLoader(int var1, String var2) {
      this.type = var1;
      if (this.type == 3) {
         this.type = 1;
         this.fromdoku = true;
      }

      this.param = var2;
      this.loaderid = "db_data_loader_v1";
      this.suffix = "";
      this.description = "Adatbázis forrás";
      this.onlyhead = false;

      try {
         this.dbhandler = DbFactory.getDbHandler();
      } catch (Exception var4) {
         this.dbhandler = null;
      }

      this.bm = null;
   }

   public DbLoader(int var1, String var2, String var3) {
      this.type = var1;
      if (this.type == 3) {
         this.type = 1;
         this.fromdoku = true;
      }

      this.param = var2;
      this.storedproc = var3;
      this.loaderid = "db_data_loader_v1";
      this.suffix = "";
      this.description = "Adatbázis forrás";
      this.onlyhead = false;

      try {
         this.dbhandler = DbFactory.getDbHandler();
      } catch (Exception var5) {
         this.dbhandler = null;
      }

      this.bm = null;
   }

   public DbLoader(String var1, String var2, String var3) {
      this.type = -1;
      if (this.type == 3) {
         this.type = 1;
         this.fromdoku = true;
      }

      this.param = var1;
      this.storedproc = var2;
      this.sptype = var3;
      this.loaderid = "db_data_loader_v1";
      this.suffix = "";
      this.description = "Adatbázis forrás";
      this.onlyhead = false;

      try {
         this.dbhandler = DbFactory.getDbHandler();
      } catch (Exception var5) {
         this.dbhandler = null;
      }

      this.bm = null;
   }

   public void setDbhandler(IDbHandler var1) {
      this.dbhandler = var1;
   }

   public String getId() {
      return this.loaderid;
   }

   public void setStoredproc_w(String var1) {
      this.storedproc_w = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public Hashtable getHeadData(File var1) {
      if (this.dbhandler == null) {
         return null;
      } else {
         Hashtable var2 = new Hashtable();
         int var4;
         byte[] var5;
         String var6;
         int var7;
         int var8;
         int var9;
         InputStream var14;
         XmlLoader var16;
         Hashtable var18;
         switch(this.type) {
         case -1:
            try {
               if (this.parht == null) {
                  this.parht = new Hashtable();
                  this.parht.put("JABEV_PARAMS_ARRAY", new String[]{this.param});
               }

               this.parht.put("LOB_TYPE", this.sptype);
               if (this.storedproc != null) {
                  this.parht.put("BLOL_STORED_PROCEDURE_NAME", this.storedproc);
               }

               var14 = this.dbhandler.getXMLByStream_New(this.parht);
               this.skipcount = 0;
               this.encoding = "";
               var4 = var14.read();
               if (var4 == 255) {
                  this.encoding = "UTF-16LE";
               }

               if (var4 == 254) {
                  this.encoding = "UTF-16BE";
               }

               if (var4 == 239) {
                  this.skipcount = 3;
               }

               if (this.encoding.equals("")) {
                  var5 = new byte[128];
                  var14.read(var5);
                  var6 = new String(var5);
                  var7 = var6.indexOf("encoding=");
                  var8 = var6.indexOf("\"", var7);
                  var9 = var6.indexOf("\"", var8 + 1);
                  if (var8 == -1) {
                     var8 = var6.indexOf("'", var7);
                     var9 = var6.indexOf("'", var8 + 1);
                  }

                  this.encoding = var6.substring(var8 + 1, var9);
               }

               this.dbhandler.closeStatement();
               var14 = this.dbhandler.getXMLByStream_New(this.parht);
               if (this.skipcount != 0) {
                  var14.skip((long)this.skipcount);
               }

               var16 = new XmlLoader();
               var18 = var16.getHeadData(var14, this.encoding);
               this.dbhandler.closeStatement();
               return var18;
            } catch (Exception var12) {
               var12.printStackTrace();
               return null;
            }
         case 0:
            try {
               this.parht.put("LOB_TYPE", "C");
               var14 = this.dbhandler.getXMLByStream(this.parht);
               XmlLoader var15 = new XmlLoader();
               Hashtable var17 = var15.getHeadData(var14, "utf-8");
               this.dbhandler.closeStatement();
               return var17;
            } catch (Exception var11) {
               var11.printStackTrace();
               return null;
            }
         case 1:
            try {
               if (this.parht == null) {
                  this.parht = new Hashtable();
                  this.parht.put("JABEV_PARAMS_ARRAY", new String[]{this.param});
               }

               this.parht.put("LOB_TYPE", this.fromdoku ? "BX" : "B");
               if (this.storedproc != null) {
                  var2.put("BLOL_STORED_PROCEDURE_NAME", this.storedproc);
               }

               var14 = this.dbhandler.getXMLByStream(this.parht);
               this.skipcount = 0;
               this.encoding = "";
               var4 = var14.read();
               if (var4 == 255) {
                  this.encoding = "UTF-16LE";
               }

               if (var4 == 254) {
                  this.encoding = "UTF-16BE";
               }

               if (var4 == 239) {
                  this.skipcount = 3;
               }

               if (this.encoding.equals("")) {
                  var5 = new byte[128];
                  var14.read(var5);
                  var6 = new String(var5);
                  var7 = var6.indexOf("encoding=");
                  var8 = var6.indexOf("\"", var7);
                  var9 = var6.indexOf("\"", var8 + 1);
                  if (var8 == -1) {
                     var8 = var6.indexOf("'", var7);
                     var9 = var6.indexOf("'", var8 + 1);
                  }

                  this.encoding = var6.substring(var8 + 1, var9);
               }

               this.dbhandler.closeStatement();
               var14 = this.dbhandler.getXMLByStream(this.parht);
               if (this.skipcount != 0) {
                  var14.skip((long)this.skipcount);
               }

               var16 = new XmlLoader();
               var18 = var16.getHeadData(var14, this.encoding);
               this.dbhandler.closeStatement();
               return var18;
            } catch (Exception var10) {
               var10.printStackTrace();
               return null;
            }
         case 2:
            try {
               Vector var3 = new Vector();
               return (Hashtable)var3.get(0);
            } catch (Exception var13) {
               var13.printStackTrace();
            }
         default:
            return null;
         }
      }
   }

   public BookModel load(String var1, String var2, String var3, String var4) {
      return this.load(var1, var2, var3, var4, true);
   }

   public BookModel load(String var1, String var2, String var3, String var4, boolean var5) {
      this.silent = var5;
      this.warninglist = new Vector();
      BookModel var6 = new BookModel();
      var6.hasError = true;
      if (this.dbhandler == null) {
         var6.errormsg = "Nincs megfelelő adatbáziskezelő!";
         return var6;
      } else {
         try {
            switch(this.type) {
            case -1:
               return this.done_none(var6);
            case 0:
               return this.done_clob(var6);
            case 1:
               return this.done_blob(var6);
            case 2:
               return this.done_fields(var6);
            }
         } catch (Exception var8) {
            var6.errormsg = var8.getMessage();
         }

         return var6;
      }
   }

   public BookModel load(String var1, String var2, String var3, String var4, BookModel var5) {
      if (var5 == null) {
         return this.load(var1, var2, var3, var4);
      } else {
         try {
            switch(this.type) {
            case 0:
               return this.done_clob(prev_bm);
            case 1:
               return this.done_blob(prev_bm);
            case 2:
               return this.done_fields(prev_bm);
            }
         } catch (Exception var7) {
            prev_bm.errormsg = var7.getMessage();
         }

         return prev_bm;
      }
   }

   public String getFileNameSuffix() {
      return this.suffix;
   }

   public String createFileName(String var1) {
      return var1.endsWith(this.suffix) ? var1 : var1 + this.suffix;
   }

   private void load(Vector var1) {
      try {
         Hashtable var2 = (Hashtable)var1.get(0);
         this.l_megjegyzes = (String)var2.get("note");

         for(int var3 = 1; var3 < var1.size(); ++var3) {
            try {
               Object[] var4 = (Object[])((Object[])var1.get(var3));
               Hashtable var5 = (Hashtable)var4[0];
               Hashtable var6 = (Hashtable)var4[1];
               this.done_form(var5, var6, var3);
            } catch (Exception var7) {
               var7.printStackTrace();
            }
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   private void done_form(Hashtable var1, Hashtable var2, int var3) {
      GUI_Datastore var4 = new GUI_Datastore();
      this.l_nyomtatvanyazonosito = (String)var1.get("nyomtatvanyazonosito");
      FormModel var5 = this.bm.get(this.l_nyomtatvanyazonosito);
      if (var5 == null) {
         this.errormsg = "Hibás típusú adatfile!";
      } else {
         Boolean var6 = new Boolean(this.l_nyomtatvanyazonosito.equals(this.bm.main_document_id));
         Elem var7 = new Elem(var4, this.l_nyomtatvanyazonosito, var5.name, var6);
         int[] var8 = new int[var5.size()];

         for(int var9 = 0; var9 < var8.length; ++var9) {
            var8[var9] = 1;
         }

         var7.getEtc().put("pagecounts", var8);
         this._this.add(var7);
         this.bm.setCalcelemindex(var3 - 1);
         TemplateUtils.getInstance().setDefaultValues(var5.id, this.bm);

         String var11;
         String var15;
         for(Enumeration var20 = var2.keys(); var20.hasMoreElements(); var4.set(var11, var15)) {
            String var10 = (String)var20.nextElement();
            var11 = TemplateUtils.getInstance().keyToDSId(var10, var5.id, this.bm);
            String[] var12 = var11.split("_", 2);
            if (var12.length < 2) {
               return;
            }

            int var13 = 0;

            try {
               var13 = Integer.parseInt(var12[0]);
            } catch (NumberFormatException var19) {
            }

            ++var13;
            int var14 = var5.get((PageModel)var5.fids_page.get(var12[1]));
            if (var14 == -1) {
               var15 = "A sablon nem tartalmazza az adatállományban található (" + var12[1] + " mezőkódú) mezőt.\nEz az adat nem kerül betöltésre. Ellenőrizze a nyomtatványt!";
               this.warninglist.add(new TextWithIcon(var15, 4));
            } else if (var8[var14] < var13) {
               var8[var14] = var13;
            }

            var15 = (String)var2.get(var10);
            String var16 = var11.substring(var11.indexOf("_") + 1);
            boolean var17 = ((DataFieldModel)this.bm.get(var5.id).fids.get(var16)).features.get("datatype").equals("check");
            if (var17) {
               if (var15.equals("X")) {
                  var15 = "true";
               } else {
                  System.out.println("Súlyos hiba!!!!!!!! (logikainál nem X) " + var15);
                  var15 = "false";
               }
            }
         }

         CalculatorManager.getInstance().doBetoltErtekCalcs(true);
         CalculatorManager.getInstance().multi_form_load();
         boolean var21 = false;
         if (var21) {
            HeadChecker var22 = HeadChecker.getInstance();
            Hashtable var23 = new Hashtable();
            if (this.a_nev != null) {
               var23.put("nev", this.a_nev);
            }

            if (this.a_adoszam != null) {
               var23.put("adoszam", this.a_adoszam);
            }

            if (this.a_adoazonosito != null) {
               var23.put("adoazonosito", this.a_adoazonosito);
            }

            if (this.mv_nev != null) {
               var23.put("munkavallalo/nev", this.mv_nev);
            }

            if (this.mv_adoazonosito != null) {
               var23.put("munkavallalo/adoazonosito", this.mv_adoazonosito);
            }

            if (this.l_tol != null) {
               var23.put("tol", this.l_tol);
            }

            if (this.l_ig != null) {
               var23.put("ig", this.l_ig);
            }

            String var24 = null;

            try {
               var24 = (String)this.bm.docinfo.get("org");
            } catch (Exception var18) {
               var24 = "APEH";
            }

            Object[] var25 = new Object[]{var23, var24, this.l_nyomtatvanyazonosito, new Integer(var3 - 1), var4};
            this.headerrorlist = var22.headCheck(var25, this.bm);
            if (this.headerrorlist.size() != 0 && !this.silent && !this.silentheadcheck) {
               GuiUtil.showErrorDialog((Frame)null, "HeadCheck", true, false, this.headerrorlist);
            }
         }

      }
   }

   private BookModel done_fields(BookModel var1) {
      try {
         new Hashtable();
         Vector var3 = this.make_sample_data();
         Hashtable var4 = (Hashtable)((Object[])((Object[])var3.get(1)))[0];
         String var5 = (String)var4.get("nyomtatvanyazonosito");
         String var6 = (String)var4.get("nyomtatvanyverzio");
         String var7 = (String)var4.get("org");
         File var8 = new File(TemplateChecker.getInstance().getTemplateFileNames(var5, var6, var7).getTemplateFileNames()[0]);
         if (!var8.exists()) {
            var1.errormsg = "Nincs megfelelő sablon!";
            return var1;
         } else {
            this.bm = new BookModel(var8);
            if (this.bm.hasError) {
               return this.bm;
            } else {
               this._this = this.bm.cc;
               this.load(var3);
               if (this.hasError) {
                  var1.errormsg = this.errormsg;
                  return var1;
               } else {
                  this.bm.cc.setLoadedfile((File)null);
                  this.bm.cc.l_megjegyzes = this.l_megjegyzes;
                  if (this.warninglist.size() != 0) {
                     if (this.silent) {
                        this.bm.warninglist = this.warninglist;
                     } else {
                        GuiUtil.showErrorDialog((Frame)null, "Figyelmeztetések", true, true, this.warninglist);
                     }
                  }

                  return this.bm;
               }
            }
         }
      } catch (Exception var9) {
         var9.printStackTrace();
         return var1;
      }
   }

   private BookModel done_clob(BookModel var1) {
      try {
         Hashtable var2 = this.getHeadData((File)null);
         Hashtable var3 = (Hashtable)var2.get("docinfo");
         String var4 = (String)var3.get("id");
         String var5 = (String)var3.get("ver");
         String var6 = (String)var3.get("org");
         Hashtable var7 = new Hashtable();
         var7.put("JABEV_PARAMS_ARRAY", new String[]{this.param});
         var7.put("LOB_TYPE", "C");
         new XmlLoader();
         this.dbhandler.closeStatement();
         return this.bm;
      } catch (Exception var9) {
         var9.printStackTrace();
         var1.errormsg = var9.getMessage();
         return var1;
      }
   }

   private BookModel done_blob(BookModel var1) {
      try {
         Hashtable var2 = this.getHeadData((File)null);
         if (var2 == null) {
            var1.errormsg = "Nem található a bárkódhoz tartozó bejegyzés!";
            return var1;
         } else {
            Hashtable var3 = (Hashtable)var2.get("docinfo");
            String var4 = (String)var3.get("id");
            String var5 = (String)var3.get("ver");
            String var6 = (String)var3.get("org");
            Hashtable var7 = new Hashtable();
            var7.put("JABEV_PARAMS_ARRAY", new String[]{this.param});
            var7.put("LOB_TYPE", this.fromdoku ? "BX" : "B");
            if (this.storedproc != null) {
               var7.put("BLOL_STORED_PROCEDURE_NAME", this.storedproc);
            }

            InputStream var8 = this.dbhandler.getXMLByStream(var7);
            this.parht.put("LOB_TYPE", "B");
            XmlLoader var9 = new XmlLoader();
            var9.silentheadcheck = true;
            prev_bm = var9.load(var8, var4, var5, var6, prev_bm, this.encoding);
            var9.silentheadcheck = false;
            this.dbhandler.closeStatement();
            return prev_bm;
         }
      } catch (Exception var10) {
         var10.printStackTrace();
         var1.errormsg = var10.getMessage();
         if (var1.errormsg == null) {
            var1.errormsg = var10.toString();
         }

         return var1;
      }
   }

   private BookModel done_none(BookModel var1) {
      try {
         Hashtable var2 = this.getHeadData((File)null);
         if (var2 == null) {
            var1.errormsg = "Nem található a bárkódhoz tartozó bejegyzés!";
            return var1;
         } else {
            Hashtable var3 = (Hashtable)var2.get("docinfo");
            String var4 = (String)var3.get("id");
            String var5 = (String)var3.get("ver");
            String var6 = (String)var3.get("org");
            Hashtable var7 = new Hashtable();
            var7.put("JABEV_PARAMS_ARRAY", new String[]{this.param});
            var7.put("LOB_TYPE", this.sptype);
            if (this.storedproc != null) {
               var7.put("BLOL_STORED_PROCEDURE_NAME", this.storedproc);
            }

            InputStream var8 = this.dbhandler.getXMLByStream_New(var7);
            this.parht.put("LOB_TYPE", "B");
            XmlLoader var9 = new XmlLoader();
            var9.silentheadcheck = true;
            prev_bm = var9.load(var8, var4, var5, var6, prev_bm, this.encoding);
            var9.silentheadcheck = false;
            this.dbhandler.closeStatement();
            return prev_bm;
         }
      } catch (Exception var10) {
         var10.printStackTrace();
         var1.errormsg = var10.getMessage();
         if (var1.errormsg == null) {
            var1.errormsg = var10.toString();
         }

         return var1;
      }
   }

   public boolean save(Vector var1) {
      try {
         Hashtable var2 = new Hashtable();
         var2.put("JABEV_PARAMS_ARRAY", new String[]{this.param});
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return false;
   }

   public Hashtable check() {
      boolean var1 = false;
      Hashtable var2 = new Hashtable();
      if (this.dbhandler == null) {
         var2.put("errormsg", "Nincs megfelelő adatbáziskezelő!");
         var2.put("stop", "stop");
         return var2;
      } else {
         try {
            long var3 = System.currentTimeMillis();
            Hashtable var5 = new Hashtable();
            var5.put("TASKTYPE", "GETNEXTTASK");
            this.parht = this.dbhandler.getNextTask(var5);
            if (var1) {
               System.out.println("getnexttask time=" + (System.currentTimeMillis() - var3));
            }

            String[] var6 = (String[])((String[])this.parht.get("JABEV_FUNCTIONS_ARRAY"));

            try {
               if (var6[0].equals("0")) {
                  var2.put("idle", "idle");
                  return var2;
               }

               if (var6[0].equals("2")) {
                  var2.put("stop", "stop");
                  return var2;
               }
            } catch (Exception var8) {
            }

            switch(this.type) {
            case 0:
            case 2:
               this.parht.put("LOB_TYPE", "C");
               break;
            case 1:
               this.parht.put("LOB_TYPE", "B");
               return this.DONE(var2);
            }
         } catch (Exception var9) {
            var2.put("stop", "stop");
            var2.put("errormsg", var9.getMessage() == null ? var9.toString() : var9.getMessage());
         }

         return var2;
      }
   }

   private Hashtable DONE(Hashtable var1) {
      boolean var2 = false;
      String var3 = "";
      String var4 = "";
      String var5 = "";
      long var6 = System.currentTimeMillis();
      BookModel var8 = this.load((String)null, var3, var4, var5, prev_bm);
      db += var8.cc.size();
      if (max < var8.cc.size()) {
         max = var8.cc.size();
      }

      if (var2) {
         System.out.println("load time=" + (System.currentTimeMillis() - var6));
      }

      prev_bm = var8;
      if (!var8.hasError) {
         try {
            var6 = System.currentTimeMillis();
            Vector var9 = Tools.check();
            if (var2) {
               System.out.println("check time=" + (System.currentTimeMillis() - var6) + "   errorcount=" + var9.size());
            }

            var6 = System.currentTimeMillis();
            if (var2) {
               System.out.println("save time=" + (System.currentTimeMillis() - var6));
            }
         } catch (Exception var10) {
            var10.printStackTrace();
         }
      } else {
         System.out.println("Hiba: " + var8.errormsg);
      }

      return var1;
   }

   private Vector make_sample_data() {
      Vector var1 = new Vector();
      Hashtable var2 = new Hashtable();
      var2.put("hibakszama", "-1");
      var2.put("hash", "5656daae6badb403953e8dfa59b9834ed989fa32");
      var2.put("programverzio", "v.1.0.4-1");
      var1.add(var2);
      Hashtable var3 = new Hashtable();
      var3.put("nyomtatvanyazonosito", "0808A");
      var3.put("nyomtatvanyverzio", "1.1");
      var3.put("adozo.nev", "almafa");
      var3.put("adozo.adoszam", "12593611210");
      var3.put("idoszak.tolig", "20080101");
      var3.put("idoszak.ig", "20080131");
      var3.put("megjegyzes", "");
      Hashtable var4 = new Hashtable();
      var4.put("0A0001C003A", "12593611210");
      var4.put("0A0001C008A", "almafa");
      var4.put("0A0001C009A", "1100");
      var4.put("0A0001C010A", "gsdgfsd");
      var4.put("0A0001C011A", "43");
      var4.put("0A0001C012A", "autóút");
      var4.put("0A0001C013A", "tretretg");
      var4.put("0A0001C014A", "4234");
      var4.put("0A0001C015A", "4324");
      var4.put("0A0001C016A", "43");
      var4.put("0A0001C017A", "24");
      var4.put("0A0001C029A", "20080101");
      var4.put("0A0001C030A", "20080131");
      var4.put("0A0001C039A", "8");
      var4.put("0B0001B001A", "12593611210");
      var4.put("0C0001B001A", "12593611210");
      var4.put("0D0001B001A", "12593611210");
      var4.put("0E0001B001A", "12593611210");
      var4.put("0F0001B001A", "12593611210");
      var4.put("0G0001B001A", "12593611210");
      var4.put("0H0001B001A", "12593611210");
      var4.put("0I0001B001A", "12593611210");
      var4.put("0J0001B001A", "12593611210");
      var4.put("0K0001B001A", "12593611210");
      var4.put("0L0001B001A", "12593611210");
      var4.put("0M0001B001A", "1");
      var4.put("0M0001B002A", "almafa");
      var4.put("0M0001B004A", "12593611210");
      Object[] var5 = new Object[]{var3, var4};
      var1.add(var5);
      return var1;
   }
}
