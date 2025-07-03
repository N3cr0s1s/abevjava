package hu.piller.enykp.alogic.primaryaccount.smallbusiness;

import hu.piller.enykp.alogic.primaryaccount.common.EditorPanel;

public class SmallBusinessEditorPanel extends EditorPanel {
   public static final String COMPONENT_FIRST_NAME = "first_name";
   public static final String COMPONENT_LAST_NAME = "last_name";
   public static final String COMPONENT_TAX_NUMBER = "tax_number";
   public static final String COMPONENT_TAX_ID = "tax_id";
   private static final long serialVersionUID = 1L;
   private SmallBusinessEditorBusiness sbe_business;

   public SmallBusinessEditorPanel() {
      super(new SmallBusinessEditorHeadPanel());
      this.initialize();
   }

   private void initialize() {
      this.sbe_business = new SmallBusinessEditorBusiness(this);
   }

   public SmallBusinessEditorBusiness getBusiness() {
      return this.sbe_business;
   }
}
