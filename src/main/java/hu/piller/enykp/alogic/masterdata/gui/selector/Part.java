package hu.piller.enykp.alogic.masterdata.gui.selector;

public class Part {
   private String block;
   private String md;

   public String getMd() {
      return this.md;
   }

   public void setMd(String var1) {
      this.md = var1;
   }

   public String getBlock() {
      return this.block;
   }

   public void setBlock(String var1) {
      this.block = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.block);
      var1.append(".");
      var1.append(this.md);
      return var1.toString();
   }
}
