package hu.piller.enykp.gui.component.filtercombo;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.component.HunCharComparator;
import hu.piller.enykp.util.base.Tools;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.Position.Bias;

public class ENYKFilterComboStandard extends JTextField implements KeyListener, MouseListener, FocusListener {
   private boolean freeTextEnabled;
   private boolean needSort;
   private JPopupMenu pm = new JPopupMenu();
   private JList lista = new JList() {
      public String getToolTipText(MouseEvent var1) {
         int var2 = this.locationToIndex(var1.getPoint());
         Object var3 = this.getModel().getElementAt(var2);
         return "" + var3;
      }
   };
   DefaultListModel tempModel = new DefaultListModel();
   private JScrollPane jsp;
   private Vector elemek;
   private Vector ertekek;
   private HunCharComparator hcc;
   boolean got;
   boolean mouse;
   boolean focusOnField;
   boolean focusOnList;
   String prevStr;
   public static final String FEATURE_COMBO_ITEMS = "combo_items";
   public static final String FEATURE_COMBO_VALUES = "combo_values";
   public static final String FEATURE_COMBO_DATA = "combo_data";
   public int LISTWIDTH;
   public int maxColWidth;
   private int triangle_ab;
   ENYKFilterComboStandard.FCInputVerifier fciv;
   private int[] tr_x;
   private int[] tr_y;

   public void setFreeTextEnabled(boolean var1) {
      this.freeTextEnabled = var1;
   }

   public boolean isFreeTextEnabled() {
      return this.freeTextEnabled;
   }

   public ENYKFilterComboStandard() {
      this.jsp = new JScrollPane(this.lista);
      this.elemek = null;
      this.ertekek = null;
      this.hcc = new HunCharComparator();
      this.got = false;
      this.mouse = false;
      this.focusOnField = false;
      this.focusOnList = false;
      this.prevStr = "";
      this.LISTWIDTH = GuiUtil.getW("WWWWWWWWWWWWWWW");
      this.maxColWidth = this.LISTWIDTH;
      this.triangle_ab = 7;
      this.fciv = new ENYKFilterComboStandard.FCInputVerifier();
      this.tr_x = new int[3];
      this.tr_y = new int[3];
      this.needSort = true;
   }

   public ENYKFilterComboStandard(int var1) {
      this.jsp = new JScrollPane(this.lista);
      this.elemek = null;
      this.ertekek = null;
      this.hcc = new HunCharComparator();
      this.got = false;
      this.mouse = false;
      this.focusOnField = false;
      this.focusOnList = false;
      this.prevStr = "";
      this.LISTWIDTH = GuiUtil.getW("WWWWWWWWWWWWWWW");
      this.maxColWidth = this.LISTWIDTH;
      this.triangle_ab = 7;
      this.fciv = new ENYKFilterComboStandard.FCInputVerifier();
      this.tr_x = new int[3];
      this.tr_y = new int[3];
      this.needSort = true;
      this.maxColWidth = var1;
   }

   public ENYKFilterComboStandard(Object var1, boolean var2) {
      this.jsp = new JScrollPane(this.lista);
      this.elemek = null;
      this.ertekek = null;
      this.hcc = new HunCharComparator();
      this.got = false;
      this.mouse = false;
      this.focusOnField = false;
      this.focusOnList = false;
      this.prevStr = "";
      this.LISTWIDTH = GuiUtil.getW("WWWWWWWWWWWWWWW");
      this.maxColWidth = this.LISTWIDTH;
      this.triangle_ab = 7;
      this.fciv = new ENYKFilterComboStandard.FCInputVerifier();
      this.tr_x = new int[3];
      this.tr_y = new int[3];
      this.addElements(var1, var2);
   }

   public ENYKFilterComboStandard(Object var1) {
      this.jsp = new JScrollPane(this.lista);
      this.elemek = null;
      this.ertekek = null;
      this.hcc = new HunCharComparator();
      this.got = false;
      this.mouse = false;
      this.focusOnField = false;
      this.focusOnList = false;
      this.prevStr = "";
      this.LISTWIDTH = GuiUtil.getW("WWWWWWWWWWWWWWW");
      this.maxColWidth = this.LISTWIDTH;
      this.triangle_ab = 7;
      this.fciv = new ENYKFilterComboStandard.FCInputVerifier();
      this.tr_x = new int[3];
      this.tr_y = new int[3];
      this.needSort = true;
      this.install(var1);
   }

