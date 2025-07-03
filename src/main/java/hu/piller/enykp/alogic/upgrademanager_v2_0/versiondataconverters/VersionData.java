package hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters;

import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeBusinessException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeLogger;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.Version;

public class VersionData implements Cloneable, Comparable {
   public static final String SOURCE_NETWORK = "Network";
   public static final String SOURCE_FILESYSTEM = "Filesystem";
   public static final String CATEGORY_RESOURCE = "Orgresource";
   public static final String CATEGORY_FRAMEWORK = "Framework";
   public static final String CATEGORY_TEMPLATE = "Template";
   public static final String CATEGORY_HELP = "Help";
   private String sourceCategory;
   protected String category;
   private String name;
   private Version version;
   private String description;
   private String location;
   private String organization;
   private String[] files;
   private String group = "";
   private String[] filesWithPath;
   private static final char[] spec_chars = new char[]{'/', '\\', '?', ':', ';', '.', '*', '<', '>', '|', ',', ' ', '-'};
   private static final char[] hun_chars = new char[]{'á', 'é', 'ó', 'ú', 'ű', 'ü', 'ö', 'í', 'ő', 'Á', 'É', 'Ó', 'Ú', 'Ű', 'Ü', 'Ö', 'Í', 'Ő'};
   private static final char[] converted_hun_chars = new char[]{'a', 'e', 'o', 'u', 'u', 'u', 'o', 'i', 'o', 'a', 'e', 'o', 'u', 'u', 'u', 'o', 'i', 'o'};

   public Object clone() {
      VersionData var1 = null;

      try {
         var1 = (VersionData)super.clone();
      } catch (CloneNotSupportedException var3) {
      }

      return var1;
   }

   public void setOrganization(String var1) {
      this.organization = var1;
   }

   public String getOrganization() {
      return this.organization;
   }

   public void setLocation(String var1) {
      if (var1 != null && var1.length() > 0 && var1.charAt(var1.length() - 1) == '/') {
         var1 = var1.substring(0, var1.length() - 1);
      }

      this.location = var1 == null ? "" : var1;
   }

