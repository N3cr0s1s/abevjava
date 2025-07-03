package hu.piller.enykp.alogic.kihatas;

import java.math.BigDecimal;
import java.util.Vector;

public class KihatasRecord {
   public static final String FID_PREFIX = "FID_";
   public static final String CSF_EGYEDI = "N";
   public static final String CSF_CSOP = "C";
   public static final String FORINTOS = "N";
   public static final String KARAKTERES = "K";
   public static final int DBCOL = 11;
   public static final int K_COL_FID = 0;
   public static final int K_COL_MEGALLAPITASOK = 1;
   public static final int K_COL_ADN_KOD = 2;
   public static final int K_COL_MODOSITO_OSSZEG = 3;
   public static final int K_COL_BIMO_AZON = 4;
   public static final int K_COL_PRN_AZON = 5;
   public static final int K_COL_BMAT_LAPSORSZAM = 6;
   public static final int K_BRSZ_AZON = 7;
   public static final int K_COL_EREDETI_ERTEK = 8;
   public static final int K_COL_CSOPORT_ID = 9;
   public static final int K_COL_CSOPORT_FLAG = 10;
   public static final int K_COL_BTABLAJEL = 11;
   public static final int K_COL_MERTEKEGYSEG = 12;
   public static final int K_COL_HISTORY_ADOUGY = 13;
   public static final int K_COL_HISTORY_REVIZOR = 14;
   public static final int K_COL_HISTORY_ADOZO = 15;
   public static final int K_COL_KIHATASROGZITESJEL = 16;
   public static final int K_COL_ADATTIPUSKOD = 17;
   public static final int M_COL_KIAS_AZON = 0;
   public static final int M_COL_MSVO_AZON = 1;
   public static final int M_COL_ADN_KOD = 2;
   public static final int M_COL_TORLES_JEL = 3;
   public static final int ML_COL_MSVO_AZON = 0;
   public static final int ML_COL_NAME = 1;
   public static final int ML_COL_SORSZAM = 2;
   public static final int ML_COL_ADN_LIST = 3;
   private String fid = "";
   private MegallapitasVector megallapitasVector = new MegallapitasVector();
   private String adonemKod = "";
   private String modositoOsszeg = "";
   private String bimoAzon = "";
   private String prnAzon = "";
   private String bmatLapsorszam = "";
   private String brszAzon = "";
   private String eredetiErtek = "";
   private String csoportId = "";
   private String csoportFlag = "";
   private String btablaJel = "";
   private String mertekegyseg = "";
   private String history_adozo;
   private String history_adougy;
   private String history_revizor;
   private String kihatasRogzitesJel;
   private String adattipusKod;
   private static final String[] FIELD_NAMES = new String[]{"FID", "Megállapítások", "Adónem kód", "Módosító összeg", "bimo_azon", "prn_azon", "bmat_lapsorszam", "brsz_azon", "Eredeti érték", "csoport_id", "csoport_flag"};
   public static int fieldCount;

   public KihatasRecord() {
   }

   public KihatasRecord(String var1) {
      this.fid = var1;
   }

   public KihatasRecord(String var1, MegallapitasVector var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, String var14, String var15, String var16, String var17, String var18) {
      this.fid = var1;
      this.megallapitasVector = var2;
      this.adonemKod = var3;
      this.modositoOsszeg = var4;
      this.bimoAzon = var5;
      this.prnAzon = var6;
      this.bmatLapsorszam = var7;
      this.brszAzon = var8;
      this.eredetiErtek = var9;
      this.csoportId = var10;
      this.csoportFlag = var11;
      this.btablaJel = var12;
      this.mertekegyseg = var13;
      this.history_adozo = var14;
      this.history_adougy = var15;
      this.history_revizor = var16;
      this.kihatasRogzitesJel = var17;
      this.adattipusKod = var18;
   }

   public KihatasRecord(String var1, MegallapitasVector var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, Vector var14, String var15, String var16) {
      this.fid = var1;
      this.megallapitasVector = var2;
      this.adonemKod = var3;
      this.modositoOsszeg = var4;
      this.bimoAzon = var5;
      this.prnAzon = var6;
      this.bmatLapsorszam = var7;
      this.brszAzon = var8;
      this.eredetiErtek = var9;
      this.csoportId = var10;
      this.csoportFlag = var11;
      this.btablaJel = var12;
      this.mertekegyseg = var13;
      this.setHistory(var14);
      this.kihatasRogzitesJel = var15;
      this.adattipusKod = var16;
   }

