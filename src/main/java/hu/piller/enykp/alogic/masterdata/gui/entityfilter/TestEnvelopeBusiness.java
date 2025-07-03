package hu.piller.enykp.alogic.masterdata.gui.entityfilter;

import hu.piller.enykp.alogic.primaryaccount.common.IBusiness;
import hu.piller.enykp.alogic.primaryaccount.common.IRecord;
import hu.piller.enykp.alogic.primaryaccount.common.envelope.EnvelopeBusiness;
import hu.piller.enykp.alogic.primaryaccount.common.envelope.EnvelopePanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestEnvelopeBusiness extends EnvelopeBusiness {
   public TestEnvelopeBusiness(EnvelopePanel var1) {
      super(var1);
   }

   protected void prepareCancel() {
      this.btn_cancel.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TestEnvelopeBusiness.this.parent_business.restorePanel();
         }
      });
   }

   public void setRecord(IRecord var1, IBusiness var2) {
      this.record = var1;
      this.parent_business = var2;
      this.showStableAddress();
   }
}
