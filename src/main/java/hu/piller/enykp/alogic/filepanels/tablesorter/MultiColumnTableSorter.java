package hu.piller.enykp.alogic.filepanels.tablesorter;

import hu.piller.enykp.util.base.Tools;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class MultiColumnTableSorter {
   public static final int SORT_ORDER_NATURAL = 0;
   public static final int SORT_ORDER_ASCENT = 1;
   public static final int SORT_ORDER_DESCENT = 2;
   public static boolean lock = false;
   private int primary_order = 0;
   private int primary_column = -1;
   private int secondary_column = -1;
   private int secondary_order = 0;
   private JTable t;
   private JTableHeader th;
   private TableModel tm;
   private int[] row_map;
   private boolean is_sorting;
   private boolean is_sort_enabled;
   private final Icon down_arrow = new DownArrow(3);
   private final Icon up_arrow = new UpArrow(3);
   private final Icon down_arrow_2 = new DownArrow2(3);
   private final Icon up_arrow_2 = new UpArrow2(3);
   private final Icon no_arrow = new NoArrow(3);
   private final MultiColumnTableSorter.TableHeadMouseListener head_listener = new MultiColumnTableSorter.TableHeadMouseListener();
   private final MultiColumnTableSorter.TableSorterHeaderRenderer head_renderer = new MultiColumnTableSorter.TableSorterHeaderRenderer();
   private final MultiColumnTableSorter.TableSortModel ori_model = new MultiColumnTableSorter.TableSortModel();

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
      this.ori_model.initialize(this.tm);
   }

   public void detachTable() {
      if (this.th != null) {
         this.th.removeMouseListener(this.head_listener);
         if (this.head_renderer != null) {
            this.th.setDefaultRenderer(this.head_renderer.getDefaultRenderer());
         }
      }

   }

   public JTable getTable() {
      return this.t;
   }

   public void setSortEnabled(boolean var1) {
      this.is_sort_enabled = var1;
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

   public Icon getIcon2(int var1) {
      switch(var1) {
      case 0:
         return this.no_arrow;
      case 1:
         return this.down_arrow_2;
      case 2:
         return this.up_arrow_2;
      default:
         return null;
      }
   }

   private void sort_() {
      if (!lock) {
         if (!this.is_sorting && this.is_sort_enabled) {
            this.is_sorting = true;
            int[] var4 = null;
            if (this.ori_model.getRowCount() != this.tm.getRowCount() || this.ori_model.getColumnCount() != this.tm.getColumnCount()) {
               this.ori_model.initialize(this.tm);
            }

            int var1;
            int var2;
            int[] var5;
            int var6;
            try {
               int[] var3 = this.t.getSelectedRows();
               if (var3 != null) {
                  var6 = var3.length;
                  if (var6 > 0) {
                     var5 = this.row_map;
                     var4 = new int[var6];
                     var1 = 0;

                     for(var2 = var3.length; var1 < var2; ++var1) {
                        var4[var1] = var5[var3[var1]];
                     }
                  }
               }
            } catch (Exception var14) {
               Tools.eLog(var14, 0);
            }

            int var17;
            if (this.primary_order != 0) {
               var6 = this.tm.getColumnCount();
               ArrayList var7 = new ArrayList(var2 = this.tm.getRowCount());
               MultiColumnTableSorter.TableSortModel var8 = this.ori_model;

               for(var1 = 0; var1 < var2; ++var1) {
                  MultiColumnTableSortObject var9 = new MultiColumnTableSortObject(var8.getValueAt(var1, this.t.convertColumnIndexToModel(this.primary_column)), this.secondary_order == 0 ? "" : var8.getValueAt(var1, this.t.convertColumnIndexToModel(this.secondary_column)), var1, var8.getRow(var1));
                  var7.add(var9);
               }

               this.handleNumbers(var7);
               Comparator<MultiColumnTableSortObject> var20;
               if (this.primary_order == 1) {
                  var20 = Comparator.comparing((var0) -> var0.primarySortField);
               } else {
                  var20 = Comparator.comparing((var0) -> var0.primarySortField, Comparator.reverseOrder());
               }

               if (this.secondary_order != 0 && this.secondary_column != this.primary_column) {
                  if (this.secondary_order == 1) {
                     var20 = var20.thenComparing((var0) -> var0.secondarySortField);
                  } else {
                     var20 = var20.thenComparing((var0) -> var0.secondarySortField, Comparator.reverseOrder());
                  }
               }

               Stream var10 = var7.stream().sorted(var20);
               List var11 = (List)var10.collect(Collectors.toList());
               this.row_map = new int[var11.size()];
               var5 = this.row_map;

               for(var1 = 0; var1 < var2; ++var1) {
                  for(int var12 = 0; var12 < var6; ++var12) {
                     this.tm.setValueAt(((MultiColumnTableSortObject)var11.get(var1)).vectorFieldToSave.get(var12), var1, var12);
                  }

                  var5[var1] = ((MultiColumnTableSortObject)var11.get(var1)).intFieldToSave;
               }
            } else {
               MultiColumnTableSorter.TableSortModel var15 = this.ori_model;
               this.row_map = new int[var2 = var15.getRowCount()];
               var5 = this.row_map;

               for(var1 = 0; var1 < var2; var5[var1] = var1++) {
                  var17 = 0;

                  for(int var18 = var15.getColumnCount(); var17 < var18; ++var17) {
                     this.tm.setValueAt(var15.getValueAt(var1, var17), var1, var17);
                  }
               }
            }

            try {
               if (var4 != null) {
                  ListSelectionModel var16 = this.t.getSelectionModel();
                  var17 = var4.length;
                  int[] var19 = new int[var17];
                  var5 = this.row_map;

                  for(var1 = 0; var1 < var17; ++var1) {
                     int var22 = 0;

                     for(int var21 = var5.length; var22 < var21; ++var22) {
                        if (var4[var1] == var5[var22]) {
                           var19[var1] = var22;
                        }
                     }
                  }

                  var16.clearSelection();
                  var1 = 0;

                  for(var2 = var4.length; var1 < var2; ++var1) {
                     var16.addSelectionInterval(var19[var1], var19[var1]);
                  }
               }
            } catch (Exception var13) {
               Tools.eLog(var13, 0);
            }

            this.is_sorting = false;
         }
      }
   }

   private void handleNumbers(ArrayList<MultiColumnTableSortObject> var1) {
      if (var1.size() != 0) {
         MultiColumnTableSortObject var4 = (MultiColumnTableSortObject)var1.get(0);
         int var2 = var4.primarySortField.length();
         int var3 = var4.secondarySortField.length();

         for(int var5 = 0; var5 < var1.size() && (var2 > -1 || var3 > -1); ++var5) {
            var4 = (MultiColumnTableSortObject)var1.get(var5);

            int var6;
            for(var6 = 0; var6 < var4.primarySortField.length() && var2 > -1; ++var6) {
               if (!"0123456789".contains(var4.primarySortField.substring(var6, var6 + 1))) {
                  var2 = -1;
               }
            }

            if (var2 != -1) {
               var2 = Math.max(var2, var4.primarySortField.length());
            }

            for(var6 = 0; var6 < var4.secondarySortField.length() && var3 > -1; ++var6) {
               if (!"0123456789".contains(var4.secondarySortField.substring(var6, var6 + 1))) {
                  var3 = -1;
               }
            }

            if (var3 != -1) {
               var3 = Math.max(var3, var4.secondarySortField.length());
            }
         }

         MultiColumnTableSortObject var7;
         Iterator var8;
         if (var2 > 0) {
            var8 = var1.iterator();

            while(var8.hasNext()) {
               var7 = (MultiColumnTableSortObject)var8.next();
               var7.handleNumber1(var2);
            }
         }

         if (var3 > 0) {
            var8 = var1.iterator();

            while(var8.hasNext()) {
               var7 = (MultiColumnTableSortObject)var8.next();
               var7.handleNumber2(var3);
            }
         }

      }
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

      private void reset() {
         MultiColumnTableSorter.this.primary_column = 0;
         MultiColumnTableSorter.this.primary_order = 0;
         MultiColumnTableSorter.this.secondary_column = -1;
      }

      public Object getValueAt(int var1, int var2) {
         return this.getRow(var1).get(var2);
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
            int var2 = MultiColumnTableSorter.this.tm.getColumnCount();
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

   private class TableSorterHeaderRenderer implements TableCellRenderer {
      private TableCellRenderer ori_renderer;

      public TableSorterHeaderRenderer() {
      }

      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         Component var7 = this.ori_renderer.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         int var8 = MultiColumnTableSorter.this.t.convertColumnIndexToModel(MultiColumnTableSorter.this.primary_column);
         int var9 = MultiColumnTableSorter.this.t.convertColumnIndexToModel(MultiColumnTableSorter.this.secondary_column);
         if (var7 instanceof JLabel) {
            JLabel var10 = (JLabel)var7;
            int var11 = var1.getColumnModel().getColumn(var6).getModelIndex();
            if (var8 == var11) {
               var10.setIcon(MultiColumnTableSorter.this.getIcon(MultiColumnTableSorter.this.primary_order));
            } else if (var9 == var11) {
               var10.setIcon(MultiColumnTableSorter.this.getIcon2(MultiColumnTableSorter.this.secondary_order));
            } else {
               var10.setIcon(MultiColumnTableSorter.this.no_arrow);
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
         if (MultiColumnTableSorter.this.is_sort_enabled) {
            int var2 = MultiColumnTableSorter.this.th.getColumnModel().getColumnIndexAtX(var1.getX());
            if (var1.getButton() == 1) {
               if (var2 == MultiColumnTableSorter.this.primary_column) {
                  MultiColumnTableSorter.this.primary_order = (MultiColumnTableSorter.this.primary_order + 1) % 3;
               } else {
                  MultiColumnTableSorter.this.primary_order = 1;
               }

               MultiColumnTableSorter.this.primary_column = var2;
               if (var2 == MultiColumnTableSorter.this.secondary_column) {
                  MultiColumnTableSorter.this.secondary_column = -1;
                  MultiColumnTableSorter.this.secondary_order = 0;
               }
            } else {
               if (var2 == MultiColumnTableSorter.this.primary_column || MultiColumnTableSorter.this.primary_order == 0) {
                  return;
               }

               if (var2 == MultiColumnTableSorter.this.secondary_column) {
                  MultiColumnTableSorter.this.secondary_order = (MultiColumnTableSorter.this.secondary_order + 1) % 3;
               } else {
                  MultiColumnTableSorter.this.secondary_order = 1;
               }

               MultiColumnTableSorter.this.secondary_column = var2;
            }

            if (MultiColumnTableSorter.this.primary_order == 0) {
               MultiColumnTableSorter.this.primary_column = -1;
               MultiColumnTableSorter.this.secondary_order = 0;
               MultiColumnTableSorter.this.secondary_column = -1;
            }

            try {
               MultiColumnTableSorter.this.sort_();
            } catch (Exception var4) {
               MultiColumnTableSorter.this.is_sorting = false;
            }

            MultiColumnTableSorter.this.th.revalidate();
            MultiColumnTableSorter.this.th.repaint();
         }
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
