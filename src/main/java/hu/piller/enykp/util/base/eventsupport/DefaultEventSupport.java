package hu.piller.enykp.util.base.eventsupport;

import hu.piller.enykp.util.base.Tools;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

public class DefaultEventSupport implements IEventSupport {
   private final Vector events = new Vector(32, 32);
   private final Vector event_results = new Vector(32, 32);
   private boolean synchronous_event_firing = true;
   private int thread_limit = Integer.MAX_VALUE;
   private volatile int tread_count = 0;

   public DefaultEventSupport() {
   }

   public DefaultEventSupport(boolean var1) {
      this.synchronous_event_firing = var1;
   }

   public DefaultEventSupport(boolean var1, int var2) {
      this.synchronous_event_firing = var1;
      this.thread_limit = var2;
   }

   public synchronized void addEventListener(IEventListener var1) {
      if (!this.events.contains(var1)) {
         this.events.add(var1);
      }
   }

   public synchronized void removeEventListener(IEventListener var1) {
      if (this.events.contains(var1)) {
         this.events.remove(var1);
      }

   }

   public synchronized Vector fireEvent(final Event var1) {
      int var4;
      if (this.synchronous_event_firing) {
         Vector var2 = this.events;
         Vector var7 = this.event_results;
         var7.clear();
         var4 = 0;

         for(int var5 = var2.size(); var4 < var5; ++var4) {
            var7.add(((IEventListener)var2.get(var4)).eventFired(var1));
         }

         return var7;
      } else {
         Thread var3 = new Thread(new Runnable() {
            public void run() {
               DefaultEventSupport.this.tread_count++;
               Vector var1x = DefaultEventSupport.this.events;
               int var2 = 0;

               for(int var3 = var1x.size(); var2 < var3; ++var2) {
                  try {
                     ((IEventListener)var1x.get(var2)).eventFired(var1);
                  } catch (ArrayIndexOutOfBoundsException var5) {
                     Tools.eLog(var5, 0);
                  }
               }

               DefaultEventSupport.this.tread_count--;
            }
         });
         var4 = this.thread_limit;

         while(this.tread_count > var4) {
            try {
               Thread.sleep(1L);
            } catch (InterruptedException var6) {
            }
         }

         var3.start();
         return new Vector();
      }
   }

   public synchronized void fireEvent(Object var1, String var2, String var3) {
      Hashtable var4 = this.getDefaultEventData(var2, var3);
      this.fireEvent(new Event(var1, var4));
   }

   public synchronized void fireEvent(Object var1, String var2, String var3, Object var4, Object var5) {
      Hashtable var6 = this.getDefaultEventData(var2, var3);
      var6.put(var4, var5);
      this.fireEvent(new Event(var1, var6));
   }

   public synchronized void fireEvent(Object var1, String var2, String var3, Object var4, boolean var5) {
      Hashtable var6 = this.getDefaultEventData(var2, var3);
      if (var4 != null) {
         if (var5) {
            if (var4 instanceof Map) {
               var6.putAll((Hashtable)var4);
            } else {
               var6.put("userdata", var4);
            }
         } else {
            var6.put("userdata", var4);
         }
      }

      this.fireEvent(new Event(var1, var6));
   }

   private Hashtable getDefaultEventData(String var1, String var2) {
      Hashtable var3 = new Hashtable(2);
      var3.put("action", var1);
      var3.put("event", var2);
      return var3;
   }
}
