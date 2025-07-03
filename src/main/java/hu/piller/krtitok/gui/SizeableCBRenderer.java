package hu.piller.krtitok.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class SizeableCBRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
   private JCheckBox checkBox = new JCheckBox();

   public SizeableCBRenderer() {
      this.checkBox.setIcon(FKriptodsk.getCheckboxIkonUres());
      this.checkBox.setSelectedIcon(FKriptodsk.getCheckboxIkonTeli());
      this.checkBox.addActionListener(this);
      this.checkBox.setOpaque(false);
   }

   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      this.checkBox.setHorizontalAlignment(0);
      this.checkBox.setSelected(Boolean.TRUE.equals(value));
      return this.checkBox;
   }

   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      this.checkBox.setSelected(Boolean.TRUE.equals(value));
      this.checkBox.setHorizontalAlignment(0);
      return this.checkBox;
   }

   public void actionPerformed(ActionEvent e) {
      this.stopCellEditing();
   }

   public Object getCellEditorValue() {
      return this.checkBox.isSelected();
   }
}
