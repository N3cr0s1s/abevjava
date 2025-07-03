package hu.piller.enykp.alogic.orghandler;

import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeLogger;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.reader.URLOrg;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Version;
import me.necrocore.abevjava.NecroFile;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.regex.PatternSyntaxException;

public class OrgInfo {
   private static final int ORG_ID = 0;
   private static final int ORG_SHORTNAME = 1;
   private static final int ORG_FULLNAME = 2;
   private static final String ORG_INFO_FILE_PATH;
   private static final String ORG_INFO_FILE_MASK_POSRFIX = "Resources.*\\.jar";
   private static final String ORG_INFO_FILE_MASK = ".*Resources.*\\.jar";
   private static final String ORG_FORM_TYPE_POSTFIX = "Resources";
   private Object checked_org_catalog;
   private Object org_names;
   private static OrgInfo instance;
   private Hashtable orgList = new Hashtable();
   private Vector typeList = new Vector();
   private String typedFileMask;

   public static OrgInfo getInstance() {
      if (instance == null) {
         instance = new OrgInfo();
      }

      return instance;
   }

   private OrgInfo() {
      this.mountDir(this.getResourcePath());
   }

   public String getResourcePath() {
      return PropertyList.getInstance().get("prop.sys.root") + ORG_INFO_FILE_PATH;
   }

   public void mountDir(String var1) {
      this.org_names = null;
      this.orgList = new Hashtable();
      File var2 = new NecroFile(var1);
      if (var2.exists()) {
         File[] var3 = var2.listFiles(new FileFilter() {
            public boolean accept(File var1) throws PatternSyntaxException {
               return var1.getName().matches(".*Resources.*\\.jar");
            }
         });

         for(int var4 = 0; var4 < var3.length; ++var4) {
            File var5 = var3[var4];

            try {
               this.mountFile(var5);
            } catch (Exception var7) {
               ErrorList.getInstance().writeError(1, var3[var4] + " erőforrás fájl csatolási hiba", var7, (Object)null);
            }
         }
      }

      this.getActVersions();
   }

   public void mountType(String var1) {
      this.orgList.remove(var1);
      File var2 = new NecroFile(this.getResourcePath());
      if (var2.exists()) {
         this.typedFileMask = var1 + "Resources.*\\.jar";
         File[] var3 = var2.listFiles(new FileFilter() {
            public boolean accept(File var1) throws PatternSyntaxException {
               return var1.getName().matches(OrgInfo.this.typedFileMask);
            }
         });

         for(int var4 = 0; var4 < var3.length; ++var4) {
            File var5 = var3[var4];
            this.mountFile(var5);
         }
      }

      this.getActVersions();
   }

   private void mountFile(File var1) {
      OrgResource var2 = new OrgResource(var1);
      String var3 = var2.getType();
      String var4 = var2.getVersion();
      Hashtable var5 = (Hashtable)this.orgList.get(var3);
      if (var5 == null) {
         var5 = new Hashtable();
         this.orgList.put(var3, var5);
      }

      var5.put(var4, var2);
   }

   private void getActVersions() {
      this.typeList = new Vector(this.orgList.size());
      Enumeration var1 = this.orgList.elements();

      while(var1.hasMoreElements()) {
         Hashtable var2 = (Hashtable)var1.nextElement();
         Enumeration var3 = var2.elements();
         Version var4 = new Version("v0.0.0.-1");
         OrgResource var5 = null;

         while(var3.hasMoreElements()) {
            OrgResource var6 = (OrgResource)var3.nextElement();
            Version var7 = new Version(var6.getVersion());
            if (var7.compareTo(var4) > 0) {
               var4 = var7;
               var5 = var6;
            }
         }

         if (var5 != null) {
            this.typeList.add(var5);
         }
      }

      this.checked_org_catalog = null;
      this.getOrgList();
   }

   public IPropertyList getResource(String var1, String var2) {
      String var3 = var1.substring(0, var1.lastIndexOf("Resources"));
      return (IPropertyList)((Hashtable)this.getOrgList()).get(var3);
   }

   private Object getOrgCatalog() {
      return this.checked_org_catalog;
   }

   public Set<OrgResource> readAllResourceFiles() {
      TreeSet var1 = new TreeSet();
      File var2 = new NecroFile(this.getResourcePath());
      if (var2.exists()) {
         File[] var3 = var2.listFiles(new FileFilter() {
            public boolean accept(File var1) throws PatternSyntaxException {
               return var1.getName().matches(".*Resources.*\\.jar");
            }
         });

         for(int var4 = 0; var4 < var3.length; ++var4) {
            File var5 = var3[var4];
            OrgResource var6 = new OrgResource(var5);
            var1.add(var6);
         }
      }

      return var1;
   }

