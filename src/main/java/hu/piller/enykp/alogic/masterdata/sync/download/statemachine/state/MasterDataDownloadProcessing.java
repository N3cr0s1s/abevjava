package hu.piller.enykp.alogic.masterdata.sync.download.statemachine.state;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.repository.xml.EntityHandler;
import hu.piller.enykp.alogic.masterdata.sync.download.IMasterDataDownload;
import hu.piller.enykp.alogic.masterdata.sync.download.MasterDataDownloadException;
import hu.piller.enykp.alogic.masterdata.sync.download.MasterDataDownloadResultStatus;
import hu.piller.enykp.alogic.masterdata.sync.download.statemachine.IStateMachine;
import hu.piller.enykp.alogic.masterdata.sync.download.statemachine.State;
import hu.piller.enykp.alogic.masterdata.sync.syncdir.SyncDirException;
import hu.piller.enykp.alogic.masterdata.sync.syncdir.SyncDirHandler;
import hu.piller.enykp.util.base.PropertyList;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class MasterDataDownloadProcessing implements IMasterDataDownload {
   private IStateMachine stateMachine;

   public MasterDataDownloadProcessing(IStateMachine var1) {
      this.stateMachine = var1;
   }

   public void sendMasterDataDownloadRequest(String[] var1) throws MasterDataDownloadException {
      throw new MasterDataDownloadException("Kérelem feldolgozás még nincsen kész, új kérelem nem adható be!");
   }

   public Entity getDownloadedEntity(String var1) throws MasterDataDownloadException {
      try {
         String var2 = SyncDirHandler.getResultFileContent(var1);
         SAXParserFactory var3 = SAXParserFactory.newInstance();
         var3.setNamespaceAware(true);
         var3.setValidating(false);
         SchemaFactory var4 = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
         var3.setSchema(var4.newSchema(new Source[]{new StreamSource(this.getRepositoryXsd())}));
         SAXParser var5 = var3.newSAXParser();
         XMLReader var6 = var5.getXMLReader();
         EntityHandler var7 = new EntityHandler();
         var6.setContentHandler(var7);
         var6.parse(new InputSource(new StringReader(var2)));
         if (var7.getEntities().size() == 1) {
            Entity var8 = (Entity)var7.getEntities().values().iterator().next();
            this.processEntityForVPEngedelyszam(var8);
            return var8;
         } else {
            throw new MasterDataDownloadException("NAV forrású adat: " + var7.getErrorMsg());
         }
      } catch (ParserConfigurationException var9) {
         throw new MasterDataDownloadException("NAV forrású adat: " + var9.getMessage());
      } catch (SAXException var10) {
         throw new MasterDataDownloadException("NAV forrású adat: " + var10.getMessage());
      } catch (SyncDirException var11) {
         throw new MasterDataDownloadException("NAV forrású adat: " + var11.getMessage());
      } catch (IOException var12) {
         throw new MasterDataDownloadException("NAV forrású adat: " + var12.getMessage());
      }
   }

   public Map<String, MasterDataDownloadResultStatus> getResultInfo() throws MasterDataDownloadException {
      TreeMap var1 = new TreeMap();

      try {
         Set var2 = SyncDirHandler.getErrIds();
         Set var3 = SyncDirHandler.getSuccessIds();
         Iterator var4 = var2.iterator();

         String var5;
         while(var4.hasNext()) {
            var5 = (String)var4.next();
            if (var3.contains(var5)) {
               throw new MasterDataDownloadException("A(z) " + var5 + " azonosító a hiba és az eredmény listában is szerepel!");
            }

            var1.put(var5, MasterDataDownloadResultStatus.ERROR);
         }

         var4 = var3.iterator();

         while(var4.hasNext()) {
            var5 = (String)var4.next();
            if (var2.contains(var5)) {
               throw new MasterDataDownloadException("A(z) " + var5 + " azonosító a hiba és az eredmény listában is szerepel!");
            }

            var1.put(var5, MasterDataDownloadResultStatus.SUCCESS);
         }

         return var1;
      } catch (SyncDirException var6) {
         throw new MasterDataDownloadException(var6.getMessage());
      }
   }

   public void done() throws MasterDataDownloadException {
      try {
         SyncDirHandler.archive();
         this.stateMachine.setState(State.READY);
      } catch (SyncDirException var2) {
         throw new MasterDataDownloadException(var2.getMessage());
      }
   }

   private File getRepositoryXsd() {
      String var1 = (String)String.class.cast(PropertyList.getInstance().get("prop.sys.root")) + File.separator + "xsd" + File.separator + "Repository.xsd";
      return new File(var1);
   }

   private void processEntityForVPEngedelyszam(Entity var1) {
      List var2 = var1.getBlock("VPOP törzsadatok").getMasterData("Engedélyszám").getValues();
      ArrayList var3 = new ArrayList();
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         if (!"".equals(var5) && !var5.startsWith("HU")) {
            var3.add(var5);
         }
      }

      if (var3.size() > 0) {
         var2.removeAll(var3);
      }

   }
}
