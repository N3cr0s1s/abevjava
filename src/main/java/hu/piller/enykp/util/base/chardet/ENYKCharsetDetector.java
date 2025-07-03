package hu.piller.enykp.util.base.chardet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import org.mozilla.universalchardet.CharsetListener;
import org.mozilla.universalchardet.UniversalDetector;

public class ENYKCharsetDetector {
   public static String detect(URL var0) throws IOException, IllegalArgumentException {
      byte[] var1 = new byte[4096];
      if (var0 == null) {
         throw new IllegalArgumentException("A detektálandó tartalom URL megadása kötelező!");
      } else {
         BufferedInputStream var2 = new BufferedInputStream(var0.openStream());
         UniversalDetector var3 = new UniversalDetector(null);

         int var4;
         while((var4 = var2.read(var1)) > 0 && !var3.isDone()) {
            var3.handleData(var1, 0, var4);
         }

         var3.dataEnd();
         String var5 = var3.getDetectedCharset();
         var3.reset();
         if (var5 == null) {
            var5 = "ascii";
         }

         return var5.toLowerCase();
      }
   }
}
