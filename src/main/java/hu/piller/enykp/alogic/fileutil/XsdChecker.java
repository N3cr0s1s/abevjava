package hu.piller.enykp.alogic.fileutil;

import hu.piller.enykp.alogic.fileloader.xml.DefaultXMLParser;
import hu.piller.enykp.util.base.Result;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XsdChecker {
   public boolean hasError;
   public String errormsg;
   public Vector headerrorlist;
   private int skipcount;

   public Result _load(InputStream var1, String var2) {
      Result var3 = new Result();

      try {
         XsdChecker.bodyhandler var4 = new XsdChecker.bodyhandler();
         InputSource var10 = new InputSource(var1);
         var10.setEncoding(var2);
         if (this.skipcount != 0) {
            var1.skip((long)this.skipcount);
         }

         DefaultXMLParser var11 = new DefaultXMLParser();
         DefaultXMLParser.silent = true;
         var11.setContentHandler(var4);
         var11.parse(var10);
         var1.close();
      } catch (Exception var9) {
         var3.setOk(false);
         String var5 = var9.getMessage();
         if (var5 == null) {
            if (this.errormsg == null) {
               this.errormsg = var9.toString();
            }

            var9.printStackTrace();
         } else if (!var5.equals("OUT")) {
            if (var5.equals("HEADCHECK")) {
               try {
                  this.errormsg = (String)this.headerrorlist.get(0);
               } catch (Exception var8) {
               }
            } else {
               if (this.errormsg == null) {
                  this.errormsg = var9.getMessage();
               }

               if (var9 instanceof SAXParseException) {
                  SAXParseException var6 = (SAXParseException)var9;
                  this.errormsg = "Súlyos hiba az XML formai ellenőrzése során: " + this.errormsg + "##" + "sor: " + var6.getLineNumber() + "  oszlop: " + var6.getColumnNumber();
               }
            }
         }

         try {
            var1.close();
         } catch (IOException var7) {
         }
      }

      if (this.errormsg != null) {
         var3.errorList.add(this.errormsg);
      }

      return var3;
   }

   public String getEncoding(File var1) {
      FileInputStream var2 = null;

      try {
         this.skipcount = 0;
         var2 = new FileInputStream(var1);
         int var3 = var2.read();
         if (var3 == 255) {
            return "UTF-16LE";
         } else if (var3 == 254) {
            return "UTF-16BE";
         } else {
            if (var3 == 239) {
               this.skipcount = 3;
            }

            byte[] var4 = new byte[256];
            var2.read(var4);
            String var5 = new String(var4);
            int var6 = var5.indexOf("encoding=");
            int var7 = var5.indexOf("\"", var6);
            int var8 = var5.indexOf("\"", var7 + 1);
            if (var7 == -1) {
               var7 = var5.indexOf("'", var6);
               var8 = var5.indexOf("'", var7 + 1);
            }

            var2.close();
            return var5.substring(var7 + 1, var8);
         }
      } catch (Exception var10) {
         try {
            var2.close();
         } catch (Exception var9) {
         }

         return null;
      }
   }

   class bodyhandler extends DefaultHandler {
      HashSet codehs;

      public void characters(char[] var1, int var2, int var3) throws SAXException {
      }

      public void endDocument() throws SAXException {
         super.endDocument();
      }

      public void endElement(String var1, String var2, String var3) throws SAXException {
         super.endElement(var1, var2, var3);
      }

      public void endPrefixMapping(String var1) throws SAXException {
         super.endPrefixMapping(var1);
      }

      public void error(SAXParseException var1) throws SAXException {
         super.error(var1);
      }

      public void fatalError(SAXParseException var1) throws SAXException {
         super.fatalError(var1);
      }

      public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
         super.ignorableWhitespace(var1, var2, var3);
      }

      public void notationDecl(String var1, String var2, String var3) throws SAXException {
         super.notationDecl(var1, var2, var3);
      }

      public void processingInstruction(String var1, String var2) throws SAXException {
         super.processingInstruction(var1, var2);
      }

      public void setDocumentLocator(Locator var1) {
         super.setDocumentLocator(var1);
      }

      public void skippedEntity(String var1) throws SAXException {
         super.skippedEntity(var1);
      }

      public void startDocument() throws SAXException {
      }

      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         super.startElement(var1, var2, var3, var4);
      }

      public void startPrefixMapping(String var1, String var2) throws SAXException {
         super.startPrefixMapping(var1, var2);
      }

      public void unparsedEntityDecl(String var1, String var2, String var3, String var4) throws SAXException {
         super.unparsedEntityDecl(var1, var2, var3, var4);
      }

      public void warning(SAXParseException var1) throws SAXException {
         super.warning(var1);
      }
   }
}
