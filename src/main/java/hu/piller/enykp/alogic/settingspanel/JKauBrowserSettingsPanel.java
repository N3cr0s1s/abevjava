package hu.piller.enykp.alogic.settingspanel;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.niszws.util.KauAuthHelper;
import hu.piller.enykp.niszws.util.KauSessionTimeoutHandler;
import hu.piller.enykp.util.base.PropertyList;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/** @deprecated */
@Deprecated
public class JKauBrowserSettingsPanel extends JPanel {
   private boolean listenerOff = false;

   public JKauBrowserSettingsPanel(String var1) {
      this.setLayout(new BoxLayout(this, 0));
      this.add(this.getSelector());
      this.add(Box.createHorizontalStrut(1));
      this.add(this.getLabelForSelector(var1));
      this.add(Box.createHorizontalGlue());
   }

   protected JComponent getSelector() {
      final JCheckBox var1 = GuiUtil.getANYKCheckBox();
      String var2 = SettingsStore.getInstance().get("gui", "kaubrowser");
      if (var2 == null) {
         var1.setSelected(true);
         SettingsStore.getInstance().set("gui", "kaubrowser", "true");
      } else if ("true".equals(var2)) {
         var1.setSelected(true);
      }

      var1.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent var1x) {
            if (!JKauBrowserSettingsPanel.this.listenerOff) {
               SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                     if (JOptionPane.showOptionDialog(MainFrame.thisinstance, "A korábban megjegyzett bejelentkezési adatokat töröljük.\nFolytatja?", "Megjegyzett bejelentkezési adatok törlése", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) != 0) {
                        JKauBrowserSettingsPanel.this.listenerOff = true;
                        var1.setSelected(!var1.isSelected());
                        JKauBrowserSettingsPanel.this.listenerOff = false;
                     } else {
                        KauSessionTimeoutHandler.getInstance().reset();
                        KauAuthHelper.getInstance().reset();
                        String var1x = var1.isSelected() ? "true" : "false";
                        SettingsStore.getInstance().set("gui", "kaubrowser", var1x);
                     }
                  }
               });
            }
         }
      });
      return var1;
   }

   protected JLabel getLabelForSelector(String var1) {
      return new JLabel(var1);
   }
}
