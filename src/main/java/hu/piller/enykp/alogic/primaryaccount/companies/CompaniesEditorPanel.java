package hu.piller.enykp.alogic.primaryaccount.companies;

import hu.piller.enykp.alogic.primaryaccount.common.EditorPanel;
import java.awt.Dimension;

public class CompaniesEditorPanel extends EditorPanel {
   public static final String COMPONENT_COMPANY_NAME = "company_name";
   public static final String COMPONENT_TAX_NUMBER = "tax_number";
   private static final long serialVersionUID = 1L;
   private CompaniesEditorBusiness ce_business;

   public CompaniesEditorPanel() {
      super(new CompaniesEditorHeadPanel());
      this.setMinimumSize(new Dimension(650, 450));
      this.initialize();
   }

   private void initialize() {
      this.ce_business = new CompaniesEditorBusiness(this);
   }

   public CompaniesEditorBusiness getBusiness() {
      return this.ce_business;
   }
}
