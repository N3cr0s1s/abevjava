package hu.piller.enykp.alogic.templateutils;

import hu.piller.enykp.alogic.calculator.matrices.defaultMatrixHandler;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.metainfo.MetaStore;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class FieldsGroups {
   private static FieldsGroups instance = new FieldsGroups();
   private static final String KEY_DELIMITER = "_";
   public static final String KEY_FID = "fid";
   public static final String KEY_GROUPID = "groupId";
   public static final String META_GROUP_ID = "field_group_id";
   public static final String META_DIN_GROUP_ID = "din_field_group_id";
   public static final String META_MATRIX_ID = "matrix_id";
   public static final String META_MATRIX_DELIMITER = "matrix_delimiter";
   public static final String META_MATRIX_FIELD_COL = "matrix_field_col_num";
   public static final String META_MATRIX_FIELD_LIST = "matrix_field_list";
   public static final String META_FIELD_VALIDATION = "validation";
   public static final String META_FIELD_FID = "fid";
   public static final String META_FIELD_VID = "vid";
   public static final String ERROR_DIFF_GROUPS = "Mezőcsoport definíciós hiba: ";
   public static final String ERROR_DIFF_MATRIX_ID = "Eltér a mátrix azonosító.";
   public static final String ERROR_DIFF_DELIMITER = "Eltér az elválasztó karakter.";
   public static final String ERROR_DIFF_VALIDATION = "Eltér az ellenőrzési típus.";
   private Hashtable<String, Map<FieldsGroups.GroupType, Hashtable<String, IFieldsGroupModel>>> forms = new Hashtable();

   public static FieldsGroups getInstance() {
      return instance;
   }

   public IFieldsGroupModel getFieldsGroupByFid(FieldsGroups.GroupType var1, String var2, String var3) {
      Map var4 = (Map)this.forms.get(var2);
      if (var4 == null) {
         return null;
      } else {
         Hashtable var5 = (Hashtable)var4.get(var1);
         return var5 != null ? (IFieldsGroupModel)var5.get(this.getKey("fid", var3)) : null;
      }
   }

   public IFieldsGroupModel getFieldsGroupByGroupId(FieldsGroups.GroupType var1, String var2, String var3) {
      Map var4 = (Map)this.forms.get(var2);
      if (var4 == null) {
         return null;
      } else {
         Hashtable var5 = (Hashtable)var4.get(var1);
         return var5 != null ? (IFieldsGroupModel)var5.get(this.getKey("groupId", var3)) : null;
      }
   }

   public void release() {
      this.forms = new Hashtable();
   }

   private String getKey(String var1, String var2) {
      return var1 + "_" + var2;
   }

   public void createModels(String var1) throws Exception {
      HashMap var2 = new HashMap();
      this.forms.put(var1, var2);
      Hashtable var3 = new Hashtable();
      var2.put(FieldsGroups.GroupType.ALL, var3);
      this.createGroupModels(var1, FieldsGroups.GroupType.STATIC, "field_group_id", var3);
      this.createGroupModels(var1, FieldsGroups.GroupType.DINAMYC, "din_field_group_id", var3);
   }

   public void createGroupModels(String var1, FieldsGroups.GroupType var2, String var3, Hashtable<String, IFieldsGroupModel> var4) throws Exception {
      try {
         Map var5 = (Map)this.forms.get(var1);
         Hashtable var6 = new Hashtable();
         var5.put(var2, var6);
         MetaStore var7 = MetaInfo.getInstance().getMetaStore(var1);
         Hashtable var8 = this.getGroups(var7, var3);
         Enumeration var9 = var8.keys();

         while(var9.hasMoreElements()) {
            String var10 = (String)var9.nextElement();
            Hashtable var11 = (Hashtable)var8.get(var10);
            Enumeration var12 = var11.keys();

            while(var12.hasMoreElements()) {
               String var13 = (String)var12.nextElement();
               Hashtable var14 = (Hashtable)var7.getFieldMetas(var13);
               this.catalog(var6, var2, var10, var13, var14);
            }

            if (var2.equals(FieldsGroups.GroupType.STATIC)) {
               this.checkParameters(var6, var1, var10);
            }
         }

         var4.putAll(var6);
      } catch (Exception var15) {
         this.forms = null;
         throw var15;
      }
   }

   private Hashtable getGroups(MetaStore var1, String var2) {
      Hashtable var3 = new Hashtable();
      Hashtable var4 = var1.getFieldAttributes(var2, true);

      String var6;
      Hashtable var8;
      for(Enumeration var5 = var4.keys(); var5.hasMoreElements(); var8.put(var6, "")) {
         var6 = (String)var5.nextElement();
         String var7 = (String)var4.get(var6);
         var8 = (Hashtable)var3.get(var7);
         if (var8 == null) {
            var8 = new Hashtable();
            var3.put(var7, var8);
         }
      }

      return var3;
   }

   private void catalog(Hashtable<String, IFieldsGroupModel> var1, FieldsGroups.GroupType var2, String var3, String var4, Hashtable var5) throws Exception {
      if (var2.equals(FieldsGroups.GroupType.STATIC)) {
         this.catalogStatic(var1, var3, var4, var5);
      } else if (var2.equals(FieldsGroups.GroupType.DINAMYC)) {
         this.catalogDinamyc(var1, var3, var4);
      }

   }

   private void catalogDinamyc(Hashtable<String, IFieldsGroupModel> var1, String var2, String var3) throws Exception {
      Object var4 = (IFieldsGroupModel)var1.get(this.getKey("groupId", var2));
      if (var4 == null) {
         var4 = new FieldsGroupModelDin(var3, var2);
         var1.put(this.getKey("groupId", var2), (IFieldsGroupModel) var4);
      }

      var1.put(this.getKey("fid", var3), (IFieldsGroupModel) var4);
      ((IFieldsGroupModel)var4).addFid(var3, "");
   }

   private void catalogStatic(Hashtable<String, IFieldsGroupModel> var1, String var2, String var3, Hashtable var4) throws Exception {
      String var5 = (String)var4.get("matrix_id");
      String var6 = (String)var4.get("matrix_delimiter");
      String var7 = (String)var4.get("matrix_field_col_num");
      boolean var8 = this.getBoolean((String)var4.get("validation"));
      String var9 = (String)var4.get("vid");
      Object var10 = (IFieldsGroupModel)var1.get(this.getKey("groupId", var2));
      if (var10 == null) {
         var10 = new FieldsGroupModel(var3, var2, var5, var6, var8);
         var1.put(this.getKey("groupId", var2), (IFieldsGroupModel) var10);
      }

      if (!((IFieldsGroupModel)var10).getMatrixId().equals(var5)) {
         throw new Exception("Mezőcsoport definíciós hiba: Eltér a mátrix azonosító. (" + var3 + "/" + var9 + ")");
      } else if (!((IFieldsGroupModel)var10).getDelimiter().equals(var6)) {
         throw new Exception("Mezőcsoport definíciós hiba: Eltér az elválasztó karakter. (" + var3 + "/" + var9 + ")");
      } else if (((IFieldsGroupModel)var10).isValidation() != var8) {
         throw new Exception("Mezőcsoport definíciós hiba: Eltér az ellenőrzési típus. (" + var3 + "/" + var9 + ")");
      } else {
         var1.put(this.getKey("fid", var3), (IFieldsGroupModel) var10);
         ((IFieldsGroupModel)var10).addFid(var3, var7);
      }
   }

   private void checkParameters(Hashtable<String, IFieldsGroupModel> var1, String var2, String var3) throws Exception {
      IFieldsGroupModel var4 = (IFieldsGroupModel)var1.get(this.getKey("groupId", var3));
      if (!defaultMatrixHandler.getInstance().isMatrixExists(var2, var4.getMatrixId())) {
         throw new Exception("Nem létező mátrix! (" + var2 + "," + var4.getMatrixId() + ")");
      }
   }

   private boolean getBoolean(String var1) {
      return var1 != null && var1.equalsIgnoreCase("true");
   }

   public static enum GroupType {
      STATIC,
      DINAMYC,
      ALL;
   }
}
