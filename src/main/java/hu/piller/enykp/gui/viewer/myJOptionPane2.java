package hu.piller.enykp.gui.viewer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class myJOptionPane2 extends JOptionPane {
   static JDialog dialog;
   static String msg;
   static String msg2;
   static boolean details = false;
   static int h1;
   static int h2;
   static String bstr = ">>";
   static String bstr2 = "<<";
   JButton btn;
   static int origwidth;

   public myJOptionPane2(Object var1, int var2, int var3, Icon var4, Object[] var5, Object var6) {
      msg = var1.toString();
      this.message = var1;
      this.options = var5;
      this.initialValue = var6;
      this.icon = var4;
      details = false;
      this.setMessageType(var2);
      this.setOptionType(var3);
      this.value = UNINITIALIZED_VALUE;
      this.inputValue = UNINITIALIZED_VALUE;
      this.updateUI();
   }

   public static int showOptionDialog(myJOptionPane2 var0, Component var1, Object var2, String var3, int var4, int var5, Icon var6, Object[] var7, Object var8) throws HeadlessException {
      details = false;
      origwidth = -1;
      var0.setInitialValue(var8);
      var0.setComponentOrientation(((Component)(var1 == null ? getRootFrame() : var1)).getComponentOrientation());
      int var9 = styleFromMessageType(var5);
      dialog = var0.createDialog(var1, var3, var9);
      var0.selectInitialValue();
      var0.setIcon(var6);
      dialog.show();
      dialog.dispose();
      Object var10 = var0.getValue();
      if (var10 == null) {
         return -1;
      } else if (var7 == null) {
         return var10 instanceof Integer ? (Integer)var10 : -1;
      } else {
         int var11 = 0;

         for(int var12 = var7.length; var11 < var12; ++var11) {
            if (var7[var11].equals(var10)) {
               return var11;
            }
         }

         return -1;
      }
   }

   private static int styleFromMessageType(int var0) {
      switch(var0) {
      case -1:
      default:
         return 2;
      case 0:
         return 4;
      case 1:
         return 3;
      case 2:
         return 8;
      case 3:
         return 7;
      }
   }

   private JDialog createDialog(Component var1, String var2, int var3) throws HeadlessException {
      Window var5 = getWindowForComponent(var1);
      JDialog var4;
      if (var5 instanceof Frame) {
         var4 = new JDialog((Frame)var5, var2, true);
      } else {
         var4 = new JDialog((Dialog)var5, var2, true);
      }

      this.initDialog(var4, var3, var1);
      return var4;
   }

   static Window getWindowForComponent(Component var0) throws HeadlessException {
      if (var0 == null) {
         return getRootFrame();
      } else {
         return !(var0 instanceof Frame) && !(var0 instanceof Dialog) ? getWindowForComponent(var0.getParent()) : (Window)var0;
      }
   }

   private void initDialog(final JDialog var1, int var2, Component var3) {
      var1.setComponentOrientation(this.getComponentOrientation());
      Container var4 = var1.getContentPane();
      var4.setLayout(new BorderLayout());
      var4.add(this, "Center");
      var1.setResizable(false);
      if (JDialog.isDefaultLookAndFeelDecorated()) {
         boolean var5 = UIManager.getLookAndFeel().getSupportsWindowDecorations();
         if (var5) {
            var1.setUndecorated(true);
            this.getRootPane().setWindowDecorationStyle(var2);
         }
      }

      var1.pack();
      var1.setLocationRelativeTo(var3);
      WindowAdapter var6 = new WindowAdapter() {
         private boolean gotFocus = false;

         public void windowClosing(WindowEvent var1) {
            myJOptionPane2.this.setValue((Object)null);
         }

         public void windowGainedFocus(WindowEvent var1) {
            if (!this.gotFocus) {
               myJOptionPane2.this.selectInitialValue();
               this.gotFocus = true;
            }

         }
      };
      var1.addWindowListener(var6);
      var1.addWindowFocusListener(var6);
      var1.addComponentListener(new ComponentAdapter() {
         public void componentShown(ComponentEvent var1) {
            myJOptionPane2.this.setValue(JOptionPane.UNINITIALIZED_VALUE);
         }
      });
      this.addPropertyChangeListener(new PropertyChangeListener() {
         public void propertyChange(PropertyChangeEvent var1x) {
            if (var1.isVisible() && var1x.getSource() == myJOptionPane2.this && var1x.getPropertyName().equals("value") && var1x.getNewValue() != null && var1x.getNewValue() != JOptionPane.UNINITIALIZED_VALUE) {
               var1.setVisible(false);
            }

         }
      });
   }

   public void refreshsize() {
      if (origwidth == -1) {
         origwidth = dialog.getWidth();
      }

      if (h1 == 0) {
         h1 = dialog.getHeight();
         h2 = h1 + 80;
      }

      if (details) {
         this.btn.setText(bstr);
         this.setMessage(msg);
         dialog.setSize(origwidth, h1);
         details = false;
      } else {
         this.btn.setText(bstr2);
         this.setMessage(msg + msg2);
         int var1 = dialog.getWidth();
         if (var1 < 600) {
            var1 = 600;
         }

         dialog.setSize(var1, h2);
         details = true;
      }
   }

   public void set2msg(String var1) {
      msg2 = var1;
   }

   public void setbutton(JButton var1) {
      this.btn = var1;
   }
}
