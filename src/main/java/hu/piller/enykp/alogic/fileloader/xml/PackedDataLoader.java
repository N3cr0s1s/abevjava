package hu.piller.enykp.alogic.fileloader.xml;

import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.ILoadManager;
import hu.piller.enykp.util.base.Tools;
import me.necrocore.abevjava.NecroFile;

import java.io.File;
import java.util.HashSet;
import java.util.Hashtable;

public class PackedDataLoader implements ILoadManager {
   private String loaderid = "packed_data_loader_v1";
   private String suffix = ".xcz";
   private String description = "XCZ állományok";
   HashSet set = new HashSet();

   public PackedDataLoader() {
      this.set.add("xkr");
      this.set.add("xml");
   }

   public String getId() {
      return this.loaderid;
   }

   public String getDescription() {
      return this.description;
   }

   public Hashtable getHeadData(File var1) {
      String var2 = null;

      try {
         var2 = Tools.unzipFile(var1.getAbsolutePath(), Tools.getTempDir(), this.set, true, false);
         if (var2 == null) {
            return null;
         } else {
            Hashtable var3;
            if (var2.toLowerCase().endsWith(".xkr")) {
               XkrLoader var6 = new XkrLoader();
               var3 = var6.getHeadData(new NecroFile(var2));
               (new NecroFile(var2)).delete();
               return var3;
            } else {
               XmlLoader var4 = new XmlLoader();
               var3 = var4.getHeadData(new NecroFile(var2));
               (new NecroFile(var2)).delete();
               return var3;
            }
         }
      } catch (Exception var5) {
         var5.printStackTrace();
         return null;
      }
   }

   public BookModel load(String var1, String var2, String var3, String var4) {
      System.out.println("load 1 called");
      return null;
   }

   public BookModel load(String var1, String var2, String var3, String var4, BookModel var5) {
      System.out.println("load 2 called");

      try {
         String var6 = Tools.unzipFile(var1, Tools.getTempDir(), this.set, true, false);
         if (var6 == null) {
            return null;
         } else {
            PackedXmlLoader var7 = new PackedXmlLoader();
            BookModel var8 = var7.load(var6, var2, var3, var4, var5);
            return var8;
         }
      } catch (Exception var9) {
         var9.printStackTrace();
         return null;
      }
   }

   public String getFileNameSuffix() {
      return this.suffix;
   }

   public String createFileName(String var1) {
      return "asdfghj.zip";
   }
}
