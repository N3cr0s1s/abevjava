package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.bankszamlaszam;

import hu.piller.enykp.alogic.masterdata.gui.MDGUIFieldFactory;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.ITechnicalMdEditor;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.ITechnicalMdModel;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.SpringUtilities;
import hu.piller.enykp.gui.framework.MainFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class JBankszamlaszamEditorDialog extends JDialog implements ITechnicalMdEditor, ActionListener {
   private BankszamlaszamModel model;
   private JTextField bankName;
   private JFormattedTextField bankId;
   private JFormattedTextField accountId;
   private String oriValue;

   public JBankszamlaszamEditorDialog() {
      super(MainFrame.thisinstance);
      this.setModal(true);
      this.setDefaultCloseOperation(0);
      this.setUpLayout();
   }

   public void setModel(ITechnicalMdModel var1) {
      this.oriValue = var1.getValue();
      this.model = (BankszamlaszamModel)BankszamlaszamModel.class.cast(var1);
      this.bankName.setText(this.model.getBankName());
      this.bankId.setText(this.model.getBankId());
      this.accountId.setText(this.model.getAccountId());
   }

   public ITechnicalMdModel getModel() {
      return this.model;
   }

   public void actionPerformed(ActionEvent var1) {
      if ("btn_ok".equals(var1.getActionCommand())) {
         this.model.setBankName(this.bankName.getText());
         this.model.setBankId(this.bankId.getText().replaceAll("_", ""));
         this.model.setAccountId(this.accountId.getText().replaceAll("_", ""));
         String var2 = this.model.getDataError();
         if (var2 != null) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, var2, "Hiba", 0);
            return;
         }
      }

      if ("btn_cancel".equals(var1.getActionCommand())) {
         this.model.setValue(this.oriValue);
      }

      this.dispose();
   }

   private void setUpLayout() {
      this.setMinimumSize(new Dimension(400, 150));
      this.setPreferredSize(new Dimension(400, 150));
      this.setLocationRelativeTo(this.getParent());
      this.setUpInputFields();
      this.buildLayout();
   }

   private void setUpInputFields() {
      this.bankName = new JTextField();
      this.bankName.setMinimumSize(new Dimension(250, 20));
      this.bankName.setPreferredSize(new Dimension(250, 20));
      this.bankName.setMaximumSize(new Dimension(250, 20));
      this.bankName.setToolTipText("A számlavezető pénzintézet neve");
      this.bankId = (JFormattedTextField)MDGUIFieldFactory.getInstance().getInputFieldForType("TPenzIntezet");
      this.bankId.setMinimumSize(new Dimension(80, 20));
      this.bankId.setPreferredSize(new Dimension(80, 20));
      this.bankId.setMaximumSize(new Dimension(80, 20));
      this.bankId.setToolTipText("A bankszámlaszám első nyolc számjegye");
      this.accountId = (JFormattedTextField)MDGUIFieldFactory.getInstance().getInputFieldForType("TSzamlaSzam");
      this.accountId.setMinimumSize(new Dimension(140, 20));
      this.accountId.setPreferredSize(new Dimension(140, 20));
      this.accountId.setMaximumSize(new Dimension(140, 20));
      this.accountId.setToolTipText("A bankszámlaszám második nyolc számjegye, egymástól kötőjellel elválasztva");
   }

   private void buildLayout() {
      this.setLayout(new BoxLayout(this.getContentPane(), 1));
      JPanel var1 = new JPanel();
      var1.setBorder(BorderFactory.createLineBorder(new Color(184, 207, 229)));
      var1.setLayout(new SpringLayout());
      var1.add(new JLabel("Pénzintézet neve"));
      var1.add(this.bankName);
      var1.add(new JLabel("Számlaszám"));
      JPanel var2 = new JPanel();
      var2.setLayout(new BoxLayout(var2, 0));
      var2.add(this.bankId);
      var2.add(Box.createHorizontalStrut(5));
      var2.add(new JLabel("-"));
      var2.add(Box.createHorizontalStrut(5));
      var2.add(this.accountId);
      var1.add(var2);
      SpringUtilities.makeCompactGrid(var1, 2, 2, 10, 10, 5, 5);
      JPanel var3 = new JPanel();
      var3.setLayout(new BoxLayout(var3, 0));
      JButton var4 = new JButton();
      var4.setText("OK");
      var4.setActionCommand("btn_ok");
      var4.addActionListener(this);
      var3.add(var4);
      var3.add(Box.createHorizontalStrut(15));
      JButton var5 = new JButton();
      var5.setText("Mégsem");
      var5.setActionCommand("btn_cancel");
      var5.addActionListener(this);
      var3.add(var5);
      this.add(Box.createVerticalStrut(5));
      this.add(var1);
      this.add(Box.createVerticalStrut(5));
      this.add(var3);
   }
}
