package hu.piller.enykp.alogic.masterdata.gui.selector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SelectorHandler extends DefaultHandler {
   StringBuffer val;
   private SelectorPanel panel;
   private MDList curList;
   private String curOrg;
   private String curEntityName;
   private Part curPart;

   public SelectorHandler() {
      SAXParserFactory var1 = SAXParserFactory.newInstance();
      var1.setNamespaceAware(true);
      var1.setValidating(false);

      try {
         SAXParser var2 = var1.newSAXParser();
         var2.parse(ClassLoader.getSystemClassLoader().getResourceAsStream("resources/masterdata/selector.xml"), this);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public SelectorPanel getSelectorPanel() {
      return this.panel;
   }

   public void startDocument() throws SAXException {
      this.panel = new SelectorPanel();
   }

   public void endDocument() throws SAXException {
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      this.val = new StringBuffer();
      if ("org".equals(var2)) {
         for(int var5 = 0; var5 < var4.getLength(); ++var5) {
            if ("sn".equals(var4.getLocalName(var5))) {
               this.curOrg = var4.getValue(var5);
               this.panel.setOrg(this.curOrg);
               break;
            }
         }
      } else if ("list".equals(var2)) {
         String var8 = "";
         String var6 = "";

         for(int var7 = 0; var7 < var4.getLength(); ++var7) {
            if ("id".equals(var4.getLocalName(var7))) {
               var6 = var4.getValue(var7);
            } else if ("type".equals(var4.getLocalName(var7))) {
               var8 = var4.getValue(var7);
            }
         }

         if ("e".equals(var8)) {
            this.curList = new EntityMDList();
         } else if ("b".equals(var8)) {
            this.curList = new BlockMDList();
         }

         this.curList.setType(var8);
         this.curList.setId(var6);
      } else if ("part".equals(var2)) {
         this.curPart = new Part();
      }

   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      if ("name".equals(var2)) {
         this.curEntityName = this.val.toString();
         if (this.curList.getType().equals("e")) {
            ((EntityMDList)this.curList).setEntityName(this.curEntityName);
         }
      } else if ("block".equals(var2)) {
         this.curPart.setBlock(this.val.toString());
      } else if ("mdata".equals(var2)) {
         this.curPart.setMd(this.val.toString());
      } else if ("part".equals(var2)) {
         this.curList.addPart(this.curEntityName, this.curPart);
      } else if ("list".equals(var2)) {
         this.panel.addList(this.curList);
      } else if ("ref".equals(var2)) {
         if ("b".equals(this.curList.getType())) {
            ((BlockMDList)this.curList).setRef(this.panel.getListByOrgAndId(this.curOrg, this.val.toString()));
         }
      } else if ("rblock".equals(var2)) {
         if ("b".equals(this.curList.getType())) {
            ((BlockMDList)this.curList).setBlockName(this.curEntityName, this.val.toString());
         }
      } else if ("label".equals(var2)) {
         this.curList.setLabel(this.val.toString());
      } else if ("labelpadding".equals(var2)) {
         this.curList.setLabelPadding(Integer.parseInt(this.val.toString()));
      }

   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      this.val.append(new String(var1, var2, var3));
   }
}
