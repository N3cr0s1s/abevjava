package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0;

import hu.piller.enykp.util.base.Tools;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Hashtable;

public class Statistic {
   int initialCapacity;
   Hashtable datas;
   Object actOperation;
   long startTime;
   public static final String D = ";";

   public Statistic(int var1) {
      this.initialCapacity = var1;
      this.init();
   }

   public void init() {
      this.datas = new Hashtable(this.initialCapacity);
   }

   public void setTime() {
      this.startTime = System.currentTimeMillis();
   }

   public void startOperation(Object var1) {
      if (this.datas != null) {
         if (!this.datas.containsKey(var1)) {
            this.datas.put(var1, new Statistic.StatRecord());
         }

         this.actOperation = var1;
      }
   }

   public void endOperation() {
      if (this.datas != null) {
         if (this.actOperation != null) {
            if (this.datas.containsKey(this.actOperation)) {
               ((Statistic.StatRecord)this.datas.get(this.actOperation)).addValue(System.currentTimeMillis() - this.startTime);
            }
         }
      }
   }

   public void writeStatistic(String var1) {
      BufferedWriter var2 = null;

      try {
         FileOutputStream var3 = new FileOutputStream(var1);
         var2 = new BufferedWriter(new OutputStreamWriter(var3));
         Enumeration var4 = this.datas.keys();
         var2.write("name;count;min;max;sum;avrg");
         var2.newLine();

         while(var4.hasMoreElements()) {
            Object var5 = var4.nextElement();
            var2.write(var5.toString());
            var2.write(";");
            var2.write(this.datas.get(var5).toString());
            System.out.println("Kifejezések futási ideje:" + ((Statistic.StatRecord)this.datas.get(var5)).sum / 1000000L);
            var2.newLine();
         }

         var2.flush();
         var2.close();
      } catch (FileNotFoundException var16) {
         var16.printStackTrace();
      } catch (IOException var17) {
         var17.printStackTrace();
      } finally {
         try {
            if (var2 != null) {
               var2.flush();
               var2.close();
            }
         } catch (IOException var15) {
            Tools.eLog(var15, 0);
         }

      }

   }

   class StatRecord {
      int count = 0;
      long min = Long.MAX_VALUE;
      long max = Long.MIN_VALUE;
      long sum = 0L;
      long avrg = 0L;

      public StatRecord() {
      }

      public void addValue(long var1) {
         ++this.count;
         if (this.min > var1) {
            this.min = var1;
         }

         if (this.max < var1) {
            this.max = var1;
         }

         this.sum += var1;
      }

      private void compute() {
         if (this.count == 0) {
            this.avrg = 0L;
         } else {
            this.avrg = this.sum / (long)this.count;
         }
      }

      public String toString() {
         this.compute();
         return this.count + ";" + this.min / 1000L + ";" + this.max / 1000L + ";" + this.sum / 1000L + ";" + this.avrg / 1000L;
      }
   }
}
