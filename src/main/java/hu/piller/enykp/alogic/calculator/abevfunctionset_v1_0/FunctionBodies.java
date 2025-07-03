package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0;

import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.cachedExp.CachedItems;
import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.cachedExp.ICachedItem;
import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.interval.DefaultIntervalHandler;
import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.interval.IInterval;
import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.interval.IIntervalHandler;
import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.interval.intervalImpl.DateIntervalImpl;
import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.interval.intervalImpl.DummyIntervalImpl;
import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.interval.intervalImpl.NumIntervalImpl;
import hu.piller.enykp.alogic.calculator.calculator_c.CalcHelper;
import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.calculator.calculator_c.ExpClass;
import hu.piller.enykp.alogic.calculator.calculator_c.GoToButton;
import hu.piller.enykp.alogic.calculator.calculator_c.LookupListModel;
import hu.piller.enykp.alogic.calculator.calculator_c.MatrixSearchItem;
import hu.piller.enykp.alogic.calculator.calculator_c.MatrixSearchModel;
import hu.piller.enykp.alogic.calculator.matrices.IMatrixHandler;
import hu.piller.enykp.alogic.calculator.matrices.defaultMatrixHandler;
import hu.piller.enykp.alogic.filepanels.attachement.AttachementTool;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.orghandler.OrgHandler;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.gui.component.HunCharComparator;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.framework.MainPanel;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.interfaces.ICalculator;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.validation.ValidationUtilityAPEH;
import hu.piller.enykp.util.validation.ValidationUtilityVPOP;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.DefaultListModel;

public class FunctionBodies {
   protected static IMatrixHandler matrixHandler = defaultMatrixHandler.getInstance();
   public static boolean test = false;
   private static Hashtable staticData;
   public static String stat_form_id = null;
   private static Object[] g_ds_key = new Object[]{null, null};
   public static Vector calc_error_list = new Vector();
   public static BookModel gui_info = null;
   public static MetaInfo meta_info = null;
   public static ICalculator ic = null;
   public static boolean activeFormActivity = true;
   public static String outerFormId;
   public static int zeroCheckRecursio;
   private static Vector spVector = null;
   public static final String HASZNALATI_MOD_GENERALAS = "bevallás_generálás";
   public static final String HASZNALATI_MOD_WEB = "webes_kitöltés";
   public static final String HASZNALATI_MOD_JAVKERET = "javítókeret";
   public static final String HASZNALATI_MOD_BATCH_ELLENORZO = "batch_ellenőrző";
   public static final String SZERKESZTES_IGEN = "I";
   public static final String SZERKESZTES_NEM = "N";
   public static final Integer IDX_MESSAGE = new Integer(0);
   public static final Integer IDX_WARNING = new Integer(1);
   public static final Integer IDX_ERROR = new Integer(2);
   public static final String EX_TYPE_MISMATCH = "Program hiba, típus eltérés!";
   public static final Long ID_EX_TYPE_MISMATCH = new Long(12100L);
   private static final String EX_PAR_UZEMMOD = "Program hiba az üzemmód meghatározásánál!";
   private static final Long ID_EX_PAR_UZEMMOD = new Long(12110L);
   private static final String EX_EREDETI = "Program hiba az eredeti() függvény kezelésénél";
   private static final Long ID_EX_EREDETI = new Long(12111L);
   private static final String EX_PAR_CNT_MISMATCH = "Program hiba, a paraméterek száma vagy típusa nem megfelelő!";
   private static final Long ID_EX_PAR_CNT_MISMATCH = new Long(12101L);
   private static final String EX_FN_FIELD = "Program hiba, hiba történt a mező adatok megszerzésekor!";
   private static final Long ID_EX_FN_FIELD = new Long(12106L);
   private static final String EX_FN_PARAM_BAD_RELATION = "Hibás reláció";
   private static final String EX_FN_PARAM_COL_ROWCOUNT = "Az oszlopok számossága eltér";
   private static final String EX_FN_PARAM_DIFF_PAGE = "Az oszlopok nem mind ugyanazon a lapon találhatók";
   private static final String EX_FN_INTERVALLUM_SZURES = "Hiba az intervallum szűrés során";
   private static final String EX_FN_INTERVALLUM_CHECK = "Az intervallumok között átfedés van.";
   private static final String EX_FN_INTERVALLUM_CHECK_NEXT = "Átfedés van a következő üzenetben hivatkozott időszakkal.";
   private static final String EX_FN_INTERVALLUM_CHECK_PRE = "Átfedés van az előző üzenetben hivatkozott időszakkal.";
   private static final String EX_FN_INTERVALLUM_DISTANCE_CHECK_NEXT = "Legalább %s napnak el kell telnie a következő üzenetben hivatkozott időszakig.";
   private static final String EX_FN_INTERVALLUM_DISTANCE_CHECK_PRE = "Legalább %s napnak el kell telnie az előző üzenetben hivatkozott időszaktól.";
   private static final String EX_FN_INTERVALLUM_LENGTH_CHECK = "Az időszak hosszabb mint a megengedett %s nap.";
   private static final String EX_FN_INTERVALLUM_CONNECTED_LENGTH_MSG = "Az időszak összevonásra került az alábbi időszakokkal.";
   private static final String EX_FN_INTERVALLUM_CONNECTED_LENGTH_CHECK = "Az összevont időszak hosszabb mint a megengedett %s nap.";
   private static final String EX_FN_INTERVALLUM_DATE_TYPE_ERROR = "Nem dátum típusú az adat.";
   private static final Long ID_EX_FN_INTERVALLUM_SZURES = new Long(12112L);
   private static final String EX_FN_TABLAZAT_SOR_SAME = "Ismétlődő sorok:";
   private static final String EX_FN_TABLAZAT_SOR_DIFF = "Különböző tartalmú sorok:";
   private static final String EX_FN_TABLAZAT_UNIQUE = "Táblázat sorainak a vizsgálata";
   private static final Long ID_EX_FN_TABLAZAT_UNIQUE = new Long(12114L);
   private static final String EX_FN_PAGE_SAME = "Azonos tartalmú oldalak!";
   private static final String EX_FN_PAGE_UNIQUE = "Dinamikus lapok oldalainak egyediség vizsgálata";
   private static final Long ID_EX_FN_PAGE_UNIQUE = new Long(12115L);
   public static final String EX_FN_FIELD_NOT_ON_PAGE = "A mezőazonosító nem a laphoz tartozik!";
   private static final String EX_FN_PAGE_DYN = "Program hiba, nem dinamikus a lap!";
   private static final Long ID_EX_FN_PAGE_DYN = new Long(12108L);
   private static final String EX_FN_PAGE_DYN_COUNT = "Program hiba, dinamikus lapszám meghatározásánál!";
   private static final Long ID_EX_FN_PAGE_DYN_COUNT = new Long(12111L);
   private static final String EX_FN_FIELD_ID = "Program hiba, hiba történt a mező azonosító meghatározásakor!";
   private static final Long ID_EX_FN_FIELD_ID = new Long(12109L);
   private static final String EX_FN_DIN_OLDAL = "Sablon hiba! Csak az aktuális laphoz tartalmazó mezőket tartalmazhat a kifejezés.";
   private static final Long ID_EX_FN_DIN_OLDAL = new Long(12124L);
   private static final Long ID_WARNING = new Long(12150L);
   public static final String MSG_DIFF_VALUES = "[Nyomtatvány] Eltér a mező értéke a számított értéktől! ";
   public static final Long ID_FILLIN_ERROR;
   public static final int COL_MIN = 64;
   public static final int COL_PERIOD = 26;
   public static final String ROW_COL_DELIMITER = ";";
   public static final String ROW_COL_INT_STR = "..";
   public static final String REG_ONE_DIM = "\\[.*\\]_.*";
   public static final String REG_TWO_DIM = "\\[.*\\]_.*\\[.*\\]";
   public static final String REG_GOOD_EMIAL = "([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})";
   private static final String CONST_NULL = "##null##";
   private static final String DUMMY_DELIMITER = "##";
   private static final String PREFIX_DELIMITER = "#";
   private static final String POSTFIX_DELIMITER = "=";
   public static final String MULTI_DELIMITER = "\n";
   private static String DECIMAL_FORMAT_MASK;
   public static final String ERR_FORMAT_MASK = "Lap, oszlop, sor formátum maszk hiba! ";
   public static final String ERR_COLUMN_DEF = "Oszlop meghatározási hiba! ";
   public static final String ERR_INTERVALLUM_CONV = "Intervallum konverziós hiba! ";
   public static final String ERR_NOT_CONTINOUS = "Hibás sor, oszlop sorrend! ";
   public static final String ERR_MISSING_PAGE_ROW_COL = "Hiányzó lap, sor, oszlop azonosító! ";
   public static final String ERR_GETTING_FIELD_ID = "Hiba a belső mezőazonosító meghatározása során! (lap/oszlop/sor)";
   public static final Long ERR_FN_ID;
   public static final String ERR_FN = "Számítási hiba! ";
   public static final String ERR_MATRIX_SEARCH_LESS_COL = "Az eredmény oszlop nem létezik! ";
   public static final String ERR_BAD_OR_MISSING_PARAM = "Hibás vagy hiányzó paraméter! ";
   public static final String ERR_MISSING_CALCULATOR_PARAM = "Hiányzik a kalkulátor referencia! ";
   public static final Long ID_ERR_MISSING_CALCULATOR_PARAM;
   public static final Long ID_ERR_MISSING_GUI_PARAM;
   public static final String ERR_MISSING_GUI_PARAM = "Hiányzik a GUI referencia! ";
   public static final Long ID_ERR_GUI_INFO;
   public static final String ERR_GUI_INFO = "Hiba GUI info szolgáltatások lekérdezése során! ";
   public static final Long ERR_ID_FN_DIFF_COL_COUNT;
   public static final String ERR_FN_DIFF_COL_COUNT = "Az oszlopok elemszáma különbözik! ";
   public static final Long ID_ERR_GUI_FIELD_SET;
   public static final String ERR_GUI_FIELD_SET = "Hiba a mező readonly tulajdonságának állítása közben ";
   public static final Long ID_ERR_MATRIX_SEARCH_OC;
   public static final String ERR_MATRIX_SEARCH_OC = "Mátrix keresési hiba! Output oszlop nem létezik! ";
   public static final String ERR_MATRIX_SEARCH = "Mátrix keresési hiba! ";
   public static final Long ID_ERR_CHECK_UNIQUEM;
   public static final String ERR_CHECK_UNIQUEM = "[Nyomtatvány] Nem egyedi a mező tartalma! ";
   public static final String CHECK_UNIQUEM_INFO = "Ismétlődő értékek:";
   public static final Long ID_ERR_CHECK_COMPAREAM;
   public static final String ERR_CHECK_COMPAREAM = "Eltér a mező tartalma! ";
   public static final Long ID_ERR_CHECK_COMP;
   public static final String ERR_CHECK_COMP = "Összehasonlítási hiba! ";
   public static final String ERR_CHECK_COMPAM = "Fő/részbizonylat összehasonlítási hiba! ";
   public static final Long ID_ERR_EU_MEMBERSHIP;
   public static final String ERR_EU_MEMBERSHIP = "Nem megfelelő tagállam";
   public static final String ERR_FILTER = "Intervallum szűrés hiba! ";
   public static final Long ID_ERR_GET_MULTI_VALUE;
   public static final String ERR_GET_MULTI_VALUE = "Hiba másik nyomtatvány adatának az elkérésekor! ";
   public static final Long ID_ERR_ELOZO_ERTEK_PAR;
   public static final String ERR_ELOZO_ERTEK_PAR = "Mezőhöz kötött képletben használható az előző_érték függvény!";
   public static final String CMD_GETFIDBYROWCOL = "get_fid_by_row_col";
   public static final String CMD_GETROWCOLBYFID = "get_row_col_by_fid";
   public static final String CMD_GET_FIDS = "get_ids";
   public static final int IDX_FID = 0;
   public static final int IDX_VID = 1;
   public static final String CMD_GET_PAGE_INFOS = "get_ipage_infos";
   public static final String CMD_GET_FIELD_METAS = "get_field_metas";
   public static final String CMD_GET_FIELD_GUIS = "get_field_datas";
   private static Object[] ids_info;
   private static Object[] expRequest;
   public static final String keyStatMeta = "meta";
   public static final String keyStatGui = "gui";
   public static final String ATTR_TYPE = "type";
   public static final String ATTR_CALCZE = "calc_ze";
   public static final String ATTR_ROUND = "round";
   public static final int ATTR_ROUND_DOESNT_EXISTS = -1;
   public static final String ATTR_ROUND_DOESNT_EXISTS_MSG = "A kerekítési érték nem létezik";
   public static final int ATTR_ROUND_NOT_NUMBER = -2;
   public static final String ATTR_SETTINGS_GUI_CALC_ENABLE = "mezőszámítás";
   private static Object[] par_field_do_calculation;
   private static int[] specfgv1_searchIndex;
   private static Object[] specfgv1_searchValues;
   private static int[] specfgv2_searchIndex;
   private static Object[] specfgv2_searchValues;
   private static Object[] getfieldmetas;
   private static Object[] getfieldguis;
   public static final String NULL_GROUP_ID = "null_group_id";
   public static final String NO_GROUP_ID = "no_group_id";
   public static final String CMD_GET_FIELD_TYPE = "get_field_type";
   public static final Object[] get_field_type;
   public static final char YES = '1';
   public static final char NO = '0';
   public static final String STR_EMPTY = "";
   public static final String STR_ZERO = "0";
   public static final String PRE_AND = "&";
   public static final String VAR_PREFIX = "$";
   public static final String VAR_DEL = "_";
   public static final String VAR_DEL2 = "@";
   public static final String VAR_PRE_SUM = "sum";
   public static final String VAR_PRE_LAP = "lap";
   public static final String VAR_PRE_OSZ = "osz";
   public static final String VAR_PRE_PREV = "prev";
   public static final String VAR_PRE_FIRST = "first";
   public static final String JO_ADOSZAM_KIZART_PREFIX = "08";
   public static final String JO_ADOSZAM_AFAKOD = "12349";
   public static final String JO_CSOPORT_AZONOSITO_KIZART_PREFIX = "08";
   public static final String JO_CSOPORT_AZONOSITO_AFAKOD = "5";
   public static final String ERVENYES_ADOSZAM_KIZART_PREFIX = "08";
   public static final String ERVENYES_ADOSZAM_AFAKOD = "1234569";
   public static final int CONST_MAX_LIST_INDEX_NUMBER = 10;
   public static final String KEY_PRECISION = "precision";
   public static final int ERROR_VALUE = -1;
   public static Calendar calendar;
   public static SimpleDateFormat formatter;
   public static String g_active_form_id;
   public static Hashtable g_function_description;
   public static Hashtable g_variables;
   public static boolean g_all_fileds_empty;
   public static boolean g_in_variable_exp;
   public static String g_extended_error;
   public static Object g_calc_record;
   public static Object[] g_current_field_id;
   public static StoreItem g_previous_item;
   public static IDataStore g_active_data_store;
   public static Boolean g_readonly_calc_state;
   public static Integer g_readonly_calc_act_page_number;
   public static Hashtable g_field_types;
   public static Hashtable g_designed_page_names;
   public static Hashtable g_real_page_names;
   public static Hashtable g_real_vid_names;
   public static Hashtable g_designed_vid_names;
   public static Hashtable g_table_fids;
   public static Hashtable<String, Vector<String>> g_readOnlyCalcFields;
   public static Hashtable<String, HashSet<String>> g_feltetelesErtekFields;
   public static Hashtable<String, Map<String, Set<String>>> g_page_fids;
   public static Hashtable<String, Map<String, Set<String>>> g_visible_page_fids;
   public static Hashtable g_vids;
   public static Hashtable g_rows;
   public static Hashtable g_compaream;
   public static FunctionBodies.ExpressionFields g_exp_fields;
   public static int g_compaream_source;
   public static String g_saved_active_form_id;
   public static IDataStore g_saved_active_data_store;
   public static CachedItems g_cached_items;
   public static boolean isFullCheck;
   public static boolean isFullCalc;
   public static Hashtable g_din_expressions;
   public static List<String> g_gen_ertek_changed_field_value;
   public static Map<String, HashMap<String, HashSet<String>>> g_fo_adat_dependency;
   public static final String REPLACED_STR = "\\_\\@\\[\\]\\+\\-\\*\\/\\(\\)\\ ";

   public static IDataStore getActiveDataStore() {
      return g_active_data_store;
   }

   public static void saveActiveParameters() {
      g_saved_active_form_id = g_active_form_id;
      g_saved_active_data_store = g_active_data_store;
   }

   public static void restoreActiveParameters() {
      g_active_form_id = g_saved_active_form_id;
      g_active_data_store = g_saved_active_data_store;
      g_saved_active_form_id = null;
      g_saved_active_data_store = null;
   }

   public static void setActiveParameters(String var0, IDataStore var1) {
      g_active_form_id = var0;
      g_active_data_store = var1;
   }

   public static void release() {
      ids_info = new Object[]{null, null};
      specfgv1_searchIndex = new int[]{0, 0};
      specfgv1_searchValues = new Object[]{null, null, null};
      specfgv2_searchIndex = new int[]{0, 0, 0};
      specfgv2_searchValues = new Object[]{null, null, null};
   }

   public static void setInfoObject(Object var0) {
      gui_info = (BookModel)var0;
   }

   public static void setMetaInfo(Object var0) {
      meta_info = (MetaInfo)var0;
   }

   public static BookModel getInfoObject() {
      return gui_info;
   }

   public static ICalculator getICObj() {
      return ic;
   }

   public static void setIc(Object var0) {
      ic = (ICalculator)var0;
   }

   public static void setActiveFormActivity(boolean var0) {
      activeFormActivity = var0;
   }

   public static void writeError(ExpClass var0, Long var1, String var2, Exception var3, Object var4, boolean var5) {
      String var6 = "";
      String var7 = var2;
      if (var5) {
         if (g_current_field_id != null) {
            var6 = Calculator.extendedInfo(FIdHelper.getFieldId(g_current_field_id), getDinLapSzam());
         }

         var7 = var2 + (var4 != null ? " " + var4.toString() + " " : " ") + var6;
      }

      writeCalcMsg(var0, var1, var7, var3, var4, IErrorList.LEVEL_ERROR);
   }

   public static void writeError(ExpClass var0, Long var1, String var2, Exception var3, Object var4) {
      writeError(var0, var1, var2, var3, var4, true);
   }

   public static void writeWarning(ExpClass var0, Long var1, String var2, Exception var3, Object var4) {
      writeCalcMsg(var0, var1, var2, var3, var4, IErrorList.LEVEL_WARNING);
   }

   private static Integer getDinLapSzam() {
      try {
         return FIdHelper.isFieldDynamic(g_current_field_id) ? FIdHelper.getDPageNumber(getSelf()) : null;
      } catch (Exception var1) {
         return null;
      }
   }

   public static void writeCalcMsg(ExpClass var0, Long var1, String var2, Exception var3, Object var4, Integer var5) {
      try {
         String var6 = getCurrentCalcErrorCode();
         if (var1.equals(ID_EX_TYPE_MISMATCH) || var1.equals(ERR_FN_ID)) {
            System.out.println("belsoHibakod = " + var6);
            var6 = "m002";
         }

         String var7 = "FunctionBodies.writeError: " + ABEVFunctionSet.ID_DEFAULT_FUNCTION_ERROR + " " + var2 + " Hibakód:" + var1 + " " + (var4 == null ? "" : var4.toString());
         if (var0 == null) {
            System.out.println(var7);
         } else {
            String var8 = "";
            if (g_current_field_id != null) {
               var8 = " Mezőazonosító:" + FIdHelper.getFieldId(g_current_field_id);
            }

            Object[] var9 = new Object[]{ABEVFunctionSet.ID_DEFAULT_FUNCTION_ERROR, var2 + var8, var5, var3, var4, var6};
            var0.setError(var9);
         }

         calc_error_list.add(new Object[]{ABEVFunctionSet.ID_DEFAULT_FUNCTION_ERROR, var2, var5, var3, var4, var6});
      } catch (Exception var10) {
         var10.printStackTrace();
      }

   }

   private static void showWarning(ExpClass var0, Long var1, String var2, Exception var3, Object var4) {
      try {
         if (isInGeneratorMod()) {
            return;
         }

         Object[] var5 = new Object[]{var1, var2, IErrorList.LEVEL_SHOW_WARNING, var3, var4, null};
         var0.setError(var5);
         calc_error_list.add(new Object[]{var1, var2, IErrorList.LEVEL_WARNING, var3, var4, null});
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   private static Object createGotoButtonByDefaultPageNumber(String var0) {
      Object var1 = null;

      try {
         Object[] var2 = FIdHelper.getDataStoreKey(var0);
         var1 = createGotoButtonByField(var0, (Integer)var2[0]);
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
      }

      return var1;
   }

   private static Object createGotoButtonByField(String var0, Integer var1) {
      GoToButton var2 = new GoToButton("Ugrás a mezőre");
      var2.setFieldParams((Elem)getICObj().getGuiFormObject(), new StoreItem(var0, var1, "dummyData"));
      return var2;
   }

   private static Object createGotoButtonByPage(String var0, Integer var1) {
      GoToButton var2 = new GoToButton("Ugrás a mezőre");
      var2.setPageParams((Elem)getICObj().getGuiFormObject(), var0, var1);
      return var2;
   }

   public static void viewOperation(ExpClass var0, StringBuffer var1) {
      if (var0.getExpType() != 3) {
         var1.append(var0.getResult());
      } else {
         var1.append(var0.getIdentifier());
         var1.append("(");
         int var2 = var0.getParametersCount();

         for(int var4 = 0; var4 < var2; ++var4) {
            ExpClass var3 = var0.getParameter(var4);
            if (var4 > 0) {
               var1.append(",");
            }

            viewOperation(var3, var1);
         }

         var1.append(")");
      }
   }

   public static boolean cdv(String var0) {
      int[] var1 = new int[]{9, 7, 3, 1, 9, 7, 3, 1, 9, 7, 3, 1, 9, 7, 3, 1};
      int var2 = 0;
      int var3 = var0.length();

      for(int var4 = 0; var4 < var3 - 1; ++var4) {
         var2 += (var0.charAt(var4) - 48) * var1[var4];
      }

      return (10 - var2 % 10) % 10 == Integer.parseInt(var0.substring(var3 - 1, var3));
   }

   public static double Tz_round(double var0) {
      BigDecimal var2 = getRoundedDecimalValue(var0);
      return var2 != null ? Math.floor(var2.stripTrailingZeros().doubleValue() + 0.5D) : Math.floor(var0 + 0.5D);
   }

   public static double Tz_round(float var0) {
      BigDecimal var1 = getRoundedDecimalValue(var0);
      return var1 != null ? Math.floor((double)var1.stripTrailingZeros().floatValue() + 0.5D) : Math.floor((double)var0 + 0.5D);
   }

   public static BigDecimal Tz_round(BigDecimal var0) {
      if (var0 == null) {
         return null;
      } else {
         BigDecimal var1 = var0.stripTrailingZeros();
         return var1.signum() < 0 ? var1.setScale(0, 5) : var1.setScale(0, 4);
      }
   }

   public static BigDecimal getRoundedDecimalValue(Object var0) {
      try {
         if (g_current_field_id != null) {
            String var1 = FIdHelper.getFieldId(g_current_field_id);
            if (isRoundAble(var1, var0)) {
               int var2 = getPrecision(var1);
               return NumericOperations.round(var0, NumericOperations.getObjectType(var0, var0), var2);
            }
         }
      } catch (Exception var3) {
         Tools.eLog(var3, 1);
      }

      return null;
   }

   public static synchronized void fnSum(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      Object var5 = new BigDecimal(0);
      byte var6 = 2;

      for(int var7 = 0; var7 < var1; ++var7) {
         ExpClass var3 = var0.getParameter(var7);
         if (var3 != null) {
            int var2 = var3.getType();
            if (var2 != 0) {
               Object var4 = var3.getValue();
               if (var4 != null) {
                  if (var2 != 2) {
                     writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, var4);
                     break;
                  }

                  if (var5 == null) {
                     var5 = var3.getValue();
                  } else {
                     switch(var2) {
                     case 2:
                        try {
                           var5 = NumericOperations.add(var5, var4);
                           break;
                        } catch (Exception var9) {
                           writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
                           return;
                        }
                     default:
                        writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
                     }
                  }
               }
            }
         }
      }

      var0.setType(var6);
      var0.setResult(var5);
   }

   public static synchronized void fnSumNeg(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      Object var4 = null;
      byte var6 = 2;

      for(int var7 = 0; var7 < var1; ++var7) {
         ExpClass var3 = var0.getParameter(var7);
         if (var3 != null) {
            Object var5 = var3.getValue();
            if (var5 != null) {
               int var2 = var3.getType();
               if (var2 != 2) {
                  writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
                  break;
               }

               if (var4 == null && NumericOperations.getBigDecimal(var5).signum() < 0) {
                  var4 = var3.getValue();
               } else {
                  switch(var2) {
                  case 0:
                     break;
                  case 2:
                     var4 = NumericOperations.negAdd(var4, var5);
                     break;
                  default:
                     writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
                  }
               }
            }
         }
      }

      if (var4 == null) {
         var4 = new BigDecimal(0);
      }

      var0.setType(var6);
      var0.setResult(var4);
   }

   public static synchronized void fnSumPos(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      Object var4 = null;
      byte var6 = 2;

      for(int var7 = 0; var7 < var1; ++var7) {
         ExpClass var3 = var0.getParameter(var7);
         if (var3 != null) {
            Object var5 = var3.getValue();
            if (var5 != null) {
               int var2 = var3.getType();
               if (var2 != 2) {
                  writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
                  break;
               }

               if (var4 == null && NumericOperations.getBigDecimal(var5).signum() > 0) {
                  var4 = var3.getValue();
               } else {
                  switch(var2) {
                  case 0:
                     break;
                  case 2:
                     var4 = NumericOperations.posAdd(var4, var5);
                     break;
                  default:
                     writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
                  }
               }
            }
         }
      }

      if (var4 == null) {
         var4 = new BigDecimal(0);
      }

      var0.setType(var6);
      var0.setResult(var4);
   }

   public static synchronized void fnAvrg(ExpClass var0) {
      int var2 = 0;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 > 0) {
         Object var4 = null;
         int var6 = 2;

         ExpClass var3;
         int var8;
         for(var8 = 0; var8 < var1; ++var8) {
            if ((var3 = var0.getParameter(var8)) != null && (var4 = var3.getValue()) != null) {
               var6 = var3.getType();
               ++var2;
               break;
            }
         }

         for(int var9 = var8 + 1; var9 < var1; ++var9) {
            var3 = var0.getParameter(var9);
            if (var3 != null) {
               Object var5 = var3.getValue();
               if (var5 != null) {
                  int var7 = var3.getType();
                  if (var7 != 2 || var6 != 2) {
                     writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
                     break;
                  }

                  switch(var7) {
                  case 2:
                     try {
                        var4 = NumericOperations.add(var4, var5);
                        ++var2;
                        break;
                     } catch (Exception var12) {
                        writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
                        return;
                     }
                  default:
                     writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
                  }
               }
            }
         }

         if (var4 != null) {
            try {
               var4 = NumericOperations.division(var4, var2);
            } catch (Exception var11) {
               writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
               return;
            }
         }

         var0.setType(var6);
         var0.setResult(var4);
      }

   }

