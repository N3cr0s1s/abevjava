package hu.piller.enykp.print.simpleprint;

public class PageTitle {
   private boolean oldModel;
   private String titleString;
   private KPrintPageType pageType;
   private KPrintFormFeedType pageBreak;
   private boolean landscape;

   public PageTitle(String var1, KPrintPageType var2, KPrintFormFeedType var3, boolean var4, String var5) {
      this.titleString = var1;
      this.pageType = var2;
      this.pageBreak = var3;
      this.landscape = var4;
   }

   public String getTitleString() {
      return this.titleString;
   }

   public KPrintPageType getPageType() {
      return this.pageType;
   }

   public KPrintFormFeedType getPageBreak() {
      return this.pageBreak;
   }

   public boolean isLandscape() {
      return this.landscape;
   }
}
