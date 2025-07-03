package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.interfaces.IPropertyList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectReplacer implements Serializable, IPropertyList {
   public static final String OBJ_CALCULATOR = "calculator";
   public static final String OBJ_GUI = "gui_manager";
   public static final String OBJ_FV_SET = "function_set";
   protected String object_key;
   protected Object tmp_object;

   ObjectReplacer() {
   }

   ObjectReplacer(String var1, Object var2) {
      this.setObjectKey(var1, var2);
   }

   public void setObjectKey(String var1, Object var2) {
      this.object_key = var1;
      this.tmp_object = var2;
   }

   public String getObjectKey() {
      return this.object_key;
   }

   public Object getObject() {
      return this.tmp_object;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeUTF(this.object_key);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.object_key = var1.readUTF();
   }

   public boolean set(Object var1, Object var2) {
      return false;
   }

   public Object get(Object var1) {
      return null;
   }
}
