package hu.piller.xml.xes.element;

import hu.piller.xml.XMLElem;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;

public class KeyInfo implements XMLElem {
   public static final String TAG_KEY_INFO = "KeyInfo";
   public static final String TAG_KEY_NAME = "KeyName";
   public static final String TAG_RETRIEVALMETHOD = "RetrievalMethod";
   public static final String ATTR_URI = "URI";
   private String keyName;
   private KeyData keyData;
   private EncryptedKey encKey;
   private Vector retrievalMethods;

   public KeyInfo() {
   }

   public KeyInfo(EncryptedKey encKey) {
      this.encKey = encKey;
   }

   public KeyInfo(KeyData keyData) {
      this.keyData = keyData;
   }

   public KeyInfo(Vector retrievalMethods) {
      this.retrievalMethods = retrievalMethods;
   }

   public void setKeyData(KeyData keyData) {
      this.keyData = keyData;
   }

   public KeyData getKeyData() {
      return this.keyData;
   }

   public void setEncKey(EncryptedKey encKey) {
      this.encKey = encKey;
   }

   public EncryptedKey getEncKey() {
      return this.encKey;
   }

   public void addRetrievalMethod(String rmUri) {
      if (this.retrievalMethods == null) {
         this.retrievalMethods = new Vector();
      }

      this.retrievalMethods.add(rmUri);
   }

   public void setRetrievalMethods(Vector rms) {
      Enumeration en = rms.elements();

      while(en.hasMoreElements()) {
         EncryptedKey ek = (EncryptedKey)en.nextElement();
         String id = ek.getId();
         this.addRetrievalMethod("#" + id);
      }

   }

   public void printXML(String indent, OutputStream out) throws IOException {
      indent = indent + "\t";
      out.write((indent + "<ds:" + "KeyInfo" + " xmlns:ds='http://www.w3.org/2000/09/xmldsig#'>").getBytes("iso-8859-2"));
      if (this.keyData != null) {
         this.keyData.printXML(indent, out);
      } else if (this.encKey != null) {
         this.encKey.printXML(indent, out);
      }

      if (this.retrievalMethods != null && this.retrievalMethods.size() > 0) {
         for(int i = 0; i < this.retrievalMethods.size(); ++i) {
            out.write((indent + "\t<ds:" + "RetrievalMethod" + " " + "URI" + "='" + this.retrievalMethods.elementAt(i) + "' />").getBytes("iso-8859-2"));
         }
      }

      out.write((indent + "</ds:" + "KeyInfo" + ">").getBytes("iso-8859-2"));
      out.flush();
   }

   public String toString() {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      try {
         this.printXML("\n", baos);
      } catch (IOException var3) {
         var3.printStackTrace();
         return null;
      }

      return baos.toString();
   }
}
