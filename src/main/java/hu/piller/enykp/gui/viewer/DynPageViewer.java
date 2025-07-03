package hu.piller.enykp.gui.viewer;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.filepanels.filepanel.UpArrow;
import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.TableFilterPanel;
import hu.piller.enykp.alogic.filepanels.tablesorter.TableSorter;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.GUI_Datastore;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.gui.viewer.pagecounter.JPageCounter;
import hu.piller.enykp.gui.viewer.pagecounter.PageChangeEvent;
import hu.piller.enykp.interfaces.IDataField;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.util.base.Tools;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

public class DynPageViewer extends JPanel {
   public PageViewer pv;
   PageModel pm;
   JPanel dynpanel;
   JScrollPane sc;
   JPageCounter PG;
   DynCopy DC;
   boolean nofire;

   public DynPageViewer() {
      this.init();
   }

   private void init() {
      this.setLayout(new BorderLayout());
      this.setName("dpv");
      this.pv = new PageViewer();
      this.dynpanel = new JPanel();
      this.dynpanel.setLayout(new BoxLayout(this.dynpanel, 0));
      this.add(this.dynpanel, "North");
      this.sc = new JScrollPane(this.pv);
      InputMap var1 = this.sc.getInputMap(1);
      var1.put(KeyStroke.getKeyStroke("ctrl END"), "null");
      var1.put(KeyStroke.getKeyStroke("ctrl HOME"), "null");
      this.add(this.sc);
      this.nofire = false;
   }

   public DynPageViewer(PageModel var1) {
      this.init();
      this.load(var1);
   }

   public void load(PageModel var1) {
      this.pm = var1;
      this.pv.load(var1);

      try {
         WatermarkViewport var2 = new WatermarkViewport(this.pv, MainFrame.thisinstance.mp.forceDisabledPageShowing);
         this.sc.setViewport(var2);
      } catch (Exception var10) {
         System.out.println("A képlettel tiltott lapot kizárólag megtekintés céljából mutatjuk!");
      }

      if (var1.dynamic) {
         if (this.PG == null) {
            this.dynpanel.add(Box.createHorizontalGlue());
            JButton var11 = new JButton("Választ");
            var11.setFocusable(false);
            var11.setToolTipText("A kiválasztott mező tartalma szerint kereshető lista megjelenítése.");
            var11.setSize(new Dimension(GuiUtil.getW(var11, var11.getText()), GuiUtil.getCommonItemHeight() + 2));
            var11.setMinimumSize(new Dimension(10, GuiUtil.getCommonItemHeight() + 2));
            var11.setPreferredSize(var11.getSize());
            var11.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent var1) {
                  DynPageViewer.this.showDialog(MainFrame.thisinstance);
               }
            });
            this.dynpanel.add(var11);
            this.dynpanel.add(Box.createHorizontalStrut(10));
            this.DC = new DynCopy();
            this.dynpanel.add(this.DC);
            this.DC.addChangeListener(new ChangeListener() {
               public void stateChanged(ChangeEvent var1) {
                  if (var1.getSource() instanceof JTextField) {
                     JTextField var2 = (JTextField)var1.getSource();
                     String var3 = var2.getText();

                     int var4;
                     try {
                        var4 = Integer.parseInt(var3);
                     } catch (NumberFormatException var6) {
                        var4 = 1;
                     }

                     if (0 < var4) {
                        DynPageViewer.this.addaction(var4);
                     }
                  }

               }
            });
            this.PG = new JPageCounter();
            this.PG.setMaximumSize(this.PG.getPreferredSize());
            this.dynpanel.add(this.PG);
            if (MainFrame.thisinstance.mp.readonlymode || MainFrame.readonlymodefromubev) {
               this.PG.add_button.setEnabled(false);
               this.PG.remove_button.setEnabled(false);
               this.DC.setreadonly(true);
            }

            if ("1".equals(MainFrame.opmode)) {
               this.PG.remove_button.setEnabled(false);
            }

