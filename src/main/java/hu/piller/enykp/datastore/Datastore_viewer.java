package hu.piller.enykp.datastore;

import hu.piller.enykp.alogic.filepanels.tablesorter.TableSorter;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class Datastore_viewer {
   public static String toString(GUI_Datastore var0) {
      try {
         StringBuffer var1 = new StringBuffer();
         Enumeration var2 = var0.datas.keys();

         while(var2.hasMoreElements()) {
            StoreItem var3 = (StoreItem)var2.nextElement();
            var1.append(var3.toString());
            var1.append(" = ");
            var1.append(var3.value.toString());
            var1.append("\n");
         }

         return var1.toString();
      } catch (Exception var4) {
         return null;
      }
   }

   public static DefaultTableModel toDTM(BookModel var0, GUI_Datastore var1, Map<String, String> var2) {
      try {
         DefaultTableModel var3 = new DefaultTableModel();
         var3.addColumn("Adóügyi azonosító");
         var3.addColumn("Érték");
         var3.addColumn("Közös azonosító");
         Iterator var4 = var2.keySet().iterator();

         while(var4.hasNext()) {
            Object var5 = var4.next();
            var3.addRow(new Object[]{var5, var2.get(var5), "---- VÁLTOZÓ ----"});
         }

         Enumeration var7 = var1.datas.keys();

         while(var7.hasMoreElements()) {
            StoreItem var8 = (StoreItem)var7.nextElement();
            if (var8.value != null && var8.value.toString().length() > 0) {
               var3.addRow(new Object[]{getVid(var0, var8), var8.value, var8.toString()});
            }
         }

         return var3;
      } catch (Exception var6) {
         return null;
      }
   }

   private static String getVid(BookModel var0, StoreItem var1) {
      String var2;
      try {
         var2 = (String)((Hashtable)var0.getMetas(var0.get_formid()).get(var1.code)).get("vid");
      } catch (Exception var4) {
         var2 = "";
      }

      return var2;
   }

   public static void showDialog(BookModel var0, GUI_Datastore var1, Map<String, String> var2) {
      DefaultTableModel var3 = toDTM(var0, var1, var2);
      if (var3 != null) {
         JTable var4 = new JTable(var3);
         var4.setRowHeight(GuiUtil.getCommonItemHeight() + 2);

         for(int var5 = 0; var5 < var4.getColumnModel().getColumnCount(); ++var5) {
            TableColumn var6 = var4.getColumnModel().getColumn(var5);
            var6.setMinWidth(GuiUtil.getW("WWAdóügyi azonosítóWW"));
            var6.setPreferredWidth(var6.getMinWidth());
         }

         var4.setEnabled(false);
         TableSorter var10 = new TableSorter();
         var10.attachTable(var4);
         var10.setSortEnabled(true);
         JScrollPane var11 = new JScrollPane(var4);
         String var7 = "Összes elem: " + (var3.getRowCount() + var2.size());
         final JDialog var8 = new JDialog(MainFrame.thisinstance, var7, true);
         var8.getContentPane().add(var11);
         JButton var9 = new JButton("Ok");
         var9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               var8.hide();
            }
         });
         var8.getContentPane().add(var9, "South");
         var8.setSize(3 * GuiUtil.getW("WWAdóügyi azonosítóWW"), 600);
         var8.setLocationRelativeTo(MainFrame.thisinstance);
         var8.show();
      }
   }
}
