package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0;

import hu.piller.enykp.util.base.Tools;
import java.math.BigDecimal;

public class NumericOperations {
   public static final int DEFAULT_PRECISION = 40;
   static final int AR_PLUS = 0;
   static final int AR_MINUS = 1;
   static final int AR_POWER = 2;
   static final int AR_DIVISION = 3;
   static final int LO_AND = 4;
   static final int LO_OR = 5;
   static final int LO_XOR = 6;
   public static final int RE_LESS = 7;
   public static final int RE_GREATER = 8;
   public static final int RE_EQUAL = 9;
   public static final int RE_LESS_OR_EQ = 10;
   public static final int RE_GREATER_OR_EQ = 11;
   public static final int RE_NOT_EQ = 12;
   public static final int OBJ_STRING = 0;
   public static final int OBJ_INTEGER = 5;
   public static final int OBJ_BOOLEAN = 6;
   public static final int OBJ_BIGDECIMAL = 7;
   public static final String STR_ZERO = "0";

   public static Object createNumber(String var0) throws Exception {
      try {
         return new BigDecimal(var0);
      } catch (Exception var2) {
         return new BigDecimal(var0.trim());
      }
   }

   public static BigDecimal tryIntegerNumberFormat(Double var0) {
      try {
         return !Double.isInfinite(var0) && !Double.isNaN(var0) ? new BigDecimal(var0) : null;
      } catch (NumberFormatException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static BigDecimal tryIntegerNumberFormat(Float var0) {
      try {
         return !Float.isInfinite(var0) && !Float.isNaN(var0) ? new BigDecimal((double)var0) : null;
      } catch (NumberFormatException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static Object processArithmAndLogical(int var0, Object var1, Object var2) {
      int var3 = getObjectType(var1, var2);
      switch(var3) {
      case 0:
         if (var1 == null && var2 == null) {
            switch(var0) {
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
               return Boolean.TRUE;
            case 12:
               return Boolean.FALSE;
            }
         } else {
            if (var1 == null || var2 == null) {
               if (var1 != null) {
                  switch(var0) {
                  case 9:
                     return Boolean.FALSE;
                  case 12:
                     return Boolean.TRUE;
                  default:
                     return var1.toString();
                  }
               } else {
                  switch(var0) {
                  case 9:
                     return Boolean.FALSE;
                  case 12:
                     return Boolean.TRUE;
                  default:
                     return var2.toString();
                  }
               }
            }

            switch(var0) {
            case 0:
            case 4:
            case 5:
               return var1.toString() + var2.toString();
            case 1:
            case 2:
            case 3:
            case 6:
            case 7:
            case 8:
            case 10:
            case 11:
            default:
               break;
            case 9:
               return var1.equals(var2.toString());
            case 12:
               return !var1.equals(var2.toString());
            }
         }
      case 1:
      case 2:
      case 3:
      case 4:
      default:
         break;
      case 5:
         if (var1 != null || var2 != null) {
            if (var1 != null && var2 != null) {
               switch(var0) {
               case 0:
                  return new Integer(getInteger(var1) + getInteger(var2));
               case 1:
                  return new Integer(getInteger(var1) - getInteger(var2));
               case 2:
                  return new Integer(getInteger(var1) * getInteger(var2));
               case 3:
                  try {
                     return getBigDecimal(var1).divide(getBigDecimal(var2), 40, 4);
                  } catch (Exception var6) {
                     return null;
                  }
               case 4:
                  return new Integer(getInteger(var1) & getInteger(var2));
               case 5:
                  return new Integer(getInteger(var1) | getInteger(var2));
               case 6:
                  return new Integer(getInteger(var1) ^ getInteger(var2));
               case 7:
                  return getInteger(var1) < getInteger(var2);
               case 8:
                  return getInteger(var1) > getInteger(var2);
               case 9:
                  return getInteger(var1) == getInteger(var2);
               case 10:
                  return getInteger(var1) <= getInteger(var2);
               case 11:
                  return getInteger(var1) >= getInteger(var2);
               case 12:
                  return getInteger(var1) != getInteger(var2);
               }
            } else if (var1 != null) {
               switch(var0) {
               case 2:
               case 3:
               case 7:
               case 8:
               case 10:
               case 11:
                  break;
               case 4:
               case 5:
               case 6:
               default:
                  return Integer.valueOf(var1.toString());
               case 9:
                  return Boolean.FALSE;
               case 12:
                  return Boolean.TRUE;
               }
            } else {
               switch(var0) {
               case 1:
                  Integer var7 = Integer.valueOf(var2.toString());
                  if (var7 != null) {
                     var7 = new Integer(-var7);
                  }

                  return var7;
               case 2:
               case 3:
               case 7:
               case 8:
               case 10:
               case 11:
                  break;
               case 4:
               case 5:
               case 6:
               default:
                  return Integer.valueOf(var2.toString());
               case 9:
                  return Boolean.FALSE;
               case 12:
                  return Boolean.TRUE;
               }
            }
         }
         break;
      case 6:
         if (var1 != null || var2 != null) {
            if (var1 != null && var2 != null) {
               switch(var0) {
               case 4:
                  return getBoolean(var1) & getBoolean(var2);
               case 5:
                  return getBoolean(var1) | getBoolean(var2);
               case 6:
                  return getBoolean(var1) ^ getBoolean(var2);
               case 7:
               case 8:
               case 10:
               case 11:
               default:
                  break;
               case 9:
                  return getBoolean(var1) == getBoolean(var2);
               case 12:
                  return getBoolean(var1) != getBoolean(var2);
               }
            } else if (var1 != null) {
               switch(var0) {
               case 4:
                  return Boolean.FALSE;
               case 5:
                  return getBoolean(var1);
               case 6:
                  return getBoolean(var1);
               case 7:
               case 8:
               case 10:
               case 11:
               default:
                  break;
               case 9:
                  return Boolean.FALSE;
               case 12:
                  return Boolean.TRUE;
               }
            } else {
               switch(var0) {
               case 4:
                  return Boolean.FALSE;
               case 5:
                  return getBoolean(var1);
               case 6:
                  return getBoolean(var1);
               case 7:
               case 8:
               case 10:
               case 11:
               default:
                  break;
               case 9:
                  return Boolean.FALSE;
               case 12:
                  return Boolean.TRUE;
               }
            }
         }
         break;
      case 7:
         if (var1 != null || var2 != null) {
            if (var1 != null && var2 != null) {
               switch(var0) {
               case 0:
                  return getBigDecimal(var1).add(getBigDecimal(var2));
               case 1:
                  return getBigDecimal(var1).add(getBigDecimal(var2).negate());
               case 2:
                  return getBigDecimal(var1).multiply(getBigDecimal(var2));
               case 3:
                  try {
                     return getBigDecimal(var1).divide(getBigDecimal(var2), 40, 4);
                  } catch (Exception var5) {
                     return null;
                  }
               case 4:
                  return new BigDecimal(getBigDecimal(var1).longValue() & getBigDecimal(var2).longValue());
               case 5:
                  return new BigDecimal(getBigDecimal(var1).longValue() | getBigDecimal(var2).longValue());
               case 6:
                  return new BigDecimal(getBigDecimal(var1).longValue() ^ getBigDecimal(var2).longValue());
               case 7:
                  return getBigDecimal(var1).compareTo(getBigDecimal(var2)) < 0;
               case 8:
                  return getBigDecimal(var1).compareTo(getBigDecimal(var2)) > 0;
               case 9:
                  return getBigDecimal(var1).compareTo(getBigDecimal(var2)) == 0;
               case 10:
                  return getBigDecimal(var1).compareTo(getBigDecimal(var2)) <= 0;
               case 11:
                  return getBigDecimal(var1).compareTo(getBigDecimal(var2)) >= 0;
               case 12:
                  return getBigDecimal(var1).compareTo(getBigDecimal(var2)) != 0;
               }
            } else if (var1 != null) {
               switch(var0) {
               case 2:
               case 3:
               case 7:
               case 8:
               case 10:
               case 11:
                  break;
               case 4:
               case 5:
               case 6:
               default:
                  return getBigDecimal(var1);
               case 9:
                  return Boolean.FALSE;
               case 12:
                  return Boolean.TRUE;
               }
            } else {
               switch(var0) {
               case 1:
                  BigDecimal var4 = getBigDecimal(var2);
                  if (var4 != null) {
                     var4 = var4.negate();
                  }

                  return var4;
               case 2:
               case 3:
               case 7:
               case 8:
               case 10:
               case 11:
                  break;
               case 4:
               case 5:
               case 6:
               default:
                  return getBigDecimal(var2);
               case 9:
                  return Boolean.FALSE;
               case 12:
                  return Boolean.TRUE;
               }
            }
         }
      }

      return null;
   }

   public static double Adoszamit(double var0, double var2, double var4, double var6, double var8) {
      double var10 = 0.0D;
      if (var0 < var6 && var6 <= var2) {
         var10 = FunctionBodies.Tz_round((var6 - var0) * var8 + var4);
      }

      return var10;
   }

   public static int getObjectType(Object var0, Object var1) {
      if (!(var0 instanceof String) && !(var1 instanceof String)) {
         if (!(var0 instanceof BigDecimal) && !(var1 instanceof BigDecimal)) {
            if (!(var0 instanceof Integer) && !(var1 instanceof Integer)) {
               if (var0 instanceof Boolean && var1 instanceof Boolean) {
                  return 6;
               } else if (var0 == null && var1 instanceof Boolean) {
                  return 6;
               } else {
                  return var0 instanceof Boolean && var1 == null ? 6 : 0;
               }
            } else {
               return 5;
            }
         } else {
            return 7;
         }
      } else {
         return 0;
      }
   }

   public static Double checkDouble(Double var0) {
      return var0 == null || !var0.isNaN() && !var0.isInfinite() ? var0 : null;
   }

   public static Double getDouble(Object var0) {
      if (var0 instanceof Double) {
         return (Double)var0;
      } else {
         return var0 == null ? null : Double.valueOf(var0.toString());
      }
   }

   public static Integer getInteger(Object var0) {
      if (var0 instanceof Integer) {
         return (Integer)var0;
      } else {
         return var0 == null ? null : Integer.valueOf(var0.toString());
      }
   }

   public static Boolean getBoolean(Object var0) {
      if (var0 instanceof Boolean) {
         return (Boolean)var0;
      } else {
         return var0 == null ? null : Boolean.valueOf(var0.toString());
      }
   }

   public static BigDecimal getBigDecimal(Object var0) {
      if (var0 instanceof BigDecimal) {
         return (BigDecimal)var0;
      } else if (var0 == null) {
         return null;
      } else {
         String var1 = var0.toString();
         return var1.length() == 0 ? null : new BigDecimal(var1);
      }
   }

   public static String getRoundedValue(String var0, Object var1) {
      if (var1 != null && var1.toString().length() != 0) {
         if (var0 == null) {
            return var1.toString();
         } else {
            try {
               if (FunctionBodies.isRoundAble(var0, var1)) {
                  if (var1.toString().equals("0")) {
                     return "0";
                  }

                  int var2 = FunctionBodies.getPrecision(var0);
                  BigDecimal var3 = round(var1, getObjectType(var1, var1), var2);
                  if (var3.compareTo(BigDecimal.ZERO) == 0) {
                     return var3.stripTrailingZeros().setScale(var2).toPlainString();
                  }

                  return var3.stripTrailingZeros().toPlainString();
               }
            } catch (Exception var4) {
               Tools.eLog(var4, 1);
            }

            return var1.toString();
         }
      } else {
         return "";
      }
   }

   public static BigDecimal round(Object var0, int var1, int var2) throws Exception {
      switch(var1) {
      case 7:
         BigDecimal var3 = getBigDecimal(var0);
         return var3.setScale(var2, 4);
      default:
         throw new Exception("Program hiba, típus eltérés!");
      }
   }

   public static Object add(Object var0, Object var1) throws Exception {
      switch(getObjectType(var0, var1)) {
      case 5:
         if (var0 == null) {
            return getInteger(var1);
         }

         if (var1 == null) {
            return getInteger(var0);
         }

         var0 = new Integer(getInteger(var0) + getInteger(var1));
         break;
      case 7:
         if (var0 == null) {
            return getBigDecimal(var1);
         }

         if (var1 == null) {
            return getBigDecimal(var0);
         }

         var0 = getBigDecimal(var0).add(getBigDecimal(var1));
         break;
      default:
         throw new Exception(FunctionBodies.ID_EX_TYPE_MISMATCH + " " + "Program hiba, típus eltérés!");
      }

      return var0;
   }

   public static Object posAdd(Object var0, Object var1) {
      BigDecimal var2 = getBigDecimal(var0);
      if (var1 != null) {
         BigDecimal var3 = getBigDecimal(var1);
         if (var3.signum() > 0) {
            if (var0 == null) {
               var2 = var3;
            } else {
               var2 = getBigDecimal(var0).add(var3);
            }
         }
      }

      return var2;
   }

   public static Object negAdd(Object var0, Object var1) {
      BigDecimal var2 = getBigDecimal(var0);
      if (var1 != null) {
         BigDecimal var3 = getBigDecimal(var1);
         if (var3.signum() < 0) {
            if (var0 == null) {
               var2 = var3;
            } else {
               var2 = getBigDecimal(var0).add(var3);
            }
         }
      }

      return var2;
   }

   public static BigDecimal division(Object var0, Object var1) {
      try {
         return getBigDecimal(var0).divide(getBigDecimal(var1), 40, 4);
      } catch (Exception var3) {
         return null;
      }
   }
}
