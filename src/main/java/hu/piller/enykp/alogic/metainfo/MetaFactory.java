package hu.piller.enykp.alogic.metainfo;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

public class MetaFactory {
   public static final String PA_ID_FORM_BAR_CODE = "Javítani kívánt nyomtatvány vonalkódja";
   public static final String PA_ID_E_SEND_REJECT = "Elektronikus feladás tiltása";
   public static final String PA_ID_P_SEND_REJECT = "Papíros beküldés tiltása";
   public static final String PA_ID_ALBIZ_NAME = "Bizonylat tulajdonos név";
   public static final String PA_ID_ALBIZ_AZON = "Bizonylat tulajdonos azonosító";
   public static final String PA_ID_EMP_NAME = "Munkavállaló neve";
   public static final String PA_ID_EMP_TAXNUMBER = "Munkavállaló adószáma";
   public static final String PA_ID_EMP_TAXID = "Munkavállaló adóazonosító jele";
   public static final String PA_ID_EMP_NAME_TITLE = "Munkavállaló titulus";
   public static final String PA_ID_EMP_FNAME = "Munkavállaló vezetéknév";
   public static final String PA_ID_EMP_LNAME = "Munkavállaló keresztnév";
   public static final String PA_ID_NAME = "Adózó neve";
   public static final String PA_ID_FNAME_TITLE = "Név titulus";
   public static final String PA_ID_FNAME = "Vezetékneve";
   public static final String PA_ID_LNAME = "Keresztneve";
   public static final String PA_ID_TAXNUMBER = "Adózó adószáma";
   public static final String PA_ID_EU_MEMBER_ID = "Közösségi adószám ország kód";
   public static final String PA_ID_EU_TAXNUMBER = "Közösségi adószám";
   public static final String PA_ID_TAXID = "Adózó adóazonosító jele";
   public static final String PA_ID_TERMFROM = "Bevallási időszak kezdete";
   public static final String PA_ID_TERMTO = "Bevallási időszak vége";
   public static final String PA_ID_SIGN = "Adózó aláírása";
   public static final String PA_ID_LINECODE = "Vonalkód";
   public static final String PA_ID_STATE = "Státusz";
   public static final String PA_ID_APPLY = "Vonatkozik";
   public static final String PA_ID_TAXEXPNAME = "Adótanácsadó neve";
   public static final String PA_ID_TAXEXPID = "Adótanácsadó azonosítószáma";
   public static final String PA_ID_TAXEXPTESTIMONTIALID = "Adótanácsadó Bizonyítvány";
   public static final String PA_ID_ADMINNAME = "Ügyintéző neve";
   public static final String PA_ID_ADMINTEL = "Ügyintéző telefonszáma";
   public static final String PA_ID_FCORPID = "Pénzintézet-azonosító";
   public static final String PA_ID_FCORPNAME = "Pénzintézet neve";
   public static final String PA_ID_ACCOUNTID = "Számla-azonosító";
   public static final String PA_ID_S_ZIP = "Irányítószám";
   public static final String PA_ID_S_SETTLEMENT = "Település";
   public static final String PA_ID_S_PUBLICPLACE = "Közterület neve";
   public static final String PA_ID_S_HOUSENUMBER = "Házszám";
   public static final String PA_ID_S_LEVEL = "Emelet";
   public static final String PA_ID_S_PUBLICPLACETYPE = "Közterület jellege";
   public static final String PA_ID_S_BUILDING = "Épület";
   public static final String PA_ID_S_STAIRCASE = "Lépcsőház";
   public static final String PA_ID_S_DOOR = "Ajtó";
   public static final String PA_ID_M_ZIP = "L Irányítószám";
   public static final String PA_ID_M_SETTLEMENT = "L Település";
   public static final String PA_ID_M_PUBLICPLACE = "L Közterület neve";
   public static final String PA_ID_M_HOUSENUMBER = "L Házszám";
   public static final String PA_ID_M_LEVEL = "L Emelet";
   public static final String PA_ID_M_PUBLICPLACETYPE = "L Közterület jellege";
   public static final String PA_ID_M_BUILDING = "L Épület";
   public static final String PA_ID_M_STAIRCASE = "L Lépcsőház";
   public static final String PA_ID_M_DOOR = "L Ajtó";
   public static final String PA_ID_HK_AZON = "Hivatali kapu azonosító";
   public static final String PA_ID_PANIDS = "panids";
   public static final String VID_KEYS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
   public static final String MAP_KEY_FID = "fid";
   public static final String MAP_KEY_VID = "vid";
   public static final String MAP_KEY_DID = "did";
   public static final String MAP_KEY_ROW = "row";
   public static final String MAP_KEY_COL = "col";
   public static final String MAP_KEY_PID = "pid";
   public static final String META_ATTR_NOBELFELD = "nobelfeld";
   public static final String META_ATTR_COPY_FIELD = "copy_fld";
   public static final String META_ATTR_DPNUMBER_FIELD = "DPageNumber";
   private static final String[][] be_mapped = new String[][]{{"META", "fid", "fid"}, {"META", "vid", "vid"}, {"META", "did", "did"}, {"META", "row", "row"}, {"META", "col", "col"}, {"PAGEMETA", "pid", "pid"}};
   private static final int TAG_NAME_IDX = 0;
   private static final int TAG_ATTR_NAME_IDX = 1;
   private static final int TAG_MAP_KEY_IDX = 2;
   private static Hashtable mapped_attributes = new Hashtable(16);
   private static Hashtable mapped_keys = new Hashtable(16);

