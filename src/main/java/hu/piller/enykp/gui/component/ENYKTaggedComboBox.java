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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
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

public class ENYKTaggedComboBox extends ENYKFormattedTaggedTextField implements FocusListener, MouseListener, KeyListener {
   private static final int COMBO_TYPE_LIGHT = 0;
   private static final int COMBO_TYPE_HARD = 1;
   private boolean engedekeny = false;
   private static final String FEATURE_COMBO_ITEMS = "combo_items";
   private static final String FEATURE_COMBO_VALUES = "combo_values";
   private static final String FEATURE_COMBO_DATA = "combo_data";
   private Vector elemek = null;
   private Vector ertekek = null;
   private Vector indexek = null;
   private Vector mentettErtekek = null;
   private int selectedIndex = -1;
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
   private DefaultListModel tempModel = new DefaultListModel();
   private DefaultListModel dummyModel = new DefaultListModel();
   private boolean mouse = false;
   private boolean focusOnField = false;
   private boolean focusOnList = false;
   String prevStr = "";
   ENYKTaggedComboBox.FCInputVerifier fciv = new ENYKTaggedComboBox.FCInputVerifier();
   private JPopupMenu pm = new JPopupMenu();
   private JScrollPane jsp;
   private int triangle_ab;
   private int listWidth;
   private static final int LISTWIDTH = 700;
   private static final int MINLISTWIDTH = 200;
   private boolean is_printing;
   private boolean isEditing;
   private boolean full_search;
   private String groupId;
   private String matrixId;
   private String matrix_delimiter;
   private DataFieldModel df;
   private IDataStore ids;
   private Integer dinamikusLapszam;
   private static boolean isInicialized = false;
   ListDataListener[] ldl;
   private int[] tr_x;
   private int[] tr_y;
   protected Object feature_combo_data;
   protected Object feature_combo_values;
   protected Object feature_combo_items;

   public ENYKTaggedComboBox() {
      this.jsp = new JScrollPane(this.lista);
      this.triangle_ab = 7;
      this.listWidth = 200;
      this.is_printing = false;
      this.full_search = false;
      this.matrixId = "";
      this.tr_x = new int[3];
      this.tr_y = new int[3];
   }

