package hu.piller.enykp.gui.viewer.slidercombo;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.util.icon.ENYKIconSet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class SliderPanel extends JPanel {
   private JSlider slider;
   private JButton ok_button;
   private JButton first_button;
   private JButton last_button;
   private JTextField counter;
   private EventListenerList change_event_listener_list;
   private ChangeEvent change_event;

   public SliderPanel() {
      this.build();
      this.prepare();
   }

   private void build() {
      JPanel var1 = new JPanel(new BorderLayout());
      var1.setPreferredSize(new Dimension(6 * GuiUtil.getCommonItemHeight(), (int)(0.8D * (double)GuiUtil.getCommonItemHeight())));
      this.slider = new JSlider() {
         public void updateUI() {
            this.setUI(new SizeableSliderUI(this));
         }
      };
      var1.add(this.slider);
      this.slider.setSnapToTicks(true);
      this.slider.setMajorTickSpacing(1);
      this.slider.setPaintTicks(true);
      ENYKIconSet var2 = ENYKIconSet.getInstance();
      this.ok_button = new JButton(var2.get("anyk_ellenorzes"));
      Dimension var3 = new Dimension(this.ok_button.getIcon().getIconWidth() + 2, this.ok_button.getIcon().getIconHeight() + 2);
      this.ok_button.setSize(var3);
      this.ok_button.setPreferredSize(this.ok_button.getSize());
      this.first_button = new JButton(var2.get("page_tobb_lapozas_balra"));
      this.first_button.setSize(var3);
      this.first_button.setPreferredSize(this.ok_button.getSize());
      this.last_button = new JButton(var2.get("page_tobb_lapozas_jobbra"));
      this.last_button.setSize(var3);
      this.last_button.setPreferredSize(this.ok_button.getSize());
      this.counter = new JTextField();
      this.counter.setSize(new Dimension(GuiUtil.getW("WWW"), GuiUtil.getCommonItemHeight() + 4));
      this.counter.setMinimumSize(this.counter.getSize());
      this.counter.setPreferredSize(this.counter.getSize());
      this.counter.setEditable(false);
      this.counter.setFocusable(false);
      this.counter.setBorder(BorderFactory.createEtchedBorder(1));
      this.counter.setHorizontalAlignment(4);
      this.first_button.setMargin(new Insets(0, 0, 0, 0));
      this.last_button.setMargin(new Insets(0, 0, 0, 0));
      this.ok_button.setMargin(new Insets(0, 0, 0, 0));
      this.setBorder(new EmptyBorder(1, 1, 1, 1));
      this.setLayout(new FlowLayout(0));
      this.add(this.first_button);
      this.add(this.last_button);
      this.add(this.counter);
      this.add(this.ok_button);
      this.add(var1);
   }

   private void prepare() {
      this.change_event_listener_list = new EventListenerList();
      this.first_button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            SliderPanel.this.slider.setValue(SliderPanel.this.slider.getMinimum());
         }
      });
      this.last_button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            SliderPanel.this.slider.setValue(SliderPanel.this.slider.getMaximum());
         }
      });
      this.slider.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent var1) {
            SliderPanel.this.counter.setText("" + SliderPanel.this.slider.getValue());
         }
      });
   }

   public void addOkButtonActionListener(ActionListener var1) {
      this.ok_button.addActionListener(var1);
   }

   public void removeOkButtonActionListener(ActionListener var1) {
      this.ok_button.removeActionListener(var1);
   }

   public int getSliderValue() {
      return this.slider.getValue();
   }

   public int getSliderMaximum() {
      return this.slider.getMaximum();
   }

   public int getSliderMinimum() {
      return this.slider.getMinimum();
   }

   public void setSliderValue(int var1) {
      this.slider.setValue(var1);
   }

   public void setSliderMaximum(int var1) {
      this.slider.setMaximum(var1);
   }

   public void setSliderMinimum(int var1) {
      this.slider.setMinimum(var1);
   }

   public void addSliderChangeListener(ChangeListener var1) {
      this.change_event_listener_list.add(ChangeListener.class, var1);
   }

   public void removeSliderChangeListener(ChangeListener var1) {
      this.change_event_listener_list.remove(ChangeListener.class, var1);
   }

   public boolean isSlider(Object var1) {
      return this.slider == var1;
   }

   public void fireSliderChange() {
      Object[] var1 = this.change_event_listener_list.getListenerList();

      for(int var2 = var1.length - 2; var2 >= 0; var2 -= 2) {
         if (var1[var2] == ChangeListener.class) {
            if (this.change_event == null) {
               this.change_event = new ChangeEvent(this);
            }

            ((ChangeListener)var1[var2 + 1]).stateChanged(this.change_event);
         }
      }

   }
}
