package hu.piller.enykp.alogic.filesaver.enykinner;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ENYKClipboardHandler implements ClipboardOwner {
   public void lostOwnership(Clipboard var1, Transferable var2) {
   }

   public void setClipboardContents(String var1) {
      StringSelection var2 = new StringSelection(var1);
      Clipboard var3 = Toolkit.getDefaultToolkit().getSystemClipboard();
      var3.setContents(var2, this);
   }

   public String getClipboardContents() {
      String var1 = "";
      Clipboard var2 = Toolkit.getDefaultToolkit().getSystemClipboard();
      Transferable var3 = var2.getContents((Object)null);
      boolean var4 = var3 != null && var3.isDataFlavorSupported(DataFlavor.stringFlavor);
      if (var4) {
         try {
            var1 = (String)var3.getTransferData(DataFlavor.stringFlavor);
         } catch (UnsupportedFlavorException var6) {
            var6.printStackTrace();
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }

      return var1;
   }
}