   public void print(Graphics var1) {
      this.is_printing = true;
      super.print(var1);
      this.is_printing = false;
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

   public void setValue(String var1) {
      super.setText(this.findNewValue(var1));
      this.setENYKValue(var1, false);
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
         int var2 = this.graphics_bounds.width - var4 - var5;
         int var3 = this.graphics_bounds.y + var6;
         this.tr_x[0] = var2;
         this.tr_y[0] = var3;
         this.tr_x[1] = var2 + var4;
         this.tr_y[1] = var3;
         this.tr_x[2] = var2 + var4;
         this.tr_y[2] = var3 + var4;

         try {
            if (((String)this.features.get("validation")).equalsIgnoreCase("false")) {
               var1.setColor(this.getTriangleColor(0));
            } else {
               var1.setColor(Color.RED);
            }
         } catch (Exception var9) {
            var1.setColor(Color.RED);
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

   private Color getTriangleColor(int var1) {
      if (var1 == 1) {
         return this.full_search ? Color.CYAN : new Color(0, 0, 255);
      } else {
         return this.full_search ? new Color(64, 228, 64) : new Color(0, 128, 0);
      }
   }

   protected void applyFeatures2() {
      super.applyFeatures2();
      applayTCBFeatures(this);
   }

   protected void initFeatures() {
      super.initFeatures();
      this.full_search = this.features.containsKey("full_search");

      try {
         this.isEditing = (Boolean)this.features.get("_painter_");
      } catch (Exception var5) {
         System.out.println("_painter_ ???");
      }

      if (!this.isEditing) {
         if (this.features.containsKey("_zoom_")) {
            super.setZoom((Integer)this.features.get("_zoom_"));
         }

         this.selectedIndex = -1;
         this.handleMatrixThings(true);
         if (this.feature_combo_data == null && this.feature_abev_mask != null) {
            if (this.matrixId != null && !this.matrixId.equals("")) {
               this.handleVectors(this.elemek, this.ertekek);
            }

            try {
               this.compareValuesToMask(this.feature_abev_mask);
            } catch (Exception var4) {
               Tools.eLog(var4, 0);
            }
         } else if (this.feature_combo_data instanceof Object[]) {
            Object[] var1 = (Object[])((Object[])this.feature_combo_data);
            if (var1.length > 1 && (var1[0] instanceof Vector || var1[0] == null) && (var1[1] instanceof Vector || var1[1] == null)) {
               this.elemek = var1[0] != null ? new Vector((Vector)((Vector)var1[0])) : null;
               this.ertekek = var1[1] != null ? new Vector((Vector)((Vector)var1[1])) : null;
               this.handleVectors(this.elemek, this.ertekek);

               try {
                  this.compareValuesToMask(this.feature_abev_mask);
               } catch (Exception var3) {
                  Tools.eLog(var3, 0);
               }
            }
         }

      }
   }

   private static void applayTCBFeatures(ENYKTaggedComboBox var0) {
      Object var1 = var0.getFeatures().get("combo_data");
      var0.feature_combo_data = var1 == null ? null : var1;
      var1 = var0.getFeatures().get("combo_values");
      var0.feature_combo_values = var1 == null ? null : var1;
      var1 = var0.getFeatures().get("combo_items");
      var0.feature_combo_items = var1 == null ? null : var1;
   }

   public void setFeatures(Hashtable var1) {
      super.setFeatures(var1);

      try {
         this.isEditing = (Boolean)var1.get("_painter_");
      } catch (Exception var6) {
         System.out.println("_painter_ ???");
      }

      if (!this.isEditing) {
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

         try {
            this.compareValuesToMask((String)var1.get("abev_mask"));
         } catch (Exception var5) {
            Tools.eLog(var5, 0);
         }

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
      this.setMaxWidth();
      if (!isInicialized) {
         if (this.elemek != null) {
            FocusListener[] var1 = this.getFocusListeners();
            FocusListener[] var2 = var1;
            int var3 = var1.length;

            int var4;
            FocusListener var5;
            for(var4 = 0; var4 < var3; ++var4) {
               var5 = var2[var4];
               this.removeFocusListener(var5);
            }

            this.addFocusListener(this);
            var2 = var1;
            var3 = var1.length;

            for(var4 = 0; var4 < var3; ++var4) {
               var5 = var2[var4];
               this.addFocusListener(var5);
            }

            this.setInputVerifier(this.fciv);
            this.lista.removeAll();
            this.fillLista();
            this.lista.setName("listaafilterhez");
            this.lista.setMaximumSize(new Dimension(100, 50));
            this.lista.setFocusable(true);
            if (this.getKeyListeners().length == 1) {
               this.addKeyListener(this);
               this.addMouseListener(this);
               this.lista.addKeyListener(this);
               this.lista.addMouseListener(this);
            }

            this.lista.setInputVerifier(new ENYKTaggedComboBox.LInputVerifier());
            this.lista.addFocusListener(this);
            this.pm.setFocusable(false);
            this.pm.addMouseListener(this);
            this.pm.addPopupMenuListener(new PopupMenuListener() {
               public void popupMenuCanceled(PopupMenuEvent var1) {
                  ENYKTaggedComboBox.this.mouse = false;
               }

               public void popupMenuWillBecomeInvisible(PopupMenuEvent var1) {
                  ENYKTaggedComboBox.this.mouse = false;
               }

               public void popupMenuWillBecomeVisible(PopupMenuEvent var1) {
               }
            });
            this.pm.add(this.jsp);
            this.initTemp();
            isInicialized = true;
         }
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

   private void compareValuesToMask(String var1) {
      if (var1.length() != 0) {
         if (var1.startsWith("#")) {
            if (!var1.matches("[#]*")) {
               if (this.ertekek != null) {
                  this.mentettErtekek = new Vector(this.ertekek.size());

                  int var2;
                  for(var2 = 0; var2 < this.ertekek.size(); ++var2) {
                     String var3 = (String)this.ertekek.elementAt(var2);
                     this.mentettErtekek.add(var3);
                  }

                  boolean var7 = false;

                  for(int var4 = 0; var4 < this.ertekek.size(); ++var4) {
                     boolean var8 = false;
                     var2 = 0;
                     String var5 = (String)this.ertekek.elementAt(var4);

                     for(int var6 = 0; var6 < var1.length(); ++var6) {
                        if (var1.charAt(var6) != '#') {
                           var5 = var5.substring(0, var2) + var1.charAt(var6) + var5.substring(var2);
                           var8 = true;
                        }

                        ++var2;
                     }

                     if (var8) {
                        this.ertekek.setElementAt(var5, var4);
                     }
                  }

               }
            }
         }
      }
   }

   public int getRecordIndex() {
      try {
         return this.selectedIndex;
      } catch (Exception var2) {
         return -1;
      }
   }

   private String findOriginalValue(String var1) {
      try {
         if (this.mentettErtekek.contains(var1)) {
            return var1;
         } else {
            StringBuffer var2 = new StringBuffer();
            String var3 = this.feature_abev_mask;

            for(int var4 = 0; var4 < var3.length(); ++var4) {
               if (var3.charAt(var4) == '#') {
                  var2.append(var1.charAt(var4));
               }
            }

            return var2.toString();
         }
      } catch (Exception var5) {
         return var1;
      }
   }

   private String findNewValue(String var1) {
      try {
         if (this.ertekek.contains(var1)) {
            return var1;
         } else {
            StringBuffer var2 = new StringBuffer();
            String var3 = this.feature_abev_mask;
            int var4 = 0;

            for(int var5 = 0; var5 < var3.length(); ++var5) {
               if (var3.charAt(var5) == '#') {
                  var2.append(var1.charAt(var4++));
               } else {
                  var2.append(var3.charAt(var5));
               }
            }

            return var2.toString();
         }
      } catch (Exception var6) {
         return var1;
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

   private void setFieldText(Object var1) {
      if (var1 instanceof DoubleListValue) {
         try {
            this.selectedIndex = ((DoubleListValue)var1).index;
            if (this.feature_abev_mask.startsWith("/")) {
               this.setText("/" + this.ertekek.get(this.elemek.indexOf(var1.toString())));
            } else if (this.feature_abev_mask.startsWith("\\")) {
               this.setText("\\" + this.ertekek.get(this.elemek.indexOf(var1.toString())));
            } else {
               this.setText((String)this.ertekek.get(this.elemek.indexOf(var1.toString())));
            }

            this.setValue(this.getText());
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
            this.setValue((String)this.ertekek.elementAt(var2));

            try {
               this.selectedIndex = (Integer)this.indexek.elementAt(var2);
            } catch (Exception var4) {
               this.selectedIndex = -1;
            }
         } catch (Exception var5) {
            this.setText("");
            this.setValue("");
            this.selectedIndex = -1;
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
         if (this.getText().trim().length() == 0) {
            this.initTemp();
            this.lista.setModel(this.tempModel);
            this.lista.removeSelectionInterval(0, this.lista.getModel().getSize());
            var6 = this.lista.getModel().getSize();
            if (var6 == 1) {
               ++var6;
            }
         }

         int var3 = this.searchFirstItemIndex(this.getText().trim());
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
         this.mouse = false;
         if (this.getInputVerifier() == null) {
            this.setInputVerifier(this.fciv);
         }

         int var2 = var1.getKeyCode();
         int var3 = 0;
         if (var2 != 112 && var2 != 113 && var2 != 16 && var2 != 17) {
            if (this.isEnabled()) {
               if (var2 != 27 && var2 != 37 && var2 != 39) {
                  String var4;
                  if (var2 == 10 || var2 == 9 || var2 == 38 || var2 == 40 || var2 == 36 || var2 == 35 || var2 == 33 || var2 == 34 || var2 == 127 || var2 == 8) {
                     if (var2 == 127) {
                        if (var1.getModifiers() == 1) {
                           this.setFieldText("");
                           this.doAfterDelete();
                           return;
                        }

                        if ("".equals(this.getText().trim())) {
                           this.doAfterDelete();
                           return;
                        }
                     }

                     if (this.pm.isVisible()) {
                        if (var2 == 8) {
                           if ("".equals(this.getText().trim())) {
                              this.doAfterDelete();
                           }
                        } else {
                           if (var2 == 10 && var1.getModifiers() == 2 || var2 == 9) {
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
                        }

                        return;
                     }

                     if (var2 == 8) {
                        if ("".equals(this.getText().trim())) {
                           this.doAfterDelete();
                           return;
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

                  var4 = this.maskMinus(this.getText(0, this.getDocument().getLength())).trim();
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
                           var4 = this.maskMinus(this.getText(0, this.getDocument().getLength())).trim();
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
                           var4 = this.maskMinus(this.getText(0, this.getDocument().getLength())).trim();
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

   private String superTrim(String var1) {
      String var2 = this.maskMinus(var1).trim();
      return var2.length() > 0 ? var2.substring(0, var2.length() - 1) : var2;
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
            this.setInputVerifier((InputVerifier)null);
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
            if (!this.mouse) {
               this.shpm(false);
            } else {
               this.setFieldText(this.lista.getSelectedValue());
            }
         } else {
            this.shpm(false);
         }
      } else if (!this.getText().trim().equals("") && !this.engedekeny && !this.ertekek.contains(this.maskMinus(this.getText())) && !this.ertekek.contains(this.getText()) && !this.ertekek.contains(this.getText().trim())) {
         this.setFieldText("");
         this.setValue("");
      } else {
         int var2 = this.oneInstance(this.getText());
         if (var2 > -1) {
            this.setFieldText(this.lista.getSelectedValue());
         }
      }

      if (!this.focusOnField && !this.focusOnList) {
         this.shpm(false);
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

   private void fillLista() {
      this.lista.setListData(this.elemek);
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

   private String maskMinus(String var1) {
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

   public Object getFieldValue() {
      if (this.engedekeny) {
         return super.getFieldValue();
      } else {
         try {
            if (this.ertekek == null) {
               return "";
            } else {
               String var1 = this.findOriginalValue(super.getValue().toString().trim());
               if (!this.feature_abev_mask.startsWith("\\") && !this.feature_abev_mask.startsWith("/")) {
                  if (this.ertekek.indexOf(this.getText().trim()) == -1) {
                     if (this.mentettErtekek == null) {
                        return "";
                     }

                     if (this.mentettErtekek.indexOf(this.getText().trim()) == -1) {
                        return "";
                     }
                  }
               } else if (this.ertekek.indexOf(this.maskMinus(this.getText()).trim()) == -1) {
                  if (this.mentettErtekek == null) {
                     return "";
                  }

                  if (this.mentettErtekek.indexOf(this.getText().trim()) == -1) {
                     return "";
                  }
               }

               return var1;
            }
         } catch (Exception var2) {
            return super.getValue();
         }
      }
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

         for(int var1 = 0; var1 < this.ldl.length; ++var1) {
            this.tempModel.removeListDataListener(this.ldl[var1]);
         }
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
      }

   }

   private void resetLDListeners() {
      try {
         for(int var1 = 0; var1 < this.ldl.length; ++var1) {
            this.tempModel.addListDataListener(this.ldl[var1]);
         }

         this.lista.setModel(this.dummyModel);
         this.lista.setModel(this.tempModel);
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
      }

   }

   private void setInicialized(Vector var1, Vector var2) {
      if (var1 == null && var2 == null) {
         this.initialized = this.elemek == null && this.ertekek == null;
      }

   }

   private class LInputVerifier extends InputVerifier {
      private LInputVerifier() {
      }

      public boolean verify(JComponent var1) {
         if (ENYKTaggedComboBox.this.mouse) {
            ENYKTaggedComboBox.this.mouse = false;
            ENYKTaggedComboBox.this.setFieldText(ENYKTaggedComboBox.this.lista.getSelectedValue());
            ENYKTaggedComboBox.this.grabFocus();
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
         if (ENYKTaggedComboBox.this.maskMinus(ENYKTaggedComboBox.this.getText()).trim().equals("")) {
            return true;
         } else {
            int var2;
            if (ENYKTaggedComboBox.this.pm.isVisible()) {
               if (ENYKTaggedComboBox.this.engedekeny) {
                  var2 = ENYKTaggedComboBox.this.oneInstance(ENYKTaggedComboBox.this.getText());
                  if (var2 > -1) {
                     ENYKTaggedComboBox.this.lista.setSelectedIndex(var2);
                     ENYKTaggedComboBox.this.setFieldText(ENYKTaggedComboBox.this.lista.getSelectedValue());
                  }
               } else {
                  ENYKTaggedComboBox.this.setFieldText(ENYKTaggedComboBox.this.lista.getSelectedValue());
               }

               ENYKTaggedComboBox.this.shpm(false);
               return true;
            } else {
               try {
                  var2 = ENYKTaggedComboBox.this.containsIgnoreCase(ENYKTaggedComboBox.this.maskMinus(ENYKTaggedComboBox.this.getText().trim()));
                  if (ENYKTaggedComboBox.this.maskMinus(ENYKTaggedComboBox.this.getText()).trim().equals("")) {
                     return true;
                  } else {
                     switch(var2) {
                     case -1:
                        ENYKTaggedComboBox.this.setText(ENYKTaggedComboBox.this.getText().toUpperCase());
                        return true;
                     case 0:
                        return true;
                     case 1:
                        ENYKTaggedComboBox.this.setText(ENYKTaggedComboBox.this.getText().toLowerCase());
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
      }

      // $FF: synthetic method
      FCInputVerifier(Object var2) {
         this();
      }
   }
}