   public static synchronized void fnCsakSzamjegy(ExpClass var0) {
      String var3 = "0123456789";
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var4 = var0.getParameter(0);
         if (var4 == null) {
            return;
         }

         Object var5 = var4.getValue();
         if (var5 == null) {
            return;
         }

         String var2 = var4.getValue().toString();
         String var6 = "";

         for(int var7 = 0; var7 < var2.length(); ++var7) {
            if (var3.indexOf(var2.substring(var7, var7 + 1)) > -1) {
               var6 = var6 + var2.substring(var7, var7 + 1);
            }
         }

         var0.setType(1);
         var0.setResult(var6);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnSzamjegy(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 3) {
         ExpClass var5 = var0.getParameter(0);
         if (var5 == null) {
            return;
         }

         Object var7 = var5.getValue();
         if (var7 == null) {
            return;
         }

         var5 = var0.getParameter(1);
         if (var5 == null) {
            return;
         }

         Object var6 = var5.getValue();
         if (var6 == null) {
            return;
         }

         int var2 = NumericOperations.getInteger(var6);
         var5 = var0.getParameter(2);
         if (var5 == null) {
            return;
         }

         var6 = var5.getValue();
         if (var6 == null) {
            return;
         }

         int var3 = NumericOperations.getInteger(var6);
         String var4 = var7.toString();
         if (var4.length() == 0) {
            return;
         }

         if (var4.length() >= var2 - 1 + var3) {
            var4 = var4.substring(var2 - 1, var2 + var3 - 1);
         } else if (var4.length() < var2) {
            var4 = "";
         } else {
            var4 = var4.substring(var2 - 1);
         }

         if (var4.length() == 0) {
            return;
         }

         Integer var10;
         try {
            var10 = new Integer(var4);
         } catch (Exception var9) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
            return;
         }

         var0.setType(2);
         var0.setResult(var10);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnUjVPOPSzam(ExpClass var0) {
      int var2 = 0;
      byte var3 = 48;
      int[] var4 = new int[]{9, 3, 1, 7, 9, 3, 1, 7, 9, 3, 1, 7, 9};
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         try {
            ExpClass var8 = var0.getParameter(0);
            if (var8 == null) {
               return;
            }

            Object var9 = var8.getValue();
            if (var9 == null) {
               return;
            }

            String var5 = var9.toString().toUpperCase();
            var0.setType(4);
            var0.setResult(Boolean.FALSE);
            if (var5.length() != 14) {
               return;
            }

            String var6 = var5.substring(7, 8);
            if (!var6.equals("A") && !var6.equals("K") && !var6.equals("L") && !var6.equals("T") && !var6.equals("U") && !var6.equals("V") && !var6.equals("X") && !var6.equals("Y")) {
               return;
            }

            String var11 = var5.substring(0, 5) + var5.substring(8, 14);

            int var13;
            try {
               for(int var12 = 0; var12 < var11.length(); ++var12) {
                  var13 = Integer.parseInt(var11.substring(var12, var12 + 1));
               }
            } catch (NumberFormatException var14) {
               return;
            }

            char[] var16 = var5.toCharArray();

            for(var13 = 0; var13 < 13; ++var13) {
               if (var13 <= 4 || var13 >= 8 || var16[var13] >= '0' && var16[var13] <= '9') {
                  var2 += (var16[var13] - var3) * var4[var13];
               } else {
                  var2 += (var16[var13] - 55) * var4[var13];
               }
            }

            boolean var7 = var2 % 10 == var16[13] - var3;
            Boolean var10;
            if (var7) {
               var10 = Boolean.TRUE;
            } else {
               var10 = Boolean.FALSE;
            }

            var0.setResult(var10);
         } catch (Exception var15) {
            var0.setResult(Boolean.FALSE);
         }
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnEvesAdo1996(ExpClass var0) {
      double var2 = 0.0D;
      double var4 = 0.0D;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var6 = var0.getParameter(0);
         if (var6 == null) {
            return;
         }

         Object var7 = var6.getValue();
         if (var7 == null) {
            return;
         }

         var4 = NumericOperations.getDouble(var7);
         if (var4 <= 150000.0D) {
            var2 = Tz_round(0.2D * var4);
         } else {
            if (var2 == 0.0D) {
               var2 = NumericOperations.Adoszamit(150000.0D, 220000.0D, 30000.0D, var4, 0.25D);
            }

            if (var2 == 0.0D) {
               var2 = NumericOperations.Adoszamit(220000.0D, 380000.0D, 47500.0D, var4, 0.35D);
            }

            if (var2 == 0.0D) {
               var2 = NumericOperations.Adoszamit(380000.0D, 550000.0D, 103500.0D, var4, 0.4D);
            }

            if (var2 == 0.0D) {
               var2 = NumericOperations.Adoszamit(550000.0D, 900000.0D, 171500.0D, var4, 0.44D);
            }

            if (var2 == 0.0D) {
               var2 = Tz_round(0.48D * (var4 - 900000.0D) + 325000.0D);
            }
         }

         BigDecimal var8 = NumericOperations.tryIntegerNumberFormat(NumericOperations.checkDouble(new Double(var2)));
         var0.setType(2);
         var0.setResult(var8);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnEvesAdo1997(ExpClass var0) {
      double var2 = 0.0D;
      double var4 = 0.0D;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var6 = var0.getParameter(0);
         if (var6 == null) {
            return;
         }

         Object var7 = var6.getValue();
         if (var7 == null) {
            return;
         }

         var4 = NumericOperations.getDouble(var7);
         if (var4 <= 250000.0D) {
            var2 = Tz_round(0.2D * var4);
         } else {
            if (var2 == 0.0D) {
               var2 = NumericOperations.Adoszamit(250000.0D, 300000.0D, 50000.0D, var4, 0.22D);
            }

            if (var2 == 0.0D) {
               var2 = NumericOperations.Adoszamit(300000.0D, 500000.0D, 61000.0D, var4, 0.31D);
            }

            if (var2 == 0.0D) {
               var2 = NumericOperations.Adoszamit(500000.0D, 700000.0D, 123000.0D, var4, 0.35D);
            }

            if (var2 == 0.0D) {
               var2 = NumericOperations.Adoszamit(700000.0D, 1100000.0D, 193000.0D, var4, 0.39D);
            }

            if (var2 == 0.0D) {
               var2 = Tz_round(0.42D * (var4 - 1100000.0D) + 349000.0D);
            }
         }

         BigDecimal var8 = NumericOperations.tryIntegerNumberFormat(NumericOperations.checkDouble(new Double(var2)));
         var0.setType(2);
         var0.setResult(var8);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnEvesAdo1999(ExpClass var0) {
      double var2 = 0.0D;
      double var4 = 0.0D;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var6 = var0.getParameter(0);
         if (var6 == null) {
            return;
         }

         Object var7 = var6.getValue();
         if (var7 == null) {
            return;
         }

         var4 = NumericOperations.getDouble(var7);
         if (var4 <= 400000.0D) {
            var2 = Tz_round(0.2D * var4);
         } else {
            if (var2 == 0.0D) {
               var2 = NumericOperations.Adoszamit(400000.0D, 1000000.0D, 80000.0D, var4, 0.3D);
            }

            if (var2 == 0.0D) {
               var2 = Tz_round(0.4D * (var4 - 1000000.0D) + 260000.0D);
            }
         }

         BigDecimal var8 = NumericOperations.tryIntegerNumberFormat(NumericOperations.checkDouble(new Double(var2)));
         var0.setType(2);
         var0.setResult(var8);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnEvesAdo2001(ExpClass var0) {
      double var2 = 0.0D;
      double var4 = 0.0D;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var6 = var0.getParameter(0);
         if (var6 == null) {
            return;
         }

         Object var7 = var6.getValue();
         if (var7 == null) {
            return;
         }

         var4 = NumericOperations.getDouble(var7);
         if (var4 <= 480000.0D) {
            var2 = Tz_round(0.2D * var4);
         } else {
            if (var2 == 0.0D) {
               var2 = NumericOperations.Adoszamit(480000.0D, 1050000.0D, 96000.0D, var4, 0.3D);
            }

            if (var2 == 0.0D) {
               var2 = Tz_round(0.4D * (var4 - 1050000.0D) + 267000.0D);
            }
         }

         BigDecimal var8 = NumericOperations.tryIntegerNumberFormat(NumericOperations.checkDouble(new Double(var2)));
         var0.setType(2);
         var0.setResult(var8);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnEvesAdo2002(ExpClass var0) {
      double var2 = 0.0D;
      double var4 = 0.0D;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var6 = var0.getParameter(0);
         if (var6 == null) {
            return;
         }

         Object var7 = var6.getValue();
         if (var7 == null) {
            return;
         }

         var4 = NumericOperations.getDouble(var7);
         if (var4 <= 600000.0D) {
            var2 = Tz_round(0.2D * var4);
         } else {
            if (var2 == 0.0D) {
               var2 = NumericOperations.Adoszamit(600000.0D, 1200000.0D, 120000.0D, var4, 0.3D);
            }

            if (var2 == 0.0D) {
               var2 = Tz_round(0.4D * (var4 - 1200000.0D) + 300000.0D);
            }
         }

         BigDecimal var8 = NumericOperations.tryIntegerNumberFormat(NumericOperations.checkDouble(new Double(var2)));
         var0.setType(2);
         var0.setResult(var8);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnEvesAdo2003(ExpClass var0) {
      double var2 = 0.0D;
      double var4 = 0.0D;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var6 = var0.getParameter(0);
         if (var6 == null) {
            return;
         }

         Object var7 = var6.getValue();
         if (var7 == null) {
            return;
         }

         var4 = NumericOperations.getDouble(var7);
         if (var4 <= 650000.0D) {
            var2 = Tz_round(0.2D * var4);
         } else {
            if (var2 == 0.0D) {
               var2 = NumericOperations.Adoszamit(650000.0D, 1350000.0D, 130000.0D, var4, 0.3D);
            }

            if (var2 == 0.0D) {
               var2 = Tz_round(0.4D * (var4 - 1350000.0D) + 340000.0D);
            }
         }

         BigDecimal var8 = NumericOperations.tryIntegerNumberFormat(NumericOperations.checkDouble(new Double(var2)));
         var0.setType(2);
         var0.setResult(var8);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnEvesAdo2004(ExpClass var0) {
      double var2 = 0.0D;
      double var4 = 0.0D;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var6 = var0.getParameter(0);
         if (var6 == null) {
            return;
         }

         Object var7 = var6.getValue();
         if (var7 == null) {
            return;
         }

         var4 = NumericOperations.getDouble(var7);
         if (var4 <= 800000.0D) {
            var2 = Tz_round(0.18D * var4);
         } else {
            if (var2 == 0.0D) {
               var2 = NumericOperations.Adoszamit(800000.0D, 1500000.0D, 144000.0D, var4, 0.26D);
            }

            if (var2 == 0.0D) {
               var2 = Tz_round(0.38D * (var4 - 1500000.0D) + 326000.0D);
            }
         }

         BigDecimal var8 = NumericOperations.tryIntegerNumberFormat(NumericOperations.checkDouble(new Double(var2)));
         var0.setType(2);
         var0.setResult(var8);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnEvesAdo2005(ExpClass var0) {
      double var2 = 0.0D;
      double var4 = 0.0D;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var6 = var0.getParameter(0);
         if (var6 == null) {
            return;
         }

         Object var7 = var6.getValue();
         if (var7 == null) {
            return;
         }

         var4 = NumericOperations.getDouble(var7);
         if (var4 <= 1500000.0D) {
            var2 = Tz_round(0.18D * var4);
         }

         if (var4 > 1500000.0D) {
            var2 = Tz_round(0.38D * (var4 - 1500000.0D)) + 270000.0D;
         }

         BigDecimal var8 = NumericOperations.tryIntegerNumberFormat(NumericOperations.checkDouble(new Double(var2)));
         var0.setType(2);
         var0.setResult(var8);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnEvesAdo2006(ExpClass var0) {
      double var2 = 0.0D;
      double var4 = 0.0D;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var6 = var0.getParameter(0);
         if (var6 == null) {
            return;
         }

         Object var7 = var6.getValue();
         if (var7 == null) {
            return;
         }

         var4 = NumericOperations.getDouble(var7);
         if (var4 <= 1550000.0D) {
            var2 = Tz_round(0.18D * var4);
         }

         if (var4 > 1550000.0D) {
            var2 = Tz_round(0.36D * (var4 - 1550000.0D)) + 279000.0D;
         }

         BigDecimal var8 = NumericOperations.tryIntegerNumberFormat(NumericOperations.checkDouble(new Double(var2)));
         var0.setType(2);
         var0.setResult(var8);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static boolean EvEllenorzes(int var0, int var1, int var2, int var3) {
      boolean var4 = true;
      Date var5 = new Date();
      calendar.set(var0, var1, var2);
      long var6 = (var5.getTime() - calendar.getTimeInMillis()) / 86400000L;
      float var8 = (float)(var6 - (long)var3);
      var8 = (float)Math.round(var8 / 365.0F);
      if (var8 > 110.0F || var8 < 1.0F) {
         var4 = false;
      }

      return var4;
   }

   public static synchronized void fnJoAdoAzonosito(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      boolean var6 = false;
      if (var1 == 1) {
         ExpClass var3 = var0.getParameter(0);
         int var2 = var3.getType();
         if (var2 == 0) {
            return;
         }

         if (var2 != 1 && var2 != 2) {
            writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
            return;
         }

         Boolean var4;
         String var5;
         if (var2 == 1) {
            var5 = var3.getValue().toString();
            if (var5.length() == 0) {
               var4 = Boolean.TRUE;
               var0.setType(4);
               var0.setResult(var4);
               return;
            }
         } else {
            String var7 = var3.getValue().toString();
            if (var7.equals("0")) {
               var4 = Boolean.TRUE;
               var0.setType(4);
               var0.setResult(var4);
               return;
            }

            var5 = var7;
         }

         if (var5.length() != 10) {
            var4 = Boolean.FALSE;
            var0.setType(4);
            var0.setResult(var4);
            return;
         }

         try {
            Long.parseLong(var5);
         } catch (Exception var10) {
            var4 = Boolean.FALSE;
            var0.setType(4);
            var0.setResult(var4);
            return;
         }

         char[] var11 = var5.toCharArray();
         if (var11[0] != '8') {
            var4 = Boolean.FALSE;
            var0.setType(4);
            var0.setResult(var4);
            return;
         }

         int var8 = 0;

         for(int var9 = 0; var9 < 9; ++var9) {
            var8 += (var9 + 1) * Character.getNumericValue(var11[var9]);
         }

         if (Character.getNumericValue(var11[9]) != var8 % 11) {
            var4 = Boolean.FALSE;
            var0.setType(4);
            var0.setResult(var4);
            return;
         }

         var6 = true;
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

      var0.setType(4);
      var0.setResult(var6);
   }

   public static synchronized void fnJoAlszamlaSzam(ExpClass var0) {
      boolean var3 = false;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var4 = var0.getParameter(0);
         if (var4 == null) {
            return;
         }

         int var2 = var4.getType();
         if (var2 == 0) {
            return;
         }

         if (var2 == 1 && var4.getValue().toString().equals("")) {
            var3 = true;
         }

         if (var2 == 2 && var4.getValue().toString().equals("0")) {
            var3 = true;
         }

         String var6 = var4.getValue().toString();

         try {
            Long.parseLong(var6);
            var3 = (var6.length() == 8 || var6.length() == 16) && cdv(var6);
         } catch (Exception var8) {
            var3 = false;
         }

         var0.setType(4);
         var0.setResult(var3);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnJoOstermelo(ExpClass var0) {
      byte var1 = 48;
      var0.setType(0);
      var0.setResult((Object)null);
      int var2 = var0.getParametersCount();
      if (var2 == 1) {
         ExpClass var3 = var0.getParameter(0);
         int var4 = var3.getType();
         if (var4 == 0) {
            return;
         }

         if (var4 != 1 && var4 != 2) {
            writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
            return;
         }

         var0.setType(4);
         String var5 = var3.getValue().toString();
         int var6;
         if ((var6 = chkInputIds(var4, var5, 10)) < 1) {
            if (var6 == 0) {
               var0.setResult(Boolean.TRUE);
            } else {
               var0.setResult(Boolean.FALSE);
            }

            return;
         }

         char[] var7 = var5.toCharArray();
         int[] var8 = new int[]{9, 7, 3, 1, 9, 7, 3, 1, 9};
         boolean var9 = 9 - getCDV(var7, var8, 0, 10) == var7[9] - var1;
         var0.setResult(var9);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnJoBarkod(ExpClass var0) {
      byte var1 = 48;
      byte var2 = 1;
      byte var3 = 2;
      var0.setType(0);
      var0.setResult((Object)null);
      int var4 = var0.getParametersCount();
      if (var4 == 1) {
         ExpClass var5 = var0.getParameter(0);
         int var6 = var5.getType();
         if (var6 == 0) {
            return;
         }

         if (var6 != 1 && var6 != 2) {
            writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
            return;
         }

         var0.setType(4);
         String var7 = var5.getValue().toString();
         int var8;
         if ((var8 = chkInputIds(var6, var7, 10)) < 1) {
            if (var8 == 0) {
               var0.setResult(Boolean.TRUE);
            } else {
               var0.setResult(Boolean.FALSE);
            }

            return;
         }

         char[] var9 = var7.toCharArray();
         int[] var10 = new int[]{9, 7, 3, 1, 9, 7, 3, 1, 9};
         int var11 = (10 - getCDV(var9, var10, 0, 10)) % 10;
         boolean var12 = var11 == var9[9] - var1;
         if (!var12) {
            var12 = (var11 + var2) % 10 == var9[9] - var1;
         }

         if (!var12) {
            var12 = (var11 + var3) % 10 == var9[9] - var1;
         }

         var0.setResult(var12);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnJoTajSzam(ExpClass var0) {
      String var1 = "000000000";
      byte var2 = 48;
      var0.setType(0);
      var0.setResult((Object)null);
      int var3 = var0.getParametersCount();
      if (var3 == 1) {
         ExpClass var4 = var0.getParameter(0);
         int var5 = var4.getType();
         if (var5 == 0) {
            return;
         }

         if (var5 != 1 && var5 != 2) {
            writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
            return;
         }

         var0.setType(4);
         String var6 = var4.getValue().toString();
         if (var6.equals(var1)) {
            var0.setResult(Boolean.FALSE);
            return;
         }

         int var7;
         if ((var7 = chkInputIds(var5, var6, 9)) < 1) {
            if (var7 == 0) {
               var0.setResult(Boolean.TRUE);
            } else {
               var0.setResult(Boolean.FALSE);
            }

            return;
         }

         char[] var8 = var6.toCharArray();
         boolean var9 = getCDVs1(var8, 8, 3, 7, 10) == var8[8] - var2;
         var0.setResult(var9);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   private static int getCDV(char[] var0, int[] var1, int var2, int var3) {
      int var4 = var2;
      byte var5 = 48;

      for(int var6 = 0; var6 < var1.length; ++var6) {
         var4 += (var0[var6] - var5) * var1[var6];
      }

      return var4 % var3;
   }

   private static int getCDVs1(char[] var0, int var1, int var2, int var3, int var4) {
      int var5 = 0;
      int var6 = 0;
      byte var7 = 48;

      int var8;
      for(var8 = 0; var8 < var1; var8 += 2) {
         var6 += var0[var8] - var7;
      }

      for(var8 = 1; var8 < var1; var8 += 2) {
         var5 += var0[var8] - var7;
      }

      return (var5 * var3 + var6 * var2) % var4;
   }

   private static int chkInputIds(int var0, String var1, int var2) {
      if (var0 == 1) {
         if (var1.length() == 0) {
            return 0;
         }
      } else if (var1.equals("0")) {
         return 0;
      }

      if (var1.length() != var2) {
         return -1;
      } else {
         try {
            Long.parseLong(var1);
            return 1;
         } catch (Exception var4) {
            return -1;
         }
      }
   }

   public static synchronized void fnIn(ExpClass var0) {
      boolean var2 = false;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 > 1) {
         String var8 = null;
         ExpClass var4 = var0.getParameter(0);
         if (var4 == null) {
            return;
         }

         int var7 = var4.getType();
         if (var7 != 0) {
            Object var3 = var4.getValue();
            if (var3 != null) {
               var8 = var3.toString();
               if (var8.length() == 0) {
                  var8 = null;
               }
            } else {
               var8 = null;
            }
         }

         for(int var9 = 1; var9 < var1; ++var9) {
            var4 = var0.getParameter(var9);
            if (var4 != null) {
               var7 = var4.getType();
               String var6;
               if (var7 == 0) {
                  var6 = null;
               } else {
                  Object var10 = var4.getValue();
                  if (var10 != null) {
                     var6 = var10.toString();
                     if (var6.length() == 0) {
                        var6 = null;
                     }
                  } else {
                     var6 = null;
                  }
               }

               if (var8 == null && var6 == null) {
                  var2 = true;
                  break;
               }

               if (var8 != null && var6 != null && var8.compareTo(var6.toString()) == 0) {
                  var2 = true;
                  break;
               }
            }
         }

         var0.setType(4);
         var0.setResult(var2);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnJoDatum(ExpClass var0) {
      boolean var2 = false;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var3 = var0.getParameter(0);
         if (var3 != null && var3.getValue() != null) {
            String var5 = var3.getValue().toString();
            if (var5.length() == 0) {
               var2 = true;
            } else {
               var2 = isJoDatum(var5);
            }
         }

         Boolean var4;
         if (var2) {
            var4 = Boolean.TRUE;
         } else {
            var4 = Boolean.FALSE;
         }

         var0.setType(4);
         var0.setResult(var4);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static boolean isJoDatum(String var0) {
      boolean var1 = false;
      int var2 = var0.length();
      if (var2 != 8) {
         var1 = false;
      } else {
         try {
            Integer.parseInt(var0);
            formatter.setLenient(false);
            formatter.parse(var0);
            var1 = true;
         } catch (Exception var4) {
            var1 = false;
         }
      }

      return var1;
   }

   public static synchronized void fnJoHatarozatszam(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         Boolean var2 = Boolean.TRUE;
         var0.setType(4);
         var0.setResult(var2);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnJoSzamlaszam(ExpClass var0) {
      boolean var3 = false;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var4 = var0.getParameter(0);
         int var2 = var4.getType();
         if (var2 == 0) {
            return;
         }

         if (var4 != null && var4.getValue() != null) {
            var0.setType(4);
            if (var2 == 1 && var4.getValue().toString().equals("")) {
               var0.setResult(true);
               return;
            }

            String var5 = var4.getValue().toString();

            try {
               Long.parseLong(var5);
               var3 = var5.length() == 8 && cdv(var5);
            } catch (Exception var7) {
               var0.setResult(false);
               return;
            }
         }

         var0.setResult(var3);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnJoVPOPSzam(ExpClass var0) {
      boolean var4 = true;
      boolean var5 = true;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 != 1) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            ExpClass var6 = var0.getParameter(0);
            if (var6 != null) {
               if (var6.getValue() == null) {
                  return;
               }

               int var2 = var6.getType();
               if (var2 == 0) {
                  return;
               }

               var0.setType(4);
               var0.setResult(Boolean.FALSE);
               int[] var8 = new int[]{9, 3, 1, 7};
               String var9;
               if (var2 == 1) {
                  var9 = var6.getValue().toString();
                  if (var9.length() == 0) {
                     var5 = false;
                  }
               } else {
                  var9 = NumericOperations.getBigDecimal(var6.getValue()).toPlainString();
               }

               if (var5) {
                  int var3 = 0;

                  for(int var10 = 0; var10 < var9.length(); ++var10) {
                     if (var9.substring(var10, var10 + 1).equals("0")) {
                        ++var3;
                     }
                  }

                  if (var9.length() == var3) {
                     var5 = false;
                     var4 = false;
                  }
               }

               if (var5 && var9.length() != 13 && var9.length() != 14) {
                  var4 = false;
                  var5 = false;
               }

               if (var5) {
                  char[] var16 = var9.toCharArray();
                  int var11 = 0;
                  int var12 = 0;
                  int var13 = var9.length();

                  for(int var14 = 0; var14 < var13 - 1; ++var14) {
                     if (var9.length() == 14 && var14 == 5 && !Character.isDigit(var16[var14])) {
                        var11 += (var16[var14] % 10 - 5) * var8[var12];
                     } else {
                        var11 += Character.getNumericValue(var16[var14]) * var8[var12];
                     }

                     var12 = (var12 + 1) % 4;
                  }

                  var4 = Character.getNumericValue(var16[var13 - 1]) == var11 % 10;
               }
            }

            var0.setResult(var4);
         } catch (Exception var15) {
            var15.printStackTrace();
            var0.setResult(Boolean.FALSE);
         }
      }

   }

   public static synchronized void fnKisbetus(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var3 = var0.getParameter(0);
         if (var3 != null && var3.getValue() != null) {
            int var2 = var3.getType();
            if (var2 == 1) {
               String var4 = var3.getValue().toString().toLowerCase();
               var0.setType(1);
               var0.setResult(var4);
            }
         }
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnKornyezet(ExpClass var0) {
      boolean var7 = false;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 2) {
         ExpClass var8 = var0.getParameter(0);
         if (var8 == null) {
            return;
         }

         Object var9 = var8.getValue();
         if (var9 == null) {
            return;
         }

         int var2 = var8.getType();
         if (var2 != 2) {
            return;
         }

         double var3 = Math.abs(NumericOperations.getDouble(var9));
         var8 = var0.getParameter(1);
         if (var8 == null) {
            return;
         }

         var9 = var8.getValue();
         if (var9 == null) {
            return;
         }

         var2 = var8.getType();
         if (var2 != 2) {
            return;
         }

         double var5 = NumericOperations.getDouble(var9);
         var7 = var3 <= var5;
         Boolean var10;
         if (var7) {
            var10 = Boolean.TRUE;
         } else {
            var10 = Boolean.FALSE;
         }

         var0.setType(4);
         var0.setResult(var10);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnLen(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var3 = var0.getParameter(0);
         if (var3 == null) {
            return;
         }

         Object var4 = var3.getValue();
         if (var4 == null) {
            return;
         }

         int var2 = String.valueOf(var3.getValue().toString()).length();
         Integer var5 = new Integer(var2);
         var0.setType(2);
         var0.setResult(var5);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnDatumEv(ExpClass var0) {
      String var3 = "";
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         try {
            ExpClass var4 = var0.getParameter(0);
            if (var4 == null) {
               return;
            }

            int var2 = var4.getType();
            String var5;
            switch(var2) {
            case 0:
               var5 = "0";
               break;
            case 2:
               var3 = var4.getValue().toString();

               try {
                  char[] var6 = var3.toCharArray();
                  if (var3.length() <= 3) {
                     var3 = String.copyValueOf(var6, 0, var3.length());
                  } else {
                     var3 = String.copyValueOf(var6, 0, 4);
                  }

                  var5 = var3;
               } catch (Exception var7) {
                  var5 = "0";
               }
               break;
            default:
               return;
            }

            int var9 = Integer.parseInt(var5);
            if (var9 > -1 && var9 < 10000) {
               var0.setType(2);
               var0.setResult(new Integer(var9));
            }
         } catch (Exception var8) {
            var0.setType(0);
            var0.setResult((Object)null);
         }
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnDatumHo(ExpClass var0) {
      String var4 = "";
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         try {
            ExpClass var5 = var0.getParameter(0);
            if (var5 == null) {
               return;
            }

            int var2 = var5.getType();
            String var6;
            switch(var2) {
            case 0:
               var6 = "0";
               break;
            case 2:
               try {
                  var4 = var5.getValue().toString();
                  char[] var7 = var4.toCharArray();
                  switch(var4.length()) {
                  case 0:
                  case 1:
                  case 2:
                  case 3:
                  case 4:
                     var4 = "0";
                     break;
                  case 5:
                     var4 = String.copyValueOf(var7, 4, 1);
                     break;
                  default:
                     var4 = String.copyValueOf(var7, 4, 2);
                  }

                  var6 = var4;
               } catch (Exception var8) {
                  var6 = "0";
               }
               break;
            default:
               return;
            }

            int var3 = Integer.parseInt(var6);
            if (var3 > 0 && var3 < 13) {
               var0.setType(2);
               var0.setResult(new Integer(var3));
            }
         } catch (Exception var9) {
            var0.setType(0);
            var0.setResult((Object)null);
         }
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnDatumNap(ExpClass var0) {
      String var3 = "";
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         try {
            ExpClass var4 = var0.getParameter(0);
            if (var4 == null) {
               return;
            }

            int var2 = var4.getType();
            String var5;
            switch(var2) {
            case 0:
               var5 = "0";
               break;
            case 2:
               try {
                  var3 = var4.getValue().toString();
                  char[] var6 = var3.toCharArray();
                  switch(var3.length()) {
                  case 0:
                  case 1:
                  case 2:
                  case 3:
                  case 4:
                  case 5:
                  case 6:
                     var3 = "0";
                     break;
                  case 7:
                     var3 = String.copyValueOf(var6, 6, 1);
                     break;
                  default:
                     var3 = String.copyValueOf(var6, 6, 2);
                  }

                  var5 = var3;
               } catch (Exception var7) {
                  var5 = "0";
               }
               break;
            default:
               return;
            }

            int var9 = Integer.parseInt(var5);
            if (var9 > 0 && var9 < 32) {
               var0.setType(2);
               var0.setResult(new Integer(var9));
            }
         } catch (Exception var8) {
            var0.setType(0);
            var0.setResult((Object)null);
         }
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnDatumNev(ExpClass var0) {
      String var4 = "";
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var5 = var0.getParameter(0);
         if (var5 == null) {
            return;
         }

         int var2 = var5.getType();
         byte var6;
         switch(var2) {
         case 0:
            var6 = 0;
            break;
         case 2:
            try {
               var4 = var5.getValue().toString();
               char[] var7 = var4.toCharArray();
               switch(var4.length()) {
               case 0:
               case 1:
               case 2:
               case 3:
               case 4:
                  var4 = "0";
                  break;
               case 5:
                  var4 = String.copyValueOf(var7, 4, 1);
                  break;
               default:
                  var4 = String.copyValueOf(var7, 4, 2);
               }

               int var8 = Integer.parseInt(var4);
               byte var3;
               switch(var8) {
               case 0:
                  var3 = 0;
                  break;
               case 1:
               case 2:
               case 3:
                  var3 = 1;
                  break;
               case 4:
               case 5:
               case 6:
                  var3 = 2;
                  break;
               case 7:
               case 8:
               case 9:
                  var3 = 3;
                  break;
               case 10:
               case 11:
               case 12:
                  var3 = 4;
                  break;
               default:
                  var3 = 0;
               }

               var6 = var3;
            } catch (Exception var9) {
               var6 = 0;
            }
            break;
         default:
            var6 = 0;
         }

         var0.setType(2);
         var0.setResult(new BigDecimal(var6));
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnDatumokKulonbsege(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 2) {
         ExpClass var7 = var0.getParameter(0);
         if (var7 == null) {
            return;
         }

         Object var8 = var7.getValue();
         if (var8 == null) {
            return;
         }

         int var2 = var7.getType();
         if (var2 != 2) {
            return;
         }

         String var3 = var7.getValue().toString();
         var7 = var0.getParameter(1);
         if (var7 == null) {
            return;
         }

         var8 = var7.getValue();
         if (var8 == null) {
            return;
         }

         var2 = var7.getType();
         if (var2 != 2) {
            return;
         }

         String var4 = var7.getValue().toString();
         BigDecimal var9 = new BigDecimal(0);
         if (isJoDatum(var3) && isJoDatum(var4)) {
            try {
               formatter.setLenient(false);
               Date var10 = formatter.parse(var3);
               long var11 = var10.getTime();
               Date var13 = formatter.parse(var4);
               long var14 = var13.getTime();
               long var5 = var14 / 86400000L - var11 / 86400000L;
               if (var11 < 0L && var14 > 0L) {
                  ++var5;
               }

               if (var11 > 0L && var14 < 0L) {
                  --var5;
               }

               var9 = new BigDecimal(var5);
            } catch (Exception var16) {
               Tools.eLog(var16, 1);
            }
         }

         var0.setType(2);
         var0.setResult(var9);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnDatumPlusznap(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 2) {
         ExpClass var5 = var0.getParameter(0);
         if (var5 == null) {
            return;
         }

         Object var6 = var5.getValue();
         if (var6 == null) {
            return;
         }

         int var2 = var5.getType();
         if (var2 != 2) {
            return;
         }

         String var3 = var5.getValue().toString();
         var5 = var0.getParameter(1);
         if (var5 == null) {
            return;
         }

         var6 = var5.getValue();
         if (var6 == null) {
            return;
         }

         var2 = var5.getType();
         if (var2 != 2) {
            return;
         }

         String var4 = var5.getValue().toString();
         Integer var7 = new Integer(0);
         if (isJoDatum(var3)) {
            try {
               formatter.setLenient(false);
               Date var10 = formatter.parse(var3);
               calendar.setTime(var10);
               calendar.add(5, Integer.valueOf(var4));
               var7 = new Integer(Integer.parseInt(formatter.format(calendar.getTime())));
            } catch (Exception var11) {
               Tools.eLog(var11, 1);
            }
         }

         var0.setType(2);
         var0.setResult(var7);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnDatumPluszHo(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);

      try {
         int var1 = var0.getParametersCount();
         if (var1 == 3) {
            Integer var2 = NumericOperations.getInteger(getParameter(var0, 0, true, 2, true));
            Integer var3 = NumericOperations.getInteger(getParameter(var0, 1, true, 2, true));
            Integer var4 = NumericOperations.getInteger(getParameter(var0, 2, true, 2, true));
            if (var2 == null || var3 == null || var4 == null) {
               return;
            }

            Integer var5 = 0;
            if (isJoDatum(var2.toString())) {
               try {
                  formatter.setLenient(false);
                  Date var6 = formatter.parse(var2.toString());
                  calendar.setTime(var6);
                  calendar.add(2, var3);
                  if (var4 == 1) {
                     calendar.set(5, 1);
                  } else if (var4 == 2) {
                     calendar.set(5, calendar.getActualMaximum(5));
                  }

                  var5 = new Integer(Integer.parseInt(formatter.format(calendar.getTime())));
               } catch (Exception var7) {
                  Tools.eLog(var7, 1);
               }
            }

            var0.setType(2);
            var0.setResult(var5);
         } else {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
         }
      } catch (Exception var8) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", var8, (Object)null);
      }

   }

   public static synchronized void fnTrim(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      char var1 = 20;

      try {
         int var2 = var0.getParametersCount();
         if (var2 != 1 && var2 != 2) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
         } else {
            Object var3 = getParameter(var0, 0, true, 1, true);
            if (var3 == null) {
               return;
            }

            String var4 = var3.toString();
            if (var4.length() == 0) {
               var0.setResult(var4);
               var0.setType(1);
               return;
            }

            if (var2 == 2) {
               String var5 = ABEVFeaturedBaseFunctions.getString(getParameter(var0, 1, true, 1, true), "");
               if (var5.length() == 0) {
                  var0.setResult(var4);
                  var0.setType(1);
                  return;
               }

               if (var5.length() != 1) {
                  throw new Exception("Hibás vagy hiányzó paraméter!  Csak egy karakter lehet a trimmelő paraméter.");
               }

               var1 = var5.toCharArray()[0];
            }

            var0.setResult(var1 == 20 ? var4.trim() : trim(var4, var1));
            var0.setType(1);
         }
      } catch (Exception var6) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", var6, (Object)null);
      }

   }

   public static String trim(String var0, char var1) {
      if (var0 != null && var0.length() != 0) {
         int var2 = var0.length();
         int var3 = 0;
         byte var4 = 0;
         char[] var5 = var0.toCharArray();

         int var6;
         for(var6 = var0.length(); var3 < var2 && var5[var4 + var3] == var1; ++var3) {
         }

         while(var3 < var2 && var5[var4 + var2 - 1] == var1) {
            --var2;
         }

         return var3 <= 0 && var2 >= var6 ? var0 : var0.substring(var3, var2);
      } else {
         return var0;
      }
   }

   public static synchronized void fnEgyuttesenKitoltendo(ExpClass var0) {
      boolean var3 = true;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      ExpClass var4 = var0.getParameter(0);
      Object var6 = var4.getValue();
      int var7 = var4.getType();
      if (var7 == 4 && !(Boolean)var6) {
         var6 = null;
      }

      if (var7 == 2 && !kitoltottNumField(var4, var6)) {
         var6 = null;
      }

      boolean var2 = var6 == null;

      for(int var8 = 1; var8 < var1; ++var8) {
         var4 = var0.getParameter(var8);
         var6 = var4.getValue();
         var7 = var4.getType();
         if (var7 == 4 && !(Boolean)var6) {
            var6 = null;
         }

         if (var7 == 2 && !kitoltottNumField(var4, var6)) {
            var6 = null;
         }

         if (var2 != (var6 == null)) {
            var3 = false;
            break;
         }
      }

      Boolean var5;
      if (var3) {
         var5 = Boolean.TRUE;
      } else {
         var5 = Boolean.FALSE;
      }

      var0.setType(4);
      var0.setResult(var5);
   }

   public static synchronized void fnFilln(ExpClass var0) {
      int var2 = 0;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      ExpClass var5 = var0.getParameter(0);
      Object var7 = var5.getValue();
      int var3 = NumericOperations.getInteger(var7);

      for(int var9 = 1; var9 < var1; ++var9) {
         var5 = var0.getParameter(var9);
         var7 = var5.getValue();
         int var8 = var5.getType();
         if (var7 != null && (var8 != 2 || kitoltottNumField(var5, var7))) {
            ++var2;
         }
      }

      boolean var4 = var2 == var3;
      Boolean var6;
      if (var4) {
         var6 = Boolean.TRUE;
      } else {
         var6 = Boolean.FALSE;
      }

      var0.setType(4);
      var0.setResult(var6);
   }

   public static synchronized void fnFolytonos(ExpClass var0) {
      boolean var2 = false;
      boolean var3 = true;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();

      for(int var8 = 0; var8 < var1; ++var8) {
         ExpClass var4 = var0.getParameter(var8);
         Object var6 = var4.getValue();
         int var7 = var4.getType();
         if (var6 == null || var7 == 2 && !kitoltottNumField(var4, var6)) {
            var2 = true;
         } else if (var2) {
            var3 = false;
            break;
         }
      }

      Boolean var5;
      if (var3) {
         var5 = Boolean.TRUE;
      } else {
         var5 = Boolean.FALSE;
      }

      var0.setType(4);
      var0.setResult(var5);
   }

   public static synchronized void fnUnique(ExpClass var0) {
      boolean var2 = true;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();

      for(int var8 = 0; var8 < var1; ++var8) {
         ExpClass var3 = var0.getParameter(var8);
         if (var3 == null) {
            break;
         }

         Object var5 = var3.getValue();
         int var7 = var3.getType();
         if ((var7 != 2 || kitoltottNumField(var3, var5)) && var5 != null && (var7 != 1 || var5.toString().length() != 0)) {
            for(int var9 = 0; var8 < var1; ++var9) {
               if (var8 != var9) {
                  var3 = var0.getParameter(var9);
                  if (var3 == null) {
                     break;
                  }

                  Object var6 = var3.getValue();
                  var7 = var3.getType();
                  if (var7 == 2 && !kitoltottNumField(var3, var6)) {
                     var6 = null;
                  }

                  if (var6 != null && var5.toString().equals(var6.toString())) {
                     var2 = false;
                     break;
                  }
               }
            }
         }
      }

      Boolean var4;
      if (var2) {
         var4 = Boolean.TRUE;
      } else {
         var4 = Boolean.FALSE;
      }

      var0.setType(4);
      var0.setResult(var4);
   }

   public static synchronized void fnKitoltott(ExpClass var0) {
      boolean var3 = true;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount() - 1;
      int var7 = -1;

      while(var7++ < var1 && var3) {
         ExpClass var4 = var0.getParameter(var7);
         int var2 = var4.getType();
         Object var6 = var4.getValue();
         if (var6 == null) {
            var3 = false;
         } else {
            switch(var2) {
            case 0:
               var3 = false;
               break;
            case 1:
               if (((String)var6).length() == 0) {
                  var3 = false;
               }
               break;
            case 2:
               if (!kitoltottNumField(var4, var6)) {
                  var3 = false;
               }
            case 3:
            default:
               break;
            case 4:
               var3 = (Boolean)var6;
            }
         }
      }

      Boolean var5;
      if (var3) {
         var5 = Boolean.TRUE;
      } else {
         var5 = Boolean.FALSE;
      }

      var0.setType(4);
      var0.setResult(var5);
   }

   public static synchronized void fnKitoltottDarab(ExpClass var0) {
      int var2 = 0;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();

      for(int var7 = 0; var7 < var1; ++var7) {
         ExpClass var4 = var0.getParameter(var7);
         int var3 = var4.getType();
         Object var6 = var4.getValue();
         if (var6 != null) {
            switch(var3) {
            case 0:
               break;
            case 1:
               if (((String)var6).length() != 0) {
                  ++var2;
               }
               break;
            case 2:
               if (kitoltottNumField(var4, var6)) {
                  ++var2;
               }
               break;
            case 3:
            default:
               ++var2;
               break;
            case 4:
               if ((Boolean)var6) {
                  ++var2;
               }
            }
         }
      }

      Integer var5 = new Integer(var2);
      var0.setType(2);
      var0.setResult(var5);
   }

   public static synchronized void fnFlag(ExpClass var0) {
      boolean var2 = false;
      String var4 = "";
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();

      for(int var8 = 0; var8 < var1; ++var8) {
         ExpClass var5 = var0.getParameter(var8);
         int var3 = var5.getType();
         if (var3 == 4) {
            Object var7 = var5.getValue();
            if (var7 != null) {
               if (var7.toString().equals("true")) {
                  var4 = var4 + "1";
               } else {
                  var4 = var4 + "0";
               }
            } else {
               var4 = var4 + "0";
            }
         }
      }

      var0.setType(1);
      var0.setResult(var4);
   }

   public static synchronized void fnHonapUtolsoNapja(ExpClass var0) {
      long var8 = 0L;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 2) {
         ExpClass var10 = var0.getParameter(0);
         if (var10 == null) {
            return;
         }

         Object var11 = var10.getValue();
         if (var11 == null) {
            return;
         }

         int var2 = var10.getType();
         if (var2 != 2) {
            return;
         }

         String var3 = var10.getValue().toString();
         var10 = var0.getParameter(1);
         if (var10 == null) {
            return;
         }

         var11 = var10.getValue();
         if (var11 == null) {
            return;
         }

         var2 = var10.getType();
         if (var2 != 2) {
            return;
         }

         String var4 = var10.getValue().toString();
         int var5 = Integer.valueOf(var3);
         int var6 = Integer.valueOf(var4);
         int var7 = 31;
         formatter.setLenient(false);

         while(var7 > 24) {
            try {
               var8 = (long)(var5 * 10000 + var6 * 100 + var7);
               Date var12 = formatter.parse(String.valueOf(var8));
               break;
            } catch (Exception var13) {
               var8 = 0L;
               --var7;
            }
         }

         BigDecimal var14 = new BigDecimal(var8);
         var0.setType(2);
         var0.setResult(var14);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnNegyedevUtolsoNapja(ExpClass var0) {
      long var9 = 0L;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 2) {
         ExpClass var11 = var0.getParameter(0);
         if (var11 == null) {
            return;
         }

         Object var12 = var11.getValue();
         if (var12 == null) {
            return;
         }

         int var2 = var11.getType();
         if (var2 != 2) {
            return;
         }

         String var3 = var11.getValue().toString();
         var11 = var0.getParameter(1);
         if (var11 == null) {
            return;
         }

         var12 = var11.getValue();
         if (var12 == null) {
            return;
         }

         var2 = var11.getType();
         if (var2 != 2) {
            return;
         }

         String var4 = var11.getValue().toString();
         int var5 = Integer.valueOf(var3);
         int var6 = Integer.valueOf(var4);
         int var7 = var6 * 3;
         int var8 = 31;
         formatter.setLenient(false);

         while(var8 > 24) {
            try {
               var9 = (long)(var5 * 10000 + var7 * 100 + var8);
               Date var13 = formatter.parse(String.valueOf(var9));
               break;
            } catch (Exception var14) {
               var9 = 0L;
               --var8;
            }
         }

         BigDecimal var15 = new BigDecimal(var9);
         var0.setType(2);
         var0.setResult(var15);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnSzam(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 2) {
         Object var3 = null;
         ExpClass var2 = var0.getParameter(0);
         if (var2 != null) {
            var3 = var2.getValue();
            if (var3 != null) {
               var3 = getNumber(var0, var3);
            }
         }

         if (var3 == null) {
            var2 = var0.getParameter(1);
            if (var2 == null) {
               return;
            }

            var3 = var2.getValue();
            if (var3 == null) {
               return;
            }

            var3 = getNumber(var0, var3);
            if (var3 == null) {
               writeTypeMismatchError(-1, (Exception)null, (Object)null, var0, var2);
               return;
            }
         }

         var0.setType(2);
         var0.setResult(var3);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   private static Object getNumber(ExpClass var0, Object var1) {
      Object var2 = null;
      switch(NumericOperations.getObjectType(var1, var1)) {
      case 0:
         try {
            if (((String)var1).length() == 0) {
               return null;
            }

            var2 = NumericOperations.createNumber(var1.toString());
            break;
         } catch (Exception var4) {
            return null;
         }
      case 1:
      case 2:
      case 3:
      case 4:
      default:
         return null;
      case 5:
      case 7:
         return var1;
      case 6:
         if ((Boolean)var1) {
            var2 = new Integer(1);
         } else {
            var2 = new Integer(0);
         }
      }

      return var2;
   }

   public static synchronized void fnSubStr(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 3) {
         ExpClass var6 = var0.getParameter(0);
         if (var6 == null) {
            return;
         }

         Object var7 = var6.getValue();
         if (var7 == null) {
            return;
         }

         int var2 = var6.getType();
         if (var2 != 1) {
            return;
         }

         String var3 = var6.getValue().toString();
         var6 = var0.getParameter(1);
         if (var6 == null) {
            return;
         }

         var7 = var6.getValue();
         if (var7 == null) {
            return;
         }

         var2 = var6.getType();
         if (var2 != 2) {
            return;
         }

         String var4 = var6.getValue().toString();
         var6 = var0.getParameter(2);
         if (var6 == null) {
            return;
         }

         var7 = var6.getValue();
         if (var7 == null) {
            return;
         }

         var2 = var6.getType();
         if (var2 != 2) {
            return;
         }

         String var5 = var6.getValue().toString();
         int var8 = Integer.valueOf(var4) - 1;
         int var9 = Integer.valueOf(var5);
         int var10 = var3.length();
         if (var10 == 0) {
            var0.setType(1);
            var0.setResult("");
            return;
         }

         if (var10 < var8 + var9) {
            var9 = var10 - var8;
         }

         if (var8 > -1 && var10 >= var8 && var10 >= var8 + var9) {
            String var12 = "";
            if (var9 < 0) {
               int var11 = 0;
               if (var8 + var9 > 0) {
                  var11 = var9 + var8;
               }

               var12 = var3.substring(var11, var8 + 1);
            } else {
               var12 = var3.substring(var8, var8 + var9);
            }

            var0.setType(1);
            var0.setResult(var12);
         }
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnRightStr(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 2) {
         ExpClass var5 = var0.getParameter(0);
         if (var5 == null) {
            return;
         }

         Object var6 = var5.getValue();
         if (var6 == null) {
            return;
         }

         int var2 = var5.getType();
         if (var2 != 1) {
            return;
         }

         String var3 = var5.getValue().toString();
         var5 = var0.getParameter(1);
         if (var5 == null) {
            return;
         }

         var6 = var5.getValue();
         if (var6 == null) {
            return;
         }

         var2 = var5.getType();
         if (var2 != 2) {
            return;
         }

         String var4 = var5.getValue().toString();
         int var7 = Integer.valueOf(var4);
         int var8 = var3.length();
         if (var7 > var8) {
            var7 = var8;
         }

         if (var7 <= var8 && var7 > 0) {
            var3 = var3.substring(var8 - var7, var8);
         } else {
            var3 = "";
         }

         var0.setType(1);
         var0.setResult(var3);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnSzamString(ExpClass var0) {
      boolean var6 = false;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var7 = var0.getParameter(0);
         if (var7 == null) {
            return;
         }

         Object var8 = var7.getValue();
         if (var8 == null) {
            return;
         }

         int var2 = var7.getType();

         try {
            String var3 = var7.getValue().toString();
            double var4 = Double.valueOf(var3);
            var6 = true;
         } catch (Exception var10) {
            var6 = false;
         }

         Boolean var11;
         if (var6) {
            var11 = Boolean.TRUE;
         } else {
            var11 = Boolean.FALSE;
         }

         var0.setType(4);
         var0.setResult(var11);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnNot(ExpClass var0) {
      boolean var3 = false;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var4 = var0.getParameter(0);
         if (var4 == null) {
            return;
         }

         Object var6 = var4.getValue();
         if (var6 == null) {
            return;
         }

         int var2 = var4.getType();
         if (var2 == 4) {
            var3 = !var6.toString().equals("true");
            Boolean var5;
            if (var3) {
               var5 = Boolean.TRUE;
            } else {
               var5 = Boolean.FALSE;
            }

            var0.setType(4);
            var0.setResult(var5);
         }
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnMunkanap(ExpClass var0) {
      boolean var6 = false;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var7 = var0.getParameter(0);
         if (var7 == null) {
            return;
         }

         Object var9 = var7.getValue();
         if (var9 == null) {
            return;
         }

         int var2 = var7.getType();
         if (var2 == 2) {
            formatter.setLenient(false);
            String var4 = var9.toString();

            try {
               Date var10 = formatter.parse(var4);
               Calendar var11 = Calendar.getInstance();
               var11.setTime(var10);
               int var5 = var11.get(7);
               var6 = var5 != 1 && var5 != 7;
            } catch (Exception var12) {
               Tools.eLog(var12, 1);
            }

            List var13 = Arrays.asList("20240101", "20240315", "20240329", "20240401", "20240501", "20240520", "20240819", "20240820", "20241023", "20241101", "20241224", "20241225", "20241226", "20241227", "20250101", "20250315", "20250418", "20250421", "20250501", "20250502", "20250609", "20250820", "20251023", "20251024", "20251101", "20251224", "20251225", "20251226", "20260101", "20260315", "20260403", "20260406", "20260501", "20260525", "20260820", "20261023", "20261101", "20261225", "20261226", "20270101", "20270315", "20270326", "20270329", "20270501", "20270517", "20270820", "20271023", "20271101", "20271225", "20271226", "20280101", "20280315", "20280414", "20280417", "20280501", "20280605", "20280820", "20281023", "20281101", "20281225", "20281226", "20290101", "20290315", "20290330", "20290402", "20290501", "20290521", "20290820", "20291023", "20291101", "20291225", "20291226", "20300101", "20300315", "20300419", "20300422", "20300501", "20300610", "20300820", "20301023", "20301101", "20301225", "20301226");
            HashSet var14 = new HashSet(var13);
            if (var4.equals("20040110") || var4.equals("20041218") || var4.equals("20070310") || var4.equals("20070421") || var4.equals("20071020") || var4.equals("20071027") || var4.equals("20071222") || var4.equals("20071229") || var4.equals("20080426") || var4.equals("20081018") || var4.equals("20081220") || var4.equals("20090328") || var4.equals("20090829") || var4.equals("20091219") || var4.equals("20101211") || var4.equals("20110319") || var4.equals("20111105") || var4.equals("20120324") || var4.equals("20120421") || var4.equals("20121027") || var4.equals("20121110") || var4.equals("20121215") || var4.equals("20121201") || var4.equals("20130824") || var4.equals("20131207") || var4.equals("20131221") || var4.equals("20140510") || var4.equals("20141018") || var4.equals("20141213") || var4.equals("20150110") || var4.equals("20150808") || var4.equals("20151212") || var4.equals("20160305") || var4.equals("20161015") || var4.equals("20180310") || var4.equals("20180421") || var4.equals("20181013") || var4.equals("20181110") || var4.equals("20181201") || var4.equals("20181215") || var4.equals("20190810") || var4.equals("20191207") || var4.equals("20191214") || var4.equals("20200829") || var4.equals("20201212") || var4.equals("20211211") || var4.equals("20220326") || var4.equals("20221015") || var4.equals("20240803") || var4.equals("20241207") || var4.equals("20241214") || var4.equals("20250517") || var4.equals("20251018") || var4.equals("20251213")) {
               var6 = true;
            }

            if (var4.equals("20040101") || var4.equals("20040315") || var4.equals("20040531") || var4.equals("20041101") || var4.equals("20040102") || var4.equals("20040820") || var4.equals("20041224") || var4.equals("20050315") || var4.equals("20050328") || var4.equals("20050526") || var4.equals("20051101") || var4.equals("20051226") || var4.equals("20060315") || var4.equals("20060501") || var4.equals("20061023") || var4.equals("20061101") || var4.equals("20061225") || var4.equals("20061226") || var4.equals("20070101") || var4.equals("20070315") || var4.equals("20070316") || var4.equals("20070409") || var4.equals("20070430") || var4.equals("20070501") || var4.equals("20070528") || var4.equals("20070820") || var4.equals("20071022") || var4.equals("20071023") || var4.equals("20071101") || var4.equals("20071102") || var4.equals("20071224") || var4.equals("20071225") || var4.equals("20071226") || var4.equals("20071231") || var4.equals("20080101") || var4.equals("20080324") || var4.equals("20080501") || var4.equals("20080502") || var4.equals("20080512") || var4.equals("20080820") || var4.equals("20081023") || var4.equals("20081024") || var4.equals("20081224") || var4.equals("20081225") || var4.equals("20081226") || var4.equals("20090101") || var4.equals("20090102") || var4.equals("20090413") || var4.equals("20090501") || var4.equals("20090601") || var4.equals("20090820") || var4.equals("20090821") || var4.equals("20091023") || var4.equals("20091224") || var4.equals("20091225") || var4.equals("20100101") || var4.equals("20100315") || var4.equals("20100405") || var4.equals("20100501") || var4.equals("20100524") || var4.equals("20100820") || var4.equals("20101023") || var4.equals("20101101") || var4.equals("20101224") || var4.equals("20101225") || var4.equals("20101226") || var4.equals("20110101") || var4.equals("20110314") || var4.equals("20110315") || var4.equals("20110425") || var4.equals("20110501") || var4.equals("20110613") || var4.equals("20110820") || var4.equals("20111023") || var4.equals("20111031") || var4.equals("20111101") || var4.equals("20111225") || var4.equals("20111226") || var4.equals("20120101") || var4.equals("20120315") || var4.equals("20120501") || var4.equals("20120820") || var4.equals("20121023") || var4.equals("20121101") || var4.equals("20121225") || var4.equals("20121226") || var4.equals("20120316") || var4.equals("20120409") || var4.equals("20120430") || var4.equals("20120528") || var4.equals("20121022") || var4.equals("20121102") || var4.equals("20121224") || var4.equals("20121231") || var4.equals("20130101") || var4.equals("20130315") || var4.equals("20130401") || var4.equals("20130501") || var4.equals("20130520") || var4.equals("20130819") || var4.equals("20130820") || var4.equals("20131023") || var4.equals("20131101") || var4.equals("20131224") || var4.equals("20131225") || var4.equals("20131226") || var4.equals("20131227") || var4.equals("20140101") || var4.equals("20140315") || var4.equals("20140501") || var4.equals("20140820") || var4.equals("20141023") || var4.equals("20141101") || var4.equals("20141225") || var4.equals("20141226") || var4.equals("20140421") || var4.equals("20140502") || var4.equals("20140609") || var4.equals("20141024") || var4.equals("20141224") || var4.equals("20150101") || var4.equals("20150315") || var4.equals("20150501") || var4.equals("20150820") || var4.equals("20151023") || var4.equals("20151101") || var4.equals("20151225") || var4.equals("20151226") || var4.equals("20150102") || var4.equals("20150406") || var4.equals("20150525") || var4.equals("20150821") || var4.equals("20151224") || var4.equals("20160101") || var4.equals("20160315") || var4.equals("20160501") || var4.equals("20160820") || var4.equals("20161023") || var4.equals("20161101") || var4.equals("20161225") || var4.equals("20161226") || var4.equals("20160314") || var4.equals("20160328") || var4.equals("20160516") || var4.equals("20161031") || var4.equals("20170101") || var4.equals("20170315") || var4.equals("20170414") || var4.equals("20170417") || var4.equals("20170501") || var4.equals("20170605") || var4.equals("20170820") || var4.equals("20171023") || var4.equals("20171101") || var4.equals("20171225") || var4.equals("20171226") || var4.equals("20180101") || var4.equals("20180315") || var4.equals("20180316") || var4.equals("20180330") || var4.equals("20180402") || var4.equals("20180430") || var4.equals("20180501") || var4.equals("20180521") || var4.equals("20180820") || var4.equals("20181022") || var4.equals("20181023") || var4.equals("20181101") || var4.equals("20181102") || var4.equals("20181224") || var4.equals("20181225") || var4.equals("20181226") || var4.equals("20181231") || var4.equals("20190101") || var4.equals("20190315") || var4.equals("20190419") || var4.equals("20190422") || var4.equals("20190501") || var4.equals("20190610") || var4.equals("20190819") || var4.equals("20190820") || var4.equals("20191023") || var4.equals("20191101") || var4.equals("20191224") || var4.equals("20191225") || var4.equals("20191226") || var4.equals("20191227") || var4.equals("20191228") || var4.equals("20191229") || var4.equals("20200101") || var4.equals("20200410") || var4.equals("20200413") || var4.equals("20200501") || var4.equals("20200601") || var4.equals("20200820") || var4.equals("20200821") || var4.equals("20201023") || var4.equals("20201224") || var4.equals("20201225") || var4.equals("20210101") || var4.equals("20210315") || var4.equals("20210402") || var4.equals("20210405") || var4.equals("20210524") || var4.equals("20210820") || var4.equals("20211101") || var4.equals("20211224") || var4.equals("20220314") || var4.equals("20220315") || var4.equals("20220415") || var4.equals("20220418") || var4.equals("20220606") || var4.equals("20221031") || var4.equals("20221101") || var4.equals("20221226") || var4.equals("20230315") || var4.equals("20230407") || var4.equals("20230410") || var4.equals("20230501") || var4.equals("20230529") || var4.equals("20231023") || var4.equals("20231101") || var4.equals("20231225") || var4.equals("20231226") || var14.contains(var4)) {
               var6 = false;
            }
         }

         Boolean var8;
         if (var6) {
            var8 = Boolean.TRUE;
         } else {
            var8 = Boolean.FALSE;
         }

         var0.setType(4);
         var0.setResult(var8);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   private static String third_block(String var0) {
      String[] var1 = new String[]{"", "egy", "kettő", "három", "négy", "öt", "hat", "hét", "nyolc", "kilenc"};
      String[] var2 = new String[]{"", "tizen", "huszon", "harminc", "negyven", "ötven", "hatvan", "hetven", "nyolcvan", "kilencven"};
      String[] var3 = new String[]{"", "egyszáz", "kétszáz", "háromszáz", "négyszáz", "ötszáz", "hatszáz", "hétszáz", "nyolcszáz", "kilencszáz"};
      String var4 = "";

      for(int var5 = 2; var5 > -1; --var5) {
         int var6 = Integer.parseInt(var0.substring(var5, var5 + 1));
         if (var5 == 2) {
            var4 = var1[var6];
         }

         if (var5 == 1) {
            if (var4.length() != 0 || var6 != 1 && var6 != 2) {
               var4 = var2[var6] + var4;
            } else {
               var4 = var6 == 1 ? "tíz" : "húsz";
            }
         }

         if (var5 == 0) {
            var4 = var3[var6] + var4;
         }
      }

      return var4;
   }

   private static String getNum2Str(double var0) {
      String var2 = "";
      String var4 = "";
      String var5 = "";
      long var8 = Math.round(var0);
      if (var8 < 0L) {
         var2 = "mínusz ";
         var8 = -var8;
      }

      if (var8 > 2000L) {
         var4 = "-";
      }

      String var6 = "000000000000000000" + Long.toString(var8);
      int var10 = var6.length();
      var6 = var6.substring(var10 - 18, var10);
      int var11 = 1;

      for(int var12 = 15; var12 >= 0; var12 -= 3) {
         String var7 = third_block(var6.substring(var12, var12 + 3));
         if (var11 == 1) {
            var5 = var7;
         }

         if (var11 == 2 && var7.length() > 0) {
            var5 = var7 + "ezer" + var4 + var5;
         }

         if (var11 == 3 && var7.length() > 0) {
            var5 = var7 + "millió" + var4 + var5;
         }

         if (var11 == 4 && var7.length() > 0) {
            var5 = var7 + "milliárd" + var4 + var5;
         }

         if (var11 == 5 && var7.length() > 0) {
            var5 = var7 + "billió" + var4 + var5;
         }

         if (var11 == 6 && var7.length() > 0) {
            var5 = var7 + "billiárd" + var4 + var5;
         }

         ++var11;
      }

      var6 = var5.length() == 0 ? "nulla" : var2 + var5;
      if (var6.substring(var6.length() - 1, var6.length()).equals("-")) {
         var6 = var6.substring(0, var6.length() - 1);
      }

      return var6;
   }

   public static synchronized void fnNum2Str(ExpClass var0) {
      String var4 = "";
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var5 = var0.getParameter(0);
         if (var5 == null) {
            return;
         }

         Object var7 = var5.getValue();
         if (var7 == null) {
            return;
         }

         int var2 = var5.getType();
         if (var2 == 2) {
            String var3 = var7.toString();
            var4 = getNum2Str(Double.parseDouble(var3));
         }

         var0.setType(1);
         var0.setResult(var4);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnMainap(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();

      int var2;
      try {
         formatter.setLenient(false);
         var2 = Integer.parseInt(formatter.format(new Date()));
      } catch (Exception var5) {
         var2 = 0;
      }

      Integer var3 = new Integer(var2);
      var0.setType(2);
      var0.setResult(var3);
   }

   public static synchronized void fnModulo(ExpClass var0) {
      BigDecimal var4 = null;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 2) {
         ExpClass var5 = var0.getParameter(0);
         if (var5 == null) {
            return;
         }

         Object var7 = var5.getValue();
         if (var7 == null) {
            return;
         }

         int var2 = var5.getType();
         var5 = var0.getParameter(1);
         if (var5 == null) {
            return;
         }

         Object var8 = var5.getValue();
         if (var8 == null) {
            return;
         }

         int var3 = var5.getType();
         if (var2 == 2 && var3 == 2) {
            try {
               var4 = abevReminder(var7, var8);
            } catch (Exception var10) {
               writeError(var0, ERR_FN_ID, "Számítási hiba!  (" + var7 + " modulo " + var8 + ")", var10, (Object)null);
               Integer var6 = new Integer(Integer.MIN_VALUE);
               var0.setType(2);
               var0.setResult(var6);
               return;
            }
         }

         BigDecimal var11 = NumericOperations.tryIntegerNumberFormat(var4 == null ? null : var4.doubleValue());
         var0.setType(2);
         var0.setResult(var11);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   private static BigDecimal abevReminder(Object var0, Object var1) {
      BigDecimal var2 = NumericOperations.getBigDecimal(var0);
      BigDecimal var3 = NumericOperations.getBigDecimal(var1);
      return var2.remainder(var3).stripTrailingZeros();
   }

   public static synchronized void fnHaN(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      ExpClass var3 = null;
      int var5 = 0;

      while(true) {
         if (var5 < var1 - 1) {
            ExpClass var2 = var0.getParameter(var5);
            if (var2 == null) {
               return;
            }

            Object var4 = var2.getValue();
            if (var4 == null) {
               return;
            }

            if (NumericOperations.getObjectType(var4, var4) != 6) {
               writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
               return;
            }

            if (!(Boolean)var4) {
               var5 += 2;
               continue;
            }

            var3 = var0.getParameter(var5 + 1);
         }

         if (var3 == null) {
            var3 = var0.getParameter(var1 - 1);
         }

         var0.setType(var3.getType());
         var5 = var3.getFlag();
         var0.setFlag(var5);
         var0.setDontModify(var3.isDontModify());
         Object var6 = var3.getResult();
         var0.setResult(var6);
         return;
      }
   }

   public static synchronized void fnSpec_fgv4(ExpClass var0) {
      byte var6 = 25;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 2) {
         ExpClass var7 = var0.getParameter(0);
         if (var7 == null) {
            return;
         }

         Object var9 = var7.getValue();
         if (var9 == null) {
            return;
         }

         int var2 = var7.getType();
         var7 = var0.getParameter(1);
         if (var7 == null) {
            return;
         }

         Object var10 = var7.getValue();
         if (var10 == null) {
            return;
         }

         int var3 = var7.getType();
         if (var2 == 2 && var3 == 2) {
            int var4 = NumericOperations.getInteger(var9);
            int var5 = NumericOperations.getInteger(var10);
            if (var4 == var5 || var4 == var5 * 2 || var4 == var5 * 3) {
               var6 = 20;
            }
         } else {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
         }

         Integer var8 = new Integer(var6);
         var0.setType(2);
         var0.setResult(var8);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   private static boolean belso_fgv(String var0, String var1) {
      boolean var2 = true;

      for(int var3 = 0; var3 < var0.length(); ++var3) {
         if (!var0.substring(var3, var3 + 1).equals(var1)) {
            var2 = false;
            break;
         }
      }

      return var2;
   }

   private static String cserel_spec_fgv(String var0, String var1, String var2) {
      String var3 = "";

      for(int var4 = 0; var4 < var0.length(); ++var4) {
         if (var0.substring(var4, var4 + 1).equals(var1)) {
            var3 = var3 + var2;
         } else {
            var3 = var3 + var0.substring(var4, var4 + 1);
         }
      }

      return var3;
   }

   public static synchronized void fnSpec_fgv3(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      String var6 = "";

      for(int var7 = 0; var7 < var1; ++var7) {
         ExpClass var3 = var0.getParameter(var7);
         if (var3 == null) {
            return;
         }

         Object var5 = var3.getValue();
         if (var5 == null) {
            return;
         }

         int var2 = var3.getType();
         if (var2 != 2) {
            writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
            return;
         }

         var6 = var6 + var5.toString();
      }

      String var10 = "4";
      String var8 = var6;
      var6 = "";

      for(int var9 = 0; var9 < var8.length(); ++var9) {
         if (!var8.substring(var9, var9 + 1).equals("0")) {
            var6 = var6 + var8.substring(var9, var9 + 1);
         }
      }

      String var11 = cserel_spec_fgv(var6, "4", "1");
      String var4;
      if (belso_fgv(var11, "1")) {
         var4 = "1";
         var0.setType(2);
         var0.setResult(var4);
      } else {
         var11 = cserel_spec_fgv(var6, "4", "2");
         if (belso_fgv(var11, "2")) {
            var4 = "2";
            var0.setType(2);
            var0.setResult(var4);
         } else if (belso_fgv(var6, "3")) {
            var4 = "3";
            var0.setType(2);
            var0.setResult(var4);
         } else {
            var0.setType(2);
            var0.setResult(var10);
         }
      }
   }

   public static synchronized void fnNagybetus(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var3 = var0.getParameter(0);
         String var4 = "";
         if (var3 != null && var3.getValue() != null) {
            int var2 = var3.getType();
            if (var2 == 1) {
               var4 = var3.getValue().toString().toUpperCase();
            }
         }

         var0.setType(1);
         var0.setResult(var4);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnPos(ExpClass var0) {
      boolean var4 = false;
      String var5 = "";
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 2) {
         ExpClass var6 = var0.getParameter(0);
         if (var6 == null) {
            return;
         }

         Object var8 = var6.getValue();
         if (var8 == null) {
            return;
         }

         int var2 = var6.getType();
         var6 = var0.getParameter(1);
         if (var6 == null) {
            return;
         }

         Object var9 = var6.getValue();
         if (var9 == null) {
            return;
         }

         int var3 = var6.getType();
         if (var2 != 1 || var3 != 1) {
            writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
            return;
         }

         var5 = var9.toString();
         if (var8.toString().length() == 0) {
            return;
         }

         int var10 = var5.indexOf(var8.toString()) + 1;
         Integer var7 = new Integer(var10);
         var0.setType(2);
         var0.setResult(var7);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnSpec_fgv5(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 2) {
         try {
            ExpClass var2 = var0.getParameter(0);
            if (var2 == null) {
               return;
            }

            Object var3 = var2.getValue();
            if (var3 == null) {
               return;
            }

            int var4 = var2.getType();
            if (var4 == 0) {
               return;
            }

            var2 = var0.getParameter(1);
            if (var2 == null) {
               return;
            }

            Object var5 = var2.getValue();
            if (var5 == null) {
               return;
            }

            int var6 = var2.getType();
            if (var6 == 0) {
               return;
            }

            String var7;
            String var8;
            if (test) {
               var7 = "[170]_A[1..6]";
               var8 = "[170]_D[1..6]";
            } else {
               var7 = "[" + var5 + "]_A[1..20]";
               var8 = "[" + var5 + "]_D[1..20]";
            }

            Vector var9 = getCachedFieldFids(var0, var7);
            Vector var10 = getCachedFieldFids(var0, var8);
            Object var11 = new Integer(0);

            for(int var12 = 0; var12 < var9.size(); ++var12) {
               String var13 = (String)var9.elementAt(var12);
               ExpClass var14 = getFieldValue(var0, var13);
               Object var15 = var14.getValue();
               int var16 = var14.getType();
               if (var16 != 0) {
                  if (var16 != var4) {
                     writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
                     return;
                  }

                  if (var15.equals(var3)) {
                     ExpClass var17 = getFieldValue(var0, (String)var10.get(var12));
                     Object var18 = var17.getValue();
                     int var19 = var17.getType();
                     if (var19 != 0) {
                        try {
                           var11 = NumericOperations.add(var11, var18);
                        } catch (Exception var21) {
                           writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
                           return;
                        }
                     }
                  }
               }
            }

            var0.setType(2);
            var0.setResult(var11);
         } catch (Exception var22) {
            writeError(var0, ERR_FN_ID, var22.getMessage(), (Exception)null, (Object)null);
         }
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnSpec_fgv6(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);

      try {
         ExpClass var1 = var0.getParameter(0);
         if (var1 == null) {
            return;
         }

         Object var2 = var1.getValue();
         if (var2 == null) {
            return;
         }

         int var3 = var1.getType();
         if (var3 == 0) {
            return;
         }

         var1 = var0.getParameter(1);
         if (var1 == null) {
            return;
         }

         Object var4 = var1.getValue();
         if (var4 == null) {
            return;
         }

         int var5 = var1.getType();
         if (var5 == 0) {
            return;
         }

         Object[] var6;
         if (test) {
            var6 = new Object[]{"[02170]_A[1..6]", "[02170]_D[1..6]"};
         } else {
            var6 = new Object[]{"[" + var4 + "]_A[1..20]", "[" + var4 + "]_E[1..20]"};
         }

         boolean var7 = uniqPairs(var0, var6, new boolean[]{true, true}, false, 0);
         var0.setType(4);
         var0.setResult(var7);
      } catch (Exception var8) {
         writeError(var0, ERR_FN_ID, var8.getMessage(), (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnSpec_fgv7(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);

      try {
         ExpClass var1 = var0.getParameter(0);
         if (var1 == null) {
            return;
         }

         Object var5 = var1.getValue();
         if (var5 == null) {
            return;
         }

         int var6 = var1.getType();
         if (var6 == 0) {
            return;
         }

         var1 = var0.getParameter(1);
         if (var1 == null) {
            return;
         }

         Object var7 = var1.getValue();
         if (var7 == null) {
            return;
         }

         int var8 = var1.getType();
         if (var8 == 0) {
            return;
         }

         String var2;
         String var3;
         byte var4;
         if (test) {
            var2 = "[02110]_B[4..6]";
            var3 = "[02110]_C[1..6]";
            var4 = 3;
         } else {
            var2 = "[" + var7 + "]_B[17..20]";
            var3 = "[" + var7 + "]_C[1..20]";
            var4 = 16;
         }

         boolean var9 = specDiff(var0, var2, var3, var4);
         var0.setType(4);
         var0.setResult(var9);
      } catch (Exception var10) {
         writeError(var0, ERR_FN_ID, var10.getMessage(), (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnSpec_fgv8(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);

      try {
         ExpClass var1 = var0.getParameter(0);
         if (var1 == null) {
            return;
         }

         Object var5 = var1.getValue();
         if (var5 == null) {
            return;
         }

         int var6 = var1.getType();
         if (var6 == 0) {
            return;
         }

         var1 = var0.getParameter(1);
         if (var1 == null) {
            return;
         }

         Object var7 = var1.getValue();
         if (var7 == null) {
            return;
         }

         int var8 = var1.getType();
         if (var8 == 0) {
            return;
         }

         String var2;
         String var3;
         byte var4;
         if (test) {
            var2 = "[02110]_B[4..6]";
            var3 = "[02110]_C[1..6]";
            var4 = 3;
         } else {
            var2 = "[" + var7 + "]_B[17..20]";
            var3 = "[" + var7 + "]_C[1..20]";
            var4 = 16;
         }

         boolean var9 = specDiff(var0, var2, var3, var4);
         var0.setType(4);
         var0.setResult(var9);
      } catch (Exception var10) {
         writeError(var0, ERR_FN_ID, var10.getMessage(), (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnSpec_fgv9(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);

      try {
         ExpClass var1 = var0.getParameter(0);
         if (var1 == null) {
            return;
         }

         Object var2 = var1.getValue();
         if (var2 == null) {
            return;
         }

         int var3 = var1.getType();
         if (var3 == 0) {
            return;
         }

         var1 = var0.getParameter(1);
         if (var1 == null) {
            return;
         }

         Object var4 = var1.getValue();
         if (var4 == null) {
            return;
         }

         int var5 = var1.getType();
         if (var5 == 0) {
            return;
         }

         Object[] var6;
         if (test) {
            var6 = new Object[]{"[02170]_A[1..6]", "[02170]_D[1..6]"};
         } else {
            var6 = new Object[]{"[" + var4 + "]_A[1..20]", "[" + var4 + "]_H[1..20]"};
         }

         boolean var7 = uniqPairs(var0, var6, new boolean[]{true, false}, false, 2);
         var0.setType(4);
         var0.setResult(var7);
      } catch (Exception var8) {
         writeError(var0, ERR_FN_ID, var8.getMessage(), (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnSpec_fgv10(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         try {
            ExpClass var2 = var0.getParameter(0);
            if (var2 == null) {
               return;
            }

            Object var3 = var2.getValue();
            if (var3 == null) {
               return;
            }

            int var4 = var2.getType();
            var2 = var0.getParameter(1);
            if (var2 == null) {
               return;
            }

            Object var5 = var2.getValue();
            if (var5 == null) {
               return;
            }

            int var6 = var2.getType();
            if (var6 == 0) {
               return;
            }

            String var7;
            if (test) {
               var7 = "[02170]_A[1..6]";
            } else {
               var7 = "[" + var5 + "]_A[1..20]";
            }

            boolean var8 = searchValue(var0, var7, var3, var4);
            var0.setType(4);
            var0.setResult(var8);
         } catch (Exception var9) {
            writeError(var0, ERR_FN_ID, var9.getMessage(), (Exception)null, (Object)null);
         }
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnSpec_fgv1(ExpClass var0) {
      String var1 = "specmatrix";
      short var2 = 1995;
      String var3 = ",";
      boolean var8 = false;
      var0.setType(0);
      var0.setResult((Object)null);
      if (var0.getParametersCount() < 3) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            boolean var9 = false;
            Integer var7 = NumericOperations.getInteger(getParameter(var0, 0, true, 2, var9));
            String var4 = (String)getParameter(var0, 1, true, 1, var9);
            String var5 = (String)getParameter(var0, 2, true, 1, var9);
            if (var7 == null || var4 == null || var5 == null) {
               return;
            }

            String var6 = chkInSpec_1_2(var7, var5, var2);
            if (var6 != null) {
               MatrixSearchModel var10 = new MatrixSearchModel(var1, var3);
               var10.addSearchItem(new MatrixSearchItem(0, "=", var6, false));
               var10.addSearchItem(new MatrixSearchItem(2, "=", var4, false));
               Vector var11 = matrixHandler.search(getFormid(), var10, true, false);
               if (var11 != null) {
                  var8 = true;
               }

               var0.setResult(var8);
               var0.setType(4);
            }
         } catch (Exception var12) {
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var12.getMessage(), var12, var0);
         }

      }
   }

   public static synchronized void fnSpec_fgv2(ExpClass var0) {
      String var1 = "specmatrix";
      short var2 = 1995;
      String var3 = ",";
      boolean var10 = false;
      var0.setType(0);
      var0.setResult((Object)null);
      if (var0.getParametersCount() < 4) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            boolean var11 = false;
            Integer var9 = NumericOperations.getInteger(getParameter(var0, 0, true, 2, var11));
            String var7 = (String)getParameter(var0, 1, true, 1, var11);
            String var5 = (String)getParameter(var0, 2, true, 1, var11);
            Object var8 = getParameter(var0, 3, false, 0, var11);
            if (var9 == null || var8 == null || var5 == null || var7 == null) {
               return;
            }

            String var4 = var8.toString();
            if (var4.length() == 0) {
               return;
            }

            String var6 = chkInSpec_1_2(var9, var5, var2);
            if (var6 != null) {
               MatrixSearchModel var12 = new MatrixSearchModel(var1, var3);
               var12.addSearchItem(new MatrixSearchItem(0, "=", var6, false));
               var12.addSearchItem(new MatrixSearchItem(1, "=", var4, false));
               var12.addSearchItem(new MatrixSearchItem(2, "=", var7, false));
               Vector var13 = matrixHandler.search(getFormid(), var12, true, false);
               if (var13 != null) {
                  var10 = true;
               }

               var0.setResult(var10);
               var0.setType(4);
            }
         } catch (Exception var14) {
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var14.getMessage(), var14, var0);
         }

      }
   }

   public static synchronized void fnKereses_matrixban(ExpClass var0) {
      byte var1 = 4;
      byte var2 = 3;
      var0.setType(0);
      var0.setResult((Object)null);
      int var3 = var0.getParametersCount();
      if (var1 + 2 > var3) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            boolean var9 = false;
            String var5 = ((String)getParameter(var0, 0, true, 1, var9)).toLowerCase();
            Boolean var7 = (Boolean)getParameter(var0, 1, true, 4, var9);
            Integer var4 = NumericOperations.getInteger(getParameter(var0, 2, true, 2, var9));
            getParameter(var0, 3, false, 0, var9);
            String var6 = ",";
            if (var5 == null || var7 == null || var4 == null) {
               return;
            }

            getFormMatrixData(var0, var5, var7, false, true, var4, var6, var1, var2, var3);
         } catch (Exception var10) {
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var10.getMessage(), var10, var0);
         }

      }
   }

   public static synchronized void fnKereses_matrixban2(ExpClass var0) {
      byte var1 = 5;
      byte var2 = 3;
      var0.setType(0);
      var0.setResult((Object)null);
      int var3 = var0.getParametersCount();
      if (var1 + 2 > var3) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            boolean var9 = false;
            String var5 = ((String)getParameter(var0, 0, true, 1, var9)).toLowerCase();
            Boolean var7 = (Boolean)getParameter(var0, 1, true, 4, var9);
            Integer var4 = NumericOperations.getInteger(getParameter(var0, 2, true, 2, var9));
            getParameter(var0, 3, false, 0, var9);
            String var6 = (String)getParameter(var0, 4, true, 1, var9);
            if (var5 == null || var7 == null || var4 == null || var6 == null) {
               return;
            }

            getFormMatrixData(var0, var5, var7, false, true, var4, var6, var1, var2, var3);
         } catch (Exception var10) {
            var10.printStackTrace();
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var10.getMessage(), var10, var0);
         }

      }
   }

   public static synchronized void fnOszlop_sziget(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      if (var0.getParametersCount() != 1) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            boolean var2 = false;
            String var1 = (String)getParameter(var0, 0, true, 1, var2);
            if (var1 == null) {
               return;
            }

            Vector var3 = getCachedFieldFids(var0, var1);
            if (var3 != null && var3.size() != 0) {
               String var4 = getFlagValue(var0, var3, (Integer)null);
               boolean var5 = true;
               if (test) {
                  System.out.println("res = " + var4);
               }

               if (var4.indexOf(49) > -1) {
                  var4 = var4.substring(var4.indexOf(49), var4.lastIndexOf(49) + 1);
                  if (var4.indexOf(48) > -1) {
                     var5 = false;
                  }
               }

               var0.setType(4);
               var0.setResult(var5);
            }
         } catch (Exception var6) {
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var6.getMessage(), var6, var0);
         }

      }
   }

   public static synchronized void fnOszlopfolytonos(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      if (var0.getParametersCount() != 1) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            boolean var2 = false;
            String var1 = (String)getParameter(var0, 0, true, 1, var2);
            if (var1 == null) {
               return;
            }

            boolean var3 = true;
            String var4 = getDynamicFlagValue(var0, var1);
            if (var4 != null) {
               if (var4.indexOf(49) > -1) {
                  var4 = var4.substring(0, var4.lastIndexOf(49) + 1);
                  if (var4.indexOf(48) > -1) {
                     var3 = false;
                  }
               }

               var0.setType(4);
               var0.setResult(var3);
            }
         } catch (Exception var5) {
            var5.printStackTrace();
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var5.getMessage(), var5, var0);
         }

      }
   }

   private static String getDynamicFlagValue(ExpClass var0, String var1) throws Exception {
      StringBuffer var2 = new StringBuffer();
      Vector var3 = getCachedFieldFids(var0, var1);
      if (var3 != null && var3.size() != 0) {
         if (g_current_field_id != null) {
            if (FIdHelper.isFieldDynamic(g_current_field_id)) {
               var2.append(getFlagValue(var0, var3, (Integer)null));
            } else {
               getDynFlagValue(var0, var1, var3, var2);
            }
         } else {
            getDynFlagValue(var0, var1, var3, var2);
         }
      }

      return var2.length() == 0 ? null : var2.toString();
   }

   private static void getDynFlagValue(ExpClass var0, String var1, Vector var2, StringBuffer var3) throws Exception {
      Object[] var4 = convABEVtableIDs(var1);
      String var5 = (String)var4[0];
      PageModel var6 = getPageByName(var0, var5);
      Integer var7 = getPageDeepCount(var0, var5, var6);
      if (var7 != null) {
         for(Integer var8 = 0; var8 < var7; var8 = var8 + 1) {
            String var9 = getFlagValue(var0, var2, var8);
            if (var9 != null) {
               var3.append(var9);
            }
         }
      }

   }

   public static synchronized void fnOszlopunique(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      byte var1 = -1;
      Integer var2 = new Integer(1);
      Vector var3 = new Vector(1);
      boolean var4 = g_current_field_id != null;
      int var8 = var0.getParametersCount();
      if (var8 >= 1 && var8 <= 2) {
         int var5;
         Integer var6;
         String var7;
         int var13;
         try {
            String var9 = (String)getParameter(var0, 0, true, 1, true);
            if (var8 == 2) {
               var2 = NumericOperations.getInteger(getParameter(var0, 1, true, 2, true));
            }

            if (var9 == null || var2 == null) {
               writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
               return;
            }

            Vector var10 = getCachedFieldFids(var0, var9);
            var3.add(var10);
            var13 = checkRowCount(var1, var10);
            Object[] var11 = convABEVtableIDs(var9);
            var7 = (String)var11[0];
            var5 = getPageCounter(var0, var7);
            var6 = var4 ? FIdHelper.getDPageNumber(getSelf()) : 0;
         } catch (Exception var12) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, var12.getMessage());
            return;
         }

         tablazatUnique(var0, Boolean.TRUE, var2, var3, var7, var13, var5, var6, var4);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }
   }

   public static synchronized void fnOszlopunique_old(ExpClass var0) {
      int var3 = 1;
      var0.setType(0);
      var0.setResult((Object)null);
      int var4 = var0.getParametersCount();
      if (var4 >= 1 && var4 <= 2) {
         try {
            boolean var5 = false;
            String var1 = (String)getParameter(var0, 0, true, 1, var5);
            if (var1 == null) {
               return;
            }

            if (var4 == 2) {
               Integer var2 = NumericOperations.getInteger(getParameter(var0, 1, true, 2, var5));
               if (var2 == null) {
                  return;
               }

               var3 = var2;
            }

            DefaultListModel var6 = getDynamicSortedListOfValue(var0, var1);
            if (var6 != null) {
               int var7 = 0;
               int var8 = 0;

               int var9;
               for(var9 = 0; var9 <= var3 && var7 < var6.getSize(); var7 = var8) {
                  while(var8 < var6.getSize() && var6.getElementAt(var7).equals(var6.getElementAt(var8))) {
                     ++var8;
                  }

                  var9 = var8 - var7;
               }

               boolean var10 = var9 <= var3;
               var0.setType(4);
               var0.setResult(var10);
            }
         } catch (Exception var11) {
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var11.getMessage(), var11, var0);
         }

      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }
   }

   public static synchronized void fnElsoKitoltott(ExpClass var0) {
      getFilled(var0, false);
   }

   public static synchronized void fnUtolsoKitoltott(ExpClass var0) {
      getFilled(var0, true);
   }

   public static synchronized void fnDkitoltottekszama(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      if (var0.getParametersCount() != 1) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            boolean var2 = false;
            String var1 = (String)getParameter(var0, 0, true, 1, var2);
            if (var1 == null) {
               return;
            }

            int var3 = 0;
            Object[] var4 = convABEVtableIDs(var1);
            String var5 = (String)var4[0];
            PageModel var6 = getPageByName(var0, var5);
            if (var6 == null) {
               return;
            }

            if (getPageIsDynamic(var0, var5, var6)) {
               Vector var7 = getCachedFieldFids(var0, var1);
               Integer var8 = getPageDeepCount(var0, var5, var6);
               if (var8 != null) {
                  for(Integer var9 = 0; var9 < var8; var9 = var9 + 1) {
                     if (var7 != null && var7.size() != 0) {
                        String var10 = getFlagValue(var0, var7, var9);
                        if (var10 != null) {
                           byte[] var11 = var10.getBytes();

                           for(int var12 = 0; var12 < var11.length; ++var12) {
                              if (var11[var12] == 49) {
                                 ++var3;
                              }
                           }
                        }
                     }
                  }
               }
            }

            var0.setType(2);
            var0.setResult(new Integer(var3));
         } catch (Exception var13) {
            var13.printStackTrace();
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var13.getMessage(), var13, var0);
         }

      }
   }

   public static synchronized void fnSor(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      if (var0.getParametersCount() != 1) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            boolean var2 = false;
            String var1 = (String)getParameter(var0, 0, true, 1, var2);
            if (var1 == null || var1.length() == 0) {
               return;
            }

            String var3 = getRowNumByVid(var1, getFormid());
            if (var3 != null) {
               var0.setType(2);
               var0.setResult(new Integer(var3));
            }
         } catch (Exception var4) {
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var4.getMessage(), var4, var0);
         }

      }
   }

   public static synchronized void fnOszlop(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      if (var0.getParametersCount() != 1) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            boolean var2 = false;
            String var1 = (String)getParameter(var0, 0, true, 1, var2);
            if (var1 == null || var1.length() == 0) {
               return;
            }

            String var3 = getColNumByVid(var1, getFormid());
            if (var3 != null) {
               var0.setType(2);
               var0.setResult(new Integer(var3));
            }
         } catch (Exception var4) {
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var4.getMessage(), var4, var0);
         }

      }
   }

   public static synchronized void fnUtolsoLap(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      if (var0.getParametersCount() != 1) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            boolean var3 = false;
            String var1 = (String)getParameter(var0, 0, true, 1, var3);
            if (var1 == null) {
               return;
            }

            String var4 = getPageActPageName(var0);
            if (var4 == null) {
               var0.setType(4);
               var0.setResult(Boolean.TRUE);
               return;
            }

            byte var5 = -1;
            if (getRealPageName(var1).equalsIgnoreCase(var4)) {
               PageModel var6 = getPageByName(var0, var1);
               if (var6 == null) {
                  return;
               }

               if (getPageIsDynamic(var0, var1, var6)) {
                  Integer var2 = getPageDeepCount(var0, var1, var6);
                  if (var2 != null && var2 instanceof Integer) {
                     int var7 = (Integer)var2;
                     var2 = FIdHelper.getDPageNumber(getSelf());
                     if (var2 != null && var2 instanceof Integer) {
                        int var8 = (Integer)var2;
                        if (var8 == var7 - 1) {
                           var5 = 1;
                        } else {
                           var5 = 0;
                        }
                     }
                  }
               } else {
                  writeError(var0, ID_EX_FN_PAGE_DYN, "Program hiba, nem dinamikus a lap!", (Exception)null, var0);
               }
            } else {
               var5 = 0;
            }

            if (var5 < 0) {
               return;
            }

            var0.setType(4);
            var0.setResult(var5 == 1);
         } catch (Exception var9) {
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var9.getMessage(), var9, var0);
         }

      }
   }

   public static synchronized void fnElsodlap(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      if (var0.getParametersCount() != 3) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            boolean var6 = false;
            Object var1 = getParameter(var0, 0, false, 2, var6);
            if (var1 == null) {
               return;
            }

            Integer var3 = NumericOperations.getInteger(getParameter(var0, 1, true, 2, var6));
            if (var3 == null) {
               return;
            }

            int var4 = var3;
            String var5 = (String)getParameter(var0, 2, true, 1, var6);
            if (var5 == null) {
               return;
            }

            int var7 = getExpType(var0, 0);
            var0.setType(var7);
            if (var4 == 1) {
               var0.setResult(var1);
            } else {
               switch(var7) {
               case 1:
                  var0.setResult("0");
                  break;
               case 2:
                  var0.setResult(new Integer(0));
               }
            }
         } catch (Exception var8) {
            var8.printStackTrace();
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var8.getMessage(), var8, var0);
         }

      }
   }

   public static boolean isLogikaiNullToFalse(Object var0, Object var1, int var2, String var3) {
      return 4 == var2 && var3 == null && var1.toString().compareTo(var0.toString()) == 0 && var0.toString().equalsIgnoreCase("false");
   }

   public static String getRawFValue(String var0, Integer var1) {
      try {
         IDataStore var2 = g_active_data_store;
         if (var2 != null) {
            try {
               Object[] var3;
               if (var1 == null) {
                  var3 = FIdHelper.getDataStoreKey(var0);
               } else {
                  g_ds_key[0] = var1;
                  g_ds_key[1] = var0 == null ? "" : var0;
                  var3 = g_ds_key;
               }

               g_exp_fields.add((String)var3[1], (Integer)var3[0]);
               return var2.get(var3);
            } catch (Exception var5) {
               Tools.eLog(var5, 1);
            }
         }
      } catch (Exception var6) {
         Tools.eLog(var6, 1);
      }

      return null;
   }

   public static synchronized void fnFeltetelesErtek(ExpClass var0) {
      String var7 = null;
      var0.setType(4);
      var0.setResult(Boolean.TRUE);
      int var8 = var0.getParametersCount();
      if (var8 != 3 && var8 != 4) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            boolean var9 = false;
            Object var1 = getParameter(var0, 0, true, 4, var9);
            if (var1 == null) {
               return;
            }

            boolean var4 = NumericOperations.getBoolean(var1);
            if (var4) {
               String var5 = (String)getParameter(var0, 2, true, 1, var9);
               if (var5 == null) {
                  return;
               }

               if (var5.startsWith("&")) {
                  Vector var10 = getCachedFieldFids(var0, var5.substring(1));
                  if (var10 != null) {
                     var7 = (String)var10.elementAt(0);
                  }
               } else {
                  var7 = getFidByVid(var5, getFormid());
               }

               if (var7 == null) {
                  writeError(var0, ID_EX_FN_FIELD_ID, "Program hiba, hiba történt a mező azonosító meghatározásakor!" + var5, (Exception)null, (Object)null);
                  return;
               }

               ExpClass var16 = getFieldValue(var0, var7);
               Object var11 = var16.getValue();
               if (var11 == null) {
                  var11 = "";
               }

               int var12 = getFieldType(var7);
               ExpClass var13 = var0.getParameter(1);
               if (var13.isDontModify()) {
                  return;
               }

               Object var2;
               ExpClass var3;
               if (var12 == 2) {
                  var3 = ABEVFeaturedBaseFunctions.baseTypeConversion1(var13);
                  var2 = var3.getValue();
               } else if (var12 == 4 && -3 != var13.getFlag()) {
                  var3 = ABEVFeaturedBaseFunctions.baseTypeLogicalConversion1(var13);
                  var2 = var3.getValue();
               } else {
                  var2 = var13.getValue();
               }

               if (var2 == null) {
                  var2 = "";
               }

               if (var13.getFlag() == -3) {
                  var2 = "";
               }

               String var14 = getRawFValue(var7, (Integer)null);
               if (isLogikaiNullToFalse(var2, var11, var12, var14) || var11.toString().compareTo(var2.toString()) != 0 || isInGeneratorMod()) {
                  if (!feltetelesErtekIrhat(var7)) {
                     if (!isLogikaiNullToFalse(var2, var11, var12, var14)) {
                        writeWarning(var0, ID_FILLIN_ERROR, "[Nyomtatvány] Eltér a mező értéke a számított értéktől!  (" + var7 + ", Jelenlegi: '" + var11.toString() + "', Számított: '" + var2.toString() + "') \n" + extendedInfo(var7, FIdHelper.isFieldDynamic(var7) ? FIdHelper.getDPageNumber(var7) : null), (Exception)null, createGotoButtonByDefaultPageNumber(var7));
                     }
                  } else {
                     setFieldValue(var0, var7, var2);
                     par_field_do_calculation[0] = var7;
                     par_field_do_calculation[7] = FIdHelper.createId(var7);
                     getICObj().fieldDoCalculations((Object)par_field_do_calculation);
                  }

                  if (var8 == 4) {
                     String var6 = (String)getParameter(var0, 3, true, 1, var9);
                     if (feltetelesErtekIrhat(var7) && var6 != null && var6.length() != 0) {
                        showWarning(var0, ID_WARNING, var6 + " (" + var7 + ", " + var11.toString() + ", " + var2.toString() + ")", (Exception)null, (Object)null);
                     }
                  }
               }
            }
         } catch (Exception var15) {
            var15.printStackTrace();
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var15.getMessage(), var15, var0);
         }

      }
   }

   private static boolean feltetelesErtekIrhat(String var0) {
      if (isInGeneratorMod()) {
         return true;
      } else if (!isFullCheck && !isFullCalc) {
         if (!isFieldCalcEnabled()) {
            return false;
         } else {
            return !MainFrame.ellvitamode;
         }
      } else {
         return false;
      }
   }

   private static boolean isFieldCalcEnabled() {
      String var0 = SettingsStore.getInstance().get("gui", "mezőszámítás");
      return var0 == null || !((String)var0).equalsIgnoreCase("false");
   }

   private static boolean isInGeneratorMod() {
      return "10".equals(gui_info.getHasznalatiMod());
   }

   private static boolean isInWebKitoltoMod() {
      return "20".equals(gui_info.getHasznalatiMod());
   }

   private static boolean isInBatchEllenorzoOpMode() {
      return gui_info.getOperationMode().equals("2") && !isInGeneratorMod();
   }

   public static boolean isInJavkeretOpMode() {
      if (isInJavkeretExtMod()) {
         return true;
      } else {
         return gui_info.getOperationMode().equals("1") && !isInGeneratorMod();
      }
   }

   private static boolean isInJavkeretExtMod() {
      return "30".equals(gui_info.getHasznalatiMod());
   }

   public static synchronized void fnFeltetelesErtekadas(ExpClass var0) {
      Object var4 = null;
      boolean var8 = false;
      boolean var11 = false;
      boolean var14 = false;
      var0.setType(0);
      var0.setResult((Object)null);
      if (var0.getParametersCount() != 3) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            boolean var16 = false;
            Object var1 = getParameter(var0, 0, true, 4, var16);
            if (var1 == null) {
               return;
            }

            boolean var5 = NumericOperations.getBoolean(var1);
            Object var2 = getParameter(var0, 1, false, 4, var16);
            int var9 = getExpType(var0, 1);
            int var12 = var0.getParameter(1).getFlag();
            boolean var6 = var0.getParameter(1).isDontModify();
            Object var3 = getParameter(var0, 2, false, 4, var16);
            int var10 = getExpType(var0, 2);
            int var13 = var0.getParameter(1).getFlag();
            boolean var7 = var0.getParameter(1).isDontModify();
            if (var3 == null) {
               var3 = "";
               var10 = 1;
            }

            String var15 = getSelfFid(var0);
            if (var15 == null) {
               writeError(var0, ID_EX_FN_FIELD_ID, "Program hiba, hiba történt a mező azonosító meghatározásakor!", (Exception)null, (Object)null);
               return;
            }

            ExpClass var17;
            int var19;
            int var20;
            if (g_readonly_calc_state) {
               gui_info.setFieldReadOnly(var15, var5);
               var17 = getActualFieldValue(var15);
               var19 = var17.getType();
               var4 = var17.getValue();
               var20 = var0.getFlag();
               var8 = var0.isDontModify();
               if (!isFieldCalcEnabled()) {
                  gui_info.setFieldReadOnly(var15, false);
               }
            } else if (!feltetelesErtekIrhat(var15)) {
               if (var5) {
                  var19 = var9;
                  var4 = var2;
                  var20 = var12;
                  var8 = var6;
               } else {
                  var17 = getActualFieldValue(var15);
                  var19 = var17.getType();
                  var4 = var17.getValue();
                  var20 = var0.getFlag();
                  var8 = var0.isDontModify();
               }

               if (!isFieldCalcEnabled()) {
                  gui_info.setFieldReadOnly(var15, false);
               }
            } else if (!isInGeneratorMod() && !g_readonly_calc_act_page_number.equals(FIdHelper.getDPageNumber(var15))) {
               var17 = getActualFieldValue(var15);
               var19 = var17.getType();
               var4 = var17.getValue();
               var20 = var0.getFlag();
               var8 = var0.isDontModify();
            } else if (var5) {
               gui_info.setFieldReadOnly(var15, true);
               var19 = var9;
               var4 = var2;
               var20 = var12;
               var8 = var6;
            } else {
               if (gui_info.getFieldReadOnly(var15)) {
                  var19 = var10;
                  var4 = var3;
                  var20 = var13;
                  var8 = var7;
               } else {
                  var17 = getActualFieldValue(var15);
                  var19 = var17.getType();
                  var4 = var17.getValue();
                  var20 = var0.getFlag();
                  var8 = var0.isDontModify();
               }

               gui_info.setFieldReadOnly(var15, false);
            }

            var0.setType(var19);
            var0.setFlag(var20);
            var0.setDontModify(var8);
            switch(var19) {
            case 2:
               if (var4 == null) {
                  var4 = new BigDecimal("0");
               }
               break;
            case 4:
               if (var4 == null) {
                  var4 = Boolean.FALSE;
               }
            }

            var0.setResult(var4);
         } catch (Exception var18) {
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var18.getMessage(), var18, var0);
         }

      }
   }

   private static ExpClass getActualFieldValue(String var0) {
      Integer var1 = FIdHelper.getDPageNumber(var0);
      return getFValue(var0, var1);
   }

   public static synchronized void fnDSum(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var3 = 0;
      Object var1 = null;

      try {
         boolean var6 = false;
         String var7 = (String)getParameter(var0, 0, true, 1, var6);
         String var8 = getFidByVid(var7, getFormid());
         if (var8 == null) {
            writeError(var0, ID_EX_FN_FIELD_ID, "Program hiba, hiba történt a mező azonosító meghatározásakor!", (Exception)null, var7);
            return;
         }

         int var9 = getFieldType(var8);
         Vector var2 = gui_info.get_dfield_values(var8);
         if (var2 != null) {
            if (var2 instanceof Vector) {
               Vector var5 = (Vector)var2;
               int var10 = 0;

               for(int var11 = var5.size(); var10 < var11; ++var10) {
                  String var14 = (String)var5.get(var10);
                  ExpClass var13;
                  if (var14 != null && var14.length() != 0 && (var13 = ExpFactoryLight.createConstant(var14, var9)) != null) {
                     Object var12 = var13.getValue();
                     int var4 = var13.getType();
                     if (!g_in_variable_exp && !isFieldEmpty(var13)) {
                        g_all_fileds_empty = false;
                     }

                     if (var4 != 0 && var12 != null) {
                        if (var4 != 2) {
                           writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
                           break;
                        }

                        if (var1 == null) {
                           var1 = var12;
                           var3 = var4;
                        } else {
                           switch(var4) {
                           case 2:
                              var3 = 2;

                              try {
                                 var1 = NumericOperations.add(var1, var12);
                                 break;
                              } catch (Exception var16) {
                                 writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
                                 return;
                              }
                           default:
                              writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
                           }
                        }
                     }
                  }
               }
            }
         } else {
            writeError(var0, ID_EX_FN_FIELD, "Program hiba, hiba történt a mező adatok megszerzésekor!", (Exception)null, var7);
         }
      } catch (Exception var17) {
         var1 = null;
         var3 = 0;
      }

      var0.setType(var3);
      var0.setResult(var1);
   }

   public static synchronized void fnDErtekAzonos(ExpClass var0) {
      fnErtekDiff(var0, true);
   }

   public static synchronized void fnDErtekKulonbozo(ExpClass var0) {
      fnErtekDiff(var0, false);
   }

   public static synchronized void fnErtekDiff(ExpClass var0, boolean var1) {
      var0.setType(0);
      var0.setResult((Object)null);
      byte var4 = 0;
      Boolean var2 = null;

      try {
         if (var0.getParametersCount() != 2) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
            return;
         }

         boolean var7 = false;
         String var8 = (String)getParameter(var0, 0, true, 1, var7);
         Boolean var9 = (Boolean)getParameter(var0, 1, true, 4, var7);
         if (var8 == null || var9 == null) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
            return;
         }

         boolean var10 = var9;
         String var11 = getFidByVid(var8, getFormid());
         if (var11 == null) {
            writeError(var0, ID_EX_FN_FIELD_ID, "Program hiba, hiba történt a mező azonosító meghatározásakor!", (Exception)null, var8);
            return;
         }

         if (isLaphozKotott() && var10) {
            ExpClass var12 = getDFieldValue(var0, var11, FIdHelper.getPageNumber(getSelf()));
            if (var12.getValue() == null) {
               var4 = 4;
               var2 = Boolean.TRUE;
               return;
            }
         }

         Vector var3 = gui_info.get_dfield_values(var11);
         if (var3 != null) {
            if (var3 instanceof Vector) {
               Vector var6 = (Vector)var3;
               Hashtable var23 = null;
               String var13 = null;
               int var16 = 0;
               int var17 = var6.size();

               while(true) {
                  while(true) {
                     int var5;
                     Object var14;
                     ExpClass var15;
                     if (var16 >= var17 || var13 != null) {
                        while(true) {
                           while(true) {
                              if (var16 >= var17 || var2 != null) {
                                 if (var2 == null) {
                                    var4 = 4;
                                    var2 = Boolean.TRUE;
                                 }

                                 return;
                              }

                              var14 = var6.get(var16++);
                              var15 = ExpFactoryLight.createConstant(var14.toString(), getFieldType(var11));
                              var14 = var15.getValue();
                              var5 = var15.getType();
                              if (var5 != 0 && var14 != null) {
                                 break;
                              }

                              if (!var10) {
                                 var14 = "##null##";
                                 break;
                              }
                           }

                           if (var1) {
                              if (!var13.equalsIgnoreCase(var14.toString())) {
                                 var4 = 4;
                                 var2 = Boolean.FALSE;
                              }
                           } else {
                              if (var23.containsKey(var14.toString())) {
                                 var4 = 4;
                                 var2 = Boolean.FALSE;
                              }

                              var23.put(var14.toString(), "");
                           }
                        }
                     }

                     var14 = var6.get(var16++);
                     var15 = ExpFactoryLight.createConstant(var14.toString(), getFieldType(var11));
                     var14 = var15.getValue();
                     var5 = var15.getType();
                     if (var5 != 0 && var14 != null) {
                        var13 = var14.toString();
                        break;
                     }

                     if (!var10) {
                        var13 = "##null##";
                        break;
                     }
                  }

                  var23 = new Hashtable(var6.size());
                  var23.put(var13, "");
               }
            }
         } else {
            writeError(var0, ID_EX_FN_FIELD, "Program hiba, hiba történt a mező adatok megszerzésekor!", (Exception)null, var8);
         }
      } catch (Exception var21) {
         var2 = null;
         var4 = 0;
      } finally {
         var0.setType(var4);
         var0.setResult(var2);
      }

   }

   public static synchronized void fnLapokszama(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      if (var0.getParametersCount() != 2) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            boolean var3 = false;
            String var1 = (String)getParameter(var0, 0, true, 1, var3);
            if (var1 == null) {
               return;
            }

            String var2 = (String)getParameter(var0, 1, true, 1, var3);
            if (var2 == null) {
               return;
            }

            int var4 = specPageCounter(var0, var1, var2, (String)null);
            if (var4 < 0) {
               return;
            }

            var0.setType(2);
            var0.setResult(new BigDecimal(var4));
         } catch (Exception var5) {
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var5.getMessage(), var5, var0);
         }

      }
   }

   public static synchronized void fnDPageCount(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      String var1 = "@";

      try {
         boolean var2 = false;
         String var3 = (String)getParameter(var0, 0, true, 1, var2);
         if (var3.indexOf(var1) == -1) {
            var1 = "_";
         }

         int var4 = var3.indexOf(var1);
         String var5 = var3.substring(0, var4);
         String var6 = var3.substring(var4 + 1);
         int var7 = specPageCounter(var0, var5, var6, (String)null);
         if (var7 < 0) {
            return;
         }

         var0.setType(2);
         var0.setResult(new BigDecimal(var7));
      } catch (Exception var8) {
         writeError(var0, ERR_FN_ID, "Számítási hiba! " + var8.getMessage(), var8, var0);
      }

   }

   public static synchronized void fnDPageNumber(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);

      try {
         Object[] var1 = getSelf();
         if (var1 != null) {
            int var2 = FIdHelper.getPageNumber(var1);
            var0.setType(2);
            var0.setResult(new BigDecimal(var2 + 1));
         }
      } catch (Exception var3) {
         writeError(var0, ERR_FN_ID, "Számítási hiba! " + var3.getMessage(), var3, var0);
      }

   }

   public static synchronized void fnDPrev(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);

      try {
         boolean var6 = false;
         String var7 = (String)getParameter(var0, 0, true, 1, var6);
         String var8 = getFidByVid(var7, getFormid());
         String var9 = getPageNameByField(var0, var8);
         PageModel var10 = getPageByName(var0, var9);
         if (var10 == null) {
            return;
         }

         if (getPageIsDynamic(var0, var9, var10)) {
            Integer var1 = FIdHelper.getDPageNumber(var8);
            if (var1 != null && var1 instanceof Integer) {
               int var3 = (Integer)var1;
               if (var3 == 0) {
                  var0.setType(2);
                  var0.setResult(new Integer(0));
               } else {
                  ExpClass var5 = getDprevFieldValue(var0, var8, var9, var3 - 1);
                  Object var2 = var5.getValue();
                  int var4 = var5.getType();
                  if (var4 == 0) {
                     var0.setType(2);
                     var0.setResult(new Integer(0));
                  } else {
                     var0.setType(var4);
                     var0.setResult(var2);
                  }
               }
            }
         }
      } catch (Exception var11) {
         writeError(var0, ERR_FN_ID, "Számítási hiba! " + var11.getMessage(), var11, var0);
      }

   }

   private static ExpClass getDprevFieldValue(ExpClass var0, String var1, String var2, int var3) {
      ExpClass var4 = ic.getExpressionDefinition(var1);
      return var4 == null ? getDFieldValue(var0, var1, var3) : calculateFieldValue(var4, var1, var2, var3);
   }

   private static ExpClass calculateFieldValue(ExpClass var0, String var1, String var2, int var3) {
      Hashtable var4 = new Hashtable();
      var4.put(getRealPageName(var2), new Integer(var3));
      FIdHelper.setDinPageNumber(var4);
      ExpClass var5 = null;

      try {
         Object var6 = ABEVFunctionSet.expwrapper.get("", var0);
         var5 = ExpFactoryLight.createConstant(var6 == null ? null : var6.toString(), getFieldType(var1));
      } catch (Exception var7) {
         writeError(var0, ERR_FN_ID, "Számítási hiba! ", var7, var5);
      }

      FIdHelper.resetDinPageNumber(var4);
      return var5;
   }

   public static synchronized void fnDFirst(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);

      try {
         boolean var1 = false;
         String var2 = (String)getParameter(var0, 0, true, 1, var1);
         String var3 = getFidByVid(var2, getFormid());
         ExpClass var4 = getDFieldValue(var0, var3, 0);
         var0.setType(var4.getType());
         var0.setResult(var4.getValue());
      } catch (Exception var5) {
         writeError(var0, ERR_FN_ID, "Számítási hiba! " + var5.getMessage(), var5, var0);
      }

   }

   public static synchronized void fnJobTeszt(ExpClass var0) {
      boolean var1 = true;
   }

   public static synchronized void fngetfidbyvid(ExpClass var0) {
      if (var0.getParametersCount() == 1) {
         var0.setType(1);
         var0.setResult("");

         try {
            String var1 = (String)getParameter(var0, 0, true, 1, false);
            String var2 = getFidByVid(var1, getFormid());
            var0.setType(1);
            var0.setResult(var2);
         } catch (Exception var3) {
            var3.printStackTrace();
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var3.getMessage(), var3, var0);
         }

      }
   }

   public static synchronized void fnFieldReadOnly(ExpClass var0) {
      if (var0.getParametersCount() == 4) {
         var0.setType(0);
         var0.setResult((Object)null);

         try {
            String var1 = (String)getParameter(var0, 0, true, 1, false);
            String var2 = (String)getParameter(var0, 1, true, 1, false);
            String var3 = (String)getParameter(var0, 2, true, 1, false);
            String var4 = (String)getParameter(var0, 3, true, 1, false);
            if (var1 == null || var2 == null || var3 == null || var4 == null) {
               return;
            }

            if (!var4.equalsIgnoreCase("1") && !var4.equalsIgnoreCase("0")) {
               return;
            }

            int var5 = Integer.parseInt(var3);
            boolean var6 = var4.equalsIgnoreCase("1");
            boolean var7;
            if (var1.equalsIgnoreCase("set")) {
               if (isFullCheck) {
                  var7 = false;
               } else {
                  gui_info.setFieldReadOnly(var2, var6);
                  var7 = true;
               }
            } else {
               var7 = !gui_info.getFieldReadOnly(var2);
            }

            var0.setType(4);
            var0.setResult(var7);
         } catch (Exception var8) {
            var8.printStackTrace();
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var8.getMessage(), var8, var0);
         }

      }
   }

