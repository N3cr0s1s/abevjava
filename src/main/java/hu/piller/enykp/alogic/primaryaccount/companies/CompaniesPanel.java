package hu.piller.enykp.alogic.primaryaccount.companies;

import hu.piller.enykp.alogic.primaryaccount.PrimaryAccountsPanel;
import hu.piller.enykp.alogic.primaryaccount.common.ListPanel;
import javax.swing.JPanel;

public class CompaniesPanel extends ListPanel {
   private static final long serialVersionUID = 1L;
   private CompaniesBusiness c_business;
   private CompaniesEditorPanel editor_panel;
   private PrimaryAccountsPanel primary_account_panel;

   public CompaniesPanel(PrimaryAccountsPanel var1) {
      this.primary_account_panel = var1;
      this.prepare();
   }

   private void prepare() {
      this.editor_panel = new CompaniesEditorPanel();
      this.c_business = new CompaniesBusiness(this);
   }

   public PrimaryAccountsPanel getNestPanel() {
      return this.primary_account_panel;
   }

   public JPanel getEditorPanel() {
      return this.editor_panel;
   }

   public CompaniesBusiness getBusiness() {
      return this.c_business;
   }
}
