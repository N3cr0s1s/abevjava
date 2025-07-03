package hu.piller.enykp.alogic.upgrademanager_v2_0.gui.orgselector;

import hu.piller.enykp.alogic.masterdata.sync.ui.entityfilter.SizeableCBRenderer;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.orghandler.SuccessorException;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.proxy.ProxySettings;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class JOrgSelectorDialog extends JDialog implements ActionListener {
   private JCheckBox chk_save;
   private JCheckBox chk_noshow;
   private JButton btn_select_all;
   private JButton btn_deselect_all;
   private JButton btn_continue;
   private JButton btn_close;
   private JTable tbl_selection;
   private Collection<String> selected;

   public JOrgSelectorDialog(JFrame var1) {
      super(var1);
      this.init();
   }

   public boolean hasSelected() {
      return this.selected != null && this.selected.size() > 0;
   }

   public String[] getSelected() {
      return this.hasSelected() ? (String[])this.selected.toArray(new String[this.selected.size()]) : null;
   }

   public void actionPerformed(ActionEvent var1) {
      int var3;
      if ("continue".equals(var1.getActionCommand())) {
         if (this.getTblSelection() != null && this.getTblSelection().getCellEditor() != null) {
            this.getTblSelection().getCellEditor().stopCellEditing();
         }

         this.selected = new TreeSet();
         OrgSelectorTableModel var2 = (OrgSelectorTableModel)this.getTblSelection().getModel();

         for(var3 = 0; var3 < var2.getRowCount(); ++var3) {
            if (var2.getValueAt(var3, 1).equals(Boolean.TRUE)) {
               String var4 = (String)((Vector)var2.getDataVector().elementAt(var3)).elementAt(0);
               this.selected.add(var4);
            }
         }

         this.setOrgs((String[])this.selected.toArray(new String[this.selected.size()]));
         this.dispose();
      } else if ("close".equals(var1.getActionCommand())) {
         this.dispose();
      } else {
         TableModel var5;
         if ("all".equals(var1.getActionCommand())) {
            if (this.getTblSelection() != null && this.getTblSelection().getCellEditor() != null) {
               this.getTblSelection().getCellEditor().stopCellEditing();
            }

            var5 = this.getTblSelection().getModel();

            for(var3 = 0; var3 < var5.getRowCount(); ++var3) {
               var5.setValueAt(Boolean.TRUE, var3, 1);
            }
         } else if ("none".equals(var1.getActionCommand())) {
            if (this.getTblSelection() != null && this.getTblSelection().getCellEditor() != null) {
               this.getTblSelection().getCellEditor().stopCellEditing();
            }

            var5 = this.getTblSelection().getModel();

            for(var3 = 0; var3 < var5.getRowCount(); ++var3) {
               var5.setValueAt(Boolean.FALSE, var3, 1);
            }
         } else if ("save".equals(var1.getActionCommand())) {
            this.setSave(this.chk_save.isSelected());
         } else if ("noshow".equals(var1.getActionCommand())) {
            this.setNoshow(this.chk_noshow.isSelected());
            ProxySettings.getInstance().reset();
            GuiUtil.showMessageDialog(this, "A dialógusablak megjelenítését engedélyezheti/letilthatja a\nSzerviz->Beállítások->Frissítés beállítópanelen is!", "Figyelmeztetés", 1);
         }
      }

   }

   private void init() {
      this.setTitle("Szervezet választás - frissítés");
      this.setName("JOrgSelectorDialog");
      this.setResizable(true);
      this.setModal(true);
      this.getContentPane().setLayout(new GridBagLayout());
      if (this.getOwner() != null) {
         this.setLocationRelativeTo(this.getOwner());
      }

      GridBagConstraints var1 = new GridBagConstraints();
      var1.anchor = 21;
      var1.fill = 2;
      var1.ipadx = 0;
      var1.ipady = 0;
      var1.weightx = 0.5D;
      var1.weighty = 0.5D;
      JPanel var2 = new JPanel();
      var2.setLayout(new GridBagLayout());
      GridBagConstraints var3 = new GridBagConstraints();
      var3.anchor = 21;
      var3.ipadx = 0;
      var3.ipady = 0;
      var3.gridx = 0;
      var3.gridy = 0;
      var3.weightx = 0.0D;
      var3.weighty = 0.0D;
      var2.add(this.getChkSave(), var3);
      var3.gridx = 1;
      var3.gridy = 0;
      var2.add(new JLabel("Kijelölés(ek) megjegyzése"), var3);
      var3.gridx = 0;
      var3.gridy = 1;
      var2.add(this.getChkNoshow(), var3);
      var3.gridx = 1;
      var3.gridy = 1;
      var2.add(new JLabel("Többet ne jelenjen meg ez az ablak"), var3);
      var2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Beállítások"));
      JScrollPane var4 = new JScrollPane(this.getTblSelection(), 20, 31);
      var4.setPreferredSize(new Dimension(GuiUtil.getTableWidthByColumns(this.tbl_selection), 8 * (GuiUtil.getCommonItemHeight() + 2)));
      var3.gridx = 0;
      var3.gridy = 2;
      var3.gridwidth = 2;
      var3.insets = new Insets(3, 5, 3, 5);
      var2.add(var4, var3);
      var3.gridx = 0;
      var3.gridy = 3;
      JPanel var5 = new JPanel(new BorderLayout());
      Box var6 = Box.createHorizontalBox();
      var6.add(Box.createGlue());
      var6.add(this.getBtnSelectAll());
      var6.add(Box.createGlue());
      var6.add(this.getBtnDeselectAll());
      var6.add(Box.createGlue());
      var5.add(var6);
      var5.setMinimumSize(new Dimension(GuiUtil.getTableWidthByColumns(this.tbl_selection), GuiUtil.getCommonItemHeight() + 8));
      var5.setPreferredSize(var5.getMinimumSize());
      var5.setSize(var5.getMinimumSize());
      var2.add(var5, var3);
      var1.gridx = 0;
      var1.gridy = 0;
      var1.gridwidth = 2;
      var1.insets = new Insets(5, 5, 5, 5);
      this.getContentPane().add(var2, var1);
      var1.gridx = 0;
      var1.gridy = 1;
      var1.gridwidth = 1;
      var1.insets = new Insets(5, 5, 5, 5);
      this.getContentPane().add(this.getBtnContinue(), var1);
      var1.gridx = 1;
      var1.gridy = 1;
      var1.insets = new Insets(5, 5, 5, 5);
      this.getContentPane().add(this.getBtnClose(), var1);
   }

   private JTable getTblSelection() {
      if (this.tbl_selection == null) {
         if (!this.isSave()) {
            this.tbl_selection = new JTable(new OrgSelectorTableModel(OrgInfo.getInstance().getOrgIds()));
         } else {
            TreeSet var1 = new TreeSet();
            String[] var2 = this.getOrgs();
            String[] var3 = var2;
            int var4 = var2.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               String var6 = var3[var5];
               if (OrgInfo.getInstance().hasSuccessor(var6)) {
                  try {
                     var6 = OrgInfo.getInstance().getSuccessorOrgId(var6);
                  } catch (SuccessorException var8) {
                     var8.printStackTrace();
                  }
               }

               var1.add(var6);
            }

            this.tbl_selection = new JTable(new OrgSelectorTableModel(var1));
         }

         GuiUtil.setTableColWidth(this.tbl_selection);
         if (GuiUtil.modGui()) {
            this.tbl_selection.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
         }

         this.tbl_selection.getColumnModel().getColumn(this.tbl_selection.getColumnModel().getColumnCount() - 1).setCellEditor(new SizeableCBRenderer());
         this.tbl_selection.getColumnModel().getColumn(this.tbl_selection.getColumnModel().getColumnCount() - 1).setCellRenderer(new SizeableCBRenderer());
      }

      return this.tbl_selection;
   }

   private JButton getBtnContinue() {
      if (this.btn_continue == null) {
         this.btn_continue = new JButton();
         this.btn_continue.setActionCommand("continue");
         this.btn_continue.setText("Tovább");
         this.btn_continue.addActionListener(this);
         int var1 = GuiUtil.getW(this.btn_continue, this.btn_continue.getText());
         this.btn_continue.setMinimumSize(new Dimension(var1, GuiUtil.getCommonItemHeight() + 4));
         this.btn_continue.setPreferredSize(this.btn_continue.getMinimumSize());
         this.btn_continue.setMaximumSize(this.btn_continue.getMinimumSize());
      }

      return this.btn_continue;
   }

   private JButton getBtnClose() {
      if (this.btn_close == null) {
         this.btn_close = new JButton();
         this.btn_close.setActionCommand("close");
         this.btn_close.setText("Bezár");
         this.btn_close.addActionListener(this);
         int var1 = GuiUtil.getW(this.btn_close, this.btn_close.getText());
         this.btn_close.setMinimumSize(new Dimension(var1, GuiUtil.getCommonItemHeight() + 4));
         this.btn_close.setPreferredSize(this.btn_close.getMinimumSize());
         this.btn_close.setMaximumSize(this.btn_close.getMinimumSize());
      }

      return this.btn_close;
   }

   private JCheckBox getChkSave() {
      if (this.chk_save == null) {
         this.chk_save = GuiUtil.getANYKCheckBox();
         this.chk_save.setActionCommand("save");
         this.chk_save.addActionListener(this);
         this.chk_save.setSelected(this.isSave());
      }

      return this.chk_save;
   }

   private JCheckBox getChkNoshow() {
      if (this.chk_noshow == null) {
         this.chk_noshow = GuiUtil.getANYKCheckBox();
         this.chk_noshow.setActionCommand("noshow");
         this.chk_noshow.addActionListener(this);
         this.chk_noshow.setSelected(this.isNoshow());
      }

      return this.chk_noshow;
   }

   private JButton getBtnSelectAll() {
      if (this.btn_select_all == null) {
         this.btn_select_all = new JButton();
         this.btn_select_all.setActionCommand("all");
         this.btn_select_all.setText("Mind");
         this.btn_select_all.addActionListener(this);
         int var1 = GuiUtil.getW(this.btn_select_all, this.btn_select_all.getText());
         this.btn_select_all.setMinimumSize(new Dimension(var1, GuiUtil.getCommonItemHeight() + 4));
         this.btn_select_all.setPreferredSize(this.btn_select_all.getMinimumSize());
         this.btn_select_all.setMaximumSize(this.btn_select_all.getMinimumSize());
      }

      return this.btn_select_all;
   }

   private JButton getBtnDeselectAll() {
      if (this.btn_deselect_all == null) {
         this.btn_deselect_all = new JButton();
         this.btn_deselect_all.setActionCommand("none");
         this.btn_deselect_all.setText("Egyik sem");
         this.btn_deselect_all.addActionListener(this);
         int var1 = GuiUtil.getW(this.btn_deselect_all, this.btn_deselect_all.getText());
         this.btn_deselect_all.setMinimumSize(new Dimension(var1, GuiUtil.getCommonItemHeight() + 4));
         this.btn_deselect_all.setPreferredSize(this.btn_deselect_all.getMinimumSize());
         this.btn_deselect_all.setMaximumSize(this.btn_deselect_all.getMinimumSize());
      }

      return this.btn_deselect_all;
   }

   public void setOrgs(String[] var1) {
      if (var1 != null) {
         int var2 = 0;
         StringBuilder var3 = new StringBuilder();
         String[] var4 = var1;
         int var5 = var1.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            var3.append(var7);
            if (var2 < var1.length - 1) {
               var3.append(";");
            }

            ++var2;
         }

         SettingsStore.getInstance().set("upgrade", "orgs", var3.toString());
      }

   }

   public String[] getOrgs() {
      String[] var1 = new String[0];
      String var2 = SettingsStore.getInstance().get("upgrade", "orgs");
      if (var2 != null && var2.trim().length() > 0) {
         var1 = var2.split(";");
         Arrays.sort(var1);
      }

      ArrayList var3 = new ArrayList();

      for(int var4 = 0; var4 < var1.length; ++var4) {
         try {
            if (OrgInfo.getInstance().hasSuccessor(var1[var4])) {
               try {
                  var1[var4] = OrgInfo.getInstance().getSuccessorOrgId(var1[var4]);
               } catch (SuccessorException var6) {
                  var6.printStackTrace();
               }
            }

            var3.add(var1[var4]);
         } catch (IllegalArgumentException var7) {
            Tools.eLog(var7, 0);
         }
      }

      return (String[])var3.toArray(new String[var3.size()]);
   }

   public void setSave(boolean var1) {
      SettingsStore.getInstance().set("upgrade", "save", var1 ? "true" : "");
   }

   public boolean isSave() {
      String var1 = SettingsStore.getInstance().get("upgrade", "save");
      return "true".equals(var1);
   }

   public void setNoshow(boolean var1) {
      SettingsStore.getInstance().set("upgrade", "noshow", var1 ? "true" : "");
   }

   public boolean isNoshow() {
      String var1 = SettingsStore.getInstance().get("upgrade", "noshow");
      return "true".equals(var1);
   }
}