   public static synchronized void fnString(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var2 = var0.getParameter(0);
         if (var2 == null) {
            return;
         }

         Object var3 = var2.getValue();
         if (var3 == null) {
            return;
         }

         String var4 = null;
         int var5 = NumericOperations.getObjectType(var3, var3);
         switch(var5) {
         case 7:
            var4 = ((BigDecimal)var3).toPlainString();
         default:
            String var6 = var4 == null ? var3.toString() : var4.replaceAll(",", ".");
            var0.setType(1);
            var0.setResult(var6);
         }
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnImp(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 2) {
         ExpClass var2 = var0.getParameter(0);
         if (var2 == null) {
            return;
         }

         Object var3 = var2.getValue();
         if (var3 == null) {
            return;
         }

         if (NumericOperations.getObjectType(var3, var3) == 6) {
            if ((Boolean)var3) {
               var2 = var0.getParameter(1);
               var0.setType(var2.getType());
               var0.setResult(var2.getResult());
               var0.setFlag(var2.getFlag());
               var0.setDontModify(var2.isDontModify());
            } else {
               var0.setType(4);
               var0.setResult(Boolean.TRUE);
            }
         } else {
            writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés! (Az első paraméter nem logikai típusú) ", (Exception)null, (Object)null);
            System.out.println("exp.toString() = " + var0.toString());
         }
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static boolean isRoundAble(String var0, Object var1) {
      if (getFieldType(var0) == 2) {
         int var2 = NumericOperations.getObjectType(var1, var1);
         if (var2 == 7) {
            int var3 = getFieldRound(var0);
            return var3 != -1;
         }
      }

      return false;
   }

   public static int getPrecision(String var0) throws Exception {
      int var1 = getFieldRound(var0);
      if (var1 == -1) {
         throw new Exception("A kerekítési érték nem létezik");
      } else {
         if (var1 == -2 || var1 == 0 && isFieldGuiDataType(var0, 7)) {
            var1 = getDefaultPrecision();
         }

         return var1;
      }
   }

   public static int getPrecisionForKihatas(String var0) {
      int var1 = getFieldRound(var0);
      if (var1 == -1) {
         var1 = getDefaultPrecision();
      }

      if (var1 == -2 || var1 == 0 && isFieldGuiDataType(var0, 7)) {
         var1 = getDefaultPrecision();
      }

      return var1;
   }

   private static int getDefaultPrecision() {
      int var0 = getPrecisionFromTemplate();
      return var0 != -1 ? var0 : OrgHandler.getInstance().getDeafultNumericPrecision((String)gui_info.docinfo.get("org"));
   }

   private static int getPrecisionFromTemplate() {
      try {
         String var0 = (String)gui_info.docinfo.get("precision");
         if (var0 == null) {
            return -1;
         } else {
            return "".equals(var0) ? -1 : Integer.valueOf(var0);
         }
      } catch (NumberFormatException var1) {
         Tools.eLog(var1, 1);
         return -1;
      }
   }

   public static synchronized void fnGetRealZero(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      ExpClass var1 = var0.getParameter(0);
      if (var1 != null) {
         getRealValue(var0, var1);
      }
   }

   private static void getRealValue(ExpClass var0, ExpClass var1) {
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;
      Object var2 = var1.getValue();
      int var3 = var1.getType();
      int var5 = var1.getFlag();
      var0.setType(var3);
      var0.setResult(var2);
      var0.setFlag(var5);
      if (var1.isDontModify() && g_current_field_id != null) {
         try {
            ExpClass var11 = getFieldValue((ExpClass)null, FIdHelper.getFieldId(g_current_field_id));
            var0.setType(var11.getExpType());
            var0.setValue(var11.getValue());
         } catch (Exception var10) {
            writeError(var0, ID_EX_EREDETI, "Program hiba az eredeti() függvény kezelésénél", var10, (Object)null);
         }

      } else if (var3 != 4) {
         if (var2 == null) {
            var6 = true;
         } else {
            String var9 = var2.toString();
            if (var9.length() == 0) {
               var7 = true;
            }

            if (var9.equals("0.0") || var9.equals("0")) {
               var8 = true;
            }
         }

         if (var6 || var7 || var8) {
            int var4;
            if (g_current_field_id == null) {
               var4 = var3;
            } else {
               var4 = getFieldType(FIdHelper.getFieldId(g_current_field_id));
            }

            if (var4 == 2) {
               if (var5 == -2) {
                  var0.setType(2);
                  var0.setResult(new Integer(0));
                  var0.setFlag(-2);
               } else if (var5 == -3) {
                  var0.setType(0);
                  var0.setResult((Object)null);
                  var0.setFlag(-3);
               } else if (var8 && g_all_fileds_empty) {
                  var0.setType(0);
                  var0.setResult((Object)null);
                  var0.setFlag(0);
               }
            }
         }
      }
   }

   private static Object getRealZeroValueCore(ExpClass var0) {
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;
      if (var0 == null) {
         return null;
      } else {
         Object var1 = var0.getValue();
         int var3 = var0.getType();
         int var5 = var0.getFlag();
         Object var2 = var1;
         if (var0.isDontModify() && g_current_field_id != null) {
            try {
               ExpClass var12 = getFieldValue((ExpClass)null, FIdHelper.getFieldId(g_current_field_id));
               var2 = var12.getValue();
            } catch (Exception var10) {
               writeError(var0, ID_EX_EREDETI, "Program hiba az eredeti() függvény kezelésénél", var10, (Object)null);
            }

            return var2;
         } else if (var3 == 4) {
            return var1;
         } else {
            if (var1 == null) {
               var6 = true;
            } else {
               String var9 = var1.toString();
               if (var9.length() == 0) {
                  var7 = true;
               }

               if (var9.equals("0.0") || var9.equals("0")) {
                  var8 = true;
               }
            }

            if (!var6 && !var7 && !var8) {
               return var1;
            } else {
               int var4;
               if (g_current_field_id == null) {
                  var4 = var3;
               } else {
                  var4 = getFieldType(FIdHelper.getFieldId(g_current_field_id));
               }

               if (var4 != 2) {
                  return var1;
               } else if (var5 == -2) {
                  Integer var11 = new Integer(0);
                  return var11;
               } else if (var5 == -3) {
                  var2 = null;
                  return var2;
               } else {
                  return var1;
               }
            }
         }
      }
   }

   public static boolean isSpecVarZerus(ExpClass var0) {
      if (var0 != null) {
         return var0.getFlag() == -2;
      } else {
         return false;
      }
   }

   private static boolean setSpecVarZerus(ExpClass var0, int var1) {
      if (var0 != null) {
         var0.setFlag(var1);
      }

      return false;
   }

   public static boolean isFieldEmpty(ExpClass var0) {
      int var2 = var0.getType();
      Object var1;
      switch(var2) {
      case 0:
      case 3:
      default:
         break;
      case 1:
         var1 = var0.getValue();
         if (var1.toString().length() != 0) {
            return false;
         }
         break;
      case 2:
         var1 = var0.getValue();
         if (var1 != null) {
            return false;
         }
         break;
      case 4:
         var1 = var0.getValue();
         if (var1 != null) {
            return !(Boolean)var1;
         }
      }

      return true;
   }

   public static synchronized void fnIsZeroVisible(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      if (var0.getParametersCount() == 2) {
         ExpClass var1 = var0.getParameter(1);
         if (var1.getType() == 4) {
            Object var2 = var1.getValue();
            if (var2 instanceof Boolean) {
               boolean var3 = (Boolean)var2;
               var1 = var0.getParameter(0);
               var2 = var1.getResult();
               String var4 = var2 == null ? "" : var2.toString();
               if ("".equals(var4)) {
                  if (var3) {
                     var0.setType(2);
                     var0.setValue(new Integer(0));
                     if (!g_in_variable_exp) {
                        g_all_fileds_empty = false;
                     }
                  } else {
                     var0.setType(var1.getType());
                     var0.setValue(var1.getValue());
                  }
               } else if (!var4.equals("0") && !var4.equals("0.0")) {
                  var0.setType(var1.getType());
                  var0.setValue(var1.getValue());
               } else if (var3) {
                  var0.setType(2);
                  var0.setValue(var2);
                  if (!g_in_variable_exp) {
                     g_all_fileds_empty = false;
                  }
               } else {
                  var0.setType(0);
                  var0.setValue((Object)null);
               }
            } else {
               writeTypeMismatchError(4, (Exception)null, (Object)null, var0, var1);
            }
         } else {
            writeTypeMismatchError(4, (Exception)null, (Object)null, var0, var1);
         }
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   private static void writeTypeMismatchError(int var0, Exception var1, Object var2, ExpClass var3, ExpClass var4) {
      String var5;
      try {
         var5 = getExpValueTypeName(var0);
      } catch (Exception var11) {
         var5 = "???";
      }

      String var7;
      try {
         var7 = getExpValueTypeName(var4.getType());
      } catch (Exception var10) {
         var7 = "???";
      }

      Object var6;
      try {
         var6 = var4.getValue();
      } catch (Exception var9) {
         var6 = "";
      }

      var5 = var5 == null ? "???" : var5.toString();
      var7 = var7 == null ? "???" : var7.toString();
      String var12 = var6 == null ? "???" : var6.toString();
      writeError(var3, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés! (" + var5 + "!" + var7 + ":" + var12 + ")", var1, var2);
   }

   private static String getExpValueTypeName(int var0) {
      String var1 = "";
      if (var0 >= 0) {
         if (var0 == 0) {
            return "nil";
         }

         if ((var0 & 1) == 1) {
            var1 = var1 + "Szöveg";
         }

         if ((var0 & 2) == 2) {
            var1 = var1 + (var1.length() > 0 ? "|" : "") + "Szám";
         }

         if ((var0 & 4) == 4) {
            var1 = var1 + (var1.length() > 0 ? "|" : "") + "Logikai";
         }
      }

      return var1.length() == 0 ? "(Ismeretlen)" : var1;
   }

   public static synchronized void fnEkv(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 2) {
         ExpClass var2 = var0.getParameter(0);
         if (var2 == null) {
            return;
         }

         Object var3 = var2.getValue();
         if (var3 == null) {
            return;
         }

         var2 = var0.getParameter(1);
         if (var2 == null) {
            return;
         }

         Object var4 = var2.getValue();
         if (var4 == null) {
            return;
         }

         if (NumericOperations.getObjectType(var3, var3) == 6 && NumericOperations.getObjectType(var4, var4) == 6) {
            boolean var5 = !((Boolean)var3 ^ (Boolean)var4);
            var0.setType(4);
            var0.setResult(var5);
         } else {
            writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
         }
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   private static boolean searchValue(ExpClass var0, String var1, Object var2, int var3) throws Exception {
      Vector var4 = getCachedFieldFids(var0, var1);
      boolean var5 = false;

      for(int var6 = 0; var6 < var4.size(); ++var6) {
         String var7 = (String)var4.elementAt(var6);
         ExpClass var8 = getFieldValue(var0, var7);
         Object var9 = var8.getValue();
         int var10 = var8.getType();
         if (var10 != 0 && var9.toString().compareTo(var2.toString()) == 0) {
            var5 = true;
            break;
         }
      }

      return var5;
   }

   private static boolean specDiff(ExpClass var0, String var1, String var2, int var3) throws Exception {
      Vector var4 = getCachedFieldFids(var0, var1);
      Vector var5 = getCachedFieldFids(var0, var2);
      Vector var6 = getFieldValues(var0, var4);
      Vector var7 = getFieldValues(var0, var5);
      Vector var8 = getSpecVector();

      for(int var10 = 0; var10 < var6.size(); ++var10) {
         String var9 = getString(var6, var10);
         if (var9.length() != 0) {
            int var11 = var8.indexOf(var9);
            if (var11 == -1) {
               return false;
            }

            var9 = getString(var7, var11);
            if (var9.length() != 0) {
               String var12 = getString(var7, var10 + var3);
               if (var12.length() != 0 && var9.equals(var12)) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   private static String getString(Vector var0, int var1) {
      String var2 = "";
      Object var3 = var0.get(var1);
      if (var3 != null) {
         var2 = var3.toString();
      }

      return var2;
   }

   private static Vector getSpecVector() {
      if (spVector == null) {
         spVector = new Vector(17);
         spVector.add("397");
         spVector.add("155");
         spVector.add("381");
         spVector.add("393");
         spVector.add("394");
         spVector.add("395");
         spVector.add("162");
         spVector.add("392");
         spVector.add("396");
         spVector.add("388");
         spVector.add("389");
         spVector.add("151");
         spVector.add("304");
         spVector.add("352");
         spVector.add("356");
         spVector.add("369");
         spVector.add("370");
      }

      return spVector;
   }

   private static boolean uniqPairs(ExpClass var0, Object[] var1, boolean[] var2, boolean var3, int var4) throws Exception {
      Object[] var5 = new Object[var1.length];

      int var6;
      for(var6 = 0; var6 < var1.length; ++var6) {
         String var7 = (String)var1[var6];
         var5[var6] = getCachedFieldFids(var0, var7);
      }

      var6 = ((Vector)var5[0]).size();
      Vector var18 = new Vector(var6);
      boolean[] var8 = new boolean[var6];

      int var9;
      for(var9 = 0; var9 < var6; ++var9) {
         var18.add("");
         var8[var9] = false;
      }

      int var11;
      String var12;
      for(var9 = 0; var9 < var5.length; ++var9) {
         Vector var10 = (Vector)var5[var9];

         for(var11 = 0; var11 < var10.size(); ++var11) {
            var12 = (String)var10.elementAt(var11);
            ExpClass var13 = getFieldValue(var0, var12);
            Object var14 = var13.getValue();
            int var15 = var13.getType();
            if (var9 == 0 && var15 == 0) {
               var8[var11] = true;
            } else {
               if (var9 > 0) {
                  if (!var3 && var15 == 0) {
                     var8[var11] = true;
                     continue;
                  }

                  if (var4 != 0 && var15 != var4) {
                     var8[var11] = true;
                     continue;
                  }
               }

               if (var2[var9]) {
                  String var16 = (String)var18.get(var11);
                  StringBuffer var17 = new StringBuffer();
                  var17.append(var16);
                  var17.append("#");
                  var17.append(var14.toString());
                  var18.set(var11, var17.toString());
               }
            }
         }
      }

      for(var9 = 0; var9 < var18.size(); ++var9) {
         if (!var8[var9]) {
            String var19 = (String)var18.elementAt(var9);

            for(var11 = var9 + 1; var11 < var18.size(); ++var11) {
               if (!var8[var11]) {
                  var12 = (String)var18.elementAt(var11);
                  if (var19.equals(var12)) {
                     return false;
                  }
               }
            }
         }
      }

      return true;
   }

   private static void getFilled(ExpClass var0, boolean var1) {
      var0.setType(0);
      var0.setResult((Object)null);
      if (var0.getParametersCount() != 1) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            boolean var3 = false;
            String var2 = (String)getParameter(var0, 0, true, 1, var3);
            if (var2 == null) {
               return;
            }

            Object[] var4 = convABEVtableIDs(var2);
            boolean var5 = (Boolean)var4[3];
            Vector var6 = getCachedFieldFids(var0, var2);
            if (var6 != null && var6.size() != 0) {
               String var7 = getFirstNotNull(var0, var6, var1);
               if (var7 != null) {
                  String var8;
                  if (var5) {
                     var8 = getRowNumByFid(var7, getFormid());
                  } else {
                     var8 = getColNumByFid(var7, getFormid());
                  }

                  if (var8 != null) {
                     var0.setType(2);
                     var0.setResult(Integer.valueOf(var8));
                  }
               }
            }
         } catch (Exception var9) {
            var9.printStackTrace();
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var9.getMessage(), var9, var0);
         }

      }
   }

   private static String getRowNumByFid(String var0, String var1) {
      Object[] var2 = new Object[]{var0, null};
      Object[] var3 = (Object[])((Object[])getRowColById(var2, var1));
      return var3 != null ? (String)var3[0] : null;
   }

   private static String getColNumByFid(String var0, String var1) {
      Object[] var2 = new Object[]{var0, null};
      Object[] var3 = (Object[])((Object[])getRowColById(var2, var1));
      return var3 != null ? (String)var3[1] : null;
   }

   private static String getRowNumByVid(String var0, String var1) {
      if (var0 == null) {
         return null;
      } else {
         Object[] var2 = new Object[]{null, null};
         String var3 = null;

         try {
            Hashtable var4 = (Hashtable)g_rows.get(var1);
            if (var4 != null) {
               var3 = (String)var4.get(var0);
            }

            if (var3 == null) {
               var2[0] = null;
               var2[1] = var0;
               Object[] var5 = (Object[])((Object[])getRowColById(var2, var1));
               if (var5 != null) {
                  var3 = (String)var5[0];
               }
            }

            if (var3 != null) {
               if (var4 == null) {
                  var4 = new Hashtable();
                  g_rows.put(var1, var4);
               }

               var4.put(var0, var3);
            }
         } catch (Exception var6) {
            Tools.eLog(var6, 1);
         }

         return var3;
      }
   }

   private static String getColNumByVid(String var0, String var1) {
      Object[] var2 = new Object[]{null, var0};
      Object[] var3 = (Object[])((Object[])getRowColById(var2, var1));
      return var3 != null ? (String)var3[1] : null;
   }

   private static Object getRowColById(Object[] var0, String var1) {
      return meta_info.getRowColByFId(var0, var1);
   }

   private static Hashtable getCountedListOfValue(ExpClass var0, Vector var1) {
      Hashtable var2 = new Hashtable(var1.size());
      Integer var3 = new Integer(1);

      for(int var4 = 0; var4 < var1.size(); ++var4) {
         String var5 = (String)var1.elementAt(var4);
         ExpClass var6 = getFieldValue(var0, var5);
         Object var7 = var6.getValue();
         switch(var6.getType()) {
         case 0:
            break;
         case 1:
         case 2:
         case 4:
            if (var7.toString().compareTo("0") != 0) {
               String var8 = var7.toString();
               if (var2.containsKey(var8)) {
                  int var9 = (Integer)var2.get(var8);
                  var2.put(var8, new Integer(var9 + 1));
               } else {
                  var2.put(var8, var3);
               }
            }
            break;
         case 3:
         default:
            writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
         }
      }

      return var2;
   }

   private static DefaultListModel getDynamicSortedListOfValue(ExpClass var0, String var1) throws Exception {
      FunctionBodies.SortedList var2 = new FunctionBodies.SortedList();
      Vector var3 = getCachedFieldFids(var0, var1);
      if (var3 != null && var3.size() != 0) {
         if (g_current_field_id != null) {
            if (FIdHelper.isFieldDynamic(g_current_field_id)) {
               getSortedListOfValue(var0, var3, (Integer)null, var2);
            } else {
               getDynSortedListOfValues(var0, var3, var1, var2);
            }
         } else {
            getDynSortedListOfValues(var0, var3, var1, var2);
         }
      }

      return var2.size() == 0 ? null : var2;
   }

   private static void getDynSortedListOfValues(ExpClass var0, Vector var1, String var2, FunctionBodies.SortedList var3) throws Exception {
      Object[] var4 = convABEVtableIDs(var2);
      String var5 = (String)var4[0];
      PageModel var6 = getPageByName(var0, var5);
      Integer var7 = getPageDeepCount(var0, var5, var6);
      if (var7 != null) {
         for(Integer var8 = 0; var8 < var7; var8 = var8 + 1) {
            getSortedListOfValue(var0, var1, var8, var3);
         }
      }

   }

   private static DefaultListModel getSortedListOfValue(ExpClass var0, Vector var1, Integer var2, FunctionBodies.SortedList var3) {
      for(int var4 = 0; var4 < var1.size(); ++var4) {
         String var5 = (String)var1.elementAt(var4);
         ExpClass var6 = getFValue(var5, var2);
         Object var7 = var6.getValue();
         switch(var6.getType()) {
         case 0:
            break;
         case 1:
         case 2:
         case 4:
            if (var7.toString().compareTo("0") != 0) {
               var3.addElement(var7.toString());
            }
            break;
         case 3:
         default:
            writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
         }
      }

      return var3;
   }

   private static String getFlagValue(ExpClass var0, Vector var1, Integer var2) {
      StringBuffer var3 = new StringBuffer(var1.size());

      for(int var4 = 0; var4 < var1.size(); ++var4) {
         var3.append('0');
         String var5 = (String)var1.elementAt(var4);
         ExpClass var6 = getFValue(var5, var2);
         Object var7 = var6.getValue();
         switch(var6.getType()) {
         case 0:
            var3.setCharAt(var4, '0');
            break;
         case 1:
         case 2:
            if (var7.toString().compareTo("0") == 0) {
               var3.setCharAt(var4, '0');
            } else {
               var3.setCharAt(var4, '1');
            }
            break;
         case 3:
         default:
            writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
            break;
         case 4:
            var3.setCharAt(var4, '1');
         }
      }

      return var3.toString();
   }

   private static String getFirstNotNull(ExpClass var0, Vector var1, boolean var2) {
      int var3 = 0;
      int var4 = var1.size();
      byte var5 = 1;
      if (var2) {
         var3 = var1.size() - 1;
         var4 = -1;
         var5 = -1;
      }

      for(int var6 = var3; var6 != var4; var6 += var5) {
         String var7 = (String)var1.elementAt(var6);
         ExpClass var8 = getFieldValue(var0, var7);
         Object var9 = var8.getValue();
         switch(var8.getType()) {
         case 0:
            break;
         case 1:
         case 2:
            if (var9.toString().compareTo("0") != 0) {
               return var7;
            }
            break;
         case 3:
         default:
            writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
            break;
         case 4:
            return var7;
         }
      }

      return null;
   }

   private static Object[] convABEVtableIDs(String var0) throws Exception {
      byte var1 = 0;
      if (var0.matches("\\[.*\\]_.*")) {
         var1 = 1;
      }

      if (var0.matches("\\[.*\\]_.*\\[.*\\]")) {
         var1 = 2;
      }

      if (var0.indexOf("]") != var0.lastIndexOf("]")) {
         var1 = 2;
      }

      if (var1 == 0) {
         throw new Exception("Lap, oszlop, sor formátum maszk hiba! ");
      } else {
         Object[] var2 = new Object[4];
         var2[0] = convABEVpageIDs(var0.substring(0, var0.indexOf("]") + 1));
         String var3;
         if (var1 == 1) {
            var3 = var0.substring(var0.indexOf("]") + 2);
            if (!isInteger(var3)) {
               var2[1] = convABEVcolumnID(var3);
               var2[2] = null;
               var2[3] = Boolean.TRUE;
            } else {
               var2[1] = null;
               var2[2] = String.valueOf(Integer.parseInt(var3));
               var2[3] = Boolean.FALSE;
            }
         } else {
            var3 = var0.substring(var0.indexOf("]") + 2, var0.lastIndexOf("["));
            String var4;
            if (!isInteger(var3)) {
               var2[1] = convABEVcolumnID(var3);
               var4 = var0.substring(var0.indexOf("]") + 1).toUpperCase();
               var2[2] = convABEVrowIDs(var4.substring(var4.indexOf("[")));
               var2[3] = Boolean.TRUE;
            } else {
               var4 = var0.substring(var0.indexOf("]") + 1).toUpperCase();

               try {
                  var2[1] = convABEVrowIDs(var4.substring(var4.indexOf("[")));
               } catch (Exception var6) {
                  var2[1] = convABEVcolIDs(var4.substring(var4.indexOf("[")));
               }

               var2[2] = String.valueOf(Integer.parseInt(var3));
               var2[3] = Boolean.FALSE;
            }
         }

         return var2;
      }
   }

   private static boolean isInteger(String var0) {
      try {
         Integer.parseInt(var0);
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   private static String convABEVpageIDs(String var0) {
      return var0.startsWith("[") && var0.endsWith("]") ? var0.substring(1, var0.length() - 1) : null;
   }

   private static String convABEVcolumnID(String var0) throws Exception {
      char[] var1 = var0.toUpperCase().toCharArray();
      int var2 = var1[0] - 64;
      if (var2 >= 1 && var2 <= 26) {
         if (var0.length() == 1) {
            return String.valueOf(var2);
         } else {
            int var3 = Integer.valueOf(var0.substring(1));
            return String.valueOf(26 * var3 + var2);
         }
      } else {
         throw new Exception("Oszlop meghatározási hiba! ");
      }
   }

   private static String[] convABEVrowIDs(String var0) throws Exception {
      try {
         StringBuffer var1 = new StringBuffer();
         String var2 = var0.trim();
         var2 = var2.substring(1, var2.length() - 1);
         String[] var3 = var2.split(";");

         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4];
            if (var5.indexOf("..") > -1) {
               var1.append(enumerateRow(var5));
            } else {
               var1.append(Integer.valueOf(var5));
               var1.append(";");
            }
         }

         String var7 = var1.toString();
         var7 = var7.substring(0, var7.length() - 1);
         String[] var8 = var7.split(";");
         checkContinues(var8);
         return var8;
      } catch (Exception var6) {
         throw new Exception("Intervallum konverziós hiba!  " + var6.getMessage());
      }
   }

   private static String[] convABEVcolIDs(String var0) throws Exception {
      try {
         StringBuffer var1 = new StringBuffer();
         String var2 = var0.trim();
         var2 = var2.substring(1, var2.length() - 1);
         String[] var3 = var2.split(";");

         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4];
            if (var5.indexOf("..") > -1) {
               var1.append(enumerateCol(var5));
            } else {
               var1.append(Integer.valueOf(convABEVcolumnID(var5)));
               var1.append(";");
            }
         }

         String var7 = var1.toString();
         var7 = var7.substring(0, var7.length() - 1);
         String[] var8 = var7.split(";");
         checkContinues(var8);
         return var8;
      } catch (Exception var6) {
         throw new Exception("Intervallum konverziós hiba!  " + var6.getMessage() + " " + var0);
      }
   }

   private static String enumerateRow(String var0) {
      int var1 = Integer.valueOf(var0.substring(0, var0.indexOf("..")));
      int var2 = Integer.valueOf(var0.substring(var0.indexOf("..") + "..".length()));
      return enumerateItem(var1, var2);
   }

   private static String enumerateCol(String var0) throws Exception {
      int var1 = Integer.valueOf(convABEVcolumnID(var0.substring(0, var0.indexOf(".."))));
      int var2 = Integer.valueOf(convABEVcolumnID(var0.substring(var0.indexOf("..") + "..".length())));
      return enumerateItem(var1, var2);
   }

   private static String enumerateItem(int var0, int var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = var0; var3 <= var1; ++var3) {
         var2.append(var3);
         var2.append(";");
      }

      return var2.toString();
   }

   private static void checkContinues(String[] var0) throws Exception {
      int[] var1 = new int[var0.length];

      int var2;
      for(var2 = 0; var2 < var0.length; ++var2) {
         String var3 = var0[var2];
         var1[var2] = Integer.valueOf(var3);
      }

      for(var2 = 0; var2 < var1.length; ++var2) {
         int var5 = var1[var2];

         for(int var4 = var2 + 1; var4 < var1.length; ++var4) {
            if (var5 >= var1[var4]) {
               throw new Exception("Hibás sor, oszlop sorrend! ");
            }
         }
      }

   }

   private static Object getFieldFid(String var0, String var1, String var2, String var3) throws Exception {
      return getCheckedFieldId(var0, var1, var2, var3, 0);
   }

   private static Object getFieldVid(String var0, String var1, String var2, String var3) throws Exception {
      return getCheckedFieldId(var0, var1, var2, var3, 1);
   }

   private static Object getCheckedFieldId(String var0, String var1, String var2, String var3, int var4) throws Exception {
      Object var5 = getFieldId(var0, var1, var2, var3);
      if (!(var5 instanceof Vector)) {
         if (var5 instanceof Object[]) {
            Object[] var10 = (Object[])((Object[])var5);
            if (var10[var4] == null) {
               throw new Exception("Hiba a belső mezőazonosító meghatározása során! (lap/oszlop/sor) (" + var0 + "/" + var2 + "/" + var1 + " ) ");
            } else {
               return var10[var4];
            }
         } else {
            throw new Exception("Hiba a belső mezőazonosító meghatározása során! (lap/oszlop/sor) (" + var0 + "/" + var2 + "/" + var1 + " ) ");
         }
      } else {
         Vector var6 = (Vector)var5;
         Vector var7 = null;
         if (var6 != null) {
            var7 = new Vector(var6.size());

            for(int var8 = 0; var8 < var6.size(); ++var8) {
               Object[] var9 = (Object[])((Object[])var6.elementAt(var8));
               if (var9[var4] == null) {
                  throw new Exception("Hiba a belső mezőazonosító meghatározása során! (lap/oszlop/sor) (" + var0 + "/" + var2 + "/" + var1 + "/" + var8 + " ) ");
               }

               var7.add(var9[var4]);
            }

            Collections.sort(var7);
         }

         return var7;
      }
   }

   private static Object getFieldId(String var0, String var1, String var2, String var3) throws Exception {
      try {
         return meta_info.getFIdByRowCol(getRealPageName(var0), var1, var2, var3, gui_info);
      } catch (Exception var5) {
         throw new Exception("Hiányzó lap, sor, oszlop azonosító!  : (" + var3 + ") " + var0 + "/" + var1 + "/" + var2);
      }
   }

   private static Vector getFieldFids(Object[] var0) throws Exception {
      String var1 = (String)var0[0];
      Object var2 = var0[1];
      Object var3 = var0[2];
      String var4 = null;
      String var5 = null;
      String[] var6 = null;
      String[] var7 = null;
      if (var1 != null && var1.length() != 0 && (var2 != null || var3 != null)) {
         boolean var8 = (Boolean)var0[3];
         if (var8) {
            var4 = (String)var2;
            var7 = (String[])((String[])var3);
         } else {
            var5 = (String)var3;
            var6 = (String[])((String[])var2);
         }

         Object var9 = null;
         Vector var10 = new Vector();
         if (var8 && (var7 == null || var7.length == 0) || !var8 && (var6 == null || var6.length == 0)) {
            var9 = getFieldFid(var1, var5, var4, getFormid());
            if (var9 == null) {
               throw new Exception("Hiba a belső mezőazonosító meghatározása során! (lap/oszlop/sor) (" + var1 + "/" + var4 + " ) ");
            }

            if (var9 instanceof Vector) {
               var10 = (Vector)var9;
            } else {
               var10.add(var9);
            }
         } else if (var8) {
            var10 = getFieldFidsByColRow(var1, var7, var4);
         } else {
            var10 = getFieldFidsByRowCol(var1, var5, var6);
         }

         return var10;
      } else {
         throw new Exception("Hiányzó lap, sor, oszlop azonosító! ");
      }
   }

   private static Vector reverse(Vector var0) {
      Vector var1 = new Vector(var0.size());

      for(int var2 = var0.size() - 1; var2 >= 0; --var2) {
         Object var3 = var0.elementAt(var2);
         var1.add(var3);
      }

      return var1;
   }

   private static Vector getFieldFidsByColRow(String var0, String[] var1, String var2) throws Exception {
      if (var0 != null && var0.length() != 0 && var2 != null && var2.length() != 0 && var1 != null && var1.length != 0) {
         Vector var4 = new Vector();

         Object var3;
         for(int var5 = 0; var5 < var1.length; ++var5) {
            String var6 = var1[var5];
            var3 = getFieldFid(var0, var6, var2, getFormid());
            if (var3 == null) {
               throw new Exception("Hiba a belső mezőazonosító meghatározása során! (lap/oszlop/sor) (" + var0 + "/" + var2 + "/" + var6 + " ) ");
            }

            var4.add(var3);
         }

         var3 = null;
         return var4;
      } else {
         throw new Exception("Hiányzó lap, sor, oszlop azonosító! ");
      }
   }

   private static Vector getFieldFidsByRowCol(String var0, String var1, String[] var2) throws Exception {
      if (var0 != null && var0.length() != 0 && var1 != null && var1.length() != 0 && var2 != null && var2.length != 0) {
         Vector var4 = new Vector();

         Object var3;
         for(int var5 = 0; var5 < var2.length; ++var5) {
            String var6 = var2[var5];
            var3 = getFieldFid(var0, var1, var6, getFormid());
            if (var3 == null) {
               throw new Exception("Hiba a belső mezőazonosító meghatározása során! (lap/oszlop/sor) (" + var0 + "/" + var6 + "/" + var1 + " ) ");
            }

            var4.add(var3);
         }

         var3 = null;
         return var4;
      } else {
         throw new Exception("Hiányzó lap, sor, oszlop azonosító! ");
      }
   }

   private static Vector getFieldValues(ExpClass var0, Vector var1) {
      Vector var2 = new Vector(var1.size());

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         String var4 = (String)var1.elementAt(var3);
         ExpClass var5 = getFieldValue(var0, var4);
         Object var6 = var5.getValue();
         var2.add(var6);
      }

      return var2;
   }

   private static String chkInSpec_1_2(int var0, String var1, int var2) {
      if (var0 > var2) {
         String var3 = var1.toLowerCase();
         if (var3.compareTo("ö") == 0 || var3.compareTo("p") == 0) {
            if (var3.compareTo("ö") == 0) {
               var3 = "o";
            }

            return var0 + var3;
         }
      }

      return null;
   }

   private static int getActType(Object var0) {
      if (var0 == null) {
         return 0;
      } else if (var0 instanceof String) {
         return 1;
      } else if (var0 instanceof Number) {
         return 2;
      } else {
         return var0 instanceof Boolean ? 4 : 0;
      }
   }

   private static void setDefault(ExpClass var0, int var1) {
      ExpClass var2 = var0.getParameter(var1);
      var0.setResult(var2.getValue());
      var0.setType(var2.getType());
   }

   private static boolean parameterIsNull(ExpClass var0, int var1) {
      ExpClass var2 = var0.getParameter(var1);
      return var2.getType() == 0;
   }

   private static int getExpType(ExpClass var0, int var1) {
      ExpClass var2 = var0.getParameter(var1);
      return var2.getType();
   }

   private static int getExpFlag(ExpClass var0, int var1) {
      ExpClass var2 = var0.getParameter(var1);
      return var2.getFlag();
   }

   private static Object getParameter(ExpClass var0, int var1, boolean var2, int var3, boolean var4) throws Exception {
      ExpClass var5 = var0.getParameter(var1);
      Object var6 = var5.getValue();
      int var7 = var5.getType();
      if (var7 == 0) {
         return null;
      } else if (!var2) {
         return var6;
      } else if (var7 != var3) {
         if (var4) {
            throw new Exception("Hibás vagy hiányzó paraméter! : (" + var1 + ")");
         } else {
            return null;
         }
      } else {
         return var6;
      }
   }

   private static int getDinCnt(ExpClass var0, int var1, int var2) throws Exception {
      byte var3 = 0;

      try {
         for(int var6 = var2; var6 < var1; var6 += 2) {
            if (parameterIsNull(var0, var6) || parameterIsNull(var0, var6 + 1)) {
               return (var6 - var2) / 2;
            }
         }

         return (var1 - var2) / 2;
      } catch (Exception var5) {
         throw new Exception("Hibás vagy hiányzó paraméter! : (" + var3 + ")");
      }
   }

   private static void setSearchParameters(ExpClass var0, MatrixSearchModel var1, int var2, boolean var3, boolean var4) throws Exception {
      byte var5 = 0;

      try {
         for(int var9 = var2; var9 < var0.getParametersCount(); var9 += 2) {
            if (parameterIsNull(var0, var9)) {
               return;
            }

            Integer var6 = NumericOperations.getInteger(getParameter(var0, var9, true, 2, true)) - 1;
            String var7 = ABEVFeaturedBaseFunctions.getString(getParameter(var0, var9 + 1, false, 0, true), "");
            var1.addSearchItem(new MatrixSearchItem(var6, var4 ? "tartalmaz" : "=", var7, var3));
         }

      } catch (Exception var8) {
         var8.printStackTrace();
         throw new Exception("Hibás vagy hiányzó paraméter! : (" + var5 + ")");
      }
   }

   private static void setSearchParameters(ExpClass var0, MatrixSearchModel var1, int var2) throws Exception {
      byte var3 = 0;

      try {
         for(int var9 = var2; var9 < var0.getParametersCount(); var9 += 4) {
            if (parameterIsNull(var0, var9) || parameterIsNull(var0, var9 + 1) || parameterIsNull(var0, var9 + 3)) {
               return;
            }

            Integer var4 = NumericOperations.getInteger(getParameter(var0, var9, true, 2, true)) - 1;
            String var5 = (String)((String)getParameter(var0, var9 + 1, true, 1, true));
            String var6 = ABEVFeaturedBaseFunctions.getString(getParameter(var0, var9 + 2, false, 0, true), "");
            Boolean var7 = (Boolean)((Boolean)getParameter(var0, var9 + 3, true, 4, true));
            var1.addSearchItem(new MatrixSearchItem(var4, var5, var6, var7));
         }

      } catch (Exception var8) {
         var8.printStackTrace();
         throw new Exception("Hibás vagy hiányzó paraméter! : (" + var3 + ")");
      }
   }

   private static Object[] pieces(String var0, String var1) {
      String[] var2 = var0.split(var1);
      Object[] var3 = new Object[var2.length];

      for(int var4 = 0; var4 < var2.length; ++var4) {
         String var5 = var2[var4];
         var3[var4] = new Object[]{var5, String.valueOf(1)};
      }

      return var3;
   }

   private static Object[] sensitiveRow(Object var0) {
      if (var0 instanceof Object[]) {
         Object[] var1 = (Object[])((Object[])var0);
         Object[] var2 = new Object[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            Object[] var4 = (Object[])((Object[])var1[var3]);
            Object var5 = var4[0];
            String var6 = null;
            if (var5 instanceof String) {
               var6 = ((String)var5).toLowerCase();
            }

            var2[var3] = new Object[]{var6 != null ? var6 : var4[0], var4[1]};
         }

         return var2;
      } else {
         return null;
      }
   }

   private static void log(Object var0) {
      if (var0 == null) {
         System.out.println("null");
      } else {
         if (var0 instanceof Object[]) {
            Object[] var1 = (Object[])((Object[])var0);

            for(int var2 = 0; var2 < var1.length; ++var2) {
               Object var3 = var1[var2];
               log(var3);
            }
         } else {
            System.out.println(var0.getClass().toString() + " - " + var0.toString());
         }

      }
   }

   public static int specPageCounter(ExpClass var0, String var1, String var2, String var3) {
      int var4 = -1;
      PageModel var8 = getPageByName(var0, var1);
      if (var8 == null) {
         return var4;
      } else {
         if (getPageIsDynamic(var0, var1, var8)) {
            Integer var5 = getPageDeepCount(var0, var1, var8);
            if (var5 != null && var5 instanceof Integer) {
               var4 = (Integer)var5;
               if (var4 == 1) {
                  var4 = -1;
                  String var9 = var3 == null ? getFidByVid(var2, getFormid()) : var3;
                  if (var9 != null) {
                     var4 = 0;
                     ExpClass var7 = getDFieldValue(var0, var9, 0);
                     if (var7 != null) {
                        Object var6 = var7.getValue();
                        if (var6 != null) {
                           int var10 = var7.getType();
                           if (var10 == 4) {
                              var4 = (Boolean)var6 ? 1 : 0;
                           } else {
                              String var11 = var6.toString();
                              if (var11.compareTo("") != 0 && var11.compareTo("0") != 0) {
                                 var4 = 1;
                              }
                           }
                        }
                     }
                  } else {
                     writeError(var0, ID_EX_FN_FIELD_ID, "Program hiba, hiba történt a mező azonosító meghatározásakor!", (Exception)null, var0);
                  }
               }
            }
         } else {
            writeError(var0, ID_EX_FN_PAGE_DYN, "Program hiba, nem dinamikus a lap!", (Exception)null, var0);
         }

         return var4;
      }
   }

   private static String getFidByVid(String var0, String var1) {
      if (var0 == null) {
         return null;
      } else {
         String var2 = getRealVidCode(var0);
         String var3 = null;

         try {
            Hashtable var4 = (Hashtable)g_vids.get(g_active_form_id);
            if (var4 != null) {
               var3 = (String)var4.get(var2);
            }

            if (var3 == null) {
               ids_info[0] = null;
               ids_info[1] = var2;
               Object[] var5 = (Object[])((Object[])getIDs(ids_info, var1));
               if (var5 != null) {
                  var3 = (String)var5[0];
               }
            }

            if (var3 != null) {
               if (var4 == null) {
                  var4 = new Hashtable();
                  g_vids.put(g_active_form_id, var4);
               }

               var4.put(var2, var3);
            }
         } catch (Exception var6) {
            Tools.eLog(var6, 1);
         }

         return var3;
      }
   }

   private static String getVidByFid(String var0, String var1) {
      if (var0 == null) {
         return null;
      } else {
         ids_info[0] = var0;
         ids_info[1] = null;
         Object[] var2 = (Object[])((Object[])getIDs(ids_info, var1));
         return var2 != null ? (String)var2[1] : null;
      }
   }

   private static Object getIDs(Object[] var0, String var1) {
      Object var2 = meta_info.getIds(var0, var1);
      return var2;
   }

   public static void initFormid() {
      if (activeFormActivity) {
         stat_form_id = g_active_form_id;
      } else {
         stat_form_id = outerFormId;
      }

      if (stat_form_id == null || stat_form_id.length() == 0) {
         System.out.println("Hiba: FunctionBodies.getFormid: " + stat_form_id);
      }

   }

   public static String getFormid() {
      return stat_form_id;
   }

   public static PageModel checkPageName(ExpClass var0, String var1) {
      return getPageByName(var0, var1);
   }

   public static PageModel getPageByName(ExpClass var0, String var1) {
      String var2 = getRealPageName(var1);
      PageModel var3 = null;
      FormModel var4 = gui_info.get(g_active_form_id);
      if (var4 != null) {
         var3 = (PageModel)var4.names_page.get(var2);
      }

      if (var3 == null) {
         writeError(var0, ID_ERR_GUI_INFO, "Hiba GUI info szolgáltatások lekérdezése során! (lapkérés)", (Exception)null, var1);
      }

      return var3;
   }

   public static PageModel getPageNameById(ExpClass var0, String var1) {
      PageModel var2 = null;
      FormModel var3 = gui_info.get(g_active_form_id);
      if (var3 != null) {
         var2 = (PageModel)var3.fids_page.get(var1);
      }

      if (var2 == null) {
         writeError(var0, ID_ERR_GUI_INFO, "Hiba GUI info szolgáltatások lekérdezése során! (lapkérés)", (Exception)null, var1);
      }

      return var2;
   }

   public static String getPageId(ExpClass var0, String var1) {
      PageModel var2 = getPageByName(var0, var1);
      if (var2 != null) {
         return var2.pid;
      } else {
         writeError(var0, ID_ERR_GUI_INFO, "Hiba GUI info szolgáltatások lekérdezése során! (lapazonosító)", (Exception)null, var1);
         return null;
      }
   }

   public static boolean isPageDynamic(ExpClass var0, String var1) {
      PageModel var2 = getPageByName(var0, var1);
      return getPageIsDynamic(var0, var1, var2);
   }

   public static boolean getPageIsDynamic(ExpClass var0, String var1, PageModel var2) {
      if (var2 != null) {
         return var2.dynamic;
      } else {
         writeError(var0, ID_ERR_GUI_INFO, "Hiba GUI info szolgáltatások lekérdezése során! (dinamikus-e a lap)", (Exception)null, var1);
         return false;
      }
   }

   public static Integer getPageDeepCount(ExpClass var0, String var1, PageModel var2) {
      if (var2 != null) {
         FormModel var3 = gui_info.get(g_active_form_id);
         int[] var4 = gui_info.get_pagecounts();
         return new Integer(var4[var3.get(var2)]);
      } else {
         if (var0 != null) {
            writeError(var0, ID_ERR_GUI_INFO, "Hiba GUI info szolgáltatások lekérdezése során! (dinamikus lapok száma)", (Exception)null, var1);
         }

         return null;
      }
   }

   private static String getPageActPageName(ExpClass var0) {
      try {
         Object[] var1 = getSelf();
         return var1 == null ? null : FIdHelper.getPageId(var1);
      } catch (Exception var2) {
         writeError(var0, ERR_FN_ID, "Számítási hiba! ", (Exception)null, (Object)null);
         return null;
      }
   }

   private static String getPageNameByField(ExpClass var0, String var1) {
      try {
         return FIdHelper.getPageId(var1);
      } catch (Exception var3) {
         writeError(var0, ERR_FN_ID, "Számítási hiba! ", var3, var1);
         return null;
      }
   }

   private static void setFieldValue(ExpClass var0, String var1, Object var2) {
      Object[] var3 = FIdHelper.getDataStoreKey(var1);
      setDataValue(var0, var3[0].toString() + "_" + var3[1].toString(), var1, var2);
   }

   private static void setDataValue(ExpClass var0, String var1, String var2, Object var3) {
      IDataStore var4 = g_active_data_store;
      if (var3 == null) {
         ic.writeDataStore(var4, var1, "");
      } else {
         ic.writeDataStore(var4, var1, NumericOperations.getRoundedValue(var2, var3));
      }

   }

   private static String getSelfFid(ExpClass var0) {
      return FIdHelper.getFieldId(getSelf());
   }

   private static Object[] getSelf() {
      return g_current_field_id;
   }

   private static ExpClass getFieldValue(ExpClass var0, String var1) {
      return getFValue(var1, (Integer)null);
   }

   private static ExpClass getDFieldValue(ExpClass var0, String var1, int var2) {
      return getFValue(var1, new Integer(var2));
   }

   private static ExpClass getFValue(String var0, Integer var1) {
      try {
         IDataStore var2 = g_active_data_store;
         if (var2 != null) {
            try {
               Object[] var3;
               if (var1 == null) {
                  var3 = FIdHelper.getDataStoreKey(var0);
               } else {
                  g_ds_key[0] = var1;
                  g_ds_key[1] = var0 == null ? "" : var0;
                  var3 = g_ds_key;
               }

               g_exp_fields.add((String)var3[1], (Integer)var3[0]);
               String var5 = var2.get(var3);
               if (var5 == null) {
                  var5 = "";
               }

               ExpClass var4 = ExpFactoryLight.createConstant(var5, getFieldType(var0));
               return var4;
            } catch (Exception var6) {
               Tools.eLog(var6, 1);
            }
         }
      } catch (Exception var7) {
         Tools.eLog(var7, 1);
      }

      return null;
   }

   private static BigDecimal getDFieldNumberValue(String var0, Integer var1) {
      try {
         IDataStore var2 = g_active_data_store;
         if (var2 != null) {
            try {
               Object[] var3;
               if (var1 == null) {
                  var3 = FIdHelper.getDataStoreKey(var0);
               } else {
                  g_ds_key[0] = var1;
                  g_ds_key[1] = var0 == null ? "" : var0;
                  var3 = g_ds_key;
               }

               g_exp_fields.add((String)var3[1], (Integer)var3[0]);
               BigDecimal var4 = NumericOperations.getBigDecimal(var2.get(var3));
               return var4;
            } catch (Exception var5) {
               Tools.eLog(var5, 1);
            }
         }
      } catch (Exception var6) {
         Tools.eLog(var6, 1);
      }

      return null;
   }

   private static void getFormMatrixData(ExpClass var0, String var1, boolean var2, boolean var3, boolean var4, int var5, String var6, int var7, int var8, int var9) throws Exception {
      int var10 = var5 - 1;
      String var12 = null;
      int var11 = getDinCnt(var0, var9, var7);
      if (var11 == 0) {
         setDefault(var0, var8);
      } else {
         int[] var13 = new int[var11];
         Object[] var14 = new Object[10];
         MatrixSearchModel var15 = new MatrixSearchModel(var1, var6);
         setSearchParameters(var0, var15, var7, var2, var3);
         Vector var16 = matrixHandler.search(getFormid(), var15, var4, false);
         if (var16 == null) {
            setDefault(var0, var8);
         } else {
            var12 = getSearchedItems(var16, var10);
            var0.setResult(var12);
            var0.setType(1);
         }

      }
   }

   private static String getSearchedItems(List var0, int var1) {
      StringBuffer var2 = new StringBuffer();
      if (var0 != null) {
         int var3 = var0.size();
         if (var3 > 0) {
            Object[] var4 = (Object[])((Object[])var0.get(0));
            if (var4.length > var1) {
               var2.append(var4[var1]);
            }

            for(int var5 = 1; var5 < var3; ++var5) {
               Object[] var6 = (Object[])((Object[])var0.get(var5));
               var2.append("\n");
               if (var6.length > var1) {
                  var2.append(var6[var1]);
               }
            }
         }
      }

      return var2.toString();
   }

   public static void debug(ExpClass var0, String var1) {
      try {
         ExpClass var2 = var0.getParameter(0);
         String var3 = var2.getIdentifier();
         if (var3.equals("field")) {
            ExpClass var4 = var2.getParameter(0);

            try {
               if (var4.getValue().equals(var1)) {
                  throw new Exception("debug");
               }
            } catch (Exception var6) {
               Tools.eLog(var6, 1);
            }
         }
      } catch (Exception var7) {
         Tools.eLog(var7, 1);
      }

   }

   public static void setOuterFormId(String var0) {
      outerFormId = var0;
   }

   public static Hashtable getDeptFields(String[] var0, Object[] var1) {
      Hashtable var2 = new Hashtable();

      try {
         for(int var3 = 2; var3 < var0.length; ++var3) {
            var2.put(ABEVFunctionSet.getParam(var0[var3]), "");
         }

         return var2;
      } catch (Exception var4) {
         return null;
      }
   }

   public static Hashtable getAllInputFields(String[] var0, Object[] var1) {
      Hashtable var2 = new Hashtable();

      try {
         for(int var3 = 1; var3 < var0.length; ++var3) {
            var2.put(ABEVFunctionSet.getParam(var0[var3]), "");
         }

         return var2;
      } catch (Exception var4) {
         return null;
      }
   }

   public static Hashtable getDeptFieldsBySpec(String var0, Object[] var1) {
      Hashtable var2 = new Hashtable();

      try {
         Vector var3 = getCachedFieldFids((ExpClass)null, var0, getFormid());
         if (var3 != null) {
            for(int var4 = 0; var4 < var3.size(); ++var4) {
               var2.put(var3.elementAt(var4), "");
            }
         }

         return var2;
      } catch (Exception var5) {
         return null;
      }
   }

   public static Hashtable getDeptFieldsBySpecIntAtfed(String[] var0, Object[] var1) {
      Hashtable var2 = new Hashtable();

      try {
         storeFieldIdDs(var2, ABEVFunctionSet.getParam(var0[1]), var1);
         storeFieldIdDs(var2, ABEVFunctionSet.getParam(var0[2]), var1);

         for(int var3 = 4; var3 < var0.length; var3 += 3) {
            storeFieldIdDs(var2, ABEVFunctionSet.getParam(var0[var3]), var1);
         }

         return var2;
      } catch (Exception var4) {
         Tools.eLog(var4, 1);
         return null;
      }
   }

   public static Hashtable getDeptFieldsBySpecIntAtfedCsoport(String[] var0, Object[] var1) {
      Hashtable var2 = new Hashtable();

      try {
         storeFieldIdDs(var2, ABEVFunctionSet.getParam(var0[1]), var1);
         storeFieldIdDs(var2, ABEVFunctionSet.getParam(var0[2]), var1);
         storeFieldIdDs(var2, ABEVFunctionSet.getParam(var0[4]), var1);

         for(int var3 = 5; var3 < var0.length; var3 += 3) {
            storeFieldIdDs(var2, ABEVFunctionSet.getParam(var0[var3]), var1);
         }

         return var2;
      } catch (Exception var4) {
         Tools.eLog(var4, 1);
         return null;
      }
   }

   public static Hashtable getDeptFieldsBySpecNapokSzama(String[] var0, Object[] var1) {
      Hashtable var2 = new Hashtable();

      try {
         storeFieldIdDs(var2, ABEVFunctionSet.getParam(var0[2]), var1);
         storeFieldIdDs(var2, ABEVFunctionSet.getParam(var0[3]), var1);
         storeFieldIdDs(var2, ABEVFunctionSet.getParam(var0[7]), var1);

         for(int var3 = 8; var3 < var0.length; var3 += 3) {
            storeFieldIdDs(var2, ABEVFunctionSet.getParam(var0[var3]), var1);
         }

         return var2;
      } catch (Exception var4) {
         Tools.eLog(var4, 1);
         return null;
      }
   }

   private static void storeFieldIdDs(Hashtable var0, String var1, Object[] var2) {
      if (var1 != null && var1.length() != 0) {
         if (isFieldIdType(var1)) {
            var0.put(var1, "");
         } else {
            Hashtable var3 = getDeptFieldsBySpec(var1, var2);
            if (var3 != null) {
               var0.putAll(var3);
            }

         }
      }
   }

   public static Hashtable getDeptFieldsBySpecTableUnique(String[] var0, Object[] var1) {
      Hashtable var2 = new Hashtable();

      try {
         for(int var3 = 3; var3 < var0.length; ++var3) {
            String var4 = ABEVFunctionSet.getParam(var0[var3]);
            Hashtable var5 = getDeptFieldsBySpec(var4, var1);
            if (var5 != null) {
               var2.putAll(var5);
            }
         }

         return var2;
      } catch (Exception var6) {
         return null;
      }
   }

   public static Hashtable getDepFidByVid(String var0, Object var1) {
      Hashtable var2 = new Hashtable();
      String var3 = getFidByVid(var0, getFormid());
      if (var3 != null) {
         var2.put(var3, "");
      }

      return var2;
   }

   public static Hashtable collectSpecialDependencies(String var0, String var1, Object var2, Hashtable var3) {
      Hashtable var4;
      if (var3 == null) {
         var4 = new Hashtable();
      } else {
         var4 = var3;
      }

      String var7 = getFormid();
      Hashtable var5 = (Hashtable)var4.get(var7);
      if (var5 == null) {
         var5 = new Hashtable();
         var4.put(var7, var5);
      }

      Hashtable var6 = (Hashtable)var5.get(var0);
      if (var6 == null) {
         var6 = new Hashtable();
         var5.put(var0, var6);
      }

      if (var0.equalsIgnoreCase("dept_type_dinpages")) {
         Hashtable var8 = getSpecialDinPageDept(var1, var2);
         if (var8 != null) {
            var6.putAll(var8);
         }
      }

      return var4;
   }

   public static Hashtable getSpecialDinPageDept(String var0, Object var1) {
      Hashtable var2 = new Hashtable();
      if (var1 != null) {
         String var3 = getFormid();
         String var4 = getFidByVid(var0, var3);
         if (var4 != null) {
            var2.put(var4, var1);
         }
      }

      return var2;
   }

   public static Hashtable getDepFidByFormVid(String var0, Object var1, String var2, String var3) {
      Hashtable var4 = new Hashtable();
      String var5 = var2;
      if (var0.indexOf(var2) == -1) {
         var5 = var3;
      }

      int var6 = var0.indexOf(var5);
      var0.substring(0, var6);
      String var8 = var0.substring(var6 + 1);
      String var9 = getFidByVid(var8, getFormid());
      if (var9 != null) {
         var4.put(var9, "");
      }

      return var4;
   }

   public static Hashtable getDepSpec5fgv(String var0, Object var1) {
      Hashtable var2 = null;

      try {
         String var3 = "[" + var0 + "]_A[1..20]";
         String var4 = "[" + var0 + "]_D[1..20]";
         Vector var5 = getCachedFieldFids((ExpClass)null, var3);
         Vector var6 = getCachedFieldFids((ExpClass)null, var4);
         var2 = toArray(var5, var2);
         var2 = toArray(var6, var2);
         return var2;
      } catch (Exception var7) {
         return null;
      }
   }

   public static Hashtable toArray(Vector var0, Hashtable var1) {
      if (var0 == null) {
         return null;
      } else {
         Hashtable var2 = var1;
         if (var1 == null) {
            var2 = new Hashtable();
         }

         for(int var3 = 0; var3 < var0.size(); ++var3) {
            var2.put(var0.elementAt(var3), "");
         }

         return var2;
      }
   }

   public static synchronized void fnUniqueM(ExpClass var0) {
      byte var2 = 0;
      Object var3 = null;
      var0.setType(var2);
      var0.setResult(var3);
      int var1 = var0.getParametersCount();
      if (var1 == 2) {
         String var4;
         String var5;
         try {
            var5 = (String)getParameter(var0, 0, true, 1, true);
            var4 = (String)getParameter(var0, 1, true, 1, true);
         } catch (Exception var8) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
            return;
         }

         ICachedItem var7 = g_cached_items.getResult(var5, "unique_m", var4);
         Object[] var6 = var7.getResult();
         g_extended_error = collectUniquemUserData(var7.getErrors());
         var0.setType((Integer)var6[1]);
         var0.setResult(var6[0]);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   private static String collectUniquemUserData(Vector var0) {
      if (var0.size() == 0) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         var1.append("Ismétlődő értékek:");
         var1.append(" ");

         for(int var2 = 0; var2 < var0.size(); ++var2) {
            Object[] var3 = (Object[])((Object[])var0.elementAt(var2));
            var1.append(var3[3]);
            var1.append(",");
         }

         return var1.substring(0, var1.lastIndexOf(","));
      }
   }

   public static boolean uniquemBody(String var0, String var1, Object var2, Vector var3) {
      Hashtable var4 = (Hashtable)var2;
      ExpClass var5 = getFieldValue((ExpClass)null, var1);
      Object var6 = var5.getValue();
      int var7 = var5.getType();
      if (var7 == 0) {
         return true;
      } else if (var4.containsKey(var6)) {
         var3.add(new Object[]{ID_FILLIN_ERROR, "[Nyomtatvány] Nem egyedi a mező tartalma!  " + var0 + ":" + var1 + ", Érték:" + (var6.toString().equalsIgnoreCase("##null##") ? "üres" : var6.toString()), null, var6.toString(), null});
         return false;
      } else {
         var4.put(var6, "");
         return true;
      }
   }

   public static synchronized void fnCompareAM(ExpClass var0) {
      byte var2 = 0;
      Object var3 = null;
      var0.setType(var2);
      var0.setResult(var3);
      int var1 = var0.getParametersCount();
      if (var1 == 5) {
         String var4;
         String var5;
         String var6;
         String var7;
         String var8;
         try {
            var4 = (String)getParameter(var0, 0, true, 1, true);
            var5 = (String)getParameter(var0, 1, true, 1, true);
            var6 = (String)getParameter(var0, 2, true, 1, true);
            var7 = (String)getParameter(var0, 3, true, 1, true);
            var8 = (String)getParameter(var0, 4, true, 1, true);
         } catch (Exception var16) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
            return;
         }

         if (var4 == null || var5 == null || var6 == null || var7 == null || var8 == null) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
            return;
         }

         byte var9 = -1;
         if (var8.equalsIgnoreCase("=")) {
            var9 = 9;
         } else if (var8.equalsIgnoreCase("<")) {
            var9 = 7;
         } else if (var8.equalsIgnoreCase(">")) {
            var9 = 8;
         } else if (var8.equalsIgnoreCase("<=")) {
            var9 = 10;
         } else if (var8.equalsIgnoreCase(">=")) {
            var9 = 11;
         } else if (var8.equalsIgnoreCase("<>")) {
            var9 = 12;
         }

         if (var9 == -1) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, "parameter 5");
            return;
         }

         ExpClass var10;
         if (g_cached_items.getMode() == 1) {
            String var11 = var4 + "_" + var5;
            if (g_compaream.containsKey(var11)) {
               var10 = (ExpClass)g_compaream.get(var11);
            } else {
               var10 = getMultiValue(var0, var4, var5);
               if (var10 != null) {
                  g_compaream.put(var11, var10);
               }
            }
         } else {
            var10 = getMultiValue(var0, var4, var5);
         }

         if (var10 == null) {
            var2 = 4;
            var3 = Boolean.FALSE;
         } else {
            Object var17 = var10.getValue();
            int var12 = var10.getType();
            ExpClass var13 = getFieldValue(var0, var7);
            Object var14 = var13.getValue();
            int var15 = var13.getType();
            if (var15 != 2 && var15 != 1 && var15 != 4 && var15 != 0 || var15 != var12 && var15 != 0 && var12 != 0) {
               writeTypeMismatchError(var12, (Exception)null, (Object)null, var0, var13);
               var0.setType(0);
               var0.setResult((Object)null);
               return;
            }

            if (var15 == 4 && var12 == 0) {
               var17 = Boolean.FALSE;
            }

            if (var15 == 0 && var12 == 4) {
               var14 = Boolean.FALSE;
            }

            var2 = 4;
            var3 = NumericOperations.processArithmAndLogical(var9, var17, var14);
            if (!(var3 instanceof Boolean)) {
               writeError(var0, ID_ERR_CHECK_COMP, "Fő/részbizonylat összehasonlítási hiba! ", (Exception)null, var6 + "/" + var7 + ": " + (var14 == null ? "Üres" : var14.toString()) + ", " + var4 + "/" + var5 + ": " + (var17 == null ? "Üres" : var17.toString()));
               var2 = 0;
               var3 = null;
            }
         }

         var0.setType(var2);
         var0.setResult(var3);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   private static ExpClass getMultiValue(ExpClass var0, String var1, String var2) {
      ExpClass var4 = null;
      boolean var7 = true;
      IPropertyList var5 = (IPropertyList)gui_info.get_store_collection();
      int var3 = gui_info.getCalcelemindex();
      saveActiveParameters();

      try {
         int var10;
         if (g_cached_items.getMode() == 1) {
            if (g_compaream_source == -1) {
               var10 = getFirstDataStoreByFormId(var5, var1);
               if (var10 != -1) {
                  g_compaream_source = var10;
               }
            } else {
               var10 = g_compaream_source;
            }
         } else {
            var10 = getFirstDataStoreByFormId(var5, var1);
         }

         gui_info.setCalcelemindex(var10);
         Elem var6 = (Elem)((Vector)var5).get(var10);
         if (var6 != null) {
            IDataStore var8 = (IDataStore)var6.getRef();
            setActiveParameters(var1, var8);
            var4 = getFieldValue(var0, var2);
         }
      } catch (Exception var9) {
         var9.printStackTrace();
         writeError(var0, ID_ERR_GET_MULTI_VALUE, "Hiba másik nyomtatvány adatának az elkérésekor! ", var9, var1 + ":" + var2);
      }

      gui_info.setCalcelemindex(var3);
      restoreActiveParameters();
      return var4;
   }

   private static int getFirstDataStoreByFormId(IPropertyList var0, String var1) {
      int[] var2 = (int[])((int[])var0.get(new Object[]{"filterfirstitem", var1}));
      return var2.length != 0 && var2[0] != -1 ? var2[0] : -1;
   }

   public static Hashtable getFieldMetas() {
      return getFieldTables("meta", getfieldmetas);
   }

   public static Hashtable getFieldGuis() {
      return getFieldTables("gui", getfieldguis);
   }

   private static Hashtable getFieldTables(String var0, Object[] var1) {
      String var2 = getFormid();
      if (var2 != null) {
         Object var3 = staticData.get(var2);
         if (var3 == null) {
            var3 = new Hashtable();
            staticData.put(var2, var3);
         }

         Object var4 = ((Hashtable)var3).get(var0);
         if (var4 != null) {
            return (Hashtable)var4;
         }

         Object var5 = meta_info.getFieldMetas(var2);
         if (var5 != null) {
            ((Hashtable)var3).put(var0, var5);
            return (Hashtable)var5;
         }
      }

      return null;
   }

   public static void initStaticData() {
      staticData = new Hashtable();
      g_active_form_id = null;
      g_current_field_id = null;
      g_previous_item = null;
      g_calc_record = null;
      g_active_data_store = null;
      g_readonly_calc_state = false;
      g_readonly_calc_act_page_number = new Integer(0);
      g_field_types = new Hashtable();
      g_table_fids = new Hashtable();
      g_readOnlyCalcFields = new Hashtable();
      g_feltetelesErtekFields = new Hashtable();
      g_page_fids = new Hashtable();
      g_visible_page_fids = new Hashtable();
      g_vids = new Hashtable();
      g_rows = new Hashtable();
      g_function_description = new Hashtable();
      g_variables = new Hashtable();
      g_din_expressions = new Hashtable();
      g_saved_active_form_id = null;
      g_saved_active_data_store = null;
      FIdHelper.fieldInfoTables = new Hashtable();
      FIdHelper.otherDynPage = false;
      FIdHelper.dynPageSettings = new Hashtable();
      g_cached_items = new CachedItems();
      g_designed_page_names = new Hashtable();
      g_real_page_names = new Hashtable();
      g_designed_vid_names = new Hashtable();
      g_real_vid_names = new Hashtable();
      g_exp_fields = new FunctionBodies.ExpressionFields();
      g_gen_ertek_changed_field_value = new ArrayList();
      g_fo_adat_dependency = new HashMap();
   }

   public static void initCompareAM() {
      g_compaream = new Hashtable();
      g_compaream_source = -1;
   }

   private static Object getFieldMetaAttr(String var0, String var1) {
      if (var0 != null && var1 != null) {
         Hashtable var2 = getFieldMetas();
         if (var2 != null) {
            Object var3 = ((Hashtable)var2).get(var0);
            if (var3 != null) {
               return ((Hashtable)var3).get(var1);
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private static boolean existFieldMetaAttr(String var0, String var1) {
      if (var0 != null && var1 != null) {
         Hashtable var2 = getFieldMetas();
         if (var2 != null) {
            Object var3 = ((Hashtable)var2).get(var0);
            if (var3 != null) {
               return ((Hashtable)var3).containsKey(var1);
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static int getFieldType_meta(String var0) {
      Object var1 = getFieldMetaAttr(var0, "type");
      if (var1 == null) {
         return -1;
      } else {
         try {
            return Integer.parseInt((String)var1);
         } catch (NumberFormatException var3) {
            return -1;
         }
      }
   }

   public static int getFieldType(String var0) {
      boolean var1 = true;
      Hashtable var2 = (Hashtable)g_field_types.get(g_active_form_id);
      Object var3 = var2.get(var0);
      int var4 = (Integer)var3;
      return var4;
   }

   public static boolean getFieldCalcZE(String var0) {
      return existFieldMetaAttr(var0, "calc_ze");
   }

   public static int getFieldRound(String var0) {
      Object var1 = getFieldMetaAttr(var0, "round");
      if (var1 == null) {
         return -1;
      } else {
         try {
            return Integer.valueOf((String)var1);
         } catch (NumberFormatException var3) {
            return -2;
         }
      }
   }

   public static boolean isFieldGuiDataType(String var0, int var1) {
      if (var0 == null) {
         return false;
      } else {
         int var2 = ((DataFieldModel)gui_info.get(g_active_form_id).fids.get(var0)).type;
         return var2 == var1;
      }
   }

   private static boolean kitoltottNumField(ExpClass var0, Object var1) {
      if (var1 == null) {
         return false;
      } else {
         String var2 = var1.toString();
         if (var2.length() == 0) {
            return false;
         } else {
            if (var2.equals("0")) {
               String var3 = getFidFromExp(var0);
               if (var3 != null) {
                  return getFieldCalcZE(var3);
               }
            }

            return true;
         }
      }
   }

   private static String getFidFromExp(ExpClass var0) {
      String var1 = "field";
      String var2 = var0.getIdentifier();
      if (var2 != null && var2.compareToIgnoreCase(var1) == 0) {
         ExpClass var3 = var0.getParameter(0);
         String var4 = (String)var3.getValue();
         return var4;
      } else {
         return null;
      }
   }

   private static Vector<String> getCachedFieldFids(ExpClass var0, String var1) {
      return getCachedFieldFids(var0, var1, g_active_form_id);
   }

   private static Vector<String> getCachedFieldFids(ExpClass var0, String var1, String var2) {
      Vector var3 = null;

      try {
         Hashtable var4 = (Hashtable)g_table_fids.get(var2);
         if (var4 != null) {
            var3 = (Vector)var4.get(var1);
         }

         if (var3 == null) {
            Object[] var5 = convABEVtableIDs(var1);
            var3 = getFieldFids(var5);
            if (var3 != null) {
               if (var4 == null) {
                  var4 = new Hashtable();
                  g_table_fids.put(var2, var4);
               }

               var4.put(var1, var3);
            }
         }

         return var3;
      } catch (Exception var6) {
         writeError(var0, ERR_FN_ID, "Számítási hiba! " + var6.getMessage() + " Paraméter:" + var1, var6, var0);
         return var3;
      }
   }

   public static void storeErrors(ExpClass var0, Vector var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         Object[] var3 = (Object[])((Object[])var1.elementAt(var2));
         writeError(var0, (Long)var3[0], (String)var3[1], (Exception)var3[2], var3[3]);
      }

   }

   public static synchronized void fnEvesAdo2007(ExpClass var0) {
      double var2 = 0.0D;
      double var4 = 0.0D;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var6 = var0.getParameter(0);
         if (var6 == null) {
            return;
         }

         Object var7 = var6.getValue();
         if (var7 == null) {
            return;
         }

         var4 = NumericOperations.getDouble(var7);
         if (var4 <= 1700000.0D) {
            var2 = Tz_round(0.18D * var4);
         }

         if (var4 > 1700000.0D) {
            var2 = Tz_round(0.36D * (var4 - 1550000.0D)) + 306000.0D;
         }

         BigDecimal var8 = NumericOperations.tryIntegerNumberFormat(NumericOperations.checkDouble(new Double(var2)));
         var0.setType(2);
         var0.setResult(var8);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnFeltetelesErtek2(ExpClass var0) {
      fnFeltetelesErtek(var0);
   }

   public static synchronized void fnKeresesMatrixban3(ExpClass var0) {
      byte var1 = 6;
      byte var2 = 3;
      boolean var9 = false;
      var0.setType(0);
      var0.setResult((Object)null);
      int var3 = var0.getParametersCount();
      if (var1 + 2 > var3) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            boolean var11 = false;
            String var5 = ((String)getParameter(var0, 0, true, 1, var11)).toLowerCase();
            Boolean var7 = (Boolean)getParameter(var0, 1, true, 4, var11);
            Integer var4 = NumericOperations.getInteger(getParameter(var0, 2, true, 2, var11));
            Object var10 = getParameter(var0, 3, false, 0, var11);
            String var6 = (String)getParameter(var0, 4, true, 1, var11);
            Boolean var8 = (Boolean)getParameter(var0, 5, true, 4, var11);
            if (var5 == null || var7 == null || var4 == null || var10 == null || var6 == null) {
               return;
            }

            getFormMatrixData(var0, var5, var7, var8, var9, var4, var6, var1, var2, var3);
         } catch (Exception var12) {
            var12.printStackTrace();
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var12.getMessage(), var12, var0);
         }

      }
   }

   public static synchronized void fnDinLapKapcsol(ExpClass var0) {
      byte var2 = 0;
      Object var3 = null;
      var0.setType(var2);
      var0.setResult(var3);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var4;
         try {
            var4 = var0.getParameter(0);
         } catch (Exception var8) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
            return;
         }

         if (ic == null) {
            return;
         }

         Hashtable var5 = new Hashtable();
         getDinPagesByField(var4, var5);

         try {
            String var6 = FIdHelper.getPageId(g_current_field_id);
            var5.remove(var6);
         } catch (Exception var7) {
            Tools.eLog(var7, 0);
         }

         Hashtable var9 = new Hashtable();
         storeDeepNumber(var5);
         dinKapcsol(var0, var5, var9, var4);
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnDinOldalKapcsol(ExpClass var0) {
      byte var2 = 0;
      Object var3 = null;
      var0.setType(var2);
      var0.setResult(var3);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var4;
         try {
            var4 = var0.getParameter(0);
         } catch (Exception var11) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
            return;
         }

         if (ic == null) {
            return;
         }

         Hashtable var5 = new Hashtable();
         getDinPagesByField(var4, var5);
         if (var5.size() > 1) {
            writeError(var0, ID_EX_FN_DIN_OLDAL, "Sablon hiba! Csak az aktuális laphoz tartalmazó mezőket tartalmazhat a kifejezés.", (Exception)null, var5.keySet().toString());
            return;
         }

         String var6 = null;

         try {
            if (var5.size() == 1) {
               var6 = FIdHelper.getPageId(g_current_field_id);
               if (!var5.containsKey(var6)) {
                  writeError(var0, ID_EX_FN_DIN_OLDAL, "Sablon hiba! Csak az aktuális laphoz tartalmazó mezőket tartalmazhat a kifejezés.", (Exception)null, var5.keySet().toString());
                  return;
               }
            }
         } catch (Exception var10) {
            Tools.eLog(var10, 0);
         }

         int var7 = getDPageNumber();
         Hashtable var8 = new Hashtable();
         if (var6 != null) {
            Hashtable var9 = new Hashtable();
            var9.put(new Integer(var7), "");
            var8.put(var6, var9);
         }

         boolean var12 = storeDeepNumber(var5);
         dinKapcsol(var0, var5, var8, var4);
         if (var0.getType() == 2 && (Integer)var0.getValue() == 0 && var12) {
            g_all_fileds_empty = false;
         }
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void dinKapcsol(ExpClass var0, Hashtable var1, Hashtable<String, Hashtable<Integer, String>> var2, ExpClass var3) {
      Hashtable var4 = new Hashtable(var1);
      initActPages(var4);
      int var5 = 0;

      try {
         var5 = 0;

         while(variatePageIndexes(var1, var2, var4)) {
            try {
               if (exeuteByDinPage(var4, var3)) {
                  ++var5;
               }
            } catch (Exception var7) {
               writeError(var0, ERR_FN_ID, "Számítási hiba! ", var7, var3);
               return;
            }
         }
      } catch (Exception var8) {
         writeError(var0, ERR_FN_ID, "Számítási hiba! ", var8, var3);
      }

      FIdHelper.resetDinPageNumber(var1);
      var0.setType(2);
      var0.setResult(new Integer(var5));
   }

   public static synchronized void fnAktDinErtek(ExpClass var0) {
      byte var1 = 0;
      Object var2 = null;
      var0.setType(var1);
      var0.setResult(var2);

      try {
         if (var0.getParametersCount() == 1) {
            String var3 = (String)getParameter(var0, 0, true, 1, false);
            int var4 = FIdHelper.getPageNumber(getSelf());
            ExpClass var5 = getDFieldValue(var0, var3, var4);
            var0.setType(var5.getType());
            var0.setResult(var5.getValue());
         }
      } catch (Exception var6) {
         writeError(var0, ERR_FN_ID, "Számítási hiba! " + var6.getMessage(), var6, var0);
      }

   }

   private static boolean storeDeepNumber(Hashtable var0) {
      boolean var1 = false;
      Enumeration var2 = var0.keys();

      String var4;
      int var6;
      for(FormModel var3 = gui_info.get(); var2.hasMoreElements(); var0.put(var4, new Integer(var6))) {
         var4 = (String)var2.nextElement();
         int var5 = var3.getPageindex(var4);
         var6 = gui_info.get_pagecounts()[var5] - 1;
         if (var6 > 0) {
            var1 = true;
         }

         if (var6 == -1) {
            var0.remove(var4);
         }
      }

      return var1;
   }

   private static void initActPages(Hashtable var0) {
      if (var0 != null && var0.size() >= 1) {
         Enumeration var1 = var0.keys();

         while(var1.hasMoreElements()) {
            Object var2 = var1.nextElement();
            var0.put(var2, new Integer(0));
         }

         var1 = var0.keys();
         var0.put(var1.nextElement(), new Integer(-1));
      }
   }

   private static boolean variatePageIndexes(Hashtable var0, Hashtable<String, Hashtable<Integer, String>> var1, Hashtable var2) {
      if (var2 != null && var2.size() >= 1) {
         Enumeration var3 = var2.keys();

         while(var3.hasMoreElements()) {
            String var4 = (String)var3.nextElement();
            int var5 = (Integer)var2.get(var4);
            int var6 = (Integer)var0.get(var4);
            Hashtable var7 = (Hashtable)var1.get(var4);
            if (var7 != null) {
               while(var7.containsKey(new Integer(var5 + 1))) {
                  ++var5;
               }
            }

            if (var5 != var6) {
               var2.put(var4, new Integer(var5 + 1));
               return true;
            }

            var2.put(var4, new Integer(0));
         }

         return false;
      } else {
         return false;
      }
   }

   private static boolean exeuteByDinPage(Hashtable var0, ExpClass var1) throws Exception {
      FIdHelper.setDinPageNumber(var0);
      Object var2 = ABEVFunctionSet.expwrapper.get("", var1);
      Object var3 = var1.getValue();
      int var4 = var1.getType();
      if (var4 == 4 && var3 != null && var3 instanceof Boolean) {
         return (Boolean)var3;
      } else {
         throw new Exception("Program hiba, típus eltérés!");
      }
   }

   private static Hashtable getCachedDinParams(ExpClass var0) {
      Hashtable var1 = (Hashtable)g_din_expressions.get(g_active_form_id);
      if (var1 == null) {
         var1 = new Hashtable();
         g_din_expressions.put(g_active_form_id, var1);
      }

      String var2 = ABEVFunctionSet.getExpString(var0);
      Hashtable var3 = (Hashtable)var1.get(var2);
      if (var3 == null) {
         var3 = new Hashtable();
         expRequest[1] = var2;
         ExpClass var4 = (ExpClass)((IPropertyList)ic).get(expRequest);
         Hashtable var5 = new Hashtable();
         getDinPagesByField(var4, var5);
         var3.put("exp", var4);
         var3.put("pages", var5);
         var1.put(var2, var3);
      }

      return var3;
   }

   private static void getDinPagesByField(ExpClass var0, Hashtable var1) {
      Hashtable var2 = new Hashtable();
      var2.put("field", "");
      var2.put("akt_din_érték", "");
      var2.put("adózói_érték", "");
      int var5 = var0.getExpType();
      switch(var5) {
      case 0:
         return;
      case 1:
         return;
      case 2:
         return;
      default:
         String var3 = var0.getIdentifier();
         if (var2.containsKey(var3)) {
            ExpClass var7 = var0.getParameter(0);
            String var8 = (String)var7.getValue();

            try {
               if (FIdHelper.isFieldDynamic(var8)) {
                  var1.put(FIdHelper.getPageId(var8), "");
               }
            } catch (Exception var10) {
               return;
            }
         }

         int var4 = var0.getParametersCount();

         for(int var11 = 0; var11 < var4; ++var11) {
            ExpClass var6 = var0.getParameter(var11);
            getDinPagesByField(var6, var1);
         }

      }
   }

   public static synchronized void fnFillOnPrevious(ExpClass var0) {
      fnFillOn(var0, false);
   }

   public static synchronized void fnFillOnFollowing(ExpClass var0) {
      fnFillOn(var0, true);
   }

   public static synchronized void fnFillOn(ExpClass var0, boolean var1) {
      int var3 = 0;
      var0.setType(0);
      var0.setResult((Object)null);

      try {
         Object[] var4 = getSelf();
         String var5 = FIdHelper.getFieldId(var4);
         if (var1) {
            String var6 = getPageActPageName(var0);
            PageModel var7 = getPageByName(var0, var6);
            Integer var8 = getPageDeepCount(var0, var6, var7);
            if (var8 != null) {
               var3 = var8 - 1;
            }
         }

         ExpClass var2 = getDFieldValue(var0, var5, var3);
         if (var2 != null) {
            var0.setType(var2.getType());
            var0.setResult(var2.getValue());
         }
      } catch (Exception var9) {
         writeError(var0, ERR_FN_ID, "Számítási hiba! " + var9.getMessage(), var9, var0);
      }

   }

   public static synchronized void fnJoVPid(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      boolean var1 = false;
      if (var0.getParametersCount() == 1) {
         ExpClass var2 = var0.getParameter(0);
         int var3 = var2.getType();
         if (var3 == 0) {
            return;
         }

         if (var3 != 1 && var3 != 2) {
            writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
            return;
         }

         var1 = ValidationUtilityVPOP.isVPid(var2.getValue().toString());
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

      var0.setType(4);
      var0.setResult(var1);
   }

   public static synchronized void fnJoGln(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      boolean var1 = false;
      if (var0.getParametersCount() == 1) {
         ExpClass var2 = var0.getParameter(0);
         int var3 = var2.getType();
         if (var3 == 0) {
            return;
         }

         if (var3 != 1 && var3 != 2) {
            writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
            return;
         }

         var1 = ValidationUtilityVPOP.isGln(var2.getValue().toString());
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

      var0.setType(4);
      var0.setResult(var1);
   }

   public static synchronized void fnJoRegSzam(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      boolean var1 = false;
      if (var0.getParametersCount() == 1) {
         ExpClass var2 = var0.getParameter(0);
         int var3 = var2.getType();
         if (var3 == 0) {
            return;
         }

         if (var3 != 1 && var3 != 2) {
            writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
            return;
         }

         var1 = ValidationUtilityVPOP.isRegSzam(var2.getValue().toString());
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

      var0.setType(4);
      var0.setResult(var1);
   }

   public static synchronized void fnTiltEsTorol(ExpClass var0) {
      var0.setType(4);
      var0.setResult(Boolean.FALSE);
      if (var0.getParametersCount() != 2) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            boolean var1 = false;
            Object var2 = getParameter(var0, 0, true, 4, var1);
            if (var2 == null) {
               return;
            }

            boolean var3 = NumericOperations.getBoolean(var2);
            String var4 = (String)getParameter(var0, 1, true, 1, var1);
            if (var4 == null) {
               return;
            }

            String var5 = getFidByVid(var4, getFormid());
            if (var5 == null) {
               writeError(var0, ID_EX_FN_FIELD_ID, "Program hiba, hiba történt a mező azonosító meghatározásakor!", (Exception)null, (Object)null);
               return;
            }

            if (feltetelesErtekIrhat(var5)) {
               if (var3) {
                  gui_info.setFieldReadOnly(var5, true);
                  setFieldValue(var0, var5, "");
               } else {
                  gui_info.setFieldReadOnly(var5, false);
               }
            }

            var0.setResult(Boolean.TRUE);
         } catch (Exception var6) {
            writeError(var0, ERR_FN_ID, "Számítási hiba! " + var6.getMessage(), var6, var0);
         }

      }
   }

   public static synchronized void fnDummyVersion(ExpClass var0) {
      var0.setType(4);
      var0.setResult(Boolean.TRUE);
   }

   public static synchronized void fnCsatolmanyDb(ExpClass var0) {
      var0.setType(2);
      var0.setResult(AttachementTool.countAttachement(gui_info));
      g_all_fileds_empty = false;
   }

   public static synchronized void fnUzemmod(ExpClass var0) {
      var0.setType(1);
      var0.setResult("0");

      try {
         var0.setResult(getByActiveRole(gui_info.getRole()));
      } catch (Exception var2) {
         writeError(var0, ID_EX_PAR_UZEMMOD, "Program hiba az üzemmód meghatározásánál!", var2, (Object)null);
      }

   }

   private static String getByActiveRole(String var0) {
      if (var0.equals("0")) {
         return "A";
      } else if (var0.equals("1")) {
         return "J";
      } else if (var0.equals("2")) {
         return "R";
      } else {
         return var0.equals("3") ? "U" : null;
      }
   }

   public static synchronized void fnAnykParam(ExpClass var0) {
      var0.setResult((Object)null);
      var0.setType(0);
      if (var0.getParametersCount() != 1) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            String var1 = (String)getParameter(var0, 0, true, 1, true);
            if ("számított".equalsIgnoreCase(var1)) {
               var0.setResult(getDirty2Flag());
               var0.setType(1);
            } else if ("kötegelt".equalsIgnoreCase(var1)) {
               var0.setResult(!gui_info.isSingle() ? "I" : "N");
               var0.setType(1);
            } else if ("főnyomtatvány_azonosító".equalsIgnoreCase(var1)) {
               var0.setResult(gui_info.main_document_id);
               var0.setType(1);
            } else if ("használati_mód".equalsIgnoreCase(var1)) {
               var0.setType(1);
               if (isInGeneratorMod()) {
                  var0.setResult("bevallás_generálás");
               } else if (isInWebKitoltoMod()) {
                  var0.setResult("webes_kitöltés");
               } else if (isInJavkeretOpMode()) {
                  var0.setResult("javítókeret");
               } else if (isInBatchEllenorzoOpMode()) {
                  var0.setResult("batch_ellenőrző");
               } else {
                  var0.setResult("");
               }
            } else if ("szerkesztés".equalsIgnoreCase(var1)) {
               var0.setResult(isInLeker() ? SZERKESZTES_NEM : SZERKESZTES_IGEN);
               var0.setType(1);
            }
         } catch (Exception var2) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", var2, (Object)null);
         }

      }
   }

   private static boolean isInLeker() {
      try {
         if ("10".equals(MainFrame.hasznalati_mod)) {
            return false;
         } else if (MainFrame.readonlymodefromubev) {
            return true;
         } else if ("2".equals(MainFrame.opmode)) {
            return !gui_info.getBatchRecalcMode() || !gui_info.get_main_formmodel().id.equalsIgnoreCase(gui_info.get_formid());
         } else if (isXczEditMode()) {
            return !getDesktopEditStateForBetoltErtek();
         } else if (gui_info.isXkr_mode()) {
            if (getDesktopEditStateForBetoltErtek()) {
               return false;
            } else {
               return isXkrViewMode();
            }
         } else {
            MainPanel var0 = MainFrame.thisinstance.mp;
            return var0.isReadonlyMode() || var0.isFuncReadonlyMode() || var0.isReadonlyState() || !getDesktopEditStateForBetoltErtek();
         }
      } catch (Exception var1) {
         return false;
      }
   }

   private static boolean isXkrViewMode() {
      if (gui_info.isXkr_mode()) {
         if (singleCommandImport()) {
            return !"importxkr".equalsIgnoreCase(gui_info.getMuvelet());
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   private static boolean singleCommandImport() {
      return getPropertylistValue("prop.dynamic.importOne");
   }

   private static boolean isXczEditMode() {
      return gui_info.isXkr_mode() && gui_info.isXczModeModifier();
   }

   private static boolean getDesktopEditStateForBetoltErtek() {
      return getPropertylistValue("desktop_edit_state_for_betolt_ertek");
   }

   private static boolean getPropertylistValue(String var0) {
      try {
         Object var1 = PropertyList.getInstance().get(var0);
         return var1 != null ? (Boolean)var1 : false;
      } catch (Exception var2) {
         return false;
      }
   }

   private static String getDirty2Flag() {
      Object var0 = PropertyList.getInstance().get("prop.dynamic.dirty2_original");
      if (var0 == null) {
         var0 = PropertyList.getInstance().get("prop.dynamic.dirty2");
         if (var0 == null) {
            var0 = Boolean.FALSE;
         }
      }

      return var0 instanceof Boolean && (Boolean)var0 ? "N" : "I";
   }

   public static synchronized void fnEredeti(ExpClass var0) {
      var0.setResult((Object)null);
      var0.setType(0);
      var0.setDontModify(true);
   }

   private static String getRealPageName(String var0) {
      try {
         Hashtable var1 = (Hashtable)g_real_page_names.get(getFormid());
         if (var1 == null) {
            createDesignedPageTables(getFormid());
            var1 = (Hashtable)g_real_page_names.get(getFormid());
         }

         if (var1.containsKey(var0)) {
            return var0;
         } else {
            Hashtable var2 = (Hashtable)g_designed_page_names.get(getFormid());
            String var3 = (String)var2.get(replaceName(var0));
            return var3 == null ? "" : var3;
         }
      } catch (Exception var4) {
         return "";
      }
   }

   private static void createDesignedPageTables(String var0) {
      FormModel var1 = gui_info.get(var0);
      if (var1 != null) {
         Hashtable var2 = new Hashtable();
         Hashtable var3 = new Hashtable();
         g_real_page_names.put(var0, var2);
         g_designed_page_names.put(var0, var3);
         Enumeration var4 = var1.names_page.keys();

         while(var4.hasMoreElements()) {
            String var5 = (String)var4.nextElement();
            var2.put(var5, var5);
            var3.put(replaceName(var5), var5);
         }
      }

   }

   public static String replaceName(String var0) {
      char[] var1 = var0.toLowerCase().toCharArray();
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         char var4 = var1[var3];
         if ("\\_\\@\\[\\]\\+\\-\\*\\/\\(\\)\\ ".lastIndexOf(var4) <= -1) {
            var2.append(var4);
         }
      }

      return var2.toString();
   }

   private static String getRealVidCode(String var0) {
      try {
         Hashtable var1 = (Hashtable)g_real_vid_names.get(g_active_form_id);
         if (var1 == null) {
            createDesignedVidTable(g_active_form_id);
            var1 = (Hashtable)g_real_vid_names.get(g_active_form_id);
         }

         if (var1.containsKey(var0)) {
            return var0;
         } else {
            Hashtable var2 = (Hashtable)g_real_vid_names.get(g_active_form_id);
            String var3 = (String)var2.get(replaceName(var0));
            return var3 == null ? "" : var3;
         }
      } catch (Exception var4) {
         return "";
      }
   }

   private static void createDesignedVidTable(String var0) {
      FormModel var1 = gui_info.get(var0);
      if (var1 != null) {
         Hashtable var2 = new Hashtable();
         Hashtable var3 = new Hashtable();
         g_real_vid_names.put(var0, var2);
         g_real_vid_names.put(var0, var3);
         Hashtable var4 = meta_info.getFieldAttributes(var0, "vid", true);
         Enumeration var5 = var4.elements();

         while(var5.hasMoreElements()) {
            String var6 = (String)var5.nextElement();
            var2.put(var6, var6);
            var3.put(replaceName(var6), var6);
         }
      }

   }

   private static String extendedInfo(String var0, Integer var1) {
      return MetaInfo.extendedInfoTxt(var0, var1, g_active_form_id, gui_info);
   }

   public static boolean isToleranced(String var0, String var1, String var2, String var3) {
      try {
         BigDecimal var4 = getMetaTolerance(var1);
         if (var4 != null) {
            BigDecimal var5 = new BigDecimal(var2 != null && var2.length() != 0 ? var2 : "0");
            BigDecimal var6 = new BigDecimal(var3 != null && var3.length() != 0 ? var3 : "0");
            return var5.negate().add(var6).abs().compareTo(var4) < 1;
         }
      } catch (Exception var7) {
         System.out.println("FunctionBodies.isToleranced error:" + var7.getMessage() + " (f1:" + var1 + ", p1:" + var2 + ", p2:" + var3 + ")");
      }

      return false;
   }

   private static BigDecimal getMetaTolerance(String var0) {
      try {
         if (getFieldType(var0) == 2) {
            Object var1 = getFieldMetaAttr(var0, "tolerance");
            if (var1 != null && var1.toString().length() > 0 && !var1.toString().equalsIgnoreCase("0")) {
               return new BigDecimal(var1.toString());
            }
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      return null;
   }

   public static synchronized void fnIntervallumAtfedes(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      byte var1 = 3;

      try {
         String var2 = (String)getParameter(var0, 0, true, 1, true);
         String var3 = (String)getParameter(var0, 1, true, 1, true);
         boolean var4 = (Boolean)getParameter(var0, 2, true, 4, true);
         if (var2 != null && var3 != null) {
            int var5 = var0.getParametersCount();
            if (var5 >= var1 && (var5 - var1) % 3 == 0) {
               String var6 = getPageNameByUniParams(var0, var2);
               checkSamePage(var0, var6, var2);
               checkSamePage(var0, var6, var3);
               int var7 = -1;
               Vector var8 = getFieldFidsByUniParams(var0, var2);
               Vector var9 = new Vector();

               for(int var10 = var1; var10 < var5; var10 += 3) {
                  String var11 = (String)getParameter(var0, var10, true, 1, true);
                  checkSamePage(var0, var6, var11);
                  int var12 = getRelation((String)getParameter(var0, var10 + 1, true, 1, true));
                  if (var12 < 0) {
                     throw new Exception("Hibás reláció");
                  }

                  Vector var13 = getFieldFidsByUniParams(var0, var11);
                  var7 = checkRowCount(var7, var13);
                  if (var13.size() > 1) {
                     checkRowCount(var7, var8);
                  }

                  Object var14 = getSzuroErtek(getParameter(var0, var10 + 2, false, 0, false), getFieldType((String)var13.get(0)));
                  var9.add(new FunctionBodies.Filter(var13, var12, var14));
               }

               checkCsoportosIntervallumAtfedes(var0, var2, var3, var4, (String)null, var9);
            } else {
               writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
            }
         } else {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
         }
      } catch (Exception var15) {
         writeError(var0, ID_EX_FN_INTERVALLUM_SZURES, "Hiba az intervallum szűrés során", (Exception)null, var15.getMessage());
      }
   }

   public static synchronized void fnIntervallumAtfedesCsoport(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      byte var1 = 4;

      try {
         String var2 = (String)getParameter(var0, 0, true, 1, true);
         String var3 = (String)getParameter(var0, 1, true, 1, true);
         boolean var4 = (Boolean)getParameter(var0, 2, true, 4, true);
         String var5 = (String)getParameter(var0, 3, true, 1, true);
         if (var2 != null && var3 != null && var5 != null) {
            int var6 = var0.getParametersCount();
            if (var6 >= var1 && (var6 - var1) % 3 == 0) {
               String var7 = getPageNameByUniParams(var0, var2);
               checkSamePage(var0, var7, var3);
               if (var5 != null && var5.length() != 0) {
                  checkSamePage(var0, var7, var5);
               }

               int var8 = -1;
               Vector var9 = getFieldFidsByUniParams(var0, var2);
               Vector var10 = new Vector();

               for(int var11 = var1; var11 < var6; var11 += 3) {
                  String var12 = (String)getParameter(var0, var11, true, 1, true);
                  checkSamePage(var0, var7, var12);
                  int var13 = getRelation((String)getParameter(var0, var11 + 1, true, 1, true));
                  if (var13 < 0) {
                     throw new Exception("Hibás reláció");
                  }

                  Vector var14 = getFieldFidsByUniParams(var0, var12);
                  var8 = checkRowCount(var8, var14);
                  if (var14.size() > 1) {
                     checkRowCount(var8, var9);
                  }

                  Object var15 = getSzuroErtek(getParameter(var0, var11 + 2, false, 0, false), getFieldType((String)var14.get(0)));
                  var10.add(new FunctionBodies.Filter(var14, var13, var15));
               }

               checkCsoportosIntervallumAtfedes(var0, var2, var3, var4, var5, var10);
            } else {
               writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
            }
         } else {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
         }
      } catch (Exception var16) {
         writeError(var0, ID_EX_FN_INTERVALLUM_SZURES, "Hiba az intervallum szűrés során", (Exception)null, var16.getMessage());
      }
   }

   private static Object getSzuroErtek(Object var0, int var1) throws Exception {
      return ExpFactoryLight.createConstant(var0 == null ? null : var0.toString(), var1).getValue();
   }

   public static synchronized void checkCsoportosIntervallumAtfedes(ExpClass var0, String var1, String var2, boolean var3, String var4, Vector<FunctionBodies.Filter> var5) {
      byte var7 = -1;
      HashMap var13 = new HashMap();
      Vector var14 = new Vector();
      var0.setType(0);
      var0.setResult((Object)null);

      String var6;
      int var8;
      Integer var9;
      Vector var10;
      Vector var11;
      Vector var12;
      try {
         var6 = getPageNameByUniParams(var0, var1);
         var10 = getFieldFidsByUniParams(var0, var1);
         int var23 = checkRowCount(var7, var10);
         var11 = getFieldFidsByUniParams(var0, var2);
         checkRowCount(var23, var11);
         if (var4 != null && var4.length() != 0) {
            var12 = getFieldFidsByUniParams(var0, var4);
            if (var12.size() > 1) {
               checkRowCount(var23, var12);
            }
         } else {
            var12 = null;
         }

         var8 = getPageCounter(var0, var6);
         var9 = isLaphozKotott() ? FIdHelper.getDPageNumber(getSelf()) : 0;
      } catch (Exception var22) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, var22.getMessage());
         return;
      }

      int var15 = var3 ? var10.size() : var8 * var10.size();

      try {
         int var16 = 0;
         if (isLaphozKotott() && var3) {
            var16 = var9;
            var8 = var9 + 1;
         }

         for(int var17 = var16; var17 < var8; ++var17) {
            for(int var18 = 0; var18 < var10.size(); ++var18) {
               boolean var19 = true;

               for(int var20 = 0; var20 < var5.size() && var19; ++var20) {
                  var19 = ((FunctionBodies.Filter)var5.elementAt(var20)).check(var0, var18, var17);
               }

               if (var19) {
                  catalogIntervals(var10, var11, var12, var13, var18, var17, var15);
               }
            }

            if (var3) {
               atfedesEllenorzes(var13, var14);
               var13 = new HashMap();
            }
         }

         if (!var3) {
            atfedesEllenorzes(var13, var14);
         }
      } catch (Exception var21) {
         writeError(var0, ID_EX_FN_INTERVALLUM_SZURES, "Hiba az intervallum szűrés során", (Exception)null, var21.getMessage());
         return;
      }

      if (isFieldCheck() || isFormCheck()) {
         listErrors(var0, var14, var6);
      }

      var0.setType(4);
      var0.setResult(var14.size() == 0 ? Boolean.TRUE : Boolean.FALSE);
   }

   private static void catalogIntervals(Vector var0, Vector var1, Vector var2, Map<String, Vector<IIntervalHandler>> var3, int var4, int var5, int var6) {
      Object var7 = getExpValue(getFValue((String)var0.elementAt(var4), var5));
      Object var8 = getExpValue(getFValue((String)var1.elementAt(var4), var5));
      if (var7 != null || var8 != null) {
         if (var7 == null) {
            var7 = var8;
         }

         if (var8 == null) {
            var8 = var7;
         }

         String var9 = createGroupId(var2, var4, var5);
         DefaultIntervalHandler var10 = new DefaultIntervalHandler(new NumIntervalImpl(var7.toString(), var8.toString()), (String)var0.elementAt(var4), var5);
         addToGroups(var3, var9, var10, var6);
      }
   }

   private static void atfedesEllenorzes(Map<String, Vector<IIntervalHandler>> var0, Vector<IIntervalHandler> var1) {
      Iterator var2 = var0.values().iterator();

      while(var2.hasNext()) {
         Vector var3 = (Vector)var2.next();

         for(int var4 = 0; var4 < var3.size(); ++var4) {
            IIntervalHandler var5 = (IIntervalHandler)var3.elementAt(var4);

            for(int var6 = var4 + 1; var6 < var3.size(); ++var6) {
               IIntervalHandler var7 = (IIntervalHandler)var3.elementAt(var6);
               if (var5.getInterval().compareTo(var7.getInterval()) == 0) {
                  var1.add(var5);
                  var1.add(var7);
               }
            }
         }
      }

   }

   private static void listErrors(ExpClass var0, Vector<IIntervalHandler> var1, String var2) {
      for(int var3 = 0; var3 < var1.size(); var3 += 2) {
         setIntervalErrorMessage(var0, var2, (IIntervalHandler)var1.elementAt(var3), "Átfedés van a következő üzenetben hivatkozott időszakkal.");
         setIntervalErrorMessage(var0, var2, (IIntervalHandler)var1.elementAt(var3 + 1), "Átfedés van az előző üzenetben hivatkozott időszakkal.");
      }

   }

   private static void setIntervalErrorMessage(ExpClass var0, String var1, IIntervalHandler var2, String var3) {
      String var4 = var3 + " Lap:" + var1.toString() + " Lapszám:" + (var2.getPageNumber() + 1) + " Sorszám:" + getRowNumByFid(var2.getFid(), getFormid()) + " Intervallum: " + var2.getInterval().getOriginalStartValue().toString() + "-" + var2.getInterval().getOriginalEndValue().toString();
      writeWarning(var0, ID_FILLIN_ERROR, var4, (Exception)null, createGotoButtonByField(var2.getFid().toString(), var2.getPageNumber() + 0));
   }

   private static String createGroupId(Vector var0, int var1, int var2) {
      String var3 = "no_group_id";
      if (var0 != null) {
         Object var4 = getExpValue(getFValue((String)var0.elementAt(var0.size() == 1 ? 0 : var1), var2));
         var3 = var4 == null ? "null_group_id" : var4.toString();
      }

      return var3;
   }

   private static void addToGroups(Map<String, Vector<IIntervalHandler>> var0, String var1, IIntervalHandler var2, int var3) {
      Vector var4 = (Vector)var0.get(var1);
      if (var4 == null) {
         var4 = new Vector(var3);
         var0.put(var1, var4);
      }

      var4.add(var2);
   }

   private static void checkSamePage(ExpClass var0, String var1, String var2) throws Exception {
      if (var1 != null) {
         String var3 = getRealPageName(var1);
         String var4;
         if (isExistingField(var2)) {
            var4 = getRealPageName(getPageNameByField(var0, var2));
         } else {
            Object[] var5 = convABEVtableIDs(var2);
            if (var5 == null || var5[0] == null) {
               throw new Exception("Az oszlopok nem mind ugyanazon a lapon találhatók(Lap: " + var1 + " Paraméterek: " + var2 + ")");
            }

            var4 = getRealPageName((String)var5[0]);
         }

         if (!var3.equalsIgnoreCase(var4)) {
            throw new Exception("Az oszlopok nem mind ugyanazon a lapon találhatók(Lap: " + var1 + " Paraméterek: " + var2 + ")");
         }
      }
   }

   private static Vector<String> getFieldFidsByUniParams(ExpClass var0, String var1) {
      Vector var2;
      if (isExistingField(var1)) {
         var2 = new Vector();
         var2.add(var1);
      } else {
         var2 = getCachedFieldFids(var0, var1);
      }

      return var2;
   }

   private static String getPageNameByUniParams(ExpClass var0, String var1) throws Exception {
      if (isExistingField(var1)) {
         return getPageNameByField(var0, var1);
      } else {
         Object[] var2 = convABEVtableIDs(var1);
         return (String)var2[0];
      }
   }

   private static boolean isLaphozKotott() {
      return g_current_field_id != null;
   }

   private static Object getExpValue(ExpClass var0) {
      return var0 == null ? null : var0.getValue();
   }

   private static int checkRowCount(int var0, Vector var1) throws Exception {
      int var2 = var1.size();
      if (var0 == -1) {
         return var2;
      } else if (var0 != var2) {
         throw new Exception("Az oszlopok számossága eltér (" + (var1.size() == 0 ? "" : var1.elementAt(0)) + ")");
      } else {
         return var2;
      }
   }

   private static int getPageCounter(ExpClass var0, String var1) throws Exception {
      PageModel var2 = getPageByName(var0, var1);
      if (var2 == null) {
         writeError(var0, ID_EX_FN_PAGE_DYN_COUNT, "Program hiba, dinamikus lapszám meghatározásánál!", (Exception)null, var1);
         throw new Exception("Program hiba, dinamikus lapszám meghatározásánál!");
      } else {
         return getPageDeepCount(var0, var1, var2);
      }
   }

   private static int getRelation(String var0) {
      byte var1 = -1;
      if (var0.equalsIgnoreCase("=")) {
         var1 = 9;
      } else if (var0.equalsIgnoreCase("<")) {
         var1 = 7;
      } else if (var0.equalsIgnoreCase(">")) {
         var1 = 8;
      } else if (var0.equalsIgnoreCase("<=")) {
         var1 = 10;
      } else if (var0.equalsIgnoreCase(">=")) {
         var1 = 11;
      } else if (var0.equalsIgnoreCase("<>")) {
         var1 = 12;
      }

      return var1;
   }

   public static synchronized void fnTablazatUnique(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      byte var1 = 2;
      String var2 = null;
      int var3 = -1;
      boolean var8 = g_current_field_id != null;
      int var10 = var0.getParametersCount();
      if (var10 < 3) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         Boolean var4;
         Integer var5;
         Vector var6;
         int var7;
         Integer var9;
         try {
            var4 = (Boolean)getParameter(var0, 0, true, 4, true);
            var5 = NumericOperations.getInteger(getParameter(var0, 1, true, 2, true));
            if (var4 == null || var5 == null) {
               writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
               return;
            }

            var6 = new Vector(var10 - 2);

            for(int var11 = var1; var11 < var10; ++var11) {
               String var12 = (String)getParameter(var0, var11, true, 1, true);
               Object[] var13 = convABEVtableIDs(var12);
               String var14 = (String)var13[0];
               if (var2 == null) {
                  var2 = var14;
               }

               checkSamePage(var0, var2, var12);
               Vector var15 = getCachedFieldFids(var0, var12);
               var3 = checkRowCount(var3, var15);
               var6.add(var15);
            }

            var7 = getPageCounter(var0, var2);
            var9 = var8 ? FIdHelper.getDPageNumber(getSelf()) : 0;
         } catch (Exception var16) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, var16.getMessage());
            return;
         }

         tablazatUnique(var0, var4, var5, var6, var2, var3, var7, var9, var8);
      }
   }

   public static synchronized void tablazatUnique(ExpClass var0, Boolean var1, Integer var2, Vector<Vector> var3, String var4, int var5, int var6, Integer var7, boolean var8) {
      Vector var10 = new Vector();
      var0.setType(0);
      var0.setResult((Object)null);
      Vector var9 = new Vector(var8 ? var5 : var6 * var5);

      int var11;

      class TableRow implements Comparable {
         private int pageNumber;
         private Vector<String> fids;
         private Vector<String> values;
         private String diffValue;

         public TableRow(int var1) {
            this.pageNumber = var1;
            this.fids = new Vector();
            this.values = new Vector();
         }

         public int getPageNumber() {
            return this.pageNumber;
         }

         public void add(String var1, String var2) {
            this.fids.add(var1);
            this.values.add(var2);
         }

         public Vector<String> getFids() {
            return this.fids;
         }

         public String generateValue() {
            StringBuilder var1 = new StringBuilder();
            StringBuilder var2 = new StringBuilder();

            for(int var3 = 0; var3 < this.values.size(); ++var3) {
               String var4 = (String)this.values.elementAt(var3);
               var1.append(var4);
               var2.append(var4);
               var2.append("##");
            }

            return var1.length() == 0 ? null : var2.toString();
         }

         public void setDiffValue(String var1) {
            this.diffValue = var1;
         }

         public String getDiffValue() {
            return this.diffValue;
         }

         public int compareTo(Object var1) {
            return this.diffValue.compareTo(((TableRow)var1).getDiffValue());
         }
      }

      try {
         var11 = 0;
         if (var8) {
            var11 = var7;
            var6 = var7 + 1;
         }

         int var12;
         int var13;
         TableRow var14;
         for(var12 = var11; var12 < var6; ++var12) {
            for(var13 = 0; var13 < var5; ++var13) {
               var14 = new TableRow(var12);
               Iterator var15 = var3.iterator();

               while(var15.hasNext()) {
                  Vector var16 = (Vector)var15.next();
                  String var17 = ABEVFeaturedBaseFunctions.getString(getExpValue(getFValue((String)var16.elementAt(var13), var12)), (String)null);
                  var14.add((String)var16.elementAt(var13), var17);
               }

               String var21 = var14.generateValue();
               if (var21 != null) {
                  var14.setDiffValue(var21);
                  var9.add(var14);
               }
            }
         }

         if (var9.size() > 1) {
            TableRow var22;
            if (var1) {
               Collections.sort(var9);
               var12 = 1;

               for(var13 = 1; var13 < var9.size(); ++var13) {
                  var14 = (TableRow)var9.elementAt(var13);
                  var22 = (TableRow)var9.elementAt(var13 - 1);
                  if (var22.getDiffValue().equals(var14.getDiffValue())) {
                     ++var12;
                  } else {
                     if (var12 > var2) {
                        for(int var24 = var13 - var12; var24 < var13; ++var24) {
                           var10.add(var9.elementAt(var24));
                        }
                     }

                     var12 = 1;
                  }
               }

               if (var12 > var2) {
                  for(var13 = var9.size() - var12; var13 < var9.size(); ++var13) {
                     var10.add(var9.elementAt(var13));
                  }
               }
            } else {
               Hashtable var19 = new Hashtable(var9.size());
               var19.put(((TableRow)var9.elementAt(0)).getDiffValue(), var9.elementAt(0));

               for(var13 = 1; var13 < var9.size(); ++var13) {
                  var14 = (TableRow)var9.elementAt(var13);
                  var22 = (TableRow)var9.elementAt(var13 - 1);
                  if (!var22.getDiffValue().equals(var14.getDiffValue())) {
                     var19.put(var14.getDiffValue(), var14);
                  }
               }

               if (var19.size() > var2) {
                  var10 = new Vector(var19.values());
               }
            }
         }
      } catch (Exception var18) {
         writeError(var0, ID_EX_FN_TABLAZAT_UNIQUE, "Táblázat sorainak a vizsgálata", (Exception)null, var18.getMessage());
         return;
      }

      if (isFieldCheck() || isFormCheck()) {
         for(var11 = 0; var11 < var10.size(); ++var11) {
            TableRow var20 = (TableRow)var10.elementAt(var11);
            String var23 = (var1 ? "Ismétlődő sorok:" : "Különböző tartalmú sorok:") + " Lap:" + var4 + " Lapszám:" + (var20.getPageNumber() + 1) + " Sorszám:" + getRowNumByFid((String)var20.getFids().elementAt(0), getFormid()) + " Érték:" + var20.getDiffValue().replaceAll("##", "/");
            writeWarning(var0, ID_FILLIN_ERROR, var23, (Exception)null, createGotoButtonByField((String)var20.getFids().elementAt(0), var20.getPageNumber()));
         }
      }

      var0.setType(4);
      var0.setResult(var10.size() == 0 ? Boolean.TRUE : Boolean.FALSE);
   }

   private static boolean isFieldCheck() {
      return isCurrentEvent("field_check");
   }

   private static boolean isFormCheck() {
      return isCurrentEvent("form_check");
   }

   private static boolean isCurrentEvent(String var0) {
      if (g_calc_record == null) {
         return false;
      } else {
         String var1 = CalcHelper.getEvent(g_calc_record);
         return var1 == null ? false : var1.toString().equals(var0);
      }
   }

   private static String getCurrentCalcErrorCode() {
      return g_calc_record == null ? null : CalcHelper.getErrorCode(g_calc_record);
   }

   public static synchronized void fnJoEmail(ExpClass var0) {
      var0.setResult((Object)null);
      var0.setType(0);
      if (var0.getParametersCount() != 1) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      } else {
         try {
            String var1 = (String)getParameter(var0, 0, true, 1, true);
            var0.setResult(ValidationUtilityAPEH.isValidEmail(var1));
            var0.setType(4);
         } catch (Exception var2) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", var2, (Object)null);
         }

      }
   }

   public static synchronized void fnJoBIC(ExpClass var0) {
      var0.setResult((Object)null);
      var0.setType(0);
      if (var0.getParametersCount() >= 1 && var0.getParametersCount() <= 2) {
         try {
            String var1 = (String)getParameter(var0, 0, true, 1, true);
            boolean var2 = ValidationUtilityAPEH.isValidSWIFT(var1);
            if (var2 && var0.getParametersCount() == 2) {
               String var3 = (String)getParameter(var0, 1, true, 1, true);
               var2 = checkEUMemberShip(var3, var1.substring(4, 6), var0);
            }

            var0.setResult(var2);
            var0.setType(4);
         } catch (Exception var4) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", var4, (Object)null);
         }

      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }
   }

