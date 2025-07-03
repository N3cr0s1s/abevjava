package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.Tools;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

public class CalcFactory {
   private static boolean show_times = false;
   public static final String CLAZZ_EVENT = "event";
   public static final String CLAZZ_TARGET = "target";
   private static boolean is_xml_processed = false;
   private static final Vector all_calc = new Vector(8192, 4096);
   private static Object[] full_collection;
   private static final Object temp_calc_record = CalcHelper.createEmptyCalcRecord();
   private static IPropertyList calculator;
   private static BookModel gui_info;
   private static IPropertyList functions;
   private static IPropertyList variables;
   private static Hashtable maps;

   public static void processFormCalculations(Object var0) throws Exception {
      Object var1 = CalcHelper.createEmptyCalcRecord();
      CalcHelper.setTargetType(var1, "form");
      CalcHelper.setTargetBind(var1, CalcHelper.getTargetBindBoolean("yes"));
      processCalcParentTag(var0, (String)null, 0, var1);
   }

   public static void processPageCalculations(Object var0) throws Exception {
      Vector var1;
      if ((var1 = ExpStoreFactory.getDataVector(var0)) != null) {
         int var4 = 0;

         for(int var5 = var1.size(); var4 < var5; ++var4) {
            Hashtable var2;
            Object var3;
            if ((var2 = ExpStoreFactory.getAttributes(var3 = var1.get(var4))) != null) {
               Object var6 = CalcHelper.createEmptyCalcRecord();
               CalcHelper.setTargetType(var6, "page");
               CalcHelper.setTargetId(var6, var2.get("pid"));
               CalcHelper.setTargetBind(var6, CalcHelper.getTargetBindBoolean("yes"));
               processCalcParentTag(var3, "target_id", 2, var6);
            }
         }
      }

   }

   public static void processFieldCalculations(Object var0) throws Exception {
      Vector var1;
      if ((var1 = ExpStoreFactory.getDataVector(var0)) != null) {
         int var4 = 0;

         for(int var5 = var1.size(); var4 < var5; ++var4) {
            Hashtable var2;
            Object var3;
            if ((var2 = ExpStoreFactory.getAttributes(var3 = var1.get(var4))) != null) {
               Object var6 = CalcHelper.createEmptyCalcRecord();
               CalcHelper.setTargetType(var6, "field");
               CalcHelper.setTargetId(var6, var2.get("fid"));
               CalcHelper.setTargetBind(var6, CalcHelper.getTargetBindBoolean("yes"));
               processCalcParentTag(var3, "target_id", 1, var6);
            }
         }
      }

   }

   private static void processCalcParentTag(Object var0, String var1, int var2, Object var3) throws Exception {
      is_xml_processed = false;
      Vector var4;
      if ((var4 = ExpStoreFactory.getDataVector(var0)) != null) {
         int var5 = 0;

         for(int var6 = var4.size(); var5 < var6; ++var5) {
            processCalcTag(var4.get(var5), var1, var2, var3);
         }
      }

   }

   private static void processCalcTag(Object var0, String var1, int var2, Object var3) throws Exception {
      is_xml_processed = false;
      if (maps == null) {
         maps = new Hashtable(16);
      }

      if ("calc".equalsIgnoreCase(ExpStoreFactory.getTagName(var0))) {
         Hashtable var4;
         if ((var4 = ExpStoreFactory.getAttributes(var0)) != null) {
            String[] var5 = getEvents(var4);
            if (var5 != null) {
               Hashtable var6 = new Hashtable(var4);
               populateCalcRecord(var6, var3);
               int var8 = 0;

               for(int var9 = var5.length; var8 < var9; ++var8) {
                  var6.put("on_event", var5[var8]);
                  boolean var7 = var5[var8].equalsIgnoreCase("field_calc");
                  Object var10 = populateCalcRecord(CalcHelper.createCalcRecord(var6, var1, var2, var7), var3);
                  Integer var11 = CalcHelper.getRole(var10);
                  if (maskRole(var11 == null ? 15 : var11)) {
                     classifyCalcRecord(var10, all_calc, maps);
                  }
               }
            }
         }

      }
   }

   private static boolean maskRole(Integer var0) {
      return (var0 & getEnvRoleMask()) > 0;
   }

