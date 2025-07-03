package hu.piller.enykp.alogic.settingspanel.upgrade;

import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.alogic.settingspanel.upgrade.onlineupdate.view.JOrgsOnlineUpdateStatusDialog;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class UpgradePanel extends JPanel implements ActionListener {
   static final String BORDER_ORGSELECTOR = "Szervezet kiválasztása";
   static final String TXT_ORGSELECTOR = "Tiltom a Szervezetválasztó dialógusablak megjelenítését";
   static final String BORDER_ARCHIVE = "Sablonarchiválás engedélyezése";
   static final String TXT_ARCHIVE = "Engedélyezem a sablonok archiválását";
   static final String BORDER_AUTOUPDATE = "Automatikus frissítések engedélyezése funkciónként";
   static final String BORDER_UPDATE_MSG = "Tájékoztatás frissítések kereséséről";
   static final String TXT_UPDATE_MSG = "Frissítéskeresés tájékoztató üzenet engedélyezése";
   static final String BORDER_ONLINEUPDATE_MSG = "Várakozás frissítési információkra";
   int commonHeight = GuiUtil.getCommonItemHeight() + 4;
   int commonMaxWidth = 500;
   private Map<String, JCheckBox> boxes = new HashMap();

   public UpgradePanel() {
      this.createElements();
      this.loadInitValues();
   }

   private void createElements() {
      Dimension var1 = GuiUtil.getSettingsDialogDimension();
      this.setPreferredSize(var1);
      this.setSize(var1);
      JPanel var2 = new JPanel();
      var2.setSize(new Dimension(GuiUtil.getW("XML állomány ellenőrzése, és átadása elektronikus beküldésre WWWWWWWW") + 20, (int)var1.getHeight()));
      var2.setPreferredSize(new Dimension(GuiUtil.getW("XML állomány ellenőrzése, és átadása elektronikus beküldésre WWWWWWWW") + 20, (int)var1.getHeight()));
      JScrollPane var3 = new JScrollPane(var2, 20, 30);
      this.setLayout(new BorderLayout());
      this.add(var3, "Center");
      this.setSize(new Dimension(300, 250));
      this.setLayout(new BoxLayout(this, 1));
      var2.setLayout(new BoxLayout(var2, 1));
      var2.add(Box.createVerticalStrut(15));
      var2.add(this.createOrgSelectionPanel());
      var2.add(Box.createVerticalStrut(15));
      var2.add(this.createArchivePanel());
      var2.add(Box.createVerticalStrut(15));
      var2.add(this.createFunctionAutoUpdatePanel());
      var2.add(Box.createVerticalStrut(15));
      var2.add(this.createUpdateLookUpMsg());
      var2.add(Box.createVerticalStrut(15));
      var2.add(this.createOnlineUpdateConfig());
      Component[] var4 = var2.getComponents();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Component var7 = var4[var6];
         if (var7 instanceof JPanel) {
            var7.setMaximumSize(new Dimension(this.commonMaxWidth, (int)var7.getMaximumSize().getHeight()));
            var7.setMinimumSize(new Dimension(this.commonMaxWidth, (int)var7.getMinimumSize().getHeight()));
            var7.setPreferredSize(new Dimension(this.commonMaxWidth, (int)var7.getPreferredSize().getHeight()));
         }
      }

   }

   private JPanel createOnlineUpdateConfig() {
      JPanel var1 = new JPanel();
      var1.setBorder(BorderFactory.createTitledBorder("Várakozás frissítési információkra"));
      var1.setMaximumSize(new Dimension(GuiUtil.getW("XML állomány ellenőrzése, és átadása elektronikus beküldésre WWWWWWWW"), 3 * this.commonHeight));
      var1.setMinimumSize(new Dimension(GuiUtil.getW("XML állomány ellenőrzése, és átadása elektronikus beküldésre WWWWWWWW"), 3 * this.commonHeight));
      var1.setPreferredSize(new Dimension(GuiUtil.getW("XML állomány ellenőrzése, és átadása elektronikus beküldésre WWWWWWWW"), 3 * this.commonHeight));
      GridBagConstraints var2 = new GridBagConstraints();
      var2.fill = 0;
      var2.anchor = 21;
      var2.gridx = 0;
      var2.gridy = 0;
      var2.weightx = 0.0D;
      var2.ipadx = 10;
      var2.ipady = 5;
      var1.setLayout(new GridBagLayout());
      if (this.hasOrgWithOnlineUpdate()) {
         JButton var3 = new JButton();
         var3.setName("onlineupdate");
         var3.setText("Várakozás tiltása");
         var3.setToolTipText("Az ÁNYK indulásakor várja/ne várja meg az összes szervezet frissítési információinak letöltését");
         Dimension var4 = new Dimension(GuiUtil.getW(var3, var3.getText()), this.commonHeight + 4);
         var3.setMinimumSize(var4);
         var3.setEnabled(true);
         var3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               final JOrgsOnlineUpdateStatusDialog var2 = new JOrgsOnlineUpdateStatusDialog(MainFrame.thisinstance);
               SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                     var2.pack();
                     var2.setVisible(true);
                  }
               });
            }
         });
         var1.add(var3, var2);
      } else {
         Label var5 = new Label("Nincs olyan szervezet telepítve, ami frissítési információkra várakozást írna elő.");
         var1.add(var5, var2);
      }

      this.commonMaxWidth = (int)Math.max((double)this.commonMaxWidth, var1.getPreferredSize().getWidth());
      return var1;
   }

   private boolean hasOrgWithOnlineUpdate() {
      String[] var1 = OrgInfo.getInstance().getOrgIdsOfOrgsWithOnlineUpdate();
      return var1 != null && var1.length > 0;
   }

   private JPanel createUpdateLookUpMsg() {
      JPanel var1 = new JPanel();
      var1.setBorder(BorderFactory.createTitledBorder("Tájékoztatás frissítések kereséséről"));
      GridBagConstraints var2 = new GridBagConstraints();
      var2.fill = 0;
      var2.anchor = 21;
      var2.gridx = 0;
      var2.gridy = 0;
      var2.weightx = 0.0D;
      var2.ipadx = 10;
      var2.ipady = 5;
      var1.setLayout(new GridBagLayout());
      JLabel var3 = new JLabel("Frissítéskeresés tájékoztató üzenet engedélyezése");
      int var4 = GuiUtil.getW(var3, "XML állomány ellenőrzése, és átadása elektronikus beküldésre WWWWWWWW");
      var1.setMinimumSize(new Dimension(var4, 3 * this.commonHeight));
      var1.setMaximumSize(var1.getMinimumSize());
      var1.setPreferredSize(var1.getMinimumSize());
      this.commonMaxWidth = (int)Math.max((double)this.commonMaxWidth, var1.getPreferredSize().getWidth());
      JCheckBox var5 = GuiUtil.getANYKCheckBox();
      var5.setEnabled(true);
      var5.setName("lookupmsg");
      var5.setActionCommand("lookupmsg");
      var5.addActionListener(this);
      var1.add(var5, var2);
      this.boxes.put("lookupmsg", var5);
      ++var2.gridx;
      var2.weightx = 1.0D;
      var1.add(var3, var2);
      return var1;
   }

   private JPanel createArchivePanel() {
      JPanel var1 = new JPanel();
      var1.setBorder(BorderFactory.createTitledBorder("Sablonarchiválás engedélyezése"));
      GridBagConstraints var2 = new GridBagConstraints();
      var2.fill = 0;
      var2.anchor = 21;
      var2.gridx = 0;
      var2.gridy = 0;
      var2.weightx = 0.0D;
      var2.ipadx = 10;
      var2.ipady = 5;
      var1.setLayout(new GridBagLayout());
      JLabel var3 = new JLabel("Engedélyezem a sablonok archiválását");
      int var4 = GuiUtil.getW(var3, "XML állomány ellenőrzése, és átadása elektronikus beküldésre WWWWWWWW");
      var1.setMinimumSize(new Dimension(var4, 3 * this.commonHeight));
      var1.setMaximumSize(var1.getMinimumSize());
      var1.setPreferredSize(var1.getMinimumSize());
      this.commonMaxWidth = (int)Math.max((double)this.commonMaxWidth, var1.getPreferredSize().getWidth());
      JCheckBox var5 = GuiUtil.getANYKCheckBox();
      var5.setEnabled(true);
      var5.setName("autoarchive");
      var5.setActionCommand("autoarchive");
      var5.addActionListener(this);
      var1.add(var5, var2);
      this.boxes.put("autoarchive", var5);
      ++var2.gridx;
      var2.weightx = 1.0D;
      var1.add(var3, var2);
      return var1;
   }

   private JPanel createOrgSelectionPanel() {
      JPanel var1 = new JPanel();
      var1.setBorder(BorderFactory.createTitledBorder("Szervezet kiválasztása"));
      GridBagConstraints var2 = new GridBagConstraints();
      var2.fill = 0;
      var2.anchor = 21;
      var2.gridx = 0;
      var2.gridy = 0;
      var2.weightx = 0.0D;
      var2.ipadx = 10;
      var2.ipady = 5;
      var1.setLayout(new GridBagLayout());
      JLabel var3 = new JLabel("Tiltom a Szervezetválasztó dialógusablak megjelenítését");
      int var4 = GuiUtil.getW(var3, "XML állomány ellenőrzése, és átadása elektronikus beküldésre WWWWWWWW");
      var1.setMinimumSize(new Dimension(var4, 3 * this.commonHeight));
      var1.setMaximumSize(var1.getMinimumSize());
      var1.setPreferredSize(var1.getMinimumSize());
      this.commonMaxWidth = (int)Math.max((double)this.commonMaxWidth, var1.getPreferredSize().getWidth());
      JCheckBox var5 = GuiUtil.getANYKCheckBox();
      var5.setEnabled(true);
      var5.setName("noshow");
      var5.setActionCommand("noshow");
      var5.addActionListener(this);
      var1.add(var5, var2);
      this.boxes.put("noshow", var5);
      ++var2.gridx;
      var2.weightx = 1.0D;
      var1.add(var3, var2);
      return var1;
   }

   private JPanel createFunctionAutoUpdatePanel() {
      JPanel var1 = new JPanel();
      var1.setBorder(BorderFactory.createTitledBorder("Automatikus frissítések engedélyezése funkciónként"));
      GridBagConstraints var2 = new GridBagConstraints();
      var2.fill = 0;
      var2.anchor = 21;
      var2.gridx = 0;
      var2.gridy = 0;
      var2.ipady = 5;
      var2.ipadx = 10;
      var1.setLayout(new GridBagLayout());
      UpgradeFunction[] var3 = UpgradeFunction.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         UpgradeFunction var6 = var3[var5];
         JCheckBox var7 = GuiUtil.getANYKCheckBox();
         var7.setEnabled(true);
         var7.setName(var6.getCmd());
         var7.setActionCommand(var6.getCmd());
         var7.addActionListener(this);
         var2.weightx = 0.0D;
         var1.add(var7, var2);
         this.boxes.put(var6.getCmd(), var7);
         ++var2.gridx;
         var2.weightx = 1.0D;
         JLabel var8 = new JLabel(var6.getDesc());
         var1.add(var8, var2);
         var2.ipadx = 0;
         --var2.gridx;
         ++var2.gridy;
      }

      int var9 = GuiUtil.getW("XML állomány ellenőrzése, és átadása elektronikus beküldésre WWWWWWWW");
      var1.setMaximumSize(new Dimension(var9, (UpgradeFunction.values().length + 1) * (this.commonHeight + 5)));
      var1.setMinimumSize(new Dimension(var9, (UpgradeFunction.values().length + 1) * (this.commonHeight + 5)));
      var1.setPreferredSize(new Dimension(var9, (UpgradeFunction.values().length + 1) * (this.commonHeight + 5)));
      var1.setSize(new Dimension(var9, (UpgradeFunction.values().length + 1) * (this.commonHeight + 5)));
      this.commonMaxWidth = (int)Math.max((double)this.commonMaxWidth, var1.getPreferredSize().getWidth());
      return var1;
   }

   public void actionPerformed(ActionEvent var1) {
      Iterator var2 = this.boxes.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if (var3.equals(var1.getActionCommand())) {
            this.storeSettings(var3, ((JCheckBox)this.boxes.get(var3)).isSelected());
            break;
         }
      }

   }

   private void storeSettings(String var1, boolean var2) {
      SettingsStore.getInstance().set("upgrade", var1, Boolean.valueOf(var2).toString());
   }

   private void loadInitValues() {
      Iterator var1 = this.boxes.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         ((JCheckBox)this.boxes.get(var2)).setSelected(this.getBooleanValue(var2));
      }

   }

   private boolean getBooleanValue(String var1) {
      if (SettingsStore.getInstance().get("upgrade") == null) {
         return !"noshow".equals(var1) && !"autoarchive".equals(var1);
      } else {
         String var2 = SettingsStore.getInstance().get("upgrade", var1);
         if (var2 == null) {
            return !"noshow".equals(var1) && !"autoarchive".equals(var1);
         } else {
            return Boolean.parseBoolean(var2);
         }
      }
   }
}
