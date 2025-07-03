package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0;

import hu.piller.enykp.alogic.calculator.calculator_c.ExpClass;
import hu.piller.enykp.interfaces.ICalculator;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.Tools;

public class ExpFactoryLight {
   public static final String C_TRUE = "true";
   public static final String C_FALSE = "false";
   private static final String QUOTATION = "\"";
   private static final int STRUCTURE_ID = 0;
   private static final int EXP_CONSTANT_ID = 1;
   private static final String ZERUS_STR = "zérus";
   private static final String NIL_STR = "nil";
   private static final int NIL_ID = 0;
   private static final int STRING_ID = 1;
   private static final int NUMBER_ID = 2;
   private static final int LOGICAL_ID = 4;
   public static final int NO_PARAMETER = -2;
   public static final int MISSING_TYPE = -1;
   public static ICalculator calculator;
   public static IPropertyList gui;

   public static ExpClass createConstant(String var0) throws Exception {
      return createConstant(var0, -2);
   }

   public static ExpClass createConstant(String var0, int var1) throws Exception {
      ExpClass var2 = createConstant_(var0, var1);
      return var2;
   }

   private static ExpClass createConstant_(String var0, int var1) throws Exception {
      if (var0 == null) {
         return new ExpClass(0, 1, 0, (Object)null, (Object[])null, 0, (String)null, (Object)null);
      } else if (var0.length() == 0) {
         return var1 == 4 ? new ExpClass(0, 1, 4, Boolean.FALSE, (Object[])null, 0, (String)null, (Object)null) : new ExpClass(0, 1, 0, (Object)null, (Object[])null, 0, (String)null, (Object)null);
      } else if (var1 != 1 && var1 != -1) {
         if (var0.equalsIgnoreCase("true")) {
            return new ExpClass(0, 1, 4, Boolean.TRUE, (Object[])null, 0, (String)null, (Object)null);
         } else if (var0.equalsIgnoreCase("false")) {
            return new ExpClass(0, 1, 4, Boolean.FALSE, (Object[])null, 0, (String)null, (Object)null);
         } else if (var0.startsWith("\"") && var0.endsWith("\"")) {
            return new ExpClass(0, 1, 1, var0.substring(1, var0.length() - 1), (Object[])null, 0, (String)null, (Object)null);
         } else {
            try {
               return new ExpClass(0, 1, 2, NumericOperations.createNumber(var0), (Object[])null, 0, (String)null, (Object)null);
            } catch (NumberFormatException var3) {
               Tools.eLog(var3, 0);
               return new ExpClass(0, 1, 1, var0, (Object[])null, 0, (String)null, (Object)null);
            }
         }
      } else {
         return new ExpClass(0, 1, 1, var0, (Object[])null, 0, (String)null, (Object)null);
      }
   }

   public static Boolean createBoolean(String var0) {
      if (var0.equalsIgnoreCase("true")) {
         return Boolean.TRUE;
      } else {
         return var0.equalsIgnoreCase("false") ? Boolean.FALSE : null;
      }
   }

   public static String createStringExpression(ExpClass var0) {
      String var1 = "";
      int var2 = var0.getExpType();
      switch(var2) {
      case 0:
      default:
         break;
      case 1:
         int var3 = var0.getType();
         Object var4;
         switch(var3) {
         case 0:
            var1 = var1 + "nil";
            return var1;
         case 1:
            var4 = var0.getValue();
            if (var4 != null) {
               var1 = var1 + "\"" + var4.toString() + "\"";
            }

            return var1;
         case 2:
            var4 = var0.getValue();
            if (var4 != null) {
               var1 = var1 + var4.toString();
            }

            return var1;
         case 3:
         default:
            var1 = var1 + "{ismeretlen kifejezés}";
            return var1;
         case 4:
            var4 = var0.getValue();
            if (var4 instanceof Boolean) {
               var1 = var1 + ((Boolean)var4 ? "true" : "false");
            }

            return var1;
         }
      case 2:
         var1 = var1 + var0.getIdentifier();
         break;
      case 3:
         var1 = var1 + var0.getIdentifier() + "(";
         int var5 = 0;

         for(int var6 = var0.getParametersCount(); var5 < var6; ++var5) {
            var1 = var1 + (var5 > 0 ? ", " : "") + createStringExpression(var0.getParameter(var5));
         }

         var1 = var1 + ")";
      }

      return var1;
   }
}
