package hu.piller.enykp.alogic.masterdata.gui;

import hu.piller.enykp.alogic.masterdata.core.BlockDefinition;
import hu.piller.enykp.alogic.masterdata.core.BlockLayoutDef;
import hu.piller.enykp.alogic.masterdata.core.EntityError;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryException;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryMetaFactory;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.component.filtercombo.ENYKFilterComboPanel;
import hu.piller.enykp.util.base.PropertyList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

public class MDBlockComponent implements ActionListener {
   private static final Color ERROR_MARKER_COLOR = new Color(190, 131, 127);
   private static final Color DIFF_MARKER_COLOR;
   private Color markerColor;
   static final int NONE = -1;
   private boolean bordered;
   private BlockDefinition blockDefinition;
   private JPanel pnl_blockContent;
   private JPanel pnl_repeater;
   private JPanel pnl_content;
   private JLabel lbl_blockCount;
   protected int current_block;
   protected MDBlockComponentBean content;
   protected Hashtable<String, JComponent> fields;
   private Properties mdNames;
   private Hashtable<String, EntityError> errors;
   String[] shortStringsEndsWith = new String[]{"Irányítószám", "Ajtó", "Emelet", "Lépcsőház", "Épület", "Házszám"};
   HashSet<String> shortStringsEndsWithSet;
   Dimension dim;
   Dimension shortDim;
   String maskedS;

   public MDBlockComponent(BlockDefinition var1, boolean var2) {
      this.shortStringsEndsWithSet = new HashSet(Arrays.asList(this.shortStringsEndsWith));
      this.dim = new Dimension(GuiUtil.getW("WWWWWWWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 2);
      this.shortDim = new Dimension(GuiUtil.getW("WWWW"), GuiUtil.getCommonItemHeight() + 2);
      this.maskedS = "WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW";
      this.blockDefinition = var1;
      this.bordered = var2;
      this.pnl_blockContent = null;
      this.content = new MDBlockComponentBean(var1.getBlockName(), var1.getMasterDataNames());
      this.current_block = -1;
      this.lbl_blockCount = new JLabel();
      this.fields = new Hashtable();
      if (this.isMandatoryBlock()) {
         this.content.addEmptyDataRecord();
         this.current_block = 1;
      }

      this.errors = new Hashtable();
      this.setMarkerToError();
   }

   public void cleanErrors() {
      this.errors.clear();
   }

   public void setMarkerToError() {
      this.markerColor = ERROR_MARKER_COLOR;
   }

   public void setMarkerToDiff() {
      this.markerColor = DIFF_MARKER_COLOR;
   }

   public void addError(EntityError var1) {
      String var2 = var1.getBlock_seq() + "#" + var1.getMDKey();
      this.errors.put(var2, var1);
   }

   public JPanel getLayout() {
      if (this.pnl_blockContent == null) {
         this.pnl_blockContent = new JPanel();
         if (this.bordered) {
            this.pnl_blockContent.setBorder(BorderFactory.createTitledBorder(this.blockDefinition.getBlockName()));
         }

         this.pnl_blockContent.setLayout(new BoxLayout(this.pnl_blockContent, 1));
         if (this.blockDefinition.getMax() > 1) {
            this.pnl_blockContent.add(this.getLayoutForRepeater());
            this.pnl_blockContent.add(Box.createVerticalStrut(5));
         }

         this.pnl_blockContent.add(this.getLayoutForContent());
      }

      this.refreshGUIState();
      return this.pnl_blockContent;
   }

   public void storeDataInFields() {
      String[] var1 = this.blockDefinition.getMasterDataNames();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         if (this.fields.containsKey(var4)) {
            this.content.setMDValue(this.current_block, var4, this.getValueOfComponent((JComponent)this.fields.get(var4)));
         }
      }

   }

   public void actionPerformed(ActionEvent var1) {
      if ("add".equals(var1.getActionCommand())) {
         if (this.content.getSize() < this.blockDefinition.getMax()) {
            if (this.current_block != -1) {
               this.storeDataInFields();
            }

            this.content.addEmptyDataRecord();
            this.current_block = this.content.getSize();
         }
      } else if ("remove".equals(var1.getActionCommand())) {
         if (this.content.getSize() > 0 && this.content.getSize() - 1 >= this.blockDefinition.getMin()) {
            this.content.removeDataRecord(this.current_block);
            if (this.current_block > this.content.getSize()) {
               --this.current_block;
            }

            if (this.current_block == 0) {
               this.current_block = -1;
            }
         }
      } else if ("prev".equals(var1.getActionCommand())) {
         if (this.current_block > 1) {
            this.storeDataInFields();
            --this.current_block;
         }
      } else if ("next".equals(var1.getActionCommand()) && this.content.getSize() > 0 && this.current_block < this.content.getSize()) {
         this.storeDataInFields();
         ++this.current_block;
      }

      this.refreshGUIState();
   }

   public void refreshGUIState() {
      this.enableDisableInputFields();
      if (this.content.getSize() != 0) {
         this.showCurrentContent();
      }

      this.refreshCounter();
   }

   public void showCurrentContent() {
      String[] var1 = this.blockDefinition.getMasterDataNames();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         if (this.fields.containsKey(var4)) {
            this.setValueOfComponent((JComponent)this.fields.get(var4), this.content.getMDValue(this.current_block, var4));
            this.setComponentSize(var4);
            String var5 = this.current_block + "#" + var4;
            if (this.errors.containsKey(var5)) {
               EntityError var6 = (EntityError)this.errors.get(var5);
               ((JComponent)this.fields.get(var4)).setBackground(this.markerColor);
               ((JComponent)this.fields.get(var4)).setToolTipText(var6.getErrorMsg());
               this.fields.get(var4);
            } else {
               ((JComponent)this.fields.get(var4)).setBackground(Color.WHITE);
               ((JComponent)this.fields.get(var4)).setToolTipText("");
            }
         }
      }

   }

