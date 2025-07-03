package hu.piller.enykp.alogic.primaryaccount.people;

import hu.piller.enykp.alogic.primaryaccount.common.EditorPanel;

public class PeopleEditorPanel extends EditorPanel {
   public static final String COMPONENT_FIRST_NAME = "first_name";
   public static final String COMPONENT_LAST_NAME = "last_name";
   public static final String COMPONENT_TAX_ID = "tax_id";
   private static final long serialVersionUID = 1L;
   private PeopleEditorBusiness pe_business;

   public PeopleEditorPanel() {
      super(new PeopleEditorHeadPanel());
      this.initialize();
   }

   private void initialize() {
      this.pe_business = new PeopleEditorBusiness(this);
   }

   public PeopleEditorBusiness getBusiness() {
      return this.pe_business;
   }
}
