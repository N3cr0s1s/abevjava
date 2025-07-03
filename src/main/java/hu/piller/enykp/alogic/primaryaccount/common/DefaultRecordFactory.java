package hu.piller.enykp.alogic.primaryaccount.common;

import hu.piller.enykp.alogic.primaryaccount.common.file.SeekableOutputStream;
import hu.piller.enykp.alogic.primaryaccount.common.xml.XMLParser;
import hu.piller.enykp.util.base.EventLog;
import hu.piller.enykp.util.base.Tools;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;

import me.necrocore.abevjava.NecroFile;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class DefaultRecordFactory extends XMLParser implements IRecordFactory {
   private static final String CHAR_SET = "ISO-8859-2";
   private static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"ISO-8859-2\"?>";
   private static final String XML_RECORD_HEAD = "RECORDS";
   private static final String XML_RECORD = "RECORD";
   private static final String FILE_COPY_SUFF = ".cpy";
   protected final Vector records = new Vector(128, 4096);
   protected IRecord record;
   protected File path;
   protected DefaultEnvelope envelope;
   protected boolean append;
   protected Long max_id = new Long(0L);
   protected String U_XML_RECORD_HEAD = "RECORDS";
   protected String U_XML_RECORD = "RECORD";

   public DefaultRecordFactory() throws SAXException {
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if (var2.equalsIgnoreCase(this.U_XML_RECORD)) {
         DefaultRecord var6 = this.getNewRecord(this.path, this.envelope);
         var6.getData().putAll(checkAttributes(this.getAttributes(var4)));

         try {
            Long var5 = Long.valueOf(var6.getData().get("id").toString());
            if (this.append) {
               this.max_id = new Long(this.max_id + 1L);
               var6.getData().put("id", this.max_id.toString());
            } else if (var5 > this.max_id) {
               this.max_id = var5;
            }
         } catch (NumberFormatException var8) {
            Tools.eLog(var8, 0);
         }

         this.records.add(var6);
      }

   }

   protected DefaultRecord getNewRecord(File var1, DefaultEnvelope var2) {
      return new DefaultRecord(this, var1, var2);
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
   }

   public Hashtable getAttributes(Attributes var1) {
      int var2 = var1.getLength();
      Hashtable var3 = new Hashtable(var2);

      for(int var4 = 0; var4 < var2; ++var4) {
         var3.put(this.getString(var1.getLocalName(var4)), this.getString(var1.getValue(var4)));
      }

      return var3;
   }

   private String getString(Object var1) {
      return var1 == null ? "" : var1.toString();
   }

   public String getNewId() {
      this.max_id = new Long(this.max_id + 1L);
      return this.max_id.toString();
   }

   public Vector getRecords() {
      return this.records;
   }

   public Vector loadRecords(File var1, DefaultEnvelope var2) throws Exception {
      if (var1 != null) {
         File var3 = this.getFileCopy(var1);
         if (!var1.exists() && var3.exists()) {
            var3.renameTo(var1);
         }

         if (var1.exists()) {
            this.path = var1;
            this.record = null;
            this.envelope = var2;
            this.append = false;
            this.records.clear();
            this.max_id = new Long(0L);
            super.parse(var1.getAbsolutePath());
         }
      }

      return this.records;
   }

   public Vector loadRecords(InputStream var1, DefaultEnvelope var2) throws Exception {
      this.path = new NecroFile("");
      this.record = null;
      this.envelope = var2;
      this.append = false;
      this.records.clear();
      this.max_id = new Long(0L);
      super.parse(var1);
      return this.records;
   }

   public Vector appendRecords(File var1, DefaultEnvelope var2) throws Exception {
      if (var1 != null) {
         File var3 = this.getFileCopy(var1);
         if (!var1.exists() && var3.exists()) {
            var3.renameTo(var1);
         }

         if (var1.exists()) {
            this.path = var1;
            this.record = null;
            this.envelope = var2;
            this.append = true;
            super.parse(var1.getAbsolutePath());
         }
      }

      return this.records;
   }

   public void saveRecords(File var1) {
      File var3 = null;
      if (var1 != null) {
         try {
            if (var1.exists()) {
               var3 = this.getFileCopy(var1);
               if (var3.exists()) {
                  var3.delete();
               }

               (new NecroFile(var1.getPath())).renameTo(var3);
            } else {
               (new NecroFile(var1.getParent())).mkdirs();
            }

            SeekableOutputStream var2 = this.openFile(var1);
            var2.write("<?xml version=\"1.0\" encoding=\"ISO-8859-2\"?>");
            var2.newLine();
            this.writeHeadBegin(var2);
            int var4 = 0;

            for(int var9 = this.records.size(); var4 < var9; ++var4) {
               this.writeRecord(var2, (IRecord)this.records.get(var4));
            }

            this.writeHeadEnd(var2);
            this.closeFile(var2);
            if (var3 != null && var3.exists()) {
               var3.delete();
            }
         } catch (Exception var8) {
            System.out.println(">log: saveRecords() " + var8);
            String var5 = "" + var8 + " (" + var1 + ")";

            try {
               writeLog(var5);
            } catch (Exception var7) {
               System.out.println(var5);
            }
         }
      }

   }

   private File getFileCopy(File var1) {
      return var1 != null ? new NecroFile(var1.getPath() + ".cpy") : null;
   }

   public void deleteAll(File var1) {
      if (var1.exists()) {
         var1.delete();
      }

      this.createFile(var1);
   }

   public void reload(File var1, IRecord var2) throws Exception {
      this.record = var2;
      super.parse(var1.getAbsolutePath());
   }

   public void save(File var1, IRecord var2) {
      this.record = var2;
      if (!this.records.contains(var2)) {
         this.records.add(var2);
      }

      this.saveRecords(var1);
   }

   public void delete(File var1, IRecord var2) {
      this.records.remove(var2);
      if (var1.exists()) {
         var1.delete();
      }

      this.saveRecords(var1);
   }

   private boolean createFile(File var1) {
      try {
         SeekableOutputStream var2 = this.openFile(var1);
         var2.write("<?xml version=\"1.0\" encoding=\"ISO-8859-2\"?>");
         var2.newLine();
         this.writeHeadBegin(var2);
         this.writeHeadEnd(var2);
         return this.closeFile(var2);
      } catch (Exception var4) {
         return false;
      }
   }

   private SeekableOutputStream openFile(File var1) throws Exception {
      return new SeekableOutputStream(var1, "ISO-8859-2");
   }

   private boolean closeFile(SeekableOutputStream var1) {
      if (var1 != null) {
         try {
            var1.flush();
            var1.close();
         } catch (IOException var5) {
            try {
               var1.close();
            } catch (IOException var4) {
               return false;
            }
         }
      }

      return true;
   }

   private void writeRecord(SeekableOutputStream var1, IRecord var2) throws IOException {
      String var3 = this.getXMLRecord(var2);
      if (var3 != null) {
         var1.write(var3);
         var1.newLine();
      }

   }

   private void writeHeadBegin(SeekableOutputStream var1) throws IOException {
      String var2 = this.getXMLHeadBegin();
      if (var2 != null) {
         var1.write(var2);
         var1.newLine();
      }

   }

   private void writeHeadEnd(SeekableOutputStream var1) throws IOException {
      String var2 = this.getXMLHeadEnd();
      if (var2 != null) {
         var1.write(var2);
         var1.newLine();
      }

   }

   protected String getXMLRecord(IRecord var1) {
      String var2 = "";
      if (var1 != null) {
         var2 = " " + this.gatherAttributes(var1.getData());
      }

      return "    <" + this.U_XML_RECORD + var2 + "/>";
   }

   protected String gatherAttributes(Hashtable var1) {
      String var2 = "";
      if (var1 != null) {
         for(Iterator var3 = var1.entrySet().iterator(); var3.hasNext(); var2 = var2 + "\"") {
            Entry var4 = (Entry)var3.next();
            if (var2.length() > 0) {
               var2 = var2 + " ";
            }

            var2 = var2 + quoteCharacters(var4.getKey().toString());
            var2 = var2 + "=\"";
            var2 = var2 + quoteCharacters(var4.getValue().toString());
         }
      }

      return var2;
   }

   protected String getXMLHeadBegin() {
      return "<" + this.U_XML_RECORD_HEAD + ">";
   }

   protected String getXMLHeadEnd() {
      return "</" + this.U_XML_RECORD_HEAD + ">";
   }

   protected static String quoteCharacters(String var0) {
      StringBuffer var1 = null;
      int var2 = 0;
      int var3 = var0.length();

      for(int var4 = 0; var2 < var3; ++var2) {
         char var5 = var0.charAt(var2);
         String var6 = null;
         if (var5 == '&') {
            var6 = "&amp;";
         } else if (var5 == '<') {
            var6 = "&lt;";
         } else if (var5 == '\r') {
            var6 = "&#13;";
         } else if (var5 == '>') {
            var6 = "&gt;";
         } else if (var5 == '"') {
            var6 = "&quot;";
         } else if (var5 == '\'') {
            var6 = "&apos;";
         }

         if (var6 != null) {
            if (var1 == null) {
               var1 = new StringBuffer(var0);
            }

            var1.replace(var2 + var4, var2 + var4 + 1, var6);
            var4 += var6.length() - 1;
         }
      }

      if (var1 == null) {
         return var0;
      } else {
         return var1.toString();
      }
   }

   protected static void writeLog(Object var0) {
      try {
         EventLog.getInstance().logEvent(var0);
      } catch (IOException var2) {
         Tools.eLog(var2, 0);
      }

   }

   public static Hashtable checkAttributes(Hashtable var0) {
      if (var0 != null) {
         Object var1;
         if ((var1 = var0.get("s_zip")) != null) {
            var0.put("s_zip", filterForValue((String)var1, '_'));
         }

         if ((var1 = var0.get("m_zip")) != null) {
            var0.put("m_zip", filterForValue((String)var1, '_'));
         }
      }

      return var0;
   }

   private static String filterForValue(String var0, char var1) {
      String var2;
      if (var0 != null) {
         int var5 = var0.length();

         int var3;
         for(var3 = 0; var3 < var5 && var0.charAt(var3) == var1; ++var3) {
         }

         int var4;
         for(var4 = var5 - 1; var4 > -1 && var0.charAt(var4) == var1; --var4) {
         }

         ++var4;
         if (var3 < var4) {
            var2 = var0.substring(var3, var4);
         } else {
            var2 = "";
         }
      } else {
         var2 = var0;
      }

      return var2;
   }
}
