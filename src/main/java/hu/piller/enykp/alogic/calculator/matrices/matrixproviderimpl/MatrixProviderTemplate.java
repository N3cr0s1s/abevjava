package hu.piller.enykp.alogic.calculator.matrices.matrixproviderimpl;

import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.calculator.matrices.IMatrixProvider;
import hu.piller.enykp.alogic.calculator.matrices.MREF;
import hu.piller.enykp.alogic.calculator.matrices.MatrixMeta;

public class MatrixProviderTemplate implements IMatrixProvider {
   public Object[] getMatrix(MREF var1) {
      return (Object[])((Object[])Calculator.getInstance().getExpStore(var1.getFormID()).getMatrixItem(var1.getMatrixID(), (int[])null));
   }

   public MatrixMeta getMatrixMeta(MREF var1) {
      return null;
   }
}
