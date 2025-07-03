package hu.piller.enykp.util.base.errordialog;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.table.DefaultTableModel;

public class ErrorDialog extends JDialog implements ActionListener {
   private JList errors;
   private String fileName;
   private boolean processStoppped;
   private boolean windowExit;
   private JLabel dialogLabel;
   private static final int CHAR_LENGTH_IN_A_ROW = 80;
   private static final String SEPARATOR = "  ";
   private static final String MARGIN = "                         ";

   public boolean isProcessStoppped() {
      return this.processStoppped;
   }

   public void setDialogLabel(JLabel var1) {
      this.dialogLabel = var1;
   }

   public ErrorDialog(Frame var1, String var2, boolean var3, DefaultTableModel var4) throws HeadlessException {
      super(var1, var2, var3);
      this.fileName = null;
      this.processStoppped = false;
      this.windowExit = true;
      this.dialogLabel = new JLabel();
      super.setSize(700, 500);
      super.setLocationRelativeTo(var1);
      this.setDefaultCloseOperation(2);

      Vector var5;
      try {
         var5 = this.convertDtm2Vector(var4);
      } catch (Exception var11) {
         var5 = new Vector();
         var5.add(new TextWithIcon("Hiba történt a lista előállításakor!", 0));
         var11.printStackTrace();
      }

      this.dialogLabel.setText("");
      this.getContentPane().setLayout(new BorderLayout(5, 5));
      boolean var6 = true;
      if (!var6) {
         this.errors = this.getList(var5);
      } else {
         this.errors = this.getExtendedList(var5);
      }

      JScrollPane var7 = new JScrollPane(this.errors, 20, 30);
      var7.setPreferredSize(new Dimension(680, 450));
      this.errors.setSelectionMode(0);
      JTextArea var8 = new JTextArea();
      var8.setEditable(false);
      var8.setLineWrap(true);
      var8.setWrapStyleWord(true);
      JScrollPane var9 = new JScrollPane(var8);
      var9.setHorizontalScrollBarPolicy(31);
      var9.setVerticalScrollBarPolicy(20);
      var9.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
      var9.setMinimumSize(new Dimension(Integer.MAX_VALUE, 90));
      var9.setPreferredSize(new Dimension(Integer.MAX_VALUE, 90));
      var9.setAlignmentX(0.0F);
      JPanel var10 = new JPanel();
      var10.setLayout(new BoxLayout(var10, 1));
      var10.setAlignmentX(0.0F);
      var10.setPreferredSize(new Dimension(700, 495));
      var10.add(var7, (Object)null);
      var10.add(var9, (Object)null);
      this.handleClick(var8);
      this.getContentPane().add(var10, "Center");
      this.getContentPane().add(this.dialogLabel, "North");
      this.pack();
      this.setVisible(true);
   }

   public ErrorDialog(Frame var1, String var2, boolean var3, boolean var4, Vector var5) throws HeadlessException {
      this(var1, var2, var3, var4, var5, "", false, (File)null);
   }

   public ErrorDialog(Frame var1, String var2, boolean var3, boolean var4, Vector var5, File var6) throws HeadlessException {
      this(var1, var2, var3, var4, var5, "", false, var6);
   }

   public ErrorDialog(Frame var1, String var2, boolean var3, boolean var4, Vector var5, String var6, boolean var7) throws HeadlessException {
      this(var1, var2, var3, var4, var5, var6, var7, (File)null);
   }

