package hu.piller.xml.xes.element;

import hu.piller.xml.XMLElem;
import java.io.IOException;
import java.io.OutputStream;
import java.security.PublicKey;

public abstract class KeyData implements XMLElem {
   PublicKey publicKey;

   public KeyData() {
   }

   KeyData(PublicKey publicKey) {
      this.publicKey = publicKey;
   }

   public PublicKey getPk() {
      return this.publicKey;
   }

   public abstract void printXML(String var1, OutputStream var2) throws IOException;
}
