package hu.piller.enykp.alogic.archivemanager;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import java.awt.Dimension;
import javax.swing.JDialog;

public class ArchiveManagerDialog extends JDialog {
   private ArchiveManagerDialogPanel amdp;

   public ArchiveManagerDialog() {
      super(MainFrame.thisinstance);
      this.build();
      this.setSize(new Dimension((int)((double)GuiUtil.getScreenW() * 0.8D), (int)((double)GuiUtil.getScreenH() * 0.8D)));
      this.setPreferredSize(this.getSize());
      this.setMinimumSize(this.getSize());
      this.setDefaultCloseOperation(2);
   }

   private void build() {
      this.amdp = new ArchiveManagerDialogPanel(this);
      this.getContentPane().add(this.amdp);
      this.setModal(true);
   }

   public ArchiveManagerDialogPanel getAmdp() {
      return this.amdp;
   }
}
