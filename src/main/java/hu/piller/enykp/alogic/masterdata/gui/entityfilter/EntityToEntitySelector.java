package hu.piller.enykp.alogic.masterdata.gui.entityfilter;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

public class EntityToEntitySelector {
   public String[] selectorPartNames = new String[0];
   public SelectorPartRule[] selectorPartRules = new SelectorPartRule[0];

   public EntityToEntitySelector() {
      this.loadRules();
   }

   private void loadRules() {
      SAXParserFactory var1 = SAXParserFactory.newInstance();
      var1.setValidating(false);
      var1.setNamespaceAware(true);

      try {
         SAXParser var2 = var1.newSAXParser();
         EntitySelectorHandler var3 = new EntitySelectorHandler();
         var2.parse(ClassLoader.getSystemClassLoader().getResourceAsStream("resources/masterdata/EntitySelector.xml"), var3);
         this.selectorPartNames = var3.getPartNames();
         this.selectorPartRules = var3.getRules();
      } catch (ParserConfigurationException var4) {
         var4.printStackTrace();
      } catch (SAXException var5) {
         var5.printStackTrace();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public String[] getSelectorPartNames() {
      return this.selectorPartNames;
   }

   public EntitySelector map(Entity var1) {
      EntitySelector var2 = new EntitySelector(var1.getName(), var1.getId());
      String[] var3 = this.getSelectorPartNames();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         SelectorPartRule var7 = this.getRuleForEntityTypeAndPartName(var1.getName(), var6);
         StringBuffer var8 = new StringBuffer();
         if (var7 != null) {
            SelectorPartRule.BlockMapping[] var9 = var7.getBlockMappings();
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               SelectorPartRule.BlockMapping var12 = var9[var11];
               if (var12.getBlockName().equals("CONSTANT")) {
                  var8.append(var12.getMdName());
               } else {
                  var8.append(var1.getBlock(var12.getBlockName()).getMasterData(var12.getMdName()).getValue());
               }

               var8.append(" ");
            }
         }

         var2.setSelectorValue(var6, var8.toString());
      }

      return var2;
   }

   private SelectorPartRule getRuleForEntityTypeAndPartName(String var1, String var2) {
      SelectorPartRule[] var3 = this.selectorPartRules;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         SelectorPartRule var6 = var3[var5];
         if (this.isRuleForEntitTypeAndPartName(var6, var1, var2)) {
            return var6;
         }
      }

      return null;
   }

   private boolean isRuleForEntitTypeAndPartName(SelectorPartRule var1, String var2, String var3) {
      return var2.equals(var1.getEntityType()) && var3.equals(var1.getPartName());
   }
}
