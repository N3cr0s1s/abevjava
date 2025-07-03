package hu.piller.enykp.alogic.metainfo;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

public class MetaStore {
   private Hashtable maps;
   private Hashtable cached_tables;

   public void setMaps(Hashtable var1) {
      this.maps = var1;
   }

   public void addFieldMeta(String var1, String var2, String var3) {
      Hashtable var4 = (Hashtable)this.maps.get("fid");
      if (var2 != null && var3 != null) {
         Object var5 = var4.get(var1);
         if (var5 instanceof Map) {
            ((Map)var5).put(var2, var3);
         } else if (var5 == null) {
            Hashtable var6 = new Hashtable();
            var6.put(var2, var3);
            var4.put(var1, var6);
         }
      }

   }

   public Hashtable getFieldMetas() {
      return (Hashtable)this.maps.get("fid");
   }

   public Hashtable getRowMetas() {
      return (Hashtable)this.maps.get("row");
   }

   public Hashtable getColMetas() {
      return (Hashtable)this.maps.get("col");
   }

   public Vector getFieldIds() {
      Hashtable var1 = (Hashtable)this.maps.get("fid");
      Vector var2 = new Vector(var1.size());
      Enumeration var3 = var1.keys();

      while(var3.hasMoreElements()) {
         var2.add(var3.nextElement());
      }

      Object[] var4 = var2.toArray();
      Arrays.sort(var4);
      var2.setSize(0);
      var2.addAll(Arrays.asList(var4));
      return var2;
   }

   public String getFieldMeta(Object var1, Object var2) {
      Hashtable var3 = (Hashtable)this.maps.get("fid");
      Object var4 = var3.get(var1);
      if (var4 instanceof Map) {
         var4 = ((Map)var4).get(var2);
         return var4 == null ? "" : var4.toString();
      } else {
         return "";
      }
   }

   public Map getFieldMetas(Object var1) {
      Hashtable var2 = (Hashtable)this.maps.get("fid");
      Object var3 = var2.get(var1);
      return var3 instanceof Map ? new Hashtable((Map)var3) : new Hashtable();
   }

   public String getPageMeta(Object var1, Object var2) {
      Hashtable var3 = (Hashtable)this.maps.get("pid");
      Object var4 = var3.get(var1);
      if (var4 instanceof Map) {
         var4 = ((Map)var4).get(var2);
         return var4 == null ? "" : var4.toString();
      } else {
         return "";
      }
   }

   public Map getPageMetas(Object var1) {
      Hashtable var2 = (Hashtable)this.maps.get("pid");
      Object var3 = var2.get(var1);
      return var3 instanceof Map ? new Hashtable((Map)var3) : new Hashtable();
   }

   public String getDidMeta(Object var1, Object var2) {
      Hashtable var3 = (Hashtable)this.maps.get("did");
      Object var4 = var3.get(var1);
      if (var4 instanceof Map) {
         var4 = ((Map)var4).get(var2);
         return var4 == null ? "" : var4.toString();
      } else {
         return "";
      }
   }

   public Map getDidMetas(Object var1) {
      Hashtable var2 = (Hashtable)this.maps.get("did");
      Object var3 = var2.get(var1);
      return var3 instanceof Map ? new Hashtable((Map)var3) : new Hashtable();
   }

   public Vector getFilteredFieldMetas_And(Hashtable var1) {
      Hashtable var2 = this.preSelect((Hashtable)this.maps.get("fid"), "fid", var1);
      return this.getMetas_And(var2, var1);
   }

   public Vector getFilteredFieldMetas_And(Vector var1) {
      return this.getMetas_And((Hashtable)this.maps.get("fid"), var1);
   }

   public Vector getFilteredFieldMetas_Or(Vector var1) {
      return this.getMetas_Or((Hashtable)this.maps.get("fid"), var1);
   }