   public ENYKFilterComboStandard(Object var1, int var2) {
      this.jsp = new JScrollPane(this.lista);
      this.elemek = null;
      this.ertekek = null;
      this.hcc = new HunCharComparator();
      this.got = false;
      this.mouse = false;
      this.focusOnField = false;
      this.focusOnList = false;
      this.prevStr = "";
      this.LISTWIDTH = GuiUtil.getW("WWWWWWWWWWWWWWW");
      this.maxColWidth = this.LISTWIDTH;
      this.triangle_ab = 7;
      this.fciv = new ENYKFilterComboStandard.FCInputVerifier();
      this.tr_x = new int[3];
      this.tr_y = new int[3];
      this.needSort = true;
      this.maxColWidth = var2;
      this.install(var1);
   }

   public boolean addElements(Object var1, boolean var2) {
      this.needSort = var2;
      if (var1 instanceof Object[]) {
         if (var2) {
            int var3 = ((Object[])((Object[])var1)).length;
            Object[] var4 = new Object[var3];
            System.arraycopy(var1, 0, var4, 0, var3);
            Arrays.sort((Object[])((Object[])var4));
            this.install(var1);
            return true;
         } else {
            this.install(var1);
            return true;
         }
      } else {
         return false;
      }
   }

   private void install(Object var1) {
      if (var1 instanceof Object[]) {
         Vector var2 = new Vector(Arrays.asList((Object[])((Object[])var1)));
         this.handleVectors(var2, (Vector)null);
      }

   }

   public void superPaint(Graphics var1) {
   }

   public void release() {
      this.tr_x = null;
      this.tr_y = null;
   }

   public void setFeature(String var1, Object var2) {
      if (var1.equals("combo_data") && var2 instanceof Object[]) {
         Object[] var3 = (Object[])((Object[])var2);
         if (var3.length > 1 && (var3[0] instanceof Vector || var3[0] == null) && (var3[1] instanceof Vector || var3[1] == null)) {
            this.elemek = var3[0] != null ? new Vector((Vector)((Vector)var3[0])) : null;
            this.ertekek = var3[1] != null ? new Vector((Vector)((Vector)var3[1])) : null;
            this.handleVectors(this.elemek, this.ertekek);
         }
      }

   }

   private void handleVectors(Vector var1, Vector var2) {
      if (var1 == null) {
         this.elemek = var2;
      } else {
         this.elemek = var1;
         if (var2 != null) {
            try {
               if (var1.size() == var2.size()) {
                  this.ertekek = var2;
               }
            } catch (Exception var4) {
               Tools.eLog(var4, 0);
            }
         }
      }

      this.splitData();
      this.init();
   }

   private void init() {
      if (this.elemek != null) {
         if (this.elemek.size() != 0) {
            Vector var1 = this.mergeVectors();
            if (this.needSort) {
               Collections.sort(var1, this.hcc);
            }

            this.splitVectors(var1);
            var1.removeAllElements();
            this.addKeyListener(this);
            this.addMouseListener(this);
            FocusListener[] var2 = this.getFocusListeners();

            int var3;
            for(var3 = 0; var3 < var2.length; ++var3) {
               this.removeFocusListener(var2[var3]);
            }

            this.addFocusListener(this);

            for(var3 = 0; var3 < var2.length; ++var3) {
               this.addFocusListener(var2[var3]);
            }

            this.setInputVerifier(this.fciv);
            this.lista.removeAll();
            this.lista.setListData(this.elemek);
            this.lista.setFocusable(true);
            this.lista.addKeyListener(this);
            this.lista.addMouseListener(this);
            this.lista.setInputVerifier(new ENYKFilterComboStandard.LInputVerifier());
            this.lista.addFocusListener(this);
            this.pm.setFocusable(false);
            this.pm.addMouseListener(this);
            this.pm.addPopupMenuListener(new PopupMenuListener() {
               public void popupMenuCanceled(PopupMenuEvent var1) {
                  ENYKFilterComboStandard.this.mouse = false;
               }

               public void popupMenuWillBecomeInvisible(PopupMenuEvent var1) {
                  ENYKFilterComboStandard.this.mouse = false;
               }

               public void popupMenuWillBecomeVisible(PopupMenuEvent var1) {
               }
            });
            this.jsp.setSize(new Dimension(this.maxColWidth, 8 * GuiUtil.getCommonItemHeight()));
            this.jsp.setPreferredSize(this.jsp.getSize());
            this.pm.add(this.jsp);
            this.initTemp();
            AbstractAction var5 = new AbstractAction() {
               public void actionPerformed(ActionEvent var1) {
                  if (ENYKFilterComboStandard.this.pm.isVisible()) {
                     ENYKFilterComboStandard.this.setFieldText(ENYKFilterComboStandard.this.lista.getSelectedValue());
                     ENYKFilterComboStandard.this.shpm(false);
                  }

                  ENYKFilterComboStandard.this.fireActionPerformed();
               }
            };
            InputMap var4 = this.getInputMap(0);
            var4.put(KeyStroke.getKeyStroke("ENTER"), "newenter");
            this.setInputMap(0, var4);
            this.getActionMap().put("newenter", var5);
         }
      }
   }

