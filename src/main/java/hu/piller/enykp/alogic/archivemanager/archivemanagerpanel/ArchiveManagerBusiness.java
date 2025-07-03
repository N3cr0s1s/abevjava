package hu.piller.enykp.alogic.archivemanager.archivemanagerpanel;

import hu.piller.enykp.alogic.archivemanager.ArchiveManager;
import hu.piller.enykp.alogic.archivemanager.ArchiveManagerDialog;
import hu.piller.enykp.alogic.archivemanager.ArchiveManagerDialogPanel;
import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.ExecHandler.ProgressPanel;
import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.ExecHandler.ThreadRunner;
import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.abevfilechooser.ABEVArchivePanel;
import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.archivefilepanel.filepanel.ListItem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.util.base.eventsupport.CloseEvent;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Hashtable;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public class ArchiveManagerBusiness implements IReport {
   public static final String MSG_CONFIRM_EXECUTE = "Végrehajtás megerősítése";
   public static final String MSG_CONFIRM_ARCHIVE = "Indítja az archiválást?";
   public static final String MSG_CONFIRM_RELOAD = "Indítja a visszatöltést?";
   public static final String MSG_CONFIRM_COPY = "Indítja a másolást?";
   private ArchiveManagerPanel amp;
   private ABEVArchivePanel chooser_panel;
   private ArchiveFileBusiness fbcp;
   private ABEVArchivePanel archive_panel;
   private ArchiveFileBusiness fbap;
   private JButton btn_mark_all_file;
   private JButton btn_unmark_all_file;
   private JButton btn_copy_file;
   private JButton btn_cancel;
   private JButton btn_exit;
   private JProgressBar pball;
   private JProgressBar pbrun;
   private String ArchiveRootPath;
   private String SavesRootPath;
   private ArchiveManagerDialogPanel amdp;
   private ArchiveManagerDialog amd;
   private boolean backGroundActive = false;
   private ThreadRunner threadRunner;
   private ProgressPanel pbp;
   private ErrorHandler errorhandler;
   private int pb = 0;
   private boolean isArchiveProcessing;

   ArchiveManagerBusiness(ArchiveManagerPanel var1, ArchiveManagerDialogPanel var2, ArchiveManagerDialog var3) {
      this.amp = var1;
      this.amdp = var2;
      this.amd = var3;
      this.errorhandler = new ErrorHandler();
      if (var1 != null) {
         this.prepare();
      }

      var2.setAmb(this);
   }

   private void prepare() {
      this.initThreadRunner();
      this.chooser_panel = (ABEVArchivePanel)this.amp.getAMDComponent("chooser_panel");
      this.fbcp = this.chooser_panel.getFileChooser().getFilePanel().getBusiness();
      this.archive_panel = (ABEVArchivePanel)this.amp.getAMDComponent("archive_panel");
      this.fbap = this.archive_panel.getFileChooser().getFilePanel().getBusiness();
      this.btn_cancel = (JButton)this.amdp.getAMDComponent("stop_button");
      this.btn_exit = (JButton)this.amdp.getAMDComponent("exit_button");
      this.btn_mark_all_file = (JButton)this.amp.getAMDComponent("mark_all_file_button");
      this.btn_unmark_all_file = (JButton)this.amp.getAMDComponent("unmark_all_file_button");
      this.btn_copy_file = (JButton)this.amp.getAMDComponent("copy_file_button");
      this.ArchiveRootPath = ArchiveManager.getArchiveRootPath();
      this.SavesRootPath = ArchiveManager.getSavesRootPath();
      this.amd.setDefaultCloseOperation(0);
      this.btn_cancel.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ArchiveManagerBusiness.this.stop();
         }
      });
      this.btn_exit.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ArchiveManagerBusiness.this.closeArchiveManager();
            ArchiveManagerBusiness.this.amdp.fireEvent(new CloseEvent(ArchiveManagerBusiness.this.amdp));
            ArchiveManagerBusiness.this.amd.setVisible(false);
         }
      });
      this.btn_mark_all_file.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (ArchiveManagerBusiness.this.ArchiveRootPath == null) {
               ArchiveManagerBusiness.viewMessage(ArchiveManagerBusiness.this.amdp, "Nincs beállítva az arhívum könyvtára, futtassa a telepítőt a felhasználói beállításokkal! ", true);
            } else {
               ArchiveManagerBusiness.this.putArchive();
            }
         }
      });
      this.btn_unmark_all_file.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (ArchiveManagerBusiness.this.ArchiveRootPath == null) {
               ArchiveManagerBusiness.viewMessage(ArchiveManagerBusiness.this.amdp, "Nincs beállítva az arhívum könyvtára, futtassa a telepítőt a felhasználói beállításokkal! ", true);
            } else {
               ArchiveManagerBusiness.this.getArchive();
            }
         }
      });
      this.initThreadRunner();
   }

   private void reLoadFileInfo() {
      this.chooser_panel.getPanel().getFilePanel().getBusiness().reLoadFileInfo();
      this.archive_panel.getPanel().getFilePanel().getBusiness().reLoadFileInfo();
   }

   public void stopOperation() {
      if (this.isBackGroundActive()) {
         this.stop();
      }

      this.closeArchiveManager();
   }

   private void closeArchiveManager() {
      this.fbap.saveFilterSettings("archive_archive_panel");
      this.fbcp.saveFilterSettings("archive_select_panel");
      this.initFBp(this.fbap);
      this.initFBp(this.fbcp);
   }

   private void initFBp(ArchiveFileBusiness var1) {
      var1.initQuery();
   }

   private boolean confirm(String var1, String var2) {
      return JOptionPane.showOptionDialog(this.amp, var2, var1, 0, 3, (Icon)null, (Object[])null, (Object)null) == 0;
   }

   private void initThreadRunner() {
      this.threadRunner = new ThreadRunner(this.errorhandler);
      this.pbp = (ProgressPanel)this.amdp.getAMDComponent("progress_panel");
      this.pbp.setVisible(true);
      this.pball = (JProgressBar)this.pbp.getPBComponent("pb_all");
      this.threadRunner.initProgressBarAll(this.pball);
      this.pbrun = (JProgressBar)this.pbp.getPBComponent("pb_run");
      this.threadRunner.initProgressBarRun(this.pbrun);
   }

   public void stop() {
      try {
         this.threadRunner.cancel();
      } catch (Exception var2) {
         viewMessage(this.amp, var2.getMessage(), false);
      }

   }

   private void putArchive() {
      try {
         this.isArchiveProcessing = true;
         Hashtable var1 = new Hashtable();
         if (this.fbcp.getSelectedRows() == 0) {
            viewMessage(this.amdp, "Válassza ki a listából a megfelelő elemeket!", true);
            return;
         }

         if (!this.confirm("Végrehajtás megerősítése", "Indítja az archiválást?")) {
            return;
         }

         this.amp.repaint();
         this.setButtonsEnabled(false);
         this.fbap.initQuery();
         var1.put("length", new Integer(this.fbcp.getSelectedRows()));
         var1.put("runable", new Archiver(this.errorhandler));
         var1.put("source", this.fbcp.getSelectedFiles());
         var1.put("destpath", this.ArchiveRootPath);
         var1.put("function", "archiv");
         var1.put("component", this.amdp);
         this.threadRunner.setMainReport(this);
         this.threadRunner.setRunParams(var1);
         this.setBackGroundActive(true);
         this.pball.setString((String)null);
         this.threadRunner.run();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   private void getArchive() {
      try {
         this.isArchiveProcessing = false;
         Hashtable var1 = new Hashtable();
         if (this.fbap.getSelectedRows() == 0) {
            viewMessage(this.amdp, "Válassza ki a listából a megfelelő elemeket!", true);
            return;
         }

         if (!this.confirm("Végrehajtás megerősítése", "Indítja a visszatöltést?")) {
            return;
         }

         this.setButtonsEnabled(false);
         this.amp.repaint();
         this.fbcp.initQuery();
         var1.put("length", new Integer(this.fbap.getSelectedRows()));
         var1.put("runable", new Archiver(this.errorhandler));
         var1.put("source", this.fbap.getSelectedFiles());
         var1.put("destpath", this.SavesRootPath);
         var1.put("function", "reload");
         var1.put("component", this.amdp);
         this.threadRunner.setMainReport(this);
         this.threadRunner.setRunParams(var1);
         this.setBackGroundActive(true);
         this.pball.setString((String)null);
         this.threadRunner.run();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public boolean isBackGroundActive() {
      return this.backGroundActive;
   }

   public void setBackGroundActive(boolean var1) {
      this.backGroundActive = var1;
   }

   public static void viewMessage(Component var0, String var1, boolean var2) {
      byte var3 = 0;
      if (var2) {
         var3 = 1;
      }

      GuiUtil.showMessageDialog(var0, var1, "Üzenet", var3);
   }

   public Object[] setResult(Object[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         Object[] var3 = (Object[])((Object[])var1[var2]);
         Object[] var4 = this.applyResult(var3);
         this.amp.revalidate();
         this.amp.repaint();
         if (var4 != null) {
            return var4;
         }
      }

      return null;
   }

   public void sendEnd() {
      this.reLoadFileInfo();
   }

   public Object[] applyResult(Object[] var1) {
      int var2 = (Integer)var1[0];
      switch(var2) {
      case 1:
         return this.startData(var1);
      case 2:
         return this.endData(var1);
      case 3:
         return this.getQuestion(var1);
      case 4:
         return this.endThread(var1);
      default:
         return null;
      }
   }

   public Object[] startData(Object[] var1) {
      return null;
   }

   public Object[] endData(Object[] var1) {
      if (this.pball != null) {
         this.pball.setValue(++this.pb);
      }

      ListItem var2 = (ListItem)var1[3];
      boolean var3 = (Boolean)var1[1];
      String var4 = (String)var1[2];
      String var5 = (String)var1[5];
      String var6 = (String)var1[4];

      try {
         if (!var3) {
            viewMessage(this.amdp, var4, false);
            return null;
         }

         ListItem var7 = (ListItem)var2.clone();
         var7.setItem(new File(var5));
         if (var6.compareTo("archiv") == 0) {
            this.fbcp.removeItem(var2);
            this.fbap.addNewFile(var7);
         } else if (var6.compareTo("reload") == 0) {
            this.fbap.removeItem(var2);
            this.fbcp.addNewFile(var7);
         } else if (var6.compareTo("copy") == 0) {
            this.fbap.removeSelectFlag(var2);
            this.fbcp.addNewFile(var7);
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      return null;
   }

   public Object[] endThread(Object[] var1) {
      if (this.pball != null) {
         this.pball.setValue(this.pb = 0);
         this.pball.setString("");
      }

      if (this.pbrun != null) {
         this.pbrun.setIndeterminate(false);
      }

      boolean var2 = (Boolean)var1[1];
      String var3 = (String)var1[2];
      if (!var2) {
         this.errorhandler.errAdmin("9065", "Hiba az arhiválás során! (" + var3 + ")", (Exception)null, (Object)null);
         viewMessage(this.amdp, var3, false);
      }

      this.setButtonsEnabled(true);
      this.setBackGroundActive(false);
      this.amp.revalidate();
      this.amp.repaint();
      Object[] var4 = new Object[]{new Integer(0), null};
      this.fbap.rescan();
      this.fbcp.rescan();
      return var4;
   }

   public Object[] getQuestion(Object[] var1) {
      Object[] var2 = (Object[])((Object[])var1[3]);
      Object[] var3 = new Object[]{new Integer(1), null};
      int var4 = JOptionPane.showOptionDialog(this.amdp, var2[0], (String)var2[1], 0, 3, (Icon)null, (Object[])null, (Object)null);
      var3[1] = new Integer(var4);
      this.amp.revalidate();
      this.amp.repaint();
      return var3;
   }

   public void setButtonsEnabled(boolean var1) {
      this.btn_mark_all_file.setEnabled(var1);
      this.btn_unmark_all_file.setEnabled(var1);
      this.fbcp.setEnabled(var1);
      this.fbap.setEnabled(var1);
      this.btn_cancel.setEnabled(!var1);
      this.btn_exit.setEnabled(var1);
   }
}
