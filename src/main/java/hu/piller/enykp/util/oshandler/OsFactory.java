package hu.piller.enykp.util.oshandler;

import hu.piller.enykp.interfaces.IOsHandler;

public class OsFactory {
   public static final String OS_WINDOWS = "windows";
   public static final String OS_WINDOWS_98 = "98";
   public static final String OS_WINDOWS_ME = "me";
   public static final String OS_WINDOWS_NT = "nt";
   public static final String OS_WINDOWS_XP = "xp";
   public static final String OS_WINDOWS_VISTA = "vista";
   public static final String OS_WINDOWS_7 = " 7";
   public static final String OS_WINDOWS_8 = " 8";
   public static final String OS_WINDOWS_2000 = "2000";
   public static final String OS_WINDOWS_10 = "10";
   public static final String OS_WINDOWS_11 = "11";

   public static IOsHandler getOsHandler() {
      if (IOsHandler.OS_NAME.indexOf("linux") != -1) {
         return new DefaultUnixOsHandler();
      } else if (IOsHandler.OS_NAME.indexOf("windows") != -1) {
         if (IOsHandler.OS_NAME.indexOf("98") != -1) {
            return new Win9xOsHandler();
         } else if (IOsHandler.OS_NAME.indexOf("me") != -1) {
            return new WinMeOsHandler();
         } else if (IOsHandler.OS_NAME.indexOf("nt") != -1) {
            return new WinNTOsHandler();
         } else if (IOsHandler.OS_NAME.indexOf("vista") != -1) {
            return new WinVistaOsHandler();
         } else if (IOsHandler.OS_NAME.indexOf(" 7") != -1) {
            return new WinWin7OsHandler();
         } else if (IOsHandler.OS_NAME.indexOf(" 8") != -1) {
            return new WinWin7OsHandler();
         } else if (IOsHandler.OS_NAME.indexOf("10") != -1) {
            return new WinWin7OsHandler();
         } else {
            return (IOsHandler)(IOsHandler.OS_NAME.indexOf("11") != -1 ? new WinWin7OsHandler() : new DefaultWindowsOsHandler());
         }
      } else {
         return (IOsHandler)(IOsHandler.OS_NAME.indexOf("mac") != -1 ? new OtherUnixOsHandler() : new DefaultUnixOsHandler());
      }
   }
}
