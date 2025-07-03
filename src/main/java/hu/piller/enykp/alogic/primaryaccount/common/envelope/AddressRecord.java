package hu.piller.enykp.alogic.primaryaccount.common.envelope;

import hu.piller.enykp.alogic.primaryaccount.common.DefaultRecord;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

public class AddressRecord extends DefaultRecord {
   private final Vector zips = new Vector(16, 16);
   private Object shire;
   private Vector<AddressRecord.ApehAddress> apehAddressOpt = new Vector();

   public AddressRecord(AddressRecordFactory var1, File var2, APEHEnvelope var3) {
      super(var1, var2, var3);
   }

   public String getName() {
      Object var1 = this.getData().get("title");
      Object var2 = this.getData().get("title_hint");
      String var3 = var1 == null ? "" : var1.toString();
      String var4 = var2 == null ? "" : var2.toString();
      return var4.length() == 0 ? var3 : var3 + " (" + var4 + ")";
   }

   public String toString() {
      return this.getName();
   }

   public void addApehAddress(Hashtable var1) {
      AddressRecord.ApehAddress var2 = new AddressRecord.ApehAddress(this, (String)var1.get("title"), (String)var1.get("title_hint"), (String)var1.get("settlement"), (String)var1.get("po_box"), (String)var1.get("zip"));
      this.apehAddressOpt.add(var2);
   }

   public Vector getApehAddress() {
      return this.apehAddressOpt;
   }

   public Vector getZips() {
      return this.zips;
   }

   public Object getShire() {
      return this.shire;
   }

   public void setShire(Object var1) {
      this.shire = var1;
   }

   public class ApehAddress {
      private String title;
      private String titleHint;
      private String settlement;
      private String poBox;
      private String zip;
      private AddressRecord container;

      public ApehAddress(AddressRecord var2, String var3, String var4, String var5, String var6, String var7) {
         this.container = var2;
         this.title = var3;
         this.titleHint = var4;
         this.settlement = var5;
         this.poBox = var6;
         this.zip = var7;
      }

      public AddressRecord getAddressRecord() {
         return this.container;
      }

      public Object getShire() {
         return this.container.getShire();
      }

      public String getTitle() {
         return this.title;
      }

      public String getTitleHint() {
         return this.titleHint;
      }

      public String getSettlement() {
         return this.settlement;
      }

      public String getPoBox() {
         return this.poBox;
      }

      public String getZip() {
         return this.zip;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            AddressRecord.ApehAddress var2 = (AddressRecord.ApehAddress)var1;
            if (this.poBox != null) {
               if (!this.poBox.equals(var2.poBox)) {
                  return false;
               }
            } else if (var2.poBox != null) {
               return false;
            }

            label62: {
               if (this.settlement != null) {
                  if (this.settlement.equals(var2.settlement)) {
                     break label62;
                  }
               } else if (var2.settlement == null) {
                  break label62;
               }

               return false;
            }

            label55: {
               if (this.title != null) {
                  if (this.title.equals(var2.title)) {
                     break label55;
                  }
               } else if (var2.title == null) {
                  break label55;
               }

               return false;
            }

            if (this.titleHint != null) {
               if (!this.titleHint.equals(var2.titleHint)) {
                  return false;
               }
            } else if (var2.titleHint != null) {
               return false;
            }

            if (this.zip != null) {
               if (!this.zip.equals(var2.zip)) {
                  return false;
               }
            } else if (var2.zip != null) {
               return false;
            }

            return true;
         } else {
            return false;
         }
      }

      public int hashCode() {
         int var1 = this.title != null ? this.title.hashCode() : 0;
         var1 = 29 * var1 + (this.titleHint != null ? this.titleHint.hashCode() : 0);
         var1 = 29 * var1 + (this.settlement != null ? this.settlement.hashCode() : 0);
         var1 = 29 * var1 + (this.poBox != null ? this.poBox.hashCode() : 0);
         var1 = 29 * var1 + (this.zip != null ? this.zip.hashCode() : 0);
         return var1;
      }

      public String toString() {
         return this.title + "(" + this.titleHint + ")";
      }
   }
}
