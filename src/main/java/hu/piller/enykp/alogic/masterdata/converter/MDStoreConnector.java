package hu.piller.enykp.alogic.masterdata.converter;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityException;

public interface MDStoreConnector {
   Entity getEntity(String var1) throws EntityException;

   String[] map(String var1, boolean var2);
}
