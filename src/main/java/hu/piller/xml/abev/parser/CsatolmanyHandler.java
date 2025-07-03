package hu.piller.xml.abev.parser;

import hu.piller.xml.XMLElemHandler;
import hu.piller.xml.xes.XESDocumentController;
import hu.piller.xml.xes.parser.EncryptedDataHandler;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.crypto.SecretKey;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CsatolmanyHandler extends XMLElemHandler {
   private OutputStream out;
   private SecretKey sk;
   private String csatAzon;

   public CsatolmanyHandler(XMLElemHandler parent, XESDocumentController parser, String csatAzon) {
      super(parent, parser);
      this.csatAzon = csatAzon;
   }

   public synchronized void setOutputStream(OutputStream out) {
      this.out = out;
      this.notify();
   }

   public synchronized void setSecretKey(SecretKey sk) {
      this.sk = sk;
      this.notify();
   }

   public void startElement(String namespaceURI, String localName, String qName, Attributes attrs) throws SAXException {
      this.contents.reset();
      if (localName.equals("EncryptedData")) {
         if (this.sk == null && this.parser instanceof BoritekParser3) {
            ((BoritekParser3)this.parser).askSecretKey(this);
         }

         if (this.out == null && this.parser instanceof BoritekParser3) {
            try {
               ((BoritekParser3)this.parser).askOutputStream(this, this.csatAzon);
            } catch (IOException var7) {
               throw new SAXException(var7);
            }
         }
      }

      try {
         this.parser.getReader().setContentHandler(new EncryptedDataHandler(this.parent, (XESDocumentController)this.parser, this.out));
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
      if (localName.equals("Csatolmany")) {
         try {
            this.out.flush();
            this.out.close();
            this.out = null;
         } catch (IOException var5) {
            var5.printStackTrace();
         }

         this.parser.getReader().setContentHandler(this.parent);
      }

   }
}
