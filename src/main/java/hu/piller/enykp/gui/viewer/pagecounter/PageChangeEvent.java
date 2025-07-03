package hu.piller.enykp.gui.viewer.pagecounter;

import javax.swing.event.ChangeEvent;

public class PageChangeEvent extends ChangeEvent {
   public static final String EVT_NEW_PAGE = "new_page";
   public static final String EVT_DELETE_PAGE = "delete_page";
   public static final String EVT_CHANGE_PAGE = "change_page";
   private String event;
   private int old_value;

   public PageChangeEvent(Object var1) {
      super(var1);
   }

   public void setEventType(String var1) {
      this.event = var1;
   }

   public String getEventType() {
      return this.event;
   }

   public void setOldValue(int var1) {
      this.old_value = var1;
   }

   public int getOldValue() {
      return this.old_value;
   }
}
