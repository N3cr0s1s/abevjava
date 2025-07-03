package hu.piller.enykp.alogic.masterdata.repository.xml;

import hu.piller.enykp.alogic.masterdata.core.Block;
import hu.piller.enykp.alogic.masterdata.core.BlockDefinition;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.MasterData;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryException;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryMetaFactory;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class EntityHandler extends DefaultHandler {
   private boolean has_xml_sequence;
   private long sequence;
   private Hashtable<Long, Entity> entities = new Hashtable();
   BlockDefinition[] entityDef;
   private String type;
   private long id;
   Vector<Block> blocks = new Vector();
   String blockName;
   int block_seq;
   Vector<MasterData> masterDatas = new Vector();
   private String mdKey;
   private String mdVal;
   private List<String> mdVals = new ArrayList(1);
   StringBuffer sb;
   StringBuffer errorMsg = new StringBuffer("");
   private boolean is_entity_valid;

   public long getSequence() {
      return this.sequence;
   }

   public Hashtable<Long, Entity> getEntities() {
      return this.entities;
   }

   public String getErrorMsg() {
      return this.errorMsg.toString();
   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      if (this.is_entity_valid) {
         if ("key".equals(var2)) {
            this.mdKey = this.sb.toString();
         } else if ("val".equals(var2)) {
            this.mdVals.add(this.sb.toString());
         } else if ("MasterData".equals(var2)) {
            this.masterDatas.add(new MasterData(this.mdKey, (String[])this.mdVals.toArray(new String[this.mdVals.size()])));
            this.mdVals.clear();
         } else {
            int var6;
            if ("name".equals(var2)) {
               this.blockName = this.sb.toString();
               boolean var4 = false;
               BlockDefinition[] var5 = this.entityDef;
               var6 = var5.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  BlockDefinition var8 = var5[var7];
                  if (this.blockName.equals(var8.getBlockName())) {
                     var4 = true;
                     break;
                  }
               }

               if (!var4) {
                  this.is_entity_valid = false;
                  this.writeError("Érvénytelen blokktípus '" + this.sb.toString() + "'");
               }
            } else if ("seq".equals(var2)) {
               try {
                  this.block_seq = Integer.parseInt(this.sb.toString());
               } catch (NumberFormatException var15) {
                  this.is_entity_valid = false;
                  this.writeError("Érvénytelen blokkazonosító '" + this.sb.toString() + "'");
               }
            } else if ("Block".equals(var2)) {
               BlockDefinition[] var17 = this.entityDef;
               int var18 = var17.length;

               for(var6 = 0; var6 < var18; ++var6) {
                  BlockDefinition var23 = var17[var6];
                  if (var23.getBlockName().equals(this.blockName)) {
                     Block var25 = new Block(var23, this.block_seq);
                     Iterator var9 = this.masterDatas.iterator();

                     while(var9.hasNext()) {
                        MasterData var10 = (MasterData)var9.next();

                        try {
                           if (var10.hasValueList()) {
                              var25.getMasterData(var10.getKey()).getValues().clear();
                              var25.getMasterData(var10.getKey()).getValues().addAll(var10.getValues());
                           } else {
                              var25.getMasterData(var10.getKey()).setValue(var10.getValue());
                           }
                        } catch (Exception var14) {
                           this.is_entity_valid = false;
                           this.writeError(var14.getMessage());
                        }
                     }

                     this.blocks.add(var25);
                  }
               }
            } else if ("type".equals(var2)) {
               this.type = this.sb.toString();
               String var19 = null;

               try {
                  this.entityDef = MDRepositoryMetaFactory.getMDRepositoryMeta().getBlockDefinitionsForEntity(this.type);
               } catch (MDRepositoryException var13) {
                  this.entityDef = null;
                  var19 = var13.getMessage();
               }

               if (this.entityDef == null) {
                  this.is_entity_valid = false;
                  this.writeError("Érvénytelen egyedtípus '" + this.type + "'" + var19 == null ? "" : " (" + var19 + ")");
               }
            } else if ("id".equals(var2)) {
               try {
                  this.id = Long.parseLong(this.sb.toString());
                  if (!this.has_xml_sequence && this.id > this.sequence) {
                     this.sequence = this.id;
                  }
               } catch (NumberFormatException var12) {
                  this.is_entity_valid = true;
                  this.writeError("Érvénytelen egyed azonosító '" + this.sb.toString() + "'");
               }
            } else if ("Entity".equals(var2) && this.is_entity_valid) {
               try {
                  Entity var21 = new Entity(this.type, this.entityDef, this.id);
                  Iterator var20 = this.blocks.iterator();

                  while(var20.hasNext()) {
                     Block var22 = (Block)var20.next();
                     String[] var24 = new String[0];
                     BlockDefinition[] var26 = this.entityDef;
                     int var28 = var26.length;

                     int var29;
                     for(var29 = 0; var29 < var28; ++var29) {
                        BlockDefinition var11 = var26[var29];
                        if (var11.getBlockName().equals(var22.getName())) {
                           var24 = var11.getMasterDataNames();
                           break;
                        }
                     }

                     String[] var27 = var24;
                     var28 = var24.length;

                     for(var29 = 0; var29 < var28; ++var29) {
                        String var30 = var27[var29];
                        if (var22.getMasterData(var30).hasValueList()) {
                           var21.getBlock(var22.getName(), var22.getSeq()).getMasterData(var30).getValues().clear();
                           var21.getBlock(var22.getName(), var22.getSeq()).getMasterData(var30).getValues().addAll(var22.getMasterData(var30).getValues());
                        } else {
                           var21.getBlock(var22.getName(), var22.getSeq()).getMasterData(var30).setValue(var22.getMasterData(var30).getValue());
                        }
                     }
                  }

                  this.entities.put(var21.getId(), var21);
               } catch (Exception var16) {
                  this.is_entity_valid = false;
                  this.writeError(var16.getMessage());
               }
            }
         }

      }
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      this.sb = new StringBuffer();
      if ("Repository".equals(var2)) {
         String var5 = var4.getValue("", "sequence");
         if (var5 != null) {
            this.sequence = Long.parseLong(var5);
            this.has_xml_sequence = true;
         }

         this.entities.clear();
      } else if ("Block".equals(var2)) {
         this.masterDatas.clear();
      } else if ("Entity".equals(var2)) {
         this.blocks.clear();
         this.is_entity_valid = true;
      }

   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      this.sb.append(new String(var1, var2, var3));
   }

   private void writeError(String var1) {
      String var2 = "Egyed '" + (this.type == null ? "ismeretlen" : this.type) + "' (id=" + (this.id == 0L ? "ismeretlen" : this.id) + "): ";
      this.errorMsg.append(var2);
      this.errorMsg.append(var1);
      this.errorMsg.append("\n");
   }
}
