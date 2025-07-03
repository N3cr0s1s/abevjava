package hu.piller.enykp.alogic.kontroll;

import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.metainfo.MetaStore;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.util.base.Tools;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;

public class StartForm extends JPanel implements ItemListener {
   private JComboBox nyomtatvany;
   private JComboBox adoszam;
   private JTable fileTable;
   private KTools ktools;
   private static final Object[] COLNAMES = new Object[]{"Nyomtatvány típus", "Elmentve", "Megjegyzés", "Fájlnév"};
   private static final String NL = "\r\n";
   int hivatkozasSzamlalo = 1;
   int maxDidKodSzamLimit = 3;
   private JLabel path;
   private Hashtable filledRows = null;

   public StartForm(final JDialog var1, DatTableModel var2) {
      var1.setTitle(var1.getTitle() + " létrehozása");
      this.ktools = new KTools();
      BevelBorder var3 = new BevelBorder(0);
      this.setBorder(var3);
      this.setLayout(new BorderLayout());
      this.setPreferredSize(new Dimension(700, 480));
      JLabel var4 = new JLabel("A kontroll állomány elérési útja");
      this.path = new JLabel(Kontroll.kontrollPath);
      this.path.setForeground(Color.BLUE);
      this.path.setFont(new Font("dialog", 1, 14));
      String var7 = "Kontroll nyomtatvány: ";
      String var8 = "Adószám: ";
      JLabel var5 = new JLabel(var7);
      Dimension var9 = new Dimension(200, 20);

      try {
         this.nyomtatvany = new JComboBox(this.ktools.getKontrollNames(var2));
         this.nyomtatvany.setPreferredSize(var9);
         this.nyomtatvany.setMinimumSize(var9);
         this.nyomtatvany.insertItemAt("", 0);
         this.nyomtatvany.setSelectedIndex(0);
         this.nyomtatvany.setName("nyomtatvany");
         this.nyomtatvany.addItemListener(this);
      } catch (Exception var20) {
         this.nyomtatvany = new JComboBox(new String[]{"Hiba a kvf fájl olvasásakor"});
         this.nyomtatvany.setEnabled(false);
      }

      JLabel var6 = new JLabel(var8);
      this.adoszam = new JComboBox();
      this.adoszam.setPreferredSize(var9);
      this.adoszam.setMinimumSize(var9);
      this.adoszam.setName("adoszam");
      this.adoszam.addItemListener(this);
      JPanel var10 = new JPanel(new GridLayout(2, 1));
      Dimension var11 = new Dimension(690, 80);
      var10.setPreferredSize(var11);
      var10.setMinimumSize(var11);
      JPanel var12 = new JPanel(new FlowLayout(0));
      JPanel var13 = new JPanel(new FlowLayout(0));
      var12.add(var4);
      var12.add(this.path);
      var12.setPreferredSize(new Dimension(690, 60));
      var12.setMinimumSize(var12.getPreferredSize());
      var13.add(var5);
      var13.add(this.nyomtatvany);
      var13.add(var6);
      var13.add(this.adoszam);
      var10.add(var12);
      var10.add(var13);
      this.add(var10, "North");
      ReadOnlyTableModel var14 = new ReadOnlyTableModel(COLNAMES, 0);
      this.fileTable = new JTable(var14);
      this.fileTable.setShowGrid(true);
      this.fileTable.setAutoResizeMode(0);
      this.setColWidth();
      this.fileTable.setRowSelectionAllowed(true);
      this.fileTable.setModel(var14);
      JScrollPane var15 = new JScrollPane(this.fileTable, 20, 30);
      this.add(var15, "Center");
      JPanel var16 = new JPanel();
      JButton var17 = new JButton("OK");
      var17.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            try {
               if (StartForm.this.fileTable.getRowCount() == 0) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nincs megfelelő nyomtatvány", "Kontroll állományok létrehozása", 1);
               } else if (!StartForm.this.adoszam.getSelectedItem().equals("") && StartForm.this.fileTable.getSelectedRows().length > 0) {
                  StartForm.this._doPressedOkButton();
                  MainFrame.thisinstance.setGlassLabel((String)null);
               } else {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nincs kijelölt nyomtatvány illetve azonosító", "Kontroll állományok létrehozása", 1);
               }
            } catch (Exception var3) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiba a kontroll állomány létrehozásakor!" + (var3.getMessage().startsWith("*") ? var3.getMessage().substring(1) : ""), "Kontroll állományok létrehozása", 1);
               MainFrame.thisinstance.setGlassLabel((String)null);
               var3.printStackTrace();
            }

         }
      });
      var16.add(var17);
      JButton var18 = new JButton("Mégsem");
      var18.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            try {
               Tools.resetLabels();
               var1.dispose();
            } catch (Exception var3) {
               var3.printStackTrace();
            }

         }
      });
      var16.add(var18);
      JButton var19 = new JButton("Bezár");
      var19.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            try {
               Tools.resetLabels();
               var1.dispose();
            } catch (Exception var3) {
               var3.printStackTrace();
            }

         }
      });
      var16.add(var19);
      this.add(var16, "South");
      this.setVisible(true);
   }

   public void itemStateChanged(ItemEvent var1) {
      if (var1.getSource() == this.nyomtatvany) {
         this.changeComboModel(var1.getItem());
         this.adoszam.repaint();
      }

      if (var1.getSource() == this.adoszam) {
         this.changeTableModel(this.nyomtatvany.getSelectedItem() + "|" + this.adoszam.getSelectedItem());
         this.fileTable.repaint();
      }

      this.pathCimkeBeallitas();
   }

   public void changeComboModel(Object var1) {
      Vector var2 = (Vector)this.ktools.kvf_azonosito.get(var1);
      if (var2 != null) {
         Collections.sort(var2);
      }

      DefaultComboBoxModel var3;
      if (!var1.equals("")) {
         var3 = new DefaultComboBoxModel(var2);
      } else {
         var3 = new DefaultComboBoxModel();
      }

      this.adoszam.setModel(var3);

      try {
         if (var2.size() <= 0) {
            throw new Exception();
         }

         this.changeTableModel(var1 + "|" + var2.get(0));
         this.fileTable.repaint();
      } catch (Exception var5) {
         this.fileTable.setModel(new ReadOnlyTableModel(COLNAMES, 0));
         this.setColWidth();
      }

   }

   public void changeTableModel(String var1) {
      Vector var2 = (Vector)this.ktools.kvf_kdata.get(var1);
      ReadOnlyTableModel var3 = new ReadOnlyTableModel(COLNAMES, var2.size());

      for(int var5 = 0; var5 < var2.size(); ++var5) {
         KData var4 = (KData)var2.get(var5);
         var3.setValueAt(var4.informacio, var5, 0);
         var3.setValueAt(var4.mentve, var5, 1);
         var3.setValueAt(var4.megjegyzes, var5, 2);
         var3.setValueAt(var4.filename, var5, 3);
      }

      this.fileTable.setModel(var3);

      try {
         if (((KvfData)this.ktools.kvf_data.get(var1.substring(0, var1.indexOf("|")) + ".kvf")).egydatbol) {
            this.fileTable.setSelectionMode(0);
         } else {
            this.fileTable.setSelectionMode(2);
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      this.setColWidth();
   }

   private void setColWidth() {
      this.fileTable.getColumnModel().getColumn(0).setPreferredWidth(290);
      this.fileTable.getColumnModel().getColumn(1).setPreferredWidth(96);
      this.fileTable.getColumnModel().getColumn(2).setPreferredWidth(290);
      this.fileTable.getColumnModel().removeColumn(this.fileTable.getColumnModel().getColumn(3));
   }

   private void _doPressedOkButton() throws Exception {
      Vector[] var1 = ((KvfData)this.ktools.kvf_data.get((String)this.nyomtatvany.getSelectedItem() + ".kvf")).file;
      if (this.filledRows == null) {
         this.filledRows = this.fillVV(var1);
      }

      StringBuffer[] var2 = new StringBuffer[var1.length];
      int[] var3 = new int[var1.length];
      int[] var4 = new int[var1.length];
      String[] var5 = new String[var1.length];
      String var8 = "";
      String[] var9 = new String[var1.length];

      for(int var10 = 0; var10 < var1.length; ++var10) {
         var2[var10] = new StringBuffer();
         var5[var10] = var1[var10].get(0) + "_0";
         var3[var10] = 1;
      }

      this.hivatkozasSzamlalo = 1;
      String var29 = ((String)this.fileTable.getModel().getValueAt(this.fileTable.getSelectedRows()[0], 0)).toLowerCase();

      for(int var11 = 0; var11 < this.fileTable.getSelectedRows().length; ++var11) {
         for(int var12 = 0; var12 < var1.length; ++var12) {
            var4[var12] = 1;
         }

         BookModel var31 = KTools.getBookModel((String)this.fileTable.getModel().getValueAt(this.fileTable.getSelectedRows()[var11], 3), var29);
         Hashtable var13 = this.getHtFromBm(var31);
         int var14 = this.getMaxDinamikusLapSzam(var31);
         var31 = null;
         boolean var15 = false;

         for(int var16 = 0; var16 < var1.length; ++var16) {
            if (var1[var16].size() <= 2) {
               throw new Exception("Hibás a File" + var16 + " szekció szerkezete !");
            }

            var15 = this.isFolap(var1[var16], var29.toUpperCase());
            int var17 = this.getMaxDidkodSzam(var1[var16]) - 2;

            for(int var18 = 0; var18 < var14; ++var18) {
               if (!var15 || var18 <= 0) {
                  for(int var19 = 2; var19 < var17; ++var19) {
                     int var6 = var2[var16].length();
                     String var7 = "";
                     boolean var20 = false;
                     int var21 = 0;

                     for(int var22 = 2; var22 < var1[var16].size(); ++var22) {
                        if (var16 > 0 && var22 == 9) {
                           var21 = var2[var16].length();
                        }

                        String var23 = (String)var1[var16].elementAt(var22);
                        if (var23.startsWith("B")) {
                           try {
                              var8 = this.getLine(var13, var23, true, var18, var19);
                              var2[var16].append(var8);
                              if (this.getCharCount(var23, ',') > 2) {
                                 var7 = var7 + var8;
                              }

                              var20 = var20 || !this.filledRows.containsKey(var22) && !this.ures(var8);
                           } catch (Exception var27) {
                              Tools.eLog(var27, 0);
                           }
                        } else if (var23.startsWith("K")) {
                           var2[var16].append(var23.substring(1));
                        } else if (var23.startsWith("S")) {
                           try {
                              var8 = this.getLine(var13, var23, false, var18, var19);
                              var2[var16].append(var8);
                              if (this.getCharCount(var23, ',') > 2) {
                                 var7 = var7 + var8;
                              }

                              var20 = var20 || !this.filledRows.containsKey(var22) && !this.ures(var8);
                           } catch (Exception var28) {
                              Tools.eLog(var28, 0);
                           }
                        } else if (var23.startsWith("R")) {
                           var2[var16].append(this.getSzamlalo(var23, var3[var16]));
                        } else if (var23.startsWith("H")) {
                           var2[var16].append(this.getSzamlalo(var23, this.hivatkozasSzamlalo));
                        } else if (var23.startsWith("T")) {
                           var2[var16].append(this.getSzamlalo(var23, var4[var16]));
                        }
                     }

                     if (var16 > 0 && this.ures(var2[var16].substring(var21))) {
                        var20 = false;
                     }

                     if ((var17 <= this.maxDidKodSzamLimit || !this.ures(var7)) && var20) {
                        this.ujsor(var2[var16]);
                        int var10002 = var4[var16]++;
                        var10002 = var3[var16]++;
                     } else {
                        var2[var16].delete(var6, var2[var16].length());
                     }
                  }

                  if (var14 > 1) {
                     this.ujsor(var2[var16]);
                  }
               }
            }

            this.ujsor(var2[var16]);
         }

         ++this.hivatkozasSzamlalo;
      }

      String var30 = null;
      if (((String)this.adoszam.getSelectedItem()).length() == 11) {
         var30 = Kontroll.kontrollPath + File.separator + ((String)this.adoszam.getSelectedItem()).substring(0, 8);
      } else {
         var30 = Kontroll.kontrollPath + File.separator + this.adoszam.getSelectedItem();
      }

      try {
         var30 = var30 + "." + var5[0];
      } catch (Exception var26) {
         Tools.eLog(var26, 0);
      }

      boolean var32 = true;
      if ((new File(var30)).exists()) {
         var32 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Ilyen nevű kontroll állomány már létezik a\n" + Kontroll.kontrollPath + " mappában.\nFelülírja ?", "Kontroll állományok létrehozása", 0, 3, (Icon)null, Kontroll.igenNem, Kontroll.igenNem[0]) == 0;
      }

      if (var32) {
         for(int var33 = 0; var33 < var1.length; ++var33) {
            var4[var33] = 1;
            if (var33 > 0 && var3[var33] == 1) {
               var9[var33] = null;
            } else {
               if (((String)this.adoszam.getSelectedItem()).length() == 11) {
                  var9[var33] = Kontroll.kontrollPath + File.separator + ((String)this.adoszam.getSelectedItem()).substring(0, 8);
               } else {
                  var9[var33] = Kontroll.kontrollPath + File.separator + this.adoszam.getSelectedItem();
               }

               try {
                  FileOutputStream var35 = new FileOutputStream(var9[var33] + "." + var5[var33]);
                  var35.write(this.ansiToOem(var2[var33].toString()));
                  var35.close();
               } catch (IOException var25) {
                  var25.printStackTrace();
               }
            }
         }

         this.writeKifFile(var9, var5, var3[0]);
         this.writeLogFile(var9, var5, var3);
         String var34 = "A kontroll állomány";
         if (var9.length > 1) {
            var34 = var34 + "(ok)";
         }

         String var36 = "";

         for(int var37 = 0; var37 < var9.length; ++var37) {
            var36 = var36 + (var9[var37] != null ? var9[var37] + "." + var5[var37] + "   Rekordszám : " + (var3[var37] - 1) + "\n" : "");
         }

         var34 = var34 + " létrehozása véget ért !\nFájl" + (var9.length > 1 ? "(ok)" : "") + " :\n" + var36;
         GuiUtil.showMessageDialog(MainFrame.thisinstance, var34, "Kontroll állományok létrehozása", 1);
      }

   }

   private String getLine(Hashtable var1, String var2, boolean var3, int var4, int var5) throws Exception {
      String var6 = "";
      String[] var7 = var2.split(",");
      var5 = Math.min(this.getCharCount(var2, ','), var5);
      int var8 = Integer.parseInt(var7[1]);
      if (var7[var5].indexOf("_") == -1) {
         var7[var5] = var4 + "_" + var7[var5];
      }

      Object var9 = null;
      if (var1.containsKey(var7[var5])) {
         var9 = var1.get(var7[var5]);
      } else {
         String var10 = "x" + var7[var5].substring(var7[var5].indexOf("_"));
         if (var1.containsKey(var10)) {
            var9 = var1.get(var10);
         }
      }

      if (var9 == null) {
         var9 = "";
      }

      try {
         if (((String)var9).equalsIgnoreCase("true")) {
            var9 = "X";
         }

         if (((String)var9).equalsIgnoreCase("false")) {
            var9 = "";
         }
      } catch (Exception var11) {
         Tools.eLog(var11, 0);
      }

      if (var3) {
         var6 = var6 + this.getBFixString((String)var9, var8);
      } else {
         var6 = var6 + this.getSFixString((String)var9, var8);
      }

      return var6;
   }

   private String getBFixString(String var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         var1 = var1 + " ";
      }

      return var1.substring(0, var2);
   }

   private String getSFixString(String var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         var1 = "0" + var1;
      }

      return var1.substring(var1.length() - var2);
   }

   private String getSzamlalo(String var1, int var2) {
      int var3 = Integer.parseInt(var1.substring(1));
      String var4 = "" + var2;

      for(int var5 = 0; var5 < var3; ++var5) {
         var4 = "0" + var4;
      }

      return var4.substring(var4.length() - var3);
   }

   private int getMaxDinamikusLapSzam(BookModel var1) {
      int[] var2 = (int[])((int[])((Elem)var1.cc.get(0)).getEtc().get("pagecounts"));
      int var3 = var2[0];

      for(int var4 = 1; var4 < var2.length; ++var4) {
         if (var3 < var2[var4]) {
            var3 = var2[var4];
         }
      }

      return var3;
   }

   private int getMaxDidkodSzam(Vector var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         String var4 = (String)var1.elementAt(var3);
         var2 = Math.max(var2, this.getCharCount(var4, ','));
      }

      return var2 + 3;
   }

   private void ujsor(StringBuffer var1) {
      if (var1.length() >= 2) {
         if (!var1.substring(var1.length() - 2).equals("\r\n")) {
            var1.append("\r\n");
         }

      }
   }

   private int getCharCount(String var1, char var2) {
      int var3 = 0;

      for(int var4 = 0; var4 < var1.length(); ++var4) {
         if (var1.charAt(var4) == var2) {
            ++var3;
         }
      }

      return var3;
   }

   private void writeKifFile(String[] var1, String[] var2, int var3) {
      String var4;
      if (((String)this.adoszam.getSelectedItem()).length() == 11) {
         var4 = ((String)this.adoszam.getSelectedItem()).substring(0, 8) + "-" + ((String)this.adoszam.getSelectedItem()).substring(8, 9) + "-" + ((String)this.adoszam.getSelectedItem()).substring(9);
      } else {
         var4 = (String)this.adoszam.getSelectedItem();
      }

      try {
         FileOutputStream var5 = new FileOutputStream(var1[0] + "-" + var2[0].substring(0, var2[0].length() - 2) + ".kif");
         var5.write(var4.getBytes("ISO-8859-2"));
         var5.write("\r\n".getBytes());
         var5.write(((String)this.nyomtatvany.getSelectedItem()).getBytes("ISO-8859-2"));
         var5.write("\r\n".getBytes());
         var5.write(Integer.toString(var3 - 1).getBytes("ISO-8859-2"));
         var5.write("\r\n".getBytes());
         var5.write(this.getTimeString().getBytes("ISO-8859-2"));
         var5.write("\r\n".getBytes());

         for(int var6 = 0; var6 < var1.length; ++var6) {
            if (var6 <= 0 || var1[var6] != null) {
               var5.write((var1[var6] + "." + var2[var6]).getBytes("ISO-8859-2"));
               var5.write("\r\n".getBytes());
            }
         }

         var5.close();
      } catch (IOException var7) {
         var7.printStackTrace();
      }

   }

   private void writeLogFile(String[] var1, String[] var2, int[] var3) {
      try {
         FileOutputStream var4 = new FileOutputStream(Kontroll.kontrollPath + File.separator + "logfile.txt", true);
         var4.write("\r\n".getBytes());
         var4.write(this.getTimeString().getBytes("ISO-8859-2"));
         var4.write("\r\n".getBytes());
         var4.write("\r\n".getBytes());

         for(int var5 = 0; var5 < var1.length; ++var5) {
            if (var5 <= 0 || var1[var5] != null) {
               var4.write(("File : " + var1[var5] + "." + var2[var5] + "    Rekordszám = " + (var3[var5] - 1)).getBytes("ISO-8859-2"));
               var4.write("\r\n".getBytes());
            }
         }

         var4.write("--------------------------------------------------------------------------------".getBytes("ISO-8859-2"));
         var4.write("\r\n".getBytes());
         var4.close();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   private String getTimeString() throws IllegalArgumentException {
      SimpleDateFormat var1 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
      Date var2 = Calendar.getInstance().getTime();
      return var1.format(var2);
   }

   private boolean ures(String var1) {
      var1 = var1.replaceAll(" ", "");
      var1 = var1.replaceAll("0", "");
      return var1.equals("");
   }

   private void pathCimkeBeallitas() {
      if (this.adoszam.getSelectedItem() == null) {
         this.path.setText("");
      } else {
         String var1 = Kontroll.kontrollPath;
         if (this.ktools.kvf_data.get(this.nyomtatvany.getSelectedItem() + ".kvf") != null) {
            for(int var2 = 0; var2 < ((KvfData)this.ktools.kvf_data.get((String)this.nyomtatvany.getSelectedItem() + ".kvf")).file.length; ++var2) {
               if (((String)this.adoszam.getSelectedItem()).length() == 11) {
                  var1 = var1 + ((String)this.adoszam.getSelectedItem()).substring(0, 8) + "." + ((KvfData)this.ktools.kvf_data.get((String)this.nyomtatvany.getSelectedItem() + ".kvf")).file[var2].get(0) + "_0  ";
               } else {
                  var1 = var1 + this.adoszam.getSelectedItem() + "." + ((KvfData)this.ktools.kvf_data.get((String)this.nyomtatvany.getSelectedItem() + ".kvf")).file[var2].get(0) + "_0  ";
               }
            }
         }

         this.path.setText(var1);
         if (this.fileTable.getRowCount() > 0) {
            if (this.fileTable.getSelectionModel().getSelectionMode() == 0) {
               this.fileTable.setRowSelectionInterval(0, 0);
            } else {
               this.fileTable.selectAll();
            }
         }

         if (this.path.getText().length() > 60) {
            this.path.setText(this.path.getText().substring(0, 60) + "...");
         }

      }
   }

   private boolean isFolap(Vector var1, String var2) {
      for(int var3 = 0; var3 < var1.size(); ++var3) {
         String[] var4 = ((String)var1.elementAt(var3)).split(",");

         try {
            if (var4.length >= 3) {
               for(int var5 = 2; var5 < var4.length; ++var5) {
                  Object[] var6 = new Object[]{null, null, var4[var5]};
                  Object var7 = MetaInfo.getInstance().getIds(var6, var2);
                  String var8 = (String)((Object[])((Object[])var7))[0];
                  if (var8.indexOf("XXXX") > -1) {
                     return false;
                  }
               }
            }
         } catch (Exception var9) {
            Tools.eLog(var9, 0);
         }
      }

      return true;
   }

   private byte[] ansiToOem(String var1) {
      byte[] var2 = var1.getBytes();

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         if (Kontroll.ansiToOem.containsKey(var1.substring(var3, var3 + 1))) {
            var2[var3] = (Byte)Kontroll.ansiToOem.get(var1.substring(var3, var3 + 1));
         }
      }

      return var2;
   }

   private Hashtable getHtFromBm(BookModel var1) {
      if (var1 == null) {
         System.out.println("KONTROLL: bm = null");
         return null;
      } else if (var1.cc == null) {
         System.out.println("KONTROLL: bm.cc = null");
         return null;
      } else if (var1.cc.size() == 0) {
         System.out.println("KONTROLL: bm.cc.size = 0");
         return null;
      } else if (var1.cc.get(0) == null) {
         System.out.println("KONTROLL: bm.cc.get(0) = null");
         return null;
      } else {
         Hashtable var2 = new Hashtable();
         String var3 = ((Elem)var1.cc.get(0)).getType();
         IDataStore var4 = (IDataStore)((Elem)var1.cc.get(0)).getRef();
         Iterator var5 = var4.getCaseIdIterator();
         MetaInfo var6 = MetaInfo.getInstance();
         MetaStore var7 = var6.getMetaStore(var3);
         Hashtable var8 = var7.getFieldMetas();

         while(var5.hasNext()) {
            StoreItem var9 = (StoreItem)var5.next();
            String var10 = var9.toString();

            try {
               var2.put(var10.substring(0, var10.indexOf("_")) + "_" + ((Hashtable)var8.get(var9.code)).get("did"), var4.get(var10));
            } catch (Exception var12) {
               var2.put("0_" + ((Hashtable)var8.get(var9.code)).get("did"), var4.get(var10));
            }

            if (var10.indexOf("XXXX") == -1) {
               var2.put("x_" + ((Hashtable)var8.get(var9.code)).get("did"), var4.get(var10));
            }
         }

         return var2;
      }
   }

   private void doPressedOkButton() throws Exception {
      Vector[] var1 = ((KvfData)this.ktools.kvf_data.get((String)this.nyomtatvany.getSelectedItem() + ".kvf")).file;
      Hashtable[] var2 = new Hashtable[this.fileTable.getSelectedRows().length];
      BookModel[] var3 = new BookModel[this.fileTable.getSelectedRows().length];
      StringBuffer[] var4 = new StringBuffer[var1.length];
      int[] var5 = new int[var1.length];
      int[] var6 = new int[var1.length];
      String[] var7 = new String[var1.length];
      String var10 = "";
      String[] var11 = new String[var1.length];

      for(int var12 = 0; var12 < var1.length; ++var12) {
         var4[var12] = new StringBuffer();
         var7[var12] = var1[var12].get(0) + "_0";
         var5[var12] = 1;
         var6[var12] = 1;
      }

      String var36 = ((String)this.fileTable.getModel().getValueAt(this.fileTable.getSelectedRows()[0], 0)).toLowerCase();

      int var17;
      for(int var13 = 0; var13 < this.fileTable.getSelectedRows().length; ++var13) {
         var3[var13] = KTools.getBookModel((String)this.fileTable.getModel().getValueAt(this.fileTable.getSelectedRows()[var13], 3), var36);
         var2[var13] = this.getHtFromBm(var3[var13]);
         int var14 = this.getMaxDinamikusLapSzam(var3[var13]);
         boolean var15 = false;

         for(int var16 = 0; var16 < var1.length; ++var16) {
            if (var1[var16].size() <= 2) {
               throw new Exception("Hibás a File" + var16 + " szekció szerkezete !");
            }

            var15 = this.isFolap(var1[var16], var36.toUpperCase());
            var17 = this.getMaxDidkodSzam(var1[var16]);
            int var18 = var17 - 4;
            boolean var19 = true;
            int var20 = 1;
            boolean var21 = false;
            boolean var22 = true;
            boolean var23 = false;

            for(int var24 = 0; var24 < var14; ++var24) {
               if (!var15 || var24 <= 0) {
                  int var46 = 0;
                  int var25 = 2;

                  for(boolean var26 = false; var25 < var17 && !var26; ++var25) {
                     int var44 = var25;
                     int var45 = var25 + 1;
                     int var8 = var4[var16].length();
                     String var9 = "";
                     boolean var27 = false;
                     int var28 = 0;

                     for(int var29 = 2; var29 < var1[var16].size(); ++var29) {
                        if (var16 > 0 && var29 == 9) {
                           var28 = var4[var16].length();
                        }

                        String var30 = (String)var1[var16].elementAt(var29);
                        int var43 = this.getCharCount(var30, ',') - 1;
                        if (var43 > 1 && var18 % var43 == 0 && var18 / var43 > 1) {
                           if (var46 == 0) {
                              var46 = var18 / var43;
                           }

                           var20 = var43;
                        }

                        int var31;
                        if (var30.startsWith("B")) {
                           try {
                              if (var20 < var43) {
                                 var44 = (var25 - 2) * var46 + 2;
                                 var45 = var44 + var46;
                                 if (var45 >= var17 - 2) {
                                    var26 = true;
                                 }
                              }

                              for(var31 = var44; var31 < var45; ++var31) {
                                 var10 = this.getLine(var2[var13], var30, true, var24, var31);
                                 var4[var16].append(var10);
                                 if (var43 > 1) {
                                    var9 = var9 + var10;
                                 }

                                 var27 = var27 || !this.ures(var10);
                              }

                              var45 = var44 + 1;
                           } catch (Exception var34) {
                              Tools.eLog(var34, 0);
                           }
                        } else if (var30.startsWith("K")) {
                           var4[var16].append(var30.substring(1));
                        } else if (var30.startsWith("S")) {
                           try {
                              if (var20 < var43) {
                                 var44 = (var25 - 2) * var46 + 2;
                                 var45 = var44 + var46;
                                 if (var45 >= var17 - 2) {
                                    var26 = true;
                                 }
                              }

                              for(var31 = var44; var31 < var45; ++var31) {
                                 var10 = this.getLine(var2[var13], var30, false, var24, var31);
                                 var4[var16].append(var10);
                                 if (var43 > 1) {
                                    var9 = var9 + var10;
                                 }

                                 var27 = var27 || !this.ures(var10);
                              }

                              var45 = var44 + 1;
                           } catch (Exception var35) {
                              Tools.eLog(var35, 0);
                           }
                        } else if (var30.startsWith("R")) {
                           var4[var16].append(this.getSzamlalo(var30, var5[var16]));
                        } else if (var30.startsWith("H")) {
                           var4[var16].append(this.getSzamlalo(var30, this.hivatkozasSzamlalo));
                        } else if (var30.startsWith("T")) {
                           var4[var16].append(this.getSzamlalo(var30, var6[var16]));
                        }
                     }

                     if (var16 > 0 && this.ures(var4[var16].substring(var28))) {
                        var27 = false;
                     }

                     if ((var17 <= this.maxDidKodSzamLimit || !this.ures(var9)) && var27) {
                        this.ujsor(var4[var16]);
                        int var10002 = var6[var16]++;
                        var10002 = var5[var16]++;
                     } else {
                        var4[var16].delete(var8, var4[var16].length());
                     }
                  }

                  if (var14 > 1) {
                     this.ujsor(var4[var16]);
                  }
               }
            }

            this.ujsor(var4[var16]);
         }

         ++this.hivatkozasSzamlalo;
      }

      String var37 = null;
      if (((String)this.adoszam.getSelectedItem()).length() == 11) {
         var37 = Kontroll.kontrollPath + File.separator + ((String)this.adoszam.getSelectedItem()).substring(0, 8);
      } else {
         var37 = Kontroll.kontrollPath + File.separator + this.adoszam.getSelectedItem();
      }

      try {
         var37 = var37 + "." + var7[0];
      } catch (Exception var33) {
         Tools.eLog(var33, 0);
      }

      boolean var38 = true;
      if ((new File(var37)).exists()) {
         var38 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Ilyen nevű kontroll állomány már létezik a\n" + Kontroll.kontrollPath + " mappában.\nFelülírja ?", "Kontroll állományok létrehozása", 0, 3, (Icon)null, Kontroll.igenNem, Kontroll.igenNem[0]) == 0;
      }

      if (var38) {
         for(int var39 = 0; var39 < var1.length; ++var39) {
            var6[var39] = 1;
            if (var39 > 0 && var5[var39] == 1) {
               var11[var39] = null;
            } else {
               if (((String)this.adoszam.getSelectedItem()).length() == 11) {
                  var11[var39] = Kontroll.kontrollPath + File.separator + ((String)this.adoszam.getSelectedItem()).substring(0, 8);
               } else {
                  var11[var39] = Kontroll.kontrollPath + File.separator + this.adoszam.getSelectedItem();
               }

               try {
                  FileOutputStream var41 = new FileOutputStream(var11[var39] + "." + var7[var39]);
                  var41.write(this.ansiToOem(var4[var39].toString()));
                  var41.close();
               } catch (IOException var32) {
                  var32.printStackTrace();
               }
            }
         }

         this.writeKifFile(var11, var7, var5[0]);
         this.writeLogFile(var11, var7, var5);
         String var40 = "A kontroll állomány";
         if (var11.length > 1) {
            var40 = var40 + "(ok)";
         }

         String var42 = "";

         for(var17 = 0; var17 < var11.length; ++var17) {
            var42 = var42 + (var11[var17] != null ? var11[var17] + "." + var7[var17] + "   Rekordszám : " + (var5[var17] - 1) + "\n" : "");
         }

         var40 = var40 + " létrehozása véget ért !\nFájl" + (var11.length > 1 ? "(ok)" : "") + " :\n" + var42;
         GuiUtil.showMessageDialog(MainFrame.thisinstance, var40, "Kontroll állományok létrehozása", 1);
      }

   }

   private Hashtable fillVV(Vector[] var1) {
      Hashtable var2 = new Hashtable();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         Vector var4 = var1[var3];
         int var5 = this.getMaxDidkodSzam(var4) - 4;

         for(int var6 = 0; var6 < var4.size(); ++var6) {
            String[] var7 = ((String)var4.elementAt(var6)).split(",");
            if (var7.length >= 3 && var7.length < var5) {
               String var8 = this.fillStr(var7, var5);
               var4.set(var6, var8);
               var2.put(var6, true);
            }
         }
      }

      return var2;
   }

   private String fillStr(String[] var1, int var2) {
      String var3 = var1[0] + "," + var1[1];

      for(int var4 = 2; var4 < var1.length; ++var4) {
         String var5 = var1[var4];
         int var6 = var2 / (var1.length - 2);

         for(int var7 = 0; var7 < var6; ++var7) {
            var3 = var3 + "," + var5;
         }
      }

      return var3;
   }
}
