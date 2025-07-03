package hu.piller.enykp.alogic.primaryaccount.people;

import hu.piller.enykp.alogic.primaryaccount.PrimaryAccountsPanel;
import hu.piller.enykp.alogic.primaryaccount.common.ListPanel;
import javax.swing.JPanel;

public class PeoplePanel extends ListPanel {
   private static final long serialVersionUID = 1L;
   private PeopleBusiness p_business;
   private PeopleEditorPanel editor_panel;
   private PrimaryAccountsPanel primary_account_panel;

   public PeoplePanel(PrimaryAccountsPanel var1) {
      this.primary_account_panel = var1;
      this.prepare();
   }

   private void prepare() {
      this.editor_panel = new PeopleEditorPanel();
      this.p_business = new PeopleBusiness(this);
   }

   public JPanel getEditorPanel() {
      return this.editor_panel;
   }

   public PrimaryAccountsPanel getNestPanel() {
      return this.primary_account_panel;
   }

   public PeopleBusiness getBusiness() {
      return this.p_business;
   }
}
