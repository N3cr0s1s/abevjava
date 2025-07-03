package hu.piller.enykp.gui.model;

import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.print.simpleprint.KPrintFormFeedType;
import hu.piller.enykp.print.simpleprint.KPrintPageType;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Area;
import java.util.Hashtable;
import java.util.Vector;
import org.xml.sax.Attributes;

public class PageModel {
   FormModel fm;
   public Vector y_sorted_df;
   public Vector tab_sorted_df;
   public Vector z_sorted_vf;
   public Area A;
   public String pid;
   public String name;
   public String title;
   public int orientation;
   public int size;
   public Dimension psize;
   public boolean dynamic;
   public int maxpage;
   public Hashtable xmlht = new Hashtable();
   public int role;
   private String hide;
   private KPrintPageType kpLapTipus;
   private KPrintFormFeedType kpLapDobas;
   private boolean kpLandscape;

   public PageModel(Attributes var1) {
      this.attributes_done(var1, this.xmlht);
      this.y_sorted_df = new Vector();
      this.tab_sorted_df = new Vector(10001);

      for(int var2 = 0; var2 < 10000; ++var2) {
         this.tab_sorted_df.add((Object)null);
      }

      this.z_sorted_vf = new Vector();
      this.A = new Area();
      this.pid = var1.getValue("pid");
      this.name = var1.getValue("name");
      this.title = var1.getValue("title");
      String var10 = var1.getValue("orientation");
      if (var10 == null) {
         var10 = "portrait";
      }

      this.orientation = 1;
      if (var10.equals("portrait")) {
         this.orientation = 0;
      }

      String var3 = var1.getValue("size");
      if (var3 == null) {
         var3 = "A4";
      }

      if (var3.equals("A4")) {
         this.size = 0;
      } else {
         this.size = 1;
      }

      short var4 = 0;
      short var5 = 0;
      if (this.orientation == 0) {
         switch(this.size) {
         case 0:
            var4 = 800;
            var5 = 1140;
         }
      } else {
         switch(this.size) {
         case 0:
            var4 = 1140;
            var5 = 800;
         }
      }

      this.psize = new Dimension(var4, var5);
      this.dynamic = false;
      String var6 = var1.getValue("dynamic");
      if (var6 != null && var6.equals("yes")) {
         this.dynamic = true;
      }

      String var7 = var1.getValue("maxpagecount");
      this.maxpage = 1;
      if (var7 != null) {
         try {
            this.maxpage = Integer.parseInt(var7);
         } catch (NumberFormatException var9) {
            this.maxpage = 1;
         }
      }

      this.role = this.get_int(var1.getValue("role"), 15);
      int var8 = 0;
      this.hide = var1.getValue("hide");
      if (this.hide != null) {
         if (this.hide.contains("A") && (this.role & 1) != 0) {
            ++var8;
         }

         if (this.hide.contains("J") && (this.role & 2) != 0) {
            var8 += 2;
         }

         if (this.hide.contains("R") && (this.role & 4) != 0) {
            var8 += 4;
         }

         if (this.hide.contains("U") && (this.role & 8) != 0) {
            var8 += 8;
         }
      } else {
         this.hide = "";
      }

      this.role -= var8;
      this.kpLapTipus = KPrintPageType.fromValue(var1.getValue("kprint_pagetype"));
      this.kpLapDobas = KPrintFormFeedType.fromValue(var1.getValue("kprint_formfeed"));
      this.kpLandscape = "true".equalsIgnoreCase(var1.getValue("kprint_landscape"));
      if (this.kpLapTipus == KPrintPageType.AU) {
         this.role = 0;
      }

   }

   public void addDF(DataFieldModel var1) {
      if (!var1.visible) {
         this.fm.invisible_fields.put(var1.key, var1);
      }

      if (var1.key.length() < 11) {
         this.fm.shortcoded_fields.put(var1.key, var1);
      }

      this.A.add(new Area(var1.r));
      this.tab_sorted_df.setElementAt(var1, var1.tabindex);

      for(int var2 = 0; var2 < this.y_sorted_df.size(); ++var2) {
         if (((DataFieldModel)this.y_sorted_df.get(var2)).y == var1.y && ((DataFieldModel)this.y_sorted_df.get(var2)).x > var1.x) {
            this.y_sorted_df.insertElementAt(var1, var2);
            return;
         }

         if (((DataFieldModel)this.y_sorted_df.get(var2)).y > var1.y) {
            this.y_sorted_df.insertElementAt(var1, var2);
            return;
         }
      }

      this.y_sorted_df.add(var1);
   }

   public void addVF(Object var1) {
      this.z_sorted_vf.add(var1);
   }

   public DataFieldModel getAt(Point var1) {
      int var2 = this.y_sorted_df.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         DataFieldModel var4 = (DataFieldModel)this.y_sorted_df.get(var3);
         if (var4.y > var1.y) {
            break;
         }

         if (var4.contains(var1)) {
            if (!var4.visible) {
               return null;
            }

            if ((var4.role & this.getmask()) == 0) {
               return null;
            }

            return var4;
         }
      }