   private static int getEnvRoleMask() {
      String var0 = Calculator.getInstance().getBookModel().getRole();
      if (var0.equalsIgnoreCase("0")) {
         return 1;
      } else if (var0.equalsIgnoreCase("1")) {
         return 2;
      } else if (var0.equalsIgnoreCase("2")) {
         return 4;
      } else {
         return var0.equalsIgnoreCase("3") ? 8 : 15;
      }
   }

   private static String[] getEvents(Hashtable var0) {
      if (var0 != null) {
         Object var1 = var0.get("on_event");
         if (var1 instanceof String) {
            String[] var2 = ((String)var1).split(",");
            int var4 = 0;
            int var5 = 0;

            int var6;
            for(var6 = var2.length; var5 < var6; ++var5) {
               var2[var5] = var2[var5].trim();
               if (var2[var5].length() == 0) {
                  var2[var5] = null;
               } else {
                  ++var4;
               }
            }

            String[] var3 = new String[var4];
            var5 = 0;
            var6 = 0;

            for(int var7 = var2.length; var5 < var7; ++var5) {
               if (var2[var5] != null) {
                  var3[var6++] = var2[var5];
               }
            }

            return var3;
         }
      }

      return null;
   }

   private static Object populateCalcRecord(Object var0, Object var1) {
      long var2 = System.currentTimeMillis();
      if (var1 != null) {
         boolean var6 = true;
         CalcHelper.emptyCalcRecord(temp_calc_record);
         Map var9;
         Map var11;
         if (CalcHelper.isCalcRecord(var0)) {
            if (var1 instanceof IPropertyList) {
               IPropertyList var7 = (IPropertyList)var1;
               if (CalcHelper.getEvent(var0) == null) {
                  CalcHelper.setEvent(temp_calc_record, var7.get("on_event"));
               }

               if (CalcHelper.getTargetType(var0) == null) {
                  CalcHelper.setTargetType(temp_calc_record, var7.get("target_type"));
               }

               if (CalcHelper.getTargetId(var0) == null) {
                  CalcHelper.setTargetId(temp_calc_record, var7.get("target_id"));
               }

               if (CalcHelper.getTargetBind(var0) == null) {
                  CalcHelper.setTargetBind(temp_calc_record, var7.get("target_bind"));
               }

               if (CalcHelper.getMsg(var0) == null) {
                  CalcHelper.setMsg(temp_calc_record, var7.get("msg"));
               }

               if (CalcHelper.getErrorCode(var0) == null) {
                  CalcHelper.setErrorCode(temp_calc_record, var7.get("errorcode"));
               }

               if (CalcHelper.getErrorCodeLevel(var0) == null) {
                  CalcHelper.setErrorCodeLevel(temp_calc_record, var7.get("errorlevel"));
               }

               if (CalcHelper.getErrorLevel(var0) == null) {
                  CalcHelper.setErrorLevel(temp_calc_record, var7.get("msglevel"));
               }

               if (CalcHelper.getRole(var0) == null) {
                  CalcHelper.setRole(temp_calc_record, var7.get("role"));
               }

               if (CalcHelper.getExp(var0) == null) {
                  CalcHelper.setExp(temp_calc_record, (ExpClass)var7.get("cexp"));
               }

               if (CalcHelper.getId(var0) == null) {
                  CalcHelper.setId(temp_calc_record, var7.get("id"));
               }
            } else if (var1 instanceof Hashtable) {
               var9 = (Map)var1;
               if (CalcHelper.getEvent(var0) == null) {
                  CalcHelper.setEvent(temp_calc_record, var9.get("on_event"));
               }

               if (CalcHelper.getTargetType(var0) == null) {
                  CalcHelper.setTargetType(temp_calc_record, var9.get("target_type"));
               }

               if (CalcHelper.getTargetId(var0) == null) {
                  CalcHelper.setTargetId(temp_calc_record, var9.get("target_id"));
               }

               if (CalcHelper.getTargetBind(var0) == null) {
                  CalcHelper.setTargetBind(temp_calc_record, var9.get("target_bind"));
               }

               if (CalcHelper.getMsg(var0) == null) {
                  CalcHelper.setMsg(temp_calc_record, var9.get("msg"));
               }

               if (CalcHelper.getErrorLevel(var0) == null) {
                  CalcHelper.setErrorLevel(temp_calc_record, var9.get("msglevel"));
               }

               if (CalcHelper.getErrorCode(var0) == null) {
                  CalcHelper.setErrorCode(temp_calc_record, var9.get("errorcode"));
               }

               if (CalcHelper.getErrorCodeLevel(var0) == null) {
                  CalcHelper.setErrorCodeLevel(temp_calc_record, var9.get("errorlevel"));
               }

               if (CalcHelper.getRole(var0) == null) {
                  CalcHelper.setRole(temp_calc_record, var9.get("role"));
               }

               if (CalcHelper.getExp(var0) == null) {
                  CalcHelper.setExp(temp_calc_record, (ExpClass)var9.get("cexp"));
               }

               if (CalcHelper.getId(var0) == null) {
                  CalcHelper.setId(temp_calc_record, var9.get("id"));
               }
            } else if (var1 instanceof Object[]) {
               if (CalcHelper.getEvent(var0) == null) {
                  CalcHelper.setEvent(temp_calc_record, CalcHelper.getEvent(var1));
               }

               if (CalcHelper.getTargetType(var0) == null) {
                  CalcHelper.setTargetType(temp_calc_record, CalcHelper.getTargetType(var1));
               }

               if (CalcHelper.getTargetId(var0) == null) {
                  CalcHelper.setTargetId(temp_calc_record, CalcHelper.getTargetId(var1));
               }

               if (CalcHelper.getTargetBind(var0) == null) {
                  CalcHelper.setTargetBind(temp_calc_record, CalcHelper.getTargetBind(var1));
               }

               if (CalcHelper.getMsg(var0) == null) {
                  CalcHelper.setMsg(temp_calc_record, CalcHelper.getMsg(var1));
               }

               if (CalcHelper.getErrorLevel(var0) == null) {
                  CalcHelper.setErrorLevel(temp_calc_record, CalcHelper.getErrorLevel(var1));
               }

               if (CalcHelper.getExp(var0) == null) {
                  CalcHelper.setExp(temp_calc_record, CalcHelper.getExp(var1));
               }

               if (CalcHelper.getId(var0) == null) {
                  CalcHelper.setId(temp_calc_record, CalcHelper.getId(var1));
               }
            } else {
               var6 = false;
            }
         } else if (var0 instanceof Map) {
            var9 = (Map)var0;
            if (var1 instanceof IPropertyList) {
               IPropertyList var8 = (IPropertyList)var1;
               if (var9.get("on_event") == null) {
                  CalcHelper.setEvent(temp_calc_record, var8.get("on_event"));
               }

               if (var9.get("target_type") == null) {
                  CalcHelper.setTargetType(temp_calc_record, var8.get("target_type"));
               }

               if (var9.get("target_id") == null) {
                  CalcHelper.setTargetId(temp_calc_record, var8.get("target_id"));
               }

               if (var9.get("target_bind") == null) {
                  CalcHelper.setTargetBind(temp_calc_record, var8.get("target_bind"));
               }

               if (var9.get("msg") == null) {
                  CalcHelper.setMsg(temp_calc_record, var8.get("msg"));
               }

               if (CalcHelper.getErrorCode(var0) == null) {
                  CalcHelper.setErrorCode(temp_calc_record, var8.get("errorcode"));
               }

               if (CalcHelper.getErrorCodeLevel(var0) == null) {
                  CalcHelper.setErrorCodeLevel(temp_calc_record, var8.get("errorlevel"));
               }

               if (var9.get("msglevel") == null) {
                  CalcHelper.setErrorLevel(temp_calc_record, var8.get("msglevel"));
               }

               if (var9.get("role") == null) {
                  CalcHelper.setRole(temp_calc_record, var8.get("role"));
               }

               if (var9.get("cexp") == null) {
                  CalcHelper.setExp(temp_calc_record, (ExpClass)var8.get("cexp"));
               }

               if (var9.get("id") == null) {
                  CalcHelper.setId(temp_calc_record, var8.get("id"));
               }
            } else if (var1 instanceof Hashtable) {
               var11 = (Map)var1;
               if (var9.get("on_event") == null) {
                  CalcHelper.setEvent(temp_calc_record, var11.get("on_event"));
               }

               if (var9.get("target_type") == null) {
                  CalcHelper.setTargetType(temp_calc_record, var11.get("target_type"));
               }

               if (var9.get("target_id") == null) {
                  CalcHelper.setTargetId(temp_calc_record, var11.get("target_id"));
               }

               if (var9.get("target_bind") == null) {
                  CalcHelper.setTargetBind(temp_calc_record, var11.get("target_bind"));
               }

               if (var9.get("msg") == null) {
                  CalcHelper.setMsg(temp_calc_record, var11.get("msg"));
               }

               if (CalcHelper.getErrorCode(var0) == null) {
                  CalcHelper.setErrorCode(temp_calc_record, var11.get("errorcode"));
               }

               if (CalcHelper.getErrorCodeLevel(var0) == null) {
                  CalcHelper.setErrorCodeLevel(temp_calc_record, var11.get("errorlevel"));
               }

               if (var9.get("msglevel") == null) {
                  CalcHelper.setErrorLevel(temp_calc_record, var11.get("msglevel"));
               }

               if (var9.get("role") == null) {
                  CalcHelper.setRole(temp_calc_record, var11.get("role"));
               }

               if (var9.get("cexp") == null) {
                  CalcHelper.setExp(temp_calc_record, (ExpClass)var11.get("cexp"));
               }

               if (var9.get("id") == null) {
                  CalcHelper.setId(temp_calc_record, var11.get("id"));
               }
            } else if (var1 instanceof Object[]) {
               if (var9.get("on_event") == null) {
                  CalcHelper.setEvent(temp_calc_record, CalcHelper.getEvent(var1));
               }

               if (var9.get("target_type") == null) {
                  CalcHelper.setTargetType(temp_calc_record, CalcHelper.getTargetType(var1));
               }

               if (var9.get("target_id") == null) {
                  CalcHelper.setTargetId(temp_calc_record, CalcHelper.getTargetId(var1));
               }

               if (var9.get("target_bind") == null) {
                  CalcHelper.setTargetBind(temp_calc_record, CalcHelper.getTargetBind(var1));
               }

               if (var9.get("msg") == null) {
                  CalcHelper.setMsg(temp_calc_record, CalcHelper.getMsg(var1));
               }

               if (var9.get("errorcode") == null) {
                  CalcHelper.setErrorCode(temp_calc_record, CalcHelper.getErrorCode(var1));
               }

               if (var9.get("errorlevel") == null) {
                  CalcHelper.setErrorCodeLevel(temp_calc_record, CalcHelper.getErrorCodeLevel(var1));
               }

               if (var9.get("msglevel") == null) {
                  CalcHelper.setErrorLevel(temp_calc_record, CalcHelper.getErrorLevel(var1));
               }

               if (var9.get("role") == null) {
                  CalcHelper.setRole(temp_calc_record, CalcHelper.getRole(var1));
               }

               if (var9.get("cexp") == null) {
                  CalcHelper.setExp(temp_calc_record, CalcHelper.getExp(var1));
               }

               if (var9.get("id") == null) {
                  CalcHelper.setId(temp_calc_record, CalcHelper.getId(var1));
               }
            } else {
               var6 = false;
            }
         }

         if (var6) {
            if (CalcHelper.isCalcRecord(var0)) {
               CalcHelper.copy(temp_calc_record, var0, false);
            } else if (var0 instanceof Map) {
               var11 = (Map)var0;
               String var10;
               if ((var10 = CalcHelper.getEvent(temp_calc_record)) != null) {
                  var11.put("on_event", var10);
               }

               if ((var10 = CalcHelper.getTargetType(temp_calc_record)) != null) {
                  var11.put("target_type", var10);
               }

               if ((var10 = CalcHelper.getTargetId(temp_calc_record)) != null) {
                  var11.put("target_id", var10);
               }

               Boolean var12;
               if ((var12 = CalcHelper.getTargetBind(temp_calc_record)) != null) {
                  var11.put("target_bind", var12);
               }

               if ((var10 = CalcHelper.getMsg(temp_calc_record)) != null) {
                  var11.put("msg", var10);
               }

               if ((var10 = CalcHelper.getErrorCode(temp_calc_record)) != null) {
                  var11.put("errorcode", var10);
               }

               if ((var10 = CalcHelper.getErrorCodeLevel(temp_calc_record)) != null) {
                  var11.put("errorlevel", var10);
               }

               Integer var13;
               if ((var13 = CalcHelper.getErrorLevel(temp_calc_record)) != null) {
                  var11.put("msglevel", var13);
               }

               if ((var13 = CalcHelper.getRole(temp_calc_record)) != null) {
                  var11.put("role", var13);
               }

               ExpClass var14;
               if ((var14 = CalcHelper.getExp(temp_calc_record)) != null) {
                  var11.put("cexp", var14);
               }

               if ((var10 = CalcHelper.getId(temp_calc_record)) != null) {
                  var11.put("id", var10);
               }
            }
         }
      }

      long var4 = System.currentTimeMillis();
      if (show_times && var4 - var2 > 0L) {
         System.out.println("calculator.populateCalcRecord : " + (var4 - var2));
      }

      return var0;
   }

