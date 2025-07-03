package hu.piller.enykp.gui.component;

import hu.piller.enykp.alogic.calculator.lookup.LookupList;
import hu.piller.enykp.alogic.calculator.lookup.LookupListHandler;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.viewer.PageViewer;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.util.base.DoubleListValue;
import hu.piller.enykp.util.base.Tools;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.ListDataListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.Position.Bias;

public class ENYKFilterCombo extends ENYKTextField implements KeyListener, MouseListener, FocusListener {
   private static final int COMBO_TYPE_LIGHT = 0;
   private static final int COMBO_TYPE_HARD = 1;
   private static final int COMBO_TYPE_NORMAL = 2;
   private boolean engedekeny = false;
   private JPopupMenu pm = new JPopupMenu();
   private JList lista = new JList() {
      public String getToolTipText(MouseEvent var1) {
         int var2 = this.locationToIndex(var1.getPoint());
         String var3 = "";

         Object var6;
         try {
            var6 = this.getModel().getElementAt(var2);
         } catch (Exception var5) {
            var6 = "";
         }

         return "" + var6;
      }
   };
   DefaultListModel tempModel = new DefaultListModel();
   DefaultListModel dummyModel = new DefaultListModel();
   private JScrollPane jsp;
   private Vector elemek;
   private Vector ertekek;
   private Vector indexek;
   private String groupId;
   private String matrixId;
   private String matrix_delimiter;
   private DataFieldModel df;
   private IDataStore ids;
   private Integer dinamikusLapszam;
   private int selectedIndex;
   boolean mouse;
   boolean focusOnField;
   boolean focusOnList;
   String prevStr;
   private static final String FEATURE_COMBO_ITEMS = "combo_items";
   private static final String FEATURE_COMBO_VALUES = "combo_values";
   private static final String FEATURE_COMBO_DATA = "combo_data";
   private static final int LISTWIDTH = 700;
   private static final int MINLISTWIDTH = 200;
   private int listWidth;
   private int triangle_ab;
   private boolean isEditing;
   private boolean full_search;
   private static boolean isInicialized = false;
   ENYKFilterCombo.FCInputVerifier fciv;
   ListDataListener[] ldl;
   private int[] tr_x;
   private int[] tr_y;
   private Object feature_combo_data;
   private Object feature_combo_values;
   private Object feature_combo_items;

   public ENYKFilterCombo() {
      this.jsp = new JScrollPane(this.lista);
      this.elemek = null;
      this.ertekek = null;
      this.indexek = null;
      this.matrixId = "";
      this.selectedIndex = -1;
      this.mouse = false;
      this.focusOnField = false;
      this.focusOnList = false;
      this.prevStr = "";
      this.listWidth = 200;
      this.triangle_ab = 7;
      this.full_search = false;
      this.fciv = new ENYKFilterCombo.FCInputVerifier();
      this.tr_x = new int[3];
      this.tr_y = new int[3];
   }

