package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.IFunctionSet;
import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.NumericOperations;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.Tools;
import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Vector;

public class ExpFactory {
   private static final char BR_OPEN = '[';
   private static final char BR_CLOSE = ']';
   private static final String C_TRUE = "true";
   private static final String C_FALSE = "false";
   private static final char QUOTATION = '"';
   private static final char SEPARATOR = ',';
   private static final int SEPARATOR_LEN = 1;
   private static final String ZERUS_STR = "zérus";
   private static final String NIL_STR = "nil";
   private static final int STRUCTURE = 0;
   public static final int PH_ID_FIELD = 1;
   public static final int PH_ID_PAGE = 2;
   public static final int PH_ID_VARIABLE = 3;
   public static IPropertyList variables;
   public static IPropertyList functions;
   public static IPropertyList calculator;
   public static BookModel gui;
   public static String form_id;
   public static int exp_counter;
   private static final Hashtable fn_descriptors = new Hashtable(256);
   public static final Hashtable dependency_map = new Hashtable(512);
   private static Vector dependency_items;
   public static final Hashtable page_dependency_map = new Hashtable(512);
   private static Vector page_dependency_items;
   public static final Hashtable variable_dependency_map = new Hashtable(512);
   private static Vector variable_dependency_items;
   private static final String[] dep_field = new String[]{null};
   private static final Vector elements = new Vector(32);

   public static void release() {
      release_(false);
   }

   public static void release_(boolean var0) {
      if (dependency_map != null) {
         dependency_map.clear();
      }

      if (dependency_items != null) {
         dependency_items.clear();
      }

      if (page_dependency_map != null) {
         page_dependency_map.clear();
      }

      if (page_dependency_items != null) {
         page_dependency_items.clear();
      }

      if (variable_dependency_map != null) {
         variable_dependency_map.clear();
      }

      if (variable_dependency_items != null) {
         variable_dependency_items.clear();
      }

      dep_field[0] = null;
      if (elements != null) {
         elements.clear();
      }

      if (!var0) {
         variables = null;
         functions = null;
         calculator = null;
         gui = null;
         form_id = null;
      }

      if (fn_descriptors != null) {
         fn_descriptors.clear();
      }

   }

   public static ExpClass createExp(String var0, String var1, int var2, boolean var3, String var4) throws Exception {
      ExpClass var5 = null;
      if (var0 != null) {
         dependency_items = null;
         page_dependency_items = null;
         variable_dependency_items = null;
         if (var1 != null) {
            switch(var2) {
            case 1:
               if ((dependency_items = (Vector)dependency_map.get(var1)) == null) {
                  dependency_items = new Vector(64, 32);
                  dependency_map.put(var1, dependency_items);
               }
               break;
            case 2:
               if ((page_dependency_items = (Vector)page_dependency_map.get(var1)) == null) {
                  page_dependency_items = new Vector(64, 32);
                  page_dependency_map.put(var1, page_dependency_items);
               }
               break;
            case 3:
               if ((variable_dependency_items = (Vector)variable_dependency_map.get(var1)) == null) {
                  variable_dependency_items = new Vector(64, 32);
                  variable_dependency_map.put(var1, variable_dependency_items);
               }
            }
         }

         var5 = createExpressionRecursive(var0, dependency_items, page_dependency_items, variable_dependency_items, variables, fn_descriptors, functions, calculator, gui, form_id, elements, ',', '"', '[', ']', 1, var3, var4, true);
      }

      return var5;
   }

   private static ExpClass createExpressionRecursive(String var0, Vector var1, Vector var2, Vector var3, IPropertyList var4, Hashtable var5, IPropertyList var6, IPropertyList var7, BookModel var8, Object var9, Vector var10, char var11, char var12, char var13, char var14, int var15, boolean var16, String var17, boolean var18) throws Exception {
      var0 = var0.trim();
      ExpClass var19;
      if (var0.charAt(0) == '[' && var0.charAt(var0.length() - 1) == ']') {
         var19 = createExpression(var0.substring(1, var0.length() - 1), var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18);
      } else {
         var19 = createConstant(var0, var1, var2, var3, var4, var12);
      }

      ++exp_counter;
      return var19;
   }

