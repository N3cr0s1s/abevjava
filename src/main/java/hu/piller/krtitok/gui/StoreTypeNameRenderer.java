package hu.piller.krtitok.gui;

import javax.swing.table.DefaultTableCellRenderer;

public class StoreTypeNameRenderer extends DefaultTableCellRenderer {
   public void setValue(Object value) {
      int type = ((Number)value).intValue();
      if (type == 200) {
         this.setText("Java kulcstár");
      }

      if (type == 300) {
         this.setText("Pkcs12 kulcstár");
      }

      if (type == 400) {
         this.setText("X509 tanúsítvány");
      }

      if (type == 120 || type == 110 || type == 140 || type == 130) {
         this.setText("PGP kulcstár");
      }

   }
}
