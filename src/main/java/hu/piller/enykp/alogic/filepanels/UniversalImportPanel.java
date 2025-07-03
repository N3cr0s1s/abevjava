package hu.piller.enykp.alogic.filepanels;

import hu.piller.enykp.alogic.filesaver.vid.EnykVidSaver;
import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.orghandler.OrgHandler;
import hu.piller.enykp.alogic.settingspanel.upgrade.UpgradeFunction;
import hu.piller.enykp.alogic.templateutils.blacklist.BlacklistStore;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.framework.MainPanel;
import hu.piller.enykp.gui.framework.Menubar;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

public class UniversalImportPanel extends JPanel {
   static JDialog dia;
   JTextField source;
   JTextField dest;
   File frm;
   File tem;
   Hashtable attrs;
   static JPanel msgPanel;
   static int calculatedWidth;

   public UniversalImportPanel() {
      this.build();
   }

   public static void showDialog() {
      String var0 = "Technikai áttöltés új nyomtatványba";
      dia = new JDialog(MainFrame.thisinstance, var0, true);
      UniversalImportPanel var1 = new UniversalImportPanel();
      dia.getContentPane().add(var1);
      dia.setSize((int)var1.getPreferredSize().getWidth() + GuiUtil.getW("WW"), (int)(var1.getPreferredSize().getHeight() + (double)(2 * GuiUtil.getCommonItemHeight())));
      dia.setPreferredSize(dia.getSize());
      dia.setMinimumSize(dia.getSize());
      dia.setResizable(true);
      dia.setLocationRelativeTo(MainFrame.thisinstance);
      dia.show();
   }

   private void build() {
      this.setLayout(new BorderLayout());
      this.add(this.buildcenter(), "Center");
      this.add(this.buildbuttons(), "South");
   }

   private Component buildcenter() {
      JPanel var1 = new JPanel();
      var1.setLayout((LayoutManager)null);
      String var2 = "<html><h2><font color=\"red\">Figyelem!</font></h2>A funkció használata körültekintést igényel.<br>A különböző típusú nyomtatványok adatai nem minden esetben feleltethetők meg egymásnak.<br>A program a kimaradó adatokról listát készít, de emellett is előfordulhat adateltérés, ezért az áttöltés után tételesen ellenőrizze az átvitt értékeket!<br><br></html>";
      String[] var3 = new String[]{"A funkció használata körültekintést igényel.", "A különböző típusú nyomtatványok adatai nem minden esetben feleltethetők meg egymásnak.", "A program a kimaradó adatokról listát készít, de emellett is előfordulhat adateltérés, ezért az áttöltés után tételesen ellenőrizze az átvitt értékeket!"};
      this.createPanelFromText(var3);
      int var4 = (int)(0.5D * (double)GuiUtil.getCommonItemHeight());
      int var5 = msgPanel.getHeight() + 5;
      var1.add(msgPanel);
      JPanel var6 = this.buildnorth();
      var6.setBounds(10, var5, calculatedWidth, var6.getHeight());
      var5 += var6.getHeight();
      var5 += var4;
      var1.add(var6);
      JPanel var7 = this.buildsouth();
      var7.setBounds(10, var5, calculatedWidth, var7.getHeight());
      var5 += var7.getHeight();
      var5 += var4;
      var1.add(var7);
      var1.setSize(new Dimension(calculatedWidth, var5));
      var1.setPreferredSize(var1.getSize());
      var1.setMinimumSize(var1.getSize());
      return var1;
   }

