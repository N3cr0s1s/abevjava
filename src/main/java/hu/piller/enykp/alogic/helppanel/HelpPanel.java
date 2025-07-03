package hu.piller.enykp.alogic.helppanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

class HelpPanel extends JPanel {
   private static final long serialVersionUID = 1L;
   public static final String COMPONENT_EDITOR_EDP = "editor";
   public static final String COMPONENT_BACK_BTN = "back";
   public static final String COMPONENT_FORWARD_BTN = "forward";
   public static final String COMPONENT_OPEN_WITH_BROWSER_BTN = "open_with_browser";
   public static final String COMPONENT_FIND_LBL = "find_text_lbl";
   public static final String COMPONENT_FIND_TXT = "find_text";
   public static final String COMPONENT_SEARCH_BTN = "search";
   public static final String COMPONENT_SEARCH_N_BTN = "search_n";
   public static final String COMPONENT_SEARCH_P_BTN = "search_p";
   private JScrollPane scp_editor = null;
   private JEditorPane edp_editor = null;
   private JPanel buttons_panel = null;
   private JButton btn_back = null;
   private JButton btn_forward = null;
   private JButton btn_open_with_browser = null;
   private HelpPanelBusiness h_business;
   private JPanel find_panel = null;
   private JPanel north_panel = null;
   private JLabel lbl_find = null;
   private JTextField txt_find = null;
   private JPanel buttons_panel2 = null;
   private JButton btn_search = null;
   private JButton btn_n_search = null;
   private JButton btn_p_search = null;

   public HelpPanel() {
      this.initialize();
      this.prepare();
   }

   private void initialize() {
      this.setLayout(new BorderLayout());
      this.setSize(new Dimension(520, 395));
      this.add(this.getNorth_panel(), "North");
      this.add(this.getScp_editor(), "Center");
   }

   private void prepare() {
      this.h_business = new HelpPanelBusiness(this);
   }

   private JScrollPane getScp_editor() {
      if (this.scp_editor == null) {
         this.scp_editor = new JScrollPane();
         this.scp_editor.setViewportView(this.getEdp_editor());
         this.scp_editor.setVerticalScrollBarPolicy(20);
         this.scp_editor.setHorizontalScrollBarPolicy(30);
      }

      return this.scp_editor;
   }

   private JEditorPane getEdp_editor() {
      if (this.edp_editor == null) {
         this.edp_editor = new JEditorPane();
      }

      return this.edp_editor;
   }

   private JPanel getButtons_panel() {
      if (this.buttons_panel == null) {
         this.buttons_panel = new JPanel();
         this.buttons_panel.setLayout(new BoxLayout(this.getButtons_panel(), 0));
         this.buttons_panel.add(this.getBtn_back(), (Object)null);
         this.buttons_panel.add(this.getBtn_forward(), (Object)null);
         this.buttons_panel.add(Box.createHorizontalGlue(), (Object)null);
         this.buttons_panel.add(this.getBtn_open_with_browser(), (Object)null);
      }

      return this.buttons_panel;
   }

   private JButton getBtn_back() {
      if (this.btn_back == null) {
         this.btn_back = new JButton();
         this.btn_back.setText("Vissza");
         this.btn_back.setToolTipText(this.btn_back.getText());
         this.btn_back.setAlignmentX(0.0F);
      }

      return this.btn_back;
   }

   private JButton getBtn_forward() {
      if (this.btn_forward == null) {
         this.btn_forward = new JButton();
         this.btn_forward.setText("Előre");
         this.btn_forward.setToolTipText(this.btn_forward.getText());
         this.btn_forward.setAlignmentX(0.0F);
      }

      return this.btn_forward;
   }

   private JButton getBtn_open_with_browser() {
      if (this.btn_open_with_browser == null) {
         this.btn_open_with_browser = new JButton();
         this.btn_open_with_browser.setText("Megnyitás böngészőben");
         this.btn_open_with_browser.setToolTipText(this.btn_open_with_browser.getText());
         this.btn_open_with_browser.setAlignmentX(1.0F);
      }

      return this.btn_open_with_browser;
   }

   private JPanel getFind_panel() {
      if (this.find_panel == null) {
         this.find_panel = new JPanel();
         this.find_panel.setLayout(new BorderLayout());
         this.find_panel.add(this.getLbl_find(), "North");
         this.find_panel.add(this.getTxt_find(), "Center");
         this.find_panel.add(this.getButtons_panel2(), "South");
      }

      return this.find_panel;
   }

   private JPanel getNorth_panel() {
      if (this.north_panel == null) {
         this.north_panel = new JPanel();
         this.north_panel.setLayout(new BoxLayout(this.getNorth_panel(), 1));
         this.north_panel.add(this.getButtons_panel(), (Object)null);
         this.north_panel.add(Box.createRigidArea(new Dimension(0, 5)), (Object)null);
         this.north_panel.add(this.getFind_panel(), (Object)null);
      }

      return this.north_panel;
   }

   private JLabel getLbl_find() {
      if (this.lbl_find == null) {
         this.lbl_find = new JLabel("Keresendő kifejezés:");
      }

      return this.lbl_find;
   }

   private JTextField getTxt_find() {
      if (this.txt_find == null) {
         this.txt_find = new JTextField();
      }

      return this.txt_find;
   }

   private JPanel getButtons_panel2() {
      if (this.buttons_panel2 == null) {
         this.buttons_panel2 = new JPanel();
         this.buttons_panel2.setLayout(new BoxLayout(this.getButtons_panel2(), 0));
         this.buttons_panel2.add(this.getBtn_search(), (Object)null);
         this.buttons_panel2.add(this.getBtn_n_search(), (Object)null);
         this.buttons_panel2.add(this.getBtn_p_search(), (Object)null);
      }

      return this.buttons_panel2;
   }

   private JButton getBtn_search() {
      if (this.btn_search == null) {
         this.btn_search = new JButton();
         this.btn_search.setText("Keresés a lapon");
         this.btn_search.setToolTipText(this.btn_search.getText());
      }

      return this.btn_search;
   }

   private JButton getBtn_n_search() {
      if (this.btn_n_search == null) {
         this.btn_n_search = new JButton();
         this.btn_n_search.setText("Következő keresése");
         this.btn_n_search.setToolTipText(this.btn_n_search.getText());
      }

      return this.btn_n_search;
   }

   private JButton getBtn_p_search() {
      if (this.btn_p_search == null) {
         this.btn_p_search = new JButton();
         this.btn_p_search.setText("Előző keresése");
         this.btn_p_search.setToolTipText(this.btn_p_search.getText());
      }

      return this.btn_p_search;
   }

   public JComponent getHComponent(String var1) {
      if ("back".equalsIgnoreCase(var1)) {
         return this.btn_back;
      } else if ("forward".equalsIgnoreCase(var1)) {
         return this.btn_forward;
      } else if ("open_with_browser".equalsIgnoreCase(var1)) {
         return this.btn_open_with_browser;
      } else if ("editor".equalsIgnoreCase(var1)) {
         return this.edp_editor;
      } else if ("find_text_lbl".equalsIgnoreCase(var1)) {
         return this.lbl_find;
      } else if ("find_text".equalsIgnoreCase(var1)) {
         return this.txt_find;
      } else if ("search".equalsIgnoreCase(var1)) {
         return this.btn_search;
      } else if ("search_n".equalsIgnoreCase(var1)) {
         return this.btn_n_search;
      } else {
         return "search_p".equalsIgnoreCase(var1) ? this.btn_p_search : null;
      }
   }

   public HelpPanelBusiness getHelpBusiness() {
      return this.h_business;
   }
}
