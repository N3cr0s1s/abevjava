package hu.piller.enykp.print;

import java.awt.Graphics;
import java.util.Vector;

public interface JEPPrinter {
   double BR_CORRECTION = 0.8D;
   int COL2MARGIN = 390;
   int H_SPACE = 0;
   int BOTTOM_MARGIN = 110 + (int)((double)MainPrinter.nyomtatoMargo * 2.8D);

   void setFooterVector(Vector var1);

   void setHeaderVector(Vector var1);

   void extraPrint(Graphics var1, int var2, String var3, int var4);

   void barkodPrint(Graphics var1, BrPrinter var2, String var3, String var4, int var5) throws Exception;
}
