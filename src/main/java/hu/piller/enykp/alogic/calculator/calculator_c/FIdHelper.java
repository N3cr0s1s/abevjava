package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.util.base.Tools;
import java.util.Arrays;
import java.util.Vector;

public class FIdHelper {
   public static final int IDX_FIELD_ID = 0;
   public static final int IDX_PAGE_NUMBER = 1;
   public static final int IDX_RETURN_VALUE = 2;
   public static final int IDX_MASTER_CASE_ID = 3;
   public static final int IDX_ERROR_MESSAGE = 4;
   public static final int IDX_ERROR_LEVEL = 5;
   public static final int IDX_FORM_ID = 6;
   public static final int IDX_DONT_CALC = 7;
   private static final int IDX_TAB_ID = 0;
   private static BookModel info_object = null;
   private static final Object[] get_field_info = new Object[]{"get_field_info", null, null};

   public static void release() {
      info_object = null;
   }

   public static Object createId(Object var0, Object var1, Object var2) {
      return new Object[]{var0, var1, var2, null, null, null, null, null};
   }

   public static void clearId(Object var0) {
      Object[] var1 = (Object[])((Object[])var0);
      int var2 = 0;

      for(int var3 = var1.length; var2 < var3; ++var2) {
         var1[var2] = null;
      }

   }

   public static void setFieldId(Object var0, Object var1) {
      ((Object[])((Object[])var0))[0] = var1;
   }

   public static void setDPageNumber(Object var0, int var1) {
      ((Object[])((Object[])var0))[1] = new Integer(var1);
   }

   public static void setInfoObject(BookModel var0) {
      info_object = var0;
   }

   public static void setReturnValue(Object var0, Object var1) {
      ((Object[])((Object[])var0))[2] = var1;
   }

   public static void setDataStoreId(Object var0, Object var1) {
      ((Object[])((Object[])var0))[3] = var1;
   }

   public static void setErrorMessage(Object var0, Object var1) {
      ((Object[])((Object[])var0))[4] = var1;
   }

   public static void setErrorLevel(Object var0, Object var1) {
      ((Object[])((Object[])var0))[5] = var1;
   }

   public static void setFormId(Object var0, Object var1) {
      ((Object[])((Object[])var0))[6] = var1;
   }

   public static void setDontCalc(Object var0, Object var1) {
      ((Object[])((Object[])var0))[7] = var1;
   }

   public static String getFieldId(Object var0) {
      return (String)((Object[])((Object[])var0))[0];
   }

   public static Object getFormId(Object var0) {
      return ((Object[])((Object[])var0))[6];
   }

   public static Object getDontCalc(Object var0) {
      try {
         return ((Object[])((Object[])var0))[7];
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
         return null;
      }
   }

   public static int getDPageNumber(Object var0) {
      Object var1 = ((Object[])((Object[])var0))[1];
      Integer var2 = (Integer)var1;
      return var2;
   }

   public static Object getInfoObject() {
      return info_object;
   }

   public static int getPageCount(Object var0) throws Exception {
      String var1 = getFieldId(var0);
      int[] var2 = info_object.get_pagecounts();
      int var3 = info_object.get(ExpFactory.form_id).get_field_pageindex(var1);
      return var2[var3];
   }

   public static Object getReturnValue(Object var0) {
      return ((Object[])((Object[])var0))[2];
   }

   public static Object getDataStoreKey(String var0) {
      if (info_object == null) {
         return null;
      } else {
         int var3 = getDPageNumber(var0);
         Integer var1 = var3 >= 0 ? new Integer(var3) : new Integer(0);
         return new Object[]{var1, var0};
      }
   }

   public static Object getDataStoreKey(Object var0) {
      if (info_object == null) {
         return null;
      } else if (var0 instanceof String) {
         return getDataStoreKey((String)var0);
      } else if (var0 instanceof Object[]) {
         Object var1 = ((Object[])((Object[])var0))[1];
         String var2 = "" + ((Object[])((Object[])var0))[0];
         return new Object[]{var1, var2};
      } else {
         return null;
      }
   }

   public static Object getDataStoreKey(Object var0, Object var1) {
      if (info_object == null) {
         return null;
      } else {
         Integer var2 = new Integer(getDPageNumber(var0));
         String var3 = var1 == null ? "" : var1.toString();
         return new Object[]{var2, var3};
      }
   }

   public static String toString(Object var0) {
      return (Object[])((Object[])var0) == null ? "" : (new Vector(Arrays.asList((Object[])((Object[])var0)))).toString();
   }
}
