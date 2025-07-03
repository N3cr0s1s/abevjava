package hu.piller.enykp.util.base.messageinfo;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.util.icon.ENYKIconSet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

public class MessageInfoPanel extends JPanel {
   private static final long serialVersionUID = 1L;
   public static final String COMPONENT_SHOW_EXCEPTIONS_CHK = "show_exceptions";
   public static final String COMPONENT_SHOW_ONLY_FORM_MESSAGES_CHK = "show_only_form_check_messages";
   public static final String COMPONENT_SAVE_MESSAGES_BTN = "save_form_messages";
   public static final String COMPONENT_MESSAGES_IDS_TBL = "message_ids";
   public static final String COMPONENT_MESSAGES_TBL = "messages";
   public static final String COMPONENT_MESSAGE_TXT = "message";
   public static final String COMPONENT_EXCEPTION_LBL = "exception_label";
   public static final String COMPONENT_EXCEPTION_TXT = "exception";
   public static final String COMPONENT_REFRESH_BTN = "refresh";
   public static final String COMPONENT_CLEAR_BTN = "clear";
   public static final String COMPONENT_SHOW_FAT_ERR_MSG_CHK = "show_fat_err_msg_chk";
   public static final String COMPONENT_SHOW_ERR_MSG_CHK = "show_err_msg_chk";
   public static final String COMPONENT_SHOW_WRN_MSG_CHK = "show_wrn_msg_chk";
   public static final String COMPONENT_SHOW_MSG_MSG_CHK = "show_msg_msg_chk";
   public static final String COMPONENT_SHOW_GEN_ERR_MSG_CHK = "show_gen_err_msg_chk";
   public static final String COMPONENT_SHOW_FAT_ERR_MSG_LBL = "show_fat_err_msg_lbl";
   public static final String COMPONENT_SHOW_ERR_MSG_LBL = "show_err_msg_lbl";
   public static final String COMPONENT_SHOW_WRN_MSG_LBL = "show_wrn_msg_lbl";
   public static final String COMPONENT_SHOW_MSG_MSG_LBL = "show_msg_msg_lbl";
   public static final String COMPONENT_SHOW_MSG_ID_SCP = "msg_id_scp";
   public static final String COMPONENT_SHOW_MSG_ID_LBL = "msg_id_lbl";
   public static final String COMPONENT_SHOW_GEN_ERR_MSG_LBL = "show_gen_err_msg_lbl";
   public static final String COMPONENT_EXCEPTION_TXT_SP = "exception_sp";
   private JCheckBox chk_show_exceptions = null;
   private JCheckBox chk_show_only_form_check_messages = null;
   private JPanel form_panel = null;
   private JButton btn_save_form_messages = null;
   private JScrollPane scp_message_ids = null;
   private JTable tbl_message_ids = null;
   private JPanel msg_types_panel = null;
   private JCheckBox chk_show_msg = null;
   private JCheckBox chk_show_wrn = null;
   private JCheckBox chk_show_err = null;
   private JCheckBox chk_show_fat_err = null;
   private JCheckBox chk_show_gen_err = null;
   private JScrollPane scp_messages = null;
   private JTable tbl_messages = null;
   private JScrollPane scp_message = null;
   private JTextArea txt_message = null;
   private JScrollPane scp_exception = null;
   private JTextArea txt_exception = null;
   private JPanel buttons_panel = null;
   private JButton btn_refresh = null;
   private JButton btn_clear = null;
   private JLabel lbl_message = null;
   private JLabel lbl_exception = null;
   private JLabel lbl_messages = null;
   private JLabel lbl_message_ids = null;
   private JLabel lbl_show_msg = null;
   private JLabel lbl_show_wrn = null;
   private JLabel lbl_show_err = null;
   private JLabel lbl_show_fat_err = null;
   private JLabel lbl_show_gen_err = null;
   private MessageInfoBusiness mi_business;

   public MessageInfoPanel() {
      this.initialize();
      this.prepare();
   }

