package hu.piller.enykp.alogic.masterdata.sync.ui.pdfexport;

import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.MDMaintenanceModel;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.MDMaintenanceTableModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.swing.table.TableModel;

public class ViewModelToHtml {
   public static Properties labels = new Properties();

   public static String maintenanceTableModelToHtml(Object[][] var0) {
      StringBuffer var1 = new StringBuffer("<table>");
      var1.append("<tr><td>Adat</td><td>Helyben tárolt érték</td><td>NAV-tól kapott érték</td></tr>");

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.append("<tr><td>").append(var0[var2][0]).append("</td><td>").append(var0[var2][1]).append("</td><td>").append(var0[var2][2]).append("</td>");
      }

      return var1.append("</table>").toString();
   }

   public static String responseListSelectedToHtml(List<MDMaintenanceTableModel> var0) {
      StringBuffer var1 = new StringBuffer("<table>");
      return var1.append("</table>").toString();
   }

   public static String responseTableModelToHtml(Object[][] var0) {
      StringBuffer var1 = new StringBuffer("<table>");
      var1.append("<tr><td>Státusz</td><td>Típus</td><td>Név</td><td>Adószám vagy<br/>Adóazonosító jel</td><td>Teendő</td></tr>");
      return var1.append("</table>").toString();
   }

   public static String mDMaintenanceModelListAsHtml(List<MDMaintenanceModel> var0) {
      ArrayList var1 = new ArrayList();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         MDMaintenanceModel var3 = (MDMaintenanceModel)var2.next();
         Object[] var4 = new Object[3];
         String var5 = labels.getProperty(var3.getName());
         if (var5 == null) {
            var5 = var3.getName();
         }

         var4[0] = var5;
         var4[1] = var3.getLocalValue();
         var4[2] = var3.getNavValue();
         var1.add(var4);
      }

      return arrayAsHtml((Object[][])var1.toArray(new Object[var1.size()][3]), new String[]{"Adatnév", "Érték a törzsadattárban", "NAV-tól letöltött érték"});
   }

   public static String tableModelAsHtml(TableModel var0, IMapperCallback var1) {
      int var2 = var0.getRowCount();
      int var3 = var0.getColumnCount();
      ArrayList var4 = new ArrayList();

      for(int var5 = 0; var5 < var3; ++var5) {
         if (var1.isColumnEnabled(var5)) {
            var4.add(var0.getColumnName(var5));
         }
      }

      String[] var10 = (String[])var4.toArray(new String[var4.size()]);
      Object[][] var6 = new Object[var2][var10.length];

      for(int var7 = 0; var7 < var2; ++var7) {
         int var8 = 0;

         for(int var9 = 0; var9 < var3; ++var9) {
            if (var1.isColumnEnabled(var9)) {
               var6[var7][var8++] = var1.mapValue(var9, var0.getValueAt(var7, var9));
            }
         }
      }

      return arrayAsHtml(var6, var10);
   }

   public static String arrayAsHtml(Object[][] var0, String[] var1) {
      byte var2 = 8;
      StringBuffer var3 = (new StringBuffer("<table style=\"font-size:")).append(var2).append("px;\" border=\"1\">");
      var3.append("<tr>");
      String[] var4 = var1;
      int var5 = var1.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = var4[var6];
         var3.append("<td align=\"center\"><b>").append(var7).append("</b></td>");
      }

      var3.append("</tr>");

      for(int var8 = 0; var8 < var0.length; ++var8) {
         var3.append("<tr>");

         for(var5 = 0; var5 < var0[var8].length; ++var5) {
            var3.append("<td>").append(var0[var8][var5]).append("</td>");
         }

         var3.append("</tr>");
      }

      return var3.append("</table>").toString();
   }

   static {
      try {
         labels.load(ClassLoader.getSystemClassLoader().getResourceAsStream("resources/masterdata/syncmasterdatalabel.properties"));
      } catch (IOException var1) {
      }

   }
}
