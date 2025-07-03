package hu.piller.enykp.alogic.filepanels.mohu;

import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.interfaces.IOsHandler;
import hu.piller.enykp.niszws.util.KauAuthHelper;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.oshandler.OsFactory;
import me.necrocore.abevjava.NecroFile;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;

public class AVDHQuestionDialog extends JDialog implements ActionListener, WindowListener, HyperlinkListener {
   private String signQuestion = "Indulhat a nyomtatvány megjelölése?";
   private final JCheckBox noAvdhSign;
   private final JButton igenButton;

   public AVDHQuestionDialog(JFrame var1) {
      super(var1, "Kérdés", true);
      this.addWindowListener(this);
      this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), 1));
      JPanel var2 = new JPanel(new FlowLayout(0));
      JLabel var3 = new JLabel(this.signQuestion);
      var3.setAlignmentX(0.0F);
      Font var4 = var3.getFont();
      var3.setFont(new Font(var4.getName(), 1, var4.getSize() + 2));
      var2.add(var3);
      this.noAvdhSign = GuiUtil.getANYKCheckBox("Nem kérem a nyomtatvány és a csatolmányok elektronikus hitelesítését.");
      this.noAvdhSign.setBorderPainted(false);
      this.noAvdhSign.setAlignmentX(0.5F);
      JEditorPane var5 = new JEditorPane("text/html", getInfoText(false, 1));
      var5.setBackground(new Color(var3.getBackground().getRed(), var3.getBackground().getGreen(), var3.getBackground().getBlue()));
      var5.addHyperlinkListener(this);
      var5.setEditable(false);
      JScrollPane var6 = new JScrollPane(var5, 20, 30);
      var6.setViewportBorder((Border)null);
      var6.setBorder((Border)null);
      JPanel var7 = new JPanel();
      var7.setLayout(new FlowLayout(1));
      this.igenButton = new JButton("Igen");
      this.igenButton.addActionListener(this);
      JButton var8 = new JButton("Nem");
      var8.addActionListener(this);
      var7.add(this.igenButton);
      var7.add(var8);
      var7.setAlignmentX(0.5F);
      JPanel var9 = new JPanel();
      var9.setLayout(new BoxLayout(var9, 1));
      var9.add(var6);
      var9.add(Box.createVerticalStrut(10));
      var9.add(this.noAvdhSign);
      EmptyBorder var10 = new EmptyBorder(10, 10, 10, 10);
      var9.setBorder(BorderFactory.createCompoundBorder(var10, BorderFactory.createEtchedBorder(1)));
      this.getContentPane().add(Box.createVerticalStrut(10));
      this.getContentPane().add(var2);
      this.getContentPane().add(var9);
      this.getContentPane().add(Box.createVerticalStrut(10));
      this.getContentPane().add(var7);
      int var11 = Math.max((int)(0.8D * (double)GuiUtil.getScreenW()), GuiUtil.getW(this.noAvdhSign, this.noAvdhSign.getText()) + 80);
      byte var12 = 8;
      if (GuiUtil.getCommonFontSize() > 32) {
         var12 = 12;
      }

      this.setSize(var11, Math.max(450, var12 * GuiUtil.getCommonItemHeight()));
      this.setPreferredSize(this.getSize());
      this.setMinimumSize(this.getSize());
      this.setLocationRelativeTo(var1);
      this.setResizable(true);
      PropertyList.getInstance().set("prop.dynamic.avdhWithNoAuth", (Object)null);
      this.setVisible(true);
   }

   public void actionPerformed(ActionEvent var1) {
      if (this.noAvdhSign.isSelected()) {
         KauAuthHelper.getInstance().reset();
         PropertyList.getInstance().set("prop.dynamic.avdhWithNoAuth", true);
      }

      PropertyList.getInstance().set("prop.dynamic.AVDHQuestionDialogAnswer", var1.getSource() == this.igenButton ? 0 : 1);
      this.dispose();
   }

   public static String getInfoText(boolean var0, int var1) {
      JCheckBox var2 = GuiUtil.getANYKCheckBox("aaa");
      Color var3 = var2.getBackground();
      String var4 = String.format("#%02x%02x%02x", var3.getRed(), var3.getGreen(), var3.getBlue());
      Color var5 = var2.getForeground();
      String var6 = String.format("#%02x%02x%02x", var5.getRed(), var5.getGreen(), var5.getBlue());
      StringBuilder var7 = (new StringBuilder("<html><body bgcolor=\"" + var4 + "\" text=\"" + var6 + "\"><p style=\"font-family:")).append((new JLabel()).getFont().getFamily()).append("; font-size:").append(GuiUtil.getCommonFontSize()).append("\">");
      if (var0) {
         var7.append("<b>");
      }

      if (var1 == 0) {
         var7.append("A nyomtatványok és a hozzá kapcsolódó mellékletek, csatolmányok azonosításra visszavezetett dokumentumhitelesítés (AVDH) szolgáltatás segítségével válhatnak elektronikusan hitelessé. A nyomtatványról és a csatolmányokról lenyomat képződik, melyekre az AVDH szolgáltatás segítségével elektronikus aláírás kerül.");
      } else {
         var7.append("A nyomtatványok és a hozzá kapcsolódó mellékletek, csatolmányok azonosításra visszavezetett dokumentumhitelesítés (AVDH) szolgáltatás segítségével válhatnak elektronikusan hitelessé. A nyomtatványról és csatolmányokról lenyomat képződik, melyekre az Ügyfélkapun történő belépést követően, AVDH szolgáltatás segítségével elektronikus aláírás kerül. Az aláírást megelőzően a kitöltő program ellenőrzi a nyomtatványt, majd az aláírást követően titkosítja az aláírt nyomtatványt és csatolmányokat tartalmazó csomagot.");
      }

      var7.append("<br><br>");
      var7.append("Az AVDH szolgáltatásról bővebb információkat a <a href=\"https://niszavdh.gov.hu\">https://niszavdh.gov.hu</a> címen talál.<br>").append("A lenyomat ellenőrzéséhez szükséges programokat a <a href=\"http://lenyomatellenorzo.gov.hu\">http://lenyomatellenorzo.gov.hu</a> linkről töltheti le.<br>").append("Az elektronikus aláírások ellenőrzését a <a href=\"https://keaesz.gov.hu\">https://keaesz.gov.hu</a> címen végezheti el.");
      if (var0) {
         var7.append("</b>");
      }

      var7.append("</p></body></html>");
      return var7.toString();
   }

   public void windowOpened(WindowEvent var1) {
   }

   public void windowClosing(WindowEvent var1) {
      if (PropertyList.getInstance().get("prop.dynamic.AVDHQuestionDialogAnswer") == null) {
         PropertyList.getInstance().set("prop.dynamic.AVDHQuestionDialogAnswer", 1);
      }

   }

   public void windowClosed(WindowEvent var1) {
   }

   public void windowIconified(WindowEvent var1) {
   }

   public void windowDeiconified(WindowEvent var1) {
   }

   public void windowActivated(WindowEvent var1) {
   }

   public void windowDeactivated(WindowEvent var1) {
   }

   public void hyperlinkUpdate(HyperlinkEvent var1) {
      if (var1.getEventType() == EventType.ACTIVATED) {
         this.execute(var1.getURL().toString());
      }

   }

   public void execute(String var1) {
      IOsHandler var2 = OsFactory.getOsHandler();

      try {
         File var3;
         try {
            var3 = new NecroFile(SettingsStore.getInstance().get("gui", "internet_browser"));
            if (!var3.exists()) {
               throw new Exception();
            }
         } catch (Exception var5) {
            var3 = new NecroFile(var2.getSystemBrowserPath());
         }

         var2.execute(var3.getName() + " " + var1, (String[])null, var3.getParentFile());
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