   private static void classifyCalcRecord(Object var0, Vector var1, Hashtable var2) {
      long var3 = System.currentTimeMillis();
      var1.add(var0);
      addToCacheMap(var0, var2);
      long var5 = System.currentTimeMillis();
      if (show_times && var5 - var3 > 0L) {
         System.out.println("calculator.classifyCalcRecord: " + (var5 - var3));
      }

   }

   public static void normalize() throws Exception {
      full_collection = new Object[all_calc.size()];
      addClassToCollection(all_calc);
      all_calc.clear();
      is_xml_processed = true;
   }

   public static void release() {
      full_collection = null;
      maps = null;
      calculator = null;
      gui_info = null;
      functions = null;
      variables = null;
   }

   private static void addClassToCollection(Vector var0) {
      int var1 = 0;

      for(int var2 = var0.size(); var1 < var2; ++var1) {
         full_collection[var1] = var0.get(var1);
      }

   }

   public static Object getEventsCollection(Object var0) {
      if (maps == null) {
         return null;
      } else {
         Hashtable var1 = (Hashtable)maps.get("on_event");
         return var1 == null ? null : var1.get(var0);
      }
   }

   public static Object getTargetsCollection(Object var0) {
      if (maps == null) {
         return null;
      } else {
         Hashtable var1 = (Hashtable)maps.get("target_id");
         return var1 == null ? null : var1.get(var0);
      }
   }

