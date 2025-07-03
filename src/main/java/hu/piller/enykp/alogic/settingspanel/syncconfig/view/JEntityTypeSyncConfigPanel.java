package hu.piller.enykp.alogic.settingspanel.syncconfig.view;

import hu.piller.enykp.alogic.masterdata.core.BlockDefinition;
import hu.piller.enykp.alogic.masterdata.sync.configuration.ConfigService;
import hu.piller.enykp.alogic.settingspanel.syncconfig.model.ConfigState;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.SpringUtilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

public class JEntityTypeSyncConfigPanel extends JPanel {
   private List<String> blockOrder;
   private BlockDefinition[] blockDefinitions;
   private Properties labels;
   private Map<String, JCheckBox> checkBoxes;
   private JPanel scrollableCanvas;
   private GridBagConstraints gbc;

   public JEntityTypeSyncConfigPanel() {
      this.setLayout(new GridLayout(1, 1));
      this.checkBoxes = new HashMap();
      this.scrollableCanvas = new JPanel();
      this.scrollableCanvas.setLayout(new GridBagLayout());
      this.gbc = new GridBagConstraints();
      this.gbc.gridx = 0;
      this.gbc.gridy = 0;
      this.gbc.fill = 1;
      this.gbc.weightx = 1.0D;
      this.gbc.weighty = 1.0D;
      this.add(new JScrollPane(this.scrollableCanvas), this.gbc);
   }

   public Map<String, ConfigState> getConfigSettings() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.checkBoxes.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.put(var3.getKey(), ((JCheckBox)var3.getValue()).isSelected() ? ConfigState.ENABLED : ConfigState.DISABLED);
      }

      return var1;
   }

   protected Map<String, JCheckBox> getCheckBoxes() {
      return this.checkBoxes;
   }

   protected void setBlockOrder(List<String> var1) {
      this.blockOrder = var1;
   }

   protected void setBlockDefinitions(BlockDefinition[] var1) {
      this.blockDefinitions = var1;
   }

   protected void setLabels(Properties var1) {
      this.labels = var1;
   }

   protected void initCheckBoxes() {
      HashSet var1 = new HashSet();
      Iterator var2 = this.blockOrder.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         BlockDefinition var4 = this.getBlockDefinitionByBlockName(var3);
         String[] var5 = var4.getMasterDataNames();
         var1.clear();
         String[] var6 = var5;
         int var7 = var5.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String var9 = var6[var8];
            if (!ConfigService.forbiddenPanids.contains(var9) && !var1.contains(var9)) {
               if (ConfigService.panidsToTechnicalMd.containsKey(var9)) {
                  var9 = (String)ConfigService.panidsToTechnicalMd.get(var9);
                  var1.addAll((Collection)ConfigService.technicalMdToPanids.get(var9));
               }

               JCheckBox var10 = GuiUtil.getANYKCheckBox();
               var10.setName(var3 + "." + var9);
               this.checkBoxes.put(var3 + "." + var9, var10);
            }
         }
      }

   }

   protected void setUpLayout() {
      HashSet var1 = new HashSet();

      for(Iterator var2 = this.blockOrder.iterator(); var2.hasNext(); ++this.gbc.gridy) {
         String var3 = (String)var2.next();
         BlockDefinition var4 = this.getBlockDefinitionByBlockName(var3);
         JPanel var5 = new JPanel(new SpringLayout());
         var5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(184, 207, 229)), var3));
         var5.setOpaque(true);
         String[] var6 = var4.getMasterDataNames();
         int var7 = 0;

         for(int var8 = 0; var8 < var6.length; ++var8) {
            String var9 = var6[var8];
            if (!ConfigService.forbiddenPanids.contains(var9) && !var1.contains(var9)) {
               if (ConfigService.panidsToTechnicalMd.containsKey(var9)) {
                  var9 = (String)ConfigService.panidsToTechnicalMd.get(var9);
                  var1.addAll((Collection)ConfigService.technicalMdToPanids.get(var9));
               }

               String var10 = var3 + "." + var9;
               String var11 = this.labels.getProperty(var9);
               if (var11 == null) {
                  var11 = var9;
               }

               JLabel var12 = new JLabel(var11);
               var12.setToolTipText("A(z) '" + var11 + "' adat részt vesz a vizsgálatban");
               ((JCheckBox)this.checkBoxes.get(var10)).setToolTipText("A(z) '" + var11 + "' adat részt vesz a vizsgálatban");
               var5.add((Component)this.checkBoxes.get(var10));
               var5.add(var12);
               ++var7;
            }
         }

         SpringUtilities.makeCompactGrid(var5, var7, 2, 5, 5, 5, 5);
         this.scrollableCanvas.add(var5, this.gbc);
      }

   }

   private BlockDefinition getBlockDefinitionByBlockName(String var1) {
      BlockDefinition[] var2 = this.blockDefinitions;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockDefinition var5 = var2[var4];
         if (var5.getBlockName().equals(var1)) {
            return var5;
         }
      }

      return null;
   }
}