   public MDBlockComponentBean getBlockComponentBean() {
      return this.content;
   }

   protected JPanel getLayoutForRepeater() {
      if (this.pnl_repeater == null) {
         this.pnl_repeater = new JPanel();
         this.pnl_repeater.setLayout(new BoxLayout(this.pnl_repeater, 0));
         this.pnl_repeater.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
         this.pnl_repeater.add(Box.createHorizontalStrut(5));
         this.pnl_repeater.add(this.createButton("add", "Új", this, 90, 25));
         this.pnl_repeater.add(Box.createHorizontalStrut(2));
         this.pnl_repeater.add(this.createButton("remove", "Töröl", this, 90, 25));
         this.pnl_repeater.add(Box.createHorizontalStrut(2));
         this.pnl_repeater.add(this.createButton("prev", "Vissza", this, 90, 25));
         this.pnl_repeater.add(Box.createHorizontalStrut(2));
         this.pnl_repeater.add(this.createButton("next", "Előre", this, 90, 25));
         this.pnl_repeater.add(Box.createHorizontalStrut(15));
         this.pnl_repeater.add(this.lbl_blockCount);
         this.pnl_repeater.add(Box.createHorizontalGlue());
      }

      return this.pnl_repeater;
   }

   protected JPanel getLayoutForContent() {
      if (this.pnl_content == null) {
         this.pnl_content = new JPanel();
         this.pnl_content.setLayout(new BoxLayout(this.pnl_content, 1));
         if (this.blockDefinition.getBlockLayoutDef().hasColor()) {
            this.pnl_content.setBorder(BorderFactory.createBevelBorder(0));
            this.pnl_content.setBackground(new Color(this.blockDefinition.getBlockLayoutDef().getRed(), this.blockDefinition.getBlockLayoutDef().getGreen(), this.blockDefinition.getBlockLayoutDef().getBlue()));
         }

         BlockLayoutDef.Row[] var1 = this.blockDefinition.getBlockLayoutDef().getRows();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            BlockLayoutDef.Row var4 = var1[var3];
            JPanel var5 = new JPanel();
            var5.setLayout(new BoxLayout(var5, 0));
            int var10000 = var4.getTopSpace();
            var4.getClass();
            if (var10000 != -1) {
               this.pnl_content.add(Box.createVerticalStrut(var4.getTopSpace()));
            }

            BlockLayoutDef.Row.RowElement[] var6 = var4.getRowElements();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               BlockLayoutDef.Row.RowElement var9 = var6[var8];
               var10000 = var9.getType();
               var9.getClass();
               if (var10000 == 0) {
                  var10000 = var9.getWidth();
                  var9.getClass();
                  if (var10000 != -1) {
                     var5.add(Box.createHorizontalStrut(var9.getWidth()));
                  } else {
                     var5.add(Box.createHorizontalGlue());
                  }
               } else {
                  var10000 = var9.getType();
                  var9.getClass();
                  if (var10000 == 1) {
                     if (var9.hasLabel()) {
                        var5.add(this.createLabel(var9.getKey()));
                        var5.add(Box.createHorizontalStrut(var9.getSpace()));
                     }

                     JComponent var10 = this.createInputForMD(var9.getKey());
                     var10.setMaximumSize(new Dimension(var9.getWidth(), GuiUtil.getCommonItemHeight() + 2));
                     var10.setPreferredSize(new Dimension(var9.getWidth(), GuiUtil.getCommonItemHeight() + 2));
                     var10.setMinimumSize(new Dimension(var9.getWidth(), GuiUtil.getCommonItemHeight() + 2));
                     var10.setSize(new Dimension(var9.getWidth(), GuiUtil.getCommonItemHeight() + 2));
                     var5.add(var10);
                  } else {
                     var10000 = var9.getType();
                     var9.getClass();
                     if (var10000 == 2 && !"".equals(var9.getSymbol())) {
                        JLabel var11 = new JLabel(var9.getSymbol());
                        var11.setMaximumSize(new Dimension(var9.getWidth(), var4.getHeigth()));
                        var11.setPreferredSize(new Dimension(var9.getWidth(), var4.getHeigth()));
                        var11.setMinimumSize(new Dimension(var9.getWidth(), var4.getHeigth()));
                        var11.setSize(new Dimension(var9.getWidth(), var4.getHeigth()));
                        var5.add(var11);
                     }
                  }
               }
            }

            this.pnl_content.add(var5);
            this.pnl_content.add(Box.createVerticalStrut(var4.getBottomSpace()));
         }
      }

      return this.pnl_content;
   }

   public boolean isComponentForBlock(String var1) {
      return this.blockDefinition.getBlockName().equals(var1);
   }

   public String getBlockComponentManages() {
      return this.blockDefinition.getBlockName();
   }

   public String[] getMasterDataInBlock() {
      return this.blockDefinition.getMasterDataNames();
   }

   protected String getTextForMasterData(String var1) {
      String var2 = var1;
      if (this.mdNames == null) {
         this.mdNames = new Properties();

         try {
            this.mdNames.load(ClassLoader.getSystemClassLoader().getResourceAsStream("resources/masterdata/masterdatalabel.properties"));
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      if (this.mdNames.containsKey(var1)) {
         var2 = this.mdNames.getProperty(var1);
      }

      return var2;
   }

   protected JLabel createLabel(String var1) {
      return new JLabel(this.getTextForMasterData(var1));
   }

   protected JComponent createInputForMD(String var1) {
      Object var2;
      try {
         var2 = MDGUIFieldFactory.getInstance().getInputFieldForType(MDRepositoryMetaFactory.getMDRepositoryMeta().getTypeOfMasterData(var1));
         ((JComponent)var2).setName(var1);
      } catch (MDRepositoryException var4) {
         var4.printStackTrace();
         var2 = new JTextField();
         ((JComponent)var2).setPreferredSize(new Dimension(GuiUtil.getW("WWWWWWWWWWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 2));
         ((JComponent)var2).setSize(new Dimension(GuiUtil.getW("WWWWWWWWWWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 2));
      }

      this.fields.put(var1, (JComponent) var2);
      return (JComponent)var2;
   }

   private String getValueOfComponent(JComponent var1) {
      String var2 = "";
      String var3 = MDGUIFieldFactory.getInstance().getNameOfGetter(var1.getClass().getName());

      try {
         Object var4 = var1.getClass().getMethod(var3, (Class[])null).invoke(var1, (Object[])null);
         var2 = MDGUIFieldFactory.getInstance().postProcessData(var4, var1);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return var2;
   }

   private void setValueOfComponent(JComponent var1, String var2) {
      String var3 = MDGUIFieldFactory.getInstance().getNameOfSetter(var1.getClass().getName());

      try {
         var1.getClass().getMethod(var3, String.class).invoke(var1, var2);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   private JButton createButton(String var1, String var2, ActionListener var3, int var4, int var5) {
      JButton var6 = new JButton(var2);
      var6.setActionCommand(var1);
      var6.setPreferredSize(new Dimension(GuiUtil.getW(var6, var2), GuiUtil.getCommonItemHeight() + 2));
      var6.setMaximumSize(var6.getPreferredSize());
      var6.addActionListener(var3);
      return var6;
   }

   private void refreshCounter() {
      StringBuffer var1 = new StringBuffer();
      if (this.content.getSize() > 0) {
         var1.append(this.current_block);
         var1.append("/");
         var1.append(this.content.getSize());
      } else {
         var1.append("nincs(enek) ");
         var1.append(this.blockDefinition.getBlockName().toLowerCase());
      }

      var1.append(" [max: ");
      var1.append(this.blockDefinition.getMax());
      var1.append("]");
      this.lbl_blockCount.setText(var1.toString());
   }

   private void enableDisableInputFields() {
      String[] var1 = this.blockDefinition.getMasterDataNames();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         if (this.fields.containsKey(var4)) {
            if (this.content.getSize() == 0) {
               ((JComponent)this.fields.get(var4)).setEnabled(false);
            } else {
               ((JComponent)this.fields.get(var4)).setEnabled(true);
            }
         } else {
            String var5 = null;
            if (PropertyList.getInstance().get("prop.const.mddebug") != null) {
               var5 = (String)((Vector)PropertyList.getInstance().get("prop.const.mddebug")).get(0);
            }

            if ("true".equals(var5)) {
               System.err.println("'" + var4 + "': definialt adatmezo GUI input lehetoseg nelkul!");
            }
         }
      }

   }

   private boolean isMandatoryBlock() {
      return this.blockDefinition.getMin() == 1;
   }

   private void setComponentSize(String var1) {
      Object var2 = this.fields.get(var1);
      if (var2 instanceof JFormattedTextField) {
         boolean var3 = true;

         int var7;
         try {
            var7 = ((MaskFormatter)((JFormattedTextField)var2).getFormatter()).getMask().length();
         } catch (Exception var5) {
            var7 = 15;
         }

         var7 = Math.min(var7, this.maskedS.length());
         Dimension var4 = new Dimension(GuiUtil.getW(this.maskedS.substring(0, var7)), GuiUtil.getCommonItemHeight() + 2);
         ((JFormattedTextField)var2).setSize(var4);
         ((JFormattedTextField)var2).setPreferredSize(var4);
      } else if (!(var2 instanceof JTextField)) {
         if (var2 instanceof ENYKFilterComboPanel) {
            ((ENYKFilterComboPanel)var2).getCombo().setSize(new Dimension(GuiUtil.getW("WWWWWWWW"), GuiUtil.getCommonItemHeight() + 2));
            ((ENYKFilterComboPanel)var2).getCombo().setPreferredSize(new Dimension(GuiUtil.getW("WWWWWWWW"), GuiUtil.getCommonItemHeight() + 2));
         }

      } else {
         try {
            if (this.shortStringsEndsWithSet.contains(var1) || this.shortStringsEndsWithSet.contains(var1.substring(2))) {
               ((JTextField)var2).setSize(this.shortDim);
               ((JTextField)var2).setPreferredSize(this.shortDim);
               return;
            }
         } catch (Exception var6) {
         }

         ((JTextField)var2).setSize(this.dim);
         ((JTextField)var2).setPreferredSize(this.dim);
      }
   }

   static {
      DIFF_MARKER_COLOR = Color.YELLOW;
   }
}
