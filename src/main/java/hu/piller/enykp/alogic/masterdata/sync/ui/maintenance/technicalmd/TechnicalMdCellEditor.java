package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd;

import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.Colors;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.MDMaintenanceTableModel;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.EventObject;
import java.util.Hashtable;
import javax.swing.AbstractCellEditor;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellEditor;

public class TechnicalMdCellEditor extends AbstractCellEditor implements TableCellEditor, WindowListener {
   private JLabel value;
   private Border outside;
   private Border inside;
   private Border highlight;

   public TechnicalMdCellEditor() {
      this.outside = new MatteBorder(1, 0, 1, 0, Color.BLUE);
      this.inside = new EmptyBorder(1, 0, 1, 0);
      this.highlight = new CompoundBorder(this.outside, this.inside);
      this.value = new JLabel("");
   }

   public Object getCellEditorValue() {
      return this.value.getText();
   }

   public Component getTableCellEditorComponent(JTable var1, Object var2, boolean var3, int var4, int var5) {
      if (var3) {
         String var6 = (String)var1.getModel().getValueAt(var4, 0);
         ITechnicalMdModel var7 = TechnicalMdModelFactory.getInstance().getDataModelForTechnicalMasterData(var6);

         try {
            var7.setValue((String)var2);
         } catch (IllegalArgumentException var9) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "Érvénytelen érték: " + var2 + "\n" + var9.getMessage(), "Hiba", 0);
            var7.setValue("");
         }

         final JDialog var8 = TechnicalMdEditorFactory.getInstance().getEditorDialogForTechnicalMasterData(var6);
         var8.setTitle(var6 + " adatai");
         var8.addWindowListener(this);
         ((ITechnicalMdEditor)var8).setModel(var7);
         this.value.setFont((new Font(this.value.getFont().getName(), 0, 12)).deriveFont(new Hashtable()));
         this.value.setText("Töltse ki a " + var6.toLowerCase() + " adatait!");
         this.value.setBorder(this.highlight);
         this.value.setOpaque(true);
         this.value.setBackground(Colors.EDITING);
         this.value.setForeground(Color.BLACK);
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               var8.pack();
               var8.setVisible(true);
            }
         });
      } else {
         this.value.setOpaque(true);
         if (((MDMaintenanceTableModel)var1.getModel()).hasDifferentValues(var4)) {
            this.value.setBackground(Colors.HIGHLITED);
            this.value.setForeground(Color.BLACK);
         } else {
            this.value.setBackground(var1.getBackground());
            this.value.setForeground(Color.BLACK);
         }

         this.value.setBorder(this.highlight);
         this.value.setFont((new Font(this.value.getFont().getName(), 0, 12)).deriveFont(new Hashtable()));
         this.value.setText((String)var1.getModel().getValueAt(var4, var5));
      }

      return this.value;
   }

   public boolean isCellEditable(EventObject var1) {
      if (var1 instanceof MouseEvent) {
         return ((MouseEvent)var1).getClickCount() >= 2;
      } else {
         return true;
      }
   }

   public void windowOpened(WindowEvent var1) {
   }

   public void windowClosing(WindowEvent var1) {
   }

   public void windowClosed(WindowEvent var1) {
      this.value.setText(((ITechnicalMdEditor)var1.getSource()).getModel().getValue());
      ((JDialog)var1.getSource()).removeWindowListener(this);
      ((JDialog)var1.getSource()).removeAll();
      super.fireEditingStopped();
   }

   public void windowIconified(WindowEvent var1) {
   }

   public void windowDeiconified(WindowEvent var1) {
   }

   public void windowActivated(WindowEvent var1) {
   }

   public void windowDeactivated(WindowEvent var1) {
   }
}
