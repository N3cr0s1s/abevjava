package hu.piller.enykp.util.oshandler;

public class winWin7OsHandler extends defaultWindowsOsHandler {
   public String createApplicationDir() {
      return this.getEnvironmentVariable("public");
   }

   public String createProgramFilesDir() {
      return this.getEnvironmentVariable("ProgramFiles");
   }
}
