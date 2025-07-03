package hu.piller.enykp.alogic.calculator.matrices;

public class MatrixMeta {
   private String encoding = "";
   private String name = "";
   private String file = "";
   private String delimiter = "";

   public String getFile() {
      return this.file;
   }

   public void setFile(String var1) {
      this.file = var1;
   }

   public String getEncoding() {
      return this.encoding;
   }

   public void setEncoding(String var1) {
      this.encoding = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getDelimiter() {
      return this.delimiter;
   }

   public void setDelimiter(String var1) {
      this.delimiter = var1;
   }

   public String toString() {
      return "[name=" + this.name + ", file=" + this.file + ", encoding=" + this.encoding + ", delimiter=" + this.delimiter + "]";
   }
}
