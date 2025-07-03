package hu.piller.xml.abev.parser;

import hu.piller.kripto.RSACipher;
import hu.piller.tools.Base64;
import hu.piller.tools.DispatcherThread;
import hu.piller.xml.FinishException;
import hu.piller.xml.MissingKeyException;
import hu.piller.xml.abev.element.CsatolmanyInfo;
import hu.piller.xml.abev.element.DocMetaData;
import hu.piller.xml.xes.XESDocumentController;
import hu.piller.xml.xes.element.KeyInfo;
import hu.piller.xml.xes.parser.EncryptedDataHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.util.Vector;

import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;
import org.bouncycastle.crypto.SecretKey;
import org.bouncycastle.crypto.spec.SecretKeySpec;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;

public class BoritekParser3 extends XESDocumentController implements Runnable {
   public static final int OK = 0;
   public static final int KEY_DECRYPTION_FAILED = 100;
   public static final int DOC_DECRYPTION_FAILED = 200;
   public static int PARSE_ALL = 0;
   public static int PARSE_HEADER = 1;
   public static int PARSE_HEADER_KEYINFOS = 2;
   public static int PARSE_HEADER_KEYINFOS_MAINDOC = 3;
   private boolean inflate;
   private int resultCode;
   private InputStream in;
   private OutputStream out;
   private int mode;
   private PrivateKey privateKey;
   private SecretKey secretKey;
   private DocMetaData metaData;
   private Vector keyInfos;
   private BoritekHandler bh;
   private EncryptedDataHandler edh;
   private FejlecHandler fh;
   private TorzsHandler th;
   private DispatcherThread dt;
   private int attachmentCounter;
   private File destDir;
   private boolean useDefaultDirs;

   public BoritekParser3(InputStream in, OutputStream out, int mode, DispatcherThread dt) throws SAXException {
      super(XMLReaderFactory.createXMLReader());
      this.inflate = false;
      this.resultCode = 0;
      this.attachmentCounter = 1;
      this.useDefaultDirs = true;
      this.in = in;
      this.out = out;
      this.mode = mode;
      this.dt = dt;
   }

   public BoritekParser3(InputStream in, OutputStream out, int mode) throws SAXException {
      super(XMLReaderFactory.createXMLReader());
      this.inflate = false;
      this.resultCode = 0;
      this.attachmentCounter = 1;
      this.useDefaultDirs = true;
      this.in = in;
      this.out = out;
      this.mode = mode;
   }

   public BoritekParser3(InputStream in, OutputStream out, int mode, boolean inflate) throws SAXException {
      super(XMLReaderFactory.createXMLReader());
      this.inflate = false;
      this.resultCode = 0;
      this.attachmentCounter = 1;
      this.useDefaultDirs = true;
      this.in = in;
      this.out = out;
      this.mode = mode;
      this.inflate = inflate;
   }

   public BoritekParser3(InputStream in, OutputStream out) throws SAXException {
      this(in, out, PARSE_HEADER_KEYINFOS_MAINDOC);
   }

   public BoritekParser3(InputStream in, OutputStream out, boolean inflate) throws SAXException {
      this(in, out, PARSE_HEADER_KEYINFOS_MAINDOC, inflate);
   }

   public void start() throws IOException, FinishException, MissingKeyException {
      BoritekHandler bh = new BoritekHandler(this);
      this.reader.setContentHandler(bh);

      try {
         this.reader.parse(new InputSource(this.in));
      } catch (MissingKeyException var3) {
         if (this.in != null) {
            this.in.close();
         }

         if (this.out != null) {
            this.out.close();
         }

         throw var3;
      } catch (FinishException var4) {
         if (this.in != null) {
            this.in.close();
         }

         if (this.out != null) {
            this.out.close();
         }

         throw var4;
      } catch (Exception var5) {
         var5.printStackTrace();
         System.exit(-1);
      }

   }

   void setDocMetaData(DocMetaData metaData) throws FinishException {
      this.metaData = metaData;
   }

   public void addKeyInfo(KeyInfo keyInfo) {
      this.getKeyInfos().add(keyInfo);
      if (this.secretKey == null) {
         this.secretKey = this.decryptSecretKey(keyInfo);
      }

   }

   private SecretKey decryptSecretKey(KeyInfo keyInfo) {
      if (keyInfo.getEncKey() == null) {
         return null;
      } else {
         SecretKey secretKey;
         return (secretKey = this.decryptSecretKey(Base64.decode(keyInfo.getEncKey().getCipherValue().getBytes()))) != null ? secretKey : null;
      }
   }

   private SecretKey decryptSecretKey(byte[] encSecretKey) {
      try {
         byte[] secretKeyData = RSACipher.decryptData(this.privateKey, encSecretKey);
         if (secretKeyData.length != 16 && secretKeyData.length != 32) {
            return null;
         } else {
            SecretKey secretKey = new SecretKeySpec(secretKeyData, "AES");
            return secretKey;
         }
      } catch (Exception var4) {
         return null;
      }
   }

