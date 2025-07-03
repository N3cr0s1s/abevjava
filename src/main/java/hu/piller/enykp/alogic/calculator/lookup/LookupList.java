package hu.piller.enykp.alogic.calculator.lookup;

import java.util.Vector;

public class LookupList {
   private final Vector elemek;
   private final Vector ertekek;
   private final Vector indexek;
   private boolean overWrite = false;

   public LookupList(Vector var1, Vector var2, Vector var3) {
      this.elemek = var1;
      this.ertekek = var2;
      this.indexek = var3;
   }

   public Vector getElemek() {
      return this.elemek;
   }

   public Vector getErtekek() {
      return this.ertekek;
   }

   public Vector getIndexek() {
      return this.indexek;
   }

   public boolean isOverWrite() {
      return this.overWrite;
   }

   public void setOverWrite(boolean var1) {
      this.overWrite = var1;
   }
}
