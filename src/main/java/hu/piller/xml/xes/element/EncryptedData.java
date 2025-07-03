package hu.piller.xml.xes.element;

import hu.piller.kripto.AESCipher;
import hu.piller.xml.XMLElem;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Vector;
import org.bouncycastle.crypto.SecretKey;

public class EncryptedData implements XMLElem {
   public static final String TAG_ENCRYPTED_DATA = "EncryptedData";
   public static final String TAG_CIPHER_DATA = "CipherData";
   public static final String TAG_CIPHER_VALUE = "CipherValue";
   public static final String ENC_AES_256_STR = "<EncryptionMethod Algorithm='http://www.w3.org/2001/04/xmlenc#aes256-cbc'/>";
   public static final int ENC_AES_256 = 0;
   private Vector keyInfos;
   private SecretKey symmetricKey;
   private String encMethod;
   private InputStream plainSrc;
   private String id;
   private byte[] hash;

   public byte[] getHash() {
      return this.hash;
   }

   public EncryptedData(Vector keyInfos, SecretKey secretKey, InputStream plainSrc) {
      this.encMethod = "<EncryptionMethod Algorithm='http://www.w3.org/2001/04/xmlenc#aes256-cbc'/>";
      this.keyInfos = keyInfos;
      this.symmetricKey = secretKey;
      this.plainSrc = plainSrc;
   }

   public EncryptedData(String id, Vector keyInfos, SecretKey secretKey, InputStream plainSrc) {
      this(keyInfos, secretKey, plainSrc);
      this.setId(id);
   }

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public void printXML(String indent, OutputStream out) throws IOException {
      indent = indent + "\t";
      out.write((indent + "<" + "EncryptedData" + " Id='" + (this.id != null ? this.id : "") + "' xmlns='http://www.w3.org/2001/04/xmlenc#' MimeType='text/xml'>").getBytes("iso-8859-2"));
      out.write((indent + "\t" + this.encMethod).getBytes("iso-8859-2"));
      Iterator ekIt = this.keyInfos.iterator();

      while(ekIt.hasNext()) {
         ((KeyInfo)ekIt.next()).printXML(indent, out);
      }

      out.write((indent + "\t<" + "CipherData" + ">").getBytes("iso-8859-2"));
      out.write((indent + "\t\t<" + "CipherValue" + ">").getBytes("iso-8859-2"));

      try {
         this.hash = AESCipher.encryptStream(this.symmetricKey, this.plainSrc, out, true);
         out.flush();
      } catch (Exception var5) {
         var5.printStackTrace();
         throw new IOException("sikertelen az allomany titkositasa" + var5.toString());
      }

      out.write("</CipherValue>".getBytes("iso-8859-2"));
      out.write((indent + "\t</" + "CipherData" + ">").getBytes("iso-8859-2"));
      out.write((indent + "</" + "EncryptedData" + ">").getBytes("iso-8859-2"));
   }
}
