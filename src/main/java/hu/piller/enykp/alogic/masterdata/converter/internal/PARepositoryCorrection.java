package hu.piller.enykp.alogic.masterdata.converter.internal;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityException;
import hu.piller.enykp.alogic.masterdata.core.EntityHome;
import hu.piller.enykp.alogic.masterdata.core.MasterData;
import hu.piller.enykp.util.base.ErrorList;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class PARepositoryCorrection {
   private Vector<PAConversionTask> tasks = new Vector();
   private Vector<Entity> entitiesToSave;
   private PAHandler handler;
   private SAXParser parser;
   private PAtoMDMapper mapper;
   private Set<Entity> invalid;
   private String[][] taskDefs = new String[][]{{"Társaság", "COMPANY", "pa_companies.xml.converted"}, {"Magánszemély", "PERSON", "pa_people.xml.converted"}, {"Egyéni vállalkozó", "SMALLBUSINESS", "pa_smallbusinesses.xml.converted"}, {"Adótanácsadó", "TAXEXPERT", "pa_taxexperts.xml"}};
   private boolean has_fatal_error;

   public PARepositoryCorrection() {
      String[][] var1 = this.taskDefs;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String[] var4 = var1[var3];
         this.tasks.add(new PAConversionTask(var4[0], var4[1], var4[2]));
      }

      this.entitiesToSave = new Vector();
      this.invalid = new HashSet();
   }

   public void convert() {
      Iterator var1 = this.tasks.iterator();

      while(true) {
         PAConversionTask var2;
         do {
            if (!var1.hasNext()) {
               return;
            }

            var2 = (PAConversionTask)var1.next();
         } while(!var2.isTaskExecutable());

         try {
            EntityHome.getInstance().startSession();
            this.initParser();
            this.initHandlerForTask(var2);
            this.initMapper(var2);
            this.parser.parse(new FileInputStream(var2.getFile()), this.handler);
            this.entitiesToSave.clear();
            MasterData var12 = this.getFinderByType(var2.getType());
            String var4 = this.getPAKeyByType(var2.getType());
            if (var4 != null && var12 != null) {
               Iterator var5 = this.handler.getElements().keySet().iterator();

               while(var5.hasNext()) {
                  String var6 = (String)var5.next();
                  Hashtable var7 = (Hashtable)this.handler.getElements().get(var6);
                  if (var7.containsKey(var4)) {
                     var12.setValue(this.formatValue(var4, (String)var7.get(var4)));
                     Entity var8 = this.lookUpEntity(var2.getEntityType(), var12);
                     if (var8 != null && this.correction(var8, var7)) {
                        this.entitiesToSave.add(var8);
                     }
                  }
               }

               if (this.entitiesToSave.size() > 0) {
                  var5 = this.entitiesToSave.iterator();

                  while(var5.hasNext()) {
                     Entity var13 = (Entity)var5.next();

                     try {
                        EntityHome.getInstance().update(var13);
                     } catch (EntityException var10) {
                        var10.printStackTrace();
                        this.invalid.add(var13);
                     }
                  }
               }

               EntityHome.getInstance().closeSession();
            }
         } catch (Exception var11) {
            Exception var3 = var11;

            try {
               this.has_fatal_error = true;
               EntityHome.getInstance().abortSession();
               ErrorList.getInstance().writeError(ErrorList.LEVEL_SHOW_FATAL_ERROR, var3.getMessage(), var3, (Object)null);
            } catch (EntityException var9) {
               var11.printStackTrace();
               System.err.println("\nA módosítások visszavonása sikeretelen!");
               var9.printStackTrace();
               return;
            }
         }
      }
   }

   public boolean hasFatalError() {
      return this.has_fatal_error;
   }

   public Set<Entity> getInvalid() {
      return this.invalid;
   }

   private String formatValue(String var1, String var2) {
      String var3 = var2;
      if ("tax_number".equals(var1)) {
         var3 = var2.substring(0, 8) + "-" + var2.substring(8, 9) + "-" + var2.substring(9);
      }

      return var3;
   }

   public Entity lookUpEntity(String var1, MasterData var2) {
      try {
         Entity[] var3 = EntityHome.getInstance().findByTypeAndMasterData(var1, new MasterData[]{var2});
         if (var3.length != 1) {
            throw new EntityException("Nem található törzsadat (" + var1 + ") kulcs: " + var2.getKey() + "=" + var2.getValue());
         } else {
            return var3[0];
         }
      } catch (EntityException var4) {
         ErrorList.getInstance().writeError(ErrorList.LEVEL_SHOW_FATAL_ERROR, "'" + var1 + "' : " + var4.getMessage(), (Exception)null, (Object)null);
         return null;
      }
   }

   public void initMapper(PAConversionTask var1) throws Exception {
      this.mapper = PAtoMDMapperFactory.getInstance().getMapper(var1);
      if (this.mapper == null) {
         throw new Exception("Nem található adattranszformációs kód a következő típushoz: " + var1.getEntityType());
      }
   }

   public void initHandlerForTask(PAConversionTask var1) {
      if (this.handler == null) {
         this.handler = new PAHandler();
      }

      this.handler.setType(var1.getType());
   }

   public void initParser() throws Exception {
      if (this.parser == null) {
         SAXParserFactory var1 = SAXParserFactory.newInstance();
         var1.setValidating(false);
         var1.setNamespaceAware(true);
         this.parser = var1.newSAXParser();
      } else {
         this.parser.reset();
      }

   }

   public boolean hasPAtoMDConversionTasks() {
      return this.tasks.size() > 0;
   }

   private MasterData getFinderByType(String var1) {
      if ("COMPANY".equals(var1)) {
         return new MasterData("Adózó adószáma");
      } else if ("PERSON".equals(var1)) {
         return new MasterData("Adózó adóazonosító jele");
      } else {
         return "SMALLBUSINESS".equals(var1) ? new MasterData("Adózó adószáma") : null;
      }
   }

   private String getPAKeyByType(String var1) {
      if ("COMPANY".equals(var1)) {
         return "tax_number";
      } else if ("PERSON".equals(var1)) {
         return "tax_id";
      } else {
         return "SMALLBUSINESS".equals(var1) ? "tax_number" : null;
      }
   }

   private boolean correction(Entity var1, Hashtable<String, String> var2) {
      boolean var3 = false;
      String var4 = var1.getBlock("Egyéb adatok").getMasterData("Pénzintézet neve").getValue();
      if (var4 == null) {
         var4 = "";
      }

      String var5 = (String)var2.get("financial_corp");
      if (var4.trim().equals("") && var5 != null && !var5.trim().equals("")) {
         var1.getBlock("Egyéb adatok").getMasterData("Pénzintézet neve").setValue(var5);
         var3 = true;
      }

      var4 = var1.getBlock("Egyéb adatok").getMasterData("Számla-azonosító").getValue();
      if (var4.length() == 8) {
         var4 = var4 + "-";
         var3 = true;
      } else if (var4.length() == 16) {
         var4 = var4.substring(0, 8) + "-" + var4.substring(8);
         var3 = true;
      }

      var1.getBlock("Egyéb adatok").getMasterData("Számla-azonosító").setValue(var4);
      return var3;
   }
}
