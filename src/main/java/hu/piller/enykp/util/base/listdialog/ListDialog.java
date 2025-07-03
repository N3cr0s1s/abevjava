package hu.piller.enykp.util.base.listdialog;

import hu.piller.enykp.gui.GuiUtil;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

public class ListDialog {
   public static void showListDialog(JFrame var0, String var1, File var2) {
      showListDialog(var0, var1, var2, (String)null);
   }

   public static void showListDialog(JFrame var0, String var1, File var2, String var3) {
      final JDialog var4 = new JDialog(var0, true);
      if (var2 != null && var2.exists()) {
         var4.getContentPane().add(new ListPanel(var2, var3));
         String var5 = "EscPressedKey";
         var4.getRootPane().getInputMap(2).put(KeyStroke.getKeyStroke(27, 0), var5);
         var4.getRootPane().getActionMap().put(var5, new AbstractAction() {
            public void actionPerformed(ActionEvent var1) {
               var4.dispose();
            }
         });
         var4.setSize(800, 500);
         var4.setTitle(var1);
         var4.setResizable(true);
         var4.setLocationRelativeTo(var0);
         var4.setDefaultCloseOperation(2);
         var4.setVisible(true);
      } else {
         GuiUtil.showMessageDialog(var0, "Nincs megjeleníthető adat !", var1, 1);
      }

   }
}
