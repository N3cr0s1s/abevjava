package hu.piller.enykp.alogic.filepanels;

import hu.piller.enykp.alogic.filepanels.attachement.EJFileChooser;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicFileChooserUI;

public class CsoportosAddForm extends JDialog implements ActionListener, ListDataListener, ItemListener {
   static final String ADD_BUTTON_NAME = "1";
   static final String LIST_LOAD_BUTTON_NAME = "2";
   static final String DEL_BUTTON_NAME = "3";
   static final String LIST_SAVE_BUTTON_NAME = "4";
   static final String IMP_BUTTON_NAME = "5";
   static final String CANCEL_BUTTON_NAME = "6";
   static final String ADDIMP_BUTTON_NAME = "7";
   static final int ADD_BUTTON_VALUE = 1;
   static final int LIST_LOAD_BUTTON_VALUE = 2;
   static final int DEL_BUTTON_VALUE = 3;
   static final int IMP_BUTTON_VALUE = 5;
   static final int LIST_SAVE_BUTTON_VALUE = 4;
   static final int CANCEL_BUTTON_VALUE = 6;
   static final int ADDIMP_BUTTON_VALUE = 7;
   String defaultextension = ".frm.enyk";
   int impFilter = 13;
   private static final String MASK_FORM_ID = "nyomtatvány azonosító";
   private static final String MASK_TAX_ID = "adószám vagy adóazonosító jel";
   private static final String MASK_NAME_ID = "név (cégnév vagy személynév)";
   private static final String MASK_DATE_FROM_ID = "időszak kezdete";
   private static final String MASK_DATE_TO_ID = "időszak vége";
   private static final String INVALID_FILE_CHARS = "\\/:*?\"<>| ";
   static final String SKIT = ".tem.enyk";
   static final String DKIT = ".frm.enyk";
   static final String[] igenNem = new String[]{"Igen", "Nem"};
   static String sysroot;
   static String root;
   static String dataPath;
   static String templatePath;
   static String importPath;
   ErrorDialog summaDialog;
   JList logLista = new JList();
   Frame mainFrame;
   BookModel bm;
   DefaultListModel listModel = new DefaultListModel();
   JList fileLista;
   JFileChooser fc;
   CsoportosAddForm.ImpFileFileter iff;
   CsoportosAddForm.DatFileFileter dff;
   CsoportosAddForm.XmlFileFileter xff;
   CsoportosAddForm.EnykfrmFileFileter eff;
   JLabel status;
   JLabel jl;
   JButton delButton;
   JButton okButton;
   JButton saveButton;
   JButton addButton;
   JButton addimpButton;
   JButton listButton;
   JLabel infoL;
   JButton megsemButton;
   static boolean elindult = false;
   static boolean folyamatban = false;
   static boolean voltAlert = false;
   static boolean outOfMemory = false;
   static File defaultDirectory = null;
   int panelH;
   int space;

