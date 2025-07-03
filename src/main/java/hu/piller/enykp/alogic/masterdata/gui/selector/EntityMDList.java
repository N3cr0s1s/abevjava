package hu.piller.enykp.alogic.masterdata.gui.selector;

import hu.piller.enykp.alogic.masterdata.core.Block;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityException;
import hu.piller.enykp.alogic.masterdata.core.EntityHome;
import hu.piller.enykp.alogic.masterdata.core.MasterData;
import hu.piller.enykp.util.base.Tools;
import java.util.Hashtable;

public class EntityMDList extends MDList {
   private String[] entityNames;
   private Hashtable<Long, String> entityIdNames;

   public EntityMDList() {
      this.setType("e");
      this.entityNames = new String[0];
      this.entityIdNames = new Hashtable();
   }

   public void setEntityName(String var1) {
      String[] var2 = new String[this.entityNames.length + 1];
      int var3 = 0;
      String[] var4 = this.entityNames;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = var4[var6];
         var2[var3++] = var7;
      }

      var2[var3] = var1;
      this.entityNames = var2;
   }

   public String getTypeOfSelectedEntity() {
      String var1 = "";
      if (this.getSelected() != -1L) {
         var1 = (String)this.entityIdNames.get(this.getSelected());
      }

      return var1;
   }

   protected void fillModel() {
      this.clear();
      this.entityIdNames.clear();

      try {
         String[] var1 = this.entityNames;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            String var4 = var1[var3];
            Entity[] var5 = (new EntityHome()).findByTypeAndMasterData(var4, new MasterData[0]);
            Entity[] var6 = var5;
            int var7 = var5.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               Entity var9 = var6[var8];
               this.add(this.processToAbstract(var9, (Block)null), new Integer((int)var9.getId()));
               this.entityIdNames.put(var9.getId(), var9.getName());
            }
         }
      } catch (EntityException var10) {
         Tools.eLog(var10, 0);
      }

   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.toString());
      var1.append("Entitások: ");
      String[] var2 = this.entityNames;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         var1.append(var5);
         var1.append(" ");
      }

      var1.append("\n");
      return var1.toString();
   }
}
