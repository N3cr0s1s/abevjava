package hu.piller.enykp.alogic.orghandler;

import hu.piller.enykp.util.base.Tools;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class OrgHandler {
   private static final String ORG_ID_APEH = "APEH";
   private static final String ORG_ID_NAV = "NAV";
   public static final String[] IS_NOT_GENERAl_ORG_IDS = new String[]{"APEH", "NAV"};
   private static final Set<String> is_not_general_org_ids;
   private static OrgHandler instance;
   public static final String BASE64_DECODER_ORIGINAL = "Original";
   public static final String BASE64_DECODER_STREAM_SUP = "Stream";
   public static final String DEFAULT_APEH_XMLNS = "http://www.apeh.hu/abev/nyomtatvanyok/2005/01";
   public static final String DEFAULT_COMMON_XMLNS = "http://iop.gov.hu/2007/01/nyk/altalanosnyomtatvany";

   private OrgHandler() {
      Collections.addAll(is_not_general_org_ids, IS_NOT_GENERAl_ORG_IDS);
   }

   public static OrgHandler getInstance() {
      return instance;
   }

   public boolean isNotGeneralOrg(String var1) {
      return var1 == null ? false : is_not_general_org_ids.contains(var1.toUpperCase());
   }

   public String getReDirectedOrgId(String var1) {
      try {
         if (OrgInfo.getInstance().hasSuccessor(var1)) {
            return OrgInfo.getInstance().getSuccessorOrgId(var1);
         }
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
      }

      return var1;
   }

   public String getCheckedOrgId(String var1) {
      return var1 != null && var1.length() != 0 ? var1 : this.getDefaultOrgId();
   }

   public String getDefaultOrgId() {
      return this.getReDirectedOrgId("APEH");
   }

   public int getDeafultNumericPrecision(String var1) {
      return this.isNotGeneralOrg(var1) ? 2 : 8;
   }

   public String getBase64DecoderClass(String var1) {
      return this.isNotGeneralOrg(var1) ? "Stream" : "Original";
   }

   public String getXMLNS(String var1) {
      Vector var2 = OrgInfo.getInstance().getXMLNS(var1, false);
      if (var2 != null) {
         return (String)var2.get(0);
      } else {
         return getInstance().isNotGeneralOrg(var1) ? "http://www.apeh.hu/abev/nyomtatvanyok/2005/01" : "http://iop.gov.hu/2007/01/nyk/altalanosnyomtatvany";
      }
   }

   static {
      is_not_general_org_ids = new HashSet(IS_NOT_GENERAl_ORG_IDS.length);
      instance = new OrgHandler();
   }
}
