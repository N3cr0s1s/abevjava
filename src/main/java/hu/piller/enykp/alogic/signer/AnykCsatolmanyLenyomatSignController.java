package hu.piller.enykp.alogic.signer;

import hu.piller.enykp.kauclient.KauAuthMethod;
import hu.piller.enykp.kauclient.KauAuthMethods;
import hu.piller.enykp.niszws.util.DapSessionHandler;
import hu.piller.enykp.util.base.PropertyList;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AnykCsatolmanyLenyomatSignController {
   private static final int FILE_COUNT_LIMIT = 10;
   private List<List<File>> controlledFilesToSign = new ArrayList();

   public AnykCsatolmanyLenyomatSignController(List<File> var1) {
      if (var1.size() != 0) {
         int var2 = 0;
         ArrayList var3 = new ArrayList();

         for(int var4 = 0; var4 < var1.size(); ++var4) {
            File var5 = (File)var1.get(var4);
            if (var2 < 10) {
               var3.add(var5);
               ++var2;
            } else {
               this.controlledFilesToSign.add(var3);
               var3 = new ArrayList();
               var3.add(var5);
               var2 = 0;
            }
         }

         if (var3.size() > 0) {
            this.controlledFilesToSign.add(var3);
         }

      }
   }

   public void controlledSign() throws SignerException {
      try {
         DapSessionHandler.getInstance().setBatchDapUploadInProgress(KauAuthMethods.getSelected() == KauAuthMethod.KAU_DAP);

         for(int var1 = 0; var1 < this.controlledFilesToSign.size(); ++var1) {
            SignerFactory.createKrAuthenticatedAnykCsatolmanyLenyomatAsicSigner().sign((List)this.controlledFilesToSign.get(var1));
         }

      } catch (Exception var2) {
         var2.printStackTrace();
         this.handleError();
         throw new SignerException(var2.getMessage() != null ? var2.getMessage() : "Hiba az avdh aláírás készítésekor : " + var2.toString());
      }
   }

   public void controlledSignAsic() throws SignerException {
      try {
         DapSessionHandler.getInstance().setBatchDapUploadInProgress(KauAuthMethods.getSelected() == KauAuthMethod.KAU_DAP);

         for(int var1 = 0; var1 < this.controlledFilesToSign.size(); ++var1) {
            SignerFactory.createKrAuthenticatedAnykCsatolmanyLenyomatAsicSigner().signAsic((List)this.controlledFilesToSign.get(var1));
         }
      } catch (Exception var5) {
         var5.printStackTrace();
         throw new SignerException(var5.getMessage() != null ? var5.getMessage() : "Hiba a többszörös avdh aláírás készítésekor : " + var5.toString());
      } finally {
         DapSessionHandler.getInstance().reset();
      }

   }

   private void handleError() {
      for(int var1 = 0; var1 < this.controlledFilesToSign.size(); ++var1) {
         List var2 = (List)this.controlledFilesToSign.get(var1);

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            File var4 = new NecroFile(((File)var2.get(var3)).getAbsolutePath() + ".anyk.ASiC");
            var4.delete();
         }
      }

   }

   public void controlledDummyAvdhSign() throws SignerException {
      try {
         for(int var1 = 0; var1 < this.controlledFilesToSign.size(); ++var1) {
            this.dummyAvdhSign((ArrayList)this.controlledFilesToSign.get(var1), (String)((Vector)PropertyList.getInstance().get("prop.const.avdhDummyDir")).get(0));
         }

      } catch (Exception var2) {
         var2.printStackTrace();
         this.handleError();
         throw new SignerException(var2.getMessage() != null ? var2.getMessage() : "Hiba az avdh aláírás készítésekor : " + var2.toString());
      }
   }

   private void dummyAvdhSign(ArrayList<File> var1, String var2) throws IOException {
      for(int var3 = 0; var3 < var1.size(); ++var3) {
         File var4 = new NecroFile(((File)var1.get(var3)).getAbsolutePath() + ".anyk.ASiC");
         FileOutputStream var5 = new NecroFileOutputStream(var4);
         FileInputStream var6 = new FileInputStream(var2 + "\\1signer.asic");
         byte[] var7 = new byte[2048];

         int var8;
         while((var8 = var6.read(var7)) > -1) {
            var5.write(var7, 0, var8);
         }

         var5.close();
         var6.close();
      }

   }
}
