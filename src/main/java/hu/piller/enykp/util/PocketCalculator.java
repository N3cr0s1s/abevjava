package hu.piller.enykp.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PocketCalculator extends JFrame implements ActionListener {
   double firstNum;
   double secondNum;
   double total;
   double plusminus;
   int oper;
   int status = 0;
   int buttonSize = 50;
   int margin = 10;
   int gap = 5;
   Insets buttonIsets = new Insets(0, 0, 0, 0);
   Color bgColor = new Color(235, 251, 252);
   private JButton clear;
   private JButton decimal;
   private JTextField display;
   private JButton divide;
   private JButton equals;
   private JButton num1;
   private JButton num0;
   private JButton num2;
   private JButton num3;
   private JButton num4;
   private JButton num5;
   private JButton num6;
   private JButton num7;
   private JButton num8;
   private JButton num9;
   private JPanel jPanel1;
   private JButton minus;
   private JButton multiply;
   private JButton plus;
   private JButton posneg;
   private JButton back;

   public PocketCalculator() {
      this.initComponents();
      KeyboardFocusManager var1 = KeyboardFocusManager.getCurrentKeyboardFocusManager();
      var1.addKeyEventDispatcher(new PocketCalculator.PCKeyEventDispatcher());
   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.display = new JTextField();
      this.num1 = new JButton();
      this.num2 = new JButton();
      this.num3 = new JButton();
      this.num4 = new JButton();
      this.num5 = new JButton();
      this.num6 = new JButton();
      this.num7 = new JButton();
      this.num8 = new JButton();
      this.num9 = new JButton();
      this.num0 = new JButton();
      this.clear = new JButton();
      this.decimal = new JButton();
      this.plus = new JButton();
      this.minus = new JButton();
      this.multiply = new JButton();
      this.divide = new JButton();
      this.posneg = new JButton();
      this.back = new JButton();
      this.equals = new JButton();
      this.setDefaultCloseOperation(3);
      this.setTitle("Számológép");
      this.getContentPane().setBackground(this.bgColor);
      this.setResizable(false);
      this.jPanel1.setBackground(this.bgColor);
      this.display.setEditable(false);
      this.display.setBorder(BorderFactory.createEtchedBorder());
      this.display.setPreferredSize(new Dimension(2 * this.margin + 4 * this.buttonSize + 3 * this.gap - 4, 25));
      this.display.setBackground(new Color(255, 255, 255));
      this.num1.setText("1");
      this.num1.setName("NUM1");
      this.num1.addActionListener(this);
      this.num2.setText("2");
      this.num2.setName("NUM2");
      this.num2.addActionListener(this);
      this.num3.setText("3");
      this.num3.setName("NUM3");
      this.num3.addActionListener(this);
      this.num4.setText("4");
      this.num4.setName("NUM4");
      this.num4.addActionListener(this);
      this.num5.setText("5");
      this.num5.setName("NUM5");
      this.num5.addActionListener(this);
      this.num6.setText("6");
      this.num6.setName("NUM6");
      this.num6.addActionListener(this);
      this.num7.setText("7");
      this.num7.setName("NUM7");
      this.num7.addActionListener(this);
      this.num8.setText("8");
      this.num8.setName("NUM8");
      this.num8.addActionListener(this);
      this.num9.setText("9");
      this.num9.setName("NUM9");
      this.num9.addActionListener(this);
      this.num0.setText("0");
      this.num0.setName("NUM0");
      this.num0.addActionListener(this);
      this.clear.setText("C");
      this.clear.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            PocketCalculator.this.clearActionPerformed();
         }
      });
      this.decimal.setText(".");
      this.decimal.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            PocketCalculator.this.decimalActionPerformed();
         }
      });
      this.plus.setText("+");
      this.plus.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            PocketCalculator.this.operActionPerformed(1);
         }
      });
      this.minus.setText("-");
      this.minus.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            PocketCalculator.this.operActionPerformed(2);
         }
      });
      this.multiply.setText("*");
      this.multiply.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            PocketCalculator.this.operActionPerformed(3);
         }
      });
      this.divide.setText("/");
      this.divide.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            PocketCalculator.this.operActionPerformed(4);
         }
      });
      this.posneg.setText("+/-");
      this.posneg.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            PocketCalculator.this.posnegActionPerformed();
         }
      });
      this.back.setText("<-");
      this.back.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            PocketCalculator.this.backActionPerformed();
         }
      });
      this.equals.setText("=");
      this.equals.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            PocketCalculator.this.equalsActionPerformed(true);
         }
      });
      this.jPanel1.setLayout((LayoutManager)null);
      this.jPanel1.setSize(2 * this.margin + 4 * this.buttonSize + 3 * this.gap, 2 * this.margin + 5 * this.buttonSize + 4 * this.gap);
      JButton[] var1 = new JButton[]{this.num1, this.num2, this.num3, this.num4, this.num5, this.num6, this.num7, this.num8, this.num9, this.num0, this.clear, this.decimal, this.plus, this.minus, this.multiply, this.divide, this.posneg, this.back, this.equals};

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2].setMargin(this.buttonIsets);
         this.jPanel1.add(var1[var2]);
      }

      this.back.setBounds(this.margin, this.margin, this.buttonSize, this.buttonSize);
      this.divide.setBounds(this.margin + this.gap + this.buttonSize, this.margin, this.buttonSize, this.buttonSize);
      this.multiply.setBounds(this.margin + 2 * this.gap + 2 * this.buttonSize, this.margin, this.buttonSize, this.buttonSize);
      this.minus.setBounds(this.margin + 3 * this.gap + 3 * this.buttonSize, this.margin, this.buttonSize, this.buttonSize);
      this.num7.setBounds(this.margin, this.margin + this.gap + this.buttonSize, this.buttonSize, this.buttonSize);
      this.num8.setBounds(this.margin + this.gap + this.buttonSize, this.margin + this.gap + this.buttonSize, this.buttonSize, this.buttonSize);
      this.num9.setBounds(this.margin + 2 * this.gap + 2 * this.buttonSize, this.margin + this.gap + this.buttonSize, this.buttonSize, this.buttonSize);
      this.plus.setBounds(this.margin + 3 * this.gap + 3 * this.buttonSize, this.margin + this.gap + this.buttonSize, this.buttonSize, 2 * this.buttonSize + this.gap);
      this.num4.setBounds(this.margin, this.margin + 2 * this.gap + 2 * this.buttonSize, this.buttonSize, this.buttonSize);
      this.num5.setBounds(this.margin + this.gap + this.buttonSize, this.margin + 2 * this.gap + 2 * this.buttonSize, this.buttonSize, this.buttonSize);
      this.num6.setBounds(this.margin + 2 * this.gap + 2 * this.buttonSize, this.margin + 2 * this.gap + 2 * this.buttonSize, this.buttonSize, this.buttonSize);
      this.num1.setBounds(this.margin, this.margin + 3 * this.gap + 3 * this.buttonSize, this.buttonSize, this.buttonSize);
      this.num2.setBounds(this.margin + this.gap + this.buttonSize, this.margin + 3 * this.gap + 3 * this.buttonSize, this.buttonSize, this.buttonSize);
      this.num3.setBounds(this.margin + 2 * this.gap + 2 * this.buttonSize, this.margin + 3 * this.gap + 3 * this.buttonSize, this.buttonSize, this.buttonSize);
      this.equals.setBounds(this.margin + 3 * this.gap + 3 * this.buttonSize, this.margin + 3 * this.gap + 3 * this.buttonSize, this.buttonSize, this.buttonSize);
      this.num0.setBounds(this.margin, this.margin + 4 * this.gap + 4 * this.buttonSize, this.buttonSize, this.buttonSize);
      this.posneg.setBounds(this.margin + this.gap + this.buttonSize, this.margin + 4 * this.gap + 4 * this.buttonSize, this.buttonSize, this.buttonSize);
      this.decimal.setBounds(this.margin + 2 * this.gap + 2 * this.buttonSize, this.margin + 4 * this.gap + 4 * this.buttonSize, this.buttonSize, this.buttonSize);
      this.clear.setBounds(this.margin + 3 * this.gap + 3 * this.buttonSize, this.margin + 4 * this.gap + 4 * this.buttonSize, this.buttonSize, this.buttonSize);
      JPanel var5 = new JPanel(new FlowLayout(1));
      var5.setBackground(this.bgColor);
      JButton var3 = new JButton("OK");
      var3.setName("OK");
      var3.addActionListener(this);
      JButton var4 = new JButton("Mégsem");
      var4.setName("CANCEL");
      var4.addActionListener(this);
      var5.add(var3);
      var5.add(var4);
      this.getContentPane().setLayout(new BorderLayout(this.gap, this.gap));
      this.getContentPane().add(this.display, "North");
      this.getContentPane().add(this.jPanel1, "Center");
      this.getContentPane().add(var5, "South");
      this.getContentPane().setPreferredSize(new Dimension(2 * this.margin + 4 * this.buttonSize + 3 * this.gap + this.gap, 2 * this.margin + 5 * this.buttonSize + 4 * this.gap + 25 + 3 * this.gap + 40));
      this.pack();
   }

   private void decimalActionPerformed() {
      if (this.display.getText().indexOf(".") == -1) {
         this.display.setText(this.display.getText() + this.decimal.getText());
      }

   }

   private void clearActionPerformed() {
      this.display.setText("");
   }

   private void posnegActionPerformed() {
      try {
         this.plusminus = Double.parseDouble(String.valueOf(this.display.getText()));
         this.plusminus *= -1.0D;
         this.display.setText(String.valueOf(this.plusminus));
      } catch (Exception var2) {
      }

   }

   private void operActionPerformed(int var1) {
      this.oper = var1;
      if (this.firstNum != 0.0D) {
         this.equalsActionPerformed(false);
      } else {
         this.firstNum = Double.parseDouble(String.valueOf(this.display.getText()));
         this.display.setText("");
      }
   }

   private void equalsActionPerformed(boolean var1) {
      if (this.status <= 0) {
         try {
            this.secondNum = Double.parseDouble(String.valueOf(this.display.getText()));
         } catch (Exception var3) {
            return;
         }

         switch(this.oper) {
         case 1:
            this.total = this.firstNum + this.secondNum;
            break;
         case 2:
            this.total = this.firstNum - this.secondNum;
            break;
         case 3:
            this.total = this.firstNum * this.secondNum;
            break;
         case 4:
            this.total = this.firstNum / this.secondNum;
         }

         String var2 = String.valueOf(this.total);
         if (var2.endsWith(".0")) {
            var2 = var2.substring(0, var2.length() - 2);
         }

         this.display.setText(var2);
         this.firstNum = var1 ? 0.0D : this.total;
         this.secondNum = 0.0D;
         this.status = 1;
      }
   }

   private void backActionPerformed() {
      int var1 = this.display.getText().length();
      if (var1 != 0) {
         this.display.setText(this.display.getText().substring(0, var1 - 1));
      }
   }

   private void exitActionPerformed(boolean var1) {
      try {
         if (var1) {
            System.out.println(this.total);
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      this.dispose();
   }

   public void actionPerformed(ActionEvent var1) {
      if ("OK".equals(((JButton)var1.getSource()).getName())) {
         this.exitActionPerformed(true);
      } else if ("CANCEL".equals(((JButton)var1.getSource()).getName())) {
         this.exitActionPerformed(false);
      } else {
         if (((JButton)var1.getSource()).getName().startsWith("NUM")) {
            if (this.status > 0) {
               this.display.setText("");
               this.status = 0;
            }

            this.display.setText(this.display.getText() + ((JButton)var1.getSource()).getName().substring(3));
         }

      }
   }

   private class PCKeyEventDispatcher implements KeyEventDispatcher {
      private PCKeyEventDispatcher() {
      }

      public boolean dispatchKeyEvent(KeyEvent var1) {
         if (var1.getID() == 401) {
            int var2 = var1.getKeyCode();
            System.out.println(var1.getKeyCode());
            if (48 <= var2 && var2 <= 57) {
               PocketCalculator.this.display.setText(PocketCalculator.this.display.getText() + (var2 - 48));
               return false;
            }

            if (96 <= var2 && var2 <= 105) {
               PocketCalculator.this.display.setText(PocketCalculator.this.display.getText() + (var2 - 96));
               return false;
            }

            if (var2 == 107) {
               PocketCalculator.this.operActionPerformed(1);
               return false;
            }

            if (var2 == 109) {
               PocketCalculator.this.operActionPerformed(2);
               return false;
            }

            if (var2 == 106) {
               PocketCalculator.this.operActionPerformed(3);
               return false;
            }

            if (var2 == 111) {
               PocketCalculator.this.operActionPerformed(4);
               return false;
            }

            if (var2 == 8) {
               PocketCalculator.this.backActionPerformed();
               return false;
            }

            if (var2 == 67) {
               if (var1.getModifiers() != 2) {
                  PocketCalculator.this.clearActionPerformed();
               }

               return false;
            }

            if (var2 == 80) {
               PocketCalculator.this.posnegActionPerformed();
               return false;
            }

            if (var2 == 77) {
               PocketCalculator.this.posnegActionPerformed();
               return false;
            }

            if (var2 == 10) {
               PocketCalculator.this.equalsActionPerformed(true);
               return false;
            }

            if (var2 == 27) {
               PocketCalculator.this.exitActionPerformed(false);
               return false;
            }
         }

         return false;
      }

      // $FF: synthetic method
      PCKeyEventDispatcher(Object var2) {
         this();
      }
   }
}
