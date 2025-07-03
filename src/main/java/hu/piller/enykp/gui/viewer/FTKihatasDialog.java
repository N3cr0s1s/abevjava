package hu.piller.enykp.gui.viewer;

import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.ABEVFunctionSet;
import hu.piller.enykp.alogic.kihatas.MultiLineTable;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.extensions.db.DbFactory;
import hu.piller.enykp.extensions.db.IDbHandler;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.PageModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class FTKihatasDialog extends JDialog {
   private JPanel south;
   JTextField tf_e;
   JTextField tf_ar;
   JTextField tf_ad;
   JTextField tf_a;
   String bjel_sablon;
   String me_sablon;
   JButton delbtn;
   JButton okbtn;
   JButton cancelbtn;

   public JPanel getSouth() {
      return this.south;
   }

   public JTextField getTf_e() {
      return this.tf_e;
   }

   public JTextField getTf_ar() {
      return this.tf_ar;
   }

   public JTextField getTf_ad() {
      return this.tf_ad;
   }

   public JTextField getTf_a() {
      return this.tf_a;
   }

   public String getBjel_sablon() {
      return this.bjel_sablon;
   }

   public String getMe_sablon() {
      return this.me_sablon;
   }

   public JButton getDelbtn() {
      return this.delbtn;
   }

   public JButton getOkbtn() {
      return this.okbtn;
   }

   public JButton getCancelbtn() {
      return this.cancelbtn;
   }

   public FTKihatasDialog(PageModel var1, PageViewer var2, DataFieldModel var3, int var4, String var5, Vector var6, String var7, String var8, JTable var9) {
      super(MainFrame.thisinstance, "Revizori módosító tétel (kihatás) rögzítő", true);
      JPanel var10 = new JPanel(new GridLayout(6, 1, 0, 3));
      var10.setBorder(BorderFactory.createTitledBorder("Bizonylat cella adatok"));
      JPanel var11 = new JPanel();
      var11.setLayout(new BoxLayout(var11, 2));
      var11.add(new JLabel("Mezőinformációk:"));
      var11.add(Box.createHorizontalStrut(10));
      JTextField var12 = new JTextField();
      var12.setEditable(false);
      String var13 = MetaInfo.extendedInfoTxt(var3.key, var4 > -1 ? new Integer(var4) : null, var1.getFormModel().id, var1.getFormModel().bm);
      var13 = var13.substring(17, var13.length() - 1);
      if (0 < var4) {
         var13 = var13 + " Dinamikus lap száma:" + var4;
      }

      var12.setText(var13);
      var12.setCaretPosition(0);
      if (60 < var13.length()) {
         var12.setToolTipText(var13);
      }

      var11.add(var12);
      JPanel var14 = new JPanel();
      var14.setLayout(new BoxLayout(var14, 2));
      var14.add(Box.createHorizontalStrut(26));
      var14.add(new JLabel("Megnevezés:"));
      var14.add(Box.createHorizontalStrut(10));
      var12 = new JTextField();
      var12.setEditable(false);
      IDbHandler var15 = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.DefaultDbHandler");
      var12.setText(var15.getPrompt(var5));
      var12.setCaretPosition(0);
      var12.setToolTipText(var12.getText());
      var14.add(var12);
      var10.add(var14);
      var10.add(var11);
      JPanel var16 = new JPanel();
      var16.setLayout(new BoxLayout(var16, 2));
      var16.add(Box.createHorizontalGlue());
      var16.add(new JLabel("Jelleg:"));
      var16.add(Box.createHorizontalStrut(10));
      JTextField var17 = new JTextField(15);
      var17.setMaximumSize(new Dimension(250, 25));
      var17.setMinimumSize(new Dimension(100, 25));
      var17.setPreferredSize(new Dimension(210, 25));
      var17.setEditable(false);
      var17.setHorizontalAlignment(4);
      String var18 = (String)var3.features.get("btable_jel");
      if (var18 == null) {
         var18 = "";
      }

      this.bjel_sablon = var18;
      var18 = PageViewer.convertbjel(var18);
      var17.setText(var18);
      var16.add(var17);
      var10.add(var16);
      JPanel var19 = new JPanel();
      var19.setLayout(new BoxLayout(var19, 2));
      var19.add(Box.createHorizontalGlue());
      var19.add(new JLabel("Mértékegység:"));
      var19.add(Box.createHorizontalStrut(10));
      JTextField var20 = new JTextField(15);
      var20.setMaximumSize(new Dimension(150, 25));
      var20.setMinimumSize(new Dimension(100, 25));
      var20.setPreferredSize(new Dimension(110, 25));
      var20.setEditable(false);
      var20.setHorizontalAlignment(4);
      String var21 = (String)var3.features.get("mertekegyseg");
      if (var21 == null) {
         var21 = (String)var3.features.get("mask");

         try {
            var21 = var21.split("!", 2)[1];
         } catch (Exception var32) {
            var21 = "";
         }
      }

      this.me_sablon = var21;
      var20.setText(var21);
      var19.add(var20);
      var10.add(var19);
      JPanel var22 = new JPanel();
      var22.setLayout(new BoxLayout(var22, 2));
      var22.add(Box.createHorizontalGlue());
      var22.add(new JLabel("Adózói érték:"));
      var22.add(Box.createHorizontalStrut(10));
      this.tf_ad = new JTextField(15);
      this.tf_ad.setMaximumSize(new Dimension(150, 25));
      this.tf_ad.setMinimumSize(new Dimension(100, 25));
      this.tf_ad.setPreferredSize(new Dimension(110, 25));
      this.tf_ad.setEditable(false);
      this.tf_ad.setHorizontalAlignment(4);
      int var23 = ABEVFunctionSet.getInstance().getPrecisionForKihatas(var3.key);
      this.tf_ad.setText(MultiLineTable.formatnumber(var2.GETVELEM(var6, 0), var23));
      var22.add(this.tf_ad);
      var10.add(var22);
      JPanel var24 = new JPanel();
      var24.setLayout(new BoxLayout(var24, 2));
      var24.add(Box.createHorizontalGlue());
      var24.add(new JLabel("Adóügyi érték:"));
      var24.add(Box.createHorizontalStrut(10));
      this.tf_a = new JTextField(15);
      this.tf_a.setMaximumSize(new Dimension(150, 25));
      this.tf_a.setMinimumSize(new Dimension(100, 25));
      this.tf_a.setPreferredSize(new Dimension(110, 25));
      this.tf_a.setEditable(false);
      this.tf_a.setHorizontalAlignment(4);
      this.tf_a.setText(MultiLineTable.formatnumber(var2.GETVELEM(var6, 1), var23));
      var24.add(this.tf_a);
      var10.add(var24);
      JPanel var25 = new JPanel(new BorderLayout());
      var25.setBorder(BorderFactory.createTitledBorder("Bizonylat cellára vonatkozó kihatások"));
      JPanel var26 = new JPanel();
      ((FlowLayout)var26.getLayout()).setAlignment(2);
      var26.add(Box.createHorizontalGlue());
      JLabel var27 = new JLabel("Nyomtatvány adónem: ");
      var26.add(var27);
      JTextField var28 = new JTextField(10);
      var28.setEditable(false);
      var28.setText(var7);
      var28.setHorizontalAlignment(2);
      var26.add(var28);
      var26.add(new JLabel(" Eredeti érték:"));
      this.tf_e = new JTextField(15);
      this.tf_e.setEditable(false);
      this.tf_e.setHorizontalAlignment(4);
      var26.add(this.tf_e);
      var25.add(var26, "North");
      JPanel var29 = new JPanel();
      ((FlowLayout)var29.getLayout()).setAlignment(2);
      var29.add(Box.createHorizontalGlue());
      var29.add(new JLabel("Aktuális revizori érték:"));
      this.tf_ar = new JTextField(15);
      this.tf_ar.setText(MultiLineTable.formatnumber(var8, var23));
      this.tf_ar.setEditable(false);
      this.tf_ar.setHorizontalAlignment(4);
      var29.add(this.tf_ar);
      var25.add(var29, "South");
      JScrollPane var30 = new JScrollPane(var9);
      var25.add(var30);
      this.south = new JPanel();
      this.south.setLayout(new BoxLayout(this.south, 2));
      this.south.setBorder(new EmptyBorder(5, 5, 5, 5));
      this.delbtn = new JButton("Törlés");
      this.south.add(this.delbtn);
      this.south.add(Box.createHorizontalGlue());
      this.okbtn = new JButton("Ok");
      this.south.add(this.okbtn);
      this.south.add(Box.createHorizontalStrut(10));
      this.cancelbtn = new JButton("Mégsem");
      this.south.add(this.cancelbtn);
      JPanel var31 = new JPanel(new BorderLayout());
      var31.add(var10, "North");
      var31.add(var25, "Center");
      var31.add(this.south, "South");
      this.getContentPane().add(var31);
      this.setDefaultCloseOperation(0);
      this.setSize(650, 450);
      this.setLocationRelativeTo(MainFrame.thisinstance);
   }
}
