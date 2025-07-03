package hu.piller.enykp.alogic.settingspanel.printer;

import hu.piller.enykp.alogic.filepanels.attachement.EJFileChooser;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import me.necrocore.abevjava.NecroFile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicFileChooserUI;

public class PrinterSettings extends JPanel implements ChangeListener {
   public final Properties settingsProps = new Properties();
   private static final int SZINES = 1;
   private static final int SZURKE = 10;
   private static final int FF = 12;
   private ButtonGroup cbg;
   private JSpinner nyomtatoMargo;
   private JCheckBox commonId;
   private JCheckBox importId;
   private JCheckBox veszpremiId;
   private JCheckBox enablePSMode;
   private JRadioButton ff;
   private JRadioButton szi;
   private JLabel nyomtatoMargoCimke;
   private JLabel pdfDirCimke;
   private JTextField pdfDirField;
   private EJFileChooser fc;

   public PrinterSettings() {
      this.build();
      this.prepare();
   }

   private void build() {
      Dimension var1 = GuiUtil.getSettingsDialogDimension();
      var1 = new Dimension((int)var1.getWidth(), GuiUtil.getScreenH() / 2);
      this.setPreferredSize(var1);
      this.setSize(var1);
      JPanel var2 = new JPanel((LayoutManager)null);
      var2.setSize(new Dimension(GuiUtil.getW("PostScript optimalizált nyomtatási mód engedélyezése WWWWWWWWWWW") + 20, (int)var1.getHeight()));
      var2.setPreferredSize(var2.getSize());
      JScrollPane var3 = new JScrollPane(var2, 20, 30);
      var3.setSize(var2.getSize());
      var3.setPreferredSize(var2.getSize());
      this.setLayout(new BorderLayout());
      this.add(var3, "Center");
      int var4 = GuiUtil.getCommonItemHeight() + 8;
      JPanel var6 = new JPanel();
      JPanel var7 = new JPanel();
      JPanel var8 = new JPanel();
      JPanel var9 = new JPanel();
      JPanel var10 = new JPanel();
      TitledBorder var11 = BorderFactory.createTitledBorder("Nyomtatás színe");
      TitledBorder var12 = BorderFactory.createTitledBorder("Lap margója");
      TitledBorder var13 = BorderFactory.createTitledBorder("Azonosítók nyomtatása");
      TitledBorder var14 = BorderFactory.createTitledBorder("Egyéb beállítások");
      TitledBorder var15 = BorderFactory.createTitledBorder("Kivonatolt nyomtatás");
      var6.setBorder(var11);
      var6.setLayout((LayoutManager)null);
      var7.setBorder(var12);
      var8.setBorder(var13);
      var9.setBorder(var14);
      var10.setBorder(var15);
      this.cbg = new ButtonGroup();
      this.ff = GuiUtil.getANYKRadioButton("Fekete-fehér nyomtatás", false);
      GuiUtil.setDynamicBound(this.ff, this.ff.getText(), 10, var4);
      this.szi = GuiUtil.getANYKRadioButton("Színes nyomtatás", false);
      GuiUtil.setDynamicBound(this.szi, this.szi.getText(), 10, 2 * var4);
      this.cbg.add(this.ff);
      this.cbg.add(this.szi);
      var6.add(this.ff);
      var6.add(this.szi);
      this.nyomtatoMargoCimke = new JLabel("Margó : ");
      GuiUtil.setDynamicBound(this.nyomtatoMargoCimke, this.nyomtatoMargoCimke.getText(), 10, var4);
      this.nyomtatoMargo = new JSpinner(new SpinnerNumberModel(10, 0, 100, 1));
      this.nyomtatoMargo.setBounds(GuiUtil.getPositionFromPrevComponent(this.nyomtatoMargoCimke), var4, 100, GuiUtil.getCommonItemHeight() + 4);
      var7.setBorder(var12);
      var7.setLayout((LayoutManager)null);
      var7.add(this.nyomtatoMargoCimke);
      var7.add(this.nyomtatoMargo);
      this.commonId = GuiUtil.getANYKCheckBox("Közös azonosító nyomtatása ", false);
      this.commonId.addChangeListener(this);
      this.commonId.setName("cId");
      GuiUtil.setDynamicBound(this.commonId, this.commonId.getText(), 10, var4);
      int var5 = var4 + var4;
      this.importId = GuiUtil.getANYKCheckBox("Import azonosító nyomtatása ", false);
      GuiUtil.setDynamicBound(this.importId, this.importId.getText(), 10, var5);
      var5 += var4;
      this.importId.addChangeListener(this);
      this.importId.setName("iId");
      this.veszpremiId = GuiUtil.getANYKCheckBox("Belső azonosító nyomtatása ", false);
      GuiUtil.setDynamicBound(this.veszpremiId, this.veszpremiId.getText(), 10, var5);
      this.veszpremiId.addChangeListener(this);
      this.veszpremiId.setName("vId");
      var8.setBorder(var13);
      var8.setLayout((LayoutManager)null);
      var8.add(this.commonId);
      var8.add(this.importId);
      var8.add(this.veszpremiId);
      this.pdfDirCimke = new JLabel("A PDF állományok mentési könyvtára: ");
      GuiUtil.setDynamicBound(this.pdfDirCimke, this.pdfDirCimke.getText(), 20, var4);
      this.pdfDirField = new JTextField();
      this.pdfDirField.setBounds(GuiUtil.getPositionFromPrevComponent(this.pdfDirCimke) + 5, var4, GuiUtil.getW("WWWWWWWWWWWWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 4);
      this.pdfDirField.setEditable(false);
      this.fc = new EJFileChooser();
      this.fc.setFileSelectionMode(1);
      JButton var16 = new JButton("...");
      JButton var17 = new JButton("Törlés");
      var16.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            PrinterSettings.this.initDialog("Válassza ki a PDF állományok mentési könyvtárát!", PrinterSettings.this.pdfDirField.getText());
            int var2 = PrinterSettings.this.fc.showOpenDialog(PrinterSettings.this);
            if (var2 == 0) {
               PrinterSettings.this.pdfDirField.setText(PrinterSettings.this.fc.getSelectedFile().getAbsolutePath());
               PrinterSettings.this.pdfDirField.setToolTipText(PrinterSettings.this.pdfDirField.getText());
            }

         }
      });
      var17.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            PrinterSettings.this.pdfDirField.setText("");
            PrinterSettings.this.pdfDirField.setToolTipText(PrinterSettings.this.pdfDirField.getText());
         }
      });
      var16.setBounds(GuiUtil.getPositionFromPrevComponent(this.pdfDirField) + 5, var4, GuiUtil.getPointsButtonWidth(), GuiUtil.getCommonItemHeight() + 4);
      var17.setBounds(GuiUtil.getPositionFromPrevComponent(var16) + 5, var4, GuiUtil.getW(var17, var17.getText()), GuiUtil.getCommonItemHeight() + 4);
      this.enablePSMode = GuiUtil.getANYKCheckBox("PostScript optimalizált nyomtatási mód engedélyezése ", false);
      GuiUtil.setDynamicBound(this.enablePSMode, this.enablePSMode.getText(), 10, 2 * var4);
      int var18 = GuiUtil.getPositionFromPrevComponent(var17) + 50;
      int var19 = GuiUtil.getCommonItemHeight() + 2;
      var5 = GuiUtil.getCommonItemHeight() + 8;
      var6.setBounds(10, var5, var18, 3 * var19 + var4);
      var5 = (int)((double)var5 + var6.getBounds().getHeight() + 8.0D);
      var7.setBounds(10, var5, var18, 2 * var19 + var4);
      var5 = (int)((double)var5 + var7.getBounds().getHeight() + 8.0D);
      var8.setBounds(10, var5, var18, 4 * var19 + var4);
      var5 = (int)((double)var5 + var8.getBounds().getHeight() + 8.0D);
      var9.setBorder(var14);
      var9.setLayout((LayoutManager)null);
      var9.setBounds(10, var5, var18, 3 * var19 + var4);
      int var20 = GuiUtil.getPositionFromPrevComponent(var17) + 100;
      var9.add(this.pdfDirCimke);
      var9.add(this.pdfDirField);
      var9.add(var16);
      var9.add(var17);
      var9.add(this.enablePSMode);
      var2.add(var6);
      var2.add(var7);
      var2.add(var8);
      var2.add(var9);
   }

   private void prepare() {
      AncestorListener var1 = new AncestorListener() {
         public void ancestorAdded(AncestorEvent var1) {
         }

         public void ancestorMoved(AncestorEvent var1) {
         }

         public void ancestorRemoved(AncestorEvent var1) {
            try {
               PrinterSettings.this.save();
            } catch (Exception var3) {
               var3.printStackTrace();
            }

         }
      };
      this.addAncestorListener(var1);
      this.load();
   }

   public void save() {
      SettingsStore var1 = SettingsStore.getInstance();
      var1.set("printer", "print.settings.margin", this.nyomtatoMargo.getValue().toString());
      int var2 = 0;
      if (this.commonId.isSelected()) {
         ++var2;
      }

      if (this.importId.isSelected()) {
         var2 += 2;
      }

      if (this.veszpremiId.isSelected()) {
         var2 += 3;
      }

      var1.set("printer", "print.settings.kozos", var2 + "");
      var1.set("printer", "print.settings.colors", this.szi.isSelected() ? "1" : "12");
      if (this.pdfDirField.getText().equals("")) {
         this.pdfDirField.setText((String)PropertyList.getInstance().get("prop.usr.naplo"));
         this.pdfDirField.setToolTipText(this.pdfDirField.getText());
      }

      var1.set("printer", "print.settings.pdfdir", this.pdfDirField.getText());
      var1.set("printer", "print.settings.enableps", this.enablePSMode.isSelected() ? "i" : "n");
      var1.save();
   }

   public void load() {
      SettingsStore var1 = SettingsStore.getInstance();

      int var2;
      try {
         var2 = Integer.parseInt(var1.get("printer", "print.settings.margin"));
      } catch (Exception var10) {
         var2 = 2;
      }

      this.nyomtatoMargo.setValue(new Integer(var2));
      int var4 = 0;

      try {
         var4 = Integer.parseInt(var1.get("printer", "print.settings.kozos"));
      } catch (Exception var9) {
         Tools.eLog(var9, 0);
      }

      this.commonId.setSelected(var4 == 1);
      this.importId.setSelected(var4 == 2);
      this.veszpremiId.setSelected(var4 == 3);

      int var3;
      try {
         var3 = Integer.parseInt(var1.get("printer", "print.settings.colors"));
      } catch (Exception var8) {
         var3 = 12;
      }

      switch(var3) {
      case 1:
         this.szi.setSelected(true);
         break;
      default:
         this.ff.setSelected(true);
      }

      try {
         this.pdfDirField.setText(var1.get("printer", "print.settings.pdfdir"));
         this.pdfDirField.setToolTipText(this.pdfDirField.getText());
      } catch (Exception var7) {
         Tools.eLog(var7, 0);
      }

      if (this.pdfDirField.getText().equals("")) {
         this.pdfDirField.setText((String)PropertyList.getInstance().get("prop.usr.naplo"));
         this.pdfDirField.setToolTipText(this.pdfDirField.getText());
      }

      try {
         this.enablePSMode.setSelected(var1.get("printer", "print.settings.enableps").equalsIgnoreCase("i"));
      } catch (Exception var6) {
         this.enablePSMode.setSelected(false);
      }

   }

   public void stateChanged(ChangeEvent var1) {
      if (var1.getSource() instanceof JCheckBox) {
         if (((JCheckBox)var1.getSource()).getName().equals("cId") && this.commonId.isSelected()) {
            this.importId.setSelected(false);
            this.veszpremiId.setSelected(false);
         }

         if (((JCheckBox)var1.getSource()).getName().equals("iId") && this.importId.isSelected()) {
            this.commonId.setSelected(false);
            this.veszpremiId.setSelected(false);
         }

         if (((JCheckBox)var1.getSource()).getName().equals("vId") && this.veszpremiId.isSelected()) {
            this.commonId.setSelected(false);
            this.importId.setSelected(false);
         }
      }

   }

   private void initDialog(String var1, String var2) {
      this.fc.setDialogTitle(var1);
      File var3 = new NecroFile(var2);
      if (var3.exists()) {
         if (var3.isDirectory()) {
            this.fc.setCurrentDirectory(var3);
         } else {
            this.fc.setCurrentDirectory(new NecroFile(var3.getParent()));
         }
      }

      try {
         ((BasicFileChooserUI)this.fc.getUI()).setFileName("");
      } catch (ClassCastException var7) {
         try {
            this.fc.setSelectedFile(new NecroFile(""));
         } catch (Exception var6) {
            Tools.eLog(var6, 0);
         }
      }

      this.fc.setSelectedFile((File)null);
   }

   private int getPanelWidth() {
      return 700;
   }
}
