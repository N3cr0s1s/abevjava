package hu.piller.enykp.alogic.masterdata.repository.xml;

import hu.piller.enykp.alogic.masterdata.core.BlockDefinition;
import hu.piller.enykp.alogic.masterdata.core.MasterDataDefinition;
import hu.piller.enykp.alogic.masterdata.core.validator.DummyValidator;
import hu.piller.enykp.alogic.masterdata.core.validator.EntityValidator;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryMeta;
import hu.piller.enykp.util.base.PropertyList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class MDRepositoryMetaImpl implements MDRepositoryMeta {
   private Hashtable<String, BlockDefinition[]> _entity = new Hashtable();
   private Hashtable<String, MasterDataDefinition> _masterData = new Hashtable();
   private Hashtable<String, String> _validatorClassNames = new Hashtable();
   private String uriMasterDataDefinitionSchema;
   private String[] orgNames;
   private String uriEntityDefinitionSchema;
   private InputStream entityDefinition;

   public MDRepositoryMetaImpl(String var1, String[] var2, String var3, InputStream var4) {
      this.orgNames = var2 == null ? new String[0] : var2;
      this.uriMasterDataDefinitionSchema = var1;
      this.entityDefinition = var4;
      this.uriEntityDefinitionSchema = var3;
      if (this.orgNames != null) {
         this.fillMasterData();
         this.fillEntity();
         this.checkEntities();
      }

   }

   public BlockDefinition[] getBlockDefinitionsForEntity(String var1) {
      if (this._entity.containsKey(var1)) {
         return (BlockDefinition[])this._entity.get(var1);
      } else {
         System.err.println("MDRepositoryImpl.getBlockDefinitionsForEntity(): '" + var1 + "' definició nem található");
         return null;
      }
   }

   public String getOrgForMasterData(String var1) {
      if (this._masterData.containsKey(var1)) {
         return ((MasterDataDefinition)this._masterData.get(var1)).getOrg();
      } else {
         String var2 = null;
         if (PropertyList.getInstance().get("prop.const.mddebug") != null) {
            var2 = (String)((Vector)PropertyList.getInstance().get("prop.const.mddebug")).get(0);
         }

         if ("true".equals(var2)) {
            System.err.println("MDRepositoryImpl.getOrgForMasterData(): '" + var1 + "' -t definiáló szervezet nem található");
         }

         return null;
      }
   }

   public String getTypeOfMasterData(String var1) {
      return this._masterData.containsKey(var1) ? ((MasterDataDefinition)this._masterData.get(var1)).getType() : null;
   }

   public String[] getEntityTypes() {
      return (String[])this._entity.keySet().toArray(new String[this._entity.size()]);
   }

   public String[] getMasterDataCommonForEntities(String[] var1) {
      Vector var2 = new Vector();
      Iterator var3 = this._masterData.values().iterator();

      while(var3.hasNext()) {
         MasterDataDefinition var4 = (MasterDataDefinition)var3.next();
         boolean[] var5 = new boolean[var1.length];
         int var6 = 0;
         String[] var7 = var1;
         int var8 = var1.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String var10 = var7[var9];
            var5[var6++] = this.hasEntityMasterData(var10, var4.getKey());
         }

         if (this.testMatcherAND(var5)) {
            var2.add(var4.getKey());
         }
      }

      return (String[])var2.toArray(new String[var2.size()]);
   }

   public EntityValidator getValidatorForEntity(String var1) {
      Object var2 = new DummyValidator();
      if (this._entity.containsKey(var1)) {
         String var3 = (String)this._validatorClassNames.get(var1);

         try {
            var2 = (EntityValidator)Class.forName(var3).newInstance();
         } catch (Exception var5) {
            this.writeError(var5.getMessage() == null ? "HIBA '" + var1 + "': Validátor nem példányosítható" : var5.getMessage());
         }
      }

      return (EntityValidator)var2;
   }

   public String[] getMasterDataForEntityTypeBlockType(String var1, String var2) {
      Iterator var3 = this._entity.keySet().iterator();

      while(true) {
         String var4;
         do {
            if (!var3.hasNext()) {
               return new String[0];
            }

            var4 = (String)var3.next();
         } while(!var4.equals(var1));

         BlockDefinition[] var5 = (BlockDefinition[])this._entity.get(var1);
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            BlockDefinition var8 = var5[var7];
            if (var8.getBlockName().equals(var2)) {
               return var8.getMasterDataNames();
            }
         }
      }
   }

   private boolean testMatcherAND(boolean[] var1) {
      boolean var2 = true;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (!var1[var3]) {
            var2 = false;
            break;
         }
      }

      return var2;
   }

   private boolean hasEntityMasterData(String var1, String var2) {
      BlockDefinition[] var3 = (BlockDefinition[])this._entity.get(var1);
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BlockDefinition var6 = var3[var5];
         String[] var7 = var6.getMasterDataNames();
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String var10 = var7[var9];
            if (var2.equals(var10)) {
               return true;
            }
         }
      }

      return false;
   }

   private void checkEntities() {
      StringBuffer var1 = new StringBuffer("");
      Iterator var2 = this._entity.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         boolean var4 = true;
         BlockDefinition[] var5 = (BlockDefinition[])this._entity.get(var3);
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            BlockDefinition var8 = var5[var7];
            String var9 = null;
            if (PropertyList.getInstance().get("prop.const.mddebug") != null) {
               var9 = (String)((Vector)PropertyList.getInstance().get("prop.const.mddebug")).get(0);
            }

            String var10 = this.checkBlockDefinition(var8);
            if ("true".equals(var9) && !"".equals(var10)) {
               if (var4) {
                  var4 = false;
                  var1.append("A(z) '");
                  var1.append(var3);
                  var1.append("' egyedtípus definíció\n");
               }

               var1.append("'");
               var1.append(var8.getBlockName());
               var1.append("' blokkjában érvénytelen törzsadat hívatkozások vannak: ");
               var1.append(var10);
               var1.append("\n");
            }
         }
      }

      String var11 = var1.toString();
      if (!"".equals(var11)) {
         this.writeError(var11);
      }

   }

   private void fillEntity() {
      XMLReader var1 = this.initReader(this.uriEntityDefinitionSchema);
      EntityDefinitionHandler var2 = new EntityDefinitionHandler();
      var1.setContentHandler(var2);

      try {
         if (var1 == null) {
            return;
         }

         var1.parse(new InputSource(this.entityDefinition));
         this._entity = var2.getDefinitions();
         this._validatorClassNames = var2.getValidatorClasses();
      } catch (FileNotFoundException var4) {
         this.writeError(var4.getMessage());
      } catch (IOException var5) {
         this.writeError(var5.getMessage());
      } catch (SAXException var6) {
         this.writeError(var6.getMessage());
      }

   }

   private String checkBlockDefinition(BlockDefinition var1) {
      StringBuffer var2 = new StringBuffer("");
      String[] var3 = var1.getMasterDataNames();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         if (!this._masterData.containsKey(var6)) {
            var2.append("'");
            var2.append(var6);
            var2.append("' ");
         }
      }

      return var2.toString();
   }

   private void fillMasterData() {
      XMLReader var1 = this.initReader(this.uriMasterDataDefinitionSchema);
      MasterDataDefinitionHandler var2 = new MasterDataDefinitionHandler();
      var2.setOrgNames(this.orgNames);
      var1.setContentHandler(var2);

      try {
         var1.parse(new InputSource(ClassLoader.getSystemClassLoader().getResourceAsStream("resources/masterdata/masterdata.xml")));
         this._masterData.putAll(var2.getMasterDataDefinitions());
      } catch (Exception var4) {
         this.writeError(var4.getMessage());
      }

   }

   private XMLReader initReader(String var1) {
      SAXParserFactory var2 = SAXParserFactory.newInstance();
      var2.setNamespaceAware(true);
      var2.setValidating(false);

      try {
         SchemaFactory var3 = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
         var2.setSchema(var3.newSchema(new Source[]{new StreamSource(new File(var1))}));
         return var2.newSAXParser().getXMLReader();
      } catch (SAXException var4) {
         this.writeError(var4.getMessage());
         return null;
      } catch (ParserConfigurationException var5) {
         this.writeError(var5.getMessage());
         return null;
      }
   }

   private void writeError(String var1) {
      System.err.println(var1);
   }
}