   private void initialize() {
      JPanel var1 = new JPanel();
      var1.setLayout(new BoxLayout(var1, 1));
      JPanel var2 = new JPanel();
      var2.setLayout(new GridLayout(2, 1));
      JSplitPane var3 = new JSplitPane(0, var1, var2);
      this.lbl_message_ids = new JLabel();
      this.lbl_message_ids.setText("Üzenet csoportok");
      this.lbl_message_ids.setAlignmentX(0.0F);
      this.lbl_messages = new JLabel();
      this.lbl_messages.setText("Üzenetek");
      this.lbl_messages.setAlignmentX(0.0F);
      this.lbl_exception = new JLabel();
      this.lbl_exception.setText("Rendszer üzenet:");
      this.lbl_exception.setAlignmentX(0.0F);
      this.lbl_message = new JLabel();
      this.lbl_message.setText("Üzenet:");
      this.lbl_message.setAlignmentX(0.0F);
      this.setLayout(new BorderLayout());
      this.setSize(new Dimension(GuiUtil.getScreenW() / 2, (int)((double)GuiUtil.getScreenH() * 0.6D)));
      var1.add(this.getChk_show_exceptions(), (Object)null);
      var1.add(this.getForm_panel(), (Object)null);
      var1.add(this.lbl_message_ids, (Object)null);
      var1.add(this.getScp_message_ids(), (Object)null);
      var1.add(this.getMsg_types_panel(), (Object)null);
      var1.add(this.getScp_messages(), (Object)null);
      JPanel var4 = new JPanel(new BorderLayout());
      var4.add(this.lbl_message, "North");
      var4.add(this.getScp_message(), "Center");
      JPanel var5 = new JPanel(new BorderLayout());
      var5.add(this.lbl_exception, "North");
      var5.add(this.getScp_exception(), "Center");
      var2.add(var4);
      var2.add(var5);
      var3.setDividerLocation((int)((double)GuiUtil.getScreenH() * 0.3D));
      this.add(var3, "Center");
      this.add(this.getButtons_panel(), "South");
   }

   private void prepare() {
      this.mi_business = new MessageInfoBusiness(this);
   }

   public MessageInfoBusiness getBusiness() {
      return this.mi_business;
   }

