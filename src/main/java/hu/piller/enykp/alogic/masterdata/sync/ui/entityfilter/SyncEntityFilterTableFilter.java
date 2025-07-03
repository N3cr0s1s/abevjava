package hu.piller.enykp.alogic.masterdata.sync.ui.entityfilter;

import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.JTableFilter;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.component.filtercombo.ENYKFilterComboStandard;
import hu.piller.enykp.util.base.TableSorter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

public class SyncEntityFilterTableFilter extends JTableFilter implements TableModelListener {
   protected DefaultTableModel getfilteredmodel(TableModel var1) {
      SyncEntityFilterTableModel var2 = new SyncEntityFilterTableModel();

      for(int var3 = 0; var3 < var1.getRowCount(); ++var3) {
         if (this.checkrow(var1, var3)) {
            Object[] var4 = ((SyncEntityFilterTableModel)var1).getRow(var3);
            var2.addRow(var4);
         }
      }

      return var2;
   }

   protected void filter() {
      if (this.sourcetable != null) {
         this.sourcetable.clearSelection();
         DefaultTableModel var1 = this.getfilteredmodel(this.sourcemodel);
         var1.addTableModelListener(this);
         this.sourcetable.setModel(new TableSorter(var1, this.sourcetable.getTableHeader()));
      }

   }

   public void init(Object var1) {
      super.init(var1);
      this.sourcetable.getModel().addTableModelListener(this);
   }

   public void tableChanged(TableModelEvent var1) {
      if (var1.getType() == 1) {
         this.sourcemodel = (SyncEntityFilterTableModel)((SyncEntityFilterTableModel)((TableSorter)this.sourcetable.getModel()).getTableModel()).clone();
         this.filter();
      }

   }

   protected void prepare() {
      this.indexht = new Hashtable();
      this.sourcemodel = (SyncEntityFilterTableModel)((SyncEntityFilterTableModel)((TableSorter)this.sourcetable.getModel()).getTableModel()).clone();
      this.dtm = this.getModelfromSource(this.sourcetable, this.sourcecolumns);
      this.table = new JTable(this.dtm) {
         public boolean isCellEditable(int var1, int var2) {
            return var2 != 0;
         }

         public TableCellEditor getCellEditor(int var1, int var2) {
            Vector var3;
            if (SyncEntityFilterTableFilter.this.abevmode && SyncEntityFilterTableFilter.this.lastindex == var1) {
               var3 = SyncEntityFilterTableFilter.this.lastvector;
            } else {
               var3 = SyncEntityFilterTableFilter.this.getVector(SyncEntityFilterTableFilter.this.sourcetable.getModel(), SyncEntityFilterTableFilter.this.getIndex(var1));
               SyncEntityFilterTableFilter.this.lastvector = var3;
               SyncEntityFilterTableFilter.this.lastindex = var1;
            }

            if (var3 == null) {
               var3 = new Vector();
            }

            Object[] var4 = var3.toArray(new Object[0]);
            Arrays.sort(var4);
            final ENYKFilterComboStandard var5 = new ENYKFilterComboStandard(var4);
            DefaultCellEditor var6 = new DefaultCellEditor(var5) {
               public Component getTableCellEditorComponent(JTable var1, Object var2, boolean var3, int var4, int var5x) {
                  Component var6 = super.getTableCellEditorComponent(var1, var2, var3, var4, var5x);
                  JPanel var7 = new JPanel();
                  var7.setLayout(new BorderLayout());
                  var7.add(var6, "Center");
                  BasicArrowButton var8 = new BasicArrowButton(5);
                  var8.addActionListener(new ActionListener() {
                     public void actionPerformed(ActionEvent var1) {
                        if (var5 instanceof MouseListener) {
                           ((MouseListener)var5).mouseClicked(new MouseEvent(var5, 0, 0L, 0, 0, 0, 2, false));
                        }

                     }
                  });
                  var7.add(var8, "East");
                  return var7;
               }
            };
            var6.setClickCountToStart(1);
            return var6;
         }
      };
      this.table.getModel().addTableModelListener(new TableModelListener() {
         public void tableChanged(TableModelEvent var1) {
            SyncEntityFilterTableFilter.this.filter();
            if (((TableModel)var1.getSource()).getValueAt(var1.getFirstRow(), var1.getColumn()).equals("(Nincs feltétel)")) {
               SyncEntityFilterTableFilter.this.lastindex = -1;
            }

         }
      });
      this.table.setRowSelectionAllowed(false);
      if (GuiUtil.modGui()) {
         this.table.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
      }

      this.table.getTableHeader().setReorderingAllowed(false);
      this.setPreferredSize(new Dimension(Integer.MAX_VALUE, this.table.getRowHeight() * this.maxvisiblerow + 4));
      this.sc.setViewportView(this.table);
   }
}
