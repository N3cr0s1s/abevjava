package hu.piller.kripto.keys;

import hu.piller.tools.Utils;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import javax.security.cert.X509Certificate;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;

public class StoreWrapper {
   private Object store;
   private int type = -1;
   private String location;
   private Vector keys;

   public Object getStore() {
      return this.store;
   }

   public void setStore(Object store) {
      this.store = store;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public String getLocation() {
      return this.location;
   }

   public void setLocation(String location) {
      this.location = location;
   }

   public Vector listKeys() throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
      if (this.keys != null) {
         return this.keys;
      } else {
         Vector keys;
         keys = new Vector();
         Iterator keyRingIt;
         KeyWrapper kw;
         Iterator keyIt;
         label107:
         switch(this.type) {
         case 110:
            PGPSecretKeyRing keyRing = (PGPSecretKeyRing)this.store;
            keyRingIt = keyRing.getSecretKeys();

            while(true) {
               if (!keyRingIt.hasNext()) {
                  break label107;
               }

               PGPSecretKey sk = (PGPSecretKey)keyRingIt.next();
               keys.add(this.wrapKey(sk));
               keys.add(this.wrapKey(sk.getPublicKey()));
            }
         case 120:
            PGPPublicKeyRing cKeyRing = (PGPPublicKeyRing)this.store;
            keyRingIt = cKeyRing.getPublicKeys();

            while(true) {
               if (!keyRingIt.hasNext()) {
                  break label107;
               }

               PGPPublicKey pk = (PGPPublicKey)keyRingIt.next();
               keys.add(this.wrapKey(pk));
            }
         case 130:
            PGPSecretKeyRingCollection keyRingColl = (PGPSecretKeyRingCollection)this.store;
            keyRingIt = keyRingColl.getKeyRings();

            while(true) {
               if (!keyRingIt.hasNext()) {
                  break label107;
               }

               PGPSecretKeyRing dKeyRing = (PGPSecretKeyRing)keyRingIt.next();
               keyIt = dKeyRing.getSecretKeys();

               while(keyIt.hasNext()) {
                  PGPSecretKey sk = (PGPSecretKey)keyIt.next();
                  keys.add(this.wrapKey(sk));
                  keys.add(this.wrapKey(sk.getPublicKey()));
               }
            }
         case 140:
            PGPPublicKeyRingCollection fKeyRingColl = (PGPPublicKeyRingCollection)this.store;
            keyRingIt = fKeyRingColl.getKeyRings();

            while(true) {
               if (!keyRingIt.hasNext()) {
                  break label107;
               }

               PGPPublicKeyRing gKeyRing = (PGPPublicKeyRing)keyRingIt.next();
               keyIt = gKeyRing.getPublicKeys();

               while(keyIt.hasNext()) {
                  PGPPublicKey pk = (PGPPublicKey)keyIt.next();
                  keys.add(this.wrapKey(pk));
               }
            }
         case 200:
         case 300:
            KeyStore keyStore = (KeyStore)this.store;
            Enumeration en = keyStore.aliases();

            while(true) {
               if (!en.hasMoreElements()) {
                  break label107;
               }

               String alias = (String)en.nextElement();

               try {
                  if (keyStore.isKeyEntry(alias)) {
                     kw = new KeyWrapper();
                     kw.setStoreLocation(this.location);
                     kw.setStoreType(this.type);
                     kw.setType(1);
                     kw.setAlias(alias);
                     kw.setCreationDate(keyStore.getCreationDate(alias));
                     kw.setKeyStore(keyStore);
                     keys.add(kw);
                     Certificate[] certs = keyStore.getCertificateChain(alias);

                     for(int i = 0; i < certs.length; ++i) {
                        X509Certificate cer = X509Certificate.getInstance(certs[i].getEncoded());
                        keys.add(this.wrapKey(cer, alias));
                     }
                  } else {
                     X509Certificate cer = X509Certificate.getInstance(keyStore.getCertificate(alias).getEncoded());
                     keys.add(this.wrapKey(cer, alias));
                  }
               } catch (Exception var10) {
               }
            }
         case 400:
            X509Certificate cer = (X509Certificate)this.store;
            keys.add(this.wrapKey(cer));
            break;
         default:
            KeyStore cKeyStore = (KeyStore)this.store;

            for(Enumeration eng = cKeyStore.aliases(); eng.hasMoreElements(); keys.add(kw)) {
               kw = new KeyWrapper();
               kw.setStoreLocation(this.location);
               kw.setStoreType(this.type);
               String alias = (String)eng.nextElement();
               kw.setAlias(alias);
               kw.setCreationDate(cKeyStore.getCreationDate(alias));

               try {
                  if (cKeyStore.isKeyEntry(alias)) {
                     kw.setType(1);
                  } else {
                     Key key = cKeyStore.getCertificate(alias).getPublicKey();
                     kw.setKey(key);
                     kw.setType(0);
                  }
               } catch (Exception var9) {
                  var9.printStackTrace();
               }
            }
         }

         this.keys = keys;
         return keys;
      }
   }

   public Vector listKeys(KeyWrapperFilter filter) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
      Vector keys = new Vector();
      Enumeration en = this.listKeys().elements();

      while(en.hasMoreElements()) {
         KeyWrapper kw = (KeyWrapper)en.nextElement();
         if (filter.accept(kw)) {
            keys.add(kw);
         }
      }

      return keys;
   }

   private KeyWrapper wrapKey(PGPPublicKey pk) {
      KeyWrapper kw = new KeyWrapper();
      Iterator attrs = pk.getUserIDs();

      String alias;
      for(alias = ""; attrs.hasNext(); alias = alias + attrs.next() + " ") {
      }

      kw.setAlias(alias);
      kw.setCreationDate(pk.getCreationTime());

      try {
         kw.setPgpPubKey(pk);
         kw.setKey(pk.getKey(Utils.getBCP()));
      } catch (PGPException var6) {
         var6.printStackTrace();
      } catch (NoSuchProviderException var7) {
         var7.printStackTrace();
      }

      kw.setStoreLocation(this.location);
      kw.setStoreType(this.type);
      kw.setType(0);
      return kw;
   }

   private KeyWrapper wrapKey(PGPSecretKey sk) {
      KeyWrapper kw = new KeyWrapper();
      Iterator attrs = sk.getUserIDs();

      String alias;
      for(alias = ""; attrs.hasNext(); alias = alias + attrs.next() + " ") {
      }

      kw.setAlias(alias);
      kw.setCreationDate(sk.getPublicKey().getCreationTime());
      kw.setStoreLocation(this.location);
      kw.setStoreType(this.type);
      kw.setType(1);
      kw.setPgpSecretKey(sk);
      return kw;
   }

   private KeyWrapper wrapKey(X509Certificate cer) {
      KeyWrapper kw = new KeyWrapper();
      kw.setType(0);
      kw.setAlias(cer.getSubjectDN().toString());
      kw.setCreationDate(cer.getNotBefore());
      kw.setKey(cer.getPublicKey());
      kw.setStoreLocation(this.location);
      kw.setStoreType(this.type);
      kw.setCer(cer);
      return kw;
   }

   private KeyWrapper wrapKey(X509Certificate cer, String alias) {
      KeyWrapper kw = new KeyWrapper();
      kw.setType(0);
      kw.setAlias(alias);
      kw.setCreationDate(cer.getNotBefore());
      kw.setKey(cer.getPublicKey());
      kw.setStoreLocation(this.location);
      kw.setStoreType(this.type);
      kw.setCer(cer);
      return kw;
   }
}
