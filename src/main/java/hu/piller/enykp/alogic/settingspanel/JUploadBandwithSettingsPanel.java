package hu.piller.enykp.alogic.settingspanel;

import hu.piller.enykp.gui.GuiUtil;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class JUploadBandwithSettingsPanel extends JPanel {
   private JLabel text1 = new JLabel("Az ÁNYK rendelkezésére álló kimenő sávszélesség (http/https)");
   private JLabel text2 = new JLabel("[Mbps]");
   private JFormattedTextField field;

   public JUploadBandwithSettingsPanel(int var1) {
      NumberFormat var2 = NumberFormat.getNumberInstance();
      var2.setMinimumIntegerDigits(1);
      var2.setMaximumIntegerDigits(5);
      var2.setMinimumFractionDigits(0);
      var2.setMaximumFractionDigits(2);
      this.field = new JFormattedTextField(new DefaultFormatterFactory(new NumberFormatter(var2), new NumberFormatter(var2), new NumberFormatter() {
         public String valueToString(Object var1) throws ParseException {
            return super.valueToString(var1);
         }

         public Object stringToValue(String var1) throws ParseException {
            return super.stringToValue(var1);
         }
      }));
      this.field.setName("outputBandwith");
      this.field.setMinimumSize(new Dimension(80, GuiUtil.getCommonItemHeight() + 4));
      this.field.setMaximumSize(new Dimension(80, GuiUtil.getCommonItemHeight() + 4));
      this.field.setPreferredSize(new Dimension(80, GuiUtil.getCommonItemHeight() + 4));
      this.field.setHorizontalAlignment(4);
      this.field.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent var1) {
         }

         public void focusLost(FocusEvent var1) {
            JFormattedTextField var2 = (JFormattedTextField)var1.getSource();
            String var3 = var2.getText().trim();
            if ("".equals(var3)) {
               var2.setValue(1);
               var3 = "1";
            }

            SettingsStore.getInstance().set("gui", "uploadbandwith", var3);
         }
      });
      this.field.setText(SettingsStore.getInstance().get("gui", "uploadbandwith"));
      int var3 = GuiUtil.getW(this.text1, this.text1.getText()) + 10 + (int)this.field.getPreferredSize().getWidth() + 10 + GuiUtil.getW(this.text2, this.text2.getText()) + 20;
      this.setBounds(10, var1, var3, GuiUtil.getCommonItemHeight() + 20);
      this.setLayout(new BoxLayout(this, 0));
      this.add(this.text1);
      this.add(Box.createHorizontalStrut(10));
      this.add(this.field);
      this.add(Box.createHorizontalStrut(10));
      this.add(this.text2);
      this.add(Box.createHorizontalGlue());
   }

   public static JUploadBandwithSettingsPanel create(int var0) {
      JUploadBandwithSettingsPanel var1 = new JUploadBandwithSettingsPanel(var0);
      return var1;
   }
}