   public static synchronized void fnJoIBAN(ExpClass var0) {
      var0.setResult((Object)null);
      var0.setType(0);
      if (var0.getParametersCount() >= 1 && var0.getParametersCount() <= 2) {
         try {
            String var1 = (String)getParameter(var0, 0, true, 1, true);
            boolean var2 = ValidationUtilityAPEH.isValidIBAN(var1);
            if (var2 && var0.getParametersCount() == 2) {
               String var3 = (String)getParameter(var0, 1, true, 1, true);
               var2 = checkEUMemberShip(var3, var1.substring(0, 2), var0);
            }

            var0.setResult(var2);
            var0.setType(4);
         } catch (Exception var4) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", var4, (Object)null);
         }

      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }
   }

   public static synchronized void fnJoImportHatarozat(ExpClass var0) {
      var0.setResult((Object)null);
      var0.setType(0);
      if (var0.getParametersCount() >= 1 && var0.getParametersCount() <= 2) {
         try {
            String var1 = (String)getParameter(var0, 0, true, 1, true);
            boolean var2 = ValidationUtilityAPEH.isValidImportHatarozat(var1);
            if (var2 && var0.getParametersCount() == 2) {
               String var3 = (String)getParameter(var0, 1, true, 1, true);
               var2 = checkEUMemberShip(var3, var1.substring(0, 2), var0);
            }

            var0.setResult(var2);
            var0.setType(4);
         } catch (Exception var4) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", var4, (Object)null);
         }

      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }
   }

   private static boolean checkEUMemberShip(String var0, String var1, ExpClass var2) {
      if (var0 != null && var0.length() != 0) {
         if (ValidationUtilityAPEH.isValidEUMember(var0.toUpperCase()) && var0.equalsIgnoreCase(var1)) {
            return true;
         } else {
            writeError(var2, ID_ERR_EU_MEMBERSHIP, "Nem megfelelő tagállam", (Exception)null, var0 + "/" + var1);
            return false;
         }
      } else {
         return true;
      }
   }

   public static synchronized void fnJoNyilvantartasiSzam(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      boolean var1 = false;
      if (var0.getParametersCount() == 1) {
         ExpClass var2 = var0.getParameter(0);
         int var3 = var2.getType();
         if (var3 == 0) {
            return;
         }

         if (var3 != 1 && var3 != 2) {
            writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
            return;
         }

         var1 = ValidationUtilityAPEH.isValidNyilvantartasiSzam(var2.getValue().toString());
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

      var0.setType(4);
      var0.setResult(var1);
   }

   public static synchronized void fnJoPtgRegisztraciosKod(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      boolean var1 = false;
      if (var0.getParametersCount() == 1) {
         ExpClass var2 = var0.getParameter(0);
         int var3 = var2.getType();
         if (var3 == 0) {
            return;
         }

         if (var3 != 1 && var3 != 2) {
            writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
            return;
         }

         String var4 = var2.getValue().toString();
         if (var4.length() != 16) {
            var1 = false;
         } else {
            var1 = ValidationUtilityAPEH.isLuhn(var4);
         }
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

      var0.setType(4);
      var0.setResult(var1);
   }

   public static synchronized void fnLapUnique(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);

      String var1;
      HashSet var2;
      Set var3;
      try {
         int var5 = var0.getParametersCount();
         if (var5 < 1) {
            throw new Exception("Program hiba, a paraméterek száma vagy típusa nem megfelelő!");
         }

         var1 = (String)getParameter(var0, 0, true, 1, true);
         if (!isPageDynamic(var0, var1)) {
            throw new Exception("Program hiba, nem dinamikus a lap!");
         }

         var3 = getCachedVisibleFieldsByPage(var0, var1);
         var2 = new HashSet();

         for(int var6 = 1; var6 < var5; ++var6) {
            String var7 = (String)getParameter(var0, var6, true, 1, true);
            if (!var3.contains(var7)) {
               throw new Exception("A mezőazonosító nem a laphoz tartozik! (" + var7 + ")");
            }

            var2.add(var7);
         }
      } catch (Exception var9) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, var9.getMessage());
         return;
      }

      Map var4;
      try {
         List var10 = generatePageHashes(var3, var2, getPageCounter(var0, var1));
         Collections.sort(var10);
         var4 = getRedundantPages(var10);
         if (isFieldCheck() || isFormCheck()) {
            showRedundantPages(var4, var1, var0);
         }
      } catch (Exception var8) {
         writeError(var0, ID_EX_FN_PAGE_UNIQUE, "Dinamikus lapok oldalainak egyediség vizsgálata", (Exception)null, var8.getMessage());
         return;
      }

      var0.setType(4);
      var0.setResult(var4.size() == 0 ? Boolean.TRUE : Boolean.FALSE);
   }

   private static String getGoToField(Set<String> var0) {
      Iterator var1 = var0.iterator();
      return (String)var1.next();
   }

   private static List<FunctionBodies.PageValuesModel> generatePageHashes(Set<String> var0, Set<String> var1, int var2) {
      ArrayList var3 = new ArrayList(var2);

      for(int var4 = 0; var4 < var2; ++var4) {
         var3.add(collectPageFieldValues(var0, var1, var4).generateHash());
      }

      return var3;
   }

   private static FunctionBodies.PageValuesModel collectPageFieldValues(Set<String> var0, Set<String> var1, int var2) {
      FunctionBodies.PageValuesModel var3 = new FunctionBodies.PageValuesModel(var2);
      Iterator var4 = var0.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         if (!var1.contains(var5)) {
            Object var6 = getExpValue(getFValue(var5, var2));
            String var7 = var6 == null ? "" : var6.toString();
            if (getFieldType(var5) == 4 && var7.equalsIgnoreCase("false")) {
               var7 = "";
            }

            var3.addFieldValue(var7);
         }
      }

      return var3;
   }

   private static Map<String, List<FunctionBodies.PageValuesModel>> getRedundantPages(List<FunctionBodies.PageValuesModel> var0) {
      HashMap var1 = new HashMap();
      FunctionBodies.PageValuesModel var2 = (new FunctionBodies.PageValuesModel(0)).emptyModel();

      FunctionBodies.PageValuesModel var4;
      for(Iterator var3 = var0.iterator(); var3.hasNext(); var2 = var4) {
         var4 = (FunctionBodies.PageValuesModel)var3.next();
         if (var4.compareTo(var2) == 0) {
            Object var5 = (List)var1.get(var4.getHash());
            if (var5 == null) {
               var5 = new ArrayList();
               ((List)var5).add(var2);
               var1.put(var4.getHash(), var5);
            }

            ((List)var5).add(var4);
         }
      }

      return var1;
   }

   private static void showRedundantPages(Map<String, List<FunctionBodies.PageValuesModel>> var0, String var1, ExpClass var2) {
      Iterator var3 = var0.keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         List var5 = (List)var0.get(var4);
         StringBuilder var6 = new StringBuilder();
         Iterator var7 = var5.iterator();

         FunctionBodies.PageValuesModel var8;
         while(var7.hasNext()) {
            var8 = (FunctionBodies.PageValuesModel)var7.next();
            var6.append(var8.getPageNumber() + 1);
            var6.append(",");
         }

         var7 = var5.iterator();

         while(var7.hasNext()) {
            var8 = (FunctionBodies.PageValuesModel)var7.next();
            String var9 = "Azonos tartalmú oldalak! Lapnév: " + var1 + " Oldal: " + (var8.getPageNumber() + 1) + " Azonos tartalmú oldalak: (" + var6.substring(0, var6.length() - 1) + ")";
            writeWarning(var2, ID_FILLIN_ERROR, var9, (Exception)null, createGotoButtonByPage(var1, var8.getPageNumber()));
         }
      }

   }

   public static Set<String> getCachedVisibleFieldsByPage(ExpClass var0, String var1) {
      String var2 = getRealPageName(var1);
      Object var3 = new HashSet();

      try {
         Map var4 = (Map)g_visible_page_fids.get(g_active_form_id);
         if (var4 == null) {
            var4 = createVisibleFieldsByPage(g_active_form_id);
            g_visible_page_fids.put(g_active_form_id, var4);
         }

         Set var6 = (Set)var4.get(var2);
         var3 = var6 == null ? new HashSet() : var6;
      } catch (Exception var5) {
         writeError(var0, ERR_FN_ID, "Számítási hiba! " + var5.getMessage() + " Paraméter:" + var2, var5, var0);
      }

      return (Set)var3;
   }

   private static Map<String, Set<String>> createVisibleFieldsByPage(String var0) {
      HashMap var1 = new HashMap();
      FormModel var2 = gui_info.get(g_active_form_id);
      Set var3 = var2.get_invisible_fields().keySet();
      Map var4 = createFidsByPage(var0);
      Set var5 = var4.keySet();
      if (var5 != null) {
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();
            HashSet var8 = new HashSet((Collection)var4.get(var7));
            var8.removeAll(var3);
            var1.put(var7, var8);
         }
      }

      return var1;
   }

   public static Set<String> getCachedFidsByPage(ExpClass var0, String var1) {
      String var2 = getRealPageName(var1);
      Object var3 = new HashSet();

      try {
         Map var4 = (Map)g_page_fids.get(g_active_form_id);
         if (var4 == null) {
            var4 = createFidsByPage(g_active_form_id);
            g_page_fids.put(g_active_form_id, var4);
         }

         var3 = (Set)var4.get(var2);
      } catch (Exception var5) {
         writeError(var0, ERR_FN_ID, "Számítási hiba! " + var5.getMessage() + " Paraméter:" + var2, var5, var0);
      }

      return (Set)var3;
   }

   private static Map<String, Set<String>> createFidsByPage(String var0) {
      HashMap var1 = new HashMap();
      FormModel var2 = gui_info.get(var0);
      Hashtable var3 = var2.fids_page;

      String var5;
      Object var8;
      for(Enumeration var4 = var3.keys(); var4.hasMoreElements(); ((Set)var8).add(var5)) {
         var5 = (String)var4.nextElement();
         PageModel var6 = (PageModel)var3.get(var5);
         String var7 = var6.name;
         var8 = (Set)var1.get(var7);
         if (var8 == null) {
            var8 = new HashSet();
            var1.put(var7, var8);
         }
      }

      return var1;
   }

   public static synchronized void fnMatrixSzures(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      byte var1 = 2;
      var0.setType(0);
      var0.setResult((Object)null);
      int var4 = var0.getParametersCount();

      try {
         boolean var5 = false;
         String var2 = ((String)getParameter(var0, 0, true, 1, var5)).toLowerCase();
         String var3 = (String)getParameter(var0, 1, true, 1, var5);
         if (var2 == null || var3 == null) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
            return;
         }

         MatrixSearchModel var6 = new MatrixSearchModel(var2, var3);
         setSearchParameters(var0, var6, var1);
         var0.setResult(var6);
         var0.setType(5);
      } catch (Exception var7) {
         var7.printStackTrace();
         writeError(var0, ERR_FN_ID, "Számítási hiba! " + var7.getMessage(), var7, var0);
      }

   }

   public static synchronized void fnFeltetelesErtekLista(ExpClass var0) {
      feltetelesErtekLista(var0);
   }

   public static synchronized void fnFeltetelesErtekListaGroup(ExpClass var0) {
      feltetelesErtekLista(var0);
   }

   public static synchronized void feltetelesErtekLista(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);

      try {
         Boolean var1 = (Boolean)getParameter(var0, 0, true, 4, false);
         Integer var2 = NumericOperations.getInteger(getParameter(var0, 1, true, 2, false));
         String var3 = (String)getParameter(var0, 2, true, 1, false);
         MatrixSearchModel var4 = (MatrixSearchModel)getParameter(var0, 3, true, 5, false);
         if (var1 == null || var2 == null || var3 == null || var4 == null) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
            return;
         }

         LookupListModel var5 = new LookupListModel(var1, var2, var3, var4);
         var0.setResult(var5);
         var0.setType(6);
      } catch (Exception var6) {
         var6.printStackTrace();
         writeError(var0, ERR_FN_ID, "Számítási hiba! " + var6.getMessage(), var6, var0);
      }

   }

   public static synchronized void fnElozoErtek(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);

      try {
         if (g_current_field_id == null) {
            throw new Exception("Mezőhöz kötött képletben használható az előző_érték függvény!");
         }

         String var1 = getSelfFid(var0);
         int var2 = getDPageNumber();
         ExpClass var3 = getDFieldValue(var0, var1, var2);
         var0.setType(var3.getType());
         var0.setValue(var3.getValue());
         if (g_previous_item != null && var1.equalsIgnoreCase(g_previous_item.code) && var2 == g_previous_item.index) {
            ExpClass var4 = ExpFactoryLight.createConstant(g_previous_item.value.toString(), getFieldType(var1));
            var0.setValue(var4.getValue());
            var0.setType(var4.getType());
         }
      } catch (Exception var5) {
         var5.printStackTrace();
         writeError(var0, ERR_FN_ID, "Számítási hiba! " + var5.getMessage(), var5, var0);
      }

   }

   private static int getDPageNumber() {
      Integer var0 = FIdHelper.getDPageNumber(getSelf());
      return var0 != null && var0 instanceof Integer ? (Integer)var0 : -1;
   }

   public static synchronized void fnCompareStrings(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);

      try {
         int var1 = var0.getParametersCount();
         if (var1 < 4) {
            throw new Exception("Program hiba, a paraméterek száma vagy típusa nem megfelelő!");
         } else {
            Object var2 = getParameter(var0, 0, false, 0, false);
            Object var3 = getParameter(var0, 1, false, 0, false);
            String var4 = (String)getParameter(var0, 2, true, 1, false);
            Boolean var5 = (Boolean)getParameter(var0, 3, true, 4, false);
            if (var4 != null && var5 != null) {
               if (var2 != null && var3 != null) {
                  String var6 = var5 ? var2.toString() : var2.toString().toLowerCase();
                  String var7 = var5 ? var3.toString() : var3.toString().toLowerCase();
                  if (var6.length() != 0 && var7.length() != 0) {
                     HunCharComparator var8 = new HunCharComparator();
                     int var9 = var8.compare(var6, var7);
                     int var10 = getRelation(var4);
                     setInvalid(var0);
                     if (var9 < 0 && var10 == 7) {
                        setValid(var0);
                     } else if (var9 == 0 && var10 == 9) {
                        setValid(var0);
                     } else if (var9 > 0 && var10 == 8) {
                        setValid(var0);
                     }

                  } else {
                     setValid(var0);
                  }
               } else {
                  setValid(var0);
               }
            } else {
               writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
            }
         }
      } catch (Exception var11) {
         var11.printStackTrace();
         writeError(var0, ERR_FN_ID, "Számítási hiba! " + var11.getMessage(), var11, var0);
      }
   }

   public static synchronized void fnDinMaximum(ExpClass var0) {
      dinValues(var0, true);
   }

   public static synchronized void fnDinMinimum(ExpClass var0) {
      dinValues(var0, false);
   }

   public static void dinValues(ExpClass var0, boolean var1) {
      try {
         var0.setType(0);
         var0.setResult((Object)null);
         int var2 = var0.getParametersCount();
         if (var2 < 1) {
            throw new Exception("Program hiba, a paraméterek száma vagy típusa nem megfelelő!");
         }

         ExpClass var3 = var0.getParameter(0);
         if (var2 < 2) {
            setResult(var0, var3);
            return;
         }

         BigDecimal var4 = null;

         for(int var5 = 1; var5 < var2; ++var5) {
            String var6 = (String)getParameter(var0, var5, true, 1, true);
            if (getFieldType(var6) != 2) {
               throw new Exception("Program hiba, a paraméterek száma vagy típusa nem megfelelő!");
            }

            String var7 = getPageNameByField(var0, var6);
            int var8 = getPageCounter(var0, var7);

            for(int var9 = 0; var9 < var8; ++var9) {
               BigDecimal var10 = getDFieldNumberValue(var6, var9);
               var4 = compareValues(var4, var10, var1);
            }
         }

         if (var4 != null) {
            setResult(var0, ExpFactoryLight.createConstant(var4.toString(), 2));
         } else {
            setResult(var0, var3);
         }

         if (!g_in_variable_exp && !isFieldEmpty(var0)) {
            g_all_fileds_empty = false;
         }
      } catch (Exception var11) {
         var11.printStackTrace();
         writeError(var0, ERR_FN_ID, "Számítási hiba! " + var11.getMessage(), var11, var0);
      }

   }

   private static BigDecimal compareValues(BigDecimal var0, BigDecimal var1, boolean var2) {
      if (var0 == null) {
         return var1;
      } else if (var1 == null) {
         return var0;
      } else {
         return !var2 ^ var0.compareTo(var1) == 1 ? var0 : var1;
      }
   }

   private static ExpClass setResult(ExpClass var0, ExpClass var1) {
      var0.setValue(var1.getValue());
      var0.setType(var1.getType());
      var0.setFlag(var1.getFlag());
      return var0;
   }

   private static ExpClass setValid(ExpClass var0) {
      var0.setType(4);
      var0.setResult(Boolean.TRUE);
      return var0;
   }

   private static ExpClass setInvalid(ExpClass var0) {
      var0.setType(4);
      var0.setResult(Boolean.FALSE);
      return var0;
   }

   private static boolean isExistingField(String var0) {
      Hashtable var1 = (Hashtable)MetaInfo.getInstance().getFieldMetas(g_active_form_id);
      return var1.containsKey(var0);
   }

   private static boolean isFieldIdType(String var0) {
      return var0 != null && var0.indexOf("_") == -1;
   }

   public static synchronized void fnNapokSzama(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      byte var1 = 7;

      try {
         String var2 = (String)((String)getParameter(var0, 0, true, 1, true));
         String var3 = (String)getParameter(var0, 1, true, 1, true);
         String var4 = (String)getParameter(var0, 2, true, 1, true);
         boolean var5 = (Boolean)getParameter(var0, 3, true, 4, true);
         Integer var6 = NumericOperations.getInteger(getParameter(var0, 4, true, 2, true));
         Integer var7 = NumericOperations.getInteger(getParameter(var0, 5, true, 2, true));
         String var8 = (String)getParameter(var0, 6, true, 1, true);
         if (var2 != null && var3 != null && var4 != null && var6 != null && var7 != null && var8 != null) {
            int var9 = var0.getParametersCount();
            if (var9 >= var1 && (var9 - var1) % 3 == 0) {
               String var10 = getPageNameByUniParams(var0, var3);
               checkSamePage(var0, var10, var4);
               if (var8 != null && var8.length() != 0) {
                  checkSamePage(var0, var10, var8);
               }

               int var11 = -1;
               Vector var12 = getFieldFidsByUniParams(var0, var3);
               Vector var13 = new Vector();

               for(int var14 = var1; var14 < var9; var14 += 3) {
                  String var15 = (String)getParameter(var0, var14, true, 1, true);
                  checkSamePage(var0, var10, var15);
                  int var16 = getRelation((String)getParameter(var0, var14 + 1, true, 1, true));
                  if (var16 < 0) {
                     throw new Exception("Hibás reláció");
                  }

                  Vector var17 = getFieldFidsByUniParams(var0, var15);
                  var11 = checkRowCount(var11, var17);
                  if (var17.size() > 1) {
                     checkRowCount(var11, var12);
                  }

                  Object var18 = getSzuroErtek(getParameter(var0, var14 + 2, false, 0, false), getFieldType((String)var17.get(0)));
                  var13.add(new FunctionBodies.Filter(var17, var16, var18));
               }

               checkCsoportosIntervallumAtfedesWithLength(var0, var2, var3, var4, var5, var6, var7, var8, var13);
            } else {
               writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
            }
         } else {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
         }
      } catch (Exception var19) {
         writeError(var0, ID_EX_FN_INTERVALLUM_SZURES, "Hiba az intervallum szűrés során", (Exception)null, var19.getMessage());
      }
   }

   public static synchronized void checkCsoportosIntervallumAtfedesWithLength(ExpClass var0, String var1, String var2, String var3, boolean var4, Integer var5, Integer var6, String var7, Vector<FunctionBodies.Filter> var8) {
      byte var10 = -1;
      boolean var13 = false;
      HashMap var17 = new HashMap();
      Vector var18 = new Vector();
      Vector var19 = new Vector();
      Vector var20 = new Vector();
      Vector var21 = new Vector();
      Comparator var22 = new Comparator() {
         public int compare(Object var1, Object var2) {
            return ((SimpleDateFormat)((SimpleDateFormat)((IIntervalHandler)var1).getInterval().getStartValue())).getCalendar().compareTo(((SimpleDateFormat)((SimpleDateFormat)((IIntervalHandler)var2).getInterval().getStartValue())).getCalendar());
         }
      };
      var0.setType(0);
      var0.setResult((Object)null);

      String var9;
      int var11;
      Integer var12;
      Vector var14;
      Vector var15;
      Vector var16;
      try {
         var9 = getPageNameByUniParams(var0, var2);
         var14 = getFieldFidsByUniParams(var0, var2);
         int var32 = checkRowCount(var10, var14);
         var15 = getFieldFidsByUniParams(var0, var3);
         checkRowCount(var32, var15);
         if (var7 != null && var7.length() != 0) {
            var16 = getFieldFidsByUniParams(var0, var7);
            if (var16.size() > 1) {
               checkRowCount(var32, var16);
            }
         } else {
            var16 = null;
         }

         var11 = getPageCounter(var0, var9);
         var12 = isLaphozKotott() ? FIdHelper.getDPageNumber(getSelf()) : 0;
      } catch (Exception var31) {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, var31.getMessage());
         return;
      }

      int var23 = var4 ? var14.size() : var11 * var14.size();
      boolean var24 = var1 != null && var1.length() > 0;

      try {
         int var25 = 0;
         if (isLaphozKotott() && var4) {
            var25 = var12;
            var11 = var12 + 1;
         }

         for(int var26 = var25; var26 < var11; ++var26) {
            for(int var27 = 0; var27 < var14.size(); ++var27) {
               boolean var28 = true;

               for(int var29 = 0; var29 < var8.size() && var28; ++var29) {
                  var28 = ((FunctionBodies.Filter)var8.elementAt(var29)).check(var0, var27, var26);
               }

               if (var28) {
                  var13 = var13 || !catalogIntervallumEloszlas(var0, var14, var15, var16, var17, var27, var26, var23, var24, var1, var9);
               }
            }

            if (var4) {
               atfedesEllenorzes(var17, var18);
               intervallumEloszlasEllenorzes(var17, var19, var20, var21, var5, var6, var22);
               var17 = new HashMap();
            }
         }

         if (!var4) {
            atfedesEllenorzes(var17, var18);
            intervallumEloszlasEllenorzes(var17, var19, var20, var21, var5, var6, var22);
         }
      } catch (Exception var30) {
         writeError(var0, ID_EX_FN_INTERVALLUM_SZURES, "Hiba az intervallum szűrés során", (Exception)null, var30.getMessage());
         return;
      }

      if (isFieldCheck() || isFormCheck()) {
         listErrors(var0, var18, var9);
         listLengthErrors(var0, var19, var9, var5);
         listUnionErrors(var0, var21, var9, var5);
         listDistanceErrors(var0, var20, var9, var6);
      }

      var13 = var13 || var18.size() != 0 || var20.size() != 0 || var19.size() != 0 || var21.size() != 0;
      var0.setType(4);
      var0.setResult(!var13);
   }

   private static boolean catalogIntervallumEloszlas(ExpClass var0, Vector var1, Vector var2, Vector var3, Map<String, Vector<IIntervalHandler>> var4, int var5, int var6, int var7, boolean var8, String var9, String var10) {
      Object var11 = getExpValue(getFValue((String)var1.elementAt(var5), var6));
      Object var12 = getExpValue(getFValue((String)var2.elementAt(var5), var6));
      if (var11 == null && var12 == null) {
         return true;
      } else {
         if (var11 == null) {
            var11 = var12;
         }

         if (var12 == null) {
            var12 = var11;
         }

         String var13 = createGroupId(var3, var5, var6);
         String var14 = var8 ? var9 + var11.toString() : var11.toString();
         String var15 = var8 ? var9 + var12.toString() : var12.toString();

         DefaultIntervalHandler var16;
         try {
            DateIntervalImpl var17 = new DateIntervalImpl();
            if (!var17.setValues(var14, var15)) {
               throw new Exception("Nem dátum típusú az adat.");
            }

            var16 = new DefaultIntervalHandler(var17, (String)var1.elementAt(var5), var6);
         } catch (Exception var18) {
            setIntervalErrorMessage(var0, var10, new DefaultIntervalHandler(new DummyIntervalImpl(var14, var15), (String)var1.elementAt(var5), var6), "Nem dátum típusú az adat.");
            return false;
         }

         addToGroups(var4, var13, var16, var7);
         return true;
      }
   }

   private static void intervallumEloszlasEllenorzes(Map<String, Vector<IIntervalHandler>> var0, Vector<IIntervalHandler> var1, Vector<IIntervalHandler> var2, Vector<Vector<IIntervalHandler>> var3, int var4, int var5, Comparator var6) {
      Iterator var7 = var0.values().iterator();

      while(var7.hasNext()) {
         Vector var8 = (Vector)var7.next();
         Collections.sort(var8, var6);
         IIntervalHandler var9 = (IIntervalHandler)var8.elementAt(0);
         IInterval var10 = var9.getInterval();
         DateIntervalImpl var11 = new DateIntervalImpl(var10.getOriginalStartValue(), var10.getOriginalEndValue());
         if (var10.length() + 1 > var4) {
            var1.add(var9);
         }

         Vector var12 = new Vector();
         var12.add(var9);

         for(int var13 = 1; var13 < var8.size(); ++var13) {
            IIntervalHandler var14 = (IIntervalHandler)var8.elementAt(var13);
            IInterval var15 = var14.getInterval();
            if (var15.length() + 1 > var4) {
               var1.add(var14);
            }

            if (var11.isContinual(var15)) {
               var11.union(var15);
               var12.add(var14);
               if (var13 == var8.size() - 1 && var11.length() + 1 > var4) {
                  var12.add(new DefaultIntervalHandler(new DateIntervalImpl(var11.getOriginalStartValue(), var11.getOriginalEndValue()), var9.getFid(), var9.getPageNumber()));
                  var3.add(var12);
               }
            } else {
               if (var11.distance(var15) - 1 < var5) {
                  var2.add(var12.size() > 1 ? (IIntervalHandler)var12.lastElement() : var9);
                  var2.add(var14);
               }

               if (var12.size() > 1 && var11.length() + 1 > var4) {
                  var12.add(new DefaultIntervalHandler(new DateIntervalImpl(var11.getOriginalStartValue(), var11.getOriginalEndValue()), var9.getFid(), var9.getPageNumber()));
                  var3.add(var12);
               }

               var9 = var14;
               var10 = var14.getInterval();
               var11 = new DateIntervalImpl(var10.getOriginalStartValue(), var10.getOriginalEndValue());
               var12 = new Vector();
               var12.add(var14);
            }
         }
      }

   }

   private static void listDistanceErrors(ExpClass var0, Vector<IIntervalHandler> var1, String var2, Integer var3) {
      for(int var4 = 0; var4 < var1.size(); var4 += 2) {
         setIntervalErrorMessage(var0, var2, (IIntervalHandler)var1.elementAt(var4), String.format("Legalább %s napnak el kell telnie a következő üzenetben hivatkozott időszakig.", var3.toString()));
         setIntervalErrorMessage(var0, var2, (IIntervalHandler)var1.elementAt(var4 + 1), String.format("Legalább %s napnak el kell telnie az előző üzenetben hivatkozott időszaktól.", var3.toString()));
      }

   }

   private static void listLengthErrors(ExpClass var0, Vector<IIntervalHandler> var1, String var2, Integer var3) {
      for(int var4 = 0; var4 < var1.size(); ++var4) {
         setIntervalErrorMessage(var0, var2, (IIntervalHandler)var1.elementAt(var4), String.format("Az időszak hosszabb mint a megengedett %s nap.", var3.toString()) + " (" + (((IIntervalHandler)var1.elementAt(var4)).getInterval().length() + 1) + ")");
      }

   }

   private static void listUnionErrors(ExpClass var0, Vector<Vector<IIntervalHandler>> var1, String var2, Integer var3) {
      for(int var4 = 0; var4 < var1.size(); ++var4) {
         Vector var5 = (Vector)var1.elementAt(var4);

         for(int var6 = 0; var6 < var5.size() - 1; ++var6) {
            setIntervalErrorMessage(var0, var2, (IIntervalHandler)var5.elementAt(var6), "Az időszak összevonásra került az alábbi időszakokkal.");
         }

         setIntervalErrorMessage(var0, var2, (IIntervalHandler)var5.elementAt(var5.size() - 1), String.format("Az összevont időszak hosszabb mint a megengedett %s nap.", var3.toString()) + " (" + (((IIntervalHandler)var5.elementAt(var5.size() - 1)).getInterval().length() + 1) + ")");
      }

   }

   public static Vector getReadOnlyCalcFields(String var0, String var1) {
      return (Vector)g_readOnlyCalcFields.get(getKey(var0, var1));
   }

   public static void setReadOnlyCalcFields(String var0, String var1) {
      try {
         String var2 = getPageIdByFid(var0, var1);
         if (var2 == null) {
            return;
         }

         String var3 = getKey(var0, var2);
         Vector var4 = (Vector)g_readOnlyCalcFields.get(var3);
         if (var4 == null) {
            var4 = new Vector();
            g_readOnlyCalcFields.put(var3, var4);
         }

         var4.add(var1);
      } catch (Exception var5) {
      }

   }

   public static void setFoAdatDependency(String var0, String var1, String var2) {
      try {
         if (var0 == null || var1 == null || var2 == null) {
            return;
         }

         HashMap var3 = (HashMap)g_fo_adat_dependency.get(var0);
         if (var3 == null) {
            var3 = new HashMap();
            g_fo_adat_dependency.put(var0, var3);
         }

         HashSet var4 = (HashSet)var3.get(var1);
         if (var4 == null) {
            var4 = new HashSet();
            var3.put(var1, var4);
         }

         var4.add(var2);
      } catch (Exception var5) {
      }

   }

   public static boolean isFoAdatDependency(String var0, String var1) {
      try {
         HashMap var2 = getFoAdatFormFids(var0);
         return var2 != null && var1 != null ? var2.containsKey(var1) : false;
      } catch (Exception var3) {
         return false;
      }
   }

   public static HashSet<String> getSubFormFoAdatDependency(String var0, String var1) {
      try {
         HashSet var2 = null;
         HashMap var3 = getFoAdatFormFids(var0);
         if (var3 != null && var1 != null) {
            var2 = (HashSet)var3.get(var1);
         }

         return var2 == null ? new HashSet() : var2;
      } catch (Exception var4) {
         return new HashSet();
      }
   }

   public static HashMap<String, HashSet<String>> getFoAdatFormFids(String var0) {
      try {
         return var0 == null ? new HashMap() : (HashMap)g_fo_adat_dependency.get(var0);
      } catch (Exception var2) {
         return new HashMap();
      }
   }

   public static String getKey(String var0, String var1) {
      return var0 + "###" + var1;
   }

   public static HashSet getFeltetelesErtekFieldsList(String var0) {
      return (HashSet)g_feltetelesErtekFields.get(var0);
   }

   public static void setFeltetelesErtekFieldsList(String var0, String var1) {
      try {
         HashSet var2 = (HashSet)g_feltetelesErtekFields.get(var0);
         if (var2 == null) {
            var2 = new HashSet();
            g_feltetelesErtekFields.put(var0, var2);
         }

         var2.add(var1);
      } catch (Exception var3) {
      }

   }

   public static String getPageIdByFid(String var0, String var1) {
      try {
         FormModel var2 = gui_info.get(var0);
         PageModel var3 = (PageModel)var2.fids_page.get(var1);
         return var3.pid;
      } catch (Exception var4) {
         return null;
      }
   }

   public static synchronized void fnPreGenErtek(ExpClass var0) {
      fnGenErtek(var0, "fnPreGenErtek");
   }

   public static synchronized void fnPostGenErtek(ExpClass var0) {
      fnGenErtek(var0, "fnPostGenErtek");
   }

   public static synchronized void fnGenErtek(ExpClass var0, String var1) {
      var0.setType(4);
      var0.setResult(Boolean.TRUE);
      ExpClass var2 = var0.getParameter(0);
      ExpClass var3 = var0.getParameter(1);
      String var4 = null;
      String var5 = null;

      try {
         var4 = (String)ABEVFunctionSet.expwrapper.get("", var2);
         var5 = getFidByVid(var4, getFormid());
      } catch (Exception var15) {
         writeError(var0, ID_EX_FN_FIELD_ID, "Program hiba, hiba történt a mező azonosító meghatározásakor!", (Exception)null, var4 + "/" + var5);
      }

      if (var5 == null) {
         writeError(var0, ID_EX_FN_FIELD_ID, "Program hiba, hiba történt a mező azonosító meghatározásakor!", (Exception)null, var4 + "/" + var5);
      }

      try {
         int var6 = 1;
         if (FIdHelper.isFieldDynamic(var5)) {
            var6 = FIdHelper.getPageCount(var5);
         }

         String var7 = FIdHelper.getPageId(var5);

         for(int var8 = 0; var8 < var6; ++var8) {
            ExpClass var9 = getDFieldValue(var0, var5, var8);
            calculateDFieldValue(var3, var7, var8);
            Object var10 = getRealZeroValueCore(var3);
            setDataValue(var0, var8 + "_" + var5, var5, var10);
            if (isFieldChanged(var9.getValue(), var10)) {
               addGenErtekChangedField(var5);
            }

            System.out.println(var1 + ":" + var4 + "/" + var8 + "  :" + var10 + "    (fid:" + var5 + ") ");
         }
      } catch (Exception var16) {
         writeError(var0, ID_EX_FN_FIELD_ID, "Program hiba, hiba történt a mező azonosító meghatározásakor!", (Exception)null, var4 + "/" + var5);
      } finally {
         FIdHelper.resetDinPageNumber((Hashtable)null);
      }

   }

   private static void addGenErtekChangedField(String var0) {
      try {
         g_gen_ertek_changed_field_value.add(var0);
      } catch (Exception var2) {
         System.out.println("Error: fnBetoltErtek_addGenErtekChangedField");
      }

   }

   private static boolean isFieldChanged(Object var0, Object var1) {
      try {
         Object var2 = getNull(var0);
         Object var3 = getNull(var1);
         if (var2 != null) {
            return !var2.equals(var3);
         } else {
            return var3 != null;
         }
      } catch (Exception var4) {
         return false;
      }
   }

   private static Object getNull(Object var0) {
      if (var0 == null) {
         return null;
      } else {
         return var0 instanceof String && ((String)var0).length() == 0 ? null : var0;
      }
   }

   private static Object calculateDFieldValue(ExpClass var0, String var1, int var2) {
      Hashtable var3 = new Hashtable();
      var3.put(getRealPageName(var1), new Integer(var2));
      FIdHelper.setDinPageNumber(var3);
      Object var4 = null;

      try {
         var4 = ABEVFunctionSet.expwrapper.get("", var0);
      } catch (Exception var6) {
         writeError(var0, ERR_FN_ID, "Számítási hiba! ", var6, var4);
      }

      FIdHelper.resetDinPageNumber(var3);
      return var4;
   }

   public static synchronized void fnGenAktualisErtek(ExpClass var0) {
      getAktualisErtekCore(var0);
   }

   public static synchronized void getAktualisErtekCore(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      String var1 = null;

      try {
         IDataStore var2 = g_active_data_store;
         ExpClass var3 = var0.getParameter(0);
         var1 = (String)ABEVFunctionSet.expwrapper.get("", var3);
         if (var1 == null) {
            return;
         }

         String var4 = getFidByVid(var1, getFormid());
         if (var4 == null) {
            writeError(var0, ID_EX_FN_FIELD_ID, "Program hiba, hiba történt a mező azonosító meghatározásakor!", (Exception)null, var1);
            return;
         }

         Object[] var5 = FIdHelper.getDataStoreKey(var4);
         String var6 = var2.get(var5);
         if (var6 == null) {
            var6 = "";
         }

         int var7 = getFieldType(var4);
         ExpClass var8 = ExpFactoryLight.createConstant(var6, var7);
         var0.setType(var8.getType());
         var0.setResult(var8.getResult());
      } catch (Exception var9) {
         writeError(var0, ID_EX_FN_FIELD, "Program hiba, hiba történt a mező adatok megszerzésekor!", (Exception)null, var1);
      }

   }

   public static synchronized void fnGenUtolsoDinOldal(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);

      try {
         String var1 = (String)getParameter(var0, 0, true, 1, false);
         String var2 = getFidByVid(var1, getFormid());
         if (var2 == null) {
            writeError(var0, ID_EX_FN_FIELD_ID, "Program hiba, hiba történt a mező azonosító meghatározásakor!", (Exception)null, var1);
            return;
         }

         Vector var3 = gui_info.get_dfield_values(var2);
         if (var3 != null) {
            Vector var4 = (Vector)var3;
            int var5 = var4.size();
            int var6 = 0;

            while(var6 < 1 && var5-- > 0) {
               String var7 = (String)var4.get(var5);
               if (var7 != null && var7.length() != 0) {
                  var6 = var5 + 1;
               }
            }

            var0.setType(2);
            var0.setResult(new BigDecimal(var6));
         } else {
            writeError(var0, ID_EX_FN_FIELD, "Program hiba, hiba történt a mező adatok megszerzésekor!", (Exception)null, var1);
         }
      } catch (Exception var8) {
         writeError(var0, ID_EX_FN_FIELD, "Program hiba, hiba történt a mező adatok megszerzésekor!", (Exception)null, var8);
      }

   }

   public static synchronized void fnGenMezoAzonosito(ExpClass var0) {
      var0.setType(var0.getParameter(0).getType());
      var0.setResult(var0.getParameter(0).getValue());
   }

   public static synchronized void fnFoAdat(ExpClass var0) {
      byte var2 = 0;
      Object var3 = null;
      var0.setType(var2);
      var0.setResult(var3);
      int var1 = var0.getParametersCount();
      if (var1 == 2) {
         String var4;
         try {
            var4 = (String)getParameter(var0, 0, true, 1, true);
         } catch (Exception var8) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
            return;
         }

         if (var4 == null) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
            return;
         }

         String var5;
         try {
            var5 = (String)getParameter(var0, 1, true, 1, true);
         } catch (Exception var7) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
            return;
         }

         if (var5 == null) {
            writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
            return;
         }

         ExpClass var6 = getMultiValue(var0, var4, var5);
         var0.setType(var6.getType());
         var0.setResult(var6.getValue());
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnElozmenyErtek(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
   }

   public static synchronized void fnParameteresMasolas(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);

      try {
         gui_info.setFieldReadOnly(getSelfFid(var0), false);
         ExpClass var1 = getFieldValue((ExpClass)null, FIdHelper.getFieldId(g_current_field_id));
         var0.setType(var1.getType());
         var0.setResult(var1.getValue());
      } catch (Exception var2) {
         writeError(var0, ERR_FN_ID, "Számítási hiba! " + var2.getMessage(), var2, var0);
      }

   }

   public static synchronized void fnSpecLancDarab(ExpClass var0) {
      setOriginalValue(var0);
   }

   public static synchronized void fnSpecLancSum(ExpClass var0) {
      setOriginalValue(var0);
   }

   public static synchronized void setOriginalValue(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);

      try {
         ExpClass var1 = getFieldValue((ExpClass)null, FIdHelper.getFieldId(g_current_field_id));
         var0.setType(var1.getType());
         var0.setResult(var1.getValue());
      } catch (Exception var2) {
         writeError(var0, ERR_FN_ID, "Számítási hiba! " + var2.getMessage(), var2, var0);
      }

   }

   public static synchronized void fnMezoLista(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
   }

   public static synchronized void fnBetoltErtek(ExpClass var0) {
      fnGenErtek(var0, "fnBetoltErtek");
   }

   public static synchronized void fnBetoltEredetiErtek(ExpClass var0) {
      getAktualisErtekCore(var0);
   }

   public static List<String> isGenErtekChangedFieldValue() {
      return g_gen_ertek_changed_field_value;
   }

   public static List<String> getGenErtekChangedFieldValue() {
      return g_gen_ertek_changed_field_value;
   }

   public static void resetGenErtekChangedFieldValue() {
      g_gen_ertek_changed_field_value = new ArrayList();
   }

   public static void fnJoAdoszam(ExpClass var0) {
      fnAdoszamCore(var0, "08", "12349");
   }

   public static synchronized void fnJoCsoportAzonosito(ExpClass var0) {
      fnAdoszamCore(var0, "08", "5");
   }

   public static void fnErvenyesAdoszam(ExpClass var0) {
      fnAdoszamCore(var0, "08", "1234569");
   }

   public static void fnAdoszamCore(ExpClass var0, String var1, String var2) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var3 = var0.getParametersCount();
      boolean var7 = false;
      if (var3 == 1) {
         ExpClass var5 = var0.getParameter(0);
         int var4 = var5.getType();
         if (var4 == 0) {
            return;
         }

         if (var4 != 2 && var4 != 1) {
            writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
         } else {
            String var6 = var5.getValue().toString().trim();
            StringBuffer var8 = new StringBuffer();

            int var9;
            for(var9 = 0; var9 < var6.length(); ++var9) {
               if (!var6.substring(var9, var9 + 1).equals("-")) {
                  var8.append(var6.substring(var9, var9 + 1));
               }
            }

            var0.setType(4);
            if (chkInputIds(var4, var8.toString(), 11) < 0) {
               var0.setResult(Boolean.FALSE);
               return;
            }

            if (var8.length() == 11 && var2.contains(var8.substring(8, 9)) && !var1.contains(var8.substring(0, 1))) {
               var9 = Integer.parseInt(var8.substring(9, 11));
               if (1 < var9 && var9 < 45 && var9 != 21 || var9 == 51) {
                  var7 = cdv(var8.substring(0, 8));
               }
            }

            var0.setResult(var7);
         }
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Program hiba, a paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static synchronized void fnDinStringSum(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      String var1 = null;
      StringBuffer var5 = new StringBuffer();

      try {
         boolean var6 = false;
         String var7 = (String)getParameter(var0, 0, true, 1, var6);
         String var8 = getFidByVid(var7, getFormid());
         if (var8 == null) {
            writeError(var0, ID_EX_FN_FIELD_ID, "Program hiba, hiba történt a mező azonosító meghatározásakor!", (Exception)null, var7);
            return;
         }

         Vector var2 = gui_info.get_dfield_values(var8);
         if (var2 != null) {
            if (var2 instanceof Vector) {
               Vector var4 = (Vector)var2;
               int var9 = 0;

               for(int var10 = var4.size(); var9 < var10; ++var9) {
                  String var13 = (String)var4.get(var9);
                  ExpClass var12;
                  if (var13 != null && var13.length() != 0 && (var12 = ExpFactoryLight.createConstant(var13, 1)) != null) {
                     Object var11 = var12.getValue();
                     int var3 = var12.getType();
                     if (!g_in_variable_exp && !isFieldEmpty(var12)) {
                        g_all_fileds_empty = false;
                     }

                     if (var3 != 0 && var11 != null) {
                        if (var3 != 1) {
                           writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", (Exception)null, (Object)null);
                           break;
                        }

                        var5.append(var13);
                     }
                  }
               }

               var1 = var5.toString();
            }
         } else {
            writeError(var0, ID_EX_FN_FIELD, "Program hiba, hiba történt a mező adatok megszerzésekor!", (Exception)null, var7);
         }
      } catch (Exception var14) {
         writeError(var0, ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés!", var14, (Object)null);
      }

      if (var1 != null) {
         var0.setType(1);
         var0.setResult(var1);
      }

   }

   public static synchronized void fnLogikaiNull(ExpClass var0) {
      IDataStore var1 = g_active_data_store;
      var0.setType(4);
      var0.setResult(Boolean.FALSE);
      String var2 = null;
      ExpClass var3 = null;
      String var5 = null;

      try {
         String var4 = (String)getParameter(var0, 0, true, 1, true);
         if (var4 == null) {
            return;
         }

         var5 = getFidByVid(var4, getFormid());
         if (var5 == null) {
            return;
         }

         int var6 = getFieldType(var5);
         if (var6 != 4) {
            return;
         }

         Object[] var7 = FIdHelper.getDataStoreKey(var5);
         g_exp_fields.add((String)var7[1], (Integer)var7[0]);
         var2 = var1.get(var7);
         if (var2 == null || "".equals(var2)) {
            var0.setResult(Boolean.TRUE);
            var2 = "";
         }

         var3 = ExpFactoryLight.createConstant(var2, var6);
      } catch (Exception var8) {
         writeError(var0, ID_EX_FN_FIELD, "Program hiba, hiba történt a mező adatok megszerzésekor! fnLogikaiNull(" + var5 + ")", var8, (Object)null);
      }

      if (!g_in_variable_exp && !isFieldEmpty(var3)) {
         g_all_fileds_empty = false;
      }

   }

   static {
      ID_FILLIN_ERROR = ABEVFunctionSet.ID_DEFAULT_FUNCTION_ERROR;
      DECIMAL_FORMAT_MASK = "#.##############################";
      ERR_FN_ID = new Long(12111L);
      ID_ERR_MISSING_CALCULATOR_PARAM = new Long(12121L);
      ID_ERR_MISSING_GUI_PARAM = new Long(12112L);
      ID_ERR_GUI_INFO = new Long(12114L);
      ERR_ID_FN_DIFF_COL_COUNT = new Long(12113L);
      ID_ERR_GUI_FIELD_SET = new Long(12115L);
      ID_ERR_MATRIX_SEARCH_OC = new Long(12117L);
      ID_ERR_CHECK_UNIQUEM = new Long(12118L);
      ID_ERR_CHECK_COMPAREAM = new Long(12119L);
      ID_ERR_CHECK_COMP = new Long(12120L);
      ID_ERR_EU_MEMBERSHIP = new Long(12121L);
      ID_ERR_GET_MULTI_VALUE = new Long(12121L);
      ID_ERR_ELOZO_ERTEK_PAR = new Long(12123L);
      ids_info = new Object[]{null, null};
      expRequest = new Object[]{"get_exp_structure", null};
      par_field_do_calculation = new Object[]{null, null, null, null, null, null, null, null};
      specfgv1_searchIndex = new int[]{0, 0};
      specfgv1_searchValues = new Object[]{null, null, null};
      specfgv2_searchIndex = new int[]{0, 0, 0};
      specfgv2_searchValues = new Object[]{null, null, null};
      getfieldmetas = new Object[]{"get_field_metas", null};
      getfieldguis = new Object[]{"get_field_datas", null};
      get_field_type = new Object[]{"get_field_type", null};
      calendar = new GregorianCalendar(new Locale("hu", "HU"));
      formatter = new SimpleDateFormat("yyyyMMdd", new Locale("hu", "HU"));
      g_active_form_id = null;
      g_function_description = new Hashtable();
      g_variables = new Hashtable();
      g_all_fileds_empty = true;
      g_in_variable_exp = false;
      g_extended_error = "";
      g_calc_record = null;
      g_current_field_id = null;
      g_previous_item = null;
      g_active_data_store = null;
      g_readonly_calc_state = false;
      g_readonly_calc_act_page_number = new Integer(0);
      g_field_types = new Hashtable();
      g_designed_page_names = new Hashtable();
      g_real_page_names = new Hashtable();
      g_real_vid_names = new Hashtable();
      g_designed_vid_names = new Hashtable();
      g_table_fids = new Hashtable();
      g_readOnlyCalcFields = new Hashtable();
      g_feltetelesErtekFields = new Hashtable();
      g_page_fids = new Hashtable();
      g_visible_page_fids = new Hashtable();
      g_vids = new Hashtable();
      g_rows = new Hashtable();
      g_compaream = new Hashtable();
      g_exp_fields = new FunctionBodies.ExpressionFields();
      g_compaream_source = -1;
      g_saved_active_form_id = null;
      g_saved_active_data_store = null;
      isFullCheck = false;
      isFullCalc = false;
      g_din_expressions = null;
      g_gen_ertek_changed_field_value = new ArrayList();
      g_fo_adat_dependency = new HashMap();
   }

   public static class ExpressionFields {
      private Vector<GoToButton> list = new Vector();
      private boolean collect;

      public void release() {
         this.list = new Vector();
      }

      public boolean isCollect() {
         return this.collect;
      }

      public void setCollect(boolean var1) {
         this.collect = var1;
      }

      public void add(String var1, int var2) {
         this.addGotoButton(new StoreItem(var1, var2, (Object)null));
      }

      private void addGotoButton(StoreItem var1) {
         if (this.collect) {
            Elem var2 = (Elem)FunctionBodies.gui_info.get_store_collection().get(FunctionBodies.gui_info.getCalcelemindex());
            GoToButton var3 = new GoToButton("Ugrás a mezőre");
            var3.setFieldParams(var2, var1);
            this.list.add(var3);
         }

      }

      public Iterator<GoToButton> iterator() {
         return this.list.iterator();
      }
   }

   static class PageValuesModel implements Comparable {
      private int pageNumber;
      private ArrayList<String> values;
      private String hash;

      public PageValuesModel(int var1) {
         this.pageNumber = var1;
         this.values = new ArrayList();
      }

      public int getPageNumber() {
         return this.pageNumber;
      }

      public void addFieldValue(String var1) {
         this.values.add(var1);
      }

      public FunctionBodies.PageValuesModel emptyModel() {
         this.pageNumber = -1;
         this.hash = "_dummyHashValue_";
         return this;
      }

      public FunctionBodies.PageValuesModel generateHash() {
         StringBuilder var1 = new StringBuilder();
         int var2 = 0;
         Iterator var3 = this.values.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var1.append("#");
            var1.append(var2++);
            var1.append("=");
            var1.append(var4);
         }

         this.hash = var1.toString();
         return this;
      }

      public String getHash() {
         return this.hash;
      }

      public int compareTo(Object var1) {
         return this.hash.compareTo(((FunctionBodies.PageValuesModel)var1).getHash());
      }
   }

   static class Filter {
      private Vector oszlop;
      private int relation;
      private Object value;

      public Filter(Vector var1, int var2, Object var3) {
         this.oszlop = var1;
         this.relation = var2;
         this.value = var3;
      }

      public boolean check(ExpClass var1, int var2, int var3) throws Exception {
         int var4 = this.oszlop.size() == 1 ? 0 : var2;
         ExpClass var5 = FunctionBodies.getFValue((String)this.oszlop.elementAt(var4), var3);
         if (var5 != null && var5.getValue() != null) {
            Object var6 = NumericOperations.processArithmAndLogical(this.relation, var5.getValue(), this.value);
            if (!(var6 instanceof Boolean)) {
               FunctionBodies.writeError(var1, FunctionBodies.ID_ERR_CHECK_COMP, "Összehasonlítási hiba! ", (Exception)null, this.oszlop.elementAt(var4) + ":" + this.value);
               throw new Exception("Intervallum szűrés hiba! ");
            } else {
               return (Boolean)var6;
            }
         } else {
            return false;
         }
      }
   }

   public static class SortedList extends DefaultListModel {
      public void addElement(String var1) {
         int var2 = 0;
         int var3 = this.getSize();

         int var4;
         for(var4 = var2 + var3 >> 1; var3 > var2; var4 = var2 + var3 >> 1) {
            if (var1.compareTo((String)this.getElementAt(var4)) > 0) {
               var2 = var4 + 1;
            } else {
               var3 = var4;
            }
         }

         this.insertElementAt(var1, var4);
      }
   }
}
