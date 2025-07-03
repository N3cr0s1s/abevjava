package hu.piller.enykp.gui.framework;

import hu.piller.enykp.alogic.archivemanager.ArchiveManager;
import hu.piller.enykp.alogic.ebev.Ebev;
import hu.piller.enykp.alogic.fileloader.xml.XMLPost;
import hu.piller.enykp.alogic.filepanels.ABEVSavePanel;
import hu.piller.enykp.alogic.filepanels.MyABEVNewPanel;
import hu.piller.enykp.alogic.filepanels.UniversalImportPanel;
import hu.piller.enykp.alogic.filepanels.attachement.AtcTools;
import hu.piller.enykp.alogic.filepanels.attachement.EJFileChooser;
import hu.piller.enykp.alogic.filepanels.batch.BatchFunctions;
import hu.piller.enykp.alogic.filepanels.datafileoperations.CopyFromImpExpFolder;
import hu.piller.enykp.alogic.filepanels.datafileoperations.CopyFromSaveFolder;
import hu.piller.enykp.alogic.filepanels.datafileoperations.CopyToImpExpFolder;
import hu.piller.enykp.alogic.filepanels.datafileoperations.CopyToSaveFolder;
import hu.piller.enykp.alogic.filesaver.enykinner.EnykAutoFill;
import hu.piller.enykp.alogic.filesaver.imp.ImpSaver;
import hu.piller.enykp.alogic.masterdata.core.EntityException;
import hu.piller.enykp.alogic.masterdata.core.EntityHome;
import hu.piller.enykp.alogic.masterdata.gui.entityfilter.EntityFilterForm;
import hu.piller.enykp.alogic.masterdata.gui.selector.EntitySelection;
import hu.piller.enykp.alogic.masterdata.save.SaverCommandObject;
import hu.piller.enykp.alogic.masterdata.sync.SyncCommandObject;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.panels.CreateFieldDefinitionsFile;
import hu.piller.enykp.alogic.panels.FormArchiver;
import hu.piller.enykp.alogic.panels.InstalledForms;
import hu.piller.enykp.alogic.panels.SendLog;
import hu.piller.enykp.alogic.settingspanel.BaseSettingsPane;
import hu.piller.enykp.alogic.settingspanel.SettingsDialog;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.alogic.templateutils.blacklist.BlacklistStore;
import hu.piller.enykp.alogic.upgrademanager_v2_0.fileinstaller.FileInstaller;
import hu.piller.enykp.alogic.upgrademanager_v2_0.fileinstaller.JFileInstallDialog;
import hu.piller.enykp.alogic.upgrademanager_v2_0.fileinstaller.JInstallerFileChooser;
import hu.piller.enykp.alogic.upgrademanager_v2_0.gui.JUpgradeFormDialog;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldFactory;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.gui.viewer.DefaultMultiFormViewer;
import hu.piller.enykp.interfaces.ICommandObject;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.ImageUtil;
import hu.piller.enykp.util.base.InfoPanel;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.messageinfo.MessageInfo;
import hu.piller.enykp.util.icon.ENYKIconSet;
import hu.piller.krtitok.KriptoApp;
import me.necrocore.abevjava.NecroFile;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

