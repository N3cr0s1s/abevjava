package hu.piller.enykp.util;

import hu.piller.enykp.util.base.Version;

public final class JavaInfo {
   public static Version getVersion() {
      return new Version(System.getProperty("java.specification.version"));
   }

   public static boolean isJavaFxAvailable() {
      try {
         Class.forName("javafx.embed.swing.JFXPanel");
         return true;
      } catch (ClassNotFoundException var1) {
         return false;
      }
   }

   public static String getJavaFXRuntimeVersion() {
      return System.getProperty("javafx.runtime.version");
   }

   public static String getNoJFXMessage() {
      return " Az ön által használt java verzióval (" + System.getProperty("java.version") + ") nem használható a \"KAÜ portál használatával\" felhasználói azonosítás opció. Kérjük válasszon másik azonosítási módot a Szerviz -> Beállítások -> Működés fülön!";
   }

   public static String getNoJFXMessageLines() {
      return "Az ön által használt java verzióval (" + System.getProperty("java.version") + ") nem használható a \"KAÜ portál használatával\" felhasználói azonosítás opció.\nKérjük válasszon másik azonosítási módot a Szerviz -> Beállítások -> Működés fülön!";
   }

   public static String getJFXSettingsMessage() {
      return "Az ön által használt java verzióval (" + System.getProperty("java.version") + ") nem használható ez az opció.\nÍgy nem tudja majd használni az azonosítást igénylő funkciókat!\nEnnek ellenére beállítja?";
   }
}
