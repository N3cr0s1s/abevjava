package hu.piller.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Utils {
   public static String PROVIDER_NAME_BCP = "BC";
   private static BouncyCastleProvider PROVIDER_BCP;
   public static final int FILE_OK = 0;
   public static final int FILE_NOT_EXISTS = -100;
   public static final int CANNOT_READ_FILE = -200;
   public static final int FILE_NOT_FILE = -300;
   public static final int CANNOT_WRITE_FILE = -400;
   public static final String CHARSET = "iso-8859-2";

   public static BouncyCastleProvider getBCP() {
      if (PROVIDER_BCP == null) {
         PROVIDER_BCP = new BouncyCastleProvider();
      }

      return PROVIDER_BCP;
   }

   public static long writeBlobToFile(Blob blob, String outFile) throws IOException, SQLException {
      long wrote = 0L;
      OutputStream fwriter = new FileOutputStream(outFile);
      wrote = readFromBlob(blob, fwriter);
      fwriter.close();
      return wrote;
   }

   public static long readFromBlob(Blob blob, OutputStream out) throws SQLException, IOException {
      int bBufLen = 1024;
      InputStream in = blob.getBinaryStream();
      long read = 0L;

      int length;
      for(byte[] buf = new byte[bBufLen]; (length = in.read(buf)) != -1; read += (long)length) {
         out.write(buf, 0, length);
      }

      in.close();
      return read;
   }

   public static String toHexString(byte[] array) {
      String str = "";

      int i;
      try {
         for(DataInputStream is = new DataInputStream(new ByteArrayInputStream(array)); is.available() > 0; str = str + Integer.toHexString(i)) {
            i = is.readUnsignedByte();
            if (i < 16) {
               str = str + "0";
            }
         }
      } catch (IOException var4) {
         str = null;
      }

      return str.toUpperCase();
   }

   public static byte[] toByteArray(String hexaStr) {
      byte[] bts = new byte[hexaStr.length() / 2];

      for(int i = 0; i < bts.length; ++i) {
         bts[i] = (byte)Integer.parseInt(hexaStr.substring(2 * i, 2 * i + 2), 16);
      }

      return bts;
   }

   public static byte[] getRand(int length) {
      Random rand = new Random();
      byte[] bytes = new byte[length];
      rand.nextBytes(bytes);
      return bytes;
   }

   public static String getRand(Random rand, int length) {
      byte[] bytes = new byte[length];
      rand.nextBytes(bytes);
      return toHexString(bytes);
   }

   public static byte[] getBytes(byte[] src, int off, int len) {
      byte[] dest = new byte[len];
      if (off + len <= src.length) {
         dest = new byte[len];

         for(int k = 0; k < len; ++k) {
            dest[k] = src[k + off];
         }
      }

      return dest;
   }

   public static String createHash(byte[] b) throws NoSuchAlgorithmException, IOException {
      String digest = null;
      DigestInputStream dis = null;
      ByteArrayInputStream bais = null;
      byte[] digestBuf = new byte[4096];
      bais = new ByteArrayInputStream(b);
      dis = new DigestInputStream(bais, MessageDigest.getInstance("SHA"));

      while(digestBuf.length == dis.read(digestBuf, 0, digestBuf.length)) {
      }

      MessageDigest md = dis.getMessageDigest();
      byte[] d = md.digest();
      digest = toHexString(d);
      dis.close();
      bais.close();
      return digest;
   }

   public static int testFile(String fileName, boolean has2write) {
      File f = new File(fileName);
      if (has2write) {
         if (!f.exists()) {
            try {
               f.createNewFile();
               return 0;
            } catch (IOException var4) {
               var4.printStackTrace();
               return -400;
            }
         } else if (!f.canRead()) {
            return -200;
         } else if (!f.canWrite()) {
            return -200;
         } else {
            return !f.isFile() ? -300 : 0;
         }
      } else if (!f.exists()) {
         return -100;
      } else if (!f.canRead()) {
         return -200;
      } else {
         return !f.isFile() ? -300 : 0;
      }
   }

   public static String replaceBlank(String str) {
      ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes());
      ByteArrayOutputStream os = new ByteArrayOutputStream();

      int b;
      while((b = is.read()) != -1) {
         if (b != 32 && b != 9) {
            os.write(b);
         }
      }

      return os.toString();
   }

   public static Properties getEnvVars() throws IOException {
      Properties envVars = new Properties();
      Map<String, String> env = System.getenv();
      Iterator var3 = env.keySet().iterator();

      while(var3.hasNext()) {
         String key = (String)var3.next();
         envVars.put(key, env.get(key));
      }

      return envVars;
   }

   public static byte[] charToByteArray(char[] carr) {
      byte[] bytes = new byte[carr.length * 2];

      for(int i = 0; i < carr.length; ++i) {
         char c = carr[i];
         bytes[2 * i] = (byte)(c & 255);
         bytes[2 * i + 1] = (byte)(c >> 8 & 255);
      }

      return bytes;
   }

   public static byte[] chars2bytes(char[] carr) throws ArithmeticException {
      byte[] bytes = new byte[carr.length];

      for(int i = 0; i < carr.length; ++i) {
         char ch = carr[i];
         if (ch >= 256) {
            throw new ArithmeticException("non ascii char");
         }

         bytes[i] = (byte)ch;
      }

      return bytes;
   }

   public static String getSHA1Hash(String file) {
      String digest = null;

      try {
         FileInputStream fis = new FileInputStream(file);
         byte[] digestBuf = new byte[4096];
         DigestInputStream dis = new DigestInputStream(fis, MessageDigest.getInstance("SHA"));

         while(digestBuf.length == dis.read(digestBuf, 0, digestBuf.length)) {
         }

         digest = toHexString(dis.getMessageDigest().digest());
      } catch (IOException var6) {
         digest = null;
      } catch (NoSuchAlgorithmException var7) {
         digest = null;
      }

      return digest;
   }

   public static byte[] getSHA1Hash(byte[] rawdata) {
      ByteArrayInputStream is = null;
      DigestInputStream dis = null;
      Object var4 = null;

      try {
         is = new ByteArrayInputStream(rawdata);
         byte[] digestBuf = new byte[4096];
         dis = new DigestInputStream(is, MessageDigest.getInstance("SHA"));

         while(digestBuf.length == dis.read(digestBuf, 0, digestBuf.length)) {
         }
      } catch (IOException var6) {
         var4 = null;
      } catch (NoSuchAlgorithmException var7) {
         var4 = null;
      }

      return dis.getMessageDigest().digest();
   }

   public static String format(String date, String fromSeparator, String toSeparator) {
      String formatedDate;
      if (!fromSeparator.equals("")) {
         formatedDate = date.replaceAll(fromSeparator, toSeparator);
      } else {
         try {
            formatedDate = date.substring(0, 4);
         } catch (Exception var7) {
            return date;
         }

         try {
            formatedDate = formatedDate + toSeparator + date.substring(4, 6);
         } catch (Exception var6) {
            return formatedDate;
         }

         try {
            formatedDate = formatedDate + toSeparator + date.substring(6, 8);
         } catch (Exception var5) {
            return formatedDate;
         }
      }

      return formatedDate;
   }

   public static String intervalFormat(String interval, String separator) {
      String res = interval;

      try {
         if (interval.length() > 8) {
            res = format(interval.substring(0, interval.indexOf(separator)), "", ".");
            res = res + separator + format(interval.substring(interval.indexOf(separator) + 1), "", ".");
         } else {
            res = format(interval, "", ".");
         }
      } catch (Exception var4) {
      }

      return res;
   }

   public static String formatInterval(String ival) {
      String fival = ival;
      if (ival.trim().length() == 17) {
         fival = ival.substring(0, 4) + "." + ival.substring(4, 6) + "." + ival.substring(6, 8) + "-" + ival.substring(9, 13) + "." + ival.substring(13, 15) + "." + ival.substring(6, 8);
      } else if (ival.trim().length() == 8) {
         fival = ival.substring(0, 4) + "." + ival.substring(4, 6) + "." + ival.substring(6, 8);
      }

      return fival;
   }

   public static String formatReqDate(String ival) {
      String fival = ival;
      if (ival.trim().length() == 17) {
         fival = ival.substring(0, 4) + "." + ival.substring(4, 6) + "." + ival.substring(6, 8) + "-" + ival.substring(9, 13) + "." + ival.substring(13, 15) + "." + ival.substring(6, 8);
      } else if (ival.trim().length() == 8) {
         fival = ival.substring(0, 4) + "." + ival.substring(4, 6) + "." + ival.substring(6, 8);
      }

      return fival;
   }

   public static boolean isIdoszak(String idoszak) {
      boolean ok = false;
      Date mydate1 = null;
      Date mydate2 = null;
      SimpleDateFormat sdf = null;
      String isz1 = null;
      String isz2 = null;
      String date1str = null;
      String date2str = null;

      try {
         if (idoszak.length() == 17 && idoszak.charAt(8) == '-') {
            isz1 = idoszak.substring(0, 8);
            isz2 = idoszak.substring(9, 17);
            sdf = new SimpleDateFormat("yyyyMMdd");
            mydate1 = sdf.parse(isz1);
            mydate2 = sdf.parse(isz2);
            date1str = sdf.format(mydate1);
            date2str = sdf.format(mydate2);
            if (mydate1 == null || mydate2 == null) {
               ok = false;
            }
         } else {
            ok = false;
         }
      } catch (Exception var10) {
         ok = false;
      }

      ok = isz1.equalsIgnoreCase(date1str) && isz2.equalsIgnoreCase(date2str);
      return ok;
   }

   public static boolean compareFiles(String f1, String f2) {
      try {
         FileInputStream fis1 = new FileInputStream(f1);
         FileInputStream fis2 = new FileInputStream(f2);
         byte[] buffer1 = new byte[512];
         byte[] buffer2 = new byte[512];

         while(fis1.read(buffer1) != -1) {
            fis2.read(buffer2);
            if (!Arrays.equals(buffer1, buffer2)) {
               return false;
            }
         }

         return true;
      } catch (Exception var8) {
         var8.printStackTrace();
         return false;
      }
   }

   public static void generateRandomFile(String destName, int size) throws IOException, FileNotFoundException {
      long maxFileSize = 4294967296L;
      int bufSize = 512;
      byte[] buffer = null;
      if ((long)size <= maxFileSize) {
         Random rand = new Random();
         FileOutputStream fos = new FileOutputStream(destName);
         if (size <= bufSize) {
            buffer = new byte[size];
            rand.nextBytes(buffer);
            fos.write(buffer);
         } else {
            buffer = new byte[bufSize];
            int iter = (int)Math.floor((double)(size / bufSize));

            for(int k = 0; k < iter; ++k) {
               rand.nextBytes(buffer);
               fos.write(buffer);
            }

            rand.nextBytes(buffer);
            fos.write(buffer, 0, size - iter * bufSize);
         }

         fos.flush();
         fos.close();
      }
   }

   public static InputStream fullStream(String fname) throws IOException {
      FileInputStream fis = new FileInputStream(fname);
      DataInputStream dis = new DataInputStream(fis);
      byte[] bytes = new byte[dis.available()];
      dis.readFully(bytes);
      ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
      return bais;
   }

   public static String[][] listProviders() {
      Provider[] provs = Security.getProviders();
      String[][] prov = new String[provs.length][2];

      for(int k = 0; k < provs.length; ++k) {
         prov[k][0] = provs[k].getName();
         prov[k][1] = provs[k].getClass().getName();
         System.out.println(prov[k][0] + " - " + prov[k][1]);
      }

      return prov;
   }

   public static String checkProvider(String providerClassName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
      Provider[] providers = Security.getProviders();

      for(int p = 0; p < providers.length; ++p) {
         if (providers[p].getClass().getName().equalsIgnoreCase(providerClassName)) {
            providers[p].getName();
         }
      }

      ClassLoader loader = ClassLoader.getSystemClassLoader();
      Provider prov = (Provider)loader.loadClass(providerClassName).newInstance();
      Security.addProvider(prov);
      return prov.getName();
   }

   public char[] readPasswd(InputStream in) throws IOException {
      char[] lineBuffer;
      char[] buf = lineBuffer = new char[128];
      int room = buf.length;
      int offset = 0;

      label32:
      while(true) {
         int c;
         switch(c = ((InputStream)in).read()) {
         case -1:
         case 10:
            break label32;
         case 13:
            int c2 = ((InputStream)in).read();
            if (c2 == 10 || c2 == -1) {
               break label32;
            }

            if (!(in instanceof PushbackInputStream)) {
               in = new PushbackInputStream((InputStream)in);
            }

            ((PushbackInputStream)in).unread(c2);
         default:
            --room;
            if (room < 0) {
               buf = new char[offset + 128];
               room = buf.length - offset - 1;
               System.arraycopy(lineBuffer, 0, buf, 0, offset);
               Arrays.fill(lineBuffer, ' ');
               lineBuffer = buf;
            }

            buf[offset++] = (char)c;
         }
      }

      if (offset == 0) {
         return null;
      } else {
         char[] ret = new char[offset];
         System.arraycopy(buf, 0, ret, 0, offset);
         Arrays.fill(buf, ' ');
         return ret;
      }
   }

   public static boolean replace(String fileName, byte[] str1, byte[] str2) {
      try {
         RandomAccessFile randFile = new RandomAccessFile(fileName, "rwd");
         boolean found = false;
         int count = 0;
         int k = 0;
         byte[] buf = new byte[str1.length];

         int i;
         while((i = randFile.read()) != -1 && !found) {
            ++count;
            buf[k++] = (byte)i;
            if (Arrays.equals(buf, str1)) {
               found = true;
            }

            if (k % buf.length == 0) {
               k = 0;
            }
         }

         if (found) {
            randFile.seek((long)(count - buf.length));
            randFile.write(str2);
            randFile.close();
            return true;
         }

         randFile.close();
      } catch (FileNotFoundException var9) {
         var9.printStackTrace();
      } catch (IOException var10) {
         var10.printStackTrace();
      }

      return false;
   }

   public static Vector path2Vector(String path) {
      Vector myVector = new Vector();
      StringTokenizer st = new StringTokenizer(path, File.pathSeparator);

      while(st.hasMoreTokens()) {
         String myStr = st.nextToken();
         myVector.add(myStr.trim());
      }

      return myVector;
   }
}
