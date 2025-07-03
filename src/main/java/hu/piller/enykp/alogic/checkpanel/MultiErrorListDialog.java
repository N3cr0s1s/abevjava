package hu.piller.enykp.alogic.checkpanel;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.ICommandObject;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IResult;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.EventLog;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import hu.piller.enykp.util.icon.ENYKIconSet;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

public class MultiErrorListDialog implements ICommandObject {
   private CalculatorManager calc_man;
   private MultiErrorListDialog.MELDialog dlg;
   private final Vector cmd_list = new Vector(Arrays.asList("abev.showMultiErrorListDialog"));
   private final Hashtable states = new Hashtable(8);

   public void setCalculatorManager(CalculatorManager var1) {
      this.calc_man = var1;
   }

   public CalculatorManager getCalculatorManager() {
      return this.calc_man;
   }

   private MultiErrorListDialog.MELDialog createDialog() {
      MainFrame var1 = MainFrame.thisinstance;
      return new MultiErrorListDialog.MELDialog(var1, this);
   }

   public void execute() {
      final MultiErrorListDialog.MELDialog var1 = this.createDialog();
      this.dlg = var1;
      Container var2 = var1.getContentPane();
      JPanel var3 = var1.panel;
      var3.invalidate();
      var3.setVisible(true);
      var2.add(var3);
      var1.setTitle("Összes nyomtatvány hibalistája");
      int var4 = GuiUtil.getW("WWÁllományba mentésWWWWOkWWWWEllenőrzésWWWWEllenőrzés befejezve. [WW:WW:WW]WW");
      var1.setSize(var4, 400);
      var3.setMinimumSize(new Dimension(500, 200));
      var1.setResizable(true);
      var1.setLocationRelativeTo(MainFrame.thisinstance);
      var1.setModal(false);
      var1.doErrorCheck();
      if (SwingUtilities.isEventDispatchThread()) {
         var1.setVisible(true);
      } else {
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               var1.setVisible(true);
            }
         });
      }

   }

   public void docheck() {
      this.dlg.doErrorCheck();
   }

   public void closeDialog() {
      if (this.dlg != null) {
         this.dlg.setVisible(false);
         this.dlg.dispose();
      }

   }

   public void setParameters(Hashtable var1) {
   }

   public ICommandObject copy() {
      MultiErrorListDialog var1 = new MultiErrorListDialog();
      var1.setCalculatorManager(this.getCalculatorManager());
      return var1;
   }

   public boolean isCommandIdentical(String var1) {
      if (var1 != null) {
         var1 = var1.trim();
         if (var1.equalsIgnoreCase(this.cmd_list.get(0).toString())) {
            return true;
         }
      }

      return false;
   }

   public Vector getCommandList() {
      return this.cmd_list;
   }

   public Hashtable getCommandHelps() {
      return null;
   }

   public void setStateDefinitions(String var1) {
      if (var1 != null) {
         String[] var2 = var1.split(",");
         int var4 = 0;

         for(int var5 = var2.length; var4 < var5; ++var4) {
            String[] var3 = var2[var4].split("=");
            if (var3.length > 1) {
               Object var6 = new String(var3[1]);
               if (var3[1].equalsIgnoreCase("t")) {
                  var6 = Boolean.FALSE;
               }

               if (var3[1].equalsIgnoreCase("e")) {
                  var6 = Boolean.TRUE;
               }

               this.states.put(new String(var3[0]), var6);
            }
         }
      }

   }

   public Object getState(Object var1) {
      Object[] var2 = new Object[]{Boolean.TRUE};
      if (var1 instanceof Object[]) {
         Object[] var3 = (Object[])((Object[])var1);
         String var4 = var3[0] == null ? "" : var3[0].toString();
         Object var5 = this.states.get(var4);
         if (var5 instanceof Boolean) {
            var2[0] = var5;
         }
      }

      return var2;
   }

   private static void writeLog(Object var0) {
      try {
         EventLog.getInstance().writeLog(var0);
      } catch (IOException var2) {
         Tools.eLog(var2, 0);
      }

   }

   private static class MELDialog extends JDialog implements IResult, IEventListener {
      boolean detailmode = false;
      private static final String MSG_SEPARATOR = "|";
      private static final String CHAR_SET = "ISO-8859-2";
      private static final String FORM_MSG_ID_FIELD = "4001";
      private static final String FORM_MSG_ID_FORM = "4002";
      private static final String FORM_MSG_ID_NO_EXPSTORE = "4005";
      DefaultListModel2 dlm_all;
      DefaultListModel2 dlm_errors;
      boolean check_break;
      JCheckBox chk_warnings;
      JCheckBox chk_details;
      JList lst_messages;
      JTextArea txt_message;
      JButton btn_save;
      JButton btn_ok;
      JButton btn_recheck;
      TimerProgressBar pb_filling;
      JPanel panel;
      private boolean is_returned;
      private Thread check_thread;
      MultiErrorListDialog error_list_co;
      boolean is_in_dialog = true;
      Object form_name = null;
      JSplitPane splitPane;

      private String detail_filter(String var1) {
         boolean var2 = this.detailmode;
         if (var2 && !this.chk_details.isSelected()) {
            int var3 = var1.indexOf("Mezőinformációk");
            if (var3 == -1) {
               var3 = var1.length();
            }

            String var4 = var1.substring(0, var3);
            return var4;
         } else {
            return var1;
         }
      }

      MELDialog(Frame var1, MultiErrorListDialog var2) {
         super(var1);
         this.error_list_co = var2;
         this.build();
         this.prepare();
      }

      public Object eventFired(Event var1) {
         Object var2 = var1.getUserData();
         if (var2 instanceof Hashtable) {
            Hashtable var3 = (Hashtable)var2;
            var2 = var3.get("event");
            if (var2.equals("multi_form_switch")) {
               var2 = var3.get("name");
               String var8 = var2 == null ? "(Nyomtatvány)" : var2.toString();
               this.form_name = var8;
               if (!this.pb_filling.isIndeterminate()) {
                  final TimerProgressBar var4 = this.pb_filling;
                  int var5 = var4.getValue();
                  ++var5;
                  if (SwingUtilities.isEventDispatchThread()) {
                     var4.setValue(var5);
                     var4.setString("" + var5 + " / " + var4.getMaximum());
                  } else {
                     int finalVar = var5;
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                           var4.setValue(finalVar);
                           var4.setString("" + finalVar + " / " + var4.getMaximum());
                        }
                     });
                  }
               }
            } else {
               var2 = var3.get("item");
               if (var2 instanceof Object[]) {
                  Object[] var10 = (Object[])((Object[])var2);
                  if (var10[0] != null) {
                     String var11 = var10[0].toString();
                     if (var11.equalsIgnoreCase("4001") || var11.equalsIgnoreCase("4002") || var11.equalsIgnoreCase("4005")) {
                        MultiErrorListDialog.MELDialog.ListItem var6 = new MultiErrorListDialog.MELDialog.ListItem(var10);
                        int var7 = getInt(var10[4]);
                        if (this.form_name != null) {
                           this.dlm_all.addElement(this.form_name);
                           this.dlm_errors.addElement(this.form_name);
                           this.form_name = null;
                        }

                        if (this.dlm_all != null) {
                           this.dlm_all.addElement(var6);
                        }

                        if ((var7 == IErrorList.LEVEL_ERROR || var7 == IErrorList.LEVEL_SHOW_ERROR || var7 == IErrorList.LEVEL_FATAL_ERROR || var7 == IErrorList.LEVEL_SHOW_FATAL_ERROR) && this.dlm_errors != null) {
                           this.dlm_errors.addElement(var6);
                        }
                     }
                  }
               }
            }
         } else if (var2 instanceof String) {
            String var9 = (String)var2;
            if ("afterclose".equalsIgnoreCase(var9)) {
               this.btn_ok.doClick(0);
            }
         }

         return null;
      }

      private void build() {
         this.chk_warnings = GuiUtil.getANYKCheckBox("Figyelmeztetések is láthatók");
         this.chk_warnings.setAlignmentX(0.0F);
         this.lst_messages = new JList();
         this.lst_messages.setSelectionMode(0);
         JScrollPane var3 = new JScrollPane(this.lst_messages);
         var3.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
         var3.setAlignmentX(0.0F);
         this.txt_message = new JTextArea();
         this.txt_message.setEditable(false);
         this.txt_message.setLineWrap(true);
         this.txt_message.setWrapStyleWord(true);
         JScrollPane var4 = new JScrollPane(this.txt_message);
         var4.setHorizontalScrollBarPolicy(31);
         var4.setVerticalScrollBarPolicy(20);
         var4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
         var4.setMinimumSize(new Dimension(Integer.MAX_VALUE, 70));
         var4.setPreferredSize(new Dimension(Integer.MAX_VALUE, 70));
         var4.setAlignmentX(0.0F);
         SettingsStore var5 = SettingsStore.getInstance();
         String var6 = var5.get("gui", "detail");
         this.detailmode = var6 == null ? true : "true".equals(var6);
         this.chk_details = GuiUtil.getANYKCheckBox("Mezőinformációk is láthatók");
         JPanel var7 = new JPanel();
         var7.setLayout(new BoxLayout(var7, 0));
         var7.setAlignmentX(0.0F);
         var7.add(this.chk_warnings);
         if (this.detailmode) {
            var7.add(Box.createHorizontalGlue());
            var7.add(this.chk_details);
         }

         this.chk_details.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent var1) {
               Object var2 = MELDialog.this.lst_messages.getSelectedValue();
               MELDialog.this.txt_message.setText(var2 == null ? "" : var2.toString());
               MELDialog.this.repaint();
            }
         });
         JPanel var1 = new JPanel();
         var1.setLayout(new BoxLayout(var1, 1));
         var1.setAlignmentX(0.0F);
         var1.add(var7, (Object)null);
         var1.add(var3, (Object)null);
         this.splitPane = new JSplitPane(0, var1, var4);
         this.splitPane.setResizeWeight(0.5D);

         try {
            BasicSplitPaneDivider var8 = (BasicSplitPaneDivider)this.splitPane.getComponent(2);
            var8.setBackground(GuiUtil.getHighLightColor());
         } catch (Exception var9) {
            System.out.println("SplitPane - defaultColor");
         }

         this.btn_save = new JButton("Állományba mentés");
         this.btn_save.setAlignmentX(0.0F);
         this.btn_ok = new JButton("Ok");
         this.btn_ok.setAlignmentX(1.0F);
         this.btn_recheck = new JButton("Ellenőrzés");
         this.btn_recheck.setAlignmentX(1.0F);
         this.pb_filling = new TimerProgressBar();
         this.pb_filling.setAlignmentX(0.5F);
         JPanel var2 = new JPanel();
         var2.setLayout(new BoxLayout(var2, 0));
         var2.setAlignmentX(0.0F);
         var2.add(this.btn_save, (Object)null);
         var2.add(Box.createGlue(), (Object)null);
         var2.add(this.pb_filling, (Object)null);
         var2.add(Box.createRigidArea(new Dimension(5, 0)));
         var2.add(Box.createGlue(), (Object)null);
         var2.add(this.btn_recheck, (Object)null);
         var2.add(this.btn_ok, (Object)null);
         this.getRootPane().setDefaultButton(this.btn_ok);
         this.panel = new JPanel();
         this.panel.setLayout(new BoxLayout(this.panel, 1));
         this.panel.add(this.splitPane, (Object)null);
         this.panel.add(var2, (Object)null);
      }

      private void prepare() {
         this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent var1) {
               CalculatorManager.getInstance().end_formcheck();
            }
         });
         this.chk_warnings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               MELDialog.this.setListDataModel();
            }
         });
         this.btn_save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               SimpleDateFormat var2 = new SimpleDateFormat("[yyyy.MM.dd] [kk:mm:ss.SSS]");
               JFileChooser var4 = null;
               if (var4 == null) {
                  UIManager.put("FileChooser.saveInLabelText", "Mentés ide:");
                  UIManager.put("FileChooser.fileNameLabelText", "Állomány neve:");
                  UIManager.put("FileChooser.filesOfTypeLabelText", "Állomány szűrő:");
                  UIManager.put("FileChooser.cancelButtonText", "Mégsem");
                  UIManager.put("FileChooser.saveButtonText", "Mentés");
                  UIManager.put("FileChooser.acceptAllFileFilterText", "Minden Állomány");
                  var4 = new JFileChooser();
                  var4.setDialogTitle("Látható üzenetek állományba mentése");
                  var4.setSelectedFile(new NecroFile("Javas_ABEV_Üzenetek.txt"));
                  var4.setCurrentDirectory(new NecroFile((String)PropertyList.getInstance().get("prop.usr.naplo")));
               }

               while(true) {
                  int var3 = var4.showSaveDialog(SwingUtilities.getRoot(MELDialog.this));
                  if (var3 != 0) {
                     break;
                  }

                  DefaultListModel2 var5 = (DefaultListModel2)MELDialog.this.lst_messages.getModel();
                  File var6 = var4.getSelectedFile();
                  if (!var6.exists() || 1 != JOptionPane.showConfirmDialog(SwingUtilities.getRoot(MELDialog.this), var6.getName() + " állomány létezik ! Felülírja ?", "Üzenetek állományba mentése", 0)) {
                     try {
                        BufferedWriter var7 = new BufferedWriter(new OutputStreamWriter(new NecroFileOutputStream(var6), "ISO-8859-2"));

                        try {
                           var7.write("Mentés dátuma: " + var2.format(new Date()));
                           var7.newLine();
                           var7.write("Nyomtatvány: " + MELDialog.this.getFormName());
                           var7.newLine();
                           var7.write("Üzenet elválasztó: |");
                           var7.newLine();
                           var7.write("Üzenetek:");
                           var7.newLine();
                           int var9 = 0;

                           for(int var10 = var5.getSize(); var9 < var10; ++var9) {
                              Object var11 = var5.getElementAt(var9);
                              if (!(var11 instanceof MultiErrorListDialog.MELDialog.ListItem)) {
                                 var7.newLine();
                                 var7.write("" + (var11 == null ? "" : var11.toString()));
                              } else {
                                 MultiErrorListDialog.MELDialog.ListItem var12 = (MultiErrorListDialog.MELDialog.ListItem)var11;
                                 Object[] var8 = var12.item;
                                 int var13 = 0;

                                 for(int var14 = var8.length; var13 < var14; ++var13) {
                                    var11 = var8[var13];
                                    if (var13 == 4 && var12.getIcon() instanceof Icon) {
                                       var11 = MultiErrorListDialog.MELDialog.getIconName(var12.getIcon());
                                    }

                                    if (var13 == 3 && var11 instanceof JButton) {
                                       var11 = "";
                                    }

                                    if (var11 == null) {
                                       var11 = "";
                                    }

                                    if (var13 == 1) {
                                       String var25 = var11.toString().replaceAll("#13", "");
                                       var11 = var25.toString().replaceAll("\\n", " ");
                                    }

                                    if (var13 != 0) {
                                       var7.write("|" + var11);
                                    } else {
                                       try {
                                          if (!var8[4].equals(IErrorList.LEVEL_ERROR) && !var8[4].equals(IErrorList.LEVEL_SHOW_ERROR)) {
                                             if (!var8[4].equals(IErrorList.LEVEL_FATAL_ERROR) && !var8[4].equals(IErrorList.LEVEL_SHOW_FATAL_ERROR)) {
                                                if (!var8[4].equals(IErrorList.LEVEL_WARNING) && !var8[4].equals(IErrorList.LEVEL_SHOW_WARNING)) {
                                                   if (var8[4].equals(IErrorList.LEVEL_MESSAGE) || var8[4].equals(IErrorList.LEVEL_SHOW_MESSAGE)) {
                                                      var11 = "M";
                                                   }
                                                } else {
                                                   var11 = "F";
                                                }
                                             } else {
                                                var11 = "SH";
                                             }
                                          } else {
                                             var11 = "H";
                                          }
                                       } catch (Exception var21) {
                                          Tools.eLog(var21, 0);
                                       }

                                       var7.write("" + var11);
                                    }
                                 }
                              }

                              var7.newLine();
                           }

                           return;
                        } catch (IOException var22) {
                           MultiErrorListDialog.writeLog("Hiba történt az üzenetek mentésekor: " + var22);
                           break;
                        } finally {
                           var7.flush();
                           var7.close();
                           GuiUtil.showMessageDialog(SwingUtilities.getRoot(MELDialog.this), "Az üzeneteket a(z) '" + var6.getName() + "' nevű állományba mentettük.", "Hibalista mentése", 1);
                        }
                     } catch (Exception var24) {
                        MultiErrorListDialog.writeLog("Hiba történt az üzenetek mentésekor: " + var24);
                        break;
                     }
                  }
               }

            }
         });
         this.btn_ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               MELDialog.this.setVisible(false);
               MELDialog.this.dispose();
               CalculatorManager.getInstance().end_formcheck();
            }
         });
         this.btn_recheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               MELDialog.this.doErrorCheck();
            }
         });
         this.lst_messages.setCellRenderer(new ListCellRenderer() {
            private final JLabel renderer = new JLabel();

            public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
               this.renderer.setOpaque(true);
               if (var2 instanceof MultiErrorListDialog.MELDialog.ListItem) {
                  MultiErrorListDialog.MELDialog.ListItem var6 = (MultiErrorListDialog.MELDialog.ListItem)var2;
                  this.renderer.setIcon(var6.getIcon());
                  this.renderer.setText(var6.toString());
                  this.renderer.setBackground(var4 ? var1.getSelectionBackground() : var1.getBackground());
               } else {
                  this.renderer.setIcon((Icon)null);
                  this.renderer.setText(var2 == null ? "???" : var2.toString());
                  this.renderer.setBackground(var4 ? var1.getSelectionBackground() : var1.getBackground());
               }

               return this.renderer;
            }
         });
         this.lst_messages.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent var1) {
               if (MELDialog.this.lst_messages.getSelectedValue() instanceof MultiErrorListDialog.MELDialog.ListItem) {
                  Object var2 = MELDialog.this.lst_messages.getSelectedValue();
                  MELDialog.this.txt_message.setText(var2 == null ? "" : var2.toString());
               } else {
                  MELDialog.this.txt_message.setText("");
               }

            }
         });
         this.lst_messages.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent var1) {
               if (var1.getClickCount() == 2) {
                  Object var2 = MELDialog.this.lst_messages.getSelectedValue();
                  if (var2 instanceof MultiErrorListDialog.MELDialog.ListItem && ((MultiErrorListDialog.MELDialog.ListItem)var2).item instanceof Object[]) {
                     Object[] var3 = ((MultiErrorListDialog.MELDialog.ListItem)var2).item;
                     if (3 < var3.length) {
                        var2 = var3[3];
                        if (var2 instanceof JButton) {
                           ((JButton)var2).doClick(0);
                        }
                     }
                  }
               }

            }
         });
         this.dlm_all = new DefaultListModel2();
         this.dlm_errors = new DefaultListModel2();
         this.chk_warnings.setSelected(true);
         this.setListDataModel();
      }

      private void setListDataModel() {
         if (this.chk_warnings.isSelected()) {
            this.lst_messages.setModel(this.dlm_all);
         } else {
            this.lst_messages.setModel(this.dlm_errors);
         }

      }

      private static synchronized Object getIcon(Object var0) {
         int var1 = getInt(var0);
         ENYKIconSet.getInstance().get("");
         if (var1 == IErrorList.LEVEL_MESSAGE) {
            return ENYKIconSet.getInstance().get("statusz_zold");
         } else if (var1 == IErrorList.LEVEL_WARNING) {
            return ENYKIconSet.getInstance().get("statusz_sarga");
         } else if (var1 == IErrorList.LEVEL_ERROR) {
            return ENYKIconSet.getInstance().get("statusz_piros");
         } else if (var1 == IErrorList.LEVEL_FATAL_ERROR) {
            return ENYKIconSet.getInstance().get("statusz_kek");
         } else if (var1 == IErrorList.LEVEL_SHOW_MESSAGE) {
            return ENYKIconSet.getInstance().get("statusz_zold");
         } else if (var1 == IErrorList.LEVEL_SHOW_WARNING) {
            return ENYKIconSet.getInstance().get("statusz_sarga");
         } else if (var1 == IErrorList.LEVEL_SHOW_ERROR) {
            return ENYKIconSet.getInstance().get("statusz_piros");
         } else {
            return var1 == IErrorList.LEVEL_SHOW_FATAL_ERROR ? ENYKIconSet.getInstance().get("statusz_kek") : var0;
         }
      }

      private static synchronized int getInt(Object var0) {
         if (var0 instanceof Integer) {
            return (Integer)var0;
         } else if (var0 instanceof Long) {
            return ((Long)var0).intValue();
         } else if (var0 instanceof Short) {
            return ((Short)var0).intValue();
         } else if (var0 instanceof Float) {
            return ((Float)var0).intValue();
         } else {
            return var0 instanceof Double ? ((Double)var0).intValue() : -1;
         }
      }

      private static synchronized String getIconName(Icon var0) {
         if (var0 == ENYKIconSet.getInstance().get("statusz_zold")) {
            return "Üzenet";
         } else if (var0 == ENYKIconSet.getInstance().get("statusz_sarga")) {
            return "Figyelmeztetés";
         } else if (var0 == ENYKIconSet.getInstance().get("statusz_piros")) {
            return "Hiba";
         } else if (var0 == ENYKIconSet.getInstance().get("statusz_kek")) {
            return "Végzetes hiba";
         } else if (var0 == ENYKIconSet.getInstance().get("statusz_zold")) {
            return "Feldobott üzenet";
         } else if (var0 == ENYKIconSet.getInstance().get("statusz_sarga")) {
            return "Feldobott figyelmeztetés";
         } else if (var0 == ENYKIconSet.getInstance().get("statusz_piros")) {
            return "Feldobott hiba";
         } else {
            return var0 == ENYKIconSet.getInstance().get("statusz_kek") ? "Végzetes feldobott hiba" : "?";
         }
      }

      private void doErrorCheck() {
         this.check_thread = new Thread(new Runnable() {
            public void run() {
               final JDialog[] var1 = new JDialog[1];
               final Thread var2 = new Thread(new Runnable() {
                  public void run() {
                     if (!MELDialog.this.is_returned) {
                        var1[0] = new JDialog(MELDialog.this, "Nyomtatvány ellenőrzés ...");
                        var1[0].setDefaultCloseOperation(0);
                        JPanel var1x = new JPanel();
                        final JLabel var2 = new JLabel("Nyomtatvány ellenőrzése folyamatban ...");
                        final JButton var3 = new JButton("Megszakítás");
                        var1x.setLayout(new BorderLayout(5, 5));
                        var1x.add(var2, "Center");
                        var1x.add(var3, "East");
                        var3.addActionListener(new ActionListener() {
                           public void actionPerformed(ActionEvent var1x) {
                              var3.setVisible(false);
                              var2.setText("Ellenőrzés megszakítása folyamatban ...");
                              if (MELDialog.this.error_list_co.getCalculatorManager() != null) {
                                 MELDialog.this.check_break = true;
                                 MELDialog.this.error_list_co.getCalculatorManager().check_all_stop((Object)null);
                              }

                           }
                        });
                        var1[0].setContentPane(var1x);
                        var1[0].setSize(350, 60);
                        var1[0].setLocationRelativeTo(MELDialog.this.panel);
                        var1[0].setModal(true);
                        if (!MELDialog.this.is_returned) {
                           var1[0].setVisible(true);
                        }
                     }

                  }
               });
               boolean var19 = false;

               try {
                  var19 = true;
                  MELDialog.this.is_returned = false;
                  MELDialog.this.btn_ok.setEnabled(false);
                  MELDialog.this.btn_recheck.setEnabled(false);
                  MELDialog.this.btn_save.setEnabled(false);
                  int var3 = -1;
                  if (Calculator.getInstance() != null) {
                     Vector var4 = Calculator.getInstance().getBookModel().get_store_collection();
                     if (var4 != null) {
                        var3 = var4.size();
                     }
                  }

                  MELDialog.this.pb_filling.setStringPainted(true);
                  MELDialog.this.pb_filling.setString("Összes nyomtatvány ellenőrzése ...");
                  if (var3 > 0) {
                     MELDialog.this.pb_filling.setIndeterminate(false);
                     MELDialog.this.pb_filling.setMinimum(0);
                     MELDialog.this.pb_filling.setMaximum(var3);
                  } else {
                     MELDialog.this.pb_filling.setIndeterminate(true);
                  }

                  if (MELDialog.this.dlm_all != null) {
                     MELDialog.this.dlm_all.clear();
                  }

                  if (MELDialog.this.dlm_errors != null) {
                     MELDialog.this.dlm_errors.clear();
                  }

                  MELDialog.this.lst_messages.setModel(new DefaultListModel());
                  ((DefaultListModel)MELDialog.this.lst_messages.getModel()).addElement("Hibalista készítése folyamatban ...");

                  IErrorList var33;
                  try {
                     var33 = ErrorList.getInstance();
                  } catch (Exception var30) {
                     var33 = null;
                  }

                  if (var33 instanceof IEventSupport) {
                     IEventSupport var5 = (IEventSupport)var33;

                     try {
                        var5.addEventListener(MELDialog.this);
                        if (MELDialog.this.error_list_co.getCalculatorManager() != null) {
                           MELDialog.this.check_break = false;
                           SwingUtilities.invokeLater(new Runnable() {
                              public void run() {
                                 var2.start();
                              }
                           });
                           MELDialog.this.error_list_co.getCalculatorManager().do_check_all(MELDialog.this, MELDialog.this);

                           while(!MELDialog.this.is_returned) {
                              try {
                                 Thread.sleep(10L);
                              } catch (InterruptedException var29) {
                                 Tools.eLog(var29, 0);
                              }
                           }

                           if (MELDialog.this.check_break) {
                              String var6 = "Az ellenőrzési műveletet a felhasználó megszakította ...";
                              MELDialog.this.dlm_all.addElement(" ");
                              MELDialog.this.dlm_all.addElement(var6);
                              MELDialog.this.dlm_errors.addElement(" ");
                              MELDialog.this.dlm_errors.addElement(var6);
                           }

                           try {
                              Thread.sleep(100L);
                           } catch (InterruptedException var28) {
                              Tools.eLog(var28, 0);
                           }
                        }
                     } finally {
                        if (var1[0] != null) {
                           var1[0].setVisible(false);
                           var1[0].dispose();
                        }

                        try {
                           var2.join();
                        } catch (InterruptedException var27) {
                           Tools.eLog(var27, 0);
                        }

                        var5.removeEventListener(MELDialog.this);
                     }

                     var19 = false;
                  } else {
                     var19 = false;
                  }
               } finally {
                  if (var19) {
                     new Thread(new Runnable() {
                        public void run() {
                           try {
                              Thread.sleep(100L);
                           } catch (InterruptedException var3) {
                              Tools.eLog(var3, 0);
                           }

                           MELDialog.this.invalidate();
                           MELDialog.this.validate();

                           try {
                              RepaintManager.currentManager(MELDialog.this).addDirtyRegion(MELDialog.this.panel, 0, 0, MELDialog.this.panel.getWidth(), MELDialog.this.panel.getHeight());
                              RepaintManager.currentManager(MELDialog.this).paintDirtyRegions();
                           } catch (Exception var2) {
                              Tools.eLog(var2, 0);
                           }

                        }
                     });
                     Thread var11 = new Thread() {
                        public void run() {
                           if (SwingUtilities.isEventDispatchThread()) {
                              MELDialog.this.pb_filling.setString("Ellenőrzés befejezve.");
                              MELDialog.this.pb_filling.setValue(MELDialog.this.pb_filling.getMaximum());
                              MELDialog.this.pb_filling.setIndeterminate(false);
                              MELDialog.this.btn_save.setEnabled(true);
                              MELDialog.this.btn_recheck.setEnabled(true);
                              MELDialog.this.btn_ok.setEnabled(true);
                           } else {
                              SwingUtilities.invokeLater(new Runnable() {
                                 public void run() {
                                    MELDialog.this.pb_filling.setString("Ellenőrzés befejezve.");
                                    MELDialog.this.pb_filling.setValue(MELDialog.this.pb_filling.getMaximum());
                                    MELDialog.this.pb_filling.setIndeterminate(false);
                                    MELDialog.this.btn_save.setEnabled(true);
                                    MELDialog.this.btn_recheck.setEnabled(true);
                                    MELDialog.this.btn_ok.setEnabled(true);
                                 }
                              });
                           }

                           MELDialog.this.setListDataModel();
                           SwingUtilities.invokeLater(new Runnable() {
                              public void run() {
                                 MELDialog.this.invalidate();
                                 MELDialog.this.validate();

                                 try {
                                    RepaintManager.currentManager(MELDialog.this).addDirtyRegion(MELDialog.this.panel, 0, 0, MELDialog.this.panel.getWidth(), MELDialog.this.panel.getHeight());
                                    RepaintManager.currentManager(MELDialog.this).paintDirtyRegions();
                                 } catch (Exception var2) {
                                    Tools.eLog(var2, 0);
                                 }

                              }
                           });
                        }
                     };
                     SwingUtilities.invokeLater(new Runnable() {
                        // $FF: synthetic field
                        final Thread val$t1;

                        {
                           this.val$t1 = var2;
                        }

                        public void run() {
                           this.val$t1.start();
                        }
                     });
                  }
               }

               new Thread(new Runnable() {
                  public void run() {
                     try {
                        Thread.sleep(100L);
                     } catch (InterruptedException var3) {
                        Tools.eLog(var3, 0);
                     }

                     MELDialog.this.invalidate();
                     MELDialog.this.validate();

                     try {
                        RepaintManager.currentManager(MELDialog.this).addDirtyRegion(MELDialog.this.panel, 0, 0, MELDialog.this.panel.getWidth(), MELDialog.this.panel.getHeight());
                        RepaintManager.currentManager(MELDialog.this).paintDirtyRegions();
                     } catch (Exception var2) {
                        Tools.eLog(var2, 0);
                     }

                  }
               });
               Thread var34 = new Thread() {
                  public void run() {
                     if (SwingUtilities.isEventDispatchThread()) {
                        MELDialog.this.pb_filling.setString("Ellenőrzés befejezve.");
                        MELDialog.this.pb_filling.setValue(MELDialog.this.pb_filling.getMaximum());
                        MELDialog.this.pb_filling.setIndeterminate(false);
                        MELDialog.this.btn_save.setEnabled(true);
                        MELDialog.this.btn_recheck.setEnabled(true);
                        MELDialog.this.btn_ok.setEnabled(true);
                     } else {
                        SwingUtilities.invokeLater(new Runnable() {
                           public void run() {
                              MELDialog.this.pb_filling.setString("Ellenőrzés befejezve.");
                              MELDialog.this.pb_filling.setValue(MELDialog.this.pb_filling.getMaximum());
                              MELDialog.this.pb_filling.setIndeterminate(false);
                              MELDialog.this.btn_save.setEnabled(true);
                              MELDialog.this.btn_recheck.setEnabled(true);
                              MELDialog.this.btn_ok.setEnabled(true);
                           }
                        });
                     }

                     MELDialog.this.setListDataModel();
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                           MELDialog.this.invalidate();
                           MELDialog.this.validate();

                           try {
                              RepaintManager.currentManager(MELDialog.this).addDirtyRegion(MELDialog.this.panel, 0, 0, MELDialog.this.panel.getWidth(), MELDialog.this.panel.getHeight());
                              RepaintManager.currentManager(MELDialog.this).paintDirtyRegions();
                           } catch (Exception var2) {
                              Tools.eLog(var2, 0);
                           }

                        }
                     });
                  }
               };
               SwingUtilities.invokeLater(new Runnable() {
                  // $FF: synthetic field
                  final Thread val$t1;

                  {
                     this.val$t1 = var2;
                  }

                  public void run() {
                     this.val$t1.start();
                  }
               });
            }
         });
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               MELDialog.this.pb_filling.setValue(0);
               MELDialog.this.pb_filling.setStartTime();
               SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                     MELDialog.this.check_thread.start();
                  }
               });
            }
         });
      }

      private String getFormName() {
         String var1 = Calculator.getInstance().getBookModel().get_formname();
         if (var1 instanceof String) {
            String var2 = (String)var1;
            var2 = var2.trim();
            if (var2.length() > 0) {
               return var2;
            }
         }

         return "(Nincs)";
      }

      public void setResult(Object var1) {
         this.is_returned = true;
      }

      private class ListItem {
         public Object[] item;

         public ListItem(Object[] var2) {
            this.item = var2;
         }

         public Icon getIcon() {
            return this.item != null && 4 < this.item.length ? (Icon)MultiErrorListDialog.MELDialog.getIcon(this.item[4]) : null;
         }

         public String toString() {
            return this.item != null && this.item.length > 1 && this.item[1] != null ? MELDialog.this.detail_filter(this.item[1].toString().replaceAll("#13", " ")) : "";
         }
      }
   }
}
