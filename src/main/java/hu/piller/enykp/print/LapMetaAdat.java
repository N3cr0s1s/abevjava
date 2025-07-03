package hu.piller.enykp.print;

import java.awt.Dimension;
import java.util.Vector;

public class LapMetaAdat {
   public static final boolean ALLO = true;
   public static final boolean FEKVO = false;
   public String lapCim;
   public String lapId;
   public String formId;
   public String lapNev;
   public boolean alloLap;
   public boolean nyomtathato;
   private boolean nyomtatando;
   public boolean isGuiEnabled = true;
   public boolean folapE = false;
   public boolean disabledByRole = false;
   public int lapSzam;
   public Vector adatNevek;
   public boolean dinamikusE;
   public boolean vanKitoltottMezo;
   public String barkodString;
   public Dimension meret;
   public Dimension meret_mod;
   public int maxLapszam;
   public int foLapIndex;
   public int brImageDarab;
   public int zippedDataLength;
   public boolean _mnp = false;
   public boolean onlyInBrowser = false;
   public boolean _dis = false;
   public boolean _mp = false;
   public String printable_condition = null;
   public boolean printable = true;
   public boolean uniquePrintable = false;
   public boolean forcedByUser = false;

   public boolean isNyomtatando() {
      return this.nyomtatando;
   }

   public void setNyomtatando(boolean var1) {
      if (this.nyomtathato) {
         this.nyomtatando = var1;
      } else if (!var1) {
         this.nyomtatando = var1;
      }

   }

   public LapMetaAdat() {
      this.lapCim = "";
      this.lapNev = "";
      this.lapId = "";
      this.alloLap = true;
      this.nyomtathato = true;
      this.nyomtatando = true;
      this.printable = true;
      this.lapSzam = 1;
      this.dinamikusE = false;
      this.meret = new Dimension(800, 1140);
   }

   public LapMetaAdat(String var1, String var2, String var3, boolean var4, boolean var5, boolean var6, int var7, Vector var8, boolean var9, Dimension var10, Dimension var11, int var12, int var13, String var14, boolean var15, Object var16, boolean var17, boolean var18) {
      this.lapCim = var1;
      this.lapId = var3;
      this.lapNev = var2;
      this.alloLap = var4;
      this.nyomtatando = var5;
      this.nyomtathato = var6;
      this.lapSzam = var7;
      this.adatNevek = var8;
      this.dinamikusE = var9;
      this.meret = var10;
      this.meret_mod = var11;
      this.maxLapszam = var12;
      this.foLapIndex = var13;
      this.formId = var14;
      this.printable = var15;
      this.printable_condition = var16 == null ? null : (String)var16;
      this.folapE = var17;
      this.onlyInBrowser = var18;
   }
}
