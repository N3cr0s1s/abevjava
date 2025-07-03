package hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.downloadablecomponents;

import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

public class SAXStopParseException extends SAXParseException {
   public SAXStopParseException(String var1, Locator var2) {
      super(var1, var2);
   }

   public SAXStopParseException(String var1, Locator var2, Exception var3) {
      super(var1, var2, var3);
   }

   public SAXStopParseException(String var1, String var2, String var3, int var4, int var5) {
      super(var1, var2, var3, var4, var5);
   }

   public SAXStopParseException(String var1, String var2, String var3, int var4, int var5, Exception var6) {
      super(var1, var2, var3, var4, var5, var6);
   }
}
