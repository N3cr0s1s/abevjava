package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0;

import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.cachedExp.ICachedItem;
import hu.piller.enykp.alogic.calculator.calculator_c.ExpClass;
import hu.piller.enykp.alogic.fileutil.HeadChecker;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.util.base.Tools;
import java.math.BigDecimal;
import java.util.Vector;

public class ABEVFeaturedBaseFunctions {
   private static final Long ID_EX_TYPE_MISMATCH = new Long(12100L);
   private static final String EX_TYPE_MISMATCH = "Program hiba, típus eltérés!";
   private static final Long ID_EX_PAR_CNT_MISMATCH = new Long(12101L);
   private static final String EX_PAR_CNT_MISMATCH = "Program hiba,, paraméterek száma nem megfelelő!";
   private static final Long ID_EX_FN_FIELD = new Long(12106L);
   private static final String EX_FN_FIELD = "Program hiba, hiba történt mező adat megszerzésekor!";
   private static final String[] CALL_GUI_LABELS = new String[]{"Típus", "Név", "Azonosító"};
   private static final Integer FAZIS_ADOZO = new Integer(0);
   private static final int HEAD_ALBIZONYLAT_AZONOSITO_INDEX = 12;

   private static String getExpValueTypeName(int var0) {
      String var1 = "";
      if (var0 >= 0) {
         if (var0 == 0) {
            return "nil";
         }

         if ((var0 & 1) == 1) {
            var1 = var1 + "Szöveg";
         }

         if ((var0 & 2) == 2) {
            var1 = var1 + (var1.length() > 0 ? "|" : "") + "Szám";
         }

         if ((var0 & 4) == 4) {
            var1 = var1 + (var1.length() > 0 ? "|" : "") + "Logikai";
         }
      }

      return var1.length() == 0 ? "(Ismeretlen)" : var1;
   }

   private static int getExpValueType(Object var0) {
      if (var0 == null) {
         return 0;
      } else if (var0 instanceof String) {
         return 1;
      } else if (var0 instanceof Boolean) {
         return 4;
      } else {
         return var0 instanceof Number ? 2 : -1;
      }
   }

   private static void writeError(Long var0, String var1, Exception var2, Object var3, ExpClass var4) {
      FunctionBodies.writeError(var4, var0, var1, var2, var3);
   }

   private static void writeTypeMismatchError(int var0, Exception var1, Object var2, ExpClass var3, ExpClass var4) {
      Object[] var5 = getTypeMismatchError(var0, var1, var2, var3, var4);
      writeError((Long)var5[0], (String)var5[1], (Exception)var5[2], var5[3], (ExpClass)var5[4]);
   }

   private static Object[] getTypeMismatchError(int var0, Exception var1, Object var2, ExpClass var3, ExpClass var4) {
      String var5;
      try {
         var5 = getExpValueTypeName(var0);
      } catch (Exception var11) {
         var5 = "???";
      }

      String var7;
      try {
         var7 = getExpValueTypeName(var4.getType());
      } catch (Exception var10) {
         var7 = "???";
      }

      Object var6;
      try {
         var6 = var4.getValue();
      } catch (Exception var9) {
         var6 = "";
      }

      var5 = var5 == null ? "???" : var5.toString();
      var7 = var7 == null ? "???" : var7.toString();
      String var12 = var6 == null ? "???" : var6.toString();
      return new Object[]{ID_EX_TYPE_MISMATCH, "Program hiba, típus eltérés! (" + var5 + "!" + var7 + ":" + var12 + ")", var1, var2, var3};
   }

   public static String getString(Object var0, String var1) {
      if (var0 != null && var0.toString().length() != 0) {
         return var0.toString();
      } else {
         return var1 == null ? "" : var1;
      }
   }

   private static String getNotNullString(Object var0, String var1) {
      return var0 != null && var0.toString().length() != 0 ? var0.toString() : var1;
   }

   public static ExpClass baseTypeConversion1(ExpClass var0) {
      int var1 = var0.getType();
      if (var1 == 0) {
         try {
            var0 = ExpFactoryLight.createConstant("0");
         } catch (Exception var4) {
            Tools.eLog(var4, 0);
         }
      } else if (var1 == 1) {
         try {
            var0 = ExpFactoryLight.createConstant(getString(var0.getValue(), "0"));
         } catch (Exception var3) {
            Tools.eLog(var3, 0);
         }
      }

      return var0;
   }

