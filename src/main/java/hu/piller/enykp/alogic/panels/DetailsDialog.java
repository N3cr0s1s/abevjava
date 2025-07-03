package hu.piller.enykp.alogic.panels;

import hu.piller.enykp.alogic.fileutil.DataChecker;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.util.base.Result;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.math.BigDecimal;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class DetailsDialog extends JDialog {
   private DataFieldModel df;
   private StoreItem si;
   private DefaultTableModel dtm;
   private JTextField tf;
   private TableModelListener tableModelListener;

   public DetailsDialog(DataFieldModel var1, StoreItem var2) {
      super(MainFrame.thisinstance, "Összeadandó tételek", true);
      this.df = var1;
      this.si = var2;
      JPanel var3 = new JPanel();
      var3.setLayout(new BorderLayout());
      Vector var4 = this.si.getDetails();
      Vector var5 = new Vector();
      Vector var7;
      Vector var21;
      if (var4 != null) {
         for(int var6 = 0; var6 < var4.size(); ++var6) {
            var7 = new Vector();
            var7.add(((Vector)var4.get(var6)).get(0));
            String var8 = (String)((Vector)var4.get(var6)).get(1);
            BigDecimal var9 = null;

            try {
               var9 = new BigDecimal(var8);
            } catch (Exception var20) {
               var9 = new BigDecimal(0);
            }

            var7.add(var9);
            var5.add(var7);
         }

         var21 = new Vector();
         var21.add("");
         var21.add((Object)null);
         var5.add(var21);
      } else {
         BigDecimal var22 = null;
         if (this.si.value != null) {
            try {
               var22 = new BigDecimal(this.si.value.toString());
            } catch (Exception var19) {
               var22 = null;
            }
         }

         var7 = new Vector();
         var7.add("");
         var7.add(var22);
         var5.add(var7);
         if (var22 != null) {
            var7 = new Vector();
            var7.add("");
            var7.add((Object)null);
            var5.add(var7);
         }
      }

      var21 = new Vector();
      var21.add("Megjegyzés");
      var21.add("Összeg");
      final JTable var23 = new JTable(var5, var21) {
         public Class<?> getColumnClass(int var1) {
            return var1 == 1 ? BigDecimal.class : String.class;
         }

         public Component prepareEditor(TableCellEditor var1, int var2, int var3) {
            Component var4 = super.prepareEditor(var1, var2, var3);
            if (var3 == 0) {
               return var4;
            } else {
               if (var4 instanceof JTextField) {
                  JTextField var5 = (JTextField)var4;
                  String var6 = var5.getText();
                  PlainDocument var7 = new PlainDocument() {
                     public void insertString(int var1, String var2, AttributeSet var3) throws BadLocationException {
                        StringBuffer var4 = new StringBuffer(this.getText(0, this.getLength()));
                        if ("-".equals(var2) && var1 != 0 && var4.charAt(0) != '-') {
                           var1 = 0;
                        }

                        if ("+".equals(var2)) {
                           if (var4.charAt(0) == '-') {
                              super.remove(0, 1);
                           }

                        } else {
                           var4.insert(var1, var2);
                           String var5 = var4.toString();

                           try {
                              new BigDecimal(var5);
                              super.insertString(var1, var2, var3);
                           } catch (NumberFormatException var7) {
                              if ("-".equals(var5)) {
                                 super.insertString(var1, var2, var3);
                              }
                           }

                        }
                     }
                  };
                  var5.setDocument(var7);
                  var5.setText(var6);
               }

               return var4;
            }
         }

         public Component prepareRenderer(TableCellRenderer var1, int var2, int var3) {
            Component var4 = super.prepareRenderer(var1, var2, var3);
            if (var3 == 0) {
               return var4;
            } else {
               DefaultTableCellRenderer var5 = (DefaultTableCellRenderer)var4;
               String var6 = null;

               try {
                  var6 = DetailsDialog.formatnumber(this.getValueAt(var2, var3).toString());
               } catch (Exception var8) {
                  var6 = "";
               }

               var5.setText(var6);
               return var5;
            }
         }
      };
      var23.getColumnModel().getColumn(0).setMinWidth(GuiUtil.getW("WWWWWWWWWWWW"));
      var23.getColumnModel().getColumn(0).setMaxWidth(2 * GuiUtil.getW("WWWWWWWWWWWW"));
      var23.getColumnModel().getColumn(0).setWidth((int)(1.5D * (double)GuiUtil.getW("WWWWWWWWWWWW")));
      var23.getColumnModel().getColumn(0).setPreferredWidth((int)(1.5D * (double)GuiUtil.getW("WWWWWWWWWWWW")));
      DefaultCellEditor var24 = new DefaultCellEditor(new JTextField()) {
         public Object getCellEditorValue() {
            String var1 = ((JTextField)this.getComponent()).getText();
            if ("-".equals(var1)) {
               var1 = "";
            }

            try {
               return new BigDecimal(var1);
            } catch (Exception var3) {
               return null;
            }
         }
      };
      var23.getTableHeader().getColumnModel().getColumn(1).setCellEditor(var24);
      if (GuiUtil.modGui()) {
         var23.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
      }

      var23.setSelectionMode(2);
      var3.add(new JScrollPane(var23));
      var3.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
      this.dtm = (DefaultTableModel)var23.getModel();
      this.tableModelListener = new TableModelListener() {
         public void tableChanged(TableModelEvent var1) {
            try {
               DetailsDialog.this.tf.setText(DetailsDialog.formatnumber(DetailsDialog.this.getsum()));
               if (DetailsDialog.this.dtm.getValueAt(DetailsDialog.this.dtm.getRowCount() - 1, 1) != null) {
                  Vector var2 = new Vector();
                  var2.add("");
                  var2.add((Object)null);
                  DetailsDialog.this.dtm.addRow(var2);
               }
            } catch (Exception var3) {
            }

         }
      };
      this.dtm.addTableModelListener(this.tableModelListener);
      var23.addMouseMotionListener(new MouseMotionListener() {
         public void mouseDragged(MouseEvent var1) {
         }

         public void mouseMoved(MouseEvent var1) {
            try {
               Point var2 = var1.getPoint();
               int var3 = var23.columnAtPoint(var2);
               if (var3 != 0) {
                  var23.setToolTipText((String)null);
                  return;
               }

               Object var4 = var23.getValueAt(var23.rowAtPoint(var2), var3);
               if (var4 != null) {
                  TableCellRenderer var5 = var23.getDefaultRenderer(String.class);
                  if (var5 instanceof JLabel) {
                     JLabel var6 = (JLabel)var5;
                     String var7 = var4.toString();
                     var6.setText(var7);
                     if (var6.getPreferredSize().getWidth() > (double)var23.getColumnModel().getColumn(var3).getWidth()) {
                        var23.setToolTipText(var7);
                     } else {
                        var23.setToolTipText((String)null);
                     }
                  } else {
                     var23.setToolTipText((String)null);
                  }
               }
            } catch (Exception var8) {
            }

         }
      });

      try {
         var23.getSelectionModel().setSelectionInterval(0, 0);
      } catch (Exception var18) {
      }

      JPanel var25 = new JPanel();
      var25.setLayout(new BorderLayout(0, 5));
      var25.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
      var3.add(var25, "South");
      JLabel var10 = new JLabel();
      var10.setBorder(BorderFactory.createLineBorder(new Color(151, 158, 164), 1));
      var25.add(var10);
      JPanel var11 = new JPanel();
      var11.setLayout(new BoxLayout(var11, 2));
      var11.add(Box.createHorizontalGlue());
      JLabel var12 = new JLabel("Összesen:");
      var12.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
      var11.add(var12);
      this.tf = new JTextField();
      this.tf.setMinimumSize(new Dimension(GuiUtil.getW(var12, "WWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 2));
      this.tf.setPreferredSize(this.tf.getMinimumSize());
      this.tf.setHorizontalAlignment(4);
      this.tf.setText(formatnumber(this.getsum()));
      this.tf.setEditable(false);
      this.tf.setBorder(new LineBorder(GuiUtil.getHighLightColor()));
      var11.add(this.tf);
      var25.add(var11, "North");
      JPanel var13 = new JPanel();
      var13.setLayout(new BoxLayout(var13, 2));
      var25.add(var13, "South");
      JButton var14 = new JButton("Új sor");
      var14.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Vector var2 = new Vector();
            var2.add("");
            var2.add("");
            DetailsDialog.this.dtm.addRow(var2);
         }
      });
      JButton var15 = new JButton("Töröl");
      var15.setPreferredSize(new Dimension(GuiUtil.getW(var15, var15.getText()), GuiUtil.getCommonItemHeight() + 4));
      var15.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (DetailsDialog.this.dtm.getRowCount() == 1) {
               DetailsDialog.this.dtm.setValueAt("", 0, 0);
               DetailsDialog.this.dtm.setValueAt((Object)null, 0, 1);
            } else {
               int[] var2 = var23.getSelectedRows();
               if (var2.length == 0) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem választott sort!", "Hibaüzenet", 0);
               } else {
                  for(int var3 = var2.length - 1; -1 < var3; --var3) {
                     try {
                        DetailsDialog.this.dtm.removeRow(var2[var3]);
                     } catch (Exception var5) {
                     }
                  }

                  if (DetailsDialog.this.dtm.getRowCount() == 0) {
                     Vector var6 = new Vector();
                     var6.add("");
                     var6.add((Object)null);
                     DetailsDialog.this.dtm.addRow(var6);
                  }

               }
            }
         }
      });
      var13.add(var15);
      var13.add(Box.createHorizontalGlue());
      JButton var16 = new JButton("Mégsem");
      var16.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            DetailsDialog.this.setVisible(false);
         }
      });
      JButton var17 = new JButton("OK");
      var17.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (var23.isEditing()) {
               var23.getCellEditor().stopCellEditing();
            }

            if (DetailsDialog.this.checkdtm()) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Ha a megjegyzést kitöltötte, akkor az összeg nem maradhat üresen!", "Hibaüzenet", 0);
            } else {
               String var2 = DetailsDialog.stripformatnumber(DetailsDialog.this.tf.getText());
               Result var3 = DataChecker.getInstance().checkField(DetailsDialog.this.df.formmodel.bm, DetailsDialog.this.df.formmodel.id, DetailsDialog.this.df.key, var2);
               if (!var3.isOk()) {
                  try {
                     Vector var4 = var3.errorList;
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "Kérem módosítsa a beírt összeget!\n" + var4.get(0), "Hibaüzenet", 0);
                  } catch (Exception var5) {
                  }

               } else {
                  DetailsDialog.this.dtm.removeTableModelListener(DetailsDialog.this.tableModelListener);
                  DetailsDialog.this.trimdtm();
                  if (DetailsDialog.this.dtm.getRowCount() == 0) {
                     DetailsDialog.this.si.setDetail((Vector)null);
                     DetailsDialog.this.si.setDetailSum((String)null);
                     var2 = "";
                  } else {
                     DetailsDialog.this.si.setDetail(DetailsDialog.this.convert(DetailsDialog.this.dtm.getDataVector()));
                     DetailsDialog.this.si.setDetailSum(var2);
                  }

                  DetailsDialog.this.si.value = var2;
                  DetailsDialog.this.setVisible(false);
               }
            }
         }
      });
      var17.setPreferredSize(new Dimension(GuiUtil.getW(var17, var17.getText()), GuiUtil.getCommonItemHeight() + 4));
      var16.setPreferredSize(new Dimension(GuiUtil.getW(var16, var16.getText()), GuiUtil.getCommonItemHeight() + 4));
      var13.add(var17);
      var13.add(Box.createHorizontalStrut(5));
      var13.add(var16);
      this.getContentPane().add(var3);
      this.setSize(3 * GuiUtil.getW("WWWWWWWWWWWW"), 10 * GuiUtil.getCommonItemHeight());
      this.setPreferredSize(this.getSize());
      this.setMinimumSize(this.getSize());
      this.setLocationRelativeTo(MainFrame.thisinstance);
      this.setVisible(true);
   }

   private void trimdtm() {
      for(int var1 = this.dtm.getRowCount() - 1; -1 < var1; --var1) {
         if (!(this.dtm.getValueAt(var1, 1) instanceof BigDecimal) && (this.dtm.getValueAt(var1, 0) == null || "".equals(this.dtm.getValueAt(var1, 0)))) {
            this.dtm.removeRow(var1);
         }
      }

   }

   private Vector convert(Vector var1) {
      Vector var2 = new Vector();

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         Vector var4 = (Vector)var1.get(var3);
         Vector var5 = new Vector();

         for(int var6 = 0; var6 < var4.size(); ++var6) {
            String var7 = null;

            try {
               var7 = var4.get(var6).toString();
            } catch (Exception var9) {
            }

            var5.add(var7);
         }

         var2.add(var5);
      }

      return var2;
   }

   private boolean checkdtm() {
      for(int var1 = 0; var1 < this.dtm.getRowCount(); ++var1) {
         if (!(this.dtm.getValueAt(var1, 1) instanceof BigDecimal)) {
            if (this.dtm.getValueAt(var1, 0) != null && !"".equals(this.dtm.getValueAt(var1, 0))) {
               return true;
            }

            return false;
         }
      }

      return false;
   }

   private String getsum() {
      if (this.dtm.getRowCount() == 0) {
         return "";
      } else {
         BigDecimal var1 = new BigDecimal(0);

         for(int var2 = 0; var2 < this.dtm.getRowCount(); ++var2) {
            try {
               var1 = var1.add((BigDecimal)this.dtm.getValueAt(var2, 1));
            } catch (Exception var4) {
            }
         }

         return var1.toString();
      }
   }

   public static String formatnumber(String var0) {
      if (var0.length() < 4) {
         return var0;
      } else {
         String var1 = "";
         int var2 = 1;

         for(int var3 = var0.length() - 1; -1 < var3; --var3) {
            var1 = var1 + var0.charAt(var3);
            if (var2 % 3 == 0) {
               var1 = var1 + " ";
            }

            ++var2;
         }

         return (new StringBuffer(var1.trim())).reverse().toString();
      }
   }

   public static String stripformatnumber(String var0) {
      BigDecimal var1 = new BigDecimal(var0.replaceAll(" ", ""));
      return var1.toString();
   }
}
