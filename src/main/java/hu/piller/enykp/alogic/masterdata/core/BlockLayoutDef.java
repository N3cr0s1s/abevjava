package hu.piller.enykp.alogic.masterdata.core;

public class BlockLayoutDef {
   private static final int NOCOLOR = -1;
   private static final int DEFAULT = -1;
   private int width = -1;
   private int heigth = -1;
   private int red = -1;
   private int green = -1;
   private int blue = -1;
   private BlockLayoutDef.Row[] rows = new BlockLayoutDef.Row[0];

   public BlockLayoutDef.Row addRow() {
      BlockLayoutDef.Row[] var1 = new BlockLayoutDef.Row[this.rows.length + 1];

      int var2;
      for(var2 = 0; var2 < this.rows.length; ++var2) {
         var1[var2] = this.rows[var2];
      }

      var1[var2] = new BlockLayoutDef.Row();
      this.rows = var1;
      return this.rows[var2];
   }

   public BlockLayoutDef.Row[] getRows() {
      return this.rows;
   }

   public int getWidth() {
      return this.width;
   }

   public void setWidth(int var1) {
      this.width = var1;
   }

   public int getHeigth() {
      return this.heigth + 100;
   }

   public void setHeigth(int var1) {
      this.heigth = var1;
   }

   public boolean hasColor() {
      return this.red != -1 && this.green != -1 && this.blue != -1;
   }

   public int getRed() {
      if (this.red < 0) {
         return 0;
      } else {
         return this.red > 255 ? 255 : this.red;
      }
   }

   public void setRed(int var1) {
      this.red = var1;
   }

   public int getGreen() {
      if (this.green < 0) {
         return 0;
      } else {
         return this.green > 255 ? 255 : this.green;
      }
   }

   public void setGreen(int var1) {
      this.green = var1;
   }

   public int getBlue() {
      if (this.blue < 0) {
         return 0;
      } else {
         return this.blue > 255 ? 255 : this.blue;
      }
   }

   public void setBlue(int var1) {
      this.blue = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("BlockLayout - width: ");
      var1.append(this.getWidth() == -1 ? "AUTO" : this.getWidth());
      var1.append(", heigth: ");
      var1.append(this.getHeigth() == -1 ? "AUTO" : this.getHeigth());
      var1.append(", RGB(");
      if (this.hasColor()) {
         var1.append(this.getRed() == -1 ? "DEFAULT" : this.getRed());
         var1.append(", ");
         var1.append(this.getGreen() == -1 ? "DEFAULT" : this.getGreen());
         var1.append(", ");
         var1.append(this.getBlue() == -1 ? "DEFAULT" : this.getBlue());
      } else {
         var1.append("PARENT_COLOR");
      }

      var1.append(")\n");
      BlockLayoutDef.Row[] var2 = this.rows;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockLayoutDef.Row var5 = var2[var4];
         var1.append(var5);
      }

      return var1.toString();
   }

   public class Row {
      public final int NOT_SET = -1;
      private int topspace = -1;
      private int bottomspace = 1;
      private int heigth = 20;
      private BlockLayoutDef.Row.RowElement[] rowElements = new BlockLayoutDef.Row.RowElement[0];

      public void setTopSpace(int var1) {
         if (var1 < 0) {
            var1 = 0;
         }

         this.topspace = var1;
      }

      public int getTopSpace() {
         return this.topspace;
      }

      public void setBottomSpace(int var1) {
         if (var1 < 0) {
            var1 = 0;
         }

         this.bottomspace = var1;
      }

      public int getBottomSpace() {
         return this.bottomspace;
      }

      public void setHeigth(int var1) {
         this.heigth = var1;
      }

      public int getHeigth() {
         return this.heigth;
      }

      public BlockLayoutDef.Row.RowElement[] getRowElements() {
         return this.rowElements;
      }

      public BlockLayoutDef.Row.RowElement addRowElement() {
         BlockLayoutDef.Row.RowElement[] var1 = new BlockLayoutDef.Row.RowElement[this.rowElements.length + 1];

         int var2;
         for(var2 = 0; var2 < this.rowElements.length; ++var2) {
            var1[var2] = this.rowElements[var2];
         }

         var1[var2] = new BlockLayoutDef.Row.RowElement();
         this.rowElements = var1;
         return var1[var2];
      }

      public int getRowHeigth() {
         byte var1 = 0;
         int var2 = var1 + (this.getTopSpace() == -1 ? 0 : this.getTopSpace());
         var2 += this.getHeigth();
         var2 += this.getBottomSpace() == -1 ? 0 : this.getBottomSpace();
         return var2;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("ROW {");
         var1.append("topspace: ");
         var1.append(this.getTopSpace() == -1 ? "NOT_SET" : this.getTopSpace());
         var1.append(", bottomspace: ");
         var1.append(this.getBottomSpace());
         var1.append(", heigth: ");
         var1.append(this.getHeigth());
         var1.append(" ");
         BlockLayoutDef.Row.RowElement[] var2 = this.rowElements;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            BlockLayoutDef.Row.RowElement var5 = var2[var4];
            var1.append(var5.toString());
         }

         var1.append("}\n");
         return var1.toString();
      }

      public class RowElement {
         public final int ANY_WIDTH = -1;
         public final int GLUE = 0;
         public final int MDATA = 1;
         public final int SYMBOL = 2;
         private boolean has_label = true;
         private int width = -1;
         private int type = 0;
         private String key = "";
         private int space = 0;
         private String symbol;

         public void setWidth(int var1) {
            this.width = var1;
         }

         public int getWidth() {
            return this.width;
         }

         public void setType(int var1) {
            this.type = var1;
            if (this.type == 2) {
               this.symbol = "";
            }

         }

         public int getType() {
            return this.type;
         }

         public void setKey(String var1) {
            this.key = var1;
         }

         public String getKey() {
            return this.key;
         }

         public void setSpace(int var1) {
            this.space = var1;
         }

         public int getSpace() {
            return this.space;
         }

         public void setHasLabel(boolean var1) {
            this.has_label = var1;
         }

         public boolean hasLabel() {
            return this.has_label;
         }

         public void setSymbol(String var1) {
            this.symbol = var1;
         }

         public String getSymbol() {
            return this.symbol;
         }

         public String toString() {
            StringBuffer var1 = new StringBuffer("");
            var1.append("[tpye: ");
            if (this.type == 0) {
               var1.append(" glue");
            } else if (this.type == 1) {
               var1.append(" mdata");
               var1.append(", label: ");
               var1.append(this.hasLabel() ? "exists" : "absent");
               var1.append(", key: ");
               var1.append(this.key);
               var1.append(", space: ");
               var1.append(this.space);
            } else if (this.type == 2) {
               var1.append(" symbol ('");
               var1.append(this.symbol);
               var1.append("')");
            }

            var1.append(", width: ");
            var1.append(this.width == -1 ? "any" : this.width);
            var1.append("]");
            return var1.toString();
         }
      }
   }
}
