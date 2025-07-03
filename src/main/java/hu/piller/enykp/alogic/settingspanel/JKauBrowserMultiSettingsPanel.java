package hu.piller.enykp.alogic.settingspanel;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.kauclient.KauAuthMethod;
import hu.piller.enykp.niszws.util.KauAuthHelper;
import hu.piller.enykp.niszws.util.KauSessionTimeoutHandler;
import hu.piller.enykp.util.JavaInfo;
import hu.piller.enykp.util.base.PropertyList;
import java.awt.LayoutManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

public class JKauBrowserMultiSettingsPanel extends JPanel {
   private boolean listenerOff = false;
   JRadioButton[] rb = new JRadioButton[KauAuthMethod.values().length];
   private JKauBrowserMultiSettingsPanel.RBItemListener rbItemListener = new JKauBrowserMultiSettingsPanel.RBItemListener();
   private JKauBrowserMultiSettingsPanel.ExtButtonGroup idGroup = new JKauBrowserMultiSettingsPanel.ExtButtonGroup();

   public JKauBrowserMultiSettingsPanel(String var1) {
      this.setLayout((LayoutManager)null);
      this.setBorder(new TitledBorder(var1));
      this.getSelector(this);
   }

   protected void getSelector(JPanel var1) {
      String var2 = SettingsStore.getInstance().get("gui", "kaubrowser");
      var2 = this.getCurrentMethod(var2);
      int var3 = GuiUtil.getCommonItemHeight() + 4;

      for(int var4 = 0; var4 < this.rb.length; ++var4) {
         this.rb[var4] = GuiUtil.getANYKRadioButton(KauAuthMethod.values()[var4].getText());
         if (var2.equalsIgnoreCase(KauAuthMethod.values()[var4].name())) {
            this.rb[var4].setSelected(true);
            this.idGroup.setPrevSelected(var4);
         }

         GuiUtil.setDynamicBound(this.rb[var4], this.rb[var4].getText(), 10, var3 + var4 * var3);
         this.rb[var4].addItemListener(this.rbItemListener);
         this.idGroup.add(this.rb[var4]);
         var1.add(this.rb[var4]);
      }

   }

   private String getCurrentMethod(String var1) {
      try {
         if (var1 == null) {
            var1 = KauAuthMethod.KAU_UKP.name();
         }

         return !var1.equals(KauAuthMethod.KAU_DAP.name()) && !var1.equals(KauAuthMethod.KAU_UKP.name()) && !var1.equals(KauAuthMethod.KAU_EML.name()) ? KauAuthMethod.KAU_ALL.name() : var1;
      } catch (Exception var3) {
         return "KAU_UKP";
      }
   }

   private class RBItemListener implements ItemListener {
      private RBItemListener() {
      }

      public void itemStateChanged(final ItemEvent var1) {
         if (!JKauBrowserMultiSettingsPanel.this.listenerOff) {
            if (var1.getStateChange() == 1) {
               if (var1.getSource() == JKauBrowserMultiSettingsPanel.this.rb[0]) {
                  SwingUtilities.invokeLater(new Runnable() {
                     public void run() {
                        if (!JavaInfo.isJavaFxAvailable() && JOptionPane.showOptionDialog(MainFrame.thisinstance, JavaInfo.getJFXSettingsMessage(), "Ehhez a funkcióhoz nem megfelelő a java verzió", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) != 0) {
                           JKauBrowserMultiSettingsPanel.this.listenerOff = true;
                           JKauBrowserMultiSettingsPanel.this.rb[JKauBrowserMultiSettingsPanel.this.idGroup.getPrevSelected()].setSelected(true);
                           JKauBrowserMultiSettingsPanel.this.listenerOff = false;
                        } else if (JOptionPane.showOptionDialog(MainFrame.thisinstance, "A korábban megjegyzett bejelentkezési adatokat töröljük.\nFolytatja?", "Megjegyzett bejelentkezési adatok törlése", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) != 0) {
                           JKauBrowserMultiSettingsPanel.this.listenerOff = true;
                           JKauBrowserMultiSettingsPanel.this.rb[JKauBrowserMultiSettingsPanel.this.idGroup.getPrevSelected()].setSelected(true);
                           JKauBrowserMultiSettingsPanel.this.listenerOff = false;
                        } else {
                           JKauBrowserMultiSettingsPanel.this.idGroup.setPrevSelected(RBItemListener.this.getSelectedIndex(var1));
                           KauSessionTimeoutHandler.getInstance().reset();
                           KauAuthHelper.getInstance().reset();
                           SettingsStore.getInstance().set("gui", "kaubrowser", KauAuthMethod.values()[JKauBrowserMultiSettingsPanel.this.idGroup.getPrevSelected()].name());
                        }
                     }
                  });
               } else {
                  JKauBrowserMultiSettingsPanel.this.idGroup.setPrevSelected(this.getSelectedIndex(var1));
                  SettingsStore.getInstance().set("gui", "kaubrowser", KauAuthMethod.values()[JKauBrowserMultiSettingsPanel.this.idGroup.getPrevSelected()].name());
               }

            }
         }
      }

      private int getSelectedIndex(ItemEvent var1) {
         for(int var2 = 0; var2 < JKauBrowserMultiSettingsPanel.this.rb.length; ++var2) {
            if (var1.getSource() == JKauBrowserMultiSettingsPanel.this.rb[var2]) {
               return var2;
            }
         }

         return 0;
      }

      // $FF: synthetic method
      RBItemListener(Object var2) {
         this();
      }
   }

   private class ExtButtonGroup extends ButtonGroup {
      private int prevSelected = 0;

      public ExtButtonGroup() {
      }

      public int getPrevSelected() {
         return this.prevSelected;
      }

      public void setPrevSelected(int var1) {
         this.prevSelected = var1;
      }
   }
}
