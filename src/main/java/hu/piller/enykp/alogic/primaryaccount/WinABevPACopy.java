package hu.piller.enykp.alogic.primaryaccount;

import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.primaryaccount.common.DefaultEnvelope;
import hu.piller.enykp.alogic.primaryaccount.common.DefaultRecord;
import hu.piller.enykp.alogic.primaryaccount.common.DefaultRecordFactory;
import hu.piller.enykp.alogic.primaryaccount.common.IRecord;
import hu.piller.enykp.alogic.primaryaccount.companies.CompaniesBusiness;
import hu.piller.enykp.alogic.primaryaccount.companies.CompanyRecordFactory;
import hu.piller.enykp.alogic.primaryaccount.people.PeopleBusiness;
import hu.piller.enykp.alogic.primaryaccount.people.PeopleRecordFactory;
import hu.piller.enykp.alogic.primaryaccount.smallbusiness.SmallBusinessBusiness;
import hu.piller.enykp.alogic.primaryaccount.smallbusiness.SmallBusinessRecordFactory;
import hu.piller.enykp.alogic.primaryaccount.taxexperts.TaxExpertBusiness;
import hu.piller.enykp.alogic.primaryaccount.taxexperts.TaxExpertRecordFactory;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import me.necrocore.abevjava.NecroFile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

public class WinABevPACopy implements IEventListener {
   public static final int CONSTRAINT_ONLY_PACCOUNTS = 0;
   public static final int CONSTRAINT_ONLY_TAXEXPERTS = 1;
   private static final String[] type_keys = new String[]{"adoszam", "adoszam", "adoazonosito", "azonosito"};
   private static final String[] xml_type_keys = new String[]{"tax_number", "tax_number", "tax_id", "te_id"};
   private static final String ERR_ID = "WinABevPACopy";
   private static final boolean is_testing = false;
   private boolean is_changed;
   private boolean is_canceled;
   private Vector error_list;

   public void start() {
      this.start(-1);
   }

   public void start(int var1) {
      int var2 = 1;
      if (var1 == 0) {
         var2 = JOptionPane.showConfirmDialog(MainFrame.thisinstance, "Indulhat a törzsadatok átvétele ABev-ből ?", "Törzsadatok átvétele", 0);
      } else if (var1 == 1) {
         var2 = JOptionPane.showConfirmDialog(MainFrame.thisinstance, "Indulhat az adótanácsadók átvétele ABev-ből ?", "Adótanácsadók átvétele", 0);
      } else if (var1 == -1) {
         var2 = 0;
      }

      if (var2 == 0) {
         this.start_(var1);
      }

   }

   private void start_(int var1) {
      this.is_changed = false;
      this.is_canceled = false;
      this.error_list = new Vector(256);
      File[] var3 = this.getABevPAFile(var1);
      boolean var4 = false;
      if (var3 != null) {
         int var5 = 0;

         for(int var6 = var3.length; var5 < var6; ++var5) {
            File var2 = var3[var5];
            if (var2 != null) {
               try {
                  BufferedReader var7 = new BufferedReader(new InputStreamReader(new FileInputStream(var2), "ISO-8859-2"));
                  Vector var8 = this.loadRecords(var7);
                  var7.close();
                  this.installErrorListener();
                  if (!this.is_canceled) {
                     this.fetchRecords(new CompanyRecordFactory(), CompaniesBusiness.getPrimaryAccountPath(), var8, 0);
                  }

                  if (!this.is_canceled) {
                     this.fetchRecords(new SmallBusinessRecordFactory(), SmallBusinessBusiness.getPrimaryAccountPath(), var8, 1);
                  }

                  if (!this.is_canceled) {
                     this.fetchRecords(new PeopleRecordFactory(), PeopleBusiness.getPrimaryAccountPath(), var8, 2);
                  }

                  if (!this.is_canceled) {
                     this.fetchRecords(new TaxExpertRecordFactory(), TaxExpertBusiness.getPrimaryAccountPath(), var8, 3);
                  }

                  this.uninstallErrorListener();
                  if (this.is_changed) {
                     var4 = true;
                  }

                  if (this.is_canceled) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "ABev törzsadat(ok) átemelését a felhasználó megszakította.\n(" + var2.toString() + ")" + (this.is_changed ? "\nAz átemelés részben megtörtént." : "\nVáltozás nem történt."), "ABev törzsadatok átvétele", 2);
                     break;
                  }

                  String var9;
                  if (var1 == 0) {
                     var9 = "Törzsadatok átvétele befejeződött";
                  } else if (var1 == 1) {
                     var9 = "Adótanácsadók átvétele  befejeződött";
                  } else {
                     var9 = "ABev törzsadat(ok) átemelése megtörtént";
                  }

                  GuiUtil.showMessageDialog(MainFrame.thisinstance, var9 + ".\n(" + var2.toString() + ")" + (this.is_changed ? "" : "\nVáltozás nem történt."), "ABev törzsadatok átvétele", 1);
               } catch (Exception var10) {
                  this.uninstallErrorListener();
                  var10.printStackTrace();
               }
            }
         }

