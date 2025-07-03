package hu.piller.krtitok;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class IniReadAndWrite {
   public static final String INI_MY_PRIVATE_KEY = "SAJAT_TITKOS_KULCS";
   public static final String INI_MY_PUBLIC_KEY = "SAJAT_NYILVANOS_KULCS";
   public static final String INI_MY_KEY_AUTOMATIC_USE = "SAJAT_AUTOMATIKUS";
   public static final String INI_MY_KR_DIR = "KRDIR";
   public static final String INI_MY_TITKOSITATLAN = "TITKOSITASRA_VARO";
   public static final String INI_MY_LETOLTOTT = "LETOLTOTT";
   public static final String INI_MY_KULDENDO = "KULDENDO";
   public static final String INI_MY_ELKULDOTT = "ELKULDOT";
   public static final String INI_PATH;
   private static Properties prop;

   static {
      INI_PATH = System.getProperty("user.home") + File.separator;
   }

   public void writeIni(String myPrivateKey, String myPublicKey, boolean myKeyAutomaticUse, String myKrDir, String myTitkositatlan, String myLetoltott, String myKuldendo, String myElkuldott) {
      try {
         FileOutputStream outstr = new FileOutputStream(INI_PATH + ".krtitok.ini");
         Properties prop = new Properties();

         try {
            prop.setProperty("SAJAT_TITKOS_KULCS", myPrivateKey);
            prop.setProperty("SAJAT_NYILVANOS_KULCS", myPublicKey);
            prop.setProperty("SAJAT_AUTOMATIKUS", Boolean.toString(myKeyAutomaticUse));
            prop.setProperty("KRDIR", myKrDir);
            prop.setProperty("TITKOSITASRA_VARO", myTitkositatlan);
            prop.setProperty("LETOLTOTT", myLetoltott);
            prop.setProperty("KULDENDO", myKuldendo);
            prop.setProperty("ELKULDOT", myElkuldott);
            prop.store(outstr, "Beállítások");
            outstr.close();
         } catch (IOException var12) {
            KriptoApp.logger.info("E6000");
         }
      } catch (FileNotFoundException var13) {
         KriptoApp.logger.info("E6000");
      }

   }

   public void writeIni(Properties props) {
      this.writeIni(props.getProperty("SAJAT_TITKOS_KULCS"), props.getProperty("SAJAT_NYILVANOS_KULCS"), Boolean.valueOf(props.getProperty("SAJAT_AUTOMATIKUS")), props.getProperty("KRDIR"), props.getProperty("TITKOSITASRA_VARO"), props.getProperty("LETOLTOTT"), props.getProperty("KULDENDO"), props.getProperty("ELKULDOT"));
   }

   public void readIni() {
      try {
         File f = new File(INI_PATH + ".krtitok.ini");
         if (f.exists()) {
            FileInputStream inpstr = new FileInputStream(INI_PATH + ".krtitok.ini");
            prop = new Properties();

            try {
               prop.load(inpstr);
               inpstr.close();
            } catch (IOException var4) {
               KriptoApp.logger.severe("E6000");
            }
         } else {
            KriptoApp.logger.severe("E6001");
         }
      } catch (FileNotFoundException var5) {
         KriptoApp.logger.severe("E6001");
      }

   }

   public static void IniPathVectorLoad() {
      IniReadAndWrite IRAndW = new IniReadAndWrite();
      IRAndW.readIni();
      prop = IRAndW.getProperties();
   }

   public Properties getProperties() {
      return prop;
   }
}
