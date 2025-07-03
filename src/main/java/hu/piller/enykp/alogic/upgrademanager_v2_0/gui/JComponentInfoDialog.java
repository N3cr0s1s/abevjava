package hu.piller.enykp.alogic.upgrademanager_v2_0.gui;

import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import hu.piller.enykp.gui.GuiUtil;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class JComponentInfoDialog extends JDialog implements ActionListener {
   JLabel lbl_header;
   JLabel lbl_lblnev;
   JLabel lbl_nev;
   JLabel lbl_lblverzio;
   JLabel lbl_verzio;
   JLabel lbl_lblorg;
   JLabel lbl_org;
   JPanel pnl_description;
   JTextPane text_description;
   JButton button;
   int dialogW = 260;

   public JComponentInfoDialog(VersionData var1) {
      this.dialogW = Math.max(260, 260 * GuiUtil.getCommonFontSize() / 12);
      this.init(var1);
      Dimension var2 = new Dimension(this.dialogW + 60, GuiUtil.getCommonItemHeight() * 20);
      this.setPreferredSize(var2);
      this.setSize(var2);
      this.setMinimumSize(var2);
   }

   private void init(VersionData var1) {
      this.setTitle("Az összetevő névjegye");
      this.setResizable(true);
      this.setModal(true);
      this.lbl_header = new JLabel("Összetevő leíró");
      this.lbl_lblnev = new JLabel("Összetevő :");
      this.lbl_nev = new JLabel(var1.getName() + " " + this.getTypeByCategory(var1.getCategory()));
      this.lbl_lblverzio = new JLabel("Verzió :");
      this.lbl_verzio = new JLabel(var1.getVersion().toString());
      this.lbl_lblorg = new JLabel("Közzétevő :");
      this.lbl_org = new JLabel(OrgInfo.getInstance().getOrgShortnameByOrgID(var1.getOrganization()));
      this.pnl_description = new JPanel();
      this.pnl_description.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Leírás"));
      this.text_description = new JTextPane();
      this.text_description.setPreferredSize(new Dimension(this.dialogW, GuiUtil.getCommonItemHeight() * 8));
      this.text_description.setEditable(false);

      try {
         this.text_description.getDocument().insertString(0, var1.getDescription() != null && !"".equals(var1.getDescription().trim()) ? var1.getDescription() : "Az összetevő szállítója nem adott meg leírást", (AttributeSet)null);
      } catch (BadLocationException var4) {
      }

      this.button = new JButton("Bezár");
      this.button.addActionListener(this);
      this.button.setMinimumSize(new Dimension(GuiUtil.getW(this.button, this.button.getText()), GuiUtil.getCommonItemHeight() + 2));
      this.button.setMaximumSize(this.button.getMinimumSize());
      this.button.setPreferredSize(this.button.getMinimumSize());
      this.getContentPane().setLayout(new GridBagLayout());
      GridBagConstraints var2 = new GridBagConstraints();
      var2.anchor = 21;
      var2.fill = 2;
      var2.ipadx = 0;
      var2.ipady = 0;
      var2.weightx = 0.0D;
      var2.weighty = 0.0D;
      var2.gridx = 0;
      var2.gridy = 1;
      var2.gridwidth = 1;
      var2.insets = new Insets(15, 6, 5, 5);
      this.getContentPane().add(this.lbl_lblnev, var2);
      var2.gridx = 1;
      var2.gridy = 1;
      this.getContentPane().add(this.lbl_nev, var2);
      var2.gridx = 0;
      var2.gridy = 2;
      var2.insets = new Insets(0, 6, 5, 5);
      this.getContentPane().add(this.lbl_lblverzio, var2);
      var2.gridx = 1;
      var2.gridy = 2;
      this.getContentPane().add(this.lbl_verzio, var2);
      var2.gridx = 0;
      var2.gridy = 3;
      this.getContentPane().add(this.lbl_lblorg, var2);
      var2.gridx = 1;
      var2.gridy = 3;
      this.getContentPane().add(this.lbl_org, var2);
      JScrollPane var3 = new JScrollPane(this.text_description, 20, 31);
      var3.setPreferredSize(new Dimension(this.dialogW, GuiUtil.getCommonItemHeight() * 8));
      this.text_description.setCaretPosition(0);
      this.pnl_description.add(var3);
      var2.gridx = 0;
      var2.gridy = 4;
      var2.gridwidth = 2;
      var2.insets = new Insets(0, 5, 5, 5);
      this.getContentPane().add(this.pnl_description, var2);
      var2.gridx = 0;
      var2.gridy = 5;
      var2.gridwidth = 2;
      var2.anchor = 10;
      var2.fill = 0;
      var2.insets = new Insets(5, 5, 5, 5);
      this.getContentPane().add(this.button, var2);
   }

   private String getTypeByCategory(String var1) {
      if ("Help".equals(var1)) {
         return "Segédlet";
      } else {
         return "Template".equals(var1) ? "Nyomtatvány" : "(ismeretlen típus)";
      }
   }

   public void actionPerformed(ActionEvent var1) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            JComponentInfoDialog.this.dispose();
         }
      });
   }
}
