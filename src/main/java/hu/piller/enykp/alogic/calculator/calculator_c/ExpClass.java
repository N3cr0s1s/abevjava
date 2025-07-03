package hu.piller.enykp.alogic.calculator.calculator_c;

public class ExpClass {
   public static final int INVALID = -1;
   public static final int EXP_EMPTY = 0;
   public static final int EXP_CONSTANT = 1;
   public static final int EXP_VARIABLE = 2;
   public static final int EXP_EXPRESSION = 3;
   public static final int EXP_OPERATION = 4;
   public static final int FLAG_EMPTY = 0;
   public static final int FLAG_ZERUS = -2;
   public static final int FLAG_NIL = -3;
   public static final int ZERUS = -2;
   public static final int NIL = 0;
   public static final int STRING = 1;
   public static final int NUMBER = 2;
   public static final int LOGICAL = 4;
   public static final int MATRIXSEARCHMODEL = 5;
   public static final int LOOKUPLISTMODEL = 6;
   private int structure;
   private int exp_type;
   private int type;
   private Object value;
   private Object source;
   private String identifier;
   private int flag;
   private boolean dontmodify = false;
   private Object[] error;
   private ExpClass[] parameters = new ExpClass[0];

   public ExpClass() {
   }

   public ExpClass(int var1, int var2, int var3, Object var4, Object[] var5, int var6, String var7, Object var8) {
      this.structure = var1;
      this.exp_type = var2;
      this.type = var3;
      this.value = var4;
      this.error = var5;
      this.flag = var6;
      this.identifier = var7;
      this.source = var8;
   }

   public void setStructure(int var1) {
      this.structure = var1;
   }

   public int getStructure() {
      return this.structure;
   }

   public void setExpType(int var1) {
      this.exp_type = var1;
   }

   public int getExpType() {
      return this.exp_type;
   }

   public void setType(int var1) {
      this.type = var1;
   }

   public int getType() {
      return this.type;
   }

   public void setValue(Object var1) {
      this.value = var1;
   }

   public Object getValue() {
      return this.value;
   }

   public void setResult(Object var1) {
      this.value = var1;
   }

   public Object getResult() {
      return this.value;
   }

   public void setSource(Object var1) {
      this.source = var1;
   }

   public Object getSource() {
      return this.source;
   }

   public void setIdentifier(String var1) {
      this.identifier = var1;
   }

   public String getIdentifier() {
      return this.identifier;
   }

   public void setFlag(int var1) {
      this.flag = var1;
   }

   public int getFlag() {
      return this.flag;
   }

   public boolean isDontModify() {
      return this.dontmodify;
   }

   public void setDontModify(boolean var1) {
      this.dontmodify = var1;
   }

   public int getForwardedFlag(int var1, int var2) {
      return (var1 & -2) == -2 && var2 == 0 ? var1 : 0;
   }

   public void setError(Object[] var1) {
      this.error = var1;
   }

   public Object[] getError() {
      return this.error;
   }

   public int getParametersCount() {
      return this.parameters.length;
   }

   public void setParameter(int var1, ExpClass var2) {
      if (var1 >= this.parameters.length) {
         ExpClass[] var3 = new ExpClass[var1 + 1];
         System.arraycopy(this.parameters, 0, var3, 0, this.parameters.length);
         this.parameters = var3;
      }

      this.parameters[var1] = var2;
   }

   public ExpClass getParameter(int var1) {
      return var1 < this.parameters.length && var1 >= 0 ? this.parameters[var1] : null;
   }
}
