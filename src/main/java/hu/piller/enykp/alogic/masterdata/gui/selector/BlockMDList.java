package hu.piller.enykp.alogic.masterdata.gui.selector;

import hu.piller.enykp.alogic.masterdata.core.Block;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityException;
import hu.piller.enykp.alogic.masterdata.core.EntityHome;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

public class BlockMDList extends MDList {
   private MDList ref;
   private Hashtable<String, String> blockNames;

   public BlockMDList() {
      this.setType("b");
      this.blockNames = new Hashtable();
   }

   public void setRef(MDList var1) {
      this.ref = var1;
      this.getRef().getCombo().getCombo().addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent var1) {
            BlockMDList.this.updateGUI();
         }

         public void focusLost(FocusEvent var1) {
            BlockMDList.this.updateGUI();
         }
      });
      this.getRef().getCombo().getBtn().addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            BlockMDList.this.updateGUI();
         }
      });
   }

   public MDList getRef() {
      return this.ref;
   }

   public void setBlockName(String var1, String var2) {
      this.blockNames.put(var1, var2);
   }

   public String getBlockName(String var1) {
      return (String)this.blockNames.get(var1);
   }

   protected void fillModel() {
      this.clear();
      if (this.ref != null) {
         try {
            if (this.ref.getSelected() != -1L) {
               Entity var1 = (new EntityHome()).findByID(this.ref.getSelected());
               if (var1 != null) {
                  Block[] var2 = var1.getBlocks((String)this.blockNames.get(var1.getName()));
                  int var3 = var2.length;

                  for(int var4 = 0; var4 < var3; ++var4) {
                     Block var5 = var2[var4];
                     if (!var5.isEmpty()) {
                        this.add(this.processToAbstract(var1, var5), var5.getSeq());
                     }
                  }
               }
            }
         } catch (EntityException var6) {
            var6.printStackTrace();
         }
      }

   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.toString());
      var1.append("ref=");
      var1.append(this.ref.getId());
      var1.append("\nblockNames\n");
      Iterator var2 = this.blockNames.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.append(var3.getKey());
         var1.append(": ");
         var1.append(var3.getValue());
         var1.append("\n");
      }

      return var1.toString();
   }
}
