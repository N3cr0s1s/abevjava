package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.cachedExp;

import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.FunctionBodies;
import java.util.Hashtable;
import java.util.Vector;

public class CachedUniquem extends DefaultCachedItem {
   Hashtable tempdata;

   public CachedUniquem(String var1, Object[] var2) {
      this.function = var1;
      this.formid = ((String)var2[1]).replace('"', ' ').trim();
      this.parameter = ((String)var2[2]).replace('"', ' ').trim();
      this.init();
   }

   public void init() {
      this.result = new Object[]{Boolean.TRUE, new Integer(4)};
      this.errors = new Vector(10, 10000);
      this.tempdata = new Hashtable();
   }

   public void releaseTmpData() {
      this.tempdata = null;
   }

   public void exec() {
      if (!FunctionBodies.uniquemBody(this.formid, this.parameter, this.tempdata, this.errors)) {
         this.result[0] = Boolean.FALSE;
      }

   }
}
