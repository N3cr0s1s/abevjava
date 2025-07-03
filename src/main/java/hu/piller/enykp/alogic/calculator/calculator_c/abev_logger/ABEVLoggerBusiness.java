package hu.piller.enykp.alogic.calculator.calculator_c.abev_logger;

import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.calculator.calculator_c.ExpClass;
import hu.piller.enykp.alogic.calculator.calculator_c.ExpFactory;
import hu.piller.enykp.alogic.fileutil.HeadChecker;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.util.base.Tools;
import me.necrocore.abevjava.NecroFile;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Vector;

public class ABEVLoggerBusiness {
   public static final String E_TYPE_FIELD_CALC = "mezőszámítás";
   public static final String E_TYPE_FIELD_CHECK = "mezőellenőrzés";
   public static final String E_TYPE_FORM = "nyomtatvány";
   public static final String E_TYPE_VARIABLE = "változó";
   public static final String E_TYPE_PAGE_CHECK = "lapengedélyezés";
   private static final String FILE_NAME_EXTENSION = ".txt";
   private static final int FILE_NAME_EXTENSION_LENGTH = ".txt".length();
   private static boolean count_file_name = false;
   private static ABEVLoggerBusiness.LogFileFilter log_file_filter = new ABEVLoggerBusiness.LogFileFilter();
   private static ABEVLoggerBusiness.LogFileComparator log_file_comparator = new ABEVLoggerBusiness.LogFileComparator();
   private static boolean dirty = false;
   private static Object opened = null;
   private static Object form_id = null;
   private static Object unique_id = null;
   private static final Object[] ids = new Object[]{null, null, null};
   private static final Object[] get_ids;
   private static final Object[] get_pa_data;
   private static final String C_TRUE = "true";
   private static final String C_FALSE = "false";
   private static final String NIL_STR = "nil";

   public static Object open(Calculator var0) {
      if (opened == null) {
         Object var1 = null;
         File var4 = null;

         try {
            acquireDynamicInfos(var0.getBookModel());
            Object var2 = var0.getProperty("prop.usr.root");
            Object var3 = var0.getProperty("prop.usr.saves");
            var4 = new NecroFile(var2 == null ? "." : var2.toString(), var3 == null ? "" : var3.toString());
            var2 = var0.getProperty("prop.dynamic.opened_file");
            if (var2 == null) {
               try {
                  var2 = var0.getBookModel().cc.getLoadedfile();
               } catch (Exception var13) {
                  Tools.eLog(var13, 0);
               }
            }

            File var5 = new NecroFile(var2 == null ? "." + File.separator + form_id : var2.toString());
            if (!var4.getPath().equalsIgnoreCase(var5.getParent())) {
               var1 = new NecroFile(var4, var5.getName());
            } else {
               var1 = var5.getPath();
            }
         } catch (Exception var14) {
            Tools.eLog(var14, 0);
         }

         String var6 = var1 == null ? form_id + "" : var1.toString();
         if (count_file_name) {
            String var7 = var6 + "_";
            var6 = var7 + "1" + ".txt";
            log_file_filter.setFileNameStart(var7);
            File[] var8 = (new NecroFile(var4 == null ? "." : var4.getPath())).listFiles(log_file_filter);
            if (var8.length > 0) {
               log_file_comparator.setFileNameStart(var7);
               Arrays.sort(var8, log_file_comparator);
               String var9 = var8[0].getPath();
               String var10 = var9.substring(var7.length(), var9.length() - FILE_NAME_EXTENSION_LENGTH);

               try {
                  var6 = var7 + (Integer.parseInt(var10) + 1) + ".txt";
               } catch (NumberFormatException var12) {
                  Tools.eLog(var12, 0);
               }
            }
         } else {
            var6 = var6 + ".txt";
         }

         ABEVLogger.open(new NecroFile(var6));
         System.out.println(">ABEVLog: " + var6);
         opened = new Object();
         return opened;
      } else {
         return null;
      }
   }

   public static void acquireDynamicInfos(BookModel var0) {
      try {
         form_id = var0.get_formid();
         Object var1 = HeadChecker.getInstance().getPAData(new Object[]{"Vonatkozik"}, form_id, var0);
         unique_id = "";
         if (var1 instanceof Hashtable && (var1 = ((Hashtable)var1).get("Vonatkozik")) instanceof Vector) {
            Vector var2 = (Vector)var1;
            if (var2.size() > 0) {
               unique_id = var2.get(0);
            }
         }
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
      }

   }

   public static void close(Object var0) {
      if (opened == var0) {
         ABEVLogger.close();
         opened = null;
      }

   }

   public static void write() {
      if (dirty) {
         ABEVLogger.write();
         dirty = false;
      }

   }

   public static void setExpressionType(Object var0) {
      ABEVLogger.expression_type = var0 == null ? "" : var0.toString();
      dirty = true;
   }

   public static void setExpressionIdByFId(Object var0, Object var1, BookModel var2) {
      PageModel var3 = null;

      try {
         FormModel var4 = var2.get(ExpFactory.form_id);
         int var5 = var4.get_field_pageindex(var1 == null ? "" : var1.toString());
         var3 = var4.get(var5);
      } catch (Exception var6) {
         Tools.eLog(var6, 0);
      }

      if (var3 != null) {
         setExpressionIdByPId(var0, var3.pid);
      } else {
         setExpressionIdByPId(var0, (Object)null);
      }

   }