   private JCheckBox getChk_show_exceptions() {
      if (this.chk_show_exceptions == null) {
         this.chk_show_exceptions = GuiUtil.getANYKCheckBox();
         this.chk_show_exceptions.setAlignmentX(0.0F);
         this.chk_show_exceptions.setText("Rendszer üzenetek láthatók");
         this.chk_show_exceptions.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.chk_show_exceptions.getHeight()));
      }

      return this.chk_show_exceptions;
   }

   private JCheckBox getChk_show_only_form_check_messages() {
      if (this.chk_show_only_form_check_messages == null) {
         this.chk_show_only_form_check_messages = GuiUtil.getANYKCheckBox();
         this.chk_show_only_form_check_messages.setAlignmentX(0.0F);
         this.chk_show_only_form_check_messages.setText("Csak űrlap ellenőrzési üzenetek láthatók");
      }

      return this.chk_show_only_form_check_messages;
   }

   private JPanel getForm_panel() {
      if (this.form_panel == null) {
         this.form_panel = new JPanel();
         this.form_panel.setLayout(new BoxLayout(this.getForm_panel(), 0));
         this.form_panel.setAlignmentX(0.0F);
         this.form_panel.add(this.getChk_show_only_form_check_messages(), (Object)null);
         this.form_panel.add(this.getBtn_refresh(), (Object)null);
         this.form_panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.chk_show_exceptions.getHeight()));
      }

      return this.form_panel;
   }

   private JButton getBtn_save_form_messages() {
      if (this.btn_save_form_messages == null) {
         this.btn_save_form_messages = new JButton();
         this.btn_save_form_messages.setAlignmentX(0.0F);
         this.btn_save_form_messages.setText("Állományba mentés");
      }

      return this.btn_save_form_messages;
   }

   private JScrollPane getScp_message_ids() {
      if (this.scp_message_ids == null) {
         this.scp_message_ids = new JScrollPane();
         this.scp_message_ids.setAlignmentX(0.0F);
         this.scp_message_ids.setHorizontalScrollBarPolicy(31);
         this.scp_message_ids.setVerticalScrollBarPolicy(20);
         this.scp_message_ids.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
         this.scp_message_ids.setPreferredSize(new Dimension(Integer.MAX_VALUE, 200));
         this.scp_message_ids.setViewportView(this.getTbl_message_ids());
      }

      return this.scp_message_ids;
   }

   private JTable getTbl_message_ids() {
      if (this.tbl_message_ids == null) {
         this.tbl_message_ids = new JTable();
      }

      return this.tbl_message_ids;
   }

   private JScrollPane getScp_messages() {
      if (this.scp_messages == null) {
         this.scp_messages = new JScrollPane();
         this.scp_messages.setAlignmentX(0.0F);
         this.scp_messages.setHorizontalScrollBarPolicy(30);
         this.scp_messages.setVerticalScrollBarPolicy(20);
         this.scp_messages.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
         this.scp_messages.setViewportView(this.getTbl_messages());
      }

      return this.scp_messages;
   }

   private JTable getTbl_messages() {
      if (this.tbl_messages == null) {
         this.tbl_messages = new JTable();
         this.tbl_messages.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
      }

      return this.tbl_messages;
   }

   private JPanel getMsg_types_panel() {
      if (this.msg_types_panel == null) {
         this.msg_types_panel = new JPanel();
         this.msg_types_panel.setAlignmentX(0.0F);
         this.msg_types_panel.setLayout(new BoxLayout(this.getMsg_types_panel(), 0));
         this.msg_types_panel.add(this.lbl_messages, (Object)null);
         this.msg_types_panel.add(Box.createRigidArea(new Dimension(20, 0)), (Object)null);
         this.msg_types_panel.add(this.getChk_show_msg(), (Object)null);
         this.msg_types_panel.add(this.getLbl_show_msg(), (Object)null);
         this.msg_types_panel.add(Box.createRigidArea(new Dimension(20, 0)), (Object)null);
         this.msg_types_panel.add(this.getChk_show_wrn(), (Object)null);
         this.msg_types_panel.add(this.getLbl_show_wrn(), (Object)null);
         this.msg_types_panel.add(Box.createRigidArea(new Dimension(20, 0)), (Object)null);
         this.msg_types_panel.add(this.getChk_show_err(), (Object)null);
         this.msg_types_panel.add(this.getLbl_show_err(), (Object)null);
         this.msg_types_panel.add(Box.createRigidArea(new Dimension(20, 0)), (Object)null);
         this.msg_types_panel.add(this.getChk_show_fat_err(), (Object)null);
         this.msg_types_panel.add(this.getLbl_show_fat_err(), (Object)null);
         this.msg_types_panel.add(Box.createRigidArea(new Dimension(20, 0)), (Object)null);
         this.msg_types_panel.add(this.getChk_show_gen_err(), (Object)null);
         this.msg_types_panel.add(this.getLbl_show_gen_err(), (Object)null);
         this.msg_types_panel.add(Box.createGlue(), (Object)null);
      }

      return this.msg_types_panel;
   }

   private JCheckBox getChk_show_msg() {
      if (this.chk_show_msg == null) {
         this.chk_show_msg = GuiUtil.getANYKCheckBox("");
         this.chk_show_msg.setAlignmentX(0.0F);
      }

      return this.chk_show_msg;
   }

   private JLabel getLbl_show_msg() {
      if (this.lbl_show_msg == null) {
         this.lbl_show_msg = new JLabel("");
         this.lbl_show_msg.setAlignmentX(0.0F);
         this.lbl_show_msg.setIcon(ENYKIconSet.getInstance().get("statusz_zold"));
      }

      return this.lbl_show_msg;
   }

   private JCheckBox getChk_show_wrn() {
      if (this.chk_show_wrn == null) {
         this.chk_show_wrn = GuiUtil.getANYKCheckBox("");
         this.chk_show_wrn.setAlignmentX(0.0F);
      }

      return this.chk_show_wrn;
   }

   private JLabel getLbl_show_wrn() {
      if (this.lbl_show_wrn == null) {
         this.lbl_show_wrn = new JLabel("");
         this.lbl_show_wrn.setAlignmentX(0.0F);
         this.lbl_show_wrn.setIcon(ENYKIconSet.getInstance().get("statusz_sarga"));
      }

      return this.lbl_show_wrn;
   }

   private JCheckBox getChk_show_fat_err() {
      if (this.chk_show_fat_err == null) {
         this.chk_show_fat_err = GuiUtil.getANYKCheckBox("");
         this.chk_show_fat_err.setAlignmentX(0.0F);
      }

      return this.chk_show_fat_err;
   }

   private JCheckBox getChk_show_err() {
      if (this.chk_show_err == null) {
         this.chk_show_err = GuiUtil.getANYKCheckBox("");
         this.chk_show_err.setAlignmentX(0.0F);
      }

      return this.chk_show_err;
   }

   private JCheckBox getChk_show_gen_err() {
      if (this.chk_show_gen_err == null) {
         this.chk_show_gen_err = GuiUtil.getANYKCheckBox("");
         this.chk_show_gen_err.setAlignmentX(0.0F);
      }

      return this.chk_show_gen_err;
   }

   private JLabel getLbl_show_err() {
      if (this.lbl_show_err == null) {
         this.lbl_show_err = new JLabel("");
         this.lbl_show_err.setAlignmentX(0.0F);
         this.lbl_show_err.setIcon(ENYKIconSet.getInstance().get("statusz_piros"));
      }

      return this.lbl_show_err;
   }

   private JLabel getLbl_show_fat_err() {
      if (this.lbl_show_fat_err == null) {
         this.lbl_show_fat_err = new JLabel("");
         this.lbl_show_fat_err.setAlignmentX(0.0F);
         this.lbl_show_fat_err.setIcon(ENYKIconSet.getInstance().get("statusz_kek"));
      }

      return this.lbl_show_fat_err;
   }

   private JLabel getLbl_show_gen_err() {
      if (this.lbl_show_gen_err == null) {
         this.lbl_show_gen_err = new JLabel("");
         this.lbl_show_gen_err.setAlignmentX(0.0F);
         this.lbl_show_gen_err.setIcon(ENYKIconSet.getInstance().get("statusz_fekete"));
      }

      return this.lbl_show_gen_err;
   }

   private JScrollPane getScp_message() {
      if (this.scp_message == null) {
         this.scp_message = new JScrollPane();
         this.scp_message.setAlignmentX(0.0F);
         this.scp_message.setHorizontalScrollBarPolicy(31);
         this.scp_message.setVerticalScrollBarPolicy(20);
         this.scp_message.setMaximumSize(new Dimension(Integer.MAX_VALUE, 3 * GuiUtil.getCommonItemHeight()));
         this.scp_message.setMinimumSize(new Dimension(Integer.MAX_VALUE, 3 * GuiUtil.getCommonItemHeight()));
         this.scp_message.setPreferredSize(new Dimension(Integer.MAX_VALUE, 3 * GuiUtil.getCommonItemHeight()));
         this.scp_message.setViewportView(this.getTxt_message());
      }

      return this.scp_message;
   }

   private JTextArea getTxt_message() {
      if (this.txt_message == null) {
         this.txt_message = new JTextArea();
      }

      return this.txt_message;
   }

   private JScrollPane getScp_exception() {
      if (this.scp_exception == null) {
         this.scp_exception = new JScrollPane();
         this.scp_exception.setAlignmentX(0.0F);
         this.scp_exception.setHorizontalScrollBarPolicy(31);
         this.scp_exception.setVerticalScrollBarPolicy(20);
         this.scp_exception.setMaximumSize(new Dimension(Integer.MAX_VALUE, 3 * GuiUtil.getCommonItemHeight()));
         this.scp_exception.setMinimumSize(new Dimension(Integer.MAX_VALUE, 3 * GuiUtil.getCommonItemHeight()));
         this.scp_exception.setPreferredSize(new Dimension(Integer.MAX_VALUE, 3 * GuiUtil.getCommonItemHeight()));
         this.scp_exception.setViewportView(this.getTxt_exception());
      }

      return this.scp_exception;
   }

   private JTextArea getTxt_exception() {
      if (this.txt_exception == null) {
         this.txt_exception = new JTextArea();
      }

      return this.txt_exception;
   }

   private JPanel getButtons_panel() {
      if (this.buttons_panel == null) {
         GridLayout var1 = new GridLayout();
         var1.setRows(1);
         var1.setColumns(2);
         this.buttons_panel = new JPanel();
         this.buttons_panel.setLayout(var1);
         this.buttons_panel.add(this.getBtn_save_form_messages(), (Object)null);
         this.buttons_panel.add(this.getBtn_clear(), (Object)null);
         this.buttons_panel.setSize(new Dimension(GuiUtil.getW("WWÁllományba mentésWWWWLista törléseWW"), GuiUtil.getCommonItemHeight() + 4));
         this.buttons_panel.setPreferredSize(new Dimension(GuiUtil.getW("WWÁllományba mentésWWWWLista törléseWW"), GuiUtil.getCommonItemHeight() + 4));
         this.buttons_panel.setMinimumSize(new Dimension(GuiUtil.getW("WWÁllományba mentésWWWWLista törléseWW"), GuiUtil.getCommonItemHeight() + 4));
      }

      return this.buttons_panel;
   }

   private JButton getBtn_refresh() {
      if (this.btn_refresh == null) {
         this.btn_refresh = new JButton();
         this.btn_refresh.setText("Frissítés");
      }

      return this.btn_refresh;
   }

   private JButton getBtn_clear() {
      if (this.btn_clear == null) {
         this.btn_clear = new JButton();
         this.btn_clear.setText("Lista törlése");
      }

      return this.btn_clear;
   }

   public JComponent getIPComponent(String var1) {
      if ("show_exceptions".equalsIgnoreCase(var1)) {
         return this.chk_show_exceptions;
      } else if ("show_only_form_check_messages".equalsIgnoreCase(var1)) {
         return this.chk_show_only_form_check_messages;
      } else if ("save_form_messages".equalsIgnoreCase(var1)) {
         return this.btn_save_form_messages;
      } else if ("message_ids".equalsIgnoreCase(var1)) {
         return this.tbl_message_ids;
      } else if ("messages".equalsIgnoreCase(var1)) {
         return this.tbl_messages;
      } else if ("message".equalsIgnoreCase(var1)) {
         return this.txt_message;
      } else if ("exception_label".equalsIgnoreCase(var1)) {
         return this.lbl_exception;
      } else if ("exception".equalsIgnoreCase(var1)) {
         return this.txt_exception;
      } else if ("refresh".equalsIgnoreCase(var1)) {
         return this.btn_refresh;
      } else if ("clear".equalsIgnoreCase(var1)) {
         return this.btn_clear;
      } else if ("exception_sp".equalsIgnoreCase(var1)) {
         return this.scp_exception;
      } else if ("show_fat_err_msg_chk".equalsIgnoreCase(var1)) {
         return this.chk_show_fat_err;
      } else if ("show_err_msg_chk".equalsIgnoreCase(var1)) {
         return this.chk_show_err;
      } else if ("show_wrn_msg_chk".equalsIgnoreCase(var1)) {
         return this.chk_show_wrn;
      } else if ("show_msg_msg_chk".equalsIgnoreCase(var1)) {
         return this.chk_show_msg;
      } else if ("show_gen_err_msg_chk".equalsIgnoreCase(var1)) {
         return this.chk_show_gen_err;
      } else if ("msg_id_scp".equalsIgnoreCase(var1)) {
         return this.scp_message_ids;
      } else if ("msg_id_lbl".equalsIgnoreCase(var1)) {
         return this.lbl_message_ids;
      } else if ("show_err_msg_lbl".equalsIgnoreCase(var1)) {
         return this.lbl_show_err;
      } else if ("show_fat_err_msg_lbl".equalsIgnoreCase(var1)) {
         return this.lbl_show_fat_err;
      } else if ("show_wrn_msg_lbl".equalsIgnoreCase(var1)) {
         return this.lbl_show_wrn;
      } else if ("show_msg_msg_lbl".equalsIgnoreCase(var1)) {
         return this.lbl_show_msg;
      } else {
         return "show_gen_err_msg_lbl".equalsIgnoreCase(var1) ? this.lbl_show_gen_err : null;
      }
   }

   public void destroy() {
      this.mi_business = null;
   }
}