   public CsoportosAddForm(Frame var1, BookModel var2) throws HeadlessException {
      super(var1, "Csoportos hozzáadás", true);
      this.fileLista = new JList(this.listModel);
      this.fc = new JFileChooser();
      this.iff = new CsoportosAddForm.ImpFileFileter();
      this.dff = new CsoportosAddForm.DatFileFileter();
      this.xff = new CsoportosAddForm.XmlFileFileter();
      this.eff = new CsoportosAddForm.EnykfrmFileFileter();
      this.infoL = new JLabel("...fájl hozzáadása");
      this.panelH = 100;
      this.space = 5;
      this.setLayout(new BorderLayout(10, 10));
      this.bm = var2;
      voltAlert = false;
      this.mainFrame = var1;
      this.status = new JLabel(" ");
      this.jl = new JLabel("     0 db fájl a listában");
      this.delButton = new JButton("Töröl");
      this.okButton = new JButton("Indítás");
      this.saveButton = new JButton("Lista mentése");
      this.addButton = new JButton("Abevjava...");
      this.addimpButton = new JButton("Import...");
      this.listButton = new JButton("Lista betöltése");
      int var3 = Math.max(GuiUtil.getW("WLista betöltéseW"), GuiUtil.getW("W...fájl hozzáadásaW"));
      this.fileLista.setSelectionMode(2);
      this.listModel.addListDataListener(this);
      this.fc.setMultiSelectionEnabled(true);
      this.fc.addChoosableFileFilter(new CsoportosAddForm.TxtFileFileter());
      this.setButtonState(false);
      int var4 = var1.getX() + var1.getWidth() / 2 - 250;
      if (var4 < 0) {
         var4 = 0;
      }

      int var5 = var1.getY() + var1.getHeight() / 2 - 200;
      if (var5 < 0) {
         var5 = 0;
      }

      this.setBounds(var4, var5, 600, 400);
      JPanel var6 = new JPanel(new BorderLayout());
      JPanel var7 = new JPanel((LayoutManager)null);
      JPanel var8 = new JPanel(new FlowLayout(1));
      int var9 = this.space;
      this.addButton.setName("1");
      this.addButton.addActionListener(this);
      this.addButton.setBounds(this.space, var9, var3, GuiUtil.getCommonItemHeight() + 2);
      var9 += GuiUtil.getCommonItemHeight() + 6;
      this.addimpButton.setName("7");
      this.addimpButton.addActionListener(this);
      this.addimpButton.setBounds(this.space, var9, var3, GuiUtil.getCommonItemHeight() + 2);
      var9 += GuiUtil.getCommonItemHeight() + 6;
      this.infoL.setBounds(this.space, var9, var3, GuiUtil.getCommonItemHeight() + 2);
      var9 += GuiUtil.getCommonItemHeight() + 10;
      this.listButton.setName("2");
      this.listButton.addActionListener(this);
      this.listButton.setBounds(this.space, var9, var3, GuiUtil.getCommonItemHeight() + 2);
      var9 += GuiUtil.getCommonItemHeight() + 6;
      this.delButton.setName("3");
      this.delButton.addActionListener(this);
      this.delButton.setBounds(this.space, var9, var3, GuiUtil.getCommonItemHeight() + 2);
      var9 += GuiUtil.getCommonItemHeight() + 6;
      this.saveButton.setName("4");
      this.saveButton.addActionListener(this);
      this.saveButton.setBounds(this.space, var9, var3, GuiUtil.getCommonItemHeight() + 2);
      var9 += GuiUtil.getCommonItemHeight() + 6;
      var7.add(this.addButton);
      var7.add(this.addimpButton);
      var7.add(this.infoL);
      var7.add(this.listButton);
      var7.add(this.delButton);
      var7.add(this.saveButton);
      this.panelH = var9 + GuiUtil.getCommonItemHeight() + 10;
      JScrollPane var10 = new JScrollPane(this.fileLista, 20, 30);
      int var11 = Math.max(420, GuiUtil.getW("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW"));
      this.panelH = Math.max(this.panelH, 8 * GuiUtil.getCommonItemHeight());
      var10.setBounds(this.space, 0, var11, 8 * GuiUtil.getCommonItemHeight());
      var10.setSize(var11, 8 * GuiUtil.getCommonItemHeight());
      var10.setPreferredSize(var10.getSize());
      var10.setMinimumSize(var10.getSize());
      var7.setBounds(GuiUtil.getPositionFromPrevComponent(var10) + 2 * this.space, 0, var3 + 2 * this.space, this.panelH);
      var7.setSize(var3 + 2 * this.space, this.panelH);
      var7.setPreferredSize(var7.getSize());
      var7.setMinimumSize(var7.getSize());
      var6.add(var10, "Center");
      var6.add(var7, "East");
      var6.add(this.status, "South");
      var6.setSize(new Dimension((int)Math.min((double)GuiUtil.getW("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW" + this.infoL.getText()), 0.8D * (double)GuiUtil.getScreenW()), (int)(var7.getSize().getHeight() + (double)(3 * GuiUtil.getCommonItemHeight()))));
      var6.setPreferredSize(var6.getSize());
      var6.setMinimumSize(var6.getSize());
      this.getContentPane().add(var6, "Center");
      this.setSize(new Dimension(var6.getWidth(), 30 + var6.getHeight() + 3 * GuiUtil.getCommonItemHeight()));
      this.setPreferredSize(this.getSize());
      this.setMinimumSize(this.getSize());
      this.okButton.setName("5");
      this.okButton.addActionListener(this);
      this.megsemButton = new JButton("Mégsem");
      this.megsemButton.setName("6");
      this.megsemButton.addActionListener(this);
      var8.add(this.okButton);
      var8.add(this.megsemButton);
      var8.setSize(new Dimension(GuiUtil.getW("WWIndításWWWWMégsemWW"), 3 * GuiUtil.getCommonItemHeight()));
      var8.setPreferredSize(var8.getSize());
      var8.setMinimumSize(var8.getSize());
      var8.setBorder(new EmptyBorder(5, 5, 5, 5));
      this.jl.setSize(new Dimension(GuiUtil.getW(this.jl, this.jl.getText()), GuiUtil.getCommonItemHeight() + 4));
      this.jl.setPreferredSize(this.jl.getSize());
      this.getContentPane().add(this.jl, "North");
      this.getContentPane().add(var8, "South");
      this.pack();
      this.setResizable(true);
      this.setVisible(true);
   }

