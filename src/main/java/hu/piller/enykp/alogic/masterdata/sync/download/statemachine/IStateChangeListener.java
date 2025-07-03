package hu.piller.enykp.alogic.masterdata.sync.download.statemachine;

public interface IStateChangeListener {
   void stateChanged(State var1, State var2);
}
