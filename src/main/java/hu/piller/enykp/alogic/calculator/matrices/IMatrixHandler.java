package hu.piller.enykp.alogic.calculator.matrices;

import hu.piller.enykp.alogic.calculator.calculator_c.MatrixSearchModel;
import java.util.Vector;

public interface IMatrixHandler {
   Vector search(String var1, MatrixSearchModel var2, boolean var3, boolean var4);

   boolean isMatrixExists(String var1, String var2);

   Object[] getMatrix(String var1, String var2) throws Exception;

   MatrixMeta getMatrixParameters(String var1, String var2);

   String createNameSpace(String var1, String var2);

   String createCatalogId(String var1, String var2);

   String createHash(String var1, String var2, String var3, String var4);

   void release();
}
