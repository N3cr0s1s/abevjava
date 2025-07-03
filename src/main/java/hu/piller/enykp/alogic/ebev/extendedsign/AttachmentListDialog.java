package hu.piller.enykp.alogic.ebev.extendedsign;

import hu.piller.enykp.alogic.ebev.Attachements;
import hu.piller.enykp.alogic.ebev.Ebev;
import hu.piller.enykp.alogic.ebev.EbevTools;
import hu.piller.enykp.alogic.ebev.SendParams;
import hu.piller.enykp.alogic.filepanels.attachement.AttachementTool;
import hu.piller.enykp.alogic.kontroll.ReadOnlyTableModel;
import hu.piller.enykp.alogic.masterdata.sync.ui.entityfilter.SizeableCBRenderer;
import hu.piller.enykp.alogic.signer.SignerException;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.framework.MainPanel;
import hu.piller.enykp.gui.framework.Menubar;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IOsHandler;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.tabledialog.TooltipTableHeader;
import hu.piller.enykp.util.icon.ENYKIconSet;
import hu.piller.enykp.util.oshandler.OsFactory;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class AttachmentListDialog extends JDialog implements TableModelListener {
   public static final int MAIN_WIDTH = 780;
   public static final int MAIN_HEIGHT = 420;
   public static final int NOTEINPUT_WIDTH = 400;
   public static final int NOTEINPUT_HEIGHT = 60;
   public static final int RENAME_WIDTH = 400;
   public static final int RENAME_HEIGHT = 220;
   public static final int TYPE_SIGN = 0;
   public static final int TYPE_RESET = 1;
   public static final int TYPE_XCZ = 2;
   private BookModel bookModel;
   private String loadedFileAzon;
   private JFrame mainFrame;
   private Object[] defaultAtcColumns;
   private Object[] defaultDataColumns;
   private ReadOnlyTableModel atcTableModel;
   private ReadOnlyTableModel dataTableModel;
   private JTable atcTable;
   private JTable dataTable;
   private JButton okButton = new JButton();
   private JButton cancelButton = new JButton("Kilépés");
   private JButton showPdfButton = new JButton("Aláírók");
   private AttachmentListDialog.ClickListener cl = new AttachmentListDialog.ClickListener();
   private Hashtable<String, String> pdfFiles = new Hashtable();
   private int firstSign = 0;
   private SendParams sp = new SendParams(PropertyList.getInstance());
   private int lastSelectedTable = 0;
   private final Cursor helpCursor = new Cursor(12);
   private final Cursor defaultCursor = new Cursor(0);
   Object cmdObject;

   public AttachmentListDialog(JFrame var1, BookModel var2, final int var3) {
      super(var1, "A nyomtatvány és csatolmányai", true);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosed(WindowEvent var1) {
            Enumeration var2 = AttachmentListDialog.this.pdfFiles.keys();
            boolean var3 = true;

            while(var2.hasMoreElements()) {
               Object var4 = var2.nextElement();

               try {
                  File var5 = new NecroFile((String)AttachmentListDialog.this.pdfFiles.get(var4));
                  var5.delete();
               } catch (Exception var6) {
                  var3 = false;
               }
            }

         }
      });
      this.mainFrame = var1;
      this.bookModel = var2;
      if (var3 == 0) {
         this.defaultAtcColumns = new Object[]{"Csatolmány neve", "Megjegyzés", "Típus", "Aláírók száma", ""};
         this.defaultDataColumns = new Object[]{"Nyomtatványfájl neve", "Aláírók száma", ""};
      } else {
         this.defaultAtcColumns = new Object[]{"Csatolmány neve", "Megjegyzés", "Típus", "Aláírók száma"};
         this.defaultDataColumns = new Object[]{"Nyomtatványfájl neve", "Aláírók száma"};
      }

      this.atcTableModel = new ReadOnlyTableModel(this.defaultAtcColumns, 0);
      this.dataTableModel = new ReadOnlyTableModel(this.defaultDataColumns, 0);
      this.atcTable = new JTable(this.atcTableModel) {
         public boolean isCellEditable(int var1, int var2) {
            return var2 == 4 && var3 == 0;
         }

         public Class<?> getColumnClass(int var1) {
            return var1 == 4 && var3 == 0 ? Boolean.class : String.class;
         }
      };
      this.atcTable.setTableHeader(new TooltipTableHeader(this.atcTable.getColumnModel()));
      this.dataTable = new JTable(this.dataTableModel) {
         public boolean isCellEditable(int var1, int var2) {
            return var2 == 2 && var3 == 0 && !"-".equals(AttachmentListDialog.this.dataTable.getValueAt(0, 0));
         }

         public Class<?> getColumnClass(int var1) {
            return var1 == 2 && var3 == 0 ? Boolean.class : String.class;
         }
      };
      if (this.dataTable.getColumnModel().getColumnCount() > 2) {
         this.dataTable.getColumnModel().getColumn(2).setCellEditor(new SizeableCBRenderer());
         this.dataTable.getColumnModel().getColumn(2).setCellRenderer(new SizeableCBRenderer());
      }

      if (this.atcTable.getColumnModel().getColumnCount() > 4) {
         this.atcTable.getColumnModel().getColumn(4).setCellEditor(new SizeableCBRenderer());
         this.atcTable.getColumnModel().getColumn(4).setCellRenderer(new SizeableCBRenderer());
      }

      this.dataTable.setTableHeader(new TooltipTableHeader(this.dataTable.getColumnModel()));
      AttachmentListDialog.IconTableCellRenderer var4 = new AttachmentListDialog.IconTableCellRenderer();
      JLabel var5 = new JLabel(ENYKIconSet.getInstance().get("anyk_sugo"));
      var5.setSize(30, 20);
      var5.setPreferredSize(new Dimension(30, 20));
      if (var3 == 0) {
         if (this.atcTable.getColumnModel().getColumnCount() > 4) {
            this.atcTable.getColumnModel().getColumn(4).setHeaderRenderer(var4);
            this.atcTable.getColumnModel().getColumn(4).setHeaderValue(var5);
         }

         if (this.dataTable.getColumnModel().getColumnCount() > 2) {
            this.dataTable.getColumnModel().getColumn(2).setHeaderRenderer(var4);
            this.dataTable.getColumnModel().getColumn(2).setHeaderValue(var5);
         }

         AttachmentListDialog.IconTableHeaderMouseAdapter var6 = new AttachmentListDialog.IconTableHeaderMouseAdapter();
         this.atcTable.getTableHeader().addMouseListener(var6);
         this.dataTable.getTableHeader().addMouseListener(var6);
      }

      this.loadedFileAzon = var2.cc.getLoadedfile().getName();
      this.loadedFileAzon = this.loadedFileAzon.substring(0, this.loadedFileAzon.length() - ".frm.enyk".length());
      int var11 = this.mainFrame.getX() + this.mainFrame.getWidth() / 2 - 390;
      if (var11 < 0) {
         var11 = 0;
      }

      int var12 = this.mainFrame.getY() + this.mainFrame.getHeight() / 2 - 210;
      if (var12 < 0) {
         var12 = 0;
      }

      this.setBounds(var11, var12, 780, 420);
      EmptyBorder var13 = new EmptyBorder(10, 10, 10, 10);
      new EtchedBorder(0);
      EmptyBorder var15 = new EmptyBorder(0, 10, 0, 10);
      Container var16 = this.getContentPane();
      var16.setLayout(new BoxLayout(var16, 1));
      JPanel var8 = new JPanel();
      var8.setLayout(new BoxLayout(var8, 0));

      try {
         this.fillAtcTable();
      } catch (Exception var23) {
         var23.printStackTrace();
      }

      AsicPdfHandler var17 = new AsicPdfHandler();
      String var18 = this.sp.srcPath + this.loadedFileAzon + File.separator + "alairt" + File.separator + this.loadedFileAzon + ".urlap.anyk.ASiC";
      Result var19 = var17.getSigners(var18);
      String var20 = "-";
      if (var19.isOk()) {
         var20 = (String)var19.errorList.get(1);
         this.pdfFiles.put(this.loadedFileAzon, (String)var19.errorList.get(0));
      }

      if (var3 == 0) {
         this.dataTableModel.addRow(new Object[]{this.loadedFileAzon, var20, Boolean.TRUE});
      } else {
         this.dataTableModel.addRow(new Object[]{this.loadedFileAzon, var20});
      }

      this.dataTable.addMouseListener(this.cl);
      this.dataTable.addMouseMotionListener(this.cl);
      this.dataTable.getModel().addTableModelListener(this);
      this.dataTable.setName("dataTable");
      this.dataTable.getColumnModel().getColumn(0).setMinWidth(200);
      this.dataTable.getColumnModel().getColumn(0).setPreferredWidth(445);
      this.dataTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            Component var7 = super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
            if (var6 == 1) {
               ((DefaultTableCellRenderer)var7).setHorizontalAlignment(0);
            } else {
               ((DefaultTableCellRenderer)var7).setHorizontalAlignment(2);
            }

            return var7;
         }
      });
      this.dataTable.getTableHeader().setBorder(new LineBorder(Color.gray, 1));
      this.dataTable.setBorder(new LineBorder(Color.gray, 1));
      if (GuiUtil.modGui()) {
         this.dataTable.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
      }

      this.atcTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            Component var7 = super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
            if (var6 == 3) {
               ((DefaultTableCellRenderer)var7).setHorizontalAlignment(0);
            } else {
               ((DefaultTableCellRenderer)var7).setHorizontalAlignment(2);
            }

            return var7;
         }
      });
      this.atcTable.getTableHeader().setBorder(new LineBorder(Color.gray, 1));
      this.atcTable.setBorder(new LineBorder(Color.gray, 1));
      this.atcTable.setName("atcTable");
      JScrollPane var24 = new JScrollPane(this.atcTable, 20, 30);
      JScrollPane var7 = new JScrollPane(this.dataTable, 20, 30);
      var24.setPreferredSize(new Dimension(760, 200));
      var24.setMinimumSize(new Dimension(760, 2 * (GuiUtil.getCommonItemHeight() + 2)));
      var7.setPreferredSize(new Dimension(760, 200));
      var7.setMinimumSize(new Dimension(760, 2 * (GuiUtil.getCommonItemHeight() + 2)));
      var24.setBorder(var13);
      var7.setBorder(var13);
      JLabel var9 = new JLabel("Nyomtatvány és mellékletek hitelesítése az AVDH szolgáltatással");
      switch(var3) {
      case 0:
         var9.setText("Nyomtatvány és mellékletek hitelesítése az AVDH szolgáltatással");
         this.okButton.setText("AVDH aláírás");
         break;
      case 1:
         var9.setText("Aláírások visszavonása. A visszavonás mindenki aláírására vonatkozik!");
         this.okButton.setText("Visszavonás");
         break;
      case 2:
         var9.setText("XCZ adatcsomag összeállítása");
         this.okButton.setText("XCZ készítés");
      }

      var9.setFont(new Font(var9.getFont().getName(), 1, var9.getFont().getSize() + 2));
      var9.setAlignmentX(0.5F);
      JLabel var10 = new JLabel("A csatolmányok adatai:");
      JPanel var21 = new JPanel(new FlowLayout(0));
      var21.add(var10);
      var21.setBorder(var15);
      var9.setBorder(var13);
      this.atcTable.addMouseListener(this.cl);
      this.atcTable.addMouseMotionListener(this.cl);
      this.atcTable.getModel().addTableModelListener(this);
      if (this.atcTable.getColumnModel().getColumnCount() > 3) {
         this.atcTable.getColumnModel().getColumn(0).setMinWidth(200);
         this.atcTable.getColumnModel().getColumn(0).setPreferredWidth(380);
         this.atcTable.getColumnModel().getColumn(1).setMinWidth(80);
         this.atcTable.getColumnModel().getColumn(1).setPreferredWidth(100);
         this.atcTable.getColumnModel().getColumn(2).setMinWidth(80);
         this.atcTable.getColumnModel().getColumn(2).setPreferredWidth(100);
         this.atcTable.getColumnModel().getColumn(3).setMinWidth(70);
         this.atcTable.getColumnModel().getColumn(3).setPreferredWidth(90);
      }

      if (var3 == 0 && this.atcTable.getColumnModel().getColumnCount() > 4) {
         this.atcTable.getColumnModel().getColumn(4).setMinWidth(40);
         this.atcTable.getColumnModel().getColumn(4).setPreferredWidth(60);
      }

      this.atcTable.setSelectionMode(0);
      if (GuiUtil.modGui()) {
         this.atcTable.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
      }

      this.showPdfButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            String var2 = null;
            if (AttachmentListDialog.this.lastSelectedTable == 0) {
               var2 = (String)AttachmentListDialog.this.pdfFiles.get(AttachmentListDialog.this.dataTable.getValueAt(0, 0));
            } else if (AttachmentListDialog.this.atcTable.getSelectedRowCount() == 1) {
               var2 = (String)AttachmentListDialog.this.pdfFiles.get(AttachmentListDialog.this.atcTable.getValueAt(AttachmentListDialog.this.atcTable.getSelectedRow(), 0));
            }

            if (var2 != null) {
               AttachmentListDialog.this.execute((new NecroFile(var2)).getParentFile(), var2);
            } else {
               GuiUtil.showMessageDialog(AttachmentListDialog.this.getParent(), "A fájl nincs AVDH szolgáltatással aláírva!", "Üzenet", 1);
            }

         }
      });
      this.okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            try {
               switch(var3) {
               case 0:
                  AttachmentListDialog.this.handleAttachments();
                  break;
               case 1:
                  AttachmentListDialog.this.handleReset();
                  break;
               case 2:
                  AttachmentListDialog.this.handleCreateXCZ();
               }
            } catch (Exception var3x) {
               var3x.printStackTrace();
            }

            if (var3 == 1 || var3 == 2) {
               AttachmentListDialog.this.dispose();
            }

         }
      });
      this.cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            AttachmentListDialog.this.dispose();
         }
      });
      var16.add(var9);
      var16.add(var7);
      var16.add(var21);
      var16.add(var24);
      var8.add(this.showPdfButton);
      var8.add(Box.createHorizontalGlue());
      this.okButton.setAlignmentX(0.5F);
      var8.add(this.okButton);
      var8.add(Box.createHorizontalStrut(20));
      var8.add(this.cancelButton);
      var8.setBorder(var13);
      var16.add(var8);
      this.firstSign = this.checkSignCount();
      switch(this.firstSign) {
      case -1:
         var9.setText("Figyelem! A listákban található aláírt és alá nem írt fájl is!");
         this.okButton.setEnabled(var3 != 2);
         break;
      case 2:
         var9.setText("Figyelem! Az egyes csatolmányok illetve a nyomtatvány aláíróinak száma eltér!");
      }

      GuiUtil.setTableColWidth(this.atcTable);
      GuiUtil.setTableColWidth(this.dataTable);
      int var22 = (int)Math.min(0.8D * (double)GuiUtil.getScreenW(), 1.5D * (double)GuiUtil.getW("WWWAláírókWWXCZ KészítésWWKilépésWWW"));
      this.setSize(var22, 15 * (GuiUtil.getCommonItemHeight() + 4));
      this.setPreferredSize(this.getSize());
      this.setMinimumSize(this.getSize());
      this.pack();
      this.setVisible(true);
   }

   private void fillAtcTable() throws Exception {
      String var1 = AttachementTool.getAtcFilename(this.bookModel.cc.getLoadedfile().getAbsolutePath(), this.bookModel);
      File var2 = new NecroFile(var1);
      if (var2.exists()) {
         Object var3 = AttachementTool.loadAtcFile(var2, false);
         if (var3 instanceof String) {
            throw new Exception((String)var3);
         } else {
            Vector var4 = (Vector)var3;
            AsicPdfHandler var5 = new AsicPdfHandler();

            for(int var6 = 0; var6 < var4.size(); ++var6) {
               String[] var7 = (String[])((String[])var4.elementAt(var6));
               if (!var7[0].toLowerCase().endsWith(".anyk.ASiC".toLowerCase())) {
                  String var8 = var7[0] + ".anyk.ASiC";
                  Result var9 = var5.getSigners(var8);
                  String var10 = "-";
                  if (var9.isOk()) {
                     var10 = (String)var9.errorList.get(1);
                     this.pdfFiles.put(var7[0], (String)var9.errorList.get(0));
                  }

                  this.atcTableModel.addRow(new Object[]{var7[0], var7[1], var7[2], var10, Boolean.TRUE});
               }
            }

         }
      }
   }

   private void execute(File var1, String var2) {
      IOsHandler var3 = OsFactory.getOsHandler();

      try {
         var3.execute(var2, (String[])null, var1);
      } catch (Exception var5) {
         GuiUtil.showMessageDialog(this.getParent(), "Nem sikerült futtatni a pdf olvasó alkalmazást!", "Csatolmányok kezelése", 0);
      }

   }

   public void tableChanged(TableModelEvent var1) {
   }

   private void handleAttachments() throws Exception {
      if (!this.hasAnySignableFile()) {
         GuiUtil.showMessageDialog(this.getParent(), "Nem jelölt ki egyetlen fájlt sem a műveletre!", "Üzenet", 1);
      } else if (this.hasFirstAndOtherSignableFile()) {
         GuiUtil.showMessageDialog(this.getParent(), "Az aláírandó fájlok között nem lehet első és nem első aláírásra kijelölt! ('Aláírók száma' oszlop)\nKérjük egyszerre vagy csak '-'-lel jelölt, vagy csak tetszőleges számot tartalmazó sorokat jelöljön ki!", "Üzenet", 1);
      } else {
         Result var1 = EbevTools.checkAttachment(this.bookModel, this.bookModel.cc.getLoadedfile().getAbsolutePath(), this.bookModel.get_formid());
         if (!var1.isOk()) {
            EbevTools.showExtendedResultDialog(this, "A csatolmányok ellenőrzése hibát jelzett!", var1.errorList, 0);
         } else {
            Vector var2 = AttachementTool.mergeCstFiles(var1.errorList);
            Attachements var3 = new Attachements(this.bookModel);
            int var4 = this.checkCheckedRowSignCount();
            Vector var5 = new Vector();
            ArrayList var6 = new ArrayList();

            for(int var7 = 0; var7 < var2.size(); ++var7) {
               String[] var8 = (String[])((String[])var2.elementAt(var7));
               File var9 = new NecroFile(var8[0] + ".anyk.ASiC");
               if (var9.exists()) {
                  if (this.fileMarked2Sign(var8[0])) {
                     var6.add(var9);
                  }

                  var8[0] = var8[0] + ".anyk.ASiC";
               } else if (this.fileMarked2Sign(var8[0])) {
                  var5.add(var8);
               }
            }

            Vector var27 = new Vector();
            if (var4 > 0) {
               String var29 = this.sp.srcPath + this.loadedFileAzon + File.separator + "alairt" + File.separator + this.loadedFileAzon + ".urlap.anyk.ASiC";
               if ((Boolean)this.dataTable.getValueAt(0, 2)) {
                  var6.add(new NecroFile(var29));
               }

               label266: {
                  try {
                     PropertyList.getInstance().set("prop.dynamic.avdh_direct_from_menu", "1");
                     var3.handleOtherAVDH(var6);
                     break label266;
                  } catch (SignerException var23) {
                     ErrorList.getInstance().writeError(new Long(4001L), "AVDH aláírás nem sikerült!", IErrorList.LEVEL_ERROR, var23, (Object)null);
                     GuiUtil.showMessageDialog(this.getParent(), var23.getMessage(), "Hiba az avdh aláírás készítésekor", 0);
                  } finally {
                     PropertyList.getInstance().set("prop.dynamic.avdh_direct_from_menu", (Object)null);
                  }

                  return;
               }

               this.createLogVector(var27, var6);
            } else {
               KrPreparation var28 = new KrPreparation(this.bookModel);
               String var30 = var28.avdh(true);
               if (var30 == null) {
                  return;
               }

               label275: {
                  try {
                     PropertyList.getInstance().set("prop.dynamic.avdh_direct_from_menu", "1");
                     var3.handleFirstAVDH(var5, false, (Boolean)this.dataTable.getValueAt(0, 2));
                     break label275;
                  } catch (SignerException var25) {
                     ErrorList.getInstance().writeError(new Long(4001L), "AVDH aláírás nem sikerült!", IErrorList.LEVEL_ERROR, var25, (Object)null);
                     GuiUtil.showMessageDialog(this.getParent(), var25.getMessage(), "Hiba az avdh aláírás készítésekor", 0);
                  } finally {
                     PropertyList.getInstance().set("prop.dynamic.avdh_direct_from_menu", (Object)null);
                  }

                  return;
               }

               var28.moveXmlToTargetDir(var30);
               this.createLogVector(var27, var5);
               if (var27.size() == 0) {
                  this.createFirstSigningLogVector(var27);
               } else if ((Boolean)this.dataTable.getValueAt(0, 2)) {
                  var27.insertElementAt(Tools.beautyPath(this.sp.srcPath + this.loadedFileAzon + File.separator + "alairt" + File.separator + this.dataTableModel.getValueAt(0, 0) + ".urlap.anyk.ASiC"), 1);
               }
            }

            this.setSignerCount();

            try {
               Ebev.log(11, this.bookModel.cc.getLoadedfile());
            } catch (Exception var22) {
               ErrorList.getInstance().writeError(new Long(4001L), "AVDH aláírás naplózása nem sikerült!", IErrorList.LEVEL_ERROR, var22, (Object)null);
            }

            EbevTools.showExtendedResultDialog(this, "A nyomtatvány " + (var27.size() > 1 ? "és a csatolmányok" : "") + " aláírása megtörtént.", var27, 1);
            Menubar.thisinstance.setState(MainPanel.READONLY);
            MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("AVDH aláírással hitelesített");
            MainFrame.thisinstance.mp.setReadonly(true);
         }
      }
   }

   private void createFirstSigningLogVector(Vector var1) {
      var1.add("Az alábbi aláírás fájlokat készítettük: ");
      if ((Boolean)this.dataTable.getValueAt(0, 2)) {
         var1.add(this.sp.srcPath + this.loadedFileAzon + File.separator + "alairt" + File.separator + this.dataTableModel.getValueAt(0, 0) + ".urlap.anyk.ASiC");
      }

      if (this.atcTableModel.getRowCount() != 0) {
         for(int var2 = 0; var2 < this.atcTableModel.getRowCount(); ++var2) {
            if ((Boolean)this.atcTableModel.getValueAt(var2, 4)) {
               var1.add(this.atcTableModel.getValueAt(var2, 0) + ".anyk.ASiC");
            }
         }

      }
   }

   private void createLogVector(Vector var1, Vector var2) {
      if (var2.size() != 0) {
         var1.add("Az alábbi aláírás fájlokat készítettük: ");

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            String var4 = ((String[])((String[])var2.elementAt(var3)))[0];
            if (var4.toLowerCase().endsWith(".anyk.ASiC".toLowerCase())) {
               var1.add(Tools.beautyPath(var4));
            }
         }

      }
   }

   private void createLogVector(Vector var1, ArrayList var2) {
      if (var2.size() != 0) {
         var1.add("Az alábbi aláírás fájlokat készítettük: ");

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            File var4 = (File)var2.get(var3);
            if (var4.getName().toLowerCase().endsWith(".anyk.ASiC".toLowerCase())) {
               var1.add(Tools.beautyPath(var4.getAbsolutePath()));
            }
         }

      }
   }

   private void setSignerCount() {
      AsicPdfHandler var1 = new AsicPdfHandler();

      for(int var2 = 0; var2 < this.atcTableModel.getRowCount(); ++var2) {
         String var3 = this.atcTableModel.getValueAt(var2, 0) + ".anyk.ASiC";
         Result var4 = var1.getSigners(var3);
         String var5 = "-";
         if (var4.isOk()) {
            var5 = (String)var4.errorList.get(1);
            this.pdfFiles.put((String)this.atcTableModel.getValueAt(var2, 0), (String)var4.errorList.get(0));
         }

         if (this.atcTableModel.getColumnCount() > 4) {
            if ((Boolean)this.atcTableModel.getValueAt(var2, 4)) {
               this.atcTableModel.setValueAt(var5, var2, 3);
            }
         } else {
            this.atcTableModel.setValueAt(var5, var2, 3);
         }
      }

      String var6 = this.sp.srcPath + this.loadedFileAzon + File.separator + "alairt" + File.separator + this.dataTableModel.getValueAt(0, 0) + ".urlap.anyk.ASiC";
      Result var7 = var1.getSigners(var6);
      String var8 = "-";
      if (var7.isOk()) {
         var8 = (String)var7.errorList.get(1);
         this.pdfFiles.put((String)this.dataTableModel.getValueAt(0, 0), (String)var7.errorList.get(0));
      }

      if (this.dataTableModel.getColumnCount() > 2) {
         if ((Boolean)this.dataTableModel.getValueAt(0, 2)) {
            this.dataTableModel.setValueAt(var8, 0, 1);
         }
      } else {
         this.dataTableModel.setValueAt(var8, 0, 1);
      }

      this.firstSign = this.checkSignCount();
   }

   private void handleReset() {
      Vector var1 = new Vector();
      boolean var2 = true;

      for(int var3 = 0; var3 < this.atcTableModel.getRowCount(); ++var3) {
         File var4 = new NecroFile(this.atcTableModel.getValueAt(var3, 0) + ".anyk.ASiC");
         if (var4.exists()) {
            var2 = var2 && var4.delete();
         }
      }

      String var7 = this.sp.srcPath + this.loadedFileAzon;
      String var8 = this.sp.srcPath + this.loadedFileAzon + ".xml";
      if (!(new NecroFile(var8)).delete()) {
         var1.add(var8);
         var2 = false;
      }

      var8 = this.sp.srcPath + this.loadedFileAzon + File.separator + "alairt" + File.separator + this.loadedFileAzon + ".urlap.anyk.ASiC";
      if (!(new NecroFile(var8)).delete()) {
         var1.add(var8);
         var2 = false;
      }

      var8 = this.sp.srcPath + this.loadedFileAzon + File.separator + "alairt";
      if (!(new NecroFile(var8)).delete()) {
         var1.add(var8);
         var2 = false;
      }

      var8 = this.sp.srcPath + this.loadedFileAzon;
      if (!(new NecroFile(var8)).delete()) {
         var1.add(var8);
         var2 = false;
      }

      Menubar.thisinstance.setState(MainPanel.NORMAL);
      MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("Módosítható");
      MainFrame.thisinstance.mp.setReadonly(false);
      if (!var2) {
         EbevTools.showExtendedResultDialog((JDialog)null, "Néhány aláírást tartalmazó .anyk.ASiC fájl törlése nem sikerült a \n" + var7 + " mappából. Kérjük ezeket más módon törölje!", var1, 0);
      } else {
         GuiUtil.showMessageDialog(this.getParent(), "Az aláírásokat sikeresen visszavonta!", "Aláírások visszavonása", 1);
      }

      try {
         Ebev.log(14, this.bookModel.cc.getLoadedfile());
      } catch (Exception var6) {
         ErrorList.getInstance().writeError(new Long(4001L), "AVDH aláírás visszavonásának naplózása nem sikerült!", IErrorList.LEVEL_ERROR, var6, (Object)null);
      }

      this.setSignerCount();
   }

   private void handleCreateXCZ() throws Exception {
      int var1 = this.checkSignCount();
      String var2 = "";
      if (var1 < 1) {
         File var3 = new NecroFile(this.sp.srcPath + this.loadedFileAzon + File.separator + "alairt");
         if (var3.exists()) {
            String[] var4 = var3.list();
            if (var4.length < 1) {
               var2 = "A nyomtatvány illetve a csatolmányok a program szerint semmilyen módon nincsenek aláírva!\nMégis folytatja?";
            }

            if (var4.length > 1) {
               GuiUtil.showMessageDialog(this.getParent(), "A külső aláírással ellátott nyomtatvány lenyomatot tartalmazó mappában\n" + var3.getAbsolutePath() + "\ntöbb fájl is található!\nA művelet így nem folytatható!!", "XCZ fájl készítése", 0);
               return;
            }
         } else {
            var2 = "A nyomtatvány illetve a csatolmányok a program szerint semmilyen módon nincsenek aláírva!\nMégis folytatja?";
         }

         if (!"".equals(var2) && JOptionPane.showOptionDialog(MainFrame.thisinstance, var2, "Kérdés", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 1) {
            return;
         }
      }

      Hashtable var11 = new Hashtable();
      String var12 = this.sp.root + "";
      if (!var12.endsWith(File.separator)) {
         var12 = var12 + File.separator;
      }

      var12 = var12 + PropertyList.getInstance().get("prop.usr.tmp");
      if (!var12.endsWith(File.separator)) {
         var12 = var12 + File.separator;
      }

      var12 = var12 + this.loadedFileAzon + "_" + this.bookModel.get_formid() + ".atc";
      FileOutputStream var5 = new NecroFileOutputStream(var12);
      var5.write("encoding=\"utf-8\"\n".getBytes("utf-8"));
      File var6;
      if (this.atcTableModel.getRowCount() > 0) {
         var6 = (new NecroFile((String)this.atcTableModel.getValueAt(0, 0))).getParentFile();
      } else {
         var6 = new NecroFile(this.sp.root + PropertyList.getInstance().get("prop.usr.attachment") + this.loadedFileAzon + File.separator + this.bookModel.get_formid());
      }

      for(int var9 = 0; var9 < this.atcTableModel.getRowCount(); ++var9) {
         String var10 = (String)this.atcTableModel.getValueAt(var9, 0);
         File var7 = new NecroFile(var10);
         var5.write((var7.getParentFile().getName() + File.separator + var7.getName() + ";" + this.atcTableModel.getValueAt(var9, 1) + ";" + this.atcTableModel.getValueAt(var9, 2) + "\n").getBytes("utf-8"));
         var11.put(var10, var7.getParentFile().getName() + File.separator + var7.getName());
         File var8 = new NecroFile(var10 + ".anyk.ASiC");
         if (var8.exists()) {
            var11.put(var8, var8.getParentFile().getName() + File.separator + var8.getName());
         }
      }

      var5.close();
      var11.put(var12, (new NecroFile(var12)).getName());
      String var13 = this.sp.srcPath + this.loadedFileAzon + ".xml";
      var11.put(var13, (new NecroFile(var13)).getName());
      if ("".equals(var2)) {
         var13 = this.sp.srcPath + this.loadedFileAzon + File.separator + "alairt";
         File[] var14 = (new NecroFile(var13)).listFiles();
         var11.put(var14[0], var6.getName() + File.separator + var14[0].getName());
      }

      File var15 = new NecroFile(this.sp.krdir + "fizikai_adathordozo");
      if (!var15.exists()) {
         var15.mkdir();
      }

      this.zipInBackground(var11, var12);
   }

   private void openSourceFolder(String var1) {
      try {
         Desktop.getDesktop().open(new NecroFile(var1));
      } catch (Exception var3) {
         ErrorList.getInstance().writeError(new Long(4009L), "Nem sikerült a " + var1 + " mappa megnyitása!", IErrorList.LEVEL_ERROR, var3, (Object)null);
      }

   }

   private int checkSignCount() {
      if (this.atcTableModel.getRowCount() == 0) {
         return "-".equals((String)this.dataTable.getValueAt(0, 1)) ? 0 : 1;
      } else {
         int var1 = 0;
         String var2 = (String)this.atcTableModel.getValueAt(0, 3);
         boolean var3 = "-".equals(var2);

         boolean var4;
         for(var4 = false; var1 < this.atcTableModel.getRowCount(); ++var1) {
            if ("-".equals(this.atcTableModel.getValueAt(var1, 3))) {
               var3 = true;
            }

            if (!var2.equals(this.atcTableModel.getValueAt(var1, 3))) {
               var4 = true;
            }
         }

         if ("-".equals(this.dataTableModel.getValueAt(0, 1))) {
            var3 = true;
         }

         if (!var2.equals(this.dataTableModel.getValueAt(0, 1))) {
            var4 = true;
         }

         if (!var3 && !var4) {
            return 1;
         } else if (var3 && var4) {
            return -1;
         } else if (var3 && !var4) {
            return 0;
         } else {
            return 2;
         }
      }
   }

   private int checkCheckedRowSignCount() {
      if (this.atcTableModel.getRowCount() == 0) {
         return "-".equals((String)this.dataTable.getValueAt(0, 1)) && (Boolean)this.dataTable.getValueAt(0, 2) ? 0 : 1;
      } else {
         int var1 = 0;
         String var2 = this.searchFirstCheckedValue();
         boolean var3 = "-".equals(var2);
         boolean var4 = false;

         while(var1 < this.atcTableModel.getRowCount()) {
            if (!(Boolean)this.atcTableModel.getValueAt(var1, 4)) {
               ++var1;
            } else {
               if ("-".equals(this.atcTableModel.getValueAt(var1, 3))) {
                  var3 = true;
               }

               if (!var2.equals(this.atcTableModel.getValueAt(var1, 3))) {
                  var4 = true;
               }

               ++var1;
            }
         }

         if ((Boolean)this.dataTable.getValueAt(0, 2)) {
            if ("-".equals(this.dataTableModel.getValueAt(0, 1))) {
               var3 = true;
            }

            if (!var2.equals(this.dataTableModel.getValueAt(0, 1))) {
               var4 = true;
            }
         }

         if (!var3 && !var4) {
            return 1;
         } else if (var3 && var4) {
            return -1;
         } else if (var3 && !var4) {
            return 0;
         } else {
            return 2;
         }
      }
   }

   private String searchFirstCheckedValue() {
      for(int var1 = 0; var1 < this.atcTableModel.getRowCount(); ++var1) {
         if ((Boolean)this.atcTableModel.getValueAt(0, 4)) {
            return (String)this.atcTableModel.getValueAt(0, 3);
         }
      }

      return "";
   }

   private boolean fileMarked2Sign(String var1) {
      for(int var2 = 0; var2 < this.atcTable.getRowCount(); ++var2) {
         if (var1.equals(this.atcTable.getValueAt(var2, 0))) {
            return (Boolean)this.atcTable.getValueAt(var2, 4);
         }
      }

      return false;
   }

   private boolean hasAnySignableFile() {
      for(int var1 = 0; var1 < this.atcTable.getRowCount(); ++var1) {
         if ((Boolean)this.atcTable.getValueAt(var1, 4)) {
            return true;
         }
      }

      return (Boolean)this.dataTable.getValueAt(0, 2);
   }

   private boolean hasFirstAndOtherSignableFile() {
      if (this.atcTable.getRowCount() == 0) {
         return false;
      } else {
         int var1 = 0;
         int var2 = 0;

         for(int var3 = 0; var3 < this.atcTable.getRowCount(); ++var3) {
            if ((Boolean)this.atcTable.getValueAt(var3, 4)) {
               if ("-".equals(this.atcTable.getValueAt(var3, 3))) {
                  ++var1;
               } else {
                  ++var2;
               }
            }
         }

         if ((Boolean)this.dataTable.getValueAt(0, 2)) {
            if ("-".equals(this.dataTable.getValueAt(0, 1))) {
               ++var1;
            } else {
               ++var2;
            }
         }

         if (var1 + var2 == 0) {
            return false;
         } else if (var1 > 0 && var2 > 0) {
            return true;
         } else {
            return false;
         }
      }
   }

   private void zipInBackground(final Hashtable var1, final String var2) {
      final JDialog var3 = new JDialog(MainFrame.thisinstance, "Csatolmány fájlok másolása", true);
      var3.setDefaultCloseOperation(0);
      final SwingWorker var4 = new SwingWorker() {
         public Object doInBackground() throws InterruptedException {
            try {
               Thread.sleep(200L);
               String var1x = AttachmentListDialog.this.sp.krdir + "fizikai_adathordozo" + File.separator + AttachmentListDialog.this.loadedFileAzon + ".xcz";
               int var2x = Tools.zipFileAndRename(var1, var1x, false);
               FileOutputStream var3x = null;

               try {
                  var3x = new NecroFileOutputStream(AttachmentListDialog.this.sp.destPath + AttachmentListDialog.this.loadedFileAzon + ".xcz" + "_status");
                  var3x.write((AttachmentListDialog.this.sp.krdir + "fizikai_adathordozo" + File.separator + AttachmentListDialog.this.loadedFileAzon + ".xcz").getBytes());
               } catch (Exception var20) {
                  var2x = -4;
               } finally {
                  try {
                     var3x.close();
                     (new NecroFile(var2)).delete();
                  } catch (Exception var16) {
                  }

               }

               try {
                  Ebev.log(13, AttachmentListDialog.this.bookModel.cc.getLoadedfile());
               } catch (Exception var19) {
                  ErrorList.getInstance().writeError(new Long(4001L), "Az XCZ fájl készítése nem sikerült!", IErrorList.LEVEL_ERROR, var19, (Object)null);
               }

               if (var2x == 0) {
                  return "";
               } else {
                  try {
                     (new NecroFile(var1x)).delete();
                  } catch (Exception var18) {
                  }

                  try {
                     (new NecroFile(AttachmentListDialog.this.sp.destPath + AttachmentListDialog.this.loadedFileAzon + ".xcz" + "_status")).delete();
                  } catch (Exception var17) {
                  }

                  return "Hiba az XCZ fájl készítésekor!";
               }
            } catch (Exception var22) {
               return var22.getMessage();
            }
         }

         public void done() {
            try {
               AttachmentListDialog.this.cmdObject = this.get();
               if (AttachmentListDialog.this.cmdObject != null) {
                  try {
                     var3.setVisible(false);
                  } catch (Exception var2x) {
                     Tools.eLog(var2x, 0);
                  }

                  if ("".equals(AttachmentListDialog.this.cmdObject)) {
                     if (JOptionPane.showOptionDialog(MainFrame.thisinstance, "A külső adathordozóra másolható " + AttachmentListDialog.this.sp.krdir + "fizikai_adathordozo" + File.separator + AttachmentListDialog.this.loadedFileAzon + ".xcz" + " fájl elkészült!\n" + "Megnyissuk a csomagot tartalmazó mappát?", "Kérdés", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0) {
                        AttachmentListDialog.this.openSourceFolder(AttachmentListDialog.this.sp.krdir + "fizikai_adathordozo");
                     }

                     Menubar.thisinstance.setState(MainPanel.READONLY);
                     MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("Adathordozóhoz másolva");
                     MainFrame.thisinstance.mp.setReadonly(true);
                  } else {
                     GuiUtil.showMessageDialog(AttachmentListDialog.this.getParent(), AttachmentListDialog.this.cmdObject, "XCZ fájl készítése", 0);
                  }
               }
            } catch (Exception var3x) {
               var3x.printStackTrace();
               AttachmentListDialog.this.cmdObject = null;
            }

         }
      };
      var3.addWindowListener(new WindowAdapter() {
         public void windowOpened(WindowEvent var1) {
            var4.execute();
         }
      });
      int var5 = MainFrame.thisinstance.getX() + MainFrame.thisinstance.getWidth() / 2 - 250;
      if (var5 < 0) {
         var5 = 0;
      }

      int var6 = MainFrame.thisinstance.getY() + MainFrame.thisinstance.getHeight() / 2 - 200;
      if (var6 < 0) {
         var6 = 0;
      }

      var3.setBounds(var5, var6, 600, 100);
      var3.setSize(600, 100);
      JPanel var7 = new JPanel((LayoutManager)null, true);
      var7.setLayout(new BoxLayout(var7, 1));
      BevelBorder var8 = new BevelBorder(0);
      var7.setBorder(var8);
      Dimension var9 = new Dimension(600, 70);
      var7.setPreferredSize(var9);
      JLabel var10 = new JLabel("Kérjük várjon, az xcz fájl készítése folyamatban...");
      var10.setPreferredSize(var9);
      var10.setAlignmentX(0.5F);
      var7.add(var10);
      var7.setVisible(true);
      var3.getContentPane().add(var7);
      var3.pack();
      var3.setVisible(true);
   }

   private class IconTableHeaderMouseAdapter extends MouseAdapter {
      private IconTableHeaderMouseAdapter() {
      }

      public void mouseEntered(MouseEvent var1) {
         AttachmentListDialog.this.setCursor(AttachmentListDialog.this.helpCursor);
      }

      public void mouseExited(MouseEvent var1) {
         AttachmentListDialog.this.setCursor(AttachmentListDialog.this.defaultCursor);
      }

      public void mouseClicked(MouseEvent var1) {
         GuiUtil.showMessageDialog(AttachmentListDialog.this.getParent(), "Technikai okokból a táblázatban látható fájlok többszörös aláírását csoportokban végezzük.\nElőfordulhat, hogy az egyes csoportok aláírása nem sikerül. Azért, hogy ebben az esetben\nne kelljen elölről kezdeni az egész folyamatot, lehetősége van csak a kijelölt csatolmányokat aláírni.\nEz praktikusan azt jelenti, hogy ebben az esetben elég azokat a sorokat kijelölni, ahol az 'aláírók száma' kevesebb.\n\n Az aláírandó fájlok kijelöléshez kérjük használja az utolsó oszlopban lévő jelölőnégyzeteket!", "Üzenet", 1);
      }

      // $FF: synthetic method
      IconTableHeaderMouseAdapter(Object var2) {
         this();
      }
   }

   public class IconTableCellRenderer implements TableCellRenderer {
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         return (JComponent)var2;
      }
   }

   private class ClickListener implements MouseListener, MouseMotionListener {
      private ClickListener() {
      }

      public void mouseClicked(MouseEvent var1) {
         if ("dataTable".equals(((JTable)var1.getSource()).getName())) {
            AttachmentListDialog.this.lastSelectedTable = 0;
            AttachmentListDialog.this.atcTable.getSelectionModel().clearSelection();
         } else {
            AttachmentListDialog.this.lastSelectedTable = 1;
            AttachmentListDialog.this.dataTable.getSelectionModel().clearSelection();
         }

         if (var1.getClickCount() == 2) {
            String var2;
            if ("atcTable".equals(((JTable)var1.getSource()).getName())) {
               if (AttachmentListDialog.this.atcTable.getSelectedColumn() == 3) {
                  var2 = (String)AttachmentListDialog.this.pdfFiles.get(AttachmentListDialog.this.atcTable.getValueAt(AttachmentListDialog.this.atcTable.getSelectedRow(), 0));
                  AttachmentListDialog.this.execute((new NecroFile(var2)).getParentFile(), var2);
               }
            } else {
               var2 = (String)AttachmentListDialog.this.pdfFiles.get(AttachmentListDialog.this.dataTable.getValueAt(AttachmentListDialog.this.dataTable.getSelectedRow(), 0));
               AttachmentListDialog.this.execute((new NecroFile(var2)).getParentFile(), var2);
            }
         }

      }

      public void mouseEntered(MouseEvent var1) {
      }

      public void mouseExited(MouseEvent var1) {
      }

      public void mousePressed(MouseEvent var1) {
      }

      public void mouseReleased(MouseEvent var1) {
      }

      public void mouseDragged(MouseEvent var1) {
      }

      public void mouseMoved(MouseEvent var1) {
         try {
            if ("dataTable".equals(((JTable)var1.getSource()).getName())) {
               AttachmentListDialog.this.dataTable.setToolTipText((String)AttachmentListDialog.this.dataTable.getValueAt(AttachmentListDialog.this.dataTable.rowAtPoint(var1.getPoint()), AttachmentListDialog.this.dataTable.columnAtPoint(var1.getPoint())));
            } else {
               AttachmentListDialog.this.atcTable.setToolTipText((String)AttachmentListDialog.this.atcTable.getValueAt(AttachmentListDialog.this.atcTable.rowAtPoint(var1.getPoint()), AttachmentListDialog.this.atcTable.columnAtPoint(var1.getPoint())));
            }
         } catch (Exception var3) {
            AttachmentListDialog.this.atcTable.setToolTipText((String)null);
            Tools.eLog(var3, 0);
         }

      }

      // $FF: synthetic method
      ClickListener(Object var2) {
         this();
      }
   }
}
