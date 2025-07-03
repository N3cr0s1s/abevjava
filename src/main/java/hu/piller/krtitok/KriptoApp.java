package hu.piller.krtitok;

import hu.piller.kripto.keys.KeyManager;
import hu.piller.kripto.keys.KeyWrapper;
import hu.piller.kripto.keys.StoreManager;
import hu.piller.kripto.keys.StoreWrapper;
import hu.piller.krtitok.gui.FKriptodsk;
import hu.piller.krtitok.tools.log.Logger;
import hu.piller.tools.GeneralException;
import hu.piller.tools.Utils;
import hu.piller.tools.bzip2.DeflatorThread;
import hu.piller.xml.FinishException;
import hu.piller.xml.MissingKeyException;
import hu.piller.xml.abev.BoritekBuilder;
import hu.piller.xml.abev.element.DocMetaData;
import hu.piller.xml.abev.parser.BoritekParser3;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.Vector;
import org.bouncycastle.crypto.BadPaddingException;
import org.bouncycastle.crypto.Cipher;
import org.bouncycastle.crypto.IllegalBlockSizeException;
import org.bouncycastle.crypto.NoSuchPaddingException;
import org.bouncycastle.openpgp.PGPException;

public class KriptoApp {
   public static KriptoApp instance;
   public static HashMap kepek;
   private static final String usage = "Alkalmazas hasznalata:\n krtitok.exe <opciok>\n    -help               - segitseg a program hasznalatahoz\n    -cmd arg            - parancs (arg lehet: keygen vagy encrypt vagy decrypt vagy test)\n    -debug              - debug uzenetek kiirasa\n    -nozip              - ne hasznaljon tomoritest (bzip)\n    -silent             - ne irjon semmit a kimenetre\n    -mf arg             - metaallomany\n    -src arg            - forrasallomany (titkositando/kititkositando)\n    -dest arg           - celallomany (titkositott/kititkositott allomany)\n    -keylen arg         - kulcshossz (kulcsgeneralashoz, 1024/2048)\n    -keypass arg        - kulcsjelszo\n    -alias arg          - kulcsalias\n    -seckey arg         - titkositott (es nyilvanos) kulcsot tartalmazo\n    -pubkey arg         - csak nyilvanos kulcsot tartalmazo allomany\n    -store arg          - kulcstar\n    -storetype arg      - kulcstar tipusa (PGP, ez az alapertelmezett)\n    -storepass arg      - kulcstarat vedo jelszo\n    -recipient arg      - titkositaskor lehet megadni a cimzett publikus kulcsat tartalmazo allomanyt (egy db \n                          PGP publikus kulcsot vagy egy db X509 tanusitvany-t tartalmazo allomany eleresi ut)\n    -cimzett arg        - metaallomany <abev:Cimzett>, akinek a postafiokjaba kuldjuk az uzenetet\n    -docid arg\n    -doktipazon arg     - metaadat: <abev:DokTipusAzonosito>\n    -doktipleiras arg   - metaadat: <abev:DokTipusLeiras>\n    -doktipver arg      - metaadat: <abev:DokTipusVerzio>\n    -filenev arg        - metaadat: <abev:FileNev>\n    -megjegyzes arg     - metaadat: <abev:Megjegyzes>\n    -param arg1=arg2    - metaadat: <abev:Parameter Nev=\"arg1\" Ertek=\"arg2\"/>\n";
   public static final String version = "1.3.1";
   private static Hashtable startArgs = new Hashtable();
   private static final String ARG_CMD = "-cmd";
   private String cmd;
   private static final String ARG_HELP = "-help";
   private static final String ARG_DEBUG = "-debug";
   public static boolean debug = false;
   private static final String ARG_NOZIP = "-nozip";
   private boolean zip = true;
   private static final String ARG_SILENT = "-silent";
   private boolean silent;
   private static final String ARG_DOKAZON = "-docid";
   private String docid;
   private static final String ARG_METAFILE = "-mf";
   private String mf;
   private static final String ARG_SRCFILE = "-src";
   private String src;
   private static final String ARG_DESTFILE = "-dest";
   private String dest;
   private static final String ARG_KEYLENGTH = "-keylen";
   private int keyLen;
   private static final String ARG_KEYPASS = "-keypass";
   private char[] keypass;
   private static final String ARG_KEYALIAS = "-alias";
   private String keyAlias;
   private static final String ARG_SECRETKEYFILE = "-seckey";
   private String secretKeyFile;
   private static final String ARG_PUBLICKEYFILE = "-pubkey";
   private String publicKeyFile;
   private static final String ARG_STORE = "-store";
   private String store;
   private static final String ARG_STORETYPE = "-storetype";
   private String storeType;
   private static final String ARG_STOREPASS = "-storepass";
   private String storePass;
   private static final String ARG_RECIPIENT = "-recipient";
   private static final String ARG_CIMZETT = "-cimzett";
   private String[] cimzett;
   private static final String ARG_DOKTAIPUSAZONOSITO = "-doktipazon";
   private String dokTipusAzonosito;
   private static final String ARG_DOKTIPLEIRAS = "-doktipleiras";
   private String dokTipusLeiras;
   private static final String ARG_DOKTIPVERZIO = "-doktipver";
   private String dokTipVerzio;
   private static final String ARG_FILENEV = "-filenev";
   private String fileNev;
   private static final String ARG_MEGJEGYZES = "-megjegyzes";
   private String megjegyzes;
   private static final String ARG_PARAM = "-param";
   private static final String RECIPIENT_APEH = "APEH";
   private static final String APEH_PUB_KEY = "-----BEGIN PGP PUBLIC KEY BLOCK-----\nVersion: BCPG v1.33\n\nmQELBERPc9gBCADTQHC7WsUtf2n/GkS6DrqYi2Cd4gB2vhdY9hQT4ShOYxJ96M12\nVisyc/4fIn+PsBKkG8tpXPfDs6PLEX349PXOsk0il1PDJ9joEN5a1FPPkmw2r/2D\nFjvJ2gpHVjNXsrlgBo6ongWgCaQ9VblnNqu/SuRxBn167n3sZLqba5znkfiXQpcp\nwMjBTfl0paXMKH4qY+s1UMOLbuoSMngtrPdSZYFpPkvi9Zr2QZ4gyw5eAHH7OLMI\nvjZdjh4U7e6EuvukVxsEkvAiqWAPh8yxIBf2VGSViiNpOJ3gkM8kEtRtbZOB818r\nwl1pcn8gFFe4SJw1x3awH3c+0W9x4g+H/RA/AAYptBNBUEVIIDxlYmV2QGFwZWgu\naHU+iQE2BBMBAgAgBQJET3PYAhsDBgsJCAcDAgQVAggDBBYCAwECHgECF4AACgkQ\nQ1Dz7lBclTapPQgAoh0um31+lUJDES7gS79Jt9PM0pBBYcP2GaLvYERHgGDo1zEC\nw6Lop3F+5YBVWxnwurf8+iCUe/tWL7goCxWThW/jpQKqf9ujX+PV4DggljQZ4qux\necMUCVHsHFRU6MsCGIBhk/Lledbi3P7G3+b9UN27E4geKrJ7MtEYTtXQxGbioXBI\nIhvzhrJIkejUW8QN5EAxC5PzZE9PhvjjSlIy2TWoZcnl/bnCSfI9qKR4rDejN8MY\nxh9cn/vWhORtsvvU8w37Xdfgcztb9dDHCIGl/sHt6xsyHIrJjsrB1MSW/pCgZB61\ns/HDYdeUFe/2Nr9cVe0l9ePuw0x5PxIyUuuiHw==\n=y5M0\n-----END PGP PUBLIC KEY BLOCK-----";
   private static final String CMD_TITKOSIT = "titkosit";
   private static final String CMD_ENCRYPT = "encrypt";
   private static final String CMD_DECRYPT = "decrypt";
   private static final String CMD_KEYGEN = "keygen";
   private static final String CMD_TEST = "test";
   private static final String[] CMDS = new String[]{"decrypt", "encrypt", "keygen", "titkosit"};
   private static final String KEYLEN_1024 = "1024";
   private static final String KEYLEN_2048 = "2048";
   private static final String[] KEYLENS = new String[]{"2048", "1024"};
   private static final String KEYLEN_DEFAULT = "1024";
   public static final String STORETYPE_P12 = "P12";
   public static final String STORETYPE_PGP = "PGP";
   public static final String STORETYPE_JKS = "JKS";
   public static final String[] STORETYPES = new String[]{"JKS", "PGP"};
   public static final String STORETYPE_DEFAULT = "PGP";
   public static final String PROP_USERNAME = "user.name";
   public static final String PROP_USERHOME = "user.home";
   public static final String EXT_KR = ".kr";
   public static final String EXT_MF = ".mf";
   public static final String EXT_DEC = ".dec";
   public static final String EXT_ABV = ".abv";
   public static final String EXT_DAT = ".dat";
   public static final String EXT_XML = ".xml";
   public static final int EXIT_OK = 0;
   public static final int EXIT_ENCR_FAILED = -1;
   public static final int EXIT_LOG_FAILED = 2;
   public static final int EXIT_INVALID_CMD = 10;
   public static final int EXIT_MF_NOTEXIST = 20;
   public static final int EXIT_MF_NOTFILE = 21;
   public static final int EXIT_MF_CANNOTREAD = 22;
   public static final int EXIT_SRC_NOTEXIST = 30;
   public static final int EXIT_SRC_NOTFILE = 31;
   public static final int EXIT_SRC_CANNOTREAD = 32;
   public static final int EXIT_DEST_NOTEXIST = 40;
   public static final int EXIT_DEST_NOTFILE = 41;
   public static final int EXIT_DEST_CANNOTREAD = 42;
   public static final int EXIT_DEST_CANNOTWRITE = 43;
   public static final int EXIT_TMP_NOTEXIST = 50;
   public static final int EXIT_TMP_NOTFILE = 51;
   public static final int EXIT_TMP_CANNOTREAD = 52;
   public static final int EXIT_TMP_CANNOTWRITE = 53;
   public static final int EXIT_RENAME_FAILED = 70;
   public static final int EXIT_SECKEY_CANNOTWRITE = 83;
   public static final int EXIT_PUBKEY_CANNOTWRITE = 93;
   public static final int EXIT_UNSUPPORTED_STORETYPE = 100;
   public static final int EXIT_MISSING_STARTARG = 110;
   public static final int EXIT_UNKNOWN_STARTARG = 111;
   public static final int EXIT_PROVIDER_FAILED = 120;
   public static final String USER_HOME = System.getProperty("user.home");
   public static final String USER_NAME = System.getProperty("user.name");
   private static String USER_KEY_PATH_PUB;
   private static String USER_KEY_PATH_PRI;
   private static Boolean USER_KEY_AUTOMATIC_USE;
   public static final Logger logger;
   public static final String logFileName = ".krtitok.log";
   public static final String iniFileName = ".krtitok.ini";
   public static final String logResourceFileName = "hu.piller.krtitok.resources.msgs_hu";
   public static final String resourceFileName = "hu.piller.krtitok.resources.msgs_hu";
   public static PropertyResourceBundle resources;
   public static final String ENV_VAR_KRDIR = "KRDIR";
   private static String KRDIR;
   private static String KRDIR_TITKOSITATLAN;
   private static String KRDIR_LETOLTOTT;
   private static String KRDIR_KULDENDO;
   private static String KRDIR_ELKULDOTT;
   private static Vector PATH_KRDIR;
   private static Vector PATH_KRDIR_TITKOSITATLAN;
   private static Vector PATH_KRDIR_LETOLTOTT;
   private static Vector PATH_KRDIR_KULDENDO;
   private static Vector PATH_KRDIR_ELKULDOTT;
   private static Vector LAST_PATH_KRDIR_TITKOSITATLAN;
   private static Vector LAST_PATH_KRDIR_LETOLTOTT;
   private static Vector LAST_PATH_KRDIR_KULDENDO;
   private static Vector LAST_PATH_LAST_KRDIR_ELKULDOTT;
   private int exitCode;
   private FKriptodsk fKriptodskX;

