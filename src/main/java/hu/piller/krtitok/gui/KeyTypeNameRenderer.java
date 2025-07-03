package hu.piller.krtitok.gui;

import hu.piller.kripto.keys.KeyManager;
import javax.swing.table.DefaultTableCellRenderer;

public class KeyTypeNameRenderer extends DefaultTableCellRenderer {
   public void setValue(Object value) {
      if (value != null) {
         int type = ((Number)value).intValue();
         this.setText((String)KeyManager.TYPE_NAMES.get("" + type));
      }

   }
}
