package hu.piller.enykp.gui.component;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.util.base.Tools;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class DatumValaszto extends JPopupMenu implements ActionListener, MouseListener, ItemListener, ChangeListener {
   private static final int LEFTMARGIN = 5;
   private Calendar kezdodatum = null;
   private Calendar vegedatum = null;
   private JSpinner evek = new JSpinner();
   private SpinnerNumberModel snm = new SpinnerNumberModel();
   private JSpinner honapok = new JSpinner();
   private SpinnerListModel slm = new SpinnerListModel(new String[]{"január", "február", "március", "április", "május", "június", "július", "augusztus", "szeptember", "október", "november", "december"});
   private DatumValaszto.ReadonlyTable napok;
   private JButton ok = new JButton("Rendben");
   private JPanel evHoPanel = new JPanel((LayoutManager)null);
   private JPanel foPanel = new JPanel();
   private JPanel napPanel = new JPanel();
   private JPanel gombPanel = new JPanel();
   private Border etched = BorderFactory.createEtchedBorder();
   private TitledBorder tb;
   public int minyear;
   public int maxyear;
   public int maiSor;
   public int maiOszlop;
   public int maiNap;
   public int kod;
   private Calendar most;
   private Calendar elseje;
   private String aktualisNap;
   private String kivalasztottDatum;
   boolean kellEllenorzes;
   private Hashtable actionListeners;
   private ENYKDateCombo szulo;
   private JLabel jl;

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

   public DatumValaszto(ENYKDateCombo var1) {
      this.tb = BorderFactory.createTitledBorder(this.etched, "Dátum");
      this.minyear = 1900;
      this.maxyear = 2050;
      this.most = Calendar.getInstance();
      this.elseje = Calendar.getInstance();
      this.kellEllenorzes = true;
      this.actionListeners = new Hashtable();
      this.szulo = var1;
      this.jl = new JLabel();
      Font var2 = this.jl.getFont().deriveFont(0, 12.0F);
      this.jl.setFont(var2);
      this.elseje.set(5, 1);
      this.initDialog();
      this.napokTablaFrissitese(true);
   }

   private void initDialog() {
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
      new JDialog(new JFrame(), "Dátumbeállítás", true);
      Point var1 = new Point(this.szulo.getX(), this.szulo.getY());
      SwingUtilities.convertPointToScreen(var1, this.szulo);
      this.foPanel.setFocusCycleRoot(true);
      this.foPanel.setLayout((LayoutManager)null);
      this.foPanel.setBorder(this.tb);
      this.evHoPanel.setLayout((LayoutManager)null);
      String[] var2 = new String[]{"H", "K", "Sze", "Cs", "P", "Szo", "V"};
      String[][] var3 = new String[][]{{"", "", "", "", "", "", ""}, {"", "", "", "", "", "", ""}, {"", "", "", "", "", "", ""}, {"", "", "", "", "", "", ""}, {"", "", "", "", "", "", ""}, {"", "", "", "", "", "", ""}};
      DefaultTableModel var4 = new DefaultTableModel(var3, var2);
      this.napok = new DatumValaszto.ReadonlyTable(var4);
      this.napok.getSelectionModel().setSelectionMode(0);
      this.napok.getColumnModel().setColumnSelectionAllowed(false);
      this.napok.setRowSelectionAllowed(false);
      this.napok.setCellSelectionEnabled(true);
      this.napok.getTableHeader().setReorderingAllowed(false);
      this.napok.getTableHeader().setResizingAllowed(false);
      this.napok.addMouseListener(this);
      this.napok.setShowGrid(false);
      this.honapok.setModel(this.slm);
      this.honapok.addChangeListener(this);
      this.kellEllenorzes = false;
      this.setHonapSzam(this.most.get(2));
      this.kellEllenorzes = true;
      this.evHoPanel.add(this.evek);
      this.evHoPanel.add(this.honapok);
      this.foPanel.add(this.evHoPanel);
      JScrollPane var5 = new JScrollPane(this.napok);
      this.napPanel.setLayout(new BorderLayout());
      this.napPanel.add(var5, "Center");
      this.foPanel.add(this.napPanel);
      this.gombPanel.setLayout(new BorderLayout());
      this.ok.addActionListener(this);
      this.gombPanel.add(this.ok);
      this.foPanel.add(this.gombPanel);
      this.add(this.foPanel);
      this.handleSizing();
      this.pack();
   }

   public void showDateDialog() {
      this.handleSizing();
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
      int var5 = -1;
      int var6 = -1;

      for(int var7 = 0; var7 < 6; ++var7) {
         for(int var8 = 0; var8 < 7; ++var8) {
            int var9 = var7 * 7 + var8 + 1;
            if (var9 >= var3 && var9 - var3 + 1 <= var4) {
               if (var9 - var3 + 1 == this.maiNap) {
                  this.maiSor = var7;
                  this.maiOszlop = var8;
               }

               this.napok.setValueAt((new Integer(var9 - var3 + 1)).toString(), var7, var8);
            } else {
               this.napok.setValueAt("", var7, var8);
            }

            if (this.aktualisNap != null && this.aktualisNap.equals(this.napok.getValueAt(var7, var8))) {
               var5 = var7;
               var6 = var8;
            }
         }
      }

      this.napok.removeColumnSelectionInterval(0, 6);
      if (var5 > -1 && var6 > -1) {
         this.napok.changeSelection(var5, var6, false, false);
      }

      this.kivalasztottDatumBeallitas();
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
      if (var1.getSource() instanceof DatumValaszto.ReadonlyTable) {
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
            this.szulo.grabFocus();
            this.setVisible(false);
         }

      } else {
         Enumeration var1 = this.actionListeners.keys();

         while(var1.hasMoreElements()) {
            ((ActionListener)var1.nextElement()).actionPerformed(new ActionEvent(this, 1, "DateChangedTo:" + this.kivalasztottDatum));
         }

         if (this.isShowing()) {
            this.szulo.grabFocus();
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

   private int getPositionFromPrevComponent(JComponent var1) {
      return (int)(var1.getBounds().getX() + var1.getBounds().getWidth());
   }

   private double getDVItemHeight() {
      double var1 = (double)this.jl.getFontMetrics(this.jl.getFont()).getHeight();
      return this.szulo.zoom_f * var1;
   }

   private double getDVWidth(String var1) {
      return this.szulo.zoom_f * (double)SwingUtilities.computeStringWidth(this.jl.getFontMetrics(this.jl.getFont()), var1);
   }

   private void handleSizing() {
      Font var1 = this.jl.getFont().deriveFont((float)(12.0D * this.szulo.zoom_f));
      ((DefaultEditor)this.evek.getEditor()).getTextField().setFont(var1);
      ((DefaultEditor)this.honapok.getEditor()).getTextField().setFont(var1);
      this.tb.setTitleFont(this.tb.getTitleFont().deriveFont((float)(12.0D * this.szulo.zoom_f)));
      this.napok.setFont(var1);
      this.napok.getTableHeader().setFont(var1);
      this.ok.setFont(var1);
      if (GuiUtil.modGui()) {
         this.napok.setRowHeight((int)(this.getDVItemHeight() + 2.0D));
      }

      byte var2 = 0;
      this.evek.setBounds(0, var2, (int)this.getDVWidth("WWWWWW"), (int)(this.getDVItemHeight() + 2.0D));
      this.honapok.setBounds(this.getPositionFromPrevComponent(this.evek) + 20, var2, (int)this.getDVWidth("WszeptemberW"), (int)(this.getDVItemHeight() + 2.0D));
      int var3 = this.getPositionFromPrevComponent(this.honapok) + 20;
      int var4 = (int)((double)var2 + this.getDVItemHeight() + 6.0D);
      this.evHoPanel.setBounds(5, var4, var3, (int)(1.5D * (this.getDVItemHeight() + 2.0D)));
      var4 += (int)(1.5D * (this.getDVItemHeight() + 2.0D));
      this.napPanel.setBounds(5, var4, var3, (int)(7.0D * (this.getDVItemHeight() + 3.0D)));
      var4 = (int)((double)var4 + 7.0D * (this.getDVItemHeight() + 3.0D));
      this.gombPanel.setBounds(5, var4, var3, (int)(this.getDVItemHeight() + 2.0D));
      var4 = (int)((double)var4 + this.getDVItemHeight() + 2.0D);
      this.foPanel.setPreferredSize(new Dimension(5 + var3 + 5, var4 + 5));
      this.setBounds(this.foPanel.getBounds());
   }

   public class ReadonlyTable extends JTable {
      public ReadonlyTable(TableModel var2) {
         super(var2);
      }

      public boolean isCellEditable(int var1, int var2) {
         return false;
      }

      public void changeSelection(int var1, int var2, boolean var3, boolean var4) {
         if (!((String)DatumValaszto.this.napok.getValueAt(var1, var2)).equals("")) {
            super.changeSelection(var1, var2, false, false);
            int var5;
            if (DatumValaszto.this.kellEllenorzes) {
               var5 = DatumValaszto.this.napEllenorzes();
            } else {
               var5 = 0;
            }

            if (var5 != 0) {
               Calendar var6 = var5 == 1 ? DatumValaszto.this.kezdodatum : DatumValaszto.this.vegedatum;
               DatumValaszto.this.napKijeloleseATablaban(var6.get(5));
            }
         }

      }
   }
}
