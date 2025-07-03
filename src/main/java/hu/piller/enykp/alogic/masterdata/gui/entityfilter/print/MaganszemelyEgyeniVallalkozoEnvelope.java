package hu.piller.enykp.alogic.masterdata.gui.entityfilter.print;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.primaryaccount.common.ABEVEnvelope;
import java.util.Hashtable;

public class MaganszemelyEgyeniVallalkozoEnvelope extends ABEVEnvelope {
   private Entity entity;

   public MaganszemelyEgyeniVallalkozoEnvelope(Entity var1) {
      this.entity = var1;
   }

   protected Hashtable getData(int var1) {
      Hashtable var2 = this.envelope_data;
      this.initializeData(var2);
      if (this.record != null) {
         var2.put("f_feladó", this.getName());
         var2.put("f_utca", this.getStreet(var1));
         if (var1 == 0) {
            var2.put("f_város", this.entity.getBlock("Állandó cím").getMasterData("Település").getValue());
            var2.put("f_irsz", this.entity.getBlock("Állandó cím").getMasterData("Irányítószám").getValue());
         } else if (var1 == 1) {
            var2.put("f_város", this.entity.getBlock("Levelezési cím").getMasterData("L Település").getValue());
            var2.put("f_irsz", this.entity.getBlock("Levelezési cím").getMasterData("L Irányítószám").getValue());
         }
      }

      this.getReceiverData(var2);
      return var2;
   }

   private String getName() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.entity.getBlock("Törzsadatok").getMasterData("Vezetékneve").getValue());
      var1.append(" ");
      var1.append(this.entity.getBlock("Törzsadatok").getMasterData("Keresztneve").getValue());
      return var1.toString();
   }

   private String getStreet(int var1) {
      StringBuffer var2 = new StringBuffer();
      String var3 = "";
      if (var1 == 0) {
         var2.append(this.entity.getBlock("Állandó cím").getMasterData("Közterület neve").getValue());
         var2.append(" ");
         var2.append(this.entity.getBlock("Állandó cím").getMasterData("Közterület jellege").getValue());
         var2.append(" ");
         var2.append(this.entity.getBlock("Állandó cím").getMasterData("Házszám").getValue());
         var2.append(". ");
         var3 = this.entity.getBlock("Állandó cím").getMasterData("Épület").getValue();
         if (!"".equals(var3)) {
            var2.append(var3);
            var2.append(" ép. ");
         }

         var3 = this.entity.getBlock("Állandó cím").getMasterData("Lépcsőház").getValue();
         if (!"".equals(var3)) {
            var2.append(var3);
            var2.append(" lh. ");
         }

         var3 = this.entity.getBlock("Állandó cím").getMasterData("Emelet").getValue();
         if (!"".equals(var3)) {
            var2.append(var3);
            var2.append(" em. ");
         }

         var3 = this.entity.getBlock("Állandó cím").getMasterData("Ajtó").getValue();
         if (!"".equals(var3)) {
            var2.append(var3);
         }
      } else if (var1 == 1) {
         var2.append(this.entity.getBlock("Levelezési cím").getMasterData("L Közterület neve").getValue());
         var2.append(" ");
         var2.append(this.entity.getBlock("Levelezési cím").getMasterData("L Közterület jellege").getValue());
         var2.append(" ");
         var2.append(this.entity.getBlock("Levelezési cím").getMasterData("L Házszám").getValue());
         var2.append(". ");
         var3 = this.entity.getBlock("Levelezési cím").getMasterData("L Épület").getValue();
         if (!"".equals(var3)) {
            var2.append(var3);
            var2.append(" ép. ");
         }

         var3 = this.entity.getBlock("Levelezési cím").getMasterData("L Lépcsőház").getValue();
         if (!"".equals(var3)) {
            var2.append(var3);
            var2.append(" lh. ");
         }

         var3 = this.entity.getBlock("Levelezési cím").getMasterData("L Emelet").getValue();
         if (!"".equals(var3)) {
            var2.append(var3);
            var2.append(" em. ");
         }

         var3 = this.entity.getBlock("Levelezési cím").getMasterData("L Ajtó").getValue();
         if (!"".equals(var3)) {
            var2.append(var3);
         }
      }

      return var2.toString();
   }
}
