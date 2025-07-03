package hu.piller.enykp.gui.framework;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.fileloader.db.DbLoader;
import hu.piller.enykp.alogic.fileloader.db.DbPkgLoader;
import hu.piller.enykp.alogic.fileloader.db.RDbLoader;
import hu.piller.enykp.alogic.fileloader.imp.MultiImport;
import hu.piller.enykp.alogic.fileloader.xml.XMLFlyCheckLoader;
import hu.piller.enykp.alogic.fileloader.xml.XMLPost;
import hu.piller.enykp.alogic.fileloader.xml.XmlQuickloader;
import hu.piller.enykp.alogic.filepanels.ABEVNewPanel;
import hu.piller.enykp.alogic.filepanels.ABEVOpenPanel;
import hu.piller.enykp.alogic.filepanels.MyABEVNewPanel;
import hu.piller.enykp.alogic.filepanels.attachement.AttachementTool;
import hu.piller.enykp.alogic.filepanels.attachement.Csatolmanyok;
import hu.piller.enykp.alogic.filepanels.attachement.CstParser;
import hu.piller.enykp.alogic.filepanels.batch.BatchCheck;
import hu.piller.enykp.alogic.filepanels.batch.BatchFunctions;
import hu.piller.enykp.alogic.filepanels.mohu.AVDHQuestionDialog;
import hu.piller.enykp.alogic.filepanels.resources.ResourceHandlerUI;
import hu.piller.enykp.alogic.filesaver.db.RDbSaver;
import hu.piller.enykp.alogic.filesaver.enykinner.ENYKClipboardHandler;
import hu.piller.enykp.alogic.filesaver.enykinner.EnykAutoFill;
import hu.piller.enykp.alogic.filesaver.enykinner.EnykInnerSaver;
import hu.piller.enykp.alogic.fileutil.DeleteInPublicMode;
import hu.piller.enykp.alogic.fileutil.ExtendedTemplateData;
import hu.piller.enykp.alogic.fileutil.FileNameResolver;
import hu.piller.enykp.alogic.fileutil.FileStatusChecker;
import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.helppanel.Help;
import hu.piller.enykp.alogic.kontroll.Kontroll;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityException;
import hu.piller.enykp.alogic.masterdata.core.EntityHome;
import hu.piller.enykp.alogic.masterdata.core.MasterData;
import hu.piller.enykp.alogic.masterdata.envelope.Envelope;
import hu.piller.enykp.alogic.masterdata.envelope.model.EnvelopeModel;
import hu.piller.enykp.alogic.masterdata.gui.selector.EntitySelection;
import hu.piller.enykp.alogic.panels.EnvelopePrint;
import hu.piller.enykp.alogic.panels.FormDataListDialog;
import hu.piller.enykp.alogic.settingspanel.BaseSettingsPane;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.alogic.settingspanel.upgrade.UpgradeFunction;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.alogic.templateutils.blacklist.BlacklistStore;
import hu.piller.enykp.datastore.CachedCollection;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.GUI_Datastore;
import hu.piller.enykp.extensions.db.DbFactory;
import hu.piller.enykp.extensions.db.IDbHandler;
import hu.piller.enykp.extensions.db.IDbInfo;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldFactory;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.viewer.DefaultMultiFormViewer;
import hu.piller.enykp.interfaces.ICommandObject;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IOsHandler;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.print.MainPrinter;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.SwingWorker;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import hu.piller.enykp.util.icon.ENYKIconSet;
import hu.piller.enykp.util.oshandler.OsFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class Menubar extends JMenuBar {
   public static Menubar thisinstance;
   public static boolean innewtemplate = false;
   Action newaction;
   Action openaction;
   Action saveaction;
   Action saveasaction;
   Action closeaction;
   Action recalcaction;
   Action checkaction;
   Action setcalcaction;
   Action noteaction;
   Action eraseaction;
   Action printaction;
   Action emptyprintaction;
   Action envprintaction;
   Action exitaction;
   Action externalSignAction;
   Action attachaction;
   Action bigxmlaction;
   Action groupaction;
   Action kontrollaction;
   Action kontrollcopyaction;
   Action adatlistaaction;
   Action gateaction;
   Action resourcesaction;
   Action atcPackCreatorAction;
   Action xmldisplayaction;
   Action xmleditaction;
   Action xmlsaveaction;
   Action xmlqeditaction;
   Action xmlmergeaction;
   Action xmlcloseaction;
   Action gotoaction;
   Action calculatoraction;
   Action detailsaction;
   Action openbarcodeaction;
   Action savebarcodeaction;
   ICommandObject newcmd;
   ICommandObject opencmd;
   ICommandObject savecmd;
   ICommandObject saveascmd;
   ICommandObject closecmd;
   ICommandObject recalccmd;
   ICommandObject checkcmd;
   ICommandObject notecmd;
   ICommandObject erasecmd;
   ICommandObject printcmd;
   ICommandObject emptyprintcmd;
   ICommandObject envprintcmd;
   public ICommandObject exitcmd;
   public ICommandObject setcalccmd;
   ICommandObject attachcmd;
   ICommandObject bigxmlcmd;
   ICommandObject groupcmd;
   ICommandObject kontrollcmd;
   ICommandObject kontrollcopycmd;
   ICommandObject adatlistacmd;
   ICommandObject gatecmd;
   ICommandObject resourcescmd;
   ICommandObject cmdFrissitesek;
   ICommandObject xmldisplaycmd;
   ICommandObject xmleditcmd;
   ICommandObject xmlsavecmd;
   ICommandObject xmlqeditcmd;
   ICommandObject xmlmergecmd;
   ICommandObject xmlclosecmd;
   ICommandObject gotocmd;
   ICommandObject print2cmd;
   public ICommandObject calculatorcmd;
   public ICommandObject detailscmd;
   public ICommandObject openbarcodecmd;
   public ICommandObject savebarcodecmd;
   public Hashtable menuitemht;
   public Hashtable menuitemht2;
   public String menuextratext = "";
   private File tmp_savefile;
   private String menu_adatok = "Adatok";
   private String menu_ellenorzesek = "Ellenőrzések";
   private String mi_new = "Új nyomtatvány";
   private String mi_open = "Nyomtatvány megnyitása";
   private String mi_save = "Nyomtatvány mentése";
   private String mi_saveas = "Nyomtatvány mentése másként";
   private String mi_close = "Nyomtatvány bezárása";
   private String mi_recalc = "Számított mezők újraszámítása";
   private String mi_note = "Megjegyzés";
   private String mi_attach = "Csatolmányok kezelése";
   private String mi_erase = "Nyomtatvány adatainak törlése";
   private String mi_print = "Nyomtatvány kinyomtatása";
   private String mi_check = "Ellenőrzés";
   private String mi_exit = "Kilépés";
   private String mi_bigxml = "XML állomány ellenőrzése és átadása elektronikus beküldésre";
   private String mi_group = "Csoportos műveletek";
   private String mi_kontroll = "Kontroll állományok létrehozása";
   private String mi_kontrollcopy = "Kontroll állományok másolása";
   private String mi_adatlista = "Nyomtatvány adatok listázása";
   private String mi_emptyprint = "Üres nyomtatvány kinyomtatása";
   private String mi_envprint = "Boríték nyomtatása";
   private String mi_resources = "Telepített paraméter-állományok";
   private String mi_xmldisplay = "XML állomány megnyitása megtekintésre";
   private String mi_xmledit = "XML állomány betöltése";
   private String mi_xmlqedit = "XML állomány megnyitása szerkesztésre";
   private String mi_xmlsave = "XML állomány mentése";
   private String mi_xmlmerge = "Járulék típusú XML állományok összemásolása";
   private String mi_xmlclose = "XML állomány bezárása";
   private String mi_goto = "Ugrás mezőre";
   private String mi_setcalc = "Mezőszámítás ki/be kapcsolása";
   private String mi_gate = "Belépés dokumentum feltöltéshez - SZÜF portál";
   private String mi_calculator = "Számológép";
   private String mi_details = "Tételes adatrögzítés";
   private boolean saveInProgress = false;

   public Menubar() {
      this.init();
      this.createActions();
      JMenu var1 = new JMenu(this.menu_adatok);
      JMenuItem var2 = new JMenuItem(this.newaction);
      var2.putClientProperty("code", "new");
      KeyStroke var3 = KeyStroke.getKeyStroke(85, 2);
      var2.setAccelerator(var3);
      this.menuitemht.put(this.newcmd, var2);
      var1.add(var2);
      var2 = new JMenuItem(this.openaction);
      var2.putClientProperty("code", "open");
      var3 = KeyStroke.getKeyStroke(77, 2);
      var2.setAccelerator(var3);
      this.menuitemht.put(this.opencmd, var2);
      var1.add(var2);
      var2 = new JMenuItem(this.saveaction);
      var2.putClientProperty("code", "save");
      var3 = KeyStroke.getKeyStroke(69, 2);
      var2.setAccelerator(var3);
      this.menuitemht.put(this.savecmd, var2);
      var1.add(var2);
      var2 = new JMenuItem(this.saveasaction);
      var2.putClientProperty("code", "saveas");
      this.menuitemht.put(this.saveascmd, var2);
      var1.add(var2);
      var2 = new JMenuItem(this.closeaction);
      var2.putClientProperty("code", "close");
      var3 = KeyStroke.getKeyStroke(66, 2);
      var2.setAccelerator(var3);
      this.menuitemht.put(this.closecmd, var2);
      var1.add(var2);
      var2 = new JMenuItem(this.recalcaction);
      var2.putClientProperty("code", "recalc");
      var3 = KeyStroke.getKeyStroke(83, 2);
      var2.setAccelerator(var3);
      this.menuitemht.put(this.recalccmd, var2);
      var1.add(var2);
      var1.add(new JSeparator());
      var2 = new JMenuItem(this.noteaction);
      var2.putClientProperty("code", "note");
      this.menuitemht.put(this.notecmd, var2);
      var1.add(var2);
      var2 = new JMenuItem(this.eraseaction);
      var2.putClientProperty("code", "erase");
      this.menuitemht.put(this.erasecmd, var2);
      var1.add(var2);
      var2 = new JMenuItem(this.gotoaction);
      var2.putClientProperty("code", "goto");
      var3 = KeyStroke.getKeyStroke(71, 2);
      var2.setAccelerator(var3);
      this.menuitemht.put(this.gotocmd, var2);
      var1.add(var2);
      var1.add(new JSeparator());
      var2 = new JMenuItem(this.attachaction);
      var2.putClientProperty("code", "attach");
      this.menuitemht.put(this.attachcmd, var2);
      var1.add(var2);
      var1.add(new JSeparator());
      var2 = new JMenuItem(this.xmlqeditaction);
      var2.putClientProperty("code", "xmlqedit");
      var3 = KeyStroke.getKeyStroke(90, 2);
      var2.setAccelerator(var3);
      this.menuitemht.put(this.xmlqeditcmd, var2);
      var1.add(var2);
      var2 = new JMenuItem(this.xmlsaveaction);
      var2.putClientProperty("code", "xmlsave");
      this.menuitemht.put(this.xmlsavecmd, var2);
      var1.add(var2);
      var2 = new JMenuItem(this.xmlcloseaction);
      var2.putClientProperty("code", "xmlclose");
      var3 = KeyStroke.getKeyStroke(87, 2);
      var2.setAccelerator(var3);
      this.menuitemht.put(this.xmlclosecmd, var2);
      var1.add(var2);
      var2 = new JMenuItem(this.xmldisplayaction);
      var2.putClientProperty("code", "xmldisplay");
      this.menuitemht.put(this.xmldisplaycmd, var2);
      var1.add(var2);
      var2 = new JMenuItem(this.xmlmergeaction);
      var2.putClientProperty("code", "xmlmerge");
      this.menuitemht.put(this.xmlmergecmd, var2);
      var1.add(var2);
      var2 = new JMenuItem(this.bigxmlaction);
      var2.putClientProperty("code", "bigxml");
      var3 = KeyStroke.getKeyStroke(68, 2);
      var2.setAccelerator(var3);
      this.menuitemht.put(this.bigxmlcmd, var2);
      var1.add(var2);
      var1.add(new JSeparator());
      var2 = new JMenuItem(this.groupaction);
      var2.putClientProperty("code", "group");
      this.menuitemht.put(this.groupcmd, var2);
      var1.add(var2);
      var1.add(new JSeparator());
      var2 = new JMenuItem(this.adatlistaaction);
      var2.putClientProperty("code", "adatlista");
      this.menuitemht.put(this.adatlistacmd, var2);
      var1.add(var2);
      var1.add(new JSeparator());
      var2 = new JMenuItem(this.emptyprintaction);
      var2.putClientProperty("code", "emptyprint");
      this.menuitemht.put(this.emptyprintcmd, var2);
      var1.add(var2);
      var2 = new JMenuItem(this.printaction);
      var2.putClientProperty("code", "print");
      var3 = KeyStroke.getKeyStroke(78, 2);
      var2.setAccelerator(var3);
      this.menuitemht.put(this.printcmd, var2);
      var1.add(var2);
      var2 = new JMenuItem(this.envprintaction);
      var2.putClientProperty("code", "envprint");
      this.menuitemht.put(this.envprintcmd, var2);
      var1.add(var2);
      var1.add(new JSeparator());
      var2 = new JMenuItem(this.exitaction);
      var2.putClientProperty("code", "exit");
      this.menuitemht.put(this.exitcmd, var2);
      var1.add(var2);
      this.add(var1);
      var1 = new JMenu(this.menu_ellenorzesek);
      var2 = new JMenuItem(this.checkaction);
      var2.putClientProperty("code", "check");
      var3 = KeyStroke.getKeyStroke(82, 2);
      var2.setAccelerator(var3);
      this.menuitemht.put(this.checkcmd, var2);
      var1.add(var2);
      this.add(var1);
      UgyfelkapuMenuItem var4 = new UgyfelkapuMenuItem(thisinstance);
      this.add(var4.getMainMenuItem(this.menuitemht));
      String var10 = SettingsStore.getInstance().get("gui", "cegkapu");
      if (var10 != null && var10.equals("true")) {
         CegkapuMenuItem var5 = new CegkapuMenuItem(thisinstance);
         this.add(var5.getMainMenuItem(this.menuitemht));
      }

      ServiceMenuItem var11 = new ServiceMenuItem(thisinstance);
      var11.getMainMenuItem(this.menuitemht);
      this.cmdFrissitesek = var11.cmdFrissitesek;

      for(int var12 = 0; var12 < this.getMenuCount(); ++var12) {
         JMenu var6 = this.getMenu(var12);
         var6.setName(var6.getText());
         Component[] var7 = var6.getMenuComponents();

         for(int var8 = 0; var8 < var7.length; ++var8) {
            Component var9 = var7[var8];
            if (var9 instanceof JMenuItem) {
               var9.setName(((JMenuItem)var9).getText());
            }

            if (var9 instanceof JMenu) {
               var9.setName(((JMenu)var9).getText());
            }
         }
      }

   }

   private void init() {
      thisinstance = this;
      this.menuitemht = new Hashtable();
      this.menuitemht2 = new Hashtable();
      InputMap var1 = this.getInputMap(2);
      ActionMap var2 = this.getActionMap();
      Object var3 = var1.get(KeyStroke.getKeyStroke("F10"));
      final Action var4 = var2.get(var3);
      AbstractAction var5 = new AbstractAction("FilterTakeFocus", (Icon)null) {
         public void actionPerformed(ActionEvent var1) {
            if (!MainFrame.thisinstance.getGlassPane().isVisible()) {
               var4.actionPerformed(new ActionEvent(Menubar.thisinstance, 0, (String)null));
            }
         }
      };
      var2.put(var3, var5);
   }

   public Menubar(String var1) {
      try {
         this.init();
         this.createActions();
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      try {
         if (DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.DefaultDbHandler") != null) {
            this.openbarcodecmd = new ICommandObject() {
               public void execute() {
                  RDbLoader var1 = new RDbLoader();
                  var1.done();
               }

               public void setParameters(Hashtable var1) {
               }

               public Object getState(Object var1) {
                  return Boolean.TRUE;
               }
            };
            this.openbarcodeaction = new AbstractAction("Megnyitás bárkód szerint", (Icon)null) {
               public void actionPerformed(ActionEvent var1) {
                  Menubar.this.openbarcodecmd.execute();
               }
            };
            this.savebarcodecmd = new ICommandObject() {
               public void execute() {
                  RDbSaver var1 = new RDbSaver();
                  if (MainFrame.opmode != null && !MainFrame.opmode.equals("0")) {
                     System.out.println("calling_check_all - Menu_SaveAndExit");
                  }

                  int var2 = var1.save();
                  if (var2 == 0) {
                     Menubar.this.tmp_savefile = new NecroFile("");
                  } else {
                     Menubar.this.tmp_savefile = null;
                  }

               }

               public void execute1() {
                  MainFrame.thisinstance.setGlassLabel("Mentés folyamatban!");
                  Runnable var1 = new Runnable() {
                     public void run() {
                        RDbSaver var1 = new RDbSaver();
                        int var2 = var1.save();
                        if (var2 == 0) {
                           Menubar.this.tmp_savefile = new NecroFile("");
                        } else {
                           Menubar.this.tmp_savefile = null;
                        }

                        MainFrame.thisinstance.setGlassLabel((String)null);
                     }
                  };
                  Thread var2 = new Thread(var1);
                  var2.start();
               }

               public void setParameters(Hashtable var1) {
               }

               public Object getState(Object var1) {
                  try {
                     int var2 = (Integer)var1;
                     switch(var2) {
                     case 0:
                        return Boolean.FALSE;
                     case 1:
                        return Boolean.TRUE;
                     case 2:
                     default:
                        break;
                     case 3:
                        return Boolean.FALSE;
                     }
                  } catch (Exception var3) {
                  }

                  return Boolean.FALSE;
               }
            };
            this.savecmd = this.savebarcodecmd;
            this.savebarcodeaction = new AbstractAction("Mentés bárkód szerint", (Icon)null) {
               public void actionPerformed(ActionEvent var1) {
                  Menubar.this.savebarcodecmd.execute();
               }
            };
            this.exitcmd = new ICommandObject() {
               public void execute() {
                  Thread var1 = new Thread(new Runnable() {
                     public void run() {
                        execute2();
                     }
                  });
                  var1.start();
               }

               public void execute2() {
                  int var1 = Menubar.this.beforedialog();
                  if (var1 != 2) {
                     if (MainFrame.publicmode) {
                        boolean var2 = DeleteInPublicMode.getInstance().exit(false);
                        if (!var2) {
                           return;
                        }
                     }

                     try {
                        DefaultMultiFormViewer var10 = MainFrame.thisinstance.mp.getDMFV();
                        if (var10 != null && var10.bm.dbpkgloader != null) {
                           var10.bm.dbpkgloader.close();
                        }
                     } catch (Exception var9) {
                        var9.printStackTrace();
                     }

                     SettingsStore var11 = SettingsStore.getInstance();

                     try {
                        var11.set("gui", "x", MainFrame.thisinstance.getLocation().x + "");
                        var11.set("gui", "y", MainFrame.thisinstance.getLocation().y + "");
                        var11.set("gui", "w", MainFrame.thisinstance.getWidth() + "");
                        var11.set("gui", "h", MainFrame.thisinstance.getHeight() + "");
                     } catch (Exception var8) {
                        Tools.eLog(var8, 0);
                     }

                     SettingsStore.getInstance().save();
                     boolean var3 = true;
                     Object var4 = PropertyList.getInstance().get("no_exit");
                     if (var4 != null) {
                        var3 = false;
                     }

                     if (var3) {
                        try {
                           MainFrame var10000 = MainFrame.thisinstance;
                           MainFrame.runfile.delete();
                        } catch (Exception var7) {
                        }

                        try {
                           MainFrame.deleteCalculationRunFile();
                        } catch (Exception var6) {
                        }

                        System.exit(0);
                     } else {
                        MainFrame.thisinstance.setVisible(false);
                        MainFrame.thisinstance.dispose();
                     }

                  }
               }

               public void setParameters(Hashtable var1) {
               }

               public Object getState(Object var1) {
                  return Boolean.TRUE;
               }
            };
            final ICommandObject var2 = new ICommandObject() {
               public void execute() {
                  try {
                     DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
                     if (var1 != null) {
                        BookModel var2 = var1.bm;
                        Elem var3 = (Elem)var2.cc.getActiveObject();
                        String[] var4 = (String[])((String[])var3.getEtc().get("dbparams"));
                        BigDecimal var5 = new BigDecimal(var4[1]);
                        IDbHandler var6 = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.DefaultDbHandler");
                        IDbInfo var7 = var6.getJavitandoBizresz(var5, 2, (String)null, (String)null, (String)null);
                        DefaultTableModel var8 = var7.getTable();
                        if (var8.getDataVector().size() == 0) {
                           if (var2.isSingle()) {
                              GuiUtil.showMessageDialog(MainFrame.thisinstance, "A bizonylatnak nincs tárolt hibája!", "Üzenet", 1);
                           } else {
                              GuiUtil.showMessageDialog(MainFrame.thisinstance, "A részbizonylatnak nincs tárolt hibája!", "Üzenet", 1);
                           }
                        } else {
                           new ErrorDialog(MainFrame.thisinstance, "Hibalista", false, var8);
                        }
                     }
                  } catch (Exception var9) {
                  }

               }

               public void setParameters(Hashtable var1) {
               }

               public Object getState(Object var1) {
                  return Boolean.TRUE;
               }
            };
            AbstractAction var3 = new AbstractAction("Hibalista", (Icon)null) {
               public void actionPerformed(ActionEvent var1) {
                  var2.execute();
               }
            };
            this.exitaction = new AbstractAction("Kilépés", (Icon)null) {
               public void actionPerformed(ActionEvent var1) {
                  Menubar.this.exitcmd.execute();
               }
            };
            JMenu var4;
            JMenuItem var5;
            if (MainFrame.readonlymodefromubev) {
               var4 = new JMenu(this.menu_adatok);
               var5 = new JMenuItem(this.printaction);
               var4.add(var5);
               this.menuitemht.put(this.printcmd, var5);
               var5 = new JMenuItem(this.xmlsaveaction);
               var4.add(var5);
               this.menuitemht.put(this.xmlsavecmd, var5);
               var5 = new JMenuItem(var3);
               var4.add(var5);
               this.menuitemht.put(var2, var5);
               var4.addSeparator();
               var5 = new JMenuItem(this.exitaction);
               var4.add(var5);
               this.menuitemht.put(this.exitcmd, var5);
               this.add(var4);
               return;
            }

            if (MainFrame.rogzitomode) {
               var4 = new JMenu(this.menu_adatok);
               var5 = new JMenuItem(this.gotoaction);
               var5.putClientProperty("code", "goto");
               KeyStroke var6 = KeyStroke.getKeyStroke(71, 2);
               var5.setAccelerator(var6);
               this.menuitemht.put(this.gotocmd, var5);
               var4.add(var5);
               var5 = new JMenuItem(this.exitaction);
               var4.add(var5);
               this.menuitemht.put(this.exitcmd, var5);
               this.add(var4);
               return;
            }

            var4 = new JMenu(this.menu_adatok);
            var5 = new JMenuItem(this.recalcaction);
            var4.add(var5);
            this.menuitemht.put(this.recalccmd, var5);
            var5 = new JMenuItem(this.checkaction);
            var4.add(var5);
            this.menuitemht.put(this.checkcmd, var5);
            var4.addSeparator();
            var5 = new JMenuItem(this.printaction);
            var4.add(var5);
            this.menuitemht.put(this.printcmd, var5);
            var4.addSeparator();
            var5 = new JMenuItem(this.xmlsaveaction);
            var4.add(var5);
            this.menuitemht.put(this.xmlsavecmd, var5);
            var4.addSeparator();
            var5 = new JMenuItem(this.exitaction);
            var4.add(var5);
            this.menuitemht.put(this.exitcmd, var5);
            this.add(var4);
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   private void createActions() {
      ENYKIconSet var1 = ENYKIconSet.getInstance();
      this.newcmd = new ICommandObject() {
         public void execute() {
            try {
               int var1 = Menubar.this.beforedialog();
               if (var1 == 2) {
                  return;
               }

               MyABEVNewPanel var2 = new MyABEVNewPanel();
               final Hashtable var3 = var2.showDialog();

               try {
                  String[] var4 = Menubar.this.getDocAndOrgId(var3.get("selected_template_docinfo"));
                  if (BlacklistStore.getInstance().handleGuiMessage(var4[0], var4[1])) {
                     return;
                  }
               } catch (Exception var6) {
               }

               if (var3 != null && var3.size() != 0) {
                  DefaultMultiFormViewer var8 = MainFrame.thisinstance.mp.getDMFV();
                  if (var8 != null) {
                     var8.bm.destroy();
                     MainFrame.thisinstance.mp.intoleftside(new JPanel());
                     var8.bm = null;
                     DataFieldFactory.getInstance().freemem();
                     MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                     MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                     MainFrame.thisinstance.mp.readonlymode = false;
                     MainFrame.thisinstance.mp.funcreadonly = false;
                     MainFrame.thisinstance.mp.forceDisabledPageShowing = false;
                     MainPanel var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                     MainFrame.thisinstance.mp.set_kiut_url((String)null);
                  }

                  XMLPost.xmleditnemjo = false;
                  MainFrame.thisinstance.setGlassLabel("Betöltés folyamatban!");
                  Thread var5 = new Thread(new Runnable() {
                     public void run() {
                        Menubar.this.setEditStateForBetoltErtek(true);
                        Object[] var1 = (Object[])((Object[])var3.get("selected_file"));
                        File var2 = (File)var1[0];
                        Object var3x = var3.get("primary_account");
                        Object var4 = var3.get("tax_expert");
                        String var5 = var3.get("file_status").toString();
                        final BookModel var6 = new BookModel(var2);
                        if (!var6.hasError) {
                           var6.setPAInfo((Object)null, (Object)null);
                           if (var3.get("selections") != null) {
                              var6.setSelectedEntities((EntitySelection[])((EntitySelection[])var3.get("selections")));
                           }

                           Menubar.innewtemplate = true;
                           DefaultMultiFormViewer var7 = new DefaultMultiFormViewer(var6);
                           Menubar.innewtemplate = false;
                           MainPanel var10001;
                           if (var7.fv.getTp().getTabCount() == 0) {
                              var6.destroy();
                              var10001 = MainFrame.thisinstance.mp;
                              MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                              MainFrame.thisinstance.setGlassLabel((String)null);
                              MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                              MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                              MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                              return;
                           }

                           if (var3x.equals("(Nincs kiválasztva)") && var4.equals("(Nincs kiválasztva)")) {
                              var6.dirty = false;
                           }

                           try {
                              if (var3x.equals("") && var4.equals("") && MainFrame.FTRmode) {
                                 DocumentBuilderFactory var8 = DocumentBuilderFactory.newInstance();
                                 DocumentBuilder var9 = var8.newDocumentBuilder();
                                 MainFrame.FTRdoc = var9.newDocument();
                                 MainFrame.FTRroot = MainFrame.FTRdoc.createElement("nyomtatvany");
                                 MainFrame.FTRroot.setAttribute("multi", var6.isSingle() ? "nem" : "igen");
                                 MainFrame.FTRdoc.appendChild(MainFrame.FTRroot);
                                 Element var10 = MainFrame.FTRdoc.createElement("leiras");
                                 MainFrame.FTRroot.appendChild(var10);
                                 Text var11 = MainFrame.FTRdoc.createTextNode("Megjegyzést lehet írni");
                                 var10.appendChild(var11);
                                 var10 = MainFrame.FTRdoc.createElement("template");
                                 var11 = MainFrame.FTRdoc.createTextNode(var2.getName());
                                 var10.appendChild(var11);
                                 MainFrame.FTRroot.appendChild(var10);
                                 MainFrame.FTRmezok = MainFrame.FTRdoc.createElement("mezok");
                                 MainFrame.FTRmezok.setAttribute("formid", var6.get(0).toString());
                                 MainFrame.FTRroot.appendChild(MainFrame.FTRmezok);
                              }
                           } catch (ParserConfigurationException var12) {
                           }

                           try {
                              MainFrame.thisinstance.mp.intoleftside(var7);
                              int var14 = var6.carryOnTemplate();
                              String var16;
                              if (var14 == 0) {
                                 System.out.println("VISSZAVONVA");
                                 MainFrame.thisinstance.mp.funcreadonly = true;
                                 var10001 = MainFrame.thisinstance.mp;
                                 MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
                                 var16 = BookModel.CHECK_VALID_MESSAGES[var14];
                                 MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var16);
                              } else {
                                 int var15 = Tools.handleTemplateCheckerResultNew(var6);
                                 if (var14 == 1 && var15 >= 4) {
                                    MainFrame.thisinstance.mp.funcreadonly = true;
                                    var10001 = MainFrame.thisinstance.mp;
                                    MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
                                    var16 = BookModel.CHECK_VALID_MESSAGES[var15];
                                    MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var16);
                                 } else {
                                    MainFrame.thisinstance.mp.funcreadonly = false;
                                    var10001 = MainFrame.thisinstance.mp;
                                    MainFrame.thisinstance.mp.setstate(MainPanel.NORMAL);
                                    MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var5);
                                 }
                              }

                              MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("Ny:" + (String)var6.docinfo.get("ver"));
                              BaseSettingsPane.done_menuextratext(true);
                           } catch (Exception var13) {
                              MainFrame.thisinstance.mp.intoleftside(new JPanel());
                              var7.bm = null;
                              var6.destroy();
                              SwingUtilities.invokeLater(new Runnable() {
                                 public void run() {
                                    MainFrame.thisinstance.setGlassLabel((String)null);
                                    Menubar.this.newcmd.execute();
                                 }
                              });
                           }

                           MainFrame.thisinstance.setGlassLabel((String)null);
                           Menubar.this.done_epost_dialog(var6);
                           if (var6.isAvdhModel()) {
                              Menubar.this.done_avdh_dialog(var6.id);
                           }
                        } else {
                           var6.destroy();
                           SwingUtilities.invokeLater(new Runnable() {
                              public void run() {
                                 MainFrame.thisinstance.setGlassLabel((String)null);
                                 Menubar.this.newcmd.execute();
                                 Menubar.this.done_epost_dialog(var6);
                              }
                           });
                        }

                     }
                  });
                  var5.start();
               }
            } catch (Exception var7) {
               var7.printStackTrace();
            }

         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            return XMLPost.xmleditnemjo ? Boolean.FALSE : Boolean.TRUE;
         }
      };
      this.opencmd = new ICommandObject() {
         public void execute() {
            final ExtendedTemplateData[] var1 = new ExtendedTemplateData[]{new ExtendedTemplateData("", "")};
            SwingWorker var2 = new SwingWorker() {
               public Object construct() {
                  try {
                     int var1x = Menubar.this.beforedialog();
                     if (var1x == 2) {
                        return null;
                     }

                     MainFrame.thisinstance.setGlassLabel("Listakészítés folyamatban!");
                     ABEVOpenPanel var2 = new ABEVOpenPanel();
                     var2.setMode("open");
                     var2.setPath(new NecroFile((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.saves")));
                     var2.setFilters(new String[]{"inner_data_loader_v1"});
                     Hashtable var3 = var2.showDialog();
                     if (var3 != null) {
                        if (var3.size() != 0) {
                           DefaultMultiFormViewer var4 = MainFrame.thisinstance.mp.getDMFV();
                           Object[] var5 = (Object[])((Object[])var3.get("selected_files"));
                           final File var6 = (File)((Object[])((Object[])var5[0]))[0];
                           final boolean var7 = (Boolean)var3.get("read_only");
                           final boolean var8 = (Boolean)var3.get("function_read_only");
                           final String var9 = var3.get("file_status").toString();
                           Hashtable var10 = (Hashtable)((Hashtable)((Object[])((Object[])var5[0]))[3]).get("docinfo");
                           if (var10 == null || var10.size() == 0) {
                              GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem meghatározható a nyomtatvány típusa!\nFeltehetőleg sérült az állomány!", "Hibaüzenet", 0);
                              return null;
                           }

                           String var11 = (String)var10.get("id");
                           String var12 = (String)var10.get("templatever");
                           String var13 = (String)var10.get("org");
                           PropertyList.getInstance().set("prop.dynamic.ilyenkor", "");
                           TemplateChecker.getInstance().setGetTemplateIfHavent(0);
                           var1[0] = TemplateChecker.getInstance().getTemplateFilenameWithDialog(var11, var12, var13, UpgradeFunction.OPEN);
                           final File var14 = var1[0].getTemplateFile();
                           if (var14 == null) {
                              return null;
                           }

                           TemplateChecker.getInstance().setGetTemplateIfHavent(1);
                           PropertyList.getInstance().set("prop.dynamic.ilyenkor", (Object)null);
                           final BookModel var15 = var4 != null ? var4.bm : null;
                           boolean var16;
                           if (var4 != null) {
                              if (var4.bm.loadedfile != null && var14 != null) {
                                 if (!var4.bm.loadedfile.getAbsolutePath().equals(var14.getAbsolutePath())) {
                                     var16 = false;
                                     var4.bm.destroy();
                                 } else {
                                    var15.reempty();
                                    var16 = true;
                                 }
                              } else {
                                  var16 = false;
                                  var4.bm.destroy();
                              }

                              MainFrame.thisinstance.mp.intoleftside(new JPanel());
                              var4.bm = null;
                              DataFieldFactory.getInstance().freemem();
                              MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                              MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                              MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                              MainFrame.thisinstance.mp.readonlymode = false;
                              MainFrame.thisinstance.mp.forceDisabledPageShowing = false;
                              MainFrame.thisinstance.mp.funcreadonly = false;
                              MainPanel var10001 = MainFrame.thisinstance.mp;
                              MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                              MainFrame.thisinstance.mp.set_kiut_url((String)null);
                           } else {
                               var16 = false;
                           }

                            XMLPost.xmleditnemjo = false;
                           MainFrame.thisinstance.setGlassLabel("Betöltés folyamatban!");
                           Thread var19 = new Thread(new Runnable() {
                              public void run() {
                                 Menubar.this.setEditStateForBetoltErtek(!var7 && !var8);
                                 if (var14 != null) {
                                    BookModel var1x;
                                    if (var16) {
                                       var1x = var15;
                                       var1x.reload(var6, false);
                                    } else {
                                       var1x = new BookModel(var14, var6);
                                    }

                                    MainPanel var10001;
                                    if (!var1x.hasError) {
                                       DefaultMultiFormViewer var2 = new DefaultMultiFormViewer(var1x);
                                       if (var2.fv.getTp().getTabCount() == 0) {
                                          var1x.destroy();
                                          var10001 = MainFrame.thisinstance.mp;
                                          MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                                          MainFrame.thisinstance.setGlassLabel((String)null);
                                          MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                                          MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                                          MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                                          return;
                                       }

                                       MainFrame.thisinstance.mp.intoleftside(var2);
                                       int var3 = var1x.carryOnTemplate();
                                       System.out.println("TEMPLATE_CHECK_MENU : " + var3);
                                       boolean var4 = true;
                                       int var6x;
                                       String var7x;
                                       switch(var3) {
                                       case 0:
                                          MainFrame.thisinstance.mp.funcreadonly = true;
                                          var10001 = MainFrame.thisinstance.mp;
                                          MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
                                          var7x = BookModel.CHECK_VALID_MESSAGES[var3];
                                          MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var7x);
                                          var6x = 0;
                                          break;
                                       case 1:
                                          var6x = Tools.handleTemplateCheckerResult(var1x);
                                          System.out.println("TEMPLATE_CHECK_MENU_2 : " + var6x);
                                          if (var6x >= 4) {
                                             MainFrame.thisinstance.mp.funcreadonly = true;
                                             var10001 = MainFrame.thisinstance.mp;
                                             MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
                                             var7x = BookModel.CHECK_VALID_MESSAGES[var6x];
                                             MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var7x);
                                          } else {
                                             var6x = -1;
                                          }
                                          break;
                                       case 2:
                                       default:
                                          var6x = -1;
                                          break;
                                       case 3:
                                          var6x = -1;
                                          System.out.println("HIBA_AZ_ELLENORZESKOR");
                                       }

                                       if (var6x == -1) {
                                          MainFrame.thisinstance.mp.funcreadonly = var8;
                                          if (var7) {
                                             var10001 = MainFrame.thisinstance.mp;
                                             MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
                                          } else {
                                             var10001 = MainFrame.thisinstance.mp;
                                             MainFrame.thisinstance.mp.setstate(MainPanel.NORMAL);
                                          }

                                          MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var9);
                                       }

                                       if (var16) {
                                          StatusPane.thisinstance.currentpagename.setText(var1x.name + " v:" + var1x.docinfo.get("ver"));
                                       }

                                       if ("0".equals(MainFrame.role)) {
                                          boolean var5 = (Boolean)PropertyList.getInstance().get("prop.dynamic.hasNewTemplate");
                                          if (var5) {
                                             CalculatorManager.getInstance().multiform_calc();
                                             GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az új sablon miatt, a mezők újraszámítása megtörtént a nyomtatványon!", "Üzenet", 1);
                                          }
                                       }

                                       if (var1[0].isTemplateDisaled()) {
                                          TemplateUtils.getInstance().handleDisabledTemplates(var1[0].getTemplateId(), var1[0].getOrgId());
                                          var1x.setDisabledTemplate(true);
                                       }
                                    } else {
                                       var1x.destroy();
                                       var10001 = MainFrame.thisinstance.mp;
                                       MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                                    }

                                    MainFrame.thisinstance.setGlassLabel((String)null);
                                    TemplateUtils.getInstance().handleDisabledTemplateMessage(var1x);
                                 } else {
                                    SwingUtilities.invokeLater(new Runnable() {
                                       public void run() {
                                          MainFrame.thisinstance.setGlassLabel((String)null);
                                          Menubar.this.opencmd.execute();
                                       }
                                    });
                                 }

                              }
                           });
                           var19.start();
                        } else {
                           MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                           MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                           MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                        }
                     }
                  } catch (Exception var20) {
                     var20.printStackTrace();
                  }

                  return null;
               }
            };
            var2.start();
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            return XMLPost.xmleditnemjo ? Boolean.FALSE : Boolean.TRUE;
         }
      };
      this.savecmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (!var1.fv.pv.pv.leave_component()) {
                  return;
               }

               BookModel var2 = var1.bm;
               String var3;
               if (var2.cc.getLoadedfile() == null) {
                  FileNameResolver var4 = new FileNameResolver(var2);
                  var3 = var4.generateFileName();
               } else {
                  var3 = var2.cc.getLoadedfile().getAbsolutePath();
               }

               try {
                  EnykInnerSaver var12 = new EnykInnerSaver(var2);
                  File var5 = var12.save(var3, -1);
                  Menubar.this.tmp_savefile = var5;
                  if (var5 != null) {
                     var2.cc.setLoadedfile(var5);
                     var2.dirty = false;
                     String var6 = null;

                     try {
                        var6 = (String)((Vector)PropertyList.getInstance().get("prop.const.fileNameToClipBoard")).get(0);
                     } catch (Exception var10) {
                        var6 = "";
                     }

                     if ("igen".equals(var6.toLowerCase())) {
                        ENYKClipboardHandler var7 = new ENYKClipboardHandler();
                        var7.setClipboardContents(var5.toString());
                        String var8 = var7.getClipboardContents();
                        String var9;
                        if (var8.equals(var5.toString())) {
                           var9 = "Az állomány nevének vágólapra másolása sikeres!\n\n" + var5.getParent() + "\n" + var5.getName();
                           GuiUtil.showMessageDialog(MainFrame.thisinstance, var9, "Üzenet", 1);
                        } else {
                           var9 = "Az állomány nevének vágólapra másolása sikertelen!";
                           GuiUtil.showMessageDialog(MainFrame.thisinstance, var9, "Üzenet", 0);
                        }
                     }
                  }
               } catch (Exception var11) {
                  var11.printStackTrace();
               }
            }

         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  if (XMLPost.xmleditnemjo) {
                     return Boolean.FALSE;
                  }

                  return Boolean.TRUE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }
      };
      this.saveascmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (!var1.fv.pv.pv.leave_component()) {
                  return;
               }

               BookModel var2 = var1.bm;
               Object var4 = null;
               FileNameResolver var5 = new FileNameResolver(var2);
               String var3 = var5.generateFileName();
               var3 = (String)JOptionPane.showInputDialog(MainFrame.thisinstance, "A mentendő nyomtatvány állomány új neve:                                          ", "Mentés másként", 3, (Icon)null, (Object[])null, var3);
               if (var3 == null) {
                  return;
               }

               String[] var6 = new String[]{"Igen", "Nem"};
               if (var3.endsWith(".frm.enyk".toUpperCase())) {
                  var3 = var3.substring(0, var3.lastIndexOf(".frm.enyk".toUpperCase()));
               }

               if (!var3.endsWith(".frm.enyk")) {
                  var3 = var3 + ".frm.enyk";
               }

               String var7 = var5.normalizeString(var3.substring(0, var3.lastIndexOf(".frm.enyk"))) + ".frm.enyk";
               boolean var8 = false;
               if (!var3.equals(var7)) {
                  var8 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "A " + var3 + " fájlnév problémát okozhat az Ügyfélkapus feladásnál. Átnevezzük " + var7 + " -re?\nAmennyiben a Nem-et választja, Önnek kell átneveznie a fájlt.", "Fájl átnevezés", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0;
                  if (!var8) {
                     return;
                  }

                  var3 = var7;
               }

               File var9 = new NecroFile(var3);
               if (var9.getParent() == null) {
                  if (var4 == null) {
                     var3 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.saves") + File.separator + var3;
                  } else {
                     var3 = (new NecroFile((File)var4, var3)).getAbsolutePath();
                  }
               }

               var9 = new NecroFile(var3);
               if (AttachementTool.hasAttachement(var2, var9)) {
                  String var20 = "A " + var3 + " állomány névhez csatolmány tartozik. Kérem adjon meg másik nevet!";
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, var20, "Üzenet", 0);
                  return;
               }

               int var11;
               if (var9.exists()) {
                  int var10 = FileStatusChecker.getInstance().getStatus(var3, (String)((String)(var2.cc.docinfo.containsKey("krfilename") ? var2.cc.docinfo.get("krfilename") : "")));
                  if (var10 == 2 || var10 == 1) {
                     String var21 = "Már létező állomány nevet adott meg: " + var3 + "\nAz állomány állapota: " + FileStatusChecker.getStringStatus(var10) + "\nKérem adjon meg másik nevet!";
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, var21, "Üzenet", 0);
                     return;
                  }

                  var11 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "A megadott állomány létezik!\nFelülírja?", "Kérdés", 0, 3, (Icon)null, var6, var6[0]);
                  if (var11 == 1) {
                     return;
                  }
               }

               try {
                  var2.cc.docinfo.remove("krfilename");
                  var2.cc.docinfo.remove("attachment_count");
                  EnykInnerSaver var19 = new EnykInnerSaver(var2);
                  var9 = var19.save(var3, -1);
                  Menubar.this.tmp_savefile = var9;
                  if (var9 != null) {
                     var2.cc.setLoadedfile(var9);
                     var2.dirty = false;
                     var11 = var2.carryOnTemplate();
                     boolean var12 = true;
                     MainPanel var10001;
                     int var22;
                     String var23;
                     switch(var11) {
                     case 0:
                        MainFrame.thisinstance.mp.funcreadonly = true;
                        var10001 = MainFrame.thisinstance.mp;
                        MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
                        var23 = BookModel.CHECK_VALID_MESSAGES[var11];
                        MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var23);
                        var22 = 0;
                        break;
                     case 1:
                        var22 = Tools.handleTemplateCheckerResult(var2);
                        if (var22 >= 4) {
                           MainFrame.thisinstance.mp.funcreadonly = true;
                           var10001 = MainFrame.thisinstance.mp;
                           MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
                           var23 = BookModel.CHECK_VALID_MESSAGES[var22];
                           MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var23);
                        } else {
                           var22 = -1;
                        }
                        break;
                     case 2:
                     default:
                        var22 = -1;
                        break;
                     case 3:
                        var22 = -1;
                        System.out.println("HIBA_AZ_ELLENORZESKOR");
                     }

                     String var13;
                     if (var22 == -1) {
                        if (var2 != null && BlacklistStore.getInstance().handleGuiMessage(var2.getTemplateId(), var2.getOrgId())) {
                           TemplateUtils.getInstance().handleDisabledTemplates(var2.getTemplateId(), var2.getOrgId());
                        } else {
                           MainFrame.thisinstance.mp.setReadonly(false);
                           MainFrame.thisinstance.mp.funcreadonly = false;
                           var13 = FileStatusChecker.getInstance().getExtendedFileState(var9, (String)((String)(var2.cc.docinfo.containsKey("krfilename") ? var2.cc.docinfo.get("krfilename") : "")));
                           MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var13);
                        }
                     }

                     var13 = null;

                     try {
                        var13 = (String)((Vector)PropertyList.getInstance().get("prop.const.fileNameToClipBoard")).get(0);
                     } catch (Exception var17) {
                        var13 = "";
                     }

                     if ("igen".equals(var13.toLowerCase())) {
                        ENYKClipboardHandler var14 = new ENYKClipboardHandler();
                        var14.setClipboardContents(var9.toString());
                        String var15 = var14.getClipboardContents();
                        String var16;
                        if (var15.equals(var9.toString())) {
                           var16 = "Az állomány nevének vágólapra másolása sikeres!\n\n" + var9.getParent() + "\n" + var9.getName();
                           GuiUtil.showMessageDialog(MainFrame.thisinstance, var16, "Üzenet", 1);
                        } else {
                           var16 = "Az állomány nevének vágólapra másolása sikertelen!";
                           GuiUtil.showMessageDialog(MainFrame.thisinstance, var16, "Üzenet", 0);
                        }
                     }
                  }
               } catch (Exception var18) {
                  var18.printStackTrace();
               }
            }

         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  if (XMLPost.xmleditnemjo) {
                     return Boolean.FALSE;
                  }

                  return Boolean.TRUE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.TRUE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }
      };
      this.closecmd = new ICommandObject() {
         public void execute() {
            Menubar.this.setEditStateForBetoltErtek(false);
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               try {
                  if (!var1.fv.pv.pv.leave_component()) {
                     return;
                  }
               } catch (Exception var15) {
               }

               BookModel var2 = var1.bm;
               if (!MainFrame.thisinstance.mp.isReadonlyMode()) {
                  if (MainFrame.FTRmode && MainFrame.FTRdoc != null) {
                     try {
                        TransformerFactory var3 = TransformerFactory.newInstance();
                        Transformer var4 = var3.newTransformer();
                        var4.setOutputProperty("encoding", "windows-1250");
                        var4.setOutputProperty("indent", "yes");
                        DOMSource var5 = new DOMSource(MainFrame.FTRdoc);
                        File var6 = new NecroFile((String)PropertyList.getInstance().get("prop.usr.naplo"), "FTR_" + var2.name + "_" + Long.toString(System.currentTimeMillis()) + ".xml");
                        FileOutputStream var7 = new NecroFileOutputStream(var6);
                        OutputStreamWriter var8 = new OutputStreamWriter(var7, "windows-1250");
                        StreamResult var9 = new StreamResult(var8);
                        var4.transform(var5, var9);
                        MainFrame.FTRdoc = null;
                        MainFrame.FTRmezok = null;
                        var8.close();
                        var7.close();
                     } catch (TransformerConfigurationException var10) {
                        var10.printStackTrace();
                     } catch (FileNotFoundException var11) {
                        var11.printStackTrace();
                     } catch (UnsupportedEncodingException var12) {
                        var12.printStackTrace();
                     } catch (TransformerException var13) {
                        var13.printStackTrace();
                     } catch (Exception var14) {
                        var14.printStackTrace();
                     }
                  }

                  if (var2.dirty) {
                     int var16 = Menubar.this.savequestion();
                     if (var16 == 0) {
                        Menubar.this.tmp_savefile = null;
                        Menubar.this.savecmd.execute();
                        if (Menubar.this.tmp_savefile == null) {
                           return;
                        }
                     }

                     if (var16 == 2) {
                        return;
                     }
                  }
               }

               XMLPost.xmleditnemjo = false;
               var1.destroy();
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

         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  if (XMLPost.xmleditnemjo) {
                     return Boolean.FALSE;
                  }

                  return Boolean.TRUE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.TRUE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }
      };
      this.notecmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               BookModel var2 = var1.bm;
               String var3;
               Elem var4;
               if (MainFrame.thisinstance.mp.isReadonlyMode()) {
                  var3 = null;
                  var4 = (Elem)var2.cc.getActiveObject();

                  try {
                     var3 = (String)var4.getEtc().get("orignote");
                  } catch (Exception var9) {
                  }

                  JOptionPane.showConfirmDialog(MainFrame.thisinstance, var3 != null && !"".equals(var3) ? var3 : "(Nincs megjegyzés)", "Megjegyzés az állományhoz", -1);
               } else {
                  var3 = null;
                  var4 = (Elem)var2.cc.getActiveObject();

                  try {
                     var3 = (String)var4.getEtc().get("orignote");
                  } catch (Exception var8) {
                  }

                  String var5 = (String)JOptionPane.showInputDialog(MainFrame.thisinstance, "Megjegyzés szövege:", "Megjegyzés az állományhoz", 3, (Icon)null, (Object[])null, var3);
                  if (var5 != null) {
                     if (var4.equals(var2.get_main_elem())) {
                        var2.cc.l_megjegyzes = var5;
                     }

                     try {
                        var4.getEtc().put("orignote", var5);
                     } catch (Exception var7) {
                        var4.setEtc(new Hashtable());
                        var4.getEtc().put("orignote", var5);
                     }

                     var2.dirty = true;
                  }
               }
            }

         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  return Boolean.TRUE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.TRUE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }
      };
      this.erasecmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               var1.fv.pv.pv.leave_component_nocheck();
               BookModel var2 = var1.bm;
               Elem var3 = (Elem)var2.cc.getActiveObject();
               String var4 = var3.toString();
               Object[] var5 = new Object[]{"Igen", "Nem"};
               int var6 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Törli a nyomtatvány adatait?" + (var4 == null ? "" : "\n" + var4 + " adatait fogja törölni!"), "Adatok törlése", 0, 3, (Icon)null, var5, var5[0]);
               if (var6 == 1 || var6 == -1) {
                  return;
               }

               GUI_Datastore var7 = (GUI_Datastore)var3.getRef();
               var7.reset();
               int[] var8 = (int[])((int[])var3.getEtc().get("pagecounts"));

               for(int var9 = 0; var9 < var8.length; ++var9) {
                  var8[var9] = 1;
               }

               TemplateUtils.getInstance().setDefaultValues(var3.getType(), var2);
               CalculatorManager.getInstance().doBetoltErtekCalcs(false);
               CalculatorManager.getInstance().do_dpage_count();
               CalculatorManager.getInstance().multi_form_load();
               CalculatorManager.getInstance().form_calc();
               var1.fv.gotoPage(0);
               var1.fv.pv.refresh();
               var2.cc.l_megjegyzes = null;
               if (AttachementTool.hasAttachement(var2)) {
                  String var10 = "A csatolmányok bejegyzése megmaradt.";
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, var10, "Figyelmeztetés", 2);
               }

               var2.dirty = true;
            }

         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  return Boolean.TRUE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }
      };
      this.printcmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (!var1.fv.pv.pv.leave_component()) {
                  return;
               }

               BookModel var2 = var1.bm;
               MainPrinter var3 = new MainPrinter(var2);

               try {
                  var3.initFromMenu(false);
               } catch (Exception var5) {
                  var5.printStackTrace();
               }
            }

         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  return Boolean.TRUE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.TRUE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }
      };
      this.print2cmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (!var1.fv.pv.pv.leave_component()) {
                  return;
               }

               BookModel var2 = var1.bm;
               MainPrinter var3 = new MainPrinter(var2);

               try {
                  var3.initFromIcon(false);
                  if (PropertyList.getInstance().get("brCountError") != null) {
                     PropertyList.getInstance().set("brCountError", (Object)null);
                     var3.initFromMenu(false);
                  }
               } catch (Exception var5) {
                  var5.printStackTrace();
               }
            }

         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  return Boolean.TRUE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.TRUE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }
      };
      this.recalccmd = new ICommandObject() {
         public void execute() {
            boolean var1 = false;
            String var2 = SettingsStore.getInstance().get("gui", "mezőszámítás");
            if (var2 != null && var2.equals("true")) {
               var1 = true;
            }

            if (MainFrame.isPart && MainFrame.isPartOnlyMain) {
               var1 = true;
            }

            if (!var1) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "A mezők újraszámítása nem lehetséges,\n mert ki van kapcsolva a funkció.\nA Szerviz/Beállításoknál (Működés lap) kapcsolhatja vissza!", "Figyelmeztetés", 2);
            } else {
               Object[] var3 = new Object[]{"Igen", "Nem"};
               int var4 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Indulhat a számított mezők újraszámítása?", "Újraszámítás", 0, 3, (Icon)null, var3, var3[0]);
               if (var4 != 1) {
                  DefaultMultiFormViewer var5 = MainFrame.thisinstance.mp.getDMFV();
                  if (var5 != null) {
                     if (!var5.fv.pv.pv.leave_component()) {
                        return;
                     }

                     int var6 = var5.bm.cc.getActiveObjectindex();
                     var5.bm.setCalcelemindex(var6);
                     CalculatorManager var7 = new CalculatorManager();
                     MainFrame.recalc_in_progress = true;
                     var7.form_calc();
                     MainFrame.recalc_in_progress = false;
                     var5.fv.pv.refresh();
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "A mezők újraszámítása megtörtént!", "Üzenet", 1);
                  }

               }
            }
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  return Boolean.TRUE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }
      };
      this.checkcmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (!var1.fv.pv.pv.leave_component()) {
                  return;
               }

               int var2 = var1.bm.cc.getActiveObjectindex();
               var1.bm.setCalcelemindex(var2);
               CalculatorManager var3 = new CalculatorManager();
               var3.formcheck();
            }

         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  return Boolean.TRUE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }
      };
      this.setcalccmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (!var1.fv.pv.pv.leave_component()) {
                  return;
               }

               SettingsStore var2 = SettingsStore.getInstance();
               String var3 = var2.get("gui", "mezőszámítás");
               boolean var4 = false;
               if (var3 != null && var3.equals("true")) {
                  var4 = true;
               }

               var4 = !var4;
               if (MainFrame.rogzitomode) {
                  var4 = false;
               }

               BaseSettingsPane.calchelper(var4, var1.bm);
               var2.set("gui", "mezőszámítás", var4 ? "true" : "false");
            }

         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  return Boolean.TRUE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }
      };
      this.exitcmd = new ICommandObject() {
         public void execute() {
            int var1 = Menubar.this.beforedialog();
            if (var1 != 2) {
               if (MainFrame.publicmode) {
                  boolean var2 = DeleteInPublicMode.getInstance().exit(false);
                  if (!var2) {
                     return;
                  }
               }

               try {
                  int var10 = MainFrame.thisinstance.mp.getStatuspanel().zoom_slider.getValue();
                  SettingsStore.getInstance().set("GUI", "zoom", var10 + "");
               } catch (Exception var9) {
               }

               SettingsStore var11 = SettingsStore.getInstance();

               try {
                  var11.set("gui", "x", MainFrame.thisinstance.getLocation().x + "");
                  var11.set("gui", "y", MainFrame.thisinstance.getLocation().y + "");
                  var11.set("gui", "w", MainFrame.thisinstance.getWidth() + "");
                  var11.set("gui", "h", MainFrame.thisinstance.getHeight() + "");
               } catch (Exception var8) {
                  Tools.eLog(var8, 0);
               }

               SettingsStore.getInstance().save();
               CstParser.clean();
               boolean var3 = true;
               Object var4 = PropertyList.getInstance().get("no_exit");
               if (var4 != null) {
                  var3 = false;
               }

               if (var3) {
                  try {
                     MainFrame var10000 = MainFrame.thisinstance;
                     MainFrame.runfile.delete();
                  } catch (Exception var7) {
                  }

                  try {
                     MainFrame.deleteCalculationRunFile();
                  } catch (Exception var6) {
                  }

                  System.exit(0);
               } else {
                  MainFrame.thisinstance.setVisible(false);
                  MainFrame.thisinstance.dispose();
                  MainFrame.thisinstance = null;
               }

            }
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            return XMLPost.xmleditnemjo ? Boolean.FALSE : Boolean.TRUE;
         }
      };
      this.attachaction = new AbstractAction(this.mi_attach, var1.get("attach")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.attachcmd.execute();
         }
      };
      this.attachcmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (var1.fv.pv.pv.leave_component()) {
                  new Csatolmanyok(MainFrame.thisinstance, var1.bm);
               }
            }
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  if (XMLPost.xmleditnemjo) {
                     return Boolean.FALSE;
                  }

                  return Boolean.TRUE;
               case 2:
               default:
                  return Boolean.FALSE;
               case 3:
                  return XMLPost.xmleditnemjo ? Boolean.FALSE : Boolean.TRUE;
               }
            } catch (Exception var3) {
               return Boolean.FALSE;
            }
         }
      };
      this.bigxmlcmd = new ICommandObject() {
         public void execute() {
            XMLPost.done();
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.TRUE;
               case 1:
                  return Boolean.FALSE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }
      };
      this.groupcmd = new ICommandObject() {
         public void execute() {
            new BatchFunctions(false, true, false);
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.TRUE;
               case 1:
                  return Boolean.FALSE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }
      };
      this.kontrollcmd = new ICommandObject() {
         public void execute() {
            Kontroll var1 = new Kontroll();

            try {
               var1.init(0);
            } catch (Exception var3) {
               var3.printStackTrace();
            }

         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            int var2 = (Integer)var1;
            return var2 == 0 ? Boolean.TRUE : Boolean.FALSE;
         }
      };
      this.kontrollcopycmd = new ICommandObject() {
         public void execute() {
            Kontroll var1 = new Kontroll();

            try {
               var1.init(1);
            } catch (Exception var3) {
               var3.printStackTrace();
            }

         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            int var2 = (Integer)var1;
            return var2 == 0 ? Boolean.TRUE : Boolean.FALSE;
         }
      };
      this.adatlistacmd = new ICommandObject() {
         public void execute() {
            FormDataListDialog.getInstance().execute();
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            return FormDataListDialog.getInstance().getState(var1);
         }
      };
      this.emptyprintcmd = new ICommandObject() {
         public void execute() {
            Hashtable var1 = new Hashtable();
            var1.put("emptyprint", "");
            ABEVNewPanel var2 = new ABEVNewPanel(var1);
            final Hashtable var3 = var2.showDialog();
            if (var3 != null && var3.size() != 0) {
               MainFrame.thisinstance.setGlassLabel("Betöltés folyamatban!");
               Thread var4 = new Thread(new Runnable() {
                  public void run() {
                     Object[] var1 = (Object[])((Object[])var3.get("selected_file"));
                     File var2 = (File)var1[0];
                     BookModel var3x = new BookModel(var2);
                     var3x.emptyprint = true;
                     if (!var3x.hasError) {
                        DefaultMultiFormViewer var4 = new DefaultMultiFormViewer(var3x);
                        MainPanel var10001;
                        if (var4.fv.getTp().getTabCount() == 0) {
                           var3x.destroy();
                           var10001 = MainFrame.thisinstance.mp;
                           MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                           MainFrame.thisinstance.setGlassLabel((String)null);
                           MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                           MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                           MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                           return;
                        }

                        var4.addallempty();
                        MainFrame.thisinstance.mp.intoleftside(var4);
                        var10001 = MainFrame.thisinstance.mp;
                        MainFrame.thisinstance.mp.setstate(MainPanel.NORMAL);
                        MainFrame.thisinstance.setGlassLabel((String)null);
                        MainPrinter var5 = new MainPrinter(var3x);

                        try {
                           var5.initFromMenu(true);
                        } catch (Exception var7) {
                           var7.printStackTrace();
                        }

                        StatusPane.thisinstance.currentpagename.setText("");
                     } else {
                        SwingUtilities.invokeLater(new Runnable() {
                           public void run() {
                              MainFrame.thisinstance.setGlassLabel((String)null);
                              Menubar.this.emptyprintcmd.execute();
                           }
                        });
                     }

                  }
               });
               var4.start();
            }

         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.TRUE;
               case 1:
                  return Boolean.FALSE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }
      };
      this.envprintcmd = new ICommandObject() {
         public void execute() {
            EnvelopeModel var1 = new EnvelopeModel(Calculator.getInstance().getBookModel());
            if (!var1.getFeladoAllandoCim().isFeladoPrintable() && !var1.getFeladoLevelezesiCim().isFeladoPrintable()) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "A feladónak sem levelezési, sem állandó címe nem képezhető az űrlapon szereplő adatok alapján!\nNem tud borítékot nyomtatni.", "Nyomtatási hiba", 0);
            } else if (var1.getSzervezetCimei().size() > 0) {
               Envelope var2 = new Envelope(MainFrame.thisinstance);
               var2.setModel(var1);
            } else {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatvány publikálója nem adott meg levelezési címet!\nNem tud borítékot nyomtatni.", "Nyomtatási hiba", 0);
            }

         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            EnvelopePrint var2 = new EnvelopePrint();
            return var2.getState(var1);
         }
      };
      this.gatecmd = new ICommandObject() {
         public void execute() {
            IOsHandler var1 = OsFactory.getOsHandler();

            try {
               File var2;
               try {
                  var2 = new NecroFile(SettingsStore.getInstance().get("gui", "internet_browser"));
                  if (!var2.exists()) {
                     throw new Exception();
                  }
               } catch (Exception var4) {
                  var2 = new NecroFile(var1.getSystemBrowserPath());
               }

               if (var2.getName().toLowerCase().indexOf("edge") > -1) {
                  var1.execute("start Microsoft-Edge:https://tarhely.gov.hu/levelezes/login", (String[])null, var2.getParentFile());
               } else {
                  var1.execute(var2.getName() + " https://tarhely.gov.hu/levelezes/login", (String[])null, var2.getParentFile());
               }
            } catch (Exception var5) {
               var5.printStackTrace();
            }

         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            return Boolean.TRUE;
         }
      };
      this.resourcescmd = new ICommandObject() {
         public void execute() {
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  ResourceHandlerUI var1 = new ResourceHandlerUI();
                  var1.fillContent();
                  var1.pack();
                  var1.setVisible(true);
               }
            });
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            return Boolean.TRUE;
         }
      };
      this.xmldisplaycmd = new ICommandObject() {
         public void execute() {
            XMLPost.display();
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.TRUE;
               case 1:
                  return Boolean.FALSE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }
      };
      this.xmleditcmd = new ICommandObject() {
         public void execute() {
            XMLPost.edit();
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.TRUE;
               case 1:
                  return Boolean.FALSE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }
      };
      this.xmlqeditcmd = new ICommandObject() {
         public void execute() {
            XMLPost.edit_nem_jo();
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.TRUE;
               case 1:
                  return Boolean.FALSE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }
      };
      this.xmlsavecmd = new ICommandObject() {
         public void execute() {
            XMLPost.save();
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  return Boolean.TRUE;
               case 2:
               default:
                  break;
               case 3:
                  return MainFrame.readonlymodefromubev ? Boolean.TRUE : Boolean.FALSE;
               }
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }
      };
      this.xmlmergecmd = new ICommandObject() {
         public void execute() {
            XMLPost.merge();
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.TRUE;
               case 1:
                  return Boolean.FALSE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }
      };
      this.xmlclosecmd = new ICommandObject() {
         public void execute() {
            XMLPost.close();
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  if (XMLPost.xmleditnemjo) {
                     return Boolean.TRUE;
                  }

                  return Boolean.FALSE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }
      };
      this.gotocmd = new ICommandObject() {
         public void execute() {
            Menubar.this.cmd_goto();
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  return Boolean.TRUE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }
      };
      this.calculatorcmd = new ICommandObject() {
         public void execute() {
            Menubar.this.executeCalculator();
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.TRUE;
               case 1:
                  return Boolean.TRUE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.TRUE;
               }
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }
      };
      this.detailscmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               DataFieldModel var2 = var1.fv.pv.pv.getCurrent_df();
               var1.fv.pv.pv.done_details(var2);
               var1.fv.pv.pv.setCurrent_df(var2);
               var1.fv.pv.pv.change_component(var2);
            }

         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  if (XMLPost.xmleditnemjo) {
                     return Boolean.FALSE;
                  }

                  return Boolean.TRUE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }
      };
      this.newaction = new AbstractAction(this.mi_new, var1.get("abev_new_a")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.newcmd.execute();
         }
      };
      this.openaction = new AbstractAction(this.mi_open, var1.get("abev_open_a")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.opencmd.execute();
         }
      };
      this.saveaction = new AbstractAction(this.mi_save, var1.get("abev_save_a")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.savecmd.execute();
         }
      };
      this.saveasaction = new AbstractAction(this.mi_saveas, var1.get("abev_saveas_a")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.saveascmd.execute();
         }
      };
      this.closeaction = new AbstractAction(this.mi_close, var1.get("close")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.closecmd.execute();
         }
      };
      this.recalcaction = new AbstractAction(this.mi_recalc, var1.get("calc")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.recalccmd.execute();
         }
      };
      this.noteaction = new AbstractAction(this.mi_note, var1.get("abev_comment_i")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.notecmd.execute();
         }
      };
      this.eraseaction = new AbstractAction(this.mi_erase, var1.get("abev_delete_i")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.erasecmd.execute();
         }
      };
      this.printaction = new AbstractAction(this.mi_print, var1.get("abev_print_i")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.printcmd.execute();
         }
      };
      this.checkaction = new AbstractAction(this.mi_check, var1.get("abev_check_i")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.checkcmd.execute();
         }
      };
      this.setcalcaction = new AbstractAction(this.mi_setcalc, var1.get("abev_setcalc_i")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.setcalccmd.execute();
         }
      };
      this.exitaction = new AbstractAction(this.mi_exit, var1.get("abev_out_i")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.exitcmd.execute();
         }
      };
      this.bigxmlaction = new AbstractAction(this.mi_bigxml, var1.get("bigxml")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.bigxmlcmd.execute();
         }
      };
      this.groupaction = new AbstractAction(this.mi_group, var1.get("group")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.groupcmd.execute();
         }
      };
      this.kontrollaction = new AbstractAction(this.mi_kontroll, var1.get("kontroll")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.kontrollcmd.execute();
         }
      };
      this.kontrollcopyaction = new AbstractAction(this.mi_kontrollcopy, var1.get("kontrollc")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.kontrollcopycmd.execute();
         }
      };
      this.adatlistaaction = new AbstractAction(this.mi_adatlista, var1.get("lista")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.adatlistacmd.execute();
         }
      };
      this.emptyprintaction = new AbstractAction(this.mi_emptyprint, var1.get("emptyprint")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.emptyprintcmd.execute();
         }
      };
      this.envprintaction = new AbstractAction(this.mi_envprint, var1.get("envelopeprint")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.envprintcmd.execute();
         }
      };
      this.gateaction = new AbstractAction(this.mi_gate, var1.get("abev_sign_i")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.gatecmd.execute();
         }
      };
      this.resourcesaction = new AbstractAction(this.mi_resources, var1.get("resources")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.resourcescmd.execute();
         }
      };
      this.xmldisplayaction = new AbstractAction(this.mi_xmldisplay, var1.get("xmldisplay")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.xmldisplaycmd.execute();
         }
      };
      this.xmleditaction = new AbstractAction(this.mi_xmledit, var1.get("xmledit")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.xmleditcmd.execute();
         }
      };
      this.xmlqeditaction = new AbstractAction(this.mi_xmlqedit, var1.get("xmlqedit")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.xmlqeditcmd.execute();
         }
      };
      this.xmlsaveaction = new AbstractAction(this.mi_xmlsave, var1.get("xmlsave")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.xmlsavecmd.execute();
         }
      };
      this.xmlmergeaction = new AbstractAction(this.mi_xmlmerge, var1.get("xmlmerge")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.xmlmergecmd.execute();
         }
      };
      this.xmlcloseaction = new AbstractAction(this.mi_xmlclose, var1.get("xmlclose")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.xmlclosecmd.execute();
         }
      };
      this.gotoaction = new AbstractAction(this.mi_goto, var1.get("goto")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.gotocmd.execute();
         }
      };
      this.calculatoraction = new AbstractAction(this.mi_calculator, var1.get("calculator")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.calculatorcmd.execute();
         }
      };
      this.detailsaction = new AbstractAction(this.mi_details, var1.get("details")) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.this.detailscmd.execute();
         }
      };
   }

   public int savequestion() {
      Object[] var1 = new Object[]{"Igen", "Nem", "Mégsem"};
      int var2 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Kívánja az adatokat menteni?", "Bezárás", 0, 3, (Icon)null, var1, var1[0]);
      return var2;
   }

   private int beforedialog() {
      DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
      if (var1 != null) {
         try {
            var1.fv.pv.pv.leave_component_nocheck();
            if (MainFrame.thisinstance.mp.isReadonlyMode()) {
               return 1;
            }

            if (MainFrame.readonlymodefromubev) {
               return 1;
            }

            if (this.saveInProgress) {
               System.out.println("Mentés folyamatban...");
               return 2;
            }

            BookModel var2 = var1.bm;
            if (var2.dirty) {
               int var3 = this.savequestion();
               if (var3 == 0) {
                  if (var2.dbpkgloader == null) {
                     this.saveInProgress = true;
                     this.tmp_savefile = null;
                     this.savecmd.execute();
                     this.saveInProgress = false;
                     if (this.tmp_savefile == null) {
                        return 2;
                     }
                  } else {
                     int var4 = var2.dbpkgloader.save(var2);
                     if (var4 == 1) {
                        return 2;
                     }
                  }
               }

               if (var3 == 1) {
                  try {
                     IDbHandler var7 = DbFactory.getDbHandler();
                     if (var7 != null) {
                        var7.handleExitWithoutSave();
                     }
                  } catch (Exception var5) {
                     var5.printStackTrace();
                  }

                  return 1;
               }

               if (var3 == 2) {
                  return 2;
               }
            }
         } catch (Exception var6) {
            this.saveInProgress = false;
            return 1;
         }
      }

      return 1;
   }

   public void paint(Graphics var1) {
      super.paint(var1);
      var1.setColor(Color.RED);
      int var2 = var1.getFontMetrics().stringWidth(this.menuextratext);
      int var3 = this.getWidth() - var2 - 15;
      var1.drawString(this.menuextratext, var3, 20);
      var1.setColor(MainFrame.signcolor);
      var1.drawLine(0, 0, 0, 0);
   }

   public void setState(Object var1) {
      Object var2;
      if (var1 == null) {
         var2 = MainFrame.thisinstance.mp.state;
      } else {
         var2 = var1;
      }

      Enumeration var3 = this.menuitemht.keys();

      ICommandObject var4;
      JMenuItem var5;
      while(var3.hasMoreElements()) {
         var4 = (ICommandObject)var3.nextElement();
         var5 = (JMenuItem)this.menuitemht.get(var4);

         try {
            var5.setEnabled((Boolean)var4.getState(var2));
         } catch (Exception var8) {
            var5.setEnabled(false);
         }
      }

      var3 = this.menuitemht2.keys();

      while(var3.hasMoreElements()) {
         var4 = (ICommandObject)var3.nextElement();
         var5 = (JMenuItem)this.menuitemht2.get(var4);

         try {
            var5.setEnabled((Boolean)var4.getState(var2));
         } catch (Exception var7) {
            var5.setEnabled(false);
         }
      }

   }

   private String getProperty(String var1) {
      return (String)PropertyList.getInstance().get(var1);
   }

   public void cmd_file_new(String var1) {
      try {
         String[] var2 = var1.split(" ");
         var1 = var2[0];
         final boolean var3 = var2.length > 1 ? "fill".equalsIgnoreCase(var2[1]) : false;
         final String var4 = var2.length > 2 ? var2[2] : null;
         File var5;
         if (var1.endsWith(".tem.enyk")) {
            var5 = new NecroFile(var1);
            if (!var5.exists()) {
               File var11 = new NecroFile(this.getProperty("prop.dynamic.templates.absolutepath"));
               var5 = new NecroFile(var11, var1);
            }

            if (var5 == null || !var5.exists()) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "A megadott állomány nem található! ( " + var1 + " )", "Hiba!", 0);
               return;
            }

            String var12 = var5.getParent();
            if (var5 != null) {
               PropertyList.getInstance().set("prop.dynamic.templates.absolutepath", var12);
            }
         } else {
            try {
               ExtendedTemplateData var6 = TemplateChecker.getInstance().getTemplateFileNames(var1);
               String[] var7 = var6.getTemplateFileNames();
               var5 = new NecroFile(var7[0]);
            } catch (Exception var9) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "A megadott állomány azonosító nem található! ( " + var1 + " )", "Hiba!", 0);
               return;
            }
         }

         final Hashtable var13 = new Hashtable();
         Object[] var14 = new Object[]{var5};
         var13.put("selected_file", var14);
         var13.put("primary_account", "(Nincs kiválasztva)");
         var13.put("tax_expert", "(Nincs kiválasztva)");
         var13.put("file_status", "Módosítható");
         MainFrame.thisinstance.setGlassLabel("Betöltés folyamatban!");
         Thread var8 = new Thread(new Runnable() {
            public void run() {
               Object[] var1 = (Object[])((Object[])var13.get("selected_file"));
               File var2 = (File)var1[0];
               Object var3x = var13.get("primary_account");
               Object var4x = var13.get("tax_expert");
               String var5 = var13.get("file_status").toString();
               BookModel var6 = new BookModel(var2);
               if (BlacklistStore.getInstance().handleGuiMessage(var6.getTemplateId(), var6.getOrgId())) {
                  var6.setDisabledTemplate(true);

                  try {
                     var6.errormsg = BlacklistStore.getInstance().getMessage(var6.getTemplateId(), var6.getOrgId());
                  } catch (Exception var12) {
                  }

                  var6.hasError = true;
               }

               if (!var6.hasError) {
                  var6.setPAInfo(var3x, var4x);
                  DefaultMultiFormViewer var7 = new DefaultMultiFormViewer(var6);
                  MainPanel var10001;
                  if (var7.fv.getTp().getTabCount() == 0) {
                     var6.destroy();
                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                     MainFrame.thisinstance.setGlassLabel((String)null);
                     MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                     MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                     MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                     return;
                  }

                  if (var3x.equals("(Nincs kiválasztva)") && var4x.equals("(Nincs kiválasztva)")) {
                     var6.dirty = false;
                  }

                  MainFrame.thisinstance.mp.intoleftside(var7);
                  if (var3) {
                     var6.autofill = true;
                     Object var8 = var6.cc.getActiveObject();
                     int var9 = var6.forms.size();
                     if (var9 > 1) {
                        for(int var10 = 0; var10 < var9; ++var10) {
                           if (var10 != var6.get_main_index()) {
                              FormModel var11 = var6.get(var10);
                              var6.addForm(var11);
                           }
                        }
                     }

                     EnykAutoFill var14 = new EnykAutoFill(var6);
                     var14.fill();
                     var6.cc.setActiveObject(var8);
                     MainFrame.thisinstance.mp.getDMFV().fv.pv.pv.refresh();
                  }

                  Help var13x = Help.getInstance("kiut");
                  if (var4 != null) {
                     var13x.setForcePath("file:\\" + var4);
                     var13x.update_url("file:\\" + var4);
                     Menubar.this.handleStatusPanel();
                  } else {
                     var13x.setForcePath((String)null);
                  }

                  var10001 = MainFrame.thisinstance.mp;
                  MainFrame.thisinstance.mp.setstate(MainPanel.NORMAL);
                  MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var5);
                  BaseSettingsPane.done_menuextratext(true);
                  MainFrame.thisinstance.setGlassLabel((String)null);
               } else {
                  SwingUtilities.invokeLater(new Runnable() {
                     public void run() {
                        MainFrame.thisinstance.setGlassLabel((String)null);
                        Menubar.this.newcmd.execute();
                     }
                  });
               }

            }
         });
         var8.start();
      } catch (Exception var10) {
         var10.printStackTrace();
      }

   }

   public void cmd_file_open(String var1, Boolean var2) {
      this.cmd_file_open(var1, var2, false);
   }

   public void cmd_file_open(String var1, Boolean var2, boolean var3) {
      this.cmd_file_open(var1, var2, var3, false, false, false, false);
   }

   public void cmd_file_open(String var1, Boolean var2, final boolean var3, final boolean var4, final boolean var5, final boolean var6, final boolean var7) {
      try {
         final File var8 = new NecroFile(var1);
         if (var8 == null || !var8.exists()) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A megadott állomány nem található! ( " + var1 + " )", "Hiba", 0);
            if ("yes".equals(PropertyList.getInstance().get("prop.dynamic.uniimport"))) {
               PropertyList.getInstance().set("prop.dynamic.uniimport", "");
               MainFrame.thisinstance.setGlassLabel((String)null);
            }

            return;
         }

         CachedCollection var9 = new CachedCollection();
         Hashtable var10 = new Hashtable();
         Hashtable var11 = var9.getHeadData(var8);
         String[] var12 = this.getDocAndOrgId(var11);
         if (var12 == null) {
            return;
         }

         if (BlacklistStore.getInstance().handleGuiMessage(var12[0], var12[1])) {
            return;
         }

         Object[] var13 = new Object[]{var8, null, null, var11};
         Object[] var14 = new Object[]{var13};
         var10.put("selected_files", var14);
         Object[] var15 = (Object[])((Object[])var10.get("selected_files"));
         final Hashtable var16 = (Hashtable)((Hashtable)((Object[])((Object[])var15[0]))[3]).get("docinfo");
         String var17 = FileStatusChecker.getInstance().getExtendedFileState(var8, (String)((String)(var16.containsKey("krfilename") ? var16.get("krfilename") : "")));
         var10.put("file_status", var17);
         var10.put("read_only", FileStatusChecker.getInstance().getFileReadOnlyState(var17));
         var10.put("function_read_only", var2);
         final File var18 = (File)((Object[])((Object[])var15[0]))[0];
         final boolean var19 = (Boolean)var10.get("read_only");
         final boolean var20 = (Boolean)var10.get("function_read_only");
         final String var21 = var10.get("file_status").toString();
         MainFrame.thisinstance.setGlassLabel("Betöltés folyamatban!");
         Thread var22 = new Thread(new Runnable() {
            public void run() {
               String var1 = (String)var16.get("id");
               String var2 = (String)var16.get("templatever");
               String var3x = (String)var16.get("org");
               PropertyList.getInstance().set("prop.dynamic.ilyenkor", "");
               File var4x;
               ExtendedTemplateData var5x;
               if (!var6) {
                  var5x = TemplateChecker.getInstance().getTemplateFilenameWithDialog(var1, var2, var3x);
                  var4x = var5x.getTemplateFile();
               } else {
                  try {
                     var5x = TemplateChecker.getInstance().getTemplateFileNames(var1, var2, var3x);
                     var4x = new NecroFile(var5x.getTemplateFileNames()[0]);
                  } catch (Exception var10) {
                     MainFrame.thisinstance.setGlassLabel((String)null);
                     if ("yes".equals(PropertyList.getInstance().get("prop.dynamic.uniimport"))) {
                        PropertyList.getInstance().set("prop.dynamic.uniimport", "");
                     }

                     return;
                  }
               }

               PropertyList.getInstance().set("prop.dynamic.ilyenkor", (Object)null);
               if (var4x != null) {
                  BookModel var11;
                  if (var7) {
                     var11 = new BookModel(var4x, var18, false, true);
                  } else {
                     var11 = new BookModel(var4x, var18);
                  }

                  MainPanel var10001;
                  if (!var11.hasError) {
                     if (var4) {
                        var11.setCcLoadedFile((File)null);
                     }

                     DefaultMultiFormViewer var6x = new DefaultMultiFormViewer(var11);
                     if (var6x.fv.getTp().getTabCount() == 0) {
                        var11.destroy();
                        var10001 = MainFrame.thisinstance.mp;
                        MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                        MainFrame.thisinstance.setGlassLabel((String)null);
                        MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                        MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                        MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                        return;
                     }

                     MainFrame.thisinstance.mp.intoleftside(var6x);
                     int var7x = var11.carryOnTemplate();
                     boolean var8x = true;
                     int var12;
                     String var13;
                     switch(var7x) {
                     case 0:
                        System.out.println("TEMPLATE VISSZAVONVA");
                        MainFrame.thisinstance.mp.funcreadonly = true;
                        var10001 = MainFrame.thisinstance.mp;
                        MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
                        var13 = BookModel.CHECK_VALID_MESSAGES[var7x];
                        MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var13);
                        var12 = 0;
                        break;
                     case 1:
                        var12 = Tools.handleTemplateCheckerResult(var11);
                        System.out.println("TEMPLATE HASZNALATBAN");
                        if (var12 >= 4) {
                           System.out.println("   - DE A VERZIO NEM STIMMEL " + var12);
                           MainFrame.thisinstance.mp.funcreadonly = true;
                           var10001 = MainFrame.thisinstance.mp;
                           MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
                           var13 = BookModel.CHECK_VALID_MESSAGES[var12];
                           MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var13);
                        } else {
                           var12 = -1;
                        }
                        break;
                     case 2:
                     default:
                        var12 = -1;
                        break;
                     case 3:
                        var12 = -1;
                        System.out.println("HIBA_AZ_ELLENORZESKOR");
                     }

                     if (var12 == -1) {
                        MainFrame.thisinstance.mp.funcreadonly = var20;
                        if (var19) {
                           var10001 = MainFrame.thisinstance.mp;
                           MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
                        } else {
                           var10001 = MainFrame.thisinstance.mp;
                           MainFrame.thisinstance.mp.setstate(MainPanel.NORMAL);
                        }

                        MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var21);
                     }

                     MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("Ny:" + (String)var11.docinfo.get("ver"));
                     if (var5) {
                        CalculatorManager.getInstance().multiform_calc();
                     }

                     if ("yes".equals(PropertyList.getInstance().get("prop.dynamic.uniimport"))) {
                        PropertyList.getInstance().set("prop.dynamic.uniimport", "");
                        String var9 = "A számított mezők újraszámítása lefutott. \nA különböző típusú nyomtatványok adatai nem minden esetben feleltethetők meg egymásnak.\nA program a kimaradó adatokról listát készített, de emellett is előfordulhat adateltérés, ezért az áttöltés után tételesen ellenőrizze az átvitt értékeket!\nA nyomtatvány nem kerül automatikusan mentésre, ezt az Adatok -> Nyomtatvány mentése menüponttal tudja megtenni.";
                        GuiUtil.showMessageDialog(MainFrame.thisinstance, var9, "Figyelmeztetés", 2);
                     }
                  } else {
                     if ("yes".equals(PropertyList.getInstance().get("prop.dynamic.uniimport"))) {
                        PropertyList.getInstance().set("prop.dynamic.uniimport", "");
                     }

                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                     GuiUtil.setcurrentpagename("");
                  }

                  if (var3) {
                     var8.delete();
                  }

                  MainFrame.thisinstance.setGlassLabel((String)null);
               } else {
                  if (var3) {
                     var8.delete();
                  }

                  if ("yes".equals(PropertyList.getInstance().get("prop.dynamic.uniimport"))) {
                     PropertyList.getInstance().set("prop.dynamic.uniimport", "");
                  } else {
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                           MainFrame.thisinstance.setGlassLabel((String)null);
                           Menubar.this.opencmd.execute();
                        }
                     });
                  }
               }

            }
         });
         var22.start();
      } catch (Exception var23) {
         var23.printStackTrace();
      }

   }

   public void cmd_file_multiimport(String var1) {
      File var2 = new NecroFile(var1);
      if (!var2.exists()) {
         File var3 = new NecroFile((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.import"));
         var2 = new NecroFile(var3, var1);
      }

      if (var2 != null && var2.exists()) {
         MultiImport var4 = new MultiImport();
         var4.display(var2.getAbsolutePath());
      } else {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A megadott állomány nem található! ( " + var2.getAbsolutePath() + " )", "Üzenet", 0);
      }
   }

   public void cmd_file_import(String var1) {
      File var2 = new NecroFile(var1);
      if (!var2.exists()) {
         File var3 = new NecroFile((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.import"));
         var2 = new NecroFile(var3, var1);
      }

      if (var2 != null && var2.exists()) {
         boolean var9 = false;

         try {
            if (!var1.toLowerCase().endsWith(".dat") && !var1.toLowerCase().endsWith(".abv") && !var1.toLowerCase().endsWith(".imp") && !var1.toLowerCase().endsWith(".xml") && !var1.toLowerCase().endsWith(".xkr") && !var1.toLowerCase().endsWith(".kat") && !var1.toLowerCase().endsWith(".elk") && !var1.toLowerCase().endsWith(".xcz")) {
               var9 = false;
            } else {
               var9 = true;
            }
         } catch (Exception var8) {
            Tools.eLog(var8, 0);
         }

         boolean var4 = var9 && !var1.toLowerCase().endsWith(".xkr");
         BatchFunctions var5 = new BatchFunctions(true, false, var4);
         MainFrame.thisinstance.setGlassLabel("Importálás folyamatban!");
         MainFrame.thisinstance.glasslock = true;

         try {
            if (var9) {
               var5.cmdOne(var2.getCanonicalFile().getAbsolutePath(), (String)null, true);
            } else {
               var5.cmd(var2.getCanonicalFile().getAbsolutePath());
            }
         } catch (Exception var7) {
            Tools.eLog(var7, 0);
         }

      } else {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A megadott állomány nem található! ( " + var1 + " )", "Üzenet", 0);
      }
   }

   public void cmd_edit_imp(String var1) {
      File var2 = new NecroFile(var1);
      if (!var2.exists()) {
         File var3 = new NecroFile((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.import"));
         var2 = new NecroFile(var3, var1);
      }

      if (var2 != null && var2.exists()) {
         BatchFunctions var6 = new BatchFunctions(true, false, true);
         MainFrame.thisinstance.setGlassLabel("Importálás folyamatban!");
         MainFrame.thisinstance.glasslock = true;

         try {
            var6.cmdOne(var2.getAbsolutePath(), ".imp", true);
         } catch (Exception var5) {
            var5.printStackTrace();
         }

      } else {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A megadott állomány nem található! ( " + var1 + " )", "Üzenet", 0);
      }
   }

   public void cmd_edit_xml(String var1) {
      File var2 = new NecroFile(var1);
      if (!var2.exists()) {
         File var3 = new NecroFile((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.import"));
         var2 = new NecroFile(var3, var1);
      }

      if (var2 != null && var2.exists()) {
         BatchFunctions var6 = new BatchFunctions(true, false, true);
         MainFrame.thisinstance.setGlassLabel("Importálás folyamatban!");
         MainFrame.thisinstance.glasslock = true;

         try {
            var6.cmdOne(var2.getAbsolutePath(), ".xml", true);
         } catch (Exception var5) {
            var5.printStackTrace();
         }

      } else {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A megadott állomány nem található! ( " + var1 + " )", "Üzenet", 0);
      }
   }

   public void cmd_edit_dat(String var1) {
      File var2 = new NecroFile(var1);
      if (!var2.exists()) {
         File var3 = new NecroFile((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.import"));
         var2 = new NecroFile(var3, var1);
      }

      if (var2 != null && var2.exists()) {
         BatchFunctions var6 = new BatchFunctions(true, false, true);
         MainFrame.thisinstance.setGlassLabel("Importálás folyamatban!");
         MainFrame.thisinstance.glasslock = true;

         try {
            var6.cmdOne(var2.getAbsolutePath(), ".dat", true);
         } catch (Exception var5) {
            var5.printStackTrace();
         }

      } else {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A megadott állomány nem található! ( " + var1 + " )", "Üzenet", 0);
      }
   }

   public void cmd_show_xml(String var1) {
      File var2 = new NecroFile(var1);
      if (!var2.exists()) {
         File var3 = new NecroFile((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.import"));
         var2 = new NecroFile(var3, var1);
      }

      if (var2 != null && var2.exists()) {
         XMLPost.done_display(var2);
      } else {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A megadott állomány nem található! ( " + var1 + " )", "Hiba", 0);
      }
   }

   public void menuSelectionChanged(boolean var1) {
      if (!var1) {
         CalculatorManager.getInstance().closeDialog();
      }

   }

   public void cmd_check_dir(String var1) {
      File var2 = new NecroFile(var1);
      if (!var2.exists()) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A megadott könyvtár nem található! ( " + var1 + " )");
      } else if (!var2.isDirectory()) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A megadott állomány nem könyvtár! ( " + var1 + " )");
      } else {
         BatchFunctions var3 = new BatchFunctions(true, false, false);
         var3.cmdCheck(var1);
      }
   }

   public void cmd_check_xml(String var1) {
      try {
         BatchCheck var2 = new BatchCheck(0);
         String var3 = var2.doCheck(var1);
         MainFrame.thisinstance.setGlassLabel((String)null);
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az eredmény file: " + var3 + "");
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void cmd_dsign_xml(String var1) {
      try {
         BatchCheck var2 = new BatchCheck(0);
         String var3 = var2.doDsign(var1);
         MainFrame.thisinstance.setGlassLabel((String)null);
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az eredmény file: " + var3 + "");
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void cmd_db_open_xml(String var1, final boolean var2) {
      String[] var3 = var1.split(" ");
      if (var3.length == 4) {
         if (var2) {
            MainFrame.readonlymodefromubev = true;
            MainFrame.thisinstance.setJMenuBar((JMenuBar)null);
            MainFrame.thisinstance.setJMenuBar(new Menubar("1"));
            MainFrame.thisinstance.mp.hideToolbar();
         }

         IDbHandler var4 = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.DefaultDbHandler");
         var4.login(var3[0], var3[1], var3[2]);
         final String var6 = var3[3];

         try {
            MainFrame.thisinstance.setGlassLabel("Betöltés folyamatban!");
            Thread var7 = new Thread(new Runnable() {
               public void run() {
                  String var1 = "";
                  String var2x = "";
                  String var3 = "";
                  boolean var4 = false;
                  boolean var5 = var2;
                  String var6x = "";
                  DbLoader var7 = new DbLoader(1, var6);
                  BookModel var8 = var7.load((String)null, var1, var2x, var3);
                  MainPanel var10001;
                  if (!var8.hasError) {
                     PropertyList.getInstance().set("bfoLogStoredProcParams", new String[]{"EBEV_ABEVJAVA.BFO_LOG", var6});
                     DefaultMultiFormViewer var9 = new DefaultMultiFormViewer(var8);
                     if (var9.fv.getTp().getTabCount() == 0) {
                        var8.destroy();
                        var10001 = MainFrame.thisinstance.mp;
                        MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                        MainFrame.thisinstance.setGlassLabel((String)null);
                        MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                        MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                        MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                        return;
                     }

                     MainFrame.thisinstance.mp.intoleftside(var9);
                     MainFrame.thisinstance.mp.funcreadonly = var4;
                     if (var5) {
                        var10001 = MainFrame.thisinstance.mp;
                        MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
                     } else {
                        var10001 = MainFrame.thisinstance.mp;
                        MainFrame.thisinstance.mp.setstate(MainPanel.NORMAL);
                     }

                     MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var6x);
                     MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("Ny:" + (String)var8.docinfo.get("ver"));
                  } else {
                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, var8.errormsg);
                     GuiUtil.setcurrentpagename("");
                  }

                  MainFrame.thisinstance.setGlassLabel((String)null);
               }
            });
            var7.start();
         } catch (Exception var8) {
            var8.printStackTrace();
         }

      }
   }

   public void cmd_db_open_xml_doku(String var1, final boolean var2) {
      String[] var3 = var1.split(" ");
      if (var3.length == 4) {
         if (var2) {
            MainFrame.readonlymodefromubev = true;
            MainFrame.thisinstance.setJMenuBar((JMenuBar)null);
            MainFrame.thisinstance.setJMenuBar(new Menubar("1"));
            MainFrame.thisinstance.mp.hideToolbar();
         }

         IDbHandler var4 = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.DefaultDbHandler");
         var4.login(var3[0], var3[1], var3[2]);
         final String var6 = var3[3];

         try {
            MainFrame.thisinstance.setGlassLabel("Betöltés folyamatban!");
            Thread var7 = new Thread(new Runnable() {
               public void run() {
                  String var1 = "";
                  String var2x = "";
                  String var3 = "";
                  boolean var4 = false;
                  boolean var5 = var2;
                  String var6x = "";
                  DbLoader var7 = new DbLoader(3, var6);
                  BookModel var8 = var7.load((String)null, var1, var2x, var3);
                  MainPanel var10001;
                  if (!var8.hasError) {
                     PropertyList.getInstance().set("bfoLogStoredProcParams", new String[]{"dok_abevjava_pkg.bfo_log", var6});
                     DefaultMultiFormViewer var9 = new DefaultMultiFormViewer(var8);
                     if (var9.fv.getTp().getTabCount() == 0) {
                        var8.destroy();
                        var10001 = MainFrame.thisinstance.mp;
                        MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                        MainFrame.thisinstance.setGlassLabel((String)null);
                        MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                        MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                        MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                        return;
                     }

                     MainFrame.thisinstance.mp.intoleftside(var9);
                     MainFrame.thisinstance.mp.funcreadonly = var4;
                     if (var5) {
                        var10001 = MainFrame.thisinstance.mp;
                        MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
                     } else {
                        var10001 = MainFrame.thisinstance.mp;
                        MainFrame.thisinstance.mp.setstate(MainPanel.NORMAL);
                     }

                     MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var6x);
                     MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("Ny:" + (String)var8.docinfo.get("ver"));
                  } else {
                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, var8.errormsg);
                     GuiUtil.setcurrentpagename("");
                  }

                  MainFrame.thisinstance.setGlassLabel((String)null);
               }
            });
            var7.start();
         } catch (Exception var8) {
            var8.printStackTrace();
         }

      }
   }

   public void cmd_db_open_xml_sp(String var1, final boolean var2) {
      String[] var3 = var1.split(" ");
      if (var2) {
         MainFrame.readonlymodefromubev = true;
         MainFrame.thisinstance.setJMenuBar((JMenuBar)null);
         MainFrame.thisinstance.setJMenuBar(new Menubar("1"));
         MainFrame.thisinstance.mp.hideToolbar();
      }

      IDbHandler var4 = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.DefaultDbHandler");
      var4.login(var3[0], var3[1], var3[2]);
      final String var6 = var3[3];
      final String var7 = var3[4];
      final String var8 = var3[5];
      final String var9 = var3.length > 6 ? var3[6] : "";

      try {
         MainFrame.thisinstance.setGlassLabel("Betöltés folyamatban!");
         Thread var10 = new Thread(new Runnable() {
            public void run() {
               String var1 = "";
               String var2x = "";
               String var3 = "";
               boolean var4 = false;
               String var5 = "";
               DbLoader var6x = new DbLoader(var6, var7, var8);
               BookModel var7x = var6x.load((String)null, var1, var2x, var3);
               var7x.dbloader = var6x;
               MainPanel var10001;
               if (!var7x.hasError) {
                  PropertyList.getInstance().set("bfoLogStoredProcParams", new String[]{var9, var6});
                  DefaultMultiFormViewer var8x = new DefaultMultiFormViewer(var7x);
                  if (var8x.fv.getTp().getTabCount() == 0) {
                     var7x.destroy();
                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                     MainFrame.thisinstance.setGlassLabel((String)null);
                     MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                     MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                     MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                     return;
                  }

                  MainFrame.thisinstance.mp.intoleftside(var8x);
                  MainFrame.thisinstance.mp.funcreadonly = var4;
                  if (var2) {
                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
                  } else {
                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.NORMAL);
                  }

                  MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var5);
                  MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("Ny:" + var7x.docinfo.get("ver"));
               } else {
                  var10001 = MainFrame.thisinstance.mp;
                  MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, var7x.errormsg);
                  GuiUtil.setcurrentpagename("");
               }

               MainFrame.thisinstance.setGlassLabel((String)null);
            }
         });
         var10.start();
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }

   public void cmd_db_check_xml(final String var1) {
      try {
         MainFrame.thisinstance.setGlassLabel("Betöltés folyamatban!");
         Thread var2 = new Thread(new Runnable() {
            public void run() {
               String var1x = "";
               String var2 = "";
               String var3 = "";
               boolean var4 = false;
               boolean var5 = false;
               String var6 = "";
               DbLoader var7 = new DbLoader(1, var1);
               BookModel var8 = var7.load((String)null, var1x, var2, var3);
               if (!var8.hasError) {
                  try {
                     MainFrame.thisinstance.setGlassLabel("Ellenőrzés folyamatban!");
                     Vector var9 = Tools.check();
                     MainFrame.thisinstance.setGlassLabel("Mentés folyamatban!");
                     if (var7.save(var9)) {
                        GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az eredmény az adatbázisban!");
                     }
                  } catch (Exception var10) {
                     var10.printStackTrace();
                  }
               } else {
                  MainPanel var10001 = MainFrame.thisinstance.mp;
                  MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
               }

               MainFrame.thisinstance.setGlassLabel((String)null);
            }
         });
         var2.start();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void cmd_db_check_xml_silent(String var1) {
      String var2 = "";
      String var3 = "";
      String var4 = "";
      DbLoader var5 = new DbLoader(1, var1);
      BookModel var6 = var5.load((String)null, var2, var3, var4);
      if (!var6.hasError) {
         try {
            Vector var7 = Tools.check();
            if (var5.save(var7)) {
               System.out.println("Az eredmény az adatbázisban!");
            }
         } catch (Exception var8) {
            var8.printStackTrace();
         }
      }

   }

   public void cmd_db_parse_xml_silent(String var1) {
      XmlQuickloader var2 = new XmlQuickloader(1, var1);
      var2.qload();
   }

   public void cmd_db_parse_xml_loop_silent(String var1) {
      XmlQuickloader var2 = new XmlQuickloader(1, var1);
      var2.loop_qload();
   }

   public void cmd_print_file_list(String var1) {
      MainFrame.thisinstance.setGlassLabel("Képgenerálás folyamatban!");
      MainFrame.thisinstance.glasslock = true;
      BatchFunctions var2 = new BatchFunctions(false, false, false);
      var2.cmdPrint2Jpg(var1);
   }

   public void cmd_pdf_print_xml_silent(String var1, boolean var2) {
      String var3 = null;
      String var4 = null;
      String var5 = null;
      IPropertyList var6 = PropertyList.getInstance();

      try {
         String[] var7 = var1.split(";");
         var3 = var7[0];
         var4 = var7[1];

         File var13;
         try {
            if (var2) {
               var5 = var7[2];
               if (var7.length == 4) {
                  var13 = new NecroFile(var7[3]);
                  if (!var13.isDirectory()) {
                     System.out.println("Hibás pdf output path");
                     this.writeerrorfile(var4, var3, ";3.44.0;;", "Hibás pdf output path " + var7[3], (Vector)null);
                     return;
                  }

                  var6.set("prop.usr.forcedPdfPath", var7[3]);
               }
            } else if (var7.length == 3) {
               var13 = new NecroFile(var7[2]);
               if (!var13.isDirectory()) {
                  System.out.println("Hibás pdf output path");
                  this.writeerrorfile(var4, var3, ";3.44.0;;", "Hibás pdf output path " + var7[2], (Vector)null);
                  return;
               }

               var6.set("prop.usr.forcedPdfPath", var7[2]);
            }
         } catch (Exception var11) {
            System.out.println("Hibás paraméterezés, nincs megadva output html file");
            return;
         }

         var13 = new NecroFile(var3);
         String var9;
         if (!var13.exists()) {
            var13 = new NecroFile((new NecroFile((String)var6.get("prop.usr.root"), (String)var6.get("prop.usr.import"))).getAbsolutePath(), var3);
            if (!var13.exists()) {
               var9 = "A megadott állomány nem található! ( " + var3 + " )";
               System.out.println(var9);
               this.writeerrorfile(var4, var3, ";3.44.0;;", var9, (Vector)null);
               return;
            }

            var3 = var13.getAbsolutePath();
         }

         var13 = new NecroFile(var4);
         var9 = var13.getParent();
         if (var9 == null) {
            var4 = (new NecroFile((new NecroFile(var3)).getParent(), var4)).getAbsolutePath();
         }
      } catch (Exception var12) {
         String var8 = "Hibás parancssor!";
         System.out.println(var8);
         this.writeerrorfile(var4, var3, ";3.44.0;;", var8, (Vector)null);
         return;
      }

      new NecroFile(var3);
      BatchFunctions var14 = new BatchFunctions(false, false, false);

      try {
         if (var3.toLowerCase().endsWith(".xml")) {
            var6.set("html.create.xml.silent", var5);
            var14.cmdPrint2PdfFromXml(var3, var4);
         } else {
            var14.cmdPrint2PdfFromList(var3, var4);
         }
      } catch (Exception var10) {
         Tools.eLog(var10, 0);
      }

      var6.set("html.create.xml.silent", (Object)null);
      var6.set("prop.usr.forcedPdfPath", (Object)null);
   }

   public Hashtable cmd_file_new_silent(String var1) {
      Hashtable var2 = new Hashtable();

      try {
         String[] var3 = var1.split(" ");
         String var4 = var3[0];
         String var5 = var3[1];
         BookModel var6 = new BookModel(new NecroFile(var4), true);
         var2.put("bookmodel", var6);
         if (var6.hasError) {
            return var2;
         }

         if (var6.isDisabledTemplate()) {
            try {
               if (BlacklistStore.getInstance().handleGuiMessage(var6.getTemplateId(), var6.getOrgId())) {
                  var6.errormsg = BlacklistStore.getInstance().getMessage(var6.getTemplateId(), var6.getOrgId());
                  var6.hasError = true;
                  return var2;
               }
            } catch (Exception var14) {
            }
         }

         try {
            MasterData[] var7 = new MasterData[]{new MasterData("Adózó adószáma", var5)};
            Entity[] var8 = (new EntityHome()).findByTypeAndMasterData("*", var7);
            EntitySelection[] var9 = new EntitySelection[]{new EntitySelection()};
            var9[0].setEntityId(var8[0].getId());
            var6.setSelectedEntities(var9);
         } catch (EntityException var13) {
            Tools.eLog(var13, 0);
         }

         var6.addForm(var6.get(0));
         FileNameResolver var17 = new FileNameResolver(var6);
         String var16 = var17.generateFileName();

         try {
            EnykInnerSaver var18 = new EnykInnerSaver(var6);
            File var10 = null;
            Result var11 = var18.save(var16, -1, true);
            var2.put("result", var11);
            if (var11.isOk()) {
               var10 = (File)var11.errorList.get(0);
               var2.put("filename", var10.getAbsolutePath());
            }
         } catch (Exception var12) {
            var12.printStackTrace();
         }
      } catch (Exception var15) {
         var2.put("exception", var15);
      }

      return var2;
   }

   public static void cmd_logprint(String var0) {
      try {
         IDbHandler var1 = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.CommonDbHandler", false);
         if (var1 != null) {
            String[] var2 = (String[])((String[])PropertyList.getInstance().get("bfoLogStoredProcParams"));
            if (var2 != null) {
               String var3 = "Nyomtatás";
               if (var2.length > 2) {
                  var3 = var2[2];
               }

               var1.bfoLog(var2[0], var2[1], var3);
            }
         }
      } catch (Exception var5) {
         System.out.println("LOG PRINTING FAILED: " + var5.toString());
         var5.printStackTrace();
      }

   }

   public void done_epost_dialog(BookModel var1) {
      if ("internet".equals(var1.epost) || "onlyinternet".equals(var1.epost)) {
         final String var2 = this.get_settingsInfo("epos");
         if (var2.indexOf("," + var1.id + ",") != -1) {
            return;
         }

         int var3 = GuiUtil.getW("Ez a nyomtatvány az ügyfélkapus regisztráció elvégzésére jogosult szervnél történő regisztrációt követően");
         final JDialog var4 = new JDialog(MainFrame.thisinstance, "Figyelem!", true);
         JCheckBox var5 = GuiUtil.getANYKCheckBox("aaa");
         Color var6 = var5.getBackground();
         String var7 = String.format("#%02x%02x%02x", var6.getRed(), var6.getGreen(), var6.getBlue());
         Color var8 = var5.getForeground();
         String var9 = String.format("#%02x%02x%02x", var8.getRed(), var8.getGreen(), var8.getBlue());
         String var10 = "<html><body bgcolor=\"" + var7 + "\" text=\"" + var9 + "\" style=\"font-family:" + var4.getFont().getFamily() + "; font-size:" + GuiUtil.getCommonFontSize() + ";\" >Ez a nyomtatvány az ügyfélkapus regisztráció elvégzésére jogosult szervnél történő regisztrációt követően kizárólag az Ügyfélkapun (<a href='http://www.magyarorszag.hu'>www.magyarorszag.hu</a>) keresztül elektronikusan adható be!<br>További információért kérjük látogasson el a <a href='http://www.magyarorszag.hu'>www.magyarorszag.hu</a> honlapra.</center></html>";
         if ("internet".equals(var1.epost)) {
            var10 = "<html><body bgcolor=\"" + var7 + "\" text=\"" + var9 + "\" style=\"font-family:" + var4.getFont().getFamily() + "; font-size:" + GuiUtil.getCommonFontSize() + ";\" >Ez a nyomtatvány az ügyfélkapus regisztráció elvégzésére jogosult szervnél történő regisztrációt követően az Ügyfélkapun (<a href='http://www.magyarorszag.hu'>www.magyarorszag.hu</a>) keresztül elektronikusan is beadható!<br>További információért kérjük látogasson el a <a href='http://www.magyarorszag.hu'>www.magyarorszag.hu</a> honlapra.</center></html>";
         }

         JEditorPane var11 = new JEditorPane("text/html", var10);
         var11.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent var1) {
               if (var1.getEventType() == EventType.ACTIVATED) {
                  Menubar.this.execute(var1.getURL().toString());
               }

            }
         });
         var11.setEditable(false);
         final JCheckBox var12 = GuiUtil.getANYKCheckBox("Ez a figyelmeztetés többet NE jelenjen meg!");
         var12.setAlignmentX(0.5F);
         var11.setFont(var4.getFont());
         var11.setEditable(false);
         var11.setSize(Math.max(600, var3), 12 * GuiUtil.getCommonItemHeight());
         var11.setPreferredSize(var11.getSize());
         JScrollPane var13 = new JScrollPane(var11, 20, 30);
         var13.setSize(Math.max(600, var3), 12 * GuiUtil.getCommonItemHeight());
         var13.setPreferredSize(var13.getSize());
         JPanel var14 = new JPanel(new BorderLayout());
         JPanel var15 = new JPanel(new BorderLayout());
         JPanel var16 = new JPanel();
         var15.add(var13, "Center");
         var12.setSize(var3 / 2 + 20, GuiUtil.getCommonItemHeight() + 4);
         var11.setBackground(var12.getBackground());
         EmptyBorder var17 = new EmptyBorder(10, 10, 10, 10);
         var13.setBorder(var17);
         var12.setPreferredSize(var12.getSize());
         var12.setBorder(var17);
         var15.add(var12, "South");
         var14.add(var15, "Center");
         final String var18 = var1.id;
         JButton var19 = new JButton("Ok");
         var19.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               if (var12.isSelected()) {
                  Menubar.this.done_settings(var2, var18, "epos");
               }

               var4.setVisible(false);
               var4.dispose();
            }
         });
         var16.add(var19);
         var14.add(var16, "South");
         int var20 = GuiUtil.getW("Ez a nyomtatvány az ügyfélkapus regisztráció elvégzésére jogosult szervnél történő regisztrációt követően");
         var15.setSize(var20, 8 * (GuiUtil.getCommonItemHeight() + 2));
         var15.setPreferredSize(var15.getSize());
         var4.getContentPane().add(var14);
         var4.setSize(var3, 12 * GuiUtil.getCommonItemHeight());
         var4.setPreferredSize(new Dimension(var3, 12 * GuiUtil.getCommonItemHeight()));
         var4.setMinimumSize(new Dimension(var3, 12 * GuiUtil.getCommonItemHeight()));
         var4.setLocationRelativeTo(MainFrame.thisinstance);
         var4.setVisible(true);
      }

   }

   public void cmd_rdb_open_readonly(String var1) {
      String[] var2 = var1.split(" ");
      if (var2.length == 4) {
         IDbHandler var3 = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.DefaultDbHandler");
         int var4 = var3.login(var2[0], var2[1], var2[2]);
         if (var4 == 0) {
            RDbLoader var5 = new RDbLoader();
            var5.done_withgui(var2[3]);
         }

      }
   }

   public void cmd_goto() {
      MainFrame.thisinstance.mp.getDMFV().gotoField();
   }

   public void cmd_open_xml(String var1) {
      File var2 = new NecroFile(var1);
      if (!var2.exists()) {
         IPropertyList var3 = PropertyList.getInstance();
         var2 = new NecroFile((new NecroFile((String)var3.get("prop.usr.root"), (String)var3.get("prop.usr.import"))).getAbsolutePath(), var1);
         if (!var2.exists()) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A megadott állomány nem található! ( " + var1 + " )", "Hiba", 0);
            return;
         }
      }

      XMLPost.cmd_edit_nem_jo(var2);
   }

   public void cmd_check_xml_silent(String var1) {
      String var2 = null;
      String var3 = null;

      try {
         String[] var4 = var1.split(";");
         var2 = var4[0];
         var3 = var4[1];
         File var5 = new NecroFile(var2);
         if (!var5.exists()) {
            IPropertyList var6 = PropertyList.getInstance();
            var5 = new NecroFile((new NecroFile((String)var6.get("prop.usr.root"), (String)var6.get("prop.usr.import"))).getAbsolutePath(), var2);
            if (!var5.exists()) {
               System.out.println("A megadott állomány nem található! ( " + var2 + " )");
               return;
            }

            var2 = var5.getAbsolutePath();
         }

         var5 = new NecroFile(var3);
         String var12 = var5.getParent();
         if (var12 == null) {
            IPropertyList var7 = PropertyList.getInstance();
            var3 = (new NecroFile((new NecroFile((String)var7.get("prop.usr.root"), (String)var7.get("prop.usr.import"))).getAbsolutePath(), var3)).getAbsolutePath();
         }
      } catch (Exception var9) {
         System.out.println("Hibás parancssor!");
         return;
      }

      try {
         if (!var2.toLowerCase().endsWith(".xml")) {
            System.out.println("Csak XML állomány adható meg!");
            return;
         }

         BatchCheck var10 = new BatchCheck(0);
         int var11 = var10.doCheckOne(var2, var3);
         if (var11 == 0) {
            System.out.println("A művelet befejeződött!");
         } else {
            System.out.println("Hibakód = " + var11);
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public void cmd_qcheck_xml_silent(String var1) {
      String var2 = null;
      String var3 = null;

      File var26;
      String var29;
      try {
         String[] var4 = var1.split(";");
         var2 = var4[0];
         var3 = var4[1];
         var26 = new NecroFile(var2);
         if (!var26.exists()) {
            IPropertyList var6 = PropertyList.getInstance();
            var26 = new NecroFile((new NecroFile((String)var6.get("prop.usr.root"), (String)var6.get("prop.usr.import"))).getAbsolutePath(), var2);
            if (!var26.exists()) {
               var29 = "A megadott állomány nem található! ( " + var2 + " )";
               System.out.println(var29);
               this.writeerrorfile(var3, var2, ";3.44.0;;", var29, (Vector)null);
               return;
            }

            var2 = var26.getAbsolutePath();
         }

         var26 = new NecroFile(var3);
         String var27 = var26.getParent();
         if (var27 == null) {
            var3 = (new NecroFile((new NecroFile(var2)).getParent(), var3)).getAbsolutePath();
         }
      } catch (Exception var23) {
         String var5 = "Hibás parancssor!";
         System.out.println(var5);
         this.writeerrorfile(var3, var2, ";3.44.0;;", var5, (Vector)null);
         return;
      }

      try {
         if (!var2.toLowerCase().endsWith(".xml")) {
            String var25 = "Csak XML állomány adható meg!";
            System.out.println(var25);
            this.writeerrorfile(var3, var2, ";3.44.0;;", var25, (Vector)null);
            return;
         }

         XMLFlyCheckLoader var24 = new XMLFlyCheckLoader();
         var26 = new NecroFile(var2);
         Hashtable var28 = var24.getHeadData(var26);
         if (var24.hasError) {
            var29 = var24.errormsg.substring(var24.errormsg.indexOf(":") + 1);
            String var30 = "Hiba a megadott állomány betöltésekor! ( " + var26 + " )\nSúlyos hiba az XML formai ellenőrzése során:\n  " + var29;
            this.writeerrorfile(var3, var2, ";3.44.0;;", var30, (Vector)null);
            return;
         }

         Hashtable var7 = (Hashtable)var28.get("docinfo");
         Hashtable var8 = new Hashtable();
         var8.put("docinfo", var7);
         Object[] var9 = new Object[]{var26, null, null, var8};
         Object[] var10 = new Object[]{var9};
         Hashtable var11 = new Hashtable();
         var11.put("selected_files", var10);
         var11.put("read_only", Boolean.FALSE);
         var11.put("function_read_only", Boolean.FALSE);
         var11.put("file_status", "");
         var8 = (Hashtable)((Hashtable)((Object[])((Object[])var10[0]))[3]).get("docinfo");
         String var12 = (String)var8.get("id");
         String var13 = (String)var8.get("templatever");
         String var14 = (String)var8.get("org");
         var24.silentheadcheck = true;
         CalculatorManager.xml = true;
         BookModel var15 = var24.load(var2, var12, var13, var14, true);
         CalculatorManager.xml = false;
         GuiUtil.setcurrentpagename("");
         var24.silentheadcheck = false;
         if (!var15.hasError) {
            String var32;
            if (var15.splitesaver.equalsIgnoreCase("true")) {
               var32 = "Az ilyen típusú nyomtatványt (több nyomtatvány egybecsomagolva) nem lehet importálni!\n ( " + var26 + " )";
               System.out.println(var32);
               this.writeerrorfile(var3, var2, ";3.44.0;" + var15.docinfo.get("id") + ";" + var15.docinfo.get("ver"), var32, (Vector)null);
               var24.destroy();
               return;
            }

            if (var24.headcheckfatalerror) {
               var32 = "A nyomtatvány súlyos hibát tartalmaz!";
               System.out.println(var32);
               this.writeerrorfile(var3, var2, ";3.44.0;" + var15.docinfo.get("id") + ";" + var15.docinfo.get("ver"), var32, var24.headerrorlist);
               var24.destroy();
               return;
            }

            if (var24.fatalerror) {
               var32 = "A megadott állomány nem létező mezőkódot tartalmaz!";
               System.out.println(var32);
               this.writeerrorfile(var3, var2, ";3.44.0;" + var15.docinfo.get("id") + ";" + var15.docinfo.get("ver"), var32, var24.warninglist);
               var24.destroy();
               return;
            }

            try {
               Vector var16 = null;
               var16 = var24.getErrorVector();
               if (var16 == null) {
                  var16 = new Vector();
               }

               if (var24.warninglist != null) {
                  var16.add(" > Figyelmeztetések:");
                  var16.addAll(var24.warninglist);
               }

               FileOutputStream var17 = new NecroFileOutputStream(var3);
               PrintWriter var18 = new PrintWriter(var17);
               var18.println(" > " + var2 + ";" + "3.44.0" + ";" + var15.docinfo.get("id") + ";" + var15.docinfo.get("ver"));

               for(int var19 = 0; var19 < var16.size(); ++var19) {
                  if (var16.get(var19).toString().startsWith(" >")) {
                     var18.println(var16.get(var19).toString().replaceAll("\r|\n", ""));
                  } else {
                     var18.println(" >> " + var16.get(var19).toString().replaceAll("\r|\n", ""));
                  }
               }

               var18.println(" > Vége");
               var18.close();
            } catch (Exception var21) {
               var21.printStackTrace();
            }

            var24.destroy();
         } else {
            String[] var34;
            if (var15.errormsg != null) {
               var34 = var15.errormsg.split(" bl_url ");
            } else {
               var34 = new String[]{""};
            }

            String var31 = "";

            try {
               var31 = var15.docinfo.get("id") + ";" + var15.docinfo.get("ver");
            } catch (Exception var20) {
               var31 = "";
            }

            Vector var33 = new Vector();
            if (var34.length > 1) {
               var33.add(var34[1]);
            }

            if (var24.warninglist != null) {
               var33.addAll(var24.warninglist);
            }

            if (var24.getErrorVector() != null) {
               var33.addAll(var24.getErrorVector());
            }

            this.writeerrorfile(var3, var2, ";3.44.0;" + var31, var34[0], var33);
         }
      } catch (Exception var22) {
         var22.printStackTrace();
         this.writeerrorfile(var3, var2, ";3.44.0;;", var22.toString(), (Vector)null);
      }

      System.out.println("Vége");
   }

   public void writeerrorfile(String var1, String var2, String var3, String var4, Vector var5) {
      try {
         FileOutputStream var6 = new NecroFileOutputStream(var1);
         PrintWriter var7 = new PrintWriter(var6);
         var7.println(" > " + var2 + var3);
         var7.println(" > " + var4);
         if (var5 != null) {
            for(int var8 = 0; var8 < var5.size(); ++var8) {
               String var9 = var5.get(var8).toString().replaceAll("\r|\n", "");
               if (var9.startsWith(" > ")) {
                  var9 = var9.substring(3);
               }

               var7.println(" >> " + var9);
            }
         }

         var7.println(" > Vége");
         var7.close();
      } catch (FileNotFoundException var10) {
         Tools.eLog(var10, 0);
      }

   }

   public void open_bookModel(BookModel var1) {
      this.open_bookModel(var1, true);
   }

   public void open_bookModel(BookModel var1, boolean var2) {
      if (!var1.hasError) {
         String var3 = "Módosítható";
         int var4 = 0;
         if (!var2) {
            FileStatusChecker var5 = FileStatusChecker.getInstance();
            var4 = var5.getExtendedFileInfo(var1.cc.getLoadedfile().getName().substring(0, var1.cc.getLoadedfile().getName().length() - ".frm.enyk".length()));
            if (var4 > 0) {
               var3 = FileStatusChecker.getStringStatus(var4);
            }
         }

         String var9 = var2 ? "Megnyitva csak olvasásra" : var3;
         DefaultMultiFormViewer var6 = new DefaultMultiFormViewer(var1);
         MainPanel var10001;
         if (var6.fv.getTp().getTabCount() == 0) {
            var1.destroy();
            var10001 = MainFrame.thisinstance.mp;
            MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
            MainFrame.thisinstance.setGlassLabel((String)null);
            MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
            MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
            MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
            return;
         }

         MainFrame.thisinstance.mp.intoleftside(var6);
         if (var1.isDisabledTemplate()) {
            TemplateUtils.getInstance().handleDisabledTemplates(var1.getTemplateId(), var1.getOrgId());
         } else {
            MainFrame.thisinstance.mp.funcreadonly = var2;
            if (!var2 && var4 <= 0) {
               var10001 = MainFrame.thisinstance.mp;
               MainFrame.thisinstance.mp.setstate(MainPanel.NORMAL);
            } else {
               var10001 = MainFrame.thisinstance.mp;
               MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
            }

            MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var9);
            if ("0".equals(MainFrame.role)) {
               Boolean var7 = (Boolean)PropertyList.getInstance().get("prop.dynamic.hasNewTemplate");
               if (var7 != null) {
                  boolean var8 = var7;
                  if (var8) {
                     CalculatorManager.getInstance().multiform_calc();
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az új sablon miatt, a mezők újraszámítása megtörtént a nyomtatványon!", "Üzenet", 1);
                  }
               }
            }

            var1.setXkr_mode(false);
            var1.setXczModeModifier(false);
         }

         MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("Ny:" + (String)var1.docinfo.get("ver"));
         StatusPane.thisinstance.currentpagename.setText(var1.name + " v:" + var1.docinfo.get("ver") + var1.helpverstr);
         MainFrame.thisinstance.setGlassLabel((String)null);
      }

   }

   public void cmd_atadas_alairashoz_xml_silent(String var1) {
      try {
         String[] var2 = var1.split(";");
         String var3 = var2[0];
         String var4 = var2[1];
         String var5 = var2[2];
         BatchFunctions var6 = new BatchFunctions(false, false, false);
         String var7 = "NO_AVDH_SIGN";
         if (var2.length > 3) {
            var7 = var2[3];
         }

         var6.passToDSign(var3, var4, var5, var7);
      } catch (HeadlessException var8) {
         var8.printStackTrace();
      }

   }

   public void cmd_krkeszites_xml_silent(String var1) {
      try {
         String[] var2 = var1.split(";");
         String var3 = var2[0];
         String var4 = var2[1];
         String var5 = var2[2];
         String var6 = var2[3];
         BatchFunctions var7 = new BatchFunctions(false, false, false);
         var7.encrypt(var3, var4, var5, var6);
      } catch (HeadlessException var8) {
         var8.printStackTrace();
      }

   }

   public void cmd_bekuldes_xcz_silent(String var1) {
      try {
         String[] var2 = var1.split(";");
         String var3 = var2[0];
         String var4 = var2[1];
         String var5 = var2[2];
         BatchFunctions var6 = new BatchFunctions(false, false, false);
         String var7 = "NO_AVDH_SIGN";
         if (var2.length > 3) {
            var7 = var2[3];
         }

         var6.xczSend(var3, var4, var5, var7);
      } catch (HeadlessException var8) {
         var8.printStackTrace();
      }

   }

   public void cmd_pdf_kr_silent(String var1) {
      try {
         String[] var2 = var1.split(";");
         String var3 = var2[0];
         String var4 = var2[1];
         String var5 = var2[2];
         BatchFunctions var6 = new BatchFunctions(false, false, false);
         var6.cmdPrint2PdfFromXml(var3, var4, var5);
      } catch (HeadlessException var7) {
         var7.printStackTrace();
      }

   }

   public void cmd_db_xml_edit(String var1) {
      String[] var2 = var1.split(" ");
      boolean var3 = true;
      IDbHandler var4 = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.DefaultDbHandler");
      var4.login(var2[0], var2[1], var2[2]);
      final String var6 = var2[3];
      final String var7 = var2[4];
      final String var8 = var2[5];

      try {
         MainFrame.thisinstance.setGlassLabel("Betöltés folyamatban!");
         Thread var9 = new Thread(new Runnable() {
            public void run() {
               String var1 = "";
               String var2 = "";
               String var3 = "";
               boolean var4 = false;
               boolean var5 = false;
               String var6x = "";
               String var7x = "utf-8";
               DbPkgLoader var8x = new DbPkgLoader(var6, var7, var8, var7x);
               BookModel var9 = var8x.load();
               var9.dbpkgloader = var8x;
               MainPanel var10001;
               if (!var9.hasError) {
                  DefaultMultiFormViewer var10 = new DefaultMultiFormViewer(var9);
                  if (var10.fv.getTp().getTabCount() == 0) {
                     var9.destroy();
                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                     MainFrame.thisinstance.setGlassLabel((String)null);
                     MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                     MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                     MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                     return;
                  }

                  MainFrame.thisinstance.mp.intoleftside(var10);
                  MainFrame.thisinstance.mp.funcreadonly = var4;
                  if (var5) {
                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.READONLY);
                  } else {
                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.NORMAL);
                  }

                  MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var6x);
                  MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("Ny:" + (String)var9.docinfo.get("ver"));
               } else {
                  var10001 = MainFrame.thisinstance.mp;
                  MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, var9.errormsg);
                  GuiUtil.setcurrentpagename("");
               }

               MainFrame.thisinstance.setGlassLabel((String)null);
            }
         });
         var9.start();
      } catch (Exception var10) {
         var10.printStackTrace();
      }

   }

   public boolean isAutoFillMode() {
      try {
         DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
         if (var1 != null) {
            var1.fv.pv.pv.leave_component_nocheck();
            BookModel var2 = var1.bm;
            return var2.autofill;
         } else {
            return false;
         }
      } catch (Exception var3) {
         return false;
      }
   }

   public void done_avdh_dialog(final String var1) {
      final String var2 = this.get_settingsInfo("avdh");
      if (var2.indexOf("," + var1 + ",") == -1) {
         final JDialog var3 = new JDialog(MainFrame.thisinstance, "Figyelem!", true);
         final JCheckBox var4 = GuiUtil.getANYKCheckBox("Ez a figyelmeztetés többet NE jelenjen meg!");
         int var5 = GuiUtil.getW(var4, var4.getText()) + 20;
         var4.setAlignmentX(0.5F);
         var4.setSize(var5, GuiUtil.getCommonItemHeight() + 2);
         var4.setPreferredSize(var4.getSize());
         JEditorPane var6 = new JEditorPane("text/html", AVDHQuestionDialog.getInfoText(false, 0));
         var6.setBackground(var4.getBackground());
         var6.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent var1) {
               if (var1.getEventType() == EventType.ACTIVATED) {
                  Menubar.this.execute(var1.getURL().toString());
               }

            }
         });
         var6.setEditable(false);
         var6.setAlignmentX(0.5F);
         int var7 = Math.max((int)(0.8D * (double)GuiUtil.getScreenW()), var5);
         byte var8 = 8;
         if (GuiUtil.getCommonFontSize() > 32) {
            var8 = 12;
         }

         var6.setSize(var5, var8 * GuiUtil.getCommonItemHeight());
         var6.setPreferredSize(var6.getSize());
         JScrollPane var9 = new JScrollPane(var6, 20, 30);
         JButton var10 = new JButton("Ok");
         var10.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1x) {
               if (var4.isSelected()) {
                  Menubar.this.done_settings(var2, var1, "avdh");
               }

               var3.setVisible(false);
               var3.dispose();
            }
         });
         JPanel var11 = new JPanel(new BorderLayout());
         var11.add(var9, "Center");
         JPanel var12 = new JPanel(new BorderLayout());
         var12.add(var4, "Center");
         JPanel var13 = new JPanel();
         var13.setLayout(new BoxLayout(var13, 0));
         var13.add(Box.createGlue());
         var13.add(var10);
         var13.add(Box.createGlue());
         var12.add(var13, "South");
         var11.add(var12, "South");
         var3.getContentPane().add(var11);
         var3.setSize(var7, 12 * GuiUtil.getCommonItemHeight());
         var3.setPreferredSize(var3.getSize());
         var3.setLocationRelativeTo(MainFrame.thisinstance);
         var3.setResizable(true);
         var3.setVisible(true);
      }
   }

   private void done_settings(String var1, String var2, String var3) {
      String var4;
      if (var1.length() == 0) {
         var4 = "," + var2 + ",";
      } else {
         var4 = var1 + var2 + ",";
      }

      IPropertyList var5 = PropertyList.getInstance();
      File var6 = new NecroFile((new NecroFile((String)var5.get("prop.usr.root"), (String)var5.get("prop.usr.settings"))).getAbsolutePath(), var3 + ".info");

      try {
         FileOutputStream var7 = new NecroFileOutputStream(var6);
         var7.write(var4.getBytes());
         var7.close();
      } catch (Exception var8) {
         Tools.eLog(var8, 0);
      }

   }

   private String get_settingsInfo(String var1) {
      IPropertyList var2 = PropertyList.getInstance();
      File var3 = new NecroFile((new NecroFile((String)var2.get("prop.usr.root"), (String)var2.get("prop.usr.settings"))).getAbsolutePath(), var1 + ".info");
      if (var3.exists()) {
         try {
            FileInputStream var4 = new FileInputStream(var3);
            BufferedReader var5 = new BufferedReader(new InputStreamReader(var4));
            String var6 = var5.readLine();
            return var6 == null ? "" : var6;
         } catch (Exception var7) {
            return "";
         }
      } else {
         return "";
      }
   }

   public void execute(String var1) {
      IOsHandler var2 = OsFactory.getOsHandler();

      try {
         File var3;
         try {
            var3 = new NecroFile(SettingsStore.getInstance().get("gui", "internet_browser"));
            if (!var3.exists()) {
               throw new Exception();
            }
         } catch (Exception var5) {
            var3 = new NecroFile(var2.getSystemBrowserPath());
         }

         var2.execute(var3.getName() + " " + var1, (String[])null, var3.getParentFile());
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public boolean save(BookModel var1) {
      if (var1.cc.getLoadedfile() != null) {
         return true;
      } else {
         try {
            FileNameResolver var3 = new FileNameResolver(var1);
            String var2 = var3.generateFileName();
            EnykInnerSaver var4 = new EnykInnerSaver(var1);
            File var5 = var4.save(var2, -1);
            if (var5 != null) {
               var1.cc.setLoadedfile(var5);
               var1.dirty = false;
            }

            return var5 != null;
         } catch (Exception var6) {
            ErrorList.getInstance().writeError(new Long(4001L), "AVDH aláírás előtti mentés nem sikerült!", IErrorList.LEVEL_ERROR, var6, (Object)null);
            return false;
         }
      }
   }

   private void handleStatusPanel() {
      String var1 = StatusPane.thisinstance.currentpagename.getText();
      int var2 = var1.indexOf("súgó:");
      if (var2 > -1) {
         var1 = var1.substring(0, var2) + "súgó: ENYT";
      } else {
         var1 = var1 + " súgó: ENYT";
      }

      StatusPane.thisinstance.currentpagename.setText(var1);
   }

   private void executeCalculator() {
      IOsHandler var1 = OsFactory.getOsHandler();
      boolean var2 = false;

      try {
         String var4 = SettingsStore.getInstance().get("gui", "sys_calculator");
         File var3;
         if (var4 != null && !"".equals(var4)) {
            var3 = new NecroFile(var4);
         } else {
            if (IOsHandler.OS_NAME.indexOf("windows") == -1) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nincs beállítva alapértelmezett számológép alkalmazás!", "Üzenet", 0);
               return;
            }

            var3 = new NecroFile("calc.exe");
            var2 = true;
         }

         if (!var2 && !var3.exists()) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A beállított alapértelmezett számológép alkalmazás nem található\n" + var4, "Üzenet", 0);
         } else {
            String var5 = var2 ? var3.getName() : var3.getAbsolutePath();
            if (var1.getOsName().toLowerCase().contains("windows")) {
               if (var5.contains(" ")) {
                  var5 = "\"" + var5 + "\"";
               }

               var1.execute(var5, (String[])null, var3.getParentFile());
            } else {
               try {
                  Runtime.getRuntime().exec(new String[]{var5});
               } catch (Exception var7) {
                  var7.printStackTrace();
               }

            }
         }
      } catch (Exception var8) {
         ErrorList.getInstance().writeError(new Integer(2000), "hiba a számológép indításakor", var8, (Object)null);
      }
   }

   private void setEditStateForBetoltErtek(boolean var1) {
      PropertyList.getInstance().set("desktop_edit_state_for_betolt_ertek", var1);
   }

   protected String[] getDocAndOrgId(Object var1) {
      if (var1 == null) {
         return null;
      } else {
         Hashtable var2 = (Hashtable)((Hashtable)var1).get("docinfo");
         return new String[]{(String)var2.get("id"), (String)var2.get("org")};
      }
   }
}
