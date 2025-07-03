package hu.piller.enykp.alogic.masterdata.core.validator;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityError;

public interface EntityValidator {
   EntityError[] isValid(Entity var1);
}
