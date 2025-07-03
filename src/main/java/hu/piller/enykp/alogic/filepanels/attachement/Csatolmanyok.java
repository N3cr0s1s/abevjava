package hu.piller.enykp.alogic.filepanels.attachement;

import hu.piller.enykp.alogic.ebev.extendedsign.AsicPdfHandler;
import hu.piller.enykp.alogic.filesaver.enykinner.EnykInnerSaver;
import hu.piller.enykp.alogic.fileutil.FileNameResolver;
import hu.piller.enykp.alogic.kontroll.ReadOnlyTableModel;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.interfaces.IOsHandler;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.oshandler.OsFactory;
import hu.piller.tools.TableSorter;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.table.DefaultTableCellRenderer;

public class Csatolmanyok extends JDialog implements TableModelListener, ItemListener {
   public static final int MAIN_WIDTH = 700;
   public static final int MAIN_HEIGHT = 420;
   public static final String NO_SIGNER = "-";
   IOsHandler oh;
   IPropertyList iplMaster = PropertyList.getInstance();
   BookModel bookModel;
   JFrame mainFrame;
   Object[] defaultFileColumns;
   ReadOnlyTableModel fileTableModel;
   final TableSorter sorter;
   final JTable fileTable;
   EJFileChooser fc = new EJFileChooser();
   JComboBox attachementTypeCombo;
   JButton addButton = new JButton("Hozzáad");
   JButton delButton = new JButton("Töröl");
   JButton showButton = new JButton("Megtekint");
   JButton pdfShowButton = new JButton("Aláírók");
   JButton noteButton = new JButton("Megjegyzés");
   JButton okButton = new JButton("Ok");
   JButton cancelButton = new JButton("Mégsem");
   JDialog noteInputDialog = new JDialog(this, "Megjegyzés", true);
   JDialog renameDialog = new JDialog(this, "Átnevezés", true);
   JTextField noteTextField = new JTextField();
   JButton noteOkButton = new JButton("OK");
   JTextField renameTextField = new JTextField();
   JButton renameOkButton = new JButton("OK");
   JButton renameOWButton = new JButton("Felülír");
   JButton renameCancelButton = new JButton("Mégsem");
   JLabel renameLabel1 = new JLabel("Ilyen nevű fájl már létezik a csatolmány mappában.");
   JLabel renameLabel2 = new JLabel("Kérjük adja meg milyen néven kerüljön oda.");
   JLabel renameLabel3 = new JLabel("Amennyiben a \"Mégsem\" gombot választja");
   JLabel renameLabel4 = new JLabel("a másolás félbeszakad és átnevezheti a megfelelő fájlokat.");
   JLabel attachementTypeLabel = new JLabel("Csatolmányok típusa:");
   JLabel attachementCountLabel = new JLabel("Csatoltak:");
   JLabel attachementCount = new JLabel();
   JLabel attachementRequired = new JLabel();
   JLabel attachementFileExtensionsLabel = new JLabel("File típusok:");
   JLabel attachementFileExtensions = new JLabel();
   Csatolmanyok.EnterKeyListener ekl = new Csatolmanyok.EnterKeyListener();
   Csatolmanyok.ClickListener cl = new Csatolmanyok.ClickListener();
   Hashtable setAttachment;
   Hashtable pdf4Signers = new Hashtable();
   HashSet<File> asicFiles = new HashSet();
   HashSet<File> urlapXmlFiles = new HashSet();
   public static final String[] autoRename = new String[]{"Abbahagyom és átnevezem", "Automatikus átnevezés"};
   boolean overwrite = false;
   boolean readOnly = false;
   boolean hasAttachementTagInXml = true;
   boolean hasAlert = false;
   static String sysroot;
   static String root;
   static String attachPath;
   static String fileNev;
   AttachementInfo[] attachementInfoArray;
   long oneAttachementSize = 0L;
   long allAttachementSize = 0L;
   long attachedFileSize = 0L;
   String attachementFromXmlHead;
   private boolean AVDH;
   private boolean hasAVDHFile = false;
   private String copyMessage = "";
   private static HashSet<Character> disabledChars = new HashSet();
   Object cmdObject;

