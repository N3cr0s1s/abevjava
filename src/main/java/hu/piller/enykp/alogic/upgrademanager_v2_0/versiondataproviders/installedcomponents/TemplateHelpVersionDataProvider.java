package hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.installedcomponents;

import hu.piller.enykp.alogic.fileloader.docinfo.DocInfoLoader;
import hu.piller.enykp.alogic.upgrademanager_v2_0.Directories;
import me.necrocore.abevjava.NecroFile;

import java.io.File;

public class TemplateHelpVersionDataProvider extends InstalledVersionDataProvider {
   public static final String CATEGORY = "Help";

   public TemplateHelpVersionDataProvider() {
      super("Help");
      this.path = new NecroFile(Directories.getHelpsPath());
      this.filters = new Object[]{new DocInfoLoader()};
   }
}
