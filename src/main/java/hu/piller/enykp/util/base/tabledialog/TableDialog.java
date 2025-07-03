package hu.piller.enykp.util.base.tabledialog;

import hu.piller.enykp.gui.GuiUtil;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

public class TableDialog {
   public static void showTableDialog(JFrame var0, String var1, File var2) {
      showTableDialog(var0, var1, var2, (String[])null, ";", (String)null);
   }

   public static void showTableDialog(JFrame var0, String var1, File var2, String[] var3) {
      showTableDialog(var0, var1, var2, var3, ";", (String)null);
   }

   public static void showTableDialog(JFrame var0, String var1, File var2, String[] var3, String var4) {
      showTableDialog(var0, var1, var2, var3, var4, (String)null);
   }

   public static void showTableDialog(JFrame var0, String var1, File var2, String[] var3, String var4, String var5) {
      showTableDialog(var0, var1, var2, var3, var4, var5, false);
   }

   public static void showTableDialog(JFrame var0, String var1, File var2, String[] var3, String var4, String var5, boolean var6) {
      final JDialog var7 = new JDialog(var0, true);
      if (var2 != null && var2.exists()) {
         var7.getContentPane().add(new TablePanel(var2, var3, var4, var5, var6));
         String var8 = "EscPressedKey";
         var7.getRootPane().getInputMap(2).put(KeyStroke.getKeyStroke(27, 0), var8);
         var7.getRootPane().getActionMap().put(var8, new AbstractAction() {
            public void actionPerformed(ActionEvent var1) {
               var7.dispose();
            }
         });
         String var9 = "";

         int var10;
         for(var10 = 0; var10 < var3.length; ++var10) {
            var9 = var9 + var3[var10] + "WW";
         }

         var10 = Math.max(GuiUtil.getW(var9 + "WW"), 800);
         var7.setSize((int)Math.min((double)var10, (double)GuiUtil.getScreenW() * 0.8D), 500);
         var7.setTitle(var1);
         var7.setResizable(true);
         var7.setLocationRelativeTo(var0);
         var7.setDefaultCloseOperation(2);
         var7.setVisible(true);
      } else {
         GuiUtil.showMessageDialog(var0, "Nincs megjeleníthető adat !", var1, 1);
      }

   }
}
