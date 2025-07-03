package hu.piller.enykp.alogic.upgrademanager_v2_0.gui.component;

import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.JTableFilter;
import hu.piller.enykp.alogic.upgrademanager_v2_0.gui.UpgradeTableEntry;
import hu.piller.enykp.alogic.upgrademanager_v2_0.gui.UpgradeTableModel;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.component.filtercombo.ENYKFilterComboStandard;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.tabledialog.TooltipTableHeader;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

public class JUpgradeTableFilter extends JTableFilter implements TableModelListener {
   protected DefaultTableModel getModelfromSource(JTable var1, Vector var2) {
      DefaultTableModel var3 = new DefaultTableModel();
      var3.addColumn("Oszlopok");
      var3.addColumn("Feltétel");
      this.ftypes = new int[var2.size() + 1];

      for(int var4 = 0; var4 < var2.size(); ++var4) {
         int var5 = (Integer)var2.get(var4);
         String var6 = var1.getColumnModel().getColumn(var5).getHeaderValue().toString();
         var3.addRow(new Object[]{var6, "(Nincs feltétel)"});
         this.getVector(this.sourcemodel, var5);
         this.indexht.put(new Integer(var4), new Integer(var5));
         this.ftypes[var4] = this.getftype(var4);
      }

      var3.addRow(new Object[]{"Keresés kulcsszavak alapján", "(Nincs feltétel)"});
      this.ftypes[var2.size()] = this.getftype(var2.size());
      return var3;
   }

   protected DefaultTableModel getfilteredmodel(TableModel var1) {
      UpgradeTableModel var2 = new UpgradeTableModel();

      for(int var3 = 0; var3 < var1.getRowCount(); ++var3) {
         if (this.checkrow(var1, var3)) {
            var2.getRows().add(((UpgradeTableModel)var1).getRows().get(var3));
         }
      }

      var2.col = ((UpgradeTableModel)var1).col;
      var2.mod = ((UpgradeTableModel)var1).mod;
      return var2;
   }

   protected boolean checkrow(TableModel var1, int var2) {
      for(int var3 = 0; var3 < 5; ++var3) {
         String var4 = null;
         String var5 = null;

         try {
            var5 = this.table.getValueAt(var3, 1).toString();
         } catch (Exception var12) {
            var5 = "";
         }

         if (!var5.equals("(Nincs feltétel)")) {
            if (var3 <= 3) {
               int var6 = (Integer)this.sourcecolumns.get(var3);

               try {
                  var4 = var1.getValueAt(var2, var6).toString();
               } catch (Exception var11) {
                  var4 = "";
               }
            } else {
               var4 = ((UpgradeTableEntry)((UpgradeTableModel)var1).getRows().get(var2)).getVersionData().getDescription();
            }

            this.hasFilter = true;
            var5 = var5.toUpperCase();
            var4 = var4.toUpperCase();
            if (this.ftypes[var3] == 1) {
               boolean var13 = false;
               String[] var7 = this.getKeywords(var5);
               int var8 = var7.length;

               for(int var9 = 0; var9 < var8; ++var9) {
                  String var10 = var7[var9];
                  if (var4.indexOf(var10) != -1) {
                     var13 = true;
                     break;
                  }
               }

               if (!var13) {
                  return false;
               }
            } else if (!var4.equals(var5)) {
               return false;
            }
         }
      }

      return true;
   }

   private String[] getKeywords(String var1) {
      ArrayList var2 = new ArrayList();
      String[] var3 = var1.split(" ");
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         if (!"".equals(var6.trim())) {
            var2.add(var6);
         }
      }

