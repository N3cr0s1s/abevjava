package hu.piller.enykp.alogic.filepanels.batch;

import hu.piller.enykp.alogic.fileloader.xml.XmlLoader;
import hu.piller.enykp.gui.model.BookModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

public class FileLoader4BatchCheck implements Loader4BatchCheck {
   private Vector fileNames;
   private int currentIndex = -1;

   public void setFileToList(String var1) {
      this.fileNames = new Vector();
      this.fileNames.add(var1);
   }

   public BookModel superLoad(BookModel var1, String var2) {
      XmlLoader var3 = new XmlLoader();
      Hashtable var4 = this.loadHeadData(new File(var2), var3);

      Hashtable var5;
      try {
         var5 = (Hashtable)var4.get("docinfo");
         if (var5.size() == 0) {
            throw new Exception();
         }
      } catch (Exception var10) {
         return null;
      }

      String var6 = var5.containsKey("id") ? (String)var5.get("id") : "";
      String var7 = var5.containsKey("templatever") ? (String)var5.get("templatever") : "";
      String var8 = var5.containsKey("org") ? (String)var5.get("org") : "";
      BookModel var9 = var3.load(var2, var6, var7, var8, var1);
      if (var3.headcheckfatalerror) {
         var9.hasError = true;
         var9.errormsg = var3.errormsg;
      }

      if (var3.fatalerror) {
         var9.hasError = true;
         var9.errormsg = var3.errormsg;
      }

      return var9;
   }

   public String nextId() {
      ++this.currentIndex;

      try {
         return (String)this.fileNames.elementAt(this.currentIndex);
      } catch (Exception var2) {
         return null;
      }
   }

   public int createList(String var1) {
      File var2 = new File(var1);
      if (!var2.exists()) {
         return 1;
      } else {
         if (var2.isDirectory()) {
            this.fileNames = new Vector(Arrays.asList(var2.list()));
         } else {
            this.fileNames = new Vector();

            try {
               BufferedReader var3 = new BufferedReader(new FileReader(var2));

               String var4;
               while((var4 = var3.readLine()) != null) {
                  if (var4.toLowerCase().endsWith(".xml")) {
                     this.fileNames.add(var4);
                  }
               }
            } catch (Exception var5) {
               return 2;
            }
         }

         return 0;
      }
   }

   public Hashtable loadHeadData(File var1, XmlLoader var2) {
      Hashtable var3 = var2.getHeadData(var1);
      var3.put("templatefile", var1);
      return var3;
   }
}
