package hu.piller.enykp.alogic.calculator.calculator_c;

import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

public class DependencyListFileHelper {
   static File getDependencyFile(Object var0, Object var1, Object var2, Object var3) {
      String var4 = var0 == null ? "" : var0.toString().trim();
      String var5 = var1 == null ? "" : var1.toString().trim();
      String var6 = var2 == null ? "" : var2.toString().trim();
      String var7 = var3 == null ? "" : var3.toString().trim();
      String var8;
      if (var4.length() == 0) {
         var8 = ".";
      } else {
         var8 = var4;
      }

      if (var5.length() > 0) {
         var8 = var8 + File.separator + var5;
      }

      if (var6.length() > 0) {
         var8 = var8 + File.separator + var6 + var7;
      }

      return new NecroFile(var8);
   }

   static void saveToFile(File var0, Object var1) throws IOException {
      var0.getParentFile().mkdirs();
      var0.createNewFile();
      BufferedWriter var2 = new BufferedWriter(new OutputStreamWriter(new NecroFileOutputStream(var0), "iso-8859-2"));

      try {
         if (var1 instanceof Map) {
            Map var3 = (Map)var1;

            for(Iterator var4 = var3.entrySet().iterator(); var4.hasNext(); var2.newLine()) {
               Entry var5 = (Entry)var4.next();
               var2.write(var5.getKey().toString());
               var2.write(", ");
               Object var6 = var5.getValue();
               if (var6 instanceof Object[]) {
                  String var7 = (new Vector(Arrays.asList((Object[])((Object[])var6)))).toString();
                  var7 = var7.substring(1, var7.length() - 1);
                  var2.write(var7);
               } else {
                  var2.write(var6.toString());
               }
            }
         }
      } finally {
         var2.flush();
         var2.close();
      }

   }
}
