package hu.piller.enykp.alogic.primaryaccount.smallbusiness;

import hu.piller.enykp.alogic.primaryaccount.common.DefaultEnvelope;
import hu.piller.enykp.alogic.primaryaccount.common.DefaultRecord;
import hu.piller.enykp.alogic.primaryaccount.common.DefaultRecordFactory;
import java.io.File;
import org.xml.sax.SAXException;

public class SmallBusinessRecordFactory extends DefaultRecordFactory {
   public SmallBusinessRecordFactory() throws SAXException {
      super.U_XML_RECORD_HEAD = "SMALLBUSINESSES";
      super.U_XML_RECORD = "SMALLBUSINESS";
   }

   protected DefaultRecord getNewRecord(File var1, DefaultEnvelope var2) {
      return new SmallBusinessRecord(this, var1, (SmallBusinessEnvelope)var2);
   }
}
