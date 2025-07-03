package hu.piller.enykp.alogic.orghandler;

import hu.piller.enykp.alogic.orghandler.xml.OrgInfoParser;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.JarUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;
import org.xml.sax.SAXException;

public class OrgResource implements IPropertyList, Comparable {
   public static final boolean debugOn = true;
   private static String resource_name = "OrgResource";
   private static OrgResource this_;
   private Hashtable org_info_xml;
   private Object addresses;
   public static final String FILE_SEPARATOR = "/";
   public static final String FUNCTION_GETIMAGE = "getimage";
   public static final String FUNCTION_GETORGINFO = "getorginfo";
   public static final String FUNCTION_GETADDRESSES = "getaddresses";
   public static final String FUNCTION_GETCERTNAME = "getcertname";
   public static final String FUNCTION_GETMASTERDATA = "getmasterdata";
   public static final String IMAGE_TYPE_PICTURE = "kép";
   public static final String[] EXT_PICTURE_ARR = new String[]{"jpg", "jpeg", "gif"};
   public static final String PATH_PICTURE = "pictures";
   public static final String ORG_INFO = "orginfo.xml";
   public static final String ADDRESSES = "addresses.xml";
   public static final String MASTERDATA = "masterdata.xml";
   public static final String CONTAINER_INFO_FILE = "resourcecontainer.xml";
   private JarUtil ju;
   private File path;
   private Hashtable orgcontainer;
   private String type;
   private String version;

   public OrgResource(File var1) {
      this.path = var1;
      this.ju = new JarUtil("file:" + var1.getAbsolutePath());
      this.mainParse();
      Vector var2 = (Vector)this.orgcontainer.get("Resources");
      Hashtable var3 = (Hashtable)var2.get(0);
      this.type = (String)var3.get("ResourceType");
      this.version = (String)var3.get("Version");
   }

   private void mainParse() {
      try {
         byte[] var1 = this.ju.getImageResource("resourcecontainer.xml");
         OrgParser var2 = new OrgParser();
         this.orgcontainer = (Hashtable)var2.parse(var1);
      } catch (Exception var3) {
         this.writeError(new Integer(17100), "A szervezet erőforrás leírót nem sikerült betölteni", var3, "resourcecontainer.xml");
      }

   }

   public String getType() {
      return this.type;
   }

   public String getVersion() {
      return this.version;
   }

   public String getPath() {
      return this.path.getPath();
   }

   public boolean set(Object var1, Object var2) {
      return false;
   }

   public Object get(Object var1) {
      if (var1 != null) {
         if (var1 instanceof String) {
            String var2 = (String)var1;
            if ("getorginfo".equalsIgnoreCase(var2)) {
               return this.getOrgInfo();
            }

            if ("getaddresses".equalsIgnoreCase(var2)) {
               return this.getAddresses();
            }

            if ("getcertname".equalsIgnoreCase(var2)) {
               return this.getCertName();
            }

            if ("getmasterdata".equalsIgnoreCase(var2)) {
               return this.getMasterDataDefinitions();
            }
         } else if (var1 instanceof Object[]) {
            Object[] var12 = (Object[])((Object[])var1);
            if (var12.length > 0 && var12[0] instanceof String) {
               String var3 = (String)var12[0];
               if (var3.equalsIgnoreCase("getimage") && var12.length == 3 && var12[1] instanceof String) {
                  String var4 = (String)var12[1];
                  if (var12[2] instanceof String) {
                     String var5 = (String)var12[2];
                     if (var4.equals("kép")) {
                        String var7 = "pictures/" + var5 + ".";
                        String[] var8 = EXT_PICTURE_ARR;
                        int var9 = var8.length;

                        for(int var10 = 0; var10 < var9; ++var10) {
                           String var11 = var8[var10];
                           byte[] var6 = this.ju.getImageResource(var7 + var11);
                           if (var6 != null) {
                              return new Object[]{var6};
                           }

                           var6 = this.ju.getImageResource(var7 + var11.toUpperCase());
                           if (var6 != null) {
                              return new Object[]{var6};
                           }
                        }

                        return null;
                     }
                  }
               }
            }
         }
      }

      return null;
   }

   private Object getMasterDataDefinitions() {
      ByteArrayInputStream var1 = null;
      byte[] var2 = this.ju.getEntry("masterdata.xml");
      if (var2 != null) {
         var1 = new ByteArrayInputStream(var2);
      }

      return var1;
   }

   private Object getOrgInfo() {
      Hashtable var1;
      if (this.org_info_xml == null) {
         try {
            var1 = (new OrgInfoParser()).parse(this.ju.getImageResource("orginfo.xml"));
            this.org_info_xml = var1;
         } catch (SAXException var3) {
            var1 = null;
            var3.printStackTrace();
         }
      } else {
         var1 = this.org_info_xml;
      }

      return var1;
   }

   private Object getAddresses() {
      Object var1;
      if (this.addresses == null) {
         var1 = this.ju.getImageResource("addresses.xml");
         this.addresses = var1;
      } else {
         var1 = this.addresses;
      }

      return var1;
   }

   private Object getCertName() {
      Object var2 = this.getOrgInfo();
      byte[] var1;
      if (var2 instanceof Hashtable) {
         Hashtable var3 = (Hashtable)var2;
         String var4;
         if ("szervezet".equalsIgnoreCase((String)var3.get("name"))) {
            var3 = (Hashtable)var3.get("attributes");
            if (var3 != null) {
               var4 = (String)var3.get("certfilename");
            } else {
               var4 = null;
            }
         } else {
            var4 = null;
         }

         if (var4 != null) {
            var1 = this.ju.getImageResource(var4);
         } else {
            var1 = null;
         }
      } else {
         var1 = null;
      }

      return var1;
   }

   public void writeError(Object var1, String var2, Exception var3, Object var4) {
      ErrorList.getInstance().writeError(var1, var2, var3, var4);
      System.out.println(var1 + "," + var2 + "," + var3.getMessage() + "," + var4.toString());
   }

   public String getOrgUpgradeURL() {
      return this.getSzervezetAttribute("updateurl");
   }

   public String getOrgname() {
      return this.getSzervezetAttribute("orgname");
   }

   public String getPrefix() {
      return this.getSzervezetAttribute("prefix");
   }

   public String getOrgId() {
      return this.getSzervezetAttribute("id");
   }

   public String getKRCimzett() {
      return this.getSzervezetAttribute("krcimzett").replaceAll(" ", "");
   }

   public String getUtodszervezet() {
      return this.getSzervezetAttribute("successorid");
   }

   public String getOnlineupdate() {
      return this.getSzervezetAttribute("onlineupdate");
   }

   public String getNeedOriginalTemplateVersion() {
      return this.getSzervezetAttribute("originalversion");
   }

   public String getEperGateOption() {
      return this.getSzervezetAttribute("epergate");
   }

   public String getPreferredUplodaMaxSize() {
      return this.getSzervezetAttribute("maxsize");
   }

   public String getOrgCheckValid() {
      return this.getSzervezetAttribute("org_check_valid");
   }

   private String getSzervezetAttribute(String var1) {
      Hashtable var2 = (Hashtable)this.get("getorginfo");
      var2 = (Hashtable)var2.get("attributes");
      String var3 = (String)var2.get(var1);
      return var3 == null ? "" : var3;
   }

   public int compareTo(Object var1) {
      OrgResource var2 = (OrgResource)var1;
      int var3 = this.getOrgId().compareTo(var2.getOrgId());
      if (var3 == 0) {
         var3 = this.getVersion().compareTo(var2.getVersion());
      }

      return var3;
   }
}
