package hu.piller.enykp.alogic.filesaver.db;

import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.font.FontHandler;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Vector;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.JTable.PrintMode;
import javax.swing.border.Border;
import javax.swing.table.TableColumn;

public class Utalasi_tetelek_dialog {
   static JDialog dia;
   static String barcode;
   static Font font;
   static Font pfont;
   static String ret;

   public static void show(String[] var0, String var1) {
      barcode = var1;
      Hashtable var2 = new Hashtable();
      var2.put("font_name", "Courier New");
      var2.put("font_style", new Integer(0));
      var2.put("font_size", new Integer(13));
      font = (Font)FontHandler.getInstance().getFont(var2);
      var2.put("font_size", new Integer(8));
      pfont = (Font)FontHandler.getInstance().getFont(var2);
      dia = new JDialog(MainFrame.thisinstance, "Keletkező kiutalási tételek adatai", true);
      Dimension var3 = new Dimension(1000, 250);
      dia.setSize(var3);
      dia.setLocationRelativeTo(MainFrame.thisinstance);
      dia.setContentPane(makeContent(dia, var0));
      dia.setVisible(true);
   }

   private static JPanel makeContent(final JDialog var0, String[] var1) {
      JPanel var2 = new JPanel(new BorderLayout());
      final JList var3 = new JList(var1);
      var3.setFont(new Font("Monospaced", 0, 12));
      var3.setCellRenderer(new DefaultListCellRenderer() {
         public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
            return super.getListCellRendererComponent(var1, var2, var3, false, false);
         }
      });
      var2.add(new JScrollPane(var3));
      JPanel var4 = new JPanel();
      JButton var5 = new JButton("A javítás befejezve, kiutalás mehet");
      var5.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Utalasi_tetelek_dialog.ret = "V";
            var0.setVisible(false);
            var0.dispose();
         }
      });
      JButton var6 = new JButton("A javítás még nem befejezett");
      var6.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Utalasi_tetelek_dialog.ret = "F";
            var0.setVisible(false);
            var0.dispose();
         }
      });
      JButton var7 = new JButton("Javítás befejezve, hibásak a kiutalás adatok");
      var7.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Utalasi_tetelek_dialog.ret = "H";
            var0.setVisible(false);
            var0.dispose();
         }
      });
      var4.add(var5);
      var4.add(var6);
      var4.add(var7);
      JButton var8 = new JButton("Nyomtat");
      var8.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ListModel var2 = var3.getModel();
            Vector var3x = new Vector();

            for(int var4 = 0; var4 < var2.getSize(); ++var4) {
               Vector var5 = new Vector();
               var5.add(var2.getElementAt(var4).toString().replaceAll(" ", " "));
               var3x.add(var5);
            }

            Vector var11 = new Vector();
            var11.add(0, "Utalási tételek");
            JTable var12 = new JTable(var3x, var11);
            var12.setShowGrid(false);
            var12.setBorder((Border)null);
            var12.setFont(Utalasi_tetelek_dialog.pfont);
            Dimension var6 = var12.getPreferredSize();
            var6.width = 900;
            var12.setSize(var6);
            var12.setPreferredSize(var6);
            TableColumn var7 = var12.getColumnModel().getColumn(0);
            var7.setPreferredWidth(900);
            var7.setWidth(900);

            try {
               HashPrintRequestAttributeSet var8 = new HashPrintRequestAttributeSet();
               var8.add(OrientationRequested.LANDSCAPE);
               MediaPrintableArea var9 = new MediaPrintableArea(0, 0, 210, 370, 1000);
               var8.add(var9);
               var12.print(PrintMode.FIT_WIDTH, (MessageFormat)null, (MessageFormat)null, true, var8, true);
            } catch (PrinterException var10) {
               var10.printStackTrace();
            }

         }
      });
      var4.add(var8);
      var2.add(var4, "South");
      return var2;
   }
}
