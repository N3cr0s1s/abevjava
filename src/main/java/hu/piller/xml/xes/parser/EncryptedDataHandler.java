package hu.piller.xml.xes.parser;

import hu.piller.kripto.AESCipher;
import hu.piller.xml.FinishException;
import hu.piller.xml.MissingKeyException;
import hu.piller.xml.XMLElemHandler;
import hu.piller.xml.abev.parser.BoritekParser3;
import hu.piller.xml.xes.XESDocumentController;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Vector;
import org.bouncycastle.crypto.SecretKey;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EncryptedDataHandler extends XMLElemHandler {
   private Vector tagPath;
   private OutputStream out;
   private SecretKey sk;
   private PipedInputStream pin;
   private OutputStreamWriter writer;
   private boolean decrypting;
   private boolean inflate;
   private Thread thread;
   private long threadTimeOut;

   public EncryptedDataHandler(XMLElemHandler parent, XESDocumentController parser) throws IOException {
      super(parent, parser);
      this.decrypting = false;
      this.inflate = false;
      this.threadTimeOut = 5L;
      this.tagPath = new Vector();
      PipedOutputStream pout = new PipedOutputStream();
      this.writer = new OutputStreamWriter(pout);
      this.pin = new PipedInputStream(pout);
      if (parser instanceof BoritekParser3) {
         this.inflate = ((BoritekParser3)parser).isInflate();
      }

   }

   public EncryptedDataHandler(XMLElemHandler parent, XESDocumentController parser, OutputStream out, SecretKey sk) throws IOException {
      this(parent, parser);
      this.out = out;
      this.sk = sk;
   }

   public EncryptedDataHandler(XMLElemHandler parent, XESDocumentController parser, OutputStream out) throws IOException {
      this(parent, parser);
      this.out = out;
   }

   public EncryptedDataHandler(XMLElemHandler parent, XESDocumentController parser, OutputStream out, SecretKey sk, boolean inflate) throws IOException {
      this(parent, parser, out, sk);
      this.inflate = inflate;
   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      try {
         if (this.decrypting) {
            this.writer.write(ch, start, length);
            this.writer.flush();
         }

      } catch (IOException var5) {
         throw new SAXException(var5.toString());
      }
   }

   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
      this.tagPath.add(localName);
      if (localName.equals("KeyInfo")) {
         this.parser.getReader().setContentHandler(new KeyInfoHandler(this, (XESDocumentController)this.parser));
      }

      if (localName.equals("CipherData")) {
         if (this.parser instanceof BoritekParser3 && ((BoritekParser3)this.parser).getMode() == BoritekParser3.PARSE_HEADER_KEYINFOS) {
            throw new FinishException("parse_header_keyinfos ready");
         }

         if (this.sk == null && this.parser instanceof BoritekParser3) {
            ((BoritekParser3)this.parser).askSecretKey(this);
         }

         if (this.out == null && this.parser instanceof BoritekParser3) {
            ((BoritekParser3)this.parser).askOutputStream(this);
         }
      }

      if (localName.equals("CipherValue")) {
         if (this.sk == null) {
            throw new MissingKeyException("missing symmetric key");
         }

         this.decrypting = true;
         this.thread = new Thread(new Runnable() {
            public void run() {
               try {
                  if (!EncryptedDataHandler.this.inflate) {
                     AESCipher.decryptStream(EncryptedDataHandler.this.sk, EncryptedDataHandler.this.pin, EncryptedDataHandler.this.out, true);
                  } else {
                     AESCipher.decryptInflateStream(EncryptedDataHandler.this.sk, EncryptedDataHandler.this.pin, EncryptedDataHandler.this.out, true);
                  }
               } catch (Exception var2) {
                  ((BoritekParser3)EncryptedDataHandler.this.parser).setResultCode(200);
                  var2.printStackTrace();
               }

            }
         });
         this.thread.start();
      }

   }

   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
      if (localName.equals("CipherValue")) {
         this.waitDecryptThread();
         if (this.parser instanceof BoritekParser3 && ((BoritekParser3)this.parser).getMode() == BoritekParser3.PARSE_HEADER_KEYINFOS_MAINDOC) {
            throw new FinishException("ready");
         }
      }

      if (localName.equals("EncryptedData")) {
         this.parser.getReader().setContentHandler(this.parent);
      }

   }

   public void waitDecryptThread() throws SAXException {
      if (this.thread != null && this.thread.isAlive()) {
         try {
            this.writer.flush();
            this.writer.close();
            this.thread.join(this.threadTimeOut * 1000L);
            this.decrypting = false;
            if (this.parser instanceof BoritekParser3) {
               ((BoritekParser3)this.parser).setResultCode(0);
            }
         } catch (IOException var11) {
            var11.printStackTrace();
            throw new SAXException(var11.toString());
         } catch (InterruptedException var12) {
            var12.printStackTrace();
         } finally {
            try {
               if (this.out != null) {
                  this.out.close();
               }

               if (this.pin != null) {
                  this.pin.close();
               }
            } catch (IOException var10) {
               var10.printStackTrace();
            }

         }
      }

   }

   public synchronized void setOutputStream(OutputStream out) {
      this.out = out;
      this.notify();
   }

   public synchronized void setSecretKey(SecretKey secretKey) {
      this.sk = secretKey;
      this.notify();
   }
}