   public static void setExpressionIdByPId(Object var0, Object var1) {
      String var2 = var0 == null ? "" : var0.toString();
      if (var1 == null) {
         String var10000 = "";
      } else {
         var1.toString();
      }

      String var4 = unique_id == null ? "" : unique_id.toString();
      ABEVLogger.expression_id = form_id + "_" + var2 + "_" + var4;
      dirty = true;
   }

   public static void setExpressionResult(Object var0) {
      ABEVLogger.expression_result = var0 == null ? "" : var0.toString();
      dirty = true;
   }

   public static void setErrorMessage(Object var0) {
      ABEVLogger.error_message = var0 == null ? "" : var0.toString();
      dirty = true;
   }

   public static void setErrorMessageType(Object var0) {
      if (var0 != IErrorList.LEVEL_FATAL_ERROR && var0 != IErrorList.LEVEL_SHOW_FATAL_ERROR) {
         if (var0 != IErrorList.LEVEL_ERROR && var0 != IErrorList.LEVEL_SHOW_ERROR) {
            if (var0 != IErrorList.LEVEL_WARNING && var0 != IErrorList.LEVEL_SHOW_WARNING) {
               if (var0 == IErrorList.LEVEL_MESSAGE || var0 == IErrorList.LEVEL_SHOW_MESSAGE) {
                  var0 = "Message";
               }
            } else {
               var0 = "Warning";
            }
         } else {
            var0 = "Error";
         }
      } else {
         var0 = "Fatal Error";
      }

      ABEVLogger.error_message_type = var0 == null ? "" : var0.toString();
      dirty = true;
   }

   public static void setDIDCode(Object var0) {
      ABEVLogger.did_code = var0 == null ? "" : var0.toString();
      dirty = true;
   }

   public static void setVIDCode(Object var0) {
      ABEVLogger.vid_code = var0 == null ? "" : var0.toString();
      dirty = true;
   }

   public static void setCIDCode(Object var0) {
      setCIDCode(var0, -1);
   }

   public static void setCIDCode(Object var0, int var1) {
      String var2 = var0 == null ? "" : var0.toString();
      ids[0] = var2;

      try {
         Object[] var3 = (Object[])((Object[])MetaInfo.getInstance().getIds(ids, form_id));
         setVIDCode(var3[1]);
         setDIDCode(var3[2]);
      } catch (Exception var4) {
         Tools.eLog(var4, 0);
      }

      if (var1 >= 0) {
         ABEVLogger.cid_code = TemplateUtils.getInstance().DSIdToCId(var1 + "_" + var2, form_id);
      } else {
         ABEVLogger.cid_code = var2;
      }

      dirty = true;
   }

   public static void setExpression(Object var0) {
      try {
         ABEVLogger.expression = var0 instanceof ExpClass ? createStringExpression((ExpClass)var0) : "";
      } catch (Exception var2) {
         ABEVLogger.expression = var0 == null ? "" : var0.toString();
      }

      dirty = true;
   }

   public static void release() {
      if (opened != null) {
         close(opened);
      }

      opened = null;
      form_id = null;
      unique_id = null;
      ids[0] = null;
      get_ids[2] = null;
      get_pa_data[2] = null;
   }

   private static String createStringExpression(ExpClass var0) {
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

   public static void clear() {
      ABEVLogger.clear();
   }

   static {
      get_ids = new Object[]{"get_ids", ids, null};
      get_pa_data = new Object[]{"get_pa_data", new Object[]{"Vonatkozik"}, null};
   }

   private static class LogFileComparator implements Comparator {
      private int file_name_start_len;

      private LogFileComparator() {
      }

      void setFileNameStart(String var1) {
         this.file_name_start_len = var1.length();
      }

      public int compare(Object var1, Object var2) {
         String var3 = ((File)var2).getPath();
         String var4 = ((File)var1).getPath();

         try {
            int var5 = Integer.parseInt(var3.substring(this.file_name_start_len, var3.length() - ABEVLoggerBusiness.FILE_NAME_EXTENSION_LENGTH));
            int var6 = Integer.parseInt(var4.substring(this.file_name_start_len, var4.length() - ABEVLoggerBusiness.FILE_NAME_EXTENSION_LENGTH));
            return var6 > var5 ? -1 : (var6 == var5 ? 0 : 1);
         } catch (Exception var7) {
            Tools.eLog(var7, 0);
            return 1;
         }
      }

      // $FF: synthetic method
      LogFileComparator(Object var1) {
         this();
      }
   }

   private static class LogFileFilter implements FileFilter {
      private String file_name_start;
      private int file_name_start_len;

      private LogFileFilter() {
      }

      void setFileNameStart(String var1) {
         this.file_name_start = var1;
         this.file_name_start_len = var1.length();
      }

      public boolean accept(File var1) {
         if (var1.isDirectory()) {
            return false;
         } else {
            String var2 = var1.getPath();
            if (var2.startsWith(this.file_name_start) && var2.endsWith(".txt")) {
               try {
                  Integer.parseInt(var2.substring(this.file_name_start_len, var2.length() - ABEVLoggerBusiness.FILE_NAME_EXTENSION_LENGTH));
                  return true;
               } catch (NumberFormatException var4) {
                  Tools.eLog(var4, 0);
               }
            }

            return false;
         }
      }

      // $FF: synthetic method
      LogFileFilter(Object var1) {
         this();
      }
   }
}
