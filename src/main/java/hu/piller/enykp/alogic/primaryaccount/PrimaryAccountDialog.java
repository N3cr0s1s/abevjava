package hu.piller.enykp.alogic.primaryaccount;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

public class PrimaryAccountDialog extends JDialog {
   private static PrimaryAccountDialog instance;
   private static PrimaryAccountsPanel panel;

   private PrimaryAccountDialog(Frame var1, PAInfo var2) {
      super(var1);
      this.build(var2);
   }

   private void build(PAInfo var1) {
      panel = new PrimaryAccountsPanel(this, var1);
      this.getContentPane().add(panel);
      String var2 = "EscPressedKey";
      this.getRootPane().getInputMap(2).put(KeyStroke.getKeyStroke(27, 0), var2);
      this.getRootPane().getActionMap().put(var2, new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            PrimaryAccountDialog.this.setVisible(false);
         }
      });
   }

   public static void setStartGroup(int var0) {
      if (panel != null) {
         panel.getBusiness().setStartGroup(var0);
      }

   }

   public static PrimaryAccountDialog getInstance(JFrame var0, PAInfo var1) {
      if (instance == null) {
         instance = new PrimaryAccountDialog(var0, var1);
      }

      return instance;
   }

   public static PrimaryAccountsPanel getPrimaryAccountsPanel() {
      return panel;
   }

   public static void detach() {
      if (instance != null) {
         instance.getContentPane().remove(panel);
      }

   }

   public static void attach() {
      if (instance != null) {
         Container var0 = instance.getContentPane();
         panel.invalidate();
         panel.setVisible(true);
         var0.add(panel);
      }

   }
}
