package hu.piller.kripto.keys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
import javax.swing.filechooser.FileFilter;

public class StoreFilter extends FileFilter implements java.io.FileFilter {
   private boolean readContent;
   private Vector types;

   public StoreFilter(boolean readContent) {
      this(new int[]{200, 300, 400, 120, 110, 140, 130}, readContent);
   }

   public StoreFilter(int[] types, boolean readContent) {
      this.readContent = readContent;
      this.types = new Vector();

      for(int i = 0; i < types.length; ++i) {
         this.types.add(new Integer(types[i]));
      }

   }

   public boolean accept(File pathname) {
      String fileName = pathname.getName();
      int type;
      if (!this.readContent) {
         if (pathname.isDirectory()) {
            return false;
         }

         for(type = 0; type < this.types.size(); ++type) {
            String[] typeExts = (String[])StoreManager.EXT.get("" + this.types.elementAt(type));

            for(int j = 0; j < typeExts.length; ++j) {
               if (fileName.endsWith(typeExts[j])) {
                  return true;
               }
            }
         }
      } else {
         if (pathname.length() > 204800L) {
            return false;
         }

         try {
            if (pathname.isDirectory() || this.readContent && !pathname.canRead()) {
               return false;
            }

            type = StoreManager.getStoreType((InputStream)(new FileInputStream(pathname)));

            for(int i = 0; i < this.types.size(); ++i) {
               if (this.types.contains(new Integer(type))) {
                  return true;
               }
            }
         } catch (NoSuchAlgorithmException var6) {
            var6.printStackTrace();
         } catch (FileNotFoundException var7) {
            var7.printStackTrace();
         }
      }

      return false;
   }

   public String getDescription() {
      return "Store filter";
   }

   public void addFilterType(int type) {
      Integer typeInt = new Integer(type);
      if (!this.types.contains(typeInt)) {
         this.types.add(typeInt);
      }

   }

   public void removeFilterType(int type) {
      Integer typeInt = new Integer(type);
      if (this.types.contains(typeInt)) {
         this.types.remove(typeInt);
      }

   }
}
