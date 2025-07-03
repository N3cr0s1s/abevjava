package hu.piller.enykp.alogic.calculator.calculator_c;

public class ExpHelper {
   public static final int INVALID = -1;
   public static final int EXP_EMPTY = 0;
   public static final int EXP_CONSTANT = 1;
   public static final int EXP_VARIABLE = 2;
   public static final int EXP_EXPRESSION = 3;
   public static final int EXP_OPERATION = 4;
   public static final int ZERUS = -2;
   public static final int NIL = 0;
   public static final int STRING = 1;
   public static final int NUMBER = 2;
   public static final int LOGICAL = 4;
   public static final int IDX_STRUCTURE_ID = 0;
   public static final int IDX_EXPTYPE = 1;
   public static final int IDX_TYPE = 2;
   public static final int IDX_RESULT = 3;
   public static final int IDX_ERROR = 4;
   public static final int IDX_CALC_SOURCE = 5;
   public static final int IDX_GUI_SOURCE = 6;
   public static final int IDX_FLAG = 7;
   public static final int IDX_IDENTIFIER = 8;
   public static final int IDX_FN_SOURCE = 9;
   public static final int IDX_FIRSTPARAMETERIDX = 10;

   public static int getStructure(Object[] var0) {
      return (Integer)var0[0];
   }

   public static void setStructure(Object[] var0, int var1) {
      var0[0] = new Integer(var1);
   }

   public static int getExpType(Object[] var0) {
      return (Integer)var0[1];
   }

   public static void setExpType(Object[] var0, int var1) {
      var0[1] = new Integer(var1);
   }

   public static int getType(Object[] var0) {
      return (Integer)var0[2];
   }

   public static void setType(Object[] var0, int var1) {
      var0[2] = new Integer(var1);
   }

   public static Object getValue(Object[] var0) {
      return var0[3];
   }

   public static void setValue(Object[] var0, Object var1) {
      var0[3] = var1;
   }

   public static Object getResult(Object[] var0) {
      return var0[3];
   }

   public static void setResult(Object[] var0, Object var1) {
      var0[3] = var1;
   }

   public static Object getSource(Object[] var0) {
      return var0.length > 9 ? var0[9] : null;
   }

   public static void setSource(Object[] var0, Object var1) {
      if (var0.length > 9) {
         var0[9] = var1;
      }

   }

   public static Object getCalcSource(Object[] var0) {
      return var0[5];
   }

   public static void setCalcSource(Object[] var0, Object var1) {
      var0[5] = var1;
   }

   public static Object getGUISource(Object[] var0) {
      return var0[6];
   }

   public static void setGUISource(Object[] var0, Object var1) {
      var0[6] = var1;
   }

   public static String getIdentidier(Object[] var0) {
      return (String)var0[8];
   }

   public static void setIdentidier(Object[] var0, String var1) {
      var0[8] = var1;
   }

   public static Integer getFlag(Object[] var0) {
      return (Integer)var0[7];
   }

   public static void setFlag(Object[] var0, Integer var1) {
      var0[7] = var1;
   }

   public static Integer getForwardedFlag(Integer var0, Object var1) {
      if (var1 instanceof Number && var0 != null) {
         Number var2 = (Number)var1;
         int var3 = var2.intValue();
         int var4 = var0;
         if ((var4 & -2) == -2 && var3 == 0) {
            return var0;
         }
      }

      return null;
   }

   public static void setError(Object[] var0, Object var1) {
      var0[4] = var1;
   }

   public static Object getError(Object[] var0) {
      return var0[4];
   }

   public static int getParametersCount(Object[] var0) {
      return getExpType(var0) != 3 ? 0 : var0.length - 10;
   }

   public static Object[] getParameter(Object[] var0, int var1) {
      if (getExpType(var0) != 3) {
         return null;
      } else {
         return 10 + var1 < var0.length ? (Object[])((Object[])var0[10 + var1]) : null;
      }
   }

   public static void setParameter(Object[] var0, int var1, Object[] var2) {
      if (getExpType(var0) == 3) {
         if (10 + var1 < var0.length) {
            var0[10 + var1] = var2;
         }

      }
   }

   public static int getFirstParameterIndex() {
      return 10;
   }
}
