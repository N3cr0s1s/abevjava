package hu.piller.enykp.alogic.fileutil;

import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import me.necrocore.abevjava.NecroFile;

import java.io.File;
import java.util.HashSet;
import java.util.Vector;

public class FileStatusChecker implements IPropertyList {
   public static final String STATE_ALL = "(Minden állapot)";
   public static final String STATE_EDITABLE = "Módosítható";
   public static final String STATE_SIGNED_FOR_SEND = "Küldésre megjelölt";
   public static final String STATE_SENT = "Elküldött";
   public static final String STATE_PASSED_TO_EXTERNAL_SIGN = "Külső aláírásra átadva";
   public static final String STATE_PASSED_TO_AVDH_SIGN = "AVDH aláírással hitelesített";
   public static final String STATE_PASSED_TO_XCZ = "Adathordozóhoz másolva";
   public static final String STATE_UNKNOWN = "(Ismeretlen)";
   public static final int STATE_CODE_ALL = -1;
   public static final int STATE_CODE_EDITABLE = 0;
   public static final int STATE_CODE_SIGNED_FOR_SEND = 1;
   public static final int STATE_CODE_SENT = 2;
   public static final int STATE_CODE_PASSED_TO_EXTERNAL_SIGN = 3;
   public static final int STATE_CODE_PASSED_TO_AVDH_SIGN = 4;
   public static final int STATE_CODE_PASSED_TO_XCZ = 5;
   public static final String EXTERNAL_SIGN_POSTFIX = "_attachment";
   private static final Long RESOURCE_ERROR_ID = new Long(950L);
   public static final String FKIT = ".frm.enyk";
   public static final String FKIT08 = ".kr";
   private String srcPath;
   private String destPath;
   private String sentPath;
   private static boolean splitTemplate = false;
   private static FileStatusChecker instance;
   private static final int MAX_FILEPART_COUNT = 4;
   public static final int TYPE_INT = 0;
   public static final int TYPE_VECTOR = 1;
   private static HashSet destFiles;
   private static HashSet sentFiles;
   private static int counter;

   public static FileStatusChecker getInstance() {
      if (instance == null) {
         instance = new FileStatusChecker();
      }

      splitTemplate = false;
      return instance;
   }

   private FileStatusChecker() {
      IPropertyList var1 = PropertyList.getInstance();
      this.destPath = (new NecroFile((String)var1.get("prop.usr.krdir"), (String)var1.get("prop.usr.ds_dest"))).getAbsolutePath();
      this.sentPath = (new NecroFile((String)var1.get("prop.usr.krdir"), (String)var1.get("prop.usr.ds_sent"))).getAbsolutePath();
      this.srcPath = (new NecroFile((String)var1.get("prop.usr.krdir"), (String)var1.get("prop.usr.ds_src"))).getAbsolutePath();
      if (!this.destPath.endsWith("\\") && !this.destPath.endsWith("/")) {
         this.destPath = this.destPath + File.separator;
      }

      if (!this.sentPath.endsWith("\\") && !this.sentPath.endsWith("/")) {
         this.sentPath = this.sentPath + File.separator;
      }

      if (!this.srcPath.endsWith("\\") && !this.srcPath.endsWith("/")) {
         this.srcPath = this.srcPath + File.separator;
      }

   }

   public boolean set(Object var1, Object var2) {
      try {
         splitTemplate = (Boolean)var2;
      } catch (Exception var4) {
         Tools.eLog(var4, 0);
      }

      return false;
   }

   public Object get(Object var1) {
      try {
         if (!(var1 instanceof Object[])) {
            throw new ClassCastException();
         }

         if (!((Object[])((Object[])var1))[0].equals("get_filestatus")) {
            if (!((Object[])((Object[])var1))[0].equals("get_string_filestatus")) {
               throw new NullPointerException();
            }

            if (((Object[])((Object[])var1))[1] == null) {
               return getStringStatus(0);
            }
         }

         File var2 = (File)((Object[])((Object[])var1))[1];
         String var3 = var2.getName();
         if (!var3.toLowerCase().endsWith(".frm.enyk")) {
            return "Módosítható";
         }

         int var4;
         if (((Object[])((Object[])var1))[2].equals("")) {
            String var5 = var3.substring(0, var3.toLowerCase().indexOf(".frm.enyk"));
            var4 = this.getExtendedStatus(var3, var5);
         } else {
            var4 = this.getExtendedStatus(var3, (String)((Object[])((Object[])var1))[2]);
         }

         if (((Object[])((Object[])var1))[0].equals("get_string_filestatus")) {
            return getStringStatus(var4);
         }

         return new Integer(var4);
      } catch (ClassCastException var6) {
         ErrorList.getInstance().writeError(RESOURCE_ERROR_ID, "Hibás objektumtípus", var6, (Object)null);
      } catch (NullPointerException var7) {
         ErrorList.getInstance().writeError(RESOURCE_ERROR_ID, "Üres objektum", var7, (Object)null);
      } catch (Exception var8) {
         ErrorList.getInstance().writeError(RESOURCE_ERROR_ID, "Általános hiba", var8, (Object)null);
      }

      return null;
   }