   public static ExpClass baseTypeLogicalConversion1(ExpClass var0) {
      int var1 = var0.getType();
      if (var1 == 0) {
         try {
            var0 = ExpFactoryLight.createConstant("", 4);
         } catch (Exception var4) {
            Tools.eLog(var4, 0);
         }
      } else if (var1 == 1 || var1 == 4) {
         try {
            var0 = ExpFactoryLight.createConstant(getString(var0.getValue(), ""), 4);
         } catch (Exception var3) {
            Tools.eLog(var3, 0);
         }
      }

      return var0;
   }

   private static ExpClass baseTypeConversion1(ExpClass var0, int var1) {
      int var2 = var0.getType();
      if (var2 == 0) {
         try {
            switch(var1) {
            case 1:
               var0 = ExpFactoryLight.createConstant("\"\"");
               break;
            case 2:
               var0 = ExpFactoryLight.createConstant("0");
            }
         } catch (Exception var4) {
            Tools.eLog(var4, 0);
         }
      }

      return var0;
   }

   public static synchronized void fnLessOrEq(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 > 0) {
         Object var3 = null;
         ExpClass var2 = baseTypeConversion1(var0.getParameter(0));
         int var6 = var2.getType();
         Object var4 = var2.getValue();
         if (var6 != 2) {
            writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
            return;
         }

         for(int var8 = 1; var8 < var1; ++var8) {
            var2 = baseTypeConversion1(var0.getParameter(var8));
            int var7 = var2.getType();
            Object var5 = var2.getValue();
            if (var7 != 2) {
               writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
               break;
            }

            var3 = NumericOperations.processArithmAndLogical(10, var4, var5);
            if (var3 instanceof Boolean && !(Boolean)var3) {
               break;
            }

            var4 = var5;
         }

         var0.setType(getExpValueType(var3));
         var0.setResult(var3);
      }

   }

   public static synchronized void fnGreaterOrEq(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 > 0) {
         Object var3 = null;
         ExpClass var2 = baseTypeConversion1(var0.getParameter(0));
         int var6 = var2.getType();
         Object var4 = var2.getValue();
         if (var6 != 2) {
            writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
            return;
         }

         for(int var8 = 1; var8 < var1; ++var8) {
            var2 = baseTypeConversion1(var0.getParameter(var8));
            int var7 = var2.getType();
            Object var5 = var2.getValue();
            if (var7 != 2) {
               writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
               break;
            }

            var3 = NumericOperations.processArithmAndLogical(11, var4, var5);
            if (var3 instanceof Boolean && !(Boolean)var3) {
               break;
            }

            var4 = var5;
         }

         var0.setType(getExpValueType(var3));
         var0.setResult(var3);
      }

   }

   public static synchronized void fnGreater(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 > 0) {
         Object var3 = null;
         ExpClass var2 = baseTypeConversion1(var0.getParameter(0));
         int var6 = var2.getType();
         Object var4 = var2.getValue();
         if (var6 != 2) {
            writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
            return;
         }

         for(int var8 = 1; var8 < var1; ++var8) {
            var2 = baseTypeConversion1(var0.getParameter(var8));
            int var7 = var2.getType();
            Object var5 = var2.getValue();
            if (var7 != 2) {
               writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
               break;
            }

            var3 = NumericOperations.processArithmAndLogical(8, var4, var5);
            if (var3 instanceof Boolean && !(Boolean)var3) {
               break;
            }

            var4 = var5;
         }

         var0.setType(getExpValueType(var3));
         var0.setResult(var3);
      }

   }

   public static synchronized void fnLess(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 > 0) {
         Object var3 = null;
         ExpClass var2 = baseTypeConversion1(var0.getParameter(0));
         int var6 = var2.getType();
         Object var4 = var2.getValue();
         if (var6 != 2) {
            writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
            return;
         }

         for(int var8 = 1; var8 < var1; ++var8) {
            var2 = baseTypeConversion1(var0.getParameter(var8));
            int var7 = var2.getType();
            Object var5 = var2.getValue();
            if (var7 != 2) {
               writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
               break;
            }

            var3 = NumericOperations.processArithmAndLogical(7, var4, var5);
            if (var3 instanceof Boolean && !(Boolean)var3) {
               break;
            }

            var4 = var5;
         }

         var0.setType(getExpValueType(var3));
         var0.setResult(var3);
      }

   }

   public static synchronized void fnAdozoiErtek(ExpClass var0) {
      BookModel var1 = FunctionBodies.getInfoObject();
      fnFieldCore(var0, "0".equals(var1.getRole()));
   }

   public static synchronized void fnField(ExpClass var0) {
      fnFieldCore(var0, true);
   }

   public static synchronized void fnFieldCore(ExpClass var0, boolean var1) {
      var0.setType(0);
      var0.setResult((Object)null);
      IDataStore var2 = FunctionBodies.g_active_data_store;
      String var3 = null;
      Object var6 = null;

      try {
         ExpClass var5 = var0.getParameter(0);
         var6 = var5.getValue();
         Object[] var7 = FIdHelper.getDataStoreKey(var6.toString());
         FunctionBodies.g_exp_fields.add((String)var7[1], (Integer)var7[0]);
         if (var1) {
            var3 = var2.get(var7);
         } else {
            var3 = getTaxPayerDataFromHistory(var7);
         }

         if (var3 == null) {
            var3 = "";
         }

         int var8 = FunctionBodies.getFieldType(var6.toString());
         var3 = convertIfLogical(var3, var8);
         ExpClass var4 = ExpFactoryLight.createConstant(var3, var8);
         var0.setType(var4.getType());
         var0.setResult(var4.getResult());
      } catch (Exception var9) {
         writeError(ID_EX_FN_FIELD, "Program hiba, hiba történt mező adat megszerzésekor! (" + var6 + ")", var9, (Object)null, var0);
      }

      if (!FunctionBodies.g_in_variable_exp && !FunctionBodies.isFieldEmpty(var0)) {
         FunctionBodies.g_all_fileds_empty = false;
      }

   }

   private static String convertIfLogical(String var0, int var1) {
      return var1 != 4 || !"X".equalsIgnoreCase(var0) && !"1".equals(var0) ? var0 : "true";
   }

   private static String getTaxPayerDataFromHistory(Object[] var0) {
      BookModel var1 = FunctionBodies.getInfoObject();
      return var1.getHistorydata((Integer)var0[0] + "_" + (String)var0[1], FAZIS_ADOZO);
   }

   public static synchronized void fnEqual(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 > 0) {
         Object var3 = null;
         int var8 = getFirstNotNillType(var0);
         ExpClass var2 = baseTypeConversion1(var0.getParameter(0), var8);
         int var6 = var2.getType();
         Object var4 = var2.getValue();

         for(int var9 = 1; var9 < var1; ++var9) {
            var2 = baseTypeConversion1(var0.getParameter(var9), var8);
            int var7 = var2.getType();
            Object var5 = var2.getValue();
            var3 = NumericOperations.processArithmAndLogical(9, var4, var5);
            if (var3 instanceof Boolean && !(Boolean)var3) {
               break;
            }

            var4 = var5;
         }

         var0.setType(getExpValueType(var3));
         var0.setResult(var3);
      }

   }

   public static synchronized void fnNotEq(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 > 0) {
         Object var3 = null;
         int var8 = getFirstNotNillType(var0);
         ExpClass var2 = baseTypeConversion1(var0.getParameter(0), var8);
         int var6 = var2.getType();
         Object var4 = var2.getValue();
         if (var6 != 2 && var6 != 1 && var6 != 4 && var6 != 0) {
            writeTypeMismatchError(var6, (Exception)null, (Object)null, var0, var2);
            return;
         }

         for(int var9 = 1; var9 < var1; ++var9) {
            var2 = baseTypeConversion1(var0.getParameter(var9), var8);
            int var7 = var2.getType();
            Object var5 = var2.getValue();
            if (var7 != 2 && var7 != 1 && var7 != 4 && var7 != 0 || var7 != var6 && var7 != 0 && var6 != 0) {
               writeTypeMismatchError(var6, (Exception)null, (Object)null, var0, var2);
               break;
            }

            var3 = NumericOperations.processArithmAndLogical(12, var4, var5);
            if (var3 instanceof Boolean && !(Boolean)var3) {
               break;
            }

            var4 = var5;
         }

         var0.setType(getExpValueType(var3));
         var0.setResult(var3);
      }

   }

   private static int getFirstNotNillType(ExpClass var0) {
      int var1 = var0.getParametersCount();
      if (var1 == 0) {
         return 0;
      } else {
         for(int var2 = 0; var2 < var1; ++var2) {
            ExpClass var3 = var0.getParameter(var2);
            int var4 = var3.getType();
            if (var4 != 0) {
               return var4;
            }
         }

         return 0;
      }
   }

   public static synchronized void fnPower(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 > 0) {
         int var7 = 0;

         ExpClass var2;
         int var8;
         for(var8 = 0; var8 < var1; ++var8) {
            if ((var2 = var0.getParameter(var8)) != null) {
               if (var2.getValue() == null || var2.getType() == 0) {
                  ++var7;
               }
            } else {
               ++var7;
            }
         }

         if (var7 == 0) {
            var2 = baseTypeConversion1(var0.getParameter(0));
            int var5 = var2.getType();
            Object var3 = var2.getValue();
            if (var5 != 2) {
               writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
               return;
            }

            for(var8 = 1; var8 < var1; ++var8) {
               var2 = baseTypeConversion1(var0.getParameter(var8));
               Object var4 = var2.getValue();
               int var6 = var2.getType();
               if (var6 != 2) {
                  writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
                  break;
               }

               var3 = NumericOperations.processArithmAndLogical(2, var3, var4);
            }

            var0.setType(var5);
            var0.setResult(var3);
         } else {
            var0.setType(2);
            var0.setResult(new BigDecimal(0));
         }
      }

   }

   public static synchronized void fnDivision(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 > 0) {
         int var7 = 0;

         ExpClass var2;
         int var8;
         for(var8 = 0; var8 < var1; ++var8) {
            if ((var2 = var0.getParameter(var8)) != null) {
               if (var2.getValue() == null) {
                  ++var7;
               }
            } else {
               ++var7;
            }
         }

         if (var7 == 0) {
            var2 = baseTypeConversion1(var0.getParameter(0));
            int var5 = var2.getType();
            Object var3 = var2.getValue();
            if (var5 != 2) {
               writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
               return;
            }

            for(var8 = 1; var8 < var1; ++var8) {
               var2 = baseTypeConversion1(var0.getParameter(var8));
               Object var4 = var2.getValue();
               int var6 = var2.getType();
               if (var6 != 2) {
                  writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
                  break;
               }

               var3 = NumericOperations.processArithmAndLogical(3, var3, var4);
            }

            var0.setType(var5);
            var0.setResult(var3);
         } else {
            var0.setType(2);
            var0.setResult(new BigDecimal(0));
         }
      }

   }

   public static synchronized void fnPlus(ExpClass var0) {
      boolean var2 = false;
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 > 0) {
         Object var3 = null;
         ExpClass var5 = null;
         int var6 = 0;
         int var9 = var0.getType();
         var0.setType(0);
         var0.setResult((Object)null);

         int var8;
         for(var8 = 0; var8 < var1; ++var8) {
            if ((var5 = var0.getParameter(var8)) != null && (var3 = var5.getValue()) != null) {
               var6 = var5.getType();
               if (var6 != 0) {
                  break;
               }
            }
         }

         if (var6 == 0) {
            return;
         }

         if (var6 == 2) {
            var2 = true;
         }

         if (var6 != 2 && var6 != 1) {
            writeTypeMismatchError(3, (Exception)null, (Object)null, var0, var5);
            return;
         }

         for(int var10 = var8 + 1; var10 < var1; ++var10) {
            var5 = var0.getParameter(var10);
            if (var5 != null) {
               Object var4 = var5.getValue();
               if (var4 != null) {
                  int var7 = var5.getType();
                  if (var7 != 0) {
                     if (var7 != 2 && var7 != 1) {
                        writeTypeMismatchError(var6, (Exception)null, (Object)null, var0, var5);
                        return;
                     }

                     if (var2 && var7 == 1) {
                        try {
                           var4 = NumericOperations.createNumber(getNotNullString(var4, "0"));
                        } catch (Exception var12) {
                           writeTypeMismatchError(3, (Exception)null, (Object)null, var0, var5);
                           return;
                        }
                     }

                     var3 = NumericOperations.processArithmAndLogical(0, var3, var4);
                  }
               }
            }
         }

         var0.setType(var6);
         var0.setResult(var3);
      } else {
         var0.setType(0);
         var0.setResult((Object)null);
      }

   }

   public static synchronized void fnMinus(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 > 0) {
         Object var3 = null;
         int var8 = -1;
         int var9 = var0.getType();
         ExpClass var2 = baseTypeConversion1(var0.getParameter(0));
         int var5 = var2.getType();
         var3 = var2.getValue();
         if (var5 != 2 && var5 != 0) {
            writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
            return;
         }

         for(int var10 = 1; var10 < var1; ++var10) {
            var2 = var0.getParameter(var10);
            int var7 = var2.getType();
            if (var7 != 0) {
               ;
            }

            var2 = baseTypeConversion1(var2);
            Object var4 = var2.getValue();
            int var6 = var2.getType();
            if (var6 != 2 && var6 != 0) {
               writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
               break;
            }

            if (var7 == 0) {
               ++var8;
            }

            switch(var6) {
            case 2:
               var3 = NumericOperations.processArithmAndLogical(1, var3, var4);
               if (var3 != null && var8 % 2 > 0) {
                  var3 = NumericOperations.processArithmAndLogical(1, (Object)null, var3);
               }

               var8 = -1;
               break;
            default:
               writeTypeMismatchError(var5, (Exception)null, (Object)null, var0, var2);
            }
         }

         var0.setType(var5);
         var0.setResult(var3);
      }

   }

   public static synchronized void fnMax(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      var0.setFlag(0);
      int var1 = var0.getParametersCount();
      if (var1 > 0) {
         ExpClass var3 = null;
         Object var5 = null;
         int var7 = 0;
         int var9 = 0;

         ExpClass var2;
         while(var9 < var1) {
            var2 = var0.getParameter(var9);
            var3 = var2;
            ++var9;
            if (var2.getType() != 0) {
               var7 = var2.getType();
               var5 = var2.getValue();
               break;
            }
         }

         if (var7 != 2) {
            return;
         }

         for(int var10 = var9; var10 < var1; ++var10) {
            var2 = var0.getParameter(var10);
            if (var2 != null) {
               Object var6 = var2.getValue();
               if (var6 != null) {
                  int var8 = var2.getType();
                  if (var8 != 0) {
                     if (var8 != 2) {
                        writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
                        break;
                     }

                     Object var4;
                     var4 = var5;
                     label38:
                     switch(var8) {
                     case 2:
                        if (var5 == null) {
                           var5 = var6;
                        }

                        switch(NumericOperations.getObjectType(var5, var6)) {
                        case 5:
                           var5 = new Integer(Math.max(NumericOperations.getInteger(var5), NumericOperations.getInteger(var6)));
                           break label38;
                        case 7:
                           var5 = NumericOperations.getBigDecimal(var5).max(NumericOperations.getBigDecimal(var6));
                           break label38;
                        default:
                           writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
                           return;
                        }
                     default:
                        writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
                     }

                     if (var3 == null || !var5.equals(var4)) {
                        var3 = var2;
                     }
                  }
               }
            }
         }

         var0.setType(var7);
         var0.setResult(var5);
      }

   }

   public static synchronized void fnMin(ExpClass var0) {
      int var2 = 0;
      var0.setType(0);
      var0.setResult((Object)null);
      var0.setFlag(0);
      int var1 = var0.getParametersCount();
      if (var1 > 0) {
         ExpClass var4 = null;
         Object var6 = null;
         byte var10 = 0;
         int var12 = var10 + 1;
         ExpClass var3 = var0.getParameter(var10);
         var4 = var3;
         int var8 = var3.getType();
         if (var8 == 0) {
            var8 = 2;
            var3 = baseTypeConversion1(var3);
         }

         var6 = var3.getValue();
         if (var8 != 2) {
            return;
         }

         if (var1 == 1) {
            var0.setType(var8);
            var0.setResult(var6);
            return;
         }

         for(int var11 = var12; var11 < var1; ++var11) {
            var3 = var0.getParameter(var11);
            if (var3 != null) {
               Object var7 = var3.getValue();
               if (var7 == null) {
                  ++var2;
               } else {
                  int var9 = var3.getType();
                  if (var9 == 0) {
                     ++var2;
                  } else {
                     if (var9 != 2) {
                        writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var3);
                        break;
                     }

                     Object var5;
                     var5 = var6;
                     label42:
                     switch(var9) {
                     case 2:
                        if (var6 == null) {
                           var6 = var7;
                        }

                        switch(NumericOperations.getObjectType(var6, var7)) {
                        case 5:
                           var6 = new Integer(Math.min(NumericOperations.getInteger(var6), NumericOperations.getInteger(var7)));
                           break label42;
                        case 7:
                           var6 = NumericOperations.getBigDecimal(var6).min(NumericOperations.getBigDecimal(var7));
                           break label42;
                        default:
                           writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var3);
                           return;
                        }
                     default:
                        writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var3);
                     }

                     if (var4 == null || !var6.equals(var5)) {
                        var4 = var3;
                     }
                  }
               }
            }
         }

         if (var2 == var1 - 1) {
            return;
         }

         var0.setType(var8);
         var0.setResult(var6);
      }

   }

   public static synchronized void fnRound(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var2 = var0.getParameter(0);
         if (var2 == null) {
            return;
         }

         Object var3 = var2.getValue();
         if (var3 == null) {
            return;
         }

         int var4 = var2.getType();
         if (var4 == 2) {
            switch(NumericOperations.getObjectType(var3, var3)) {
            case 7:
               var3 = FunctionBodies.Tz_round(NumericOperations.getBigDecimal(var3));
            case 5:
               var0.setType(2);
               var0.setResult(var3);
               break;
            default:
               writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
               return;
            }
         } else {
            var0.setType(var4);
            var0.setResult(var3);
         }
      } else {
         writeError(ID_EX_PAR_CNT_MISMATCH, "Program hiba,, paraméterek száma nem megfelelő!", (Exception)null, (Object)null, var0);
      }

   }

   public static synchronized void fnGlobSum(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 2) {
         try {
            ExpClass var2 = var0.getParameter(1);
            String var3 = (String)var0.getParameter(0).getValue();
            ICachedItem var5 = FunctionBodies.g_cached_items.getResult(var3, "globsum", ABEVFunctionSet.getExpString(var2));
            Object[] var4 = var5.getResult();
            FunctionBodies.storeErrors(var0, var5.getErrors());
            var0.setType((Integer)var4[1]);
            var0.setResult(var4[0]);
         } catch (Exception var6) {
            Tools.eLog(var6, 1);
         }
      }

   }

   public static Object globsumBody(Object var0, Object var1, Vector var2) {
      ExpClass var3 = (ExpClass)var0;
      Object var4 = ((Object[])((Object[])var1))[0];
      FunctionBodies.g_all_fileds_empty = true;
      var3.setResult(ABEVFunctionSet.getInstance().calculate(var3));
      if (!FunctionBodies.g_all_fileds_empty) {
         ((Object[])((Object[])var1))[2] = false;
      }

      Object var5 = var3.getValue();
      int var6 = var3.getType();
      if (var6 != 0 && var5 != null) {
         if (var6 == 1) {
            try {
               var5 = NumericOperations.createNumber(getNotNullString(var5, "0"));
               var6 = 2;
            } catch (Exception var9) {
               var2.add(getTypeMismatchError(3, (Exception)null, (Object)null, (ExpClass)null, var3));
               return var4;
            }
         }

         if (var6 != 2) {
            var2.add(getTypeMismatchError(-1, (Exception)null, (Object)null, (ExpClass)null, (ExpClass)null));
            return var4;
         } else {
            if (var4 == null) {
               var4 = var5;
               ((Object[])((Object[])var1))[1] = new Integer(var6);
            } else {
               switch(var6) {
               case 2:
                  ((Object[])((Object[])var1))[1] = new Integer(2);

                  try {
                     var4 = NumericOperations.add(var4, var5);
                     break;
                  } catch (Exception var8) {
                     var2.add(getTypeMismatchError(2, (Exception)null, (Object)null, (ExpClass)null, var3));
                     return var4;
                  }
               default:
                  var2.add(getTypeMismatchError(2, (Exception)null, (Object)null, (ExpClass)null, var3));
               }
            }

            return var4;
         }
      } else {
         return var4;
      }
   }

   public static synchronized void fnIf(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      var0.setFlag(0);
      int var1 = var0.getParametersCount();
      if (var1 == 3) {
         ExpClass var2 = var0.getParameter(0);
         if (var2 == null) {
            return;
         }

         Object var3 = var2.getValue();
         if (var3 == null) {
            return;
         }

         if (NumericOperations.getObjectType(var3, var3) != 6) {
            writeTypeMismatchError(4, (Exception)null, (Object)null, var0, var2);
            return;
         }

         if ((Boolean)var3) {
            var2 = var0.getParameter(1);
         } else {
            var2 = var0.getParameter(2);
         }

         var0.setType(var2.getType());
         int var4 = var2.getFlag();
         var0.setFlag(var4);
         var0.setDontModify(var2.isDontModify());
         var0.setResult(var2.getResult());
      } else {
         writeError(ID_EX_PAR_CNT_MISMATCH, "Program hiba,, paraméterek száma nem megfelelő!", (Exception)null, (Object)null, var0);
      }

   }

   public static synchronized void fnAnd(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var2 = var0.getParametersCount();
      if (var2 > 0) {
         Object var4 = null;
         ExpClass var3 = var0.getParameter(0);
         Object var5;
         int var7;
         if (var3 != null) {
            var7 = var3.getType();
            var5 = var3.getValue();
         } else {
            var7 = 0;
            var5 = null;
         }

         if (var7 != 2 && var7 != 1 && var7 != 4 && var7 != 0) {
            writeTypeMismatchError(7, (Exception)null, (Object)null, var0, var3);
            return;
         }

         for(int var12 = 1; var12 < var2; ++var12) {
            var3 = var0.getParameter(var12);
            int var8 = var3.getType();
            Object var6 = var3.getValue();
            if (var8 != 2 && var8 != 1 && var8 != 4 && var8 != 0 || var8 != var7 && var8 != 0 && var7 != 0) {
               writeTypeMismatchError(var7, (Exception)null, (Object)null, var0, var3);
               break;
            }

            var4 = NumericOperations.processArithmAndLogical(4, var5, var6);
            if (var4 instanceof Boolean && !(Boolean)var4) {
               break;
            }

            var5 = var6;
         }

         var0.setType(getExpValueType(var4));
         var0.setResult(var4);
      }

   }

   public static synchronized void fnOr(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var2 = var0.getParametersCount();
      if (var2 > 0) {
         Object var4 = null;
         ExpClass var3 = var0.getParameter(0);
         Object var5;
         int var7;
         if (var3 != null) {
            var7 = var3.getType();
            var5 = var3.getValue();
         } else {
            var7 = 0;
            var5 = null;
         }

         if (var7 != 2 && var7 != 1 && var7 != 4 && var7 != 0) {
            writeTypeMismatchError(7, (Exception)null, (Object)null, var0, var3);
            return;
         }

         for(int var12 = 1; var12 < var2; ++var12) {
            var3 = var0.getParameter(var12);
            int var8 = var3.getType();
            Object var6 = var3.getValue();
            if (var8 != 2 && var8 != 1 && var8 != 4 && var8 != 0 || var8 != var7 && var8 != 0 && var7 != 0) {
               writeTypeMismatchError(var7, (Exception)null, (Object)null, var0, var3);
               break;
            }

            var4 = NumericOperations.processArithmAndLogical(5, var5, var6);
            if (var4 instanceof Boolean && !(Boolean)var4) {
               break;
            }

            var5 = var6;
         }

         var0.setType(getExpValueType(var4));
         var0.setResult(var4);
      }

   }

   public static synchronized void fnXor(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 > 0) {
         Object var3 = null;
         ExpClass var2 = var0.getParameter(0);
         Object var4;
         int var6;
         if (var2 != null) {
            var6 = var2.getType();
            var4 = var2.getValue();
         } else {
            var6 = 0;
            var4 = null;
         }

         if (var6 != 2 && var6 != 4 && var6 != 0) {
            writeTypeMismatchError(6, (Exception)null, (Object)null, var0, var2);
            return;
         }

         for(int var8 = 1; var8 < var1; ++var8) {
            var2 = var0.getParameter(var8);
            int var7 = var2.getType();
            Object var5 = var2.getValue();
            if (var7 != 2 && var7 != 4 && var7 != 0 || var7 != var6 && var7 != 0 && var6 != 0) {
               writeTypeMismatchError(var6, (Exception)null, (Object)null, var0, var2);
               break;
            }

            var3 = NumericOperations.processArithmAndLogical(6, var4, var5);
            if (var3 instanceof Boolean && !(Boolean)var3) {
               break;
            }

            var4 = var5;
         }

         var0.setType(getExpValueType(var3));
         var0.setResult(var3);
      }

   }

   public static synchronized void fnCallGUI(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 > 0) {
         ExpClass var2 = var0.getParameter(0);
         if (var2 == null) {
            return;
         }

         int var4 = var2.getType();
         if (var4 != 1) {
            writeTypeMismatchError(1, (Exception)null, (Object)null, var0, var2);
            return;
         }

         Object var3 = var2.getValue();
         String var5 = var3 == null ? "" : var3.toString();
         BookModel var6 = FunctionBodies.getInfoObject();
         Object[] var7 = new Object[CALL_GUI_LABELS.length];

         for(int var8 = 1; var8 < var1; ++var8) {
            var2 = var0.getParameter(var8);
            if (var2 == null) {
               var3 = null;
            } else {
               var3 = var2.getValue();
            }

            var7[var8 - 1] = var3 == null ? null : var3.toString().trim();
         }

         if (var7[2] == null) {
            int var9;
            String var14;
            if (var7[1] != null) {
               var14 = var7[1].toString().trim();
               var9 = var14.lastIndexOf(" ");
               if (var9 > 0) {
                  String var10 = var14.substring(var9 + 1).trim();
                  if (isId(var10)) {
                     var7[1] = var14.substring(0, var9).trim();
                     var7[2] = var10;
                  } else {
                     var7[1] = var14;
                     var7[2] = "";
                  }
               }
            } else if (var7[0] != null) {
               var14 = var7[0].toString().trim();
               var9 = var14.lastIndexOf(" ");
               int var15 = var14.indexOf(" ");
               if (var9 > 0) {
                  String var11 = var14.substring(var9 + 1).trim();
                  String var12 = var14.substring(0, var15).trim();
                  var7[0] = var12;
                  if (isId(var11)) {
                     var7[1] = var14.substring(var15, var9).trim();
                     var7[2] = var11;
                  } else {
                     var7[1] = var14.substring(var15).trim();
                     var7[2] = "";
                  }
               }
            }
         }

         var6.callgui(var5, CALL_GUI_LABELS, var7);

         try {
            var2 = ExpFactoryLight.createConstant(var3 == null ? "" : var3.toString());
            var0.setType(var2.getType());
            var0.setResult(var2.getValue());
         } catch (Exception var13) {
            Tools.eLog(var13, 1);
         }
      } else {
         writeError(ID_EX_PAR_CNT_MISMATCH, "Program hiba,, paraméterek száma nem megfelelő!", (Exception)null, (Object)null, var0);
      }

   }

   private static boolean isId(String var0) {
      return var0 != null && var0.length() > 0 && getAlbizAzon().equals(var0) ? true : isNumber(var0);
   }

   private static boolean isNumber(String var0) {
      try {
         Long.parseLong(var0);
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   private static String getAlbizAzon() {
      BookModel var0 = FunctionBodies.getInfoObject();
      String var1 = FunctionBodies.getFormid();
      IDataStore var2 = FunctionBodies.getActiveDataStore();
      String[] var3 = (String[])((String[])HeadChecker.getInstance().getHeadData(var0.get(var1).id, var2));

      try {
         String var4 = var3[12];
         return var4 == null ? "" : var4;
      } catch (Exception var5) {
         return "";
      }
   }

   public static void fnAbs(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      var0.setFlag(0);
      int var1 = var0.getParametersCount();
      if (var1 == 1) {
         ExpClass var2 = var0.getParameter(0);
         if (var2 == null) {
            return;
         }

         Object var3 = var2.getValue();
         if (var3 == null) {
            return;
         }

         int var4 = var2.getType();
         if (var4 == 2) {
            switch(NumericOperations.getObjectType(var3, var3)) {
            case 5:
               var3 = new Integer(Math.abs(NumericOperations.getInteger(var3)));
               break;
            case 7:
               var3 = NumericOperations.getBigDecimal(var3).abs();
               break;
            default:
               writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
               return;
            }

            var0.setType(2);
            var0.setResult(var3);
         } else if (var4 != 0) {
            writeTypeMismatchError(2, (Exception)null, (Object)null, var0, var2);
         }
      } else {
         writeError(ID_EX_PAR_CNT_MISMATCH, "Program hiba,, paraméterek száma nem megfelelő!", (Exception)null, (Object)null, var0);
      }

   }
}
