package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.util.base.Tools;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

public class TemplateFileHelper {
   static File getTempFile(Object var0, Object var1, Object var2, Object var3, Object var4) {
      String var5 = var0 == null ? "" : var0.toString().trim();
      String var6 = var1 == null ? "" : var1.toString().trim();
      String var7 = var2 == null ? "" : var2.toString().trim();
      String var8 = var3 == null ? "" : var3.toString().trim();
      String var9 = var4 == null ? "" : var4.toString().trim();
      String var10;
      if (var5.length() == 0) {
         var10 = ".";
      } else {
         var10 = var5;
      }

      if (var6.length() > 0) {
         var10 = var10 + File.separator + var6;
      }

      if (var7.length() > 0) {
         var10 = var10 + File.separator + var7;
      }

      if (var8.length() > 0) {
         var10 = var10 + File.separator + var8 + var9;
      }

      return new File(var10);
   }

   static boolean isPreparedTemplateExist(File var0) {
      if (var0 == null) {
         return false;
      } else {
         return var0.exists() && var0.isFile();
      }
   }

   static void saveToFile(File var0, Object var1) throws IOException {
      var0.getParentFile().mkdirs();
      var0.createNewFile();
      FileOutputStream var2 = new FileOutputStream(var0);
      ObjectOutputStream var3 = new ObjectOutputStream(var2);

      try {
         if (var1 instanceof Vector) {
            Vector var4 = (Vector)var1;
            int var5 = 0;

            for(int var6 = var4.size(); var5 < var6; ++var5) {
               var3.writeObject(var4.get(var5));
            }
         } else if (var1 instanceof Map) {
            Map var10 = (Map)var1;
            Iterator var11 = var10.entrySet().iterator();

            while(var11.hasNext()) {
               Entry var12 = (Entry)var11.next();
               var3.writeObject(var12.getKey());
               var3.writeObject(var12.getValue());
            }
         }
      } finally {
         var3.flush();
         var3.close();
      }

   }

   static void loadFromFile(File var0, Object var1) throws Exception {
      FileInputStream var2 = new FileInputStream(var0);
      ObjectInputStream var3 = new ObjectInputStream(var2);

      try {
         if (var1 instanceof Vector) {
            Vector var12 = (Vector)var1;
            var12.removeAllElements();

            while(true) {
               var12.add(var3.readObject());
            }
         }

         if (var1 instanceof Map) {
            Map var4 = (Map)var1;
            var4.clear();

            while(true) {
               Object var5 = var3.readObject();
               Object var6 = var3.readObject();
               var4.put(var5, var6);
            }
         }
      } catch (EOFException var10) {
         Tools.eLog(var10, 0);
      } finally {
         var3.close();
      }

   }
}
