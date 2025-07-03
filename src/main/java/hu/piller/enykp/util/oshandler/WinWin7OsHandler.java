package hu.piller.enykp.util.oshandler;

public class WinWin7OsHandler extends DefaultWindowsOsHandler {
   public String createApplicationDir() {
      return this.getEnvironmentVariable("public");
   }

   public String createProgramFilesDir() {
      return this.getEnvironmentVariable("ProgramFiles");
   }
}
