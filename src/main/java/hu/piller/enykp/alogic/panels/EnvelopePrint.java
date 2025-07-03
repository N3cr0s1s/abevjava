package hu.piller.enykp.alogic.panels;

import hu.piller.enykp.alogic.primaryaccount.PAInfo;
import hu.piller.enykp.alogic.primaryaccount.common.IBusiness;
import hu.piller.enykp.alogic.primaryaccount.common.IRecord;
import hu.piller.enykp.alogic.primaryaccount.common.envelope.EnvelopeBusiness;
import hu.piller.enykp.alogic.primaryaccount.common.envelope.EnvelopePanel;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.ICommandObject;
import hu.piller.enykp.util.base.eventsupport.CloseEvent;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;

public class EnvelopePrint implements IBusiness, ICommandObject {
   private EnvelopePanel envelope_panel;
   private IRecord record;
   private JDialog dlg;
   private final Object[] update_skin = new Object[]{"work_panel", "static", "Boríték nyomtatás", "tab_close", null};
   private final EnvelopePrint.CloseActionListener close_action_listener = new EnvelopePrint.CloseActionListener();
   private final Vector cmd_list = new Vector(Arrays.asList("abev.showEnvelopePrintDialog"));
   Boolean[] states;

   public EnvelopePrint() {
      this.states = new Boolean[]{Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
      this.build();
   }

   private void build() {
      this.envelope_panel = new EnvelopePanel();
   }

   private void prepare(IRecord var1) {
      EnvelopeBusiness var2 = this.envelope_panel.getBusiness();
      JButton var3 = (JButton)this.envelope_panel.getEPComponent("cancel_button");
      var2.setRecord(var1, this);
      var2.setOrgCbo(false);
      var3.addActionListener(this.close_action_listener);
      this.update_skin[4] = this.envelope_panel;
   }

   private IRecord getPARecord() {
      PAInfo var1 = PAInfo.getInstance();
      return var1.get_pa_record();
   }

   public void restorePanel() {
      if (this.dlg != null) {
         this.dlg.setVisible(false);
         this.dlg.dispose();
      }
   }

   public void execute() {
      if (this.dlg == null) {
         this.dlg = new JDialog(MainFrame.thisinstance);
      }

      boolean var1 = true;
      Container var2 = this.dlg.getContentPane();
      this.prepare(this.getPARecord());
      if (var1) {
         this.envelope_panel.invalidate();
         this.envelope_panel.setVisible(true);
         var2.add(this.envelope_panel);
         this.dlg.setSize(700, 450);
         this.dlg.setTitle("Boríték nyomtatása");
         this.dlg.setResizable(false);
         this.dlg.setModal(true);
         this.dlg.setLocationRelativeTo(MainFrame.thisinstance);
         this.dlg.setVisible(true);
      }

   }

   public void setParameters(Hashtable var1) {
   }

   public ICommandObject copy() {
      return new EnvelopePrint();
   }

   public boolean isCommandIdentical(String var1) {
      if (var1 != null) {
         var1 = var1.trim();
         if (var1.equalsIgnoreCase(this.cmd_list.get(0).toString())) {
            return true;
         }
      }

      return false;
   }

   public Vector getCommandList() {
      return this.cmd_list;
   }

   public Hashtable getCommandHelps() {
      return null;
   }

   public Object getState(Object var1) {
      if (var1 instanceof Integer) {
         int var2 = (Integer)var1;
         return var2 >= 0 && var2 <= this.states.length - 1 ? this.states[var2] : Boolean.FALSE;
      } else {
         return Boolean.FALSE;
      }
   }

   private class CloseActionListener implements ActionListener {
      private CloseActionListener() {
      }

      public void actionPerformed(ActionEvent var1) {
         EnvelopePrint.this.envelope_panel.fireEvent(new CloseEvent(EnvelopePrint.this.envelope_panel));
      }

      // $FF: synthetic method
      CloseActionListener(Object var2) {
         this();
      }
   }
}
