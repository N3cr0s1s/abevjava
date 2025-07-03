package hu.piller.enykp.alogic.settingspanel;

import hu.piller.enykp.alogic.settingspanel.setenv.Setenv;
import hu.piller.enykp.alogic.settingspanel.setenv.SetenvException;
import hu.piller.enykp.alogic.settingspanel.setenv.SetenvHandler;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.framework.Menubar;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.kauclient.KauAuthMethod;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.oshandler.OsFactory;
import me.necrocore.abevjava.NecroFile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

public class BaseSettingsPane extends JPanel {
   public static final int SUSPICIOUS_MEM_MAX = 1024;
   public static BaseSettingsPane thisinstance = null;
   protected JCheckBox calc;
   protected JCheckBox check;
   protected JCheckBox fieldcodedisplay;
   protected JCheckBox overwritemode;
   protected JCheckBox cegKapuEnabled;
   protected JTextField browserpath;
   protected JTextField dsigpath;
   protected JTextField pdfPath;
   protected JTextField calcPath;
   SettingsStore ss;
   private JPanel heapSettings;
   private JFormattedTextField heapMin;
   private JFormattedTextField heapMax;
   private JLabel heapTxt1;
   private JLabel heapTxt2;
   private JLabel heapTxt3;
   private SetenvHandler setenvHandler;
   private Setenv currSetenv;
   private int oriHeapMax;
   private int oriHeapMin;
   private FileFilter exeFilter;
   private int commonItemHeight2 = GuiUtil.getCommonItemHeight() + 2;

