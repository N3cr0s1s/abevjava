package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd;

import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.bankszamlaszam.JBankszamlaszamEditorDialog;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.kozossegiadoszam.JKozossegiAdoszamEditorDialog;
import javax.swing.JDialog;

public class TechnicalMdEditorFactory {
   private static TechnicalMdEditorFactory instance;

   private TechnicalMdEditorFactory() {
   }

   public static TechnicalMdEditorFactory getInstance() {
      if (instance == null) {
         instance = new TechnicalMdEditorFactory();
      }

      return instance;
   }

   public JDialog getEditorDialogForTechnicalMasterData(String var1) {
      if ("Bankszámlaszám".equals(var1)) {
         return new JBankszamlaszamEditorDialog();
      } else {
         return "Közösségi adószám".equals(var1) ? new JKozossegiAdoszamEditorDialog() : null;
      }
   }
}
