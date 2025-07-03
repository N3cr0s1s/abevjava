package hu.piller.enykp.gui.component;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ENYKComponentFactory implements Map {
   private static final String COMPONENT_CHECK_BOX = "check_box";
   private static final String COMPONENT_DATE_COMBO = "date_combo";
   private static final String COMPONENT_FILTER_COMBO = "filter_combo";
   private static final String COMPONENT_FT_TEXT_FIELD = "formatted_tagged_text_field";
   private static final String COMPONENT_FT_TEXT_FIELD_PAINTER = "formatted_tagged_text_field_painter";
   private static final String COMPONENT_F_TEXT_FIELD = "formatted_text_field";
   private static final String COMPONENT_PANEL = "panel";
   private static final String COMPONENT_T_TEXT_FIELD = "tagged_text_field";
   private static final String COMPONENT_TEXT_FIELD = "text_field";
   private static final String COMPONENT_TAGGED_COMBO = "tagged_combo";
   private static final String COMPONENT_TEXT_AREA = "text_area";
   private static final String COMPONENT_SCROLL_TEXT_AREA = "scroll_text_area";
   private static final Hashtable COMPONENTS = new Hashtable(16);

   public void initialize(Object var1) {
   }

   public int size() {
      return COMPONENTS.size();
   }

   public void clear() {
   }

   public boolean isEmpty() {
      return COMPONENTS.isEmpty();
   }

   public boolean containsKey(Object var1) {
      return COMPONENTS.containsKey(var1);
   }

   public boolean containsValue(Object var1) {
      return false;
   }

   public Collection values() {
      return null;
   }

   public void putAll(Map var1) {
   }

   public Set entrySet() {
      return null;
   }

   public Set keySet() {
      return new Set() {
         private Set set = ENYKComponentFactory.this.keySet();

         public int size() {
            return this.set.size();
         }

         public void clear() {
         }

         public boolean isEmpty() {
            return this.set.isEmpty();
         }

         public Object[] toArray() {
            return this.set.toArray();
         }

         public boolean add(Object var1) {
            return false;
         }

         public boolean contains(Object var1) {
            return this.set.contains(var1);
         }

         public boolean remove(Object var1) {
            return false;
         }

         public boolean addAll(Collection var1) {
            return false;
         }

         public boolean containsAll(Collection var1) {
            return this.set.containsAll(var1);
         }

         public boolean removeAll(Collection var1) {
            return false;
         }

         public boolean retainAll(Collection var1) {
            return false;
         }

         public Iterator iterator() {
            return this.set.iterator();
         }

         public Object[] toArray(Object[] var1) {
            return this.set.toArray(var1);
         }
      };
   }

   public Object get(Object var1) {
      Object var2 = null;
      if (var1 instanceof String) {
         String var3 = (String)var1;
         if (var3.equalsIgnoreCase("check_box")) {
            return new ENYKCheckBox();
         }

         if (var3.equalsIgnoreCase("date_combo")) {
            return new ENYKDateCombo();
         }

         if (var3.equalsIgnoreCase("filter_combo")) {
            return new ENYKFilterCombo();
         }

         if (var3.equalsIgnoreCase("formatted_tagged_text_field")) {
            return new ENYKFormattedTaggedTextField();
         }

         if (var3.equalsIgnoreCase("formatted_tagged_text_field_painter")) {
            return new ENYKFormattedTaggedTextFieldPainter();
         }

         if (var3.equalsIgnoreCase("formatted_text_field")) {
            return new ENYKFormattedTextField();
         }

         if (var3.equalsIgnoreCase("text_field")) {
            return new ENYKTextField();
         }

         if (var3.equalsIgnoreCase("tagged_combo")) {
            return new ENYKTaggedComboBox();
         }

         if (var3.equalsIgnoreCase("text_area")) {
            return new ENYKTextArea();
         }

         if (var3.equalsIgnoreCase("scroll_text_area")) {
            return new ENYKScrollTextArea();
         }
      }

      return new ENYKTextField();
   }

   public Object remove(Object var1) {
      return null;
   }

   public Object put(Object var1, Object var2) {
      return null;
   }

   static {
      COMPONENTS.put("check_box", "");
      COMPONENTS.put("date_combo", "");
      COMPONENTS.put("filter_combo", "");
      COMPONENTS.put("formatted_tagged_text_field", "");
      COMPONENTS.put("formatted_text_field", "");
      COMPONENTS.put("panel", "");
      COMPONENTS.put("tagged_text_field", "");
      COMPONENTS.put("text_field", "");
      COMPONENTS.put("tagged_combo", "");
      COMPONENTS.put("text_area", "");
      COMPONENTS.put("scroll_text_area", "");
   }
}
