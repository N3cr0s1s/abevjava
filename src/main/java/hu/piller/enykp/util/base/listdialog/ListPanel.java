package hu.piller.enykp.util.base.listdialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class ListPanel extends JPanel {
   private static final String HUN_CHARSET = "ISO-8859-2";
   private File file;
   private String charset;
   private boolean is_initialized;
   private JList list;
   private JButton ok_button;

   public ListPanel() {
      this.setFileCharset("ISO-8859-2");
      this.initialize();
   }

   public ListPanel(File var1) {
      this.setFile(var1);
      this.setFileCharset("ISO-8859-2");
      this.initialize();
   }

   public ListPanel(File var1, String var2) {
      this.setFile(var1);
      this.setFileCharset(var2);
      this.initialize();
   }

   private void initialize() {
      this.build();
      this.prepare();
      this.is_initialized = true;
      this.rebuild();
   }

   public void setFile(File var1) {
      if (var1 != null && var1.exists()) {
         this.file = var1;
         this.rebuild();
      }

   }

   public void setFileCharset(String var1) {
      this.charset = var1;
   }

   public void rebuild() {
      if (this.is_initialized) {
         DefaultListModel var1 = (DefaultListModel)this.list.getModel();
         if (var1 != null) {
            var1.clear();
         }

         if (var1 == null) {
            var1 = new DefaultListModel();
         }

         if (this.file != null && this.file.exists()) {
            this.read(var1);
         }

         this.list.setModel(var1);
      }

   }

   private void build() {
      JList var1 = new JList(new DefaultListModel());
      JScrollPane var2 = new JScrollPane(var1);
      JButton var3 = new JButton("OK");
      JPanel var4 = new JPanel();
      var4.setLayout(new FlowLayout(2, 3, 5));
      var4.add(var3);
      this.setLayout(new BorderLayout());
      this.add(Box.createHorizontalStrut(3), "West");
      this.add(Box.createHorizontalStrut(3), "East");
      this.add(Box.createVerticalStrut(3), "North");
      this.add(var2, "Center");
      this.add(var4, "South");
      this.list = var1;
      this.ok_button = var3;
   }

   private void prepare() {
      this.list.getInputMap(1).remove(KeyStroke.getKeyStroke(27, 0));
      this.ok_button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Component var2 = SwingUtilities.getRoot(ListPanel.this);
            if (var2 instanceof JDialog) {
               ((JDialog)var2).dispose();
            } else if (var2 instanceof JFrame) {
               ((JFrame)var2).dispose();
            }

         }
      });
   }

   private void read(DefaultListModel var1) {
      BufferedReader var2 = null;

      try {
         if (this.charset == null) {
            var2 = new BufferedReader(new InputStreamReader(new FileInputStream(this.file)));
         } else {
            var2 = new BufferedReader(new InputStreamReader(new FileInputStream(this.file), this.charset));
         }

         String var3;
         while((var3 = var2.readLine()) != null) {
            var1.addElement(var3);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      if (var2 != null) {
         try {
            var2.close();
         } catch (IOException var4) {
            var4.printStackTrace();
         }
      }

   }
}
