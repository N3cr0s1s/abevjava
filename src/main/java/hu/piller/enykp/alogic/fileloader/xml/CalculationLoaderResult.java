package hu.piller.enykp.alogic.fileloader.xml;

public class CalculationLoaderResult {
   public static final String STATUS_OK = "0";
   public static final String STATUS_INPUTSTREAM_READ_ERROR = "-101";
   public static final String STATUS_TEMPLATE_ERROR = "-102";
   public static final String STATUS_XML_LOAD_ERROR = "-103";
   public static final String STATUS_CALCULATOR_ERROR = "-104";
   public static final String STATUS_XML_SAVE_ERROR = "-105";
   public static final String STATUS_PDF_CREATE_ERROR = "-106";
   public static final String STATUS_ERRORLIST_CREATE_ERROR = "-107";
   public static final String STATUS_DATACHECKER_ERROR = "-110";
   private byte[] calculatedXMLData;
   private byte[] pdfData;
   private byte[] errorListXMLData;
   private String status;

   public CalculationLoaderResult() {
   }

   public CalculationLoaderResult(byte[] var1, byte[] var2, byte[] var3) {
      this.calculatedXMLData = var1;
      this.errorListXMLData = var2;
      this.pdfData = var3;
      this.status = "0";
   }

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String var1) {
      this.status = var1;
   }

   public byte[] getCalculatedXMLData() {
      return this.calculatedXMLData;
   }

   public byte[] getErrorListXMLData() {
      return this.errorListXMLData;
   }

   public byte[] getPdfData() {
      return this.pdfData;
   }
}
