package hu.piller.enykp.alogic.upgrademanager_v2_0;

public class UpgradeTechnicalException extends Exception {
   public UpgradeTechnicalException() {
   }

   public UpgradeTechnicalException(String var1) {
      super(var1);
   }

   public UpgradeTechnicalException(String var1, Throwable var2) {
      super(var1);
      if (var2 != null) {
         if (var2.getCause() != null) {
            var2 = var2.getCause();
         }

         this.initCause(var2);
      }

   }
}
