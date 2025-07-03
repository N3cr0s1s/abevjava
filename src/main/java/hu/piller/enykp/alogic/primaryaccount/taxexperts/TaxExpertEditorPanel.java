package hu.piller.enykp.alogic.primaryaccount.taxexperts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TaxExpertEditorPanel extends JPanel {
   public static final String COMPONENT_NAME = "name";
   public static final String COMPONENT_ID = "id";
   public static final String COMPONENT_TESTIMONIAL_NUMBER = "testimonial_number";
   public static final String COMPONENT_OK_BTN = "ok";
   public static final String COMPONENT_CANCEL_BTN = "cancel";
   private static final long serialVersionUID = 1L;
   private JPanel head_panel;
   private JPanel buttons_panel;
   private JLabel lbl_name = null;
   private JTextField txt_name = null;
   private JLabel lbl_id = null;
   private JTextField txt_id = null;
   private JLabel lbl_testimonial_number = null;
   private JTextField txt_testimonial_number = null;
   private JButton btn_ok;
   private JButton btn_cancel;
   private TaxExpertEditorBusiness editor_business;

   public TaxExpertEditorPanel() {
      this.initialize();
      this.prepare();
   }

   private void prepare() {
      this.editor_business = new TaxExpertEditorBusiness(this);
   }

   private void initialize() {
      this.setLayout(new BoxLayout(this, 1));
      this.add(this.getDataPanel(), (Object)null);
      this.add(this.getButtonsPanel(), (Object)null);
      this.add(Box.createGlue(), (Object)null);
   }

   private JPanel getButtonsPanel() {
      if (this.buttons_panel == null) {
         this.buttons_panel = new JPanel();
         this.buttons_panel.setLayout(new FlowLayout(2));
         this.buttons_panel.add(this.getOkButton(), (Object)null);
         this.buttons_panel.add(this.getCancelButton(), (Object)null);
      }

      return this.buttons_panel;
   }

   private JButton getOkButton() {
      if (this.btn_ok == null) {
         this.btn_ok = new JButton("Rendben");
      }

      return this.btn_ok;
   }

   private JButton getCancelButton() {
      if (this.btn_cancel == null) {
         this.btn_cancel = new JButton("Mégsem");
      }

      return this.btn_cancel;
   }

   private JPanel getDataPanel() {
      if (this.head_panel == null) {
         this.head_panel = new JPanel();
         GridBagConstraints var1 = new GridBagConstraints();
         var1.fill = 2;
         var1.gridy = 2;
         var1.weightx = 1.0D;
         var1.insets = new Insets(5, 5, 5, 5);
         var1.gridx = 1;
         GridBagConstraints var2 = new GridBagConstraints();
         var2.gridx = 0;
         var2.anchor = 17;
         var2.insets = new Insets(5, 5, 5, 0);
         var2.gridy = 2;
         this.lbl_testimonial_number = new JLabel();
         this.lbl_testimonial_number.setText("Bizonyítvány szám ");
         GridBagConstraints var3 = new GridBagConstraints();
         var3.fill = 2;
         var3.gridy = 1;
         var3.weightx = 1.0D;
         var3.insets = new Insets(5, 5, 0, 5);
         var3.gridx = 1;
         GridBagConstraints var4 = new GridBagConstraints();
         var4.gridx = 0;
         var4.anchor = 17;
         var4.insets = new Insets(5, 5, 0, 0);
         var4.gridy = 1;
         this.lbl_id = new JLabel();
         this.lbl_id.setText("Adóazonosító/Adószám ");
         GridBagConstraints var5 = new GridBagConstraints();
         var5.fill = 2;
         var5.gridy = 0;
         var5.weightx = 1.0D;
         var5.insets = new Insets(5, 5, 0, 5);
         var5.gridx = 1;
         GridBagConstraints var6 = new GridBagConstraints();
         var6.gridx = 0;
         var6.anchor = 17;
         var6.insets = new Insets(5, 5, 0, 0);
         var6.gridy = 0;
         this.lbl_name = new JLabel();
         this.lbl_name.setText("Név ");
         this.head_panel.setLayout(new GridBagLayout());
         this.head_panel.setSize(new Dimension(428, 131));
         this.head_panel.setBackground(new Color(152, 204, 152));
         this.head_panel.add(this.lbl_name, var6);
         this.head_panel.add(this.getTxt_name(), var5);
         this.head_panel.add(this.lbl_id, var4);
         this.head_panel.add(this.getTxt_id(), var3);
         this.head_panel.add(this.lbl_testimonial_number, var2);
         this.head_panel.add(this.getTxt_testimonial_number(), var1);
         this.head_panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
         this.head_panel.setBorder(BorderFactory.createEtchedBorder(0));
      }

      return this.head_panel;
   }

   private JTextField getTxt_name() {
      if (this.txt_name == null) {
         this.txt_name = new JTextField();
      }

      return this.txt_name;
   }

   private JTextField getTxt_id() {
      if (this.txt_id == null) {
         this.txt_id = new JTextField();
      }

      return this.txt_id;
   }

   private JTextField getTxt_testimonial_number() {
      if (this.txt_testimonial_number == null) {
         this.txt_testimonial_number = new JTextField();
      }

      return this.txt_testimonial_number;
   }

   public JComponent getEEPComponent(String var1) {
      if ("name".equalsIgnoreCase(var1)) {
         return this.txt_name;
      } else if ("id".equalsIgnoreCase(var1)) {
         return this.txt_id;
      } else if ("testimonial_number".equalsIgnoreCase(var1)) {
         return this.txt_testimonial_number;
      } else if ("ok".equalsIgnoreCase(var1)) {
         return this.btn_ok;
      } else {
         return "cancel".equalsIgnoreCase(var1) ? this.btn_cancel : null;
      }
   }

   public TaxExpertEditorBusiness getBusiness() {
      return this.editor_business;
   }
}
