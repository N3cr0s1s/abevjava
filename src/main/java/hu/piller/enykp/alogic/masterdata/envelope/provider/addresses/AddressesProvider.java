package hu.piller.enykp.alogic.masterdata.envelope.provider.addresses;

import hu.piller.enykp.alogic.masterdata.envelope.model.Address;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.util.base.ErrorList;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;

public class AddressesProvider {
   public ArrayList<Address> getAddressesByOrgId(String var1) {
      ArrayList var2 = new ArrayList();
      byte[] var3 = (byte[])((byte[])OrgInfo.getInstance().getOrgAddress(var1));

      try {
         SAXParserFactory var4 = SAXParserFactory.newInstance();
         SAXParser var5 = var4.newSAXParser();
         AddressesHandler var6 = new AddressesHandler();
         var5.parse(new InputSource(new ByteArrayInputStream(var3)), var6);
         var2 = var6.getAddresses();
      } catch (Exception var7) {
         ErrorList.getInstance().writeError(AddressesProvider.class, var1 + " boríték címlista feldolgozási hiba!", ErrorList.LEVEL_SHOW_ERROR, var7, (Object)null);
      }

      return var2;
   }
}
