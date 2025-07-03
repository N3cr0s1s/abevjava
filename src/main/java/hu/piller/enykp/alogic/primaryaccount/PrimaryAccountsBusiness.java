package hu.piller.enykp.alogic.primaryaccount;

import hu.piller.enykp.alogic.primaryaccount.companies.CompaniesPanel;
import hu.piller.enykp.alogic.primaryaccount.people.PeoplePanel;
import hu.piller.enykp.alogic.primaryaccount.smallbusiness.SmallBusinessPanel;
import hu.piller.enykp.alogic.primaryaccount.taxexperts.TaxExpertPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PrimaryAccountsBusiness {
   public static final int LIST_GROUP_COMPANIES = 0;
   public static final int LIST_GROUP_TAXEXPERTS = 1;
   private PrimaryAccountsPanel p_accounts_panel;
   private JTabbedPane tbp_tabs;
   private int companies_idx;

   public PrimaryAccountsBusiness(PrimaryAccountsPanel var1) {
      this.p_accounts_panel = var1;
      this.prepare();
   }

   private void prepare() {
      this.tbp_tabs = (JTabbedPane)this.p_accounts_panel.getPAPComponent("tabs");
      this.prepareTabs();
   }

   private void prepareTabs() {
      this.tbp_tabs.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent var1) {
            int var2 = PrimaryAccountsBusiness.this.tbp_tabs.getSelectedIndex();
            if (var2 < 3) {
               PrimaryAccountsBusiness.this.companies_idx = var2;
            }

         }
      });
   }

   public void setStartGroup(int var1) {
      switch(var1) {
      case 0:
         this.tbp_tabs.setSelectedIndex(this.companies_idx);
         break;
      case 1:
         this.tbp_tabs.setSelectedIndex(3);
         break;
      default:
         this.tbp_tabs.setSelectedIndex(0);
      }

   }

   public Object searchElement(int var1, Object var2) {
      Object var3 = null;
      if ((var1 & 1) == 1) {
         var3 = ((CompaniesPanel)this.p_accounts_panel.getPanel(0)).getBusiness().searchCompany(var2);
      }

      if (var3 == null && (var1 & 2) == 2) {
         var3 = ((SmallBusinessPanel)this.p_accounts_panel.getPanel(1)).getBusiness().searchSmallBusiness(var2);
      }

      if (var3 == null && (var1 & 4) == 4) {
         var3 = ((PeoplePanel)this.p_accounts_panel.getPanel(2)).getBusiness().searchPeople(var2);
      }

      if (var3 == null && (var1 & 8) == 8) {
         var3 = ((TaxExpertPanel)this.p_accounts_panel.getPanel(3)).getBusiness().searchTaxExpert(var2);
      }

      return var3;
   }

   public void reload(int var1) {
      if ((var1 & 1) == 1) {
         ((CompaniesPanel)this.p_accounts_panel.getPanel(0)).getBusiness().reload();
      }

      if ((var1 & 2) == 2) {
         ((SmallBusinessPanel)this.p_accounts_panel.getPanel(1)).getBusiness().reload();
      }

      if ((var1 & 4) == 4) {
         ((PeoplePanel)this.p_accounts_panel.getPanel(2)).getBusiness().reload();
      }

      if ((var1 & 8) == 8) {
         ((TaxExpertPanel)this.p_accounts_panel.getPanel(3)).getBusiness().reload();
      }

   }
}
