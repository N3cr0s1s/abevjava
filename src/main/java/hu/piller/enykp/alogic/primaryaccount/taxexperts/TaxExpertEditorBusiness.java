package hu.piller.enykp.alogic.primaryaccount.taxexperts;

import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.primaryaccount.common.IBusiness;
import hu.piller.enykp.alogic.primaryaccount.common.IRecord;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.util.icon.ENYKIconSet;
import java.awt.AWTKeyStroke;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class TaxExpertEditorBusiness {
   private TaxExpertEditorPanel editor_panel;
   private IRecord record;
   private IBusiness parent_business;
   private JButton btn_ok;
   private JButton btn_cancel;
   private JTextField txt_name;
   private JTextField txt_id;
   private JTextField txt_testimontal_number;
   public static final String NAME = "te_name";
   public static final String ID = "te_id";
   public static final String TESTIMONTAL_NUMBER = "te_testimontial_id";

   public TaxExpertEditorBusiness(TaxExpertEditorPanel var1) {
      this.editor_panel = var1;
      this.prepare();
   }

   private void prepare() {
      this.btn_ok = (JButton)this.editor_panel.getEEPComponent("ok");
      this.btn_cancel = (JButton)this.editor_panel.getEEPComponent("cancel");
      this.txt_name = (JTextField)this.editor_panel.getEEPComponent("name");
      this.txt_id = (JTextField)this.editor_panel.getEEPComponent("id");
      this.txt_testimontal_number = (JTextField)this.editor_panel.getEEPComponent("testimonial_number");
      this.prepareOk();
      this.prepareCancel();
      this.prepareIcons();
      this.prepareEnterLeave();
   }

   private void prepareEnterLeave() {
      Container[] var2 = new Container[]{this.txt_name, this.txt_id, this.txt_testimontal_number};
      int var3 = 0;

      for(int var4 = var2.length; var3 < var4; ++var3) {
         Container var1 = var2[var3];
         Set var5 = var1.getFocusTraversalKeys(0);
         HashSet var6 = new HashSet(var5);
         var6.add(AWTKeyStroke.getAWTKeyStroke(10, 0));
         var1.setFocusTraversalKeys(0, Collections.unmodifiableSet(var6));
      }

   }

   private void prepareIcons() {
      ENYKIconSet var1 = ENYKIconSet.getInstance();
      this.setButtonIcon(this.btn_ok, "anyk_ellenorzes", var1);
      this.setButtonIcon(this.btn_cancel, "anyk_megse", var1);
   }

   private void setButtonIcon(JButton var1, String var2, ENYKIconSet var3) {
      var1.setIcon(var3.get(var2));
   }

   private void prepareOk() {
      this.btn_ok.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (TaxExpertEditorBusiness.this.isContentValid()) {
               TaxExpertEditorBusiness.this.modifyRecord();
               TaxExpertEditorBusiness.this.record.save();
               TaxExpertBusiness var2 = (TaxExpertBusiness)TaxExpertEditorBusiness.this.parent_business;
               var2.mapRecord(TaxExpertEditorBusiness.this.record);
               var2.refreshView();
               var2.restorePanel();
               var2.firePrimaryAccountChanged();
            }

         }
      });
   }

   private boolean isContentValid() {
      String var1 = this.txt_name.getText();
      String var2 = this.txt_id.getText();
      String var3 = this.txt_testimontal_number.getText();
      var1 = var1.trim();
      var2 = var2.trim();
      var3 = var3.trim();
      if (var1.length() != 0 && var2.length() != 0 && var3.length() != 0) {
         if (var2.length() > 10 && !this.isValidTaxNumber()) {
            GuiUtil.showMessageDialog(this.editor_panel, "Az adószám hibás !", "Törzsadat ellenőrzés", 2);
            return false;
         } else if (var2.length() <= 10 && !this.isValidTaxId()) {
            GuiUtil.showMessageDialog(this.editor_panel, "Az adóazonosító jel hibás !", "Törzsadat ellenőrzés", 2);
            return false;
         } else {
            return true;
         }
      } else {
         GuiUtil.showMessageDialog(this.editor_panel, "Név, Azonosító és Bizonyítvány szám mezőket kötelező kitölteni !", "Törzsadat ellenőrzés", 2);
         return false;
      }
   }

   private boolean isValidTaxNumber() {
      Calculator var1 = Calculator.getInstance();
      String var2 = this.filterForNumbers(this.txt_id.getText());
      if (var2.length() > 0) {
         Object var3 = var1.calculateExpression("[jóadószám,\"" + var2 + "\"]");
         if (var3 == null) {
            return false;
         } else {
            return var3 instanceof Boolean ? (Boolean)var3 : true;
         }
      } else {
         return false;
      }
   }

   private boolean isValidTaxId() {
      Calculator var1 = Calculator.getInstance();
      String var2 = this.filterForNumbers(this.txt_id.getText());
      if (var2.length() > 0) {
         Object var3 = var1.calculateExpression("[jóadóazonosító,\"" + var2 + "\"]");
         if (var3 == null) {
            return false;
         } else {
            return var3 instanceof Boolean ? (Boolean)var3 : true;
         }
      } else {
         return false;
      }
   }

   private void prepareCancel() {
      this.installKeyBindingForButton(this.btn_cancel, 27);
      this.btn_cancel.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TaxExpertEditorBusiness.this.parent_business.restorePanel();
         }
      });
   }

   private void installKeyBindingForButton(final JButton var1, int var2) {
      String var3 = KeyStroke.getKeyStroke(var2, 0).toString() + "Pressed";
      TaxExpertEditorPanel var4 = this.editor_panel;
      var4.getInputMap(2).put(KeyStroke.getKeyStroke(var2, 0), var3);
      var4.getActionMap().put(var3, new AbstractAction() {
         public void actionPerformed(ActionEvent var1x) {
            if (var1.isVisible() && var1.isEnabled()) {
               var1.doClick();
            }

         }
      });
   }

   public void setRecord(IRecord var1, IBusiness var2) {
      this.record = var1;
      this.parent_business = var2;
      this.showRecord();
   }

   private void showRecord() {
      if (this.record != null) {
         Hashtable var1 = this.record.getData();
         this.txt_name.setText((String)var1.get("te_name"));
         this.txt_id.setText((String)var1.get("te_id"));
         this.txt_testimontal_number.setText((String)var1.get("te_testimontial_id"));
      }
   }

   private void modifyRecord() {
      if (this.record != null) {
         Hashtable var1 = this.record.getData();
         var1.put("te_name", this.txt_name.getText());
         var1.put("te_id", this.filterForNumbers(this.txt_id.getText()));
         var1.put("te_testimontial_id", this.txt_testimontal_number.getText());
      }
   }

   private String filterForNumbers(String var1) {
      String var2 = "";
      if (var1 != null) {
         char[] var3 = var1.toCharArray();
         int var4 = 0;

         for(int var5 = var3.length; var4 < var5; ++var4) {
            if ("0123456789".indexOf(var3[var4]) >= 0) {
               var2 = var2 + var3[var4];
            }
         }

         return var2;
      } else {
         return null;
      }
   }
}
