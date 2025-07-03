package hu.piller.enykp.alogic.filepanels.filepanel.filterpanel;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.util.base.eventsupport.DefaultEventSupport;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TableFilterPanel extends JPanel implements IEventSupport, IFilterPanel, IFilterPanelLogic {
   private IFilterPanelBusiness filter_business = null;
   private JPanel filter_buttons_panel = null;
   private JPanel filter_panel = null;
   private JPanel file_filter_panel = null;
   protected JTableFilter tableFilter;
   protected JButton btn_clear_filters = null;
   private JLabel lbl_file_filter = null;
   protected JLabel lbl_title = null;
   private JScrollPane scp_file_filter = null;
   private JLabel lbl_toggle_filter = null;
   private JList lst_file_filter = null;
   private DefaultEventSupport des = new DefaultEventSupport();

   public TableFilterPanel(JTable var1) {
      this.initialize();
      this.prepare();
   }

   private void initialize() {
      this.build();
   }

   private void build() {
      this.setLayout(new BorderLayout());
      this.add(this.getFilter_panel(), "Center");
      this.add(this.getFilter_buttons_panel(), "South");
   }

   private void prepare() {
      this.filter_business = new TableFilterPanelBusiness(this);
   }

   public void destroy() {
      this.filter_business = null;
   }

   public IFilterPanelBusiness getBusinessHandler() {
      return this.filter_business;
   }

   private JPanel getFilter_panel() {
      if (this.filter_panel == null) {
         this.filter_panel = new JPanel();
         byte var1 = 110;
         GridBagConstraints var2 = new GridBagConstraints();
         var2.gridx = 3;
         var2.fill = 2;
         var2.gridy = 0;
         var2.weightx = 1.0D;
         GridBagConstraints var3 = new GridBagConstraints();
         var3.gridx = 4;
         var3.anchor = 13;
         var3.fill = 3;
         var3.gridwidth = 1;
         var3.gridy = 0;
         GridBagConstraints var4 = new GridBagConstraints();
         var4.gridy = 3;
         var4.gridx = 0;
         var4.fill = 1;
         var4.anchor = 18;
         var4.weightx = 1.0D;
         var4.weighty = 1.0D;
         var4.gridwidth = 5;
         var4.insets = new Insets(5, 5, 5, 5);
         GridBagConstraints var5 = new GridBagConstraints();
         var5.gridy = 1;
         var5.gridx = 0;
         var5.fill = 1;
         var5.anchor = 18;
         var5.weightx = 1.0D;
         var5.weighty = 1.0D;
         var5.gridwidth = 5;
         var5.insets = new Insets(5, 5, 5, 5);
         GridBagConstraints var6 = new GridBagConstraints();
         var6.gridx = 0;
         var6.anchor = 18;
         var6.insets = new Insets(5, 5, 5, 0);
         var6.gridy = 2;
         this.lbl_file_filter = new JLabel();
         this.lbl_file_filter.setText("Állomány szűrők");
         this.lbl_file_filter.setMinimumSize(new Dimension(var1, GuiUtil.getCommonItemHeight() + 2));
         GridBagConstraints var7 = new GridBagConstraints();
         var7.gridx = 0;
         var7.anchor = 17;
         var7.fill = 2;
         var7.gridwidth = 4;
         var7.gridy = 0;
         this.lbl_title = new JLabel();
         this.lbl_title.setName("label0");
         this.lbl_title.setText("Szűrési feltételek");
         this.lbl_title.setFont(new Font("Dialog", 2, Math.max(18, GuiUtil.getCommonFontSize())));
         this.lbl_title.setBackground(GuiUtil.getModifiedBGColor());
         this.lbl_title.setOpaque(true);
         this.lbl_title.setMinimumSize(new Dimension(var1, GuiUtil.getCommonItemHeight() + 4));
         this.filter_panel.setLayout(new GridBagLayout());
         this.filter_panel.setBorder(BorderFactory.createEtchedBorder(0));
         this.filter_panel.setAlignmentX(0.0F);
         this.filter_panel.add(this.getTableFilter(), var5);
         this.filter_panel.add(this.lbl_file_filter, var6);
         this.filter_panel.add(this.getScp_file_filter(), var4);
         this.filter_panel.add(this.lbl_title, var7);
         this.filter_panel.add(this.getLbl_toggle_filter(), var3);
         this.filter_panel.add(Box.createGlue(), var2);
      }

      return this.filter_panel;
   }

   protected JTableFilter getTableFilter() {
      if (this.tableFilter == null) {
         this.tableFilter = new JTableFilter();
         this.tableFilter.setStatusLabel(this.lbl_title);
      }

      return this.tableFilter;
   }

   private JScrollPane getScp_file_filter() {
      if (this.scp_file_filter == null) {
         this.scp_file_filter = new JScrollPane();
         this.scp_file_filter.setViewportView(this.getLst_file_filter());
         this.scp_file_filter.setMinimumSize(new Dimension(150, (int)(1.5D * (double)GuiUtil.getCommonItemHeight())));
         this.scp_file_filter.setPreferredSize(this.scp_file_filter.getMinimumSize());
      }

      return this.scp_file_filter;
   }

   private JList getLst_file_filter() {
      if (this.lst_file_filter == null) {
         this.lst_file_filter = new JList();
      }

      return this.lst_file_filter;
   }

   private JLabel getLbl_toggle_filter() {
      if (this.lbl_toggle_filter == null) {
         this.lbl_toggle_filter = new JLabel();
         this.lbl_toggle_filter.setOpaque(true);
         this.lbl_toggle_filter.setBackground(GuiUtil.getModifiedBGColor());
         this.lbl_toggle_filter.setText("V");
      }

      return this.lbl_toggle_filter;
   }

   private JButton getBtn_clear_filters() {
      if (this.btn_clear_filters == null) {
         this.btn_clear_filters = new JButton();
         this.btn_clear_filters.setText("Szűrési feltételek törlése");
         this.btn_clear_filters.setAlignmentX(0.5F);
         this.btn_clear_filters.setToolTipText("Szűrési feltételek törlése");
      }

      return this.btn_clear_filters;
   }

   private JPanel getFilter_buttons_panel() {
      if (this.filter_buttons_panel == null) {
         this.filter_buttons_panel = new JPanel();
         this.filter_buttons_panel.add(this.getBtn_clear_filters(), (Object)null);
      }

      return this.filter_buttons_panel;
   }

   private JPanel getFile_filter_panel() {
      if (this.file_filter_panel == null) {
         this.file_filter_panel = new JPanel();
         this.file_filter_panel.setLayout(new BorderLayout());
         this.file_filter_panel.setAlignmentX(0.0F);
         this.file_filter_panel.add(this.lbl_file_filter, "North");
         this.file_filter_panel.add(this.getScp_file_filter(), "Center");
      }

      return this.file_filter_panel;
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

   public Component getComponent(String var1) {
      if ("file_filter".equalsIgnoreCase(var1)) {
         return this.lst_file_filter;
      } else if ("filter_title_lbl".equalsIgnoreCase(var1)) {
         return this.lbl_title;
      } else if ("file_filters_lbl".equalsIgnoreCase(var1)) {
         return this.lbl_file_filter;
      } else if ("file_filters_scp".equalsIgnoreCase(var1)) {
         return this.scp_file_filter;
      } else if ("file_filters_toggle_btn".equalsIgnoreCase(var1)) {
         return this.lbl_toggle_filter;
      } else if ("clear_filters".equalsIgnoreCase(var1)) {
         return this.btn_clear_filters;
      } else if ("table_filter".equalsIgnoreCase(var1)) {
         return this.tableFilter;
      } else {
         return "filter_btn_panel".equalsIgnoreCase(var1) ? this.filter_buttons_panel : null;
      }
   }
}
