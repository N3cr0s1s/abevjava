package hu.piller.enykp.niszws.obhservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(
   name = "signerTypeEnum"
)
@XmlEnum
public enum SignerTypeEnum {
   @XmlEnumValue("ASiC")
   A_SI_C("ASiC"),
   @XmlEnumValue("PAdES")
   P_AD_ES("PAdES"),
   @XmlEnumValue("XAdES")
   X_AD_ES("XAdES");

   private final String value;

   private SignerTypeEnum(String var3) {
      this.value = var3;
   }

   public String value() {
      return this.value;
   }

   public static SignerTypeEnum fromValue(String var0) {
      SignerTypeEnum[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SignerTypeEnum var4 = var1[var3];
         if (var4.value.equals(var0)) {
            return var4;
         }
      }

      throw new IllegalArgumentException(var0);
   }
}
