package hu.piller.enykp.alogic.calculator.lookup;

import java.util.List;

public interface ILookupListProvider {
   List<String> getTableView(int var1, String var2) throws Exception;

   LookupList getSortedTableView(int var1) throws Exception;

   boolean validate(int var1, String var2);

   void release();

   String getFieldCol();

   String getFieldList();

   int[] createFieldList(String var1) throws Exception;
}
