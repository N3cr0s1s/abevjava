package hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.downloadablecomponents;

import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import hu.piller.enykp.util.base.Version;
import java.util.Stack;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlHandler extends DefaultHandler {
   private static final String KATEGORIA = "kategoria";
   private static final String SZERVEZET = "szervezet";
   private static final String ROVIDNEV = "rovidnev";
   private static final String VERZIO = "verzio";
   private static final String ELNEVEZES = "elnevezes";
   private static final String CSOPORT = "csoport";
   private static final String URL = "url";
   private static final String FILES = "files";
   private static final String FILE = "file";
   private static final String KERETPROGRAM = "keretprogram";
   private static final String NYOMTATVANY = "nyomtatvany";
   private static final String UTMUTATO = "utmutato";
   private Vector components = new Vector();
   private Vector files = new Vector();
   private boolean verbose;
   private VersionData versionData;
   private StringBuffer content;
   private Stack<String> stack = new Stack();

   public void verboseOn() {
      this.verbose = true;
   }

   public void verboseOff() {
      this.verbose = false;
   }

   public Vector getComponents() {
      return this.components;
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      this.stack.push(var2);
      this.content = new StringBuffer();
      if (this.isVersionDataSegmentSeparator((String)this.stack.peek())) {
         this.versionData = new VersionData();
      } else if (this.isFilesetSeparator((String)this.stack.peek())) {
         this.files.clear();
      }

   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      if (this.isVersionDataSegmentSeparator((String)this.stack.peek())) {
         this.components.add(this.versionData);
      } else if (this.isFilesetSeparator((String)this.stack.peek())) {
         int var4 = this.files.size();
         String[] var5 = new String[0];
         if (var4 > 0) {
            var5 = (String[])((String[])this.files.toArray(new String[var4]));
         }

         this.versionData.setFiles(var5);
      } else if ("kategoria".equals(this.stack.peek())) {
         this.versionData.setCategory(this.content.toString().trim());
      } else if ("szervezet".equals(this.stack.peek())) {
         this.versionData.setOrganization(this.apehToNav(this.content.toString().trim()));
      } else if ("rovidnev".equals(this.stack.peek())) {
         this.versionData.setName(this.content.toString().trim());
      } else if ("verzio".equals(this.stack.peek())) {
         this.versionData.setVersion(new Version(this.content.toString().trim()));
      } else if ("elnevezes".equals(this.stack.peek())) {
         this.versionData.setDescription(this.content.toString().trim());
      } else if ("url".equals(this.stack.peek())) {
         this.versionData.setLocation(this.content.toString().trim());
      } else if ("csoport".equals(this.stack.peek())) {
         this.versionData.setGroup(this.content.toString().trim());
      } else if ("file".equals(this.stack.peek())) {
         this.files.add(this.content.toString().trim());
      }

      this.stack.pop();
   }

   private String apehToNav(String var1) {
      String var2 = var1;
      if ("APEH".equals(var1)) {
         var2 = "NAV";
      }

      return var2;
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      this.content.append(new String(var1, var2, var3));
   }

   private boolean isVersionDataSegmentSeparator(String var1) {
      return "keretprogram".equals(var1) || "nyomtatvany".equals(var1) || "utmutato".equals(var1);
   }

   private boolean isFilesetSeparator(String var1) {
      return "files".equals(var1);
   }

   public void warning(SAXParseException var1) throws SAXException {
      this.printInfo(var1);
   }

   public void error(SAXParseException var1) throws SAXException {
      this.printInfo(var1);
   }

   public void fatalError(SAXParseException var1) throws SAXException {
      this.printInfo(var1);
   }

   private void printInfo(SAXParseException var1) throws SAXException {
      if (this.verbose) {
         if (var1.getPublicId() != null) {
            System.out.println("    pid: " + var1.getPublicId());
         }

         if (var1.getSystemId() != null) {
            System.out.println("    sid: " + var1.getSystemId());
         }

         System.out.println("    sor: " + var1.getLineNumber());
         System.out.println("    oszlop: " + var1.getColumnNumber());
         System.out.println("    üzenet: " + var1.getMessage());
         System.out.println("    hely: " + System.getProperty("user.dir"));
      }

      throw var1;
   }
}
