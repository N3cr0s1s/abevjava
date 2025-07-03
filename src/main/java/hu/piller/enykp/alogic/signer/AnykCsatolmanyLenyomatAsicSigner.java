package hu.piller.enykp.alogic.signer;

import hu.piller.enykp.niszws.obhservice.Document;
import hu.piller.enykp.niszws.obhservice.ObhServicePortType;
import hu.piller.enykp.niszws.obhservice.ObhServiceRequestType;
import hu.piller.enykp.niszws.obhservice.ObhServiceResponseType;
import hu.piller.enykp.niszws.obhservice.ObjectFactory;
import hu.piller.enykp.niszws.obhservice.SignedDocument;
import hu.piller.enykp.niszws.obhservice.SignerTypeEnum;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class AnykCsatolmanyLenyomatAsicSigner implements ISigner {
   private ObhServicePortType obhService2;

   public void setObhService2(ObhServicePortType var1) {
      this.obhService2 = var1;
   }

   public void sign(List<File> var1) throws SignerException {
      this.checkInputFileList(var1);
      List var2 = Collections.EMPTY_LIST;

      try {
         var2 = this.createAnykCsatolmanyLenyomatsForFiles(var1);
         if (var2.size() != var1.size()) {
            String var11 = String.format("Az előállított ANYK_CSATOLMANY_LENYOMAT állományok száma (%1$d) nem egyezik az aláírásra küldött állományok számával (%2$d)!", var2.size(), var1.size());
            System.out.println("KAU_LOG_1 " + var11);
            throw new SignerException(var11);
         }

         ObhServiceRequestType var3;
         try {
            var3 = this.createFileSignRequestForAsicSigner(var2, "ANYK_CSATOLMANY_LENYOMAT", "text/xml");
         } catch (Exception var9) {
            System.out.println("KAU_LOG_2 " + var9);
            throw new SignerException(var9.getMessage());
         }

         if (var3 == null) {
            throw new SignerException("Az aláírás kérés előállítása sikertelen!");
         }

         if (var3.getDocuments() == null) {
            throw new SignerException("Az aláírás kérés előállítása sikertelen (Documents)!");
         }

         if (var3.getDocuments().getDocument() == null) {
            throw new SignerException("Az aláírás kérés előállítása sikertelen (Document)!");
         }

         if (var3.getDocuments().getDocument().size() != var1.size()) {
            String var12 = String.format("Az előállított ANYK_CSATOLMANY_LENYOMAT állományok száma (%1$d) nem egyezik az aláírásra küldött állományok számával (%2$d)!", var3.getDocuments().getDocument().size(), var1.size());
            throw new SignerException(var12);
         }

         ObhServiceResponseType var4 = null;
         var4 = this.obhService2.signDocument(var3);
         if (var4 == null) {
            throw new SignerException("Az aláírás előállítása sikertelen!");
         }

         if (var4.getDocuments() == null) {
            throw new SignerException("Az aláírás előállítása sikertelen (Documents)!");
         }

         if (var4.getDocuments().getDocument() == null) {
            throw new SignerException("Az aláírás előállítása sikertelen (Document)!");
         }

         if (var4.getError() != null) {
            throw new SignerException("Az aláírás nem sikerült! A szolgáltatás hibakódja: " + var4.getError().getErrorCode() + " : " + var4.getError().getErrorMessage());
         }

         if (var4.getDocuments().getDocument().size() != var2.size()) {
            String var5 = String.format("Az előállított ASiC konténerek száma (%1$d) nem egyezik az aláírásra küldött ANYK_CSATOLMANY_LENYOMAT állományok számával (%2$d)!", var4.getDocuments().getDocument().size(), var2.size());
            throw new SignerException(var5);
         }

         this.saveAttachmentSignatures(var4, var1);
      } finally {
         this.deleteFiles(var2);
      }

   }

   public void signAsic(List<File> var1) throws SignerException {
      if (this.obhService2 == null) {
         throw new SignerException("A melléklet lenyomat aláíró szolgáltatás nincsen beállítva!");
      } else {
         this.checkInputFileList(var1);

         ObhServiceRequestType var2;
         try {
            var2 = this.createFileSignRequestForAsicSigner(var1, "ANYK_CSATOLMANY_ASIC", "application/vnd.etsi.asic-e+zip");
         } catch (Exception var6) {
            throw new SignerException(var6.getMessage());
         }

         if (var2 == null) {
            throw new SignerException("Az aláírás kérés előállítása sikertelen!");
         } else if (var2.getDocuments() == null) {
            throw new SignerException("Az aláírás kérés előállítása sikertelen (Documents)!");
         } else if (var2.getDocuments().getDocument() == null) {
            throw new SignerException("Az aláírás kérés előállítása sikertelen (Document)!");
         } else {
            ObhServiceResponseType var3 = null;

            try {
               var3 = this.obhService2.signDocument(var2);
            } catch (Exception var5) {
               throw new SignerException(var5.getMessage());
            }

            if (var3 == null) {
               throw new SignerException("Az aláírás előállítása sikertelen!");
            } else if (var3.getDocuments() == null) {
               throw new SignerException("Az aláírás előállítása sikertelen (Documents)!");
            } else if (var3.getDocuments().getDocument() == null) {
               throw new SignerException("Az aláírás előállítása sikertelen (Document)!");
            } else if (var3.getError() != null) {
               throw new SignerException("Az aláírás nem sikerült! A szolgáltatás hibakódja: " + var3.getError().getErrorCode() + " : " + var3.getError().getErrorMessage());
            } else if (var3.getDocuments().getDocument().size() != var2.getDocuments().getDocument().size()) {
               String var4 = String.format("Az előállított ASiC könténerek száma (%1$d) nem egyezik az aláírásra küldött ANYK_CSATOLMANY_ASIC állományok számával (%2$d)!", var3.getDocuments().getDocument().size(), var2.getDocuments().getDocument().size());
               throw new SignerException(var4);
            } else {
               this.overrideOriginalAsicWithSignedAsic(var3, var1);
            }
         }
      }
   }

   protected void checkInputFileList(List<File> var1) throws SignerException {
      if (var1 == null || var1.isEmpty()) {
         throw new SignerException("Üres lista!");
      }
   }

   protected List<File> createAnykCsatolmanyLenyomatsForFiles(List<File> var1) throws SignerException {
      ArrayList var2 = new ArrayList();

      try {
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            File var4 = (File)var3.next();
            AnykCsatolmanyLenyomat var5 = AnykCsatolmanyLenyomat.create(var4);
            File var6 = var4.getParentFile();
            File var7 = new File(var6, var5.getFileName() + ".anyk");
            this.writeFile(var7, var5.toXml().getBytes(Charset.forName("UTF-8")));
            var2.add(var7);
         }

         return var2;
      } catch (Exception var8) {
         throw new SignerException(var8.getMessage());
      }
   }

   protected ObhServiceRequestType createFileSignRequestForAsicSigner(List<File> var1, String var2, String var3) throws IOException {
      ObhServiceRequestType var4 = (new ObjectFactory()).createObhServiceRequestType();
      var4.setSender("ÁNYK");
      var4.setUid(UUID.randomUUID().toString());
      ObhServiceRequestType.Documents var5 = (new ObjectFactory()).createObhServiceRequestTypeDocuments();
      var4.setDocuments(var5);
      Iterator var6 = var1.iterator();

      while(var6.hasNext()) {
         File var7 = (File)var6.next();
         Document var8 = (new ObjectFactory()).createDocument();
         var8.setDocType(var2);
         var8.setDocBytes(Files.readAllBytes(Paths.get(var7.toURI())));
         var8.setFileInResponse(true);
         var8.setFileName(var7.getName());
         var8.setSignerType(SignerTypeEnum.A_SI_C);
         var8.setMimeType(var3);
         var5.getDocument().add(var8);
      }

      return var4;
   }

   protected void saveAttachmentSignatures(ObhServiceResponseType var1, List<File> var2) throws SignerException {
      Iterator var3 = var1.getDocuments().getDocument().iterator();

      SignedDocument var4;
      boolean var5;
      do {
         if (!var3.hasNext()) {
            return;
         }

         var4 = (SignedDocument)var3.next();
         var5 = false;
         Iterator var6 = var2.iterator();

         while(var6.hasNext()) {
            File var7 = (File)var6.next();
            String var8 = var7.getName();
            String var9 = var4.getOriginalFileName().substring(0, var4.getOriginalFileName().length() - ".anyk".length());
            if (var8.equals(var9)) {
               var5 = true;
               File var10 = var7.getParentFile();
               File var11 = new File(var10, var4.getFileName());

               try {
                  this.writeFile(var11, var4.getDocBytes());
               } catch (IOException var14) {
                  String var13 = String.format("Hiba történt a(z) %1$s fájl %2$s ASiC aláírás állományának mentése során : %3$s", var7.getAbsolutePath(), var4.getFileName(), var14.getMessage());
                  throw new SignerException(var13);
               }
            }
         }
      } while(var5);

      String var15 = String.format("A(z) %1$s ASiC aláírás állomány nem köthető az aláírandó fájlok egyikéhez sem!", var4.getFileName());
      throw new SignerException(var15);
   }

   protected void overrideOriginalAsicWithSignedAsic(ObhServiceResponseType var1, List<File> var2) throws SignerException {
      Iterator var3 = var1.getDocuments().getDocument().iterator();

      SignedDocument var4;
      boolean var5;
      do {
         if (!var3.hasNext()) {
            return;
         }

         var4 = (SignedDocument)var3.next();
         var5 = false;
         Iterator var6 = var2.iterator();

         while(var6.hasNext()) {
            File var7 = (File)var6.next();
            String var8 = var7.getName();
            if (var8.equals(var4.getOriginalFileName())) {
               var5 = true;
               File var9 = var7.getParentFile();
               File var10 = new File(var9, var4.getOriginalFileName());

               try {
                  if (var7.exists()) {
                     System.out.println("Torlesre kerul az alairasra kuldott eredeti: " + var7.getAbsolutePath());
                     var7.delete();
                  }

                  this.writeFile(var10, var4.getDocBytes());
               } catch (IOException var13) {
                  String var12 = String.format("Hiba történt a(z) %1$s ASiC többes alírás mentése során : %3$s", var4.getFileName(), var13.getMessage());
                  throw new SignerException(var12);
               }
            }
         }
      } while(var5);

      String var14 = String.format("A(z) %1$s ASiC aláírás állomány nem köthető az aláírandó fájlok egyikéhez sem (keresett aláírandó fájl: %2$s)!", var4.getFileName(), var4.getOriginalFileName());
      throw new SignerException(var14);
   }

   protected void deleteFiles(List<File> var1) throws SignerException {
      Iterator var2 = var1.iterator();

      File var3;
      do {
         if (!var2.hasNext()) {
            return;
         }

         var3 = (File)var2.next();
      } while(var3.delete());

      throw new SignerException("A(z) " + var3.getAbsolutePath() + " fájl nem törölhető!");
   }

   protected void writeFile(File var1, byte[] var2) throws IOException {
      System.out.println("A torolt eredeti helyere irodik: " + var1.getAbsolutePath() + " (" + var2.length + " byte)");
      FileOutputStream var3 = null;

      try {
         var3 = new FileOutputStream(var1);
         var3.write(var2);
         var3.flush();
      } finally {
         if (var3 != null) {
            var3.close();
         }

      }

   }
}
