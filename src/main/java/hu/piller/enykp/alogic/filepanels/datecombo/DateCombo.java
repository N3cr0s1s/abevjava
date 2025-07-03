package hu.piller.enykp.alogic.filepanels.datecombo;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

public class DateCombo extends JPanel {
   private static final long serialVersionUID = 1L;
   private DatePopup date_popup;
   private JFormattedTextField txt_editor = null;
   private JButton btn_button = null;

   private void initialize() {
      this.setLayout(new BoxLayout(this, 0));
      this.setSize(new Dimension(274, 52));
      this.add(this.getTxt_editor(), (Object)null);
      this.add(this.getBtn_button(), (Object)null);
   }

   public boolean isPopupVisible() {
      return this.date_popup.isVisible();
   }

   public void setPopupVisible(JComponent var1, boolean var2) {
      if (this.date_popup.isVisible() != var2) {
         this.date_popup.setVisible(var2);
         if (this.date_popup.isVisible()) {
            this.date_popup.show(var1, 0, var1.getHeight());
         } else {
            this.date_popup.setVisible(false);
         }

      }
   }

   public DateCombo() {
      this.initialize();
      this.prepare();
   }

   private void prepare() {
      this.date_popup = new DatePopup(this);
      this.date_popup.addActionListener(new ActionListener() {
         private final String ACT_KEY = "DateChangedTo:";

         public void actionPerformed(ActionEvent var1) {
            String var2 = var1.getActionCommand();
            if (var2.startsWith("DateChangedTo:")) {
               DateCombo.this.txt_editor.setText(var2.substring("DateChangedTo:".length()));
               DateCombo.this.txt_editor.setValue(DateCombo.this.txt_editor.getText());
            }

         }
      });
      this.btn_button.addMouseListener(new MouseListener() {
         private boolean popup_visible;

         public void mouseClicked(MouseEvent var1) {
         }

         public void mouseEntered(MouseEvent var1) {
         }

         public void mouseExited(MouseEvent var1) {
         }

         public void mousePressed(MouseEvent var1) {
            this.popup_visible = DateCombo.this.isPopupVisible();
         }

         public void mouseReleased(MouseEvent var1) {
            DateCombo.this.setPopupVisible(DateCombo.this, !this.popup_visible);
         }
      });
      this.txt_editor.addComponentListener(new ComponentListener() {
         public void componentHidden(ComponentEvent var1) {
         }

         public void componentMoved(ComponentEvent var1) {
         }

         public void componentResized(ComponentEvent var1) {
            Rectangle var2 = DateCombo.this.btn_button.getBounds();
            var2.y = 0;
            var2.height = DateCombo.this.txt_editor.getHeight();
            DateCombo.this.btn_button.setBounds(var2);
         }

         public void componentShown(ComponentEvent var1) {
         }
      });
   }

   private JFormattedTextField getTxt_editor() {
      if (this.txt_editor == null) {
         try {
            MaskFormatter var1 = new MaskFormatter("****.**.**");
            var1.setValidCharacters("0123456789 ");
            var1.setPlaceholderCharacter(' ');
            this.txt_editor = new JFormattedTextField(var1);
            var1.install(this.txt_editor);
         } catch (ParseException var3) {
            if (this.txt_editor == null) {
               this.txt_editor = new JFormattedTextField();
            }
         }

         this.txt_editor.setInputVerifier(new InputVerifier() {
            public boolean verify(JComponent var1) {
               return true;
            }
         });
      }

      return this.txt_editor;
   }

   private JButton getBtn_button() {
      if (this.btn_button == null) {
         this.btn_button = new JButton();
         this.btn_button.setMargin(new Insets(4, 1, 4, 1));
         this.btn_button.setIcon(new DownArrow(5));
      }

      return this.btn_button;
   }

   public void setText(String var1) {
      this.txt_editor.setText(var1);
      this.txt_editor.setValue(this.txt_editor.getText());
   }

   public String getText() {
      return this.txt_editor.getText();
   }

   public JFormattedTextField getEditor() {
      return this.txt_editor;
   }
}
