package hu.piller.enykp.alogic.filepanels.tablesorter;

import hu.piller.enykp.util.base.Tools;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class TableSorter {
   public static final int SORT_ORDER_NATURAL = 0;
   public static final int SORT_ORDER_ASCENT = 1;
   public static final int SORT_ORDER_DESCENT = 2;
   public static boolean lock = false;
   private JTable t;
   private JTableHeader th;
   private TableModel tm;
   private int[] row_map;
   private boolean is_sorting;
   private boolean is_sort_enabled;
   private final Icon down_arrow = new DownArrow(3);
   private final Icon up_arrow = new UpArrow(3);
   private final Icon no_arrow = new NoArrow(3);
   private int sort_order;
   private int sort_column;
   private final TableSorter.TableHeadMouseListener head_listener = new TableSorter.TableHeadMouseListener();
   private final TableSorter.TableSorterHeaderRenderer head_renderer = new TableSorter.TableSorterHeaderRenderer();
   private final TableSorter.TableSorterComparator comparator = new TableSorter.TableSorterComparator();
   private final TableModelListener model_listener = new TableSorter.TableSorterModelListener();
   private final TableSorter.TableSortModel ori_model = new TableSorter.TableSortModel();

   public void attachTable(JTable var1) {
      this.detachTable();
      if (var1 != null) {
         this.t = var1;
         this.th = this.t.getTableHeader();
         this.tm = this.t.getModel();
         this.prepare();
      }

   }

   private void prepare() {
      this.head_renderer.setDefaultRenderer(this.th.getDefaultRenderer());
      this.th.setDefaultRenderer(this.head_renderer);
      this.th.addMouseListener(this.head_listener);
      this.tm.addTableModelListener(this.model_listener);
      this.ori_model.initialize(this.tm);
   }

   public void detachTable() {
      if (this.th != null) {
         this.th.removeMouseListener(this.head_listener);
         if (this.head_renderer != null) {
            this.th.setDefaultRenderer(this.head_renderer.getDefaultRenderer());
         }
      }

      if (this.t != null) {
         this.t.getModel().removeTableModelListener(this.model_listener);
      }

   }

   public JTable getTable() {
      return this.t;
   }

   public void setSortOrder(int var1) {
      switch(var1) {
      case 0:
      case 1:
      case 2:
         if (var1 != this.sort_order) {
            this.sort_(this.sort_column, this.sort_order = var1);
         }
      default:
      }
   }

   public int getSortOrder() {
      return this.sort_order;
   }

   public int getSortedColumn() {
      return this.sort_column;
   }

   public void setSortEnabled(boolean var1) {
      this.is_sort_enabled = var1;
   }

   public boolean isSortEnabled() {
      return this.is_sort_enabled;
   }

   private int getNextSortOrder(int var1) {
      switch(var1) {
      case 0:
         this.sort_order = 1;
         break;
      case 1:
         this.sort_order = 2;
         break;
      case 2:
         this.sort_order = 0;
      }

      return this.sort_order;
   }

   public Icon getIcon(int var1) {
      switch(var1) {
      case 0:
         return this.no_arrow;
      case 1:
         return this.down_arrow;
      case 2:
         return this.up_arrow;
      default:
         return null;
      }
   }

   public void sort() {
      this.sort_(this.sort_column, this.sort_order);
   }

   public void sort(int var1, int var2) {
      this.sort_(var1, var2);
      this.sort_column = var1;
      this.sort_order = var2;
      this.th.revalidate();
      this.th.repaint();
   }

   private void sort_(int var1, int var2) {
      if (!lock) {
         if (!this.is_sorting && this.is_sort_enabled) {
            this.is_sorting = true;
            int[] var6 = null;
            if (this.ori_model.getRowCount() != this.tm.getRowCount() || this.ori_model.getColumnCount() != this.tm.getColumnCount()) {
               this.ori_model.initialize(this.tm);
            }

            int var3;
            int var4;
            int[] var7;
            int var8;
            try {
               int[] var5 = this.t.getSelectedRows();
               if (var5 != null) {
                  var8 = var5.length;
                  if (var8 > 0) {
                     var7 = this.row_map;
                     var6 = new int[var8];
                     var3 = 0;

                     for(var4 = var5.length; var3 < var4; ++var3) {
                        var6[var3] = var7[var5[var3]];
                     }
                  }
               }
            } catch (Exception var14) {
               Tools.eLog(var14, 0);
            }

            int var11;
            int var17;
            if (var2 != 0) {
               var8 = this.tm.getColumnCount();
               Object[][] var9 = new Object[var4 = this.tm.getRowCount()][3];
               TableSorter.TableSortModel var10 = this.ori_model;

               for(var3 = 0; var3 < var4; ++var3) {
                  var9[var3][0] = var10.getValueAt(var3, var1);
                  var9[var3][1] = new Integer(var3);
                  var9[var3][2] = var10.getRow(var3);
               }

               this.comparator.setOrder(var2);
               Arrays.sort(var9, this.comparator);
               this.row_map = new int[var9.length];
               var7 = this.row_map;

               for(var3 = 0; var3 < var4; ++var3) {
                  for(var11 = 0; var11 < var8; ++var11) {
                     this.tm.setValueAt(((Vector)((Vector)var9[var3][2])).get(var11), var3, var11);
                  }

                  var7[var3] = (Integer)((Integer)var9[var3][1]);
               }
            } else {
               TableSorter.TableSortModel var15 = this.ori_model;
               this.row_map = new int[var4 = var15.getRowCount()];
               var7 = this.row_map;

               for(var3 = 0; var3 < var4; var7[var3] = var3++) {
                  var17 = 0;

                  for(int var18 = var15.getColumnCount(); var17 < var18; ++var17) {
                     this.tm.setValueAt(var15.getValueAt(var3, var17), var3, var17);
                  }
               }
            }

            try {
               if (var6 != null) {
                  ListSelectionModel var16 = this.t.getSelectionModel();
                  var17 = var6.length;
                  int[] var19 = new int[var17];
                  var7 = this.row_map;

                  for(var3 = 0; var3 < var17; ++var3) {
                     var11 = 0;

                     for(int var12 = var7.length; var11 < var12; ++var11) {
                        if (var6[var3] == var7[var11]) {
                           var19[var3] = var11;
                        }
                     }
                  }

                  var16.clearSelection();
                  var3 = 0;

                  for(var4 = var6.length; var3 < var4; ++var3) {
                     var16.addSelectionInterval(var19[var3], var19[var3]);
                  }
               }
            } catch (Exception var13) {
               Tools.eLog(var13, 0);
            }

            this.is_sorting = false;
         }
      }
   }

   public void clearOriModel() {
      this.ori_model.clear();
   }

   private class TableSortModel {
      private final Vector table_data;

      private TableSortModel() {
         this.table_data = new Vector(128, 128);
      }

      public void initialize(TableModel var1) {
         this.clear();
         int var2 = var1.getRowCount();
         int var3 = var1.getColumnCount();

         for(int var4 = 0; var4 < var2; ++var4) {
            this.addNewRow();

            for(int var5 = 0; var5 < var3; ++var5) {
               this.setValueAt(var1.getValueAt(var4, var5), var4, var5);
            }
         }

         this.reset();
      }

      public void copy(TableSorter.TableSortModel var1) {
         this.clear();
         int var2 = var1.getRowCount();
         int var3 = var1.getColumnCount();

         for(int var4 = 0; var4 < var2; ++var4) {
            this.addNewRow();

            for(int var5 = 0; var5 < var3; ++var5) {
               this.setValueAt(var1.getValueAt(var4, var5), var4, var5);
            }
         }

         this.reset();
      }

      private void reset() {
         TableSorter.this.sort_column = 0;
         TableSorter.this.sort_order = 0;
         TableSorter.this.sort();
      }

      public Object getValueAt(int var1, int var2) {
         try {
            return this.getRow(var1).get(var2);
         } catch (Exception var4) {
            return null;
         }
      }

      public void setValueAt(Object var1, int var2, int var3) {
         Vector var4 = this.getRow(var2);
         if (var3 < var4.size()) {
            var4.remove(var3);
         }

         var4.insertElementAt(var1, var3);
      }

      public Vector getRow(int var1) {
         return var1 < this.table_data.size() ? (Vector)this.table_data.get(var1) : null;
      }

      public int getColumnCount() {
         Vector var1;
         return (var1 = this.getRow(0)) == null ? 0 : var1.size();
      }

      public int getRowCount() {
         return this.table_data.size();
      }

      public Vector addNewRow() {
         Vector var1 = this.getNewRow();
         this.table_data.add(var1);
         return var1;
      }

      public void removeRow(int var1) {
         this.table_data.remove(var1);
      }

      private Vector getNewRow() {
         Vector var1 = this.getRow(0);
         if (var1 == null) {
            int var2 = TableSorter.this.tm.getColumnCount();
            var1 = new Vector(var2, var2 > 16 ? var2 : 16);
         } else {
            var1 = (Vector)var1.clone();
         }

         var1.clear();
         return var1;
      }

      public void clear() {
         this.table_data.clear();
      }

      // $FF: synthetic method
      TableSortModel(Object var2) {
         this();
      }
   }

   private class TableSorterModelListener implements TableModelListener {
      private boolean is_processing_event;

      private TableSorterModelListener() {
      }

      public void tableChanged(TableModelEvent var1) {
         if (!this.is_processing_event && !TableSorter.this.is_sorting) {
            this.is_processing_event = true;
            TableModel var4 = (TableModel)var1.getSource();
            int var5;
            int var6;
            int var7;
            int var8;
            switch(var1.getType()) {
            case -1:
               var5 = var1.getFirstRow();
               var6 = var5;

               for(var7 = var1.getLastRow(); var5 <= var7; ++var5) {
                  TableSorter.this.ori_model.removeRow(TableSorter.this.row_map[var6]);
               }

               TableSorter.this.sort();
               break;
            case 0:
               int var3 = var1.getColumn();
               if (var3 >= 0) {
                  var5 = var1.getFirstRow();

                  for(var6 = var1.getLastRow(); var5 <= var6; ++var5) {
                     TableSorter.this.ori_model.setValueAt(var4.getValueAt(var5, var3), TableSorter.this.row_map[var5], var3);
                  }
               } else {
                  var5 = var1.getFirstRow();

                  for(var6 = var1.getLastRow(); var5 <= var6; ++var5) {
                     var7 = 0;

                     for(var8 = var4.getColumnCount(); var7 < var8; ++var7) {
                        TableSorter.this.ori_model.setValueAt(var4.getValueAt(var5, var7), TableSorter.this.row_map[var5], var7);
                     }
                  }
               }

               TableSorter.this.sort();
               break;
            case 1:
               var5 = var1.getFirstRow();

               for(var6 = var1.getLastRow(); var5 <= var6; ++var5) {
                  Vector var2 = TableSorter.this.ori_model.addNewRow();
                  var7 = 0;

                  for(var8 = var4.getColumnCount(); var7 < var8; ++var7) {
                     var2.add(var4.getValueAt(var5, var7));
                  }
               }

               TableSorter.this.sort();
            }

            this.is_processing_event = false;
         }
      }

      // $FF: synthetic method
      TableSorterModelListener(Object var2) {
         this();
      }
   }

   private class TableSorterComparator implements Comparator {
      private int order;

      private TableSorterComparator() {
      }

      public int compare(Object var1, Object var2) {
         Object var4 = ((Object[])((Object[])var1))[0];
         Object var5 = ((Object[])((Object[])var2))[0];
         if (this.order == 0) {
            return 0;
         } else {
            int var3;
            if (var4 != null && var5 != null) {
               if (var4 instanceof Comparable) {
                  var3 = ((Comparable)var4).compareTo(var5);
               } else {
                  var3 = var4.toString().compareTo(var5.toString());
               }
            } else if (var4 == var5) {
               var3 = 0;
            } else {
               var3 = var4 == null ? -1 : 1;
            }

            if (var3 == 0) {
               return var3;
            } else if (this.order == 1) {
               return var3;
            } else {
               return var3 < 0 ? 1 : -1;
            }
         }
      }

      public void setOrder(int var1) {
         this.order = var1;
      }

      // $FF: synthetic method
      TableSorterComparator(Object var2) {
         this();
      }
   }

   private class TableSorterHeaderRenderer implements TableCellRenderer {
      private TableCellRenderer ori_renderer;

      public TableSorterHeaderRenderer() {
      }

      public TableSorterHeaderRenderer(TableCellRenderer var2) {
         this.setDefaultRenderer(var2);
      }

      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         Component var7 = this.ori_renderer.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         if (var7 instanceof JLabel) {
            JLabel var8 = (JLabel)var7;
            int var9 = var1.getColumnModel().getColumn(var6).getModelIndex();
            if (TableSorter.this.sort_column == var9) {
               var8.setIcon(TableSorter.this.getIcon(TableSorter.this.getSortOrder()));
            } else {
               var8.setIcon(TableSorter.this.no_arrow);
            }
         }

         return var7;
      }

      public void setDefaultRenderer(TableCellRenderer var1) {
         this.ori_renderer = var1;
      }

      public TableCellRenderer getDefaultRenderer() {
         return this.ori_renderer;
      }
   }

   private class TableHeadMouseListener implements MouseListener {
      private TableHeadMouseListener() {
      }

      public void mouseClicked(MouseEvent var1) {
         TableSorter.this.sort_column = TableSorter.this.th.getColumnModel().getColumnIndexAtX(var1.getX());
         int var2 = TableSorter.this.t.convertColumnIndexToModel(TableSorter.this.sort_column);
         TableSorter.this.sort_column = var2;
         TableSorter.this.sort_(var2, TableSorter.this.getNextSortOrder(TableSorter.this.sort_order));
         TableSorter.this.th.revalidate();
         TableSorter.this.th.repaint();
      }

      public void mouseEntered(MouseEvent var1) {
      }

      public void mouseExited(MouseEvent var1) {
      }

      public void mousePressed(MouseEvent var1) {
      }

      public void mouseReleased(MouseEvent var1) {
      }

      // $FF: synthetic method
      TableHeadMouseListener(Object var2) {
         this();
      }
   }
}
