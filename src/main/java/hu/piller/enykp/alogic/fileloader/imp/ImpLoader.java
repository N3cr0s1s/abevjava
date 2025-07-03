package hu.piller.enykp.alogic.fileloader.imp;

import hu.piller.enykp.alogic.fileutil.ExtendedTemplateData;
import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.fileutil.TemplateNameResolver;
import hu.piller.enykp.alogic.fileutil.XConverter;
import hu.piller.enykp.alogic.metainfo.MetaFactory;
import hu.piller.enykp.alogic.metainfo.MetaStore;
import hu.piller.enykp.alogic.settingspanel.upgrade.UpgradeFunction;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.alogic.templateutils.blacklist.BlacklistStore;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.gui.model.VisualFieldModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.ILoadManager;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;

public class ImpLoader implements ILoadManager {
   static final String RESOURCE_NAME = "ABEV Import Betöltő";
   static final Long RESOURCE_ERROR_ID = new Long(1000L);
   private static final String KEY_TS_NY_AZON = "$NY_AZON";
   private static final String KEY_TS_SOROK_SZAMA = "$SOROK_SZÁMA";
   private static final String KEY_TS_D_LAPOK_SZAMA = "$D_LAPOK_SZÁMA";
   private static final String KEY_TS_INFO = "$INFO";
   private static final String KEY_TS_D_LAPOK = "D_LAPOK";
   private static final String KEY_TS_SOROK = "SOROK";
   private static final String KEY_TS_TYPE = "type";
   private static final String KEY_TS_SAVED = "saved";
   private static final String KEY_TS_DOC = "doc";
   private static final String KEY_TS_DOCINFO = "docinfo";
   private int load_type = 0;
   private final Hashtable data = new Hashtable(512);
   private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");

   public String getId() {
      return "imp_data_loader_v1";
   }

   public String getDescription() {
      return "Import állomány";
   }

   public Hashtable getHeadData(File var1) {
      Hashtable var2;
      if (var1 != null && var1.toString().toLowerCase().endsWith(this.getFileNameSuffix())) {
         try {
            this.load_type = 1;
            this.load(var1.toString(), (String)null, (String)null, (String)null);
         } finally {
            this.load_type = 0;
         }

         if (this.data.size() > 0) {
            var2 = new Hashtable(4);
            var2.put("type", "single");
            var2.put("saved", sdf.format(new Date(var1.lastModified())));
            Hashtable var3 = new Hashtable();
            var3.put("id", this.getString(this.data.get("$NY_AZON")));
            var3.put("name", this.getString(this.data.get("$NY_AZON")));
            var3.put("ver", "");
            var3.put("tax_number", "");
            var3.put("account_name", "");
            var3.put("person_name", "");
            var3.put("from_date", "");
            var3.put("to_date", "");
            var3.put("info", this.getString(this.data.get("$INFO")));
            var3.put("note", "");
            var2.put("docinfo", var3);
            var2.put("doc", new Vector(0));
         } else {
            var2 = null;
         }
      } else {
         var2 = null;
      }

      return var2;
   }

   public BookModel load(String var1, String var2, String var3, String var4, BookModel var5) {
      return this.load(var1, var2, var3, var4);
   }

   public BookModel load(String var1, String var2, String var3, String var4) {
      BookModel var5 = null;

      try {
         if (this.load_type == 0) {
            ExtendedTemplateData var6 = TemplateChecker.getInstance().getTemplateFileNames(var2, var3, var4, PropertyList.getInstance().get("prop.dynamic.xml_loader_call") == null ? null : UpgradeFunction.CUST_IMP);
            String[] var7 = var6.getTemplateFileNames();
            File var8 = new File(var7[0]);
            var5 = new BookModel(var8);
            var5.setDisabledTemplate(var6.isTemplateDisaled());
            if (var5.errormsg == null) {
               var5.errormsg = "";
            }

            if (var5.hasError) {
               if (!var8.exists()) {
                  var5.errormsg = var7[2];
               }

               return var5;
            }

            this.loadImpFile(var1, var5);
            if (var5.hasError) {
               return var5;
            }

            var5.addForm(var5.get(this.getFormId()));
            this.fillDataStore(var5);
            this.setPageCounts(var5);
            this.setMessage(var5);
         } else {
            this.loadImpFile(var1, (BookModel)null);
         }
      } catch (Exception var9) {
         if (var5 == null) {
            this.writeError("Betöltési hiba !\n", var9);
         } else {
            var5.errormsg = var5.errormsg + (var5.errormsg.length() == 0 ? "" : ";") + "Betöltési hiba !\n" + var9;
         }
      }

      return var5;
   }