   public void print(Graphics var1) {
      if (this.is_printable && this.initialized) {
         boolean var2 = false;
         byte var3 = 0;
         int var4 = Integer.parseInt((String)this.features.get("w"));
         int var5 = Integer.parseInt((String)this.features.get("h"));
         int var6 = Integer.parseInt((String)this.features.get("frame_line_width"));
         var1.setColor(this.is_bwmode ? Color.WHITE : this.getBackground());
         var1.fillRect(1, 1, var4 - 2, var5 - 2);
         var1.setColor(this.is_bwmode ? Color.BLACK : this.getForeground());
         Font var7 = this.getFont();
         var1.setFont(var7);
         FontMetrics var8 = var1.getFontMetrics();
         int var9 = var8.stringWidth(this.getText());
         int var10 = var8.getAscent();
         int var11 = (int)this.getBounds().getWidth();
         if (var9 > var11) {
            ENYKTextField.resizeFont(this);
            var1.setFont(this.guessed_font);
            var8 = var1.getFontMetrics();
            var9 = var8.stringWidth(this.getText());
            var10 = var8.getAscent();
         }

         String var12 = (String)this.features.get("alignment");
         int var20;
         if ("right".equals(var12)) {
            var20 = var4 - var9 - (1 + var6);
         } else if ("center".equals(var12)) {
            var20 = (var4 - var9) / 2;
         } else {
            var20 = 1 + var6;
         }

         int var21 = var3 + var10 + (var5 - var10) / 2;
         var1.drawString(this.getText(), var20, var21);
         boolean var13 = false;
         boolean var14 = false;
         boolean var15 = false;
         if (this.features.get("underline") != null) {
            var13 = true;
            var21 = var10 + 2;
         }

         if (this.features.get("middleline") != null) {
            var14 = true;
            var21 = var10 / 2 + 2;
         }

         if (this.features.get("aboveline") != null) {
            var15 = true;
            var21 = 2;
         }

         if (var13 || var14 || var15) {
            var1.drawLine(var20, var21, var4, var21);
         }

         int var16 = Integer.parseInt((String)this.features.get("frame_sides"));
         Color var17 = var1.getColor();
         var1.setColor(this.is_bwmode ? Color.BLACK : (Color)this.features.get("border_color"));
         Stroke var18 = ((Graphics2D)var1).getStroke();
         if (var6 > 0) {
            BasicStroke var19 = new BasicStroke((float)var6);
            ((Graphics2D)var1).setStroke(var19);
         }

         if ((var16 & 1) == 1) {
            var1.drawLine(0, 0, var4, 0);
         }

         if ((var16 & 2) == 2) {
            var1.drawLine(var4 - var6, 0, var4 - var6, var5);
         }

         if ((var16 & 4) == 4) {
            var1.drawLine(0, var5, var4, var5);
         }

         if ((var16 & 8) == 8) {
            var1.drawLine(0, 0, 0, var5);
         }

         var1.setColor(var17);
         ((Graphics2D)var1).setStroke(var18);
         this.paintSubtitle(var1);
         var1.setFont(var7);
      }

   }

   public void paint(Graphics var1) {
      super.paint(var1);
      this.paintTriangle(var1);
   }

   public void release() {
      super.release();
      this.tr_x = null;
      this.tr_y = null;
      this.feature_combo_data = null;
      this.feature_combo_items = null;
      this.feature_combo_values = null;
   }

   private void paintTriangle(Graphics var1) {
      if (!this.is_printing && this.isEnabled()) {
         int var5;
         int var6;
         if (this.getBorder() != null) {
            Insets var7 = this.getBorder().getBorderInsets(this);
            if (var7 != null) {
               var5 = var7.right;
               var6 = var7.top;
            } else {
               var5 = 0;
               var6 = 0;
            }
         } else {
            var5 = 0;
            var6 = 0;
         }

         int var4 = (int)(this.zoom_f * (double)this.triangle_ab);
         int var2 = this.getBounds().width - var4 - var5;
         this.tr_x[0] = var2;
         this.tr_y[0] = var6;
         this.tr_x[1] = var2 + var4;
         this.tr_y[1] = var6;
         this.tr_x[2] = var2 + var4;
         this.tr_y[2] = var6 + var4;

         try {
            if (((String)this.features.get("validation")).equalsIgnoreCase("false")) {
               var1.setColor(this.getTriangleColor(0));
            } else {
               var1.setColor(this.getTriangleColor(2));
            }
         } catch (Exception var9) {
            var1.setColor(this.getTriangleColor(2));
         }

         try {
            if (((String)this.features.get("din_ertek_lista")).equalsIgnoreCase("yes")) {
               var1.setColor(this.getTriangleColor(1));
            }
         } catch (Exception var8) {
            Tools.eLog(var8, 0);
         }

         var1.fillPolygon(this.tr_x, this.tr_y, 3);
      }
   }

   protected void applyFeatures2() {
      super.applyFeatures2();
      applayTCBFeatures(this);
   }

