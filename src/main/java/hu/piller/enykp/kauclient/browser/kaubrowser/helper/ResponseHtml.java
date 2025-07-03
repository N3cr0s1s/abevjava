package hu.piller.enykp.kauclient.browser.kaubrowser.helper;

import hu.piller.enykp.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ResponseHtml {
   private Document html;

   public ResponseHtml(Document var1) {
      this.html = var1;
      if (Boolean.parseBoolean(System.getProperty("kau.trace"))) {
         System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
         System.out.println(DomUtil.documentToString(var1));
         System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
      }

   }

   public boolean hasFormActionForUrl(String var1) {
      if (this.html != null && var1 != null) {
         NodeList var2 = this.html.getElementsByTagName("FORM");

         for(int var3 = 0; var3 < var2.getLength(); ++var3) {
            Element var4 = (Element)var2.item(var3);
            if ("redirectForm".equals(var4.getAttribute("id")) && var1.equals(var4.getAttribute("action"))) {
               return true;
            }
         }
      }

      return false;
   }

   public String getSamlResponse() {
      if (this.html != null) {
         NodeList var1 = this.html.getElementsByTagName("INPUT");

         for(int var2 = 0; var2 < var1.getLength(); ++var2) {
            Element var3 = (Element)var1.item(var2);
            if ("SAMLResponse".equals(var3.getAttribute("name"))) {
               return var3.getAttribute("value").replaceAll("&#10;", "\n");
            }
         }
      }

      return null;
   }

   public String getRelayState() {
      if (this.html != null) {
         NodeList var1 = this.html.getElementsByTagName("INPUT");

         for(int var2 = 0; var2 < var1.getLength(); ++var2) {
            Element var3 = (Element)var1.item(var2);
            if ("RelayState".equals(var3.getAttribute("name"))) {
               return var3.getAttribute("value").replaceAll("&#10;", "\n");
            }
         }
      }

      return null;
   }
}