         if (var4) {
            PAInfo.getInstance().reloadDialog();
         }
      }

      if (this.error_list.size() > 0) {
         this.showErrorList();
      }

   }

   private File[] getABevPAFile(int var1) {
      final String var2 = var1 < 0 ? ".dtt" : (var1 == 0 ? "torzs.dtt" : (var1 == 1 ? "atan.dtt" : ".dtt"));
      File[] var3 = new File[]{new NecroFile("C:\\Program Files\\Abev 2008\\"), new NecroFile("C:\\Program Files\\Abev 2006\\"), new NecroFile("D:\\Program Files\\Abev 2008\\"), new NecroFile("d:\\Program Files\\Abev 2006\\")};
      File[] var4 = null;
      int var5 = 0;

      for(int var6 = var3.length; var5 < var6; ++var5) {
         if (var3[var5].exists()) {
            File[] var7 = var3[var5].listFiles(new FilenameFilter() {
               public boolean accept(File var1, String var2x) {
                  return WinABevPACopy.this.checkFileName(var2x, var2);
               }
            });
            if (var7 != null && var7.length > 0) {
               if (var4 == null) {
                  var4 = var7;
               } else {
                  File[] var8 = var4;
                  var4 = new File[var4.length + var7.length];
                  System.arraycopy(var8, 0, var4, 0, var8.length);
                  System.arraycopy(var7, 0, var4, var8.length, var7.length);
               }
            }
         }
      }

      if (var4 == null) {
         String var9;
         if (var1 == 0) {
            var9 = "Törzsadatok átvétele ABev-ből";
         } else if (var1 == 1) {
            var9 = "Adótanácsadók átvétele ABev-ből";
         } else {
            var9 = "ABev törzsadat állomány kiválasztása";
         }

         JFileChooser var10 = new JFileChooser();
         var10.setDialogTitle(var9);
         var10.setDialogType(0);
         var10.setCurrentDirectory(new NecroFile("." + File.separator));
         var10.setFileSelectionMode(0);
         var10.setMultiSelectionEnabled(true);
         var10.setAcceptAllFileFilterUsed(false);
         var10.addChoosableFileFilter(new FileFilter() {
            public boolean accept(File var1) {
               return var1.isDirectory() || WinABevPACopy.this.checkFileName(var1.getName(), var2);
            }

            public String getDescription() {
               return "ABev törzsadat file (" + var2 + ")";
            }
         });
         var10.showOpenDialog(MainFrame.thisinstance);
         var4 = var10.getSelectedFiles();
      }

      return var4;
   }

   private boolean checkFileName(String var1, String var2) {
      boolean var3;
      if (var2.startsWith(".")) {
         var3 = var1.toLowerCase().endsWith(var2);
      } else {
         var3 = var1.equalsIgnoreCase(var2);
      }

      return var3;
   }

   private Vector loadRecords(BufferedReader var1) throws IOException {
      Vector var2 = new Vector();
      var2.add(new Hashtable());
      var2.add(new Hashtable());
      var2.add(new Hashtable());
      var2.add(new Hashtable());
      Hashtable var3 = null;
      String var5 = "";

      String var4;
      String var6;
      while((var4 = var1.readLine()) != null) {
         var4 = var4.trim();
         if (var4.length() > 0) {
            if (var4.startsWith("[")) {
               if (var3 != null) {
                  var6 = this.getKeyForRecord(var3);
                  if (!var3.containsKey(var6)) {
                     var3.put(var6, var5);
                  }

                  this.storeRecord(var2, var3);
               }

               var3 = new Hashtable();
               var5 = var4.substring(1, var4.length() - 1);
            }

            if (var3 != null) {
               String[] var7 = var4.split("=", 2);
               if (var7.length == 2) {
                  var3.put(var7[0], var7[1]);
               }
            }
         }
      }

      var6 = this.getKeyForRecord(var3);
      if (!var3.containsKey(var6)) {
         var3.put(var6, var5);
      }

      this.storeRecord(var2, var3);
      return var2;
   }

   private String getKeyForRecord(Hashtable var1) {
      String var2 = "";
      String var3 = (String)var1.get("tipus");
      if (var3 == null) {
         var2 = type_keys[3];
      } else if (var3.equals("0")) {
         var2 = type_keys[0];
      } else if (var3.equals("1")) {
         var2 = type_keys[1];
      } else if (var3.equals("2")) {
         var2 = type_keys[2];
      }

      return var2;
   }

   private void storeRecord(Vector var1, Hashtable var2) {
      if (var2 != null && var2.size() > 0) {
         Object var3 = var2.get("tipus");
         if (var3 == null) {
            this.putRecord((Hashtable)var1.get(3), var2, type_keys[3]);
         } else {
            String var4 = var3.toString().trim();
            if (var4.equals("0")) {
               this.putRecord((Hashtable)var1.get(0), var2, type_keys[0]);
            } else if (var4.equals("1")) {
               this.putRecord((Hashtable)var1.get(1), var2, type_keys[1]);
            } else if (var4.equals("2")) {
               this.putRecord((Hashtable)var1.get(2), var2, type_keys[2]);
            }
         }
      }

   }

   private void putRecord(Hashtable var1, Hashtable var2, String var3) {
      Object var4 = var1.get(var3);
      if (var4 != null) {
         var2 = this.handleSameRecords((Hashtable)var4, var2);
      }

      if (var2 != null) {
         var1.put(var2.get(var3), var2);
      }

   }

   private Hashtable handleSameRecords(Hashtable var1, Hashtable var2) {
      return var2;
   }

   private void fetchRecords(DefaultRecordFactory var1, File var2, Vector var3, int var4) throws Exception {
      if (var2 != null) {
         Hashtable var5 = (Hashtable)var3.get(var4);
         if (var5.size() > 0) {
            var1.loadRecords((File)var2, (DefaultEnvelope)null);
            Vector var6 = new Vector(4096, 4096);
            Enumeration var9 = var5.elements();

            DefaultRecord var7;
            Vector var11;
            int var12;
            int var13;
            label55:
            while(true) {
               Hashtable var8;
               do {
                  if (!var9.hasMoreElements()) {
                     break label55;
                  }

                  var8 = (Hashtable)var9.nextElement();
                  var7 = new DefaultRecord(var1, var2, (DefaultEnvelope)null);
               } while(!this.setData(var7, var8, var4));

               IRecord[] var10;
               if ((var10 = var7.filter(xml_type_keys[var4], (String)var7.getData().get(xml_type_keys[var4]))).length > 0) {
                  var7 = this.handleSameRecords2(var10, var7, var4);
                  if (this.is_canceled) {
                     break;
                  }

                  if (var7 != null) {
                     var11 = var1.getRecords();
                     var12 = 0;

                     for(var13 = var10.length; var12 < var13; ++var12) {
                        var11.remove(var10[var12]);
                     }
                  }
               }

               if (var7 != null) {
                  var6.add(var7);
               }
            }

            if (this.is_canceled) {
               return;
            }

            var11 = var1.getRecords();
            var12 = 0;

            for(var13 = var6.size(); var12 < var13; ++var12) {
               var7 = (DefaultRecord)var6.get(var12);
               var7.getData().put("id", var1.getNewId());
               var11.add(var7);
            }

            if (var6.size() > 0) {
               var1.saveRecords(var2);
               this.is_changed = true;
            }
         }
      }

   }

   private boolean setData(DefaultRecord var1, Hashtable var2, int var3) {
      boolean var4 = false;
      Hashtable var5 = var1.getData();
      switch(var3) {
      case 0:
         if (this.isValidTaxNumber(this.getValue(var2.get("adoszam")))) {
            var5.put("name", this.getValue(var2.get("nev")));

            try {
               var5.put("tax_number", this.filterForNumbers(this.getValue(var2.get("adoszam"))));
            } catch (Exception var15) {
               hu.piller.enykp.util.base.Tools.eLog(var15, 0);
            }

            var5.put("s_settlement", this.getValue(var2.get("varos")));
            var5.put("s_public_place", this.getValue(var2.get("utca")));
            var5.put("s_public_place_type", this.getValue(var2.get("kozt")));
            var5.put("s_house_number", this.getValue(var2.get("hazszam")));
            var5.put("s_building", this.getValue(var2.get("epulet")));
            var5.put("s_staircase", this.getValue(var2.get("lepcsohaz")));
            var5.put("s_level", this.getValue(var2.get("emelet")));
            var5.put("s_door", this.getValue(var2.get("ajto")));
            var5.put("s_zip", this.getValue(var2.get("irszam")));
            var5.put("m_settlement", this.getValue(var2.get("varos1")));
            var5.put("m_public_place", this.getValue(var2.get("utca1")));
            var5.put("m_public_place_type", this.getValue(var2.get("kozt1")));
            var5.put("m_house_number", this.getValue(var2.get("hazszam1")));
            var5.put("m_building", this.getValue(var2.get("epulet1")));
            var5.put("m_staircase", this.getValue(var2.get("lepcsohaz1")));
            var5.put("m_level", this.getValue(var2.get("emelet1")));
            var5.put("m_door", this.getValue(var2.get("ajto1")));
            var5.put("m_zip", this.getValue(var2.get("irszam1")));
            var5.put("administrator", this.getValue(var2.get("ugyi")));
            var5.put("tel", this.getValue(var2.get("ugyitel")));
            var5.put("financial_corp", this.getValue(var2.get("bankneve")));

            try {
               var5.put("financial_corp_id", this.filterForNumbers(this.getValue(var2.get("szamlaszam"))).substring(0, 8));
            } catch (Exception var14) {
               hu.piller.enykp.util.base.Tools.eLog(var14, 0);
            }

            try {
               var5.put("account_id", this.filterForNumbers(this.getValue(var2.get("szamlaszam"))).substring(8));
            } catch (Exception var13) {
               hu.piller.enykp.util.base.Tools.eLog(var13, 0);
            }

            var4 = true;
         } else {
            this.writeErrorParagraph("Hibás adószám ! (" + this.getValue(var2.get("adoszam")) + ")", var2, var3);
         }
         break;
      case 1:
         if (this.isValidTaxId(this.getValue(var2.get("adoazonosito")))) {
            if (this.isValidTaxNumber(this.getValue(var2.get("adoszam")))) {
               var5.put("name", this.getValue(var2.get("nev")));
               var5.put("first_name", this.getValue(var2.get("vnev")));
               var5.put("last_name", this.getValue(var2.get("knev")));

               try {
                  var5.put("tax_number", this.filterForNumbers(this.getValue(var2.get("adoszam"))));
               } catch (Exception var12) {
                  hu.piller.enykp.util.base.Tools.eLog(var12, 0);
               }

               var5.put("tax_id", this.getValue(var2.get("adoazonosito")));
               var5.put("s_settlement", this.getValue(var2.get("varos")));
               var5.put("s_public_place", this.getValue(var2.get("utca")));
               var5.put("s_public_place_type", this.getValue(var2.get("kozt")));
               var5.put("s_house_number", this.getValue(var2.get("hazszam")));
               var5.put("s_building", this.getValue(var2.get("epulet")));
               var5.put("s_staircase", this.getValue(var2.get("lepcsohaz")));
               var5.put("s_level", this.getValue(var2.get("emelet")));
               var5.put("s_door", this.getValue(var2.get("ajto")));
               var5.put("s_zip", this.getValue(var2.get("irszam")));
               var5.put("m_settlement", this.getValue(var2.get("varos1")));
               var5.put("m_public_place", this.getValue(var2.get("utca1")));
               var5.put("m_public_place_type", this.getValue(var2.get("kozt1")));
               var5.put("m_house_number", this.getValue(var2.get("hazszam1")));
               var5.put("m_building", this.getValue(var2.get("epulet1")));
               var5.put("m_staircase", this.getValue(var2.get("lepcsohaz1")));
               var5.put("m_level", this.getValue(var2.get("emelet1")));
               var5.put("m_door", this.getValue(var2.get("ajto1")));
               var5.put("m_zip", this.getValue(var2.get("irszam1")));
               var5.put("administrator", this.getValue(var2.get("ugyi")));
               var5.put("tel", this.getValue(var2.get("ugyitel")));
               var5.put("financial_corp", this.getValue(var2.get("bankneve")));

               try {
                  var5.put("financial_corp_id", this.filterForNumbers(this.getValue(var2.get("szamlaszam"))).substring(0, 8));
               } catch (Exception var11) {
                  hu.piller.enykp.util.base.Tools.eLog(var11, 0);
               }

               try {
                  var5.put("account_id", this.filterForNumbers(this.getValue(var2.get("szamlaszam"))).substring(8));
               } catch (Exception var10) {
                  hu.piller.enykp.util.base.Tools.eLog(var10, 0);
               }

               var4 = true;
            } else {
               this.writeErrorParagraph("Hibás adószám ! (" + this.getValue(var2.get("adoszam")) + ")", var2, var3);
            }
         } else {
            this.writeErrorParagraph("Hibás adóazonosító jel ! (" + this.getValue(var2.get("adoazonosito")) + ")", var2, var3);
         }
         break;
      case 2:
         if (this.isValidTaxId(this.getValue(var2.get("adoazonosito")))) {
            var5.put("name", this.getValue(var2.get("nev")));
            var5.put("first_name", this.getValue(var2.get("vnev")));
            var5.put("last_name", this.getValue(var2.get("knev")));
            var5.put("tax_id", this.getValue(var2.get("adoazonosito")));
            var5.put("s_settlement", this.getValue(var2.get("varos")));
            var5.put("s_public_place", this.getValue(var2.get("utca")));
            var5.put("s_public_place_type", this.getValue(var2.get("kozt")));
            var5.put("s_house_number", this.getValue(var2.get("hazszam")));
            var5.put("s_building", this.getValue(var2.get("epulet")));
            var5.put("s_staircase", this.getValue(var2.get("lepcsohaz")));
            var5.put("s_level", this.getValue(var2.get("emelet")));
            var5.put("s_door", this.getValue(var2.get("ajto")));
            var5.put("s_zip", this.getValue(var2.get("irszam")));
            var5.put("m_settlement", this.getValue(var2.get("varos1")));
            var5.put("m_public_place", this.getValue(var2.get("utca1")));
            var5.put("m_public_place_type", this.getValue(var2.get("kozt1")));
            var5.put("m_house_number", this.getValue(var2.get("hazszam1")));
            var5.put("m_building", this.getValue(var2.get("epulet1")));
            var5.put("m_staircase", this.getValue(var2.get("lepcsohaz1")));
            var5.put("m_level", this.getValue(var2.get("emelet1")));
            var5.put("m_door", this.getValue(var2.get("ajto1")));
            var5.put("m_zip", this.getValue(var2.get("irszam1")));
            var5.put("administrator", this.getValue(var2.get("ugyi")));
            var5.put("tel", this.getValue(var2.get("ugyitel")));
            var5.put("financial_corp", this.getValue(var2.get("bankneve")));

            try {
               var5.put("financial_corp_id", this.filterForNumbers(this.getValue(var2.get("szamlaszam"))).substring(0, 8));
            } catch (Exception var9) {
               hu.piller.enykp.util.base.Tools.eLog(var9, 0);
            }

            try {
               var5.put("account_id", this.filterForNumbers(this.getValue(var2.get("szamlaszam"))).substring(8));
            } catch (Exception var8) {
               hu.piller.enykp.util.base.Tools.eLog(var8, 0);
            }

            var4 = true;
         } else {
            this.writeErrorParagraph("Hibás adóazonosító jel ! (" + this.getValue(var2.get("adoazonosito")) + ")", var2, var3);
         }
         break;
      case 3:
         String var6 = this.getValue(var2.get("azonosito"));
         boolean var7;
         if (var6.length() > 10) {
            var7 = this.isValidTaxNumber(var6);
            if (!var7) {
               this.writeErrorParagraph("Hibás adószám ! (" + this.getValue(var2.get("azonosito")) + ")", var2, var3);
            }
         } else {
            var7 = this.isValidTaxId(var6);
            if (!var7) {
               this.writeErrorParagraph("Hibás adóazonosító jel ! (" + this.getValue(var2.get("azonosito")) + ")", var2, var3);
            }
         }

         if (var7) {
            var5.put("te_name", this.getValue(var2.get("neve")));
            var5.put("te_id", this.filterForNumbers(var6));
            var5.put("te_testimontial_id", this.getValue(var2.get("bizonyitvany")));
            var4 = true;
         }
      }

      return var4;
   }

   private void writeErrorParagraph(String var1, Hashtable var2, int var3) {
      ErrorList.getInstance().writeError("WinABevPACopy", var1, (Exception)null, (Object)null);
      String[] var4 = new String[3];
      String var5 = "        ";
      switch(var3) {
      case 0:
         var4[0] = var5 + this.getValue(var2.get("nev")) + "  " + this.getValue(var2.get("adoszam"));
         var4[1] = var5 + this.getValue(var2.get("irszam")) + " " + this.getValue(var2.get("varos"));
         var4[2] = var5 + this.getValue(var2.get("utca")) + " " + this.getValue(var2.get("kozt")) + " " + this.getValue(var2.get("hazszam"));
         break;
      case 1:
         var4[0] = var5 + this.getValue(var2.get("nev")) + "  " + this.getValue(var2.get("adoszam")) + " " + this.getValue(var2.get("adoazonosito"));
         var4[1] = var5 + this.getValue(var2.get("irszam")) + " " + this.getValue(var2.get("varos"));
         var4[2] = var5 + this.getValue(var2.get("utca")) + " " + this.getValue(var2.get("kozt")) + " " + this.getValue(var2.get("hazszam"));
         break;
      case 2:
         var4[0] = var5 + this.getValue(var2.get("nev")) + "  " + this.getValue(var2.get("adoazonosito"));
         var4[1] = var5 + this.getValue(var2.get("irszam")) + " " + this.getValue(var2.get("varos"));
         var4[2] = var5 + this.getValue(var2.get("utca")) + " " + this.getValue(var2.get("kozt")) + " " + this.getValue(var2.get("hazszam"));
         break;
      case 3:
         var4[0] = var5 + this.getValue(var2.get("neve"));
         var4[1] = var5 + this.getValue(var2.get("azonosito"));
         var4[2] = var5 + this.getValue(var2.get("bizonyitvany"));
      }

      ErrorList.getInstance().writeError("WinABevPACopy", var4[0], (Exception)null, (Object)null);
      ErrorList.getInstance().writeError("WinABevPACopy", var4[1], (Exception)null, (Object)null);
      ErrorList.getInstance().writeError("WinABevPACopy", var4[2], (Exception)null, (Object)null);
   }

   private boolean isValidTaxId(String var1) {
      Calculator var2 = Calculator.getInstance();
      String var3 = this.filterForNumbers(var1);
      if (var1.trim().length() > 0 && var3.trim().length() == 0) {
         return false;
      } else {
         Object var4 = var2.calculateExpression("[jóadóazonosító,\"" + var3 + "\"]");
         if (var4 == null) {
            return false;
         } else {
            return var4 instanceof Boolean ? (Boolean)var4 : true;
         }
      }
   }

   private boolean isValidTaxNumber(String var1) {
      Calculator var2 = Calculator.getInstance();
      String var3 = this.filterForNumbers(var1);
      if (var1.trim().length() > 0 && var3.trim().length() == 0) {
         return false;
      } else {
         Object var4 = var2.calculateExpression("[jóadószám,\"" + var3 + "\"]");
         if (var4 == null) {
            return false;
         } else {
            return var4 instanceof Boolean ? (Boolean)var4 : true;
         }
      }
   }

   private DefaultRecord handleSameRecords2(IRecord[] var1, DefaultRecord var2, int var3) {
      int var4 = WinABevPACopy.DOptionPane.show((DefaultRecord)var1[0], var2, var3);
      if (var4 == 1) {
         var2 = null;
      }

      if (var4 == 2) {
         this.is_canceled = true;
      }

      return var2;
   }

   private String getValue(Object var1) {
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

   private void installErrorListener() {
      ((IEventSupport)ErrorList.getInstance()).addEventListener(this);
   }

   private void uninstallErrorListener() {
      ((IEventSupport)ErrorList.getInstance()).removeEventListener(this);
   }

   public Object eventFired(Event var1) {
      Object var2 = var1.getUserData();
      if (var2 instanceof Hashtable) {
         var2 = ((Hashtable)var2).get("item");
         if (var2 instanceof Object[]) {
            Object[] var3 = (Object[])((Object[])var2);
            if ("WinABevPACopy".equals(var3[0])) {
               this.error_list.add(var3[1]);
            }
         }
      }

      return null;
   }

   private void showErrorList() {
      new ErrorDialog(MainFrame.thisinstance, "Átemelés közben történt hibák", true, false, this.error_list);
   }

   private static class DOptionPane {
      private static int a;

      static int show(DefaultRecord var0, DefaultRecord var1, int var2) {
         a = -1;
         JDialog var3 = getDialog(var0, var1, var2);
         var3.pack();
         var3.setResizable(false);
         var3.setLocationRelativeTo(MainFrame.thisinstance);
         var3.setModal(true);
         var3.setVisible(true);
         var3.dispose();
         return a;
      }

      private static JDialog getDialog(DefaultRecord var0, DefaultRecord var1, int var2) {
         String var3 = "Átemelés során azonos bejegyzést talált a program:";
         var3 = var3 + "\n\nEredeti:";
         var3 = var3 + "\n\n" + getData(var0, var2);
         var3 = var3 + "\n\nÚj:";
         var3 = var3 + "\n\n" + getData(var1, var2);
         final JDialog var4 = new JDialog(MainFrame.thisinstance);
         JTextArea var5 = new JTextArea();
         var5.setLineWrap(false);
         var5.setWrapStyleWord(true);
         var5.setEditable(false);
         var5.setForeground((Color)UIManager.get("Label.foreground"));
         var5.setBackground((Color)UIManager.get("Label.background"));
         var5.setFont(UIManager.getFont("Button.font"));
         var5.setFocusable(false);
         var5.setText(var3);
         JPanel var6 = new JPanel();
         var6.setLayout(new BorderLayout());
         var6.add(var5, "Center");
         var6.add(Box.createVerticalStrut(10), "North");
         JLabel var7 = new JLabel("  ", UIManager.getIcon("OptionPane.questionIcon"), 2);
         var7.setVerticalAlignment(1);
         var6.add(var7, "West");
         var6.add(Box.createHorizontalStrut(10), "East");
         var6.add(Box.createVerticalStrut(5), "South");
         JButton var8 = new JButton("Felülírás");
         var8.setDefaultCapable(true);
         JButton var9 = new JButton("Kihagyás");
         JButton var10 = new JButton("Áttöltés vége");
         JPanel var11 = new JPanel();
         var11.setLayout(new FlowLayout(1, 5, 5));
         var11.add(var8);
         var11.add(var9);
         var11.add(var10);
         JPanel var12 = new JPanel();
         var12.setLayout(new BorderLayout());
         var12.add(var6, "Center");
         var12.add(var11, "South");
         Container var13 = var4.getContentPane();
         var13.setLayout(new BorderLayout());
         var13.add(var12, "Center");
         var13.add(Box.createHorizontalStrut(10), "West");
         var4.setTitle("Azonos bejegyzések kezelése ...");
         var4.getRootPane().setDefaultButton(var8);
         var8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               WinABevPACopy.DOptionPane.handleButtonEvent(0, var4);
            }
         });
         var9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               WinABevPACopy.DOptionPane.handleButtonEvent(1, var4);
            }
         });
         var10.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               WinABevPACopy.DOptionPane.handleButtonEvent(2, var4);
            }
         });
         return var4;
      }

      private static void handleButtonEvent(int var0, JDialog var1) {
         a = var0;
         var1.setVisible(false);
      }

      private static String getData(DefaultRecord var0, int var1) {
         String var2 = "(Nincs adat)";
         if (var0 != null) {
            Hashtable var3 = var0.getData();
            switch(var1) {
            case 0:
               var2 = "" + getString(var3.get("name")) + "  " + getString(var3.get("tax_number"));
               var2 = var2 + "\n" + getString(var3.get("s_zip")) + " " + getString(var3.get("s_settlement"));
               var2 = var2 + "\n" + getString(var3.get("s_public_place")) + " " + getString(var3.get("s_public_place_type")) + " " + getString(var3.get("s_house_number"));
               break;
            case 1:
               var2 = "" + getString(var3.get("name")) + "  " + getString(var3.get("tax_number")) + " " + getString(var3.get("tax_id"));
               var2 = var2 + "\n" + getString(var3.get("s_zip")) + " " + getString(var3.get("s_settlement"));
               var2 = var2 + "\n" + getString(var3.get("s_public_place")) + " " + getString(var3.get("s_public_place_type")) + " " + getString(var3.get("s_house_number"));
               break;
            case 2:
               var2 = "" + getString(var3.get("name")) + "  " + getString(var3.get("tax_id"));
               var2 = var2 + "\n" + getString(var3.get("s_zip")) + " " + getString(var3.get("s_settlement"));
               var2 = var2 + "\n" + getString(var3.get("s_public_place")) + " " + getString(var3.get("s_public_place_type")) + " " + getString(var3.get("s_house_number"));
               break;
            case 3:
               var2 = "" + getString(var3.get("te_name"));
               var2 = var2 + "\n" + getString(var3.get("te_id"));
               var2 = var2 + "\n" + getString(var3.get("te_testimontial_id"));
            }
         }

         return var2;
      }

      private static String getString(Object var0) {
         return var0 == null ? "" : var0.toString();
      }
   }
}
