package hu.piller.enykp.gui.viewer;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.settingspanel.BaseSettingsPane;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.gui.model.VisualFieldModel;
import hu.piller.enykp.util.icon.ENYKIconSet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.TabbedPaneUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.metal.MetalTabbedPaneUI;

public class FormViewer extends JPanel {
   public FormModel fm;
   JTabbedPane tp;
   public DynPageViewer pv;
   int prevpage;
   boolean flag;
   boolean flag2;
   boolean flag3;
   int percent = 100;
   private String allPageDisabledMsg = "Minden lap tiltott!";
   private String canTShowDocument = "Nem jeleníthető meg a nyomtatvány!";
   private String errorTitle = "Hiba";
   private String pageDisabledMsg = "A lapot nem kell / nem szabad kitölteni!";
   private String msgTitle = "Üzenet";
   private String okOption = "OK";
   private String forceShowOption = "Mégis megnézem az oldalt";
   private Color currentFGColor;
   private Color defaultCurrentFGColor;
   private Color disabledFGColor;
   private Color selectedFGColor;
   private Color selectedBGColor;
   private boolean tpStateChanging;

   public FormViewer(FormModel var1) {
      this.currentFGColor = Color.BLACK;
      this.defaultCurrentFGColor = Color.BLACK;
      this.disabledFGColor = Color.LIGHT_GRAY;
      this.tpStateChanging = false;
      this.fm = var1;
      this.setName("fv");
      this.percent = MainFrame.thisinstance.mp.getStatuspanel().zoom_slider.getValue();
      String var2 = SettingsStore.getInstance().get("gui", "tablayout");
      JCheckBox var3 = GuiUtil.getANYKCheckBox("");
      this.currentFGColor = var3.getForeground();
      this.defaultCurrentFGColor = var3.getForeground();

      try {
         this.selectedBGColor = (Color)UIManager.get("MenuItem.selectionBackground");
      } catch (Exception var15) {
         this.selectedBGColor = new Color(0, 120, 215);
      }

      try {
         this.selectedFGColor = (Color)UIManager.get("MenuItem.selectionForeground");
      } catch (Exception var14) {
         this.selectedFGColor = Color.BLACK;
      }

      try {
         this.disabledFGColor = (Color)UIManager.get("MenuItem.disabledForeground");
      } catch (Exception var13) {
         this.disabledFGColor = Color.LIGHT_GRAY;
      }

      UIManager.put("TabbedPane.selected", this.selectedBGColor);
      byte var4;
      if (var2 != null && var2.equals("true")) {
         var4 = 0;
      } else {
          var4 = 1;
      }

       byte var5 = 1;
      var2 = SettingsStore.getInstance().get("gui", "tabpos");
      if (var2 != null) {
         boolean var6 = false;

         int var16;
         try {
            var16 = Integer.parseInt(var2);
         } catch (NumberFormatException var12) {
            var16 = 0;
         }

         switch(var16) {
         case 0:
            var5 = 1;
            break;
         case 1:
            var5 = 3;
            break;
         case 2:
            var5 = 4;
            break;
         case 3:
            var5 = 2;
         }
      }

      this.tp = new JTabbedPane(var5, var4) {
         protected void processKeyEvent(KeyEvent var1) {
            super.processKeyEvent(var1);
            Thread var2;
            if (var1.getID() == 401 && var1.isAltDown() && var1.getKeyChar() == 'l') {
               var2 = new Thread(new Runnable() {
                  public void run() {
                     FormViewer.this.pv.pv.setFocus(FormViewer.this.pv.pv.PM.getFirst());
                  }
               });
               var1.consume();
               var2.start();
            }

            if (var1.getID() == 401 && var1.isAltDown() && var1.getKeyCode() == 37) {
               var2 = new Thread(new Runnable() {
                  public void run() {
                     for(int var1 = 0; var1 < FormViewer.this.tp.getTabCount(); ++var1) {
                        if (FormViewer.this.tp.getForegroundAt(var1).equals(FormViewer.this.defaultCurrentFGColor)) {
                           FormViewer.this.tp.setSelectedIndex(var1);
                           break;
                        }
                     }

                  }
               });
               var1.consume();
               var2.start();
            }

            if (var1.getID() == 401 && var1.isAltDown() && var1.getKeyCode() == 39) {
               var2 = new Thread(new Runnable() {
                  public void run() {
                     for(int var1 = FormViewer.this.tp.getTabCount() - 1; var1 != -1; --var1) {
                        if (FormViewer.this.tp.getForegroundAt(var1).equals(FormViewer.this.defaultCurrentFGColor)) {
                           FormViewer.this.tp.setSelectedIndex(var1);
                           break;
                        }
                     }

                  }
               });
               var1.consume();
               var2.start();
            }

         }

         public void setUI(TabbedPaneUI var1) {
            super.setUI(new MetalTabbedPaneUI() {
               protected JButton createScrollButton(int var1) {
                  if (var1 != 5 && var1 != 1 && var1 != 3 && var1 != 7) {
                     throw new IllegalArgumentException("Direction must be one of: SOUTH, NORTH, EAST or WEST");
                  } else {
                     return new BasicArrowButton(var1, UIManager.getColor("TabbedPane.selected"), UIManager.getColor("TabbedPane.shadow"), UIManager.getColor("TabbedPane.darkShadow"), UIManager.getColor("TabbedPane.highlight")) {
                        public Dimension getPreferredSize() {
                           return new Dimension(0, 0);
                        }
                     };
                  }
               }
            });
         }

         public void setSelectedIndex(int var1) {
            super.setSelectedIndex(var1);
            if (!FormViewer.this.tpStateChanging) {
               MainFrame.thisinstance.mp.forceDisabledPageShowing = false;
            }

         }
      };
      this.tp.setName("tp");
      this.tp.removeAll();
      Dimension var8 = new Dimension(2, 2);

      int var9;
      for(var9 = 0; var9 < this.fm.size(); ++var9) {
         PageModel var10 = this.fm.get(var9);
         JPanel var11 = new JPanel();
         var11.setMinimumSize(var8);
         var11.setMaximumSize(var8);
         var11.setPreferredSize(var8);
         var11.setSize(var8);
         this.tp.addTab(var10.name, var11);
      }

      this.setTabStatus();
      this.invisiblepages();
      if (!this.setactive()) {
         GuiUtil.showMessageDialog((Component)null, this.allPageDisabledMsg + "\n" + this.canTShowDocument, this.errorTitle, 0);
      } else {
         var9 = GuiUtil.getCommonItemHeight() + 2;
         this.tp.setMinimumSize(new Dimension(80, 80));
         this.pv.zoom(this.percent);
         this.setLayout(new BorderLayout());
         if (var5 != 1 && var5 != 3) {
            if (var5 == 2) {
               if (var4 == 1) {
                  this.done_scroll_or_wrap(var5, var4, var9);
               } else {
                  this.add(this.tp, "West");
               }
            } else if (var5 == 4) {
               if (var4 == 1) {
                  this.done_scroll_or_wrap(var5, var4, var9);
               } else {
                  this.add(this.tp, "East");
               }
            }
         } else {
            this.done_scroll_or_wrap(var5, var4, var9);
         }

         this.add(this.pv, "Center");
         byte finalVar = var5;
         this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent var1) {
               try {
                  if (var4 == 0) {
                     int var2;
                     if (finalVar != 1 && finalVar != 3) {
                        var2 = FormViewer.this.tp.getUI().getTabBounds(FormViewer.this.tp, 0).width;
                        int var3 = 5 + var2 * FormViewer.this.tp.getTabRunCount();
                        FormViewer.this.tp.setPreferredSize(new Dimension(var3, 1));
                     } else {
                        var2 = 10 + 20 * FormViewer.this.tp.getTabRunCount();
                        FormViewer.this.tp.setPreferredSize(new Dimension(1, var2));
                     }

                     FormViewer.this.invalidate();
                     FormViewer.this.validate();
                     FormViewer.this.tp.invalidate();
                     FormViewer.this.tp.revalidate();
                  }
               } catch (Exception var4x) {
               }

            }
         });
         this.tp.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent var1) {
               if (var1.getKeyCode() == 39) {
                  FormViewer.this.flag2 = true;
               }

               if (var1.getKeyCode() == 37) {
                  FormViewer.this.flag3 = true;
               }

               if (var1.getKeyCode() == 40) {
                  FormViewer.this.flag2 = true;
               }

               if (var1.getKeyCode() == 38) {
                  FormViewer.this.flag3 = true;
               }

            }
         });
         this.tp.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent var1) {
               FormViewer.this.tpStateChanging = true;
               MainFrame.thisinstance.mp.forceDisabledPageShowing = false;
               FormViewer.this.currentFGColor = FormViewer.this.tp.getForegroundAt(FormViewer.this.prevpage);
               if (FormViewer.this.flag) {
                  FormViewer.this.flag = false;
                  FormViewer.this.tpStateChanging = false;
               } else {
                  if (!MainFrame.thisinstance.mp.isReadonlyMode() && !MainFrame.rogzitomode && !FormViewer.this.fm.bm.autofill) {
                     if (!FormViewer.this.pv.pv.leave_component()) {
                        FormViewer.this.back();
                        FormViewer.this.tpStateChanging = false;
                        return;
                     }

                     CalculatorManager var2 = new CalculatorManager();
                     Object[] var3 = var2.check_page(FormViewer.this.fm.get(FormViewer.this.fm.getPageindex(FormViewer.this.tp.getTitleAt(FormViewer.this.tp.getSelectedIndex()))).pid);
                     boolean var4 = false;
                     String var5 = "";
                     if (var3[1] != null && var3[1] instanceof Boolean) {
                        var4 = (Boolean)var3[1];
                        var5 = (String)var3[2];
                        if (var5 != null) {
                           var5 = var5.replaceAll("#13", "\n");
                        }
                     }

                     if (!var4) {
                        int var8;
                        if (FormViewer.this.flag2) {
                           var8 = FormViewer.this.tp.getSelectedIndex() + 1;
                           if (var8 == FormViewer.this.tp.getTabCount()) {
                              var8 = 0;
                           }

                           FormViewer.this.tp.setSelectedIndex(var8);
                           FormViewer.this.tpStateChanging = false;
                           return;
                        }

                        if (FormViewer.this.flag3) {
                           var8 = FormViewer.this.tp.getSelectedIndex() - 1;
                           if (var8 == -1) {
                              var8 = FormViewer.this.tp.getTabCount() - 1;
                           }

                           FormViewer.this.tp.setSelectedIndex(var8);
                           FormViewer.this.tpStateChanging = false;
                           return;
                        }

                        var5 = FormViewer.this.pageDisabledMsg + "\n\n" + var5;
                        Object[] var6 = new Object[]{FormViewer.this.okOption, FormViewer.this.forceShowOption};
                        int var7 = JOptionPane.showOptionDialog(FormViewer.this.getRootPane().getTopLevelAncestor(), var5, FormViewer.this.msgTitle, 0, 2, (Icon)null, var6, var6[0]);
                        if (var7 == 0 || var7 == -1) {
                           FormViewer.this.back();
                           FormViewer.this.tpStateChanging = false;
                           return;
                        }

                        MainFrame.thisinstance.mp.forceDisabledPageShowing = true;
                     }

                     FormViewer.this.flag2 = false;
                     FormViewer.this.flag3 = false;
                  }

                  if (MainFrame.rogzitomode || FormViewer.this.fm.bm.autofill) {
                     FormViewer.this.pv.pv.leave_component_nocheck();
                  }

                  if (!MainFrame.thisinstance.mp.forceDisabledPageShowing && !MainFrame.thisinstance.mp.isReadonlyMode() && !MainFrame.thisinstance.mp.funcreadonly) {
                     FormViewer.this.tp.setForegroundAt(FormViewer.this.prevpage, FormViewer.this.currentFGColor);
                     FormViewer.this.tp.setForegroundAt(FormViewer.this.tp.getSelectedIndex(), FormViewer.this.selectedFGColor);
                  }

                  FormViewer.this.pv.load(FormViewer.this.fm.get(FormViewer.this.fm.getPageindex(FormViewer.this.tp.getTitleAt(FormViewer.this.tp.getSelectedIndex()))));
                  FormViewer.this.pv.zoom(FormViewer.this.percent);
                  FormViewer.this.pv.repaint();
                  FormViewer.this.prevpage = FormViewer.this.tp.getSelectedIndex();
               }
            }
         });
         this.flag = false;
         String var17 = SettingsStore.getInstance().get("gui", "mezőszámítás");
         if (var17 != null && var17.equals("true")) {
            BaseSettingsPane.calchelper(true, this.fm.bm);
         } else {
            BaseSettingsPane.calchelper(false, this.fm.bm);
         }

         if (!this.fm.bm.emptyprint) {
            MainFrame.thisinstance.mp.set_kiut_url(this.fm);
         }

         this.highlightFirstTab();
         this.tpStateChanging = false;
      }
   }

   private void done_scroll_or_wrap(int var1, int var2, int var3) {
      JPanel var4 = new JPanel(new BorderLayout());
      var4.add(this.tp);
      if (var2 == 1) {
         JPanel var5 = this.getschandler();
         var4.add(var5, var1 != 1 && var1 != 3 ? "South" : "East");
      }

      if (var1 != 2 && var1 != 4) {
         this.tp.setPreferredSize(new Dimension(1, var3));
      } else {
         this.tp.setPreferredSize(new Dimension(this.tp.getUI().getTabBounds(this.tp, 0).width, var3));
      }

      this.add(var4, var1 == 1 ? "North" : (var1 == 3 ? "South" : (var1 == 2 ? "West" : "East")));
   }

   private JPanel getschandler() {
      final ActionMap var1 = this.tp.getActionMap();
      JPanel var2 = new JPanel();
      JButton var3 = new JButton(ENYKIconSet.getInstance().get("page_tobb_lapozas_balra"));
      var3.setToolTipText("Első");
      var3.setPreferredSize(GuiUtil.getButtonSizeByIcon(var3));
      var3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            int var2 = FormViewer.this.fm.size();

            for(int var3 = 0; var3 < var2; ++var3) {
               Action var4 = var1.get("scrollTabsBackwardAction");
               if (var4 != null && var4.isEnabled()) {
                  var4.actionPerformed(new ActionEvent(FormViewer.this.tp, 1001, (String)null, var1x.getWhen(), var1x.getModifiers()));
               }
            }

         }
      });
      var2.add(var3);
      JButton var4 = new JButton(ENYKIconSet.getInstance().get("page_egy_lapozas_balra"));
      var4.setToolTipText("Előző");
      var4.setPreferredSize(GuiUtil.getButtonSizeByIcon(var4));
      var4.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            Action var2 = var1.get("scrollTabsBackwardAction");
            if (var2 != null && var2.isEnabled()) {
               var2.actionPerformed(new ActionEvent(FormViewer.this.tp, 1001, (String)null, var1x.getWhen(), var1x.getModifiers()));
            }

         }
      });
      var2.add(var4);
      JButton var5 = new JButton(ENYKIconSet.getInstance().get("page_egy_lapozas_jobbra"));
      var5.setToolTipText("Következő");
      var5.setPreferredSize(GuiUtil.getButtonSizeByIcon(var5));
      var5.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            Action var2 = var1.get("scrollTabsForwardAction");
            if (var2 != null && var2.isEnabled()) {
               var2.actionPerformed(new ActionEvent(FormViewer.this.tp, 1001, (String)null, var1x.getWhen(), var1x.getModifiers()));
            }

         }
      });
      var2.add(var5);
      JButton var6 = new JButton(ENYKIconSet.getInstance().get("page_tobb_lapozas_jobbra"));
      var6.setToolTipText("Utolsó");
      var6.setPreferredSize(GuiUtil.getButtonSizeByIcon(var6));
      var6.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            int var2 = FormViewer.this.fm.size();

            for(int var3 = 0; var3 < var2; ++var3) {
               Action var4 = var1.get("scrollTabsForwardAction");
               if (var4 != null && var4.isEnabled()) {
                  var4.actionPerformed(new ActionEvent(FormViewer.this.tp, 1001, (String)null, var1x.getWhen(), var1x.getModifiers()));
               }
            }

         }
      });
      var2.add(var6);
      return var2;
   }

   private void back() {
      Thread var1 = new Thread(new Runnable() {
         public void run() {
            FormViewer.this.flag = true;
            FormViewer.this.tp.setSelectedIndex(FormViewer.this.prevpage);
         }
      });
      var1.start();
   }

   public void zoom(int var1) {
      this.percent = var1;
      if (this.pv != null) {
         this.pv.zoom(var1);
      }

   }

   public void gotoField(StoreItem var1) {
      PageModel var2 = (PageModel)this.fm.fids_page.get(var1.code);
      if (var2 != null) {
         if (!this.ceq(this.tp.getForegroundAt(this.findpos(this.fm.get(var2))), this.disabledFGColor)) {
            int var3 = this.findpos(this.fm.get(var2));
            if (var3 != -1) {
               MainFrame.thisinstance.mp.forceDisabledPageShowing = false;
               this.tp.setSelectedIndex(var3);
               DataFieldModel var4 = (DataFieldModel)this.fm.fids.get(var1.code);
               if ((var4.role & VisualFieldModel.getmask()) != 0) {
                  this.pv.setFocus(var4, var1.index);
               }
            }
         }
      }
   }

   public void gotoPage(int var1) {
      if (var1 >= 0) {
         this.setTabStatus();
         this.tp.setSelectedIndex(var1);
      }
   }

   public void gotoPage(String var1, int var2) {
      PageModel var3 = (PageModel)this.fm.names_page.get(var1);
      if (var3 != null) {
         if (!this.ceq(this.tp.getForegroundAt(this.findpos(this.fm.get(var3))), this.disabledFGColor)) {
            int var4 = this.findpos(this.fm.get(var3));
            if (var4 != -1) {
               MainFrame.thisinstance.mp.forceDisabledPageShowing = false;
               this.tp.setSelectedIndex(var4);
               this.pv.setDynindex(var2);
            }
         }
      }
   }

   public int getSelectedIndex() {
      return this.tp.getSelectedIndex();
   }

   public void done_after_readonly() {
      if (!MainFrame.thisinstance.mp.isReadonlyMode()) {
         this.gotoPage(this.get1enabled());
      }

      this.pv.done_after_readonly();
   }

   public void setTabStatus(Boolean[] var1) {
      for(int var2 = 0; var2 < this.tp.getTabCount(); ++var2) {
         boolean var3 = var1[var2];
         int var4 = this.fm.getPageindex(this.tp.getTitleAt(var2));
         this.tp.setForegroundAt(var4, var3 ? Color.BLACK : Color.GRAY);
      }

   }

   public void setTabStatus() {
      try {
         int var1;
         if (MainFrame.rogzitomode || this.fm.bm.autofill) {
            for(var1 = 0; var1 < this.fm.size(); ++var1) {
               int var7 = this.findpos(var1);
               if (var7 != -1) {
                  this.tp.setForegroundAt(var7, this.currentFGColor);
               }
            }

            return;
         }

         for(var1 = 0; var1 < this.fm.size(); ++var1) {
            Object[] var2 = CalculatorManager.getInstance().check_page(this.fm.get(var1).pid);
            if (var2[1] != null) {
               boolean var3 = (Boolean)var2[1];

               try {
                  int var4 = this.findpos(var1);
                  if (var4 != -1) {
                     this.tp.setForegroundAt(var4, var3 ? this.defaultCurrentFGColor : this.disabledFGColor);
                  }
               } catch (Exception var5) {
               }
            }
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public boolean getEnabled(int var1) {
      int var2 = this.findpos(var1);
      if (var2 == -1) {
         return false;
      } else {
         return this.tp.getForegroundAt(var2).equals(this.defaultCurrentFGColor);
      }
   }

   private int findpos(int var1) {
      String var2 = this.fm.get(var1).name;
      int var3 = -1;

      for(int var4 = 0; var4 < this.tp.getTabCount(); ++var4) {
         if (this.tp.getTitleAt(var4).equals(var2)) {
            var3 = var4;
            break;
         }
      }

      return var3;
   }

   public void invisiblepages() {
      for(int var1 = this.tp.getTabCount() - 1; -1 < var1; --var1) {
         boolean var2 = false;
         if (this.ceq(this.tp.getForegroundAt(var1), this.disabledFGColor)) {
            try {
               String var3 = (String)this.fm.get(var1).xmlht.get("hwdisabled");
               if (var3.equals("yes")) {
                  var2 = true;
               }
            } catch (Exception var4) {
            }
         }

         try {
            int var6 = this.fm.get(var1).role;
            if ((var6 & this.fm.get(var1).getmask()) == 0) {
               var2 = true;
            }
         } catch (Exception var5) {
         }

         if (var2) {
            this.tp.removeTabAt(var1);
         }
      }

   }

   public void destroy() {
      if (this.fm != null) {
         this.fm.destroy();
      }

      this.fm = null;
      if (this.pv != null) {
         if (this.pv.pv != null) {
            this.pv.pv.destroy();
            this.pv.remove(this.pv.pv);
         }

         this.remove(this.pv);
      }

      if (this.tp != null) {
         ChangeListener[] var1 = this.tp.getChangeListeners();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            ChangeListener var3 = var1[var2];
            this.tp.removeChangeListener(var3);
         }

         this.remove(this.tp);
      }

   }

   public boolean setactive() {
      try {
         for(int var1 = 0; var1 < this.tp.getTabCount(); ++var1) {
            if (this.ceq(this.tp.getForegroundAt(var1), this.defaultCurrentFGColor)) {
               this.tp.setSelectedIndex(var1);
               int var2 = this.fm.getPageindex(this.tp.getTitleAt(var1));
               this.pv = new DynPageViewer(this.fm.get(var2));
               this.prevpage = var2;
               return true;
            }
         }
      } catch (Exception var3) {
      }

      return false;
   }

   public int get1enabled() {
      try {
         for(int var1 = 0; var1 < this.tp.getTabCount(); ++var1) {
            if (this.ceq(this.tp.getForegroundAt(var1), this.defaultCurrentFGColor)) {
               return var1;
            }
         }
      } catch (Exception var2) {
      }

      return -1;
   }

   public JTabbedPane getTp() {
      return this.tp;
   }

   private void highlightFirstTab() {
      for(int var1 = 0; var1 < this.fm.size(); ++var1) {
         if (this.ceq(this.tp.getForegroundAt(var1), this.defaultCurrentFGColor)) {
            this.tp.setForegroundAt(var1, this.selectedFGColor);
            return;
         }
      }

   }

   private boolean ceq(Color var1, Color var2) {
      return var1.getRGB() == var2.getRGB();
   }
}
