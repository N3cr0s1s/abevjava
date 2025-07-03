package hu.piller.enykp.alogic.primaryaccount.common.envelope;

import hu.piller.enykp.alogic.primaryaccount.common.DefaultEnvelope;
import hu.piller.enykp.alogic.primaryaccount.common.DefaultRecord;
import hu.piller.enykp.alogic.primaryaccount.common.DefaultRecordFactory;
import hu.piller.enykp.alogic.primaryaccount.common.IRecord;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class AddressRecordFactory extends DefaultRecordFactory {
   private static final String ZIP_TAG = "ZIP";
   private static final String SHIRE_TAG = "SHIRE";
   private static final String APEHADDRESSOPT_TAG = "ADDRESSOPT";
   private AddressRecord current_record;

   public AddressRecordFactory() throws SAXException {
      super.U_XML_RECORD_HEAD = "ADDRESSES";
      super.U_XML_RECORD = "ADDRESS";
   }

   protected DefaultRecord getNewRecord(File var1, DefaultEnvelope var2) {
      this.current_record = new AddressRecord(this, var1, (APEHEnvelope)var2);
      return this.current_record;
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if (this.U_XML_RECORD.equalsIgnoreCase(var2)) {
         super.startElement(var1, var2, var3, var4);
      } else if ("ZIP".equalsIgnoreCase(var2)) {
         if (this.current_record != null) {
            this.current_record.getZips().add(this.getAttributes(var4));
         }
      } else if ("SHIRE".equalsIgnoreCase(var2)) {
         if (this.current_record != null) {
            this.current_record.setShire(this.getAttributes(var4));
         }
      } else if ("ADDRESSOPT".equalsIgnoreCase(var2) && this.current_record != null) {
         this.current_record.addApehAddress(this.getAttributes(var4));
      }

   }

   protected String getXMLRecord(IRecord var1) {
      String var2 = "";
      if (var1 != null) {
         var2 = "    <" + this.U_XML_RECORD + " " + this.gatherAttributes(var1.getData());
         if (var1 instanceof AddressRecord) {
            Vector var3 = ((AddressRecord)var1).getApehAddress();
            int var5;
            int var6;
            if (var3 != null) {
               var2 = var2 + "\n";
               var5 = 0;

               for(var6 = var3.size(); var5 < var6; ++var5) {
                  AddressRecord.ApehAddress var7 = (AddressRecord.ApehAddress)var3.get(var5);
                  StringBuilder var8 = (new StringBuilder("        <ADDRESSOPT title=\"")).append(var7.getTitle()).append("\" title_hint=\"").append(var7.getTitleHint()).append("\" settlement=\"").append(var7.getSettlement()).append("\" po_box=\"").append(var7.getPoBox()).append("\" zip=\"").append(var7.getZip()).append("\"/>\n");
                  var2 = var2 + var8.toString();
               }
            }

            var3 = ((AddressRecord)var1).getZips();
            Object var4;
            if (var3 != null) {
               var2 = var2 + "\n";
               var5 = 0;

               for(var6 = var3.size(); var5 < var6; ++var5) {
                  var4 = var3.get(var5);
                  if (var4 instanceof Hashtable) {
                     var2 = var2 + "        <ZIP " + this.gatherAttributes((Hashtable)var4) + "/>\n";
                  }
               }
            }

            var4 = ((AddressRecord)var1).getShire();
            if (var4 instanceof Hashtable) {
               var2 = var2 + "        <SHIRE " + this.gatherAttributes((Hashtable)var4) + "/>\n";
            }

            var2 = var2 + "    </" + this.U_XML_RECORD + ">";
         } else {
            var2 = var2 + "/>";
         }
      }

      return var2;
   }
}
