package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.util.base.Tools;

public class FnDescHelper {
   public static final int IDX_STRUCTURE_ID = 0;
   public static final int IDX_EXP_OPERATION_ID = 1;
   public static final int IDX_OPER_ID = 2;
   public static final int IDX_SOURCE = 3;
   public static final int IDX_PARAMS_CALC_FIRST = 4;
   public static final int IDX_IS_BE_MEMBER_OF_DEPENDENCY_LIST = 5;
   public static final int IDX_IS_NEED_NOTIFY_ON_CHECK = 6;

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
      Object[] var1 = (Object[])((Object[])var0);
      if (var1 != null) {
         Object var2 = var1[4];
         return var2 instanceof Boolean ? (Boolean)var2 : null;
      } else {
         return null;
      }
   }

   public static Boolean isBeMemberOfDependencyList(Object var0) {
      try {
         Object[] var1 = (Object[])((Object[])var0);
         if (var1 != null) {
            Object var2 = var1[5];
            return var2 instanceof Boolean ? (Boolean)var2 : null;
         }
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
      }

      return null;
   }

   public static Boolean isNeedNotifyOnCheck(Object var0) {
      try {
         Object[] var1 = (Object[])((Object[])var0);
         if (var1 != null) {
            Object var2 = var1[6];
            return var2 instanceof Boolean ? (Boolean)var2 : null;
         }
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
      }

      return null;
   }
}
