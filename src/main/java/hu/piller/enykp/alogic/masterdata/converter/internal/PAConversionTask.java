package hu.piller.enykp.alogic.masterdata.converter.internal;

import hu.piller.enykp.util.base.PropertyList;
import me.necrocore.abevjava.NecroFile;

import java.io.File;

public class PAConversionTask {
   public static final String TAXEXPERT = "TAXEXPERT";
   public static final String COMPANY = "COMPANY";
   public static final String PERSON = "PERSON";
   public static final String SMALLBUSINESS = "SMALLBUSINESS";
   private String paType;
   private String entityType;
   private String fileName;

   public PAConversionTask(String var1, String var2, String var3) {
      this.entityType = var1;
      this.paType = var2;
      this.fileName = var3;
   }

   public String getType() {
      return this.paType;
   }

   public String getEntityType() {
      return this.entityType;
   }

   public File getFile() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.getPath());
      var1.append("/");
      var1.append(this.fileName);
      return new NecroFile(var1.toString());
   }

   private String getPath() {
      return (String)PropertyList.getInstance().get("prop.usr.primaryaccounts");
   }

   public void archiveFile() throws Exception {
      this.getFile().renameTo(new NecroFile(this.getPath() + "/" + this.fileName + ".converted"));
   }

   public boolean isTaskExecutable() {
      return this.getFile().exists();
   }
}
