package hu.piller.enykp.alogic.masterdata.repository.xml;

import hu.piller.enykp.alogic.masterdata.core.MasterDataDefinition;
import java.util.Hashtable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MasterDataDefinitionHandler extends DefaultHandler {
   private static final String MD_DEF = "MasterDataDef";
   private static final String MD_KEY = "key";
   private static final String MD_TYPE = "type";
   private static final String MD_ORG = "org";
   private String currKey;
   private String currType;
   private String currOrg;
   private Hashtable<String, MasterDataDefinition> masterData = new Hashtable();
   private StringBuffer nodeValue;
   private String[] orgNames;

   public void setOrgNames(String[] var1) {
      this.orgNames = var1;
   }

   public Hashtable<String, MasterDataDefinition> getMasterDataDefinitions() {
      return this.masterData;
   }

   public void startDocument() throws SAXException {
      this.masterData.clear();
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      this.nodeValue.append(new String(var1, var2, var3));
   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      if ("key".equals(var2)) {
         this.currKey = this.nodeValue.toString();
      } else if ("type".equals(var2)) {
         this.currType = this.nodeValue.toString();
      } else if ("org".equals(var2)) {
         this.currOrg = this.nodeValue.toString();
      } else if ("MasterDataDef".equals(var2) && !this.currType.equals("meta")) {
         boolean var4 = false;
         if (this.orgNames.length == 1 && this.orgNames[0].equals(this.currOrg)) {
            var4 = true;
         } else {
            String[] var5 = this.orgNames;
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String var8 = var5[var7];
               if (var8.equals(this.currOrg)) {
                  var4 = true;
                  break;
               }
            }
         }

         if (var4) {
            this.masterData.put(this.currKey, new MasterDataDefinition(this.currKey, this.currOrg, this.currType));
         }
      }

   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if ("MasterDataDef".equals(var2)) {
         this.currKey = "";
      }

      this.nodeValue = new StringBuffer();
   }
}
