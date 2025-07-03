package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.interfaces.IPropertyList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;

public class ExpStoreFactory {
   public static Calculator calculator;
   public static final Vector calculatable_field_ids = new Vector(4096, 4096);
   public static final Vector checkable_field_ids = new Vector(4096, 4096);
   public static boolean break_cicle_references = true;
   private static Hashtable read_dep_list;
   private static final Object cfd_sync = new Object();

   public static void reset() {
      calculatable_field_ids.removeAllElements();
      checkable_field_ids.removeAllElements();
   }

   public static IPropertyList createMetaStore(IPropertyList var0, Object var1) {
      Vector var2;
      if ((var2 = getDataVector(var1)) != null) {
         int var4 = 0;

         for(int var5 = var2.size(); var4 < var5; ++var4) {
            Hashtable var3;
            if ((var3 = getAttributes(var2.get(var4))) != null) {
               var0.set(var3.get("fid"), new Hashtable(var3));
            }
         }
      }

      return var0;
   }

   public static IPropertyList createFieldTypesStore(IPropertyList var0, Object var1) {
      if (var1 instanceof Vector) {
         createFieldTypesStore(var0, (Vector)var1);
      } else {
         Vector var2;
         if ((var2 = getDataVector(var1)) != null) {
            createFieldTypesStore(var0, var2);
         }
      }

      return var0;
   }

   private static void createFieldTypesStore(IPropertyList var0, Vector var1) {
      int var3 = 0;

      for(int var4 = var1.size(); var3 < var4; ++var3) {
         Hashtable var2;
         if ((var2 = getAttributes(var1.get(var3))) != null) {
            Object var5 = var2.get("type");
            if (var5 != null) {
               String var6 = var5.toString().trim();
               if (var6.length() != 0) {
                  var0.set(var2.get("fid"), var6);
               }
            }
         }
      }

   }

   public static Object createMatricesStore(Object[] var0, Object var1) {
      int var6 = 0;
      Vector var2;
      if ((var2 = getDataVector(var1)) != null) {
         int var7 = 0;

         for(int var8 = var2.size(); var7 < var8; ++var7) {
            Object var3;
            if ((var3 = var2.get(var7)) instanceof Object[] && getTagName(var3).equalsIgnoreCase("matrix")) {
               Object var4;
               Hashtable var5;
               if ((var5 = getAttributes(var3)) != null) {
                  var4 = var5.get("name");
               } else {
                  var4 = null;
               }

               var0[var6++] = new Object[]{var4 == null ? "" : var4.toString(), createMatrixStore(new Object[getDataVector(var3).size()], var3)};
            }
         }
      }

      return var0;
   }

   private static Object createMatrixStore(Object[] var0, Object var1) {
      int var6 = 0;
      Vector var2;
      if ((var2 = getDataVector(var1)) != null) {
         int var7 = 0;

         for(int var8 = var2.size(); var7 < var8; ++var7) {
            Object var3;
            if ((var3 = var2.get(var7)) instanceof Object[]) {
               String var4;
               if ((var4 = getTagName(var3)).equalsIgnoreCase("val")) {
                  Hashtable var5;
                  if ((var5 = getAttributes(var3)) != null) {
                     var0[var6++] = var5.get("value");
                  }
               } else if (var4.equalsIgnoreCase("matrix")) {
                  var0[var6++] = createMatrixStore(new Object[getDataVector(var3).size()], var3);
               }
            }
         }
      }

      return var0;
   }

   public static IPropertyList createVariableStore(IPropertyList var0, Object var1) throws Exception {
      Vector var2;
      if ((var2 = getDataVector(var1)) != null) {
         int var5 = 0;

         Hashtable var3;
         String var4;
         int var6;
         for(var6 = var2.size(); var5 < var6; ++var5) {
            if ((var3 = getAttributes(var2.get(var5))) != null) {
               var4 = ((String)var3.get("name")).toLowerCase().trim();
               var0.set(var4, new ExpClass());
            }
         }

         var5 = 0;

         for(var6 = var2.size(); var5 < var6; ++var5) {
            if ((var3 = getAttributes(var2.get(var5))) != null) {
               var4 = ((String)var3.get("name")).toLowerCase().trim();
               var0.set(var4, ExpFactory.createExp((String)var3.get("value"), var4, 3, false, (String)null));
            }
         }
      }

      return var0;
   }

