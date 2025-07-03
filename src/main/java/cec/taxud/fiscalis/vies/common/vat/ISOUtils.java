package cec.taxud.fiscalis.vies.common.vat;

import java.util.HashMap;
import java.util.Map;

public class ISOUtils {
   static final Map isoCodes3166 = new HashMap();

   public static boolean isISO3166(String var0) {
      return isoCodes3166.containsValue(var0);
   }

   static {
      isoCodes3166.put("AT", "040");
      isoCodes3166.put("BE", "056");
      isoCodes3166.put("BG", "100");
      isoCodes3166.put("CY", "196");
      isoCodes3166.put("CZ", "203");
      isoCodes3166.put("DE", "276");
      isoCodes3166.put("DK", "208");
      isoCodes3166.put("EE", "233");
      isoCodes3166.put("EL", "300");
      isoCodes3166.put("ES", "724");
      isoCodes3166.put("FI", "246");
      isoCodes3166.put("FR", "250");
      isoCodes3166.put("GB", "826");
      isoCodes3166.put("HR", "191");
      isoCodes3166.put("HU", "348");
      isoCodes3166.put("IE", "372");
      isoCodes3166.put("IT", "380");
      isoCodes3166.put("LT", "440");
      isoCodes3166.put("LU", "442");
      isoCodes3166.put("LV", "428");
      isoCodes3166.put("MT", "470");
      isoCodes3166.put("NL", "528");
      isoCodes3166.put("PL", "616");
      isoCodes3166.put("PT", "620");
      isoCodes3166.put("RO", "642");
      isoCodes3166.put("SE", "752");
      isoCodes3166.put("SI", "705");
      isoCodes3166.put("SK", "703");
      isoCodes3166.put("XI", "900");
   }
}
