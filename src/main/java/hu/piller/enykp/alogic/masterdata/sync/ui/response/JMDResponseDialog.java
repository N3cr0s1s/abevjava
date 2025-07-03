package hu.piller.enykp.alogic.masterdata.sync.ui.response;

import hu.piller.enykp.alogic.masterdata.gui.MDWaitPanel;
import hu.piller.enykp.alogic.masterdata.sync.syncdir.SyncDirHandler;
import hu.piller.enykp.alogic.masterdata.sync.ui.PdfExport;
import hu.piller.enykp.alogic.masterdata.sync.ui.entityfilter.SizeableCBRenderer;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.JMDMultiLineHeaderRenderer;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.MaintenanceController;
import hu.piller.enykp.alogic.masterdata.sync.ui.pdfexport.IMapperCallback;
import hu.piller.enykp.alogic.masterdata.sync.ui.pdfexport.ViewModelToHtml;
import hu.piller.enykp.error.EnykpTechnicalException;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.base.TableSorter;
import hu.piller.enykp.util.base.tabledialog.TooltipTableHeader;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class JMDResponseDialog extends JDialog implements ActionListener {
   private static final String CMD_CLOSE = "close";
   private static final String CMD_PRINT_RESPONSES = "print_resp";
   private static final String CMD_PRINT_SELECTED = "print_list";
   private static final String CMD_TERMINATE_MAINTENANCE = "lezar";
   private static boolean running;
   private JPanel pnlResult;
   private JPanel pnlButtons;
   private JTable responseTable;
   private CountDownLatch latch;
   private JMDReponseFormController controller;

   public JMDResponseDialog() {
      super(MainFrame.thisinstance);
      running = true;
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent var1) {
            super.windowClosing(var1);
            JMDResponseDialog.running = false;
         }

         public void windowClosed(WindowEvent var1) {
            super.windowClosed(var1);
            JMDResponseDialog.running = false;
         }
      });
      Thread var1 = new Thread(new Runnable() {
         public void run() {
            JMDResponseDialog.this.init();
         }
      });
      var1.start();
   }

   public void init() {
      this.setTitle("NAV törzsadatlekérdezés eredménye");
      this.setResizable(true);
      this.setSize(this.getPanelSize() + 10, 460);
      this.setPreferredSize(this.getSize());
      this.setMinimumSize(this.getSize());
      if (this.getOwner() != null) {
         this.setLocationRelativeTo(this.getOwner());
      }

      this.latch = new CountDownLatch(1);
      final MDWaitPanel var1 = new MDWaitPanel(this, "A helyben tárolt, és a letöltött törzsadatok közti eltérések kiszámítása...");
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            var1.setVisible(true);
         }
      });
      Thread var2 = new Thread(new Runnable() {
         public void run() {
            JMDResponseDialog.this.initController();
         }
      });
      var2.start();
      boolean var3 = true;

      label79: {
         try {
            var3 = this.latch.await(360L, TimeUnit.SECONDS);
            break label79;
         } catch (InterruptedException var8) {
            Thread.currentThread().interrupt();
         } finally {
            if (!var3) {
               var1.close();
               GuiUtil.showMessageDialog(this, "Nem sikerült az adatbázis felolvasása", "Rendszerhiba", 0);
               return;
            }

         }

         return;
      }

      this.setLayout(new BorderLayout());
      this.add(this.getPnlResult(), "Center");
      this.add(this.getPnlButtons(), "South");
      var1.close();
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            JMDResponseDialog.this.setVisible(true);
         }
      });
      if (this.controller.hasError()) {
         StringBuffer var4 = new StringBuffer();
         if (this.controller.hasNoData()) {
            var4.append("Az Ön helyi törzsadattárában nem szerepel egyetlen, a NAV-tól letöltött adózó sem.\n");
            var4.append("A letöltési kérelem leadása, és annak kiszolgálása közt a helyi törzsadattárból törlésre kerültek.\n\n");
            var4.append("Legyen szíves, zárja le ezt az törzsadat karbantartást, és küldjön be új kérelmet.\n\n");
            var4.append("Amennyiben a megismételt kérelemére is ezt a választ kapja, lépjen kapcsolatba az ügyfélszolgálattal.\n");
            var4.append("A hiányzó adószámok és adóazonosítójelek listáját megtalálja a Szerviz->Üzenetek párbeszédpanelen.\n");
         } else {
            var4.append("A NAV-tól letöltött, és a helyben tárolt törzsadatok összehasonlítása során\nhiba történt.\n\n");
            var4.append("Kérem, a részletekről tájékozódjon a Szerviz->Üzenetek párbeszédpanelen.\n");
         }

         GuiUtil.showMessageDialog(this, var4.toString(), "Hiba", 0);
      }

   }

   public void initController() {
      this.controller = new JMDReponseFormController();
      this.latch.countDown();
   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = var1.getActionCommand();
      if ("close".equals(var2)) {
         this.dispose();
      } else if ("print_resp".equals(var2)) {
         String var3 = ViewModelToHtml.tableModelAsHtml(this.responseTable.getModel(), new IMapperCallback() {
            public boolean isColumnEnabled(int var1) {
               return var1 != 5;
            }

            public Object mapValue(int var1, Object var2) {
               if (var1 == 0) {
                  return "Siker".equals(String.class.cast(var2)) ? "Sikeres letöltés" : "Lekérdezési hiba";
               } else if (var1 == 4) {
                  String var3 = (String)String.class.cast(var2);
                  return !"".equals(var3) && !"nincs eltérés".equals(var3) ? "adatkarbantartás szükséges" : var3;
               } else {
                  return var2;
               }
            }
         });
         var3 = "<html><body>" + var3 + "</body></html>";
         PdfExport var4 = new PdfExport();
         var4.print("egyeztetes", var3);
      } else {
         int var17;
         if ("print_list".equals(var2)) {
            LinkedHashMap var15 = new LinkedHashMap();

            for(var17 = 0; var17 < this.responseTable.getModel().getRowCount(); ++var17) {
               if ((Boolean)Boolean.class.cast(this.responseTable.getModel().getValueAt(var17, 5))) {
                  Boolean var5 = Boolean.TRUE;
                  if (!"Siker".equals(String.class.cast(this.responseTable.getModel().getValueAt(var17, 0)))) {
                     var5 = false;
                  }

                  var15.put(String.class.cast(this.responseTable.getModel().getValueAt(var17, 3)), var5);
               }
            }

            if (var15.size() == 0) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem jelölt ki egyetlen sort sem", "Hiba", 0);
               return;
            }

            LinkedHashMap var18 = new LinkedHashMap();
            Iterator var19 = var15.entrySet().iterator();

            String var7;
            while(var19.hasNext()) {
               Entry var6 = (Entry)var19.next();
               if ((Boolean)var6.getValue()) {
                  var18.put(var6.getKey(), ViewModelToHtml.mDMaintenanceModelListAsHtml((new MaintenanceController((String)var6.getKey())).getDataForMaintenance()));
               } else {
                  try {
                     var7 = (String)var6.getKey();
                     if (var7.indexOf("-") != -1) {
                        var7 = var7.substring(0, 8);
                     }

                     var18.put(var6.getKey(), SyncDirHandler.getResultFileContent(var7));
                  } catch (Exception var13) {
                     var13.printStackTrace();
                  }
               }
            }

            StringBuffer var20 = new StringBuffer("<html><body>");
            Iterator var21 = var18.entrySet().iterator();

            while(var21.hasNext()) {
               Entry var23 = (Entry)var21.next();
               var20.append((String)var23.getValue());
            }

            var20.append("</body></html>");
            PdfExport var22 = new PdfExport();

            try {
               var7 = var22.open("egyezteto");
               if (var7 == null) {
                  return;
               }

               var22.addHtmlFragmentToPdf("<html><body>");
               Iterator var8 = var18.entrySet().iterator();

               while(var8.hasNext()) {
                  Entry var9 = (Entry)var8.next();
                  String var10 = "";
                  if (((String)var9.getKey()).indexOf("-") != -1) {
                     var10 = "adószám";
                  } else {
                     var10 = "adóazonosító jel";
                  }

                  String var11 = var10 + ":&nbsp;" + (String)var9.getKey() + "<br/><br/>" + (String)var9.getValue();
                  var22.addHtmlFragmentToPdf(var11);
               }

               var22.addHtmlFragmentToPdf("</body></html>");
               var22.close();
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "A(z) " + var7 + " kivonat mentése befejeződött!", "Üzenet", 1);
            } catch (Exception var14) {
               var14.printStackTrace();
            }
         } else if ("lezar".equals(var2)) {
            Object[] var16 = new Object[]{"Igen", "Nem"};
            var17 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Ha a befejezést választja, a feldolgozás alatt álló eredményállomány\nadatainak egyeztetése lezárásra kerül. Lezárás után tud új lekérdezést\nindítani a NAV felé.\nBe kívánja fejezni a törzsadatai karbantartását?", "Figyelmeztetés", 0, 3, (Icon)null, var16, var16[1]);
            if (var17 == 0) {
               try {
                  this.controller.closeMaintenance();
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "A karbantartás lezárva!", "Tájékoztatás", 1);
                  this.dispose();
               } catch (EnykpTechnicalException var12) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "A karbantartás lezárása nem sikerült az alábbi okból:\n\n" + var12.getMessage() + "\n\nA hiba elhárítása után kérem próbálkozzon újra!", "Hiba", 0);
               }
            }
         }
      }

   }

   private JPanel getPnlResult() {
      if (this.pnlResult == null) {
         this.pnlResult = new JPanel(new BorderLayout());
         int var1 = this.getPanelSize() + 10;
         this.pnlResult.setSize(new Dimension((int)Math.min(0.8D * (double)GuiUtil.getScreenW(), (double)var1), 400));
         this.pnlResult.setPreferredSize(this.pnlResult.getSize());
         JScrollPane var2 = new JScrollPane();
         var2.getViewport().add(this.getResponseTable());
         this.pnlResult.add(var2, "Center");
      }

      return this.pnlResult;
   }

   private JTable getResponseTable() {
      if (this.responseTable == null) {
         this.responseTable = new JTable() {
            private JMDResponseStatusCellEditor responseStatusCellEditor;
            private JMDResponseStatusCellRenderer responseStatusCellRenderer;
            private JMDMaintenanceButtonRenderer maintenanceButtonRenderer;
            private JMDMaintenanceButtonEditor maintenanceButtonEditor;

            public TableCellEditor getCellEditor(int var1, int var2) {
               if ("Lekérdezés\nstátusza".equals(this.getColumnModel().getColumn(var2).getHeaderValue())) {
                  if (this.responseStatusCellEditor == null) {
                     this.responseStatusCellEditor = new JMDResponseStatusCellEditor();
                  }

                  return this.responseStatusCellEditor;
               } else if ("Adatösszehasonlítás\neredménye".equals(this.getColumnModel().getColumn(var2).getHeaderValue())) {
                  if (this.maintenanceButtonEditor == null) {
                     this.maintenanceButtonEditor = new JMDMaintenanceButtonEditor(GuiUtil.getANYKCheckBox(), JMDResponseDialog.this);
                  }

                  return this.maintenanceButtonEditor;
               } else {
                  return super.getCellEditor(var1, var2);
               }
            }

            public TableCellRenderer getCellRenderer(int var1, int var2) {
               if ("Lekérdezés\nstátusza".equals(this.getColumnModel().getColumn(var2).getHeaderValue())) {
                  if (this.responseStatusCellRenderer == null) {
                     this.responseStatusCellRenderer = JMDResponseStatusCellRenderer.create();
                  }

                  return this.responseStatusCellRenderer;
               } else if ("Adatösszehasonlítás\neredménye".equals(this.getColumnModel().getColumn(var2).getHeaderValue())) {
                  if (this.maintenanceButtonRenderer == null) {
                     this.maintenanceButtonRenderer = new JMDMaintenanceButtonRenderer();
                  }

                  return this.maintenanceButtonRenderer;
               } else {
                  return super.getCellRenderer(var1, var2);
               }
            }

            public String getToolTipText(MouseEvent var1) {
               Point var2 = var1.getPoint();
               int var3 = this.columnAtPoint(var2);
               if (var3 == 2) {
                  int var4 = this.rowAtPoint(var2);
                  return (String)this.getValueAt(var4, 2);
               } else {
                  return super.getToolTipText(var1);
               }
            }
         };
         this.responseTable.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent var1) {
               super.mouseMoved(var1);
               Point var2 = var1.getPoint();
               int var3 = JMDResponseDialog.this.responseTable.columnAtPoint(var2);
               if (var3 == 0) {
                  int var4 = JMDResponseDialog.this.responseTable.rowAtPoint(var2);
                  String var5 = (String)JMDResponseDialog.this.responseTable.getModel().getValueAt(var4, var3);
                  if ("Hiba".equals(var5)) {
                     if (JMDResponseDialog.this.getCursor().getType() != 12) {
                        JMDResponseDialog.this.setCursor(Cursor.getPredefinedCursor(12));
                     }
                  } else if (JMDResponseDialog.this.getCursor().getType() != 0) {
                     JMDResponseDialog.this.setCursor(Cursor.getPredefinedCursor(0));
                  }
               } else if (JMDResponseDialog.this.getCursor().getType() != 0) {
                  JMDResponseDialog.this.setCursor(Cursor.getPredefinedCursor(0));
               }

            }
         });
         this.responseTable.setName("EntityFilter");
         this.responseTable.setSelectionMode(0);
         this.responseTable.setTableHeader(new TooltipTableHeader(this.responseTable.getColumnModel()));
         GuiUtil.setTableColWidth(this.responseTable);
         this.responseTable.setModel(new TableSorter(this.getResponseTableModel(), this.responseTable.getTableHeader()));
         Enumeration var1 = this.responseTable.getColumnModel().getColumns();

         while(var1.hasMoreElements()) {
            ((TableColumn)var1.nextElement()).setHeaderRenderer(new JMDMultiLineHeaderRenderer());
         }

         this.responseTable.getColumnModel().getColumn(0).setPreferredWidth(80);
         this.responseTable.getColumnModel().getColumn(1).setPreferredWidth(120);
         this.responseTable.getColumnModel().getColumn(2).setPreferredWidth(380);
         this.responseTable.getColumnModel().getColumn(3).setPreferredWidth(120);
         this.responseTable.getColumnModel().getColumn(4).setPreferredWidth(130);
         this.responseTable.getColumnModel().getColumn(5).setPreferredWidth(80);
         if (GuiUtil.modGui()) {
            this.responseTable.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
         }

         this.responseTable.getTableHeader().setReorderingAllowed(false);
         if (this.responseTable.getColumnModel().getColumnCount() > 5) {
            this.responseTable.getColumnModel().getColumn(5).setCellEditor(new SizeableCBRenderer());
            this.responseTable.getColumnModel().getColumn(5).setCellRenderer(new SizeableCBRenderer());
         }
      }

      return this.responseTable;
   }

   public void reloadResults(String var1, String var2) {
      this.controller.loadLocalEntities();
      Object[][] var3 = this.controller.getResults();
      int var4 = -1;

      int var5;
      for(var5 = 0; var5 < this.getResponseTable().getModel().getRowCount(); ++var5) {
         String var6 = (String)var3[var5][3];
         if (var1.equals(var6)) {
            var4 = var5;
            break;
         }
      }

      var5 = -1;

      int var8;
      for(var8 = 0; var8 < this.getResponseTable().getModel().getRowCount(); ++var8) {
         String var7 = (String)this.getResponseTable().getModel().getValueAt(var8, 3);
         if (var2.equals(var7)) {
            var5 = var8;
            break;
         }
      }

      for(var8 = 0; var8 < var3[var4].length; ++var8) {
         this.getResponseTable().getModel().setValueAt(var3[var4][var8], var5, var8);
      }

   }

   public static boolean isRunning() {
      return running;
   }

   private DefaultTableModel getResponseTableModel() {
      DefaultTableModel var1 = new DefaultTableModel() {
         public Class getColumnClass(int var1) {
            return var1 == this.getColumnCount() - 1 ? Boolean.class : super.getColumnClass(var1);
         }

         public boolean isCellEditable(int var1, int var2) {
            return var2 == 5 || this.hasErrorStatus(var1, var2) || this.isMaintainable(var1, var2);
         }

         private boolean hasErrorStatus(int var1, int var2) {
            String var3 = this.getValueAt(var1, var2).toString();
            return "Hiba".equals(var3) && var2 == 0;
         }

         private boolean isMaintainable(int var1, int var2) {
            String var3 = this.getValueAt(var1, var2).toString();
            return "Karbantartás".equals(var3);
         }
      };
      var1.setDataVector(this.controller.getResults(), new Object[]{"Lekérdezés\nstátusza", "Adózó típusa", "Adózó neve", "Adószám /\nAdóazonosító jel", "Adatösszehasonlítás\neredménye", "Kijelölés"});
      return var1;
   }

   private JPanel getPnlButtons() {
      if (this.pnlButtons == null) {
         this.pnlButtons = new JPanel();
         this.pnlButtons.setBorder(new EmptyBorder(5, 5, 5, 5));
         this.pnlButtons.setLayout(new BoxLayout(this.pnlButtons, 0));
         this.pnlButtons.add(Box.createHorizontalGlue());
         this.pnlButtons.add(this.createButton("print_resp", "Lista (pdf)", this, 180, 25));
         this.pnlButtons.add(Box.createHorizontalStrut(2));
         this.pnlButtons.add(this.createButton("print_list", "Kijelöltekről egyeztető ív (pdf)", this, 225, 25));
         this.pnlButtons.add(Box.createHorizontalStrut(2));
         this.pnlButtons.add(this.createButton("lezar", "Karbantartás lezárása", this, 180, 25));
         this.pnlButtons.add(Box.createHorizontalStrut(2));
         this.pnlButtons.add(this.createButton("close", "Bezár", this, 100, 25));
         this.pnlButtons.add(Box.createHorizontalGlue());
         this.pnlButtons.setSize(new Dimension(this.getPanelSize(), GuiUtil.getCommonItemHeight() + 14));
         this.pnlButtons.setPreferredSize(this.pnlButtons.getSize());
         this.pnlButtons.setMinimumSize(this.pnlButtons.getSize());
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

   private int getPanelSize() {
      JButton var1 = new JButton();
      int var2 = GuiUtil.getW(var1, "Lista (pdf)") + 5 + GuiUtil.getW(var1, "Kijelöltekről egyeztető ív (pdf)") + 5 + GuiUtil.getW(var1, "Karbantartás lezárása") + 5 + GuiUtil.getW(var1, "Bezár") + 5;
      var2 = Math.max(880, var2);
      return Math.min(var2, (int)(0.8D * (double)GuiUtil.getScreenW()));
   }
}
