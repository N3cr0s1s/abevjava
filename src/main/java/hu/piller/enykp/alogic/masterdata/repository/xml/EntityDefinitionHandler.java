package hu.piller.enykp.alogic.masterdata.repository.xml;

import hu.piller.enykp.alogic.masterdata.core.BlockDefinition;
import hu.piller.enykp.alogic.masterdata.core.BlockLayoutDef;
import java.util.Hashtable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class EntityDefinitionHandler extends DefaultHandler {
   private static final String ENTITY_DEF = "EntityDef";
   private static final String ENTITY_TYPE = "type";
   private static final String BLOCK_DEF = "BlockDef";
   private static final String BLOCK_NAME = "name";
   private static final String BLOCK_MULT_MIN = "min";
   private static final String BLOCK_MULT_MAX = "max";
   private static final String BLOCK_MASTERDATA = "masterdata";
   private static final String VALIDATOR = "validator";
   private static final String BLOCK_LAYOUT = "layout";
   private static final String BLOCK_LAYOUT_ATTR_RED = "red";
   private static final String BLOCK_LAYOUT_ATTR_GREEN = "green";
   private static final String BLOCK_LAYOUT_ATTR_BLUE = "blue";
   private static final String BLOCK_LAYOUT_ATTR_WIDTH = "width";
   private static final String BLOCK_LAYOUT_ATTR_HEIGTH = "heigth";
   private static final String BLOCK_ROW = "row";
   private static final String BLOCK_ROW_ATTR_TOPSPACE = "topspace";
   private static final String BLOCK_ROW_ATTR_BOTTOMSPACE = "bottomspace";
   private static final String BLOCK_ROW_ATTR_HEIGTH = "heigth";
   private static final String BLOCK_MDATA_KEY = "key";
   private StringBuffer nodeValue;
   private String currentType;
   private String currValidatorClass;
   private String currName;
   private int curr_min;
   private int curr_max;
   private String currMasterData;
   private BlockLayoutDef currLayout;
   private BlockLayoutDef.Row currLayoutRow;
   private BlockLayoutDef.Row.RowElement currLayoutRowElement;
   private Hashtable<String, BlockDefinition[]> definitions = new Hashtable();
   private Hashtable<String, String> validatorClasses = new Hashtable();

   public Hashtable<String, BlockDefinition[]> getDefinitions() {
      return this.definitions;
   }

   public Hashtable<String, String> getValidatorClasses() {
      return this.validatorClasses;
   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      if ("BlockDef".equals(var2)) {
         BlockDefinition var4 = new BlockDefinition(this.currName, this.curr_min, this.curr_max, this.currMasterData, this.currLayout);
         this.definitions.put(this.currentType, this.append((BlockDefinition[])this.definitions.get(this.currentType), var4));
      } else if ("type".equals(var2)) {
         this.currentType = this.nodeValue.toString();
         this.definitions.put(this.currentType, new BlockDefinition[0]);
         if (!"".equals(this.currValidatorClass)) {
            this.validatorClasses.put(this.currentType, this.currValidatorClass);
         }
      } else if ("name".equals(var2)) {
         this.currName = this.nodeValue.toString();
      } else if ("min".equals(var2)) {
         this.curr_min = Integer.parseInt(this.nodeValue.toString());
      } else if ("max".equals(var2)) {
         this.curr_max = Integer.parseInt(this.nodeValue.toString());
      } else if ("masterdata".equals(var2)) {
         this.currMasterData = this.nodeValue.toString();
      } else if ("key".equals(var2)) {
         int var10000 = this.currLayoutRowElement.getType();
         this.currLayoutRowElement.getClass();
         if (var10000 == 1) {
            this.currLayoutRowElement.setKey(this.nodeValue.toString().trim());
         }
      } else if ("validator".equals(var2)) {
         if (!"".equals(this.currentType)) {
            this.validatorClasses.put(this.currentType, this.nodeValue.toString());
         } else {
            this.currValidatorClass = this.nodeValue.toString();
         }
      }

   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if ("BlockDef".equals(var2)) {
         this.currName = "";
         this.curr_min = this.curr_max = 0;
         this.currMasterData = "";
         this.currLayout = new BlockLayoutDef();
         this.currLayoutRow = null;
         this.currLayoutRowElement = null;
      } else {
         int var5;
         if ("layout".equals(var2)) {
            for(var5 = 0; var5 < var4.getLength(); ++var5) {
               if ("red".equals(var4.getLocalName(var5))) {
                  this.currLayout.setRed(Integer.parseInt(var4.getValue(var5)));
               } else if ("green".equals(var4.getLocalName(var5))) {
                  this.currLayout.setGreen(Integer.parseInt(var4.getValue(var5)));
               } else if ("blue".equals(var4.getLocalName(var5))) {
                  this.currLayout.setBlue(Integer.parseInt(var4.getValue(var5)));
               } else if ("width".equals(var4.getLocalName(var5))) {
                  this.currLayout.setWidth(Integer.parseInt(var4.getValue(var5)));
               } else if ("heigth".equals(var4.getLocalName(var5))) {
                  this.currLayout.setHeigth(Integer.parseInt(var4.getValue(var5)));
               }
            }
         } else if ("row".equals(var2)) {
            this.currLayoutRow = this.currLayout.addRow();

            for(var5 = 0; var5 < var4.getLength(); ++var5) {
               if ("topspace".equals(var4.getLocalName(var5))) {
                  this.currLayoutRow.setTopSpace(Integer.parseInt(var4.getValue(var5)));
               } else if ("bottomspace".equals(var4.getLocalName(var5))) {
                  this.currLayoutRow.setBottomSpace(Integer.parseInt(var4.getValue(var5)));
               } else if ("heigth".equals(var4.getLocalName(var5))) {
                  this.currLayoutRow.setHeigth(Integer.parseInt(var4.getValue(var5)));
               }
            }
         } else if ("element".equals(var2)) {
            var5 = var4.getIndex("type");
            if (var5 > -1) {
               this.currLayoutRowElement = this.currLayoutRow.addRowElement();
               BlockLayoutDef.Row.RowElement var10000;
               if (var4.getValue(var5).equals("glue")) {
                  var10000 = this.currLayoutRowElement;
                  this.currLayoutRowElement.getClass();
                  var10000.setType(0);
               } else if (var4.getValue(var5).equals("mdata")) {
                  var10000 = this.currLayoutRowElement;
                  this.currLayoutRowElement.getClass();
                  var10000.setType(1);
                  var5 = var4.getIndex("haslabel");
                  if (var5 > -1) {
                     this.currLayoutRowElement.setHasLabel(Boolean.parseBoolean(var4.getValue(var5)));
                  }

                  var5 = var4.getIndex("space");
                  if (var5 > -1) {
                     this.currLayoutRowElement.setSpace(Integer.parseInt(var4.getValue(var5)));
                  }
               } else if (var4.getValue(var5).equals("symbol")) {
                  var10000 = this.currLayoutRowElement;
                  this.currLayoutRowElement.getClass();
                  var10000.setType(2);
                  var5 = var4.getIndex("display");
                  if (var5 > -1) {
                     this.currLayoutRowElement.setSymbol(var4.getValue(var5));
                  }
               }

               var5 = var4.getIndex("width");
               if (var5 > -1) {
                  this.currLayoutRowElement.setWidth(Integer.parseInt(var4.getValue(var5)));
               }
            }
         } else if ("type".equals(var2)) {
            this.currentType = "";
         } else if ("validator".equals(var2)) {
            this.currValidatorClass = "";
         } else if ("EntityDef".equals(var2)) {
            this.currentType = "";
            this.currValidatorClass = "";
         }
      }

      this.nodeValue = new StringBuffer();
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      this.nodeValue.append(new String(var1, var2, var3));
   }

   private BlockDefinition[] append(BlockDefinition[] var1, BlockDefinition var2) {
      BlockDefinition[] var3 = new BlockDefinition[var1.length + 1];

      int var4;
      for(var4 = 0; var4 < var1.length; ++var4) {
         var3[var4] = var1[var4];
      }

      var3[var4] = var2;
      return var3;
   }
}