   public BookModel multiload(String var1, String var2, String var3, String var4, Vector var5, Vector var6) {
      BookModel var7 = null;
      boolean var8 = false;

      try {
         if (this.load_type == 0) {
            File var9 = new File(TemplateChecker.getInstance().getTemplateFileNames(var2, var3, var4).getTemplateFileNames()[0]);
            var7 = new BookModel(var9);
            var8 = BlacklistStore.getInstance().isBiztipDisabled(this.getTemplateName(var7.getTemplateId()), var7.getOrgId());
            if (var7.errormsg == null) {
               var7.errormsg = "";
            }

            String[] var13;
            if (var7.hasError) {
               if (!var9.exists()) {
                  var7.errormsg = "Nem található a sablon!";
               }

               var7.setDisabledTemplate(var8);
               if (var8) {
                  var13 = BlacklistStore.getInstance().getErrorListMessage(var2, var4);
                  var7.errormsg = var13[0] + " bl_url " + var13[1];
               }

               return var7;
            }

            this.loadImpFile(var1, var7);
            if (!this.getFormId().equals(var7.main_document_id)) {
               var7.addForm(var7.get(var7.main_document_id));
               var5.insertElementAt(new File(var1), 0);
               var6.add(var1);
               var6.add(new TextWithIcon("    Nem megfelelő típusú állomány! Üres fő nyomtatvány létrehozása!", 4));
               var7.setDisabledTemplate(var8);
               if (var8) {
                  var13 = BlacklistStore.getInstance().getErrorListMessage(var2, var4);
                  var7.errormsg = var13[0] + " bl_url " + var13[1];
               }

               return var7;
            }

            var7.addForm(var7.get(this.getFormId()));
            this.fillDataStore(var7);
            this.setPageCounts(var7);
            this.setMessage(var7);
            var6.add(var1);
            if (var7.impwarninglist != null) {
               for(int var10 = 0; var10 < var7.impwarninglist.size(); ++var10) {
                  var6.add(new TextWithIcon("    " + var7.impwarninglist.get(var10), 4));
               }

               var7.impwarninglist = null;
            }

            var6.add(new TextWithIcon("    Sikeresen hozzáadva!", 3));
         } else {
            this.loadImpFile(var1, (BookModel)null);
            var8 = BlacklistStore.getInstance().isBiztipDisabled(this.getTemplateName(var7.getTemplateId()), var7.getOrgId());
         }
      } catch (Exception var11) {
         if (var7 != null) {
            var8 = BlacklistStore.getInstance().isBiztipDisabled(this.getTemplateName(var7.getTemplateId()), var7.getOrgId());
         }

         if (var7 == null) {
            this.writeError("Betöltési hiba !\n", var11);
         } else {
            if (var7.errormsg == null) {
               var7.errormsg = "";
            }

            var7.errormsg = var7.errormsg + (var7.errormsg.length() == 0 ? "" : ";") + "Betöltési hiba !\n" + var11;
         }
      }

      if (var8) {
         String[] var12 = BlacklistStore.getInstance().getErrorListMessage(this.getTemplateName(var7.getTemplateId()), var7.getOrgId());
         var7.errormsg = var12[0] + " bl_url " + var12[1];
      }

      var7.setDisabledTemplate(var8);
      return var7;
   }

   public String loadIntoBm(String var1, BookModel var2) {
      this.loadImpFile(var1, var2);
      FormModel var3 = var2.get(this.getFormId());
      if (var3 == null) {
         return this.getFormId() == null ? "Hibás formátumú állomány!" : this.getFormId() + " típusú nyomtatvány nem adható hozzá!";
      } else if (var3.id.equals(var2.main_document_id)) {
         return "Fő nyomtatvány nem adható hozzá!";
      } else if (var2.created[var2.getIndex(var3)] == var2.maxcreation[var2.getIndex(var3)]) {
         return this.getFormId() + " Már elérte a maximálisan hozzáadható darabszámot!";
      } else {
         var2.addForm(var3);
         this.fillDataStore(var2);
         this.setPageCounts(var2);
         this.setMessage(var2);
         return null;
      }
   }

   public String getFileNameSuffix() {
      return ".imp";
   }

   public String createFileName(String var1) {
      return var1 == null ? null : (var1.toLowerCase().trim().endsWith(".imp") ? var1.trim() : var1.trim() + ".imp");
   }

   private String getFormId() {
      Object var1 = this.data.get("$NY_AZON");
      return var1 == null ? null : var1.toString();
   }