   private void setButtonState(boolean var1) {
      this.delButton.setEnabled(var1);
      this.saveButton.setEnabled(var1);
      this.okButton.setEnabled(var1);
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
      } catch (NumberFormatException var5) {
         return;
      }

      switch(var3) {
      case 1:
         this.doAdd();
         break;
      case 2:
         this.doList();
         break;
      case 3:
         this.doDel();
         break;
      case 4:
         this.doSaveList();
         break;
      case 5:
         this.doImp();
         break;
      case 6:
         if (!elindult) {
            this.bm = null;
            this.setVisible(false);
            this.fc = null;
            this.iff = null;
            this.dff = null;
            this.eff = null;
            this.xff = null;
            this.summaDialog = null;
            this.fileLista = null;
            this.listModel = null;
            this.status = this.jl = null;
            this.delButton = this.okButton = this.saveButton = this.addButton = this.addimpButton = this.listButton = null;
            this.getContentPane().removeAll();
            this.getContentPane().setLayout((LayoutManager)null);
            this.getRootPane().removeAll();
            this.removeAll();
            this.dispose();
            this.mainFrame = null;
         } else if (folyamatban) {
            folyamatban = false;
         }
         break;
      case 7:
         this.doAddimp();
      }

   }

   private void doAdd() {
      ABEVOpenPanel var1 = new ABEVOpenPanel();
      var1.setMode("open_multi");
      var1.setPath(new NecroFile((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.saves")));
      var1.setFilters(new String[]{"inner_data_loader_v1"});
      Hashtable var2 = var1.showDialog();
      if (var2 != null && var2.size() != 0) {
         Object[] var3 = (Object[])((Object[])var2.get("selected_files"));

         for(int var4 = 0; var4 < var3.length; ++var4) {
            File var5 = (File)((Object[])((Object[])var3[var4]))[0];
            if (!this.addToList(var5.getAbsolutePath())) {
               GuiUtil.showMessageDialog(this.mainFrame, "A fájl már a listában van!\n(" + var5.getAbsolutePath() + ")", "Hiba", 0);
            }
         }
      }

   }

   private void doAddimp() {
      EJFileChooser var1 = new EJFileChooser();
      var1.setCurrentDirectory(new NecroFile((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.import")));
      var1.setDialogTitle("Import állomány kiválasztása");
      FileFilter var2 = new FileFilter() {
         public boolean accept(File var1) {
            if (var1.getAbsolutePath().toLowerCase().endsWith(".imp")) {
               return true;
            } else {
               return var1.isDirectory();
            }
         }

         public String getDescription() {
            return "IMP állományok";
         }
      };
      FileFilter var10000 = new FileFilter() {
         public boolean accept(File var1) {
            if (var1.getAbsolutePath().toLowerCase().endsWith(".xml")) {
               return true;
            } else {
               return var1.isDirectory();
            }
         }

         public String getDescription() {
            return "XML állományok";
         }
      };
      var1.removeChoosableFileFilter(var1.getChoosableFileFilters()[0]);
      var1.addChoosableFileFilter(var2);
      var1.setMultiSelectionEnabled(true);
      int var4 = var1.showOpenDialog(MainFrame.thisinstance);
      if (var4 == 0) {
         File[] var5 = var1.getSelectedFiles();
         if (var5 == null || var5.length == 0) {
            return;
         }

         for(int var6 = 0; var6 < var5.length; ++var6) {
            File var7 = var5[var6];
            if (var7 != null && var7.exists() && !this.addToList(var7.getAbsolutePath())) {
               GuiUtil.showMessageDialog(this.mainFrame, "A fájl már a listában van!\n(" + var7.getAbsolutePath() + ")", "Hiba", 0);
            }
         }
      }

   }

   private void doDel() {
      if (this.fileLista.getMaxSelectionIndex() > -1 && JOptionPane.showOptionDialog(this, "Biztosan törli a kijelölt fájlokat a listából?", "Kérdés", 0, 3, (Icon)null, igenNem, igenNem[0]) == 0) {
         for(int var1 = this.listModel.size(); var1 >= 0; --var1) {
            if (this.fileLista.isSelectedIndex(var1)) {
               this.listModel.remove(var1);
            }
         }

         if (this.listModel.size() == 0) {
            this.delButton.setEnabled(false);
            this.saveButton.setEnabled(false);
         }
      }

   }

   private void doList() {
      Vector var1 = new Vector();
      this.initDialog("Megnyitás");
      File var2 = new NecroFile((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.import"));

      try {
         ((BasicFileChooserUI)this.fc.getUI()).setFileName("hozzaadas_lista.txt");
      } catch (ClassCastException var9) {
         try {
            this.fc.setSelectedFile(new NecroFile("hozzaadas_lista.txt"));
         } catch (Exception var8) {
            Tools.eLog(var8, 0);
         }
      }

      this.fc.setCurrentDirectory(var2);
      int var3 = this.fc.showOpenDialog(this);
      if (var3 == 0) {
         File[] var4 = this.fc.getSelectedFiles();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            try {
               this.parseFile(var4[var5].getAbsolutePath(), var1);
            } catch (Exception var7) {
               Tools.eLog(var7, 0);
            }
         }

         if (var1.size() > 0) {
            this.fillDialog("Lista betöltése", var1);
         }

         defaultDirectory = this.fc.getCurrentDirectory();
      }

      this.delButton.setEnabled(true);
      this.saveButton.setEnabled(true);
   }

   private void doSaveList() {
      this.initDialog("Mentés");
      File var1 = new NecroFile((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.saves"));

      try {
         ((BasicFileChooserUI)this.fc.getUI()).setFileName("hozzaadas_lista.txt");
      } catch (ClassCastException var9) {
         try {
            this.fc.setSelectedFile(new NecroFile("hozzaadas_lista.txt"));
         } catch (Exception var8) {
            Tools.eLog(var8, 0);
         }
      }

      this.fc.setCurrentDirectory(var1);
      int var2 = this.fc.showSaveDialog(this);
      if (var2 == 0) {
         File var3 = this.fc.getSelectedFile();
         FileOutputStream var4 = null;

         try {
            var4 = new NecroFileOutputStream(var3);

            for(int var5 = 0; var5 < this.listModel.size(); ++var5) {
               var4.write((this.listModel.get(var5) + "\r\n").getBytes());
            }

            var4.close();
         } catch (Exception var10) {
            try {
               var4.close();
            } catch (Exception var7) {
               Tools.eLog(var10, 0);
            }

            GuiUtil.showMessageDialog(this.mainFrame, "A lista mentése nem sikerült!", "Hiba", 0);
         }

         defaultDirectory = this.fc.getCurrentDirectory();
      }

   }

   private void parseFile(String var1, Vector var2) throws Exception {
      File var3 = new NecroFile(var1);
      if (!var3.exists()) {
         GuiUtil.showMessageDialog(this.mainFrame, "Nem található a listafájl!", "Hiba", 0);
      } else {
         BufferedReader var5 = new BufferedReader(new FileReader(var3));
         boolean var6 = false;

         while(true) {
            String var4;
            while((var4 = var5.readLine()) != null) {
               var6 = true;
               if (!var4.toLowerCase().endsWith(this.defaultextension) && !var4.toLowerCase().endsWith(".imp")) {
                  var2.add(new TextWithIcon(var4 + " - nem megfelelő kiterjesztés"));
               } else {
                  try {
                     File var7 = new NecroFile(var4);
                     if (!var7.exists()) {
                        var7 = new NecroFile(var1.substring(0, var1.lastIndexOf(File.separator) + 1) + var4);
                        if (!var7.exists()) {
                           var2.add(new TextWithIcon(var4 + " - nem található a fájl"));
                           continue;
                        }

                        var4 = var1.substring(0, var1.lastIndexOf(File.separator) + 1) + var4;
                     }
                  } catch (Exception var8) {
                     var2.add(new TextWithIcon(var4 + " - hiba a beolvasáskor"));
                     continue;
                  }

                  if (!this.addToList(var4)) {
                     var2.add(new TextWithIcon(var4 + " - a fájl már a listában van"));
                  }
               }
            }

            if (!var6) {
               var2.add(new TextWithIcon("Üres lista állományt választott! (" + var1 + ")"));
            }

            return;
         }
      }
   }

   private void doImp() {
      try {
         this.okButton.setEnabled(false);
         this.delButton.setEnabled(false);
         this.saveButton.setEnabled(false);
         this.addButton.setEnabled(false);
         this.addimpButton.setEnabled(false);
         this.listButton.setEnabled(false);
         this.megsemButton.setEnabled(false);
         if (this.listModel.size() == 0) {
            GuiUtil.showMessageDialog(this.mainFrame, "Nincs elem a listában!", "Hiba", 0);
            return;
         }

         SwingWorker var1 = new SwingWorker() {
            int importedFileCount = 0;
            int fileCount;
            Vector impHibaLista;

            {
               this.fileCount = CsoportosAddForm.this.listModel.size();
               this.impHibaLista = new Vector();
            }

            public Object doInBackground() {
               try {
                  CsoportosAddForm.folyamatban = true;
                  if (CsoportosAddForm.this.listModel.size() != 0) {
                     this.impHibaLista.add(new TextWithIcon("Start : " + CsoportosAddForm.this.getTimeString(), 2));
                  }

                  while(CsoportosAddForm.this.listModel.size() != 0) {
                     CsoportosAddForm.this.okButton.setEnabled(false);
                     CsoportosAddForm.this.delButton.setEnabled(false);
                     CsoportosAddForm.this.saveButton.setEnabled(false);
                     CsoportosAddForm.this.addButton.setEnabled(false);
                     CsoportosAddForm.this.addimpButton.setEnabled(false);
                     CsoportosAddForm.this.listButton.setEnabled(false);
                     CsoportosAddForm.this.megsemButton.setEnabled(false);
                     if (!CsoportosAddForm.folyamatban) {
                        break;
                     }

                     CsoportosAddForm.this.status.setText("     " + CsoportosAddForm.this.listModel.get(0) + " fájl betöltése folyamatban...");
                     boolean var1 = false;

                     Vector var2;
                     try {
                        File var3 = new NecroFile(CsoportosAddForm.this.listModel.get(0).toString());
                        var2 = CsoportosAddForm.this.bm.add2(var3, true);
                        var1 = CsoportosAddForm.this.bm.hasAddError;
                     } catch (OutOfMemoryError var4) {
                        CsoportosAddForm.outOfMemory = true;
                        return "";
                     }

                     if (var1) {
                        this.impHibaLista.add(new TextWithIcon((String)CsoportosAddForm.this.listModel.get(0), 0));
                        this.impHibaLista.add(new TextWithIcon("Hiba történt a kijelölt fájl betöltésekor!"));
                        this.impHibaLista.addAll(var2);
                        this.impHibaLista.add(new TextWithIcon("----------------------------------------------------------------", -1));
                        SwingUtilities.invokeAndWait(new Runnable() {
                           public void run() {
                              CsoportosAddForm.this.listModel.remove(0);
                           }
                        });
                     } else {
                        ++this.importedFileCount;
                        this.impHibaLista.add(new TextWithIcon("A(z) " + CsoportosAddForm.this.listModel.get(0) + " fájlt sikeresen feldolgoztuk.", 3));
                        this.impHibaLista.addAll(var2);
                        this.impHibaLista.add(new TextWithIcon("----------------------------------------------------------------", -1));
                        SwingUtilities.invokeAndWait(new Runnable() {
                           public void run() {
                              CsoportosAddForm.this.listModel.remove(0);
                           }
                        });
                        CsoportosAddForm.this.status.setText(" ");
                     }
                  }
               } catch (Exception var5) {
                  Tools.eLog(var5, 0);
               }

               return new Object();
            }

            public void done() {
               CsoportosAddForm.folyamatban = false;
               CsoportosAddForm.elindult = false;
               CsoportosAddForm.this.status.setText(" ");
               this.impHibaLista.add(new TextWithIcon("Hozzáadásra kijelölt fájlok száma: " + this.fileCount, 2));
               this.impHibaLista.add(new TextWithIcon("Sikeresen feldolgozott fájlok száma: " + this.importedFileCount, 2));
               if (CsoportosAddForm.outOfMemory) {
                  this.impHibaLista.add(new TextWithIcon("A művelet elvégzéséhez kevés a memória! Kérjük indítsa újra az alkalmazást!"));
               }

               CsoportosAddForm.this.fillDialog("A csoportos hozzáadás eredménye:", this.impHibaLista);
               CsoportosAddForm.this.addButton.setEnabled(true);
               CsoportosAddForm.this.addimpButton.setEnabled(true);
               CsoportosAddForm.this.listButton.setEnabled(true);
               CsoportosAddForm.this.megsemButton.setEnabled(true);
            }
         };
         elindult = true;
         var1.execute();
      } catch (Exception var2) {
         this.status.setText(" ");
      }

   }

   private boolean addToList(String var1) {
      if (!this.listModel.contains(var1)) {
         this.listModel.addElement(var1);
         return true;
      } else {
         return false;
      }
   }

   private void initDialog(String var1) {
      this.fc.setDialogTitle(var1);
      if (defaultDirectory != null) {
         this.fc.setCurrentDirectory(defaultDirectory);
      }

      try {
         ((BasicFileChooserUI)this.fc.getUI()).setFileName("");
      } catch (ClassCastException var5) {
         try {
            this.fc.setSelectedFile(new NecroFile(""));
         } catch (Exception var4) {
            Tools.eLog(var4, 0);
         }
      }

      this.fc.setSelectedFile((File)null);
   }

   private void initLogDialog() {
      JPanel var1 = new JPanel();
      JButton var2 = new JButton("Rendben");
      var2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            CsoportosAddForm.this.summaDialog.dispose();
         }
      });
      JButton var3 = new JButton("Napló mentése");
      var3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            CsoportosAddForm.this.initDialog("Napló mentés");

            try {
               ((BasicFileChooserUI)CsoportosAddForm.this.fc.getUI()).setFileName("naplo.txt");
            } catch (ClassCastException var9) {
               try {
                  CsoportosAddForm.this.fc.setSelectedFile(new NecroFile("naplo.txt"));
               } catch (Exception var8) {
                  Tools.eLog(var8, 0);
               }
            }

            int var2 = CsoportosAddForm.this.fc.showSaveDialog(CsoportosAddForm.this.summaDialog);
            if (var2 == 0) {
               File var3 = CsoportosAddForm.this.fc.getSelectedFile();
               FileOutputStream var4 = null;

               try {
                  var4 = new NecroFileOutputStream(var3);

                  for(int var5 = 0; var5 < CsoportosAddForm.this.logLista.getModel().getSize(); ++var5) {
                     var4.write((CsoportosAddForm.this.logLista.getModel().getElementAt(var5) + "\r\n").getBytes());
                  }

                  var4.close();
               } catch (Exception var10) {
                  try {
                     var4.close();
                  } catch (Exception var7) {
                     Tools.eLog(var10, 0);
                  }

                  GuiUtil.showMessageDialog(CsoportosAddForm.this.mainFrame, "A lista mentése nem sikerült!", "Hiba", 0);
               }

               CsoportosAddForm.defaultDirectory = CsoportosAddForm.this.fc.getCurrentDirectory();
            }

         }
      });
      var1.add(var2);
      var1.add(var3);
      this.summaDialog.getContentPane().add(var1, "South");
   }

   private String getTimeString() {
      SimpleDateFormat var1 = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
      return var1.format(Calendar.getInstance().getTime());
   }

   private void fillDialog(String var1, Vector var2) {
      this.summaDialog = new ErrorDialog(MainFrame.thisinstance, var1, true, true, var2);
      this.summaDialog.setBounds(this.getX() + 200, this.getY() + 200, 400, 400);
   }

   public void contentsChanged(ListDataEvent var1) {
      this.jl.setText("     " + this.listModel.size() + " db fájl a listában");
      this.setButtonState(this.listModel.size() != 0);
   }

   public void intervalAdded(ListDataEvent var1) {
      this.jl.setText("     " + this.listModel.size() + " db fájl a listában");
      this.setButtonState(this.listModel.size() != 0);
   }

   public void intervalRemoved(ListDataEvent var1) {
      this.jl.setText("     " + this.listModel.size() + " db fájl a listában");
      this.setButtonState(this.listModel.size() != 0);
   }

   public void itemStateChanged(ItemEvent var1) {
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

   private class EnykfrmFileFileter extends FileFilter implements java.io.FileFilter {
      private EnykfrmFileFileter() {
      }

      public boolean accept(File var1) {
         if (var1.isDirectory()) {
            return true;
         } else {
            return var1.isFile() && var1.getName().toLowerCase().endsWith(".frm.enyk");
         }
      }

      public String getDescription() {
         return "ENYK fájlok (*.frm.enyk)";
      }

      // $FF: synthetic method
      EnykfrmFileFileter(Object var2) {
         this();
      }
   }

   private class XmlFileFileter extends FileFilter implements java.io.FileFilter {
      private XmlFileFileter() {
      }

      public boolean accept(File var1) {
         if (var1.isDirectory()) {
            return true;
         } else {
            return var1.isFile() && var1.getName().toLowerCase().endsWith(".xml");
         }
      }

      public String getDescription() {
         return "abev xml fájlok (*.xml)";
      }

      // $FF: synthetic method
      XmlFileFileter(Object var2) {
         this();
      }
   }

   private class DatFileFileter extends FileFilter implements java.io.FileFilter {
      private DatFileFileter() {
      }

      public boolean accept(File var1) {
         if (var1.isDirectory()) {
            return true;
         } else {
            return var1.isFile() && var1.getName().toLowerCase().endsWith(".dat");
         }
      }

      public String getDescription() {
         return "abev adatfájlok (*.dat)";
      }

      // $FF: synthetic method
      DatFileFileter(Object var2) {
         this();
      }
   }

   private class ImpFileFileter extends FileFilter implements java.io.FileFilter {
      private ImpFileFileter() {
      }

      public boolean accept(File var1) {
         if (var1.isDirectory()) {
            return true;
         } else {
            return var1.isFile() && var1.getName().toLowerCase().endsWith(".imp");
         }
      }

      public String getDescription() {
         return "abev import fájlok (*.imp)";
      }

      // $FF: synthetic method
      ImpFileFileter(Object var2) {
         this();
      }
   }
}
