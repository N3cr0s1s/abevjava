package hu.piller.enykp.alogic.upgrademanager_v2_0.gui;

import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.TableFilterPanel;
import hu.piller.enykp.alogic.masterdata.sync.ui.entityfilter.SizeableCBRenderer;
import hu.piller.enykp.alogic.upgrademanager_v2_0.gui.component.UpgradeTableFilterPanel;
import hu.piller.enykp.alogic.upgrademanager_v2_0.gui.orgselector.JOrgSelectorDialog;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.tabledialog.TooltipTableHeader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;

public class JUpgradeFormDialog extends JDialog {
   private static final String ERR_OVERLOADED_SERVERS = "Túlterheltek a frissítés közzétételi szerverek, próbálkozzon később!";
   private static final int MAX_WAITTIME_FOR_ALL_ORGS = 600;
   private JTable tbl_updatable;
   private JTable tbl_new;
   private JButton btn_select_all;
   private JButton btn_deselect_all;
   private JButton btn_component_info;
   private JButton btn_install;
   private JButton btn_close;
   private JLabel lbl_msg;
   private JTabbedPane pane_lists;
   private JCheckBox box_framework;
   private UpgradeFormController controller;
   private JPanel frameworkPanel;
   private CountDownLatch latch;
   int mode_new = 1;
   int sel_col_new = -1;
   int mode_upg = 1;
   int sel_col_upg = -1;

   public JUpgradeFormDialog() {
      this.init();
   }

   public JUpgradeFormDialog(Frame var1) {
      super(var1);
      Thread var2 = new Thread(new Runnable() {
         public void run() {
            JUpgradeFormDialog.this.init();
         }
      });
      var2.start();
   }

   private void initController(String[] var1) {
      try {
         this.controller = new UpgradeFormController(this, var1);
      } finally {
         this.latch.countDown();
      }

   }

