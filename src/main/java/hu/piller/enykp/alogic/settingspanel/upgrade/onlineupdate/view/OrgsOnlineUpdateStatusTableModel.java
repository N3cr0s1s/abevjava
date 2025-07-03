package hu.piller.enykp.alogic.settingspanel.upgrade.onlineupdate.view;

import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.settingspanel.upgrade.onlineupdate.model.OrgsOnlineUpdateStatus;
import hu.piller.enykp.alogic.settingspanel.upgrade.onlineupdate.model.Status;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;
import javax.swing.table.DefaultTableModel;

public class OrgsOnlineUpdateStatusTableModel extends DefaultTableModel {
   private static final String[] header = new String[]{"Szervezet azonosító", "Várakozás tiltása"};

   public OrgsOnlineUpdateStatusTableModel(OrgsOnlineUpdateStatus var1) {
      this.setDataVector(this.buildTableModelFromBusinessModel(var1), header);
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

   public Object getValueAt(int var1, int var2) {
      Object var3 = super.getValueAt(var1, var2);
      if (var2 == 0) {
         var3 = OrgInfo.getInstance().getOrgShortnameByOrgID((String)var3);
      }

      return var3;
   }

   public OrgsOnlineUpdateStatus buildBusinessModelFromTableModel() {
      OrgsOnlineUpdateStatus var1 = new OrgsOnlineUpdateStatus();

      for(int var2 = 0; var2 < this.getRowCount(); ++var2) {
         String var3 = (String)((Vector)this.getDataVector().elementAt(var2)).elementAt(0);
         Boolean var4 = (Boolean)this.getValueAt(var2, 1);
         var1.addOrgStatus(var3, var4 ? Status.DISABLED : Status.ENABLED);
      }

      return var1;
   }

   private Object[][] buildTableModelFromBusinessModel(OrgsOnlineUpdateStatus var1) {
      Object[][] var2 = new Object[var1.getOrgsOnlineUpdateStatus().size()][2];
      int var3 = 0;

      for(Iterator var4 = var1.getOrgsOnlineUpdateStatus().entrySet().iterator(); var4.hasNext(); ++var3) {
         Entry var5 = (Entry)var4.next();
         var2[var3][0] = var5.getKey();
         var2[var3][1] = Status.DISABLED.equals(var5.getValue()) ? Boolean.TRUE : Boolean.FALSE;
      }

      return var2;
   }
}
