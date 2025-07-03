package hu.piller.enykp.alogic.filepanels;

import hu.piller.enykp.alogic.filepanels.browserpanel.BrowserPanel;
import hu.piller.enykp.alogic.filepanels.filepanel.FileTable;
import hu.piller.enykp.alogic.masterdata.gui.selector.SelectorHandler;
import hu.piller.enykp.alogic.masterdata.gui.selector.SelectorPanel;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.alogic.settingspanel.upgrade.UpgradeFunction;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeManager;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.SwingWorker;
import hu.piller.enykp.util.icon.ENYKIconSet;
import me.necrocore.abevjava.NecroFile;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MyABEVNewPanel extends ABEVNewPanel {
   private SelectorPanel selectorPanel;
   private String orgID = "";
   private Dimension loadedDimension = null;
   private Point loadedStartingPoint = null;

   public MyABEVNewPanel() {
      super((Hashtable)null);
   }

   protected JPanel getBrowserPanel() {
      if (this.panel == null) {
         this.panel = new BrowserPanel();
         this.panel.setAlignmentX(0.0F);
         this.panel.getFilePanel().getBusiness().setTask(1);
         this.panel.getFilePanel().getBusiness().addEventListener(this);
      }

      ListSelectionModel var1 = ((JTable)this.panel.getFilePanel().getFPComponent("files")).getSelectionModel();
      if (var1 != null) {
         var1.setSelectionMode(0);
         var1.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent var1) {
               FileTable var2 = (FileTable)MyABEVNewPanel.this.panel.getFilePanel().getFPComponent("files");
               if (var2 != null && var2.getSelectedRow() < var2.getModel().getRowCount() && var2.getSelectedRow() >= 0) {
                  MyABEVNewPanel.this.orgID = OrgInfo.getInstance().getOrgIDByOrgShortname((String)var2.getModel().getValueAt(var2.getSelectedRow(), 13));
                  if (MyABEVNewPanel.this.isValidOrg(MyABEVNewPanel.this.orgID)) {
                     MyABEVNewPanel.this.selectorPanel.showOrgPanel(MyABEVNewPanel.this.orgID);
                  } else {
                     MyABEVNewPanel.this.selectorPanel.showOrgPanel("DEFAULT");
                  }
               }

            }
         });
      }

      return this.panel;
   }

   private boolean isValidOrg(String var1) {
      if (var1 != null && !"".equals(var1.trim())) {
         Vector var2 = (Vector)((Object[])((Object[])OrgInfo.getInstance().getOrgNames()))[0];
         if (var2 != null && var2.size() != 0) {
            for(int var3 = 0; var3 < var2.size(); ++var3) {
               if (var1.equals(var2.get(var3)) && this.selectorPanel.hasEntry(var1)) {
                  return true;
               }
            }

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   protected JPanel getExtensionPanel() {
      if (this.selectorPanel == null) {
         this.selectorPanel = (new SelectorHandler()).getSelectorPanel();
      }

      return this.selectorPanel;
   }

   public Hashtable showDialog() {
      try {
         this.initLogic();
         this.panel.getFilePanel().getBusiness().loadFilterSettings(this.getName());
         this.loadSettings();
         if (this.loadedDimension == null) {
            this.loadedDimension = new Dimension(Math.max(700, GuiUtil.getScreenW() / 2), Math.max(500, (int)((double)GuiUtil.getScreenH() * 0.6D)));
         }

         this.setSize(this.loadedDimension);
         this.setPreferredSize(this.loadedDimension);
         this.getExtensionPanel().setSize(new Dimension((int)this.loadedDimension.getWidth(), (GuiUtil.getCommonItemHeight() + 4) * 5));
         this.getExtensionPanel().setPreferredSize(new Dimension((int)this.loadedDimension.getWidth(), (GuiUtil.getCommonItemHeight() + 4) * 5));
         this.getExtensionPanel().setMinimumSize(new Dimension((int)this.loadedDimension.getWidth(), (GuiUtil.getCommonItemHeight() + 4) * 5));
         this.getExtensionPanel().setMaximumSize(new Dimension((int)this.loadedDimension.getWidth(), (GuiUtil.getCommonItemHeight() + 4) * 5));
         if (this.loadedStartingPoint != null) {
            this.setLocation(this.loadedStartingPoint);
         } else {
            this.setLocationRelativeTo(MainFrame.thisinstance);
         }

         if (lastselectedfile != null) {
            File[] var1 = new File[]{lastselectedfile};
            this.panel.setSelectedFiles(var1);
            JTable var2 = this.panel.getFilePanel().getTbl_file_list();
            var2.scrollRectToVisible(var2.getCellRect(var2.getSelectedRow(), 0, true));
            lastselectedfile = null;
         }

         this.pack();
         this.setVisible(true);
         this.mainPanel.remove(this);
         this.dispose();
         return this.result;
      } catch (Exception var3) {
         return null;
      }
   }

   protected JPanel getButtonsPanel() {
      this.btn_ok = new JButton("Megnyitás");
      JButton var2 = new JButton("Mégsem");
      this.btn_ok.setName("ok");
      var2.setName("cancel");
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout(2, 5, 5));
      var1.setAlignmentX(0.0F);
      var1.add(this.btn_ok);
      var1.add(var2);
      this.btn_ok.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Object[] var2 = MyABEVNewPanel.this.getSelectedFiles();
            if (var2 != null && var2.length > 0) {
               SwingWorker var3 = new SwingWorker() {
                  public Object construct() {
                     File var1 = (File)((Object[])((Object[])MyABEVNewPanel.this.getSelectedFiles()[0]))[0];
                     String var2 = null;

                     try {
                        PropertyList.getInstance().set("prop.dynamic.ilyenkor", "");
                        String var3 = (String)((Hashtable)((Hashtable)((Object[])((Object[])MyABEVNewPanel.this.getSelectedFiles()[0]))[3]).get("docinfo")).get("org");
                        var2 = UpgradeManager.checkUpgrade(var1.getPath(), UpgradeFunction.NEW, var3);
                     } catch (Exception var7) {
                        ErrorList.getInstance().store(ErrorList.LEVEL_SHOW_WARNING, "Nyomtatvány frissítés hiba: " + var7.getMessage() == null ? "" : var7.getMessage(), (Exception)null, (Object)null);
                        var2 = var1.getPath();
                     } finally {
                        PropertyList.getInstance().set("prop.dynamic.ilyenkor", (Object)null);
                     }

                     return var2;
                  }

                  public void finished() {
                     String var1 = (String)((Hashtable)((Hashtable)((Object[])((Object[])MyABEVNewPanel.this.getSelectedFiles()[0]))[3]).get("docinfo")).get("org");
                     if (!OrgInfo.getInstance().getOrgIdsFromResources().contains(var1)) {
                        GuiUtil.showMessageDialog(MyABEVNewPanel.this, "A sablonhoz nincs szervezet paraméter állomány !\n" + var1 + " szervezet.", "Új nyomtatvány létrehozása", 1);
                     } else {
                        MyABEVNewPanel.this.setResult(true);
                        String var2 = (String)this.get();
                        if (var2 != null) {
                           File var3 = new NecroFile(var2);
                           MyABEVNewPanel.this.result.put("selected_file", new Object[]{var3});
                           ABEVNewPanel.lastselectedfile = var3;
                           MyABEVNewPanel.this.btn_ok.setEnabled(false);
                           MyABEVNewPanel.this.panel.getFilePanel().getBusiness().saveFilterSettings(MyABEVNewPanel.this.getName());
                           MyABEVNewPanel.this.saveSettings();
                           MyABEVNewPanel.this.setDialogVisible(false);
                        }
                     }
                  }
               };
               var3.start();
            } else {
               GuiUtil.showMessageDialog(MyABEVNewPanel.this, "Nem választott ki nyomtatvány sablont !", "Új nyomtatvány létrehozása", 1);
            }
         }
      });
      var2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            MyABEVNewPanel.this.panel.getFilePanel().getBusiness().saveFilterSettings(MyABEVNewPanel.this.getName());
            MyABEVNewPanel.this.saveSettings();
            MyABEVNewPanel.this.setResult(false);
            MyABEVNewPanel.this.setDialogVisible(false);
         }
      });
      this.installKeyBindingForButton(var2, 27);
      ENYKIconSet var3 = ENYKIconSet.getInstance();
      this.setButtonIcon(this.btn_ok, "anyk_ellenorzes", var3);
      this.setButtonIcon(var2, "anyk_megse", var3);
      return var1;
   }

   protected void setResult(boolean var1) {
      this.result = new Hashtable();
      if (var1) {
         Object[] var2 = (Object[])((Object[])this.getSelectedFiles()[0]);
         this.result.put("selections", this.selectorPanel.getEntitySelection());
         this.result.put("primary_account", "");
         this.result.put("tax_expert", "");
         this.result.put("file_status", "Módosítható");
         this.result.put("selected_file", var2[0]);
         this.result.put("selected_template_docinfo", var2[3]);

         try {
            lastselectedfile = (File)var2[0];
         } catch (Exception var4) {
         }
      }

   }

   protected void build() {
      this.setSize(new Dimension(GuiUtil.getScreenW() / 2, (int)((double)GuiUtil.getScreenH() * 0.6D)));
      this.setPreferredSize(new Dimension(GuiUtil.getScreenW() / 2, (int)((double)GuiUtil.getScreenH() * 0.6D)));
      this.setLocationRelativeTo(MainFrame.thisinstance);
      this.extensionPanel = this.getExtensionPanel();
      if (this.extensionPanel != null) {
         int var1 = (GuiUtil.getCommonItemHeight() + 2) * 5;
         this.extensionPanel.setMinimumSize(new Dimension(690, var1));
         this.extensionPanel.setPreferredSize(new Dimension(690, var1));
         this.extensionPanel.setMaximumSize(new Dimension(690, var1));
         this.extensionPanel.setAlignmentX(0.0F);
      }

      this.mainPanel.setLayout(new BoxLayout(this.mainPanel, 1));
      this.mainPanel.add(this.getBrowserPanel());
      this.mainPanel.add(this.extensionPanel);
      this.mainPanel.add(this.getButtonsPanel());
      this.mainPanel.setMinimumSize(new Dimension((int)this.extensionPanel.getPreferredSize().getWidth(), (int)((double)GuiUtil.getScreenH() * 0.6D)));
      this.mainPanel.setPreferredSize(new Dimension(GuiUtil.getScreenW() / 2, (int)((double)GuiUtil.getScreenH() * 0.6D)));
      this.mainPanel.setSize(new Dimension(GuiUtil.getScreenW() / 2, (int)((double)GuiUtil.getScreenH() * 0.6D)));
      this.getContentPane().add(this.mainPanel);
      this.setTitle("Létrehozás");
   }

   private void saveSettings() {
      SettingsStore var1 = SettingsStore.getInstance();
      var1.set("filepanel_new_settings", "width", this.getWidth() + "");
      var1.set("filepanel_new_settings", "height", this.getHeight() + "");
      var1.set("filepanel_new_settings", "xPos", this.getLocation().x + "");
      var1.set("filepanel_new_settings", "yPos", this.getLocation().y + "");
   }

   private void loadSettings() {
      Hashtable var1 = SettingsStore.getInstance().get("filepanel_new_settings");
      if (var1 != null) {
         try {
            this.loadedDimension = new Dimension(Integer.parseInt((String)var1.get("width")), Integer.parseInt((String)var1.get("height")));
         } catch (Exception var4) {
            return;
         }

         try {
            this.loadedStartingPoint = new Point(Integer.parseInt((String)var1.get("xPos")), Integer.parseInt((String)var1.get("yPos")));
         } catch (Exception var3) {
         }
      }
   }
}
