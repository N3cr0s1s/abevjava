package hu.piller.enykp.alogic.kihatas;

import java.util.Vector;

public class MegallapitasVector extends Vector<MegallapitasRecord> {
   public boolean isDeleted() {
      int var1;
      for(var1 = 0; var1 < this.size() && !((MegallapitasRecord)this.elementAt(var1)).isDeleted(); ++var1) {
      }

      return var1 < this.size();
   }

   public MegallapitasVector make_copy() {
      MegallapitasVector var1 = new MegallapitasVector();

      for(int var2 = 0; var2 < this.size(); ++var2) {
         MegallapitasRecord var3 = (MegallapitasRecord)this.elementAt(var2);
         var1.add(var3.make_copy());
      }

      return var1;
   }

   public boolean vannemtorolt() {
      for(int var1 = 0; var1 < this.size(); ++var1) {
         MegallapitasRecord var2 = (MegallapitasRecord)this.elementAt(var1);
         if (!var2.isDeleted()) {
            return true;
         }
      }

      return false;
   }

   public void done_delete() {
      for(int var1 = this.size() - 1; -1 < var1; --var1) {
         MegallapitasRecord var2 = (MegallapitasRecord)this.elementAt(var1);
         if ("".equals(var2.getKias_azon())) {
            this.remove(var1);
         } else {
            var2.setToroltsegjel("T");
         }
      }

   }

   public int nondeletedcount() {
      int var1 = 0;

      for(int var2 = 0; var2 < this.size(); ++var2) {
         MegallapitasRecord var3 = (MegallapitasRecord)this.elementAt(var2);
         if (!var3.isDeleted()) {
            ++var1;
         }
      }

      return var1;
   }

   public boolean vanAdatbazisbolErkezett() {
      for(int var1 = 0; var1 < this.size(); ++var1) {
         MegallapitasRecord var2 = (MegallapitasRecord)this.elementAt(var1);
         if (!"".equals(var2.getKias_azon())) {
            return true;
         }
      }

      return false;
   }
}
