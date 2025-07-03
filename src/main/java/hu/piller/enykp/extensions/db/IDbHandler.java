package hu.piller.enykp.extensions.db;

import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IDbConnectQf;
import hu.piller.enykp.util.base.Result;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public interface IDbHandler {
   String DB_ZIPPED_XML_STRUCTURE = "DB_ZIPPED_XML_STRUCTURE";
   String DB_XML_STRUCTURE = "DB_XML_STRUCTURE";
   String DB_RELATION_STRUCTURES = "DB_RELATION_STRUCTURES";
   String ZIP_RES_INPUTSTREAM = "zipInputStream";
   String ZIP_RES_BYTEARRAY = "unzippedData";
   String ZIP_ERRMSG = "errMsg";
   String TYPE_BLOB = "B";
   String BLOB_DOKU = "BX";
   String TYPE_CLOB = "C";
   String LOB_TYPE = "LOB_TYPE";
   String BLOB_S_P_NAME = "BLOL_STORED_PROCEDURE_NAME";
   String BATCH_RECALC = "BATCH_RECALC";
   String JABEV_PARAMS_ARRAY = "JABEV_PARAMS_ARRAY";
   String JABEV_FUNCTIONS_ARRAY = "JABEV_FUNCTIONS_ARRAY";
   String TASK_TYPE = "TASKTYPE";
   String TASK_TYPE_KEY = "GETNEXTTASK";
   String TASK_TYPE_BARCODE = "GETNEXTBARKOD";
   String ORA_RES_OK = "0";
   String ORA_RES_WAIT = "1";
   String ORA_RES_CHECK_END = "2";
   String ORA_RES_UNKNOWN1 = "3";
   String ORA_RES_UNKNOWN2 = "4";
   int INT_ORA_RES_CHECK_END = 2;
   int RESULT_OK = 0;
   int RESULT_2ERRORLOG = 2;
   int EXCEPTION_GENERAL = -1;
   int EXCEPTION_SET_MAIN_HEAD_DATA = -52;
   int EXCEPTION_SET_MAIN_DATA = -53;
   int EXCEPTION_EDIT_DATA_VALUE_OBJECT_DIFFERENT_SIZE = -54;
   int EXCEPTION_EDIT_END = -55;
   int EXCEPTION_NOT_INSTANCE_OF_MODKEYSOBJECT = -56;
   int EXCEPTION_EMPTY_RESULT_SET = -57;
   int EXCEPTION_NOT_INSTANCE_OF_TEMPHEADOBJECT = -58;
   int EXCEPTION_NOT_INSTANCE_OF_TEMPSTOREOBJECT = -59;
   int EXCEPTION_PREPARED_STATEMENT_CREATION = -60;
   int EXCEPTION_SET_EVENT = -61;
   int EXCEPTION_SET_LIFE = -62;
   int EXCEPTION_SET_CHECK_END = -63;
   int EXCEPTION_EDIT_EMPTY_BRSZ_AZON = -64;
   int EXCEPTION_EDIT_EMPTY_BRTS_KOD = -65;
   int EXCEPTION_SAVE_AND_EXIT = -66;
   int EXCEPTION_NO_SUCH_TEMPLATE = -67;
   int EXCEPTION_USER_CANCEL_TEMPLATE = -68;
   int EXCEPTION_LEK_AZON = -69;
   int EXCEPTION_LEK_AZON_TOROL = -70;
   int EXCEPTION_NEW_M_PAGE = -71;
   int EXCEPTION_NO_KEY_DATA_ON_M_PAGE = -72;
   int EXCEPTION_ON_HANDLE_MOD_M_PAGE = -73;
   int EXCEPTION_SAVE_AND_EXIT_ERRORLIST = -74;
   int EXCEPTION_SAVE_WHEN_BATCH_CHECK = -75;
   int JAVITANDO_BIZRESZ = 0;
   int JAVITANDO_BIZRESZ_HIBAS = 1;
   int JAVITANDO_HIBAKODOK = 2;
   int ERRORLIST_MAX_STRING_LENGTH = 254;
   int BASIC_DBNAME_I = 0;
   int BASIC_DBTESZT_I = 1;
   int BASIC_DBDATE_I = 2;
   int BATCH_RECALC_MODE_FOR_GFFDB_CALL = 1;
   int BIZ_GARN_LISTA = 0;
   int BIZ_GARN_TIPUS_LISTA = 1;
   String ORA_RES_KEY_ARRAY_DATA = "a_data";
   String ORA_RES_KEY_CURSOR_DATA = "c_data";
   String ORA_RES_KEY_STATUS = "status";
   String ORA_RES_KEY_MESSAGE = "message";
   String ALAIRASHIBA_KOD = "i001";

   void release();

   InputStream getXMLByStream(Hashtable var1) throws Exception;

   InputStream getXMLByStream_New(Hashtable var1) throws Exception;

   Hashtable getNextTask(Hashtable var1) throws Exception;

   IDbResult getArrayFromDb(String var1, String var2, String[] var3) throws Exception;

   IDbResult getCursorFromDb(String var1, String var2, String[] var3) throws Exception;

   IDbResult getClobFromDb(String var1, String var2, String[] var3) throws Exception;

   void closeStatement() throws Exception;

   int startFieldsFromDb(BigDecimal var1) throws Exception;

   void stopFieldsFromDb(String var1, String var2, String var3, int var4) throws Exception;

   int getFieldsFromDb(IDbConnectQf var1, String var2, BigDecimal[] var3, int var4) throws Exception;

   int saveErrorList(Vector var1, String[] var2);

   int editData(String[] var1, String var2, String var3, String var4, Hashtable var5, Hashtable var6, Vector var7, Vector var8, Hashtable var9) throws Exception;

   String getVersion();

   int setLifeSignal(String[] var1);

   DefaultTableModel getBiztipOsszes(String var1);

   Object getJavitandoBiz(String var1, BigDecimal var2, String var3, String var4, Date var5, Date var6, Date var7, Date var8, String var9, String var10, BigDecimal var11, BigDecimal var12, String var13, String var14, String var15, String var16, String var17, String var18, String var19, String var20, String var21, String var22, String var23, String var24, String var25, String var26, String var27, String var28, BigDecimal var29, BigDecimal var30, String var31, String var32, String var33, String var34, String var35, String var36, String var37, String var38);

   Object getJavitandoBiz(BigDecimal var1);

   IDbInfo getJavitandoBizresz(BigDecimal var1, int var2, String var3, String var4, String var5);

   Hashtable getErrorMessages();

   Hashtable startSaveAndExit(BigDecimal var1, String var2, BigDecimal[] var3);

   String endSaveAndExit(String var1, BigDecimal var2, String var3, String var4);

   IDbInfo ujraErkeztethetoE(BigDecimal var1);

   Hashtable vanEBenne(BigDecimal var1, String[] var2);

   String javithatoE(BigDecimal var1, String var2);

   String elengedhetoE(BigDecimal var1);

   IDbInfo kiertesithetoE(BigDecimal var1);

   String kiertesites(BigDecimal var1, String var2, String var3, String var4);

   String elengedes(BigDecimal var1, String var2, String var3, String var4, String var5);

   int releaseLock(BigDecimal var1);

   Map<String, Object> getFormaOsszes();

   DefaultTableModel getKForma(String var1);

   String handleDelPages(Vector var1);

   void setTestMode();

   String lekAzon(String var1, String var2, String var3);

   String lekAzonTorol(String var1);

   String[] befogadhatoE(BigDecimal var1);

   String befogadva(BigDecimal var1, String var2, String var3);

   String batchEllenorzobe(BigDecimal var1);

   IDbInfo igkodLista(String var1, String var2);

   int login(String var1, String var2, String var3);

   int hibasFutas(String var1, int var2);

   void setVPMessage(String var1);

   Hashtable getSid();

   IDbInfo bizreszDb(BigDecimal var1);

   String getAdozovalJavitva(BigDecimal var1);

   DefaultTableModel getAdozoiCsoport();

   Object getHibakodLista(String var1, BigDecimal var2, String var3, String var4, Date var5, Date var6, Date var7, Date var8, String var9, String var10, BigDecimal var11, BigDecimal var12, String var13, String var14, String var15, String var16, String var17, String var18, String var19, String var20, String var21);

   Object getExcelLista(String var1, BigDecimal var2, String var3, String var4, Date var5, Date var6, Date var7, Date var8, String var9, String var10, BigDecimal var11, BigDecimal var12, String var13, String var14, String var15, String var16, String var17, String var18, String var19, String var20, String var21, String var22, String var23, String var24, String var25, String var26, String var27, BigDecimal var28, BigDecimal var29, String var30, String var31, String var32, String var33, String var34, String var35, String[] var36, String var37);

   void naplozas(String var1);

   Hashtable getHibasanIsKonyvelhetoTable();

   IDbInfo szignalasEll(String var1);

   String szignalas(String var1);

   boolean commitSession(String var1) throws Exception;

   int lekerdezesKilepes();

   String getValidFunkcioKod();

   String getValidEgyszKod();

   String getPrompt(String var1);

   void setDocId(BigDecimal var1, String var2, BigDecimal var3);

   Hashtable getKihatasok();

   Object[] getMegallapitasLista(Hashtable var1, BookModel var2, String var3);

   Vector getAdonemek();

   Object saveKihatas(BookModel var1);

   Object checkKihatas(BookModel var1);

   String barkodEll(String var1);

   String azonEll(String var1);

   String dbName();

   String[] tesztBevallas(String var1, String var2, String var3, String var4, Date var5, Date var6, String var7, String var8);

   String getErkezesiForma();

   Object tortenet(String var1);

   Object getRogzitendoBiz(BigDecimal var1);

   String rogzithetoE(BigDecimal var1, String var2, String var3);

   String rogzitve(String var1, BigDecimal var2, String var3, BigDecimal var4, BigDecimal var5, BigDecimal var6, BigDecimal var7, BigDecimal var8, BigDecimal var9);

   String checkConnection();

   String[] megvaltoztathatatlanKod(BigDecimal var1);

   int editDataR(String[] var1, String var2, String var3, String var4, Hashtable var5, Hashtable var6, Vector var7, Hashtable var8) throws Exception;

   boolean isValidOracleChars(String var1);

   String getLoginName();

   String[] get_kepviselo(String var1);

   Vector get_ugyfelszolg();

   String[] kepviselet_jelleg(String var1);

   String okmanytipus();

   String[] jegyzokonyv_adat_tarol(BigDecimal var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, String var14, String var15, String var16, String var17);

   String kepv_igazolo_okirat();

   String jegyzokonyv_indit(BigDecimal var1);

   String jegyzokonyv_kilep(String var1, BigDecimal var2);

   String[] setRevizoriBevallas(String var1, String var2, String var3, String var4);

   String setBizonylatLezarasUtolagos(String var1);

   String setBizonylatVeglegesitesUtolagos(String var1);

   String aRendszernekAtad(BigDecimal var1);

   String eljarnakAtad(String var1, BigDecimal var2);

   String[] getBizonylatGarnitura(String var1);

   String[] uresBevallas(String var1, String var2);

   String revizoriUtolagosExcel(String var1, String var2, String var3, int var4, int var5, String var6);

   String potrogzitheto(BigDecimal var1, String var2);

   String potrogzitesbe(BigDecimal var1, String var2, String var3);

   String getBasicOraData(int var1);

   DefaultTableModel getBizonylatGarnituraLista(int var1);

   int checkDeletePage(String[] var1);

   DefaultTableModel getBizAltipKod();

   Hashtable pfelCsomag(BigDecimal var1, String var2);

   String revizoriReszutalasJavitva(BigDecimal var1, String var2);

   String hibernalhato(BigDecimal var1);

   String hibernalas(BigDecimal var1, String var2, String var3);

   String getFazis(String var1);

   int getStopSignal();

   void handleExitWithoutSave();

   Object handleNewPage();

   String[] gen17H(String var1, String var2);

   String[] gen17(String var1, String var2);

   String adozoiAdatMasolas17(BigDecimal var1);

   String[] ugyfelszolgalatGet17(String var1);

   String[] getPartnerNev(String var1);

   String getBffsKod();

   String getBevallasTipus();

   String getBevallasFajta();

   String xmlInit(String var1, String var2);

   Hashtable xmlLoad(String var1);

   String xmlSave(Result var1, Vector var2);

   String xmlClose();

   void getTemplateData(BookModel var1);

   String[] checkRole(String var1, String var2);

   String checkIfBEF_LEOGarnitura(BigDecimal var1, BigDecimal var2);

   String bfoLog(String var1, String var2, String var3);

   String[] ugyfelszolgalatGet(BigDecimal var1, String var2);

   String ugyfelszolgalatSet(BigDecimal var1, String var2, String var3);

   int getReszbizonylatSzam(String var1);

   void setGeneratorResult(int var1);

   void setGeneratorMessage(String var1);

   void setGeneratorException(Throwable var1);

   boolean isPapiros();

   boolean isEmptyBimoFidTable();
}
