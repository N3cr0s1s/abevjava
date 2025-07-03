package hu.piller.enykp.alogic.primaryaccount.companies;

import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.primaryaccount.common.IBusiness;
import hu.piller.enykp.alogic.primaryaccount.common.IRecord;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.component.filtercombo.ENYKFilterComboPanel;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.icon.ENYKIconSet;
import java.awt.AWTKeyStroke;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

public class CompaniesEditorBusiness {
   private CompaniesEditorPanel c_editor_panel;
   private IRecord record;
   private IBusiness parent_business;
   private JButton btn_ok;
   private JButton btn_cancel;
   private JTextField txt_company_name;
   private JFormattedTextField txt_company_tax_num;
   private JTextField txt_s_settlement;
   private JTextField txt_s_public_place;
   private ENYKFilterComboPanel efc_s_public_place_type;
   private JTextField txt_s_house_number;
   private JTextField txt_s_building;
   private JTextField txt_s_staircase;
   private JTextField txt_s_level;
   private JTextField txt_s_door;
   private JFormattedTextField txt_s_zip;
   private JTextField txt_m_settlement;
   private JTextField txt_m_public_place;
   private ENYKFilterComboPanel efc_m_public_place_type;
   private JTextField txt_m_house_number;
   private JTextField txt_m_building;
   private JTextField txt_m_staircase;
   private JTextField txt_m_level;
   private JTextField txt_m_door;
   private JFormattedTextField txt_m_zip;
   private JTextField txt_admin;
   private JTextField txt_tel;
   private JTextField txt_f_corp;
   private JFormattedTextField txt_acc_num;
   public static final String NAME = "name";
   public static final String TAX_NUM = "tax_number";
   public static final String S_SETTLEMENT = "s_settlement";
   public static final String S_PUBLICPLACE = "s_public_place";
   public static final String S_PUBLICPLACETYPE = "s_public_place_type";
   public static final String S_HOUSENUMBER = "s_house_number";
   public static final String S_BUILDING = "s_building";
   public static final String S_STAIRCASE = "s_staircase";
   public static final String S_LEVEL = "s_level";
   public static final String S_DOOR = "s_door";
   public static final String S_ZIP = "s_zip";
   public static final String M_SETTLEMENT = "m_settlement";
   public static final String M_PUBLICPLACE = "m_public_place";
   public static final String M_PUBLICPLACETYPE = "m_public_place_type";
   public static final String M_HOUSENUMBER = "m_house_number";
   public static final String M_BUILDING = "m_building";
   public static final String M_STAIRCASE = "m_staircase";
   public static final String M_LEVEL = "m_level";
   public static final String M_DOOR = "m_door";
   public static final String M_ZIP = "m_zip";
   public static final String ADMIN = "administrator";
   public static final String TEL = "tel";
   public static final String F_CORP = "financial_corp";
   public static final String F_CORP_ID = "financial_corp_id";
   public static final String ACC_NUM = "account_id";

   public CompaniesEditorBusiness(CompaniesEditorPanel var1) {
      this.c_editor_panel = var1;
      this.prepare();
   }

