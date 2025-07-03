package hu.piller.enykp.alogic.filepanels.filepanel.filterpanel;

import java.awt.Component;

public interface IFilterPanelLogic {
   Component getComponent(String var1);

   IFilterPanelBusiness getBusinessHandler();

   void setVisible(boolean var1);
}