   public void setSecretKey(SecretKey secretKey) {
      this.secretKey = secretKey;
   }

   public void setPrivateKey(PrivateKey privateKey) {
      this.privateKey = privateKey;
   }

   public DocMetaData getMetaData() {
      return this.metaData == null && this.fh != null ? this.fh.getDocMetaData() : this.metaData;
   }

   public Vector getKeyInfos() {
      if (this.keyInfos == null) {
         this.keyInfos = new Vector();
      }

      return this.keyInfos;
   }

   public void setResultCode(int resultCode) {
      if (this.resultCode == 0) {
         this.resultCode = resultCode;
      }

   }

   public int getResultCode() {
      return this.resultCode;
   }

   public InputStream getNextAttachment() {
      return null;
   }

   public boolean hasNextAttachment() {
      return false;
   }

   public void setMode(int mode) {
      this.mode = mode;
   }

   public int getMode() {
      return this.mode;
   }

   public void run() {
      try {
         BoritekHandler bh = new BoritekHandler(this);
         this.reader.setContentHandler(bh);
         if (this.in != null) {
            this.reader.parse(new InputSource(this.in));
         }
      } catch (FinishException var2) {
      } catch (SAXException var3) {
         var3.printStackTrace();
      } catch (IllegalMonitorStateException var4) {
         var4.printStackTrace();
      } catch (FileNotFoundException var5) {
         var5.printStackTrace();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public void askOutputStream(EncryptedDataHandler edh) {
      if (this.dt != null) {
         this.dt.suspendDispatch();
      }

      if (this.out != null) {
         edh.setOutputStream(this.out);
      } else {
         String nyomFileNev = "nyomtatvany";
         if (this.useDefaultDirs && this.metaData != null && this.metaData.getFileNev() != null) {
            nyomFileNev = this.metaData.getFileNev();
         }

         try {
            edh.setOutputStream(new NecroFileOutputStream(new NecroFile(this.destDir, nyomFileNev)));
         } catch (FileNotFoundException var4) {
            var4.printStackTrace();
         }
      }

      if (this.dt != null) {
         this.dt.restartDispatch();
      }

   }

   public void askOutputStream(CsatolmanyHandler csh, String csatAzon) throws IOException {
      if (this.dt != null) {
         this.dt.suspendDispatch();
      }

      try {
         String csatFileNev = "csatolmany_" + this.attachmentCounter++ + ".xml";
         String nyomFileNev = "nyomtatvany";
         File csatDir = this.destDir;
         if (this.useDefaultDirs) {
            if (this.metaData != null && this.metaData.getFileNev() != null) {
               nyomFileNev = this.metaData.getFileNev();
            }

            csatDir = new NecroFile(this.destDir, nyomFileNev + "_csatolmanyai");
            if (!csatDir.exists()) {
               csatDir.mkdir();
            } else if (!csatDir.isDirectory() || !csatDir.canWrite()) {
               throw new IOException(csatDir + " not directory or cannot be written");
            }

            if (this.metaData.getCsatInfoLista() != null) {
               csatFileNev = ((CsatolmanyInfo)this.metaData.getCsatInfoLista().get(csatAzon)).getFileNev();
            }
         }

         csh.setOutputStream(new NecroFileOutputStream(new NecroFile(csatDir, csatFileNev)));
         if (this.dt != null) {
            this.dt.restartDispatch();
         }
      } catch (FileNotFoundException var6) {
         var6.printStackTrace();
      }

   }

   public void askSecretKey(EncryptedDataHandler edh) throws MissingKeyException {
      if (this.secretKey == null) {
         throw new MissingKeyException("missing symmetric key");
      } else {
         if (this.dt != null) {
            this.dt.suspendDispatch();
         }

         edh.setSecretKey(this.secretKey);
         if (this.dt != null) {
            this.dt.restartDispatch();
         }

      }
   }

   public void askSecretKey(CsatolmanyHandler csh) throws MissingKeyException {
      if (this.secretKey == null) {
         throw new MissingKeyException("missing symmetric key");
      } else {
         if (this.dt != null) {
            this.dt.suspendDispatch();
         }

         csh.setSecretKey(this.secretKey);
         if (this.dt != null) {
            this.dt.restartDispatch();
         }

      }
   }

   public File getDestDir() {
      return this.destDir;
   }

   public void setDestDir(String dir) throws IOException {
      this.destDir = new NecroFile(dir);
      if (!this.destDir.exists() && !this.destDir.mkdir()) {
         throw new IOException(dir + " not exist, cannot be created");
      } else if (!this.destDir.isDirectory()) {
         throw new IOException(dir + " not a directory");
      } else if (!this.destDir.canWrite()) {
         throw new IOException(dir + " not writeable");
      }
   }

   public boolean isUseDefaultDirs() {
      return this.useDefaultDirs;
   }

   public void setUseDefaultDirs(boolean useDefaultDirs) {
      this.useDefaultDirs = useDefaultDirs;
   }

   public boolean isInflate() {
      return this.inflate;
   }
}
