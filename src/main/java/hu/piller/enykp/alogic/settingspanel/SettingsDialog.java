package hu.piller.enykp.alogic.settingspanel;

import hu.piller.enykp.alogic.masterdata.sync.configuration.ConfigService;
import hu.piller.enykp.alogic.settingspanel.attachement.AttachementSettings;
import hu.piller.enykp.alogic.settingspanel.printer.PrinterSettings;
import hu.piller.enykp.alogic.settingspanel.proxy.ProxyPanel;
import hu.piller.enykp.alogic.settingspanel.syncconfig.controller.SyncConfigController;
import hu.piller.enykp.alogic.settingspanel.syncconfig.model.SyncConfigModel;
import hu.piller.enykp.alogic.settingspanel.syncconfig.view.JSyncConfigView;
import hu.piller.enykp.alogic.settingspanel.upgrade.UpgradePanel;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

public class SettingsDialog extends JDialog {
   private JTabbedPane tp;
   private JPanel buttonPanel;
   private JButton btn_ok;
   public static SettingsDialog thisinstance;
   private ProxyPanel proxyPanel;
   private SyncConfigController syncConfigController;
   public static final int PNL_PROXY = 5;

   public SettingsDialog() {
      super(MainFrame.thisinstance, true);
      thisinstance = this;
      this.build();
      this.prepare();
   }

   private void build() {
      this.setSize(GuiUtil.getSettingsDialogDimension());
      this.setTitle("Beállítások");
      this.setResizable(true);
      this.getContentPane().setLayout(new BorderLayout());
      this.getContentPane().add(this.createTabbedPanel(), "Center");
      this.tp.setName("Beallitasok");
      this.tp.addTab("Működés", new BaseSettingsPane());
      this.tp.addTab("Megjelenés", new DisplayPane());
      this.tp.addTab("File maszkolás", new SettingsPanel());
      this.tp.addTab("Nyomtatás", new PrinterSettings());
      this.tp.addTab("Csatolmányok", new AttachementSettings());
      this.tp.addTab("Frissítés", new UpgradePanel());
      this.proxyPanel = new ProxyPanel(this);
      this.tp.addTab("Internetkapcsolat", this.proxyPanel);
      this.tp.addTab("Törzsadat szinkronizálás", this.createSyncConfig());
      this.getContentPane().add(this.createButtonPanel(), "South");
   }

   private JSyncConfigView createSyncConfig() {
      Color var1 = (Color)UIManager.get("TabbedPane.selected");
      Insets var2 = (Insets)UIManager.get("TabbedPane.contentBorderInsets");
      UIManager.put("TabbedPane.selected", new Color(255, 255, 178));
      UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
      JSyncConfigView var3 = new JSyncConfigView();
      this.syncConfigController = new SyncConfigController();
      SyncConfigModel var4 = new SyncConfigModel();
      this.syncConfigController.setModel(var4);
      this.syncConfigController.setView(var3);
      this.syncConfigController.setConfigService(new ConfigService());
      var4.addObserver(var3);
      var3.asObservable().addObserver(this.syncConfigController);
      this.syncConfigController.afterStart();
      UIManager.put("TabbedPane.selected", var1);
      UIManager.put("TabbedPane.contentBorderInsets", var2);
      return var3;
   }

   public boolean isPanelSelected(int var1) {
      return this.tp.getSelectedIndex() == var1;
   }

   public void jumpPanel(int var1) {
      this.tp.setSelectedIndex(var1);
   }

   private JPanel createTabbedPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new BorderLayout());
      this.tp = new JTabbedPane();
      var1.add(this.tp, "Center");
      return var1;
   }

   private JPanel createButtonPanel() {
      if (this.buttonPanel == null) {
         this.buttonPanel = new JPanel();
         this.buttonPanel.add(this.getBtn_ok());
      }

      return this.buttonPanel;
   }

   private JButton getBtn_ok() {
      if (this.btn_ok == null) {
         this.btn_ok = new JButton();
         this.btn_ok.setText("OK");
      }

      return this.btn_ok;
   }

   private void prepare() {
      this.btn_ok.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (SettingsDialog.this.syncConfigController != null) {
               SettingsDialog.this.syncConfigController.update((Observable)null, (Object)null);
            }

            String var2 = ((BaseSettingsPane)SettingsDialog.this.tp.getComponentAt(0)).saveSetenv();
            if (var2 != null) {
               if (var2.charAt(0) == 'X') {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, var2.substring(1), "Érvénytelen memóriaérték", 0);
                  return;
               }

               String var3 = "";
               if (((BaseSettingsPane)SettingsDialog.this.tp.getComponentAt(0)).isSuspiciousMax()) {
                  var3 = "Egyes operációs rendszreknél problémát okozhat, ha túl nagy értéket ad meg a memória beállítások maximális értékének.\nEzért, ha a beállítás után is memória problémát észlel, a maximum értéket maximálisan 1024Mb-ra állítsa!\n\n";
               }

               if ("SAVED".equals(var2)) {
                  if (((BaseSettingsPane)SettingsDialog.this.tp.getComponentAt(0)).hasMemChanged()) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, var3 + "A megváltoztatott memória értékek életbeléptetéséhez indítsa újra az ÁNyK-t!", "Működési paraméter megváltozása", 1);
                  }
               } else {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, var2.substring(1), var3 + "Memória beállítások mentése", 0);
               }
            }

            if (SettingsDialog.this.proxyPanel.save()) {
               SettingsDialog.this.setVisible(false);
               SettingsDialog.this.dispose();
            }

         }
      });
   }
}
