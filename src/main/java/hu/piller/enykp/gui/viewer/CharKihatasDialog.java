package hu.piller.enykp.gui.viewer;

import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.ABEVFunctionSet;
import hu.piller.enykp.alogic.kihatas.MegallapitasComboLista;
import hu.piller.enykp.alogic.kihatas.MegallapitasComboRecord;
import hu.piller.enykp.alogic.kihatas.MultiLineTable;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.extensions.db.DbFactory;
import hu.piller.enykp.extensions.db.IDbHandler;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.PageModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class CharKihatasDialog extends JDialog {
   private JTextField tf_ar;
   private JButton okbtn;
   private JButton cancelbtn;
   private JPanel south;
   String bjel_sablon;
   String me_sablon;
   JComboBox megaC;
   JComboBox adonemC;

   public JTextField getTf_ar() {
      return this.tf_ar;
   }

   public JButton getOkbtn() {
      return this.okbtn;
   }

   public JButton getCancelbtn() {
      return this.cancelbtn;
   }

   public String getMsvo() {
      if (this.megaC.getSelectedIndex() < 0) {
         return null;
      } else {
         MegallapitasComboRecord var1 = (MegallapitasComboRecord)this.megaC.getSelectedItem();
         return var1.getMsvo_azon();
      }
   }

   public String getAdonemKod() {
      return this.adonemC.getSelectedIndex() < 0 ? null : this.adonemC.getSelectedItem().toString();
   }

   public CharKihatasDialog(PageModel var1, PageViewer var2, DataFieldModel var3, int var4, String var5, Vector var6, String var7, MegallapitasComboLista var8, String var9, String var10) {
      super(MainFrame.thisinstance, "Revizori módosító tétel (kihatás) rögzítő", true);
      this.setSize(650, 420);
      JPanel var11 = new JPanel(new GridLayout(6, 1, 0, 3));
      var11.setBorder(BorderFactory.createTitledBorder("Bizonylat cella adatok"));
      JPanel var12 = new JPanel();
      var12.setLayout(new BoxLayout(var12, 2));
      var12.add(new JLabel("Mezőinformációk:"));
      var12.add(Box.createHorizontalStrut(10));
      JTextField var13 = new JTextField();
      var13.setEditable(false);
      String var14 = MetaInfo.extendedInfoTxt(var3.key, var4 > -1 ? new Integer(var4) : null, var1.getFormModel().id, var1.getFormModel().bm);
      var14 = var14.substring(17, var14.length() - 1);
      if (0 < var4) {
         var14 = var14 + " Dinamikus lap száma:" + var4;
      }

      var13.setText(var14);
      var13.setCaretPosition(0);
      if (60 < var14.length()) {
         var13.setToolTipText(var14);
      }

      var12.add(var13);
      JPanel var15 = new JPanel();
      var15.setLayout(new BoxLayout(var15, 2));
      var15.add(Box.createHorizontalStrut(26));
      var15.add(new JLabel("Megnevezés:"));
      var15.add(Box.createHorizontalStrut(10));
      var13 = new JTextField();
      var13.setEditable(false);
      IDbHandler var16 = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.DefaultDbHandler");
      var13.setText(var16.getPrompt(var5));
      var13.setCaretPosition(0);
      var13.setToolTipText(var13.getText());
      var15.add(var13);
      var11.add(var15);
      var11.add(var12);
      JPanel var17 = new JPanel();
      var17.setLayout(new BoxLayout(var17, 2));
      var17.add(Box.createHorizontalGlue());
      var17.add(new JLabel("Jelleg:"));
      var17.add(Box.createHorizontalStrut(10));
      JTextField var18 = new JTextField(15);
      var18.setMaximumSize(new Dimension(250, 25));
      var18.setMinimumSize(new Dimension(100, 25));
      var18.setPreferredSize(new Dimension(210, 25));
      var18.setEditable(false);
      var18.setHorizontalAlignment(4);
      String var19 = (String)var3.features.get("btable_jel");
      if (var19 == null) {
         var19 = "";
      }

      this.bjel_sablon = var19;
      var19 = PageViewer.convertbjel(var19);
      var18.setText(var19);
      var17.add(var18);
      var11.add(var17);
      JPanel var20 = new JPanel();
      var20.setLayout(new BoxLayout(var20, 2));
      var20.add(Box.createHorizontalGlue());
      var20.add(new JLabel("Mértékegység:"));
      var20.add(Box.createHorizontalStrut(10));
      JTextField var21 = new JTextField(15);
      var21.setMaximumSize(new Dimension(150, 25));
      var21.setMinimumSize(new Dimension(100, 25));
      var21.setPreferredSize(new Dimension(110, 25));
      var21.setEditable(false);
      var21.setHorizontalAlignment(4);
      String var22 = (String)var3.features.get("mertekegyseg");
      if (var22 == null) {
         var22 = (String)var3.features.get("mask");

         try {
            var22 = var22.split("!", 2)[1];
         } catch (Exception var45) {
            var22 = "";
         }
      }

      this.me_sablon = var22;
      var21.setText(var22);
      var20.add(var21);
      var11.add(var20);
      JPanel var23 = new JPanel();
      var23.setLayout(new BoxLayout(var23, 2));
      var23.add(Box.createHorizontalGlue());
      var23.add(new JLabel("Adózói érték:"));
      var23.add(Box.createHorizontalStrut(10));
      JTextField var24 = new JTextField(15);
      var24.setMaximumSize(new Dimension(150, 25));
      var24.setMinimumSize(new Dimension(100, 25));
      var24.setPreferredSize(new Dimension(110, 25));
      var24.setEditable(false);
      var24.setHorizontalAlignment(4);
      int var25 = ABEVFunctionSet.getInstance().getPrecisionForKihatas(var3.key);
      var24.setText(MultiLineTable.formatnumber(var2.GETVELEM(var6, 0), var25));
      var23.add(var24);
      var11.add(var23);
      JPanel var26 = new JPanel();
      var26.setLayout(new BoxLayout(var26, 2));
      var26.add(Box.createHorizontalGlue());
      var26.add(new JLabel("Adóügyi érték:"));
      var26.add(Box.createHorizontalStrut(10));
      JTextField var27 = new JTextField(15);
      var27.setMaximumSize(new Dimension(150, 25));
      var27.setMinimumSize(new Dimension(100, 25));
      var27.setPreferredSize(new Dimension(110, 25));
      var27.setEditable(false);
      var27.setHorizontalAlignment(4);
      var27.setText(MultiLineTable.formatnumber(var2.GETVELEM(var6, 1), var25));
      var26.add(var27);
      var11.add(var26);
      JPanel var28 = new JPanel(new BorderLayout());
      var28.setBorder(BorderFactory.createTitledBorder("Bizonylat cellára vonatkozó kihatások"));
      Dimension var29 = new Dimension(130, 25);
      JPanel var30 = new JPanel();
      ((FlowLayout)var30.getLayout()).setAlignment(2);
      JLabel var31 = new JLabel("Nyomtatvány adónem: ");
      var31.setPreferredSize(var29);
      var31.setMinimumSize(var29);
      var31.setMaximumSize(var29);
      var31.setHorizontalAlignment(4);
      var30.add(var31);
      var30.add(Box.createHorizontalGlue());
      JTextField var32 = new JTextField(10);
      Dimension var33 = new Dimension(150, 25);
      var32.setPreferredSize(var33);
      var32.setMaximumSize(var33);
      var32.setMaximumSize(var33);
      var32.setEditable(false);
      var32.setText(var9);
      var32.setHorizontalAlignment(2);
      var30.add(var32);
      var30.add(Box.createHorizontalStrut(35));
      JLabel var34 = new JLabel("Eredeti érték:");
      var34.setMinimumSize(var29);
      var34.setMaximumSize(var29);
      var34.setPreferredSize(var29);
      var34.setHorizontalAlignment(4);
      var30.add(var34);
      var30.add(Box.createHorizontalGlue());
      JTextField var35 = new JTextField(15);
      var35.setEditable(false);
      var35.setHorizontalAlignment(2);
      var35.setPreferredSize(var33);
      var35.setMaximumSize(var33);
      var35.setMaximumSize(var33);
      var35.setText(var10);
      var30.add(var35);
      var28.add(var30, "North");
      JPanel var36 = new JPanel(new GridLayout(2, 1, 5, 5));
      JPanel var37 = new JPanel();
      ((FlowLayout)var37.getLayout()).setAlignment(2);
      JLabel var38 = new JLabel("Megállapítás:");
      var38.setPreferredSize(var29);
      var38.setMaximumSize(var29);
      var38.setMinimumSize(var29);
      var38.setHorizontalAlignment(4);
      var37.add(var38);
      var37.add(Box.createHorizontalGlue());
      DefaultComboBoxModel var39 = new DefaultComboBoxModel(var8);
      this.megaC = new JComboBox(var39);
      this.megaC.setEditable(false);
      this.megaC.setRenderer(new DefaultListCellRenderer() {
         public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
            String var6 = "";
            if (var2 instanceof MegallapitasComboRecord) {
               var6 = ((MegallapitasComboRecord)var2).getDisplayText();
            }

            return super.getListCellRendererComponent(var1, var6, var3, var4, var5);
         }
      });
      int var40 = (int)((double)this.getWidth() - var29.getWidth() - 50.0D);
      this.megaC.setMaximumSize(new Dimension(var40, 22));
      this.megaC.setMinimumSize(new Dimension(var40, 22));
      this.megaC.setPreferredSize(new Dimension(var40, 22));
      var37.add(this.megaC);
      var36.add(var37);
      JPanel var41 = new JPanel();
      ((FlowLayout)var41.getLayout()).setAlignment(2);
      JLabel var42 = new JLabel("Adónem:");
      var42.setPreferredSize(var29);
      var42.setMaximumSize(var29);
      var42.setMinimumSize(var29);
      var42.setHorizontalAlignment(4);
      var41.add(var42);
      var41.add(Box.createHorizontalGlue());
      this.adonemC = new JComboBox();
      var40 = (int)((double)this.getWidth() - var29.getWidth() - 50.0D - 200.0D - 5.0D);
      this.adonemC.setMaximumSize(new Dimension(var40, 22));
      this.adonemC.setMinimumSize(new Dimension(var40, 22));
      this.adonemC.setPreferredSize(new Dimension(var40, 22));
      var41.add(this.adonemC);
      var41.add(Box.createHorizontalStrut(200));
      var36.add(var41);
      this.megaC.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent var1) {
            if (var1.getSource() == CharKihatasDialog.this.megaC) {
               CharKihatasDialog.this.fillAdonemCombo();
            }

         }
      });
      this.fillAdonemCombo();
      var28.add(var36, "Center");
      JPanel var43 = new JPanel();
      ((FlowLayout)var43.getLayout()).setAlignment(2);
      var43.add(Box.createHorizontalGlue());
      var43.add(new JLabel("Aktuális revizori érték:"));
      this.tf_ar = new JTextField(15);
      this.tf_ar.setText(MultiLineTable.formatnumber(var7, var25));
      this.tf_ar.setEditable(false);
      this.tf_ar.setHorizontalAlignment(4);
      var43.add(this.tf_ar);
      var28.add(var43, "South");
      var28.add(var36);
      this.south = new JPanel();
      this.south.setLayout(new BoxLayout(this.south, 2));
      this.south.setBorder(new EmptyBorder(5, 5, 5, 5));
      this.okbtn = new JButton("Ok");
      this.south.add(this.okbtn);
      this.south.add(Box.createHorizontalStrut(10));
      this.cancelbtn = new JButton("Mégsem");
      this.south.add(this.cancelbtn);
      JPanel var44 = new JPanel(new BorderLayout());
      var44.add(var11, "North");
      var44.add(var28, "Center");
      var44.add(this.south, "South");
      this.getContentPane().add(var44);
      this.setDefaultCloseOperation(0);
      this.setLocationRelativeTo(MainFrame.thisinstance);
   }

   private void fillAdonemCombo() {
      MegallapitasComboRecord var1 = (MegallapitasComboRecord)this.megaC.getSelectedItem();
      this.adonemC.setModel(new DefaultComboBoxModel(var1.getAdonemlista()));
      this.adonemC.setSelectedIndex(0);
   }
}
