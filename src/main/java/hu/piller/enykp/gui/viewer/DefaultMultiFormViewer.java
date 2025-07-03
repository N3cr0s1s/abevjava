package hu.piller.enykp.gui.viewer;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.fileloader.xml.XMLPost;
import hu.piller.enykp.alogic.filepanels.ABEVSavePanel;
import hu.piller.enykp.alogic.filepanels.CsoportosAddForm;
import hu.piller.enykp.alogic.filepanels.attachement.AttachementTool;
import hu.piller.enykp.alogic.filesaver.enykinner.EnykInnerSaver;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.metainfo.MetaStore;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.datastore.Datastore_history;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.GUI_Datastore;
import hu.piller.enykp.datastore.Kihatasstore;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.extensions.db.DbFactory;
import hu.piller.enykp.extensions.db.IDbHandler;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.gui.model.VisualFieldModel;
import hu.piller.enykp.interfaces.IDataField;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.icon.ENYKIconSet;
import me.necrocore.abevjava.NecroFile;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class DefaultMultiFormViewer extends JPanel implements ActionListener {
   public BookModel bm;
   public FormViewer fv;
   JPanel mtb;
   private int gen_dbnumber = 0;
   int h = GuiUtil.getToolBarHeight();
   Dimension bdim;
   private JComboBox newcb;
   private JComboBox datacb;
   private JButton datalbl;
   private JButton newbtn;
   private JButton databtn;
   private JButton delbtn;
   private JButton checkallbtn;
   private JButton calcallbtn;
   private JButton addbtn;
   private JButton savebtn;
   private JButton vcopybtn;
   private JButton findbtn;
   private JButton calc_main;
   private int percent;
   private String newtooltip;
   private String newstr;
   private String formsstr;
   private String datatooltip;
   private String deltooltip;
   private String checkalltooltip;
   private String calcalltooltip;
   private String calcmaintooltip;
   private String addtooltip;
   private String savetooltip;
   private String vcopytooltip;
   private String findtooltip1;
   private String findtooltip2;
   private String optionstr1;
   private String optionstr2;
   private String question1;
   private String title1;
   private String title2;
   private String title3;
   private String title4;
   private String calcallstr1;
   private String calcallstr2;
   private String calcallstr3;
   private String calcallstr4;
   private String calcallstr5;
   private String calcallstr6;
   private String calcallstr7;
   private String calcmainstr1;
   private String calcmainstr2;
   private String calcmainstr3;
   private String title5;
   private String calcmainstr4;
   private String calcmainstr5;
   private String title6;
   private String calcmainstr6;
   private String calcmainstr7;
   private String title7;
   private String vcopystr1;
   private String vcopystr2;
   private String title8;
   private String vcopystr3;
   private String vcopystr4;
   private String vcopystr5;
   private String vcopystr6;
   private String vcopystr8;
   private String title9;
   private String optionstr3;
   private String optionstr4;
   private String title10;
   private String findstr1;
   private String nomorestr;
   private String title11;
   private String delstr1;
   private String delstr2;
   private String title12;
   private String delstr3;
   private String delstr4;
   private String findstr10;
   private String findstr11;
   private String findstr12;
   private String title13;
   private String findstr14;
   private String findstr15;
   private String prevfindstr;

   public DefaultMultiFormViewer(BookModel var1) {
      this.bdim = new Dimension(this.h, this.h);
      this.newtooltip = "A kiválasztott nyomtatvány létrehozása";
      this.newstr = "Új ";
      this.formsstr = "Nyomtatványok";
      this.datatooltip = "A kiválasztott nyomtatvány megjelenítése";
      this.deltooltip = "A megjelenített nyomtatvány törlése";
      this.checkalltooltip = "Összes nyomtatvány ellenőrzése";
      this.calcalltooltip = "Összes nyomtatvány újraszámítása";
      this.calcmaintooltip = "A főnyomtatvány újraszámítása";
      this.addtooltip = "Állomány beemelése";
      this.savetooltip = "Állomány kimentése";
      this.vcopytooltip = "Érték másolása";
      this.findtooltip1 = "Nyomtatvány kiválasztása mezőérték alapján,";
      this.findtooltip2 = "az aktuális utáni nyomtatványtól kezdve";
      this.optionstr1 = "Igen";
      this.optionstr2 = "Nem";
      this.question1 = "Indulhat az összes nyomtatvány ellenőrzése?";
      this.title1 = "Ellenőrzés";
      this.title2 = "Figyelmeztetés";
      this.title3 = "Újraszámítás";
      this.title4 = "Üzenet";
      this.calcallstr1 = "A mezők újraszámítása nem lehetséges,";
      this.calcallstr2 = " mert ki van kapcsolva a funkció.";
      this.calcallstr3 = "A Szerviz/Beállításoknál (Működés lap) kapcsolhatja vissza!";
      this.calcallstr4 = "Indulhat a számított mezők újraszámítása";
      this.calcallstr5 = "az összes nyomtatványon?";
      this.calcallstr6 = "A mezők újraszámítása megtörtént az összes nyomtatványon!";
      this.calcallstr7 = "Mezőszámítás folyamatban";
      this.calcmainstr1 = "A mezők újraszámítása nem lehetséges,";
      this.calcmainstr2 = " mert ki van kapcsolva a funkció.";
      this.calcmainstr3 = "A Szerviz/Beállításoknál (Működés lap) kapcsolhatja vissza!";
      this.title5 = "Figyelmeztetés";
      this.calcmainstr4 = "Indulhat a számított mezők újraszámítása";
      this.calcmainstr5 = "a főnyomtatványon?";
      this.title6 = "Újraszámítás";
      this.calcmainstr6 = "A mezők újraszámítása megtörtént a főnyomtatványon!";
      this.calcmainstr7 = "Mezőszámítás folyamatban";
      this.title7 = "Üzenet";
      this.vcopystr1 = "Nem volt kijelölt mező!";
      this.vcopystr2 = "Nem hajtható végre a művelet!";
      this.title8 = "Érték másolása";
      this.vcopystr3 = "Dinamikus lapon csak az első oldalon értelmezett ez a funkció!";
      this.vcopystr4 = "A funkció használata után az újraszámítás le fog futni!";
      this.vcopystr5 = "Másolás befejezve!";
      this.vcopystr6 = "A mezők újraszámítása megtörtént az összes nyomtatványon!";
      this.vcopystr8 = "Másolás közben hiba keletkezett!";
      this.title9 = "Üzenet";
      this.optionstr3 = "Tovább";
      this.optionstr4 = "Mégsem";
      this.title10 = "Érték keresése";
      this.findstr1 = "Keresés közben hiba keletkezett!";
      this.nomorestr = "Ebből a típusú nyomtatványból többet nem lehet létrehozni!";
      this.title11 = "Figyelmeztetés";
      this.delstr1 = "Az utolsó dokumentumot nem lehet törölni!";
      this.delstr2 = "A fő dokumentumot nem lehet törölni!";
      this.title12 = "Adatok törlése";
      this.delstr3 = "Törli az aktuális nyomtatványt?";
      this.delstr4 = " adatait fogja törölni!";
      this.findstr10 = "Keresés az összes mezőben.";
      this.findstr11 = "Keresés a kiválasztott mező alapján.";
      this.findstr12 = "Adja meg a keresendő értéket:";
      this.title13 = "Nyomtatvány kiválasztása";
      this.findstr14 = "Keresés befejezve!";
      this.findstr15 = " Nincs ilyen érték";
      this.bm = var1;
      this.setName("dmfv");
      this.buid();
      InputMap var2 = this.getInputMap(2);
      ActionMap var3 = this.getActionMap();
      String var4 = "showlist";
      var2.put(KeyStroke.getKeyStroke("ctrl Y"), var4);
      AbstractAction var5 = new AbstractAction("showlist", (Icon)null) {
         public void actionPerformed(ActionEvent var1) {
            if (DefaultMultiFormViewer.this.bm != null) {
               DefaultMultiFormViewer.this.bm.showDialog(MainFrame.thisinstance);
            }

         }
      };
      var3.put(var4, var5);
      var4 = "calcall";
      var2.put(KeyStroke.getKeyStroke("ctrl O"), var4);
      AbstractAction var6 = new AbstractAction("calcall", (Icon)null) {
         public void actionPerformed(ActionEvent var1) {
            try {
               if (1 < DefaultMultiFormViewer.this.bm.size()) {
                  DefaultMultiFormViewer.this.calcallbtn.doClick();
               }
            } catch (Exception var3) {
            }

         }
      };
      var3.put(var4, var6);
   }

   private void buid() {
      this.setLayout(new BorderLayout());
      FormModel var1;
      if (1 == this.bm.size()) {
         var1 = this.bm.get(0);
         if (var1 != null) {
            if (this.bm.cc.size() == 0) {
               this.bm.addForm(var1);
            } else {
               this.bm.cc.setActiveObject(this.bm.cc.get(0));
               this.bm.setCalcelemindex(0);
            }

            this.fv = new FormViewer(var1);
            this.add(this.fv, "Center");
         }
      } else {
         if (this.bm.cc.size() == 0) {
            var1 = this.bm.get_main_formmodel();
            if (var1 != null) {
               this.bm.addForm(var1);
               this.fv = new FormViewer(var1);
               this.add(this.fv, "Center");
            }
         } else {
            var1 = this.bm.get(((Elem)this.bm.cc.get(0)).getType());
            this.bm.cc.setActiveObject(this.bm.cc.get(0));
            this.bm.setCalcelemindex(0);
            this.fv = new FormViewer(var1);
            this.add(this.fv, "Center");
         }

         this.add(this.createMultiToolbar(), "North");
      }

   }

   private JPanel createMultiToolbar() {
      ENYKIconSet var1 = ENYKIconSet.getInstance();
      this.mtb = new JPanel();
      this.mtb.setLayout(new BoxLayout(this.mtb, 0));
      new JLabel(this.newstr);
      this.newbtn = new JButton();
      this.newbtn.setName("multinew");
      this.newbtn.setIcon(var1.get("anyk_uj_dokumentum"));
      this.newbtn.setPreferredSize(this.bdim);
      this.newbtn.setMaximumSize(this.bdim);
      this.newbtn.setMinimumSize(this.bdim);
      this.newbtn.setToolTipText(this.newtooltip);
      this.newbtn.setActionCommand("new");
      this.newbtn.addActionListener(this);
      this.newbtn.setFocusable(false);
      this.newcb = new JComboBox(this.createcombomodel());
      this.newcb.setName("multinewcb");
      this.newcb.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
         }
      });
      this.newcb.setFocusable(false);
      this.newcb.setMinimumSize(new Dimension(10, 10));
      this.mtb.add(this.newcb);
      this.mtb.add(this.newbtn);
      if ("1".equals(MainFrame.opmode) && !MainFrame.rogzitomode) {
         this.newbtn.setEnabled(false);
      }

      Component var3 = Box.createHorizontalGlue();
      this.mtb.add(var3);
      this.datalbl = new JButton(this.formsstr);
      this.datalbl.setMinimumSize(new Dimension(10, 1));
      this.datalbl.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            DefaultMultiFormViewer.this.bm.showDialog(MainFrame.thisinstance);
         }
      });
      this.databtn = new JButton();
      this.databtn.setPreferredSize(this.bdim);
      this.databtn.setToolTipText(this.datatooltip);
      this.databtn.setActionCommand("show");
      this.databtn.addActionListener(this);
      this.databtn.setFocusable(false);
      this.datacb = new JComboBox(this.createdatacombomodel());
      this.datacb.setName("multidatacb");
      this.datacb.setFocusable(false);
      this.datacb.setActionCommand("show");
      this.datacb.addActionListener(this);
      this.datacb.setMinimumSize(new Dimension(10, 10));
      this.delbtn = new JButton();
      this.delbtn.setName("multidelbtn");
      this.delbtn.setIcon(var1.get("anyk_m_nyomtatvany_torlese"));
      this.delbtn.setPreferredSize(this.bdim);
      this.delbtn.setToolTipText(this.deltooltip);
      this.delbtn.setActionCommand("del");
      this.delbtn.addActionListener(this);
      this.delbtn.setFocusable(false);
      this.delbtn.setMaximumSize(this.bdim);
      this.delbtn.setMinimumSize(this.bdim);
      this.checkallbtn = new JButton();
      this.checkallbtn.setName("checkallbtn");
      this.checkallbtn.setIcon(var1.get("anyk_o_nyomtatvany_ellenorzese_torles"));
      this.checkallbtn.setPreferredSize(this.bdim);
      this.checkallbtn.setToolTipText(this.checkalltooltip);
      this.checkallbtn.setFocusable(false);
      this.checkallbtn.setMaximumSize(this.bdim);
      this.checkallbtn.setMinimumSize(this.bdim);
      this.checkallbtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (DefaultMultiFormViewer.this.fv.pv.pv.leave_component()) {
               Object[] var2 = new Object[]{DefaultMultiFormViewer.this.optionstr1, DefaultMultiFormViewer.this.optionstr2};
               int var3 = JOptionPane.showOptionDialog(MainFrame.thisinstance, DefaultMultiFormViewer.this.question1, DefaultMultiFormViewer.this.title1, 0, 3, (Icon)null, var2, var2[0]);
               if (var3 == 0) {
                  if (MainFrame.opmode != null && !MainFrame.opmode.equals("0")) {
                     System.out.println("calling_check_all - Menu_Check");
                  }

                  CalculatorManager.getInstance().multiformcheck();
               }
            }
         }
      });
      this.calcallbtn = new JButton();
      this.calcallbtn.setName("calcallbtn");
      this.calcallbtn.setIcon(var1.get("anyk_o_nyomtatvany_ujraszámítása"));
      this.calcallbtn.setPreferredSize(this.bdim);
      this.calcallbtn.setToolTipText(this.calcalltooltip);
      this.calcallbtn.setFocusable(false);
      this.calcallbtn.setMaximumSize(this.bdim);
      this.calcallbtn.setMinimumSize(this.bdim);
      this.calcallbtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (DefaultMultiFormViewer.this.fv.pv.pv.leave_component()) {
               boolean var2 = false;
               String var3 = SettingsStore.getInstance().get("gui", "mezőszámítás");
               if (var3 != null && var3.equals("true")) {
                  var2 = true;
               }

               if (!var2) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, DefaultMultiFormViewer.this.calcallstr1 + "\n" + DefaultMultiFormViewer.this.calcallstr2 + "\n" + DefaultMultiFormViewer.this.calcallstr3, DefaultMultiFormViewer.this.title2, 2);
               } else {
                  Object[] var4 = new Object[]{DefaultMultiFormViewer.this.optionstr1, DefaultMultiFormViewer.this.optionstr2};
                  int var5 = JOptionPane.showOptionDialog(MainFrame.thisinstance, DefaultMultiFormViewer.this.calcallstr4 + "\n" + DefaultMultiFormViewer.this.calcallstr5, DefaultMultiFormViewer.this.title3, 0, 3, (Icon)null, var4, var4[0]);
                  if (var5 == 0) {
                     CalculatorManager.getInstance().closeDialog();
                     Thread var6 = new Thread(new Runnable() {
                        public void run() {
                           MainFrame.recalc_in_progress = true;
                           CalculatorManager.getInstance().multiform_calc();
                           MainFrame.recalc_in_progress = false;
                           DefaultMultiFormViewer.this.fv.pv.refresh();
                           MainFrame.thisinstance.setGlassLabel((String)null);
                           GuiUtil.showMessageDialog(MainFrame.thisinstance, DefaultMultiFormViewer.this.calcallstr6, DefaultMultiFormViewer.this.title4, 1);
                        }
                     });
                     MainFrame.thisinstance.setGlassLabel(DefaultMultiFormViewer.this.calcallstr7);
                     var6.start();
                  }
               }
            }
         }
      });
      this.calc_main = new JButton();
      this.calc_main.setName("calc_main");
      this.calc_main.setIcon(var1.get("anyk_fonyomtatvany_ujraszamitasa"));
      this.calc_main.setPreferredSize(this.bdim);
      this.calc_main.setToolTipText(this.calcmaintooltip);
      this.calc_main.setFocusable(false);
      this.calc_main.setMaximumSize(this.bdim);
      this.calc_main.setMinimumSize(this.bdim);
      this.calc_main.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (DefaultMultiFormViewer.this.fv.pv.pv.leave_component()) {
               boolean var2 = false;
               String var3 = SettingsStore.getInstance().get("gui", "mezőszámítás");
               if (var3 != null && var3.equals("true")) {
                  var2 = true;
               }

               if (MainFrame.isPart && MainFrame.isPartOnlyMain) {
                  var2 = true;
               }

               if (!var2) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, DefaultMultiFormViewer.this.calcmainstr1 + "\n" + DefaultMultiFormViewer.this.calcmainstr2 + "\n" + DefaultMultiFormViewer.this.calcmainstr3, DefaultMultiFormViewer.this.title5, 2);
               } else {
                  Object[] var4 = new Object[]{DefaultMultiFormViewer.this.optionstr1, DefaultMultiFormViewer.this.optionstr2};
                  int var5 = JOptionPane.showOptionDialog(MainFrame.thisinstance, DefaultMultiFormViewer.this.calcmainstr4 + "\n" + DefaultMultiFormViewer.this.calcmainstr5, DefaultMultiFormViewer.this.title6, 0, 3, (Icon)null, var4, var4[0]);
                  if (var5 == 0) {
                     CalculatorManager.getInstance().closeDialog();
                     Thread var6 = new Thread(new Runnable() {
                        public void run() {
                           MainFrame.recalc_in_progress = true;
                           CalculatorManager.getInstance().multiform_calc_main();
                           MainFrame.recalc_in_progress = false;
                           DefaultMultiFormViewer.this.fv.pv.refresh();
                           MainFrame.thisinstance.setGlassLabel((String)null);
                           GuiUtil.showMessageDialog(MainFrame.thisinstance, DefaultMultiFormViewer.this.calcmainstr6, DefaultMultiFormViewer.this.title7, 1);
                        }
                     });
                     MainFrame.thisinstance.setGlassLabel(DefaultMultiFormViewer.this.calcmainstr7);
                     var6.start();
                  }
               }
            }
         }
      });
      this.addbtn = new JButton();
      this.addbtn.setName("addbtn");
      this.addbtn.setIcon(var1.get("anyk_allomany_beemelese"));
      this.addbtn.setPreferredSize(this.bdim);
      this.addbtn.setToolTipText(this.addtooltip);
      this.addbtn.setFocusable(false);
      this.addbtn.setMaximumSize(this.bdim);
      this.addbtn.setMinimumSize(this.bdim);
      this.addbtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (DefaultMultiFormViewer.this.fv.pv.pv.leave_component()) {
               CalculatorManager.getInstance().closeDialog();

               try {
                  int var2 = DefaultMultiFormViewer.this.bm.cc.size();
                  new CsoportosAddForm(MainFrame.thisinstance, DefaultMultiFormViewer.this.bm);
                  DefaultMultiFormViewer.this.datacb.updateUI();
                  Hashtable var4 = new Hashtable();
                  Elem var5 = (Elem)DefaultMultiFormViewer.this.bm.cc.get(DefaultMultiFormViewer.this.bm.cc.getActiveObjectindex());
                  var4.put("id", var5.getType());
                  var4.put("guiobject", var5);
                  DefaultMultiFormViewer.this.bm.calculator.eventFired(var4);
                  boolean var6 = false;
                  String var7 = SettingsStore.getInstance().get("gui", "mezőszámítás");
                  if (var7 != null && var7.equals("true")) {
                     var6 = true;
                  }

                  if (var6 && DefaultMultiFormViewer.this.bm.cc.size() != var2 && !XMLPost.xmleditnemjo) {
                     CalculatorManager.getInstance().multiform_calc_main();
                     DefaultMultiFormViewer.this.fv.pv.refresh();
                  }
               } catch (Exception var8) {
               }

            }
         }
      });
      this.savebtn = new JButton();
      this.savebtn.setName("savebtn");
      this.savebtn.setIcon(var1.get("anyk_allomany_kimentese"));
      this.savebtn.setPreferredSize(this.bdim);
      this.savebtn.setToolTipText(this.savetooltip);
      this.savebtn.setFocusable(false);
      this.savebtn.setMaximumSize(this.bdim);
      this.savebtn.setMinimumSize(this.bdim);
      this.savebtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (DefaultMultiFormViewer.this.fv.pv.pv.leave_component()) {
               CalculatorManager.getInstance().closeDialog();

               try {
                  ABEVSavePanel var2 = new ABEVSavePanel(DefaultMultiFormViewer.this.bm);
                  var2.setMode("multi1");
                  var2.setPath(new NecroFile((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.saves")));
                  var2.setFilters(new String[]{"inner_data_saver_v1"});
                  Hashtable var3 = var2.showDialog();
                  if (var3 != null && var3.size() != 0) {
                     String var4 = (String)var3.get("file_name");
                     String var5 = (String)var3.get("file_note");
                     String var6 = DefaultMultiFormViewer.this.bm.cc.l_megjegyzes;
                     DefaultMultiFormViewer.this.bm.cc.l_megjegyzes = var5;
                     ((Elem)DefaultMultiFormViewer.this.bm.cc.getActiveObject()).getEtc().put("orignote", var5);
                     EnykInnerSaver var7 = new EnykInnerSaver(DefaultMultiFormViewer.this.bm);
                     var7.save(var4, DefaultMultiFormViewer.this.datacb.getSelectedIndex());
                     DefaultMultiFormViewer.this.bm.cc.l_megjegyzes = var6;
                  }
               } catch (Exception var8) {
                  var8.printStackTrace();
               }

            }
         }
      });
      this.vcopybtn = new JButton();
      this.vcopybtn.setName("vcopybtn");
      this.vcopybtn.setIcon(var1.get("anyk_ertek_masolasa"));
      this.vcopybtn.setPreferredSize(this.bdim);
      this.vcopybtn.setToolTipText(this.vcopytooltip);
      this.vcopybtn.setFocusable(false);
      this.vcopybtn.setMaximumSize(this.bdim);
      this.vcopybtn.setMinimumSize(this.bdim);
      this.vcopybtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            CalculatorManager.getInstance().closeDialog();

            try {
               if (DefaultMultiFormViewer.this.fv == null) {
                  return;
               }

               if (DefaultMultiFormViewer.this.bm.cc.size() == 0) {
                  return;
               }

               KeyboardFocusManager var2 = KeyboardFocusManager.getCurrentKeyboardFocusManager();
               Component var3 = var2.getPermanentFocusOwner();
               String var4 = "";
               String var5 = "";
               String var6 = "";
               if (!(var3 instanceof IDataField)) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, DefaultMultiFormViewer.this.vcopystr1 + "\n" + DefaultMultiFormViewer.this.vcopystr2, DefaultMultiFormViewer.this.title8, 1);
                  return;
               }

               PageViewer var8 = (PageViewer)var3.getParent();
               boolean var7 = var8.PM.dynamic;
               var5 = var8.current_df.key;
               var6 = ((IDataField)var3).getFieldValue().toString();
               var4 = ((Elem)DefaultMultiFormViewer.this.bm.cc.getActiveObject()).getType();
               if (var7) {
                  var8 = (PageViewer)var3.getParent();
                  if (var8.dynindex != 0) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, DefaultMultiFormViewer.this.vcopystr3, DefaultMultiFormViewer.this.title8, 1);
                     return;
                  }

                  var7 = false;
               }

               String var17 = var6;
               if (var6.equals("true")) {
                  var17 = "X";
               }

               if (var6.equals("false")) {
                  var17 = "";
               }

               Object[] var9 = new Object[]{DefaultMultiFormViewer.this.optionstr3, DefaultMultiFormViewer.this.optionstr4};
               int var10 = JOptionPane.showOptionDialog(MainFrame.thisinstance, DefaultMultiFormViewer.this.vcopystr4, DefaultMultiFormViewer.this.title8, 0, 3, (Icon)null, var9, var9[1]);
               if (var10 != 0) {
                  return;
               }

               Object[] var11 = new Object[]{DefaultMultiFormViewer.this.optionstr1, DefaultMultiFormViewer.this.optionstr2};
               var10 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Indulhat a \"" + var17 + "\" érték másolása,\n az összes " + var4 + " nyomtatvány azonos mezőjébe?", DefaultMultiFormViewer.this.title8, 0, 3, (Icon)null, var11, var11[1]);
               if (var10 != 0) {
                  return;
               }

               for(int var12 = 0; var12 < DefaultMultiFormViewer.this.bm.cc.size(); ++var12) {
                  Elem var13 = (Elem)DefaultMultiFormViewer.this.bm.cc.get(var12);
                  if (var13.getType().equals(var4) && !var7) {
                     IDataStore var14 = (IDataStore)var13.getRef();
                     String var15 = "0_" + var5;
                     var14.set(var15, var6);
                     DefaultMultiFormViewer.this.done_ds_history(var13, var5, var6, var15);
                  }
               }

               GuiUtil.showMessageDialog(MainFrame.thisinstance, DefaultMultiFormViewer.this.vcopystr5, DefaultMultiFormViewer.this.title8, 1);
               Boolean var18 = (Boolean)PropertyList.getInstance().get("prop.dynamic.dirty2");
               CalculatorManager.getInstance().multiform_calc();
               PropertyList.getInstance().set("prop.dynamic.dirty2", var18);
               DefaultMultiFormViewer.this.fv.pv.refresh();
               GuiUtil.showMessageDialog(MainFrame.thisinstance, DefaultMultiFormViewer.this.vcopystr6, DefaultMultiFormViewer.this.title9, 1);
            } catch (Exception var16) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, DefaultMultiFormViewer.this.vcopystr8, DefaultMultiFormViewer.this.title8, 0);
            }

         }
      });
      this.findbtn = new JButton();
      this.findbtn.setName("findbtn");
      this.findbtn.setIcon(var1.get("anyk_kitoltes_mezoertek_alapjan"));
      this.findbtn.setPreferredSize(this.bdim);
      this.findbtn.setToolTipText(this.findtooltip1 + "\n" + this.findtooltip2);
      this.findbtn.setFocusable(false);
      this.findbtn.setMaximumSize(this.bdim);
      this.findbtn.setMinimumSize(this.bdim);
      this.findbtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            CalculatorManager.getInstance().closeDialog();

            try {
               if (DefaultMultiFormViewer.this.fv == null) {
                  return;
               }

               if (DefaultMultiFormViewer.this.bm.cc.size() == 0) {
                  return;
               }

               KeyboardFocusManager var2 = KeyboardFocusManager.getCurrentKeyboardFocusManager();
               Component var3 = var2.getPermanentFocusOwner();
               String var4 = "";
               String var5 = "";
               String var6 = "";
               boolean var7 = false;
               var4 = ((Elem)DefaultMultiFormViewer.this.bm.cc.getActiveObject()).getType();
               if (var3 instanceof IDataField) {
                  PageViewer var8 = (PageViewer)var3.getParent();
                  var5 = var8.current_df.key;
                  var6 = ((IDataField)var3).getFieldValue().toString();
                  boolean var9 = var8.current_df.type == 1;
                  DefaultMultiFormViewer.this.findstr(var6, var4, var5, var9);
               } else {
                  DefaultMultiFormViewer.this.findstr(var6, var4, (String)null, false);
               }
            } catch (Exception var10) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, DefaultMultiFormViewer.this.findstr1, DefaultMultiFormViewer.this.title10, 0);
            }

         }
      });
      this.mtb.add(this.datalbl);
      this.mtb.add(this.datacb);
      this.mtb.add(this.delbtn);
      this.mtb.add(this.checkallbtn);
      this.mtb.add(this.calcallbtn);
      this.mtb.add(this.calc_main);
      this.mtb.add(this.addbtn);
      this.mtb.add(this.savebtn);
      this.mtb.add(this.vcopybtn);
      this.mtb.add(this.findbtn);
      if ("1".equals(MainFrame.opmode)) {
         this.addbtn.setVisible(false);
         this.savebtn.setVisible(false);
      }

      if (XMLPost.xmleditnemjo) {
         this.savebtn.setVisible(false);
      }

      if (MainFrame.rogzitomode) {
         this.checkallbtn.setVisible(false);
         this.calcallbtn.setVisible(false);
         this.calc_main.setVisible(false);
         this.addbtn.setVisible(false);
         this.savebtn.setVisible(false);
         this.vcopybtn.setVisible(false);
         this.findbtn.setVisible(false);
      }

      return this.mtb;
   }

   private void done_ds_history(Elem var1, String var2, String var3, String var4) {
      if (!MainFrame.role.equals("0")) {
         Datastore_history var5 = (Datastore_history)var1.getEtc().get("history");
         if (var5 != null) {
            Vector var6 = var5.get(var4);
            if (var6 == null) {
               var6 = new Vector();
               var6.setSize(4);
               var5.set(var4, var6);
            }

            byte var7 = 1;
            if (MainFrame.role.equals("2") || MainFrame.role.equals("3")) {
               var7 = 2;
            }

            if (var3.equals("true")) {
               var3 = "X";
            }

            if (var3.equals("false")) {
               var3 = "";
            }

            var6.set(var7, var3);
         }
      }
   }

   private ComboBoxModel createcombomodel() {
      DefaultComboBoxModel var1 = new DefaultComboBoxModel(this.bm.forms);
      return var1;
   }

   private ComboBoxModel createdatacombomodel() {
      DefaultComboBoxModel var1 = new DefaultComboBoxModel(this.bm.cc);
      return var1;
   }

   public void actionPerformed(ActionEvent var1) {
      int var2;
      FormModel var14;
      if (var1.getActionCommand().equals("new")) {
         if (!this.fv.pv.pv.leave_component()) {
            return;
         }

         var2 = this.newcb.getSelectedIndex();
         if (this.bm.maxcreation[var2] <= this.bm.created[var2]) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, this.nomorestr, this.title11, 2);
            return;
         }

         String[] var3 = null;

         try {
            IDbHandler var4 = DbFactory.getDbHandler();
            if (var4 == null && "1".equals(MainFrame.opmode)) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Belső módban lett indítva a program, de nincs elérhető adatbáziskapcsolat.\nA dbhandler nem elérhető.", this.title11, 2);
            }

            if (var4 != null && "1".equals(MainFrame.opmode)) {
               Object var5 = var4.handleNewPage();
               if (var5 instanceof String) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, var5, this.title11, 2);
                  return;
               }

               var3 = (String[])((String[])var5);
            }
         } catch (Exception var12) {
         }

         var14 = this.bm.get(var2);
         if (var14 != null) {
            this.bm.addForm(var14);
            this.datacb.setSelectedIndex(this.bm.cc.size() - 1);
            Datastore_history var15 = new Datastore_history();
            Elem var6 = (Elem)this.bm.cc.getActiveObject();
            var6.getEtc().put("history", var15);
            GUI_Datastore var7 = (GUI_Datastore)var6.getRef();
            var7.setSavepoint(this.bm);
            if (var3 != null) {
               var6.getEtc().put("dbparams", var3);
            }

            BigDecimal var8 = null;

            try {
               var8 = new BigDecimal(((String[])((String[])var6.get("dbparams")))[1]);
            } catch (Exception var11) {
               var8 = new BigDecimal(-1);
            }

            Kihatasstore var9 = new Kihatasstore(this.bm.cc, new Integer(this.bm.cc.size()), var8);
            var6.getEtc().put("kihatas", var9);
            --this.gen_dbnumber;
            var6.getEtc().put("gen_dbnumber", this.gen_dbnumber + "");
         }

         if (MainFrame.FTRmode && MainFrame.FTRdoc != null) {
            MainFrame.FTRmezok = MainFrame.FTRdoc.createElement("mezok");
            MainFrame.FTRmezok.setAttribute("formid", var14.id);
            MainFrame.FTRroot.appendChild(MainFrame.FTRmezok);
         }
      } else if (var1.getActionCommand().equals("show")) {
         try {
            if (!this.fv.pv.pv.leave_component()) {
               return;
            }
         } catch (Exception var10) {
         }

         var2 = this.datacb.getSelectedIndex();
         Elem var13 = (Elem)this.bm.cc.get(var2);
         this.bm.cc.setActiveObject(var13);
         var14 = this.bm.get(var13.getType());
         if (this.fv != null) {
            this.remove(this.fv);
         }

         this.fv = new FormViewer(var14);
         this.add(this.fv, "Center");
         this.revalidate();
      } else if (var1.getActionCommand().equals("del")) {
         CalculatorManager.getInstance().closeDialog();
         this.delcurrentform();
      }

   }

   private void delcurrentform() {
      if (this.bm.cc.size() == 1) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, this.delstr1, this.title9, 1);
      } else {
         Elem var1 = (Elem)this.bm.cc.getActiveObject();
         String var2 = this.bm.main_document_id;
         if (var2 == null) {
            var2 = "";
         }

         if (var1.getType().equals(var2)) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, this.delstr2, this.title9, 1);
         } else {
            String[] var3 = (String[])((String[])var1.getEtc().get("dbparams"));
            int var5;
            if (var3 != null) {
               IDbHandler var4 = DbFactory.getDbHandler();
               var5 = var4.checkDeletePage(var3);
               if (var5 != 0) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "A résznyomtatvány engedély hiányában nem törölhető!", this.title9, 1);
                  return;
               }
            }

            Object[] var8 = new Object[]{this.optionstr1, this.optionstr2};
            var5 = JOptionPane.showOptionDialog(MainFrame.thisinstance, this.delstr3 + "\n" + var1 + this.delstr4, this.title12, 0, 3, (Icon)null, var8, var8[0]);
            if (var5 == 0) {
               int var6 = this.bm.getIndex(this.bm.get(var1.getType()));
               if (this.bm.created[var6] == 1) {
                  String var7 = AttachementTool.getAtcFilename(this.bm);
                  if (var7 != null) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az albizonylat nem törölhető, mert csatolmányok tartoznak hozzá.\nTörölje az Adatok/Csatolmányok kezelése menüpontban.", "Hibaüzenet", 0);
                     return;
                  }
               }

               this.remove(this.fv);
               this.fv = null;
               this.bm.delForm();
               byte var9 = 0;
               this.datacb.setSelectedIndex(var9);
            }
         }
      }
   }

   public void zoom(int var1) {
      this.percent = var1;
      if (this.fv != null) {
         this.fv.zoom(var1);
      }

   }

   public void showelem(Elem var1) {
      if (this.bm.cc.size() != 1) {
         int var2 = this.bm.cc.getIndex(var1);
         if (var2 != -1) {
            this.datacb.setSelectedIndex(var2);
         }
      }
   }

   public void gotoField(StoreItem var1) {
      if (this.fv != null) {
         this.fv.gotoField(var1);
      }

   }

   public void refreshdatacb() {
      if (this.datacb != null) {
         this.datacb.repaint();
      }

   }

   public void updatedatacb() {
      if (this.datacb != null) {
         this.datacb.updateUI();
      }

   }

   private void findstr(String var1, String var2, String var3, boolean var4) {
      String var5 = (String)JOptionPane.showInputDialog(MainFrame.thisinstance, (var3 == null ? this.findstr10 : this.findstr11) + "\n" + this.findstr12, this.title13, 3, (Icon)null, (Object[])null, this.prevfindstr);
      if (var5 != null && var5.length() != 0) {
         this.prevfindstr = var5;
         if (var4) {
            if (!var5.equals("x") && !var5.equals("X")) {
               var5 = "false";
            } else {
               var5 = "true";
            }
         }

         boolean var6 = false;

         for(int var7 = this.bm.cc.getIndex((Elem)this.bm.cc.getActiveObject()) + 1; var7 < this.bm.cc.size(); ++var7) {
            Elem var8 = (Elem)this.bm.cc.get(var7);
            if (var8.getType().equals(var2)) {
               IDataStore var9 = (IDataStore)var8.getRef();
               if (((GUI_Datastore)var9).containValue(var5, var3)) {
                  var6 = true;
                  this.showelem(var8);
                  break;
               }
            }
         }

         if (!var6) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, this.findstr14 + "\n" + this.findstr15, this.title10, 1);
         }

      }
   }

   public void done_after_readonly() {
      this.fv.done_after_readonly();
   }

   public void addallempty() {
      if (this.bm.size() != 1) {
         for(int var1 = 1; var1 < this.bm.size(); ++var1) {
            FormModel var2 = this.bm.get(var1);
            if (var2 != null) {
               this.bm.addForm(var2);
               this.datacb.setSelectedIndex(this.bm.cc.size() - 1);
            }
         }

         this.datacb.setSelectedIndex(0);
      }
   }

   public void setreadonly(boolean var1) {
      try {
         this.fv.pv.done_after_readonly();
      } catch (Exception var4) {
      }

      if (this.bm.size() != 1) {
         try {
            this.newbtn.setEnabled(!var1);
            this.databtn.setEnabled(true);
            this.delbtn.setEnabled(!var1);
            this.checkallbtn.setEnabled(!var1);
            this.calcallbtn.setEnabled(!var1);
            this.calc_main.setEnabled(!var1);
            this.addbtn.setEnabled(!var1);
            this.savebtn.setEnabled(true);
            this.vcopybtn.setEnabled(!var1);
            this.findbtn.setEnabled(true);
         } catch (Exception var3) {
         }

      }
   }

   public void destroy() {
      if (this.bm != null) {
         this.bm.destroy();
      }

      this.bm = null;
      if (this.fv != null) {
         this.remove(this.fv);
         this.fv.destroy();
      }

      this.fv = null;
   }

   public void gotoField() {
      String var1 = (String)JOptionPane.showInputDialog(MainFrame.thisinstance, "A keresendő sor száma:", "Ugrás", 3, (Icon)null, (Object[])null, "");
      if (var1 != null && var1.length() != 0) {
         MetaStore var2 = MetaInfo.getInstance().getMetaStore(this.fv.fm.id);
         Hashtable var3 = var2.getRowMetas();
         Object var4 = var3.get(var1);
         if (var4 != null) {
            String var5 = null;
            if (var4 instanceof Vector) {
               Vector var6 = (Vector)var4;
               Hashtable var7 = new Hashtable();
               Hashtable var8 = new Hashtable();
               PageModel var9 = null;
               Vector var10 = new Vector();

               int var11;
               Hashtable var12;
               for(var11 = 0; var11 < var6.size(); ++var11) {
                  var12 = (Hashtable)var6.get(var11);
                  DataFieldModel var13 = (DataFieldModel)this.fv.fm.fids.get(var12.get("fid"));
                  PageModel var14 = (PageModel)this.fv.fm.fids_page.get(var12.get("fid"));
                  var8.put(var14, var12.get("fid"));
                  if (!var13.readonly && (var13.role & VisualFieldModel.getmask()) != 0) {
                     var10.add(var12);
                  }
               }

               var6 = var10;

               PageModel var19;
               for(var11 = 0; var11 < var6.size(); ++var11) {
                  var12 = (Hashtable)var6.get(var11);
                  var19 = (PageModel)this.fv.fm.fids_page.get(var12.get("fid"));
                  var7.put(var19, var12.get("fid"));
               }

               int var17;
               if (var7.size() == 1) {
                  var11 = 100000;
                  var17 = 0;

                  for(int var20 = 0; var20 < var6.size(); ++var20) {
                     String var22 = (String)((Hashtable)var6.get(var20)).get("col");
                     int var15 = Integer.parseInt(var22);
                     if (var15 < var11) {
                        var11 = var15;
                        var17 = var20;
                     }
                  }

                  var5 = (String)((Hashtable)var6.get(var17)).get("fid");
               } else {
                  Vector var18 = new Vector();

                  for(var17 = 0; var17 < this.fv.fm.size(); ++var17) {
                     var19 = this.fv.fm.get(var17);
                     if (this.fv.getEnabled(var17)) {
                        if (var7.containsKey(var19)) {
                           var18.add(var19);
                        }

                        if (var8.containsKey(var19)) {
                           var9 = var19;
                        }
                     }
                  }

                  if (var18.size() == 0) {
                     if (var9 == null) {
                        return;
                     }

                     var5 = (String)var8.get(var9);
                  } else {
                     PageModel var21;
                     if (var18.size() == 1) {
                        var21 = (PageModel)var18.get(0);
                        var5 = (String)var7.get(var21);
                     } else {
                        var21 = (PageModel)JOptionPane.showInputDialog(MainFrame.thisinstance, "A megadott sor több lapon is rajta van.\nVálasszon lapot!", "Ugrás", 3, (Icon)null, var18.toArray(), (Object)null);
                        if (var21 == null) {
                           return;
                        }

                        var5 = (String)var7.get(var21);
                     }
                  }
               }
            } else {
               var5 = (String)((Hashtable)var4).get("fid");
            }

            StoreItem var16 = new StoreItem();
            var16.code = var5;
            var16.index = 0;
            this.gotoField(var16);
         }
      }
   }
}
