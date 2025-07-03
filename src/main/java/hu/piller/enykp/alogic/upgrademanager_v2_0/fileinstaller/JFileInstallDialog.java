package hu.piller.enykp.alogic.upgrademanager_v2_0.fileinstaller;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class JFileInstallDialog extends JDialog {
   static final boolean MODAL = true;
   private JButton button;
   private JTable table;

   public JFileInstallDialog(Frame var1) {
      super(var1, true);
      this.init();
   }

   private void init() {
      this.setTitle("Telepítés telepítőcsomagból");
      this.setResizable(true);
      this.setModal(true);
      this.setLocationRelativeTo(this.getOwner());
      this.setLocation(new Point(this.getLocation().x - 160, this.getLocation().y - 150));
      this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), 1));
      JPanel var1 = new JPanel();
      var1.setLayout(new BoxLayout(var1, 1));
      var1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Státusz"));
      JTable var2 = this.getJTable();
      JScrollPane var3 = new JScrollPane(var2, 20, 31);
      Dimension var4 = new Dimension(GuiUtil.getW("WWTelepítőcsomagWWWWStátuszWW"), 10 * (GuiUtil.getCommonItemHeight() + 2));
      var3.setPreferredSize(var4);
      var3.setVerticalScrollBarPolicy(20);
      var1.add(var3);
      JPanel var5 = new JPanel();
      var5.setLayout(new BoxLayout(var5, 0));
      var5.add(Box.createHorizontalGlue());
      var5.add(this.getButton());
      var5.add(Box.createHorizontalGlue());
      this.getContentPane().add(var1);
      this.getContentPane().add(Box.createVerticalStrut(5));
      this.getContentPane().add(var5);
      this.getContentPane().add(Box.createVerticalStrut(5));
      this.pack();
   }

   public JTable getJTable() {
      if (this.table == null) {
         DefaultTableModel var1 = new DefaultTableModel();
         this.table = new JTable(var1);
         if (GuiUtil.modGui()) {
            this.table.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
         }
      }

      return this.table;
   }

   public JButton getButton() {
      if (this.button == null) {
         this.button = new JButton("Bezár");
         this.button.setActionCommand("close");
         this.button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               JFileInstallDialog.this.dispose();
            }
         });
         this.button.setEnabled(false);
      }

      return this.button;
   }

   public void setFiles(File[] var1) {
      DefaultTableModel var2 = (DefaultTableModel)this.getJTable().getModel();
      Object[][] var3 = new Object[var1.length][];

      for(int var4 = 0; var4 < var1.length; ++var4) {
         Object[] var5 = new Object[]{var1[var4].getName(), "Telepítésre vár"};
         var3[var4] = var5;
      }

      var2.setDataVector(var3, new Object[]{"Telepítőcsomag", "Státusz"});
      GuiUtil.setTableColWidth(this.table);
   }

   public void setStatus(File var1, FileInstallStatus var2) {
      DefaultTableModel var3 = (DefaultTableModel)this.getJTable().getModel();

      for(int var4 = 0; var4 < var3.getRowCount(); ++var4) {
         if (var3.getValueAt(var4, 0).equals(var1.getName())) {
            switch(var2) {
            case TELEPITES_ALATT:
               var3.setValueAt("Telepítés alatt", var4, 1);
               this.getJTable().getSelectionModel().setSelectionInterval(var4, var4);
               break;
            case SIKER:
               var3.setValueAt("Telepítve", var4, 1);
               break;
            case HIBA:
               var3.setValueAt("Telepítési hiba", var4, 1);
            }

            var3.fireTableCellUpdated(var4, 1);
            this.getJTable().scrollRectToVisible(this.getJTable().getCellRect(var4, 0, true));
            break;
         }
      }

   }

   public void endOfInstall(boolean var1) {
      final StringBuilder var2 = new StringBuilder("A telepítés befejeződött!");
      if (var1) {
         var2.append("\nAz új keretrendszer használatához az ÁNyK újraindítása szükséges.");
      }

      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, var2, "Figyelmeztetés", 1);
         }
      });
      this.getButton().setEnabled(true);
   }
}
