package hu.piller.enykp.gui.viewer.pagecounter;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.viewer.slidercombo.JSliderButton;
import hu.piller.enykp.gui.viewer.slidercombo.SliderPanel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class JPageCounter extends JPanel {
   private JPageSpinner spinner;
   private JSliderButton slider_button;
   private JTextField max_count;
   public JButton add_button;
   public JButton remove_button;
   private EventListenerList change_event_listener_list;
   private PageChangeEvent change_event;
   private String event_type;
   private int spinner_old_value;
   private int minimum;
   private int maximum;
   private String optionstr1;
   private String optionstr2;
   private String title;
   private String msg1;
   private String msg2;

   public JPageCounter() {
      this.minimum = -1;
      this.maximum = -1;
      this.optionstr1 = "Igen";
      this.optionstr2 = "Nem";
      this.title = "Lap törlése";
      this.msg1 = "A törléssel a lapon szereplő összes adat elveszik.";
      this.msg2 = "Biztos folytatja?";
      this.change_event_listener_list = new EventListenerList();
      this.build();
      this.prepare();
   }

   public JPageCounter(int var1) {
      this();
      this.setSpinnerValue(new Integer(var1));
   }

   private void build() {
      this.add_button = new JButton();
      this.add_button.setName("dynplus");
      this.add_button.setToolTipText("Lap hozzáadása");
      this.remove_button = new JButton();
      this.remove_button.setName("dynminus");
      this.remove_button.setToolTipText("Lap törlése");
      this.slider_button = new JSliderButton();
      this.spinner = new JPageSpinner();
      this.spinner.setName("dynspinner");
      this.spinner.setToolTipText("Aktuális lap: " + this.spinner.getValue());
      this.max_count = new JTextField();
      this.add_button.setIcon(CounterIcons.img_plus);
      this.remove_button.setIcon(CounterIcons.img_minus);
      boolean var1 = true;
      boolean var2 = true;
      int var3 = GuiUtil.getW("WWWWW/WWWWW") + 10;
      int var4 = GuiUtil.getW("WWWWW") + 10;
      this.add_button.setMaximumSize(new Dimension(this.add_button.getIcon().getIconWidth() + 4, GuiUtil.getCommonItemHeight() + 2));
      this.remove_button.setMaximumSize(new Dimension(this.remove_button.getIcon().getIconWidth() + 4, GuiUtil.getCommonItemHeight() + 2));
      this.slider_button.setMaximumSize(new Dimension(20, GuiUtil.getCommonItemHeight() + 2));
      this.spinner.setMaximumSize(new Dimension(var4, GuiUtil.getCommonItemHeight() + 2));
      this.max_count.setEditable(false);
      this.max_count.setBorder(BorderFactory.createEtchedBorder());
      this.max_count.setMinimumSize(new Dimension(var3, GuiUtil.getCommonItemHeight() + 2));
      this.max_count.setMaximumSize(new Dimension(var3, GuiUtil.getCommonItemHeight() + 2));
      this.max_count.setPreferredSize(new Dimension(var3, GuiUtil.getCommonItemHeight() + 2));
      this.add_button.setMinimumSize(this.add_button.getMaximumSize());
      this.remove_button.setMinimumSize(this.remove_button.getMaximumSize());
      this.slider_button.setMinimumSize(new Dimension(20, GuiUtil.getCommonItemHeight() + 2));
      this.add_button.setMargin(new Insets(0, 0, 0, 0));
      this.remove_button.setMargin(new Insets(0, 0, 0, 0));
      this.slider_button.setMargin(new Insets(0, 0, 0, 0));
      this.add_button.setPreferredSize(this.add_button.getMaximumSize());
      this.remove_button.setPreferredSize(this.remove_button.getMaximumSize());
      this.spinner.setPreferredSize(new Dimension(var4, GuiUtil.getCommonItemHeight() + 2));
      GridBagConstraints var5 = new GridBagConstraints();
      this.setLayout(new GridBagLayout());
      var5.gridheight = 1;
      var5.gridwidth = 1;
      var5.gridy = 0;
      var5.gridx = 0;
      var5.fill = 1;
      this.add(this.add_button, var5);
      var5.gridx = 1;
      this.add(this.remove_button, var5);
      var5.gridx = 2;
      this.add(this.spinner, var5);
      var5.gridx = 3;
      this.add(this.max_count, var5);
      var5.gridx = 4;
      var5.weightx = 0.0D;
      this.add(this.slider_button, var5);
      this.slider_button.getSliderPanel().setSliderValue(1);
      this.slider_button.getSliderPanel().setSliderMinimum(1);
      this.slider_button.getSliderPanel().setSliderMaximum(1);
      NumberEditor var6 = (NumberEditor)this.spinner.getEditor();
      final JFormattedTextField var7 = var6.getTextField();
      var7.addKeyListener(new KeyListener() {
         public void keyPressed(KeyEvent var1) {
            switch(var1.getKeyCode()) {
            case 8:
            case 127:
               if (var7.getText().length() > 1) {
                  break;
               }

               var1.setKeyCode(0);
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 38:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            case 115:
            case 116:
            case 117:
            case 118:
            case 119:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            default:
               var1.consume();
            case 9:
            case 10:
            case 27:
            case 37:
            case 39:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            }

         }

         public void keyReleased(KeyEvent var1) {
         }

         public void keyTyped(KeyEvent var1) {
         }
      });
   }

   private void prepare() {
      this.slider_button.addSliderChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent var1) {
            JPageCounter.this.spinner.setValue(new Integer(JPageCounter.this.slider_button.getSliderPanel().getSliderValue()));
         }
      });
      this.slider_button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Object var2 = JPageCounter.this.spinner.getValue();
            if (var2 instanceof Number) {
               JPageCounter.this.slider_button.getSliderPanel().setSliderValue(((Number)var2).intValue());
            }

         }
      });
      this.spinner.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent var1) {
            Object var2 = JPageCounter.this.spinner.getValue();
            if (var2 instanceof Number) {
               int var3 = ((Number)var2).intValue();
               SliderPanel var4 = JPageCounter.this.slider_button.getSliderPanel();
               int var5 = var4.getSliderMaximum();
               if (var3 > var5) {
                  var4.setSliderValue(var5);
                  JPageCounter.this.spinner.setValue(new Integer(var5));
               } else if (var3 >= 1) {
                  var4.setSliderValue(var3);
                  if (JPageCounter.this.event_type == null) {
                     JPageCounter.this.event_type = "change_page";
                     JPageCounter.this.spinner_old_value = -1;
                  }

                  JPageCounter.this.fireSpinnerChange();
               } else {
                  JPageCounter.this.spinner.setValue(new Integer(1));
               }

               JPageCounter.this.spinner.setToolTipText("Aktuális lap: " + JPageCounter.this.spinner.getValue());
            }

         }
      });
      this.add_button.setFocusable(false);
      this.add_button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (JPageCounter.this.check()) {
               JPageCounter.this.addPG();
            }

         }
      });
      this.remove_button.setFocusable(false);
      this.remove_button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (JPageCounter.this.check()) {
               JPageCounter.this.removePG();
            }

         }
      });
      this.slider_button.setFocusable(false);
      this.spinner.setFocusable(false);
      this.spinner.setRequestFocusEnabled(false);
      this.spinner.setValue(new Integer(1));
      this.showMaximum(1);
   }

   public void addSpinnerChangeListener(ChangeListener var1) {
      this.change_event_listener_list.add(ChangeListener.class, var1);
   }

   public void removeSpinnerChangeListener(ChangeListener var1) {
      this.change_event_listener_list.remove(ChangeListener.class, var1);
   }

   protected void fireSpinnerChange() {
      Object[] var1 = this.change_event_listener_list.getListenerList();

      for(int var2 = var1.length - 2; var2 >= 0; var2 -= 2) {
         if (var1[var2] == ChangeListener.class) {
            if (this.change_event == null) {
               this.change_event = new PageChangeEvent(this.spinner);
            }

            this.change_event.setEventType(this.event_type);
            this.change_event.setOldValue(this.spinner_old_value);
            ((ChangeListener)var1[var2 + 1]).stateChanged(this.change_event);
         }
      }

      this.event_type = null;
      this.spinner_old_value = 0;
   }

   public void setSpinnerValue(Object var1) {
      this.spinner.setValue(var1);
   }

   public Object getSpinnerValue() {
      return this.spinner.getValue();
   }

   public void addPage() {
      this.addPG();
   }

   public void removePage() {
      this.removePG();
   }

   protected void setSliderMaximum(int var1) {
      SliderPanel var2 = this.slider_button.getSliderPanel();
      if (var2.getSliderMaximum() != var1) {
         var2.setSliderMaximum(var1);
         this.showMaximum(var1);
         this.spinner.fireStateChanged_();
      }

   }

   protected int getSliderMaximum() {
      return this.slider_button.getSliderPanel().getSliderMaximum();
   }

   protected void setSliderMinimum(int var1) {
      SliderPanel var2 = this.slider_button.getSliderPanel();
      if (var2.getSliderMinimum() != var1) {
         var2.setSliderMinimum(var1);
         this.spinner.fireStateChanged_();
      }

   }

   protected int getSliderMinimum() {
      return this.slider_button.getSliderPanel().getSliderMinimum();
   }

   public void setMaximum(int var1) {
      if (this.maximum != var1) {
         this.maximum = var1;
         if (var1 > -1 && this.getValue() > var1) {
            this.setValue(var1);
         }

         this.showMaximum(this.getValue());
      }

   }

   public int getMaximum() {
      return this.maximum;
   }

   public void setMinimum(int var1) {
      if (this.minimum != var1) {
         this.minimum = var1;
         if (var1 > -1 && this.getValue() < var1) {
            this.setValue(var1);
         }
      }

   }

   public int getMinimum() {
      return this.minimum;
   }

   public void setValue(int var1) {
      this.setSliderMaximum(var1);
   }

   public int getValue() {
      return this.getSliderMaximum();
   }

   public void setReadonly(boolean var1) {
      if (this.add_button != null) {
         this.add_button.setEnabled(var1);
      }

      if (this.remove_button != null) {
         this.remove_button.setEnabled(var1);
      }

   }

   private void addPG() {
      if (this.addPGCheck()) {
         SliderPanel var1 = this.slider_button.getSliderPanel();
         int var2 = var1.getSliderMaximum();
         int var3 = (Integer)this.spinner.getValue();
         this.event_type = "new_page";
         this.spinner_old_value = var3;
         ++var2;
         var1.setSliderMaximum(var2);
         this.showMaximum(var2);
         this.spinner.setValue(new Integer(var3 + 1));
      }

   }

   private void removePG() {
      if (this.removePGCheck()) {
         SliderPanel var1 = this.slider_button.getSliderPanel();
         int var2 = var1.getSliderMaximum();
         if (var2 > 1) {
            Object[] var3 = new Object[]{this.optionstr1, this.optionstr2};
            int var4 = JOptionPane.showOptionDialog(MainFrame.thisinstance, this.msg1 + "\n" + this.msg2, this.title, 0, 3, (Icon)null, var3, var3[0]);
            if (var4 != 0) {
               return;
            }

            int var5 = var1.getSliderValue();
            this.event_type = "delete_page";
            this.spinner_old_value = var5;
            --var2;
            if (var5 > var2) {
               var5 = var2;
            }

            var1.setSliderMaximum(var2);
            this.showMaximum(var2);
            var1.setSliderValue(var5);
            this.spinner.fireStateChanged_();
            this.spinner.setValue(new Integer(var5));
         }
      }

   }

   private void showMaximum(int var1) {
      if (this.maximum > -1) {
         this.max_count.setText("/" + var1 + " /" + this.maximum);
      } else {
         this.max_count.setText("/" + var1);
      }

   }

   private boolean addPGCheck() {
      int var1 = this.getMaximum();
      return var1 <= -1 || this.getValue() + 1 <= var1;
   }

   private boolean removePGCheck() {
      int var1 = this.getMinimum();
      return var1 <= -1 || this.getValue() - 1 >= var1;
   }

   private boolean check() {
      return true;
   }

   public int spinnerUp() {
      try {
         int var1 = (Integer)this.getSpinnerValue();
         if (var1 < this.maximum) {
            this.setSpinnerValue(var1 + 1);
            this.spinner.fireStateChanged_();
            return var1 + 1;
         }
      } catch (Exception var2) {
      }

      return -1;
   }

   public int spinnerDown() {
      try {
         int var1 = (Integer)this.getSpinnerValue();
         if (var1 > this.minimum) {
            this.setSpinnerValue(var1 - 1);
            this.spinner.fireStateChanged_();
            return var1 - 1;
         }
      } catch (Exception var2) {
      }

      return -1;
   }
}
