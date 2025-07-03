package hu.piller.enykp.alogic.filepanels.attachement;

import hu.piller.enykp.alogic.ebev.EbevTools;
import hu.piller.enykp.alogic.ebev.SendParams;
import hu.piller.enykp.alogic.filesaver.xml.EnykXmlSaver;
import hu.piller.enykp.alogic.fileutil.FileNameResolver;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import hu.piller.enykp.util.base.tabledialog.TooltipTableHeader;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;
import me.necrocore.abevjava.NecroFileWriter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicFileChooserUI;

public class AtcTools {
   public static final int MAIN_WIDTH = 800;
   public static final int MAIN_HEIGHT = 420;
   private boolean txtmode;
   private Vector columnNames;
   private Vector v0;

   public AtcTools(BookModel var1) {
      this(var1, false);
   }

   public AtcTools(final BookModel var1, boolean var2) {
      this.txtmode = false;
      this.txtmode = var2;
      final JDialog var3 = new JDialog(MainFrame.thisinstance, "Fejlesztői segítség - csatolmányok", true);
      MainFrame var4 = MainFrame.thisinstance;
      JButton var5 = new JButton("Teszt állomány készítése");
      if (this.txtmode) {
         var5.setText("Állományba mentés");
      }

      JButton var6 = new JButton("Mégsem");
      JPanel var7 = this.init(var1, var5, var6);
      if (var7 != null) {
         var3.getContentPane().add(var7);
         int var8 = var4.getX() + var4.getWidth() / 2 - 400;
         if (var8 < 0) {
            var8 = 0;
         }

         int var9 = var4.getY() + var4.getHeight() / 2 - 210;
         if (var9 < 0) {
            var9 = 0;
         }

         var3.setBounds(var8, var9, 800, 420);
         var5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1x) {
               if (AtcTools.this.txtmode) {
                  AtcTools.this.done_txt(var1);
               } else {
                  FileOutputStream var3x = null;
                  String var4 = null;

                  label237: {
                     try {
                        if (var1.cc.getLoadedfile() != null) {
                           String var5 = AttachementTool.getAtcFilename(var1.cc.getLoadedfile().getAbsolutePath(), var1);
                           if (!(new NecroFile(var5)).exists()) {
                              throw new Exception("A fájlhoz nincsen csatolmány csatolva, a csomag nem készíthető el!");
                           }

                           File var2 = AtcTools.getPackedDataFilename(var1.cc.getLoadedfile().getName());
                           if (var2 == null) {
                              return;
                           }

                           Hashtable var6 = new Hashtable();

                           try {
                              var3x = new NecroFileOutputStream(var5 + ".teszt");
                              var3x.write("encoding=\"utf-8\"\n".getBytes("utf-8"));
                           } catch (Exception var34) {
                              return;
                           }

                           Object var7 = null;

                           try {
                              var7 = AttachementTool.loadAtcFile(new NecroFile(var5), true);
                           } catch (Exception var33) {
                              var33.printStackTrace();
                           }

                           if (var7 instanceof String) {
                              AtcTools.alert("HIBA! " + var7);
                              return;
                           }

                           Vector var8 = (Vector)var7;

                           for(int var9 = 0; var9 < var8.size(); ++var9) {
                              String[] var10 = (String[])((String[])var8.elementAt(var9));
                              File var11 = new NecroFile(var10[0]);
                              String var12 = var11.getName();
                              var12 = FileNameResolver.ektelen(var12);
                              String var13 = var11.getParentFile().getName();

                              try {
                                 var3x.write((var11.getParentFile().getName() + File.separator + var12 + ";" + var10[1] + ";" + var10[2] + "\n").getBytes("utf-8"));
                                 var6.put(var11.getAbsolutePath(), var13 + File.separator + var11.getName());
                              } catch (FileNotFoundException var31) {
                                 var31.printStackTrace();
                              } catch (IOException var32) {
                                 var32.printStackTrace();
                              }
                           }

                           var3x.close();
                           var6.put(var5 + ".teszt", (new NecroFile(var5)).getName());
                           EnykXmlSaver var37 = new EnykXmlSaver(var1);
                           String var38 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.tmp") + File.separator;
                           Result var39 = var37.save(var38 + var1.cc.getLoadedfile().getName().substring(0, var1.cc.getLoadedfile().getName().toLowerCase().indexOf(".frm.enyk")) + ".xml", true, true, (SendParams)null, var38 + var1.cc.getLoadedfile().getName().substring(0, var1.cc.getLoadedfile().getName().toLowerCase().indexOf(".frm.enyk")) + ".xml");
                           if (!var39.isOk()) {
                              GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiba a teszt xml fájl készítésekor!", "Hiba", 0);
                              throw new Exception("Hiba a teszt xml fájl készítésekor!\n" + var39.errorList.elementAt(0));
                           }

                           var4 = var38 + var1.cc.getLoadedfile().getName().substring(0, var1.cc.getLoadedfile().getName().toLowerCase().indexOf(".frm.enyk")) + ".xml";
                           var39 = EbevTools.checkXSD(var4);
                           if (!var39.isOk()) {
                              throw new Exception("XSD hiba a teszt xml fájl készítésekor!\n" + var39.errorList.elementAt(1));
                           }

                           var6.put(var38 + var1.cc.getLoadedfile().getName().substring(0, var1.cc.getLoadedfile().getName().toLowerCase().indexOf(".frm.enyk")) + ".xml", var1.cc.getLoadedfile().getName().substring(0, var1.cc.getLoadedfile().getName().indexOf(".frm.enyk")) + ".xml");
                           Tools.zipFileAndRename(var6, var2.getAbsolutePath(), true);
                           File var40 = new NecroFile(var5 + ".teszt");
                           var40.delete();
                           GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az 'adatfájl csatolmánnyal' csomag elkészült.\n(Létrehozott állomány: " + var2.getAbsolutePath() + ")", "Üzenet", 1);
                           break label237;
                        }

                        GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az 'adatfájl csatolmánnyal' csomag készítése nem végezhető el.\nA nyomtatvány nem volt mentve.", "Figyelmeztetés", 2);
                     } catch (Exception var35) {
                        GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az 'adatfájl csatolmánnyal' csomag készítésekor hiba történt !\n" + var35.getMessage(), "Hiba", 0);
                        break label237;
                     } finally {
                        try {
                           (new NecroFile(var4)).delete();
                           var3x.close();
                        } catch (Exception var30) {
                           Tools.eLog(var30, 0);
                        }

                     }

                     return;
                  }

                  var3.setVisible(false);
                  var3.dispose();
               }
            }
         });
         var6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               var3.setVisible(false);
               var3.dispose();
            }
         });
         if (!this.txtmode) {
            var5.doClick();
         } else {
            var3.pack();
            var3.setVisible(true);
         }
      }
   }

   private void done_txt(BookModel var1) {
      File var2 = getCsatInfoFilename("");
      if (var2 != null) {
         try {
            FileWriter var3 = new NecroFileWriter(var2);
            StringBuffer var4 = new StringBuffer();

            int var5;
            for(var5 = 0; var5 < this.columnNames.size(); ++var5) {
               var4.append(",");
               var4.append(this.columnNames.get(var5));
            }

            var3.write(var4.substring(1));
            var3.write("\n");
            var4 = new StringBuffer();

            for(var5 = 0; var5 < this.v0.size(); ++var5) {
               Vector var6 = (Vector)this.v0.get(var5);

               for(int var7 = 0; var7 < var6.size(); ++var7) {
                  var4.append(",");
                  var4.append(var6.get(var7));
               }

               var3.write(var4.substring(1));
               var3.write("\n");
            }

            var3.close();
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A csatolmany_parameterek.txt elkészült.", "Üzenet", 1);
         } catch (IOException var8) {
         }

      }
   }

   private JPanel init(BookModel var1, JButton var2, JButton var3) {
      Vector var4 = null;

      try {
         var4 = AttachementTool.parseAtcDataFromTemplate(var1.attachementsall, var1.getIndex(var1.get()));
      } catch (Exception var16) {
         var16.printStackTrace();
      }

      if (var4 == null) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az adott nyomtatványhoz nem csatolható csatolmány, a művelet nem végezhető el!", "Üzenet", 1);
         return null;
      } else {
         int var7;
         try {
            if (!this.txtmode) {
               if (var1.cc.getLoadedfile() == null) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az 'adatfájl csatolmánnyal' csomag készítése nem végezhető el.\nA nyomtatvány nem volt mentve.", "Figyelmeztetés", 2);
                  return null;
               }

               String var5 = AttachementTool.getAtcFilename(var1.cc.getLoadedfile().getAbsolutePath(), var1);
               if (!(new NecroFile(var5)).exists()) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatványhoz még nincs állomány csatolva, ezért az XCZ állomány nem készíthető el.", "Figyelmeztetés", 2);
                  return null;
               }

               Vector var6 = Tools.check();
               if (var6.size() > 0) {
                  var7 = showErrorDialog(var6);
                  if (var7 == 1) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatvány súlyos hibát is tartalmaz, a művelet nem folytatható!", "Üzenet", 1);
                     return null;
                  }

                  if (var7 == -1) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "Felhasználói megszakítás!", "Üzenet", 1);
                     return null;
                  }
               }
            }
         } catch (Exception var17) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatvány ellenőrzése nem végezhető el. A művelet nem folytatható!", "Hiba", 0);
            var17.printStackTrace();
            return null;
         }

         JPanel var18 = new JPanel(new BorderLayout(10, 10));
         JLabel var19 = new JLabel("A " + var1.get_formname() + " nyomtatványhoz csatolható csatolmánytípusok:");
         var18.setPreferredSize(new Dimension(Math.max(800, GuiUtil.getW(var19, var19.getText())), 420));
         var18.add(var19, "North");
         boolean var20 = false;

         try {
            var7 = (Integer)var4.get(0);
         } catch (Exception var15) {
            var7 = 0;
         }

         boolean var8 = false;

         int var21;
         try {
            var21 = (Integer)var4.get(1);
         } catch (Exception var14) {
            var21 = 0;
         }

         AttachementInfo[] var9 = (AttachementInfo[])((AttachementInfo[])var4.get(2));
         var4.removeAllElements();

         for(int var10 = 0; var10 < var9.length; ++var10) {
            Vector var11 = new Vector();
            var11.add(var9[var10].description);
            var11.add(var9[var10].exts);
            var11.add(var9[var10].required ? "igen" : "nem");
            var11.add(var9[var10].minCount + "");
            var11.add(var9[var10].maxCount == 0 ? "korlátlan" : var9[var10].maxCount);
            var11.add(var7 == 0 ? "korlátlan" : var7 + "");
            var11.add(var21 == 0 ? "korlátlan" : var21 + "");
            var4.add(var11);
         }

         this.columnNames = new Vector();
         this.columnNames.add("Típus");
         this.columnNames.add("kiterjesztések");
         this.columnNames.add("kötelező");
         this.columnNames.add("min darab");
         this.columnNames.add("max darab");
         this.columnNames.add("1 cst. méret (byte)");
         this.columnNames.add("össz. cst. méret (byte)");
         this.v0 = var4;
         JTable var22 = new JTable(var4, this.columnNames);
         var22.setEnabled(false);
         if (GuiUtil.modGui()) {
            var22.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
         }

         GuiUtil.setTableColWidth(var22);
         var22.setTableHeader(new TooltipTableHeader(var22.getColumnModel()));
         var22.setAutoResizeMode(0);
         JScrollPane var23 = new JScrollPane(var22, 20, 30);
         var23.setBounds(10, 40, 380, 100);
         var18.add(var23, "Center");
         JPanel var12 = new JPanel(new FlowLayout());
         var12.add(var2);
         var12.add(var3);
         JButton var13 = new JButton("Cst fájl áttekintő");
         var13.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               JFileChooser var2 = new JFileChooser();

               try {
                  FileFilter[] var3 = var2.getChoosableFileFilters();
                  var2.removeChoosableFileFilter(var3[0]);
               } catch (Exception var8) {
                  Tools.eLog(var8, 0);
               }

               var2.addChoosableFileFilter(AtcTools.this.new CstFileFilter());
               var2.setDialogTitle("Válasszon egy cst fájlt");
               File var9 = new NecroFile((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.saves"));
               var2.setCurrentDirectory(var9);
               int var4 = var2.showOpenDialog(MainFrame.thisinstance);
               if (var4 == 0) {
                  File var5 = var2.getSelectedFile();
                  if (!var5.getName().endsWith("cst")) {
                     return;
                  }

                  try {
                     AtcTools.parseFile(var5);
                  } catch (Exception var7) {
                     var7.printStackTrace();
                  }
               }

            }
         });
         var18.add(var12, "South");
         return var18;
      }
   }

   private static void parseFile(File var0) throws Exception {
      CstParser var1 = new CstParser();
      Vector var2 = var1.parse(var0, false);

      for(int var3 = var2.size() - 1; var3 >= 0; --var3) {
         String var4 = (String)var2.remove(var3);
         var4 = "  " + var4.replaceAll(";", " - ");
         var2.add(var4);
      }

      new ErrorDialog(MainFrame.thisinstance, "A cst fájl tartalma", true, false, var2);
   }

   private static void alert(String var0) {
      GuiUtil.showMessageDialog(MainFrame.thisinstance, var0, "Hiba", 0);
   }

   private static File getPackedDataFilename(String var0) {
      JFileChooser var1 = new JFileChooser();
      var1.setDialogTitle("'adatfájl csatolmánnyal' csomag készítése");

      File var2;
      try {
         if (var1.getName() == null) {
            try {
               ((BasicFileChooserUI)((BasicFileChooserUI)var1.getUI())).setFileName(var0.substring(0, var0.toLowerCase().indexOf(".frm.enyk")) + ".xcz");
            } catch (ClassCastException var5) {
               try {
                  var1.setSelectedFile(new NecroFile(var0.substring(0, var0.toLowerCase().indexOf(".frm.enyk")) + ".xcz"));
               } catch (Exception var4) {
                  Tools.eLog(var4, 0);
               }
            }
         }

         var2 = new NecroFile((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.import"));
         var1.setCurrentDirectory(var2);
      } catch (Exception var6) {
         Tools.eLog(var6, 0);
      }

      while(true) {
         if (var1.showSaveDialog(MainFrame.thisinstance) == 0) {
            var2 = var1.getSelectedFile();
            if (var2 != null) {
               if (!var2.getName().endsWith(".xcz")) {
                  var2 = new NecroFile(var2.getPath() + ".xcz");
               }

               if (var2.exists() && JOptionPane.showConfirmDialog(MainFrame.thisinstance, var2.getName() + " létezik !\nFelülírja ?", "'adatfájl csatolmánnyal' csomag készítése", 0) == 1) {
                  continue;
               }

               return var2;
            }
         }

         return null;
      }
   }

   private static File getCsatInfoFilename(String var0) {
      JFileChooser var1 = new JFileChooser();
      var1.setDialogTitle("Csatolmány paraméterek");

      try {
         if (var1.getName() == null) {
            try {
               ((BasicFileChooserUI)((BasicFileChooserUI)var1.getUI())).setFileName("csatolmany_parameterek.txt");
            } catch (ClassCastException var5) {
               try {
                  var1.setSelectedFile(new NecroFile("csatolmany_parameterek.txt"));
               } catch (Exception var4) {
                  Tools.eLog(var4, 0);
               }
            }
         }

         var1.setCurrentDirectory(new NecroFile((String)PropertyList.getInstance().get("prop.usr.naplo")));
      } catch (Exception var6) {
         Tools.eLog(var6, 0);
      }

      while(true) {
         if (var1.showSaveDialog(MainFrame.thisinstance) == 0) {
            File var2 = var1.getSelectedFile();
            if (var2 != null) {
               if (!var2.getName().endsWith(".txt")) {
                  var2 = new NecroFile(var2.getPath() + ".txt");
               }

               if (var2.exists() && JOptionPane.showConfirmDialog(MainFrame.thisinstance, var2.getName() + " létezik !\nFelülírja ?", "Csatolmány paraméterek", 0) == 1) {
                  continue;
               }

               return var2;
            }
         }

         return null;
      }
   }

   private static int showErrorDialog(Vector var0) {
      ErrorDialog var1 = null;
      Object var2 = null;
      byte var3 = 0;

      try {
         if (var0.size() > 0) {
            try {
               TextWithIcon var4 = (TextWithIcon)var0.get(var0.size() - 1);
               if (var4.ii == null) {
                  var0.remove(var0.size() - 1);
               }
            } catch (ClassCastException var6) {
               var0.remove(var0.size() - 1);
            }
         }

         var1 = new ErrorDialog(MainFrame.thisinstance, "Nyomtatvány hibalista!", true, true, var0, "A nyomtatvány ellenőrzése az alábbi eredményt adta:", true);
         if (hasFatalError(var0)) {
            var3 = 1;
         }

         if (var3 == 0) {
            try {
               if (var1.isProcessStoppped()) {
                  var3 = -1;
               } else {
                  var3 = 0;
               }
            } catch (Exception var5) {
               var3 = -1;
            }
         }

         var1 = null;
      } catch (HeadlessException var7) {
         Tools.eLog(var7, 0);
      }

      return var3;
   }

   private static boolean hasFatalError(Vector var0) {
      int var1 = 0;

      boolean var2;
      for(var2 = false; var1 < var0.size() && !var2; ++var1) {
         if (((TextWithIcon)var0.get(var1)).imageType == 1) {
            var2 = true;
         }
      }

      return var2;
   }

   private class CstFileFilter extends FileFilter implements java.io.FileFilter {
      private CstFileFilter() {
      }

      public boolean accept(File var1) {
         if (var1.isDirectory()) {
            return true;
         } else {
            return var1.isFile() && var1.getName().toLowerCase().endsWith(".cst");
         }
      }

      public String getDescription() {
         return "*.cst (csatolmány leírók)";
      }

      // $FF: synthetic method
      CstFileFilter(Object var2) {
         this();
      }
   }
}
