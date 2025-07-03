package hu.piller.enykp.alogic.settingspanel.upgrade.onlineupdate.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

public class OrgsOnlineUpdateStatus {
   private Map<String, Status> status = new HashMap();

   public void addOrgStatus(String var1, Status var2) {
      this.status.put(var1, var2);
   }

   public Map<String, Status> getOrgsOnlineUpdateStatus() {
      return Collections.unmodifiableMap(this.status);
   }

   public void merge(String[] var1) {
      if (var1 != null) {
         List var2 = Arrays.asList(var1);
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            if (!this.containsOrg(var4)) {
               this.addOrgStatus(var4, Status.ENABLED);
            }
         }

         HashSet var6 = new HashSet();
         Iterator var7 = this.status.keySet().iterator();

         String var5;
         while(var7.hasNext()) {
            var5 = (String)var7.next();
            if (!var2.contains(var5)) {
               var6.add(var5);
            }
         }

         var7 = var6.iterator();

         while(var7.hasNext()) {
            var5 = (String)var7.next();
            this.status.remove(var5);
         }

      }
   }

   public boolean containsOrg(String var1) {
      return this.status.containsKey(var1);
   }

   public Set<String> getOrgsWithEnabledOnlineUpdate() {
      TreeSet var1 = new TreeSet();
      Iterator var2 = this.status.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if (Status.ENABLED.equals(var3.getValue())) {
            var1.add(var3.getKey());
         }
      }

      return var1;
   }
}
