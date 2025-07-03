package hu.piller.enykp.alogic.filesaver.imp;

import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.metainfo.MetaStore;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.interfaces.ISaveManager;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;

public class ImpSaver implements ISaveManager {
   private BookModel bm = null;
   static final String RESOURCE_NAME = "ABEV Import Kimentő";
   static final Long RESOURCE_ERROR_ID = new Long(2000L);
   private static final int KEY_Q = 0;
   private static final int KEY_TS = 1;
   public static final String KEY_Q_NY_AZON = "ny_azon";
   public static final String KEY_Q_SOROK_SZAMA = "sorok_száma";
   public static final String KEY_Q_D_LAPOK_SZAMA = "d_lapok_száma";
   public static final String KEY_Q_INFO = "info";
   public static final String KEY_Q_D_LAPOK = "d_lapok";
   public static final String KEY_Q_SOROK = "sorok";
   public static final String KEY_Q_D_LAP = "d_lap";
   public static final String KEY_Q_SOR = "sor";
   public static final String KEY_Q_DATASTORE = "field_value_pair_list";
   public static final String KEY_Q_FORMMETADATA = "form_meta_data";
   private static final String KEY_TS_NY_AZON = "$NY_AZON";
   private static final String KEY_TS_SOROK_SZAMA = "$SOROK_SZÁMA";
   private static final String KEY_TS_D_LAPOK_SZAMA = "$D_LAPOK_SZÁMA";
   private static final String KEY_TS_INFO = "$INFO";
   private static final String KEY_TS_D_LAPOK = "D_LAPOK";
   private static final String KEY_TS_SOROK = "SOROK";
   private static final String[][] KEY_PAIRS = new String[][]{{"ny_azon", "$NY_AZON"}, {"sorok_száma", "$SOROK_SZÁMA"}, {"d_lapok_száma", "$D_LAPOK_SZÁMA"}, {"info", "$INFO"}, {"d_lapok", "D_LAPOK"}, {"sorok", "SOROK"}, {"d_lap", null}, {"sor", null}};
   private Hashtable data;
   private Hashtable did_to_cid;
   private boolean save_state;

   public ImpSaver() {
      this.init((BookModel)null);
   }

   public ImpSaver(BookModel var1) {
      this.init(var1);
   }

   public void save(OutputStream var1) {
      if (this.bm != null) {
         IDataStore var2 = this.bm.get_datastore();
         Iterator var3 = var2.getCaseIdIterator();
         Hashtable var4 = new Hashtable(2048);
         String var5 = this.bm.get_formid();

         while(var3.hasNext()) {
            Object var6 = var3.next();
            String var7;
            if ((var7 = var2.get(var6)) != null) {
               var4.put(TemplateUtils.getInstance().DSIdToCId(var6.toString(), var5), var7);
            }
         }

         Hashtable var8 = new Hashtable();
         var8.put("nyomtatvanyazonosito", this.bm.get_formid());
         Hashtable var9 = new Hashtable();
         var9.put("field_value_pair_list", var4);
         var9.put("form_meta_data", var8);
         if (((Elem)this.bm.cc.getActiveObject()).getEtc().get("orignote") != null) {
            var9.put("info", ((Elem)this.bm.cc.getActiveObject()).getEtc().get("orignote"));
         }

         this.init(this.getBookModel());
         this.setData(var9);
         this.write(var1);
      }

   }

   private Hashtable uploadAllTemplateId(String var1, Hashtable var2) {
      MetaInfo var3 = MetaInfo.getInstance();
      MetaStore var4 = var3.getMetaStore(var1);
      Vector var5 = var4.getFieldIds();

      String var7;
      for(Iterator var6 = var5.iterator(); var6.hasNext(); var2.put(var7, "")) {
         var7 = (String)var6.next();
         if (var7.indexOf("XXXX") != -1) {
            var7 = var7.substring(0, 2) + "0001" + var7.substring(6);
         }
      }

      return var2;
   }

   public Object getHelper(OutputStream var1) {
      return this;
   }

   public OutputStream getStream(String var1) {
      try {
         return new FileOutputStream(new File(new URI(var1)));
      } catch (FileNotFoundException var3) {
         throw new RuntimeException(var3);
      } catch (URISyntaxException var4) {
         throw new RuntimeException(var4);
      }
   }

   public String createFileName(String var1) {
      return var1 == null ? null : (var1.toLowerCase().trim().endsWith(".imp") ? var1.toLowerCase().trim() : var1.toLowerCase().trim() + ".imp");
   }

   public String getFileNameSuffix() {
      return ".imp";
   }

