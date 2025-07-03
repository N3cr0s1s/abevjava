package hu.piller.enykp.alogic.calculator.calculator_c;

import java.util.ArrayList;
import java.util.List;

public class MatrixSearchModel {
   private String matrixId;
   private String delimiter;
   private ArrayList<MatrixSearchItem> matrixSearchList;

   public MatrixSearchModel(String var1, String var2) {
      this.matrixId = var1;
      this.delimiter = var2;
      this.matrixSearchList = new ArrayList();
   }

   public String getMatrixId() {
      return this.matrixId;
   }

   public void setMatrixId(String var1) {
      this.matrixId = var1;
   }

   public String getDelimiter() {
      return this.delimiter;
   }

   public void setDelimiter(String var1) {
      this.delimiter = var1;
   }

   public List<MatrixSearchItem> getSearchList() {
      return this.matrixSearchList;
   }

   public void addSearchItem(MatrixSearchItem var1) {
      this.matrixSearchList.add(var1);
   }

   public String toString() {
      return "(matrixId=" + this.matrixId + ",delimiter=" + this.delimiter + ", MatrixSearchItems=(" + this.matrixSearchList.toString() + "))";
   }
}
