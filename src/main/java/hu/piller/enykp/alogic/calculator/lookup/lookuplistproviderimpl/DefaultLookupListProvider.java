package hu.piller.enykp.alogic.calculator.lookup.lookuplistproviderimpl;

import hu.piller.enykp.alogic.calculator.lookup.ILookupListProvider;
import hu.piller.enykp.alogic.calculator.lookup.LookupList;
import hu.piller.enykp.alogic.calculator.matrices.MatrixMeta;
import hu.piller.enykp.alogic.calculator.matrices.defaultMatrixHandler;
import hu.piller.enykp.gui.component.HunCharComparator;
import hu.piller.enykp.gui.component.NumCharComparator;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.Tools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public abstract class DefaultLookupListProvider implements ILookupListProvider {
   public static final int CONST_MAX_LOOKUP_INDEX_NUMBER = 30;
   public static final Long ID_ERR_LOOKUP_ITEM_CREATE = new Long(12200L);
   public static final Long ID_ERR_LOOKUP_ITEM_INDEX_CREATE = new Long(12201L);
   public static final String ERR_LOOKUP_ITEM_CREATE = "Hiba a tábla nézet létrehozásakor! ";
   public static final String ERR_LOOKUP_ITEM_INDEX_CREATE = "Hiba a tábla nézet index létrehozásakor! ";
   public static final String ERR_LOOKUP_ITEM_CREATE_LOOKUP_FLIST = "Hibás mezőlista!";
   public static final String TABLE_VIEW_DELIMITER_STRING = "  -  ";
   public static final String TMP_INDEX_DELIMITER_1 = "|#&&#|";
   protected Hashtable<String, List<String>> views;
   protected Hashtable<String, LookupList> viewsIndex;
   protected String formId;
   protected String fid;
   protected int dynPageNumber;
   protected Hashtable fieldMetas;
   protected String groupId;
   protected String matrixId;
   protected String fieldCol;
   protected String fieldList;
   protected String delimiter;
   protected boolean overWrite = false;

   protected DefaultLookupListProvider() {
   }

   public void release() {
      this.views = null;
      this.viewsIndex = null;
   }

   public List<String> getTableView(int var1, String var2) throws Exception {
      this.dynPageNumber = var1;

      String var4;
      try {
         String var3 = this.createNameSpace(this.formId, this.matrixId);
         var4 = this.createHash(var3, var2, this.delimiter, (String)null);
         if (this.views == null) {
            this.views = new Hashtable(60);
         }

         List var5 = (List)this.views.get(var4);
         if (var5 == null) {
            Object[] var6 = this.getMatrix(this.formId, this.matrixId);
            String var7 = this.delimiter;
            MatrixMeta var8 = this.getMatrixParameters(this.formId, this.matrixId);
            if (var8 != null) {
               var7 = var8.getDelimiter();
            }

            var5 = this.createView(var6, var2, var7);
            this.views.put(var4, var5);
         }

         return var5;
      } catch (Exception var9) {
         var4 = this.formId + "/" + this.matrixId + "/" + this.delimiter + "/" + var2;
         writeError(ID_ERR_LOOKUP_ITEM_CREATE, "Hiba a tábla nézet létrehozásakor! ", var9, var4);
         throw new Exception("Hiba a tábla nézet létrehozásakor! " + var4 + " - " + var9.getMessage());
      }
   }

   protected List<String> createView(Object[] var1, String var2, String var3) throws Exception {
      try {
         int[] var4 = this.createFieldList(var2);
         ArrayList var5 = new ArrayList(var1.length);

         for(int var6 = 0; var6 < var1.length; ++var6) {
            Object var7 = var1[var6];
            String[] var8 = ((String)var7).split(var3);
            StringBuilder var9 = new StringBuilder();
            var9.append(var8[var4[0]]);

            for(int var10 = 1; var10 < var4.length; ++var10) {
               var9.append("  -  ");
               var9.append(var8[var4[var10]]);
            }

            var5.add(var9.toString());
         }

         return var5;
      } catch (Exception var11) {
         writeError(ID_ERR_LOOKUP_ITEM_CREATE, "Hiba a tábla nézet létrehozásakor! ", var11, (Object)null);
         throw var11;
      }
   }

   public int[] createFieldList(String var1) throws Exception {
      try {
         String[] var2 = var1.split(",");
         int[] var3 = new int[var2.length];

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3[var4] = Integer.parseInt(var2[var4]) - 1;
         }

         return var3;
      } catch (NumberFormatException var5) {
         writeError(ID_ERR_LOOKUP_ITEM_CREATE, "Hiba a tábla nézet létrehozásakor! ", var5, "Hibás mezőlista!");
         throw new Exception("Hibás mezőlista! - " + var5.getMessage());
      }
   }

   protected static void writeError(Long var0, String var1, Exception var2, Object var3) {
      try {
         ErrorList.getInstance().writeError(var0, var1, IErrorList.LEVEL_ERROR, var2, var3);
      } catch (Exception var5) {
         Tools.eLog(var5, 1);
      }

   }

   public LookupList getSortedTableView(int var1) throws Exception {
      this.dynPageNumber = var1;

      String var3;
      try {
         String var2 = this.createNameSpace(this.formId, this.matrixId);
         var3 = this.createHash(var2, this.fieldList, this.delimiter, this.fieldCol);
         if (this.viewsIndex == null) {
            this.viewsIndex = new Hashtable(60);
         }

         LookupList var4 = (LookupList)this.viewsIndex.get(var3);
         if (var4 == null) {
            Object[] var5 = this.getMatrix(this.formId, this.matrixId);
            String var6 = this.delimiter;
            MatrixMeta var7 = this.getMatrixParameters(this.formId, this.matrixId);
            if (var7 != null) {
               var6 = var7.getDelimiter();
            }

            var4 = this.createViewIndex(var5, this.fieldCol, this.fieldList, var6);
            var4.setOverWrite(this.overWrite);
            this.viewsIndex.put(var3, var4);
         }

         return var4;
      } catch (Exception var8) {
         var3 = this.formId + "/" + this.matrixId + "/" + this.delimiter + "/" + this.fieldList;
         writeError(ID_ERR_LOOKUP_ITEM_INDEX_CREATE, "Hiba a tábla nézet index létrehozásakor! ", var8, var3);
         throw new Exception("Hiba a tábla nézet index létrehozásakor! " + var3 + " - " + var8.getMessage());
      }
   }

   protected LookupList createViewIndex(Object[] var1, String var2, String var3, String var4) throws Exception {
      try {
         List var5 = this.createView(var1, var2, var4);
         List var6 = this.createView(var1, var3, var4);
         Vector var7 = this.mergeVectors(var6, var5);
         Comparator var8 = this.getComparator(var7);

         try {
            Collections.sort(var7, var8);
         } catch (Exception var11) {
         }

         try {
            if (var8 instanceof NumCharComparator && ((NumCharComparator)var8).hasNumberException) {
               Collections.sort(var7, new HunCharComparator());
            }
         } catch (Exception var10) {
         }

         return this.splitVectors(var7);
      } catch (Exception var12) {
         writeError(ID_ERR_LOOKUP_ITEM_CREATE, "Hiba a tábla nézet létrehozásakor! ", var12, (Object)null);
         throw var12;
      }
   }

   private Comparator getComparator(List<String> var1) {
      if (var1 != null && var1.size() != 0) {
         NumCharComparator var2 = new NumCharComparator();
         return (Comparator)(var2.setNumDelimiter((String)var1.get(0)) ? var2 : new HunCharComparator());
      } else {
         return new HunCharComparator();
      }
   }

   private Vector<String> mergeVectors(List<String> var1, List<String> var2) {
      Vector var3 = new Vector(var1.size());

      for(int var4 = 0; var4 < var1.size(); ++var4) {
         var3.add((String)var1.get(var4) + "|#&@&#|" + (String)var2.get(var4) + "|#&&#|" + var4);
      }

      return var3;
   }

   private LookupList splitVectors(Vector var1) {
      Vector var2 = new Vector(var1.size());
      Vector var3 = new Vector(var1.size());
      Vector var4 = new Vector(var1.size());

      for(int var5 = 0; var5 < var1.size(); ++var5) {
         var2.add(((String)var1.elementAt(var5)).substring(0, ((String)var1.elementAt(var5)).indexOf("|#&@&#|")));
         String var6 = ((String)var1.elementAt(var5)).substring(((String)var1.elementAt(var5)).indexOf("|#&@&#|") + 7);
         var3.add(var6.substring(0, var6.indexOf("|#&&#|")));
         var4.add(Integer.valueOf(var6.substring(var6.indexOf("|#&&#|") + 6)));
      }

      return new LookupList(var2, var3, var4);
   }

   protected abstract Object[] getMatrix(String var1, String var2) throws Exception;

   private String createNameSpace(String var1, String var2) {
      return defaultMatrixHandler.getInstance().createNameSpace(var1, var2);
   }

   private String createHash(String var1, String var2, String var3, String var4) {
      return defaultMatrixHandler.getInstance().createHash(var1, var2, var3, var4);
   }

   protected MatrixMeta getMatrixParameters(String var1, String var2) {
      return defaultMatrixHandler.getInstance().getMatrixParameters(var1, var2);
   }

   public abstract boolean validate(int var1, String var2);

   public String getFieldCol() {
      return this.fieldCol;
   }

   public String getFieldList() {
      return this.fieldList;
   }
}
