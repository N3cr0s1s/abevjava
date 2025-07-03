package hu.piller.enykp.alogic.filepanels.mohu;

import hu.piller.enykp.alogic.ebev.SendParams;
import hu.piller.enykp.alogic.uploader.FeltoltesValasz;
import hu.piller.enykp.alogic.uploader.IUploader;
import hu.piller.enykp.alogic.uploader.Return;
import hu.piller.enykp.alogic.uploader.UploaderException;
import hu.piller.enykp.alogic.uploader.UploaderFactory;
import hu.piller.enykp.kauclient.KauClientException;
import hu.piller.enykp.niszws.util.KauAuthHelper;
import hu.piller.enykp.niszws.util.KauSessionTimeoutHandler;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;

public class MohuTools {
   public static final int BUFFER_SIZE = 65536;
   public static final long UK_MAX_FILE_SIZE = 524288000L;
   public static final String UK_MAX_FILE_SIZE_STRING = "500 Mbyte";
   public static final long CK_HK_MAX_FILE_SIZE = 524288000L;
   public static final String CK_HK_MAX_FILE_SIZE_STRING = "500 Mbyte";
   public static final String KAU_CANCEL_MESSAGE = "Megszakítva";
   public static final String KAU_TIMEOUT_MESSAGE = "Munkamenet nem érvényes, lépjen be újra!";
   public static final String KAU_TIMEOUT_QUESTION = "Munkamenetének érvényességi ideje lejárt! A művelet megismétléséhez újra bejelentkezik?";
   public static final String KAU_MESSAGE_START = "ANYK_KAU";
   public static final String[] repeatCancel = new String[]{"Bejelentkezés", "Mégsem"};
   public static final int TYPE_HIVATALI = 0;
   public static final int TYPE_EPER = 1;
   private SendParams sp;

   public MohuTools(SendParams var1) {
      this.sp = var1;
   }

   public synchronized boolean moveFile(String var1) throws Exception {
      if (disableCopyToSentDir() == 1) {
         return true;
      } else {
         File var2 = new File(var1);
         File var3 = new File(this.sp.sentPath + var2.getName());
         BufferedOutputStream var4 = null;
         BufferedInputStream var5 = null;

         try {
            var5 = new BufferedInputStream(new FileInputStream(var2));
            var4 = new BufferedOutputStream(new FileOutputStream(var3));
            byte[] var6 = new byte[65536];

            int var7;
            while((var7 = var5.read(var6, 0, 65536)) != -1) {
               var4.write(var6, 0, var7);
            }

            var4.flush();
            var4.close();
            var5.close();
            return removeFile(var2);
         } catch (Exception var10) {
            var10.printStackTrace();

            try {
               if (var5 != null) {
                  var5.close();
               }
            } catch (Exception var9) {
               Tools.eLog(var10, 0);
            }

            try {
               if (var4 != null) {
                  var4.close();
               }
            } catch (Exception var8) {
               Tools.eLog(var10, 0);
            }

            removeFile(var3);
            return false;
         }
      }
   }

   private static synchronized boolean removeFile(File var0) {
      try {
         return var0.isDirectory() && var0.listFiles().length > 0 ? false : var0.delete();
      } catch (Exception var2) {
         return false;
      }
   }

   public FeltoltesValasz[] callWS(String[] var1, boolean var2, String var3) throws Exception {
      long[] var4 = new long[var1.length];

      for(int var5 = 0; var5 < var1.length; ++var5) {
         File var6 = new File(var1[var5]);
         var4[var5] = var6.length();
         var1[var5] = var6.getName();
      }

      String var13 = null;
      if (var2) {
         if (var3 == null) {
            var13 = KauAuthHelper.getInstance().getAnyGateId();
         } else {
            var13 = var3;
         }
      }

      if (var13 == null) {
         var13 = "";
      }

      FeltoltesValasz[] var7 = new FeltoltesValasz[var1.length];

      try {
         for(int var8 = 0; var8 < var1.length; ++var8) {
            if ("".equals(var13)) {
               if (var4[var8] > 524288000L) {
                  var7[var8] = new FeltoltesValasz(var1[var8], false, (String)null, "Az ügyfélkapura legfeljebb 500 Mbyte méretű fájl tölthető fel! (jelenlegi : " + var4[var8] + " byte)");
                  continue;
               }
            } else if (var4[var8] > 524288000L) {
               var7[var8] = new FeltoltesValasz(var1[var8], false, (String)null, "Az Cégkapura/Hivatali kapura legfeljebb 500 Mbyte méretű fájl tölthető fel! (jelenlegi : " + var4[var8] + " byte)");
               continue;
            }

            if (KauSessionTimeoutHandler.getInstance().hasCachedKauResult() && !KauSessionTimeoutHandler.getInstance().isUploadable(var4[var8])) {
               throw new Exception("Munkamenet nem érvényes, lépjen be újra!");
            }

            IUploader var9 = UploaderFactory.createUploaderForUgyfelkapuHivatalikapu();
            Return var14;
            if ("".equals(var13)) {
               var14 = var9.upload(new String[]{var1[var8]}, (String)null, (char[])null, (String)null, (char[])null, false, false, "0", false);
            } else {
               var14 = var9.upload(new String[]{var1[var8]}, (String)null, (char[])null, var13, (char[])null, false, false, "0", false);
            }

            if (!var14.getCauseOfFail().equals("")) {
               throw new Exception(var14.getCauseOfFail());
            }

            var7[var8] = var14.getResults()[0];
         }

         return var7;
      } catch (UploaderException var10) {
         KauAuthHelper.getInstance().setAnyGateId("");
         ErrorList.getInstance().writeError("KR Feltöltés", var10.getMessage(), var10, (Object)null);
         throw var10;
      } catch (KauClientException var11) {
         ErrorList.getInstance().writeError("KR Feltöltés", var11.getMessage(), var11, (Object)null);
         throw var11;
      } catch (Exception var12) {
         ErrorList.getInstance().writeError("KR Feltöltés", var12.getMessage(), var12, (Object)null);
         throw var12;
      }
   }

   public static int disableCopyToSentDir() {
      String var0 = null;

      try {
         try {
            var0 = (String)((Vector)PropertyList.getInstance().get("prop.const.disableCopyToSentDir")).get(0);
         } catch (Exception var2) {
            return -1;
         }

         if (var0 == null) {
            var0 = "";
         }

         if ("igen".equals(var0.toLowerCase())) {
            System.out.println("Az 'elküldött' könyvtárba történő átmozgatás a 'disableCopyToSentDir' paraméterrel tiltva!");
            return 1;
         } else {
            return 0;
         }
      } catch (Exception var3) {
         return -1;
      }
   }

   private void deleteSourceXmlFile(File var1) {
      try {
         String var2 = var1.getName().toLowerCase();
         if (var2.endsWith(".kr")) {
            var2 = var2.substring(0, var2.length() - 3);
         }

         if (var2.endsWith(".xml")) {
            var2 = var2.substring(0, var2.length() - 4);
         }

         String[] var3 = (new File(this.sp.srcPath)).list();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4].toLowerCase();
            if (var5.toLowerCase().endsWith(".xml") && var2.startsWith(var5.substring(0, var5.length() - 4))) {
               (new File(this.sp.srcPath + var5)).delete();
               return;
            }
         }

      } catch (Exception var6) {
      }
   }
}
