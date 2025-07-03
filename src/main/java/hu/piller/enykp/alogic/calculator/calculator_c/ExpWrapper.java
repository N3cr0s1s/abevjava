package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.IFunctionSet;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IPropertyList;

class ExpWrapper implements IPropertyList {
   private ExpClass exp;
   private Object[] current_field_id;
   private IDataStore idata;
   private String form_id;
   private Object calcRecord;

   public ExpWrapper() {
   }

   public ExpWrapper(ExpClass var1) {
      this.setExp(var1);
   }

   public void setCurrentFieldId(Object var1, String var2, IDataStore var3) {
      this.current_field_id = (Object[])((Object[])var1);
      this.form_id = var2;
      this.idata = var3;
   }

   public void setExp(ExpClass var1) {
      this.exp = var1;
   }

   public ExpClass getExp() {
      return this.exp;
   }

   public Object getCalcRecord() {
      return this.calcRecord;
   }

   public void setCalcRecord(Object var1) {
      this.calcRecord = var1;
   }

   public boolean set(Object var1, Object var2) {
      return false;
   }

   public Object get(Object var1) {
      return this.get(var1, this.exp);
   }

   public Object get(Object var1, ExpClass var2) {
      if (var1 != null) {
         if (var2 == null) {
            return null;
         }

         int var3 = var2.getExpType();
         switch(var3) {
         case 0:
         case 1:
         default:
            break;
         case 2:
            IPropertyList var4 = (IPropertyList)var2.getSource();
            var2.setResult(var4.get(var2));
            break;
         case 3:
            IFunctionSet var5 = (IFunctionSet)var2.getSource();
            var2.setResult(var5.calcExpression(this.calcRecord, var1, var2, this.current_field_id, this.form_id, this.idata));
         }
      }

      return var2.getResult();
   }
}
