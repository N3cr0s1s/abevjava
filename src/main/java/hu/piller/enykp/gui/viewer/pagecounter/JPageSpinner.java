package hu.piller.enykp.gui.viewer.pagecounter;

import javax.swing.JSpinner;

public class JPageSpinner extends JSpinner {
   public void fireStateChanged_() {
      this.fireStateChanged();
   }
}
