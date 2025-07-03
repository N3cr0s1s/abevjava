package cec.taxud.fiscalis.vies.common;

import cec.taxud.fiscalis.vies.common.vat.CheckVat_AT;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_BE;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_BG;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_CY;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_CZ;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_DE;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_DK;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_EE;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_EL;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_ES;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_EU;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_FI;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_FR;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_GB;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_HR;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_HU;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_IE;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_IM;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_IT;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_LT;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_LU;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_LV;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_MT;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_NL;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_PL;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_PT;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_RO;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_SE;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_SI;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_SK;
import cec.taxud.fiscalis.vies.common.vat.CheckVat_VM;
import cec.taxud.fiscalis.vies.common.vat.ValidationRoutine;
import java.util.HashMap;
import java.util.Map;

public class VATValidation {
   static String sccsid = "@(#)VATValidation.java 8.0.0 2005/06/21";
   static final Map memberStateVatValidationRoutines = new HashMap();

   public static boolean check(String var0, String var1) {
      String var2 = var1.trim();
      ValidationRoutine var3 = (ValidationRoutine)memberStateVatValidationRoutines.get(var2);
      return var3 != null ? var3.check(var0) : false;
   }

   public static void main(String[] var0) {
      System.out.println(check(var0[1], var0[0]));
   }

   static {
      memberStateVatValidationRoutines.put("BE", new CheckVat_BE());
      memberStateVatValidationRoutines.put("DE", new CheckVat_DE());
      memberStateVatValidationRoutines.put("DK", new CheckVat_DK());
      memberStateVatValidationRoutines.put("ES", new CheckVat_ES());
      memberStateVatValidationRoutines.put("FR", new CheckVat_FR());
      memberStateVatValidationRoutines.put("GB", new CheckVat_GB());
      memberStateVatValidationRoutines.put("XI", new CheckVat_GB());
      memberStateVatValidationRoutines.put("EL", new CheckVat_EL());
      memberStateVatValidationRoutines.put("IE", new CheckVat_IE());
      memberStateVatValidationRoutines.put("IT", new CheckVat_IT());
      memberStateVatValidationRoutines.put("LU", new CheckVat_LU());
      memberStateVatValidationRoutines.put("NL", new CheckVat_NL());
      memberStateVatValidationRoutines.put("PT", new CheckVat_PT());
      memberStateVatValidationRoutines.put("VM", new CheckVat_VM());
      memberStateVatValidationRoutines.put("AT", new CheckVat_AT());
      memberStateVatValidationRoutines.put("FI", new CheckVat_FI());
      memberStateVatValidationRoutines.put("SE", new CheckVat_SE());
      memberStateVatValidationRoutines.put("HR", new CheckVat_HR());
      memberStateVatValidationRoutines.put("HU", new CheckVat_HU());
      memberStateVatValidationRoutines.put("EE", new CheckVat_EE());
      memberStateVatValidationRoutines.put("SI", new CheckVat_SI());
      memberStateVatValidationRoutines.put("LV", new CheckVat_LV());
      memberStateVatValidationRoutines.put("MT", new CheckVat_MT());
      memberStateVatValidationRoutines.put("LT", new CheckVat_LT());
      memberStateVatValidationRoutines.put("PL", new CheckVat_PL());
      memberStateVatValidationRoutines.put("SK", new CheckVat_SK());
      memberStateVatValidationRoutines.put("BG", new CheckVat_BG());
      memberStateVatValidationRoutines.put("RO", new CheckVat_RO());
      memberStateVatValidationRoutines.put("CY", new CheckVat_CY());
      memberStateVatValidationRoutines.put("CZ", new CheckVat_CZ());
      memberStateVatValidationRoutines.put("EU", new CheckVat_EU());
      memberStateVatValidationRoutines.put("IM", new CheckVat_IM());
   }
}
