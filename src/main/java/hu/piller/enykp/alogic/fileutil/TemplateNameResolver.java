package hu.piller.enykp.alogic.fileutil;

import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.Properties;
import java.util.Vector;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TemplateNameResolver {
   private static Properties names = new Properties();
   private static TemplateNameResolver ourInstance = new TemplateNameResolver();

   public static TemplateNameResolver getInstance() {
      return ourInstance;
   }

   public Properties getAllName() {
      return names;
   }

   public Vector getName(String var1) {
      return names.containsKey(var1) ? (Vector)names.get(var1) : null;
   }

   private TemplateNameResolver() {
      this.createTeminfoList();
   }

   private void parseResolvers(final File var1) throws Exception {
      FileInputStream var2 = new FileInputStream(var1);
      final String[] var3 = new String[]{""};
      final int[] var4 = new int[]{0};
      DefaultHandler var5 = new DefaultHandler() {
         public void startElement(String var1x, String var2, String var3x, Attributes var4x) throws SAXException {
            if (var3x.equalsIgnoreCase("template")) {
               if (!var3[0].equals(var1.getAbsolutePath())) {
                  var3[0] = var1.getAbsolutePath();
                  var4[0] = 1;
               }

               Vector var5;
               if (TemplateNameResolver.names.containsKey(var4x.getValue("formid"))) {
                  var5 = (Vector)TemplateNameResolver.names.get(var4x.getValue("formid"));
               } else {
                  var5 = new Vector();
               }

               var5.add(var4x.getValue("filename"));
               TemplateNameResolver.names.put(var4x.getValue("formid"), var5);
               if (var4x.getValue("mainid") == null && var4[0] < 2) {
                  return;
               }

               Vector var6;
               if (var4x.getValue("mainid") != null) {
                  try {
                     if (TemplateNameResolver.names.containsKey("mainid")) {
                        var6 = (Vector)TemplateNameResolver.names.get("mainid");
                     } else {
                        var6 = new Vector();
                     }

                     if (var4x.getValue("mainid") != null) {
                        if (var4[0] == 1) {
                           var4[0] = 2;
                        }

                        var6.add(var4x.getValue("mainid"));
                        TemplateNameResolver.names.put("mainid", var6);
                        return;
                     }
                  } catch (Exception var8) {
                     Tools.eLog(var8, 0);
                  }
               } else {
                  if (var4[0] < 2) {
                     return;
                  }

                  try {
                     if (TemplateNameResolver.names.containsKey("subid")) {
                        var6 = (Vector)TemplateNameResolver.names.get("subid");
                     } else {
                        var6 = new Vector();
                     }

                     var6.add(var4x.getValue("formid"));
                     TemplateNameResolver.names.put("subid", var6);
                  } catch (Exception var7) {
                     Tools.eLog(var7, 0);
                  }
               }
            }

         }
      };
      SAXParserFactory var6 = SAXParserFactory.newInstance();
      SAXParser var7 = var6.newSAXParser();
      InputSource var8 = new InputSource(var2);
      var8.setEncoding("ISO-8859-2");
      var7.parse(var8, var5);
   }

   public void createTeminfoList() {
      try {
         IPropertyList var1 = PropertyList.getInstance();
         names.clear();
         String var2 = Tools.fillPath((String)var1.get("prop.sys.root"));
         String var3 = (String)var1.get("prop.sys.abev");
         if (var3 != null) {
            var2 = Tools.fillPath(var2 + var3);
         }

         var2 = var2 + "nyomtatvanyinfo" + File.separator;
         File var4 = new File(var2);
         TemplateNameResolver.TeminfoFilenameFilter var5 = new TemplateNameResolver.TeminfoFilenameFilter();
         String[] var6 = var4.list(var5);

         for(int var7 = 0; var7 < var6.length; ++var7) {
            try {
               this.parseResolvers(new File(var2 + var6[var7]));
            } catch (Exception var9) {
               Tools.eLog(var9, 0);
            }
         }
      } catch (Exception var10) {
         Tools.eLog(var10, 0);
      }

   }

   static class TeminfoFilenameFilter implements FilenameFilter {
      public TeminfoFilenameFilter() {
      }

      public boolean accept(File var1, String var2) {
         if ((new File(var1, var2)).isDirectory()) {
            return false;
         } else {
            try {
               return var2.endsWith(".teminfo.enyk");
            } catch (Exception var4) {
               return false;
            }
         }
      }
   }
}
