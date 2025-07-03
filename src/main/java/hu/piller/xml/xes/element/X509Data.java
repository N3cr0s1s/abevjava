package hu.piller.xml.xes.element;

import hu.piller.tools.Utils;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import javax.security.cert.X509Certificate;

public class X509Data extends KeyData {
   public static final String TAG_X509_DATA = "X509Data";
   public static final String TAG_X509_SKI = "X509SKI";
   private X509Certificate cer = null;
   private String ski;

   public X509Data() {
   }

   public X509Data(X509Certificate cer) throws NoSuchAlgorithmException, IOException {
      super(cer.getPublicKey());
      this.cer = cer;
      this.ski = Utils.createHash(cer.getPublicKey().getEncoded());
   }

   private String getSki() {
      return this.ski;
   }

   public void setSki(String ski) {
      this.ski = ski;
   }

   public void printXML(String indent, OutputStream out) throws IOException {
      indent = indent + "\t";
      out.write((indent + "<ds:" + "X509Data" + ">").getBytes("iso-8859-2"));
      out.write((indent + "\t<ds:" + "X509SKI" + ">").getBytes("iso-8859-2"));
      out.write(this.getSki().getBytes("iso-8859-2"));
      out.write("</ds:X509SKI>".getBytes("iso-8859-2"));
      out.write((indent + "</ds:" + "X509Data" + ">").getBytes("iso-8859-2"));
   }
}
