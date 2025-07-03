package hu.piller.enykp.alogic.primaryaccount.common.envelope;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.util.base.eventsupport.DefaultEventSupport;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class EnvelopePanel extends JPanel implements IEventSupport {
   public static final String COMPONENT_OK_BTN = "ok_button";
   public static final String COMPONENT_CANCEL_BTN = "cancel_button";
   public static final String COMPONENT_STABLE_BTN = "stable_button";
   public static final String COMPONENT_FIND_RECEIVER_BTN = "find_receiver_button";
   public static final String COMPONENT_MAIL_BTN = "mail_button";
   public static final String COMPONENT_TAKE_OLD_CHK = "take_old_chk";
   public static final String COMPONENT_ORGANIZATIONS_CBO = "org_cbo";
   public static final String COMPONENT_ADDRESS_CBO = "addr_cbo";
   public static final String COMPONENT_S_NAME = "s_name";
   public static final String COMPONENT_S_SETTLEMENT = "s_settlement";
   public static final String COMPONENT_S_STREET = "s_street";
   public static final String COMPONENT_S_ZIP = "s_zip";
   public static final String COMPONENT_R_NAME = "r_name";
   public static final String COMPONENT_R_SETTLEMENT = "r_settlement";
   public static final String COMPONENT_R_STREET = "r_street";
   public static final String COMPONENT_R_ZIP = "r_zip";
   public static final String COMPONENT_S_TITLE_L = "s_title_le";
   public static final String COMPONENT_R_TITLE_L = "r_title_l";
   public static final String COMPONENT_R_STREET_L = "r_street_l";
   public static final String COMPONENT_R_ZIP_L = "r_zip_l";
   private static final long serialVersionUID = 1L;
   private JPanel sender_panel = null;
   private JPanel receiver_panel = null;
   private JPanel sender_selection_panel = null;
   private JPanel receiver_selection_panel = null;
   private JButton btn_stable = null;
   private JButton btn_mail = null;
   private JButton btn_find_receiver;
   private AddressPanel sender_data_panel = null;
   private JCheckBox chk_take_old_receiver;
   private JComboBox cbo_organizations = null;
   private JComboBox cbo_receivers = null;
   private AddressPanel receiver_data_panel = null;
   private JPanel buttons_panel = null;
   private JButton btn_ok = null;
   private JButton btn_cancel = null;
   protected EnvelopeBusiness e_business;
   private DefaultEventSupport des = new DefaultEventSupport();
   private JPanel receiverOrgPanel;
   private JPanel receiverPanel;

   public void addEventListener(IEventListener var1) {
      this.des.addEventListener(var1);
   }

   public void removeEventListener(IEventListener var1) {
      this.des.removeEventListener(var1);
   }

   public Vector fireEvent(Event var1) {
      return this.des.fireEvent(var1);
   }

   public EnvelopePanel() {
      this.initialize();
      this.prepare();
   }

   private void initialize() {
      this.setLayout(new BoxLayout(this, 1));
      this.setSize(new Dimension(600, 400));
      this.setPreferredSize(new Dimension(600, 400));
      this.add(this.getSender_panel(), (Object)null);
      this.add(Box.createRigidArea(new Dimension(0, 5)), (Object)null);
      this.add(this.getReceiver_panel(), (Object)null);
      this.add(this.getButtons_panel(), (Object)null);
      this.add(Box.createGlue());
   }

   protected void prepare() {
      this.e_business = new EnvelopeBusiness(this);
   }

   private JPanel getSender_panel() {
      if (this.sender_panel == null) {
         this.sender_panel = new JPanel();
         this.sender_panel.setLayout(new BoxLayout(this.getSender_panel(), 1));
         this.sender_panel.setBorder(BorderFactory.createEtchedBorder(0));
         this.sender_panel.add(this.getSender_data_panel(), (Object)null);
         this.sender_panel.add(Box.createGlue(), (Object)null);
         this.sender_panel.setAlignmentX(0.0F);
         this.sender_panel.add(this.getSender_selection_panel(), (Object)null);
         this.sender_panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
      }

      return this.sender_panel;
   }

   private JPanel getReceiver_panel() {
      if (this.receiver_panel == null) {
         this.receiver_panel = new JPanel();
         this.receiver_panel.setLayout(new BoxLayout(this.getReceiver_panel(), 1));
         this.receiver_panel.setBorder(BorderFactory.createEtchedBorder(0));
         this.receiver_panel.add(this.getReceiver_data_panel(), (Object)null);
         this.receiver_panel.add(this.getReceiver_selection_panel(), (Object)null);
         this.receiver_panel.setAlignmentX(0.0F);
         this.receiver_panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
      }

      return this.receiver_panel;
   }

   private JPanel getReceiver_selection_panel() {
      if (this.receiver_selection_panel == null) {
         this.receiver_selection_panel = new JPanel();
         this.getChk_take_old_receiver();
         this.receiver_selection_panel.setLayout(new BoxLayout(this.receiver_selection_panel, 1));
         this.receiver_selection_panel.setAlignmentX(0.0F);
         this.receiver_selection_panel.add(Box.createVerticalStrut(5));
         this.receiver_selection_panel.add(this.getReceiverOrgPanel());
         this.receiver_selection_panel.add(Box.createVerticalStrut(3));
         this.receiver_selection_panel.add(this.getReceiverPanel());
         this.receiver_selection_panel.add(Box.createVerticalStrut(3));
      }

      return this.receiver_selection_panel;
   }

   private JPanel getReceiverOrgPanel() {
      if (this.receiverOrgPanel == null) {
         this.receiverOrgPanel = new JPanel();
         this.receiverOrgPanel.setName("ReceiverOrgPanel");
         this.receiverOrgPanel.setLayout(new BoxLayout(this.receiverOrgPanel, 0));
         this.receiverOrgPanel.setAlignmentX(0.0F);
         this.receiverOrgPanel.add(Box.createHorizontalStrut(3));
         this.receiverOrgPanel.add(this.getCbo_organizations());
         this.receiverOrgPanel.add(Box.createHorizontalGlue());
      }

      return this.receiverOrgPanel;
   }

   private JPanel getReceiverPanel() {
      if (this.receiverPanel == null) {
         this.receiverPanel = new JPanel();
         this.receiverPanel.setName("ReceiverPanel");
         this.receiverPanel.setLayout(new BoxLayout(this.receiverPanel, 0));
         this.receiverPanel.setAlignmentX(0.0F);
         this.receiverPanel.add(Box.createHorizontalStrut(3));
         this.receiverPanel.add(this.getCbo_receivers());
         this.receiverPanel.add(Box.createHorizontalStrut(3));
      }

      return this.receiverPanel;
   }

   private JPanel getSender_selection_panel() {
      if (this.sender_selection_panel == null) {
         this.sender_selection_panel = new JPanel();
         this.sender_selection_panel.setLayout(new BoxLayout(this.getSender_selection_panel(), 0));
         this.sender_selection_panel.add(this.getBtn_stable(), (Object)null);
         this.sender_selection_panel.add(this.getBtn_mail(), (Object)null);
         this.sender_selection_panel.add(Box.createGlue(), (Object)null);
         this.sender_selection_panel.add(this.getBtn_find_receiver(), (Object)null);
         this.sender_selection_panel.setAlignmentX(0.0F);
      }

      return this.sender_selection_panel;
   }

   private JButton getBtn_stable() {
      if (this.btn_stable == null) {
         this.btn_stable = new JButton();
         this.btn_stable.setText("Állandó cím");
      }

      return this.btn_stable;
   }

   private JButton getBtn_mail() {
      if (this.btn_mail == null) {
         this.btn_mail = new JButton();
         this.btn_mail.setText("Levelezési cím");
      }

      return this.btn_mail;
   }

   private JButton getBtn_find_receiver() {
      if (this.btn_find_receiver == null) {
         this.btn_find_receiver = new JButton();
         this.btn_find_receiver.setText("Címzett kikeresése");
         this.btn_find_receiver.setAlignmentX(1.0F);
         this.btn_find_receiver.setVisible(false);
      }

      return this.btn_find_receiver;
   }

   private JPanel getSender_data_panel() {
      if (this.sender_data_panel == null) {
         this.sender_data_panel = new AddressPanel();
         this.sender_data_panel.setAlignmentX(0.0F);
      }

      return this.sender_data_panel;
   }

   private JCheckBox getChk_take_old_receiver() {
      if (this.chk_take_old_receiver == null) {
         this.chk_take_old_receiver = GuiUtil.getANYKCheckBox();
         this.chk_take_old_receiver.setText("Ha nem talál címzettet, tartsa meg a régit");
         this.chk_take_old_receiver.setAlignmentX(0.0F);
      }

      return this.chk_take_old_receiver;
   }

   private JComboBox getCbo_organizations() {
      if (this.cbo_organizations == null) {
         this.cbo_organizations = new JComboBox();
         this.cbo_organizations.setAlignmentX(0.0F);
         this.cbo_organizations.setEnabled(true);
      }

      return this.cbo_organizations;
   }

   private JComboBox getCbo_receivers() {
      if (this.cbo_receivers == null) {
         this.cbo_receivers = new JComboBox();
         this.cbo_receivers.setAlignmentX(0.0F);
         this.cbo_receivers.setEnabled(false);
      }

      return this.cbo_receivers;
   }

   private JPanel getReceiver_data_panel() {
      if (this.receiver_data_panel == null) {
         this.receiver_data_panel = new AddressPanel();
         this.receiver_data_panel.setAlignmentX(0.0F);
      }

      return this.receiver_data_panel;
   }

   private JPanel getButtons_panel() {
      if (this.buttons_panel == null) {
         FlowLayout var1 = new FlowLayout();
         var1.setAlignment(2);
         this.buttons_panel = new JPanel();
         this.buttons_panel.setLayout(var1);
         this.buttons_panel.add(this.getBtn_ok(), (Object)null);
         this.buttons_panel.add(this.getBtn_cancel(), (Object)null);
         this.buttons_panel.setAlignmentX(0.0F);
      }

      return this.buttons_panel;
   }

   private JButton getBtn_ok() {
      if (this.btn_ok == null) {
         this.btn_ok = new JButton();
         this.btn_ok.setText("OK");
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

   public EnvelopeBusiness getBusiness() {
      return this.e_business;
   }

   public JComponent getEPComponent(String var1) {
      if ("cancel_button".equalsIgnoreCase(var1)) {
         return this.btn_cancel;
      } else if ("ok_button".equalsIgnoreCase(var1)) {
         return this.btn_ok;
      } else if ("stable_button".equalsIgnoreCase(var1)) {
         return this.btn_stable;
      } else if ("mail_button".equalsIgnoreCase(var1)) {
         return this.btn_mail;
      } else if ("find_receiver_button".equalsIgnoreCase(var1)) {
         return this.btn_find_receiver;
      } else if ("take_old_chk".equalsIgnoreCase(var1)) {
         return this.chk_take_old_receiver;
      } else if ("org_cbo".equalsIgnoreCase(var1)) {
         return this.cbo_organizations;
      } else if ("addr_cbo".equalsIgnoreCase(var1)) {
         return this.cbo_receivers;
      } else if ("s_name".equalsIgnoreCase(var1)) {
         return this.sender_data_panel.getAComponent("name");
      } else if ("s_settlement".equalsIgnoreCase(var1)) {
         return this.sender_data_panel.getAComponent("settlement");
      } else if ("s_street".equalsIgnoreCase(var1)) {
         return this.sender_data_panel.getAComponent("street");
      } else if ("s_zip".equalsIgnoreCase(var1)) {
         return this.sender_data_panel.getAComponent("zip");
      } else if ("r_name".equalsIgnoreCase(var1)) {
         return this.receiver_data_panel.getAComponent("name");
      } else if ("r_settlement".equalsIgnoreCase(var1)) {
         return this.receiver_data_panel.getAComponent("settlement");
      } else if ("r_street".equalsIgnoreCase(var1)) {
         return this.receiver_data_panel.getAComponent("street");
      } else if ("r_zip".equalsIgnoreCase(var1)) {
         return this.receiver_data_panel.getAComponent("zip");
      } else if ("s_title_le".equalsIgnoreCase(var1)) {
         return this.sender_data_panel.getAComponent("title_l");
      } else if ("r_title_l".equalsIgnoreCase(var1)) {
         return this.receiver_data_panel.getAComponent("title_l");
      } else if ("r_street_l".equalsIgnoreCase(var1)) {
         return this.receiver_data_panel.getAComponent("street_l");
      } else {
         return "r_zip_l".equalsIgnoreCase(var1) ? this.receiver_data_panel.getAComponent("zip_l") : null;
      }
   }
}
