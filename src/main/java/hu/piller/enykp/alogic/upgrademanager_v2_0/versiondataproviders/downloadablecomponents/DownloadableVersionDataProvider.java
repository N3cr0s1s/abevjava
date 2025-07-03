package hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.downloadablecomponents;

import hu.piller.enykp.alogic.upgrademanager_v2_0.Directories;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeBusinessException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeLogger;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeTechnicalException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.VersionDataProvider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import me.necrocore.abevjava.NecroFile;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class DownloadableVersionDataProvider extends VersionDataProvider {
   private static final int MAX_WAITTIME = 30;
   private URL url = null;

   public DownloadableVersionDataProvider() {
      super("DOWNLOADABLE", "(nincs adat)");
   }

   public DownloadableVersionDataProvider(URL var1) {
      super("DOWNLOADABLE", "(nincs adat)");
      this.url = var1;
   }

   public void setURL(URL var1) {
      this.url = var1;
      if (this.getCollection() != null) {
         this.getCollection().removeAllElements();
      }

   }

   public URL getURL() {
      return this.url;
   }

   public void collect() throws UpgradeBusinessException, UpgradeTechnicalException {
      String var1 = this.downloadXml();
      Vector var2 = this.processXml(var1);
      this.updateVersionDataWithCategory(var2);
      this.setCollection(var2);
   }

   private String downloadXml() throws UpgradeTechnicalException {
      Object var1 = new Object();
      String[] var2 = new String[]{null};
      Exception[] var3 = new Exception[]{null};
      Thread var4 = new Thread(() -> {
         boolean var25 = false;

         label241: {
            try {
               var25 = true;
               InputStream cVar4 = this.openHttpConnectionToUpgradeURL().getInputStream();
               Throwable var5 = null;

               try {
                  ByteArrayOutputStream var6 = new ByteArrayOutputStream();
                  byte[] var7 = new byte[1024];
                  boolean var8 = false;

                  int var43;
                  while((var43 = cVar4.read(var7, 0, var7.length)) != -1) {
                     var6.write(var7, 0, var43);
                  }

                  var6.flush();
                  var2[0] = new String(var6.toByteArray(), "UTF-8");
               } catch (Throwable var39) {
                  var5 = var39;
                  throw var39;
               } finally {
                  if (cVar4 != null) {
                     if (var5 != null) {
                        try {
                           cVar4.close();
                        } catch (Throwable var37) {
                           var5.addSuppressed(var37);
                        }
                     } else {
                        cVar4.close();
                     }
                  }

               }

               var25 = false;
               break label241;
            } catch (IOException var41) {
               var3[0] = var41;
               var25 = false;
            } finally {
               if (var25) {
                  synchronized(var1) {
                     var1.notifyAll();
                  }
               }
            }

            synchronized(var1) {
               var1.notifyAll();
               return;
            }
         }

         synchronized(var1) {
            var1.notifyAll();
         }

      });
      var4.start();

      while(var2[0] == null && var3[0] == null) {
         synchronized(var1) {
            try {
               var1.wait();
            } catch (InterruptedException var8) {
               UpgradeLogger.getInstance().log(Thread.currentThread().getName() + " *** UpgradeManager : !! XML letöltésre várakozás megszakítva " + Thread.currentThread().isInterrupted() + " " + this.getURL());
               throw new UpgradeTechnicalException("A letöltő folyamat megszakítva, a letöltés alatt álló frissítésleíró információk elvesznek.", var8);
            }
         }
      }

      if (var2[0] != null) {
         return var2[0];
      } else {
         throw new UpgradeTechnicalException("Hálózati hiba, vagy frissítés leíró file nem található ezen az URL-en: " + this.url.getProtocol() + "://" + this.url.getHost() + this.url.getPath(), var3[0]);
      }
   }

   private URLConnection openHttpConnectionToUpgradeURL() throws IOException {
      URLConnection var1 = this.getURL().openConnection();
      var1.setConnectTimeout(30000);
      var1.setReadTimeout(30000);
      var1.setAllowUserInteraction(false);
      if (var1 instanceof HttpURLConnection) {
         ((HttpURLConnection)var1).setInstanceFollowRedirects(true);
         ((HttpURLConnection)var1).setRequestMethod("GET");
      }

      var1.connect();
      return var1;
   }

   private Vector processXml(String var1) throws UpgradeTechnicalException {
      String var2 = "http://www.w3.org/2001/XMLSchema";

      try {
         SAXParserFactory var3 = SAXParserFactory.newInstance();
         var3.setValidating(false);
         var3.setNamespaceAware(true);
         SchemaFactory var4 = SchemaFactory.newInstance(var2);
         var3.setSchema(var4.newSchema(new Source[]{new StreamSource(new NecroFile(Directories.getSchemasPath() + "/enyk.xsd"))}));
         SAXParser var5 = var3.newSAXParser();
         XMLReader var6 = var5.getXMLReader();
         XmlHandler var7 = new XmlHandler();
         var6.setContentHandler(var7);
         var6.setErrorHandler(var7);
         var6.parse(new InputSource(new ByteArrayInputStream(var1.getBytes("UTF-8"))));
         return var7.getComponents();
      } catch (SAXException var8) {
         throw new UpgradeTechnicalException("Érvénytelen frissítés leíró XML ezen az URL-en: " + this.url.getProtocol() + "://" + this.url.getHost() + this.url.getPath(), var8);
      } catch (ParserConfigurationException var9) {
         throw new UpgradeTechnicalException("Frissítés leíró elemző hibája a(z) " + this.url.getProtocol() + "://" + this.url.getHost() + this.url.getPath() + " címről letöltött leíró XML feldolgozása közben", var9);
      } catch (Exception var10) {
         throw new UpgradeTechnicalException("Hibás vagy nem feldolgozható frissítés leíró file ezen az URL-en: " + this.url.getProtocol() + "://" + this.url.getHost() + this.url.getPath(), var10);
      }
   }

   private void updateVersionDataWithCategory(Vector var1) {
      int var2 = var1.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         ((VersionData)var1.get(var3)).setSourceCategory("DOWNLOADABLE");
      }

   }
}
