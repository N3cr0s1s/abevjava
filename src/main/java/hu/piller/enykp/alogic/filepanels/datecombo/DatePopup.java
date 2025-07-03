package hu.piller.enykp.alogic.filepanels.datecombo;

import hu.piller.enykp.util.base.Tools;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class DatePopup extends JPopupMenu implements ActionListener, MouseListener, ItemListener, ChangeListener {
   private static final long serialVersionUID = 1L;
   private static final int LEFTMARGIN = 5;
   private Calendar kezdodatum = null;
   private Calendar vegedatum = null;
   private JSpinner evek = new JSpinner();
   private SpinnerNumberModel snm = new SpinnerNumberModel();
   private JSpinner honapok = new JSpinner();
   private SpinnerListModel slm = new SpinnerListModel(new String[]{"január", "február", "március", "április", "május", "június", "július", "augusztus", "szeptember", "október", "november", "december"});
   private DatePopup.ReadonlyTable napok;
   private JButton ok = new JButton("Rendben");
   private int minyear = 1995;
   private int maxyear = 2050;
   private int maiSor;
   private int maiOszlop;
   private int maiNap;
   private int kod;
   private Calendar most = Calendar.getInstance();
   private Calendar elseje = Calendar.getInstance();
   private String aktualisNap;
   private String kivalasztottDatum;
   boolean kellEllenorzes = true;
   private Hashtable actionListeners = new Hashtable();
   private JComponent szulo;

   public void setKezdodatum(Calendar var1) {
      this.kezdodatum = var1;
      if (var1 != null) {
         this.snm.setMinimum(new Integer(var1.get(1)));
         this.evek.setModel(this.snm);
      }

   }

   public void setVegedatum(Calendar var1) {
      this.vegedatum = var1;
      if (var1 != null) {
         this.snm.setMaximum(new Integer(var1.get(1)));
         this.evek.setModel(this.snm);
      }

   }

   public void setDatum(Calendar var1) {
      if (var1 == null) {
         var1 = this.most;
      }

      if (this.kezdodatum == null || !var1.before(this.kezdodatum)) {
         if (this.vegedatum == null || !var1.after(this.vegedatum)) {
            this.evek.setValue(new Integer(var1.get(1)));
            this.setHonapSzam(var1.get(2));
            this.maiNap = var1.get(5);
            this.napokTablaFrissitese(true);
         }
      }
   }

   public DatePopup(JComponent var1) {
      this.szulo = var1;
      this.elseje.set(5, 1);
      this.initDialog();
      this.napokTablaFrissitese(true);
   }

   private void initDialog() {
      this.setBorderPainted(false);
      this.maiNap = this.most.get(5);
      this.snm.setValue(new Integer(this.most.get(1)));
      this.snm.setStepSize(new Integer(1));
      if (this.kezdodatum != null) {
         this.snm.setMinimum(new Integer(this.kezdodatum.get(1)));
      } else {
         this.snm.setMinimum(new Integer(this.minyear));
      }

      if (this.vegedatum != null) {
         this.snm.setMaximum(new Integer(this.vegedatum.get(1)));
      } else {
         this.snm.setMaximum(new Integer(this.maxyear));
      }

      this.evek.setModel(this.snm);
      this.evek.addChangeListener(this);
      Point var1 = new Point(this.szulo.getX(), this.szulo.getY());
      SwingUtilities.convertPointToScreen(var1, this.szulo);
      this.setBounds((int)var1.getX(), (int)var1.getY(), 210, 205);
      JPanel var2 = new JPanel();
      var2.setPreferredSize(new Dimension(210, 205));
      var2.setLayout((LayoutManager)null);
      Border var3 = BorderFactory.createEtchedBorder();
      TitledBorder var4 = BorderFactory.createTitledBorder(var3, "Dátum");
      var2.setBorder(var4);
      JPanel var5 = new JPanel();
      var5.setLayout((LayoutManager)null);
      String[] var6 = new String[]{"H", "K", "Sze", "Cs", "P", "Szo", "V"};
      String[][] var7 = new String[][]{{"", "", "", "", "", "", ""}, {"", "", "", "", "", "", ""}, {"", "", "", "", "", "", ""}, {"", "", "", "", "", "", ""}, {"", "", "", "", "", "", ""}, {"", "", "", "", "", "", ""}};
      DefaultTableModel var8 = new DefaultTableModel(var7, var6);
      this.napok = new DatePopup.ReadonlyTable(var8);
      this.napok.getSelectionModel().setSelectionMode(0);
      this.napok.getColumnModel().setColumnSelectionAllowed(false);
      this.napok.setRowSelectionAllowed(false);
      this.napok.setCellSelectionEnabled(true);
      this.napok.getTableHeader().setReorderingAllowed(false);
      this.napok.getTableHeader().setResizingAllowed(false);
      this.napok.addMouseListener(this);
      this.napok.setShowGrid(false);
      this.evek.setBounds(0, 0, 80, 20);
      this.honapok.setBounds(100, 0, 100, 20);
      this.honapok.setModel(this.slm);
      this.honapok.addChangeListener(this);
      this.kellEllenorzes = false;
      this.setHonapSzam(this.most.get(2));
      this.kellEllenorzes = true;
      var5.add(this.evek);
      var5.add(this.honapok);
      var5.setBounds(5, 20, 200, 25);
      var2.add(var5);
      JScrollPane var9 = new JScrollPane(this.napok);
      JPanel var10 = new JPanel();
      var10.setLayout(new BorderLayout());
      var10.add(var9);
      var10.setBounds(5, 55, 200, 118);
      var2.add(var10);
      JPanel var11 = new JPanel();
      var11.setLayout(new BorderLayout());
      this.ok.addActionListener(this);
      var11.add(this.ok);
      var11.setBounds(5, 180, 200, 20);
      var2.add(var11);
      this.add(var2);
      this.pack();
   }

   public void showDateDialog() {
      this.kezdoNapBeallitas();
   }

   private void napokTablaFrissitese(boolean var1) {
      this.elseje.set(1, (Integer)this.evek.getValue());
      this.elseje.set(2, this.getHonapSzam());
      this.elseje.set(5, 1);
      boolean var2 = false;
      int var3 = this.elseje.get(7) - 1;
      if (var3 == 0) {
         var3 = 7;
      }

      int var4 = this.elseje.getActualMaximum(5);

      for(int var5 = 0; var5 < 6; ++var5) {
         for(int var6 = 0; var6 < 7; ++var6) {
            int var7 = var5 * 7 + var6 + 1;
            if (var7 >= var3 && var7 - var3 + 1 <= var4) {
               if (var7 - var3 + 1 == this.maiNap) {
                  this.maiSor = var5;
                  this.maiOszlop = var6;
               }

               this.napok.setValueAt((new Integer(var7 - var3 + 1)).toString(), var5, var6);
            } else {
               this.napok.setValueAt("", var5, var6);
            }
         }
      }

      this.kivalasztottDatum = "";
      this.napok.removeColumnSelectionInterval(0, 6);
   }

   private void kezdoNapBeallitas() {
      this.napok.changeSelection(this.maiSor, this.maiOszlop, false, false);
      this.aktualisNap = (String)this.napok.getValueAt(this.maiSor, this.maiOszlop);
      this.kivalasztottDatumBeallitas();
   }

   private void kivalasztottDatumBeallitas() {
      try {
         this.kivalasztottDatum = ((Integer)this.evek.getValue()).toString() + (this.getHonapSzam() + 1 < 10 ? "0" + (this.getHonapSzam() + 1) : "" + (this.getHonapSzam() + 1)) + (Integer.parseInt(this.aktualisNap) < 10 ? "0" + this.aktualisNap : "" + this.aktualisNap);
      } catch (Exception var2) {
         this.kivalasztottDatum = "";
      }

   }

   public void mouseClicked(MouseEvent var1) {
      if (var1.getSource() instanceof DatePopup.ReadonlyTable) {
         this.aktualisNap = this.kivalsztottElemATablaban();
         this.kivalasztottDatumBeallitas();
         if (var1.getClickCount() == 2) {
            this.fireDateChangedAction();
         }
      }

   }

   public void mouseEntered(MouseEvent var1) {
   }

   public void mouseExited(MouseEvent var1) {
   }

   public void mousePressed(MouseEvent var1) {
   }

   public void mouseReleased(MouseEvent var1) {
   }

   public void itemStateChanged(ItemEvent var1) {
      if (var1.getSource() instanceof JComboBox) {
         if (this.kellEllenorzes) {
            this.kod = this.honapEllenorzes();
         }

         this.napokTablaFrissitese(false);
      }

   }

   public void stateChanged(ChangeEvent var1) {
      if (var1.getSource() == this.evek) {
         this.kod = this.honapEllenorzes();
         if (this.kod != 0) {
            this.honapKijeloleseAComboban();
         }

         this.napokTablaFrissitese(false);
      }

      if (var1.getSource() == this.honapok) {
         if (this.kellEllenorzes) {
            this.kod = this.honapEllenorzes();
         }

         this.napokTablaFrissitese(false);
      }

   }

   public void actionPerformed(ActionEvent var1) {
      if (var1.getSource() == this.ok) {
         this.fireDateChangedAction();
      }

   }

   public void addActionListener(ActionListener var1) {
      this.actionListeners.put(var1, new Integer(0));
   }

   public void removeActionListener(ActionListener var1) {
      this.actionListeners.remove(var1);
   }

   private void fireDateChangedAction() {
      if (this.actionListeners.size() == 0) {
         if (this.isShowing()) {
            this.setVisible(false);
         }

      } else {
         Enumeration var1 = this.actionListeners.keys();

         while(var1.hasMoreElements()) {
            ((ActionListener)var1.nextElement()).actionPerformed(new ActionEvent(this, 1, "DateChangedTo:" + this.kivalasztottDatum));
         }

         if (this.isShowing()) {
            this.setVisible(false);
         }

      }
   }

   private int honapEllenorzes() {
      byte var1 = 0;
      if (this.kezdodatum != null && this.kezdodatum.get(1) == (Integer)this.evek.getValue() && this.kezdodatum.get(2) > this.getHonapSzam()) {
         this.kellEllenorzes = false;
         this.setHonapSzam(this.kezdodatum.get(2));
         this.kellEllenorzes = true;
         var1 = 1;
      }

      if (var1 == 0 && this.vegedatum != null && this.vegedatum.get(1) == (Integer)this.evek.getValue() && this.vegedatum.get(2) < this.getHonapSzam()) {
         this.kellEllenorzes = false;
         this.setHonapSzam(this.vegedatum.get(2));
         this.kellEllenorzes = true;
         var1 = 2;
      }

      return var1;
   }

   private int napEllenorzes() {
      byte var1 = 0;
      String var2 = null;
      if (this.kezdodatum != null && this.kezdodatum.get(1) == (Integer)this.evek.getValue() && this.kezdodatum.get(2) == this.getHonapSzam()) {
         try {
            var2 = this.kivalsztottElemATablaban();
            if (var2 != null) {
               if (this.kezdodatum.get(5) > Integer.parseInt(var2)) {
                  var1 = 1;
               }
            } else {
               var1 = 1;
            }
         } catch (Exception var5) {
            var1 = 1;
         }
      }

      if (var1 == 0) {
         var2 = null;
         if (this.vegedatum != null && this.vegedatum.get(1) == (Integer)this.evek.getValue() && this.vegedatum.get(2) == this.getHonapSzam()) {
            try {
               var2 = this.kivalsztottElemATablaban();
               if (var2 != null) {
                  if (this.vegedatum.get(5) < Integer.parseInt(var2)) {
                     var1 = 2;
                  }
               } else {
                  var1 = 2;
               }
            } catch (Exception var4) {
               var1 = 2;
            }
         }
      }

      return var1;
   }

   private void honapKijeloleseAComboban() {
      if (this.kezdodatum != null && (Integer)this.evek.getValue() == this.kezdodatum.get(1)) {
         this.setHonapSzam(this.kezdodatum.get(2));
      }

      if (this.vegedatum != null && (Integer)this.evek.getValue() == this.vegedatum.get(1)) {
         this.setHonapSzam(this.vegedatum.get(2));
      }

      this.napokTablaFrissitese(false);
   }

   private void napKijeloleseATablaban(int var1) {
      int var2 = 0;
      int var3 = 0;

      boolean var4;
      for(var4 = false; var2 < 6 && !var4; ++var2) {
         for(var3 = 0; var3 < 7 && !var4; ++var3) {
            try {
               if (Integer.parseInt((String)this.napok.getValueAt(var2, var3)) == var1) {
                  var4 = true;
               }
            } catch (NumberFormatException var6) {
               Tools.eLog(var6, 0);
            }
         }
      }

      if (var4) {
         this.kellEllenorzes = false;
         this.napok.changeSelection(var2 - 1, var3 - 1, false, false);
         this.kellEllenorzes = true;
      }

   }

   private String kivalsztottElemATablaban() {
      if (this.napok.getSelectedRow() == -1) {
         return null;
      } else {
         return this.napok.getSelectedColumn() == -1 ? null : (String)this.napok.getValueAt(this.napok.getSelectedRow(), this.napok.getSelectedColumn());
      }
   }

   private int getHonapSzam() {
      return ((SpinnerListModel)this.honapok.getModel()).getList().indexOf(this.honapok.getValue());
   }

   private void setHonapSzam(int var1) {
      this.honapok.setValue(((SpinnerListModel)this.honapok.getModel()).getList().get(var1));
   }

   public void show(Component var1, int var2, int var3) {
      this.showDateDialog();
      super.show(var1, var2, var3);
   }

   public class ReadonlyTable extends JTable {
      public ReadonlyTable(TableModel var2) {
         super(var2);
      }

      public boolean isCellEditable(int var1, int var2) {
         return false;
      }

      public void changeSelection(int var1, int var2, boolean var3, boolean var4) {
         if (!((String)DatePopup.this.napok.getValueAt(var1, var2)).equals("")) {
            super.changeSelection(var1, var2, false, false);
            int var5;
            if (DatePopup.this.kellEllenorzes) {
               var5 = DatePopup.this.napEllenorzes();
            } else {
               var5 = 0;
            }

            if (var5 != 0) {
               Calendar var6 = var5 == 1 ? DatePopup.this.kezdodatum : DatePopup.this.vegedatum;
               DatePopup.this.napKijeloleseATablaban(var6.get(5));
            }
         }

      }
   }
}
