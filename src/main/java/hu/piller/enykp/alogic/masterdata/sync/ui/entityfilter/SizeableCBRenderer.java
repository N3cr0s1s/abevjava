package hu.piller.enykp.alogic.masterdata.sync.ui.entityfilter;

import hu.piller.enykp.gui.GuiUtil;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class SizeableCBRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
   private JPanel cellPanel = new JPanel(new BorderLayout(0, 0));
   private JCheckBox checkBox = GuiUtil.getANYKCheckBox();

   public SizeableCBRenderer() {
      this.checkBox.addActionListener(this);
      this.checkBox.setOpaque(false);
      this.cellPanel.add(this.checkBox, "Center");
   }

   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      this.checkBox.setHorizontalAlignment(0);
      this.checkBox.setSelected(Boolean.TRUE.equals(var2));
      this.cellPanel.setBackground(var1.getSelectedRow() == var5 ? var1.getSelectionBackground() : var1.getBackground());
      return this.cellPanel;
   }

   public Component getTableCellEditorComponent(JTable var1, Object var2, boolean var3, int var4, int var5) {
      this.checkBox.setSelected(Boolean.TRUE.equals(var2));
      this.checkBox.setHorizontalAlignment(0);
      return this.cellPanel;
   }

   public void actionPerformed(ActionEvent var1) {
      this.stopCellEditing();
   }

   public Object getCellEditorValue() {
      return this.checkBox.isSelected();
   }
}
