package hu.piller.enykp.util.oshandler;

public class winNTOsHandler extends defaultWindowsOsHandler {
   protected String root_drive = null;
   protected String init_dir = null;

   public winNTOsHandler() {
      this.showDebugMessage(this.getClass().toString());
   }

   public String createInitDir() {
      if (this.init_dir == null) {
         this.init_dir = this.getEnvironmentVariable("windir");
      }

      return this.init_dir;
   }

   public String createApplicationDir() {
      return this.getRootDrive() + "Program Files";
   }

   public String createUserHomeDir() {
      return this.getRootDrive();
   }

   public String getRootDrive() {
      if (this.root_drive == null) {
         this.root_drive = this.createInitDir().substring(0, 1) + ":\\";
      }

      return this.root_drive;
   }

   public boolean canCreateDesktopIcon() {
      return false;
   }

   public boolean canCreateMenuItem() {
      return false;
   }
}
