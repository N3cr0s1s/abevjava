package hu.piller.enykp.interfaces;

import java.util.Hashtable;

public interface IDataField {
   void setValue(Object var1);

   void setFeatures(Hashtable var1);

   Object getFieldValue();

   Object getFieldValueWOMask();

   void setZoom(int var1);

   int getRecordIndex();
}
