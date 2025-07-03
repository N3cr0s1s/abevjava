package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0;

import hu.piller.enykp.alogic.calculator.calculator_c.ExpClass;
import hu.piller.enykp.interfaces.IPropertyList;

class ExpWrapper implements IPropertyList {
   private ExpClass exp;

   public ExpWrapper() {
   }

   public ExpWrapper(ExpClass var1) {
      this.setExp(var1);
   }

   public void setExp(ExpClass var1) {
      this.exp = var1;
   }

   public ExpClass getExp() {
      return this.exp;
   }

   public boolean set(Object var1, Object var2) {
      return false;
   }

   public Object get(Object var1) {
      return this.get(var1, this.exp);
   }

   public Object get(Object var1, ExpClass var2) {
      boolean var3 = FunctionBodies.g_in_variable_exp;
      ABEVFunctionSet var4 = ABEVFunctionSet.getInstance();
      if (var1 != null) {
         if (var2 == null) {
            return null;
         }

         int var5 = var2.getExpType();
         switch(var5) {
         case 0:
         case 1:
         default:
            break;
         case 2:
            FunctionBodies.g_in_variable_exp = true;
            this.determinateVariableValue(var1, var2);
            if (!var3) {
               FunctionBodies.g_in_variable_exp = false;
            }
            break;
         case 3:
            String var6 = var2.getIdentifier();
            Object var7 = ABEVFunctionSet.FUNCTION_DESCRIPTORS.get(var6);
            boolean var8 = FnDescHelper.isParamsCalcFirst(var7);
            if (var8) {
               this.determinateParamterValues(var1, var2, var6);
            }

            var2.setResult(var4.calculate(var2));
         }
      }

      return var2.getResult();
   }

   private void determinateVariableValue(Object var1, ExpClass var2) {
      try {
         ExpClass var3 = var2;
         IPropertyList var4 = (IPropertyList)FunctionBodies.g_variables.get(FunctionBodies.g_active_form_id);
         var2 = (ExpClass)var4.get(var2.getIdentifier());
         this.get(var1, var2);
         var3.setType(var2.getType());
         var3.setResult(var2.getResult());
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   private void determinateParamterValues(Object var1, ExpClass var2, String var3) {
      ExpClass var4 = var2;
      Object var5;
      if (var3.equalsIgnoreCase("if")) {
         var2 = var2.getParameter(0);
         var5 = this.get(var1, var2);
         var2.setResult(var5);
         byte var6 = 2;
         if (var2.getType() == 4 && (Boolean)var5) {
            var6 = 1;
         }

         var2 = var4.getParameter(var6);
         var2.setResult(this.get(var1, var2));
      } else if (var3.equalsIgnoreCase("imp")) {
         var2 = var2.getParameter(0);
         var5 = this.get(var1, var2);
         var2.setResult(var5);
         if (var2.getType() == 4 && !(Boolean)var5) {
            return;
         }

         var2 = var4.getParameter(1);
         var2.setResult(this.get(var1, var2));
      } else {
         int var9;
         int var10;
         if (var3.equalsIgnoreCase("ha_n")) {
            var9 = var2.getParametersCount() - 1;
            var10 = 0;
            boolean var7 = true;

            while(true) {
               while(var10 < var9 && var7) {
                  var2 = var4.getParameter(var10++);
                  Object var8 = this.get(var1, var2);
                  var2.setResult(var8);
                  if (var2.getType() == 4 && (Boolean)var8) {
                     var7 = false;
                     var2 = var4.getParameter(var10++);
                     var2.setResult(this.get(var1, var2));
                  } else {
                     ++var10;
                  }
               }

               if (var7) {
                  var2 = var4.getParameter(var10);
                  var2.setResult(this.get(var1, var2));
               }
               break;
            }
         } else {
            var9 = 0;

            for(var10 = var2.getParametersCount(); var9 < var10; ++var9) {
               var2 = var4.getParameter(var9);
               var2.setResult(this.get(var1, var2));
            }
         }
      }

   }
}
