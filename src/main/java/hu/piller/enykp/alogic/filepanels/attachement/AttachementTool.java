package hu.piller.enykp.alogic.filepanels.attachement;

import hu.piller.enykp.alogic.ebev.AttachementException;
import hu.piller.enykp.alogic.fileutil.DatastoreKeyToXml;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class AttachementTool {
   public static final int ATC_TYPE_ERROR = 1;
   public static final int ATC_EXTENSION_ERROR = 2;
   public static final int ATC_OK = 0;
   private static String[] atcs = null;
   public static final File mentesekPath;

   public static Vector parseAtcDataFromTemplate(Vector var0, int var1) throws AttachementException, Exception {
      Vector var2 = new Vector();
      Object var4 = var0.get(var1);
      if (var4 instanceof String) {
         throw new NoSuchFieldException();
      } else {
         Vector var5 = (Vector)var4;
         if (var5.size() == 0) {
            return null;
         } else {
            Hashtable var6 = (Hashtable)var5.get(0);
            if (var6.containsKey("size") && var6.containsKey("sum_size")) {
               String var7 = (String)var6.get("size");
               int var8 = Integer.parseInt(var7.substring(0, var7.length() - 2));
               if (var7.endsWith("KB")) {
                  var8 *= 1024;
               } else if (var7.endsWith("MB")) {
                  var8 = var8 * 1024 * 1024;
               }

               var7 = (String)var6.get("sum_size");
               int var9 = Integer.parseInt(var7.substring(0, var7.length() - 2));
               if (var7.endsWith("KB")) {
                  var9 *= 1024;
               } else if (var7.endsWith("MB")) {
                  var9 = var9 * 1024 * 1024;
               }

               AttachementInfo[] var3 = new AttachementInfo[Math.max(var5.size() - 1, 1)];
               if (var5.size() == 1) {
                  return null;
               } else {
                  for(int var10 = 1; var10 < var5.size(); ++var10) {
                     Hashtable var11 = (Hashtable)var5.elementAt(var10);
                     var3[var10 - 1] = new AttachementInfo(var11);
                  }

                  var2.add(new Integer(var8));
                  var2.add(new Integer(var9));
                  var2.add(var3);
                  return var2;
               }
            } else {
               throw new AttachementException("ATCSIZES");
            }
         }
      }
   }

   public static Result fullCheck(BookModel var0, File var1, String var2) throws AttachementException, Exception {
      Result var3 = new Result();
      Vector var4 = parseAtcDataFromTemplate(var0.attachementsall, var0.getIndex(var0.get(var2)));
      Vector var5 = null;
      if (var4 == null) {
         return var3;
      } else {
         int var6 = (Integer)var4.get(0);
         int var7 = (Integer)var4.get(1);
         long var8 = 0L;
         AttachementInfo[] var10 = (AttachementInfo[])((AttachementInfo[])var4.get(2));

         try {
            Object var11 = loadAtcFile(var1);
            if (var11 instanceof String) {
               var3.errorList.add("Hiba a csatolmány-leíró betöltésekor:\n" + var11);
               throw new Exception();
            }

            var5 = (Vector)var11;
         } catch (Exception var18) {
            var3.setOk(false);
            var3.errorList.add("Hiba a csatolmány-leíró betöltésekor");
            return var3;
         }

         Hashtable var19 = new Hashtable();
         boolean var12 = false;

         int var14;
         for(var14 = 0; var14 < var5.size(); ++var14) {
            String var15 = ((String[])((String[])var5.get(var14)))[2];
            String var16 = ((String[])((String[])var5.get(var14)))[0];
            if (var19.containsKey(var15)) {
               var19.put(var15, new Integer((Integer)var19.get(var15) + 1));
            } else {
               var19.put(var15, new Integer(1));
            }

            int var20 = isGoodAttachemetType(var10, var15, var16);
            if (var20 != 0) {
               var3.setOk(false);
               if (var20 == 1) {
                  var3.errorList.add(var15 + " típusú csatolmány nem csatolható a nyomtatványhoz");
               }

               if (var20 == 2) {
                  if (var16.lastIndexOf(".") > -1) {
                     var3.errorList.add(var16.substring(var16.lastIndexOf(".")) + " kiterjesztésű fájl nem csatolható a nyomtatványhoz");
                  } else {
                     var3.errorList.add("A " + var16 + " fájlnak nincs kiterjesztése, így nem csatolható a nyomtatványhoz");
                  }
               }

               return var3;
            }

            File var13 = new File(var16);
            if (var13.length() == 0L) {
               var3.errorList.add("A " + var13.getName() + " csatolmány 0 hosszú!");
               return var3;
            }

            int var17 = checkFileSize(var13, var15, var10, (long)var6, (long)var7, var8);
            if (var17 != 0) {
               var3.setOk(false);
               if (var17 == 1) {
                  var3.errorList.add("A " + var13.getName() + " csatolmány mérete átlépte a megadott értékhatárt !");
               } else if (var17 == 2) {
                  var3.errorList.add("A csatolmányok összesített mérete átlépte a megadott értékhatárt !");
               }

               return var3;
            }

            var8 += var13.length();
         }

         for(var14 = 0; var14 < var10.length; ++var14) {
            AttachementInfo var22 = var10[var14];
            if (var19.containsKey(var22.description)) {
               var22.attached = (Integer)var19.get(var22.description);
            }
         }

         String var21 = checkRequirement(var10);
         if (var21.length() > 0) {
            var3.setOk(false);
            var3.errorList.add(var21);
            return var3;
         } else {
            if (var5.size() > 0) {
               var3.errorList.insertElementAt(var5, 0);
            }

            return var3;
         }
      }
   }

   public static int isGoodAttachemetType(AttachementInfo[] var0, String var1, String var2) {
      for(int var3 = 0; var3 < var0.length; ++var3) {
         if (var0[var3].description.equalsIgnoreCase(var1)) {
            return isGoodAttachemetExtension(var0[var3].exts, var2);
         }
      }

      return 1;
   }

   public static int isGoodAttachemetExtension(String var0, String var1) {
      if (var0.indexOf("*") > -1) {
         return 0;
      } else {
         String[] var2 = var0.toLowerCase().split(";");

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var1.toLowerCase().endsWith("." + var2[var3])) {
               return 0;
            }
         }

         return 2;
      }
   }

   public static String checkRequirement(AttachementInfo[] var0) {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         AttachementInfo var3 = var0[var2];
         if (var3.required) {
            if (var3.attached < 1) {
               var1.append(var3.description).append(" csatolmányból kötelező csatolnia a nyomtatványhoz!\n");
            }

            if (var3.attached < var3.minCount && var3.minCount > 0) {
               var1.append(var3.description).append(" csatolmányból legalább ").append(var3.minCount).append(" db-ot csatolnia kell a nyomtatványhoz!\n");
            }
         }

         if (var3.attached > var3.maxCount && var3.maxCount > 0) {
            var1.append(var3.description).append(" csatolmányból legfeljebb ").append(var3.maxCount).append(" db-ot csatolhat a nyomtatványhoz!\n");
         }
      }

      return var1.toString();
   }

   public static int checkFileSize(File var0, String var1, AttachementInfo[] var2, long var3, long var5, long var7) {
      if (var3 == 0L && var5 == 0L) {
         return 0;
      } else {
         for(int var9 = 0; var9 < var2.length; ++var9) {
            if (var2[var9].description.equalsIgnoreCase(var1)) {
               if (var3 != 0L && var0.length() > var3) {
                  return 1;
               }

               if (var5 != 0L && var0.length() > var5 - var7) {
                  return 2;
               }

               return 0;
            }
         }

         return -1;
      }
   }

   public static Vector crateCsatolmanyInfo2DocMetaData(File var0, int var1) throws AttachementException, Exception {
      Vector var2 = new Vector();
      Object var3 = null;

      try {
         hu.piller.xml.abev.element.CsatolmanyInfo var4 = new hu.piller.xml.abev.element.CsatolmanyInfo();
         var4.setAzon("CSA_" + Integer.toString(var2.size() + var1));
         String var5 = var0.getAbsolutePath();
         if (var5.endsWith(".atc")) {
            var5 = var5.substring(0, var5.length() - ".atc".length()) + ".cst";
         }

         if (var5.endsWith(".frm.enyk")) {
            var5 = var5.substring(0, var5.length() - ".frm.enyk".length()) + ".cst";
         }

         if (var5.endsWith(".xml")) {
            var5 = var5.substring(0, var5.length() - ".xml".length()) + ".cst";
         }

         File var6 = new File(var5);
         var4.setFileNev(var6.getName());
         var4.setFileURI(DatastoreKeyToXml.htmlConvert(var6.toURI().toString()));
         var2.add(var4);
      } catch (Exception var7) {
         Tools.eLog(var7, 0);
      }

      if (var3 != null) {
         throw new FileNotFoundException((String)var3);
      } else {
         return var2;
      }
   }

   public static String checkAttachement(BookModel var0, Vector var1) {
      boolean var2 = false;
      boolean var3 = false;
      byte var4 = 0;
      Hashtable var5 = var0.get_templateheaddata();
      Hashtable var6 = (Hashtable)var5.get("docinfo");
      String var7 = Tools.getFormIdFromFormAttrs(var0);
      if (var7 != null) {
         var6.put("attachment", var7);
      }

      String var8 = (String)var6.get("attachment");
      if (var8.equals("2") && var1.size() == 0) {
         return "A nyomtatványhoz kötelező csatolmányt megadni!";
      } else {
         Vector var9 = var0.attachementsall;

         Vector var10;
         try {
            var10 = parseAtcDataFromTemplate(var9, 0);
            if (var10 == null && (var0.splitesaver.equals("false") || var0.cc.size() > 0)) {
               var10 = parseAtcDataFromTemplate(var9, 1);
            }
         } catch (NoSuchFieldException var21) {
            return "";
         } catch (Exception var22) {
            var10 = null;
         }

         if (var10 == null) {
            return "Hiba a nyomtatványsablon feldolgozásakor (ATTACHEMENT) !";
         } else {
            int var23 = (Integer)var10.get(0);
            int var24 = (Integer)var10.get(1);
            Hashtable var11 = new Hashtable();
            Object[] var12 = (Object[])((Object[])var10.get(2));

            for(int var13 = 0; var13 < var12.length; ++var13) {
               if (var12[var13] instanceof AttachementInfo) {
                  AttachementInfo var14 = (AttachementInfo)var12[var13];
                  var11.put(var14.description, var14);
               }
            }

            StringBuffer var25 = new StringBuffer();
            Hashtable var26 = new Hashtable();

            for(int var16 = 0; var16 < var1.size(); ++var16) {
               String[] var17 = (String[])((String[])var1.elementAt(var16));
               if (!var11.containsKey(var17[2])) {
                  return var17[2] + " típusú csatolmány nem csatolható ehhez a nyomtatványhoz!";
               }

               File var18 = new File(var17[0]);
               if (var23 > 0 && var18.length() > (long)var23) {
                  var25.append(var17[0]).append(" mérete nagyobb a megengedettnél (").append(var18.length()).append(" > ").append(var23).append("\n");
               } else {
                  if (var24 > 0 && (long)var4 + var18.length() > (long)var24) {
                     var25.append(var17[0]).append(" csatolmánnyal a csatolmányok összesített mérete nagyobb a megengedettnél (").append((long)var4 + var18.length()).append(" > ").append(var24).append("\n");
                     break;
                  }

                  int var15;
                  try {
                     var15 = (Integer)var26.get(var17[2]);
                  } catch (Exception var20) {
                     var15 = 0;
                  }

                  var26.put(var17[2], new Integer(var15 + 1));
               }
            }

            if (var25.length() == 0) {
               Enumeration var27 = var26.keys();

               while(var27.hasMoreElements()) {
                  String var28 = (String)var27.nextElement();
                  AttachementInfo var29 = (AttachementInfo)var11.get(var28);
                  Integer var19 = (Integer)var26.get(var28);
                  if (var29.required && var19 == 0) {
                     return var28 + " csatományból legalább 1 db-ot csatolnia kell a nyomtatványhoz";
                  }

                  if (var29.maxCount < var19 && var29.maxCount > 0) {
                     return var28 + " csatományból legfeljebb " + var29.maxCount + " db-ot csatolhat a nyomtatványhoz";
                  }

                  if (var29.minCount > var19) {
                     return var28 + " csatományból legalább " + var29.minCount + " db-ot csatolnia kell a nyomtatványhoz";
                  }
               }
            }

            return var25.toString();
         }
      }
   }

   public static Object loadAtcFile(File var0) throws Exception {
      return loadAtcFile(var0, true);
   }

   public static Object loadAtcFile(File var0, boolean var1) throws Exception {
      String var2 = null;
      Vector var3 = new Vector();
      boolean var4 = true;
      boolean var5 = true;
      BufferedReader var6 = new BufferedReader(new InputStreamReader(new FileInputStream(var0), "utf-8"));

      try {
         HashSet var8 = new HashSet();

         String var7;
         String[] var9;
         File var10;
         String var11;
         while((var7 = var6.readLine()) != null && var2 == null) {
            var7 = var7.trim();
            if (var7.length() != 0) {
               if (var5) {
                  if (!var7.startsWith("encoding=")) {
                     var4 = false;
                     var6.close();
                     break;
                  }

                  var5 = false;
               } else {
                  try {
                     var9 = var7.split(";");
                     var10 = new File(var9[0]);
                     if (var1 && !var10.exists()) {
                        var2 = "A " + var7 + " sor csatolmánya nem található!\nAz \"Adatok/Csatolmányok kezelése\" menüpont segítségével javíthatja a hibát!";
                        var11 = var2;
                        return var11;
                     }

                     if (!var8.contains(var9[0].toLowerCase())) {
                        var3.add(var9);
                        var8.add(var9[0].toLowerCase());
                     }
                  } catch (Exception var24) {
                     Tools.eLog(var24, 0);
                  }
               }
            }
         }

         if (var4) {
            return var3;
         } else {
            var6 = new BufferedReader(new FileReader(var0));

            while((var7 = var6.readLine()) != null) {
               try {
                  var9 = var7.split(";");
                  var10 = new File(var9[0]);
                  if (var1 && !var10.exists()) {
                     var2 = "A " + var7 + " sor csatolmánya nem található!\nAz \"Adatok/Csatolmányok kezelése\" menüpont segítségével javíthatja a hibát!";
                     var11 = var2;
                     return var11;
                  }

                  var3.add(var9);
               } catch (Exception var23) {
                  Tools.eLog(var23, 0);
               }
            }

            return var3;
         }
      } finally {
         try {
            var6.close();
         } catch (Exception var22) {
            Tools.eLog(var22, 0);
         }

      }
   }

   public static boolean hasAttachement(BookModel var0) {
      try {
         File var1 = null;

         try {
            var1 = var0.cc.getLoadedfile();
         } catch (Exception var3) {
            return false;
         }

         return hasAttachement(var0, var1);
      } catch (Exception var4) {
         return false;
      }
   }

   public static boolean hasAttachement(BookModel var0, File var1) {
      try {
         Vector var2 = getAtcFilenames(var1.getAbsolutePath(), var0);

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            File var4 = new File((String)var2.elementAt(var3));
            if (var4.exists()) {
               return true;
            }
         }

         return false;
      } catch (Exception var5) {
         return false;
      }
   }

   public static int countAttachement(BookModel var0) {
      String var1 = null;

      try {
         if (var0.cc.getLoadedfile() == null) {
            return 0;
         }

         var1 = var0.cc.getLoadedfile().getAbsolutePath();
      } catch (Exception var8) {
         ErrorList.getInstance().writeError(new Long(4001L), "Nem sikerült a fájlnév megszerzése a csatolmány ellenőrzéséhez.", (Exception)null, (Object)null);
         return 0;
      }

      try {
         Vector var2 = getAtcFilenames(var1, var0);
         int var3 = 0;
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            String var5 = var4.next().toString();
            File var6 = new File(var5);
            if (var6.exists()) {
               var3 += loadAndCountAtcFile(new File(var5));
            }
         }

         return var3;
      } catch (Exception var7) {
         ErrorList.getInstance().writeError(new Long(4001L), "Hiba a csatolmányok számolásakor", (Exception)null, (Object)null);
         return 0;
      }
   }

   public static int countAttachement_old(BookModel var0) {
      String var1 = null;

      try {
         if (var0.cc.getLoadedfile() == null) {
            return 0;
         }

         var1 = var0.cc.getLoadedfile().getAbsolutePath();
      } catch (Exception var5) {
         ErrorList.getInstance().writeError(new Long(4001L), "Nem sikerült a fájlnév megszerzése a csatolmány ellenőrzéséhez.", (Exception)null, (Object)null);
         return 0;
      }

      try {
         String var2 = getAtcFilename(var1, var0);
         File var3 = new File(var2);
         return !var3.exists() ? 0 : loadAndCountAtcFile(new File(var2));
      } catch (Exception var4) {
         ErrorList.getInstance().writeError(new Long(4001L), "Hiba a csatolmányok számolásakor", (Exception)null, (Object)null);
         return 0;
      }
   }

   public static int loadAndCountAtcFile(File var0) throws Exception {
      int var1 = 0;
      BufferedReader var2 = new BufferedReader(new InputStreamReader(new FileInputStream(var0), "utf-8"));
      String var3 = null;

      while((var3 = var2.readLine()) != null) {
         try {
            if (!var3.toLowerCase().startsWith("encoding")) {
               String var4 = var3.substring(0, var3.indexOf(";"));
               if (!var4.endsWith(".anyk.ASiC")) {
                  File var5 = new File(var4);
                  if (var5.exists()) {
                     ++var1;
                  }
               }
            }
         } catch (Exception var7) {
            Tools.eLog(var7, 0);
         }
      }

      var2.close();
      return var1;
   }

   public static String getAtcFilename(String var0, BookModel var1) {
      String var2 = "";
      if (var0.indexOf(".frm.enyk") > -1) {
         var2 = var0.substring(0, var0.toLowerCase().indexOf(".frm.enyk")) + "_" + var1.get_formid() + ".atc";
      } else {
         int var3 = var0.toLowerCase().lastIndexOf(".");
         if (var3 < 0) {
            var2 = var0 + "_" + var1.get_formid() + ".atc";
         } else {
            var2 = var0.substring(0, var3) + "_" + var1.get_formid() + ".atc";
         }
      }

      File var5 = new File(var2);
      if (!var5.exists()) {
         String var4 = Tools.getTempDir();
         if (!var4.endsWith("\\") && !var4.endsWith("/")) {
            var4 = var4 + File.separator;
         }

         var2 = var4 + var5.getName();
      }

      return var2;
   }

   public static Vector getAtcFilenames(String var0, BookModel var1) {
      Vector var2 = new Vector();

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         FormModel var4 = var1.get(var3);
         String var5 = "";
         if (var0.toLowerCase().indexOf(".frm.enyk") > -1) {
            var5 = var0.substring(0, var0.toLowerCase().indexOf(".frm.enyk")) + "_" + var4.id + ".atc";
         } else {
            int var6 = var0.toLowerCase().lastIndexOf(".");
            if (var6 < 0) {
               var5 = var0 + "_" + var4.id + ".atc";
            } else {
               var5 = var0.substring(0, var6) + "_" + var4.id + ".atc";
            }
         }

         File var8 = new File(var5);
         if (!var8.exists()) {
            String var7 = Tools.getTempDir();
            if (!var7.endsWith("\\") && !var7.endsWith("/")) {
               var7 = var7 + File.separator;
            }

            var5 = var7 + var8.getName();
         }

         var2.add(var5);
      }

      return var2;
   }

   public static Map<String, Map<String, Vector>> getAttachementList(String[] var0) {
      HashMap var1 = new HashMap();
      String[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];

         try {
            String var6;
            if (var5.toLowerCase().endsWith(".frm.enyk")) {
               var6 = var5.substring(0, var5.toLowerCase().indexOf(".frm.enyk"));
            } else {
               var6 = var5.substring(0, var5.length() - 4);
            }

            HashMap var7 = new HashMap();
            var1.put(var5, var7);
            String[] var8 = atcs;
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               String var11 = var8[var10];
               if (var6 != null && var11.startsWith(var6)) {
                  Object var12 = loadAtcFile(new File(mentesekPath.getAbsolutePath() + File.separator + var11));
                  if (!(var12 instanceof String)) {
                     String var13 = var11.replaceAll(var6, "").replaceAll(".atc", "");
                     var7.put(var13, (Vector)var12);
                  }
               }
            }
         } catch (Exception var14) {
         }
      }

      return var1;
   }

   public static String[] getAtcs() {
      return atcs;
   }

   public static void fillFileList() {
      atcs = mentesekPath.list(new AttachementTool.ATCFilenameFilter());
   }

   public static void fillFileList(File var0) {
      atcs = var0.list(new AttachementTool.ATCFilenameFilter());
   }

   public static void dropFileList() {
      atcs = null;
   }

   public static Vector mergeCstFiles(Vector var0) throws Exception {
      Vector var1 = new Vector();

      for(int var2 = 0; var2 < var0.size(); ++var2) {
         try {
            Vector var3 = (Vector)var0.elementAt(var2);

            for(int var4 = 0; var4 < var3.size(); ++var4) {
               var1.add(var3.elementAt(var4));
            }
         } catch (ClassCastException var5) {
         }
      }

      String var6 = checkAtcDup(var1);
      if (var6 != null) {
         throw new Exception(var6);
      } else {
         return var1;
      }
   }

   public static String[] getAtcPaths(BookModel var0) {
      String[] var1 = new String[var0.forms.size()];
      String var2 = var0.cc.getLoadedfile().getName();

      for(int var3 = 0; var3 < var0.forms.size(); ++var3) {
         FormModel var4 = (FormModel)var0.forms.elementAt(var3);
         var1[var3] = var2.substring(0, var2.toLowerCase().indexOf(".frm.enyk")) + File.separator + var4.id + File.separator;
      }

      return var1;
   }

   public static String checkAttachmentDuplication(BookModel var0, String var1, Vector var2) {
      String var3 = "";

      try {
         String[] var4 = getAtcPaths(var0);
         if (var4.length < 2) {
            return "";
         }

         String[] var5 = (new File(var1 + var4[0])).list();
         if (var5 == null) {
            return "";
         }

         for(int var6 = 1; var6 < var4.length; ++var6) {
            String[] var7 = (new File(var1 + var4[var6])).list();
            if (var7 != null) {
               for(int var8 = 0; var8 < var7.length; ++var8) {
                  for(int var9 = 0; var9 < var5.length; ++var9) {
                     if (var7[var8].equalsIgnoreCase(var5[var9]) && fileInTheAtcList(var2, var0.get_formid() + File.separator + var5[var9])) {
                        var3 = var3 + var5[var9] + "\n";
                     }
                  }
               }
            }
         }
      } catch (Exception var10) {
         Tools.eLog(var10, 0);
         var10.printStackTrace();
      }

      return var3;
   }

   private static boolean fileInTheAtcList(Vector var0, String var1) {
      for(int var2 = 0; var2 < var0.size(); ++var2) {
         Object[] var3 = (Object[])((Object[])var0.elementAt(var2));
         if (var3[0].toString().endsWith(var1)) {
            return true;
         }
      }

      return false;
   }

   private static String checkAtcDup(Vector var0) {
      String var1 = "";
      HashSet var2 = new HashSet();

      for(int var3 = 0; var3 < var0.size(); ++var3) {
         String[] var4 = (String[])((String[])var0.elementAt(var3));
         File var5 = new File(var4[0]);
         if (var2.contains(var5.getName())) {
            var1 = var1 + var5.getName() + "\n";
         }

         var2.add(var5.getName());
      }

      if (var1.length() > 0) {
         var1 = "Az alábbi csatolmányok több részbizonylathoz is csatolva vannak.\n" + var1;
         return var1;
      } else {
         return null;
      }
   }

   public static void setAttachmentList(Vector var0) {
      try {
         Vector var1 = new Vector();
         Vector var2 = new Vector();

         for(int var3 = 0; var3 < var0.size(); ++var3) {
            Vector var4 = (Vector)var0.elementAt(var3);

            for(int var5 = 0; var5 < var4.size(); ++var5) {
               String[] var6 = (String[])((String[])var4.elementAt(var5));
               if (var6[0].toLowerCase().endsWith(".pdf")) {
                  var2.add(var6[0]);
               }

               var1.add(var6[0] + " - " + var6[2] + (var6[1].equals("") ? "" : " (" + var6[1] + ")"));
            }
         }

         if (PropertyList.getInstance().get("prop.dynamic.ebev_call_from_xmlpost") != null) {
            PropertyList.getInstance().set("prop.dynamic.ebev_call_from_xmlpost", var1);
         }

         if (PropertyList.getInstance().get("prop.dynamic.ebev_call_from_menu") != null) {
            PropertyList.getInstance().set("prop.dynamic.ebev_call_from_menu", var1);
         }

         if (var2.size() > 0) {
            PropertyList.getInstance().set("prop.dynamic.attached_pdf_names", var2);
         }
      } catch (Exception var7) {
         Tools.eLog(var7, 0);
      }

   }

   public static String getAtcFilename(BookModel var0) {
      try {
         String var1 = var0.cc.getLoadedfile().getAbsolutePath();
         if (var1.toLowerCase().indexOf(".frm.enyk") > -1) {
            var1 = var1.substring(0, var1.toLowerCase().indexOf(".frm.enyk")) + "_" + var0.get_formid() + ".atc";
         } else {
            int var2 = var1.toLowerCase().lastIndexOf(".");
            if (var2 < 0) {
               var1 = var1 + "_" + var0.get_formid() + ".atc";
            } else {
               var1 = var1.substring(0, var2) + "_" + var0.get_formid() + ".atc";
            }
         }

         return (new File(var1)).exists() ? var1 : null;
      } catch (Exception var3) {
         return null;
      }
   }

   public int deleteAttachment(BookModel var1, String var2) {
      boolean var3 = false;
      String var4 = var1.cc.getLoadedfile().getAbsolutePath();
      String var5 = var4.substring(0, var4.toLowerCase().indexOf(".frm.enyk")) + "_" + var2 + ".atc";
      String var6 = this.getPath();
      boolean var7 = true;
      File var8 = new File(var5);
      if (var8.exists()) {
         try {
            var7 = this.deleteAllAttachement(var6, var1);
         } catch (Exception var10) {
            Tools.eLog(var10, 0);
         }

         if (!var7) {
            return 1;
         }

         var7 = var8.delete();
         if (!var7) {
            return 2;
         }
      }

      return 0;
   }

   private String getPath() {
      String var1;
      try {
         var1 = (String)PropertyList.getInstance().get("prop.usr.root");
         if (var1 == null) {
            throw new Exception();
         }
      } catch (Exception var5) {
         var1 = "";
      }

      if (!var1.endsWith("\\") && !var1.endsWith("/")) {
         var1 = var1 + File.separator;
      }

      String var2;
      try {
         var2 = (String)PropertyList.getInstance().get("prop.usr.attachment");
         if (var2 == null) {
            throw new Exception();
         }

         var2 = var1 + var2;
      } catch (Exception var4) {
         var2 = "";
      }

      if (!var2.endsWith("\\") && !var2.endsWith("/")) {
         var2 = var2 + File.separator;
      }

      return Tools.beautyPath(var2);
   }

   private boolean deleteAllAttachement(String var1, BookModel var2) {
      boolean var3 = true;
      String var4 = var1 + this.getFileName(var2);
      File[] var5 = this.getDirList(var4);

      for(int var6 = 0; var6 < var5.length; ++var6) {
         if (!var5[var6].delete()) {
            var3 = false;
         }
      }

      File var9 = new File(var4);
      var9.delete();

      try {
         var9.getParentFile().delete();
      } catch (Exception var8) {
         Tools.eLog(var8, 0);
      }

      return var3;
   }

   private File[] getDirList(String var1) {
      File var2 = new File(var1);
      return var2.exists() && var2.isDirectory() ? var2.listFiles() : null;
   }

   private String getFileName(BookModel var1) {
      String var2 = var1.cc.getLoadedfile().getName();
      return var2.substring(0, var2.toLowerCase().indexOf(".frm.enyk")) + File.separator + var1.get_formid() + File.separator;
   }

   static {
      mentesekPath = new File(PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.saves"));
   }

   static class ATCFilenameFilter implements FilenameFilter {
      public boolean accept(File var1, String var2) {
         return this.isAtcFilename(var2);
      }

      private boolean isAtcFilename(String var1) {
         return var1.endsWith(".atc");
      }
   }
}
