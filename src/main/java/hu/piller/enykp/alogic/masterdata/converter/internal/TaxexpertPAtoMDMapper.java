package hu.piller.enykp.alogic.masterdata.converter.internal;

public class TaxexpertPAtoMDMapper extends PAtoMDMapper {
   public String getEntityType() {
      return "Adótanácsadó";
   }

   protected void buildMappings() {
      this.mappings.put("te_name", new PAtoMDMapper.Mapping("Név", "Adótanácsadó neve"));
      this.mappings.put("te_id", new PAtoMDMapper.Mapping("Név", "Adótanácsadó azonosítószáma"));
      this.mappings.put("te_testimontial_id", new PAtoMDMapper.Mapping("Név", "Adótanácsadó Bizonyítvány"));
   }
}