   private void fillDataStore(BookModel var1) {
      String var2 = this.getFormId();
      IDataStore var3 = var1.get_datastore();
      FormModel var4 = var1.get();
      Hashtable var5 = (Hashtable)this.data.get("SOROK");
      Iterator var6 = var5.entrySet().iterator();

      try {
         while(var6.hasNext()) {
            Entry var7 = (Entry)var6.next();
            if (this.isValidField(var7.getKey(), var4)) {
               var3.set(TemplateUtils.getInstance().keyToDSId(var7.getKey(), var2, var1), var7.getValue().toString());
            } else {
               String var8 = "A sablon nem tartalmazza az adatállományban található (" + var7.getKey() + " mezőkódú) mezőt.\nEz az adat nem kerül betöltésre. Ellenőrizze a nyomtatványt!";
               if (var1.errormsg == null) {
                  var1.errormsg = "";
               }

               var1.errormsg = var1.errormsg + (var1.errormsg.length() == 0 ? "" : ";") + var8;
               if (var1.impwarninglist == null) {
                  var1.impwarninglist = new Vector();
               }

               var1.impwarninglist.add(var8);
            }
         }

         XConverter.convert(var1);
      } catch (Exception var9) {
         this.writeError("Adat feltöltési hiba !", var9);
      }

   }

   private boolean isValidField(Object var1, FormModel var2) {
      Object var3 = var1;
      boolean var4 = var2.get((PageModel)var2.fids_page.get(var1)) != -1;
      if (!var4) {
         var3 = TemplateUtils.getInstance().getTemplateCid(var1, Boolean.TRUE);
         var4 = var2.get((PageModel)var2.fids_page.get(var3)) != -1;
         if (!var4) {
            return false;
         }
      }

      try {
         DataFieldModel var5 = (DataFieldModel)var2.fids.get(var3);
         return var5 == null || (var5.role & VisualFieldModel.getmask()) != 0;
      } catch (Exception var6) {
         return false;
      }
   }

   private void setPageCounts(BookModel var1) {
      int[] var2 = var1.get_pagecounts();
      FormModel var4 = var1.get();
      IDataStore var5 = var1.get_datastore();

      int var3;
      StoreItem var7;
      for(Iterator var6 = var5.getCaseIdIterator(); var6.hasNext(); var2[var3] = Math.max(var2[var3], var7.index + 1)) {
         var7 = (StoreItem)var6.next();
         var3 = var4.get_field_pageindex(var7.code);
      }

   }

   private void setMessage(BookModel var1) {
      try {
         ((Elem)var1.cc.getActiveObject()).getEtc().put("orignote", this.data.get("$INFO"));
      } catch (Exception var3) {
      }

   }

   private void loadImpFile(String var1, BookModel var2) {
      try {
         BufferedReader var3 = new BufferedReader(new InputStreamReader(new FileInputStream(var1), "ISO-8859-2"));
         int var8 = 0;
         int var9 = 0;
         Hashtable var11 = null;
         Hashtable var12 = null;
         Object[] var7 = new Object[2];
         this.data.clear();

         label108:
         while(true) {
            while(true) {
               String var4;
               do {
                  do {
                     if ((var4 = var3.readLine()) == null) {
                        break label108;
                     }

                     var4 = var4.trim();
                  } while(var4.length() == 0);
               } while(var4.startsWith(";"));

               if (var4.startsWith("=")) {
                  this.writeError("Hibás értékadás ! (" + var4 + ")", (Exception)null);
               } else {
                  String[] var6 = var4.split("=", 2);
                  if (var6.length >= 2) {
                     var7[0] = var6[0];
                     var7[1] = var6[1];
                     String var5 = (String)var7[0];
                     byte var10;
                     if (var5.toUpperCase().startsWith("$")) {
                        var10 = 0;
                        if (!var5.toUpperCase().startsWith("$D_LAPOK_SZÁMA") && var5.toUpperCase().startsWith("$D_LAP")) {
                           var10 = 1;
                        }
                     } else {
                        var10 = 2;
                     }

                     if (this.load_type == 1 && var10 == 2) {
                        break label108;
                     }

                     switch(var10) {
                     case 0:
                        if ("$NY_AZON".equalsIgnoreCase(var5)) {
                           var5 = "$NY_AZON";
                        } else if ("$SOROK_SZÁMA".equalsIgnoreCase(var5)) {
                           var5 = "$SOROK_SZÁMA";
                           var8 = Integer.parseInt((String)var7[1]);
                        } else if ("$D_LAPOK_SZÁMA".equalsIgnoreCase(var5)) {
                           var5 = "$D_LAPOK_SZÁMA";
                           var9 = Integer.parseInt((String)var7[1]);
                        } else if ("$INFO".equalsIgnoreCase(var5)) {
                           var5 = "$INFO";
                        }
                        break;
                     case 1:
                        if (var11 == null) {
                           var11 = new Hashtable(Integer.parseInt((String)this.data.get("$D_LAPOK_SZÁMA")));
                        }

                        if (var9 > 0) {
                           --var9;
                           var11.put(var5, var7[1]);
                           if (var9 != 0) {
                              continue;
                           }

                           var5 = "D_LAPOK";
                           var7[1] = var11;
                        }
                        break;
                     case 2:
                        if (var12 == null) {
                           var12 = new Hashtable(Integer.parseInt((String)this.data.get("$SOROK_SZÁMA")));
                        }

                        if (var8 <= 0) {
                           var2.hasError = true;
                           var2.errormsg = "A fájl több sort tartalmaz, mint a '$sorok_száma=' mező tartalma!";
                           throw new Exception("A fájl több sort tartalmaz, mint a '$sorok_száma=' mező tartalma!");
                        }

                        --var8;
                        var12.put(var5, var7[1]);
                        if (var8 != 0) {
                           continue;
                        }

                        var5 = "SOROK";
                        var7[1] = var12;
                     }

                     this.data.put(var5, var7[1]);
                  }
               }
            }
         }

         var3.close();
         this.keysDidToCid(var12, var2);
         if (var2 != null) {
            var2.setDisabledTemplate(BlacklistStore.getInstance().isBiztipDisabled(var2.getTemplateId(), var2.getOrgId()));
         }

         if (this.data.get("D_LAPOK") == null) {
            if (var11 == null) {
               this.data.put("D_LAPOK", new Hashtable());
            } else {
               this.data.put("D_LAPOK", var11);
            }
         }

         if (this.data.get("SOROK") == null) {
            if (var12 == null) {
               this.data.put("SOROK", new Hashtable());
            } else {
               this.data.put("SOROK", var12);
            }
         }
      } catch (Exception var14) {
         this.writeError("Hiba betöltés közben !", var14);
      }

   }

