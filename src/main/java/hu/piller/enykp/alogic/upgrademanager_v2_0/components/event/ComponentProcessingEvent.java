package hu.piller.enykp.alogic.upgrademanager_v2_0.components.event;

import java.util.EventObject;

public class ComponentProcessingEvent extends EventObject {
   private byte state;
   private String organization;
   private String name;
   private String category;
   private String version;
   private String message;

   public ComponentProcessingEvent(Object var1, String var2, String var3, String var4, String var5, byte var6, String var7) {
      super(var1);
      this.organization = var2;
      this.name = var3;
      this.category = var4;
      this.version = var5;
      this.state = var6;
      this.message = var7;
   }

   public byte getState() {
      return this.state;
   }

   public String getMessage() {
      return this.message;
   }

   public String getOrganization() {
      return this.organization;
   }

   public String getName() {
      return this.name;
   }

   public String getCategory() {
      return this.category;
   }

   public String getVersion() {
      return this.version;
   }

   public String toString() {
      return "organization=" + this.organization + ", name=" + this.name + ", category=" + this.category + ", version=" + this.version + ", state=" + this.state + ", message" + this.message;
   }
}
