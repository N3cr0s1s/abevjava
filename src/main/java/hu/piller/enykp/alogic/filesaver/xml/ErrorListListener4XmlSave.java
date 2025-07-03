package hu.piller.enykp.alogic.filesaver.xml;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.ebev.Mark2Send;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import java.util.Hashtable;
import java.util.Vector;

public class ErrorListListener4XmlSave implements IEventListener {
   public Vector errorList = new Vector();
   private int pageCount = 0;
   private int realError = 0;
   private int maxDb;
   private int fatalError = 0;

   public ErrorListListener4XmlSave(int var1) {
      this.maxDb = var1;
      this.errorList.clear();
   }

   public Object eventFired(Event var1) {
      Hashtable var2 = null;
      if (var1.getSource() instanceof CalculatorManager) {
         var2 = (Hashtable)var1.getUserData();

         try {
            if (this.errorList.size() > 0) {
               if (((TextWithIcon)this.errorList.get(this.errorList.size() - 1)).ii == null) {
                  this.errorList.set(this.errorList.size() - 1, new TextWithIcon(" > " + var2.get("name") + " (" + var2.get("current") + ")", -1));
               } else {
                  this.errorList.add(new TextWithIcon(" > " + var2.get("name") + " (" + var2.get("current") + ")", -1));
                  ++this.realError;
               }
            } else {
               this.errorList.add(new TextWithIcon(" > " + var2.get("name") + " (" + var2.get("current") + ")", -1));
            }

            try {
               if (this.maxDb > -1) {
                  Mark2Send.nyomtatvanyCim.setText("Ellenőrzés: " + this.pageCount + " / " + this.maxDb);
               }
            } catch (Exception var6) {
               var6.printStackTrace();
            }

            ++this.pageCount;
         } catch (Exception var7) {
            Tools.eLog(var7, 0);
         }
      } else if (var1.getSource() instanceof ErrorList) {
         var2 = (Hashtable)var1.getUserData();
         Object[] var3 = (Object[])((Object[])var2.get("item"));
         if (var3 != null && var3[0] != null) {
            String var4 = var3[0].toString();
            if (var4.equalsIgnoreCase("4002") || var4.equalsIgnoreCase("4001") || var4.equalsIgnoreCase("1000")) {
               try {
                  if (var3[4].equals(IErrorList.LEVEL_ERROR) || var3[4].equals(IErrorList.LEVEL_FATAL_ERROR) || var3[4].equals(IErrorList.LEVEL_SHOW_ERROR) || var3[4].equals(IErrorList.LEVEL_SHOW_FATAL_ERROR)) {
                     if (!var3[4].equals(IErrorList.LEVEL_FATAL_ERROR) && !var3[4].equals(IErrorList.LEVEL_SHOW_FATAL_ERROR)) {
                        this.errorList.add(new TextWithIcon(((String)var3[1]).replaceAll("#13", " "), 0, var3[5] != null ? (String)var3[5] : null, var3[6] != null ? (String)var3[6] : null));
                        ++this.realError;
                     } else {
                        this.errorList.add(new TextWithIcon(((String)var3[1]).replaceAll("#13", " "), 1, var3[5] != null ? (String)var3[5] : null, var3[6] != null ? (String)var3[6] : null));
                        ++this.fatalError;
                        ++this.realError;
                     }
                  }
               } catch (Exception var8) {
                  var8.printStackTrace();
               }
            }
         }
      }

      return null;
   }

   public Vector getErrorList() {
      if (this.errorList.size() <= this.realError) {
         this.errorList.clear();
      }

      this.pageCount = 0;
      this.realError = 0;
      return this.errorList;
   }

   public Vector getErrorList4XmlFlyCheckLoader() {
      if (this.errorList.size() <= this.realError && this.isFirstElementPageName()) {
         this.errorList.clear();
      }

      try {
         if (this.errorList.size() > 0) {
            while(this.errorList.elementAt(this.errorList.size() - 1).toString().startsWith(" > ")) {
               this.errorList.remove(this.errorList.size() - 1);
            }
         }
      } catch (Exception var2) {
         this.errorList.clear();
      }

      this.pageCount = 0;
      this.realError = 0;
      return this.errorList;
   }

   public Vector getErrorListForDBBatch() {
      return this.errorList;
   }

   public void clearErrorList() {
      this.errorList.clear();
      this.fatalError = 0;
      this.realError = 0;
   }

   public int getRealError() {
      return this.realError;
   }

   public int getRealErrorExtra() {
      try {
         int var1 = 0;

         for(int var2 = 0; var2 < this.errorList.size(); ++var2) {
            TextWithIcon var3 = (TextWithIcon)this.errorList.elementAt(var2);
            if (var3.imageType > -1 && var3.imageType < 4) {
               ++var1;
            }
         }

         return var1;
      } catch (Exception var4) {
         return 0;
      }
   }

   public int getRealErrorM009(Hashtable var1) {
      try {
         int var2 = 0;

         for(int var3 = 0; var3 < this.errorList.size(); ++var3) {
            TextWithIcon var4 = (TextWithIcon)this.errorList.elementAt(var3);
            if (var4.imageType > -1 && var4.imageType < 4) {
               try {
                  if (!var1.containsKey(var4.getOEC())) {
                     ++var2;
                  }
               } catch (Exception var6) {
                  ++var2;
               }
            }
         }

         return var2;
      } catch (Exception var7) {
         return 0;
      }
   }

   public int getFatalError() {
      return this.fatalError;
   }

   public void restoreListener(Vector var1, int var2, int var3) {
      this.errorList.addAll(var1);
      this.realError += var2;
      this.fatalError += var3;
   }

   private boolean isFirstElementPageName() {
      if (this.errorList.size() < 1) {
         return false;
      } else {
         try {
            TextWithIcon var1 = (TextWithIcon)this.errorList.get(0);
            return var1.ii == null;
         } catch (Exception var2) {
            return false;
         }
      }
   }
}