   public Object getOrgList() {
      Object var1;
      if (this.getOrgCatalog() == null) {
         try {
            Hashtable var4 = new Hashtable(16);
            Object[] var6 = this.typeList.toArray();
            if (var6 != null) {
               int var7 = 0;

               for(int var8 = var6.length; var7 < var8; ++var7) {
                  IPropertyList var2 = (IPropertyList)var6[var7];
                  Hashtable var3 = (Hashtable)var2.get("getorginfo");
                  if (var3 != null) {
                     var3 = (Hashtable)var3.get("attributes");
                     if (var3 != null) {
                        String var5 = (String)var3.get("id");
                        var4.put(var5, var6[var7]);
                     }
                  }
               }

               this.checked_org_catalog = var4;
               var1 = var4;
            } else {
               var1 = this.checked_org_catalog;
            }
         } catch (Exception var9) {
            var1 = null;
            var9.printStackTrace();
         }
      } else {
         var1 = this.checked_org_catalog;
      }

      return var1;
   }

   public Object getOrgInfo(Object var1) {
      Object var2;
      if (this.getOrgList() != null) {
         IPropertyList var3 = (IPropertyList)((Hashtable)this.checked_org_catalog).get(var1);
         if (var3 == null) {
            return null;
         }

         var2 = var3.get("getorginfo");
      } else {
         var2 = null;
      }

      return var2;
   }

   public Object getOrgAddress(Object var1) {
      Object var2;
      if (this.getOrgList() != null) {
         IPropertyList var3 = (IPropertyList)((Hashtable)this.checked_org_catalog).get(var1);
         var2 = var3.get("getaddresses");
      } else {
         var2 = null;
      }

      return var2;
   }

   public Object getCertName(Object var1) {
      Object var2;
      if (this.getOrgList() != null) {
         IPropertyList var3 = (IPropertyList)((Hashtable)this.checked_org_catalog).get(var1);
         var2 = var3.get("getcertname");
      } else {
         var2 = null;
      }

      return var2;
   }

   public String getOrgShortnameByOrgID(String var1) {
      return this.mapProperty(var1, 0, 1);
   }

   public String getOrgFullnameByOrgID(String var1) {
      return this.mapProperty(var1, 0, 2);
   }

   public String getOrgIDByOrgShortname(String var1) {
      return this.mapProperty(var1, 1, 0);
   }

   private String mapProperty(String var1, int var2, int var3) {
      Object[] var4 = (Object[])((Object[])this.getOrgNames());
      Vector var5 = (Vector)var4[var2];
      int var6 = 0;

      boolean var7;
      for(var7 = false; var6 < var5.size(); ++var6) {
         if (var5.get(var6).equals(var1)) {
            var7 = true;
            break;
         }
      }

      return var7 ? (String)((Vector)var4[var3]).get(var6) : var1;
   }

   public Object getOrgNames() {
      if (this.org_names == null && this.getOrgList() != null) {
         Hashtable var1 = (Hashtable)this.checked_org_catalog;
         Iterator var3 = var1.entrySet().iterator();
         Vector var8 = new Vector(16);
         Vector var9 = new Vector(16);
         Vector var10 = new Vector(16);
         Vector var11 = new Vector(16);

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            IPropertyList var5 = (IPropertyList)var4.getValue();
            Hashtable var2 = (Hashtable)var5.get("getorginfo");
            var1 = (Hashtable)var2.get("attributes");
            String var6;
            String var7;
            if (var1 != null) {
               var7 = (String)var1.get("orgshortname");
               var6 = (String)var1.get("orgname");
            } else {
               var7 = null;
               var6 = null;
            }

            var8.add(var4.getKey());
            var9.add(var7);
            var10.add(var6);
            var11.add(var5);
         }

         this.org_names = new Object[]{var8, var9, var10, var11};
      }

