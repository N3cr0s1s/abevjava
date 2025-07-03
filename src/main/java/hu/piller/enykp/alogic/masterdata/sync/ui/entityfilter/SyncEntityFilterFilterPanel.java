package hu.piller.enykp.alogic.masterdata.sync.ui.entityfilter;

import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.JTableFilter;
import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.TableFilterPanel;
import javax.swing.JTable;

public class SyncEntityFilterFilterPanel extends TableFilterPanel {
   public SyncEntityFilterFilterPanel(JTable var1) {
      super(var1);
   }

   protected JTableFilter getTableFilter() {
      if (this.tableFilter == null) {
         this.tableFilter = new SyncEntityFilterTableFilter();
      }

      return this.tableFilter;
   }
}
