package hu.piller.enykp.alogic.primaryaccount.common.envelope;

import hu.piller.enykp.gui.GuiUtil;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddressPanel extends JPanel {
   public static final String COMPONENT_NAME = "name";
   public static final String COMPONENT_SETTLEMENT = "settlement";
   public static final String COMPONENT_STREET = "street";
   public static final String COMPONENT_ZIP = "zip";
   public static final String COMPONENT_TITLE_L = "title_l";
   public static final String COMPONENT_NAME_L = "name_l";
   public static final String COMPONENT_SETTLEMENT_L = "settlement_l";
   public static final String COMPONENT_STREET_L = "street_l";
   public static final String COMPONENT_ZIP_L = "zip_l";
   private static final long serialVersionUID = 1L;
   private JTextField txt_name = null;
   private JTextField txt_settlement = null;
   private JTextField txt_public_place = null;
   private JTextField txt_zip = null;
   private JLabel lbl_name = null;
   private JLabel lbl_settlement = null;
   private JLabel lbl_public_place = null;
   private JLabel lbl_zip = null;
   private JLabel lbl_title = null;

   public AddressPanel() {
      this.initialize();
   }

   private void initialize() {
      GridBagConstraints var1 = new GridBagConstraints();
      var1.gridx = 0;
      var1.anchor = 17;
      var1.fill = 2;
      var1.gridwidth = 2;
      var1.gridy = 0;
      GridBagConstraints var2 = new GridBagConstraints();
      var2.fill = 2;
      var2.gridx = 1;
      var2.gridy = 2;
      var2.ipadx = 0;
      var2.ipady = 0;
      var2.weightx = 1.0D;
      var2.anchor = 17;
      var2.insets = new Insets(5, 5, 0, 5);
      GridBagConstraints var3 = new GridBagConstraints();
      var3.fill = 2;
      var3.gridy = 1;
      var3.ipadx = 0;
      var3.ipady = 0;
      var3.weightx = 1.0D;
      var3.anchor = 17;
      var3.insets = new Insets(5, 5, 0, 5);
      var3.gridx = 1;
      GridBagConstraints var4 = new GridBagConstraints();
      var4.insets = new Insets(5, 5, 0, 0);
      var4.gridy = 2;
      var4.anchor = 17;
      var4.gridx = 0;
      GridBagConstraints var5 = new GridBagConstraints();
      var5.insets = new Insets(5, 5, 5, 0);
      var5.gridy = 4;
      var5.anchor = 17;
      var5.gridx = 0;
      GridBagConstraints var6 = new GridBagConstraints();
      var6.insets = new Insets(5, 5, 0, 0);
      var6.gridy = 3;
      var6.anchor = 17;
      var6.gridx = 0;
      GridBagConstraints var7 = new GridBagConstraints();
      var7.insets = new Insets(5, 5, 0, 0);
      var7.gridy = 1;
      var7.anchor = 17;
      var7.gridx = 0;
      GridBagConstraints var8 = new GridBagConstraints();
      var8.fill = 2;
      var8.gridy = 4;
      var8.ipadx = 0;
      var8.ipady = 0;
      var8.weightx = 1.0D;
      var8.insets = new Insets(5, 5, 5, 5);
      var8.anchor = 17;
      var8.gridx = 1;
      GridBagConstraints var9 = new GridBagConstraints();
      var9.fill = 2;
      var9.gridy = 3;
      var9.ipadx = 0;
      var9.ipady = 0;
      var9.weightx = 1.0D;
      var9.insets = new Insets(5, 5, 0, 5);
      var9.anchor = 17;
      var9.gridx = 1;
      this.lbl_zip = new JLabel();
      this.lbl_zip.setText("Irányítószám");
      this.lbl_public_place = new JLabel();
      this.lbl_public_place.setText("Cím");
      this.lbl_settlement = new JLabel();
      this.lbl_settlement.setText("Város");
      this.lbl_name = new JLabel();
      this.lbl_name.setText("Név");
      this.setLayout(new GridBagLayout());
      this.setSize(new Dimension(633, 183));
      this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
      this.add(this.getLbl_title(), var1);
      this.add(this.lbl_name, var7);
      this.add(this.getTxt_name(), var3);
      this.add(this.lbl_settlement, var4);
      this.add(this.getTxt_settlement(), var2);
      this.add(this.lbl_public_place, var6);
      this.add(this.getTxt_public_place(), var9);
      this.add(this.lbl_zip, var5);
      this.add(this.getTxt_zip(), var8);
   }

   private JTextField getTxt_name() {
      if (this.txt_name == null) {
         this.txt_name = new JTextField();
         this.txt_name.setAlignmentX(0.0F);
         this.txt_name.setEditable(false);
      }

      return this.txt_name;
   }

   private JTextField getTxt_settlement() {
      if (this.txt_settlement == null) {
         this.txt_settlement = new JTextField();
         this.txt_settlement.setEditable(false);
      }

      return this.txt_settlement;
   }

   private JTextField getTxt_public_place() {
      if (this.txt_public_place == null) {
         this.txt_public_place = new JTextField();
         this.txt_public_place.setEditable(false);
      }

      return this.txt_public_place;
   }

   private JTextField getTxt_zip() {
      if (this.txt_zip == null) {
         this.txt_zip = new JTextField();
         this.txt_zip.setEditable(false);
      }

      return this.txt_zip;
   }

   private JLabel getLbl_title() {
      if (this.lbl_title == null) {
         this.lbl_title = new JLabel();
         this.lbl_title.setText("(Fejléc)");
         this.lbl_title.setOpaque(true);
         this.lbl_title.setBackground(GuiUtil.getModifiedBGColor());
         this.lbl_title.setFont(new Font("Dialog", 2, 18));
      }

      return this.lbl_title;
   }

   public JComponent getAComponent(String var1) {
      if ("name".equalsIgnoreCase(var1)) {
         return this.txt_name;
      } else if ("settlement".equalsIgnoreCase(var1)) {
         return this.txt_settlement;
      } else if ("street".equalsIgnoreCase(var1)) {
         return this.txt_public_place;
      } else if ("zip".equalsIgnoreCase(var1)) {
         return this.txt_zip;
      } else if ("title_l".equalsIgnoreCase(var1)) {
         return this.lbl_title;
      } else if ("name_l".equalsIgnoreCase(var1)) {
         return this.lbl_name;
      } else if ("settlement_l".equalsIgnoreCase(var1)) {
         return this.lbl_settlement;
      } else if ("street_l".equalsIgnoreCase(var1)) {
         return this.lbl_public_place;
      } else {
         return "zip_l".equalsIgnoreCase(var1) ? this.lbl_zip : null;
      }
   }
}
