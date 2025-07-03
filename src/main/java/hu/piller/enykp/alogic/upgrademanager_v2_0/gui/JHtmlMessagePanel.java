package hu.piller.enykp.alogic.upgrademanager_v2_0.gui;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JHtmlMessagePanel extends JPanel {
   public JHtmlMessagePanel(String var1, String var2, String var3, ActionListener var4) {
      JLabel var5 = new JLabel(var3);
      var5.setHorizontalAlignment(0);
      JButton var6 = new JButton(var2);
      var6.setHorizontalAlignment(0);
      var6.setAlignmentY(0.5F);
      var6.addActionListener(var4);
      JPanel var7 = new JPanel();
      var7.setLayout(new BoxLayout(var7, 3));
      var7.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(var1), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
      var7.add(var5);
      this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
      this.setLayout(new BoxLayout(this, 3));
      this.add(var7);
      this.add(Box.createRigidArea(new Dimension(0, 10)));
      this.add(var6);
      this.add(Box.createRigidArea(new Dimension(0, 10)));
   }
}
