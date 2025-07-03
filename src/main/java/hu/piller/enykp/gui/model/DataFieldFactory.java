package hu.piller.enykp.gui.model;

import hu.piller.enykp.gui.component.ENYKComponentFactory;
import hu.piller.enykp.gui.component.ENYKFormattedTaggedTextFieldPainter;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class DataFieldFactory {
   public static final int TEXTFIELD = 0;
   public static final int CHECKBOX = 1;
   public static final int COMBOBOX = 2;
   public static final int TEXTAREA = 3;
   public static final int DATEFIELD = 4;
   public static final int TAGGEDTEXTFIELD = 5;
   public static final int TAGGEDCOMBO = 6;
   public static final int FORMATTEDTEXTFIELD = 7;
   public static final int SCROLLTEXTAREA = 8;
   private static DataFieldFactory ourInstance = new DataFieldFactory();
   private JPanel empty;
   private JComponent tf;
   private JComponent p_tf;
   private JComponent cb;
   private JComponent p_cb;
   private JComponent combo;
   private JComponent p_combo;
   private JComponent taf;
   private JComponent p_taf;
   private JComponent df;
   private JComponent p_df;
   private JComponent ttf;
   private JComponent p_ttf;
   private JComponent tc;
   private JComponent p_tc;
   private JComponent ftf;
   private JComponent p_ftf;
   private JComponent staf;
   private JComponent p_staf;

   public static DataFieldFactory getInstance() {
      return ourInstance;
   }

   private DataFieldFactory() {
      ENYKComponentFactory var1 = new ENYKComponentFactory();
      this.empty = new JPanel();
      this.tf = (JComponent)var1.get("text_field");
      this.cb = (JComponent)var1.get("check_box");
      this.combo = (JComponent)var1.get("filter_combo");
      this.taf = (JComponent)var1.get("text_area");
      this.df = (JComponent)var1.get("date_combo");
      this.ttf = (JComponent)var1.get("formatted_tagged_text_field");
      this.tc = (JComponent)var1.get("tagged_combo");
      this.ftf = (JComponent)var1.get("formatted_text_field");
      this.staf = (JComponent)var1.get("scroll_text_area");
      this.tf.setName("datafield");
      this.cb.setName("datafield");
      this.combo.setName("datafield");
      this.taf.setName("datafield");
      this.df.setName("datafield");
      this.ttf.setName("datafield");
      this.tc.setName("datafield");
      this.ftf.setName("datafield");
      this.staf.setName("datafield");
      this.p_tf = (JComponent)var1.get("text_field");
      this.p_cb = (JComponent)var1.get("check_box");
      this.p_combo = (JComponent)var1.get("filter_combo");
      this.p_taf = (JComponent)var1.get("text_area");
      this.p_df = (JComponent)var1.get("date_combo");
      this.p_ttf = (JComponent)var1.get("formatted_tagged_text_field_painter");
      this.p_tc = (JComponent)var1.get("tagged_combo");
      this.p_ftf = (JComponent)var1.get("formatted_text_field");
      this.p_staf = (JComponent)var1.get("scroll_text_area");
   }

   public JComponent getJComponent(int var1) {
      switch(var1) {
      case 0:
         return this.tf;
      case 1:
         return this.cb;
      case 2:
         return this.combo;
      case 3:
         return this.taf;
      case 4:
         return this.df;
      case 5:
         return this.ttf;
      case 6:
         return this.tc;
      case 7:
         return this.ftf;
      case 8:
         return this.staf;
      default:
         return this.empty;
      }
   }

   public JComponent getPainter(int var1) {
      switch(var1) {
      case 0:
         return this.p_tf;
      case 1:
         return this.p_cb;
      case 2:
         return this.p_combo;
      case 3:
         return this.p_taf;
      case 4:
         return this.p_df;
      case 5:
         return this.p_ttf;
      case 6:
         return this.p_tc;
      case 7:
         return this.p_ftf;
      case 8:
         return this.p_staf;
      default:
         return this.empty;
      }
   }

   public void freemem() {
      try {
         ((ENYKFormattedTaggedTextFieldPainter)this.p_ttf).clearImages();
      } catch (Exception var2) {
      }

   }
}
