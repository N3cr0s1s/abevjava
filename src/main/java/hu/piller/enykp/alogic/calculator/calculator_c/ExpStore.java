package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.interfaces.IPropertyList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class ExpStore {
   private Hashtable field_metas;
   private Object[] matrices;
   private IPropertyList variables;
   private IPropertyList functions;
   private Object[] form_checks;
   private Object[] form_calculations;
   private Object[] pre_gen_calcs;
   private Object[] post_gen_calcs;
   private Object[] betolt_ertek;
   private IPropertyList pages_checks;
   private IPropertyList field_calculations;
   private IPropertyList field_checks;
   private IPropertyList field_lookup_creates;
   private IPropertyList field_dependencies;
   private IPropertyList depend_pages;
   private Object[] fields;
   private Object[] calculatable_fields;
   private Object[] checkable_fields;
   private Object all_calculation;
   private Object cache_maps;
   private Hashtable<String, Hashtable> extended_cache_maps;
   private Hashtable field_calc_factors;
   private Object[] full_field_calc_order;
   private IPropertyList field_types;
   private Hashtable<String, Object[]> full_field_calc_order_on_page;

   public void setFieldMetas(Hashtable var1) {
      this.field_metas = var1;
   }

   public void setMatrices(Object[] var1) {
      this.matrices = var1;
   }

   public void setVariables(IPropertyList var1) {
      this.variables = var1;
   }

   public void setFunctions(IPropertyList var1) {
      this.functions = var1;
   }

   public void setFormCalculations(Object[] var1) {
      this.form_calculations = var1;
   }

   public void setFormChecks(Object[] var1) {
      this.form_checks = var1;
   }

   public void setPreGenCalcs(Object[] var1) {
      this.pre_gen_calcs = var1;
   }

   public void setPostGenCalcs(Object[] var1) {
      this.post_gen_calcs = var1;
   }

   public void setBetoltErtekCalcs(Object[] var1) {
      this.betolt_ertek = var1;
   }

   public void setPageChecks(IPropertyList var1) {
      this.pages_checks = var1;
   }

   public void setFieldCalculations(IPropertyList var1) {
      this.field_calculations = var1;
   }

   public void setFieldChecks(IPropertyList var1) {
      this.field_checks = var1;
   }

   public void setFieldLookupCreates(IPropertyList var1) {
      this.field_lookup_creates = var1;
   }

   public void setFields(Object[] var1) {
      this.fields = var1;
   }

   public void setCalculatableFields(Object[] var1) {
      this.calculatable_fields = var1;
   }

   public void setCheckableFields(Object[] var1) {
      this.checkable_fields = var1;
   }

   public void setFieldDependencies(IPropertyList var1) {
      this.field_dependencies = var1;
   }

   public void setPageDependencies(IPropertyList var1) {
      this.depend_pages = var1;
   }

   public void setAllCalculation(Object var1) {
      this.all_calculation = var1;
   }

   public void setCacheMaps(Object var1) {
      this.cache_maps = var1;
   }

   public void setFieldCalculationFactors(Hashtable var1) {
      this.field_calc_factors = var1;
   }

   public void setFullFieldCalcOrder(Object[] var1) {
      this.full_field_calc_order = var1;
   }

   public void setFieldTypes(IPropertyList var1) {
      this.field_types = var1;
   }

   public Object getMetas(Object var1) {
      return this.field_metas == null ? null : this.field_metas.get(var1);
   }

   public Object getMatrixItem(Object var1, int[] var2) {
      Object[] var5 = null;
      Object[] var4;
      int var7;
      int var8;
      if (var1 instanceof String) {
         var7 = 0;

         for(var8 = this.matrices.length; var7 < var8; ++var7) {
            var4 = (Object[])((Object[])this.matrices[var7]);
            String var6 = (String)var4[0];
            if (var6.equalsIgnoreCase(var1.toString())) {
               var5 = (Object[])((Object[])var4[1]);
               break;
            }
         }
      } else if (var1 instanceof Integer) {
         var7 = (Integer)var1;
         if (var7 >= 0 && var7 < this.matrices.length) {
            var5 = (Object[])((Object[])((Object[])((Object[])this.matrices[var7]))[1]);
         }
      } else if (var1 instanceof int[]) {
         return this.getMatrixItem(new Integer(0), (int[])((int[])var1));
      }

      Object var3 = null;
      if (var5 != null && var2 != null) {
         var7 = 0;

         for(var8 = var2.length; var7 < var8; ++var7) {
            if (var3 == null) {
               var4 = var5;
            } else if (var3 instanceof Object[]) {
               var4 = (Object[])((Object[])var3);
            } else {
               var4 = null;
            }

            if (var4 != null) {
               var3 = var4[var2[var7]];
            }
         }
      } else {
         var3 = var5;
      }

      return var3;
   }

   public Object getFunctions() {
      return this.functions;
   }

   public Object getVariables() {
      return this.variables;
   }

   public Object getVariable(Object var1) {
      return this.variables == null ? null : this.variables.get(var1);
   }

   public Object getFormChecks() {
      return this.form_checks;
   }

   public Object getFormCalculations() {
      return this.form_calculations;
   }

   public Object[] getPreGenCalcs() {
      return this.pre_gen_calcs;
   }

   public Object[] getPostGenCalcs() {
      return this.post_gen_calcs;
   }

   public Object[] getBetoltErtekCalcs() {
      return this.betolt_ertek;
   }

   public Object getPageChecks(Object var1) {
      return this.pages_checks == null ? null : this.pages_checks.get(var1);
   }

   public Object getFieldCalulation(Object var1) {
      return this.field_calculations == null ? null : this.field_calculations.get(var1);
   }

   public Object getFieldChecks(Object var1) {
      return this.field_checks == null ? null : this.field_checks.get(var1);
   }

   public Object getFieldLookupCreate(Object var1) {
      return this.field_lookup_creates == null ? null : this.field_lookup_creates.get(var1);
   }

   public Object[] getFields() {
      return this.fields;
   }

   public Object[] getCalculatableFields() {
      return this.calculatable_fields;
   }

   public Object[] getCheckableFields() {
      return this.checkable_fields;
   }

   public Object getFieldDependencies(Object var1) {
      return this.field_dependencies == null ? null : this.field_dependencies.get(var1);
   }

   public Object getDependPages(Object var1) {
      return this.depend_pages == null ? null : this.depend_pages.get(var1);
   }

   public Object getAllCalculation() {
      return this.all_calculation;
   }

   public Object getCacheMaps() {
      return this.cache_maps;
   }

   public Hashtable<String, Hashtable> getExtendedCacheMaps() {
      return this.extended_cache_maps;
   }

   public void setExtendedCacheMaps(Hashtable<String, Hashtable> var1) {
      this.extended_cache_maps = var1;
   }

   public Hashtable getFieldCalculationFactors() {
      return this.field_calc_factors;
   }

   public Object[] getFullFieldCalcOrder() {
      return this.full_field_calc_order;
   }

   public IPropertyList getFieldTypes() {
      return this.field_types;
   }

   public Object[] getFullFieldCalcOrderOnPage(String var1) {
      if (this.full_field_calc_order_on_page == null) {
         this.full_field_calc_order_on_page = this.createFullFieldCalcOrderOnPage(Calculator.getInstance().getBookModel(), ExpFactory.form_id);
      }

      return this.full_field_calc_order_on_page.containsKey(var1) ? (Object[])this.full_field_calc_order_on_page.get(var1) : new Object[0];
   }

   private Hashtable<String, Object[]> createFullFieldCalcOrderOnPage(BookModel var1, String var2) {
      Hashtable var3 = new Hashtable();
      Hashtable var4 = new Hashtable();
      if (this.full_field_calc_order != null) {
         FormModel var5 = var1.get(var2);
         int var6 = 0;

         for(int var7 = this.full_field_calc_order.length; var6 < var7; ++var6) {
            String var8 = ((PageModel)var5.fids_page.get(this.full_field_calc_order[var6])).pid;
            Vector var9 = (Vector)var4.get(var8);
            if (var9 == null) {
               var9 = new Vector();
               var4.put(var8, var9);
            }

            var9.add(this.full_field_calc_order[var6]);
         }
      }

      Enumeration var10 = var4.keys();

      while(var10.hasMoreElements()) {
         String var11 = (String)var10.nextElement();
         var3.put(var11, ((Vector)var4.get(var11)).toArray());
      }

      return var3;
   }

   public void release() {
      this.field_metas = null;
      this.matrices = null;
      this.variables = null;
      this.functions = null;
      this.form_checks = null;
      this.form_calculations = null;
      this.pre_gen_calcs = null;
      this.post_gen_calcs = null;
      this.pages_checks = null;
      this.field_calculations = null;
      this.field_checks = null;
      this.field_dependencies = null;
      this.depend_pages = null;
      this.fields = null;
      this.calculatable_fields = null;
      this.checkable_fields = null;
      this.all_calculation = null;
      this.cache_maps = null;
      this.extended_cache_maps = null;
      this.field_calc_factors = null;
      this.full_field_calc_order = null;
      this.field_types = null;
      this.full_field_calc_order_on_page = null;
      this.betolt_ertek = null;
   }
}
