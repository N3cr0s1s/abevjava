package hu.piller.enykp.alogic.primaryaccount.taxexperts;

import hu.piller.enykp.alogic.primaryaccount.PrimaryAccountsPanel;
import hu.piller.enykp.alogic.primaryaccount.common.ListPanel;
import javax.swing.JPanel;

public class TaxExpertPanel extends ListPanel {
   private static final long serialVersionUID = 1L;
   private TaxExpertBusiness te_business;
   private TaxExpertEditorPanel editor_panel;
   private PrimaryAccountsPanel primary_account_panel;

   public TaxExpertPanel(PrimaryAccountsPanel var1) {
      this.primary_account_panel = var1;
      this.prepare();
   }

   private void prepare() {
      this.editor_panel = new TaxExpertEditorPanel();
      this.te_business = new TaxExpertBusiness(this);
      this.setVisibleEnvelopeButton(false);
   }

   public JPanel getEditorPanel() {
      return this.editor_panel;
   }

   public PrimaryAccountsPanel getNestPanel() {
      return this.primary_account_panel;
   }

   public TaxExpertBusiness getBusiness() {
      return this.te_business;
   }
}
