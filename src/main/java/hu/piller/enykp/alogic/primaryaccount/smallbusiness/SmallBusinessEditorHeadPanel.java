package hu.piller.enykp.alogic.primaryaccount.smallbusiness;

import hu.piller.enykp.alogic.primaryaccount.common.IHeadPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SmallBusinessEditorHeadPanel extends JPanel implements IHeadPanel {
   public static final String COMPONENT_FIRST_NAME = "first_name";
   public static final String COMPONENT_LAST_NAME = "last_name";
   public static final String COMPONENT_TAX_NUMBER = "tax_number";
   public static final String COMPONENT_TAX_ID = "tax_id";
   private static final long serialVersionUID = 1L;
   private JLabel lbl_first_name = null;
   private JTextField txt_first_name = null;
   private JLabel lbl_last_name = null;
   private JTextField txt_last_name = null;
   private JLabel lbl_tax_number = null;
   private JFormattedTextField txt_tax_number = null;
   private JLabel lbl_tax_id = null;
   private JFormattedTextField txt_tax_id = null;

   public SmallBusinessEditorHeadPanel() {
      this.initialize();
   }

   private void initialize() {
      GridBagConstraints var1 = new GridBagConstraints();
      var1.fill = 2;
      var1.gridy = 1;
      var1.weightx = 1.0D;
      var1.insets = new Insets(5, 5, 5, 5);
      var1.gridx = 3;
      GridBagConstraints var2 = new GridBagConstraints();
      var2.gridx = 2;
      var2.anchor = 17;
      var2.insets = new Insets(5, 5, 0, 0);
      var2.gridy = 1;
      this.lbl_tax_id = new JLabel();
      this.lbl_tax_id.setText("Adóazonosító jel ");
      GridBagConstraints var3 = new GridBagConstraints();
      var3.fill = 2;
      var3.gridy = 1;
      var3.weightx = 1.0D;
      var3.anchor = 10;
      var3.insets = new Insets(5, 5, 5, 0);
      var3.gridx = 1;
      GridBagConstraints var4 = new GridBagConstraints();
      var4.gridx = 0;
      var4.anchor = 17;
      var4.insets = new Insets(5, 5, 5, 0);
      var4.gridy = 1;
      this.lbl_tax_number = new JLabel();
      this.lbl_tax_number.setText("Adószám ");
      GridBagConstraints var5 = new GridBagConstraints();
      var5.fill = 2;
      var5.gridy = 0;
      var5.weightx = 1.0D;
      var5.insets = new Insets(5, 5, 0, 5);
      var5.gridx = 3;
      GridBagConstraints var6 = new GridBagConstraints();
      var6.gridx = 2;
      var6.anchor = 17;
      var6.insets = new Insets(5, 5, 0, 0);
      var6.gridy = 0;
      this.lbl_last_name = new JLabel();
      this.lbl_last_name.setText("Keresztnév ");
      GridBagConstraints var7 = new GridBagConstraints();
      var7.fill = 2;
      var7.gridy = 0;
      var7.weightx = 1.0D;
      var7.insets = new Insets(5, 5, 0, 0);
      var7.gridx = 1;
      GridBagConstraints var8 = new GridBagConstraints();
      var8.gridx = 0;
      var8.anchor = 17;
      var8.insets = new Insets(5, 5, 0, 0);
      var8.gridy = 0;
      this.lbl_first_name = new JLabel();
      this.lbl_first_name.setText("Vezetéknév ");
      this.setLayout(new GridBagLayout());
      this.setBorder(BorderFactory.createEtchedBorder(0));
      this.setSize(new Dimension(565, 122));
      this.setBackground(new Color(152, 204, 152));
      this.add(this.lbl_first_name, var8);
      this.add(this.getTxt_first_name(), var7);
      this.add(this.lbl_last_name, var6);
      this.add(this.getTxt_last_name(), var5);
      this.add(this.lbl_tax_number, var4);
      this.add(this.getTxt_tax_number(), var3);
      this.add(this.lbl_tax_id, var2);
      this.add(this.getTxt_tax_id(), var1);
      this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
   }

   private JTextField getTxt_first_name() {
      if (this.txt_first_name == null) {
         this.txt_first_name = new JTextField();
      }

      return this.txt_first_name;
   }

   private JTextField getTxt_last_name() {
      if (this.txt_last_name == null) {
         this.txt_last_name = new JTextField();
      }

      return this.txt_last_name;
   }

   private JTextField getTxt_tax_number() {
      if (this.txt_tax_number == null) {
         this.txt_tax_number = new JFormattedTextField();
      }

      return this.txt_tax_number;
   }

   private JTextField getTxt_tax_id() {
      if (this.txt_tax_id == null) {
         this.txt_tax_id = new JFormattedTextField();
      }

      return this.txt_tax_id;
   }

   public JComponent getHPComponent(String var1) {
      if ("first_name".equalsIgnoreCase(var1)) {
         return this.txt_first_name;
      } else if ("last_name".equalsIgnoreCase(var1)) {
         return this.txt_last_name;
      } else if ("tax_number".equalsIgnoreCase(var1)) {
         return this.txt_tax_number;
      } else {
         return "tax_id".equalsIgnoreCase(var1) ? this.txt_tax_id : null;
      }
   }
}