public class ServiceMenuItem {
   public static ServiceMenuItem thisinstance;
   private String mainMenuSzerviz = "Szerviz";
   private String miBeallitasok = "Beállítások";
   private String miTorzsadatok = "Törzsadatok";
   private String miTorzsadatokMentese = "Törzsadatok mentése";
   private String miNAVTorzsadatSzinkron = "NAV törzsadatok szinkronizálása";
   private String miTitkositasSajatTanusitvannyal = "Titkosítás saját tanusítvánnyal";
   private String menuFejlesztoknek = "Fejlesztőknek";
   private String miCsv = "A mező definíciós file (.CSV) létrehozása";
   private String miTesztImport = "Teszt import file készítése (.imp)";
   private String miTesztXml = "Teszt XML file készítése (.xml)";
   private String miAutoFill = "Automatikus kitöltés tesztadatokkal";
   private String miCsatolmanyParameterek = "Csatolmány paraméterek";
   private String miTesztXCZ = "XML-t és csatolmányt tartalmazó csomag készítése";
   private String miFrissitesek = "Frissítések";
   private String miTelepites = "Telepítés";
   private String miTelepitettNyomtatvanyok = "Telepített nyomtatványok";
   private String miNyomtatvanyokArchivalasa = "Nyomtatványok archiválása";
   private String miNyomtatvanyokVisszaAzArchivbol = "Nyomtatványok visszavétele az archívumból";
   private String miArchivalasEsVisszatoltes = "Adatok archiválása és visszatöltése";
   private String menuFajlMuveletekMentesKonyvtarban = "Fájlműveletek a mentés könyvtárban";
   private String miMasolasMentesbol = "Másolás a mentés könyvtárból (átnevezés, törlés)";
   private String miMasolasMentesbe = "Másolás a mentés könyvtárba (átnevezés, törlés)";
   private String menuFajlMuveletekImportKonyvtarban = "Fájlműveletek az import/export könyvtárban";
   private String miMasolasImportbol = "Másolás az import/export könyvtárból (átnevezés, törlés)";
   private String miMasolasImportba = "Másolás az import/export könyvtárba (átnevezés, törlés)";
   private String miEgyediImport = "Egyedi importálás";
   private String miCsoportosImport = "Csoportos importálás";
   private String miTechnikaiAttoltes = "Technikai áttöltés új nyomtatványba";
   private String miUzenetek = "Üzenetek";
   private String miMegjelolesNaplo = "Megjelölés, átadás napló";
   private String miKitoltesi = "Kitöltési útmutató";
   private String miSugo = "Súgó";
   private String miNevjegy = "Névjegy";
   private ICommandObject cmdTorzsadatok;
   private ICommandObject cmdTitkositasSajatTanusitvannyal;
   private ICommandObject cmdTesztCSV;
   private ICommandObject cmdTesztImport;
   private ICommandObject cmdTesztXml;
   private ICommandObject cmdAutoFill;
   private ICommandObject cmdCsatolmanyParameterek;
   private ICommandObject cmdTesztXCZ;
   private ICommandObject cmdTelepites;
   private ICommandObject cmdTelepitettNyomtatvanyok;
   private ICommandObject cmdNyomtatvanyokArchivalasa;
   private ICommandObject cmdNyomtatvanyokVisszaAzArchivbol;
   private ICommandObject cmdArchivalasEsVisszatoltes;
   private ICommandObject cmdMasolasMentesbol;
   private ICommandObject cmdMasolasMentesbe;
   private ICommandObject cmdMasolasImportbol;
   private ICommandObject cmdMasolasImportba;
   private ICommandObject cmdEgyediImport;
   private ICommandObject cmdCsoportosImport;
   private ICommandObject cmdTechnikaiAttoltes;
   private ICommandObject cmdUzenetek;
   private ICommandObject cmdMegjelolesNaplo;
   private ICommandObject cmdKitoltesi;
   private ICommandObject cmdSugo;
   private ICommandObject cmdNAVTorzsadatSzinkron;
   public ICommandObject cmdBeallitasok;
   public ICommandObject cmdTorzsadatokMentese;
   public ICommandObject cmdNevjegy;
   public ICommandObject cmdFrissitesek;
   private Action actTorzsadatok;
   private Action actTitkositasSajatTanusitvannyal;
   private Action actTesztCSV;
   private Action actTesztImport;
   private Action actTesztXml;
   private Action actAutoFill;
   private Action actCsatolmanyParameterek;
   private Action actTesztXCZ;
   private Action actFrissitesek;
   private Action actTelepites;
   private Action actTelepitettNyomtatvanyok;
   private Action actNyomtatvanyokArchivalasa;
   private Action actNyomtatvanyokVisszaAzArchivbol;
   private Action actArchivalasEsVisszatoltes;
   private Action actMasolasMentesbol;
   private Action actMasolasMentesbe;
   private Action actMasolasImportbol;
   private Action actMasolasImportba;
   private Action actEgyediImport;
   private Action actCsoportosImport;
   private Action actTechnikaiAttoltes;
   private Action actUzenetek;
   private Action actMegjelolesNaplo;
   private Action actKitoltesi;
   private Action actSugo;
   private Action actNAVTorzsadatSzinkron;
   public Action actBeallitasok;
   public Action actTorzsadatokMentese;
   public Action actNevjegy;
   public static boolean innewtemplate = false;
   private Menubar menubar;

   public ServiceMenuItem(Menubar var1) {
      this.menubar = var1;
      thisinstance = this;
      this.initActions();
   }

