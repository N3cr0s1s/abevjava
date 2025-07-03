package hu.piller.enykp.alogic.masterdata.sync.download.statemachine;

import java.util.Map;

public interface IStateMachine {
   void setState(State var1);

   void addStateChangeListener(IStateChangeListener var1);

   void removeStateChangeListener(IStateChangeListener var1);

   void removeAllStateChangeListeners();

   Map<String, Object> getContextParams();
}
