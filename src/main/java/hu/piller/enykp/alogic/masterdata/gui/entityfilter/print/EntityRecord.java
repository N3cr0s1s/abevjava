package hu.piller.enykp.alogic.masterdata.gui.entityfilter.print;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.primaryaccount.common.ABEVEnvelope;
import hu.piller.enykp.alogic.primaryaccount.common.DefaultRecord;
import hu.piller.enykp.alogic.primaryaccount.common.IRecordFactory;
import java.io.File;

public class EntityRecord extends DefaultRecord {
   private Entity entity;

   public EntityRecord(Entity var1, ABEVEnvelope var2) {
      super((IRecordFactory)null, (File)null, var2);
      this.entity = var1;
      if ("Társaság".equals(var1.getName())) {
         this.getData().put("tax_number", var1.getBlock("Törzsadatok").getMasterData("Adózó adószáma").getValue());
      } else if ("Egyéni vállalkozó".equals(var1.getName())) {
         this.getData().put("tax_number", var1.getBlock("Törzsadatok").getMasterData("Adózó adószáma").getValue());
         this.getData().put("tax_id", var1.getBlock("Törzsadatok").getMasterData("Adózó adóazonosító jele").getValue());
      } else {
         this.getData().put("tax_id", var1.getBlock("Törzsadatok").getMasterData("Adózó adóazonosító jele").getValue());
      }

   }

   public String getName() {
      StringBuffer var1 = new StringBuffer("");
      if ("Társaság".equals(this.entity.getName())) {
         var1.append(this.entity.getBlock("Törzsadatok").getMasterData("Adózó neve").getValue());
      } else {
         var1.append(this.entity.getBlock("Törzsadatok").getMasterData("Vezetékneve").getValue());
         var1.append(" ");
         var1.append(this.entity.getBlock("Törzsadatok").getMasterData("Keresztneve").getValue());
      }

      return var1.toString();
   }

   public String getDescription(String var1) {
      String var2 = "";
      if ("Társaság".equals(this.entity.getName())) {
         var2 = this.entity.getBlock("Törzsadatok").getMasterData("Adózó adószáma").getValue();
      } else if ("Magánszemély".equals(this.entity.getName())) {
         var2 = this.entity.getBlock("Törzsadatok").getMasterData("Adózó adóazonosító jele").getValue();
      } else if ("Egyéni vállalkozó".equals(this.entity.getName())) {
         var2 = this.entity.getBlock("Törzsadatok").getMasterData("Adózó adóazonosító jele").getValue();
         if ("".equals(var2)) {
            var2 = this.entity.getBlock("Törzsadatok").getMasterData("Adózó adószáma").getValue();
         }
      }

      return this.getName() + " [" + var2 + "]";
   }

   public String toString() {
      return this.getName();
   }
}