      return this.org_names;
   }

   private boolean checkOrg(String var1, String[] var2) {
      if (var2 == null) {
         return true;
      } else {
         String[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (var1.equals(var6)) {
               return true;
            }
         }

         return false;
      }
   }

   public Collection<String> getOrgIdsFromResources() {
      TreeSet var1 = new TreeSet();
      Iterator var2 = ((Hashtable)this.getOrgList()).values().iterator();

      while(var2.hasNext()) {
         OrgResource var3 = (OrgResource)var2.next();
         var1.add(var3.getOrgId());
      }

      return var1;
   }

   public String lookupOrigIdByPrefix(String var1) {
      HashMap var2 = new HashMap();
      Iterator var3 = ((Hashtable)this.getOrgList()).values().iterator();

      while(var3.hasNext()) {
         OrgResource var4 = (OrgResource)var3.next();
         String var5 = var4.getOrgId();
         String var6 = var4.getPrefix();
         var2.put(var6, var5);
      }

      return (String)var2.get(var1);
   }

   public Collection<String> getOrgIds() {
      TreeSet var1 = new TreeSet();
      Iterator var2 = ((Hashtable)this.getOrgList()).values().iterator();

      while(var2.hasNext()) {
         String var3 = ((OrgResource)var2.next()).getOrgId();

         try {
            if (this.hasSuccessor(var3)) {
               try {
                  var1.add(this.getSuccessorOrgId(var3));
               } catch (SuccessorException var9) {
                  ErrorList.getInstance().writeError(1000, var9.getMessage(), var9, (Object)null);
               }
            } else {
               var1.add(var3);
            }
         } catch (IllegalArgumentException var10) {
            ErrorList.getInstance().writeError(1000, var10.getMessage(), var10, (Object)null);
         }
      }

      Hashtable var11 = TemplateChecker.getInstance().listTemplateUpgradeUrl();
      Iterator var4 = var11.keySet().iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();

         try {
            if (this.hasSuccessor(var5)) {
               try {
                  var1.add(this.getSuccessorOrgId(var5));
               } catch (SuccessorException var7) {
                  ErrorList.getInstance().writeError(1000, var7.getMessage(), var7, (Object)null);
               }
            } else {
               var1.add(var5);
            }
         } catch (IllegalArgumentException var8) {
            ErrorList.getInstance().writeError(1000, var8.getMessage(), var8, (Object)null);
         }
      }

      return var1;
   }

   public String getKRCimzett(String var1) {
      String var2 = null;
      Iterator var3 = ((Hashtable)this.getOrgList()).values().iterator();

      while(var3.hasNext()) {
         OrgResource var4 = (OrgResource)var3.next();
         if (var4.getOrgId().equals(var1)) {
            var2 = var4.getKRCimzett();
            break;
         }
      }

      return var2;
   }

   public String[] getOrgIdsOfOrgsWithOnlineUpdate() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = ((Hashtable)this.getOrgList()).values().iterator();

      while(var2.hasNext()) {
         OrgResource var3 = (OrgResource)var2.next();
         String var4 = var3.getOnlineupdate();
         if (Boolean.valueOf(var4)) {
            var1.add(var3.getOrgId());
         }
      }

      return (String[])var1.toArray(new String[var1.size()]);
   }

   public boolean hasSuccessor(String var1) throws IllegalArgumentException {
      boolean var2 = false;
      OrgResource var3 = this.getOrgResourceForOrgId(var1);
      if (!"".equals(var3.getUtodszervezet())) {
         var2 = true;
      }

      return var2;
   }

   public String getSuccessorOrgId(String var1) throws IllegalArgumentException, SuccessorException {
      if (!this.hasSuccessor(var1)) {
         throw new SuccessorException("Nincs utódszervezet kapcsolat definiálva a szervezethez: " + var1);
      } else {
         ArrayList var3 = new ArrayList();
         String var2 = this.traceSuccessorChain(var1, var3);
         var3.clear();
         return var2;
      }
   }

   private String traceSuccessorChain(String var1, Collection<String> var2) throws IllegalArgumentException, SuccessorException {
      String var3 = var1;
      var2.add(var1);
      String var4 = this.getOrgResourceForOrgId(var1).getUtodszervezet();
      if (!"".equals(var4)) {
         if (var2.contains(var4)) {
            throw new SuccessorException("Szervezet nem lehet önmaga utódszervezete az utódlási sorban: " + var4);
         }

         try {
            var3 = this.traceSuccessorChain(var4, var2);
         } catch (IllegalArgumentException var6) {
            throw new SuccessorException("Az utódszervezet erőforrásfájlának rendelkezésre kell állnia: " + var4);
         }
      }

      return var3;
   }

   private OrgResource getOrgResourceForOrgId(String var1) {
      Hashtable var2 = (Hashtable)this.getOrgList();
      if (!var2.containsKey(var1)) {
         throw new IllegalArgumentException("Ismeretlen szervezet azonosító: " + var1);
      } else {
         OrgResource var3 = (OrgResource)var2.get(var1);
         return var3;
      }
   }

   public Collection<URLOrg> getUpgradeURLAllOrganizations(String[] var1) {
      TreeSet var2 = new TreeSet();
      Iterator var3 = ((Hashtable)this.getOrgList()).values().iterator();

      while(var3.hasNext()) {
         OrgResource var4 = (OrgResource)var3.next();
         if (this.checkOrg(var4.getOrgId(), var1)) {
            try {
               String var5 = var4.getOrgUpgradeURL();
               URLOrg var6 = new URLOrg();
               var6.url = new URL(var5);
               var6.org = var4.getOrgId();
               var2.add(var6);
            } catch (MalformedURLException var12) {
               UpgradeLogger.getInstance().log(" ### UpgradeManager : érvénytelen frissítési URL a(z) " + var4.getOrgId() + " szervezet erőforrás állományában");
               ErrorList.getInstance().writeError(10001, "FIGYELEM: érvénytelen frissítési url '" + var4.getOrgUpgradeURL() + "' a(z) " + var4.getOrgname() + " erőforrás állományában!", var12, (Object)null);
            }
         }
      }

      Hashtable var13 = TemplateChecker.getInstance().listTemplateUpgradeUrl();
      Iterator var14 = var13.keySet().iterator();

      while(var14.hasNext()) {
         String var16 = (String)var14.next();
         Iterator var7 = ((Hashtable)var13.get(var16)).keySet().iterator();

         while(var7.hasNext()) {
            String var8 = (String)var7.next();
            if (var8 != null && !"".equals(var8.trim()) && this.checkOrg((String)((Hashtable)((Hashtable)var13.get(var16)).get(var8)).get("org"), var1)) {
               try {
                  URLOrg var9 = new URLOrg();
                  var9.url = new URL(var8);
                  var9.org = (String)((Hashtable)((Hashtable)var13.get(var16)).get(var8)).get("org");
                  var2.add(var9);
               } catch (MalformedURLException var11) {
                  UpgradeLogger.getInstance().log(" ### UpgradeManager : rossz upgrade url " + var8 + " az " + var16 + " szervezet " + ((Hashtable)var13.get(var16)).get(var8) + " sablonjában");
                  ErrorList.getInstance().writeError(10001, "FIGYELEM: rossz upgrade url " + var8 + " (" + var16 + " szervezet, " + ((Hashtable)var13.get(var16)).get(var8) + " sablon)", var11, (Object)null);
               }
            }
         }
      }

      TreeSet var15 = new TreeSet();
      Iterator var17 = var2.iterator();

      while(var17.hasNext()) {
         URLOrg var18 = (URLOrg)var17.next();

         try {
            if (!this.hasSuccessor(var18.org)) {
               var15.add(var18);
            }
         } catch (Exception var10) {
            var15.add(var18);
            UpgradeLogger.getInstance().log(" ### UpgradeManager : erőforrás állomány nélküli szervezet " + var18.org + " (nyomtatványban hívatkozva)");
            ErrorList.getInstance().writeError("ORGINFO", var10.getMessage(), 10001, var10, (Object)null);
         }
      }

      return var15;
   }

   public Vector<String> getXMLNS(String var1, boolean var2) {
      Hashtable var4;
      try {
         var4 = (Hashtable)this.getOrgList();
      } catch (Exception var11) {
         return null;
      }

      if (var4.size() == 0) {
         return null;
      } else {
         Vector var5 = new Vector();
         Hashtable var3;
         if (var1 != null) {
            try {
               Hashtable var12 = (Hashtable)this.getOrgInfo(var1);
               var3 = (Hashtable)var12.get("attributes");
               if (var3.containsKey("nsurl")) {
                  var5.add((String)var3.get("nsurl"));
                  return var5;
               } else {
                  return null;
               }
            } catch (Exception var9) {
               return null;
            }
         } else {
            try {
               if (!var2) {
                  return null;
               } else {
                  Enumeration var6 = var4.keys();

                  while(var6.hasMoreElements()) {
                     Object var7 = var6.nextElement();
                     Hashtable var8 = (Hashtable)this.getOrgInfo(var7);
                     var3 = (Hashtable)var8.get("attributes");
                     if (var3.containsKey("nsurl")) {
                        var5.add((String)var3.get("nsurl"));
                     }
                  }

                  return var5;
               }
            } catch (Exception var10) {
               return null;
            }
         }
      }
   }

   static {
      ORG_INFO_FILE_PATH = File.separator + "eroforrasok";
   }
}
