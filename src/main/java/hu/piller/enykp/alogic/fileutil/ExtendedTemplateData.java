package hu.piller.enykp.alogic.fileutil;

import java.io.File;
import java.util.Vector;

public class ExtendedTemplateData {
   public static final String ERROR_MESSAGE_DEFAULT = "A nyomtatvány már csak az Online Nyomtatványkitöltő Alkalmazásban érhető el! ";
   public static final String ERROR_MESSAGE_LIST = "Az alkalmazás indításához a linket másolja egy böngészőbe: ";
   private File templateFile;
   private String[] templateFileNames;
   private String templateId;
   private String orgId;
   private boolean isTemplateDisaled = false;

   public ExtendedTemplateData(String var1, String var2) {
      Vector var3 = TemplateNameResolver.getInstance().getName(var1);
      if (var3 == null) {
         this.templateId = var1;
      } else {
         this.templateId = (String)var3.get(0);
      }

      this.orgId = var2;
   }

   public ExtendedTemplateData(String var1, String var2, File var3, boolean var4) {
      Vector var5 = TemplateNameResolver.getInstance().getName(var1);
      if (var5 == null) {
         this.templateId = var1;
      }

      this.templateFile = var3;
      this.orgId = var2;
      this.isTemplateDisaled = var4;
   }

   public ExtendedTemplateData(String var1, String var2, String[] var3, boolean var4) {
      Vector var5 = TemplateNameResolver.getInstance().getName(var1);
      if (var5 == null) {
         this.templateId = var1;
      }

      this.orgId = var2;
      this.templateFileNames = var3;
      this.isTemplateDisaled = var4;
   }

   public String[] getTemplateFileNames() {
      return this.templateFileNames;
   }

   public File getTemplateFile() {
      return this.templateFile;
   }

   public boolean isTemplateDisaled() {
      return this.isTemplateDisaled;
   }

   public String getTemplateId() {
      return this.templateId;
   }

   public String getOrgId() {
      return this.orgId;
   }
}
