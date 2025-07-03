package hu.piller.enykp.alogic.settingspanel.setenv;

public class SetenvHandler {
   private SetenvFileHandler setenvFileHandler = new SetenvFileHandler();
   private SetenvSerializer setenvSerializer = new SetenvSerializer();

   public Setenv load() throws SetenvException {
      return this.setenvSerializer.deser(this.setenvFileHandler.read());
   }

   public void store(Setenv var1) throws SetenvException {
      this.setenvFileHandler.write(this.setenvSerializer.ser(var1));
   }
}
