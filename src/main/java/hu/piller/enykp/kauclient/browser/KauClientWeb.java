package hu.piller.enykp.kauclient.browser;

import hu.piller.enykp.kauclient.KauClientException;
import hu.piller.enykp.kauclient.KauResult;
import hu.piller.enykp.kauclient.browser.kaubrowser.JFXKauBrowser;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.JDialog;

public final class KauClientWeb extends JDialog {
   private static final long serialVersionUID = 1L;
   private JFXKauBrowser jfxKauBrowser;

   public KauClientWeb(Window var1, Map<String, String> var2) {
      super(var1);
      this.init(var2);
   }

   private void init(Map<String, String> var1) {
      this.setTitle("KAÜ felhasználó azonosítás");
      this.setSize(640, 680);
      if (this.getOwner() != null) {
         this.setLocationRelativeTo(this.getOwner());
      }

      this.setDefaultCloseOperation(2);
      this.setModal(true);
      this.jfxKauBrowser = new JFXKauBrowser(this, var1);
      this.addWindowListener(new WindowAdapter() {
         public void windowOpened(WindowEvent var1) {
            KauClientWeb.this.jfxKauBrowser.start();
         }

         public void windowClosing(WindowEvent var1) {
            KauClientWeb.this.jfxKauBrowser.abort();
         }

         public void windowClosed(WindowEvent var1) {
            KauClientWeb.this.jfxKauBrowser.abort();
         }
      });
      this.add(this.jfxKauBrowser);
   }

   public KauResult getKauResult() throws KauClientException {
      Future var1 = this.jfxKauBrowser.hasAuthenticationProcessFinished();

      try {
         if ((Boolean)var1.get()) {
            if (this.jfxKauBrowser.getModel().isPageFlowFinished() && this.jfxKauBrowser.getModel().getCookie() != null) {
               KauResult var2 = new KauResult();
               var2.setCookie(this.jfxKauBrowser.getModel().getCookie());
               var2.setRelayState(this.jfxKauBrowser.getModel().getRelayState());
               var2.setSamlResponse(this.jfxKauBrowser.getModel().getSamlResponse());
               var2.setSubjectConfirmationRequired(this.jfxKauBrowser.getModel().isSubjectConfirmationRequired());
               return var2;
            } else {
               throw new KauClientException("A KAÜ felhasználó azonosítás megszakadt! Kérem, ellenőrizze a hiba okát Szerviz->Üzenetek listában");
            }
         } else {
            throw new KauClientException("A KAÜ felhasználó azonosítás eredménytelen! Kérem, ellenőrizze a hiba okát Szerviz->Üzenetek listában");
         }
      } catch (ExecutionException | InterruptedException var3) {
         var3.printStackTrace();
         return null;
      }
   }
}