            this.PG.addSpinnerChangeListener(new ChangeListener() {
               public void stateChanged(ChangeEvent var1) {
                  if (!DynPageViewer.this.nofire) {
                     DynPageViewer.this.pv.leave_component_nocheck();
                     PageChangeEvent var2 = (PageChangeEvent)var1;
                     if (var2.getEventType().equals("new_page")) {
                        DynPageViewer.this.addaction();
                     } else if (var2.getEventType().equals("delete_page")) {
                        DynPageViewer.this.removeaction();
                     } else {
                        try {
                           int var3 = (Integer)((JSpinner)var1.getSource()).getValue();
                           DynPageViewer.this.pv.setActivelap(var3 - 1);
                           DynPageViewer.this.refresh();
                        } catch (Exception var4) {
                        }

                     }
                  }
               }
            });
         }

         this.PG.add_button.setEnabled(!MainFrame.thisinstance.mp.forceDisabledPageShowing);
         if ("1".equals(MainFrame.opmode)) {
            this.PG.remove_button.setEnabled(false);
         } else {
            this.PG.remove_button.setEnabled(!MainFrame.thisinstance.mp.forceDisabledPageShowing);
         }

         this.DC.setreadonly(MainFrame.thisinstance.mp.forceDisabledPageShowing);
         if (MainFrame.thisinstance.mp.readonlymode || MainFrame.readonlymodefromubev) {
            this.PG.add_button.setEnabled(false);
            this.PG.remove_button.setEnabled(false);
            this.DC.setreadonly(true);
         }

         int var12 = this.pm.getFormModel().get(this.pm);
         int var3 = ((int[])((int[])((Elem)this.pm.getFormModel().getBookModel().cc.getActiveObject()).getEtc().get("pagecounts")))[var12];
         this.pv.lapcount = var3;
         this.pv.dynindex = 0;
         this.PG.setMaximum(this.pm.maxpage);
         this.PG.setMinimum(1);
         this.PG.setValue(this.pv.lapcount);
         this.nofire = true;
         this.PG.setSpinnerValue(new Integer(this.pv.dynindex + 1));
         this.nofire = false;
         InputMap var4 = this.getInputMap(1);
         ActionMap var5 = this.getActionMap();
         String var6 = "dynPageUp";
         AbstractAction var7 = new AbstractAction(var6, (Icon)null) {
            public void actionPerformed(ActionEvent var1) {
               DataFieldModel var2 = DynPageViewer.this.pv.current_df;
               int var3 = DynPageViewer.this.PG.spinnerUp();
               if (var3 > -1 && var2 != null) {
                  DynPageViewer.this.setFocus(var2, var3 - 1);
               }

            }
         };
         if ("0".equals(MainFrame.role)) {
            var4.put(KeyStroke.getKeyStroke("F9"), var6);
            var4.put(KeyStroke.getKeyStroke("ctrl shift F9"), var6);
            var5.put(var6, var7);
         }

         String var8 = "dynPageDown";
         AbstractAction var9 = new AbstractAction(var8, (Icon)null) {
            public void actionPerformed(ActionEvent var1) {
               DataFieldModel var2 = DynPageViewer.this.pv.current_df;
               int var3 = DynPageViewer.this.PG.spinnerDown();
               if (var3 > -1 && var2 != null) {
                  DynPageViewer.this.setFocus(var2, var3 - 1);
               }

            }
         };
         if ("0".equals(MainFrame.role)) {
            var4.put(KeyStroke.getKeyStroke("ctrl F9"), var8);
            var5.put(var8, var9);
         }

         this.dynpanel.setVisible(true);
      } else {
         this.dynpanel.setVisible(false);
      }

      this.refresh();
   }

   private void addaction() {
      try {
         if ("1".equals(MainFrame.opmode)) {
            this.pv.addLapafterall();
            this.nofire = true;
            this.PG.setSpinnerValue(new Integer(this.pv.lapcount));
            this.nofire = false;
         } else {
            this.pv.addLap();
         }

         if (this.pv.dynindex + 1 != this.pv.lapcount) {
            Vector var1 = this.pm.y_sorted_df;
            GUI_Datastore var2 = this.getDatastore();

            for(int var3 = 0; var3 < var1.size(); ++var3) {
               DataFieldModel var4 = (DataFieldModel)var1.get(var3);
               var2 = (GUI_Datastore)((Elem)this.pm.getFormModel().getBookModel().cc.getActiveObject()).getRef();
               var2.shift(var4.key, this.pv.dynindex, 1, this.pv.lapcount - 2);
            }

            var2.setStatusFlag(new Boolean[]{Boolean.TRUE});
         }

         CalculatorManager.getInstance().page_calc(this.pv.PM.pid, this.pv.dynindex);
         CalculatorManager.getInstance().do_dpage_count();
         MainFrame.thisinstance.mp.getDMFV().fv.setTabStatus();
         this.refresh();
      } catch (Exception var5) {
      }

   }

   private void addaction(int var1) {
      if (!"1".equals(MainFrame.opmode)) {
         int var2 = Math.min(var1, this.pv.PM.maxpage - this.pv.lapcount);
         if (var2 != 0) {
            int var3 = this.pv.dynindex;

            for(int var4 = 0; var4 < var2; ++var4) {
               this.pv.addLap();
               Vector var5;
               GUI_Datastore var6;
               int var7;
               DataFieldModel var8;
               String var9;
               if (this.pv.dynindex + 1 != this.pv.lapcount) {
                  var5 = this.pm.y_sorted_df;
                  var6 = this.getDatastore();

                  for(var7 = 0; var7 < var5.size(); ++var7) {
                     var8 = (DataFieldModel)var5.get(var7);
                     var6 = (GUI_Datastore)((Elem)this.pm.getFormModel().getBookModel().cc.getActiveObject()).getRef();
                     var6.shift(var8.key, this.pv.dynindex, 1, this.pv.lapcount - 2);
                     var9 = var6.get(var3 + "_" + var8.key);
                     if (var9 != null && var9.length() != 0) {
                        var6.set(this.pv.dynindex + "_" + var8.key, var9);
                     }
                  }

                  var6.setStatusFlag(new Boolean[]{Boolean.TRUE});
               } else {
                  var5 = this.pm.y_sorted_df;
                  var6 = this.getDatastore();

                  for(var7 = 0; var7 < var5.size(); ++var7) {
                     var8 = (DataFieldModel)var5.get(var7);
                     var6 = (GUI_Datastore)((Elem)this.pm.getFormModel().getBookModel().cc.getActiveObject()).getRef();
                     var9 = var6.get(var3 + "_" + var8.key);
                     if (var9 != null && var9.length() != 0) {
                        var6.set(this.pv.dynindex + "_" + var8.key, var9);
                     }
                  }

                  var6.setStatusFlag(new Boolean[]{Boolean.TRUE});
               }

               CalculatorManager.getInstance().page_calc(this.pv.PM.pid, this.pv.dynindex);
            }

            CalculatorManager.getInstance().do_dpage_count();
            this.pv.dynindex = var3 + 1;
            this.nofire = true;
            this.PG.setValue(this.pv.lapcount);
            this.PG.setSpinnerValue(new Integer(this.pv.dynindex + 1));
            this.nofire = false;
            MainFrame.thisinstance.mp.getDMFV().fv.setTabStatus();
            this.refresh();
         }
      }
   }

   private void removeaction() {
      try {
         Vector var1 = this.pm.y_sorted_df;
         GUI_Datastore var2 = this.getDatastore();

         for(int var3 = 0; var3 < var1.size(); ++var3) {
            DataFieldModel var4 = (DataFieldModel)var1.get(var3);
            var2.shift(var4.key, this.pv.dynindex, -1, this.pv.lapcount - 1);
         }

         var2.setStatusFlag(new Boolean[]{Boolean.TRUE});
         this.pv.removeLap();
         CalculatorManager.getInstance().do_dpage_count();
         MainFrame.thisinstance.mp.getDMFV().fv.setTabStatus();
         this.refresh();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   private GUI_Datastore getDatastore() {
      GUI_Datastore var1 = (GUI_Datastore)((Elem)this.pm.getFormModel().getBookModel().cc.getActiveObject()).getRef();
      return var1;
   }

   public void refresh() {
      this.pv.refresh();
   }

   public void zoom(int var1) {
      if (this.pv != null) {
         this.pv.zoom(var1);

         try {
            WatermarkViewport var2 = new WatermarkViewport(this.pv, MainFrame.thisinstance.mp.forceDisabledPageShowing);
            this.sc.setViewport(var2);
         } catch (Exception var3) {
            System.out.println("A képlettel tiltott lapot kizárólag megtekintés céljából mutatjuk!");
         }
      }

   }

   public void setFocus(DataFieldModel var1, int var2) {
      if (!var1.readonly) {
         if (this.dynpanel.isVisible()) {
            this.PG.setSpinnerValue(new Integer(var2 + 1));
         }

         this.pv.setFocus(var1);
      }
   }

   public void setDynindex(int var1) {
      if (this.dynpanel.isVisible()) {
         this.PG.setSpinnerValue(new Integer(var1 + 1));
      }

   }

   public void done_after_readonly() {
      try {
         if (MainFrame.thisinstance.mp.readonlymode) {
            this.PG.add_button.setEnabled(false);
            this.PG.remove_button.setEnabled(false);
            this.DC.setreadonly(true);
         } else {
            this.PG.add_button.setEnabled(true);
            this.PG.remove_button.setEnabled(true);
            this.DC.setreadonly(false);
         }
      } catch (Exception var2) {
      }

   }

   public void showDialog(JFrame var1) {
      if (this.pv.lapcount != 1) {
         KeyboardFocusManager var2 = KeyboardFocusManager.getCurrentKeyboardFocusManager();
         Component var3 = var2.getPermanentFocusOwner();
         String var4 = "";
         String var5 = "";
         String var6 = "";
         if (var3 instanceof IDataField) {
            PageViewer var7 = (PageViewer)var3.getParent();
            var5 = var7.current_df.key;
            var6 = ((IDataField)var3).getFieldValue().toString();
         }

         if (!"".equals(var5)) {
            this.pv.leave_component_nocheck();
            Vector var30 = new Vector();
            var30.add("Sorszám");
            var30.add("Mezőérték");
            DefaultTableModel var8 = new DefaultTableModel(var30, 0);

            String var12;
            for(int var9 = 0; var9 < this.pv.lapcount; ++var9) {
               Vector var10 = new Vector();
               var10.add(new Integer(var9 + 1));
               IDataStore var11 = this.pv.getPM().getFormModel().bm.get_datastore();
               var12 = var11.get(var9 + "_" + var5);
               if (var12 == null) {
                  var12 = "";
               }

               if ("false".equals(var12)) {
                  var12 = "";
               }

               if ("true".equals(var12)) {
                  var12 = "X";
               }

               var10.add(var12);
               var8.addRow(var10);
            }

            if (var8 != null) {
               final JTable var31 = new JTable(var8) {
                  public boolean isCellEditable(int var1, int var2) {
                     return false;
                  }
               };
               var31.setSelectionMode(0);
               var31.addMouseListener(new MouseAdapter() {
                  public void mouseClicked(MouseEvent var1) {
                     if (var1.getClickCount() == 2) {
                        DynPageViewer.this.choose_done(var31);
                     }

                  }
               });
               final TableSorter var32 = new TableSorter();
               var32.attachTable(var31);
               var32.setSortEnabled(true);
               JScrollPane var33 = new JScrollPane(var31);
               var12 = "Lapok  ( " + var8.getRowCount() + " db. )";
               final JDialog var13 = new JDialog((Frame)(var1 == null ? MainFrame.thisinstance : var1), var12, true);
               var13.setLayout(new BorderLayout());
               var13.getContentPane().add(var33, "Center");
               final TableFilterPanel var14 = new TableFilterPanel((JTable)null);
               JLabel var15 = (JLabel)var14.getComponent("filter_title_lbl");
               final JPanel var16 = (JPanel)var14.getComponent("filter_btn_panel");
               final int var17 = (int)(var15.getPreferredSize().getHeight() + var16.getPreferredSize().getHeight());
               final int var18 = (int)var15.getPreferredSize().getHeight();
               final int var19 = GuiUtil.getW(var15, "WWW Szűrési feltételek (szűrés bekapcsolva) WWW");
               var31.getColumnModel().getColumn(0).setPreferredWidth(GuiUtil.getW("WWWWW"));
               var31.getColumnModel().getColumn(1).setPreferredWidth(var19 - var31.getColumnModel().getColumn(0).getPreferredWidth() - 20);
               var14.getComponent("file_filters_lbl").setVisible(false);
               var14.getComponent("file_filters_scp").setVisible(false);
               final Component var21 = var14.getComponent("file_filters_toggle_btn");
               Component var22 = var14.getComponent("filter_title_lbl");
               final MouseAdapter var23 = new MouseAdapter() {
                  public void mouseClicked(MouseEvent var1) {
                     JLabel var2 = (JLabel)var21;
                     if (var2.getIcon() instanceof UpArrow) {
                        var14.setPreferredSize(new Dimension(var19, var17 + 3 * (GuiUtil.getCommonItemHeight() + 2)));
                        var16.setVisible(true);
                     } else {
                        var14.setPreferredSize(new Dimension(var19, var18));
                        var16.setVisible(false);
                     }

                     var13.getContentPane().invalidate();
                     var13.getContentPane().validate();
                     var13.getContentPane().repaint();
                  }
               };
               var21.addMouseListener(var23);
               var22.addMouseListener(var23);
               var14.setPreferredSize(new Dimension(var19, var17 + 3 * (GuiUtil.getCommonItemHeight() + 2)));
               Vector var24 = new Vector();
               var24.add(new Integer(1));
               Object[] var25 = new Object[]{var31, var24, new Integer(2)};
               var14.getBusinessHandler().initials(var25);
               var13.getContentPane().add(var14, "North");
               JButton var26 = new JButton("Bezár");
               var26.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent var1) {
                     try {
                        var21.removeMouseListener(var23);
                        var32.detachTable();
                        var14.destroy();
                        var13.hide();
                        var13.dispose();
                     } catch (Exception var3) {
                        Tools.eLog(var3, 0);
                     }

                  }
               });
               JButton var27 = new JButton("Kiválaszt");
               var27.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent var1) {
                     DynPageViewer.this.choose_done(var31);
                  }
               });
               JButton var28 = new JButton("Táblázat nyomtatása");
               var28.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent var1) {
                     try {
                        var31.print();
                     } catch (PrinterException var3) {
                        var3.printStackTrace();
                     }

                  }
               });
               JPanel var29 = new JPanel();
               var29.add(var28);
               var29.add(var27);
               var29.add(var26);
               var13.getContentPane().add(var29, "South");
               var13.setSize(var19, 500);
               var13.setMinimumSize(var13.getSize());
               var13.setLocationRelativeTo(MainFrame.thisinstance);
               var13.setVisible(true);
            }
         }
      }
   }

   private void choose_done(JTable var1) {
      int var2 = var1.getSelectedRow();
      if (var2 != -1) {
         Integer var3 = (Integer)var1.getValueAt(var2, 0);
         this.pv.dynindex = var3 - 1;
         this.nofire = true;
         this.PG.setSpinnerValue(new Integer(this.pv.dynindex + 1));
         this.nofire = false;
         this.refresh();
      }
   }

   private void copyValueAction(DataFieldModel var1) {
      if (!"1".equals(MainFrame.opmode)) {
         GUI_Datastore var2 = this.getDatastore();
         String var3 = this.pv.dynindex + "_" + var1.key;
         String var4 = var2.get(var3);
         int var5 = this.pv.dynindex;

         for(int var6 = 0; var6 < this.pv.lapcount; ++var6) {
            var2.set(var6 + "_" + var1.key, var4);
            this.pv.dynindex = var6;
            CalculatorManager.getInstance().page_calc(this.pv.PM.pid, this.pv.dynindex);
         }

         this.pv.dynindex = var5;
         CalculatorManager.getInstance().do_dpage_count();
         this.refresh();
      }
   }
}
