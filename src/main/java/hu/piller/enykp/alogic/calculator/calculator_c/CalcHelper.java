package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.util.base.Tools;
import java.util.Map;

public class CalcHelper {
   private static boolean show_times = false;
   public static final String TAG_CALC = "calc";
   public static final String ATTR_ON_EVENT = "on_event";
   public static final String ATTR_TARGET_TYPE = "target_type";
   public static final String ATTR_TARGET_ID = "target_id";
   public static final String ATTR_TARGET_BIND = "target_bind";
   public static final String ATTR_MSG = "msg";
   public static final String ATTR_ERROR_LEVEL = "msglevel";
   public static final String ATTR_CEXP = "cexp";
   public static final String ATTR_ID = "id";
   public static final String ATTR_ERRORCODE = "errorcode";
   public static final String ATTR_ERRORCODE_LEVEL = "errorlevel";
   public static final String ATTR_ROLE = "role";
   public static final String ATTR_WEB_MSG = "web_msg";
   public static final int CR_IDX_EVENT = 0;
   public static final int CR_IDX_TARGET_TYPE = 1;
   public static final int CR_IDX_TARGET_ID = 2;
   public static final int CR_IDX_TARGET_BIND = 3;
   public static final int CR_IDX_MSG = 4;
   public static final int CR_IDX_ERROR_LEVEL = 5;
   public static final int CR_IDX_CEXP = 6;
   public static final int CR_IDX_ID = 7;
   public static final int CR_IDX_ERRORCODE = 8;
   public static final int CR_IDX_ERRORCODE_LEVEL = 9;
   public static final int CR_IDX_ROLE = 10;
   public static final String EVENT_FIELD_CALCULATION = "field_calc";
   public static final String EVENT_FIELD_CHECK = "field_check";
   public static final String EVENT_PAGE_CALCULATION = "page_calc";
   public static final String EVENT_PAGE_CHECK = "page_check";
   public static final String EVENT_FORM_CALCULATION = "form_calc";
   public static final String EVENT_FORM_CHECK = "form_check";
   public static final String EVENT_PRE_GEN_CALC = "pre_gen_calc";
   public static final String EVENT_POST_GEN_CALC = "post_gen_calc";
   public static final String EVENT_BETOLT_ERTEK = "betolt_ertek";
   public static final String EVENT_MULTI_FORM_LOAD = "multi_form_load";
   public static final String EVENT_DPAGE_COUNT_CHANGE = "dpage_count_change";
   public static final String EVENT_LOOKUP_CREATE = "lookup_create";
   public static final String TARGET_FIELD = "field";
   public static final String TARGET_PAGE = "page";
   public static final String TARGET_FORM = "form";
   public static final String BIND_YES = "yes";
   public static final String BIND_NO = "no";
   private static final int CONST_CALC_RECORD_LENGTH = 11;

   public static Object[] getCalcRecordtype(Object var0) {
      return (Object[])((Object[])var0);
   }

   public static boolean isCalcRecord(Object var0) {
      try {
         Object[] var1 = getCalcRecordtype(var0);
         if (var1 != null && var1.length == 11) {
            return true;
         }
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
      }

      return false;
   }

   public static Object createEmptyCalcRecord() {
      return new Object[11];
   }

   public static void emptyCalcRecord(Object var0) {
      setEvent(var0, (Object)null);
      setTargetType(var0, (Object)null);
      setTargetId(var0, (Object)null);
      setTargetBind(var0, (Object)null);
      setMsg(var0, (Object)null);
      setErrorLevel(var0, (Object)null);
      setExp(var0, (ExpClass)null);
      setId(var0, (Object)null);
   }

   public static void copy(Object var0, Object var1, boolean var2) {
      String var3 = getEvent(var0);
      if (var2 || var3 != null) {
         setEvent(var1, var3);
      }

      var3 = getTargetType(var0);
      if (var2 || var3 != null) {
         setTargetType(var1, var3);
      }

      var3 = getTargetId(var0);
      if (var2 || var3 != null) {
         setTargetId(var1, var3);
      }

      Boolean var4 = getTargetBind(var0);
      if (var2 || var4 != null) {
         setTargetBind(var1, var4);
      }

      var3 = getMsg(var0);
      if (var2 || var3 != null) {
         setMsg(var1, var3);
      }

      Integer var5 = getErrorLevel(var0);
      if (var2 || var5 != null) {
         setErrorLevel(var1, var5);
      }

      ExpClass var6 = getExp(var0);
      if (var2 || var6 != null) {
         setExp(var1, (ExpClass)var6);
      }

      var3 = getId(var0);
      if (var2 || var3 != null) {
         setId(var1, var3);
      }

   }

