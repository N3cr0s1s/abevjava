package hu.piller.enykp.alogic.filepanels.attachement;

import java.io.File;
import java.util.Hashtable;
import javax.swing.filechooser.FileFilter;

public class AttachementInfo {
   private String id;
   public Hashtable extensions = new Hashtable();
   public FileFilter filter;
   public String exts;
   public boolean required;
   public String description;
   public int minCount;
   public int maxCount;
   public int attached;

   public AttachementInfo(Hashtable var1) {
      this.id = (String)var1.get("id");
      String var2 = (String)var1.get("file_extensions");
      this.exts = var2;
      this.filter = new AttachementInfo.CSFileFileter(var2);
      if (var2.equals("*")) {
         this.extensions.put(new Integer(1), "*");
      } else {
         String[] var3 = var2.split(";");

         for(int var4 = 0; var4 < var3.length; ++var4) {
            this.extensions.put(new Integer(var4), var3[var4]);
         }
      }

      this.description = (String)var1.get("description");
      this.required = var1.get("required").equals("1");
      this.minCount = Integer.parseInt((String)var1.get("min_count"));
      this.maxCount = Integer.parseInt((String)var1.get("max_count"));
      this.attached = 0;
   }

   public String toString() {
      try {
         return this.description;
      } catch (Exception var2) {
         return "";
      }
   }

   private class CSFileFileter extends FileFilter implements java.io.FileFilter {
      private String extensions;

      public CSFileFileter(String var2) {
         this.extensions = "." + var2.replaceAll(";", ";\\.");
      }

      public boolean accept(File var1) {
         if (var1.isDirectory()) {
            return true;
         } else {
            if (var1.isFile()) {
               if (this.extensions.equals(".*")) {
                  return true;
               }

               String var2 = var1.getName();
               if (var2.lastIndexOf(".") > -1) {
                  var2 = var2.substring(var2.lastIndexOf("."));
               }

               if (this.extensions.toLowerCase().lastIndexOf(var2.toLowerCase()) > -1) {
                  return true;
               }
            }

            return false;
         }
      }

      public String getDescription() {
         return this.extensions + " fájlok";
      }
   }
}