   public JMenu getMainMenuItem(Hashtable var1) {
      JMenu var3 = new JMenu(this.mainMenuSzerviz);
      JMenuItem var2 = new JMenuItem(this.actBeallitasok);
      var2.putClientProperty("code", "settings");
      var1.put(this.cmdBeallitasok, var2);
      var3.add(var2);
      var3.add(new JSeparator());
      var2 = new JMenuItem(this.actTorzsadatok);
      var2.putClientProperty("code", "body");
      var1.put(this.cmdTorzsadatok, var2);
      var3.add(var2);
      var2 = new JMenuItem(this.actTorzsadatokMentese);
      var2.putClientProperty("code", "savemd");
      var1.put(this.cmdTorzsadatokMentese, var2);
      var3.add(var2);
      var2 = new JMenuItem(this.actNAVTorzsadatSzinkron);
      var2.putClientProperty("code", "syncmd");
      var1.put(this.cmdNAVTorzsadatSzinkron, var2);
      var3.add(var2);
      var3.add(new JSeparator());
      var2 = new JMenuItem(this.actTitkositasSajatTanusitvannyal);
      var2.putClientProperty("code", "mycert");
      var1.put(this.cmdTitkositasSajatTanusitvannyal, var2);
      var3.add(var2);
      JMenu var5 = new JMenu(this.menuFejlesztoknek);
      var2 = new JMenuItem(this.actTesztCSV);
      var2.putClientProperty("code", "csv");
      var1.put(this.cmdTesztCSV, var2);
      var5.add(var2);
      var2 = new JMenuItem(this.actTesztImport);
      var2.putClientProperty("code", "export");
      var1.put(this.cmdTesztImport, var2);
      var5.add(var2);
      var2 = new JMenuItem(this.actTesztXml);
      var2.putClientProperty("code", "xmlexport");
      var1.put(this.cmdTesztXml, var2);
      var5.add(var2);
      var2 = new JMenuItem(this.actAutoFill);
      var2.putClientProperty("code", "autofill");
      var1.put(this.cmdAutoFill, var2);
      var5.add(var2);
      var2 = new JMenuItem(this.actCsatolmanyParameterek);
      var2.putClientProperty("code", "atcpacktxt");
      var1.put(this.cmdCsatolmanyParameterek, var2);
      var5.add(var2);
      var2 = new JMenuItem(this.actTesztXCZ);
      var2.putClientProperty("code", "atcpack");
      var1.put(this.cmdTesztXCZ, var2);
      var5.add(var2);
      var3.add(var5);
      this.menubar.add(var3);
      var2 = new JMenuItem(this.actFrissitesek);
      var2.putClientProperty("code", "update");
      var1.put(this.cmdFrissitesek, var2);
      var3.add(var2);
      var2 = new JMenuItem(this.actTelepites);
      var2.putClientProperty("code", "install");
      var1.put(this.cmdTelepites, var2);
      var3.add(var2);
      var3.add(new JSeparator());
      var2 = new JMenuItem(this.actTelepitettNyomtatvanyok);
      var2.putClientProperty("code", "forms");
      var1.put(this.cmdTelepitettNyomtatvanyok, var2);
      var3.add(var2);
      var2 = new JMenuItem(this.actNyomtatvanyokArchivalasa);
      var2.putClientProperty("code", "formarchive");
      var1.put(this.cmdNyomtatvanyokArchivalasa, var2);
      var3.add(var2);
      var2 = new JMenuItem(this.actNyomtatvanyokVisszaAzArchivbol);
      var2.putClientProperty("code", "formarchiveback");
      var1.put(this.cmdNyomtatvanyokVisszaAzArchivbol, var2);
      var3.add(var2);
      var3.add(new JSeparator());
      var2 = new JMenuItem(this.actArchivalasEsVisszatoltes);
      var2.putClientProperty("code", "archive");
      var1.put(this.cmdArchivalasEsVisszatoltes, var2);
      var3.add(var2);
      var3.add(new JSeparator());
      var5 = new JMenu(this.menuFajlMuveletekMentesKonyvtarban);
      var2 = new JMenuItem(this.actMasolasMentesbol);
      var2.putClientProperty("code", "cp_from_save");
      var1.put(this.cmdMasolasMentesbol, var2);
      var5.add(var2);
      var2 = new JMenuItem(this.actMasolasMentesbe);
      var2.putClientProperty("code", "cp_to_save");
      var1.put(this.cmdMasolasMentesbe, var2);
      var5.add(var2);
      var3.add(var5);
      var5 = new JMenu(this.menuFajlMuveletekImportKonyvtarban);
      var2 = new JMenuItem(this.actMasolasImportbol);
      var2.putClientProperty("code", "cp_from_impexp");
      var1.put(this.cmdMasolasImportbol, var2);
      var5.add(var2);
      var2 = new JMenuItem(this.actMasolasImportba);
      var2.putClientProperty("code", "cp_to_impexp");
      var1.put(this.cmdMasolasImportba, var2);
      var5.add(var2);
      var3.add(var5);
      var3.add(new JSeparator());
      var2 = new JMenuItem(this.actEgyediImport);
      var2.putClientProperty("code", "import1");
      var1.put(this.cmdEgyediImport, var2);
      var3.add(var2);
      var2 = new JMenuItem(this.actCsoportosImport);
      var2.putClientProperty("code", "import");
      var1.put(this.cmdCsoportosImport, var2);
      var3.add(var2);
      var3.add(new JSeparator());
      var2 = new JMenuItem(this.actTechnikaiAttoltes);
      var2.putClientProperty("code", "uniimp");
      var1.put(this.cmdTechnikaiAttoltes, var2);
      var3.add(var2);
      var3.add(new JSeparator());
      var2 = new JMenuItem(this.actUzenetek);
      var2.putClientProperty("code", "naplo");
      var1.put(this.cmdUzenetek, var2);
      var3.add(var2);
      var2 = new JMenuItem(this.actMegjelolesNaplo);
      var2.putClientProperty("code", "ebevnaplo");
      var1.put(this.cmdMegjelolesNaplo, var2);
      var3.add(var2);
      var3.add(new JSeparator());
      var2 = new JMenuItem(this.actKitoltesi);
      var2.putClientProperty("code", "kiut");
      KeyStroke var4 = KeyStroke.getKeyStroke(112, 0);
      var2.setAccelerator(var4);
      var1.put(this.cmdKitoltesi, var2);
      var3.add(var2);
      var2 = new JMenuItem(this.actSugo);
      var2.putClientProperty("code", "sugo");
      var4 = KeyStroke.getKeyStroke(113, 0);
      var2.setAccelerator(var4);
      var1.put(this.cmdSugo, var2);
      var3.add(var2);
      var2 = new JMenuItem(this.actNevjegy);
      var2.putClientProperty("code", "about");
      var1.put(this.cmdNevjegy, var2);
      var3.add(var2);
      this.menubar.add(var3);
      return var3;
   }

