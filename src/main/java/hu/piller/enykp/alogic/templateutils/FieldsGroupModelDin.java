package hu.piller.enykp.alogic.templateutils;

import java.util.Hashtable;

public class FieldsGroupModelDin implements IFieldsGroupModel {
   private String fid;
   private String groupId;
   private Hashtable<String, String> fidsColumns;

   public FieldsGroupModelDin(String var1, String var2) {
      this.groupId = var2;
      this.fid = var1;
   }

   public String getFid() {
      return this.fid;
   }

   public void setFid(String var1) {
      this.fid = var1;
   }

   public String getDelimiter() {
      return null;
   }

   public boolean isValidation() {
      return false;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public String getMatrixId() {
      return null;
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
