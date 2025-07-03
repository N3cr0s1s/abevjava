package hu.piller.enykp.alogic.filesaver.xml;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.calculator.calculator_c.GoToButton;
import hu.piller.enykp.alogic.fileloader.db.OnyaErrorListElement;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import java.util.Hashtable;
import java.util.Vector;

public class ErrorListListener4OnyaCheck implements IEventListener {
   private static final String NO_FID_DATA = "-";
   private static final String AFA_GARNITURA_KOD = "65";
   private Vector<OnyaErrorListElement> errorList = new Vector();
   private int realError = 0;
   private int fatalError = 0;
   private int maxElemSzam = 1;
   private int bizonylatReszIndex = 1;
   private String garnituraKod = "";
   private boolean anykTemplateCheckRunning = false;

   public ErrorListListener4OnyaCheck() {
      this.anykTemplateCheckRunning = false;
      this.errorList.clear();
   }

   public void setGarnituraKod(String var1) {
      this.garnituraKod = var1;
   }

   public void setMaxElemCount(int var1) {
      this.maxElemSzam = var1;
   }

   public Object eventFired(Event var1) {
      Hashtable var2 = null;
      if (var1.getSource() instanceof CalculatorManager) {
         ++this.bizonylatReszIndex;
         if (this.bizonylatReszIndex > this.maxElemSzam) {
            this.bizonylatReszIndex = 1;
         }
      } else if (var1.getSource() instanceof ErrorList) {
         var2 = (Hashtable)var1.getUserData();
         Object[] var3 = (Object[])((Object[])var2.get("item"));
         if (var3 != null && var3[0] != null) {
            String var4 = var3[0].toString();
            if (var4.equalsIgnoreCase("4002") || var4.equalsIgnoreCase("4001") || var4.equalsIgnoreCase("1000")) {
               try {
                  if (var3[4].equals(IErrorList.LEVEL_ERROR) || var3[4].equals(IErrorList.LEVEL_SHOW_ERROR) || var3[4].equals(IErrorList.LEVEL_FATAL_ERROR) || var3[4].equals(IErrorList.LEVEL_SHOW_FATAL_ERROR) || var3[4].equals(IErrorList.LEVEL_WARNING) || var3[4].equals(IErrorList.LEVEL_SHOW_WARNING)) {
                     try {
                        System.out.println("GoToButton : " + ((GoToButton)var3[3]).getStoreItem().code);
                     } catch (Exception var11) {
                        System.out.println(var3[3]);
                     }

                     String var5 = this.getHibatipusFromFlag();
                     OnyaErrorListElement var6;
                     if (!var3[4].equals(IErrorList.LEVEL_FATAL_ERROR) && !var3[4].equals(IErrorList.LEVEL_SHOW_FATAL_ERROR)) {
                        if (!var3[4].equals(IErrorList.LEVEL_ERROR) && !var3[4].equals(IErrorList.LEVEL_SHOW_ERROR)) {
                           if (this.checkLapdobas(var3[6])) {
                              return null;
                           }

                           var6 = new OnyaErrorListElement(var3[5] != null ? (String)var3[5] : "", ((String)var3[1]).replaceAll("#13", " "), var5, "1", var3[6] != null ? (String)var3[6] : null, "65".equals(this.garnituraKod) ? this.bizonylatReszIndex : -1);

                           try {
                              var6.addFid(this.getValidFid(((GoToButton)var3[3]).getStoreItem()));
                           } catch (Exception var8) {
                              var6.addFid("-");
                           }

                           this.errorList.add(var6);
                        } else {
                           if (this.checkLapdobas(var3[6])) {
                              return null;
                           }

                           var6 = new OnyaErrorListElement(var3[5] != null ? (String)var3[5] : "", ((String)var3[1]).replaceAll("#13", " "), var5, "2", var3[6] != null ? (String)var3[6] : null, "65".equals(this.garnituraKod) ? this.bizonylatReszIndex : -1);

                           try {
                              var6.addFid(this.getValidFid(((GoToButton)var3[3]).getStoreItem()));
                           } catch (Exception var9) {
                              var6.addFid("-");
                           }

                           this.errorList.add(var6);
                           ++this.realError;
                        }
                     } else {
                        if (this.checkLapdobas(var3[6])) {
                           return null;
                        }

                        var6 = new OnyaErrorListElement(var3[5] != null ? (String)var3[5] : "", ((String)var3[1]).replaceAll("#13", " "), var5, "3", var3[6] != null ? (String)var3[6] : null, "65".equals(this.garnituraKod) ? this.bizonylatReszIndex : -1);

                        try {
                           var6.addFid(this.getValidFid(((GoToButton)var3[3]).getStoreItem()));
                        } catch (Exception var10) {
                           var6.addFid("-");
                        }

                        this.errorList.add(var6);
                        ++this.fatalError;
                        ++this.realError;
                     }
                  }
               } catch (Exception var12) {
                  var12.printStackTrace();
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

   public int getFatalError() {
      return this.fatalError;
   }

   private String getHibatipusFromText(Object var1) {
      try {
         String var2 = (String)var1;
         if (var2.indexOf("B]") > -1) {
            String var3 = "" + var2.charAt(var2.indexOf("B]"));
            if ("B".equals(var3) || "K".equals(var3)) {
               return var3;
            }
         }

         return "K";
      } catch (Exception var4) {
         return "K";
      }
   }

   private String getHibatipusFromFlag() {
      return this.anykTemplateCheckRunning ? "K" : "B";
   }

   public void setAnykTemplateCheckRunning(boolean var1) {
      this.anykTemplateCheckRunning = var1;
   }

   private String getValidFid(StoreItem var1) {
      String var2 = "";
      int var3 = var1.code.indexOf("XXXX");
      if (var3 == -1) {
         return var1.code;
      } else {
         String var4 = "000" + (var1.index + 1);
         var2 = var1.code.substring(0, 2) + var4.substring(var4.length() - 4) + var1.code.substring(6);
         return var2;
      }
   }

   private boolean checkLapdobas(Object var1) {
      if (var1 == null) {
         return false;
      } else {
         try {
            return ((String)var1).startsWith("nem hiba,");
         } catch (Exception var3) {
            return false;
         }
      }
   }
}
