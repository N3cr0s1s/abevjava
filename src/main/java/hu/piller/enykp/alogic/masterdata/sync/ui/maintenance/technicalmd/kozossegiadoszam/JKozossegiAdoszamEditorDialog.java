package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.kozossegiadoszam;

import hu.piller.enykp.alogic.masterdata.gui.MDGUIFieldFactory;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.ITechnicalMdEditor;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.ITechnicalMdModel;
import hu.piller.enykp.gui.SpringUtilities;
import hu.piller.enykp.gui.component.filtercombo.ENYKFilterComboPanel;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class JKozossegiAdoszamEditorDialog extends JDialog implements ITechnicalMdEditor, ActionListener {
   private KozossegiAdoszamModel model;
   private ENYKFilterComboPanel countryCode;
   private JTextField taxId;
   private String oriValue;

   public JKozossegiAdoszamEditorDialog() {
      super(MainFrame.thisinstance);
      this.setModal(true);
      this.setDefaultCloseOperation(0);
      this.setTitle("Közösségi adószám adatai");
      this.setUpLayout();
   }

   public void setModel(ITechnicalMdModel var1) {
      this.oriValue = var1.getValue();
      this.model = (KozossegiAdoszamModel)KozossegiAdoszamModel.class.cast(var1);
      this.countryCode.setText(this.model.getCountryCode());
      this.taxId.setText(this.model.getTaxId());
   }

   public KozossegiAdoszamModel getModel() {
      return this.model;
   }

   public void actionPerformed(ActionEvent var1) {
      if ("btn_ok".equals(var1.getActionCommand())) {
         this.model.setCountryCode(this.countryCode.getText());
         this.model.setTaxId(this.taxId.getText());
      } else if ("btn_cancel".equals(var1.getActionCommand())) {
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
      this.taxId = new JTextField();
      this.taxId.setMinimumSize(new Dimension(250, 20));
      this.taxId.setPreferredSize(new Dimension(250, 20));
      this.taxId.setMaximumSize(new Dimension(250, 20));
      this.taxId.setToolTipText("Adószám");
      this.countryCode = (ENYKFilterComboPanel)MDGUIFieldFactory.getInstance().getInputFieldForType("TOrszagISOLista");
      this.countryCode.setMinimumSize(new Dimension(100, 20));
      this.countryCode.setPreferredSize(new Dimension(100, 20));
      this.countryCode.setMaximumSize(new Dimension(100, 20));
      this.countryCode.setToolTipText("Az ország kétjegyű kódja");
   }

   private void buildLayout() {
      this.setLayout(new BoxLayout(this.getContentPane(), 1));
      JPanel var1 = new JPanel();
      var1.setBorder(BorderFactory.createLineBorder(new Color(184, 207, 229)));
      var1.setLayout(new SpringLayout());
      var1.add(new JLabel("Országkód"));
      JPanel var2 = new JPanel();
      var2.setLayout(new BoxLayout(var2, 0));
      var2.add(this.countryCode);
      var2.add(Box.createHorizontalGlue());
      var1.add(var2);
      var1.add(new JLabel("Adószám"));
      var1.add(this.taxId);
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
