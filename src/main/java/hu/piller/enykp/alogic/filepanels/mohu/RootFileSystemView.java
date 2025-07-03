package hu.piller.enykp.alogic.filepanels.mohu;

import me.necrocore.abevjava.NecroFile;

import java.io.File;
import javax.swing.filechooser.FileSystemView;

public class RootFileSystemView extends FileSystemView {
   File root;
   File[] roots = new File[1];

   public RootFileSystemView(File var1) {
      this.root = var1;
      this.roots[0] = var1;
   }

   public File createNewFolder(File var1) {
      File var2 = new NecroFile(var1, "New Folder");
      var2.mkdir();
      return var2;
   }

   public File getDefaultDirectory() {
      return this.root;
   }

   public File getHomeDirectory() {
      return this.root;
   }

   public File[] getRoots() {
      return this.roots;
   }
}
