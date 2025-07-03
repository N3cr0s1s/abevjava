package hu.piller.enykp.alogic.uploader;

import hu.piller.enykp.alogic.ebev.SendParams;
import hu.piller.enykp.niszws.documentsuploadservice.Document;
import hu.piller.enykp.niszws.documentsuploadservice.DocumentResult;
import hu.piller.enykp.niszws.documentsuploadservice.DocumentsUploadRequest;
import hu.piller.enykp.niszws.documentsuploadservice.DocumentsUploadResponse;
import hu.piller.enykp.niszws.documentsuploadservice.DocumentsUploadService;
import hu.piller.enykp.niszws.documentsuploadservice.OfficeAuthentication;
import hu.piller.enykp.util.base.PropertyList;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;

public class IUploaderImpl implements IUploader {
   private DocumentsUploadService documentsUploadService;

   public void setDocumentsUploadService(DocumentsUploadService var1) {
      this.documentsUploadService = var1;
   }

   public Return upload(String[] var1, String var2, char[] var3, String var4, char[] var5, boolean var6, boolean var7, String var8, boolean var9) throws UploaderException {
      String var10 = (new SendParams(PropertyList.getInstance())).destPath;
      DocumentsUploadRequest var11 = new DocumentsUploadRequest();
      String[] var12 = var1;
      int var13 = var1.length;

      for(int var14 = 0; var14 < var13; ++var14) {
         String var15 = var12[var14];
         Document var16 = this.createDocument(var10, var15, var6, var7, var8, var9);
         var11.getDocuments().add(var16);
      }

      if (var4 != null) {
         OfficeAuthentication var18 = new OfficeAuthentication();
         var18.setShortName(var4);
         var11.setOfficeAuthentication(var18);
      }

      try {
         DocumentsUploadResponse var19 = this.documentsUploadService.documentsUpload(var11);
         return this.processResponse(var19);
      } catch (UploaderException var17) {
         throw var17;
      }
   }

   private String checkErrorMessage(String var1) {
      return var1.indexOf("KAÜ azonosítás sikertelen volt") != -1 ? "KAÜ azonosítás megszakítva vagy sikertelen!" : var1;
   }

   private Return processResponse(DocumentsUploadResponse var1) throws UploaderException {
      Return var2 = new Return();
      if (var1.getCauseOfFail() != null) {
         if (var1.getCauseOfFail().contains("A felhasználó nincs hozzárendelve a megadott") || var1.getCauseOfFail().contains("A felhasználó részére a dokumentum művelet nem engedélyezett a megadott")) {
            throw new UploaderException(var1.getCauseOfFail());
         }

         var2.setCauseOfFail("Szolgáltatás végrehajtási hiba : " + var1.getCauseOfFail());
      } else {
         ArrayList var3 = new ArrayList();

         FeltoltesValasz var6;
         for(Iterator var4 = var1.getResults().iterator(); var4.hasNext(); var3.add(var6)) {
            DocumentResult var5 = (DocumentResult)var4.next();
            var6 = new FeltoltesValasz();
            var6.setFileName(var5.getName());
            var6.setStored(var5.isStored());
            if (var6.isStored()) {
               var6.setFilingNumber(var5.getFilingNumber());
            } else {
               var6.setErrorMsg(var5.getCauseOfFail());
            }
         }

         var2.setResults((FeltoltesValasz[])var3.toArray(new FeltoltesValasz[var3.size()]));
      }

      return var2;
   }

   private Document createDocument(String var1, String var2, boolean var3, boolean var4, String var5, boolean var6) throws UploaderException {
      Document var7 = new Document();

      try {
         var7.setDataHandler(new DataHandler(new FileDataSource(var1 + File.separator + var2)));
         var7.setName(var2);
      } catch (Exception var10) {
         String var9 = String.format("A(z) %1$s kr fájl beolvasása sikertelen!", var2);
         throw new UploaderException(var9);
      }

      var7.setCopyIntoThePermanentStorage(var3);
      var7.setEncryptReply(var4);
      var7.setReturnReceiptMode(var5);
      var7.setSystemMessage(var6);
      return var7;
   }
}