   public String getDescription() {
      return "Import mentő.";
   }

   public boolean save(String var1) {
      boolean var2;
      if (this.bm != null) {
         try {
            this.save((OutputStream)(new FileOutputStream(var1)));
         } catch (FileNotFoundException var4) {
            throw new RuntimeException(var4);
         }

         var2 = this.save_state;
      } else {
         var2 = false;
      }

      return var2;
   }

   public Object getErrorList() {
      return null;
   }

   public void setBookModel(BookModel var1) {
      this.bm = var1;
   }

   public BookModel getBookModel() {
      return this.bm;
   }

   private void init(BookModel var1) {
      this.data = new Hashtable(6);
      this.data.put("$NY_AZON", "");
      this.data.put("$SOROK_SZÁMA", "");
      this.data.put("$D_LAPOK_SZÁMA", "");
      this.data.put("$INFO", "");
      if (this.data.get("D_LAPOK") instanceof Vector) {
         ((Vector)this.data.get("D_LAPOK")).clear();
      } else {
         this.data.put("D_LAPOK", new Vector(128));
      }

      if (this.data.get("SOROK") instanceof Hashtable) {
         ((Hashtable)this.data.get("SOROK")).clear();
      } else {
         this.data.put("SOROK", new Hashtable(2048));
      }

      if (this.did_to_cid != null) {
         this.did_to_cid.clear();
      } else {
         this.did_to_cid = new Hashtable(128);
      }

      this.save_state = false;
      this.setBookModel(var1);
   }

   public void setData(Hashtable var1) {
      Hashtable var2 = null;
      Hashtable var3 = (Hashtable)var1.get("form_meta_data");
      this.data.put("$NY_AZON", var3.get("nyomtatvanyazonosito"));
      Hashtable var4;
      Hashtable var5;
      Hashtable var6;
      Hashtable var7;
      String var17;
      if (var1.get("field_value_pair_list") != null) {
         var4 = (Hashtable)var1.get("field_value_pair_list");
         var5 = (Hashtable)this.data.get("SOROK");
         var2 = this.getFormFieldInfos();
         var6 = (Hashtable)var2.get("fields");
         var7 = (Hashtable)var2.get("dids");
         Hashtable var8 = (Hashtable)var2.get("on_dpage");
         Iterator var9 = var4.entrySet().iterator();

         while(var9.hasNext()) {
            Entry var10 = (Entry)var9.next();
            String var11 = (String)var10.getKey();
            if (this.FIdIsCid(var11)) {
               String var16 = this.getTemplateCid(var11, this.isCidOnDpage(var11, var8));
               Object var14 = var6.get(var16);
               if (var14 != null) {
                  Object[] var15 = (Object[])((Object[])var14);
                  var17 = (String)var7.get(var16);
                  this.did_to_cid.put(var17, var16);
                  if ((Boolean)var15[1]) {
                     Object var18 = this.getCidPageCount(var11, false);
                     var17 = var17 + "[" + var18 + "]";
                  }

                  String var12 = var10.getValue().toString();
                  if (this.bm.getFieldVisible(var16) && !this.bm.getFieldReadOnly(var16) && this.getFieldAttribute(var16, this.data.get("$NY_AZON"), "copy_fld").length() <= 0) {
                     var5.put(var17, var12);
                  }
               }
            }
         }
      }

      if (var1.get("form_meta_data") != null) {
         if (var2 == null) {
            var2 = this.getFormFieldInfos();
         }

         var6 = (Hashtable)var2.get("fields");
         var5 = (Hashtable)var2.get("pages");
         var7 = (Hashtable)var2.get("on_dpage");
         var4 = (Hashtable)var1.get("field_value_pair_list");
         Enumeration var32 = var4.keys();

         label107:
         while(true) {
            while(true) {
               Object var25;
               while(var32.hasMoreElements()) {
                  String var30 = (String)var32.nextElement();
                  if (this.FIdIsCid(var30)) {
                     var17 = this.getTemplateCid(var30, this.isCidOnDpage(var30, var7));
                     if (this.bm.getFieldVisible(var17) && !this.bm.getFieldReadOnly(var17) && this.getFieldAttribute(var17, this.data.get("$NY_AZON"), "copy_fld").length() <= 0) {
                        var25 = var6.get(var17);
                        if (var25 != null) {
                           Object[] var27 = (Object[])((Object[])var25);
                           String[] var31 = new String[]{this.getCidPageCount(var30, true).toString(), var17};
                           Integer var13 = (Integer)var5.get(var27[0]);
                           if (var13 != null) {
                              int var33 = Integer.parseInt(var31[0]) + 1;
                              if (var33 > var13) {
                                 var5.put(var27[0], var33);
                              }
                           }
                        }
                     } else {
                        var4.remove(var30);
                     }
                  } else {
                     var4.remove(var30);
                  }
               }

               Vector var34 = (Vector)this.data.get("D_LAPOK");
               Iterator var23 = var5.keySet().iterator();
               int var28 = 0;

               while(var23.hasNext()) {
                  var25 = var23.next();
                  Integer var29 = (Integer)var5.get(var25);
                  if (var29 != null && var29 > 0) {
                     ++var28;
                     var34.add("$d_lap" + var28 + "=" + var25.toString() + "," + var5.get(var25));
                  }
               }

               this.data.put("$D_LAPOK_SZÁMA", new Integer(var28));
               break label107;
            }
         }
      }

      int var19 = 0;

      for(int var20 = KEY_PAIRS.length; var19 < var20; ++var19) {
         String var21 = KEY_PAIRS[var19][0];
         Object var22 = var1.get(var21);
         if (var22 != null && this.data != null) {
            if (KEY_PAIRS[var19][1] != null) {
               this.data.put(KEY_PAIRS[var19][1], var22);
            } else if ("d_lap".equalsIgnoreCase(var21) || "sor".equalsIgnoreCase(var21)) {
               String[] var24 = var22.toString().split("=", 2);
               Hashtable var26;
               if ("d_lap".equalsIgnoreCase(var21)) {
                  var26 = (Hashtable)this.data.get("D_LAPOK");
               } else {
                  var26 = (Hashtable)this.data.get("SOROK");
               }

               var26.put(var24[0], var24[1]);
            }
         }
      }

   }

