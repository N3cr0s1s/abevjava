package hu.piller.enykp.alogic.settingspanel.syncconfig.view;

import hu.piller.enykp.alogic.settingspanel.syncconfig.model.ConfigState;
import hu.piller.enykp.alogic.settingspanel.syncconfig.model.SyncConfigModel;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Map.Entry;
import javax.swing.JCheckBox;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JSyncConfigView extends JTabbedPane implements Observer, ChangeListener {
   private JSyncConfigView.MyObservable asObservable = new JSyncConfigView.MyObservable();
   private int oldTabIndex = 0;

   public JSyncConfigView() {
      this.setName("SyncConfigView");
      this.addTab("Magánszemély", JEntityTypeSyncConfigPanelFactory.getInstance().getSyncConfigPanelForEntity("Magánszemély"));
      this.addTab("Egyéni vállalkozó", JEntityTypeSyncConfigPanelFactory.getInstance().getSyncConfigPanelForEntity("Egyéni vállalkozó"));
      this.addTab("Társaság", JEntityTypeSyncConfigPanelFactory.getInstance().getSyncConfigPanelForEntity("Társaság"));
      this.addTab("Leírás", new JSyncConfigHelpPanel());
      this.setToolTipTextAt(0, "Jelölje be, hogy a Magánszemélyek mely adatai vehetnek részt a szinkronizációban");
      this.setToolTipTextAt(1, "Jelölje be, hogy az Egyéni vállalkozók mely adatai vehetnek részt a szinkronizációban");
      this.setToolTipTextAt(2, "Jelölje be, hogy a Társaságok mely adatai vehetnek részt a szinkronizációban");
      this.addChangeListener(this);
      this.setSelectedIndex(0);
   }

   public Observable asObservable() {
      return this.asObservable;
   }

   public String getEntityTypeByTabIndex(int var1) {
      return this.getTitleAt(var1);
   }

   public void update(Observable var1, Object var2) {
      SyncConfigModel var3 = (SyncConfigModel)SyncConfigModel.class.cast(var1);
      Iterator var4 = ((JEntityTypeSyncConfigPanel)this.getComponentAt(this.getSelectedIndex())).getCheckBoxes().entrySet().iterator();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         ((JCheckBox)var5.getValue()).setSelected(ConfigState.ENABLED.equals(var3.getConfig().get(var5.getKey())));
      }

   }

   public void stateChanged(ChangeEvent var1) {
      this.asObservable.changed();
      this.oldTabIndex = this.getSelectedIndex();
   }

   public int getOldTabIndex() {
      return this.oldTabIndex;
   }

   class MyObservable extends Observable {
      public void changed() {
         this.setChanged();
         this.notifyObservers();
         this.clearChanged();
      }
   }
}