   private JPanel buildnorth() {
      JPanel var1 = new JPanel((LayoutManager)null);
      JLabel var2 = new JLabel("Kiinduló adatállomány:");
      byte var3 = 10;
      GuiUtil.setDynamicBound(var2, var2.getText(), 10, var3);
      var1.add(var2);
      int var6 = var3 + (int)(1.2D * (double)GuiUtil.getCommonItemHeight());
      JButton var4 = new JButton("...");
      int var5 = GuiUtil.getW(var4, var4.getText());
      this.source = new JTextField();
      this.source.setEditable(false);
      this.source.setBounds(10, var6, calculatedWidth - 2 * var5, (int)(1.2D * (double)GuiUtil.getCommonItemHeight()));
      var1.add(this.source);
      var4.setBounds(GuiUtil.getPositionFromPrevComponent(this.source), var6, var5, (int)(1.2D * (double)GuiUtil.getCommonItemHeight()));
      var6 += (int)(1.5D * (double)GuiUtil.getCommonItemHeight());
      var4.setName("fc2");
      var4.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            UniversalImportPanel.this.done_source(UniversalImportPanel.this.source);
         }
      });
      var1.add(var4);
      var1.setSize(new Dimension(calculatedWidth, var6));
      var1.setPreferredSize(var1.getSize());
      return var1;
   }

   private JPanel buildsouth() {
      JPanel var1 = new JPanel((LayoutManager)null);
      JLabel var2 = new JLabel("Cél nyomtatvány megadása:");
      byte var3 = 10;
      GuiUtil.setDynamicBound(var2, var2.getText(), 10, var3);
      var1.add(var2);
      int var6 = var3 + (int)(1.2D * (double)GuiUtil.getCommonItemHeight());
      JButton var4 = new JButton("...");
      int var5 = GuiUtil.getW(var4, var4.getText());
      this.dest = new JTextField();
      this.dest.setEditable(false);
      this.dest.setBounds(10, var6, calculatedWidth - 2 * var5, (int)(1.2D * (double)GuiUtil.getCommonItemHeight()));
      var1.add(this.dest);
      var4.setBounds(GuiUtil.getPositionFromPrevComponent(this.dest), var6, var5, (int)(1.2D * (double)GuiUtil.getCommonItemHeight()));
      var6 += (int)(1.5D * (double)GuiUtil.getCommonItemHeight());
      var4.setName("fc2");
      var4.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            UniversalImportPanel.this.done_dest(UniversalImportPanel.this.dest);
         }
      });
      var1.add(var4);
      var1.setSize(new Dimension(calculatedWidth, var6));
      var1.setPreferredSize(var1.getSize());
      return var1;
   }

   private Component buildbuttons() {
      JPanel var1 = new JPanel();
      JButton var2 = new JButton("Áttöltés");
      var2.setName("ok");
      var2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (UniversalImportPanel.this.frm == null) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem választott ki adatállományt!", "Hibaüzenet", 0);
            } else if (UniversalImportPanel.this.tem == null) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem választott ki nyomtatvány sablont!", "Hibaüzenet", 0);
            } else if (UniversalImportPanel.this.attrs == null) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "A sablon meghatározásához hiányoznak adatok!", "Hibaüzenet", 0);
            } else {
               UniversalImportPanel.dia.setVisible(false);
               MainFrame.thisinstance.setGlassLabel("Áttöltés folyamatban!");
               MainFrame.thisinstance.glasslock = true;
               Thread var2 = new Thread(new Runnable() {
                  public void run() {
                     UniversalImportPanel.this.done_ok();
                     MainFrame.thisinstance.glasslock = false;
                  }
               });
               var2.start();
            }
         }
      });
      var1.add(var2);
      JButton var3 = new JButton("Mégsem");
      var3.setName("cancel");
      var3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (UniversalImportPanel.dia != null) {
               UniversalImportPanel.dia.setVisible(false);
            }

         }
      });
      var1.add(var3);
      return var1;
   }

   private void done_source(JTextField var1) {
      try {
         ABEVOpenPanel var2 = new ABEVOpenPanel();
         var2.setMode("open_multi");
         var2.setPath(new File((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.saves")));
         var2.setFilters(new String[]{"inner_data_loader_v1"});
         Hashtable var3 = var2.showDialog();
         if (var3 != null && var3.size() != 0) {
            Object[] var4 = (Object[])((Object[])var3.get("selected_files"));
            File var5 = (File)((Object[])((Object[])var4[0]))[0];
            this.attrs = (Hashtable)((Hashtable)((Object[])((Object[])var4[0]))[3]).get("docinfo");
            if (this.attrs.containsKey("id") && this.attrs.containsKey("org") && BlacklistStore.getInstance().handleGuiMessage((String)this.attrs.get("id"), (String)this.attrs.get("org"))) {
               return;
            }

            this.source.setText(var5.getName());
            this.frm = var5;
         }
      } catch (Exception var6) {
      }

   }

   private void done_dest(JTextField var1) {
      try {
         Hashtable var2 = new Hashtable();
         var2.put("emptyprint", "");
         ABEVNewPanel var3 = new ABEVNewPanel(var2);
         Hashtable var4 = var3.showDialog();
         if (var4.size() != 0) {
            Object[] var5 = (Object[])((Object[])var4.get("selected_file"));
            String[] var6 = this.getDocAndOrgId(var4.get("selected_template_docinfo"));
            if (BlacklistStore.getInstance().handleGuiMessage(var6[0], var6[1])) {
               return;
            }

            this.dest.setText(((File)var5[0]).getName());
            this.tem = (File)var5[0];
         }
      } catch (Exception var7) {
      }

   }

   private void done_ok() {
      String var1 = (String)this.attrs.get("id");
      String var2 = (String)this.attrs.get("templatever");
      String var3 = (String)this.attrs.get("org");
      PropertyList.getInstance().set("prop.dynamic.ilyenkor", "");
      File var4 = null;

      try {
         var4 = new File(TemplateChecker.getInstance().getTemplateFileNames(var1, var2, var3, UpgradeFunction.OPEN).getTemplateFileNames()[0]);
      } catch (Exception var19) {
         JOptionPane.showMessageDialog(MainFrame.thisinstance, "Hiba történt!\n" + var19.getMessage());
         MainFrame.thisinstance.glasslock = false;
         MainFrame.thisinstance.setGlassLabel((String)null);
         return;
      }

      if (var4 != null && var4.exists()) {
         BookModel var5 = new BookModel(this.tem);
         MainPanel var10001;
         if (var5.hasError) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A sablon beolvasásakor hiba történt!\n" + var5.errormsg, "Hibaüzenet", 0);
            MainFrame.thisinstance.glasslock = false;
            MainFrame.thisinstance.setGlassLabel((String)null);
            var10001 = MainFrame.thisinstance.mp;
            MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
            GuiUtil.setcurrentpagename("");
            MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
         } else {
            BookModel var6 = new BookModel(var4, this.frm);
            if (var6.hasError) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "A sablon beolvasásakor hiba történt!\n" + var6.errormsg, "Hibaüzenet", 0);
               MainFrame.thisinstance.glasslock = false;
               MainFrame.thisinstance.setGlassLabel((String)null);
               GuiUtil.setcurrentpagename("");
               MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
            } else {
               try {
                  if (var5.forms.size() != var6.forms.size()) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatvány sablonok nem összeegyeztethetőek!\nNem azonos az alnyomtatványok száma! \nAzonosítók: " + var5.id + "  " + var6.id, "Hibaüzenet", 0);
                     MainFrame.thisinstance.glasslock = false;
                     MainFrame.thisinstance.setGlassLabel((String)null);
                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                     GuiUtil.setcurrentpagename("");
                     MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                     return;
                  }

                  if (!OrgHandler.getInstance().getReDirectedOrgId((String)var5.docinfo.get("org")).equals(OrgHandler.getInstance().getReDirectedOrgId((String)var6.docinfo.get("org")))) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem egyezik a szervezet a kiválasztott állományoknál!", "Hibaüzenet", 0);
                     MainFrame.thisinstance.glasslock = false;
                     MainFrame.thisinstance.setGlassLabel((String)null);
                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                     GuiUtil.setcurrentpagename("");
                     MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                     return;
                  }

                  Hashtable var7 = new Hashtable();

                  for(int var8 = 0; var8 < var6.forms.size(); ++var8) {
                     try {
                        var7.put(((FormModel)var6.forms.get(var8)).id, ((FormModel)var5.forms.get(var8)).id);
                     } catch (Exception var18) {
                        var7.put(((FormModel)var6.forms.get(var8)).id, ((FormModel)var6.forms.get(var8)).id);
                     }
                  }

                  EnykVidSaver var21 = new EnykVidSaver(var6, true);
                  Result var9 = var21.save("vid_" + this.frm.getName(), -1, var5.name, var5.id, var7);
                  if (!var9.isOk()) {
                     if (var9.errorList != null && var9.errorList.size() != 0) {
                        new ErrorDialog(MainFrame.thisinstance, "Hibalista", true, true, var9.errorList);
                     }

                     MainFrame.thisinstance.glasslock = false;
                     MainFrame.thisinstance.setGlassLabel((String)null);
                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                     GuiUtil.setcurrentpagename("");
                     MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                     return;
                  }

                  Hashtable var10 = var21.getNotSavedData();
                  if (var10.size() != 0) {
                     Vector var11 = new Vector();
                     int var12 = 0;

                     while(true) {
                        if (var12 >= var6.cc.size()) {
                           if (var11.size() != 0) {
                              new ErrorDialog(MainFrame.thisinstance, "Áttölthetetlen mezők", true, true, var11);
                           }
                           break;
                        }

                        Elem var13 = (Elem)var6.cc.get(var12);
                        Hashtable var14 = (Hashtable)var10.get(var13);
                        if (var14.size() != 0) {
                           var11.add(new TextWithIcon(var13.getType() + " " + var13.getEtc().get("nev_vid") + " " + var13.getEtc().get("id_vid"), 2));
                           Enumeration var15 = var14.keys();

                           while(var15.hasMoreElements()) {
                              String var16 = (String)var15.nextElement();
                              String var17 = (String)var14.get(var16);
                              var11.add(new TextWithIcon("Mezőkód: " + var16.substring(var16.indexOf("_")) + "   Értéke: " + var17, 4));
                           }
                        }

                        ++var12;
                     }
                  }

                  File var22 = (File)var9.errorList.get(0);
                  var5.destroy();
                  var6.destroy();
                  PropertyList.getInstance().set("prop.dynamic.uniimport", "yes");
                  Menubar.thisinstance.cmd_file_open(var22.getAbsolutePath(), false, true, true, true, true, true);
               } catch (Exception var20) {
                  MainFrame.thisinstance.glasslock = false;
                  MainFrame.thisinstance.setGlassLabel((String)null);
               }

            }
         }
      } else {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nincs megfelelő nyomtatvány sablon! ( " + var1 + " )", "Hibaüzenet", 0);
         MainFrame.thisinstance.glasslock = false;
         MainFrame.thisinstance.setGlassLabel((String)null);
      }
   }

   private void createPanelFromText(String[] var1) {
      JLabel var2 = new JLabel();
      msgPanel = new JPanel();
      msgPanel.setLayout(new BoxLayout(msgPanel, 1));
      TitledBorder var3 = BorderFactory.createTitledBorder("Figyelem! ");
      var3.setTitleColor(Color.RED);
      msgPanel.setBorder(var3);
      calculatedWidth = (int)((double)GuiUtil.getScreenW() * 0.6D);
      int var4 = 300;
      int var5 = 1;

      try {
         int var6 = SwingUtilities.computeStringWidth(var2.getFontMetrics(var2.getFont()), var1[0]);
         String[] var7 = var1;
         int var8 = var1.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String var10 = var7[var9];

            for(var6 = SwingUtilities.computeStringWidth(var2.getFontMetrics(var2.getFont()), var10); var6 > calculatedWidth; var6 = SwingUtilities.computeStringWidth(var2.getFontMetrics(var2.getFont()), var10)) {
               double var11 = (double)calculatedWidth / (double)var6;
               int var13 = (int)((double)var10.length() * var11);
               int var14 = getBreakPosition(var10, var13);
               JLabel var15 = new JLabel(var10.substring(0, var14));
               var4 = Math.max(var4, GuiUtil.getW(var15, var15.getText() + "WWW"));
               msgPanel.add(var15);
               ++var5;
               var10 = var10.substring(var14 + 1);
            }

            msgPanel.add(new JLabel(var10));
            var4 = Math.max(var4, GuiUtil.getW(var10 + "WWWWW"));
            ++var5;
         }
      } catch (Exception var16) {
      }

      calculatedWidth = var4;
      msgPanel.setSize(new Dimension(var4, (var5 + 1) * GuiUtil.getCommonItemHeight() + 4));
      msgPanel.setMinimumSize(msgPanel.getSize());
      msgPanel.setPreferredSize(msgPanel.getSize());
      msgPanel.setBounds(10, 10, var4, (var5 + 1) * GuiUtil.getCommonItemHeight() + 4);
   }

   private static int getBreakPosition(String var0, int var1) {
      int var2;
      for(var2 = var1; var2 > 0; --var2) {
         if (var0.charAt(var2) == ' ') {
            return var2;
         }
      }

      for(var2 = var1; var2 > 0; --var2) {
         if (var0.charAt(var2) == '\\') {
            return var2;
         }

         if (var0.charAt(var2) == '/') {
            return var2;
         }

         if (var0.charAt(var2) == ',') {
            return var2;
         }
      }

      return var1;
   }

   protected String[] getDocAndOrgId(Object var1) {
      Hashtable var2 = (Hashtable)((Hashtable)var1).get("docinfo");
      return new String[]{(String)var2.get("id"), (String)var2.get("org")};
   }
}
