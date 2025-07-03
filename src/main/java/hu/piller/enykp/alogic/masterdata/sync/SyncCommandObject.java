package hu.piller.enykp.alogic.masterdata.sync;

import hu.piller.enykp.alogic.masterdata.sync.download.MasterDataDownload;
import hu.piller.enykp.alogic.masterdata.sync.ui.entityfilter.SyncEntityFilterForm;
import hu.piller.enykp.alogic.masterdata.sync.ui.response.JMDResponseDialog;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.framework.StatusPane;
import hu.piller.enykp.interfaces.ICommandObject;
import java.awt.Color;
import java.util.Hashtable;
import javax.swing.Icon;
import javax.swing.JOptionPane;

public class SyncCommandObject implements ICommandObject {
   public void execute() {
      if (MasterDataDownload.getInstance().isReady()) {
         this.newRequest();
      } else if (MasterDataDownload.getInstance().isWaitingForResponse()) {
         this.waitingForResponse();
      } else if (MasterDataDownload.getInstance().hasResponse()) {
         StatusPane.thisinstance.syncMessage.setText("");
         StatusPane.thisinstance.syncMessage.setToolTipText((String)null);
         StatusPane.thisinstance.syncMessage.setForeground(Color.BLACK);
         this.masterDataMaintenance();
      } else {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A törzsadat karbantartó állapota érvénytelen", "Hiba", 0);
      }

   }

   private void newRequest() {
      new SyncEntityFilterForm();
   }

   private void waitingForResponse() {
      if (MasterDataDownload.getInstance().isOperationRunning()) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A NAV-hoz benyújtott törzsadatlekérdezés-kérelem kiszolgálást figyelő háttérfolyamat fut.\nKérem, figyelje a tájékoztató üzenetet az adatok megérkezéséről!", "Figyelmeztetés", 1);
      } else {
         Object[] var1 = new Object[]{"Indítás", "Mégsem"};
         int var2 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Elindítja a NAV-hoz benyújtott törzsadatlekérdezés-kérelem megválaszolását figyelő háttérfolyamatot?\n\nAmennyiben igen, és nincsen aktív kapcsolata a NAV Adatkapuval, akkor a program rövidesen kérni fogja \nmagyarorszag.hu felhasználónevét és jelszavát.\n\nHa nem Ön volt a kérelem benyújtója, akkor kérem lépjen kapcsolatba azzal, aki benyújtotta. A kérelemre\nadott válasz letöltésére csak a kérelem benyújtója jogosult, magyarorszag.hu felhasználónevének,\nés jelszavának megadása, és sikeres NAV azonosítása után.", "Figyelmeztetés", 0, 3, (Icon)null, var1, var1[1]);
         if (var2 == 0) {
            MasterDataDownload.getInstance().resumeOperation();
            StatusPane.thisinstance.syncMessage.setText("Törzsadatlekérdezési kérelem kiszolgálását figyelő folyamat fut");
         }
      }

   }

   private void masterDataMaintenance() {
      if (!JMDResponseDialog.isRunning()) {
         Object[] var1 = new Object[]{"Igen", "Nem"};
         int var2 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Elindítja a törzsadat lekérdezés eredmény állományának egyeztetését?", "Figyelmeztetés", 0, 3, (Icon)null, var1, var1[1]);
         if (var2 == 0) {
            new JMDResponseDialog();
         }

      }
   }

   public void setParameters(Hashtable var1) {
   }

   public Object getState(Object var1) {
      return true;
   }
}
