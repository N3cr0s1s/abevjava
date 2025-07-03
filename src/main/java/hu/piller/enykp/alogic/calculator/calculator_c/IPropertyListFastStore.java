package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.interfaces.IPropertyList;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class IPropertyListFastStore extends Hashtable implements IPropertyList, Serializable {
   public IPropertyListFastStore() {
   }

   public IPropertyListFastStore(int var1) {
      super(var1);
   }

   public IPropertyListFastStore(int var1, float var2) {
      super(var1, var2);
   }

   public IPropertyListFastStore(Map var1) {
      super(var1);
   }

   public boolean set(Object var1, Object var2) {
      if (var1 != null && var2 != null) {
         this.put(var1, var2);
         return true;
      } else {
         return false;
      }
   }
}
