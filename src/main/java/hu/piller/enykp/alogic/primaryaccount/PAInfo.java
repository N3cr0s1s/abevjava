package hu.piller.enykp.alogic.primaryaccount;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.metainfo.MetaStore;
import hu.piller.enykp.alogic.primaryaccount.common.ABEVEnvelope;
import hu.piller.enykp.alogic.primaryaccount.common.DefaultEnvelope;
import hu.piller.enykp.alogic.primaryaccount.common.IRecord;
import hu.piller.enykp.alogic.primaryaccount.companies.CompaniesBusiness;
import hu.piller.enykp.alogic.primaryaccount.companies.CompanyRecordFactory;
import hu.piller.enykp.alogic.primaryaccount.people.PeopleBusiness;
import hu.piller.enykp.alogic.primaryaccount.people.PeopleRecordFactory;
import hu.piller.enykp.alogic.primaryaccount.smallbusiness.SmallBusinessBusiness;
import hu.piller.enykp.alogic.primaryaccount.smallbusiness.SmallBusinessRecordFactory;
import hu.piller.enykp.alogic.primaryaccount.taxexperts.TaxExpertBusiness;
import hu.piller.enykp.alogic.primaryaccount.taxexperts.TaxExpertRecordFactory;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IDataStore;
import java.awt.Component;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class PAInfo {
   public static final String PA_ID_NAME = "Adózó neve";
   public static final String PA_ID_FNAME_TITLE = "Név titulus";
   public static final String PA_ID_SEX = "Adózó neme";
   public static final String PA_ID_FNAME = "Vezetékneve";
   public static final String PA_ID_LNAME = "Keresztneve";
   public static final String PA_ID_TAXNUMBER = "Adózó adószáma";
   public static final String PA_ID_TAXID = "Adózó adóazonosító jele";
   public static final String PA_ID_EU_TAXNUMBER = "Közösségi adószám";
   public static final String PA_ID_TECHNICAL_ID = "Technikai azonosítás";
   public static final String PA_ID_VPID = "VPID";
   public static final String PA_ID_TERMFROM = "Bevallási időszak kezdete";
   public static final String PA_ID_TERMTO = "Bevallási időszak vége";
   public static final String PA_ID_SIGN = "Adózó aláírása";
   public static final String PA_ID_LINECODE = "Vonalkód";
   public static final String PA_ID_STATE = "Státusz";
   public static final String PA_ID_APPLY = "Vonatkozik";
   public static final String PA_ID_TAXEXPNAME = "Adótanácsadó neve";
   public static final String PA_ID_TAXEXPID = "Adótanácsadó azonosítószáma";
   public static final String PA_ID_TAXEXPTESTIMONTIALID = "Adótanácsadó Bizonyítvány";
   public static final String PA_ID_ADMINNAME = "Ügyintéző neve";
   public static final String PA_ID_ADMINTEL = "Ügyintéző telefonszáma";
   public static final String PA_ID_FCORPID = "Pénzintézet-azonosító";
   public static final String PA_ID_FCORPNAME = "Pénzintézet neve";
   public static final String PA_ID_ACCOUNTID = "Számla-azonosító";
   public static final String PA_ID_S_ZIP = "Irányítószám";
   public static final String PA_ID_S_SETTLEMENT = "Település";
   public static final String PA_ID_S_PUBLICPLACE = "Közterület neve";
   public static final String PA_ID_S_HOUSENUMBER = "Házszám";
   public static final String PA_ID_S_LEVEL = "Emelet";
   public static final String PA_ID_S_PUBLICPLACETYPE = "Közterület jellege";
   public static final String PA_ID_S_BUILDING = "Épület";
   public static final String PA_ID_S_STAIRCASE = "Lépcsőház";
   public static final String PA_ID_S_DOOR = "Ajtó";
   public static final String PA_ID_M_ZIP = "L Irányítószám";
   public static final String PA_ID_M_SETTLEMENT = "L Település";
   public static final String PA_ID_M_PUBLICPLACE = "L Közterület neve";
   public static final String PA_ID_M_HOUSENUMBER = "L Házszám";
   public static final String PA_ID_M_LEVEL = "L Emelet";
   public static final String PA_ID_M_PUBLICPLACETYPE = "L Közterület jellege";
   public static final String PA_ID_M_BUILDING = "L Épület";
   public static final String PA_ID_M_STAIRCASE = "L Lépcsőház";
   public static final String PA_ID_M_DOOR = "L Ajtó";
   private static final Hashtable primacc_to_xmlid = new Hashtable(37);
   private static PAInfo instance;
   private long last_mod_time = 0L;
   private PrimaryAccountDialog pa_dialog;

   public static PAInfo getInstance() {
      if (instance == null) {
         instance = new PAInfo();
      }

      return instance;
   }

   private PAInfo() {
   }

   public long getLastModTime() {
      return this.last_mod_time;
   }

   public void setLastModTime(long var1) {
      this.last_mod_time = var1;
   }

   public void openDialog(JFrame var1) {
      if (this.pa_dialog == null) {
         this.pa_dialog = PrimaryAccountDialog.getInstance(var1, this);
         this.pa_dialog.setTitle("Törzsadatok ...");
         this.pa_dialog.setSize(640, 480);
         this.pa_dialog.setModal(true);
      }

      this.pa_dialog.setLocationRelativeTo(var1);
      this.pa_dialog.setVisible(true);
   }

   public void closeDialog() {
      if (this.pa_dialog != null) {
         this.pa_dialog.setVisible(false);
      }

   }

   public void releaseDialog() {
      if (this.pa_dialog != null) {
         this.pa_dialog.dispose();
         this.pa_dialog = null;
      }

   }

   public void reloadDialog() {
      if (this.get_primary_accounts_panel() != null) {
         this.get_primary_accounts_panel().getBusiness().reload(15);
      }

   }

   public JDialog getDialog() {
      return this.pa_dialog;
   }

   public boolean apply_primary_account(Object var1, Object var2) {
      System.out.println("CALL APPLY_PRIMARY_ACCOUNT");
      this.convertToVezAndKerName(var1, var2);
      this.convertAdozoNeme(var1);
      boolean var3 = false;
      boolean var4 = false;
      TemplateUtils var5 = TemplateUtils.getInstance();
      if (var1 instanceof IRecord) {
         BookModel var6 = Calculator.getInstance().getBookModel();
         if (var6 != null) {
            String[] var7 = new String[2];
            Hashtable var9 = ((IRecord)var1).getData();
            Iterator var10 = primacc_to_xmlid.entrySet().iterator();
            MetaStore var15 = MetaInfo.getInstance().getMetaStore(var2);
            if (var15 != null) {
               Vector var16 = var15.getFilteredFieldMetas_And(new Vector(Arrays.asList("panids")));

               while(var10.hasNext()) {
                  Entry var11 = (Entry)var10.next();
                  Object var12 = var11.getKey();
                  int var17 = 0;

                  for(int var18 = var16.size(); var17 < var18; ++var17) {
                     Hashtable var8 = (Hashtable)var16.get(var17);
                     String var13 = (String)var8.get("panids");
                     String[] var14 = var13.split(",");
                     int var19 = 0;

                     int var20;
                     for(var20 = var14.length; var19 < var20; ++var19) {
                        var14[var19] = var14[var19].trim();
                     }

                     var19 = 0;

                     for(var20 = var14.length; var19 < var20; ++var19) {
                        if (var12.toString().equalsIgnoreCase(var14[var19])) {
                           var7[0] = this.getString(var8.get("fid"));
                           var7[1] = this.getString(var9.get(var11.getValue()));
                           if (var7[1].length() > 0) {
                              System.out.println(">panid:" + var14[var19] + ", fid:" + var7[0] + ", value:" + var7[1]);
                              if (var5.checkByDataCheckerMD(var6, (String)var2, var7[0], var7[1])) {
                                 var6.set_field_value(var7[0], var7[1]);
                                 CalculatorManager.getInstance().do_field_calculation(var2.toString(), var7[0]);
                              } else {
                                 var4 = true;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      if (var4) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "Törzsadat betöltési hiba, a részletekért kérem nézze meg az Szerviz menüpont Üzenetek almenüjét!", "Figyelmeztetés", 1);
      }

      return var3;
   }

   public Object get_primary_account_list(Object var1) {
      if (var1 instanceof String) {
         String var2 = (String)var1;

         try {
            if (var2.equalsIgnoreCase("company")) {
               return (new CompanyRecordFactory()).loadRecords(CompaniesBusiness.getPrimaryAccountPath(), (DefaultEnvelope)null);
            }

            if (var2.equalsIgnoreCase("smallbusiness")) {
               return (new SmallBusinessRecordFactory()).loadRecords(SmallBusinessBusiness.getPrimaryAccountPath(), (DefaultEnvelope)null);
            }

            if (var2.equalsIgnoreCase("people")) {
               return (new PeopleRecordFactory()).loadRecords(PeopleBusiness.getPrimaryAccountPath(), (DefaultEnvelope)null);
            }

            if (var2.equalsIgnoreCase("taxexpert")) {
               return (new TaxExpertRecordFactory()).loadRecords(TaxExpertBusiness.getPrimaryAccountPath(), (DefaultEnvelope)null);
            }
         } catch (Exception var4) {
            hu.piller.enykp.util.base.Tools.eLog(var4, 0);
         }
      }

      return null;
   }

   public PrimaryAccountsPanel get_primary_accounts_panel() {
      return PrimaryAccountDialog.getPrimaryAccountsPanel();
   }

   public Object get_pa_data(Object var1, Object var2) {
      Hashtable var3;
      if (var1 instanceof Object[]) {
         Object[] var4 = (Object[])((Object[])var1);
         Hashtable var5 = new Hashtable(var4.length);
         BookModel var6 = Calculator.getInstance().getBookModel();
         if (var6 != null) {
            MetaStore var8 = MetaInfo.getInstance().getMetaStore(var2);
            if ("".equals(var2)) {
               var2 = null;
            }

            if (var8 != null) {
               Vector var9 = var8.getFilteredFieldMetas_And(new Vector(Arrays.asList("panids")));
               Vector var10 = new Vector(var9.size());
               Vector var11 = new Vector(var9.size());
               int var12 = 0;
               int var13 = var9.size();

               label60:
               while(true) {
                  Hashtable var7;
                  if (var12 >= var13) {
                     IDataStore var21 = var6.get_datastore();
                     if (var21 == null) {
                        break;
                     }

                     int var17 = 0;
                     int var18 = var10.size();

                     while(true) {
                        if (var17 >= var18) {
                           break label60;
                        }

                        var7 = (Hashtable)var10.get(var17);
                        var11.clear();
                        var11.addAll(Arrays.asList(this.getString(var7.get("panids")).split(",")));
                        Object var22 = var7.get("fid");
                        String var24 = this.getString(var21.get("0_" + var22));
                        int var19 = 0;

                        for(int var20 = var11.size(); var19 < var20; ++var19) {
                           Object var23 = var11.get(var17);
                           Vector var16 = (Vector)var5.get(var23);
                           if (var16 == null) {
                              var16 = new Vector(8);
                              var5.put(var23, var16);
                           }

                           var16.add(var24);
                        }

                        ++var17;
                     }
                  }

                  var7 = (Hashtable)var9.get(var12);
                  var11.clear();
                  var11.addAll(Arrays.asList(this.getString(var7.get("panids")).split(",")));
                  int var14 = 0;

                  for(int var15 = var4.length; var14 < var15; ++var14) {
                     if (var11.contains(var4[var14])) {
                        var10.add(var7);
                        break;
                     }
                  }

                  ++var12;
               }
            }
         }

         var3 = var5;
      } else {
         var3 = new Hashtable();
      }

      return var3;
   }

   public IRecord get_pa_record() {
      return this.get_pa_record(Calculator.getInstance().getBookModel().get_main_formmodel().id, Calculator.getInstance().getBookModel().get_main_document());
   }

   public IRecord get_pa_record(final Object var1, final IDataStore var2) {
      if (var1 != null && var2 != null) {
         IRecord var3 = new IRecord() {
            private final Hashtable data = new Hashtable(28);
            private final DefaultEnvelope envelope = new ABEVEnvelope() {
               protected Hashtable getData(int var1x) {
                  Hashtable var2x = this.envelope_data;
                  this.initializeData(var2x);
                  if (this.record != null) {
                     Hashtable var3 = this.record.getData();
                     String var4 = this.getValue(var3, PAInfo.primacc_to_xmlid.get("Adózó neve")).trim();
                     String var5 = this.getValue(var3, PAInfo.primacc_to_xmlid.get("Név titulus")).trim() + " " + this.getValue(var3, PAInfo.primacc_to_xmlid.get("Vezetékneve")).trim() + " " + this.getValue(var3, PAInfo.primacc_to_xmlid.get("Keresztneve")).trim();
                     String var6 = "";
                     if (var4 != null && var5 != null) {
                        if (var4.length() == 0) {
                           var6 = var5.trim();
                        } else {
                           var6 = var4;
                        }
                     }

                     var2x.put("f_feladó", var6);
                     var2x.put("f_utca", this.getStreet(var3, var1x));
                     if (var1x == 0) {
                        var2x.put("f_város", this.getValue(var3, PAInfo.primacc_to_xmlid.get("Település")));
                        var2x.put("f_irsz", this.getValue(var3, PAInfo.primacc_to_xmlid.get("Irányítószám")));
                     } else if (var1x == 1) {
                        var2x.put("f_város", this.getValue(var3, PAInfo.primacc_to_xmlid.get("L Település")));
                        var2x.put("f_irsz", this.getValue(var3, PAInfo.primacc_to_xmlid.get("L Irányítószám")));
                     }
                  }

                  this.getReceiverData(var2x);
                  return var2x;
               }

               private String getStreet(Hashtable var1x, int var2x) {
                  String var3 = "";
                  if (var2x == 0) {
                     var3 = this.getDataTag(var1x, PAInfo.primacc_to_xmlid.get("Közterület neve"), var3, (String)null);
                     var3 = this.getDataTag(var1x, PAInfo.primacc_to_xmlid.get("Közterület jellege"), var3, (String)null);
                     var3 = this.getDataTag(var1x, PAInfo.primacc_to_xmlid.get("Házszám"), var3, ".");
                     var3 = this.getDataTag(var1x, PAInfo.primacc_to_xmlid.get("Épület"), var3, "ép.");
                     var3 = this.getDataTag(var1x, PAInfo.primacc_to_xmlid.get("Lépcsőház"), var3, "lh.");
                     var3 = this.getDataTag(var1x, PAInfo.primacc_to_xmlid.get("Emelet"), var3, "em.");
                     var3 = this.getDataTag(var1x, PAInfo.primacc_to_xmlid.get("Ajtó"), var3, (String)null);
                  } else if (var2x == 1) {
                     var3 = this.getDataTag(var1x, PAInfo.primacc_to_xmlid.get("L Közterület neve"), var3, (String)null);
                     var3 = this.getDataTag(var1x, PAInfo.primacc_to_xmlid.get("L Közterület jellege"), var3, (String)null);
                     var3 = this.getDataTag(var1x, PAInfo.primacc_to_xmlid.get("L Házszám"), var3, ".");
                     var3 = this.getDataTag(var1x, PAInfo.primacc_to_xmlid.get("L Épület"), var3, "ép.");
                     var3 = this.getDataTag(var1x, PAInfo.primacc_to_xmlid.get("L Lépcsőház"), var3, "lh.");
                     var3 = this.getDataTag(var1x, PAInfo.primacc_to_xmlid.get("L Emelet"), var3, "em.");
                     var3 = this.getDataTag(var1x, PAInfo.primacc_to_xmlid.get("L Ajtó"), var3, (String)null);
                  }

                  return var3;
               }
            };

            public void delete() {
            }

            public String getName() {
               return "(Ál adat rekord)";
            }

            public String getDescription(String var1x) {
               return "(Ál adat rekord)";
            }

            public Hashtable getData() {
               return this.data;
            }

            public void setData(Object var1x, Object var2x) {
               this.data.put(var1x, var2x);
            }

            public void reload() {
               BookModel var1x = Calculator.getInstance().getBookModel();
               Hashtable var2x = new Hashtable(32);
               var2x.put("Adózó neve", "");
               var2x.put("Név titulus", "");
               var2x.put("Vezetékneve", "");
               var2x.put("Keresztneve", "");
               var2x.put("Adózó neme", "");
               var2x.put("Adózó adóazonosító jele", "");
               var2x.put("Adózó adószáma", "");
               var2x.put("Közösségi adószám", "");
               var2x.put("Irányítószám", "");
               var2x.put("Település", "");
               var2x.put("Közterület neve", "");
               var2x.put("Házszám", "");
               var2x.put("Emelet", "");
               var2x.put("Közterület jellege", "");
               var2x.put("Épület", "");
               var2x.put("Lépcsőház", "");
               var2x.put("Ajtó", "");
               var2x.put("L Irányítószám", "");
               var2x.put("L Település", "");
               var2x.put("L Közterület neve", "");
               var2x.put("L Házszám", "");
               var2x.put("L Emelet", "");
               var2x.put("L Közterület jellege", "");
               var2x.put("L Épület", "");
               var2x.put("L Lépcsőház", "");
               var2x.put("L Ajtó", "");
               var2x.put("Bevallási időszak kezdete", "");
               var2x.put("Bevallási időszak vége", "");
               if (var2 != null && var1x != null) {
                  if (var1 == null) {
                     PAInfo.this.getPrimaryAccountData(var2x, var1x.get_formid(), var2);
                  } else {
                     PAInfo.this.getPrimaryAccountData(var2x, var1, var2);
                  }
               }

               this.data.put(PAInfo.primacc_to_xmlid.get("Adózó neve"), var2x.get("Adózó neve"));
               this.data.put(PAInfo.primacc_to_xmlid.get("Név titulus"), var2x.get("Név titulus"));
               this.data.put(PAInfo.primacc_to_xmlid.get("Vezetékneve"), var2x.get("Vezetékneve"));
               this.data.put(PAInfo.primacc_to_xmlid.get("Adózó neme"), var2x.get("Adózó neme"));
               this.data.put(PAInfo.primacc_to_xmlid.get("Keresztneve"), var2x.get("Keresztneve"));
               this.data.put(PAInfo.primacc_to_xmlid.get("Adózó adóazonosító jele"), var2x.get("Adózó adóazonosító jele"));
               this.data.put(PAInfo.primacc_to_xmlid.get("Adózó adószáma"), var2x.get("Adózó adószáma"));
               this.data.put(PAInfo.primacc_to_xmlid.get("Közösségi adószám"), var2x.get("Közösségi adószám"));
               this.data.put(PAInfo.primacc_to_xmlid.get("Irányítószám"), var2x.get("Irányítószám"));
               this.data.put(PAInfo.primacc_to_xmlid.get("Település"), var2x.get("Település"));
               this.data.put(PAInfo.primacc_to_xmlid.get("Közterület neve"), var2x.get("Közterület neve"));
               this.data.put(PAInfo.primacc_to_xmlid.get("Házszám"), var2x.get("Házszám"));
               this.data.put(PAInfo.primacc_to_xmlid.get("Emelet"), var2x.get("Emelet"));
               this.data.put(PAInfo.primacc_to_xmlid.get("Közterület jellege"), var2x.get("Közterület jellege"));
               this.data.put(PAInfo.primacc_to_xmlid.get("Épület"), var2x.get("Épület"));
               this.data.put(PAInfo.primacc_to_xmlid.get("Lépcsőház"), var2x.get("Lépcsőház"));
               this.data.put(PAInfo.primacc_to_xmlid.get("Ajtó"), var2x.get("Ajtó"));
               this.data.put(PAInfo.primacc_to_xmlid.get("L Irányítószám"), var2x.get("L Irányítószám"));
               this.data.put(PAInfo.primacc_to_xmlid.get("L Település"), var2x.get("L Település"));
               this.data.put(PAInfo.primacc_to_xmlid.get("L Közterület neve"), var2x.get("L Közterület neve"));
               this.data.put(PAInfo.primacc_to_xmlid.get("L Házszám"), var2x.get("L Házszám"));
               this.data.put(PAInfo.primacc_to_xmlid.get("L Emelet"), var2x.get("L Emelet"));
               this.data.put(PAInfo.primacc_to_xmlid.get("L Közterület jellege"), var2x.get("L Közterület jellege"));
               this.data.put(PAInfo.primacc_to_xmlid.get("L Épület"), var2x.get("L Épület"));
               this.data.put(PAInfo.primacc_to_xmlid.get("L Lépcsőház"), var2x.get("L Lépcsőház"));
               this.data.put(PAInfo.primacc_to_xmlid.get("L Ajtó"), var2x.get("L Ajtó"));
               this.data.put(PAInfo.primacc_to_xmlid.get("Bevallási időszak kezdete"), var2x.get("Bevallási időszak kezdete"));
               this.data.put(PAInfo.primacc_to_xmlid.get("Bevallási időszak vége"), var2x.get("Bevallási időszak vége"));
            }

            public void save() {
            }

            public void printEnvelope(Component var1x) {
               if (this.envelope != null) {
                  this.envelope.print(this, var1x);
               }

            }

            public DefaultEnvelope getEnvelope() {
               return this.envelope;
            }

            public IRecord[] filter(String var1x, String var2x) {
               return new IRecord[0];
            }

            public IRecord[] filterOnAll(String var1x, String var2x) {
               return new IRecord[0];
            }
         };
         var3.reload();
         return var3;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public Object get_pa_record_item_description(Object var1, Object var2) {
      return var1 != null && var2 instanceof IRecord ? ((IRecord)var2).getDescription(var1.toString()) : null;
   }

   public Object search_primary_account(Object var1, Object var2) {
      int var3;
      if (var1 instanceof Number) {
         var3 = ((Number)var1).intValue();
      } else {
         var3 = 15;
      }

      return this.get_primary_accounts_panel() != null ? this.get_primary_accounts_panel().getBusiness().searchElement(var3, var2) : null;
   }

   private void getPrimaryAccountData(Hashtable var1, Object var2, IDataStore var3) {
      if (var1 != null) {
         BookModel var4 = Calculator.getInstance().getBookModel();
         if (var4 != null) {
            MetaStore var6 = MetaInfo.getInstance().getMetaStore(var2);
            if (var6 != null) {
               Vector var7 = var6.getFilteredFieldMetas_And(new Vector(Arrays.asList("panids")));
               Vector var8 = new Vector(var7.size());
               Vector var9 = new Vector(var7.size());
               int var11 = 0;

               Hashtable var5;
               Enumeration var10;
               for(int var12 = var7.size(); var11 < var12; ++var11) {
                  var5 = (Hashtable)var7.get(var11);
                  var9.clear();
                  var9.addAll(Arrays.asList(this.getString(var5.get("panids")).split(",")));
                  var10 = var1.keys();

                  while(var10.hasMoreElements()) {
                     if (var9.contains(var10.nextElement())) {
                        var8.add(var5);
                        break;
                     }
                  }
               }

               if (var3 == null) {
                  var3 = var4.get_datastore();
               }

               if (var3 != null) {
                  int var13 = 0;

                  for(int var14 = var8.size(); var13 < var14; ++var13) {
                     var5 = (Hashtable)var8.get(var13);
                     var9.clear();
                     var9.addAll(Arrays.asList(this.getString(var5.get("panids")).split(",")));
                     Object var16 = var5.get("fid");
                     var10 = var1.keys();

                     while(var10.hasMoreElements()) {
                        Object var15 = var10.nextElement();
                        if (var9.contains(var15)) {
                           var1.put(var15, this.getString(var3.get("0_" + var16)));
                        }
                     }
                  }
               }
            }
         }
      }

      System.out.println();
   }

   private void convertToVezAndKerName(Object var1, Object var2) {
      MetaStore var3 = MetaInfo.getInstance().getMetaStore(var2);
      Vector var4 = var3.getFilteredFieldMetas_And(new Vector(Arrays.asList("panids")));
      boolean var5 = false;

      for(int var6 = 0; var6 < var4.size(); ++var6) {
         Hashtable var7 = (Hashtable)var4.get(var6);
         String var8 = this.getString(var7.get("panids"));
         if (var8.indexOf("Vezetékneve") > -1) {
            var5 = true;
         }
      }

      if (var5 && var1 instanceof IRecord) {
         Hashtable var9 = ((IRecord)var1).getData();
         if ((this.getString(var9.get("first_name")) + this.getString(var9.get("last_name"))).length() > 0) {
            var9.put("name", "");
         }
      }

   }

   private void convertAdozoNeme(Object var1) {
      if (var1 instanceof IRecord) {
         Hashtable var2 = ((IRecord)var1).getData();
         String var3 = this.getString(var2.get("sex"));
         if (!"".equals(var3)) {
            if ("Férfi".equals(var3)) {
               var2.put("sex", "1");
            } else if ("Nő".equals(var3)) {
               var2.put("sex", "2");
            }
         }
      }

   }

   private String getString(Object var1) {
      return var1 == null ? "" : var1.toString();
   }

   static {
      primacc_to_xmlid.put("Adózó neve", "name");
      primacc_to_xmlid.put("Név titulus", "first_name_title");
      primacc_to_xmlid.put("Adózó neme", "sex");
      primacc_to_xmlid.put("Vezetékneve", "first_name");
      primacc_to_xmlid.put("Keresztneve", "last_name");
      primacc_to_xmlid.put("Adózó adószáma", "tax_number");
      primacc_to_xmlid.put("Adózó adóazonosító jele", "tax_id");
      primacc_to_xmlid.put("Közösségi adószám", "eu_tax_number");
      primacc_to_xmlid.put("Bevallási időszak kezdete", "term_from");
      primacc_to_xmlid.put("Bevallási időszak vége", "term_to");
      primacc_to_xmlid.put("Adózó aláírása", "sign");
      primacc_to_xmlid.put("Vonalkód", "line_code");
      primacc_to_xmlid.put("Státusz", "state");
      primacc_to_xmlid.put("Vonatkozik", "apply");
      primacc_to_xmlid.put("Adótanácsadó neve", "te_name");
      primacc_to_xmlid.put("Adótanácsadó azonosítószáma", "te_id");
      primacc_to_xmlid.put("Adótanácsadó Bizonyítvány", "te_testimontial_id");
      primacc_to_xmlid.put("Ügyintéző neve", "administrator");
      primacc_to_xmlid.put("Ügyintéző telefonszáma", "tel");
      primacc_to_xmlid.put("Pénzintézet-azonosító", "financial_corp_id");
      primacc_to_xmlid.put("Pénzintézet neve", "financial_corp");
      primacc_to_xmlid.put("Számla-azonosító", "account_id");
      primacc_to_xmlid.put("Irányítószám", "s_zip");
      primacc_to_xmlid.put("Település", "s_settlement");
      primacc_to_xmlid.put("Közterület neve", "s_public_place");
      primacc_to_xmlid.put("Házszám", "s_house_number");
      primacc_to_xmlid.put("Emelet", "s_level");
      primacc_to_xmlid.put("Közterület jellege", "s_public_place_type");
      primacc_to_xmlid.put("Épület", "s_building");
      primacc_to_xmlid.put("Lépcsőház", "s_staircase");
      primacc_to_xmlid.put("Ajtó", "s_door");
      primacc_to_xmlid.put("L Irányítószám", "m_zip");
      primacc_to_xmlid.put("L Település", "m_settlement");
      primacc_to_xmlid.put("L Közterület neve", "m_public_place");
      primacc_to_xmlid.put("L Házszám", "m_house_number");
      primacc_to_xmlid.put("L Emelet", "m_level");
      primacc_to_xmlid.put("L Közterület jellege", "m_public_place_type");
      primacc_to_xmlid.put("L Épület", "m_building");
      primacc_to_xmlid.put("L Lépcsőház", "m_staircase");
      primacc_to_xmlid.put("L Ajtó", "m_door");
   }
}
