package cec;

import cec.taxud.fiscalis.vies.common.VATValidation;
import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.FunctionBodies;
import hu.piller.enykp.alogic.calculator.calculator_c.ExpClass;
import hu.piller.enykp.interfaces.IErrorList;
import java.util.Hashtable;

public class EUFunctionBodies {
   public static boolean test = true;
   private static final String[] EU_MEMBERS = new String[]{"BE", "DE", "DK", "ES", "FR", "GB", "EL", "IE", "IT", "LU", "NL", "PT", "VM", "AT", "FI", "SE", "HU", "EE", "SI", "LV", "MT", "LT", "PL", "SK", "BG", "RO", "CY", "CZ", "HR", "EU", "IM", "XI"};
   private static Hashtable EU_MEMBERS_H = new Hashtable();
   public static IErrorList calc_error_list;
   private static final String EX_TYPE_MISMATCH = "Típus eltérés !";
   private static final Long ID_EX_TYPE_MISMATCH = new Long(12100L);
   private static final String EX_PAR_CNT_MISMATCH = "Paraméterek száma vagy típusa nem megfelelő!";
   private static final Long ID_EX_PAR_CNT_MISMATCH = new Long(12101L);
   private static final Long ID_ERR_NO_MEMBER = new Long(12200L);
   private static final String STR_ERR_NO_MEMBER = "Nincs ilyen EU tagállam!";

   private static void writeError(ExpClass var0, Long var1, String var2, Exception var3, Object var4) {
      FunctionBodies.writeError(var0, var1, var2, var3, var4);
   }

   public static synchronized void fnEuJoAdoszam(ExpClass var0) {
      var0.setType(0);
      var0.setResult((Object)null);
      int var1 = var0.getParametersCount();
      if (var1 == 2) {
         ExpClass var2 = var0.getParameter(0);
         int var3 = var2.getType();
         if (var3 == 0) {
            return;
         }

         if (var3 != 1 && var3 != 2) {
            writeError(var0, ID_EX_TYPE_MISMATCH, "Típus eltérés !", (Exception)null, (Object)null);
            return;
         }

         String var4 = var2.getValue().toString();
         var2 = var0.getParameter(1);
         var3 = var2.getType();
         if (var3 == 0) {
            return;
         }

         if (var3 != 1) {
            writeError(var0, ID_EX_TYPE_MISMATCH, "Típus eltérés !", (Exception)null, (Object)null);
            return;
         }

         String var5 = var2.getValue().toString();
         var0.setType(4);
         if (var4.length() == 0) {
            var0.setResult(Boolean.TRUE);
            return;
         }

         if (!isMember(var5)) {
            writeError(var0, ID_ERR_NO_MEMBER, "Nincs ilyen EU tagállam! (" + var5 + ")", (Exception)null, (Object)null);
            var0.setResult(Boolean.FALSE);
            return;
         }

         if (VATValidation.check(var4, var5)) {
            var0.setResult(Boolean.TRUE);
         } else {
            var0.setResult(Boolean.FALSE);
         }
      } else {
         writeError(var0, ID_EX_PAR_CNT_MISMATCH, "Paraméterek száma vagy típusa nem megfelelő!", (Exception)null, (Object)null);
      }

   }

   public static boolean isMember(String var0) {
      return EU_MEMBERS_H.containsKey(var0.toUpperCase());
   }

   public static void init() {
      for(int var0 = 0; var0 < EU_MEMBERS.length; ++var0) {
         EU_MEMBERS_H.put(EU_MEMBERS[var0], Boolean.TRUE);
      }

   }
}
