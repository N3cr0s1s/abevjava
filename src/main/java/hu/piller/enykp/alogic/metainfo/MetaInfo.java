package hu.piller.enykp.alogic.metainfo;

import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.EventLog;
import hu.piller.enykp.util.base.Tools;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;

public class MetaInfo {
   private static MetaInfo instance;
   private Hashtable meta_stores;
   public static final String FORMAT_HTML = "html";
   public static final String FORMAT_TXT = "txt";
   public static final int COL_MIN = 64;
   public static final int COL_PERIOD = 26;

   public static MetaInfo getInstance() {
      if (instance == null) {
         instance = new MetaInfo();
      }

      return instance;
   }

   private MetaInfo() {
   }

   public void init() {
      if (this.meta_stores != null) {
         Iterator var2 = this.meta_stores.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var1 = (Entry)var2.next();
            MetaStore var3 = (MetaStore)var1.getValue();
            var3.release();
         }
      }

      this.meta_stores = new Hashtable();
      TemplateUtils.getInstance().init();
   }

   public boolean addForm(String var1, Hashtable var2, Hashtable var3) {
      try {
         if (var2 == null || var3 == null) {
            ErrorList.getInstance().writeError(new Integer(10000), "Hiányoznak a meteadatok!", (Exception)null, (Object)null);
         }

         MetaStore var8 = new MetaStore();
         MetaFactory.createMaps(var2, var3);
         var8.setMaps(MetaFactory.getMaps());
         MetaFactory.release();
         this.meta_stores.put(var1, var8);
      } catch (Exception var7) {
         Exception var4 = var7;

         try {
            EventLog.getInstance().writeLog("Meta adat felolvasási hiba ! " + var4);
            return false;
         } catch (IOException var6) {
            Tools.eLog(var6, 0);
         }
      }

      return true;
   }

   public MetaStore getMetaStore(Object var1) {
      return (MetaStore)this.meta_stores.get(var1);
   }

   public Object getIds(Object var1, Object var2) {
      if (var1 instanceof Object[]) {
         Object var3 = null;
         Object var4 = null;
         Object var5 = null;
         Object[] var6 = (Object[])((Object[])var1);
         int var7 = 0;

         for(int var8 = var6.length; var7 < var8; ++var7) {
            switch(var7) {
            case 0:
               var3 = var6[var7];
               break;
            case 1:
               var4 = var6[var7];
               break;
            case 2:
               var5 = var6[var7];
            }
         }

         if (var3 != null || var4 != null || var5 != null) {
            Hashtable var9 = new Hashtable(3);
            if (var3 != null) {
               var9.put("fid", var3.toString());
            }

            if (var4 != null) {
               var9.put("vid", var4.toString());
            }

            if (var5 != null) {
               var9.put("did", var5.toString());
            }

            MetaStore var12 = this.getMetaStore(var2);
            if (var12 != null) {
               Vector var13 = var12.getFilteredFieldMetas_And(var9);
               if (var13.size() > 0) {
                  Object var10 = var13.get(0);
                  if (var10 instanceof Hashtable) {
                     Hashtable var11 = (Hashtable)var10;
                     return new Object[]{var11.get("fid"), var11.get("vid"), var11.get("did")};
                  }
               }
            }
         }
      }

      return null;
   }

   public Object getRowColByFId(Object var1, Object var2) {
      if (var1 instanceof Object[]) {
         Object var3 = null;
         Object var4 = null;
         Object[] var5 = (Object[])((Object[])var1);
         int var6 = 0;

         for(int var7 = var5.length; var6 < var7; ++var6) {
            switch(var6) {
            case 0:
               var3 = var5[var6];
               break;
            case 1:
               var4 = var5[var6];
            }
         }

         if (var3 != null || var4 != null) {
            Hashtable var8 = new Hashtable(2);
            if (var3 != null) {
               var8.put("fid", var3.toString());
            }

            if (var4 != null) {
               var8.put("vid", var4.toString());
            }

            MetaStore var11 = this.getMetaStore(var2);
            if (var11 != null) {
               Vector var12 = var11.getFilteredFieldMetas_And(var8);
               if (var12.size() > 0) {
                  Object var9 = var12.get(0);
                  if (var9 instanceof Hashtable) {
                     Hashtable var10 = (Hashtable)var9;
                     return new Object[]{var10.get("row"), var10.get("col")};
                  }
               }
            }
         }
      }

      return null;
   }

   public Object getFIdByRowCol(Object var1, Object var2, Object var3, Object var4, BookModel var5) {
      if (var5 != null && (var2 != null || var3 != null) && var1 != null) {
         Hashtable var6 = new Hashtable(2);
         if (var2 != null) {
            var6.put("row", var2.toString());
         }

         if (var3 != null) {
            var6.put("col", var3.toString());
         }

         return this.getFieldIds(var5, var4, var1, var6, false);
      } else {
         return null;
      }
   }

   public static String getPageName(String var0, BookModel var1, String var2) {
      FormModel var3 = var1.get(var2);
      return ((PageModel)var3.fids_page.get(var0)).name;
   }

   public Object getFieldMetas(Object var1) {
      Hashtable var2 = null;
      MetaStore var3 = this.getMetaStore(var1);
      if (var3 != null) {
         Hashtable var4 = var3.getFieldMetas();
         if (var4 != null) {
            var2 = var4;
         }
      }

      return var2;
   }

   public Object getMeta(Object var1, Object var2, Object var3) {
      MetaStore var4 = this.getMetaStore(var3);
      if (var4 != null) {
         return var2 == null ? var4.getFieldMetas(var1) : var4.getFieldMeta(var1, var2);
      } else {
         return null;
      }
   }

   public Object getPageMeta(Object var1, Object var2, Object var3) {
      MetaStore var4 = this.getMetaStore(var3);
      if (var4 != null) {
         return var2 == null ? var4.getPageMetas(var1) : var4.getPageMeta(var1, var2);
      } else {
         return null;
      }
   }

   public Enumeration getMetaStoresIds() {
      return this.meta_stores == null ? null : this.meta_stores.keys();
   }

   public Object cidToDid(Object var1, Object var2) {
      if (var1 != null) {
         if (var2 != null) {
            var2 = var2.toString();
         }

         MetaStore var3 = this.getMetaStore(var2);
         if (var3 != null) {
            if (var1 instanceof String) {
               return var3.getFieldMeta((String)var1, "did");
            }

            if (var1 instanceof Vector) {
               Vector var4 = (Vector)var1;
               int var5 = var4.size();
               Vector var6 = new Vector(var5);

               for(int var7 = 0; var7 < var5; ++var7) {
                  var6.add(this.cidToDid(var4.get(var7), var2));
               }

               return var6;
            }
         }
      }

      return null;
   }

   public Object didToCid(Object var1, Object var2) {
      if (var1 != null) {
         if (var2 != null) {
            var2 = var2.toString();
         }

         MetaStore var3 = this.getMetaStore(var2);
         if (var3 != null) {
            if (var1 instanceof String) {
               return var3.getDidMeta((String)var1, "fid");
            }

            if (var1 instanceof Vector) {
               Vector var4 = (Vector)var1;
               int var5 = var4.size();
               Vector var6 = new Vector(var5);

               for(int var7 = 0; var7 < var5; ++var7) {
                  var6.add(this.didToCid(var4.get(var7), var2));
               }

               return var6;
            }
         }
      }

      return null;
   }

   public Hashtable getFieldAttributes(String var1, String var2, boolean var3) {
      if (var1 != null && var1.length() != 0) {
         MetaStore var4 = this.getMetaStore(var1);
         return var4 != null ? var4.getFieldAttributes(var2, var3) : null;
      } else {
         return null;
      }
   }

   public Object getFieldIds(BookModel var1, Object var2, Object var3, Hashtable var4, boolean var5) {
      MetaStore var7 = this.getMetaStore(var2);
      if (var7 != null) {
         Vector var6 = var7.getFilteredFieldMetas_And(var4);
         int var8 = var6.size();
         if (var8 > 0) {
            Vector var9 = new Vector(var8);
            int var11 = 0;

            for(int var12 = var6.size(); var11 < var12; ++var11) {
               Object var10 = var6.get(var11);
               if (var10 instanceof Hashtable) {
                  Hashtable var13 = (Hashtable)var10;
                  var9.add(new Object[]{var13.get("fid"), var13.get("vid")});
               }
            }

            if (var9.size() > 0) {
               Vector var17 = new Vector(var9.size());
               int var18 = 0;

               for(int var14 = var9.size(); var18 < var14; ++var18) {
                  Object[] var16 = (Object[])((Object[])var9.get(var18));
                  String var15 = getPageName((String)var16[0], var1, (String)var2);
                  if (var3.toString().equalsIgnoreCase(var15)) {
                     var17.add(var16);
                  }
               }

               switch(var17.size()) {
               case 0:
                  return null;
               case 1:
                  return var5 ? var17 : var17.get(0);
               default:
                  return var17;
               }
            }
         }
      }

      return null;
   }

   public String getColumnNumberByName(String var1) {
      if (var1 == null) {
         return null;
      } else {
         char[] var2 = var1.toUpperCase().toCharArray();
         int var3 = var2[0] - 64;
         if (var3 >= 1 && var3 <= 26) {
            if (var1.length() == 1) {
               return String.valueOf(var3);
            } else {
               int var4 = Integer.valueOf(var1.substring(1));
               return String.valueOf(26 * var4 + var3);
            }
         } else {
            return null;
         }
      }
   }

   public String getColumnNameByNumber(int var1) {
      int var2 = var1 % 26 + 64;
      int var3 = var1 / 26;
      return Character.toChars(var2) + (var3 == 0 ? "" : String.valueOf(var3));
   }

   public static String extendedInfo(String var0, Integer var1, String var2, BookModel var3) {
      return extendedUniInfo(var0, var1, var2, var3, "html");
   }

   public static String extendedInfoTxt(String var0, Integer var1, String var2, BookModel var3) {
      return extendedUniInfo(var0, var1, var2, var3, "txt");
   }

   public static String extendedUniInfo(String var0, Integer var1, String var2, BookModel var3, String var4) {
      String var5 = var4.equals("html") ? "<HTML>" : "";
      if (var0 != null) {
         Object[] var6 = new Object[]{null, null, null};
         String var7 = null;
         String var8 = null;
         String var9 = null;

         try {
            var7 = getPageName(var0, var3, var2);
            var8 = (String)getInstance().getMeta(var0, "row", var2);
            var9 = (String)getInstance().getMeta(var0, "col", var2);
            if (var9 != null && var9.length() > 0) {
               if (var9.equalsIgnoreCase("0")) {
                  var9 = null;
               } else {
                  var9 = getInstance().getColumnNameByNumber(Integer.valueOf(var9));
               }
            }
         } catch (Exception var12) {
            var12.printStackTrace();
         }

         var5 = var5 + "Mezőinformációk (";
         if (var7 != null) {
            var5 = var5 + "Lap: " + var7;
         }

         if (var8 != null) {
            var5 = var5 + ", Sorszám: " + var8;
         }

         if (var9 != null) {
            var5 = var5 + ", Oszlop: " + var9;
         }

         if (var1 != null) {
            var5 = var5 + ", Dinamikus lap száma: " + (var1 + 1);
         }

         var6[0] = var0;

         try {
            Object[] var10 = (Object[])((Object[])getInstance().getIds(var6, var2));
            if (var10[1] != null) {
               var5 = var5 + "," + (var4.equals("html") ? "<BR>" : "\n") + "Belső azonosító=" + var10[1].toString();
            }

            if (var10[2] != null) {
               var5 = var5 + ", Import azonosító=" + var10[2].toString();
            }
         } catch (Exception var11) {
            Tools.eLog(var11, 0);
         }

         var5 = var5 + ", Közös azonosító=" + var0 + ")" + (var4.equals("html") ? "</HTML>" : "");
      }

      return var5;
   }

   public Hashtable getNotInBelFeldFields(String var1) {
      Hashtable var2 = getInstance().getFieldAttributes(var1, "nobelfeld", false);
      Hashtable var3 = getInstance().getCopyFields(var1);
      var2.putAll(var3);
      return var2;
   }

   public Hashtable getCopyFields(String var1) {
      return getInstance().getFieldAttributes(var1, "copy_fld", true);
   }

   public Hashtable getDPageNumberFields(String var1) {
      return getInstance().getFieldAttributes(var1, "DPageNumber", false);
   }
}
