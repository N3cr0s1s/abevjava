package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.cachedExp;

import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.ABEVFeaturedBaseFunctions;
import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.ABEVFunctionSet;
import hu.piller.enykp.alogic.calculator.calculator_c.ExpClass;
import hu.piller.enykp.alogic.calculator.calculator_c.ExpFactory;
import java.util.Vector;

public class CachedGlobsum extends DefaultCachedItem {
   ExpClass expPar;

   public CachedGlobsum(String var1, Object[] var2) {
      this.function = var1;
      this.formid = ((String)var2[1]).replace('"', ' ').trim();
      this.parameter = (String)var2[2];

      try {
         this.expPar = ExpFactory.createExp(this.parameter == null ? null : this.parameter.toString(), (String)null, -1, false, (String)null);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      if (this.expPar != null) {
         this.parameter = ABEVFunctionSet.getExpString(this.expPar);
         this.init();
      }
   }

   public void init() {
      this.result = new Object[]{null, new Integer(0), true};
      this.errors = new Vector(10, 10000);
   }

   public void releaseTmpData() {
   }

   public void exec() {
      this.result[0] = ABEVFeaturedBaseFunctions.globsumBody(this.expPar, this.result, this.errors);
   }
}
