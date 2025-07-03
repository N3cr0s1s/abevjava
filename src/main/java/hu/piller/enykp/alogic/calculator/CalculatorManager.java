package hu.piller.enykp.alogic.calculator;

import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.calculator.calculator_c.GoToButton;
import hu.piller.enykp.alogic.calculator.calculator_c.LookupListModel;
import hu.piller.enykp.alogic.calculator.lookup.ILookupListProvider;
import hu.piller.enykp.alogic.calculator.lookup.LookupListHandler;
import hu.piller.enykp.alogic.filesaver.xml.ErrorListListener4OnyaCheck;
import hu.piller.enykp.alogic.fileutil.DataChecker;
import hu.piller.enykp.alogic.templateutils.FieldsGroups;
import hu.piller.enykp.alogic.templateutils.IFieldsGroupModel;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.extensions.db.DbFactory;
import hu.piller.enykp.extensions.elogic.ElogicCaller;
import hu.piller.enykp.extensions.elogic.IELogicResult;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IResult;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class CalculatorManager {
   public static boolean checkallstop = false;
   public static int flycount;
   public static boolean xml = false;
   private static boolean debugOn = true;
   public static final Integer ERR_CM = new Integer(4528);
   public static final String MUVELET_FULL_CALC = "importxkr";

   public static CalculatorManager getInstance() {
      return new CalculatorManager();
   }

   public void formcheck() {
      PropertyList.getInstance().set("veto_check", Boolean.TRUE);
      GuiUtil.startErrorListDialog(this);
   }

   public void end_formcheck() {
      PropertyList.getInstance().set("veto_check", Boolean.FALSE);
      GuiUtil.eld = null;
      GuiUtil.meld = null;
   }

   public void multiformcheck() {
      PropertyList.getInstance().set("veto_check", Boolean.TRUE);
      GuiUtil.startMultiErrorListDialog(this);
   }

   public void check_stop(Object var1) {
   }

   public void closeDialog() {
      GuiUtil.closeDialog();
   }

   public boolean isDataCheckerError(BookModel var1) {
      Result var2 = DataChecker.getInstance().hyperCheck(var1, var1.getCalcelemindex());

      for(int var3 = 0; var3 < var2.errorList.size(); ++var3) {
         TextWithIcon var4 = (TextWithIcon)var2.errorList.get(var3);
         String var5 = var4.toString();
         String var6 = "";
         GoToButton var7 = new GoToButton("");
         Object[] var8 = new Object[8];
         var8[3] = var4.storeItemObject;
         var8[6] = var4.elem;
         var7.setFieldId(var8, var1, var4.elem);
         Long var9 = new Long(4001L);
         String var10 = "m002";
         if (var4.officeErrorCode != null && var4.officeErrorCode.startsWith("m")) {
            var10 = var4.officeErrorCode;
         }

         if (var4.imageType == 4) {
            var10 = null;
         }

         ErrorList.getInstance().writeError(var9, var5, var4.imageType == 1 ? ErrorList.LEVEL_FATAL_ERROR : ErrorList.LEVEL_WARNING, (Exception)null, var7, (Object)var10, (Object)var4.valueToDb);
      }

      return !var2.isOk();
   }

   public void callFormCheckWithDataChecker(BookModel var1, Object[] var2) {
      if (!this.isDataCheckerError(var1) || this.isInGeneratorMod()) {
         Calculator.getInstance().formCheck(var2);
      }

   }

   public void callFormCheckWithDataChecker(IEventListener var1, BookModel var2, Object[] var3) {
      this.setOnyaCheckListenerMode(var1, var2, true);
      this.callFormCheckWithDataChecker(var2, var3);
      this.setOnyaCheckListenerMode(var1, var2, false);
   }

   private boolean isInGeneratorMod() {
      return "10".equals(Calculator.getInstance().getBookModel().getHasznalatiMod());
   }

   public void do_check(IResult var1) {
      BookModel var2 = Calculator.getInstance().getBookModel();
      Boolean var3 = (Boolean)PropertyList.getInstance().get("prop.dynamic.dirty2");
      PropertyList.getInstance().set("prop.dynamic.dirty2_original", var3);
      PropertyList.getInstance().set("prop.dynamic.dirty2", Boolean.TRUE);
      Elem var4 = (Elem)var2.cc.getActiveObject();
      Object[] var5 = new Object[2];
      Hashtable var6 = var2.get(var4.getType()).get_short_inv_fields_ht();
      Vector var7 = new Vector();
      var7.add(var4.getType());
      var7.add(var6);
      var5[1] = var7;
      IELogicResult var8 = null;

      try {
         var8 = ElogicCaller.exec("bizonylat_ellenorzes_elott", var2);
         if (var8.getStatus() != 0) {
            throw new CalculatorManager.ElogicResultException(var8.getMessage() + " (" + var8.getStatus() + ")");
         }

         if (var8.getStatus() == 0) {
            if (var2.get_main_formmodel().id.equalsIgnoreCase(var2.get_formid())) {
               var8 = ElogicCaller.exec("fobizonylat_ellenorzes", var2);
            } else {
               var8 = ElogicCaller.exec("bizonylat_ellenorzes", var2);
            }

            if (var8.getStatus() == 0) {
               this.callFormCheckWithDataChecker(var2, var5);
               var8 = ElogicCaller.exec("bizonylat_ellenorzes_utan", var2);
            }
         }
      } catch (CalculatorManager.ElogicResultException var10) {
         DbFactory.getDbHandler().setVPMessage(var8.getMessage());
         System.out.println("Elogiccall hiba!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
      }

      var1.setResult((Object)null);
      PropertyList.getInstance().set("prop.dynamic.dirty2", var3);
      PropertyList.getInstance().set("prop.dynamic.dirty2_original", (Object)null);
   }

   public void check_all_stop(Object var1) {
      checkallstop = true;
   }

   public int do_fly_check_all_start() {
      System.out.println("-- do_fly_check_all_start");
      System.out.println("Ellenőrzés kezdése " + new Date());
      flycount = 0;
      BookModel var1 = Calculator.getInstance().getBookModel();
      var1.calculator.eventFired("afteropen");
      PropertyList.getInstance().set("prop.dynamic.dirty2", Boolean.TRUE);
      PropertyList.getInstance().set("prop.dynamic.dirty2_original", Boolean.FALSE);
      String var2 = var1.get_main_formmodel().id;
      if (var2 == null) {
         var2 = "";
      }

      Hashtable var3 = new Hashtable();
      var3.put("event", "multi_form_switch");
      var1.calculator.eventFired("multi_start_check");
      IELogicResult var4 = ElogicCaller.exec("bizonylat_ellenorzes_elott", var1);
      if (var4.getStatus() != 0) {
         DbFactory.getDbHandler().setVPMessage(var4.getMessage());
      }

      return var4.getStatus();
   }

   public int do_fly_check_all_one(IEventListener var1) {
      System.out.println("do_fly_check_all_one");
      ++flycount;
      BookModel var2 = Calculator.getInstance().getBookModel();
      Elem var3 = (Elem)var2.cc.getActiveObject();
      Hashtable var4 = new Hashtable();
      Hashtable var5 = new Hashtable();
      Object[] var6 = new Object[2];
      var4.put("id", var3.getType());
      var4.put("guiobject", var3);
      var2.calculator.eventFired(var4);
      var5.put("name", var3.toString());
      var5.put("type", var3.getType());
      var5.put("current", new Integer(flycount));
      if (var1 != null) {
         var1.eventFired(new Event(this, var5));
      }

      Hashtable var7 = var2.get(var3.getType()).get_short_inv_fields_ht();
      Vector var8 = new Vector();
      var8.add(var3.getType());
      var8.add(var7);
      var6[1] = var8;
      if (var2.getBatchRecalcMode() && var2.get_main_formmodel().id.equalsIgnoreCase(var2.get_formid())) {
         Calculator.getInstance().mainFormCalculationsInBatchRecalc();
      }

      IELogicResult var9;
      if (var2.get_main_formmodel().id.equalsIgnoreCase(var2.get_formid())) {
         var9 = ElogicCaller.exec("fobizonylat_ellenorzes", var2);
      } else {
         var9 = ElogicCaller.exec("bizonylat_ellenorzes", var2);
      }

      if (0 == var9.getStatus()) {
         this.callFormCheckWithDataChecker(var2, var6);
      }

      if (var9.getStatus() != 0) {
         DbFactory.getDbHandler().setVPMessage(var9.getMessage());
      }

      return var9.getStatus();
   }

   public int do_fly_check_all_stop() {
      System.out.println("do_fly_check_all_stop ");
      System.out.println("Ellenőrzés vége " + new Date());
      BookModel var1 = Calculator.getInstance().getBookModel();
      var1.calculator.eventFired("multi_stop_check");
      IELogicResult var2 = ElogicCaller.exec("bizonylat_ellenorzes_utan", var1);
      var1.calculator.eventFired("afterclose");
      if (var2.getStatus() != 0) {
         DbFactory.getDbHandler().setVPMessage(var2.getMessage());
      }

      return var2.getStatus();
   }

   public void do_check_all(IResult var1, IEventListener var2) {
      long var3 = this.debugInfo("CalculatorManager.do_check_all start", 0L);
      long var5 = this.debugMemInfo("CalculatorManager.do_check_all start", 0L);
      Object[] var7 = new Object[2];
      BookModel var8 = Calculator.getInstance().getBookModel();
      var8.calculator.eventFired("afteropen");
      Object var9 = var8.cc.getActiveObject();
      Boolean var10 = (Boolean)PropertyList.getInstance().get("prop.dynamic.dirty2");
      PropertyList.getInstance().set("prop.dynamic.dirty2_original", var10);
      PropertyList.getInstance().set("prop.dynamic.dirty2", Boolean.TRUE);
      String var11 = var8.get_main_formmodel().id;
      if (var11 == null) {
         var11 = "";
      }

      Vector var12 = new Vector();
      Hashtable var13 = new Hashtable();
      Hashtable var14 = new Hashtable();
      var14.put("event", "multi_form_switch");
      int var15 = var8.cc.size();
      int var16 = 1;
      var8.calculator.eventFired("multi_start_check");
      checkallstop = false;
      IELogicResult var17 = null;

      try {
         var8.setCalcelemindex(var8.get_main_index());
         var17 = ElogicCaller.exec("bizonylat_ellenorzes_elott", var8);
         if (var17.getStatus() != 0) {
            throw new CalculatorManager.ElogicResultException(var17.getMessage() + " (" + var17.getStatus() + ")");
         }

         int var18;
         for(var18 = 0; var18 < var15 && !checkallstop; ++var18) {
            if (var18 % 1000 == 0) {
               System.out.println("Szabad Memória = " + Runtime.getRuntime().freeMemory() / 1024L / 1024L + " MB");
            }

            Elem var19 = (Elem)var8.cc.get(var18);
            var8.setCalcelemindex(var18);
            if (var11.equals(var19.getType())) {
               var12.add(new Integer(var18));
            } else {
               var13.put("id", var19.getType());
               var13.put("guiobject", var19);
               var8.calculator.eventFired(var13);
               var14.put("name", var19.toString());
               var14.put("type", var19.getType());
               var14.put("current", new Integer(var16++));
               var2.eventFired(new Event(this, var14));
               Hashtable var20 = var8.get(var19.getType()).get_short_inv_fields_ht();
               Vector var21 = new Vector();
               var21.add(var19.getType());
               var21.add(var20);
               var7[1] = var21;
               var17 = ElogicCaller.exec("bizonylat_ellenorzes", var8);
               if (var17.getStatus() != 0) {
                  throw new CalculatorManager.ElogicResultException(var17.getMessage() + " (" + var17.getStatus() + ")");
               }

               this.callFormCheckWithDataChecker(var2, var8, var7);
            }
         }

         for(var18 = 0; var18 < var12.size(); ++var18) {
            int var25 = (Integer)var12.get(var18);
            Elem var26 = (Elem)var8.cc.get(var25);
            var8.setCalcelemindex(var25);
            var13.put("id", var26.getType());
            var13.put("guiobject", var26);
            var8.calculator.eventFired(var13);
            var14.put("name", var26.toString());
            var14.put("type", var26.getType());
            var14.put("current", new Integer(var16++));
            var2.eventFired(new Event(this, var14));
            Hashtable var27 = var8.get(var26.getType()).get_short_inv_fields_ht();
            Vector var22 = new Vector();
            var22.add(var26.getType());
            var22.add(var27);
            var7[1] = var22;
            var17 = ElogicCaller.exec("fobizonylat_ellenorzes", var8);
            if (var17.getStatus() != 0) {
               throw new CalculatorManager.ElogicResultException(var17.getMessage() + " (" + var17.getStatus() + ")");
            }

            this.callFormCheckWithDataChecker(var2, var8, var7);
            var17 = ElogicCaller.exec("bizonylat_ellenorzes_utan", var8);
         }
      } catch (CalculatorManager.ElogicResultException var23) {
         DbFactory.getDbHandler().setVPMessage(var17.getMessage());
         System.out.println("Elogiccall hiba!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
      }

      var8.calculator.eventFired("multi_stop_check");
      PropertyList.getInstance().set("prop.dynamic.dirty2", var10);
      PropertyList.getInstance().set("prop.dynamic.dirty2_original", (Object)null);
      if (var1 != null) {
         var1.setResult((Object)null);
      }

      var8.cc.setActiveObject(var9);
      Elem var24 = (Elem)var8.cc.getActiveObject();
      if (var24 != null) {
         var13.put("id", var24.getType());
         var13.put("guiobject", var24);
         var8.calculator.eventFired(var13);
      }

      var8.calculator.eventFired("afterclose");
      this.debugInfo("CalculatorManager.do_check_all start", var3);
      this.debugMemInfo("CalculatorManager.do_check_all start", var5);
   }

   public void do_field_calculation(String var1, String var2) {
      this.calc_field(var1, var2, 0, (Object)null);
   }

   public Object[] check_field(String var1, int var2, Object var3) {
      Object[] var4 = new Object[]{var1, new Integer(var2), null, var3, null, null};
      Calculator.getInstance().fieldCheck(var4, (StoreItem)null);
      return var4;
   }

   public Object[] check_field(String var1, int var2, Object var3, String var4) {
      StoreItem var5 = new StoreItem(var1, var2, var4 == null ? "" : var4);
      Object[] var6 = new Object[]{var1, new Integer(var2), null, var3, null, null};
      Calculator.getInstance().fieldCheck(var6, var5);
      return var6;
   }

   public LookupListModel get_field_lookup_create(String var1, int var2) {
      return Calculator.getInstance().getFieldCreateLookup(new Object[]{var1, new Integer(var2), null, var2 + "_" + var1, null, null});
   }

   public void feltetelesErtekPreCheck() {
      Calculator.getInstance().feltetelesErtekPreCheck();
   }

   public Object[] check_page(String var1) {
      this.send_signal(Calculator.getInstance().getBookModel());
      Object[] var2 = new Object[]{var1, null, null, null};
      Calculator.getInstance().pageCheck(var2);
      return var2;
   }

   public void calc_field(String var1, String var2, int var3, Object var4) {
      try {
         IFieldsGroupModel var5 = FieldsGroups.getInstance().getFieldsGroupByFid(FieldsGroups.GroupType.ALL, var1, var2);
         if (var5 != null) {
            Hashtable var6 = var5.getFidsColumns();
            Enumeration var7 = var6.keys();

            while(var7.hasMoreElements()) {
               String var8 = (String)var7.nextElement();
               Object[] var9 = new Object[]{var8, new Integer(var3), null, var4, null, null};
               Calculator.getInstance().fieldDoDependentCalculations(var9);
            }
         } else {
            Object[] var11 = new Object[]{var2, new Integer(var3), null, var4, null, null};
            Calculator.getInstance().fieldDoDependentCalculations(var11);
         }
      } catch (Exception var10) {
         ErrorList.getInstance().writeError(ERR_CM, "CalculatorManager.calc_field error", var10, var2);
      }

   }

   public void calcReszbizonylatFuggosegek(String var1, String var2) {
      if (Calculator.getInstance().getBookModel().get_main_formmodel().id.equalsIgnoreCase(var1) && this.isFoAdatDependency(var1, var2)) {
         this.calculateAllSubFormDependency(var1, var2);
      }

   }

   Set<String> collectDependentFidsOnMainForm(String var1, String var2) {
      HashSet var3 = new HashSet();

      try {
         Set var6 = Calculator.getInstance().getCalculatorFieldDependencies(var1, var2);
         var6.add(var2);
         return var6;
      } catch (Exception var5) {
         return var3;
      }
   }

   Vector<String> collectDependentFidsOnSubForm(String var1, String var2) {
      Vector var3 = new Vector();

      try {
         HashSet var4 = new HashSet();
         Set var5 = this.collectDependentFidsOnMainForm(var1, var2);
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();
            var4.addAll(this.getSubFormFoAdatDependency(var1, var7));
         }

         var3 = Calculator.getInstance().createCalcOrderFieldList(var1, var4);
         return var3;
      } catch (Exception var8) {
         return var3;
      }
   }

   private void calculateAllSubFormDependency(String var1, String var2) {
      Vector var3 = this.collectDependentFidsOnSubForm(var1, var2);
      if (!var3.isEmpty()) {
         long var4 = this.debugInfo("CalculatorManager.foadat start " + CalculatorManager.CalcType.ALL_FIELDS, 0L);
         long var6 = this.debugMemInfo("CalculatorManager.foadat start " + CalculatorManager.CalcType.ALL_FIELDS, 0L);
         BookModel var8 = Calculator.getInstance().getBookModel();
         Object var9 = var8.cc.getActiveObject();
         String var10 = var8.get_main_formmodel().id;
         if (var10 == null) {
            var10 = "";
         }

         Vector var11 = new Vector();
         Hashtable var12 = new Hashtable();
         int var13 = var8.cc.size();
         checkallstop = false;

         for(int var14 = 0; var14 < var13 && !checkallstop; ++var14) {
            Elem var15 = (Elem)var8.cc.get(var14);
            var8.setCalcelemindex(var14);
            if (var10.equals(var15.getType())) {
               var11.add(new Integer(var14));
            } else {
               var12.put("id", var15.getType());
               var12.put("guiobject", var15);
               var8.calculator.eventFired(var12);
               Calculator.getInstance().calculateListOfFields(var3);
            }
         }

         var8.cc.setActiveObject(var9);
         Elem var16 = (Elem)var8.cc.getActiveObject();
         if (var16 != null) {
            var12.put("id", var16.getType());
            var12.put("guiobject", var16);
            var8.calculator.eventFired(var12);
         }

         PropertyList.getInstance().set("prop.dynamic.dirty2", Boolean.FALSE);
         this.debugInfo("CalculatorManager.foadat stop " + CalculatorManager.CalcType.ALL_FIELDS, var4);
         this.debugMemInfo("CalculatorManager.foadat stop " + CalculatorManager.CalcType.ALL_FIELDS, var6);
      }
   }

   public boolean FillGroupFields(String var1, IDataStore var2, DataFieldModel var3, int var4, int var5) {
      return this.FillStaticGroupFields(var1, var2, var3, var4, var5) || this.FillDinamycGroupFields(var1, var2, var3, var4, var5);
   }

   public boolean FillStaticGroupFields(String var1, IDataStore var2, DataFieldModel var3, int var4, int var5) {
      try {
         IFieldsGroupModel var6 = FieldsGroups.getInstance().getFieldsGroupByFid(FieldsGroups.GroupType.STATIC, var1, var3.key);
         if (var6 == null) {
            return false;
         } else {
            Hashtable var7 = var6.getFidsColumns();
            Enumeration var8 = var7.keys();
            if (!this.handleEmptyBrotherFields(var7, var2, var3, var5, var4)) {
               while(var8.hasMoreElements()) {
                  String var9 = (String)var8.nextElement();
                  if (!var9.equalsIgnoreCase(var3.key)) {
                     String var10 = (String)var7.get(var9);
                     List var11 = LookupListHandler.getInstance().getLookupListProvider(var1, var9).getTableView(var5, var10);
                     String var12 = (String)var11.get(var4);
                     var2.set(var5 + "_" + var9, var12);
                  }
               }
            }

            return true;
         }
      } catch (Exception var13) {
         ErrorList.getInstance().writeError(ERR_CM, "CalculatorManager.FillGroupFields error ", var13, var4);
         return true;
      }
   }

   public boolean FillDinamycGroupFields(String var1, IDataStore var2, DataFieldModel var3, int var4, int var5) {
      try {
         String var6 = var3.key;
         IFieldsGroupModel var7 = FieldsGroups.getInstance().getFieldsGroupByFid(FieldsGroups.GroupType.DINAMYC, var1, var6);
         if (var7 == null) {
            return false;
         } else {
            Hashtable var8 = var7.getFidsColumns();
            if (!this.handleEmptyBrotherFields(var8, var2, var3, var5, var4)) {
               LookupListHandler var9 = LookupListHandler.getInstance();
               ILookupListProvider var10 = var9.getLookupListProvider(var1, var6);
               String var11 = var10.getFieldList();
               List var12 = var9.getLookupListProvider(var1, var6).getTableView(var5, var11);
               String var13 = (String)var12.get(var4);
               String[] var14 = var13.split("  -  ");
               Vector var15 = this.createColIndex(var10.createFieldList(var11));
               Enumeration var16 = var8.keys();

               while(var16.hasMoreElements()) {
                  String var17 = (String)var16.nextElement();
                  if (!var17.equalsIgnoreCase(var6)) {
                     ILookupListProvider var18 = var9.getLookupListProvider(var1, var17);
                     var18.getTableView(var5, "1");
                     String var19 = var18.getFieldCol();
                     var13 = var14[(Integer)var15.get(Integer.parseInt(var19) - 1)];
                     var2.set(var5 + "_" + var17, var13);
                  }
               }
            }

            return true;
         }
      } catch (Exception var20) {
         ErrorList.getInstance().writeError(ERR_CM, "CalculatorManager.FillGroupFields errot ", var20, var4);
         return true;
      }
   }

   private Vector<Integer> createColIndex(int[] var1) {
      int var2 = 0;
      int[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int var6 = var3[var5];
         if (var6 > var2) {
            var2 = var6;
         }
      }

      Vector var7 = new Vector(var2);
      var7.setSize(var2 + 1);

      for(var4 = 0; var4 < var1.length; ++var4) {
         var7.insertElementAt(var4, var1[var4]);
      }

      return var7;
   }

   public void form_calc() {
      long var1 = this.debugInfo("CalculatorManager.form_calc start", 0L);
      Calculator.getInstance().formDoCalculations((Object)null);
      PropertyList.getInstance().set("prop.dynamic.dirty2", Boolean.FALSE);
      this.debugInfo("CalculatorManager.form_calc stop", var1);
   }

   public void page_calc(String var1, int var2) {
      long var3 = this.debugInfo("CalculatorManager.page_calc start", 0L);
      Calculator.getInstance().formDoPageCalculations(var1, var2);
      this.debugInfo("CalculatorManager.page_calc stop", var3);
   }

   public void multiform_calc() {
      BookModel var1 = Calculator.getInstance().getBookModel();
      if (var1.isXkr_mode() && !"importxkr".equalsIgnoreCase(var1.getMuvelet())) {
         this.multiform_technicals_fields_calc();
      } else {
         this.multiform_all_fields_calc();
      }

   }

   public void multiform_all_fields_calc() {
      this.multiform_specific_calc(CalculatorManager.CalcType.ALL_FIELDS);
   }

   public void multiform_technicals_fields_calc() {
      this.multiform_specific_calc(CalculatorManager.CalcType.TECHNICAL_FIELDS);
   }

   private void multiform_specific_calc(CalculatorManager.CalcType var1) {
      long var2 = this.debugInfo("CalculatorManager.multiform_calc start " + var1, 0L);
      long var4 = this.debugMemInfo("CalculatorManager.multiform_calc start " + var1, 0L);
      BookModel var6 = Calculator.getInstance().getBookModel();
      Object var7 = var6.cc.getActiveObject();
      String var8 = var6.get_main_formmodel().id;
      if (var8 == null) {
         var8 = "";
      }

      Vector var9 = new Vector();
      Hashtable var10 = new Hashtable();
      int var11 = var6.cc.size();
      var6.calculator.eventFired("multi_start_calc");
      checkallstop = false;

      int var12;
      for(var12 = 0; var12 < var11 && !checkallstop; ++var12) {
         Elem var13 = (Elem)var6.cc.get(var12);
         var6.setCalcelemindex(var12);
         if (var8.equals(var13.getType())) {
            var9.add(new Integer(var12));
         } else {
            var10.put("id", var13.getType());
            var10.put("guiobject", var13);
            var6.calculator.eventFired(var10);
            this.specificCalc(var1);
         }
      }

      for(var12 = 0; var12 < var9.size(); ++var12) {
         int var16 = (Integer)var9.get(var12);
         Elem var14 = (Elem)var6.cc.get(var16);
         var6.setCalcelemindex(var16);
         var10.put("id", var14.getType());
         var10.put("guiobject", var14);
         var6.calculator.eventFired(var10);
         this.specificCalc(var1);
      }

      var6.calculator.eventFired("multi_stop_calc");
      var6.cc.setActiveObject(var7);
      Elem var15 = (Elem)var6.cc.getActiveObject();
      if (var15 != null) {
         var10.put("id", var15.getType());
         var10.put("guiobject", var15);
         var6.calculator.eventFired(var10);
      }

      PropertyList.getInstance().set("prop.dynamic.dirty2", Boolean.FALSE);
      this.debugInfo("CalculatorManager.multiform_calc stop " + var1, var2);
      this.debugMemInfo("CalculatorManager.multiform_calc stop " + var1, var4);
   }

   private void specificCalc(CalculatorManager.CalcType var1) {
      switch(var1) {
      case ALL_FIELDS:
         Calculator.getInstance().formDoCalculations((Object)null);
         break;
      case TECHNICAL_FIELDS:
         this.form_notbelfeld_fields_calc();
      }

   }

   private long debugInfo(String var1, long var2) {
      if (debugOn) {
         long var4 = System.currentTimeMillis();
         System.out.println(var1 + (var2 == 0L ? "" : " Eltelt idő: " + (var4 - var2) / 1000L + " másodperc"));
         return var4;
      } else {
         return 0L;
      }
   }

   private long debugMemInfo(String var1, long var2) {
      if (debugOn) {
         long var4 = Runtime.getRuntime().freeMemory();
         System.out.println("FreeMem=" + var4 / 1024L / 1024L + " MB");
         System.out.println(var1 + (var2 == 0L ? "" : " Memória változás: " + (var4 - var2) / 1000L + " kilobyte"));
         return var4;
      } else {
         return 0L;
      }
   }

   public void do_dpage_count() {
      long var1 = this.debugInfo("CalculatorManager.do_dpage_count start", 0L);
      BookModel var3 = Calculator.getInstance().getBookModel();
      this.send_signal(var3);

      try {
         var3.calculator.notifyReadOnlyFieldsCalc(Boolean.TRUE, 0);
         var3.calculator.fireCalculations(new Object[]{new String[]{"on_event"}, new String[]{"dpage_count_change"}});
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         var3.calculator.notifyReadOnlyFieldsCalc(Boolean.FALSE, 0);
      }

      this.debugInfo("CalculatorManager.do_dpage_count stop", var1);
   }

   public void multi_form_load() {
      long var1 = this.debugInfo("CalculatorManager.multi_form_load start", 0L);
      BookModel var3 = Calculator.getInstance().getBookModel();
      this.send_signal(var3);
      var3.calculator.fireCalculations(new Object[]{new String[]{"on_event"}, new String[]{"multi_form_load"}});
      this.debugInfo("CalculatorManager.multi_form_load stop", var1);
   }

   public boolean doBetoltErtekCalcs(boolean var1) {
      try {
         return this.doBetoltErtekCalcsCore(var1);
      } catch (Exception var3) {
         System.out.println("doBetoltErtekCalcs hiba!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
         return false;
      }
   }

   public boolean doBetoltErtekCalcsCore(boolean var1) {
      try {
         long var2 = this.debugInfo("CalculatorManager.doBetoltErtekCalcsCore start", 0L);
         BookModel var4 = Calculator.getInstance().getBookModel();
         this.send_signal(var4);
         boolean var5 = var4.calculator.doBetoltErtekCalcs(var1);
         this.debugInfo("CalculatorManager.doBetoltErtekCalcsCore stop", var2);
         return var5;
      } catch (Exception var6) {
         System.out.println("doBetoltErtekCalcsCore hiba!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
         return false;
      }
   }

   public void form_hidden_fields_calc() {
      long var1 = this.debugInfo("CalculatorManager.form_hidden_fields_calc start", 0L);
      long var3 = System.nanoTime();
      BookModel var5 = Calculator.getInstance().getBookModel();
      this.send_signal(var5);
      Calculator.getInstance().formHiddenFieldsDoCalculations();
      long var6 = System.nanoTime();
      this.debugInfo("CalculatorManager.form_hidden_fields_calc stop", var1);
      System.out.println("nano=" + (var6 - var3) + "  " + (var6 - var3) / 1000000L);
   }

   public void form_notbelfeld_fields_calc() {
      long var1 = this.debugInfo("CalculatorManager.form_notbelfeld_fields_calc", 0L);
      long var3 = System.nanoTime();
      BookModel var5 = Calculator.getInstance().getBookModel();
      this.send_signal(var5);
      Calculator.getInstance().formNotInBelFeldFieldsDoCalculations();
      long var6 = System.nanoTime();
      this.debugInfo("CalculatorManager.form_notbelfeld_fields_calc", var1);
      System.out.println("nano=" + (var6 - var3) + "  " + (var6 - var3) / 1000000L);
   }

   public void page_fields_visibility_calc(String var1, String var2, Integer var3) {
      BookModel var4 = Calculator.getInstance().getBookModel();
      this.send_signal(var4);
      Calculator.getInstance().pageFieldsVisibilityCalc(var1, var2, var3);
   }

   private void send_signal(BookModel var1) {
      Hashtable var2 = new Hashtable();
      Elem var3 = (Elem)var1.cc.get(var1.getCalcelemindex());
      var2.put("id", var3.getType());
      var2.put("guiobject", var3);
      var1.calculator.eventFired(var2);
   }

   public void multiform_calc_main() {
      long var1 = this.debugInfo("CalculatorManager.multiform_calc_main start", 0L);
      BookModel var3 = Calculator.getInstance().getBookModel();
      Object var4 = var3.cc.getActiveObject();
      String var5 = var3.get_main_formmodel().id;
      if (var5 == null) {
         var5 = "";
      }

      Vector var6 = new Vector();
      Hashtable var7 = new Hashtable();
      int var8 = var3.cc.size();
      boolean var9 = true;
      checkallstop = false;

      int var10;
      for(var10 = 0; var10 < var8 && !checkallstop; ++var10) {
         Elem var11 = (Elem)var3.cc.get(var10);
         var3.setCalcelemindex(var10);
         if (var5.equals(var11.getType())) {
            var6.add(new Integer(var10));
         }
      }

      for(var10 = 0; var10 < var6.size(); ++var10) {
         int var14 = (Integer)var6.get(var10);
         Elem var12 = (Elem)var3.cc.get(var14);
         var3.setCalcelemindex(var14);
         var7.put("id", var12.getType());
         var7.put("guiobject", var12);
         var3.calculator.eventFired(var7);
         Calculator.getInstance().formDoCalculations((Object)null);
      }

      var3.cc.setActiveObject(var4);
      Elem var13 = (Elem)var3.cc.getActiveObject();
      if (var13 != null) {
         var7.put("id", var13.getType());
         var7.put("guiobject", var13);
         var3.calculator.eventFired(var7);
      }

      PropertyList.getInstance().set("prop.dynamic.dirty2", Boolean.FALSE);
      this.debugInfo("CalculatorManager.multiform_calc_main stop", var1);
   }

   public int ubev_signal(String var1, BookModel var2) {
      IELogicResult var3 = ElogicCaller.exec(var1, var2);
      if (var3.getStatus() != 0) {
         DbFactory.getDbHandler().setVPMessage(var3.getMessage());
      }

      return var3.getStatus();
   }

   public Hashtable<String, String> get_rogz_calc_fids_list(String var1) {
      return Calculator.getInstance().get_rogz_calc_fids_list(var1);
   }

   public Hashtable<String, String> get_rogz_stat_exc_fids_list(String var1) {
      return Calculator.getInstance().get_rogz_stat_exc_fids_list(var1);
   }

   public Map<String, String> calculateVariables(String var1) {
      return Calculator.getInstance().calculateVariables(var1);
   }

   public String getExpressionValue(IDataStore var1, String var2, String var3, int var4) throws Exception {
      return Calculator.getInstance().getExpressionValue(var1, var2, var3, var4);
   }

   private void setOnyaCheckListenerMode(IEventListener var1, BookModel var2, boolean var3) {
      if (var1 != null) {
         if (var2.isOnyaCheckMode()) {
            try {
               ((ErrorListListener4OnyaCheck)var1).setAnykTemplateCheckRunning(var3);
            } catch (Exception var5) {
            }
         }

      }
   }

   public boolean isFoAdatDependency(String var1, String var2) {
      return Calculator.getInstance().isFoAdatDependency(var1, var2);
   }

   public Set<String> getSubFormFoAdatDependency(String var1, String var2) {
      return Calculator.getInstance().getSubFormFoAdatDependency(var1, var2);
   }

   private boolean handleEmptyBrotherFields(Hashtable<String, String> var1, IDataStore var2, DataFieldModel var3, int var4, int var5) {
      if (var2.get(var4 + "_" + var3.key) != null && !"".equals(var2.get(var4 + "_" + var3.key))) {
         return var5 == -1;
      } else {
         Enumeration var6 = var1.keys();

         while(var6.hasMoreElements()) {
            String var7 = (String)var6.nextElement();
            if (!var7.equalsIgnoreCase(var3.key)) {
               var2.set(var4 + "_" + var7, "");
            }
         }

         return true;
      }
   }

   public void runDPageNumberCalcs() {
      Calculator.getInstance().runDPageNumberCalcs();
   }

   private class ElogicResultException extends Exception {
      public ElogicResultException(String var2) {
         super(var2);
      }

      public ElogicResultException() {
      }
   }

   public static enum CalcType {
      ALL_FIELDS,
      TECHNICAL_FIELDS;
   }
}
