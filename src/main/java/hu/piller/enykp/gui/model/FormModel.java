package hu.piller.enykp.gui.model;

import hu.piller.enykp.alogic.metainfo.MetaInfo;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.xml.sax.Attributes;

public class FormModel {
   public BookModel bm;
   public Vector pages;
   public Hashtable fids;
   public Hashtable labels;
   public Hashtable fids_page;
   public String id;
   public String name;
   public String guiver;
   public String fnver;
   public String docver;
   public String help;
   public String mainlabel;
   public String attachement;
   public String kp_give;
   public int maxcreation;
   public Hashtable pids_page;
   public Hashtable names_page;
   public Hashtable irids;
   public Hashtable images;
   public Hashtable invisible_fields;
   public Hashtable shortcoded_fields;
   public Hashtable kvprintht;
   public Hashtable attribs = new Hashtable();
   Hashtable short_inv_not_full_fields;
   Vector specvector;
   Hashtable specht;

   public FormModel(Attributes var1) {
      for(int var2 = 0; var2 < var1.getLength(); ++var2) {
         this.attribs.put(var1.getQName(var2), var1.getValue(var2));
      }

      this.pages = new Vector();
      this.fids = new Hashtable();
      this.fids_page = new Hashtable();
      this.pids_page = new Hashtable();
      this.names_page = new Hashtable();
      this.labels = new Hashtable();
      this.irids = new Hashtable();
      this.images = new Hashtable();
      this.id = var1.getValue("id");
      this.name = var1.getValue("name");
      this.guiver = var1.getValue("guiver");
      this.fnver = var1.getValue("fnver");
      this.docver = var1.getValue("docver");
      String var5 = var1.getValue("maxcreation");
      this.help = var1.getValue("help");
      this.attachement = var1.getValue("attachment");
      this.kp_give = var1.getValue("kp_give");
      if (var5 != null) {
         try {
            this.maxcreation = Integer.parseInt(var5);
         } catch (NumberFormatException var4) {
            this.maxcreation = Integer.MAX_VALUE;
         }
      } else {
         this.maxcreation = Integer.MAX_VALUE;
      }

      this.invisible_fields = new Hashtable();
      this.shortcoded_fields = new Hashtable();
   }

   public int size() {
      return this.pages.size();
   }

   public PageModel get(int var1) {
      return (PageModel)this.pages.get(var1);
   }

   public void addPage(PageModel var1) {
      this.pages.add(var1);
      this.pids_page.put(var1.pid, var1);
      this.names_page.put(var1.name, var1);
   }

   public void setBookModel(BookModel var1) {
      this.bm = var1;
      this.mainlabel = (String)var1.docinfo.get("info");
   }

   public BookModel getBookModel() {
      return this.bm;
   }

   public int get(PageModel var1) {
      return this.pages.indexOf(var1);
   }

   public String toString() {
      return this.id;
   }

   public int get_field_pageindex(String var1) {
      try {
         int var2 = this.get((PageModel)this.fids_page.get(var1));
         return var2;
      } catch (Exception var3) {
         return -1;
      }
   }

   public int getPageindex(String var1) {
      return this.get((PageModel)this.names_page.get(var1));
   }

   public Hashtable get_invisible_fields() {
      return this.invisible_fields;
   }

   public Hashtable init_short_inv_fields() {
      Hashtable var1 = new Hashtable(this.invisible_fields);
      Enumeration var2 = this.shortcoded_fields.keys();

      while(var2.hasMoreElements()) {
         Object var3 = var2.nextElement();
         var1.put(var3, var3);
      }

      return var1;
   }

   public Hashtable get_short_inv_not_full_fields() {
      if (this.short_inv_not_full_fields == null) {
         Hashtable var1 = this.init_short_inv_fields();
         Enumeration var2 = var1.keys();

         while(var2.hasMoreElements()) {
            String var3 = (String)var2.nextElement();
            if (var3.length() > 10) {
               var1.remove(var3);
            }
         }

         this.short_inv_not_full_fields = var1;
      }

      return this.short_inv_not_full_fields;
   }

   public Vector get_short_inv_fields() {
      if (this.specvector == null) {
         Hashtable var1 = this.init_short_inv_fields();
         this.specvector = new Vector(var1.size());
         Enumeration var2 = var1.keys();

         while(var2.hasMoreElements()) {
            Object var3 = var2.nextElement();
            this.specvector.add(var3);
         }
      }

      return this.specvector;
   }

   public Hashtable get_short_inv_fields_ht() {
      if (this.specht == null) {
         Vector var1 = this.get_short_inv_fields();
         this.specht = new Hashtable();

         for(int var2 = 0; var2 < var1.size(); ++var2) {
            Object var3 = var1.get(var2);
            this.specht.put(var3, var3);
         }

         Hashtable var4 = MetaInfo.getInstance().getFieldAttributes(this.id, "copy_fld", true);
         this.specht.putAll(var4);
         var4 = MetaInfo.getInstance().getFieldAttributes(this.id, "DPageNumber", false);
         this.specht.putAll(var4);
      }

      return this.specht;
   }

   public void destroy() {
      this.bm = null;

      for(int var1 = 0; var1 < this.pages.size(); ++var1) {
         ((PageModel)this.pages.get(var1)).destroy();
      }

   }

   public void addVF(Object var1) {
      if (var1 instanceof VisualFieldModel) {
         VisualFieldModel var2 = (VisualFieldModel)var1;
         if (var2.id != null) {
            this.labels.put(var2.id, var2);
         }
      }

   }

   public Hashtable get_docinfodoc() {
      try {
         return (Hashtable)((Hashtable)this.bm.docinfo.get("docs")).get(this.id);
      } catch (Exception var2) {
         return null;
      }
   }
}
