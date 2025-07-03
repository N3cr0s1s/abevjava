package org.bouncycastle.crypto.spec;

import java.security.spec.KeySpec;

public class PBEKeySpec implements KeySpec {
   private char[] password;
   private byte[] salt;
   private int iterationCount;
   private int keyLength;

   public PBEKeySpec(char[] password) {
      this.password = (char[])password.clone();
   }

   public PBEKeySpec(char[] password, byte[] salt, int iterationCount, int keyLength) {
      this(password == null ? new char[0] : password);
      if (salt.length != 0 && iterationCount >= 0 && keyLength >= 0) {
         this.salt = (byte[])salt.clone();
      } else {
         throw new IllegalArgumentException();
      }
   }

   public PBEKeySpec(char[] password, byte[] salt, int iterationCount) {
      this(password == null ? new char[0] : password);
      if (salt.length != 0 && iterationCount >= 0) {
         this.salt = (byte[])salt.clone();
      } else {
         throw new IllegalArgumentException();
      }
   }

   public final void clearPassword() {
      this.password = null;
   }

   public final char[] getPassword() {
      if (this.password == null) {
         throw new IllegalStateException();
      } else {
         return (char[])this.password.clone();
      }
   }

   public final byte[] getSalt() {
      return this.salt == null ? null : (byte[])this.salt.clone();
   }

   public final int getIterationCount() {
      return this.iterationCount;
   }

   public final int getKeyLength() {
      return this.keyLength;
   }
}