   public static Object getFullCollection() {
      return is_xml_processed && full_collection != null ? full_collection : null;
   }

   public static Object getCacheMaps() {
      return is_xml_processed && full_collection != null ? maps : null;
   }

   public static int getCollectionSize(Object var0) {
      if (var0 instanceof Object[]) {
         return ((Object[])((Object[])var0)).length;
      } else {
         return var0 instanceof Vector ? ((Vector)var0).size() : 0;
      }
   }

   public static void setToSerialization() {
      walkOverCalcs(1);
   }

   public static void setToUsing() {
      walkOverCalcs(2);
   }

   public static void setToUsingByDiscover() {
      calculator = ExpFactory.calculator;
      gui_info = ExpFactory.gui;
      functions = ExpFactory.functions;
      variables = ExpFactory.variables;

      try {
         walkOverCalcs(6);
      } catch (Exception var4) {
         var4.printStackTrace();
      } finally {
         calculator = null;
         gui_info = null;
         functions = null;
         variables = null;
      }

   }

   private static void walkOverCalcs(int var0) {
      int var1 = 0;

      for(int var2 = full_collection.length; var1 < var2; ++var1) {
         Object[] var3 = (Object[])((Object[])full_collection[var1]);
         ExpClass var4 = CalcHelper.getExp(var3);
         if (var4 != null) {
            replaceExpCriticalRefs(var4, var0);
         }
      }

   }

