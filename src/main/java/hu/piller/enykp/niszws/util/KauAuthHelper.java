package hu.piller.enykp.niszws.util;

import hu.piller.enykp.alogic.filepanels.mohu.Rejtely;

public class KauAuthHelper {
   private static KauAuthHelper ourInstance = new KauAuthHelper();
   private String mohuUser = "";
   private String mohuPass = "";
   private String anyGateId = "";
   private boolean ugyfelkapura = true;
   private GateType gateType;
   private boolean saveUserAndPass;
   private boolean saveAuthData4Group;
   private Rejtely rejtely;

   public static KauAuthHelper getInstance() {
      return ourInstance;
   }

   private KauAuthHelper() {
      this.gateType = GateType.UGYFELKAPU;
      this.saveUserAndPass = false;
      this.saveAuthData4Group = false;
      this.rejtely = Rejtely.getInstance();
   }

   public String getMohuUser() {
      Rejtely var10000 = this.rejtely;
      return Rejtely.decode(this.mohuUser);
   }

   public void setMohuUser(String var1) {
      this.mohuUser = this.rejtely.encode(var1);
   }

   public String getMohuPass() {
      Rejtely var10000 = this.rejtely;
      return Rejtely.decode(this.mohuPass);
   }

   public void setMohuPass(char[] var1) {
      this.mohuPass = this.rejtely.encode(new String(var1));
   }

   public String getAnyGateId() {
      Rejtely var10000 = this.rejtely;
      return Rejtely.decode(this.anyGateId);
   }

   public void setAnyGateId(String var1) {
      if (var1 == null) {
         this.anyGateId = null;
      } else if ("".equals(var1)) {
         this.anyGateId = null;
      } else {
         this.anyGateId = this.rejtely.encode(var1);
      }
   }

   public boolean isUgyfelkapura() {
      return this.ugyfelkapura;
   }

   public void setUgyfelkapura(boolean var1) {
      this.ugyfelkapura = var1;
   }

   public GateType getGateType() {
      return this.gateType;
   }

   public void setGateType(GateType var1) {
      this.gateType = var1;
   }

   public boolean isSaveUserAndPass() {
      return this.saveUserAndPass;
   }

   public void setSaveUserAndPass(boolean var1) {
      this.saveUserAndPass = var1;
   }

   public void guiSetSaveUserAndPass(boolean var1) {
      this.saveUserAndPass = var1;
   }

   public void reset() {
      this.saveUserAndPass = false;
      this.ugyfelkapura = true;
      this.gateType = GateType.UGYFELKAPU;
      this.mohuUser = "";
      this.mohuPass = "";
      this.anyGateId = "";
      this.saveAuthData4Group = false;
   }

   public void resetOfficeUsername() {
      this.saveUserAndPass = false;
      this.anyGateId = "";
   }

   public boolean isSaveAuthData() {
      return this.saveAuthData4Group;
   }

   public void setSaveAuthData(boolean var1) {
      this.saveAuthData4Group = var1;
   }
}
