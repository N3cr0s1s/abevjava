package hu.piller.enykp.alogic.upgrademanager_v2_0.gui;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.util.base.Tools;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public final class JErrorLogDialog extends JDialog {
   private JButton btn_close;

   public JErrorLogDialog(String var1) {
      this.setTitle("Telepítési hibák");
      this.setResizable(true);
      this.setModal(true);
      this.init(var1);
   }

   private void init(String var1) {
      JTextPane var2 = new JTextPane();
      var2.setEditable(false);

      try {
         var2.getDocument().insertString(0, var1, (AttributeSet)null);
      } catch (BadLocationException var6) {
         Tools.eLog(var6, 0);
      }

      ScrollPane var3 = new ScrollPane();
      var3.setPreferredSize(new Dimension(2 * GuiUtil.getW("Telepítési hibák"), 280));
      var3.add(var2);
      var3.setWheelScrollingEnabled(true);
      JPanel var4 = new JPanel();
      var4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Telepítési hibák"));
      var4.add(var3);
      var4.setLayout(new BoxLayout(var4, 3));
      this.setLayout(new GridBagLayout());
      GridBagConstraints var5 = new GridBagConstraints();
      var5.fill = 2;
      var5.gridheight = 1;
      var5.gridwidth = 1;
      var5.gridx = 0;
      var5.gridy = 0;
      var5.insets = new Insets(5, 5, 5, 5);
      var5.ipadx = 0;
      var5.ipady = 0;
      var5.weightx = 0.0D;
      var5.weighty = 0.0D;
      this.getContentPane().add(var4, var5);
      var5.gridx = 0;
      var5.gridy = 1;
      this.getContentPane().add(this.getCloseButton(), var5);
   }

   public JButton getCloseButton() {
      if (this.btn_close == null) {
         this.btn_close = new JButton("Bezár");
         this.btn_close.setMinimumSize(new Dimension(GuiUtil.getW(this.btn_close, this.btn_close.getText()), GuiUtil.getCommonItemHeight() + 2));
         this.btn_close.setPreferredSize(new Dimension(GuiUtil.getW(this.btn_close, this.btn_close.getText()), GuiUtil.getCommonItemHeight() + 2));
         this.btn_close.setMaximumSize(new Dimension(GuiUtil.getW(this.btn_close, this.btn_close.getText()), GuiUtil.getCommonItemHeight() + 2));
         this.btn_close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               JErrorLogDialog.this.dispose();
            }
         });
      }

      return this.btn_close;
   }
}
