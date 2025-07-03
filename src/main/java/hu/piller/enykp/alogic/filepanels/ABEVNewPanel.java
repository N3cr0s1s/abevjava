package hu.piller.enykp.alogic.filepanels;

import hu.piller.enykp.alogic.filepanels.browserpanel.BrowserPanel;
import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.primaryaccount.PAInfo;
import hu.piller.enykp.alogic.settingspanel.upgrade.UpgradeFunction;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeManager;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.IFileChooser;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.EventLog;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.eventsupport.DefaultEventSupport;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import hu.piller.enykp.util.icon.ENYKIconSet;
import me.necrocore.abevjava.NecroFile;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class ABEVNewPanel extends JDialog implements IEventSupport, IFileChooser, IEventListener, PropertyChangeListener, AncestorListener {
   public static final String NEW_FILE_PANEL = "Új megnyitása";
   public static final String NEW_DIALOG_TITLE = "Létrehozás";
   protected DefaultEventSupport des = new DefaultEventSupport();
   protected IEventSupport primaryaccount_event_support;
   protected JButton btn_ok;
   protected static final String S_CODE = "ABVNP6541";
   protected static final String S_FILE = "abev_new_panel";
   protected static final String KEY_FILTER_VISIBLE = "filter_visible";
   protected static final String VAL_YES = "igen";
   protected static final String VAL_NO = "nem";
   protected JPanel mainPanel = new JPanel();
   protected BrowserPanel panel;
   protected JComboBox cbo_primary_account;
   protected JComboBox cbo_tax_expert;
   protected JPanel extensionPanel;
   protected Hashtable result;
   protected File path = new NecroFile(this.getProperty("prop.dynamic.templates.absolutepath"));
   protected String[] filters = new String[]{"template_loader_v1"};
   protected String mode = "new";
   protected long lastPArefresh = 0L;
   protected static File lastselectedfile;

   public void addEventListener(IEventListener var1) {
      this.des.addEventListener(var1);
   }

   public void removeEventListener(IEventListener var1) {
      this.des.removeEventListener(var1);
   }

   public Vector fireEvent(Event var1) {
      return this.des.fireEvent(var1);
   }

   public Object eventFired(Event var1) {
      Object var2 = var1.getUserData();
      if (var2 instanceof Hashtable) {
         Hashtable var3 = (Hashtable)var2;
         var2 = var3.get("event");
         if (!var2.equals("before_start") && !var2.equals("primary_account_changed")) {
            if (!var2.equals("after_start") && var2.equals("double_click_on_file")) {
               this.btn_ok.doClick(0);
            }
         } else {
            this.refreshTaxExperts();
            this.refreshPrimaryAccounts();
         }
      } else if (var2 instanceof String) {
         String var4 = (String)var2;
         if (var4.equalsIgnoreCase("beforenew")) {
            this.cbo_primary_account.setSelectedIndex(0);
            this.cbo_tax_expert.setSelectedIndex(0);
         }
      }

      return null;
   }

   public void setSelectedFiles(File[] var1) {
      this.panel.setSelectedFiles(var1);
   }

   public Object[] getSelectedFiles() {
      return this.panel.getSelectedFiles();
   }

   public void setSelectedFilters(String[] var1) {
      this.panel.setSelectedFilters(var1);
   }

   public String[] getSelectedFilters() {
      return this.panel.getSelectedFilters();
   }

   public String[] getAllFilters() {
      return this.panel.getAllFilters();
   }

   public void addFilters(String[] var1, String[] var2) {
      this.panel.addFilters(var1, var2);
   }

   public void removeFilters(String[] var1) {
      this.panel.removeFilters(var1);
   }

   public void rescan() {
      this.panel.rescan();
   }

   public void hideFilters(String[] var1) {
      this.panel.hideFilters(var1);
   }

   public void showFilters(String[] var1) {
      this.panel.showFilters(var1);
   }

   public void setSelectedPath(URI var1) {
      this.panel.getFilePanel().getBusiness().setSelectedPath(var1);
   }

   public void propertyChange(PropertyChangeEvent var1) {
      this.refreshTaxExperts();
      this.refreshPrimaryAccounts();
      String var2;
      if ((var2 = var1.getPropertyName()) != null) {
         String var3 = (String)var2;
         Object var5;
         if (var3.equalsIgnoreCase("new_panel") && (var5 = var1.getNewValue()) != null) {
            String var4 = var5.toString();
            if (var4.equalsIgnoreCase("new")) {
               this.extensionPanel.setVisible(true);
            } else if (var4.equalsIgnoreCase("new_emptyprint")) {
               this.extensionPanel.setVisible(false);
            }

            this.panel.getFilePanel().getBusiness().refreshFileInfos();
         }
      }

   }

   public void ancestorAdded(AncestorEvent var1) {
      this.cbo_primary_account.setEnabled(true);
      this.cbo_tax_expert.setEnabled(true);
      this.btn_ok.setEnabled(true);
   }

   public void ancestorMoved(AncestorEvent var1) {
   }

   public void ancestorRemoved(AncestorEvent var1) {
   }

   public ABEVNewPanel(Hashtable var1) {
      super(MainFrame.thisinstance);
      this.setName("abevnewpanel");
      this.build();
      this.prepare();
      if (var1 != null && var1.containsKey("emptyprint")) {
         this.setName("emptyprint");
         this.extensionPanel.setVisible(false);
      }

   }

   protected void build() {
      this.setSize(new Dimension(GuiUtil.getScreenW() / 2, (int)((double)GuiUtil.getScreenH() * 0.6D)));
      this.setPreferredSize(new Dimension(GuiUtil.getScreenW() / 2, (int)((double)GuiUtil.getScreenH() * 0.6D)));
      this.setLocationRelativeTo(MainFrame.thisinstance);
      this.mainPanel.setLayout(new BoxLayout(this.mainPanel, 1));
      this.extensionPanel = this.getExtensionPanel();
      this.mainPanel.add(this.getBrowserPanel(), (Object)null);
      this.mainPanel.add(this.extensionPanel, (Object)null);
      this.mainPanel.add(this.getButtonsPanel(), (Object)null);
      this.mainPanel.setPreferredSize(new Dimension(GuiUtil.getScreenW() / 2, (int)((double)GuiUtil.getScreenH() * 0.6D)));
      this.mainPanel.setSize(new Dimension(GuiUtil.getScreenW() / 2, (int)((double)GuiUtil.getScreenH() * 0.6D)));
      this.getContentPane().add(this.mainPanel);
      this.setLocationRelativeTo(MainFrame.thisinstance);
      this.setTitle("Létrehozás");
   }

   protected void prepare() {
      this.panel.getFilePanel().getBusiness().setEnykFileFilters(TemplateChecker.getInstance());
      this.panel.getFilePanel().getBusiness().setNeedTemplateFilter(true);
      this.setModal(true);
   }

   protected void initLogic() {
      BrowserPanel var1 = (BrowserPanel)this.getFileChooser();
      var1.setFileSystemBrowserVisible(false);
      var1.is_started = true;
      this.addFilters(this.filters, (String[])null);
      var1.setSelectedPath(this.path.toURI());
   }

   public void setPath(File var1) {
      this.path = var1;
   }

   public void setFilters(String[] var1) {
      this.filters = var1;
   }

   public void setMode(String var1) {
      this.mode = var1;
   }

   protected String getProperty(String var1) {
      return (String)PropertyList.getInstance().get(var1);
   }

   protected JPanel getBrowserPanel() {
      if (this.panel == null) {
         this.panel = new BrowserPanel();
         this.panel.setAlignmentX(0.0F);
         this.panel.getFilePanel().getBusiness().setTask(1);
         this.panel.getFilePanel().getBusiness().addEventListener(this);
      }

      return this.panel;
   }

   protected JPanel getExtensionPanel() {
      JLabel var2 = new JLabel("Törzsadat ");
      var2.setAlignmentX(0.0F);
      int var5 = GuiUtil.getCommonItemHeight() + 2;
      this.cbo_primary_account = new JComboBox(new ABEVNewPanel.SortedDefaultComboBoxModel());
      this.cbo_primary_account.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
      this.cbo_primary_account.setMinimumSize(new Dimension(GuiUtil.getW("WWWWWWWWWWWWWWWWWWWWWWW"), var5));
      this.cbo_primary_account.setAlignmentX(0.0F);
      this.cbo_primary_account.setName("primary_account");
      JLabel var3 = new JLabel("Adótanácsadó ");
      var3.setAlignmentX(0.0F);
      this.cbo_tax_expert = new JComboBox(new ABEVNewPanel.SortedDefaultComboBoxModel());
      this.cbo_tax_expert.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
      this.cbo_tax_expert.setMinimumSize(new Dimension(GuiUtil.getW("WWWWWWWWWWWWWWWWWWWWWWW"), var5));
      this.cbo_tax_expert.setAlignmentX(0.0F);
      this.cbo_tax_expert.setName("tax_expert");
      JButton var4 = new JButton("Frissítés");
      var4.setVisible(false);
      var4.setAlignmentX(0.0F);
      JPanel var1 = new JPanel();
      var1.setLayout(new GridBagLayout());
      var1.setBorder(BorderFactory.createEtchedBorder(0));
      var1.setAlignmentX(0.0F);
      var1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
      GridBagConstraints var6 = new GridBagConstraints();
      var6.anchor = 18;
      var6.gridx = 0;
      var6.gridy = 0;
      var1.add(var2, var6);
      var6 = new GridBagConstraints();
      var6.anchor = 18;
      var6.fill = 2;
      var6.gridx = 1;
      var6.gridy = 0;
      var6.weightx = 1.0D;
      var1.add(this.cbo_primary_account, var6);
      var6 = new GridBagConstraints();
      var6.anchor = 18;
      var6.gridx = 0;
      var6.gridy = 1;
      var1.add(var3, var6);
      var6 = new GridBagConstraints();
      var6.anchor = 18;
      var6.fill = 2;
      var6.gridx = 1;
      var6.gridy = 1;
      var6.weightx = 1.0D;
      var1.add(this.cbo_tax_expert, var6);
      var4.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ABEVNewPanel.this.refreshTaxExperts();
            ABEVNewPanel.this.refreshPrimaryAccounts();
         }
      });
      return var1;
   }

   protected JPanel getButtonsPanel() {
      this.btn_ok = new JButton("Megnyitás");
      JButton var2 = new JButton("Mégsem");
      this.btn_ok.setName("ok");
      var2.setName("cancel");
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout(2, 5, 5));
      var1.setAlignmentX(0.0F);
      var1.add(this.btn_ok);
      var1.add(var2);
      this.btn_ok.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Object[] var2 = ABEVNewPanel.this.getSelectedFiles();
            if (var2 != null && var2.length > 0) {
               SwingWorker var3 = new SwingWorker() {
                  public Object doInBackground() {
                     File var1 = (File)((Object[])((Object[])ABEVNewPanel.this.getSelectedFiles()[0]))[0];
                     String var2 = null;

                     try {
                        PropertyList.getInstance().set("prop.dynamic.ilyenkor", "");
                        String var3 = (String)((Hashtable)((Hashtable)((Object[])((Object[])ABEVNewPanel.this.getSelectedFiles()[0]))[3]).get("docinfo")).get("org");
                        var2 = UpgradeManager.checkUpgrade(var1.getPath(), UpgradeFunction.NEW, var3);
                     } catch (Exception var7) {
                        ErrorList.getInstance().store(ErrorList.LEVEL_SHOW_WARNING, "Nyomtatvány frissítés hiba: " + var7.getMessage() == null ? "" : var7.getMessage(), (Exception)null, (Object)null);
                        var2 = var1.getPath();
                     } finally {
                        PropertyList.getInstance().set("prop.dynamic.ilyenkor", (Object)null);
                     }

                     return var2;
                  }

                  public void done() {
                     String var1 = (String)((Hashtable)((Hashtable)((Object[])((Object[])ABEVNewPanel.this.getSelectedFiles()[0]))[3]).get("docinfo")).get("org");
                     if (!OrgInfo.getInstance().getOrgIdsFromResources().contains(var1)) {
                        GuiUtil.showMessageDialog(ABEVNewPanel.this, "A sablonhoz nincs szervezet paraméter állomány !\n" + var1 + " szervezet.", "Új nyomtatvány létrehozása", 1);
                     } else {
                        ABEVNewPanel.this.setResult(true);
                        File var2 = null;

                        try {
                           var2 = new NecroFile((String)this.get());
                        } catch (InterruptedException var4) {
                           var2 = null;
                        } catch (ExecutionException var5) {
                           Tools.eLog(var5, 0);
                        }

                        ABEVNewPanel.this.result.put("selected_file", new Object[]{var2});
                        ABEVNewPanel.lastselectedfile = var2;
                        ABEVNewPanel.this.cbo_primary_account.setEnabled(false);
                        ABEVNewPanel.this.cbo_tax_expert.setEnabled(false);
                        ABEVNewPanel.this.btn_ok.setEnabled(false);
                        ABEVNewPanel.this.panel.getFilePanel().getBusiness().saveFilterSettings(ABEVNewPanel.this.getName());
                        ABEVNewPanel.this.setDialogVisible(false);
                     }
                  }
               };
               var3.execute();
            } else {
               GuiUtil.showMessageDialog(ABEVNewPanel.this, "Nem választott ki nyomtatvány sablont !", "Új nyomtatvány létrehozása", 1);
            }
         }
      });
      var2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ABEVNewPanel.this.setResult(false);
            ABEVNewPanel.this.panel.getFilePanel().getBusiness().saveFilterSettings(ABEVNewPanel.this.getName());
            ABEVNewPanel.this.setDialogVisible(false);
         }
      });
      this.setDefaultCloseOperation(2);
      this.installKeyBindingForButton(var2, 27);
      ENYKIconSet var3 = ENYKIconSet.getInstance();
      this.setButtonIcon(this.btn_ok, "anyk_k_nyomtatvany_letrehozasa", var3);
      this.setButtonIcon(var2, "anyk_megse", var3);
      return var1;
   }

   protected void setButtonIcon(JButton var1, String var2, ENYKIconSet var3) {
      var1.setIcon(var3.get(var2));
   }

   protected void installKeyBindingForButton(final JButton var1, int var2) {
      String var3 = KeyStroke.getKeyStroke(var2, 0).toString() + "Pressed";
      JPanel var4 = this.mainPanel;
      var4.getInputMap(2).put(KeyStroke.getKeyStroke(var2, 0), var3);
      var4.getActionMap().put(var3, new AbstractAction() {
         public void actionPerformed(ActionEvent var1x) {
            if (var1.isVisible() && var1.isEnabled()) {
               var1.doClick();
            }

         }
      });
   }

   public IFileChooser getFileChooser() {
      return this.panel;
   }

   public Object getSelectedPrimaryAccount() {
      return this.cbo_primary_account.getSelectedItem();
   }

   public Object getSelectedTaxExpert() {
      return ((ABEVNewPanel.ComboItem)this.cbo_tax_expert.getSelectedItem()).getItem();
   }

   public void refreshPrimaryAccounts() {
      DefaultComboBoxModel var2 = (DefaultComboBoxModel)this.cbo_primary_account.getModel();
      var2.removeAllElements();
      var2.addElement(new ABEVNewPanel.ComboItem("(Nincs kiválasztva)"));
      PAInfo var3 = PAInfo.getInstance();

      Vector var1;
      int var4;
      int var5;
      try {
         var1 = (Vector)var3.get_primary_account_list("company");
         if (var1 != null) {
            var4 = 0;

            for(var5 = var1.size(); var4 < var5; ++var4) {
               var2.addElement(new ABEVNewPanel.ComboItem(var1.get(var4)));
            }
         } else {
            writeLog(" A törzs cég adatai nem érhetők el!");
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      try {
         var1 = (Vector)var3.get_primary_account_list("smallbusiness");
         if (var1 != null) {
            var4 = 0;

            for(var5 = var1.size(); var4 < var5; ++var4) {
               var2.addElement(new ABEVNewPanel.ComboItem(var1.get(var4)));
            }
         } else {
            writeLog("A törzs egyéni vállalkozó adatai nem érhetők el!");
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      try {
         var1 = (Vector)var3.get_primary_account_list("people");
         if (var1 != null) {
            var4 = 0;

            for(var5 = var1.size(); var4 < var5; ++var4) {
               var2.addElement(new ABEVNewPanel.ComboItem(var1.get(var4)));
            }
         } else {
            writeLog("A törzs magán személyek adatai nem érhetők el!");
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public void refreshTaxExperts() {
      DefaultComboBoxModel var2 = (DefaultComboBoxModel)this.cbo_tax_expert.getModel();
      var2.removeAllElements();
      var2.addElement(new ABEVNewPanel.ComboItem("(Nincs kiválasztva)"));
      PAInfo var3 = PAInfo.getInstance();

      try {
         Vector var1 = (Vector)var3.get_primary_account_list("taxexpert");
         if (var1 != null) {
            int var4 = 0;

            for(int var5 = var1.size(); var4 < var5; ++var4) {
               var2.addElement(new ABEVNewPanel.ComboItem(var1.get(var4)));
            }
         } else {
            writeLog("A törzs adó tanácsadók adatai nem érhetők el!");
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   protected IPropertyList getABEVLogicInfo() {
      IPropertyList var1 = PropertyList.getInstance();
      if (var1 != null) {
         Object var2 = var1.get("abev_logic_info");
         if (var2 instanceof IPropertyList) {
            return (IPropertyList)var2;
         }
      }

      return null;
   }

   public static void writeLog(Object var0) {
      try {
         EventLog.getInstance().writeLog("Új megnyitása: " + (var0 == null ? "" : var0));
      } catch (IOException var2) {
         Tools.eLog(var2, 0);
      }

   }

   protected void setDialogVisible(boolean var1) {
      this.setVisible(var1);
   }

   public Hashtable showDialog() {
      try {
         this.initLogic();
         PAInfo var1 = PAInfo.getInstance();
         long var2 = var1.getLastModTime();
         if (this.lastPArefresh <= var2) {
            this.refreshTaxExperts();
            this.refreshPrimaryAccounts();
            this.lastPArefresh = var2;
         }

         this.panel.getFilePanel().getBusiness().loadFilterSettings(this.getName());
         if (lastselectedfile != null) {
            File[] var4 = new File[]{lastselectedfile};
            this.panel.setSelectedFiles(var4);
            JTable var5 = this.panel.getFilePanel().getTbl_file_list();
            var5.scrollRectToVisible(var5.getCellRect(var5.getSelectedRow(), 0, true));
            lastselectedfile = null;
         }

         this.pack();
         this.setVisible(true);
         this.mainPanel.remove(this);
         this.dispose();
         return this.result;
      } catch (Exception var6) {
         var6.printStackTrace();
         return null;
      }
   }

   protected void setResult(boolean var1) {
      this.result = new Hashtable();
      if (var1) {
         Object[] var2 = (Object[])((Object[])this.getSelectedFiles()[0]);
         this.result.put("primary_account", ((ABEVNewPanel.ComboItem)this.cbo_primary_account.getSelectedItem()).getItem());
         this.result.put("tax_expert", ((ABEVNewPanel.ComboItem)this.cbo_tax_expert.getSelectedItem()).getItem());
         this.result.put("file_status", "Módosítható");
         this.result.put("selected_file", var2[0]);
         this.result.put("selected_template_docinfo", var2[3]);

         try {
            lastselectedfile = (File)var2[0];
         } catch (Exception var4) {
         }
      }

   }

   protected class ComboItem {
      protected Object item;

      ComboItem(Object var2) {
         this.setItem(var2);
      }

      public void setItem(Object var1) {
         this.item = var1;
      }

      public Object getItem() {
         return this.item;
      }

      public String toString() {
         if (this.item == null) {
            return "???";
         } else {
            if (!(this.item instanceof String)) {
               PAInfo var1 = PAInfo.getInstance();
               Object var2 = var1.get_pa_record_item_description("abev_new_panel_description", this.item);
               if (var2 instanceof String) {
                  return (String)var2;
               }
            }

            return this.item.toString();
         }
      }
   }

   protected static class SortedDefaultComboBoxModel extends DefaultComboBoxModel {
      protected final Comparator comparator = new ABEVNewPanel.SortedDefaultComboBoxModel.DefaultComparator();

      public SortedDefaultComboBoxModel() {
      }

      public SortedDefaultComboBoxModel(Object[] var1) {
         super(var1);
      }

      public SortedDefaultComboBoxModel(Vector var1) {
         super(var1);
      }

      public void addElement(Object var1) {
         int var2 = 0;
         int var3 = this.getSize() - 1;
         int var4 = -1;
         Comparator var7 = this.comparator;

         while(var2 <= var3) {
            int var5 = var2 + var3 >> 1;
            Object var6 = this.getElementAt(var5);
            int var8 = var7.compare(var1, var6);
            if (var8 < 0) {
               var3 = var5 - 1;
            } else {
               if (var8 <= 0) {
                  var4 = var5;
                  break;
               }

               var2 = var5 + 1;
            }
         }

         if (var4 == -1) {
            var4 = var2;
         }

         if (var4 >= this.getSize()) {
            super.addElement(var1);
         } else {
            this.insertElementAt(var1, var4);
         }

      }

      protected class DefaultComparator implements Comparator {
         public int compare(Object var1, Object var2) {
            if (var1 == var2) {
               return 0;
            } else if (var1 == null) {
               return -1;
            } else {
               return var2 == null ? 1 : var1.toString().compareTo(var2.toString());
            }
         }
      }
   }
}
