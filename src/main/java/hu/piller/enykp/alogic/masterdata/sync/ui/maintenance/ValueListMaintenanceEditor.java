package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance;

import hu.piller.enykp.gui.component.filtercombo.ENYKFilterComboPanel;
import hu.piller.enykp.gui.component.filtercombo.ENYKFilterComboStandard;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ValueListMaintenanceEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
   private List<String> valueList = new ArrayList();
   private String selectedValue;

   public ValueListMaintenanceEditor(List<String> var1) {
      this.valueList.addAll(var1);
   }

   public Object getCellEditorValue() {
      return this.selectedValue;
   }

   public Component getTableCellEditorComponent(JTable var1, Object var2, boolean var3, int var4, int var5) {
      this.selectedValue = (String)var2;
      ENYKFilterComboPanel var6 = new ENYKFilterComboPanel("West");
      var6.getCombo().LISTWIDTH = 350;
      var6.setEnabled(false);
      Vector var7 = new Vector();
      Vector var8 = new Vector();
      Iterator var9 = this.valueList.iterator();

      while(var9.hasNext()) {
         String var10 = (String)var9.next();
         var7.add(var10);
         var8.add(var10);
      }

      var6.setFeature(new Vector[]{var7, var8});
      var6.setText(this.selectedValue);
      var6.getCombo().addActionListener(this);
      if (var3) {
         var6.setBackground(Color.WHITE);
      } else {
         var6.setBackground(Color.WHITE);
      }

      return var6;
   }

   public void actionPerformed(ActionEvent var1) {
      this.selectedValue = ((ENYKFilterComboStandard)var1.getSource()).getText();
   }
}
