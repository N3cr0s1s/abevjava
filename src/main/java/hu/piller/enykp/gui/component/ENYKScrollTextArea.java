package hu.piller.enykp.gui.component;

import hu.piller.enykp.interfaces.IDataField;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Hashtable;
import javax.swing.JScrollPane;

public class ENYKScrollTextArea extends JScrollPane implements IDataField {
   ENYKTextArea ta = new ENYKTextArea();
   ENYKTextArea jta;
   boolean painter;
   boolean readOnly = false;
   Object resize_font_sync = new Object();

   public ENYKScrollTextArea() {
      this.ta.setTextLimitationAllowed(false);
      this.ta.setEditable(false);
      this.setViewportView(this.ta);
      this.addFocusListener(new FocusAdapter() {
         public void focusGained(FocusEvent var1) {
            ENYKScrollTextArea.this.ta.requestFocusInWindow();
         }
      });
      this.jta = new ENYKTextArea();
      this.jta.setTextLimitationAllowed(false);
      this.jta.setLineWrap(true);
      this.jta.setWrapStyleWord(true);
      this.jta.setFont(this.ta.getFont());
   }

   public void setValue(Object var1) {
      this.ta.setValue(var1);
   }

   public void setFeatures(Hashtable var1) {
      Boolean var2 = (Boolean)var1.get("_painter_");
      this.painter = var2;
      this.ta.setFeatures(var1);
      if (var1.get("readonlymode") != null) {
         this.readOnly = (Boolean)var1.get("readonlymode");
         var1.remove("readonlymode");
      }

      if (this.readOnly) {
         this.ta.setEditable(false);
      } else {
         this.jta.setFeatures(var1);
         String var3 = (String)var1.get("editable");
         if ("true".equalsIgnoreCase(var3)) {
            this.ta.setEditable(true);
         } else {
            this.ta.setEditable(false);
         }
      }

   }

   public Object getFieldValue() {
      return this.ta.getFieldValue();
   }

   public Object getFieldValueWOMask() {
      return this.getFieldValue();
   }

   public void setZoom(int var1) {
      synchronized(this.resize_font_sync) {
         Font var3 = (Font)this.ta.features.get("font");
         var3 = var3.deriveFont((float)var1 / 100.0F * var3.getSize2D());
         var3 = var3.getSize() > 5 ? var3 : var3.deriveFont(5.0F);
         this.ta.setFont(var3);
         this.jta.setFont(this.ta.getFont());
      }

      this.invalidate();
      this.repaint();
   }

   public int getRecordIndex() {
      return -1;
   }

   public void paint(Graphics var1) {
      if (this.painter) {
         this.jta.setBounds(0, 0, this.getBounds().width, this.getBounds().height);
         this.jta.setText(this.ta.getText());
         Rectangle var2 = var1.getClipBounds();
         var1.setClip(var2.intersection(new Rectangle(0, 0, this.getBounds().width, this.getBounds().height)));
         this.jta.paint(var1);
         var1.setClip(var2);
      } else {
         super.paint(var1);
      }
   }

   public void print(Graphics var1) {
      this.jta.setBounds(0, 0, this.getBounds().width, this.getBounds().height);
      this.jta.setText(this.ta.getText());
      Rectangle var2 = var1.getClipBounds();
      if (var2 != null) {
         var1.setClip(var2.intersection(new Rectangle(0, 0, this.getBounds().width, this.getBounds().height)));
      } else {
         var1.setClip(new Rectangle(0, 0, this.getBounds().width, this.getBounds().height));
      }

      this.jta.print(var1);
      var1.setClip(var2);
   }
}
