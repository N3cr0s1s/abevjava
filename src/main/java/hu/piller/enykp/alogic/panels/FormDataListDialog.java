package hu.piller.enykp.alogic.panels;

import hu.piller.enykp.alogic.filepanels.datafileoperations.Operations;
import hu.piller.enykp.alogic.filepanels.filepanel.FileBusiness;
import hu.piller.enykp.alogic.filepanels.filepanel.FilePanel;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.ICommandObject;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.eventsupport.CloseEvent;
import me.necrocore.abevjava.NecroFile;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class FormDataListDialog implements ICommandObject {
   private static FormDataListDialog instance;
   public static final String FORM_NAME = "FormDataListDialog";
   private String[] filters = new String[]{"inner_data_loader_v1"};
   public static final String DEFAULT_LIST_FILE_NAME = "nyomtatvany_adatok.txt";
   private final Object[] update_skin = new Object[]{"work_panel", "static", "Nyomtatvány adatok", "tab_close", null};
   private File path = new NecroFile(this.getProperty("prop.usr.root"), this.getProperty("prop.usr.saves"));
   private FilePanel file_panel;
   private FileBusiness business;
   private JDialog dlg;
   private final Vector cmd_list = new Vector(Arrays.asList("abev.showFormDataListDialog"));
   Boolean[] states;

   private FormDataListDialog() {
      this.states = new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
      this.build();
      this.prepare();
   }

   private void build() {
      this.file_panel = new FilePanel();
      this.business = this.file_panel.getBusiness();
      this.business.setTask(4);
   }

   private void prepare() {
      this.business.setButtonExecutor(new FormDataListDialog.ButtonActions(this.file_panel));
      this.update_skin[4] = this.file_panel;
   }

   public static FormDataListDialog getInstance() {
      if (instance == null) {
         instance = new FormDataListDialog();
      }

      return instance;
   }

   public void execute() {
      this.build();
      this.prepare();
      this.dlg = new JDialog(MainFrame.thisinstance);
      boolean var1 = true;
      Container var2 = this.dlg.getContentPane();
      this.business.addFilters(this.filters, (String[])null);
      this.business.setSelectedPath(this.path.toURI());
      this.file_panel.getBusiness().loadFilterSettings("FormDataListDialog");
      if (var1) {
         this.file_panel.invalidate();
         this.file_panel.setVisible(true);
         var2.add(this.file_panel);
         this.dlg.setTitle("Nyomtatvány adatok listázása");
         this.dlg.setSize(GuiUtil.getScreenW() / 2, GuiUtil.getScreenH() / 2);
         this.dlg.setResizable(true);
         this.dlg.setLocationRelativeTo(MainFrame.thisinstance);
         this.dlg.setModal(true);
         this.dlg.setVisible(true);
      }

   }

   public void setParameters(Hashtable var1) {
   }

   public ICommandObject copy() {
      return getInstance();
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

   public Object getState(Object var1) {
      if (var1 instanceof Integer) {
         int var2 = (Integer)var1;
         return var2 >= 0 && var2 <= this.states.length - 1 ? this.states[var2] : Boolean.FALSE;
      } else {
         return Boolean.FALSE;
      }
   }

   private String getProperty(String var1) {
      String var4 = "";
      IPropertyList var2 = PropertyList.getInstance();
      Object var3 = var2.get(var1);
      if (var3 != null) {
         var4 = var3.toString();
      }

      return var4;
   }

   private class ButtonActions extends FileBusiness.ButtonExecutor {
      private static final String CHAR_SET = "ISO-8859-2";
      private static final String COL_SEP = "|";
      private static final String ROW_SEP = "-";
      private static final String ROW_NODE = "+";

      public ButtonActions(FilePanel var2) {
         super(var2);
      }

      public void b11Clicked() {
         JTable var4 = (JTable)this.file_panel.getFPComponent("files");
         TableColumnModel var5 = var4.getColumnModel();
         Vector var1 = new Vector();
         int var7 = 1;

         FormDataListDialog.ButtonActions.ColumnEntry var2;
         TableColumn var6;
         for(int var8 = var4.getColumnCount(); var7 < var8; ++var7) {
            var6 = var5.getColumn(var7);
            var2 = new FormDataListDialog.ButtonActions.ColumnEntry(var4.getColumnName(var7), var6.getMaxWidth() > 0 ? Boolean.TRUE : Boolean.FALSE);
            var1.add(var2);
         }

         FormDataListDialog.ButtonActions.ColumnSelectionDialog var3 = new FormDataListDialog.ButtonActions.ColumnSelectionDialog(var1);
         var7 = var3.dlm.getSize();
         String var12 = "WWWWWWWWWWWWWWWWWWW";

         int var9;
         try {
            for(var9 = 0; var9 < var7; ++var9) {
               String var10 = (String)((FormDataListDialog.ButtonActions.ColumnEntry)var3.dlm.get(var9)).getKey();
               if (var10.length() > var12.length()) {
                  var12 = var10;
               }
            }
         } catch (Exception var11) {
            var12 = "WWWWWWWWWWWWWWWWWWW";
         }

         var3.setSize(GuiUtil.getW(var12 + "WWWW"), 450);
         var3.setTitle("Lista szerkesztés");
         var3.setResizable(true);
         var3.setModal(true);
         var3.setLocationRelativeTo(this.file_panel);
         var3.setVisible(true);
         var9 = 0;

         for(int var13 = var1.size(); var9 < var13; ++var9) {
            var2 = (FormDataListDialog.ButtonActions.ColumnEntry)var1.get(var9);
            var6 = var4.getColumn(var2.getKey());
            if ((Boolean)var2.getValue()) {
               var6.setMaxWidth(Integer.MAX_VALUE);
               var6.setMinWidth(100);
               var6.setResizable(true);
            } else {
               var6.setWidth(0);
               var6.setMinWidth(0);
               var6.setMaxWidth(0);
               var6.setResizable(false);
            }
         }

         var4.repaint();
      }

      public void b12Clicked() {
         FormDataListDialog.ButtonActions.PrintableStringOutputStream var1 = new FormDataListDialog.ButtonActions.PrintableStringOutputStream();
         if (this.printDataToStream(var1)) {
            PrinterJob var2 = PrinterJob.getPrinterJob();

            try {
               var2.setPrintable(var1);
               if (var2.printDialog()) {
                  var2.print();
                  GuiUtil.showMessageDialog(this.file_panel, "Nyomtatás befejeződött.", "Nyomtatvány adatok", 1);
               }
            } catch (Exception var5) {
               var2.cancel();
               var5.printStackTrace();
               GuiUtil.showMessageDialog(this.file_panel, "Nyomtatás közben hiba keletkezett !", "Nyomtatvány adatok", 0);
            }
         } else {
            GuiUtil.showMessageDialog(this.file_panel, "Nyomtatás közben hiba keletkezett !", "Nyomtatvány adatok", 0);
         }

         try {
            var1.close();
         } catch (IOException var4) {
            var4.printStackTrace();
         }

      }

      public void b13Clicked() {
         Object var5 = PropertyList.getInstance().get("prop.usr.naplo");
         File var3 = var5 == null ? null : new NecroFile(var5.toString());
         boolean var4 = false;

         while(true) {
            File[] var2 = Operations.getFile(this.file_panel, "Mentés file-ba", 1, var3, "nyomtatvany_adatok.txt");
            File var1;
            if (var2 != null && var2.length > 0) {
               var1 = var2[0];
            } else {
               var1 = null;
            }

            if (var1 == null) {
               return;
            }

            if (!var1.getName().endsWith(".txt")) {
               var1 = new NecroFile(var1.getParent(), var1.getName() + ".txt");
            }

            if (var1.exists()) {
               if (0 != JOptionPane.showConfirmDialog(this.file_panel, var1.getName() + " állomány létezik ! Felülírja ?", "Mentés file-ba", 0)) {
                  continue;
               }

               var4 = true;
            }

            if (var1 != null) {
               try {
                  FileOutputStream var6 = new FileOutputStream(var1);
                  if (this.printDataToStream(var6)) {
                     GuiUtil.showMessageDialog(this.file_panel, "Mentés befejeződött.\n(Létrehozott állomány: " + var1.getName() + ")", "Nyomtatvány adatok", 1);
                  } else {
                     if (var1.exists() && var4) {
                        var1.delete();
                     }

                     GuiUtil.showMessageDialog(this.file_panel, "Mentés közben hiba keletkezett !\n(Állomány: " + var1.getName() + ")", "Nyomtatvány adatok", 0);
                  }
               } catch (FileNotFoundException var8) {
                  var8.printStackTrace();
               }
            }

            return;
         }
      }

      private boolean printDataToStream(OutputStream var1) {
         FormDataListDialog.ButtonActions.ColStatOutputStream var2 = new FormDataListDialog.ButtonActions.ColStatOutputStream();
         this.doColStat(var2);
         return this.printDataToStream_(var1, var2);
      }

      private void doColStat(FormDataListDialog.ButtonActions.ColStatOutputStream var1) {
         JTable var2 = (JTable)this.file_panel.getFPComponent("files");
         TableColumnModel var3 = var2.getColumnModel();
         int var5 = 0;

         TableColumn var4;
         int var6;
         for(var6 = var2.getColumnCount(); var5 < var6; ++var5) {
            var4 = var3.getColumn(var5);
            if (var4.getWidth() > 0) {
               var1.reset();

               try {
                  var1.write(var2.getColumnName(var5).getBytes("ISO-8859-2"));
                  var1.storeStat(var2.getColumnName(var5));
               } catch (IOException var12) {
                  Tools.eLog(var12, 0);
               }
            }
         }

         var5 = 0;

         for(var6 = var2.getRowCount(); var5 < var6; ++var5) {
            int var7 = 0;

            for(int var8 = var2.getColumnCount(); var7 < var8; ++var7) {
               var4 = var3.getColumn(var7);
               if (var4.getWidth() > 0) {
                  Object var9 = var2.getValueAt(var5, var7);
                  var9 = var9 == null ? "" : var9;
                  var1.reset();

                  try {
                     var1.write(var9.toString().getBytes("ISO-8859-2"));
                     var1.storeStat(var2.getColumnName(var7));
                  } catch (IOException var11) {
                     Tools.eLog(var11, 0);
                  }
               }
            }
         }

      }

      private boolean printDataToStream_(OutputStream var1, FormDataListDialog.ButtonActions.ColStatOutputStream var2) {
         JTable var3 = (JTable)this.file_panel.getFPComponent("files");
         TableColumnModel var4 = var3.getColumnModel();
         SimpleDateFormat var6 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
         BufferedWriter var7 = null;

         try {
            var7 = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(var1), "ISO-8859-2"));
            var7.write("AbevJava - Nyomtatványok");
            var7.newLine();
            var7.newLine();
            var7.write("|");
            boolean var8 = false;
            int var9 = 0;

            TableColumn var5;
            int var10;
            for(var10 = var3.getColumnCount(); var9 < var10; ++var9) {
               var5 = var4.getColumn(var9);
               if (var5.getWidth() > 0) {
                  if (var8) {
                     var7.write("|");
                  }

                  var7.write(this.formatCol(var3.getColumnName(var9), var3.getColumnName(var9), var2));
                  var8 = true;
               }
            }

            var7.write("|");
            var7.newLine();
            var9 = 0;

            int var11;
            int var12;
            for(var10 = var3.getColumnCount(); var9 < var10; ++var9) {
               var11 = 0;

               for(var12 = var2.getStat(var3.getColumnName(var9)); var11 < var12; ++var11) {
                  if (var11 == 0) {
                     var7.write("+");
                  }

                  var7.write("-");
               }
            }

            var7.write("+");
            var7.newLine();
            var9 = 0;

            for(var10 = var3.getRowCount(); var9 < var10; ++var9) {
               var7.write("|");
               var8 = false;
               var11 = 0;

               for(var12 = var3.getColumnCount(); var11 < var12; ++var11) {
                  var5 = var4.getColumn(var11);
                  if (var5.getWidth() > 0) {
                     if (var8) {
                        var7.write("|");
                     }

                     Object var13 = var3.getValueAt(var9, var11);
                     var7.write(this.formatCol(var3.getColumnName(var11), var13.toString(), var2));
                     var8 = true;
                  }
               }

               var7.write("|");
               var7.newLine();
            }

            var7.newLine();
            var7.write("Listagenerálás dátuma: " + var6.format(new Date()));
            var7.flush();
            var7.close();
            return true;
         } catch (Exception var15) {
            var15.printStackTrace();
            if (var7 != null) {
               try {
                  var7.close();
               } catch (IOException var14) {
                  Tools.eLog(var15, 0);
               }
            }

            return false;
         }
      }

      private String formatCol(String var1, String var2, FormDataListDialog.ButtonActions.ColStatOutputStream var3) {
         var2 = var2 == null ? "" : var2;
         if (var3 != null) {
            int var4 = var3.getStat(var1);
            int var5 = 0;

            for(int var6 = Math.abs(var4 - var2.length()); var5 < var6; ++var5) {
               var2 = var2 + " ";
            }
         }

         return var2;
      }

      public void b14Clicked() {
      }

      public void b15Clicked() {
      }

      public void b21Clicked() {
      }

      public void b22PathClicked() {
      }

      public void b31Clicked() {
      }

      public void b32Clicked() {
         this.file_panel.getBusiness().saveFilterSettings("FormDataListDialog");
         FormDataListDialog.this.dlg.setVisible(false);
         this.file_panel.fireEvent(new CloseEvent(this.file_panel));
      }

      class ColumnEntry implements Entry {
         private Object key;
         private Object value;

         ColumnEntry(Object var2, Object var3) {
            this.key = var2;
            this.value = var3;
         }

         public Object getKey() {
            return this.key;
         }

         public Object getValue() {
            return this.value;
         }

         public Object setValue(Object var1) {
            Object var2 = this.value;
            this.value = var1;
            return var2;
         }
      }

      class ColumnSelectionDialog extends JDialog {
         private DefaultListModel dlm;
         private JButton btn_ok;
         private JList lst_column_list;

         ColumnSelectionDialog(Vector var2) {
            this.build();
            this.prepare(var2);
         }

         private void build() {
            JLabel var1 = new JLabel("Oszlopok láthatósága");
            var1.setAlignmentX(0.0F);
            this.lst_column_list = new JList(this.dlm = new DefaultListModel());
            this.lst_column_list.setAlignmentX(0.0F);
            this.lst_column_list.setSelectionMode(0);
            JScrollPane var2 = new JScrollPane(this.lst_column_list);
            var2.setAlignmentX(0.0F);
            var2.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            this.btn_ok = new JButton("Ok");
            this.btn_ok.setAlignmentX(0.5F);
            JPanel var4 = new JPanel();
            var4.setAlignmentX(0.0F);
            var4.setLayout(new FlowLayout());
            var4.add(this.btn_ok, (Object)null);
            JPanel var3 = new JPanel();
            var3.setLayout(new BoxLayout(var3, 1));
            var3.add(var1, (Object)null);
            var3.add(var2, (Object)null);
            var3.add(var4, (Object)null);
            this.setContentPane(var3);
         }

         private void prepare(Vector var1) {
            this.btn_ok.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent var1) {
                  ColumnSelectionDialog.this.setVisible(false);
                  ColumnSelectionDialog.this.dispose();
               }
            });
            this.lst_column_list.setCellRenderer(new ListCellRenderer() {
               private final JCheckBox renderer = GuiUtil.getANYKCheckBox();

               public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
                  if (var2 instanceof Entry) {
                     Entry var6 = (Entry)var2;
                     this.renderer.setText(var6.getKey().toString());
                     this.renderer.setSelected((Boolean)var6.getValue());
                     this.renderer.setBackground(var4 ? var1.getSelectionBackground() : var1.getBackground());
                     return this.renderer;
                  } else {
                     return null;
                  }
               }
            });
            this.lst_column_list.addMouseListener(new MouseListener() {
               public void mouseClicked(MouseEvent var1) {
                  Entry var2 = (Entry)ColumnSelectionDialog.this.lst_column_list.getSelectedValue();
                  var2.setValue((Boolean)var2.getValue() ? Boolean.FALSE : Boolean.TRUE);
                  ColumnSelectionDialog.this.lst_column_list.revalidate();
                  ColumnSelectionDialog.this.lst_column_list.repaint();
               }

               public void mouseEntered(MouseEvent var1) {
               }

               public void mouseExited(MouseEvent var1) {
               }

               public void mousePressed(MouseEvent var1) {
               }

               public void mouseReleased(MouseEvent var1) {
               }
            });
            if (var1 != null && this.dlm != null) {
               Iterator var2 = var1.iterator();

               while(var2.hasNext()) {
                  Entry var3 = (Entry)var2.next();
                  this.dlm.addElement(var3);
               }
            }

         }
      }

      class PrintableStringOutputStream extends OutputStream implements Printable {
         private final StringWriter sw = new StringWriter(4096);
         private String print_id = "";

         public PrintableStringOutputStream() {
         }

         public void flush() throws IOException {
            this.sw.flush();
         }

         public void close() throws IOException {
            this.sw.close();
         }

         public void write(int var1) throws IOException {
            String var2 = new String(new byte[]{(byte)var1}, "ISO-8859-2");
            this.sw.write(var2);
         }

         public int print(Graphics var1, PageFormat var2, int var3) throws PrinterException {
            boolean var4 = false;
            if (var2 == null) {
               return 1;
            } else {
               int var5 = (int)var2.getImageableWidth();
               int var6 = (int)var2.getImageableHeight();
               ((Graphics2D)var1).translate(var2.getImageableX(), var2.getImageableY());
               if (var3 == 0) {
                  this.print_id = String.valueOf(System.currentTimeMillis());
               }

               BufferedReader var7 = null;

               try {
                  var1.setFont(var1.getFont().deriveFont(8.0F));
                  FontMetrics var15 = var1.getFontMetrics();
                  var1.drawString(var3 + 1 + ". lap [" + System.getProperty("user.name") + " - nyomtatási azonosító:" + this.print_id + "]", 0, var6 - var15.getMaxDescent());
                  var6 -= var15.getHeight() + 5;
                  var1.drawLine(0, var6, var5, var6);
                  Font var17 = new Font("Monospaced", 0, 10);
                  var1.setFont(var17);
                  var7 = new BufferedReader(new StringReader(this.sw.toString()));
                  var15 = var1.getFontMetrics();
                  int var12 = var15.getMaxAscent();
                  int var10 = var15.getHeight();
                  int var11 = 0;
                  int var14 = 0;
                  ((Graphics2D)var1).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                  ((Graphics2D)var1).setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

                  String var8;
                  while((var8 = var7.readLine()) != null) {
                     String var32 = "";
                     int var18 = 0;

                     byte var33;
                     int var13;
                     for(int var19 = var8.length(); var18 < var19; ++var18) {
                        var32 = var32 + var8.substring(var18, var18 + 1);
                        Rectangle2D var16 = var15.getStringBounds(var32, var1);
                        if (var16.getWidth() > (double)var5) {
                           var32 = var32.substring(0, var32.length() - 1);
                           --var18;
                           var13 = var12 + var10 * var11++;
                           if (var13 + var15.getMaxDescent() > var6) {
                              ++var14;
                              var33 = 0;
                              var11 = var33 + 1;
                              var13 = var12 + var10 * var33;
                           }

                           if (var14 == var3) {
                              var1.drawString(var32, 0, var13);
                              var4 = true;
                           }

                           var32 = "";
                        }
                     }

                     if (var32.length() > 0 || var8.length() == 0) {
                        var13 = var12 + var10 * var11++;
                        if (var13 + var15.getMaxDescent() > var6) {
                           ++var14;
                           var33 = 0;
                           var11 = var33 + 1;
                           var13 = var12 + var10 * var33;
                        }

                        if (var14 == var3) {
                           var1.drawString(var32, 0, var13);
                           var4 = true;
                        }
                     }

                     if (var14 > var3) {
                        byte var35;
                        if (var4) {
                           var35 = 0;
                           return var35;
                        }

                        var35 = 1;
                        return var35;
                     }
                  }
               } catch (IOException var30) {
                  byte var9 = 1;
                  return var9;
               } finally {
                  if (var7 != null) {
                     try {
                        var7.close();
                     } catch (IOException var29) {
                        var29.printStackTrace();
                     }
                  }

               }

               if (var4) {
                  return 0;
               } else {
                  return 1;
               }
            }
         }
      }

      class ColStatOutputStream extends OutputStream {
         private final Hashtable column_stat = new Hashtable();
         private int counter;

         public void write(int var1) throws IOException {
            ++this.counter;
         }

         public void reset() {
            this.counter = 0;
         }

         public void storeStat(String var1) {
            Object var2 = this.column_stat.get(var1);
            if (var2 != null) {
               Integer var3 = (Integer)var2;
               if (this.counter < var3) {
                  return;
               }
            }

            this.column_stat.put(var1, new Integer(this.counter));
         }

         public int getStat(String var1) {
            Object var2 = this.column_stat.get(var1);
            return var2 != null ? (Integer)var2 : 0;
         }

         public int getSumStat() {
            Enumeration var1 = this.column_stat.keys();

            int var2;
            for(var2 = 0; var1.hasMoreElements(); var2 += (Integer)this.column_stat.get(var1.nextElement())) {
            }

            return var2;
         }

         public int getColCount() {
            return this.column_stat.size();
         }
      }
   }
}
