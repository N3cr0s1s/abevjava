package hu.piller.enykp.alogic.masterdata.converter.internal;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityException;
import hu.piller.enykp.alogic.masterdata.core.EntityHome;
import hu.piller.enykp.util.base.ErrorList;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class PAConverter {
   private Vector<PAConversionTask> tasks = new Vector();
   private Vector<Entity> entitiesToSave;
   private PAHandler handler;
   private SAXParser parser;
   private PAtoMDMapper mapper;
   private Set<Entity> invalid;
   private String[][] taskDefs = new String[][]{{"Társaság", "COMPANY", "pa_companies.xml"}, {"Magánszemély", "PERSON", "pa_people.xml"}, {"Egyéni vállalkozó", "SMALLBUSINESS", "pa_smallbusinesses.xml"}, {"Adótanácsadó", "TAXEXPERT", "pa_taxexperts.xml"}};
   private boolean has_fatal_error;

   public PAConverter() {
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

      label107:
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
            Iterator var15 = this.handler.getElements().keySet().iterator();

            while(true) {
               String var4;
               Entity var5;
               do {
                  if (!var15.hasNext()) {
                     boolean var16 = false;
                     if (this.entitiesToSave.size() > 0) {
                        Iterator var17 = this.entitiesToSave.iterator();

                        while(var17.hasNext()) {
                           var5 = (Entity)var17.next();

                           try {
                              EntityHome.getInstance().update(var5);
                           } catch (EntityException var13) {
                              this.invalid.add(var5);
                           }
                        }

                        var16 = true;
                     }

                     if (this.handler.getElements().size() == 0 || var16) {
                        var2.archiveFile();
                     }

                     EntityHome.getInstance().closeSession();
                     continue label107;
                  }

                  var4 = (String)var15.next();
                  var5 = this.createEntityForType(var2.getEntityType());
               } while(var5 == null);

               Hashtable var6 = (Hashtable)this.handler.getElements().get(var4);
               StringBuffer var7 = new StringBuffer("");
               Iterator var8 = var6.keySet().iterator();

               while(var8.hasNext()) {
                  String var9 = (String)var8.next();
                  PAtoMDMapper.Mapping var10 = this.mapper.getMappingForPAAttrib(var9);
                  if (var10.isMapped()) {
                     String var11 = (String)var6.get(var9);
                     if ("tax_number".equals(var9) && var11.indexOf("-") == -1) {
                        var11 = var11.substring(0, 8) + "-" + var11.substring(8, 9) + "-" + var11.substring(9);
                     }

                     if ("account_id".equals(var9) && var11 != null) {
                        if (var11.length() == 8) {
                           var11 = var11 + "-";
                        } else if (var11.length() == 16) {
                           var11 = var11.substring(0, 8) + "-" + var11.substring(8);
                        }
                     }

                     if (!"N/A".equals(var10.getBlockName())) {
                        var5.getBlock(var10.getBlockName(), 1).getMasterData(var10.getMdKey()).setValue(var11);
                     }
                  } else {
                     var7.append(var9);
                     var7.append(" = ");
                     var7.append((String)var6.get(var9));
                     var7.append("\n");
                  }
               }

               if (!"".equals(var7.toString())) {
                  StringBuilder var18 = new StringBuilder();
                  var18.append("Nem leképzett adat a(z) ");
                  var18.append(var4);
                  var18.append(" azonosítójú '");
                  var18.append(var5.getName());
                  var18.append("' törzsadatban: (");
                  var18.append("fájl ");
                  var18.append(var2.getFile());
                  var18.append("),\n");
                  var18.append(var7);
                  ErrorList.getInstance().store(ErrorList.LEVEL_SHOW_WARNING, var18.toString(), (Exception)null, (Object)null);
               }

               this.entitiesToSave.add(var5);
            }
         } catch (Exception var14) {
            Exception var3 = var14;

            try {
               this.has_fatal_error = true;
               EntityHome.getInstance().abortSession();
               System.out.println("!!! Torzsadat konverzio sikertelen : " + var2.getEntityType() + ", inditsa ujra a programot");
               ErrorList.getInstance().writeError(ErrorList.LEVEL_SHOW_FATAL_ERROR, var3.getMessage(), var3, (Object)null);
            } catch (EntityException var12) {
               var14.printStackTrace();
               System.err.println("\nA módosítások visszavonása sikeretelen!");
               var12.printStackTrace();
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

   public Entity createEntityForType(String var1) {
      Entity var2 = null;

      try {
         var2 = EntityHome.getInstance().create(var1);
      } catch (EntityException var4) {
         ErrorList.getInstance().writeError(ErrorList.LEVEL_SHOW_FATAL_ERROR, "'" + var1 + "' : " + var4.getMessage(), var4, (Object)null);
      }

      return var2;
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
}
