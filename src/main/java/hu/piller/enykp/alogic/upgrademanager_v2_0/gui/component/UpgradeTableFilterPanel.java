package hu.piller.enykp.alogic.upgrademanager_v2_0.gui.component;

import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.JTableFilter;
import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.TableFilterPanel;
import javax.swing.JTable;

public class UpgradeTableFilterPanel extends TableFilterPanel {
   public UpgradeTableFilterPanel(JTable var1) {
      super(var1);
   }

   protected JTableFilter getTableFilter() {
      if (this.tableFilter == null) {
         this.tableFilter = new JUpgradeTableFilter();
         this.tableFilter.setStatusLabel(this.lbl_title);
      }

      return this.tableFilter;
   }
}
