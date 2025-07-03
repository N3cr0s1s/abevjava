package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0;

import hu.piller.enykp.alogic.calculator.calculator_c.ExpClass;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IPropertyList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public interface IFunctionSet {
   boolean setStartFullcheck();

   boolean setStartFullcalc();

   boolean isStartFullcheck();

   boolean isStartFullcalc();

   boolean setStopFullcheck();

   boolean setStopFullcalc();

   boolean setMultiStartCalc();

   boolean setMultiStopCalc();

   boolean setMultiStartCheck();

   boolean setMultiStopCheck();

   boolean setFieldTypes(Hashtable var1);

   boolean setFunctionDescriptions(IPropertyList var1, String var2);

   boolean setVariables(IPropertyList var1, String var2);

   boolean setFormId(String var1);

   boolean setDataStore(IDataStore var1);

   boolean setReadonlyFieldCalcState(Boolean var1, Integer var2);

   Object getDependency(Object[] var1);

   Object setCheckDept(Object[] var1);

   Object[] getFunctionList();

   Object getFunctionDescriptors(String var1);

   Object calcExpression(Object var1, Object var2, ExpClass var3, Object[] var4, String var5, IDataStore var6);

   void init(BookModel var1);

   Hashtable getSpecialDependencies(String var1);

   void setFormCheck(boolean var1);

   boolean isToleranced(String var1, String var2, String var3, String var4);

   boolean isCachedTargetId(String var1, String var2, String var3);

   String getExtendedError();

   String getRoundedValue(String var1, Object var2);

   Iterator getExpressionFieldsList();

   void setPreviousItem(StoreItem var1);

   Vector getReadOnlyCalcFieldsList(String var1, String var2);

   int getPrecisionForKihatas(String var1);

   HashSet getFeltetelesErtekFieldsList(String var1);
}
