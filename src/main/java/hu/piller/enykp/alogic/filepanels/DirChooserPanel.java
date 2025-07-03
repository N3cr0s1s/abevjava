package hu.piller.enykp.alogic.filepanels;

import me.necrocore.abevjava.NecroFile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class DirChooserPanel extends JPanel {
   private File rootdir;
   private JTree tree;
   private JTextField tf;
   private Vector list;
   private int db;
   private Vector found;
   private int found_pos;

   public DirChooserPanel() {
      this.setLayout(new BorderLayout());
      DefaultMutableTreeNode var1 = new DefaultMutableTreeNode("Üres");
      DefaultTreeModel var2 = new DefaultTreeModel(var1);
      this.tree = new JTree(var2);
      this.tree.setFocusable(false);
      JScrollPane var3 = new JScrollPane(this.tree);
      this.add(var3);
      JPanel var4 = new JPanel(new BorderLayout());
      JButton var5 = new JButton(UIManager.getIcon("FileChooser.newFolderIcon"));
      var5.setToolTipText("Új könyvtár létrehozása");
      var5.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TreePath var2 = DirChooserPanel.this.tree.getSelectionPath();
            if (var2 != null) {
               if (DirChooserPanel.this.tf.getText().trim().length() != 0) {
                  DefaultMutableTreeNode var3 = (DefaultMutableTreeNode)var2.getLastPathComponent();
                  DirChooserPanel.Entity var4 = (DirChooserPanel.Entity)var3.getUserObject();
                  File var5 = new NecroFile(var4.f, DirChooserPanel.this.tf.getText());
                  boolean var6 = var5.mkdir();
                  if (var6) {
                     DirChooserPanel.this.setRootdir(DirChooserPanel.this.rootdir, var5);
                  }
               }
            }
         }
      });
      JButton var6 = new JButton(UIManager.getIcon("FileChooser.newFolderIcon"));
      var6.setBackground(Color.RED);
      var6.setToolTipText("A kijelölt könyvtár törlése");
      var6.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TreePath var2 = DirChooserPanel.this.tree.getSelectionPath();
            if (var2 != null) {
               DefaultMutableTreeNode var3 = (DefaultMutableTreeNode)var2.getLastPathComponent();
               DirChooserPanel.Entity var4 = (DirChooserPanel.Entity)var3.getUserObject();
               File var5 = var4.f;
               boolean var6 = var5.delete();
               if (var6) {
                  DirChooserPanel.this.setRootdir(DirChooserPanel.this.rootdir, (File)null);
               }

            }
         }
      });
      JPanel var7 = new JPanel(new BorderLayout());
      var7.add(var5, "West");
      var7.add(var6, "East");
      var4.add(var7, "East");
      this.tf = new JTextField();
      this.tf.setEnabled(false);
      this.tf.getDocument().addDocumentListener(new DocumentListener() {
         public void insertUpdate(DocumentEvent var1) {
            DirChooserPanel.this.tf_change();
         }

         public void removeUpdate(DocumentEvent var1) {
            DirChooserPanel.this.tf_change();
         }

         public void changedUpdate(DocumentEvent var1) {
            DirChooserPanel.this.tf_change();
         }
      });
      this.tf.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent var1) {
            int[] var2;
            int var3;
            TreePath var4;
            DefaultMutableTreeNode var6;
            if (var1.getKeyCode() == 40) {
               if (var1.getModifiers() == 1) {
                  DirChooserPanel.this.found_pos++;
                  if (DirChooserPanel.this.found_pos >= DirChooserPanel.this.found.size()) {
                     DirChooserPanel.this.found_pos = 0;
                  }

                  var6 = (DefaultMutableTreeNode)DirChooserPanel.this.found.get(DirChooserPanel.this.found_pos);
                  DirChooserPanel.this.visibleNode(var6);
                  return;
               }

               var2 = DirChooserPanel.this.tree.getSelectionRows();
               if (var2 == null) {
                  return;
               }

               var3 = var2[0] + 1;
               DirChooserPanel.this.tree.setSelectionRow(var3);
               var4 = DirChooserPanel.this.tree.getSelectionPath();
               DirChooserPanel.this.tree.scrollPathToVisible(var4);
            }

            if (var1.getKeyCode() == 38) {
               if (var1.getModifiers() == 1) {
                  DirChooserPanel.this.found_pos--;
                  if (DirChooserPanel.this.found_pos == -1) {
                     DirChooserPanel.this.found_pos = DirChooserPanel.this.found.size() - 1;
                  }

                  var6 = (DefaultMutableTreeNode)DirChooserPanel.this.found.get(DirChooserPanel.this.found_pos);
                  DirChooserPanel.this.visibleNode(var6);
                  return;
               }

               var2 = DirChooserPanel.this.tree.getSelectionRows();
               if (var2 == null) {
                  return;
               }

               var3 = var2[0] - 1;
               DirChooserPanel.this.tree.setSelectionRow(var3);
               var4 = DirChooserPanel.this.tree.getSelectionPath();
               DirChooserPanel.this.tree.scrollPathToVisible(var4);
            }

            if (var1.getKeyCode() == 39) {
               var2 = DirChooserPanel.this.tree.getSelectionRows();
               DirChooserPanel.this.tree.expandRow(var2[0]);
               var3 = var2[0] + 1;
               DirChooserPanel.this.tree.setSelectionRow(var3);
               var4 = DirChooserPanel.this.tree.getSelectionPath();
               DirChooserPanel.this.tree.scrollPathToVisible(var4);
            }

            if (var1.getKeyCode() == 36) {
               DirChooserPanel.this.tree.setSelectionRow(0);
               TreePath var5 = DirChooserPanel.this.tree.getSelectionPath();
               DirChooserPanel.this.tree.scrollPathToVisible(var5);
               var1.consume();
            }

            if (var1.getKeyCode() == 37) {
            }

            if (var1.getKeyCode() == 27) {
               DirChooserPanel.this.tf.setText("");
            }

            if (var1.getKeyCode() == 10) {
            }

         }
      });
      var4.add(this.tf);
      this.add(var4, "South");
   }

   private void visibleNode(DefaultMutableTreeNode var1) {
      TreePath var2 = new TreePath(var1.getPath());
      this.tree.setSelectionPath(var2);
      this.tree.makeVisible(var2);
      this.tree.scrollPathToVisible(var2);
   }

   private void tf_change() {
      this.db = 0;
      this.found = new Vector();
      DefaultMutableTreeNode var1 = null;

      for(int var2 = 0; var2 < this.list.size(); ++var2) {
         DefaultMutableTreeNode var3 = (DefaultMutableTreeNode)this.list.get(var2);
         DirChooserPanel.Entity var4 = (DirChooserPanel.Entity)var3.getUserObject();
         File var5 = var4.f;
         int var6 = var5.getName().toLowerCase().startsWith(this.tf.getText().toLowerCase()) ? 1 : -1;
         if (var6 != -1) {
            if (this.db == 0) {
               var1 = var3;
            }

            ++this.db;
            this.found.add(var3);
         }
      }

      if (var1 != null) {
         this.found_pos = 0;
         this.visibleNode(var1);
         System.out.println("db=" + this.db);
      }
   }

   public void setRootdir(File var1) {
      this.setRootdir(var1, (File)null);
   }

   public void setRootdir(File var1, final File var2) {
      this.rootdir = var1;
      DefaultMutableTreeNode var3 = new DefaultMutableTreeNode("Szerkezet betöltése folyamatban ... ");
      DefaultTreeModel var4 = new DefaultTreeModel(var3);
      this.tree.setModel(var4);
      Thread var5 = new Thread(new Runnable() {
         public void run() {
            DefaultTreeModel var1 = DirChooserPanel.this.getDirModel((File)null);
            DirChooserPanel.this.tree.setModel(var1);
            if (var2 != null) {
               DefaultMutableTreeNode var2x = DirChooserPanel.this.getFromList(var2);
               if (var2x != null) {
                  DirChooserPanel.this.visibleNode(var2x);
               }
            }

            DirChooserPanel.this.tf.setEnabled(true);
            DirChooserPanel.this.tf.requestFocus();
         }
      });
      var5.start();
   }

   public DefaultTreeModel getDirModel(File var1) {
      if (var1 == null) {
         var1 = this.rootdir;
      }

      this.list = new Vector();
      if (var1.isDirectory()) {
         DefaultMutableTreeNode var2 = new DefaultMutableTreeNode(new DirChooserPanel.Entity(var1));
         DefaultTreeModel var3 = new DefaultTreeModel(var2);
         this.fillsubdirmodel(var1, var2);
         return var3;
      } else {
         return null;
      }
   }

   public void fillsubdirmodel(File var1, DefaultMutableTreeNode var2) {
      String[] var3 = new String[0];
      var3 = var1.list();
      if (var3 != null) {
         ((DirChooserPanel.Entity)var2.getUserObject()).db = var3.length;

         for(int var4 = 0; var4 < var3.length; ++var4) {
            File var5 = new NecroFile(var1, var3[var4]);
            if (var5.isDirectory()) {
               DefaultMutableTreeNode var6 = new DefaultMutableTreeNode(new DirChooserPanel.Entity(var5));
               var2.add(var6);
               this.list.add(var6);
               this.fillsubdirmodel(var5, var6);
            }
         }

      }
   }

   private DefaultMutableTreeNode getFromList(File var1) {
      for(int var2 = 0; var2 < this.list.size(); ++var2) {
         DefaultMutableTreeNode var3 = (DefaultMutableTreeNode)this.list.get(var2);
         if (((DirChooserPanel.Entity)var3.getUserObject()).f.equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public File getSelectedDir() {
      try {
         TreePath var1 = this.tree.getSelectionPath();
         DefaultMutableTreeNode var2 = (DefaultMutableTreeNode)var1.getLastPathComponent();
         DirChooserPanel.Entity var3 = (DirChooserPanel.Entity)var2.getUserObject();
         File var4 = var3.f;
         return var4;
      } catch (Exception var5) {
         return null;
      }
   }

   public class Entity {
      File f;
      int db;

      public Entity(File var2) {
         this.f = var2;
      }

      public String toString() {
         if (this.f == null) {
            return "";
         } else {
            String var1 = this.f.getName();
            return var1.length() == 0 ? "Mentések  ( " + this.db + " )" : var1 + "  ( " + this.db + " )";
         }
      }
   }
}