      return (String[])var2.toArray(new String[var2.size()]);
   }

   protected void filter() {
      if (this.sourcetable != null) {
         this.sourcetable.clearSelection();
         this.hasFilter = false;
         DefaultTableModel var1 = this.getfilteredmodel(this.sourcemodel);
         var1.addTableModelListener(this);
         this.sourcetable.setModel(var1);
         ((UpgradeTableModel)var1).reSort();
         if (this.label != null) {
            String var2 = "";
            if (this.hasFilter) {
               var2 = "(szűrés bekapcsolva!)";
            }

            this.label.setText("<html>" + this.origlabeltext + " <font color=\"FF0000\">" + var2 + "</font></html>");
         }

         GuiUtil.setTableColWidth(this.sourcetable);
      }

   }

   public void init(Object var1) {
      super.init(var1);
      this.sourcetable.getModel().addTableModelListener(this);
   }

   public void tableChanged(TableModelEvent var1) {
      if (var1.getType() == 1) {
         this.sourcemodel = ((UpgradeTableModel)this.sourcetable.getModel()).clone();
         this.filter();
      }

   }

   protected void prepare() {
      this.indexht = new Hashtable();
      this.sourcemodel = ((UpgradeTableModel)this.sourcetable.getModel()).clone();
      this.dtm = this.getModelfromSource(this.sourcetable, this.sourcecolumns);
      this.table = new JTable(this.dtm) {
         public String getToolTipText(MouseEvent var1) {
            return this.rowAtPoint(var1.getPoint()) == 4 && this.columnAtPoint(var1.getPoint()) == 0 ? "Leírásokban keres. Több kulcsszót szóközökkel elválasztva adhat meg, amelyek között 'vagy' feltétel van." : null;
         }

         public boolean isCellEditable(int var1, int var2) {
            return var2 != 0;
         }

         public TableCellEditor getCellEditor(int var1, int var2) {
            Vector var3;
            if (JUpgradeTableFilter.this.abevmode && JUpgradeTableFilter.this.lastindex == var1) {
               var3 = JUpgradeTableFilter.this.lastvector;
            } else {
               var3 = JUpgradeTableFilter.this.getVector(JUpgradeTableFilter.this.sourcetable.getModel(), JUpgradeTableFilter.this.getIndex(var1));
               JUpgradeTableFilter.this.lastvector = var3;
               JUpgradeTableFilter.this.lastindex = var1;
            }

            if (var3 == null) {
               var3 = new Vector();
            }

            Object[] var4 = var3.toArray(new Object[0]);
            Arrays.sort(var4);
            if (JUpgradeTableFilter.this.ftypes[var1] == 1) {
               JTextField var7 = new JTextField();
               var7.addFocusListener(new FocusAdapter() {
                  public void focusGained(FocusEvent var1) {
                     try {
                        String var2 = ((JTextField)var1.getSource()).getText();
                        if (var2 != null && var2.startsWith("(Nincs feltétel)")) {
                           ((JTextField)var1.getSource()).setText(var2.substring("(Nincs feltétel)".length()));
                        }
                     } catch (Exception var3) {
                        Tools.eLog(var3, 0);
                     }

                  }
               });
               return new DefaultCellEditor(var7);
            } else {
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
         }
      };
      this.table.getModel().addTableModelListener(new TableModelListener() {
         public void tableChanged(TableModelEvent var1) {
            ((UpgradeTableModel)JUpgradeTableFilter.this.sourcemodel).col = ((UpgradeTableModel)JUpgradeTableFilter.this.sourcetable.getModel()).col;
            ((UpgradeTableModel)JUpgradeTableFilter.this.sourcemodel).mod = ((UpgradeTableModel)JUpgradeTableFilter.this.sourcetable.getModel()).mod;
            JUpgradeTableFilter.this.filter();
            if (((TableModel)var1.getSource()).getValueAt(var1.getFirstRow(), var1.getColumn()).equals("(Nincs feltétel)")) {
               JUpgradeTableFilter.this.lastindex = -1;
            }

         }
      });
      this.table.setRowSelectionAllowed(false);
      this.table.setTableHeader(new TooltipTableHeader(this.table.getColumnModel()));
      GuiUtil.setTableColWidth(this.table);
      if (GuiUtil.modGui()) {
         this.table.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
      }

      this.table.getTableHeader().setReorderingAllowed(false);
      this.setPreferredSize(new Dimension(Integer.MAX_VALUE, this.table.getRowHeight() * this.maxvisiblerow + 4));
      this.sc.setViewportView(this.table);
   }
}
