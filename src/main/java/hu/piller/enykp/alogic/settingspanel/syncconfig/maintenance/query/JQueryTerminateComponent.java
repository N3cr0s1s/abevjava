package hu.piller.enykp.alogic.settingspanel.syncconfig.maintenance.query;

import hu.piller.enykp.alogic.masterdata.sync.download.MasterDataDownload;
import hu.piller.enykp.alogic.masterdata.sync.download.statemachine.State;
import hu.piller.enykp.alogic.masterdata.sync.syncdir.SyncDirException;
import hu.piller.enykp.alogic.masterdata.sync.syncdir.SyncDirHandler;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.StatusPane;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class JQueryTerminateComponent extends JPanel implements ActionListener, PropertyChangeListener {
   private JButton btnTerminate;
   private JLabel lblInfo;
   private static final String MSG_ACTIVE_QUERY = "A(z) QUERYID azonosítójú lekérdezés STATUS.";
   private static final String MSG_NO_QUERY = "Az ÁNYK készen áll NAV törzsadat lekérdezés kérelem beadására.";
   private static final String MSG_ERROR = "A lekérdezés állapot ellenőrzése közben hiba történt!";
   private static final String MSG_STATUS_UNSERVED_INTERRUPTABLE = "kiszolgálatlan, a kérelem státusz ellenőrzés nem fut";
   private static final String MSG_STATUS_UNSERVED_UNINTERRUPTABLE = "kiszolgálatlan, a kérelem státusz ellenőrzése fut";
   private static final String MSG_STATUS_SERVED = "kiszolgálva, egyeztetés folyamatban";

   public JQueryTerminateComponent() {
      this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(184, 207, 229)), "Törzsadat lekérdezés állapota"));
      this.setLayout(new BoxLayout(this, 0));
      this.btnTerminate = new JButton("Megszakít");
      this.btnTerminate.setName("terminate");
      this.btnTerminate.setActionCommand("terminate");
      this.btnTerminate.addActionListener(this);
      this.lblInfo = new JLabel();
      this.add(Box.createHorizontalStrut(10));
      this.add(this.btnTerminate);
      this.add(Box.createHorizontalStrut(10));
      this.add(this.lblInfo);
      this.add(Box.createHorizontalGlue());
      this.addPropertyChangeListener(this);
      this.setQueryInfo();
   }

   public void actionPerformed(ActionEvent var1) {
      try {
         SyncDirHandler.archive();
         MasterDataDownload.getInstance().setState(State.READY);
         StatusPane.thisinstance.syncMessage.setText("");
         StatusPane.thisinstance.syncMessage.setToolTipText((String)null);
         StatusPane.thisinstance.syncMessage.setForeground(Color.BLACK);
      } catch (SyncDirException var3) {
         GuiUtil.showMessageDialog(this, "Hiba", var3.getMessage(), 0);
      }

      this.setQueryInfo();
   }

   public void propertyChange(final PropertyChangeEvent var1) {
      if ("QUERY_INFO".equals(var1.getPropertyName()) && var1.getNewValue() != null) {
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               Object[] var1x = (Object[])Object[].class.cast(var1.getNewValue());
               JQueryTerminateComponent.this.btnTerminate.setEnabled((Boolean)var1x[0]);
               JQueryTerminateComponent.this.lblInfo.setText((String)var1x[1]);
            }
         });
      }

   }

   private void setQueryInfo() {
      Boolean var1 = Boolean.FALSE;
      String var2 = "";
      Object[] var3 = new Object[2];
      if (MasterDataDownload.getInstance().isReady()) {
         var1 = Boolean.FALSE;
         var2 = "Az ÁNYK készen áll NAV törzsadat lekérdezés kérelem beadására.";
      } else {
         String var4;
         try {
            var4 = SyncDirHandler.getQueryId();
         } catch (Exception var6) {
            var4 = "????";
         }

         if (MasterDataDownload.getInstance().isWaitingForResponse()) {
            if (MasterDataDownload.getInstance().isOperationRunning()) {
               var1 = Boolean.FALSE;
               var2 = "A(z) QUERYID azonosítójú lekérdezés STATUS.".replaceAll("STATUS", "kiszolgálatlan, a kérelem státusz ellenőrzése fut");
            } else {
               var1 = Boolean.TRUE;
               var2 = "A(z) QUERYID azonosítójú lekérdezés STATUS.".replaceAll("STATUS", "kiszolgálatlan, a kérelem státusz ellenőrzés nem fut");
            }
         } else if (MasterDataDownload.getInstance().hasResponse()) {
            var1 = Boolean.TRUE;
            var2 = "A(z) QUERYID azonosítójú lekérdezés STATUS.".replace("STATUS", "kiszolgálva, egyeztetés folyamatban");
         }

         var2 = var2.replace("QUERYID", var4);
      }

      var3[0] = var1;
      var3[1] = var2;
      this.firePropertyChange("QUERY_INFO", (Object)null, var3);
   }
}