   public ErrorDialog(Frame var1, String var2, boolean var3, boolean var4, Vector var5, String var6, boolean var7, File var8) throws HeadlessException {
      super(var1, var2, var3);
      this.fileName = null;
      this.processStoppped = false;
      this.windowExit = true;
      this.dialogLabel = new JLabel();
      super.setSize(Math.min(GuiUtil.getW("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW"), (int)(0.8D * (double)GuiUtil.getScreenW())), Math.max(500, 12 * GuiUtil.getCommonItemHeight()));
      super.setMinimumSize(super.getSize());
      super.setLocationRelativeTo(var1);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent var1) {
            ErrorDialog.this.processStoppped = ErrorDialog.this.windowExit;
         }
      });
      this.windowExit = true;
      this.setDefaultCloseOperation(2);
      this.dialogLabel.setText(var6);
      this.getContentPane().setLayout(new BorderLayout(5, 5));

      try {
         this.fileName = var8 == null ? null : var8.getName();
      } catch (Exception var14) {
         this.fileName = null;
      }

      JPanel var9 = new JPanel();
      JButton var10 = new JButton();
      if (var7) {
         var10.setText("Tovább");
      } else {
         var10.setText("Rendben");
      }

      var10.setName("okbutton");
      var10.addActionListener(this);
      JButton var11 = new JButton("Lista mentése");
      var11.addActionListener(this);
      JButton var12 = new JButton("Mégsem");
      var12.addActionListener(this);
      var12.setName("cancelbutton");
      var9.add(var10);
      if (var7) {
         var9.add(var12);
      }

      var9.add(var11);
      this.getContentPane().add(var9, "South");
      if (!var4) {
         this.errors = this.getList(var5);
      } else {
         this.errors = this.getExtendedList(var5);
      }

      JScrollPane var13 = new JScrollPane(this.errors, 20, 30);
      var13.setPreferredSize(new Dimension(Math.min(GuiUtil.getW("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW"), (int)(0.8D * (double)GuiUtil.getScreenW())), Math.max(450, 10 * GuiUtil.getCommonItemHeight())));
      this.getContentPane().add(var13, "Center");
      this.getContentPane().add(this.dialogLabel, "North");
      this.pack();
      this.setVisible(true);
   }

   public void actionPerformed(ActionEvent var1) {
      if (var1.getSource() instanceof JButton) {
         this.windowExit = false;
         if (((JButton)var1.getSource()).getText().equals("Lista mentése")) {
            this.doListSave(this.errors);
         } else {
            this.processStoppped = !((JButton)var1.getSource()).getName().equals("okbutton");
            this.dispose();
         }
      }

   }

   private JList getList(Vector var1) {
      return new JList(var1);
   }

   private JList getExtendedList(Vector var1) {
      EJList var2 = new EJList();
      var2.setListData(var1);
      return var2;
   }

   private void doListSave(JList var1) {
      JFileChooser var2 = new JFileChooser();
      var2.setDialogTitle("Mentés");
      var2.setSelectedFile((File)null);
      String var3 = "enyk_hibalista_" + this.getTimeString("yyyyMMdd_HH_mm_ss") + ".txt";
      if (this.fileName != null) {
         var3 = this.fileName + var3;
      }

      try {
         var2.setCurrentDirectory(new File((String)PropertyList.getInstance().get("prop.usr.naplo")));
      } catch (Exception var14) {
         Tools.eLog(var14, 0);
      }

      try {
         ((BasicFileChooserUI)var2.getUI()).setFileName(var3);
      } catch (ClassCastException var12) {
         var12.printStackTrace();

         try {
            var2.setSelectedFile(new File(var3));
         } catch (Exception var11) {
         }
      } catch (Exception var13) {
         var13.printStackTrace();

         try {
            var2.setSelectedFile(new File(var3));
         } catch (Exception var10) {
         }
      }

      int var4 = var2.showSaveDialog(this);
      if (var4 == 0) {
         File var5 = var2.getSelectedFile();
         FileOutputStream var6 = null;

         try {
            var6 = new FileOutputStream(var5);
            int var7;
            if (var1 instanceof EJList) {
               for(var7 = 0; var7 < var1.getModel().getSize(); ++var7) {
                  var6.write((var1.getModel().getElementAt(var7).toString() + "\r\n").getBytes());
               }
            } else {
               for(var7 = 0; var7 < var1.getModel().getSize(); ++var7) {
                  var6.write((var1.getModel().getElementAt(var7) + "\r\n").getBytes());
               }
            }

            var6.close();
         } catch (Exception var15) {
            try {
               var6.close();
            } catch (Exception var9) {
               Tools.eLog(var9, 0);
            }

            GuiUtil.showMessageDialog(this, "A lista mentése nem sikerült!", "Hiba", 0);
         }
      }

   }

   private String getTimeString(String var1) {
      SimpleDateFormat var2 = new SimpleDateFormat(var1);
      return var2.format(Calendar.getInstance().getTime());
   }

   private Vector convertDtm2Vector(DefaultTableModel var1) {
      Vector var2 = new Vector();

      for(int var3 = 0; var3 < var1.getRowCount(); ++var3) {
         String var4 = "";
         var4 = "[" + var1.getValueAt(var3, 3) + "] [" + var1.getValueAt(var3, 2) + "] ";
         if (var1.getValueAt(var3, 5) != null) {
            String var5 = var1.getValueAt(var3, 4).toString();
            String var6 = var1.getValueAt(var3, 5).toString();
            var5 = var5.replaceAll("#13", "");
            var5 = var5.replaceAll(" ", "");
            var6 = var6.replaceAll("#13", "");
            var6 = var6.replaceAll(" ", "");
            if (var6.indexOf(var5) > -1) {
               var4 = var4 + var1.getValueAt(var3, 5);
            } else {
               var4 = var4 + var1.getValueAt(var3, 4) + "  " + var1.getValueAt(var3, 5);
            }
         } else {
            var4 = var4 + var1.getValueAt(var3, 4);
         }

         try {
            if (var1.getValueAt(var3, 6) != null) {
               var4 = var4 + " \n" + var1.getValueAt(var3, 6);
            }
         } catch (Exception var7) {
            var4 = var4 + " \nKiegészítő info hibás";
         }

         var2.add(new TextWithIcon(var4, 0));
      }

      return var2;
   }

   private void _addRow2Vector(Vector var1, String var2) {
      byte var3 = 80;
      String var4 = "";
      boolean var5 = false;
      int var6 = 0;

      while(var6 > -1) {
         int var7 = var6;
         var6 = var2.indexOf("  ", var6 + var3);
         if (var6 > -1) {
            if (var7 == 0) {
               var1.add(new TextWithIcon(var2.substring(var7, var6), 0));
            } else {
               var1.add(new TextWithIcon("                         " + var2.substring(var7, var6), -1));
            }
         } else if (var7 == 0) {
            var1.add(new TextWithIcon(var2.substring(var7), 0));
         } else {
            var1.add(new TextWithIcon("                         " + var2.substring(var7), -1));
         }
      }

   }

   private void handleClick(final JTextArea var1) {
      this.errors.addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent var1x) {
            TextWithIcon var2 = (TextWithIcon)ErrorDialog.this.errors.getSelectedValue();
            var1.setText(var2.getMSG());
         }
      });
   }
}
