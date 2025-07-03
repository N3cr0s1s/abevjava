package hu.piller.enykp.interfaces;

import java.io.File;
import java.net.URI;

public interface IFileChooser {
   void setSelectedFiles(File[] var1);

   Object[] getSelectedFiles();

   void setSelectedFilters(String[] var1);

   String[] getSelectedFilters();

   String[] getAllFilters();

   void addFilters(String[] var1, String[] var2);

   void removeFilters(String[] var1);

   void rescan();

   void hideFilters(String[] var1);

   void showFilters(String[] var1);

   void setSelectedPath(URI var1);
}
