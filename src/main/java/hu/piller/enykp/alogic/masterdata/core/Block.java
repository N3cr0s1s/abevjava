package hu.piller.enykp.alogic.masterdata.core;

public class Block implements Comparable {
   String name;
   MasterData[] elements;
   int seq;

   public Block(BlockDefinition var1, int var2) {
      this.name = var1.getBlockName();
      if (this.name != null && !"".equals(this.name)) {
         this.seq = var2;
         String[] var3 = var1.getMasterDataNames();
         this.elements = new MasterData[var3.length];

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4] == null) {
               throw new IllegalArgumentException(this.name + ": null törzsadatmező!");
            }

            this.checkForDuplicate(var3[var4]);
            this.elements[var4] = new MasterData(var3[var4]);
         }

      } else {
         throw new IllegalArgumentException("Törzsadat-blokknak nincsen neve!");
      }
   }

   public String getName() {
      return this.name;
   }

   public int getSeq() {
      return this.seq;
   }

   public boolean isEmpty() {
      boolean var1 = true;

      for(int var2 = 0; var2 < this.elements.length; ++var2) {
         if (!this.elements[var2].isEmpty()) {
            var1 = false;
            break;
         }
      }

      return var1;
   }

   public MasterData getMasterData(String var1) {
      int var2;
      if ((var2 = this.getIndex(var1)) > -1) {
         return this.elements[var2];
      } else {
         throw new IllegalArgumentException("Nincs '" + var1 + "' nevü törzsadat elem!");
      }
   }

   public void clear() {
      for(int var1 = 0; var1 < this.elements.length; ++var1) {
         this.elements[var1].clear();
      }

   }

   public boolean hasKey(String var1) {
      return this.getIndex(var1) != -1;
   }

   private void checkForDuplicate(String var1) {
      for(int var2 = 0; var2 < this.elements.length; ++var2) {
         if (this.elements[var2] != null && this.elements[var2].getKey().equals(var1)) {
            throw new IllegalArgumentException(this.name + ": nem lehet kétszer blokkon belül azonos törzsadatmező '" + var1 + "'!");
         }
      }

   }

   private int getIndex(String var1) {
      for(int var2 = 0; var2 < this.elements.length; ++var2) {
         if (this.elements[var2] != null && this.elements[var2].getKey().equals(var1)) {
            return var2;
         }
      }

      return -1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Név: '");
      var1.append(this.getName());
      var1.append("' sorszám ");
      var1.append(this.seq);
      var1.append("\n");

      for(int var2 = 0; var2 < this.elements.length; ++var2) {
         var1.append(this.elements[var2]);
         var1.append("\n");
      }

      return var1.toString();
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof Block) {
         Block var2 = (Block)var1;
         if (this.name.equals(var2.name) && this.elements.length == var2.elements.length) {
            boolean var3 = true;

            for(int var4 = 0; var4 < this.elements.length; ++var4) {
               if (!this.elements[var4].equals(var2.elements[var4]) || this.seq != var2.seq) {
                  var3 = false;
                  break;
               }
            }

            if (var3) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.toString().hashCode();
   }

   public int compareTo(Object var1) {
      if (var1 != null && var1 instanceof Block) {
         Block var2 = (Block)var1;
         int var3 = this.name.compareTo(var2.name);
         return var3 != 0 ? var3 : this.seq - var2.seq;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public String toXmlString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<Block>");
      var1.append("<name>");
      var1.append(this.name);
      var1.append("</name>");
      var1.append("<seq>");
      var1.append(this.seq);
      var1.append("</seq>");

      for(int var2 = 0; var2 < this.elements.length; ++var2) {
         var1.append(this.elements[var2].toXmlString());
      }

      var1.append("</Block>");
      return var1.toString();
   }
}
