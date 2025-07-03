package hu.piller.enykp.alogic.filepanels.filepanel.filterpanel;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public interface IFilteredFilesRefresh {
   Object[] getFileTableRow(Object var1);

   void rescan();

   void showFileList(Vector var1);

   void showFileList(DefaultTableModel var1);

   void setVct_filtered_files(Vector var1);

   Vector getVct_files();

   void setFilesTitle(String var1);

   void setFilesTitleLocked(boolean var1);
}
