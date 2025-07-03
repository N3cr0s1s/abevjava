package hu.piller.enykp.alogic.signer;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class AnykCsatolmanyLenyomat {
   public static final String POSTFIX = ".anyk";
   private String fileName;
   private String hash;

   private AnykCsatolmanyLenyomat() {
   }

   public static AnykCsatolmanyLenyomat create(File var0) throws NoSuchAlgorithmException, IOException {
      AnykCsatolmanyLenyomat var1 = new AnykCsatolmanyLenyomat();
      var1.fileName = var0.getName();
      Hash var2 = HashUtils.calcHash(var0, HashType.SHA_256);
      var1.hash = var2.getValue();
      return var1;
   }

   public String getFileName() {
      return this.fileName;
   }

   public String getHash() {
      return this.hash;
   }

   public String toXml() throws Exception {
      return (new AnykCsatolmanyLenyomatXmlBuilder()).createXmlText(this);
   }

   public String toString() {
      return "AnykCsatolmanyLenyomat [fileName=" + this.fileName + ", hash=" + this.hash + "]";
   }

   public int hashCode() {
      byte var2 = 1;
      int var3 = 31 * var2 + (this.fileName == null ? 0 : this.fileName.hashCode());
      var3 = 31 * var3 + (this.hash == null ? 0 : this.hash.hashCode());
      return var3;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         AnykCsatolmanyLenyomat var2 = (AnykCsatolmanyLenyomat)var1;
         if (this.fileName == null) {
            if (var2.fileName != null) {
               return false;
            }
         } else if (!this.fileName.equals(var2.fileName)) {
            return false;
         }

         if (this.hash == null) {
            if (var2.hash != null) {
               return false;
            }
         } else if (!this.hash.equals(var2.hash)) {
            return false;
         }

         return true;
      }
   }
}
