package hu.piller.enykp.gui.framework;

import hu.piller.enykp.alogic.fileloader.xml.XMLPost;
import hu.piller.enykp.alogic.helppanel.Help;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.viewer.DefaultMultiFormViewer;
import hu.piller.enykp.interfaces.ICommandObject;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.icon.ENYKIconSet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.File;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainPanel extends JPanel {
   CenterPane centerpanel;
   JComponent toolbarpanel;
   JComponent statuspanel;
   JComponent flowcontrolpanel;
   JComponent leftcomp;
   int tbplace;
   Help sugo;
   Help help;
   String sugoroot;
   String prevhelp;
   public boolean readonlymode = false;
   public boolean forceDisabledPageShowing = false;
   public boolean funcreadonly = false;
   public Integer state;
   public static final Integer EMPTY = new Integer(0);
   public static final Integer NORMAL = new Integer(1);
   public static final Integer READONLY = new Integer(3);
   private String flowstr1 = "Kitöltési útmutató";
   private String flow2 = "Súgó";
   private JButton frissitesButton;
   private JLabel frissitesMessage;
   private Object lock = new Object();

   public MainPanel() {
      super(new BorderLayout());
      this.setCenterpanel(new CenterPane());
      this.setStatuspanel(new StatusPane());
      this.sugo = Help.getInstance("sugo");
      this.help = Help.getInstance("kiut");
      ToolbarPane var1 = new ToolbarPane(this.tbplace);
      this.setToolbarpanel(var1, (Object)null);
      if (MainFrame.opmode.equals("0")) {
         this.addToolbarItems();
      } else {
         this.addToolbarItems_inside();
      }

      this.setFlowcontrolpanel(new FlowcontrolPane(this.centerpanel));
      this.addFlowWindow(this.flowstr1 + " (F1)", (Icon)null, this.help.getPanel());
      this.addFlowWindow(this.flow2 + " (F2)", (Icon)null, this.sugo.getPanel());
      this.sugoroot = (String)PropertyList.getInstance().get("prop.sys.helps");
      String var2 = null;

      try {
         var2 = (String)((Vector)PropertyList.getInstance().get("prop.const.root")).get(0);
      } catch (Exception var4) {
      }

      if (var2 != null) {
         this.sugo.setForcePath((String)null);
         this.sugo.update_url(this.sugoroot + File.separator + var2);
      }

      this.prevhelp = "";
      this.setstate(new Integer(0));
   }

   public void set_kiut_url(FormModel var1) {
      try {
         if (this.prevhelp.equals(var1.help)) {
            return;
         }

         this.prevhelp = var1.help;
         this.help.update_url(this.sugoroot, var1.help, var1.bm.docinfo.get("org").toString(), var1.bm.splitesaver.equalsIgnoreCase("true") ? var1.bm.get_formid() : var1.bm.id);
      } catch (Exception var3) {
      }

   }

   public void set_kiut_url(String var1) {
      if (var1 == null) {
         try {
            this.help.setForcePath((String)null);
            this.help.update_url("");
            this.prevhelp = "";
            return;
         } catch (Exception var4) {
            Tools.eLog(var4, 0);
         }
      }

      try {
         if (this.prevhelp.equals(var1)) {
            return;
         }

         this.prevhelp = var1;
         this.help.update_url(this.sugoroot, var1, this.getDMFV().bm.docinfo.get("org").toString(), this.getDMFV().bm.splitesaver.equalsIgnoreCase("true") ? this.getDMFV().fv.fm.bm.get_formid() : this.getDMFV().fv.fm.bm.id);
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
      }

   }

   public void setStatuspanel(JComponent var1) {
      if (this.statuspanel != null) {
         this.remove(this.statuspanel);
      }

      this.statuspanel = var1;
      this.add(this.statuspanel, "South");
   }

   public void setFlowcontrolpanel(JComponent var1) {
      if (this.flowcontrolpanel != null) {
         this.remove(this.flowcontrolpanel);
      }

      this.flowcontrolpanel = var1;
      this.add(this.flowcontrolpanel, "East");
   }

   public void setCenterpanel(CenterPane var1) {
      if (this.centerpanel != null) {
         this.remove(this.centerpanel);
      }

      this.centerpanel = var1;
      this.add(this.centerpanel, "Center");
   }

   public void setToolbarpanel(JComponent var1, Object var2) {
      if (this.toolbarpanel != null) {
         this.remove(this.toolbarpanel);
      }

      this.toolbarpanel = var1;
      if (var2 == null) {
         var2 = this.tbplace == 1 ? "West" : "North";
      }

      this.add(this.toolbarpanel, var2);
   }

   public void addToolbarItem(JComponent var1, ICommandObject var2) {
      if (this.toolbarpanel instanceof ToolbarPane) {
         ((ToolbarPane)this.toolbarpanel).addTBItem(var1, var2);
      }

   }

   public void removeToolbarItem(JComponent var1) {
      if (this.toolbarpanel instanceof ToolbarPane) {
         ((ToolbarPane)this.toolbarpanel).removeTBItem(var1);
      }

   }

   public void addFrissitesButton() {
      synchronized(this.lock) {
         try {
            if (this.frissitesButton != null) {
               return;
            }

            ENYKIconSet var2 = ENYKIconSet.getInstance();
            this.frissitesButton = new JButton("Frissítések elérhetők!", var2.get("anyk_frissites"));
            this.frissitesButton.setName("FrissitesButton");
            this.frissitesButton.setToolTipText("Frissítések elérhetők");
            this.frissitesButton.setBorderPainted(true);
            this.frissitesButton.setFocusPainted(true);
            this.frissitesButton.setFocusable(false);
            this.addToolbarItem(this.frissitesButton, Menubar.thisinstance.cmdFrissitesek);
         } finally {
            this.lock.notifyAll();
         }

      }
   }

   public void removeFrissitesButton() {
      synchronized(this.lock) {
         try {
            if (this.frissitesButton != null) {
               this.removeToolbarItem(this.frissitesButton);
               this.frissitesButton = null;
            }
         } finally {
            this.lock.notifyAll();
         }

      }
   }

   public void addFrissitesMessage() {
      synchronized(this.lock) {
         try {
            if (this.isUpgradeMsgEnabled() && this.frissitesMessage == null) {
               this.frissitesMessage = new JLabel("  Frissítések keresése...");
               this.frissitesMessage.setName("FrissitesMessage");
               this.frissitesMessage.setFocusable(false);
               this.frissitesMessage.setToolTipText("Telepített összetevők újabb verzióinak keresése");
               this.addToolbarItem(this.frissitesMessage, (ICommandObject)null);
               return;
            }
         } finally {
            this.lock.notifyAll();
         }

      }
   }

   public void removeFrissitesMessage() {
      synchronized(this.lock) {
         try {
            if (this.frissitesMessage != null) {
               this.removeToolbarItem(this.frissitesMessage);
               this.frissitesMessage = null;
            }
         } finally {
            this.lock.notifyAll();
         }

      }
   }

   private boolean isUpgradeMsgEnabled() {
      boolean var1 = false;
      if (SettingsStore.getInstance().get("upgrade") == null) {
         var1 = true;
      } else {
         String var2 = SettingsStore.getInstance().get("upgrade", "lookupmsg");
         if (var2 == null) {
            var1 = true;
         } else if (Boolean.parseBoolean(var2)) {
            var1 = true;
         }
      }

      return var1;
   }

   private void addToolbarItems_inside() {
      Menubar var1 = Menubar.thisinstance;
      ENYKIconSet var2 = ENYKIconSet.getInstance();
      JButton var3 = new JButton();
      var3.setToolTipText(var1.openbarcodeaction.getValue("Name").toString());
      var3.setIcon(var2.get("anyk_megnyitas"));
      var3.setBorderPainted(false);
      var3.setFocusPainted(false);
      var3.setMargin(new Insets(0, 0, 0, 0));
      var3.setFocusable(false);
      var3 = new JButton();
      var3.setToolTipText(var1.savebarcodeaction.getValue("Name").toString());
      var3.setIcon(var2.get("anyk_mentes"));
      var3.setBorderPainted(false);
      var3.setFocusPainted(false);
      var3.setMargin(new Insets(0, 0, 0, 0));
      var3.setFocusable(false);
      var3 = new JButton();
      var3.setToolTipText(var1.checkaction.getValue("Name").toString());
      var3.setIcon(var2.get("anyk_ellenorzes"));
      var3.setBorderPainted(false);
      var3.setFocusPainted(false);
      var3.setMargin(new Insets(0, 0, 0, 0));
      var3.setFocusable(false);
      var3.setName("tb_check");
      if (!MainFrame.rogzitomode) {
         this.addToolbarItem(var3, var1.checkcmd);
      }

      var3 = new JButton();
      var3.setToolTipText(var1.setcalcaction.getValue("Name").toString());
      var3.setIcon(var2.get("anyk_m_nyomtatvany_torlese"));
      var3.setBorderPainted(false);
      var3.setFocusPainted(false);
      var3.setMargin(new Insets(0, 0, 0, 0));
      var3.setFocusable(false);
      if (!MainFrame.ellvitamode && !MainFrame.rogzitomode) {
         this.addToolbarItem(var3, var1.setcalccmd);
      }

      var3 = new JButton();
      var3.setToolTipText(var1.exitaction.getValue("Name").toString());
      var3.setIcon(var2.get("anyk_kilepes"));
      var3.setBorderPainted(false);
      var3.setFocusPainted(false);
      var3.setMargin(new Insets(0, 0, 0, 0));
      var3.setFocusable(false);
      this.addToolbarItem(var3, var1.exitcmd);
   }

   private void addToolbarItems() {
      Menubar var1 = Menubar.thisinstance;
      new ServiceMenuItem(var1);
      ServiceMenuItem var2 = ServiceMenuItem.thisinstance;
      ENYKIconSet var3 = ENYKIconSet.getInstance();
      JButton var4 = new JButton();
      var4.setToolTipText(var1.newaction.getValue("Name").toString());
      var4.setIcon(var3.get("anyk_k_nyomtatvany_letrehozasa"));
      var4.setBorderPainted(false);
      var4.setFocusPainted(false);
      var4.setMargin(new Insets(0, 0, 0, 0));
      var4.setFocusable(false);
      this.addToolbarItem(var4, var1.newcmd);
      var4 = new JButton();
      var4.setToolTipText(var1.openaction.getValue("Name").toString());
      var4.setIcon(var3.get("anyk_megnyitas"));
      var4.setBorderPainted(false);
      var4.setFocusPainted(false);
      var4.setMargin(new Insets(0, 0, 0, 0));
      var4.setFocusable(false);
      this.addToolbarItem(var4, var1.opencmd);
      var4 = new JButton();
      var4.setToolTipText(var1.saveaction.getValue("Name").toString());
      var4.setIcon(var3.get("anyk_mentes"));
      var4.setBorderPainted(false);
      var4.setFocusPainted(false);
      var4.setMargin(new Insets(0, 0, 0, 0));
      var4.setFocusable(false);
      this.addToolbarItem(var4, var1.savecmd);
      var4 = new JButton();
      var4.setToolTipText(var1.saveasaction.getValue("Name").toString());
      var4.setIcon(var3.get("anyk_mentes_maskent"));
      var4.setBorderPainted(false);
      var4.setFocusPainted(false);
      var4.setMargin(new Insets(0, 0, 0, 0));
      var4.setFocusable(false);
      this.addToolbarItem(var4, var1.saveascmd);
      var4 = new JButton();
      var4.setToolTipText(var1.noteaction.getValue("Name").toString());
      var4.setIcon(var3.get("anyk_megjegyzes"));
      var4.setBorderPainted(false);
      var4.setFocusPainted(false);
      var4.setMargin(new Insets(0, 0, 0, 0));
      var4.setFocusable(false);
      this.addToolbarItem(var4, var1.notecmd);
      var4 = new JButton();
      var4.setToolTipText(var1.gateaction.getValue("Name").toString());
      var4.setIcon(var3.get("anyk_belepes"));
      var4.setBorderPainted(false);
      var4.setFocusPainted(false);
      var4.setMargin(new Insets(0, 0, 0, 0));
      var4.setFocusable(false);
      this.addToolbarItem(var4, var1.gatecmd);
      var4 = new JButton();
      var4.setToolTipText(var1.printaction.getValue("Name").toString());
      var4.setIcon(var3.get("anyk_nyomtatas"));
      var4.setBorderPainted(false);
      var4.setFocusPainted(false);
      var4.setMargin(new Insets(0, 0, 0, 0));
      var4.setFocusable(false);
      this.addToolbarItem(var4, var1.print2cmd);
      var4 = new JButton();
      var4.setToolTipText(var1.checkaction.getValue("Name").toString());
      var4.setIcon(var3.get("anyk_ellenorzes"));
      var4.setBorderPainted(false);
      var4.setFocusPainted(false);
      var4.setMargin(new Insets(0, 0, 0, 0));
      var4.setFocusable(false);
      var4.setName("tb_check");
      this.addToolbarItem(var4, var1.checkcmd);
      var4 = new JButton();
      var4.setToolTipText(var1.eraseaction.getValue("Name").toString());
      var4.setIcon(var3.get("anyk_torles"));
      var4.setBorderPainted(false);
      var4.setFocusPainted(false);
      var4.setMargin(new Insets(0, 0, 0, 0));
      var4.setFocusable(false);
      this.addToolbarItem(var4, var1.erasecmd);
      var4 = new JButton();
      var4.setToolTipText(var2.actTorzsadatokMentese.getValue("Name").toString());
      var4.setIcon(var3.get("anyk_torzsadatok_mentese"));
      var4.setRolloverEnabled(false);
      var4.setBorderPainted(false);
      var4.setFocusPainted(false);
      var4.setMargin(new Insets(0, 0, 0, 0));
      var4.setFocusable(false);
      this.addToolbarItem(var4, var2.cmdTorzsadatokMentese);
      var4 = new JButton();
      var4.setToolTipText(var1.detailsaction.getValue("Name").toString() + " (F6)");
      var4.setIcon(var3.get("anyk_tenyleges_adatrogzites"));
      var4.setBorderPainted(false);
      var4.setFocusPainted(false);
      var4.setMargin(new Insets(0, 0, 0, 0));
      var4.setFocusable(false);
      this.addToolbarItem(var4, var1.detailscmd);
      var4 = new JButton();
      var4.setToolTipText(var1.calculatoraction.getValue("Name").toString() + " (F7)");
      var4.setIcon(var3.get("anyk_szamologep"));
      var4.setBorderPainted(false);
      var4.setFocusPainted(false);
      var4.setMargin(new Insets(0, 0, 0, 0));
      var4.setFocusable(false);
      this.addToolbarItem(var4, var1.calculatorcmd);
      var4 = new JButton();
      var4.setToolTipText(var2.actBeallitasok.getValue("Name").toString());
      var4.setIcon(var3.get("anyk_beallitasok"));
      var4.setBorderPainted(false);
      var4.setFocusPainted(false);
      var4.setMargin(new Insets(0, 0, 0, 0));
      var4.setFocusable(false);
      this.addToolbarItem(var4, var2.cmdBeallitasok);
      var4 = new JButton();
      var4.setToolTipText(var2.actNevjegy.getValue("Name").toString());
      var4.setIcon(var3.get("anyk_sugo"));
      var4.setBorderPainted(false);
      var4.setFocusPainted(false);
      var4.setMargin(new Insets(0, 0, 0, 0));
      var4.setFocusable(false);
      this.addToolbarItem(var4, var2.cmdNevjegy);
      var4 = new JButton();
      var4.setToolTipText(var1.exitaction.getValue("Name").toString());
      var4.setIcon(var3.get("anyk_kilepes"));
      var4.setBorderPainted(false);
      var4.setFocusPainted(false);
      var4.setMargin(new Insets(0, 0, 0, 0));
      var4.setFocusable(false);
      this.addToolbarItem(var4, var1.exitcmd);
   }

   public void addFlowWindow(String var1, Icon var2, JPanel var3) {
      if (this.flowcontrolpanel instanceof FlowcontrolPane) {
         ((FlowcontrolPane)this.flowcontrolpanel).addFlowItem(var1, var2, var3);
      }

   }

   public void intoleftside(JComponent var1) {
      CenterPane var2 = this.centerpanel;
      int var3 = var2.getDividerLocation();
      if (var3 < 10 && var1 instanceof DefaultMultiFormViewer) {
         var3 = (int)((double)this.getWidth() * 0.6D);
      }

      var1.setSize(var3, var2.getHeight());
      var1.setPreferredSize(new Dimension(var3, var2.getHeight()));
      if (this.leftcomp != null) {
         var2.remove(this.leftcomp);
      }

      var2.setLeftComponent(var1);
      var2.setDividerLocation(var3);
      this.leftcomp = var1;
   }

   public void makeempty() {
      DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
      if (var1 != null) {
         var1.bm.destroy();
         MainFrame.thisinstance.mp.intoleftside(new JPanel());
         XMLPost.xmleditnemjo = false;
         MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
         MainFrame.thisinstance.mp.readonlymode = false;
         MainFrame.thisinstance.mp.forceDisabledPageShowing = false;
         MainFrame.thisinstance.mp.funcreadonly = false;
         this.setstate(new Integer(0));
         var1.bm = null;
      }

   }

   public void showpanel(JPanel var1) {
      ((FlowcontrolPane)this.flowcontrolpanel).showpanel(var1);
   }

   public void showpanel(String var1) {
      ((FlowcontrolPane)this.flowcontrolpanel).showpanel(var1);
   }

   public StatusPane getStatuspanel() {
      return (StatusPane)this.statuspanel;
   }

   public DefaultMultiFormViewer getDMFV() {
      return this.leftcomp instanceof DefaultMultiFormViewer ? (DefaultMultiFormViewer)this.leftcomp : null;
   }

   public void setstate(Integer var1) {
      this.state = var1;
      this.readonlymode = this.state == 3;
      Menubar.thisinstance.setState(this.state);
      ToolbarPane.thisinstance.setState(this.state);
      DefaultMultiFormViewer var2 = this.getDMFV();
      if (var2 != null) {
         var2.setreadonly(this.readonlymode);
      }

      try {
         if (this.state == 0) {
            GuiUtil.setTitle((String)null);
         } else {
            GuiUtil.setTitle(var2.bm.cc.getLoadedfile().getCanonicalFile().getName());
         }
      } catch (Exception var4) {
      }

   }

   public void setReadonly(boolean var1) {
      if (var1) {
         this.setstate(READONLY);
      } else {
         this.setstate(NORMAL);
      }

      this.getDMFV().done_after_readonly();
   }

   public void hideToolbar() {
      this.toolbarpanel.setVisible(false);
   }

   public void showToolbar() {
      this.toolbarpanel.setVisible(true);
   }

   public boolean isReadonlyMode() {
      return this.readonlymode;
   }

   public boolean isFuncReadonlyMode() {
      return this.funcreadonly;
   }

   public boolean isReadonlyState() {
      try {
         return this.state.equals(READONLY);
      } catch (Exception var2) {
         return false;
      }
   }

   public boolean onDisabledPageReadOnly() {
      return this.forceDisabledPageShowing ? false : this.readonlymode;
   }
}
