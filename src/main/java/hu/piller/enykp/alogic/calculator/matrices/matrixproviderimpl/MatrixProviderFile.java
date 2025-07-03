package hu.piller.enykp.alogic.calculator.matrices.matrixproviderimpl;

import hu.piller.enykp.alogic.calculator.matrices.IMatrixProvider;
import hu.piller.enykp.alogic.calculator.matrices.MREF;
import hu.piller.enykp.alogic.calculator.matrices.MatrixMeta;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.Tools;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.jar.JarFile;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public abstract class MatrixProviderFile implements IMatrixProvider {
   public Object[] getMatrix(MREF var1) {
      String var2 = this.getPath(var1);
      MatrixMeta var3 = this.readMatrixMetaFromFile(var1, var2);
      return this.readMatrixFromFile(var3, var2);
   }

   public MatrixMeta getMatrixMeta(MREF var1) {
      String var2 = this.getPath(var1);
      return this.readMatrixMetaFromFile(var1, var2);
   }

   protected abstract String getPath(MREF var1);

   private Object[] readMatrixFromFile(MatrixMeta var1, String var2) {
      JarFile var3 = null;
      BufferedReader var4 = null;

      try {
         Vector var5 = new Vector();
         var3 = new JarFile(var2);
         var4 = new BufferedReader(new InputStreamReader(var3.getInputStream(var3.getEntry("matrix/" + var1.getFile())), var1.getEncoding()));

         String var6;
         while((var6 = var4.readLine()) != null) {
            var5.add(var6);
         }

         Object[] var7 = var5.toArray(new Object[var5.size()]);
         return var7;
      } catch (Exception var21) {
         ErrorList.getInstance().store(ErrorList.LEVEL_ERROR, "A " + var2 + " fájl olvasása során hiba történt!", var21, (Object)null);
      } finally {
         try {
            if (var4 != null) {
               var4.close();
            }
         } catch (IOException var20) {
            var20.printStackTrace();
         }

         try {
            if (var3 != null) {
               var3.close();
            }
         } catch (IOException var19) {
            Tools.eLog(var19, 0);
         }

      }

      return null;
   }

   private MatrixMeta readMatrixMetaFromFile(MREF var1, String var2) {
      MatrixMeta var3 = null;
      JarFile var4 = null;

      try {
         var4 = new JarFile(var2);
         MatricesHandler var5 = new MatricesHandler();
         SAXParserFactory var6 = SAXParserFactory.newInstance();
         SAXParser var7 = var6.newSAXParser();
         var7.parse(var4.getInputStream(var4.getEntry("matrices.xml")), var5);
         var3 = var5.getMatrixMeta(var1.getMatrixID());
      } catch (Exception var16) {
         ErrorList.getInstance().store(ErrorList.LEVEL_ERROR, "A " + var2 + " fájl olvasása során hiba történt!", var16, (Object)null);
      } finally {
         try {
            if (var4 != null) {
               var4.close();
            }
         } catch (IOException var15) {
            Tools.eLog(var15, 0);
         }

      }

      return var3;
   }
}
