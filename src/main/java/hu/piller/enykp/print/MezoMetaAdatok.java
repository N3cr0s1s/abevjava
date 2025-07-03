package hu.piller.enykp.print;

public class MezoMetaAdatok {
   public static boolean print_on_flp = false;
   public static boolean fill_on_lp = false;
   public static boolean fill_on_fp = false;
   public static boolean copy_fld = false;
   public static boolean szamitott = false;
   public static boolean office_fill = false;
   public static boolean notinbarkod = false;
   public static boolean notinbrhead = false;
   public static boolean DPageNumber = false;
   private static MezoMetaAdatok ourInstance = new MezoMetaAdatok();

   public static MezoMetaAdatok getInstance() {
      init();
      return ourInstance;
   }

   private MezoMetaAdatok() {
   }

   private static void init() {
      fill_on_fp = false;
      fill_on_lp = false;
      print_on_flp = false;
      copy_fld = false;
      szamitott = false;
      office_fill = false;
      notinbarkod = false;
      notinbrhead = false;
      DPageNumber = false;
   }
}
