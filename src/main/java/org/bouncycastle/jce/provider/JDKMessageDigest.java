package org.bouncycastle.jce.provider;

import java.security.MessageDigest;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;

public class JDKMessageDigest extends MessageDigest {
   Digest digest;

   protected JDKMessageDigest(Digest digest) {
      super(digest.getAlgorithmName());
      this.digest = digest;
   }

   public void engineReset() {
      this.digest.reset();
   }

   public void engineUpdate(byte input) {
      this.digest.update(input);
   }

   public void engineUpdate(byte[] input, int offset, int len) {
      this.digest.update(input, offset, len);
   }

   public byte[] engineDigest() {
      byte[] digestBytes = new byte[this.digest.getDigestSize()];
      this.digest.doFinal(digestBytes, 0);
      return digestBytes;
   }

   public static class MD5 extends JDKMessageDigest implements Cloneable {
      public MD5() {
         super(new MD5Digest());
      }

      public Object clone() throws CloneNotSupportedException {
         JDKMessageDigest.MD5 d = (JDKMessageDigest.MD5)super.clone();
         d.digest = new MD5Digest((MD5Digest)this.digest);
         return d;
      }
   }

   public static class SHA1 extends JDKMessageDigest implements Cloneable {
      public SHA1() {
         super(new SHA1Digest());
      }

      public Object clone() throws CloneNotSupportedException {
         JDKMessageDigest.SHA1 d = (JDKMessageDigest.SHA1)super.clone();
         d.digest = new SHA1Digest((SHA1Digest)this.digest);
         return d;
      }
   }

   public static class SHA224 extends JDKMessageDigest implements Cloneable {
      public SHA224() {
         super(new SHA224Digest());
      }

      public Object clone() throws CloneNotSupportedException {
         JDKMessageDigest.SHA224 d = (JDKMessageDigest.SHA224)super.clone();
         d.digest = new SHA224Digest((SHA224Digest)this.digest);
         return d;
      }
   }

   public static class SHA256 extends JDKMessageDigest implements Cloneable {
      public SHA256() {
         super(new SHA256Digest());
      }

      public Object clone() throws CloneNotSupportedException {
         JDKMessageDigest.SHA256 d = (JDKMessageDigest.SHA256)super.clone();
         d.digest = new SHA256Digest((SHA256Digest)this.digest);
         return d;
      }
   }

   public static class SHA384 extends JDKMessageDigest implements Cloneable {
      public SHA384() {
         super(new SHA384Digest());
      }

      public Object clone() throws CloneNotSupportedException {
         JDKMessageDigest.SHA384 d = (JDKMessageDigest.SHA384)super.clone();
         d.digest = new SHA384Digest((SHA384Digest)this.digest);
         return d;
      }
   }

   public static class SHA512 extends JDKMessageDigest implements Cloneable {
      public SHA512() {
         super(new SHA512Digest());
      }

      public Object clone() throws CloneNotSupportedException {
         JDKMessageDigest.SHA512 d = (JDKMessageDigest.SHA512)super.clone();
         d.digest = new SHA512Digest((SHA512Digest)this.digest);
         return d;
      }
   }
}
