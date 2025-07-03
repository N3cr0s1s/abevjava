package org.bouncycastle.jce.interfaces;

import java.util.Enumeration;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObjectIdentifier;

public interface PKCS12BagAttributeCarrier {
   void setBagAttribute(DERObjectIdentifier var1, DEREncodable var2);

   DEREncodable getBagAttribute(DERObjectIdentifier var1);

   Enumeration getBagAttributeKeys();
}
