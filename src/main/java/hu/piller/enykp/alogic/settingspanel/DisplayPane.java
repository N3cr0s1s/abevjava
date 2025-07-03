package hu.piller.enykp.alogic.settingspanel;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.icon.ENYKIconSet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

public class DisplayPane extends JPanel {
   protected JCheckBox wrap;
   protected JComboBox place;
   protected JCheckBox detail;
   protected JRadioButton rbUseDefaultAzon;
   protected JTextField defaultCegkapuAzon;
   protected JRadioButton rbUseFormDataAzon;
   protected JComboBox lafCombo;
   protected JSpinner fontSizeField;
   Vector<String> lafNames = new Vector();
   Vector<String> lafClassNames = new Vector();
   SettingsStore ss;

   public DisplayPane() {
      Dimension var1 = GuiUtil.getSettingsDialogDimension();
      this.setPreferredSize(var1);
      this.setSize(var1);
      JPanel var2 = new JPanel((LayoutManager)null);
      var2.setSize(new Dimension(GuiUtil.getW("Meghatalmazott/Könyvelőiroda Cég/Hivatali kapu azonosító :") + 20, (int)var1.getHeight()));
      var2.setPreferredSize(new Dimension(GuiUtil.getW("Meghatalmazott/Könyvelőiroda Cég/Hivatali kapu azonosító :") + 20, (int)var1.getHeight()));
      JScrollPane var3 = new JScrollPane(var2, 20, 30);
      this.setLayout(new BorderLayout());
      this.add(var3, "Center");
      this.ss = SettingsStore.getInstance();
      int var4 = GuiUtil.getCommonItemHeight() + 8;
      JLabel var6 = new JLabel("A lapfülek elhelyezkedése:");
      this.wrap = GuiUtil.getANYKCheckBox("A lapfülek több sorban");
      this.detail = GuiUtil.getANYKCheckBox("Mezőinformáció kikapcsolása a hibaüzenetben");
      Vector var7 = new Vector();
      var7.add("Felül");
      var7.add("Alul");
      var7.add("Jobb");
      var7.add("Bal");
      this.place = new JComboBox(var7);
      GuiUtil.setDynamicBound(var6, var6.getText(), 10, var4);
      this.place.setBounds(GuiUtil.getPositionFromPrevComponent(var6), var4, GuiUtil.getW(var6, "WWfelülWW"), GuiUtil.getCommonItemHeight() + 2);
      int var5 = var4 + var4;
      GuiUtil.setDynamicBound(this.wrap, this.wrap.getText(), 10, var5);
      var5 += var4;
      GuiUtil.setDynamicBound(this.detail, this.detail.getText(), 10, var5);
      var5 += var4;
      this.rbUseDefaultAzon = GuiUtil.getANYKRadioButton("Meghatalmazott/Könyvelőiroda Cég/Hivatali kapu azonosító :");
      this.rbUseFormDataAzon = GuiUtil.getANYKRadioButton("Nyomtatványból történő Cégkapu azonosítás");
      this.defaultCegkapuAzon = new JTextField();
      LookAndFeelInfo[] var8 = UIManager.getInstalledLookAndFeels();
      LookAndFeelInfo[] var9 = var8;
      int var10 = var8.length;

      for(int var11 = 0; var11 < var10; ++var11) {
         LookAndFeelInfo var12 = var9[var11];
         if (var12.getName().toLowerCase().contains("windows classic")) {
            this.lafNames.add("Windows Classic");
            this.lafClassNames.add(var12.getClassName());
         } else {
            if (var12.getName().toLowerCase().contains("windows")) {
               this.lafNames.add("Windows");
               this.lafClassNames.add(var12.getClassName());
            }

            if (var12.getName().toLowerCase().contains("mac")) {
               this.lafNames.add("Mac");
               this.lafClassNames.add(var12.getClassName());
            }

            if (var12.getName().toLowerCase().contains("metal")) {
               this.lafNames.add("Java alapértelmezett");
               this.lafClassNames.add(var12.getClassName());
            }
         }
      }

      int var30 = this.lafNames.indexOf("Windows");
      if (var30 > -1 && this.lafNames.contains("Windows Classic")) {
         this.lafNames.remove(var30);
         this.lafClassNames.remove(var30);
      }

      this.lafCombo = new JComboBox(this.lafNames);
      this.lafCombo.setToolTipText("Alkalmazásához kattintson az 'Alkalmaz' gombra!");
      String var31 = this.ss.get("gui", "LookAndFeelClassName");
      String var32 = this.ss.get("gui", "anykFontSize");

      try {
         if (var31 != null && var31.contains(var31)) {
            this.lafCombo.setSelectedIndex(this.lafClassNames.indexOf(var31));
         }
      } catch (Exception var29) {
         this.lafCombo.setSelectedIndex(0);
      }

      byte var13 = 56;
      if (GuiUtil.getScreenW() < 2000) {
         var13 = 32;
      }

      SpinnerNumberModel var33;
      if (var32 != null) {
         int var14 = Integer.parseInt(var32);
         if (var14 > var13) {
            var14 = var13;
         }

         var33 = new SpinnerNumberModel(var14, 12, var13, 1);
      } else {
         var33 = new SpinnerNumberModel(12, 12, var13, 1);
      }

      this.fontSizeField = new JSpinner();
      this.fontSizeField.setToolTipText("Alkalmazásához kattintson az 'Alkalmaz' gombra!");
      this.fontSizeField.setModel(var33);
      String var34 = this.ss.get("gui", "tablayout");
      if (var34 != null && var34.equals("true")) {
         this.wrap.setSelected(true);
      }

      var34 = this.ss.get("gui", "detail");
      if (var34 == null || var34 != null && var34.equals("true")) {
         this.detail.setSelected(true);
      }

      var34 = this.ss.get("gui", "tabpos");
      if (var34 != null) {
         boolean var15 = false;

         int var35;
         try {
            var35 = Integer.parseInt(var34);
         } catch (NumberFormatException var28) {
            var35 = 0;
         }

         this.place.setSelectedIndex(var35);
      }

      var34 = this.ss.get("gui", "usableCKAzon");
      if (var34 == null) {
         var34 = "1";
      }

      if (!"0".equals(var34) && !"1".equals(var34)) {
         var34 = "1";
      }

      this.rbUseDefaultAzon.setSelected("0".equals(var34));
      this.rbUseFormDataAzon.setSelected("1".equals(var34));
      var34 = this.ss.get("gui", "defaultCKAzon");
      if (var34 != null) {
         this.defaultCegkapuAzon.setText(var34);
      }

      this.wrap.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent var1) {
            String var2 = DisplayPane.this.wrap.isSelected() ? "true" : "false";
            DisplayPane.this.ss.set("gui", "tablayout", var2);
         }
      });
      this.detail.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent var1) {
            String var2 = DisplayPane.this.detail.isSelected() ? "true" : "false";
            DisplayPane.this.ss.set("gui", "detail", var2);
         }
      });
      this.rbUseDefaultAzon.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent var1) {
            DisplayPane.this.ss.set("gui", "usableCKAzon", DisplayPane.this.rbUseDefaultAzon.isSelected() ? "0" : "1");
         }
      });
      this.rbUseFormDataAzon.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent var1) {
            DisplayPane.this.ss.set("gui", "usableCKAzon", DisplayPane.this.rbUseDefaultAzon.isSelected() ? "0" : "1");
         }
      });
      this.place.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent var1) {
            int var2 = DisplayPane.this.place.getSelectedIndex();
            String var3 = "" + var2;
            DisplayPane.this.ss.set("gui", "tabpos", var3);
         }
      });
      this.defaultCegkapuAzon.addFocusListener(new FocusAdapter() {
         public void focusLost(FocusEvent var1) {
            DisplayPane.this.ss.set("gui", "defaultCKAzon", DisplayPane.this.defaultCegkapuAzon.getText());
         }
      });
      var5 += var4;
      JLabel var36 = new JLabel("Alapértelmezettként használandó Cég/Hivatali kapu azonosító");
      GuiUtil.setDynamicBound(var36, var36.getText(), 10, var5);
      int var18 = 24;
      int var19 = 24;

      Object var16;
      try {
         var16 = ENYKIconSet.getInstance().get("anyk_sugo");
      } catch (Exception var27) {
         try {
            var16 = UIManager.getIcon("OptionPane.questionIcon");
         } catch (Exception var26) {
            var16 = null;
         }
      }

      JButton var17;
      if (var16 != null) {
         var18 = ((Icon)var16).getIconWidth() + 2;
         var19 = ((Icon)var16).getIconHeight() + 2;
         var17 = new JButton((Icon)var16);
      } else {
         var17 = new JButton(".?.");
      }

      var17.setToolTipText("Segítség a Cég/Hivatali kapu azonosítóhoz...");
      var17.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            GuiUtil.showMessageDialog(DisplayPane.this, "Az itt kiválasztott opció szerint kerül beállításra az a Cég vagy Hivatali kapu,\namelyet a 'Nyomtatványok csoportos közvetlen beküldése a Cég/Hivatali kapun keresztül'\nfunkció választásakor használ, a nyomtatványok beküldésére.", "Cég/Hivatali kapu azonosító", 1);
         }
      });
      var17.setBounds(GuiUtil.getPositionFromPrevComponent(var36), var5, var18, var19);
      var5 += var4;
      var5 += 8;
      GuiUtil.setDynamicBound(this.rbUseDefaultAzon, this.rbUseDefaultAzon.getText(), 10, var5);
      this.defaultCegkapuAzon.setBounds(GuiUtil.getPositionFromPrevComponent(this.rbUseDefaultAzon), var5, GuiUtil.getW(var36, "WWWWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 8);
      var5 += var4;
      GuiUtil.setDynamicBound(this.rbUseFormDataAzon, this.rbUseFormDataAzon.getText(), 10, var5);
      var5 += var4;
      var5 += var4;
      JLabel var20 = new JLabel("Kinézet");
      GuiUtil.setDynamicBound(var20, var20.getText(), 10, var5);
      this.lafCombo.setBounds(GuiUtil.getPositionFromPrevComponent(var20) + 5, var5, GuiUtil.getW(var36, "WWWWINDOWSWWW"), GuiUtil.getCommonItemHeight() + 4);
      var5 += var4;
      JLabel var21 = new JLabel("Betűméret (alapértelmezett: 12)");
      GuiUtil.setDynamicBound(var21, var21.getText(), 10, var5);
      this.fontSizeField.setBounds(GuiUtil.getPositionFromPrevComponent(var21) + 5, var5, GuiUtil.getW(var36, "WWW"), GuiUtil.getCommonItemHeight() + 4);
      JButton var22 = new JButton("Előnézet");
      var22.setBounds(GuiUtil.getPositionFromPrevComponent(this.fontSizeField) + 20, var5, GuiUtil.getW(var22, var22.getText()) + 8, GuiUtil.getCommonItemHeight() + 4);
      var2.add(var22);
      var22.addActionListener((var1x) -> {
         try {
            this.showDemoDialog((String)this.lafClassNames.get(this.lafCombo.getSelectedIndex()), this.fontSizeField.getValue().toString());
         } catch (Exception exc) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A beállításoknak megfelelő előnézet nem jeleníthető meg.", "Hiba", 0);
            exc.printStackTrace();
         }

      });
      JButton var23 = new JButton("Alkalmaz");
      var23.setBounds(GuiUtil.getPositionFromPrevComponent(var22) + 20, var5, GuiUtil.getW(var23, var23.getText()) + 8, GuiUtil.getCommonItemHeight() + 4);
      var23.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            try {
               if ((Integer)DisplayPane.this.fontSizeField.getValue() > (Integer)((SpinnerNumberModel)DisplayPane.this.fontSizeField.getModel()).getMaximum()) {
                  return;
               }

               DisplayPane.this.saveSettings();
               SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                     GuiUtil.showMessageDialog(SettingsDialog.thisinstance, "A beállítás életbeléptetéséhez indítsa újra az ÁNYK-t", "Figyelmeztetés", 2);
                  }
               });
            } catch (Exception var3) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "A beállításoknak megfelelő nézet mentése nem sikerült.", "Hiba", 0);
               var3.printStackTrace();
            }

         }
      });
      int var10000 = var5 + var4;
      String var24 = this.ss.get("gui", "anykHighDPI");
      if (var24 == null) {
         var24 = Toolkit.getDefaultToolkit().getScreenResolution() > 120 ? "true" : "false";
      }

      var2.add(var23);
      var2.add(var20);
      var2.add(var21);
      var2.add(this.lafCombo);
      var2.add(this.fontSizeField);
      var2.add(this.wrap);
      var2.add(var6);
      var2.add(this.place);
      var2.add(this.detail);
      var2.add(var36);
      ButtonGroup var25 = new ButtonGroup();
      var25.add(this.rbUseDefaultAzon);
      var25.add(this.rbUseFormDataAzon);
      var2.add(this.rbUseDefaultAzon);
      var2.add(this.rbUseFormDataAzon);
      var2.add(this.defaultCegkapuAzon);
      var2.add(var17);
   }

   private void showDemoDialog(String var1, String var2) {
      int var3 = 12;

      try {
         try {
            var3 = Integer.parseInt(var2);
         } catch (NumberFormatException var21) {
            var3 = 12;
         }

         if (var1 != null) {
            UIManager.setLookAndFeel(var1);
         }
      } catch (Exception var22) {
         System.out.println("LookAndFeel beállítás sikertelen: " + var22.toString());
      }

      final JDialog var4 = new JDialog(MainFrame.thisinstance, "Néhány képernyőelem...", true);
      var4.setDefaultCloseOperation(2);
      Font var5 = new Font(var4.getFont().getFontName(), 0, var3);
      JPanel var6 = new JPanel();
      var6.setBorder(new EmptyBorder(10, 10, 10, 10));
      var6.setLayout(new BoxLayout(var6, 3));
      JLabel var7 = new JLabel("Adja meg a mező értékét:");
      var7.setFont(var5);
      var7.setBounds(0, 0, this.getW(var7, var7.getText()), var7.getFontMetrics(var5).getHeight());
      var6.add(var7);
      JTextField var8 = new JTextField();
      var8.setText("alapérték");
      var8.setFont(var5);
      var8.setBounds(this.getW(var7, var7.getText()) + 10, 0, GuiUtil.getW(var7, var7.getText()), 2 + var7.getFontMetrics(var5).getHeight());
      var6.add(var8);
      JCheckBox var9 = GuiUtil.getANYKCheckBox("Válassza ki a jelölőnégyzetet!");
      var9.setFont(var5);
      var6.add(var9);
      Vector var10 = new Vector();
      var10.add("Első sor 1. oszlop");
      var10.add("Első sor 2. oszlop");
      var10.add("Első sor 3. oszlop");
      Vector var11 = new Vector();
      var11.add("Második sor 1. oszlop");
      var11.add("Második sor 2. oszlop");
      var11.add("Második sor 3. oszlop");
      Vector var12 = new Vector();
      var12.add("Harmadik sor 1. oszlop");
      var12.add("Harmadik sor 2. oszlop");
      var12.add("Harmadik sor 3. oszlop");
      JPanel var13 = new JPanel((LayoutManager)null);
      var13.setSize(new Dimension(this.getW(var8, "WWHarmadik sor 3. oszlopWW"), 5 * this.getFontMetrics(var5).getHeight()));
      var13.setMinimumSize(var13.getSize());
      var13.setPreferredSize(var13.getSize());
      JComboBox var14 = new JComboBox(var10);
      var14.setFont(var5);
      var13.add(var14);
      var14.setBounds(0, 0, this.getW(var8, "WHarmadik sor 3. oszlopW"), this.getFontMetrics(var5).getHeight() + 2);
      var6.add(var13);
      Vector var15 = new Vector();
      var15.add(var10);
      var15.add(var11);
      var15.add(var12);
      Vector var16 = new Vector();
      var16.add("OszlopFejléc 1");
      var16.add("OszlopFejléc 2");
      var16.add("OszlopFejléc 3");
      DefaultTableModel var17 = new DefaultTableModel(var15, var16);
      JTable var18 = new JTable(var17);
      var18.setDefaultEditor(Object.class, (TableCellEditor)null);
      var18.setFont(var5);
      var18.getTableHeader().setFont(var5);
      var18.getTableHeader().setResizingAllowed(false);

      for(int var19 = 0; var19 < 3; ++var19) {
         var18.getColumnModel().getColumn(var19).setMinWidth(this.getW(var8, "Harmadik sor 1. oszlop"));
         var18.getColumnModel().getColumn(var19).setPreferredWidth(this.getW(var8, "Harmadik sor 1. oszlop"));
         var18.getColumnModel().getColumn(var19).setWidth(this.getW(var8, "Harmadik sor 1. oszlop"));
      }

      var18.setRowHeight(this.getFontMetrics(var5).getHeight() + 2);
      var18.setMinimumSize(new Dimension(3 * this.getW(var8, "Harmadik sor 1. oszlop"), 300));
      var18.setCellSelectionEnabled(false);
      var18.setAutoResizeMode(4);
      JScrollPane var23 = new JScrollPane(var18, 22, 32);
      var6.add(var23);
      var4.add(var6, "Center");
      JButton var20 = new JButton("Bezár");
      var20.setFont(var5);
      var20.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            var4.dispose();
         }
      });
      var6.add(var20);
      var4.setLocationRelativeTo(MainFrame.thisinstance);
      var4.setSize(3 * GuiUtil.getW("Harmadik sor 1. oszlop"), 600);
      var4.setVisible(true);
   }

   private void saveSettings() throws Exception {
      SettingsStore var1 = SettingsStore.getInstance();
      int var2 = (int)(0.6D * (double)GuiUtil.getScreenW());
      int var3 = (int)(0.8D * (double)GuiUtil.getScreenH());
      int var4 = (int)(0.9D * (double)GuiUtil.getScreenW());
      int var5 = (int)(0.9D * (double)GuiUtil.getScreenH());

      int var6;
      try {
         var6 = Integer.parseInt(var1.get("filepanel_new_settings", "width"));
      } catch (Exception var24) {
         var6 = var2;
      }

      int var7;
      try {
         var7 = Integer.parseInt(var1.get("filepanel_new_settings", "height"));
      } catch (Exception var23) {
         var7 = var3;
      }

      int var8;
      try {
         var8 = Integer.parseInt(var1.get("filepanel_open_settings", "width"));
      } catch (Exception var22) {
         var8 = var2;
      }

      int var9;
      try {
         var9 = Integer.parseInt(var1.get("filepanel_open_settings", "height"));
      } catch (Exception var21) {
         var9 = var3;
      }

      int var10;
      try {
         var10 = Integer.parseInt(var1.get("gui", "w"));
      } catch (Exception var20) {
         var10 = Math.max(800, var2);
      }

      int var11;
      try {
         var11 = Integer.parseInt(var1.get("gui", "h"));
      } catch (Exception var19) {
         var11 = Math.max(600, var3);
      }

      int var12;
      try {
         var12 = Integer.parseInt(var1.get("gui", "anykFontSize"));
      } catch (Exception var18) {
         var12 = 12;
      }

      int var13 = Integer.parseInt(this.fontSizeField.getValue().toString());
      double var14 = (double)var13 / (double)var12;
      if (var14 > 1.0D || var14 < 0.9999D) {
         if (var14 > 1.0D) {
            var6 = Math.min((int)((double)var6 * var14), var4);
            var7 = Math.min((int)((double)var7 * var14), var5);
            var8 = Math.min((int)((double)var8 * var14), var4);
            var9 = Math.min((int)((double)var9 * var14), var5);
            var10 = Math.min((int)((double)var10 * var14), var4);
            var11 = Math.min((int)((double)var11 * var14), var5);
         } else {
            Font var16 = this.wrap.getFont().deriveFont(0, (float)var13);
            int var17 = this.wrap.getFontMetrics(var16).getHeight();
            var6 = (int)(0.4D * (double)GuiUtil.getScreenW());
            var7 = 25 * (var17 + 4);
            var8 = var6;
            var9 = 25 * (var17 + 4);
            var10 = var6;
            var11 = var7;
         }

         var1.set("filepanel_open_settings", "width", var8 + "");
         var1.set("filepanel_open_settings", "height", var9 + "");
         var1.set("filepanel_new_settings", "width", var6 + "");
         var1.set("filepanel_new_settings", "height", var7 + "");
         var1.set("gui", "w", var10 + "");
         var1.set("gui", "h", var11 + "");
      }

      UIManager.setLookAndFeel((String)this.lafClassNames.get(this.lafCombo.getSelectedIndex()));
      var1.set("gui", "LookAndFeelClassName", (String)this.lafClassNames.get(this.lafCombo.getSelectedIndex()));
      var1.set("gui", "anykFontSize", this.fontSizeField.getValue().toString());
   }

   private int getW(JComponent var1, String var2) {
      return SwingUtilities.computeStringWidth(var1.getFontMetrics(var1.getFont()), var2) + 20;
   }
}
