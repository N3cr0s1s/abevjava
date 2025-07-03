package hu.piller.enykp.alogic.masterdata.save;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityError;
import hu.piller.enykp.alogic.masterdata.gui.MDBlockComponent;
import hu.piller.enykp.alogic.masterdata.gui.MDForm;
import hu.piller.enykp.gui.framework.MainFrame;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class SaverMDForm extends MDForm {
   public SaverMDForm(Entity var1, boolean var2, List<EntityError> var3) {
      super(var1.getName());
      this.setTitle("Törzsadat");
      this.setSize(710, 550);
      this.setModal(true);
      this.setLocationRelativeTo(MainFrame.thisinstance);
      long var4 = var2 ? -1L : var1.getId();
      this.controller.setEntityIDForUpdate(var4);
      this.btnOk.setText("Mentés");
      Vector var6 = this.controller.convertEntityToMDBlockComponentBean(var1);
      this.showData(var6);
      if (var3.size() > 0) {
         Vector var7 = this.getBlockcomponents();
         Iterator var8 = var3.iterator();

         while(var8.hasNext()) {
            EntityError var9 = (EntityError)var8.next();
            Iterator var10 = var7.iterator();

            while(var10.hasNext()) {
               MDBlockComponent var11 = (MDBlockComponent)var10.next();
               if (var11.isComponentForBlock(var9.getBlockName())) {
                  var11.addError(var9);
                  var11.setMarkerToDiff();
               }
            }
         }

         this.refreshGUI();
      }

      this.setVisible(true);
   }
}
