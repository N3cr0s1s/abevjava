package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.interval.intervalImpl;

import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.interval.DefaultInterval;
import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.interval.IInterval;
import hu.piller.enykp.util.base.Tools;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateIntervalImpl extends DefaultInterval {
   private SimpleDateFormat start;
   private SimpleDateFormat end;

   public DateIntervalImpl() {
   }

   public DateIntervalImpl(Object var1, Object var2) {
      this.setValues(var1, var2);
   }

   public boolean setValues(Object var1, Object var2) {
      SimpleDateFormat var3 = getDate(var1);
      if (var3 == null) {
         return false;
      } else {
         SimpleDateFormat var4 = getDate(var2);
         if (var4 == null) {
            return false;
         } else {
            if (var4.getCalendar().before(var3.getCalendar())) {
               this.addValues(var2, var1);
            } else {
               this.addValues(var1, var2);
            }

            return true;
         }
      }
   }

   public void addValues(Object var1, Object var2) {
      this.originalStartValue = var1;
      this.originalEndValue = var2;
      this.start = getDate(var1);
      this.end = getDate(var2);
   }

   public SimpleDateFormat getStartValue() {
      return this.start;
   }

   public SimpleDateFormat getEndValue() {
      return this.end;
   }

   public int length() {
      return this.getNumberOfDaysBetween(this.start, this.end);
   }

   public int distance(IInterval var1) {
      return this.getNumberOfDaysBetween(this.end, (SimpleDateFormat)var1.getStartValue());
   }

   public void union(IInterval var1) {
      if (this.start.getCalendar().after(((SimpleDateFormat)var1.getStartValue()).getCalendar())) {
         this.start = (SimpleDateFormat)var1.getStartValue();
         this.originalStartValue = var1.getOriginalStartValue();
      }

      if (this.end.getCalendar().before(((SimpleDateFormat)var1.getEndValue()).getCalendar())) {
         this.end = (SimpleDateFormat)var1.getEndValue();
         this.originalEndValue = var1.getOriginalEndValue();
      }

   }

   public boolean isOverLap(IInterval var1) {
      return this.compareTo(var1) == 0;
   }

   public boolean isContinual(IInterval var1) {
      return this.isOverLap(var1) || this.distance(var1) < 2;
   }

   public int compareTo(Object var1) {
      IInterval var2 = (IInterval)var1;
      if (this.end.getCalendar().before(((SimpleDateFormat)var2.getStartValue()).getCalendar())) {
         return -1;
      } else {
         return this.start.getCalendar().after(((SimpleDateFormat)var2.getEndValue()).getCalendar()) ? 1 : 0;
      }
   }

   private static SimpleDateFormat getDate(Object var0) {
      try {
         if (var0 instanceof SimpleDateFormat) {
            return (SimpleDateFormat)var0;
         } else if (var0 == null) {
            return null;
         } else {
            SimpleDateFormat var1 = new SimpleDateFormat("yyyyMMdd", new Locale("hu", "HU"));
            var1.parse(var0.toString());
            return var1;
         }
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
         return null;
      }
   }

   private int getNumberOfDaysBetween(SimpleDateFormat var1, SimpleDateFormat var2) {
      if (var1.getCalendar().get(1) == var2.getCalendar().get(1)) {
         return var2.getCalendar().get(6) - var1.getCalendar().get(6);
      } else {
         int var3 = var2.getCalendar().get(6) + (var1.getCalendar().getActualMaximum(6) - var1.getCalendar().get(6));
         GregorianCalendar var4 = new GregorianCalendar(2010, 1, 1);

         for(int var5 = var1.getCalendar().get(1) + 1; var5 <= var2.getCalendar().get(1) - 1; ++var5) {
            var4.set(1, var5);
            var3 += var4.getActualMaximum(6);
         }

         return var3;
      }
   }
}
