package hu.piller.krtitok.gui;

import hu.piller.krtitok.KriptoApp;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableCellRenderer;

public class StoreImageRenderer extends DefaultTableCellRenderer {
   private static ImageIcon IMG_STORE_JKS;
   private static ImageIcon IMG_STORE_P12;
   private static ImageIcon IMG_STORE_CER;
   private static ImageIcon IMG_STORE_PGP;

   static {
      byte[] store_jks = KriptoApp.getKep("store_jks.PNG");
      byte[] store_p12 = KriptoApp.getKep("store_p12.PNG");
      byte[] store_cer = KriptoApp.getKep("store_cer.PNG");
      byte[] store_pgp = KriptoApp.getKep("store_pgp.PNG");
      if (store_jks != null) {
         IMG_STORE_JKS = new ImageIcon(store_jks);
      }

      if (store_p12 != null) {
         IMG_STORE_P12 = new ImageIcon(store_p12);
      }

      if (store_cer != null) {
         IMG_STORE_CER = new ImageIcon(store_cer);
      }

      if (store_pgp != null) {
         IMG_STORE_PGP = new ImageIcon(store_pgp);
      }

   }

   public void setValue(Object value) {
      int type = ((Number)value).intValue();
      if (type == 200) {
         this.setIcon(IMG_STORE_JKS);
      }

      if (type == 300) {
         this.setIcon(IMG_STORE_P12);
      }

      if (type == 400) {
         this.setIcon(IMG_STORE_CER);
      }

      if (type == 120 || type == 110 || type == 140 || type == 130) {
         this.setIcon(IMG_STORE_PGP);
      }

   }
}
