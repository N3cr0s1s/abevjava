package hu.piller.enykp.util.base.messageinfo;

import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.filepanels.tablesorter.TableSorter;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.EventLog;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import hu.piller.enykp.util.icon.ENYKIconSet;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class MessageInfoBusiness implements IEventListener {
   private static final int ID_FIELD_VISIBLE = 0;
   private static final int ID_FIELD_ID = 1;
   private static final int MSG_FIELD_ERR_MSG_ITEM = 0;
   private static final int MSG_FIELD_ID = 1;
   private static final int MSG_FIELD_TYPE = 2;
   private static final int MSG_FIELD_MESSAGE = 3;
   private static final int MSG_FIELD_EXCEPTION = 4;
   private static final int MSG_FIELD_ACTION = 5;
   private static final String FORM_MSG_ID_FIELD = "4001";
   private static final String FORM_MSG_ID_FORM = "4002";
   private static final String MSG_SEPARATOR = "|";
   private static final String CHAR_SET = "ISO-8859-2";
   private MessageInfoPanel message_info_panel;
   private JCheckBox chk_show_only_form_msgs;
   private JButton btn_save_messages;
   private JTable id_table;
   private JTable msg_table;
   private JTextArea msg_area;
   private JTextArea err_area;
   private JScrollPane sp_err_area;
   private JLabel lbl_err_area;
   private JCheckBox show_exception;
   private JButton refresh;
   private JButton clear;
   private JCheckBox chk_show_fat_err_msg;
   private JCheckBox chk_show_err_msg;
   private JCheckBox chk_show_wrn_msg;
   private JCheckBox chk_show_msg_msg;
   private JCheckBox chk_show_gen_err_msg;
   private JLabel lbl_show_fat_err_msg;
   private JLabel lbl_show_err_msg;
   private JLabel lbl_show_gen_err_msg;
   private JLabel lbl_show_wrn_msg;
   private JLabel lbl_show_msg_msg;
   private JLabel lbl_msg_id;
   private JScrollPane scp_msg_id;
   private TableColumn exception_column;
   private Hashtable visible_ids;
   private TableSorter msg_table_sorter;
   private TableSorter id_table_sorter;
   private static final String SETTINGS_KEY_SHOW_ONLY_FORM_MESSAGES = "show_only_form_messages";
   private static final String YES = "igen";
   private static final String NO = "nem";

   public Object eventFired(Event var1) {
      Object var2 = var1.getUserData();
      if (var2 instanceof Hashtable) {
         this.fillIdTable();
      }

      return null;
   }

   public MessageInfoBusiness(MessageInfoPanel var1) {
      this.message_info_panel = var1;
      this.chk_show_only_form_msgs = (JCheckBox)this.message_info_panel.getIPComponent("show_only_form_check_messages");
      this.btn_save_messages = (JButton)this.message_info_panel.getIPComponent("save_form_messages");
      this.id_table = (JTable)this.message_info_panel.getIPComponent("message_ids");
      this.chk_show_fat_err_msg = (JCheckBox)this.message_info_panel.getIPComponent("show_fat_err_msg_chk");
      this.chk_show_err_msg = (JCheckBox)this.message_info_panel.getIPComponent("show_err_msg_chk");
      this.chk_show_wrn_msg = (JCheckBox)this.message_info_panel.getIPComponent("show_wrn_msg_chk");
      this.chk_show_msg_msg = (JCheckBox)this.message_info_panel.getIPComponent("show_msg_msg_chk");
      this.chk_show_gen_err_msg = (JCheckBox)this.message_info_panel.getIPComponent("show_gen_err_msg_chk");
      this.lbl_show_gen_err_msg = (JLabel)this.message_info_panel.getIPComponent("show_gen_err_msg_lbl");
      this.lbl_show_fat_err_msg = (JLabel)this.message_info_panel.getIPComponent("show_fat_err_msg_lbl");
      this.lbl_show_err_msg = (JLabel)this.message_info_panel.getIPComponent("show_err_msg_lbl");
      this.lbl_show_wrn_msg = (JLabel)this.message_info_panel.getIPComponent("show_wrn_msg_lbl");
      this.lbl_show_msg_msg = (JLabel)this.message_info_panel.getIPComponent("show_msg_msg_lbl");
      this.msg_table = (JTable)this.message_info_panel.getIPComponent("messages");
      this.msg_area = (JTextArea)this.message_info_panel.getIPComponent("message");
      this.lbl_err_area = (JLabel)this.message_info_panel.getIPComponent("exception_label");
      this.err_area = (JTextArea)this.message_info_panel.getIPComponent("exception");
      this.sp_err_area = (JScrollPane)this.message_info_panel.getIPComponent("exception_sp");
      this.show_exception = (JCheckBox)this.message_info_panel.getIPComponent("show_exceptions");
      this.refresh = (JButton)this.message_info_panel.getIPComponent("refresh");
      this.clear = (JButton)this.message_info_panel.getIPComponent("clear");
      this.lbl_msg_id = (JLabel)this.message_info_panel.getIPComponent("msg_id_lbl");
      this.scp_msg_id = (JScrollPane)this.message_info_panel.getIPComponent("msg_id_scp");
      this.id_table_sorter = new TableSorter();
      this.msg_table_sorter = new TableSorter();
      this.prepare();
   }

   private void prepare() {
      this.visible_ids = new Hashtable(64);
      this.prepareShowOnlyFormMsgs();
      this.prepareSaveMessages();
      this.prepareIdTable();
      this.prepareShowFatErrMsg();
      this.prepareShowErrMsg();
      this.prepareShowWrnMsg();
      this.prepareShowGenErrMsg();
      this.prepareShowMsgMsg();
      this.prepareMsgTable();
      this.prepareMsgArea();
      this.prepareErrArea();
      this.prepareShowException();
      this.prepareRefresh();
      this.prepareClear();
      this.loadSettings();
      this.arrangePanel();
      this.fillIdTable();
   }

   private void prepareShowOnlyFormMsgs() {
      this.chk_show_only_form_msgs.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            MessageInfoBusiness.this.saveSettings();
            MessageInfoBusiness.this.arrangePanel();
            MessageInfoBusiness.this.fillIdTable();
         }
      });
      this.chk_show_only_form_msgs.setVisible(false);
      this.chk_show_only_form_msgs.setSelected(false);
   }

   private void prepareSaveMessages() {
      this.btn_save_messages.addActionListener(new ActionListener() {
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
               var4.setSelectedFile(new File("Javas_ABev_üzenetek.txt"));

               try {
                  var4.setCurrentDirectory(new File((String)PropertyList.getInstance().get("prop.usr.naplo")));
               } catch (Exception var18) {
                  Tools.eLog(var18, 0);
               }
            }

            while(true) {
               int var3 = var4.showSaveDialog(SwingUtilities.getRoot(MessageInfoBusiness.this.message_info_panel));
               if (var3 != 0) {
                  break;
               }

               TableModel var5 = MessageInfoBusiness.this.msg_table.getModel();
               File var6 = var4.getSelectedFile();
               if (!var6.exists() || 1 != JOptionPane.showConfirmDialog(SwingUtilities.getRoot(MessageInfoBusiness.this.message_info_panel), var6.getName() + " állomány létezik ! Felülírja ?", "Üzenetek állományba mentése", 0)) {
                  try {
                     BufferedWriter var7 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var6), "ISO-8859-2"));

                     try {
                        var7.write("Mentés dátuma: " + var2.format(new Date()));
                        var7.newLine();
                        var7.write("Nyomtatvány: " + MessageInfoBusiness.this.getFormName());
                        var7.newLine();
                        var7.write("Üzenet elválasztó: |");
                        var7.newLine();
                        var7.write("Üzenetek:");
                        var7.newLine();
                        int var8 = 0;

                        for(int var9 = var5.getRowCount(); var8 < var9; ++var8) {
                           int var10 = 1;

                           for(int var11 = var5.getColumnCount(); var10 < var11; ++var10) {
                              Object var12 = var5.getValueAt(var8, var10);
                              if (var12 instanceof ImageIcon) {
                                 var12 = MessageInfoBusiness.this.getIconName((ImageIcon)var12);
                              }

                              if (var12 instanceof AbstractButton) {
                                 var12 = "";
                              }

                              if (var12 == null) {
                                 var12 = "";
                              }

                              var7.write("" + (var10 == 0 ? "" : "|") + var12.toString().replaceAll("\n", ""));
                           }

                           var7.newLine();
                        }

                        return;
                     } catch (IOException var19) {
                        MessageInfoBusiness.this.writeLog("Hiba történt az üzenetek elmentésekor: " + var19);
                        break;
                     } finally {
                        var7.flush();
                        var7.close();
                        MessageInfoBusiness.this.writeLog("Üzenetek a(z) '" + var6.getName() + "' nevű állományba el lettek mentve.");
                        MessageInfoBusiness.this.writeError("ÜP", "Üzenetek a(z) '" + var6.getName() + "' nevű állományba el lettek mentve.", IErrorList.LEVEL_SHOW_MESSAGE, (Exception)null, (Object)null);
                        MessageInfoBusiness.this.fillIdTable();
                     }
                  } catch (Exception var21) {
                     MessageInfoBusiness.this.writeLog("Hiba történt az üzenetek elmentésekor: " + var21);
                     break;
                  }
               }
            }

         }
      });
   }

   private void prepareIdTable() {
      TableModel var1 = this.getIdTableModel();
      var1.addTableModelListener(new TableModelListener() {
         public void tableChanged(TableModelEvent var1) {
            if (var1.getType() == 0) {
               TableModel var2 = (TableModel)var1.getSource();
               int var3 = var1.getFirstRow();
               MessageInfoBusiness.this.visible_ids.put(var2.getValueAt(var3, 1), var2.getValueAt(var3, 0));
               MessageInfoBusiness.this.fillIdTable();
            }

         }
      });
      this.id_table.setModel(var1);
      this.id_table.getTableHeader().setReorderingAllowed(false);
      this.id_table.setSelectionMode(0);
      TableColumnModel var2 = this.id_table.getColumnModel();
      TableColumn var3 = var2.getColumn(0);
      var3.setMaxWidth(70);
      var3.setMinWidth(70);
      var3.setResizable(false);
      this.id_table_sorter.attachTable(this.id_table);
      this.id_table_sorter.setSortEnabled(true);
   }

   private TableModel getIdTableModel() {
      return new MessageInfoBusiness.IdTableModel();
   }

   private void prepareShowFatErrMsg() {
      this.chk_show_fat_err_msg.setToolTipText("Végzetes hiba üzenet láthatóság kapcsoló");
      this.chk_show_fat_err_msg.setSelected(true);
      this.chk_show_fat_err_msg.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            MessageInfoBusiness.this.fillMessageTable();
         }
      });
      this.lbl_show_fat_err_msg.setToolTipText("Végzetes hiba üzenet jele");
   }

   private void prepareShowErrMsg() {
      this.chk_show_err_msg.setToolTipText("Hiba üzenet láthatóság kapcsoló");
      this.chk_show_err_msg.setSelected(true);
      this.chk_show_err_msg.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            MessageInfoBusiness.this.fillMessageTable();
         }
      });
      this.lbl_show_err_msg.setToolTipText("Hiba üzenet jele");
   }

   private void prepareShowGenErrMsg() {
      this.chk_show_gen_err_msg.setToolTipText("Egyéb hiba üzenet láthatóság kapcsoló");
      this.chk_show_gen_err_msg.setSelected(true);
      this.chk_show_gen_err_msg.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            MessageInfoBusiness.this.fillMessageTable();
         }
      });
      this.lbl_show_gen_err_msg.setToolTipText("Egyéb hiba jele");
   }

   private void prepareShowWrnMsg() {
      this.chk_show_wrn_msg.setToolTipText("Figyelmeztető üzenet láthatóság kapcsoló");
      this.chk_show_wrn_msg.setSelected(true);
      this.chk_show_wrn_msg.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            MessageInfoBusiness.this.fillMessageTable();
         }
      });
      this.lbl_show_wrn_msg.setToolTipText("Figyelmeztető üzenet jele");
   }

   private void prepareShowMsgMsg() {
      this.chk_show_msg_msg.setToolTipText("Egyéb üzenet láthatóság kapcsoló");
      this.chk_show_msg_msg.setSelected(true);
      this.chk_show_msg_msg.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            MessageInfoBusiness.this.fillMessageTable();
         }
      });
      this.lbl_show_msg_msg.setToolTipText("Egyéb üzenet jele");
   }

   private void prepareMsgTable() {
      this.msg_table.setDefaultRenderer(JButton.class, new MessageInfoBusiness.ButtonTableCellRenderer());
      this.msg_table.setDefaultEditor(JButton.class, new MessageInfoBusiness.ButtonTableCellEditor());
      this.msg_table.setSelectionMode(0);
      if (GuiUtil.modGui()) {
         this.msg_table.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
      }

      TableModel var1 = this.getMsgTableModel();
      this.msg_table.setModel(var1);
      this.msg_table.getTableHeader().setReorderingAllowed(false);
      TableColumnModel var2 = this.msg_table.getColumnModel();
      TableColumn var3 = var2.getColumn(0);
      var3.setMaxWidth(0);
      var3.setMinWidth(0);
      var3.setPreferredWidth(0);
      var3.setResizable(false);
      var3 = var2.getColumn(1);
      var3.setMaxWidth(70);
      var3.setMinWidth(70);
      var3.setResizable(false);
      var3 = var2.getColumn(2);
      var3.setMaxWidth(GuiUtil.getToolBarHeight() + 4);
      var3.setMinWidth(GuiUtil.getToolBarHeight() + 4);
      var3.setResizable(false);
      var3 = var2.getColumn(3);
      var3.setMinWidth(100);
      var3.setPreferredWidth(200);
      var3 = var2.getColumn(5);
      var3.setMinWidth(0);
      var3.setMaxWidth(0);
      this.msg_table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent var1) {
            int var2 = MessageInfoBusiness.this.msg_table.getSelectedRow();
            Object var3 = null;
            Object var4 = null;
            TableModel var5 = MessageInfoBusiness.this.msg_table.getModel();
            if (var5.getRowCount() > var2 && var2 >= 0) {
               var3 = var5.getValueAt(var2, 3);
               var4 = var5.getValueAt(var2, 4);
            }

            MessageInfoBusiness.this.msg_area.setText(var3 == null ? "" : var3.toString());
            MessageInfoBusiness.this.err_area.setText(var4 == null ? "" : var4.toString());
         }
      });
      this.msg_table.addMouseMotionListener(new MouseMotionListener() {
         private Object v;

         public void mouseDragged(MouseEvent var1) {
            this.v = null;
         }

         public void mouseMoved(MouseEvent var1) {
            Point var2 = var1.getPoint();
            int var3 = MessageInfoBusiness.this.msg_table.columnAtPoint(var2);

            try {
               Object var4 = MessageInfoBusiness.this.msg_table.getValueAt(MessageInfoBusiness.this.msg_table.rowAtPoint(var2), var3);
               if (var4 != this.v) {
                  this.v = var4;
                  TableCellRenderer var5 = MessageInfoBusiness.this.msg_table.getDefaultRenderer(String.class);
                  if (var5 instanceof JLabel) {
                     JLabel var6 = (JLabel)var5;
                     String var7;
                     if (this.v instanceof JButton) {
                        var7 = ((JButton)this.v).getText();
                     } else {
                        var7 = this.v.toString();
                     }

                     if (var7 != null && !(this.v instanceof Icon)) {
                        var6.setText(var7);
                        double var8 = var6.getPreferredSize().getWidth();
                        double var10 = (double)MessageInfoBusiness.this.msg_table.getColumnModel().getColumn(var3).getWidth();
                        var10 -= 2.0D;
                        if (this.v instanceof JButton) {
                           Insets var12 = ((JButton)this.v).getMargin();
                           Icon var13 = ((JButton)this.v).getIcon();
                           int var14 = ((JButton)this.v).getIconTextGap();
                           var10 -= (double)(var12.left + var12.right + var14 + (var13 == null ? 0 : var13.getIconWidth()));
                        }

                        if (var8 > var10) {
                           MessageInfoBusiness.this.msg_table.setToolTipText(var7);
                        } else {
                           MessageInfoBusiness.this.msg_table.setToolTipText((String)null);
                        }
                     } else {
                        MessageInfoBusiness.this.msg_table.setToolTipText((String)null);
                     }
                  }
               }
            } catch (Exception var15) {
               MessageInfoBusiness.this.msg_table.setToolTipText((String)null);
            }

         }
      });
      this.msg_table.getTableHeader().addMouseMotionListener(new MouseMotionListener() {
         int i = -2;

         public void mouseDragged(MouseEvent var1) {
            this.i = -2;
         }

         public void mouseMoved(MouseEvent var1) {
            Point var2 = var1.getPoint();
            JTableHeader var3 = MessageInfoBusiness.this.msg_table.getTableHeader();

            try {
               int var4 = var3.columnAtPoint(var2);
               if (var4 != this.i) {
                  this.i = var4;
                  TableCellRenderer var5 = MessageInfoBusiness.this.msg_table.getDefaultRenderer(String.class);
                  if (var5 instanceof JLabel) {
                     JLabel var6 = (JLabel)var5;
                     String var7 = MessageInfoBusiness.this.msg_table.getColumnName(this.i);
                     Object var8 = null;
                     var6.setText(var7);
                     double var9 = var6.getPreferredSize().getWidth();
                     var9 += var8 == null ? 0.0D : (double)((Icon)var8).getIconWidth();
                     var9 += (double)var6.getIconTextGap();
                     if (var9 > (double)MessageInfoBusiness.this.msg_table.getColumnModel().getColumn(var4).getWidth()) {
                        var3.setToolTipText(MessageInfoBusiness.this.msg_table.getColumnName(this.i));
                     } else {
                        var3.setToolTipText((String)null);
                     }
                  }
               }
            } catch (Exception var11) {
               var3.setToolTipText((String)null);
            }

         }
      });
      this.msg_table_sorter.attachTable(this.msg_table);
      this.msg_table_sorter.setSortEnabled(true);
   }

   private TableModel getMsgTableModel() {
      return new MessageInfoBusiness.MsgTableModel();
   }

   private void prepareMsgArea() {
      this.msg_area.setEditable(false);
      this.msg_area.setLineWrap(true);
      this.msg_area.setWrapStyleWord(true);
   }

   private void prepareErrArea() {
      this.err_area.setEditable(false);
      this.err_area.setLineWrap(true);
      this.err_area.setWrapStyleWord(true);
   }

   private void prepareShowException() {
      this.show_exception.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            MessageInfoBusiness.this.setExceptionColumnVisible(MessageInfoBusiness.this.show_exception.isSelected());
            MessageInfoBusiness.this.lbl_err_area.setVisible(MessageInfoBusiness.this.show_exception.isSelected());
            MessageInfoBusiness.this.sp_err_area.setVisible(MessageInfoBusiness.this.show_exception.isSelected());
            MessageInfoBusiness.this.message_info_panel.revalidate();
            MessageInfoBusiness.this.message_info_panel.repaint();
         }
      });
      this.show_exception.setVisible(false);
      this.setExceptionColumnVisible(true);
      this.lbl_err_area.setVisible(true);
      this.sp_err_area.setVisible(true);
   }

   private void setExceptionColumnVisible(boolean var1) {
      if (var1) {
         if (this.exception_column != null) {
            this.msg_table.getColumnModel().addColumn(this.exception_column);
            this.msg_table.getColumnModel().moveColumn(this.msg_table.getColumnModel().getColumnCount() - 1, 4);
         }
      } else {
         this.exception_column = this.msg_table.getColumnModel().getColumn(4);
         this.msg_table.getColumnModel().removeColumn(this.exception_column);
      }

      this.msg_table.revalidate();
      this.msg_table.repaint();
   }

   private void prepareRefresh() {
      this.refresh.setVisible(false);
      this.refresh.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            MessageInfoBusiness.this.fillIdTable();
         }
      });
   }

   private void prepareClear() {
      this.clear.setVisible(true);
      this.clear.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            try {
               MessageInfoBusiness.this.clearMessages(true);
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

         }
      });
   }

   public void clearMessages(boolean var1) {
      IErrorList var2 = ErrorList.getInstance();
      if (var2 != null && var2.getItems().length > 0) {
         boolean var3 = true;
         if (var1) {
            var3 = JOptionPane.showConfirmDialog(SwingUtilities.getRoot(this.message_info_panel), "Biztosan törölni akarja a hiba listát ?", "Megerősítés", 0) == 0;
         }

         if (var3) {
            var2.clear();
            this.visible_ids.clear();
            this.id_table_sorter.clearOriModel();
            ((DefaultTableModel)this.id_table.getModel()).getDataVector().clear();
            this.id_table.revalidate();
            this.id_table.repaint();
            this.msg_table_sorter.clearOriModel();
            ((DefaultTableModel)this.msg_table.getModel()).getDataVector().clear();
            this.msg_table.revalidate();
            this.msg_table.repaint();
            this.msg_area.setText("");
            this.err_area.setText("");
         }
      }

   }

   public void installEventListener() {
      this.uninstallEventListener();
      ((IEventSupport)ErrorList.getInstance()).addEventListener(this);
   }

   public void uninstallEventListener() {
      ((IEventSupport)ErrorList.getInstance()).removeEventListener(this);
   }

   public void arrangePanel() {
      if (!this.chk_show_only_form_msgs.isSelected()) {
      }

      this.lbl_msg_id.setVisible(false);
      this.scp_msg_id.setVisible(false);
      TableColumn var1 = this.msg_table.getColumnModel().getColumn(1);
      var1.setMinWidth(0);
      var1.setPreferredWidth(0);
      var1.setResizable(false);
      this.msg_table.revalidate();
      this.msg_table.repaint();
   }

   public synchronized void fillIdTable() {
      try {
         IErrorList var1 = ErrorList.getInstance();
         if (var1 != null) {
            Object[] var2 = var1.getIdList();
            DefaultTableModel var3 = (DefaultTableModel)this.id_table_sorter.getTable().getModel();
            int var4 = 0;
            int var5 = 0;

            for(int var6 = var2.length; var5 < var6; ++var5) {
               if (!this.visible_ids.containsKey(var2[var5])) {
                  if (this.visible_ids.get(var2[var5]) == null) {
                     this.visible_ids.put(var2[var5], Boolean.TRUE);
                  }

                  var3.addRow(new Object[]{this.visible_ids.get(var2[var5]), var2[var5]});
                  ++var4;
               }
            }

            if (var4 > 0) {
               this.id_table_sorter.sort();
            }

            this.fillMessageTable();
         }
      } catch (Exception var7) {
         Tools.eLog(var7, 0);
      }

   }

   private synchronized void fillMessageTable() {
      try {
         IErrorList var7 = ErrorList.getInstance();
         DefaultTableModel var1 = (DefaultTableModel)this.msg_table_sorter.getTable().getModel();
         int var10 = var1.getDataVector().size();
         var1.getDataVector().clear();
         this.msg_table_sorter.clearOriModel();
         int var8 = 0;
         int var9 = 0;
         Object[] var3 = var7.getItems();
         int var11 = 0;

         for(int var12 = var3.length; var11 < var12; ++var11) {
            Object[] var2 = (Object[])((Object[])var3[var11]);
            Object var4 = var2[4];
            if ((Boolean)this.visible_ids.get(var2[0]) && this.isMsgIdShowable(var2[0]) && this.isShowable(var4)) {
               if (var9 < var1.getRowCount() && var1.getValueAt(var9, 0) == var2) {
                  ++var9;
               } else {
                  Object var5 = var2[2];
                  Object var6 = var2[3];
                  if (!(var6 instanceof AbstractButton)) {
                     var6 = null;
                  }

                  CharArrayWriter var13 = new CharArrayWriter();
                  if (var5 != null) {
                     ((Exception)var5).printStackTrace(new PrintWriter(var13));
                  }

                  var1.insertRow(var9, new Object[]{var2, var2[0], this.getIcon(var4), this.formatMessage(var2[1]), var5 == null ? var5 : var13.toString(), var6});
                  ++var9;
                  ++var8;
               }
            } else if (var9 < var1.getRowCount()) {
               while(var1.getValueAt(var9, 0) == var2) {
                  var1.removeRow(var9);
                  ++var8;
                  if (var9 >= var1.getRowCount()) {
                     break;
                  }
               }
            }
         }

         if (var8 > 0 || var10 > 0) {
            this.msg_table_sorter.sort();
            this.msg_table.revalidate();
            this.msg_table.repaint();
         }
      } catch (Exception var14) {
         Tools.eLog(var14, 0);
      }

   }

   private Object formatMessage(Object var1) {
      String var2;
      if (var1 == null) {
         var2 = "";
      } else {
         var2 = var1.toString().replaceAll("#13", "");
      }

      return var2;
   }

   private boolean isMsgIdShowable(Object var1) {
      if (var1 != null) {
         String var2 = var1.toString();
         if (!this.chk_show_only_form_msgs.isSelected()) {
            return true;
         } else {
            return var2.equalsIgnoreCase("4001") || var2.equalsIgnoreCase("4002");
         }
      } else {
         return false;
      }
   }

   private boolean isShowable(Object var1) {
      int var2 = this.getInt(var1);
      if (var2 == IErrorList.LEVEL_MESSAGE && !this.chk_show_msg_msg.isSelected()) {
         return false;
      } else if (var2 == IErrorList.LEVEL_WARNING && !this.chk_show_wrn_msg.isSelected()) {
         return false;
      } else if (var2 == IErrorList.LEVEL_ERROR && !this.chk_show_err_msg.isSelected()) {
         return false;
      } else if (var2 == IErrorList.LEVEL_FATAL_ERROR && !this.chk_show_fat_err_msg.isSelected()) {
         return false;
      } else if (var2 == IErrorList.LEVEL_SHOW_MESSAGE && !this.chk_show_msg_msg.isSelected()) {
         return false;
      } else if (var2 == IErrorList.LEVEL_SHOW_WARNING && !this.chk_show_wrn_msg.isSelected()) {
         return false;
      } else if (var2 == IErrorList.LEVEL_SHOW_ERROR && !this.chk_show_err_msg.isSelected()) {
         return false;
      } else if (var2 == IErrorList.LEVEL_SHOW_FATAL_ERROR && !this.chk_show_fat_err_msg.isSelected()) {
         return false;
      } else {
         return var2 != IErrorList.LEVEL_SHOW_GENERIC_ERROR || this.chk_show_gen_err_msg.isSelected();
      }
   }

   private Object getIcon(Object var1) {
      int var2 = this.getInt(var1);
      if (var2 == IErrorList.LEVEL_MESSAGE) {
         return ENYKIconSet.getInstance().get("statusz_zold");
      } else if (var2 == IErrorList.LEVEL_WARNING) {
         return ENYKIconSet.getInstance().get("statusz_sarga");
      } else if (var2 == IErrorList.LEVEL_ERROR) {
         return ENYKIconSet.getInstance().get("statusz_piros");
      } else if (var2 == IErrorList.LEVEL_FATAL_ERROR) {
         return ENYKIconSet.getInstance().get("statusz_kek");
      } else if (var2 == IErrorList.LEVEL_SHOW_MESSAGE) {
         return ENYKIconSet.getInstance().get("statusz_zold");
      } else if (var2 == IErrorList.LEVEL_SHOW_WARNING) {
         return ENYKIconSet.getInstance().get("statusz_sarga");
      } else if (var2 == IErrorList.LEVEL_SHOW_ERROR) {
         return ENYKIconSet.getInstance().get("statusz_piros");
      } else if (var2 == IErrorList.LEVEL_SHOW_FATAL_ERROR) {
         return ENYKIconSet.getInstance().get("statusz_kek");
      } else {
         return var2 == IErrorList.LEVEL_SHOW_GENERIC_ERROR ? ENYKIconSet.getInstance().get("statusz_piros") : var1;
      }
   }

   private int getInt(Object var1) {
      if (var1 instanceof Integer) {
         return (Integer)var1;
      } else if (var1 instanceof Long) {
         return ((Long)var1).intValue();
      } else if (var1 instanceof Short) {
         return ((Short)var1).intValue();
      } else if (var1 instanceof Float) {
         return ((Float)var1).intValue();
      } else {
         return var1 instanceof Double ? ((Double)var1).intValue() : -1;
      }
   }

   private String getIconName(ImageIcon var1) {
      if (var1 == ENYKIconSet.getInstance().get("statusz_zold")) {
         return "Üzenet";
      } else if (var1 == ENYKIconSet.getInstance().get("statusz_sarga")) {
         return "Figyelmeztetés";
      } else if (var1 == ENYKIconSet.getInstance().get("statusz_piros")) {
         return "Hiba";
      } else if (var1 == ENYKIconSet.getInstance().get("statusz_kek")) {
         return "Végzetes hiba";
      } else if (var1 == ENYKIconSet.getInstance().get("statusz_zold")) {
         return "Feldobott üzenet";
      } else if (var1 == ENYKIconSet.getInstance().get("statusz_sarga")) {
         return "Feldobott figyelmeztetés";
      } else if (var1 == ENYKIconSet.getInstance().get("statusz_piros")) {
         return "Feldobott hiba";
      } else {
         return var1 == ENYKIconSet.getInstance().get("statusz_kek") ? "Feldobott végzetes hiba" : "?";
      }
   }

   private String getFormName() {
      BookModel var1 = Calculator.getInstance().getBookModel();
      return var1 != null ? var1.get_formid() : "(Nincs)";
   }

   private void loadSettings() {
   }

   private void saveSettings() {
   }

   private void writeError(Object var1, String var2, Integer var3, Exception var4, Object var5) {
      ErrorList.getInstance().writeError(var1, var2, var3, var4, var5);
   }

   private void writeLog(Object var1) {
      try {
         EventLog.getInstance().logEvent(var1);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   private class ButtonTableCellEditor implements TableCellEditor {
      private JButton editor;
      private Vector listeners;

      private ButtonTableCellEditor() {
         this.listeners = new Vector(7);
      }

      public Component getTableCellEditorComponent(JTable var1, Object var2, boolean var3, int var4, int var5) {
         this.editor = var2 instanceof JButton ? (JButton)var2 : null;
         return this.editor;
      }

      public void cancelCellEditing() {
         int var1 = 0;

         for(int var2 = this.listeners.size(); var1 < var2; ++var1) {
            ((CellEditorListener)this.listeners.get(var1)).editingCanceled(new ChangeEvent(this));
         }

      }

      public boolean stopCellEditing() {
         int var1 = 0;

         for(int var2 = this.listeners.size(); var1 < var2; ++var1) {
            ((CellEditorListener)this.listeners.get(var1)).editingStopped(new ChangeEvent(this));
         }

         return true;
      }

      public Object getCellEditorValue() {
         return this.editor;
      }

      public boolean isCellEditable(EventObject var1) {
         return true;
      }

      public boolean shouldSelectCell(EventObject var1) {
         return false;
      }

      public void addCellEditorListener(CellEditorListener var1) {
         if (!this.listeners.contains(var1)) {
            this.listeners.add(var1);
         }
      }

      public void removeCellEditorListener(CellEditorListener var1) {
         if (this.listeners.contains(var1)) {
            this.listeners.remove(var1);
         }

      }

      // $FF: synthetic method
      ButtonTableCellEditor(Object var2) {
         this();
      }
   }

   private class ButtonTableCellRenderer implements TableCellRenderer {
      private JButton renderer = new JButton();
      private JTextField l_renderer = new JTextField("");

      public ButtonTableCellRenderer() {
         this.l_renderer.setBorder((Border)null);
         this.renderer.setAlignmentX(0.0F);
      }

      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         if (var2 == null) {
            this.l_renderer.setBackground(var3 ? var1.getSelectionBackground() : var1.getBackground());
            return this.l_renderer;
         } else if (var2 instanceof JButton) {
            return (Component)var2;
         } else {
            this.renderer.setText(var2.toString());
            return this.renderer;
         }
      }
   }

   private class MsgTableModel extends DefaultTableModel {
      MsgTableModel() {
         this.addColumn("Hiba üzenet elem");
         this.addColumn("Azonosító");
         this.addColumn("Üzenet fajta");
         this.addColumn("Üzenet");
         this.addColumn("Rendszer üzenet");
         this.addColumn("Művelet");
      }

      public boolean isCellEditable(int var1, int var2) {
         return var1 >= 0 && var2 == this.getColumnCount() - 1;
      }

      public Class getColumnClass(int var1) {
         if (var1 == 5) {
            return JButton.class;
         } else {
            Object var2 = this.getValueAt(0, var1);
            return var2 == null ? Object.class : var2.getClass();
         }
      }
   }

   private class IdTableModel extends DefaultTableModel {
      IdTableModel() {
         this.addColumn("Látható-e");
         this.addColumn("Azonosító");
      }

      public boolean isCellEditable(int var1, int var2) {
         return var1 >= 0 && var2 < 1;
      }

      public Class getColumnClass(int var1) {
         Object var2 = this.getValueAt(0, var1);
         return var2 == null ? Object.class : var2.getClass();
      }
   }
}
