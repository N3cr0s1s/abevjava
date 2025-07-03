package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0;

public class FnDescHelper {
   public static final int IDX_STRUCTURE_ID = 0;
   public static final int IDX_EXP_OPERATION_ID = 1;
   public static final int IDX_OPER_ID = 2;
   public static final int IDX_SOURCE = 3;
   public static final int IDX_PARAMS_CALC_FIRST = 4;
   public static final int IDX_IS_BE_MEMBER_OF_DEPENDENCY_LIST = 5;

   public static Object[] getFnDescType(Object var0) {
      return (Object[])((Object[])var0);
   }

   public static Object getOperationId(Object var0) {
      Object[] var1 = (Object[])((Object[])var0);
      return var1 != null ? var1[2] : null;
   }

   public static Object getSource(Object var0) {
      Object[] var1 = (Object[])((Object[])var0);
      return var1 != null ? var1[3] : null;
   }

   public static Boolean isParamsCalcFirst(Object var0) {
      return (Boolean)((Object[])((Object[])var0))[4];
   }

   public static Boolean isBeMemberOfDependencyList(Object var0) {
      return (Boolean)((Object[])((Object[])var0))[5];
   }
}
