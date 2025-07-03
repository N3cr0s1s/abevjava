package hu.piller.enykp.util.base;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class TableSorter extends AbstractTableModel {
   protected TableModel tableModel;
   public static final int DESCENDING = -1;
   public static final int NOT_SORTED = 0;
   public static final int ASCENDING = 1;
   private static TableSorter.Directive EMPTY_DIRECTIVE = new TableSorter.Directive(-1, 0);
   private static RuleBasedCollator collator;
   public static final Comparator COMPARABLE_COMAPRATOR;
   public static final Comparator LEXICAL_COMPARATOR;
   private TableSorter.Row[] viewToModel;
   private int[] modelToView;
   private JTableHeader tableHeader;
   private MouseListener mouseListener;
   private TableModelListener tableModelListener;
   private Map columnComparators;
   private java.util.List sortingColumns;

   public TableSorter() {
      this.columnComparators = new HashMap();
      this.sortingColumns = new ArrayList();
      this.mouseListener = new TableSorter.MouseHandler();
      this.tableModelListener = new TableSorter.TableModelHandler();
   }

   public TableSorter(TableModel var1) {
      this();
      this.setTableModel(var1);
   }

   public TableSorter(TableModel var1, JTableHeader var2) {
      this();
      this.setTableHeader(var2);
      this.setTableModel(var1);
   }

   private void clearSortingState() {
      this.viewToModel = null;
      this.modelToView = null;
   }

   public TableModel getTableModel() {
      return this.tableModel;
   }

   public void setTableModel(TableModel var1) {
      if (this.tableModel != null) {
         this.tableModel.removeTableModelListener(this.tableModelListener);
      }

      this.tableModel = var1;
      if (this.tableModel != null) {
         this.tableModel.addTableModelListener(this.tableModelListener);
      }

      this.clearSortingState();
      this.fireTableStructureChanged();
   }

   public JTableHeader getTableHeader() {
      return this.tableHeader;
   }

   public void setTableHeader(JTableHeader var1) {
      if (this.tableHeader != null) {
         TableCellRenderer var2 = this.tableHeader.getDefaultRenderer();
         if (var2 instanceof TableSorter.SortableHeaderRenderer) {
            this.tableHeader.setDefaultRenderer(((TableSorter.SortableHeaderRenderer)var2).tableCellRenderer);
         }
      }

      this.tableHeader = var1;
      if (this.tableHeader != null) {
         this.tableHeader.addMouseListener(this.mouseListener);
         this.tableHeader.setDefaultRenderer(new TableSorter.SortableHeaderRenderer(this.tableHeader.getDefaultRenderer()));
      }

   }

   public boolean isSorting() {
      return this.sortingColumns.size() != 0;
   }

   private TableSorter.Directive getDirective(int var1) {
      for(int var2 = 0; var2 < this.sortingColumns.size(); ++var2) {
         TableSorter.Directive var3 = (TableSorter.Directive)this.sortingColumns.get(var2);
         if (var3.column == var1) {
            return var3;
         }
      }

      return EMPTY_DIRECTIVE;
   }

   public int getSortingStatus(int var1) {
      return this.getDirective(var1).direction;
   }

   private void sortingStatusChanged() {
      this.clearSortingState();
      this.fireTableDataChanged();
      if (this.tableHeader != null) {
         this.tableHeader.repaint();
      }

   }

   public void setSortingStatus(int var1, int var2) {
      TableSorter.Directive var3 = this.getDirective(var1);
      if (var3 != EMPTY_DIRECTIVE) {
         this.sortingColumns.remove(var3);
      }

      if (var2 != 0) {
         this.sortingColumns.add(new TableSorter.Directive(var1, var2));
      }

      this.sortingStatusChanged();
   }

   protected Icon getHeaderRendererIcon(int var1, int var2) {
      TableSorter.Directive var3 = this.getDirective(var1);
      return var3 == EMPTY_DIRECTIVE ? null : new TableSorter.Arrow(var3.direction == -1, var2, this.sortingColumns.indexOf(var3));
   }

   private void cancelSorting() {
      this.sortingColumns.clear();
      this.sortingStatusChanged();
   }

   public void setColumnComparator(Class var1, Comparator var2) {
      if (var2 == null) {
         this.columnComparators.remove(var1);
      } else {
         this.columnComparators.put(var1, var2);
      }

   }

   protected Comparator getComparator(int var1) {
      Class var2 = this.tableModel.getColumnClass(var1);
      Comparator var3 = (Comparator)this.columnComparators.get(var2);
      if (var3 != null) {
         return var3;
      } else {
         return Comparable.class.isAssignableFrom(var2) ? COMPARABLE_COMAPRATOR : LEXICAL_COMPARATOR;
      }
   }

   private TableSorter.Row[] getViewToModel() {
      if (this.viewToModel == null) {
         int var1 = this.tableModel.getRowCount();
         this.viewToModel = new TableSorter.Row[var1];

         for(int var2 = 0; var2 < var1; ++var2) {
            this.viewToModel[var2] = new TableSorter.Row(var2);
         }

         if (this.isSorting()) {
            Arrays.sort(this.viewToModel);
         }
      }

      return this.viewToModel;
   }

   public int modelIndex(int var1) {
      return this.getViewToModel()[var1].modelIndex;
   }

   private int[] getModelToView() {
      if (this.modelToView == null) {
         int var1 = this.getViewToModel().length;
         this.modelToView = new int[var1];

         for(int var2 = 0; var2 < var1; this.modelToView[this.modelIndex(var2)] = var2++) {
         }
      }

      return this.modelToView;
   }

   public int getRowCount() {
      return this.tableModel == null ? 0 : this.tableModel.getRowCount();
   }

   public int getColumnCount() {
      return this.tableModel == null ? 0 : this.tableModel.getColumnCount();
   }

   public String getColumnName(int var1) {
      return this.tableModel.getColumnName(var1);
   }

   public Class getColumnClass(int var1) {
      return this.tableModel.getColumnClass(var1);
   }

   public boolean isCellEditable(int var1, int var2) {
      return this.tableModel.isCellEditable(this.modelIndex(var1), var2);
   }

   public Object getValueAt(int var1, int var2) {
      try {
         return this.tableModel.getValueAt(this.modelIndex(var1), var2);
      } catch (Exception var4) {
         return null;
      }
   }

   public void setValueAt(Object var1, int var2, int var3) {
      this.tableModel.setValueAt(var1, this.modelIndex(var2), var3);
   }

   static {
      try {
         String var0 = "< a< á< b< c< cs< d< dz< dzs< e< é< f< g< gy< h< i< í< j< k< l< ly< m< n< ny< o< ó< ö< ő< p< q< r< s< sz< t< ty< u< ú< ü< ű< v< w< x< y< z< zs";
         collator = new RuleBasedCollator(var0);
         collator.setStrength(1);
      } catch (Exception var1) {
         System.err.println("--> TableSorter: A táblázat elemeinek rendezettsége eltérhet a magyar helyesírás szabályaitól");
         var1.printStackTrace();
         collator = null;
      }

      COMPARABLE_COMAPRATOR = new Comparator() {
         public int compare(Object var1, Object var2) {
            return ((Comparable)var1).compareTo(var2);
         }
      };
      LEXICAL_COMPARATOR = new Comparator() {
         public int compare(Object var1, Object var2) {
            int var3;
            if (TableSorter.collator != null) {
               var3 = TableSorter.collator.compare(var1.toString(), var2.toString());
            } else {
               var3 = var1.toString().compareTo(var2.toString());
            }

            return var3;
         }
      };
   }

   private static class Directive {
      private int column;
      private int direction;

      public Directive(int var1, int var2) {
         this.column = var1;
         this.direction = var2;
      }
   }

   private class SortableHeaderRenderer implements TableCellRenderer {
      private TableCellRenderer tableCellRenderer;

      public SortableHeaderRenderer(TableCellRenderer var2) {
         this.tableCellRenderer = var2;
      }

      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         Component var7 = this.tableCellRenderer.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         if (var7 instanceof JLabel) {
            JLabel var8 = (JLabel)var7;
            var8.setHorizontalTextPosition(2);
            int var9 = var1.convertColumnIndexToModel(var6);
            var8.setIcon(TableSorter.this.getHeaderRendererIcon(var9, var8.getFont().getSize()));
         }

         return var7;
      }
   }

   private static class Arrow implements Icon {
      private boolean descending;
      private int size;
      private int priority;

      public Arrow(boolean var1, int var2, int var3) {
         this.descending = var1;
         this.size = var2;
         this.priority = var3;
      }

      public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
         Color var5 = var1 == null ? Color.GRAY : var1.getBackground();
         int var6 = (int)((double)(this.size / 2) * Math.pow(0.8D, (double)this.priority));
         int var7 = this.descending ? var6 : -var6;
         var4 = var4 + 5 * this.size / 6 + (this.descending ? -var7 : 0);
         int var8 = this.descending ? 1 : -1;
         var2.translate(var3, var4);
         var2.setColor(var5.darker());
         var2.drawLine(var6 / 2, var7, 0, 0);
         var2.drawLine(var6 / 2, var7 + var8, 0, var8);
         var2.setColor(var5.brighter());
         var2.drawLine(var6 / 2, var7, var6, 0);
         var2.drawLine(var6 / 2, var7 + var8, var6, var8);
         if (this.descending) {
            var2.setColor(var5.darker().darker());
         } else {
            var2.setColor(var5.brighter().brighter());
         }

         var2.drawLine(var6, 0, 0, 0);
         var2.setColor(var5);
         var2.translate(-var3, -var4);
      }

      public int getIconWidth() {
         return this.size;
      }

      public int getIconHeight() {
         return this.size;
      }
   }

   private class MouseHandler extends MouseAdapter {
      private MouseHandler() {
      }

      public void mouseClicked(MouseEvent var1) {
         JTableHeader var2 = (JTableHeader)var1.getSource();
         int[] var3 = this.getIndexesOfSelectedRowsOriginalModel(var2);
         TableColumnModel var4 = var2.getColumnModel();
         int var5 = var4.getColumnIndexAtX(var1.getX());
         int var6 = var4.getColumn(var5).getModelIndex();
         if (var6 != -1) {
            int var7 = TableSorter.this.getSortingStatus(var6);
            if (!var1.isControlDown()) {
               TableSorter.this.cancelSorting();
            }

            var7 += var1.isShiftDown() ? -1 : 1;
            var7 = (var7 + 4) % 3 - 1;
            TableSorter.this.setSortingStatus(var6, var7);
         }

         this.reselectRowsSelectedBeforeReorder(var2.getTable(), var3);
      }

      private void reselectRowsSelectedBeforeReorder(JTable var1, int[] var2) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var1.getSelectionModel().isSelectionEmpty()) {
               var1.getSelectionModel().setSelectionInterval(TableSorter.this.getModelToView()[var2[var3]], TableSorter.this.getModelToView()[var2[var3]]);
            } else {
               var1.getSelectionModel().addSelectionInterval(TableSorter.this.getModelToView()[var2[var3]], TableSorter.this.getModelToView()[var2[var3]]);
            }
         }

      }

      private int[] getIndexesOfSelectedRowsOriginalModel(JTableHeader var1) {
         int[] var2 = var1.getTable().getSelectedRows();
         int[] var3 = new int[var2.length];

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3[var4] = TableSorter.this.getViewToModel()[var2[var4]].modelIndex;
         }

         return var3;
      }

      // $FF: synthetic method
      MouseHandler(Object var2) {
         this();
      }
   }

   private class TableModelHandler implements TableModelListener {
      private TableModelHandler() {
      }

      public void tableChanged(TableModelEvent var1) {
         if (!TableSorter.this.isSorting()) {
            TableSorter.this.clearSortingState();
            TableSorter.this.fireTableChanged(var1);
         } else if (var1.getFirstRow() == -1) {
            TableSorter.this.cancelSorting();
            TableSorter.this.fireTableChanged(var1);
         } else {
            int var2 = var1.getColumn();
            if (var1.getFirstRow() == var1.getLastRow() && var2 != -1 && TableSorter.this.getSortingStatus(var2) == 0 && TableSorter.this.modelToView != null) {
               int var3 = TableSorter.this.getModelToView()[var1.getFirstRow()];
               TableSorter.this.fireTableChanged(new TableModelEvent(TableSorter.this, var3, var3, var2, var1.getType()));
            } else {
               TableSorter.this.clearSortingState();
               TableSorter.this.fireTableDataChanged();
            }
         }
      }

      // $FF: synthetic method
      TableModelHandler(Object var2) {
         this();
      }
   }

   private class Row implements Comparable {
      private int modelIndex;

      public Row(int var2) {
         this.modelIndex = var2;
      }

      public int compareTo(Object var1) {
         int var2 = this.modelIndex;
         int var3 = ((TableSorter.Row)var1).modelIndex;
         Iterator var4 = TableSorter.this.sortingColumns.iterator();

         TableSorter.Directive var5;
         int var10;
         do {
            if (!var4.hasNext()) {
               return 0;
            }

            var5 = (TableSorter.Directive)var4.next();
            int var6 = var5.column;
            Object var7 = TableSorter.this.tableModel.getValueAt(var2, var6);
            Object var8 = TableSorter.this.tableModel.getValueAt(var3, var6);
            boolean var9 = false;
            if (var7 == null && var8 == null) {
               var10 = 0;
            } else if (var7 == null) {
               var10 = -1;
            } else if (var8 == null) {
               var10 = 1;
            } else {
               var10 = TableSorter.this.getComparator(var6).compare(var7, var8);
            }
         } while(var10 == 0);

         return var5.direction == -1 ? -var10 : var10;
      }
   }
}
