package org.bouncycastle.openpgp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bouncycastle.bcpg.BCPGOutputStream;

public class PGPPublicKeyRingCollection {
   private Map pubRings;
   private List order;

   private PGPPublicKeyRingCollection(Map pubRings, List order) {
      this.pubRings = new HashMap();
      this.order = new ArrayList();
      this.pubRings = pubRings;
      this.order = order;
   }

   public PGPPublicKeyRingCollection(byte[] encoding) throws IOException, PGPException {
      this((InputStream)(new ByteArrayInputStream(encoding)));
   }

   public PGPPublicKeyRingCollection(InputStream in) throws IOException, PGPException {
      this.pubRings = new HashMap();
      this.order = new ArrayList();
      PGPObjectFactory pgpFact = new PGPObjectFactory(in);
      PGPPublicKeyRing pgpPub = null;

      while((pgpPub = (PGPPublicKeyRing)pgpFact.nextObject()) != null) {
         Long key = new Long(pgpPub.getPublicKey().getKeyID());
         this.pubRings.put(key, pgpPub);
         this.order.add(key);
      }

   }

   public PGPPublicKeyRingCollection(Collection collection) throws IOException, PGPException {
      this.pubRings = new HashMap();
      this.order = new ArrayList();
      Iterator it = collection.iterator();

      while(it.hasNext()) {
         PGPPublicKeyRing pgpPub = (PGPPublicKeyRing)it.next();
         Long key = new Long(pgpPub.getPublicKey().getKeyID());
         this.pubRings.put(key, pgpPub);
         this.order.add(key);
      }

   }

   public int size() {
      return this.order.size();
   }

   public Iterator getKeyRings() {
      return this.pubRings.values().iterator();
   }

   public Iterator getKeyRings(String userID, boolean matchPartial) throws PGPException {
      Iterator it = this.getKeyRings();
      ArrayList rings = new ArrayList();

      while(it.hasNext()) {
         PGPPublicKeyRing pubRing = (PGPPublicKeyRing)it.next();
         Iterator uIt = pubRing.getPublicKey().getUserIDs();

         while(uIt.hasNext()) {
            if (matchPartial) {
               if (((String)uIt.next()).indexOf(userID) > -1) {
                  rings.add(pubRing);
               }
            } else if (uIt.next().equals(userID)) {
               rings.add(pubRing);
            }
         }
      }

      return rings.iterator();
   }

   public Iterator getKeyRings(String userID) throws PGPException {
      return this.getKeyRings(userID, false);
   }

   public PGPPublicKey getPublicKey(long keyID) throws PGPException {
      Iterator it = this.getKeyRings();

      while(it.hasNext()) {
         PGPPublicKeyRing pubRing = (PGPPublicKeyRing)it.next();
         PGPPublicKey pub = pubRing.getPublicKey(keyID);
         if (pub != null) {
            return pub;
         }
      }

      return null;
   }

   public PGPPublicKeyRing getPublicKeyRing(long keyID) throws PGPException {
      Long id = new Long(keyID);
      if (this.pubRings.containsKey(id)) {
         return (PGPPublicKeyRing)this.pubRings.get(id);
      } else {
         Iterator it = this.getKeyRings();

         while(it.hasNext()) {
            PGPPublicKeyRing pubRing = (PGPPublicKeyRing)it.next();
            PGPPublicKey pub = pubRing.getPublicKey(keyID);
            if (pub != null) {
               return pubRing;
            }
         }

         return null;
      }
   }

   public byte[] getEncoded() throws IOException {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      this.encode(bOut);
      return bOut.toByteArray();
   }

   public void encode(OutputStream outStream) throws IOException {
      BCPGOutputStream out;
      if (outStream instanceof BCPGOutputStream) {
         out = (BCPGOutputStream)outStream;
      } else {
         out = new BCPGOutputStream(outStream);
      }

      Iterator it = this.order.iterator();

      while(it.hasNext()) {
         PGPPublicKeyRing sr = (PGPPublicKeyRing)this.pubRings.get(it.next());
         sr.encode(out);
      }

   }

   public static PGPPublicKeyRingCollection addPublicKeyRing(PGPPublicKeyRingCollection ringCollection, PGPPublicKeyRing publicKeyRing) {
      Long key = new Long(publicKeyRing.getPublicKey().getKeyID());
      if (ringCollection.pubRings.containsKey(key)) {
         throw new IllegalArgumentException("Collection already contains a key with a keyID for the passed in ring.");
      } else {
         Map newPubRings = new HashMap(ringCollection.pubRings);
         List newOrder = new ArrayList(ringCollection.order);
         newPubRings.put(key, publicKeyRing);
         newOrder.add(key);
         return new PGPPublicKeyRingCollection(newPubRings, newOrder);
      }
   }

   public static PGPPublicKeyRingCollection removePublicKeyRing(PGPPublicKeyRingCollection ringCollection, PGPPublicKeyRing publicKeyRing) {
      Long key = new Long(publicKeyRing.getPublicKey().getKeyID());
      if (!ringCollection.pubRings.containsKey(key)) {
         throw new IllegalArgumentException("Collection already contains a key with a keyID for the passed in ring.");
      } else {
         Map newPubRings = new HashMap(ringCollection.pubRings);
         List newOrder = new ArrayList(ringCollection.order);
         newPubRings.remove(key);

         for(int i = 0; i < newOrder.size(); ++i) {
            Long r = (Long)newOrder.get(i);
            if (r == key) {
               newOrder.remove(i);
               break;
            }
         }

         return new PGPPublicKeyRingCollection(newPubRings, newOrder);
      }
   }
}
