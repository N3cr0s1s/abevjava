package hu.piller.enykp.interfaces;

import hu.piller.enykp.alogic.calculator.calculator_c.ExpClass;
import hu.piller.enykp.datastore.StoreItem;
import java.util.Vector;

public interface ICalculator {
   void build(Object var1) throws Exception;

   void fieldCheck(Object var1, StoreItem var2);

   void fieldDoCalculations(Object var1);

   void fieldDoCalculations(Vector var1);

   void pageCheck(Object var1);

   void formCheck(Object var1);

   void formDoCalculations(Object var1);

   Object calculateExpression(Object var1);

   void fireCalculations(Object var1);

   void release();

   Object get_matrix_item(Object var1);

   ExpClass getExpressionDefinition(String var1);

   void writeDataStore(IDataStore var1, Object var2, String var3);

   Object getGuiFormObject();

   boolean doBetoltErtekCalcs(boolean var1);
}
