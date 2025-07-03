package hu.piller.enykp.alogic.primaryaccount.taxexperts;

import hu.piller.enykp.alogic.primaryaccount.common.DefaultEnvelope;
import hu.piller.enykp.alogic.primaryaccount.common.DefaultRecord;
import hu.piller.enykp.alogic.primaryaccount.common.DefaultRecordFactory;
import java.io.File;
import org.xml.sax.SAXException;

public class TaxExpertRecordFactory extends DefaultRecordFactory {
   public TaxExpertRecordFactory() throws SAXException {
      super.U_XML_RECORD_HEAD = "TAXEXPERTS";
      super.U_XML_RECORD = "TAXEXPERT";
   }

   protected DefaultRecord getNewRecord(File var1, DefaultEnvelope var2) {
      return new TaxExpertRecord(this, var1, (TaxExpertEnvelope)var2);
   }
}
