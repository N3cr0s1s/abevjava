package hu.piller.enykp.alogic.calculator.calculator_c;

public class LookupListModel {
   private Boolean overWrite;
   private Integer valueColumnNumber;
   private String listColumnNumbers;
   private MatrixSearchModel matrixSearchModel;

   public LookupListModel(Boolean var1, Integer var2, String var3, MatrixSearchModel var4) {
      this.overWrite = var1;
      this.valueColumnNumber = var2;
      this.listColumnNumbers = var3;
      this.matrixSearchModel = var4;
   }

   public Boolean isOverWrite() {
      return this.overWrite;
   }

   public void setOverWrite(Boolean var1) {
      this.overWrite = var1;
   }

   public Integer getValueColumnNumber() {
      return this.valueColumnNumber;
   }

   public void setValueColumnNumber(Integer var1) {
      this.valueColumnNumber = var1;
   }

   public String getListColumnNumbers() {
      return this.listColumnNumbers;
   }

   public void setListColumnNumbers(String var1) {
      this.listColumnNumbers = var1;
   }

   public MatrixSearchModel getMatrixSearchModel() {
      return this.matrixSearchModel;
   }

   public void setMatrixSearchModel(MatrixSearchModel var1) {
      this.matrixSearchModel = var1;
   }

   public String toString() {
      return "(overWrite=" + this.overWrite + ", valueColumnNumber=" + this.valueColumnNumber + ", listColumnNumbers=" + this.listColumnNumbers + ", matrixSearchModel=" + this.matrixSearchModel.toString() + ")";
   }
}
