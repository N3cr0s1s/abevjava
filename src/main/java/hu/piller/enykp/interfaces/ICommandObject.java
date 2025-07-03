package hu.piller.enykp.interfaces;

import java.util.Hashtable;

public interface ICommandObject {
   void execute();

   void setParameters(Hashtable var1);

   Object getState(Object var1);
}
