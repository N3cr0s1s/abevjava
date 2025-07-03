package hu.piller.enykp.alogic.upgrademanager_v2_0.lookupupgrades;

import hu.piller.enykp.gui.framework.MainFrame;
import javax.swing.SwingUtilities;

public class UILookUpEventListener implements ILookUpEventListener {
   public void handleLookUpEvent(LookUpEvent var1) {
      if (var1.equals(LookUpEvent.START_LOOKUP)) {
         this.showMessage();
      } else if (var1.equals(LookUpEvent.FINISH_LOOKUP)) {
         this.removeMessage();
      } else if (var1.equals(LookUpEvent.UPGRADES_FOUND)) {
         this.showButton();
      }

   }

   private void showMessage() {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            MainFrame.thisinstance.mp.addFrissitesMessage();
            MainFrame.thisinstance.mp.updateUI();
         }
      });
   }

   private void removeMessage() {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            MainFrame.thisinstance.mp.removeFrissitesMessage();
            MainFrame.thisinstance.mp.updateUI();
         }
      });
   }

   private void showButton() {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            MainFrame.thisinstance.mp.removeFrissitesMessage();
            MainFrame.thisinstance.mp.addFrissitesButton();
            MainFrame.thisinstance.mp.updateUI();
         }
      });
   }
}