   public String getFid() {
      return this.fid;
   }

   public void setFid(String var1) {
      this.fid = var1;
   }

   public MegallapitasVector getMegallapitasVector() {
      return this.megallapitasVector;
   }

   public void setMegallapitasVector(MegallapitasVector var1) {
      this.megallapitasVector = var1;
   }

   public String getAdonemKod() {
      return this.adonemKod;
   }

   public void setAdonemKod(String var1) {
      this.adonemKod = var1;
   }

   public String getModositoOsszeg() {
      return this.modositoOsszeg;
   }

   public void setModositoOsszeg(String var1) {
      this.modositoOsszeg = var1;
   }

   public String getBimoAzon() {
      return this.bimoAzon;
   }

   public void setBimoAzon(String var1) {
      this.bimoAzon = var1;
   }

   public String getPrnAzon() {
      return this.prnAzon;
   }

   public void setPrnAzon(String var1) {
      this.prnAzon = var1;
   }

   public String getBmatLapsorszam() {
      return this.bmatLapsorszam;
   }

   public void setBmatLapsorszam(String var1) {
      this.bmatLapsorszam = var1;
   }

   public String getBrszAzon() {
      return this.brszAzon;
   }

   public void setBrszAzon(String var1) {
      this.brszAzon = var1;
   }

   public String getEredetiErtek() {
      return this.eredetiErtek;
   }

   public void setEredetiErtek(String var1) {
      this.eredetiErtek = var1;
   }

   public String getCsoportId() {
      return this.csoportId;
   }

   public void setCsoportId(String var1) {
      this.csoportId = var1;
   }

   public String getCsoportFlag() {
      return this.csoportFlag;
   }

   public void setCsoportFlag(String var1) {
      this.csoportFlag = var1;
   }

   public String getBtablaJel() {
      return this.btablaJel;
   }

   public void setBtablaJel(String var1) {
      this.btablaJel = var1;
   }

   public String getMertekegyseg() {
      return this.mertekegyseg;
   }

   public void setMertekegyseg(String var1) {
      this.mertekegyseg = var1;
   }

   public void setHistory(Vector var1) {
      try {
         this.history_adozo = (String)var1.get(0);
      } catch (Exception var5) {
         this.history_adozo = "";
      }

      try {
         this.history_adougy = (String)var1.get(1);
      } catch (Exception var4) {
         this.history_adougy = "";
      }

      try {
         this.history_revizor = (String)var1.get(2);
      } catch (Exception var3) {
         this.history_revizor = "";
      }

   }

   public String getHistory_adozo() {
      return this.history_adozo == null ? "" : this.history_adozo;
   }

   public void setHistory_adozo(String var1) {
      this.history_adozo = var1;
   }

   public String getHistory_adougy() {
      return this.history_adougy == null ? "" : this.history_adougy;
   }

   public void setHistory_adougy(String var1) {
      this.history_adougy = var1;
   }

   public String getHistory_revizor() {
      return this.history_revizor == null ? "" : this.history_revizor;
   }

   public void setHistory_revizor(String var1) {
      this.history_revizor = var1;
   }

   public String getKihatasRogzitesJel() {
      return this.kihatasRogzitesJel;
   }

   public void setKihatasRogzitesJel(String var1) {
      this.kihatasRogzitesJel = var1;
   }

   public String getAdattipusKod() {
      return this.adattipusKod;
   }

   public void setAdattipusKod(String var1) {
      this.adattipusKod = var1;
   }

   public boolean isEmpty() {
      return "".equals(this.fid);
   }

   public String getFieldName(int var1) {
      return var1 >= fieldCount ? null : FIELD_NAMES[var1];
   }

   public String toString() {
      return "FID: " + this.fid + ", Megállapítások: " + this.megallapitasVector + ", Adónem kód: " + this.adonemKod + ", Módosító összeg: " + this.modositoOsszeg + ", bimo_azon: " + this.bimoAzon + ", prn_azon " + this.prnAzon + ", bmat_lapsorszam " + this.bmatLapsorszam + ", brsz_azon " + this.brszAzon + ", Eredeti érték " + this.eredetiErtek + ", csoport_id " + this.csoportId + ", csoport_flag " + this.csoportFlag + ", btablajel " + this.btablaJel + ", Mértékegység: " + this.mertekegyseg + ", history: ad:" + this.history_adozo + ", aü:" + this.history_adougy + ", r:" + this.history_revizor + ", kihatás örgzítés jel: " + this.kihatasRogzitesJel + ", adattípus kód: " + this.adattipusKod;
   }

