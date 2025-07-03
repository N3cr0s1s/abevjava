package hu.piller.enykp.alogic.masterdata.sync.logic.transformation;

import hu.piller.enykp.error.EnykpBusinessException;
import hu.piller.enykp.error.EnykpTechnicalException;
import hu.piller.enykp.util.content.ContentUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class NavToAnykStructureConverter {
   private static final String PATH_TRANSFORMATION_RULES = "resources/masterdata/";
   private static NavToAnykStructureConverter instance;

   private NavToAnykStructureConverter() {
   }

   public static NavToAnykStructureConverter getInstance() {
      if (instance == null) {
         instance = new NavToAnykStructureConverter();
      }

      return instance;
   }

   public String convert(String var1) throws EnykpBusinessException, EnykpTechnicalException {
      String var2 = "";
      DocumentBuilderFactory var3 = DocumentBuilderFactory.newInstance();

      try {
         String var4 = this.getEntityType(var1);
         String var5 = this.getXmlEncoding(var1);
         ByteArrayInputStream var6 = new ByteArrayInputStream(var1.getBytes(var5));
         ByteArrayOutputStream var7 = new ByteArrayOutputStream();
         DocumentBuilder var8 = var3.newDocumentBuilder();
         Document var9 = var8.parse(var6, var5);
         StreamSource var10 = new StreamSource(this.getXslAsInputStream("resources/masterdata/" + this.getXslNameForEntity(var4)));
         TransformerFactory var11 = TransformerFactory.newInstance();
         var11.setURIResolver(new NavToAnykStructureConverter.MdSyncXslUriResolver());
         Transformer var12 = var11.newTransformer(var10);
         var12.setOutputProperty("method", "xml");
         var12.setOutputProperty("encoding", var5);
         var12.setOutputProperty("indent", "yes");
         StreamResult var13 = new StreamResult(var7);
         var12.transform(new DOMSource(var9), var13);
         var2 = var7.toString(var5);
         return var2;
      } catch (ParserConfigurationException var14) {
         throw new EnykpTechnicalException(var14.getMessage());
      } catch (SAXException var15) {
         throw new EnykpTechnicalException(var15.getMessage());
      } catch (IOException var16) {
         throw new EnykpTechnicalException(var16.getMessage());
      } catch (TransformerConfigurationException var17) {
         throw new EnykpTechnicalException(var17.getMessage());
      } catch (TransformerException var18) {
         throw new EnykpBusinessException(var18.getMessage());
      }
   }

   public String getXmlEncoding(String var1) throws EnykpBusinessException {
      Matcher var2 = Pattern.compile("<\\?xml.*encoding.*\\?>", 32).matcher(var1);

      String var3;
      int var4;
      int var5;
      for(var3 = ""; var2.find(); var3 = var2.group().substring(var4, var5)) {
         var4 = var2.group().indexOf("encoding");
         var4 += "encoding=\"".length();

         for(var5 = var4; var2.group().charAt(var5) != '"' && var5 < var2.group().length(); ++var5) {
         }
      }

      if ("".equals(var3)) {
         var3 = "utf-8";
      }

      return var3;
   }

   public String getEntityType(String var1) throws EnykpBusinessException {
      Matcher var2 = Pattern.compile("<type>.*</type>", 32).matcher(var1);
      String var3 = "";

      while(var2.find()) {
         int var4 = var2.group().indexOf(">");

         int var5;
         for(var5 = var4; var2.group().charAt(var5) != '<' && var5 < var2.group().length(); ++var5) {
         }

         var3 = var2.group().substring(var4 + 1, var5);
         if (var3.startsWith("NAV_")) {
            var3 = var3.replace("NAV_", "");
         }
      }

      if ("".equals(var3)) {
         throw new EnykpBusinessException("A adózó típusa nem állapítható meg!");
      } else {
         return var3;
      }
   }

   public String getXslNameForEntity(String var1) throws EnykpBusinessException {
      if ("Magánszemély".equals(var1)) {
         return "person.xsl";
      } else if ("Egyéni vállalkozó".equals(var1)) {
         return "selfemployed.xsl";
      } else if ("Társaság".equals(var1)) {
         return "corporation.xsl";
      } else {
         throw new EnykpBusinessException("Nem támogatott típus: " + var1);
      }
   }

   public InputStream getXslAsInputStream(String var1) {
      return ClassLoader.getSystemClassLoader().getResourceAsStream(var1);
   }

   class MdSyncXslUriResolver implements URIResolver {
      private final String SYNC_XSL_ENCODING = "UTF-8";

      public Source resolve(String var1, String var2) throws TransformerException {
         try {
            String var3 = ContentUtil.readTextFromStream(this.getClass().getClassLoader().getResourceAsStream(var1), "UTF-8");
            return new StreamSource(new ByteArrayInputStream(var3.getBytes("UTF-8")), var1);
         } catch (Exception var4) {
            throw new TransformerConfigurationException(var4.getMessage());
         }
      }
   }
}
