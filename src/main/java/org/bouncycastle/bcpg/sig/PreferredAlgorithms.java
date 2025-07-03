package org.bouncycastle.bcpg.sig;

import org.bouncycastle.bcpg.SignatureSubpacket;

public class PreferredAlgorithms extends SignatureSubpacket {
   private static final byte[] intToByteArray(int[] v) {
      byte[] data = new byte[v.length];

      for(int i = 0; i != v.length; ++i) {
         data[i] = (byte)v[i];
      }

      return data;
   }

   public PreferredAlgorithms(int type, boolean critical, byte[] data) {
      super(type, critical, data);
   }

   public PreferredAlgorithms(int type, boolean critical, int[] preferrences) {
      super(type, critical, intToByteArray(preferrences));
   }

   /** @deprecated */
   public int[] getPreferrences() {
      return this.getPreferences();
   }

   public int[] getPreferences() {
      int[] v = new int[this.data.length];

      for(int i = 0; i != v.length; ++i) {
         v[i] = this.data[i] & 255;
      }

      return v;
   }
}