   public static Object createCalcRecord(Map var0, String var1, int var2, boolean var3) throws Exception {
      long var4 = System.currentTimeMillis();
      Object[] var8 = getCalcRecordtype(createEmptyCalcRecord());
      if (var0 != null) {
         Object var9;
         if ((var9 = var0.get("on_event")) != null) {
            var8[0] = var9;
         }

         if ((var9 = var0.get("target_type")) != null) {
            var8[1] = var9;
         }

         if ((var9 = var0.get("target_id")) != null) {
            var8[2] = var9;
         }

         if ((var9 = var0.get("target_bind")) != null) {
            var8[3] = getTargetBindBoolean(var9);
         }

         if ((var9 = var0.get("msg")) != null) {
            var8[4] = var9;
         }

         if ((var9 = var0.get("web_msg")) != null && (isInGeneratorMod() || isInONYACheckMod())) {
            var8[4] = var9;
         }

         if ((var9 = var0.get("errorcode")) != null) {
            var8[8] = var9;
         }

         if ((var9 = var0.get("errorlevel")) != null) {
            var8[9] = var9;
         }

         if ((var9 = var0.get("msglevel")) != null) {
            var8[5] = getErrorLevelInteger(var9);
         }

         if ((var9 = var0.get("role")) != null) {
            var8[10] = getRoleNumber(var9);
         }

         if ((var9 = var0.get("cexp")) != null) {
            Object var10 = var8[3];
            boolean var11;
            if (var10 instanceof Boolean) {
               var11 = (Boolean)var10;
            } else {
               var11 = false;
            }

            var10 = var8[0];
            boolean var12;
            if (var10 instanceof String) {
               var12 = ((String)var10).equalsIgnoreCase("field_check") || ((String)var10).equalsIgnoreCase("form_check");
            } else {
               var12 = false;
            }

            if (var11 && var3) {
               var8[6] = ExpFactory.createExp((String)var9, getAttributeStringValue(var0, var1), var2, var12, (String)var8[2]);
            } else {
               var8[6] = ExpFactory.createExp((String)var9, (String)null, var2, var12, (String)var8[2]);
            }
         }

         if ((var9 = var0.get("id")) != null) {
            var8[7] = var9;
         }
      }

      long var6 = System.currentTimeMillis();
      if (show_times && var6 - var4 > 0L) {
         System.out.println("calculator.createCalcRecord: " + (var6 - var4));
      }

      return var8;
   }

   private static boolean isInGeneratorMod() {
      return "10".equals(MainFrame.hasznalati_mod);
   }

   private static boolean isInONYACheckMod() {
      return MainFrame.onyaCheckMode;
   }

   public static Integer getErrorLevelInteger(Object var0) {
      if (var0 != null) {
         if (var0 instanceof String) {
            String var1 = (String)var0;
            if ("error".equalsIgnoreCase(var1)) {
               return IErrorList.LEVEL_ERROR;
            }

            if ("warning".equalsIgnoreCase(var1)) {
               return IErrorList.LEVEL_WARNING;
            }

            if ("fatalerror".equalsIgnoreCase(var1)) {
               return IErrorList.LEVEL_FATAL_ERROR;
            }

            return IErrorList.LEVEL_MESSAGE;
         }

         if (var0 instanceof Integer) {
            return (Integer)var0;
         }

         try {
            return Integer.valueOf(var0.toString());
         } catch (NumberFormatException var2) {
            Tools.eLog(var2, 0);
         }
      }

      return null;
   }

   public static Integer getRoleNumber(Object var0) {
      if (var0 != null) {
         if (var0 instanceof String) {
            return Integer.valueOf((String)var0);
         }

         if (var0 instanceof Integer) {
            return (Integer)var0;
         }

         try {
            return Integer.valueOf(var0.toString());
         } catch (NumberFormatException var2) {
            Tools.eLog(var2, 0);
         }
      }

      return null;
   }

   public static Boolean getTargetBindBoolean(Object var0) {
      if (var0 instanceof String) {
         return "yes".equalsIgnoreCase(var0.toString()) ? Boolean.TRUE : Boolean.FALSE;
      } else {
         return var0 instanceof Boolean ? (Boolean)var0 : Boolean.TRUE;
      }
   }

   private static String getAttributeStringValue(Map var0, Object var1) {
      if (var0 != null && var1 != null) {
         Object var2 = var0.get(var1);
         return var2 == null ? null : var2.toString();
      } else {
         return null;
      }
   }

