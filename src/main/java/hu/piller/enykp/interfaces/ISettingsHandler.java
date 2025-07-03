package hu.piller.enykp.interfaces;

public interface ISettingsHandler {
   String PROP_SETTINGS_ID = "code";
   String PROP_FILENAME = "filename";
   String PROP_HANDLER = "handler";
   String PROP_STREAM = "stream";
   String PROP_STORE_DATA = "datastore";
   String PROP_DATA = "data";
   String PROP_DATA_PROVIDER = "provider";

   boolean load(IPropertyList var1);

   boolean save(IPropertyList var1);

   boolean load(Object[] var1);

   boolean save(Object[] var1);
}
