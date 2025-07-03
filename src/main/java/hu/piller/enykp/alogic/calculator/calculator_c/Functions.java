package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.IFunctionSet;
import hu.piller.enykp.interfaces.IPropertyList;
import java.util.Hashtable;
import java.util.Iterator;

public class Functions implements IPropertyList {
   private Hashtable functions;

   public Functions(Hashtable var1) {
      this.functions = var1;
   }

   public boolean set(Object var1, Object var2) {
      return false;
   }

   public Object get(Object var1) {
      if (var1 != null) {
         var1 = var1.toString().toLowerCase();
      }

      IFunctionSet var2 = (IFunctionSet)this.functions.get(var1);
      return var2 != null ? var2.getFunctionDescriptors((String)var1) : null;
   }

   public Iterator getIterator() {
      return this.functions != null ? this.functions.entrySet().iterator() : null;
   }
}
