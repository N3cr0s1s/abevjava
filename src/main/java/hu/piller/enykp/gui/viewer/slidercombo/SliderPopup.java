package hu.piller.enykp.gui.viewer.slidercombo;

import java.awt.AWTEvent;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

public class SliderPopup {
   private static SliderPopup popup;
   private JPopupMenu popupMenu;
   private SliderPanel slider_panel;
   private ActionListener ok_button_action_listener = new ActionListener() {
      public void actionPerformed(ActionEvent var1) {
         SliderPopup.this.fireSliderChangeAndHide();
      }
   };

   private SliderPopup() {
      Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
         public void eventDispatched(AWTEvent var1) {
            if (SliderPopup.this.slider_panel != null) {
               if (var1.getID() == 500 && SliderPopup.this.slider_panel.isSlider(var1.getSource())) {
                  MouseEvent var2 = (MouseEvent)var1;
                  if (var2.getClickCount() == 2) {
                     SliderPopup.this.fireSliderChangeAndHide();
                  }
               }

            }
         }
      }, 16L);
      this.popupMenu = new JPopupMenu();
      this.installKeyBindings(this.popupMenu);
   }

   public static SliderPopup getInstance() {
      if (popup == null) {
         popup = new SliderPopup();
      }

      return popup;
   }

   public boolean isVisible() {
      return this.popupMenu == null ? false : this.popupMenu.isVisible();
   }

   public void show(JButton var1, SliderPanel var2) {
      this.detachPanel();
      this.slider_panel = var2;
      this.attachPanel();
      this.popupMenu.show(var1, 0, var1.getSize().height);
      KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(this.popupMenu);
   }

   public void hide() {
      if (this.popupMenu != null) {
         this.popupMenu.setVisible(false);
         this.detachPanel();
      }

   }

   private void attachPanel() {
      if (this.slider_panel != null) {
         this.slider_panel.addOkButtonActionListener(this.ok_button_action_listener);
         if (this.popupMenu != null) {
            this.popupMenu.add(this.slider_panel);
         }
      }

   }

   private void detachPanel() {
      if (this.slider_panel != null) {
         if (this.popupMenu != null) {
            this.popupMenu.remove(this.slider_panel);
         }

         this.slider_panel.removeOkButtonActionListener(this.ok_button_action_listener);
         this.slider_panel = null;
      }

   }

   private void installKeyBindings(JComponent var1) {
      this.installKeyBinding(var1, "ENTER", "commitPosition", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            SliderPopup.this.fireSliderChangeAndHide();
         }
      });
      this.installKeyBinding(var1, "ESCAPE", "cancelPosition", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            SliderPopup.this.hide();
         }
      });
   }

   private void installKeyBinding(JComponent var1, String var2, String var3, Action var4) {
      var1.getInputMap(1).put(KeyStroke.getKeyStroke(var2), var3);
      var1.getActionMap().put(var3, var4);
   }

   private void fireSliderChangeAndHide() {
      this.popupMenu.setVisible(false);
      if (this.slider_panel != null) {
         this.slider_panel.fireSliderChange();
      }

      this.detachPanel();
   }
}
