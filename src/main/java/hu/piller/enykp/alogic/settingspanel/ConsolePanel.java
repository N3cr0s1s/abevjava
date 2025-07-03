package hu.piller.enykp.alogic.settingspanel;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ConsolePanel extends JPanel {
   private JScrollPane pane;
   private JTextArea content;

   public ConsolePanel() {
      this.setSize(new Dimension(300, 250));
      this.setLayout(new BoxLayout(this, 1));
      this.content = new JTextArea(50, 160);
      this.content.setMinimumSize(new Dimension(280, 200));
      this.content.setMaximumSize(new Dimension(280, 200));
      this.content.setPreferredSize(new Dimension(280, 200));
      this.pane = new JScrollPane(this.content);
      this.add(this.pane);
   }
}