   public Boolean getFileReadOnlyState(String var1) {
      return !var1.equalsIgnoreCase("(Minden állapot)") && !var1.equalsIgnoreCase("Módosítható") ? Boolean.TRUE : Boolean.FALSE;
   }

   public String getFileState(File var1, String var2) {
      return this.getExtendedFileState(var1, var2);
   }

   public String getExtendedFileState(File var1, String var2) {
      Object var3 = this.get(new Object[]{"get_filestatus", var1, var2});
      if (var3 instanceof Integer) {
         switch((Integer)var3) {
         case 0:
            return "Módosítható";
         case 1:
            return "Küldésre megjelölt";
         case 2:
            return "Elküldött";
         case 3:
            return "Külső aláírásra átadva";
         case 4:
            return "AVDH aláírással hitelesített";
         case 5:
            return "Adathordozóhoz másolva";
         default:
            return "(Ismeretlen)";
         }
      } else {
         return "(adat nem elérhető)";
      }
   }

   public int getStatus(String var1, String var2) {
      return this.getExtendedStatus(var1, var2);
   }

   public int getExtendedStatus(String var1, String var2) {
      if ("".equals(var2)) {
         var2 = var1.substring(0, var1.length() - ".frm.enyk".length());
      }

      return (Integer)this.work(var1, 0, var2);
   }

   public Vector getFilenames(String var1, String var2) {
      if ("".equals(var2)) {
         var2 = var1.substring(0, var1.length() - ".frm.enyk".length());
      }

      return (Vector)this.work(var1, 1, var2);
   }

   public static String getStringStatus(int var0) {
      switch(var0) {
      case 0:
         return "Módosítható";
      case 1:
         return "Küldésre megjelölt";
      case 2:
         return "Elküldött";
      case 3:
         return "Külső aláírásra átadva";
      case 4:
         return "AVDH aláírással hitelesített";
      case 5:
         return "Adathordozóhoz másolva";
      default:
         return "(Ismeretlen)";
      }
   }

