package hu.piller.enykp.alogic.upgrademanager_v2_0.components;

import hu.piller.enykp.alogic.upgrademanager_v2_0.components.reader.ComponentsReader;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.reader.ComponentsReaderFactory;

public class InstalledComponents extends Components {
   public InstalledComponents() {
      ComponentsReader var1 = ComponentsReaderFactory.getInstalledComponentsReader();
      this.components.addAll(var1.getComponents());
   }
}
