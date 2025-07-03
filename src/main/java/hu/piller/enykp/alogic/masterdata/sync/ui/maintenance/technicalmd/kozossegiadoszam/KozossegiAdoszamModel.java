package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.kozossegiadoszam;

import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.ITechnicalMdModel;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.handler.KozossegiAdoszamMdHandler;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class KozossegiAdoszamModel implements ITechnicalMdModel {
   private String countryCode = "";
   private String taxId = "";

   public String getCountryCode() {
      return this.countryCode;
   }

   public void setCountryCode(String var1) {
      this.countryCode = var1;
   }

   public String getTaxId() {
      return this.taxId;
   }

   public void setTaxId(String var1) {
      this.taxId = var1;
   }

   public void setValue(String var1) {
      KozossegiAdoszamMdHandler var2 = new KozossegiAdoszamMdHandler();
      Map var3 = var2.split(Arrays.asList(var1));
      if (!((List)var3.get("Közösségi adószám ország kód")).isEmpty()) {
         this.countryCode = (String)((List)var3.get("Közösségi adószám ország kód")).get(0);
      }

      if (!((List)var3.get("Közösségi adószám")).isEmpty()) {
         this.taxId = (String)((List)var3.get("Közösségi adószám")).get(0);
      }

   }

   public String getValue() {
      return this.countryCode + this.taxId;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         KozossegiAdoszamModel var2 = (KozossegiAdoszamModel)var1;
         if (!this.countryCode.equals(var2.countryCode)) {
            return false;
         } else {
            return this.taxId.equals(var2.taxId);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.countryCode.hashCode();
      var1 = 31 * var1 + this.taxId.hashCode();
      return var1;
   }

   public String toString() {
      return "BankszamlaszamModel{countryCode='" + this.countryCode + '\'' + ", taxId='" + this.taxId + '\'' + '}';
   }
}
