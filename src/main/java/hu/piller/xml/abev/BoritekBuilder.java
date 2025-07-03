package hu.piller.xml.abev;

import hu.piller.kripto.RSACipher;
import hu.piller.kripto.keys.KeyManager;
import hu.piller.kripto.keys.KeyWrapper;
import hu.piller.kripto.keys.StoreManager;
import hu.piller.kripto.keys.StoreWrapper;
import hu.piller.tools.Base64;
import hu.piller.xml.abev.element.Boritek;
import hu.piller.xml.abev.element.Csatolmany;
import hu.piller.xml.abev.element.CsatolmanyInfo;
import hu.piller.xml.abev.element.DocMetaData;
import hu.piller.xml.abev.element.Fejlec;
import hu.piller.xml.abev.element.Torzs;
import hu.piller.xml.xes.element.EncryptedData;
import hu.piller.xml.xes.element.EncryptedKey;
import hu.piller.xml.xes.element.InvalidOperationException;
import hu.piller.xml.xes.element.KeyInfo;
import hu.piller.xml.xes.element.PGPData;
import hu.piller.xml.xes.element.X509Data;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.security.cert.X509Certificate;
import org.bouncycastle.crypto.BadPaddingException;
import org.bouncycastle.crypto.IllegalBlockSizeException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.NoSuchPaddingException;
import org.bouncycastle.crypto.SecretKey;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;

public class BoritekBuilder {
   private Vector recipients;
   private DocMetaData metaData;
   private InputStream src;
   private OutputStream dest;
   private Boritek boritek;
   private Hashtable csatInfo;
   private int recipientCounter;

   public DocMetaData getMetaData() {
      return this.metaData;
   }

