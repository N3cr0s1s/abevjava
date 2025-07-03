package hu.piller.enykp.alogic.templateutils;

import java.util.Hashtable;

public class FieldsGroupModel implements IFieldsGroupModel {
   private String fid;
   private String groupId;
   private String matrixId;
   private String delimiter;
   private boolean validation;
   private Hashtable<String, String> fidsColumns;

   public FieldsGroupModel(String var1, String var2, String var3, String var4, boolean var5) {
      this.delimiter = var4;
      this.groupId = var2;
      this.matrixId = var3;
      this.fid = var1;
      this.validation = var5;
   }

   public String getFid() {
      return this.fid;
   }

   public void setFid(String var1) {
      this.fid = var1;
   }

   public String getDelimiter() {
      return this.delimiter;
   }

   public boolean isValidation() {
      return this.validation;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public String getMatrixId() {
      return this.matrixId;
   }

   public Hashtable<String, String> getFidsColumns() {
      return this.fidsColumns;
   }

   public void addFid(String var1, String var2) {
      if (this.fidsColumns == null) {
         this.fidsColumns = new Hashtable();
      }

      this.fidsColumns.put(var1, var2);
   }
}