   private static ExpClass createExpression(String var0, Vector var1, Vector var2, Vector var3, IPropertyList var4, Hashtable var5, IPropertyList var6, IPropertyList var7, BookModel var8, Object var9, Vector var10, char var11, char var12, char var13, char var14, int var15, boolean var16, String var17, boolean var18) throws Exception {
      String[] var20 = getElements(var0, var10, var11, var12, var13, var14, var15);
      int var19 = var20.length;
      if (var19 > 0) {
         ExpClass var21 = new ExpClass(0, 3, 0, (Object)null, (Object[])null, 0, (String)null, (Object)null);
         Object var22 = var5.get(var20[0].trim());
         if (var22 == null) {
            var22 = var6.get(var20[0]);
            if (var22 == null) {
               throw new Exception("Ismeretlen függvény: (" + var20[0] + ")\n" + "A nyomtatványhoz a keretprogram újabb verziója szükséges!");
            }

            var5.put(var20[0], var22);
         }

         if (var6 != null) {
            var21.setIdentifier((String)FnDescHelper.getOperationId(var22));
            var21.setSource(FnDescHelper.getSource(var22));
         }

         if (var21.getIdentifier() == null) {
            throw new Exception("Ismeretlen függvény: (" + var20[0] + ")\n" + "A nyomtatványhoz a keretprogram újabb verziója szükséges!");
         } else {
            Boolean var23 = FnDescHelper.isBeMemberOfDependencyList(var22);
            boolean var24 = var23 != null && var23;
            if (var24) {
               Object var25 = FnDescHelper.getSource(var22);
               if (var25 instanceof IFunctionSet) {
                  IFunctionSet var26 = (IFunctionSet)var25;
                  var25 = var26.getDependency(new Object[]{null, var20, "calculator, calculator_info", var7, "gui_info", var8, "form_id", var9, "target_id", var17});
                  var23 = FnDescHelper.isNeedNotifyOnCheck(var22);
                  boolean var27 = var23 != null && var23;
                  if (var16 && var27) {
                     var26.setCheckDept(new Object[]{null, var20, "calculator, calculator_info", var7, "gui_info", var8, "form_id", var9, "target_id", var17});
                  }

                  if (var25 instanceof String) {
                     String[] var28 = dep_field;
                     var28[0] = (String)var25;
                     var25 = var28;
                  }

                  if (var25 instanceof String[]) {
                     String[] var29 = (String[])((String[])var25);
                     int var30 = 0;

                     for(int var31 = var29.length; var30 < var31; ++var30) {
                        String var33 = var29[var30].trim();
                        if (var33.charAt(0) == var12 && var33.charAt(var33.length() - 1) == var12) {
                           var33 = var33.substring(1, var33.length() - 1);
                        }

                        if (var18) {
                           if (var1 != null) {
                              var1.add(var33);
                           }

                           if (var2 != null) {
                              var2.add(var33);
                           }

                           if (var3 != null) {
                              var3.add(var33);
                           }
                        }
                     }
                  }
               }
            }

            var21.setParameter(var19 - 2, (ExpClass)null);

            for(int var32 = 1; var32 < var19; ++var32) {
               var21.setParameter(var32 - 1, createExpressionRecursive(var20[var32], var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, getRecursiveDependency(var20)));
            }

            return var21;
         }
      } else {
         return new ExpClass(0, 3, 0, (Object)null, (Object[])null, 0, (String)null, (Object)null);
      }
   }

   private static boolean getRecursiveDependency(String[] var0) {
      if (var0 != null && var0.length >= 1) {
         return !"globsum".equalsIgnoreCase(var0[0]);
      } else {
         return true;
      }
   }

   private static ExpClass createConstant(String var0, Vector var1, Vector var2, Vector var3, IPropertyList var4, char var5) throws Exception {
      if (var0 == null) {
         return new ExpClass(0, 1, 0, (Object)null, (Object[])null, 0, (String)null, (Object)null);
      } else if (var0.length() == 0) {
         return new ExpClass(0, 1, 0, (Object)null, (Object[])null, 0, (String)null, (Object)null);
      } else if ("nil".equalsIgnoreCase(var0)) {
         return new ExpClass(0, 1, 0, (Object)null, (Object[])null, -3, (String)null, (Object)null);
      } else if ("zérus".equalsIgnoreCase(var0)) {
         return new ExpClass(0, 1, 2, new Integer(0), (Object[])null, -2, (String)null, (Object)null);
      } else if (var0.equalsIgnoreCase("true")) {
         return new ExpClass(0, 1, 4, Boolean.TRUE, (Object[])null, 0, (String)null, (Object)null);
      } else if (var0.equalsIgnoreCase("false")) {
         return new ExpClass(0, 1, 4, Boolean.FALSE, (Object[])null, 0, (String)null, (Object)null);
      } else if (var0.charAt(0) == var5 && var0.charAt(var0.length() - 1) == var5) {
         return new ExpClass(0, 1, 1, var0.substring(1, var0.length() - 1), (Object[])null, 0, (String)null, (Object)null);
      } else {
         try {
            return new ExpClass(0, 1, 2, NumericOperations.createNumber(var0), (Object[])null, 0, (String)null, (Object)null);
         } catch (NumberFormatException var8) {
            Tools.eLog(var8, 0);
            if (var4 != null) {
               String var6 = var0.toLowerCase().trim();
               ExpClass var7 = (ExpClass)var4.get(var6);
               if (var7 == null) {
                  throw new Exception("Ismeretlen változó: " + var6);
               } else {
                  if (var1 != null) {
                     var1.add(var6);
                  }

                  if (var2 != null) {
                     var2.add(var6);
                  }

                  if (var3 != null) {
                     var3.add(var6);
                  }

                  return new ExpClass(0, 2, 0, (Object)null, (Object[])null, 0, var6, var4);
               }
            } else {
               return new ExpClass(0, 1, 0, (Object)null, (Object[])null, 0, (String)null, (Object)null);
            }
         }
      }
   }

   public static Object createNumber(String var0) throws Exception {
      Object var1;
      try {
         var1 = new Integer(var0);
      } catch (NumberFormatException var3) {
         var1 = new BigDecimal(var0);
      }

      return var1;
   }

   private static String[] getElements(String var0, Vector var1, char var2, char var3, char var4, char var5, int var6) {
      var1.removeAllElements();
      int var7 = 0;
      boolean var9 = false;
      var0 = var0.trim();
      int var12 = 0;
      int var13 = var0.length();
      if (var13 > 0 && var0.charAt(var12) == var2) {
         ++var12;
      }

      int var8 = var12;

      for(int var14 = var12; var14 < var13; ++var14) {
         char var11 = var0.charAt(var14);
         if (var11 == var3) {
            var9 = !var9;
         }

         if (!var9) {
            if (var11 == var4) {
               ++var7;
            } else if (var11 == var5) {
               --var7;
            } else if (var7 == 0 && var11 == var2) {
               var1.add(var0.substring(var8, var14));
               var14 += var6;
               var8 = var14--;
            }
         }
      }

      var1.add(var0.substring(var8, var13));
      String[] var10 = new String[var1.size()];
      var10 = (String[])((String[])var1.toArray(var10));
      return var10;
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
