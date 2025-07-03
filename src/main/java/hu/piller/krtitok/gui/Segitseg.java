package hu.piller.krtitok.gui;

import hu.piller.krtitok.KriptoApp;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class Segitseg extends JFrame {
   public Segitseg() {
      this.setDefaultCloseOperation(2);
      JTextPane tp = new JTextPane();
      this.getContentPane().add(new JScrollPane(tp));
      this.pack();
      this.setSize(800, 600);
      this.setVisible(true);
      tp.setContentType("text/html");
      tp.setEditable(false);

      try {
         URL url = KriptoApp.class.getResource("resources/help/Krtitok_v1.2.html");
         tp.setPage(url);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
