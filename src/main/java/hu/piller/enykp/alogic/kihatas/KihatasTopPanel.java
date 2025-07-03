package hu.piller.enykp.alogic.kihatas;

import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.ABEVFunctionSet;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.viewer.PageViewer;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class KihatasTopPanel extends JPanel {
   JTextField tf_au;
   int precision;

   public KihatasTopPanel(String var1, String var2, String var3, String var4, DataFieldModel var5) {
      this.precision = ABEVFunctionSet.getInstance().getPrecisionForKihatas(var5.key);
      this.setLayout(new GridLayout(6, 1, 0, 3));
      this.setBorder(BorderFactory.createTitledBorder("Bizonylat cella adatok"));
      JPanel var6 = new JPanel();
      var6.setLayout(new BoxLayout(var6, 2));
      var6.add(new JLabel("Mezőinformációk:"));
      var6.add(Box.createHorizontalStrut(10));
      JTextField var7 = new JTextField();
      var7.setEditable(false);
      var7.setText(var1);
      var7.setCaretPosition(0);
      if (60 < var1.length()) {
         var7.setToolTipText(var1);
      }

      var6.add(var7);
      JPanel var8 = new JPanel();
      var8.setLayout(new BoxLayout(var8, 2));
      var8.add(Box.createHorizontalStrut(26));
      var8.add(new JLabel("Megnevezés:"));
      var8.add(Box.createHorizontalStrut(10));
      var7 = new JTextField();
      var7.setEditable(false);
      var7.setText(var2);
      var7.setCaretPosition(0);
      var7.setToolTipText(var7.getText());
      var8.add(var7);
      this.add(var8);
      this.add(var6);
      JPanel var9 = new JPanel();
      var9.setLayout(new BoxLayout(var9, 2));
      var9.add(Box.createHorizontalGlue());
      var9.add(new JLabel("Jelleg:"));
      var9.add(Box.createHorizontalStrut(10));
      JTextField var10 = new JTextField(15);
      var10.setMaximumSize(new Dimension(250, 25));
      var10.setMinimumSize(new Dimension(100, 25));
      var10.setPreferredSize(new Dimension(210, 25));
      var10.setEditable(false);
      var10.setHorizontalAlignment(4);
      String var11 = (String)var5.features.get("btable_jel");
      if (var11 == null) {
         var11 = "";
      }

      var11 = PageViewer.convertbjel(var11);
      var10.setText(var11);
      var9.add(var10);
      this.add(var9);
      JPanel var12 = new JPanel();
      var12.setLayout(new BoxLayout(var12, 2));
      var12.add(Box.createHorizontalGlue());
      var12.add(new JLabel("Mértékegység:"));
      var12.add(Box.createHorizontalStrut(10));
      JTextField var13 = new JTextField(15);
      var13.setMaximumSize(new Dimension(150, 25));
      var13.setMinimumSize(new Dimension(100, 25));
      var13.setPreferredSize(new Dimension(110, 25));
      var13.setEditable(false);
      var13.setHorizontalAlignment(4);
      String var14 = (String)var5.features.get("mertekegyseg");
      if (var14 == null) {
         var14 = (String)var5.features.get("mask");

         try {
            var14 = var14.split("!", 2)[1];
         } catch (Exception var17) {
            var14 = "";
         }
      }

      var13.setText(var14);
      var12.add(var13);
      this.add(var12);
      JPanel var15 = new JPanel();
      var15.setLayout(new BoxLayout(var15, 2));
      var15.add(Box.createHorizontalGlue());
      var15.add(new JLabel("Adózói érték:"));
      var15.add(Box.createHorizontalStrut(10));
      var7 = new JTextField();
      var7.setMaximumSize(new Dimension(150, 25));
      var7.setMinimumSize(new Dimension(100, 25));
      var7.setPreferredSize(new Dimension(115, 25));
      var7.setEditable(false);
      var7.setHorizontalAlignment(4);
      var7.setText(MultiLineTable.formatnumber(var3, this.precision));
      var15.add(var7);
      this.add(var15);
      JPanel var16 = new JPanel();
      var16.setLayout(new BoxLayout(var16, 2));
      var16.add(Box.createHorizontalGlue());
      var16.add(new JLabel("Adóügyi érték:"));
      var16.add(Box.createHorizontalStrut(10));
      this.tf_au = new JTextField();
      this.tf_au.setMaximumSize(new Dimension(150, 25));
      this.tf_au.setMinimumSize(new Dimension(100, 25));
      this.tf_au.setPreferredSize(new Dimension(115, 25));
      this.tf_au.setEditable(false);
      this.tf_au.setHorizontalAlignment(4);
      this.tf_au.setText(MultiLineTable.formatnumber(var4, this.precision));
      var16.add(this.tf_au);
      this.add(var16);
   }

   public String getAdougyiFieldText() {
      return MultiLineTable.stripformatnumber(this.tf_au.getText());
   }
}
