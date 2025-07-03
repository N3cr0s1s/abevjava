package hu.piller.enykp.util.base.eventsupport;

import java.util.EventObject;

public class Event extends EventObject {
   private Object user_data;

   public Event(Object var1) {
      super(var1);
   }

   public Event(Object var1, Object var2) {
      super(var1);
      this.user_data = var2;
   }

   public Object getUserData() {
      return this.user_data;
   }
}
