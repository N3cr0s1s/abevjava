package hu.piller.enykp.datastore;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.viewer.PageViewer;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IEventLog;
import hu.piller.enykp.util.base.Tools;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class GUI_Datastore implements IDataStore, Serializable {
   private static final int SET = 0;
   private static final int GET = 1;
   private static final int RESET = 2;
   private static final int BEGIN = 3;
   private static final int ROLLBACK = 4;
   private static final int COMMIT = 5;
   private static final int REMOVE = 6;
   private static final int SHIFT = 7;
   protected IEventLog mastereventlog;
   Hashtable datas = new Hashtable(200);
   Hashtable trdatas = new Hashtable(200);
   Hashtable basedatas = new Hashtable(200);
   private boolean inTransaction = false;
   private boolean dirty = false;
   private int index;
   private int direction;
   private int maxindex;
   private boolean ccdirty = false;
   public boolean inkihatas = false;
   public long setcount;
   public long getcount;
   private boolean wasSavepoint = false;
   private int ccindex;

   public GUI_Datastore() {
      this.setcount = this.getcount = 0L;
   }

   public String getIOInfo() {
      return "set=" + this.setcount + " db    get=" + this.getcount + " db";
   }

   public void set(Object var1, String var2) {
      ++this.setcount;
      if (var2 != null) {
         try {
            GuiUtil.getDMFV_bm().dirty = true;
         } catch (Exception var10) {
            Tools.eLog(var10, 0);
         }

         if (!var2.equals("")) {
            this.dirty = true;
            this.ccdirty = true;
         }

         String var3 = null;
         if (MainFrame.datastorehistorylive) {
            var3 = this._get(var1);
         }

         this.done(0, var1, var2);

         try {
            if (!this.wasSavepoint) {
               return;
            }

            PageViewer var4;
            if (MainFrame.datastorehistorylive) {
               var4 = GuiUtil.getDMFV().fv.pv.pv;
            } else {
               var4 = null;
            }

            if (var4 != null) {
               boolean var5 = true;
               if (var3 == null && var2 == null) {
                  return;
               }

               if (var3 == null && var2.length() == 0) {
                  var3 = "0";
                  var2 = "0";
                  var5 = false;
               }

               if (var3 != null && var3.equals(var2)) {
                  var5 = false;
               }

               if (var3 == null) {
                  var3 = "0";
               }

               if (var3.length() == 0) {
                  var3 = "0";
               }

               if (var5) {
                  if (var1 instanceof Object[]) {
                     Object[] var7 = (Object[])((Object[])var1);
                     var4.done_set_history(var7[0] + "_" + var7[1], var2, (String)var7[1]);
                  } else {
                     var4.done_set_history(var1.toString(), var2, var1.toString().substring(var1.toString().indexOf("_") + 1));
                  }
               }

               if (("2".equals(MainFrame.role) || "3".equals(MainFrame.role)) && (this.inkihatas || MainFrame.recalc_in_progress)) {
                  String var12 = var2;
                  if (var2 == null) {
                     var12 = "0";
                  }

                  if (var12.length() == 0) {
                     var12 = "0";
                  }

                  if (var1 instanceof Object[]) {
                     Object[] var8 = (Object[])((Object[])var1);
                     String var9 = TemplateUtils.getInstance().DSIdToCId(var8[0] + "_" + var8[1], (Object)null);
                     var4.done_set_kihatas(var8[0] + "_" + var8[1], var12, var9, var3, this.ccindex);
                  } else {
                     var4.done_set_kihatas(var1.toString(), var12, var1.toString(), var3, this.ccindex);
                  }
               }
            }
         } catch (Exception var11) {
            Tools.eLog(var11, 0);
         }

      }
   }

   public String get(Object var1) {
      ++this.getcount;
      return this.done(1, var1, (String)null);
   }

   public void reset() {
      this.dirty = true;
      this.ccdirty = true;
      this.done(2, (Object)null, (String)null);
   }

   public void beginTransaction() {
      this.done(3, (Object)null, (String)null);
   }

   public void rollbackTransaction() {
      this.done(4, (Object)null, (String)null);
   }

   public void commitTransaction() {
      this.done(5, (Object)null, (String)null);
   }

   public void remove(Object var1) {
      this.dirty = true;
      this.ccdirty = true;
      this.done(6, var1, (String)null);
   }

   public void shift(String var1, int var2, int var3, int var4) {
      this.index = var2;
      this.direction = var3;
      this.maxindex = var4;
      this.done(7, var1, (String)null);
   }

   private void _shift(Object var1) {
      StoreItem var2 = new StoreItem();
      var2.code = (String)var1;
      var2.index = this.index;
      StoreItem var3;
      StoreItem var5;
      if (this.direction == -1) {
         var2.index = this.index;
         var3 = (StoreItem)this.datas.get(var2);
         if (var3 != null) {
            var3.index = -1;
         }

         for(int var4 = this.index; var4 < this.maxindex; ++var4) {
            var2.index = var4 + 1;
            var3 = (StoreItem)this.datas.get(var2);
            if (var3 != null) {
               this.datas.remove(var3);
               var3.index = var4;
               this.datas.put(var3, var3);
            } else {
               var5 = new StoreItem();
               var5.code = (String)var1;
               var5.index = var4;
               var5.value = "";
               this.datas.put(var5, var5);
            }
         }

         var2.index = this.maxindex;
         var2.value = "";
         var3 = (StoreItem)this.datas.get(var2);
         if (var3 != null) {
            var3.value = "";
         } else {
            this.datas.put(var2, var2);
         }
      } else {
         for(int var6 = this.maxindex; var6 >= this.index; --var6) {
            var2.index = var6;
            StoreItem var7 = (StoreItem)this.datas.get(var2);
            if (var7 != null) {
               this.datas.remove(var7);
               var7.index = var6 + 1;
               this.datas.remove(var7);
               this.datas.put(var7, var7);
            } else {
               var5 = new StoreItem();
               var5.code = (String)var1;
               var5.index = var6 + 1;
               var5.value = "";
               this.datas.put(var5, var5);
            }
         }

         var2.index = this.index;
         var2.value = "";
         var3 = (StoreItem)this.datas.get(var2);
         if (var3 != null) {
            var3.value = "";
         } else {
            this.datas.put(var2, var2);
         }
      }

      this.dirty = true;
      this.ccdirty = true;
   }

   private void _shift_old(Object var1) {
      StoreItem var2 = new StoreItem();
      var2.code = (String)var1;
      var2.index = this.index;
      StoreItem var3;
      StoreItem var4;
      int var5;
      if (this.direction == -1) {
         var2.index = this.index;
         if (this.datas.contains(var2)) {
            var3 = (StoreItem)this.datas.get(var2);
            var3.index = -1;
         }

         for(var5 = this.index; var5 <= this.maxindex; ++var5) {
            var2.index = var5 + 1;
            if (this.datas.contains(var2)) {
               var4 = (StoreItem)this.datas.get(var2);
               var4.index = var5;
            } else {
               var2.index = var5;
               this.datas.put(var2, var2);
            }
         }

         var2.index = this.maxindex;
         var2.value = "";
         if (this.datas.contains(var2)) {
            var3 = (StoreItem)this.datas.get(var2);
            var3.value = "";
         } else {
            this.datas.put(var2, var2);
         }
      } else {
         for(var5 = this.maxindex; var5 >= this.index; --var5) {
            var2.index = var5;
            if (this.datas.contains(var2)) {
               var4 = (StoreItem)this.datas.get(var2);
               var4.index = var5 + 1;
            } else {
               var2.index = var5 + 1;
               this.datas.put(var2, var2);
            }
         }

         var2.index = this.index;
         var2.value = "";
         if (this.datas.contains(var2)) {
            var3 = (StoreItem)this.datas.get(var2);
            var3.value = "";
         } else {
            this.datas.put(var2, var2);
         }
      }

   }

   private void _set(Object var1, String var2) {
      StoreItem var3;
      if (var1 instanceof Object[]) {
         var3 = this.convert((Object[])((Object[])var1), var2);
      } else {
         var3 = this.convert(var1, var2);
      }

      StoreItem var4;
      if (!this.inTransaction) {
         var4 = (StoreItem)this.datas.get(var3);
         if (var4 != null) {
            var4.value = var2;
         } else {
            this.datas.put(var3, var3);
         }
      } else {
         var4 = (StoreItem)this.trdatas.get(var3);
         if (var4 != null) {
            var4.value = var2;
         } else {
            this.trdatas.put(var3, var3);
         }
      }

   }

   private String _get(Object var1) {
      StoreItem var2;
      if (var1 instanceof Object[]) {
         var2 = this.convert((Object[])((Object[])var1), "");
      } else {
         var2 = this.convert(var1, "");
      }

      StoreItem var3;
      if (!this.inTransaction) {
         try {
            var3 = (StoreItem)this.datas.get(var2);
            return var3 != null ? var3.value.toString() : null;
         } catch (Exception var4) {
            return null;
         }
      } else {
         var3 = (StoreItem)this.trdatas.get(var2);
         return var3 != null ? var3.value.toString() : null;
      }
   }

   private void _beginTransaction() {
      if (!this.inTransaction) {
         this.inTransaction = true;
         this.trdatas.clear();
         this.trdatas.putAll(this.datas);
      }

   }

   private void _rollbackTransaction() {
      this.inTransaction = false;
      this.trdatas.clear();
   }

   private void _commitTransaction() {
      this.inTransaction = false;
      this.datas.clear();
      this.datas.putAll(this.trdatas);
      this.trdatas.clear();
   }

   private void _reset() {
      if (this.inTransaction) {
         this.trdatas.clear();
      } else {
         this.datas.clear();
      }

   }

   private void _remove(Object var1) {
      StoreItem var2;
      if (var1 instanceof Object[]) {
         var2 = this.convert((Object[])((Object[])var1), "");
      } else {
         var2 = this.convert(var1, "");
      }

      StoreItem var3;
      if (this.inTransaction) {
         if (this.trdatas.contains(var2)) {
            var3 = (StoreItem)this.trdatas.get(var2);
            var3.index = -1;
         }
      } else if (this.datas.contains(var2)) {
         var3 = (StoreItem)this.datas.get(var2);
         var3.index = -1;
      }

   }

   public Iterator getCaseIdIterator() {
      return this.datas.values().iterator();
   }

   public Object getMasterCaseId(Object var1) {
      StoreItem var2;
      if (var1 instanceof Object[]) {
         var2 = this.convert((Object[])((Object[])var1), "");
      } else {
         var2 = this.convert(var1, "");
      }

      if (this.datas.contains(var2)) {
         StoreItem var3 = (StoreItem)this.datas.get(var2);
         return var3;
      } else {
         return var2;
      }
   }

   public Object getStatusFlag() {
      return new Boolean[]{new Boolean(this.dirty)};
   }

   public void setStatusFlag(Object var1) {
      try {
         if (var1 instanceof Object[]) {
            Object[] var2 = (Object[])((Object[])var1);
            this.dirty = (Boolean)var2[0];
         }
      } catch (Exception var3) {
      }

   }

   public void print() {
      int var1 = 0;
      System.out.println("Datastore tartalma:" + this.datas.size());
      Enumeration var2 = this.datas.elements();

      while(var2.hasMoreElements()) {
         StoreItem var3 = (StoreItem)var2.nextElement();
         if (!var3.value.equals("")) {
            System.out.println("" + var3.toString() + " = " + var3.value);
            ++var1;
         }
      }

      System.out.println("Nem üres =" + var1);
      System.out.println("Üres =" + (this.datas.size() - var1));
   }

   public void setDetails(String var1, Vector var2, String var3) {
      StoreItem var4 = this.convert((Object)var1, "");
      StoreItem var5 = (StoreItem)this.datas.get(var4);
      var5.setDetailSum(var3);
      var5.setDetail(var2);
   }

   private synchronized String done(int var1, Object var2, String var3) {
      switch(var1) {
      case 0:
         this._set(var2, var3);
         break;
      case 1:
         return this._get(var2);
      case 2:
         this._reset();
         break;
      case 3:
         this._beginTransaction();
         break;
      case 4:
         this._rollbackTransaction();
         break;
      case 5:
         this._commitTransaction();
         break;
      case 6:
         this._remove(var2);
         break;
      case 7:
         this._shift(var2);
      }

      return null;
   }

   private StoreItem convert(Object var1, String var2) {
      try {
         if (var1 instanceof StoreItem) {
            StoreItem var8 = new StoreItem();
            var8.code = ((StoreItem)var1).code;
            var8.index = ((StoreItem)var1).index;
            var8.value = var2;
            return var8;
         } else {
            String[] var3 = ((String)var1).split("_", 2);
            StoreItem var4 = new StoreItem();
            var4.code = var3[1];

            try {
               var4.index = Integer.parseInt(var3[0]);
            } catch (NumberFormatException var6) {
               var4.index = -1;
            }

            var4.value = var2;
            return var4;
         }
      } catch (Exception var7) {
         return null;
      }
   }

   private StoreItem convert(Object[] var1, String var2) {
      try {
         StoreItem var3 = new StoreItem();
         var3.code = (String)var1[1];

         try {
            var3.index = (Integer)var1[0];
         } catch (NumberFormatException var5) {
            var3.index = -1;
         }

         var3.value = var2;
         return var3;
      } catch (Exception var6) {
         return null;
      }
   }

   private String getdata() {
      return this.inTransaction ? this.trdatas.toString() : this.datas.toString();
   }

   private String getdata(int var1) {
      String var2 = null;
      StringBuffer var3 = new StringBuffer();
      switch(var1) {
      case 0:
         var2 = "SET";
         break;
      case 1:
         var2 = "GET";
         break;
      case 2:
         var2 = "RESET";
         break;
      case 3:
         var2 = "BEGIN";
         break;
      case 4:
         var2 = "ROLLBACK";
         break;
      case 5:
         var2 = "COMMIT";
         break;
      case 6:
         var2 = "REMOVE";
         break;
      case 7:
         var2 = "SHIFT";
      }

      Enumeration var4;
      Object var5;
      if (this.inTransaction) {
         var4 = this.datas.keys();

         while(var4.hasMoreElements()) {
            var5 = var4.nextElement();
            var3.append(this.datas.get(var5).toString());
            var3.append(" ");
         }
      } else {
         var4 = this.trdatas.keys();

         while(var4.hasMoreElements()) {
            var5 = var4.nextElement();
            var3.append(this.trdatas.get(var5).toString());
            var3.append(" ");
         }
      }

      return var2 + " " + var3;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      Enumeration var2 = this.datas.keys();

      while(var2.hasMoreElements()) {
         Object var3 = var2.nextElement();
         var1.append(this.datas.get(var3).toString());
         var1.append("\n");
      }

      return var1.toString();
   }

   public boolean containValue(String var1, String var2) {
      Enumeration var3;
      StoreItem var4;
      if (var2 != null) {
         var3 = this.datas.keys();

         do {
            if (!var3.hasMoreElements()) {
               return false;
            }

            var4 = (StoreItem)var3.nextElement();
         } while(!var4.code.equals(var2) || var4.value.toString().indexOf(var1) == -1);

         return true;
      } else {
         var3 = this.datas.keys();

         do {
            if (!var3.hasMoreElements()) {
               return false;
            }

            var4 = (StoreItem)var3.nextElement();
         } while(var4.value.toString().indexOf(var1) == -1);

         return true;
      }
   }

   public boolean isCcdirty() {
      return this.ccdirty;
   }

   public void setCcdirty(boolean var1) {
      this.ccdirty = var1;
   }

   public void setSavepoint(BookModel var1) {
      this.handleTextAreas(var1);
      this.basedatas.clear();
      Enumeration var2 = this.datas.keys();

      while(var2.hasMoreElements()) {
         StoreItem var3 = (StoreItem)var2.nextElement();
         this.basedatas.put(var3.toString(), var3.value);
      }

      this.wasSavepoint = true;
   }

   public void setSavepoint(Hashtable<String, String> var1, BookModel var2) {
      this.handleTextAreas(var2);
      if (var1 == null) {
         this.setSavepoint(var2);
      } else {
         this.basedatas.clear();
         Enumeration var3 = this.datas.keys();

         while(var3.hasMoreElements()) {
            StoreItem var4 = (StoreItem)var3.nextElement();
            if (var1.containsKey(var4.toString())) {
               this.basedatas.put(var4.toString(), var1.get(var4.toString()));
            } else {
               this.basedatas.put(var4.toString(), var4.value);
            }
         }

         this.wasSavepoint = true;
      }
   }

   public Hashtable getChangedValues() {
      Hashtable var1 = new Hashtable();
      Enumeration var2 = this.datas.keys();

      while(true) {
         StoreItem var3;
         String var4;
         while(true) {
            if (!var2.hasMoreElements()) {
               return var1;
            }

            var3 = (StoreItem)var2.nextElement();
            var4 = var3.toString();
            if (this.basedatas.containsKey(var4)) {
               if (this.basedatas.get(var4).equals(var3.value)) {
                  continue;
               }
               break;
            } else if (!"".equals(var3.value) && !"false".equals(var3.value)) {
               break;
            }
         }

         var1.put(var4, var3.value);
      }
   }

   public boolean hasChangedValue() {
      Enumeration var1 = this.datas.keys();
      boolean var2 = false;

      while(var1.hasMoreElements() && !var2) {
         StoreItem var3 = (StoreItem)var1.nextElement();
         String var4 = var3.toString();
         if (this.basedatas.containsKey(var4)) {
            if (this.basedatas.get(var4).equals(var3.value)) {
               continue;
            }
         } else if ("".equals(var3.value) || "false".equals(var3.value)) {
            continue;
         }

         var2 = true;
      }

      return var2;
   }

   public void putExtraValues(Hashtable var1, Datastore_history var2) {
      if (var1 != null) {
         if (var2 != null) {
            Enumeration var3 = this.datas.keys();

            while(true) {
               StoreItem var4;
               String var5;
               Vector var6;
               byte var7;
               while(true) {
                  if (!var3.hasMoreElements()) {
                     return;
                  }

                  var4 = (StoreItem)var3.nextElement();
                  var5 = var4.toString();
                  var6 = var2.get(var5);
                  var7 = 1;
                  if ("2".equals(MainFrame.role) || "3".equals(MainFrame.role)) {
                     var7 = 2;
                  }

                  try {
                     if (!var6.get(var7).equals(var6.get(var7 - 1))) {
                        break;
                     }

                     if (var2.check(var5, var4.value, var7)) {
                        var1.put(var5, var4.value);
                     }
                  } catch (Exception var10) {
                     break;
                  }
               }

               try {
                  if (var6.get(var7).equals(var6.get(0)) && var2.check(var5, var4.value, var7)) {
                     var1.put(var5, var4.value);
                  }
               } catch (Exception var9) {
               }
            }
         }
      }
   }

   public Hashtable getStat(String var1) {
      Hashtable var2 = new Hashtable();
      Hashtable var3 = new Hashtable();
      int var4 = 0;
      int var5 = 0;
      Hashtable var6 = CalculatorManager.getInstance().get_rogz_stat_exc_fids_list(var1);
      Enumeration var7 = this.datas.keys();

      while(var7.hasMoreElements()) {
         StoreItem var8 = (StoreItem)var7.nextElement();
         if (var8.index >= 0 && !var6.containsKey(var8.code) && var8.code.length() >= 11 && var8.value.toString().length() != 0) {
            String var9 = var8.index + "_" + var8.code.substring(0, 2);
            var3.put(var9, "");
            var5 += var8.value.toString().length();
            ++var4;
         }
      }

      int var10 = var3.size();
      var2.put("pagenum", new BigDecimal(var10));
      var2.put("fieldnum", new BigDecimal(var4));
      var2.put("totalcharnum", new BigDecimal(var5));
      return var2;
   }

   public void setCcindex(int var1) {
      this.ccindex = var1;
   }

   private void handleTextAreas(BookModel var1) {
      try {
         ArrayList var2 = new ArrayList(this.datas.keySet());
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            StoreItem var4 = (StoreItem)var3.next();
            if (var4.value != null && var4.value.toString().contains("\n") && var1.isInTextArea(var4.code)) {
               String var5 = var4.value.toString().replaceAll("\n", " ");
               ((StoreItem)this.datas.get(var4)).value = var5;
            }
         }
      } catch (Exception var6) {
      }

   }
}
