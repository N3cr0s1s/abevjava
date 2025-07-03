package hu.piller.enykp.alogic.masterdata.gui.entityfilter;

import hu.piller.enykp.alogic.masterdata.core.EntityException;
import hu.piller.enykp.alogic.masterdata.core.EntityHome;
import hu.piller.enykp.alogic.masterdata.gui.MDWaitPanel;
import hu.piller.enykp.alogic.masterdata.sync.download.MasterDataDownload;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.base.TableSorter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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

public class EntityFilterForm extends JDialog implements ActionListener, WindowListener {
   private JTable table;
   private JPanel pnlEntities;
   private JPanel pnlButtons;
   private EntityFilterFormController controller;
   private CountDownLatch latch;

   public EntityFilterForm() {
      super(MainFrame.thisinstance);
      Thread var1 = new Thread(new Runnable() {
         public void run() {
            EntityFilterForm.this.init();
         }
      });
      var1.start();
   }

   public void init() {
      this.addWindowListener(this);
      this.setTitle("Törzsadatkezelő");
      this.setResizable(true);
      this.latch = new CountDownLatch(1);
      final MDWaitPanel var1 = new MDWaitPanel(this, "Lista előkészítése...");
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            var1.setVisible(true);
         }
      });
      Thread var2 = new Thread(new Runnable() {
         public void run() {
            EntityFilterForm.this.initController();
         }
      });
      var2.start();
      boolean var3 = true;

      label63: {
         try {
            var3 = this.latch.await(360000L, TimeUnit.MILLISECONDS);
            break label63;
         } catch (InterruptedException var8) {
            Thread.currentThread().interrupt();
         } finally {
            var1.close();
            if (!var3) {
               GuiUtil.showMessageDialog(this, "Nem sikerült az adatbázis felolvasása", "Rendszerhiba", 0);
               return;
            }

         }

         return;
      }

      this.setLayout(new BorderLayout(10, 10));
      JPanel var4 = this.getPnlEntities();
      this.add(var4, "Center");
      this.add(this.getPnlButtons(), "South");
      this.setSize((int)Math.max(640.0D, var4.getPreferredSize().getWidth() + 40.0D), 480);
      this.setMinimumSize(new Dimension((int)Math.max(640.0D, var4.getPreferredSize().getWidth() + 40.0D), 480));
      if (this.getOwner() != null) {
         this.setLocationRelativeTo(this.getOwner());
      }

      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            EntityFilterForm.this.setVisible(true);
         }
      });
   }

   public void initController() {
      try {
         this.controller = new EntityFilterFormController();
         ((EntityFilterTableModel)((TableSorter)this.getTblEntities().getModel()).getTableModel()).fillTable(this.controller.load());
      } finally {
         this.latch.countDown();
      }

   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = var1.getActionCommand();
      Thread var3;
      if ("new".equals(var2)) {
         var3 = new Thread(new Runnable() {
            public void run() {
               EntityFilterForm.this.controller.newEntity();
               SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                     ((EntityFilterTableModel)((TableSorter)EntityFilterForm.this.getTblEntities().getModel()).getTableModel()).fillTable(EntityFilterForm.this.controller.load());
                  }
               });
            }
         });
         var3.start();
      } else if ("modify".equals(var2)) {
         if (this.getTblEntities().getSelectedRow() < 0) {
            return;
         }

         var3 = new Thread(new Runnable() {
            public void run() {
               int var1 = ((TableSorter)EntityFilterForm.this.getTblEntities().getModel()).modelIndex(EntityFilterForm.this.getTblEntities().getSelectedRow());
               EntitySelector var2 = (EntitySelector)((EntityFilterTableModel)((TableSorter)EntityFilterForm.this.getTblEntities().getModel()).getTableModel()).getRows().get(var1);
               EntityFilterForm.this.controller.modify(var2.getEntityType(), var2.getEntityId());
               SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                     ((EntityFilterTableModel)((TableSorter)EntityFilterForm.this.getTblEntities().getModel()).getTableModel()).fillTable(EntityFilterForm.this.controller.load());
                     GuiUtil.setTableColWidth(EntityFilterForm.this.table, "Név", GuiUtil.getW("WWWWWWWWWWWWWWW"));
                  }
               });
            }
         });
         var3.start();
      } else {
         int var5;
         if ("delete".equals(var2)) {
            if (this.getTblEntities().getSelectedRow() < 0) {
               return;
            }

            int var8 = ((TableSorter)this.getTblEntities().getModel()).modelIndex(this.getTblEntities().getSelectedRow());
            final EntitySelector var4 = (EntitySelector)((EntityFilterTableModel)((TableSorter)this.getTblEntities().getModel()).getTableModel()).getRows().get(var8);
            if (!"Adótanácsadó".equals(var4.getEntityType())) {
               if (MasterDataDownload.getInstance().isWaitingForResponse()) {
                  var5 = JOptionPane.showConfirmDialog(MainFrame.thisinstance, "Önnek a NAV-hoz benyújtott, kiszolgálatlan törzsadat letöltési kérelme van.\nNem javasolt a helyi törzsadattár módosítása a szinkronizáció lezárása előtt.\n\nA figyelmeztetés ellenére megerősíti, hogy folytatja a kiválasztott elem törlését?", "Figyelmeztetés", 0);
                  if (var5 == 1) {
                     return;
                  }
               }

               if (MasterDataDownload.getInstance().hasResponse()) {
                  var5 = JOptionPane.showConfirmDialog(MainFrame.thisinstance, "Az Ön ÁNyK rendszerében törzsadat karbantartás van folyamatban.\nJavasoljuk, hogy a helyi törzsadattárból való törlés előtt zárja le a karbantartást.\n\nA figyelmeztetés ellenére megerősíti, hogy folytatja a kiválasztott elem törlését?", "Figyelmeztetés", 0);
                  if (var5 == 1) {
                     return;
                  }
               }
            }

            var5 = JOptionPane.showConfirmDialog(MainFrame.thisinstance, "Kérem, erősítse meg hogy valóban törölni akarja a kiválasztott törzsadatot!", "Törlés megerősítése", 0);
            if (var5 == 0) {
               Thread var6 = new Thread(new Runnable() {
                  public void run() {
                     EntityFilterForm.this.controller.delete(var4.getEntityId());
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                           ((EntityFilterTableModel)((TableSorter)EntityFilterForm.this.getTblEntities().getModel()).getTableModel()).fillTable(EntityFilterForm.this.controller.load());
                        }
                     });
                  }
               });
               var6.start();
            }
         } else if ("envelope".equals(var2)) {
            if (this.getTblEntities().getSelectedRow() < 0) {
               return;
            }

            var3 = new Thread(new Runnable() {
               public void run() {
                  int var1 = ((TableSorter)EntityFilterForm.this.getTblEntities().getModel()).modelIndex(EntityFilterForm.this.getTblEntities().getSelectedRow());
                  EntitySelector var2 = (EntitySelector)((EntityFilterTableModel)((TableSorter)EntityFilterForm.this.getTblEntities().getModel()).getTableModel()).getRows().get(var1);
                  EntityFilterForm.this.controller.envelope(var2.getEntityId());
               }
            });
            var3.start();
         } else if ("close".equals(var2)) {
            String var9 = "";
            boolean var10 = true;

            try {
               var5 = 0;
               if (this.controller.hasChanged()) {
                  var5 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "A partnertörzs megváltozott. Kívánja menteni?", "Törzsadatkezelő", 0, 3, (Icon)null, (Object[])null, (Object)null);
               }

               if (var5 == 0) {
                  EntityHome.getInstance().closeSession();
               } else {
                  if (var5 != 1) {
                     return;
                  }

                  EntityHome.getInstance().abortSession();
               }
            } catch (EntityException var7) {
               var7.printStackTrace();
               var9 = var7.getMessage() == null ? "N/A" : var7.getMessage();
            }

            if (!"".equals(var9)) {
               StringBuilder var12 = new StringBuilder();
               if (!"N/A".equals(var9)) {
                  var12.append("A hiba oka:\n");
                  var12.append(var9);
                  var12.append("\n\n");
               }

               var12.append("Visszatér az adatbevitelhez, és újra próbálkozik a mentéssel?\n");
               var12.append("(Ha a 'Nem' gombra kattint, a jelenlegi módosításai biztosan elvesznek)");
               int var11 = JOptionPane.showConfirmDialog(MainFrame.thisinstance, var12.toString(), "Adatmentési hiba", 0);
               if (var11 == 0 || var11 == -1) {
                  var10 = false;
               }
            }

            if (var10) {
               this.dispose();
            }
         }
      }

   }

   private JPanel getPnlEntities() {
      if (this.pnlEntities == null) {
         this.pnlEntities = new JPanel();
         this.pnlEntities.setLayout(new GridBagLayout());
         GridBagConstraints var1 = new GridBagConstraints();
         Vector var2 = new Vector();
         String var3 = "";

         int var4;
         for(var4 = 0; var4 < this.getTblEntities().getModel().getColumnCount(); ++var4) {
            var2.add(var4);
            var3 = var3 + this.getTblEntities().getModel().getColumnName(var4).length() + "WWWWWWWWWW";
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
         var4 = SwingUtilities.computeStringWidth(this.pnlEntities.getFontMetrics(this.pnlEntities.getFont()), var3);
         Dimension var5 = new Dimension(Math.max(630, var4), 400);
         this.pnlEntities.setPreferredSize(var5);
         this.pnlEntities.setMinimumSize(var5);
      }

      return this.pnlEntities;
   }

   private EntityFilterFilterPanel getPnlFilter(JTable var1, Vector var2) {
      EntityFilterFilterPanel var3 = new EntityFilterFilterPanel((JTable)null);
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
         EntityFilterTableModel var1 = new EntityFilterTableModel();
         this.table = new JTable();
         this.table.setSelectionMode(0);
         TableSorter var2 = new TableSorter(var1, this.table.getTableHeader());
         this.table.setModel(var2);
         this.table.setName("EntityFilter");
      }

      GuiUtil.setTableColWidth(this.table, "Név", GuiUtil.getW("WWWWWWWWWWWWWWW"));
      this.table.setAutoResizeMode(0);
      return this.table;
   }

   private JPanel getPnlButtons() {
      if (this.pnlButtons == null) {
         this.pnlButtons = new JPanel();
         int var1 = GuiUtil.getW(new JButton("Módosít"), "Módosít");
         int var2 = GuiUtil.getCommonItemHeight() + 4;
         int var3 = Math.max(650, var1 * 5);
         this.pnlButtons.setMinimumSize(new Dimension(var3, var2 + 20));
         this.pnlButtons.setPreferredSize(this.pnlButtons.getMinimumSize());
         this.pnlButtons.setMaximumSize(this.pnlButtons.getMinimumSize());
         this.pnlButtons.setLayout(new BoxLayout(this.pnlButtons, 0));
         this.pnlButtons.add(Box.createHorizontalGlue());
         this.pnlButtons.add(this.createButton("new", "Új", this, var1, var2));
         this.pnlButtons.add(Box.createHorizontalStrut(2));
         this.pnlButtons.add(this.createButton("modify", "Módosít", this, var1, var2));
         this.pnlButtons.add(Box.createHorizontalStrut(2));
         this.pnlButtons.add(this.createButton("delete", "Töröl", this, var1, var2));
         this.pnlButtons.add(Box.createHorizontalStrut(2));
         this.pnlButtons.add(this.createButton("envelope", "Boríték", this, var1, var2));
         this.pnlButtons.add(Box.createHorizontalStrut(2));
         this.pnlButtons.add(this.createButton("close", "Bezár", this, var1, var2));
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
      var6.setPreferredSize(var6.getMinimumSize());
      var6.setMaximumSize(var6.getMinimumSize());
      return var6;
   }

   public void windowOpened(WindowEvent var1) {
   }

   public void windowClosing(WindowEvent var1) {
      try {
         int var2 = 1;
         if (this.controller.hasChanged()) {
            var2 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "A partnertörzs megváltozott. Kívánja menteni?", "Törzsadatkezelő", 0, 3, (Icon)null, (Object[])null, (Object)null);
         }

         if (var2 == 0) {
            EntityHome.getInstance().closeSession();
         } else {
            EntityHome.getInstance().abortSession();
         }
      } catch (EntityException var4) {
         String var3 = "Az adattár lezárása nem sikerült, kérem nézze meg a felhasználói kézikönyvet mi a teendő!";
         if (var4.getMessage() != null) {
            var3 = var3 + "\nA hiba oka:\n";
            var3 = var3 + var4.getMessage();
         }

         GuiUtil.showMessageDialog(MainFrame.thisinstance, var3, "Adattár lezárás hiba", 0);
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
}
