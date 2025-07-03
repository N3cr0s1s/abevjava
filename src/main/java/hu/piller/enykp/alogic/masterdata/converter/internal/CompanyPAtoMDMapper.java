package hu.piller.enykp.alogic.masterdata.converter.internal;

public class CompanyPAtoMDMapper extends PAtoMDMapper {
   public String getEntityType() {
      return "Társaság";
   }

   protected void buildMappings() {
      this.mappings.put("name", new PAtoMDMapper.Mapping("Törzsadatok", "Adózó neve"));
      this.mappings.put("tax_number", new PAtoMDMapper.Mapping("Törzsadatok", "Adózó adószáma"));
      this.mappings.put("s_zip", new PAtoMDMapper.Mapping("Állandó cím", "Irányítószám"));
      this.mappings.put("s_public_place", new PAtoMDMapper.Mapping("Állandó cím", "Közterület neve"));
      this.mappings.put("s_staircase", new PAtoMDMapper.Mapping("Állandó cím", "Lépcsőház"));
      this.mappings.put("s_level", new PAtoMDMapper.Mapping("Állandó cím", "Emelet"));
      this.mappings.put("s_public_place_type", new PAtoMDMapper.Mapping("Állandó cím", "Közterület jellege"));
      this.mappings.put("s_settlement", new PAtoMDMapper.Mapping("Állandó cím", "Település"));
      this.mappings.put("s_building", new PAtoMDMapper.Mapping("Állandó cím", "Épület"));
      this.mappings.put("s_door", new PAtoMDMapper.Mapping("Állandó cím", "Ajtó"));
      this.mappings.put("s_house_number", new PAtoMDMapper.Mapping("Állandó cím", "Házszám"));
      this.mappings.put("m_zip", new PAtoMDMapper.Mapping("Levelezési cím", "L Irányítószám"));
      this.mappings.put("m_public_place", new PAtoMDMapper.Mapping("Levelezési cím", "L Közterület neve"));
      this.mappings.put("m_staircase", new PAtoMDMapper.Mapping("Levelezési cím", "L Lépcsőház"));
      this.mappings.put("m_level", new PAtoMDMapper.Mapping("Levelezési cím", "L Emelet"));
      this.mappings.put("m_public_place_type", new PAtoMDMapper.Mapping("Levelezési cím", "L Közterület jellege"));
      this.mappings.put("m_settlement", new PAtoMDMapper.Mapping("Levelezési cím", "L Település"));
      this.mappings.put("m_building", new PAtoMDMapper.Mapping("Levelezési cím", "L Épület"));
      this.mappings.put("m_door", new PAtoMDMapper.Mapping("Levelezési cím", "L Ajtó"));
      this.mappings.put("m_house_number", new PAtoMDMapper.Mapping("Levelezési cím", "L Házszám"));
      this.mappings.put("administrator", new PAtoMDMapper.Mapping("Egyéb adatok", "Ügyintéző neve"));
      this.mappings.put("tel", new PAtoMDMapper.Mapping("Egyéb adatok", "Ügyintéző telefonszáma"));
      this.mappings.put("financial_corp", new PAtoMDMapper.Mapping("Egyéb adatok", "Pénzintézet neve"));
      this.mappings.put("financial_corp_id", new PAtoMDMapper.Mapping("Egyéb adatok", "Pénzintézet-azonosító"));
      this.mappings.put("account_id", new PAtoMDMapper.Mapping("Egyéb adatok", "Számla-azonosító"));
   }
}
