package hu.piller.enykp.kauclient.browser.kaubrowser;

import hu.piller.enykp.util.JavaInfo;
import hu.piller.enykp.util.base.ErrorList;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.application.Platform;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.SwingUtilities;

public class JFXKauBrowser extends JFXPanel {
   private static final long serialVersionUID = 1L;
   private final ExecutorService pool = Executors.newFixedThreadPool(1);
   private JFXKauBrowserController jfxKauBrowserController;
   private JFXKauBrowserModel jfxKauBrowserModel;
   private Window container;
   private WebView view;
   private WebEngine engine;

   public JFXKauBrowser(Window var1, Map<String, String> var2) {
      Platform.setImplicitExit(false);
      this.container = var1;
      this.jfxKauBrowserModel = new JFXKauBrowserModel();
      this.jfxKauBrowserModel.setAuthTokens(var2);
      this.jfxKauBrowserController = new JFXKauBrowserController();
      this.jfxKauBrowserController.setJfxKauBrowserModel(this.jfxKauBrowserModel);
   }

   public void start() {
      Platform.runLater(() -> {
         this.view = new WebView();
         this.engine = this.view.getEngine();
         if (Boolean.parseBoolean(System.getProperty("kau.trace"))) {
            String var1 = String.format("ANyK built-in browser info : %s", this.engine.getUserAgent());
            System.out.println(var1);
         }

         this.engine.locationProperty().addListener((var1x, var2x, var3) -> {
            try {
               if (Boolean.parseBoolean(System.getProperty("kau.trace"))) {
                  String var4 = String.format("ANyK built-in browser change location OLD='%s'  -->  NEW='%s'", var2x, var3);
                  System.out.println(var4);
               }

               this.jfxKauBrowserController.checkLocation(var3);
            } catch (Exception var5) {
               ErrorList.getInstance().writeError(1000, "KAÜ kliens authentikáció vége vizsgálat hiba", var5, (Object)null);
               this.shutDown();
            }

         });
         this.engine.getLoadWorker().stateProperty().addListener((var1x, var2x, var3) -> {
            try {
               if (Boolean.parseBoolean(System.getProperty("kau.trace"))) {
                  String var4 = String.format("ANyK built-in browser loadWorker %s OLD='%s'  -->  NEW='%s'", var1x, var2x, var3);
                  System.out.println(var4);
               }

               if (this.jfxKauBrowserModel.isPageFlowFinished()) {
                  this.jfxKauBrowserController.onFinishPageFlow();
                  this.shutDown();
               } else if (State.SUCCEEDED.equals(var3)) {
                  this.jfxKauBrowserController.processHtml(this.engine.getDocument());
               }
            } catch (Exception var5) {
               ErrorList.getInstance().writeError(1000, "KAÜ kliens lapbetöltési hiba", var5, (Object)null);
               this.shutDown();
            }

         });
         this.engine.documentProperty().addListener((var1x, var2x, var3) -> {
            if (var3 != null) {
               StringBuilder var4 = (new StringBuilder()).append("var element = document.getElementById('desktop-view');").append("if (element && element.hasAttribute('hidden')) { ").append("  element.removeAttribute('hidden'); ").append("}");
               this.engine.executeScript(var4.toString());
            }

         });
         this.engine.setOnAlert((var0) -> {
            System.out.println(String.format("JS Alert : %s", var0.toString()));
         });
         this.engine.setOnError((var0) -> {
            System.out.println(String.format("JS Error : %s ", var0.toString()));
         });

         try {
            this.setScene(new Scene(this.view));
            this.engine.setJavaScriptEnabled(true);
            this.engine.loadContent(this.jfxKauBrowserModel.getKauLoginFormLoaderHtml(), "text/html");
         } catch (Exception var2) {
            ErrorList.getInstance().writeError(1000, "KAÜ kliens indítási hiba", var2, (Object)null);
            this.shutDown();
         }

      });
   }

   public void abort() {
      Platform.runLater(() -> {
         if (this.engine != null) {
            this.engine.getLoadWorker().cancel();
         }

      });
      this.jfxKauBrowserController.done();
   }

   protected void shutDown() {
      SwingUtilities.invokeLater(() -> {
         try {
            this.container.dispose();
         } catch (Exception var2) {
         }

      });
   }

   public Future<Boolean> hasAuthenticationProcessFinished() {
      return this.pool.submit(() -> {
         try {
            synchronized(this.jfxKauBrowserModel.getSync()) {
               while(!this.jfxKauBrowserModel.isDone()) {
                  System.out.println(Thread.currentThread().getName() + " folyamat befejezésére vár");
                  this.jfxKauBrowserModel.getSync().wait();
               }
            }

            return Boolean.TRUE;
         } catch (Exception var4) {
            ErrorList.getInstance().writeError(1000, "KAÜ azonosítás eredményére várakozás közben hiba történt", var4, (Object)null);
            return Boolean.FALSE;
         }
      });
   }

   public JFXKauBrowserModel getModel() {
      return this.jfxKauBrowserModel;
   }

   protected void processMouseEvent(MouseEvent var1) {
      if (var1.getClickCount() > 1) {
         var1.consume();
      } else {
         super.processMouseEvent(var1);
      }
   }

   static {
      String var0 = JavaInfo.getJavaFXRuntimeVersion();
      if (var0 != null) {
         System.out.println("JavaFX Runtime Version " + var0);
      } else {
         System.out.println("JavaFX Runtime Version nem elérhetö");
      }

   }
}
