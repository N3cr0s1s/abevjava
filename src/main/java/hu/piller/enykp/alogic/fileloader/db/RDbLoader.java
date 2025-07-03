package hu.piller.enykp.alogic.fileloader.db;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.filesaver.xml.ErrorListListener4XmlSave;
import hu.piller.enykp.alogic.fileutil.DataChecker;
import hu.piller.enykp.alogic.fileutil.HeadChecker;
import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.settingspanel.BaseSettingsPane;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.datastore.Datastore_history;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.GUI_Datastore;
import hu.piller.enykp.datastore.Kihatasstore;
import hu.piller.enykp.extensions.db.DbFactory;
import hu.piller.enykp.extensions.db.IDbHandler;
import hu.piller.enykp.extensions.elogic.ElogicCaller;
import hu.piller.enykp.extensions.elogic.IELogicResult;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.framework.MainPanel;
import hu.piller.enykp.gui.framework.Menubar;
import hu.piller.enykp.gui.framework.StatusPane;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldFactory;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.gui.viewer.DefaultMultiFormViewer;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IDbConnectQf;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import java.awt.Component;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class RDbLoader implements IDbConnectQf {
   private Hashtable<String, String> forceModifiedKeys = new Hashtable();
   IDbHandler dbhandler;
   BookModel bm;
   IDataStore ds;
   Datastore_history dsh;
   FormModel lc_fm;
   int[] pc;
   boolean first;
   boolean nocachemode = false;
   String curbr;
   String bizt_azon;
   String event_azon;
   String igcode;
   String adozovaljavit;
   ErrorListListener4XmlSave ell4xs;
   String[] dbparams;
   String[] maindbparams;
   public static int alldb = 0;
   public static int db1 = 0;
   public static long begintime = 0L;
   public static long addtime = 0L;
   public static long setvaluetime = 0L;
   public static long checktime = 0L;
   public static long starttime = 0L;
   public static boolean idomeres = false;
   public static int globalstatus;
   boolean voltnyomtatvanyhiba;
   boolean rolechanged;
   public static boolean tesztmode = false;
   public static int ALIVE_SIGNAL_COUNT = 3;
   int asc;
   Vector vprecheck;
   BigDecimal[] d_id;
   private static final String[] VIDS_TO_REMOVE = new String[]{"E_bevallás", "stat_G_terv_vonalkod_torzs", "terv_adoev_torzs", "terv_keszites_datum_torzs", "terv_feldolgozas_datum_torzs"};
   private static final HashSet<String> VID_SET_TO_REMOVE;
   boolean megsem = true;

   public RDbLoader() {
      System.out.println("RDbLoader - constructor");
      this.first = true;
      String var1 = null;

      try {
         IPropertyList var2 = PropertyList.getInstance();
         var1 = (String)((Vector)var2.get("prop.const.alive_signal_count")).get(0);
      } catch (Exception var4) {
         var1 = null;
      }

      try {
         this.asc = Integer.parseInt(var1);
         if (this.asc < 2) {
            this.asc = 2;
         }
      } catch (Exception var3) {
         this.asc = ALIVE_SIGNAL_COUNT;
      }

   }

   public int done() {
      String var1 = JOptionPane.showInputDialog("A nyomtatvány bárkódja:", "6674692941");
      if (var1 == null) {
         return 0;
      } else {
         if (var1.length() == 0) {
            var1 = "6685215807";
         }

         try {
            this.dbhandler = DbFactory.getDbHandler();
            this.curbr = var1;
            int var2 = this.dbhandler.getFieldsFromDb(this, var1, (BigDecimal[])null, 0);
            if (var2 != 0) {
               System.out.println("vége hibával = " + var2);
            }
         } catch (Exception var3) {
            var3.printStackTrace();
         }

         return 0;
      }
   }

   public int done_withgui(String var1) {
      this.forceModifiedKeys.clear();
      if (idomeres) {
         System.out.println("--- " + System.currentTimeMillis());
      }

      if (var1 == null) {
         return 0;
      } else {
         try {
            long var2 = System.nanoTime();
            this.dbhandler = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.DefaultDbHandler");
            this.curbr = var1;
            if (!MainFrame.rogzitomode) {
               this.dbhandler.naplozas(var1);
            }

            this.dbhandler.setDocId(MainFrame.dokumentum_id, this.curbr, MainFrame.elug_azon);
            long var4 = System.nanoTime();
            if (idomeres) {
               System.out.println("TTT idomeres naplozas " + (var4 - var2) / 1000000L);
            }

            var2 = System.nanoTime();
            int var6 = this.dbhandler.getFieldsFromDb(this, var1, (BigDecimal[])null, 0);
            var4 = System.nanoTime();
            if (idomeres) {
               System.out.println("TTT idomeres getFieldsFromDb " + (var4 - var2) / 1000000L);
            }

            if (idomeres) {
               System.out.println("--- " + System.currentTimeMillis());
            }

            if (var6 == -68) {
               try {
                  MainFrame.thisinstance.mp.getDMFV().bm.dirty = false;
               } catch (Exception var9) {
               }

               return 1;
            }

            if (var6 != 0) {
               GuiUtil.showMessageDialog((Component)null, "Betöltés közben hiba lépett fel!\nHibakód: " + var6, "Üzenet", 0);

               try {
                  MainFrame.thisinstance.mp.getDMFV().bm.dirty = false;
               } catch (Exception var8) {
               }

               return 1;
            }
         } catch (Exception var10) {
            var10.printStackTrace();
         }

         if (MainFrame.readonlymodefromubev) {
            MainPanel var10001 = MainFrame.thisinstance.mp;
            MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
         }

         return 0;
      }
   }

   public int done_withgui(String var1, BigDecimal[] var2) {
      this.forceModifiedKeys.clear();
      if (var1 == null) {
         return 0;
      } else {
         try {
            this.dbhandler = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.DefaultDbHandler");
            this.curbr = var1;
            this.d_id = var2;
            MainFrame.isPartOnlyMain = false;
            this.dbhandler.naplozas(var1);
            this.dbhandler.setDocId(MainFrame.dokumentum_id, this.curbr, MainFrame.elug_azon);
            int var3 = this.dbhandler.getFieldsFromDb(this, var1, var2, 0);
            if (var3 == -68) {
               return 1;
            }

            if (var3 != 0) {
               GuiUtil.showMessageDialog((Component)null, "Betöltés közben hiba lépett fel!\nHibakód: " + var3, "Üzenet", 0);
               return 1;
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }

         return 0;
      }
   }

   public int done(String var1) {
      this.forceModifiedKeys.clear();
      db1 = 0;
      begintime = 0L;
      addtime = 0L;
      setvaluetime = 0L;
      checktime = 0L;

      try {
         if (this.dbhandler == null) {
            this.dbhandler = DbFactory.getDbHandler();
         }

         this.curbr = var1;
         long var2 = System.nanoTime();
         int var4 = this.dbhandler.getFieldsFromDb(new RDbLoader.MyIDbConnectQf(), var1, (BigDecimal[])null, 0);
         long var5 = System.nanoTime();
         if (var4 != 0) {
            return -1;
         }

         if (!this.bm.hasError) {
            try {
               var2 = System.nanoTime();
               this.dbhandler.startFieldsFromDb((BigDecimal)null);
               var5 = System.nanoTime();
               var2 = System.nanoTime();
               Vector var7 = Tools.check();
               System.out.println("error count=" + var7.size());
               var5 = System.nanoTime();
               checktime = var5 - var2;
            } catch (Exception var12) {
               var12.printStackTrace();

               try {
                  if (this.bm != null) {
                     this.bm.destroy();
                  }

                  try {
                     this.dbhandler.stopFieldsFromDb(this.bizt_azon, this.curbr, globalstatus != 0 ? "1" : "0", 0);
                  } catch (Exception var10) {
                     Tools.eLog(var10, 0);
                  }
               } catch (Exception var11) {
                  Tools.eLog(var11, 0);
               }
            }
         } else {
            this.bm.destroy();

            try {
               this.dbhandler.stopFieldsFromDb(this.bizt_azon, this.curbr, globalstatus != 0 ? "1" : "0", 0);
            } catch (Exception var9) {
               Tools.eLog(var9, 0);
            }
         }
      } catch (Exception var13) {
         Tools.eLog(var13, 0);
      }

      return 0;
   }

   public int done2(String var1) {
      this.forceModifiedKeys.clear();
      db1 = 0;
      begintime = 0L;
      addtime = 0L;
      setvaluetime = 0L;
      checktime = 0L;

      try {
         if (this.dbhandler == null) {
            this.dbhandler = DbFactory.getDbHandler();
         }

         this.curbr = var1;
         long var2 = System.nanoTime();
         long var4 = System.nanoTime();
         System.out.println("getFieldsFromDb " + (var4 - var2) / 1000000L);
         if (!this.bm.hasError) {
            try {
               var2 = System.nanoTime();
               Vector var6 = Tools.check();
               System.out.println("error count=" + var6.size());
               var4 = System.nanoTime();
               checktime = var4 - var2;
            } catch (Exception var10) {
               var10.printStackTrace();

               try {
                  if (this.bm != null) {
                     this.bm.destroy();
                  }
               } catch (Exception var9) {
                  Tools.eLog(var9, 0);
               }
            }
         } else {
            this.bm.destroy();

            try {
               this.dbhandler.stopFieldsFromDb(this.bizt_azon, this.curbr, globalstatus != 0 ? "1" : "0", 0);
            } catch (Exception var8) {
               Tools.eLog(var8, 0);
            }
         }
      } catch (Exception var11) {
         Tools.eLog(var11, 0);
      }

      return 0;
   }

   public int done3(String var1) {
      System.out.println("Loading...");
      this.forceModifiedKeys.clear();
      db1 = 0;
      begintime = 0L;
      addtime = 0L;
      setvaluetime = 0L;
      checktime = 0L;

      try {
         if (this.dbhandler == null) {
            this.dbhandler = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.AdvancedDbHandler");
         }

         this.curbr = var1;
         this.nocachemode = true;
         if (tesztmode) {
            this.dbhandler.setTestMode();
         }

         IErrorList var2 = ErrorList.getInstance();
         var2.clear();
         this.ell4xs = new ErrorListListener4XmlSave(-1);
         boolean var3 = false;
         this.ell4xs.clearErrorList();
         ((IEventSupport)var2).addEventListener(this.ell4xs);
         this.first = true;
         byte var4 = 0;
         if (MainFrame.batch_recalc_mode) {
            var4 = 1;
         }

         int var5 = this.dbhandler.getFieldsFromDb(new RDbLoader.MyIDbConnectQf(), var1, (BigDecimal[])null, var4);
         if (this.bm == null) {
            var5 = -10;
         } else if (this.bm.hasError && "A nyomtatványhoz a keretprogram újabb verziója szükséges!".equals(this.bm.errormsg)) {
            IELogicResult var10000 = new IELogicResult() {
               public int getStatus() {
                  return -10;
               }

               public String getMessage() {
                  return "A nyomtatványhoz a keretprogram újabb verziója szükséges!";
               }
            };
            var5 = -10;
         } else {
            IELogicResult var6 = ElogicCaller.eventBatchAfterCheck(this.bm);
            if (var5 == 0 && var6.getStatus() != 0) {
               var5 = var6.getStatus();
            }
         }

         try {
            this.dbhandler.stopFieldsFromDb(this.bizt_azon, this.curbr, this.voltnyomtatvanyhiba ? "1" : "0", var5);
         } catch (Exception var8) {
            Tools.eLog(var8, 0);
         }

         ((IEventSupport)var2).removeEventListener(this.ell4xs);
      } catch (Exception var9) {
         var9.printStackTrace();
      }

      return 0;
   }

   public int done4(String var1) {
      this.forceModifiedKeys.clear();
      db1 = 0;
      begintime = 0L;
      addtime = 0L;
      setvaluetime = 0L;
      checktime = 0L;

      try {
         if (this.dbhandler == null) {
            this.dbhandler = DbFactory.getDbHandler();
         }

         this.curbr = var1;
         this.nocachemode = true;
         IErrorList var2 = ErrorList.getInstance();
         this.ell4xs = new ErrorListListener4XmlSave(-1);
         boolean var3 = false;
         this.ell4xs.clearErrorList();
         ((IEventSupport)var2).addEventListener(this.ell4xs);
         this.first = true;
         long var4 = System.nanoTime();
         int var6 = this.dbhandler.getFieldsFromDb(new RDbLoader.MyIDbConnectQf2(), var1, (BigDecimal[])null, 0);
         long var7 = System.nanoTime();
         if (var6 != 0) {
            System.out.println("itt vagyunk");
         }

         ((IEventSupport)var2).removeEventListener(this.ell4xs);
         Vector var9 = this.ell4xs.getErrorList();
         if (var9.size() > 0) {
            for(int var10 = 0; var10 < var9.size() && !var3; ++var10) {
               TextWithIcon var11 = (TextWithIcon)var9.get(var10);
               var3 = var11.ii != null;
            }
         }

         if (!var3) {
            var9.clear();
            var9.add("A ellenőrzés nem talált hibát ");
         }

         System.out.println("errorcount=" + var9.size());
      } catch (Exception var12) {
         Tools.eLog(var12, 0);
      }

      return 0;
   }

   public int[] begin(String var1, String[] var2) {
      this.forceModifiedKeys.clear();
      if (idomeres) {
         System.out.println("---begin " + System.currentTimeMillis());
      }

      DefaultMultiFormViewer var3 = MainFrame.thisinstance.mp.getDMFV();
      String var5 = "";
      Object var6 = null;

      try {
         this.bizt_azon = var2[4];
      } catch (Exception var19) {
         this.bizt_azon = null;
      }

      try {
         this.event_azon = var2[5];
      } catch (Exception var18) {
         this.event_azon = null;
      }

      try {
         this.igcode = var2[6];
      } catch (Exception var17) {
         this.igcode = null;
      }

      long var7 = System.nanoTime();
      final File var9 = TemplateChecker.getInstance().getTemplateFilenameWithDialog(var1, var5, (String)var6).getTemplateFile();
      long var10 = System.nanoTime();
      if (idomeres) {
         System.out.println("TTT idomeres TemplateChecker " + (var10 - var7) / 1000000L);
      }

      if (var9 == null) {
         return new int[]{-68, 0};
      } else if (!var9.exists()) {
         return new int[]{-67};
      } else {
         BookModel var12 = var3 != null ? var3.bm : null;
         boolean var13 = false;
         if (var3 != null) {
            if (var3.bm.loadedfile != null && var9 != null) {
               if (!var3.bm.loadedfile.getAbsolutePath().equals(var9.getAbsolutePath())) {
                  var3.bm.destroy();
               } else {
                  var12.reempty();
                  var13 = true;
               }
            } else {
               var3.bm.destroy();
            }

            MainFrame.thisinstance.mp.intoleftside(new JPanel());
            var3.bm = null;
            DataFieldFactory.getInstance().freemem();
            MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
            MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
            MainPanel var10001 = MainFrame.thisinstance.mp;
            MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
            MainFrame.thisinstance.mp.readonlymode = false;
            MainFrame.thisinstance.mp.forceDisabledPageShowing = false;
            if (MainFrame.readonlymodefromubev) {
               MainFrame.thisinstance.mp.readonlymode = true;
            }

            MainFrame.thisinstance.mp.funcreadonly = false;
            MainFrame.thisinstance.mp.set_kiut_url((String)null);
         } else if (MainFrame.db_prev_bm != null) {
            if (MainFrame.db_prev_bm.loadedfile != null && var9 != null) {
               if (!MainFrame.db_prev_bm.loadedfile.getAbsolutePath().equals(var9.getAbsolutePath())) {
                  MainFrame.db_prev_bm.destroy();
                  MainFrame.db_prev_bm = null;
               } else {
                  var12 = MainFrame.db_prev_bm;
                  var13 = true;
               }
            } else {
               MainFrame.db_prev_bm.destroy();
               MainFrame.db_prev_bm = null;
            }
         }

         MainFrame.thisinstance.setGlassLabel("Adatbázisból betöltés folyamatban!");
         boolean finalVar1 = var13;
         BookModel finalVar2 = var12;
         Thread var16 = new Thread(new Runnable() {
            public void run() {
               if (RDbLoader.idomeres) {
                  System.out.println("---szál be " + System.currentTimeMillis());
               }

               boolean var1 = false;
               boolean var2 = false;
               String var3 = "Adatbázisból betöltve";
               if (var9 != null) {
                  long var4 = System.nanoTime();
                  if (finalVar1) {
                     RDbLoader.this.bm = finalVar2;
                     RDbLoader.this.bm.reempty();
                  } else {
                     RDbLoader.this.bm = new BookModel(var9);
                  }

                  long var6 = System.nanoTime();
                  if (RDbLoader.idomeres) {
                     System.out.println("TTT idomeres new Bookmodel or reempty " + (var6 - var4) / 1000000L);
                  }

                  RDbLoader.this.bm.setBarcode(RDbLoader.this.curbr);
                  RDbLoader.this.bm.setIgazgatosagi_kod(RDbLoader.this.igcode);
                  RDbLoader.this.bm.from_rdb = true;
                  MainPanel var10001;
                  if (RDbLoader.this.bm.hasError) {
                     RDbLoader.this.bm.destroy();
                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);

                     try {
                        RDbLoader.this.dbhandler.stopFieldsFromDb(RDbLoader.this.bizt_azon, RDbLoader.this.curbr, RDbLoader.globalstatus != 0 ? "1" : "0", 0);
                     } catch (Exception var15) {
                        Tools.eLog(var15, 0);
                     }
                  } else {
                     MainFrame.db_prev_bm = RDbLoader.this.bm;
                     RDbLoader.this.bm.setBarcode(RDbLoader.this.curbr);
                     RDbLoader.this.bm.setBizt_azon(RDbLoader.this.bizt_azon);
                     RDbLoader.this.bm.setEvent_azon(RDbLoader.this.event_azon);
                     var4 = System.nanoTime();
                     DefaultMultiFormViewer var8 = new DefaultMultiFormViewer(RDbLoader.this.bm);
                     var6 = System.nanoTime();
                     if (RDbLoader.idomeres) {
                        System.out.println("TTT idomeres new DefaultMultiFormViewer " + (var6 - var4) / 1000000L);
                     }

                     if (var8.fv.getTp().getTabCount() == 0) {
                        RDbLoader.this.bm.destroy();
                        var10001 = MainFrame.thisinstance.mp;
                        MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                        MainFrame.thisinstance.setGlassLabel((String)null);
                        MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                        MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                        MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                        return;
                     }

                     MainFrame.thisinstance.mp.intoleftside(var8);
                     MainFrame.thisinstance.mp.funcreadonly = var1;
                     if (!var2 && !MainFrame.readonlymodefromubev) {
                        var10001 = MainFrame.thisinstance.mp;
                        MainFrame.thisinstance.mp.setstate(MainPanel.NORMAL);
                     } else {
                        var10001 = MainFrame.thisinstance.mp;
                        MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
                     }

                     if (MainFrame.readonlymodefromubev) {
                        MainFrame.thisinstance.mp.readonlymode = true;
                     }

                     MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var3);
                     MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("Ny:" + (String)RDbLoader.this.bm.docinfo.get("ver"));

                     try {
                        long var9x = System.nanoTime();
                        int var11 = RDbLoader.this.dbhandler.startFieldsFromDb((BigDecimal)null);
                        long var12x = System.nanoTime();
                        if (RDbLoader.idomeres) {
                           System.out.println("TTT idomeres startFieldsFromDb " + (var12x - var9x) / 1000000L);
                        }

                        if (var11 != 0) {
                           GuiUtil.showMessageDialog((Component)null, "Betöltés közben hiba lépett fel!\nHibakód: " + var11, "Üzenet", 0);
                           throw new Exception();
                        }

                        var9x = System.nanoTime();
                        var8.fv.setTabStatus();
                        var8.refreshdatacb();
                        var8.fv.pv.refresh();
                        var12x = System.nanoTime();
                        if (RDbLoader.idomeres) {
                           System.out.println("TTT idomeres refresh " + (var12x - var9x) / 1000000L);
                        }

                        MainFrame.datastorehistorylive = true;

                        try {
                           if (MainFrame.isPart && 1 < RDbLoader.this.bm.cc.size()) {
                              Elem var14 = (Elem)RDbLoader.this.bm.cc.get(RDbLoader.this.bm.cc.size() - 1);
                              GuiUtil.showelem(var14);
                           }
                        } catch (Exception var17) {
                           Tools.eLog(var17, 0);
                        }

                        if (MainFrame.isPart && MainFrame.isPartOnlyMain) {
                           BaseSettingsPane.calchelper(false, RDbLoader.this.bm);
                           SettingsStore var19 = SettingsStore.getInstance();
                           var19.set("gui", "mezőszámítás", "false");
                        }
                     } catch (Exception var18) {
                        var18.printStackTrace();

                        try {
                           RDbLoader.this.dbhandler.stopFieldsFromDb(RDbLoader.this.bizt_azon, RDbLoader.this.curbr, RDbLoader.globalstatus != 0 ? "1" : "0", 0);
                           if (RDbLoader.this.bm != null) {
                              RDbLoader.this.bm.destroy();
                           }

                           if (var8 != null) {
                              var8.destroy();
                           }

                           MainFrame.db_prev_bm = null;
                           MainFrame.thisinstance.mp.intoleftside(new JPanel());
                           DataFieldFactory.getInstance().freemem();
                           MainFrame.thisinstance.mp.set_kiut_url((String)null);
                           var10001 = MainFrame.thisinstance.mp;
                           MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                           MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                           MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                           BaseSettingsPane.done_menuextratext(true);
                           MainFrame.thisinstance.mp.readonlymode = false;
                           if (MainFrame.readonlymodefromubev) {
                              MainFrame.thisinstance.mp.readonlymode = true;
                           }

                           MainFrame.thisinstance.mp.funcreadonly = false;
                           StatusPane.thisinstance.currentpagename.setText("");
                        } catch (Exception var16) {
                           Tools.eLog(var18, 0);
                        }
                     }
                  }

                  MainFrame.thisinstance.setGlassLabel((String)null);
                  if (RDbLoader.idomeres) {
                     System.out.println("---szál ki " + System.currentTimeMillis());
                  }
               }

            }
         });
         var16.start();
         if (idomeres) {
            System.out.println("---begin ki " + System.currentTimeMillis());
         }

         return new int[]{0, var13 ? 1 : 0};
      }
   }

   public int add(String var1, String[] var2) {
      if (idomeres) {
         System.out.println("---add " + System.currentTimeMillis());
      }

      try {
         if (!this.first) {
            try {
               CalculatorManager.getInstance().doBetoltErtekCalcs(true);
               CalculatorManager.getInstance().form_hidden_fields_calc();
               CalculatorManager.getInstance().form_notbelfeld_fields_calc();
               CalculatorManager.getInstance().multi_form_load();
               ((GUI_Datastore)this.ds).setSavepoint(this.bm);
            } catch (Exception var5) {
               Tools.eLog(var5, 0);
            }

            FormModel var3 = this.bm.get(var1);
            if (var3 == null) {
               return 100;
            }

            this.bm.addForm_noPA(var3);
         }

         this.first = false;
         Elem var7 = (Elem)this.bm.cc.getActiveObject();
         this.lc_fm = this.bm.get(var7.getType());
         this.pc = (int[])((int[])var7.getEtc().get("pagecounts"));
         this.ds = (IDataStore)var7.getRef();
         this.dsh = (Datastore_history)var7.getEtc().get("history");
         if (this.dsh == null) {
            this.dsh = new Datastore_history();
            var7.getEtc().put("history", this.dsh);
         }

         Kihatasstore var4 = (Kihatasstore)var7.getEtc().get("kihatas");
         if (var4 == null) {
            var4 = new Kihatasstore(this.bm.cc, new Integer(this.bm.cc.size()), new BigDecimal(var2[1]));
            var7.getEtc().put("kihatas", var4);
         }

         if (var2 != null) {
            var7.getEtc().put("dbparams", var2);
         }
      } catch (Exception var6) {
         return 13;
      }

      MainFrame.thisinstance.setGlassLabel("Adatbázisból betöltés folyamatban! (" + this.bm.cc.size() + ")");
      if (idomeres) {
         System.out.println("---add ki " + System.currentTimeMillis());
      }

      return 0;
   }

   public int setvalue(String[] var1, Object var2) {
      int var3 = this.getPageCount(var1[5]);
      String var4 = var3 + "_" + var1[0];
      int var5 = 0;
      if (var1[1] != null) {
         try {
            var5 = Integer.parseInt(var1[1]);
         } catch (NumberFormatException var12) {
            var5 = 0;
         }
      }

      String var6 = var1[4];
      if (var6 == null && var5 < 3) {
         var6 = var1[3];
         if (var6 == null && var5 < 1) {
            var6 = var1[2];
         }
      }

      boolean var7 = false;
      if (var6 == null) {
         var6 = "";
      }

      try {
         String var8 = (String)((DataFieldModel)this.bm.get(this.lc_fm.id).fids.get(var1[0])).features.get("vid");
         if (var8 != null && this.needToDropByVidCheck(var8)) {
            var7 = true;
            this.forceModifiedKeys.put(var4, var6);
         }
      } catch (Exception var11) {
      }

      Vector var16 = new Vector();
      if (var7 && ("1".equals(MainFrame.role) || "2".equals(MainFrame.role))) {
         var6 = "";
         if ("1".equals(MainFrame.role)) {
            var1[3] = "";
         } else {
            var1[4] = "";
         }
      }

      try {
         if (var1[2] == null && var5 == 1) {
            var16.add("");
         } else {
            var16.add(var1[2]);
         }
      } catch (Exception var15) {
         var16.add((Object)null);
      }

      try {
         if (var1[3] == null && var5 == 2) {
            var16.add("");
         } else {
            var16.add(var1[3]);
         }
      } catch (Exception var14) {
         var16.add((Object)null);
      }

      try {
         if (var1[4] == null && var5 == 4) {
            var16.add("");
         } else {
            var16.add(var1[4]);
         }
      } catch (Exception var13) {
         var16.add((Object)null);
      }

      var16.add(var2);
      this.dsh.set(var4, var16);
      this.dsh.sete(var4, var16);
      int var9 = this.lc_fm.get((PageModel)this.lc_fm.fids_page.get(var1[0]));
      if (var9 == -1) {
         System.out.println("hibás adat: " + this.lc_fm.id + " " + var1[0]);
         return 0;
      } else {
         if (this.pc[var9] < var3 + 1) {
            this.pc[var9] = var3 + 1;
         }

         boolean var10 = ((DataFieldModel)this.bm.get(this.lc_fm.id).fids.get(var1[0])).features.get("datatype").equals("check");
         if (var10) {
            if (!var6.equals("X") && !var6.equals("x")) {
               if (var6.equals("")) {
                  var6 = "false";
               }
            } else {
               var6 = "true";
            }
         }

         this.ds.set(var4, var6);
         return 0;
      }
   }

   public int partend() {
      this.del_empty_dyn_pages_from_end();
      return 0;
   }

   private void del_empty_dyn_pages_from_end() {
      if (!this.megsem) {
         Hashtable var1 = this.lc_fm.get_short_inv_fields_ht();

         for(int var2 = 0; var2 < this.lc_fm.size(); ++var2) {
            PageModel var3 = this.lc_fm.get(var2);
            if (var3.dynamic) {
               Vector var4 = var3.y_sorted_df;

               while(true) {
                  int var6 = this.pc[var2];
                  if (var6 < 2) {
                     break;
                  }

                  Object[] var7 = new Object[]{new Integer(var6 - 1), null};
                  boolean var5 = true;

                  for(int var8 = 0; var8 < var4.size(); ++var8) {
                     var7[1] = ((DataFieldModel)var4.get(var8)).key;
                     String var9 = this.ds.get(var7);
                     if (var9 != null && !var9.equals("") && !var1.containsKey(var7[1])) {
                        var5 = false;
                        break;
                     }
                  }

                  if (!var5) {
                     break;
                  }

                  this.pc[var2] = var6 - 1;
               }
            }
         }

      }
   }

   private int getPageCount(String var1) {
      try {
         int var2 = Integer.parseInt(var1) - 1;
         if (var2 < 0) {
            var2 = 0;
         }

         return var2;
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
         return 0;
      }
   }

   private boolean needToDropByVidCheck(String var1) {
      return false;
   }

   public int end() {
      if (idomeres) {
         System.out.println("---end " + System.currentTimeMillis());
      }

      try {
         this.del_empty_dyn_pages_from_end();
         ((GUI_Datastore)this.ds).setSavepoint(this.bm);
         boolean var1 = CalculatorManager.getInstance().doBetoltErtekCalcs(true);
         if (var1 && Calculator.getInstance().isInJavkeretOpMode()) {
            CalculatorManager.getInstance().form_hidden_fields_calc();
            CalculatorManager.getInstance().form_notbelfeld_fields_calc();
            CalculatorManager.getInstance().multi_form_load();
            System.out.println("RDbLoader.end: Javkeret, betölt érték módosulása miatt újraszámítás");
            CalculatorManager.getInstance().form_calc();
         } else {
            CalculatorManager.getInstance().form_hidden_fields_calc();
            CalculatorManager.getInstance().form_notbelfeld_fields_calc();
            CalculatorManager.getInstance().multi_form_load();
            ((GUI_Datastore)this.ds).setSavepoint(this.bm);
         }
      } catch (Exception var8) {
         Tools.eLog(var8, 0);
      }

      this.bm.setCalcelemindex(0);
      this.bm.cc.setActiveObject(this.bm.cc.get(0));
      Result var9 = DataChecker.getInstance().superCheck(this.bm, false, -1);
      if (!var9.isOk()) {
         new ErrorDialog(MainFrame.thisinstance, "Adatok formai ellenőrzésének hibalistája", true, true, var9.errorList);
      }

      IELogicResult var2 = ElogicCaller.eventAfterDataLoad(this.bm);
      if (var2.getStatus() != 0) {
         GuiUtil.showMessageDialog((Component)null, var2.getMessage(), "Üzenet", 0);
         return var2.getStatus();
      } else {
         if (this.bm.cc.size() == 1) {
            MainFrame.isPartOnlyMain = true;
         }

         if (MainFrame.dokumentum_id != null) {
            Vector var3 = this.dbhandler.getAdonemek();
            this.bm.cc.adonemek = var3;
            Hashtable var4 = this.dbhandler.getKihatasok();
            if (var4 == null) {
               GuiUtil.showMessageDialog((Component)null, "A kihatások lekérdezése közben hiba lépett fel.", "Hibaüzenet", 0);
               return 2;
            }

            this.bm.cc.setAll_kihatas_ht(var4);
            Object var5 = this.dbhandler.checkKihatas(this.bm);
            if (var5 != null) {
               try {
                  new ErrorDialog((Frame)null, "Hibalista", true, false, (Vector)var5);
               } catch (HeadlessException var7) {
               }
            }
         }

         if (MainFrame.rogzitomode) {
            Menubar.thisinstance.setcalccmd.execute();
         }

         MainFrame.thisinstance.setGlassLabel((String)null);
         if (idomeres) {
            System.out.println("---end ki " + System.currentTimeMillis());
         }

         return 0;
      }
   }

   public boolean hasEndSignal() {
      try {
         return !MainFrame.runfile.exists();
      } catch (Exception var2) {
         return false;
      }
   }

   public boolean isTestMode() {
      return MainFrame.ubevtesztmode;
   }

   public void doneloop() {
      this.forceModifiedKeys.clear();
      String var1 = System.getProperty("java.version");
      System.out.println("Java verzió=" + var1);
      System.out.println("Verzió:3.44.0");
      long var2 = Runtime.getRuntime().freeMemory();
      System.out.println("Total memory=" + Runtime.getRuntime().totalMemory() + " byte");
      System.out.println("Max memory=" + Runtime.getRuntime().maxMemory() + " byte");
      System.out.println("Free memory=" + var2 + " byte  (" + var2 / 1024L / 1024L + " MB )");
      System.out.println("Processor db=" + Runtime.getRuntime().availableProcessors());
      System.out.println("barcode loop start");
      long var4 = System.nanoTime();
      long var6 = 0L;
      this.rolechanged = false;
      starttime = System.currentTimeMillis();

      while(true) {
         try {
            this.dbhandler = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.AdvancedDbHandler");
            if (tesztmode) {
               this.dbhandler.setTestMode();
            }

            Hashtable var8 = new Hashtable();
            var8.put("TASKTYPE", "GETNEXTBARKOD");
            var8.put("IDbConnectQf", this);
            long var9 = System.currentTimeMillis();
            Hashtable var11 = this.dbhandler.getNextTask(var8);
            long var12 = System.currentTimeMillis();
            String[] var14 = (String[])((String[])var11.get("JABEV_FUNCTIONS_ARRAY"));
            var14 = (String[])((String[])var11.get("JABEV_PARAMS_ARRAY"));
            String var15 = var14[0];

            String var16;
            try {
               var16 = var14[1];
               String var17 = var14[2];
               this.rolechanged = this.setRole(var17);
            } catch (Exception var18) {
               Tools.eLog(var18, 0);
            }

            MainFrame.batch_recalc_mode = false;
            MainFrame.batch_recalc_role = null;
            var16 = (String)var11.get("BATCH_RECALC");
            if (var16 != null && var16.length() != 0) {
               MainFrame.batch_recalc_role = var16;
               MainFrame.batch_recalc_mode = true;
            }

            var9 = System.nanoTime();
            this.done3(var15);
            var12 = System.nanoTime();
            System.out.println("br=" + var15 + " db=" + db1 + " -- " + (var12 - var9) / 1000000L + " ms  / " + begintime / 1000000L + " , " + addtime / 1000000L + " , " + setvaluetime / 1000000L + " , " + checktime / 1000000L + " = " + (begintime + addtime + setvaluetime + checktime) / 1000000L);
         } catch (Exception var19) {
            var19.printStackTrace();
         }

         ++var6;
      }
   }

   private boolean setRole(String var1) throws Exception {
      String var2;
      if (var1.equals("0")) {
         var2 = "1";
      } else if (var1.equals("1")) {
         var2 = "1";
      } else if (var1.equals("2")) {
         var2 = "2";
      } else {
         if (!var1.equals("10")) {
            throw new Exception("Siketelen állapotkód beállítás! (Nem létező állapotkód.)");
         }

         var2 = "3";
      }

      boolean var3 = !var2.equals(MainFrame.role);
      MainFrame.role = var2;
      MainFrame.veszprole = var1;
      return var3;
   }

   private void errlistsave2db() {
      if (this.ell4xs.getRealErrorExtra() != 0) {
         this.voltnyomtatvanyhiba = true;
      }

      Vector var1 = this.ell4xs.getErrorListForDBBatch();
      Vector var2 = new Vector(var1);
      this.ell4xs.clearErrorList();
      if (var2.size() != 0) {
         try {
            this.cleanErrorList(var2);
         } catch (Exception var4) {
            Tools.eLog(var4, 0);
         }
      }

      this.dbhandler.saveErrorList(var2, this.dbparams);
   }

   public void service_run(String var1) {
      this.dbhandler = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.AdvancedDbHandler");
      ElogicCaller.service(var1);
   }

   private boolean save(BookModel var1, String var2, String var3, int var4) {
      Vector var5 = new Vector();
      Elem var6 = (Elem)var1.cc.get(var4);
      GUI_Datastore var7 = (GUI_Datastore)var6.getRef();
      Hashtable var8 = var6.getEtc();
      Datastore_history var9 = (Datastore_history)var8.get("history");
      Hashtable var10 = var7.getChangedValues();
      var7.putExtraValues(var10, var9);
      Hashtable var11 = new Hashtable();
      Enumeration var12 = var10.keys();

      while(var12.hasMoreElements()) {
         String var13 = (String)var12.nextElement();
         if (var9 != null) {
            try {
               Object var14 = var9.get(var13).get(3);
               var11.put(var13, var14);
            } catch (Exception var21) {
            }
         }
      }

      try {
         String[] var22 = null;

         try {
            var22 = (String[])((String[])var6.getEtc().get("dbparams"));
         } catch (Exception var19) {
         }

         Vector var23 = new Vector();
         Hashtable var16 = new Hashtable();

         try {
            String var17 = HeadChecker.getInstance().getAlbizIdFid(var6.getType(), var1);
            var16.put("albizid", var17);
         } catch (Exception var18) {
         }

         if (var4 == var1.get_main_index()) {
            var16.put("main_document", "");
         }

         if (MainFrame.opmode != null && !MainFrame.opmode.equals("0")) {
            System.out.println("RDbLoader calling - db_editData");
         }

         int var15 = this.dbhandler.editData(var22, var3, var2, var6.getType(), var10, var11, var5, var23, var16);
         if (var15 != 0) {
            return false;
         } else {
            return true;
         }
      } catch (Exception var20) {
         var20.printStackTrace();
         return false;
      }
   }

   private void cleanErrorList(Vector var1) {
      for(int var2 = var1.size() - 1; var2 >= 0; --var2) {
         TextWithIcon var3 = (TextWithIcon)var1.elementAt(var2);
         if (var3.ii == null) {
            var1.remove(var2);
         } else {
            for(int var4 = 0; var4 < PropertyList.ERRORLIST_PILL_SPECIAL_ERRORS.length; ++var4) {
               if (PropertyList.ERRORLIST_PILL_SPECIAL_ERRORS[var4].equals(var3.officeErrorCode)) {
                  var1.remove(var2);
               }
            }
         }
      }

   }

   public RDbLoader.MyIDbConnectQf getIdbq() {
      return new RDbLoader.MyIDbConnectQf();
   }

   public void setNocachemode(boolean var1) {
      this.nocachemode = var1;
   }

   public void setCurbr(String var1) {
      this.curbr = var1;
   }

   public void ujEll4xs() {
      this.ell4xs = new ErrorListListener4XmlSave(-1);
      boolean var1 = false;
      this.ell4xs.clearErrorList();
   }

   public boolean isBatch() {
      try {
         Object var1 = PropertyList.getInstance().get("prop.const.opmode");
         if (var1 == null) {
            return false;
         } else if (var1 instanceof Vector) {
            Vector var2 = (Vector)var1;
            return var2.get(0).equals("2");
         } else {
            return false;
         }
      } catch (Exception var3) {
         return false;
      }
   }

   static {
      VID_SET_TO_REMOVE = new HashSet(Arrays.asList(VIDS_TO_REMOVE));
   }

   private class MyIDbConnectQf2 implements IDbConnectQf {
      private MyIDbConnectQf2() {
      }

      public int[] begin(String var1, String[] var2) {
         long var3 = System.nanoTime();
         String var5 = var1;
         String var6 = "";
         Object var7 = null;
         File var8 = null;

         try {
            var8 = new File(TemplateChecker.getInstance().getTemplateFileNames(var5, var6, (String)var7).getTemplateFileNames()[0]);
         } catch (Exception var11) {
            var11.printStackTrace();
         }

         if (var8 == null) {
            return new int[]{1, 0};
         } else {
            if (RDbLoader.this.bm != null && RDbLoader.this.bm.loadedfile.getAbsolutePath().equals(var8.getAbsolutePath())) {
               RDbLoader.this.bm.reempty();
            } else {
               RDbLoader.this.bm = new BookModel(var8);
            }

            RDbLoader.this.bm.setBarcode(RDbLoader.this.curbr);
            if (RDbLoader.this.nocachemode) {
               CalculatorManager.getInstance().do_fly_check_all_start();
            }

            long var9 = System.nanoTime();
            RDbLoader.begintime = var9 - var3;
            return new int[]{0, 0};
         }
      }

      public int add(String var1, String[] var2) {
         long var3 = System.nanoTime();
         ++RDbLoader.db1;
         ++RDbLoader.alldb;
         if (RDbLoader.alldb % 100 == 0) {
            System.out.println("alldb=" + RDbLoader.alldb + "  freemem=" + Runtime.getRuntime().freeMemory() / 1024L / 1024L + " MB");
         }

         FormModel var5 = RDbLoader.this.bm.get(var1);
         if (var5 == null) {
            return 100;
         } else {
            RDbLoader.this.bm.addForm_noPA(var5);
            Elem var6 = (Elem)RDbLoader.this.bm.cc.getActiveObject();
            RDbLoader.this.lc_fm = RDbLoader.this.bm.get(var6.getType());
            RDbLoader.this.pc = (int[])((int[])var6.getEtc().get("pagecounts"));
            RDbLoader.this.ds = (IDataStore)var6.getRef();
            RDbLoader.this.dsh = (Datastore_history)var6.getEtc().get("history");
            if (RDbLoader.this.dsh == null) {
               RDbLoader.this.dsh = new Datastore_history();
               var6.getEtc().put("history", RDbLoader.this.dsh);
            }

            Kihatasstore var7 = (Kihatasstore)var6.getEtc().get("kihatas");
            if (var7 == null) {
               var7 = new Kihatasstore(RDbLoader.this.bm.cc, new Integer(RDbLoader.this.bm.cc.size()), new BigDecimal(var2[1]));
               var6.getEtc().put("kihatas", var7);
            }

            long var8 = System.nanoTime();
            RDbLoader.addtime += var8 - var3;
            return 0;
         }
      }

      public int setvalue(String[] var1, Object var2) {
         long var3 = System.nanoTime();
         int var5 = RDbLoader.this.getPageCount(var1[5]);
         String var6 = var5 + "_" + var1[0];
         int var7 = 0;
         if (var1[1] != null) {
            try {
               var7 = Integer.parseInt(var1[1]);
            } catch (NumberFormatException var18) {
               var7 = 0;
            }
         }

         String var8 = "";
         switch(var7) {
         case 0:
         case 1:
            var8 = var1[2];
            break;
         case 2:
         case 3:
            var8 = var1[3];
            break;
         case 4:
         case 5:
         case 6:
         case 7:
            var8 = var1[4];
         }

         if (var8 == null) {
            var8 = "";
         }

         try {
            String var9 = (String)((DataFieldModel)RDbLoader.this.bm.get(RDbLoader.this.lc_fm.id).fids.get(var1[0])).features.get("vid");
            if (var9 != null && RDbLoader.this.needToDropByVidCheck(var9)) {
               System.out.println("value : " + var8 + " dropped by vid check");
               return 0;
            }
         } catch (Exception var17) {
         }

         Vector var19 = new Vector();

         try {
            var19.add(var1[2]);
         } catch (Exception var16) {
            var19.add("");
         }

         try {
            var19.add(var1[3]);
         } catch (Exception var15) {
            var19.add("");
         }

         try {
            var19.add(var1[4]);
         } catch (Exception var14) {
            var19.add("");
         }

         var19.add(var2);
         RDbLoader.this.dsh.set(var6, var19);
         RDbLoader.this.dsh.sete(var6, var19);
         int var10 = RDbLoader.this.lc_fm.get((PageModel)RDbLoader.this.lc_fm.fids_page.get(var1[0]));
         if (RDbLoader.this.pc[var10] < var5 + 1) {
            RDbLoader.this.pc[var10] = var5 + 1;
         }

         boolean var11 = ((DataFieldModel)RDbLoader.this.bm.get(RDbLoader.this.lc_fm.id).fids.get(var1[0])).features.get("datatype").equals("check");
         if (var11) {
            if (!var8.equals("X") && !var8.equals("x")) {
               if (var8.equals("")) {
                  var8 = "false";
               }
            } else {
               var8 = "true";
            }
         }

         RDbLoader.this.ds.set(var6, var8);
         long var12 = System.nanoTime();
         RDbLoader.setvaluetime += var12 - var3;
         return 0;
      }

      public int partend() {
         try {
            CalculatorManager.getInstance().doBetoltErtekCalcs(true);
            CalculatorManager.getInstance().form_hidden_fields_calc();
            CalculatorManager.getInstance().multi_form_load();
         } catch (Exception var2) {
            Tools.eLog(var2, 0);
         }

         if (RDbLoader.this.nocachemode && !RDbLoader.this.first) {
            CalculatorManager.getInstance().do_fly_check_all_one(RDbLoader.this.ell4xs);
         }

         RDbLoader.this.first = false;
         return 0;
      }

      public int end() {
         RDbLoader.this.bm.setCalcelemindex(0);
         RDbLoader.this.bm.cc.setActiveObject(RDbLoader.this.bm.cc.get(0));
         if (RDbLoader.this.nocachemode) {
            CalculatorManager.getInstance().do_fly_check_all_one(RDbLoader.this.ell4xs);
            CalculatorManager.getInstance().do_fly_check_all_stop();
         }

         return 0;
      }

      public boolean hasEndSignal() {
         try {
            return !MainFrame.runfile.exists();
         } catch (Exception var2) {
            return false;
         }
      }

      public boolean isTestMode() {
         return MainFrame.ubevtesztmode;
      }

      // $FF: synthetic method
      MyIDbConnectQf2(Object var2) {
         this();
      }
   }

   private class MyIDbConnectQf implements IDbConnectQf {
      private MyIDbConnectQf() {
      }

      public int[] begin(String var1, String[] var2) {
         RDbLoader.globalstatus = 0;
         RDbLoader.this.voltnyomtatvanyhiba = false;
         long var3 = System.nanoTime();

         try {
            RDbLoader.this.bizt_azon = var2[4];
         } catch (Exception var22) {
            RDbLoader.this.bizt_azon = null;
         }

         try {
            RDbLoader.this.event_azon = var2[5];
         } catch (Exception var21) {
            RDbLoader.this.event_azon = null;
         }

         try {
            RDbLoader.this.igcode = var2[6];
         } catch (Exception var20) {
            RDbLoader.this.igcode = null;
         }

         try {
            RDbLoader.this.adozovaljavit = var2[7];
         } catch (Exception var19) {
            RDbLoader.this.adozovaljavit = null;
         }

         try {
            TemplateChecker.getInstance().setNeedDialog4Download(var2[8].equals("D"));
         } catch (Exception var18) {
            TemplateChecker.getInstance().setNeedDialog4Download(true);
         }

         String var5 = var1;
         String var6 = "";
         Object var7 = null;
         File var8 = null;
         long var9 = System.nanoTime();

         try {
            String var11 = TemplateChecker.getInstance().getTemplateFileNames(var5, var6, (String)var7).getTemplateFileNames()[0];
            if (var11 == null) {
               System.out.println("Nem található megfelelő nyomtatványsablon: " + var5);
               return new int[]{1, 0};
            }

            if (var11.equals("")) {
               System.out.println("Nem található megfelelő nyomtatványsablon: " + var5);
               return new int[]{2, 0};
            }

            var8 = new File(var11);
         } catch (Exception var17) {
            var17.printStackTrace();
         }

         long var23 = System.nanoTime();
         if (var8 == null) {
            return new int[]{1, 0};
         } else if (!var8.exists()) {
            return new int[]{2, 0};
         } else {
            boolean var13 = false;
            if (RDbLoader.this.bm != null && RDbLoader.this.bm.loadedfile.getAbsolutePath().equals(var8.getAbsolutePath())) {
               if (RDbLoader.this.rolechanged) {
                  System.out.println("Sablonvaltas rolvaltozas miatt!");
                  if (RDbLoader.this.bm != null) {
                     RDbLoader.this.bm.destroy();
                  }

                  RDbLoader.this.bm = new BookModel(var8);
               } else {
                  try {
                     RDbLoader.this.bm.reempty();
                     var13 = true;
                  } catch (Exception var16) {
                     RDbLoader.this.bm.hasError = true;
                     RDbLoader.this.bm.errormsg = "Hibás volt az előző beolvasás! (batch mód)";
                     var13 = false;
                  }
               }
            } else {
               System.out.println("Sablonvaltas!");
               if (RDbLoader.this.bm != null) {
                  RDbLoader.this.bm.destroy();
               }

               RDbLoader.this.bm = new BookModel(var8);
            }

            if (RDbLoader.this.bm.hasError) {
               System.out.println("BookModel error=" + RDbLoader.this.bm.errormsg);
               int var24;
               if (RDbLoader.this.bm.calculator != null) {
                  var24 = RDbLoader.this.bm.calculator.getLastElogicCallerStatus();
               } else {
                  var24 = -10;
               }

               RDbLoader.this.bm.destroy();
               RDbLoader.this.bm = null;
               return var24 != 0 ? new int[]{var24, 0} : new int[]{3, 0};
            } else {
               RDbLoader.this.bm.setAdozovaljavit(RDbLoader.this.adozovaljavit);
               RDbLoader.this.bm.setTesztmode(RDbLoader.tesztmode);
               RDbLoader.this.bm.setBarcode(RDbLoader.this.curbr);
               RDbLoader.this.bm.setBizt_azon(RDbLoader.this.bizt_azon);
               RDbLoader.this.bm.setEvent_azon(RDbLoader.this.event_azon);
               RDbLoader.this.bm.setIgazgatosagi_kod(RDbLoader.this.igcode);
               if (RDbLoader.this.nocachemode) {
                  RDbLoader.this.bm.cc.setNoCacheMode();
               }

               long var14 = System.nanoTime();
               RDbLoader.begintime = var14 - var3;
               return new int[]{0, var13 ? 1 : 0};
            }
         }
      }

      public int add(String var1, String[] var2) {
         long var3 = System.nanoTime();
         ++RDbLoader.db1;
         ++RDbLoader.alldb;
         if (RDbLoader.alldb % 100 == 0) {
            System.out.println("alldb=" + RDbLoader.alldb + "  freemem=" + Runtime.getRuntime().freeMemory() / 1024L / 1024L + " MB    time=" + (System.currentTimeMillis() - RDbLoader.starttime) / 1000L + " sec");
         }

         Elem var5 = (Elem)RDbLoader.this.bm.cc.getActiveObject();
         if (RDbLoader.this.nocachemode && var5 != null && !var5.noMapped()) {
            var5.setMappedObject((Object)null);
            var5.getEtc().clear();
         }

         FormModel var6 = RDbLoader.this.bm.get(var1);
         if (var6 == null) {
            return 100;
         } else {
            RDbLoader.this.bm.addForm_noPA(var6);
            var5 = (Elem)RDbLoader.this.bm.cc.getActiveObject();
            RDbLoader.this.lc_fm = RDbLoader.this.bm.get(var5.getType());
            RDbLoader.this.pc = (int[])((int[])var5.getEtc().get("pagecounts"));
            RDbLoader.this.ds = (IDataStore)var5.getRef();
            RDbLoader.this.dsh = (Datastore_history)var5.getEtc().get("history");
            if (RDbLoader.this.dsh == null) {
               RDbLoader.this.dsh = new Datastore_history();
               var5.getEtc().put("history", RDbLoader.this.dsh);
            }

            Kihatasstore var7 = (Kihatasstore)var5.getEtc().get("kihatas");
            if (var7 == null) {
               var7 = new Kihatasstore(RDbLoader.this.bm.cc, new Integer(RDbLoader.this.bm.cc.size()), new BigDecimal(var2[1]));
               var5.getEtc().put("kihatas", var7);
            }

            if (var2 != null) {
               var5.getEtc().put("dbparams", var2);
            }

            RDbLoader.this.dbparams = var2;
            long var8 = System.nanoTime();
            RDbLoader.addtime += var8 - var3;
            return 0;
         }
      }

      public int setvalue(String[] var1, Object var2) {
         long var3 = System.nanoTime();
         int var5 = RDbLoader.this.getPageCount(var1[5]);
         String var6 = var5 + "_" + var1[0];
         int var7 = 0;
         if (var1[1] != null) {
            try {
               var7 = Integer.parseInt(var1[1]);
            } catch (NumberFormatException var15) {
               var7 = 0;
            }
         }

         String var8 = var1[4];
         if (var8 == null && var7 < 3) {
            var8 = var1[3];
            if (var8 == null && var7 < 1) {
               var8 = var1[2];
            }
         }

         if (var8 == null) {
            var8 = "";
         }

         try {
            String var9 = (String)((DataFieldModel)RDbLoader.this.bm.get(RDbLoader.this.lc_fm.id).fids.get(var1[0])).features.get("vid");
            if (var9 != null && RDbLoader.this.needToDropByVidCheck(var9)) {
               System.out.println("value : " + var8 + " dropped by vid check");
               return 0;
            }
         } catch (Exception var14) {
         }

         Vector var19 = new Vector();

         try {
            if (var1[2] == null && var7 == 1) {
               var19.add("");
            } else {
               var19.add(var1[2]);
            }
         } catch (Exception var18) {
            var19.add((Object)null);
         }

         try {
            if (var1[3] == null && var7 == 2) {
               var19.add("");
            } else {
               var19.add(var1[3]);
            }
         } catch (Exception var17) {
            var19.add((Object)null);
         }

         try {
            if (var1[4] == null && var7 == 4) {
               var19.add("");
            } else {
               var19.add(var1[4]);
            }
         } catch (Exception var16) {
            var19.add((Object)null);
         }

         var19.add(var2);
         RDbLoader.this.dsh.set(var6, var19);
         RDbLoader.this.dsh.sete(var6, var19);
         int var10 = RDbLoader.this.lc_fm.get((PageModel)RDbLoader.this.lc_fm.fids_page.get(var1[0]));
         if (RDbLoader.this.pc[var10] < var5 + 1) {
            RDbLoader.this.pc[var10] = var5 + 1;
         }

         boolean var11 = ((DataFieldModel)RDbLoader.this.bm.get(RDbLoader.this.lc_fm.id).fids.get(var1[0])).features.get("datatype").equals("check");
         if (var11) {
            if (!var8.equals("X") && !var8.equals("x")) {
               if (var8.equals("")) {
                  var8 = "false";
               }
            } else {
               var8 = "true";
            }
         }

         RDbLoader.this.ds.set(var6, var8);
         long var12 = System.nanoTime();
         RDbLoader.setvaluetime += var12 - var3;
         return 0;
      }

      public int partend() {
         try {
            RDbLoader.this.del_empty_dyn_pages_from_end();
            ((GUI_Datastore)RDbLoader.this.ds).setSavepoint(RDbLoader.this.bm);
            if (RDbLoader.this.dbhandler.isPapiros() && RDbLoader.this.isBatch()) {
               CalculatorManager.getInstance().runDPageNumberCalcs();
            }

            boolean var1 = CalculatorManager.getInstance().doBetoltErtekCalcs(true);
            if (var1 && Calculator.getInstance().isInJavkeretOpMode()) {
               CalculatorManager.getInstance().form_hidden_fields_calc();
               CalculatorManager.getInstance().multi_form_load();
               System.out.println("RDbLoader.end: Javkeret, betölt érték módosulása miatt újraszámítás");
               CalculatorManager.getInstance().form_calc();
            } else {
               CalculatorManager.getInstance().form_hidden_fields_calc();
               CalculatorManager.getInstance().multi_form_load();
               ((GUI_Datastore)RDbLoader.this.ds).setSavepoint(RDbLoader.this.bm);
            }
         } catch (Exception var4) {
            Tools.exception2SOut(var4);
         }

         if (RDbLoader.this.nocachemode) {
            int var2;
            if (!RDbLoader.this.first) {
               Result var5 = DataChecker.getInstance().superCheck(RDbLoader.this.bm, false, RDbLoader.this.bm.cc.size() - 1);
               if (var5.isOk()) {
                  var2 = CalculatorManager.getInstance().do_fly_check_all_one(RDbLoader.this.ell4xs);
                  RDbLoader.this.errlistsave2db();
                  if (var2 != 0) {
                     RDbLoader.globalstatus = var2;
                     System.out.println("do_fly_check_all_one return: " + var2);
                     return var2;
                  }
               } else {
                  Vector var8 = var5.errorList;
                  RDbLoader.this.dbhandler.saveErrorList(var8, RDbLoader.this.dbparams);
                  RDbLoader.this.voltnyomtatvanyhiba = true;
               }
            } else {
               RDbLoader.this.maindbparams = RDbLoader.this.dbparams;
               IELogicResult var6 = ElogicCaller.eventBatchBeforeDataLoad(RDbLoader.this.bm);
               if (var6.getStatus() != 0) {
                  RDbLoader.globalstatus = var6.getStatus();
                  System.out.println("ElogicCaller.eventBatchBeforeDataLoad return: " + var6.getMessage());
                  return var6.getStatus();
               }

               var2 = CalculatorManager.getInstance().do_fly_check_all_start();
               if (var2 != 0) {
                  RDbLoader.this.errlistsave2db();
                  RDbLoader.globalstatus = var2;
                  System.out.println("do_fly_check_all_start return: " + var2);
                  return var2;
               }

               Vector var3 = RDbLoader.this.ell4xs.getErrorListForDBBatch();
               RDbLoader.this.vprecheck = new Vector(var3);
               RDbLoader.this.ell4xs.clearErrorList();
            }

            if (RDbLoader.this.bm.cc.size() % RDbLoader.this.asc == 0) {
               int var7 = RDbLoader.this.dbhandler.setLifeSignal(RDbLoader.this.dbparams);
               System.out.println("setLifeSignal ret=" + var7);
               if (var7 != 0) {
                  RDbLoader.globalstatus = var7;
                  return var7;
               }
            }
         }

         RDbLoader.this.first = false;
         return 0;
      }

      public int end() {
         if (RDbLoader.globalstatus != 0) {
            return RDbLoader.globalstatus;
         } else {
            RDbLoader.this.bm.setCalcelemindex(0);
            RDbLoader.this.bm.cc.setActiveObject(RDbLoader.this.bm.cc.get(0));
            if (RDbLoader.this.nocachemode) {
               RDbLoader.this.dbparams = RDbLoader.this.maindbparams;
               Result var1 = DataChecker.getInstance().superCheck(RDbLoader.this.bm, false, 0);
               if (var1.isOk()) {
                  int var2 = CalculatorManager.getInstance().do_fly_check_all_one(RDbLoader.this.ell4xs);
                  RDbLoader.this.ell4xs.errorList.addAll(RDbLoader.this.vprecheck);
                  RDbLoader.this.errlistsave2db();
                  if (var2 != 0) {
                     return var2;
                  }

                  CalculatorManager.getInstance().do_fly_check_all_stop();
                  if (MainFrame.batch_recalc_mode) {
                     Object var3 = null;
                     String var4 = MainFrame.batch_recalc_role;
                     if ("A".equals(MainFrame.batch_recalc_role)) {
                        var4 = "3";
                     }

                     if ("J".equals(MainFrame.batch_recalc_role)) {
                        var4 = "4";
                     }

                     if (!RDbLoader.this.save(RDbLoader.this.bm, var4, (String)var3, 0)) {
                        return -66;
                     }
                  }
               } else {
                  Vector var6 = var1.errorList;
                  RDbLoader.this.dbhandler.saveErrorList(var6, RDbLoader.this.dbparams);
                  RDbLoader.this.voltnyomtatvanyhiba = true;
               }
            }

            long var5 = System.nanoTime();
            return 0;
         }
      }

      public boolean hasEndSignal() {
         try {
            return !MainFrame.runfile.exists();
         } catch (Exception var2) {
            return false;
         }
      }

      public boolean isTestMode() {
         return MainFrame.ubevtesztmode;
      }

      // $FF: synthetic method
      MyIDbConnectQf(Object var2) {
         this();
      }
   }
}
