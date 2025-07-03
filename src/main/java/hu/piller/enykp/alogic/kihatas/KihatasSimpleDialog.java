package hu.piller.enykp.alogic.kihatas;

import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.ABEVFunctionSet;
import hu.piller.enykp.alogic.fileutil.DataChecker;
import hu.piller.enykp.datastore.Datastore_history;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.viewer.PageViewer;
import hu.piller.enykp.interfaces.IDataStore;
import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class KihatasSimpleDialog extends JDialog {
   JTextField tf;
   DataFieldModel df;
   PageViewer PV;
   Datastore_history dshist;
   IDataStore ds;
   String kskey;
   KihatasTableModel dtm;
   String adonem;
   int precision;

   public static void main(String[] var0) {
      new KihatasSimpleDialog((PageViewer)null, (DataFieldModel)null, (IDataStore)null, (Datastore_history)null, (String)null, (KihatasTableModel)null, "megnevezés", "mezőinformációk", "adónem", "123", "265", "3234");
   }

   public KihatasSimpleDialog(PageViewer var1, DataFieldModel var2, IDataStore var3, Datastore_history var4, String var5, KihatasTableModel var6, String var7, String var8, String var9, String var10, String var11, String var12) {
      super(MainFrame.thisinstance, "Számított mező felülírása", true);
      this.PV = var1;
      this.df = var2;
      this.dshist = var4;
      this.ds = var3;
      this.kskey = var5;
      this.dtm = var6;
      this.adonem = var9;
      this.precision = ABEVFunctionSet.getInstance().getPrecisionForKihatas(var2.key);
      JPanel var13 = new JPanel(new BorderLayout());
      String var14 = (String)var2.features.get("btable_jel");
      if (var14 == null) {
         var14 = "";
      }

      var14 = PageViewer.convertbjel(var14);
      String var15 = (String)var2.features.get("mertekegyseg");
      if (var15 == null) {
         var15 = (String)var2.features.get("mask");

         try {
            var15 = var15.split("!", 2)[1];
         } catch (Exception var22) {
            var15 = "";
         }
      }

      JPanel var16 = this.createNorth(var7, var8, var9, var10, var11, var12, var14, var15);
      var13.add(var16, "North");
      JPanel var17 = new JPanel((LayoutManager)null);
      var17.setBorder(BorderFactory.createTitledBorder("Módosítás"));
      JLabel var18 = new JLabel("Módosított érték:");
      var18.setBounds(10, 20, 420, 20);
      var18.setHorizontalAlignment(4);
      var17.add(var18);
      this.tf = new JTextField();
      this.tf.setBounds(440, 20, 100, 20);
      this.tf.setHorizontalAlignment(4);
      this.tf.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent var1) {
            KihatasSimpleDialog.this.tf.setText(MultiLineTable.stripformatnumber(KihatasSimpleDialog.this.tf.getText()));
         }

         public void focusLost(FocusEvent var1) {
            KihatasSimpleDialog.this.tf.setText(MultiLineTable.formatnumber(KihatasSimpleDialog.this.tf.getText(), KihatasSimpleDialog.this.precision));
         }
      });
      var17.add(this.tf);
      var13.add(var17);
      JPanel var19 = new JPanel();
      var19.setLayout(new BoxLayout(var19, 2));
      var19.setBorder(new EmptyBorder(5, 5, 5, 5));
      JButton var20 = new JButton("Mégsem");
      JButton var21 = new JButton("Ok");
      var19.add(Box.createHorizontalGlue());
      var19.add(var20);
      var19.add(Box.createHorizontalStrut(10));
      var19.add(var21);
      var20.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            KihatasSimpleDialog.this.done_cancel();
         }
      });
      var21.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            KihatasSimpleDialog.this.done_ok();
         }
      });
      var13.add(var19, "South");
      var13.setBorder(new EmptyBorder(5, 5, 5, 5));
      this.getContentPane().add(var13);
      this.setDefaultCloseOperation(0);
      this.setSize(580, 325);
      this.setLocationRelativeTo(MainFrame.thisinstance);
      this.setVisible(true);
   }

   private JPanel createNorth(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8) {
      JPanel var9 = new JPanel((LayoutManager)null);
      var9.setBorder(BorderFactory.createTitledBorder("Bizonylat cella adatok"));
      byte var10 = 10;
      byte var11 = 20;
      byte var12 = 120;
      byte var13 = 20;
      byte var14 = 10;
      byte var15 = 10;
      JLabel var16 = new JLabel("Megnevezés:");
      var16.setBounds(var10, var11, var12, var13);
      var16.setHorizontalAlignment(4);
      var9.add(var16);
      JTextField var17 = new JTextField(var1);
      var17.setEditable(false);
      var17.setBounds(var10 + var12 + var14, var11, 400, var13);
      var9.add(var17);
      var16 = new JLabel("Mezőinformációk:");
      var16.setBounds(var10, var11 + var13 + var15, var12, var13);
      var16.setHorizontalAlignment(4);
      var9.add(var16);
      var17 = new JTextField(var2);
      var17.setEditable(false);
      var17.setBounds(var10 + var12 + var14, var11 + var13 + var15, 400, var13);
      var9.add(var17);
      var16 = new JLabel("Adónem:");
      var16.setBounds(var10, var11 + 4 * (var13 + var15), var12, var13);
      var16.setHorizontalAlignment(4);
      var9.add(var16);
      var17 = new JTextField(var3);
      var17.setEditable(false);
      var17.setBounds(var10 + var12 + var14, var11 + 4 * (var13 + var15), 150, var13);
      var9.add(var17);
      var16 = new JLabel("Jelleg:");
      var16.setBounds(var10, var11 + 2 * (var13 + var15), var12, var13);
      var16.setHorizontalAlignment(4);
      var9.add(var16);
      var17 = new JTextField(var7);
      var17.setEditable(false);
      var17.setBounds(var10 + var12 + var14, var11 + 2 * (var13 + var15), 150, var13);
      var9.add(var17);
      var16 = new JLabel("Mértékegység:");
      var16.setBounds(var10, var11 + 3 * (var13 + var15), var12, var13);
      var16.setHorizontalAlignment(4);
      var9.add(var16);
      var17 = new JTextField(var8);
      var17.setEditable(false);
      var17.setBounds(var10 + var12 + var14, var11 + 3 * (var13 + var15), 150, var13);
      var9.add(var17);
      int var18 = var10 + 300;
      var16 = new JLabel("Adózói érték:");
      var16.setBounds(var18, var11 + 2 * (var13 + var15), var12, var13);
      var16.setHorizontalAlignment(4);
      var9.add(var16);
      var17 = new JTextField(MultiLineTable.formatnumber(var4, this.precision));
      var17.setEditable(false);
      var17.setHorizontalAlignment(4);
      var17.setBounds(var18 + var12 + var14, var11 + 2 * (var13 + var15), 100, var13);
      var9.add(var17);
      var16 = new JLabel("Adóügyi érték:");
      var16.setBounds(var18, var11 + 3 * (var13 + var15), var12, var13);
      var16.setHorizontalAlignment(4);
      var9.add(var16);
      var17 = new JTextField(MultiLineTable.formatnumber(var5, this.precision));
      var17.setEditable(false);
      var17.setHorizontalAlignment(4);
      var17.setBounds(var18 + var12 + var14, var11 + 3 * (var13 + var15), 100, var13);
      var9.add(var17);
      var16 = new JLabel("Aktuális érték:");
      var16.setBounds(var18, var11 + 4 * (var13 + var15), var12, var13);
      var16.setHorizontalAlignment(4);
      var9.add(var16);
      var17 = new JTextField(MultiLineTable.formatnumber(var6, this.precision));
      var17.setEditable(false);
      var17.setHorizontalAlignment(4);
      var17.setBounds(var18 + var12 + var14, var11 + 4 * (var13 + var15), 100, var13);
      var9.add(var17);
      var9.setBounds(0, 0, 500, 190);
      var9.setPreferredSize(var9.getSize());
      return var9;
   }

   private void done_cancel() {
      this.setVisible(false);
   }

   private void done_ok() {
      int var1 = Integer.parseInt(MultiLineTable.stripformatnumber(this.tf.getText()));
      if (var1 < 0 && !DataChecker.getInstance().lehetEMinusz((String)this.df.features.get("irids"))) {
         JOptionPane.showMessageDialog(MainFrame.thisinstance, "Az Aktuális revizori érték nem lehet negatív!", "Hibaüzenet", 0);
      } else {
         String var2 = MultiLineTable.stripformatnumber(this.tf.getText());
         Vector var3 = this.dshist.get(this.kskey);
         String var4 = "0";
         if (var3 == null) {
            var3 = new Vector();
            var3.setSize(4);
            if (var2 != null) {
               var3.set(2, var2);
            }

            this.dshist.set(this.kskey, var3);
         } else {
            try {
               var4 = (String)var3.get(2);
               if (var4 == null || "".equals(var4)) {
                  var4 = (String)var3.get(1);
                  if (var4 == null || "".equals(var4)) {
                     var4 = (String)var3.get(0);
                  }
               }
            } catch (Exception var14) {
            }

            if (var2 != null) {
               var3.set(2, var2);
            }
         }

         this.ds.set(this.kskey, var2);
         if (var4 == null || "".equals(var4)) {
            var4 = "0";
         }

         KihatasRecord var5 = (KihatasRecord)this.dtm.get(0);
         if ("".equals(var5.getEredetiErtek())) {
            var5.setEredetiErtek(var4);
         } else {
            var4 = var5.getEredetiErtek();
         }

         String var6 = this.PV.getPM().getFormModel().id;
         String var7 = this.kskey.split("_")[0];
         String var8 = this.kskey.split("_")[1];
         var5.setFid(var6 + "@" + var8);
         var5.setBmatLapsorszam(var7);
         var5.setCsoportFlag("N");
         var5.setAdonemKod(this.adonem);

         try {
            BigDecimal var9 = new BigDecimal(var4);
            BigDecimal var10 = new BigDecimal(var2);
            BigDecimal var11 = var10.subtract(var9);
            String var12 = var11.toPlainString();
            var5.setModositoOsszeg(var12);
         } catch (Exception var13) {
         }

         this.PV.refresh();
         this.setVisible(false);
      }
   }
}
