package hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.installedcomponents;

import hu.piller.enykp.alogic.upgrademanager_v2_0.Directories;
import hu.piller.enykp.gui.model.BookModel;
import me.necrocore.abevjava.NecroFile;

import java.io.File;

public class TemplateVersionDataProvider extends InstalledVersionDataProvider {
   public static final String CATEGORY = "Template";

   public TemplateVersionDataProvider() {
      super("Template");
      this.path = new NecroFile(Directories.getTemplatesPath());
      this.filters = new Object[]{new BookModel()};
   }
}