   public BaseSettingsPane() {
      thisinstance = this;
      Dimension var1 = GuiUtil.getSettingsDialogDimension();
      this.setPreferredSize(var1);
      this.setSize(var1);
      JPanel var2 = new JPanel((LayoutManager)null);
      Dimension var3 = new Dimension(GuiUtil.getW(KauAuthMethod.KAU_UKP.getText()) + 20, (int)var1.getHeight());
      var2.setSize(var3);
      var2.setPreferredSize(var3);
      JScrollPane var4 = new JScrollPane(var2, 20, 30);
      this.setLayout(new BorderLayout());
      this.add(var4, "Center");
      this.exeFilter = new FileFilter() {
         public boolean accept(File var1) {
            if (var1.isDirectory()) {
               return true;
            } else {
               return var1.isFile() && var1.getName().toLowerCase().endsWith(".exe");
            }
         }

         public String getDescription() {
            return "alkalmazások";
         }
      };
      this.ss = SettingsStore.getInstance();
      this.setenvHandler = new SetenvHandler();

      try {
         this.currSetenv = this.setenvHandler.load();
      } catch (SetenvException var24) {
         ErrorList.getInstance().writeError(1, "Memória beállítások betöltése sikertelen.", var24, (Object)null);
         this.currSetenv = new Setenv();
         this.currSetenv.setMemMin(0);
         this.currSetenv.setMemMax(0);
      }

      this.oriHeapMin = this.currSetenv.getMemMin();
      this.oriHeapMax = this.currSetenv.getMemMax();
      this.calc = GuiUtil.getANYKCheckBox("A program kezelje a számított mezőket");
      this.check = GuiUtil.getANYKCheckBox("A mezők elhagyásakor ellenőrzi a beírt értéket");
      this.fieldcodedisplay = GuiUtil.getANYKCheckBox("Mezőkód kijelzés");
      this.overwritemode = GuiUtil.getANYKCheckBox("Mező javításánál felülírás mód");
      this.overwritemode.setName("overwritecb");
      this.cegKapuEnabled = GuiUtil.getANYKCheckBox("Kapcsolat a Cég/Hivatali kapuval menü engedélyezése");
      this.cegKapuEnabled.setName("cegKapuEnabled");
      this.browserpath = new JTextField();
      this.browserpath.setEditable(false);
      this.pdfPath = new JTextField();
      this.pdfPath.setEditable(true);
      this.dsigpath = new JTextField();
      this.dsigpath.setEditable(false);
      this.calcPath = new JTextField();
      this.calcPath.setEditable(true);
      String var5 = this.ss.get("gui", "mezőszámítás");
      if (var5 != null && var5.equals("true")) {
         this.calc.setSelected(true);
      }

      var5 = this.ss.get("gui", "mezőellenőrzés");
      if (var5 != null && var5.equals("true")) {
         this.check.setSelected(true);
      }

      var5 = this.ss.get("gui", "mezőkódkijelzés");
      if (var5 != null && var5.equals("true")) {
         this.fieldcodedisplay.setSelected(true);
      }

      var5 = this.ss.get("gui", "felülírásmód");
      if (var5 != null && var5.equals("true")) {
         this.overwritemode.setSelected(true);
      }

      var5 = this.ss.get("gui", "cegkapu");
      if (var5 != null && var5.equals("true")) {
         this.cegKapuEnabled.setSelected(true);
      }

      if (var5 != null && "".equals(var5)) {
         this.cegKapuEnabled.setSelected(false);
         this.cegKapuEnabled.setEnabled(false);
      }

      this.calc.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent var1) {
            String var2 = BaseSettingsPane.this.calc.isSelected() ? "true" : "false";
            BaseSettingsPane.this.ss.set("gui", "mezőszámítás", var2);
            BaseSettingsPane.calchelper(BaseSettingsPane.this.calc.isSelected());
            if (!BaseSettingsPane.this.calc.isSelected()) {
               SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                     GuiUtil.showMessageDialog(SettingsDialog.thisinstance, "A számított mezők kikapcsolása mellett szerkesztett nyomtatványban a számított adatok\nés kitöltési ellenőrzések nem működnek!\nEzzel elveszíti a program nyújtotta előnyök nagy részét.\nHa szeretne visszatérni a program normális működéséhez, akkor válassza az\nAdatok menü \"Számított mezők újraszámolása\" menüpontot!\nA menüpont lefuttatása a számított mezők tartalmát felülbírálja!", "Figyelmeztetés", 2);
                  }
               });
            }

         }
      });
      this.cegKapuEnabled.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent var1) {
            String var2 = BaseSettingsPane.this.cegKapuEnabled.isSelected() ? "true" : "false";
            BaseSettingsPane.this.ss.set("gui", "cegkapu", var2);
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  GuiUtil.showMessageDialog(SettingsDialog.thisinstance, "A beállítás életbeléptetéséhez indítsa újra az ÁNYK-t", "Figyelmeztetés", 2);
               }
            });
         }
      });
      this.check.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent var1) {
            String var2 = BaseSettingsPane.this.check.isSelected() ? "true" : "false";
            BaseSettingsPane.this.ss.set("gui", "mezőellenőrzés", var2);
         }
      });
      this.fieldcodedisplay.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent var1) {
            String var2 = BaseSettingsPane.this.fieldcodedisplay.isSelected() ? "true" : "false";
            BaseSettingsPane.this.ss.set("gui", "mezőkódkijelzés", var2);
         }
      });
      this.overwritemode.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent var1) {
            String var2 = BaseSettingsPane.this.overwritemode.isSelected() ? "true" : "false";
            BaseSettingsPane.this.ss.set("gui", "felülírásmód", var2);
         }
      });
      byte var6 = 30;
      int var7 = GuiUtil.getCommonItemHeight() + 8;
      GuiUtil.setDynamicBound(this.calc, this.calc.getText(), 10, var6);
      int var25 = var6 + var7;
      GuiUtil.setDynamicBound(this.check, this.check.getText(), 10, var25);
      var25 += var7;
      GuiUtil.setDynamicBound(this.cegKapuEnabled, this.cegKapuEnabled.getText(), 10, var25);
      var25 += var7;
      GuiUtil.setDynamicBound(this.fieldcodedisplay, this.fieldcodedisplay.getText(), 10, var25);
      var25 += var7;
      GuiUtil.setDynamicBound(this.overwritemode, this.overwritemode.getText(), 10, var25);
      var25 += var7;
      var2.add(this.calc);
      var2.add(this.check);
      var2.add(this.cegKapuEnabled);
      var2.add(this.fieldcodedisplay);
      var2.add(this.overwritemode);
      JKauBrowserMultiSettingsPanel var8 = new JKauBrowserMultiSettingsPanel("Felhasználói azonosítás");
      int var9 = this.getPanelWidth();

      int var10;
      try {
         var10 = KauAuthMethod.values().length + 2;
      } catch (Exception var23) {
         var10 = 5;
      }

      var8.setBounds(10, var25, var9, var10 * (GuiUtil.getCommonItemHeight() + 4));
      var2.add(var8);
      var25 += 5 * (GuiUtil.getCommonItemHeight() + 4);
      JLabel var11 = new JLabel("internetböngésző:");
      GuiUtil.setDynamicBound(var11, var11.getText(), 10, var25);
      var25 += var7;
      int var12 = (int)(350.0D * Math.max(1.0D, (double)(GuiUtil.getCommonFontSize() / 12)));
      this.browserpath.setBounds(10, var25, var12, GuiUtil.getCommonItemHeight() + 4);
      var5 = this.ss.get("gui", "internet_browser");
      this.browserpath.setText(var5);
      var2.add(var11);
      var2.add(this.browserpath);
      this.browserpath.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            PropertyList.getInstance().set("prop.internet_browser.path", BaseSettingsPane.this.browserpath.getText());
            BaseSettingsPane.this.ss.set("gui", "internet_browser", BaseSettingsPane.this.browserpath.getText());
         }
      });
      JButton var13 = new JButton("...");
      var13.setMargin(new Insets(0, 0, 0, 0));
      var13.setBounds(GuiUtil.getPositionFromPrevComponent(this.browserpath) + 5, var25, GuiUtil.getPointsButtonWidth(), GuiUtil.getCommonItemHeight() + 4);
      var13.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            JFileChooser var2 = new JFileChooser(BaseSettingsPane.this.browserpath.getText());
            var2.setFileFilter(BaseSettingsPane.this.exeFilter);
            var2.setDialogTitle("Megnyitás");
            if (BaseSettingsPane.this.browserpath.getText() == null || BaseSettingsPane.this.browserpath.getText().length() == 0) {
               var2.setCurrentDirectory(new NecroFile(OsFactory.getOsHandler().getProgramFilesDir()));
            }

            int var3 = var2.showOpenDialog(BaseSettingsPane.thisinstance);
            if (var3 == 0) {
               BaseSettingsPane.this.browserpath.setText(var2.getSelectedFile().getAbsolutePath());
               PropertyList.getInstance().set("prop.internet_browser.path", BaseSettingsPane.this.browserpath.getText());
               BaseSettingsPane.this.ss.set("gui", "internet_browser", BaseSettingsPane.this.browserpath.getText());
            }

         }
      });
      var2.add(var13);
      JButton var14 = new JButton("Törlés");
      int var15 = GuiUtil.getW(var14, "Törlés");
      var14.setMargin(new Insets(0, 0, 0, 0));
      var14.setBounds(GuiUtil.getPositionFromPrevComponent(var13) + 5, var25, var15, GuiUtil.getCommonItemHeight() + 4);
      var14.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            BaseSettingsPane.this.browserpath.setText((String)null);
            PropertyList.getInstance().set("prop.internet_browser.path", BaseSettingsPane.this.browserpath.getText());
            BaseSettingsPane.this.ss.set("gui", "internet_browser", BaseSettingsPane.this.browserpath.getText());
         }
      });
      var2.add(var14);
      var25 += var7;
      JLabel var16 = new JLabel("pdf megjelenítő:");
      GuiUtil.setDynamicBound(var16, var16.getText(), 10, var25);
      var25 += var7;
      this.pdfPath.setBounds(10, var25, var12, GuiUtil.getCommonItemHeight() + 4);
      var5 = this.ss.get("gui", "pdf_viewer");
      this.pdfPath.setText(var5);
      var2.add(var16);
      var2.add(this.pdfPath);
      this.pdfPath.addFocusListener(new FocusAdapter() {
         public void focusLost(FocusEvent var1) {
            PropertyList.getInstance().set("prop.pdf_viewer.path", BaseSettingsPane.this.pdfPath.getText());
            BaseSettingsPane.this.ss.set("gui", "pdf_viewer", BaseSettingsPane.this.pdfPath.getText());
         }
      });
      JButton var17 = new JButton("...");
      var17.setMargin(new Insets(0, 0, 0, 0));
      var17.setBounds(GuiUtil.getPositionFromPrevComponent(this.pdfPath) + 5, var25, GuiUtil.getPointsButtonWidth(), GuiUtil.getCommonItemHeight() + 4);
      var17.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            JFileChooser var2 = new JFileChooser(BaseSettingsPane.this.pdfPath.getText());
            var2.setFileFilter(BaseSettingsPane.this.exeFilter);
            var2.setDialogTitle("Megnyitás");
            if (BaseSettingsPane.this.pdfPath.getText() == null || BaseSettingsPane.this.pdfPath.getText().length() == 0) {
               var2.setCurrentDirectory(new NecroFile(OsFactory.getOsHandler().getProgramFilesDir()));
            }

            int var3 = var2.showOpenDialog(BaseSettingsPane.thisinstance);
            if (var3 == 0) {
               BaseSettingsPane.this.pdfPath.setText(var2.getSelectedFile().getAbsolutePath());
               PropertyList.getInstance().set("prop.pdf_viewer.path", BaseSettingsPane.this.pdfPath.getText());
               BaseSettingsPane.this.ss.set("gui", "pdf_viewer", BaseSettingsPane.this.pdfPath.getText());
            }

         }
      });
      var2.add(var17);
      JButton var18 = new JButton("Törlés");
      var18.setMargin(new Insets(0, 0, 0, 0));
      var18.setBounds(GuiUtil.getPositionFromPrevComponent(var17) + 5, var25, var15, GuiUtil.getCommonItemHeight() + 4);
      var18.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            BaseSettingsPane.this.pdfPath.setText((String)null);
            PropertyList.getInstance().set("prop.pdf_viewer.path", BaseSettingsPane.this.pdfPath.getText());
            BaseSettingsPane.this.ss.set("gui", "pdf_viewer", BaseSettingsPane.this.pdfPath.getText());
         }
      });
      var2.add(var18);
      var25 += var7;
      JLabel var19 = new JLabel("digitális aláírás könyvtára:");
      GuiUtil.setDynamicBound(var19, var19.getText(), 10, var25);
      var25 += var7;
      this.dsigpath.setBounds(10, var25, var12, GuiUtil.getCommonItemHeight() + 4);
      var5 = this.ss.get("gui", "digitális_aláírás");
      if (var5 == null || var5.equals("")) {
         String var20 = Tools.fillPath((String)PropertyList.getInstance().get("prop.usr.krdir")) + (String)PropertyList.getInstance().get("prop.usr.ds_src");
         var5 = Tools.beautyPath(var20);
      }

      this.dsigpath.setText(var5);
      var2.add(var19);
      var2.add(this.dsigpath);
      this.dsigpath.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            PropertyList.getInstance().set("prop.usr.ds_src2", BaseSettingsPane.this.dsigpath.getText());
            BaseSettingsPane.this.ss.set("gui", "digitális_aláírás", BaseSettingsPane.this.dsigpath.getText());
         }
      });
      var13 = new JButton("...");
      var13.setMargin(new Insets(0, 0, 0, 0));
      var13.setBounds(GuiUtil.getPositionFromPrevComponent(this.dsigpath) + 5, var25, GuiUtil.getPointsButtonWidth(), GuiUtil.getCommonItemHeight() + 4);
      var13.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            JFileChooser var2 = new JFileChooser(BaseSettingsPane.this.dsigpath.getText());
            var2.setDialogTitle("Kiválasztás");
            var2.setFileSelectionMode(1);
            if (BaseSettingsPane.this.dsigpath.getText() == null || BaseSettingsPane.this.dsigpath.getText().length() == 0) {
               var2.setCurrentDirectory(new NecroFile(OsFactory.getOsHandler().getUserHomeDir()));
            }

            int var3 = var2.showOpenDialog(BaseSettingsPane.thisinstance);
            if (var3 == 0) {
               if (Tools.checkIfPathEqualsSendPath(var2.getSelectedFile().getAbsolutePath())) {
                  GuiUtil.showMessageDialog(BaseSettingsPane.this, "Ez a könyvtár nem lehet egyben a küldendő fájlok helye is!", "Beállítások", 0);
                  return;
               }

               BaseSettingsPane.this.dsigpath.setText(var2.getSelectedFile().getAbsolutePath());
               PropertyList.getInstance().set("prop.usr.ds_src2", BaseSettingsPane.this.dsigpath.getText());
               BaseSettingsPane.this.ss.set("gui", "digitális_aláírás", BaseSettingsPane.this.dsigpath.getText());
            }

         }
      });
      var2.add(var13);
      var14 = new JButton("Törlés");
      var14.setMargin(new Insets(0, 0, 0, 0));
      var14.setBounds(GuiUtil.getPositionFromPrevComponent(var13) + 5, var25, var15, GuiUtil.getCommonItemHeight() + 4);
      var14.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            BaseSettingsPane.this.dsigpath.setText((String)null);
            PropertyList.getInstance().set("prop.usr.ds_src2", BaseSettingsPane.this.dsigpath.getText());
            BaseSettingsPane.this.ss.set("gui", "digitális_aláírás", BaseSettingsPane.this.dsigpath.getText());
         }
      });
      var2.add(var14);
      var25 += var7;
      JLabel var26 = new JLabel("számológép:");
      GuiUtil.setDynamicBound(var26, var26.getText(), 10, var25);
      var25 += var7;
      this.calcPath.setBounds(10, var25, var12, GuiUtil.getCommonItemHeight() + 4);
      var5 = this.ss.get("gui", "sys_calculator");
      this.calcPath.setText(var5);
      var2.add(var26);
      var2.add(this.calcPath);
      this.calcPath.addFocusListener(new FocusAdapter() {
         public void focusLost(FocusEvent var1) {
            PropertyList.getInstance().set("prop.sys_calculator.path", BaseSettingsPane.this.calcPath.getText());
            BaseSettingsPane.this.ss.set("gui", "sys_calculator", BaseSettingsPane.this.calcPath.getText());
         }
      });
      JButton var21 = new JButton("...");
      var21.setMargin(new Insets(0, 0, 0, 0));
      var21.setBounds(GuiUtil.getPositionFromPrevComponent(this.calcPath) + 5, var25, GuiUtil.getPointsButtonWidth(), GuiUtil.getCommonItemHeight() + 4);
      var21.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            JFileChooser var2 = new JFileChooser(BaseSettingsPane.this.calcPath.getText());
            var2.setDialogTitle("Megnyitás");
            if (BaseSettingsPane.this.calcPath.getText() == null || BaseSettingsPane.this.calcPath.getText().length() == 0) {
               var2.setCurrentDirectory(new NecroFile(OsFactory.getOsHandler().getProgramFilesDir()));
            }

            int var3 = var2.showOpenDialog(BaseSettingsPane.thisinstance);
            if (var3 == 0) {
               BaseSettingsPane.this.calcPath.setText(var2.getSelectedFile().getAbsolutePath());
               PropertyList.getInstance().set("prop.sys_calculator.path", BaseSettingsPane.this.calcPath.getText());
               BaseSettingsPane.this.ss.set("gui", "sys_calculator", BaseSettingsPane.this.calcPath.getText());
            }

         }
      });
      var2.add(var21);
      JButton var22 = new JButton("Törlés");
      var22.setMargin(new Insets(0, 0, 0, 0));
      var22.setBounds(GuiUtil.getPositionFromPrevComponent(var21) + 5, var25, var15, GuiUtil.getCommonItemHeight() + 4);
      var22.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            BaseSettingsPane.this.calcPath.setText((String)null);
            PropertyList.getInstance().set("prop.sys_calculator.path", BaseSettingsPane.this.calcPath.getText());
            BaseSettingsPane.this.ss.set("gui", "sys_calculator", BaseSettingsPane.this.calcPath.getText());
         }
      });
      var2.add(var22);
      var25 += var7;
      var2.add(this.getHeapSettings(var25));
      var25 += var7;
      var25 += 12;
      var2.add(JUploadBandwithSettingsPanel.create(var25 + 8));
   }

   private JPanel getHeapSettings(int var1) {
      if (this.heapSettings == null) {
         this.heapTxt1 = new JLabel("Az ÁNYK minimum");
         this.heapTxt2 = new JLabel("MB, és maximum");
         this.heapTxt3 = new JLabel("MB memóriát használhat.");
         this.heapMin = new JFormattedTextField();
         this.heapMin.setName("heapMin");

         MaskFormatter var2;
         try {
            var2 = new MaskFormatter("####");
            var2.setPlaceholder("_");
            var2.setAllowsInvalid(false);
            this.heapMin.setFormatterFactory(new DefaultFormatterFactory(var2));
            var2.install(this.heapMin);
            this.heapMin.setFocusLostBehavior(0);
         } catch (Exception var4) {
         }

         this.heapMin.setMinimumSize(new Dimension(80, this.commonItemHeight2));
         this.heapMin.setMaximumSize(this.heapMin.getMinimumSize());
         this.heapMin.setPreferredSize(this.heapMin.getMinimumSize());
         this.heapMin.setHorizontalAlignment(4);
         this.heapMin.setText(Integer.toString(this.currSetenv.getMemMin()));
         this.heapMin.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent var1) {
            }

            public void focusLost(FocusEvent var1) {
               JFormattedTextField var2 = (JFormattedTextField)var1.getSource();
               String var3 = var2.getText().trim();
               if ("".equals(var3)) {
                  GuiUtil.showMessageDialog(BaseSettingsPane.this, "A mező nem hagyható üresen!\nAz alapértelmezett minimum értékkel (128MB) kerül feltöltésre", "Érvénytelen minimum érték", 0);
                  var2.setValue("128");
                  var3 = "128";
               }

               BaseSettingsPane.this.currSetenv.setMemMin(Integer.parseInt(var3));
            }
         });
         this.heapMax = new JFormattedTextField();
         this.heapMax.setName("heapMax");

         try {
            var2 = new MaskFormatter("####");
            var2.setPlaceholder("_");
            var2.setAllowsInvalid(false);
            this.heapMax.setFormatterFactory(new DefaultFormatterFactory(var2));
            var2.install(this.heapMax);
            this.heapMax.setFocusLostBehavior(0);
         } catch (Exception var3) {
         }

         this.heapMax.setMinimumSize(new Dimension(80, this.commonItemHeight2));
         this.heapMax.setMaximumSize(this.heapMax.getMinimumSize());
         this.heapMax.setPreferredSize(this.heapMax.getMinimumSize());
         this.heapMax.setHorizontalAlignment(4);
         this.heapMax.setText(Integer.toString(this.currSetenv.getMemMax()));
         this.heapMax.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent var1) {
            }

            public void focusLost(FocusEvent var1) {
               JFormattedTextField var2 = (JFormattedTextField)var1.getSource();
               String var3 = var2.getText().trim();
               if ("".equals(var3)) {
                  GuiUtil.showMessageDialog(BaseSettingsPane.this, "A mező nem hagyható üresen!\nAz alapértelmezett maximum értékkel (256MB) kerül feltöltésre", "Érvénytelen maximum érték", 0);
                  var2.setValue("256");
                  var3 = "256";
               }

               BaseSettingsPane.this.currSetenv.setMemMax(Integer.parseInt(var3));
            }
         });
         this.heapSettings = new JPanel();
         int var5 = GuiUtil.getW(this.heapTxt1, this.heapTxt1.getText()) + 10 + (int)this.heapMin.getPreferredSize().getWidth() + 10 + GuiUtil.getW(this.heapTxt2, this.heapTxt2.getText()) + 10 + (int)this.heapMax.getPreferredSize().getWidth() + 10 + GuiUtil.getW(this.heapTxt3, this.heapTxt3.getText()) + 20;
         this.heapSettings.setLayout(new BoxLayout(this.heapSettings, 0));
         this.heapSettings.add(this.heapTxt1);
         this.heapSettings.add(Box.createHorizontalStrut(10));
         this.heapSettings.add(this.heapMin);
         this.heapSettings.add(Box.createHorizontalStrut(10));
         this.heapSettings.add(this.heapTxt2);
         this.heapSettings.add(Box.createHorizontalStrut(10));
         this.heapSettings.add(this.heapMax);
         this.heapSettings.add(Box.createHorizontalStrut(10));
         this.heapSettings.add(this.heapTxt3);
         this.heapSettings.add(Box.createHorizontalGlue());
         this.heapSettings.setSize(new Dimension(var5, this.commonItemHeight2));
         this.heapSettings.setBounds(10, var1, var5, this.commonItemHeight2);
         this.heapSettings.setPreferredSize(this.heapSettings.getSize());
         this.heapSettings.setMinimumSize(this.heapSettings.getSize());
      }

      return this.heapSettings;
   }

   public String saveSetenv() {
      String var1 = null;

      try {
         if (Integer.parseInt(this.heapMin.getText().trim()) == 0 && Integer.parseInt(this.heapMax.getText().trim()) == 0) {
            var1 = "RÉrvénytelen értékek találhatók a minimum és maximum mezőkben, nem mentjük!";
         } else if (Integer.parseInt(this.heapMin.getText().trim()) == 0) {
            var1 = "XA minimum érték érvénytelen";
         } else if (Integer.parseInt(this.heapMin.getText().trim()) == 0) {
            var1 = "XA maximum érték érvénytelen";
         } else if (this.nemJava_1_9()) {
            try {
               this.currSetenv.check();
            } catch (SetenvException var4) {
               ErrorList.getInstance().writeError(2000, "A beállított memóriaértékek érvénytelenek.", 3000, var4, (Object)null);
               var1 = "X" + var4.getMessage();
            }
         }
      } catch (Exception var5) {
         var1 = "X" + var5.getMessage();
      }

      try {
         if (var1 == null) {
            if (this.hasMemChanged()) {
               this.setenvHandler.store(this.currSetenv);
            }

            var1 = "SAVED";
         }
      } catch (SetenvException var3) {
         var1 = "R" + var3.getMessage();
         ErrorList.getInstance().writeError(1, "Memória beállítások mentése sikertelen!\nA részleteket az Üzenetek párbeszédablakban találja!", 3000, var3, (Object)null);
      }

      return var1;
   }

   public boolean hasMemChanged() {
      return this.oriHeapMax != this.currSetenv.getMemMax() || this.oriHeapMin != this.currSetenv.getMemMin();
   }

   public boolean isSuspiciousMax() {
      try {
         return Integer.parseInt(this.heapMax.getText()) > 1024;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public static void calchelper(boolean var0, BookModel var1) {
      try {
         FormModel var2 = var1.get();
         Hashtable var3 = var2.fids;
         Enumeration var4 = var3.keys();

         while(var4.hasMoreElements()) {
            String var5 = (String)var4.nextElement();
            DataFieldModel var6 = (DataFieldModel)var3.get(var5);
            if (var6.orig_readonly && var6.writeable) {
               var6.readonly = var0;
            }
         }

         MainFrame.thisinstance.mp.getDMFV().fv.pv.refresh();
      } catch (Exception var7) {
      }

      done_menuextratext(var0);
      if (!var0) {
         PropertyList.getInstance().set("prop.dynamic.dirty2", Boolean.TRUE);
      }

   }

   public static void calchelper(boolean var0) {
      try {
         BookModel var1 = MainFrame.thisinstance.mp.getDMFV().bm;
         FormModel var2 = var1.get();
         Hashtable var3 = var2.fids;
         Enumeration var4 = var3.keys();

         while(var4.hasMoreElements()) {
            String var5 = (String)var4.nextElement();
            DataFieldModel var6 = (DataFieldModel)var3.get(var5);
            if (var6.orig_readonly && var6.writeable) {
               var6.readonly = var0;
            }
         }

         MainFrame.thisinstance.mp.getDMFV().fv.pv.refresh();
      } catch (Exception var7) {
      }

      done_menuextratext(var0);
      if (!var0) {
         PropertyList.getInstance().set("prop.dynamic.dirty2", Boolean.TRUE);
      }

   }

   public static void done_menuextratext(boolean var0) {
      if (!var0) {
         Menubar.thisinstance.menuextratext = "Kikapcsolt a számított mezők kezelése!";
      } else {
         Menubar.thisinstance.menuextratext = "";
         SettingsStore.getInstance().set("gui", "mezőszámítás", "true");
      }

      try {
         Menubar.thisinstance.repaint();
      } catch (Exception var2) {
      }

   }

   public boolean nemJava_1_9() {
      try {
         return System.getProperty("java.runtime.version").indexOf("1.8") == -1;
      } catch (Exception var2) {
         return false;
      }
   }

   private int getPanelWidth() {
      String var1 = "";

      for(int var2 = 0; var2 < KauAuthMethod.values().length; ++var2) {
         if (KauAuthMethod.values()[var2].getText().length() > var1.length()) {
            var1 = KauAuthMethod.values()[var2].getText();
         }
      }

      return GuiUtil.getW(new JLabel(var1), var1 + "W") + 40;
   }
}
