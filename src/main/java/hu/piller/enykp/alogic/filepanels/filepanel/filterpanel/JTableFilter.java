package hu.piller.enykp.alogic.filepanels.filepanel.filterpanel;

import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.ArchiveFileTable;
import hu.piller.enykp.alogic.filepanels.filepanel.FileTable;
import hu.piller.enykp.alogic.filepanels.tablesorter.TableSorter;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.component.filtercombo.ENYKFilterComboStandard;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class JTableFilter extends JPanel {
   protected JTable sourcetable;
   protected Vector sourcecolumns;
   protected Vector filtertypes;
   protected int[] ftypes;
   protected TableModel sourcemodel;
   protected int maxvisiblerow;
   protected JScrollPane sc;
   protected JTable table;
   protected DefaultTableModel dtm;
   protected boolean abevmode = true;
   protected Vector lastvector;
   protected int lastindex = -1;
   protected Hashtable indexht;
   protected JLabel label;
   protected String origlabeltext;
   protected boolean hasFilter;
   private JPanel teszt;
   public static boolean flag = false;

   public JTableFilter() {
      this.build();
   }

   private void build() {
      this.setLayout(new BorderLayout());
      this.sc = new JScrollPane();
      this.add(this.sc, "Center");
   }

   private JPanel getTeszt() {
      if (this.teszt == null) {
      }

      return this.teszt;
   }

   protected DefaultTableModel getModelfromSource(JTable var1, Vector var2) {
      DefaultTableModel var3 = new DefaultTableModel();
      var3.addColumn("Oszlopok");
      var3.addColumn("Feltétel");
      this.ftypes = new int[var2.size()];

      for(int var4 = 0; var4 < var2.size(); ++var4) {
         int var5 = (Integer)var2.get(var4);
         String var6 = var1.getColumnModel().getColumn(var5).getHeaderValue().toString();
         var3.addRow(new Object[]{var6, "(Nincs feltétel)"});
         this.getVector(this.sourcemodel, var5);
         this.indexht.put(new Integer(var4), new Integer(var5));
         this.ftypes[var4] = this.getftype(var4);
      }

      return var3;
   }

   protected int getftype(int var1) {
      boolean var2 = false;

      int var5;
      try {
         var5 = (Integer)this.filtertypes.get(var1);
      } catch (Exception var4) {
         var5 = 0;
      }

      return var5;
   }

   protected Vector getVector(TableModel var1, int var2) {
      Vector var3 = new Vector();
      var3.add("(Nincs feltétel)");
      Hashtable var4 = new Hashtable();

      for(int var5 = 0; var5 < var1.getRowCount(); ++var5) {
         Object var6 = var1.getValueAt(var5, var2);
         if (var6 != null) {
            var4.put(var6.toString(), "");
         }
      }

      Enumeration var7 = var4.keys();

      while(var7.hasMoreElements()) {
         String var8 = Tools.getString(var7.nextElement(), (String)null);
         var3.add(var8);
      }

      return var3;
   }

   protected boolean checkrow(TableModel var1, int var2) {
      for(int var3 = 0; var3 < this.sourcecolumns.size(); ++var3) {
         int var4 = (Integer)this.sourcecolumns.get(var3);
         String var5 = null;

         try {
            var5 = var1.getValueAt(var2, var4).toString();
         } catch (Exception var9) {
            var5 = "";
         }

         String var6 = null;

         try {
            var6 = this.table.getValueAt(var3, 1).toString();
         } catch (Exception var8) {
            var6 = "(Nincs feltétel)";
            this.table.setValueAt(var6, var3, 1);
         }

         if (!var6.equals("(Nincs feltétel)")) {
            this.hasFilter = true;
            var6 = var6.toUpperCase();
            var5 = var5.toUpperCase();
            if (this.ftypes[var3] == 1) {
               if (var5.indexOf(var6) == -1) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }
         }
      }

      return true;
   }

   protected int getIndex(int var1) {
      try {
         return (Integer)this.indexht.get(new Integer(var1));
      } catch (Exception var3) {
         return 0;
      }
   }

   protected DefaultTableModel getfilteredmodel(TableModel var1) {
      DefaultTableModel var2 = new DefaultTableModel();

      int var3;
      for(var3 = 0; var3 < var1.getColumnCount(); ++var3) {
         var2.addColumn(var1.getColumnName(var3));
      }

      for(var3 = 0; var3 < var1.getRowCount(); ++var3) {
         if (this.checkrow(var1, var3)) {
            var2.addRow(this.getRowData(var1, var3));
         }
      }

      return var2;
   }

   protected Vector getRowData(TableModel var1, int var2) {
      Vector var3 = new Vector();

      for(int var4 = 0; var4 < var1.getColumnCount(); ++var4) {
         var3.add(var1.getValueAt(var2, var4));
      }

      return var3;
   }

   public void init(Object var1) {
      if (var1 instanceof Object[]) {
         Object[] var2 = (Object[])((Object[])var1);
         this.sourcetable = (JTable)var2[0];
         this.sourcecolumns = (Vector)var2[1];
         this.maxvisiblerow = (Integer)var2[2];
         ++this.maxvisiblerow;

         try {
            this.filtertypes = (Vector)var2[3];
         } catch (Exception var4) {
            this.filtertypes = new Vector();
         }

         this.sourcetable.setAutoResizeMode(0);
         if (GuiUtil.modGui()) {
            this.sourcetable.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
         }

         this.prepare();
      }

   }

   public void setSourcemodel(Object var1) {
      if (var1 instanceof TableModel) {
         this.sourcemodel = (TableModel)var1;
         this.filter();
      }

   }

   public void clearFilters() {
      if (this.table != null) {
         if (this.table.getCellEditor() != null) {
            this.table.getCellEditor().stopCellEditing();
         }

         for(int var1 = 0; var1 < this.table.getRowCount(); ++var1) {
            this.table.setValueAt("(Nincs feltétel)", var1, 1);
         }
      }

      this.lastindex = -1;
   }

   protected void prepare() {
      this.indexht = new Hashtable();
      this.sourcemodel = this.sourcetable.getModel();
      this.dtm = this.getModelfromSource(this.sourcetable, this.sourcecolumns);
      this.table = new JTable(this.dtm) {
         public boolean isCellEditable(int var1, int var2) {
            return var2 != 0;
         }

         public TableCellEditor getCellEditor(int var1, int var2) {
            Vector var3;
            if (JTableFilter.this.abevmode && JTableFilter.this.lastindex == var1) {
               var3 = JTableFilter.this.lastvector;
            } else {
               var3 = JTableFilter.this.getVector(JTableFilter.this.sourcetable.getModel(), JTableFilter.this.getIndex(var1));
               JTableFilter.this.lastvector = var3;
               JTableFilter.this.lastindex = var1;
            }

            if (var3 == null) {
               var3 = new Vector();
            }

            Object[] var4 = var3.toArray(new Object[0]);
            Arrays.sort(var4);
            if (JTableFilter.this.ftypes[var1] == 1) {
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
               final ENYKFilterComboStandard var5 = new ENYKFilterComboStandard(var4, JTableFilter.this.table.getColumnModel().getColumn(JTableFilter.this.table.getColumnModel().getColumnCount() - 1).getWidth() - 10);
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
                     var7.setFocusable(true);
                     var7.addFocusListener(new FocusAdapter() {
                        public void focusGained(FocusEvent var1) {
                           Thread var2 = new Thread(new Runnable() {
                              public void run() {
                                 var5.requestFocus();
                              }
                           });
                           var2.start();
                        }
                     });
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
            JTableFilter.this.filter();
            if (((TableModel)var1.getSource()).getValueAt(var1.getFirstRow(), var1.getColumn()).equals("(Nincs feltétel)")) {
               JTableFilter.this.lastindex = -1;
            }

         }
      });
      this.table.setRowSelectionAllowed(false);
      this.table.getTableHeader().setReorderingAllowed(false);
      this.table.setSurrendersFocusOnKeystroke(true);
      if (GuiUtil.modGui()) {
         this.table.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
      }

      int var1 = this.calculateHeight();
      this.setPreferredSize(new Dimension(Integer.MAX_VALUE, var1));
      int var2 = (int)Math.max(this.sc.getVerticalScrollBar().getPreferredSize().getWidth(), (double)GuiUtil.getW("??"));
      this.sc.getVerticalScrollBar().setPreferredSize(new Dimension(var2, 0));
      this.sc.getHorizontalScrollBar().setPreferredSize(new Dimension(0, var2));
      this.sc.setViewportView(this.table);
   }

   protected void filter() {
      if (!flag) {
         if (this.sourcetable != null) {
            this.sourcetable.clearSelection();
            this.hasFilter = false;
            int[] var1 = this.getCurrenColWidth(this.sourcetable.getColumnModel());
            DefaultTableModel var2 = this.getfilteredmodel(this.sourcemodel);
            this.sourcetable.setModel(var2);
            this.resetColWidth(var1);
            if (this.label != null) {
               String var3 = "";
               if (this.hasFilter) {
                  var3 = "(szűrés bekapcsolva!)";
               }

               this.label.setText("<html>" + this.origlabeltext + " <font color=\"FF0000\">" + var3 + "</font></html>");
            }
         }

      }
   }

   public boolean setInitialValue(int var1, String var2) {
      flag = true;
      this.clearFilters();
      flag = false;
      int var3 = (Integer)this.sourcecolumns.get(var1);

      for(int var4 = 0; var4 < this.sourcemodel.getRowCount(); ++var4) {
         if (this.sourcemodel.getValueAt(var4, var3).equals(var2)) {
            this.table.setValueAt(var2, var1, 1);
            return true;
         }
      }

      return false;
   }

   public Vector getFilterValues() {
      Vector var1 = new Vector();

      for(int var2 = 0; var2 < this.table.getRowCount(); ++var2) {
         var1.add(this.table.getValueAt(var2, 1));
      }

      return var1;
   }

   public void setFilterValues(Vector var1, Vector var2, final String var3, final String var4, final String var5) {
      flag = true;
      this.clearFilters();

      for(int var6 = 0; var6 < this.table.getRowCount(); ++var6) {
         try {
            this.table.setValueAt(var1.get(var6), var6, 1);
         } catch (Exception var13) {
         }
      }

      flag = false;
      this.filter();
      if (!"InstalledForms".equals(var5) && !"abevnewpanel".equals(var5) && !"emptyprint".equals(var5) && !"FormArchiver".equals(var5)) {
         Vector var15 = new Vector();

         try {
            for(int var7 = 0; var7 < var2.size(); ++var7) {
               String var8 = (String)var2.get(var7);
               if (var8 == null) {
                  break;
               }

               String[] var9 = var8.split("\\|");
               Object[] var10 = new Object[]{var9[0], new Integer(var9[1]), new Integer(var9[2])};
               var15.add(var10);
            }
         } catch (NumberFormatException var14) {
            var15.removeAllElements();
         }

         final TableColumnModel var16 = this.sourcetable.getColumnModel();
         final TableColumn var17;
         if (this.sourcetable instanceof ArchiveFileTable) {
            var17 = var16.getColumn(1);
         } else {
            var17 = null;
         }

         if (var16.getColumnCount() > var15.size()) {
            this.resetColumns(var3, var4, var16, var5, var17);
         } else {
            this.setcolwidth(var15, var17);
         }

         this.sourcetable.getTableHeader().setReorderingAllowed(true);

         try {
            JScrollPane var18 = (JScrollPane)this.sourcetable.getParent().getParent();
            var18.setVerticalScrollBarPolicy(22);
            int var19 = (int)Math.max(var18.getVerticalScrollBar().getPreferredSize().getWidth(), (double)GuiUtil.getW("??"));
            var18.getVerticalScrollBar().setPreferredSize(new Dimension(var19, 0));
            var18.getHorizontalScrollBar().setPreferredSize(new Dimension(0, var19));
            JButton var11 = new JButton("?");
            var11.setMargin(new Insets(0, 0, 0, 0));
            var11.setToolTipText("Visszaállítja a táblázat oszlopainak eredeti elrendezését és méretét.");
            var11.setBackground(Color.PINK);
            var11.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent var1) {
                  Object[] var2 = new Object[]{"Igen", "Nem"};
                  int var3x = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Visszaállítja a táblázat oszlopainak eredeti elrendezését és méretét?", "Visszaállítás", 0, 3, (Icon)null, var2, var2[0]);
                  if (var3x != 1 && var3x != -1) {
                     JTableFilter.this.resetColumns(var3, var4, var16, var5, var17);
                  }
               }
            });
            var18.setCorner("UPPER_RIGHT_CORNER", var11);
         } catch (Exception var12) {
            var12.printStackTrace();
         }

      }
   }

   private void setcolwidth(Vector var1, TableColumn var2) {
      if (var1.size() != 0) {
         TableColumnModel var3 = this.sourcetable.getColumnModel();
         DefaultTableModel var4 = (DefaultTableModel)this.sourcetable.getModel();
         DefaultTableColumnModel var5 = new DefaultTableColumnModel();

         for(int var6 = 0; var6 < var1.size(); ++var6) {
            Object[] var7 = (Object[])((Object[])var1.get(var6));
            String var8 = (String)var7[0];
            int var9 = (Integer)var7[2];
            int var10 = (Integer)var7[1];
            TableColumn var11;
            if (var2 != null && var2.getModelIndex() == var10) {
               var11 = new TableColumn(var2.getModelIndex(), var2.getWidth(), var2.getCellRenderer(), var2.getCellEditor());
               var11.setMaxWidth(var2.getMaxWidth());
               var5.addColumn(var11);
            } else {
               var11 = new TableColumn(var10, var9);
               var11.setMinWidth(10);
               var11.setPreferredWidth(var9);
               var11.setMaxWidth(1000);
               if (var9 == 0) {
                  var11.setMinWidth(0);
                  var11.setMaxWidth(0);
                  var11.setPreferredWidth(0);
               }

               var11.setHeaderValue(var8);
               var5.addColumn(var11);
            }
         }

         this.sourcetable.setColumnModel(var5);
      }

   }

   public Vector getColsWidth() {
      Vector var1 = new Vector();
      TableColumnModel var2 = this.sourcetable.getColumnModel();

      for(int var3 = 0; var3 < var2.getColumnCount(); ++var3) {
         TableColumn var4 = var2.getColumn(var3);
         int var5 = var4.getWidth();
         String var6 = null;

         try {
            var6 = var4.getHeaderValue().toString();
         } catch (Exception var8) {
            var6 = "";
         }

         int var7 = var4.getModelIndex();
         var1.add(new Object[]{var6, new Integer(var7), new Integer(var5)});
      }

      return var1;
   }

   public String[] getSortInfo() {
      String[] var1 = new String[]{"", ""};
      TableSorter var2 = null;

      try {
         var2 = ((FileTable)this.sourcetable).getTable_sorter();
      } catch (Exception var4) {
         var2 = ((hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.archivefilepanel.filepanel.FileTable)this.sourcetable).getTable_sorter();
      }

      var1[0] = "" + var2.getSortedColumn();
      var1[1] = "" + var2.getSortOrder();
      return var1;
   }

   public void setSortInfo(String[] var1) {
      boolean var2 = true;

      int var7;
      try {
         var7 = Integer.parseInt(var1[0]);
      } catch (NumberFormatException var6) {
         var7 = -1;
      }

      if (var7 != -1) {
         boolean var3 = true;

         int var8;
         try {
            var8 = Integer.parseInt(var1[1]);
         } catch (NumberFormatException var5) {
            var8 = -1;
         }

         if (var8 != -1) {
            TableSorter var4 = ((FileTable)this.sourcetable).getTable_sorter();
            var4.sort(var7, var8);
         }
      }
   }

   public void setStatusLabel(JLabel var1) {
      this.label = var1;
      this.origlabeltext = this.label.getText();
   }

   public void setReorderingAllowed(boolean var1) {
      this.sourcetable.getTableHeader().setReorderingAllowed(var1);
   }

   private void handleDifference(TableColumnModel var1, Vector var2) {
      if (var1.getColumnCount() > var2.size()) {
         Object[] var3 = new Object[]{"AAAAAAA", 18, 80};
         var2.add(var3);
      }
   }

   private void resetColumns(String var1, String var2, TableColumnModel var3, String var4, TableColumn var5) {
      Hashtable var6 = SettingsStore.getInstance().remove(var1);
      Enumeration var7 = var6.keys();

      while(var7.hasMoreElements()) {
         String var8 = (String)var7.nextElement();
         if (var8.startsWith(var2)) {
            var6.remove(var8);
         }
      }

      SettingsStore.getInstance().set(var1, var6);
      DefaultTableColumnModel var15 = new DefaultTableColumnModel();

      for(int var9 = 0; var9 < var3.getColumnCount(); ++var9) {
         TableColumn var10 = var3.getColumn(var9);
         TableColumn var11 = new TableColumn(var10.getModelIndex(), var10.getWidth());
         var11.setHeaderValue(var10.getHeaderValue());

         try {
            var11.setMinWidth((Integer)PropertyList.colinfos.get(var4 + "_tc" + var9 + "minw"));
            var11.setMaxWidth((Integer)PropertyList.colinfos.get(var4 + "_tc" + var9 + "maxw"));
            var11.setPreferredWidth((Integer)PropertyList.colinfos.get(var4 + "_tc" + var9 + "pw"));
            var11.setWidth((Integer)PropertyList.colinfos.get(var4 + "_tc" + var9 + "w"));
            var11.setResizable((Integer)PropertyList.colinfos.get(var4 + "_tc" + var9 + "resizable") == 1);
         } catch (Exception var14) {
         }

         if ("id".equals(var11.getHeaderValue())) {
            var11.setMinWidth(0);
            var11.setMaxWidth(0);
            var11.setPreferredWidth(0);
            var11.setWidth(0);
         }

         if (this.sourcetable instanceof ArchiveFileTable) {
            try {
               if (var5.getModelIndex() == var9 && var5.getCellRenderer() != null) {
                  var11.setCellRenderer(var5.getCellRenderer());
               }
            } catch (Exception var13) {
            }
         }

         var15.addColumn(var11);
      }

      this.sourcetable.setColumnModel(var15);
   }

   private int calculateHeight() {
      int var1 = 18 * this.maxvisiblerow + 6;
      int var2 = 6 * this.table.getRowHeight() + 4;
      return Math.max(var1, var2);
   }

   private int[] getCurrenColWidth(TableColumnModel var1) {
      int[] var2 = new int[var1.getColumnCount()];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = var1.getColumn(var3).getPreferredWidth();
      }

      return var2;
   }

   private void resetColWidth(int[] var1) {
      for(int var2 = 0; var2 < this.sourcetable.getColumnModel().getColumnCount(); ++var2) {
         try {
            this.sourcetable.getColumnModel().getColumn(var2).setPreferredWidth(var1[var2]);
         } catch (Exception var4) {
         }
      }

   }
}
