package hu.piller.enykp.alogic.calculator.matrices;

import hu.piller.enykp.alogic.calculator.calculator_c.MatrixSearchItem;
import hu.piller.enykp.alogic.calculator.calculator_c.MatrixSearchModel;
import hu.piller.enykp.gui.component.HunCharComparator;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.Tools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class defaultMatrixHandler implements IMatrixHandler {
   public static final String CMD_GETMATRIXITEM = "get_matrix_item";
   public static final int CONST_MAX_MATRIX_INDEX_NUMBER = 30;
   public static final String ERR_MATRIX_ITEM_CREATE = "Hiba a mátrix meghatározásakor! ";
   public static final Long ID_ERR_MATRIX_ITEM_CREATE = new Long(12118L);
   public static final String ERR_MATRIX_ITEM_CREATE_MATRIX = "Nem létező mátrix!";
   public static final String ERR_MATRIX_ITEM_CREATE_MATRIX_NULL = "A mátrix szolgáltató null értékkel tért vissza";
   public static final Long ID_ERR_MATRIX_NOT_FND = new Long(12116L);
   public static final String ERR_MATRIX_NOT_FND = "Hiányzó mátrix! ";
   public static final Long ID_ERR_MATRIX_ITEM = new Long(12117L);
   public static final String ERR_MATRIX_ITEM = "Hiba a mátrix elemek összehasonlításánál! ";
   public static final String URI_PROTOCOL_DELIMITER_DESIGNER = "#";
   public static final String URI_PROTOCOL_DELIMITER_INNER = ":";
   public static final String URI_PROTOCOL_MATRIX_BELSO = "belso";
   public static final String URI_PROTOCOL_MATRIX_KULSO_ALTALANOS = "altalanos";
   private static final String ABC = " !\"0123456789aAáÁbBcCdDeEéÉfFgGhHiIíÍjJkKlLmMnNoOóÓöÖőŐpPqQrRsStTuUúÚüÜűŰvVwWxXyYzZ";
   defaultMatrixHandler.AbevForm[] forms;
   int maxIndexList = 0;
   private static defaultMatrixHandler instance = new defaultMatrixHandler();

   public static IMatrixHandler getInstance() {
      return instance;
   }

   private defaultMatrixHandler() {
   }

   public void release() {
      this.forms = null;
      this.maxIndexList = 0;
   }

   public Vector search(String var1, MatrixSearchModel var2, boolean var3, boolean var4) {
      if (this.forms == null) {
         this.forms = new defaultMatrixHandler.AbevForm[30];
         this.maxIndexList = 0;
      }

      String var5 = this.createCatalogId(var1, var2.getMatrixId());
      int var6 = this.getAbevForm(var5);
      if (var6 < 0) {
         this.addAbevForm(var5, var1);
         var6 = this.getAbevForm(var5);
      }

      return var6 < 0 ? null : this.forms[var6].search(var2.getMatrixId(), var2.getSearchList(), var2.getDelimiter(), var3, var4);
   }

   private int getAbevForm(String var1) {
      for(int var2 = 0; var2 < this.maxIndexList; ++var2) {
         if (this.forms[var2].getCatalogId().compareTo(var1) == 0) {
            return var2;
         }
      }

      return -1;
   }

   private void addAbevForm(String var1, String var2) {
      defaultMatrixHandler.AbevForm var3 = new defaultMatrixHandler.AbevForm(var1, var2);
      this.forms[this.maxIndexList++] = var3;
   }

   public String createNameSpace(String var1, String var2) {
      return var2.indexOf("#") > -1 ? var2.replace("#", ":") : "belso:" + var1 + ":" + var2;
   }

   public String createCatalogId(String var1, String var2) {
      return this.createNameSpace(var1, var2);
   }

   public Object[] getMatrix(String var1, String var2) throws Exception {
      try {
         Object[] var3 = MatrixProvider.getInstance().getMatrix(new MREF(this.createNameSpace(var1, var2)));
         if (var3 == null) {
            throw new Exception("A mátrix szolgáltató null értékkel tért vissza");
         } else {
            return (Object[])((Object[])var3);
         }
      } catch (Exception var4) {
         writeError(ID_ERR_MATRIX_ITEM_CREATE, "Hiba a mátrix meghatározásakor! ", var4, (Object)null);
         throw new Exception("Nem létező mátrix! - " + var4.getMessage());
      }
   }

   public MatrixMeta getMatrixParameters(String var1, String var2) {
      return MatrixProvider.getInstance().getMatrixMeta(new MREF(this.createNameSpace(var1, var2)));
   }

   public String createHash(String var1, String var2, String var3, String var4) {
      return var1 + ":" + var2 + ":" + var3 + ":" + var4;
   }

   public boolean isMatrixExists(String var1, String var2) {
      try {
         this.getMatrix(var1, var2);
         return true;
      } catch (Exception var4) {
         return false;
      }
   }

   private static void writeError(Long var0, String var1, Exception var2, Object var3) {
      try {
         ErrorList.getInstance().writeError(var0, var1, IErrorList.LEVEL_ERROR, var2, var3);
      } catch (Exception var5) {
         Tools.eLog(var5, 1);
      }

   }

   class ListComparator implements Comparator {
      HunCharComparator hunCharComparator = new HunCharComparator();
      private List<defaultMatrixHandler.SearchParameter> searchParameters;
      Object[] matrix;
      boolean search;

      public ListComparator(List<defaultMatrixHandler.SearchParameter> var2, Object[] var3) {
         this.searchParameters = var2;
         this.matrix = var3;
      }

      public void setSearch(boolean var1) {
         this.search = var1;
      }

      public int compare(Object var1, Object var2) {
         try {
            Iterator var4 = this.searchParameters.iterator();

            int var3;
            do {
               if (!var4.hasNext()) {
                  return 0;
               }

               defaultMatrixHandler.SearchParameter var5 = (defaultMatrixHandler.SearchParameter)var4.next();
               int var6 = var5.getColumnIndex();
               String var7 = ((String[])((String[])this.matrix[(Integer)var1]))[var6];
               String var8;
               if (this.search) {
                  var8 = (String)((Object[])((Object[])var2))[var6];
               } else {
                  var8 = ((String[])((String[])this.matrix[(Integer)var2]))[var6];
               }

               var7 = var5.isCaseSensitive() ? var7 : var7.toLowerCase();
               var8 = var5.isCaseSensitive() ? var8 : var8.toLowerCase();
               var3 = this.hunCharComparator.compare(var7, var8);
            } while(var3 == 0);

            return var3;
         } catch (Exception var9) {
            defaultMatrixHandler.writeError(defaultMatrixHandler.ID_ERR_MATRIX_ITEM, "Hiba a mátrix elemek összehasonlításánál! ", var9, var1.toString() + " - " + var2.toString() + "Parameterek:" + this.searchParameters.toString());
            return 0;
         }
      }

      public int fullScanComparator(String var1, String var2, defaultMatrixHandler.SearchParameter var3) throws Exception {
         String var4 = var3.isCaseSensitive() ? var1 : var1.toLowerCase();
         String var5 = var3.isCaseSensitive() ? var2 : var2.toLowerCase();
         switch(var3.realation) {
         case 1:
            return this.hunCharComparator.compare(var4, var5);
         case 2:
            return var4.compareTo(var5) == 0 ? -1 : 0;
         case 3:
            return var4.indexOf(var5) > -1 ? 0 : -1;
         case 4:
            return var5.indexOf(var4) > -1 ? 0 : -1;
         default:
            throw new Exception("Összehasonlítási hiba!");
         }
      }
   }

   class SearchParameter {
      final Integer columnIndex;
      final int realation;
      final boolean caseSensitive;

      SearchParameter(Integer var2, int var3, boolean var4) {
         this.columnIndex = var2;
         this.realation = var3;
         this.caseSensitive = var4;
      }

      public Integer getColumnIndex() {
         return this.columnIndex;
      }

      public int getRealation() {
         return this.realation;
      }

      public boolean isCaseSensitive() {
         return this.caseSensitive;
      }

      public String toString() {
         return "(columnIndex=" + this.columnIndex + ",realation=" + this.realation + ",caseSensitive=" + this.caseSensitive + ")";
      }
   }

   class FastMatrix {
      private ArrayList sortedList;
      private defaultMatrixHandler.ListComparator comparator;
      List<defaultMatrixHandler.SearchParameter> searchParameters = new ArrayList();
      Object[] matrix;

      public FastMatrix(Object[] var2, List<MatrixSearchItem> var3) {
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            MatrixSearchItem var5 = (MatrixSearchItem)var4.next();
            this.searchParameters.add(defaultMatrixHandler.this.new SearchParameter(var5.getColumnIndex(), var5.getRealation(), var5.isCaseSensitive()));
         }

         this.matrix = var2;
         this.comparator = defaultMatrixHandler.this.new ListComparator(this.searchParameters, var2);
         this.sortedList = this.createSortedList(var2, this.comparator);
      }

      public List<defaultMatrixHandler.SearchParameter> getSearchParameters() {
         return this.searchParameters;
      }

      public Vector search(List<MatrixSearchItem> var1, boolean var2) {
         try {
            Object[] var3 = this.convertValuesToArray(var1);
            this.comparator.setSearch(true);
            if (var1.size() == 0) {
               return new Vector(this.sortedList);
            } else {
               Vector var4;
               if (this.needFullScan()) {
                  var4 = this.fullScan(var3, var2);
               } else {
                  var4 = this.binarySearch(var3, var2);
               }

               return var4;
            }
         } catch (Exception var5) {
            var5.printStackTrace();
            return null;
         }
      }

      private boolean needFullScan() {
         for(int var1 = 0; var1 < this.searchParameters.size(); ++var1) {
            if (((defaultMatrixHandler.SearchParameter)this.searchParameters.get(var1)).getRealation() != 1) {
               return true;
            }
         }

         return false;
      }

      private Vector fullScan(Object[] var1, boolean var2) throws Exception {
         boolean var3 = false;
         Vector var4 = new Vector();

         for(int var5 = 0; var5 < this.sortedList.size(); ++var5) {
            int var6 = this.iterConditions(var5, var1);
            if (var6 == 0) {
               var4.add(this.sortedList.get(var5));
               if (var2) {
                  return var4;
               }
            }
         }

         return var4.size() != 0 ? var4 : null;
      }

      private Vector binarySearch(Object[] var1, boolean var2) throws Exception {
         int var3 = Collections.binarySearch(this.sortedList, var1, this.comparator);
         if (var3 < 0) {
            return null;
         } else {
            Vector var4 = new Vector();
            if (var2) {
               var4.add(this.sortedList.get(var3));
            } else {
               this.downStair(var3, 0, var4, var1);
               var4.add(this.sortedList.get(var3));
               this.upStair(var3, this.sortedList.size(), var4, var1);
            }

            return var4;
         }
      }

      private Object[] convertValuesToArray(List<MatrixSearchItem> var1) {
         int var2 = 0;
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            MatrixSearchItem var4 = (MatrixSearchItem)var3.next();
            int var5 = var4.getColumnIndex();
            if (var2 < var5) {
               var2 = var5;
            }
         }

         Object[] var6 = new Object[var2 + 1];

         MatrixSearchItem var8;
         for(Iterator var7 = var1.iterator(); var7.hasNext(); var6[var8.getColumnIndex()] = var8.getSearchedValue()) {
            var8 = (MatrixSearchItem)var7.next();
         }

         return var6;
      }

      private void upStair(int var1, int var2, Vector var3, Object[] var4) throws Exception {
         int var5 = 0;
         int var6 = var1;

         while(true) {
            ++var6;
            if (var6 >= var2 || var5 != 0) {
               return;
            }

            var5 = this.iterConditions(var6, var4);
            if (var5 == 0) {
               var3.add(this.sortedList.get(var6));
            }
         }
      }

      private void downStair(int var1, int var2, Vector var3, Object[] var4) throws Exception {
         int var5 = 0;
         int var6 = var1;
         Vector var7 = new Vector();

         while(true) {
            --var6;
            if (var6 < var2 || var5 != 0) {
               for(int var8 = var7.size() - 1; var8 >= 0; --var8) {
                  Object var9 = var7.elementAt(var8);
                  var3.add(var9);
               }

               return;
            }

            var5 = this.iterConditions(var6, var4);
            if (var5 == 0) {
               var7.add(this.sortedList.get(var6));
            }
         }
      }

      private int iterConditions(int var1, Object[] var2) throws Exception {
         for(int var3 = 0; var3 < this.searchParameters.size(); ++var3) {
            int var4 = ((defaultMatrixHandler.SearchParameter)this.searchParameters.get(var3)).getColumnIndex();
            boolean var5 = false;
            int var6 = this.comparator.fullScanComparator(((String[])((String[])this.matrix[(Integer)this.sortedList.get(var1)]))[var4], (String)var2[var4], (defaultMatrixHandler.SearchParameter)this.searchParameters.get(var3));
            if (var6 != 0) {
               return var6;
            }
         }

         return 0;
      }

      private ArrayList createSortedList(Object[] var1, defaultMatrixHandler.ListComparator var2) {
         ArrayList var3 = new ArrayList(var1.length);

         for(int var4 = 0; var4 < var1.length; ++var4) {
            var3.add(new Integer(var4));
         }

         var2.setSearch(false);
         Collections.sort(var3, var2);
         return var3;
      }
   }

   class AbevMatrix {
      String matrixName;
      String formId;
      Object[] abevMatrix;
      Object[] matrix;
      Object[] sensitiveMatrix;
      defaultMatrixHandler.FastMatrix[] indexList;
      int maxFastIndexList = 0;

      public AbevMatrix(String var2, String var3) {
         this.matrixName = var2;
         this.formId = var3;
      }

      public boolean init() {
         this.abevMatrix = this.getAbevMatrix(this.formId, this.matrixName);
         return this.abevMatrix != null;
      }

      private Object[] getAbevMatrix(String var1, String var2) {
         try {
            Object[] var3 = defaultMatrixHandler.this.getMatrix(var1, var2);
            if (var3 == null) {
               defaultMatrixHandler.writeError(defaultMatrixHandler.ID_ERR_MATRIX_NOT_FND, "Hiányzó mátrix! ", (Exception)null, var2);
               return null;
            } else {
               return (Object[])((Object[])var3);
            }
         } catch (Exception var4) {
            defaultMatrixHandler.writeError(defaultMatrixHandler.ID_ERR_MATRIX_NOT_FND, "Hiányzó mátrix! ", (Exception)null, var2);
            return null;
         }
      }

      private void create2DimMatrix(Object[] var1, String var2) {
         this.matrix = new Object[var1.length];
         this.sensitiveMatrix = new Object[var1.length];
         String[] var3 = null;
         String[] var4 = null;

         for(int var5 = 0; var5 < var1.length; ++var5) {
            String var6 = (String)var1[var5];
            if (var6.lastIndexOf(var2) == var6.length() - 1) {
               var6 = var6 + " ";
               var3 = var6.split(var2);
               var4 = var6.toLowerCase().split(var2);
               var3[var3.length - 1] = "";
               var4[var4.length - 1] = "";
            } else {
               var3 = var6.split(var2);
               var4 = var6.toLowerCase().split(var2);
            }

            this.matrix[var5] = var4;
            this.sensitiveMatrix[var5] = var3;
         }

      }

      public String getMatrixName() {
         return this.matrixName;
      }

      public Vector search(List<MatrixSearchItem> var1, String var2, boolean var3, boolean var4) {
         try {
            if (this.matrix == null || this.sensitiveMatrix == null) {
               this.create2DimMatrix(this.abevMatrix, var2);
            }

            if (this.matrix != null && this.sensitiveMatrix != null) {
               Object[] var5 = this.sensitiveMatrix;
               if (this.indexList == null) {
                  this.indexList = new defaultMatrixHandler.FastMatrix[30];
                  this.maxFastIndexList = 0;
               }

               int var6 = this.getFastMatrix(var1);
               if (var6 < 0) {
                  this.addFastIndex(defaultMatrixHandler.this.new FastMatrix(var5, var1));
                  var6 = this.getFastMatrix(var1);
               }

               if (var6 < 0) {
                  return null;
               } else {
                  Vector var7 = this.indexList[var6].search(var1, var3);
                  if (var7 == null) {
                     return null;
                  } else if (var4) {
                     return var7;
                  } else {
                     Vector var8 = new Vector();

                     for(int var9 = 0; var9 < var7.size(); ++var9) {
                        Integer var10 = (Integer)var7.elementAt(var9);
                        var8.add(this.sensitiveMatrix[var10]);
                     }

                     return var8;
                  }
               }
            } else {
               return null;
            }
         } catch (Exception var11) {
            var11.printStackTrace();
            return null;
         }
      }

      private int getFastMatrix(List<MatrixSearchItem> var1) {
         for(int var2 = 0; var2 < this.maxFastIndexList; ++var2) {
            defaultMatrixHandler.FastMatrix var3 = this.indexList[var2];
            List var4 = var3.getSearchParameters();
            if (this.compSearchParameterLists(var4, var1)) {
               return var2;
            }
         }

         return -1;
      }

      private boolean compSearchParameterLists(List<defaultMatrixHandler.SearchParameter> var1, List<MatrixSearchItem> var2) {
         if (var1.size() != var2.size()) {
            return false;
         } else {
            for(int var3 = 0; var3 < var1.size(); ++var3) {
               if (!((defaultMatrixHandler.SearchParameter)var1.get(var3)).getColumnIndex().equals(((MatrixSearchItem)var2.get(var3)).getColumnIndex())) {
                  return false;
               }

               if (((defaultMatrixHandler.SearchParameter)var1.get(var3)).getRealation() != ((MatrixSearchItem)var2.get(var3)).getRealation()) {
                  return false;
               }

               if (((defaultMatrixHandler.SearchParameter)var1.get(var3)).isCaseSensitive() != ((MatrixSearchItem)var2.get(var3)).isCaseSensitive()) {
                  return false;
               }
            }

            return true;
         }
      }

      public void addFastIndex(defaultMatrixHandler.FastMatrix var1) {
         this.indexList[this.maxFastIndexList++] = var1;
      }
   }

   class AbevForm {
      String catalogId;
      String formId;
      defaultMatrixHandler.AbevMatrix[] matrices;
      int maxMatrixIndexList = 0;

      public AbevForm(String var2, String var3) {
         this.catalogId = var2;
         this.formId = var3;
      }

      public String getCatalogId() {
         return this.catalogId;
      }

      public void setCatalogId(String var1) {
         this.catalogId = var1;
      }

      public String getFormId() {
         return this.formId;
      }

      public void setFormId(String var1) {
         this.formId = var1;
      }

      public defaultMatrixHandler.AbevMatrix[] getMatrices() {
         return this.matrices;
      }

      public Vector search(String var1, List<MatrixSearchItem> var2, String var3, boolean var4, boolean var5) {
         if (this.matrices == null) {
            this.matrices = new defaultMatrixHandler.AbevMatrix[30];
            this.maxMatrixIndexList = 0;
         }

         int var6 = this.getAbevMatrix(var1);
         if (var6 < 0) {
            this.addAbevMatrix(var1);
            var6 = this.getAbevMatrix(var1);
         }

         String var7 = var3;
         MatrixMeta var8 = defaultMatrixHandler.this.getMatrixParameters(this.formId, var1);
         if (var8 != null) {
            var7 = var8.getDelimiter();
         }

         return var6 < 0 ? null : this.matrices[var6].search(var2, var7, var4, var5);
      }

      private int getAbevMatrix(String var1) {
         for(int var2 = 0; var2 < this.maxMatrixIndexList; ++var2) {
            if (this.matrices[var2].getMatrixName().compareTo(var1) == 0) {
               return var2;
            }
         }

         return -1;
      }

      public boolean addAbevMatrix(String var1) {
         defaultMatrixHandler.AbevMatrix var2 = defaultMatrixHandler.this.new AbevMatrix(var1, this.formId);
         if (var2.init()) {
            this.matrices[this.maxMatrixIndexList++] = var2;
            return true;
         } else {
            return false;
         }
      }
   }
}