   private void keysDidToCid(Hashtable var1, BookModel var2) {
      if (var1 != null && var2 != null) {
         int var3 = var1.size();
         if (var3 > 0) {
            FormModel var4 = var2.get((String)this.data.get("$NY_AZON"));
            if (var4 != null) {
               Hashtable var5 = var2.getMetas((String)this.data.get("$NY_AZON"));
               Hashtable var6 = var2.getPageMetas((String)this.data.get("$NY_AZON"));

               try {
                  MetaStore var12 = new MetaStore();
                  Hashtable var13 = new Hashtable(var1.size());
                  MetaFactory.createMaps(var5, var6);
                  var12.setMaps(MetaFactory.getMaps());
                  MetaFactory.release();

                  Object var7;
                  Object var8;
                  for(Iterator var14 = var1.keySet().iterator(); var14.hasNext(); var13.put(var7, var1.get(var8))) {
                     var8 = var14.next();
                     String var9 = var8.toString();
                     String var10;
                     String var11;
                     if (var9.endsWith("]")) {
                        var10 = var9.substring(0, var9.indexOf("["));
                        var11 = var9.substring(var9.indexOf("[") + 1, var9.length() - 1);
                     } else {
                        var10 = var9;
                        var11 = "1";
                     }

                     var7 = var12.getDidMeta(var10, "fid");
                     if (var7 == null || var7.equals("")) {
                        var7 = var10;
                     }

                     if (this.FIdIsCid(var7)) {
                        var7 = this.getPagedCid(var7, var11);
                     }
                  }

                  var1.clear();
                  var1.putAll(var13);
               } catch (Exception var15) {
                  Tools.eLog(var15, 0);
               }
            }
         }
      }

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

   private Object getPagedCid(Object var1, Object var2) {
      Object var4 = TemplateUtils.getInstance().getPagedCid(var1, var2);
      Object var3;
      if (var4 != null) {
         var3 = var4.toString();
      } else {
         var3 = var1;
      }

      return var3;
   }

   private String getString(Object var1) {
      return var1 == null ? "" : var1.toString();
   }

   private void writeError(String var1, Exception var2) {
      ErrorList.getInstance().writeError(RESOURCE_ERROR_ID, "ABEV Import Betöltő: " + (var1 == null ? "" : var1), var2, (Object)null);
   }

   private String getTemplateName(String var1) {
      if (var1 == null) {
         return "";
      } else {
         Vector var2 = TemplateNameResolver.getInstance().getName(var1);
         return var2 != null && var2.size() != 0 ? (String)var2.get(0) : var1;
      }
   }
}
