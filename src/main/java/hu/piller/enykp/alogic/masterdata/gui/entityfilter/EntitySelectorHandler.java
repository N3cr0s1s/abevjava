package hu.piller.enykp.alogic.masterdata.gui.entityfilter;

import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class EntitySelectorHandler extends DefaultHandler {
   public static final String PART = "part";
   public static final String NAME = "name";
   public static final String ENTITY = "entity";
   public static final String TYPE = "type";
   public static final String BLOCK = "block";
   public static final String BLOCKNAME = "blockname";
   public static final String MDATA = "mdata";
   StringBuffer val;
   SelectorPartRule[] rules = new SelectorPartRule[0];
   private SelectorPartRule curPartRule;
   private SelectorPartRule.BlockMapping curBlockMapping;
   private String curPartName;
   String[] partNames = null;

   public SelectorPartRule[] getRules() {
      return this.rules;
   }

   public String[] getPartNames() {
      if (this.partNames == null) {
         Vector var1 = new Vector();
         SelectorPartRule[] var2 = this.rules;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            SelectorPartRule var5 = var2[var4];
            if (!var1.contains(var5.getPartName())) {
               var1.add(var5.getPartName());
            }
         }

         this.partNames = (String[])var1.toArray(new String[var1.size()]);
      }

      return this.partNames;
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      this.val.append(new String(var1, var2, var3));
   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      if ("type".equals(var2)) {
         this.curPartRule.setEntityType(this.val.toString());
      } else if ("name".equals(var2)) {
         this.curPartName = this.val.toString();
      } else if ("blockname".equals(var2)) {
         this.curBlockMapping.setBlockName(this.val.toString());
      } else if ("mdata".equals(var2)) {
         this.curBlockMapping.setMdName(this.val.toString());
      }

   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if ("entity".equals(var2)) {
         this.curPartRule = this.addRule();
         this.curPartRule.setPartName(this.curPartName);
      } else if ("block".equals(var2)) {
         this.curBlockMapping = this.curPartRule.addBlockMapping();
      }

      this.val = new StringBuffer();
   }

   private SelectorPartRule addRule() {
      SelectorPartRule[] var1 = new SelectorPartRule[this.rules.length + 1];
      int var2 = 0;
      SelectorPartRule[] var3 = this.rules;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         SelectorPartRule var6 = var3[var5];
         var1[var2++] = var6;
      }

      var1[var2] = new SelectorPartRule();
      this.rules = var1;
      return this.rules[var2];
   }
}
