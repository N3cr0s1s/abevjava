package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.gui.model.BookModel;

public class PIdHelper {
   public static final int IDX_PAGE_ID = 0;
   public static final int IDX_RETURN_VALUE = 1;
   public static final int IDX_MESSAGE = 2;
   public static final int IDX_MESSAGE_LEVEL = 3;
   private static BookModel info_object = null;

   public static void release() {
      info_object = null;
   }

   public static String getPageId(Object var0) {
      return (String)((String)((Object[])((Object[])var0))[0]);
   }

   public static void setReturnValue(Object var0, Object var1) {
      ((Object[])((Object[])var0))[1] = var1;
   }

   public static void setInfoObject(BookModel var0) {
      info_object = var0;
   }

   public static void setMessage(Object var0, Object var1) {
      ((Object[])((Object[])var0))[2] = var1;
   }

   public static void setMessageLevel(Object var0, Object var1) {
      ((Object[])((Object[])var0))[3] = var1;
   }

   public static Object getReturnValue(Object var0) {
      return ((Object[])((Object[])var0))[1];
   }

   public static Object getInfoObject() {
      return info_object;
   }

   public static Object getMessage(Object var0) {
      return ((Object[])((Object[])var0))[2];
   }

   public static Object getMessageLevel(Object var0) {
      return ((Object[])((Object[])var0))[3];
   }

   public static String toString(Object var0) {
      return var0 == null ? "" : var0.toString();
   }
}