   private static void replaceExpCriticalRefs(ExpClass var0, int var1) {
      Object var2 = var0.getSource();
      int var3;
      if ((var1 & 1) == 1) {
         if (var2 != null) {
            var0.setSource(new ObjectReplacer("function_set", var2));
         }
      } else if ((var1 & 2) == 2) {
         if ((var1 & 4) == 4) {
            if (var2 != null) {
               var3 = var0.getExpType();
               String var4 = var0.getIdentifier();
               if (var3 == 3) {
                  Object var5 = FnDescHelper.getSource(functions.get(var4));
                  var0.setSource(var5);
               } else if (var3 == 2) {
                  var0.setSource(variables);
               }
            }
         } else if (var2 != null) {
            var0.setSource(((ObjectReplacer)var2).getObject());
         }
      }

      var3 = 0;

      for(int var6 = var0.getParametersCount(); var3 < var6; ++var3) {
         replaceExpCriticalRefs(var0.getParameter(var3), var1);
      }

   }

   public static Object getSavableObject() {
      Vector var0 = new Vector(3);
      var0.add(full_collection);
      var0.add(maps);
      return var0;
   }

   public static boolean setLoadedObject(Object var0) {
      boolean var1 = false;
      is_xml_processed = false;
      if (var0 instanceof Vector) {
         Vector var2 = (Vector)var0;
         if (var2.size() == 3) {
            Object var3 = var2.get(0);
            Object var4 = var2.get(1);
            Object var5 = var2.get(2);
            if (var3 instanceof Object[] && var4 instanceof IPropertyList && var5 instanceof Hashtable) {
               full_collection = (Object[])((Object[])var3);
               maps = (Hashtable)var5;
               is_xml_processed = true;
               var1 = true;
            }
         }
      }

      return var1;
   }

