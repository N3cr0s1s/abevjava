package hu.piller.kripto.keys;

import hu.piller.tools.Utils;
import hu.piller.xml.xes.element.KeyData;
import hu.piller.xml.xes.element.PGPData;
import hu.piller.xml.xes.element.X509Data;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;
import javax.security.cert.X509Certificate;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;

public class KeyWrapper {
   private int type;
   private String alias;
   private Key key;
   private String storeLocation;
   private int storeType;
   private Date creationDate;
   private boolean selected;
   private X509Certificate cer;
   private PGPSecretKey pgpSecretKey;
   private PGPPublicKey pgpPubKey;
   private KeyStore keyStore;

   public KeyWrapper() {
      new KeyWrapper(4, "", (Key)null, -1, "");
   }

   public KeyWrapper(int type, String alias, Key key, int storeType, String storeLocation) {
      this.type = type;
      this.alias = alias;
      this.key = key;
      this.storeType = storeType;
      this.storeLocation = storeLocation;
   }

   public KeyStore getKeyStore() {
      return this.keyStore;
   }

   public void setKeyStore(KeyStore keyStore) {
      this.keyStore = keyStore;
   }

   public PGPPublicKey getPgpPubKey() {
      return this.pgpPubKey;
   }

   public void setPgpPubKey(PGPPublicKey pgpPubKey) {
      this.pgpPubKey = pgpPubKey;
   }

   public PGPSecretKey getPgpSecretKey() {
      return this.pgpSecretKey;
   }

   public void setPgpSecretKey(PGPSecretKey pgpSecretKey) {
      this.pgpSecretKey = pgpSecretKey;
   }

   public X509Certificate getCer() {
      return this.cer;
   }

   public void setCer(X509Certificate cer) {
      this.cer = cer;
   }

   public boolean isSelected() {
      return this.selected;
   }

   public void setSelected(boolean selected) {
      this.selected = selected;
   }

   public String getStoreLocation() {
      return this.storeLocation;
   }

   public void setStoreLocation(String storeLocation) {
      this.storeLocation = storeLocation;
   }

   public int getStoreType() {
      return this.storeType;
   }

   public void setStoreType(int storeType) {
      this.storeType = storeType;
   }

   public Date getCreationDate() {
      return this.creationDate;
   }

   public void setCreationDate(Date creationDate) {
      this.creationDate = creationDate;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public String getAlias() {
      return this.alias;
   }

   public void setAlias(String alias) {
      this.alias = alias;
   }

   /** @deprecated */
   public Key getKey() {
      return this.key;
   }

   public Key getKey(char[] keyPass) {
      if (this.key == null) {
         try {
            if (this.type == 1) {
               switch(this.storeType) {
               case 110:
               case 130:
                  if (this.pgpSecretKey != null) {
                     this.key = this.pgpSecretKey.extractPrivateKey(keyPass, Utils.getBCP()).getKey();
                  }
                  break;
               case 200:
               case 300:
                  if (this.keyStore != null && this.keyStore.containsAlias(this.alias) && this.keyStore.isKeyEntry(this.alias)) {
                     this.key = this.keyStore.getKey(this.alias, keyPass);
                  }
               }
            }
         } catch (Exception var3) {
            return null;
         }
      }

      return this.key;
   }

   public void setKey(Key key) {
      this.key = key;
   }

   public String toString() {
      return "[Key]\n alias: " + this.alias + "\n type : " + this.type + "\n date : " + this.getCreationDate() + "\n store: " + this.storeLocation + "\n store: " + this.storeType;
   }

   public boolean equals(Object obj) {
      if (obj.getClass().getName().equalsIgnoreCase(this.getClass().getName())) {
         KeyWrapper kw2 = (KeyWrapper)obj;
         if (!kw2.getAlias().equalsIgnoreCase(this.alias)) {
            return false;
         } else if (kw2.getCreationDate().compareTo(this.creationDate) != 0) {
            return false;
         } else if (kw2.getStoreLocation() != null && !kw2.getStoreLocation().equalsIgnoreCase(this.storeLocation)) {
            return false;
         } else if (kw2.getStoreType() != this.storeType) {
            return false;
         } else if (kw2.getType() != this.type) {
            return false;
         } else {
            return kw2.getKey((char[])null) == null || kw2.getKey((char[])null).equals(this.key);
         }
      } else {
         return false;
      }
   }

   public KeyData getKeyData() throws NoSuchAlgorithmException, IOException, NoSuchProviderException, PGPException {
      if (this.cer != null) {
         return new X509Data(this.cer);
      } else {
         return this.pgpPubKey != null ? new PGPData(this.pgpPubKey) : null;
      }
   }

   public boolean isPublic() {
      return this.type == 0;
   }
}
