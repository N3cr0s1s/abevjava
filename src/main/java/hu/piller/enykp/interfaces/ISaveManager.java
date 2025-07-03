package hu.piller.enykp.interfaces;

import java.io.OutputStream;

public interface ISaveManager {
   void save(OutputStream var1);

   Object getHelper(OutputStream var1);

   OutputStream getStream(String var1);

   String createFileName(String var1);

   String getFileNameSuffix();

   String getDescription();

   boolean save(String var1);

   Object getErrorList();
}
