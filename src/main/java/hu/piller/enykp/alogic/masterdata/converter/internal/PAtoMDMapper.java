package hu.piller.enykp.alogic.masterdata.converter.internal;

import java.util.Hashtable;

public abstract class PAtoMDMapper {
   protected Hashtable<String, PAtoMDMapper.Mapping> mappings = new Hashtable();

   public PAtoMDMapper() {
      this.buildMappings();
   }

   public PAtoMDMapper.Mapping getMappingForPAAttrib(String var1) {
      PAtoMDMapper.Mapping var2;
      if (this.mappings.containsKey(var1)) {
         var2 = (PAtoMDMapper.Mapping)this.mappings.get(var1);
      } else {
         var2 = new PAtoMDMapper.Mapping("", "");
      }

      return var2;
   }

   public abstract String getEntityType();

   protected abstract void buildMappings();

   public class Mapping {
      private String blockName;
      private String mdKey;

      public Mapping(String var2, String var3) {
         this.blockName = this.getNotNullValue(var2);
         this.mdKey = this.getNotNullValue(var3);
      }

      public String getBlockName() {
         return this.blockName;
      }

      public String getMdKey() {
         return this.mdKey;
      }

      public boolean isMapped() {
         boolean var1 = true;
         if ("".equals(this.blockName) || "".equals(this.mdKey)) {
            var1 = false;
         }

         return var1;
      }

      private String getNotNullValue(String var1) {
         return var1 == null ? "" : var1.trim();
      }
   }
}