   public static String getEvent(Object var0) {
      Object[] var1 = getCalcRecordtype(var0);
      if (var1 != null) {
         Object var2 = var1[0];
         return var2 == null ? null : var2.toString();
      } else {
         return null;
      }
   }

   public static void setEvent(Object var0, Object var1) {
      getCalcRecordtype(var0)[0] = var1 == null ? null : var1.toString();
   }

   public static String getTargetType(Object var0) {
      Object[] var1 = getCalcRecordtype(var0);
      if (var1 != null) {
         Object var2 = var1[1];
         return var2 == null ? null : var2.toString();
      } else {
         return null;
      }
   }

   public static void setTargetType(Object var0, Object var1) {
      getCalcRecordtype(var0)[1] = var1 == null ? null : var1.toString();
   }

   public static String getTargetId(Object var0) {
      Object[] var1 = getCalcRecordtype(var0);
      if (var1 != null) {
         Object var2 = var1[2];
         return var2 == null ? null : var2.toString();
      } else {
         return null;
      }
   }

   public static void setTargetId(Object var0, Object var1) {
      getCalcRecordtype(var0)[2] = var1 == null ? null : var1.toString();
   }

   public static Boolean getTargetBind(Object var0) {
      Object[] var1 = getCalcRecordtype(var0);
      if (var1 != null) {
         Object var2 = var1[3];
         return var2 instanceof Boolean ? (Boolean)var2 : null;
      } else {
         return null;
      }
   }

   public static void setTargetBind(Object var0, Object var1) {
      if (var1 instanceof Boolean || var1 == null) {
         getCalcRecordtype(var0)[3] = var1 == null ? null : var1;
      }

   }

   public static String getMsg(Object var0) {
      Object[] var1 = getCalcRecordtype(var0);
      if (var1 != null) {
         Object var2 = var1[4];
         return var2 == null ? null : var2.toString();
      } else {
         return null;
      }
   }

   public static void setMsg(Object var0, Object var1) {
      getCalcRecordtype(var0)[4] = var1 == null ? null : var1.toString();
   }

   public static String getErrorCode(Object var0) {
      Object[] var1 = getCalcRecordtype(var0);
      Object var2 = null;
      if (var1 != null) {
         if (var1.length > 8) {
            var2 = var1[8];
         }

         return var2 == null ? null : var2.toString();
      } else {
         return null;
      }
   }

   public static void setErrorCode(Object var0, Object var1) {
      getCalcRecordtype(var0)[8] = var1 == null ? null : var1.toString();
   }

   public static String getErrorCodeLevel(Object var0) {
      Object[] var1 = getCalcRecordtype(var0);
      Object var2 = null;
      if (var1 != null) {
         if (var1.length > 9) {
            var2 = var1[9];
         }

         return var2 == null ? null : var2.toString();
      } else {
         return null;
      }
   }

   public static void setErrorCodeLevel(Object var0, Object var1) {
      getCalcRecordtype(var0)[9] = var1 == null ? null : var1.toString();
   }

   public static Integer getErrorLevel(Object var0) {
      Object[] var1 = getCalcRecordtype(var0);
      if (var1 != null) {
         Object var2 = var1[5];
         return var2 instanceof Integer ? (Integer)var2 : null;
      } else {
         return null;
      }
   }

   public static void setErrorLevel(Object var0, Object var1) {
      if (var1 instanceof Integer || var1 == null) {
         getCalcRecordtype(var0)[5] = var1 == null ? null : var1;
      }

   }

   public static Integer getRole(Object var0) {
      Object[] var1 = getCalcRecordtype(var0);
      if (var1 != null) {
         Object var2 = var1[10];
         return var2 instanceof Integer ? (Integer)var2 : null;
      } else {
         return null;
      }
   }

   public static void setRole(Object var0, Object var1) {
      if (var1 instanceof Integer || var1 == null) {
         getCalcRecordtype(var0)[10] = var1 == null ? null : var1;
      }

   }

   public static ExpClass getExp(Object var0) {
      Object[] var1 = getCalcRecordtype(var0);
      return var1 != null ? (ExpClass)var1[6] : null;
   }

   public static void setExp(Object var0, ExpClass var1) {
      getCalcRecordtype(var0)[6] = var1 == null ? null : var1;
   }

   public static String getId(Object var0) {
      Object[] var1 = getCalcRecordtype(var0);
      if (var1 != null) {
         Object var2 = var1[7];
         return var2 == null ? null : var2.toString();
      } else {
         return null;
      }
   }

   public static void setId(Object var0, Object var1) {
      getCalcRecordtype(var0)[7] = var1;
   }
}
