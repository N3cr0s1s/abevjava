package hu.piller.enykp.alogic.filepanels.mohu;

public interface ILoginDialog {
   boolean showIfNeed();

   int getState();

   boolean isGroupLogin();

   void setGroupLogin(boolean var1);

   String getDefaultCKAzon();

   boolean useFormDataCKAzon();
}