   private void prepare() {
      this.btn_ok = (JButton)this.c_editor_panel.getEPComponent("ok");
      this.btn_cancel = (JButton)this.c_editor_panel.getEPComponent("cancel");
      this.txt_company_name = (JTextField)this.c_editor_panel.getEPComponent("company_name");
      this.txt_company_tax_num = (JFormattedTextField)this.c_editor_panel.getEPComponent("tax_number");
      this.txt_s_settlement = (JTextField)this.c_editor_panel.getEPComponent("s_settlement");
      this.txt_s_public_place = (JTextField)this.c_editor_panel.getEPComponent("s_public_place");
      this.efc_s_public_place_type = (ENYKFilterComboPanel)this.c_editor_panel.getEPComponent("s_public_place_type_2");
      this.txt_s_house_number = (JTextField)this.c_editor_panel.getEPComponent("s_house_number");
      this.txt_s_building = (JTextField)this.c_editor_panel.getEPComponent("s_building");
      this.txt_s_staircase = (JTextField)this.c_editor_panel.getEPComponent("s_staircase");
      this.txt_s_level = (JTextField)this.c_editor_panel.getEPComponent("s_level");
      this.txt_s_door = (JTextField)this.c_editor_panel.getEPComponent("s_door");
      this.txt_s_zip = (JFormattedTextField)this.c_editor_panel.getEPComponent("s_zip");
      this.txt_m_settlement = (JTextField)this.c_editor_panel.getEPComponent("m_settlement");
      this.txt_m_public_place = (JTextField)this.c_editor_panel.getEPComponent("m_public_place");
      this.efc_m_public_place_type = (ENYKFilterComboPanel)this.c_editor_panel.getEPComponent("m_public_place_type_2");
      this.txt_m_house_number = (JTextField)this.c_editor_panel.getEPComponent("m_house_number");
      this.txt_m_building = (JTextField)this.c_editor_panel.getEPComponent("m_building");
      this.txt_m_staircase = (JTextField)this.c_editor_panel.getEPComponent("m_staircase");
      this.txt_m_level = (JTextField)this.c_editor_panel.getEPComponent("m_level");
      this.txt_m_door = (JTextField)this.c_editor_panel.getEPComponent("m_door");
      this.txt_m_zip = (JFormattedTextField)this.c_editor_panel.getEPComponent("m_zip");
      this.txt_admin = (JTextField)this.c_editor_panel.getEPComponent("administrator");
      this.txt_tel = (JTextField)this.c_editor_panel.getEPComponent("tel");
      this.txt_f_corp = (JTextField)this.c_editor_panel.getEPComponent("financial_corp");
      this.txt_acc_num = (JFormattedTextField)this.c_editor_panel.getEPComponent("account_number");
      this.prepareOk();
      this.prepareCancel();
      this.prepareSPublicPlaceType();
      this.prepareMPublicPlaceType();
      this.prepareFormats();
      this.prepareIcons();
      this.prepareEnterLeave();
   }

