package hu.piller.enykp.gui.framework;

import javax.swing.JApplet;
import javax.swing.JLabel;

public class MainApplet extends JApplet {
   public void init() {
      JLabel var1 = new JLabel("start abevjava frame ...");
      this.getContentPane().add(var1);
      MainFrame.main((String[])null);
   }

   public void destroy() {
   }
}