   private void listDown() {
      this.listDown(true);
   }

   private void listDown(boolean var1) {
      boolean var2 = true;
      int var3 = this.maxColWidth;
      int var5;
      if (this.getText().length() == 0) {
         this.initTemp();
         this.lista.setModel(this.tempModel);
         this.lista.removeSelectionInterval(0, this.lista.getModel().getSize());
         var5 = this.lista.getModel().getSize();
         if (var5 == 1) {
            ++var5;
         }

         var3 = Math.max(this.getWidthFromListmodel(), this.maxColWidth);
         if (this.pm.getInvoker() != null) {
            var3 = Math.max(var3, this.pm.getInvoker().getWidth());
         }

         this.pm.setPopupSize(var3, Math.min(8, var5) * (GuiUtil.getCommonItemHeight() + 2) + 8);
      }

      int var4 = this.searchFirstItemIndex(this.getText());
      if (var4 < 0) {
         var4 = 0;
      }

      this.lista.setSelectedIndex(var4);
      this.lista.ensureIndexIsVisible(var4);
      var5 = this.lista.getModel().getSize();
      if (var5 == 1) {
         ++var5;
      }

      this.pm.setPopupSize(Math.min(Math.max(var3, this.maxColWidth), this.maxColWidth), Math.min(var5, 8) * (GuiUtil.getCommonItemHeight() + 2) + 8);
      this.pm.show(this, 0, this.getHeight());
      if (var1) {
         this.grabFocus();
      }

   }

   public void keyPressed(KeyEvent var1) {
      int var2 = var1.getKeyCode();
      if (var2 == 40 || var2 == 38 || var2 == 33 || var2 == 34) {
         var1.consume();
      }

   }

   private boolean isKeyProcessable(int var1) {
      return var1 == 10 || var1 == 38 || var1 == 40 || var1 == 36 || var1 == 35 || var1 == 33 || var1 == 34;
   }

   public void addKeyListener(KeyListener var1) {
      KeyListener[] var2 = this.getKeyListeners();
      boolean var3 = true;

      for(int var4 = 0; var4 < var2.length; ++var4) {
         if (var2[var4].equals(this)) {
            var3 = false;
            break;
         }
      }

      if (var3) {
         super.addKeyListener(var1);
      }

   }

   public void keyReleased(KeyEvent var1) {
      try {
         int var2 = var1.getKeyCode();
         int var3 = 0;
         if (!this.isEnabled()) {
            return;
         }

         if (this.getText().startsWith("(Nincs feltétel)")) {
            this.removeNoruleLabel("" + var1.getKeyChar());
         }

         if (var2 == 27 || var2 == 37 || var2 == 39) {
            this.shpm(false);
            if (var2 == 27) {
               this.setFieldText("");
            }

            return;
         }

         if (this.isKeyProcessable(var2)) {
            if (this.pm.isVisible()) {
               if (var2 == 10) {
                  this.mouse = false;
                  this.setFieldText(this.lista.getSelectedValue());
                  this.shpm(false);
                  return;
               }

               if (var2 == 40) {
                  if (this.lista.getSelectedIndex() < this.lista.getModel().getSize() - 1) {
                     var3 = 1;
                  }
               } else if (var2 == 38) {
                  if (this.lista.getSelectedIndex() > 0) {
                     var3 = -1;
                  }
               } else if (var2 == 36) {
                  var3 = -this.lista.getSelectedIndex();
               } else if (var2 == 35) {
                  var3 = this.lista.getModel().getSize() - this.lista.getSelectedIndex() - 1;
               } else if (var2 == 33) {
                  var3 = -1 * Math.min(this.lista.getSelectedIndex() + 1, 7);
               } else if (var2 == 34) {
                  var3 = Math.min(this.lista.getModel().getSize() - 1, 7);
               }

               this.lista.setSelectedIndex(this.lista.getSelectedIndex() + var3);
               this.lista.ensureIndexIsVisible(this.lista.getSelectedIndex() + var3);
               this.setFieldText(this.lista.getSelectedValue());
               var1.consume();
               return;
            }

            if (var2 == 40) {
               this.mouse = true;
               this.listDown(false);
               return;
            }

            return;
         }

         String var4 = this.getText(0, this.getDocument().getLength());
         this.findMatches(var4);

         while(this.tempModel.size() == 0) {
            if (!this.freeTextEnabled) {
               this.setText(var4.substring(0, var4.length() - 1));
               var4 = this.getText(0, this.getDocument().getLength());
               this.findMatches(var4);
            } else {
               this.tempModel.addElement(var4);
            }
         }

         this.lista.setModel(this.tempModel);
         this.listDown();
      } catch (Exception var5) {
         Tools.eLog(var5, 0);
      }

   }

