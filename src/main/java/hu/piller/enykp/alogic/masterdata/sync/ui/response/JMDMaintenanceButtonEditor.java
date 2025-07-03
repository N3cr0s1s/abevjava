package hu.piller.enykp.alogic.masterdata.sync.ui.response;

import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.JMDMaintenanceForm;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;

public class JMDMaintenanceButtonEditor extends DefaultCellEditor {
   protected JButton button = new JButton();
   private String label;
   private boolean isPushed;
   private String curId;
   private int row;
   private JMDResponseDialog responseForm;

   public JMDMaintenanceButtonEditor(JCheckBox var1, JMDResponseDialog var2) {
      super(var1);
      this.button.setOpaque(true);
      this.button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            JMDMaintenanceButtonEditor.this.fireEditingStopped();
         }
      });
      this.responseForm = var2;
   }

   public Component getTableCellEditorComponent(JTable var1, Object var2, boolean var3, int var4, int var5) {
      if (!"".equals(var2) && !"nincs eltérés".equals(var2)) {
         if (var3) {
            this.button.setForeground(var1.getSelectionForeground());
            this.button.setBackground(var1.getSelectionBackground());
         } else {
            this.button.setForeground(var1.getForeground());
            this.button.setBackground(var1.getBackground());
         }

         this.curId = (String)var1.getModel().getValueAt(var4, 3);
         this.label = var2 == null ? "" : var2.toString();
         this.button.setText(this.label);
         this.isPushed = true;
         this.row = var4;
         return this.button;
      } else {
         this.label = (String)var2;
         JLabel var6 = new JLabel();
         var6.setText(this.label);
         var6.setOpaque(true);
         if (var3) {
            var6.setForeground(var1.getSelectionForeground());
            var6.setBackground(UIManager.getColor("Table.selectionBackground"));
         } else {
            var6.setForeground(var1.getForeground());
            var6.setBackground(UIManager.getColor("Table.selectionBackground"));
         }

         return var6;
      }
   }

   public Object getCellEditorValue() {
      if (this.isPushed) {
         new JMDMaintenanceForm(this.curId, this.responseForm, this.row);
      }

      this.isPushed = false;
      return new String(this.label);
   }

   public boolean stopCellEditing() {
      this.isPushed = false;
      return super.stopCellEditing();
   }
}
