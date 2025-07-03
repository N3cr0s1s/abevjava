package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0;

import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class FIdHelper {
   public static final int IDX_FIELD_ID = 0;
   public static final int IDX_PAGE_NUMBER = 1;
   public static final int IDX_DPAGE_NUMBER = 2;
   public static final int IDX_RETURN_VALUE = 2;
   public static final int IDX_FIELD_PAGE_NAME = 0;
   public static final int IDX_FIELD_ISDYNAMIC = 3;
   public static final int IDX_CALC_FIELD_ID = 0;
   public static final int IDX_CALC_ACT_PAGE_NUM = 1;
   public static final int IDX_CALC_ISDYNAMIC = 2;
   public static final int IDX_CALC_VALUE = 3;
   private static BookModel info_object = null;
   private static Object[] g_ds_key = new Object[]{null, null};
   private static Integer zeroDsKey = new Integer(0);
   public static Hashtable fieldInfoTables = new Hashtable();
   public static boolean otherDynPage = false;
   public static Hashtable dynPageSettings = new Hashtable();
   private static boolean isFormCheck = false;

   public static Object createId(Object var0, Object var1, Object var2) {
      return new Object[]{var0, var1, var2, null, null, null, null, null};
   }

   public static Object createId(Object var0) {
      return new Object[]{var0, null, null, null, null, null, null, null};
   }

   public static void setInfoObject(BookModel var0) {
      info_object = var0;
   }

   public static String getFieldId(Object var0) {
      return (String)((Object[])((Object[])var0))[0];
   }

   public static Object getInfoObject() {
      return info_object;
   }

   private static Object[] getActiveField() {
      return FunctionBodies.g_current_field_id;
   }

   public static int getPageNumber(Object var0) {
      if (var0 == null) {
         return 0;
      } else {
         Object var1 = ((Object[])((Object[])var0))[1];
         Integer var2 = (Integer)var1;
         return var2;
      }
   }

   public static boolean isFieldDynamic(Object var0) throws Exception {
      String var1;
      if (var0 instanceof String) {
         var1 = (String)var0;
      } else {
         Object[] var2 = (Object[])((Object[])var0);
         var1 = (String)var2[0];
      }

      try {
         FormModel var4 = info_object.get(FunctionBodies.g_active_form_id);
         return ((PageModel)var4.fids_page.get(var1)).dynamic;
      } catch (Exception var3) {
         throw new Exception("Érvénytelen mező információt kapott mező információ kéréskor ! (" + var1 + ")");
      }
   }

   public static String getPageId(Object var0) throws Exception {
      String var1;
      if (var0 instanceof String) {
         var1 = (String)var0;
      } else {
         Object[] var2 = (Object[])((Object[])var0);
         var1 = (String)var2[0];
      }

      try {
         FormModel var4 = info_object.get(FunctionBodies.g_active_form_id);
         return ((PageModel)var4.fids_page.get(var1)).name;
      } catch (Exception var3) {
         throw new Exception("Érvénytelen mező információt kapott mező információ kéréskor ! (" + var1 + ")");
      }
   }

   public static Object[] getDataStoreKey(Object var0, Object var1, Object var2, Object var3) {
      return getDataStoreKey(var1);
   }

   public static Object[] getDataStoreKey(Object var0) {
      g_ds_key[0] = getDPageNumber(var0);
      g_ds_key[1] = var0 == null ? "" : var0.toString();
      return g_ds_key;
   }

   public static Integer getDPageNumber(Object var0) {
      Integer var2 = zeroDsKey;

      try {
         boolean var3 = isFieldDynamic(var0);
         if (var3) {
            Object[] var4 = getActiveField();
            if (var4 != null) {
               var2 = (Integer)var4[1];
               if (var2 == null) {
                  var2 = zeroDsKey;
               }
            }

            if (isFormCheck) {
               var2 = getPageCount(var0) - 1;
            }

            if (otherDynPage) {
               String var1 = getPageId(var0);
               if (dynPageSettings.containsKey(var1)) {
                  return (Integer)((Vector)dynPageSettings.get(var1)).lastElement();
               }
            }
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return var2;
   }

   public static void setDinPageNumber(Hashtable var0) {
      try {
         otherDynPage = true;

         String var2;
         Vector var3;
         for(Enumeration var1 = var0.keys(); var1.hasMoreElements(); var3.add(var0.get(var2))) {
            var2 = (String)var1.nextElement();
            var3 = (Vector)dynPageSettings.get(var2);
            if (var3 == null) {
               var3 = new Vector();
               dynPageSettings.put(var2, var3);
            }
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void resetDinPageNumber(Hashtable var0) {
      try {
         otherDynPage = false;
         dynPageSettings = new Hashtable();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static void setFormCheck(boolean var0) {
      isFormCheck = var0;
   }

   public static int getPageCount(Object var0) throws Exception {
      String var1;
      if (var0 instanceof String) {
         var1 = (String)var0;
      } else {
         Object[] var2 = (Object[])((Object[])var0);
         var1 = (String)var2[0];
      }

      int[] var4 = info_object.get_pagecounts();
      int var3 = info_object.get(FunctionBodies.g_active_form_id).get_field_pageindex(var1);
      return var4[var3];
   }
}
