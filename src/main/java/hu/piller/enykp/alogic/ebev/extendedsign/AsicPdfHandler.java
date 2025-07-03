package hu.piller.enykp.alogic.ebev.extendedsign;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class AsicPdfHandler {
   String pdfFilename = "meghatalmazo.pdf";
   String ext = "asic";
   private Random r = new Random(System.currentTimeMillis());

   public Result getSigners(String var1) {
      Result var2 = new Result();
      if (!(new File(var1)).exists()) {
         var2.setOk(false);
         var2.errorList.add("Nincs ilyen nevű aláírás fájl");
         return var2;
      } else {
         ArrayList var3 = new ArrayList();
         String var4 = PropertyList.getInstance().get("prop.usr.root") + "";
         if (!var4.endsWith(File.separator)) {
            var4 = var4 + File.separator;
         }

         var4 = var4 + PropertyList.getInstance().get("prop.usr.tmp");
         if (!var4.endsWith(File.separator)) {
            var4 = var4 + File.separator;
         }

         String var5 = var4 + (new File(var1)).getName() + "_alairok.pdf";
         var4 = var4 + this.r.nextInt();
         if (!(new File(var4)).mkdir()) {
            var2.setOk(false);
            var2.errorList.add("Nem sikerült megállapítani az aláírókat!");
         }

         var4 = var4 + File.separator;

         try {
            String[] var6 = this.getAllFile(var1, var5, var4, var3);
            var2.errorList.add(var6[0]);
            var2.errorList.add(var6[1]);
         } catch (Exception var7) {
            var2.setOk(false);
            var2.errorList.add("Hiba az aláírók meghatározásakor!");
         }

         if (!this.clean(var4)) {
            var2.errorList.add("Nem sikerült az ideiglenes állományok törlése a " + PropertyList.getInstance().get("prop.usr.tmp") + " mappából.");
         }

         return var2;
      }
   }

   public String[] getAllFile(String var1, String var2, String var3, ArrayList<String> var4) throws IOException, DocumentException {
      ZipFile var5 = new ZipFile(var1);
      Enumeration var6 = var5.entries();

      while(var6.hasMoreElements()) {
         ZipEntry var7 = (ZipEntry)var6.nextElement();
         String var8;
         if (this.pdfFilename.equals(var7.getName())) {
            var8 = var3 + File.separator + (new File(var1)).getName() + this.r.nextInt() + "_" + var7.getName();
            this.unzip(var5.getInputStream(var7), new BufferedOutputStream(new FileOutputStream(var8)));
            var4.add(var8);
         }

         if (var7.getName().toLowerCase().endsWith(this.ext)) {
            var8 = var3 + File.separator + var7.getName();
            File var9 = new File(var8);
            if (var9.exists()) {
               var8 = var8 + this.r.nextInt();
            }

            this.unzip(var5.getInputStream(var7), new BufferedOutputStream(new FileOutputStream(var8)));
            this.getAllFile(var8, var2, var3, var4);
         }
      }

      var5.close();
      this.mergePdf(var4, var2);
      return new String[]{var2, var4.size() + ""};
   }

   private void unzip(InputStream var1, OutputStream var2) throws IOException {
      byte[] var3 = new byte[1024];

      int var4;
      while((var4 = var1.read(var3)) >= 0) {
         var2.write(var3, 0, var4);
      }

      var1.close();
      var2.close();
   }

   private void mergePdf(ArrayList<String> var1, String var2) throws IOException, DocumentException {
      FileOutputStream var3 = new FileOutputStream(var2);
      Document var4 = new Document();
      PdfWriter var5 = PdfWriter.getInstance(var4, var3);
      var4.open();
      PdfContentByte var6 = var5.getDirectContent();
      Iterator var7 = var1.iterator();

      while(var7.hasNext()) {
         String var8 = (String)var7.next();
         PdfReader var9 = new PdfReader(var8);
         var4.newPage();
         PdfImportedPage var10 = var5.getImportedPage(var9, 1);
         var6.addTemplate(var10, 0.0F, 0.0F);
         var9.close();
      }

      var4.close();
      var3.close();
   }

   private boolean clean(String var1) {
      boolean var2 = true;
      File var3 = new File(var1);
      if (!var3.isDirectory()) {
         return var2;
      } else {
         File[] var4 = var3.listFiles();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            var2 = var2 && var4[var5].delete();
         }

         return var2 && var3.delete();
      }
   }
}