   private Hashtable preSelect(Hashtable var1, String var2, Hashtable var3) {
      Hashtable var4 = null;
      Hashtable var6 = new Hashtable(var1.size());
      Iterator var7 = var3.entrySet().iterator();

      while(true) {
         Object var11;
         do {
            Hashtable var5;
            Object var10;
            String var15;
            do {
               if (!var7.hasNext()) {
                  if (var4 == null) {
                     var4 = var1;
                  }

                  return var4;
               }

               Entry var8 = (Entry)var7.next();
               Object var9 = var8.getKey();
               var10 = var8.getValue();
               var15 = var9.toString();
               var5 = (Hashtable)this.maps.get(var15);
            } while(var5 == null);

            var15 = var10.toString();
            var11 = var5.get(var15);
         } while(var11 == null);

         Vector var13;
         if (var11 instanceof Vector) {
            var13 = (Vector)var11;
         } else {
            var13 = new Vector(1);
            var13.add(var11);
         }

         Vector var14 = null;
         int var16 = 0;

         for(int var17 = var13.size(); var16 < var17; ++var16) {
            var11 = var13.get(var16);
            Object var12 = ((Hashtable)var11).get(var2);
            if (var12 != null) {
               String var18 = var11.toString();
               if (var6.get(var18) == null) {
                  var6.put(var18, "");
                  if (var4 == null) {
                     var4 = new Hashtable(var1.size());
                  }

                  if (var17 > 1) {
                     if (var14 == null) {
                        Object var19 = var4.get(var12);
                        if (var19 instanceof Vector) {
                           var14 = (Vector)var19;
                        } else {
                           var14 = new Vector(var17);
                           if (var19 != null) {
                              var14.add(var19);
                           }

                           var4.put(var12, var14);
                        }
                     }

                     var14.add(var11);
                  } else {
                     var4.put(var12, var11);
                  }
               }
            }
         }
      }
   }

   private Vector getMetas_And(Hashtable var1, Hashtable var2) {
      if (var2 != null && var2.size() > 0) {
         Vector var9 = new Vector(1);
         Vector var11 = new Vector(16, 16);
         var9.add((Object)null);
         Iterator var18 = var1.entrySet().iterator();

         while(var18.hasNext()) {
            Entry var21 = (Entry)var18.next();
            Object var14 = var21.getValue();
            Vector var23;
            if (var14 instanceof Vector) {
               var23 = (Vector)var14;
            } else {
               var23 = var9;
               var9.set(0, var14);
            }

            int var15 = 0;

            for(int var16 = var23.size(); var15 < var16; ++var15) {
               Map var22 = (Map)var23.get(var15);
               Iterator var17 = var2.entrySet().iterator();
               boolean var10 = true;

               label74:
               while(true) {
                  while(true) {
                     if (!var17.hasNext()) {
                        break label74;
                     }

                     Entry var19 = (Entry)var17.next();
                     Object var12 = var22.get(var19.getKey());
                     if (var12 == null) {
                        var10 = false;
                        break label74;
                     }

                     Object var13 = var19.getValue();
                     if (var12 instanceof String && var13 instanceof String) {
                        if (!((String)var12).equalsIgnoreCase((String)var13)) {
                           var10 = false;
                           break label74;
                        }
                     } else if (!var12.equals(var13)) {
                        var10 = false;
                        break label74;
                     }
                  }
               }

               if (var10) {
                  var11.add(var22);
               }
            }
         }

         return var11;
      } else {
         Vector var3 = new Vector(Arrays.asList(var1.values().toArray()));
         int var4 = var3.size();

         Object var5;
         for(int var6 = 0; var6 < var4; ++var6) {
            var5 = var3.get(var6);
            if (var5 instanceof Vector) {
               var4 += ((Vector)var5).size() - 1;
            }
         }

         Vector var20 = new Vector(var4);
         int var7 = 0;

         for(int var8 = var3.size(); var7 < var8; ++var7) {
            var5 = var3.get(var7);
            if (var5 instanceof Vector) {
               var20.addAll((Vector)var5);
            } else {
               var20.add(var5);
            }
         }

         return var20;
      }
   }

   private Vector getMetas_And(Hashtable var1, Vector var2) {
      if (var2 != null && var2.size() > 0) {
         Vector var12 = new Vector(16, 16);
         Iterator var4 = var1.entrySet().iterator();

         while(var4.hasNext()) {
            Entry var6 = (Entry)var4.next();
            Map var10 = (Map)var6.getValue();
            Iterator var5 = var10.entrySet().iterator();

            while(var5.hasNext()) {
               Entry var7 = (Entry)var5.next();
               String var8 = (String)var7.getKey();
               Iterator var3 = var2.iterator();
               boolean var11 = true;

               while(var3.hasNext()) {
                  String var9 = (String)var3.next();
                  if (!var8.equalsIgnoreCase(var9)) {
                     var11 = false;
                     break;
                  }
               }

               if (var11 && !var12.contains(var10)) {
                  var12.add(var10);
               }
            }
         }

         return var12;
      } else {
         return new Vector(Arrays.asList(var1.values().toArray()));
      }
   }

