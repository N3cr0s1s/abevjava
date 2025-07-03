package hu.piller.enykp.alogic.kihatas;

import hu.piller.enykp.gui.viewer.PageViewer;
import java.awt.Color;
import java.awt.Component;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowFilter.Entry;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class MultiLineTable extends JTable {
   MegallapitasComboLista megallapitaslista;
   int precision;

   public MultiLineTable(KihatasTableModel var1, MegallapitasComboLista var2, int var3) {
      super(var1);
      this.megallapitaslista = var2;
      this.precision = var3;
      this.setDefaultRenderer(Vector.class, new MultiLabelCellRenderer());
      TableRowSorter var4 = new TableRowSorter(var1);
      var4.setSortable(1, false);
      var4.setSortable(3, false);
      RowFilter var5 = new RowFilter() {
         public boolean include(Entry var1) {
            if (!(var1.getValue(1) instanceof Vector)) {
               return true;
            } else {
               MegallapitasVector var2 = (MegallapitasVector)var1.getValue(1);
               if (var2.size() == 0) {
                  return true;
               } else {
                  for(int var3 = 0; var3 < var2.size(); ++var3) {
                     MegallapitasRecord var4 = (MegallapitasRecord)var2.get(var3);
                     if (!var4.isDeleted()) {
                        return true;
                     }
                  }

                  return false;
               }
            }
         }
      };
      var4.setRowFilter(var5);
      this.setRowSorter(var4);
      this.getColumnModel().removeColumn(this.getColumnModel().getColumn(10));
      this.getColumnModel().removeColumn(this.getColumnModel().getColumn(9));
      this.getColumnModel().removeColumn(this.getColumnModel().getColumn(8));
      this.getColumnModel().removeColumn(this.getColumnModel().getColumn(7));
      this.getColumnModel().removeColumn(this.getColumnModel().getColumn(6));
      this.getColumnModel().removeColumn(this.getColumnModel().getColumn(5));
      this.getColumnModel().removeColumn(this.getColumnModel().getColumn(4));
      this.getColumnModel().removeColumn(this.getColumnModel().getColumn(2));
      this.getColumnModel().removeColumn(this.getColumnModel().getColumn(0));
      this.getColumnModel().getColumn(0).setMinWidth(50);
      this.getColumnModel().getColumn(0).setMaxWidth(700);
      this.getColumnModel().getColumn(0).setWidth(650);
      this.getColumnModel().getColumn(0).setPreferredWidth(650);
      DefaultCellEditor var6 = new DefaultCellEditor(new JTextField()) {
         public Object getCellEditorValue() {
            String var1 = ((JTextField)this.getComponent()).getText();
            if ("-".equals(var1)) {
               var1 = "";
            }

            return var1;
         }
      };
      var6.setClickCountToStart(1);
      this.getTableHeader().getColumnModel().getColumn(1).setCellEditor(var6);
      this.getTableHeader().setReorderingAllowed(false);
   }

   public Class<?> getColumnClass(int var1) {
      return var1 == 0 ? Vector.class : super.getColumnClass(var1);
   }

   public boolean isCellEditable(int var1, int var2) {
      if (var2 == 0) {
         return false;
      } else {
         var1 = this.getRowSorter().convertRowIndexToModel(var1);
         return ((KihatasRecord)((KihatasTableModel)this.getModel()).get(var1)).getMegallapitasVector().vannemtorolt();
      }
   }

   public Component prepareRenderer(TableCellRenderer var1, int var2, int var3) {
      Component var4 = super.prepareRenderer(var1, var2, var3);
      if (var3 != 0) {
         DefaultTableCellRenderer var14 = (DefaultTableCellRenderer)var4;
         if (var3 == 1) {
            var14.setHorizontalAlignment(4);

            try {
               String var15 = var14.getText();
               var15 = formatnumber(var15, this.precision);
               var14.setText(var15);
            } catch (NumberFormatException var13) {
            }
         } else {
            var14.setHorizontalAlignment(2);
         }

         return var14;
      } else {
         boolean var5 = false;
         Object var6 = this.getValueAt(var2, 0);
         if (!(var6 instanceof MegallapitasVector)) {
            return var4;
         } else {
            MegallapitasVector var7 = (MegallapitasVector)var6;
            boolean[] var8 = new boolean[var7.size()];

            int var9;
            for(var9 = 0; var9 < var7.size(); ++var9) {
               MegallapitasRecord var10 = (MegallapitasRecord)var7.get(var9);
               if (!this.checkdata(var10)) {
                  var8[var9] = true;
               }
            }

            for(var9 = 0; var9 < var7.size(); ++var9) {
               if (var8[var9]) {
                  var5 = true;
               }
            }

            if (var5 && var4 instanceof JPanel) {
               JPanel var16 = (JPanel)var4;
               Component[] var17 = var16.getComponents();

               for(int var11 = 0; var11 < var17.length; ++var11) {
                  Component var12 = var17[var11];
                  if (var8[var11]) {
                     var12.setForeground(Color.RED);
                  }
               }
            }

            return var4;
         }
      }
   }

   public Component prepareEditor(TableCellEditor var1, int var2, int var3) {
      Component var4 = super.prepareEditor(var1, var2, var3);
      if (var4 instanceof JTextField) {
         JTextField var5 = (JTextField)var4;
         String var6 = var5.getText();
         PlainDocument var7 = new PlainDocument() {
            public void insertString(int var1, String var2, AttributeSet var3) throws BadLocationException {
               StringBuffer var4 = new StringBuffer(this.getText(0, this.getLength()));
               var4.insert(var1, var2);
               String var5 = var4.toString();

               try {
                  if (MultiLineTable.stringFitsToDocument(var5, MultiLineTable.this.precision)) {
                     super.insertString(var1, var2, var3);
                  }
               } catch (NumberFormatException var7) {
                  if ("-".equals(var5)) {
                     super.insertString(var1, var2, var3);
                  }
               }

            }
         };
         var5.setDocument(var7);
         var5.setText(var6);
      }

      return var4;
   }

   private boolean checkdata(MegallapitasRecord var1) {
      String var2 = var1.getAdonemkod();
      String var3 = var1.getMsvo_azon();
      return this.megallapitaslista.checkdata(var2, var3);
   }

   public static String formatnumber(String var0, int var1) {
      String var2;
      try {
         double var3 = Double.parseDouble(var0);
         NumberFormat var5 = NumberFormat.getNumberInstance();
         var5.setMaximumFractionDigits(var1);
         var2 = var5.format(var3);
      } catch (NumberFormatException var6) {
         var2 = var0;
      } catch (NullPointerException var7) {
         var2 = var0;
      }

      return var2;
   }

   public static String stripformatnumber(String var0) {
      NumberFormat var1 = NumberFormat.getNumberInstance();

      try {
         Number var2 = var1.parse(var0);
         return PageViewer.cutZeroDecimal(var2.doubleValue() + "");
      } catch (ParseException var3) {
         return "";
      }
   }

   public static boolean stringFitsToDocument(String var0, int var1) {
      var0 = var0.replaceAll(",", ".");

      try {
         new Double(var0.replaceAll(",", "."));
      } catch (NumberFormatException var3) {
         return false;
      } catch (NullPointerException var4) {
         return false;
      }

      int var2 = var0.indexOf(".");
      return var2 == -1 || var0.length() - var2 - 1 <= var1;
   }
}
