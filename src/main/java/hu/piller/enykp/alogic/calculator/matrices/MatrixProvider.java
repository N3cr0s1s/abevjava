package hu.piller.enykp.alogic.calculator.matrices;

import hu.piller.enykp.alogic.calculator.matrices.matrixproviderimpl.MatrixProviderAltalanos;
import hu.piller.enykp.alogic.calculator.matrices.matrixproviderimpl.MatrixProviderOrganization;
import hu.piller.enykp.alogic.calculator.matrices.matrixproviderimpl.MatrixProviderTemplate;

public class MatrixProvider implements IMatrixProvider {
   private static MatrixProvider provider;

   public static synchronized IMatrixProvider getInstance() {
      if (provider == null) {
         provider = new MatrixProvider();
      }

      return provider;
   }

   private MatrixProvider() {
   }

   public Object[] getMatrix(MREF var1) {
      Object[] var2 = this.getMatrixProviderByScope(var1.getScope()).getMatrix(var1);
      return var2;
   }

   public MatrixMeta getMatrixMeta(MREF var1) {
      MatrixMeta var2 = this.getMatrixProviderByScope(var1.getScope()).getMatrixMeta(var1);
      return var2;
   }

   public IMatrixProvider getMatrixProviderByScope(String var1) {
      Object var2;
      if (var1.equals(MREF.NS_BELSO)) {
         var2 = new MatrixProviderTemplate();
      } else if (var1.equals(MREF.NS_ALTALANOS)) {
         var2 = new MatrixProviderAltalanos();
      } else {
         var2 = new MatrixProviderOrganization();
      }

      return (IMatrixProvider)var2;
   }
}
