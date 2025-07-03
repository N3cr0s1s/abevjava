package hu.piller.enykp.alogic.upgrademanager_v2_0.components.reader;

import java.net.URL;

public class URLOrg implements Comparable {
   public URL url;
   public String org;

   public int compareTo(Object var1) {
      return var1 instanceof URLOrg ? this.org.compareTo(((URLOrg)var1).org) : 0;
   }
}
