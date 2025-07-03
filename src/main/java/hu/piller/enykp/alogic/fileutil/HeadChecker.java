package hu.piller.enykp.alogic.fileutil;

import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.metainfo.MetaStore;
import hu.piller.enykp.alogic.orghandler.OrgHandler;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class HeadChecker {
   public static final boolean debugOn = true;
   public static final String RESOURCE_NAME = "HeadChecker";
   public static final Long RESOURCE_ERROR_ID = new Long(896L);
   public static final Long ID_ERR_DIFF = new Long(2010L);
   public static final String MSG_ERR_FATAL = "Hiba a nyomtaványinformációs rész ellenőrzésekor";
   public static final String ID_ALBIZONYLAT_INFO_NONE = "0";
   public static final String ID_ALBIZONYLAT_INFO_MUNKAVALLALO = "1";
   public static final String ID_ALBIZONYLAT_INFO_ALBIZONYLAT = "2";
   public static final String CMD_HEAD_CHECK = "head_check";
   public static final String CMD_RELEASE = "release";
   public static final String xmlMetaSpecName = "nev";
   public static final String xmlMetaSpecEmpName = "munkavallalo/nev";
   public static final String xmlMetaSpecEuTax = "adoszam";
   private static boolean specNameStored;
   private static boolean specEmpNameStored;
   private static boolean specEuTaxStored;
   private static String[] xmlMeta = new String[]{"adoszam", "adoazonosito", "nev", "tol", "ig", "munkavallalo/nev", "munkavallalo/adoazonosito", "albizonylatazonositas/megnevezes", "albizonylatazonositas/azonosito"};
   private static String[] xmlAlbizMeta = new String[]{"munkavallalo/nev", "munkavallalo/adoazonosito", "albizonylatazonositas/megnevezes", "albizonylatazonositas/azonosito"};
   private static final String[] abev_panids = new String[]{"Adózó adószáma", "Adózó adóazonosító jele", "Adózó neve", "Bevallási időszak kezdete", "Bevallási időszak vége", "Munkavállaló neve", "Munkavállaló adóazonosító jele", "Bizonylat tulajdonos név", "Bizonylat tulajdonos azonosító"};
   private static final Boolean[] abev_panids_req;
   private static final String[] abev_panids_spec_name;
   private static final String[] abev_panids_spec_name_albiz;
   private static final String[] abev_panids_spec_emp_name;
   private static final String[] abev_panids_spec_eu_tax;
   private static String[] meta_full_emp_panids;
   private static String kiv1;
   private static String kiv2;
   public static final String EXT_INFO_NAME = "Név";
   public static final String EXT_INFO_MUNK_NEV = "munkavallalo/nev";
   public static final String EXT_INFO_ALBIZNEV = "albizonylatazonositas/megnevezes";
   public static final String EXT_INFO_FOBIZNEV = "nev";
   public static final String EXT_INFO_ID = "Azonosító";
   public static final String EXT_INFO_MUNK_ID = "munkavallalo/adoazonosito";
   public static final String EXT_INFO_ALBIZID = "albizonylatazonositas/azonosito";
   public static final String[] NON_FATAL_ERRORS_DNS;
   private static Hashtable ht_form_panids_fid;
   private static Hashtable ht_form_panids_spec_name_fid;
   private static Hashtable ht_form_panids_spec_emp_name_fid;
   private static Hashtable ht_form_panids_spec_eu_tax_fid;
   private static Hashtable ht_form_panids_full_emp_existing_panids;
   private static Hashtable ht_panids_meta;
   private static Hashtable ht_meta_panids;
   private static Hashtable ht_meta_req;
   private static Hashtable ht_albiz_meta_req;
   private static Hashtable ht_panids_spec_name;
   private static Hashtable ht_panids_spec_name_albiz;
   private static Hashtable ht_panids_spec_emp_name;
   private static Hashtable ht_panids_spec_eu_tax;
   private static Hashtable ht_full_emp_panids;
   private static Hashtable<String, String> ht_albiz_fid;
   public static final String NULL_VALUE = "HeadCheckerNullValue";
   private static HeadChecker instance;
   private Vector errorlist;
   private boolean isFatalError;

   public static HeadChecker getInstance() {
      if (instance == null) {
         instance = new HeadChecker();
      }

      return instance;
   }

   private HeadChecker() {
      this.createPanidsXmlMetaInd();
      this.createMetaReqInd();
      this.createAlBizMetaReqInd();
      this.createPanidsSpecName();
      this.createPanidsSpecNameAlbiz();
      this.createPanidsSpecEmpName();
      this.createPanidsSpecEuTax();
      this.createFullEmpPanids();
      this.release();
   }

   public void release() {
      ht_form_panids_fid = new Hashtable();
      ht_form_panids_spec_name_fid = new Hashtable();
      ht_form_panids_spec_emp_name_fid = new Hashtable();
      ht_form_panids_spec_eu_tax_fid = new Hashtable();
      ht_form_panids_full_emp_existing_panids = new Hashtable();
      specNameStored = false;
      specEmpNameStored = false;
      specEuTaxStored = false;
      ht_albiz_fid = new Hashtable();
   }

   private void createPanidsXmlMetaInd() {
      ht_panids_meta = new Hashtable(abev_panids.length);
      ht_meta_panids = new Hashtable(abev_panids.length);

      for(int var1 = 0; var1 < abev_panids.length; ++var1) {
         ht_panids_meta.put(abev_panids[var1], xmlMeta[var1]);
         ht_meta_panids.put(xmlMeta[var1], abev_panids[var1]);
      }

   }

   private void createMetaReqInd() {
      ht_meta_req = new Hashtable(abev_panids.length);

      for(int var1 = 0; var1 < abev_panids.length; ++var1) {
         ht_meta_req.put(xmlMeta[var1], abev_panids_req[var1]);
      }

   }

   private void createAlBizMetaReqInd() {
      ht_albiz_meta_req = new Hashtable(xmlAlbizMeta.length);
      String[] var1 = xmlAlbizMeta;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         ht_albiz_meta_req.put(var4, "");
      }

   }

   private void createPanidsSpecName() {
      ht_panids_spec_name = new Hashtable(abev_panids_spec_name.length);
      String[] var1 = abev_panids_spec_name;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         ht_panids_spec_name.put(var4, "");
      }

   }

   private void createPanidsSpecNameAlbiz() {
      ht_panids_spec_name_albiz = new Hashtable(abev_panids_spec_name_albiz.length);
      String[] var1 = abev_panids_spec_name_albiz;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         ht_panids_spec_name_albiz.put(var4, "");
      }

   }

   private void createPanidsSpecEmpName() {
      ht_panids_spec_emp_name = new Hashtable(abev_panids_spec_emp_name.length);

      for(int var1 = 0; var1 < abev_panids_spec_emp_name.length; ++var1) {
         ht_panids_spec_emp_name.put(abev_panids_spec_emp_name[var1], "");
      }

   }

   private void createPanidsSpecEuTax() {
      ht_panids_spec_eu_tax = new Hashtable(abev_panids_spec_eu_tax.length);
      String[] var1 = abev_panids_spec_eu_tax;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         ht_panids_spec_eu_tax.put(var4, "");
      }

   }

   private void createFullEmpPanids() {
      ht_full_emp_panids = new Hashtable(meta_full_emp_panids.length);
      String[] var1 = meta_full_emp_panids;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         ht_full_emp_panids.put(var4, "");
      }

   }

   public Vector headCheck(Object[] var1, BookModel var2) {
      this.isFatalError = false;
      this.errorlist = new Vector();
      Hashtable var4 = (Hashtable)var1[0];
      String var5 = (String)var1[1];
      String var3 = (String)var1[2];
      Integer var6 = (Integer)var1[3];
      IDataStore var7 = (IDataStore)var1[4];
      Hashtable var8 = this.getFastHeadData(var3, var7);
      this.compareDatas(var4, var8, var5, var6, var3, var2);
      return this.errorlist;
   }

   private void compareDatas(Hashtable var1, Hashtable var2, String var3, Integer var4, String var5, BookModel var6) {
      if (OrgHandler.getInstance().isNotGeneralOrg(var3)) {
         this.compareA(var1, var2, var4, var5, var6);
      } else {
         this.compareB(var1, var2, var4, var5);
      }

   }

   private boolean searchInArr(String var1, String[] var2) {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         String var4 = var2[var3];
         if (var4.equalsIgnoreCase(var1)) {
            return true;
         }
      }

      return false;
   }

   private void compareA(Hashtable var1, Hashtable var2, Integer var3, String var4, BookModel var5) {
      try {
         Enumeration var6 = var2.keys();

         while(true) {
            String var7;
            String var8;
            String var9;
            boolean var10;
            do {
               if (!var6.hasMoreElements()) {
                  var6 = var1.keys();

                  while(var6.hasMoreElements()) {
                     var7 = (String)var6.nextElement();
                     if (!var2.containsKey(var7)) {
                        this.writeError("nyomtatványinformációs részében a következő található, pedig nem szükséges: " + var7 + "=" + (var1.get(var7) == null ? "" : var1.get(var7)), var3, this.getExtInfo(var2, var4, var5), var4, true);
                     }
                  }

                  if ("0".equals(this.getHeaderTagType(var4, var5))) {
                     for(int var13 = 0; var13 < xmlAlbizMeta.length; ++var13) {
                        var8 = xmlAlbizMeta[var13];
                        if (var1.containsKey(var8)) {
                           this.writeError("nyomtatványinformációs részében a következő szerepel, pedig nem szükséges: " + var8 + "=" + (var1.get(var8) == null ? "" : var1.get(var8)), var3, this.getExtInfo(var2, var4, var5), var4, true);
                        }
                     }
                  }

                  return;
               }

               var7 = (String)var6.nextElement();
               var8 = this.getValue((String)var2.get(var7));
               var9 = (String)var1.get(var7);
               var10 = false;
            } while(var8.length() == 0 && var9 == null);

            String var11 = (String)((Hashtable)ht_form_panids_fid.get(var4)).get(var7);
            if (var7.equalsIgnoreCase("nev") && specNameStored) {
               var11 = "(" + ((Hashtable)ht_form_panids_spec_name_fid.get(var4)).get("Név titulus") + "/" + ((Hashtable)ht_form_panids_spec_name_fid.get(var4)).get("Vezetékneve") + "/" + ((Hashtable)ht_form_panids_spec_name_fid.get(var4)).get("Keresztneve") + ")";
            }

            if (var7.equalsIgnoreCase("munkavallalo/nev") && specEmpNameStored) {
               var11 = "(" + ((Hashtable)ht_form_panids_spec_emp_name_fid.get(var4)).get("Munkavállaló titulus") + "/" + ((Hashtable)ht_form_panids_spec_emp_name_fid.get(var4)).get("Munkavállaló vezetéknév") + "/" + ((Hashtable)ht_form_panids_spec_emp_name_fid.get(var4)).get("Munkavállaló keresztnév") + ")";
            }

            if (var7.equalsIgnoreCase("adoszam") && specEuTaxStored) {
               var11 = "(" + ((Hashtable)ht_form_panids_spec_eu_tax_fid.get(var4)).get("Közösségi adószám") + ")";
            }

            if ("0".equals(this.getHeaderTagType(var4, var5))) {
               if (var9 == null) {
                  if (!ht_albiz_meta_req.containsKey(var7)) {
                     this.writeError("a nyomtatványinformációs részből hiányzik a következő: " + var7 + " (" + var11 + "=" + var8 + ")", var3, this.getExtInfo(var2, var4, var5), var4, true);
                  }
               } else if (!var9.equals(var8)) {
                  if (!this.searchInArr(var7, NON_FATAL_ERRORS_DNS)) {
                     var10 = true;
                  }

                  this.writeError("a nyomtatványinformációs rész nincs összhangban az adatrésszel (" + var7 + "=" + (var9 == null ? "" : var9) + ", " + var11 + "=" + var8 + ")", var3, this.getExtInfo(var2, var4, var5), var4, var10);
               }
            } else if (var9 == null) {
               this.writeError("a nyomtatványinformációs részből hiányzik a következő: " + var7 + " (" + var11 + "=" + var8 + ")", var3, this.getExtInfo(var2, var4, var5), var4, true);
            } else if (!var9.equals(var8)) {
               if (!this.searchInArr(var7, NON_FATAL_ERRORS_DNS)) {
                  var10 = true;
               }

               this.writeError("a nyomtatványinformációs rész nincs összhangban az adatrésszel (" + var7 + "=" + (var9 == null ? "" : var9) + ", " + var11 + "=" + var8 + ")", var3, this.getExtInfo(var2, var4, var5), var4, var10);
            }
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      }
   }

   private void compareB(Hashtable var1, Hashtable var2, Integer var3, String var4) {
      Enumeration var5 = var2.keys();

      String var6;
      String var7;
      while(var5.hasMoreElements()) {
         var6 = (String)var5.nextElement();
         var7 = this.getValue((String)var2.get(var6));
         String var8 = (String)var1.get(var6);
         if (var8 != null && !var8.equals(var7)) {
            String var9 = (String)((Hashtable)ht_form_panids_fid.get(var4)).get(var6);
            this.writeError("a nyomtatványinformációs rész nincs összhangban az adatrésszel (" + var6 + "=" + var8 + ", " + var9 + "=" + var7 + ")", var3, (String)null, (String)null, false);
         }
      }

      var5 = var1.keys();

      while(true) {
         while(true) {
            do {
               if (!var5.hasMoreElements()) {
                  return;
               }

               var6 = (String)var5.nextElement();
            } while(var2.containsKey(var6));

            if (!var6.equals(kiv1) && !var6.equals(kiv2)) {
               if ((var7 = var1.get(var6).toString()).length() != 0) {
                  this.writeError("a nyomtatványinformációs rész adatot tartalmaz pedig üresnek kellene lennie (" + var6 + "=" + var7 + ")", var3, (String)null, (String)null, false);
               }
            } else {
               this.writeError("nyomtatványinformációs részében a következő található, pedig nem szükséges: " + var6 + "=" + var1.get(var6), var3, (String)null, (String)null, false);
            }
         }
      }
   }

   private String getValue(String var1) {
      return var1.equals("HeadCheckerNullValue") ? "" : var1;
   }

   private void writeError(String var1, Integer var2, String var3, String var4, boolean var5) {
      if (MainFrame.role.equals("0")) {
         TextWithIcon var6;
         if (this.errorlist.size() == 0 && var3 != null) {
            var6 = new TextWithIcon(" >  " + var4, -1);
            this.errorlist.add(var6);
            var6 = new TextWithIcon(" >  " + var3, -1);
            this.errorlist.add(var6);
         }

         int var7 = var5 ? 1 : 0;
         var6 = new TextWithIcon("[" + ID_ERR_DIFF + "] " + " Az " + var2 + ". nyomtatványon " + var1, var7);
         this.errorlist.add(var6);
         this.setFatalError(var5);
      }

   }

   private Hashtable createStructures(String var1) {
      if (ht_form_panids_fid.containsKey(var1)) {
         return (Hashtable)ht_form_panids_fid.get(var1);
      } else {
         MetaStore var2 = this.getMetaStore(var1);
         Vector var4 = var2.getFilteredFieldMetas_And(new Vector(Arrays.asList("panids")));
         String var5 = "0";
         Hashtable var6 = new Hashtable();
         Hashtable var7 = new Hashtable();
         Hashtable var8 = new Hashtable();
         Hashtable var9 = new Hashtable();
         Vector var10 = new Vector(var4.size());
         int var11 = 0;

         for(int var12 = var4.size(); var11 < var12; ++var11) {
            Hashtable var3 = (Hashtable)var4.get(var11);
            var10.clear();
            var10.addAll(Arrays.asList(this.getString(var3.get("panids")).split(",")));
            int var13 = 0;

            for(int var14 = var10.size(); var13 < var14; ++var13) {
               String var15 = var10.get(var13).toString().trim();
               String var16 = (String)ht_panids_meta.get(var15);
               if (var16 != null) {
                  var6.put(var16, var3.get("fid"));
               }

               if (ht_panids_spec_name.containsKey(var15)) {
                  var7.put(var15, var3.get("fid"));
               }

               if (ht_panids_spec_emp_name.containsKey(var15)) {
                  var8.put(var15, var3.get("fid"));
               }

               if (ht_panids_spec_eu_tax.containsKey(var15)) {
                  var9.put(var15, var3.get("fid"));
               }

               if (ht_panids_spec_name_albiz.containsKey(var15)) {
                  ht_panids_spec_name_albiz.put(var15, var3.get("fid"));
               }

               if (ht_full_emp_panids.containsKey(var15)) {
                  var5 = "1";
               }

               if (ht_panids_spec_name_albiz.containsKey(var15)) {
                  var5 = "2";
               }
            }
         }

         ht_form_panids_fid.put(var1, var6);
         ht_form_panids_spec_name_fid.put(var1, var7);
         ht_form_panids_spec_emp_name_fid.put(var1, var8);
         ht_form_panids_spec_eu_tax_fid.put(var1, var9);
         ht_form_panids_full_emp_existing_panids.put(var1, var5);
         return var6;
      }
   }

   private Hashtable getFastHeadData(String var1, IDataStore var2) {
      String var3 = "0";
      Hashtable var4 = new Hashtable();
      Hashtable var5 = this.createStructures(var1);
      if (var5 == null) {
         return null;
      } else {
         Enumeration var6 = var5.keys();

         while(var6.hasMoreElements()) {
            String var7 = (String)var6.nextElement();
            String var8 = (String)var5.get(var7);
            String var9 = this.getString(var2.get(var3 + "_" + var8));
            var4.put(var7, var9 == null ? NULL_VALUE : var9);
         }

         this.analyzeSpecName(var1, var2, var4);
         this.analyzeSpecTaxNumber(var1, var2, var4);
         return var4;
      }
   }

   private void analyzeSpecName(String var1, IDataStore var2, Hashtable var3) {
      specNameStored = false;
      specEmpNameStored = false;
      Object var4 = var3.get("nev");
      String var5 = var4 == null ? "" : (String)var4;
      String var6 = this.getSpecNameData(var1, var2);
      if (var5 != null && var6 != null && var5.length() == 0) {
         var3.put("nev", var6);
         specNameStored = true;
      }

      Object var7 = var3.get("munkavallalo/nev");
      String var8 = var7 == null ? "" : (String)var7;
      String var9 = this.getSpecEmpNameData(var1, var2);
      if (var8 != null && var9 != null && var8.length() == 0) {
         var3.put("munkavallalo/nev", var9);
         specEmpNameStored = true;
      }

   }

   private void analyzeSpecTaxNumber(String var1, IDataStore var2, Hashtable var3) {
      specEuTaxStored = false;
      Object var4 = var3.get("adoszam");
      String var5 = var4 == null ? "" : (String)var4;
      if (var5.length() == 0) {
         Hashtable var6 = this.getSpecEuTaxData(var1, var2);
         String var7 = this.getString(var6.get("Közösségi adószám"));
         if (var7.length() > 0) {
            var3.put("adoszam", var7);
            specEuTaxStored = true;
         }
      }

   }

   private Hashtable getSpecEuTaxData(String var1, IDataStore var2) {
      String var3 = "0";
      Hashtable var4 = (Hashtable)ht_form_panids_spec_eu_tax_fid.get(var1);
      Hashtable var5 = new Hashtable();
      if (var4 == null) {
         return var5;
      } else {
         Enumeration var6 = var4.keys();

         while(var6.hasMoreElements()) {
            String var7 = (String)var6.nextElement();
            String var8 = (String)var4.get(var7);
            var5.put(var7, this.getString(var2.get(var3 + "_" + var8)));
         }

         return var5;
      }
   }

   private String getSpecNameData(String var1, IDataStore var2) {
      String var3 = "0";
      StringBuffer var4 = new StringBuffer();
      Hashtable var5 = (Hashtable)ht_form_panids_spec_name_fid.get(var1);
      if (var5 == null) {
         return "";
      } else {
         Hashtable var6 = new Hashtable();
         Enumeration var7 = var5.keys();

         String var9;
         while(var7.hasMoreElements()) {
            String var8 = (String)var7.nextElement();
            var9 = (String)var5.get(var8);
            var6.put(var8, this.getString(var2.get(var3 + "_" + var9)));
         }

         for(int var10 = 0; var10 < abev_panids_spec_name.length; ++var10) {
            var9 = abev_panids_spec_name[var10];
            var4.append(this.getString(var6.get(var9)));
            var4.append(" ");
         }

         return var4.toString().trim();
      }
   }

   private String getSpecEmpNameData(String var1, IDataStore var2) {
      String var3 = "0";
      StringBuffer var4 = new StringBuffer();
      Hashtable var5 = (Hashtable)ht_form_panids_spec_emp_name_fid.get(var1);
      if (var5 == null) {
         return "";
      } else {
         Hashtable var6 = new Hashtable();
         Enumeration var7 = var5.keys();

         String var9;
         while(var7.hasMoreElements()) {
            String var8 = (String)var7.nextElement();
            var9 = (String)var5.get(var8);
            var6.put(var8, this.getString(var2.get(var3 + "_" + var9)));
         }

         for(int var10 = 0; var10 < abev_panids_spec_emp_name.length; ++var10) {
            var9 = abev_panids_spec_emp_name[var10];
            var4.append(this.getString(var6.get(var9)));
            var4.append(" ");
         }

         return var4.toString().trim();
      }
   }

   private String getString(Object var1) {
      return var1 == null ? "" : var1.toString();
   }

   public MetaStore getMetaStore(Object var1) {
      return MetaInfo.getInstance().getMetaStore(var1);
   }

   public Object getHeadData(Object var1, IDataStore var2) {
      String[] var3 = new String[]{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
      String[] var4 = new String[]{"Adózó adószáma", "Adózó adóazonosító jele", "Név titulus", "Vezetékneve", "Keresztneve", "Adózó neve", "Bevallási időszak kezdete", "Bevallási időszak vége", "Munkavállaló titulus", "Munkavállaló vezetéknév", "Munkavállaló keresztnév", "Munkavállaló neve", "Munkavállaló adóazonosító jele", "Munkavállaló adószáma", "Javítani kívánt nyomtatvány vonalkódja", "Elektronikus feladás tiltása", "Bizonylat tulajdonos név", "Bizonylat tulajdonos azonosító", "Közösségi adószám ország kód", "Közösségi adószám", "Hivatali kapu azonosító"};

      try {
         MetaStore var6 = this.getMetaStore(var1);
         if ("".equals(var1)) {
            var1 = null;
         }

         if (var6 != null) {
            Vector var7 = var6.getFilteredFieldMetas_And(new Vector(Arrays.asList("panids")));
            Vector var8 = new Vector(var7.size());
            Vector var9 = new Vector(var7.size());
            int var10 = 0;

            Hashtable var5;
            for(int var11 = var7.size(); var10 < var11; ++var10) {
               var5 = (Hashtable)var7.get(var10);
               var9.clear();
               var9.addAll(Arrays.asList(this.getString(var5.get("panids")).split(",")));
               int var12 = 0;

               int var13;
               for(var13 = var9.size(); var12 < var13; ++var12) {
                  var9.set(var12, var9.get(var12).toString().trim());
               }

               var12 = 0;

               for(var13 = var4.length; var12 < var13; ++var12) {
                  if (var9.contains(var4[var12])) {
                     var8.add(var5);
                     break;
                  }
               }
            }

            String var23 = "";
            String var24 = "";
            String var25 = "";
            String var26 = "";
            String var14 = "";
            String var15 = "";
            Object[] var16 = new Object[]{"get_datastorekey", "0", null};
            int var17 = 0;

            for(int var18 = var8.size(); var17 < var18; ++var17) {
               var5 = (Hashtable)var8.get(var17);
               var16[2] = var5.get("fid");
               var9.clear();
               var9.addAll(Arrays.asList(this.getString(var5.get("panids")).split(",")));
               int var19 = 0;

               for(int var20 = var9.size(); var19 < var20; ++var19) {
                  String var21 = var9.get(var19).toString().trim();
                  if (var21.equalsIgnoreCase("Adózó adószáma")) {
                     var3[1] = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Adózó adóazonosító jele")) {
                     var3[2] = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Név titulus")) {
                     var23 = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Vezetékneve")) {
                     var24 = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Keresztneve")) {
                     var25 = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Adózó neve")) {
                     var3[3] = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Bevallási időszak kezdete")) {
                     var3[4] = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Bevallási időszak vége")) {
                     var3[5] = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Munkavállaló titulus")) {
                     var26 = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Munkavállaló vezetéknév")) {
                     var14 = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Munkavállaló keresztnév")) {
                     var15 = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Munkavállaló neve")) {
                     var3[6] = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Munkavállaló adóazonosító jele")) {
                     var3[7] = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Munkavállaló adószáma")) {
                     var3[8] = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Javítani kívánt nyomtatvány vonalkódja")) {
                     var3[9] = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Elektronikus feladás tiltása")) {
                     var3[10] = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Bizonylat tulajdonos név")) {
                     var3[11] = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Bizonylat tulajdonos azonosító")) {
                     var3[12] = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Közösségi adószám ország kód")) {
                     var3[13] = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Közösségi adószám")) {
                     var3[14] = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  } else if (var21.equalsIgnoreCase("Hivatali kapu azonosító")) {
                     var3[15] = this.getString(var2.get(var16[1] + "_" + var16[2]));
                  }
               }
            }

            if (var23.length() + var24.length() + var25.length() > 0 && (var3[3] == null || var3[3].length() == 0)) {
               var3[3] = (var23 + " " + var24 + " " + var25).trim();
            }

            if (var26.length() + var14.length() + var15.length() > 0 && (var3[6] == null || var3[6].length() == 0)) {
               var3[6] = (var26 + " " + var14 + " " + var15).trim();
            }
         }
      } catch (Exception var22) {
         Tools.eLog(var22, 1);
      }

      return var3;
   }

   public Object getPAData(Object var1, Object var2, BookModel var3) {
      Hashtable var4;
      if (var1 instanceof Object[]) {
         Object[] var5 = (Object[])((Object[])var1);
         Hashtable var6 = new Hashtable(var5.length);
         if (var3 != null) {
            MetaStore var8 = this.getMetaStore(var2);
            if ("".equals(var2)) {
               var2 = null;
            }

            if (var8 != null) {
               Vector var9 = var8.getFilteredFieldMetas_And(new Vector(Arrays.asList("panids")));
               Vector var10 = new Vector(var9.size());
               Vector var11 = new Vector(var9.size());
               int var12 = 0;
               int var13 = var9.size();

               label60:
               while(true) {
                  Hashtable var7;
                  if (var12 >= var13) {
                     IDataStore var21 = this.getDataStore(var3);
                     if (var21 == null) {
                        break;
                     }

                     Object[] var22 = new Object[]{"get_datastorekey", "0", null};
                     int var17 = 0;
                     int var18 = var10.size();

                     while(true) {
                        if (var17 >= var18) {
                           break label60;
                        }

                        var7 = (Hashtable)var10.get(var17);
                        var11.clear();
                        var11.addAll(Arrays.asList(this.getString(var7.get("panids")).split(",")));
                        var22[2] = var7.get("fid");
                        String var24 = this.getString(var21.get(var22[1] + "_" + var22[2]));
                        int var19 = 0;

                        for(int var20 = var11.size(); var19 < var20; ++var19) {
                           Object var23 = var11.get(var17);
                           Vector var16 = (Vector)var6.get(var23);
                           if (var16 == null) {
                              var16 = new Vector(8);
                              var6.put(var23, var16);
                           }

                           var16.add(var24);
                        }

                        ++var17;
                     }
                  }

                  var7 = (Hashtable)var9.get(var12);
                  var11.clear();
                  var11.addAll(Arrays.asList(this.getString(var7.get("panids")).split(",")));
                  int var14 = 0;

                  for(int var15 = var5.length; var14 < var15; ++var14) {
                     if (var11.contains(var5[var14])) {
                        var10.add(var7);
                        break;
                     }
                  }

                  ++var12;
               }
            }
         }

         var4 = var6;
      } else {
         var4 = new Hashtable();
      }

      return var4;
   }

   public IDataStore getDataStore(BookModel var1) {
      return (IDataStore)((Elem)var1.get_store_collection().get(var1.getCalcelemindex())).getRef();
   }

   private String getExtInfo(Hashtable var1, String var2, BookModel var3) {
      if (var1 != null) {
         if ("1".equals(this.getHeaderTagType(var2, var3))) {
            return " Név: " + var1.get("munkavallalo/nev") + ", " + "Azonosító" + ": " + var1.get("munkavallalo/adoazonosito") + " ";
         }

         if ("2".equals(this.getHeaderTagType(var2, var3))) {
            return " Név: " + var1.get("albizonylatazonositas/megnevezes") + ", " + "Azonosító" + ": " + var1.get("albizonylatazonositas/azonosito") + " ";
         }
      }

      return " Név: " + var1.get("nev") + " ";
   }

   private void setFatalError(boolean var1) {
      this.isFatalError = this.isFatalError || var1;
   }

   public boolean hadFatalError() {
      return this.isFatalError;
   }

   public String getHeaderTagType(String var1, BookModel var2) {
      if (!this.isSingle(var2) && !this.isSplitSaver(var2)) {
         this.createStructures(var1);
         return (String)ht_form_panids_full_emp_existing_panids.get(var1);
      } else {
         return "0";
      }
   }

   private boolean isSplitSaver(BookModel var1) {
      return var1 != null ? var1.splitesaver.equalsIgnoreCase("true") : MainFrame.thisinstance.mp.getDMFV().bm.splitesaver.equalsIgnoreCase("true");
   }

   private boolean isSingle(BookModel var1) {
      return var1 != null ? var1.isSingle() : MainFrame.thisinstance.mp.getDMFV().bm.isSingle();
   }

   public String getAlbizIdFid(String var1, BookModel var2) {
      if (var2 != null && !"".equals(var1) && var1 != null) {
         if (ht_albiz_fid.containsKey(var1)) {
            String var15 = (String)ht_albiz_fid.get(var1);
            return "".equals(var15) ? null : var15;
         } else {
            MetaStore var5 = this.getMetaStore(var1);
            if (var5 != null) {
               Vector var6 = var5.getFilteredFieldMetas_And(new Vector(Arrays.asList("panids")));
               int var7 = 0;

               for(int var8 = var6.size(); var7 < var8; ++var7) {
                  Hashtable var3 = (Hashtable)var6.get(var7);
                  String var9 = this.getString(var3.get("panids"));
                  if (!"".equals(var9)) {
                     String[] var10 = var9.split(",");
                     String[] var11 = var10;
                     int var12 = var10.length;

                     for(int var13 = 0; var13 < var12; ++var13) {
                        String var14 = var11[var13];
                        if (var14.equalsIgnoreCase("Bizonylat tulajdonos azonosító") || var14.equalsIgnoreCase("Munkavállaló adóazonosító jele")) {
                           String var4 = (String)var3.get("fid");
                           System.out.println("HeadChecker.getAlbizIdFid:" + var14 + ":" + var4);
                           ht_albiz_fid.put(var1, var4);
                           return var4;
                        }
                     }
                  }
               }
            }

            System.out.println("HeadChecker.getAlbizIdFid: Nincs találat");
            ht_albiz_fid.put(var1, "");
            return null;
         }
      } else {
         System.out.println("HeadChecker.getAlbizIdFid: Hiányos paraméterek");
         return null;
      }
   }

   static {
      abev_panids_req = new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
      abev_panids_spec_name = new String[]{"Név titulus", "Vezetékneve", "Keresztneve"};
      abev_panids_spec_name_albiz = new String[]{"Bizonylat tulajdonos név", "Bizonylat tulajdonos azonosító"};
      abev_panids_spec_emp_name = new String[]{"Munkavállaló titulus", "Munkavállaló vezetéknév", "Munkavállaló keresztnév"};
      abev_panids_spec_eu_tax = new String[]{"Közösségi adószám"};
      meta_full_emp_panids = new String[]{"Munkavállaló titulus", "Munkavállaló vezetéknév", "Munkavállaló keresztnév", "Munkavállaló neve", "Munkavállaló adóazonosító jele", "Munkavállaló adószáma"};
      kiv1 = "munkavallalo/nev";
      kiv2 = "munkavallalo/adoazonosito";
      NON_FATAL_ERRORS_DNS = new String[]{"nev", "tol", "ig"};
      ht_albiz_fid = new Hashtable();
   }
}