   public Object getValue(int var1) {
      switch(var1) {
      case 0:
         return this.getFid();
      case 1:
         return this.getMegallapitasVector();
      case 2:
         return this.getAdonemKod();
      case 3:
         return this.getModositoOsszeg();
      case 4:
         return this.getBimoAzon();
      case 5:
         return this.getPrnAzon();
      case 6:
         return this.getBmatLapsorszam();
      case 7:
         return this.getBrszAzon();
      case 8:
         return this.getEredetiErtek();
      case 9:
         return this.getCsoportId();
      case 10:
         return this.getCsoportFlag();
      case 11:
         return this.getBtablaJel();
      case 12:
         return this.getMertekegyseg();
      case 13:
         return this.getHistory_adougy();
      case 14:
         return this.getHistory_revizor();
      case 15:
         return this.getHistory_adozo();
      case 16:
         return this.getKihatasRogzitesJel();
      case 17:
         return this.getAdattipusKod();
      default:
         return null;
      }
   }

   public void setValue(Object var1, int var2) {
      switch(var2) {
      case 0:
         this.setFid((String)var1);
         break;
      case 1:
         this.setMegallapitasVector((MegallapitasVector)var1);
         break;
      case 2:
         this.setAdonemKod((String)var1);
         break;
      case 3:
         this.setModositoOsszeg((String)var1);
         break;
      case 4:
         this.setBimoAzon((String)var1);
         break;
      case 5:
         this.setPrnAzon((String)var1);
         break;
      case 6:
         this.setBmatLapsorszam((String)var1);
         break;
      case 7:
         this.setBrszAzon((String)var1);
         break;
      case 8:
         this.setEredetiErtek((String)var1);
         break;
      case 9:
         this.setCsoportId((String)var1);
         break;
      case 10:
         this.setCsoportFlag((String)var1);
         break;
      case 11:
         this.setBtablaJel((String)var1);
         break;
      case 12:
         this.setMertekegyseg((String)var1);
      }

   }

   public KihatasRecord make_copy() {
      KihatasRecord var1 = new KihatasRecord(this.fid, this.megallapitasVector.make_copy(), this.adonemKod, this.modositoOsszeg, this.bimoAzon, this.prnAzon, this.bmatLapsorszam, this.brszAzon, this.eredetiErtek, this.csoportId, this.csoportFlag, this.btablaJel, this.mertekegyseg, this.history_adozo, this.history_adougy, this.history_revizor, this.kihatasRogzitesJel, this.adattipusKod);
      return var1;
   }

   public BigDecimal getModositoOsszegValue() {
      BigDecimal var1 = null;

      try {
         var1 = new BigDecimal(this.modositoOsszeg.replaceAll(",", "."));
      } catch (Exception var3) {
         var1 = new BigDecimal(0);
      }

      return var1;
   }

   public BigDecimal getModositoOsszegValue2() {
      BigDecimal var1 = null;

      try {
         var1 = new BigDecimal(this.modositoOsszeg.replaceAll(",", "."));
      } catch (Exception var3) {
         var1 = null;
      }

      return var1;
   }

   public void update(String var1, String var2, int var3, String var4, String var5, String var6) {
      int var7 = var2.indexOf("_");
      if (var7 != -1) {
         var2 = var2.substring(var7 + 1);
      }

      this.setEredetiErtek(var1);
      this.setFid(var4 + "@" + var2);
      this.setBmatLapsorszam(var3 + "");
      this.setCsoportFlag(var6);
   }

   public void update(String var1, String var2, int var3, String var4, String var5, String var6, String var7, String var8) {
      int var9 = var2.indexOf("_");
      if (var9 != -1) {
         var2 = var2.substring(var9 + 1);
      }

      this.setEredetiErtek(var1);
      this.setFid(var4 + "@" + var2);
      this.setBmatLapsorszam(var3 + "");
      this.setCsoportFlag(var6);
      this.setBtablaJel(var7);
      this.setMertekegyseg(var8);
   }

   public boolean vannemtorolt() {
      return this.megallapitasVector.vannemtorolt();
   }

   static {
      fieldCount = FIELD_NAMES.length;
   }
}
