package hu.piller.enykp.alogic.upgrademanager_v2_0.gui.orgselector;

import hu.piller.enykp.alogic.orghandler.OrgInfo;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;

public class OrgSelectorTableModel extends DefaultTableModel {
   private static final String[] header = new String[]{"Szervezet azonosító", "Frissítésre kijelöl"};

   public OrgSelectorTableModel(Collection<String> var1) {
      this.setDataVector(this.buildContent(var1), header);
   }

   public int getColumnCount() {
      return header.length;
   }

   public Class<?> getColumnClass(int var1) {
      return var1 == 0 ? String.class : Boolean.class;
   }

   public String getColumnName(int var1) {
      return header[var1];
   }

   public boolean isCellEditable(int var1, int var2) {
      return var2 != 0;
   }

   private Object[][] buildContent(Collection<String> var1) {
      Collection var2 = OrgInfo.getInstance().getOrgIds();
      Object[][] var3 = new Object[var2.size()][2];
      int var4 = 0;

      for(Iterator var5 = var2.iterator(); var5.hasNext(); ++var4) {
         String var6 = (String)var5.next();
         var3[var4][0] = var6;
         if (var1.contains(var6)) {
            var3[var4][1] = Boolean.TRUE;
         } else {
            var3[var4][1] = Boolean.FALSE;
         }
      }

      return var3;
   }

   public Object getValueAt(int var1, int var2) {
      Object var3 = super.getValueAt(var1, var2);
      if (var2 == 0) {
         var3 = OrgInfo.getInstance().getOrgShortnameByOrgID((String)var3);
      }

      return var3;
   }
}
