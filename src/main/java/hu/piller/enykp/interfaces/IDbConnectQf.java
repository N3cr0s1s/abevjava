package hu.piller.enykp.interfaces;

public interface IDbConnectQf {
   int[] begin(String var1, String[] var2);

   int add(String var1, String[] var2);

   int setvalue(String[] var1, Object var2);

   int partend();

   int end();

   boolean hasEndSignal();

   boolean isTestMode();
}