   public void keyTyped(KeyEvent var1) {
      int var2 = var1.getKeyCode();
      if (var2 == 40 || var2 == 38 || var2 == 33 || var2 == 34) {
         var1.consume();
      }

   }

   public void mouseClicked(MouseEvent var1) {
      if (var1.getSource() != this && !var1.getSource().getClass().toString().endsWith("ENYKFilterComboPanel")) {
         this.mouse = false;
         this.setFieldText(this.lista.getSelectedValue());
         if (this.pm.isVisible()) {
            this.shpm(false);
            this.grabFocus();
         }
      } else {
         if (!this.isEnabled()) {
            return;
         }

         if (var1.getClickCount() == 2) {
            this.mouse = true;
            this.setInputVerifier((InputVerifier)null);
            if (this.lista.getModel().getSize() == 1 || this.getText().trim().equals("")) {
               this.lista.setListData(this.elemek);
            }

            this.listDown();
         }
      }

   }

   public void mouseEntered(MouseEvent var1) {
   }

   public void mouseExited(MouseEvent var1) {
   }

   public void mousePressed(MouseEvent var1) {
      if (var1.getSource() != this && this.lista.isVisible()) {
         int var2 = this.lista.locationToIndex(var1.getPoint());
         this.setFieldText(this.lista.getModel().getElementAt(var2));
         var1.consume();
         this.fireActionPerformed();
      }

   }

   public void mouseReleased(MouseEvent var1) {
   }

   public void focusGained(FocusEvent var1) {
      if (var1.getSource() == this) {
         this.focusOnField = true;
         if (this.getInputVerifier() == null && !this.mouse) {
            this.setInputVerifier(this.fciv);
         }
      } else {
         this.focusOnList = true;
      }

   }

   public void focusLost(FocusEvent var1) {
      if (var1.getSource() == this) {
         this.focusOnField = false;
      } else {
         this.focusOnList = false;
      }

      if (this.pm.isVisible()) {
         if (var1.getSource() == this) {
            if (!this.mouse) {
               this.shpm(false);
            } else {
               this.setFieldText(this.lista.getSelectedValue());
            }
         } else {
            this.shpm(false);
         }
      } else if (!this.getText().trim().equals("") && !this.ertekek.contains(this.getText()) && !this.ertekek.contains(this.getText().trim()) && !this.freeTextEnabled) {
         this.setText("");
      }

      if (!this.focusOnField && !this.focusOnList) {
         this.shpm(false);
      }

   }

   private int searchFirstItemIndex(String var1) {
      return this.searchFirstItemIndex(var1, 0);
   }

   private int searchFirstItemIndex(String var1, int var2) {
      int var3 = -1;

      try {
         while(var1.length() > 0 && var3 == -1) {
            var3 = this.lista.getNextMatch(var1, var2, Bias.Forward);
            var1 = var1.substring(0, var1.length() - 1);
         }
      } catch (Exception var5) {
         Tools.eLog(var5, 0);
      }

      return var3;
   }

   private Vector mergeVectors() {
      Vector var1 = new Vector(this.elemek.size());

      for(int var2 = 0; var2 < this.elemek.size(); ++var2) {
         var1.add(this.elemek.get(var2) + "|#&@&#|" + this.ertekek.get(var2));
      }

      return var1;
   }