   public static Hashtable getPageMetas() {
      return (Hashtable)mapped_attributes.get("pid");
   }

   public static Hashtable getFieldMetas() {
      return (Hashtable)mapped_attributes.get("fid");
   }

   public static Hashtable getDIdMetas() {
      return (Hashtable)mapped_attributes.get("did");
   }

   public static Hashtable getVIdMetas() {
      return (Hashtable)mapped_attributes.get("vid");
   }

   public static Hashtable getRowMetas() {
      return (Hashtable)mapped_attributes.get("row");
   }

   public static Hashtable getColMetas() {
      return (Hashtable)mapped_attributes.get("col");
   }

   public static Hashtable getMaps() {
      return mapped_attributes;
   }

   public static void createMaps(Hashtable var0, Hashtable var1) {
      release();
      createMetas(var0, var1);
   }

   public static void release() {
      mapped_attributes = new Hashtable(16);
   }

   private static void createMetas(Hashtable var0, Hashtable var1) {
      createMetasTag("META", var0);
      createMetasTag("PAGEMETA", var1);
   }

   private static void createMetasTag(String var0, Hashtable var1) {
      Enumeration var2 = var1.elements();

      while(var2.hasMoreElements()) {
         Hashtable var3 = (Hashtable)var2.nextElement();
         int var4 = 0;

         for(int var5 = be_mapped.length; var4 < var5; ++var4) {
            if (var0.equalsIgnoreCase(be_mapped[var4][0])) {
               Object var6 = var3.get(be_mapped[var4][1]);
               if (var6 != null) {
                  String var7 = be_mapped[var4][2];
                  Object var8 = (Map)mapped_attributes.get(var7);
                  if (var8 == null) {
                     var8 = new Hashtable(128);
                     mapped_attributes.put(var7, var8);
                  }

                  String var9 = var6.toString();
                  Object var10 = ((Map)var8).get(var9);
                  if (var10 == null && ((Map)var8).get(var9.toLowerCase()) != null) {
                     System.out.println("k_key case: " + var9);
                  }

                  if (var10 == null) {
                     ((Map)var8).put(var9, var3);
                  } else {
                     Vector var11;
                     if (var10 instanceof Vector) {
                        var11 = (Vector)var10;
                     } else {
                        var11 = new Vector(32, 64);
                        var11.add(var10);
                        ((Map)var8).put(var9, var11);
                     }

                     var11.add(var3);
                  }
               }
            }
         }
      }

   }

   public static Vector getDataVector(Object var0) {
      if (var0 instanceof Object[]) {
         Object[] var1 = (Object[])((Object[])var0);
         if (var1.length > 2 && var1[2] instanceof Vector) {
            return (Vector)var1[2];
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
      }

      return null;
   }

   private static String getTagName(Object var0) {
      if (var0 instanceof Object[]) {
         Object[] var1 = (Object[])((Object[])var0);
         return (String)var1[0];
      } else {
         return null;
      }
   }

   public static String isContainCalculation(Object var0) {
      Vector var1 = getDataVector(var0);
      if (var1 != null) {
         Hashtable var2 = getAttributes(var0);
         Object var3 = var2 == null ? "" : var2.get("fid");
         int var4 = 0;

         for(int var5 = var1.size(); var4 < var5; ++var4) {
            if ("CALC".equalsIgnoreCase(getTagName(var1.get(var4)))) {
               return var3 == null ? "" : var3.toString();
            }
         }
      }

      return null;
   }
}
