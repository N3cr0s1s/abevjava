package hu.piller.enykp.util.trace;

import me.necrocore.abevjava.NecroFile;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class TraceConfig {
   private static final String CFG_CLASSNAME_SEPARATOR = ",";
   private static final String CFG_SEPARATOR = ";";
   private Set<String> tracedClassesFQN = new HashSet();
   private String logDir;

   public TraceConfig(String var1) {
      if (this.isValidConfigString(var1)) {
         String[] var2 = var1.split(";");
         if (this.isValidLogDir(var2[0])) {
            this.logDir = var2[0];
            Scanner var3 = new Scanner(var2[1]);
            var3.useDelimiter(",");

            while(var3.hasNext()) {
               String var4 = var3.next();
               if (this.isValidConfigElement(var4)) {
                  this.tracedClassesFQN.add(var4.toUpperCase());
               }
            }

         } else {
            throw new IllegalArgumentException("invalid trace log dir " + var2[0]);
         }
      } else {
         throw new IllegalArgumentException("invalid trace configuration " + var1);
      }
   }

   public Set<String> getTracedClassesFQN() {
      return this.tracedClassesFQN;
   }

   public String getLogDir() {
      return this.logDir;
   }

   private boolean isValidLogDir(String var1) {
      File var2 = new NecroFile(var1);
      return var2.exists() && var2.isDirectory();
   }

   private boolean isValidConfigString(String var1) {
      return var1 != null && var1.indexOf(";") != -1 && !"".equals(var1.trim());
   }

   private boolean isValidConfigElement(String var1) {
      return var1 != null && !"".equals(var1.trim()) && var1.length() < 256;
   }
}
