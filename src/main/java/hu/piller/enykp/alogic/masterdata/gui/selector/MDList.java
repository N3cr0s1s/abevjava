package hu.piller.enykp.alogic.masterdata.gui.selector;

import hu.piller.enykp.alogic.masterdata.core.Block;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.component.filtercombo.ENYKFilterComboPanel;
import java.awt.Dimension;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

public abstract class MDList {
   public static final String ENTITY_LIST = "e";
   public static final String BLOCK_LIST = "b";
   private int label_padding;
   private String label;
   private String type;
   private String id;
   private Hashtable<String, Integer> model = new Hashtable();
   private Hashtable<String, Part[]> parts = new Hashtable();
   private ENYKFilterComboPanel combo = new ENYKFilterComboPanel();

   protected MDList() {
      int var1 = GuiUtil.getCommonItemHeight();
      int var2 = Math.max(350, 350 + 350 * (var1 - 12) / 100);
      var1 += 2;
      this.combo.setMinimumSize(new Dimension(var2, var1));
      this.combo.setSize(new Dimension(var2, var1));
      this.combo.setPreferredSize(new Dimension(var2, var1));
      this.combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, var1));
      this.combo.getCombo().LISTWIDTH = var2;
      this.combo.getCombo().maxColWidth = var2;
   }

   public ENYKFilterComboPanel getCombo() {
      return this.combo;
   }

   public int getLabelPadding() {
      return this.label_padding;
   }

   public void setLabelPadding(int var1) {
      this.label_padding = var1;
   }

   public void setType(String var1) {
      this.type = var1;
   }

   public void setId(String var1) {
      this.id = var1;
   }

   public void setLabel(String var1) {
      this.label = var1;
   }

   public String getLabel() {
      return this.label;
   }

   public void clear() {
      this.model.clear();
   }

   public void add(String var1, Integer var2) {
      this.model.put(var1, var2);
   }

   public String getType() {
      return this.type;
   }

   public String getId() {
      return this.id;
   }

   public long getSelected() {
      if (this.combo.getText() != null && !"".equals(this.combo.getText())) {
         Integer var1 = (Integer)this.model.get(this.combo.getText());
         return var1 == null ? -1L : (long)var1;
      } else {
         return -1L;
      }
   }

   public void addPart(String var1, Part var2) {
      if (!this.parts.containsKey(var1)) {
         this.parts.put(var1, new Part[0]);
      }

      Part[] var3 = new Part[((Part[])this.parts.get(var1)).length + 1];
      int var4 = 0;
      Part[] var5 = (Part[])this.parts.get(var1);
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Part var8 = var5[var7];
         var3[var4++] = var8;
      }

      var3[var4] = var2;
      this.parts.put(var1, var3);
   }

   protected abstract void fillModel();

   protected String processToAbstract(Entity var1, Block var2) {
      StringBuffer var3 = new StringBuffer();
      Part[] var4 = (Part[])this.parts.get(var1.getName());
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Part var7 = var4[var6];
         if (var2 != null && var7.getBlock().equals(var2.getName())) {
            var3.append(var2.getMasterData(var7.getMd()).getValue());
         } else {
            var3.append(var1.getBlock(var7.getBlock(), 1).getMasterData(var7.getMd()).getValue());
         }

         var3.append(" ");
      }

      return var3.toString();
   }

   public void updateGUI() {
      this.fillModel();
      this.fillCombo();
   }

   private void fillCombo() {
      Vector[] var1 = new Vector[2];
      Object var2 = this.model.keySet();
      if (((Set)var2).size() == 0) {
         var2 = new TreeSet();
         ((Set)var2).add("");
      }

      var1[0] = new Vector((Collection)var2);
      var1[1] = new Vector((Collection)var2);
      this.combo.setFeature(var1);
      this.combo.setText("");
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("type=");
      var1.append(this.type);
      var1.append(" id=");
      var1.append(this.id);
      var1.append("\nParts:\n");
      Iterator var2 = this.parts.keySet().iterator();

      String var3;
      while(var2.hasNext()) {
         var3 = (String)var2.next();
         var1.append(var3);
         var1.append(": ");
         Part[] var4 = (Part[])this.parts.get(var3);
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Part var7 = var4[var6];
            var1.append(var7);
            var1.append(" ");
         }

         var1.append("\n");
      }

      var1.append("Model:\n");
      var2 = this.model.keySet().iterator();

      while(var2.hasNext()) {
         var3 = (String)var2.next();
         var1.append(var3);
         var1.append(" -> ");
         var1.append(this.model.get(var3));
         var1.append("\n");
      }

      return var1.toString();
   }
}
