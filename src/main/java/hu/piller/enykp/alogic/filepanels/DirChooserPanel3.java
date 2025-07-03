package hu.piller.enykp.alogic.filepanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Position.Bias;

public class DirChooserPanel3 extends JPanel {
   private File rootdir;
   private File curdir;
   private JLabel curdir_lbl;
   private JList l;
   private JTextField tf;
   private int db;
   private int found_pos;

   public DirChooserPanel3() {
      this.setLayout(new BorderLayout());
      this.curdir_lbl = new JLabel("");
      this.add(this.curdir_lbl, "North");
      DefaultListModel var1 = new DefaultListModel();
      var1.addElement("Üres");
      this.l = new JList();
      this.l.setFocusable(false);
      this.l.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent var1) {
            if (var1.getClickCount() == 2) {
               DirChooserPanel3.this.done_enter();
            }

         }
      });
      JScrollPane var2 = new JScrollPane(this.l);
      this.add(var2);
      JPanel var3 = new JPanel(new BorderLayout());
      JButton var4 = new JButton(UIManager.getIcon("FileChooser.newFolderIcon"));
      var4.setToolTipText("Új könyvtár létrehozása");
      var4.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            DirChooserPanel3.Entity var2 = (DirChooserPanel3.Entity)DirChooserPanel3.this.l.getSelectedValue();
            if (var2 != null) {
               if (DirChooserPanel3.this.tf.getText().trim().length() != 0) {
                  File var3 = new File(var2.f.getParentFile(), DirChooserPanel3.this.tf.getText());
                  boolean var4 = var3.mkdir();
                  if (var4) {
                     DirChooserPanel3.this.refresh(var3);
                  }
               }
            }
         }
      });
      var4.setFocusable(false);
      JButton var5 = new JButton(UIManager.getIcon("FileChooser.newFolderIcon"));
      var5.setBackground(Color.RED);
      var5.setToolTipText("A kijelölt könyvtár törlése");
      var5.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            DirChooserPanel3.Entity var2 = (DirChooserPanel3.Entity)DirChooserPanel3.this.l.getSelectedValue();
            if (var2 != null) {
               File var3 = var2.f;
               boolean var4 = var3.delete();
               if (var4) {
                  DirChooserPanel3.this.refresh(var3);
               }

            }
         }
      });
      var5.setFocusable(false);
      JPanel var6 = new JPanel(new BorderLayout());
      var6.add(var4, "West");
      var6.add(var5, "East");
      var3.add(var6, "East");
      this.tf = new JTextField();
      this.tf.setEnabled(false);
      this.tf.getDocument().addDocumentListener(new DocumentListener() {
         public void insertUpdate(DocumentEvent var1) {
            DirChooserPanel3.this.tf_change();
         }

         public void removeUpdate(DocumentEvent var1) {
            DirChooserPanel3.this.tf_change();
         }

         public void changedUpdate(DocumentEvent var1) {
            DirChooserPanel3.this.tf_change();
         }
      });
      this.tf.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent var1) {
            int var2;
            Rectangle var3;
            if (var1.getKeyCode() == 40) {
               if (var1.getModifiers() == 1) {
                  var2 = DirChooserPanel3.this.l.getNextMatch(DirChooserPanel3.this.tf.getText(), DirChooserPanel3.this.found_pos, Bias.Forward);
                  DirChooserPanel3.this.found_pos = var2 + 1;
                  if (DirChooserPanel3.this.found_pos >= DirChooserPanel3.this.l.getModel().getSize()) {
                     DirChooserPanel3.this.found_pos = 0;
                  }

                  DirChooserPanel3.this.l.setSelectedIndex(var2);
                  var3 = DirChooserPanel3.this.l.getUI().getCellBounds(DirChooserPanel3.this.l, var2, var2);
                  DirChooserPanel3.this.l.scrollRectToVisible(var3);
                  return;
               }

               var2 = DirChooserPanel3.this.l.getSelectedIndex();
               ++var2;
               if (DirChooserPanel3.this.l.getModel().getSize() <= var2) {
                  return;
               }

               DirChooserPanel3.this.l.setSelectedIndex(var2);
               var3 = DirChooserPanel3.this.l.getUI().getCellBounds(DirChooserPanel3.this.l, var2, var2);
               DirChooserPanel3.this.l.scrollRectToVisible(var3);
            }

            if (var1.getKeyCode() == 38) {
               if (var1.getModifiers() == 1) {
                  var2 = DirChooserPanel3.this.l.getNextMatch(DirChooserPanel3.this.tf.getText(), DirChooserPanel3.this.found_pos, Bias.Backward);
                  DirChooserPanel3.this.found_pos = var2 - 1;
                  if (DirChooserPanel3.this.found_pos == -1) {
                     DirChooserPanel3.this.found_pos = DirChooserPanel3.this.l.getModel().getSize() - 1;
                  }

                  DirChooserPanel3.this.l.setSelectedIndex(var2);
                  var3 = DirChooserPanel3.this.l.getUI().getCellBounds(DirChooserPanel3.this.l, var2, var2);
                  DirChooserPanel3.this.l.scrollRectToVisible(var3);
                  return;
               }

               var2 = DirChooserPanel3.this.l.getSelectedIndex();
               --var2;
               if (var2 < 0) {
                  return;
               }

               DirChooserPanel3.this.l.setSelectedIndex(var2);
               var3 = DirChooserPanel3.this.l.getUI().getCellBounds(DirChooserPanel3.this.l, var2, var2);
               DirChooserPanel3.this.l.scrollRectToVisible(var3);
            }

            if (var1.getKeyCode() == 39) {
            }

            if (var1.getKeyCode() == 36) {
               DirChooserPanel3.this.l.setSelectedIndex(0);
               DirChooserPanel3.this.l.scrollRectToVisible(new Rectangle(0, 0));
               var1.consume();
            }

            if (var1.getKeyCode() == 37) {
            }

            if (var1.getKeyCode() == 27) {
               DirChooserPanel3.this.tf.setText("");
            }

            if (var1.getKeyCode() == 10) {
               DirChooserPanel3.this.done_enter();
            }

         }
      });
      var3.add(this.tf);
      this.add(var3, "South");
   }

   private void done_enter() {
      DirChooserPanel3.Entity var1 = (DirChooserPanel3.Entity)this.l.getSelectedValue();
      if (var1 != null) {
         DefaultListModel var2 = this.getDirModel(var1.f);
         this.l.setModel(var2);
         int var3 = 0;
         if (var1.startdir != null) {
            DirChooserPanel3.Entity var4 = new DirChooserPanel3.Entity(var1.startdir);
            var3 = var2.indexOf(var4);
         }

         this.l.setSelectedIndex(var3);
         Rectangle var5 = this.l.getUI().getCellBounds(this.l, var3, var3);
         this.l.scrollRectToVisible(var5);
      }
   }

   private void refresh(File var1) {
      File var2 = var1.getParentFile();
      DefaultListModel var3 = this.getDirModel(var2);
      this.l.setModel(var3);
      int var4 = this.l.getNextMatch(var1.getName(), 0, Bias.Forward);
      if (var4 != -1) {
         this.l.setSelectedIndex(var4);
         Rectangle var5 = this.l.getUI().getCellBounds(this.l, var4, var4);
         this.l.scrollRectToVisible(var5);
      }
   }

   private void tf_change() {
      int var1 = this.l.getNextMatch(this.tf.getText(), 0, Bias.Forward);
      this.found_pos = var1 + 1;
      if (var1 != -1) {
         this.l.setSelectedIndex(var1);
         Rectangle var2 = this.l.getUI().getCellBounds(this.l, var1, var1);
         this.l.scrollRectToVisible(var2);
      }
   }

   public void setRootdir(File var1) {
      this.setRootdir(var1, (File)null);
   }

   public void setRootdir(File var1, final File var2) {
      this.rootdir = var1;
      this.found_pos = 0;
      DefaultListModel var3 = new DefaultListModel();
      var3.addElement("Szerkezet betöltése folyamatban ... ");
      this.l.setModel(var3);
      Thread var4 = new Thread(new Runnable() {
         public void run() {
            DefaultListModel var1 = DirChooserPanel3.this.getDirModel((File)null);
            DirChooserPanel3.this.l.setModel(var1);
            if (var2 != null) {
            }

            DirChooserPanel3.this.tf.setEnabled(true);
            DirChooserPanel3.this.tf.requestFocus();
         }
      });
      var4.start();
   }

   public DefaultListModel getDirModel(File var1) {
      if (var1 == null) {
         var1 = this.rootdir;
      }

      if (var1.isDirectory()) {
         this.curdir_lbl.setText(var1.getAbsolutePath());
         DefaultListModel var2 = new DefaultListModel();
         this.fillsubdirmodel(var1, var2);
         return var2;
      } else {
         return null;
      }
   }

   public void fillsubdirmodel(File var1, DefaultListModel var2) {
      String[] var3 = new String[0];
      var3 = var1.list();
      if (!this.rootdir.equals(var1)) {
         var2.addElement(new DirChooserPanel3.Entity(var1.getParentFile(), "..", var1));
      }

      for(int var4 = 0; var4 < var3.length; ++var4) {
         File var5 = new File(var1, var3[var4]);
         if (var5.isDirectory()) {
            var2.addElement(new DirChooserPanel3.Entity(var5));
         }
      }

   }

   public File getSelectedDir() {
      try {
         int var1 = this.l.getSelectedIndex();
         DirChooserPanel3.Entity var2 = (DirChooserPanel3.Entity)this.l.getSelectedValue();
         File var3 = var2.f;
         return var3;
      } catch (Exception var4) {
         return null;
      }
   }

   public class Entity {
      File f;
      File startdir;
      String title;
      int db;

      public Entity(File var2) {
         this.f = var2;
      }

      public Entity(File var2, String var3) {
         this.f = var2;
         this.title = var3;
      }

      public Entity(File var2, String var3, File var4) {
         this.f = var2;
         this.title = var3;
         this.startdir = var4;
      }

      public String toString() {
         if (this.title != null) {
            return this.title;
         } else if (this.f == null) {
            return "";
         } else {
            String var1 = this.f.getName();
            return var1.length() == 0 ? "Mentések  ( " + this.db + " )" : var1;
         }
      }

      public boolean equals(Object var1) {
         try {
            return this.f.equals(((DirChooserPanel3.Entity)var1).f);
         } catch (Exception var3) {
            return false;
         }
      }
   }
}
