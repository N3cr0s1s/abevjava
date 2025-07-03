package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0;

public class OperHelper {
   private static final Integer STRUCTURE_ID = new Integer(0);
   private static final Integer EXP_OPERATION_ID = new Integer(4);
   public static final Integer IDX_STRUCTURE_ID = new Integer(0);
   public static final Integer IDX_OPERATION_ID = new Integer(1);
   public static final Integer IDX_OPER_ID = new Integer(2);
   public static final Integer IDX_SOURCE_ID = new Integer(3);
   public static final int IDX_PARAMS_CALC_FIRST = 4;
   public static final int IDX_IS_BE_MEMBER_OF_DEPENDENCY_LIST = 5;

   public static Object createOperation(Object var0, Object var1, Boolean var2, Boolean var3, Boolean var4) {
      if (var2 == null) {
         var2 = Boolean.TRUE;
      }

      if (var3 == null) {
         var3 = Boolean.FALSE;
      }

      if (var4 == null) {
         var4 = Boolean.FALSE;
      }

      return new Object[]{STRUCTURE_ID, EXP_OPERATION_ID, var0, var1, var2, var3, var4};
   }
}
