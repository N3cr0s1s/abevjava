package hu.piller.enykp.alogic.settingspanel.attachement;

import hu.piller.enykp.alogic.filepanels.attachement.EJFileChooser;
import hu.piller.enykp.alogic.kontroll.ReadOnlyTableModel;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.TableSorter;
import hu.piller.enykp.util.base.Tools;
import me.necrocore.abevjava.NecroFile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicFileChooserUI;

public class AttachementSettings extends JPanel {
   private Hashtable extsToPublic = new Hashtable();
   ReadOnlyTableModel extTableModel = new ReadOnlyTableModel(new Object[]{"kiterjesztés", "alkalmazás"}, 0);
   final TableSorter sorter;
   final JTable extTable;
   private Properties settings;
   final JTextField ext;
   final JTextField app;
   final JTextField zipTempDir;
   EJFileChooser fc;
   AttachementSettings.ExeFileFileter eff;

   public AttachementSettings() {
      this.sorter = new TableSorter(this.extTableModel);
      this.extTable = new JTable(this.sorter);
      this.settings = new Properties();
      this.ext = new JTextField();
      this.app = new JTextField();
      this.zipTempDir = new JTextField();
      this.fc = new EJFileChooser();
      this.eff = new AttachementSettings.ExeFileFileter();
      this.build();
   }

   private void build() {
      this.setLayout(new BorderLayout(5, 5));
      this.add(this.getTextFieldPanel(), "North");
      this.add(this.getListPanel(), "Center");
      this.load();
      this.fc.setMultiSelectionEnabled(false);
      this.fc.addChoosableFileFilter(this.eff);
      if (GuiUtil.modGui()) {
         this.extTable.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
      }

      this.extTable.addMouseListener(new MouseListener() {
         public void mouseClicked(MouseEvent var1) {
            int var2 = ((JTable)var1.getSource()).getSelectedRow();
            AttachementSettings.this.ext.setText((String)AttachementSettings.this.extTableModel.getValueAt(var2, 0));
            AttachementSettings.this.app.setText((String)AttachementSettings.this.extTableModel.getValueAt(var2, 1));
            AttachementSettings.this.app.setToolTipText(AttachementSettings.this.app.getText());
         }

         public void mouseEntered(MouseEvent var1) {
         }

         public void mouseExited(MouseEvent var1) {
         }

         public void mousePressed(MouseEvent var1) {
         }

         public void mouseReleased(MouseEvent var1) {
         }
      });
      AncestorListener var1 = new AncestorListener() {
         public void ancestorAdded(AncestorEvent var1) {
         }

         public void ancestorMoved(AncestorEvent var1) {
         }

         public void ancestorRemoved(AncestorEvent var1) {
            try {
               AttachementSettings.this.save();
            } catch (Exception var3) {
               var3.printStackTrace();
            }

         }
      };
      this.addAncestorListener(var1);
   }

