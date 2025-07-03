package hu.piller.enykp.alogic.templateutils;

import hu.piller.enykp.alogic.fileloader.docinfo.DocInfoLoader;
import hu.piller.enykp.alogic.fileutil.DataChecker;
import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.metainfo.MetaStore;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.templateutils.blacklist.BlacklistStore;
import hu.piller.enykp.alogic.upgrademanager_v2_0.Directories;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.framework.MainPanel;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import hu.piller.enykp.util.filelist.EnykFileList;
import me.necrocore.abevjava.NecroFile;

import java.io.File;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

public class TemplateUtils {
   private static final String ENV_KEY_KOZOSSEGI_ADOSZAM = "kozossegi_adoszam";
   private static final String ENV_KEY_ADOSZAM = "adoszam";
   public static final Long ERROR_ID_DATCHECKER_1 = new Long(4001L);
   private static final String VID_KEYS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
   private Hashtable cache_ids_by_attribute;
   private static final String HASH_DELIMITER = "_#_#_";
   private static TemplateUtils instance;

   public static TemplateUtils getInstance() {
      if (instance == null) {
         instance = new TemplateUtils();
      }

      return instance;
   }

   private TemplateUtils() {
      this.init();
   }

   public void init() {
      this.cache_ids_by_attribute = new Hashtable(8);
   }

   public Object getTemplateCid(Object var1, Object var2) {
      if (!(var1 instanceof Vector)) {
         if (var1 != null) {
            boolean var7;
            if (var2 instanceof Boolean) {
               var7 = (Boolean)var2;
            } else {
               var7 = false;
            }

            return this.getTemplateCid_(var1, var7);
         } else {
            return null;
         }
      } else {
         Vector var3 = (Vector)var1;
         int var4 = var3.size();
         Vector var5 = new Vector(var4);

         for(int var6 = 0; var6 < var4; ++var6) {
            var5.add(this.getTemplateCid(var3.get(var6), var2));
         }

         return var5;
      }
   }

   private String getTemplateCid_(Object var1, boolean var2) {
      if (this.isCid_(var1)) {
         String var3 = var1.toString();
         return var2 ? var3.substring(0, 2) + "XXXX" + var3.substring(6) : var3.substring(0, 2) + "0001" + var3.substring(6);
      } else {
         return "";
      }
   }

   public Object isCid(Object var1) {
      if (!(var1 instanceof Vector)) {
         if (var1 != null) {
            return this.isCid_(var1) ? Boolean.TRUE : Boolean.FALSE;
         } else {
            return null;
         }
      } else {
         Vector var2 = (Vector)var1;
         int var3 = var2.size();
         Vector var4 = new Vector(var3);

         for(int var5 = 0; var5 < var3; ++var5) {
            var4.add(this.isCid(var2.get(var5)));
         }

         return var4;
      }
   }

   private boolean isCid_(Object var1) {
      if (var1 != null) {
         String var2 = var1.toString();
         int var3 = var2.length();
         if (var3 == 11 || var3 == 13) {
            return true;
         }
      }

      return false;
   }

   public Object getCidPageCount(Object var1) {
      if (!(var1 instanceof Vector)) {
         return var1 != null ? this.getCidPageCount_(var1) : null;
      } else {
         Vector var2 = (Vector)var1;
         int var3 = var2.size();
         Vector var4 = new Vector(var3);

         for(int var5 = 0; var5 < var3; ++var5) {
            var4.add(this.getCidPageCount(var2.get(var5)));
         }

         return var4;
      }
   }

   private Object getCidPageCount_(Object var1) {
      if (this.isCid_(var1)) {
         String var2 = var1.toString();
         return var2.substring(2, 6);
      } else {
         return "";
      }
   }

   public Object getPagedCid(Object var1, Object var2) {
      if (var1 == null) {
         return "";
      } else {
         String var4 = var1.toString();
         if (!this.isCid_(var4)) {
            return "";
         } else {
            String var3;
            if (var2 instanceof Number) {
               var3 = String.valueOf(((Number)var2).intValue());
            } else {
               if (var2 == null) {
                  return var4;
               }

               var3 = var2.toString();
            }

            var3 = var3.trim();
            int var5 = 0;

            for(int var6 = 4 - var3.length(); var5 < var6; ++var5) {
               var3 = "0" + var3;
            }

            return var4.substring(0, 2) + var3 + var4.substring(6);
         }
      }
   }

