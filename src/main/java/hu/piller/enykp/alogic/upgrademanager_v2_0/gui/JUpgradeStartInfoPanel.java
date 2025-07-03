package hu.piller.enykp.alogic.upgrademanager_v2_0.gui;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

public class JUpgradeStartInfoPanel extends JDialog implements ActionListener {
   private JProgressBar progressBar;
   private int length;
   private int counter;
   private Timer timer;

   public JUpgradeStartInfoPanel(String var1, JDialog var2) {
      super(var2);
      this.init(var1);
   }

   public JUpgradeStartInfoPanel(String var1, JDialog var2, int var3) {
      super(var2);
      this.length = var3;
      this.init(var1);
   }

   public void close() {
      if (this.timer != null) {
         synchronized(this) {
            this.timer.stop();
         }
      }

      this.dispose();
   }

   private void init(String var1) {
      this.getContentPane().setLayout(new BorderLayout());
      this.setLayout(new BorderLayout());
      this.setResizable(false);
      this.setLocationRelativeTo(MainFrame.thisinstance);
      int var2 = GuiUtil.getW(new JLabel(var1), var1) + 40;
      this.setLocation((GuiUtil.getScreenW() - var2) / 2, GuiUtil.getScreenH() / 2 - 50);
      this.add(this.getContent(var1));
      this.setDefaultCloseOperation(0);
      this.setModal(true);
      this.pack();
   }

   private JPanel getContent(String var1) {
      JPanel var2 = new JPanel();
      var2.setBorder(BorderFactory.createEtchedBorder(1));
      var2.setPreferredSize(new Dimension(GuiUtil.getW(new JLabel(var1), var1) + 40, 100));
      var2.setLayout(new GridBagLayout());
      GridBagConstraints var3 = new GridBagConstraints();
      var3.fill = 0;
      var3.anchor = 10;
      var3.gridx = 0;
      var3.gridy = 0;
      JLabel var4 = new JLabel();
      var4.setText(var1);
      var2.add(var4, var3);
      var3.fill = 2;
      var3.gridy = 1;
      var3.insets = new Insets(15, 0, 5, 0);
      var2.add(this.getProgressBar(), var3);
      return var2;
   }

   private JProgressBar getProgressBar() {
      return this.length == 0 ? this.getIndeterminateProgressBar() : this.getDeterminateProgressBar();
   }

   private JProgressBar getIndeterminateProgressBar() {
      if (this.progressBar == null) {
         this.progressBar = new JProgressBar();
         this.progressBar.setIndeterminate(true);
      }

      return this.progressBar;
   }

   private JProgressBar getDeterminateProgressBar() {
      if (this.progressBar == null) {
         this.progressBar = new JProgressBar(0, this.length);
         this.progressBar.setValue(this.counter);
         this.progressBar.setIndeterminate(false);
         this.timer = new Timer(1000, this);
         this.timer.start();
      }

      return this.progressBar;
   }

   public void actionPerformed(ActionEvent var1) {
      synchronized(this) {
         this.progressBar.setValue(++this.counter);
      }
   }
}
