package hu.piller.enykp.alogic.masterdata.sync.ui.entityfilter;

import hu.piller.enykp.alogic.masterdata.gui.MDWaitPanel;
import hu.piller.enykp.alogic.masterdata.sync.download.MasterDataDownloadException;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.base.SwingWorker;
import hu.piller.enykp.util.base.TableSorter;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

public class SyncEntityFilterForm extends JDialog implements ActionListener {
   private static final String CMD_CLOSE = "close";
   private static final String CMD_REQUEST = "request";
   private static final String CMD_PRINT_LIST = "print_list";
   private static final String CMD_SELECT_ALL = "select_all";
   private static final String CMD_UNSELECT_ALL = "unselect_all";
   private JTable table;
   private JPanel pnlEntities;
   private JPanel pnlButtons;
   private SyncEntityFilterFormController controller;
   private CountDownLatch latch;

   public SyncEntityFilterForm() {
      super(MainFrame.thisinstance);
      this.setModal(true);
      Thread var1 = new Thread(new Runnable() {
         public void run() {
            SyncEntityFilterForm.this.init();
         }
      });
      var1.start();
   }

   public void init() {
      this.setTitle("Szinkronizálandó adózók kiválasztása");
      this.setResizable(true);
      final MDWaitPanel var1 = new MDWaitPanel(this, "Lista előkészítése...");
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            var1.setVisible(true);
         }
      });
      this.latch = new CountDownLatch(1);
      Thread var2 = new Thread(new Runnable() {
         public void run() {
            SyncEntityFilterForm.this.initController();
         }
      });
      var2.start();
      boolean var3 = false;

      label89: {
         try {
            var3 = this.latch.await(360000L, TimeUnit.MILLISECONDS);
            break label89;
         } catch (InterruptedException var8) {
            Thread.currentThread().interrupt();
            if (!var2.isInterrupted()) {
               var2.interrupt();
            }
         } finally {
            var1.close();
            if (!var3) {
               GuiUtil.showMessageDialog(this, "Nem sikerült a helyi adatbázis felolvasása", "Rendszerhiba", 0);
               return;
            }

         }

         return;
      }

      this.setLayout(new BorderLayout());
      int var4 = 20;

      for(int var5 = 0; var5 < 6; ++var5) {
         var4 += this.table.getColumnModel().getColumn(var5).getWidth();
      }

      var4 = Math.min(GuiUtil.getScreenW(), var4);
      this.add(this.getPnlEntities(), "Center");
      JPanel var10 = this.getPnlButtons();
      this.add(var10, "South");
      this.setSize(Math.max(640, var4), 480);
      this.setMinimumSize(this.getSize());
      this.setPreferredSize(this.getSize());
      if (this.getOwner() != null) {
         this.setLocationRelativeTo(this.getOwner());
      }

      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            SyncEntityFilterForm.this.setVisible(true);
         }
      });
   }

   public void initController() {
      try {
         this.controller = new SyncEntityFilterFormController();
         this.getTableModel().fillTable(this.controller.load());
      } finally {
         this.latch.countDown();
      }

   }

   private SyncEntityFilterTableModel getTableModel() {
      return (SyncEntityFilterTableModel)((TableSorter)this.getTblEntities().getModel()).getTableModel();
   }

   public void actionPerformed(ActionEvent var1) {
      if ("close".equals(var1.getActionCommand())) {
         this.dispose();
      } else {
         int var3;
         if ("request".equals(var1.getActionCommand())) {
            if (this.getTblEntities() != null && this.getTblEntities().getCellEditor() != null) {
               this.getTblEntities().getCellEditor().stopCellEditing();
            }

            final String[] var2 = this.getTableModel().getSelectedIds();
            if (var2 == null || var2.length == 0) {
               return;
            }

            var3 = JOptionPane.showOptionDialog(this, "A NAV törzsadat-nyilvántartását érheti el ezzel a funkcióval. Indulhat a kapcsolódás a NAV-hoz?", "Figyelmeztetés", 0, 3, (Icon)null, (Object[])null, (Object)null);
            if (var3 == 0) {
               (new Thread(new Runnable() {
                  private MDWaitPanel waitPanel;
                  private SwingWorker sender = new SwingWorker() {
                     public Object construct() {
                        String var1 = null;

                        try {
                           SyncEntityFilterForm.this.controller.send(var2);
                        } catch (MasterDataDownloadException var3) {
                           var1 = var3.getMessage();
                        }

                        return var1;
                     }
                  };

                  public void run() {
                     this.sender.start();
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                           SyncEntityFilterForm.this.getGlassPane().setCursor(Cursor.getPredefinedCursor(3));
                           SyncEntityFilterForm.this.getGlassPane().addMouseListener(new MouseAdapter() {
                           });
                           SyncEntityFilterForm.this.getGlassPane().addKeyListener(new KeyAdapter() {
                           });
                           SyncEntityFilterForm.this.getGlassPane().setVisible(true);
                        }
                     });
                     String var1 = (String)this.sender.get();
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                           SyncEntityFilterForm.this.getGlassPane().setCursor(Cursor.getPredefinedCursor(0));
                        }
                     });
                     if (var1 == null) {
                        GuiUtil.showMessageDialog(MainFrame.thisinstance, "A kérelem befogadása megtörtént, kiszolgálása folyamatban van.\nA kért adatok megérkezésekor az ÁNYK státuszsorában tájékoztató üzenet jelenik meg.", "Üzenet", 1);
                     } else {
                        GuiUtil.showMessageDialog(MainFrame.thisinstance, "A kérelem beküldése sikertelen!\n" + var1, "Hiba", 0);
                     }

                     SyncEntityFilterForm.this.dispose();
                  }
               })).start();
            }
         } else {
            int var4;
            if ("select_all".equals(var1.getActionCommand())) {
               if (this.getTblEntities() != null && this.getTblEntities().getCellEditor() != null) {
                  this.getTblEntities().getCellEditor().stopCellEditing();
               }

               var4 = this.getTableModel().getRowCount();

               for(var3 = 0; var3 < var4; ++var3) {
                  this.getTableModel().setValueAt(Boolean.TRUE, var3, 5);
               }
            } else if ("unselect_all".equals(var1.getActionCommand())) {
               if (this.getTblEntities() != null && this.getTblEntities().getCellEditor() != null) {
                  this.getTblEntities().getCellEditor().stopCellEditing();
               }

               var4 = this.getTableModel().getRowCount();

               for(var3 = 0; var3 < var4; ++var3) {
                  this.getTableModel().setValueAt(Boolean.FALSE, var3, 5);
               }
            }
         }
      }

   }

   private JPanel getPnlEntities() {
      if (this.pnlEntities == null) {
         this.pnlEntities = new JPanel();
         this.pnlEntities.setPreferredSize(new Dimension(630, 400));
         this.pnlEntities.setMinimumSize(new Dimension(630, 400));
         this.pnlEntities.setLayout(new GridBagLayout());
         GridBagConstraints var1 = new GridBagConstraints();
         Vector var2 = new Vector();

         for(int var3 = 0; var3 < this.getTblEntities().getModel().getColumnCount(); ++var3) {
            var2.add(var3);
         }

         var1.anchor = 23;
         var1.fill = 2;
         var1.gridwidth = 1;
         var1.gridheight = 1;
         var1.weightx = 1.0D;
         var1.gridx = 0;
         var1.gridy = 0;
         var1.ipady = 10;
         this.pnlEntities.add(this.getPnlFilter(this.getTblEntities(), var2), var1);
         var1.gridy = 1;
         var1.fill = 1;
         var1.weighty = 1.0D;
         this.pnlEntities.add(new JScrollPane(this.getTblEntities()), var1);
      }

      return this.pnlEntities;
   }

   private SyncEntityFilterFilterPanel getPnlFilter(JTable var1, Vector var2) {
      SyncEntityFilterFilterPanel var3 = new SyncEntityFilterFilterPanel((JTable)null);
      var3.getBusinessHandler().clearFilters();
      var3.getBusinessHandler().setFilterVisibility(true);
      var3.getBusinessHandler().setFileFilterTypeVisibility(false);
      var3.getBusinessHandler().setVisible(false);
      ((JLabel)var3.getComponent("filter_title_lbl")).setText("Szűrési feltételek");
      var3.getBusinessHandler().initials(new Object[]{var1, var2, var2.size()});
      return var3;
   }

   private JTable getTblEntities() {
      if (this.table == null) {
         SyncEntityFilterTableModel var1 = new SyncEntityFilterTableModel();
         this.table = new JTable();
         this.table.setSelectionMode(0);
         TableSorter var2 = new TableSorter(var1, this.table.getTableHeader());
         this.table.setModel(var2);
         this.table.setName("EntityFilter");
         this.table.getTableHeader().setReorderingAllowed(false);
         this.table.getColumnModel().getColumn(this.table.getColumnModel().getColumnCount() - 1).setCellEditor(new SizeableCBRenderer());
         this.table.getColumnModel().getColumn(this.table.getColumnModel().getColumnCount() - 1).setCellRenderer(new SizeableCBRenderer());
      }

      GuiUtil.setTableColWidth(this.table);
      if (GuiUtil.modGui()) {
         this.table.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
      }

      return this.table;
   }

   private JPanel getPnlButtons() {
      if (this.pnlButtons == null) {
         this.pnlButtons = new JPanel();
         int var1 = GuiUtil.getW(new JButton("Kijelöltek lekérdezése"), "Kijelöltek lekérdezése");
         int var2 = GuiUtil.getW(new JButton("Egyik sem"), "Egyik sem");
         int var3 = GuiUtil.getCommonItemHeight() + 4;
         int var4 = Math.max(650, var2 * 4 + var1);
         this.pnlButtons.setPreferredSize(new Dimension(var4, var3 + 20));
         this.pnlButtons.setSize(new Dimension(var4, var3 + 20));
         this.pnlButtons.setMinimumSize(this.pnlButtons.getSize());
         this.pnlButtons.setMaximumSize(this.pnlButtons.getSize());
         this.pnlButtons.setLayout(new BoxLayout(this.pnlButtons, 0));
         this.pnlButtons.add(Box.createHorizontalGlue());
         this.pnlButtons.add(this.createButton("request", "Kijelöltek lekérdezése", this, var1, var3));
         this.pnlButtons.add(Box.createHorizontalStrut(2));
         this.pnlButtons.add(this.createButton("select_all", "Mind", this, var2, var3));
         this.pnlButtons.add(Box.createHorizontalStrut(2));
         this.pnlButtons.add(this.createButton("unselect_all", "Egyik sem", this, var2, var3));
         this.pnlButtons.add(Box.createHorizontalStrut(2));
         this.pnlButtons.add(this.createButton("close", "Bezár", this, var2, var3));
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
      var6.setMinimumSize(new Dimension(var4, var5));
      var6.setPreferredSize(new Dimension(var4, var5));
      var6.setMaximumSize(new Dimension(var4, var5));
      return var6;
   }
}