   public static Object getFilteredCollection(Object var0, String var1, Object var2) {
      Object[] var3 = new Object[0];
      if (var0 != null && (var0 instanceof Object[] || var0 instanceof Vector)) {
         Vector var5 = new Vector(4096, 4096);
         Object[] var4;
         int var8;
         int var9;
         if (var0 instanceof Object[]) {
            Object[] var10 = (Object[])((Object[])var0);
            var8 = 0;

            for(var9 = var10.length; var8 < var9; ++var8) {
               var4 = CalcHelper.getCalcRecordtype(var10[var8]);
               checkMatches(var4, var1, var2, var5);
            }
         } else {
            Vector var7 = (Vector)var0;
            var8 = 0;

            for(var9 = var7.size(); var8 < var9; ++var8) {
               var4 = CalcHelper.getCalcRecordtype(var7.get(var8));
               checkMatches(var4, var1, var2, var5);
            }
         }

         int var6 = var5.size();
         var3 = new Object[var6];
         var5.toArray(var3);
      }

      return var3;
   }

   private static void checkMatches(Object[] var0, String var1, Object var2, Vector var3) {
      Object var4;
      if ("on_event".equalsIgnoreCase(var1)) {
         var4 = CalcHelper.getEvent(var0);
      } else if ("target_type".equalsIgnoreCase(var1)) {
         var4 = CalcHelper.getTargetType(var0);
      } else if ("target_id".equalsIgnoreCase(var1)) {
         var4 = CalcHelper.getTargetId(var0);
      } else if ("target_bind".equalsIgnoreCase(var1)) {
         var4 = CalcHelper.getTargetBind(var0);
      } else if ("msg".equalsIgnoreCase(var1)) {
         var4 = CalcHelper.getMsg(var0);
      } else if ("errorcode".equalsIgnoreCase(var1)) {
         var4 = CalcHelper.getErrorCode(var0);
      } else if ("errorlevel".equalsIgnoreCase(var1)) {
         var4 = CalcHelper.getErrorCodeLevel(var0);
      } else if ("msglevel".equalsIgnoreCase(var1)) {
         var4 = CalcHelper.getErrorLevel(var0);
      } else if ("role".equalsIgnoreCase(var1)) {
         var4 = CalcHelper.getRole(var0);
      } else if ("cexp".equalsIgnoreCase(var1)) {
         var4 = CalcHelper.getExp(var0);
      } else if ("id".equalsIgnoreCase(var1)) {
         var4 = CalcHelper.getId(var0);
      } else {
         var4 = null;
      }

      boolean var5;
      if (var2 instanceof String) {
         var5 = ((String)var2).equalsIgnoreCase(var4 == null ? null : var4.toString());
      } else if (var2 == null) {
         var5 = var2 == var4;
      } else {
         var5 = var2.equals(var4);
      }

      if (var5) {
         var3.add(var0);
      }

   }

