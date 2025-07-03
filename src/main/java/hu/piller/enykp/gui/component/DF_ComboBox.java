package hu.piller.enykp.gui.component;

import hu.piller.enykp.interfaces.IDataField;
import java.util.Hashtable;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class DF_ComboBox extends JComboBox implements IDataField {
   public void setValue(Object var1) {
      this.setSelectedItem(var1);
   }

   public void setFeatures(Hashtable var1) {
      Object[] var2 = (Object[])((Object[])var1.get("spvalues"));
      this.setModel(new DefaultComboBoxModel(var2));
   }

   public Object getFieldValue() {
      return this.getSelectedItem();
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
