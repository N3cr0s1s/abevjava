package hu.piller.enykp.alogic.primaryaccount.companies;

import hu.piller.enykp.alogic.primaryaccount.common.DefaultEnvelope;
import hu.piller.enykp.alogic.primaryaccount.common.DefaultRecord;
import hu.piller.enykp.alogic.primaryaccount.common.DefaultRecordFactory;
import java.io.File;
import org.xml.sax.SAXException;

public class CompanyRecordFactory extends DefaultRecordFactory {
   public CompanyRecordFactory() throws SAXException {
      super.U_XML_RECORD_HEAD = "COMPANIES";
      super.U_XML_RECORD = "COMPANY";
   }

   protected DefaultRecord getNewRecord(File var1, DefaultEnvelope var2) {
      return new CompanyRecord(this, var1, (CompanyEnvelope)var2);
   }
}