   static {
      logger = new Logger(USER_HOME + File.separator + ".krtitok.log");
      readKRDIR();
      loadConfig();
   }

   public int getExitCode() {
      return this.exitCode;
   }

   public void setExitCode(int exitCode) {
      this.exitCode = exitCode;
   }

   private void parseCmd(String[] args) {
      Properties params = new Properties();
      Vector recipients = new Vector();
      startArgs.put("-cmd", "");
      startArgs.put("-docid", "");
      startArgs.put("-cimzett", "");
      startArgs.put("-doktipazon", "");
      startArgs.put("-doktipleiras", "");
      startArgs.put("-doktipver", "");
      startArgs.put("-filenev", "");
      startArgs.put("-megjegyzes", "");
      startArgs.put("-mf", "");
      startArgs.put("-src", "");
      startArgs.put("-dest", "");
      startArgs.put("-keylen", "");
      startArgs.put("-keypass", "");
      startArgs.put("-store", "");
      startArgs.put("-storetype", "");
      startArgs.put("-storepass", "");
      startArgs.put("-alias", "");
      startArgs.put("-seckey", "");
      startArgs.put("-pubkey", "");

      String argName;
      String argValue;
      String paramValue;
      for(int k = 0; k < args.length; ++k) {
         if (startArgs.containsKey(args[k])) {
            argName = args[k];
            ++k;
            argValue = args[k];
            if (argName.equalsIgnoreCase("-param")) {
               int sepInd = argValue.indexOf(61);
               paramValue = argValue.substring(0, sepInd);
               ++sepInd;
               String paramValue1 = argValue.substring(sepInd);
               params.put(paramValue1, paramValue1);
            } else if (argName.equalsIgnoreCase("-recipient")) {
               recipients.add(argValue);
            } else {
               startArgs.put(argName, argValue);
            }
         } else if (args[k].equalsIgnoreCase("-debug")) {
            debug = true;
         } else {
            if (!args[k].equalsIgnoreCase("-nozip")) {
               if (args[k].equalsIgnoreCase("-help")) {
                  printUsage();
                  this.setExitCode(0);
                  return;
               }

               this.setExitCode(111);
               return;
            }

            this.zip = false;
         }

         startArgs.put("-param", params);
         startArgs.put("-recipient", recipients);
      }

      if (debug) {
         Enumeration keys = startArgs.keys();

         while(true) {
            while(keys.hasMoreElements()) {
               argName = (String)keys.nextElement();
               Enumeration en;
               if (argName.equalsIgnoreCase("-param")) {
                  params = (Properties)startArgs.get("-param");
                  en = params.keys();

                  while(en.hasMoreElements()) {
                     String paramName = (String)en.nextElement();
                     paramValue = params.getProperty(paramName);
                     System.out.println("param: " + paramName + " value: " + paramValue);
                  }
               } else if (argName.equalsIgnoreCase("-recipient")) {
                  recipients = (Vector)startArgs.get("-recipient");
                  en = recipients.elements();

                  while(en.hasMoreElements()) {
                     System.out.println("cimzett: " + en.nextElement());
                  }
               } else {
                  argValue = (String)startArgs.get(argName);
                  System.out.println("arg: " + argName + " value: " + argValue);
               }
            }

            return;
         }
      }
   }

