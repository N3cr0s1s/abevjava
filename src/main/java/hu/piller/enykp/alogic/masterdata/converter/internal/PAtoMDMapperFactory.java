package hu.piller.enykp.alogic.masterdata.converter.internal;

public class PAtoMDMapperFactory {
   private static PAtoMDMapperFactory _instance;

   private PAtoMDMapperFactory() {
   }

   public static final PAtoMDMapperFactory getInstance() {
      if (_instance == null) {
         _instance = new PAtoMDMapperFactory();
      }

      return _instance;
   }

   public PAtoMDMapper getMapper(PAConversionTask var1) {
      Object var2 = null;
      if ("Magánszemély".equals(var1.getEntityType())) {
         var2 = new PersonPAtoMDMapper();
      } else if ("Egyéni vállalkozó".equals(var1.getEntityType())) {
         var2 = new SmallbusinessPAtoMDMapper();
      } else if ("Társaság".equals(var1.getEntityType())) {
         var2 = new CompanyPAtoMDMapper();
      } else if ("Adótanácsadó".equals(var1.getEntityType())) {
         var2 = new TaxexpertPAtoMDMapper();
      }

      return (PAtoMDMapper)var2;
   }
}
