package hu.piller.enykp.gui.viewer.slidercombo;

import hu.piller.enykp.gui.GuiUtil;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.event.ChangeListener;

public class JSliderButton extends JButton {
   private JSliderButton this_;
   private SliderPopup slider_popup;
   private SliderPanel slider_panel;

   public JSliderButton() {
      this.prepare();
   }

   private void prepare() {
      this.this_ = this;
      this.setIcon(SliderIcons.img_down);
      this.setSize(new Dimension(GuiUtil.getW("W"), GuiUtil.getCommonItemHeight() + 2));
      this.setPreferredSize(this.getSize());
      this.setMaximumSize(this.getSize());
      this.setMinimumSize(this.getSize());
      this.slider_panel = new SliderPanel();
      this.slider_popup = SliderPopup.getInstance();
      this.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            JSliderButton.this.this_.toggleSliderPopupVisibility();
         }
      });
   }

   protected void toggleSliderPopupVisibility() {
      if (this.slider_popup.isVisible()) {
         this.slider_popup.hide();
      } else {
         this.slider_popup.show(this, this.slider_panel);
      }

   }

   public void addSliderChangeListener(ChangeListener var1) {
      this.slider_panel.addSliderChangeListener(var1);
   }

   public void removeSliderChangeListener(ChangeListener var1) {
      this.slider_panel.removeSliderChangeListener(var1);
   }

   public SliderPanel getSliderPanel() {
      return this.slider_panel;
   }
}
