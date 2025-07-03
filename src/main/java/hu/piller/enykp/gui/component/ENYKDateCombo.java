package hu.piller.enykp.gui.component;

import hu.piller.enykp.util.base.Tools;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.text.MaskFormatter;

public class ENYKDateCombo extends ENYKFormattedTaggedTextField implements ActionListener, FocusListener {
   private DatumValaszto dv = new DatumValaszto(this);
   public String elvalaszto = ".";
   public String datumFormatum = "yyyy.MM.dd";
   public static String DEFAULT_DATE_MASK = "####.##.#####";
   public static SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyyMMdd");
   private String eleje;
   private int triangle_ab = 7;
   protected boolean is_printing;
   private int[] tr_x = new int[5];
   private int[] tr_y = new int[5];

   public ENYKDateCombo() {
      this.initPanel();
      this.feature_abev_mask = DEFAULT_DATE_MASK;
      this.initFeatures();
   }

   public void print(Graphics var1) {
      this.is_printing = true;
      super.print(var1);
      this.is_printing = false;
   }

   public void paint(Graphics var1) {
      super.paint(var1);
      this.paintTriangle(var1);
   }

   public void release() {
      super.release();
      this.tr_x = null;
      this.tr_y = null;
   }

   public void setValue(Object var1) {
      String var2 = null;

      try {
         var2 = var1.toString();
         if (this.checkIfRightDate(this.eleje, var2)) {
            var2 = var2.substring(0, 4) + this.elvalaszto + var2.substring(4, 6) + this.elvalaszto + var2.substring(6);
         } else {
            var2 = "";
         }
      } catch (Exception var4) {
         Tools.eLog(var4, 0);
      }

      super.setENYKValue(var2 == null ? null : var2.toString(), false);
   }

   private boolean checkIfRightDate(String var1, String var2) {
      int var3 = Math.max(var1.indexOf("#"), var1.length());
      var1 = var1.substring(0, var3);
      return var2.startsWith(var1);
   }

   private void paintTriangle(Graphics var1) {
      if (!this.is_printing && this.isEnabled()) {
         int var5;
         int var6;
         if (this.getBorder() != null) {
            Insets var7 = this.getBorder().getBorderInsets(this);
            if (var7 != null) {
               var5 = var7.right;
               var6 = var7.top;
            } else {
               var5 = 0;
               var6 = 0;
            }
         } else {
            var5 = 0;
            var6 = 0;
         }

         int var4 = (int)(this.zoom_f * (double)this.triangle_ab);
         int var2 = this.graphics_bounds.width - var4 - var5;
         int var3 = this.graphics_bounds.y + var6;
         this.tr_x[0] = var2;
         this.tr_y[0] = var3;
         this.tr_x[1] = var2 + var4;
         this.tr_y[1] = var3;
         this.tr_x[2] = var2 + var4;
         this.tr_y[2] = var3 + var4;
         var1.setColor(Color.RED);
         var1.fillPolygon(this.tr_x, this.tr_y, 3);
      }
   }

   protected void initFeatures() {
      super.initFeatures();
      this.setHorizontalAlignment(10);
      if (this.feature_abev_mask != null) {
         if (this.feature_abev_mask.indexOf(this.elvalaszto) == -1) {
            this.elvalaszto = "";
         }

         String var1 = this.feature_abev_mask;
         if (this.isValidMask(var1)) {
            int var3 = var1.indexOf(35);
            String var2 = "";
            if (var3 > 0) {
               var2 = var1.substring(0, var3);

               try {
                  Integer.parseInt(var2);
               } catch (NumberFormatException var5) {
                  var2 = var2.substring(0, var2.length() - 1);
               }
            }

            this.eleje = var2;
            this.applyEleje();
            this.findSeparator(var1);
         }
      }

   }

   private boolean isValidMask(String var1) {
      return var1.length() <= 11;
   }

   protected void applyMask(int var1) {
      ENYKFormattedTaggedTextField.applayMask(this.feature_abev_mask, this, var1);
      AbstractFormatter var2 = this.getFormatter();
      if (var2 instanceof ENYKMaskFormatter) {
         ((ENYKMaskFormatter)var2).setValueContainsLiteralCharacters(true);
      } else if (var2 instanceof MaskFormatter) {
         ((MaskFormatter)var2).setValueContainsLiteralCharacters(true);
      }

   }

   public void setMezoSzoveg(String var1) {
      this.setText(var1.substring(this.eleje.length()));
   }

