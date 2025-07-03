package hu.piller.enykp.alogic.archivemanager;

import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.ArchiveManagerPanel;
import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.abevfilechooser.ABEVArchivePanel;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.ICommandObject;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IEventLog;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.EventLog;
import hu.piller.enykp.util.base.PropertyList;
import me.necrocore.abevjava.NecroFile;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class ArchiveManager {
   public static final boolean debugOn = false;
   public static final String ENABLED_FILTERS = "LoadFilter";
   Boolean[] states;
   private String[] filters;
   private static String resource_name = "Archívum kezelő";
   private static ArchiveManagerDialogPanel amdp = null;
   private static IPropertyList enykUtils = null;

   public static IPropertyList getEnykUtils() {
      return enykUtils;
   }

   public ArchiveManager() {
      this.states = new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
      this.filters = new String[]{"inner_data_loader_v1"};
   }

   public ICommandObject getArchiveManagerCommandObject() {
      return new ArchiveManager.ArchiveManagerCO(new ArchiveManagerDialog());
   }

   public static IPropertyList getMasterPropertyList() {
      return PropertyList.getInstance();
   }

   public static String getArchiveRootPath() {
      return getPath("prop.usr.archive", "Sikertelen levéltár gyökér megszerzési művelet !");
   }

   public static String getSavesRootPath() {
      return getPath("prop.usr.saves", "Sikertelen mentés gyökér megszerzési művelet !");
   }

   public static String getPath(String var0, String var1) {
      try {
         IPropertyList var2 = getMasterPropertyList();
         if (var2 != null) {
            try {
               String var3 = (new NecroFile((String)var2.get("prop.usr.root"), (String)var2.get(var0))).toString();
               return var3 == null ? (new NecroFile(".")).getCanonicalPath() : var3;
            } catch (Exception var4) {
               writeError(var1, var4);
            }
         }
      } catch (Exception var5) {
         writeError("Sikertelen tulajdonság lista megszerzési művelet ! ", var5);
      }

      return ".";
   }

   public static IErrorList getErrorlist() {
      return ErrorList.getInstance();
   }

   public static IEventLog getEventLog() {
      try {
         return EventLog.getInstance();
      } catch (IOException var1) {
         return null;
      }
   }

   public static void writeError(String var0, Exception var1) {
      ErrorList.getInstance().writeError(new Long(0L), resource_name + ": " + var0, var1, (Object)null);
   }

   public static ArchiveManagerDialogPanel getAmdp() {
      return amdp;
   }

   private void showMessage(String var1) {
   }

   private class ArchiveManagerCO implements ICommandObject {
      private JDialog amd_d;
      private JFrame amd_f;
      private ArchiveManagerDialogPanel amd_p;
      private boolean sized = false;
      private Object[] updateSkin = new Object[]{"work_panel", "dynamic", "Adatok archiválása és visszatöltése", "arch_mgr", null};

      public ArchiveManagerCO(JDialog var2) {
         this.amd_d = var2;
      }

      public ArchiveManagerCO(JFrame var2) {
         this.amd_f = var2;
      }

      public ArchiveManagerCO(ArchiveManagerDialogPanel var2) {
         this.amd_p = var2;
      }

      public ArchiveManagerCO(ArchiveManagerDialog var2) {
         this.amd_d = var2;
         this.amd_p = var2.getAmdp();
      }

      public void execute() {
         if (this.amd_d != null) {
            if (!this.sized) {
               this.amd_d.setSize(800, 600);
               this.amd_d.setTitle(ArchiveManager.resource_name);
               this.sized = true;
            }

            this.amd_d.setResizable(true);
            this.amd_d.setLocationRelativeTo(MainFrame.thisinstance);
            this.setFilters();
            this.rescanFolders();
            this.amd_d.setVisible(true);
         }

      }

      private void rescanFolders() {
         ArchiveManagerPanel var1 = (ArchiveManagerPanel)this.amd_p.getAMDComponent("archive_panel");
         ABEVArchivePanel var2 = (ABEVArchivePanel)var1.getAMDComponent("chooser_panel");
         var2.getPanel().getFilePanel().getBusiness().rescan();
         ABEVArchivePanel var3 = (ABEVArchivePanel)var1.getAMDComponent("archive_panel");
         var3.getPanel().getFilePanel().getBusiness().rescan();
      }

      private void setFilters() {
         ArchiveManagerPanel var1 = (ArchiveManagerPanel)this.amd_p.getAMDComponent("archive_panel");
         ABEVArchivePanel var2 = (ABEVArchivePanel)var1.getAMDComponent("chooser_panel");
         var2.getPanel().getFilePanel().getBusiness().addFilters(ArchiveManager.this.filters, (String[])null);
         ABEVArchivePanel var3 = (ABEVArchivePanel)var1.getAMDComponent("archive_panel");
         var3.getPanel().getFilePanel().getBusiness().addFilters(ArchiveManager.this.filters, (String[])null);
      }

      public void setParameters(Hashtable var1) {
      }

      public ICommandObject copy() {
         return this.amd_f != null ? ArchiveManager.this.new ArchiveManagerCO(this.amd_f) : ArchiveManager.this.new ArchiveManagerCO(this.amd_d);
      }

      public boolean isCommandIdentical(String var1) {
         return false;
      }

      public Vector getCommandList() {
         return null;
      }

      public Hashtable getCommandHelps() {
         return null;
      }

      public Object getState(Object var1) {
         if (var1 instanceof Integer) {
            int var2 = (Integer)var1;
            return var2 >= 0 && var2 <= ArchiveManager.this.states.length - 1 ? ArchiveManager.this.states[var2] : Boolean.FALSE;
         } else {
            return Boolean.FALSE;
         }
      }
   }
}
