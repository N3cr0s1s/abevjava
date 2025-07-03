package hu.piller.enykp.alogic.filepanels.mohu;

import hu.piller.enykp.alogic.ebev.Ebev;
import hu.piller.enykp.alogic.ebev.SendParams;
import hu.piller.enykp.alogic.fileloader.kr.KrHeadParser;
import hu.piller.enykp.alogic.filepanels.ABEVOpenPanel;
import hu.piller.enykp.alogic.filepanels.attachement.EJFileChooser;
import hu.piller.enykp.alogic.kontroll.ReadOnlyTableModel;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.orghandler.OrgResource;
import hu.piller.enykp.alogic.uploader.AuthenticationException;
import hu.piller.enykp.alogic.uploader.FeltoltesValasz;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.IHelperLoad;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.niszws.util.GateType;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.TableSorter;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.EJList;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/** @deprecated */
@Deprecated
public class EperMainMohuPanel extends JDialog implements ActionListener, TableModelListener {
   private static final String ADD_BUTTON_NAME = "1";
   private static final String ADD_KR_BUTTON_NAME = "2";
   private static final String DEL_BUTTON_NAME = "3";
   private static final String IMP_BUTTON_NAME = "5";
   private static final String CANCEL_BUTTON_NAME = "6";
   private static final String LOG_OK_BUTTON_NAME = "7";
   private static final String LOG_SAVE_BUTTON_NAME = "8";
   private static final int ADD_BUTTON_VALUE = 1;
   private static final int ADD_KR_BUTTON_VALUE = 2;
   private static final int DEL_BUTTON_VALUE = 3;
   private static final int START_BUTTON_VALUE = 5;
   private static final int CANCEL_BUTTON_VALUE = 6;
   private static final int LOG_OK_BUTTON_VALUE = 7;
   private static final int LOG_SAVE_BUTTON_VALUE = 8;
   public static int MAIN_WIDTH = 790;
   public static final int MAIN_HEIGHT = 520;
   public static final int SUMMA_WIDTH = 400;
   public static final int SUMMA_HEIGHT = 400;
   public static final int AUTH_WIDTH = 500;
   public static final int AUTH_HEIGHT = 310;
   public static final int OPEN_WIDTH = 600;
   public static final int OPEN_HEIGHT = 400;
   public static final String[] DI_KEYS = new String[]{"id", "tax_number", "person_name", "from_date", "to_date", "state", "info", "account_name", "save_date", "note", "ver", "templatever", "org"};
   static IHelperLoad ihl;
   private String[] resFilter;
   private JDialog summaDialog;
   private LoginDialog authDialog;
   private ABEVOpenPanel openPanel;
   private EJList logLista;
   private JScrollPane logSP;
   private JFrame mainFrame;
   private ReadOnlyTableModel fileTableModel;
   private TableSorter sorter;
   private JTable fileTable;
   private EJFileChooser fc;
   private EJFileChooser fc4Save;
   private EperMainMohuPanel.TxtFileFileter tff;
   private EperMainMohuPanel.KrFileFileter krff;
   private JProgressBar status;
   private JLabel jl = new JLabel("     0 db fájl a listában");
   private JButton delButton;
   private JButton okButton;
   private JButton addButton;
   private JButton addKrButton;
   private JButton megsemButton;
   private JComboBox filters;
   int actualSelectedIndex = 0;
   private Vector postHibaLista;
   private static boolean elindult = false;
   private static boolean folyamatban = false;
   private static boolean outOfMemory = false;
   private static File defaultDirectory = null;
   boolean tovabb = false;
   boolean alertTovabb = false;
   private Object cmdObject;
   static IPropertyList iplMaster = PropertyList.getInstance();
   private SendParams sp;
   Hashtable rowMatch;
   private static final Runtime s_runtime = Runtime.getRuntime();

