package hu.piller.enykp.alogic.fileloader.xml;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.ebev.Ebev;
import hu.piller.enykp.alogic.filepanels.XMLMergePanel;
import hu.piller.enykp.alogic.filepanels.attachement.CstParser;
import hu.piller.enykp.alogic.filepanels.attachement.EJFileChooser;
import hu.piller.enykp.alogic.filepanels.batch.BatchFunctions;
import hu.piller.enykp.alogic.filepanels.mohu.AVDHQuestionDialog;
import hu.piller.enykp.alogic.fileutil.DataChecker;
import hu.piller.enykp.alogic.settingspanel.BaseSettingsPane;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.alogic.settingspanel.upgrade.UpgradeFunction;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.framework.MainPanel;
import hu.piller.enykp.gui.framework.Menubar;
import hu.piller.enykp.gui.framework.StatusPane;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldFactory;
import hu.piller.enykp.gui.viewer.DefaultMultiFormViewer;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

public class XMLPost {
   public static boolean inxmldisplay = false;
   public static boolean xmleditnemjo = false;
   public static boolean xmlbooleancheck = false;

   public static void done() {
      EJFileChooser var0 = new EJFileChooser();
      String var1 = SettingsStore.getInstance().get("userpaths", "prop.dynamic.userlastpath.2");
      File var2 = new File((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.import"));
      if (var1 != null) {
         var2 = new File(var1.toString());
      }

      var0.setCurrentDirectory(var2);
      var0.setDialogTitle("XML v. XCZ állomány kiválasztása");
      FileFilter var3 = new FileFilter() {
         public boolean accept(File var1) {
            String var2 = var1.getAbsolutePath().toLowerCase();
            if (!var2.endsWith(".xml") && !var2.endsWith(".xcz")) {
               return var1.isDirectory();
            } else {
               return true;
            }
         }

         public String getDescription() {
            return "XML és XCZ állományok";
         }
      };
      var0.removeChoosableFileFilter(var0.getChoosableFileFilters()[0]);
      var0.addChoosableFileFilter(var3);
      int var4 = var0.showOpenDialog(MainFrame.thisinstance);
      if (var4 == 0) {
         File var5 = var0.getSelectedFile();
         if (var5 != null && var5.exists()) {
            try {
               String var6 = null;

               try {
                  var6 = var5.getParent();
               } catch (Exception var15) {
               }

               SettingsStore.getInstance().set("userpaths", "prop.dynamic.userlastpath.2", var6);

               try {
                  var5 = handlePackedFile(var5);
               } catch (Exception var14) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiba a csomagolt fájl feladásakor: " + var14.getMessage(), "Hiba", 0);
                  StatusPane.thisinstance.currentpagename.setText("");
                  deltmpfiles();
                  return;
               }

               if (var5 == null) {
                  StatusPane.thisinstance.currentpagename.setText("");
                  deltmpfiles();
                  return;
               }

               var5 = Tools.clearFileName(var5);
               if (var5 == null) {
                  StatusPane.thisinstance.currentpagename.setText("");
                  deltmpfiles();
                  return;
               }

               ErrorList.getInstance().writeError(new Integer(23112), "[ " + (new SimpleDateFormat()).format(new Date()) + " ] " + var5.getAbsolutePath(), 0, (Exception)null, (Object)null);
               if (!check_codepage(var5)) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "A megadott állomány nem megfelelő kódolású!\nA kódolás csak 'windows-1250' vagy 'utf-8' lehet.\n ( " + var5 + " )", "Hiba", 0);
                  deltmpfiles();
                  return;
               }

               Object var7 = new XmlLoader();
               if (((XmlLoader)var7).checksuffix(var5)) {
                  var7 = new XkrLoader();
               }

               Hashtable var8 = ((XmlLoader)var7).getHeadData(var5);
               if (((XmlLoader)var7).hasError) {
                  String var17 = ((XmlLoader)var7).errormsg.substring(((XmlLoader)var7).errormsg.indexOf(":") + 1);
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiba a megadott állomány betöltésekor! ( " + var5 + " )\nSúlyos hiba az XML formai ellenőrzése során:\n  " + var17, "Hiba", 0);
                  deltmpfiles();
                  return;
               }

               Hashtable var9 = (Hashtable)var8.get("docinfo");
               Hashtable var10 = new Hashtable();
               var10.put("docinfo", var9);
               Object[] var11 = new Object[]{var5, null, null, var10};
               Object[] var12 = new Object[]{var11};
               Hashtable var13 = new Hashtable();
               var13.put("selected_files", var12);
               var13.put("read_only", Boolean.FALSE);
               var13.put("function_read_only", Boolean.FALSE);
               var13.put("file_status", "");
               var13.put("templatever", var9.get("ver"));
               xmlbooleancheck = true;
               done_load(var13);
            } catch (Exception var16) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiba a megadott állomány betöltésekor! ( " + var5 + " )", "Hiba", 2);
               deltmpfiles();
            }

            StatusPane.thisinstance.currentpagename.setText("");
         }
      }

   }

   public static void done_load(Hashtable var0) {
      if (var0 != null && var0.size() != 0) {
         Object[] var1 = (Object[])((Object[])var0.get("selected_files"));
         final File var2 = (File)((Object[])((Object[])var1[0]))[0];
         Object[] var3 = new Object[]{"Igen", "Nem"};
         int var4 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Indulhat az állomány ellenőrzése?\n" + var2, "Kérdés", 0, 3, (Icon)null, var3, var3[0]);
         if (var4 != 0) {
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  MainFrame.thisinstance.setGlassLabel((String)null);
                  XMLPost.done();
               }
            });
            return;
         }

         boolean var5 = (Boolean)var0.get("read_only");
         boolean var6 = (Boolean)var0.get("function_read_only");
         String var7 = var0.get("file_status").toString();
         final Hashtable var8 = (Hashtable)((Hashtable)((Object[])((Object[])var1[0]))[3]).get("docinfo");
         MainFrame.thisinstance.setGlassLabel("Betöltés folyamatban!");
         Thread var9 = new Thread(new Runnable() {
            public void run() {
               String var1 = (String)var8.get("id");
               String var2x = (String)var8.get("templatever");
               if (var2x == null) {
                  var2x = (String)var8.get("ver");
               }

               String var3 = (String)var8.get("org");
               XMLFlyCheckLoader var4 = new XMLFlyCheckLoader();
               var4.silentheadcheck = true;
               CalculatorManager.xml = true;
               PropertyList.getInstance().set("prop.dynamic.ilyenkor", "");
               BookModel var5 = var4.load(var2.getAbsolutePath(), var1, var2x, var3, false);
               int var6 = var5.carryOnTemplate();
               Vector var8x;
               Vector var16;
               MainPanel var10001;
               switch(var6) {
               case 0:
                  var16 = new Vector();

                  try {
                     var16.add(new TextWithIcon("  - Hiba történt a nyomtatvány betöltésekor! " + BookModel.CHECK_VALID_MESSAGES[var6], 1));
                  } catch (Exception var11) {
                  }

                  new ErrorDialog(MainFrame.thisinstance, "Hiba!", true, true, var16, var2);
                  var10001 = MainFrame.thisinstance.mp;
                  MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                  MainFrame.thisinstance.setGlassLabel((String)null);
                  var4.destroy();
                  XMLPost.deltmpfiles();
                  MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                  MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                  return;
               case 1:
                  int var7 = Tools.handleTemplateCheckerResult(var5);
                  if (var7 >= 4) {
                     var8x = new Vector();

                     try {
                        var8x.add(new TextWithIcon("  - Hiba történt a nyomtatvány betöltésekor! " + BookModel.CHECK_VALID_MESSAGES[var7], 1));
                     } catch (Exception var10) {
                     }

                     new ErrorDialog(MainFrame.thisinstance, "Hiba!", true, true, var8x, var2);
                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                     MainFrame.thisinstance.setGlassLabel((String)null);
                     var4.destroy();
                     XMLPost.deltmpfiles();
                     MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                     MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                     return;
                  }
               case 2:
               default:
                  break;
               case 3:
                  System.out.println("HIBA_AZ_ELLENORZESKOR");
               }

               PropertyList.getInstance().set("prop.dynamic.ilyenkor", (Object)null);
               CalculatorManager.xml = false;
               GuiUtil.setcurrentpagename("");
               var4.silentheadcheck = false;
               XMLPost.xmlbooleancheck = false;
               if (!var5.hasError) {
                  if (var5.splitesaver.equalsIgnoreCase("true")) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az ilyen típusú nyomtatványt (több nyomtatvány egybecsomagolva) nem lehet importálni!\nAz xml állomány nem adható fel.\n ( " + var2 + " )", "Hiba", 0);
                     MainFrame.thisinstance.setGlassLabel((String)null);
                     var4.destroy();
                     XMLPost.deltmpfiles();
                     return;
                  }

                  if (var4.headcheckfatalerror) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatvány súlyos hibát tartalmaz!\nAz xml állomány nem adható fel.\n ( " + var2 + " )", "Hiba", 0);
                     MainFrame.thisinstance.setGlassLabel((String)null);
                     var4.destroy();
                     XMLPost.deltmpfiles();
                     return;
                  }

                  if (var4.fatalerror) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "A megadott állomány nem létező mezőkódot tartalmaz!\nAz xml állomány nem adható fel.\n ( " + var2 + " )", "Hiba", 0);
                     MainFrame.thisinstance.setGlassLabel((String)null);
                     var4.destroy();
                     XMLPost.deltmpfiles();
                     return;
                  }

                  MainFrame.thisinstance.setGlassLabel((String)null);

                  try {
                     boolean var17 = true;
                     if (var4.errorVector != null && Tools.hasFatalError(var4.errorVector)) {
                        var17 = false;
                     }

                     if (var17) {
                        int var18 = 0;
                        if (var5.isAvdhModel()) {
                           new AVDHQuestionDialog(MainFrame.thisinstance);
                           var18 = (Integer)PropertyList.getInstance().get("prop.dynamic.AVDHQuestionDialogAnswer");
                           PropertyList.getInstance().set("prop.dynamic.AVDHQuestionDialogAnswer", (Object)null);
                        }

                        if (var18 != 0) {
                           CalculatorManager.xml = false;
                           XMLPost.deltmpfiles();
                           var10001 = MainFrame.thisinstance.mp;
                           MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                           var4.destroy();
                           SwingUtilities.invokeLater(new Runnable() {
                              public void run() {
                                 MainFrame.thisinstance.setGlassLabel((String)null);
                                 XMLPost.done();
                              }
                           });
                           return;
                        }
                     }

                     CalculatorManager.xml = true;
                     var8x = null;
                     var8x = var4.errorVector;
                     Ebev var9 = new Ebev(var5);
                     PropertyList.getInstance().set("prop.dynamic.ebev_call_from_xmlpost", Boolean.TRUE);
                     var9.mark(false, true, var8x);
                     CalculatorManager.xml = false;
                     XMLPost.deltmpfiles();
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                  } catch (Exception var15) {
                     XMLPost.deltmpfiles();
                  }

                  try {
                     try {
                        var16 = (Vector)PropertyList.getInstance().get("prop.dynamic.ebev_call_from_xmlpost");
                     } catch (Exception var13) {
                        var16 = new Vector();
                     }

                     if (var16.size() > 0) {
                        new ErrorDialog(MainFrame.thisinstance, "Az xml állományt sikeresen megjelöltük. Az alábbi csatolmányokat adtuk hozzá:", true, true, var16, var2);
                     }

                     PropertyList.getInstance().set("prop.dynamic.ebev_call_from_xmlpost", (Object)null);
                  } catch (HeadlessException var14) {
                     Tools.eLog(var14, 0);
                  }

                  System.out.println("StatusText CLEARED");
                  MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                  var4.destroy();
               } else {
                  var16 = new Vector();

                  try {
                     String[] var19;
                     if (!var5.isDisabledTemplate()) {
                        var16.add(new TextWithIcon("  - Hiba történt a nyomtatvány betöltésekor! Súlyos hiba az XML formai ellenőrzése során:", 1));
                        var19 = var5.errormsg.split("##");
                        var16.add(new TextWithIcon("           " + var19[0], -1));
                        var16.add(new TextWithIcon("           " + var19[1], -1));
                     } else if (var5.errormsg != null) {
                        var19 = var5.errormsg.split(" bl_url ");
                        var16.add(new TextWithIcon(var19[0], -1));
                        if (var19.length > 1) {
                           var16.add(new TextWithIcon(var19[1], -1));
                        }
                     }
                  } catch (Exception var12) {
                  }

                  new ErrorDialog(MainFrame.thisinstance, "Hiba!", true, true, var16, var2);
                  var10001 = MainFrame.thisinstance.mp;
                  MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                  var4.destroy();
                  SwingUtilities.invokeLater(new Runnable() {
                     public void run() {
                        MainFrame.thisinstance.setGlassLabel((String)null);
                        XMLPost.done();
                     }
                  });
               }

               MainFrame.thisinstance.setGlassLabel((String)null);
            }
         });
         var9.start();
      }

   }

   private static void deltmpfiles() {
      try {
         Vector var0 = (Vector)PropertyList.getInstance().get("prop.usr.xcz.files");

         for(int var1 = 0; var1 < var0.size(); ++var1) {
            String var2 = (String)var0.get(var1);
            File var3 = new File(var2);
            var3.delete();
         }

         String var6 = (String)PropertyList.getInstance().get("prop.usr.xcz.dir");
         File var7 = new File(var6);
         Tools.deleteDir(var7);
      } catch (Exception var5) {
      }

      try {
         PropertyList.getInstance().set("prop.usr.xcz.files", (Object)null);
         PropertyList.getInstance().set("prop.usr.xcz.dir", (Object)null);
      } catch (Exception var4) {
      }

   }

   public static void display() {
      EJFileChooser var0 = new EJFileChooser();
      String var1 = SettingsStore.getInstance().get("userpaths", "prop.dynamic.userlastpath.3");
      File var2 = new File((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.import"));
      if (var1 != null) {
         var2 = new File(var1.toString());
      }

      var0.setCurrentDirectory(var2);
      var0.setDialogTitle("XML állomány kiválasztása");
      FileFilter var3 = new FileFilter() {
         public boolean accept(File var1) {
            String var2 = var1.getAbsolutePath().toLowerCase();
            if (!var2.endsWith(".xml") && !var2.endsWith(".xkr")) {
               return var1.isDirectory();
            } else {
               return true;
            }
         }

         public String getDescription() {
            return "XML és XKR állományok";
         }
      };
      var0.removeChoosableFileFilter(var0.getChoosableFileFilters()[0]);
      var0.addChoosableFileFilter(var3);
      int var4 = var0.showOpenDialog(MainFrame.thisinstance);
      if (var4 == 0) {
         File var5 = var0.getSelectedFile();
         if (var5 != null && var5.exists()) {
            String var6 = null;

            try {
               var6 = var5.getParent();
            } catch (Exception var8) {
            }

            SettingsStore.getInstance().set("userpaths", "prop.dynamic.userlastpath.3", var6);
            ErrorList.getInstance().writeError(new Integer(23112), "[ " + (new SimpleDateFormat()).format(new Date()) + " ] " + var5.getAbsolutePath(), 0, (Exception)null, (Object)null);
            done_display(var5);
         }
      } else {
         GuiUtil.setcurrentpagename("");
      }

   }

   public static void done_display(File var0) {
      done_display(var0, true);
   }

   public static void done_display(final File var0, final boolean var1) {
      MainFrame.thisinstance.setGlassLabel("Betöltés folyamatban!");
      setEditStateForBetoltErtek(!var1);
      Thread var2 = new Thread(new Runnable() {
         public void run() {
            try {
               System.out.println("XML betöltes kezdete [ " + (new SimpleDateFormat("yyyyMMdd HHmmss")).format(new Date()) + " ] ");
               Object var1x = new XmlLoader();
               if (((XmlLoader)var1x).checksuffix(var0)) {
                  var1x = new XkrLoader();
               }

               Hashtable var2 = ((XmlLoader)var1x).getHeadData(var0);
               BookModel var3;
               if (!((XmlLoader)var1x).hasError) {
                  Hashtable var4 = (Hashtable)var2.get("docinfo");
                  String var5 = (String)var4.get("id");
                  String var6 = (String)var4.get("templatever");
                  if (var6 == null) {
                     var6 = (String)var4.get("ver");
                  }

                  String var7 = (String)var4.get("org");
                  XMLPost.inxmldisplay = true;
                  ((XmlLoader)var1x).silentheadcheck = true;
                  PropertyList.getInstance().set("prop.dynamic.xml_loader_call", UpgradeFunction.XML_EDIT);
                  var3 = ((XmlLoader)var1x).load(var0.getAbsolutePath(), var5, var6, var7, false);
                  if (!var3.hasError && var3.get_main_index() == -1) {
                     var3.hasError = true;
                     var3.errormsg = "A nyomtatvány nem tartalmaz fődokumentumot, ezért nem használható önállóan. Csak hozzáadással emelhető be.##Állományok  kötegelt nyomtatványba való beemeléséről részletes leírást a: \"Speciális kötegelt nyomtatványok kezelése a nyomtatvány kitöltőben\" dokumentációban talál.";
                  }

                  ((XmlLoader)var1x).silentheadcheck = false;
                  XMLPost.inxmldisplay = false;
                  if (PropertyList.getInstance().get("generalas_miatti_ujraszamitas") != null) {
                     try {
                        CalculatorManager.getInstance().multiform_calc();
                        CalculatorManager.getInstance().feltetelesErtekPreCheck();
                     } catch (Exception var10) {
                        var3.hasError = true;
                        var3.errormsg = "Generálás##Hiba a nyomtatvány újraszámításakor";
                     }
                  }
               } else {
                  var3 = new BookModel();
                  var3.hasError = true;
                  var3.errormsg = ((XmlLoader)var1x).errormsg;
               }

               MainPanel var10001;
               if (var3.hasError) {
                  Vector var13 = new Vector();

                  try {
                     var13.add(new TextWithIcon("  - Hiba történt a nyomtatvány betöltésekor! Súlyos hiba az XML formai ellenőrzése során:", 1));
                     String[] var17 = var3.errormsg.split("##");
                     var13.add(new TextWithIcon("           " + var17[0], -1));
                     var13.add(new TextWithIcon("           " + var17[1], -1));
                  } catch (Exception var9) {
                  }

                  if (PropertyList.getInstance().get("prop.dynamic.templateLoad.userTerminated") == null) {
                     new ErrorDialog(MainFrame.thisinstance, "Hiba!", true, true, var13);
                  }

                  PropertyList.getInstance().set("prop.dynamic.templateLoad.userTerminated", (Object)null);
                  var10001 = MainFrame.thisinstance.mp;
                  MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                  MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                  MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                  var3.destroy();
                  if (!XMLPost.xmleditnemjo) {
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                           MainFrame.thisinstance.setGlassLabel((String)null);
                           XMLPost.display();
                        }
                     });
                  } else {
                     XMLPost.xmleditnemjo = false;
                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                  }
               } else {
                  MainFrame.thisinstance.setGlassLabel("Adatok formai ellenőrzése!");
                  Result var12 = DataChecker.getInstance().superCheck(var3, false);
                  MainFrame.thisinstance.setGlassLabel((String)null);
                  if (!var12.isOk()) {
                     new ErrorDialog(MainFrame.thisinstance, "Hibalista", true, true, var12.errorList);
                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                     MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                     MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                     MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                     var3.destroy();
                     if (!XMLPost.xmleditnemjo) {
                        SwingUtilities.invokeLater(new Runnable() {
                           public void run() {
                              MainFrame.thisinstance.setGlassLabel((String)null);
                              XMLPost.display();
                           }
                        });
                     } else {
                        XMLPost.xmleditnemjo = false;
                     }

                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                     return;
                  }

                  if (!XMLPost.xmleditnemjo) {
                     CstParser var15 = new CstParser();
                     MainFrame.thisinstance.setGlassLabel("Csatolmányok feldolgozása!");
                     var15.parse(var3);
                  }

                  DefaultMultiFormViewer var16 = new DefaultMultiFormViewer(var3);
                  if (var16.fv.getTp().getTabCount() == 0) {
                     var3.destroy();
                     XMLPost.xmleditnemjo = false;
                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                     MainFrame.thisinstance.setGlassLabel((String)null);
                     MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                     MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                     MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                     return;
                  }

                  MainFrame.thisinstance.mp.intoleftside(var16);
                  int var18 = var3.carryOnTemplate();
                  boolean var19 = true;
                  int var20;
                  String var21;
                  switch(var18) {
                  case 0:
                     MainFrame.thisinstance.mp.funcreadonly = true;
                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
                     var21 = BookModel.CHECK_VALID_MESSAGES[var18];
                     MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var21);
                     var20 = 0;
                     break;
                  case 1:
                     var20 = Tools.handleTemplateCheckerResult(var3);
                     if (var20 >= 4) {
                        MainFrame.thisinstance.mp.funcreadonly = true;
                        var10001 = MainFrame.thisinstance.mp;
                        MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
                        var21 = BookModel.CHECK_VALID_MESSAGES[var20];
                        MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var21);
                     } else {
                        var20 = -1;
                     }
                     break;
                  case 2:
                  default:
                     var20 = -1;
                     break;
                  case 3:
                     var20 = -1;
                     System.out.println("HIBA_AZ_ELLENORZESKOR");
                  }

                  if (var20 == -1) {
                     MainFrame.thisinstance.mp.funcreadonly = var1;
                     if (!var1 && !var3.isDisabledTemplate()) {
                        var10001 = MainFrame.thisinstance.mp;
                        MainFrame.thisinstance.mp.setstate(MainPanel.NORMAL);
                     } else {
                        var10001 = MainFrame.thisinstance.mp;
                        MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
                     }

                     if (!var3.isDisabledTemplate()) {
                        MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(!var1 && !var3.isDisabledTemplate() ? "Módosítható" : "Megnyitva csak olvasásra");
                     }
                  }

                  MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("Ny:" + (String)var3.docinfo.get("ver"));
                  if (var3.isDisabledTemplate()) {
                     TemplateUtils.getInstance().handleDisabledTemplateMessage(var3);
                  }
               }

               MainFrame.thisinstance.setGlassLabel((String)null);
               long var14 = Runtime.getRuntime().freeMemory() / 1024L / 1024L;
               System.out.println("Free memory=" + var14 + " MB");
               System.out.println("XML betöltes stop [ " + (new SimpleDateFormat("yyyyMMdd HHmmss")).format(new Date()) + " ] ");
               if (MainFrame.xml_loaded_dialog) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "XML állomány betöltve! ( " + var0 + " )", "Üzenet", 1);
               }
            } catch (Exception var11) {
               XMLPost.inxmldisplay = false;
               MainFrame.thisinstance.setGlassLabel((String)null);
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiba a megadott állomány betöltésekor! ( " + var0 + " )", "Hiba", 2);
               var11.printStackTrace();
            }

         }
      });
      var2.start();
   }

   private static boolean check_codepage(File var0) {
      String var1 = getEncoding(var0);
      if (var1 == null) {
         return false;
      } else {
         return PropertyList.ENABLED_CODEPAGES.contains(var1.toLowerCase());
      }
   }

   public static String getEncoding(File var0) {
      FileInputStream var1 = null;

      try {
         var1 = new FileInputStream(var0);
         int var2 = var1.read();
         if (var2 == 255) {
            return "UTF-16LE";
         } else if (var2 == 254) {
            return "UTF-16BE";
         } else {
            if (var2 == 239) {
            }

            byte[] var3 = new byte[256];
            var1.read(var3);
            String var4 = new String(var3);
            int var5 = var4.indexOf("encoding=");
            int var6 = var4.indexOf("\"", var5);
            int var7 = var4.indexOf("\"", var6 + 1);
            if (var6 == -1) {
               var6 = var4.indexOf("'", var5);
               var7 = var4.indexOf("'", var6 + 1);
            }

            var1.close();
            return var4.substring(var6 + 1, var7);
         }
      } catch (Exception var9) {
         try {
            var1.close();
         } catch (IOException var8) {
         }

         return null;
      }
   }

   public static void edit() {
      EJFileChooser var0 = new EJFileChooser();
      String var1 = SettingsStore.getInstance().get("userpaths", "prop.dynamic.userlastpath.4");
      File var2 = new File((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.import"));
      if (var1 != null) {
         var2 = new File(var1.toString());
      }

      var0.setCurrentDirectory(var2);
      var0.setDialogTitle("XML állomány kiválasztása");
      FileFilter var3 = new FileFilter() {
         public boolean accept(File var1) {
            String var2 = var1.getAbsolutePath().toLowerCase();
            if (!var2.endsWith(".xml") && !var2.endsWith(".xkr")) {
               return var1.isDirectory();
            } else {
               return true;
            }
         }

         public String getDescription() {
            return "XML és XKR állományok";
         }
      };
      var0.removeChoosableFileFilter(var0.getChoosableFileFilters()[0]);
      var0.addChoosableFileFilter(var3);
      int var4 = var0.showOpenDialog(MainFrame.thisinstance);
      if (var4 == 0) {
         File var5 = var0.getSelectedFile();
         if (var5 != null && var5.exists()) {
            String var6 = null;

            try {
               var6 = var5.getParent();
            } catch (Exception var8) {
            }

            SettingsStore.getInstance().set("userpaths", "prop.dynamic.userlastpath.4", var6);
            ErrorList.getInstance().writeError(new Integer(23112), "[ " + (new SimpleDateFormat()).format(new Date()) + " ] " + var5.getAbsolutePath(), 0, (Exception)null, (Object)null);
            BatchFunctions var7 = new BatchFunctions(true, false, true);
            MainFrame.thisinstance.setGlassLabel("Importálás folyamatban ...");
            var7.cmdOne(var5.getAbsolutePath(), (String)null, true);
         }
      } else {
         GuiUtil.setcurrentpagename("");
      }

   }

   public static void edit_nem_jo() {
      EJFileChooser var0 = new EJFileChooser();
      String var1 = SettingsStore.getInstance().get("userpaths", "prop.dynamic.userlastpath.5");
      File var2 = new File((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.import"));
      if (var1 != null) {
         var2 = new File(var1.toString());
      }

      var0.setCurrentDirectory(var2);
      var0.setDialogTitle("XML állomány kiválasztása");
      FileFilter var3 = new FileFilter() {
         public boolean accept(File var1) {
            String var2 = var1.getAbsolutePath().toLowerCase();
            if (var2.endsWith(".xml")) {
               return true;
            } else {
               return var1.isDirectory();
            }
         }

         public String getDescription() {
            return "XML állományok";
         }
      };
      var0.removeChoosableFileFilter(var0.getChoosableFileFilters()[0]);
      var0.addChoosableFileFilter(var3);
      int var4 = var0.showOpenDialog(MainFrame.thisinstance);
      if (var4 == 0) {
         File var5 = var0.getSelectedFile();
         if (var5 != null) {
            String var6;
            if (!var5.exists()) {
               var6 = var5.getAbsolutePath() + ".xml";
               var5 = new File(var6);
            }

            if (var5.exists()) {
               var6 = null;

               try {
                  var6 = var5.getParent();
               } catch (Exception var8) {
               }

               SettingsStore.getInstance().set("userpaths", "prop.dynamic.userlastpath.5", var6);
               xmleditnemjo = true;
               ErrorList.getInstance().writeError(new Integer(23112), "[ " + (new SimpleDateFormat()).format(new Date()) + " ] " + var5.getAbsolutePath(), 0, (Exception)null, (Object)null);
               done_display(var5, false);
            }
         }
      } else {
         GuiUtil.setcurrentpagename("");
      }

   }

   public static void cmd_edit_nem_jo(File var0) {
      xmleditnemjo = true;
      ErrorList.getInstance().writeError(new Integer(23112), "[ " + (new SimpleDateFormat()).format(new Date()) + " ] " + var0.getAbsolutePath(), 0, (Exception)null, (Object)null);
      done_display(var0, false);
   }

   public static void save() {
      EJFileChooser var0 = new EJFileChooser();
      String var1 = SettingsStore.getInstance().get("userpaths", "prop.dynamic.userlastpath.6");
      File var2 = new File((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.import"));
      if (var1 != null) {
         var2 = new File(var1.toString());
      }

      var0.setCurrentDirectory(var2);
      var0.setDialogTitle("XML állomány kiválasztása");
      FileFilter var3 = new FileFilter() {
         public boolean accept(File var1) {
            if (var1.getAbsolutePath().toLowerCase().endsWith(".xml")) {
               return true;
            } else {
               return var1.isDirectory();
            }
         }

         public String getDescription() {
            return "XML állományok";
         }
      };
      var0.removeChoosableFileFilter(var0.getChoosableFileFilters()[0]);
      var0.addChoosableFileFilter(var3);
      int var4 = var0.showSaveDialog(MainFrame.thisinstance);
      if (var4 == 0) {
         File var5 = var0.getSelectedFile();
         if (var5 != null) {
            String var6 = null;

            try {
               var6 = var5.getParent();
            } catch (Exception var13) {
            }

            SettingsStore.getInstance().set("userpaths", "prop.dynamic.userlastpath.6", var6);
            DefaultMultiFormViewer var7 = MainFrame.thisinstance.mp.getDMFV();
            BookModel var8 = var7.bm;
            final Ebev var9 = new Ebev(var8);
            String var10 = "";
            if (!var10.toLowerCase().endsWith(".xml")) {
               var10 = var10 + ".xml";
            } else {
                var10 = var5.getAbsolutePath();
            }

             MainFrame.thisinstance.setGlassLabel("XML mentése folyamatban!");
            String finalVar1 = var10;
            Thread var12 = new Thread(new Runnable() {
               public void run() {
                  System.out.println("XML mentes start [ " + (new SimpleDateFormat("yyyyMMdd HHmmss")).format(new Date()) + " ] ");
                  Result var1 = new Result();
                  if (XMLPost.checkChars(finalVar1)) {
                     var1 = var9.export(finalVar1);
                  } else {
                     var1.setOk(false);
                     var1.errorList.add("Érvénytelen fájlnevet adott meg!");
                  }

                  System.out.println("XML mentes stop [ " + (new SimpleDateFormat("yyyyMMdd HHmmss")).format(new Date()) + " ] ");
                  if (!var1.isOk()) {
                     if (var1.errorList.size() == 0) {
                        var1.errorList.add("Hiba történt a művelet elvégzésekor.");
                     }

                     GuiUtil.showMessageDialog(MainFrame.thisinstance, var1.errorList.get(0), "XML mentés", 0);
                  }

                  MainFrame.thisinstance.setGlassLabel((String)null);
               }
            });
            var12.start();
         }
      }

   }

   public static void merge() {
      String var0 = "XML file-ok összemásolása";
      JDialog var1 = new JDialog(MainFrame.thisinstance, var0, true);
      XMLMergePanel var2 = new XMLMergePanel(var1);
      var1.getContentPane().add(var2);
      var1.setSize(GuiUtil.getW("WWWKérem adja meg az összemásolandó file-okat!WWW"), 25 * GuiUtil.getCommonItemHeight());
      var1.setLocationRelativeTo(MainFrame.thisinstance);
      var1.show();
   }

   public static boolean close() {
      final DefaultMultiFormViewer var0 = MainFrame.thisinstance.mp.getDMFV();
      setEditStateForBetoltErtek(false);
      if (var0 != null) {
         if (!var0.fv.pv.pv.leave_component()) {
            return false;
         }

         BookModel var1 = var0.bm;
         if (!MainFrame.thisinstance.mp.readonlymode) {
            final int var2 = Menubar.thisinstance.savequestion();
            if (var2 != 2) {
               Thread var3 = new Thread(new Runnable() {
                  public void run() {
                     if (var2 == 0) {
                        XMLPost.save2();
                     }

                     XMLPost.xmleditnemjo = false;
                     var0.destroy();
                     MainFrame.thisinstance.mp.intoleftside(new JPanel());
                     DataFieldFactory.getInstance().freemem();
                     MainFrame.thisinstance.mp.set_kiut_url((String)null);
                     MainPanel var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                     MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                     MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                     BaseSettingsPane.done_menuextratext(true);
                     MainFrame.thisinstance.mp.readonlymode = false;
                     MainFrame.thisinstance.mp.forceDisabledPageShowing = false;
                     MainFrame.thisinstance.mp.funcreadonly = false;
                     StatusPane.thisinstance.currentpagename.setText("");
                  }
               });
               var3.start();
            }

            if (var2 == 2) {
               return false;
            }
         }
      }

      return true;
   }

   public static void save2() {
      EJFileChooser var0 = new EJFileChooser();
      String var1 = SettingsStore.getInstance().get("userpaths", "prop.dynamic.userlastpath.6");
      File var2 = new File((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.import"));
      if (var1 != null) {
         var2 = new File(var1.toString());
      }

      var0.setCurrentDirectory(var2);
      var0.setDialogTitle("XML állomány kiválasztása");
      FileFilter var3 = new FileFilter() {
         public boolean accept(File var1) {
            if (var1.getAbsolutePath().toLowerCase().endsWith(".xml")) {
               return true;
            } else {
               return var1.isDirectory();
            }
         }

         public String getDescription() {
            return "XML állományok";
         }
      };
      var0.removeChoosableFileFilter(var0.getChoosableFileFilters()[0]);
      var0.addChoosableFileFilter(var3);
      int var4 = var0.showSaveDialog(MainFrame.thisinstance);
      if (var4 == 0) {
         File var5 = var0.getSelectedFile();
         if (var5 != null) {
            String var6 = null;

            try {
               var6 = var5.getParent();
            } catch (Exception var13) {
            }

            SettingsStore.getInstance().set("userpaths", "prop.dynamic.userlastpath.6", var6);
            DefaultMultiFormViewer var7 = MainFrame.thisinstance.mp.getDMFV();
            BookModel var8 = var7.bm;
            Ebev var9 = new Ebev(var8);
            String var10 = var5.getAbsolutePath();
            if (!var10.toLowerCase().endsWith(".xml")) {
               var10 = var10 + ".xml";
            }

            MainFrame.thisinstance.setGlassLabel("XML mentése folyamatban!");
            Result var12 = var9.export(var10);
            if (!var12.isOk()) {
               if (var12.errorList.size() == 0) {
                  var12.errorList.add("Hiba történt a művelet elvégzésekor.");
               }

               GuiUtil.showMessageDialog(MainFrame.thisinstance, var12.errorList.get(0), "XML mentés", 0);
            }

            MainFrame.thisinstance.setGlassLabel((String)null);
         }
      }

   }

   private static File handlePackedFile(File var0) throws Exception {
      if (!var0.getName().toLowerCase().endsWith(".xcz")) {
         return var0;
      } else {
         Result var1 = Tools.parseXCZFile2(var0.getAbsolutePath(), "" + System.currentTimeMillis());
         if (var1.isOk()) {
            return new File(var1.errorList.elementAt(0).toString());
         } else {
            throw new Exception(var1.errorList.elementAt(0).toString());
         }
      }
   }

   private static boolean checkChars(String var0) {
      if (var0.indexOf("<") > -1) {
         return false;
      } else if (var0.indexOf(">") > -1) {
         return false;
      } else {
         File var1 = new File(var0);
         return var1.getParentFile().exists();
      }
   }

   public void doneCMD(String var1, FileWriter var2, String var3) throws IOException {
      File var4 = new File(var1);
      if (!var4.exists()) {
         this.wl(var2, "RESULT:ERROR:A " + var1 + " fájl nem létezik!");
      } else if (!var1.toLowerCase().endsWith(".xcz")) {
         this.wl(var2, "RESULT:ERROR:A " + var1 + " fájl nem megfelelő formátumú (xcz)!");
      } else {
         try {
            try {
               var4 = handlePackedFile(var4);
            } catch (Exception var15) {
               deltmpfiles();
               this.wl(var2, "RESULT:ERROR:Hiba a csomagolt fájl feladásakor: " + var15.getMessage());
               return;
            }

            if (var4 == null) {
               deltmpfiles();
               return;
            }

            this.wl(var2, var4.getAbsolutePath());
            String var19 = Tools.clearFileName((new File(var1)).getName());
            if (!(new File(var1)).getName().equals(var19)) {
               deltmpfiles();
               this.wl(var2, "RESULT:ERROR:A " + var1 + " fájlnév problémát okozhat az Ügyfélkapus feladásnál.");
               return;
            }

            if (!check_codepage(var4)) {
               this.wl(var2, "RESULT:ERROR:A megadott állomány nem megfelelő kódolású!- A kódolás csak 'windows-1250' vagy 'utf-8' lehet. ( " + var4 + " )");
               deltmpfiles();
               return;
            }

            Object var6 = new XmlLoader(false);
            if (((XmlLoader)var6).checksuffix(var4)) {
               var6 = new XkrLoader();
            }

            Hashtable var7 = ((XmlLoader)var6).getHeadData(var4);
            if (((XmlLoader)var6).hasError) {
               String var20 = ((XmlLoader)var6).errormsg.substring(((XmlLoader)var6).errormsg.indexOf(":") + 1);
               this.wl(var2, "RESULT:ERROR:Hiba a megadott állomány betöltésekor! ( " + var4 + " ) - Súlyos hiba az XML formai ellenőrzése során:  " + var20);
               deltmpfiles();
               return;
            }

            Hashtable var8 = (Hashtable)var7.get("docinfo");
            Hashtable var9 = new Hashtable();
            var9.put("docinfo", var8);
            Object[] var10 = new Object[]{var4, null, null, var9};
            Object[] var11 = new Object[]{var10};
            Hashtable var12 = new Hashtable();
            var12.put("selected_files", var11);
            var12.put("read_only", Boolean.FALSE);
            var12.put("function_read_only", Boolean.FALSE);
            var12.put("file_status", "");
            xmlbooleancheck = true;
            this.done_load_CMD(var12, var2, var3);
         } catch (Exception var16) {
            Exception var18 = var16;
            this.wl(var2, "RESULT:ERROR:Hiba a megadott állomány betöltésekor! ( " + var4 + " )");

            try {
               this.wl(var2, var18.toString());
            } catch (Exception var14) {
            }

            deltmpfiles();
         } catch (Error var17) {
            Error var5 = var17;
            this.wl(var2, "RESULT:ERROR:Hiba a megadott állomány betöltésekor! ( " + var4 + " )");

            try {
               this.wl(var2, var5.toString());
            } catch (Exception var13) {
            }

            deltmpfiles();
         }

      }
   }

   public void done_load_CMD(Hashtable var1, FileWriter var2, String var3) throws IOException {
      if (var1 != null && var1.size() != 0) {
         Object[] var4 = (Object[])((Object[])var1.get("selected_files"));
         File var5 = (File)((Object[])((Object[])var4[0]))[0];
         boolean var6 = (Boolean)var1.get("read_only");
         boolean var7 = (Boolean)var1.get("function_read_only");
         String var8 = var1.get("file_status").toString();
         Hashtable var9 = (Hashtable)((Hashtable)((Object[])((Object[])var4[0]))[3]).get("docinfo");
         String var10 = (String)var9.get("id");
         String var11 = (String)var9.get("templatever");
         String var12 = (String)var9.get("org");
         XMLFlyCheckLoader var13 = new XMLFlyCheckLoader();
         var13.silentheadcheck = true;
         CalculatorManager.xml = true;
         PropertyList.getInstance().set("prop.dynamic.ilyenkor", "");
         BookModel var14 = var13.load(var5.getAbsolutePath(), var10, var11, var12, true);
         int var15 = var14.carryOnTemplate();
         switch(var15) {
         case 0:
            this.wl(var2, "RESULT:ERROR:" + BookModel.CHECK_VALID_MESSAGES[var15] + "( " + var5 + " )");
            var13.destroy();
            deltmpfiles();
            return;
         case 1:
            int var16 = Tools.handleTemplateCheckerResult(var14);
            if (var16 >= 4) {
               this.wl(var2, "RESULT:ERROR:" + BookModel.CHECK_VALID_MESSAGES[var16] + "( " + var5 + " )");
               var13.destroy();
               deltmpfiles();
               return;
            }
         case 2:
         default:
            break;
         case 3:
            System.out.println("HIBA_AZ_ELLENORZESKOR");
         }

         PropertyList.getInstance().set("prop.dynamic.ilyenkor", (Object)null);
         CalculatorManager.xml = false;
         GuiUtil.setcurrentpagename("");
         var13.silentheadcheck = true;
         xmlbooleancheck = false;
         if (!var14.hasError) {
            if (var14.splitesaver.equalsIgnoreCase("true")) {
               this.wl(var2, "RESULT:ERROR:Az ilyen típusú nyomtatványt (több nyomtatvány egybecsomagolva) nem lehet importálni! - Az xml állomány nem adható fel.( " + var5 + " )");
               var13.destroy();
               deltmpfiles();
               return;
            }

            if (var13.headcheckfatalerror) {
               this.wl(var2, "RESULT:ERROR:A nyomtatvány súlyos hibát tartalmaz! - Az xml állomány nem adható fel.( " + var5 + " )");
               var13.destroy();
               deltmpfiles();
               return;
            }

            if (var13.fatalerror) {
               this.wl(var2, "RESULT:ERROR:A megadott állomány nem létező mezőkódot tartalmaz! - Az xml állomány nem adható fel.( " + var5 + " )");
               var13.destroy();
               deltmpfiles();
               return;
            }

            Error var27;
            Vector var29;
            try {
               CalculatorManager.xml = true;
               var27 = null;
               var29 = var13.errorVector;
               Ebev var17 = new Ebev(var14);
               PropertyList.getInstance().set("prop.dynamic.ebev_call_from_xmlpost", Boolean.TRUE);
               int var19;
               if (var29.size() > 1) {
                  this.wl(var2, "A nyomtatvány ellenőrzése az alábbi hibákat eredményezte:");
                  boolean var18 = false;

                  for(var19 = 0; var19 < var29.size(); ++var19) {
                     if (var29.elementAt(var19) instanceof TextWithIcon && ((TextWithIcon)var29.elementAt(var19)).imageType == 1) {
                        var18 = true;
                     }

                     this.wl(var2, var29.elementAt(var19).toString());
                  }

                  if ("STOP_ON_ERROR".equals(var3)) {
                     var13.destroy();
                     deltmpfiles();
                     this.wl(var2, "RESULT:ERROR:További műveletet nem végzünk.");
                     return;
                  }

                  if (var18) {
                     var13.destroy();
                     deltmpfiles();
                     this.wl(var2, "RESULT:ERROR:A nyomtatvány súlyos hibát tartalmaz, további műveletet nem végzünk.");
                     return;
                  }
               }

               Result var32 = var17.mark(true, true, var29);
               if (!var32.isOk()) {
                  for(var19 = 0; var19 < var32.errorList.size(); ++var19) {
                     this.wl(var2, var32.errorList.elementAt(var19).toString());
                  }

                  var13.destroy();
                  deltmpfiles();
                  this.wl(var2, "RESULT:ERROR:Hiba a kr állomány készítése közben, további műveletet nem végzünk.");
                  return;
               }

               CalculatorManager.xml = false;
            } catch (Exception var25) {
               Exception var28 = var25;
               this.wl(var2, "RESULT:ERROR:Programhiba a kr állomány készítése közben, további műveletet nem végzünk.");

               try {
                  this.wl(var2, var28.toString());
               } catch (Exception var24) {
               }
            } catch (Error var26) {
               var27 = var26;
               this.wl(var2, "RESULT:ERROR:Programhiba a kr állomány készítése közben, további műveletet nem végzünk.");

               try {
                  this.wl(var2, var27.toString());
               } catch (Exception var23) {
               }
            }

            try {
               var29 = (Vector)PropertyList.getInstance().get("prop.dynamic.ebev_call_from_xmlpost");
            } catch (Exception var22) {
               var29 = new Vector();
            }

            if (var29.size() > 0) {
               this.wl(var2, "Az xml állományt sikeresen megjelöltük. Az alábbi csatolmányokat adtuk hozzá:");

               for(int var30 = 0; var30 < var29.size(); ++var30) {
                  this.wl(var2, var29.elementAt(var30).toString());
               }
            }
         } else {
            String[] var31;
            if (var14.isDisabledTemplate()) {
               try {
                  this.wl(var2, "  - Hiba történt a nyomtatvány betöltésekor!");
                  var31 = var14.errormsg.split(" bl_url ");
                  this.wl(var2, "      " + var31[0]);
                  if (var31.length > 1) {
                     this.wl(var2, "      " + var31[1]);
                  }
               } catch (Exception var21) {
               }
            } else {
               try {
                  this.wl(var2, "  - Hiba történt a nyomtatvány betöltésekor! Súlyos hiba az XML formai ellenőrzése során:");
                  var31 = var14.errormsg.split("##");
                  this.wl(var2, "           " + var31[0]);
                  this.wl(var2, "           " + var31[1]);
                  this.wl(var2, "RESULT:ERROR:További műveletet nem végzünk.");
               } catch (Exception var20) {
               }
            }
         }

         PropertyList.getInstance().set("prop.dynamic.ebev_call_from_xmlpost", (Object)null);
         deltmpfiles();
         var13.destroy();
      }

   }

   private void wl(FileWriter var1, String var2) {
      try {
         var1.write(var2);
         var1.write("\n");
      } catch (IOException var4) {
      }

   }

   private static void setEditStateForBetoltErtek(boolean var0) {
      PropertyList.getInstance().set("desktop_edit_state_for_betolt_ertek", var0);
   }
}
