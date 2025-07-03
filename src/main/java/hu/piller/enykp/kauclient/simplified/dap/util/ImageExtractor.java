package hu.piller.enykp.kauclient.simplified.dap.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

public class ImageExtractor {
   public static BufferedImage extractInlineImageFromImgTag(String var0) throws IOException {
      String var1 = var0.replaceAll(".*src=\"data:image/(png|jpg);base64,", "").replaceAll("\".*", "");
      byte[] var2 = DatatypeConverter.parseBase64Binary(var1);
      ByteArrayInputStream var3 = new ByteArrayInputStream(var2);
      return ImageIO.read(var3);
   }
}
