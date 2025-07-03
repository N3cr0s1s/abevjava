package org.bouncycastle.asn1;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class DERGeneralizedTime extends DERObject {
   String time;

   public static DERGeneralizedTime getInstance(Object obj) {
      if (obj != null && !(obj instanceof DERGeneralizedTime)) {
         if (obj instanceof ASN1OctetString) {
            return new DERGeneralizedTime(((ASN1OctetString)obj).getOctets());
         } else {
            throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
         }
      } else {
         return (DERGeneralizedTime)obj;
      }
   }

   public static DERGeneralizedTime getInstance(ASN1TaggedObject obj, boolean explicit) {
      return getInstance(obj.getObject());
   }

   public DERGeneralizedTime(String time) {
      this.time = time;
   }

   public DERGeneralizedTime(Date time) {
      SimpleDateFormat dateF = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
      dateF.setTimeZone(new SimpleTimeZone(0, "Z"));
      this.time = dateF.format(time);
   }

   DERGeneralizedTime(byte[] bytes) {
      char[] dateC = new char[bytes.length];

      for(int i = 0; i != dateC.length; ++i) {
         dateC[i] = (char)(bytes[i] & 255);
      }

      this.time = new String(dateC);
   }

   public String getTime() {
      if (this.time.charAt(this.time.length() - 1) == 'Z') {
         return this.time.substring(0, this.time.length() - 1) + "GMT+00:00";
      } else {
         int signPos = this.time.length() - 5;
         char sign = this.time.charAt(signPos);
         if (sign != '-' && sign != '+') {
            signPos = this.time.length() - 3;
            sign = this.time.charAt(signPos);
            return sign != '-' && sign != '+' ? this.time : this.time.substring(0, signPos) + "GMT" + this.time.substring(signPos) + ":00";
         } else {
            return this.time.substring(0, signPos) + "GMT" + this.time.substring(signPos, signPos + 3) + ":" + this.time.substring(signPos + 3);
         }
      }
   }

   public Date getDate() throws ParseException {
      SimpleDateFormat dateF;
      if (this.time.indexOf(46) == 14) {
         dateF = new SimpleDateFormat("yyyyMMddHHmmss.SSS'Z'");
      } else {
         dateF = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
      }

      dateF.setTimeZone(new SimpleTimeZone(0, "Z"));
      return dateF.parse(this.time);
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
      out.writeEncoded(24, this.getOctets());
   }

   public boolean equals(Object o) {
      return o != null && o instanceof DERGeneralizedTime ? this.time.equals(((DERGeneralizedTime)o).time) : false;
   }

   public int hashCode() {
      return this.time.hashCode();
   }
}
