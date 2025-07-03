package hu.piller.enykp.util.base;

import hu.piller.enykp.alogic.filepanels.attachement.EJFileChooser;
import hu.piller.enykp.alogic.filesaver.enykinner.ENYKClipboardHandler;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.alogic.templateutils.blacklist.Blacklist;
import hu.piller.enykp.alogic.templateutils.blacklist.BlacklistStore;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.IOsHandler;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.oshandler.OsFactory;
import me.necrocore.abevjava.NecroFile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;

public class InfoPanel extends JDialog implements HyperlinkListener {
   private JPanel elsoFul;
   private JPanel masodikFul;
   private JPanel harmadikFul;
   private JPanel negyedikFul;
   private JPanel otodikFul;
   private JPanel buttonPanel;
   private JTabbedPane mainTabbedPane;
   private JButton okButton;
   private JButton saveButton;
   private JEditorPane jep1;
   private JEditorPane jep2;
   private JEditorPane jep3;
   private JEditorPane jep4;
   private JEditorPane jep5;
   private Properties ps;
   private StringBuffer sb1;
   private IPropertyList iplMaster = PropertyList.getInstance();
   private Color bgc;
   private String currentFilename = "";
   String[] abevJavaCfgPathAndContent;
   private Hashtable urls = new Hashtable();
   private static InfoPanel ourInstance = new InfoPanel();
   private int commonFontSize = 12;
   String filename = "abevjavapath.cfg";
   String b = "";
   String bv = "";

   public static InfoPanel getInstance() {
      return ourInstance;
   }

   private InfoPanel() {
      super(MainFrame.thisinstance, "Általános nyomtatványkitöltő program", true);
      this.commonFontSize = GuiUtil.getCommonFontSize();
      this.bgc = (new JLabel()).getBackground();
      this.setSize((int)Math.min(0.6D * (double)GuiUtil.getScreenW(), (double)GuiUtil.getW("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW")), Math.min((int)(0.8D * (double)GuiUtil.getScreenH()), Math.max(600, GuiUtil.getCommonItemHeight() * 50)));
      this.setPreferredSize(this.getSize());
      this.setLocationRelativeTo(MainFrame.thisinstance);
      this.setResizable(true);
      this.init();
   }

   public void showDialog(String var1) {
      this.setFul1(var1);
      this.jep2.setText(this.setOrgText());
      ourInstance.getContentPane().setLayout(new BorderLayout(10, 10));
      ourInstance.getContentPane().setBackground(this.bgc);
      ourInstance.getContentPane().add(this.mainTabbedPane, "Center");
      ourInstance.getContentPane().add(this.buttonPanel, "South");
      super.setVisible(true);
   }

