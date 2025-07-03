package hu.piller.enykp.alogic.settingspanel.upgrade;

public enum UpgradeFunction {
   NEW("Új nyomtatvány", "new"),
   OPEN("Nyomtatvány megnyitása", "update"),
   XML_EDIT("XML állomány megnyitása szerkesztésre", "xmledit"),
   XML_CHKUPL("XML állomány ellenőrzése, és átadása elektronikus beküldésre", "xmlchkupl"),
   CUST_IMP("Egyedi import", "custimp");

   private String desc;
   private String cmd;

   private UpgradeFunction(String var3, String var4) {
      this.desc = var3;
      this.cmd = var4;
   }

   public String getDesc() {
      return this.desc;
   }

   public String getCmd() {
      return this.cmd;
   }
}
