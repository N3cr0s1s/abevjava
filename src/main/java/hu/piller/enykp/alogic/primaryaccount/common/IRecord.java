package hu.piller.enykp.alogic.primaryaccount.common;

import java.awt.Component;
import java.util.Hashtable;

public interface IRecord {
   String DESC_ID_ABEVNEWPANEL = "abev_new_panel_description";

   void delete();

   String getName();

   String getDescription(String var1);

   Hashtable getData();

   void setData(Object var1, Object var2);

   void reload();

   void save();

   void printEnvelope(Component var1);

   DefaultEnvelope getEnvelope();

   IRecord[] filter(String var1, String var2);

   IRecord[] filterOnAll(String var1, String var2);
}