   private Hashtable getFormFieldInfos() {
      Hashtable var1 = new Hashtable(2);
      Hashtable var2 = new Hashtable(1024);
      Hashtable var3 = new Hashtable(1024);
      Hashtable var4 = new Hashtable(1024);
      Hashtable var5 = new Hashtable(64);
      IPropertyList var6 = PropertyList.getInstance();
      if (var6 != null) {
         FormModel var14 = this.bm.get(this.bm.get_formid());
         int var16 = 0;

         for(int var17 = var14.size(); var16 < var17; ++var16) {
            Boolean var12 = Boolean.FALSE;
            PageModel var15 = var14.get(var16);
            String var11 = var15.name;
            boolean var13 = var15.dynamic;
            if (var13) {
               var5.put(var11, new Integer(0));
               var12 = Boolean.TRUE;
            }

            Vector var8 = this.page_info(var15.y_sorted_df);
            Vector var7 = (Vector)MetaInfo.getInstance().cidToDid(var8, this.bm.get_formid());
            int var18 = 0;

            for(int var19 = var7.size(); var18 < var19; ++var18) {
               String var9 = (String)var8.get(var18);
               String var10 = (String)var7.get(var18);
               var2.put(var9, new Object[]{var11, var13 ? Boolean.TRUE : Boolean.FALSE});
               var3.put(var9, var10);
               var4.put(var9, var12);
            }
         }
      }

      var1.put("fields", var2);
      var1.put("dids", var3);
      var1.put("on_dpage", var4);
      var1.put("pages", var5);
      return var1;
   }

   private Vector page_info(Vector var1) {
      Vector var2;
      if (var1 == null) {
         var2 = new Vector(0);
      } else {
         var2 = new Vector(var1.size());
         int var3 = 0;

         for(int var4 = var1.size(); var3 < var4; ++var3) {
            var2.add(((DataFieldModel)var1.get(var3)).key);
         }
      }

      return var2;
   }

   private boolean FIdIsCid(Object var1) {
      Object var3 = TemplateUtils.getInstance().isCid(var1);
      boolean var2;
      if (var3 instanceof Boolean) {
         var2 = (Boolean)var3;
      } else {
         var2 = false;
      }

      return var2;
   }

   private String getTemplateCid(Object var1, Object var2) {
      return (String)TemplateUtils.getInstance().getTemplateCid(var1, var2);
   }

   private Object isCidOnDpage(Object var1, Hashtable var2) {
      Object var3 = var2.get(var1);
      if (var3 == null) {
         var3 = Boolean.TRUE;
      }

      return var3;
   }

