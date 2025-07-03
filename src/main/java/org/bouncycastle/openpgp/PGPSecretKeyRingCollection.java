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

public class PGPSecretKeyRingCollection {
   private Map secretRings;
   private List order;

   private PGPSecretKeyRingCollection(Map secretRings, List order) {
      this.secretRings = new HashMap();
      this.order = new ArrayList();
      this.secretRings = secretRings;
      this.order = order;
   }

   public PGPSecretKeyRingCollection(byte[] encoding) throws IOException, PGPException {
      this((InputStream)(new ByteArrayInputStream(encoding)));
   }

   public PGPSecretKeyRingCollection(InputStream in) throws IOException, PGPException {
      this.secretRings = new HashMap();
      this.order = new ArrayList();
      PGPObjectFactory pgpFact = new PGPObjectFactory(in);
      Object obj = null;

      while((obj = pgpFact.nextObject()) != null) {
         if (!(obj instanceof PGPSecretKeyRing)) {
            throw new IOException(obj.getClass().getName() + " found where PGPSecretKeyRingExpected");
         }

         PGPSecretKeyRing pgpSecret = (PGPSecretKeyRing)obj;
         Long key = new Long(pgpSecret.getPublicKey().getKeyID());
         this.secretRings.put(key, pgpSecret);
         this.order.add(key);
      }

   }

   public PGPSecretKeyRingCollection(Collection collection) throws IOException, PGPException {
      this.secretRings = new HashMap();
      this.order = new ArrayList();
      Iterator it = collection.iterator();

      while(it.hasNext()) {
         PGPSecretKeyRing pgpSecret = (PGPSecretKeyRing)it.next();
         Long key = new Long(pgpSecret.getPublicKey().getKeyID());
         this.secretRings.put(key, pgpSecret);
         this.order.add(key);
      }

   }

   public int size() {
      return this.order.size();
   }

   public Iterator getKeyRings() {
      return this.secretRings.values().iterator();
   }

   public Iterator getKeyRings(String userID, boolean matchPartial) throws PGPException {
      Iterator it = this.getKeyRings();
      ArrayList rings = new ArrayList();

      while(it.hasNext()) {
         PGPSecretKeyRing secRing = (PGPSecretKeyRing)it.next();
         Iterator uIt = secRing.getSecretKey().getUserIDs();

         while(uIt.hasNext()) {
            if (matchPartial) {
               if (((String)uIt.next()).indexOf(userID) > -1) {
                  rings.add(secRing);
               }
            } else if (uIt.next().equals(userID)) {
               rings.add(secRing);
            }
         }
      }

      return rings.iterator();
   }

   public Iterator getKeyRings(String userID) throws PGPException {
      return this.getKeyRings(userID, false);
   }

   public PGPSecretKey getSecretKey(long keyID) throws PGPException {
      Iterator it = this.getKeyRings();

      while(it.hasNext()) {
         PGPSecretKeyRing secRing = (PGPSecretKeyRing)it.next();
         PGPSecretKey sec = secRing.getSecretKey(keyID);
         if (sec != null) {
            return sec;
         }
      }

      return null;
   }

   public PGPSecretKeyRing getSecretKeyRing(long keyID) throws PGPException {
      Long id = new Long(keyID);
      if (this.secretRings.containsKey(id)) {
         return (PGPSecretKeyRing)this.secretRings.get(id);
      } else {
         Iterator it = this.getKeyRings();

         while(it.hasNext()) {
            PGPSecretKeyRing secretRing = (PGPSecretKeyRing)it.next();
            PGPSecretKey secret = secretRing.getSecretKey(keyID);
            if (secret != null) {
               return secretRing;
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
         PGPSecretKeyRing sr = (PGPSecretKeyRing)this.secretRings.get(it.next());
         sr.encode(out);
      }

   }

   public static PGPSecretKeyRingCollection addSecretKeyRing(PGPSecretKeyRingCollection ringCollection, PGPSecretKeyRing secretKeyRing) {
      Long key = new Long(secretKeyRing.getPublicKey().getKeyID());
      if (ringCollection.secretRings.containsKey(key)) {
         throw new IllegalArgumentException("Collection already contains a key with a keyID for the passed in ring.");
      } else {
         Map newSecretRings = new HashMap(ringCollection.secretRings);
         List newOrder = new ArrayList(ringCollection.order);
         newSecretRings.put(key, secretKeyRing);
         newOrder.add(key);
         return new PGPSecretKeyRingCollection(newSecretRings, newOrder);
      }
   }

   public static PGPSecretKeyRingCollection removeSecretKeyRing(PGPSecretKeyRingCollection ringCollection, PGPSecretKeyRing secretKeyRing) {
      Long key = new Long(secretKeyRing.getPublicKey().getKeyID());
      if (!ringCollection.secretRings.containsKey(key)) {
         throw new IllegalArgumentException("Collection already contains a key with a keyID for the passed in ring.");
      } else {
         Map newSecretRings = new HashMap(ringCollection.secretRings);
         List newOrder = new ArrayList(ringCollection.order);
         newSecretRings.remove(key);

         for(int i = 0; i < newOrder.size(); ++i) {
            Long r = (Long)newOrder.get(i);
            if (r == key) {
               newOrder.remove(i);
               break;
            }
         }

         return new PGPSecretKeyRingCollection(newSecretRings, newOrder);
      }
   }
}
