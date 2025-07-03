package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.interfaces.IPropertyList;
import java.util.Hashtable;

public class Variables implements IPropertyList {
   private Hashtable variables;
   private ExpWrapper ew = new ExpWrapper();

   public Variables(int var1) {
      this.variables = new Hashtable(var1);
   }

   public boolean set(Object var1, Object var2) {
      this.variables.put(((String)var1).toLowerCase().trim(), var2);
      return true;
   }

   public Object get(Object var1) {
      if (var1 instanceof String) {
         return this.variables.get(var1);
      } else if (var1 instanceof ExpClass) {
         ExpClass var3 = (ExpClass)var1;
         ExpWrapper var5 = this.ew;
         ExpClass var2 = (ExpClass)this.variables.get(var3.getIdentifier());
         var5.setExp(var2);
         var5.setCalcRecord((Object)null);
         Object var4 = var5.get("");
         var3.setType(var2.getType());
         var3.setResult(var2.getResult());
         return var4;
      } else {
         return null;
      }
   }

   public Hashtable getVariables() {
      return this.variables;
   }
}
