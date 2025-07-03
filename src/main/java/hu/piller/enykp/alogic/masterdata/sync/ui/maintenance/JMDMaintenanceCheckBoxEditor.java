package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

class JMDMaintenanceCheckBoxEditor extends DefaultCellEditor implements ItemListener {
   private JCheckBox checkBox;

   public JMDMaintenanceCheckBoxEditor(JCheckBox var1) {
      super(var1);
      this.checkBox = var1;
      this.checkBox.setOpaque(true);
      this.checkBox.setHorizontalAlignment(0);
      this.checkBox.setBorderPainted(true);
   }

   public Component getTableCellEditorComponent(JTable var1, Object var2, boolean var3, int var4, int var5) {
      if (var2 != null && this.checkBox != null) {
         this.checkBox.addItemListener(this);
         if (((MDMaintenanceTableModel)var1.getModel()).hasDifferentValues(var4)) {
            this.checkBox.setBackground(Colors.HIGHLITED);
         } else {
            this.checkBox.setBackground(var1.getBackground());
         }

         if ((Boolean)var2) {
            this.checkBox.setSelected(true);
         } else {
            this.checkBox.setSelected(false);
         }

         return this.checkBox;
      } else {
         return null;
      }
   }

   public Object getCellEditorValue() {
      return this.checkBox.isSelected() ? new Boolean(true) : new Boolean(false);
   }

   public void itemStateChanged(ItemEvent var1) {
      super.fireEditingStopped();
   }
}
