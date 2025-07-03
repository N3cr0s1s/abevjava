package hu.piller.enykp.alogic.kontroll;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class KvfData {
   int ellenorzo_szam;
   String nyomtatvanytipus;
   boolean egydatbol;
   int kimenetek_szama;
   Vector[] file;

   public KvfData(String var1) throws Exception {
      BufferedReader var2 = new BufferedReader(new InputStreamReader(new FileInputStream(var1)));
      this.ellenorzo_szam = new Integer(var2.readLine());
      this.nyomtatvanytipus = var2.readLine().substring(17);
      this.egydatbol = !var2.readLine().equals("EgyDATból=0");
      this.kimenetek_szama = new Integer(var2.readLine().substring(15));
      this.file = new Vector[this.kimenetek_szama];
      int var4 = -1;

      String var3;
      while((var3 = var2.readLine()) != null) {
         if (var3.startsWith("File")) {
            ++var4;
            this.file[var4] = new Vector();
         } else {
            this.file[var4].add(var3.substring(var3.indexOf("=") + 1));
         }
      }

   }
}
