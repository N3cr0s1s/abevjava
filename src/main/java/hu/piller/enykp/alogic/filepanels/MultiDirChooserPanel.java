package hu.piller.enykp.alogic.filepanels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MultiDirChooserPanel extends JPanel {
   File rootdir;
   DirChooserPanel dcp;
   DirChooserPanel2 dcp2;
   DirChooserPanel3 dcp3;
   JTabbedPane tp;

   public MultiDirChooserPanel() {
      this.setLayout(new BorderLayout());
      this.tp = new JTabbedPane();
      this.dcp = new DirChooserPanel();
      this.dcp3 = new DirChooserPanel3();
      this.tp.addTab("Összes könyvtár", this.dcp);
      this.tp.addTab("Egy könyvtár", this.dcp3);
      this.add(this.tp);
   }

   public File getSelectedDir() {
      Component var1 = this.tp.getSelectedComponent();
      if (var1.equals(this.dcp)) {
         return this.dcp.getSelectedDir();
      } else {
         return var1.equals(this.dcp3) ? this.dcp3.getSelectedDir() : null;
      }
   }

   public void setRootdir(File var1) {
      this.rootdir = var1;
      this.dcp.setRootdir(var1);
      this.dcp3.setRootdir(var1);
   }
}
