package hu.piller.enykp.alogic.calculator.calculator_c.abev_logger;

import hu.piller.enykp.util.base.Tools;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

class ABEVLogger {
   public static final String LINE_STARTER = "* ";
   public static final char ELEMENT_SEPARATOR = '§';
   public static final String[] HEAD_FIELDS = new String[]{"Képlet típusa", "Képlet azonosítója", "Értek", "Képlet hiba", "Képlet hiba fajta", "DID", "VID", "CID", "Képlet"};
   public static String expression_type;
   public static String expression_id;
   public static String expression_result;
   public static String error_message;
   public static String error_message_type;
   public static String did_code;
   public static String vid_code;
   public static String cid_code;
   public static String expression;
   private static final String CHAR_SET = "ISO-8859-2";
   private static BufferedWriter bw;

   public static void clear() {
      expression_type = "";
      expression_id = "";
      expression_result = "";
      error_message = "";
      error_message_type = "";
      did_code = "";
      vid_code = "";
      cid_code = "";
      expression = "";
   }

   public static void open(File var0) {
      try {
         bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var0, true), "ISO-8859-2"));
         StringBuffer var1 = new StringBuffer();
         var1.append(HEAD_FIELDS[0]);
         var1.append('§');
         var1.append(HEAD_FIELDS[1]);
         var1.append('§');
         var1.append(HEAD_FIELDS[2]);
         var1.append('§');
         var1.append(HEAD_FIELDS[3]);
         var1.append('§');
         var1.append(HEAD_FIELDS[4]);
         var1.append('§');
         var1.append(HEAD_FIELDS[5]);
         var1.append('§');
         var1.append(HEAD_FIELDS[6]);
         var1.append('§');
         var1.append(HEAD_FIELDS[7]);
         var1.append('§');
         var1.append(HEAD_FIELDS[8]);
         writeBuffer(var1);
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
      }

   }

   public static void close() {
      try {
         bw.flush();
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
      }

      try {
         bw.close();
      } catch (Exception var1) {
         Tools.eLog(var1, 0);
      }

   }

   public static void write() {
      StringBuffer var0 = new StringBuffer();
      var0.append("* ");
      var0.append(expression_type);
      var0.append('§');
      var0.append(expression_id);
      var0.append('§');
      var0.append(expression_result);
      var0.append('§');
      var0.append(error_message);
      var0.append('§');
      var0.append(error_message_type);
      var0.append('§');
      var0.append(did_code);
      var0.append('§');
      var0.append(vid_code);
      var0.append('§');
      var0.append(cid_code);
      var0.append('§');
      var0.append(expression);
      var0.append('§');
      writeBuffer(var0);
   }

   private static void writeBuffer(StringBuffer var0) {
      try {
         bw.write(var0.toString());
         bw.newLine();
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
      }

   }
}