   private void splitVectors(Vector var1) {
      this.elemek.removeAllElements();
      this.ertekek.removeAllElements();

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         this.elemek.add(((String)var1.elementAt(var2)).substring(0, ((String)var1.elementAt(var2)).indexOf("|#&@&#|")));
         this.ertekek.add(((String)var1.elementAt(var2)).substring(((String)var1.elementAt(var2)).indexOf("|#&@&#|") + 7));
      }

   }

   private void splitData() {
      if (this.ertekek == null) {
         this.ertekek = new Vector(this.elemek.size());

         for(int var1 = 0; var1 < this.elemek.size(); ++var1) {
            String var2 = (String)this.elemek.elementAt(var1);
            var2 = var2.replaceAll("/=", "nothingsimpossible");

            try {
               String[] var3 = var2.split("=");
               var3[0] = var3[0].replaceAll("nothingsimpossible", "=");
               this.ertekek.add(var3[0]);
            } catch (Exception var4) {
               this.ertekek.add(var2);
            }
         }

      }
   }

   private void findMatches(String var1) {
      if (var1.length() == 0 || var1.length() < this.prevStr.length()) {
         this.initTemp();
      }

      int var2 = 0;

      while(var2 < this.tempModel.getSize()) {
         if (!((String)this.tempModel.getElementAt(var2)).toLowerCase().startsWith(var1.toLowerCase())) {
            this.tempModel.remove(var2);
         } else {
            ++var2;
         }
      }

      this.prevStr = var1;
      this.lista.setModel(this.tempModel);
   }

   private void initTemp() {
      this.tempModel.clear();

      for(int var1 = 0; var1 < this.elemek.size(); ++var1) {
         this.tempModel.addElement(this.elemek.get(var1));
      }

   }

   private void setFieldText(Object var1) {
      try {
         if (this.freeTextEnabled && this.elemek.indexOf(var1) == -1) {
            this.setText((String)var1);
         } else {
            this.setText((String)this.ertekek.get(this.elemek.indexOf(var1)));
         }

         this.setCaretPosition(0);
      } catch (Exception var3) {
         this.setText("");
      }

   }

   private void shpm(boolean var1) {
      this.pm.setVisible(var1);
      if (!var1) {
         this.mouse = false;
      }

      if (this.getInputVerifier() == null) {
         this.setInputVerifier(this.fciv);
      }

   }

   public String getENYKValue() {
      return this.ertekek.indexOf(this.getText()) == -1 ? "" : this.getENYKText();
   }

   public String getENYKText() {
      return this.getText();
   }

   private void removeNoruleLabel(String var1) {
      this.setText(var1);
   }

   private int getWidthFromListmodel() {
      String var1 = "";

      for(int var2 = 0; var2 < this.tempModel.getSize(); ++var2) {
         String var3 = (String)this.tempModel.get(var2);
         if (var1.length() < var3.length()) {
            var1 = var3;
         }
      }

      return GuiUtil.getW(var1 + "WW");
   }

   private class LInputVerifier extends InputVerifier {
      private LInputVerifier() {
      }

      public boolean verify(JComponent var1) {
         if (ENYKFilterComboStandard.this.mouse) {
            ENYKFilterComboStandard.this.mouse = false;
            ENYKFilterComboStandard.this.setFieldText(ENYKFilterComboStandard.this.lista.getSelectedValue());
            ENYKFilterComboStandard.this.grabFocus();
         }

         return true;
      }

      // $FF: synthetic method
      LInputVerifier(Object var2) {
         this();
      }
   }

   private class FCInputVerifier extends InputVerifier {
      private FCInputVerifier() {
      }

      public boolean verify(JComponent var1) {
         if (ENYKFilterComboStandard.this.pm.isVisible()) {
            ENYKFilterComboStandard.this.setFieldText(ENYKFilterComboStandard.this.lista.getSelectedValue());
            ENYKFilterComboStandard.this.shpm(false);
            return true;
         } else {
            try {
               return ENYKFilterComboStandard.this.getText().equals("") || ENYKFilterComboStandard.this.ertekek.contains(ENYKFilterComboStandard.this.getText());
            } catch (Exception var3) {
               return false;
            }
         }
      }

      // $FF: synthetic method
      FCInputVerifier(Object var2) {
         this();
      }
   }
}