   private void initActions() {
      ENYKIconSet var1 = ENYKIconSet.getInstance();
      this.actBeallitasok = new AbstractAction(this.miBeallitasok, var1.get("anyk_beallitasok")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdBeallitasok.execute();
         }
      };
      this.cmdBeallitasok = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 == null || var1.fv.pv.pv.leave_component()) {
               SettingsDialog var2 = new SettingsDialog();
               var2.setLocation(150, 100);
               var2.setVisible(true);
            }
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            return Boolean.TRUE;
         }
      };
      this.actTorzsadatok = new AbstractAction(this.miTorzsadatok, var1.get("body")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdTorzsadatok.execute();
         }
      };
      this.cmdTorzsadatok = new ICommandObject() {
         public void execute() {
            try {
               EntityHome.getInstance().startSession();
               new EntityFilterForm();
            } catch (final EntityException var2) {
               SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                     StringBuilder var1 = new StringBuilder("Adattár betöltési hiba!");
                     var1.append("\n");
                     var1.append(var2.getMessage());
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, var1.toString(), "Adattár megnyitási hiba", 0);
                  }
               });
            }

         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            return Boolean.TRUE;
         }
      };
      this.actTorzsadatokMentese = new AbstractAction(this.miTorzsadatokMentese, var1.get("savemd")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdTorzsadatokMentese.execute();
         }
      };
      this.cmdTorzsadatokMentese = new SaverCommandObject();
      this.actNAVTorzsadatSzinkron = new AbstractAction(this.miNAVTorzsadatSzinkron, (Icon)null) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdNAVTorzsadatSzinkron.execute();
         }
      };
      this.cmdNAVTorzsadatSzinkron = new SyncCommandObject();
      this.actTitkositasSajatTanusitvannyal = new AbstractAction(this.miTitkositasSajatTanusitvannyal, var1.get("mycert")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdTitkositasSajatTanusitvannyal.execute();
         }
      };
      this.cmdTitkositasSajatTanusitvannyal = new ICommandObject() {
         public void execute() {
            String var1 = "\\";
            ImageUtil var2 = null;

            try {
               var2 = new ImageUtil(new URL(this.getClass().getProtectionDomain().getCodeSource().getLocation().toString()));
            } catch (MalformedURLException var8) {
               Tools.eLog(var8, 0);
            }

            byte[] var3 = var2.getImageResource("resources" + var1 + "msgs_hu.properties");
            if (var3 == null) {
               var1 = "/";
               var3 = var2.getImageResource("resources/msgs_hu.properties");
            }

            HashMap var4 = new HashMap();
            var4.put("key_pair.PNG", "");
            var4.put("key_pub.PNG", "");
            var4.put("key_sec.PNG", "");
            var4.put("store_cer.PNG", "");
            var4.put("store_jks.PNG", "");
            var4.put("store_p12.PNG", "");
            var4.put("store_pgp.PNG", "");
            Iterator var5 = var4.keySet().iterator();

            while(var5.hasNext()) {
               Object var6 = var5.next();
               byte[] var7 = var2.getImageResource("resources" + var1 + "images" + var1 + var6);
               var4.put(var6, var7);
            }

            var4.put("anykFontSize", GuiUtil.getCommonFontSize() + "");
            var4.put("anykCheckBox", GuiUtil.getANYKCheckBox());
            new KriptoApp(var4, new ByteArrayInputStream(var3));
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
      this.actTesztCSV = new AbstractAction(this.miCsv, var1.get("csv")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdTesztCSV.execute();
         }
      };
      this.cmdTesztCSV = new ICommandObject() {
         public void execute() {
            CreateFieldDefinitionsFile.getInstance().execute();
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
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }
      };
      this.actTesztImport = new AbstractAction(this.miTesztImport, var1.get("export")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdTesztImport.execute();
         }
      };
      this.cmdTesztImport = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               ABEVSavePanel var2 = new ABEVSavePanel(var1.bm);
               var2.setMode("export");
               var2.setPath(new NecroFile((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.import")));
               var2.setFilters(new String[]{"imp_data_saver_v1"});
               Hashtable var3 = var2.showDialog();
               if (var3 != null && var3.size() != 0) {
                  try {
                     String var4 = (String)var3.get("file_name");
                     String var5 = (String)var3.get("file_note");
                     String var6 = var1.bm.cc.l_megjegyzes;
                     var1.bm.cc.l_megjegyzes = var5;
                     ImpSaver var7 = new ImpSaver(var1.bm);
                     var7.save(var4);
                     var1.bm.cc.l_megjegyzes = var6;
                     GuiUtil.showMessageDialog((Component)null, var4 + "\nnéven mentettük.", "Üzenet", 1);
                  } catch (Exception var8) {
                     var8.printStackTrace();
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
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }
      };
      this.actTesztXml = new AbstractAction(this.miTesztXml, var1.get("xmlexport")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdTesztXml.execute();
         }
      };
      this.cmdTesztXml = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (!var1.fv.pv.pv.leave_component()) {
                  return;
               }

               BookModel var2 = var1.bm;

               try {
                  Ebev var3 = new Ebev(var2);
                  var3.export((String)null);
               } catch (Exception var4) {
                  var4.printStackTrace();
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
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }
      };
      this.actAutoFill = new AbstractAction(this.miAutoFill, var1.get("csv")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdAutoFill.execute();
         }
      };
      this.cmdAutoFill = new ICommandObject() {
         BookModel newBm;

         public void execute() {
            try {
               MyABEVNewPanel var1 = new MyABEVNewPanel();
               final Hashtable var2 = var1.showDialog();
               Hashtable var3 = (Hashtable)((Hashtable)var2.get("selected_template_docinfo")).get("docinfo");
               if (BlacklistStore.getInstance().handleGuiMessage((String)var3.get("id"), (String)var3.get("org"))) {
                  return;
               }

               if (var2.size() != 0) {
                  DefaultMultiFormViewer var4 = MainFrame.thisinstance.mp.getDMFV();
                  if (var4 != null) {
                     var4.bm.destroy();
                     MainFrame.thisinstance.mp.intoleftside(new JPanel());
                     var4.bm = null;
                     DataFieldFactory.getInstance().freemem();
                     MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                     MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                     MainFrame.thisinstance.mp.readonlymode = false;
                     MainFrame.thisinstance.mp.funcreadonly = false;
                     MainPanel var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                     MainFrame.thisinstance.mp.set_kiut_url((String)null);
                  }

                  XMLPost.xmleditnemjo = false;
                  MainFrame.thisinstance.setGlassLabel("Betöltés folyamatban!");
                  Thread var5 = new Thread(new Runnable() {
                     public void run() {
                        Object[] var1 = (Object[])((Object[])var2.get("selected_file"));
                        File var2x = (File)var1[0];
                        Object var3 = var2.get("primary_account");
                        Object var4 = var2.get("tax_expert");
                        String var5 = var2.get("file_status").toString();
                        newBm = new BookModel(var2x);
                        if (!newBm.hasError) {
                           newBm.autofill = true;
                           newBm.setPAInfo((Object)null, (Object)null);
                           if (var2.get("selections") != null) {
                              newBm.setSelectedEntities((EntitySelection[])((EntitySelection[])var2.get("selections")));
                           }

                           ServiceMenuItem.innewtemplate = true;
                           DefaultMultiFormViewer var6 = new DefaultMultiFormViewer(newBm);
                           ServiceMenuItem.innewtemplate = false;
                           MainPanel var10001;
                           if (var6.fv.getTp().getTabCount() == 0) {
                              newBm.destroy();
                              var10001 = MainFrame.thisinstance.mp;
                              MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                              MainFrame.thisinstance.setGlassLabel((String)null);
                              MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                              MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                              MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                              return;
                           }

                           if (var6.fv.getTp().getTabCount() == 0) {
                              newBm.destroy();
                              var10001 = MainFrame.thisinstance.mp;
                              MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                              MainFrame.thisinstance.setGlassLabel((String)null);
                              MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                              MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                              MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                              return;
                           }

                           if (var3.equals("(Nincs kiválasztva)") && var4.equals("(Nincs kiválasztva)")) {
                              newBm.dirty = false;
                           }

                           try {
                              MainFrame.thisinstance.mp.intoleftside(var6);
                              var10001 = MainFrame.thisinstance.mp;
                              MainFrame.thisinstance.mp.setstate(MainPanel.NORMAL);
                              MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var5);
                              MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("Ny:" + (String)newBm.docinfo.get("ver"));
                              BaseSettingsPane.done_menuextratext(true);
                           } catch (Exception var11) {
                              MainFrame.thisinstance.mp.intoleftside(new JPanel());
                              var6.bm = null;
                              newBm.destroy();
                              SwingUtilities.invokeLater(new Runnable() {
                                 public void run() {
                                    MainFrame.thisinstance.setGlassLabel((String)null);
                                    ServiceMenuItem.this.menubar.newcmd.execute();
                                 }
                              });
                           }

                           MainFrame.thisinstance.setGlassLabel((String)null);
                           ServiceMenuItem.this.menubar.done_epost_dialog(newBm);
                           if (newBm.isAvdhModel()) {
                              ServiceMenuItem.this.menubar.done_avdh_dialog(newBm.id);
                           }

                           Object var7 = newBm.cc.getActiveObject();
                           int var8 = newBm.forms.size();
                           if (var8 > 1) {
                              for(int var9 = 0; var9 < var8; ++var9) {
                                 if (var9 != newBm.get_main_index()) {
                                    FormModel var10 = newBm.get(var9);
                                    newBm.addForm(var10);
                                 }
                              }
                           }

                           EnykAutoFill var12 = new EnykAutoFill(newBm);
                           var12.fill();
                           newBm.cc.setActiveObject(var7);
                           MainFrame.thisinstance.mp.getDMFV().fv.pv.pv.refresh();
                        } else {
                           newBm.destroy();
                           SwingUtilities.invokeLater(new Runnable() {
                              public void run() {
                                 MainFrame.thisinstance.setGlassLabel((String)null);
                                 ServiceMenuItem.this.menubar.newcmd.execute();
                              }
                           });
                        }

                     }
                  });
                  var5.start();
               }
            } catch (Exception var6) {
               var6.printStackTrace();
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
               Tools.eLog(var3, 0);
            }

            return Boolean.TRUE;
         }
      };
      this.actCsatolmanyParameterek = new AbstractAction(this.miCsatolmanyParameterek, var1.get("csv")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdCsatolmanyParameterek.execute();
         }
      };
      this.cmdCsatolmanyParameterek = new ICommandObject() {
         public void execute() {
            new AtcTools(MainFrame.thisinstance.mp.getDMFV().bm, true);
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
               Tools.eLog(var3, 0);
            }

            return Boolean.TRUE;
         }
      };
      this.actTesztXCZ = new AbstractAction(this.miTesztXCZ, var1.get("csv")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdTesztXCZ.execute();
         }
      };
      this.cmdTesztXCZ = new ICommandObject() {
         public void execute() {
            new AtcTools(MainFrame.thisinstance.mp.getDMFV().bm);
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
               Tools.eLog(var3, 0);
            }

            return Boolean.TRUE;
         }
      };
      this.actFrissitesek = new AbstractAction(this.miFrissitesek, var1.get("update")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdFrissitesek.execute();
         }
      };
      this.cmdFrissitesek = new ICommandObject() {
         public void execute() {
            String[] var1 = (String[])((String[])PropertyList.getInstance().get("orgs_notconnected.cache"));
            final JUpgradeFormDialog var8;
            if (var1 != null) {
               if (var1.length > 0) {
                  StringBuffer var2 = new StringBuffer("<html><body>Az alábbi szervezetek nyomtatványai a Szerviz -> Frissítések funkción keresztül nem frissíthetők.<br/>A frissítés az adott szervezetek oldaláról végezhető el.<br/><ul>");
                  String[] var3 = var1;
                  int var4 = var1.length;

                  for(int var5 = 0; var5 < var4; ++var5) {
                     String var6 = var3[var5];
                     String var7 = OrgInfo.getInstance().getOrgFullnameByOrgID((String)var6);
                     var2.append("<li>").append(var7).append("&nbsp;[").append((String)var6).append("]").append("</li>");
                  }

                  var2.append("</ul></html>");
                  JOptionPane.showOptionDialog(MainFrame.thisinstance, var2.toString(), "Tájékoztatás", -1, 2, (Icon)null, new Object[]{"Tovább"}, (Object)null);
                  final JUpgradeFormDialog var9 = new JUpgradeFormDialog(MainFrame.thisinstance);
                  var9.addWindowListener(new WindowAdapter() {
                     public void windowClosed(WindowEvent var1) {
                        var9.dispose();
                     }
                  });
               } else {
                  var8 = new JUpgradeFormDialog(MainFrame.thisinstance);
                  var8.addWindowListener(new WindowAdapter() {
                     public void windowClosed(WindowEvent var1) {
                        var8.dispose();
                     }
                  });
               }
            } else {
               var8 = new JUpgradeFormDialog(MainFrame.thisinstance);
               var8.addWindowListener(new WindowAdapter() {
                  public void windowClosed(WindowEvent var1) {
                     var8.dispose();
                  }
               });
            }

         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            return Boolean.TRUE;
         }
      };
      this.actTelepites = new AbstractAction(this.miTelepites, var1.get("install")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdTelepites.execute();
         }
      };
      this.cmdTelepites = new ICommandObject() {
         public void execute() {
            JInstallerFileChooser var1 = new JInstallerFileChooser();
            int var2 = var1.showOpenDialog(MainFrame.thisinstance);
            if (var2 == 0) {
               File[] var3 = var1.getSelectedFiles();
               final JFileInstallDialog var4 = new JFileInstallDialog(MainFrame.thisinstance);
               SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                     var4.setVisible(true);
                  }
               });
               FileInstaller var5 = new FileInstaller();
               var5.setFileInstallDialog(var4);
               var5.setFiles(var3);
               var4.setFiles(var3);
               var5.install();
            }

         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            return Boolean.TRUE;
         }
      };
      this.actTelepitettNyomtatvanyok = new AbstractAction(this.miTelepitettNyomtatvanyok, var1.get("forms")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdTelepitettNyomtatvanyok.execute();
         }
      };
      this.cmdTelepitettNyomtatvanyok = new ICommandObject() {
         public void execute() {
            InstalledForms.getInstance().execute();
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            return Boolean.TRUE;
         }
      };
      this.actNyomtatvanyokArchivalasa = new AbstractAction(this.miNyomtatvanyokArchivalasa, var1.get("formarchive")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdNyomtatvanyokArchivalasa.execute();
         }
      };
      this.cmdNyomtatvanyokArchivalasa = new ICommandObject() {
         public void execute() {
            FormArchiver.getInstance().execute();
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
      this.actNyomtatvanyokVisszaAzArchivbol = new AbstractAction(this.miNyomtatvanyokVisszaAzArchivbol, var1.get("formarchiveback")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdNyomtatvanyokVisszaAzArchivbol.execute();
         }
      };
      this.cmdNyomtatvanyokVisszaAzArchivbol = new ICommandObject() {
         public void execute() {
            EJFileChooser var1 = new EJFileChooser();
            File var2 = new NecroFile((String)PropertyList.getInstance().get("prop.sys.root"), "nyomtatvanyok_archivum");
            String var3 = SettingsStore.getInstance().get("userpaths", "prop.dynamic.userlastpath.fa");
            if (var3 != null) {
               var2 = new NecroFile(var3.toString());
            }

            var1.setCurrentDirectory(var2);
            var1.setDialogTitle("Archivált nyomtatvány kiválasztása");
            FileFilter var4 = new FileFilter() {
               public boolean accept(File var1) {
                  if (var1.getAbsolutePath().toLowerCase().endsWith(".tem_enyk_zip")) {
                     return true;
                  } else {
                     return var1.isDirectory();
                  }
               }

               public String getDescription() {
                  return "Archivált nyomtatványok";
               }
            };
            var1.removeChoosableFileFilter(var1.getChoosableFileFilters()[0]);
            var1.addChoosableFileFilter(var4);
            String var5 = SettingsStore.getInstance().get("userpaths", "prop.dynamic.userlastfilter.fa");
            if (var5 != null) {
               FileFilter var6 = null;
               if (var4.getDescription().equals(var5.toString())) {
                  var6 = var4;
               }

               if (var6 != null) {
                  var1.setFileFilter(var6);
               }
            }

            var1.setMultiSelectionEnabled(true);
            int var17 = var1.showOpenDialog(MainFrame.thisinstance);
            if (var17 == 0) {
               File[] var7 = var1.getSelectedFiles();
               int var8 = var7.length;

               for(int var9 = 0; var9 < var7.length; ++var9) {
                  File var10 = var7[var9];
                  if (var10 != null && var10.exists()) {
                     String var11 = null;

                     try {
                        var11 = var10.getParent();
                     } catch (Exception var16) {
                     }

                     SettingsStore.getInstance().set("userpaths", "prop.dynamic.userlastpath.fa", var11);
                     FileFilter var12 = var1.getFileFilter();
                     SettingsStore.getInstance().set("userpaths", "prop.dynamic.userlastfilter.fa", var12.getDescription());
                     File var13 = new NecroFile(ServiceMenuItem.this.getProperty("prop.dynamic.templates.absolutepath"));

                     try {
                        String var14 = Tools.unzipFile(var10.getAbsolutePath(), var13.getAbsolutePath(), (Set)null, true, false);
                        if (var14 != null) {
                           GuiUtil.showMessageDialog(MainFrame.thisinstance, "A visszaállítás nem sikerült!\nÁllománynév: " + var10.getName(), "Üzenet", 0);
                           --var8;
                        } else {
                           if (var7.length == 1) {
                              GuiUtil.showMessageDialog(MainFrame.thisinstance, "A visszaállítás sikerült!\nÁllománynév: " + var10.getName(), "Üzenet", 1);
                           }

                           var10.delete();
                        }
                     } catch (Exception var15) {
                        var15.printStackTrace();
                     }
                  }
               }

               if (var7.length != 1) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "A visszaállítás befejeződött!\n" + var8 + " darab állomány visszaállítva.", "Üzenet", 1);
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
      this.actArchivalasEsVisszatoltes = new AbstractAction(this.miArchivalasEsVisszatoltes, var1.get("archive")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdArchivalasEsVisszatoltes.execute();
         }
      };
      this.cmdArchivalasEsVisszatoltes = new ICommandObject() {
         public void execute() {
            ArchiveManager var1 = new ArchiveManager();
            var1.getArchiveManagerCommandObject().execute();
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
      this.actMasolasMentesbol = new AbstractAction(this.miMasolasMentesbol, var1.get("cpfs")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdMasolasMentesbol.execute();
         }
      };
      this.cmdMasolasMentesbol = new ICommandObject() {
         public void execute() {
            CopyFromSaveFolder.getInstance().execute();
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            return CopyFromSaveFolder.getInstance().getState(var1);
         }
      };
      this.actMasolasMentesbe = new AbstractAction(this.miMasolasMentesbe, var1.get("cpts")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdMasolasMentesbe.execute();
         }
      };
      this.cmdMasolasMentesbe = new ICommandObject() {
         public void execute() {
            CopyToSaveFolder.getInstance().execute();
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            return CopyToSaveFolder.getInstance().getState(var1);
         }
      };
      this.actMasolasImportbol = new AbstractAction(this.miMasolasImportbol, var1.get("cpfie")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdMasolasImportbol.execute();
         }
      };
      this.cmdMasolasImportbol = new ICommandObject() {
         public void execute() {
            CopyFromImpExpFolder.getInstance().execute();
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            return CopyFromImpExpFolder.getInstance().getState(var1);
         }
      };
      this.actMasolasImportba = new AbstractAction(this.miMasolasImportba, var1.get("cptie")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdMasolasImportba.execute();
         }
      };
      this.cmdMasolasImportba = new ICommandObject() {
         public void execute() {
            CopyToImpExpFolder.getInstance().execute();
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            return CopyToImpExpFolder.getInstance().getState(var1);
         }
      };
      this.actEgyediImport = new AbstractAction(this.miEgyediImport, var1.get("import1")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdEgyediImport.execute();
         }
      };
      this.cmdEgyediImport = new ICommandObject() {
         public void execute() {
            EJFileChooser var1 = new EJFileChooser();
            File var2 = new NecroFile((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.import"));
            String var3 = SettingsStore.getInstance().get("userpaths", "prop.dynamic.userlastpath.0");
            if (var3 != null) {
               var2 = new NecroFile(var3.toString());
            }

            var1.setCurrentDirectory(var2);
            var1.setDialogTitle("Import állomány kiválasztása");
            FileFilter var4 = new FileFilter() {
               public boolean accept(File var1) {
                  return var1.getAbsolutePath().toLowerCase().endsWith(".imp") ? true : var1.isDirectory();
               }

               public String getDescription() {
                  return "IMP állományok";
               }
            };
            FileFilter var5 = new FileFilter() {
               public boolean accept(File var1) {
                  return var1.getAbsolutePath().toLowerCase().endsWith(".xcz") ? true : var1.isDirectory();
               }

               public String getDescription() {
                  return "XCZ állományok";
               }
            };
            FileFilter var6 = new FileFilter() {
               public boolean accept(File var1) {
                  return var1.getAbsolutePath().toLowerCase().endsWith(".xml") || var1.isDirectory();
               }

               public String getDescription() {
                  return "XML állományok";
               }
            };
            FileFilter var7 = new FileFilter() {
               public boolean accept(File var1) {
                  return var1.getAbsolutePath().toLowerCase().endsWith(".xkr") || var1.isDirectory();
               }

               public String getDescription() {
                  return "XKR állományok";
               }
            };
            var1.removeChoosableFileFilter(var1.getChoosableFileFilters()[0]);
            var1.addChoosableFileFilter(var4);
            var1.addChoosableFileFilter(var5);
            var1.addChoosableFileFilter(var7);
            var1.addChoosableFileFilter(var6);
            String var8 = SettingsStore.getInstance().get("userpaths", "prop.dynamic.userlastfilter.0");
            if (var8 != null) {
               FileFilter var9 = null;
               if (var6.getDescription().equals(var8.toString())) {
                  var9 = var6;
               }

               if (var7.getDescription().equals(var8.toString())) {
                  var9 = var7;
               }

               if (var5.getDescription().equals(var8.toString())) {
                  var9 = var5;
               }

               if (var4.getDescription().equals(var8.toString())) {
                  var9 = var4;
               }

               if (var9 != null) {
                  var1.setFileFilter(var9);
               }
            }

            int var15 = var1.showOpenDialog(MainFrame.thisinstance);
            if (var15 == 0) {
               File var10 = var1.getSelectedFile();
               if (var10 != null && var10.exists()) {
                  String var11 = null;

                  try {
                     var11 = var10.getParent();
                  } catch (Exception var14) {
                  }

                  SettingsStore.getInstance().set("userpaths", "prop.dynamic.userlastpath.0", var11);
                  FileFilter var12 = var1.getFileFilter();
                  SettingsStore.getInstance().set("userpaths", "prop.dynamic.userlastfilter.0", var12.getDescription());
                  ErrorList.getInstance().writeError(23112, "[ " + (new SimpleDateFormat()).format(new Date()) + " ] " + var10.getAbsolutePath(), 0, (Exception)null, (Object)null);
                  BatchFunctions var13 = new BatchFunctions(true, false, true);
                  MainFrame.thisinstance.setGlassLabel("Importálás folyamatban ...");
                  PropertyList.getInstance().set("prop.dynamic.ilyenkor", (Object)null);
                  var13.cmdOne(var10.getAbsolutePath(), (String)null, true, 1);
               }
            } else {
               GuiUtil.setcurrentpagename("");
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
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }
      };
      this.actCsoportosImport = new AbstractAction(this.miCsoportosImport, var1.get("import")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdCsoportosImport.execute();
         }
      };
      this.cmdCsoportosImport = new ICommandObject() {
         public void execute() {
            new BatchFunctions(true, true, false);
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
      this.actTechnikaiAttoltes = new AbstractAction(this.miTechnikaiAttoltes, var1.get("uniimp")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdTechnikaiAttoltes.execute();
         }
      };
      this.cmdTechnikaiAttoltes = new ICommandObject() {
         public void execute() {
            UniversalImportPanel.showDialog();
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
      this.actUzenetek = new AbstractAction(this.miUzenetek, var1.get("naplo")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdUzenetek.execute();
         }
      };
      this.cmdUzenetek = new ICommandObject() {
         public void execute() {
            MessageInfo.getInstance().openDialog(MainFrame.thisinstance);
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
      this.actMegjelolesNaplo = new AbstractAction(this.miMegjelolesNaplo, var1.get("ebevnaplo")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdMegjelolesNaplo.execute();
         }
      };
      this.cmdMegjelolesNaplo = new ICommandObject() {
         public void execute() {
            SendLog.show();
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
      this.actKitoltesi = new AbstractAction(this.miKitoltesi, var1.get("kiut")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdKitoltesi.execute();
         }
      };
      this.cmdKitoltesi = new ICommandObject() {
         public void execute() {
            try {
               MainFrame.thisinstance.mp.showpanel("Kitöltési útmutató (F1)");
               DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
               if (var1 != null) {
                  PageModel var2 = var1.fv.pv.pv.getPM();
                  String var3 = (String)var2.xmlht.get("help");
                  MainFrame.thisinstance.mp.set_kiut_url(var3);
               }
            } catch (Exception var4) {
               Tools.eLog(var4, 0);
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
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }
      };
      this.actSugo = new AbstractAction(this.miSugo, var1.get("sugo")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdSugo.execute();
         }
      };
      this.cmdSugo = new ICommandObject() {
         public void execute() {
            MainFrame.thisinstance.mp.showpanel("Súgó (F2)");
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            return Boolean.TRUE;
         }
      };
      this.actNevjegy = new AbstractAction(this.miNevjegy, var1.get("about")) {
         public void actionPerformed(ActionEvent var1) {
            ServiceMenuItem.this.cmdNevjegy.execute();
         }
      };
      this.cmdNevjegy = new ICommandObject() {
         public void execute() {
            String var1 = null;
            DefaultMultiFormViewer var2 = MainFrame.thisinstance.mp.getDMFV();
            if (var2 != null) {
               try {
                  var1 = var2.bm.cc.getLoadedfile().getAbsolutePath();
               } catch (Exception var4) {
                  Tools.eLog(var4, 0);
               }
            }

            InfoPanel.getInstance().showDialog(var1);
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            return Boolean.TRUE;
         }
      };
   }

   private String getProperty(String var1) {
      return (String)PropertyList.getInstance().get(var1);
   }
}
