package hu.piller.enykp.gui.component;

import hu.piller.enykp.interfaces.IDataField;
import java.util.Hashtable;
import javax.swing.JTextField;

public class DF_TextField extends JTextField implements IDataField {
   public void setValue(Object var1) {
      this.setText(var1.toString());
   }

   public void setFeatures(Hashtable var1) {
   }

   public Object getFieldValue() {
      return this.getText();
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
