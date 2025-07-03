package hu.piller.enykp.alogic.primaryaccount.companies;

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

public class CompaniesEditorHeadPanel extends JPanel implements IHeadPanel {
   public static final String COMPONENT_COMPANY_NAME = "company_name";
   public static final String COMPONENT_TAX_NUMBER = "tax_number";
   private static final long serialVersionUID = 1L;
   private JPanel head_panel = null;
   private JLabel lbl_company = null;
   private JLabel lbl_tax_number = null;
   private JTextField txt_company = null;
   private JFormattedTextField txt_tax_number = null;

   public CompaniesEditorHeadPanel() {
      this.initialize();
   }

   private void initialize() {
      if (this.head_panel == null) {
         GridBagConstraints var1 = new GridBagConstraints();
         var1.gridx = 0;
         var1.anchor = 17;
         var1.ipadx = 0;
         var1.ipady = 0;
         var1.insets = new Insets(5, 5, 5, 0);
         var1.gridy = 1;
         GridBagConstraints var2 = new GridBagConstraints();
         var2.fill = 2;
         var2.gridx = 1;
         var2.gridy = 1;
         var2.anchor = 17;
         var2.insets = new Insets(5, 5, 5, 5);
         var2.weightx = 1.0D;
         GridBagConstraints var3 = new GridBagConstraints();
         var3.fill = 2;
         var3.gridx = 1;
         var3.gridy = 0;
         var3.anchor = 17;
         var3.ipadx = 0;
         var3.ipady = 0;
         var3.insets = new Insets(5, 5, 0, 5);
         var3.weightx = 1.0D;
         this.lbl_tax_number = new JLabel();
         this.lbl_tax_number.setText("Adószám ");
         GridBagConstraints var4 = new GridBagConstraints();
         var4.gridx = 0;
         var4.ipadx = 0;
         var4.weightx = 0.0D;
         var4.anchor = 17;
         var4.ipady = 0;
         var4.insets = new Insets(5, 5, 0, 0);
         var4.fill = 0;
         var4.gridy = 0;
         this.lbl_company = new JLabel();
         this.lbl_company.setText("Társaság neve ");
         this.setLayout(new GridBagLayout());
         this.setBorder(BorderFactory.createEtchedBorder(0));
         this.setSize(new Dimension(410, 104));
         this.setBackground(new Color(152, 204, 152));
         this.add(this.lbl_company, var4);
         this.add(this.lbl_tax_number, var1);
         this.add(this.getTxt_company(), var3);
         this.add(this.getTxt_tax_number(), var2);
         this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
      }

   }

   private JTextField getTxt_company() {
      if (this.txt_company == null) {
         this.txt_company = new JTextField();
      }

      return this.txt_company;
   }

   private JTextField getTxt_tax_number() {
      if (this.txt_tax_number == null) {
         this.txt_tax_number = new JFormattedTextField();
      }

      return this.txt_tax_number;
   }

   public JComponent getHPComponent(String var1) {
      if ("company_name".equalsIgnoreCase(var1)) {
         return this.txt_company;
      } else {
         return "tax_number".equalsIgnoreCase(var1) ? this.txt_tax_number : null;
      }
   }
}
