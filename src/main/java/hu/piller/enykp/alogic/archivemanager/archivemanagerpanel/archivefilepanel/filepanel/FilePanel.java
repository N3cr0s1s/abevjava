package hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.archivefilepanel.filepanel;

import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.ArchiveFileBusiness;
import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.ArchiveFileTable;
import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.IFilterPanel;
import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.IFilterPanelLogic;
import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.TableFilterPanel;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.util.base.eventsupport.DefaultEventSupport;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class FilePanel extends JPanel implements IEventSupport, IFilterPanel {
   public static final boolean debugOn = false;
   public static final String COMPONENT_FILES_TBL = "files";
   public static final String COMPONENT_B11_BTN = "select_all";
   public static final String COMPONENT_B12_BTN = "deselect_all";
   public static final String COMPONENT_B13_BTN = "replica";
   public static final String COMPONENT_B14_BTN = "rename";
   public static final String COMPONENT_B15_BTN = "delete";
   public static final String COMPONENT_B21_BTN = "copy";
   public static final String COMPONENT_B22_BTN = "choose_path";
   public static final String COMPONENT_PATH_TXT = "path_txt";
   public static final String COMPONENT_PATH_SCP = "path_csp";
   public static final String COMPONENT_B32_BTN = "ok";
   public static final String COMPONENT_B31_BTN = "cancel";
   public static final String COMPONENT_BSEP_COMP = "btn_line_sep";
   public static final String COMPONENT_BUTTONS_PANEL = "buttons_panel";
   public static final String COMPONENT_FILE_LIST_PANEL = "file_list_panel";
   public static final String COMPONENT_FILE_FILTER_PANEL = "file_filter_panel";
   public static final String COMPONENT_FILES_TITLE_LBL = "files_title_lbl";
   public static final String COMPONENT_SELECTALL_BTN = "btn_select_all";
   public static final String COMPONENT_UNSELECTALL_BTN = "btn_unselect_all";
   public static final String COMPONENT_SELECT_SUM_PANEL = "select_sum_panel";
   public static final String COMPONENT_SELECT_LBL_SUM_PANEL_ALL = "lbl_select_sum_all";
   public static final String COMPONENT_SELECT_LBL_SUM_PANEL_SEL = "lbl_select_sum_sel";
   public static final String COMPONENT_FILE_LIST_SCP = "scp_file_list";
   public static final String COMPONENT_FILE_LIST_LBL = "lbl_file_list";
   private static final long serialVersionUID = 1L;
   private JPanel button_panel = null;
   private JPanel file_list_panel = null;
   private JScrollPane scp_file_list = null;
   private JTable tbl_file_list = null;
   private JPanel first_row_panel = null;
   private JPanel second_row_panel = null;
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
   private Component buttons_sparator;
   private JLabel lbl_title2 = null;
   private JPanel filter_panel;
   private JLabel lbl_file_list = null;
   private JButton btn_select_all = null;
   private JButton btn_unselect_all = null;
   private JPanel check_buttons_panel = null;
   private JPanel select_sum_panel = null;
   private JLabel lbl_select_sum_lbl = null;
   private JLabel lbl_select_sum_sep = null;
   private JLabel lbl_select_sum_all = null;
   private JLabel lbl_select_sum_sel = null;
   private ArchiveFileBusiness f_business;
   private DefaultEventSupport des = new DefaultEventSupport();

   public boolean getVisible() {
      return this.getVisible();
   }

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
      this.showMessage("FilePanel.initialize");
      this.setLayout(new BorderLayout());
      this.setMinimumSize(new Dimension(220, 100));
      this.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
      this.add(this.getFilter_panel(), "North");
      this.add(this.getFile_list_panel(), "Center");
      this.add(this.getButton_panel(), "South");
      this.showMessage("FilePanel.initialize end");
   }

   private void prepare() {
      this.f_business = new ArchiveFileBusiness(this);
   }

   private JPanel getFilter_panel() {
      if (this.filter_panel == null) {
         this.filter_panel = new TableFilterPanel((JTable)null);
      }

      return this.filter_panel;
   }

   private Vector getFilterColumns() {
      Vector var1 = new Vector();

      for(int var2 = 2; var2 < FileBusiness.ID_COLUMN_NAMES.length; ++var2) {
         var1.add(new Integer(var2));
      }

      return var1;
   }

   private JPanel getButton_panel() {
      if (this.button_panel == null) {
         this.button_panel = new JPanel();
         this.button_panel.setLayout(new BoxLayout(this.getButton_panel(), 1));
         this.button_panel.setBorder(BorderFactory.createEtchedBorder(0));
         this.button_panel.add(this.getFirst_row_panel(), (Object)null);
         this.button_panel.add(this.getSecond_row_panel(), (Object)null);
         this.button_panel.add(this.buttons_sparator = Box.createRigidArea(new Dimension(0, 5)), (Object)null);
         this.button_panel.add(this.getThird_row_panel(), (Object)null);
      }

      return this.button_panel;
   }

   private JPanel getFile_list_panel() {
      if (this.file_list_panel == null) {
         this.file_list_panel = new JPanel();
         this.file_list_panel.setLayout(new BorderLayout());
         this.file_list_panel.setBorder(BorderFactory.createEtchedBorder(0));
         this.file_list_panel.add(this.getSelect_sum_panel(), "North");
         this.file_list_panel.add(this.getScp_file_list(), "Center");
         this.file_list_panel.add(this.getCheck_buttons_panel(), "South");
      }

      return this.file_list_panel;
   }

   private JScrollPane getScp_file_list() {
      if (this.scp_file_list == null) {
         this.scp_file_list = new JScrollPane();
         this.scp_file_list.setViewportView(this.getTbl_file_list());
         this.scp_file_list.setVerticalScrollBarPolicy(20);
         this.scp_file_list.setHorizontalScrollBarPolicy(30);
      }

      return this.scp_file_list;
   }

   public JTable getTbl_file_list() {
      if (this.tbl_file_list == null) {
         this.tbl_file_list = new ArchiveFileTable();
         this.tbl_file_list.setName("tbl_file_list");
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
         this.first_row_panel.setAlignmentX(0.0F);
      }

      return this.first_row_panel;
   }

   private JPanel getSecond_row_panel() {
      if (this.second_row_panel == null) {
         this.second_row_panel = new JPanel();
         this.second_row_panel.setLayout(new BoxLayout(this.getSecond_row_panel(), 0));
         this.second_row_panel.setAlignmentX(0.0F);
         this.second_row_panel.add(this.getBtn_21(), (Object)null);
         this.second_row_panel.add(this.getScp_path(), (Object)null);
         this.second_row_panel.add(this.getBtn_22(), (Object)null);
      }

      return this.second_row_panel;
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
         this.btn_11.setAlignmentX(0.0F);
      }

      return this.btn_11;
   }

   private JButton getBtn_12() {
      if (this.btn_12 == null) {
         this.btn_12 = new JButton();
         this.btn_12.setText("12");
         this.btn_12.setAlignmentX(0.0F);
      }

      return this.btn_12;
   }

   private JButton getBtn_13() {
      if (this.btn_13 == null) {
         this.btn_13 = new JButton();
         this.btn_13.setText("13");
         this.btn_13.setAlignmentX(0.0F);
      }

      return this.btn_13;
   }

   private JButton getBtn_14() {
      if (this.btn_14 == null) {
         this.btn_14 = new JButton();
         this.btn_14.setText("14");
         this.btn_14.setAlignmentX(0.0F);
      }

      return this.btn_14;
   }

   private JButton getBtn_15() {
      if (this.btn_15 == null) {
         this.btn_15 = new JButton();
         this.btn_15.setText("15");
         this.btn_15.setAlignmentX(0.0F);
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
         this.btn_21.setAlignmentX(0.0F);
         this.btn_21.setAlignmentY(0.0F);
         this.btn_21.setMaximumSize(new Dimension(100, Integer.MAX_VALUE));
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
         this.btn_22.setMaximumSize(new Dimension(70, Integer.MAX_VALUE));
      }

      return this.btn_22;
   }

   public ArchiveFileBusiness getBusiness() {
      return this.f_business;
   }

   private JPanel getCheck_buttons_panel() {
      if (this.check_buttons_panel == null) {
         this.check_buttons_panel = new JPanel();
         this.check_buttons_panel.setLayout(new BoxLayout(this.getCheck_buttons_panel(), 0));
         this.check_buttons_panel.add(this.getSelectAllBtn(), (Object)null);
         this.check_buttons_panel.add(this.getUnSelectAllBtn(), (Object)null);
         this.check_buttons_panel.add(Box.createGlue());
         this.check_buttons_panel.setAlignmentX(0.0F);
      }

      return this.check_buttons_panel;
   }

   private JButton getSelectAllBtn() {
      if (this.btn_select_all == null) {
         this.btn_select_all = new JButton();
         this.btn_select_all.setText("Mindet");
         this.btn_select_all.setToolTipText("Mindet kijelöli");
         this.btn_select_all.setPreferredSize(new Dimension(300, 30));
      }

      return this.btn_select_all;
   }

   private JButton getUnSelectAllBtn() {
      if (this.btn_unselect_all == null) {
         this.btn_unselect_all = new JButton();
         this.btn_unselect_all.setText("Egyet sem");
         this.btn_unselect_all.setToolTipText("Kijelölések megszüntetése");
         this.btn_unselect_all.setPreferredSize(new Dimension(300, 30));
      }

      return this.btn_unselect_all;
   }

   private JPanel getSelect_sum_panel() {
      if (this.select_sum_panel == null) {
         this.select_sum_panel = new JPanel();
         this.select_sum_panel.setLayout(new BoxLayout(this.getSelect_sum_panel(), 0));
         this.select_sum_panel.setOpaque(true);
         this.select_sum_panel.setBackground(GuiUtil.getModifiedBGColor());
         this.select_sum_panel.setBorder(BorderFactory.createEtchedBorder(0));
         this.select_sum_panel.add(this.get_lbl_file_list(), (Object)null);
         this.select_sum_panel.add(Box.createGlue(), (Object)null);
         this.select_sum_panel.add(this.get_lbl_select_sum_lbl("Dialog", 2, GuiUtil.getCommonFontSize()), (Object)null);
         this.select_sum_panel.add(this.get_lbl_select_sum_all("Dialog", 2, GuiUtil.getCommonFontSize()), (Object)null);
         this.select_sum_panel.add(this.get_lbl_select_sum_sep("Dialog", 2, GuiUtil.getCommonFontSize()), (Object)null);
         this.select_sum_panel.add(this.get_lbl_select_sum_sel("Dialog", 2, GuiUtil.getCommonFontSize()), (Object)null);
      }

      return this.select_sum_panel;
   }

   private JLabel get_lbl_file_list() {
      if (this.lbl_file_list == null) {
         this.lbl_file_list = new JLabel();
         this.lbl_file_list.setText("Állományok");
         this.lbl_file_list.setBackground(GuiUtil.getModifiedBGColor());
         this.lbl_file_list.setFont(new Font("Dialog", 2, GuiUtil.getCommonFontSize()));
         this.lbl_file_list.setOpaque(true);
      }

      return this.lbl_file_list;
   }

   private JLabel get_lbl_select_sum_lbl(String var1, int var2, int var3) {
      if (this.lbl_select_sum_lbl == null) {
         this.lbl_select_sum_lbl = new JLabel();
         this.lbl_select_sum_lbl.setText("Összes/Kijelölt: ");
         this.lbl_select_sum_lbl.setBackground(GuiUtil.getModifiedBGColor());
         this.lbl_select_sum_lbl.setFont(new Font(var1, var2, var3));
         this.lbl_select_sum_lbl.setOpaque(true);
      }

      return this.lbl_select_sum_lbl;
   }

   private JLabel get_lbl_select_sum_all(String var1, int var2, int var3) {
      if (this.lbl_select_sum_all == null) {
         this.lbl_select_sum_all = new JLabel();
         this.lbl_select_sum_all.setText("0");
         this.lbl_select_sum_all.setHorizontalAlignment(4);
         this.lbl_select_sum_all.setMinimumSize(new Dimension(GuiUtil.getW(this.lbl_select_sum_all, "WW"), GuiUtil.getCommonItemHeight() + 2));
         this.lbl_select_sum_all.setPreferredSize(this.lbl_select_sum_all.getMinimumSize());
         this.lbl_select_sum_all.setBackground(GuiUtil.getModifiedBGColor());
         this.lbl_select_sum_all.setFont(new Font(var1, var2, var3));
         this.lbl_select_sum_all.setOpaque(true);
      }

      return this.lbl_select_sum_all;
   }

   private JLabel get_lbl_select_sum_sep(String var1, int var2, int var3) {
      if (this.lbl_select_sum_sep == null) {
         this.lbl_select_sum_sep = new JLabel();
         this.lbl_select_sum_sep.setText(" / ");
         this.lbl_select_sum_sep.setBackground(GuiUtil.getModifiedBGColor());
         this.lbl_select_sum_sep.setFont(new Font(var1, var2, var3));
         this.lbl_select_sum_sep.setOpaque(true);
      }

      return this.lbl_select_sum_sep;
   }

   private JLabel get_lbl_select_sum_sel(String var1, int var2, int var3) {
      if (this.lbl_select_sum_sel == null) {
         this.lbl_select_sum_sel = new JLabel();
         this.lbl_select_sum_sel.setText("0");
         this.lbl_select_sum_sel.setHorizontalAlignment(2);
         this.lbl_select_sum_sel.setMinimumSize(new Dimension(GuiUtil.getW(this.lbl_select_sum_all, "WW"), GuiUtil.getCommonItemHeight() + 2));
         this.lbl_select_sum_sel.setPreferredSize(this.lbl_select_sum_sel.getMinimumSize());
         this.lbl_select_sum_sel.setBackground(GuiUtil.getModifiedBGColor());
         this.lbl_select_sum_sel.setFont(new Font(var1, var2, var3));
         this.lbl_select_sum_sel.setOpaque(true);
      }

      return this.lbl_select_sum_sel;
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
      } else if ("file_filter_panel".equalsIgnoreCase(var1)) {
         return this.filter_panel;
      } else if ("btn_select_all".equalsIgnoreCase(var1)) {
         return this.btn_select_all;
      } else if ("btn_unselect_all".equalsIgnoreCase(var1)) {
         return this.btn_unselect_all;
      } else if ("scp_file_list".equalsIgnoreCase(var1)) {
         return this.scp_file_list;
      } else if ("select_sum_panel".equalsIgnoreCase(var1)) {
         return this.select_sum_panel;
      } else if ("select_sum_panel".equalsIgnoreCase(var1)) {
         return this.select_sum_panel;
      } else if ("lbl_select_sum_all".equalsIgnoreCase(var1)) {
         return this.lbl_select_sum_all;
      } else if ("lbl_select_sum_sel".equalsIgnoreCase(var1)) {
         return this.lbl_select_sum_sel;
      } else {
         return (Component)("lbl_file_list".equalsIgnoreCase(var1) ? this.lbl_file_list : ((IFilterPanelLogic)this.filter_panel).getComponent(var1));
      }
   }

   private void showMessage(String var1) {
   }
}
