package hu.piller.enykp.alogic.filepanels.filepanel.filterpanel;

import java.io.File;
import java.util.Vector;

public interface IFilterPanelBusiness {
   void setFileBusiness(IFilteredFilesRefresh var1);

   Vector filterFileList(Vector var1);

   String getFileState(File var1, String var2);

   void addFilters(String[] var1, String[] var2);

   void removeFilters(String[] var1);

   void clearFilters();

   void setExclusiveFilterSelection(boolean var1);

   void setSelectedFilters(String[] var1);

   String[] getSelectedFilters();

   String[] getAllFilters();

   void hideFilters(String[] var1);

   void showFilters(String[] var1);

   void storeFileInfo(Vector var1);

   void resetFileInfo(Vector var1);

   void setEnabled(boolean var1);

   void setFilterVisibility(boolean var1);

   boolean getFilterVisibility();

   void setFileFilterTypeVisibility(boolean var1);

   boolean getFileFilterTypeVisibility();

   void initials(Object var1);

   void setDefaultFilterValues(int var1, String var2);

   void refresh(Object var1);

   void setVisible(boolean var1);

   boolean getVisible();

   void saveLastFilterValues(String var1);

   void loadLastFilterValues(String var1);
}
