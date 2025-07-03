package hu.piller.enykp.datastore;

import java.util.ArrayList;
import java.util.List;

public class EgyenlegData {
   private String adonemKod;
   private String adonemMegnevezes;
   private List<Long> egyenlegetErintoBefizetendo;
   private List<Long> egyenlegetErintoVisszaigenyelheto;
   private List<Long> reszletfizetestErinto;

   public EgyenlegData(String var1, String var2) {
      this.adonemKod = var1;
      this.adonemMegnevezes = var2;
   }

   public String getAdonemKod() {
      return this.adonemKod;
   }

   public void add2Befizetendo(String var1) {
      if (var1 != null) {
         if (!"".equals(var1) && !"0".equals(var1)) {
            if (this.egyenlegetErintoBefizetendo == null) {
               this.egyenlegetErintoBefizetendo = new ArrayList();
            }

            this.egyenlegetErintoBefizetendo.add(new Long(var1));
         }
      }
   }

   public void add2Visszaigenyelheto(String var1) {
      if (var1 != null) {
         if (!"".equals(var1) && !"0".equals(var1)) {
            if (this.egyenlegetErintoVisszaigenyelheto == null) {
               this.egyenlegetErintoVisszaigenyelheto = new ArrayList();
            }

            this.egyenlegetErintoVisszaigenyelheto.add(new Long(var1));
         }
      }
   }

   public void add2Reszletfizetendo(String var1) {
      if (var1 != null) {
         if (!"".equals(var1) && !"0".equals(var1)) {
            if (this.reszletfizetestErinto == null) {
               this.reszletfizetestErinto = new ArrayList();
            }

            this.reszletfizetestErinto.add(new Long(var1));
         }
      }
   }

   public List getEgyenlegetErintoBefizetendo() {
      return this.egyenlegetErintoBefizetendo;
   }

   public List getEgyenlegetErintoVisszaigenyelheto() {
      return this.egyenlegetErintoVisszaigenyelheto;
   }

   public List getReszletfizetestErinto() {
      return this.reszletfizetestErinto;
   }

   public boolean isEmpty() {
      return this.egyenlegetErintoBefizetendo == null && this.reszletfizetestErinto == null;
   }

   public boolean isEgyenlegEmpty() {
      return this.egyenlegetErintoBefizetendo == null;
   }

   public boolean isReszletEmpty() {
      return this.reszletfizetestErinto == null;
   }

   public String toXmlTag() {
      if (this.isEmpty()) {
         return "";
      } else {
         long var1 = this.getSum();
         if (var1 < 1000L) {
            var1 = 0L;
         }

         StringBuilder var3 = (new StringBuilder("\t<adonemTetel adonem=\"")).append(this.adonemKod).append("\" adonemMegnevezes=\"").append(this.adonemMegnevezes).append("\">\n");
         if (!this.isEgyenlegEmpty()) {
            var3.append("\t\t<bevallasSzerint>\n").append("\t\t\t").append(var1).append("\n").append("\t\t</bevallasSzerint>\n");
         }

         if (!this.isReszletEmpty()) {
            var3.append("\t\t<reszletfizetesSzerint>\n").append("\t\t\t").append(this.getReszletSum()).append("\n").append("\t\t</reszletfizetesSzerint>\n");
         }

         var3.append("\t</adonemTetel>\n");
         return var3.toString();
      }
   }

   private long getSum() {
      long var1 = 0L;
      if (this.egyenlegetErintoBefizetendo != null) {
         for(int var3 = 0; var3 < this.egyenlegetErintoBefizetendo.size(); ++var3) {
            var1 += (Long)this.egyenlegetErintoBefizetendo.get(var3);
         }
      }

      return var1;
   }

   private long getReszletSum() {
      long var1 = 0L;
      if (this.reszletfizetestErinto != null) {
         for(int var3 = 0; var3 < this.reszletfizetestErinto.size(); ++var3) {
            var1 += (Long)this.reszletfizetestErinto.get(var3);
         }
      }

      return var1;
   }
}
