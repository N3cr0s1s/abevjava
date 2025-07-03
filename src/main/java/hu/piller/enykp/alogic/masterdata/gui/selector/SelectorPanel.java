package hu.piller.enykp.alogic.masterdata.gui.selector;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SelectorPanel extends JPanel {
   private Hashtable<String, MDList> lists = new Hashtable();
   private String curOrg = "";
   private Vector<String> orgs = new Vector();

   public SelectorPanel() {
      this.setLayout(new GridBagLayout());
      this.setBorder(BorderFactory.createTitledBorder("Törzsadat választás"));
   }

   public void showOrgPanel(String var1) {
      this.curOrg = var1 == null ? "" : var1;
      this.removeAll();
      GridBagConstraints var2 = new GridBagConstraints();
      var2.gridx = 5;
      var2.gridy = 5;
      var2.ipady = 7;
      var2.anchor = 17;

      for(Iterator var3 = this.getMDListIdsForCurOrg().iterator(); var3.hasNext(); ++var2.gridy) {
         String var4 = (String)var3.next();
         ((MDList)this.lists.get(var4)).updateGUI();
         JPanel var5 = new JPanel();
         var5.setAlignmentX(0.0F);
         var5.setLayout(new GridLayout(1, 2));
         var5.add(new JLabel(((MDList)this.lists.get(var4)).getLabel()));
         var5.add(((MDList)this.lists.get(var4)).getCombo());
         this.add(var5, var2);
      }

      this.validate();
      this.repaint();
   }

   private Set<String> getMDListIdsForCurOrg() {
      TreeSet var1 = new TreeSet();
      if (!"".equals(this.curOrg)) {
         Iterator var2 = this.lists.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            if (var3.startsWith(this.curOrg)) {
               var1.add(var3);
            }
         }
      }

      return var1;
   }

   public void addList(MDList var1) {
      this.lists.put(this.curOrg + "@" + var1.getId(), var1);
   }

   public MDList getListByOrgAndId(String var1, String var2) {
      String var3 = var1 + "@" + var2;
      return (MDList)this.lists.get(var3);
   }

   private MDList[] getListsByOrg(String var1) {
      Vector var2 = new Vector();
      Iterator var3 = this.lists.keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         String[] var5 = var4.split("@");
         if (var1.equals(var5[0])) {
            var2.add(this.lists.get(var4));
         }
      }

      return (MDList[])var2.toArray(new MDList[var2.size()]);
   }

   private Hashtable<String, MDList>[] filterByType(MDList[] var1) {
      Hashtable[] var4 = new Hashtable[2];

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var4[var5] = new Hashtable();
      }

      MDList[] var10 = var1;
      int var6 = var1.length;

      int var7;
      MDList var8;
      for(var7 = 0; var7 < var6; ++var7) {
         var8 = var10[var7];
         if ("e".equals(var8.getType())) {
            var4[0].put(var8.getId(), var8);
         }
      }

      var10 = var1;
      var6 = var1.length;

      for(var7 = 0; var7 < var6; ++var7) {
         var8 = var10[var7];
         if ("b".equals(var8.getType())) {
            String var9 = ((MDList)var4[0].get(((BlockMDList)var8).getRef().getId())).getId();
            var4[1].put(var9 + "@" + var8.getId(), var8);
         }
      }

      return var4;
   }

   public EntitySelection[] getEntitySelection() {
      Vector var3 = new Vector();
      Hashtable[] var4 = this.filterByType(this.getListsByOrg(this.curOrg));
      Iterator var5 = var4[0].keySet().iterator();

      while(true) {
         String var6;
         EntityMDList var7;
         do {
            if (!var5.hasNext()) {
               return (EntitySelection[])var3.toArray(new EntitySelection[var3.size()]);
            }

            var6 = (String)var5.next();
            var7 = (EntityMDList)var4[0].get(var6);
         } while(var7.getSelected() == -1L);

         EntitySelection var8 = new EntitySelection();
         var8.setEntityId(var7.getSelected());
         Iterator var9 = var4[1].keySet().iterator();

         while(var9.hasNext()) {
            String var10 = (String)var9.next();
            if (var10.startsWith(var6)) {
               BlockMDList var11 = (BlockMDList)var4[1].get(var10);
               String var12 = var11.getBlockName(var7.getTypeOfSelectedEntity());
               int var13 = (int)var11.getSelected();
               if (var13 != -1) {
                  var8.addBlockSelection(var12, var13);
               }
            }
         }

         var3.add(var8);
      }
   }

   public void setOrg(String var1) {
      this.curOrg = var1 == null ? "" : var1;
      if (!this.orgs.contains(var1)) {
         this.orgs.add(var1);
      }

   }

   public boolean hasEntry(String var1) {
      return this.orgs.contains(var1);
   }

   public String getOrg() {
      return this.curOrg;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.lists.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var1.append("Lista: ");
         var1.append(var3);
         var1.append("\n");
         var1.append(this.lists.get(var3));
      }

      return var1.toString();
   }
}
