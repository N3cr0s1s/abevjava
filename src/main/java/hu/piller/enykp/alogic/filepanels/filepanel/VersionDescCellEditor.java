package hu.piller.enykp.alogic.filepanels.filepanel;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import java.awt.Component;
import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

public class VersionDescCellEditor implements TableCellEditor {
   public Component getTableCellEditorComponent(final JTable var1, Object var2, boolean var3, int var4, int var5) {
      final String var6 = (String)var1.getModel().getValueAt(var4, 12);
      if (var6 != null && !"".equals(var6)) {
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, var6, "Verzióváltás oka", 1);
               var1.invalidate();
            }
         });
      }

      return null;
   }

   public Object getCellEditorValue() {
      return null;
   }

   public boolean isCellEditable(EventObject var1) {
      return true;
   }

   public boolean shouldSelectCell(EventObject var1) {
      return false;
   }

   public boolean stopCellEditing() {
      return false;
   }

   public void cancelCellEditing() {
   }

   public void addCellEditorListener(CellEditorListener var1) {
   }

   public void removeCellEditorListener(CellEditorListener var1) {
   }
}