   public void initFeatures() {
      super.initFeatures();
      this.full_search = this.features.containsKey("full_search");

      try {
         String var1 = (String)this.features.get("alignment");
         if ("right".equals(var1)) {
            this.setHorizontalAlignment(4);
         } else if ("center".equals(var1)) {
            this.setHorizontalAlignment(0);
         } else {
            this.setHorizontalAlignment(2);
         }
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
      }

      try {
         this.isEditing = (Boolean)this.features.get("_painter_");
      } catch (Exception var2) {
         System.out.println("_painter_ ???");
      }

      if (!this.isEditing) {
         this.selectedIndex = -1;
         this.handleMatrixThings(true);
         if (this.feature_combo_data instanceof Object[]) {
            Object[] var4 = (Object[])((Object[])this.feature_combo_data);
            if (var4.length > 1 && (var4[0] instanceof Vector || var4[0] == null) && (var4[1] instanceof Vector || var4[1] == null)) {
               this.elemek = var4[0] != null ? new Vector((Vector)((Vector)var4[0])) : null;
               this.ertekek = var4[1] != null ? new Vector((Vector)((Vector)var4[1])) : null;
               this.handleVectors(this.elemek, this.ertekek);
            }
         } else if (this.matrixId != null && !this.matrixId.equals("")) {
            this.handleVectors(this.elemek, this.ertekek);
         }

      }
   }

   protected static void applayTCBFeatures(ENYKFilterCombo var0) {
      Object var1 = var0.getFeatures().get("combo_data");
      var0.feature_combo_data = var1 == null ? null : var1;
      var1 = var0.getFeatures().get("combo_values");
      var0.feature_combo_values = var1 == null ? null : var1;
      var1 = var0.getFeatures().get("combo_items");
      var0.feature_combo_items = var1 == null ? null : var1;
   }

   private void handleVectors(Vector var1, Vector var2) {
      this.lista.setFont(this.lista.getFont().deriveFont(1, (float)(12.0D * this.zoom_f)));
      if (var1 == null && var2 == null) {
         try {
            LookupList var3 = LookupListHandler.getInstance().getLookupListProvider(this.df.formmodel.id, (String)this.df.features.get("fid")).getSortedTableView(this.dinamikusLapszam);
            this.elemek = var3.getElemek();
            this.ertekek = var3.getErtekek();
            this.indexek = var3.getIndexek();
            this.engedekeny = var3.isOverWrite();
         } catch (Exception var5) {
            var5.printStackTrace();
            this.elemek = new Vector();
            this.ertekek = new Vector();
            this.indexek = new Vector();
            this.lista.removeAll();
         }
      } else {
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
      }

      this.init();
   }

