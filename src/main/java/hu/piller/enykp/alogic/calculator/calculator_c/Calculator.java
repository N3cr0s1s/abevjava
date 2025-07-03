package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.ABEVFunctionSet;
import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.IFunctionSet;
import hu.piller.enykp.alogic.calculator.calculator_c.abev_logger.ABEVLoggerBusiness;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.extensions.elogic.ElogicCaller;
import hu.piller.enykp.extensions.elogic.IELogicResult;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.interfaces.ICalculator;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IHelperLoad;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.EventLog;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

public class Calculator implements ICalculator, IPropertyList, Serializable {
   private static Calculator instance;
   public static final String RES_NAME = "Kalkulátor";
   public static final String VALUE_TRUE = "true";
   public static final String VALUE_FALSE = "false";
   public static final String VALUE_TRUE_POPULATE = "X";
   public static final String VALUE_FALSE_POPULATE = "üres";
   public static final String[] FORMS_WITH_ACCELERATED_DEPENDENCIES = new String[]{"18T1041", "19T1041", "PTGSZLAH", "AATKOD"};
   private static Set acceleratedForms = new HashSet();
   public static final Long ID_DEFAULT_CALCULATOR_ERROR = new Long(4001L);
   public static final Long ERR_ID = new Long(4000L);
   public static final Long ERR_ID_FIELD_CHECK = new Long(4001L);
   private static final Long ERR_ID_FORM_CHECK = new Long(4002L);
   private static final Long ERR_ID_NO_PROPLIST = new Long(4003L);
   private static final Long ERR_ID_NO_DATASTORE = new Long(4004L);
   private static final Long ERR_ID_NO_EXPSTORE = new Long(4005L);
   private static final Long ERR_ID_NO_GUIINFO = new Long(4006L);
   private static final String CMD_GETMATRIXITEM = "get_matrix_item";
   private static final String CMD_GETCALCULATOR = "get_calculator";
   private static final String CMD_GETFIELDTYPE = "get_field_type";
   private static final String CMD_GETEXPSTRUCTURE = "get_exp_structure";
   private static final String CMD_RELEASE = "release";
   private static final String CMD_CALCULATEFIELDDEPENDENCIES = "calculate_field_dependencies";
   private static final String VAL_YES = "igen";
   public static final String ERROR_MESSAGE_BUILD = "Nem sikerült betölteni a számítási adatokat!";
   public static final String ERROR_MESSAGE_BUILD_EXT = "Hiba az UBEV modulban!";
   public static final String ERROR_MESSAGE_READONLY_CALC = "Hiba történt a mező írhatóságának beállításánál! ";
   public static final String DATA_QUERY_METAS = "metas";
   public static final String DATA_QUERY_VARS = "vars";
   public static final String DATA_QUERY_FORMCALCS = "formcalcs";
   public static final String DATA_QUERY_PREGENCALCS = "pregencalcs";
   public static final String DATA_QUERY_POSTGENCALCS = "postgencalcs";
   public static final String DATA_QUERY_BETOLTERTEKCALCS = "betoltertek";
   public static final String DATA_QUERY_PAGECALCS = "pagecalcs";
   public static final String DATA_QUERY_FIELDCALCS = "fieldcalcs";
   public static final String DATA_QUERY_MATRICES = "matrices";
   private ExpStore exp_store = null;
   private Hashtable exp_stores = null;
   private Hashtable checked_map = null;
   private Hashtable calced_map = null;
   private Hashtable<String, Vector> tableShortInvNotFullFields;
   private Hashtable<String, Vector> tableNotInBelFeldFields;
   private Hashtable<String, Vector> tableDPageNumberFields;
   private boolean abev_log;
   private Object abev_log_key = null;
   private boolean time_stat;
   private IDataStore highlighted_datastore;
   boolean create_dependency_file;
   private Object gui_form_object;
   private Hashtable field_list_cache;
   private BookModel gui_info;
   private int lastElogicCallerStatus;
   private static final int CCODE_CHECK_FIELD = 1;
   private static final int CCODE_CHECK_FIELD_VALUE = 2;
   private long check_field_sum;
   private long exp_sum;
   private long form_check_sum;

   public static Calculator getInstance() {
      if (instance == null) {
         instance = new Calculator();
      }

      return instance;
   }

   private Calculator() {
      acceleratedForms.addAll(Arrays.asList(FORMS_WITH_ACCELERATED_DEPENDENCIES));
      this.populateCalculatorCommObj();
      this.initializeAndRegisterInnerFunctionSets((Object)null);
   }

   public void initialize(Object var1) {
      if (var1 instanceof IPropertyList) {
         IPropertyList var2 = (IPropertyList)var1;
         Object var3 = var2.get("prop.dynamic.abevlog");
         if (var3 instanceof Boolean) {
            this.abev_log = (Boolean)var3;
         }

         var3 = var2.get("timeStatistic");
         String var4;
         if (var3 instanceof String) {
            var4 = ((String)var3).trim();
            this.time_stat = var4.equalsIgnoreCase("igen");
         }

         var3 = var2.get("createDependencyFile");
         if (var3 instanceof String) {
            var4 = ((String)var3).trim();
            this.create_dependency_file = var4.equalsIgnoreCase("igen");
         }
      }

   }

   private void initializeAndRegisterInnerFunctionSets(Object var1) {
      ABEVFunctionSet var2 = new ABEVFunctionSet();
      var2.initialize(var1);
      FnFactory.addFunctionBag(var2);
   }

   private void populateCalculatorCommObj() {
      IPropertyList var1 = PropertyList.getInstance();
      if (var1 instanceof IPropertyList) {
         IPropertyList var2 = (IPropertyList)var1;
         if (var2.get("calculator_info") == null) {
            var2.set("calculator_info", this);
         }
      }

   }

   public void release() {
      this.release_(false);
   }

   private void release_(boolean var1) {
      this.tableShortInvNotFullFields = null;
      this.tableNotInBelFeldFields = null;
      this.tableDPageNumberFields = null;
      this.exp_store = null;
      if (this.exp_stores != null) {
         Iterator var2 = this.exp_stores.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            ExpStore var4 = (ExpStore)var3.getValue();
            var4.release();
         }

         this.exp_stores.clear();
      }

