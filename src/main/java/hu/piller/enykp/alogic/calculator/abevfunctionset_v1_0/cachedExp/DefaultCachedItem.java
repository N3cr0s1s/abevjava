package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.cachedExp;

import java.util.Vector;

public abstract class DefaultCachedItem implements ICachedItem {
   protected String formid = null;
   protected String function = null;
   protected String parameter = null;
   protected Object[] result = null;
   protected Vector errors = null;

   public Object[] getResult() {
      return this.result;
   }

   public Vector getErrors() {
      return this.errors;
   }

   public String getFormid() {
      return this.formid;
   }

   public String getFunction() {
      return this.function;
   }

   public String getParameter() {
      return this.parameter;
   }
}
