package org.bouncycastle.asn1;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class DERUTCTime extends DERObject {
   String time;

   public static DERUTCTime getInstance(Object obj) {
      if (obj != null && !(obj instanceof DERUTCTime)) {
         if (obj instanceof ASN1OctetString) {
            return new DERUTCTime(((ASN1OctetString)obj).getOctets());
         } else {
            throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
         }
      } else {
         return (DERUTCTime)obj;
      }
   }

   public static DERUTCTime getInstance(ASN1TaggedObject obj, boolean explicit) {
      return getInstance(obj.getObject());
   }

   public DERUTCTime(String time) {
      this.time = time;
   }

   public DERUTCTime(Date time) {
      SimpleDateFormat dateF = new SimpleDateFormat("yyMMddHHmmss'Z'");
      dateF.setTimeZone(new SimpleTimeZone(0, "Z"));
      this.time = dateF.format(time);
   }

   DERUTCTime(byte[] bytes) {
      char[] dateC = new char[bytes.length];

      for(int i = 0; i != dateC.length; ++i) {
         dateC[i] = (char)(bytes[i] & 255);
      }

      this.time = new String(dateC);
   }

   public String getTime() {
      if (this.time.length() == 11) {
         return this.time.substring(0, 10) + "00GMT+00:00";
      } else if (this.time.length() == 13) {
         return this.time.substring(0, 12) + "GMT+00:00";
      } else {
         return this.time.length() == 17 ? this.time.substring(0, 12) + "GMT" + this.time.substring(12, 15) + ":" + this.time.substring(15, 17) : this.time;
      }
   }

   public String getAdjustedTime() {
      String d = this.getTime();
      return d.charAt(0) < '5' ? "20" + d : "19" + d;
   }

   private byte[] getOctets() {
      char[] cs = this.time.toCharArray();
      byte[] bs = new byte[cs.length];

      for(int i = 0; i != cs.length; ++i) {
         bs[i] = (byte)cs[i];
      }

      return bs;
   }

   void encode(DEROutputStream out) throws IOException {
      out.writeEncoded(23, this.getOctets());
   }

   public boolean equals(Object o) {
      return o != null && o instanceof DERUTCTime ? this.time.equals(((DERUTCTime)o).time) : false;
   }

   public int hashCode() {
      return this.time.hashCode();
   }
}