   public static Object[] createFormCalculationStore(Object[] var0, Object var1) throws Exception {
      Object[] var2 = (Object[])((Object[])var1);
      if (var2 != null) {
         int var3 = 0;

         for(int var4 = var2.length; var3 < var4; ++var3) {
            var0[var3] = var2[var3];
         }
      }

      return var0;
   }

   public static Object[] createFormCheckStore(Object[] var0, Object var1) throws Exception {
      Iterator var2 = CalcFactoryHelper.getCollectionIterator(var1);

      for(int var3 = 0; var2.hasNext(); ++var3) {
         var0[var3] = var2.next();
      }

      return var0;
   }

   public static IPropertyList createPageCheckStore(IPropertyList var0, Object var1, Object var2) throws Exception {
      if (var0 != null && var1 != null && var2 instanceof Hashtable) {
         Hashtable var3 = (Hashtable)((Hashtable)var2).get("target_id");
         String[] var4 = new String[]{"on_event", "target_type"};
         String[] var5 = new String[]{"page_check", "page"};
         Iterator var10 = CalcFactoryHelper.getCollectionIterator(var1);

         for(int var11 = 0; var10.hasNext(); ++var11) {
            String var6 = CalcHelper.getTargetId(var10.next());
            Vector var9 = (Vector)var3.get(var6);
            if (var9 != null) {
               Object var7 = CalcFactory.getFilteredCollection(var9, (String[])var4, (Object[])var5);
               if (var7 instanceof Object[]) {
                  Object[] var8 = (Object[])((Object[])var7);
                  if (var8.length > 0) {
                     var0.set(var6, var8);
                  }
               }
            }
         }
      }

      return var0;
   }

   public static IPropertyList createFieldCalculationStore(IPropertyList var0, Object var1, Object var2) throws Exception {
      if (var0 != null && var1 != null && var2 instanceof Hashtable) {
         Hashtable var3 = new Hashtable(CalcFactory.getCollectionSize(var1));
         Hashtable var4 = (Hashtable)((Hashtable)var2).get("target_id");
         String[] var5 = new String[]{"on_event", "target_type"};
         String[] var6 = new String[]{"field_calc", "field"};
         Iterator var11 = CalcFactoryHelper.getCollectionIterator(var1);

         for(int var12 = 0; var11.hasNext(); ++var12) {
            String var7 = CalcHelper.getTargetId(var11.next());
            Vector var10 = (Vector)var4.get(var7);
            if (var10 != null) {
               Object var8 = CalcFactory.getFilteredCollection(var10, (String[])var5, (Object[])var6);
               if (var8 instanceof Object[]) {
                  Object[] var9 = (Object[])((Object[])var8);
                  if (var9.length > 0) {
                     var0.set(var7, var9);
                  }

                  if (var3.get(var7) == null) {
                     calculatable_field_ids.add(var7);
                     var3.put(var7, "");
                  }
               }
            }
         }
      }

      return var0;
   }

   public static IPropertyList createFieldCheckStore(IPropertyList var0, Object var1, Object var2) throws Exception {
      if (var0 != null && var1 != null && var2 instanceof Hashtable) {
         Hashtable var3 = new Hashtable(CalcFactory.getCollectionSize(var1));
         Hashtable var4 = (Hashtable)((Hashtable)var2).get("target_id");
         String[] var5 = new String[]{"on_event", "target_type"};
         String[] var6 = new String[]{"field_check", "field"};
         Iterator var11 = CalcFactoryHelper.getCollectionIterator(var1);

         for(int var12 = 0; var11.hasNext(); ++var12) {
            String var7 = CalcHelper.getTargetId(var11.next());
            Vector var10 = (Vector)var4.get(var7);
            if (var10 != null) {
               Object var8 = CalcFactory.getFilteredCollection(var10, (String[])var5, (Object[])var6);
               if (var8 instanceof Object[]) {
                  Object[] var9 = (Object[])((Object[])var8);
                  if (var9.length > 0) {
                     var0.set(var7, var9);
                  }

                  if (var3.get(var7) == null) {
                     checkable_field_ids.add(var7);
                     var3.put(var7, "");
                  }
               }
            }
         }
      }

      return var0;
   }

