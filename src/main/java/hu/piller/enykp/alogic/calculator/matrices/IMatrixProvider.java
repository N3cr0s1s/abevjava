package hu.piller.enykp.alogic.calculator.matrices;

public interface IMatrixProvider {
   Object[] getMatrix(MREF var1);

   MatrixMeta getMatrixMeta(MREF var1);
}