      this.exp_stores = null;
      this.field_list_cache = null;
      ABEVLoggerBusiness.release();
      ABEVFunctionSet.getInstance().release();
      if (!var1) {
         ExpFactory.release();
         FIdHelper.release();
         PIdHelper.release();
         CalcFactory.release();
         this.gui_form_object = null;
      }

   }

   public void build(Object var1) throws Exception {
      this.tableShortInvNotFullFields = null;
      this.tableNotInBelFeldFields = null;
      this.tableDPageNumberFields = null;
      boolean var2 = false;
      long var3 = System.currentTimeMillis();
      this.lastElogicCallerStatus = 0;
      if (var2) {
         System.out.println("calculator.build.1 " + (System.currentTimeMillis() - var3));
      }

      ABEVFunctionSet.getInstance().init(this.gui_info);
      DataSource var45 = new DataSource((File)var1);
      if (var45 instanceof IHelperLoad) {
         IHelperLoad var5 = (IHelperLoad)var45;
         var5.read();
         ExpStore var28 = null;
         Hashtable var29 = new Hashtable(1024);
         ExpStoreFactory.calculator = this;
         ExpFactory.calculator = this;
         ExpFactory.gui = this.gui_info;
         ExpFactory.exp_counter = 0;
         this.exp_stores = new Hashtable(16);
         this.field_list_cache = new Hashtable();
         if (var2) {
            System.out.println("calculator.build.2 " + (System.currentTimeMillis() - var3));
         }

         Hashtable var7 = new Hashtable(1);
         var7.put("get_template_helper", "");
         var5.getData(var7);
         var1 = var7.get("get_template_helper");
         if (var2) {
            System.out.println("calculator.build.3 " + (System.currentTimeMillis() - var3));
         }

         if (var1 instanceof IHelperLoad) {
            var5 = (IHelperLoad)var7.get("get_template_helper");
            var7.clear();
            var7.put("get_form_iterator", "");
            var7.put("get_file_name", "");
            var5.getData(var7);
            if (var2) {
               System.out.println("calculator.build.4 " + (System.currentTimeMillis() - var3));
            }

            if ((var1 = var7.get("get_form_iterator")) instanceof Iterator) {
               Iterator var32 = (Iterator)var1;
               String var33 = (String)var7.get("get_file_name");
               boolean var35 = true;

               while(var32.hasNext()) {
                  this.exp_store = null;
                  if (var2) {
                     System.out.println("calculator.build.5 " + (System.currentTimeMillis() - var3));
                  }

                  try {
                     var5 = (IHelperLoad)var32.next();
                     if (var2) {
                        System.out.println("calculator.build.6 " + (System.currentTimeMillis() - var3));
                     }

                     Hashtable var6 = new Hashtable(10);
                     var6.put("metas", "");
                     var6.put("vars", "");
                     var6.put("formcalcs", "");
                     var6.put("pregencalcs", "");
                     var6.put("postgencalcs", "");
                     var6.put("betoltertek", "");
                     var6.put("pagecalcs", "");
                     var6.put("fieldcalcs", "");
                     var6.put("matrices", "");
                     var5.getData(var6);
                     if (var2) {
                        System.out.println("calculator.build.7 " + (System.currentTimeMillis() - var3));
                     }

                     String var34 = this.getStoreId(var5);
                     if (var2) {
                        System.out.println("calculator.build.8 " + (System.currentTimeMillis() - var3));
                     }

                     int var26 = this.getDataItemCount(var6.get("vars"));
                     Variables var8 = new Variables(var26);
                     ExpFactory.form_id = var34;
                     ExpFactory.variables = var8;
                     ExpFactory.functions = FnFactory.createFunctionList();
                     ExpFactory.dependency_map.clear();
                     ExpFactory.page_dependency_map.clear();
                     Object var31 = MetaInfo.getInstance().getFieldMetas(var34);
                     IPropertyListFastStore var14;
                     if (var31 != null) {
                        Vector var36 = new Vector(((Hashtable)var31).values());
                        int var27 = var36.size();
                        var14 = new IPropertyListFastStore(var27);
                        ExpStoreFactory.createFieldTypesStore(var14, (Object)var36);
                     } else {
                        var14 = new IPropertyListFastStore(0);
                     }

                     ExpStoreFactory.createVariableStore(var8, var6.get("vars"));
                     this.notifyFunctionBags5(ExpFactory.variables, var34);
                     this.notifyFunctionBags(var34, this.getDataStore(), var14, ExpFactory.variables);
                     if (var2) {
                        System.out.println("calculator.build.8.1 " + (System.currentTimeMillis() - var3));
                     }

                     boolean var50 = false;
                     Object var39;
                     Object var40;
                     File var41;
                     if (var50) {
                        String var37 = (new File(var33)).getName();
                        Object var38 = this.getProperty("prop.usr.root");
                        var39 = this.getProperty("prop.usr.tmp");
                        var40 = this.getProperty("prop.usr.tmp_calc");
                        var41 = TemplateFileHelper.getTempFile(var38, var39, var40, var37, "." + var34 + ".calc.cf");
                        if (TemplateFileHelper.isPreparedTemplateExist(var41)) {
                           Vector var42 = new Vector(3);
                           TemplateFileHelper.loadFromFile(var41, var42);
                           CalcFactory.setLoadedObject(var42);
                           CalcFactory.setToUsingByDiscover();
                        } else {
                           CalcFactory.processFormCalculations(var6.get("formcalcs"));
                           CalcFactory.processFormCalculations(var6.get("pregencalcs"));
                           CalcFactory.processFormCalculations(var6.get("postgencalcs"));
                           CalcFactory.processFormCalculations(var6.get("betoltertek"));
                           CalcFactory.processPageCalculations(var6.get("pagecalcs"));
                           CalcFactory.processFieldCalculations(var6.get("fieldcalcs"));
                           CalcFactory.normalize();
                           CalcFactory.setToSerialization();
                           TemplateFileHelper.saveToFile(var41, CalcFactory.getSavableObject());
                           CalcFactory.setToUsing();
                        }
                     } else {
                        CalcFactory.processFormCalculations(var6.get("formcalcs"));
                        CalcFactory.processFormCalculations(var6.get("pregencalcs"));
                        CalcFactory.processFormCalculations(var6.get("postgencalcs"));
                        CalcFactory.processFormCalculations(var6.get("betoltertek"));
                        CalcFactory.processPageCalculations(var6.get("pagecalcs"));
                        CalcFactory.processFieldCalculations(var6.get("fieldcalcs"));
                        CalcFactory.normalize();
                     }

                     if (var2) {
                        System.out.println("calculator.build.9 " + (System.currentTimeMillis() - var3));
                     }

                     Object[] var18 = new Object[this.getDataItemCount(var6.get("matrices"))];
                     var26 = CalcFactory.getCollectionSize(CalcFactory.getEventsCollection("form_check"));
                     Object[] var19 = new Object[var26];
                     var26 = CalcFactory.getCollectionSize(CalcFactory.getEventsCollection("form_calc"));
                     Object[] var17 = new Object[var26];
                     var26 = CalcFactory.getCollectionSize(CalcFactory.getEventsCollection("pre_gen_calc"));
                     Object[] var21 = new Object[var26];
                     var26 = CalcFactory.getCollectionSize(CalcFactory.getEventsCollection("post_gen_calc"));
                     Object[] var22 = new Object[var26];
                     var26 = CalcFactory.getCollectionSize(CalcFactory.getEventsCollection("betolt_ertek"));
                     Object[] var23 = new Object[var26];
                     var26 = CalcFactory.getCollectionSize(CalcFactory.getEventsCollection("page_check"));
                     IPropertyListFastStore var9 = new IPropertyListFastStore(var26);
                     var26 = CalcFactory.getCollectionSize(CalcFactory.getEventsCollection("field_calc"));
                     IPropertyListFastStore var10 = new IPropertyListFastStore(var26);
                     ExpStoreFactory.reset();
                     Object[] var25 = null;
                     Object[] var20 = null;
                     Object[] var24 = null;
                     var26 = CalcFactory.getCollectionSize(CalcFactory.getEventsCollection("field_check"));
                     IPropertyListFastStore var11 = new IPropertyListFastStore(var26);
                     var26 = CalcFactory.getCollectionSize(CalcFactory.getEventsCollection("lookup_create"));
                     IPropertyListFastStore var15 = new IPropertyListFastStore(var26);
                     if (var2) {
                        System.out.println("calculator.build.10 " + (System.currentTimeMillis() - var3));
                     }

                     Hashtable var16 = this.gui_info.getMetas(var34);
                     if (var2) {
                        System.out.println("calculator.build.10.1 " + (System.currentTimeMillis() - var3));
                     }

                     ExpStoreFactory.createMatricesStore(var18, var6.get("matrices"));
                     if (var2) {
                        System.out.println("calculator.build.10.2 " + (System.currentTimeMillis() - var3));
                     }

                     ExpStoreFactory.createFormCalculationStore(var17, CalcFactory.getEventsCollection("form_calc"));
                     if (var2) {
                        System.out.println("calculator.build.10.3 " + (System.currentTimeMillis() - var3));
                     }

                     ExpStoreFactory.createFormCheckStore(var19, CalcFactory.getEventsCollection("form_check"));
                     if (var2) {
                        System.out.println("calculator.build.10.4 " + (System.currentTimeMillis() - var3));
                     }

                     ExpStoreFactory.createFormCheckStore(var21, CalcFactory.getEventsCollection("pre_gen_calc"));
                     if (var2) {
                        System.out.println("calculator.build.10.4.1 " + (System.currentTimeMillis() - var3));
                     }

                     ExpStoreFactory.createFormCheckStore(var22, CalcFactory.getEventsCollection("post_gen_calc"));
                     if (var2) {
                        System.out.println("calculator.build.10.4.1 " + (System.currentTimeMillis() - var3));
                     }

                     ExpStoreFactory.createFormCheckStore(var23, CalcFactory.getEventsCollection("betolt_ertek"));
                     if (var2) {
                        System.out.println("calculator.build.10.4.1 " + (System.currentTimeMillis() - var3));
                     }

                     Object var51 = CalcFactory.getEventsCollection("page_check");
                     ExpStoreFactory.createPageCheckStore(var9, var51, CalcFactory.getCacheMaps());
                     if (var2) {
                        System.out.println("calculator.build.10.5 " + (System.currentTimeMillis() - var3));
                     }

                     var51 = CalcFactory.getEventsCollection("field_calc");
                     if (var2) {
                        System.out.println("calculator.build.10.5.1 " + (System.currentTimeMillis() - var3));
                     }

                     ExpStoreFactory.createFieldCalculationStore(var10, var51, CalcFactory.getCacheMaps());
                     if (var2) {
                        System.out.println("calculator.build.10.5.2 " + (System.currentTimeMillis() - var3));
                     }

                     var51 = CalcFactory.getEventsCollection("field_check");
                     if (var2) {
                        System.out.println("calculator.build.10.5.3 " + (System.currentTimeMillis() - var3));
                     }

                     ExpStoreFactory.createFieldCheckStore(var11, var51, CalcFactory.getCacheMaps());
                     if (var2) {
                        System.out.println("calculator.build.10.6 " + (System.currentTimeMillis() - var3));
                     }

                     var51 = CalcFactory.getEventsCollection("lookup_create");
                     ExpStoreFactory.createLookupCreateStore(var15, var51, CalcFactory.getCacheMaps());
                     if (var2) {
                        System.out.println("calculator.build.10.6.1 " + (System.currentTimeMillis() - var3));
                     }

                     var20 = ExpStoreFactory.createCalcualatableFieldStore(var20);
                     var24 = ExpStoreFactory.createCheckableFieldStore(var24);
                     var25 = ExpStoreFactory.createFieldStore(var25);
                     if (var2) {
                        System.out.println("calculator.build.10.7 " + (System.currentTimeMillis() - var3));
                     }

                     ExpStoreFactory.extendSpecialDependecies(ExpFactory.dependency_map, ABEVFunctionSet.getInstance().getSpecialDependencies(var34));
                     IPropertyList var12 = ExpStoreFactory.createFieldDependencies((IPropertyList)null, var29);
                     Object[] var30 = ExpStoreFactory.createFullFieldCalcOrder(var29, var20);
                     if (var2) {
                        System.out.println("calculator.build.10.8 " + (System.currentTimeMillis() - var3));
                     }

                     IPropertyList var13 = ExpStoreFactory.createPageDependencies((IPropertyList)null, (Hashtable)null);
                     if (var2) {
                        System.out.println("calculator.build.11 " + (System.currentTimeMillis() - var3));
                     }

                     this.exp_store = new ExpStore();
                     this.exp_store.setFieldMetas(var16);
                     this.exp_store.setMatrices(var18);
                     this.exp_store.setVariables(var8);
                     this.exp_store.setFunctions(ExpFactory.functions);
                     this.exp_store.setFormCalculations(var17);
                     this.exp_store.setFormChecks(var19);
                     this.exp_store.setPreGenCalcs(var21);
                     this.exp_store.setPostGenCalcs(var22);
                     this.exp_store.setBetoltErtekCalcs(var23);
                     this.exp_store.setPageChecks(var9);
                     this.exp_store.setFieldCalculations(var10);
                     this.exp_store.setFieldChecks(var11);
                     this.exp_store.setFieldLookupCreates(var15);
                     this.exp_store.setCalculatableFields(var20);
                     this.exp_store.setCheckableFields(var24);
                     this.exp_store.setFields(var25);
                     this.exp_store.setFieldDependencies(var12);
                     this.exp_store.setPageDependencies(var13);
                     this.exp_store.setAllCalculation(CalcFactory.getFullCollection());
                     this.exp_store.setCacheMaps(CalcFactory.getCacheMaps());
                     this.exp_store.setFieldCalculationFactors(var29);
                     this.exp_store.setFullFieldCalcOrder(var30);
                     this.exp_store.setFieldTypes(var14);
                     if (var35 && var28 == null) {
                        var28 = this.exp_store;
                     }

                     var35 = false;
                     this.exp_stores.put(this.getStoreId(var5), this.exp_store);
                     if (var2) {
                        System.out.println("calculator.build.12 " + (System.currentTimeMillis() - var3));
                     }

                     if (this.create_dependency_file) {
                        String var52 = (new File(var33)).getName();
                        var39 = this.getProperty("prop.usr.root");
                        var40 = this.getProperty("prop.usr.saves");
                        var41 = DependencyListFileHelper.getDependencyFile(var39, var40, var52, "." + var34 + ".dep.txt");
                        DependencyListFileHelper.saveToFile(var41, var12);
                     }

                     CalcFactory.release();
                     ExpFactory.release_(true);
                  } catch (Exception var44) {
                     CalcFactory.release();
                     ExpFactory.release_(true);
                     var44.printStackTrace();
                     if (this.exp_store != null) {
                        this.exp_store.release();
                        this.exp_store = null;
                     }

                     this.writeLog(var44);
                     this.writeError("Nem sikerült betölteni a számítási adatokat!", var44);
                     throw new Exception("Nem sikerült betölteni a számítási adatokat! " + var44.getMessage());
                  }
               }
            }

            this.exp_store = var28;
         }
      }

      if (this.exp_store != null) {
         int var46;
         String var47;
         try {
            IELogicResult var48 = ElogicCaller.exec("nyomtatvany_parameterek_betoltese_utan", this.gui_info);
            var46 = var48.getStatus();
            var47 = var48.getMessage();
         } catch (Exception var43) {
            var46 = -1;
            var47 = var43.getMessage();
         }

         this.lastElogicCallerStatus = var46;
         if (var46 != 0) {
            if (this.exp_store != null) {
               this.exp_store.release();
               this.exp_store = null;
            }

            String var49 = "Nem sikerült betölteni a számítási adatokat! Hiba az UBEV modulban! " + var46 + ", " + var47;
            this.writeLog(var49);
            this.writeError(var49, (Exception)null);
            throw new Exception(var49);
         }
      }

      if (var2) {
         System.out.println("calculator.build.14 " + (System.currentTimeMillis() - var3));
      }

   }

   public void setBookModel(BookModel var1) {
      this.gui_info = var1;
      FIdHelper.setInfoObject(this.gui_info);
      PIdHelper.setInfoObject(this.gui_info);
   }

   public BookModel getBookModel() {
      return this.gui_info;
   }

   public boolean set(Object var1, Object var2) {
      if (var1 instanceof String) {
         String var3 = (String)var1;
         if (var3.equalsIgnoreCase("release")) {
            this.release();
         } else if (var3.equalsIgnoreCase("calculate_field_dependencies")) {
            this.calculateFieldDependencies(var2);
         }
      }

      return false;
   }

   public Object get(Object var1) {
      if (var1 instanceof Object[]) {
         Object[] var8 = (Object[])((Object[])var1);
         if (var8[0] instanceof String) {
            String var3 = (String)var8[0];
            if (var8.length > 2) {
               if (var3.equalsIgnoreCase("get_matrix_item") && this.exp_store != null && var8[1] instanceof String && (var8[2] == null || var8[2] instanceof int[])) {
                  return this.exp_store.getMatrixItem(var8[1], (int[])((int[])var8[2]));
               }
            } else if (var8.length > 1) {
               if (var3.equalsIgnoreCase("get_matrix_item")) {
                  if (this.exp_store != null && (var8[1] instanceof int[] || var8[1] instanceof String)) {
                     return this.exp_store.getMatrixItem(var8[1], (int[])null);
                  }
               } else if (var3.equalsIgnoreCase("get_field_type")) {
                  if (this.exp_store != null && var8[1] != null) {
                     Object var4 = this.exp_store.getMetas(var8[1]);
                     if (var4 instanceof Hashtable) {
                        var4 = ((Hashtable)var4).get("type");
                        if (var4 != null) {
                           try {
                              return Integer.valueOf(var4.toString());
                           } catch (Exception var7) {
                              Tools.eLog(var7, 1);
                           }
                        }
                     }
                  }
               } else if (var3.equalsIgnoreCase("get_exp_structure")) {
                  try {
                     return ExpFactory.createExp(var8[1] == null ? null : var8[1].toString(), (String)null, -1, false, (String)null);
                  } catch (Exception var6) {
                     Tools.eLog(var6, 1);
                  }
               }
            } else if (var8.length > 0 && var3.equalsIgnoreCase("get_calculator")) {
               return this;
            }
         }
      } else if (var1 instanceof String) {
         String var2 = (String)var1;
         if (var2.equalsIgnoreCase("get_calculator")) {
            return this;
         }
      }

      return null;
   }

   public Object get_matrix_item(Object var1) {
      Object[] var2 = (Object[])((Object[])var1);
      return this.exp_store.getMatrixItem(var2[1], (int[])((int[])var2[2]));
   }

   public ExpStore getExpStore(String var1) {
      return this.exp_stores != null ? (ExpStore)this.exp_stores.get(var1) : null;
   }

   private void calculateFieldDependencies(Object var1) {
      if (var1 != null) {
         if (this.gui_info != null) {
            if (this.exp_store != null) {
               IDataStore var4;
               if (this.highlighted_datastore == null) {
                  var4 = this.getDataStore();
               } else {
                  var4 = this.highlighted_datastore;
               }

               if (var4 != null) {
                  try {
                     Object var5 = FIdHelper.createId((Object)null, (Object)null, (Object)null);
                     FIdHelper.setFieldId(var5, var1.toString());
                     int var6 = 0;

                     for(int var7 = FIdHelper.getPageCount(var5); var6 < var7; ++var6) {
                        FIdHelper.setDPageNumber(var5, var6);
                        ExpWrapper var3 = new ExpWrapper();
                        Object[] var2 = (Object[])((Object[])this.exp_store.getFieldDependencies(FIdHelper.getFieldId(var5)));
                        if (var2 != null) {
                           int var8 = 0;

                           for(int var9 = var2.length; var8 < var9; ++var8) {
                              this.calculateField(var4, var3, var5, var2[var8]);
                           }
                        }
                     }
                  } catch (Exception var10) {
                     var10.printStackTrace();
                     this.writeError("Hiba történt függő mező számítás közben !", var10);
                  }
               } else {
                  this.writeError(ERR_ID_NO_DATASTORE, "Adat tár nem létezik, művelet nem hajtható végre ! (Mező számítás, " + var1 + ")", (Exception)null, (Object)null);
               }
            } else {
               this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Mező számítás, " + var1 + ")", IErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null);
            }
         } else {
            this.writeError(ERR_ID_NO_GUIINFO, "Felület információ nem létezik, művelet nem hajtható végre ! (Mező számítás, " + var1 + ")", (Exception)null, (Object)null);
         }

      }
   }

   private void calculateDependentFields(List<String> var1) {
      if (var1 != null) {
         if (this.gui_info != null) {
            if (this.exp_store != null) {
               IDataStore var3;
               if (this.highlighted_datastore == null) {
                  var3 = this.getDataStore();
               } else {
                  var3 = this.highlighted_datastore;
               }

               if (var3 != null) {
                  try {
                     Iterator var4 = var1.iterator();

                     while(true) {
                        ExpWrapper var2;
                        Object[] var6;
                        do {
                           if (!var4.hasNext()) {
                              return;
                           }

                           String var5 = (String)var4.next();
                           var2 = new ExpWrapper();
                           var6 = (Object[])((Object[])this.exp_store.getFieldDependencies(var5));
                        } while(var6 == null);

                        Object var7 = FIdHelper.createId((Object)null, (Object)null, (Object)null);
                        Object[] var8 = var6;
                        int var9 = var6.length;

                        for(int var10 = 0; var10 < var9; ++var10) {
                           Object var11 = var8[var10];
                           if (this.exp_store.getVariable(var11) == null) {
                              FIdHelper.setFieldId(var7, var11);
                              int var12 = FIdHelper.getPageCount(var7);

                              for(int var13 = 0; var13 < var12; ++var13) {
                                 FIdHelper.setDPageNumber(var7, var13);
                                 this.calculateField(var3, var2, var7, var11);
                              }
                           }
                        }
                     }
                  } catch (Exception var14) {
                     var14.printStackTrace();
                     this.writeError("Hiba történt függő mező számítás közben !", var14);
                  }
               } else {
                  this.writeError(ERR_ID_NO_DATASTORE, "Adat tár nem létezik, művelet nem hajtható végre ! (Mező számítás, calculateDependentFields/betolt_érték)", (Exception)null, (Object)null);
               }
            } else {
               this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Mező számítás, calculateDependentFields/betolt_érték)", IErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null);
            }
         } else {
            this.writeError(ERR_ID_NO_GUIINFO, "Felület információ nem létezik, művelet nem hajtható végre ! (Mező számítás, calculateDependentFields/betolt_érték)", (Exception)null, (Object)null);
         }

      }
   }

   private String getStoreId(IHelperLoad var1) {
      Hashtable var2 = new Hashtable(1);
      var2.put("form", "");
      var1.getData(var2);
      var2 = ExpStoreFactory.getAttributes(var2.get("form"));
      return (String)var2.get("id");
   }

   public ExpClass getExpressionDefinition(String var1) {
      if (this.exp_store != null) {
         Object var2 = this.exp_store.getFieldCalulation(var1);
         if (var2 != null && var2 instanceof Object[]) {
            Object[] var3 = (Object[])((Object[])var2);
            Object[] var4 = (Object[])((Object[])var3[0]);
            ExpClass var5 = CalcHelper.getExp(var4);
            return var5;
         }
      } else {
         this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Mező érték elkérés, " + FIdHelper.getFieldId(var1) + ")", IErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null);
      }

      this.writeLog("fieldGetFieldValue");
      return null;
   }

   public String fieldGetFieldValue(Object var1) {
      return null;
   }

   public void fieldCheck(Object var1, StoreItem var2) {
      if (this.gui_info != null) {
         if (this.exp_store != null) {
            try {
               this.notifyFunctionBagsPreviousItem(var2);
               this.fieldCheck_(var1, this.exp_store, this.getDataStore(), false, new ExpWrapper(), (Hashtable)null, this.abev_log, ExpFactory.form_id, this.gui_form_object, false, 3, (Hashtable)null);
            } finally {
               this.notifyFunctionBagsPreviousItem((StoreItem)null);
            }
         } else {
            this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Mező ellenőrzés, " + FIdHelper.getFieldId(var1) + ")", IErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null);
         }
      } else {
         this.writeError(ERR_ID_NO_GUIINFO, "Felület információ nem létezik, művelet nem hajtható végre ! (Mező ellenőrzés, " + FIdHelper.getFieldId(var1) + ")", (Exception)null, (Object)null);
      }

   }

   private void fieldsCheck_(Object[] var1, Hashtable var2, Integer2 var3, Object var4, ExpStore var5, IDataStore var6, boolean var7, ExpWrapper var8, Hashtable var9, boolean var10, Object var11, Object var12, boolean var13, int var14, Hashtable var15) {
      if (var1 != null && var2 != null) {
         int var18 = 0;

         for(int var19 = var1.length; var18 < var19; ++var18) {
            Object var16 = var2.get(var1[var18]);
            if (var16 != null) {
               FIdHelper.setFieldId(var4, var1[var18]);
               this.fieldCheck_(var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15);
               Object var17 = FIdHelper.getReturnValue(var4);
               if (var17 instanceof Boolean && !(Boolean)var17) {
                  ++var3.i;
               }
            }
         }
      }

   }

   private void fieldCheck_(Object var1, ExpStore var2, IDataStore var3, boolean var4, ExpWrapper var5, Hashtable var6, boolean var7, Object var8, Object var9, boolean var10, int var11, Hashtable var12) {
      if (var3 == null) {
         var3 = this.getDataStore();
      }

      if (var5 == null) {
         var5 = new ExpWrapper();
      }

      try {
         int var13;
         int var14;
         if (var4) {
            var13 = 0;
            var14 = FIdHelper.getPageCount(var1);
         } else {
            var13 = FIdHelper.getDPageNumber(var1);
            var14 = var13 + 1;
         }

         for(int var15 = var13; var15 < var14; ++var15) {
            FIdHelper.setDPageNumber(var1, var15);
            FIdHelper.setReturnValue(var1, Boolean.TRUE);
            this.checkField(var1, var5, FIdHelper.getFieldId(var1), var2, var6, var7, var8, var9, var10, var3, var11, var12);
         }
      } catch (Exception var16) {
         var16.printStackTrace();
         this.writeError("Hiba történt mező ellenőrzés közben !", var16);
      }

   }

   private boolean checkField(Object var1, ExpWrapper var2, Object var3, ExpStore var4, Hashtable var5, boolean var6, Object var7, Object var8, boolean var9, IDataStore var10, int var11, Hashtable var12) {
      boolean var13 = true;
      if ((var11 & 1) == 1) {
         Object var14 = var4.getFieldChecks(var3);
         if (var14 instanceof Object[]) {
            Object var16 = FIdHelper.getReturnValue(var1);
            Object[] var17 = (Object[])((Object[])var14);
            if (var16 instanceof Boolean && !(Boolean)var16) {
               return !(Boolean)var16;
            }

            int var21 = 0;

            for(int var22 = var17.length; var21 < var22; ++var21) {
               Object var18 = var17[var21];
               if (var6) {
                  ABEVLoggerBusiness.clear();
                  ABEVLoggerBusiness.setExpressionType("mezőellenőrzés");
                  ABEVLoggerBusiness.setExpressionIdByFId(CalcHelper.getId(var18), FIdHelper.getFieldId(var1), this.getBookModel());
                  ABEVLoggerBusiness.setExpression(CalcHelper.getExp(var18));
                  ABEVLoggerBusiness.setCIDCode(FIdHelper.getFieldId(var1), FIdHelper.getDPageNumber(var1));
                  ABEVLoggerBusiness.setErrorMessageType(CalcHelper.getErrorLevel(var18));
                  ABEVLoggerBusiness.setErrorMessage(CalcHelper.getMsg(var18));
               }

               String var20 = CalcHelper.getTargetId(var18);
               if (var5 == null || var20 == null || var5.get(var20) == null) {
                  Object var15 = this.calculateExp(var1, var18, var2, CalcHelper.getExp(var18), (String)var7, var10);
                  if (var6) {
                     ABEVLoggerBusiness.setExpressionResult(var15);
                  }

                  Boolean var23 = CalcHelper.getTargetBind(var18);
                  boolean var19 = var23 == null || var23;
                  if (var15 != null && var19) {
                     if (var15 instanceof ExpClass) {
                        var15 = ((ExpClass)var15).getResult();
                     }

                     if (var15 instanceof Boolean) {
                        if (!(Boolean)var15) {
                           FIdHelper.setDataStoreId(var1, var10.getMasterCaseId(FIdHelper.getDataStoreKey(var1)));
                           Integer var24 = this.getDpageNumber(var1);
                           String var25 = CalcHelper.getErrorCode(var18);
                           if (!this.isAdozoRole() && (var25 == null || ((String)var25).length() <= 0)) {
                              this.writeFieldCheckError(var1, this.extendMsg(var18, var24), IErrorList.LEVEL_WARNING, var7, var8, CalcHelper.getErrorCode(var18), this.standAloneExtendedError(var18));
                           } else {
                              this.writeFieldCheckError(var1, this.extendMsg(var18, var24), this.getInsideErrorLevel(var18), var7, var8, CalcHelper.getErrorCode(var18), this.standAloneExtendedError(var18));
                           }

                           FIdHelper.setReturnValue(var1, Boolean.FALSE);
                           if (var5 != null && var20 != null) {
                              var5.put(var20, "");
                           }

                           if (var6) {
                              ABEVLoggerBusiness.write();
                           }

                           var13 = false;
                           break;
                        }
                     } else if (!(var15 instanceof LookupListModel)) {
                        this.writeLog("Mező ellenőrzési értéke nem logikai érték lett ! (" + FIdHelper.getFieldId(var1) + ")");
                     }
                  }

                  if (var6) {
                     ABEVLoggerBusiness.write();
                  }
               }
            }
         }
      }

      if ((var11 & 2) == 2) {
         this.checkFieldValue(var9, var10, var3, var1, var2, var7, var4, var12);
      }

      if (var13) {
         FIdHelper.setReturnValue(var1, Boolean.TRUE);
      }

      return var13;
   }

   public void feltetelesErtekPreCheck() {
      Integer2 var1 = new Integer2(0);
      if (this.gui_info != null) {
         if (this.exp_store != null) {
            boolean var2 = this.abev_log;
            String var3 = ExpFactory.form_id;
            IDataStore var4 = this.getDataStore();
            this.notifyFunctionBags6(var3, var4);
            this.notifyFunctionBags7();

            try {
               ExpWrapper var6 = new ExpWrapper();
               Hashtable var7 = this.gui_info.get_enabled_fields((Elem)this.gui_info.get_store_collection().get(this.gui_info.getCalcelemindex()));
               Object var8 = null;
               Object var10 = this.gui_form_object;
               boolean var11 = false;
               Hashtable var9 = this.gui_info.get(this.gui_info.getCalcelemindex()).get_short_inv_fields_ht();
               Object[] var5 = (Object[])((Object[])FIdHelper.createId((Object)null, (Object)null, (Object)null));
               HashSet var12 = ABEVFunctionSet.getInstance().getFeltetelesErtekFieldsList(var3);
               if (var12 != null) {
                  this.fieldsCheck_(var12.toArray(), var7, var1, var5, this.exp_store, var4, true, var6, (Hashtable)var8, var2, var3, var10, var11, 1, var9);
               }

               this.notifyFunctionBagsFormCheck(false);
            } catch (Exception var16) {
               System.out.println("Calculator.formCheck error");
               System.out.println("Hiba történt nyomtatvány ellenőrzés közben !");
               var16.printStackTrace();
               this.notifyFunctionBagsFormCheck(false);
               this.writeError("Hiba történt nyomtatvány ellenőrzés közben !", var16);
               ++var1.i;
            } finally {
               this.checked_map = null;
               this.highlighted_datastore = null;
               this.notifyFunctionBagsFormCheck(false);
            }
         } else {
            this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Nyomtatvány ellenőrzés)", IErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null);
            ++var1.i;
         }
      } else {
         this.writeError(ERR_ID_NO_GUIINFO, "Felület információ nem létezik, művelet nem hajtható végre ! (Nyomtatvány ellenőrzés)", (Exception)null, (Object)null);
         ++var1.i;
      }

      this.notifyFunctionBags10();
      this.notifyFunctionBagsFormCheck(false);
   }

   public LookupListModel getFieldCreateLookup(Object var1) {
      if (this.gui_info != null) {
         if (this.exp_store != null) {
            Object var2 = null;
            Object var3 = this.exp_store.getFieldLookupCreate(FIdHelper.getFieldId(var1));
            if (var3 instanceof Object[]) {
               Object[] var4 = (Object[])((Object[])var3);
               var2 = var4[0];
            }

            Object var5 = this.calculateExp(var1, var2, new ExpWrapper(), CalcHelper.getExp(var2), ExpFactory.form_id, this.getDataStore());
            if (var5 != null && var5 instanceof LookupListModel) {
               return (LookupListModel)var5;
            }

            return null;
         }

         this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Mező ellenőrzés, " + FIdHelper.getFieldId(var1) + ")", IErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null);
      } else {
         this.writeError(ERR_ID_NO_GUIINFO, "Felület információ nem létezik, művelet nem hajtható végre ! (Mező ellenőrzés, " + FIdHelper.getFieldId(var1) + ")", (Exception)null, (Object)null);
      }

      return null;
   }

   private Integer getDpageNumber(Object var1) {
      try {
         return FIdHelper.getPageCount(var1) > 1 ? FIdHelper.getDPageNumber(var1) : null;
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }

   private void checkFieldValue(boolean var1, IDataStore var2, Object var3, Object var4, ExpWrapper var5, Object var6, ExpStore var7, Hashtable var8) {
      if (!this.isBatchOpMode() && !CalculatorManager.xml || !this.getShortInvNotFullFields(ExpFactory.form_id).contains(var3)) {
         if (!this.isPartMulti() || !ABEVFunctionSet.getInstance().isCachedTargetId(var6.toString(), "globsum", (String)var3)) {
            Object var9;
            if (var1 && var2 != null && (var9 = var7.getFieldCalulation(var3)) != null) {
               try {
                  var9 = ((Object[])((Object[])var9))[0];
                  Boolean var13 = CalcHelper.getTargetBind(var9);
                  boolean var14 = !(var13 instanceof Boolean) || (Boolean)var13;
                  if (var14) {
                     try {
                        Object var15 = FIdHelper.getDataStoreKey(var4);

                        int var12;
                        try {
                           var9 = var7.getFieldTypes().get(var3);
                           var12 = var9 == null ? -1 : Integer.parseInt(var9.toString());
                        } catch (NumberFormatException var26) {
                           var12 = -1;
                        }

                        String var10 = var2.get(var15);
                        String var11 = this.calculateField_(var2, var5, var4, var3, false);
                        if (var12 == 4) {
                           if (var10 == null) {
                              var10 = "false";
                           } else if (var10.trim().length() == 0) {
                              var10 = "false";
                           }

                           if (var11 == null) {
                              var11 = "false";
                           } else if (var11.trim().length() == 0) {
                              var11 = "false";
                           }
                        }

                        var10 = var10 == null ? "" : var10;
                        var11 = var11 == null ? "" : var11;
                        if (!var10.trim().equals(var11.trim())) {
                           String var16 = FIdHelper.getFieldId(var4);
                           boolean var17;
                           if (var8 == null) {
                              var17 = true;
                           } else {
                              var17 = var8.get(var16) == null;
                           }

                           if (var17) {
                              var10 = var10.length() == 0 ? "(Üres)" : var10;
                              var11 = var11.length() == 0 ? "(Üres)" : var11;
                              FIdHelper.setDataStoreId(var4, var2.getMasterCaseId(FIdHelper.getDataStoreKey(var4)));
                              Object[] var18 = (Object[])((Object[])var7.getFieldCalulation(var3));
                              Object var19 = var18[0];
                              String var20 = CalcHelper.getErrorCode(var19);
                              String var21 = CalcHelper.getMsg(var19);
                              var10 = this.populateBoolean(var10);
                              var11 = this.populateBoolean(var11);
                              String var22 = "A program szerint a helyes érték \"" + var11 + "\". A mezőben lévő érték \"" + var10 + "\"." + " Szükség esetén futtassa a Számított mezők újraszámítása menüpontot. (" + var16 + ")";
                              if (var21 != null && var21.length() != 0) {
                                 var21 = var21 + ", " + var22;
                              } else {
                                 var21 = var22;
                              }

                              Integer var23 = this.getDpageNumber(var4);
                              if (!this.isAdozoRole() && (var10.equals("0") && var11.equals("(Üres)") || var10.equals("(Üres)") && var11.equals("0"))) {
                                 return;
                              }

                              if (ABEVFunctionSet.getInstance().isToleranced(var6.toString(), var16, var10, var11)) {
                                 return;
                              }

                              Integer var24 = IErrorList.LEVEL_ERROR;
                              Integer var25 = CalcHelper.getErrorLevel(var19);
                              if (var25 != null) {
                                 var24 = new Integer(var25.toString());
                              }

                              if (!this.isAdozoRole() && (var20 == null || ((String)var20).length() <= 0)) {
                                 this.writeFieldCheckError(var4, this.extendMsg(var21, var19, var23), IErrorList.LEVEL_WARNING, var6, this.gui_form_object, var20, this.standAloneExtendedError(var19));
                              } else {
                                 this.writeFieldCheckError(var4, this.extendMsg(var21, var19, var23), var24, var6, this.gui_form_object, var20, this.standAloneExtendedError(var19));
                              }
                           } else {
                              this.writeDataStore(var2, var15, var11);
                           }
                        }
                     } catch (Exception var27) {
                        Tools.eLog(var27, 1);
                     }
                  }
               } catch (Exception var28) {
                  Tools.eLog(var28, 1);
               }
            }

         }
      }
   }

   private String populateBoolean(String var1) {
      if (var1.equalsIgnoreCase("true")) {
         return "X";
      } else {
         return var1.equalsIgnoreCase("false") ? "üres" : var1;
      }
   }

   public void fieldDoCalculations(Object var1) {
      this.fieldDoSpecializedCalculations(var1, false);
   }

   public void fieldDoDependentCalculations(Object var1) {
      this.fieldDoSpecializedCalculations(var1, true);
   }

   private void fieldDoSpecializedCalculations(Object var1, boolean var2) {
      FIdHelper.setReturnValue(var1, Boolean.FALSE);
      if (this.gui_info != null) {
         if (this.exp_store != null) {
            IDataStore var4;
            if (this.highlighted_datastore == null) {
               var4 = this.getDataStore();
            } else {
               var4 = this.highlighted_datastore;
            }

            if (var4 != null) {
               try {
                  var4.beginTransaction();
                  int var5 = FIdHelper.getPageCount(var1);
                  ExpWrapper var3 = new ExpWrapper();

                  for(int var6 = 0; var6 < var5; ++var6) {
                     FIdHelper.setDPageNumber(var1, var6);
                     if (FIdHelper.getDontCalc(var1) == null) {
                        if (var2) {
                           if (CalcFactory.isFieldOnEvent(this.exp_store, FIdHelper.getFieldId(var1), "on_event", "multi_form_load")) {
                              this.fireCalculations(new Object[]{new String[]{"on_event"}, new String[]{"multi_form_load"}});
                           }
                        } else {
                           this.calculateField(var4, var3, var1, FIdHelper.getFieldId(var1));
                        }

                        this.checkDependentPagesOnField(var1);
                     }

                     this.calculateDependencies(var1, this.getFormid(), var4, var6, var3);
                     this.checkDependentPagesOnFDeps(var1);
                  }

                  var4.commitTransaction();
                  FIdHelper.setReturnValue(var1, Boolean.TRUE);
               } catch (Exception var7) {
                  var7.printStackTrace();
                  this.writeError("Hiba történt mező számítás közben !", var7);
                  var4.rollbackTransaction();
               }
            } else {
               this.writeError(ERR_ID_NO_DATASTORE, "Adat tár nem létezik, művelet nem hajtható végre ! (Mező számítás, " + FIdHelper.getFieldId(var1) + ")", (Exception)null, (Object)null);
            }
         } else {
            this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Mező számítás, " + FIdHelper.getFieldId(var1) + ")", IErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null);
         }
      } else {
         this.writeError(ERR_ID_NO_GUIINFO, "Felület információ nem létezik, művelet nem hajtható végre ! (Mező számítás, " + FIdHelper.getFieldId(var1) + ")", (Exception)null, (Object)null);
      }

   }

   public void fieldDoCalculations(Vector var1) {
      if (var1 != null && var1.size() == 2) {
         Object var2 = var1.get(0);
         Object var3 = var1.get(1);
         if (var3 instanceof Vector) {
            this.fieldDoCalculations_((Vector)var3, 1, var2);
         }
      }

   }

   public void fieldDoCalculations_(Vector var1, int var2, Object var3) {
      if (this.field_list_cache != null) {
         Object[] var4 = null;
         if (var3 != null) {
            var4 = (Object[])((Object[])this.field_list_cache.get(var3));
         }

         int var9;
         if (var4 == null) {
            Object[] var5 = this.exp_store.getFullFieldCalcOrder();
            Vector var6 = new Vector(var1);
            int var7 = 0;
            var4 = new Object[var1.size()];
            int var8 = 0;

            for(var9 = var5.length; var8 < var9; ++var8) {
               if (var6.contains(var5[var8])) {
                  var4[var7++] = var5[var8];
                  var6.remove(var5[var8]);
               }
            }

            var8 = 0;

            for(var9 = var6.size(); var8 < var9; ++var8) {
               var4[var7++] = var6.get(var8);
            }

            this.field_list_cache.put(var3, var4);
         }

         Object var14 = FIdHelper.createId((Object)null, (Object)null, (Object)null);
         ExpWrapper var16 = new ExpWrapper();
         IDataStore var17 = this.getDataStore();
         var17.beginTransaction();

         try {
            var9 = 0;

            for(int var10 = var4.length; var9 < var10; ++var9) {
               Object var15 = var4[var9];
               FIdHelper.setFieldId(var14, var15);
               FIdHelper.setReturnValue(var14, Boolean.FALSE);
               int var11 = 0;

               for(int var12 = FIdHelper.getPageCount(var14); var11 < var12; ++var11) {
                  FIdHelper.setDPageNumber(var14, var11);
                  if ((var2 & 1) == 1) {
                     this.calculateField(var17, var16, var14, var15);
                  }

                  if ((var2 & 2) == 2) {
                     this.calculateDependencies(var14, this.getFormid(), var17, var11, var16);
                  }

                  if ((var2 & 4) == 4) {
                     this.checkDependentPagesOnField(var14);
                     this.checkDependentPagesOnFDeps(var14);
                  }
               }
            }

            var17.commitTransaction();
         } catch (Exception var13) {
            var17.rollbackTransaction();
            var13.printStackTrace();
         }
      }

   }

   private void calculateDependencies(Object var1, String var2, IDataStore var3, int var4, ExpWrapper var5) throws Exception {
      if (this.isAccelereted(var2)) {
         this.fastCalculateDependencies(var1, var2, var3, var4, var5);
      } else {
         this.slowCalculateDependencies(var1, var3, var5);
      }

   }

   private void fastCalculateDependencies(Object var1, String var2, IDataStore var3, int var4, ExpWrapper var5) throws Exception {
      Object[] var6 = (Object[])((Object[])this.exp_store.getFieldDependencies(FIdHelper.getFieldId(var1)));
      String var7 = this.getPageIdByFid(var2, FIdHelper.getFieldId(var1));
      if (var6 != null) {
         Object var8 = FIdHelper.createId((Object)null, (Object)null, (Object)null);
         ExpStore var10 = this.exp_store;
         int var11 = 0;

         for(int var12 = var6.length; var11 < var12; ++var11) {
            if (var10.getVariable(var6[var11]) == null) {
               FIdHelper.setFieldId(var8, var6[var11]);
               String var13 = this.getPageIdByFid(var2, (String)var6[var11]);
               if (var7.equals(var13)) {
                  FIdHelper.setDPageNumber(var8, var4);
                  this.calculateField(var3, var5, var8, var6[var11]);
               } else {
                  int var9 = FIdHelper.getPageCount(var8);
                  int var14 = 0;

                  for(int var15 = var9; var14 < var15; ++var14) {
                     FIdHelper.setDPageNumber(var8, var14);
                     this.calculateField(var3, var5, var8, var6[var11]);
                  }
               }
            }
         }
      }

   }

   private void slowCalculateDependencies(Object var1, IDataStore var2, ExpWrapper var3) throws Exception {
      Object[] var4 = (Object[])((Object[])this.exp_store.getFieldDependencies(FIdHelper.getFieldId(var1)));
      if (var4 != null) {
         Object var5 = FIdHelper.createId((Object)null, (Object)null, (Object)null);
         ExpStore var7 = this.exp_store;
         int var8 = 0;

         for(int var9 = var4.length; var8 < var9; ++var8) {
            if (var7.getVariable(var4[var8]) == null) {
               FIdHelper.setFieldId(var5, var4[var8]);
               int var6 = FIdHelper.getPageCount(var5);
               int var10 = 0;

               for(int var11 = var6; var10 < var11; ++var10) {
                  FIdHelper.setDPageNumber(var5, var10);
                  this.calculateField(var2, var3, var5, var4[var8]);
               }
            }
         }
      }

   }

   private boolean isAccelereted(String var1) {
      return acceleratedForms.contains(var1);
   }

   public Map<String, String> calculateVariables(String var1) {
      HashMap var2 = new HashMap();

      try {
         Object var3 = this.getExpStore(var1).getVariables();
         Hashtable var4 = ((Variables)var3).getVariables();
         Iterator var5 = var4.keySet().iterator();

         while(var5.hasNext()) {
            Object var6 = var5.next();
            ExpClass var7 = (ExpClass)var4.get(var6);
            Object var8 = ABEVFunctionSet.getInstance().calcVariable(var7, var1, this.getDataStore());
            var2.put(var6.toString(), var8.toString());
         }
      } catch (Exception var9) {
         Tools.eLog(var9, 1);
      }

      return var2;
   }

   public Object calculateExpression(Object var1) {
      if (!(var1 instanceof ExpClass)) {
         try {
            if (ExpFactory.functions == null) {
               ExpFactory.functions = FnFactory.createFunctionList();
            }

            if (ExpFactory.calculator == null) {
               ExpFactory.calculator = this;
            }

            var1 = ExpFactory.createExp(var1 == null ? null : var1.toString(), (String)null, -1, false, (String)null);
         } catch (Exception var3) {
            var1 = null;
         }
      }

      return this.calculateExp((Object)null, (Object)null, (ExpWrapper)null, (ExpClass)var1, (String)null, (IDataStore)null);
   }

   public void fireCalculations(Object var1) {
      if (var1 instanceof Object[]) {
         Object var2 = null;
         Object var3 = null;
         Object[] var4 = (Object[])((Object[])var1);
         Object var5 = null;

         try {
            var2 = var4[0];
            var3 = var4[1];
         } catch (Exception var19) {
            Tools.eLog(var19, 1);
         }

         if (var4.length > 2) {
            var5 = var4[2];
         }

         if (var2 instanceof String[] && var3 instanceof String[]) {
            if (this.exp_store == null) {
               return;
            }

            Object var6 = this.exp_store.getAllCalculation();
            Object var7 = this.exp_store.getCacheMaps();
            Object var8 = CalcFactory.getFilteredCollection2(var6, var7, (String[])((String[])var2), (String[])((String[])var3));
            Iterator var9 = CalcFactoryHelper.getCollectionIterator(var8);

            try {
               Object var10 = FIdHelper.createId((Object)null, (Object)null, (Object)null);
               if (var9.hasNext()) {
                  ExpWrapper var17 = new ExpWrapper();
                  IDataStore var18 = this.getDataStore();

                  label85:
                  while(true) {
                     while(true) {
                        while(true) {
                           if (!var9.hasNext()) {
                              break label85;
                           }

                           Object var11 = var9.next();
                           Boolean var15 = CalcHelper.getTargetBind(var11);
                           boolean var16 = var15 == null || var15;
                           if (var16) {
                              String var12 = CalcHelper.getEvent(var11);
                              String var13 = CalcHelper.getTargetType(var11);
                              String var14 = CalcHelper.getTargetId(var11);
                              if ("form_calc".equalsIgnoreCase(var12) && "form".equalsIgnoreCase(var13)) {
                                 this.formDoCalculations((Object)null);
                              } else {
                                 if ("form_check".equalsIgnoreCase(var12) && "form".equalsIgnoreCase(var13)) {
                                    throw new Exception("Ellenőrzés kivéve 20100205 hipercheck módosítás!");
                                 }

                                 if (var13.equalsIgnoreCase("field")) {
                                    if (this.gui_info != null && var18 instanceof IDataStore) {
                                       FIdHelper.clearId(var10);
                                       FIdHelper.setFieldId(var10, var14);
                                       this.fieldDoCalculations(var10);
                                    }
                                 } else {
                                    this.calculateExp((Object)null, var11, var17, CalcHelper.getExp(var11), ExpFactory.form_id, var18);
                                 }
                              }
                           } else {
                              this.calculateExp((Object)null, var11, var17, CalcHelper.getExp(var11), ExpFactory.form_id, var18);
                           }
                        }
                     }
                  }
               }

               if (var5 instanceof Vector) {
                  Vector var21 = (Vector)var5;
                  Vector var22 = new Vector(var21.size());
                  int var23 = 0;

                  for(int var24 = var21.size(); var23 < var24; ++var23) {
                     var22.add(var21.get(var23).toString());
                  }

                  this.fieldDoCalculations_(var22, 2, (Object)null);
               }
            } catch (Exception var20) {
               var20.printStackTrace();
            }
         }
      }

   }

   private Object calculateExp(Object var1, Object var2, ExpWrapper var3, ExpClass var4, String var5, IDataStore var6) {
      Object var7 = null;
      if (var4 != null) {
         if (var3 == null) {
            var3 = new ExpWrapper();
         }

         var3.setExp(var4);
         var3.setCalcRecord(var2);
         var3.setCurrentFieldId(var1, var5, var6);
         var7 = var3.get("");
      }

      return var7;
   }

   private void calculateField(IDataStore var1, ExpWrapper var2, Object var3, Object var4) throws Exception {
      this.calculateField_(var1, var2, var3, var4, true);
   }

   private String calculateField_(IDataStore var1, ExpWrapper var2, Object var3, Object var4, boolean var5) throws Exception {
      String var7 = null;
      if (var4 instanceof String) {
         Object var8 = this.exp_store.getFieldCalulation(var4);
         Hashtable var15 = this.calced_map;
         boolean var16 = this.abev_log;
         if (!(var8 instanceof Object[])) {
            return null;
         }

         Object[] var9 = (Object[])((Object[])var8);
         int var17 = 0;

         for(int var18 = var9.length; var17 < var18; ++var17) {
            Object[] var10 = (Object[])((Object[])var9[var17]);
            if (var16) {
               ABEVLoggerBusiness.clear();
               ABEVLoggerBusiness.setExpressionType("mezőszámítás");
               ABEVLoggerBusiness.setExpressionIdByFId(CalcHelper.getId(var10), FIdHelper.getFieldId(var3), this.getBookModel());
               ABEVLoggerBusiness.setExpression(CalcHelper.getExp(var10));
               ABEVLoggerBusiness.setCIDCode(FIdHelper.getFieldId(var3), FIdHelper.getDPageNumber(var3));
            }

            ExpClass var11 = CalcHelper.getExp(var10);
            if (var11 != null) {
               try {
                  Object var6 = this.calculateExp(var3, var10, var2, var11, ExpFactory.form_id, var1);
                  if (var16) {
                     ABEVLoggerBusiness.setExpressionResult(var6);
                  }

                  String var12 = CalcHelper.getTargetId(var10);
                  String var13 = var12 + "_" + FIdHelper.getDPageNumber(var3);
                  Boolean var19 = CalcHelper.getTargetBind(var10);
                  boolean var14 = var19 == null || var19;
                  if (var12 != null && var14) {
                     Object var20 = FIdHelper.getDataStoreKey(var3, var12);
                     String var21 = var1.get(var20);
                     var21 = var21 == null ? "" : var21;
                     var7 = ((IFunctionSet)CalcHelper.getExp(var10).getSource()).getRoundedValue((String)((Object[])((Object[])var20))[1], var6);
                     if (var15 != null && var13 != null && var21.equals(var7) && var15.get(var13) != null) {
                        continue;
                     }

                     if (var5) {
                        this.writeDataStore(var1, var20, var7);
                     }

                     if (var15 != null && var13 != null) {
                        var15.put(var13, "");
                     }
                  }
               } catch (Exception var22) {
                  var22.printStackTrace();
                  this.writeError("Hiba történt mező számítás közben !", var22);
               }
            }

            if (var16) {
               ABEVLoggerBusiness.write();
            }
         }
      }

      return var7;
   }

   private void checkDependentPagesOnFDeps(Object var1) {
      if (var1 instanceof Object[]) {
         var1 = FIdHelper.getFieldId(var1);
      }

      Object[] var2 = (Object[])((Object[])this.exp_store.getFieldDependencies(var1));
      if (var2 != null) {
         int var3 = 0;

         for(int var4 = var2.length; var3 < var4; ++var3) {
            this.checkDependentPagesOnField(var2[var3]);
         }
      }

   }

   private void checkDependentPagesOnField(Object var1) {
      if (var1 instanceof Object[]) {
         var1 = FIdHelper.getFieldId(var1);
      }

      Object[] var2;
      if ((var2 = (Object[])((Object[])this.exp_store.getDependPages(var1))) != null) {
         int var3 = 0;

         for(int var4 = var2.length; var3 < var4; ++var3) {
            this.gui_info.do_page_check((String)var2[var3]);
         }
      }

   }

   public void pageCheck(Object var1) {
      if (this.gui_info != null) {
         if (this.exp_store != null) {
            boolean var8 = this.abev_log;
            IPropertyList var2 = this.getPropertyList();
            if (var2 != null) {
               try {
                  var2.set("calculator_current_page_id", var1);
                  PIdHelper.setReturnValue(var1, Boolean.TRUE);
                  Object[] var3 = (Object[])((Object[])this.exp_store.getPageChecks(PIdHelper.getPageId(var1)));
                  if (var3 != null) {
                     ExpWrapper var5 = new ExpWrapper();
                     int var9 = 0;

                     for(int var10 = var3.length; var9 < var10; ++var9) {
                        Object var6 = var3[var9];
                        if (var8) {
                           ABEVLoggerBusiness.clear();
                           ABEVLoggerBusiness.setExpressionType("lapengedélyezés");
                           ABEVLoggerBusiness.setExpressionIdByPId(CalcHelper.getId(var6), PIdHelper.getPageId(var1));
                           ABEVLoggerBusiness.setExpression(CalcHelper.getExp(var6));
                        }

                        Object var4 = this.calculateExp((Object)null, var6, var5, CalcHelper.getExp(var6), ExpFactory.form_id, this.getDataStore());
                        if (var8) {
                           ABEVLoggerBusiness.setExpressionResult(var4);
                        }

                        Boolean var11 = CalcHelper.getTargetBind(var6);
                        boolean var7 = var11 == null || var11;
                        if (var4 instanceof Boolean && var7 && !(Boolean)var4) {
                           PIdHelper.setReturnValue(var1, Boolean.FALSE);
                           PIdHelper.setMessage(var1, CalcHelper.getMsg(var6));
                           PIdHelper.setMessageLevel(var1, CalcHelper.getErrorLevel(var6));
                           if (var8) {
                              ABEVLoggerBusiness.setErrorMessage(CalcHelper.getMsg(var6));
                              ABEVLoggerBusiness.setErrorMessageType(CalcHelper.getErrorLevel(var6));
                              ABEVLoggerBusiness.write();
                           }
                           break;
                        }

                        if (var8) {
                           ABEVLoggerBusiness.write();
                        }
                     }
                  }
               } catch (Exception var15) {
                  var15.printStackTrace();
                  this.writeError("Hiba történt lap ellenőrzés közben !", var15);
               } finally {
                  var2.set("calculator_current_page_id", (Object)null);
               }
            } else {
               this.writeError(ERR_ID_NO_PROPLIST, "Tulajdonság lista nem létezik, művelet nem hajtható végre ! (Lap ellenőrzés, " + PIdHelper.getPageId(var1) + ")", (Exception)null, (Object)null);
            }
         } else {
            this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Lap ellenőrzés, " + PIdHelper.getPageId(var1) + ")", IErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null);
         }
      } else {
         this.writeError(ERR_ID_NO_GUIINFO, "Felület információ nem létezik, művelet nem hajtható végre ! (Lap ellenőrzés, " + PIdHelper.getPageId(var1) + ")", (Exception)null, (Object)null);
      }

   }

   public void formCheck(Object var1) {
      this.exp_sum = 0L;
      this.check_field_sum = 0L;
      this.form_check_sum = 0L;
      Integer2 var2 = new Integer2(0);
      if (this.gui_info != null) {
         if (this.exp_store != null) {
            boolean var4 = this.abev_log;
            this.checkDisabledPageFields();
            String var5 = ExpFactory.form_id;
            IDataStore var6 = this.getDataStore();
            this.notifyFunctionBags6(var5, var6);
            this.notifyFunctionBags7();

            try {
               ExpWrapper var11 = new ExpWrapper();
               Hashtable var13 = this.gui_info.get_enabled_fields((Elem)this.gui_info.get_store_collection().get(this.gui_info.getCalcelemindex()));
               Object var14 = null;
               Object var16 = this.gui_form_object;
               Object var17 = this.getPropertyList().get("prop.dynamic.dirty2");
               boolean var18 = var17 instanceof Boolean && (Boolean)var17;
               Object var19 = ((Object[])((Object[])var1))[1];
               Hashtable var15;
               if (var19 == null) {
                  var15 = null;
               } else {
                  var15 = (Hashtable)((Vector)var19).get(1);
               }

               Object[] var8 = (Object[])((Object[])FIdHelper.createId((Object)null, (Object)null, (Object)null));
               if (var18) {
                  this.fieldsCheck_(this.exp_store.getFullFieldCalcOrder(), var13, var2, var8, this.exp_store, var6, true, var11, (Hashtable)var14, var4, var5, var16, var18, 2, var15);
               }

               this.fieldsCheck_(this.exp_store.getCheckableFields(), var13, var2, var8, this.exp_store, var6, true, var11, (Hashtable)var14, var4, var5, var16, var18, 1, var15);
               Object[] var7 = (Object[])((Object[])this.exp_store.getFormChecks());
               this.notifyFunctionBagsFormCheck(true);
               if (var7 != null) {
                  int var20 = 0;

                  for(int var21 = var7.length; var20 < var21; ++var20) {
                     Object var10 = var7[var20];
                     if (var4) {
                        ABEVLoggerBusiness.clear();
                        ABEVLoggerBusiness.setExpressionType("nyomtatvány");
                        ABEVLoggerBusiness.setExpressionIdByPId(CalcHelper.getId(var10), (Object)null);
                        ABEVLoggerBusiness.setExpression(CalcHelper.getExp(var10));
                     }

                     Object var9 = this.calculateExp((Object)null, var10, var11, CalcHelper.getExp(var10), ExpFactory.form_id, var6);
                     if (var4) {
                        ABEVLoggerBusiness.setExpressionResult(var9);
                     }

                     Boolean var22 = CalcHelper.getTargetBind(var10);
                     boolean var12 = var22 == null || var22;
                     if (var9 instanceof Boolean && var12 && !(Boolean)var9) {
                        Integer var23 = new Integer(0);
                        String var24 = CalcHelper.getErrorCode(var10);
                        if (CalcHelper.getTargetType(var10).equalsIgnoreCase("field")) {
                           FIdHelper.setFieldId(var8, CalcHelper.getTargetId(var10));
                           FIdHelper.setDPageNumber(var8, 0);
                           FIdHelper.setDataStoreId(var8, var6.getMasterCaseId(FIdHelper.getDataStoreKey((Object)var8)));
                           if (this.isAdozoRole() || var24 != null && ((String)var24).length() > 0) {
                              this.writeFieldCheckError(var8, this.extendMsg(var10, var23), this.getInsideErrorLevel(var10), var5, var16, CalcHelper.getErrorCode(var10), this.standAloneExtendedError(var10));
                           } else {
                              this.writeFieldCheckError(var8, this.extendMsg(var10, var23), IErrorList.LEVEL_WARNING, var5, var16, CalcHelper.getErrorCode(var10), this.standAloneExtendedError(var10));
                           }
                        } else {
                           String var25 = ((IFunctionSet)CalcHelper.getExp(var10).getSource()).getExtendedError();
                           if (!this.isAdozoRole() && (var24 == null || ((String)var24).length() <= 0)) {
                              this.writeFormCheckError(CalcHelper.getMsg(var10) + " " + var25, IErrorList.LEVEL_WARNING, CalcHelper.getErrorCode(var10), this.standAloneExtendedError(var10));
                           } else {
                              this.writeFormCheckError(CalcHelper.getMsg(var10) + " " + var25, this.getInsideErrorLevel(var10), CalcHelper.getErrorCode(var10), this.standAloneExtendedError(var10));
                           }
                        }

                        ++var2.i;
                        if (this.abev_log) {
                           ABEVLoggerBusiness.setErrorMessage(CalcHelper.getMsg(var10));
                           ABEVLoggerBusiness.setErrorMessageType(CalcHelper.getErrorLevel(var10));
                        }
                     }

                     if (var4) {
                        ABEVLoggerBusiness.write();
                     }
                  }
               }

               this.notifyFunctionBagsFormCheck(false);
            } catch (Exception var29) {
               System.out.println("Calculator.formCheck error");
               System.out.println("Hiba történt nyomtatvány ellenőrzés közben !");
               var29.printStackTrace();
               this.notifyFunctionBagsFormCheck(false);
               this.writeError("Hiba történt nyomtatvány ellenőrzés közben !", var29);
               ++var2.i;
            } finally {
               this.checked_map = null;
               this.highlighted_datastore = null;
               this.notifyFunctionBagsFormCheck(false);
            }
         } else {
            this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Nyomtatvány ellenőrzés)", IErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null);
            ++var2.i;
         }
      } else {
         this.writeError(ERR_ID_NO_GUIINFO, "Felület információ nem létezik, művelet nem hajtható végre ! (Nyomtatvány ellenőrzés)", (Exception)null, (Object)null);
         ++var2.i;
      }

      Object[] var3;
      if (var1 instanceof Object[] && (var3 = (Object[])((Object[])var1)).length > 0) {
         var3[0] = new Integer(var2.i);
      }

      this.notifyFunctionBags10();
      this.notifyFunctionBagsFormCheck(false);
   }

   public void formDoCalculations(Object var1) {
      if (this.isInGeneratorMod()) {
         System.out.println("Start pregen ----------------------------------");
         this.doPreGenCalcs();
         System.out.println("Stop pregen ----------------------------------");
      }

      this.formDoCalc(var1);
      if (this.isInGeneratorMod()) {
         System.out.println("Start postgen ----------------------------------");
         this.doPostGenCalcs();
         System.out.println("Stop postgen ----------------------------------");
      }

   }

   public void formDoCalc(Object var1) {
      int var2 = 0;
      if (this.exp_store != null) {
         String var4 = ExpFactory.form_id;
         IDataStore var5 = this.getDataStore();
         this.notifyFunctionBags6(var4, var5);
         this.notifyFunctionBags8();

         try {
            this.calced_map = new Hashtable(4096);
            this.highlighted_datastore = this.getDataStore();
            Object[] var6 = this.exp_store.getFullFieldCalcOrder();
            if (var6 != null) {
               ExpWrapper var9 = new ExpWrapper();
               var5 = this.getDataStore();
               var5.beginTransaction();
               Object[] var7 = (Object[])((Object[])FIdHelper.createId((Object)null, (Object)null, (Object)null));
               int var10 = 0;
               int var11 = var6.length;

               while(true) {
                  if (var10 >= var11) {
                     var5.commitTransaction();
                     break;
                  }

                  if (!this.isPartMulti() || !ABEVFunctionSet.getInstance().isCachedTargetId(var4.toString(), "globsum", (String)var6[var10])) {
                     FIdHelper.setFieldId(var7, var6[var10]);
                     FIdHelper.setReturnValue(var7, Boolean.FALSE);
                     int var12 = 0;

                     for(int var13 = FIdHelper.getPageCount(var7); var12 < var13; ++var12) {
                        FIdHelper.setDPageNumber(var7, var12);
                        this.calculateField(var5, var9, var7, FIdHelper.getFieldId(var7));
                        this.checkDependentPagesOnField(var7);
                        this.checkDependentPagesOnFDeps(var7);
                     }

                     Object var8 = FIdHelper.getReturnValue(var7);
                     if (var8 instanceof Boolean && !(Boolean)var8) {
                        ++var2;
                     }
                  }

                  ++var10;
               }
            }

            this.fireCalculations(new Object[]{new String[]{"on_event"}, new String[]{"multi_form_load"}});
         } catch (Exception var17) {
            if (var5 != null) {
               var5.rollbackTransaction();
            }

            this.writeError("Hiba törtrént nyomtatvány számítás közben !", var17);
            ++var2;
         } finally {
            this.calced_map = null;
            this.highlighted_datastore = null;
         }
      } else {
         this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Nyomtatvány számítás)", IErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null);
         ++var2;
      }

      this.notifyFunctionBags11();
   }

   public void doPreGenCalcs() {
      this.doGenCalcs(this.exp_store.getPreGenCalcs());
   }

   public void doPostGenCalcs() {
      this.doGenCalcs(this.exp_store.getPostGenCalcs());
   }

   public boolean doBetoltErtekCalcs(boolean var1) {
      List var2 = this.doGenCalcs(this.exp_store.getBetoltErtekCalcs());
      if (!var2.isEmpty() && var1) {
         getInstance().calculateDependentFields(var2);
      }

      return !var2.isEmpty();
   }

   public List<String> doGenCalcs(Object[] var1) {
      ABEVFunctionSet.getInstance().resetGenErtekChangedFieldValue();
      if (this.gui_info != null) {
         if (this.exp_store != null) {
            String var2 = ExpFactory.form_id;
            IDataStore var3 = this.getDataStore();
            this.notifyFunctionBags6(var2, var3);

            try {
               var3.beginTransaction();
               ExpWrapper var4 = new ExpWrapper();
               if (var1 != null) {
                  Object[] var5 = var1;
                  int var6 = var1.length;

                  for(int var7 = 0; var7 < var6; ++var7) {
                     Object var8 = var5[var7];
                     this.calculateExp((Object)null, var8, var4, CalcHelper.getExp(var8), ExpFactory.form_id, var3);
                  }
               }

               var3.commitTransaction();
            } catch (Exception var13) {
               if (var3 != null) {
                  var3.rollbackTransaction();
               }

               this.writeError("Generálási hiba a pre/post fázisban.", var13);
            } finally {
               this.highlighted_datastore = null;
            }
         } else {
            this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Nyomtatvány ellenőrzés)", IErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null);
         }
      } else {
         this.writeError(ERR_ID_NO_GUIINFO, "Felület információ nem létezik, művelet nem hajtható végre ! (Nyomtatvány ellenőrzés)", (Exception)null, (Object)null);
      }

      return ABEVFunctionSet.getInstance().getGenErtekChangedFieldValue();
   }

   public void mainFormCalculationsInBatchRecalc() {
      if (this.exp_store != null) {
         String var1 = ExpFactory.form_id;
         IDataStore var2 = this.getDataStore();
         this.notifyFunctionBags6(var1, var2);
         this.notifyFunctionBags8();

         try {
            this.calced_map = new Hashtable(4096);
            this.highlighted_datastore = this.getDataStore();
            Object[] var3 = this.exp_store.getFullFieldCalcOrder();
            if (var3 != null) {
               ExpWrapper var6 = new ExpWrapper();
               var2 = this.getDataStore();
               var2.beginTransaction();
               Object[] var4 = (Object[])((Object[])FIdHelper.createId((Object)null, (Object)null, (Object)null));
               int var7 = 0;

               for(int var8 = var3.length; var7 < var8; ++var7) {
                  FIdHelper.setFieldId(var4, var3[var7]);
                  FIdHelper.setReturnValue(var4, Boolean.FALSE);
                  int var9 = 0;

                  for(int var10 = FIdHelper.getPageCount(var4); var9 < var10; ++var9) {
                     FIdHelper.setDPageNumber(var4, var9);
                     this.calculateField(var2, var6, var4, FIdHelper.getFieldId(var4));
                  }

                  Object var5 = FIdHelper.getReturnValue(var4);
               }

               var2.commitTransaction();
            }
         } catch (Exception var14) {
            if (var2 != null) {
               var2.rollbackTransaction();
            }

            this.writeError("Hiba törtrént nyomtatvány számítás közben !", var14);
         } finally {
            this.calced_map = null;
            this.highlighted_datastore = null;
         }
      } else {
         this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Nyomtatvány számítás)", IErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null);
      }

      this.notifyFunctionBags11();
   }

   public void formDoPageCalculations(String var1, int var2) {
      if (this.exp_store != null) {
         String var3 = ExpFactory.form_id;
         IDataStore var4 = this.getDataStore();
         this.notifyFunctionBags6(var3, var4);

         try {
            this.calced_map = new Hashtable(4096);
            this.highlighted_datastore = this.getDataStore();
            Object[] var5 = this.exp_store.getFullFieldCalcOrderOnPage(var1);
            if (var5 != null) {
               ExpWrapper var7 = new ExpWrapper();
               var4 = this.getDataStore();
               var4.beginTransaction();
               Object[] var6 = (Object[])((Object[])FIdHelper.createId((Object)null, (Object)null, (Object)null));
               int var8 = 0;

               for(int var9 = var5.length; var8 < var9; ++var8) {
                  FIdHelper.setFieldId(var6, var5[var8]);
                  FIdHelper.setReturnValue(var6, Boolean.FALSE);
                  FIdHelper.setDPageNumber(var6, var2);
                  this.calculateField(var4, var7, var6, FIdHelper.getFieldId(var6));
               }

               var4.commitTransaction();
            }
         } catch (Exception var13) {
            if (var4 != null) {
               var4.rollbackTransaction();
            }

            this.writeError("Hiba törtrént nyomtatvány számítás közben !", var13);
         } finally {
            this.calced_map = null;
            this.highlighted_datastore = null;
         }
      } else {
         this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Nyomtatvány számítás)", IErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null);
      }

   }

   public void formHiddenFieldsDoCalculations() {
      if (this.exp_store != null) {
         String var1 = ExpFactory.form_id;
         this.formFieldsDoCalculations(this.getShortInvNotFullFields(var1));
      } else {
         this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Nyomtatvány számítás)", IErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null);
      }

   }

   public void formNotInBelFeldFieldsDoCalculations() {
      if (this.exp_store != null) {
         String var1 = ExpFactory.form_id;
         this.formFieldsDoCalculations(this.getNotInBelFeldFields(var1));
      } else {
         this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Nyomtatvány számítás)", IErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null);
      }

   }

   public void pageFieldsVisibilityCalc(String var1, String var2, Integer var3) {
      if (this.exp_store != null) {
         String var4 = ExpFactory.form_id;
         if (!var4.equals(var1)) {
            this.writeCalculationError("Hiba történt a mező írhatóságának beállításánál!  Eltérő form azonosító.");
            return;
         }

         this.pageFieldsDoCalculations(ABEVFunctionSet.getInstance().getReadOnlyCalcFieldsList(var1, var2), var3);
      } else {
         this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Nyomtatvány számítás)", IErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null);
      }

   }

   public void pageFieldsDoCalculations(Vector var1, Integer var2) {
      if (var1 != null) {
         if (this.exp_store != null) {
            String var3 = ExpFactory.form_id;
            IDataStore var4 = this.getDataStore();
            this.notifyFunctionBags6(var3, var4);

            try {
               this.notifyReadOnlyFieldsCalc(Boolean.TRUE, var2);
               this.calced_map = new Hashtable(4096);
               this.highlighted_datastore = this.getDataStore();
               if (var1 != null) {
                  ExpWrapper var6 = new ExpWrapper();
                  var4 = this.getDataStore();
                  var4.beginTransaction();
                  Object[] var5 = (Object[])((Object[])FIdHelper.createId((Object)null, (Object)null, (Object)null));
                  int var7 = 0;

                  for(int var8 = var1.size(); var7 < var8; ++var7) {
                     FIdHelper.setFieldId(var5, var1.get(var7));
                     FIdHelper.setReturnValue(var5, Boolean.FALSE);
                     FIdHelper.setDPageNumber(var5, var2);

                     try {
                        this.calculateField(var4, var6, var5, FIdHelper.getFieldId(var5));
                     } catch (Exception var14) {
                        var14.printStackTrace();
                        this.writeCalculationError("Hiba történt a mező írhatóságának beállításánál! " + var14.getMessage());
                     }
                  }

                  var4.commitTransaction();
               }
            } catch (Exception var15) {
               if (var4 != null) {
                  var4.rollbackTransaction();
               }

               var15.printStackTrace();
               this.writeCalculationError("Hiba történt a mező írhatóságának beállításánál! " + var15.getMessage());
            } finally {
               this.calced_map = null;
               this.highlighted_datastore = null;
               this.notifyReadOnlyFieldsCalc(Boolean.FALSE, var2);
            }
         } else {
            this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Nyomtatvány számítás)", IErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null);
         }

      }
   }

   public void formFieldsDoCalculations(Vector var1) {
      if (this.exp_store != null) {
         String var2 = ExpFactory.form_id;
         IDataStore var3 = this.getDataStore();
         this.notifyFunctionBags6(var2, var3);

         try {
            this.calced_map = new Hashtable(4096);
            this.highlighted_datastore = this.getDataStore();
            if (var1 != null) {
               ExpWrapper var5 = new ExpWrapper();
               var3 = this.getDataStore();
               var3.beginTransaction();
               Object[] var4 = (Object[])((Object[])FIdHelper.createId((Object)null, (Object)null, (Object)null));
               int var6 = 0;

               for(int var7 = var1.size(); var6 < var7; ++var6) {
                  FIdHelper.setFieldId(var4, var1.get(var6));
                  FIdHelper.setReturnValue(var4, Boolean.FALSE);
                  int var8 = 0;

                  for(int var9 = FIdHelper.getPageCount(var4); var8 < var9; ++var8) {
                     FIdHelper.setDPageNumber(var4, var8);

                     try {
                        this.calculateField(var3, var5, var4, FIdHelper.getFieldId(var4));
                     } catch (Exception var15) {
                        var15.printStackTrace();
                        this.writeCalculationError("Hiba történt rejtett mező újraszámítása közben ! " + var15.getMessage());
                     }
                  }
               }

               var3.commitTransaction();
            }
         } catch (Exception var16) {
            if (var3 != null) {
               var3.rollbackTransaction();
            }

            var16.printStackTrace();
            this.writeCalculationError("Hiba történt a rejtett mezők újraszámítása közben ! " + var16.getMessage());
         } finally {
            this.calced_map = null;
            this.highlighted_datastore = null;
         }
      } else {
         this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Nyomtatvány számítás)", IErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null);
      }

   }

   public void calculateListOfFields(Vector<String> var1) {
      if (this.exp_store != null) {
         String var2 = ExpFactory.form_id;
         IDataStore var3 = this.getDataStore();
         this.notifyFunctionBags6(var2, var3);

         try {
            this.calced_map = new Hashtable(4096);
            this.highlighted_datastore = this.getDataStore();
            if (var1 != null) {
               ExpWrapper var5 = new ExpWrapper();
               var3 = this.getDataStore();
               var3.beginTransaction();
               Object[] var4 = (Object[])((Object[])FIdHelper.createId((Object)null, (Object)null, (Object)null));
               int var6 = 0;

               for(int var7 = var1.size(); var6 < var7; ++var6) {
                  FIdHelper.setFieldId(var4, var1.get(var6));
                  FIdHelper.setReturnValue(var4, Boolean.FALSE);
                  ArrayList var8 = new ArrayList();
                  var8.add(var1.get(var6));
                  int var9 = 0;

                  for(int var10 = FIdHelper.getPageCount(var4); var9 < var10; ++var9) {
                     FIdHelper.setDPageNumber(var4, var9);

                     try {
                        this.calculateField(var3, var5, var4, FIdHelper.getFieldId(var4));
                        this.calculateDependentFields(var8);
                     } catch (Exception var16) {
                        var16.printStackTrace();
                        this.writeCalculationError("Hiba történt rejtett mező újraszámítása közben ! " + var16.getMessage());
                     }
                  }
               }

               var3.commitTransaction();
            }
         } catch (Exception var17) {
            if (var3 != null) {
               var3.rollbackTransaction();
            }

            var17.printStackTrace();
            this.writeCalculationError("Hiba történt a rejtett mezők újraszámítása közben ! " + var17.getMessage());
         } finally {
            this.calced_map = null;
            this.highlighted_datastore = null;
         }
      } else {
         this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Nyomtatvány számítás)", IErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null);
      }

   }

   private void writeCalculationError(String var1) {
      Long var2 = new Long(4001L);
      String var3 = "";
      GoToButton var4 = new GoToButton("");
      ErrorList.getInstance().writeError(var2, var1, ErrorList.LEVEL_FATAL_ERROR, (Exception)null, var4, (Object)"m002", (Object)var3);
   }

   private Vector getShortInvNotFullFields(String var1) {
      if (this.tableShortInvNotFullFields == null) {
         this.tableShortInvNotFullFields = new Hashtable();
      }

      Vector var2 = (Vector)this.tableShortInvNotFullFields.get(var1);
      if (var2 == null) {
         Hashtable var3 = this.gui_info.get(var1).get_short_inv_not_full_fields();
         var2 = this.createCalcOrderFieldList(var3);
         this.tableShortInvNotFullFields.put(var1, var2);
      }

      return var2;
   }

   private Vector getNotInBelFeldFields(String var1) {
      if (this.tableNotInBelFeldFields == null) {
         this.tableNotInBelFeldFields = new Hashtable();
      }

      Vector var2 = (Vector)this.tableNotInBelFeldFields.get(var1);
      if (var2 == null) {
         Hashtable var3 = MetaInfo.getInstance().getNotInBelFeldFields(var1);
         var2 = this.createCalcOrderFieldList(var3);
         this.tableNotInBelFeldFields.put(var1, var2);
      }

      return var2;
   }

   private Vector getDPageNumberFields(String var1) {
      if (this.tableDPageNumberFields == null) {
         this.tableDPageNumberFields = new Hashtable();
      }

      Vector var2 = (Vector)this.tableDPageNumberFields.get(var1);
      if (var2 == null) {
         Hashtable var3 = MetaInfo.getInstance().getDPageNumberFields(var1);
         var2 = this.createCalcOrderFieldList(var3);
         this.tableDPageNumberFields.put(var1, var2);
      }

      return var2;
   }

   private Vector createCalcOrderFieldList(Hashtable var1) {
      Vector var2 = new Vector(var1.size());
      Object[] var3 = this.exp_store.getFullFieldCalcOrder();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         String var5 = (String)var3[var4];
         if (var1.containsKey(var5)) {
            var2.add(var5);
         }
      }

      return var2;
   }

   public Vector<String> createCalcOrderFieldList(String var1, Set<String> var2) {
      Vector var3 = new Vector();

      try {
         HashSet var4 = new HashSet();
         var4.addAll(var2);
         Vector var5 = new Vector();
         ExpStore var6 = (ExpStore)this.exp_stores.get(var1);
         Object[] var7 = var6.getFullFieldCalcOrder();

         for(int var8 = 0; var8 < var7.length; ++var8) {
            String var9 = (String)var7[var8];
            if (var2.contains(var9)) {
               var5.add(var9);
               var4.remove(var9);
            }
         }

         var3.addAll(var4);
         var3.addAll(var5);
         return var3;
      } catch (Exception var10) {
         return var3;
      }
   }

   private String getFormid() {
      try {
         return ((Elem)this.gui_info.get_store_collection().get(this.gui_info.getCalcelemindex())).getType();
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
         return null;
      }
   }

   private IDataStore getDataStore() {
      try {
         return (IDataStore)((Elem)this.gui_info.get_store_collection().get(this.gui_info.getCalcelemindex())).getRef();
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
         return null;
      }
   }

   private IPropertyList getPropertyList() {
      return PropertyList.getInstance();
   }

   public Object getProperty(Object var1) {
      IPropertyList var2 = this.getPropertyList();
      return var2 != null ? var2.get(var1) : null;
   }

   private void writeError(String var1, Exception var2) {
      ErrorList.getInstance().writeError(ID_DEFAULT_CALCULATOR_ERROR, var1, var2, (Object)null);
   }

   private void writeError(Object var1, String var2, Exception var3, Object var4) {
      ErrorList.getInstance().writeError(ID_DEFAULT_CALCULATOR_ERROR, var2, var3, var4);
   }

   private void writeError(Object var1, String var2, Integer var3, Exception var4, Object var5) {
      this.writeError(ID_DEFAULT_CALCULATOR_ERROR, var2, var3, var4, var5, (Object)null, (Object)null);
   }

   private void writeError(Object var1, String var2, Integer var3, Exception var4, Object var5, Object var6, Object var7) {
      ErrorList.getInstance().writeError(ID_DEFAULT_CALCULATOR_ERROR, var2, var3, var4, var5, (Object)var6, (Object)var7);
   }

   private void writeLog(Object var1) {
      try {
         EventLog.getInstance().writeLog(var1);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   private String extendMsg(String var1, Object var2, Integer var3) {
      String var4 = CalcHelper.getTargetId(var2);
      String var5 = ((IFunctionSet)CalcHelper.getExp(var2).getSource()).getExtendedError();
      String var6 = var1;
      if (var5.length() > 0) {
         var6 = var1 + " " + var5;
      }

      return this.gui_info.isOnyaCheckMode() ? var1 : this.extendMsg(var6, var4, var3);
   }

   private String extendMsg(Object var1, Integer var2) {
      String var3 = CalcHelper.getMsg(var1);
      String var4 = CalcHelper.getTargetId(var1);
      String var5 = ((IFunctionSet)CalcHelper.getExp(var1).getSource()).getExtendedError();
      String var6 = var3;
      if (var5.length() > 0) {
         var6 = var3 + " " + var5;
      }

      return this.gui_info.isOnyaCheckMode() ? var3 : this.extendMsg(var6, var4, var2);
   }

   private String standAloneExtendedError(Object var1) {
      String var2 = ((IFunctionSet)CalcHelper.getExp(var1).getSource()).getExtendedError();
      return var2 == null ? "" : var2;
   }

   private String extendMsg(String var1, String var2, Integer var3) {
      if (var2 != null) {
         Object[] var10000 = new Object[]{null, null, null};
         String var5 = "";
         var5 = var5 + extendedInfo(var2, var3);
         var1 = var1 + " \n" + var5;
      }

      return var1;
   }

   public static String extendedInfo(String var0, Integer var1) {
      return MetaInfo.extendedInfoTxt(var0, var1, ExpFactory.form_id, instance.gui_info);
   }

   private void writeFieldCheckError(Object var1, String var2, Integer var3, Object var4, Object var5, Object var6, Object var7) {
      GoToButton var8 = null;
      if (var1 != null && this.isInteraktivOpMode()) {
         var8 = new GoToButton("Ugrás a mezőre");
         var8.setFieldId(var1, this.gui_info, var5);
      }

      FIdHelper.setErrorMessage(var1, var2);
      FIdHelper.setErrorLevel(var1, var3);
      this.writeError(ERR_ID_FIELD_CHECK, var2 == null ? "(Nincs üzenet)" : var2, var3, (Exception)null, var8, var6, var7);
   }

   private void writeFormCheckError(String var1, Integer var2, Object var3, Object var4) {
      this.writeError(ERR_ID_FORM_CHECK, var1 == null ? "(Nincs üzenet)" : var1, var2, (Exception)null, this.getFirstFieldGoToButton(), var3, var4);
   }

   private GoToButton getFirstFieldGoToButton() {
      if (this.isInteraktivOpMode()) {
         try {
            Iterator var1 = ABEVFunctionSet.getInstance().getExpressionFieldsList();
            if (var1.hasNext()) {
               return (GoToButton)var1.next();
            }
         } catch (Exception var2) {
            return null;
         }
      }

      return null;
   }

   private int getDataItemCount(Object var1) {
      Vector var2;
      return (var2 = ExpStoreFactory.getDataVector(var1)) != null ? var2.size() : 0;
   }

   private void notifyFunctionBags(Object var1, Object var2, Object var3, Object var4) {
      Vector var5 = FnFactory.fn_bags;
      if (var5 != null) {
         int var7 = 0;

         for(int var8 = var5.size(); var7 < var8; ++var7) {
            IFunctionSet var6 = (IFunctionSet)var5.get(var7);
            if (var6 != null) {
               var6.setFormId((String)var1);
               var6.setDataStore((IDataStore)var2);
               var6.setFieldTypes((Hashtable)var3);
               var6.setVariables((IPropertyList)var4, (String)var1);
            }
         }
      }

   }

   private void notifyFunctionBags5(Object var1, Object var2) {
      Vector var3 = FnFactory.fn_bags;
      if (var3 != null) {
         int var5 = 0;

         for(int var6 = var3.size(); var5 < var6; ++var5) {
            IFunctionSet var4 = (IFunctionSet)var3.get(var5);
            if (var4 != null) {
               var4.setVariables((IPropertyList)var1, (String)var2);
            }
         }
      }

   }

   public void notifyReadOnlyFieldsCalc(Boolean var1, Integer var2) {
      Vector var3 = FnFactory.fn_bags;
      if (var3 != null) {
         int var5 = 0;

         for(int var6 = var3.size(); var5 < var6; ++var5) {
            IFunctionSet var4 = (IFunctionSet)var3.get(var5);
            if (var4 != null) {
               var4.setReadonlyFieldCalcState(var1, var2);
            }
         }
      }

   }

   private void notifyFunctionBags6(Object var1, Object var2) {
      Vector var3 = FnFactory.fn_bags;
      if (var3 != null) {
         int var5 = 0;

         for(int var6 = var3.size(); var5 < var6; ++var5) {
            IFunctionSet var4 = (IFunctionSet)var3.get(var5);
            if (var4 != null) {
               var4.setFormId((String)var1);
               var4.setDataStore((IDataStore)var2);
            }
         }
      }

   }

   private void notifyFunctionBags7() {
      Vector var1 = FnFactory.fn_bags;
      if (var1 != null) {
         int var3 = 0;

         for(int var4 = var1.size(); var3 < var4; ++var3) {
            IFunctionSet var2 = (IFunctionSet)var1.get(var3);
            if (var2 != null) {
               var2.setStartFullcheck();
            }
         }
      }

   }

   private void notifyFunctionBags8() {
      Vector var1 = FnFactory.fn_bags;
      if (var1 != null) {
         int var3 = 0;

         for(int var4 = var1.size(); var3 < var4; ++var3) {
            IFunctionSet var2 = (IFunctionSet)var1.get(var3);
            if (var2 != null) {
               var2.setStartFullcalc();
            }
         }
      }

   }

   private void notifyFunctionBags9(String var1) {
      Vector var2 = FnFactory.fn_bags;
      if (var2 != null) {
         int var4 = 0;

         for(int var5 = var2.size(); var4 < var5; ++var4) {
            IFunctionSet var3 = (IFunctionSet)var2.get(var4);
            if (var3 != null) {
               if ("multi_start_calc".equalsIgnoreCase(var1)) {
                  var3.setMultiStartCalc();
               } else if ("multi_start_check".equalsIgnoreCase(var1)) {
                  var3.setMultiStartCheck();
               } else if ("multi_stop_calc".equalsIgnoreCase(var1)) {
                  var3.setMultiStopCalc();
               } else if ("multi_stop_check".equalsIgnoreCase(var1)) {
                  var3.setMultiStopCheck();
               }
            }
         }
      }

   }

   private void notifyFunctionBags10() {
      Vector var1 = FnFactory.fn_bags;
      if (var1 != null) {
         int var3 = 0;

         for(int var4 = var1.size(); var3 < var4; ++var3) {
            IFunctionSet var2 = (IFunctionSet)var1.get(var3);
            if (var2 != null) {
               var2.setStopFullcheck();
            }
         }
      }

   }

   private void notifyFunctionBags11() {
      Vector var1 = FnFactory.fn_bags;
      if (var1 != null) {
         int var3 = 0;

         for(int var4 = var1.size(); var3 < var4; ++var3) {
            IFunctionSet var2 = (IFunctionSet)var1.get(var3);
            if (var2 != null) {
               var2.setStopFullcalc();
            }
         }
      }

   }

   private void notifyFunctionBagsFormCheck(boolean var1) {
      Vector var2 = FnFactory.fn_bags;
      if (var2 != null) {
         int var4 = 0;

         for(int var5 = var2.size(); var4 < var5; ++var4) {
            IFunctionSet var3 = (IFunctionSet)var2.get(var4);
            if (var3 != null) {
               var3.setFormCheck(var1);
            }
         }
      }

   }

   private void notifyFunctionBagsPreviousItem(StoreItem var1) {
      Vector var2 = FnFactory.fn_bags;
      if (var2 != null) {
         int var4 = 0;

         for(int var5 = var2.size(); var4 < var5; ++var4) {
            IFunctionSet var3 = (IFunctionSet)var2.get(var4);
            if (var3 != null) {
               var3.setPreviousItem(var1);
            }
         }
      }

   }

   public Object eventFired(Object var1) {
      if (var1 instanceof Hashtable) {
         Hashtable var2 = (Hashtable)var1;
         if (this.exp_stores != null && var2.get("id") != null) {
            String var3 = (String)var2.get("id");
            if (var3 != null) {
               ExpStore var4 = (ExpStore)this.exp_stores.get(var3);
               this.exp_store = var4;
               if (var4 != null) {
                  this.gui_form_object = var2.get("guiobject");
                  ExpFactory.form_id = var3;
                  if (this.abev_log) {
                     ABEVLoggerBusiness.acquireDynamicInfos(this.getBookModel());
                  }
               } else {
                  this.writeError("Calculator: Számítási tár váltás nem lehetséges ! (" + var3 + ")", (Exception)null);
               }
            }
         }
      } else if (var1 instanceof String) {
         String var5 = (String)var1;
         if ("afteropen".equalsIgnoreCase(var5)) {
            if (this.abev_log) {
               this.abev_log_key = ABEVLoggerBusiness.open(this);
            }
         } else if ("afterclose".equalsIgnoreCase(var5)) {
            if (this.abev_log) {
               ABEVLoggerBusiness.close(this.abev_log_key);
            }
         } else if (var5.startsWith("multi_")) {
            this.notifyFunctionBags9(var5);
         }
      }

      return null;
   }

   private void checkDisabledPageFields() {
      if (this.gui_info != null) {
         Elem var1 = (Elem)this.gui_info.get_store_collection().get(this.gui_info.getCalcelemindex());
         if (var1 != null) {
            Tools.checkDisabled(var1, this.gui_info);
         }
      }

   }

   private boolean isInteraktivOpMode() {
      return !this.gui_info.getOperationMode().equals("2");
   }

   public boolean isInBelsoFeldolgozo() {
      return this.isOfficeOpMode() && !this.isInGeneratorMod();
   }

   private boolean isOfficeOpMode() {
      return !this.gui_info.getOperationMode().equals("0");
   }

   private boolean isBatchOpMode() {
      return this.gui_info.getOperationMode().equals("2");
   }

   private boolean isAdozoRole() {
      return this.gui_info.getRole().equals("0");
   }

   private boolean isOfficerRole() {
      return this.gui_info.getRole().equals("1");
   }

   private boolean isRevizorRole() {
      return this.gui_info.getRole().equals("2");
   }

   private boolean isUtolagosRevizoriRole() {
      return this.gui_info.getRole().equals("3");
   }

   private boolean isPartMulti() {
      return MainFrame.isPart;
   }

   private boolean isPartOnlyMainOpMulti() {
      return MainFrame.isPartOnlyMain;
   }

   public int getLastElogicCallerStatus() {
      return this.lastElogicCallerStatus;
   }

   private boolean isInGeneratorMod() {
      return "10".equals(this.gui_info.getHasznalatiMod());
   }

   private Integer getInsideErrorLevel(Object var1) {
      Integer var2 = CalcHelper.getErrorLevel(var1);
      return !this.isInGeneratorMod() && !this.isBatchOpMode() && !this.isOfficerRole() && !this.isRevizorRole() && !this.isUtolagosRevizoriRole() || var2 == null || this.gui_info.isOnyaCheckMode() || !var2.equals(IErrorList.LEVEL_WARNING) && !var2.equals(IErrorList.LEVEL_MESSAGE) ? var2 : IErrorList.LEVEL_ERROR;
   }

   public void writeDataStore(IDataStore var1, Object var2, String var3) {
      var1.set(var2, var3);
   }

   public Hashtable<String, String> get_rogz_calc_fids_list(String var1) {
      Hashtable var2 = new Hashtable();
      if (var1.equalsIgnoreCase("1044A")) {
         var2.put("0A0001E001A", "");
         var2.put("0A0001E002A", "");
         var2.put("0A0001E005A", "");
      } else if (var1.equalsIgnoreCase("1044T")) {
         var2.put("0A0001C001A", "");
         var2.put("0A0001C002A", "");
         var2.put("0A0001C003A", "");
         var2.put("0A0001C004A", "");
         var2.put("0A0001C005A", "");
      }

      return var2;
   }

   public Hashtable<String, String> get_rogz_stat_exc_fids_list(String var1) {
      return this.gui_info.get(var1).get_short_inv_fields_ht();
   }

   public Object getGuiFormObject() {
      return this.gui_form_object;
   }

   public boolean isCalculatorActive() {
      Vector var1 = FnFactory.fn_bags;
      if (var1 != null) {
         IFunctionSet var2 = (IFunctionSet)var1.get(0);
         if (var2 != null) {
            return var2.isStartFullcheck() || var2.isStartFullcalc();
         }
      }

      return false;
   }

   public String getPageIdByFid(String var1, String var2) {
      try {
         FormModel var3 = this.gui_info.get(var1);
         PageModel var4 = (PageModel)var3.fids_page.get(var2);
         return var4.pid;
      } catch (Exception var5) {
         return null;
      }
   }

   public String getExpressionValue(IDataStore var1, String var2, String var3, int var4) throws Exception {
      String var6 = "";
      Object var7 = this.exp_store.getFieldCalulation(var3);
      if (!(var7 instanceof Object[])) {
         return null;
      } else {
         Object[] var8 = (Object[])((Object[])var7);
         Object[] var9 = (Object[])((Object[])var8[0]);
         ExpClass var10 = CalcHelper.getExp(var9);
         if (var10 != null) {
            try {
               Object var11 = FIdHelper.createId((Object)null, (Object)null, (Object)null);
               FIdHelper.setFieldId(var11, var3);
               FIdHelper.setDPageNumber(var11, var4);
               Object var5 = this.calculateExp(var11, var9, (ExpWrapper)null, var10, var2, var1);
               var6 = ((IFunctionSet)CalcHelper.getExp(var9).getSource()).getRoundedValue(var3, var5);
            } catch (Exception var13) {
               var13.printStackTrace();
               this.writeError("Hiba történt mező számítás közben! (getExpressionValue)", var13);
            }
         }

         return var6;
      }
   }

   public boolean isFoAdatDependency(String var1, String var2) {
      return ABEVFunctionSet.getInstance().isFoAdatDependency(var1, var2);
   }

   public Set<String> getSubFormFoAdatDependency(String var1, String var2) {
      return ABEVFunctionSet.getInstance().getSubFormFoAdatDependency(var1, var2);
   }

   public Set<String> getCalculatorFieldDependencies(String var1, String var2) {
      HashSet var3 = new HashSet();

      try {
         ExpStore var4 = this.getExpStore(var1);
         var3 = new HashSet(Arrays.asList((String[])((String[])var4.getFieldDependencies(var2))));
         return var3;
      } catch (Exception var5) {
         return var3;
      }
   }

   public boolean isInJavkeretOpMode() {
      return ABEVFunctionSet.getInstance().isInJavkeretOpMode();
   }

   public void runDPageNumberCalcs() {
      if (this.exp_store != null) {
         String var1 = ExpFactory.form_id;
         this.formFieldsDoCalculations(this.getDPageNumberFields(var1));
      } else {
         this.writeError(ERR_ID_NO_EXPSTORE, "Kifejezés tár nem létezik, művelet nem hajtható végre ! (Lapszám mezők újraszámítása)", IErrorList.LEVEL_ERROR, (Exception)null, (Object)null);
      }

   }
}