   private void init() {
      if (!isInicialized) {
         FocusListener[] var1 = this.getFocusListeners();

         int var2;
         for(var2 = 0; var2 < var1.length; ++var2) {
            this.removeFocusListener(var1[var2]);
         }

         this.addFocusListener(this);

         for(var2 = 0; var2 < var1.length; ++var2) {
            this.addFocusListener(var1[var2]);
         }

         this.setInputVerifier(this.fciv);
         this.lista.setFocusable(true);
         this.lista.setInputVerifier(new ENYKFilterCombo.LInputVerifier());
         this.lista.addFocusListener(this);
         this.pm.setFocusable(false);
         if (this.getKeyListeners().length == 1) {
            this.addKeyListener(this);
            this.addMouseListener(this);
            this.lista.addKeyListener(this);
            this.lista.addMouseListener(this);
            this.pm.addMouseListener(this);
         }

         this.pm.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuCanceled(PopupMenuEvent var1) {
               ENYKFilterCombo.this.mouse = false;
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent var1) {
               ENYKFilterCombo.this.mouse = false;
            }

            public void popupMenuWillBecomeVisible(PopupMenuEvent var1) {
            }
         });
         this.pm.add(this.jsp);
      }

      if (this.elemek != null) {
         if (this.elemek.size() != 0) {
            this.lista.removeAll();
            this.fillLista();
            this.setMaxWidth();
            if (!isInicialized) {
               isInicialized = true;
               this.initTemp();
            }

         }
      }
   }

   private void listDown() {
      this.listDown(true);
   }

   private void listDown(boolean var1) {
      if (this.isFocusOwner()) {
         boolean var2 = true;
         int var6;
         if (this.getText().length() == 0) {
            this.initTemp();
            this.lista.setModel(this.tempModel);
            this.lista.removeSelectionInterval(0, this.lista.getModel().getSize());
            var6 = this.lista.getModel().getSize();
            if (var6 == 1) {
               ++var6;
            }
         }

         int var3;
         if (this.full_search) {
            var3 = this.searchFirstItemIndexFULL(this.getText());
         } else {
            var3 = this.searchFirstItemIndex(this.getText());
         }

         if (var3 < 0) {
            var3 = 0;
         }

         if (!this.engedekeny) {
            try {
               this.lista.setSelectedIndex(var3);
            } catch (Exception var5) {
               Tools.eLog(var5, 0);
            }
         }

         this.lista.ensureIndexIsVisible(var3);
         var6 = this.lista.getModel().getSize();
         if (var6 == 1) {
            ++var6;
         }

         this.pm.setPopupSize(Math.min(this.listWidth, 700), Math.min(var6 + 1, 8) * 23 + 2);
         this.pm.repaint();
         this.pm.show(this, 0, this.getBounds().height);
         this.lista.ensureIndexIsVisible(var3);
         if (var1) {
            this.grabFocus();
         }

      }
   }

   public void keyPressed(KeyEvent var1) {
      int var2 = var1.getKeyCode();
      if (var2 == 40 || var2 == 38 || var2 == 33 || var2 == 34) {
         var1.consume();
      }

   }

   public void keyReleased(KeyEvent var1) {
      try {
         int var2 = var1.getKeyCode();
         if (var2 != 112 && var2 != 113 && var2 != 16 && var2 != 17) {
            int var3 = 0;
            if (this.isEnabled()) {
               if (var2 != 27 && var2 != 37 && var2 != 39) {
                  String var4;
                  if (var2 == 10 || var2 == 38 || var2 == 40 || var2 == 36 || var2 == 35 || var2 == 33 || var2 == 127 || var2 == 34 || var2 == 8) {
                     if (var2 == 127) {
                        if (var1.getModifiers() == 1) {
                           this.setFieldText("");
                           this.doAfterDelete();
                           return;
                        }

                        if ("".equals(this.getText())) {
                           this.doAfterDelete();
                           return;
                        }
                     }

                     if (this.pm.isVisible()) {
                        if (var2 == 8) {
                           if ("".equals(this.getText())) {
                              this.doAfterDelete();
                           }
                        } else {
                           if (var2 == 10) {
                              if (var1.getModifiers() == 2) {
                                 this.mouse = false;
                                 this.setFieldText(this.lista.getSelectedValue());
                                 this.shpm(false);
                                 return;
                              }
                           } else if (var2 == 40) {
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
                        }

                        var1.consume();
                        return;
                     }

                     if (var2 == 8) {
                        if ("".equals(this.getText())) {
                           this.doAfterDelete();
                        }
                     } else {
                        if (var2 == 40) {
                           if (var1.getModifiers() == 2) {
                              this.mouse = true;
                              var4 = this.maskMinus(this.getText(0, this.getDocument().getLength())).trim();
                              this.clearLDListeners();

                              try {
                                 if (this.full_search) {
                                    this.findAllMatches(var4);
                                 } else {
                                    this.findMatches(var4);
                                 }
                              } catch (Exception var46) {
                                 Tools.eLog(var46, 0);
                              } finally {
                                 this.resetLDListeners();
                              }

                              this.listDown(false);
                              return;
                           }

                           return;
                        }

                        if (var2 == 38) {
                           if (this.getParent() instanceof PageViewer) {
                              ((PageViewer)this.getParent()).done_up();
                           }

                           return;
                        }
                     }
                  }

                  var4 = this.getText(0, this.getDocument().getLength());
                  if (var4.equals("")) {
                     this.initTemp();
                  }

                  this.clearLDListeners();

                  try {
                     if (this.full_search) {
                        this.findAllMatches(var4);
                     } else {
                        this.findMatches(var4);
                     }
                  } catch (Exception var48) {
                     Tools.eLog(var48, 0);
                  } finally {
                     this.resetLDListeners();
                  }

                  if (this.engedekeny) {
                     this.selectedIndex = -1;
                     if (!this.findMatchesLight(var4)) {
                        this.shpm(false);
                        return;
                     }

                     this.clearLDListeners();

                     try {
                        while(this.tempModel.size() == 0) {
                           this.setText(var4.substring(0, var4.length() - 1));
                           var4 = this.getText(0, this.getDocument().getLength());
                           if (this.full_search) {
                              this.findAllMatches(var4);
                           } else {
                              this.findMatches(var4);
                           }
                        }
                     } catch (Exception var52) {
                        Tools.eLog(var52, 0);
                     } finally {
                        this.resetLDListeners();
                     }

                     this.lista.setModel(this.tempModel);
                     this.listDown();
                  } else {
                     this.clearLDListeners();

                     try {
                        while(this.tempModel.size() == 0) {
                           this.setText(var4.substring(0, var4.length() - 1));
                           var4 = this.getText(0, this.getDocument().getLength());
                           if (this.full_search) {
                              this.findAllMatches(var4);
                           } else {
                              this.findMatches(var4);
                           }
                        }
                     } catch (Exception var50) {
                        Tools.eLog(var50, 0);
                     } finally {
                        this.resetLDListeners();
                     }

                     this.lista.setModel(this.tempModel);
                     this.listDown();
                  }

               } else {
                  this.shpm(false);
                  if (var2 == 27) {
                     this.setFieldText("");
                  }

               }
            }
         }
      } catch (Exception var54) {
         Tools.eLog(var54, 0);
      }
   }

   private void doAfterDelete() {
      this.prevStr = "";
      this.initTemp();
      this.shpm(false);
   }

   public void keyTyped(KeyEvent var1) {
      int var2 = var1.getKeyCode();
      if (var2 == 40 || var2 == 38 || var2 == 33 || var2 == 34) {
         var1.consume();
      }

   }

   public void mouseClicked(MouseEvent var1) {
      if (var1.getSource() == this) {
         if (!this.isEnabled()) {
            return;
         }

         if (var1.getClickCount() == 2) {
            this.mouse = true;
            this.fillLista();
            this.listDown();
         }
      } else {
         this.mouse = false;
         this.setFieldText(this.lista.getSelectedValue());
         if (this.pm.isVisible()) {
            this.shpm(false);
            this.grabFocus();
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
         if (var2 >= 0) {
            this.setFieldText(this.lista.getModel().getElementAt(var2));
         }

         var1.consume();
         this.grabFocus();
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
            this.setFieldText(this.lista.getSelectedValue());
            if (!this.mouse) {
               this.shpm(false);
            }
         } else {
            this.shpm(false);
         }
      } else if (!this.getText().trim().equals("") && !this.engedekeny && !this.ertekek.contains(this.getText()) && !this.ertekek.contains(this.getText().trim())) {
         this.setFieldText("");
         this.setValue("");
      } else if (this.engedekeny) {
         int var2 = this.oneInstance(this.getText());
         if (var2 > -1) {
            this.lista.setSelectedIndex(var2);
            this.setFieldText(this.lista.getSelectedValue());
         }
      }

      if (!this.focusOnField && !this.focusOnList) {
         this.shpm(false);
      }

   }

   private int searchFirstItemIndex(String var1) {
      return this.searchFirstItemIndex(var1, 0);
   }

   private int searchFirstItemIndexFULL(String var1) {
      return this.searchFirstItemIndexFULL(var1, 0);
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

   private int searchFirstItemIndexFULL(String var1, int var2) {
      for(int var3 = 0; var3 < this.lista.getModel().getSize(); ++var3) {
         if (this.lista.getModel().getElementAt(var3).toString().toLowerCase().contains(var1.toLowerCase())) {
            return var3;
         }
      }

      return 0;
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
      if (var1.length() == 0) {
         this.initTemp();
      } else {
         int var2;
         if (var1.length() < this.prevStr.length()) {
            this.tempModel.clear();

            for(var2 = 0; var2 < this.elemek.size(); ++var2) {
               if (((String)this.elemek.elementAt(var2)).toLowerCase().startsWith(var1.toLowerCase())) {
                  try {
                     this.tempModel.addElement(new DoubleListValue(this.elemek.elementAt(var2), (Integer)this.indexek.elementAt(var2)));
                  } catch (Exception var4) {
                     this.tempModel.addElement(new DoubleListValue(this.elemek.elementAt(var2), -1));
                  }
               }
            }
         } else {
            var2 = 0;

            while(var2 < this.tempModel.getSize()) {
               if (!this.tempModel.getElementAt(var2).toString().toLowerCase().startsWith(var1.toLowerCase())) {
                  this.tempModel.remove(var2);
               } else {
                  ++var2;
               }
            }
         }
      }

      this.lista.setModel(this.tempModel);
      this.prevStr = var1;
   }

   private void findAllMatches(String var1) {
      if (var1.length() == 0) {
         this.initTemp();
      } else {
         int var2;
         if (var1.length() < this.prevStr.length()) {
            this.tempModel.clear();

            for(var2 = 0; var2 < this.elemek.size(); ++var2) {
               if (((String)this.elemek.elementAt(var2)).toLowerCase().contains(var1.toLowerCase())) {
                  try {
                     this.tempModel.addElement(new DoubleListValue(this.elemek.elementAt(var2), (Integer)this.indexek.elementAt(var2)));
                  } catch (Exception var4) {
                     this.tempModel.addElement(new DoubleListValue(this.elemek.elementAt(var2), -1));
                  }
               }
            }
         } else {
            var2 = 0;

            while(var2 < this.tempModel.getSize()) {
               if (!this.tempModel.getElementAt(var2).toString().toLowerCase().contains(var1.toLowerCase())) {
                  this.tempModel.remove(var2);
               } else {
                  ++var2;
               }
            }
         }
      }

      this.lista.setModel(this.tempModel);
      this.prevStr = var1;
   }

   private void initTemp() {
      this.clearLDListeners();

      try {
         this.lista.setModel(this.dummyModel);
         this.tempModel.clear();

         for(int var1 = 0; var1 < this.elemek.size(); ++var1) {
            try {
               this.tempModel.addElement(new DoubleListValue(this.elemek.elementAt(var1), (Integer)this.indexek.elementAt(var1)));
            } catch (Exception var7) {
               this.tempModel.addElement(new DoubleListValue(this.elemek.elementAt(var1), -1));
            }
         }
      } catch (Exception var8) {
         Tools.eLog(var8, 0);
      } finally {
         this.resetLDListeners();
      }

   }

   private void setFieldText(Object var1) {
      if (var1 instanceof DoubleListValue) {
         try {
            this.selectedIndex = ((DoubleListValue)var1).index;
            this.setText((String)this.ertekek.get(this.elemek.indexOf(var1.toString())));
         } catch (Exception var6) {
            this.setText("");
            this.setValue("");
            this.selectedIndex = -1;
         }
      } else {
         if (var1 == null) {
            return;
         }

         if (var1.toString().trim().equals("")) {
            this.setText("");
            this.selectedIndex = -1;
            return;
         }

         try {
            int var2 = this.elemek.indexOf(var1);
            this.setText((String)this.ertekek.elementAt(var2));

            try {
               this.selectedIndex = (Integer)this.indexek.elementAt(var2);
            } catch (Exception var4) {
               this.selectedIndex = -1;
            }
         } catch (Exception var5) {
            this.setText("");
            this.selectedIndex = -1;
         }
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

   public Object getFieldValue() {
      if (this.engedekeny) {
         return super.getFieldValue();
      } else {
         return this.ertekek.indexOf(this.getText()) == -1 ? "" : super.getFieldValue();
      }
   }

   public int getRecordIndex() {
      try {
         return this.selectedIndex;
      } catch (Exception var2) {
         return -1;
      }
   }

   public void setFeatures(Hashtable var1) {
      super.setFeatures(var1);

      try {
         this.isEditing = (Boolean)var1.get("_painter_");
      } catch (Exception var4) {
         System.out.println("_painter_ ???");
      }

      if (!this.isEditing) {
         if (var1.containsKey("_zoom_")) {
            super.setZoom((Integer)var1.get("_zoom_"));
         }

         this.handleMatrixThings(false);
         if (!this.getText().trim().equals("")) {
            this.selectedIndex = Tools.searchInTheMatrix(this.ids, this.df.formmodel.id, this.matrixId, this.matrix_delimiter, this.groupId, this.dinamikusLapszam);
         }

         Vector var2 = null;
         Vector var3 = null;
         if (var1.containsKey("splist")) {
            var2 = this.getFeatureAsVector((String[])((String[])var1.get("splist")));
         }

         if (var1.containsKey("spvalues")) {
            var3 = this.getFeatureAsVector((String[])((String[])var1.get("spvalues")));
         }

         this.handleVectors(var2, var3);
         if (isInicialized) {
            this.initTemp();
         }

      }
   }

   private Vector getFeatureAsVector(String[] var1) {
      Vector var2 = new Vector(var1.length);

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.add(var1[var3]);
      }

      return var2;
   }

   private boolean findMatchesLight(String var1) {
      if (var1.length() == 0) {
         return true;
      } else {
         for(int var2 = 0; var2 < this.elemek.size(); ++var2) {
            if (this.full_search) {
               if (((String)this.elemek.elementAt(var2)).toLowerCase().contains(var1.toLowerCase())) {
                  return true;
               }
            } else if (((String)this.elemek.elementAt(var2)).toLowerCase().startsWith(var1.toLowerCase())) {
               return true;
            }
         }

         return false;
      }
   }

   private void fillLista() {
      this.lista.setListData(this.elemek);
   }

   private Color getTriangleColor(int var1) {
      if (var1 == 1) {
         return this.full_search ? Color.CYAN : new Color(0, 0, 255);
      } else if (var1 == 0) {
         return this.full_search ? new Color(64, 228, 64) : new Color(0, 128, 0);
      } else {
         return this.full_search ? new Color(255, 152, 0) : new Color(255, 0, 0);
      }
   }

   private int containsIgnoreCase(String var1) {
      boolean var2 = false;

      int var3;
      for(var3 = 0; var3 < this.ertekek.size() && !var2; ++var3) {
         if (((String)this.ertekek.elementAt(var3)).equalsIgnoreCase(var1)) {
            var2 = true;
         }
      }

      if (var2) {
         --var3;
         if (((String)this.ertekek.elementAt(var3)).equals(var1)) {
            return 0;
         }

         if (((String)this.ertekek.elementAt(var3)).toLowerCase().equals(var1)) {
            return -1;
         }

         if (((String)this.ertekek.elementAt(var3)).toUpperCase().equals(var1)) {
            return 1;
         }
      }

      return 2;
   }

   public String maskMinus(String var1) {
      String var2 = this.feature_abev_mask;
      if (var2 == null) {
         return var1;
      } else if (!var2.equals("") && !var2.startsWith("%")) {
         StringBuffer var3 = new StringBuffer();

         for(int var4 = 0; var4 < var1.length(); ++var4) {
            try {
               if (var1.charAt(var4) != var2.charAt(var4)) {
                  var3.append(var1.charAt(var4));
               }
            } catch (Exception var6) {
               Tools.eLog(var6, 0);
            }
         }

         return var3.toString();
      } else {
         return var1;
      }
   }

   private void setMaxWidth() {
      this.listWidth = 200;
      String var1 = "";

      for(int var2 = 0; var2 < this.elemek.size(); ++var2) {
         if (((String)this.elemek.get(var2)).length() > var1.length()) {
            var1 = (String)this.elemek.get(var2);
         }
      }

      JLabel var3 = new JLabel();
      var3.setFont(this.lista.getFont());
      this.listWidth = Math.min(GuiUtil.getW(var3, var1 + "WWW"), 700);
   }

   private void handleMatrixThings(boolean var1) {
      try {
         this.isEditing = (Boolean)this.features.get("_painter_");
      } catch (Exception var9) {
         System.out.println("_painter_ ???");
      }

      if (var1 || !this.isEditing) {
         this.engedekeny = false;

         try {
            if (this.features.containsKey("validation") && ((String)this.features.get("validation")).equalsIgnoreCase("false")) {
               this.engedekeny = true;
            }
         } catch (Exception var8) {
            this.engedekeny = false;
         }

         try {
            this.groupId = (String)this.features.get("field_group_id");
         } catch (Exception var7) {
            this.groupId = "";
         }

         try {
            this.matrixId = (String)this.features.get("matrix_id");
         } catch (Exception var6) {
            this.matrixId = "";
         }

         try {
            this.matrix_delimiter = (String)this.features.get("matrix_delimiter");
         } catch (Exception var5) {
            this.matrix_delimiter = ";";
         }

         try {
            this.df = (DataFieldModel)this.features.get("_df_");
         } catch (Exception var4) {
            this.df = null;
         }

         this.ids = MainFrame.thisinstance.mp.getDMFV().bm.get_datastore();

         try {
            this.dinamikusLapszam = (Integer)this.features.get("_dynindex_");
         } catch (Exception var3) {
            this.dinamikusLapszam = 0;
         }

      }
   }

   private int oneInstance(String var1) {
      var1 = var1.trim();
      boolean var2 = false;

      int var3;
      for(var3 = 0; var3 < this.lista.getModel().getSize() && !var2; ++var3) {
         if (this.lista.getModel().getElementAt(var3).toString().toLowerCase().startsWith(var1.toLowerCase() + " ")) {
            var2 = true;
         }
      }

      if (var2 && this.lista.getModel().getSize() == 1) {
         this.selectedIndex = this.ertekek.indexOf(var1);
         return var3 - 1;
      } else {
         return -1;
      }
   }

   private void clearLDListeners() {
      try {
         this.ldl = this.tempModel.getListDataListeners();
         ListDataListener[] var1 = this.ldl;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            ListDataListener var4 = var1[var3];
            this.tempModel.removeListDataListener(var4);
         }
      } catch (Exception var5) {
         Tools.eLog(var5, 0);
      }

   }

   private void resetLDListeners() {
      try {
         ListDataListener[] var1 = this.ldl;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            ListDataListener var4 = var1[var3];
            this.tempModel.addListDataListener(var4);
         }

         this.lista.setModel(this.dummyModel);
         this.lista.setModel(this.tempModel);
      } catch (Exception var5) {
         Tools.eLog(var5, 0);
      }

   }

   private class LInputVerifier extends InputVerifier {
      private LInputVerifier() {
      }

      public boolean verify(JComponent var1) {
         if (ENYKFilterCombo.this.engedekeny) {
            ENYKFilterCombo.this.shpm(false);
            return true;
         } else {
            if (ENYKFilterCombo.this.mouse) {
               ENYKFilterCombo.this.mouse = false;
               ENYKFilterCombo.this.setFieldText(ENYKFilterCombo.this.lista.getSelectedValue());
               ENYKFilterCombo.this.grabFocus();
            }

            return true;
         }
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
         int var2;
         if (ENYKFilterCombo.this.pm.isVisible()) {
            if (ENYKFilterCombo.this.engedekeny) {
               var2 = ENYKFilterCombo.this.oneInstance(ENYKFilterCombo.this.getText());
               if (var2 > -1) {
                  ENYKFilterCombo.this.lista.setSelectedIndex(var2);
                  ENYKFilterCombo.this.setFieldText(ENYKFilterCombo.this.lista.getSelectedValue());
               }
            } else if (ENYKFilterCombo.this.getText().trim().equals("")) {
               ENYKFilterCombo.this.setFieldText("");
               ENYKFilterCombo.this.setValue("");
            } else {
               ENYKFilterCombo.this.setFieldText(ENYKFilterCombo.this.lista.getSelectedValue());
            }

            ENYKFilterCombo.this.shpm(false);
            return true;
         } else {
            try {
               var2 = ENYKFilterCombo.this.containsIgnoreCase(ENYKFilterCombo.this.getText().trim());
               if (ENYKFilterCombo.this.maskMinus(ENYKFilterCombo.this.getText()).trim().equals("")) {
                  return true;
               } else {
                  switch(var2) {
                  case -1:
                     ENYKFilterCombo.this.setText(ENYKFilterCombo.this.getText().toLowerCase());
                     return true;
                  case 0:
                     return true;
                  case 1:
                     ENYKFilterCombo.this.setText(ENYKFilterCombo.this.getText().toUpperCase());
                     return true;
                  default:
                     return false;
                  }
               }
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
