package hu.piller.enykp.alogic.primaryaccount.smallbusiness;

import hu.piller.enykp.alogic.primaryaccount.PrimaryAccountsPanel;
import hu.piller.enykp.alogic.primaryaccount.common.ListPanel;
import javax.swing.JPanel;

public class SmallBusinessPanel extends ListPanel {
   private static final long serialVersionUID = 1L;
   private SmallBusinessBusiness sb_business;
   private SmallBusinessEditorPanel editor_panel;
   private PrimaryAccountsPanel primary_account_panel;

   public SmallBusinessPanel(PrimaryAccountsPanel var1) {
      this.primary_account_panel = var1;
      this.prepare();
   }

   private void prepare() {
      this.editor_panel = new SmallBusinessEditorPanel();
      this.sb_business = new SmallBusinessBusiness(this);
   }

   public JPanel getEditorPanel() {
      return this.editor_panel;
   }

   public PrimaryAccountsPanel getNestPanel() {
      return this.primary_account_panel;
   }

   public SmallBusinessBusiness getBusiness() {
      return this.sb_business;
   }
}
