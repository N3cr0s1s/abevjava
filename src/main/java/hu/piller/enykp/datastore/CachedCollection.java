package hu.piller.enykp.datastore;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.fileutil.DataChecker;
import hu.piller.enykp.alogic.fileutil.DatastoreKeyToXml;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.gui.model.VisualFieldModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.ILoadManager;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import hu.piller.enykp.util.base.eventsupport.DefaultEventSupport;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class CachedCollection extends FileMappedList implements IEventSupport, IPropertyList, ILoadManager {
   String loaderid;
   String suffix;
   String description;
   private File loadedfile;
   private boolean nosettitle;
   Elem lc_elem;
   public BookModel bm;
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
   public String a_adoszam;
   public String a_adoazonosito;
   public String a_nev;
   public String mv_adoszam;
   public String mv_adoazonosito;
   public String mv_nev;
   String mezokey;
   String mezovalue;
   IDataStore newdatastore;
   Vector reszletek;
   String reszletertek;
   String reszleteksum;
   String reszletnote;
   FormModel lc_fm;
   int[] pc;
   private int curindex;
   private boolean single;
   private boolean onlyhead;
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
   private boolean in_reszlet;
   private DefaultEventSupport des;
   private Object activeObject;
   private Hashtable activeObjects;
   public boolean hasError;
   public String errormsg;
   public boolean addmode;
   public Vector deletedparams;
   public Hashtable all_kihatas_ht;
   public DefaultTableModel megallapitasok;
   public Vector adonemek;
   public int sequence;
   public String lc_snstr;

   public CachedCollection() {
      this(60010);
   }

   public CachedCollection(int var1) {
      super(var1, 100);
      this.nosettitle = false;
      this.single = true;
      this.des = new DefaultEventSupport();
      this.activeObject = null;
      this.activeObjects = new Hashtable();
      this.addmode = false;
      this.init();
   }

   private void init() {
      this.loaderid = "inner_data_loader_v1";
      this.suffix = ".frm.enyk";
      this.description = "Nyomtatványok";
      this.deletedparams = new Vector();
      this.sequence = 0;
      this.docinfo = new Hashtable();
   }

   public void setNoCacheMode() {
      this.setMaxInMemory(-1);
   }

   public Object getActiveObject() {
      try {
         if (((Elem)this.activeObject).getRef() == null) {
            this.get((Elem)this.activeObject);
         }
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
      }

      return this.activeObject;
   }

   public void setActiveObject(Object var1) {
      if (var1 != null) {
         if (!(var1 instanceof Elem)) {
            this.activeObject = var1;
         } else {
            this.get((Elem)var1);
            this.activeObject = var1;
            if (this.activeObjects.get("currentActiveObject") != null) {
               this.activeObjects.put("previousActiveObject", this.activeObjects.get("currentActiveObject"));
            }

            this.activeObjects.put("currentActiveObject", var1);
            Hashtable var2 = new Hashtable();
            var2.put("id", ((Elem)var1).getType());
            var2.put("guiobject", var1);
            if (this.bm == null) {
               System.out.println("BookModel is null");
            }

            if (this.bm.calculator == null) {
               System.out.println("BookModel contains reference of calculator is null");
            }

            this.bm.calculator.eventFired(var2);
            this.bm.setCalcelemindex(this.bm.cc.getActiveObjectindex());
         }
      }
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

   public Elem get(Elem var1) {
      return var1 == null ? var1 : (Elem)super.get(var1);
   }

   public int getIndex(Elem var1) {
      for(int var2 = 0; var2 < this.size(); ++var2) {
         if (this.getquick(var2).equals(var1)) {
            return var2;
         }
      }

      return -1;
   }

   public boolean set(Object var1, Object var2) {
      try {
         if (var1.equals("setActiveObject")) {
            this.setActiveObject(var2);
            return true;
         } else {
            return false;
         }
      } catch (Exception var4) {
         var4.printStackTrace();
         return false;
      }
   }

   public Object get(Object var1) {
      if (!(var1 instanceof Object[])) {
         return var1.equals("getActiveObject") ? this.getActiveObject() : null;
      } else {
         try {
            Object[] var2 = (Object[])((Object[])var1);
            int[] var3;
            int var4;
            if (var2[0].equals("filter")) {
               var3 = new int[this.size() + 1];
               var3[this.size()] = -1;
               var4 = 0;

               for(int var11 = 0; var11 < this.size(); ++var11) {
                  Elem var6 = (Elem)super.get(var11);
                  var3[var11] = -1;

                  try {
                     if (var6.getType().equals(var2[1])) {
                        var3[var4++] = var11;
                     }
                  } catch (Exception var8) {
                  }
               }

               return var3;
            } else if (!var2[0].equals("filterfirstitem")) {
               return null;
            } else {
               var3 = new int[]{-1};

               for(var4 = 0; var4 < this.size(); ++var4) {
                  Elem var5 = (Elem)super.get(var4);

                  try {
                     if (var5.getType().equals(var2[1])) {
                        var3[0] = var4;
                        break;
                     }
                  } catch (Exception var9) {
                  }
               }

               return var3;
            }
         } catch (Exception var10) {
            var10.printStackTrace();
            return null;
         }
      }
   }

   public void destroy() {
      super.destroy(true);
      this.bm = null;
   }

   public void close() {
   }

   public void writeAll() {
   }

   public void loadItem(Elem var1) throws Exception {
      this.get(var1);
   }

   public boolean remove(Object var1) {
      if (!(var1 instanceof Elem)) {
         return false;
      } else {
         try {
            this.deletedparams.add(((Elem)var1).getEtc().get("dbparams"));
         } catch (Exception var3) {
         }

         boolean var2 = super.remove(var1);
         return var2;
      }
   }

   public BookModel load(String var1, String var2, String var3, String var4, BookModel var5) {
      return this.bm;
   }

   public BookModel load(String var1, String var2, String var3, String var4) {
      return this.bm;
   }

   public void load(File var1) {
      this.onlyhead = false;
      this._load(var1);

      try {
         this.l_megjegyzes = (String)((Elem)this.get(0)).getEtc().get("orignote");
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
      }

      if (this.addmode) {
         GuiUtil.setGlassLabel((String)null);
      } else {
         if (this.bm.get_main_index() == -1) {
            this.hasError = true;
            this.errormsg = "A nyomtatvány nem tartalmaz fődokumentumot ezért nem használható önállóan. \nCsak hozzáadással emelhető be.";
         }

      }
   }

   public void addFile(File var1) {
      this.onlyhead = false;
      File var2 = this.getLoadedfile();
      String var3 = this.l_megjegyzes;
      this._load(var1);
      this.setLoadedfile(var2);
      this.l_megjegyzes = var3;
   }

   private void _load(File var1) {
      this.hasError = false;
      this.curindex = 0;
      this.setLoadedfile(var1);
      this.in_type = false;
      this.in_saved = false;
      this.in_hibakszama = false;
      this.in_hash = false;
      this.in_muvelet = false;
      this.in_programverzio = false;
      this.in_adozo = false;
      this.in_mezo = false;
      this.in_reszlet = false;
      this.in_nyomtatvanyazonosito = this.in_nyomtatvanyverzio = this.in_tol = this.in_ig = this.in_megjegyzes = false;
      this.in_a_adoszam = this.in_a_adoazonosito = this.in_a_nev = this.in_mv_adoszam = this.in_mv_adoazonosito = this.in_mv_nev = false;
      FileInputStream var2 = null;

      try {
         var2 = new FileInputStream(var1);
         Object var10;
         if (this.onlyhead) {
            var10 = new CachedCollection.headhandler();
         } else {
            var10 = new CachedCollection.bodyhandler();
         }

         SAXParserFactory var4 = SAXParserFactory.newInstance();
         SAXParser var5 = var4.newSAXParser();
         InputSource var6 = new InputSource(var2);
         var6.setEncoding("utf-8");
         var5.parse(var6, (DefaultHandler)var10);
         var2.close();
      } catch (Exception var9) {
         Exception var3 = var9;

         try {
            if (!var3.getMessage().equals("OUT")) {
               this.hasError = true;
               if (this.errormsg == null) {
                  this.errormsg = var3.getMessage();
               }

               ErrorList.getInstance().writeError(new Integer(23112), "[ " + (new SimpleDateFormat()).format(new Date()) + " ] " + var1.getAbsolutePath(), 0, var3, (Object)null);
            }
         } catch (Exception var8) {
            this.hasError = true;
            if (this.errormsg == null) {
               this.errormsg = var9.toString();
            }
         }

         try {
            var2.close();
         } catch (IOException var7) {
         }
      }

   }

   private void attributes_done(Attributes var1, Hashtable var2) {
      for(int var3 = 0; var3 < var1.getLength(); ++var3) {
         var2.put(var1.getQName(var3), var1.getValue(var3));
      }

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
         this._load(var1);

         try {
            if (this.mv_nev != null && this.l_nyomtatvanyazonosito.endsWith("M")) {
               ((Hashtable)this.head.get("docinfo")).put("info", this.mv_nev);
            }
         } catch (Exception var3) {
         }

         if (this.head == null) {
            this.head = new Hashtable();
            this.head.put("type", "error");
            this.head.put("saved", "");
            this.head.put("docinfo", new Hashtable());
         }

         return this.head;
      }
   }

   public String getFileNameSuffix() {
      return this.suffix;
   }

   public String createFileName(String var1) {
      return var1.endsWith(this.suffix) ? var1 : var1 + this.suffix;
   }

   public int getActiveObjectindex() {
      for(int var1 = 0; var1 < this.size(); ++var1) {
         if (this.getquick(var1).equals(this.activeObject)) {
            return var1;
         }
      }

      return -1;
   }

   public void setSavepoints() {
      for(int var1 = 0; var1 < this.size(); ++var1) {
         GUI_Datastore var2 = (GUI_Datastore)((Elem)this.get(var1)).getRef();
         var2.setSavepoint(this.bm);
      }

   }

   public void setAll_kihatas_ht(Hashtable var1) {
      this.all_kihatas_ht = var1;
   }

   public synchronized Integer getSequence() {
      return this.sequence++;
   }

   public File getLoadedfile() {
      return this.loadedfile;
   }

   public void setLoadedfile(File var1) {
      this.loadedfile = var1;
      if (!this.nosettitle) {
         try {
            if (var1 == null) {
               GuiUtil.setTitle((String)null);
            } else if (MainFrame.thisinstance.mp.getDMFV().bm.cc.equals(this)) {
               GuiUtil.setTitle(var1.getCanonicalFile().getName());
            }
         } catch (Exception var3) {
         }

      }
   }

   public boolean isNosettitle() {
      return this.nosettitle;
   }

   public void setNosettitle(boolean var1) {
      this.nosettitle = var1;
   }

   class bodyhandler extends DefaultHandler {
      private boolean errorflag = false;
      private boolean roleerrorflag = false;
      private String vidcode;

      public void characters(char[] var1, int var2, int var3) throws SAXException {
         if (CachedCollection.this.in_mezo) {
            CachedCollection.this.mezovalue = CachedCollection.this.mezovalue + DatastoreKeyToXml.plainConvert(new String(var1, var2, var3));
         } else if (CachedCollection.this.in_reszlet) {
            CachedCollection.this.reszletnote = CachedCollection.this.reszletnote + new String(var1, var2, var3);
         } else if (CachedCollection.this.in_type) {
            String var4 = new String(var1, var2, var3);
            CachedCollection.this.head.put("type", var4);
            if (var4.equals("multi")) {
               CachedCollection.this.single = false;
            }
         } else if (CachedCollection.this.in_saved) {
            CachedCollection.this.head.put("saved", new String(var1, var2, var3));
         } else if (CachedCollection.this.in_hibakszama) {
            CachedCollection.this.abev_hibakszama = new String(var1, var2, var3);
         } else if (CachedCollection.this.in_hash) {
            CachedCollection.this.abev_hash = new String(var1, var2, var3);
         } else if (CachedCollection.this.in_muvelet) {
            CachedCollection.this.muvelet = new String(var1, var2, var3);
         } else if (CachedCollection.this.in_programverzio) {
            CachedCollection.this.abev_programverzio = new String(var1, var2, var3);
         } else if (CachedCollection.this.in_nyomtatvanyazonosito) {
            CachedCollection.this.l_nyomtatvanyazonosito = CachedCollection.this.l_nyomtatvanyazonosito + new String(var1, var2, var3);
         } else if (CachedCollection.this.in_nyomtatvanyverzio) {
            CachedCollection.this.l_nyomtatvanyverzio = CachedCollection.this.l_nyomtatvanyverzio + new String(var1, var2, var3);
         } else if (CachedCollection.this.in_tol) {
            CachedCollection.this.l_tol = CachedCollection.this.l_tol + new String(var1, var2, var3);
         } else if (CachedCollection.this.in_ig) {
            CachedCollection.this.l_ig = CachedCollection.this.l_ig + new String(var1, var2, var3);
         } else if (CachedCollection.this.in_megjegyzes) {
            CachedCollection.this.l_megjegyzes = CachedCollection.this.l_megjegyzes + new String(var1, var2, var3);
         } else if (CachedCollection.this.in_a_adoszam) {
            CachedCollection.this.a_adoszam = CachedCollection.this.a_adoszam + new String(var1, var2, var3);
         } else if (CachedCollection.this.in_a_adoazonosito) {
            CachedCollection.this.a_adoazonosito = CachedCollection.this.a_adoazonosito + new String(var1, var2, var3);
         } else if (CachedCollection.this.in_a_nev) {
            CachedCollection.this.a_nev = CachedCollection.this.a_nev + new String(var1, var2, var3);
         } else if (CachedCollection.this.in_mv_adoszam) {
            CachedCollection.this.mv_adoszam = CachedCollection.this.mv_adoszam + new String(var1, var2, var3);
         } else if (CachedCollection.this.in_mv_adoazonosito) {
            CachedCollection.this.mv_adoazonosito = CachedCollection.this.mv_adoazonosito + new String(var1, var2, var3);
         } else if (CachedCollection.this.in_mv_nev) {
            CachedCollection.this.mv_nev = CachedCollection.this.mv_nev + new String(var1, var2, var3);
         }

      }

      public void endDocument() throws SAXException {
         super.endDocument();
      }

      public void endElement(String var1, String var2, String var3) throws SAXException {
         String var13;
         if (var3.equals("mezo")) {
            try {
               if (this.roleerrorflag) {
                  this.roleerrorflag = false;
                  CachedCollection.this.in_mezo = false;
                  return;
               }

               if (this.errorflag) {
                  var13 = "A sablon nem tartalmazza az adatállományban található mezőkódot (vid) : " + this.vidcode + "    Értéke : " + CachedCollection.this.mezovalue;
                  CachedCollection.this.bm.warninglist.add(new TextWithIcon(var13, 4));
                  this.errorflag = false;
                  CachedCollection.this.in_mezo = false;
                  return;
               }

               boolean var4 = false;

               try {
                  String var5 = CachedCollection.this.mezokey.substring(CachedCollection.this.mezokey.indexOf("_") + 1);
                  var4 = ((DataFieldModel)CachedCollection.this.bm.get(CachedCollection.this.lc_fm.id).fids.get(var5)).features.get("datatype").equals("check");
               } catch (Exception var11) {
                  CachedCollection.this.in_mezo = false;
                  return;
               }

               if (var4) {
                  if (CachedCollection.this.mezovalue.equals("X")) {
                     CachedCollection.this.mezovalue = "true";
                  } else {
                     System.out.println("Súlyos hiba!!!!!!!! (logikainál nem X) " + CachedCollection.this.mezovalue);
                     CachedCollection.this.mezovalue = "false";
                  }
               }

               String var6;
               int var15;
               try {
                  var6 = CachedCollection.this.mezokey.substring(CachedCollection.this.mezokey.indexOf("_") + 1);
                  var15 = ((PageModel)CachedCollection.this.lc_fm.fids_page.get(var6)).maxpage;
               } catch (Exception var10) {
                  var15 = 1;
               }

               var6 = CachedCollection.this.mezokey.substring(0, CachedCollection.this.mezokey.indexOf("_"));

               int var7;
               try {
                  var7 = Integer.parseInt(var6);
               } catch (NumberFormatException var9) {
                  var7 = 0;
               }

               if (var15 <= var7) {
                  System.out.println("NagyonSúlyos index hiba!!!!!!!! " + CachedCollection.this.mezokey);
               } else {
                  CachedCollection.this.newdatastore.set(CachedCollection.this.mezokey, CachedCollection.this.mezovalue);
               }
            } catch (Exception var12) {
               var12.printStackTrace();
            }

            CachedCollection.this.in_mezo = false;
         } else if (var3.equals("reszlet")) {
            CachedCollection.this.in_reszlet = false;
            Vector var14 = new Vector();
            var14.add(CachedCollection.this.reszletnote);
            var14.add(CachedCollection.this.reszletertek);
            CachedCollection.this.reszletek.add(var14);
         } else if (var3.equals("reszletek")) {
            if (CachedCollection.this.newdatastore.get(CachedCollection.this.mezokey) == null) {
               CachedCollection.this.bm.warninglist.add(new TextWithIcon("A " + CachedCollection.this.mezokey + " kódú mező nem szerepel a sablonban, ezért az  adatállományban szereplő tételes adatrögzítést nem lehet betölteni.", 4));
            } else {
               var13 = CachedCollection.this.mezokey.substring(CachedCollection.this.mezokey.indexOf("_") + 1);
               DataFieldModel var16 = (DataFieldModel)CachedCollection.this.lc_fm.fids.get(var13);
               if (var16 == null) {
                  return;
               }

               if (DataChecker.getInstance().forintE(var16.features)) {
                  CachedCollection.this.newdatastore.setDetails(CachedCollection.this.mezokey, CachedCollection.this.reszletek, CachedCollection.this.reszleteksum);
               }
            }
         } else if (var3.equals("nyomtatvany")) {
            var13 = (String)CachedCollection.this.docinfo.get("calculated");
            boolean var17 = false;
            if ("false".equals(var13)) {
               var17 = true;
            }

            if (!var17) {
               CalculatorManager.getInstance().doBetoltErtekCalcs(true);
            }

            if (var13.equals("true")) {
               CalculatorManager.getInstance().do_dpage_count();
            }

            CalculatorManager.getInstance().multi_form_load();
            if (CachedCollection.this.bm.warninglist == null) {
               return;
            }

            if (CachedCollection.this.bm.warninglist.size() != 0 && TextWithIcon.IMG_BLACKPOINT.equals(((TextWithIcon)CachedCollection.this.bm.warninglist.get(CachedCollection.this.bm.warninglist.size() - 1)).ii)) {
               CachedCollection.this.bm.warninglist.remove(CachedCollection.this.bm.warninglist.size() - 1);
            }
         } else if (var3.equals("head")) {
            CachedCollection.this.head.put("docinfo", CachedCollection.this.docinfo);
         } else if (var3.equals("type")) {
            CachedCollection.this.in_type = false;
         } else if (var3.equals("saved")) {
            CachedCollection.this.in_saved = false;
         } else if (var3.equals("hibakszama")) {
            CachedCollection.this.in_hibakszama = false;
         } else if (var3.equals("hash")) {
            CachedCollection.this.in_hash = false;
         } else if (var3.equals("muvelet")) {
            CachedCollection.this.in_muvelet = false;
         } else if (var3.equals("programverzio")) {
            CachedCollection.this.in_programverzio = false;
         } else if (var3.equals("nyomtatvanyazonosito")) {
            CachedCollection.this.in_nyomtatvanyazonosito = false;
         } else if (var3.equals("nyomtatvanyverzio")) {
            CachedCollection.this.in_nyomtatvanyverzio = false;
         } else if (var3.equals("tol")) {
            CachedCollection.this.in_tol = false;
         } else if (var3.equals("ig")) {
            CachedCollection.this.in_ig = false;
         } else if (var3.equals("megjegyzes")) {
            CachedCollection.this.in_megjegyzes = false;
         } else if (var3.equals("adoszam")) {
            if (CachedCollection.this.in_adozo) {
               CachedCollection.this.in_a_adoszam = false;
            } else {
               CachedCollection.this.in_mv_adoszam = false;
            }
         } else if (var3.equals("adoazonosito")) {
            if (CachedCollection.this.in_adozo) {
               CachedCollection.this.in_a_adoazonosito = false;
            } else {
               CachedCollection.this.in_mv_adoazonosito = false;
            }
         } else if (var3.equals("nev")) {
            if (CachedCollection.this.in_adozo) {
               CachedCollection.this.in_a_nev = false;
            } else {
               CachedCollection.this.in_mv_nev = false;
            }
         } else if (var3.equals("adozo")) {
            CachedCollection.this.in_adozo = false;
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
         Object[] var7;
         int var23;
         String var25;
         if (var3.equals("mezo")) {
            this.roleerrorflag = false;
            CachedCollection.this.mezovalue = "";
            CachedCollection.this.in_mezo = true;
            CachedCollection.this.mezokey = var4.getValue("vid");
            String[] var5;
            DataFieldModel var9;
            if (CachedCollection.this.mezokey != null) {
               try {
                  var5 = CachedCollection.this.mezokey.split("_", 2);
                  String var19 = var5[1];
                  var7 = new Object[]{null, var19, null};
                  Object[] var8 = (Object[])((Object[])MetaInfo.getInstance().getIds(var7, CachedCollection.this.lc_fm.id));
                  CachedCollection.this.mezokey = var5[0] + "_" + var8[0];
                  var9 = ((PageModel)CachedCollection.this.lc_fm.fids_page.get(var8[0])).getByCode(var8[0].toString());
                  if (var9 != null && (var9.role & VisualFieldModel.getmask()) == 0) {
                     throw new Exception();
                  }
               } catch (Exception var16) {
                  if (CachedCollection.this.bm.warninglist == null) {
                     CachedCollection.this.bm.warninglist = new Vector();
                  }

                  String[] var6 = CachedCollection.this.mezokey.split("_", 2);
                  this.vidcode = var6[1];
                  this.errorflag = true;
                  return;
               }
            } else {
               CachedCollection.this.mezokey = var4.getValue("eazon");
            }

            if (CachedCollection.this.bm.no_tech_fields && CachedCollection.this.lc_fm.get_short_inv_fields_ht().containsKey(CachedCollection.this.mezokey)) {
               return;
            }

            var5 = CachedCollection.this.mezokey.split("_", 2);
            if (var5.length < 2) {
               throw new SAXException("Érvénytelen mezőazonosító! Mezőkód: " + CachedCollection.this.mezokey);
            }

            int var20 = 0;

            try {
               var20 = Integer.parseInt(var5[0]);
            } catch (NumberFormatException var15) {
            }

            int var21;
            try {
               var21 = ((PageModel)CachedCollection.this.lc_fm.fids_page.get(var5[1])).maxpage;
            } catch (Exception var14) {
               var21 = 1;
            }

            if (var21 <= var20) {
               if (CachedCollection.this.bm.warninglist == null) {
                  CachedCollection.this.bm.warninglist = new Vector();
               }

               var25 = "Dinamikus index hiba! (Nagyobb a megengedettnél) Mezőkód: " + CachedCollection.this.mezokey;
               CachedCollection.this.bm.warninglist.add(new TextWithIcon(var25, 4));
               return;
            }

            ++var20;
            var23 = CachedCollection.this.lc_fm.get((PageModel)CachedCollection.this.lc_fm.fids_page.get(var5[1]));
            if (var23 != -1) {
               try {
                  var9 = ((PageModel)CachedCollection.this.lc_fm.fids_page.get(var5[1])).getByCode(var5[1]);
                  if (var9 != null && (var9.role & VisualFieldModel.getmask()) == 0) {
                     var23 = -1;
                  }
               } catch (Exception var13) {
                  var23 = -1;
               }
            }

            if (var23 == -1) {
               this.roleerrorflag = true;
               String var26 = "A sablon nem tartalmazza az adatállományban található (" + var5[1] + " mezőkódú) mezőt.\nEz az adat nem kerül betöltésre. Ellenőrizze a nyomtatványt! \n" + CachedCollection.this.l_nyomtatvanyazonosito;
               if (CachedCollection.this.bm.silent) {
                  if (CachedCollection.this.bm.warninglist == null) {
                     CachedCollection.this.bm.warninglist = new Vector();
                  }

                  CachedCollection.this.bm.warninglist.add(new TextWithIcon(var26, 4));
               } else {
                  if (CachedCollection.this.bm.warninglist == null) {
                     CachedCollection.this.bm.warninglist = new Vector();
                  }

                  CachedCollection.this.bm.warninglist.add(new TextWithIcon(var26, 4));
               }
            } else if (CachedCollection.this.pc[var23] < var20) {
               CachedCollection.this.pc[var23] = var20;
            }
         } else if (var3.equals("reszlet")) {
            CachedCollection.this.in_reszlet = true;
            CachedCollection.this.reszletnote = "";
            CachedCollection.this.reszletertek = var4.getValue("ertek");
         } else if (var3.equals("reszletek")) {
            CachedCollection.this.reszletek = new Vector();
            CachedCollection.this.reszleteksum = var4.getValue("osszeg");
         } else if (var3.equals("mezok")) {
            CachedCollection.this.lc_fm = CachedCollection.this.bm.get(CachedCollection.this.l_nyomtatvanyazonosito);
            if (CachedCollection.this.lc_fm == null) {
               CachedCollection.this.errormsg = "Hibás típusú adatfile!";
               throw new SAXException(CachedCollection.this.errormsg);
            }

            int var17 = CachedCollection.this.bm.getIndex(CachedCollection.this.bm.get(CachedCollection.this.l_nyomtatvanyazonosito));
            if (CachedCollection.this.bm.maxcreation[var17] < CachedCollection.this.bm.created[var17] + 1) {
               CachedCollection.this.errormsg = "Súlyos hiba! Az első kivételével csak alnyomtatványt tartalmazhat az '.frm.enyk' file!";
               if (!CachedCollection.this.addmode) {
                  throw new SAXException(CachedCollection.this.errormsg);
               }
            }

            CachedCollection.this.newdatastore = new GUI_Datastore();
            Boolean var22 = new Boolean(CachedCollection.this.l_nyomtatvanyazonosito.equals(CachedCollection.this.bm.main_document_id));
            CachedCollection.this.lc_elem = new Elem(CachedCollection.this.newdatastore, CachedCollection.this.l_nyomtatvanyazonosito, CachedCollection.this.lc_fm.name, var22);
            var7 = null;

            Integer var24;
            try {
               var24 = Integer.parseInt(CachedCollection.this.lc_snstr);
            } catch (NumberFormatException var12) {
               var24 = null;
            }

            if (var24 == null) {
               var24 = CachedCollection.this.getSequence();
            }

            CachedCollection.this.lc_elem.getEtc().put("sn", var24);

            try {
               if (CachedCollection.this.lc_elem.getFejlec() == null) {
                  CachedCollection.this.lc_elem.setFejlec(new Hashtable());
               }

               CachedCollection.this.lc_elem.getFejlec().put("mvtaxid", CachedCollection.this.mv_adoazonosito);
               CachedCollection.this.lc_elem.getFejlec().put("mvname", CachedCollection.this.mv_nev);
            } catch (Exception var11) {
            }

            CachedCollection.this.pc = new int[CachedCollection.this.lc_fm.size()];

            for(var23 = 0; var23 < CachedCollection.this.pc.length; ++var23) {
               CachedCollection.this.pc[var23] = 1;
            }

            CachedCollection.this.lc_elem.getEtc().put("pagecounts", CachedCollection.this.pc);
            CachedCollection.this.add(CachedCollection.this.lc_elem);
            int var10002 = CachedCollection.this.bm.created[var17]++;
            CachedCollection.this.bm.setCalcelemindex(CachedCollection.this.curindex - 1);
            CachedCollection.this.lc_elem.getEtc().put("orignote", CachedCollection.this.l_megjegyzes);
            if (CachedCollection.this.bm.warninglist == null) {
               CachedCollection.this.bm.warninglist = new Vector();
            }

            if (CachedCollection.this.bm.warninglist.size() != 0 && TextWithIcon.IMG_BLACKPOINT.equals(((TextWithIcon)CachedCollection.this.bm.warninglist.get(CachedCollection.this.bm.warninglist.size() - 1)).ii)) {
               CachedCollection.this.bm.warninglist.remove(CachedCollection.this.bm.warninglist.size() - 1);
            }

            var25 = CachedCollection.this.lc_elem.getType() + " " + (CachedCollection.this.mv_nev != null ? CachedCollection.this.mv_nev : "") + " " + (CachedCollection.this.mv_adoazonosito != null ? CachedCollection.this.mv_adoazonosito : "");
            CachedCollection.this.bm.warninglist.add(new TextWithIcon(var25, 2));
         } else if (var3.equals("head")) {
            CachedCollection.this.head = new Hashtable();
            CachedCollection.this.attributes_done(var4, CachedCollection.this.head);
         } else if (var3.equals("docinfo")) {
            CachedCollection.this.docinfo = new Hashtable();
            CachedCollection.this.attributes_done(var4, CachedCollection.this.docinfo);
            String var18 = (String)CachedCollection.this.docinfo.get("seq");
            if (var18 != null) {
               try {
                  CachedCollection.this.sequence = Integer.parseInt(var18);
               } catch (NumberFormatException var10) {
                  CachedCollection.this.sequence = 0;
               }
            }
         } else if (var3.equals("type")) {
            CachedCollection.this.in_type = true;
         } else if (var3.equals("saved")) {
            CachedCollection.this.in_saved = true;
         } else if (var3.equals("nyomtatvanyok")) {
            CachedCollection.this.nyomtatvanyok = new Hashtable();
            CachedCollection.this.attributes_done(var4, CachedCollection.this.nyomtatvanyok);
         } else if (var3.equals("hibakszama")) {
            CachedCollection.this.in_hibakszama = true;
         } else if (var3.equals("hash")) {
            CachedCollection.this.in_hash = true;
         } else if (var3.equals("muvelet")) {
            CachedCollection.this.in_muvelet = true;
         } else if (var3.equals("programverzio")) {
            CachedCollection.this.in_programverzio = true;
         } else if (var3.equals("nyomtatvany")) {
            CachedCollection.this.lc_elem = null;
            CachedCollection.this.l_megjegyzes = "";
            ++CachedCollection.this.curindex;
            CachedCollection.this.lc_snstr = var4.getValue("sn");
            GuiUtil.setGlassLabel("Betöltve:" + CachedCollection.this.curindex);
         } else if (var3.equals("nyomtatvanyazonosito")) {
            CachedCollection.this.in_nyomtatvanyazonosito = true;
            CachedCollection.this.l_nyomtatvanyazonosito = "";
         } else if (var3.equals("nyomtatvanyverzio")) {
            CachedCollection.this.in_nyomtatvanyverzio = true;
            CachedCollection.this.l_nyomtatvanyverzio = "";
         } else if (var3.equals("tol")) {
            CachedCollection.this.in_tol = true;
            CachedCollection.this.l_tol = "";
         } else if (var3.equals("ig")) {
            CachedCollection.this.in_ig = true;
            CachedCollection.this.l_ig = "";
         } else if (var3.equals("megjegyzes")) {
            CachedCollection.this.in_megjegyzes = true;
            CachedCollection.this.l_megjegyzes = "";
         } else if (var3.equals("adoszam")) {
            if (CachedCollection.this.in_adozo) {
               CachedCollection.this.in_a_adoszam = true;
               CachedCollection.this.a_adoszam = "";
            } else {
               CachedCollection.this.in_mv_adoszam = true;
               CachedCollection.this.mv_adoszam = "";
            }
         } else if (var3.equals("adoazonosito")) {
            if (CachedCollection.this.in_adozo) {
               CachedCollection.this.in_a_adoazonosito = true;
               CachedCollection.this.a_adoazonosito = "";
            } else {
               CachedCollection.this.in_mv_adoazonosito = true;
               CachedCollection.this.mv_adoazonosito = "";
            }
         } else if (var3.equals("nev")) {
            if (CachedCollection.this.in_adozo) {
               CachedCollection.this.in_a_nev = true;
               CachedCollection.this.a_nev = "";
            } else {
               CachedCollection.this.in_mv_nev = true;
               CachedCollection.this.mv_nev = "";
            }
         } else if (var3.equals("adozo")) {
            CachedCollection.this.in_adozo = true;
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
         if (CachedCollection.this.in_type) {
            String var4 = new String(var1, var2, var3);
            CachedCollection.this.head.put("type", var4);
            if (var4.equals("multi")) {
               CachedCollection.this.single = false;
            }
         } else if (CachedCollection.this.in_muvelet) {
            CachedCollection.this.head.put("muvelet", new String(var1, var2, var3));
         } else if (CachedCollection.this.in_saved) {
            CachedCollection.this.head.put("saved", new String(var1, var2, var3));
         } else if (CachedCollection.this.in_mv_nev) {
            CachedCollection.this.mv_nev = CachedCollection.this.mv_nev + new String(var1, var2, var3);
         } else if (CachedCollection.this.in_nyomtatvanyazonosito) {
            CachedCollection.this.l_nyomtatvanyazonosito = CachedCollection.this.l_nyomtatvanyazonosito + new String(var1, var2, var3);
         }

      }

      public void endDocument() throws SAXException {
         super.endDocument();
      }

      public void endElement(String var1, String var2, String var3) throws SAXException {
         if (var3.equals("head")) {
            CachedCollection.this.head.put("docinfo", CachedCollection.this.docinfo);
            if (CachedCollection.this.single) {
               throw new SAXException("OUT");
            }
         } else if (var3.equals("type")) {
            CachedCollection.this.in_type = false;
         } else if (var3.equals("muvelet")) {
            CachedCollection.this.in_muvelet = false;
         } else if (var3.equals("saved")) {
            CachedCollection.this.in_saved = false;
         } else {
            if (var3.equals("nyomtatvanyinformacio")) {
               throw new SAXException("OUT");
            }

            if (var3.equals("nyomtatvanyazonosito")) {
               CachedCollection.this.in_nyomtatvanyazonosito = false;
            } else if (var3.equals("adozo")) {
               CachedCollection.this.in_adozo = false;
            } else if (var3.equals("nev")) {
               if (CachedCollection.this.in_adozo) {
                  CachedCollection.this.in_a_nev = false;
               } else {
                  CachedCollection.this.in_mv_nev = false;
               }
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
         if (var3.equals("head")) {
            CachedCollection.this.head = new Hashtable();
            CachedCollection.this.attributes_done(var4, CachedCollection.this.head);
         } else if (var3.equals("docinfo")) {
            CachedCollection.this.docinfo = new Hashtable();
            CachedCollection.this.attributes_done(var4, CachedCollection.this.docinfo);
         } else if (var3.equals("type")) {
            CachedCollection.this.in_type = true;
         } else if (var3.equals("muvelet")) {
            CachedCollection.this.in_muvelet = true;
         } else if (var3.equals("saved")) {
            CachedCollection.this.in_saved = true;
         } else if (var3.equals("nev")) {
            if (CachedCollection.this.in_adozo) {
               CachedCollection.this.in_a_nev = true;
               CachedCollection.this.a_nev = "";
            } else {
               CachedCollection.this.in_mv_nev = true;
               CachedCollection.this.mv_nev = "";
            }
         } else if (var3.equals("adozo")) {
            CachedCollection.this.in_adozo = true;
         } else if (var3.equals("nyomtatvanyazonosito")) {
            CachedCollection.this.in_nyomtatvanyazonosito = true;
            CachedCollection.this.l_nyomtatvanyazonosito = "";
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
}
