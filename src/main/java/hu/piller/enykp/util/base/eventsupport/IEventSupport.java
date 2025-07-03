package hu.piller.enykp.util.base.eventsupport;

import java.util.Vector;

public interface IEventSupport {
   String KEY_ACTION = "action";
   String KEY_EVENT = "event";
   String KEY_USERDATA = "userdata";
   String ACT_INSERT = "insert";
   String ACT_UPDATE = "update";
   String ACT_DELETE = "delete";

   void addEventListener(IEventListener var1);

   void removeEventListener(IEventListener var1);

   Vector fireEvent(Event var1);
}
