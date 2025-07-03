package hu.piller.enykp.alogic.masterdata.gui.entityfilter;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.component.filtercombo.ENYKFilterComboPanel;
import hu.piller.enykp.gui.framework.MainFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class TypeSelectorPanel extends JDialog implements ActionListener {
   private String typeSelected;
   private JPanel selectorPanel;
   private JPanel buttonPanel;
   private ENYKFilterComboPanel cmbType;

   public TypeSelectorPanel(String[] var1) {
      super(MainFrame.thisinstance);
      this.init(var1);
      this.typeSelected = "NONE";
   }

   public String getTypeSelected() {
      return this.typeSelected;
   }

   public void setSelected(String var1) {
      this.cmbType.setText(var1);
   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = var1.getActionCommand();
      if ("valaszt".equals(var2)) {
         if (!"".equals(this.cmbType.getText())) {
            this.typeSelected = this.cmbType.getText();
         }

         this.dispose();
      } else if ("bezar".equals(var2)) {
         this.dispose();
      }

   }

   private void init(String[] var1) {
      JPanel var2 = new JPanel();
      var2.setPreferredSize(new Dimension(GuiUtil.getW("WWWWWWWWWWWWWWWWWWWWWWWWW"), 4 * (GuiUtil.getCommonItemHeight() + 4)));
      var2.setLayout(new BorderLayout(10, 10));
      var2.add(this.getSelectorPanel(var1), "Center");
      var2.add(this.getButtonPanel(), "South");
      this.add(var2);
      this.setTitle("Típus választás");
      this.setResizable(false);
      this.setModal(true);
      this.setSize(new Dimension(GuiUtil.getW("WWWWWWWWWWWWWWWWWWWWWWWWW") + 30, 4 * (GuiUtil.getCommonItemHeight() + 4) + 60));
      this.setPreferredSize(this.getSize());
      if (this.getOwner() != null) {
         this.setLocationRelativeTo(this.getOwner());
      }

   }

   private JPanel getButtonPanel() {
      if (this.buttonPanel == null) {
         this.buttonPanel = new JPanel();
         this.buttonPanel.setPreferredSize(new Dimension(GuiUtil.getW("WWWWWWWWWWWWWWWWWWWWWWWWW"), 2 * (GuiUtil.getCommonItemHeight() + 4)));
         this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel, 0));
         this.buttonPanel.add(Box.createHorizontalGlue());
         this.buttonPanel.add(this.createButton("valaszt", "Kiválaszt", this, 85, 25));
         this.buttonPanel.add(Box.createHorizontalStrut(5));
         this.buttonPanel.add(this.createButton("bezar", "Bezár", this, 85, 25));
         this.buttonPanel.add(Box.createHorizontalGlue());
      }

      return this.buttonPanel;
   }

   private JPanel getSelectorPanel(String[] var1) {
      if (this.selectorPanel == null) {
         this.selectorPanel = new JPanel(new BorderLayout());
         this.selectorPanel.setPreferredSize(new Dimension(GuiUtil.getW("WWWWWWWWWWWWWWWWWWWWWWWWW") + 20, 2 * (GuiUtil.getCommonItemHeight() + 4)));
         this.selectorPanel.setBorder(BorderFactory.createTitledBorder("Típus választás"));
         this.selectorPanel.add(this.getTypeCombo(var1), "North");
      }

      return this.selectorPanel;
   }

   private ENYKFilterComboPanel getTypeCombo(String[] var1) {
      if (this.cmbType == null) {
         this.cmbType = new ENYKFilterComboPanel(GuiUtil.getW("WWWWWWWWWWWWWWWWWWWWWWWWW"));
      }

      this.cmbType.addElements(var1, false);
      return this.cmbType;
   }

   private JButton createButton(String var1, String var2, ActionListener var3, int var4, int var5) {
      JButton var6 = new JButton();
      var6.setActionCommand(var1);
      var6.setName(var2);
      var6.setText(var2);
      var6.addActionListener(var3);
      var4 = GuiUtil.getW(var6, var6.getText());
      var5 = GuiUtil.getCommonItemHeight() + 4;
      var6.setMinimumSize(new Dimension(var4, var5));
      var6.setPreferredSize(var6.getMinimumSize());
      var6.setMaximumSize(var6.getMinimumSize());
      return var6;
   }
}
