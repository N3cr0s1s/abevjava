package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.bankszamlaszam;

import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.ITechnicalMdModel;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.handler.BankszamlaszamMdHandler;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BankszamlaszamModel implements ITechnicalMdModel {
   private String bankName = "";
   private String bankId = "";
   private String accountId = "";

   public String getBankName() {
      return this.bankName;
   }

   public void setBankName(String var1) {
      this.bankName = var1;
   }

   public String getAccountId() {
      return this.accountId;
   }

   public void setAccountId(String var1) {
      this.accountId = var1;
   }

   public String getBankId() {
      return this.bankId;
   }

   public void setBankId(String var1) {
      this.bankId = var1;
   }

   public void setValue(String var1) {
      BankszamlaszamMdHandler var2 = new BankszamlaszamMdHandler();
      Map var3 = var2.split(Arrays.asList(var1));
      this.bankName = (String)((List)var3.get("Pénzintézet neve")).get(0);
      this.bankId = (String)((List)var3.get("Pénzintézet-azonosító")).get(0);
      this.accountId = (String)((List)var3.get("Számla-azonosító")).get(0);
   }

   public String getValue() {
      return BankszamlaszamFormatter.buildFormattedBankAccount(this.bankName, this.bankId, this.accountId);
   }

   public String getDataError() {
      if (this.bankName.length() == 0 || this.bankId.length() != 0 && this.accountId.length() != 0) {
         if (this.bankName.length() == 0 && this.bankId.length() != 0 && this.accountId.length() != 0) {
            return "Kérem, adja meg a pénzintézet(fiók) nevét is!";
         } else if (this.bankId.length() > 0 && this.bankId.length() != 8) {
            return "A pénzforgalmi jelzőszám pénzintézet azonosítója pontosan nyolc számjegyből kell, hogy álljon!";
         } else if (this.accountId.replaceAll("-", "").length() > 0 && this.accountId.replaceAll("-", "").length() != 8 && this.accountId.replaceAll("-", "").length() != 16) {
            return "A pénzforgalmi jelzőszám számlatulajdonos azonosítója 8 vagy 16 számjegyből kell, hogy álljon!";
         } else if (this.bankId.length() == 8 && this.accountId.replaceAll("-", "").length() != 8 && this.accountId.replaceAll("-", "").length() != 16) {
            return "A pénzforgalmi jelzőszám számlatulajdonos azonosítót ki kell töltenie!";
         } else if (this.accountId.startsWith("-")) {
            return "A számlatulajdonos azonosítója 8 vagy 16 számjegy kell legyen. Ha 8 számjegy, akkor az első számcsoportot töltse ki!";
         } else {
            String[] var1 = this.accountId.split("-");
            return var1.length == 2 && var1[0] != null && var1[1] != null && var1[0].length() > 0 && var1[0].length() != 8 && var1[1].length() > 0 && var1[1].length() != 8 ? "A számlatulajdonos azonosító egy, vagy két 8 számjegyből álló számcsoportból kell hogy álljon!" : null;
         }
      } else {
         return "Kérem, töltse ki a számlaszám adatait is!";
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         BankszamlaszamModel var2 = (BankszamlaszamModel)var1;
         if (!this.accountId.equals(var2.accountId)) {
            return false;
         } else if (!this.bankId.equals(var2.bankId)) {
            return false;
         } else {
            return this.bankName.equals(var2.bankName);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.bankName.hashCode();
      var1 = 31 * var1 + this.bankId.hashCode();
      var1 = 31 * var1 + this.accountId.hashCode();
      return var1;
   }

   public String toString() {
      return "BankszamlaszamModel{bankName='" + this.bankName + '\'' + ", bankId='" + this.bankId + '\'' + ", accountId='" + this.accountId + '\'' + '}';
   }
}
