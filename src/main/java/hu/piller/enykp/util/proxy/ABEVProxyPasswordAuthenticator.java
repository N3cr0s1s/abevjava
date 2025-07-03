package hu.piller.enykp.util.proxy;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.Authenticator.RequestorType;

public class ABEVProxyPasswordAuthenticator extends Authenticator {
   private static final String PROXY_DUMP = "proxy.dump";
   private static final String ERR_MSG = "%1$s proxy authentikációs kérés a(z) %2$s:%3$s hostról (protokoll: %4$s)";
   private String username;
   private char[] password;

   public ABEVProxyPasswordAuthenticator(String var1, char[] var2) {
      this.username = var1;
      this.password = var2;
   }

   protected PasswordAuthentication getPasswordAuthentication() {
      PasswordAuthentication var1 = null;
      if (this.isDumpEnabled()) {
         this.dump();
      }

      if (RequestorType.PROXY.equals(this.getRequestorType())) {
         String var2 = this.getRequestingProtocol().toLowerCase();
         String var3 = System.getProperty(var2 + ".proxyHost");
         String var4 = System.getProperty(var2 + ".proxyPort");
         if (this.getRequestingHost().equalsIgnoreCase(var3) && this.getRequestingPort() == Integer.parseInt(var4)) {
            var1 = new PasswordAuthentication(this.username, this.password);
         }
      }

      return var1;
   }

   private boolean isDumpEnabled() {
      return System.getProperty("proxy.dump") != null;
   }

   private void dump() {
      String var1 = String.format("%1$s proxy authentikációs kérés a(z) %2$s:%3$s hostról (protokoll: %4$s)", this.getRequestorType(), this.getRequestingHost(), this.getRequestingPort(), this.getRequestingScheme());
      System.err.println(var1);
   }
}
