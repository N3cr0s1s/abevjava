package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.IFunctionSet;
import hu.piller.enykp.interfaces.IPropertyList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class FnFactory {
   public static final Vector fn_bags = new Vector(32, 32);

   public static void addFunctionBag(IFunctionSet var0) {
      if (!fn_bags.contains(var0)) {
         fn_bags.add(var0);
      }

   }

   public static IPropertyList createFunctionList() {
      Hashtable var3 = new Hashtable(256);
      Iterator var2 = fn_bags.iterator();

      while(true) {
         IFunctionSet var0;
         Object[] var1;
         do {
            if (!var2.hasNext()) {
               return new Functions(var3);
            }

            var0 = (IFunctionSet)var2.next();
         } while((var1 = var0.getFunctionList()) == null);

         int var4 = 0;

         for(int var5 = var1.length; var4 < var5; ++var4) {
            var3.put(((String)var1[var4]).toLowerCase(), var0);
         }
      }
   }
}