   private Vector getMetas_Or(Hashtable var1, Hashtable var2) {
      if (var2 != null && var2.size() > 0) {
         Vector var9 = new Vector(1);
         Vector var11 = new Vector(16, 16);
         var9.add((Object)null);
         Iterator var18 = var1.entrySet().iterator();

         while(var18.hasNext()) {
            Entry var21 = (Entry)var18.next();
            Object var14 = var21.getValue();
            Vector var23;
            if (var14 instanceof Vector) {
               var23 = (Vector)var14;
            } else {
               var23 = var9;
               var9.set(0, var14);
            }

            int var15 = 0;

            for(int var16 = var23.size(); var15 < var16; ++var15) {
               Map var22 = (Map)var23.get(var15);
               Iterator var17 = var2.entrySet().iterator();
               boolean var10 = false;

               label74:
               while(true) {
                  while(true) {
                     if (!var17.hasNext()) {
                        break label74;
                     }

                     Entry var19 = (Entry)var17.next();
                     Object var12 = var22.get(var19.getKey());
                     if (var12 == null) {
                        var10 = false;
                        break label74;
                     }

                     Object var13 = var19.getValue();
                     if (var12 instanceof String && var13 instanceof String) {
                        if (((String)var12).equalsIgnoreCase((String)var13)) {
                           var10 = true;
                           break label74;
                        }
                     } else if (var12.equals(var13)) {
                        var10 = true;
                        break label74;
                     }
                  }
               }

               if (var10) {
                  var11.add(var22);
               }
            }
         }

         return var11;
      } else {
         Vector var3 = new Vector(Arrays.asList(var1.values().toArray()));
         int var4 = var3.size();

         Object var5;
         for(int var6 = 0; var6 < var4; ++var6) {
            var5 = var3.get(var6);
            if (var5 instanceof Vector) {
               var4 += ((Vector)var5).size() - 1;
            }
         }

         Vector var20 = new Vector(var4);
         int var7 = 0;

         for(int var8 = var3.size(); var7 < var8; ++var7) {
            var5 = var3.get(var7);
            if (var5 instanceof Vector) {
               var20.addAll((Vector)var5);
            } else {
               var20.add(var5);
            }
         }

         return var20;
      }
   }

   private Vector getMetas_Or(Hashtable var1, Vector var2) {
      if (var2 != null && var2.size() > 0) {
         Vector var12 = new Vector(16, 16);
         Iterator var4 = var1.entrySet().iterator();

         while(var4.hasNext()) {
            Entry var6 = (Entry)var4.next();
            Map var10 = (Map)var6.getValue();
            Iterator var5 = var10.entrySet().iterator();

            while(var5.hasNext()) {
               Entry var7 = (Entry)var5.next();
               String var8 = (String)var7.getKey();
               Iterator var3 = var2.iterator();
               boolean var11 = false;

               while(var3.hasNext()) {
                  String var9 = (String)var3.next();
                  if (var8.equalsIgnoreCase(var9)) {
                     var11 = true;
                     break;
                  }
               }

               if (var11 && !var12.contains(var10)) {
                  var12.add(var10);
               }
            }
         }

         return var12;
      } else {
         return new Vector(Arrays.asList(var1.values().toArray()));
      }
   }

   public String toString() {
      return this.hashCode() + "" + this.maps;
   }

   public void release() {
      this.maps.clear();
      this.cached_tables = null;
   }

   public Hashtable getFieldAttributes(String var1, boolean var2) {
      if (var1 != null && var1.length() != 0) {
         if (this.cached_tables == null) {
            this.cached_tables = new Hashtable();
         }

         Hashtable var3 = (Hashtable)this.cached_tables.get(var1);
         if (var3 == null) {
            var3 = new Hashtable();
            this.cached_tables.put(var1, var3);
         }

         Hashtable var4 = (Hashtable)var3.get(var2);
         if (var4 == null) {
            try {
               Hashtable var5 = this.getFieldMetas();
               var4 = new Hashtable(var5.size());
               var3.put(var2, var4);
               Enumeration var6 = var5.keys();

               while(true) {
                  String var7;
                  String var10;
                  do {
                     Object var9;
                     do {
                        if (!var6.hasMoreElements()) {
                           return var4;
                        }

                        var7 = (String)var6.nextElement();
                        Hashtable var8 = (Hashtable)var5.get(var7);
                        var9 = var8.get(var1);
                     } while(var9 == null);

                     var10 = var9.toString().trim();
                  } while(var10.length() == 0 && var2);

                  var4.put(var7, var10);
               }
            } catch (Exception var11) {
               var11.printStackTrace();
            }
         }

         return var4;
      } else {
         return null;
      }
   }
}
