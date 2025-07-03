package hu.piller.enykp.alogic.calculator.calculator_c;

public class MatrixSearchItem {
   public static final String OPERATOR_EQUALS_TXT = "=";
   public static final String OPERATOR_NOT_EQUALS_TXT = "<>";
   public static final String OPERATOR_CONTAINS_TXT = "tartalmaz";
   public static final String OPERATOR_PART_TXT = "része";
   public static final int OPERATOR_EQUALS = 1;
   public static final int OPERATOR_NOT_EQUALS = 2;
   public static final int OPERATOR_CONTAINS = 3;
   public static final int OPERATOR_PART = 4;
   public static final int OPERATOR_UNDEFINED = 100;
   private final Integer columnIndex;
   private final int realation;
   private final Object searchedValue;
   private final boolean caseSensitive;

   public MatrixSearchItem(Integer var1, String var2, Object var3, boolean var4) {
      this.columnIndex = var1;
      this.searchedValue = var3;
      this.caseSensitive = var4;
      if (var2.equalsIgnoreCase("=")) {
         this.realation = 1;
      } else if (var2.equalsIgnoreCase("<>")) {
         this.realation = 2;
      } else if (var2.equalsIgnoreCase("tartalmaz")) {
         this.realation = 3;
      } else if (var2.equalsIgnoreCase("része")) {
         this.realation = 4;
      } else {
         this.realation = 100;
      }

   }

   public int getRealation() {
      return this.realation;
   }

   public Integer getColumnIndex() {
      return this.columnIndex;
   }

   public Object getSearchedValue() {
      return this.searchedValue;
   }

   public boolean isCaseSensitive() {
      return this.caseSensitive;
   }

   public String toString() {
      return "(columnIndex=" + this.columnIndex + ",realation=" + this.realation + ",searchedValue=" + this.searchedValue + ",caseSensitive=" + this.caseSensitive + ")";
   }
}
