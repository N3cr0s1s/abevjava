package hu.piller.enykp.print;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import javax.print.SimpleDoc;
import javax.print.DocFlavor.BYTE_ARRAY;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;

public class ImagePrint {
   private PrinterJob printJob;
   private PrintRequestAttributeSet prAttrSet;
   public static final int BRPLUS = 200;
   public static final int RES = 3;
   public static final int TEXT_SHIFT = 10;
   public String loadedFilename;
   BufferedImage bImage;
   private int szin = 1;
   public static String datum;
   public static SupportType st;

   public ImagePrint(PrinterJob var1, PrintRequestAttributeSet var2, SupportType var3) {
      this.printJob = var1;
      this.prAttrSet = var2;
      st = var3;
   }

   private int getDefaultPageWidth(Lap var1) {
      return (int)var1.getPf().getWidth();
   }

   private int getDefaultPageHeight(Lap var1) {
      return (int)var1.getPf().getHeight();
   }

   private double getScaleX(Lap var1) {
      return var1.getPf().getImageableWidth() / var1.getLma().meret.getWidth();
   }

   private double getScaleY(Lap var1) {
      double var2 = 0.0D;
      if (this.isElektronikusBeadas()) {
         var2 = var1.getPf().getImageableHeight() / var1.getLma().meret_mod.getHeight();
      } else {
         double var4 = 210.0D;
         var2 = (var1.getPf().getImageableHeight() - var4) / (var1.getLma().meret_mod.getHeight() - 150.0D + (double)(var1.getLma().alloLap ? 0 : 30));
      }

      return var2;
   }

   private void fillWithColor(Graphics2D var1, BufferedImage var2, Color var3) {
      Color var4 = var1.getColor();
      var1.setColor(var3);
      var1.fillRect(0, 0, var2.getWidth(), var2.getHeight());
      var1.setColor(var4);
   }

   private boolean isElektronikusBeadas() {
      boolean var1 = false;
      if (MainPrinter.getBookModel().epost != null && MainPrinter.getBookModel().epost.equalsIgnoreCase("onlyinternet")) {
         var1 = true;
      }

      return var1;
   }

   public int printToImage(Lap var1) throws Exception, Error {
      byte var2 = 3;
      if (!this.isElektronikusBeadas()) {
         var2 = 4;
      }

      BufferedImage var3 = new BufferedImage(this.getDefaultPageWidth(var1) * var2, this.getDefaultPageHeight(var1) * var2, this.szin);
      Graphics2D var4 = (Graphics2D)var3.getGraphics();
      this.fillWithColor(var4, var3, Color.WHITE);
      var4.scale(this.getScaleX(var1) * (double)var2, this.getScaleY(var1) * (double)var2);
      var1.getPrintable().setPageindex(var1.getLma().foLapIndex);
      var1.getPrintable().setDynindex(var1.getLma().lapSzam);
      var1.getPrintable().print(var4, var1.getPf(), 0);
      if (!MainPrinter.emptyPrint) {
         try {
            var1.extraPrint(var4, Math.max(0.0D, var1.getLma().meret_mod.getWidth() - var1.getLma().meret.getWidth()), var1.getPf(), this.isElektronikusBeadas());
         } catch (Exception var9) {
            var9.printStackTrace();
            if (var9 instanceof PrinterException && var9.getMessage().startsWith("*HIBA")) {
               throw (PrinterException)var9;
            }

            throw new PrinterException("Barkód nyomtatás hiba!");
         }
      } else if (!MainPrinter.emptyPrint) {
         Font var5 = var4.getFont();
         var4.setFont(new Font(var4.getFont().getFontName(), 0, 15));
         Color var6 = var4.getColor();
         var4.setColor(Color.RED);
         var4.drawString("Hibás nyomtatványsablon, hiányzó bárkód! A nyomtatvány nem küldhető be!", (int)((double)MainPrinter.nyomtatoMargo * 2.8D) + 10, var3.getHeight());
         var4.setFont(var5);
         var4.setColor(var6);
      }

      ByteArrayOutputStream var10 = new ByteArrayOutputStream();
      if (st.gifSupported) {
         if (!ImageIO.write(var3, "GIF", var10)) {
            System.out.println("GIF failed");
            if (!ImageIO.write(var3, "PNG", var10)) {
               System.out.println("PNG failed");
               if (!ImageIO.write(var3, "JPG", var10)) {
                  System.out.println("JPG failed");
                  return 1;
               }
            }
         }
      } else if (st.pngSupported) {
         if (!ImageIO.write(var3, "PNG", var10)) {
            System.out.println("PNG failed 2");
            return 1;
         }
      } else if (!ImageIO.write(var3, "JPG", var10)) {
         System.out.println("JPG failed 2");
         return 1;
      }

      MediaPrintableArea var11 = new MediaPrintableArea(0, 0, 500, 500, 1000);
      HashDocAttributeSet var7 = new HashDocAttributeSet();
      var7.add(var11);
      var7.add(var1.getLma().alloLap ? OrientationRequested.PORTRAIT : OrientationRequested.LANDSCAPE);
      SimpleDoc var8 = new SimpleDoc(var10.toByteArray(), BYTE_ARRAY.GIF, var7);
      this.prAttrSet.add(var1.getLma().alloLap ? OrientationRequested.PORTRAIT : OrientationRequested.LANDSCAPE);
      this.printJob.getPrintService().createPrintJob().print(var8, (PrintRequestAttributeSet)null);
      var3 = null;
      return 0;
   }
}