   private Object getCidPageCount(Object var1, boolean var2) {
      Object var3 = TemplateUtils.getInstance().getCidPageCount(var1);
      if (var3 == null) {
         var3 = "?";
      } else {
         try {
            var3 = Integer.valueOf(var3.toString());
            if (var2) {
               var3 = new Integer((Integer)var3 - 1);
            }
         } catch (NumberFormatException var5) {
            var3 = "?";
         }
      }

      return var3;
   }

   private void write(OutputStream var1) {
      this.save_state = false;

      try {
         BufferedWriter var2 = new BufferedWriter(new OutputStreamWriter(var1, "ISO-8859-2"));
         this.data.put("$SOROK_SZÁMA", new Integer(4 + ((Vector)this.data.get("D_LAPOK")).size() + ((Hashtable)this.data.get("SOROK")).size()));
         this.writeKey(this.data, "$NY_AZON", var2, false);
         this.writeKey(this.data, "$SOROK_SZÁMA", var2, false);
         this.writeKey(this.data, "$D_LAPOK_SZÁMA", var2, false);
         this.writeVector(this.data.get("D_LAPOK"), var2);
         this.writeKey(this.data, "$INFO", var2, false);
         this.writeSorted(this.data.get("SOROK"), var2, true);
         var2.flush();
         var2.close();
         this.save_state = true;
      } catch (Exception var4) {
         this.writeError("Hiba történt állomány írása közben !", var4);
      }

   }

   private void writeHashtable(Object var1, BufferedWriter var2, boolean var3) throws Exception {
      if (var1 instanceof Hashtable) {
         Hashtable var4 = (Hashtable)var1;
         Iterator var5 = var4.keySet().iterator();

         while(var5.hasNext()) {
            this.writeKey(var4, var5.next().toString(), var2, var3);
         }
      }

   }

   private void writeSorted(Object var1, BufferedWriter var2, boolean var3) throws Exception {
      if (var1 instanceof Hashtable) {
         Hashtable var4 = (Hashtable)var1;
         ArrayList var5 = new ArrayList(var4.keySet());
         Collections.sort(var5, new Comparator() {
            public int compare(Object var1, Object var2) {
               String var3 = (String)var1;
               String var4 = (String)var2;
               int var5 = var3.indexOf("[");
               int var6 = var4.indexOf("[");
               if (var5 == -1) {
                  return var6 > -1 ? -1 : Integer.valueOf(var3).compareTo(Integer.valueOf(var4));
               } else if (var6 == -1) {
                  return 1;
               } else {
                  String[] var7 = var3.substring(0, var3.indexOf("]")).split("\\[");
                  String[] var8 = var4.substring(0, var4.indexOf("]")).split("\\[");
                  int var9 = Integer.valueOf((String)var7[0]).compareTo(Integer.valueOf((String)var8[0]));
                  return var9 == 0 ? Integer.valueOf((String)var7[1]).compareTo(Integer.valueOf((String)var8[1])) : var9;
               }
            }
         });
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            this.writeKey(var4, var6.next().toString(), var2, var3);
         }
      }

   }

   private void writeVector(Object var1, BufferedWriter var2) throws Exception {
      if (var1 instanceof Vector) {
         Vector var3 = (Vector)var1;

         for(int var4 = 0; var4 < var3.size(); ++var4) {
            String var5 = (String)var3.elementAt(var4);
            var2.write(var5);
            var2.newLine();
         }
      }

   }

   private void writeKey(Hashtable var1, String var2, BufferedWriter var3, boolean var4) throws Exception {
      String var5 = var1.get(var2).toString();
      if (var4) {
         Object var6 = this.did_to_cid.get(var2);
         if (var6 == null) {
            var6 = var2;
         }

         String var7 = this.getFieldType(var6, this.data.get("$NY_AZON")).trim();
         if ("4".equals(var7)) {
            if ("true".equalsIgnoreCase(var5.trim())) {
               var5 = "X";
            } else if ("false".equalsIgnoreCase(var5.trim())) {
               var5 = "";
            }
         }
      }

      var3.write(var2.toLowerCase() + "=" + var5);
      var3.newLine();
   }

   private String getFieldType(Object var1, Object var2) {
      String var3 = (String)MetaInfo.getInstance().getMeta(var1, "type", var2);
      return var3 == null ? "" : var3;
   }

   private String getFieldAttribute(Object var1, Object var2, String var3) {
      String var4 = (String)MetaInfo.getInstance().getMeta(var1, var3, var2);
      return var4 == null ? "" : var4;
   }

   private void writeError(String var1, Exception var2) {
      ErrorList.getInstance().writeError(RESOURCE_ERROR_ID, "ABEV Import Kimentő: " + (var1 == null ? "" : var1), var2, (Object)null);
   }
}
