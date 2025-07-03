package hu.piller.enykp.util.oshandler;

public class WinVistaOsHandler extends DefaultWindowsOsHandler {
   public String createApplicationDir() {
      return this.getEnvironmentVariable("public");
   }

   public String createProgramFilesDir() {
      return this.getEnvironmentVariable("ProgramFiles");
   }
}
