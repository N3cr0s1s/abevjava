package hu.piller.enykp.alogic.masterdata.envelope.model;

import java.util.ArrayList;
import java.util.Iterator;

public class Address {
   private ArrayList<AddressOpt> addressOpts = new ArrayList();
   private ArrayList<ZipRange> zipRanges = new ArrayList();
   private int shire;
   private int id;

   public void setId(int var1) {
      this.id = var1;
   }

   public int getId() {
      return this.id;
   }

   public ArrayList<AddressOpt> getAddressOpts() {
      return this.addressOpts;
   }

   public ArrayList<ZipRange> getZipRanges() {
      return this.zipRanges;
   }

   public int getShire() {
      return this.shire;
   }

   public void setShire(int var1) {
      this.shire = var1;
   }

   public boolean isForZip(int var1) {
      boolean var2 = false;
      Iterator var3 = this.zipRanges.iterator();

      while(var3.hasNext()) {
         ZipRange var4 = (ZipRange)var3.next();
         if (var4.isInRange(var1)) {
            var2 = true;
            break;
         }
      }

      return var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      Iterator var2 = this.addressOpts.iterator();

      while(var2.hasNext()) {
         AddressOpt var3 = (AddressOpt)var2.next();
         var1.append(var3.toString());
         var1.append("\n");
      }

      var2 = this.zipRanges.iterator();

      while(var2.hasNext()) {
         ZipRange var4 = (ZipRange)var2.next();
         var1.append(var4.toString());
         var1.append("\n");
      }

      var1.append("shire: ").append(this.shire).append("\n");
      return var1.toString();
   }
}
