package hu.piller.enykp.alogic.masterdata.envelope.provider.addresses;

import hu.piller.enykp.alogic.masterdata.envelope.model.Address;
import hu.piller.enykp.alogic.masterdata.envelope.model.AddressOpt;
import hu.piller.enykp.alogic.masterdata.envelope.model.ZipRange;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AddressesHandler extends DefaultHandler {
   private ArrayList<Address> addresses = new ArrayList();
   private Address currAddress;

   public ArrayList<Address> getAddresses() {
      return this.addresses;
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if ("ADDRESS".equals(var3)) {
         this.currAddress = new Address();
         this.currAddress.setId(Integer.parseInt(var4.getValue("id")));
      } else if ("ADDRESSOPT".equals(var3)) {
         AddressOpt var5 = new AddressOpt();
         var5.setPoBox(var4.getValue("po_box"));
         var5.setSettlement(var4.getValue("settlement"));
         var5.setTitle(var4.getValue("title"));
         var5.setTitleHint(var4.getValue("title_hint"));
         var5.setZip(Integer.parseInt(var4.getValue("zip")));
         this.currAddress.getAddressOpts().add(var5);
      } else if ("ZIP".equals(var3)) {
         ZipRange var6 = new ZipRange();
         var6.setFrom(Integer.parseInt(var4.getValue("zip_from")));
         var6.setTo(Integer.parseInt(var4.getValue("zip_to")));
         this.currAddress.getZipRanges().add(var6);
      } else if ("SHIRE".equals(var3)) {
         this.currAddress.setShire(Integer.parseInt(var4.getValue("code")));
      }

   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      if ("ADDRESS".equals(var3)) {
         this.addresses.add(this.currAddress);
      }

   }
}
