package hu.piller.enykp.alogic.upgrademanager_v2_0.components.reader;

import java.util.Collection;

public interface ComponentsReader {
   Collection getComponents();

   String[] getOrgsNotConnected();

   void setOrgs(String[] var1);
}
