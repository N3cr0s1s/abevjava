package hu.piller.enykp.alogic.masterdata.gui.entityfilter;

import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.JTableFilter;
import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.TableFilterPanel;
import javax.swing.JTable;

public class EntityFilterFilterPanel extends TableFilterPanel {
   public EntityFilterFilterPanel(JTable var1) {
      super(var1);
   }

   protected JTableFilter getTableFilter() {
      if (this.tableFilter == null) {
         this.tableFilter = new EntityFilterTableFilter();
      }

      return this.tableFilter;
   }
}
