package hu.piller.enykp.alogic.masterdata.gui.entityfilter;

import hu.piller.enykp.alogic.primaryaccount.common.envelope.EnvelopePanel;

public class TestEnvelopePanel extends EnvelopePanel {
   protected void prepare() {
      this.e_business = new TestEnvelopeBusiness(this);
   }
}
