package hu.piller.enykp.alogic.masterdata.envelope.gui;

import hu.piller.enykp.alogic.masterdata.envelope.Envelope;
import hu.piller.enykp.alogic.masterdata.envelope.painter.EnvelopePainter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;

public class EnvelopePreviewPanel extends JPanel {
   Envelope envelope;
   EnvelopePainter painter;

   public EnvelopePreviewPanel(Envelope var1) {
      this.envelope = var1;
      this.painter = new EnvelopePainter();
      this.setBorder(new EnvelopePreviewPanel.EnvelopePreviewBorder());
   }

   protected void paintComponent(Graphics var1) {
      Graphics2D var2 = (Graphics2D)var1;
      Rectangle var3 = new Rectangle();
      var3.x = this.getBounds().x - this.getParent().getInsets().left;
      var3.y = this.getBounds().y - this.getParent().getInsets().top;
      var3.width = this.getBounds().width;
      var3.height = this.getBounds().height;
      this.painter.paintEnvelope(var2, var3, this.envelope.getSelectedFelado(), this.envelope.getSelectedCimzett());
   }

   class EnvelopePreviewBorder extends AbstractBorder {
      public void paintBorder(Component var1, Graphics var2, int var3, int var4, int var5, int var6) {
         Color var7 = var2.getColor();
         var2.setColor(new Color(184, 207, 229));
         var2.drawRect(var3, var4, var5 - 1, var6 - 1);
         var2.setColor(var7);
      }

      public Insets getBorderInsets(Component var1) {
         return new Insets(0, 0, 0, 0);
      }

      public boolean isBorderOpaque() {
         return false;
      }
   }
}
