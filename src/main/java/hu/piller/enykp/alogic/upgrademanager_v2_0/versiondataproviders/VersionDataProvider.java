package hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders;

import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeBusinessException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeTechnicalException;
import java.util.Vector;

public abstract class VersionDataProvider {
   public static final String SOURCE_CATEGORY_DOWNLOADABLE = "DOWNLOADABLE";
   public static final String SOURCE_CATEGORY_INSTALLED = "INSTALLED";
   public static final String NO_DATA = "(nincs adat)";
   private String sourceCategory;
   private String category;
   private Vector collection;

   public VersionDataProvider(String var1, String var2) {
      this.sourceCategory = var1;
      this.category = var2;
      this.collection = new Vector();
   }

   public String getSourceCategory() {
      return this.sourceCategory;
   }

   protected void setSourceCategory(String var1) {
      this.sourceCategory = var1;
   }

   public String getCategory() {
      return this.category;
   }

   protected void setCategory(String var1) {
      this.category = var1;
   }

   public Vector getCollection() {
      return this.collection;
   }

   protected void setCollection(Vector var1) {
      this.collection = var1;
   }

   public abstract void collect() throws UpgradeBusinessException, UpgradeTechnicalException;

   public String toString() {
      StringBuffer var1 = new StringBuffer(1024);
      var1.append("[");
      var1.append(this.sourceCategory);
      var1.append(", ");
      var1.append(this.category);
      var1.append(", ");

      for(int var2 = 0; this.collection != null && var2 < this.collection.size(); ++var2) {
         var1.append("{");
         var1.append(this.collection.get(var2).toString());
         var1.append("}");
         if (var2 < this.collection.size() - 1) {
            var1.append(",");
         }
      }

      var1.append("]");
      return var1.toString();
   }
}
