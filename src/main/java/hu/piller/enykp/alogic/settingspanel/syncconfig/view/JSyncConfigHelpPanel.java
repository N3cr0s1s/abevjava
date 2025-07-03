package hu.piller.enykp.alogic.settingspanel.syncconfig.view;

import hu.piller.enykp.alogic.settingspanel.syncconfig.maintenance.query.JQueryTerminateComponent;
import hu.piller.enykp.gui.GuiUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class JSyncConfigHelpPanel extends JPanel {
   private static final String URL_HELP = "resources/masterdata/syncconfighelp.html";
   private JTextPane textPane;

   public JSyncConfigHelpPanel() {
      this.setName("SyncConfigHelp");
      this.setSize(new Dimension(800, 400));
      this.setLayout(new BoxLayout(this, 1));
      this.add(new JQueryTerminateComponent());
      this.add(Box.createVerticalStrut(5));
      JScrollPane var1 = new JScrollPane(this.getContentPane());
      var1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(184, 207, 229)), "Konfigurációs lehetőségek"));
      this.add(var1);
   }

   private JTextPane getContentPane() {
      if (this.textPane == null) {
         this.textPane = new JTextPane();
         this.textPane.putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);
         this.textPane.setEditable(false);
         this.textPane.setBorder(BorderFactory.createLineBorder(Color.lightGray));
         this.textPane.setContentType("text/html");

         try {
            int var1 = Math.max(4, 4 + (GuiUtil.getCommonFontSize() - 12) / 2);
            this.textPane.setText(this.getTextFromUrl(new URL(ClassLoader.getSystemClassLoader().getResource("resources/masterdata/syncconfighelp.html").toExternalForm()), var1));
         } catch (Exception var2) {
            this.textPane.setText("Segédlet betöltési hiba: " + var2.getMessage());
         }
      }

      return this.textPane;
   }

   private String getTextFromUrl(URL var1, int var2) {
      StringBuffer var3 = new StringBuffer();

      try {
         BufferedReader var4 = new BufferedReader(new InputStreamReader(var1.openStream()));

         String var5;
         while((var5 = var4.readLine()) != null) {
            var3.append(var5);
         }

         var4.close();
      } catch (IOException var9) {
         var9.printStackTrace();
      }

      JCheckBox var10 = GuiUtil.getANYKCheckBox("aaa");
      Color var11 = var10.getBackground();
      String var6 = String.format("#%02x%02x%02x", var11.getRed(), var11.getGreen(), var11.getBlue());
      Color var7 = var10.getForeground();
      String var8 = String.format("#%02x%02x%02x", var7.getRed(), var7.getGreen(), var7.getBlue());
      return var3.toString().replaceAll("#FGRRGGBB", var8).replaceAll("#BGRRGGBB", var6);
   }
}