   public static IPropertyList createLookupCreateStore(IPropertyList var0, Object var1, Object var2) throws Exception {
      if (var0 != null && var1 != null && var2 instanceof Hashtable) {
         new Hashtable(CalcFactory.getCollectionSize(var1));
         Hashtable var4 = (Hashtable)((Hashtable)var2).get("target_id");
         String[] var5 = new String[]{"on_event", "target_type"};
         String[] var6 = new String[]{"lookup_create", "field"};
         Iterator var11 = CalcFactoryHelper.getCollectionIterator(var1);

         for(int var12 = 0; var11.hasNext(); ++var12) {
            String var7 = CalcHelper.getTargetId(var11.next());
            Vector var10 = (Vector)var4.get(var7);
            if (var10 != null) {
               Object var8 = CalcFactory.getFilteredCollection(var10, (String[])var5, (Object[])var6);
               if (var8 instanceof Object[]) {
                  Object[] var9 = (Object[])((Object[])var8);
                  if (var9.length > 0) {
                     var0.set(var7, var9);
                  }
               }
            }
         }
      }

      return var0;
   }

   public static Object[] createCalcualatableFieldStore(Object[] var0) {
      if (calculatable_field_ids != null) {
         int var1 = calculatable_field_ids.size();
         var0 = new Object[var1];

         for(int var2 = 0; var2 < var1; ++var2) {
            var0[var2] = calculatable_field_ids.get(var2);
         }
      }

      return var0;
   }

   public static Object[] createCheckableFieldStore(Object[] var0) {
      if (checkable_field_ids != null) {
         int var1 = checkable_field_ids.size();
         var0 = new Object[var1];

         for(int var2 = 0; var2 < var1; ++var2) {
            var0[var2] = checkable_field_ids.get(var2);
         }
      }

      return var0;
   }

   public static Object[] createFieldStore(Object[] var0) {
      int var1 = 0;
      int var2 = 0;
      if (calculatable_field_ids != null) {
         var1 = calculatable_field_ids.size();
      }

      if (checkable_field_ids != null) {
         var2 = checkable_field_ids.size();
      }

      Vector var5 = new Vector(var1 + var2);
      int var6;
      if (var1 + var2 > 0) {
         Object var4;
         for(var6 = 0; var6 < var1; ++var6) {
            if (!var5.contains(var4 = calculatable_field_ids.get(var6))) {
               var5.add(var4);
            }
         }

         for(var6 = 0; var6 < var2; ++var6) {
            if (!var5.contains(var4 = checkable_field_ids.get(var6))) {
               var5.add(var4);
            }
         }
      }

      int var3 = var5.size();
      if (var3 > 0) {
         var0 = new Object[var3];

         for(var6 = 0; var6 < var3; ++var6) {
            var0[var6] = var5.get(var6);
         }
      }

      return var0;
   }

   public static Vector getDataVector(Object var0) {
      if (var0 instanceof Object[]) {
         Object[] var1 = (Object[])((Object[])var0);
         if (var1.length > 3 && var1[3] instanceof Vector) {
            return (Vector)var1[3];
         }
      }

      return null;
   }

   public static Hashtable getAttributes(Object var0) {
      if (var0 instanceof Object[]) {
         Object[] var1 = (Object[])((Object[])var0);
         Object var2;
         if (var1.length > 1 && (var2 = var1[1]) instanceof Hashtable) {
            return (Hashtable)var2;
         }
      } else if (var0 instanceof Hashtable) {
         return (Hashtable)var0;
      }

      return null;
   }

   public static String getTagName(Object var0) {
      if (var0 instanceof Object[]) {
         Object[] var1 = (Object[])((Object[])var0);
         return (String)var1[0];
      } else {
         return null;
      }
   }

