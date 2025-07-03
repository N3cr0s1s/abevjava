package hu.piller.enykp.alogic.archivemanager.archivemanagerpanel;

import javax.swing.Icon;

public interface IListItem {
   Icon getIcon();

   Object getItem();

   void setItem(Object var1);

   Object getText();
}
