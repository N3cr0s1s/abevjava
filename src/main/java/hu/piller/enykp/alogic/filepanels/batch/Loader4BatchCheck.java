package hu.piller.enykp.alogic.filepanels.batch;

import hu.piller.enykp.gui.model.BookModel;

public interface Loader4BatchCheck {
   int RES_NOMESSAGE = -1;
   int RES_MISSION_COMPLETED = 0;
   int RES_ERROR_FILE_NOT_EXISTS = 1;
   int RES_ERROR_ON_PARSELIST = 2;
   int RES_ERROR_ON_LOAD = 3;
   int RES_WARNING_ON_LOAD = 4;

   BookModel superLoad(BookModel var1, String var2);

   String nextId();

   int createList(String var1);

   void setFileToList(String var1);
}
