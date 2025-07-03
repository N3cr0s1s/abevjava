package hu.piller.enykp.gui.component.filtercombo;

import hu.piller.enykp.gui.GuiUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ENYKFilterComboPanel extends JPanel {
   ENYKFilterComboStandard combo;
   JButton btn;

   public ENYKFilterComboPanel() {
      this.build();
      this.prepare();
   }

   public ENYKFilterComboPanel(int var1) {
      this.build(var1);
      this.prepare();
   }

   public ENYKFilterComboPanel(String var1) {
      this.build(var1);
      this.prepare();
   }

   public void enableFreeText() {
      this.combo.setFreeTextEnabled(true);
   }

   public void disableFreeText() {
      this.combo.setFreeTextEnabled(false);
   }

   public ENYKFilterComboPanel(Object var1) {
      this.build(var1, false);
      this.prepare();
   }

   public ENYKFilterComboPanel(Object var1, boolean var2) {
      this.build(var1, var2);
      this.prepare();
   }

   private void build(Object var1, boolean var2) {
      this.setLayout(new BorderLayout());
      this.add(this.getCombo(var1, var2), "Center");
      this.add(this.getBtn(), "South");
   }

   private void build() {
      this.setLayout(new BorderLayout());
      this.add(this.getCombo(), "Center");
      this.add(this.getBtn(), "East");
   }

   private void build(int var1) {
      this.setLayout(new BorderLayout());
      this.add(this.getCombo(var1), "Center");
      this.add(this.getBtn(), "East");
   }

   private void build(String var1) {
      this.setLayout(new BorderLayout());
      this.add(this.getCombo(), "Center");
      this.add(this.getBtn(), var1);
   }

   private void prepare() {
      this.btn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ENYKFilterComboPanel.this.combo.mouseClicked(new MouseEvent(ENYKFilterComboPanel.this.combo, 0, 0L, 0, 0, 0, 2, false));
         }
      });
   }

   public ENYKFilterComboStandard getCombo() {
      if (this.combo == null) {
         this.combo = new ENYKFilterComboStandard();
         this.combo.setToolTipText("Kattintson a mezőbe, és kezdje el gépelni a keresett szöveget");
      }

      return this.combo;
   }

   public ENYKFilterComboStandard getCombo(int var1) {
      if (this.combo == null) {
         this.combo = new ENYKFilterComboStandard(var1);
         this.combo.setToolTipText("Kattintson a mezőbe, és kezdje el gépelni a keresett szöveget");
      }

      return this.combo;
   }

   public ENYKFilterComboStandard getCombo(Object var1, boolean var2) {
      if (this.combo == null) {
         this.combo = new ENYKFilterComboStandard(var1, var2);
         this.combo.setToolTipText("Kattintson a mezőbe, és kezdje el gépelni a keresett szöveget");
      }

      return this.combo;
   }

   public String getText() {
      return this.combo.getText();
   }

   public void setText(String var1) {
      this.combo.setText(var1);
   }

   public boolean addElements(Object var1, boolean var2) {
      return this.combo.addElements(var1, var2);
   }

   public void setFeature(Object var1) {
      this.combo.setFeature("combo_data", var1);
   }

   public JButton getBtn() {
      if (this.btn == null) {
         this.btn = new SizeableArrowButton(5, GuiUtil.getCommonFontSize());
      }

      return this.btn;
   }

   public void setBackground(Color var1) {
      if (this.combo != null) {
         this.combo.setBackground(var1);
      }

   }

   public void setToolTipText(String var1) {
      if (this.combo != null) {
         this.combo.setToolTipText(var1);
      }

   }
}
