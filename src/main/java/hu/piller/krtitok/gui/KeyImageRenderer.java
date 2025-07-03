package hu.piller.krtitok.gui;

import hu.piller.krtitok.KriptoApp;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableCellRenderer;

public class KeyImageRenderer extends DefaultTableCellRenderer {
   private static ImageIcon IMG_KEY_PUB;
   private static ImageIcon IMG_KEY_SEC;
   private static ImageIcon IMG_KEY_PAIR;
   private static String ANYK_FONT_SIZE;

   static {
      byte[] key_pub = KriptoApp.getKep("key_pub.PNG");
      byte[] key_sec = KriptoApp.getKep("key_sec.PNG");
      byte[] key_pair = KriptoApp.getKep("key_pair.PNG");
      String anykFontSize = KriptoApp.getAnykFontSize("anykFontSize");
      if (key_pub != null) {
         IMG_KEY_PUB = new ImageIcon(key_pub);
      }

      if (key_sec != null) {
         IMG_KEY_SEC = new ImageIcon(key_sec);
      }

      if (key_pair != null) {
         IMG_KEY_PAIR = new ImageIcon(key_pair);
      }

      if (anykFontSize != null) {
         ANYK_FONT_SIZE = anykFontSize;
      }

   }

   public void setValue(Object value) {
      if (value != null) {
         if (((Number)value).intValue() == 0) {
            this.setIcon(IMG_KEY_PUB);
         }

         if (((Number)value).intValue() == 1) {
            this.setIcon(IMG_KEY_SEC);
         }

         if (((Number)value).intValue() == 2) {
            this.setIcon(IMG_KEY_PAIR);
         }
      }

   }
}
