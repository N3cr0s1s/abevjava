package hu.piller.xml.xes.element;

import hu.piller.xml.XMLElem;
import java.io.IOException;
import java.io.OutputStream;

public class EncryptedKey implements XMLElem {
   public static final String TAG_ENCRYPTED_KEY = "EncryptedKey";
   public static final String TAG_CIPHER_DATA = "CipherData";
   public static final String TAG_CIPHER_VALUE = "CipherValue";
   public static final String ENC_RSA_STR = "<EncryptionMethod Algorithm='http://www.w3.org/2001/04/xmlenc#rsa-1_5'/>";
   public String encMethod;
   private KeyInfo keyInfo;
   private String id;
   private String cipherValue;

   public EncryptedKey() {
      this.encMethod = "<EncryptionMethod Algorithm='http://www.w3.org/2001/04/xmlenc#rsa-1_5'/>";
      this.keyInfo = new KeyInfo();
   }

   public EncryptedKey(KeyInfo keyInfo, String cipherValue) {
      this.encMethod = "<EncryptionMethod Algorithm='http://www.w3.org/2001/04/xmlenc#rsa-1_5'/>";
      this.setKeyInfo(keyInfo);
      this.setCipherValue(cipherValue);
   }

   public EncryptedKey(String id, KeyInfo keyInfo, String cipherValue) {
      this(keyInfo, cipherValue);
      this.setId(id);
   }

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public KeyInfo getKeyInfo() {
      return this.keyInfo;
   }

   public void setKeyInfo(KeyInfo keyInfo) {
      this.keyInfo = keyInfo;
   }

   public String getCipherValue() {
      return this.cipherValue;
   }

   public void setCipherValue(String cipherValue) {
      this.cipherValue = cipherValue;
   }

   public String getEncMethod() {
      return this.encMethod;
   }

   public void setEncMethod(String encMethod) {
      this.encMethod = encMethod;
   }

   public void printXML(String indent, OutputStream out) throws IOException {
      indent = indent + "\t";
      out.write((indent + "<" + "EncryptedKey" + " Id='" + (this.id != null ? this.id : "") + "' xmlns='http://www.w3.org/2001/04/xmlenc#'>").getBytes("iso-8859-2"));
      this.keyInfo.printXML(indent, out);
      out.write((indent + "\t<" + "CipherData" + ">").getBytes("iso-8859-2"));
      out.write((indent + "\t\t<" + "CipherValue" + ">").getBytes("iso-8859-2"));
      out.write(this.cipherValue.getBytes("iso-8859-2"));
      out.write("</CipherValue>".getBytes("iso-8859-2"));
      out.write((indent + "\t</" + "CipherData" + ">").getBytes("iso-8859-2"));
      out.write((indent + "</" + "EncryptedKey" + ">").getBytes("iso-8859-2"));
   }
}
