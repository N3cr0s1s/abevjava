package hu.piller.enykp.alogic.templateutils;

import java.util.Hashtable;

public interface IFieldsGroupModel {
   String getFid();

   void setFid(String var1);

   String getDelimiter();

   boolean isValidation();

   String getGroupId();

   String getMatrixId();

   Hashtable<String, String> getFidsColumns();

   void addFid(String var1, String var2);
}
