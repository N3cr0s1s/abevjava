package hu.piller.enykp.kauclient;

public enum KauAuthMethod {
   KAU_ALL("KAÜ portál használatával"),
   KAU_DAP("DÁP mobilalkalmazással (KAÜ-azonosítással háttérben)"),
   KAU_UKP("Ügyfélkapu+ hitelesítő alkalmazással (KAÜ-azonosítással háttérben)"),
   KAU_EML("Ügyfélkapu+ e-mailes kóddal (KAÜ-azonosítással háttérben)");

   private String text;

   private KauAuthMethod(String var3) {
      this.text = var3;
   }

   public String getText() {
      return this.text;
   }
}