   public static Object getFilteredCollection(Object var0, String[] var1, Object[] var2) {
      Object var3 = new Object[0];
      if (var1 != null && var2 != null && var1.length == var2.length) {
         var3 = var0;
         int var4 = 0;

         for(int var5 = var1.length; var4 < var5; ++var4) {
            var3 = getFilteredCollection(var3, var1[var4], var2[var4]);
         }
      }

      return var3;
   }

   public static Object getFilteredCollection2(Object var0, Object var1, String[] var2, Object[] var3) {
      Object var4 = new Object[0];
      if (var2 != null && var3 != null && var2.length == var3.length) {
         Hashtable var5 = (Hashtable)var1;
         Vector var6 = null;
         int var7 = 0;

         for(int var8 = var2.length; var7 < var8; ++var7) {
            if (var5 != null) {
               Hashtable var9 = (Hashtable)var5.get(var2[var7]);
               if (var9 != null) {
                  Vector var10 = (Vector)var9.get(var3[var7]);
                  if (var10 != null) {
                     if (var6 == null) {
                        var6 = var10;
                     } else if (var10.size() < var6.size()) {
                        var6 = var10;
                     }
                  }
               }
            }
         }

         Object var11;
         if (var6 == null) {
            var11 = var0;
         } else {
            var11 = var6;
         }

         var4 = getFilteredCollection(var11, var2, var3);
      }

      return var4;
   }

   public static boolean isFieldOnEvent(ExpStore var0, String var1, String var2, String var3) {
      try {
         if (var0 != null) {
            Hashtable var4 = var0.getExtendedCacheMaps();
            if (var4 == null) {
               var4 = new Hashtable();
               var0.setExtendedCacheMaps(var4);
            }

            Hashtable var5 = (Hashtable)var4.get(var2 + "_" + var3);
            if (var5 == null) {
               var5 = new Hashtable();
               Object var6 = var0.getCacheMaps();
               if (var6 != null) {
                  Hashtable var7 = (Hashtable)((Hashtable)var6).get(var2);
                  if (var7 != null) {
                     Vector var8 = (Vector)var7.get(var3);
                     if (var8 != null) {
                        for(int var9 = 0; var9 < var8.size(); ++var9) {
                           Object var10 = var8.elementAt(var9);
                           var5.put(CalcHelper.getTargetId(var10), "");
                        }
                     }
                  }

                  var4.put(var2 + "_" + var3, var5);
               }
            }

            return var5.containsKey(var1);
         } else {
            return false;
         }
      } catch (Exception var11) {
         Tools.eLog(var11, 1);
         return false;
      }
   }

   private static void addToCacheMap(Object var0, Hashtable var1) {
      checkAddToCacheMap(var1, var0, "on_event", CalcHelper.getEvent(var0));
      checkAddToCacheMap(var1, var0, "target_id", CalcHelper.getTargetId(var0));
   }

   private static void checkAddToCacheMap(Hashtable var0, Object var1, String var2, Object var3) {
      Vector var4 = checkCacheMapValue(var3, checkCacheMap(var2, var0, 2), 20);
      if (var4 != null) {
         var4.add(var1);
      }

   }

   private static Hashtable checkCacheMap(Object var0, Hashtable var1, int var2) {
      Hashtable var3;
      if (var0 != null && var1 != null) {
         Hashtable var4 = (Hashtable)var1.get(var0);
         if (var4 == null) {
            var4 = new Hashtable(var2);
            var1.put(var0, var4);
         }

         var3 = var4;
      } else {
         var3 = null;
      }

      return var3;
   }

   private static Vector checkCacheMapValue(Object var0, Hashtable var1, int var2) {
      Vector var3;
      if (var0 != null && var1 != null) {
         Vector var4 = (Vector)var1.get(var0);
         if (var4 == null) {
            var4 = new Vector(var2, 2048);
            var1.put(var0, var4);
         }

         var3 = var4;
      } else {
         var3 = null;
      }

      return var3;
   }
}
