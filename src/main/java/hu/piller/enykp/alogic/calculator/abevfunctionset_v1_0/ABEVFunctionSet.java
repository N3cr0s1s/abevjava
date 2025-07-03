package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0;

import cec.EUFunctionBodies;
import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.calculator.calculator_c.ExpClass;
import hu.piller.enykp.alogic.calculator.lookup.LookupListHandler;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.templateutils.FieldsGroups;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IEventLog;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.EventLog;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class ABEVFunctionSet implements IFunctionSet {
   public static final Long ID_DEFAULT_FUNCTION_ERROR = new Long(4001L);
   private static final int IDX_ERR_ID = 0;
   private static final int IDX_ERR_MSG = 1;
   public static final int IDX_ERR_EXCEPTION = 2;
   public static final int IDX_ERR_USERDATA = 3;
   private static boolean calcNotCall = true;
   public static final String VAR_PREFIX = "$";
   private static final String CALC_INPAR_FORMID = "form_id";
   public static final String CALC_INPAR_TARGETID = "target_id";
   public static final String CALC_MESSAGE_TITLE = "Ellenőrzési, számítási üzenet";
   private static final String FN_JOADOSZAM = "jóadószám";
   private static final String FN_SUM = "sum";
   private static final String FN_SUMNEG = "sumneg";
   private static final String FN_SUMPOS = "sumpos";
   private static final String FN_AVRG = "avrg";
   private static final String FN_CSAKSZAMJEGY = "csakszámjegy";
   private static final String FN_SZAMJEGY = "számjegy";
   private static final String FN_UJ_VPOPSZAM = "új_vpopszám";
   private static final String FN_EVESADO1996 = "évesadó1996";
   private static final String FN_EVESADO1997 = "évesadó1997";
   private static final String FN_EVESADO1999 = "évesadó1999";
   private static final String FN_EVESADO2001 = "évesadó2001";
   private static final String FN_EVESADO2002 = "évesadó2002";
   private static final String FN_EVESADO2003 = "évesadó2003";
   private static final String FN_EVESADO2004 = "évesadó2004";
   private static final String FN_EVESADO2005 = "évesadó2005";
   private static final String FN_EVESADO2007 = "évesadó2007";
   private static final String FN_JOADOAZONOSITO = "jóadóazonosító";
   private static final String FN_JOSZAMLASZAM = "jószámlaszám";
   private static final String FN_JOALSZAMLASZAM = "jóalszámlaszám";
   private static final String FN_IN = "in";
   private static final String FN_JO_DATUM = "jódátum";
   private static final String FN_JO_HATAROZATSZAM = "jóhatározatszám";
   private static final String FN_JO_VPOPSZAM = "jóvpopszám";
   private static final String FN_KISBETUS = "kisbetüs";
   private static final String FN_KORNYEZET = "környezet";
   private static final String FN_LEN = "len";
   private static final String FN_DATUMEV = "dátumév";
   private static final String FN_DATUMHO = "dátumhó";
   private static final String FN_DATUMNAP = "dátumnap";
   private static final String FN_DATUMNEV = "dátumnév";
   private static final String FN_DATUMOKKULONBSEGE = "dátumok_különbsége";
   private static final String FN_DATUMPLUSZNAP = "dátumplusznap";
   private static final String FN_EGYUTTESEN_KITOLTENDO = "együttesenkitöltendő";
   private static final String FN_FILLN = "filln";
   private static final String FN_FOLYTONOS = "folytonos";
   private static final String FN_UNIQUE = "unique";
   private static final String FN_KITOLTOTT = "kitöltött";
   private static final String FN_KITOLTOTT_DARAB = "kitöltöttdarab";
   private static final String FN_FLAG = "flag";
   private static final String FN_HONAP_UTOLSO_NAPJA = "hónaputolsónapja";
   private static final String FN_NEGYEDEV_UTOLSO_NAPJA = "negyedévutolsónapja";
   private static final String FN_SZAM = "szám";
   private static final String FN_SUBSTR = "substr";
   private static final String FN_RIGHTSTR = "rightstr";
   private static final String FN_SZAMSTRING = "szám_string";
   private static final String FN_NOT = "not";
   private static final String FN_MUNKANAP = "munkanap";
   private static final String FN_NUM2STR = "num2str";
   private static final String FN_MAINAP = "mainap";
   private static final String FN_MODULO = "modulo";
   public static final String FN_HAN = "ha_n";
   private static final String FN_SPEC_FGV3 = "spec_fgv3";
   private static final String FN_SPEC_FGV4 = "spec_fgv4";
   private static final String FN_SPEC_FGV5 = "spec_fgv5";
   private static final String FN_SPEC_FGV6 = "spec_fgv6";
   private static final String FN_SPEC_FGV7 = "spec_fgv7";
   private static final String FN_SPEC_FGV8 = "spec_fgv8";
   private static final String FN_SPEC_FGV9 = "spec_fgv9";
   private static final String FN_SPEC_FGV10 = "spec_fgv10";
   private static final String FN_NAGYBETUS = "nagybetüs";
   private static final String FN_POS = "pos";
   private static final String FN_KERESES_MATRIXBAN = "keresés_mátrixban";
   private static final String FN_KERESES_MATRIXBAN2 = "keresés_mátrixban2";
   private static final String FN_KERESES_MATRIXBAN3 = "keresés_mátrixban3";
   private static final String FN_SPEC_FGV1 = "spec_fgv1";
   private static final String FN_SPEC_FGV2 = "spec_fgv2";
   private static final String FN_OSZLOP_SZIGET = "oszlop_sziget";
   private static final String FN_OSZLOP_FOLYTONOS = "oszlopfolytonos";
   private static final String FN_OSZLOP_UNIQUE = "oszlopunique";
   private static final String FN_ELSO_KITOLTOTT = "első_kitöltött";
   private static final String FN_UTOLSO_KITOLTOTT = "utolsó_kitöltött";
   private static final String FN_D_KITOLTOTTEK_SZAMA = "d_kitöltöttekszáma";
   private static final String FN_SOR = "sor";
   private static final String FN_OSZLOP = "oszlop";
   private static final String FN_LAPOKSZAMA = "lapokszáma";
   private static final String FN_UTOLSO_LAP = "utolso_lap";
   private static final String FN_ELSO_DLAP = "első_dlap";
   private static final String FN_FELTETELES_ERTEK = "feltételes_érték";
   private static final String FN_FELTETELES_ERTEK2 = "feltételes_érték2";
   private static final String FN_FELTETELES_ERTEKADAS = "feltételes_értékadás";
   private static final String FN_DFIRST = "DFirst";
   private static final String FN_DPAGECOUNT = "DPageCount";
   private static final String FN_DPAGENUMBER = "DPageNumber";
   private static final String FN_DPREV = "DPrev";
   private static final String FN_DSUM = "DSum";
   private static final String FN_DERTEK_AZONOS = "d_érték_azonos";
   private static final String FN_DERTEK_KULONBOZO = "d_érték_különböző";
   private static final String FN_VANOLYANDINLAP = "din_lapra_feltétel_teljesült_darab";
   private static final String FN_VANOLYANDINLAP_CN = "din_lapra_feltétel123_teljesült_m_n_kapcsolat_darab";
   private static final String FN_VANOLYANDINLAP_C1 = "din_lapra_feltétel123_teljesült_egy_egy_kapcsolat_darab";
   public static final String FN_GLOB_SUM = "globsum";
   public static final String FN_IMP = "imp";
   private static final String FN_EKV = "ekv";
   private static final String FN_STRING = "string";
   private static final String FN_TRIM = "trim";
   private static final String FN_EVESADO2006 = "évesadó2006";
   private static final String FN_JOOSTERMELO = "jóőstermelő";
   private static final String FN_JOTAJSZAM = "jótajszám";
   private static final String FN_JOBARKOD = "jóbárkód";
   private static final String FN_JOB_TESZT = "jobteszt";
   private static final String FN_GETFIDBYVID = "getfidbyvid";
   private static final String FN_FIELDREADONLY = "FieldReadOnly";
   private static final String FN_GETREALZERO = "getRealZero";
   private static final String FN_ISZEROVISIBLE = "isZeroVisible";
   public static final String FN_UNIQM = "unique_m";
   private static final String FN_COMPAREAM = "compare_am";
   private static final String FN_EUJOADOSZAM = "eu_jóadószám";
   private static final String FN_JOCSOPORTAZONOSITO = "jócsoportazonosító";
   private static final String FN_DIN_LAP_KAPCSOL = "din_lap_kapcsol";
   private static final String FN_DIN_OLDAL_KAPCSOL = "din_oldal_kapcsol";
   private static final String FN_FILL_ON_PREVIOUS = "fill_on_previous";
   private static final String FN_FILL_ON_FOLLOWING = "fill_on_following";
   private static final String FN_VPOP_JOVPID = "jóvpid";
   private static final String FN_VPOP_JOGLN = "jógln";
   private static final String FN_VPOP_JORAGSZAM = "jóregszám";
   private static final String FN_VPOP_JOENGEDELYSZAM = "jóengedélyszám";
   private static final String FN_JO_NYILVANTARTASI_SZAM = "jó_nyilv_szám";
   private static final String FN_JO_BIC = "jó_bic";
   private static final String FN_JO_IBAN = "jó_iban";
   private static final String FN_JO_IMPORTHATAROZAT = "jó_importhatározat";
   private static final String FN_VPOP_TILTESTOROL = "tiltéstöröl";
   private static final String FN_DUMMY_VERSION = "verziokontroll";
   private static final String FN_CSATOLMANY_DB = "csatolmánydb";
   private static final String FN_UZEMMOD = "üzemmód";
   private static final String FN_EREDETI = "eredeti";
   private static final String FN_ANYK_PARAM = "anyk_param";
   private static final String FN_DATUMPLUSZHO = "dátumpluszhó";
   private static final String FN_INTERVALLUM_ATFEDES = "nincsátfedés";
   private static final String FN_INTERVALLUM_ATFEDES_CSOPORT = "nincsátfedés2";
   private static final String FN_NAPOK_SZAMA = "napok_száma";
   private static final String FN_TABLAZAT_UNIQUE = "táblázatunique";
   private static final String FN_LAP_UNIQUE = "lapunique";
   private static final String FN_MATRIX_SZURES = "mátrix_szűrés";
   private static final String FN_FELTETELES_ERTEKLISTA = "feltételes_értéklista";
   private static final String FN_FELTETELES_ERTEKLISTA_GROUP = "feltételes_értéklista2";
   private static final String FN_ELOZO_ERTEK = "előző_érték";
   private static final String FN_ORD = "ord";
   private static final String FN_DIN_MAX = "din_max";
   private static final String FN_DIN_MIN = "din_min";
   public static final String FN_AKT_DIN_ERTEK = "akt_din_érték";
   public static final String FN_PRE_GEN_ERTEK = "pre_gen_érték";
   public static final String FN_POST_GEN_ERTEK = "post_gen_érték";
   public static final String FN_GEN_AKTUALIS_ERTEK = "gen_aktuális_érték";
   public static final String FN_GEN_UTOLSO_DIN_OLDAL = "gen_utolsó_din_oldal";
   public static final String FN_GEN_MEZO_AZONOSITO = "gen_mező_azonosító";
   private static final String FN_LESS = "<";
   private static final String FN_GREATER = ">";
   private static final String FN_LESS_OR_EQ = "<=";
   private static final String FN_GREATER_OR_EQ = ">=";
   public static final String FN_FIELD = "field";
   public static final String FN_ADOZOI_ERTEK = "adózói_érték";
   private static final String FN_EQUAL = "=";
   private static final String FN_NOT_EQUAL = "!=";
   private static final String FN_PLUS = "+";
   private static final String FN_MINUS = "-";
   private static final String FN_POWER = "*";
   private static final String FN_DIVISION = "/";
   private static final String FN_MIN = "min";
   private static final String FN_MAX = "max";
   private static final String FN_ROUND = "round";
   public static final String FN_IF = "if";
   private static final String FN_CALLGUI = "callgui";
   private static final String FN_AND = "&";
   private static final String FN_OR = "|";
   private static final String FN_XOR = "^";
   private static final String FN_ABS = "abs";
   private static final String FN_SETFIELDVALUE = "setfieldvalue";
   public static final String FIELD_ATTRIBUTE_TOLERANCE = "tolerance";
   public static final String FIELD_ATTRIBUTE_TYPE = "type";
   public static final String FN_ANYK_PARAM_CALCULATED = "számított";
   public static final String FN_ANYK_PARAM_KOTEGELT = "kötegelt";
   public static final String FN_ANYK_PARAM_FONYOMTATVANY_AZONOSITO = "főnyomtatvány_azonosító";
   public static final String FN_ANYK_PARAM_HASZNALATI_MOD = "használati_mód";
   public static final String FN_JO_EMAIL = "jóemail";
   public static final String FN_JO_PTG_REG_KOD = "jó_ptg_reg_kód";
   public static final String FN_FO_ADAT = "főadat";
   public static final String FN_ELOZMENY_ERTEK = "előzmény_érték";
   public static final String FN_PARAMETERES_MASOLAS = "paraméteres_másolás";
   public static final String FN_MEZO_LISTA = "mező_lista";
   public static final String FN_BETOLT_ERTEK = "betölt_érték";
   public static final String FN_BETOLT_AKTUALIS_ERTEK = "betölt_aktuális_érték";
   public static final String FN_ANYK_PARAM_SZERKESZTES = "szerkesztés";
   public static final String FN_ERVENYES_ADOSZAM = "érvényesadószám";
   public static final String FN_SPEC_LANC_DARAB = "spec_lánc_darab";
   public static final String FN_SPEC_LANC_SUM = "spec_lánc_sum";
   public static final String FN_DIN_STRING_SUM = "din_string_sum";
   public static final String FN_LOGIKAI_NULL = "logikai_null";
   private static IErrorList masterErrorList = null;
   private static IEventLog eventLog = null;
   public static ExpWrapper expwrapper = null;
   public static final String DEPT_TYPE_DINPAGES = "dept_type_dinpages";
   private static final String[] FUNCTION_NAMES_REVERSE_CALCULATION = new String[]{"globsum", "din_lap_kapcsol", "din_oldal_kapcsol", "din_lapra_feltétel_teljesült_darab", "din_lapra_feltétel123_teljesült_m_n_kapcsolat_darab", "din_lapra_feltétel123_teljesült_egy_egy_kapcsolat_darab", "compare_am", "unique_m", "pre_gen_érték", "post_gen_érték", "betölt_érték", "betölt_aktuális_érték"};
   private static final String[] FUNCTION_NAMES_IS_MEMEBER_OF_DEPENDENCY_ARR = new String[]{"field", "első_kitöltött", "utolsó_kitöltött", "d_kitöltöttekszáma", "első_dlap", "feltételes_érték", "feltételes_érték2", "DFirst", "DPrev", "DSum", "spec_fgv5", "DPageCount", "DPageNumber", "globsum", "unique_m", "d_érték_azonos", "d_érték_különböző", "nincsátfedés", "nincsátfedés2", "napok_száma", "táblázatunique", "din_max", "din_min", "akt_din_érték", "adózói_érték", "feltételes_értékadás", "paraméteres_másolás", "mező_lista", "főadat", "spec_lánc_darab", "spec_lánc_sum", "din_string_sum", "logikai_null"};
   private static final String[] FUNCTION_NAMES_IS_MEMEBER_OF_CHECK_ARR = new String[]{"globsum", "unique_m"};
   private static Hashtable FUNCTION_NAMES_IS_MEMEBER_OF_DEPENDENCY = null;
   private static Hashtable FUNCTION_NAMES_IS_MEMEBER_OF_CHECK = null;
   private static final String[] FUNCTION_NAMES = new String[]{"jóadószám", "sum", "sumneg", "sumpos", "avrg", "csakszámjegy", "számjegy", "új_vpopszám", "évesadó1996", "évesadó1997", "évesadó1999", "évesadó2001", "évesadó2002", "évesadó2003", "évesadó2004", "évesadó2005", "jóadóazonosító", "jószámlaszám", "jóalszámlaszám", "in", "jódátum", "jóhatározatszám", "jóvpopszám", "kisbetüs", "környezet", "len", "dátumév", "dátumhó", "dátumnap", "dátumnév", "dátumok_különbsége", "dátumplusznap", "együttesenkitöltendő", "filln", "folytonos", "unique", "kitöltött", "kitöltöttdarab", "flag", "hónaputolsónapja", "negyedévutolsónapja", "szám", "substr", "rightstr", "szám_string", "not", "munkanap", "num2str", "mainap", "modulo", "ha_n", "spec_fgv4", "spec_fgv3", "nagybetüs", "pos", "spec_fgv1", "spec_fgv2", "spec_fgv5", "spec_fgv6", "spec_fgv7", "spec_fgv8", "spec_fgv9", "spec_fgv10", "keresés_mátrixban", "keresés_mátrixban2", "oszlop_sziget", "oszlopfolytonos", "oszlopunique", "első_kitöltött", "utolsó_kitöltött", "d_kitöltöttekszáma", "sor", "oszlop", "lapokszáma", "utolso_lap", "első_dlap", "feltételes_érték", "feltételes_értékadás", "imp", "string", "ekv", "évesadó2006", "jóőstermelő", "jótajszám", "jóbárkód", "DSum", "DFirst", "DPageCount", "DPageNumber", "DPrev", "d_érték_azonos", "d_érték_különböző", "din_lapra_feltétel_teljesült_darab", "din_lapra_feltétel123_teljesült_m_n_kapcsolat_darab", "din_lapra_feltétel123_teljesült_egy_egy_kapcsolat_darab", "getRealZero", "isZeroVisible", "<", ">", "<=", ">=", "field", "=", "!=", "*", "/", "+", "-", "min", "max", "round", "globsum", "jobteszt", "getfidbyvid", "FieldReadOnly", "unique_m", "compare_am", "if", "callgui", "&", "|", "^", "abs", "eu_jóadószám", "évesadó2007", "feltételes_érték2", "jócsoportazonosító", "keresés_mátrixban3", "din_lap_kapcsol", "fill_on_previous", "fill_on_following", "jóvpid", "jógln", "jóregszám", "tiltéstöröl", "verziokontroll", "csatolmánydb", "üzemmód", "eredeti", "anyk_param", "dátumpluszhó", "trim", "jóemail", "nincsátfedés", "táblázatunique", "jó_nyilv_szám", "lapunique", "jó_bic", "jó_iban", "jó_importhatározat", "mátrix_szűrés", "feltételes_értéklista", "előző_érték", "ord", "din_max", "din_min", "nincsátfedés2", "napok_száma", "feltételes_értéklista2", "din_oldal_kapcsol", "akt_din_érték", "adózói_érték", "jó_ptg_reg_kód", "pre_gen_érték", "post_gen_érték", "gen_aktuális_érték", "gen_utolsó_din_oldal", "gen_mező_azonosító", "főadat", "előzmény_érték", "paraméteres_másolás", "mező_lista", "betölt_érték", "betölt_aktuális_érték", "érvényesadószám", "spec_lánc_darab", "spec_lánc_sum", "din_string_sum", "logikai_null"};
   public static final Hashtable FUNCTION_DESCRIPTORS;
   private static final Hashtable FUNCTION_QUICK_REF;
   private static final int DEFAULT_STRUCTURE_ID = 0;
   private static final Long RESOURCE_ERROR_ID;
   private static final String RESOURCE_NAME = "Függvény készlet";
   private static ABEVFunctionSet instance;
   private Hashtable specialDependencies = null;

   public ABEVFunctionSet() {
      EUFunctionBodies.init();
      instance = this;
      this.getMasterErrorList();
      this.getEventLog();
   }

   public void initialize(Object var1) {
      this.initializeFunctions();
      expwrapper = new ExpWrapper();
   }

   public void init(BookModel var1) {
      FunctionBodies.setIc(Calculator.getInstance());
      FunctionBodies.setInfoObject(var1);
      FunctionBodies.setMetaInfo(MetaInfo.getInstance());
      FIdHelper.setInfoObject(var1);
      FunctionBodies.matrixHandler.release();
      LookupListHandler.getInstance().release();
      FieldsGroups.getInstance().release();
      FunctionBodies.release();
      FunctionBodies.initStaticData();
      expwrapper = new ExpWrapper();
   }

   public void release() {
      FunctionBodies.setInfoObject((Object)null);
      FunctionBodies.setMetaInfo((Object)null);
      FIdHelper.setInfoObject((BookModel)null);
      FunctionBodies.matrixHandler.release();
      LookupListHandler.getInstance().release();
      FieldsGroups.getInstance().release();
      FunctionBodies.release();
      FunctionBodies.initStaticData();
      expwrapper = new ExpWrapper();
      this.specialDependencies = new Hashtable();
   }

   public static ABEVFunctionSet getInstance() {
      return instance;
   }

   private String getProperty(String var1) {
      String var4 = "";
      IPropertyList var2 = this.getMasterPropertyList();
      if (var2 != null) {
         Object var3 = var2.get(var1);
         if (var3 != null) {
            var4 = var3.toString();
         }
      }

      return var4;
   }

   private static Boolean isForwardCalculation(String var0) {
      for(int var1 = 0; var1 < FUNCTION_NAMES_REVERSE_CALCULATION.length; ++var1) {
         if (FUNCTION_NAMES_REVERSE_CALCULATION[var1].equalsIgnoreCase(var0)) {
            return Boolean.FALSE;
         }
      }

      return Boolean.TRUE;
   }

   private static Boolean isMemberOfDependency(String var0) {
      return FUNCTION_NAMES_IS_MEMEBER_OF_DEPENDENCY != null ? FUNCTION_NAMES_IS_MEMEBER_OF_DEPENDENCY.containsKey(var0.toLowerCase()) : Boolean.FALSE;
   }

   private static Boolean isMemberOfCheck(String var0) {
      return FUNCTION_NAMES_IS_MEMEBER_OF_CHECK != null ? FUNCTION_NAMES_IS_MEMEBER_OF_CHECK.containsKey(var0.toLowerCase()) : Boolean.FALSE;
   }

   private void initializeFunctions() {
      initDependencies();
      initCalculations();
      int var1 = 0;

      for(int var2 = FUNCTION_NAMES.length; var1 < var2; ++var1) {
         FUNCTION_DESCRIPTORS.put(FUNCTION_NAMES[var1].toLowerCase(), OperHelper.createOperation(FUNCTION_NAMES[var1].toLowerCase(), this, isForwardCalculation(FUNCTION_NAMES[var1]), isMemberOfDependency(FUNCTION_NAMES[var1]), isMemberOfCheck(FUNCTION_NAMES[var1])));
         FUNCTION_QUICK_REF.put(FUNCTION_NAMES[var1].toLowerCase(), new Integer(var1));
      }

   }

   private static void initDependencies() {
      FUNCTION_NAMES_IS_MEMEBER_OF_DEPENDENCY = new Hashtable(FUNCTION_NAMES_IS_MEMEBER_OF_DEPENDENCY_ARR.length);

      for(int var0 = 0; var0 < FUNCTION_NAMES_IS_MEMEBER_OF_DEPENDENCY_ARR.length; ++var0) {
         FUNCTION_NAMES_IS_MEMEBER_OF_DEPENDENCY.put(FUNCTION_NAMES_IS_MEMEBER_OF_DEPENDENCY_ARR[var0].toLowerCase(), "");
      }

   }

   private static void initCalculations() {
      FUNCTION_NAMES_IS_MEMEBER_OF_CHECK = new Hashtable(FUNCTION_NAMES_IS_MEMEBER_OF_CHECK_ARR.length);

      for(int var0 = 0; var0 < FUNCTION_NAMES_IS_MEMEBER_OF_CHECK_ARR.length; ++var0) {
         FUNCTION_NAMES_IS_MEMEBER_OF_CHECK.put(FUNCTION_NAMES_IS_MEMEBER_OF_CHECK_ARR[var0].toLowerCase(), "");
      }

   }

   private IErrorList getMasterErrorList() {
      if (masterErrorList == null) {
         masterErrorList = ErrorList.getInstance();
      }

      return masterErrorList;
   }

   public IEventLog getEventLog() {
      if (eventLog == null) {
         try {
            eventLog = EventLog.getInstance();
         } catch (IOException var2) {
            Tools.eLog(var2, 1);
         }
      }

      return eventLog;
   }

   private IPropertyList getMasterPropertyList() {
      return PropertyList.getInstance();
   }

   public Object calculate(ExpClass var1) {
      FunctionBodies.calc_error_list.removeAllElements();
      this.calculateDefaultStructure(var1);
      this.evaluateErrorList(FunctionBodies.calc_error_list, var1);
      return var1.getResult();
   }

   private void calculateDefaultStructure(ExpClass var1) {
      String var2 = var1.getIdentifier();
      FunctionBodies.initFormid();
      var1.setFlag(0);
      var1.setDontModify(false);
      int var3 = 0;
      boolean var4 = false;
      if (!var2.equalsIgnoreCase("if") && !var2.equalsIgnoreCase("ha_n") && !var2.equalsIgnoreCase("imp") && !var2.equalsIgnoreCase("feltételes_értékadás")) {
         var3 = getFlags(var1);
         var4 = getDontModifies(var1);
      }

      if (var2.equalsIgnoreCase("feltételes_értékadás")) {
         var3 = 0;
      }

      int var5 = (Integer)FUNCTION_QUICK_REF.get(var2);
      switch(var5) {
      case 0:
         FunctionBodies.fnJoAdoszam(var1);
         break;
      case 1:
         FunctionBodies.fnSum(var1);
         break;
      case 2:
         FunctionBodies.fnSumNeg(var1);
         break;
      case 3:
         FunctionBodies.fnSumPos(var1);
         break;
      case 4:
         FunctionBodies.fnAvrg(var1);
         break;
      case 5:
         FunctionBodies.fnCsakSzamjegy(var1);
         break;
      case 6:
         FunctionBodies.fnSzamjegy(var1);
         break;
      case 7:
         FunctionBodies.fnUjVPOPSzam(var1);
         break;
      case 8:
         FunctionBodies.fnEvesAdo1996(var1);
         break;
      case 9:
         FunctionBodies.fnEvesAdo1997(var1);
         break;
      case 10:
         FunctionBodies.fnEvesAdo1999(var1);
         break;
      case 11:
         FunctionBodies.fnEvesAdo2001(var1);
         break;
      case 12:
         FunctionBodies.fnEvesAdo2002(var1);
         break;
      case 13:
         FunctionBodies.fnEvesAdo2003(var1);
         break;
      case 14:
         FunctionBodies.fnEvesAdo2004(var1);
         break;
      case 15:
         FunctionBodies.fnEvesAdo2005(var1);
         break;
      case 16:
         FunctionBodies.fnJoAdoAzonosito(var1);
         break;
      case 17:
         FunctionBodies.fnJoSzamlaszam(var1);
         break;
      case 18:
         FunctionBodies.fnJoAlszamlaSzam(var1);
         break;
      case 19:
         FunctionBodies.fnIn(var1);
         break;
      case 20:
         FunctionBodies.fnJoDatum(var1);
         break;
      case 21:
         FunctionBodies.fnJoHatarozatszam(var1);
         break;
      case 22:
         FunctionBodies.fnJoVPOPSzam(var1);
         break;
      case 23:
         FunctionBodies.fnKisbetus(var1);
         break;
      case 24:
         FunctionBodies.fnKornyezet(var1);
         break;
      case 25:
         FunctionBodies.fnLen(var1);
         break;
      case 26:
         FunctionBodies.fnDatumEv(var1);
         break;
      case 27:
         FunctionBodies.fnDatumHo(var1);
         break;
      case 28:
         FunctionBodies.fnDatumNap(var1);
         break;
      case 29:
         FunctionBodies.fnDatumNev(var1);
         break;
      case 30:
         FunctionBodies.fnDatumokKulonbsege(var1);
         break;
      case 31:
         FunctionBodies.fnDatumPlusznap(var1);
         break;
      case 32:
         FunctionBodies.fnEgyuttesenKitoltendo(var1);
         break;
      case 33:
         FunctionBodies.fnFilln(var1);
         break;
      case 34:
         FunctionBodies.fnFolytonos(var1);
         break;
      case 35:
         FunctionBodies.fnUnique(var1);
         break;
      case 36:
         FunctionBodies.fnKitoltott(var1);
         break;
      case 37:
         FunctionBodies.fnKitoltottDarab(var1);
         break;
      case 38:
         FunctionBodies.fnFlag(var1);
         break;
      case 39:
         FunctionBodies.fnHonapUtolsoNapja(var1);
         break;
      case 40:
         FunctionBodies.fnNegyedevUtolsoNapja(var1);
         break;
      case 41:
         FunctionBodies.fnSzam(var1);
         break;
      case 42:
         FunctionBodies.fnSubStr(var1);
         break;
      case 43:
         FunctionBodies.fnRightStr(var1);
         break;
      case 44:
         FunctionBodies.fnSzamString(var1);
         break;
      case 45:
         FunctionBodies.fnNot(var1);
         break;
      case 46:
         FunctionBodies.fnMunkanap(var1);
         break;
      case 47:
         FunctionBodies.fnNum2Str(var1);
         break;
      case 48:
         FunctionBodies.fnMainap(var1);
         break;
      case 49:
         FunctionBodies.fnModulo(var1);
         break;
      case 50:
         FunctionBodies.fnHaN(var1);
         break;
      case 51:
         FunctionBodies.fnSpec_fgv4(var1);
         break;
      case 52:
         FunctionBodies.fnSpec_fgv3(var1);
         break;
      case 53:
         FunctionBodies.fnNagybetus(var1);
         break;
      case 54:
         FunctionBodies.fnPos(var1);
         break;
      case 55:
         FunctionBodies.fnSpec_fgv1(var1);
         break;
      case 56:
         FunctionBodies.fnSpec_fgv2(var1);
         break;
      case 57:
         FunctionBodies.fnSpec_fgv5(var1);
         break;
      case 58:
         FunctionBodies.fnSpec_fgv6(var1);
         break;
      case 59:
         FunctionBodies.fnSpec_fgv7(var1);
         break;
      case 60:
         FunctionBodies.fnSpec_fgv8(var1);
         break;
      case 61:
         FunctionBodies.fnSpec_fgv9(var1);
         break;
      case 62:
         FunctionBodies.fnSpec_fgv10(var1);
         break;
      case 63:
         FunctionBodies.fnKereses_matrixban(var1);
         break;
      case 64:
         FunctionBodies.fnKereses_matrixban2(var1);
         break;
      case 65:
         FunctionBodies.fnOszlop_sziget(var1);
         break;
      case 66:
         FunctionBodies.fnOszlopfolytonos(var1);
         break;
      case 67:
         FunctionBodies.fnOszlopunique(var1);
         break;
      case 68:
         FunctionBodies.fnElsoKitoltott(var1);
         break;
      case 69:
         FunctionBodies.fnUtolsoKitoltott(var1);
         break;
      case 70:
         FunctionBodies.fnDkitoltottekszama(var1);
         break;
      case 71:
         FunctionBodies.fnSor(var1);
         break;
      case 72:
         FunctionBodies.fnOszlop(var1);
         break;
      case 73:
         FunctionBodies.fnLapokszama(var1);
         break;
      case 74:
         FunctionBodies.fnUtolsoLap(var1);
         break;
      case 75:
         FunctionBodies.fnElsodlap(var1);
         break;
      case 76:
         FunctionBodies.fnFeltetelesErtek(var1);
         break;
      case 77:
         FunctionBodies.fnFeltetelesErtekadas(var1);
         break;
      case 78:
         FunctionBodies.fnImp(var1);
         break;
      case 79:
         FunctionBodies.fnString(var1);
         break;
      case 80:
         FunctionBodies.fnEkv(var1);
         break;
      case 81:
         FunctionBodies.fnEvesAdo2006(var1);
         break;
      case 82:
         FunctionBodies.fnJoOstermelo(var1);
         break;
      case 83:
         FunctionBodies.fnJoTajSzam(var1);
         break;
      case 84:
         FunctionBodies.fnJoBarkod(var1);
         break;
      case 85:
         FunctionBodies.fnDSum(var1);
         break;
      case 86:
         FunctionBodies.fnDFirst(var1);
         break;
      case 87:
         FunctionBodies.fnDPageCount(var1);
         break;
      case 88:
         FunctionBodies.fnDPageNumber(var1);
         break;
      case 89:
         FunctionBodies.fnDPrev(var1);
         break;
      case 90:
         FunctionBodies.fnDErtekAzonos(var1);
         break;
      case 91:
         FunctionBodies.fnDErtekKulonbozo(var1);
      case 92:
      case 93:
      case 94:
         break;
      case 95:
         FunctionBodies.fnGetRealZero(var1);
         break;
      case 96:
         FunctionBodies.fnIsZeroVisible(var1);
         break;
      case 97:
         ABEVFeaturedBaseFunctions.fnLess(var1);
         break;
      case 98:
         ABEVFeaturedBaseFunctions.fnGreater(var1);
         break;
      case 99:
         ABEVFeaturedBaseFunctions.fnLessOrEq(var1);
         break;
      case 100:
         ABEVFeaturedBaseFunctions.fnGreaterOrEq(var1);
         break;
      case 101:
         ABEVFeaturedBaseFunctions.fnField(var1);
         break;
      case 102:
         ABEVFeaturedBaseFunctions.fnEqual(var1);
         break;
      case 103:
         ABEVFeaturedBaseFunctions.fnNotEq(var1);
         break;
      case 104:
         ABEVFeaturedBaseFunctions.fnPower(var1);
         break;
      case 105:
         ABEVFeaturedBaseFunctions.fnDivision(var1);
         break;
      case 106:
         ABEVFeaturedBaseFunctions.fnPlus(var1);
         break;
      case 107:
         ABEVFeaturedBaseFunctions.fnMinus(var1);
         break;
      case 108:
         ABEVFeaturedBaseFunctions.fnMin(var1);
         break;
      case 109:
         ABEVFeaturedBaseFunctions.fnMax(var1);
         break;
      case 110:
         ABEVFeaturedBaseFunctions.fnRound(var1);
         break;
      case 111:
         calcNotCall = false;

         try {
            ABEVFeaturedBaseFunctions.fnGlobSum(var1);
         } catch (Exception var7) {
            Tools.eLog(var7, 1);
         }

         calcNotCall = true;
         break;
      case 112:
         FunctionBodies.fnJobTeszt(var1);
         break;
      case 113:
         FunctionBodies.fngetfidbyvid(var1);
         break;
      case 114:
         FunctionBodies.fnFieldReadOnly(var1);
         break;
      case 115:
         FunctionBodies.fnUniqueM(var1);
         break;
      case 116:
         FunctionBodies.fnCompareAM(var1);
         break;
      case 117:
         ABEVFeaturedBaseFunctions.fnIf(var1);
         break;
      case 118:
         ABEVFeaturedBaseFunctions.fnCallGUI(var1);
         break;
      case 119:
         ABEVFeaturedBaseFunctions.fnAnd(var1);
         break;
      case 120:
         ABEVFeaturedBaseFunctions.fnOr(var1);
         break;
      case 121:
         ABEVFeaturedBaseFunctions.fnXor(var1);
         break;
      case 122:
         ABEVFeaturedBaseFunctions.fnAbs(var1);
         break;
      case 123:
         EUFunctionBodies.fnEuJoAdoszam(var1);
         break;
      case 124:
         FunctionBodies.fnEvesAdo2007(var1);
         break;
      case 125:
         FunctionBodies.fnFeltetelesErtek2(var1);
         break;
      case 126:
         FunctionBodies.fnJoCsoportAzonosito(var1);
         break;
      case 127:
         FunctionBodies.fnKeresesMatrixban3(var1);
         break;
      case 128:
         FunctionBodies.fnDinLapKapcsol(var1);
         break;
      case 129:
         FunctionBodies.fnFillOnPrevious(var1);
         break;
      case 130:
         FunctionBodies.fnFillOnFollowing(var1);
         break;
      case 131:
         FunctionBodies.fnJoVPid(var1);
         break;
      case 132:
         FunctionBodies.fnJoGln(var1);
         break;
      case 133:
         FunctionBodies.fnJoRegSzam(var1);
         break;
      case 134:
         FunctionBodies.fnTiltEsTorol(var1);
         break;
      case 135:
         FunctionBodies.fnDummyVersion(var1);
         break;
      case 136:
         FunctionBodies.fnCsatolmanyDb(var1);
         break;
      case 137:
         FunctionBodies.fnUzemmod(var1);
         break;
      case 138:
         FunctionBodies.fnEredeti(var1);
         break;
      case 139:
         FunctionBodies.fnAnykParam(var1);
         break;
      case 140:
         FunctionBodies.fnDatumPluszHo(var1);
         break;
      case 141:
         FunctionBodies.fnTrim(var1);
         break;
      case 142:
         FunctionBodies.fnJoEmail(var1);
         break;
      case 143:
         FunctionBodies.fnIntervallumAtfedes(var1);
         break;
      case 144:
         FunctionBodies.fnTablazatUnique(var1);
         break;
      case 145:
         FunctionBodies.fnJoNyilvantartasiSzam(var1);
         break;
      case 146:
         FunctionBodies.fnLapUnique(var1);
         break;
      case 147:
         FunctionBodies.fnJoBIC(var1);
         break;
      case 148:
         FunctionBodies.fnJoIBAN(var1);
         break;
      case 149:
         FunctionBodies.fnJoImportHatarozat(var1);
         break;
      case 150:
         FunctionBodies.fnMatrixSzures(var1);
         break;
      case 151:
         FunctionBodies.fnFeltetelesErtekLista(var1);
         break;
      case 152:
         FunctionBodies.fnElozoErtek(var1);
         break;
      case 153:
         FunctionBodies.fnCompareStrings(var1);
         break;
      case 154:
         FunctionBodies.fnDinMaximum(var1);
         break;
      case 155:
         FunctionBodies.fnDinMinimum(var1);
         break;
      case 156:
         FunctionBodies.fnIntervallumAtfedesCsoport(var1);
         break;
      case 157:
         FunctionBodies.fnNapokSzama(var1);
         break;
      case 158:
         FunctionBodies.fnFeltetelesErtekListaGroup(var1);
         break;
      case 159:
         FunctionBodies.fnDinOldalKapcsol(var1);
         break;
      case 160:
         FunctionBodies.fnAktDinErtek(var1);
         break;
      case 161:
         ABEVFeaturedBaseFunctions.fnAdozoiErtek(var1);
         break;
      case 162:
         FunctionBodies.fnJoPtgRegisztraciosKod(var1);
         break;
      case 163:
         FunctionBodies.fnPreGenErtek(var1);
         break;
      case 164:
         FunctionBodies.fnPostGenErtek(var1);
         break;
      case 165:
         FunctionBodies.fnGenAktualisErtek(var1);
         break;
      case 166:
         FunctionBodies.fnGenUtolsoDinOldal(var1);
         break;
      case 167:
         FunctionBodies.fnGenMezoAzonosito(var1);
         break;
      case 168:
         FunctionBodies.fnFoAdat(var1);
         break;
      case 169:
         FunctionBodies.fnElozmenyErtek(var1);
         break;
      case 170:
         FunctionBodies.fnParameteresMasolas(var1);
         break;
      case 171:
         FunctionBodies.fnMezoLista(var1);
         break;
      case 172:
         FunctionBodies.fnBetoltErtek(var1);
         break;
      case 173:
         FunctionBodies.fnBetoltEredetiErtek(var1);
         break;
      case 174:
         FunctionBodies.fnErvenyesAdoszam(var1);
         break;
      case 175:
         FunctionBodies.fnSpecLancDarab(var1);
         break;
      case 176:
         FunctionBodies.fnSpecLancSum(var1);
         break;
      case 177:
         FunctionBodies.fnDinStringSum(var1);
         break;
      case 178:
         FunctionBodies.fnLogikaiNull(var1);
         break;
      default:
         var1.setResult("");
      }

      int var6 = var1.getFlag();
      var1.setFlag(passForwardedFlag(var3, var6));
      var1.setDontModify(var4 || var1.isDontModify());
   }

   private void evaluateErrorList(Vector var1, ExpClass var2) {
      if (var1 != null) {
         if (var1.size() == 0) {
            return;
         }

         this.evaluateErrorList(var1);
         this.searchErrors(var2);
         this.evaluateErrorLog(var1);
      }

   }

   private void evaluateErrorList(Vector var1) {
      if (var1.size() != 0) {
         Object[] var2 = var1.toArray();
         int var4 = 0;

         for(int var5 = var2.length - 1; var4 < var5; ++var4) {
            Object[] var3 = (Object[])((Object[])var2[var4]);
            masterErrorList.writeError(var3[0], (String)var3[1], var3.length > 2 ? (Integer)var3[2] : null, var3.length > 3 ? (Exception)var3[3] : null, var3.length > 4 ? var3[4] : null, var3.length > 5 ? var3[5] : null, var3.length > 6 ? var3[6] : null);
         }

      }
   }

   private void evaluateErrorLog(Vector var1) {
      if (var1.size() != 0) {
         Object[] var2 = var1.toArray();
         IEventLog var4 = this.getEventLog();
         int var5 = 0;

         for(int var6 = var2.length; var5 < var6; ++var5) {
            Object[] var3 = (Object[])((Object[])var2[var5]);
            var4.writeLog(var3[1] + ", " + var3[2]);
         }

      }
   }

   private void searchErrors(ExpClass var1) {
      Object[] var2 = var1.getError();
      if (var2 != null) {
         if (var2 instanceof Object[]) {
            Object[] var3 = (Object[])((Object[])var2);
            Long var4 = (Long)var3[0];
            Integer var5 = (Integer)var3[2];
            if (var5 == IErrorList.LEVEL_ERROR) {
               String var6;
               if (var4.equals(FunctionBodies.ID_FILLIN_ERROR)) {
                  var6 = getErrorMeassage(var1);
               } else {
                  var6 = "Függvény készlet hiba." + getErrorString(var1) + " [Hiba a kifejezésben: " + ExpFactoryLight.createStringExpression(var1) + "]";
               }

               // TODO: Why is uncastable
//               Object paramVar4 = var2 instanceof Exception ? (Exception)var2 : null
               Exception paramVar4 = null;
               masterErrorList.writeError(var3[0], var6, var5, paramVar4 , null, (var3.length > 5 ? var3[5] : null), (var3.length > 6 ? var3[6] : null));
            } else {
               masterErrorList.writeError(var3[0], (String)var3[1], (Integer)var3[2], (Exception)var3[3], var3.length > 4 ? var3[4] : null, var3.length > 5 ? var3[5] : null, var3.length > 6 ? var3[6] : null);
            }
         } else {
            // TODO: Fix
//            Object paramVar = var2 instanceof String ? " " + var2 : "";
            Object paramVar = " - Missing data -";
//            Object paramVar2 = var2 instanceof Exception ? (Exception)var2 : null;
            Exception paramVar2 = null;
            masterErrorList.writeError(RESOURCE_ERROR_ID, "Függvény készlet: [Hiba a kifejezésben: " + ExpFactoryLight.createStringExpression(var1) + (paramVar) + "]", paramVar2, null);
         }

         var1.setError(null);
      }

      int var7 = var1.getExpType();
      switch(var7) {
      case 3:
         int var8 = 0;

         for(int var9 = var1.getParametersCount(); var8 < var9; ++var8) {
            this.searchErrors(var1.getParameter(var8));
         }
      case 0:
      case 1:
      case 2:
      default:
      }
   }

   private static void setFormId(Object[] var0) {
      Object var1 = getParameter(var0, "form_id");
      if (var1 instanceof String) {
         FunctionBodies.setOuterFormId((String)var1);
      }

   }

   private static Object getParameter(Object[] var0, String var1) {
      for(int var2 = 0; var2 < var0.length; var2 += 2) {
         Object var3 = var0[var2];
         if (var3 instanceof String) {
            String var4 = (String)var3;
            if (var4.equalsIgnoreCase(var1)) {
               return var0[var2 + 1];
            }
         }
      }

      return null;
   }

   public static String getParam(String var0) {
      return var0.indexOf("\"") > -1 ? var0.substring(var0.indexOf("\"") + 1, var0.length() - 1) : var0;
   }

   private static String[] toStringArray(Hashtable var0) {
      if (var0.size() == 0) {
         return null;
      } else {
         String[] var1 = new String[var0.size()];
         Enumeration var2 = var0.keys();

         for(int var3 = 0; var2.hasMoreElements(); var1[var3++] = var2.nextElement().toString()) {
         }

         return var1;
      }
   }

   public boolean setStartFullcheck() {
      FunctionBodies.isFullCheck = true;
      if (FunctionBodies.g_cached_items.getMode() != 1 && FunctionBodies.g_active_form_id.equals(FunctionBodies.gui_info.main_document_id)) {
         FunctionBodies.g_cached_items.exec(FunctionBodies.gui_info);
      }

      return true;
   }

   public boolean setStopFullcheck() {
      if (FunctionBodies.g_cached_items.getMode() == 1) {
         FunctionBodies.g_cached_items.exec();
      }

      FunctionBodies.isFullCheck = false;
      return true;
   }

   public boolean setStartFullcalc() {
      FunctionBodies.isFullCalc = true;
      if (FunctionBodies.g_cached_items.getMode() != 1 && FunctionBodies.g_active_form_id.equals(FunctionBodies.gui_info.main_document_id)) {
         FunctionBodies.g_cached_items.exec(FunctionBodies.gui_info);
      }

      return true;
   }

   public boolean isStartFullcheck() {
      return FunctionBodies.isFullCheck;
   }

   public boolean isStartFullcalc() {
      return FunctionBodies.isFullCalc;
   }

   public boolean setStopFullcalc() {
      if (FunctionBodies.g_cached_items.getMode() == 1) {
         FunctionBodies.g_cached_items.exec();
      }

      FunctionBodies.isFullCalc = false;
      return true;
   }

   public boolean setMultiStartCalc() {
      FunctionBodies.g_cached_items.setProcessMode(1);
      FunctionBodies.initCompareAM();
      return true;
   }

   public boolean setMultiStopCalc() {
      FunctionBodies.g_cached_items.setProcessMode(0);
      FunctionBodies.initCompareAM();
      return true;
   }

   public boolean setMultiStartCheck() {
      FunctionBodies.g_cached_items.setProcessMode(1);
      FunctionBodies.initCompareAM();
      return true;
   }

   public boolean setMultiStopCheck() {
      FunctionBodies.g_cached_items.setProcessMode(0);
      FunctionBodies.initCompareAM();
      return true;
   }

   public boolean setFieldTypes(Hashtable var1) {
      Hashtable var2 = (Hashtable)MetaInfo.getInstance().getFieldMetas(FunctionBodies.g_active_form_id);
      Hashtable var3 = new Hashtable(var2.size());
      Enumeration var4 = var2.keys();

      while(var4.hasMoreElements()) {
         String var5 = (String)var4.nextElement();
         Hashtable var6 = (Hashtable)var2.get(var5);
         Object var7 = var6.get("type");
         if (var7 != null) {
            String var8 = var7.toString().trim();
            if (var8.length() != 0) {
               var3.put(var5, Integer.valueOf(var8));
            }
         }
      }

      FunctionBodies.g_field_types.put(FunctionBodies.g_active_form_id, var3);
      return true;
   }

   public boolean setFunctionDescriptions(IPropertyList var1, String var2) {
      FunctionBodies.g_function_description.put(var2, var1);
      return true;
   }

   public boolean setVariables(IPropertyList var1, String var2) {
      FunctionBodies.g_variables.put(var2, var1);
      return true;
   }

   public boolean setReadonlyFieldCalcState(Boolean var1, Integer var2) {
      FunctionBodies.g_readonly_calc_state = var1;
      FunctionBodies.g_readonly_calc_act_page_number = var2;
      return true;
   }

   public boolean setFormId(String var1) {
      if (calcNotCall) {
         FunctionBodies.g_active_form_id = var1;
      }

      return true;
   }

   public boolean setDataStore(IDataStore var1) {
      if (calcNotCall) {
         FunctionBodies.g_active_data_store = var1;
      }

      return true;
   }

   public Object getDependency(Object[] var1) {
      try {
         FunctionBodies.setActiveFormActivity(false);
         setFormId(var1);
         FunctionBodies.initFormid();
         if (var1.length > 1) {
            Object var2 = var1[1];
            if (var2 instanceof String[]) {
               String[] var3 = (String[])((String[])var2);
               String var4 = var3[0];
               String var5 = getParam(var3[1]);
               Hashtable var6 = null;
               if (!isMemberOfDependency(var4)) {
                  return null;
               }

               if ("field".equalsIgnoreCase(var4)) {
                  FunctionBodies.setActiveFormActivity(true);
                  return new String[]{var5};
               }

               if ("adózói_érték".equalsIgnoreCase(var4)) {
                  FunctionBodies.setActiveFormActivity(true);
                  return new String[]{var5};
               }

               if ("akt_din_érték".equalsIgnoreCase(var4)) {
                  FunctionBodies.setActiveFormActivity(true);
                  return new String[]{var5};
               }

               if ("feltételes_érték".equalsIgnoreCase(var4)) {
                  FunctionBodies.setFeltetelesErtekFieldsList((String)var1[7], (String)var1[9]);
                  FunctionBodies.setActiveFormActivity(true);
                  return null;
               }

               if ("feltételes_érték2".equalsIgnoreCase(var4)) {
                  FunctionBodies.setFeltetelesErtekFieldsList((String)var1[7], (String)var1[9]);
                  FunctionBodies.setActiveFormActivity(true);
                  return null;
               }

               if ("getRealZero".equalsIgnoreCase(var4)) {
                  FunctionBodies.setActiveFormActivity(true);
                  return null;
               }

               if ("első_kitöltött".equalsIgnoreCase(var4)) {
                  var6 = FunctionBodies.getDeptFieldsBySpec(var5, var1);
               } else if ("utolsó_kitöltött".equalsIgnoreCase(var4)) {
                  var6 = FunctionBodies.getDeptFieldsBySpec(var5, var1);
               } else if ("d_kitöltöttekszáma".equalsIgnoreCase(var4)) {
                  var6 = FunctionBodies.getDeptFieldsBySpec(var5, var1);
               } else if ("első_dlap".equalsIgnoreCase(var4)) {
                  var5 = getParam(var3[3]);
                  var6 = FunctionBodies.getDepFidByVid(var5, var1);
               } else if ("DFirst".equalsIgnoreCase(var4)) {
                  var6 = FunctionBodies.getDepFidByVid(var5, var1);
               } else {
                  if ("DPrev".equalsIgnoreCase(var4)) {
                     this.specialDependencies = FunctionBodies.collectSpecialDependencies("dept_type_dinpages", var5, getParameter(var1, "target_id"), this.specialDependencies);
                     FunctionBodies.setActiveFormActivity(true);
                     return null;
                  }

                  if ("DSum".equalsIgnoreCase(var4)) {
                     var6 = FunctionBodies.getDepFidByVid(var5, var1);
                  } else if ("din_string_sum".equalsIgnoreCase(var4)) {
                     var6 = FunctionBodies.getDepFidByVid(var5, var1);
                  } else if ("DPageCount".equalsIgnoreCase(var4)) {
                     var6 = FunctionBodies.getDepFidByFormVid(var5, var1, "@", "_");
                  } else if ("DPageNumber".equalsIgnoreCase(var4)) {
                     var6 = FunctionBodies.getDepFidByFormVid(var5, var1, "@", "_");
                  } else {
                     if ("globsum".equalsIgnoreCase(var4)) {
                        FunctionBodies.g_cached_items.add("globsum", (Object[])((Object[])var1[1]), (String)var1[7], (String)var1[9]);
                        FunctionBodies.setActiveFormActivity(true);
                        return null;
                     }

                     if ("unique_m".equalsIgnoreCase(var4)) {
                        FunctionBodies.g_cached_items.add("unique_m", (Object[])((Object[])var1[1]), (String)var1[7], (String)var1[9]);
                        FunctionBodies.setActiveFormActivity(true);
                        return null;
                     }

                     if ("spec_fgv5".equalsIgnoreCase(var4)) {
                        var6 = FunctionBodies.getDepSpec5fgv(getParam(var3[2]), var1);
                     } else if ("d_érték_azonos".equalsIgnoreCase(var4)) {
                        var6 = FunctionBodies.getDepFidByVid(var5, var1);
                     } else if ("d_érték_különböző".equalsIgnoreCase(var4)) {
                        var6 = FunctionBodies.getDepFidByVid(var5, var1);
                     } else if ("nincsátfedés".equalsIgnoreCase(var4)) {
                        var6 = FunctionBodies.getDeptFieldsBySpecIntAtfed(var3, var1);
                     } else if ("nincsátfedés2".equalsIgnoreCase(var4)) {
                        var6 = FunctionBodies.getDeptFieldsBySpecIntAtfedCsoport(var3, var1);
                     } else if ("napok_száma".equalsIgnoreCase(var4)) {
                        var6 = FunctionBodies.getDeptFieldsBySpecNapokSzama(var3, var1);
                     } else if ("táblázatunique".equalsIgnoreCase(var4)) {
                        var6 = FunctionBodies.getDeptFieldsBySpecTableUnique(var3, var1);
                     } else if ("din_max".equalsIgnoreCase(var4)) {
                        var6 = FunctionBodies.getDeptFields(var3, var1);
                     } else if ("din_min".equalsIgnoreCase(var4)) {
                        var6 = FunctionBodies.getDeptFields(var3, var1);
                     } else {
                        if ("feltételes_értékadás".equalsIgnoreCase(var4)) {
                           FunctionBodies.setReadOnlyCalcFields((String)var1[7], (String)var1[9]);
                           FunctionBodies.setActiveFormActivity(true);
                           return null;
                        }

                        if ("paraméteres_másolás".equalsIgnoreCase(var4)) {
                           FunctionBodies.setReadOnlyCalcFields((String)var1[7], (String)var1[9]);
                           FunctionBodies.setActiveFormActivity(true);
                           return null;
                        }

                        if ("mező_lista".equalsIgnoreCase(var4)) {
                           FunctionBodies.setActiveFormActivity(true);
                           return null;
                        }

                        if ("főadat".equalsIgnoreCase(var4)) {
                           FunctionBodies.setFoAdatDependency(getParam(var3[1]), getParam(var3[2]), (String)var1[9]);
                           FunctionBodies.setActiveFormActivity(true);
                           return null;
                        }

                        if ("spec_lánc_darab".equalsIgnoreCase(var4)) {
                           FunctionBodies.setActiveFormActivity(true);
                           var6 = FunctionBodies.getAllInputFields(var3, var1);
                        } else {
                           if ("spec_lánc_sum".equalsIgnoreCase(var4)) {
                              FunctionBodies.setActiveFormActivity(true);
                              return new String[]{var5};
                           }

                           if ("logikai_null".equalsIgnoreCase(var4)) {
                              FunctionBodies.setActiveFormActivity(true);
                              var6 = FunctionBodies.getDepFidByVid(var5, var1);
                           }
                        }
                     }
                  }
               }

               FunctionBodies.setActiveFormActivity(true);
               if (var6 != null && var6.size() != 0) {
                  return toStringArray(var6);
               }

               return null;
            }
         }

         return null;
      } catch (Exception var7) {
         var7.printStackTrace();
         FunctionBodies.setActiveFormActivity(true);
         return null;
      }
   }

   public Object setCheckDept(Object[] var1) {
      try {
         FunctionBodies.setActiveFormActivity(false);
         setFormId(var1);
         FunctionBodies.initFormid();
         if (var1.length > 1) {
            Object var2 = var1[1];
            if (var2 instanceof String[]) {
               String[] var3 = (String[])((String[])var2);
               String var4 = var3[0];
               if (!isMemberOfCheck(var4)) {
                  FunctionBodies.setActiveFormActivity(true);
                  return null;
               }

               if ("globsum".equalsIgnoreCase(var4)) {
                  FunctionBodies.g_cached_items.add("globsum", (Object[])((Object[])var1[1]), (String)var1[7], (String)var1[9]);
               } else if ("unique_m".equalsIgnoreCase(var4)) {
                  FunctionBodies.g_cached_items.add("unique_m", (Object[])((Object[])var1[1]), (String)var1[7], (String)var1[9]);
               }

               FunctionBodies.setActiveFormActivity(true);
               return null;
            }
         }

         return null;
      } catch (Exception var5) {
         var5.printStackTrace();
         FunctionBodies.setActiveFormActivity(true);
         return null;
      }
   }

   public Object[] getFunctionList() {
      return FUNCTION_NAMES;
   }

   public Object getFunctionDescriptors(String var1) {
      return FUNCTION_DESCRIPTORS.get(var1.toLowerCase());
   }

   public Object calcExpression(Object var1, Object var2, ExpClass var3, Object[] var4, String var5, IDataStore var6) {
      FunctionBodies.g_calc_record = var1;
      FunctionBodies.g_current_field_id = var4;
      FunctionBodies.g_active_form_id = var5;
      FunctionBodies.g_active_data_store = var6;
      FunctionBodies.g_all_fileds_empty = true;
      FunctionBodies.g_in_variable_exp = false;
      FunctionBodies.g_extended_error = "";
      FunctionBodies.g_exp_fields.release();
      Object var7 = expwrapper.get("", var3);
      FunctionBodies.g_calc_record = null;
      return var7;
   }

   public Object calcVariable(ExpClass var1, String var2, IDataStore var3) {
      FunctionBodies.g_calc_record = null;
      FunctionBodies.g_current_field_id = null;
      FunctionBodies.g_active_form_id = var2;
      FunctionBodies.g_active_data_store = var3;
      FunctionBodies.g_all_fileds_empty = true;
      FunctionBodies.g_in_variable_exp = false;
      FunctionBodies.g_extended_error = "";
      FunctionBodies.g_exp_fields.release();
      Object var4 = expwrapper.get("", var1);
      FunctionBodies.g_calc_record = null;
      return var4;
   }

   private static int passForwardedFlag(int var0, int var1) {
      try {
         if (var0 == -2 || var1 == -2) {
            return -2;
         }

         if (var0 == -3 || var1 == -3) {
            return -3;
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return 0;
   }

   private static int getFlags(ExpClass var0) {
      int var1 = var0.getParametersCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         ExpClass var3 = var0.getParameter(var2);
         int var5;
         switch(var3.getExpType()) {
         case 0:
         case 1:
            break;
         case 2:
            ExpClass var4 = getExpression(var3);
            if (var4 != null) {
               var5 = var4.getFlag();
               if (var5 != 0) {
                  return var5;
               }
            }
            break;
         default:
            var5 = var3.getFlag();
            if (var5 != 0) {
               return var5;
            }
         }
      }

      return 0;
   }

   private static boolean getDontModifies(ExpClass var0) {
      int var1 = var0.getParametersCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         ExpClass var3 = var0.getParameter(var2);
         boolean var5;
         switch(var3.getExpType()) {
         case 0:
         case 1:
            break;
         case 2:
            ExpClass var4 = getExpression(var3);
            if (var4 != null) {
               var5 = var4.isDontModify();
               if (var5) {
                  return var5;
               }
            }
            break;
         default:
            var5 = var3.isDontModify();
            if (var5) {
               return var5;
            }
         }
      }

      return false;
   }

   private static ExpClass getExpression(ExpClass var0) {
      String var1 = var0.getIdentifier();
      if (var1 == null) {
         return null;
      } else {
         Object var2 = var0.getSource();
         if (var2 instanceof IPropertyList) {
            IPropertyList var3 = (IPropertyList)var2;
            Object var4 = var3.get(var1);
            if (var4 instanceof ExpClass) {
               return (ExpClass)var4;
            }
         }

         return null;
      }
   }

   private static String getErrorString(ExpClass var0) {
      if (var0.getError() == null) {
         return null;
      } else {
         Object[] var1 = var0.getError();
         StringBuffer var2 = new StringBuffer();
         var2.append(" (");
         var2.append(var1[0]);
         var2.append(") ");
         var2.append(var1[1]);
         return var2.toString();
      }
   }

   private static String getErrorMeassage(ExpClass var0) {
      if (var0.getError() == null) {
         return null;
      } else {
         Object[] var1 = var0.getError();
         return var1[1].toString();
      }
   }

   public static String getExpString(ExpClass var0) {
      StringBuffer var1 = new StringBuffer();
      if (var0.getExpType() == 3) {
         var1.append(var0.getIdentifier());
         var1.append(" ");

         for(int var2 = 0; var2 < var0.getParametersCount(); ++var2) {
            var1.append(getExpString(var0.getParameter(var2)));
            var1.append(" ");
         }
      } else {
         var1.append(var0.getResult());
         var1.append(" ");
      }

      return var1.toString();
   }

   public Hashtable getSpecialDependencies(String var1) {
      return this.specialDependencies == null ? null : (Hashtable)this.specialDependencies.get(var1);
   }

   public void setFormCheck(boolean var1) {
      FIdHelper.setFormCheck(var1);
      FunctionBodies.g_exp_fields.setCollect(var1);
   }

   public boolean isToleranced(String var1, String var2, String var3, String var4) {
      return FunctionBodies.isToleranced(var1, var2, var3, var4);
   }

   public boolean isCachedTargetId(String var1, String var2, String var3) {
      return FunctionBodies.g_cached_items.isCachedTargetId(var1, var2, var3);
   }

   public String getExtendedError() {
      return FunctionBodies.g_extended_error;
   }

   public String getRoundedValue(String var1, Object var2) {
      return NumericOperations.getRoundedValue(var1, var2);
   }

   public Iterator getExpressionFieldsList() {
      return FunctionBodies.g_exp_fields.iterator();
   }

   public void setPreviousItem(StoreItem var1) {
      FunctionBodies.g_previous_item = var1;
   }

   public Vector getReadOnlyCalcFieldsList(String var1, String var2) {
      return FunctionBodies.getReadOnlyCalcFields(var1, var2);
   }

   public int getPrecisionForKihatas(String var1) {
      return FunctionBodies.getPrecisionForKihatas(var1);
   }

   public HashSet getFeltetelesErtekFieldsList(String var1) {
      return FunctionBodies.getFeltetelesErtekFieldsList(var1);
   }

   public List<String> getGenErtekChangedFieldValue() {
      return FunctionBodies.getGenErtekChangedFieldValue();
   }

   public void resetGenErtekChangedFieldValue() {
      FunctionBodies.resetGenErtekChangedFieldValue();
   }

   public boolean isFoAdatDependency(String var1, String var2) {
      return FunctionBodies.isFoAdatDependency(var1, var2);
   }

   public Set<String> getSubFormFoAdatDependency(String var1, String var2) {
      return FunctionBodies.getSubFormFoAdatDependency(var1, var2);
   }

   public boolean isInJavkeretOpMode() {
      return FunctionBodies.isInJavkeretOpMode();
   }

   static {
      FUNCTION_DESCRIPTORS = new Hashtable(FUNCTION_NAMES.length);
      FUNCTION_QUICK_REF = new Hashtable(FUNCTION_NAMES.length);
      RESOURCE_ERROR_ID = new Long(12000L);
   }
}
