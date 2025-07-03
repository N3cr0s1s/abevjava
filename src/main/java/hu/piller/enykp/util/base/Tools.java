package hu.piller.enykp.util.base;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.calculator.calculator_c.MatrixSearchItem;
import hu.piller.enykp.alogic.calculator.calculator_c.MatrixSearchModel;
import hu.piller.enykp.alogic.calculator.lookup.LookupListHandler;
import hu.piller.enykp.alogic.calculator.matrices.IMatrixHandler;
import hu.piller.enykp.alogic.calculator.matrices.defaultMatrixHandler;
import hu.piller.enykp.alogic.ebev.SendParams;
import hu.piller.enykp.alogic.filepanels.attachement.AttachementTool;
import hu.piller.enykp.alogic.filesaver.xml.ErrorListListener4XmlSave;
import hu.piller.enykp.alogic.fileutil.FileNameResolver;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.alogic.templateutils.FieldsGroups;
import hu.piller.enykp.alogic.templateutils.IFieldsGroupModel;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeManager;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import hu.piller.enykp.datastore.Adonemek;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IResult;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class Tools {
   public static final int NO_LOG = 0;
   public static final int LOG = 1;
   public static final int FILE_OK = 0;
   public static final int FILE_IS_NULL = 1;
   public static final int FILE_NOT_EXISTS = 2;
   private static String[] atcFiles;
   private static String[] cstFiles;
   private static HashSet attachments = new HashSet();
   public static final int TERVEZET_FID_STATUS_CONTINUE = 0;
   public static final int TERVEZET_FID_STATUS_DONT_ADD = 1;

   public static String getString(Object var0, String var1) {
      return var0 == null ? var1 : var0.toString();
   }

   public static String getString(Object var0) {
      return var0 == null ? "" : var0.toString();
   }

   public static String fillPath(String var0) {
      if (var0 == null) {
         return File.separator;
      } else {
         return !var0.endsWith("\\") && !var0.endsWith("/") ? var0 + File.separator : var0;
      }
   }

   public static String beautyPath(String var0) {
      if (File.separator.equals("\\")) {
         var0 = var0.replaceAll("/", "\\\\");
      } else {
         var0 = var0.replaceAll("\\\\", "/");
      }

      return var0;
   }

   public static String fullService(String var0, boolean var1) throws Exception {
      String var2 = getString(PropertyList.getInstance().get(var0));
      if (var2 == null) {
         throw new Exception();
      } else {
         if (var1) {
            File var3 = new File(var2);
            if (!var3.exists() || !var3.isDirectory()) {
               throw new Exception();
            }
         }

         var2 = fillPath(var2);
         return beautyPath(var2);
      }
   }

   public static String ht2string(Hashtable var0, String[] var1) {
      try {
         StringBuffer var2 = new StringBuffer();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2.append("|");
            var2.append(var0.get(var1[var3]));
         }

         return var2.substring(1);
      } catch (Exception var4) {
         return "";
      }
   }

   public static BufferedImage createBI(JComponent var0) {
      var0.validate();
      Dimension var1 = var0.getSize();
      if (var1.width == 0) {
         var1 = var0.getPreferredSize();
      }

      BufferedImage var2 = new BufferedImage(var1.width, var1.height, 1);
      Graphics2D var3 = var2.createGraphics();
      var0.paint(var3);
      var3.dispose();
      return var2;
   }

   private static String maskMinus(String var0, String var1) {
      if (var0 == null) {
         return null;
      } else if (var1 == null) {
         return var0;
      } else if (!var1.equals("") && !var1.startsWith("%")) {
         StringBuffer var2 = new StringBuffer();

         for(int var3 = 0; var3 < var0.length(); ++var3) {
            try {
               if (var0.charAt(var3) != var1.charAt(var3)) {
                  var2.append(var0.charAt(var3));
               }
            } catch (Exception var5) {
               eLog(var5, 0);
            }
         }

         return var2.toString();
      } else {
         return var0;
      }
   }

   public static boolean superMatch(String var0, String var1, String var2) {
      return var0.matches(var1) ? true : maskMinus(var0, var2).matches(var1);
   }

   public static void resetLabels() {
      GuiUtil.resetLabels();
   }

   public static String getTimeStringForFiles() {
      SimpleDateFormat var0 = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");
      return var0.format(Calendar.getInstance().getTime());
   }

   public static Vector check() throws Exception {
      IErrorList var0 = ErrorList.getInstance();
      ErrorListListener4XmlSave var1 = new ErrorListListener4XmlSave(-1);
      Vector var2 = null;
      boolean var3 = false;
      CalculatorManager var4 = CalculatorManager.getInstance();
      var1.clearErrorList();
      if (var2 != null) {
         var2.clear();
      }

      ((IEventSupport)var0).addEventListener(var1);
      CalculatorManager.xml = true;
      var4.do_check_all((IResult)null, var1);
      CalculatorManager.xml = false;
      ((IEventSupport)var0).removeEventListener(var1);
      var2 = var1.getErrorList();
      if (var2.size() > 0) {
         for(int var5 = 0; var5 < var2.size() && !var3; ++var5) {
            TextWithIcon var6 = (TextWithIcon)var2.get(var5);
            var3 = var6.ii != null;
         }
      }

      if (!var3) {
         var2.clear();
         var2.add("A ellenőrzés nem talált hibát ");
      }

      return var2;
   }

   private int getErrorCount(Vector var1) {
      try {
         int var2 = 0;

         for(int var3 = 0; var3 < var1.size(); ++var3) {
            String var4;
            if (var1.elementAt(var3) instanceof String) {
               var4 = (String)var1.elementAt(var3);
            } else {
               var4 = ((TextWithIcon)var1.elementAt(var3)).text;
            }

            if (!var4.startsWith(" > ")) {
               ++var2;
            }
         }

         return var2;
      } catch (Exception var5) {
         return 0;
      }
   }

   public static JDialog createInitDialog(String var0, String var1) {
      Object var2 = MainFrame.thisinstance;
      if (var2 == null) {
         log("a mainframe null");
         var2 = new JFrame();
      }

      JDialog var3 = new JDialog((Frame)var2, var0, false);
      int var4 = ((JFrame)var2).getX() + ((JFrame)var2).getWidth() / 2 - 200;
      if (var4 < 0) {
         var4 = 0;
      }

      int var5 = ((JFrame)var2).getY() + ((JFrame)var2).getHeight() / 2 - 30;
      if (var5 < 0) {
         var5 = 0;
      }

      var3.setBounds(var4, var5, 400, 60);
      JPanel var6 = new JPanel(new BorderLayout());
      JProgressBar var7 = new JProgressBar(0);
      var7.setIndeterminate(true);
      var7.setStringPainted(true);
      var7.setString(var1);
      var6.add(var7, "Center");
      var3.getContentPane().add(var6, "Center");
      return var3;
   }

   public static JDialog createInitDialog(JDialog var0, String var1, String var2) {
      return createInitDialog(var0, var1, var2, false);
   }

   public static JDialog createInitDialog(JDialog var0, String var1, String var2, boolean var3) {
      if (var0 == null) {
         log("a mainframe null");
         return null;
      } else {
         JDialog var4 = new JDialog(var0, var1, var3);
         int var5 = var0.getX() + var0.getWidth() / 2 - 200;
         if (var5 < 0) {
            var5 = 0;
         }

         int var6 = var0.getY() + var0.getHeight() / 2 - 30;
         if (var6 < 0) {
            var6 = 0;
         }

         var4.setBounds(var5, var6, 400, 60);
         JPanel var7 = new JPanel(new BorderLayout());
         JProgressBar var8 = new JProgressBar(0);
         var8.setIndeterminate(true);
         var8.setStringPainted(true);
         var8.setString(var2);
         var7.add(var8, "Center");
         var4.getContentPane().add(var7, "Center");
         return var4;
      }
   }

   public static Result checkDisabled(Elem var0, BookModel var1) {
      Result var2 = new Result();
      String var3 = var0.getType();
      FormModel var4 = var1.get(var0.getType());
      Hashtable var5 = var1.get(var3).fids;
      Hashtable var6 = var1.get_enabled_fields(var0);
      Iterator var7 = ((IDataStore)var0.getRef()).getCaseIdIterator();

      while(var7.hasNext()) {
         StoreItem var8 = (StoreItem)var7.next();
         if (var8.value != null && !var8.value.equals("") && var8.index >= 0 && !var8.code.endsWith("H") && !var6.containsKey(var8.code)) {
            try {
               Hashtable var9 = ((DataFieldModel)var5.get(var8.code)).features;
               if (savable(var9, (String)var8.value) != null) {
                  int var10 = var4.get_field_pageindex(var8.code);
                  var2.setOk(false);
                  String var11 = "";

                  try {
                     var11 = var8.value.toString();
                     if (var11.equals("true")) {
                        var11 = "X";
                     }
                  } catch (Exception var13) {
                     eLog(var13, 0);
                  }

                  if (MainFrame.opmode.equals("0")) {
                     ErrorList.getInstance().writeError(new Long(4001L), "Súlyos Hiba!  A " + var4.get(var10).name + " nem kitölthető lapon a " + var8.code + " kódú mezőben " + var11 + " érték található. Törölje!", ErrorList.LEVEL_FATAL_ERROR, (Exception)null, "m006", (Object)"m006", (Object)("A " + var4.get(var10).name + " nem kitölthető lapon a " + var8.code + " kódú mezőben " + var11 + " érték található"));
                  } else if (!checkDisabledByRole(var1, var3, var4.get_field_pageindex(var8.code))) {
                     ErrorList.getInstance().writeError(new Long(4001L), "Hiba!  A " + var4.get(var10).name + " nem kitölthető lapon a " + var8.code + " kódú mezőben " + var11 + " érték található. Törölje!", ErrorList.LEVEL_ERROR, (Exception)null, "m006", (Object)"m006", (Object)("A " + var4.get(var10).name + " nem kitölthető lapon a " + var8.code + " kódú mezőben " + var11 + " érték található"));
                  }
               }
            } catch (Exception var15) {
               try {
                  var2.errorList.add("Hiba: " + var8.code);
               } catch (Exception var14) {
                  eLog(var14, 0);
               }
            }
         }
      }

      return var2;
   }

   public static boolean checkDisabledByRole(BookModel var0, String var1, int var2) {
      PageModel var3 = var0.get(var1).get(var2);
      int var4 = var3.role;
      int var5 = var3.getmask();
      return (var4 & var5) == 0;
   }

   private static String savable(Hashtable var0, String var1) {
      if (var0 != null) {
         try {
            if (var0.containsKey("visible") && ((String)var0.get("visible")).equalsIgnoreCase("no")) {
               return null;
            }

            if (var0.containsKey("readonly")) {
               return null;
            }

            if (((String)var0.get("datatype")).equalsIgnoreCase("check")) {
               if (var1.equalsIgnoreCase("true")) {
                  return "X";
               }

               return null;
            }

            if (var0.containsKey("mask") && var0.get("mask").toString().toLowerCase().startsWith(var1)) {
               return null;
            }
         } catch (Exception var3) {
            exception2SOut(var3);
         }
      }

      return var1;
   }

   public static File clearFileName(File var0) {
      String var1 = clearFileName(var0.getName());
      File var2 = new File(var0.getParent() + File.separator + var1);
      boolean var3 = true;
      if (!var1.equals(var0.getName())) {
         var3 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "A " + var0.getName() + " fájlnév problémát okozhat az Ügyfélkapus feladásnál. Átnevezzük " + var1 + " -re?\nAmennyiben a Nem-et választja, Önnek kell átneveznie a fájlt.", "Fájl átnevezés", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0;
         if (var3) {
            var3 = var0.renameTo(var2);

            try {
               Vector var4 = (Vector)PropertyList.getInstance().get("prop.usr.xcz.files");
               if (var4 != null) {
                  var4.add(var2.getAbsolutePath());
               }
            } catch (Exception var11) {
               eLog(var11, 0);
            }

            final String var12 = var0.getName().substring(0, var0.getName().indexOf(".xml"));
            File[] var5 = var0.getParentFile().listFiles(new FilenameFilter() {
               public boolean accept(File var1, String var2) {
                  if (!var2.endsWith(".atc")) {
                     return false;
                  } else {
                     return var2.startsWith(var12);
                  }
               }
            });

            for(int var6 = 0; var6 < var5.length; ++var6) {
               String var7 = clearFileName(var5[var6].getName());
               File var8 = new File(var0.getParent() + File.separator + var7);
               var3 &= var5[var6].renameTo(var8);

               try {
                  Vector var9 = (Vector)PropertyList.getInstance().get("prop.usr.xcz.files");
                  if (var9 != null) {
                     var9.add(var8.getAbsolutePath());
                  }
               } catch (Exception var10) {
                  eLog(var10, 0);
               }
            }

            if (!var3) {
               GuiUtil.showMessageDialog((Component)null, "Sajnos nem sikerült a fájl átnevezése!", "Fájl átnevezés hiba!", 0);
            }
         }
      }

      return var3 ? var2 : null;
   }

   public static String clearFileName(String var0) {
      String var1 = var0.substring(var0.lastIndexOf("."));
      String var2 = (new FileNameResolver((BookModel)null)).normalizeString(var0.substring(0, var0.lastIndexOf("."))) + var1;
      return var2;
   }

   public static String encrypt(String var0) {
      byte[] var1 = java.util.Base64.getEncoder().encode(var0.getBytes(Charset.forName("UTF-8")));
      return new String(var1);
   }

   public static String decrypt(String var0) {
      return new String(java.util.Base64.getDecoder().decode(var0.getBytes(Charset.forName("UTF-8"))), Charset.forName("UTF-8"));
   }

   public static void eLog(Throwable var0, int var1) {
      if (var1 != 0) {
         var0.printStackTrace();
      }
   }

   public static String getFormIdFromFormAttrs(BookModel var0) {
      return var0.get(var0.get_formid()).attachement;
   }

   public static boolean hasFatalError(Vector var0) {
      int var1 = 0;

      boolean var2;
      for(var2 = false; var1 < var0.size() && !var2; ++var1) {
         if (((TextWithIcon)var0.get(var1)).imageType == 1) {
            var2 = true;
         }
      }

      return var2;
   }

   public static int emptyPage(Vector var0, IDataStore var1) {
      for(int var2 = 0; var2 < var0.size(); ++var2) {
         DataFieldModel var3 = (DataFieldModel)var0.get(var2);
         String var4 = var1.get("0_" + var3.key);
         if (var4 != null && !var4.equals("") && (var3.features.get("copy_fld") == null || var3.features.get("copy_fld").equals("")) && var3.features.get("notinbarkod") == null && var3.features.get("DPageNumber") == null) {
            return 0;
         }
      }

      return 1;
   }

   public static int searchInTheMatrix(IDataStore var0, String var1, String var2, String var3, String var4, Integer var5) {
      try {
         FieldsGroups var6 = FieldsGroups.getInstance();
         IFieldsGroupModel var7 = var6.getFieldsGroupByGroupId(FieldsGroups.GroupType.STATIC, var1, var4);
         Hashtable var8 = var7.getFidsColumns();
         Hashtable var9 = getFriendValues(var8, var0, var5);
         Integer[] var10 = new Integer[var8.size()];
         Vector var11 = (Vector)var9.remove("INDEXES");
         var11.toArray(var10);
         int[] var12 = new int[var10.length];
         int var13 = 0;

         for(int var14 = 0; var14 < var12.length; ++var14) {
            var12[var14] = var10[var14] - 1;
            if (var10[var14] > var13) {
               var13 = var10[var14];
            }
         }

         Enumeration var20 = var9.keys();
         if (!var20.hasMoreElements()) {
            return -1;
         } else {
            Object var15 = var20.nextElement();
            Vector var16 = (Vector)var9.get(var15);
            ArrayList var17 = new ArrayList(var12.length);

            int var18;
            for(var18 = 0; var18 < var12.length; ++var18) {
               var17.add(new MatrixSearchItem(var12[var18], "=", var16.elementAt(var18), false));
            }

            var18 = whereIsItInTheMatrix(var1, var2, var17, var3);
            return var18;
         }
      } catch (Exception var19) {
         return -1;
      }
   }

   public static int whereIsItInTheMatrix(String var0, String var1, java.util.List<MatrixSearchItem> var2, String var3) {
      IMatrixHandler var4 = defaultMatrixHandler.getInstance();
      MatrixSearchModel var5 = new MatrixSearchModel(var1, var3);
      Iterator var6 = var2.iterator();

      while(var6.hasNext()) {
         MatrixSearchItem var7 = (MatrixSearchItem)var6.next();
         var5.addSearchItem(var7);
      }

      Vector var8 = var4.search(var0, var5, true, true);
      return var8 == null ? -1 : (Integer)var8.elementAt(0);
   }

   private static Hashtable getFriendValues(Hashtable var0, IDataStore var1, Integer var2) {
      Hashtable var3 = new Hashtable();
      Enumeration var4 = var0.keys();

      while(var4.hasMoreElements()) {
         String var5 = (String)var4.nextElement();
         putIntoHt(var3, var2, var1.get(var2 + "_" + var5) == null ? "" : var1.get(var2 + "_" + var5));
         putIntoHt(var3, "INDEXES", Integer.valueOf((String)var0.get(var5)));
      }

      return var3;
   }

   private static void putIntoHt(Hashtable var0, Object var1, Object var2) {
      Vector var3;
      if (var0.containsKey(var1)) {
         var3 = (Vector)var0.get(var1);
      } else {
         var3 = new Vector();
      }

      var3.add(var2);
      var0.put(var1, var3);
   }

   public static Vector getComboList(DataFieldModel var0) {
      String var1;
      try {
         var1 = (String)var0.features.get("matrix_id");
      } catch (Exception var11) {
         var1 = "";
      }

      String var2;
      try {
         var2 = (String)var0.features.get("matrix_delimiter");
      } catch (Exception var10) {
         var2 = ";";
      }

      String var3;
      try {
         var3 = (String)var0.features.get("matrix_field_col_num");
      } catch (Exception var9) {
         var3 = "-1";
      }

      String var4;
      try {
         var4 = (String)var0.features.get("matrix_field_list");
      } catch (Exception var8) {
         var4 = "";
      }

      if ((String)var0.features.get("list") == null && (String)var0.features.get("values") == null) {
         try {
            Integer var5 = (Integer)var0.features.get("_dynindex_");
            return LookupListHandler.getInstance().getLookupListProvider(var0.formmodel.id, (String)var0.features.get("fid")).getSortedTableView(var5).getElemek();
         } catch (Exception var7) {
            Vector var6 = new Vector();
            var6.add("Hiba: nem sikerült a választható lista előállítása!");
            return var6;
         }
      } else {
         return new Vector(Arrays.asList((String[])((String[])var0.features.get("splist"))));
      }
   }

   public static int loadedFileCheck(File var0) {
      if (var0 == null) {
         return 1;
      } else {
         return !var0.exists() ? 2 : 0;
      }
   }

   public static boolean checkIfPathEqualsSendPath(String var0) {
      String var1 = fillPath((String)PropertyList.getInstance().get("prop.usr.krdir")) + (String)PropertyList.getInstance().get("prop.usr.ds_dest");
      return checkIf2PathsEqual(var0, var1);
   }

   public static boolean checkIf2PathsEqual(String var0, String var1) {
      var0 = beautyPath(var0);
      var1 = beautyPath(var1);
      return var0.equalsIgnoreCase(var1);
   }

   public static void emptyDir(File var0, FilenameFilter var1, Result var2) {
      if (!var0.isDirectory()) {
         if (!var0.delete() && !var0.delete()) {
            var2.errorList.add(var0);
            var2.setOk(false);
         }
      } else {
         File[] var3;
         if (var1 == null) {
            var3 = var0.listFiles();
         } else {
            var3 = var0.listFiles(var1);
         }

         for(int var4 = 0; var4 < var3.length; ++var4) {
            File var5 = var3[var4];
            if (var5.isDirectory()) {
               emptyDir(var5, var1, var2);
            }

            if (!var5.delete() && !var5.delete()) {
               var2.setOk(false);
               var2.errorList.add(var5);
            }
         }

         if (!var0.delete()) {
            var0.delete();
         }

      }
   }

   public static void saveSettings(String[] var0, String[] var1, String[] var2) {
      SettingsStore var3 = SettingsStore.getInstance();

      for(int var4 = 0; var4 < var1.length; ++var4) {
         var3.set(var0[var4], var1[var4], var2[var4]);
      }

      var3.save();
   }

   public static String[] loadSettings(String[] var0, String[] var1) {
      String[] var2 = new String[var1.length];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         try {
            var2[var3] = SettingsStore.getInstance().get(var0[var3], var1[var3]);
         } catch (Exception var5) {
            var2[var3] = null;
         }
      }

      return var2;
   }

   public static String unzipFile(String var0, String var1, Set var2, boolean var3, boolean var4) throws Exception {
      Result var5 = _unzipFile(var0, var1, var2, var3, var4, (String)null, (String)null, (BookModel)null);

      try {
         return var5.isOk() ? var5.errorList.elementAt(0).toString() : null;
      } catch (Exception var7) {
         return null;
      }
   }

   public static Result parseXCZFile(String var0, BookModel var1, String var2, String var3) throws Exception {
      Result var4 = new Result();
      Object var5 = null;
      ZipFile var6 = null;
      String var7 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.attachment") + File.separator + var2;
      String var8 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.tmp");
      String var9 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.saves") + File.separator;
      String var10 = null;

      try {
         (new File(var7)).mkdir();
      } catch (Exception var29) {
         eLog(var29, 0);
      }

      try {
         var6 = new ZipFile(var0);
         Hashtable var11 = new Hashtable();
         HashSet var12 = new HashSet();
         Vector var13 = new Vector();
         Enumeration var14 = var6.entries();

         while(true) {
            while(var14.hasMoreElements()) {
               ZipEntry var16 = (ZipEntry)var14.nextElement();
               String var17 = var16.getName();
               if (var17.indexOf("\\") <= -1 && var17.indexOf("/") <= -1) {
                  if (var17.toLowerCase().endsWith(".atc")) {
                     var10 = var8 + File.separator + var17;
                     copyInputStream(var6.getInputStream(var16), new BufferedOutputStream(new FileOutputStream(var10)), true);
                     var13.add(var10);
                  }
               } else {
                  int var18 = var17.indexOf("\\");
                  if (var18 < 0) {
                     var18 = var17.indexOf("/");
                  }

                  String var15 = var17.substring(0, var18);
                  if (checkDirName(var15, var1)) {
                     (new File(var7 + File.separator + var15)).mkdir();
                     var4.setOk(true);
                  } else {
                     var4.errorList.add("a tömörített állományban lévő " + var15 + " mappa nem megfelelő, mert nincs ilyen azonosítójú nyomtatvány");
                  }

                  if (!(new File(var7 + File.separator + var17)).isDirectory()) {
                     copyInputStream(var6.getInputStream(var16), new BufferedOutputStream(new FileOutputStream(var7 + File.separator + var17)), true);
                  }

                  File var19 = new File(beautyPath(var7 + File.separator + var17));
                  var12.add(var15);
                  var11.put(var19.getParentFile().getName() + File.separator + var19.getName(), "");
               }
            }

            try {
               PropertyList.getInstance().set("prop.usr.xcz.files", var13);
               PropertyList.getInstance().set("prop.usr.xcz.dir", var7);
               handleAVDHSignatures(var12, var11, var13, var9 + var2, var7);
               createAtcFile(var12, var11, var13, var9 + var2, var7, false);
               var13.add(var3);
               break;
            } catch (Exception var30) {
               throw new Exception("a csatolmányleíró fájl készítésekor! " + (var30.getMessage() == null ? "" : var30.getMessage()));
            }
         }
      } catch (Exception var31) {
         exception2SOut(var31);
         if (!var4.isOk()) {
            throw new Exception("Hiba a csomagolt fájl feldolgozásakor: " + var4.errorList.elementAt(0).toString());
         }

         throw new Exception("Hiba a csomagolt fájl feldolgozásakor: " + var31.getMessage());
      } finally {
         try {
            var6.close();
         } catch (Exception var28) {
            eLog(var28, 0);
         }

      }

      if (var4.isOk()) {
         var4.errorList.insertElementAt(var5, 0);
      }

      return var4;
   }

   public static Result parseXCZFile2(String var0, String var1) throws Exception {
      Result var2 = new Result();
      String var3 = null;
      ZipFile var4 = null;
      String var5 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.tmp") + File.separator + var1;
      String var6 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.tmp");
      String var7 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.saves") + File.separator;
      String var8 = null;

      try {
         (new File(var5)).mkdir();
      } catch (Exception var29) {
         eLog(var29, 0);
      }

      try {
         var4 = new ZipFile(var0);
         Hashtable var9 = new Hashtable();
         HashSet var10 = new HashSet();
         Vector var11 = new Vector();
         Enumeration var12 = var4.entries();

         while(true) {
            if (!var12.hasMoreElements()) {
               try {
                  PropertyList.getInstance().set("prop.usr.xcz.files", var11);
                  PropertyList.getInstance().set("prop.usr.xcz.dir", var5);
                  createAtcFile(var10, var9, var11, var6 + File.separator + var1, var5, true, true);
                  break;
               } catch (Exception var28) {
                  throw new Exception("Hiba a csatolmányleíró fájl készítésekor! \n" + var28.getMessage());
               }
            }

            ZipEntry var14 = (ZipEntry)var12.nextElement();
            String var15 = var14.getName();
            if (var15.indexOf("\\") <= -1 && var15.indexOf("/") <= -1) {
               if (var15.toLowerCase().endsWith(".atc") || var15.toLowerCase().endsWith(".xml") || var15.toLowerCase().endsWith(".xkr")) {
                  var8 = var6 + File.separator + var15;
                  copyInputStream(var4.getInputStream(var14), new BufferedOutputStream(new FileOutputStream(var8)), true);
                  var11.add(var8);
                  if (var15.toLowerCase().endsWith(".xml") || var15.toLowerCase().endsWith(".xkr")) {
                     var3 = var8;
                  }
               }
            } else {
               int var16 = var15.indexOf("\\");
               if (var16 < 0) {
                  var16 = var15.indexOf("/");
               }

               String var13 = var15.substring(0, var16);
               (new File(var5 + File.separator + var13)).mkdir();
               if (!(new File(var5 + File.separator + var15)).isDirectory()) {
                  copyInputStream(var4.getInputStream(var14), new BufferedOutputStream(new FileOutputStream(var5 + File.separator + var15)), true);
               }

               File var17 = new File(beautyPath(var5 + File.separator + var15));
               var10.add(var13);
               var9.put(var17.getParentFile().getName() + File.separator + var17.getName(), "");
            }
         }
      } catch (ZipException var30) {
         throw new Exception("Hiba a csomagolt fájl megnyitásakor! Lehetséges, hogy nem megfelelő a zip fájl formátuma.");
      } catch (Exception var31) {
         if (!var2.isOk()) {
            throw new Exception("Hiba a csomagolt fájl feldolgozásakor: " + var2.errorList.elementAt(0).toString());
         }

         throw new Exception("Hiba a csomagolt fájl feldolgozásakor: " + var31.getMessage());
      } finally {
         try {
            var4.close();
         } catch (Exception var27) {
            eLog(var27, 0);
         }

      }

      if (var2.isOk()) {
         if (var3 == null) {
            var2.errorList.insertElementAt("A csomagolt állomány nem tartalmaz xml vagy xkr állományt.", 0);
            var2.setOk(false);
         } else {
            var2.errorList.insertElementAt(var3, 0);
         }
      }

      return var2;
   }

   public static Result _unzipFile(String var0, String var1, Set var2, boolean var3, boolean var4, String var5, String var6, BookModel var7) throws Exception {
      Result var8 = new Result();
      String var9 = null;
      ZipFile var10 = null;
      boolean var11 = false;

      try {
         var10 = new ZipFile(var0);
         Hashtable var12 = new Hashtable();
         Hashtable var13 = new Hashtable();
         Enumeration var14 = var10.entries();
         String var15 = "#&@ToRrEs9";
         boolean var16 = false;
         if (var4 && var7 != null) {
            var8.setOk(false);
         }

         label244:
         while(true) {
            ZipEntry var17;
            label237:
            do {
               while(var14.hasMoreElements() && !var16) {
                  var17 = (ZipEntry)var14.nextElement();
                  if (var17.getName().indexOf("\\") > -1 || var17.getName().indexOf("/") > -1) {
                     var15 = (new File(var17.getName())).getParent();
                     if (var15 == null) {
                        var15 = var17.getName();
                     }

                     if (var15.endsWith(File.separator) || var15.endsWith("/")) {
                        var15 = var15.substring(0, var15.length() - 1);
                     }

                     if (var4) {
                        if (var7 != null) {
                           if (checkDirName(var15, var7)) {
                              (new File(var1 + File.separator + var15)).mkdir();
                              var8.setOk(true);
                           } else {
                              var8.errorList.add("a tömörített állományban lévő " + var15 + " mappa nem megfelelő, mert nincs ilyen azonosítójú nyomtatvány");
                           }
                        } else {
                           (new File(var1 + File.separator + var15)).mkdir();
                        }
                     }
                  }

                  if (var4 || !var17.getName().startsWith(var15 + "\\") && !var17.getName().startsWith(var15 + "/")) {
                     continue label237;
                  }
               }

               if (var5 != null) {
                  String var27 = var5 + (new File(var9)).getName();
                  var27 = var27.substring(0, var6.length() - 4) + ".atc";
               }
               break label244;
            } while(var2 != null && !var2.contains(var17.getName().substring(var17.getName().length() - 3).toLowerCase()));

            if (var7 == null || var17.getName().startsWith(var15)) {
               copyInputStream(var10.getInputStream(var17), new BufferedOutputStream(new FileOutputStream(var1 + File.separator + var17.getName())), true);
            }

            if (var17.getName().startsWith(var15)) {
               var12.put(var15, beautyPath(var1 + File.separator + var17.getName()));
               var13.put(beautyPath(var1 + File.separator + var17.getName()), "");
            }

            if (var17.getName().substring(var17.getName().length() - 3).toLowerCase().matches("x(..)")) {
               var9 = var1 + File.separator + var17.getName();
            }

            if (var3 && !var17.getName().startsWith(var15)) {
               var16 = true;
            }
         }
      } catch (Exception var25) {
         exception2SOut(var25);
         if (!var8.isOk()) {
            throw new Exception("Hiba a csomagolt fájl feldolgozásakor: " + var8.errorList.elementAt(0).toString());
         }

         throw new Exception("Hiba a csomagolt fájl feldolgozásakor: " + var25.getMessage());
      } finally {
         try {
            var10.close();
         } catch (Exception var24) {
            eLog(var24, 0);
         }

      }

      if (var8.isOk()) {
         var8.errorList.insertElementAt(var9, 0);
      }

      return var8;
   }

   private static final void copyInputStream(InputStream var0, OutputStream var1, boolean var2) throws IOException {
      byte[] var3 = new byte[1024];

      int var4;
      while((var4 = var0.read(var3)) >= 0) {
         var1.write(var3, 0, var4);
      }

      var0.close();
      if (var2) {
         var1.close();
      }

   }

   private static void createAtcFile(HashSet var0, Hashtable var1, Vector var2, String var3, String var4, boolean var5) throws Exception {
      createAtcFile(var0, var1, var2, var3, var4, var5, false);
   }

   private static void createAtcFile(HashSet var0, Hashtable var1, Vector var2, String var3, String var4, boolean var5, boolean var6) throws Exception {
      File var7 = null;
      Iterator var8 = var0.iterator();

      while(true) {
         while(var8.hasNext()) {
            Object var9 = var8.next();
            Hashtable var10 = new Hashtable();
            Result[] var11 = parseAtcFiles(var2, var1, var10, var3, (String)var9);
            if (var11 == null) {
               throw new Exception("A csomag nem tartalmaz csatolmányleíró állományt (.atc) !");
            }

            addFilesToList(var11[2].errorList);
            String var12;
            if (var6) {
               if (var11[0].errorList.size() != var11[2].errorList.size()) {
                  throw new Exception("Az XCZ állományban lévő állományok, mappák és a csatolmány leíró által hivatkozott állományok és mappák száma nem egyezik!");
               }

               if (var11[1].errorList.size() > 0) {
                  StringBuilder var33 = new StringBuilder("Az alábbi csatolmányok nem találhatók:");

                  for(int var35 = 0; var35 < var11[1].errorList.size(); ++var35) {
                     var33.append("\n").append(var11[1].errorList.elementAt(var35));
                  }

                  throw new Exception(var33.toString());
               }

               HashSet var29 = handleFileCount(var1, var9, var10, var4);
               if (var29.size() > 0) {
                  ArrayList var31 = saveBadFiles(var4, var29);
                  if (var31.size() > 0) {
                     var31.add(0, "A csomagban lévő néhány fájl nem található a csatolmány leíróban. Lehetséges, hogy külső aláíró programmal vannak aláírva. Az alábbi néven mentettük a fájlokat:");
                     PropertyList.getInstance().set("prop.usr.xcz.batchOne.3rdPartySign", var31);
                  }
               }
            } else {
               var12 = null;
               if (var11[0].errorList.size() != var11[2].errorList.size()) {
                  var12 = "Az XCZ állományban lévő állományok, mappák és a csatolmány leíró által hivatkozott állományok és mappák száma nem egyezik!";
               }

               if (var11[1].errorList.size() > 0) {
                  StringBuilder var13 = new StringBuilder("Az alábbi csatolmányok nem találhatók:");

                  for(int var14 = 0; var14 < var11[1].errorList.size(); ++var14) {
                     var13.append("\n").append(var11[1].errorList.elementAt(var14));
                  }

                  var12 = var13.toString();
               }

               HashSet var30 = handleFileCount(var1, var9, var10, var4);
               if (var30.size() > 0) {
                  ArrayList var34 = saveBadFiles(var4, var30);
                  if (var34.size() > 0) {
                     var34.add(0, "A csomagban lévő néhány fájl nem található a csatolmány leíróban. Lehetséges, hogy külső aláíró programmal vannak aláírva. Az alábbi néven mentettük a fájlokat:");
                     PropertyList.getInstance().set("prop.usr.xcz.batchOne.3rdPartySign", var34);
                  }
               }

               PropertyList.getInstance().set("prop.usr.xcz.batchOne", var12);
               if (var12 != null) {
                  PropertyList.getInstance().set("prop.usr.xcz.batchOne.files2delete", var1);
               }
            }

            var12 = "";
            if (var11[0].errorList.size() > 0) {
               var12 = (String)var11[0].errorList.elementAt(0);
            }

            if (var5) {
               if ((new File(var12)).delete()) {
                  log("A törlés sikerült " + var12);
               } else {
                  log("A törlés nem sikerült " + var12);
               }

               var7 = new File(var12);
            } else {
               var7 = new File(var3 + "_" + var9 + ".atc");
            }

            if (var10.size() > 0) {
               FileOutputStream var32 = null;

               try {
                  var32 = new FileOutputStream(var7);
                  var32.write("encoding=utf-8\n".getBytes("utf-8"));
                  Enumeration var36 = var10.keys();

                  while(var36.hasMoreElements()) {
                     Object var15 = var36.nextElement();
                     if (var15.toString().indexOf(".") > 0) {
                        String var16 = ((String[])((String[])var10.get(var15)))[1];
                        if (var16 == null) {
                           var16 = "";
                        }

                        var32.write((var4 + File.separator + var15 + ";" + var16 + ";" + ((String[])((String[])var10.get(var15)))[2] + "\n").getBytes("utf-8"));
                     }
                  }
               } catch (Exception var27) {
                  eLog(var27, 0);
               } finally {
                  try {
                     var32.close();
                  } catch (Exception var25) {
                     eLog(var25, 0);
                  }

               }
            } else {
               try {
                  var7.delete();
               } catch (Exception var26) {
                  eLog(var26, 0);
               }
            }
         }

         return;
      }
   }

   private static HashSet<String> handleFileCount(Hashtable var0, Object var1, Hashtable var2, String var3) {
      Enumeration var4 = var0.keys();
      String var5 = (String)var1;
      if (!var5.endsWith(File.separator)) {
         var5 = var5 + File.separator;
      }

      HashSet var6 = new HashSet();

      while(var4.hasMoreElements()) {
         Object var7 = var4.nextElement();
         if ((new File(var3 + File.separator + var7.toString())).isFile() && !var7.toString().toLowerCase().endsWith(".anyk.ASiC".toLowerCase()) && !var7.toString().toLowerCase().endsWith("_lenyomat.xml") && !var2.containsKey(var7)) {
            var6.add((String)var7);
         }
      }

      return var6;
   }

   private static Result[] parseAtcFiles(Vector var0, Hashtable var1, Hashtable var2, String var3, String var4) throws Exception {
      Result[] var5 = new Result[]{new Result(), new Result(), new Result()};
      boolean var6 = false;

      for(int var7 = 0; var7 < var0.size(); ++var7) {
         String var8 = (String)var0.elementAt(var7);
         if (var8.toLowerCase().endsWith(var4.toLowerCase() + ".atc")) {
            File var9 = new File(var8);
            if (var9.exists()) {
               Object var10 = AttachementTool.loadAtcFile(var9, false);
               if (var10 instanceof String) {
                  throw new Exception(var10.toString());
               }

               Vector var11 = (Vector)var10;
               if (var11.size() == 0) {
                  throw new Exception("A(z) " + var9.getName() + " fájl tartalma hibás - valószínűleg nem tartalmaz csatolmány adatot");
               }

               int var12;
               for(var12 = 0; var12 < var11.size(); ++var12) {
                  var5[2].errorList.add(var11.elementAt(var12));
               }

               var6 = true;

               for(var12 = 0; var12 < var11.size(); ++var12) {
                  String[] var13 = (String[])((String[])var11.elementAt(var12));
                  String var14 = beautyPath(var3 + File.separator + var13[0]);
                  File var15 = new File(var14);
                  var14 = var15.getParentFile().getName() + File.separator + var15.getName();
                  if (var1.containsKey(var14) && var14.indexOf(var4) > -1) {
                     var2.put(var14, var13);
                     var5[0].errorList.add(var8);
                  } else {
                     var5[1].errorList.add(var14);
                  }
               }
            }
         }
      }

      return var6 ? var5 : null;
   }

   public static int zipFile(String var0, String var1) {
      return zipFile(var0, var1, ".zip");
   }

   public static int zipFile(String var0, String var1, String var2) {
      return zipFile(new String[]{var0}, var1, var2);
   }

   public static int zipFile(String[] var0, String var1, String var2) {
      if (var2 == null) {
         var2 = ".zip";
      }

      if (!(new File(var1)).isDirectory()) {
         return -1;
      } else {
         if (!var1.endsWith("\\") && !var1.endsWith("/")) {
            var1 = var1 + File.separator;
         }

         String var3 = var1 + (new File(var0[0])).getName() + var2;
         ZipOutputStream var4 = null;

         byte var6;
         try {
            var4 = new ZipOutputStream(new FileOutputStream(var3));

            for(int var5 = 0; var5 < var0.length; ++var5) {
               var4.putNextEntry(new ZipEntry((new File(var0[var5])).getName()));
               copyInputStream(new FileInputStream(var0[var5]), var4, false);
               var4.closeEntry();
            }

            return 0;
         } catch (FileNotFoundException var18) {
            var6 = -2;
         } catch (IOException var19) {
            var6 = -3;
            return var6;
         } finally {
            try {
               var4.close();
            } catch (Exception var17) {
               eLog(var17, 0);
            }

         }

         return var6;
      }
   }

   public static int zipFileAndRename(Hashtable var0, String var1, boolean var2) {
      ZipOutputStream var3 = null;

      byte var5;
      try {
         var3 = new ZipOutputStream(new FileOutputStream(var1));
         Enumeration var4 = var0.keys();

         while(var4.hasMoreElements()) {
            Object var17 = var4.nextElement();
            String var6 = var0.get(var17).toString();
            if (var2) {
               var6 = FileNameResolver.ektelen(var6);
            }

            var3.putNextEntry(new ZipEntry(var6));
            copyInputStream(new FileInputStream(var17.toString()), var3, false);
            var3.closeEntry();
         }

         return 0;
      } catch (Exception var15) {
         ErrorList.getInstance().writeError(new Long(4001L), "xcz fájl készítés hiba!", IErrorList.LEVEL_ERROR, var15, (Object)null);
         var5 = -2;
      } finally {
         try {
            var3.close();
         } catch (Exception var14) {
            eLog(var14, 0);
         }

      }

      return var5;
   }

   public static String getTempDir() {
      Object var0 = PropertyList.getInstance().get("prop.usr.tmp");
      if (var0 != null) {
         return PropertyList.getInstance().get("prop.usr.root") + File.separator + var0.toString();
      } else {
         Map var1 = System.getenv();
         if (var1.containsKey("TEMP")) {
            return (String)var1.get("TEMP");
         } else if (var1.containsKey("TMP")) {
            return (String)var1.get("TMP");
         } else if (var1.containsKey("temp")) {
            return (String)var1.get("temp");
         } else {
            return var1.containsKey("tmp") ? (String)var1.get("tmp") : "";
         }
      }
   }

   private static boolean checkDirName(String var0, BookModel var1) {
      Vector var2 = var1.forms;
      boolean var3 = false;

      for(int var4 = 0; var4 < var2.size(); ++var4) {
         FormModel var5 = (FormModel)var2.elementAt(var4);
         if (var5.id.toLowerCase().equals(var0.toLowerCase())) {
            var3 = true;
         }
      }

      return var3;
   }

   public static void listCstFiles() {
      cstFiles = AttachementTool.mentesekPath.list(new Tools.CSTFilenameFilter());
   }

   public static void releaseCstFiles() {
      cstFiles = null;
   }

   public static void startCopy() {
      AttachementTool.fillFileList();
      atcFiles = AttachementTool.getAtcs();
   }

   public static void endCopy() {
      AttachementTool.dropFileList();
      atcFiles = null;
   }

   public static int copyAtc(Object[] var0, String var1, Vector var2) {
      HashSet var3 = new HashSet();

      try {
         File var4 = new File(var1);
         if (!var4.exists() && !var4.mkdir()) {
            return -1;
         } else {
            var1 = fillPath(var1);
            String var5 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.attachment") + File.separator;
            String var6 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.saves") + File.separator;
            Vector var7 = new Vector();

            for(int var8 = 0; var8 < var0.length; ++var8) {
               var7.clear();
               File var9;
               if (var0[var8] instanceof Object[]) {
                  var9 = (File)((Object[])((Object[])var0[var8]))[0];
               } else {
                  var9 = new File((String)var0[var8]);
               }

               if (var9.exists()) {
                  String var10 = var9.getName();
                  if (!var2.contains(var10)) {
                     int var11 = var10.toLowerCase().indexOf(".frm.enyk");
                     if (var11 > -1) {
                        var10 = var10.substring(0, var11);
                     }

                     for(int var12 = 0; var12 < atcFiles.length; ++var12) {
                        if (atcFiles[var12].indexOf(var10) == 0) {
                           String var13 = atcFiles[var12].substring(var10.length() + 1);

                           try {
                              var13 = var13.substring(0, var13.length() - ".atc".length());
                           } catch (Exception var15) {
                              continue;
                           }

                           File var14 = new File(new File(var5, var10), var13);
                           if (var14.exists()) {
                              var7.add(var6 + atcFiles[var12]);
                              copyInputStream(new FileInputStream(var6 + atcFiles[var12]), new FileOutputStream(var1 + atcFiles[var12]), true);
                              var3.add(var1 + atcFiles[var12]);
                           }
                        }
                     }

                     readAtc(var7);
                     File var17 = new File(var5 + var10);
                     if (var17.exists()) {
                        copyWholeDir(var17, new File(var1 + var10), true);
                     }

                     var3.add(var1 + var10);
                     copyInputStream(new FileInputStream(var9), new FileOutputStream(var1 + var9.getName()), true);
                  }
               }
            }

            return 0;
         }
      } catch (Exception var16) {
         deleteFiles(var3);
         ErrorList.getInstance().writeError(new Long(4001L), "Hiba a másoláskor!", var16, (Object)null);
         return -2;
      }
   }

   public static int copyDSIGFolder(SendParams var0, String var1, String var2) {
      HashSet var3 = new HashSet();
      File var4 = new File(var2 + File.separator + "_dsig");
      var4.mkdir();
      var4 = new File(var2 + File.separator + "_dsig" + File.separator + var1);
      var4.mkdir();
      File var5 = new File(var0.srcPath + var1 + ".xml");

      try {
         var4 = new File(var2 + File.separator + "_dsig" + File.separator + var1 + ".xml");
         copyInputStream(new FileInputStream(var5), new FileOutputStream(var4), true);
         var3.add(var4);
         new File(var2 + File.separator + "_dsig" + File.separator + var1 + File.separator + var1 + ".xml");
         copyWholeDir(new File(var0.srcPath + var1), new File(var2 + File.separator + "_dsig" + File.separator + var1), false);
         return 0;
      } catch (IOException var7) {
         deleteFiles(var3);
         ErrorList.getInstance().writeError(new Long(4001L), "Hiba a másoláskor!", var7, (Object)null);
         return -2;
      }
   }

   public static int _copyDSIGFolder(SendParams var0, String var1, String var2) {
      File var3 = new File(var0.srcPath + var1);
      var3.mkdir();
      File var4 = new File(var2 + File.separator + "_dsig");
      var3 = new File(var0.srcPath);

      try {
         copyWholeDir(var4, var3, false);
         return 0;
      } catch (IOException var6) {
         ErrorList.getInstance().writeError(new Long(4001L), "Hiba a másoláskor!", var6, (Object)null);
         return -2;
      }
   }

   public static int delDSIG(SendParams var0, File[] var1) {
      byte var2 = 0;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         File var4 = var1[var3];
         String var5 = var4.getName().substring(0, var4.getName().length() - ".frm.enyk".length());

         try {
            File var6 = new File(var0.srcPath + var5 + ".xml");
            var6.delete();
            var6 = new File(var0.srcPath + var5);
            deleteDir(var6);
         } catch (Exception var7) {
            var2 = -2;
         }
      }

      return var2;
   }

   public static int delXczStatus(SendParams var0, File[] var1) {
      byte var2 = 0;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         File var4 = var1[var3];
         String var5 = var4.getName().substring(0, var4.getName().length() - ".frm.enyk".length());

         try {
            File var6 = new File(var0.destPath + var5 + ".xcz_status");
            var6.delete();
            var6 = new File(var0.srcPath + var5);
            deleteDir(var6);
         } catch (Exception var7) {
            var2 = -2;
         }
      }

      return var2;
   }

   public static int renameAtc(Object[] var0, String var1, boolean var2) {
      new HashSet();

      try {
         String var4 = "";
         String var5 = "";
         if (var2) {
            var4 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.attachment") + File.separator;
         }

         if (var2) {
            var5 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.saves") + File.separator;
         }

         for(int var6 = 0; var6 < var0.length; ++var6) {
            File var7;
            if (var0[var6] instanceof Object[]) {
               var7 = (File)((Object[])((Object[])var0[var6]))[0];
            } else {
               var7 = new File((String)var0[var6]);
            }

            if (!var2) {
               var4 = var7.getParent();
               var5 = var7.getParent();
               AttachementTool.fillFileList(new File(var5));
               atcFiles = AttachementTool.getAtcs();
            }

            String var8 = var7.getName();
            int var9 = var8.toLowerCase().indexOf(".frm.enyk");
            if (var9 > -1) {
               var8 = var8.substring(0, var9);
            }

            String var10 = var1;
            var9 = var1.toLowerCase().indexOf(".frm.enyk");
            if (var9 > -1) {
               var10 = var1.substring(0, var9);
            }

            for(int var11 = 0; var11 < atcFiles.length; ++var11) {
               if (atcFiles[var11].indexOf(var8) == 0) {
                  String var12 = atcFiles[var11].substring(var8.length() + 1);
                  var12 = var12.substring(0, var12.length() - ".atc".length());
                  File var13 = new File(new File(var4, var8), var12);
                  if (var13.exists()) {
                     File var14 = new File(var5, var10 + atcFiles[var11].substring(var8.length()));
                     (new File(var5, atcFiles[var11])).renameTo(var14);
                     readAndWriteAtcFile(var14.getAbsolutePath(), var8, var10);
                  }
               }
            }

            (new File(var4, var8)).renameTo(new File(var4, var10));
         }

         return 0;
      } catch (Exception var15) {
         ErrorList.getInstance().writeError(new Long(4001L), "Hiba az átnevezéskor!", var15, (Object)null);
         return -2;
      }
   }

   public static int delAtc(Object[] var0, boolean var1) {
      byte var3;
      try {
         listCstFiles();
         String var2 = "";
         String var18 = "";
         if (var1) {
            var2 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.attachment") + File.separator;
         }

         if (var1) {
            var18 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.saves") + File.separator;
         }

         for(int var4 = 0; var4 < var0.length; ++var4) {
            File var5;
            if (var0[var4] instanceof Object[]) {
               var5 = (File)((Object[])((Object[])var0[var4]))[0];
            } else {
               var5 = new File((String)var0[var4]);
            }

            if (!var1) {
               var2 = var5.getParent();
               var18 = var5.getParent();
               AttachementTool.fillFileList(new File(var18));
               atcFiles = AttachementTool.getAtcs();
            }

            String var6 = var5.getName();
            int var7 = var6.toLowerCase().indexOf(".frm.enyk");
            if (var7 > -1) {
               var6 = var6.substring(0, var7);
            }

            int var8;
            for(var8 = 0; var8 < atcFiles.length; ++var8) {
               if (atcFiles[var8].indexOf(var6) == 0) {
                  String var9 = atcFiles[var8].substring(var6.length() + 1);

                  try {
                     var9 = var9.substring(0, var9.length() - ".atc".length());
                  } catch (Exception var15) {
                     continue;
                  }

                  File var10 = new File(new File(var2, var6), var9);
                  if (var10.exists()) {
                     (new File(var18, atcFiles[var8])).delete();
                  }
               }
            }

            for(var8 = 0; var8 < cstFiles.length; ++var8) {
               if (cstFiles[var8].indexOf(var6) == 0) {
                  (new File(var18, cstFiles[var8])).delete();
               }
            }

            File var19 = new File(var2, var6);
            if (var19.exists()) {
               deleteDir(var19);
            }
         }

         return 0;
      } catch (Exception var16) {
         ErrorList.getInstance().writeError(new Long(4001L), "Hiba a törléskor!", var16, (Object)null);
         var3 = -2;
      } finally {
         releaseCstFiles();
      }

      return var3;
   }

   private static void readAtc(Vector var0) throws Exception {
      attachments.clear();

      for(int var1 = 0; var1 < var0.size(); ++var1) {
         Object var2 = AttachementTool.loadAtcFile(new File((String)var0.elementAt(var1)), false);
         if (var2 instanceof Vector) {
            for(int var3 = 0; var3 < ((Vector)var2).size(); ++var3) {
               String var4 = ((String[])((String[])((Vector)var2).elementAt(var3)))[0];
               attachments.add(var4);
            }
         }
      }

   }

   private static void copyWholeDir(File var0, File var1, boolean var2) throws IOException {
      if (var0.isDirectory()) {
         if (!var1.exists()) {
            var1.mkdir();
         }

         String[] var3 = var0.list();
         String[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            File var8 = new File(var0, var7);
            File var9 = new File(var1, var7);
            copyWholeDir(var8, var9, var2);
         }
      } else {
         if (var2 && !attachments.contains(var0.getAbsolutePath()) && !attachments.contains(var0.getAbsolutePath().substring(0, var0.getAbsolutePath().length() - ".anyk.ASiC".length()))) {
            return;
         }

         FileInputStream var10 = new FileInputStream(var0);
         FileOutputStream var11 = new FileOutputStream(var1);
         copyInputStream(var10, var11, true);
      }

   }

   public static void deleteFiles(HashSet var0) {
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         Object var2 = var1.next();
         File var3;
         if (var2 instanceof String) {
            var3 = new File((String)var2);
         } else {
            var3 = (File)var2;
         }

         if (var3.isFile()) {
            var3.delete();
         } else {
            deleteDir(var3);
         }
      }

   }

   public static void deleteDir(File var0) {
      if (var0.isDirectory()) {
         String[] var1 = var0.list();
         String[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            File var6 = new File(var0, var5);
            deleteDir(var6);
         }

         var0.delete();
      } else {
         var0.delete();
      }

   }

   public static int archiveAtc(String var0, String var1, String var2) {
      if (atcFiles.length == 0) {
         return 0;
      } else {
         HashSet var3 = new HashSet();
         HashSet var4 = new HashSet();

         try {
            File var5 = new File(var1);
            if (!var5.exists() && !var5.mkdir()) {
               return -1;
            } else {
               var1 = fillPath(var1);
               String var6 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.attachment") + File.separator;
               String var7 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.saves") + File.separator;
               Vector var8 = new Vector();
               File var9 = new File(var0);
               String var10 = var9.getName();
               int var11 = var10.toLowerCase().indexOf(".frm.enyk");
               if (var11 > -1) {
                  var10 = var10.substring(0, var11);
               }

               var11 = var2.toLowerCase().indexOf(".frm.enyk");
               if (var11 > -1) {
                  var2 = var2.substring(0, var11);
               }

               String var12 = var2.substring(var10.length());

               for(int var13 = 0; var13 < atcFiles.length; ++var13) {
                  if (atcFiles[var13].indexOf(var10) > -1) {
                     String var14 = getModAtcFilename(atcFiles[var13], var12, true);
                     if (!var14.toLowerCase().endsWith(".atc")) {
                        var14 = var14 + ".atc";
                     }

                     var8.add(var7 + atcFiles[var13]);
                     copyInputStream(new FileInputStream(var7 + atcFiles[var13]), new FileOutputStream(var1 + var14), true);
                     var4.add(var1 + var14);
                     var3.add(var7 + atcFiles[var13]);
                  }
               }

               readAtc(var8);
               File var16 = new File(var6 + var10);
               if (var16.exists()) {
                  copyWholeDir(var16, new File(var1 + var10 + var12), true);
               }

               var4.add(var1 + var10 + var12);
               var3.add(var6 + var10);
               deleteFiles(var3);
               return 0;
            }
         } catch (Exception var15) {
            deleteFiles(var4);
            ErrorList.getInstance().writeError(new Long(4001L), "Hiba a másoláskor!", var15, (Object)null);
            return -2;
         }
      }
   }

   public static void archiveDSIG(SendParams var0, String var1, String var2, String var3) {
      HashSet var4 = new HashSet();
      HashSet var5 = new HashSet();
      File var6 = new File(var1);
      String var7 = var6.getName().substring(0, var6.getName().length() - ".frm.enyk".length());
      var6 = new File(var3);
      String var8 = var6.getName().substring(0, var6.getName().length() - ".frm.enyk".length());
      var6 = new File(var2 + File.separator + var8 + File.separator + "_dsig");
      var6.mkdir();

      try {
         copyInputStream(new FileInputStream(var0.srcPath + var7 + ".xml"), new FileOutputStream(var6.getAbsolutePath() + File.separator + var7 + ".xml"), true);
         var4.add(var0.srcPath + var7 + ".xml");
         var5.add(var6.getAbsolutePath() + File.separator + var7 + ".xml");
         copyWholeDir(new File(var0.srcPath + var7), new File(var2 + File.separator + var8 + File.separator + "_dsig" + File.separator + var7), false);
         var4.add(var0.srcPath + var7);
         var5.add(var2 + File.separator + var8 + File.separator + "_dsig" + File.separator + var7);
         deleteFiles(var4);
      } catch (Exception var12) {
         try {
            deleteFiles(var5);
         } catch (Exception var11) {
         }
      }

   }

   private static String getModAtcFilename(String var0, String var1, boolean var2) {
      if (var1.equals("")) {
         return var0;
      } else {
         return var2 ? var0.substring(0, var0.length() - 4) + var1 : var0.substring(0, var0.indexOf(var1));
      }
   }

   public static int resetAtc(String var0, String var1, boolean var2, String var3) {
      HashSet var4 = new HashSet();
      HashSet var5 = new HashSet();
      if (var3.toLowerCase().indexOf(".frm.enyk") > -1) {
         var3 = var3.substring(0, var3.toLowerCase().indexOf(".frm.enyk"));
      }

      try {
         String var6 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.attachment") + File.separator;
         String var7 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.saves") + File.separator;
         int var8 = var0.toLowerCase().indexOf(".frm.enyk");
         if (var8 > -1) {
            var0 = var0.substring(0, var8);
         }

         File var9 = new File(var6 + getModAtcFilename(var0, var3, false));
         if (!var9.exists() && !var9.mkdir()) {
            return -1;
         } else {
            String[] var10 = (new File(var1)).list(new Tools.ATCFilenameFilter());
            Vector var11 = new Vector();

            for(int var12 = 0; var12 < var10.length; ++var12) {
               if (var10[var12].indexOf(getModAtcFilename(var0, var3, false)) > -1) {
                  var11.add(var7 + var10[var12]);
                  String var13 = getModAtcFilename(var10[var12], var3, false);
                  if (!var13.toLowerCase().endsWith(".atc")) {
                     var13 = var13 + ".atc";
                  }

                  copyInputStream(new FileInputStream(var1 + var10[var12]), new FileOutputStream(var7 + var13), true);
                  var5.add(var7 + var13);
                  var4.add(var1 + var10[var12]);
               }
            }

            File var15 = new File(var1 + var0);
            if (var15.exists()) {
               copyWholeDir(var15, new File(var6 + getModAtcFilename(var0, var3, false)), false);
            }

            var5.add(var6 + var0);
            var4.add(var15);
            if (var2) {
               deleteFiles(var4);
            }

            return 0;
         }
      } catch (Exception var14) {
         deleteFiles(var5);
         ErrorList.getInstance().writeError(new Long(4001L), "Hiba a másoláskor!", var14, (Object)null);
         return -2;
      }
   }

   public static int resetDSIG(SendParams var0, String var1, String var2, boolean var3, String var4) {
      var1 = var1.substring(0, var1.length() - ".frm.enyk".length());
      File var5 = new File(var2 + var1 + File.separator + "_dsig");
      File var6 = new File(var0.srcPath);

      try {
         copyWholeDir(var5, var6, false);
         deleteDir(var5);
         return 0;
      } catch (Exception var8) {
         return -2;
      }
   }

   public static int copyFile4Masolatkeszites(File var0, File var1) {
      FileInputStream var2 = null;
      FileOutputStream var3 = null;
      BufferedReader var4 = null;
      BufferedWriter var5 = null;

      try {
         var2 = new FileInputStream(var0);
         var3 = new FileOutputStream(var1);
         var4 = new BufferedReader(new InputStreamReader(var2, "UTF-8"));
         var5 = new BufferedWriter(new OutputStreamWriter(var3, "UTF-8"));
         String var6 = null;

         while((var6 = var4.readLine()) != null) {
            var5.write(parseLine(var6) + "\r\n");
         }

         byte var7 = 1;
         return var7;
      } catch (Exception var29) {
         log("" + var29);
      } finally {
         if (var5 != null) {
            try {
               var5.flush();
               var5.close();
               var5 = null;
            } catch (IOException var28) {
               eLog(var28, 0);
            }
         }

         if (var4 != null) {
            try {
               var4.close();
               var4 = null;
            } catch (IOException var27) {
               eLog(var27, 0);
            }
         }

         if (var3 != null) {
            try {
               var3.close();
               var3 = null;
            } catch (IOException var26) {
               eLog(var26, 0);
            }
         }

         if (var2 != null) {
            try {
               var2.close();
               var2 = null;
            } catch (IOException var25) {
               eLog(var25, 0);
            }
         }

      }

      return 0;
   }

   public static String parseLine(String var0) {
      try {
         if (var0.indexOf("krfilename=") < 0 && var0.indexOf("attachment_count=") < 0) {
            return var0;
         } else {
            StringBuffer var1 = stringRemover(new StringBuffer(var0), "krfilename=\"(.*)kr\" ");
            var1 = stringRemover(var1, "attachment_count=\"(.*)\" ");
            return var1.toString();
         }
      } catch (Exception var2) {
         return var0;
      }
   }

   public static StringBuffer stringRemover(StringBuffer var0, String var1) {
      Pattern var2 = Pattern.compile(var1);
      Matcher var3 = var2.matcher(var0);
      StringBuffer var4 = new StringBuffer();

      while(var3.find()) {
         var3.appendReplacement(var4, "");
      }

      var3.appendTail(var4);
      return var4;
   }

   public static int readAndWriteAtcFile(String var0, String var1, String var2) throws Exception {
      Object var3 = AttachementTool.loadAtcFile(new File(var0), false);
      if (var3 instanceof Vector) {
         Vector var4 = (Vector)var3;

         for(int var5 = 0; var5 < ((Vector)var3).size(); ++var5) {
            String[] var6 = (String[])((String[])var4.elementAt(var5));
            String var7 = (new File(var6[0])).getAbsolutePath();
            var7 = var7.replaceFirst(var1, var2);
            var6[0] = var7;
         }

         FileOutputStream var10 = null;

         try {
            var10 = new FileOutputStream(var0);
            var10.write("encoding=utf-8\n".getBytes("utf-8"));

            for(int var11 = 0; var11 < ((Vector)var3).size(); ++var11) {
               Object[] var12 = (Object[])((Object[])var4.elementAt(var11));
               var10.write((var12[0] + ";" + var12[1] + ";" + var12[2] + "\n").getBytes("utf-8"));
            }

            var10.close();
         } catch (Exception var9) {
            try {
               var10.close();
            } catch (Exception var8) {
               eLog(var9, 0);
            }

            return -1;
         }
      }

      return 0;
   }

   private static void addFilesToList(Vector var0) {
      Vector var1;
      if (PropertyList.getInstance().get("prop.usr.xcz.batchOne.files2show") != null) {
         var1 = (Vector)PropertyList.getInstance().get("prop.usr.xcz.batchOne.files2show");
      } else {
         var1 = new Vector();
      }

      for(int var2 = 0; var2 < var0.size(); ++var2) {
         var1.add(var0.elementAt(var2));
      }

      PropertyList.getInstance().set("prop.usr.xcz.batchOne.files2show", var1);
   }

   public static int createErrorLogFromVector(String var0, Vector var1) {
      int var2 = -1;

      try {
         if (var1 == null) {
            ErrorList.getInstance().writeError(new Long(4001L), var0, new Exception("Nem részletezhető hiba"), (Object)null);
         }

         String var3 = "";

         for(int var4 = 0; var4 < var1.size(); ++var4) {
            try {
               Object var5 = var1.elementAt(var4);
               if (var5 instanceof String) {
                  var3 = var3 + var5 + "\n";
                  var2 = var4;
               }
            } catch (Exception var6) {
            }
         }

         ErrorList.getInstance().writeError(new Long(4001L), var0, new Exception(var3), (Object)null);
         return var2;
      } catch (Exception var7) {
         return -1;
      }
   }

   private static void handleAVDHSignatures(HashSet var0, Hashtable var1, Vector var2, String var3, String var4) {
      Enumeration var5 = var1.keys();
      String var6 = null;
      String var7 = null;

      while(var5.hasMoreElements()) {
         String var8 = var4 + File.separator + var5.nextElement();
         if (var8.toLowerCase().endsWith(".urlap.anyk.ASiC".toLowerCase())) {
            var6 = var8;
         }

         if (var8.toLowerCase().indexOf("_lenyomat") > -1) {
            var7 = var8;
         }
      }

      if (var6 != null || var7 != null) {
         SendParams var12 = new SendParams(PropertyList.getInstance());
         File var9;
         String var10;
         String var11;
         if (var6 != null) {
            var9 = new File(var6);
            var10 = var9.getName().substring(0, var9.getName().length() - ".urlap.anyk.ASiC".length());
            (new File(var12.srcPath + var10)).mkdir();
            (new File(var12.srcPath + var10 + File.separator + "alairt")).mkdir();
            (new File(var6)).renameTo(new File(var12.srcPath + var10 + File.separator + "alairt" + File.separator + var9.getName()));
            var11 = var12.root + "";
            if (!var11.endsWith(File.separator)) {
               var11 = var11 + File.separator;
            }

            var11 = var11 + PropertyList.getInstance().get("prop.usr.tmp");
            if (!var11.endsWith(File.separator)) {
               var11 = var11 + File.separator;
            }

            (new File(var11 + var10 + ".xml")).renameTo(new File(var12.srcPath + var10 + ".xml"));
         } else {
            var9 = new File(var7);
            var10 = var9.getName().substring(0, var9.getName().indexOf("_lenyomat"));
            (new File(var12.srcPath + var10)).mkdir();
            if (var7.toLowerCase().endsWith(".xml")) {
               (new File(var12.srcPath + var10 + File.separator + "alairando")).mkdir();
               (new File(var7)).renameTo(new File(var12.srcPath + var10 + File.separator + "alairando" + File.separator + var9.getName()));
            } else {
               (new File(var12.srcPath + var10 + File.separator + "alairando")).mkdir();
               (new File(var7)).renameTo(new File(var12.srcPath + var10 + File.separator + "alairt" + File.separator + var9.getName()));
            }

            var11 = var12.root + "";
            if (!var11.endsWith(File.separator)) {
               var11 = var11 + File.separator;
            }

            var11 = var11 + PropertyList.getInstance().get("prop.usr.tmp");
            if (!var11.endsWith(File.separator)) {
               var11 = var11 + File.separator;
            }

            (new File(var11 + var10 + ".xml")).renameTo(new File(var12.srcPath + var10 + ".xml"));
         }

      }
   }

   public static int handleTemplateCheckerResult(BookModel var0) {
      String var1 = var0.loadedfile.getName();
      Object var2 = PropertyList.getInstance().get("prop.dynamic.TemplateChecker.result");
      if (var2 == null) {
         return 2;
      } else if (!(var2 instanceof String[])) {
         return 2;
      } else {
         String[] var3 = (String[])((String[])var2);
         if (var3.length > 1) {
            String var6 = var3[0].substring(var3[0].lastIndexOf(";") + 1);
            if (!var6.equals(var0.getTemplateId())) {
               PropertyList.getInstance().set("prop.dynamic.TemplateChecker.result", (Object)null);
               return 2;
            } else {
               var6 = var3[0].substring(0, var3[0].lastIndexOf(";"));
               return !var1.toLowerCase().equalsIgnoreCase((new File(var6)).getName().toLowerCase()) ? 4 : 2;
            }
         } else {
            try {
               VersionData var4 = UpgradeManager.getUpdateableVersionData(var0);
               if (var4 != null) {
                  System.out.println("Letoltheto legfrissebb verzio : " + var4.getVersion().toString());
                  return 5;
               } else {
                  return 2;
               }
            } catch (Exception var5) {
               return 2;
            }
         }
      }
   }

   public static int handleTemplateCheckerResultNew(BookModel var0) {
      try {
         VersionData var1 = UpgradeManager.getUpdateableVersionData(var0);
         return var1 != null ? 5 : 2;
      } catch (Exception var2) {
         return 2;
      }
   }

   public static void exception2SOut(Exception var0) {
      log("------- Exception:");
      log(var0.getMessage());

      try {
         StackTraceElement[] var1 = var0.getStackTrace();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            StackTraceElement var3 = var1[var2];
            log(" >  " + var3);
         }
      } catch (Exception var4) {
         log("Az exception nem írható ki (ex)!");
      } catch (Error var5) {
         log("Az exception nem írható ki (err)!");
      }

      log("-------");
   }

   public static void log(String var0) {
      System.out.println(var0);
   }

   public static HashMap<String, String[]> getWP(BookModel var0, Hashtable<String, String> var1) {
      HashMap var2 = new HashMap();
      int var3 = var0.cc.size();
      Adonemek var4 = Adonemek.getInstance();

      for(int var5 = 0; var5 < var3; ++var5) {
         Enumeration var6 = var1.keys();

         while(var6.hasMoreElements()) {
            String var7 = (String)var6.nextElement();
            String var8 = var4.getAdonemMegnevezes((String)var1.get(var7));
            var2.put(var7, new String[]{(String)var1.get(var7), var8});
         }
      }

      return var2;
   }

   public static int handleTervezetFieldFid(String var0, BookModel var1) {
      if (isInGeneratorMod(var1)) {
         return 0;
      } else if (!isTervezetFieldFid(var0)) {
         return 0;
      } else {
         return "0".equals(MainFrame.role) && "0".equals(MainFrame.opmode) ? 1 : 0;
      }
   }

   private static boolean isInGeneratorMod(BookModel var0) {
      return "10".equals(var0.getHasznalatiMod());
   }

   public static boolean isTervezetFieldFid(String var0) {
      return PropertyList.getInstance().get("prop.dynamic.tervezetFieldFid") == null ? false : var0.equals(PropertyList.getInstance().get("prop.dynamic.tervezetFieldFid"));
   }

   public static void removeACertainVidFromMainDocument(BookModel var0, String var1) {
      try {
         FormModel var2 = var0.get(((Elem)var0.cc.get(0)).getType());
         Enumeration var3 = var2.fids.keys();
         boolean var4 = false;
         String var5 = "";

         while(var3.hasMoreElements() && !var4) {
            var5 = (String)var3.nextElement();
            DataFieldModel var6 = (DataFieldModel)var2.fids.get(var5);
            if (var1.equals(var6.features.get("vid"))) {
               var4 = true;
            }
         }

         if (!var4) {
            return;
         }

         StoreItem var9 = new StoreItem(var5, 0, "");
         IDataStore var7 = (IDataStore)((Elem)var0.cc.get(0)).getRef();
         var7.remove(var9);
      } catch (Exception var8) {
         eLog(var8, 0);
      }

   }

   private static ArrayList<String> saveBadFiles(String var0, HashSet<String> var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         var2.add(var0 + File.separator + (String)var3.next());
      }

      return var2;
   }

   static class CSTFilenameFilter implements FilenameFilter {
      public boolean accept(File var1, String var2) {
         return this.isAtcFilename(var2);
      }

      private boolean isAtcFilename(String var1) {
         return var1.endsWith(".cst");
      }
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
