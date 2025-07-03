package hu.piller.enykp.gui.framework;

import javax.swing.BorderFactory;
import javax.swing.JSplitPane;

public class CenterPane extends JSplitPane {
   public CenterPane() {
      super(1);
      this.setName("centerpane");
      this.setDividerSize(0);
      this.setResizeWeight(1.0D);
      this.setBorder(BorderFactory.createEtchedBorder());
   }
}