   public static IPropertyList createFieldDependencies(IPropertyList var0, Hashtable var1) throws Exception {
      synchronized(cfd_sync) {
         Hashtable var3 = new Hashtable(ExpFactory.dependency_map.size() + ExpFactory.variable_dependency_map.size());
         var3.putAll(ExpFactory.dependency_map);
         var3.putAll(ExpFactory.variable_dependency_map);
         if (var3.size() > 0) {
            Hashtable var4 = var3;
            var3 = createSimpleDependency(var3);
            var3 = createExtendedDependency(var3, (Hashtable)null);
            createExtendedDependency(var4, (Hashtable)null);
            var1.putAll(var4);
            var3 = createSortedDependency(var3);
            if (var0 == null && calculator != null) {
               var0 = new IPropertyListFastStore(var3.size());
            }

            if (var0 != null) {
               Iterator var5 = var3.entrySet().iterator();

               while(var5.hasNext()) {
                  Entry var6 = (Entry)var5.next();
                  Vector var7 = (Vector)var6.getValue();
                  if (var7 != null && var7.size() > 0) {
                     ((IPropertyList)var0).set(var6.getKey(), var7.toArray());
                  }
               }
            }
         }

         return (IPropertyList)var0;
      }
   }

   public static IPropertyList createPageDependencies(IPropertyList var0, Hashtable var1) throws Exception {
      Hashtable var2 = new Hashtable(ExpFactory.page_dependency_map.size() + ExpFactory.variable_dependency_map.size());
      var2.putAll(ExpFactory.page_dependency_map);
      var2.putAll(ExpFactory.variable_dependency_map);
      if (var2.size() > 0) {
         var2 = createSimpleDependency(var2);
         var2 = createExtendedDependency(var2, (Hashtable)null);
         var2 = createSortedDependency(var2);
         if (var0 == null && calculator != null) {
            var0 = new IPropertyListFastStore(var2.size());
         }

         if (var0 != null) {
            Iterator var3 = var2.entrySet().iterator();

            while(var3.hasNext()) {
               Entry var4 = (Entry)var3.next();
               Vector var5 = (Vector)var4.getValue();
               if (var5 != null && var5.size() > 0) {
                  ((IPropertyList)var0).set(var4.getKey(), var5.toArray());
               }
            }
         }
      }

      return (IPropertyList)var0;
   }

   private static Hashtable createSimpleDependency(Hashtable var0) throws Exception {
      Hashtable var1 = null;
      if (var0 != null) {
         var1 = new Hashtable(var0.size());
         Iterator var2 = var0.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var4 = (Entry)var2.next();
            String var5 = (String)var4.getKey();
            Iterator var3 = ((Vector)var4.getValue()).iterator();

            while(var3.hasNext()) {
               String var6 = (String)var3.next();
               if (var5.equalsIgnoreCase(var6)) {
                  if (!break_cicle_references) {
                     throw new Exception("Körkörös hivatkozás a(z) '" + var5 + "' mezőre !");
                  }
               } else {
                  Object var7;
                  Vector var8;
                  if ((var7 = var1.get(var6)) == null) {
                     var8 = new Vector(32, 32);
                     var1.put(var6, var8);
                  } else {
                     var8 = (Vector)var7;
                  }

                  if (!var8.contains(var5)) {
                     var8.add(var5);
                  }
               }
            }
         }
      }

