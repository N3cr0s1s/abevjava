package hu.piller.enykp.kauclient.simplified.dap.widget;

import hu.piller.enykp.gui.GuiUtil;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JImageDialog extends JDialog {
   private boolean is_canceled;

   public JImageDialog(Frame var1, BufferedImage var2) {
      super(var1, "DÁP QR kódos azonosítás", true);
      this.setDefaultCloseOperation(0);
      int var3 = GuiUtil.getW("WWWW3. Olvassa be a QR-kódot a telefon kamerájával.WWWW");
      JPanel var4 = this.createTopPanel(var3 - 8);
      JPanel var5 = new JPanel();
      JPanel var6 = this.createBottomPanel(var3 - 8);
      var6.add(new JLabel("1. Nyissa meg a DÁP mobilalkalmazást"));
      this.setSize(var3, 600);
      this.setLayout(new BorderLayout());
      JLabel var7 = new JLabel(new ImageIcon(var2));
      var5.add(var7);
      this.add(var4, "North");
      this.add(var5, "Center");
      this.add(var6, "South");
      this.setResizable(false);
      this.pack();
      this.setLocationRelativeTo((Component)null);
   }

   public boolean isCanceled() {
      return this.is_canceled;
   }

   public void close() {
      this.setVisible(false);
      this.dispose();
   }

   private JPanel createTopPanel(int var1) {
      JPanel var2 = new JPanel((LayoutManager)null);
      var2.add(new JLabel());
      int var3 = GuiUtil.getCommonItemHeight();
      byte var4 = 10;
      JLabel var5 = new JLabel("QR-kód beolvasása");
      Font var6 = var5.getFont();
      var5.setFont(var6.deriveFont((float)(var6.getSize() + 5)));
      GuiUtil.setDynamicBound(var5, var5.getText(), 10, var4);
      int var8 = var4 + 2 * var3;
      var2.add(var5);
      var5 = new JLabel("Nyissa meg a DÁP mobilalkalmazást, és");
      var5.setFont(var6.deriveFont(0));
      GuiUtil.setDynamicBound(var5, var5.getText(), 10, var8);
      var2.add(var5);
      var8 += var3;
      var5 = new JLabel("olvassa be a QR-kódot");
      var5.setFont(var6.deriveFont(0));
      GuiUtil.setDynamicBound(var5, var5.getText(), 10, var8);
      var2.add(var5);
      var8 += var3;
      var8 += var3;
      Dimension var7 = new Dimension(var1, var8);
      var2.setMinimumSize(var7);
      var2.setPreferredSize(var7);
      var2.setSize(var7);
      return var2;
   }

   private JPanel createBottomPanel(int var1) {
      JPanel var2 = new JPanel((LayoutManager)null);
      var2.add(new JLabel());
      int var3 = GuiUtil.getCommonItemHeight();
      byte var4 = 10;
      JLabel var5 = new JLabel("Hogyan működik?");
      Font var6 = var5.getFont();
      var5.setFont(var6.deriveFont((float)(var6.getSize() + 5)));
      GuiUtil.setDynamicBound(var5, var5.getText(), 10, var4);
      var2.add(var5);
      int var10 = var4 + 2 * var3;
      var5 = new JLabel("1. Nyissa meg a DÁP mobilalkalmazást.");
      var5.setFont(var6.deriveFont(0));
      GuiUtil.setDynamicBound(var5, var5.getText(), 10, var10);
      var2.add(var5);
      var10 = (int)((double)var10 + 1.5D * (double)var3);
      var5 = new JLabel("2. Válassza ki a QR belépés funkciót.");
      var5.setFont(var6.deriveFont(0));
      GuiUtil.setDynamicBound(var5, var5.getText(), 10, var10);
      var2.add(var5);
      var10 = (int)((double)var10 + 1.5D * (double)var3);
      var5 = new JLabel("3. Olvassa be a QR-kódot a telefon kamerájával.");
      var5.setFont(var6.deriveFont(0));
      GuiUtil.setDynamicBound(var5, var5.getText(), 10, var10);
      var2.add(var5);
      var10 = (int)((double)var10 + 1.5D * (double)var3);
      var5 = new JLabel("4. Engedélyezze a bejelentkezést.");
      var5.setFont(var6.deriveFont(0));
      GuiUtil.setDynamicBound(var5, var5.getText(), 10, var10);
      var2.add(var5);
      var10 = (int)((double)var10 + 1.5D * (double)var3);
      var10 += var3;
      JButton var7 = new JButton("Mégsem");
      var7.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (Boolean.parseBoolean(System.getProperty("kau.trace"))) {
               System.out.println("DÁP QR kódos azonosítás ÁNYK-ban megszakítva");
            }

            JImageDialog.this.is_canceled = true;
            JImageDialog.this.setVisible(false);
         }
      });
      int var8 = GuiUtil.getW(var7, "Mégsem");
      var8 = (var1 - var8) / 2;
      GuiUtil.setDynamicBound(var7, var7.getText(), var8, var10);
      var2.add(var7);
      var10 += var3;
      var10 += var3;
      Dimension var9 = new Dimension(var1, var10);
      var2.setMinimumSize(var9);
      var2.setPreferredSize(var9);
      var2.setSize(var9);
      return var2;
   }
}
