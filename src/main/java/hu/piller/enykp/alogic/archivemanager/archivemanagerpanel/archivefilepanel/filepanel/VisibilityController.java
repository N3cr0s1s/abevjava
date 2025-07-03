package hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.archivefilepanel.filepanel;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.JComponent;

public class VisibilityController implements ComponentListener {
   private final Hashtable visibilities = new Hashtable(32);
   private boolean is_visible;
   private boolean is_visible_all;

   public void setComponentVisibility(JComponent var1, boolean var2) {
      if (var1 != null) {
         this.visibilities.put(var1, var2 ? Boolean.TRUE : Boolean.FALSE);
      }

   }

   public boolean getComponentVisibility(JComponent var1) {
      if (var1 != null) {
         Object var2 = this.visibilities.get(var1);
         if (var2 instanceof Boolean) {
            return (Boolean)var2;
         }
      }

      return false;
   }

   public void setVisible(boolean var1) {
      this.setVisible(var1, false);
   }

   public void setVisibleAll(boolean var1) {
      this.setVisible(var1, true);
   }

   private void setVisible(boolean var1, boolean var2) {
      this.is_visible = var1;
      this.is_visible_all = var2;
      this.setVisibile();
   }

   public void setVisibile() {
      Iterator var2 = this.visibilities.keySet().iterator();

      while(true) {
         JComponent var1;
         do {
            if (!var2.hasNext()) {
               this.is_visible_all = false;
               return;
            }

            var1 = (JComponent)var2.next();
         } while(!this.is_visible_all && !this.getComponentVisibility(var1));

         var1.setVisible(this.is_visible);
      }
   }

   public boolean isVisible() {
      return this.is_visible;
   }

   public void componentHidden(ComponentEvent var1) {
   }

   public void componentMoved(ComponentEvent var1) {
   }

   public void componentResized(ComponentEvent var1) {
      this.setVisibile();
   }

   public void componentShown(ComponentEvent var1) {
   }
}
