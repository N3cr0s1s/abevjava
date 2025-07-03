package hu.piller.enykp.alogic.masterdata.core;

import hu.piller.enykp.alogic.masterdata.core.validator.EntityValidator;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryException;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryMetaFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Entity implements Cloneable {
   private String name;
   private BlockDefinition[] entityDefinition;
   private Block[][] data;
   private long id;
   private EntityValidator entityValidator;
   private EntityError[] validity;

   public Entity(String var1, BlockDefinition[] var2, long var3) {
      this.name = var1;
      this.id = var3;
      this.entityDefinition = var2;
      this.data = new Block[var2.length][];
      int var5 = 0;
      BlockDefinition[] var6 = var2;
      int var7 = var2.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         BlockDefinition var9 = var6[var8];
         this.data[var5++] = new Block[0];

         for(int var10 = 0; var10 < var9.getMin(); ++var10) {
            this.addBlock(var9.getBlockName());
         }
      }

      try {
         this.entityValidator = MDRepositoryMetaFactory.getMDRepositoryMeta().getValidatorForEntity(var1);
         this.validity = new EntityError[0];
      } catch (MDRepositoryException var11) {
         this.validity = new EntityError[]{new EntityError(var1, "", -1, "", "", var11.getMessage())};
      }

   }

   public void fill(Map<String, String> var1) {
      Block[] var2 = this.getAllBlocks();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Block var5 = var2[var4];
         Iterator var6 = var1.keySet().iterator();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();
            if (var5.hasKey(var7)) {
               String var8 = (String)var1.get(var7);
               var8 = var8 == null ? "" : var8;
               var5.getMasterData(var7).setValue(var8);
            }
         }
      }

   }

   public List<EntityError> merge(Entity var1) {
      ArrayList var2 = new ArrayList();
      BlockDefinition[] var3 = this.entityDefinition;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BlockDefinition var6 = var3[var5];
         Block var7 = null;
         int var8;
         if (var6.getMin() == 1 && var6.getMax() == 1) {
            var7 = this.getBlock(var6.getBlockName());
            var8 = 1;
         } else {
            if (var1.getBlock(var6.getBlockName(), 1).isEmpty()) {
               continue;
            }

            this.addBlock(var6.getBlockName());
            Block[] var9 = this.getBlocks(var6.getBlockName());
            var7 = var9[var9.length - 1];
            var8 = var9.length;
         }

         String[] var16 = var6.getMasterDataNames();
         int var10 = var16.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            String var12 = var16[var11];
            String var13 = var7.getMasterData(var12).getValue();
            String var14 = var1.getBlock(var6.getBlockName()).getMasterData(var12).getValue();
            if (var12.equals("Születési időpont")) {
               var13 = var13.replaceAll("-", "");
            }

            if (!"".equals(var14) && !var13.equals(var14)) {
               var7.getMasterData(var12).setValue(var14);
               EntityError var15 = new EntityError("", var6.getBlockName(), var8, var12, var13, "Megváltozott: " + var13);
               var2.add(var15);
            }
         }
      }

      return var2;
   }

   public void addBlock(String var1) {
      int var2 = this.getBlockIndex(var1);
      if (var2 == -1) {
         throw new IllegalArgumentException("Nincs ilyen nevű blokk: " + var1);
      } else {
         int var3 = this.expand(var2);
         int var4 = var3 + 1;
         this.data[var2][var3] = new Block(this.entityDefinition[var2], var4);
      }
   }

   public int getMin(String var1) {
      BlockDefinition[] var2 = this.entityDefinition;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockDefinition var5 = var2[var4];
         if (var5.getBlockName().equals(var1)) {
            return var5.getMin();
         }
      }

      throw new IllegalArgumentException("Nincs ilyen nevű blokk: " + var1);
   }

   public int getMax(String var1) {
      BlockDefinition[] var2 = this.entityDefinition;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockDefinition var5 = var2[var4];
         if (var5.getBlockName().equals(var1)) {
            return var5.getMax();
         }
      }

      throw new IllegalArgumentException("Nincs ilyen nevű blokk: " + var1);
   }

   public void removeBlock(String var1, int var2) {
      int var3 = this.getBlockIndex(var1);
      if (this.data[var3].length - 1 < this.getMin(var1)) {
         throw new IllegalArgumentException("Minimum elemszám korlát megsértése miatt nem törölhető!");
      } else if (this.data[var3].length >= var2 && var2 > 0) {
         --var2;
         this.removeBlock(var3, var2);
      } else {
         throw new IllegalArgumentException("Nem létező elem nem törölhető!");
      }
   }

   public void removeBlock(Block var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Érvénytelen állapotú blokk (null)");
      } else {
         int var2 = this.getBlockIndex(var1.getName());
         if (this.data[var2].length - 1 < this.getMin(var1.getName())) {
            throw new IllegalArgumentException("Minimum elemszám korlát megsértése miatt nem törölhető!");
         } else {
            int var3;
            for(var3 = 0; var3 < this.data[var2].length && !this.data[var2][var3].equals(var1); ++var3) {
            }

            this.removeBlock(var2, var3);
         }
      }
   }

   private void removeBlock(int var1, int var2) {
      Block[] var3 = new Block[this.data[var1].length - 1];

      int var4;
      for(var4 = 0; var4 < var2; ++var4) {
         var3[var4] = this.data[var1][var4];
      }

      for(var4 = var2 + 1; var4 < this.data[var1].length; ++var4) {
         var3[var4 - 1] = this.data[var1][var4];
         --var3[var4 - 1].seq;
      }

      this.data[var1] = var3;
   }

   public boolean isEmpty() {
      for(int var1 = 0; var1 < this.data.length; ++var1) {
         for(int var2 = 0; var2 < this.data[var1].length; ++var2) {
            if (!this.data[var1][var2].isEmpty()) {
               return false;
            }
         }
      }

      return true;
   }

   public long getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public Block getBlock(String var1, int var2) {
      if (var2 <= 0) {
         throw new IllegalArgumentException("Érvénytelen elem hívatkozás: " + var1 + "/" + var2);
      } else {
         int var3 = this.getBlockIndex(var1);
         if (this.data[var3].length < var2) {
            if (this.entityDefinition[var3].getMax() < var2) {
               throw new IllegalArgumentException("Érvénytelen elem hívatkozás: " + var1 + "/" + var2);
            }

            this.addBlock(var1);
            var2 = this.data[var3].length;
         }

         return this.data[var3][var2 - 1];
      }
   }

   public Block getBlock(String var1) {
      return this.getBlock(var1, 1);
   }

   public Block[] getBlocks(String var1) {
      return this.data[this.getBlockIndex(var1)];
   }

   public Block[] getAllBlocks() {
      int var1 = 0;

      for(int var2 = 0; var2 < this.data.length; ++var2) {
         var1 += this.data[var2].length;
      }

      Block[] var5 = new Block[var1];
      var1 = 0;

      for(int var3 = 0; var3 < this.data.length; ++var3) {
         for(int var4 = 0; var4 < this.data[var3].length; ++var1) {
            var5[var1] = this.data[var3][var4];
            ++var4;
         }
      }

      return var5;
   }

   public void clear() {
      for(int var1 = 0; var1 < this.data.length; ++var1) {
         for(int var2 = 0; var2 < this.data[var1].length; ++var2) {
            this.data[var1][var2].clear();
         }
      }

   }

   public void validate() {
      if (this.entityValidator != null) {
         this.validity = this.entityValidator.isValid(this);
      }

   }

   public EntityError[] getValidityStatus() {
      return (EntityError[])this.validity.clone();
   }

   private int expand(int var1) {
      if (this.data[var1].length == this.entityDefinition[var1].getMax()) {
         throw new IllegalArgumentException("Nem adható hozzá újabb adatblokk!");
      } else {
         Block[] var2 = new Block[this.data[var1].length + 1];

         for(int var3 = 0; var3 < this.data[var1].length; ++var3) {
            var2[var3] = this.data[var1][var3];
         }

         this.data[var1] = var2;
         return this.data[var1].length - 1;
      }
   }

   private int getBlockIndex(String var1) {
      for(int var2 = 0; var2 < this.entityDefinition.length; ++var2) {
         if (this.entityDefinition[var2].getBlockName().equals(var1)) {
            return var2;
         }
      }

      throw new IllegalArgumentException(this.name + ": érvénytelen blokk '" + var1 + "'");
   }

   public boolean equals(Object var1) {
      if (var1 instanceof Entity) {
         Entity var2 = (Entity)var1;
         if (this.id == var2.id) {
            return true;
         }
      }

      return false;
   }

   public int hashCode() {
      return (new Long(this.id)).hashCode();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Típus: ");
      var1.append(this.name);
      var1.append(" (id=");
      var1.append(this.id);
      var1.append(")\n");

      for(int var2 = 0; var2 < this.data.length; ++var2) {
         for(int var3 = 0; var3 < this.data[var2].length; ++var3) {
            var1.append(this.data[var2][var3]);
            var1.append("\n");
         }
      }

      return var1.toString();
   }

   public Object clone() throws CloneNotSupportedException {
      Entity var1 = new Entity(this.name, this.entityDefinition, this.id);
      BlockDefinition[] var2 = this.entityDefinition;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockDefinition var5 = var2[var4];
         String[] var6 = var5.getMasterDataNames();
         int var7 = this.getBlockIndex(var5.getBlockName());
         Block[] var8 = this.data[var7];
         int var9 = var8.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            Block var11 = var8[var10];
            String[] var12 = var6;
            int var13 = var6.length;

            for(int var14 = 0; var14 < var13; ++var14) {
               String var15 = var12[var14];
               var1.getBlock(var5.getBlockName(), var11.getSeq()).getMasterData(var15).setValue(var11.getMasterData(var15).getValue());
            }
         }
      }

      return var1;
   }

   public String toXmlString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<Entity>");
      var1.append("<type>");
      var1.append(this.name);
      var1.append("</type>");
      var1.append("<id>");
      var1.append(this.id);
      var1.append("</id>");

      for(int var2 = 0; var2 < this.data.length; ++var2) {
         for(int var3 = 0; var3 < this.data[var2].length; ++var3) {
            var1.append(this.data[var2][var3].toXmlString());
         }
      }

      var1.append("</Entity>");
      return var1.toString();
   }
}
