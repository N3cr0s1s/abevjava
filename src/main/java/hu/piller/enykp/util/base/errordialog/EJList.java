package hu.piller.enykp.util.base.errordialog;

import javax.swing.JList;

public class EJList extends JList {
   public EJList() {
      this.setCellRenderer(new ErrorListCellRenderer());
   }
}
