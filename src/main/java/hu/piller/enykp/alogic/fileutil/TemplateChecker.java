package hu.piller.enykp.alogic.fileutil;

import hu.piller.enykp.alogic.orghandler.OrgHandler;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.orghandler.OrgResource;
import hu.piller.enykp.alogic.settingspanel.upgrade.UpgradeFunction;
import hu.piller.enykp.alogic.templateutils.blacklist.BlacklistStore;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeBusinessException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeManager;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeTechnicalException;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.EventLog;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.Version;
import hu.piller.enykp.util.filelist.EnykFileList;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TemplateChecker {
   private static final String DEFAULT_VER = "000";
   public static final String FUNC_GET_TEMPLATE_FILENAME = "get_templatefilename";
   public static final String FUNC_FILTER_FILES = "get_filtered_files";
   private static final String VER_FULL_EXPAND = "000000000000";
   private static final String NSEPARATOR = "\\.";
   private static final String RELEASESEPARATOR = "-";
   private static final String RELEASEMAXVALUE = "XXXXXXXXX";
   private static final int VER_LENGTH = "000".length();
   private static final int VER_FULL_LENGTH = "000000000000".length();
   private IPropertyList iplMaster;
   private EnykFileList efl = EnykFileList.getInstance();
   private String templatePath;
   private Hashtable vers = new Hashtable();
   private Hashtable multiVers = new Hashtable();
   private Hashtable orgs = new Hashtable();
   private Hashtable templatename4Filename = new Hashtable();
   private static int selectedIndex;
   public static final int YES_PLEASE = 0;
   public static final int NO_THANKS = 1;
   private int getTemplateIfHavent = 1;
   private boolean needDialog4Download = true;
   private static TemplateChecker ourInstance = new TemplateChecker();
   String[] valasz = new String[]{"Kötegelt", "Önálló"};

   public void setNeedDialog4Download(boolean var1) {
      this.needDialog4Download = var1;
   }

   public void setGetTemplateIfHavent(int var1) {
      this.getTemplateIfHavent = var1;
   }

   public static TemplateChecker getInstance() {
      return ourInstance;
   }

   public static TemplateChecker getInstance(String var0) {
      return ourInstance;
   }

   private TemplateChecker() {
      try {
         this.init();
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
      }

   }

   public ExtendedTemplateData getTemplateFileNames(String var1, String var2, String var3, UpgradeFunction var4) throws Exception {
      return this.getTemplateFileNames(var1, var2, var3, false, var4);
   }

   public ExtendedTemplateData getTemplateFileNames(String var1, String var2, String var3) throws Exception {
      return this.getTemplateFileNames(var1, var2, var3, false, (UpgradeFunction)null);
   }

   public ExtendedTemplateData getTemplateFileNames(String var1, String var2, String var3, boolean var4, UpgradeFunction var5) throws Exception {
      PropertyList.getInstance().set("prop.dynamic.TemplateChecker.result", (Object)null);
      if (var1 == null) {
         throw new Exception("Hiányzó paraméter");
      } else {
         if (var2 == null) {
            var2 = "000";
         }

         if (var2.equals("")) {
            var2 = "000";
         }

         var3 = OrgHandler.getInstance().getCheckedOrgId(var3);
         this.vers.clear();
         this.multiVers.clear();
         this.templatename4Filename.clear();
         Object[] var6 = this.efl.list(this.templatePath, new Object[]{"template_loader_v1"});
         this.getTemplateNamesFromObjects(var6);

         try {
            String[] var7 = this.parseTemps(var1, var2, var3, var6, var4, false);

            try {
               EventLog.getInstance().writeLog("templateId: " + var1 + ", ver: " + var2 + ", org: " + var3 + ", RESULT LENGTH: " + var7.length);
            } catch (Exception var17) {
               var17.printStackTrace();
            }

            String var8;
            try {
               if (var5 != null && !var7[0].equals("")) {
                  var8 = (new File(var7[0])).getPath();
                  var7[0] = UpgradeManager.checkUpgrade(var8, var5, var3);
               }
            } catch (Exception var16) {
               Tools.eLog(var16, 0);
            }

            if (var7[0].equals("") && this.needDialog4Download && "0".equals(MainFrame.opmode) && MainFrame.thisinstance.isVisible() && GuiUtil.showOptionDialog(MainFrame.thisinstance, "A kívánt nyomtatványhoz nem találtunk megfelelő sablont. Megpróbáljuk letölteni az internetről?", "Letöltés", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0) {
               var8 = MainFrame.thisinstance.getGlassText();
               MainFrame.thisinstance.setGlassLabel("Sablon letöltése");
               TemplateNameResolver.getInstance().createTeminfoList();
               Properties var9 = TemplateNameResolver.getInstance().getAllName();
               String var10 = var1;

               try {
                  if (var9.containsKey(var1)) {
                     System.out.println("TEMPLATE ID RESOLVED");
                     var10 = (String)((Vector)var9.get(var1)).get(0);
                  }
               } catch (Exception var15) {
                  var10 = var1;
               }

               try {
                  if ("Nem definiált".equals(var3) && var10.indexOf("_") != -1) {
                     String var11 = var10.substring(0, var10.indexOf("_"));
                     var3 = OrgInfo.getInstance().lookupOrigIdByPrefix(var11);
                  }

                  UpgradeManager.downloadTemplate("Nem definiált".equals(var3) ? OrgHandler.getInstance().getDefaultOrgId() : var3, var10, var2);
               } catch (UpgradeBusinessException var12) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Letöltés hiba: " + var12.getMessage(), "Hiba a sablon letöltésekor", 0);
               } catch (UpgradeTechnicalException var13) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Letöltés hiba: " + var13.getMessage(), "Hiba a sablon letöltésekor", 0);
               } catch (Exception var14) {
                  var14.printStackTrace();
               }

               var6 = this.efl.list(this.templatePath, new Object[]{"template_loader_v1"});
               this.getTemplateNamesFromObjects(var6);
               var7 = this.parseTemps(var1, var2, var3, var6, var4, false);
               MainFrame.thisinstance.setGlassLabel(var8);
            }

            this.handleCheckResultProperty(var7, var1);
            var8 = this.getTemplateId4ETD(var1);
            String var19 = this.getOrgId4ETD(var6, var7[0]);
            return new ExtendedTemplateData(var8, var19, var7, BlacklistStore.getInstance().isBiztipDisabled(var8, var19));
         } catch (Exception var18) {
            return null;
         }
      }
   }

   public String[] getTemplateFileNames4CheckUpdate(String var1, String var2, String var3) throws Exception {
      PropertyList.getInstance().set("prop.dynamic.TemplateChecker.result", (Object)null);
      if (var2 == null) {
         var2 = "000";
      }

      if (var2.equals("")) {
         var2 = "000";
      }

      var3 = OrgHandler.getInstance().getCheckedOrgId(var3);
      if (var1 == null) {
         throw new Exception("Hiányzó paraméter");
      } else {
         this.vers.clear();
         this.multiVers.clear();
         this.templatename4Filename.clear();
         Object[] var4 = this.efl.list(this.templatePath, new Object[]{"template_loader_v1"});
         this.getTemplateNamesFromObjects(var4);

         try {
            String[] var5 = this.parseTemps(var1, var2, var3, var4, false, true);
            this.handleCheckResultProperty(var5, var1);
            return var5;
         } catch (Exception var6) {
            return null;
         }
      }
   }

   public ExtendedTemplateData getTemplateFileNames(String var1) throws Exception {
      return this.getTemplateFileNames(var1, "000", OrgHandler.getInstance().getDefaultOrgId());
   }

   public Object getFilteredFiles(Object[] var1) {
      Hashtable var2 = new Hashtable(var1.length);
      Hashtable var3 = new Hashtable(var1.length);

      String var7;
      for(int var4 = 0; var4 < var1.length; ++var4) {
         Object[] var5 = (Object[])((Object[])var1[var4]);

         try {
            Hashtable var6 = (Hashtable)((Hashtable)var5[1]).get("docinfo");
            var7 = (String)var6.get("ver");
            String var8 = (String)var6.get("id");
            String var9 = OrgHandler.getInstance().getReDirectedOrgId((String)var6.get("org"));
            var6.put("org", var9);
            if (var8 != null && var7 != null) {
               String var10 = var9.toUpperCase() + var8.toUpperCase();
               String var11 = var10 + this.getVersion(var7);
               var3.put(var11, var5);
               if (var2.containsKey(var10)) {
                  if (((String)var2.get(var10)).compareTo(var11) < 0) {
                     var2.put(var10, var11);
                  }
               } else {
                  var2.put(var10, var11);
               }
            }
         } catch (Exception var12) {
            Tools.eLog(var12, 0);
         }
      }

      Object[] var13 = new Object[var2.size()];
      Enumeration var14 = var2.keys();

      for(int var15 = 0; var14.hasMoreElements(); var13[var15++] = var3.get(var2.get(var7))) {
         var7 = (String)var14.nextElement();
      }

      return var13;
   }

   public Hashtable<String, Hashtable<String, Hashtable>> listTemplateUpgradeUrl() {
      Hashtable var1 = new Hashtable();
      Hashtable var2 = new Hashtable();
      Object[] var3 = this.efl.list(this.templatePath, new Object[]{"template_loader_v1"});
      if (var3 != null && var3.length > 0) {
         Object[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Object var7 = var4[var6];
            Object[] var8 = (Object[])((Object[])var7);
            Hashtable var9 = (Hashtable)((Hashtable)var8[1]).get("docinfo");
            String var10 = (String)var9.get("URL");
            if (var10 != null && (var10 == null || !var10.trim().equals(""))) {
               String var11 = OrgHandler.getInstance().getReDirectedOrgId((String)var9.get("org"));
               var9.put("org", var11);
               String var12 = (String)var9.get("id");
               if (var2.get(var10) == null) {
                  var2.put(var10, this.validateURL(var10));
               }

               if (!(Boolean)var2.get(var10)) {
                  System.err.println("FIGYELEM: rossz upgrade url:" + var10 + " Szervezet:" + var11 + " Paraméterek:" + var9.toString());
               } else {
                  Hashtable var13 = (Hashtable)var1.get(var11);
                  if (var13 == null) {
                     var13 = new Hashtable();
                     var1.put(var11, var13);
                  }

                  var13.put(var10, var9);
               }
            }
         }
      }

      return var1;
   }

   private Boolean validateURL(String var1) {
      try {
         new URL(var1);
      } catch (MalformedURLException var3) {
         return false;
      }

      return true;
   }

   public Set<String> getTemplatePublishers() {
      TreeSet var1 = new TreeSet();
      Object[] var2 = this.efl.list(this.templatePath, new Object[]{"template_loader_v1"});
      if (var2 != null && var2.length > 0) {
         Object[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Object var6 = var3[var5];
            Object[] var7 = (Object[])((Object[])var6);
            Hashtable var8 = (Hashtable)((Hashtable)var7[1]).get("docinfo");
            String var9 = OrgHandler.getInstance().getReDirectedOrgId((String)var8.get("org"));
            var8.put("org", var9);
            if (var9 != null) {
               var1.add(var9);
            }
         }
      }

      return var1;
   }

   public Object[] listLastTemplateVersions() {
      Object[] var1 = this.efl.list(this.templatePath, new Object[]{"template_loader_v1"});
      return var1 != null && var1.length > 0 ? (Object[])((Object[])this.getFilteredFiles(var1)) : null;
   }

   private String[] parseTemps(String var1, String var2, String var3, Object[] var4, boolean var5, boolean var6) throws Exception {
      boolean var7 = true;

      try {
         if (MainFrame.opmode.equals("1")) {
            var7 = false;
         }
      } catch (Exception var15) {
         var7 = false;
      }

      if (PropertyList.getInstance().get("prop.dynamic.ilyenkor") != null) {
         var7 = false;
      }

      TemplateNameResolver.getInstance().createTeminfoList();
      Properties var8 = TemplateNameResolver.getInstance().getAllName();
      Vector var9 = new Vector();
      Vector var10 = null;
      byte var11 = 0;
      String[] var12 = this.doParse(var1, var2, var4, var5);
      this.vers.clear();
      this.multiVers.clear();
      if (var8.containsKey(var1)) {
         var10 = (Vector)var8.get(var1);
      }

      if (var10 != null) {
         for(int var13 = 0; var13 < var10.size(); ++var13) {
            String[] var14 = this.doParse((String)var10.elementAt(var13), var2, var4, var5);
            this.vers.clear();
            this.multiVers.clear();
            if (!var14[0].equals("")) {
               var9.add(var14);
            }
         }
      }

      if (var9.size() > 0) {
         if (!var12[0].equals("")) {
            var9.insertElementAt(var12, 0);
         }
      } else {
         var9.add(var12);
      }

      if (var7 && var9.size() > 1) {
         if (!var8.containsKey("mainid") && !var8.containsKey("subid")) {
            if (!var6) {
               selectedIndex = this.createTemplateChooser(var9);
            }

            if (selectedIndex == -1) {
               return new String[]{"", "", "Nem választott nyomtatványt"};
            }

            return (String[])((String[])var9.elementAt(selectedIndex));
         }

         Vector var16;
         if (var8.containsKey("mainid")) {
            var16 = (Vector)var8.get("mainid");
            if (var16.contains(var1)) {
               var11 = 1;
            }
         }

         if (var8.containsKey("subid")) {
            var16 = (Vector)var8.get("subid");
            if (var16.contains(var1)) {
               var11 = 2;
            }
         }

         if (var11 < 2) {
            if (!var6) {
               selectedIndex = this.createTemplateChooser(var9);
            }

            if (selectedIndex == -1) {
               return new String[]{"", "", "Nem választott nyomtatványt"};
            }

            return (String[])((String[])var9.elementAt(selectedIndex));
         }
      }

      selectedIndex = 0;
      return (String[])((String[])var9.elementAt(0));
   }

   private String fillVer(String var1) {
      String var2 = "000" + var1;
      return var2.substring(var2.length() - VER_LENGTH);
   }

   public String getVersion(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var2 = var1.split("-");
         String[] var3 = var2[0].split("\\.");
         StringBuffer var4 = new StringBuffer();

         for(int var5 = 0; var5 < var3.length; ++var5) {
            var4.append(this.fillVer(var3[var5]));
         }

         String var6 = var4.toString() + "000000000000";
         return var6.substring(0, VER_FULL_LENGTH) + "-" + (var2.length > 1 ? var2[1].toUpperCase() : "XXXXXXXXX");
      } else {
         return "000000000000";
      }
   }

   private void init() {
      try {
         this.iplMaster = PropertyList.getInstance();
         this.templatePath = (String)this.iplMaster.get("prop.dynamic.templates.absolutepath");
         this.templatePath = Tools.fillPath(this.templatePath);
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
      }

   }

   public ExtendedTemplateData getTemplateFilenameWithDialog(String var1, String var2, String var3) {
      return this.getTemplateFilenameWithDialog(var1, var2, var3, (UpgradeFunction)null);
   }

   public ExtendedTemplateData getTemplateFilenameWithDialog(String var1, String var2, String var3, UpgradeFunction var4) {
      PropertyList.getInstance().set("prop.dynamic.TemplateChecker.result", (Object)null);
      String var5 = "A megnyitni kívánt nyomtatványhoz nem található megfelelő sablon!";

      try {
         this.iplMaster.set("prop.dynamic.hasNewTemplate", Boolean.FALSE);
         ExtendedTemplateData var6 = this.getTemplateFileNames(var1, var2, var3, true, var4);
         String[] var7 = var6.getTemplateFileNames();
         if ("Nem definiált".equals(var3) && !var6.getOrgId().isEmpty()) {
            var3 = var6.getOrgId();
         }

         try {
            var5 = var7[2];
         } catch (Exception var12) {
            var5 = "A megnyitni kívánt nyomtatványhoz nem található megfelelő sablon!";
         }

         File var8;
         String var9;
         if (MainFrame.opmode.equals("1")) {
            var8 = new File(var7[0]);
            if (var8.exists()) {
               var9 = this.getTemplateId4ETD(var1);
               return new ExtendedTemplateData(var9, var3, var8, BlacklistStore.getInstance().isBiztipDisabled(var9, var3));
            } else {
               throw new Exception();
            }
         } else if (!var7[1].equals(var7[0])) {
            this.handleCheckResultProperty(var7, var1);
            var9 = "A megnyitni kívánt nyomtatványból újabb verzió is létezik. Kívánja az újjal megnyitni az állományt?";
            if (var7[1].equals("")) {
               var9 = "A megnyitni kívánt nyomtatványhoz nincs meg az eredeti verziójú sablon. Kívánja a legújabb sablonnal megnyitni az állományt?";
            }

            boolean var10 = this.needOriginalTemplateVersion(var7[0], var7[1]);
            String var11;
            if (this.getTemplateIfHavent == 0) {
               if (GuiUtil.showOptionDialog((Component)null, var9, "Nyomtatványverzió választás", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0) {
                  var8 = new File(var7[0]);
                  if (var8.exists()) {
                     this.iplMaster.set("prop.dynamic.hasNewTemplate", Boolean.TRUE);
                     var11 = this.getTemplateId4ETD(var1);
                     return new ExtendedTemplateData(var11, var3, var8, BlacklistStore.getInstance().isBiztipDisabled(var11, var3));
                  } else {
                     throw new Exception();
                  }
               } else if (var7[1].equals("")) {
                  PropertyList.getInstance().set("prop.dynamic.templateLoad.userTerminated", true);
                  return null;
               } else {
                  var11 = this.getTemplateId4ETD(var1);
                  return new ExtendedTemplateData(var11, var3, new File(var7[1]), BlacklistStore.getInstance().isBiztipDisabled(var11, var3));
               }
            } else {
               if (var10) {
                  var8 = new File(var7[1]);
               } else {
                  var8 = new File(var7[0]);
               }

               if (var8.exists()) {
                  var11 = this.getTemplateId4ETD(var1);
                  return new ExtendedTemplateData(var11, var3, var8, BlacklistStore.getInstance().isBiztipDisabled(var11, var3));
               } else {
                  throw new Exception();
               }
            }
         } else {
            var8 = new File(var7[0]);
            if (var8.exists()) {
               var9 = this.getTemplateId4ETD(var1);
               return new ExtendedTemplateData(var9, var3, var8, BlacklistStore.getInstance().isBiztipDisabled(var9, var3));
            } else {
               throw new Exception();
            }
         }
      } catch (Exception var13) {
         GuiUtil.showMessageDialog((Component)null, var5, "Nyomtatványverzió választás", 0);
         return null;
      }
   }

   private String[] doParse(String var1, String var2, Object[] var3, boolean var4) {
      OrgInfo var5 = OrgInfo.getInstance();
      boolean var6 = false;
      String var7 = "Nincs megfelelő szervezet paraméter állomány";
      String var8 = "Nincs megfelelő szervezet paraméter állomány";

      String var11;
      for(int var9 = 0; var9 < var3.length; ++var9) {
         Object[] var10 = (Object[])((Object[])var3[var9]);

         try {
            var11 = (String)var10[0];
            Hashtable var12 = (Hashtable)((Hashtable)var10[1]).get("docinfo");
            String var13 = (String)var12.get("ver");
            String var14 = (String)var12.get("id");
            String var15 = (String)var12.get("org");
            if (var14 != null) {
               if (var15 == null) {
                  var15 = "";
               }

               if (var13 != null && (var14.toLowerCase().equals(var1.toLowerCase()) || var14.toLowerCase().equals(var1.toLowerCase()))) {
                  try {
                     Hashtable var16 = (Hashtable)var5.getOrgInfo(OrgHandler.getInstance().getReDirectedOrgId((String)var12.get("org")));
                     if (var16 == null) {
                        throw new Exception();
                     }
                  } catch (Exception var19) {
                     try {
                        var8 = "Nincs megfelelő szervezet paraméter állomány (" + OrgHandler.getInstance().getReDirectedOrgId((String)var12.get("org")) + ")";
                        var7 = "Nincs " + OrgHandler.getInstance().getReDirectedOrgId((String)var12.get("org")) + " előtagú (nevű) erőforrásfájl (sablon: " + var10[0] + ")";
                     } catch (Exception var18) {
                        var7 = "Hiányzó erőforrásfájl";
                     }

                     var6 = true;
                     ErrorList.getInstance().writeError(new Long(4001L), var7, (Exception)null, (Object)null);
                     continue;
                  }

                  if (!var14.toLowerCase().equals(var1.toLowerCase())) {
                     this.multiVers.put(this.getVersion(var13), var11);
                  } else {
                     this.vers.put(this.getVersion(var13), var11);
                  }

                  this.orgs.put(var11, var15);
               }
            }
         } catch (Exception var20) {
            Tools.eLog(var20, 0);
            ErrorList.getInstance().writeError(new Integer(4001), "Sablon hiba", var20, (Object)null);
         }
      }

      if (this.vers.size() == 0) {
         this.vers = this.multiVers;
      }

      if (this.vers.size() == 0) {
         return new String[]{"", "", var6 ? var8 : "Nincs megfelelő nyomtatvány sablon"};
      } else {
         Enumeration var21 = this.vers.keys();
         Vector var22 = new Vector(this.vers.size());

         while(var21.hasMoreElements()) {
            var22.add(var21.nextElement());
         }

         Collections.sort(var22);
         var11 = (String)this.vers.get(var22.get(var22.size() - 1));
         String var23 = (String)this.vers.get(this.getVersion(var2));
         if (var23 != null) {
            return new String[]{var11, var23, ""};
         } else if (var4) {
            return new String[]{var11, "", ""};
         } else {
            return new String[]{var11};
         }
      }
   }

   private String getRelation(String var1, String var2) {
      try {
         String var3 = var2.substring(0, 3);

         String var4;
         for(var4 = var2.substring(3, 6); var3.length() > 0 && var3.charAt(0) == '0'; var3 = var3.substring(1, var3.length())) {
         }

         while(var4.length() > 0 && var4.charAt(0) == '0') {
            var4 = var4.substring(1, var4.length());
         }

         if (var3.length() == 0) {
            var3 = "0";
         }

         if (var4.length() == 0) {
            var4 = "0";
         }

         Version var5 = new Version("1.0");
         Version var6 = new Version(var3 + "." + var4);
         return var6.compareTo(var5) + "";
      } catch (Exception var7) {
         return "X";
      }
   }

   private int createTemplateChooser(Vector var1) {
      final JDialog var2 = new JDialog(MainFrame.thisinstance, "Nyomtatványválasztás", true);
      var2.setSize(500, 250);
      var2.setResizable(true);
      var2.setLocationRelativeTo(MainFrame.thisinstance);
      var2.setDefaultCloseOperation(0);
      var2.setLayout(new BorderLayout(10, 10));
      JLabel var3 = new JLabel("<html><br>&nbsp;&nbsp;A megnyitni kívánt állomány adatai az alábbi nyomtatványokba tölthetők be.<br>&nbsp;&nbsp;Válassza ki a megfelelő nyomtatványt:<br>&nbsp;</html>");
      Vector var4 = new Vector();

      for(int var5 = 0; var5 < var1.size(); ++var5) {
         String[] var6 = (String[])((String[])var1.elementAt(var5));
         if (var6[0].equals("")) {
            var4.add("?????");
         } else {
            var4.add(this.templatename4Filename.get(var6[0]));
         }
      }

      JList var11 = new JList(var4);
      var11.setBorder(BorderFactory.createEtchedBorder(1));
      var11.setSelectedIndex(0);
      var11.addMouseListener(new MouseListener() {
         public void mouseClicked(MouseEvent var1) {
            if (var1.getClickCount() == 2) {
               var2.setVisible(false);
               var2.dispose();
            }

         }

         public void mousePressed(MouseEvent var1) {
         }

         public void mouseReleased(MouseEvent var1) {
         }

         public void mouseEntered(MouseEvent var1) {
         }

         public void mouseExited(MouseEvent var1) {
         }
      });
      JButton var12 = new JButton("Betölt");
      var12.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            var2.setVisible(false);
            var2.dispose();
         }
      });
      JScrollPane var7 = new JScrollPane(var11, 20, 30);
      JPanel var8 = new JPanel();
      JPanel var9 = new JPanel();
      JPanel var10 = new JPanel();
      var9.setSize(20, 20);
      var10.setSize(20, 20);
      var8.add(var12);
      var2.add(var9, "East");
      var2.add(var10, "West");
      var2.add(var3, "North");
      var2.add(var7, "Center");
      var2.add(var8, "South");
      if (MainFrame.thisinstance.isVisible()) {
         var2.setVisible(true);
         return var11.getSelectedIndex();
      } else {
         return 0;
      }
   }

   private void getTemplateNamesFromObjects(Object[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         Hashtable var3 = (Hashtable)((Hashtable)((Object[])((Object[])var1[var2]))[1]).get("docinfo");
         this.templatename4Filename.put(((Object[])((Object[])var1[var2]))[0], var3.get("name"));
      }

   }

   private boolean needOriginalTemplateVersion(String var1, String var2) {
      if ("".equals(var2)) {
         return false;
      } else {
         String var3 = "";

         try {
            if (!"".equals(var1)) {
               var3 = (String)this.orgs.get(var1);
            } else {
               var3 = (String)this.orgs.get(var2);
            }
         } catch (Exception var5) {
            var3 = "";
         }

         if ("".equals(var3)) {
            return false;
         } else {
            OrgResource var4 = (OrgResource)((Hashtable)OrgInfo.getInstance().getOrgList()).get(var3);
            return "true".equals(var4.getNeedOriginalTemplateVersion());
         }
      }
   }

   private void handleCheckResultProperty(String[] var1, String var2) {
      String[] var3 = new String[var1.length];

      for(int var4 = 0; var4 < var1.length; ++var4) {
         if (!"".equals(var1[var4])) {
            var3[var4] = var1[var4] + ";" + var2;
         }
      }

      PropertyList.getInstance().set("prop.dynamic.TemplateChecker.result", var3);
   }

   private String getTemplateId4ETD(String var1) {
      Vector var2 = TemplateNameResolver.getInstance().getName(var1);
      if (var2 == null) {
         return var1;
      } else {
         try {
            return (String)var2.get(0);
         } catch (Exception var4) {
            return var1;
         }
      }
   }

   private String getOrgId4ETD(Object[] var1, String var2) {
      Object[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object var6 = var3[var5];

         try {
            String var7 = (String)((Object[])((Object[])var6))[0];
            if (var2.equals(var7)) {
               Hashtable var8 = (Hashtable)((Hashtable)((Object[])((Object[])var6))[1]).get("docinfo");
               String var9 = (String)var8.get("org");
               return OrgHandler.getInstance().getReDirectedOrgId(var9);
            }
         } catch (Exception var10) {
            var10.printStackTrace();
         }
      }

      return "";
   }
}
