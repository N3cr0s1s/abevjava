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
import javax.swing.AbstractAction;
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
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

public class ErrorListDialog implements ICommandObject {
   private CalculatorManager calc_man;
   private ErrorListDialog.ELDialog dlg;
   private final Vector cmd_list = new Vector(Arrays.asList("abev.showErrorListDialog"));
   private final Hashtable states = new Hashtable(8);

   private ErrorListDialog.ELDialog createDialog() {
      MainFrame var1 = MainFrame.thisinstance;
      return new ErrorListDialog.ELDialog(var1, this);
   }

   public void setCalculatorManager(CalculatorManager var1) {
      this.calc_man = var1;
   }

   public CalculatorManager getCalculatorManager() {
      return this.calc_man;
   }

   public void execute() {
      final ErrorListDialog.ELDialog var1 = this.createDialog();
      this.dlg = var1;
      Container var2 = var1.getContentPane();
      JPanel var3 = var1.panel;
      var3.invalidate();
      var3.setVisible(true);
      var2.add(var3);
      var1.setTitle("Nyomtatványkitöltési hibalista");
      Dimension var4 = new Dimension(3 * GuiUtil.getW(var1.chk_details, var1.chk_details.getText()), 20 * GuiUtil.getCommonItemHeight());
      var1.splitPane.setDividerLocation(12 * GuiUtil.getCommonItemHeight());
      var1.setSize(var4);
      var1.setMinimumSize(var4);
      var3.setMinimumSize(new Dimension(450, 200));
      var1.setResizable(true);
      var1.setLocationRelativeTo(MainFrame.thisinstance);
      var1.doErrorCheck();
      var1.setModal(false);
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
      ErrorListDialog var1 = new ErrorListDialog();
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

   private static class ELDialog extends JDialog implements IResult, IEventListener {
      boolean detailmode = false;
      private static final String MSG_SEPARATOR = "|";
      private static final String CHAR_SET = "ISO-8859-2";
      private static final String FORM_MSG_ID_FIELD = "4001";
      private static final String FORM_MSG_ID_FORM = "4002";
      private static final String FORM_MSG_ID_NO_EXPSTORE = "4005";
      DefaultListModel dlm_all;
      DefaultListModel dlm_errors;
      JCheckBox chk_warnings;
      JCheckBox chk_details;
      JList lst_messages;
      JTextArea txt_message;
      JButton btn_save;
      JButton btn_ok;
      JButton btn_recheck;
      JLabel checking;
      JPanel panel;
      private boolean is_returned;
      private Thread check_thread;
      ErrorListDialog error_list_co;
      JDialog modalDialog = new JDialog(this, "Nyomtatvány ellenőrzése", true);
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

      ELDialog(Frame var1, ErrorListDialog var2) {
         super(var1);
         this.error_list_co = var2;
         this.build();
         this.prepare();
         this.createModalDialog();
      }

      public Object eventFired(Event var1) {
         Object var2 = var1.getUserData();
         if (var2 instanceof Hashtable) {
            Hashtable var3 = (Hashtable)var2;
            var2 = var3.get("item");
            if (var2 instanceof Object[]) {
               Object[] var4 = (Object[])((Object[])var2);
               if (var4[0] != null) {
                  String var5 = var4[0].toString();
                  if (var5.equalsIgnoreCase("4001") || var5.equalsIgnoreCase("4002") || var5.equalsIgnoreCase("4005")) {
                     ErrorListDialog.ELDialog.ListItem var6 = new ErrorListDialog.ELDialog.ListItem(var4);
                     int var7 = getInt(var4[4]);
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

         return null;
      }

      private void build() {
         String var5 = "EscPressedKey";
         this.getRootPane().getInputMap(2).put(KeyStroke.getKeyStroke(27, 0), var5);
         this.getRootPane().getActionMap().put(var5, new AbstractAction() {
            public void actionPerformed(ActionEvent var1) {
               ELDialog.this.setVisible(false);
               ELDialog.this.dispose();
            }
         });
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
         SettingsStore var6 = SettingsStore.getInstance();
         String var7 = var6.get("gui", "detail");
         this.detailmode = var7 == null ? true : "true".equals(var7);
         this.chk_details = GuiUtil.getANYKCheckBox("Mezőinformációk is láthatók");
         JPanel var8 = new JPanel();
         var8.setLayout(new BoxLayout(var8, 0));
         var8.setAlignmentX(0.0F);
         var8.add(this.chk_warnings);
         if (this.detailmode) {
            var8.add(Box.createHorizontalGlue());
            var8.add(this.chk_details);
         }

         this.chk_details.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent var1) {
               Object var2 = ELDialog.this.lst_messages.getSelectedValue();
               ELDialog.this.txt_message.setText(var2 == null ? "" : var2.toString());
               ELDialog.this.repaint();
            }
         });
         JPanel var1 = new JPanel();
         var1.setLayout(new BoxLayout(var1, 1));
         var1.setAlignmentX(0.0F);
         var1.add(var8, (Object)null);
         var1.add(var3, (Object)null);
         var1.setMinimumSize(new Dimension(Integer.MAX_VALUE, 120));
         this.splitPane = new JSplitPane(0, var1, var4);
         this.splitPane.setResizeWeight(0.5D);

         try {
            BasicSplitPaneDivider var9 = (BasicSplitPaneDivider)this.splitPane.getComponent(2);
            var9.setBackground(GuiUtil.getHighLightColor());
         } catch (Exception var10) {
            System.out.println("SplitPane - defaultColor");
         }

         this.btn_save = new JButton("Állományba mentés");
         this.btn_save.setAlignmentX(0.0F);
         this.btn_ok = new JButton("Ok");
         this.btn_ok.setAlignmentX(1.0F);
         this.btn_recheck = new JButton("Ellenőrzés");
         this.btn_recheck.setAlignmentX(1.0F);
         this.checking = new JLabel("Ellenőrzés folyamatban...");
         JPanel var2 = new JPanel();
         var2.setLayout(new BoxLayout(var2, 0));
         var2.setAlignmentX(0.0F);
         var2.add(this.btn_save, (Object)null);
         var2.add(Box.createGlue(), (Object)null);
         var2.add(this.checking);
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
               ELDialog.this.setListDataModel();
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
                  var4.setSelectedFile(new File("Javas_ABEV_Üzenetek.txt"));
                  var4.setCurrentDirectory(new File((String)PropertyList.getInstance().get("prop.usr.naplo")));
               }

               while(true) {
                  int var3 = var4.showSaveDialog(SwingUtilities.getRoot(ELDialog.this));
                  if (var3 != 0) {
                     break;
                  }

                  DefaultListModel var5 = (DefaultListModel)ELDialog.this.lst_messages.getModel();
                  File var6 = var4.getSelectedFile();
                  if (!var6.exists() || 1 != JOptionPane.showConfirmDialog(SwingUtilities.getRoot(ELDialog.this), var6.getName() + " állomány létezik ! Felülírja ?", "Üzenetek állományba mentése", 0)) {
                     try {
                        BufferedWriter var7 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var6), "ISO-8859-2"));

                        try {
                           var7.write("Mentés dátuma: " + var2.format(new Date()));
                           var7.newLine();
                           var7.write("Nyomtatvány: " + ELDialog.this.getFormName());
                           var7.newLine();
                           var7.write("Üzenet elválasztó: |");
                           var7.newLine();
                           var7.write("Üzenetek:");
                           var7.newLine();
                           int var10 = 0;

                           for(int var11 = var5.getSize(); var10 < var11; ++var10) {
                              Object var9;
                              if ((var9 = var5.getElementAt(var10)) instanceof ErrorListDialog.ELDialog.ListItem) {
                                 ErrorListDialog.ELDialog.ListItem var12 = (ErrorListDialog.ELDialog.ListItem)var9;
                                 Object[] var8 = var12.item;
                                 int var13 = 0;

                                 for(int var14 = var8.length; var13 < var14; ++var13) {
                                    var9 = var8[var13];
                                    if (var13 == 4 && var12.getIcon() instanceof Icon) {
                                       var9 = ErrorListDialog.ELDialog.getIconName(var12.getIcon());
                                    }

                                    if (var13 == 3 && var9 instanceof JButton) {
                                       var9 = "";
                                    }

                                    if (var9 == null) {
                                       var9 = "";
                                    }

                                    if (var13 == 1) {
                                       String var22 = var9.toString().replaceAll("#13", "");
                                       var9 = var22.toString().replaceAll("\\n", " ");
                                    }

                                    var7.write("" + (var13 == 0 ? "" : "|") + var9);
                                 }

                                 var7.newLine();
                              }
                           }

                           return;
                        } catch (IOException var19) {
                           ErrorListDialog.writeLog("Hiba történt az üzenetek elmentésekor: " + var19);
                           break;
                        } finally {
                           var7.flush();
                           var7.close();
                           GuiUtil.showMessageDialog(SwingUtilities.getRoot(ELDialog.this), "Az üzeneteket a(z) '" + var6.getName() + "' nevű állományba mentettük.", "Hibalista elmentése", 1);
                        }
                     } catch (Exception var21) {
                        ErrorListDialog.writeLog("Hiba történt az üzenetek elmentésekor: " + var21);
                        break;
                     }
                  }
               }

            }
         });
         this.btn_ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               ELDialog.this.setVisible(false);
               ELDialog.this.dispose();
               CalculatorManager.getInstance().end_formcheck();
            }
         });
         this.btn_recheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               ELDialog.this.doErrorCheck();
            }
         });
         this.lst_messages.setCellRenderer(new ListCellRenderer() {
            private final JLabel renderer = new JLabel();

            public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
               this.renderer.setOpaque(true);
               if (var2 instanceof ErrorListDialog.ELDialog.ListItem) {
                  ErrorListDialog.ELDialog.ListItem var6 = (ErrorListDialog.ELDialog.ListItem)var2;
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
               Object var2 = ELDialog.this.lst_messages.getSelectedValue();
               ELDialog.this.txt_message.setText(var2 == null ? "" : var2.toString());
            }
         });
         this.lst_messages.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent var1) {
               if (var1.getClickCount() == 2) {
                  Object var2 = ELDialog.this.lst_messages.getSelectedValue();
                  if (var2 instanceof ErrorListDialog.ELDialog.ListItem && ((ErrorListDialog.ELDialog.ListItem)var2).item instanceof Object[]) {
                     Object[] var3 = ((ErrorListDialog.ELDialog.ListItem)var2).item;
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
         this.dlm_all = new DefaultListModel();
         this.dlm_errors = new DefaultListModel();
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
         } else if (var0 == ENYKIconSet.getInstance().get("statusz_kek")) {
            return "Végzetes feldobott hiba";
         } else {
            return var0 == ENYKIconSet.getInstance().get("statusz_fekete") ? "Egyéb hiba" : "?";
         }
      }

      private void doErrorCheck() {
         SettingsStore var1 = SettingsStore.getInstance();
         String var2 = var1.get("gui", "detail");
         this.detailmode = var2 == null ? true : "true".equals(var2);
         DefaultListModel var3 = new DefaultListModel();

         try {
            this.is_returned = false;
            this.btn_ok.setEnabled(false);
            this.btn_recheck.setEnabled(false);
            this.btn_save.setEnabled(false);
            var3.addElement("Hibalista készítése folyamatban ...");
            this.lst_messages.setModel(var3);
            this.checking.setText("Ellenőrzés folyamatban...");
            if (this.dlm_all != null) {
               this.dlm_all.clear();
               this.dlm_all.addElement(this.getFormId());
            }

            if (this.dlm_errors != null) {
               this.dlm_errors.clear();
               this.dlm_errors.addElement(this.getFormId());
            }

            this.lst_messages.repaint();

            IErrorList var4;
            try {
               var4 = ErrorList.getInstance();
            } catch (Exception var16) {
               var4 = null;
            }

            if (var4 instanceof IEventSupport) {
               IEventSupport var5 = (IEventSupport)var4;

               try {
                  var5.addEventListener(this);
                  if (this.error_list_co.getCalculatorManager() != null) {
                     this.modalDialog.setVisible(true);
                  }
               } finally {
                  var5.removeEventListener(this);
                  this.checking.setText("Ellenőrzés befejezve.");
                  this.setListDataModel();
                  this.btn_save.setEnabled(true);
                  this.btn_recheck.setEnabled(true);
                  this.btn_ok.setEnabled(true);
                  this.invalidate();
                  this.validate();

                  try {
                     RepaintManager.currentManager(this).addDirtyRegion(this.panel, 0, 0, this.panel.getWidth(), this.panel.getHeight());
                     RepaintManager.currentManager(this).paintDirtyRegions();
                     MainFrame.thisinstance.mp.getDMFV().fv.pv.pv.refresh();
                  } catch (Exception var14) {
                     Tools.eLog(var14, 0);
                  }

               }
            }
         } catch (Exception var17) {
            var17.printStackTrace();
         }

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

      private String getFormId() {
         String var1 = Calculator.getInstance().getBookModel().get_formid();
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

      private void createModalDialog() {
         this.modalDialog.setLocationRelativeTo(MainFrame.thisinstance);
         this.modalDialog.setLayout(new BorderLayout());
         Dimension var1 = new Dimension(400, 60);
         this.modalDialog.add(this.checking, "Center");
         this.modalDialog.setSize(var1);
         this.modalDialog.addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent var1) {
               ELDialog.this.error_list_co.getCalculatorManager().do_check(ELDialog.this);

               while(!ELDialog.this.is_returned) {
                  try {
                     Thread.sleep(10L);
                  } catch (InterruptedException var3) {
                     Tools.eLog(var3, 0);
                  }
               }

               ELDialog.this.modalDialog.setVisible(false);
            }
         });
      }

      private class ListItem {
         public Object[] item;

         public ListItem(Object[] var2) {
            this.item = var2;
         }

         public Icon getIcon() {
            return this.item != null && 4 < this.item.length ? (Icon)ErrorListDialog.ELDialog.getIcon(this.item[4]) : null;
         }

         public String toString() {
            return this.item != null && this.item.length > 1 && this.item[1] != null ? ELDialog.this.detail_filter(this.item[1].toString().replaceAll("#13", " ")) : "";
         }
      }
   }
}