   private JPanel getTextFieldPanel() {
      JPanel var1 = new JPanel();
      JPanel var2 = new JPanel(new FlowLayout(1, 0, 0));
      JButton var3 = new JButton("...");
      var3.setPreferredSize(new Dimension(GuiUtil.getPointsButtonWidth(), GuiUtil.getCommonItemHeight() + 4));
      var3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            AttachementSettings.this.fc.setFileSelectionMode(0);
            int var2 = AttachementSettings.this.fc.showOpenDialog(MainFrame.thisinstance);
            if (var2 == 0) {
               File var3 = AttachementSettings.this.fc.getSelectedFile();
               AttachementSettings.this.app.setText(var3.getAbsolutePath());
               AttachementSettings.this.app.setToolTipText(AttachementSettings.this.app.getText());
            }

         }
      });
      JButton var4 = new JButton("Hozzáad");
      var4.setMargin(new Insets(1, 1, 1, 1));
      var4.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            AttachementSettings.this.addToTable(AttachementSettings.this.ext.getText(), AttachementSettings.this.app.getText(), false);
         }
      });
      JLabel var5 = new JLabel("     ");
      this.ext.setSize(new Dimension(GuiUtil.getW("WWWWW"), GuiUtil.getCommonItemHeight() + 4));
      this.ext.setPreferredSize(this.ext.getSize());
      this.app.setSize(new Dimension(GuiUtil.getW("WWWWWWWWWWWWWWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 4));
      this.app.setPreferredSize(this.app.getSize());
      var4.setPreferredSize(new Dimension(GuiUtil.getW(var4, var4.getText()), GuiUtil.getCommonItemHeight() + 4));
      var2.add(new JLabel("Kiterjesztés : *."));
      var2.add(this.ext);
      var2.add(var5);
      var2.add(new JLabel("Alkalmazás : "));
      var2.add(this.app);
      var2.add(var3);
      var1.add(var2);
      var1.add(var4);
      return var1;
   }

   private JPanel getListPanel() {
      JPanel var1 = new JPanel(new BorderLayout(5, 5));
      this.extTable.setSelectionMode(0);
      this.sorter.setTableHeader(this.extTable.getTableHeader());
      JScrollPane var2 = new JScrollPane(this.extTable, 20, 30);
      JPanel var3 = new JPanel(new FlowLayout(0));
      var3.add(new JLabel("Jelenlegi hozzárendelések"));
      var1.add(var3, "North");
      var1.add(var2, "Center");
      int var4 = GuiUtil.getCommonItemHeight() + 6;
      byte var5 = 10;
      JLabel var6 = new JLabel("Amennyiben adatait hálózati meghajtón tárolja a csatolmányt is tartalmazó nyomtatvány feladása sokáig tarthat.");
      GuiUtil.setDynamicBound(var6, var6.getText(), 5, var5);
      int var14 = var5 + var4;
      JLabel var7 = new JLabel("Itt megadhat egy helyi mappát, amelyet a program ideiglenes tárolóként használ a feladáshoz.");
      GuiUtil.setDynamicBound(var7, var7.getText(), 5, var14);
      var14 += var4;
      JLabel var8 = new JLabel("Helyi mappa átmeneti tárolásra :");
      GuiUtil.setDynamicBound(var8, var8.getText(), 5, var14);
      JPanel var9 = new JPanel((LayoutManager)null);
      var9.setPreferredSize(new Dimension(GuiUtil.getW(var6, var6.getText()), 10 + 3 * (GuiUtil.getCommonItemHeight() + 6)));
      this.zipTempDir.setBounds(GuiUtil.getPositionFromPrevComponent(var8) + 5, var14, 180, GuiUtil.getCommonItemHeight() + 4);
      this.zipTempDir.setEditable(false);
      JButton var10 = new JButton("...");
      var10.setBounds(GuiUtil.getPositionFromPrevComponent(this.zipTempDir) + 5, var14, GuiUtil.getPointsButtonWidth(), GuiUtil.getCommonItemHeight() + 4);
      JButton var11 = new JButton("Törlés");
      var11.setBounds(GuiUtil.getPositionFromPrevComponent(var10) + 5, var14, GuiUtil.getW(var11, var11.getText()), GuiUtil.getCommonItemHeight() + 4);
      var11.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            AttachementSettings.this.zipTempDir.setText("");
         }
      });
      var10.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            AttachementSettings.this.initDialog("Válassza ki az átmeneti tároló könyvtárat!", AttachementSettings.this.zipTempDir.getText());
            AttachementSettings.this.fc.setFileSelectionMode(1);
            int var2 = AttachementSettings.this.fc.showOpenDialog(MainFrame.thisinstance);
            if (var2 == 0) {
               AttachementSettings.this.zipTempDir.setText(AttachementSettings.this.fc.getSelectedFile().getAbsolutePath());
            }

         }
      });
      var9.add(var6);
      var9.add(var7);
      var9.add(var8);
      var9.add(this.zipTempDir);
      var9.add(var10);
      var9.add(var11);
      var1.add(var9, "South");
      JPanel var12 = new JPanel();
      JButton var13 = new JButton("Töröl");
      var13.setMargin(new Insets(2, 1, 2, 1));
      var13.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            AttachementSettings.this.doDel();
         }
      });
      var12.add(var13);
      var1.add(var12, "East");
      var1.setPreferredSize(new Dimension((int)var6.getBounds().getWidth(), 4 * (GuiUtil.getCommonItemHeight() + 2)));
      var1.setMinimumSize(new Dimension((int)var6.getBounds().getWidth(), 4 * (GuiUtil.getCommonItemHeight() + 2)));
      var1.setSize(new Dimension((int)var6.getBounds().getWidth(), 4 * (GuiUtil.getCommonItemHeight() + 2)));
      return var1;
   }

   private JPanel getOkPanel() {
      JPanel var1 = new JPanel();
      JButton var2 = new JButton("Hozzárendelések mentése");
      var2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            AttachementSettings.this.save();
         }
      });
      var1.add(var2);
      return var1;
   }

   private void prepare() {
      AncestorListener var1 = new AncestorListener() {
         public void ancestorAdded(AncestorEvent var1) {
         }

         public void ancestorMoved(AncestorEvent var1) {
         }

         public void ancestorRemoved(AncestorEvent var1) {
            try {
               AttachementSettings.this.save();
            } catch (Exception var3) {
               var3.printStackTrace();
            }

         }
      };
      this.addAncestorListener(var1);
      this.load();
   }

   public void save() {
      SettingsStore var1 = SettingsStore.getInstance();
      var1.set("attachments", this.extsToPublic);
      var1.set("attachments", "attachment.temporary.directory", this.zipTempDir.getText());
      var1.save();
   }

   public void load() {
      SettingsStore var1 = SettingsStore.getInstance();
      this.extsToPublic.clear();

      for(int var2 = 0; var2 < this.extTableModel.getRowCount(); ++var2) {
         this.extTableModel.removeRow(var2);
      }

      Hashtable var6 = var1.get("attachments");
      if (var6 != null) {
         try {
            this.zipTempDir.setText((String)var6.remove("attachment.temporary.directory"));
         } catch (Exception var5) {
            this.zipTempDir.setText("");
         }

         Enumeration var3 = var6.keys();

         while(var3.hasMoreElements()) {
            String var4 = (String)var3.nextElement();
            this.addToTable(var4, (String)var6.get(var4), true);
         }

      }
   }

   private void addToTable(String var1, String var2, boolean var3) {
      if (!var1.equals("") && !var2.equals("")) {
         var1 = var1.toLowerCase();
         if (this.extsToPublic.containsKey(var1)) {
            if (var3) {
               return;
            }

            if (JOptionPane.showOptionDialog(this, "Ez a kiterjesztés már szerepel a listában. Felülírja ?", "Kérdés", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 1) {
               return;
            }

            int var4 = this.findkey(var1);
            this.extTableModel.removeRow(var4);
         }

         this.extsToPublic.put(var1.toLowerCase(), var2);
         Vector var5 = new Vector(2);
         var5.add(var1.toLowerCase());
         var5.add(var2);
         this.extTableModel.addRow(var5);
      } else {
         if (!var3) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "Üres érték!", "Hiba", 0);
         }

      }
   }

   private int findkey(String var1) {
      boolean var2 = false;

      int var3;
      for(var3 = 0; !var2 && var3 < this.extTable.getRowCount(); ++var3) {
         var2 = this.extTableModel.getValueAt(var3, 0).equals(var1);
      }

      return var3 - 1;
   }

   private void doDel() {
      if (this.extTable.getSelectedRows().length > 0) {
         if (JOptionPane.showOptionDialog(this, "Biztosan törli a kijelölt fájlokat a listából?", "Kérdés", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0) {
            for(int var1 = this.extTable.getSelectedRows().length; var1 > 0; --var1) {
               this.extsToPublic.remove(this.extTableModel.getValueAt(this.extTable.getSelectedRows()[var1 - 1], 0));
               this.extTableModel.removeRow(this.extTable.getSelectedRows()[var1 - 1]);
            }
         }
      } else {
         GuiUtil.showMessageDialog(this, "Nincs kijelölt fájl.", "Beállítások", 1);
      }

   }

   private void saveProps() {
      this.settings.clear();
      Enumeration var1 = this.extsToPublic.keys();

      while(var1.hasMoreElements()) {
         Object var2 = var1.nextElement();
         this.settings.setProperty((String)var2, this.extsToPublic.get(var2).toString());
      }

   }

   private void loadProps() {
      try {
         this.extsToPublic.clear();
         Enumeration var1 = this.settings.keys();

         while(var1.hasMoreElements()) {
            Object var2 = var1.nextElement();
            this.extsToPublic.put(var2, this.settings.get(var2));
         }

         this.copyFromHashtable();
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
      }

   }

   private void copyFromHashtable() {
      Enumeration var1 = this.extsToPublic.keys();

      while(var1.hasMoreElements()) {
         Object var2 = var1.nextElement();
         Vector var3 = new Vector();
         var3.add(var2);
         var3.add(this.extsToPublic.get(var2));
         this.extTableModel.addRow(var3);
      }

   }

   private void initDialog(String var1, String var2) {
      this.fc.setDialogTitle(var1);
      File var3 = new NecroFile(var2);
      if (var3.exists()) {
         if (var3.isDirectory()) {
            this.fc.setCurrentDirectory(var3);
         } else {
            this.fc.setCurrentDirectory(new NecroFile(var3.getParent()));
         }
      }

      try {
         ((BasicFileChooserUI)this.fc.getUI()).setFileName("");
      } catch (ClassCastException var7) {
         try {
            this.fc.setSelectedFile(new NecroFile(""));
         } catch (Exception var6) {
            Tools.eLog(var6, 0);
         }
      }

      this.fc.setSelectedFile((File)null);
   }

   private class ExeFileFileter extends FileFilter implements java.io.FileFilter {
      private ExeFileFileter() {
      }

      public boolean accept(File var1) {
         if (var1.isDirectory()) {
            return true;
         } else {
            return var1.isFile();
         }
      }

      public String getDescription() {
         return "alkalmazások";
      }

      // $FF: synthetic method
      ExeFileFileter(Object var2) {
         this();
      }
   }
}