   public EperMainMohuPanel() throws HeadlessException {
      super(MainFrame.thisinstance, "Nyomtatványok csoportos közvetlen beküldése a Perkapun keresztül", true);
      this.sp = new SendParams(iplMaster);
      this.rowMatch = new Hashtable();
      this.setDefaultCloseOperation(2);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent var1) {
            Tools.resetLabels();
         }
      });
      this.mainFrame = MainFrame.thisinstance;
      double var1 = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
      if (var1 < (double)MAIN_WIDTH) {
         MAIN_WIDTH = (int)var1;
      }

      this.news();

      try {
         this.init();
      } catch (NullPointerException var13) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "Alkalmazáshiba", "Hiba", 0);
         return;
      } catch (Exception var14) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiányzó paraméter : " + var14.getMessage().substring(1), "Hiba", 0);
         return;
      }

      this.initFcs();
      this.initLogDialog();
      this.summaDialog.setSize(400, 400);
      this.summaDialog.setLocationRelativeTo(MainFrame.thisinstance);
      this.authDialog.setSize(500, 360);
      this.authDialog.setLocationRelativeTo(MainFrame.thisinstance);
      this.openPanel = new ABEVOpenPanel();
      this.openPanel.setSize(600, 400);
      this.openPanel.setLocationRelativeTo(MainFrame.thisinstance);
      this.tableSettings();
      this.setButtonState(false);
      int var3 = this.mainFrame.getX() + this.mainFrame.getWidth() / 2 - MAIN_WIDTH / 2;
      if (var3 < 0) {
         var3 = 0;
      }

      int var4 = this.mainFrame.getY() + this.mainFrame.getHeight() / 2 - 260;
      if (var4 < 0) {
         var4 = 0;
      }

      this.setBounds(var3, var4, MAIN_WIDTH, 520);
      JPanel var5 = new JPanel(new BorderLayout());
      JPanel var6 = new JPanel(new BorderLayout(20, 0));
      JPanel var7 = new JPanel();
      JPanel var8 = new JPanel(new GridLayout(2, 1));
      JPanel var9 = new JPanel();
      JPanel var10 = new JPanel(new FlowLayout(0));
      this.addButton.setName("1");
      this.addKrButton.setName("2");
      this.addButton.addActionListener(this);
      this.addKrButton.addActionListener(this);
      this.addButton.setToolTipText("Nyomtatvány hozzáadása a listához");
      this.addKrButton.setToolTipText("kr fájl hozzáadása a listához a(z) " + this.sp.destPath + " mappából");
      this.delButton.setName("3");
      this.delButton.addActionListener(this);
      this.delButton.setToolTipText("A kijelölt nyomtatványok törlése a listából");
      var7.add(this.addButton);
      var7.add(this.addKrButton);
      var7.add(this.delButton);
      JScrollPane var11 = new JScrollPane(this.fileTable, 20, 30);
      Dimension var12 = new Dimension(MAIN_WIDTH, 350);
      var6.setPreferredSize(var12);
      var6.setMinimumSize(var12);
      var10.setBounds(5, 5, 590, 30);
      var11.setBounds(10, 40, 500, 250);
      var6.add(var11, "Center");
      var6.add(var7, "South");
      this.status.setString(" ");
      this.status.setIndeterminate(false);
      this.status.setStringPainted(true);
      this.status.setBorderPainted(false);
      var8.add(this.status);
      var5.add(var6, "Center");
      var5.add(var8, "South");
      this.getContentPane().add(var5, "Center");
      this.okButton.setName("5");
      this.okButton.addActionListener(this);
      this.megsemButton.setName("6");
      this.megsemButton.addActionListener(this);
      var9.add(this.okButton);
      var9.add(this.megsemButton);
      var9.setPreferredSize(new Dimension(MAIN_WIDTH, 40));
      this.jl.setPreferredSize(new Dimension(300, 30));
      this.openPanel.setFilters(new String[]{this.resFilter[this.filters.getSelectedIndex()]});
      this.rowMatch.put(6, "Küldésre megjelölt");
      this.openPanel.setSelectedPath((new File(this.sp.dataPath)).toURI());
      this.openPanel.setMode("open_import", this.rowMatch);
      this.openPanel.setTitle("Nyomtatvány hozzáadása " + this.beauty(this.sp.dataPath));
      this.getContentPane().add(var9, "South");
      this.pack();
      this.setVisible(true);
   }

   private void init() throws Exception {
      iplMaster = PropertyList.getInstance();
      this.filters.addItem("Nyomtatványok");
      this.resFilter = new String[]{"inner_data_loader_v1"};
   }

   private void initLogDialog() {
      JPanel var1 = new JPanel();
      JButton var2 = new JButton("Rendben");
      JButton var3 = new JButton("Lista mentése");
      var2.setName("7");
      var3.setName("8");
      var2.addActionListener(this);
      var3.addActionListener(this);
      var1.add(var2);
      var1.add(var3);
      this.summaDialog.getContentPane().add(var1, "South");
   }

   private void initDialog() {
      if (defaultDirectory != null) {
         this.fc4Save.setCurrentDirectory(defaultDirectory);
      }

      try {
         ((BasicFileChooserUI)this.fc.getUI()).setFileName("");
      } catch (ClassCastException var6) {
         try {
            this.fc.setSelectedFile(new File(""));
         } catch (Exception var5) {
            Tools.eLog(var5, 0);
         }
      }

      this.fc.setSelectedFile((File)null);

      try {
         ((BasicFileChooserUI)this.fc4Save.getUI()).setFileName("csoportos_muveletek_uzenetek.txt");
      } catch (ClassCastException var4) {
         try {
            this.fc4Save.setSelectedFile(new File("csoportos_muveletek_uzenetek.txt"));
         } catch (Exception var3) {
            Tools.eLog(var3, 0);
         }
      }

      this.fc4Save.setSelectedFile((File)null);
   }

   private void doAdd() {
      Hashtable var1 = this.openPanel.showDialog();
      if (var1 != null) {
         Object[] var2 = (Object[])((Object[])var1.get("selected_files"));
         this.addFilesToList(var2);
         var1 = null;
      }
   }

   private void doAddKr() {
      this.initDialog();
      int var1 = this.fc.showOpenDialog(this);
      if (var1 == 0) {
         File[] var2 = this.fc.getSelectedFiles();
         this.addKrFileToList(var2);
      }

   }

   private void doDel() {
      if (this.fileTable.getSelectedRows().length > 0 && JOptionPane.showOptionDialog(this, "Biztosan törli a kijelölt fájlokat a listából?", "Kérdés", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0) {
         int[] var1 = this.getOriginalIndexes(this.fileTable.getSelectedRows());

         for(int var2 = var1.length; var2 > 0; --var2) {
            this.fileTableModel.removeRow(var1[var2 - 1]);
         }
      }

   }

   private int[] getOriginalIndexes(int[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         Object var3 = this.fileTable.getModel().getValueAt(var1[var2], 0);
         int var4 = 0;

         for(boolean var5 = false; var4 < this.fileTableModel.getRowCount() && !var5; ++var4) {
            if (this.fileTableModel.getValueAt(var4, 0).equals(var3)) {
               var5 = true;
               var1[var2] = var4;
            }
         }
      }

      Arrays.sort(var1);
      return var1;
   }

   private String startPost() {
      try {
         if (this.fileTableModel.getRowCount() == 0) {
            GuiUtil.showMessageDialog(this.mainFrame, "Nincs elem a listában!", "Üzenet", 1);
            return null;
         }

         this.tovabb = this.authDialog.showIfNeed();
         if (this.tovabb && this.authDialog.getState() != 3) {
            return null;
         }

         this.postHibaLista.clear();
         this.enableAll(false);
         final JDialog var1 = new JDialog(this.mainFrame, "Küldése", true);
         var1.setDefaultCloseOperation(0);
         final boolean[] var2 = new boolean[]{false};
         SwingWorker var3 = new SwingWorker() {
            int postedFileCount = 0;
            int fileCount;

            {
               this.fileCount = EperMainMohuPanel.this.fileTableModel.getRowCount();
            }

            public Object doInBackground() {
               Result var1x = null;

               try {
                  EperMainMohuPanel.folyamatban = true;
                  EperMainMohuPanel.this.filters.setEnabled(false);
                  if (EperMainMohuPanel.this.fileTableModel.getRowCount() != 0) {
                     EperMainMohuPanel.this.postHibaLista.add(new TextWithIcon("Start : " + EperMainMohuPanel.this.getTimeString("yyyy.MM.dd HH.mm.ss"), -1));
                     EperMainMohuPanel.this.postHibaLista.add(new TextWithIcon("-------------------------------------------", -1));
                  }

                  var1x = new Result();

                  while(EperMainMohuPanel.this.fileTableModel.getRowCount() != 0 && EperMainMohuPanel.folyamatban) {
                     EperMainMohuPanel.this.postHibaLista.add(new TextWithIcon(" ", -1));
                     EperMainMohuPanel.this.postHibaLista.add(new TextWithIcon("[" + EperMainMohuPanel.this.fileTableModel.getValueAt(0, 0) + "]", -1));
                     EperMainMohuPanel.this.status.setIndeterminate(true);
                     EperMainMohuPanel.this.status.setString("     " + EperMainMohuPanel.this.fileTableModel.getValueAt(0, 0) + " fájl küldése folyamatban...");

                     FeltoltesValasz[] var2x;
                     try {
                        var2x = EperMainMohuPanel.this.doPost((String)EperMainMohuPanel.this.fileTableModel.getValueAt(0, 0));
                        if (var2x == null) {
                           EperMainMohuPanel.this.postHibaLista.add(new TextWithIcon(" - Nem sikerült a kapcsolódás a fogadó szerverhez", 0));
                           var1x.setOk(false);
                           var1x.errorList.add("Nem sikerült a kapcsolódás a fogadó szerverhez");
                           break;
                        }
                     } catch (Exception var4) {
                        var4.printStackTrace();
                        EperMainMohuPanel.this.postHibaLista.add(new TextWithIcon(" - Hiba történt a fájl küldésekor", 0));
                        Tools.eLog(var4, 0);
                        var1x.setOk(false);
                        var1x.errorList.add("Hiba történt a fájl küldésekor");
                        continue;
                     }

                     if (var2x[0].isStored()) {
                        ++this.postedFileCount;
                     }

                     EperMainMohuPanel.this.fileTableModel.removeRow(0);
                     EperMainMohuPanel.this.status.setString(" ");
                  }

                  EperMainMohuPanel.this.authDialog.delPass();
               } catch (Exception var5) {
                  var1.setVisible(false);
                  var5.printStackTrace();
               }

               EperMainMohuPanel.this.status.setIndeterminate(false);
               EperMainMohuPanel.this.filters.setEnabled(true);
               var1x.errorList = EperMainMohuPanel.this.postHibaLista;
               return var1x;
            }

            public void done() {
               var2[0] = true;

               try {
                  EperMainMohuPanel.this.cmdObject = this.get();
               } catch (Exception var3) {
                  var3.printStackTrace();
                  EperMainMohuPanel.this.cmdObject = null;
               }

               try {
                  var1.setVisible(false);
               } catch (Exception var2x) {
                  Tools.eLog(var2x, 0);
               }

               EperMainMohuPanel.folyamatban = false;
               EperMainMohuPanel.elindult = false;
               EperMainMohuPanel.this.status.setString(" ");
               EperMainMohuPanel.this.postHibaLista.add(new TextWithIcon(" ", -1));
               EperMainMohuPanel.this.postHibaLista.add(new TextWithIcon(" ", -1));
               EperMainMohuPanel.this.postHibaLista.add(new TextWithIcon("A küldésre kijelölt fájlok száma: " + this.fileCount, -1));
               if (this.postedFileCount != 0) {
                  EperMainMohuPanel.this.postHibaLista.add(new TextWithIcon(this.postedFileCount + " fájlt sikeresen beküldtünk.", -1));
               }

               if (this.fileCount - this.postedFileCount != 0) {
                  EperMainMohuPanel.this.postHibaLista.add(new TextWithIcon(this.fileCount - this.postedFileCount + " fájl küldése nem sikerült.", 0));
               }

               if (EperMainMohuPanel.outOfMemory) {
                  EperMainMohuPanel.this.postHibaLista.add(new TextWithIcon("A küldés elvégzéséhez kevés a memória! Kérjük indítsa újra az alkalmazást!", 0));
               }

               EperMainMohuPanel.this.enableAll(true);
               MainFrame.thisinstance.glasslock = false;
               MainFrame.thisinstance.setGlassLabel((String)null);
               Tools.resetLabels();
               EperMainMohuPanel.this.fillDialog("A küldés eredménye:", EperMainMohuPanel.this.postHibaLista);
            }
         };
         elindult = true;
         var2[0] = false;
         var3.execute();

         try {
            if (var2[0]) {
               var1.setVisible(false);
            }
         } catch (Exception var6) {
            Tools.eLog(var6, 0);
         }
      } catch (Exception var7) {
         this.status.setString(" ");
         this.enableAll(true);
      }

      try {
         return ((File)((Result)this.cmdObject).errorList.elementAt(0)).getAbsolutePath();
      } catch (Exception var5) {
         return null;
      }
   }

   private void setButtonState(boolean var1) {
      if (!folyamatban) {
         this.delButton.setEnabled(var1);
         this.okButton.setEnabled(var1);
      }
   }

   private String getTimeString(String var1) {
      SimpleDateFormat var2 = new SimpleDateFormat(var1);
      return var2.format(Calendar.getInstance().getTime());
   }

   private void fillDialog(String var1, Vector var2) {
      this.summaDialog.setTitle(var1);
      this.logLista.removeAll();
      this.logLista.setListData(var2);
      this.logSP.setViewportView(this.logLista);
      Dimension var3 = new Dimension(650, 500);
      this.logSP.setPreferredSize(var3);
      this.logSP.setMinimumSize(var3);
      this.summaDialog.getContentPane().add(this.logSP, "Center");
      this.summaDialog.pack();
      this.summaDialog.setVisible(true);
   }

   private FeltoltesValasz[] doPost(String var1) throws Exception {
      FeltoltesValasz[] var2 = null;
      boolean var3 = true;

      try {
         MohuTools var4 = new MohuTools(this.sp);

         try {
            var2 = var4.callWS(new String[]{var1}, false, (String)null);
         } catch (AuthenticationException var9) {
            var2 = null;
            this.postHibaLista.add(new TextWithIcon("Nem sikerült kapcsolódni a fogadó szerverhez!", 1));
            if (var9.getType() == 0) {
               this.postHibaLista.add(new TextWithIcon(" - Kérjük ellenőrizze a kapcsolatot és, hogy helyesen adta-e meg Ügyfélkapus felhasználónevét és jelszavát!", -1));
            } else {
               this.postHibaLista.add(new TextWithIcon(" - Kérjük ellenőrizze a kapcsolatot és, hogy helyesen adta-e meg Perkapus felhasználónevét és jelszavát!", -1));
            }

            this.postHibaLista.add(new TextWithIcon(" - Részletes leírást a Szerviz/Üzenetek menüpontban talál.", -1));
         } catch (Exception var10) {
            var2 = null;
            this.postHibaLista.add(new TextWithIcon("A nyomtatvány beküldése sikertelen!", 1));
            this.postHibaLista.add(new TextWithIcon(" - Részletes leírást a Szerviz/Üzenetek menüpontban talál.", -1));
         } catch (Error var11) {
            System.out.println("ERROR!!!");
            var11.printStackTrace();
            var2 = null;
            this.postHibaLista.add(new TextWithIcon("A nyomtatvány beküldése sikertelen!", 1));
            this.postHibaLista.add(new TextWithIcon(" - Részletes leírást a Szerviz/Üzenetek menüpontban talál.", -1));
         }

         if (var2 != null && var2.length != 0) {
            if (var2[0].isStored()) {
               var3 = var4.moveFile(this.sp.destPath + var2[0].getFileName());
            }

            if (var2[0].isStored()) {
               this.postHibaLista.add(new TextWithIcon(var1 + "  - sikeresen beküldve.", -1));
               if (!var3) {
                  this.postHibaLista.add(new TextWithIcon(var1 + "  - átmozgatása az elküldött mappába nem sikerült. Kérem mozgassa át a küldendő mappából az elküldöttbe!", 4));
               }

               if (var2[0].getFilingNumber() == null) {
                  this.postHibaLista.add(new TextWithIcon("A nyomtatvány a érkeztetési számát az értesítési tárhelyére belépve tudja megnézni. A továbbiakban azon a számon hivatkozhat rá.", 3));
               } else {
                  this.postHibaLista.add(new TextWithIcon("A nyomtatvány a " + var2[0].getFilingNumber() + " érkeztetési számot kapta. A továbbiakban ezen a számon hivatkozhat rá.\n", 3));

                  try {
                     Ebev.log(4, new File(this.sp.sentPath + var2[0].getFileName()), "Érkeztetési szám: " + var2[0].getFilingNumber());
                  } catch (Exception var8) {
                     try {
                        System.out.println("Figyelmeztetés! Nem sikerült a feladás naplózása. A nyomtatvány a " + var2[0].getFilingNumber() + " érkeztetési számot kapta");
                     } catch (Exception var7) {
                        Tools.eLog(var8, 0);
                     }
                  }
               }
            } else {
               this.postHibaLista.add(new TextWithIcon(var1 + "  - küldése nem sikerült.", 0));
               if (var2[0].getErrorMsg() != null) {
                  this.postHibaLista.add(new TextWithIcon("A sikertelenség oka a fogadó oldal szerint: " + var2[0].getErrorMsg() + "\n", 0));
               }
            }
         }
      } catch (Exception var12) {
         this.postHibaLista.add(new TextWithIcon(var1 + "  - Hiba történt a küldéskor.", 0));
         ErrorList.getInstance().writeError(4001L, "Csoportos Mohu küldés hiba", var12, (Object)null);
      }

      return var2;
   }

   private void enableAll(boolean var1) {
      this.addButton.setEnabled(var1);
      this.addKrButton.setEnabled(var1);
      this.okButton.setEnabled(var1 & this.fileTableModel.getRowCount() > 0);
      this.delButton.setEnabled(this.okButton.isEnabled());
      this.megsemButton.setEnabled(true);
      this.filters.setEnabled(true);
      this.setDefaultCloseOperation(var1 ? 2 : 0);
   }

   private String beauty(Object var1) {
      String var2 = var1.toString();
      if (var2 != null) {
         if (File.separator.equals("\\")) {
            var2 = var2.replaceAll("/", "\\\\");
         } else {
            var2 = var2.replaceAll("\\\\", "/");
         }
      }

      return var2;
   }

   private boolean alreadyInList(Vector var1) {
      boolean var2 = false;
      String var3 = this.parseVector(var1);
      int var4 = 0;

      for(Vector var5 = this.fileTableModel.getDataVector(); var4 < var5.size() && !var2; ++var4) {
         var2 = var3.toLowerCase().indexOf(((Vector)var5.get(var4)).get(0).toString().toLowerCase()) > -1;
      }

      return var2;
   }

   private String parseVector(Vector var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         if (var1.elementAt(var3) != null) {
            var2.append(var1.elementAt(var3)).append(";");
         }
      }

      return var2.toString();
   }

   private void tableSettings() {
      this.fileTable.setSelectionMode(2);
      this.fileTable.setRowSelectionAllowed(true);
      this.fileTableModel.addTableModelListener(this);
      this.fileTable.setAutoResizeMode(0);
      this.fileTable.addMouseMotionListener(new MouseMotionListener() {
         public void mouseDragged(MouseEvent var1) {
         }

         public void mouseMoved(MouseEvent var1) {
            try {
               EperMainMohuPanel.this.fileTable.setToolTipText((String)EperMainMohuPanel.this.fileTableModel.getValueAt(EperMainMohuPanel.this.fileTable.rowAtPoint(var1.getPoint()), EperMainMohuPanel.this.fileTable.columnAtPoint(var1.getPoint())));
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

         }
      });

      for(int var2 = 0; var2 < 5; ++var2) {
         TableColumn var1 = this.fileTable.getColumnModel().getColumn(var2);
         if (var2 == 0) {
            var1.setPreferredWidth(300);
         } else {
            var1.setPreferredWidth(100);
         }
      }

      this.sorter.setTableHeader(this.fileTable.getTableHeader());
   }

   private void addFilesToList(Object[] var1) {
      Vector var2 = new Vector();
      Vector var3 = new Vector();
      boolean var4 = PropertyList.getInstance().get("prop.dynamic.gateTypeEPER") != null;
      String var9 = "500 Mbyte";
      long var10 = 524288000L;

      for(int var12 = 0; var12 < var1.length; ++var12) {
         Object[] var13 = (Object[])((Object[])var1[var12]);
         Vector var14 = new Vector();
         String var16;
         File var17;
         if (var13.length > 3 && var13[3] != null && var13[3] instanceof Map) {
            Object var15 = ((Map)var13[3]).get("docinfo");
            if (var15 != null && var15 instanceof Map) {
               var16 = (String)((Map)var15).get("krfilename");
               if (var16 != null && var16.trim().length() > 0) {
                  var17 = new File(this.sp.destPath + var16);
                  if (var17.exists()) {
                     var14.add(this.sp.destPath + var16);
                  }
               }
            }
         }

         if (var14.size() == 0) {
            var14 = this.getMarkedFiles4EnykFile((File)var13[0]);
         }

         if (var4) {
            try {
               Hashtable var7 = (Hashtable)var13[2];
               String var5 = (String)var7.get("org");

               try {
                  if (OrgInfo.getInstance().hasSuccessor(var5)) {
                     var5 = OrgInfo.getInstance().getSuccessorOrgId(var5);
                  }
               } catch (Exception var22) {
                  var3.add(((File)var13[0]).getAbsolutePath());
                  var3.add(" - " + var22.getMessage() + "!");
                  continue;
               }

               OrgResource var6 = (OrgResource)((Hashtable)OrgInfo.getInstance().getOrgList()).get(var5);
               String var8 = var6.getPreferredUplodaMaxSize();
               if ("".equals(var8) || "0".equals(var8)) {
                  var8 = "100000";
               }

               var9 = var8 + " Mbyte";

               try {
                  var10 = Long.parseLong(var8) * 1024L * 1024L;
               } catch (NumberFormatException var21) {
                  var10 = 524288000L;
               }

               System.out.println("LOG: perkapu max upload size: " + var8 + " - " + var10 + " - " + var9);
            } catch (Exception var23) {
               ErrorList.getInstance().writeError(new Integer(23112), "Hiba a mérethatár megállapításakor, nem alkalmazunk korlátot.", var23, (Object)null);
               continue;
            }
         }

         for(int var24 = 0; var24 < var14.size(); ++var24) {
            var16 = (String)var14.elementAt(var24);
            var17 = new File(var16);
            if (var17.length() > var10) {
               var3.add(((File)var13[0]).getAbsolutePath());
               var3.add(" - A dokumentum mérete meghaladja az " + var9 + "-ot. Ekkora állományt a Perkapu nem tud fogadni!");
            } else {
               Vector var18 = new Vector();
               var18.add(var16);
               Hashtable var19 = (Hashtable)((Hashtable)var13[3]).get("docinfo");
               if (var19.size() == 0) {
                  var3.add(((File)var13[0]).getAbsolutePath() + " - Nem olvashatóak a nyomtatványadatok.");
                  var18 = null;
               } else if (var19.get("name").equals("HIBÁS ÁLLOMÁNY !")) {
                  var3.add(((File)var13[0]).getAbsolutePath() + " - " + "HIBÁS ÁLLOMÁNY !" + (var19.containsKey("info") ? " - " + var19.get("info") : ""));
                  var18 = null;
               } else {
                  var19.put("state", ((Hashtable)var13[2]).get("state"));
                  var19.put("save_date", this.formatDate(((Hashtable)var13[3]).get("saved")));

                  for(int var20 = 0; var20 < DI_KEYS.length; ++var20) {
                     if (!var19.containsKey(DI_KEYS[var20])) {
                        var18.add("");
                     } else {
                        var18.add(var19.get(DI_KEYS[var20]));
                     }
                  }

                  if (!this.alreadyInList(var18)) {
                     this.fileTableModel.addRow(var18);
                  } else {
                     var2.add(var18.get(0));
                  }
               }
            }
         }
      }

      if (var2.size() > 0) {
         new ErrorDialog(MainFrame.thisinstance, "Az alábbi fájlok már a listában vannak", true, false, var2);
      }

      if (var3.size() > 0) {
         new ErrorDialog(MainFrame.thisinstance, "Az alábbi fájlokat nem adtuk a listához", true, false, var3);
      }

      var2 = null;
      var3 = null;
   }

   private String formatDate(Object var1) {
      if (var1 == null) {
         return "";
      } else {
         char[] var3 = var1.toString().trim().toCharArray();
         String var2 = "";
         int var4 = 0;

         for(int var5 = var3.length; var4 < var5 && var4 < 14; ++var4) {
            if (var4 != 4 && var4 != 6 && var4 != 14) {
               if (var4 == 8) {
                  var2 = var2 + "   ";
               } else if (var4 == 10 || var4 == 12) {
                  var2 = var2 + ":";
               }
            } else {
               var2 = var2 + ".";
            }

            var2 = var2 + var3[var4];
         }

         return var2;
      }
   }

   public static void runGC() throws Exception {
      for(int var0 = 0; var0 < 4; ++var0) {
         _runGC();
      }

   }

   private static void _runGC() throws Exception {
      long var0 = usedMemory();
      long var2 = Long.MAX_VALUE;

      for(int var4 = 0; var0 < var2 && var4 < 500; ++var4) {
         s_runtime.runFinalization();
         s_runtime.gc();
         Thread.yield();
         var2 = var0;
         var0 = usedMemory();
      }

   }

   public static long usedMemory() {
      return s_runtime.totalMemory() - s_runtime.freeMemory();
   }

   public static long freeMemory() {
      return s_runtime.freeMemory();
   }

   public void actionPerformed(ActionEvent var1) {
      String var2;
      if (var1.getSource() instanceof JButton) {
         var2 = ((JButton)var1.getSource()).getName();
      } else {
         var2 = ((JRadioButton)var1.getSource()).getName();
      }

      int var3;
      try {
         var3 = Integer.parseInt(var2);
      } catch (NumberFormatException var6) {
         return;
      }

      switch(var3) {
      case 1:
         this.doAdd();
         break;
      case 2:
         this.doAddKr();
         break;
      case 3:
         this.doDel();
      case 4:
      default:
         break;
      case 5:
         this.startPost();
         break;
      case 6:
         if (!elindult) {
            Tools.resetLabels();

            try {
               this.release();
            } catch (Exception var5) {
               Tools.eLog(var5, 0);
            }

            this.setVisible(false);
            this.dispose();
            this.mainFrame = null;
         } else if (folyamatban) {
            folyamatban = false;
            this.postHibaLista.add(new TextWithIcon("Felhasználói megszakítás", -1));
            this.megsemButton.setEnabled(false);
         }
         break;
      case 7:
         this.logOkAction();
         break;
      case 8:
         this.logSaveAction();
      }

   }

   public void tableChanged(TableModelEvent var1) {
      this.jl.setText("     " + this.fileTableModel.getRowCount() + " db fájl a listában");
      this.setButtonState(this.fileTableModel.getRowCount() != 0);
   }

   private void logOkAction() {
      this.summaDialog.setVisible(false);
      this.summaDialog.dispose();
   }

   private void logSaveAction() {
      this.initDialog();

      boolean var1;
      do {
         var1 = true;
         int var2 = this.fc4Save.showSaveDialog(this.summaDialog);
         if (var2 == 0) {
            File var3 = this.fc4Save.getSelectedFile();
            if (var3.exists()) {
               var1 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Ilyen nevű fájl már létezik. Felülírjuk?", "Csoportos beküldés", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0;
            }

            if (var1) {
               FileOutputStream var4 = null;

               try {
                  var4 = new FileOutputStream(var3);

                  for(int var5 = 0; var5 < this.logLista.getModel().getSize(); ++var5) {
                     if (this.logLista.getModel().getElementAt(var5) instanceof TextWithIcon) {
                        var4.write((((TextWithIcon)((TextWithIcon)this.logLista.getModel().getElementAt(var5))).text + "\r\n").getBytes());
                     } else {
                        var4.write((this.logLista.getModel().getElementAt(var5).toString() + "\r\n").getBytes());
                     }
                  }

                  var4.close();
               } catch (Exception var8) {
                  try {
                     var4.close();
                  } catch (Exception var7) {
                     Tools.eLog(var7, 0);
                  }

                  GuiUtil.showMessageDialog(this.mainFrame, "A lista mentése nem sikerült!", "Hiba", 0);
               }

               defaultDirectory = this.fc4Save.getCurrentDirectory();
            }
         }
      } while(!var1);

   }

   private void news() {
      this.delButton = new JButton("Töröl");
      this.okButton = new JButton("Indítás");
      this.addButton = new JButton();
      this.addKrButton = new JButton();
      this.addButton.setText("Nyomtatvány hozzáadás");
      this.addKrButton.setText("kr fájl hozzáadás");
      this.megsemButton = new JButton("Bezár");
      this.postHibaLista = new Vector();
      this.status = new JProgressBar(0);
      this.tff = new EperMainMohuPanel.TxtFileFileter();
      this.krff = new EperMainMohuPanel.KrFileFileter();
      Object[] var1 = new Object[]{"Állomány", "Nyomtatvány neve", "Adószám", "Név", "Dátumtól", "Dátumig", "Státusz", "Információ", "Adóazonosító", "Mentve", "Megjegyzés", "Verzió", "Sablon verzió", "Szervezet"};
      this.filters = new JComboBox();
      this.summaDialog = new JDialog(this, "", true);

      try {
         this.authDialog = LoginDialogFactory.create(GateType.CEGKAPU_HIVATALIKAPU, 0, false, (String)null);
      } catch (Exception var3) {
         var3.printStackTrace();
         this.authDialog = null;
      }

      this.fileTableModel = new ReadOnlyTableModel(var1, 0);
      this.sorter = new TableSorter(this.fileTableModel);
      this.fileTable = new JTable(this.sorter);
      this.logLista = new EJList();
      this.logSP = new JScrollPane(this.logLista, 20, 30);
   }

   private void release() throws Exception {
      this.summaDialog = null;
      this.authDialog = null;
      this.addButton.removeActionListener(this);
      this.addKrButton.removeActionListener(this);
      this.delButton.removeActionListener(this);
      this.okButton.removeActionListener(this);
      this.filters.removeActionListener(this);
      this.filters.removeAllItems();
      this.megsemButton.removeActionListener(this);
      this.fc.removeActionListener(this);
      this.fc = null;
      this.fc4Save.removeActionListener(this);
      this.fc4Save = null;
      this.fileTableModel.removeTableModelListener(this);
      this.filters = null;
      this.tff = null;
      this.krff = null;
      this.logLista = null;

      for(int var1 = 0; var1 < this.resFilter.length; ++var1) {
         this.resFilter[var1] = null;
      }

      this.resFilter = null;
      this.openPanel.setFilters((String[])null);
      this.openPanel = null;
      this.fileTable.setModel(new DefaultTableModel());
      this.fileTableModel = null;
      this.sorter.setTableModel((TableModel)null);
      this.sorter = null;
      this.removeAll();
      this.fileTable = null;
      this.mainFrame = null;
   }

   private Vector getMarkedFiles4EnykFile(File var1) {
      Vector var2 = new Vector();
      String var3 = var1.getName().substring(0, var1.getName().toLowerCase().indexOf(".frm.enyk"));
      String var4 = this.sp.destPath + var3 + ".kr";
      var1 = new File(var4);
      if (var1.exists()) {
         var2.add(var4);
         return var2;
      } else {
         var4 = this.sp.destPath + var3 + "_0" + "_p" + ".kr";
         var1 = new File(var4);
         if (var1.exists()) {
            var2.add(var4);
         }

         var4 = this.sp.destPath + var3 + "_1" + "_p" + ".kr";
         var1 = new File(var4);
         if (var1.exists()) {
            var2.add(var4);
         }

         var4 = this.sp.destPath + var3 + "_2" + "_p" + ".kr";
         var1 = new File(var4);
         if (var1.exists()) {
            var2.add(var4);
         }

         return var2;
      }
   }

   private void addKrFileToList(File[] var1) {
      Vector var2 = new Vector();
      String var3 = "";
      String var6 = "500 Mbyte";
      long var7 = 524288000L;
      new Hashtable();

      for(int var10 = 0; var10 < var1.length; ++var10) {
         if (!this.alreadyInListKr(var1[var10])) {
            KrHeadParser var11 = new KrHeadParser(var1[var10].getAbsolutePath(), (String)null);

            try {
               var11.parse();
            } catch (Exception var15) {
               if (var15.getMessage() == null || !"*FORCE_END*".equals(var15.getMessage())) {
                  continue;
               }
            }

            Hashtable var9 = var11.getData();
            if (var9.containsKey("org")) {
               var3 = (String)var9.get("org");
            }

            try {
               if (OrgInfo.getInstance().hasSuccessor(var3)) {
                  var3 = OrgInfo.getInstance().getSuccessorOrgId(var3);
               }
            } catch (Exception var14) {
               var2.add(var1[var10].getAbsolutePath());
               var2.add(" - " + var14.getMessage() + "!");
               continue;
            }

            OrgResource var4 = (OrgResource)((Hashtable)OrgInfo.getInstance().getOrgList()).get(var3);
            String var5 = var4.getPreferredUplodaMaxSize();
            if ("".equals(var5) || "0".equals(var5)) {
               var5 = "100000";
            }

            var6 = var5 + " Mbyte";

            try {
               var7 = Long.parseLong(var5) * 1024L * 1024L;
            } catch (NumberFormatException var13) {
               var7 = 524288000L;
            }

            if (var1[var10].length() > var7) {
               var2.add(var1[var10].getAbsolutePath());
               var2.add(" - A dokumentum mérete meghaladja az " + var6 + "-ot. Ekkora állományt a Perkapu nem tud fogadni!");
            } else {
               Vector var12 = new Vector();
               var12.add(var1[var10].getName());
               this.fileTableModel.addRow(var12);
            }
         }
      }

      if (var2.size() > 0) {
         new ErrorDialog(MainFrame.thisinstance, "Az alábbi fájlokat nem adtuk a listához", true, false, var2);
      }

   }

   private void initFcs() {
      this.fc = new EJFileChooser("MetalFileChooserUI$1");
      this.fc.setCurrentDirectory(new File(this.sp.destPath));
      FileFilter[] var1 = this.fc.getChoosableFileFilters();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.fc.removeChoosableFileFilter(var1[var2]);
      }

      this.fc.setDialogTitle("kr fájl hozzáadás");
      this.fc4Save = new EJFileChooser();
      this.fc4Save.setDialogTitle("Lista mentése");
      this.fc.setCurrentDirectory(new File(this.sp.destPath));
      this.fc.setMultiSelectionEnabled(true);
      this.fc.addChoosableFileFilter(this.krff);
      this.fc.setFileSelectionMode(0);
      enableChangeFolderButton(this.fc);

      try {
         this.fc4Save.setCurrentDirectory(new File((String)PropertyList.getInstance().get("prop.usr.naplo")));
      } catch (Exception var6) {
         Tools.eLog(var6, 0);
      }

      try {
         ((BasicFileChooserUI)this.fc4Save.getUI()).setFileName("csoportos_muveletek_uzenetek.txt");
      } catch (ClassCastException var5) {
         try {
            this.fc4Save.setSelectedFile(new File("csoportos_muveletek_uzenetek.txt"));
         } catch (Exception var4) {
            Tools.eLog(var4, 0);
         }
      }

      this.fc4Save.addChoosableFileFilter(this.tff);
      defaultDirectory = this.fc4Save.getCurrentDirectory();
   }

   private static void enableChangeFolderButton(Container var0) {
      int var1 = var0.getComponentCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         Component var3 = var0.getComponent(var2);
         if (var3 instanceof JButton) {
            JButton var4 = (JButton)var3;
            Icon var5 = var4.getIcon();
            if (var5 != null && (var5 == UIManager.getIcon("FileChooser.homeFolderIcon") || var5 == UIManager.getIcon("FileChooser.upFolderIcon"))) {
               var4.setEnabled(false);
            }
         } else if (var3 instanceof Container) {
            enableChangeFolderButton((Container)var3);
         } else if (var3 instanceof JComboBox) {
            System.out.println(var3.getClass().getName());
         }
      }

   }

   private boolean alreadyInListKr(File var1) {
      boolean var2 = false;
      String var3 = var1.getName().toLowerCase();
      int var4 = 0;

      for(Vector var5 = this.fileTableModel.getDataVector(); var4 < var5.size() && !var2; ++var4) {
         var2 = ((Vector)var5.get(var4)).get(0).toString().toLowerCase().endsWith(var3);
      }

      return var2;
   }

   private class KrFileFileter extends FileFilter implements java.io.FileFilter {
      private KrFileFileter() {
      }

      public boolean accept(File var1) {
         if (var1.isDirectory()) {
            return true;
         } else {
            return var1.isFile() && var1.getName().toLowerCase().endsWith(".kr");
         }
      }

      public String getDescription() {
         return "kr fájlok (*.kr)";
      }

      // $FF: synthetic method
      KrFileFileter(Object var2) {
         this();
      }
   }

   private class TxtFileFileter extends FileFilter implements java.io.FileFilter {
      private TxtFileFileter() {
      }

      public boolean accept(File var1) {
         if (var1.isDirectory()) {
            return true;
         } else {
            return var1.isFile() && var1.getName().toLowerCase().endsWith(".txt");
         }
      }

      public String getDescription() {
         return "szöveges fájlok (*.txt)";
      }

      // $FF: synthetic method
      TxtFileFileter(Object var2) {
         this();
      }
   }
}
