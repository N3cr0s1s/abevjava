package hu.piller.enykp.alogic.masterdata.envelope.model;

import java.util.Arrays;

public class AddressOpt {
   public static final AddressOpt DUMMY = new AddressOpt();
   private String[] title = new String[]{""};
   private String titleHint = "";
   private String settlement = "";
   private String poBox = "";
   private String kozteruletCim = "";
   private int zip;

   public String[] getTitle() {
      return this.title;
   }

   public void setTitle(String var1) {
      this.title = var1.split("\\|");
   }

   public int getNumOfAddressParts() {
      return this.title.length;
   }

   public String getTitleHint() {
      return this.titleHint;
   }

   public void setTitleHint(String var1) {
      this.titleHint = var1;
   }

   public String getSettlement() {
      return this.settlement;
   }

   public void setSettlement(String var1) {
      this.settlement = var1;
   }

   public String getPoBox() {
      return this.poBox;
   }

   public void setPoBox(String var1) {
      this.poBox = var1;
   }

   public String getKozteruletCim() {
      return this.kozteruletCim;
   }

   public void setKozteruletCim(String var1) {
      this.kozteruletCim = var1;
   }

   public int getZip() {
      return this.zip;
   }

   public String getFormattedZip() {
      return this.zip <= 0 ? "" : Integer.toString(this.zip);
   }

   public void setZip(int var1) {
      this.zip = var1;
   }

   public boolean hasValidZip() {
      return this.zip >= 1000 && this.zip <= 9999;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = 0; var2 < this.title.length; ++var2) {
         var1.append(this.title[var2]);
         if (var2 < this.title.length - 1) {
            var1.append(",");
         }
      }

      var1.append("(").append(this.getTitleHint()).append(")");
      return var1.toString();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         AddressOpt var2 = (AddressOpt)var1;
         if (this.zip != var2.zip) {
            return false;
         } else {
            label64: {
               if (this.kozteruletCim != null) {
                  if (this.kozteruletCim.equals(var2.kozteruletCim)) {
                     break label64;
                  }
               } else if (var2.kozteruletCim == null) {
                  break label64;
               }

               return false;
            }

            if (this.poBox != null) {
               if (!this.poBox.equals(var2.poBox)) {
                  return false;
               }
            } else if (var2.poBox != null) {
               return false;
            }

            if (this.settlement != null) {
               if (!this.settlement.equals(var2.settlement)) {
                  return false;
               }
            } else if (var2.settlement != null) {
               return false;
            }

            if (!Arrays.equals(this.title, var2.title)) {
               return false;
            } else {
               if (this.titleHint != null) {
                  if (!this.titleHint.equals(var2.titleHint)) {
                     return false;
                  }
               } else if (var2.titleHint != null) {
                  return false;
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.titleHint != null ? this.titleHint.hashCode() : 0;
      var1 = 29 * var1 + (this.settlement != null ? this.settlement.hashCode() : 0);
      var1 = 29 * var1 + (this.poBox != null ? this.poBox.hashCode() : 0);
      var1 = 29 * var1 + (this.kozteruletCim != null ? this.kozteruletCim.hashCode() : 0);
      var1 = 29 * var1 + this.zip;
      return var1;
   }

   public boolean isFeladoPrintable() {
      return true;
   }

   public boolean isCimzettPrintable() {
      boolean var1 = this.title.length > 0 && this.title[0] != null && !"".equals(this.title[0].trim());
      boolean var2 = this.settlement != null && !"".equals(this.settlement.trim());
      boolean var3 = this.poBox != null && !"".equals(this.poBox.trim());
      boolean var4 = this.kozteruletCim != null && !"".equals(this.kozteruletCim.trim());
      return var1 && var2 && (var3 || var4) && this.hasValidZip();
   }
}
