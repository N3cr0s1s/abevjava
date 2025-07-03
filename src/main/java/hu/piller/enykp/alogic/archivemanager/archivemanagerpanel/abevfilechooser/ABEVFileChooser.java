package hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.abevfilechooser;

import hu.piller.enykp.interfaces.IFileChooser;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.EventLog;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.eventsupport.DefaultEventSupport;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Vector;
import javax.swing.JPanel;

public class ABEVFileChooser implements IFileChooser, IEventSupport {
   static final String RESOURCE_NAME = "ABEV Állomány Kiválasztó";
   static final Long RESOURCE_ERROR_ID = new Long(0L);
   private static ABEVFileChooser this_;
   private JPanel panel;
   private IFileChooser file_chooser;
   private final DefaultEventSupport des = new DefaultEventSupport();

   public void addEventListener(IEventListener var1) {
      this.des.addEventListener(var1);
   }

   public void removeEventListener(IEventListener var1) {
      this.des.removeEventListener(var1);
   }

   public Vector fireEvent(Event var1) {
      return this.des.fireEvent(var1);
   }

   public ABEVFileChooser() {
      this_ = this;
   }

   public void beforeStart() {
      this.des.fireEvent(this, "update", "before_start");
   }

   public void afterStart() {
      this.des.fireEvent(this, "update", "after_start");
   }

   public void setSelectedFiles(File[] var1) {
      if (this.file_chooser != null) {
         this.file_chooser.setSelectedFiles(var1);
      }

   }

   public Object[] getSelectedFiles() {
      return this.file_chooser != null ? this.file_chooser.getSelectedFiles() : new Object[0];
   }

   public void setSelectedFilters(String[] var1) {
      if (this.file_chooser != null) {
         this.file_chooser.setSelectedFilters(var1);
      }

   }

   public String[] getSelectedFilters() {
      return this.file_chooser != null ? this.file_chooser.getSelectedFilters() : new String[0];
   }

   public String[] getAllFilters() {
      return this.file_chooser != null ? this.file_chooser.getAllFilters() : new String[0];
   }

   public void addFilters(String[] var1, String[] var2) {
      if (this.file_chooser != null) {
         this.file_chooser.addFilters(var1, var2);
      }

   }

   public void removeFilters(String[] var1) {
      if (this.file_chooser != null) {
         this.file_chooser.removeFilters(var1);
      }

   }

   public void rescan() {
      if (this.file_chooser != null) {
         this.file_chooser.rescan();
      }

   }

   public void hideFilters(String[] var1) {
   }

   public void showFilters(String[] var1) {
   }

   public void setSelectedPath(URI var1) {
   }

   public JPanel getPanel() {
      return this.panel;
   }

   public static IPropertyList getMasterPropertyList() {
      return PropertyList.getInstance();
   }

   public static void writeError(String var0, Exception var1) {
      ErrorList.getInstance().writeError(RESOURCE_ERROR_ID, "ABEV Állomány Kiválasztó: " + var0, var1, (Object)null);
   }

   public static void writeLog(Object var0) {
      try {
         EventLog.getInstance().writeLog("ABEV Állomány Kiválasztó: " + (var0 == null ? "" : var0));
      } catch (IOException var2) {
      }
   }
}
