package hu.piller.enykp.alogic.helppanel;

import hu.piller.enykp.util.base.ErrorList;
import java.io.File;
import java.util.Hashtable;
import javax.swing.JPanel;

public class Help {
   private static final Long RESOURCE_ERROR_ID = new Long(15000L);
   private static final Hashtable instances = new Hashtable();
   private HelpPanel help_panel = new HelpPanel();
   private String name;
   private String forcePath = null;

   public static Help getInstance(String var0) {
      Object var1 = instances.get(var0);
      if (var1 == null && var0 != null) {
         var1 = new Help(var0);
         instances.put(var0, var1);
      }

      return (Help)var1;
   }

   public void setForcePath(String var1) {
      if (var1 != null) {
         File var2 = new File(var1);
         this.forcePath = var2.getParent();
      } else {
         this.forcePath = var1;
      }

   }

   private Help(String var1) {
      this.name = var1;
   }

   public void release() {
      instances.clear();
   }

   public void update_url(String var1, String var2, String var3, String var4) {
      String var5 = var2;
      if (var2 != null) {
         var5 = var2.replace('/', '-');
      }

      try {
         int var8 = var5.indexOf("#");
         String var9 = var8 == -1 ? var5 : var5.substring(0, var8);
         String var7 = var1 + File.separator + var3 + File.separator + var4;
         int var10 = var1.indexOf("file:///");
         File var6;
         if (var10 == -1) {
            var6 = this.help_panel.getHelpBusiness().getFileByLocation(var7 + File.separator + var9);
         } else {
            String var11 = var1.substring(8) + File.separator + var3 + File.separator + var4;
            var6 = new File(var11 + File.separator + var9);
         }

         if (var6.exists()) {
            this.update_url(var7 + File.separator + var5);
            return;
         }
      } catch (Exception var12) {
         System.out.println("e = " + var12);
      }

      this.update_url(var1 + File.separator + var5);
   }

   public void update_url(String var1) {
      try {
         HelpPanelBusiness var2 = this.help_panel.getHelpBusiness();
         if (var1.trim().length() == 0) {
            var2.enableButtons(false);
            var2.clearContent();
         } else {
            if (this.forcePath != null) {
               var1 = this.forcePath + "\\" + (new File(var1)).getName();
            }

            var2.updateHelpPanel(var2.getURL(var2.getFileByLocation(var1)));
            var2.enableButtons(true);
         }
      } catch (Exception var3) {
         this.writeError(RESOURCE_ERROR_ID, "Hibás súgó útvonal ! (" + var1 + ")", var3, (Object)null);
      }

   }

   public JPanel getPanel() {
      return this.help_panel;
   }

   public String getName() {
      return this.name;
   }

   private void writeError(Object var1, String var2, Exception var3, Object var4) {
      ErrorList.getInstance().writeError(var1, var2, var3, var4);
   }
}