      return var1;
   }

   private static Hashtable createExtendedDependency(Hashtable var0, Hashtable var1) throws Exception {
      if (var0 != null) {
         Iterator var2 = var0.entrySet().iterator();
         new Integer2();
         read_dep_list = new Hashtable(512);

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            Vector var4 = (Vector)var3.getValue();
            read_dep_list.clear();
            findDependencies(var0, new Vector(var4), var4, read_dep_list);
            if (var1 != null) {
               var1.put(var3.getKey(), new Integer2(var4.size()));
            }
         }
      }

      return var0;
   }

   private static void findDependencies(Hashtable var0, Vector var1, Vector var2, Hashtable var3) throws Exception {
      if (var1 != null && var2 != null) {
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            Object var5 = var4.next();
            if (var3.get(var5) == null) {
               Vector var6 = (Vector)var0.get(var5);
               if (var6 != var2) {
                  if (!var2.contains(var5)) {
                     var2.add(var5);
                  }

                  var3.put(var5, "");
                  findDependencies(var0, var6, var2, var3);
               }
            }
         }
      }

   }

   private static Hashtable createSortedDependency(Hashtable var0) throws Exception {
      Hashtable var1 = null;
      if (var0 != null) {
         var1 = new Hashtable(var0.size());

         Vector var6;
         String var7;
         label81:
         for(Iterator var3 = var0.entrySet().iterator(); var3.hasNext(); var1.put(var7, var6)) {
            Entry var2 = (Entry)var3.next();
            var7 = (String)var2.getKey();
            Vector var4 = (Vector)((Vector)var2.getValue()).clone();
            var6 = new Vector(var4.size());
            int var9 = var4.size() - 1;

            while(true) {
               while(true) {
                  if (var4.size() <= 0) {
                     continue label81;
                  }

                  String var8 = (String)var4.get(var9);
                  Vector var5 = (Vector)var0.get(var8);
                  if (var5 == null) {
                     var6.insertElementAt(var8, 0);
                     var4.remove(var8);
                     var9 = var4.size() - 1;
                  } else if (var5.contains(var7)) {
                     if (!break_cicle_references) {
                        throw new Exception("Körkörös hivatkozás a(z) '" + var7 + "' mezőre !");
                     }

                     var5.remove(var7);
                     System.out.println("Körkörös hivatkozás a(z) '" + var7 + "' mezőre megszakítva !");
                  } else if (var5.contains(var8)) {
                     if (!break_cicle_references) {
                        throw new Exception("Körkörös hivatkozás a(z) '" + var8 + "' mezőre !");
                     }

                     var5.remove(var8);
                     System.out.println("Körkörös hivatkozás a(z) '" + var8 + "' mezőre megszakítva !");
                  } else {
                     Object var11;
                     if (var5.size() > 0 && (var11 = var0.get(var5.get(0))) != null && ((Vector)var11).contains(var8)) {
                        if (!break_cicle_references) {
                           throw new Exception("Körkörös hivatkozás a(z) '" + var8 + "' mezőre !");
                        }

                        var5.remove(0);
                        System.out.println("Körkörös hivatkozás a(z) '" + var8 + "' mezőre megszakítva !");
                     } else {
                        boolean var10 = true;
                        int var12 = var4.size();
                        int var14 = 0;

                        for(int var15 = var5.size(); var14 < var15; ++var14) {
                           int var13;
                           if ((var13 = var4.indexOf(var5.get(var14))) > -1) {
                              if (var13 < var12) {
                                 var12 = var13;
                              }
                           } else if (var14 == 0) {
                              var12 = var4.size();
                           }
                        }

                        if (var12 < var9) {
                           var10 = false;
                           var4.insertElementAt(var4.remove(var9), 0);
                        } else if (var10) {
                           var6.insertElementAt(var8, 0);
                           var4.remove(var8);
                           var9 = var4.size() - 1;
                        } else {
                           var9 = (var9 - 1) % var4.size();
                        }
                     }
                  }
               }
            }
         }
      }

      return var1;
   }

   public static Object[] createFullFieldCalcOrder(Hashtable var0, Object[] var1) {
      Object[] var2 = null;
      if (var0 != null) {
         Hashtable var3 = new Hashtable(var0.size());
         Hashtable var4 = new Hashtable(512);
         var3.putAll(var0);
         Iterator var5 = var3.values().iterator();

         Vector var6;
         while(var5.hasNext()) {
            var6 = (Vector)var5.next();
            int var8 = 0;

            for(int var9 = var6.size(); var8 < var9; ++var8) {
               Object var7 = var6.get(var8);
               var4.put(var7, var7);
            }

            var6.clear();
            var6.addAll(var4.values());
            var4.clear();
         }

         Vector var16 = new Vector(var3.size());
         Vector var17 = new Vector();
         int var12 = Integer.MAX_VALUE;

         int var14;
         int var15;
         for(int var13 = 0; var3.size() > 0; ++var13) {
            var5 = var3.values().iterator();

            for(var14 = 0; var5.hasNext(); ++var14) {
               var6 = (Vector)var5.next();
               if (var14 == 0) {
                  var12 = var6.size();
               } else {
                  var12 = Math.min(var12, var6.size());
               }
            }

            var5 = var3.entrySet().iterator();

            while(var5.hasNext()) {
               Entry var10 = (Entry)var5.next();
               String var11 = (String)var10.getKey();
               var6 = (Vector)var10.getValue();
               if (var6.size() == var12) {
                  var17.add(var11);
               }
            }

            var16.addAll(var17);
            var14 = 0;

            for(var15 = var17.size(); var14 < var15; ++var14) {
               var3.remove(var17.get(var14));
            }

            var5 = var3.values().iterator();

            while(var5.hasNext()) {
               var6 = (Vector)var5.next();
               var6.removeAll(var17);
            }

            var17.clear();
         }

         Vector var18 = new Vector(Arrays.asList(var1));
         var14 = 0;

         for(var15 = var16.size(); var14 < var15; ++var14) {
            if (!var18.contains(var16.get(var14))) {
               var16.removeElementAt(var14--);
               --var15;
            }
         }

         var2 = var16.toArray();
      }

      return var2;
   }

   public static void extendSpecialDependecies(Hashtable var0, Hashtable var1) {
      if (var1 != null) {
         Enumeration var2 = var1.keys();

         while(var2.hasMoreElements()) {
            String var3 = (String)var2.nextElement();
            if (var3.equalsIgnoreCase("dept_type_dinpages")) {
               extendDinPageDept(var0, (Hashtable)var1.get(var3));
            }
         }
      }

   }

   private static void extendDinPageDept(Hashtable var0, Hashtable var1) {
      Enumeration var2 = var1.keys();

      while(var2.hasMoreElements()) {
         String var3 = (String)var2.nextElement();
         String var4 = (String)var1.get(var3);
         Vector var5 = (Vector)var0.get(var3);
         Vector var6;
         if (var5 == null) {
            var6 = new Vector();
            var6.add(var3);
            var0.put(var4, var6);
         } else {
            var6 = new Vector(var5);
            var6.remove(var4);
            var0.put(var4, var6);
         }
      }

   }

   private static void checkCircle(Hashtable<String, Vector<String>> var0) throws Exception {
      boolean var1 = false;
      Iterator var2 = var0.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Vector var4 = new Vector();
         var4.add(var3);
         HashSet var5 = new HashSet();
         var5.add(var3);
         if (checkCircleRecursive(var0, var3, var4, var5)) {
            logCircle(var3, var4);
         }
      }

   }

   private static void logCircle(String var0, Vector<String> var1) {
      System.out.println("Körkörös hivatkozás a '" + var0 + "' kulcsú mező számításának elemzése során!");
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         var2.append((String)var1.elementAt(var3));
         var2.append(",");
      }

      System.out.println("   Az ismétlődést tartalmazó kör: (" + var2.toString() + ")");
   }

   private static boolean checkCircleRecursive(Hashtable<String, Vector<String>> var0, String var1, Vector<String> var2, HashSet<String> var3) {
      Vector var4 = (Vector)var0.get(var1);
      if (var4 == null) {
         return false;
      } else {
         for(int var5 = 0; var5 < var4.size(); ++var5) {
            String var6 = (String)var4.elementAt(var5);
            if (var1.equalsIgnoreCase(var6)) {
               if (var4.size() != 1) {
                  var2.add(var6);
                  return true;
               }
            } else {
               if (var3.contains(var6)) {
                  var2.add(var6);
                  return true;
               }

               var2.add(var6);
               var3.add(var6);
               if (checkCircleRecursive(var0, var6, var2, var3)) {
                  return true;
               }

               var2.remove(var2.lastElement());
               var3.remove(var6);
            }
         }

         return false;
      }
   }
}
