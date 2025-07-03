package hu.piller.enykp.alogic.primaryaccount.people;

import hu.piller.enykp.alogic.primaryaccount.common.DefaultEnvelope;
import hu.piller.enykp.alogic.primaryaccount.common.DefaultRecord;
import hu.piller.enykp.alogic.primaryaccount.common.DefaultRecordFactory;
import java.io.File;
import org.xml.sax.SAXException;

public class PeopleRecordFactory extends DefaultRecordFactory {
   public PeopleRecordFactory() throws SAXException {
      super.U_XML_RECORD_HEAD = "PEOPLE";
      super.U_XML_RECORD = "PERSON";
   }

   protected DefaultRecord getNewRecord(File var1, DefaultEnvelope var2) {
      return new PeopleRecord(this, var1, (PeopleEnvelope)var2);
   }
}