   public static void printUsage() {
      System.out.println("Alkalmazas hasznalata:\n krtitok.exe <opciok>\n    -help               - segitseg a program hasznalatahoz\n    -cmd arg            - parancs (arg lehet: keygen vagy encrypt vagy decrypt vagy test)\n    -debug              - debug uzenetek kiirasa\n    -nozip              - ne hasznaljon tomoritest (bzip)\n    -silent             - ne irjon semmit a kimenetre\n    -mf arg             - metaallomany\n    -src arg            - forrasallomany (titkositando/kititkositando)\n    -dest arg           - celallomany (titkositott/kititkositott allomany)\n    -keylen arg         - kulcshossz (kulcsgeneralashoz, 1024/2048)\n    -keypass arg        - kulcsjelszo\n    -alias arg          - kulcsalias\n    -seckey arg         - titkositott (es nyilvanos) kulcsot tartalmazo\n    -pubkey arg         - csak nyilvanos kulcsot tartalmazo allomany\n    -store arg          - kulcstar\n    -storetype arg      - kulcstar tipusa (PGP, ez az alapertelmezett)\n    -storepass arg      - kulcstarat vedo jelszo\n    -recipient arg      - titkositaskor lehet megadni a cimzett publikus kulcsat tartalmazo allomanyt (egy db \n                          PGP publikus kulcsot vagy egy db X509 tanusitvany-t tartalmazo allomany eleresi ut)\n    -cimzett arg        - metaallomany <abev:Cimzett>, akinek a postafiokjaba kuldjuk az uzenetet\n    -docid arg\n    -doktipazon arg     - metaadat: <abev:DokTipusAzonosito>\n    -doktipleiras arg   - metaadat: <abev:DokTipusLeiras>\n    -doktipver arg      - metaadat: <abev:DokTipusVerzio>\n    -filenev arg        - metaadat: <abev:FileNev>\n    -megjegyzes arg     - metaadat: <abev:Megjegyzes>\n    -param arg1=arg2    - metaadat: <abev:Parameter Nev=\"arg1\" Ertek=\"arg2\"/>\n");
      System.exit(0);
   }

