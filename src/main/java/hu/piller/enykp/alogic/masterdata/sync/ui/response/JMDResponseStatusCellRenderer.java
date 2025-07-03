package hu.piller.enykp.alogic.masterdata.sync.ui.response;

import java.awt.Color;
import java.awt.Component;
import java.awt.font.TextAttribute;
import java.util.Hashtable;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class JMDResponseStatusCellRenderer implements TableCellRenderer {
   private static final String TXT_ERROR = "Hiba";
   private static final String TXT_SUCCESS = "Siker";
   private JLabel lblError = new JLabel("Hiba");
   private JLabel lblSuccess;
   private JLabel lblToShow;

   private JMDResponseStatusCellRenderer() {
      Hashtable var1 = new Hashtable();
      var1.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
      var1.put(TextAttribute.FOREGROUND, Color.BLUE);
      this.lblError.setOpaque(true);
      this.lblError.setForeground(Color.RED);
      this.lblSuccess = new JLabel("Siker");
      this.lblSuccess.setOpaque(true);
      this.lblSuccess.setForeground(UIManager.getColor("Table.foreground"));
   }

   public static JMDResponseStatusCellRenderer create() {
      return new JMDResponseStatusCellRenderer();
   }

   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      String var7 = (String)String.class.cast(var2);
      if ("Hiba".equals(var7)) {
         this.lblToShow = this.lblError;
         this.lblToShow.setToolTipText("Kattintson a részletekért!");
      } else if ("Siker".equals(var7)) {
         this.lblToShow = this.lblSuccess;
         this.lblToShow.setToolTipText((String)null);
      } else {
         System.err.println("Érvénytelen állapotkód: " + var7);
      }

      if (var3) {
         this.lblToShow.setBackground(var1.getSelectionBackground());
      } else {
         this.lblToShow.setBackground(var1.getBackground());
      }

      this.lblToShow.setHorizontalAlignment(0);
      return this.lblToShow;
   }
}
