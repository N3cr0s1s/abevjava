package hu.piller.enykp.interfaces;

import java.util.Iterator;
import java.util.Vector;

public interface IDataStore {
   void set(Object var1, String var2);

   String get(Object var1);

   void beginTransaction();

   void rollbackTransaction();

   void commitTransaction();

   void reset();

   void remove(Object var1);

   Iterator getCaseIdIterator();

   Object getMasterCaseId(Object var1);

   Object getStatusFlag();

   void setStatusFlag(Object var1);

   void print();

   void setDetails(String var1, Vector var2, String var3);
}
