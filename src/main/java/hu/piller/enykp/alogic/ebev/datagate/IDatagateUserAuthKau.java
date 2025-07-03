package hu.piller.enykp.alogic.ebev.datagate;

import hu.piller.enykp.kauclient.KauResult;
import java.util.Map;

public interface IDatagateUserAuthKau {
   KauResult authenticate(Map<String, String> var1) throws Exception;
}