   private void init() {
      this.setTitle("Frissítések és új összetevők telepítése");
      this.setResizable(true);
      this.setModal(true);
      this.latch = new CountDownLatch(1);
      final JOrgSelectorDialog var1 = new JOrgSelectorDialog(MainFrame.thisinstance);
      if (!var1.isNoshow()) {
         Point var2 = this.getLocation();
         var1.setLocation(new Point(var2.x + 160, var2.y + 90));
         var1.pack();

         try {
            SwingUtilities.invokeAndWait(new Runnable() {
               public void run() {
                  var1.setVisible(true);
               }
            });
         } catch (Exception var12) {
            var12.printStackTrace();
            return;
         }

         if (!var1.hasSelected()) {
            return;
         }
      }

      final JUpgradeStartInfoPanel var15 = new JUpgradeStartInfoPanel("Kapcsolat létrehozása az összetevő frissítéseket közzétevő szervezetekkel.", this, 0);
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            var15.setVisible(true);
         }
      });
      Thread var3 = new Thread(new Runnable() {
         public void run() {
            if (!var1.isNoshow()) {
               JUpgradeFormDialog.this.initController(var1.getSelected());
            } else {
               JUpgradeFormDialog.this.initController(var1.getOrgs());
            }

         }
      });
      var3.start();
      boolean var4 = true;

      label142: {
         try {
            var4 = this.latch.await(600L, TimeUnit.SECONDS);
            break label142;
         } catch (InterruptedException var13) {
            Thread.currentThread().interrupt();
         } finally {
            var15.close();
            if (!var4) {
               ErrorList.getInstance().writeError("UPGRADE_FORM", "Túlterheltek a frissítés közzétételi szerverek, próbálkozzon később!", (Exception)null, (Object)null);
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Túlterheltek a frissítés közzétételi szerverek, próbálkozzon később!", "Hálózati hiba", 0);
               return;
            }

         }

         return;
      }

      int var6;
      if (this.controller.getOrgsNotConnected().length != 0) {
         StringBuilder var5 = new StringBuilder("Sikertelen kapcsolódás a következő szervezet(ek)hez:\n");

         for(var6 = 0; var6 < this.controller.getOrgsNotConnected().length; ++var6) {
            var5.append(this.controller.getOrgsNotConnected()[var6]);
            if (var6 < this.controller.getOrgsNotConnected().length - 1) {
               var5.append(", ");
            }
         }

         GuiUtil.showMessageDialog(this, var5.toString(), "Hiba", 0);
      }

      this.controller.initPanes();
      this.setLayout(new BorderLayout());
      JPanel var16 = new JPanel(new BorderLayout());
      var16.add(this.getVersionDataTabbedPane(), "Center");
      var16.add(this.createControlButtonsPanel(), "East");
      this.add(var16, "Center");
      this.createFrameworkStatusPanel();
      this.add(this.frameworkPanel, "South");
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            JUpgradeFormDialog.this.setVisible(true);
         }
      });
      var6 = (int)(this.frameworkPanel.getSize().getWidth() + (double)GuiUtil.getW("WWEgyik semWW"));
      int var7 = GuiUtil.getW(new JButton(), "WEgyik semW");

      for(int var8 = 0; var8 < 5; ++var8) {
         var7 += this.tbl_new.getColumnModel().getColumn(var8).getWidth();
      }

      var7 = Math.min(GuiUtil.getScreenW(), var7);
      Dimension var17 = new Dimension(Math.max(var7, var6), 20 * GuiUtil.getCommonItemHeight());
      this.setMinimumSize(var17);
      this.setSize(var17);
      if (this.getOwner() != null) {
         this.setLocationRelativeTo(this.getOwner());
      }

   }

   public JTable getNewTable() {
      if (this.tbl_new == null) {
         UpgradeTableModel var1 = new UpgradeTableModel();
         this.tbl_new = new JTable(var1);
         this.tbl_new.setTableHeader(new TooltipTableHeader(this.tbl_new.getColumnModel()));
         JUpgradeFormDialog.UpgradeTableCellRenderer var2 = new JUpgradeFormDialog.UpgradeTableCellRenderer(this.tbl_new.getTableHeader().getDefaultRenderer(), 1);
         this.tbl_new.getTableHeader().setDefaultRenderer(var2);
         this.tbl_new.getTableHeader().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent var1) {
               for(int var2 = 0; var2 <= 5; ++var2) {
                  if (var2 != 4) {
                     Rectangle var3 = JUpgradeFormDialog.this.tbl_new.getTableHeader().getHeaderRect(var2);
                     if (var3.contains(var1.getPoint())) {
                        JUpgradeFormDialog.this.sel_col_new = var2;
                        ((UpgradeTableModel)JUpgradeFormDialog.this.tbl_new.getModel()).sortByColumn(var2, JUpgradeFormDialog.this.mode_new);
                        JUpgradeFormDialog var10000 = JUpgradeFormDialog.this;
                        var10000.mode_new *= -1;
                        break;
                     }
                  }
               }

            }
         });
         this.tbl_new.setSelectionMode(0);
         this.tbl_new.setAutoResizeMode(0);
         GuiUtil.setTableColWidth(this.tbl_new);
         this.tbl_new.getColumnModel().getColumn(this.tbl_new.getColumnModel().getColumnCount() - 2).setCellEditor(new SizeableCBRenderer());
         this.tbl_new.getColumnModel().getColumn(this.tbl_new.getColumnModel().getColumnCount() - 2).setCellRenderer(new SizeableCBRenderer());
      }

      return this.tbl_new;
   }

   public JTable getUpgradableTable() {
      if (this.tbl_updatable == null) {
         UpgradeTableModel var1 = new UpgradeTableModel();
         this.tbl_updatable = new JTable(var1);
         this.tbl_updatable.setTableHeader(new TooltipTableHeader(this.tbl_updatable.getColumnModel()));
         JUpgradeFormDialog.UpgradeTableCellRenderer var2 = new JUpgradeFormDialog.UpgradeTableCellRenderer(this.tbl_updatable.getTableHeader().getDefaultRenderer(), 0);
         this.tbl_updatable.getTableHeader().setDefaultRenderer(var2);
         this.tbl_updatable.getTableHeader().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent var1) {
               for(int var2 = 0; var2 <= 5; ++var2) {
                  if (var2 != 4) {
                     Rectangle var3 = JUpgradeFormDialog.this.tbl_updatable.getTableHeader().getHeaderRect(var2);
                     if (var3.contains(var1.getPoint())) {
                        JUpgradeFormDialog.this.sel_col_upg = var2;
                        ((UpgradeTableModel)JUpgradeFormDialog.this.tbl_updatable.getModel()).sortByColumn(var2, JUpgradeFormDialog.this.mode_upg);
                        JUpgradeFormDialog var10000 = JUpgradeFormDialog.this;
                        var10000.mode_upg *= -1;
                        break;
                     }
                  }
               }

            }
         });
         this.tbl_updatable.setSelectionMode(0);
         this.tbl_updatable.setAutoResizeMode(0);
         this.tbl_updatable.getColumnModel().getColumn(this.tbl_updatable.getColumnModel().getColumnCount() - 2).setCellEditor(new SizeableCBRenderer());
         this.tbl_updatable.getColumnModel().getColumn(this.tbl_updatable.getColumnModel().getColumnCount() - 2).setCellRenderer(new SizeableCBRenderer());
      }

      GuiUtil.setTableColWidth(this.tbl_updatable);
      return this.tbl_updatable;
   }

   public JCheckBox getBoxFrame() {
      if (this.box_framework == null) {
         this.box_framework = GuiUtil.getANYKCheckBox();
         this.box_framework.setSelected(false);
      }

      return this.box_framework;
   }

   public JButton getBtnSelectAll() {
      if (this.btn_select_all == null) {
         this.btn_select_all = new JButton("Mind");
         this.btn_select_all.setToolTipText("A táblázatban felsorolt elemek kijelölése telepítésre");
         this.btn_select_all.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                     JUpgradeFormDialog.this.controller.selectAll();
                  }
               });
            }
         });
         Dimension var1 = new Dimension(GuiUtil.getW(new JButton(), "Egyik sem"), GuiUtil.getCommonItemHeight() + 4);
         this.btn_select_all.setMinimumSize(var1);
         this.btn_select_all.setPreferredSize(var1);
      }

      return this.btn_select_all;
   }

   public JButton getBtnDeselectAll() {
      if (this.btn_deselect_all == null) {
         this.btn_deselect_all = new JButton("Egyik sem");
         this.btn_deselect_all.setToolTipText("A táblázatban minden telepítés kijelölés visszavonása");
         this.btn_deselect_all.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                     JUpgradeFormDialog.this.controller.deselectAll();
                  }
               });
            }
         });
         Dimension var1 = new Dimension(GuiUtil.getW(new JButton(), "Egyik sem"), GuiUtil.getCommonItemHeight() + 4);
         this.btn_deselect_all.setMinimumSize(var1);
         this.btn_deselect_all.setPreferredSize(var1);
      }

      return this.btn_deselect_all;
   }

   public JButton getBtnComponentInfo() {
      if (this.btn_component_info == null) {
         this.btn_component_info = new JButton("Leírás");
         this.btn_component_info.setToolTipText("Tájékoztató az aktuális sorban (kék szín) levő elemről");
         this.btn_component_info.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                     JUpgradeFormDialog.this.controller.info();
                  }
               });
            }
         });
         Dimension var1 = new Dimension(GuiUtil.getW(new JButton(), "Egyik sem"), GuiUtil.getCommonItemHeight() + 4);
         this.btn_component_info.setMinimumSize(var1);
         this.btn_component_info.setPreferredSize(var1);
      }

      return this.btn_component_info;
   }

   public JButton getBtnInstall() {
      if (this.btn_install == null) {
         this.btn_install = new JButton("Telepít");
         this.btn_install.setToolTipText("Kijelölt elemek telepítése");
         this.btn_install.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                     JUpgradeFormDialog.this.controller.install();
                  }
               });
            }
         });
         Dimension var1 = new Dimension(GuiUtil.getW(new JButton(), "Egyik sem"), GuiUtil.getCommonItemHeight() + 4);
         this.btn_install.setMinimumSize(var1);
         this.btn_install.setPreferredSize(var1);
      }

      return this.btn_install;
   }

   public JButton getBtnClose() {
      if (this.btn_close == null) {
         this.btn_close = new JButton("Bezár");
         this.btn_close.setToolTipText("Frissítés panel bezárása");
         this.btn_close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               Component var2 = SwingUtilities.getRoot(JUpgradeFormDialog.this);
               if (var2 instanceof Window) {
                  var2.setVisible(false);
               }

            }
         });
         Dimension var1 = new Dimension(GuiUtil.getW(new JButton(), "Egyik sem"), GuiUtil.getCommonItemHeight() + 4);
         this.btn_close.setMinimumSize(var1);
         this.btn_close.setPreferredSize(var1);
      }

      return this.btn_close;
   }

   public JTabbedPane getVersionDataTabbedPane() {
      if (this.pane_lists == null) {
         this.pane_lists = new JTabbedPane();
         Dimension var1 = new Dimension(645, 350);
         this.pane_lists.setMinimumSize(var1);
         this.pane_lists.setPreferredSize(var1);
         this.pane_lists.addTab("Frissítések 1", (Icon)null, this.createUpgradablePanel(), "Az Ön ÁNyK példányában telepített nyomtatványok és segédletek újabb verziói");
         this.pane_lists.setMnemonicAt(0, 49);
         this.pane_lists.addTab("Újdonságok 2", (Icon)null, this.createNewAndUpgradablePanel(), "Az Ön ÁNyK példányára nézve új, és frissített nyomtatványok, segédletek");
         this.pane_lists.setMnemonicAt(1, 50);
      }

      return this.pane_lists;
   }

   public void createFrameworkStatusPanel() {
      if (this.frameworkPanel == null) {
         this.frameworkPanel = new JPanel();
         this.frameworkPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Keretrendszer"));
         this.frameworkPanel.setLayout(new BoxLayout(this.frameworkPanel, 2));
         this.lbl_msg = new JLabel();
         Dimension var1 = new Dimension(GuiUtil.getW(this.lbl_msg, "Publikálták az ÁNyK keretrendszer új változatát. A telepítés ajánlott.") + 40, 2 * (GuiUtil.getCommonItemHeight() + 4));
         this.frameworkPanel.setMinimumSize(var1);
         this.frameworkPanel.setPreferredSize(var1);
         this.frameworkPanel.setSize(var1);
         this.frameworkPanel.add(Box.createHorizontalGlue());
         this.frameworkPanel.add(this.getBoxFrame());
         this.frameworkPanel.add(this.lbl_msg);
         this.frameworkPanel.add(Box.createHorizontalGlue());
      }

      if (this.controller.hasFrameworkUpgrade() && !this.controller.isRestartNeeded()) {
         this.getBoxFrame().setSelected(true);
         if (!this.isFrameworkUpgradeCheckBoxVisible()) {
            this.frameworkPanel.removeAll();
            this.frameworkPanel.add(Box.createHorizontalGlue());
            this.frameworkPanel.add(this.getBoxFrame());
            this.frameworkPanel.add(this.lbl_msg);
            this.frameworkPanel.add(Box.createHorizontalGlue());
         }

         this.lbl_msg.setText("Publikálták az ÁNyK keretrendszer új változatát. A telepítés ajánlott.");
      } else if (this.controller.isRestartNeeded()) {
         this.frameworkPanel.remove(this.getBoxFrame());
         this.getBoxFrame().setSelected(false);
         this.lbl_msg.setText("Az új keretrendszer csak az ÁNyK újraindításával lesz használható.");
      } else {
         this.frameworkPanel.remove(this.getBoxFrame());
         this.lbl_msg.setText("Ön az ÁNyK keretrendszer legújabb változatát használja.");
      }

   }

   private boolean isFrameworkUpgradeCheckBoxVisible() {
      Component[] var1 = this.frameworkPanel.getComponents();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2].equals(this.getBoxFrame())) {
            return true;
         }
      }

      return false;
   }

   private JPanel createUpgradablePanel() {
      Vector var1 = new Vector();
      var1.add(0);
      var1.add(1);
      var1.add(2);
      var1.add(3);
      JPanel var2 = new JPanel();
      Dimension var3 = new Dimension(645, 300);
      var2.setMinimumSize(var3);
      var2.setPreferredSize(var3);
      var2.setLayout(new GridBagLayout());
      GridBagConstraints var4 = new GridBagConstraints();
      var4.anchor = 23;
      var4.fill = 2;
      var4.gridwidth = 1;
      var4.gridheight = 1;
      var4.weightx = 1.0D;
      var4.gridx = 0;
      var4.gridy = 0;
      var2.add(this.getFilterPanel(this.getUpgradableTable(), var1), var4);
      var4.gridy = 1;
      var4.fill = 1;
      var4.weighty = 1.0D;
      var2.add(new JScrollPane(this.getUpgradableTable()), var4);
      return var2;
   }

   private JPanel createNewAndUpgradablePanel() {
      Vector var1 = new Vector();
      var1.add(0);
      var1.add(1);
      var1.add(2);
      var1.add(3);
      JPanel var2 = new JPanel();
      Dimension var3 = new Dimension(645, 300);
      var2.setMinimumSize(var3);
      var2.setPreferredSize(var3);
      var2.setLayout(new GridBagLayout());
      GridBagConstraints var4 = new GridBagConstraints();
      var4.anchor = 23;
      var4.fill = 2;
      var4.gridwidth = 1;
      var4.gridheight = 1;
      var4.weightx = 1.0D;
      var4.gridx = 0;
      var4.gridy = 0;
      var2.add(this.getFilterPanel(this.getNewTable(), var1), var4);
      var4.gridy = 1;
      var4.fill = 1;
      var4.weighty = 1.0D;
      var2.add(new JScrollPane(this.getNewTable()), var4);
      return var2;
   }

   private TableFilterPanel getFilterPanel(JTable var1, Vector var2) {
      UpgradeTableFilterPanel var3 = new UpgradeTableFilterPanel((JTable)null);
      var3.getBusinessHandler().clearFilters();
      var3.getBusinessHandler().setFilterVisibility(true);
      var3.getBusinessHandler().setFileFilterTypeVisibility(false);
      var3.getBusinessHandler().setVisible(false);
      ((JLabel)var3.getComponent("filter_title_lbl")).setText("Szűrési feltételek");
      Integer[] var4 = new Integer[]{0, 0, 0, 0, 1};
      Vector var5 = new Vector();
      Collections.addAll(var5, var4);
      var3.getBusinessHandler().initials(new Object[]{var1, var2, var2.size() + 1, var5});
      return var3;
   }

   private JPanel createControlButtonsPanel() {
      JPanel var1 = new JPanel();
      Dimension var2 = new Dimension(GuiUtil.getW("WWEgyik semWW"), 300);
      var1.setMinimumSize(var2);
      var1.setPreferredSize(var2);
      var1.setLayout(new GridBagLayout());
      GridBagConstraints var3 = new GridBagConstraints();
      var3.fill = 0;
      var3.gridheight = 1;
      var3.gridwidth = 1;
      var3.insets = new Insets(5, 0, 0, 0);
      var3.gridx = 0;
      var3.gridy = 0;
      var1.add(this.getBtnSelectAll(), var3);
      var3.gridy = 1;
      var1.add(this.getBtnDeselectAll(), var3);
      var3.insets = new Insets(30, 0, 0, 0);
      var3.gridy = 2;
      var1.add(this.getBtnComponentInfo(), var3);
      var3.gridy = 3;
      var1.add(this.getBtnInstall(), var3);
      var3.gridy = 4;
      var1.add(this.getBtnClose(), var3);
      return var1;
   }

   private class Arrow implements Icon {
      private boolean descending;
      private int size;
      private int priority;

      public Arrow(boolean var2, int var3, int var4) {
         this.descending = var2;
         this.size = var3;
         this.priority = var4;
      }

      public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
         Color var5 = var1 == null ? Color.GRAY : var1.getBackground();
         int var6 = (int)((double)(this.size / 2) * Math.pow(0.8D, (double)this.priority));
         int var7 = this.descending ? var6 : -var6;
         var4 = var4 + 5 * this.size / 6 + (this.descending ? -var7 : 0);
         int var8 = this.descending ? 1 : -1;
         var2.translate(var3, var4);
         var2.setColor(var5.darker());
         var2.drawLine(var6 / 2, var7, 0, 0);
         var2.drawLine(var6 / 2, var7 + var8, 0, var8);
         var2.setColor(var5.brighter());
         var2.drawLine(var6 / 2, var7, var6, 0);
         var2.drawLine(var6 / 2, var7 + var8, var6, var8);
         if (this.descending) {
            var2.setColor(var5.darker().darker());
         } else {
            var2.setColor(var5.brighter().brighter());
         }

         var2.drawLine(var6, 0, 0, 0);
         var2.setColor(var5);
         var2.translate(-var3, -var4);
      }

      public int getIconWidth() {
         return this.size;
      }

      public int getIconHeight() {
         return this.size;
      }
   }

   class UpgradeTableCellRenderer implements TableCellRenderer {
      public static final int UPGRADE_TABLE = 0;
      public static final int NEW_TABLE = 1;
      private TableCellRenderer orig;
      private int tableType;

      public UpgradeTableCellRenderer(TableCellRenderer var2, int var3) {
         this.orig = var2;
         this.tableType = var3;
      }

      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         Component var7 = this.orig.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         int var8;
         int var9;
         switch(this.tableType) {
         case 0:
            var9 = JUpgradeFormDialog.this.mode_upg;
            var8 = JUpgradeFormDialog.this.sel_col_upg;
            break;
         case 1:
            var9 = JUpgradeFormDialog.this.mode_new;
            var8 = JUpgradeFormDialog.this.sel_col_new;
            break;
         default:
            var9 = -1;
            var8 = -1;
         }

         if (var7 instanceof JLabel) {
            JLabel var10 = (JLabel)var7;
            var10.setHorizontalTextPosition(2);
            JUpgradeFormDialog.Arrow var11 = JUpgradeFormDialog.this.new Arrow(var9 == -1, var10.getFont().getSize(), -1);
            if (var6 == var8) {
               var10.setIcon(var11);
            } else {
               var10.setIcon((Icon)null);
            }
         }

         return var7;
      }
   }
}