   private void init() {
      try {
         this.ps = System.getProperties();
         this.mainTabbedPane = new JTabbedPane();
         this.mainTabbedPane.setBackground(this.bgc);
         this.elsoFul = new JPanel(new BorderLayout());
         this.masodikFul = new JPanel(new BorderLayout());
         this.harmadikFul = new JPanel(new BorderLayout());
         this.negyedikFul = new JPanel(new BorderLayout());
         this.otodikFul = new JPanel(new BorderLayout());
         this.elsoFul.setBackground(this.bgc);
         this.masodikFul.setBackground(this.bgc);
         this.harmadikFul.setBackground(this.bgc);
         this.negyedikFul.setBackground(this.bgc);
         this.otodikFul.setBackground(this.bgc);
         this.jep1 = new JEditorPane();
         this.jep1.setContentType("text/html");
         this.jep1.setEditable(false);
         this.jep1.setName("jep1");
         this.jep2 = new JEditorPane();
         this.jep2.addHyperlinkListener(this);
         this.jep2.setContentType("text/html");
         this.jep2.setEditable(false);
         this.jep2.setName("jep2");
         this.setFul2();
         this.jep3 = new JEditorPane();
         this.jep3.addHyperlinkListener(this);
         this.jep3.setContentType("text/html");
         this.jep3.setEditable(false);
         this.jep3.setName("jep3");
         this.setFul3();
         this.jep4 = new JEditorPane();
         this.jep4.addHyperlinkListener(this);
         this.jep4.setContentType("text/html");
         this.jep4.setEditable(false);
         this.jep4.setName("jep4");
         this.jep5 = new JEditorPane();
         this.jep5.addHyperlinkListener(this);
         this.jep5.setContentType("text/html");
         this.jep5.setEditable(false);
         this.jep5.setName("jep5");
         this.setFul5();
         this.mainTabbedPane.addTab("Környezet", this.elsoFul);
         this.mainTabbedPane.addTab("Szervezet", this.masodikFul);
         this.mainTabbedPane.addTab("Segítség", this.harmadikFul);
         this.mainTabbedPane.addTab("Kitiltási lista", this.otodikFul);
         this.okButton = new JButton("Rendben");
         this.okButton.setBackground(this.bgc);
         this.okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               InfoPanel.ourInstance.dispose();
            }
         });
         this.saveButton = new JButton("Állományba mentés");
         this.saveButton.setBackground(this.bgc);
         this.saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               try {
                  InfoPanel.this.saveData();
               } catch (Exception var3) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiba a névjegy adatok mentésekor", "Hiba", 0);
                  var3.printStackTrace();
               }

            }
         });
         this.buttonPanel = new JPanel();
         this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel, 0));
         this.buttonPanel.setBackground(this.bgc);
         this.buttonPanel.add(Box.createHorizontalGlue());
         this.buttonPanel.add(this.okButton);
         this.buttonPanel.add(this.saveButton);
         this.buttonPanel.add(Box.createHorizontalGlue());
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
      }

   }

   public void hyperlinkUpdate(HyperlinkEvent var1) {
      if (var1.getEventType() == EventType.ACTIVATED) {
         this.execute("\"" + this.urls.get(var1.getDescription()) + "\"");
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
         } catch (Exception var6) {
            var3 = new NecroFile(var2.getSystemBrowserPath());
         }

         var2.execute(var3.getName() + " " + var1, (String[])null, var3.getParentFile());
      } catch (Exception var7) {
         try {
            ENYKClipboardHandler var4 = new ENYKClipboardHandler();
            var4.setClipboardContents(var1.toString());
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem sikerült a hivatkozás megnyitása.\nA hivatkozás url-jét kimásoltuk a vágólapra, beillesztheti a böngésző címsorába.", "Böngészés hiba", 0);
         } catch (Exception var5) {
            var7.printStackTrace();
            var5.printStackTrace();
         }
      }

   }

   private void setFul1(String var1) {
      this.currentFilename = var1;
      this.abevJavaCfgPathAndContent = this.getAJCP();
      String var2 = "";

      try {
         var2 = (String)this.ps.get("java.vm.name");
         if (var2.indexOf("64") > -1) {
            var2 = " (64)";
         } else {
            var2 = " (32)";
         }
      } catch (Exception var22) {
         var2 = "";
      }

      if (this.abevJavaCfgPathAndContent[0].endsWith("<hr>")) {
         this.abevJavaCfgPathAndContent[0] = this.abevJavaCfgPathAndContent[0].substring(0, this.abevJavaCfgPathAndContent[0].length() - 4);
      }

      if (this.abevJavaCfgPathAndContent[1].endsWith("<hr>")) {
         this.abevJavaCfgPathAndContent[1] = this.abevJavaCfgPathAndContent[1].substring(0, this.abevJavaCfgPathAndContent[1].length() - 4);
      }

      this.elsoFul.removeAll();
      StringBuffer var3 = new StringBuffer(this.setHtmlHead());
      var3.append("<center><div style=\"font-size: ").append(this.commonFontSize).append("px; font-weight:bold;\">Általános nyomtatványkitöltő program</div>");
      var3.append("<br><table border=0>");

      try {
         var3.append("<tr><td width=250><b>ÁNYK keretprogram verzió&nbsp;:&nbsp;</b></td><td><b>").append("v.3.44.0").append("</b></td></tr>");
      } catch (Exception var21) {
         Tools.eLog(var21, 0);
      }

      try {
         if (var1 != null) {
            var3.append("<tr><td width=250><b>Aktuális fájl&nbsp;:&nbsp;</b></td><td><b>").append(var1).append("</b></td></tr>");
         }
      } catch (Exception var20) {
         Tools.eLog(var20, 0);
      }

      try {
         String var4 = MainFrame.thisinstance.mp.getDMFV().bm.docinfo.get("name") + " " + MainFrame.thisinstance.mp.getDMFV().bm.docinfo.get("ver");
         var3.append("<tr><td width=250>Nyomtatvány verzió&nbsp;:&nbsp;</td><td>").append(var4).append("</td></tr>");
      } catch (Exception var19) {
         Tools.eLog(var19, 0);
      }

      try {
         var3.append("<tr><td width=250>Operációs rendszer&nbsp;:&nbsp;</td><td>").append(this.ps.get("os.name")).append(" ").append(this.ps.get("sun.os.patch.level")).append("</td></tr>");
      } catch (Exception var18) {
         Tools.eLog(var18, 0);
      }

      try {
         var3.append("<tr><td width=250><b>Java verzió&nbsp;:&nbsp;</b></td><td><b>").append(this.ps.get("java.vm.vendor")).append(" verzió : ").append(this.ps.get("java.version")).append(var2).append("</b></td></tr>");
      } catch (Exception var17) {
         Tools.eLog(var17, 0);
      }

      try {
         var3.append("<tr><td width=250><b>Java info&nbsp;:&nbsp;</b></td><td><b>").append(this.ps.get("java.vendor")).append(" - ").append(this.ps.get("java.runtime.name")).append("</b></td></tr>");
      } catch (Exception var16) {
         Tools.eLog(var16, 0);
      }

      var3.append("<tr><td colspan=\"2\"></td></tr>");

      try {
         var3.append("<tr><td width=200>Indítási könyvtár&nbsp;:&nbsp;</td><td>").append(this.ps.get("user.dir")).append("</td></tr>");
      } catch (Exception var15) {
         Tools.eLog(var15, 0);
      }

      try {
         var3.append("<tr><td width=200><b>Felhasználói könyvtár&nbsp;:&nbsp;</b></td><td><b>").append(Tools.beautyPath(System.getProperty("user.home"))).append("</b></td></tr>");
      } catch (Exception var14) {
         Tools.eLog(var14, 0);
      }

      try {
         var3.append("<tr><td width=200><b>Postázó könyvtár (KRDIR)&nbsp;:&nbsp;</b></td><td><b>").append(Tools.beautyPath((String)this.iplMaster.get("prop.usr.krdir"))).append("</b></td></tr>");
      } catch (Exception var13) {
         Tools.eLog(var13, 0);
      }

      try {
         var3.append("<tr><td width=200>Nyomtatványok telepítésének helye&nbsp;:&nbsp;</td><td>").append(Tools.beautyPath((String)this.iplMaster.get("prop.dynamic.templates.absolutepath"))).append("</td></tr>");
      } catch (Exception var12) {
         Tools.eLog(var12, 0);
      }

      try {
         var3.append("<tr><td width=200><b>Mentett bevallások helye&nbsp;:&nbsp;</b></td><td><b>").append(Tools.beautyPath(this.iplMaster.get("prop.usr.root") + File.separator + this.iplMaster.get("prop.usr.saves"))).append("</b></td></tr>");
      } catch (Exception var11) {
         Tools.eLog(var11, 0);
      }

      try {
         var3.append("<tr><td width=200><b>Törzsadatok helye&nbsp;:&nbsp;</b></td><td><b>").append(Tools.beautyPath((String)this.iplMaster.get("prop.usr.primaryaccounts"))).append("</b></td></tr>");
      } catch (Exception var10) {
         Tools.eLog(var10, 0);
      }

      try {
         var3.append("<tr><td width=200>Import állományok helye&nbsp;:&nbsp;</td><td>").append(Tools.beautyPath(this.iplMaster.get("prop.usr.root") + File.separator + this.iplMaster.get("prop.usr.import"))).append("</td></tr>");
      } catch (Exception var9) {
         Tools.eLog(var9, 0);
      }

      try {
         var3.append("<tr><td width=200><b>Kontroll állományok helye&nbsp;:&nbsp;</b></td><td><b>").append(Tools.beautyPath(this.iplMaster.get("prop.usr.root") + File.separator + this.iplMaster.get("prop.usr.kontroll"))).append("</b></td></tr>");
      } catch (Exception var8) {
         Tools.eLog(var8, 0);
      }

      try {
         var3.append("<tr><td width=200><b>Paraméter-állományok útvonala&nbsp;:&nbsp;</b></td><td><b>").append(Tools.beautyPath(this.iplMaster.get("prop.sys.root") + File.separator + "eroforrasok")).append("</b></td></tr>");
      } catch (Exception var7) {
         Tools.eLog(var7, 0);
      }

      try {
         var3.append("<tr><td width=200 valign=\"top\">Abevjavapath.cfg állomány helye&nbsp;:&nbsp;</td><td>").append(this.abevJavaCfgPathAndContent[0]).append("</td></tr>");
      } catch (Exception var6) {
         Tools.eLog(var6, 0);
      }

      try {
         var3.append("<tr><td width=200 valign=\"top\">Abevjavapath.cfg állomány tartalma&nbsp;:&nbsp;</td><td>").append(this.abevJavaCfgPathAndContent[1]).append("</td></tr>");
      } catch (Exception var5) {
         Tools.eLog(var5, 0);
      }

      var3.append("</table>");
      var3.append("</center>");
      var3.append(this.setHtmlFoot());
      this.jep1.setText(var3.toString());
      this.jep1.setCaretPosition(0);
      this.elsoFul.add(new JScrollPane(this.jep1), "Center");
   }

   private void setFul2() {
      this.jep2.setCaretPosition(0);
      this.masodikFul.add(new JScrollPane(this.jep2), "Center");
   }

   private void setFul3() {
      StringBuffer var1 = new StringBuffer(this.setHtmlHead());
      var1.append("<br>Aktuális információk az interneten:<br>");
      var1.append("<blockquote><a href=\"aktinf\">Információk a nyomtatványkitöltő program használatához</a></blockquote>");
      var1.append("<blockquote><a href=\"nyaktinf\">Nyomtatványokkal kapcsolatos részletes információk</a></blockquote>");
      this.urls.put("aktinf", "https://nav.gov.hu/nyomtatvanyok/letoltesek/nyomtatvanykitolto_programok/nyomtatvany_apeh/keretprogramok/javakitolto");
      this.urls.put("nyaktinf", "http://nav.gov.hu/nav/letoltesek");
      var1.append(this.setHtmlFoot());
      this.jep3.setText(var1.toString());
      this.jep3.setCaretPosition(0);
      this.harmadikFul.add(new JScrollPane(this.jep3), "Center");
   }

   private void setFul4() {
      StringBuffer var1 = new StringBuffer(this.setHtmlHead());
      var1.append("<center><div style=\"font-size: ").append(this.commonFontSize + 2).append("px; font-weight:bold;\">Általános nyomtatványkitöltő program<br><br></div>");

      try {
         var1.append("<tr><td width=250><b>Verzió : </b></td><td>").append("v.3.44.0").append("</td></tr>");
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
      }

      this.jep4.setText(var1.toString());
      this.jep4.setCaretPosition(0);
      this.negyedikFul.add(new JScrollPane(this.jep4), "Center");
   }

   private void setFul5() {
      StringBuffer var1 = new StringBuffer(this.setHtmlHead());
      var1.append("<center><div style=\"font-size: ").append(this.commonFontSize).append("px; font-weight:bold;\">Az ÁNYK-ban jelenleg nem szerkeszthető nyomtatványok listája<br><br></div>");
      HashMap var2 = BlacklistStore.getInstance().getFullList();
      var1.append("<table border=\"0\">");
      var1.append("<tr><td width=250>Nyomtatvány</td><td>Megjegyzés</td><td>Javasolt elérés</td></tr>");
      Iterator var3 = var2.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         var1.append("<tr><td width=250>").append(this.getViewKey((String)var4.getKey(), ((Blacklist.Template)var4.getValue()).getOrg())).append("</td><td>").append(((Blacklist.Template)var4.getValue()).getMessage()).append("</td><td><a href=\"bltid").append((String)var4.getKey()).append("\">").append(((Blacklist.Template)var4.getValue()).getTargetUrl()).append("</a></td></tr>");
         this.urls.put("bltid" + (String)var4.getKey(), ((Blacklist.Template)var4.getValue()).getTargetUrl());
      }

      var1.append("</table></center><br>");
      var1.append("A kitiltási lista letölthető az alábbi helyről:<br><a href=\"bl_url_user\">").append("https://nav.gov.hu/nyomtatvanyok/letoltesek/blacklist.xml").append("</a>");
      this.urls.put("bl_url_user", "https://nav.gov.hu/nyomtatvanyok/letoltesek/blacklist.xml");
      this.jep5.setText(var1.toString());
      this.jep5.setCaretPosition(0);
      this.otodikFul.add(new JScrollPane(this.jep5), "Center");
   }

   private String setHtmlHead() {
      String var1 = String.format("#%02x%02x%02x", this.bgc.getRed(), this.bgc.getGreen(), this.bgc.getBlue());
      return "<html><body style=\"background:" + var1 + "; font:Arial; font-size:" + this.commonFontSize + "px;\">";
   }

   private String setHtmlFoot() {
      return "</body></html>";
   }

   private String getOrphanOrgids() {
      StringBuilder var1 = new StringBuilder("");
      String var2 = this.getRowForOrphanOrgids("<b>Nyomtatványokban található paraméter állomány nélküli szervezet azonosítók</b>", TemplateUtils.getInstance().getTemplateOrphanOrgids());
      String var3 = this.getRowForOrphanOrgids("<b>Segédletekben található paraméter állomány nélküli szervezet azonosítók</b>", TemplateUtils.getInstance().getHelpOrphanOrgids());
      if (!"".equals(var2) || !"".equals(var3)) {
         var1.append("<br><div align=\"left\"><table border=\"0\" cellpadding=\"5\">");
         var1.append(var2);
         var1.append(var3);
         var1.append("</table></div>");
      }

      return var1.toString();
   }

   private String getRowForOrphanOrgids(String var1, String[] var2) {
      StringBuilder var3 = new StringBuilder("");
      if (var2 != null && var2.length > 0) {
         var3.append("<tr>");
         var3.append("<td>").append(var1).append("</td>");
         var3.append("</tr><tr>");
         var3.append("<td>");

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3.append(var2[var4]);
            if (var4 < var2.length - 1) {
               var3.append(", ");
            }
         }

         var3.append("</td>");
         var3.append("</tr>");
      }

      return var3.toString();
   }

   private String setOrgText() {
      this.sb1 = new StringBuffer(this.setHtmlHead());
      OrgInfo var1 = OrgInfo.getInstance();
      Hashtable var2 = (Hashtable)var1.getOrgList();
      String var3 = this.getOrphanOrgids();
      if (var2.size() <= 0 && "".equals(var3)) {
         this.sb1.append("<br><div align=\"left\">Nincs elérhető szervezet-információ</div><br>");
      } else {
         this.sb1.append("<br><div align=\"left\">Elérhető szervezet-információk:</div><br>");
      }

      this.sb1.append("<table border=\"0\" width=\"100%\">");
      Enumeration var4 = var2.keys();
      int var5 = 0;
      Color var6 = this.bgc.brighter();

      for(String var7 = String.format("#%02x%02x%02x", var6.getRed(), var6.getGreen(), var6.getBlue()); var4.hasMoreElements(); this.sb1.append("</td></tr>")) {
         ++var5;
         Object var8 = var4.nextElement();
         Hashtable var9 = (Hashtable)((Hashtable)var1.getOrgInfo(var8)).get("attributes");
         this.urls.put("ufl" + var5, var9.get("ugyfelszolgalaturl"));
         this.sb1.append("<tr style=\"text-align: left;");
         if (var5 % 2 == 0) {
            this.sb1.append(" background:" + var7 + ";");
         }

         this.sb1.append("\">");
         this.sb1.append("<td width=\"30\" valign=\"top\">");
         if (var2.size() > 1) {
            this.sb1.append(" - ");
         } else {
            this.sb1.append("&nbsp;");
         }

         this.sb1.append("</td><td>");
         String var10 = (String)var9.get("successorid");
         if (var10 != null) {
            this.sb1.append("<font color=\"#8c8c8c\">");
         }

         this.sb1.append("<b>Szervezet neve : ").append(var9.get("orgname")).append("</b><br>");
         this.sb1.append("Ügyfélszolgálat : <a href=\"ufl").append(var5).append("\">").append(var9.get("ugyfelszolgalaturl")).append("</a><br>");
         if (var10 != null) {
            this.sb1.append("</font>");
         }
      }

      this.sb1.append("</table>");
      this.sb1.append("</center>");
      this.sb1.append(var3);
      this.sb1.append(this.setHtmlFoot());
      return this.sb1.toString();
   }

   private String[] getAJCP() {
      IOsHandler var1 = OsFactory.getOsHandler();
      StringBuffer var2 = new StringBuffer();
      StringBuffer var3 = new StringBuffer();
      File var4 = null;
      File var5 = null;
      File var6 = null;

      try {
         var4 = new NecroFile(var1.getInitDir() + File.separator + this.filename);
         if (var4.exists()) {
            var2.append(this.b).append(var4.getParent()).append(this.bv).append("<hr>");

            try {
               var3.append(this.b).append(this.readContent(var4)).append(this.bv).append("<hr>");
            } catch (IOException var11) {
               Tools.eLog(var11, 0);
            }
         }
      } catch (Exception var12) {
         Tools.eLog(var12, 0);
      }

      try {
         var5 = new NecroFile(var1.getUserHomeDir(), ".abevjava" + File.separator + this.filename);
         if (!var4.equals(var5) && var5.exists()) {
            var2.append(this.b).append(var5.getParent()).append(this.bv).append("<hr>");

            try {
               var3.append(this.b).append(this.readContent(var5)).append(this.bv).append("<hr>");
            } catch (IOException var9) {
               Tools.eLog(var9, 0);
            }
         }
      } catch (Exception var10) {
         Tools.eLog(var10, 0);
      }

      try {
         var6 = this.getFileInUacInitDir(var1);
         if (var6 == null || var6.equals(var4) || var6.equals(var5) || !var6.exists()) {
            return new String[]{var2.toString(), var3.toString()};
         }

         var2.append(this.b).append(var6.getParent()).append(this.bv).append("<hr>");

         try {
            var3.append(this.b).append(this.readContent(var6)).append(this.bv).append("<hr>");
         } catch (IOException var8) {
            Tools.eLog(var8, 0);
         }
      } catch (Exception var13) {
         Tools.eLog(var13, 0);
      }

      return new String[]{var2.toString(), var3.toString()};
   }

   private File getFileInUacInitDir(IOsHandler var1) {
      String var2 = var1.getEnvironmentVariable("LOCALAPPDATA");
      if (var2 != null && var2.length() > 0) {
         var2 = var2 + "\\VirtualStore\\Windows";
         File var3 = new NecroFile(var2 + File.separator + this.filename);
         if (var3.exists()) {
            return var3;
         }
      }

      return null;
   }

   private String readContent(File var1) throws IOException {
      BufferedReader var2 = new BufferedReader(new FileReader(var1));
      StringBuffer var3 = new StringBuffer();

      String var4;
      while((var4 = var2.readLine()) != null) {
         var3.append(var4).append("<br>");
      }

      return var3.toString();
   }

   private void saveData() throws Exception {
      OrgInfo var1 = OrgInfo.getInstance();
      Hashtable var2 = (Hashtable)var1.getOrgList();
      EJFileChooser var3 = new EJFileChooser();
      File var4 = new NecroFile((String)PropertyList.getInstance().get("prop.usr.naplo"));
      var3.setCurrentDirectory(var4);
      var3.setSelectedFile(new NecroFile((String)PropertyList.getInstance().get("prop.usr.naplo") + File.separator + "nevjegy.txt"));
      var3.setDialogTitle("Információk mentése - fájl választás");
      int var5 = var3.showSaveDialog(MainFrame.thisinstance);
      if (var5 == 0) {
         File var6 = var3.getSelectedFile();
         if (var6 != null) {
            String var7 = null;

            try {
               var7 = var6.getParent();
            } catch (Exception var25) {
            }

            String var8 = var6.getAbsolutePath();
            FileOutputStream var9 = new FileOutputStream(var8);

            try {
               String var10 = "";

               try {
                  var10 = (String)this.ps.get("java.vm.name");
                  if (var10.indexOf("64") > -1) {
                     var10 = " (64)";
                  } else {
                     var10 = " (32)";
                  }
               } catch (Exception var24) {
                  var10 = "";
               }

               var9.write("Általános nyomtatványkitöltő program\n".getBytes("utf-8"));
               var9.write("ÁNYK keretprogram verzió: v.3.44.0\n".getBytes("utf-8"));
               if (this.currentFilename == null) {
                  var9.write("Aktuális fájl: nincs betöltve fájl\n".getBytes("utf-8"));
               } else {
                  var9.write(("Aktuális fájl: " + this.currentFilename + "\n").getBytes("utf-8"));
               }

               String var11;
               try {
                  var11 = MainFrame.thisinstance.mp.getDMFV().bm.docinfo.get("name") + " " + MainFrame.thisinstance.mp.getDMFV().bm.docinfo.get("ver");
               } catch (Exception var23) {
                  var11 = "nincs betöltve nyomtatvány";
               }

               var9.write(("Nyomtatvány verzió: " + var11 + "\n").getBytes("utf-8"));
               var9.write(("Operációs rendszer: " + this.ps.get("os.name") + " " + this.ps.get("sun.os.patch.level") + "\n").getBytes("utf-8"));
               var9.write(("Java verziób" + this.ps.get("java.vm.vendor") + " verzió : " + this.ps.get("java.version") + var10 + "\n").getBytes("utf-8"));
               var9.write(("Java info" + this.ps.get("java.vendor") + " - " + this.ps.get("java.runtime.name") + "\n").getBytes("utf-8"));
               var9.write(("Indítási könyvtár: " + this.ps.get("user.dir") + "\n").getBytes("utf-8"));
               var9.write(("Felhasználói könyvtár: " + Tools.beautyPath(System.getProperty("user.home")) + "\n").getBytes("utf-8"));
               var9.write(("Postázó könyvtár (KRDIR): " + Tools.beautyPath((String)this.iplMaster.get("prop.usr.krdir")) + "\n").getBytes("utf-8"));
               var9.write(("Nyomtatványok telepítésének helye: " + Tools.beautyPath((String)this.iplMaster.get("prop.dynamic.templates.absolutepath")) + "\n").getBytes("utf-8"));
               var9.write(("Mentett bevallások helye: " + Tools.beautyPath(this.iplMaster.get("prop.usr.root") + File.separator + this.iplMaster.get("prop.usr.saves")) + "\n").getBytes("utf-8"));
               var9.write(("Kontroll állományok helye: " + Tools.beautyPath(this.iplMaster.get("prop.usr.root") + File.separator + this.iplMaster.get("prop.usr.kontroll")) + "\n").getBytes("utf-8"));
               var9.write(("Törzsadatok helye: " + Tools.beautyPath((String)this.iplMaster.get("prop.usr.primaryaccounts")) + "\n").getBytes("utf-8"));
               var9.write(("Import állományok helye: " + Tools.beautyPath(this.iplMaster.get("prop.usr.root") + File.separator + this.iplMaster.get("prop.usr.import")) + "\n").getBytes("utf-8"));
               var9.write(("Paraméter-állományok útvonala: " + Tools.beautyPath(this.iplMaster.get("prop.sys.root") + File.separator + "eroforrasok") + "\n").getBytes("utf-8"));
               var9.write(("Abevjavapath.cfg állomány tartalma: " + this.abevJavaCfgPathAndContent[1] + "\n").getBytes("utf-8"));
               var9.write(("Abevjavapath.cfg állomány helye: " + this.abevJavaCfgPathAndContent[0] + "\n").getBytes("utf-8"));
               var9.write("\n".getBytes("utf-8"));
               String var12 = this.getOrphanOrgids4Save();
               if (var2.size() <= 0 && "".equals(var12)) {
                  var9.write("Nincs elérhető szervezet-információ\n".getBytes("utf-8"));
               } else {
                  var9.write("Elérhető szervezet-információk:\n".getBytes("utf-8"));
                  Enumeration var13 = var2.keys();
                  int var14 = 0;

                  while(var13.hasMoreElements()) {
                     ++var14;
                     Object var15 = var13.nextElement();
                     Hashtable var16 = (Hashtable)((Hashtable)var1.getOrgInfo(var15)).get("attributes");
                     String var17 = (String)var16.get("successorid");
                     var9.write(("Szervezet neve : " + var16.get("orgname") + "\n").getBytes("utf-8"));
                     var9.write(("Ügyfélszolgálat : " + var16.get("ugyfelszolgalaturl") + "\n").getBytes("utf-8"));
                  }

                  var9.write((var12 + "\n").getBytes("utf-8"));
                  this.sb1.append(this.setHtmlFoot());
               }

               var9.write("\n".getBytes("utf-8"));
               var9.write("Aktuális információk az interneten:\n".getBytes("utf-8"));
               var9.write("Információk a nyomtatványkitöltő program használatához : https://nav.gov.hu/nyomtatvanyok/letoltesek/nyomtatvanykitolto_programok/nyomtatvany_apeh/keretprogramok/javakitolto\n".getBytes("utf-8"));
               var9.write("Nyomtatványokkal kapcsolatos részletes információk: http://nav.gov.hu/nav/letoltesek".getBytes("utf-8"));
               HashMap var27 = BlacklistStore.getInstance().getFullList();
               if (var27 != null || !var27.isEmpty()) {
                  var9.write("\n\n".getBytes("utf-8"));
                  var9.write("Az ÁNYK programban már nem használható nyomtatványok listája (név; megjegyzés; online elérés):\n".getBytes("utf-8"));
                  Iterator var28 = var27.entrySet().iterator();

                  while(var28.hasNext()) {
                     Entry var29 = (Entry)var28.next();
                     var9.write((this.getViewKey((String)var29.getKey(), ((Blacklist.Template)var29.getValue()).getOrg()) + "; " + ((Blacklist.Template)var29.getValue()).getMessage() + "; " + ((Blacklist.Template)var29.getValue()).getTargetUrl() + "\n").getBytes("utf-8"));
                  }
               }
            } finally {
               var9.close();
            }
         }
      }

   }

   private String getOrphanOrgids4Save() {
      StringBuilder var1 = new StringBuilder("\nNyomtatványokban található paraméter állomány nélküli szervezet azonosítók: ");
      String[] var2 = TemplateUtils.getInstance().getTemplateOrphanOrgids();
      int var3 = 0;
      String[] var4 = var2;
      int var5 = var2.length;

      int var6;
      String var7;
      for(var6 = 0; var6 < var5; ++var6) {
         var7 = var4[var6];
         var1.append(var7).append("; ");
         ++var3;
      }

      var1.append("\n");
      var1.append("\nSegédletekben található paraméter állomány nélküli szervezet azonosítók: ");
      var2 = TemplateUtils.getInstance().getHelpOrphanOrgids();
      var4 = var2;
      var5 = var2.length;

      for(var6 = 0; var6 < var5; ++var6) {
         var7 = var4[var6];
         var1.append(var7).append("; ");
         ++var3;
      }

      var1.append("\n");
      return var3 == 0 ? "" : var1.toString();
   }

   private String getViewKey(String var1, String var2) {
      if (var2 == null) {
         return var1;
      } else {
         return var1.startsWith(var2) ? var1.substring(var2.length() + 1) : var1;
      }
   }
}
