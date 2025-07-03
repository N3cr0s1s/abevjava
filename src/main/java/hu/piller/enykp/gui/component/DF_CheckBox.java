package hu.piller.enykp.gui.component;

import hu.piller.enykp.interfaces.IDataField;
import java.util.Hashtable;
import javax.swing.JCheckBox;

public class DF_CheckBox extends JCheckBox implements IDataField {
   public void setValue(Object var1) {
      boolean var2 = false;
      if (var1 instanceof Boolean) {
         var2 = (Boolean)var1;
      }

      this.setSelected(var2);
   }

   public void setFeatures(Hashtable var1) {
   }

   public Object getFieldValue() {
      return new Boolean(this.isSelected());
   }

   public Object getFieldValueWOMask() {
      return this.getFieldValue();
   }

   public void setZoom(int var1) {
   }

   public int getRecordIndex() {
      return -1;
   }
}
