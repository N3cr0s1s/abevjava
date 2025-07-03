package hu.piller.enykp.alogic.filesaver.db;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.filesaver.xml.ErrorListListener4XmlSave;
import hu.piller.enykp.alogic.fileutil.DataChecker;
import hu.piller.enykp.alogic.fileutil.HeadChecker;
import hu.piller.enykp.datastore.Datastore_history;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.GUI_Datastore;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.extensions.db.DbFactory;
import hu.piller.enykp.extensions.db.IDbHandler;
import hu.piller.enykp.extensions.elogic.ElogicCaller;
import hu.piller.enykp.extensions.elogic.IELogicResult;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.viewer.DefaultMultiFormViewer;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IResult;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import java.awt.Component;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class RDbSaver {
   IDbHandler dbhandler;

   public RDbSaver() {
      try {
         this.dbhandler = DbFactory.getDbHandler();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public int save() {
      if (this.dbhandler == null) {
         return 1;
      } else {
         DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
         if (var1 != null) {
            if (!var1.fv.pv.pv.leave_component()) {
               return 1;
            }

            BookModel var2 = var1.bm;

            try {
               if (MainFrame.rogzitomode) {
                  return this.savefixing(var2, "3", "2");
               }

               MainFrame.thisinstance.setGlassLabel("Mentés folyamatban!");
               ErrorList.getInstance().clear();
               Result var3 = DataChecker.getInstance().superCheck(var2, false);
               if (!var3.isOk()) {
                  new ErrorDialog(MainFrame.thisinstance, "Hibalista", true, true, var3.errorList);
                  MainFrame.thisinstance.setGlassLabel((String)null);
                  return 1;
               }

               int var4 = Integer.parseInt(MainFrame.role);
               int var5 = Integer.parseInt("2");
               int var6 = Integer.parseInt("3");
               String var7 = "1";
               ErrorListListener4XmlSave var8 = new ErrorListListener4XmlSave(-1);
               IErrorList var9 = ErrorList.getInstance();
               ((IEventSupport)var9).addEventListener(var8);
               if (MainFrame.opmode != null && !MainFrame.opmode.equals("0")) {
                  System.out.println("calling_check_all - InnerSave");
               }

               CalculatorManager.getInstance().do_check_all((IResult)null, var8);
               if (var8.getFatalError() != 0) {
                  GuiUtil.showMessageDialog((Component)null, "A nyomtatvány súlyos hibát tartalmaz, nem menthető!", "Üzenet", 0);
                  MainFrame.thisinstance.setGlassLabel((String)null);
                  return 1;
               }

               if (("2".equals(MainFrame.role) || "3".equals(MainFrame.role)) && MainFrame.ellvitamode) {
                  Object var10 = this.dbhandler.checkKihatas(var2);
                  if (var10 != null) {
                     try {
                        new ErrorDialog((Frame)null, "Hibalista", true, false, (Vector)var10);
                     } catch (HeadlessException var28) {
                     }

                     MainFrame.thisinstance.setGlassLabel((String)null);
                     return 1;
                  }
               }

               int var32 = var8.getRealErrorExtra();
               Hashtable var11 = this.dbhandler.getHibasanIsKonyvelhetoTable();
               int var12 = var8.getRealErrorM009(var11);
               Vector var13 = var8.getErrorListForDBBatch();
               ((IEventSupport)var9).removeEventListener(var8);
               String var14 = "U";
               var7 = "N";
               if (MainFrame.isPart) {
                  var14 = "F";
               }

               boolean var15 = "2".equals(MainFrame.role) || "3".equals(MainFrame.role);
               String var16;
               int var18;
               if (var12 == 0 && !MainFrame.isPart && !var15 || MainFrame.isPart) {
                  var16 = "A bevallás hibátlan.\nVisszatartja a javításra várók között?";
                  if (MainFrame.isPart) {
                     var16 = "Részbizonylat javítás.\nAkarja-e folytatni a bizonylat javítását?";
                  }

                  Object[] var17 = new Object[]{"Igen", "Nem"};
                  var18 = JOptionPane.showOptionDialog(MainFrame.thisinstance, var16, "Kérdés", 0, 3, (Icon)null, var17, var17[1]);
                  if (var18 == 0) {
                     var7 = "I";
                  } else {
                     var7 = "N";
                  }
               }

               var16 = "4";
               if (var4 == var5) {
                  var16 = "5";
               }

               if (var4 == var6) {
                  var16 = "5";
               }

               Hashtable var33 = this.dbhandler.startSaveAndExit(new BigDecimal(var2.getBizt_azon()), var14, MainFrame.d_id);
               var18 = (Integer)var33.get("ret");
               if (var18 != 0) {
                  GuiUtil.showMessageDialog((Component)null, "Mentés közben hiba lépett fel!\nHibakód: " + var18, "Üzenet", 0);
                  MainFrame.thisinstance.setGlassLabel((String)null);
                  return 1;
               }

               try {
                  var2.setEvent_azon((String)var33.get("beny_azon"));
               } catch (Exception var30) {
               }

               IELogicResult var19 = ElogicCaller.eventBeforeSave(var2);
               if (var19.getStatus() != 0) {
                  GuiUtil.showMessageDialog((Component)null, "Mentés előtti hiba lépett fel!\nHibaüzenet: " + var19.getMessage(), "Üzenet", 0);
                  MainFrame.thisinstance.setGlassLabel((String)null);
                  return 1;
               }

               boolean var20 = this.save(var2, var16, var7, var13);
               if (!var20) {
                  GuiUtil.showMessageDialog((Component)null, "Mentés közben súlyos hiba lépett fel!\nHibakód: -1", "Üzenet", 0);
                  MainFrame.thisinstance.setGlassLabel((String)null);
                  return 1;
               }

               var2.dirty = false;
               if (("2".equals(MainFrame.role) || "3".equals(MainFrame.role)) && MainFrame.ellvitamode) {
                  Object var21 = this.dbhandler.saveKihatas(var2);
                  if (var21 != null) {
                     var2.dirty = true;

                     try {
                        new ErrorDialog((Frame)null, "Hibalista", true, false, (Vector)var21);
                     } catch (HeadlessException var27) {
                     }

                     MainFrame.thisinstance.setGlassLabel((String)null);
                     return 1;
                  }
               }

               String var34 = "fk";
               String var22 = "no comment";
               String var23 = this.dbhandler.endSaveAndExit(var2.getBarcode(), new BigDecimal(var2.getBizt_azon()), 0 < var32 ? "1" : "0", var7);
               if (!var23.equals("0")) {
                  var2.dirty = true;
                  GuiUtil.showMessageDialog((Component)null, "Mentés vége közben hiba lépett fel!\n" + var23, "Üzenet", 0);
                  MainFrame.thisinstance.setGlassLabel((String)null);
                  return 1;
               }

               String var24 = "F";
               if ("N".equals(var7) && var15 && !"3".equals(MainFrame.role)) {
                  Hashtable var25 = this.dbhandler.pfelCsomag(new BigDecimal(var2.getBizt_azon()), var2.getBarcode());
                  if (var25.get("p_ret") != null) {
                     GuiUtil.showMessageDialog((Component)null, var25.get("p_ret"), "Üzenet", 0);
                  } else if (var25.get("p_array") != null) {
                     try {
                        Utalasi_tetelek_dialog.show((String[])((String[])var25.get("p_array")), var2.getBarcode());
                        var24 = Utalasi_tetelek_dialog.ret;
                     } catch (Exception var29) {
                     }
                  }
               }

               MainFrame.revizor_valasza_to_ubevframe = var24;
               String var35 = this.dbhandler.revizoriReszutalasJavitva(new BigDecimal(var2.getBizt_azon()), var24);
               if (var35 != null) {
                  GuiUtil.showMessageDialog((Component)null, "Revizori részutalás közben hiba lépett fel!\n" + var35, "Üzenet", 0);
                  MainFrame.thisinstance.setGlassLabel("");
                  return 1;
               }

               MainFrame.toubevframe_wassave = true;
               GuiUtil.showMessageDialog((Component)null, "Mentés sikeresen befejeződőtt!", "Üzenet", 1);
            } catch (Exception var31) {
               GuiUtil.showMessageDialog((Component)null, "Mentés közben hiba lépett fel!\nHibakód: " + var31.toString(), "Üzenet", 0);
               this.exception2SOut(var31);
            }

            MainFrame.thisinstance.setGlassLabel((String)null);
         }

         return 0;
      }
   }

   private boolean save(BookModel var1, String var2, String var3, Vector var4) {
      if (var1.cc.deletedparams != null && var1.cc.deletedparams.size() != 0) {
         String var5 = this.dbhandler.handleDelPages(var1.cc.deletedparams);
         if (var5 != null) {
            GuiUtil.showMessageDialog((Component)null, "Törlés közben hiba lépett fel!\n" + var5, "Üzenet", 0);
            return false;
         }
      }

      Vector var23 = new Vector();

      for(int var6 = 0; var6 < var1.cc.size(); ++var6) {
         Elem var7 = (Elem)var1.cc.get(var6);
         var23.clear();

         int var8;
         label102:
         for(var8 = 0; var8 < var4.size(); ++var8) {
            if (((TextWithIcon)var4.get(var8)).text.startsWith(" > ") && ((TextWithIcon)var4.get(var8)).text.indexOf(var7.toString()) != -1) {
               for(int var9 = var8 + 1; var9 < var4.size(); ++var9) {
                  if (((TextWithIcon)var4.get(var9)).ii == null) {
                     break label102;
                  }

                  var23.add(var4.get(var9));
               }
            }
         }

         if (var6 == 0) {
            for(var8 = 0; var8 < var4.size() && ((TextWithIcon)var4.get(var8)).ii != null; ++var8) {
               var23.add(var4.get(var8));
            }
         }

         GUI_Datastore var24 = (GUI_Datastore)var7.getRef();
         Hashtable var25 = var7.getEtc();
         Datastore_history var10 = (Datastore_history)var25.get("history");
         Hashtable var11 = var24.getChangedValues();
         var24.putExtraValues(var11, var10);
         Hashtable var12 = new Hashtable();
         Enumeration var13 = var11.keys();

         while(var13.hasMoreElements()) {
            String var14 = (String)var13.nextElement();
            if (var10 != null) {
               try {
                  Object var15 = var10.get(var14).get(3);
                  var12.put(var14, var15);
               } catch (Exception var21) {
               }
            }
         }

         try {
            String[] var26 = null;

            try {
               var26 = (String[])((String[])var7.getEtc().get("dbparams"));
            } catch (Exception var20) {
            }

            Vector var27 = new Vector();
            Hashtable var17 = new Hashtable();

            try {
               String var18 = HeadChecker.getInstance().getAlbizIdFid(var7.getType(), var1);
               var17.put("albizid", var18);
            } catch (Exception var19) {
            }

            if (var6 == var1.get_main_index()) {
               var17.put("main_document", "");
            }

            int var16;
            if (MainFrame.rogzitomode) {
               var16 = this.dbhandler.editDataR(var26, var3, var2, var7.getType(), var11, var12, var27, var17);
            } else if ("5".equals(var2) && this.dbhandler.isEmptyBimoFidTable()) {
               System.out.println("Itt nem lenne szabad adatokat menteni!");
               System.out.println("----------");
               this.logValues(var11);
               System.out.println("----------");
               var16 = this.dbhandler.editData(var26, var3, var2, var7.getType(), new Hashtable(), var12, var23, var27, var17);
            } else {
               if (MainFrame.opmode != null && !MainFrame.opmode.equals("0")) {
                  System.out.println("RDbSaver calling - db_editData");
               }

               var16 = this.dbhandler.editData(var26, var3, var2, var7.getType(), var11, var12, var23, var27, var17);
            }

            if (var16 != 0) {
               if (var27.size() == 0) {
                  GuiUtil.showMessageDialog((Component)null, "Mentés közben hiba lépett fel!\nHibakód: " + var16, "Üzenet", 0);
               } else {
                  GuiUtil.showErrorDialog((Frame)null, "Hibalista", true, false, var27);
               }

               return false;
            }
         } catch (Exception var22) {
            this.exception2SOut(var22);
            return false;
         }
      }

      return true;
   }

   private int savefixing(BookModel var1, String var2, String var3) {
      String var4 = "IGEN";
      String[] var5 = new String[]{"Igen", "Nem", "Mégsem"};
      int var6 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Később folytatja a nyomtatvány rögzítését?", "Kérdés", 1, 3, (Icon)null, var5, var5[0]);
      if (var6 == 2) {
         return 1;
      } else {
         if (var6 == 1) {
            var4 = "NEM";
         }

         boolean var7 = this.save(var1, var2, var3, new Vector());
         if (!var7) {
            GuiUtil.showMessageDialog((Component)null, "Mentés közben súlyos hiba lépett fel!\nHibakód: -1", "Üzenet", 0);
            return 1;
         } else {
            var1.dirty = false;
            BigDecimal var8 = null;

            try {
               var8 = new BigDecimal(var1.getBizt_azon());
            } catch (Exception var22) {
            }

            new BigDecimal(0);
            BigDecimal var10 = new BigDecimal(0);
            BigDecimal var11 = new BigDecimal(0);
            BigDecimal var12 = new BigDecimal(0);
            BigDecimal var13 = new BigDecimal(0);
            BigDecimal var14 = new BigDecimal(0);
            BigDecimal var9 = new BigDecimal(var1.cc.size());

            for(int var15 = 0; var15 < var1.cc.size(); ++var15) {
               Elem var16 = (Elem)var1.cc.get(var15);
               GUI_Datastore var17 = (GUI_Datastore)var16.getRef();
               Hashtable var18 = var17.getStat(var16.getType());
               BigDecimal var19 = (BigDecimal)var18.get("pagenum");
               BigDecimal var20 = (BigDecimal)var18.get("fieldnum");
               BigDecimal var21 = (BigDecimal)var18.get("totalcharnum");
               var10 = var10.add(var19);
               var11 = var11.add(var20);
               var13 = var13.add(var21);
            }

            System.out.println("bizonylatDb=" + var9);
            System.out.println("lapDb=" + var10);
            System.out.println("nMezoDb=" + var11);
            System.out.println("kMezoDb=" + var12);
            System.out.println("nKarDb=" + var13);
            System.out.println("kKarDb=" + var14);
            if (var6 == 0) {
               var9 = new BigDecimal(0);
               var10 = new BigDecimal(0);
               var11 = new BigDecimal(0);
               var12 = new BigDecimal(0);
               var13 = new BigDecimal(0);
               var14 = new BigDecimal(0);
            }

            String var23 = this.dbhandler.rogzitve(var1.getBarcode(), var8, var4, var9, var10, var11, var12, var13, var14);
            if (var23 != null) {
               GuiUtil.showMessageDialog((Component)null, var23, "Üzenet", 0);
               return 1;
            } else {
               MainFrame.toubevframe_wassave = true;
               GuiUtil.showMessageDialog((Component)null, "Mentés sikeresen befejeződőtt!", "Üzenet", 1);
               return 0;
            }
         }
      }
   }

   public void exception2SOut(Exception var1) {
      System.out.println("------- Exception:");

      try {
         System.out.println(var1.getMessage());
         StackTraceElement[] var2 = var1.getStackTrace();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            StackTraceElement var4 = var2[var3];
            System.out.print(" >  ");
            System.out.println(var4);
         }
      } catch (Exception var5) {
         System.out.println("Az exception nem írható ki (ex)!");
      } catch (Error var6) {
         System.out.println("Az exception nem írható ki (err)!");
      }

      System.out.println("-------");
   }

   private void generateBtablaMindigKihatasRec(BookModel var1) {
      try {
         for(int var2 = 0; var2 < var1.cc.size(); ++var2) {
            Elem var3 = (Elem)var1.cc.get(var2);
            IDataStore var4 = (IDataStore)var3.getRef();
            Iterator var5 = var4.getCaseIdIterator();

            while(var5.hasNext()) {
               StoreItem var6 = (StoreItem)var5.next();
               DataFieldModel var7 = (DataFieldModel)var1.get(var3.getType()).fids.get(var6.code);
               if ("mindig".equals(var7.features.get("btable"))) {
                  try {
                     String[] var8 = (String[])((String[])var3.getEtc().get("dbparams"));
                     String var9 = var8[1];
                     Hashtable var10 = (Hashtable)var1.cc.all_kihatas_ht.get(new BigDecimal(var9));
                     String var11 = var6.index + "_" + var6.code;
                     DefaultTableModel var12 = (DefaultTableModel)var10.get(var11);
                     if (var12 == null) {
                        var12 = new DefaultTableModel(0, 11);
                        var10.put(var11, var12);
                     }

                     if (var12.getRowCount() == 0) {
                        Vector var13 = new Vector();

                        for(int var14 = 0; var14 < 11; ++var14) {
                           var13.add("");
                        }

                        var12.addRow(var13);
                        byte var22 = 0;
                        String var15 = var3.getType();
                        int var16 = var6.index;
                        String var17 = "";
                        String var18 = var6.code;
                        String var19 = var7.getAdonem(var4, var16);
                        var12.setValueAt(var17, var22, 8);
                        var12.setValueAt(var15 + "@" + var18, var22, 0);
                        var12.setValueAt(var16 + "", var22, 6);
                        var12.setValueAt(var19, var22, 2);
                     }
                  } catch (Exception var20) {
                     var20.printStackTrace();
                  }
               }
            }
         }
      } catch (Exception var21) {
         var21.printStackTrace();
      }

   }

   private void logValues(Hashtable var1) {
      Enumeration var2 = var1.keys();

      while(var2.hasMoreElements()) {
         Object var3 = var2.nextElement();
         System.out.println(var3 + " : " + var1.get(var3));
      }

   }
}