   private void applyEleje() {
      if (this.eleje != null) {
         this.eleje = this.eleje.trim();
         Calendar var2;
         if (!this.eleje.equals("")) {
            Calendar var1 = Calendar.getInstance();
            if (this.eleje.length() <= 4) {
               var1.set(1, Integer.parseInt((this.eleje + "000").substring(0, 4)));
               var1.set(2, 0);
               var1.set(5, 1);
            } else if (this.eleje.length() <= 7) {
               var1.set(1, Integer.parseInt(this.eleje.substring(0, 4)));
               var1.set(2, Integer.parseInt((this.eleje + "01").substring(5, 7)) - 1);
               var1.set(5, 1);
            } else {
               var1.set(1, Integer.parseInt(this.eleje.substring(0, 4)));
               var1.set(2, Integer.parseInt(this.eleje.substring(5, 7)) - 1);
               var1.set(5, Integer.parseInt((this.eleje + "01").substring(8, 10)));
            }

            this.dv.setKezdodatum(var1);
            var2 = Calendar.getInstance();
            if (this.eleje.length() <= 4) {
               var2.set(1, Integer.parseInt((this.eleje + "999").substring(0, 4)));
               var2.set(2, 11);
               var2.set(5, 31);
            } else if (this.eleje.length() <= 7) {
               var2.set(1, Integer.parseInt(this.eleje.substring(0, 4)));
               var2.set(2, Integer.parseInt((this.eleje + "99").substring(5, 7)) - 1);
               var2.set(5, 1);
            } else {
               var2.set(1, Integer.parseInt(this.eleje.substring(0, 4)));
               var2.set(2, Integer.parseInt(this.eleje.substring(5, 7)) - 1);
               var2.set(5, Integer.parseInt((this.eleje + "99").substring(8, 10)));
            }

            this.dv.setVegedatum(var2);
            if (!Calendar.getInstance().after(var1)) {
               this.dv.setDatum(var1);
            } else if (!Calendar.getInstance().before(var2)) {
               this.dv.setDatum(var2);
            }
         } else {
            try {
               Date var6 = defaultDateFormat.parse(this.dv.minyear + "0101");
               var2 = Calendar.getInstance();
               var2.setTime(var6);
               this.dv.setKezdodatum(var2);
               Calendar var3 = Calendar.getInstance();
               Date var4 = defaultDateFormat.parse(this.dv.maxyear + "1230");
               var3.setTime(var4);
               this.dv.setVegedatum(var3);
            } catch (ParseException var5) {
               Tools.eLog(var5, 0);
            }
         }
      }

   }

   private void initPanel() {
      this.setLayout((LayoutManager)null);
      this.setPreferredSize(new Dimension(121, 20));
      this.setBounds(0, 0, 100, 20);
      this.addFocusListener(this);
      this.setBounds(101, 0, 20, 20);
      this.addActionListener(this);
      this.dv.addActionListener(this);
      this.applyEleje();
      this.addMouseListener(new MouseListener() {
         public void mouseClicked(MouseEvent var1) {
            if (var1.getSource() == ENYKDateCombo.this && var1.getClickCount() == 2) {
               if (!((ENYKDateCombo)var1.getSource()).isEnabled()) {
                  return;
               }

               if (!ENYKDateCombo.this.isFocusOwner()) {
                  return;
               }

               ENYKDateCombo.this.applyEleje();
               Calendar var2 = Calendar.getInstance();

               try {
                  Date var3 = ENYKDateCombo.this.checkDate().d;
                  var2.setTime(var3);
                  ENYKDateCombo.this.dv.setDatum(var2);
               } catch (ParseException var4) {
                  ENYKDateCombo.this.dv.setDatum(var2);
               }

               ENYKDateCombo.this.dv.show(ENYKDateCombo.this, 0, ENYKDateCombo.this.getBounds().height);
            }

         }

         public void mouseEntered(MouseEvent var1) {
         }

         public void mouseExited(MouseEvent var1) {
         }

         public void mousePressed(MouseEvent var1) {
         }

         public void mouseReleased(MouseEvent var1) {
         }
      });
      this.setVisible(true);
   }

   public void actionPerformed(ActionEvent var1) {
      if (var1.getActionCommand().startsWith("DateChangedTo:")) {
         try {
            String var2 = var1.getActionCommand().substring(14);
            var2 = var2.substring(0, 4) + this.elvalaszto + var2.substring(4, 6) + this.elvalaszto + var2.substring(6, 8);
            this.setENYKValue(var2 == null ? null : var2.toString(), false);
         } catch (Exception var3) {
            Tools.eLog(var3, 0);
         }
      }

   }

   public void focusGained(FocusEvent var1) {
      if (var1.getSource() == this && this.getInputVerifier() == null) {
      }

   }

   public void focusLost(FocusEvent var1) {
      try {
         this.checkDate();
      } catch (ParseException var3) {
         this.setValue("");
      }

   }

   private void findSeparator(String var1) {
      for(int var2 = 0; var2 < var1.length(); ++var2) {
         if (var1.charAt(var2) != '#') {
            try {
               Integer.parseInt("" + var1.charAt(var2));
            } catch (NumberFormatException var4) {
               this.elvalaszto = "" + var1.charAt(var2);
               return;
            }
         }
      }

   }

   public Object getFieldValue() {
      try {
         String var1 = "";

         try {
            var1 = this.checkDate().s;
         } catch (ParseException var3) {
            return "";
         }

         return var1;
      } catch (Exception var4) {
         return super.getValue();
      }
   }

   private ENYKDateCombo.DateAndString checkDate() throws ParseException {
      String var1 = this.getText();
      var1 = var1.replaceAll("\\.", "");
      var1 = var1.replaceAll("-", "");
      String var3 = this.feature_abev_mask;
      var3 = var3.replaceAll("\\.", "");
      var3 = var3.replaceAll("-", "");
      SimpleDateFormat var2;
      if (var3.length() == 4) {
         var2 = new SimpleDateFormat("yyyy");
      } else if (var3.length() == 6) {
         var2 = new SimpleDateFormat("yyyyMM");
      } else {
         var2 = new SimpleDateFormat("yyyyMMdd");
         if (var1.trim().length() < 8) {
            throw new ParseException("", 0);
         }
      }

      Date var4 = var2.parse(var1);
      if (!var2.format(var4).equals(var1)) {
         throw new ParseException("", 1);
      } else {
         ENYKDateCombo.DateAndString var5 = new ENYKDateCombo.DateAndString(var4, var1);
         return var5;
      }
   }

   private class DateAndString {
      public Date d;
      public String s;

      public DateAndString(Date var2, String var3) {
         this.d = var2;
         this.s = var3;
      }
   }
}
