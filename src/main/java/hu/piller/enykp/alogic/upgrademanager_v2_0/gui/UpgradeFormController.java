package hu.piller.enykp.alogic.upgrademanager_v2_0.gui;

import hu.piller.enykp.alogic.masterdata.sync.ui.entityfilter.SizeableCBRenderer;
import hu.piller.enykp.alogic.upgrademanager_v2_0.Directories;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeLogger;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeManager;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.DownloadableComponents;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.NewComponents;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.UpgradableComponents;
import hu.piller.enykp.alogic.upgrademanager_v2_0.lookupupgrades.LookUpUpgrades;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.installedcomponents.FrameSystemVersionDataProvider;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.base.SwingWorker;
import hu.piller.enykp.util.base.Tools;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public final class UpgradeFormController implements ActionListener {
   private static final int PANE_UPGRADABLE = 0;
   private static final int PANE_NEW_AND_UPGRADABLE = 1;
   private static final int DOWNLOADABLE_ALL = 2;
   private DownloadableComponents installable = null;
   private JUpgradeFormDialog form;
   private Boolean hasFrameworkUpgrade = null;
   private final DownloadableComponents[] componentsContainer = new DownloadableComponents[3];
   private VersionData framework = null;
   private JUpgradeStartInfoPanel timerPanel;
   private Timer timer;
   private ReentrantLock timerLock;
   private boolean is_finished;
   private String[] orgs;

   public UpgradeFormController(JUpgradeFormDialog var1, String[] var2) {
      this.form = var1;
      this.timerLock = new ReentrantLock();
      this.orgs = var2;
      this.setComponentContainerBaseElement();
      this.setComponentContainerComputedElements();
   }

   public String[] getOrgsNotConnected() {
      return this.componentsContainer[2].getOrgsNotConnected();
   }

   private void setComponentContainerBaseElement() {
      this.componentsContainer[2] = new DownloadableComponents(this.orgs);
   }

   private void setComponentContainerComputedElements() {
      this.componentsContainer[0] = new UpgradableComponents(this.componentsContainer[2]);
      this.componentsContainer[1] = new NewComponents(this.componentsContainer[2]);
   }

   public void selectAll() {
      int var1 = this.form.getVersionDataTabbedPane().getSelectedIndex();
      switch(var1) {
      case 0:
         ((UpgradeTableModel)this.form.getUpgradableTable().getModel()).selectAll();
         break;
      case 1:
         ((UpgradeTableModel)this.form.getNewTable().getModel()).selectAll();
      }

   }

   public void deselectAll() {
      int var1 = this.form.getVersionDataTabbedPane().getSelectedIndex();
      switch(var1) {
      case 0:
         ((UpgradeTableModel)this.form.getUpgradableTable().getModel()).deselectAll();
         break;
      case 1:
         ((UpgradeTableModel)this.form.getNewTable().getModel()).deselectAll();
      }

   }

   public void info() {
      JComponentInfoDialog var1 = null;
      VersionData var2 = null;
      int var4 = this.form.getVersionDataTabbedPane().getSelectedIndex();
      int var3;
      switch(var4) {
      case 0:
         var3 = this.form.getUpgradableTable().getSelectedRow();
         if (var3 < 0) {
            return;
         }

         var2 = ((UpgradeTableModel)this.form.getUpgradableTable().getModel()).getVersionDataByRowIdx(var3);
         break;
      case 1:
         var3 = this.form.getNewTable().getSelectedRow();
         if (var3 < 0) {
            return;
         }

         var2 = ((UpgradeTableModel)this.form.getNewTable().getModel()).getVersionDataByRowIdx(var3);
         break;
      default:
         return;
      }

      if (var2 != null) {
         var1 = new JComponentInfoDialog(var2);
         Point var5 = this.form.getLocation();
         var1.setLocation(new Point(var5.x + 160, var5.y + 90));
         var1.pack();
         var1.setVisible(true);
      }
   }

   public void interrupt() {
      this.installable.abortProcessing();
   }

   public void install() {
      if (this.isRestartNeeded()) {
         if (SwingUtilities.isEventDispatchThread()) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az új keretrendszer telepítésre vár!\nTovábbi frissítésre csak a keretrendszer újraindítás után lesz módja!", "Figyelem", 2);
         } else {
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az új keretrendszer telepítésre vár!\nTovábbi frissítésre csak a keretrendszer újraindítás után lesz módja!", "Figyelem", 2);
               }
            });
         }

      } else {
         this.disableAllButtons();
         final JInstallProgressDialog var1 = new JInstallProgressDialog(this);
         Point var2 = this.form.getLocation();
         var1.setLocation(new Point(var2.x + 100, var2.y + 60));
         var1.pack();
         SwingWorker var4 = new SwingWorker() {
            Vector<VersionData> markedToInstall = null;

            public Object construct() {
               UpgradeFormController.this.is_finished = false;
               UpgradeFormController.this.installable = null;
               this.markedToInstall = new Vector();
               this.markedToInstall.addAll(((UpgradeTableModel)UpgradeFormController.this.form.getUpgradableTable().getModel()).getVersionDataToInstall());
               this.markedToInstall.addAll(((UpgradeTableModel)UpgradeFormController.this.form.getNewTable().getModel()).getVersionDataToInstall());
               if (UpgradeFormController.this.hasFrameworkUpgrade() && UpgradeFormController.this.form.getBoxFrame().isSelected()) {
                  this.markedToInstall.add(UpgradeFormController.this.framework);
               }

               if (this.markedToInstall.size() == 0) {
                  return null;
               } else {
                  UpgradeFormController.this.installable = new DownloadableComponents(this.markedToInstall);
                  UpgradeFormController.this.installable.addComponentProcessedEventListener(var1);
                  SwingUtilities.invokeLater(new Runnable() {
                     public void run() {
                        var1.getProgressBar().setMinimum(0);
                        var1.getProgressBar().setMaximum(markedToInstall.size());
                        var1.getProgressBar().setValue(0);
                        var1.getProgressBar().setIndeterminate(false);
                        var1.getCloseButton().setEnabled(false);
                        var1.getErrorButton().setEnabled(false);
                        var1.setVisible(true);
                     }
                  });

                  try {
                     UpgradeManager.interruptLookUpUpgardes();
                     UpgradeFormController.this.installable.install();
                  } catch (Exception var5) {
                     UpgradeLogger.getInstance().log(var5);
                  } finally {
                     UpgradeFormController.this.timer = new Timer(5000, UpgradeFormController.this);
                     UpgradeFormController.this.timer.setRepeats(false);
                     UpgradeFormController.this.timer.start();
                     UpgradeFormController.this.setComponentContainerComputedElements();
                     (new LookUpUpgrades()).run();
                  }

                  return null;
               }
            }

            public void finished() {
               try {
                  UpgradeFormController.this.timerLock.lock();
                  UpgradeFormController.this.is_finished = true;
                  if (UpgradeFormController.this.timer != null) {
                     UpgradeFormController.this.timer.stop();
                  }

                  if (UpgradeFormController.this.timerPanel != null) {
                     UpgradeFormController.this.timerPanel.close();
                     UpgradeFormController.this.timerPanel = null;
                  }
               } finally {
                  UpgradeFormController.this.timerLock.unlock();
               }

               try {
                  if (UpgradeFormController.this.installable != null) {
                     UpgradeFormController.this.initPanes();
                     MainFrame.thisinstance.mp.removeFrissitesMessage();
                     MainFrame.thisinstance.mp.removeFrissitesButton();
                     if (UpgradeFormController.this.isUpgradeable()) {
                        MainFrame.thisinstance.mp.addFrissitesButton();
                     }

                     MainFrame.thisinstance.mp.updateUI();
                  }
               } finally {
                  var1.getCloseButton().setEnabled(true);
                  var1.getErrorButton().setEnabled(true);
                  var1.getAbortButton().setEnabled(false);
                  UpgradeFormController.this.enableAllButtons();
               }

            }
         };
         var4.start();
      }
   }

   private boolean isUpgradeable() {
      boolean var1 = false;
      if (!this.componentsContainer[0].getComponents().isEmpty()) {
         var1 = true;
         boolean var2 = false;

         for(int var3 = 0; var3 < this.componentsContainer[0].getComponents().size(); ++var3) {
            if (!((VersionData)((Vector)this.componentsContainer[0].getComponents()).get(var3)).getCategory().equals("Orgresource")) {
               var2 = true;
               break;
            }
         }

         if (!var2) {
            var1 = false;
         }

         if (!var1) {
            var1 = this.hasFrameworkUpgrade();
         }
      }

      return var1;
   }

   public void actionPerformed(ActionEvent var1) {
      try {
         this.timerLock.lock();
         if (!this.is_finished) {
            this.timerPanel = new JUpgradeStartInfoPanel("Az ÁNyK most újraépíti a verzió nyilvántartását, kérem várjon!", this.form);
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  try {
                     UpgradeFormController.this.timerLock.lock();
                     if (UpgradeFormController.this.timerPanel != null) {
                        UpgradeFormController.this.timerPanel.setVisible(true);
                     }
                  } finally {
                     UpgradeFormController.this.timerLock.unlock();
                  }

               }
            });
         }
      } finally {
         this.timerLock.unlock();
      }

   }

   public void initPanes() {
      this.form.createFrameworkStatusPanel();
      ((UpgradeTableModel)this.form.getUpgradableTable().getModel()).fillTable((Vector)this.componentsContainer[0].getComponents());
      ((UpgradeTableModel)this.form.getNewTable().getModel()).fillTable((Vector)this.componentsContainer[1].getComponents());
      this.form.getNewTable().getColumnModel().getColumn(this.form.getNewTable().getColumnModel().getColumnCount() - 2).setCellEditor(new SizeableCBRenderer());
      this.form.getNewTable().getColumnModel().getColumn(this.form.getNewTable().getColumnModel().getColumnCount() - 2).setCellRenderer(new SizeableCBRenderer());
      this.form.getUpgradableTable().getColumnModel().getColumn(this.form.getNewTable().getColumnModel().getColumnCount() - 2).setCellEditor(new SizeableCBRenderer());
      this.form.getUpgradableTable().getColumnModel().getColumn(this.form.getNewTable().getColumnModel().getColumnCount() - 2).setCellRenderer(new SizeableCBRenderer());
   }

   public boolean hasFrameworkUpgrade() {
      if (this.hasFrameworkUpgrade == null) {
         FrameSystemVersionDataProvider var1 = new FrameSystemVersionDataProvider();

         try {
            var1.collect();
            VersionData var2 = (VersionData)var1.getCollection().get(0);
            Iterator var3 = ((Vector)this.componentsContainer[0].getComponents()).iterator();

            while(var3.hasNext()) {
               VersionData var4 = (VersionData)var3.next();
               if (var4.greaterThan(var2)) {
                  this.framework = var4;
                  break;
               }
            }

            if (this.framework != null) {
               this.hasFrameworkUpgrade = Boolean.TRUE;
            } else {
               this.hasFrameworkUpgrade = Boolean.FALSE;
            }
         } catch (Exception var5) {
            Tools.eLog(var5, 0);
         }
      }

      return this.hasFrameworkUpgrade;
   }

   public boolean isRestartNeeded() {
      File var1 = new File(Directories.getUpgradePath() + File.separator + "abevjava_install.jar");
      return var1.exists();
   }

   private void disableAllButtons() {
      this.form.getBtnClose().setEnabled(false);
      this.form.getBtnComponentInfo().setEnabled(false);
      this.form.getBtnDeselectAll().setEnabled(false);
      this.form.getBtnInstall().setEnabled(false);
      this.form.getBtnSelectAll().setEnabled(false);
   }

   private void enableAllButtons() {
      this.form.getBtnClose().setEnabled(true);
      this.form.getBtnComponentInfo().setEnabled(true);
      this.form.getBtnDeselectAll().setEnabled(true);
      this.form.getBtnInstall().setEnabled(true);
      this.form.getBtnSelectAll().setEnabled(true);
   }
}