   private void prepareEnterLeave() {
      Container[] var2 = new Container[]{this.txt_company_name, this.txt_company_tax_num, this.txt_s_settlement, this.txt_s_public_place, this.efc_s_public_place_type, this.txt_s_house_number, this.txt_s_building, this.txt_s_staircase, this.txt_s_level, this.txt_s_door, this.txt_s_zip, this.txt_m_settlement, this.txt_m_public_place, this.efc_m_public_place_type, this.txt_m_house_number, this.txt_m_building, this.txt_m_staircase, this.txt_m_level, this.txt_m_door, this.txt_m_zip, this.txt_admin, this.txt_tel, this.txt_f_corp, this.txt_acc_num};
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

   private void prepareFormats() {
      this.setFormatter(this.txt_company_tax_num, "########-#-##");
      this.setFormatter(this.txt_s_zip, "AAAAAAA");
      this.setFormatter(this.txt_m_zip, "AAAAAAA");
      this.setFormatter(this.txt_acc_num, "########-########-########");
      this.txt_company_tax_num.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent var1) {
            CompaniesEditorBusiness.this.txt_company_tax_num.getCaret().setDot(0);
         }

         public void focusLost(FocusEvent var1) {
         }
      });
      this.txt_s_zip.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent var1) {
            CompaniesEditorBusiness.this.txt_s_zip.getCaret().setDot(0);
         }

         public void focusLost(FocusEvent var1) {
         }
      });
      this.txt_m_zip.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent var1) {
            CompaniesEditorBusiness.this.txt_m_zip.getCaret().setDot(0);
         }

         public void focusLost(FocusEvent var1) {
         }
      });
      this.txt_acc_num.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent var1) {
            CompaniesEditorBusiness.this.txt_acc_num.getCaret().setDot(0);
         }

         public void focusLost(FocusEvent var1) {
         }
      });
   }

   private void prepareOk() {
      this.btn_ok.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (CompaniesEditorBusiness.this.isContentValid()) {
               CompaniesEditorBusiness.this.modifyRecord();
               CompaniesEditorBusiness.this.record.save();
               CompaniesBusiness var2 = (CompaniesBusiness)CompaniesEditorBusiness.this.parent_business;
               var2.mapRecord(CompaniesEditorBusiness.this.record);
               var2.refreshView();
               var2.restorePanel();
               var2.firePrimaryAccountChanged();
            }

         }
      });
   }

   private boolean isContentValid() {
      String var1 = this.txt_company_name.getText();
      String var2 = this.filterForNumbers(this.txt_company_tax_num.getText());
      var1 = var1.trim();
      var2 = var2.trim();
      if (var1.length() != 0 && var2.length() != 0) {
         if (!this.isValidTaxNumber()) {
            GuiUtil.showMessageDialog(this.c_editor_panel, "Az adószám hibás !", "Törzsadat ellenőrzés", 2);
            return false;
         } else {
            IRecord[] var3;
            if ((var3 = this.record.filterOnAll("tax_number", this.filterForNumbers(this.txt_company_tax_num.getText()))).length > 0) {
               IRecord var4 = this.getFirstDifferent(var3, this.record);
               if (var4 != null) {
                  GuiUtil.showMessageDialog(this.c_editor_panel, "Adószám már használva van itt: '" + var4 + "' !", "Törzsadat ellenőrzés", 2);
                  return false;
               }
            }

            return true;
         }
      } else {
         GuiUtil.showMessageDialog(this.c_editor_panel, "Társaság neve és Adószám mezőket kötelező kitölteni !", "Törzsadat ellenőrzés", 2);
         return false;
      }
   }

   private boolean isValidTaxNumber() {
      Calculator var1 = Calculator.getInstance();
      String var2 = this.txt_company_tax_num.getText();
      String var3 = this.filterForNumbers(var2);
      if (var2.trim().length() > 0 && var3.trim().length() == 0) {
         return false;
      } else {
         Object var4 = var1.calculateExpression("[jóadószám,\"" + this.filterForNumbers(var2) + "\"]");
         if (var4 == null) {
            return false;
         } else {
            return var4 instanceof Boolean ? (Boolean)var4 : true;
         }
      }
   }

   private IRecord getFirstDifferent(IRecord[] var1, IRecord var2) {
      int var3 = 0;

      for(int var4 = var1.length; var3 < var4; ++var3) {
         if (var1[var3] != var2) {
            return var1[var3];
         }
      }

      return null;
   }

   private void prepareCancel() {
      this.installKeyBindingForButton(this.btn_cancel, 27);
      this.btn_cancel.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            CompaniesEditorBusiness.this.parent_business.restorePanel();
         }
      });
   }

   private void installKeyBindingForButton(final JButton var1, int var2) {
      String var3 = KeyStroke.getKeyStroke(var2, 0).toString() + "Pressed";
      CompaniesEditorPanel var4 = this.c_editor_panel;
      var4.getInputMap(2).put(KeyStroke.getKeyStroke(var2, 0), var3);
      var4.getActionMap().put(var3, new AbstractAction() {
         public void actionPerformed(ActionEvent var1x) {
            if (var1.isVisible() && var1.isEnabled()) {
               var1.doClick();
            }

         }
      });
   }

   private void prepareSPublicPlaceType() {
      this.fillUpPPT(this.efc_s_public_place_type);
   }

   private void prepareMPublicPlaceType() {
      this.fillUpPPT(this.efc_m_public_place_type);
   }

   private void setFormatter(JFormattedTextField var1, String var2) {
      this.setFormatter(var1, var2, (String)null);
   }

   private void setFormatter(JFormattedTextField var1, String var2, String var3) {
      try {
         MaskFormatter var4 = new MaskFormatter(var2);
         var4.setPlaceholderCharacter('_');
         var4.setAllowsInvalid(false);
         if (var3 != null) {
            var4.setValidCharacters(var3 + var4.getPlaceholderCharacter());
         }

         var1.setFormatterFactory(new DefaultFormatterFactory(var4));
         var4.install(var1);
         var1.setFocusLostBehavior(0);
      } catch (ParseException var6) {
         Tools.eLog(var6, 0);
      }

   }

   private void fillUpPPT(ENYKFilterComboPanel var1) {
      var1.addElements(new String[]{"utca", "út", "tér", "útja", "körtér", "körút", "köz", "postafiók", "akna", "akna-alsó", "akna-felső", "alagút", "alsórakpart", "arborétum", "autóút", "állat és növ.kert", "állomás", "árnyék", "árok", "átjáró", "barakképület", "barlang", "bánya", "bányatelep", "bástya", "bástyája", "bejáró", "bekötőút", "csárda", "csónakházak", "domb", "dűlő", "dűlősor", "dűlőterület", "dűlőút", "egyetemváros", "egyéb", "elágazás", "emlékút", "erdészház", "erdészlak", "erdő", "erdősor", "fasor", "fasora", "felső", "forduló", "föld", "főmérnökség", "főtér", "főút", "gát", "gátőrház", "gátsor", "gyár", "gyártelep", "gyárváros", "gyümölcsös", "határsor", "határút", "ház", "hegy", "hegyhát", "hegyhát dűlő", "hegyhát köz", "hídfő", "iskola", "jav.alatt", "játszótér", "kapu", "kastély", "kert", "kertsor", "kilátó", "kioszk", "kocsiszín", "kolónia", "korzó", "kör", "körönd", "körvasútsor", "körzet", "kultúrpark", "kunyhó", "kút", "kültelek", "lakóház", "lakónegyed", "lakótelep", "lejáró", "lejtő", "lépcső", "liget", "major", "malom", "menedékház", "mélyút", "munkásszálló", "műút", "oldal", "orom", "őrház", "őrházak", "őrházlak", "park", "parkja", "part", "pálya", "pályaudvar", "piac", "pihenő", "pince", "pincesor", "puszta", "rakpart", "repülőtér", "rész", "rét", "sarok", "sánc", "sávház", "sétány", "sor", "sora", "sportpálya", "sporttelep", "stadion", "strandfürdő", "sugárút", "szállás", "szállások", "szer", "szél", "sziget", "szivattyútelep", "szőlő", "szőlőhegy", "szőlők", "tag", "tanya", "tanyák", "telep", "temető", "tere", "tető", "téli kikötő", "tömb", "turistaház", "udvar", "utak", "utcája", "útőrház", "üdülő", "üdülő-part üdülő-sor", "üdülő-telep", "vadaskert", "vadászház", "vasútállomás", "vasútimegálló", "vasútiőrház", "vasútsor", "vágóhíd", "vár", "várköz", "város", "vezetőút", "villasor", "vízmű", "völgy", "zsilip", "zug"}, true);
   }

   public void setRecord(IRecord var1, IBusiness var2) {
      this.record = var1;
      this.parent_business = var2;
      this.showRecord();
   }

   private void showRecord() {
      if (this.record != null) {
         Hashtable var1 = this.record.getData();
         this.txt_company_tax_num.setText("");
         this.txt_s_zip.setText("");
         this.txt_m_zip.setText("");
         this.txt_acc_num.setText("");
         this.txt_company_name.setText((String)var1.get("name"));
         this.txt_company_tax_num.setText((String)var1.get("tax_number"));
         this.txt_s_settlement.setText((String)var1.get("s_settlement"));
         this.txt_s_public_place.setText((String)var1.get("s_public_place"));
         this.efc_s_public_place_type.setText((String)var1.get("s_public_place_type"));
         this.txt_s_house_number.setText((String)var1.get("s_house_number"));
         this.txt_s_building.setText((String)var1.get("s_building"));
         this.txt_s_staircase.setText((String)var1.get("s_staircase"));
         this.txt_s_level.setText((String)var1.get("s_level"));
         this.txt_s_door.setText((String)var1.get("s_door"));
         this.txt_s_zip.setText((String)var1.get("s_zip"));
         this.txt_m_settlement.setText((String)var1.get("m_settlement"));
         this.txt_m_public_place.setText((String)var1.get("m_public_place"));
         this.efc_m_public_place_type.setText((String)var1.get("m_public_place_type"));
         this.txt_m_house_number.setText((String)var1.get("m_house_number"));
         this.txt_m_building.setText((String)var1.get("m_building"));
         this.txt_m_staircase.setText((String)var1.get("m_staircase"));
         this.txt_m_level.setText((String)var1.get("m_level"));
         this.txt_m_door.setText((String)var1.get("m_door"));
         this.txt_m_zip.setText((String)var1.get("m_zip"));
         this.txt_admin.setText((String)var1.get("administrator"));
         this.txt_tel.setText((String)var1.get("tel"));
         this.txt_f_corp.setText((String)var1.get("financial_corp"));
         String var2 = (String)var1.get("financial_corp_id");
         var2 = var2 == null ? "" : var2;
         String var3 = (String)var1.get("account_id");
         var3 = var3 == null ? "" : var3;
         this.txt_acc_num.setText(var2 + var3);
      }
   }

   private void modifyRecord() {
      if (this.record != null) {
         Hashtable var1 = this.record.getData();
         var1.put("name", this.txt_company_name.getText());
         var1.put("tax_number", this.filterForNumbers(this.txt_company_tax_num.getText()));
         var1.put("s_settlement", this.txt_s_settlement.getText());
         var1.put("s_public_place", this.txt_s_public_place.getText());
         var1.put("s_public_place_type", this.getString(this.efc_s_public_place_type.getText()));
         var1.put("s_house_number", this.txt_s_house_number.getText());
         var1.put("s_building", this.txt_s_building.getText());
         var1.put("s_staircase", this.txt_s_staircase.getText());
         var1.put("s_level", this.txt_s_level.getText());
         var1.put("s_door", this.txt_s_door.getText());
         var1.put("s_zip", this.filterForValue(this.txt_s_zip.getText(), ((MaskFormatter)this.txt_s_zip.getFormatter()).getPlaceholderCharacter()));
         var1.put("m_settlement", this.txt_m_settlement.getText());
         var1.put("m_public_place", this.txt_m_public_place.getText());
         var1.put("m_public_place_type", this.getString(this.efc_m_public_place_type.getText()));
         var1.put("m_house_number", this.txt_m_house_number.getText());
         var1.put("m_building", this.txt_m_building.getText());
         var1.put("m_staircase", this.txt_m_staircase.getText());
         var1.put("m_level", this.txt_m_level.getText());
         var1.put("m_door", this.txt_m_door.getText());
         var1.put("m_zip", this.filterForValue(this.txt_m_zip.getText(), ((MaskFormatter)this.txt_m_zip.getFormatter()).getPlaceholderCharacter()));
         var1.put("administrator", this.txt_admin.getText());
         var1.put("tel", this.txt_tel.getText());
         var1.put("financial_corp", this.txt_f_corp.getText());

         try {
            var1.put("financial_corp_id", this.filterForNumbers(this.txt_acc_num.getText()).substring(0, 8));
         } catch (Exception var4) {
            Tools.eLog(var4, 0);
         }

         try {
            var1.put("account_id", this.filterForNumbers(this.txt_acc_num.getText()).substring(8));
         } catch (Exception var3) {
            Tools.eLog(var3, 0);
         }

      }
   }

   private String getString(Object var1) {
      return var1 == null ? "" : var1.toString();
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

   private String filterForValue(String var1, char var2) {
      String var3;
      if (var1 != null) {
         int var6 = var1.length();

         int var4;
         for(var4 = 0; var4 < var6 && var1.charAt(var4) == var2; ++var4) {
         }

         int var5;
         for(var5 = var6 - 1; var5 > -1 && var1.charAt(var5) == var2; --var5) {
         }

         ++var5;
         if (var4 < var5) {
            var3 = var1.substring(var4, var5);
         } else {
            var3 = "";
         }
      } else {
         var3 = var1;
      }

      return var3;
   }
}
