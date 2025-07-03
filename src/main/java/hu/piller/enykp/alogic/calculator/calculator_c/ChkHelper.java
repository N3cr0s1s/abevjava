package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.util.base.Tools;

public class ChkHelper {
   public static final int IDX_FIELD_CALCULATION = 0;
   public static final int IDX_FIELD_CHECKS = 1;
   public static final int IDX_FIELD_CHECK_MSG_LEVEL = 2;

   public static Object[] getCheckCalc(Object var0) {
      return (Object[])((Object[])((Object[])((Object[])var0))[0]);
   }

   public static String getCheckMsg(Object var0) {
      return (String)((Object[])((Object[])var0))[1];
   }

   public static Integer getCheckMsgLevel(Object var0) {
      try {
         return (Integer)((Object[])((Object[])var0))[2];
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
         return null;
      }
   }
}
