package hu.piller.enykp.alogic.kihatas;

import java.util.Vector;

public class MegallapitasComboLista extends Vector<MegallapitasComboRecord> {
   public String getMsvo_azonByName(String var1) {
      for(int var2 = 0; var2 < this.size(); ++var2) {
         MegallapitasComboRecord var3 = (MegallapitasComboRecord)this.elementAt(var2);
         if (var3.getNev().equals(var1)) {
            return var3.getMsvo_azon();
         }
      }

      return null;
   }

   public String getNameByMsvoAzon(String var1) {
      for(int var2 = 0; var2 < this.size(); ++var2) {
         MegallapitasComboRecord var3 = (MegallapitasComboRecord)this.elementAt(var2);
         if (var3.getMsvo_azon().equals(var1)) {
            return var3.getNev();
         }
      }

      return null;
   }

   public String getDisplayTextByMsvoAzon(String var1) {
      for(int var2 = 0; var2 < this.size(); ++var2) {
         MegallapitasComboRecord var3 = (MegallapitasComboRecord)this.elementAt(var2);
         if (var3.getMsvo_azon().equals(var1)) {
            return var3.getDisplayText();
         }
      }

      return "A megnevezés nem található!";
   }

   public Vector getAdnlistByMsvoAzon(String var1) {
      for(int var2 = 0; var2 < this.size(); ++var2) {
         MegallapitasComboRecord var3 = (MegallapitasComboRecord)this.elementAt(var2);
         if (var3.getMsvo_azon().equals(var1)) {
            return var3.getAdonemlista();
         }
      }

      return new Vector();
   }

   public boolean checkdata(String var1, String var2) {
      for(int var3 = 0; var3 < this.size(); ++var3) {
         MegallapitasComboRecord var4 = (MegallapitasComboRecord)this.elementAt(var3);
         if (var4.getMsvo_azon().equals(var2)) {
            Vector var5 = var4.getAdonemlista();

            for(int var6 = 0; var6 < var5.size(); ++var6) {
               String var7 = (String)var5.elementAt(var6);
               if (var7.equals(var1)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public int getIndexByMsvo(String var1) {
      for(int var2 = 0; var2 < this.size(); ++var2) {
         MegallapitasComboRecord var3 = (MegallapitasComboRecord)this.elementAt(var2);
         if (var3.getMsvo_azon().equals(var1)) {
            return var2;
         }
      }

      return -1;
   }
}
