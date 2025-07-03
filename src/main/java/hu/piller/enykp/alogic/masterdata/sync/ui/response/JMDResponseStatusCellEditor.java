package hu.piller.enykp.alogic.masterdata.sync.ui.response;

import hu.piller.enykp.alogic.masterdata.sync.syncdir.SyncDirHandler;
import java.awt.Component;
import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

public class JMDResponseStatusCellEditor implements TableCellEditor {
   public Component getTableCellEditorComponent(final JTable var1, Object var2, boolean var3, final int var4, int var5) {
      String var6 = (String)var1.getModel().getValueAt(var4, 0);
      if (var6 != null && !"Siker".equals(var6)) {
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               String var1x = (String)var1.getModel().getValueAt(var4, 2);
               String var2 = (String)var1.getModel().getValueAt(var4, 3);
               String var3 = (String)var1.getModel().getValueAt(var4, 1);
               String var4x;
               if (!"Társaság".equals(var3) && !"Egyéni vállalkozó".equals(var3)) {
                  var4x = var2;
               } else {
                  var4x = var2.substring(0, 8);
               }

               String var5;
               try {
                  var5 = SyncDirHandler.getResultFileContent(var4x);
               } catch (Exception var7) {
                  var5 = var7.getMessage();
               }

               JMDResponseErrorStatusDialog var6 = new JMDResponseErrorStatusDialog(var1x, var2, var5);
               var6.pack();
               var6.setVisible(true);
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
