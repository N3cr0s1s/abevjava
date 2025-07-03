package hu.piller.enykp.alogic.masterdata.gui;

import hu.piller.enykp.alogic.masterdata.core.Block;
import hu.piller.enykp.alogic.masterdata.core.BlockDefinition;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityError;
import hu.piller.enykp.alogic.masterdata.core.EntityException;
import hu.piller.enykp.alogic.masterdata.core.EntityHome;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryException;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryMetaFactory;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import java.util.Iterator;
import java.util.Vector;

public final class MDFormController {
   private boolean changed;
   private long NEWINSTANCE = -1L;
   private long entity_id;

   public MDFormController() {
      this.entity_id = this.NEWINSTANCE;
   }

   public boolean hasChanged() {
      return this.changed;
   }

   public void setEntityIDForUpdate(long var1) {
      this.entity_id = var1;
   }

   public String getOrgForMasterData(String var1) {
      String var2 = "ALL";

      try {
         var2 = MDRepositoryMetaFactory.getMDRepositoryMeta().getOrgForMasterData(var1);
      } catch (MDRepositoryException var4) {
         var4.printStackTrace();
      }

      return var2;
   }

   public BlockDefinition[] getEntityDefinition(String var1) {
      BlockDefinition[] var2 = new BlockDefinition[0];

      try {
         var2 = MDRepositoryMetaFactory.getMDRepositoryMeta().getBlockDefinitionsForEntity(var1);
      } catch (MDRepositoryException var4) {
         var4.printStackTrace();
      }

      return var2;
   }

   public String[] getOrgsForEntity(String var1) {
      Vector var2 = new Vector();
      if (var1 != null && !"".equals(var1.trim())) {
         try {
            BlockDefinition[] var3 = this.getEntityDefinition(var1);
            BlockDefinition[] var4 = var3;
            int var5 = var3.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               BlockDefinition var7 = var4[var6];
               String[] var8 = var7.getMasterDataNames();
               int var9 = var8.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  String var11 = var8[var10];
                  String var12 = MDRepositoryMetaFactory.getMDRepositoryMeta().getOrgForMasterData(var11);
                  if (var12 != null && !var2.contains(var12)) {
                     var2.add(var12);
                  }
               }
            }
         } catch (MDRepositoryException var13) {
            var13.printStackTrace();
         }
      }

      return (String[])var2.toArray(new String[var2.size()]);
   }

   public Vector<MDBlockComponentBean> read(long var1) {
      Vector var3 = new Vector();

      try {
         Entity var4 = EntityHome.getInstance().findByID(var1);
         var3 = this.convertEntityToMDBlockComponentBean(var4);
      } catch (EntityException var5) {
         var5.printStackTrace();
      }

      return var3;
   }

   public Vector<MDBlockComponentBean> convertEntityToMDBlockComponentBean(Entity var1) {
      Vector var2 = new Vector();
      if (var1 != null) {
         BlockDefinition[] var3 = this.getEntityDefinition(var1.getName());
         BlockDefinition[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            BlockDefinition var7 = var4[var6];
            MDBlockComponentBean var8 = new MDBlockComponentBean(var7.getBlockName(), var7.getMasterDataNames());
            int var9 = 1;
            Block[] var10 = var1.getBlocks(var7.getBlockName());
            int var11 = var10.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               Block var13 = var10[var12];
               var8.addEmptyDataRecord();
               String[] var14 = var7.getMasterDataNames();
               int var15 = var14.length;

               for(int var16 = 0; var16 < var15; ++var16) {
                  String var17 = var14[var16];
                  var8.setMDValue(var9, var17, var13.getMasterData(var17).getValue());
               }

               ++var9;
            }

            var2.add(var8);
         }
      }

      return var2;
   }

   public EntityError[] save(String var1, Vector<MDBlockComponentBean> var2) {
      String var3 = "OK";
      this.changed = false;
      Entity var4 = null;

      try {
         BlockDefinition[] var5 = this.getEntityDefinition(var1);
         var4 = this.getEntity(var1);
         Iterator var17 = var2.iterator();

         while(true) {
            MDBlockComponentBean var7;
            int var10;
            int var18;
            do {
               do {
                  if (!var17.hasNext()) {
                     if ("OK".equals(var3)) {
                        EntityHome.getInstance().update(var4);
                        this.entity_id = var4.getId();
                     }

                     return var4.getValidityStatus();
                  }

                  var7 = (MDBlockComponentBean)var17.next();
                  BlockDefinition var8 = null;
                  BlockDefinition[] var9 = var5;
                  var10 = var5.length;

                  int var11;
                  for(var11 = 0; var11 < var10; ++var11) {
                     BlockDefinition var12 = var9[var11];
                     if (var7.getBlockType().equals(var12.getBlockName())) {
                        var8 = var12;
                        break;
                     }
                  }

                  for(var18 = 1; var18 <= var7.getSize(); ++var18) {
                     if (var8 == null) {
                        var3 = "HIBA: érvénytelen adatblokk '" + var7.getBlockType() + "'";
                        break;
                     }

                     String[] var19 = var8.getMasterDataNames();
                     var11 = var19.length;

                     for(int var20 = 0; var20 < var11; ++var20) {
                        String var13 = var19[var20];
                        String var14 = var4.getBlock(var7.getBlockType(), var18).getMasterData(var13).getValue();
                        String var15 = var7.getMDValue(var18, var13);
                        if (!this.changed && !var14.equals(var15)) {
                           this.changed = true;
                        }

                        var4.getBlock(var7.getBlockType(), var18).getMasterData(var13).setValue(var15);
                     }
                  }
               } while(!"OK".equals(var3));

               var18 = var4.getBlocks(var7.getBlockType()).length;
               var10 = var7.getSize();
            } while(var18 <= var10);

            while(var18 > var10) {
               var4.removeBlock(var7.getBlockType(), var18);
               --var18;
            }
         }
      } catch (EntityException var16) {
         StringBuffer var6 = new StringBuffer("Hibás adatok!");
         var6.append("\n\n");
         if (var4 != null && var4.getValidityStatus().length > 0) {
            var6.append("Mozgassa az egeret az adatbeviteli űrlap pirossal kiemelt hibás mezői\n");
            var6.append("fölé, az érintett adatelem hibájával kapcsolatos tájékoztatásért!\n\n");
         }

         GuiUtil.showMessageDialog(MainFrame.thisinstance, var6.toString(), "Hiba", 0);
         var3 = var16.getMessage();
         return var4.getValidityStatus();
      }
   }

   private Entity getEntity(String var1) throws EntityException {
      Entity var2;
      if (this.entity_id != this.NEWINSTANCE) {
         var2 = EntityHome.getInstance().findByID(this.entity_id);
      } else {
         var2 = EntityHome.getInstance().create(var1);
      }

      return var2;
   }
}