   public String getLocation() {
      return this.location;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public void setVersion(Version var1) {
      this.version = var1;
   }

   public Version getVersion() {
      return this.version;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setCategory(String var1) {
      this.category = var1;
   }

   public String getCategory() {
      return this.category;
   }

   public void setGroup(String var1) {
      this.group = var1;
   }

   public String getGroup() {
      return this.group;
   }

   public void setSourceCategory(String var1) {
      this.sourceCategory = var1;
   }

   public String getSourceCategory() {
      return this.sourceCategory;
   }

   public void setFiles(String[] var1) {
      this.files = new String[var1.length];
      this.filesWithPath = new String[var1.length];
      int var2 = 0;
      String[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         this.filesWithPath[var2] = var6;
         this.files[var2++] = this.processFile(var6);
      }

   }

   public String[] getFiles() {
      return this.files;
   }

   public boolean isFramework() {
      return "Framework".equals(this.category);
   }

   public boolean greaterThan(Object var1) {
      boolean var2 = false;
      if (this.category != null && this.name != null && this.organization != null && this.version != null) {
         if (var1 != null && var1 instanceof VersionData) {
            VersionData var3 = (VersionData)var1;
            if (this.category.equalsIgnoreCase(var3.category) && this.normalize(this.name).equalsIgnoreCase(this.normalize(var3.name)) && this.organization.equalsIgnoreCase(var3.organization) && this.version.compareTo(var3.version) > 0) {
               var2 = true;
               if (this.hasNameConflict(var3)) {
                  UpgradeLogger.getInstance().log("NEVKONFLIKTUS: [" + this.organization + "] " + this.name + " <--> " + var3.name);
                  String var4 = "";
                  String var5 = "";
                  if (this.sourceCategory.equals("Filesystem")) {
                     var4 = this.name;
                     var5 = var3.name;
                  } else {
                     var5 = this.name;
                     var4 = var3.name;
                  }

                  String var6 = "A(z) " + this.organization + " szervezet nyomtatványában megadott '" + var4 + "' azonosító, és a frissítési verzióleíróban található '" + var5 + "' azonosító nem egyezik.";
                  ErrorList.getInstance().writeError("VersionData", var6, IErrorList.LEVEL_WARNING, (Exception)null, (Object)null);
               }
            }
         } else {
            var2 = false;
         }
      } else {
         var2 = false;
      }

      return var2;
   }

   private boolean hasNameConflict(VersionData var1) {
      boolean var2 = false;
      if (!this.name.equals(var1.name)) {
         var2 = true;
      }

      return var2;
   }

   public boolean equalsCompatible(VersionData var1) {
      if (this.category != null) {
         if (!this.category.equalsIgnoreCase(var1.category)) {
            return false;
         }
      } else if (var1.category != null) {
         return false;
      }

      label44: {
         if (this.name != null) {
            if (this.normalize(this.name).equalsIgnoreCase(this.normalize(var1.name))) {
               break label44;
            }
         } else if (var1.name == null) {
            break label44;
         }

         return false;
      }

      label37: {
         if (this.organization != null) {
            if (this.organization.equalsIgnoreCase(var1.organization)) {
               break label37;
            }
         } else if (var1.organization == null) {
            break label37;
         }

         return false;
      }

      if (this.version != null) {
         if (!this.version.equals(var1.version)) {
            return false;
         }
      } else if (var1.version != null) {
         return false;
      }

      return true;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         VersionData var2;
         label57: {
            var2 = (VersionData)var1;
            if (this.category != null) {
               if (this.category.equalsIgnoreCase(var2.category)) {
                  break label57;
               }
            } else if (var2.category == null) {
               break label57;
            }

            return false;
         }

         label50: {
            if (this.name != null) {
               if (this.normalize(this.name).equalsIgnoreCase(this.normalize(var2.name))) {
                  break label50;
               }
            } else if (var2.name == null) {
               break label50;
            }

            return false;
         }

         if (this.organization != null) {
            if (!this.organization.equalsIgnoreCase(var2.organization)) {
               return false;
            }
         } else if (var2.organization != null) {
            return false;
         }

         if (this.version != null) {
            if (!this.version.equals(var2.version)) {
               return false;
            }
         } else if (var2.version != null) {
            return false;
         }

         return true;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.category != null ? this.category.hashCode() : 0;
      var1 = 29 * var1 + (this.name != null ? this.name.hashCode() : 0);
      var1 = 29 * var1 + (this.version != null ? this.version.hashCode() : 0);
      var1 = 29 * var1 + (this.organization != null ? this.organization.hashCode() : 0);
      return var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(1024);
      var1.append("[");
      var1.append(this.organization);
      var1.append(", ");
      var1.append(this.category);
      var1.append(", ");
      var1.append(this.name);
      var1.append(", ");
      var1.append(this.version);
      var1.append(", ");
      var1.append(this.description);
      var1.append(", ");
      var1.append(this.group);
      var1.append(", ");
      var1.append(this.location);
      var1.append(", ");
      var1.append(this.sourceCategory);
      var1.append(", ");
      var1.append("[");

      for(int var2 = 0; this.files != null && var2 < this.files.length; ++var2) {
         var1.append(this.files[var2]);
         if (var2 < this.files.length - 1) {
            var1.append(", ");
         }
      }

      var1.append("]]");
      return var1.toString();
   }

   public String getFileNameByType(String var1) throws UpgradeBusinessException {
      return this.getFileNameByType(var1, false);
   }

   public String getFileNameByTypeAndPath(String var1) throws UpgradeBusinessException {
      return this.getFileNameByType(var1, true);
   }

   private String getFileNameByType(String var1, boolean var2) throws UpgradeBusinessException {
      String[] var3;
      if (var2) {
         var3 = this.filesWithPath;
      } else {
         var3 = this.files;
      }

      for(int var4 = 0; var4 < var3.length; ++var4) {
         String[] var5 = var3[var4].split("\\.");
         if (var1.equalsIgnoreCase(var5[var5.length - 1])) {
            return var3[var4];
         }
      }

      if ("jar".equals(var1)) {
         throw new UpgradeBusinessException(this.getOrganization() + " " + this.getName() + " " + this.getVersion().toString() + " nem elérhető!");
      } else {
         return this.getFileNameByType("jar", var2);
      }
   }

   private String processFile(String var1) {
      int var2 = var1.lastIndexOf("/");
      ++var2;
      return var1.substring(var2);
   }

   private String normalize(String var1) {
      StringBuilder var2 = new StringBuilder("");

      for(int var3 = 0; var1 != null && var3 < var1.length(); ++var3) {
         char var4 = var1.charAt(var3);
         var4 = this.convertSpecialCharacter(var4);
         if (var4 != '_') {
            var4 = this.convertHungarianCharacter(var4);
         }

         var2.append(var4);
      }

      return var2.toString();
   }

   private char convertSpecialCharacter(char var1) {
      int var2 = this.indexOf(spec_chars, var1);
      return var2 != -1 ? '_' : var1;
   }

   private char convertHungarianCharacter(char var1) {
      int var2 = this.indexOf(hun_chars, var1);
      return var2 != -1 ? converted_hun_chars[var2] : var1;
   }

   private int indexOf(char[] var1, char var2) {
      int var3 = -1;
      int var4 = var3;
      char[] var5 = var1;
      int var6 = var1.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         char var8 = var5[var7];
         ++var4;
         if (var8 == var2) {
            var3 = var4;
            break;
         }
      }

      return var3;
   }

   public int compareTo(Object var1) {
      return var1 instanceof VersionData ? this.name.compareTo(((VersionData)var1).name) : 0;
   }
}