      return null;
   }

   public int getmask() {
      if (MainFrame.role.equals("0")) {
         return 1;
      } else if (MainFrame.role.equals("1")) {
         return 2;
      } else if (MainFrame.role.equals("2")) {
         return 4;
      } else {
         return MainFrame.role.equals("3") ? 8 : 1;
      }
   }

   public DataFieldModel getAt2(Point var1) {
      try {
         int var2 = this.y_sorted_df.size();

         for(int var3 = 0; var3 < var2; ++var3) {
            DataFieldModel var4 = (DataFieldModel)this.y_sorted_df.get(var3);
            if (var4.y > var1.y) {
               break;
            }

            if (var4.contains(var1)) {
               if (!var4.visible) {
                  return null;
               }

               if ((var4.role & VisualFieldModel.getmask()) == 0) {
                  return null;
               }

               return var4;
            }
         }
      } catch (Exception var5) {
      }

      return null;
   }

   public void setFormModel(FormModel var1) {
      this.fm = var1;
   }

   public FormModel getFormModel() {
      return this.fm;
   }

   public DataFieldModel getByCode(String var1) {
      DataFieldModel var2 = (DataFieldModel)this.fm.fids.get(var1);
      if (var2 == null) {
         return null;
      } else if (var2.readonly) {
         return null;
      } else {
         return this.equals(this.fm.fids_page.get(var1)) ? var2 : null;
      }
   }

   public DataFieldModel getByCodeAll(String var1) {
      DataFieldModel var2 = (DataFieldModel)this.fm.fids.get(var1);
      if (var2 == null) {
         return null;
      } else {
         return this.equals(this.fm.fids_page.get(var1)) ? var2 : null;
      }
   }

   public DataFieldModel getFirst() {
      return this.getNext(0);
   }

   public DataFieldModel getNext(int var1) {
      int var2;
      DataFieldModel var3;
      for(var2 = var1; var2 < this.tab_sorted_df.size(); ++var2) {
         var3 = (DataFieldModel)this.tab_sorted_df.get(var2);
         if (var3 != null && var3.visible && !var3.readonly && (var3.role & this.getmask()) != 0 && (!MainFrame.rogzitomode || !var3.key.equals(MainFrame.fixfidcode))) {
            return var3;
         }
      }

      for(var2 = 0; var2 < var1; ++var2) {
         var3 = (DataFieldModel)this.tab_sorted_df.get(var2);
         if (var3 != null && var3.visible && !var3.readonly && (var3.role & this.getmask()) != 0 && (!MainFrame.rogzitomode || !var3.key.equals(MainFrame.fixfidcode))) {
            return var3;
         }
      }

      return null;
   }

   public DataFieldModel getPrev(int var1) {
      int var2;
      DataFieldModel var3;
      for(var2 = var1; -1 < var2; --var2) {
         var3 = (DataFieldModel)this.tab_sorted_df.get(var2);
         if (var3 != null && var3.visible && !var3.readonly && (var3.role & this.getmask()) != 0 && (!MainFrame.rogzitomode || !var3.key.equals(MainFrame.fixfidcode))) {
            return var3;
         }
      }

      for(var2 = this.tab_sorted_df.size() - 1; var1 < var2; --var2) {
         var3 = (DataFieldModel)this.tab_sorted_df.get(var2);
         if (var3 != null && var3.visible && !var3.readonly && (var3.role & this.getmask()) != 0 && (!MainFrame.rogzitomode || !var3.key.equals(MainFrame.fixfidcode))) {
            return var3;
         }
      }

      return null;
   }

   private void attributes_done(Attributes var1, Hashtable var2) {
      for(int var3 = 0; var3 < var1.getLength(); ++var3) {
         var2.put(var1.getQName(var3), var1.getValue(var3));
      }

   }

   public void clear() {
      if (this.z_sorted_vf != null) {
         this.z_sorted_vf.clear();
      }

      if (this.y_sorted_df != null) {
         this.y_sorted_df.clear();
      }

      if (this.tab_sorted_df != null) {
         this.tab_sorted_df.clear();
      }

      if (this.xmlht != null) {
         this.xmlht.clear();
      }

   }

   public void destroy() {
      this.fm = null;
      if (this.z_sorted_vf != null) {
         this.z_sorted_vf.clear();
      }

      this.z_sorted_vf = null;
      if (this.y_sorted_df != null) {
         this.y_sorted_df.clear();
      }

      this.y_sorted_df = null;
      if (this.tab_sorted_df != null) {
         this.tab_sorted_df.clear();
      }

      this.tab_sorted_df = null;
      if (this.xmlht != null) {
         this.xmlht.clear();
      }

      this.xmlht = null;
   }

   private int get_int(Object var1, int var2) {
      try {
         return Integer.parseInt(var1.toString());
      } catch (Exception var4) {
         return var2;
      }
   }

   public String toString() {
      return this.name;
   }

   public KPrintPageType getKpLapTipus() {
      return this.kpLapTipus;
   }

   public KPrintFormFeedType getKpLapDobas() {
      return this.kpLapDobas;
   }

   public boolean isKpLandscape() {
      return this.kpLandscape;
   }

   public int getCalculatedHide() {
      int var1 = 0;
      if (this.hide.contains("A")) {
         ++var1;
      }

      if (this.hide.contains("J")) {
         var1 += 2;
      }

      if (this.hide.contains("R")) {
         var1 += 4;
      }

      if (this.hide.contains("U")) {
         var1 += 8;
      }

      return 15 - var1;
   }
}
