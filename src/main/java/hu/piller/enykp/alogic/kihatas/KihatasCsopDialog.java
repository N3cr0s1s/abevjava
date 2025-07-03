package hu.piller.enykp.alogic.kihatas;

import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.ABEVFunctionSet;
import hu.piller.enykp.alogic.fileutil.DataChecker;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.datastore.Datastore_history;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.Kihatasstore;
import hu.piller.enykp.extensions.db.DbFactory;
import hu.piller.enykp.extensions.db.IDbHandler;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.gui.viewer.PageViewer;
import hu.piller.enykp.interfaces.IDataStore;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class KihatasCsopDialog extends JDialog {
   KihatasTopPanel north;
   MultiLineTable table;
   TableModelListener tableModelListener;
   KihatasTableModel dtm;
   KihatasTableModel copydtm;
   MegallapitasComboLista megallapitaslista;
   Kihatasstore ks;
   String kskey;
   PageViewer PV;
   PageModel PM;
   DataFieldModel df;
   int dynindex;
   boolean nem_listas_kellene;
   JTextField tf_e;
   JTextField tf_ar;
   IDataStore ds;
   Datastore_history dshist;
   int precision;

   public KihatasCsopDialog(PageViewer var1, PageModel var2, DataFieldModel var3, int var4, MegallapitasComboLista var5, boolean var6) {
      super(MainFrame.thisinstance, "Revizori módosító tétel (kihatás) rögzítő", true);
      this.PV = var1;
      this.PM = var2;
      this.df = var3;
      this.dynindex = var4;
      this.megallapitaslista = var5;
      this.nem_listas_kellene = var6;
      this.precision = ABEVFunctionSet.getInstance().getPrecisionForKihatas(this.df.key);
      BookModel var7 = var2.getFormModel().getBookModel();
      Elem var8 = (Elem)var7.cc.getActiveObject();
      this.ds = (IDataStore)var8.getRef();
      this.dshist = (Datastore_history)var8.getEtc().get("history");
      Object[] var9 = (Object[])((Object[])var8.getEtc().get("dbparams"));
      String var10 = null;

      try {
         var10 = (String)var9[3];
      } catch (Exception var23) {
         var10 = null;
      }

      this.kskey = var4 + "_" + this.df.key;
      Vector var11 = this.dshist.get(this.kskey);
      if (var11 == null) {
         var11 = new Vector();
         var11.setSize(4);
      }

      this.ks = (Kihatasstore)var8.getEtc().get("kihatas");
      this.dtm = this.ks.get(this.kskey);
      this.copydtm = KihatasTableModel.make_copy(this.dtm);
      String var12 = MetaInfo.extendedInfoTxt(this.df.key, var2.dynamic ? new Integer(var4) : null, var2.getFormModel().id, var2.getFormModel().bm);
      var12 = var12.substring(17, var12.length() - 1);
      if (0 < var4) {
         var12 = var12 + " Dinamikus lap száma:" + var4;
      }

      IDbHandler var13 = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.DefaultDbHandler");
      String var14 = var13.getPrompt(this.df.key);
      String var15 = this.GETVELEM(var11, 0);
      String var16 = this.GETVELEM(var11, 1);
      String var17 = this.GETVELEM(var11, 2);
      String var18 = this.df.getAdonem(this.ds, var4);
      String var19 = this.dtm.getEredeti(var11);
      JPanel var20 = new JPanel(new BorderLayout());
      this.north = new KihatasTopPanel(var12, var14, var15, var16, this.df);
      JPanel var21 = this.createcenterpanel(var18, var17, var19);
      JPanel var22 = this.createsouthpanel();
      var20.add(this.north, "North");
      var20.add(var21, "Center");
      var20.add(var22, "South");
      this.addWindowListener(new WindowAdapter() {
         public void windowOpened(WindowEvent var1) {
            String var2 = (String)KihatasCsopDialog.this.df.features.get("btable_jel");
            if (var2 == null) {
               var2 = "";
            }

            String var3 = (String)KihatasCsopDialog.this.df.features.get("mertekegyseg");
            if (var3 == null) {
               var3 = (String)KihatasCsopDialog.this.df.features.get("mask");

               try {
                  var3 = var3.split("!", 2)[1];
               } catch (Exception var16) {
                  var3 = "";
               }
            }

            String var4 = KihatasCsopDialog.this.dtm.checkBtablajel(var2);
            String var5 = KihatasCsopDialog.this.dtm.checkMertekegyseg(var3);
            String var6 = var4 + var5;
            String var7 = MultiLineTable.stripformatnumber(KihatasCsopDialog.this.tf_e.getText());

            try {
               new BigDecimal(var7);
            } catch (Exception var15) {
               new BigDecimal(0);
            }

            String var9 = KihatasCsopDialog.this.north.getAdougyiFieldText();

            try {
               new BigDecimal(var9);
            } catch (Exception var14) {
               new BigDecimal(0);
            }

            String var11 = KihatasCsopDialog.this.dtm.ComputeRevValue();
            String var12 = MultiLineTable.stripformatnumber(KihatasCsopDialog.this.tf_ar.getText());
            if (!"".equals(var11) && !PageViewer.equalsIgnoreDecimal(var11, var12)) {
               KihatasCsopDialog.this.tf_ar.setForeground(Color.RED);
               String var13 = "A számított érték (" + var11 + ") és az alap érték (" + var12 + ") eltérést mutat!\nA különbözet automatikusan javításra kerül.";
               JOptionPane.showMessageDialog(MainFrame.thisinstance, var13 + var6, "Figyelmeztetés", 1);
               KihatasCsopDialog.this.tf_ar.setText(MultiLineTable.formatnumber(var11, KihatasCsopDialog.this.precision));
               KihatasCsopDialog.this.tf_ar.setForeground(Color.BLACK);
            } else if (var6.length() != 0) {
               JOptionPane.showMessageDialog(MainFrame.thisinstance, var6, "Figyelmeztetés", 1);
            }

            KihatasCsopDialog.this.dtm.setBtablajel(var2);
            KihatasCsopDialog.this.dtm.setMertekegyseg(var3);
         }
      });
      this.getContentPane().add(var20);
      this.setDefaultCloseOperation(0);
      this.setSize(790, 500);
      this.setLocationRelativeTo(MainFrame.thisinstance);
      this.setVisible(true);
   }

   private String GETVELEM(Vector var1, int var2) {
      try {
         return (String)var1.get(var2);
      } catch (Exception var4) {
         return null;
      }
   }

   private JPanel createsouthpanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new BoxLayout(var1, 2));
      var1.setBorder(new EmptyBorder(5, 5, 5, 5));
      JButton var2 = new JButton("Törlés");
      var1.add(var2);
      var1.add(Box.createHorizontalStrut(10));
      JButton var3 = new JButton("Megállapítás választó");
      var1.add(var3);
      var1.add(Box.createHorizontalGlue());
      JButton var4 = new JButton("Mégsem");
      JButton var5 = new JButton("Ok");
      var1.add(var5);
      var1.add(Box.createHorizontalStrut(10));
      var1.add(var4);
      var2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            KihatasCsopDialog.this.done_del();
         }
      });
      var3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            KihatasCsopDialog.this.done_select();
         }
      });
      var4.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            KihatasCsopDialog.this.done_cancel();
         }
      });
      var5.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            KihatasCsopDialog.this.done_ok();
         }
      });
      var1.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent var1) {
            if (var1.getClickCount() == 2) {
               KihatasCsopDialog.this.done_debug_info();
            }

         }
      });
      return var1;
   }

   private void done_debug_info() {
      JDialog var1 = new JDialog(MainFrame.thisinstance, "Info", true);
      JTable var2 = new JTable(this.dtm);
      var1.getContentPane().add(new JScrollPane(var2));
      var1.setSize(1000, 300);
      var1.setVisible(true);
   }

   private JPanel createcenterpanel(String var1, String var2, String var3) {
      JPanel var4 = new JPanel(new BorderLayout());
      var4.setBorder(BorderFactory.createTitledBorder("Bizonylat cellára vonatkozó kihatások"));
      JPanel var5 = new JPanel();
      ((FlowLayout)var5.getLayout()).setAlignment(2);
      JLabel var6 = new JLabel("Adónem: ");
      var5.add(var6);
      JTextField var7 = new JTextField(40);
      var7.setEditable(false);
      var7.setHorizontalAlignment(2);
      var5.add(var7);
      var7.setText(var1);
      var5.add(new JLabel(" Eredeti érték:"));
      this.tf_e = new JTextField();
      this.tf_e.setMaximumSize(new Dimension(150, 25));
      this.tf_e.setMinimumSize(new Dimension(100, 25));
      this.tf_e.setPreferredSize(new Dimension(110, 25));
      this.tf_e.setEditable(false);
      this.tf_e.setHorizontalAlignment(4);
      this.tf_e.setText(MultiLineTable.formatnumber(var3, this.precision));
      var5.add(this.tf_e);
      var4.add(var5, "North");
      JPanel var8 = new JPanel();
      ((FlowLayout)var8.getLayout()).setAlignment(2);
      var8.add(new JLabel("Aktuális revizori érték:"));
      this.tf_ar = new JTextField();
      this.tf_ar.setMaximumSize(new Dimension(150, 25));
      this.tf_ar.setMinimumSize(new Dimension(100, 25));
      this.tf_ar.setPreferredSize(new Dimension(110, 25));
      this.tf_ar.setText(MultiLineTable.formatnumber(var2, this.precision));
      this.tf_ar.setEditable(false);
      this.tf_ar.setHorizontalAlignment(4);
      var8.add(this.tf_ar);
      var4.add(var8, "South");
      this.table = new MultiLineTable(this.dtm, this.megallapitaslista, this.precision);
      JScrollPane var9 = new JScrollPane(this.table);
      var4.add(var9);
      this.table.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent var1) {
            if (var1.getClickCount() == 2) {
               KihatasCsopDialog.this.done_select();
            }

         }
      });
      this.tableModelListener = new TableModelListener() {
         public void tableChanged(TableModelEvent var1) {
            if (!KihatasCsopDialog.this.dtm.hasEmptyRec()) {
               KihatasCsopDialog.this.dtm.addEmptyRec();
            }

            KihatasCsopDialog.this.tf_ar.setText(MultiLineTable.formatnumber(KihatasCsopDialog.this.dtm.getSzumma(MultiLineTable.stripformatnumber(KihatasCsopDialog.this.tf_e.getText())), KihatasCsopDialog.this.precision));
         }
      };
      this.table.getSelectionModel().setSelectionInterval(this.table.getRowCount() - 1, this.table.getRowCount() - 1);
      this.dtm.addTableModelListener(this.tableModelListener);
      return var4;
   }

   private void done_del() {
      int var1 = this.table.getSelectedRow();
      if (var1 == -1) {
         JOptionPane.showMessageDialog(MainFrame.thisinstance, "Nem választott sort!", "Hibaüzenet", 0);
      } else {
         var1 = this.table.getRowSorter().convertRowIndexToModel(var1);
         this.table.clearSelection();
         this.dtm.done_delete(var1);
         this.tf_ar.setText(MultiLineTable.formatnumber(this.dtm.getSzumma(MultiLineTable.stripformatnumber(this.tf_e.getText())), this.precision));
         this.table.tableChanged(new TableModelEvent(this.dtm));
      }
   }

   private void done_select() {
      int var1 = this.table.getSelectedRow();
      if (var1 == -1) {
         JOptionPane.showMessageDialog(MainFrame.thisinstance, "Nem választott sort!", "Hibaüzenet", 0);
      } else {
         var1 = this.table.getRowSorter().convertRowIndexToModel(var1);
         DefaultTableModel var2 = this.generateDTM(this.dtm, var1);
         new CheckTableDialog(var2);
         if (var2.getValueAt(0, 0) != null) {
            MegallapitasVector var3 = null;

            try {
               var3 = (MegallapitasVector)this.dtm.getValueAt(var1, 1);
            } catch (Exception var9) {
               var3 = new MegallapitasVector();
            }

            MegallapitasVector var4 = new MegallapitasVector();
            this.dtm.setValueAt(var4, var1, 1);

            int var5;
            MegallapitasRecord var6;
            for(var5 = 0; var5 < var2.getRowCount(); ++var5) {
               if ((Boolean)var2.getValueAt(var5, 0)) {
                  var6 = new MegallapitasRecord("", var2.getValueAt(var5, 3).toString(), (String)var2.getValueAt(var5, 2), "");
                  int var7 = this.in(var6, var3);
                  if (var7 != -1) {
                     var6.setKias_azon(((MegallapitasRecord)var3.get(var7)).getKias_azon());
                     var6.setToroltsegjel("M");
                  } else {
                     var6.setToroltsegjel("");
                  }

                  var4.add(var6);
               }
            }

            for(var5 = 0; var5 < var3.size(); ++var5) {
               var6 = (MegallapitasRecord)var3.get(var5);
               if (var6.isDeleted()) {
                  var4.add(var6);
               } else if (!var6.getKias_azon().equals("") && this.deleted(var6, var2)) {
                  var6.setToroltsegjel("T");
                  var4.add(var6);
               }
            }

            String var10 = (String)this.df.features.get("btable_jel");
            if (var10 == null) {
               var10 = "";
            }

            String var11 = (String)this.df.features.get("mertekegyseg");
            if (var11 == null) {
               var11 = (String)this.df.features.get("mask");

               try {
                  var11 = var11.split("!", 2)[1];
               } catch (Exception var8) {
                  var11 = "";
               }
            }

            this.dtm.updateRec(var1, MultiLineTable.stripformatnumber(this.tf_e.getText()), this.df.key, this.dynindex, this.PM.getFormModel().id, "", "C", var10, var11);
            this.dtm.fireTableDataChanged();
         }
      }
   }

   private boolean deleted(MegallapitasRecord var1, DefaultTableModel var2) {
      for(int var3 = 0; var3 < var2.getRowCount(); ++var3) {
         String var4 = (String)var2.getValueAt(var3, 2);
         String var5 = (String)var2.getValueAt(var3, 3);
         if (var5.equals(var1.getMsvo_azon()) && var4.equals(var1.getAdonemkod()) && !(Boolean)var2.getValueAt(var3, 0)) {
            return true;
         }
      }

      return false;
   }

   private int in(MegallapitasRecord var1, Vector var2) {
      for(int var3 = 0; var3 < var2.size(); ++var3) {
         MegallapitasRecord var4 = (MegallapitasRecord)var2.get(var3);
         if (var4.getMsvo_azon().equals(var1.getMsvo_azon()) && var4.getAdonemkod().equals(var1.getAdonemkod())) {
            return var3;
         }
      }

      return -1;
   }

   private void done_cancel() {
      if (this.table.isEditing()) {
         this.table.getCellEditor().stopCellEditing();
      }

      this.ks.set(this.kskey, this.copydtm);
      this.dtm.removeTableModelListener(this.tableModelListener);
      this.setVisible(false);
   }

   private void done_ok() {
      if (this.table.isEditing()) {
         this.table.getCellEditor().stopCellEditing();
      }

      String var1 = this.check_fill();
      if (var1 != null) {
         JOptionPane.showMessageDialog(this, var1, "Hibaüzenet", 0);
      } else {
         this.dtm.setCsopjel("C");
         this.dtm.setAdattipusKod("N");
         this.dtm.setHistory(this.dshist.get(this.kskey));
         if (this.nem_listas_kellene && this.done_nem_listas_kellene()) {
            JOptionPane.showMessageDialog(this, "A megállapítás oszlopban csak 1 elemű lista lehet!", "Hibaüzenet", 0);
         } else {
            double var2 = Double.parseDouble(MultiLineTable.stripformatnumber(this.tf_ar.getText()));
            if (var2 < 0.0D && !DataChecker.getInstance().lehetEMinusz((String)this.df.features.get("irids"))) {
               JOptionPane.showMessageDialog(MainFrame.thisinstance, "Az Aktuális revizori érték nem lehet negatív!", "Hibaüzenet", 0);
            } else {
               this.dtm.removeTableModelListener(this.tableModelListener);
               this.PV.kihatas_vege(MultiLineTable.stripformatnumber(this.tf_ar.getText()), this.ds, this.dshist, this.kskey, this.dtm, true);
               this.setVisible(false);
            }
         }
      }
   }

   private boolean done_nem_listas_kellene() {
      for(int var1 = 0; var1 < this.dtm.getRowCount(); ++var1) {
         MegallapitasVector var2 = ((KihatasRecord)this.dtm.get(var1)).getMegallapitasVector();
         if (1 < var2.nondeletedcount()) {
            return true;
         }
      }

      this.dtm.setCsopjel("N");
      return false;
   }

   private String check_fill() {
      for(int var1 = 0; var1 < this.dtm.getRowCount(); ++var1) {
         BigDecimal var2 = ((KihatasRecord)this.dtm.get(var1)).getModositoOsszegValue2();
         int var3 = ((KihatasRecord)this.dtm.get(var1)).getMegallapitasVector().size();
         if (var3 != 0 && var2 == null) {
            return "Nincs kitöltve a módosító összeg!";
         }
      }

      return null;
   }

   private DefaultTableModel generateDTM(KihatasTableModel var1, int var2) {
      Vector var3 = new Vector();
      var3.add("");
      var3.add("Megállapítás");
      var3.add("Adónem");
      var3.add("msvo");
      DefaultTableModel var4 = new DefaultTableModel(var3, 0);

      String var8;
      int var10;
      for(int var5 = 0; var5 < this.megallapitaslista.size(); ++var5) {
         MegallapitasComboRecord var6 = (MegallapitasComboRecord)this.megallapitaslista.get(var5);
         String var7 = var6.getDisplayText();
         var8 = var6.getMsvo_azon();
         Vector var9 = var6.getAdonemlista();

         for(var10 = 0; var10 < var9.size(); ++var10) {
            String var11 = (String)var9.get(var10);
            Vector var12 = new Vector();
            var12.add(new Boolean(false));
            var12.add(var7);
            var12.add(var11);
            var12.add(var8);
            var4.addRow(var12);
         }
      }

      MegallapitasVector var14 = null;

      try {
         var14 = (MegallapitasVector)var1.getValueAt(var2, 1);
      } catch (Exception var13) {
         return var4;
      }

      for(int var15 = 0; var15 < var14.size(); ++var15) {
         MegallapitasRecord var16 = (MegallapitasRecord)var14.get(var15);
         var8 = var16.getMsvo_azon();
         String var17 = var16.getAdonemkod();
         if (!var16.isDeleted()) {
            var10 = this.already_in(var8, var17, var4);
            if (var10 != -1) {
               var4.setValueAt(new Boolean(true), var10, 0);
            } else {
               Vector var18 = new Vector();
               var18.add(new Boolean(true));
               var18.add(this.megallapitaslista.getDisplayTextByMsvoAzon(var8));
               var18.add(var17);
               var18.add(var8);
               var4.addRow(var18);
            }
         }
      }

      return var4;
   }

   private int already_in(String var1, String var2, DefaultTableModel var3) {
      for(int var4 = 0; var4 < var3.getRowCount(); ++var4) {
         if (var1.equals(var3.getValueAt(var4, 3)) && var2.equals(var3.getValueAt(var4, 2))) {
            return var4;
         }
      }

      return -1;
   }
}
