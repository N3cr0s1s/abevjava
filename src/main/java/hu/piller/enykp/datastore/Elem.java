package hu.piller.enykp.datastore;

import hu.piller.enykp.interfaces.IFileMappedListElement;
import hu.piller.enykp.interfaces.IPropertyList;
import java.util.HashSet;
import java.util.Hashtable;

public class Elem implements IPropertyList, IFileMappedListElement {
   private String type;
   private Object ref;
   private String label;
   private Hashtable fejlec;
   private Hashtable etc;
   private Boolean nomappingfromoutside;
   private int ccindex;

   public Elem(Object var1, String var2, String var3) {
      this.ref = var1;
      this.type = var2;
      this.label = var3;
      this.etc = new Hashtable();
      this.nomappingfromoutside = null;
   }

   public Elem(Object var1, String var2, String var3, Boolean var4) {
      this.ref = var1;
      this.type = var2;
      this.label = var3;
      this.etc = new Hashtable();
      this.nomappingfromoutside = var4;
   }

   public String getType() {
      return this.type;
   }

   public Object getRef() {
      return this.ref;
   }

   public String getLabel() {
      return this.label;
   }

   public void setLabel(String var1) {
      this.label = var1;
   }

   public Hashtable getFejlec() {
      return this.fejlec;
   }

   public void setFejlec(Hashtable var1) {
      this.fejlec = var1;
   }

   public Hashtable getEtc() {
      return this.etc;
   }

   public void setEtc(Hashtable var1) {
      this.etc = var1;
   }

   public String toString() {
      return this.label;
   }

   public boolean set(Object var1, Object var2) {
      return false;
   }

   public Object get(Object var1) {
      try {
         String var2 = (String)var1;
         if (var2.equals("getRef")) {
            return this.getRef();
         } else if (var2.equals("getType")) {
            return this.getType();
         } else if (var2.equals("getLabel")) {
            return this.getLabel();
         } else {
            return var2.equals("isInMemory") ? Boolean.TRUE : null;
         }
      } catch (Exception var3) {
         return null;
      }
   }

   public float getLoadfactor() {
      return 1.0F;
   }

   public boolean noMapped() {
      if (this.ref == null) {
         return true;
      } else if (this.nomappingfromoutside != null) {
         return this.nomappingfromoutside;
      } else {
         HashSet var1 = new HashSet();
         var1.add("0608A");
         var1.add("0708A");
         var1.add("0808A");
         System.out.println("ELEM.NOMAPPED CALLED Elem line:122");
         return var1.contains(this.type);
      }
   }

   public boolean isChanged() {
      return this.ref instanceof GUI_Datastore ? ((GUI_Datastore)this.ref).isCcdirty() : true;
   }

   public void clearChanged() {
      if (this.ref instanceof GUI_Datastore) {
         ((GUI_Datastore)this.ref).setCcdirty(false);
      }

   }

   public Object getMappedObject() {
      return this.ref;
   }

   public void setMappedObject(Object var1) {
      this.ref = var1;
   }

   public void setIndex(int var1) {
      this.ccindex = var1;
      if (this.ref != null && this.ref instanceof GUI_Datastore) {
         ((GUI_Datastore)this.ref).setCcindex(this.ccindex);
      }

   }

   public int getCcindex() {
      return this.ccindex;
   }
}
