package hu.piller.enykp.alogic.kontroll;

import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.Tools;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicFileChooserUI;

public class MasolasForm extends JPanel implements ItemListener {
   private JComboBox nyomtatvany;
   private JComboBox adoszam;
   private JTable fileTable;
   private CBTableModel allData;
   private CBTableModel actualData;
   private static String celPath = null;
   private JFileChooser pathChooser = new JFileChooser();
   private BookModel iplGui;

   public void setIplGui(BookModel var1) {
      this.iplGui = var1;
   }

   public MasolasForm(final JDialog var1, CBTableModel var2) {
      var1.setTitle(var1.getTitle() + " másolása");
      HashSet var3 = new HashSet();
      HashSet var4 = new HashSet();

      for(int var5 = 0; var5 < var2.getRowCount(); ++var5) {
         var3.add(var2.getValueAt(var5, 2));
         var4.add(var2.getValueAt(var5, 1));
      }

      this.allData = var2;
      this.actualData = new CBTableModel();
      BevelBorder var24 = new BevelBorder(0);
      this.setBorder(var24);
      this.setLayout(new BorderLayout());
      this.setPreferredSize(new Dimension(700, 480));
      JPanel var6 = new JPanel((LayoutManager)null);
      Dimension var7 = new Dimension(690, 65);
      var6.setPreferredSize(var7);
      var6.setMinimumSize(var7);
      String var10 = "Kontroll nyomtatvány: ";
      String var11 = "Adószám: ";
      JLabel var8 = new JLabel(var10);
      int var12 = SwingUtilities.computeStringWidth(this.getFontMetrics(this.getFont()), var10) + 10;
      int var13 = SwingUtilities.computeStringWidth(this.getFontMetrics(this.getFont()), var11) + 10;
      short var14 = 200;
      var8.setBounds(10, 25, var12, 20);

      try {
         this.nyomtatvany = new JComboBox(var3.toArray());
         this.nyomtatvany.insertItemAt("", 0);
         this.nyomtatvany.setSelectedIndex(0);
         this.nyomtatvany.setBounds(var12 + 20, 25, var14, 20);
         this.nyomtatvany.addItemListener(this);
      } catch (Exception var23) {
         this.nyomtatvany = new JComboBox(new String[]{"Hiba a kvf fájl olvasásakor"});
         this.nyomtatvany.setEnabled(false);
      }

      JLabel var9 = new JLabel(var11);
      var9.setBounds(var12 + 60 + var14, 25, var13, 20);
      this.adoszam = new JComboBox(var4.toArray());
      this.adoszam.insertItemAt("", 0);
      this.adoszam.setSelectedIndex(0);
      this.adoszam.setBounds(70 + var12 + var14 + var13, 25, var14, 20);
      this.adoszam.addItemListener(this);
      var6.add(var8);
      var6.add(this.nyomtatvany);
      var6.add(var9);
      var6.add(this.adoszam);
      this.add(var6, "North");
      this.fileTable = new JTable(var2);
      this.fileTable.setShowGrid(true);
      this.fileTable.setAutoResizeMode(0);
      this.setColWidth();
      this.fileTable.setRowSelectionAllowed(true);
      this.fileTable.setModel(var2);
      JScrollPane var15 = new JScrollPane(this.fileTable, 20, 30);
      this.add(var15, "Center");
      JPanel var16 = new JPanel();
      if (celPath == null) {
         SettingsStore var17 = SettingsStore.getInstance();
         if (var17.get("gui", "kontroll_allomanyok_masolasa_cel") != null && !var17.get("gui", "kontroll_allomanyok_masolasa_cel").equals("")) {
            celPath = Tools.fillPath(var17.get("gui", "kontroll_allomanyok_masolasa_cel"));
            File var18 = new NecroFile(celPath);
            if (!var18.exists() || !var18.isDirectory()) {
               celPath = null;
            }
         }
      }

      JButton var25 = new JButton("Másolás");
      var25.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            try {
               if (MasolasForm.celPath == null) {
                  GuiUtil.showMessageDialog(MasolasForm.this, "Nincs kijelölt célmappa!", "Kontroll állományok másolása", 2);
               } else {
                  int var2 = MasolasForm.this.doPressedOkButton();
                  if (var2 > 0) {
                     GuiUtil.showMessageDialog(MasolasForm.this, var2 + " állomány másolása megtörtént a " + MasolasForm.celPath + " mappába.", "Kontroll állományok másolása", 1);
                  } else if (var2 > -1) {
                     GuiUtil.showMessageDialog(MasolasForm.this, "Nincs kijelölve nyomtatvány!", "Kontroll állományok másolása", 2);
                  }
               }
            } catch (FileNotFoundException var3) {
               ErrorList.getInstance().writeError(new Long(4001L), "Kontroll másolás hiba! ", var3, (Object)null);
               GuiUtil.showMessageDialog(MasolasForm.this, "Hiba a másoláskor! Valószínűleg a .kif fájlban hibás elérési út található\n" + var3.getMessage(), "Kontroll állományok másolása", 0);
            } catch (Exception var4) {
               ErrorList.getInstance().writeError(new Long(4001L), "Kontroll másolás hiba!", var4, (Object)null);
               GuiUtil.showMessageDialog(MasolasForm.this, "Hiba a másoláskor!", "Kontroll állományok másolása", 0);
            }

         }
      });
      var25.setBounds(240, 390, 100, 30);
      var16.add(var25);
      JButton var26 = new JButton("Mégsem");
      var26.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            try {
               var1.dispose();
            } catch (Exception var3) {
               var3.printStackTrace();
            }

         }
      });
      var26.setBounds(360, 390, 100, 30);
      var16.add(var26);
      final JLabel var19 = new JLabel();
      if (celPath != null) {
         var19.setText("Kijelölt célmappa : " + celPath);
      } else {
         var19.setText("Nincs kijelölt célmappa!");
      }

      JButton var20 = new JButton("Célmappa megadása...");
      var20.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            try {
               try {
                  ((BasicFileChooserUI)MasolasForm.this.pathChooser.getUI()).setFileName("kontroll_állományok_helye");
               } catch (ClassCastException var5) {
                  try {
                     MasolasForm.this.pathChooser.setSelectedFile(new NecroFile("kontroll_állományok_helye.txt"));
                  } catch (Exception var4) {
                     Tools.eLog(var4, 0);
                  }
               }

               MasolasForm.this.pathChooser.setDialogTitle("Válassza ki a kontroll állományok helyét");
               MasolasForm.this.pathChooser.setApproveButtonText("Kijelölés");
               MasolasForm.this.pathChooser.setApproveButtonToolTipText("Itt kejelölheti a célmappát!");
               int var2 = MasolasForm.this.pathChooser.showSaveDialog(MasolasForm.this);
               if (var2 == 0) {
                  MasolasForm.celPath = MasolasForm.this.pathChooser.getCurrentDirectory().getAbsolutePath();
                  if (!MasolasForm.celPath.endsWith("\\") && !MasolasForm.celPath.endsWith("/")) {
                     MasolasForm.celPath = MasolasForm.celPath + File.separator;
                  }

                  var19.setText("Kijelölt célmappa : " + MasolasForm.celPath);
                  SettingsStore.getInstance().set("gui", "kontroll_allomanyok_masolasa_cel", MasolasForm.celPath);
               }
            } catch (Exception var6) {
               var6.printStackTrace();
            }

         }
      });
      var20.setBounds(480, 390, 100, 30);
      var16.add(var20);
      JPanel var21 = new JPanel(new FlowLayout(0));
      var21.add(var19);
      JPanel var22 = new JPanel(new GridLayout(2, 1));
      var22.add(var16);
      var22.add(var21);
      var22.setPreferredSize(var7);
      this.add(var22, "South");
      this.setVisible(true);
   }

   public void itemStateChanged(ItemEvent var1) {
      this.changeTableModel();
   }

   public void changeTableModel() {
      while(this.actualData.getRowCount() > 0) {
         this.actualData.removeRow(0);
      }

      for(int var1 = 0; var1 < this.allData.getRowCount(); ++var1) {
         if (((String)this.allData.getValueAt(var1, 1)).toLowerCase().startsWith(((String)this.adoszam.getSelectedItem()).toLowerCase()) && ((String)this.allData.getValueAt(var1, 2)).toLowerCase().startsWith(((String)this.nyomtatvany.getSelectedItem()).toLowerCase())) {
            this.actualData.addRow((Vector)this.allData.getDataVector().get(var1));
         }
      }

      this.fileTable.setModel(this.actualData);
      this.setColWidth();
   }

   private void setColWidth() {
      this.fileTable.getColumnModel().getColumn(0).setPreferredWidth(90);
      this.fileTable.getColumnModel().getColumn(1).setPreferredWidth(140);
      this.fileTable.getColumnModel().getColumn(2).setPreferredWidth(140);
      this.fileTable.getColumnModel().getColumn(3).setPreferredWidth(140);
      this.fileTable.getColumnModel().getColumn(4).setPreferredWidth(140);
      if (this.fileTable.getColumnModel().getColumnCount() > 5) {
         this.fileTable.getColumnModel().removeColumn(this.fileTable.getColumnModel().getColumn(5));
      }

   }

   private int doPressedOkButton() throws Exception {
      int var1 = 0;
      boolean var2 = true;

      for(int var3 = 0; var3 < this.fileTable.getModel().getRowCount(); ++var3) {
         if ((Boolean)this.fileTable.getModel().getValueAt(var3, 0)) {
            String[] var4 = (String[])((String[])this.fileTable.getModel().getValueAt(var3, 5));

            for(int var5 = 0; var5 < var4.length; ++var5) {
               try {
                  int var9 = this.copyFile(var4[var5], var5 == 0);
                  if (var9 != 0) {
                     return -1;
                  }

                  ++var1;
               } catch (FileNotFoundException var7) {
                  throw var7;
               } catch (Exception var8) {
                  throw new Exception("A másolás nem sikerült " + var4[var5]);
               }
            }

            this.fileTable.getModel().setValueAt(Boolean.FALSE, var3, 0);
         }
      }

      return var1;
   }

   private int copyFile(String var1, boolean var2) throws Exception {
      int var3 = 0;
      File var7 = new NecroFile(celPath + var1.substring(var1.lastIndexOf(File.separator) + 1));
      if (var7.exists()) {
         if (var2) {
            var3 = JOptionPane.showOptionDialog(this, var1.substring(var1.lastIndexOf(File.separator) + 1) + "\nIlyen nevű kontroll állomány már létezik a\n" + celPath + " mappában.\nFelülírja ?", "Kontroll állományok másolása", 1, 3, (Icon)null, Kontroll.igenNem, Kontroll.igenNem[0]);
         } else {
            var3 = 0;
         }
      }

      if (var3 != 0) {
         return var3;
      } else {
         FileInputStream var4 = new FileInputStream(var1);
         FileOutputStream var5 = new NecroFileOutputStream(celPath + var1.substring(var1.lastIndexOf(File.separator) + 1));
         byte[] var8 = new byte[2048];

         int var6;
         while((var6 = var4.read(var8)) != -1) {
            var5.write(var8, 0, var6);
         }

         try {
            var5.close();
         } catch (IOException var11) {
            Tools.eLog(var11, 0);
         }

         try {
            var4.close();
         } catch (IOException var10) {
            Tools.eLog(var10, 0);
         }

         return 0;
      }
   }
}
