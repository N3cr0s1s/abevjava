package hu.piller.enykp.alogic.masterdata.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MasterData {
   private String key;
   private List<String> value;

   public MasterData(String var1) {
      this.key = var1;
      this.init("");
   }

   public MasterData(String var1, String var2) {
      this.key = var1;
      this.init(var2);
   }

   public MasterData(String var1, String[] var2) {
      this.key = var1;
      if (var2.length == 0) {
         this.init("");
      } else {
         this.init(var2[0]);

         for(int var3 = 1; var3 < var2.length; ++var3) {
            this.extendValueList(var2[var3]);
         }
      }

   }

   private void init(String var1) {
      this.value = new ArrayList(1);
      this.value.add(var1);
   }

   public String getKey() {
      return this.key;
   }

   public String getValue() {
      return (String)this.value.get(0);
   }

   public void setValue(String var1) {
      this.value.set(0, var1);
   }

   public boolean isEmpty() {
      return this.value.isEmpty();
   }

   public void clear() {
      this.value.clear();
   }

   public boolean hasValueList() {
      return this.value.size() > 1;
   }

   public void extendValueList(String var1) {
      this.value.add(var1);
   }

   public List<String> getValues() {
      return this.value;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof MasterData)) {
         return false;
      } else {
         MasterData var2 = (MasterData)var1;
         return var2.key.equals(this.key) && var2.value.toString().equals(this.value.toString());
      }
   }

   public int hashCode() {
      return (this.key + this.value.toString()).hashCode();
   }

   public String toString() {
      return "[" + this.key + " => " + this.value + "]";
   }

   public String toXmlString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<MasterData>");
      var1.append("<key>");
      var1.append(this.key);
      var1.append("</key>");
      Iterator var2 = this.value.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var1.append("<val>");
         var1.append(this.toValidXmlContent(var3));
         var1.append("</val>");
      }

      var1.append("</MasterData>");
      return var1.toString();
   }

   private String toValidXmlContent(String var1) {
      String var2 = "";
      if (var1 == null) {
         return var2;
      } else {
         if (var1.indexOf("&") == -1 && var1.indexOf("<") == -1) {
            var2 = var1;
         } else {
            var2 = "<![CDATA[" + var1 + "]]>";
         }

         return var2;
      }
   }
}
