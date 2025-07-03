package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd;

import hu.piller.enykp.alogic.masterdata.core.Block;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import java.util.List;
import java.util.Map;

public interface ITechnicalMdHandler {
   Map<String, List<String>> split(List<String> var1);

   List<String> build(Entity var1);

   List<String> build(Block var1);
}