   public String keyToDSId(Object var1, Object var2, BookModel var3) {
      if (var1 == null) {
         return null;
      } else {
         String var4 = var1.toString();
         int var6 = var4.length();
         String[] var7 = var4.split("_", 2);
         if (var7.length > 1) {
            try {
               Integer.parseInt(var7[0]);
               return var4;
            } catch (Exception var15) {
               Tools.eLog(var15, 0);
            }
         }

         int var5;
         if (var4.endsWith("]") && (var5 = var4.indexOf("[")) > 0) {
            String[] var17 = new String[]{var4.substring(0, var5), var4.substring(var5 + 1, var4.length() - 1)};
            return var17[1].length() == 0 ? "0_" + var17[0] : var17[1] + "_" + var17[0];
         } else {
            if (var6 < 6) {
               try {
                  int var8 = Integer.parseInt(var4);
                  if (var8 >= 0 && var8 <= 65535) {
                     return "0_" + var4;
                  }
               } catch (Exception var14) {
                  Tools.eLog(var14, 0);
               }
            }

            if (this.isCid_(var4)) {
               String var16 = var4.substring(0, 2).toUpperCase();
               String var9 = var4.substring(2, 6);

               try {
                  int var10 = Integer.parseInt(var9) - 1;
                  if ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(var16.charAt(0)) > -1 && "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(var16.charAt(1)) > -1) {
                     String var11 = "" + var10 + "_" + var4;
                     String var12 = "" + var10 + "_" + var16 + "XXXX" + var4.substring(6);
                     if (var10 < 1 && var3.get((String)var2).fids.containsKey(var4)) {
                        return var11;
                     }

                     return var12;
                  }
               } catch (Exception var13) {
                  Tools.eLog(var13, 0);
               }
            }

            return "0_" + var4;
         }
      }
   }

   public String DSIdToCId(Object var1, Object var2) {
      if (var1 == null) {
         return null;
      } else {
         String var3 = var1.toString();
         String[] var5 = var3.split("_");
         if (var5.length > 1) {
            try {
               Integer.parseInt(var5[0]);
               int var4 = var5[1].length();
               String var7;
               String var8;
               if (var4 < 6) {
                  try {
                     int var6 = Integer.parseInt(var5[1]);
                     if (var6 >= 0 && var6 <= 65535) {
                        if (var2 != null) {
                           var2 = var2.toString();
                        }

                        var7 = var5[1] == null ? "" : var5[1].toString();
                        var8 = (String)MetaInfo.getInstance().didToCid(var7, var2);
                        if (var8 != null && var8.trim().length() > 0) {
                           var5[1] = var8;
                           var4 = var8.length();
                        }
                     }
                  } catch (Exception var10) {
                     Tools.eLog(var10, 0);
                  }
               }

               if (this.isCid_(var5[1])) {
                  String var13 = var5[1].substring(0, 2).toUpperCase();
                  var7 = var5[1].substring(2, 6);

                  try {
                     if ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(var13.charAt(0)) > -1 && "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(var13.charAt(1)) > -1) {
                        var8 = String.valueOf(Integer.parseInt(var5[0]) + 1);

                        for(int var9 = var8.length(); var9 < 4; ++var9) {
                           var8 = "0" + var8;
                        }

                        return var5[1].substring(0, 2) + var8 + var5[1].substring(6);
                     }
                  } catch (Exception var11) {
                     Tools.eLog(var11, 0);
                  }
               }
            } catch (Exception var12) {
               Tools.eLog(var12, 0);
            }
         }

         return var3;
      }
   }

   public Object totalConvert(Object var1, Object var2, Object var3, Object var4, Object var5, BookModel var6) {
      Object[] var7 = null;
      if (var1 instanceof Hashtable && var2 != null && var3 instanceof Vector) {
         Hashtable var8 = (Hashtable)var1;
         String var9 = var2.toString();
         Iterator var10 = var8.keySet().iterator();
         IDataStore var11 = (IDataStore)var4;
         var7 = new Object[]{var11};
         boolean var19 = true;

         while(true) {
            String var13;
            String var14;
            String var21;
            do {
               if (!var10.hasNext()) {
                  DataChecker var25 = DataChecker.getInstance();
                  var25.superCheck(var6, false);
                  return var7;
               }

               var21 = (String)var10.next();
               String var12 = this.keyToDSId(var21, var2, var6);
               var13 = this.DSIdToCId(var12, var9);
               var14 = this.keyToDSId(var13, var2, var6);
            } while(var14 == null);

            String var22;
            if (var14.equals(var13)) {
               try {
                  var22 = var13.split("_", 2)[1];
               } catch (Exception var24) {
                  var22 = var13;
               }
            } else {
               var22 = var13;
            }

            String var15 = (String)var8.get(var21);
            Hashtable var18 = (Hashtable)MetaInfo.getInstance().getMeta(var22, (Object)null, var9);
            if (var18.size() == 0) {
               if (var22.length() > 6) {
                  var22 = var22.substring(0, 2) + "XXXX" + var22.substring(6);
               }

               var18 = (Hashtable)MetaInfo.getInstance().getMeta(var22, (Object)null, var9);
            }

            if (var18 != null) {
               String var17 = (String)var18.get("type");
               if (var17 != null && var17.equals("4")) {
                  if (!var15.equals("X") && !var15.equals("x")) {
                     if (var5 != null) {
                        if (var5.getClass().getName().endsWith("ImportFileLoaderHelper")) {
                           if (var15.equalsIgnoreCase("true")) {
                              var15 = "true";
                           } else if (!var15.equalsIgnoreCase("false") && !var15.equals("")) {
                              var15 = "Nem elfogadott logikai érték (" + var15 + ")";
                           } else {
                              var15 = "false";
                           }
                        } else if (!var5.getClass().getName().endsWith("XMLLoader08Helper") && !var5.getClass().getName().endsWith("XMLLoader08HelperExt")) {
                           var15 = "Nem elfogadott logikai érték (" + var15 + ")";
                        } else if (!var15.equalsIgnoreCase("false") && !var15.equals("")) {
                           var15 = "Nem elfogadott logikai érték (" + var15 + ")";
                        } else {
                           var15 = "";
                        }
                     }
                  } else {
                     var15 = "true";
                  }
               }
            }

            var11.set(var14, var15);
         }
      } else {
         return var7;
      }
   }

   public void setDefaultValues(Object var1, BookModel var2) {
      Hashtable var3 = this.getIdsbyAttribute("defaultvalue", var1);
      IDataStore var4 = this.getDataStore(var2);
      Object[] var6 = new Object[]{new Integer(0), null};
      Enumeration var8 = var3.keys();

      while(var8.hasMoreElements()) {
         Object var5 = var8.nextElement();
         var6[1] = this.getString(var5);
         String var7 = this.getString(var3.get(var5));
         if (this.checkByDataChecker(var2, (String)var1, (String)var6[1], var7)) {
            var4.set(var6, var7);
         }
      }

   }

   public boolean checkByDataCheckerMD(BookModel var1, String var2, String var3, String var4) {
      DataChecker var5 = DataChecker.getInstance();
      Result var6 = var5.checkField(var1, var2, var3, var4, var3, 10, -1);
      if (var6.isOk()) {
         return true;
      } else {
         IErrorList var7 = ErrorList.getInstance();
         var7.writeError(ERROR_ID_DATCHECKER_1, ((TextWithIcon)var6.errorList.get(0)).text, IErrorList.LEVEL_WARNING, (Exception)null, (Object)null);
         return false;
      }
   }

   public boolean checkByDataChecker(BookModel var1, String var2, String var3, String var4) {
      DataChecker var5 = DataChecker.getInstance();
      Result var6 = var5.checkField(var1, var2, var3, var4);
      if (var6.isOk()) {
         return true;
      } else {
         IErrorList var7 = ErrorList.getInstance();
         var7.writeError(ERROR_ID_DATCHECKER_1, ((TextWithIcon)var6.errorList.get(0)).text, IErrorList.LEVEL_WARNING, (Exception)null, (Object)null);
         return false;
      }
   }

   public IDataStore getDataStore(BookModel var1) {
      return (IDataStore)((Elem)var1.get_store_collection().get(var1.getCalcelemindex())).getRef();
   }

   public Object isSavable(BookModel var1) {
      long var2 = System.currentTimeMillis();
      Object var4 = null;
      Vector var5 = var1.get_store_collection();
      int var9 = var1.getCalcelemindex();

      try {
         int var10 = 0;

         for(int var11 = var5.size(); var10 < var11; ++var10) {
            Elem var6 = (Elem)var5.get(var10);
            String var7 = var6.getType();
            IDataStore var8 = (IDataStore)var6.getRef();
            var4 = this.isSavable(var7, var8);
            if (var4 instanceof Object[]) {
               Object[] var12 = (Object[])((Object[])var4);
               if (var12.length > 0 && var12[0] instanceof Boolean && !(Boolean)var12[0]) {
                  break;
               }
            }
         }
      } catch (Exception var13) {
         Tools.eLog(var13, 0);
      }

      var1.setCalcelemindex(var9);
      return var4;
   }

   private Hashtable getIdsbyAttribute(String var1, Object var2) {
      String var3 = var1 + "_#_#_" + var2.toString();
      Hashtable var4 = (Hashtable)this.cache_ids_by_attribute.get(var3);
      if (var4 == null) {
         var4 = new Hashtable(64);
         MetaStore var7 = MetaInfo.getInstance().getMetaStore(var2);
         if (var7 != null) {
            Vector var8 = var7.getFilteredFieldMetas_And(new Vector(Arrays.asList(var1)));
            int var9 = 0;

            for(int var10 = var8.size(); var9 < var10; ++var9) {
               Hashtable var5 = (Hashtable)var8.get(var9);
               String var6 = (String)var5.get(var1);
               if (var6 != null) {
                  var4.put(this.getString(var5.get("fid")), var6);
               }
            }
         }

         this.cache_ids_by_attribute.put(var3, var4);
      }

      return var4;
   }

   private Object isSavable(Object var1, IDataStore var2) {
      Hashtable var3 = this.getIdsbyAttribute("req", var1);
      if (var3 != null && var3.size() > 0) {
         Iterator var4 = var3.entrySet().iterator();

         String var6;
         while(var4.hasNext()) {
            Entry var5 = (Entry)var4.next();
            var6 = var5.getValue().toString();
            if (!"igen".equalsIgnoreCase(var6) && !"yes".equalsIgnoreCase(var6) && !"on".equalsIgnoreCase(var6) && !"true".equalsIgnoreCase(var6) && !"igaz".equalsIgnoreCase(var6)) {
               var4.remove();
            }
         }

         if (var3.size() > 0 && var2 != null) {
            Enumeration var7 = var3.keys();
            Object[] var10 = new Object[]{new Integer(0), null};
            Vector var11 = new Vector(var3.size());

            while(var7.hasMoreElements()) {
               var10[1] = var7.nextElement();
               String var8 = var2.get(var10);
               var6 = var8 == null ? "" : var8.toString();
               if (var6.trim().length() == 0) {
                  var11.add(var10[1]);
               }
            }

            if (var11.size() > 0) {
               MetaStore var12 = MetaInfo.getInstance().getMetaStore(var1);
               StringBuffer var13 = new StringBuffer(2048);
               var13.append("A nyomtatvány nem menthető, következő mezők kitöltése kötelező:");
               int var16 = 0;

               for(int var17 = var11.size(); var16 < var17; ++var16) {
                  if (var16 % 4 == 0 && var16 + 1 < var17) {
                     var13.append("\n");
                  }

                  if (var16 > 0) {
                     var13.append(", ");
                  }

                  String var14 = var11.get(var16).toString();
                  String var15 = var12.getFieldMeta(var14, "panids");
                  if (var15.trim().length() == 0) {
                     var13.append(var14);
                  } else {
                     var13.append(var15);
                  }
               }

               return new Object[]{Boolean.FALSE, var13.toString()};
            }
         }
      }

      return new Object[]{Boolean.TRUE, "Menthető, minden ok."};
   }

   public Object getEnvelopeData(Object var1, Object var2, BookModel var3) {
      try {
         Hashtable var4 = (Hashtable)var2;
         var4.put("kozossegi_adoszam", "");
         MetaStore var5 = MetaInfo.getInstance().getMetaStore(var1);
         Vector var6 = var5.getFilteredFieldMetas_Or(new Vector(Arrays.asList("envelope")));
         Hashtable var7 = new Hashtable(var6.size());
         new Hashtable(var6.size());
         int var12 = 0;

         String var11;
         for(int var13 = var6.size(); var12 < var13; ++var12) {
            try {
               Hashtable var8 = (Hashtable)var6.get(var12);
               String var10 = var8.get("envelope").toString().trim();
               var11 = var8.get("fid").toString().trim();
               if (var10.length() > 0 && var11.length() > 0) {
                  var7.put(var10, var11);
               }
            } catch (Exception var19) {
               Tools.eLog(var19, 0);
            }
         }

         IPropertyList var21 = (IPropertyList)var3.get_store_collection();
         int[] var22 = (int[])((int[])var21.get(new Object[]{"filterfirstitem", var1}));
         if (var22.length > 1 && var22[1] != -1) {
            System.out.println(">getEnvelopeData 1-nél több adattárat talált (" + var1 + "), az első lesz használva !");
         }

         if (var22.length > 0 && var22[0] != -1) {
            Elem var14 = (Elem)((Vector)var21).get(var22[0]);
            IDataStore var15 = (IDataStore)var14.getRef();
            if (var15 != null) {
               Enumeration var16 = var4.keys();

               while(var16.hasMoreElements()) {
                  Object var17 = var16.nextElement();
                  var11 = (String)var7.get(var17);
                  String var18 = "0_" + var11;
                  var4.put(var17, this.getString(var15.get(var18)));
               }
            } else {
               System.out.println(">getEnvelopeData nem tudta megszerezni az adattárat !");
            }
         }

         this.buher_env_list(var4);
      } catch (Exception var20) {
         var20.printStackTrace();
      }

      return var2;
   }

   private void buher_env_list(Hashtable var1) {
      if (var1.containsKey("adoszam")) {
         if (var1.get("adoszam") != null && !var1.get("adoszam").equals("")) {
            return;
         }

         try {
            var1.put("adoszam", var1.remove("kozossegi_adoszam"));
         } catch (Exception var4) {
            var1.put("adoszam", "");
         }
      } else {
         try {
            var1.remove("kozossegi_adoszam");
         } catch (Exception var3) {
            Tools.eLog(var3, 0);
         }
      }

   }

   private String getString(Object var1) {
      return var1 == null ? "" : var1.toString();
   }

   public String[] getTemplateOrphanOrgids() {
      return this.getOrphanOrgIds(TemplateChecker.getInstance().getTemplatePublishers());
   }

   public String[] getHelpOrphanOrgids() {
      HashSet var1 = new HashSet();
      File var2 = new NecroFile(Directories.getHelpsPath());
      Object[] var3 = new Object[]{new DocInfoLoader()};
      Object[] var4 = EnykFileList.getInstance().list(var2.toString(), var3);
      if (var4 != null && var4.length > 0) {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            String var6 = (String)((Hashtable)((Hashtable)((Object[])((Object[])var4[var5]))[1]).get("docinfo")).get("org");
            var1.add(var6);
         }
      }

      return this.getOrphanOrgIds(var1);
   }

   private String[] getOrphanOrgIds(Set<String> var1) {
      Set var2 = (Set)OrgInfo.getInstance().getOrgIds();
      HashSet var3 = new HashSet();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();

         try {
            if (OrgInfo.getInstance().hasSuccessor(var5)) {
               var3.add(OrgInfo.getInstance().getSuccessorOrgId(var5));
            } else {
               var3.add(var5);
            }
         } catch (Exception var7) {
            var3.add(var5);
         }
      }

      var3.removeAll(var2);
      return (String[])var3.toArray(new String[var3.size()]);
   }

   public void handleDisabledTemplates(String var1, String var2) {
      MainFrame.thisinstance.mp.funcreadonly = true;
      MainPanel var10001 = MainFrame.thisinstance.mp;
      MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
      String var3 = this.getStatusText();
      if (var3 != null) {
         if ("".equals(var3)) {
            MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("Megnyitva csak olvasásra");
         } else {
            MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var3 + " - Megnyitva csak olvasásra ");
         }
      }

      System.out.println("DISABLED TEMPLATE");
   }

   private String getStatusText() {
      String var1 = MainFrame.thisinstance.mp.getStatuspanel().statusname.getText();
      return var1 != null && var1.contains("Megnyitva csak olvasásra") ? null : var1;
   }

   public void handleDisabledTemplateMessage(BookModel var1) {
      if (var1 != null) {
         BlacklistStore.getInstance().handleGuiMessage(var1.getTemplateId(), var1.getOrgId());
      }
   }
}
