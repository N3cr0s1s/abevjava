package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.interfaces.IHelperLoad;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class DataSource implements IHelperLoad {
   private File file;
   private Hashtable forms;
   private Hashtable tag_cache;

   public DataSource(File var1) {
      this.file = var1;
      this.forms = new Hashtable();
      this.tag_cache = new Hashtable();
   }

   DataSource(File var1, Object[] var2) {
      this(var1);
      this.forms.put(((Hashtable)var2[1]).get("id"), var2);
   }

   public void initialize() {
   }

   public void read() {
      try {
         if (this.forms.size() == 0) {
            this.load(this.file);
         }

      } catch (ParserConfigurationException var2) {
         throw new RuntimeException(var2);
      } catch (SAXException var3) {
         throw new RuntimeException(var3);
      } catch (IOException var4) {
         throw new RuntimeException(var4);
      }
   }

   public void getData(Hashtable var1) {
      if (var1.get("get_template_helper") != null) {
         var1.put("get_template_helper", this);
      }

      if (var1.get("get_form_iterator") != null) {
         var1.put("get_form_iterator", new DataSource.FormIterator(this.file, this.forms.elements()));
      }

      if (var1.get("get_file_name") != null) {
         var1.put("get_file_name", this.file == null ? "" : this.file.toString());
      }

      if (this.forms.size() == 1) {
         Object[] var2 = (Object[])((Object[])this.forms.elements().nextElement());
         Object[] var3;
         if (var1.get("metas") != null && (var3 = this.findFirstTag(var2, "metas", true, true)) != null) {
            var1.put("metas", var3);
         }

         if (var1.get("vars") != null && (var3 = this.findFirstTag(var2, "vars", true, true)) != null) {
            var1.put("vars", var3);
         }

         if (var1.get("formcalcs") != null && (var3 = this.findFirstTag(var2, "formcalcs", true, true)) != null) {
            var1.put("formcalcs", var3);
         }

         if (var1.get("pregencalcs") != null && (var3 = this.findFirstTag(var2, "pregencalcs", true, true)) != null) {
            var1.put("pregencalcs", var3);
         }

         if (var1.get("postgencalcs") != null && (var3 = this.findFirstTag(var2, "postgencalcs", true, true)) != null) {
            var1.put("postgencalcs", var3);
         }

         if (var1.get("betoltertek") != null && (var3 = this.findFirstTag(var2, "betoltertek", true, true)) != null) {
            var1.put("betoltertek", var3);
         }

         if (var1.get("pagecalcs") != null && (var3 = this.findFirstTag(var2, "pagecalcs", true, true)) != null) {
            var1.put("pagecalcs", var3);
         }

         if (var1.get("fieldcalcs") != null && (var3 = this.findFirstTag(var2, "fieldcalcs", true, true)) != null) {
            var1.put("fieldcalcs", var3);
         }

         if (var1.get("matrices") != null && (var3 = this.findFirstTag(var2, "matrices", true, true)) != null) {
            var1.put("matrices", var3);
         }

         if (var1.get("form") != null && (var3 = this.findFirstTag(var2, "form", true, true)) != null) {
            var1.put("form", var3);
         }
      }

   }

   public void release() {
      this.forms.clear();
   }

   private void load(File var1) throws ParserConfigurationException, SAXException, IOException {
      this.release();
      FileInputStream var2 = new FileInputStream(var1);
      SAXParserFactory var3 = SAXParserFactory.newInstance();
      SAXParser var4 = var3.newSAXParser();
      InputSource var5 = new InputSource(var2);
      var5.setEncoding("iso-8859-2");
      var4.parse(var5, new DataSource.TemplateHandler(this.forms));
   }

   private Object[] findFirstTag(Object[] var1, String var2, boolean var3, boolean var4) {
      Object[] var5 = null;
      if (var3) {
         var5 = (Object[])((Object[])this.tag_cache.get(var2));
      }

      if (var5 == null) {
         var5 = this.findFirstTagRecursive(var1, var2, var4);
      }

      return var5;
   }

   private Object[] findFirstTagRecursive(Object[] var1, String var2, boolean var3) {
      Object[] var4 = null;
      String var5 = (String)var1[0];
      if (var5.equalsIgnoreCase(var2)) {
         var4 = var1;
         if (var3) {
            this.tag_cache.put(var2, var1);
         }
      } else {
         Vector var6 = (Vector)var1[3];
         int var7 = 0;

         for(int var8 = var6.size(); var7 < var8 && var4 == null; ++var7) {
            var4 = this.findFirstTagRecursive((Object[])((Object[])var6.get(var7)), var2, var3);
         }
      }

      return var4;
   }

   private static class FormIterator implements Iterator {
      private File file;
      private Enumeration forms;

      FormIterator(File var1, Enumeration var2) {
         this.file = var1;
         if (var2 != null) {
            this.forms = var2;
         }

      }

      public void remove() {
      }

      public boolean hasNext() {
         return this.forms.hasMoreElements();
      }

      public Object next() {
         DataSource var1 = null;
         if (this.hasNext()) {
            var1 = new DataSource(this.file, (Object[])((Object[])this.forms.nextElement()));
         }

         return var1;
      }
   }

   private static class TemplateHandler extends DefaultHandler {
      private static final Hashtable ht_enabled_tags = new Hashtable();
      private static final boolean[] enabled_tags_nf = new boolean[]{false, false};
      private Hashtable forms;
      private DataSource.TemplateHandler.Current current;
      private boolean parse_content;

      TemplateHandler(Hashtable var1) {
         this.forms = var1;
      }

      public void startDocument() throws SAXException {
         super.startDocument();
         this.current = new DataSource.TemplateHandler.Current((DataSource.TemplateHandler.Current)null, (Object[])null, this.forms);
         this.parse_content = false;
      }

      public void endDocument() throws SAXException {
         super.endDocument();
         this.current = null;
      }

      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         super.startElement(var1, var2, var3, var4);
         if ("".equals(var2)) {
            var2 = var3;
         }

         boolean[] var5 = this.isEnabledTag(var2);
         boolean var6 = false;
         if (var5[0]) {
            var6 = true;
            this.parse_content = var5[1];
         }

         if (var6 || this.parse_content) {
            Hashtable var7 = this.getHtAttrs(var4);
            Object[] var8 = new Object[]{var2, var7, new StringBuffer(), new Vector()};
            if (this.current.parent == null) {
               this.current.forms.put(var7.get("id"), var8);
            } else {
               ((Vector)this.current.tag[3]).add(var8);
            }

            this.current = new DataSource.TemplateHandler.Current(this.current, var8, (Hashtable)null);
         }

      }

      public void characters(char[] var1, int var2, int var3) throws SAXException {
         super.characters(var1, var2, var3);
         if (this.parse_content) {
            ((StringBuffer)this.current.tag[2]).append(var1, var2, var3);
         }

      }

      public void endElement(String var1, String var2, String var3) throws SAXException {
         super.endElement(var1, var2, var3);
         if ("".equals(var2)) {
            var2 = var3;
         }

         boolean var4 = this.isEnabledTag(var2)[0];
         if (var4 || this.parse_content) {
            this.current = this.current.parent;
            if (var4) {
               this.parse_content = false;
            }
         }

      }

      public void warning(SAXParseException var1) throws SAXException {
         super.warning(var1);
      }

      public void error(SAXParseException var1) throws SAXException {
         super.error(var1);
      }

      public void fatalError(SAXParseException var1) throws SAXException {
         super.fatalError(var1);
      }

      private Hashtable getHtAttrs(Attributes var1) {
         Hashtable var2 = new Hashtable();
         int var4 = 0;

         for(int var5 = var1.getLength(); var4 < var5; ++var4) {
            String var3 = var1.getLocalName(var4);
            if ("".equals(var3)) {
               var3 = var1.getQName(var4);
            }

            var2.put(var3, var1.getValue(var4));
         }

         return var2;
      }

      private boolean[] isEnabledTag(String var1) {
         boolean[] var2 = (boolean[])((boolean[])ht_enabled_tags.get(var1.toLowerCase()));
         var2 = var2 == null ? enabled_tags_nf : var2;
         return var2;
      }

      static {
         ht_enabled_tags.put("form", new boolean[]{true, false});
         ht_enabled_tags.put("calcs", new boolean[]{true, true});
      }

      private static class Current {
         Hashtable forms;
         DataSource.TemplateHandler.Current parent;
         Object[] tag;

         Current(DataSource.TemplateHandler.Current var1, Object[] var2, Hashtable var3) {
            this.parent = var1;
            this.tag = var2;
            this.forms = var3;
         }
      }
   }
}
