package hu.piller.enykp.alogic.fileutil;

import hu.piller.enykp.util.base.Tools;
import java.util.Enumeration;
import java.util.Hashtable;

public class DatastoreKeyToXml {
   public static Hashtable convertHTKeys(Hashtable var0) {
      try {
         Hashtable var1 = new Hashtable();
         Enumeration var2 = var0.keys();

         while(var2.hasMoreElements()) {
            String var3 = (String)var2.nextElement();
            var1.put(convert(var3), var0.get(var3));
         }

         return var1;
      } catch (Exception var4) {
         return var0;
      }
   }

   public static Hashtable convertHTValues(Hashtable var0) {
      try {
         Enumeration var1 = var0.keys();

         while(var1.hasMoreElements()) {
            String var2 = (String)var1.nextElement();
            var0.put(var2, convert((String)var0.get(var2)));
         }
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
      }

      return var0;
   }

   public static String convert(String var0) {
      try {
         int var1 = var0.indexOf(95);
         if (var1 == -1) {
            return var0;
         } else if (var0.length() < 10) {
            return var0;
         } else {
            int var2 = Integer.parseInt(var0.substring(0, var1)) + 1;
            String var3 = "0000" + var2;
            var3 = var3.substring(var3.length() - 4);
            return var0.substring(var1 + 1, var1 + 3) + var3 + var0.substring(var1 + 7);
         }
      } catch (NumberFormatException var4) {
         return var0;
      }
   }

   public static Hashtable keyHtmlConvert(Hashtable var0) {
      Hashtable var1 = new Hashtable(var0.size());
      Enumeration var2 = var0.keys();

      while(var2.hasMoreElements()) {
         String var3 = (String)var2.nextElement();
         var1.put(htmlConvert(var3), var0.get(var3));
      }

      return var1;
   }

   public static Hashtable valueHtmlConvert(Hashtable var0) {
      Enumeration var1 = var0.keys();

      while(var1.hasMoreElements()) {
         String var2 = (String)var1.nextElement();
         if (var0.get(var2) instanceof String) {
            var0.put(var2, htmlConvert((String)var0.get(var2)));
         }
      }

      return var0;
   }

   public static String htmlConvert(String var0) {
      if (var0 == null) {
         return var0;
      } else {
         var0 = var0.replaceAll("&", "&amp;");
         var0 = var0.replaceAll("<", "&lt;");
         var0 = var0.replaceAll("\r", "&#13;");
         var0 = var0.replaceAll(">", "&gt;");
         var0 = var0.replaceAll("\"", "&quot;");
         var0 = var0.replaceAll("'", "&apos;");
         return var0;
      }
   }

   public static String htmlConvert_kivonatolt(String var0) {
      if (var0 == null) {
         return var0;
      } else {
         var0 = var0.replaceAll("&", "&amp;");
         var0 = var0.replaceAll("<", "&lt;");
         var0 = var0.replaceAll("\r", "&#13;");
         var0 = var0.replaceAll(">", "&gt;");
         var0 = var0.replaceAll("\"", "&quot;");
         return var0;
      }
   }

   public static String htmlCut(String var0) {
      var0 = var0.replaceAll("&", "_");
      var0 = var0.replaceAll("<", "_");
      var0 = var0.replaceAll("\r", "_");
      var0 = var0.replaceAll(">", "_");
      var0 = var0.replaceAll("\"", "_");
      var0 = var0.replaceAll("'", "_");
      return var0;
   }

   public static String plainConvert(String var0) {
      var0 = var0.replaceAll("&amp;", "&");
      var0 = var0.replaceAll("&lt;", "<");
      var0 = var0.replaceAll("&#13;", "\r\n");
      var0 = var0.replaceAll("&gt;", ">");
      var0 = var0.replaceAll("&quot;", "\"");
      var0 = var0.replaceAll("&apos;", "'");
      return var0;
   }
}