   public Csatolmanyok(JFrame var1, BookModel var2) throws HeadlessException {
      super(var1, "Csatolmányok kezelése", true);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosed(WindowEvent var1) {
            Enumeration var2 = Csatolmanyok.this.pdf4Signers.keys();
            boolean var3 = true;

            while(var2.hasMoreElements()) {
               Object var4 = var2.nextElement();

               try {
                  File var5 = new NecroFile((String)Csatolmanyok.this.pdf4Signers.get(var4));
                  var5.delete();
               } catch (Exception var6) {
                  var3 = false;
               }
            }

         }
      });
      this.mainFrame = var1;
      this.bookModel = var2;
      this.AVDH = var2.isAvdhModel();
      if (this.AVDH) {
         this.defaultFileColumns = new Object[]{"Csatolmány neve", "Megjegyzés", "Típus", "Aláírók"};
      } else {
         this.defaultFileColumns = new Object[]{"Csatolmány neve", "Megjegyzés", "Típus"};
      }

      this.fileTableModel = new ReadOnlyTableModel(this.defaultFileColumns, 0);
      this.sorter = new TableSorter(this.fileTableModel);
      this.fileTable = new JTable(this.sorter);
      if (this.fileTable.getColumnModel().getColumnCount() > 2) {
         this.fileTable.getColumnModel().getColumn(1).setMinWidth(80);
         this.fileTable.getColumnModel().getColumn(2).setMinWidth(80);
      }

      if (this.AVDH) {
         if (this.fileTable.getColumnModel().getColumnCount() > 3) {
            this.fileTable.getColumnModel().getColumn(0).setMinWidth(220);
            this.fileTable.getColumnModel().getColumn(3).setMinWidth(20);
         }
      } else if (this.fileTable.getColumnModel().getColumnCount() > 0) {
         this.fileTable.getColumnModel().getColumn(0).setMinWidth(260);
      }

      if (GuiUtil.modGui()) {
         this.fileTable.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
      }

      boolean var3 = false;
      int var4 = this.mainFrame.getX() + this.mainFrame.getWidth() / 2 - 350;
      if (var4 < 0) {
         var4 = 0;
      }

      int var5 = this.mainFrame.getY() + this.mainFrame.getHeight() / 2 - 210;
      if (var5 < 0) {
         var5 = 0;
      }

      this.setBounds((GuiUtil.getScreenW() - 700) / 2, var5, 700, 420);
      JPanel var6 = new JPanel(new BorderLayout(0, 10));
      JPanel var7 = new JPanel(new BorderLayout(10, 0));
      JPanel var8 = new JPanel();
      JPanel var9 = new JPanel(new FlowLayout(2));
      JPanel var10 = new JPanel((LayoutManager)null);
      JPanel var11 = new JPanel((LayoutManager)null);
      JScrollPane var12 = new JScrollPane(this.fileTable, 20, 30);
      var12.setPreferredSize(new Dimension(400, 300));
      JLabel var13 = new JLabel("Csatolmányok listája");
      EmptyBorder var14 = new EmptyBorder(5, 5, 5, 5);
      EmptyBorder var15 = new EmptyBorder(10, 10, 10, 10);
      this.noteInputDialog.getContentPane().setLayout(new FlowLayout());
      int var16 = (int)(1.5D * (double)GuiUtil.getCommonItemHeight());
      int var17 = GuiUtil.getW(this.renameLabel4, this.renameLabel4.getText() + "WWWW");
      Dimension var18 = new Dimension(var17, var16 + 80);
      this.noteTextField.setPreferredSize(new Dimension(var17 - 200, var16));
      this.noteOkButton.setSize(200, var16);
      this.noteInputDialog.getContentPane().add(this.noteTextField);
      this.noteInputDialog.getContentPane().add(this.noteOkButton);
      this.noteInputDialog.setBounds(var4, var5, var17, var16);
      this.noteInputDialog.setSize(var18);
      this.noteInputDialog.setPreferredSize(var18);
      this.noteInputDialog.setMinimumSize(var18);
      this.noteInputDialog.setResizable(true);
      this.renameDialog.getContentPane().setLayout(new BorderLayout());
      JPanel var19 = new JPanel(new BorderLayout());
      var19.setBorder(var14);
      JPanel var20 = new JPanel();
      var20.setLayout(new BoxLayout(var20, 1));
      var20.add(this.renameLabel1);
      var20.add(this.renameLabel2);
      var20.add(this.renameLabel3);
      var20.add(this.renameLabel4);
      this.renameTextField.setPreferredSize(new Dimension(var17 - 10, GuiUtil.getCommonItemHeight() + 4));
      var19.add(var20, "Center");
      var19.add(this.renameTextField, "South");
      JPanel var21 = new JPanel(new FlowLayout(1));
      var21.setBorder(var14);
      var21.add(this.renameOkButton);
      var21.add(this.renameOWButton);
      var21.add(this.renameCancelButton);
      var21.setSize(new Dimension(var17, GuiUtil.getCommonItemHeight() + 20));
      var21.setPreferredSize(var21.getSize());
      var21.setMinimumSize(var21.getSize());
      this.renameDialog.getContentPane().add(var19, "Center");
      this.renameDialog.getContentPane().add(var21, "South");
      this.renameDialog.setBounds(var4, var5, var17, 8 * (GuiUtil.getCommonItemHeight() + 4));
      this.renameDialog.setResizable(true);
      var8.add(this.showButton);
      var8.add(this.noteButton);
      if (this.AVDH) {
         var8.add(this.pdfShowButton);
      }

      var9.add(this.okButton);
      var9.add(this.cancelButton);
      var7.add(var8, "West");
      var7.add(var9, "East");
      GuiUtil.setDynamicBound(this.addButton, this.addButton.getText(), 10, 0);
      GuiUtil.setDynamicBound(this.delButton, this.addButton.getText(), 10, GuiUtil.getCommonItemHeight() + 4);
      var10.add(this.addButton);
      var10.add(this.delButton);
      var10.setPreferredSize(new Dimension((int)(this.addButton.getBounds().getWidth() + 10.0D), 70));
      var10.setMinimumSize(var10.getPreferredSize());
      var6.setBorder(var15);
      var6.add(var13, "North");
      var6.add(var12, "Center");
      var6.add(var7, "South");
      var6.add(var10, "East");
      this.noteOkButton.addKeyListener(this.ekl);
      this.noteTextField.addKeyListener(this.ekl);
      this.renameTextField.addKeyListener(this.ekl);
      this.renameOkButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Csatolmanyok.this.renameDialog.dispose();
         }
      });
      this.renameOWButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Csatolmanyok.this.overwrite = true;
            Csatolmanyok.this.renameDialog.dispose();
         }
      });
      this.renameCancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Csatolmanyok.this.renameTextField.setText("");
            Csatolmanyok.this.renameDialog.dispose();
         }
      });
      this.fileTable.addMouseListener(this.cl);
      this.fileTable.addMouseMotionListener(this.cl);
      this.fileTable.getModel().addTableModelListener(this);
      this.sorter.setTableHeader(this.fileTable.getTableHeader());
      this.setButtonState(false);
      this.noteOkButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Csatolmanyok.this.setNote();
         }
      });
      this.addButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Csatolmanyok.this.showOpenDialog();
         }
      });
      this.delButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Csatolmanyok.this.deleteRowsFromTable();
         }
      });
      this.showButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            String var2 = "";
            String var3 = "";
            if (Csatolmanyok.this.fileTable.getRowCount() == 1) {
               var3 = (String)Csatolmanyok.this.fileTableModel.getValueAt(0, 0);
            } else {
               if (Csatolmanyok.this.fileTable.getSelectedRows().length <= 0) {
                  GuiUtil.showMessageDialog(Csatolmanyok.this.getParent(), "Nincs kijelölt fájl.", "Csatolmányok kezelése", 1);
                  return;
               }

               var3 = (String)Csatolmanyok.this.fileTableModel.getValueAt(Csatolmanyok.this.fileTable.getSelectedRows()[0], 0);
            }

            var2 = var3.substring(var3.lastIndexOf(".") + 1);
            var3 = "\"" + var3 + "\"";
            if (Csatolmanyok.this.setAttachment != null && Csatolmanyok.this.setAttachment.containsKey(var2)) {
               File var4 = new NecroFile((String)Csatolmanyok.this.setAttachment.get(var2));
               Csatolmanyok.this.execute(var4.getParentFile(), var4.getName() + " " + var3);
            } else {
               Csatolmanyok.this.execute((File)null, var3);
            }

         }
      });
      this.pdfShowButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            String var2 = "";
            String var3 = "";
            if (Csatolmanyok.this.fileTable.getRowCount() == 1) {
               var3 = (String)Csatolmanyok.this.fileTableModel.getValueAt(0, 0);
            } else {
               if (Csatolmanyok.this.fileTable.getSelectedRows().length <= 0) {
                  GuiUtil.showMessageDialog(Csatolmanyok.this.getParent(), "Nincs kijelölt fájl.", "Csatolmányok kezelése", 1);
                  return;
               }

               var3 = (String)Csatolmanyok.this.fileTableModel.getValueAt(Csatolmanyok.this.fileTable.getSelectedRows()[0], 0);
            }

            try {
               String var4 = (String)Csatolmanyok.this.pdf4Signers.get(var3);
               if (var4 == null) {
                  GuiUtil.showMessageDialog(Csatolmanyok.this.getParent(), "A fájl nincs AVDH szolgáltatással aláírva!", "Csatolmányok kezelése", 1);
                  return;
               }

               if (var4.equals("-")) {
                  return;
               }

               Csatolmanyok.this.execute((new NecroFile(var4)).getParentFile(), var4);
            } catch (Exception var5) {
               GuiUtil.showMessageDialog(Csatolmanyok.this.getParent(), "Nem sikerült megjeleníteni az aláírókat.", "Csatolmányok kezelése", 1);
            }

         }
      });
      this.noteButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Csatolmanyok.this.showNoteInputDialog();
         }
      });
      this.okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Csatolmanyok.this.saveAtcFile();
         }
      });
      this.cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Csatolmanyok.this.closeThis();
         }
      });
      this.fc.setMultiSelectionEnabled(true);
      this.fc.setDialogTitle("Csatolmány hozzáadása");

      String var22;
      try {
         this.loadAttachementSettings();

         try {
            Hashtable var41 = var2.get_templateheaddata();
            Hashtable var24 = (Hashtable)var41.get("docinfo");
            String var25 = Tools.getFormIdFromFormAttrs(var2);
            if (var25 != null) {
               var24.put("attachment", var25);
            }

            if (var24.get("attachment") == null) {
               this.attachementFromXmlHead = "0";
            } else {
               this.attachementFromXmlHead = (String)var24.get("attachment");
            }

            if (this.attachementFromXmlHead.equals("0")) {
               throw new Exception();
            }
         } catch (Exception var39) {
            this.hasAlert = true;
            if (var2.isSingle()) {
               GuiUtil.showMessageDialog(this.mainFrame, "Ehhez a nyomtatványhoz nem csatolható állomány !", "Csatolmányok kezelése", 1);
            } else {
               GuiUtil.showMessageDialog(this.mainFrame, "Ehhez az alnyomtatványhoz nem csatolható állomány !", "Csatolmányok kezelése", 1);
            }

            throw new Exception("NOATTACHEMENT");
         }

         Vector var42 = var2.attachementsall;

         Vector var44;
         try {
            var44 = AttachementTool.parseAtcDataFromTemplate(var42, var2.getIndex(var2.get()));
            if (var44 == null) {
               if (!this.hasAlert) {
                  GuiUtil.showMessageDialog(this.getParent(), "Ehhez a nyomtatványhoz nem csatolhat csatolmányt", "Csatolmányok kezelése", 0);
               }

               return;
            }

            this.oneAttachementSize = (long)(Integer)var44.get(0);
            this.allAttachementSize = (long)(Integer)var44.get(1);
            this.attachementInfoArray = (AttachementInfo[])((AttachementInfo[])var44.get(2));
            if (this.attachementInfoArray.length == 0) {
               throw new NoSuchFieldException("forcedException");
            }
         } catch (NoSuchFieldException var38) {
            NoSuchFieldException var43 = var38;
            this.hasAttachementTagInXml = var38.getMessage().equals("forcedException");

            try {
               this.attachementInfoArray = new AttachementInfo[1];
               Hashtable var45 = new Hashtable();
               var45.put("id", "1");
               var45.put("description", "Csatolmány");
               var45.put("required", Boolean.FALSE);
               var45.put("min_count", "0");
               var45.put("max_count", "0");
               var45.put("file_extensions", "*");
               if (!var43.getMessage().equals("forcedException")) {
                  this.oneAttachementSize = 0L;
               }

               this.allAttachementSize = 0L;
               this.attachementInfoArray[0] = new AttachementInfo(var45);
            } catch (Exception var37) {
               Tools.eLog(var38, 0);
            }
         }

         var44 = this.parseCsI(this.attachementInfoArray);
         this.attachementTypeCombo = new JComboBox(var44);

         try {
            this.attachementTypeCombo.setToolTipText(this.attachementTypeCombo.getSelectedItem().toString());
         } catch (Exception var36) {
            this.attachementTypeCombo.setToolTipText((String)null);
         }

         this.attachementTypeCombo.addItemListener(this);
         this.attachementFileExtensions.setText(this.attachementInfoArray[0].exts);
         this.attachementFileExtensions.setToolTipText(this.attachementInfoArray[0].exts);
         if (this.attachementFileExtensions.getText().equals("*")) {
            this.attachementFileExtensions.setText("Tetszőleges fájl");
         }

         this.attachementCount.setText("" + this.attachementInfoArray[0].attached + " db");
         this.attachementCount.setText(this.attachementCount.getText() + (this.attachementInfoArray[0].minCount > 0 ? " - legalább " + this.attachementInfoArray[0].minCount + " db" : ""));
         this.attachementCount.setText(this.attachementCount.getText() + (this.attachementInfoArray[0].maxCount > 0 ? " - legfeljebb " + this.attachementInfoArray[0].maxCount + " db" : ""));
         this.attachementRequired.setText("Dokumentum csatolása " + (this.attachementInfoArray[0].required ? "" : "nem ") + "kötelező");
         short var46 = 600;
         int var26 = GuiUtil.getCommonItemHeight() + 5;
         byte var27 = 5;
         GuiUtil.setDynamicBound(this.attachementTypeLabel, this.attachementTypeLabel.getText(), 10, var27);
         int var28 = GuiUtil.getPositionFromPrevComponent(this.attachementTypeLabel);
         this.attachementTypeCombo.setBounds(var28, var27, this.getMaxCsatolmanyTipusWidth(var44), GuiUtil.getCommonItemHeight() + 5);
         int var47 = (int)Math.max((double)var46, (double)var28 + this.attachementTypeCombo.getBounds().getWidth());
         int var48 = var27 + var26;
         GuiUtil.setDynamicBound(this.attachementCountLabel, this.attachementCountLabel.getText(), 10, var48);
         GuiUtil.setDynamicBound(this.attachementCount, this.attachementCount.getText(), var28, var48);
         var47 = (int)Math.max((double)var47, (double)var28 + this.attachementCount.getBounds().getWidth());
         var48 += var26;
         GuiUtil.setDynamicBound(this.attachementRequired, this.attachementRequired.getText(), var28, var48);
         var47 = (int)Math.max((double)var47, (double)var28 + this.attachementRequired.getBounds().getWidth());
         var48 += var26;
         GuiUtil.setDynamicBound(this.attachementFileExtensionsLabel, this.attachementFileExtensionsLabel.getText(), 10, var48);
         GuiUtil.setDynamicBound(this.attachementFileExtensions, this.attachementFileExtensions.getText(), GuiUtil.getPositionFromPrevComponent(this.attachementFileExtensionsLabel) + 5, var48);
         var47 = (int)Math.max((double)var47, 10.0D + this.attachementFileExtensionsLabel.getBounds().getWidth() + this.attachementFileExtensions.getBounds().getWidth());
         var48 += var26;
         String var29 = this.oneAttachementSize == 0L ? "nincs korlátozás" : "legfeljebb " + this.oneAttachementSize / 1024L + "kB";
         String var30 = this.allAttachementSize == 0L ? "nincs korlátozás" : "legfeljebb " + this.allAttachementSize / 1024L + " kB";
         JLabel var31 = new JLabel("Egy csatolmány mérete: " + var29 + ", összesített méret: " + var30);
         if (this.oneAttachementSize == 0L && this.allAttachementSize == 0L) {
            var31.setText("A csatolmány méretére nincs korlátozás");
         }

         GuiUtil.setDynamicBound(var31, var31.getText(), 10, var48);
         var47 = (int)Math.max((double)var47, 10.0D + var31.getBounds().getWidth());
         System.out.println(var31.getText());
         var48 += var26;
         var11.add(this.attachementTypeLabel);
         var11.add(this.attachementCountLabel);
         var11.add(this.attachementCount);
         var11.add(this.attachementRequired);
         var11.add(this.attachementFileExtensionsLabel);
         var11.add(this.attachementFileExtensions);
         var11.add(this.attachementTypeCombo);
         var11.add(var31);
         var11.setPreferredSize(new Dimension(var47, var48));
         this.getContentPane().add(var11, "North");
         this.getContentPane().add(var6, "Center");
         var22 = this.doPreCheck();
         fileNev = var22;

         try {
            this.initPath();
         } catch (Exception var35) {
            GuiUtil.showMessageDialog(this.mainFrame, "Hiányzó paraméter : " + var35.getMessage().substring(1), "Csatolmányok kezelése", 0);
            return;
         }

         this.hasAVDHFile = false;
         String var32 = this.loadAtcFile();
         if (var32 != null) {
            GuiUtil.showMessageDialog(this.mainFrame, "Hiba a csatolmány-leíró beolvasásakor:\n" + var32, "Csatolmányok kezelése", 0);
         }
      } catch (Exception var40) {
         Exception var23 = var40;

         try {
            if (!var23.getMessage().equals("NOATTACHEMENT")) {
               ErrorList.getInstance().writeError(new Integer(3000), "Hiba a csatolmányok kezelésekor", var23, (Object)null);
            }
         } catch (Exception var34) {
            ErrorList.getInstance().writeError(new Integer(3000), "Hiba a csatolmányok kezelésekor", var40, (Object)null);
         }

         var22 = null;
      }

      if (var22 != null) {
         if (this.readOnly) {
            this.addButton.setEnabled(false);
            this.showButton.setEnabled(this.fileTable.getRowCount() > 0);
            this.pdfShowButton.setEnabled(this.fileTable.getRowCount() > 0);
            this.okButton.setEnabled(false);
         }

         try {
            this.loadSettings(this);
         } catch (Exception var33) {
            System.out.println("A csatolmány ablak pozíciójának betöltése nem sikerült: " + var33.toString());
         }

         this.pack();
         this.setVisible(true);
      } else if (!this.hasAlert) {
         GuiUtil.showMessageDialog(this.getParent(), var3 ? "Ehhez a nyomtatványhoz nem csatolhat csatolmányt" : "Hiba történt a nyomtatványsablon feldolgozásakor.", "Csatolmányok kezelése", 0);
      }

      this.copyMessage = "";
   }

   private String doPreCheck() throws Exception {
      Result var1 = this.isSavable();
      if (!var1.isOk()) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, var1.errorList.get(0), "Csatolmányok kezelése", 0);
         return null;
      } else {
         String var2 = "";
         String var3 = "";
         boolean var4 = false;
         if (MainFrame.thisinstance.mp.state == 3) {
            if (this.bookModel.cc.getLoadedfile() == null) {
               var2 = "";
            } else {
               var2 = this.bookModel.cc.getLoadedfile().getAbsolutePath();
            }

            this.readOnly = true;
            return var2;
         } else {
            if (this.bookModel.dirty) {
               EnykInnerSaver var5 = new EnykInnerSaver(this.bookModel);
               if (this.bookModel.cc.getLoadedfile() != null) {
                  var2 = this.bookModel.cc.getLoadedfile().getAbsolutePath();
               } else {
                  FileNameResolver var6 = new FileNameResolver(this.bookModel);
                  var2 = var6.generateFileName();
               }

               var2 = var5.save(var2, -1).getAbsolutePath();
               if (var2 != null) {
                  this.bookModel.cc.setLoadedfile(new NecroFile(var2));
               }

               var4 = true;
            } else {
               try {
                  if (this.bookModel.cc.getLoadedfile() == null) {
                     FileNameResolver var8 = new FileNameResolver(this.bookModel);
                     var2 = var8.generateFileName();
                     EnykInnerSaver var9 = new EnykInnerSaver(this.bookModel);
                     var2 = var9.save(var2, -1).getAbsolutePath();
                     if (var2 != null) {
                        this.bookModel.cc.setLoadedfile(new NecroFile(var2));
                     }

                     var4 = true;
                  } else {
                     var2 = this.bookModel.cc.getLoadedfile().getAbsolutePath();
                  }
               } catch (Exception var7) {
                  var2 = "";
               }
            }

            if (var2 != null && (new NecroFile(var2)).exists()) {
               return var2;
            } else {
               throw new IOException("A nyomtatvány mentése sikertelen !");
            }
         }
      }
   }

   private void initPath() throws Exception {
      try {
         sysroot = (String)this.iplMaster.get("prop.sys.root");
         if (sysroot == null) {
            throw new Exception();
         }
      } catch (Exception var4) {
         throw new Exception("*prop.sys.root");
      }

      if (!sysroot.endsWith("\\") && !sysroot.endsWith("/")) {
         sysroot = sysroot + File.separator;
      }

      try {
         root = (String)this.iplMaster.get("prop.usr.root");
         if (root == null) {
            throw new Exception();
         }
      } catch (Exception var3) {
         throw new Exception("*prop.usr.root");
      }

      if (!root.endsWith("\\") && !root.endsWith("/")) {
         root = root + File.separator;
      }

      try {
         attachPath = (String)this.iplMaster.get("prop.usr.attachment");
         if (attachPath == null) {
            throw new Exception();
         }

         attachPath = root + attachPath;
      } catch (Exception var2) {
         throw new Exception("*prop.usr.attachment");
      }

      if (!attachPath.endsWith("\\") && !attachPath.endsWith("/")) {
         attachPath = attachPath + File.separator;
      }

      attachPath = Tools.beautyPath(attachPath);
   }

   private void handleEnter(KeyEvent var1) {
      int var2 = var1.getKeyCode();
      if (var2 == 10) {
         if (var1.getSource() == this.renameTextField) {
            this.renameDialog.dispose();
         } else {
            this.setNote();
         }
      }

   }

   private void setNote() {
      int var1 = this.noteTextField.getText().indexOf(";");
      if (var1 != -1) {
         GuiUtil.showMessageDialog(this.getParent(), "A ' ; ' karakter nem szerepelhet a megjegyzésben!", "Csatolmányok kezelése", 2);
      } else {
         if (this.fileTable.getRowCount() != 1) {
            for(int var2 = 0; var2 < this.fileTable.getSelectedRows().length; ++var2) {
               this.fileTableModel.setValueAt(this.noteTextField.getText(), this.fileTable.getSelectedRows()[var2], 1);
            }
         } else {
            this.fileTableModel.setValueAt(this.noteTextField.getText(), 0, 1);
         }

         this.noteInputDialog.setVisible(false);
      }
   }

   private void showOpenDialog() {
      try {
         ((BasicFileChooserUI)this.fc.getUI()).setFileName("");
      } catch (ClassCastException var11) {
         try {
            this.fc.setSelectedFile(new NecroFile(""));
         } catch (Exception var10) {
            Tools.eLog(var10, 0);
         }
      }

      for(int var1 = 0; this.fc.getChoosableFileFilters().length > 0 && var1 < 10; ++var1) {
         this.fc.removeChoosableFileFilter(this.fc.getChoosableFileFilters()[0]);
      }

      this.fc.addChoosableFileFilter(this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].filter);
      this.fc.setSelectedFile((File)null);
      File var2 = null;

      try {
         var2 = (File)PropertyList.getInstance().get("prop.dynamic.atcInitDir");
         if (var2 != null) {
            this.fc.setCurrentDirectory(var2);
         }
      } catch (Exception var9) {
         var2 = null;
      }

      int var3 = this.fc.showOpenDialog(this);
      if (var3 == 0) {
         try {
            PropertyList.getInstance().set("prop.dynamic.atcInitDir", this.fc.getSelectedFile().getParentFile());
         } catch (Exception var8) {
            PropertyList.getInstance().set("prop.dynamic.atcInitDir", (Object)null);
         }

         this.asicFiles.clear();
         this.urlapXmlFiles.clear();
         File[] var4 = this.fc.getSelectedFiles();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            try {
               if (var4[var5].getName().indexOf(".") == -1) {
                  GuiUtil.showMessageDialog(this.getParent(), "A " + var4[var5].getName() + " csatolmány nevéből nem állapítható meg a típusa (nem tartalmaz kiterjesztést).\nÍgy nem csatolható!", "Csatolmányok kezelése", 1);
                  break;
               }

               if (this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].maxCount > 0 && this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].attached >= this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].maxCount) {
                  GuiUtil.showMessageDialog(this.getParent(), "Ebből a csatolmánytípusból már nem csatolhat többet a nyomtatványhoz!", "Csatolmányok kezelése", 1);
                  break;
               }

               if (var4[var5].length() == 0L) {
                  GuiUtil.showMessageDialog(this.getParent(), "A " + var4[var5].getName() + " csatolmány 0 hosszú, ezért nem csatolható a nyomtatványhoz!", "Csatolmányok kezelése", 1);
                  break;
               }

               int var6 = AttachementTool.checkFileSize(var4[var5], this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].description, this.attachementInfoArray, this.oneAttachementSize, this.allAttachementSize, this.attachedFileSize);
               if (var6 != 0) {
                  if (var6 == 1) {
                     GuiUtil.showMessageDialog(this.getParent(), "A csatolmány mérete átlépte a megadott értékhatárt !", "Csatolmányok kezelése", 1);
                  } else if (var6 == 2) {
                     GuiUtil.showMessageDialog(this.getParent(), "A csatolmányok összesített mérete átlépte a megadott értékhatárt !", "Csatolmányok kezelése", 1);
                  }
                  break;
               }

               if (this.AVDH) {
                  if (var4[var5].getName().toLowerCase().endsWith(".anyk.ASiC".toLowerCase())) {
                     if (!this.handleAvdhExtension(var4[var5])) {
                        break;
                     }
                     continue;
                  }

                  if (var4[var5].getName().toLowerCase().endsWith(".urlap.anyk.xml".toLowerCase())) {
                     if (!this.handleAvdhXmlExtension(var4[var5])) {
                        break;
                     }
                     continue;
                  }
               }

               if (var4[var5].exists() && !this.alreadyInList(var4[var5].getAbsolutePath())) {
                  Vector var7 = new Vector(3);
                  var7.add(var4[var5].getAbsolutePath());
                  var7.add("");
                  var7.add(this.attachementTypeCombo.getSelectedItem());
                  this.fileTableModel.addRow(var7);
                  ++this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].attached;
                  this.attachedFileSize += var4[var5].length();
               }
            } catch (Exception var12) {
               Tools.eLog(var12, 0);
            }
         }

         this.renameAndAddAsicFiles();
         this.renameAndAddXmlFiles();
         this.attachementCount.setText("" + this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].attached + " db");
         this.attachementCount.setText(this.attachementCount.getText() + (this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].minCount > 0 ? " - legalább " + this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].minCount + " db" : ""));
         this.attachementCount.setText(this.attachementCount.getText() + (this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].maxCount > 0 ? " - legfeljebb " + this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].maxCount + " db" : ""));
      }

   }

   private boolean handleAvdhExtension(File var1) {
      String var2 = var1.getName().substring(0, var1.getName().toLowerCase().indexOf(".anyk.asic")) + ".ASiC";
      if (JOptionPane.showOptionDialog(this, "A " + var1.getName() + " \nfájl egy korábban már hitelesített csatolmány lenyomatot tartalmaz. Ennek csatolása ezen a néven nem lehetséges.\nBefejezi a csatolmányok hozzáadását és átnevezi a fájlt, vagy hozzáadja " + var2 + " néven", "Csatolmányok kezelése", 0, 3, (Icon)null, autoRename, autoRename[0]) == 0) {
         return false;
      } else {
         this.asicFiles.add(var1);
         return true;
      }
   }

   private boolean handleAvdhXmlExtension(File var1) {
      String var2 = var1.getName().substring(0, var1.getName().toLowerCase().indexOf(".urlap.anyk.xml")) + ".xml";
      if (JOptionPane.showOptionDialog(this, "A " + var1.getName() + " \nfájl egy korábban már hitelesített nyomtatvány adatait tartalmazza. Ennek csatolása ezen a néven nem lehetséges.\nBefejezi a csatolmányok hozzáadását és átnevezi a fájlt, vagy hozzáadja " + var2 + " néven", "Csatolmányok kezelése", 0, 3, (Icon)null, autoRename, autoRename[0]) == 0) {
         return false;
      } else {
         this.urlapXmlFiles.add(var1);
         return true;
      }
   }

   private boolean renameAndAddAsicFiles() {
      boolean var1 = true;
      HashSet var2 = new HashSet();
      Iterator var3 = this.asicFiles.iterator();

      File var4;
      String var5;
      while(var3.hasNext()) {
         var4 = (File)var3.next();
         var5 = var4.getAbsolutePath().substring(0, var4.getAbsolutePath().length() - ".anyk.ASiC".length()) + ".ASiC";
         if (!var4.renameTo(new NecroFile(var5))) {
            var1 = false;
         } else {
            var2.add(new NecroFile(var5));
         }
      }

      if (!var1) {
         var3 = var2.iterator();

         while(var3.hasNext()) {
            var4 = (File)var3.next();
            var5 = var4.getAbsolutePath().substring(0, var4.getAbsolutePath().length() - ".asic".length()) + ".anyk.ASiC";
            var4.renameTo(new NecroFile(var5));
         }
      } else {
         for(var3 = var2.iterator(); var3.hasNext(); this.attachedFileSize += var4.length()) {
            var4 = (File)var3.next();
            Vector var6 = new Vector(3);
            var6.add(var4.getAbsolutePath());
            var6.add("");
            var6.add(this.attachementTypeCombo.getSelectedItem());
            this.fileTableModel.addRow(var6);
            ++this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].attached;
         }
      }

      return var1;
   }

   private boolean renameAndAddXmlFiles() {
      boolean var1 = true;
      HashSet var2 = new HashSet();
      Iterator var3 = this.urlapXmlFiles.iterator();

      File var4;
      String var5;
      while(var3.hasNext()) {
         var4 = (File)var3.next();
         var5 = var4.getAbsolutePath().substring(0, var4.getAbsolutePath().length() - ".urlap.anyk.xml".length()) + ".xml";
         if (!var4.renameTo(new NecroFile(var5))) {
            var1 = false;
         } else {
            var2.add(new NecroFile(var5));
         }
      }

      if (!var1) {
         var3 = var2.iterator();

         while(var3.hasNext()) {
            var4 = (File)var3.next();
            var5 = var4.getAbsolutePath().substring(0, var4.getAbsolutePath().length() - ".xml".length()) + ".urlap.anyk.xml";
            var4.renameTo(new NecroFile(var5));
         }
      } else {
         for(var3 = var2.iterator(); var3.hasNext(); this.attachedFileSize += var4.length()) {
            var4 = (File)var3.next();
            Vector var6 = new Vector(3);
            var6.add(var4.getAbsolutePath());
            var6.add("");
            var6.add(this.attachementTypeCombo.getSelectedItem());
            this.fileTableModel.addRow(var6);
            ++this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].attached;
         }
      }

      return var1;
   }

   private boolean alreadyInList(String var1) {
      for(int var2 = 0; var2 < this.fileTableModel.getDataVector().size(); ++var2) {
         if (((String)((Vector)this.fileTableModel.getDataVector().elementAt(var2)).elementAt(0)).equalsIgnoreCase(var1)) {
            return true;
         }
      }

      return false;
   }

   private void deleteRowsFromTable() {
      if (this.fileTable.getRowCount() != 1) {
         if (this.fileTable.getSelectedRows().length > 0) {
            if (JOptionPane.showOptionDialog(this, "Biztosan törli a kijelölt fájlokat a listából?", "Csatolmányok kezelése", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0) {
               for(int var1 = this.fileTable.getSelectedRows().length; var1 > 0; --var1) {
                  this.deleteCountAndLength((String)this.fileTableModel.getValueAt(this.fileTable.getSelectedRows()[var1 - 1], 2), new NecroFile((String)this.fileTableModel.getValueAt(this.fileTable.getSelectedRows()[var1 - 1], 0)));
                  this.fileTableModel.removeRow(this.fileTable.getSelectedRows()[var1 - 1]);
               }

               this.allAttachementSize = 0L;
            }
         } else {
            GuiUtil.showMessageDialog(this, "Nincs kijelölt fájl.", "Csatolmányok kezelése", 1);
         }
      } else if (JOptionPane.showOptionDialog(this, "Biztosan törli a fájlt a listából?", "Csatolmányok kezelése", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0) {
         this.deleteCountAndLength((String)this.fileTableModel.getValueAt(0, 2), new NecroFile((String)this.fileTableModel.getValueAt(0, 0)));
         this.fileTableModel.removeRow(0);
         this.allAttachementSize = 0L;
      }

      this.attachementCount.setText("" + this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].attached + " db");
      this.attachementCount.setText(this.attachementCount.getText() + (this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].minCount > 0 ? " - legalább " + this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].minCount + " db" : ""));
      this.attachementCount.setText(this.attachementCount.getText() + (this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].maxCount > 0 ? " - legfeljebb " + this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].maxCount + " db" : ""));
   }

   private void execute(File var1, String var2) {
      IOsHandler var3 = OsFactory.getOsHandler();

      try {
         var3.execute(var2, (String[])null, var1);
      } catch (Exception var5) {
         GuiUtil.showMessageDialog(this.getParent(), "Nem sikerült futtatni a megfelelő alkalmazást!", "Csatolmányok kezelése", 0);
      }

   }

   private void showNoteInputDialog() {
      if (this.fileTable.getRowCount() != 1) {
         if (this.fileTable.getSelectedRowCount() == 0) {
            GuiUtil.showMessageDialog(this, "Nincs kijelölt fájl.", "Csatolmányok kezelése", 1);
            return;
         }

         this.noteTextField.setText((String)this.fileTableModel.getValueAt(this.fileTable.getSelectedRows()[0], 1));
         this.noteInputDialog.setVisible(true);
      } else {
         this.noteTextField.setText((String)this.fileTableModel.getValueAt(0, 1));
         this.noteInputDialog.setVisible(true);
      }

   }

   private String loadAtcFile() {
      ArrayList var1 = this.preLoadAtcFile();
      String var2 = "";

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         String[] var4 = ((String)var1.get(var3)).split(";");

         try {
            File var5 = new NecroFile(var4[0]);
            if (var5.exists()) {
               try {
                  if (!this.AVDH || !var4[0].toLowerCase().endsWith(".anyk.ASiC".toLowerCase()) && !"Hitelesített csatolmány lenyomat".equalsIgnoreCase(var4[2])) {
                     int var6 = AttachementTool.isGoodAttachemetType(this.attachementInfoArray, var4[2], var4[0]);
                     if (var6 == 0) {
                        if (var5.length() == 0L) {
                           var2 = var2 + "A(z)" + var4[0] + " csatolmány 0 hosszú, ezért nem csatolható a nyomtatványhoz. A nyomtatvány így nem adható fel! \n";
                        }

                        if (AttachementTool.checkFileSize(var5, var4[2], this.attachementInfoArray, this.oneAttachementSize, this.allAttachementSize, this.attachedFileSize) == 0) {
                           this.attachedFileSize += var5.length();
                           this.fileTableModel.addRow(var4);
                           if (this.AVDH) {
                              this.handleSigners(var4[0], this.fileTableModel.getRowCount() - 1);
                           }

                           this.addCountDataToCSI(var4[2]);
                        } else {
                           var2 = var2 + var4[0] + " fájl méretével már túllépte a megadott értékhatárt, a nyomtatvány így nem adható fel!\n      ELLENŐRIZZE A CSATOLT FÁJLOK MÉRETÉT! \n";
                        }
                     } else {
                        if (var6 == 1) {
                           var2 = var2 + var4[2] + " csatolmánytípus (" + var4[0] + ") nem csatolható ehhez a nyomtatványhoz \n";
                        }

                        if (var6 == 2) {
                           if (var4[0].lastIndexOf(".") > -1) {
                              var2 = var2 + var4[0].substring(var4[0].lastIndexOf(".")) + " kiterjesztésű fájl nem csatolható ehhez a nyomtatványhoz \n";
                           } else {
                              var2 = var2 + "A(z)" + var4[0] + " fájlnak nincs kiterjesztése, nem csatolható ehhez a nyomtatványhoz \n";
                           }
                        }
                     }
                  }
               } catch (Exception var7) {
                  Tools.eLog(var7, 0);
               }
            } else {
               var2 = var2 + var4[0] + " fájl nem található \n";
            }
         } catch (Exception var8) {
            Tools.eLog(var8, 0);
         }
      }

      this.attachementCount.setText("" + this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].attached + " db");
      this.attachementCount.setText(this.attachementCount.getText() + (this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].minCount > 0 ? " - legalább " + this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].minCount + " db" : ""));
      this.attachementCount.setText(this.attachementCount.getText() + (this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].maxCount > 0 ? " - legfeljebb " + this.attachementInfoArray[this.attachementTypeCombo.getSelectedIndex()].maxCount + " db" : ""));
      return var2.equals("") ? null : var2;
   }

   private ArrayList<String> preLoadAtcFile() {
      ArrayList var1 = new ArrayList();
      String var2 = ".frm.enyk";
      if (fileNev.indexOf(var2) == -1) {
         var2 = ".xml";
      }

      String var3 = null;

      try {
         var3 = this.getRightATCFilename(var2);
      } catch (Exception var13) {
         var3 = null;
      }

      if (var3 == null) {
         return var1;
      } else {
         File var4 = new NecroFile(var3);
         BufferedReader var5 = null;
         boolean var6 = true;

         try {
            var5 = new BufferedReader(new InputStreamReader(new FileInputStream(var4), "utf-8"));
            boolean var8 = true;

            String var7;
            while((var7 = var5.readLine()) != null) {
               if (var8) {
                  if (!var7.startsWith("encoding=")) {
                     var6 = false;
                     var5.close();
                     break;
                  }

                  var8 = false;
               } else {
                  try {
                     var1.add(var7);
                  } catch (Exception var12) {
                     Tools.eLog(var12, 0);
                  }
               }
            }

            if (!var6) {
               var5 = new BufferedReader(new FileReader(var4));

               while((var7 = var5.readLine()) != null) {
                  try {
                     var1.add(var7);
                  } catch (Exception var11) {
                     Tools.eLog(var11, 0);
                  }
               }
            }

            var5.close();
         } catch (Exception var14) {
            try {
               var5.close();
            } catch (IOException var10) {
               Tools.eLog(var14, 0);
            }
         }

         Collections.sort(var1);
         Collections.reverse(var1);
         return var1;
      }
   }

   private void saveAtcFile() {
      if (fileNev.toLowerCase().indexOf(".frm.enyk") == -1) {
         GuiUtil.showMessageDialog(this, "Nem tudok nevet adni az atc fájlnak.", "Alkalmazáshiba", 0);
      } else {
         String var1 = fileNev.substring(0, fileNev.toLowerCase().indexOf(".frm.enyk")) + "_" + this.bookModel.get_formid() + ".atc";
         String var2 = fileNev.substring(0, fileNev.toLowerCase().indexOf(".frm.enyk")) + "_" + this.bookModel.get_formid() + ".cst";
         String var3 = AttachementTool.checkRequirement(this.attachementInfoArray);
         if (var3.length() <= 0 || JOptionPane.showOptionDialog(this, "Az alábbi hibák miatt a nyomtatványt így nem fogja tudni feladni!\nFolytassuk a műveletet?\n\n" + var3.toString(), "Csatolmányok kezelése", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) != 1) {
            if (this.fileTable.getRowCount() == 0) {
               boolean var13 = true;
               File var14 = new NecroFile(var1);
               if (var14.exists()) {
                  try {
                     if (!this.deleteAllAttachement()) {
                        GuiUtil.showMessageDialog(this, "Nem sikerült a(z) " + attachPath + this.getFileName() + " mappából az összes fájlt törölni, kérem törölje más módon!", "Csatolmányok kezelése", 0);
                        var13 = false;
                     }
                  } catch (Exception var10) {
                     Tools.eLog(var10, 0);
                  }

                  if (!var14.delete()) {
                     GuiUtil.showMessageDialog(this, "Nem sikerült a " + var1 + " fájl törlése, kérem törölje más módon!", "Csatolmányok kezelése", 0);
                     var13 = false;
                  }
               }

               var14 = new NecroFile(var2);
               if (var14.exists() && !var14.delete()) {
                  GuiUtil.showMessageDialog(this, "Nem sikerült a " + var2 + " fájl törlése, kérem törölje más módon!", "Csatolmányok kezelése", 0);
                  var13 = false;
               }

               if (var13) {
                  GuiUtil.showMessageDialog(this, "A csatolmányok eltávolítása megtörtént.", "Csatolmányok kezelése - üzenet", 1);
                  this.innerSaveAfterAttach();
               }

               this.closeThis();
            } else {
               Vector var4;
               if (this.bookModel.isAvdhModel()) {
                  this.cmdObject = null;
                  this.copyInBackground();
                  if (this.cmdObject == null) {
                     return;
                  }

                  if (this.cmdObject instanceof Vector) {
                     if (((Vector)this.cmdObject).size() == 0) {
                        GuiUtil.showMessageDialog(this, "Hiba a csatolmányok másolásakor.", "Csatolmányok kezelése", 0);
                        return;
                     }
                  } else {
                     GuiUtil.showMessageDialog(this, "Hiba a csatolmányok másolásakor." + this.cmdObject.toString(), "Csatolmányok kezelése", 0);
                  }

                  var4 = (Vector)this.cmdObject;
               } else {
                  this.copyMessage = "";
                  var4 = this.copyFiles();
                  if (var4 == null) {
                     return;
                  }

                  if (var4.size() == 0) {
                     GuiUtil.showMessageDialog(this, "Hiba a csatolmányok másolásakor.", "Csatolmányok kezelése", 0);
                     return;
                  }

                  if (!"".equals(this.copyMessage)) {
                     GuiUtil.showMessageDialog(this, "Néhány csatolmány neve a későbbi feldolgozáskor hibát okozna, ezért az alábbi neveken mentjük őket:\n" + this.copyMessage, "Csatolmányok kezelése", 0);
                  }
               }

               String var5 = AttachementTool.checkAttachmentDuplication(this.bookModel, attachPath, var4);
               if (!var5.equals("")) {
                  var5 = "Az alábbi csatolmányok több részbizonylathoz is csatolva vannak.\n" + var5;
                  if (JOptionPane.showOptionDialog(this, "Az alábbi hibák miatt a nyomtatványt így nem fogja tudni feladni!\nFolytassuk a műveletet?\n" + var5, "Csatolmányok kezelése", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 1) {
                     return;
                  }
               }

               FileOutputStream var6 = null;

               try {
                  var6 = new NecroFileOutputStream(var1);
                  var6.write("encoding=utf-8\n".getBytes("utf-8"));
                  int var7 = 0;

                  while(true) {
                     if (var7 >= var4.size()) {
                        var6.close();
                        break;
                     }

                     Object[] var8 = (Object[])((Object[])var4.elementAt(var7));
                     var6.write((var8[0] + ";" + var8[1] + ";" + var8[2] + "\n").getBytes("utf-8"));
                     ++var7;
                  }
               } catch (Exception var12) {
                  try {
                     var6.close();
                  } catch (Exception var9) {
                     Tools.eLog(var12, 0);
                  }

                  GuiUtil.showMessageDialog(this, "Hiba a csatolmány-lista mentésekor.", "Csatolmányok kezelése", 0);
                  return;
               }

               try {
                  this.szuksegtelenCsatolmanyokTorlese(var4);
               } catch (Exception var11) {
                  Tools.eLog(var11, 0);
               }

               this.simpleLoadAtcFile(new NecroFile(var1));
               this.innerSaveAfterAttach();
               GuiUtil.showMessageDialog(this, "A csatolást sikeresen befejeztük.", "Csatolmányok kezelése", 1);
               this.closeThis();
            }
         }
      }
   }

   private void innerSaveAfterAttach() {
      try {
         int var1 = this.getAllCount();
         if (var1 != 0) {
            this.bookModel.cc.docinfo.put("attachment_count", var1 + "");
         } else {
            this.bookModel.cc.docinfo.remove("attachment_count");
         }

         EnykInnerSaver var2 = new EnykInnerSaver(this.bookModel);
         var2.save(this.bookModel.cc.getLoadedfile().getAbsolutePath());
         var2 = null;
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
      }

   }

   private int getAllCount() {
      int var1 = 0;

      try {
         String var2 = this.bookModel.cc.getLoadedfile().getAbsolutePath();
         String var3 = var2.substring(0, var2.toLowerCase().indexOf(".frm.enyk"));

         for(int var4 = 0; var4 < this.bookModel.forms.size(); ++var4) {
            FormModel var5 = (FormModel)this.bookModel.forms.get(var4);
            String var6 = var3 + "_" + var5.name + ".atc";
            File var7 = new NecroFile(var6);
            int var8 = 0;

            try {
               var8 = AttachementTool.loadAndCountAtcFile(var7);
            } catch (Exception var10) {
            }

            var1 += var8;
         }

         return var1;
      } catch (Exception var11) {
         return 0;
      }
   }

   private Vector copyFiles() {
      Vector var1 = new Vector();

      for(int var3 = 0; var3 < this.fileTable.getRowCount(); ++var3) {
         try {
            String var2 = (String)this.fileTableModel.getValueAt(var3, 0);
            String var4 = this.doCopy(var2, var1, var3);
            if (!"".equals(var4)) {
               this.copyMessage = this.copyMessage + var4 + "\n";
            }
         } catch (Exception var5) {
            Tools.eLog(var5, 0);
         }
      }

      return var1;
   }

   private String doCopy(String var1, Vector var2, int var3) throws IOException {
      String var5 = "";
      String var6 = attachPath + this.getFileName();

      String var4;
      try {
         var4 = var1.substring(var1.lastIndexOf(File.separator) + 1);
      } catch (Exception var13) {
         var4 = var1;
      }

      var5 = this.checkFileNameISO88582(var4);
      if (!"".equals(var5)) {
         var4 = var5;
      }

      if (!this.isTheSameFile(var6 + var4, var1)) {
         File var7 = new NecroFile("");

         while(this.fileAlreadyAttached(var4) && !this.overwrite) {
            var4 = this.showRenameDialog(var4);
            if (var4 != null) {
               var7 = new NecroFile(var6 + var4);
            } else {
               var7 = null;
            }
         }

         this.overwrite = false;
         if (var7 == null) {
            var2 = null;
            return null;
         }

         FileInputStream var8 = new FileInputStream(var1);
         File var9 = new NecroFile(var6 + var4);
         var9.getParentFile().mkdirs();
         System.out.println("csatolmány másolása ide: " + var9);
         FileOutputStream var10 = new NecroFileOutputStream(var9);
         byte[] var11 = new byte[2048];

         int var12;
         while((var12 = var8.read(var11)) > -1) {
            var10.write(var11, 0, var12);
         }

         var10.close();
         var8.close();
      }

      this.doAddToVector(var2, var6 + var4, this.fileTableModel.getValueAt(var3, 1), this.fileTableModel.getValueAt(var3, 2));
      return var5;
   }

   private String checkFileNameISO88582(String var1) {
      String var2 = "";
      boolean var3 = false;

      try {
         String var4 = new String(var1.getBytes("iso8859-2"), "utf-8");

         for(int var5 = 0; var5 < var4.length(); ++var5) {
            try {
               if (disabledChars.contains(var4.charAt(var5))) {
                  var2 = var2 + "_";
                  var3 = true;
               } else {
                  var2 = var2 + var1.charAt(var5);
               }
            } catch (Exception var7) {
               var2 = var2 + "_";
            }
         }
      } catch (UnsupportedEncodingException var8) {
         var2 = "";
      }

      return var3 ? var2 : "";
   }

   private boolean isTheSameFile(String var1, String var2) {
      return (new NecroFile(var1)).getAbsolutePath().equalsIgnoreCase((new NecroFile(var2)).getAbsolutePath());
   }

   private boolean fileAlreadyAttached(String var1) {
      String[] var2 = AttachementTool.getAtcPaths(this.bookModel);

      for(int var3 = 0; var3 < var2.length; ++var3) {
         File var4 = new NecroFile(attachPath + var2[var3] + var1);
         if (var4.exists()) {
            return true;
         }
      }

      return false;
   }

   private void doAddToVector(Vector var1, String var2, Object var3, Object var4) {
      for(int var5 = 0; var5 < var1.size(); ++var5) {
         if (((Object[])((Object[])var1.elementAt(var5)))[0].toString().equalsIgnoreCase(var2)) {
            var1.setElementAt(new Object[]{var2, var3, var4}, var5);
            return;
         }
      }

      var1.add(new Object[]{var2, var3, var4});
   }

   private String showRenameDialog(String var1) {
      this.overwrite = false;
      this.renameTextField.setText(var1);
      this.renameDialog.setVisible(true);
      return this.renameTextField.getText().equals("") ? null : this.renameTextField.getText();
   }

   private void closeThis() {
      try {
         this.saveSettings(this);
      } catch (Exception var2) {
         System.out.println("A csatolmány ablak pozíciójának mentése nem sikerült: " + var2.toString());
      }

      this.dispose();
   }

   public void tableChanged(TableModelEvent var1) {
      this.setButtonState(this.fileTableModel.getRowCount() != 0);
   }

   public void itemStateChanged(ItemEvent var1) {
      if (var1.getSource() == this.attachementTypeCombo) {
         int var2 = this.attachementTypeCombo.getSelectedIndex();
         this.attachementCount.setText("" + this.attachementInfoArray[var2].attached + " db");
         this.attachementCount.setText(this.attachementCount.getText() + (this.attachementInfoArray[var2].minCount > 0 ? " - legalább " + this.attachementInfoArray[var2].minCount + " db" : ""));
         this.attachementCount.setText(this.attachementCount.getText() + (this.attachementInfoArray[var2].maxCount > 0 ? " - legfeljebb " + this.attachementInfoArray[var2].maxCount + " db" : ""));
         this.attachementRequired.setText("Dokumentum csatolása " + (this.attachementInfoArray[var2].required ? "" : "nem ") + "kötelező");
         this.attachementFileExtensions.setText(this.attachementInfoArray[var2].exts);
         this.attachementFileExtensions.setToolTipText(this.attachementInfoArray[var2].exts);
         if (this.attachementFileExtensions.getText().equals("*")) {
            this.attachementFileExtensions.setText("Tetszőleges fájl");
         }

         try {
            this.attachementTypeCombo.setToolTipText(var1.getItem().toString());
         } catch (Exception var4) {
            this.attachementTypeCombo.setToolTipText("");
         }
      }

   }

   private void setButtonState(boolean var1) {
      if (!this.readOnly) {
         this.delButton.setEnabled(var1);
         this.showButton.setEnabled(var1);
         this.pdfShowButton.setEnabled(var1);
         this.noteButton.setEnabled(var1);
      }
   }

   private Vector parseCsI(AttachementInfo[] var1) {
      Vector var2 = new Vector();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.add(var1[var3].description);
      }

      return var2;
   }

   private void deleteCountAndLength(String var1, File var2) {
      int var3 = -1;

      for(int var4 = 0; var4 < this.attachementTypeCombo.getItemCount(); ++var4) {
         if (this.attachementTypeCombo.getItemAt(var4).equals(var1)) {
            var3 = var4;
            break;
         }
      }

      if (var3 != -1) {
         --this.attachementInfoArray[var3].attached;
         this.attachedFileSize -= var2.length();
      }
   }

   private void addCountDataToCSI(String var1) {
      for(int var2 = 0; var2 < this.attachementInfoArray.length; ++var2) {
         if (this.attachementInfoArray[var2].description.equalsIgnoreCase(var1)) {
            ++this.attachementInfoArray[var2].attached;
         }
      }

   }

   private Result isSavable() {
      TemplateUtils var1 = TemplateUtils.getInstance();
      Result var2 = new Result();

      try {
         Object[] var3 = (Object[])((Object[])var1.isSavable(this.bookModel));
         var2.setOk((Boolean)var3[0]);
         if (!var2.isOk()) {
            var2.errorList.add(var3[1]);
         }

         return var2;
      } catch (Exception var4) {
         var2.setOk(false);
         var2.errorList.add(var4.toString());
         return var2;
      }
   }

   private void loadAttachementSettings() {
      SettingsStore var1 = SettingsStore.getInstance();
      this.setAttachment = var1.get("attachments");
   }

   private String getFileName() {
      String var1 = this.bookModel.cc.getLoadedfile().getName();
      return var1.substring(0, var1.toLowerCase().indexOf(".frm.enyk")) + File.separator + this.bookModel.get_formid() + File.separator;
   }

   private String getRightATCFilename(String var1) {
      String var2 = fileNev.substring(0, fileNev.toLowerCase().indexOf(var1)) + "_" + this.bookModel.get_formid() + ".atc";
      File var3 = new NecroFile(var2);
      if (var3.exists()) {
         return var2;
      } else {
         var2 = attachPath + "tmp" + File.separator + var3.getName();
         var3 = new NecroFile(var2);
         if (var3.exists()) {
            return var2;
         } else {
            var2 = fileNev.substring(0, fileNev.toLowerCase().indexOf(var1)) + ".atc";
            var3 = new NecroFile(var2);
            if (var3.exists()) {
               return var2;
            } else {
               var2 = attachPath + "tmp" + File.separator + var3.getName();
               var3 = new NecroFile(var2);
               if (var3.exists()) {
                  return var2;
               } else {
                  var2 = fileNev.substring(0, fileNev.toLowerCase().indexOf(var1)) + "_" + (this.bookModel.splitesaver.equals("true") ? this.bookModel.get_formid() : this.bookModel.get_main_formmodel().name) + ".xml_csatolmanyai" + File.separator + var3.getName();
                  var3 = new NecroFile(var2);
                  if (var3.exists()) {
                     return var2;
                  } else {
                     var2 = attachPath + "tmp" + File.separator + var3.getName().substring(0, var3.getName().toLowerCase().indexOf(".atc")) + "_" + this.bookModel.get(((Elem)this.bookModel.cc.getActiveObject()).getType()).name;
                     var3 = new NecroFile(var2);
                     var2 = var2 + ".xml_csatolmanyai" + File.separator + var3.getName() + ".atc";
                     var3 = new NecroFile(var2);
                     if (var3.exists()) {
                        return var2;
                     } else {
                        var2 = fileNev.substring(0, fileNev.toLowerCase().indexOf(var1));
                        var3 = new NecroFile(var2);
                        var2 = var2 + ".xml_csatolmanyai" + File.separator + var3.getName() + ".atc";
                        var3 = new NecroFile(var2);
                        if (var3.exists()) {
                           return var2;
                        } else {
                           var2 = attachPath + "tmp" + File.separator + var3.getName();
                           var3 = new NecroFile(var2);
                           var2 = var2 + ".xml_csatolmanyai" + File.separator + var3.getName();
                           var3 = new NecroFile(var2);
                           return var3.exists() ? var2 : null;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void szuksegtelenCsatolmanyokTorlese(Vector var1) {
      File[] var2 = this.getDirList(attachPath + this.getFileName());

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (!this.isInTheList(var2[var3].getAbsolutePath().toLowerCase(), var1)) {
            var2[var3].delete();
         }
      }

   }

   private File[] getDirList(String var1) {
      File var2 = new NecroFile(var1);
      return var2.exists() && var2.isDirectory() ? var2.listFiles() : null;
   }

   private boolean isInTheList(String var1, Vector var2) {
      for(int var3 = 0; var3 < var2.size(); ++var3) {
         String var4 = ((String)((Object[])((Object[])var2.elementAt(var3)))[0]).toLowerCase();
         if (var4.startsWith(var1)) {
            return true;
         }
      }

      return false;
   }

   private void simpleLoadAtcFile(File var1) {
      this.attachedFileSize = 0L;

      int var2;
      for(var2 = this.fileTableModel.getRowCount() - 1; var2 >= 0; --var2) {
         this.fileTableModel.removeRow(var2);
      }

      var2 = 0;
      BufferedReader var3 = null;
      boolean var4 = true;

      try {
         var3 = new BufferedReader(new InputStreamReader(new FileInputStream(var1), "utf-8"));
         boolean var6 = true;

         String var5;
         String[] var7;
         while((var5 = var3.readLine()) != null) {
            if (var6) {
               if (!var5.startsWith("encoding=")) {
                  var4 = false;
                  var3.close();
                  break;
               }

               var6 = false;
            } else {
               try {
                  var7 = var5.split(";");

                  try {
                     this.fileTableModel.addRow(var7);
                     ++var2;
                  } catch (Exception var11) {
                     Tools.eLog(var11, 0);
                  }
               } catch (Exception var12) {
                  Tools.eLog(var12, 0);
               }
            }
         }

         if (!var4) {
            var3 = new BufferedReader(new FileReader(var1));

            while((var5 = var3.readLine()) != null) {
               try {
                  var7 = var5.split(";");
                  this.fileTableModel.addRow(var7);
                  ++var2;
               } catch (Exception var10) {
                  Tools.eLog(var10, 0);
               }
            }
         }

         var3.close();
      } catch (Exception var13) {
         try {
            var3.close();
         } catch (IOException var9) {
            Tools.eLog(var13, 0);
         }
      }

      this.attachementCount.setText("" + var2 + " db");
   }

   private boolean deleteAllAttachement() {
      boolean var1 = true;
      String var2 = attachPath + this.getFileName();
      File[] var3 = this.getDirList(var2);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (!var3[var4].delete()) {
            var1 = false;
         }
      }

      File var7 = new NecroFile(var2);
      var7.delete();

      try {
         var7.getParentFile().delete();
      } catch (Exception var6) {
         Tools.eLog(var6, 0);
      }

      return var1;
   }

   private boolean handleAVDH(String var1) {
      boolean var2 = false;

      for(int var3 = 0; var3 < this.fileTableModel.getRowCount(); ++var3) {
         if (var1.startsWith(this.fileTableModel.getValueAt(var3, 0).toString())) {
            ((DefaultTableCellRenderer)this.fileTable.getCellRenderer(var3, 0)).setForeground(Color.BLUE);
            this.hasAVDHFile = true;
            var2 = true;
         }
      }

      return var2;
   }

   private void handleAvdhColorsInInnerfile() {
      for(int var1 = 0; var1 < this.fileTableModel.getRowCount(); ++var1) {
         File var2 = new NecroFile(this.fileTableModel.getValueAt(var1, 0).toString() + ".anyk.ASiC");
         if (var2.exists()) {
            ((DefaultTableCellRenderer)this.fileTable.getCellRenderer(var1, 0)).setForeground(Color.BLUE);
            this.hasAVDHFile = true;
         }
      }

   }

   private String checkAvdhExtensions() {
      StringBuffer var1 = new StringBuffer("");

      for(int var2 = 0; var2 < this.fileTableModel.getRowCount(); ++var2) {
         if (this.fileTableModel.getValueAt(var2, 0).toString().toLowerCase().endsWith(".anyk.ASiC".toLowerCase())) {
            var1.append(this.fileTableModel.getValueAt(var2, 0)).append(" \nfájl egy korábban már hitelesített csatolmány lenyomatot tartalmaz. A nyomtatvány így nem adható fel!\n");
         }
      }

      return var1.toString();
   }

   private void handleSigners(String var1, int var2) {
      AsicPdfHandler var3 = new AsicPdfHandler();
      String var4 = var1 + ".anyk.ASiC";
      if ((new NecroFile(var4)).exists()) {
         Result var5 = var3.getSigners(var4);
         String var6 = "-";
         if (var5.isOk()) {
            var6 = (String)var5.errorList.get(1);
            this.pdf4Signers.put(this.fileTableModel.getValueAt(var2, 0), var5.errorList.get(0));
         }

         this.fileTableModel.setValueAt(var6, var2, 3);
      }
   }

   private void copyInBackground() {
      final JDialog var1 = new JDialog(MainFrame.thisinstance, "Csatolmány fájlok másolása", true);
      var1.setDefaultCloseOperation(0);
      final boolean[] var2 = new boolean[]{false};
      final SwingWorker var3 = new SwingWorker() {
         public Object doInBackground() throws InterruptedException {
            try {
               return Csatolmanyok.this.copyFiles();
            } catch (Exception var2x) {
               return var2x.getMessage();
            }
         }

         public void done() {
            try {
               Csatolmanyok.this.cmdObject = this.get();
            } catch (Exception var3) {
               var3.printStackTrace();
               Csatolmanyok.this.cmdObject = null;
            }

            var2[0] = true;

            try {
               var1.setVisible(false);
            } catch (Exception var2x) {
               Tools.eLog(var2x, 0);
            }

         }
      };
      var1.addWindowListener(new WindowAdapter() {
         public void windowOpened(WindowEvent var1) {
            var3.execute();
         }
      });
      int var4 = MainFrame.thisinstance.getX() + MainFrame.thisinstance.getWidth() / 2 - 250;
      if (var4 < 0) {
         var4 = 0;
      }

      int var5 = MainFrame.thisinstance.getY() + MainFrame.thisinstance.getHeight() / 2 - 200;
      if (var5 < 0) {
         var5 = 0;
      }

      var1.setBounds(var4, var5, 600, 100);
      var1.setSize(600, 100);
      JPanel var6 = new JPanel((LayoutManager)null, true);
      var6.setLayout(new BoxLayout(var6, 1));
      BevelBorder var7 = new BevelBorder(0);
      var6.setBorder(var7);
      Dimension var8 = new Dimension(600, 70);
      var6.setPreferredSize(var8);
      JLabel var9 = new JLabel("Kérjük várjon, a csatolmányok másolása folyamatban...");
      var9.setPreferredSize(var8);
      var9.setAlignmentX(0.5F);
      var6.add(var9);
      var6.setVisible(true);
      var1.getContentPane().add(var6);
      var2[0] = false;
      var1.pack();
      var1.setVisible(true);
      if (var2[0]) {
         var1.setVisible(false);
      }

   }

   private int getMaxCsatolmanyTipusWidth(Vector var1) {
      String var2 = "WWWWWWWWWW";

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         if (var1.get(var3).toString().length() > var2.length()) {
            var2 = var1.get(var3).toString();
         }
      }

      return Math.min(GuiUtil.getW(var2 + "WWW"), GuiUtil.getW("legfeljebb 9999999999 kB, összesített méret: legfeljebb 9999999999 kb"));
   }

   private void saveSettings(JDialog var1) {
      SettingsStore var2 = SettingsStore.getInstance();
      var2.set("atc_settings", "width", var1.getWidth() + "");
      var2.set("atc_settings", "height", var1.getHeight() + "");
      var2.set("atc_settings", "xPos", var1.getLocation().x + "");
      var2.set("atc_settings", "yPos", var1.getLocation().y + "");
   }

   private void loadSettings(JDialog var1) {
      SettingsStore var6 = SettingsStore.getInstance();

      int var2;
      try {
         var2 = Integer.parseInt(var6.get("atc_settings", "width"));
      } catch (Exception var11) {
         return;
      }

      int var3;
      try {
         var3 = Integer.parseInt(var6.get("atc_settings", "height"));
      } catch (Exception var10) {
         return;
      }

      int var4;
      try {
         var4 = Integer.parseInt(var6.get("atc_settings", "xPos"));
      } catch (Exception var9) {
         return;
      }

      int var5;
      try {
         var5 = Integer.parseInt(var6.get("atc_settings", "yPos"));
      } catch (Exception var8) {
         return;
      }

      var1.setLocation(var4, var5);
      var1.setSize(new Dimension(var2, var3));
      var1.setPreferredSize(var1.getSize());
   }

   static {
      disabledChars.add('\\');
      disabledChars.add('&');
      disabledChars.add('/');
      disabledChars.add('?');
      disabledChars.add('|');
      disabledChars.add('<');
      disabledChars.add('>');
      disabledChars.add(':');
      disabledChars.add('*');
      disabledChars.add('"');
      disabledChars.add('+');
      disabledChars.add(',');
      disabledChars.add(';');
      disabledChars.add('=');
      disabledChars.add('[');
      disabledChars.add(']');
   }

   private class ClickListener implements MouseListener, MouseMotionListener {
      private Object v;

      private ClickListener() {
      }

      public void mouseClicked(MouseEvent var1) {
         if (var1.getClickCount() == 2 && !Csatolmanyok.this.readOnly) {
            if (Csatolmanyok.this.fileTable.getSelectedColumn() == 3) {
               String var2 = (String)Csatolmanyok.this.pdf4Signers.get(Csatolmanyok.this.fileTable.getValueAt(Csatolmanyok.this.fileTable.getSelectedRow(), 0));
               Csatolmanyok.this.execute((new NecroFile(var2)).getParentFile(), var2);
            } else {
               Csatolmanyok.this.showNoteInputDialog();
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
         this.v = null;
      }

      public void mouseMoved(MouseEvent var1) {
         try {
            Csatolmanyok.this.fileTable.setToolTipText((String)Csatolmanyok.this.fileTable.getValueAt(Csatolmanyok.this.fileTable.rowAtPoint(var1.getPoint()), Csatolmanyok.this.fileTable.columnAtPoint(var1.getPoint())));
         } catch (Exception var3) {
            Csatolmanyok.this.fileTable.setToolTipText((String)null);
            Tools.eLog(var3, 0);
         }

      }

      // $FF: synthetic method
      ClickListener(Object var2) {
         this();
      }
   }

   private class EnterKeyListener implements KeyListener {
      private EnterKeyListener() {
      }

      public void keyPressed(KeyEvent var1) {
      }

      public void keyReleased(KeyEvent var1) {
         Csatolmanyok.this.handleEnter(var1);
      }

      public void keyTyped(KeyEvent var1) {
      }

      // $FF: synthetic method
      EnterKeyListener(Object var2) {
         this();
      }
   }
}
