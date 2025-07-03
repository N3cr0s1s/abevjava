package hu.piller.enykp.alogic.calculator.matrices.matrixproviderimpl;

import hu.piller.enykp.alogic.calculator.matrices.MREF;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import java.io.File;

public class MatrixProviderAltalanos extends MatrixProviderFile {
   public String getPath(MREF var1) {
      return OrgInfo.getInstance().getResourcePath() + File.separator + "matrix.jar";
   }
}
