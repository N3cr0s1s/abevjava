package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance;

import hu.piller.enykp.alogic.masterdata.sync.ui.entityfilter.SizeableCBRenderer;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.TechnicalMdCellEditor;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.ValueListMaintenanceRenderer;
import hu.piller.enykp.alogic.masterdata.sync.ui.response.JMDResponseDialog;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.base.tabledialog.TooltipTableHeader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Enumeration;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class JMDMaintenanceForm extends JDialog implements ActionListener, WindowListener {
   private MaintenanceController maintenanceController;
   private JPanel pnlCompare;
   private JPanel pnlButtons;
   private JTable table;
   private String idInResponseForm;
   private String id;
   private JMDResponseDialog responseForm;

   public JMDMaintenanceForm(String var1, JMDResponseDialog var2, int var3) {
      super(MainFrame.thisinstance);
      this.setDefaultCloseOperation(0);
      this.addWindowListener(this);
      this.id = var1;
      this.idInResponseForm = var1;
      this.setModal(true);
      this.maintenanceController = new MaintenanceController(var1);
      this.setTitle("Törzsadatok karbantartása");
      this.setResizable(true);
      this.setSize((int)(0.8D * (double)GuiUtil.getScreenW()), (int)(0.6D * (double)GuiUtil.getScreenH()));
      this.responseForm = var2;
      if (this.getOwner() != null) {
         this.setLocationRelativeTo(this.getOwner());
      }

      Thread var4 = new Thread(new Runnable() {
         public void run() {
            JMDMaintenanceForm.this.init();
         }
      });
      var4.start();
   }

   public void windowOpened(WindowEvent var1) {
   }

   public void windowClosing(WindowEvent var1) {
      if (this.table.isEditing()) {
         this.table.getCellEditor().stopCellEditing();
      }

      int var2 = 1;
      if (((MDMaintenanceTableModel)this.getTblEntities().getModel()).hasChanged()) {
         var2 = JOptionPane.showOptionDialog(this, "El akarja menteni az adatokat, mielőtt bezárja a karbantartó képernyőt?", "Üzenet", 0, 3, (Icon)null, new String[]{"Igen", "Nem"}, "Igen");
      }

      if (var2 == 0) {
         try {
            this.maintenanceController.save((MDMaintenanceTableModel)this.table.getModel());
            this.id = this.maintenanceController.getId();
            GuiUtil.showMessageDialog(this, "Az adatok mentésre kerültek a helyi törzsadattárba.", "Tájékoztatás", 1);
         } catch (Exception var4) {
            GuiUtil.showMessageDialog(this, "A mentése sikertelen!\n" + var4.getMessage(), "Hiba", 0);
         }

         this.responseForm.reloadResults(this.id, this.idInResponseForm);
         this.dispose();
      } else if (var2 == 1) {
         this.responseForm.reloadResults(this.id, this.idInResponseForm);
         this.dispose();
      }

   }

   public void windowClosed(WindowEvent var1) {
   }

   public void windowIconified(WindowEvent var1) {
   }

   public void windowDeiconified(WindowEvent var1) {
   }

   public void windowActivated(WindowEvent var1) {
   }

   public void windowDeactivated(WindowEvent var1) {
   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = var1.getActionCommand();
      if (this.table.isEditing()) {
         this.table.getCellEditor().stopCellEditing();
      }

      if ("select_all".equals(var2)) {
         ((MDMaintenanceTableModel)this.getTblEntities().getModel()).toggleAll(true);
      } else if ("deselect_all".equals(var2)) {
         ((MDMaintenanceTableModel)this.getTblEntities().getModel()).toggleAll(false);
      } else if ("print_list".equals(var2)) {
         this.maintenanceController.pdfPrint(this.table.getModel(), this.id);
      } else if ("entity_save".equals(var2)) {
         try {
            this.maintenanceController.save((MDMaintenanceTableModel)this.table.getModel());
            this.id = this.maintenanceController.getId();
            GuiUtil.showMessageDialog(this, "Az adatok mentésre kerültek a helyi törzsadattárba.", "Tájékoztatás", 1);
            ((MDMaintenanceTableModel)this.getTblEntities().getModel()).updateTableAfterSave(this.maintenanceController.getDataForMaintenance());
         } catch (Exception var6) {
            GuiUtil.showMessageDialog(this, "A mentés sikertelen!\n" + var6.getMessage(), "Hiba", 0);
         }
      } else if ("close".equals(var2)) {
         int var3 = 1;
         if (((MDMaintenanceTableModel)this.getTblEntities().getModel()).hasChanged()) {
            var3 = JOptionPane.showOptionDialog(this, "Bezárja a karbantartó képernyőt. Kívánja az adatokat menteni?", "Üzenet", 0, 3, (Icon)null, new String[]{"Igen", "Nem"}, "Igen");
         }

         if (var3 == 0) {
            try {
               this.maintenanceController.save((MDMaintenanceTableModel)this.table.getModel());
               this.id = this.maintenanceController.getId();
               GuiUtil.showMessageDialog(this, "Az adatok mentésre kerültek a helyi törzsadattárba.", "Tájékoztatás", 1);
            } catch (Exception var5) {
               GuiUtil.showMessageDialog(this, "A mentése sikertelen!\n" + var5.getMessage(), "Hiba", 0);
               return;
            }

            this.responseForm.reloadResults(this.id, this.idInResponseForm);
            this.dispose();
         } else if (var3 == 1) {
            this.responseForm.reloadResults(this.id, this.idInResponseForm);
            this.dispose();
         }
      }

   }

   private void init() {
      this.setLayout(new BorderLayout());
      this.add(this.getPnlCompare(), "Center");
      this.add(this.getPnlButtons(), "South");
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            JMDMaintenanceForm.this.setVisible(true);
         }
      });
   }

   private JPanel getPnlCompare() {
      if (this.pnlCompare == null) {
         this.pnlCompare = new JPanel();
         this.pnlCompare.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(184, 207, 229)), "Adatok szinkronizálása"));
         this.pnlCompare.setSize(new Dimension((int)Math.min(990.0D, (double)GuiUtil.getScreenW() * 0.8D), 400));
         this.pnlCompare.setPreferredSize(this.pnlCompare.getSize());
         this.pnlCompare.setMinimumSize(this.pnlCompare.getSize());
         this.pnlCompare.setLayout(new BorderLayout());
         this.pnlCompare.add(new JScrollPane(this.getTblEntities()), "Center");
      }

      return this.pnlCompare;
   }

   private JTable getTblEntities() {
      if (this.table == null) {
         MDMaintenanceTableModel var1 = new MDMaintenanceTableModel(this.maintenanceController.getDataForMaintenance(), this.maintenanceController.getMdNamesWithMultipleNavValues());
         this.table = new JTable() {
            private Border outside;
            private Border inside;
            private Border highlight;
            private Border unselected;

            {
               this.outside = new MatteBorder(1, 0, 1, 0, Color.BLUE);
               this.inside = new EmptyBorder(1, 0, 1, 0);
               this.highlight = new CompoundBorder(this.outside, this.inside);
               this.unselected = super.getBorder();
            }

            public Component prepareRenderer(TableCellRenderer var1, int var2, int var3) {
               JComponent var4 = (JComponent)var1.getTableCellRendererComponent(this, this.getValueAt(var2, var3), this.isCellSelected(var2, var3), false, var2, var3);
               if (var4 == null) {
                  return var4;
               } else {
                  if (((MDMaintenanceTableModel)this.getModel()).hasDifferentValues(var2)) {
                     var4.setBackground(Colors.HIGHLITED);
                     var4.setForeground(Color.BLACK);
                  } else {
                     var4.setBackground(this.getBackground());
                     var4.setForeground(Color.BLACK);
                  }

                  if (this.isRowSelected(var2)) {
                     var4.setBorder(this.highlight);
                  } else {
                     var4.setBorder(this.unselected);
                  }

                  return var4;
               }
            }

            public Component prepareEditor(TableCellEditor var1, int var2, int var3) {
               if (var3 == 4) {
                  JComponent var4 = (JComponent)var1.getTableCellEditorComponent(this, this.getValueAt(var2, var3), this.isCellSelected(var2, var3), var2, var3);
                  String var5 = (String)String.class.cast(this.getModel().getValueAt(var2, 0));
                  if (!JMDMaintenanceForm.this.maintenanceController.getMasterDataEditedWithValueEditor().contains(var5)) {
                     if (this.isCellSelected(var2, var3)) {
                        var4.setBackground(Colors.EDITING);
                        var4.setForeground(Color.BLACK);
                     } else {
                        var4.setBackground(this.getBackground());
                        var4.setForeground(Color.BLACK);
                     }
                  }

                  return var4;
               } else {
                  return super.prepareEditor(var1, var2, var3);
               }
            }

            public TableCellRenderer getCellRenderer(int var1, int var2) {
               if (this.isNavValueColumn(var2)) {
                  String var3 = (String)String.class.cast(this.getModel().getValueAt(var1, 0));
                  if (JMDMaintenanceForm.this.maintenanceController.getMdNamesWithMultipleNavValues().contains(var3)) {
                     return new ValueListMaintenanceRenderer();
                  }
               }

               return super.getCellRenderer(var1, var2);
            }

            public TableCellEditor getCellEditor(int var1, int var2) {
               String var3;
               if (this.isNavValueColumn(var2)) {
                  var3 = (String)String.class.cast(this.getModel().getValueAt(var1, 0));
                  if (JMDMaintenanceForm.this.maintenanceController.getMdNamesWithMultipleNavValues().contains(var3)) {
                     return new ValueListMaintenanceEditor(JMDMaintenanceForm.this.maintenanceController.getNavDataListForMasterData(var3));
                  }
               } else if (this.isValidValueColumn(var2)) {
                  var3 = (String)String.class.cast(this.getModel().getValueAt(var1, 0));
                  if (JMDMaintenanceForm.this.maintenanceController.getMasterDataEditedWithValueEditor().contains(var3)) {
                     return new TechnicalMdCellEditor();
                  }
               }

               return super.getCellEditor(var1, var2);
            }

            private boolean isNavValueColumn(int var1) {
               return var1 == 2;
            }

            private boolean isValidValueColumn(int var1) {
               return var1 == 4;
            }
         };
         this.table.setPreferredScrollableViewportSize(this.table.getPreferredSize());
         this.table.changeSelection(0, 0, false, false);
         this.table.setSelectionMode(0);
         this.table.setTableHeader(new TooltipTableHeader(this.table.getColumnModel()));
         this.table.setModel(var1);
         Enumeration var2 = this.table.getColumnModel().getColumns();

         while(var2.hasMoreElements()) {
            ((TableColumn)var2.nextElement()).setHeaderRenderer(new JMDMultiLineHeaderRenderer());
         }

         if (this.table.getColumnModel().getColumnCount() > 3) {
            this.table.getColumnModel().getColumn(3).setCellEditor(new SizeableCBRenderer());
            this.table.getColumnModel().getColumn(3).setCellRenderer(new SizeableCBRenderer());
         }

         GuiUtil.setTableColWidth(this.table);
         this.table.setAutoResizeMode(0);
         this.table.getTableHeader().setReorderingAllowed(false);
         if (GuiUtil.modGui()) {
            this.table.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
         }
      }

      return this.table;
   }

   private JPanel getPnlButtons() {
      if (this.pnlButtons == null) {
         this.pnlButtons = new JPanel();
         this.pnlButtons.setBorder(new EmptyBorder(5, 5, 5, 5));
         JLabel var1 = new JLabel();
         String var2 = "WWWEgyeztető ív (pdf)WWWMentésWWWMindWWWEgyik semWWWBezárWWW";
         this.pnlButtons.setMinimumSize(new Dimension(GuiUtil.getW(var1, var2), GuiUtil.getCommonItemHeight() + 12));
         this.pnlButtons.setPreferredSize(this.pnlButtons.getMinimumSize());
         this.pnlButtons.setMaximumSize(this.pnlButtons.getMinimumSize());
         this.pnlButtons.setLayout(new BoxLayout(this.pnlButtons, 0));
         this.pnlButtons.add(Box.createHorizontalGlue());
         this.pnlButtons.add(this.createButton("print_list", "Egyeztető ív (pdf)", this, 180, 25));
         this.pnlButtons.add(Box.createHorizontalStrut(2));
         this.pnlButtons.add(this.createButton("entity_save", "Mentés", this, 160, 25));
         this.pnlButtons.add(Box.createHorizontalStrut(20));
         this.pnlButtons.add(this.createButton("select_all", "Mind", this, 100, 25));
         this.pnlButtons.add(Box.createHorizontalStrut(2));
         this.pnlButtons.add(this.createButton("deselect_all", "Egyik sem", this, 100, 25));
         this.pnlButtons.add(Box.createHorizontalStrut(20));
         this.pnlButtons.add(this.createButton("close", "Bezár", this, 100, 25));
         this.pnlButtons.add(Box.createHorizontalGlue());
      }

      return this.pnlButtons;
   }

   private JButton createButton(String var1, String var2, ActionListener var3, int var4, int var5) {
      JButton var6 = new JButton();
      var6.setActionCommand(var1);
      var6.setName(var2);
      var6.setText(var2);
      var6.addActionListener(var3);
      var6.setMinimumSize(new Dimension(GuiUtil.getW(var6, var2), GuiUtil.getCommonItemHeight() + 2));
      var6.setPreferredSize(var6.getMinimumSize());
      var6.setMaximumSize(var6.getMinimumSize());
      return var6;
   }
}
