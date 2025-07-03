package hu.piller.enykp.alogic.upgrademanager_v2_0.gui;

import hu.piller.enykp.gui.GuiUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class JAutoUpdateInfoDialog extends JDialog {
   private JProgressBar progressBar;
   private JLabel label;

   public JAutoUpdateInfoDialog() {
      this.init();
   }

   public JAutoUpdateInfoDialog(Frame var1) {
      super(var1);
      this.init();
   }

   public void setText(String var1) {
      this.getLabel().setText(var1);
      int var2 = GuiUtil.getW("WWWPublikált frissítési információk automatikus letöltése folyamatban, kérem várjon!WWW");
      var2 = (int)Math.min((double)Math.max(GuiUtil.getW(var1), var2), 0.8D * (double)GuiUtil.getScreenW());
      this.setSize(var2, (int)this.getSize().getHeight());
      this.invalidate();
      this.repaint();
   }

   private JProgressBar getProgressBar() {
      if (this.progressBar == null) {
         this.progressBar = new JProgressBar();
         this.progressBar.setIndeterminate(true);
      }

      return this.progressBar;
   }

   private JLabel getLabel() {
      if (this.label == null) {
         this.label = new JLabel();
      }

      return this.label;
   }

   private void init() {
      this.getContentPane().setLayout(new BorderLayout());
      this.setLayout(new BorderLayout());
      this.setResizable(false);
      this.setLocationRelativeTo(this.getOwner());
      int var1 = GuiUtil.getW("WWWPublikált frissítési információk automatikus letöltése folyamatban, kérem várjon!WWW");
      this.setLocation((GuiUtil.getScreenW() - var1) / 2, GuiUtil.getScreenH() / 2 - 50);
      this.add(this.getContent());
      this.setDefaultCloseOperation(0);
      this.setTitle("Tájékoztatás");
      this.setModal(true);
      this.pack();
   }

   private JPanel getContent() {
      JPanel var1 = new JPanel();
      var1.setBorder(BorderFactory.createEtchedBorder(1));
      var1.setPreferredSize(new Dimension(460, 100));
      var1.setLayout(new GridBagLayout());
      GridBagConstraints var2 = new GridBagConstraints();
      var2.fill = 0;
      var2.anchor = 10;
      var2.gridx = 0;
      var2.gridy = 0;
      JLabel var3 = this.getLabel();
      var3.setText("Automatikus nyomtatványfrissítés...");
      var3.setFont(new Font("Arial", 0, GuiUtil.getCommonFontSize()));
      var1.add(var3, var2);
      var2.fill = 2;
      var2.gridy = 1;
      var2.insets = new Insets(15, 0, 5, 0);
      var1.add(this.getProgressBar(), var2);
      return var1;
   }
}