   public void setMetaData(DocMetaData metaData) {
      this.metaData = metaData;
      String cimzettNyilvanosKulcs = metaData.getCimzettNyilvanosKulcs();
      if (cimzettNyilvanosKulcs != null) {
         try {
            ByteArrayInputStream bin = new ByteArrayInputStream(cimzettNyilvanosKulcs.getBytes());
            StoreWrapper sw = StoreManager.loadStore((InputStream)bin, (char[])null);
            int storeType = sw.getType();
            if (storeType == 120) {
               this.addRecipient(((PGPPublicKeyRing)sw.getStore()).getPublicKey());
            } else if (storeType == 400) {
               this.addRecipient((X509Certificate)sw.getStore());
            }
         } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
         } catch (NoSuchAlgorithmException var7) {
            var7.printStackTrace();
         } catch (NoSuchProviderException var8) {
            var8.printStackTrace();
         } catch (PGPException var9) {
            var9.printStackTrace();
         } catch (IOException var10) {
            var10.printStackTrace();
         }
      }

   }

   public void setMetaData(DocMetaData metaData, boolean useCimzettKulcs) {
      this.metaData = metaData;
      String cimzettNyilvanosKulcs = metaData.getCimzettNyilvanosKulcs();
      if (cimzettNyilvanosKulcs != null && useCimzettKulcs) {
         try {
            ByteArrayInputStream bin = new ByteArrayInputStream(cimzettNyilvanosKulcs.getBytes());
            StoreWrapper sw = StoreManager.loadStore((InputStream)bin, (char[])null);
            int storeType = sw.getType();
            if (storeType == 120) {
               this.addRecipient(((PGPPublicKeyRing)sw.getStore()).getPublicKey());
            } else if (storeType == 400) {
               this.addRecipient((X509Certificate)sw.getStore());
            }
         } catch (UnsupportedEncodingException var7) {
            var7.printStackTrace();
         } catch (NoSuchAlgorithmException var8) {
            var8.printStackTrace();
         } catch (NoSuchProviderException var9) {
            var9.printStackTrace();
         } catch (PGPException var10) {
            var10.printStackTrace();
         } catch (IOException var11) {
            var11.printStackTrace();
         }
      }

   }

   public void setPlainSrc(InputStream src) {
      this.src = src;
   }

   public void setDest(OutputStream dest) {
      this.dest = dest;
   }

   public void addRecipient(X509Certificate cer) throws NoSuchAlgorithmException, IOException {
      if (this.recipients == null) {
         this.recipients = new Vector();
      }

      ++this.recipientCounter;
      this.recipients.add(new KeyInfo(new X509Data(cer)));
   }

   public void addRecipient(String alias, X509Certificate cer) throws NoSuchAlgorithmException, IOException {
      if (this.recipients == null) {
         this.recipients = new Vector();
      }

      this.recipients.add(new KeyInfo(new X509Data(cer)));
   }

   public void addRecipient(PGPPublicKey pk) throws NoSuchProviderException, PGPException {
      if (this.recipients == null) {
         this.recipients = new Vector();
      }

      this.recipients.add(new KeyInfo(new PGPData(pk)));
   }

   public void addRecipient(String alias, PGPPublicKey pk) throws NoSuchProviderException, PGPException {
      if (this.recipients == null) {
         this.recipients = new Vector();
      }

      this.recipients.add(new KeyInfo(new PGPData(pk)));
   }

   public void addRecipient(KeyWrapper kw) throws NoSuchAlgorithmException, IOException, NoSuchProviderException, PGPException {
      if (this.recipients == null) {
         this.recipients = new Vector();
      }

      this.recipients.add(new KeyInfo(kw.getKeyData()));
   }

   public Vector listRecipient() {
      Enumeration keys = this.recipients.elements();

      while(keys.hasMoreElements()) {
         System.out.println(keys.nextElement());
      }

      return this.recipients;
   }

   public void build() throws IOException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidOperationException, InvalidKeySpecException, IllegalAccessException, ClassNotFoundException, InstantiationException, InvalidCipherTextException {
      Torzs torzs = new Torzs();
      SecretKey secretKey = KeyManager.generateAESKey(256);
      Vector encKeys = this.createEncKeys(this.recipients, secretKey);
      if (this.src != null) {
         Vector keyInfos = new Vector();

         for(int i = 0; i < encKeys.size(); ++i) {
            keyInfos.add(new KeyInfo((EncryptedKey)encKeys.elementAt(i)));
         }

         EncryptedData ed = new EncryptedData("ED", keyInfos, secretKey, this.src);
         torzs.setEncData(ed);
      }

      if (this.metaData.getCsatInfoLista() != null && !this.metaData.getCsatInfoLista().isEmpty()) {
         KeyInfo ki = new KeyInfo();
         ki.setRetrievalMethods(encKeys);
         Vector kis = new Vector();
         kis.add(ki);
         Enumeration en = this.metaData.getCsatInfoLista().elements();

         while(en.hasMoreElements()) {
            CsatolmanyInfo info = (CsatolmanyInfo)en.nextElement();
            String fileName = plainConvert(info.getFileURI());
            URI fileUri = URI.create(fileName);
            URL fileUrl = fileUri.toURL();
            EncryptedData attEd = new EncryptedData(info.getAzon(), kis, secretKey, fileUrl.openStream());
            Csatolmany csatolmany = new Csatolmany(info.getAzon(), attEd);
            torzs.addCsatolmany(csatolmany);
         }
      }

      this.boritek = new Boritek();
      this.boritek.setFejlec(new Fejlec(this.metaData));
      this.boritek.setTorzs(torzs);
      this.boritek.printXML("\n", this.dest);
   }

   private Vector createEncKeys(Vector recipients) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidOperationException, InvalidKeySpecException, IllegalAccessException, ClassNotFoundException, InstantiationException, InvalidCipherTextException {
      SecretKey secKey = KeyManager.generateAESKey(256);
      return this.createEncKeys(recipients, secKey);
   }

   private Vector createEncKeys(Vector recipients, SecretKey sk) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidOperationException, InvalidKeySpecException, IllegalAccessException, ClassNotFoundException, InstantiationException, InvalidCipherTextException {
      Iterator kiIt = recipients.iterator();
      Vector encKeys = new Vector();

      for(int id = 1; kiIt.hasNext(); ++id) {
         KeyInfo ki = (KeyInfo)kiIt.next();
         EncryptedKey ek = new EncryptedKey("EK" + id, ki, Base64.encodeBytes(RSACipher.encryptData(ki.getKeyData().getPk(), sk.getEncoded())));
         encKeys.add(ek);
      }

      return encKeys;
   }

   public byte[] getEncryptedDataHash() {
      return this.boritek.getTorzs().getEncData().getHash();
   }

   public static String plainConvert(String text) {
      text = text.replaceAll("&amp;", "&");
      text = text.replaceAll("&lt;", "<");
      text = text.replaceAll("&#13;", "\r\n");
      text = text.replaceAll("&gt;", ">");
      text = text.replaceAll("&quot;", "\"");
      text = text.replaceAll("&apos;", "'");
      return text;
   }
}
