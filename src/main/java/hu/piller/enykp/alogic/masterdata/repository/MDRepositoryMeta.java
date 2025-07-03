package hu.piller.enykp.alogic.masterdata.repository;

import hu.piller.enykp.alogic.masterdata.core.BlockDefinition;
import hu.piller.enykp.alogic.masterdata.core.validator.EntityValidator;

public interface MDRepositoryMeta {
   BlockDefinition[] getBlockDefinitionsForEntity(String var1);

   String getOrgForMasterData(String var1);

   String[] getEntityTypes();

   String[] getMasterDataCommonForEntities(String[] var1);

   String getTypeOfMasterData(String var1);

   EntityValidator getValidatorForEntity(String var1);

   String[] getMasterDataForEntityTypeBlockType(String var1, String var2);
}
