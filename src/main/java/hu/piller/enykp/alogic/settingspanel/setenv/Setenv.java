package hu.piller.enykp.alogic.settingspanel.setenv;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class Setenv {
   private int memMin;
   private int memMax;
   private String tuningOpts = "";
   private String xmlOpts = "";

   public int getMemMin() {
      return this.memMin;
   }

   public void setMemMin(int var1) {
      this.memMin = var1;
   }

   public int getMemMax() {
      return this.memMax;
   }

   public void setMemMax(int var1) {
      this.memMax = var1;
   }

   public String getTuningOpts() {
      return this.tuningOpts;
   }

   public void setTuningOpts(String var1) {
      this.tuningOpts = var1;
   }

   public String getXmlOpts() {
      return this.xmlOpts;
   }

   public void setXmlOpts(String var1) {
      this.xmlOpts = var1;
   }

   public void check() throws SetenvException {
      long var1 = this.getPhysicalMemory() / 2L / 1048576L;
      if (var1 < 256L) {
         var1 = 256L;
      }

      if (this.memMin < 128) {
         throw new SetenvException("Az ÁNYK rendelkezésére bocsátott memória minimális értéke 128MB kell legyen!");
      } else if (this.memMax < 256) {
         throw new SetenvException("Az ÁNYK rendelkezésére bocsátott memória maximális értéke legalább 256MB kell legyen!");
      } else if (this.memMax > 2048) {
         throw new SetenvException("Az ÁNYK rendelkezésére bocsátott memória maximális értéke legfeljebb 2048MB lehet!");
      } else if ((long)this.memMax > var1) {
         throw new SetenvException("Az ÁNYK rendelkezésére bocsátott memória maximális értéke legfeljebb a hozzáférhető\n fizikai memória fele (az Ön gépén " + var1 + "MB) lehet!");
      } else if (this.memMax < this.memMin) {
         throw new SetenvException("Az ÁNYK rendelkezésére bocsátott memória maximális értéke legalább akkora kell legyen, mint a minimális értéke!");
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         Setenv var2 = (Setenv)var1;
         if (this.memMax != var2.memMax) {
            return false;
         } else if (this.memMin != var2.memMin) {
            return false;
         } else {
            label38: {
               if (this.tuningOpts != null) {
                  if (this.tuningOpts.equals(var2.tuningOpts)) {
                     break label38;
                  }
               } else if (var2.tuningOpts == null) {
                  break label38;
               }

               return false;
            }

            if (this.xmlOpts != null) {
               if (!this.xmlOpts.equals(var2.xmlOpts)) {
                  return false;
               }
            } else if (var2.xmlOpts != null) {
               return false;
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.memMin;
      var1 = 31 * var1 + this.memMax;
      var1 = 31 * var1 + (this.tuningOpts != null ? this.tuningOpts.hashCode() : 0);
      var1 = 31 * var1 + (this.xmlOpts != null ? this.xmlOpts.hashCode() : 0);
      return var1;
   }

   public String toString() {
      return "[MEMORY_OPTS=" + this.memMin + "/" + this.memMax + "][TUNING_OPTS=" + this.tuningOpts + "][XML_OPTS=" + this.xmlOpts + "]";
   }

   private long getPhysicalMemory() {
      return ((OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
   }
}
