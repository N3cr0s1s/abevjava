package hu.piller.enykp.alogic.masterdata.gui.entityfilter;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.gui.entityfilter.print.EntityRecord;
import hu.piller.enykp.alogic.masterdata.gui.entityfilter.print.MaganszemelyEgyeniVallalkozoEnvelope;
import hu.piller.enykp.alogic.masterdata.gui.entityfilter.print.TarsasagEnvelope;
import hu.piller.enykp.alogic.primaryaccount.common.IBusiness;
import hu.piller.enykp.alogic.primaryaccount.common.IRecord;
import java.awt.BorderLayout;
import java.awt.Point;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class EnvelopeTestDialog extends JDialog implements IBusiness {
   public EnvelopeTestDialog(Entity var1) {
      this.init(var1);
   }

   public void close() {
      this.dispose();
   }

   private void init(Entity var1) {
      this.getContentPane().setLayout(new BorderLayout());
      this.setLayout(new BorderLayout());
      this.setResizable(false);
      this.setLocationRelativeTo(this.getOwner());
      Point var2 = this.getLocation();
      this.setLocation(var2.x - 230, var2.y - 200);
      this.add(this.getContent(var1));
      this.setModal(true);
      this.pack();
   }

   private JPanel getContent(Entity var1) {
      TestEnvelopePanel var2 = new TestEnvelopePanel();
      var2.getBusiness().setRecord(this.entity2IRecord(var1), this);
      return var2;
   }

   public void restorePanel() {
      this.dispose();
   }

   private IRecord entity2IRecord(Entity var1) {
      EntityRecord var2 = null;
      if ("Társaság".equals(var1.getName())) {
         var2 = new EntityRecord(var1, new TarsasagEnvelope(var1));
      } else if ("Magánszemély".equals(var1.getName())) {
         var2 = new EntityRecord(var1, new MaganszemelyEgyeniVallalkozoEnvelope(var1));
      } else if ("Egyéni vállalkozó".equals(var1.getName())) {
         var2 = new EntityRecord(var1, new MaganszemelyEgyeniVallalkozoEnvelope(var1));
      }

      return var2;
   }
}
