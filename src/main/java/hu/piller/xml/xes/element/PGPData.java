package hu.piller.xml.xes.element;

import hu.piller.tools.Base64;
import hu.piller.tools.Utils;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;

public class PGPData extends KeyData {
   public static final String TAG_PGP_DATA = "PGPData";
   public static final String TAG_PGP_KEY_PACKET = "PGPKeyPacket";
   private String pubKeyStr;
   private String PGPKeyPacket;

   public PGPData() {
   }

   public PGPData(PGPPublicKey pubKey) throws NoSuchProviderException, PGPException {
      super(pubKey.getKey(Utils.getBCP()));
      this.PGPKeyPacket = getKeyPacket(pubKey);
   }

   public PGPData(PublicKey pubKey, String pubKeyStr) {
      super(pubKey);
      this.pubKeyStr = pubKeyStr;
   }

   public static String getKeyPacket(PGPPublicKey pubKey) {
      String keyPacket = "";

      try {
         keyPacket = Base64.encodeBytes(pubKey.getEncoded());
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      return keyPacket;
   }

   public String getPGPKeyPacket() {
      return this.PGPKeyPacket;
   }

   public void setPGPKeyPacket(String PGPKeyPacket) {
      this.PGPKeyPacket = PGPKeyPacket;
   }

   public void printXML(String indent, OutputStream out) throws IOException {
      indent = indent + "\t";
      out.write((indent + "<ds:" + "PGPData" + ">").getBytes("iso-8859-2"));
      out.write((indent + "\t<ds:" + "PGPKeyPacket" + ">").getBytes("iso-8859-2"));
      out.write((this.pubKeyStr == null ? this.PGPKeyPacket : this.pubKeyStr).getBytes("iso-8859-2"));
      out.write("</ds:PGPKeyPacket>".getBytes("iso-8859-2"));
      out.write((indent + "</ds:" + "PGPData" + ">").getBytes("iso-8859-2"));
   }
}
