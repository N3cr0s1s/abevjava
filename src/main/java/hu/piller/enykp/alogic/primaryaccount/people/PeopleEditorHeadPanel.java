package hu.piller.enykp.alogic.primaryaccount.people;

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

public class PeopleEditorHeadPanel extends JPanel implements IHeadPanel {
   public static final String COMPONENT_FIRST_NAME = "first_name";
   public static final String COMPONENT_LAST_NAME = "last_name";
   public static final String COMPONENT_TAX_ID = "tax_id";
   private static final long serialVersionUID = 1L;
   private JLabel lbl_first_name = null;
   private JTextField txt_first_name = null;
   private JLabel lbl_last_name = null;
   private JTextField txt_last_name = null;
   private JLabel lbl_tax_id = null;
   private JFormattedTextField txt_tax_id = null;

   public PeopleEditorHeadPanel() {
      this.initialize();
   }

   private void initialize() {
      GridBagConstraints var1 = new GridBagConstraints();
      var1.fill = 2;
      var1.gridy = 1;
      var1.weightx = 1.0D;
      var1.insets = new Insets(5, 5, 5, 0);
      var1.gridx = 1;
      GridBagConstraints var2 = new GridBagConstraints();
      var2.gridx = 0;
      var2.anchor = 17;
      var2.insets = new Insets(5, 5, 5, 0);
      var2.gridy = 1;
      this.lbl_tax_id = new JLabel();
      this.lbl_tax_id.setText("Adóazonosító jel ");
      GridBagConstraints var3 = new GridBagConstraints();
      var3.fill = 2;
      var3.gridy = 0;
      var3.weightx = 1.0D;
      var3.insets = new Insets(5, 5, 0, 5);
      var3.gridx = 3;
      GridBagConstraints var4 = new GridBagConstraints();
      var4.gridx = 2;
      var4.anchor = 17;
      var4.insets = new Insets(5, 5, 0, 0);
      var4.gridy = 0;
      this.lbl_last_name = new JLabel();
      this.lbl_last_name.setText("Keresztnév ");
      GridBagConstraints var5 = new GridBagConstraints();
      var5.fill = 2;
      var5.gridy = 0;
      var5.weightx = 1.0D;
      var5.insets = new Insets(5, 5, 0, 0);
      var5.gridx = 1;
      GridBagConstraints var6 = new GridBagConstraints();
      var6.gridx = 0;
      var6.anchor = 17;
      var6.insets = new Insets(5, 5, 0, 0);
      var6.gridy = 0;
      this.lbl_first_name = new JLabel();
      this.lbl_first_name.setText("Vezetéknév ");
      this.setLayout(new GridBagLayout());
      this.setBorder(BorderFactory.createEtchedBorder(0));
      this.setSize(new Dimension(537, 170));
      this.setBackground(new Color(152, 204, 152));
      this.add(this.lbl_first_name, var6);
      this.add(this.getTxt_first_name(), var5);
      this.add(this.lbl_last_name, var4);
      this.add(this.getTxt_last_name(), var3);
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
      } else {
         return "tax_id".equalsIgnoreCase(var1) ? this.txt_tax_id : null;
      }
   }
}
