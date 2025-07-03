package hu.piller.enykp.util.trace;

import me.necrocore.abevjava.NecroFileWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class TraceHandler extends Handler {
   private Set<String> traceableClasses = new HashSet();
   private PrintWriter pw;

   public TraceHandler(TraceConfig var1) throws IOException, SecurityException {
      this.traceableClasses.addAll(var1.getTracedClassesFQN());
      if (this.traceableClasses.size() > 0) {
         this.pw = new PrintWriter(new BufferedWriter(new NecroFileWriter(var1.getLogDir() + File.separator + "anyktrc.log")));
      }

      this.setLevel(Level.INFO);
      this.setFormatter(new SimpleFormatter());
   }

   public void publish(LogRecord var1) {
      if (this.traceableClasses.contains(var1.getSourceClassName().toUpperCase())) {
         this.pw.println(this.getFormatter().format(var1));
      }

   }

   public void flush() {
      if (this.pw != null) {
         this.pw.flush();
      }

   }

   public void close() throws SecurityException {
      if (this.pw != null) {
         this.pw.close();
      }

   }
}
