package hu.piller.enykp.alogic.calculator.calculator_c;

import hu.piller.enykp.datastore.CachedCollection;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.util.base.Tools;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JButton;

public class GoToButton extends JButton implements ActionListener {
   private Elem guiFormObject;
   private String formId;
   private StoreItem storeItem;
   private String pageId;
   private Integer pageNumber;

   public GoToButton(String var1) {
      super(var1);
      super.addActionListener(this);
   }

   public void setFieldParams(Elem var1, StoreItem var2) {
      this.guiFormObject = var1;
      this.storeItem = var2;
   }

   public void setFieldParams(String var1, StoreItem var2) {
      this.formId = var1;
      this.storeItem = var2;
   }

   public void setPageParams(String var1, String var2, Integer var3) {
      this.formId = var1;
      this.pageId = var2;
      this.pageNumber = var3;
   }

   public void setPageParams(Elem var1, String var2, Integer var3) {
      this.guiFormObject = var1;
      this.pageId = var2;
      this.pageNumber = var3;
   }

   /** @deprecated */
   @Deprecated
   public void setFieldId(Object var1, BookModel var2, Object var3) {
      if (var3 instanceof Elem) {
         this.guiFormObject = (Elem)var3;
      }

      if (var1 instanceof Object[]) {
         Object[] var4 = (Object[])((Object[])var1);
         int var5 = var4.length;
         Object var6;
         if (var5 > 3) {
            var6 = var4[3];
            if (var6 instanceof StoreItem) {
               this.storeItem = (StoreItem)var6;
            }
         }

         if (var5 > 6) {
            var6 = var4[6];
            if (var6 instanceof String) {
               this.formId = (String)var6;
            }

            if (var6 instanceof Elem) {
               this.guiFormObject = (Elem)var6;
            }

            if (var2 != null) {
               if (var3 instanceof String) {
                  this.formId = (String)var3;
               }

               if (var3 instanceof Elem) {
                  this.guiFormObject = (Elem)var3;
               }
            }
         }
      }

   }

   public void actionPerformed(ActionEvent var1) {
      this.goToField();
   }

   public void goToField() {
      try {
         this.validateAttributes();
         this.reSetGuiFormObjectByFormId();
         GuiUtil.showelem(this.guiFormObject);
         if (this.storeItem == null) {
            GuiUtil.gotopage(this.pageId, this.pageNumber);
         } else {
            GuiUtil.gotoField(this.storeItem);
         }
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
      }

   }

   private void validateAttributes() throws Exception {
      if (this.guiFormObject == null && this.formId == null || this.storeItem == null && (this.pageId == null || this.pageNumber == null)) {
         GuiUtil.showMessageDialog((Component)null, "Érvénytelen mezőhivatkozás!", "Hiba", 0);
         throw new Exception("Érvénytelen mezőhivatkozás!");
      }
   }

   private void reSetGuiFormObjectByFormId() {
      if (this.guiFormObject == null) {
         CachedCollection var1 = GuiUtil.getDMFV_bm().cc;
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            if (((Elem)var3).getType().equals(this.formId)) {
               this.guiFormObject = (Elem)var3;
               break;
            }
         }
      }

   }

   public StoreItem getStoreItem() {
      return this.storeItem;
   }
}