   public FKriptodsk getFKriptodsk() {
      return this.fKriptodskX;
   }

   public KriptoApp(HashMap kepek, InputStream res) {
      instance = this;
      KriptoApp.kepek = kepek;

      try {
         resources = new PropertyResourceBundle(res);
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      FKriptodsk fKriptodsk = new FKriptodsk();
      fKriptodsk.setVisible(true);
      logger.setTextArea(fKriptodsk.getLogArea());
      logger.setResource(resources);
      logger.info("I3001", new Object[]{"1.3.1"});
      this.fKriptodskX = fKriptodsk;
   }

   public static byte[] getKep(String kulcs) {
      return kepek.containsKey(kulcs) ? (byte[])kepek.get(kulcs) : null;
   }

   public static String getAnykFontSize(String kulcs) {
      if (kepek != null) {
         return kepek.containsKey(kulcs) ? (String)kepek.get(kulcs) : null;
      } else {
         return "";
      }
   }

   public KriptoApp(String cmd) {
      instance = this;

      try {
         this.setExitCode(this.runCmd(cmd));
      } catch (Exception var3) {
         logger.severe(var3.getMessage());
      }

   }

   private int runCmd(String cmd) throws GeneralException, IOException {
      List<String> args = Arrays.asList(cmd.split(" "));
      logger.info(args.toString());
      if (((String)args.get(0)).equals("-cmd")) {
         if (Arrays.binarySearch(CMDS, args.get(1)) <= -1) {
            return 10;
         } else {
            if (((String)args.get(1)).equals("encrypt")) {
               String[] params = new String[]{"-mf", "-dest", "-src"};
               int[] exitCodes = new int[]{20, 40, 30};
               String metaFile = null;
               String srcFile = null;
               String destFile = null;

               for(int i = 0; i < params.length; ++i) {
                  int index;
                  if ((index = args.indexOf(params[i])) <= -1) {
                     return 110;
                  }

                  String filePath = (String)args.get(index + 1);
                  if (!params[i].equals("-dest")) {
                     if (!(new File(filePath)).exists()) {
                        return exitCodes[i];
                     }

                     if (params[i].equals("-mf")) {
                        metaFile = filePath;
                     }

                     if (params[i].equals("-src")) {
                        srcFile = filePath;
                     }
                  } else {
                     destFile = filePath;
                  }
               }

               Vector recipients = new Vector();
               if (USER_KEY_AUTOMATIC_USE && USER_KEY_PATH_PUB != null) {
                  if (recipients == null) {
                     recipients = new Vector();
                  }

                  recipients.add(USER_KEY_PATH_PUB);
               }

               this.encrypt(metaFile, srcFile, destFile, recipients, this.zip);
            }

            return 0;
         }
      } else {
         return 10;
      }
   }

   public void keyGen(String storeType, char[] storePass, char[] keyPass, int keyLength, String secFile, String pubFile, String alias) throws IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, IOException, PGPException, GeneralException {
      KeyPair kp = KeyManager.generateRSAKeyPair(keyLength);
      if (storeType.equalsIgnoreCase("PGP")) {
         StoreManager.exportKeyPairPGP(secFile, pubFile, kp, alias, keyPass);
      }

   }

   public void keyGen(String storeType, String storePass, String keyPass, int keyLength, String secFile, String pubFile, String alias) throws IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, IOException, PGPException, GeneralException {
      KeyPair kp = KeyManager.generateRSAKeyPair(keyLength);
      if (storeType.equalsIgnoreCase("PGP")) {
         StoreManager.exportKeyPairPGP(secFile, pubFile, kp, alias, keyPass.toCharArray());
      }

   }

   public void encrypt(String metaFile, String srcFile, String destFile, Vector recipients, boolean zip) throws GeneralException {
      try {
         FileInputStream fin = new FileInputStream(metaFile);
         BoritekParser3 bp = new BoritekParser3(fin, (OutputStream)null, BoritekParser3.PARSE_HEADER);

         try {
            bp.start();
         } catch (FinishException var9) {
         }

         this.encrypt(bp.getMetaData(), srcFile, destFile, recipients, zip);
         fin.close();
      } catch (GeneralException var10) {
         throw var10;
      } catch (Exception var11) {
         logger.debug((Throwable)var11);
         throw new GeneralException(var11.toString());
      }
   }

   public void encrypt(DocMetaData metaData, String srcFile, String destFile, Vector recipientFileNames, boolean zip) throws GeneralException {
      Vector recipientKeyWrappers = new Vector();
      KeyWrapper[] recipientKeys = null;
      if (recipientFileNames != null) {
         Enumeration en = recipientFileNames.elements();

         while(en.hasMoreElements()) {
            try {
               StoreWrapper sw = StoreManager.loadStore((InputStream)(new FileInputStream((String)en.nextElement())), (char[])null);
               Vector keys = sw.listKeys();
               Enumeration enKeys = keys.elements();

               for(int var12 = 0; enKeys.hasMoreElements(); ++var12) {
                  KeyWrapper kw = (KeyWrapper)enKeys.nextElement();
                  if (kw.getPgpPubKey() != null && kw.getPgpPubKey().isMasterKey()) {
                     recipientKeyWrappers.add(kw);
                  }
               }
            } catch (Exception var14) {
               var14.printStackTrace();
            }
         }

         if (recipientKeyWrappers.size() > 0) {
            recipientKeys = new KeyWrapper[recipientKeyWrappers.size()];

            for(int i = 0; i < recipientKeyWrappers.size(); ++i) {
               recipientKeys[i] = (KeyWrapper)recipientKeyWrappers.elementAt(i);
            }
         }
      }

      this.encrypt(metaData, srcFile, destFile, recipientKeys, zip);
   }

   public void encrypt(DocMetaData metaData, String srcFile, String destFile, KeyWrapper[] recipients, boolean zip) throws GeneralException {
      Thread deflator = null;
      PipedOutputStream pout = null;
      PipedInputStream pin = null;
      FileInputStream fin = null;
      FileOutputStream fout = null;

      try {
         BoritekBuilder bb = new BoritekBuilder();
         if (recipients != null) {
            for(int k = 0; k < recipients.length; ++k) {
               bb.addRecipient(recipients[k]);
            }
         }

         bb.setMetaData(metaData);
         fin = new FileInputStream(srcFile);
         if (!zip) {
            bb.setPlainSrc(fin);
         } else {
            pin = new PipedInputStream();
            pout = new PipedOutputStream(pin);
            deflator = new Thread(new DeflatorThread(fin, pout));
            bb.setPlainSrc(pin);
         }

         fout = new FileOutputStream(destFile);
         bb.setDest(fout);
         if (zip) {
            deflator.start();
         }

         bb.build();
         if (zip) {
            deflator.join();
            pout.flush();
            pout.close();
         }

         try {
            fin.close();
         } catch (Exception var14) {
         }

         try {
            fout.close();
         } catch (Exception var13) {
         }

         byte[] hash = bb.getEncryptedDataHash();
         Utils.replace(destFile, "0000000000000000000000000000000000000000".getBytes(), Utils.toHexString(hash).getBytes());
      } catch (Exception var15) {
         logger.debug((Throwable)var15);
         throw new GeneralException(var15.toString());
      }
   }

   public void encrypt(DocMetaData metaData, String srcFile, String destFile, KeyWrapper[] recipients, boolean zip, boolean useCimzettKulcs) throws GeneralException {
      Thread deflator = null;
      PipedOutputStream pout = null;
      PipedInputStream pin = null;
      FileInputStream fin = null;
      FileOutputStream fout = null;

      try {
         BoritekBuilder bb = new BoritekBuilder();
         bb.setMetaData(metaData, useCimzettKulcs);
         fin = new FileInputStream(srcFile);
         if (!zip) {
            bb.setPlainSrc(fin);
         } else {
            pin = new PipedInputStream();
            pout = new PipedOutputStream(pin);
            deflator = new Thread(new DeflatorThread(fin, pout));
            bb.setPlainSrc(pin);
         }

         fout = new FileOutputStream(destFile);
         bb.setDest(fout);
         if (recipients != null) {
            for(int k = 0; k < recipients.length; ++k) {
               bb.addRecipient(recipients[k]);
            }
         }

         if (zip) {
            deflator.start();
         }

         bb.build();
         if (zip) {
            deflator.join();
            pout.flush();
            pout.close();
         }

         try {
            fin.close();
         } catch (Exception var15) {
         }

         try {
            fout.close();
         } catch (Exception var14) {
         }

         byte[] hash = bb.getEncryptedDataHash();
         Utils.replace(destFile, "0000000000000000000000000000000000000000".getBytes(), Utils.toHexString(hash).getBytes());
      } catch (Exception var16) {
         logger.debug((Throwable)var16);
         throw new GeneralException(var16.toString());
      }
   }

   public void decrypt(String storeType, String store, String storePass, String alias, String keyPass, String srcFile, String destFile) throws GeneralException {
      Key key = null;

      try {
         key = StoreManager.loadKey(storeType, store, alias, storePass, keyPass);
         this.decrypt(key, srcFile, destFile);
      } catch (GeneralException var10) {
         throw var10;
      } catch (Exception var11) {
         logger.debug((Throwable)var11);
         throw new GeneralException(var11.toString());
      }
   }

   public void decryptToDir(Key key, String srcFile, String destDir) throws GeneralException, MissingKeyException {
      try {
         FileInputStream fin = new FileInputStream(srcFile);
         BoritekParser3 bp = new BoritekParser3(fin, (OutputStream)null, BoritekParser3.PARSE_ALL, true);
         bp.setDestDir(destDir);
         bp.setPrivateKey((PrivateKey)key);

         try {
            bp.start();
         } catch (FinishException var7) {
         }

         if (debug) {
            System.out.println(bp.getMetaData().toString());
         }

      } catch (Exception var8) {
         logger.debug((Throwable)var8);
         throw new GeneralException(var8.toString());
      }
   }

   public void decrypt(Key key, String srcFile, String destFile) throws GeneralException {
      try {
         FileInputStream fin = new FileInputStream(srcFile);
         FileOutputStream fout = new FileOutputStream(destFile);
         BoritekParser3 bp = new BoritekParser3(fin, fout, true);
         bp.setPrivateKey((PrivateKey)key);

         try {
            bp.start();
         } catch (FinishException var8) {
         }

         if (debug) {
            System.out.println(bp.getMetaData().toString());
         }

      } catch (Exception var9) {
         logger.debug((Throwable)var9);
         throw new GeneralException(var9.toString());
      }
   }

   private void checkPolicy() {
      try {
         Key key = KeyManager.generateAESKey(256);
         Cipher cipher = Cipher.getInstance("AES", "BC");
         cipher.init(1, key);
         cipher.doFinal(new byte[]{-86});
      } catch (SecurityException var3) {
         String msg = var3.getMessage();
         if (msg.lastIndexOf("Unsupported keysize") != -1) {
            logger.warning("policy allomany hiba");
         }
      } catch (NoSuchProviderException var4) {
         var4.printStackTrace();
      } catch (NoSuchAlgorithmException var5) {
         var5.printStackTrace();
      } catch (InvalidKeyException var6) {
         var6.printStackTrace();
      } catch (NoSuchPaddingException var7) {
         var7.printStackTrace();
      } catch (BadPaddingException var8) {
         var8.printStackTrace();
      } catch (IllegalBlockSizeException var9) {
         var9.printStackTrace();
      }

   }

   public void exit(int exitCode, Object[] params) {
      logger.info("EC" + exitCode, params);
      System.exit(exitCode);
   }

   public static void loadConfig() {
      IniReadAndWrite IRAndW = new IniReadAndWrite();
      IRAndW.readIni();
      Properties prop = IRAndW.getProperties();
      if (prop != null) {
         USER_KEY_PATH_PRI = prop.getProperty("SAJAT_TITKOS_KULCS");
         USER_KEY_PATH_PUB = prop.getProperty("SAJAT_NYILVANOS_KULCS");

         try {
            USER_KEY_AUTOMATIC_USE = Boolean.valueOf(prop.getProperty("SAJAT_AUTOMATIKUS"));
         } catch (Exception var3) {
            var3.printStackTrace();
         }

         PATH_KRDIR = Utils.path2Vector(prop.getProperty("KRDIR"));
         PATH_KRDIR_TITKOSITATLAN = Utils.path2Vector(prop.getProperty("TITKOSITASRA_VARO"));
         PATH_KRDIR_LETOLTOTT = Utils.path2Vector(prop.getProperty("LETOLTOTT"));
         PATH_KRDIR_KULDENDO = Utils.path2Vector(prop.getProperty("KULDENDO"));
         PATH_KRDIR_ELKULDOTT = Utils.path2Vector(prop.getProperty("ELKULDOT"));
         KRDIR = prop.getProperty("KRDIR");
         KRDIR_ELKULDOTT = prop.getProperty("LETOLTOTT");
         KRDIR_KULDENDO = prop.getProperty("KULDENDO");
         KRDIR_LETOLTOTT = prop.getProperty("LETOLTOTT");
         KRDIR_TITKOSITATLAN = prop.getProperty("TITKOSITASRA_VARO");
      } else {
         USER_KEY_PATH_PRI = "";
         USER_KEY_PATH_PUB = "";
         USER_KEY_AUTOMATIC_USE = Boolean.FALSE;
         PATH_KRDIR = new Vector();
         PATH_KRDIR_TITKOSITATLAN = new Vector();
         PATH_KRDIR_LETOLTOTT = new Vector();
         PATH_KRDIR_KULDENDO = new Vector();
         PATH_KRDIR_ELKULDOTT = new Vector();
         saveConfig();
      }

   }

   public static void saveConfig() {
      Properties prop = new Properties();
      prop.setProperty("SAJAT_TITKOS_KULCS", USER_KEY_PATH_PRI);
      prop.setProperty("SAJAT_NYILVANOS_KULCS", USER_KEY_PATH_PUB);
      prop.setProperty("SAJAT_AUTOMATIKUS", USER_KEY_AUTOMATIC_USE.toString());
      prop.setProperty("KRDIR", KRDIR);
      prop.setProperty("TITKOSITASRA_VARO", KRDIR_TITKOSITATLAN);
      prop.setProperty("LETOLTOTT", KRDIR_LETOLTOTT);
      prop.setProperty("KULDENDO", KRDIR_KULDENDO);
      prop.setProperty("ELKULDOT", KRDIR_ELKULDOTT);
      IniReadAndWrite ini = new IniReadAndWrite();
      ini.writeIni(prop);
   }

   private static void readKRDIR() {
      String dir = System.getProperty("user.home");
      String envdir = System.getProperty("krdir");
      if (envdir == null || envdir.trim().length() == 0) {
         try {
            envdir = Utils.getEnvVars().getProperty("KRDIR");
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      }

      if (envdir != null) {
         KRDIR = envdir;
         KRDIR_ELKULDOTT = KRDIR + File.separator + "Kr" + File.separator + "Elkuldott";
         KRDIR_KULDENDO = KRDIR + File.separator + "Kr" + File.separator + "Kuldendo";
         KRDIR_LETOLTOTT = KRDIR + File.separator + "Kr" + File.separator + "Letoltott";
         KRDIR_TITKOSITATLAN = KRDIR + File.separator + "Kr" + File.separator + "Digitalis_alairas";
      } else {
         KRDIR = dir;
         KRDIR_ELKULDOTT = KRDIR;
         KRDIR_KULDENDO = KRDIR;
         KRDIR_LETOLTOTT = KRDIR;
         KRDIR_TITKOSITATLAN = KRDIR;
      }

      if (KRDIR != null) {
         logger.debug("I3100", new Object[]{KRDIR});
      } else {
         logger.debug("I3100", new Object[]{""});
      }

   }

   public static String getKRDIR_TITKOSITATLAN() {
      return KRDIR_TITKOSITATLAN;
   }

   public static String getKRDIR_LETOLTOTT() {
      return KRDIR_LETOLTOTT;
   }

   public static String getKRDIR_KULDENDO() {
      return KRDIR_KULDENDO;
   }

   public static Vector getPATH_KRDIR_TITKOSITATLAN() {
      return PATH_KRDIR_TITKOSITATLAN;
   }

   public static String getUSER_KEY_PATH_PUB() {
      return USER_KEY_PATH_PUB;
   }

   public static void setUSER_KEY_PATH_PUB(String USER_KEY_PATH_PUB) {
      KriptoApp.USER_KEY_PATH_PUB = USER_KEY_PATH_PUB;
   }

   public static void setUSER_KEY_PATH_PRI(String USER_KEY_PATH_PRI) {
      KriptoApp.USER_KEY_PATH_PRI = USER_KEY_PATH_PRI;
   }

   public static Boolean getUSER_KEY_AUTOMATIC_USE() {
      return USER_KEY_AUTOMATIC_USE;
   }

   public static void setUSER_KEY_AUTOMATIC_USE(Boolean USER_KEY_AUTOMATIC_USE) {
      KriptoApp.USER_KEY_AUTOMATIC_USE = USER_KEY_AUTOMATIC_USE;
   }

   public static KriptoApp getInstance() {
      return instance;
   }
}
