package hu.piller.enykp.alogic.primaryaccount.common;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.component.filtercombo.ENYKFilterComboPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EditorPanel extends JPanel {
   public static final String COMPONENT_STABLE_SETTLEMENT = "s_settlement";
   public static final String COMPONENT_STABLE_PUBLIC_PLACE = "s_public_place";
   public static final String COMPONENT_STABLE_PUBLIC_PLACE_TYPE = "s_public_place_type";
   public static final String COMPONENT_STABLE_PUBLIC_PLACE_TYPE_2 = "s_public_place_type_2";
   public static final String COMPONENT_STABLE_HOUSE_NUMBER = "s_house_number";
   public static final String COMPONENT_STABLE_BUILDING = "s_building";
   public static final String COMPONENT_STABLE_STAIRCASE = "s_staircase";
   public static final String COMPONENT_STABLE_LEVEL = "s_level";
   public static final String COMPONENT_STABLE_DOOR = "s_door";
   public static final String COMPONENT_STABLE_ZIP = "s_zip";
   public static final String COMPONENT_MAIL_SETTLEMENT = "m_settlement";
   public static final String COMPONENT_MAIL_PUBLIC_PLACE = "m_public_place";
   public static final String COMPONENT_MAIL_PUBLIC_PLACE_TYPE = "m_public_place_type";
   public static final String COMPONENT_MAIL_PUBLIC_PLACE_TYPE_2 = "m_public_place_type_2";
   public static final String COMPONENT_MAIL_HOUSE_NUMBER = "m_house_number";
   public static final String COMPONENT_MAIL_BUILDING = "m_building";
   public static final String COMPONENT_MAIL_STAIRCASE = "m_staircase";
   public static final String COMPONENT_MAIL_LEVEL = "m_level";
   public static final String COMPONENT_MAIL_DOOR = "m_door";
   public static final String COMPONENT_MAIL_ZIP = "m_zip";
   public static final String COMPONENT_ADMINISTRATOR = "administrator";
   public static final String COMPONENT_TEL = "tel";
   public static final String COMPONENT_FINANCIAL_CORP = "financial_corp";
   public static final String COMPONENT_ACCOUNT_NUMBER = "account_number";
   public static final String COMPONENT_OK_BTN = "ok";
   public static final String COMPONENT_CANCEL_BTN = "cancel";
   private static final long serialVersionUID = 1L;
   private JPanel data_panel = null;
   private JPanel head_panel = null;
   private JPanel stable_address_panel = null;
   private JPanel other_panel = null;
   private JLabel lbl_company = null;
   private JLabel lbl_tax_number = null;
   private JTextField txt_company = null;
   private JFormattedTextField txt_tax_number = null;
   private JLabel lbl_zip_code = null;
   private JFormattedTextField txt_zip_code = null;
   private JLabel lbl_settlement = null;
   private JTextField txt_settlement = null;
   private JLabel lbl_public_place = null;
   private JTextField txt_public_place = null;
   private JComboBox cbo_public_place_type = null;
   private ENYKFilterComboPanel efc_public_place_type = null;
   private JLabel lbl_house_number = null;
   private JTextField txt_house_number = null;
   private JLabel lbl_building = null;
   private JTextField txt_building = null;
   private JLabel lbl_stairs = null;
   private JTextField txt_stairs = null;
   private JLabel lbl_level = null;
   private JTextField txt_level = null;
   private JLabel lbl_door = null;
   private JTextField txt_door = null;
   private JPanel mail_address_panel = null;
   private JLabel lbl_zip_code1 = null;
   private JFormattedTextField txt_zip_code1 = null;
   private JLabel lbl_settlement1 = null;
   private JTextField txt_settlement1 = null;
   private JLabel lbl_public_place1 = null;
   private JTextField txt_public_place1 = null;
   private JComboBox cbo_public_place_type1 = null;
   private ENYKFilterComboPanel efc_public_place_type1 = null;
   private JLabel lbl_house_number1 = null;
   private JTextField txt_house_number1 = null;
   private JLabel lbl_building1 = null;
   private JTextField txt_building1 = null;
   private JLabel lbl_stairs1 = null;
   private JTextField txt_stairs1 = null;
   private JLabel lbl_level1 = null;
   private JTextField txt_level1 = null;
   private JLabel lbl_door1 = null;
   private JTextField txt_door1 = null;
   private JLabel lbl_administrator = null;
   private JTextField txt_administrator = null;
   private JLabel lbl_tel = null;
   private JTextField txt_tel = null;
   private JLabel lbl_financial_corp = null;
   private JTextField txt_financial_corp = null;
   private JLabel lbl_account_number = null;
   private JFormattedTextField txt_account_number = null;
   private JPanel button_panel = null;
   private JButton btn_ok = null;
   private JButton btn_cancel = null;
   private JLabel lbl_stable_address_title = null;
   private JLabel lbl_mail_address_title = null;
   private JLabel lbl_others_title = null;

   public EditorPanel(JPanel var1) {
      this.head_panel = var1;
      this.initialize();
   }

   private void initialize() {
      this.setLayout(new BoxLayout(this, 1));
      this.setSize(new Dimension(730, 469));
      this.setBackground(new Color(204, 204, 204));
      this.add(this.getData_panel(), (Object)null);
      this.add(this.getButton_panel(), (Object)null);
      this.add(Box.createGlue(), (Object)null);
   }

   private JPanel getData_panel() {
      if (this.data_panel == null) {
         this.data_panel = new JPanel();
         this.data_panel.setLayout(new BoxLayout(this.getData_panel(), 1));
         this.data_panel.add(this.getHead_panel(), (Object)null);
         this.data_panel.add(Box.createRigidArea(new Dimension(0, 5)), (Object)null);
         this.data_panel.add(this.getStable_address_panel(), (Object)null);
         this.data_panel.add(Box.createRigidArea(new Dimension(0, 5)), (Object)null);
         this.data_panel.add(this.getMail_address_panel(), (Object)null);
         this.data_panel.add(Box.createRigidArea(new Dimension(0, 5)), (Object)null);
         this.data_panel.add(this.getOther_panel(), (Object)null);
      }

      return this.data_panel;
   }

   private JPanel getHead_panel() {
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
         this.head_panel = new JPanel();
         this.head_panel.setLayout(new GridBagLayout());
         this.head_panel.setBorder(BorderFactory.createEtchedBorder(0));
         this.head_panel.setBackground(new Color(152, 204, 152));
         this.head_panel.add(this.lbl_company, var4);
         this.head_panel.add(this.lbl_tax_number, var1);
         this.head_panel.add(this.getTxt_company(), var3);
         this.head_panel.add(this.getTxt_tax_number(), var2);
         this.head_panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
      }

      return this.head_panel;
   }

   private JPanel getStable_address_panel() {
      if (this.stable_address_panel == null) {
         GridBagConstraints var1 = new GridBagConstraints();
         var1.gridx = 0;
         var1.anchor = 18;
         var1.gridwidth = 14;
         var1.fill = 2;
         var1.gridy = 0;
         this.lbl_stable_address_title = new JLabel();
         this.lbl_stable_address_title.setOpaque(true);
         this.lbl_stable_address_title.setText("Állandó cím");
         this.lbl_stable_address_title.setBackground(GuiUtil.getModifiedBGColor());
         this.lbl_stable_address_title.setFont(new Font("Dialog", 3, 18));
         GridBagConstraints var2 = new GridBagConstraints();
         var2.gridx = 12;
         var2.insets = new Insets(5, 5, 0, 0);
         var2.gridy = 2;
         GridBagConstraints var3 = new GridBagConstraints();
         var3.fill = 2;
         var3.gridx = 13;
         var3.gridy = 2;
         var3.insets = new Insets(5, 5, 0, 5);
         this.lbl_door = new JLabel();
         this.lbl_door.setText("Ajtó ");
         this.lbl_door.setMinimumSize(new Dimension(30, 20));
         GridBagConstraints var4 = new GridBagConstraints();
         var4.gridx = 10;
         var4.insets = new Insets(5, 5, 0, 0);
         var4.gridy = 2;
         GridBagConstraints var5 = new GridBagConstraints();
         var5.fill = 2;
         var5.gridx = 11;
         var5.gridy = 2;
         var5.insets = new Insets(5, 5, 0, 0);
         this.lbl_level = new JLabel();
         this.lbl_level.setText("Emelet ");
         this.lbl_level.setMinimumSize(new Dimension(30, 20));
         GridBagConstraints var6 = new GridBagConstraints();
         var6.gridx = 8;
         var6.insets = new Insets(5, 5, 0, 0);
         var6.gridy = 2;
         GridBagConstraints var7 = new GridBagConstraints();
         var7.fill = 2;
         var7.gridx = 9;
         var7.gridy = 2;
         var7.insets = new Insets(5, 5, 0, 0);
         this.lbl_stairs = new JLabel();
         this.lbl_stairs.setText("Lépcsőház ");
         this.lbl_stairs.setMinimumSize(new Dimension(30, 20));
         GridBagConstraints var8 = new GridBagConstraints();
         var8.gridx = 6;
         var8.insets = new Insets(5, 5, 0, 0);
         var8.gridy = 2;
         GridBagConstraints var9 = new GridBagConstraints();
         var9.fill = 2;
         var9.gridx = 7;
         var9.gridy = 2;
         var9.insets = new Insets(5, 5, 0, 0);
         this.lbl_building = new JLabel();
         this.lbl_building.setText("Épület ");
         this.lbl_building.setMinimumSize(new Dimension(30, 20));
         GridBagConstraints var10 = new GridBagConstraints();
         var10.gridx = 4;
         var10.insets = new Insets(5, 5, 0, 0);
         var10.gridy = 2;
         GridBagConstraints var11 = new GridBagConstraints();
         var11.fill = 2;
         var11.gridx = 5;
         var11.gridy = 2;
         var11.insets = new Insets(5, 5, 0, 0);
         this.lbl_house_number = new JLabel();
         this.lbl_house_number.setText("Házszám ");
         this.lbl_house_number.setMinimumSize(new Dimension(30, 20));
         GridBagConstraints var12 = new GridBagConstraints();
         var12.fill = 2;
         var12.gridx = 3;
         var12.gridy = 2;
         var12.insets = new Insets(5, 5, 0, 0);
         var12.weightx = 1.0D;
         GridBagConstraints var13 = new GridBagConstraints();
         var13.fill = 2;
         var13.gridx = 1;
         var13.gridy = 2;
         var13.gridwidth = 2;
         var13.insets = new Insets(5, 5, 0, 0);
         var13.anchor = 10;
         var13.weightx = 1.0D;
         GridBagConstraints var14 = new GridBagConstraints();
         var14.gridx = 0;
         var14.anchor = 17;
         var14.insets = new Insets(5, 5, 0, 0);
         var14.gridy = 2;
         this.lbl_public_place = new JLabel();
         this.lbl_public_place.setText("Közterület ");
         GridBagConstraints var15 = new GridBagConstraints();
         var15.gridx = 0;
         var15.anchor = 17;
         var15.insets = new Insets(5, 5, 0, 0);
         var15.gridy = 1;
         GridBagConstraints var16 = new GridBagConstraints();
         var16.gridx = 0;
         var16.insets = new Insets(5, 5, 5, 0);
         var16.anchor = 17;
         var16.gridy = 4;
         GridBagConstraints var17 = new GridBagConstraints();
         var17.fill = 2;
         var17.gridx = 1;
         var17.gridy = 1;
         var17.gridwidth = 4;
         var17.insets = new Insets(5, 5, 0, 0);
         var17.anchor = 10;
         this.lbl_settlement = new JLabel();
         this.lbl_settlement.setText("Település ");
         GridBagConstraints var18 = new GridBagConstraints();
         var18.fill = 2;
         var18.gridx = 1;
         var18.gridy = 4;
         var18.insets = new Insets(5, 5, 5, 0);
         var18.anchor = 10;
         this.lbl_zip_code = new JLabel();
         this.lbl_zip_code.setText("Irányítószám ");
         this.stable_address_panel = new JPanel();
         this.stable_address_panel.setLayout(new GridBagLayout());
         this.stable_address_panel.setBorder(BorderFactory.createEtchedBorder(0));
         this.stable_address_panel.add(this.lbl_zip_code, var16);
         this.stable_address_panel.add(this.getTxt_zip_code(), var18);
         this.stable_address_panel.add(this.lbl_settlement, var15);
         this.stable_address_panel.add(this.getTxt_settlement(), var17);
         this.stable_address_panel.add(this.lbl_public_place, var14);
         this.stable_address_panel.add(this.getTxt_public_place(), var13);
         this.stable_address_panel.add(this.getEfc_public_place_type(), var12);
         this.stable_address_panel.add(this.lbl_house_number, var10);
         this.stable_address_panel.add(this.getTxt_house_number(), var11);
         this.stable_address_panel.add(this.lbl_building, var8);
         this.stable_address_panel.add(this.getTxt_building(), var9);
         this.stable_address_panel.add(this.lbl_stairs, var6);
         this.stable_address_panel.add(this.getTxt_stairs(), var7);
         this.stable_address_panel.add(this.lbl_level, var4);
         this.stable_address_panel.add(this.getTxt_level(), var5);
         this.stable_address_panel.add(this.lbl_door, var2);
         this.stable_address_panel.add(this.getTxt_door(), var3);
         this.stable_address_panel.add(this.lbl_stable_address_title, var1);
         this.stable_address_panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
      }

      return this.stable_address_panel;
   }

   private JPanel getOther_panel() {
      if (this.other_panel == null) {
         GridBagConstraints var1 = new GridBagConstraints();
         var1.gridx = 0;
         var1.fill = 2;
         var1.anchor = 18;
         var1.gridwidth = 14;
         var1.gridy = 0;
         this.lbl_others_title = new JLabel();
         this.lbl_others_title.setOpaque(true);
         this.lbl_others_title.setText("Egyéb adatok");
         this.lbl_others_title.setBackground(GuiUtil.getModifiedBGColor());
         this.lbl_others_title.setFont(new Font("Dialog", 3, 18));
         GridBagConstraints var2 = new GridBagConstraints();
         var2.gridx = 0;
         var2.anchor = 17;
         var2.insets = new Insets(5, 5, 5, 0);
         var2.gridy = 3;
         GridBagConstraints var3 = new GridBagConstraints();
         var3.fill = 2;
         var3.gridx = 1;
         var3.gridy = 3;
         var3.insets = new Insets(5, 5, 5, 0);
         var3.weightx = 1.0D;
         this.lbl_account_number = new JLabel();
         this.lbl_account_number.setText("Számlaszám ");
         GridBagConstraints var4 = new GridBagConstraints();
         var4.gridx = 0;
         var4.anchor = 17;
         var4.insets = new Insets(5, 5, 0, 0);
         var4.gridy = 2;
         GridBagConstraints var5 = new GridBagConstraints();
         var5.fill = 2;
         var5.gridx = 1;
         var5.gridy = 2;
         var5.insets = new Insets(5, 5, 0, 0);
         var5.weightx = 1.0D;
         this.lbl_financial_corp = new JLabel();
         this.lbl_financial_corp.setText("Pénzintézet ");
         GridBagConstraints var6 = new GridBagConstraints();
         var6.gridx = 2;
         var6.anchor = 17;
         var6.insets = new Insets(5, 5, 0, 0);
         var6.gridy = 1;
         GridBagConstraints var7 = new GridBagConstraints();
         var7.gridx = 0;
         var7.anchor = 17;
         var7.insets = new Insets(5, 5, 0, 0);
         var7.gridy = 1;
         GridBagConstraints var8 = new GridBagConstraints();
         var8.fill = 2;
         var8.gridx = 4;
         var8.gridy = 1;
         var8.insets = new Insets(5, 5, 0, 5);
         var8.weightx = 1.0D;
         this.lbl_tel = new JLabel();
         this.lbl_tel.setText("Tel ");
         GridBagConstraints var9 = new GridBagConstraints();
         var9.fill = 2;
         var9.gridx = 1;
         var9.gridy = 1;
         var9.insets = new Insets(5, 5, 0, 0);
         var9.weightx = 1.0D;
         this.lbl_administrator = new JLabel();
         this.lbl_administrator.setText("Ügyintéző ");
         this.other_panel = new JPanel();
         this.other_panel.setLayout(new GridBagLayout());
         this.other_panel.setBorder(BorderFactory.createEtchedBorder(0));
         this.other_panel.add(this.lbl_administrator, var7);
         this.other_panel.add(this.getTxt_administrator(), var9);
         this.other_panel.add(this.lbl_tel, var6);
         this.other_panel.add(this.getTxt_tel(), var8);
         this.other_panel.add(this.lbl_financial_corp, var4);
         this.other_panel.add(this.getTxt_financial_corp(), var5);
         this.other_panel.add(this.lbl_account_number, var2);
         this.other_panel.add(this.getTxt_account_number(), var3);
         this.other_panel.add(this.lbl_others_title, var1);
         this.other_panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
      }

      return this.other_panel;
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

   private JTextField getTxt_zip_code() {
      if (this.txt_zip_code == null) {
         this.txt_zip_code = new JFormattedTextField();
         this.txt_zip_code.setMinimumSize(new Dimension(60, 20));
         this.txt_zip_code.setPreferredSize(new Dimension(60, 20));
      }

      return this.txt_zip_code;
   }

   private JTextField getTxt_settlement() {
      if (this.txt_settlement == null) {
         this.txt_settlement = new JTextField();
         this.txt_settlement.setMinimumSize(new Dimension(70, 20));
         this.txt_settlement.setPreferredSize(new Dimension(70, 20));
      }

      return this.txt_settlement;
   }

   private JTextField getTxt_public_place() {
      if (this.txt_public_place == null) {
         this.txt_public_place = new JTextField();
         this.txt_public_place.setMinimumSize(new Dimension(40, 20));
         this.txt_public_place.setPreferredSize(new Dimension(130, 20));
      }

      return this.txt_public_place;
   }

   private JComboBox getCbo_public_place_type() {
      if (this.cbo_public_place_type == null) {
         this.cbo_public_place_type = new JComboBox();
         this.cbo_public_place_type.setMinimumSize(new Dimension(40, 20));
      }

      return this.cbo_public_place_type;
   }

   private ENYKFilterComboPanel getEfc_public_place_type() {
      if (this.efc_public_place_type == null) {
         this.efc_public_place_type = new ENYKFilterComboPanel();
         this.efc_public_place_type.setMinimumSize(new Dimension(40, 20));
      }

      return this.efc_public_place_type;
   }

   private JTextField getTxt_house_number() {
      if (this.txt_house_number == null) {
         this.txt_house_number = new JTextField();
         this.txt_house_number.setMinimumSize(new Dimension(30, 20));
         this.txt_house_number.setPreferredSize(new Dimension(40, 20));
      }

      return this.txt_house_number;
   }

   private JTextField getTxt_building() {
      if (this.txt_building == null) {
         this.txt_building = new JTextField();
         this.txt_building.setMinimumSize(new Dimension(30, 20));
         this.txt_building.setPreferredSize(new Dimension(40, 20));
      }

      return this.txt_building;
   }

   private JTextField getTxt_stairs() {
      if (this.txt_stairs == null) {
         this.txt_stairs = new JTextField();
         this.txt_stairs.setMinimumSize(new Dimension(30, 20));
         this.txt_stairs.setPreferredSize(new Dimension(40, 20));
      }

      return this.txt_stairs;
   }

   private JTextField getTxt_level() {
      if (this.txt_level == null) {
         this.txt_level = new JTextField();
         this.txt_level.setMinimumSize(new Dimension(30, 20));
         this.txt_level.setPreferredSize(new Dimension(40, 20));
      }

      return this.txt_level;
   }

   private JTextField getTxt_door() {
      if (this.txt_door == null) {
         this.txt_door = new JTextField();
         this.txt_door.setMinimumSize(new Dimension(30, 20));
         this.txt_door.setPreferredSize(new Dimension(40, 20));
      }

      return this.txt_door;
   }

   private JPanel getMail_address_panel() {
      if (this.mail_address_panel == null) {
         GridBagConstraints var1 = new GridBagConstraints();
         var1.gridx = 0;
         var1.anchor = 18;
         var1.gridwidth = 14;
         var1.fill = 2;
         var1.gridy = 0;
         this.lbl_mail_address_title = new JLabel();
         this.lbl_mail_address_title.setOpaque(true);
         this.lbl_mail_address_title.setText("Levelezési cím");
         this.lbl_mail_address_title.setBackground(GuiUtil.getModifiedBGColor());
         this.lbl_mail_address_title.setFont(new Font("Dialog", 3, 18));
         GridBagConstraints var2 = new GridBagConstraints();
         var2.fill = 2;
         var2.gridy = 2;
         var2.insets = new Insets(5, 5, 0, 5);
         var2.gridx = 13;
         GridBagConstraints var3 = new GridBagConstraints();
         var3.gridx = 12;
         var3.insets = new Insets(5, 5, 0, 0);
         var3.gridy = 2;
         this.lbl_door1 = new JLabel();
         this.lbl_door1.setText("Ajtó ");
         this.lbl_door1.setMinimumSize(new Dimension(30, 20));
         GridBagConstraints var4 = new GridBagConstraints();
         var4.fill = 2;
         var4.gridy = 2;
         var4.insets = new Insets(5, 5, 0, 0);
         var4.gridx = 11;
         GridBagConstraints var5 = new GridBagConstraints();
         var5.gridx = 10;
         var5.insets = new Insets(5, 5, 0, 0);
         var5.gridy = 2;
         this.lbl_level1 = new JLabel();
         this.lbl_level1.setText("Emelet ");
         this.lbl_level1.setMinimumSize(new Dimension(30, 20));
         GridBagConstraints var6 = new GridBagConstraints();
         var6.fill = 2;
         var6.gridy = 2;
         var6.insets = new Insets(5, 5, 0, 0);
         var6.gridx = 9;
         GridBagConstraints var7 = new GridBagConstraints();
         var7.gridx = 8;
         var7.insets = new Insets(5, 5, 0, 0);
         var7.gridy = 2;
         this.lbl_stairs1 = new JLabel();
         this.lbl_stairs1.setText("Lépcsőház ");
         this.lbl_stairs1.setMinimumSize(new Dimension(30, 20));
         GridBagConstraints var8 = new GridBagConstraints();
         var8.fill = 2;
         var8.gridy = 2;
         var8.insets = new Insets(5, 5, 0, 0);
         var8.gridx = 7;
         GridBagConstraints var9 = new GridBagConstraints();
         var9.gridx = 6;
         var9.insets = new Insets(5, 5, 0, 0);
         var9.gridy = 2;
         this.lbl_building1 = new JLabel();
         this.lbl_building1.setText("Épület ");
         this.lbl_building1.setMinimumSize(new Dimension(30, 20));
         GridBagConstraints var10 = new GridBagConstraints();
         var10.fill = 2;
         var10.gridy = 2;
         var10.insets = new Insets(5, 5, 0, 0);
         var10.gridx = 5;
         GridBagConstraints var11 = new GridBagConstraints();
         var11.gridx = 4;
         var11.insets = new Insets(5, 5, 0, 0);
         var11.gridy = 2;
         this.lbl_house_number1 = new JLabel();
         this.lbl_house_number1.setText("Házszám ");
         this.lbl_house_number1.setMinimumSize(new Dimension(30, 20));
         GridBagConstraints var12 = new GridBagConstraints();
         var12.fill = 2;
         var12.gridy = 2;
         var12.weightx = 1.0D;
         var12.insets = new Insets(5, 5, 0, 0);
         var12.gridx = 3;
         GridBagConstraints var13 = new GridBagConstraints();
         var13.fill = 2;
         var13.gridx = 1;
         var13.gridy = 2;
         var13.weightx = 1.0D;
         var13.insets = new Insets(5, 5, 0, 0);
         var13.gridwidth = 2;
         GridBagConstraints var14 = new GridBagConstraints();
         var14.anchor = 17;
         var14.gridy = 2;
         var14.insets = new Insets(5, 5, 0, 0);
         var14.gridwidth = 1;
         var14.gridx = 0;
         this.lbl_public_place1 = new JLabel();
         this.lbl_public_place1.setText("Közterület ");
         GridBagConstraints var15 = new GridBagConstraints();
         var15.fill = 2;
         var15.gridx = 1;
         var15.gridy = 1;
         var15.insets = new Insets(5, 5, 0, 0);
         var15.gridwidth = 4;
         GridBagConstraints var16 = new GridBagConstraints();
         var16.anchor = 17;
         var16.gridy = 1;
         var16.insets = new Insets(5, 5, 0, 0);
         var16.gridx = 0;
         this.lbl_settlement1 = new JLabel();
         this.lbl_settlement1.setText("Település ");
         GridBagConstraints var17 = new GridBagConstraints();
         var17.fill = 2;
         var17.gridy = 4;
         var17.insets = new Insets(5, 5, 5, 0);
         var17.gridx = 1;
         GridBagConstraints var18 = new GridBagConstraints();
         var18.gridx = 0;
         var18.gridy = 4;
         var18.insets = new Insets(5, 5, 0, 5);
         var18.anchor = 17;
         this.lbl_zip_code1 = new JLabel();
         this.lbl_zip_code1.setText("Irányítószám ");
         this.mail_address_panel = new JPanel();
         this.mail_address_panel.setLayout(new GridBagLayout());
         this.mail_address_panel.setBorder(BorderFactory.createEtchedBorder(0));
         this.mail_address_panel.add(this.lbl_zip_code1, var18);
         this.mail_address_panel.add(this.getTxt_zip_code1(), var17);
         this.mail_address_panel.add(this.lbl_settlement1, var16);
         this.mail_address_panel.add(this.getTxt_settlement1(), var15);
         this.mail_address_panel.add(this.lbl_public_place1, var14);
         this.mail_address_panel.add(this.getTxt_public_place1(), var13);
         this.mail_address_panel.add(this.getEfc_public_place_type1(), var12);
         this.mail_address_panel.add(this.lbl_house_number1, var11);
         this.mail_address_panel.add(this.getTxt_house_number1(), var10);
         this.mail_address_panel.add(this.lbl_building1, var9);
         this.mail_address_panel.add(this.getTxt_building1(), var8);
         this.mail_address_panel.add(this.lbl_stairs1, var7);
         this.mail_address_panel.add(this.getTxt_stairs1(), var6);
         this.mail_address_panel.add(this.lbl_level1, var5);
         this.mail_address_panel.add(this.getTxt_level1(), var4);
         this.mail_address_panel.add(this.lbl_door1, var3);
         this.mail_address_panel.add(this.getTxt_door1(), var2);
         this.mail_address_panel.add(this.lbl_mail_address_title, var1);
         this.mail_address_panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
      }

      return this.mail_address_panel;
   }

   private JTextField getTxt_zip_code1() {
      if (this.txt_zip_code1 == null) {
         this.txt_zip_code1 = new JFormattedTextField();
         this.txt_zip_code1.setMinimumSize(new Dimension(60, 20));
         this.txt_zip_code1.setPreferredSize(new Dimension(60, 20));
      }

      return this.txt_zip_code1;
   }

   private JTextField getTxt_settlement1() {
      if (this.txt_settlement1 == null) {
         this.txt_settlement1 = new JTextField();
         this.txt_settlement1.setMinimumSize(new Dimension(70, 20));
         this.txt_settlement1.setPreferredSize(new Dimension(70, 20));
      }

      return this.txt_settlement1;
   }

   private JTextField getTxt_public_place1() {
      if (this.txt_public_place1 == null) {
         this.txt_public_place1 = new JTextField();
         this.txt_public_place1.setMinimumSize(new Dimension(40, 20));
         this.txt_public_place1.setPreferredSize(new Dimension(130, 20));
      }

      return this.txt_public_place1;
   }

   private JComboBox getCbo_public_place_type1() {
      if (this.cbo_public_place_type1 == null) {
         this.cbo_public_place_type1 = new JComboBox();
         this.cbo_public_place_type1.setMinimumSize(new Dimension(40, 20));
      }

      return this.cbo_public_place_type1;
   }

   private ENYKFilterComboPanel getEfc_public_place_type1() {
      if (this.efc_public_place_type1 == null) {
         this.efc_public_place_type1 = new ENYKFilterComboPanel();
         this.efc_public_place_type1.setMinimumSize(new Dimension(40, 20));
      }

      return this.efc_public_place_type1;
   }

   private JTextField getTxt_house_number1() {
      if (this.txt_house_number1 == null) {
         this.txt_house_number1 = new JTextField();
         this.txt_house_number1.setMinimumSize(new Dimension(30, 20));
         this.txt_house_number1.setPreferredSize(new Dimension(40, 20));
      }

      return this.txt_house_number1;
   }

   private JTextField getTxt_building1() {
      if (this.txt_building1 == null) {
         this.txt_building1 = new JTextField();
         this.txt_building1.setMinimumSize(new Dimension(30, 20));
         this.txt_building1.setPreferredSize(new Dimension(40, 20));
      }

      return this.txt_building1;
   }

   private JTextField getTxt_stairs1() {
      if (this.txt_stairs1 == null) {
         this.txt_stairs1 = new JTextField();
         this.txt_stairs1.setMinimumSize(new Dimension(30, 20));
         this.txt_stairs1.setPreferredSize(new Dimension(40, 20));
      }

      return this.txt_stairs1;
   }

   private JTextField getTxt_level1() {
      if (this.txt_level1 == null) {
         this.txt_level1 = new JTextField();
         this.txt_level1.setMinimumSize(new Dimension(30, 20));
         this.txt_level1.setPreferredSize(new Dimension(40, 20));
      }

      return this.txt_level1;
   }

   private JTextField getTxt_door1() {
      if (this.txt_door1 == null) {
         this.txt_door1 = new JTextField();
         this.txt_door1.setMinimumSize(new Dimension(30, 20));
         this.txt_door1.setPreferredSize(new Dimension(40, 20));
      }

      return this.txt_door1;
   }

   private JTextField getTxt_administrator() {
      if (this.txt_administrator == null) {
         this.txt_administrator = new JTextField();
      }

      return this.txt_administrator;
   }

   private JTextField getTxt_tel() {
      if (this.txt_tel == null) {
         this.txt_tel = new JTextField();
      }

      return this.txt_tel;
   }

   private JTextField getTxt_financial_corp() {
      if (this.txt_financial_corp == null) {
         this.txt_financial_corp = new JTextField();
      }

      return this.txt_financial_corp;
   }

   private JTextField getTxt_account_number() {
      if (this.txt_account_number == null) {
         this.txt_account_number = new JFormattedTextField();
      }

      return this.txt_account_number;
   }

   private JPanel getButton_panel() {
      if (this.button_panel == null) {
         FlowLayout var1 = new FlowLayout();
         var1.setAlignment(2);
         this.button_panel = new JPanel();
         this.button_panel.setLayout(var1);
         this.button_panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
         this.button_panel.add(this.getBtn_ok(), (Object)null);
         this.button_panel.add(this.getBtn_cancel(), (Object)null);
      }

      return this.button_panel;
   }

   private JButton getBtn_ok() {
      if (this.btn_ok == null) {
         this.btn_ok = new JButton();
         this.btn_ok.setText("Rendben");
      }

      return this.btn_ok;
   }

   private JButton getBtn_cancel() {
      if (this.btn_cancel == null) {
         this.btn_cancel = new JButton();
         this.btn_cancel.setText("Mégsem");
      }

      return this.btn_cancel;
   }

   public JComponent getEPComponent(String var1) {
      if ("s_settlement".equalsIgnoreCase(var1)) {
         return this.txt_settlement;
      } else if ("s_public_place".equalsIgnoreCase(var1)) {
         return this.txt_public_place;
      } else if ("s_public_place_type".equalsIgnoreCase(var1)) {
         return this.cbo_public_place_type;
      } else if ("s_public_place_type_2".equalsIgnoreCase(var1)) {
         return this.efc_public_place_type;
      } else if ("s_house_number".equalsIgnoreCase(var1)) {
         return this.txt_house_number;
      } else if ("s_building".equalsIgnoreCase(var1)) {
         return this.txt_building;
      } else if ("s_staircase".equalsIgnoreCase(var1)) {
         return this.txt_stairs;
      } else if ("s_level".equalsIgnoreCase(var1)) {
         return this.txt_level;
      } else if ("s_door".equalsIgnoreCase(var1)) {
         return this.txt_door;
      } else if ("s_zip".equalsIgnoreCase(var1)) {
         return this.txt_zip_code;
      } else if ("m_settlement".equalsIgnoreCase(var1)) {
         return this.txt_settlement1;
      } else if ("m_public_place".equalsIgnoreCase(var1)) {
         return this.txt_public_place1;
      } else if ("m_public_place_type".equalsIgnoreCase(var1)) {
         return this.cbo_public_place_type1;
      } else if ("m_public_place_type_2".equalsIgnoreCase(var1)) {
         return this.efc_public_place_type1;
      } else if ("m_house_number".equalsIgnoreCase(var1)) {
         return this.txt_house_number1;
      } else if ("m_building".equalsIgnoreCase(var1)) {
         return this.txt_building1;
      } else if ("m_staircase".equalsIgnoreCase(var1)) {
         return this.txt_stairs1;
      } else if ("m_level".equalsIgnoreCase(var1)) {
         return this.txt_level1;
      } else if ("m_door".equalsIgnoreCase(var1)) {
         return this.txt_door1;
      } else if ("m_zip".equalsIgnoreCase(var1)) {
         return this.txt_zip_code1;
      } else if ("administrator".equalsIgnoreCase(var1)) {
         return this.txt_administrator;
      } else if ("tel".equalsIgnoreCase(var1)) {
         return this.txt_tel;
      } else if ("financial_corp".equalsIgnoreCase(var1)) {
         return this.txt_financial_corp;
      } else if ("account_number".equalsIgnoreCase(var1)) {
         return this.txt_account_number;
      } else if ("ok".equalsIgnoreCase(var1)) {
         return this.btn_ok;
      } else if ("cancel".equalsIgnoreCase(var1)) {
         return this.btn_cancel;
      } else {
         return this.head_panel instanceof IHeadPanel ? ((IHeadPanel)this.head_panel).getHPComponent(var1) : null;
      }
   }
}
