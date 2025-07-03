package hu.piller.enykp.alogic.kontroll;

import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IHelperLoad;
import hu.piller.enykp.util.base.Tools;
import me.necrocore.abevjava.NecroFile;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class KTools {
   public Hashtable kvf_kdata = new Hashtable();
   public Hashtable kvf_azonosito = new Hashtable();
   public Hashtable kvf_data = new Hashtable();
   public Hashtable headInfos = new Hashtable();
   static TemplateChecker tc;

   public static void setPm() {
      tc = TemplateChecker.getInstance();
   }

   public String[] getKontrollNames(DatTableModel var1) throws Exception {
      File var2 = new NecroFile(Kontroll.kontrollKVFPath);
      KontrollFilenameFilter var3 = new KontrollFilenameFilter(".kvf");
      String[] var4 = var2.list(var3);
      this.parseKvfs(var4);

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var4[var5] = var4[var5].substring(0, var4[var5].length() - 4).toLowerCase();
      }

      this.parseDtm(var1, var4);
      return var4;
   }

   private void parseKvfs(String[] var1) throws Exception {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         try {
            if (var1[var2].toLowerCase().indexOf("_") > -1 && var1[var2].toLowerCase().indexOf("_hp") == -1) {
               this.kvf_data.put(var1[var2].toLowerCase().replaceAll("_", "/"), new KvfData(Kontroll.kontrollKVFPath + var1[var2]));
               var1[var2] = var1[var2].toLowerCase().replaceAll("_", "/");
            } else {
               this.kvf_data.put(var1[var2].toLowerCase(), new KvfData(Kontroll.kontrollKVFPath + var1[var2]));
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

   }

   private void parseDtm(DatTableModel var1, String[] var2) {
      int var4;
      for(var4 = 0; var4 < var2.length; ++var4) {
         this.kvf_azonosito.put(var2[var4].toLowerCase(), new Vector());
      }

      for(var4 = 0; var4 < var1.getRowCount(); ++var4) {
         if (this.kvf_azonosito.containsKey(var1.getValueAt(var4, 0))) {
            String var5 = !var1.getValueAt(var4, 2).equals("") ? (String)var1.getValueAt(var4, 2) : (String)var1.getValueAt(var4, 3);
            KData var3 = new KData(var5, (String)var1.getValueAt(var4, 0), (String)var1.getValueAt(var4, 7), (String)var1.getValueAt(var4, 8), (String)var1.getValueAt(var4, 9));
            if (!((Vector)this.kvf_azonosito.get(var1.getValueAt(var4, 0))).contains(var3.azonosito)) {
               ((Vector)this.kvf_azonosito.get(var1.getValueAt(var4, 0))).add(var3.azonosito);
            }

            if (!this.kvf_kdata.containsKey(var1.getValueAt(var4, 0) + "|" + var3.azonosito)) {
               this.kvf_kdata.put(var1.getValueAt(var4, 0) + "|" + var3.azonosito, new Vector());
            }

            ((Vector)this.kvf_kdata.get(var1.getValueAt(var4, 0) + "|" + var3.azonosito)).add(var3);
         }
      }

   }

   public static Hashtable getFileInfo(String var0, int var1, String var2) throws Exception {
      Hashtable var3 = new Hashtable();
      var3.put("head_info", "");
      if (var1 == 0) {
         var3.put("get_form_iterator", "");
      }

      IHelperLoad var4 = (IHelperLoad)Kontroll.xmlLoadManager.getHeadData(new NecroFile(var0));
      var4.initialize();
      var4.read();
      var4.getData(var3);
      if (var1 == 0) {
         var3.put("data", parseFormIterator(var3.get("get_form_iterator")));
      }

      var4.release();
      if (var1 != 0) {
      }

      return var1 == 1 ? (Hashtable)var3.get("head_info") : var3;
   }

   public static String getTemplateFilename(String var0) throws Exception {
      String[] var1 = TemplateChecker.getInstance().getTemplateFileNames(var0, (String)null, "apeh").getTemplateFileNames();
      return (String)var1[0];
   }

   public static Hashtable parseFormIterator(Object var0) {
      try {
         Iterator var1 = (Iterator)var0;
         if (var1.hasNext()) {
            Hashtable var2 = new Hashtable();
            var2.put("data", "");
            ((IHelperLoad)var1.next()).getData(var2);
            return (Hashtable)var2.get("data");
         }
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
      }

      return null;
   }

   public static BookModel getBookModel(String var0, String var1) throws Exception {
      String var2 = null;
      var2 = getTemplateFilename(var1);
      File var3 = new NecroFile(var2);
      if (!var3.exists()) {
         throw new Exception("*Nem létezik a sablon fájl");
      } else {
         return new BookModel(var3, new NecroFile(var0));
      }
   }
}
