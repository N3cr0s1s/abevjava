package hu.piller.enykp.alogic.filepanels.attachement;

import hu.piller.enykp.util.base.Tools;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDirectoryModel;
import javax.swing.plaf.metal.MetalFileChooserUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class EJFileChooser extends JFileChooser {
   private static final int COLUMN_FILENAME = 0;
   private static final int COLUMN_FILESIZE = 1;
   private static final int COLUMN_FILETYPE = 2;
   private static final int COLUMN_FILEDATE = 3;
   private static final int COLUMN_FILEATTR = 4;
   private static final int COLUMN_COLCOUNT = 5;
   private static String[] COLUMNS = null;

   public EJFileChooser(String var1) {
      if (COLUMNS == null) {
         Locale var2 = this.getLocale();
         COLUMNS = new String[]{UIManager.getString("FileChooser.fileNameHeaderText", var2), UIManager.getString("FileChooser.fileSizeHeaderText", var2), UIManager.getString("FileChooser.fileTypeHeaderText", var2), UIManager.getString("FileChooser.fileDateHeaderText", var2), UIManager.getString("FileChooser.fileAttrHeaderText", var2)};
      }

      this.disableDirectoryCombo(this.getComponents(), var1);
   }

   public EJFileChooser() {
      if (COLUMNS == null) {
         Locale var1 = this.getLocale();
         COLUMNS = new String[]{UIManager.getString("FileChooser.fileNameHeaderText", var1), UIManager.getString("FileChooser.fileSizeHeaderText", var1), UIManager.getString("FileChooser.fileTypeHeaderText", var1), UIManager.getString("FileChooser.fileDateHeaderText", var1), UIManager.getString("FileChooser.fileAttrHeaderText", var1)};
      }

   }

   public final void setUI(ComponentUI var1) {
      super.setUI(new EJFileChooser.UI(this));
   }

   private void disableDirectoryCombo(Component[] var1, String var2) {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var1[var3] instanceof JPanel) {
            this.disableDirectoryCombo(((JPanel)var1[var3]).getComponents(), var2);
         } else {
            try {
               if (var1[var3].getClass().getName().indexOf(var2) > -1) {
                  var1[var3].setEnabled(false);
               }
            } catch (Exception var5) {
            }
         }
      }

   }

   private static final class DirectoryModel extends BasicDirectoryModel {
      int col = 0;
      boolean ascending;

      DirectoryModel(JFileChooser var1) {
         super(var1);
      }

      protected final boolean lt(File var1, File var2) {
         boolean var3 = false;
         switch(this.col) {
         case 1:
            var3 = var1.length() > var2.length();
            break;
         case 3:
            var3 = var1.lastModified() > var2.lastModified();
            break;
         default:
            var3 = var1.getName().compareToIgnoreCase(var2.getName()) > 0;
         }

         return this.ascending ? (var3 = !var3) : var3;
      }

      protected final void sort(int var1, JTable var2) {
         this.col = var1;
         this.ascending = !this.ascending;
         String var3 = " ⇑";
         if (this.ascending) {
            var3 = " ⇓";
         }

         JTableHeader var4 = var2.getTableHeader();
         TableColumnModel var5 = var4.getColumnModel();

         for(int var6 = 0; var6 < 5; ++var6) {
            try {
               TableColumn var7 = var5.getColumn(var6);
               var7.setHeaderValue(EJFileChooser.COLUMNS[var6]);
            } catch (Exception var8) {
               Tools.eLog(var8, 0);
            }
         }

         TableColumn var9 = var5.getColumn(this.col);
         var9.setHeaderValue(EJFileChooser.COLUMNS[this.col] + var3);
         var4.repaint();
         this.validateFileCache();
      }

      protected final void sort(Vector var1) {
         switch(this.col) {
         case 0:
            Collections.sort(var1, new Comparator() {
               public int compare(Object var1, Object var2) {
                  File var3 = (File)var1;
                  File var4 = (File)var2;
                  return DirectoryModel.this.ascending ? var3.getName().compareToIgnoreCase(var4.getName()) : -1 * var3.getName().compareToIgnoreCase(var4.getName());
               }
            });
            break;
         case 1:
            Collections.sort(var1, new Comparator() {
               public int compare(Object var1, Object var2) {
                  int var3 = 1;
                  File var4 = (File)var1;
                  File var5 = (File)var2;
                  if (var4.length() > var5.length()) {
                     var3 = -1;
                  } else if (var4.length() == var5.length()) {
                     var3 = 0;
                  }

                  if (DirectoryModel.this.ascending) {
                     var3 *= -1;
                  }

                  return var3;
               }
            });
         case 2:
         default:
            break;
         case 3:
            Collections.sort(var1, new Comparator() {
               public int compare(Object var1, Object var2) {
                  int var3 = 1;
                  File var4 = (File)var1;
                  File var5 = (File)var2;
                  if (var4.lastModified() > var5.lastModified()) {
                     var3 = -1;
                  } else if (var4.lastModified() == var5.lastModified()) {
                     var3 = 0;
                  }

                  if (DirectoryModel.this.ascending) {
                     var3 *= -1;
                  }

                  return var3;
               }
            });
         }

      }
   }

   private static final class UI extends MetalFileChooserUI {
      private EJFileChooser.DirectoryModel model;

      public UI(JFileChooser var1) {
         super(var1);
      }

      protected final void createModel() {
         this.model = new EJFileChooser.DirectoryModel(this.getFileChooser());
      }

      public final BasicDirectoryModel getModel() {
         return this.model;
      }

      protected final JPanel createDetailsView(JFileChooser var1) {
         JPanel var2 = super.createDetailsView(var1);
         final JTable var3 = findJTable(var2.getComponents());
         if (var3 != null) {
            var3.getTableHeader().setReorderingAllowed(false);
            var3.getTableHeader().addMouseListener(new MouseAdapter() {
               public void mouseClicked(MouseEvent var1) {
                  if (var1.getClickCount() <= 1) {
                     var1.consume();
                     int var2 = var3.getTableHeader().columnAtPoint(var1.getPoint());
                     if (var2 == 0 || var2 == 1 || var2 == 3) {
                        UI.this.model.sort(var2, var3);
                     }

                  }
               }
            });
         }

         return var2;
      }

      private static final JTable findJTable(Component[] var0) {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            if (var0[var1] instanceof JTable) {
               return (JTable)var0[var1];
            }

            if (var0[var1] instanceof Container) {
               JTable var2 = findJTable(((Container)var0[var1]).getComponents());
               if (var2 != null) {
                  return var2;
               }
            }
         }

         return null;
      }
   }
}