   private Object work(String var1, int var2, String var3) {
      if (var1 != null) {
         var1.toLowerCase();
      }

      String var5 = var3;
      if (var3.endsWith(".kr")) {
         var5 = var3.substring(0, var3.length() - ".kr".length());
      }

      try {
         String var4;
         int var6;
         boolean var9;
         if (destFiles == null) {
            File[] var16 = new File[4];
            File[] var18 = new File[4];
            var4 = (new NecroFile(var5)).getName();
            var16[0] = new NecroFile(this.sentPath + PropertyList.USER_IN_FILENAME + (var4.endsWith(".frm.enyk") ? var4.substring(0, var4.indexOf(".frm.enyk")) + ".kr" : var4 + ".kr"));
            var18[0] = new NecroFile(this.destPath + PropertyList.USER_IN_FILENAME + (var4.endsWith(".frm.enyk") ? var4.substring(0, var4.indexOf(".frm.enyk")) + ".kr" : var4 + ".kr"));

            for(int var19 = 1; var19 < var16.length; ++var19) {
               var16[var19] = new NecroFile(this.sentPath + PropertyList.USER_IN_FILENAME + (var4.endsWith(".frm.enyk") ? var4.substring(0, var4.indexOf(".frm.enyk")) + "_" + (var19 - 1) + "_p" + ".kr" : var4 + "_" + (var19 - 1) + "_p" + ".kr"));
               var18[var19] = new NecroFile(this.destPath + PropertyList.USER_IN_FILENAME + (var4.endsWith(".frm.enyk") ? var4.substring(0, var4.indexOf(".frm.enyk")) + "_" + (var19 - 1) + "_p" + ".kr" : var4 + "_" + (var19 - 1) + "_p" + ".kr"));
            }

            var9 = false;
            boolean var20 = false;

            for(int var11 = 0; var11 < var18.length; ++var11) {
               boolean var12 = var18[var11].exists();
               if (!var12) {
                  File var13 = new NecroFile(var18[var11].getParentFile(), var18[var11].getName().toLowerCase());
                  var12 = var13.exists();
               }

               var20 = var20 || var12;
               boolean var23 = var16[var11].exists();
               if (!var23) {
                  File var14 = new NecroFile(var16[var11].getParentFile(), var16[var11].getName().toLowerCase());
                  var23 = var14.exists();
               }

               var9 = var9 || var23;
            }

            if (var9) {
               var6 = 2;
            } else if (var20) {
               var6 = 1;
            } else if (var1.toLowerCase().startsWith(var4.toLowerCase())) {
               var6 = this.getExtendedFileInfo((new NecroFile(var4)).getName());
            } else {
               var6 = this.getExtendedFileInfo((new NecroFile(var1.substring(0, var1.length() - ".frm.enyk".length()))).getName());
            }

            if (var2 == 0) {
               return new Integer(var6);
            } else {
               Vector var21 = new Vector();

               for(int var22 = 0; var22 < var18.length; ++var22) {
                  if (var18[var22].exists()) {
                     var21.add(var18[var22].getAbsolutePath());
                  }
               }

               return var21;
            }
         } else {
            var5 = var5.toLowerCase();
            String[] var7 = new String[4];
            var4 = (new NecroFile(var5)).getName();
            var7[0] = PropertyList.USER_IN_FILENAME + (var4.endsWith(".frm.enyk") ? var4.substring(0, var4.indexOf(".frm.enyk")) + ".kr" : var4 + ".kr");

            for(int var8 = 1; var8 < var7.length; ++var8) {
               var7[var8] = PropertyList.USER_IN_FILENAME + (var4.endsWith(".frm.enyk") ? var4.substring(0, var4.indexOf(".frm.enyk")) + "_" + (var8 - 1) + "_p" + ".kr" : var4 + "_" + (var8 - 1) + "_p" + ".kr");
            }

            boolean var17 = false;
            var9 = false;

            for(int var10 = 0; var10 < var7.length; ++var10) {
               var9 = var9 || destFiles.contains(var7[var10]);
               var17 = var17 || sentFiles.contains(var7[var10]);
            }

            if (var17) {
               var6 = 2;
            } else if (var9) {
               var6 = 1;
            } else if (var1.toLowerCase().startsWith(var4.toLowerCase())) {
               var6 = this.getExtendedFileInfo(var4);
            } else {
               var6 = this.getExtendedFileInfo(var1.substring(0, var1.length() - ".frm.enyk".length()));
            }

            return new Integer(var6);
         }
      } catch (Exception var15) {
         ErrorList.getInstance().writeError(RESOURCE_ERROR_ID, "Általános hiba", var15, (Object)null);
         return var2 == 0 ? new Integer(-1) : null;
      }
   }

   public int getExtendedFileInfo(String var1) {
      String var2 = this.destPath + var1 + ".xcz" + "_status";
      if ((new NecroFile(var2)).exists()) {
         return 5;
      } else {
         var2 = this.srcPath + var1 + File.separator + "alairt";
         if ((new NecroFile(var2)).exists()) {
            if (this.isAvdhSigned(var1)) {
               return 4;
            } else {
               return this.isExternalSigned(var1) ? 3 : 0;
            }
         } else {
            return 0;
         }
      }
   }

   private boolean isAvdhSigned(String var1) {
      String var2 = this.srcPath + var1 + File.separator + "alairt" + File.separator + var1 + ".urlap.anyk.ASiC";
      return (new NecroFile(var2)).exists();
   }

   private boolean isExternalSigned(String var1) {
      String var2 = this.srcPath + var1 + File.separator + "alairt";
      File var3 = new NecroFile(var2);
      if (!var3.exists()) {
         return this.isExternalPassed(var1);
      } else {
         String[] var4 = var3.list();
         return var4.length != 1 ? this.isExternalPassed(var1) : true;
      }
   }

   private boolean isExternalPassed(String var1) {
      String var2 = this.srcPath + var1 + File.separator + "alairando";
      File var3 = new NecroFile(var2);
      if (!var3.exists()) {
         return false;
      } else {
         String[] var4 = var3.list();
         return var4.length != 1 ? false : var4[0].toLowerCase().endsWith((var1 + "_lenyomat" + ".xml").toLowerCase());
      }
   }

   public void startBatchMode() {
      counter = 0;

      try {
         destFiles = new HashSet();
         sentFiles = new HashSet();
         File[] var1 = (new NecroFile(this.destPath)).listFiles();
         File[] var2 = (new NecroFile(this.sentPath)).listFiles();

         int var3;
         for(var3 = 0; var3 < var1.length; ++var3) {
            destFiles.add(var1[var3].getName().toLowerCase());
         }

         for(var3 = 0; var3 < var2.length; ++var3) {
            sentFiles.add(var2[var3].getName().toLowerCase());
         }
      } catch (Exception var4) {
         this.stopBatchMode();
      }

   }

   public void stopBatchMode() {
      destFiles = null;
      sentFiles = null;
   }
}
