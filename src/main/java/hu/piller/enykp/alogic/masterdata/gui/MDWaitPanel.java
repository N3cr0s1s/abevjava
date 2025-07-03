package hu.piller.enykp.alogic.masterdata.gui;

import hu.piller.enykp.gui.framework.MainFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class MDWaitPanel extends JDialog {
   public MDWaitPanel(JDialog var1, String var2) {
      super(var1);
      this.init(var2);
   }

   public MDWaitPanel(JFrame var1, String var2) {
      super(var1);
      this.init(var2);
   }

   public void close() {
      this.dispose();
   }

   private void init(String var1) {
      this.getContentPane().setLayout(new BorderLayout());
      this.setLayout(new BorderLayout());
      this.setResizable(false);
      this.add(this.getContent(var1));
      this.setDefaultCloseOperation(0);
      this.setModal(true);
      this.setTitle("Várakozást igénylő művelet");
      this.setSize(460, 100);
      this.setLocationRelativeTo(MainFrame.thisinstance);
      this.pack();
   }

   private JPanel getContent(String var1) {
      JPanel var2 = new JPanel();
      var2.setBorder(BorderFactory.createEtchedBorder(1));
      var2.setPreferredSize(new Dimension(460, 100));
      var2.setLayout(new GridBagLayout());
      GridBagConstraints var3 = new GridBagConstraints();
      var3.fill = 0;
      var3.anchor = 10;
      var3.gridx = 0;
      var3.gridy = 0;
      JLabel var4 = new JLabel();
      var4.setText(var1);
      var4.setFont(new Font("Arial", 0, 12));
      var2.add(var4, var3);
      JProgressBar var5 = new JProgressBar();
      var5.setIndeterminate(true);
      var3.fill = 2;
      var3.gridy = 1;
      var3.insets = new Insets(15, 0, 5, 0);
      var2.add(var5, var3);
      return var2;
   }
}
