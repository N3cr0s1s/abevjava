package hu.piller.enykp.alogic.filepanels.filepanel;

import hu.piller.enykp.alogic.filepanels.datecombo.DateCombo;
import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.IFilterPanel;
import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.IFilterPanelLogic;
import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.TableFilterPanel;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.util.base.eventsupport.DefaultEventSupport;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import hu.piller.enykp.util.base.tabledialog.TooltipTableHeader;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class FilePanel extends JPanel implements IEventSupport, IFilterPanel {
   public static final String COMPONENT_FILES_TBL = "files";
   public static final String COMPONENT_B11_BTN = "select_all";
   public static final String COMPONENT_B12_BTN = "deselect_all";
   public static final String COMPONENT_B13_BTN = "replica";
   public static final String COMPONENT_B14_BTN = "rename";
   public static final String COMPONENT_B15_BTN = "delete";
   public static final String COMPONENT_B21_BTN = "copy";
   public static final String COMPONENT_B22_BTN = "choose_path";
   public static final String COMPONENT_B22_1_BTN = "22_1";
   public static final String COMPONENT_B22_2_BTN = "22_2";
   public static final String COMPONENT_B23_BTN = "b23";
   public static final String COMPONENT_PATH_TXT = "path_txt";
   public static final String COMPONENT_PATH_SCP = "path_csp";
   public static final String COMPONENT_B32_BTN = "ok";
   public static final String COMPONENT_B31_BTN = "cancel";
   public static final String COMPONENT_BSEP_COMP = "btn_line_sep";
   public static final String COMPONENT_BUTTONS_PANEL = "buttons_panel";
   public static final String COMPONENT_FILE_LIST_PANEL = "file_list_panel";
   public static final String COMPONENT_FILES_TITLE_LBL = "files_title_lbl";
   public static final String COMPONENT_FILE_FILTER_PANEL = "file_filter_panel";
   private static final long serialVersionUID = 1L;
   private JPanel filter_panel = null;
   private JPanel button_panel = null;
   private JPanel file_list_panel = null;
   private JScrollPane scp_file_list = null;
   private JTable tbl_file_list = null;
   private JTextField txt_form_name = null;
   private JTextField txt_name = null;
   private DateCombo cbo_date_from = null;
   private DateCombo cbo_date_to = null;
   private JTextField txt_info = null;
   private JTextField txt_note = null;
   private JComboBox cbo_state = null;
   private JPanel first_row_panel = null;
   private JPanel second_row_panel = null;
   private JPanel ssecond_row_panel = null;
   private JPanel third_row_panel = null;
   private JButton btn_11 = null;
   private JButton btn_12 = null;
   private JButton btn_13 = null;
   private JButton btn_14 = null;
   private JButton btn_15 = null;
   private JButton btn_31 = null;
   private JButton btn_32 = null;
   private JButton btn_21 = null;
   private JScrollPane scp_path = null;
   private JTextField txt_path = null;
   private JButton btn_22 = null;
   private JButton btn_23 = null;
   private JButton btn_22_1 = null;
   private JButton btn_22_2 = null;
   private Component buttons_sparator;
   private JButton btn_delete_filters = null;
   private JPanel filter_buttons_panel = null;
   private JLabel lbl_title2 = null;
   private JScrollPane scp_file_filter = null;
   private JList lst_file_filter = null;
   private JLabel lbl_toggle_filter = null;
   private FileBusiness f_business;
   private DefaultEventSupport des = new DefaultEventSupport();

   public void addEventListener(IEventListener var1) {
      this.des.addEventListener(var1);
   }

   public void removeEventListener(IEventListener var1) {
      this.des.removeEventListener(var1);
   }

   public Vector fireEvent(Event var1) {
      return this.des.fireEvent(var1);
   }

   public FilePanel() {
      this.initialize();
      this.prepare();
   }

   private void initialize() {
      this.setLayout(new BorderLayout());
      this.setMinimumSize(new Dimension(220, 10 * (GuiUtil.getCommonItemHeight() + 2)));
      this.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
      this.add(this.getFilter_panel(), "North");
      this.add(this.getFile_list_panel(), "Center");
      this.add(this.getButton_panel(), "South");
   }

   private void prepare() {
      this.f_business = new FileBusiness(this);
   }

   private JPanel getButton_panel() {
      if (this.button_panel == null) {
         this.button_panel = new JPanel();
         this.button_panel.setLayout(new BoxLayout(this.getButton_panel(), 3));
         this.button_panel.setBorder(BorderFactory.createEtchedBorder(0));
         this.button_panel.add(this.getFirst_row_panel(), (Object)null);
         this.button_panel.add(this.getSecond_row_panel(), (Object)null);
         this.button_panel.add(this.getSecondsecond_row_panel(), (Object)null);
         this.button_panel.add(this.buttons_sparator = Box.createRigidArea(new Dimension(0, 5)), (Object)null);
         this.button_panel.add(this.getThird_row_panel(), (Object)null);
      }

      return this.button_panel;
   }

   private JPanel getFile_list_panel() {
      if (this.file_list_panel == null) {
         this.lbl_title2 = new JLabel();
         this.lbl_title2.setText("Állományok");
         this.lbl_title2.setBackground(GuiUtil.getModifiedBGColor());
         this.lbl_title2.setFont(new Font("Dialog", 2, Math.max(18, GuiUtil.getCommonFontSize())));
         this.lbl_title2.setOpaque(true);
         this.file_list_panel = new JPanel();
         this.file_list_panel.setLayout(new BorderLayout());
         this.file_list_panel.setBorder(BorderFactory.createEtchedBorder(0));
         this.file_list_panel.add(this.lbl_title2, "North");
         this.file_list_panel.add(this.getScp_file_list(), "Center");
      }

      return this.file_list_panel;
   }

   private JScrollPane getScp_file_list() {
      if (this.scp_file_list == null) {
         this.scp_file_list = new JScrollPane();
         this.scp_file_list.setViewportView(this.getTbl_file_list());
         int var1 = (int)Math.max(this.scp_file_list.getVerticalScrollBar().getPreferredSize().getWidth(), (double)GuiUtil.getW("??"));
         this.scp_file_list.getVerticalScrollBar().setPreferredSize(new Dimension(var1, 0));
         this.scp_file_list.getHorizontalScrollBar().setPreferredSize(new Dimension(0, var1));
         this.scp_file_list.setVerticalScrollBarPolicy(20);
         this.scp_file_list.setHorizontalScrollBarPolicy(30);
      }

      return this.scp_file_list;
   }

   public JTable getTbl_file_list() {
      if (this.tbl_file_list == null) {
         this.tbl_file_list = new FileTable();
         this.tbl_file_list.setTableHeader(new TooltipTableHeader(this.tbl_file_list.getColumnModel()));
         this.tbl_file_list.setName("tbl_files");
      }

      return this.tbl_file_list;
   }

   private JPanel getFirst_row_panel() {
      if (this.first_row_panel == null) {
         this.first_row_panel = new JPanel();
         this.first_row_panel.setLayout(new BoxLayout(this.getFirst_row_panel(), 0));
         this.first_row_panel.add(this.getBtn_11(), (Object)null);
         this.first_row_panel.add(this.getBtn_12(), (Object)null);
         this.first_row_panel.add(this.getBtn_13(), (Object)null);
         this.first_row_panel.add(this.getBtn_14(), (Object)null);
         this.first_row_panel.add(this.getBtn_15(), (Object)null);
         this.first_row_panel.add(this.getBtn_21(), (Object)null);
         this.first_row_panel.setAlignmentX(0.0F);
      }

      return this.first_row_panel;
   }

   private JPanel getSecond_row_panel() {
      if (this.second_row_panel == null) {
         this.second_row_panel = new JPanel();
         this.second_row_panel.setLayout(new BoxLayout(this.getSecond_row_panel(), 0));
         this.second_row_panel.setAlignmentX(0.0F);
         this.second_row_panel.add(this.getScp_path(), (Object)null);
         this.second_row_panel.add(this.getBtn_22(), (Object)null);
         this.second_row_panel.add(this.getBtn_23(), (Object)null);
         this.second_row_panel.add(Box.createGlue(), (Object)null);
      }

      return this.second_row_panel;
   }

   private JPanel getSecondsecond_row_panel() {
      if (this.ssecond_row_panel == null) {
         this.ssecond_row_panel = new JPanel();
         this.ssecond_row_panel.setLayout(new BoxLayout(this.getSecondsecond_row_panel(), 0));
         this.ssecond_row_panel.setAlignmentX(0.0F);
         this.ssecond_row_panel.add(this.getBtn_22_1(), (Object)null);
         this.ssecond_row_panel.add(this.getBtn_22_2(), (Object)null);
      }

      return this.ssecond_row_panel;
   }

   private JPanel getThird_row_panel() {
      if (this.third_row_panel == null) {
         this.third_row_panel = new JPanel();
         this.third_row_panel.setLayout(new BoxLayout(this.getThird_row_panel(), 0));
         this.third_row_panel.setAlignmentX(0.0F);
         this.third_row_panel.add(Box.createGlue(), (Object)null);
         this.third_row_panel.add(this.getBtn_31(), (Object)null);
         this.third_row_panel.add(this.getBtn_32(), (Object)null);
      }

      return this.third_row_panel;
   }

   private JButton getBtn_11() {
      if (this.btn_11 == null) {
         this.btn_11 = new JButton();
         this.btn_11.setText("11");
      }

      return this.btn_11;
   }

   private JButton getBtn_12() {
      if (this.btn_12 == null) {
         this.btn_12 = new JButton();
         this.btn_12.setText("12");
      }

      return this.btn_12;
   }

   private JButton getBtn_13() {
      if (this.btn_13 == null) {
         this.btn_13 = new JButton();
         this.btn_13.setText("13");
         this.btn_13.setPreferredSize(new Dimension(600, 30));
      }

      return this.btn_13;
   }

   private JButton getBtn_14() {
      if (this.btn_14 == null) {
         this.btn_14 = new JButton();
         this.btn_14.setText("14");
         this.btn_14.setPreferredSize(new Dimension(600, 30));
      }

      return this.btn_14;
   }

   private JButton getBtn_15() {
      if (this.btn_15 == null) {
         this.btn_15 = new JButton();
         this.btn_15.setText("15");
         this.btn_15.setPreferredSize(new Dimension(600, 30));
      }

      return this.btn_15;
   }

   private JButton getBtn_31() {
      if (this.btn_31 == null) {
         this.btn_31 = new JButton();
         this.btn_31.setText("31");
         this.btn_31.setAlignmentX(1.0F);
      }

      return this.btn_31;
   }

   private JButton getBtn_32() {
      if (this.btn_32 == null) {
         this.btn_32 = new JButton();
         this.btn_32.setText("32");
         this.btn_32.setAlignmentX(1.0F);
      }

      return this.btn_32;
   }

   private JButton getBtn_21() {
      if (this.btn_21 == null) {
         this.btn_21 = new JButton();
         this.btn_21.setText("21");
      }

      return this.btn_21;
   }

   private JScrollPane getScp_path() {
      if (this.scp_path == null) {
         this.scp_path = new JScrollPane();
         this.scp_path.setHorizontalScrollBarPolicy(32);
         this.scp_path.setVerticalScrollBarPolicy(21);
         this.scp_path.setViewportView(this.getTxt_path());
         this.scp_path.setAlignmentY(0.0F);
         this.scp_path.setMinimumSize(new Dimension(70, 45));
         this.scp_path.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
      }

      return this.scp_path;
   }

   private JTextField getTxt_path() {
      if (this.txt_path == null) {
         this.txt_path = new JTextField();
         this.txt_path.setBorder((Border)null);
         this.txt_path.setMinimumSize(new Dimension(70, 20));
         this.txt_path.setEditable(false);
         this.txt_path.setFocusable(false);
      }

      return this.txt_path;
   }

   private JButton getBtn_22() {
      if (this.btn_22 == null) {
         this.btn_22 = new JButton();
         this.btn_22.setText("22");
         this.btn_22.setAlignmentY(0.0F);
         this.btn_22.setMargin(new Insets(0, 5, 0, 5));
         this.btn_22.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
      }

      return this.btn_22;
   }

   private JButton getBtn_23() {
      if (this.btn_23 == null) {
         this.btn_23 = new JButton();
         this.btn_23.setText("23");
         this.btn_23.setAlignmentY(0.0F);
         this.btn_23.setMargin(new Insets(0, 5, 0, 5));
         this.btn_23.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
      }

      return this.btn_23;
   }

   private JButton getBtn_22_1() {
      if (this.btn_22_1 == null) {
         this.btn_22_1 = new JButton();
         this.btn_22_1.setText("22_1");
         this.btn_22_1.setAlignmentY(0.0F);
         this.btn_22_1.setMargin(new Insets(0, 5, 0, 5));
         this.btn_22_1.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
         this.btn_22_1.setVisible(false);
      }

      return this.btn_22_1;
   }

   private JButton getBtn_22_2() {
      if (this.btn_22_2 == null) {
         this.btn_22_2 = new JButton();
         this.btn_22_2.setText("22_2");
         this.btn_22_2.setAlignmentY(0.0F);
         this.btn_22_2.setMargin(new Insets(0, 5, 0, 5));
         this.btn_22_2.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
         this.btn_22_2.setVisible(false);
      }

      return this.btn_22_2;
   }

   private JPanel getFilter_panel() {
      if (this.filter_panel == null) {
         this.filter_panel = new TableFilterPanel(this.getTbl_file_list());
      }

      return this.filter_panel;
   }

   public FileBusiness getBusiness() {
      return this.f_business;
   }

   public Component getFPComponent(String var1) {
      if ("files".equalsIgnoreCase(var1)) {
         return this.tbl_file_list;
      } else if ("select_all".equalsIgnoreCase(var1)) {
         return this.btn_11;
      } else if ("deselect_all".equalsIgnoreCase(var1)) {
         return this.btn_12;
      } else if ("replica".equalsIgnoreCase(var1)) {
         return this.btn_13;
      } else if ("rename".equalsIgnoreCase(var1)) {
         return this.btn_14;
      } else if ("delete".equalsIgnoreCase(var1)) {
         return this.btn_15;
      } else if ("copy".equalsIgnoreCase(var1)) {
         return this.btn_21;
      } else if ("choose_path".equalsIgnoreCase(var1)) {
         return this.btn_22;
      } else if ("22_1".equalsIgnoreCase(var1)) {
         return this.btn_22_1;
      } else if ("22_2".equalsIgnoreCase(var1)) {
         return this.btn_22_2;
      } else if ("b23".equalsIgnoreCase(var1)) {
         return this.btn_23;
      } else if ("path_txt".equalsIgnoreCase(var1)) {
         return this.txt_path;
      } else if ("path_csp".equalsIgnoreCase(var1)) {
         return this.scp_path;
      } else if ("cancel".equalsIgnoreCase(var1)) {
         return this.btn_31;
      } else if ("ok".equalsIgnoreCase(var1)) {
         return this.btn_32;
      } else if ("btn_line_sep".equalsIgnoreCase(var1)) {
         return this.buttons_sparator;
      } else if ("buttons_panel".equalsIgnoreCase(var1)) {
         return this.button_panel;
      } else if ("file_list_panel".equalsIgnoreCase(var1)) {
         return this.file_list_panel;
      } else if ("files_title_lbl".equalsIgnoreCase(var1)) {
         return this.lbl_title2;
      } else {
         return (Component)("file_filter_panel".equalsIgnoreCase(var1) ? this.filter_panel : ((IFilterPanelLogic)this.filter_panel).getComponent(var1));
      }
   }
}
