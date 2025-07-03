package hu.piller.enykp.alogic.settingspanel.proxy;

import hu.piller.enykp.alogic.settingspanel.SettingsDialog;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ProxyPanel extends JPanel implements ActionListener {
   public static final Dimension PANEL_SIZE = new Dimension(300, 250);
   public static final Dimension SUB_PANEL_SIZE = new Dimension(300, 30);
   public static final Dimension LABEL_SIZE = new Dimension(100, 20);
   public static final Dimension FIELD_SIZE = new Dimension(100, 20);
   public static final String PANEL_TITLE = "Proxy paraméterek beállítása";
   public static final String BTN_CANCEL_TXT = "Módosítások törlése";
   public static final String BTN_OK_TXT = "Beállítások mentése";
   public static final String CHK_PROXY = "Kapcsolódás az internethez proxy használatával";
   public static final String CHK_PROXY_AUTHENTIC = "A proxy azonosítást igényel";
   public static final String INPUT_HOST_TXT = "Hoszt:";
   public static final String INPUT_PORT_TXT = "Port:";
   public static final String INPUT_USER_TXT = "Felhasználó:";
   public static final String INPUT_PWD_TXT = "Jelszó:";
   public static final String BORDER_AU = "Beállítások";
   Container pane;
   ProxyBusiness proxyData;
   JPanel inputPanel;
   JCheckBox chkProxy;
   JCheckBox chkAuthentic;
   JPanel pHost;
   JLabel lblHost;
   JTextField inputHost;
   JPanel pPort;
   JLabel lblPort;
   JTextField inputPort;
   JPanel pUser;
   JLabel lblUser;
   JTextField inputUser;
   JPanel pPwd;
   JLabel lblpwd;
   JPasswordField inputpwd;
   JPanel pProxyPanel;
   JPanel panel1;
   JPanel panel2;
   JPanel panel3;
   JPanel panel4;
   JPanel panel5;
   JPanel panel6;
   String oldinputUser;
   boolean result;
   Component parent;
   boolean inset;

   public ProxyPanel() {
      this.proxyData = null;
      this.inputPanel = new JPanel();
      this.chkProxy = GuiUtil.getANYKCheckBox("Kapcsolódás az internethez proxy használatával");
      this.chkAuthentic = GuiUtil.getANYKCheckBox("A proxy azonosítást igényel");
      this.pHost = new JPanel();
      this.lblHost = new JLabel("Hoszt:");
      this.inputHost = new JTextField();
      this.pPort = new JPanel();
      this.lblPort = new JLabel("Port:");
      this.inputPort = new JTextField();
      this.pUser = new JPanel();
      this.lblUser = new JLabel("Felhasználó:");
      this.inputUser = new JTextField();
      this.pPwd = new JPanel();
      this.lblpwd = new JLabel("Jelszó:");
      this.inputpwd = new JPasswordField();
      this.oldinputUser = "";
      this.result = false;
      this.parent = null;
      this.pane = this;
      this.proxyData = new ProxyBusiness();
      this.createElements();
      Hashtable var1 = this.proxyData.getDataprovider();
      this.loadInitValues(var1);
   }

   public ProxyPanel(Component var1) {
      this();
      this.parent = var1;
   }

   private void createElements() {
      this.pane.setSize(new Dimension(300, 250));
      this.pane.setLayout(new BoxLayout(this.pane, 1));
      this.inputPanel.setLayout(new BoxLayout(this.inputPanel, 1));
      this.pane.add(Box.createVerticalStrut(10));
      this.pane.add(this.inputPanel);
      this.pane.add(Box.createVerticalStrut(20));
      this.chkProxy.setActionCommand("chkProxy");
      this.chkProxy.addActionListener(this);
      this.chkAuthentic.setActionCommand("chkAuthentic");
      this.chkAuthentic.addActionListener(this);
      this.inputPort.setName("inputPort");
      this.pProxyPanel = this.createProxyPanel();
      this.inputPanel.add(this.pProxyPanel);
   }

   private JPanel createProxyPanel() {
      if (this.pProxyPanel == null) {
         JLabel var1 = new JLabel();
         int var2 = Math.max(GuiUtil.getW(var1, "Kapcsolódás az internethez proxy használatával") + 60, 2 * (GuiUtil.getW(var1, "Felhasználó:WWWWW") + 150));
         this.pProxyPanel = new JPanel();
         this.pProxyPanel.setBorder(BorderFactory.createTitledBorder("Beállítások"));
         this.pProxyPanel.setLayout(new BoxLayout(this.pProxyPanel, 1));
         this.pProxyPanel.setMinimumSize(new Dimension(var2, 12 * (GuiUtil.getCommonItemHeight() + 2)));
         this.pProxyPanel.setMaximumSize(this.pProxyPanel.getMinimumSize());
         this.pProxyPanel.setPreferredSize(this.pProxyPanel.getMinimumSize());
         this.panel1 = this.createPanel();
         this.panel1.add(this.chkProxy);
         this.pProxyPanel.add(this.panel1);
         this.panel2 = this.createPanel();
         this.panel2.add(Box.createHorizontalStrut(30));
         this.panel2.add(this.lblHost);
         this.panel2.add(this.inputHost);
         this.inputHost.setMinimumSize(new Dimension(GuiUtil.getW(var1, "WWWWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 2));
         this.inputHost.setMaximumSize(this.inputHost.getMinimumSize());
         this.inputHost.setPreferredSize(this.inputHost.getMinimumSize());
         this.panel2.add(Box.createHorizontalStrut(7));
         this.panel2.add(this.lblPort);
         this.panel2.add(this.inputPort);
         this.inputPort.setMinimumSize(new Dimension(GuiUtil.getW(var1, "WWWWW"), GuiUtil.getCommonItemHeight() + 2));
         this.inputPort.setMaximumSize(this.inputPort.getMinimumSize());
         this.inputPort.setPreferredSize(this.inputPort.getMinimumSize());
         this.pProxyPanel.add(this.panel2);
         this.pProxyPanel.add(Box.createVerticalStrut(30));
         this.panel3 = this.createPanel();
         this.panel3.add(this.chkAuthentic);
         this.pProxyPanel.add(this.panel3);
         this.panel4 = this.createPanel();
         this.panel4.add(this.lblUser);
         this.panel4.add(this.inputUser);
         this.inputUser.setMinimumSize(new Dimension(GuiUtil.getW(var1, "WWWWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 2));
         this.inputUser.setMaximumSize(this.inputUser.getMinimumSize());
         this.inputUser.setPreferredSize(this.inputUser.getMinimumSize());
         this.pProxyPanel.add(this.panel4);
         this.panel5 = this.createPanel();
         this.panel5.add(this.lblpwd);
         this.panel5.add(this.inputpwd);
         this.inputpwd.setMinimumSize(new Dimension(GuiUtil.getW(var1, "WWWWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 2));
         this.inputpwd.setMaximumSize(this.inputpwd.getMinimumSize());
         this.inputpwd.setPreferredSize(this.inputpwd.getMinimumSize());
         this.pProxyPanel.add(this.panel5);
      }

      return this.pProxyPanel;
   }

   private JPanel createPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout(0));
      return var1;
   }

   public void refreshData() {
      this.loadInitValues(this.proxyData.getDataprovider());
   }

   private void loadInitValues(Hashtable var1) {
      this.inputHost.setText((String)this.getTable(var1, "proxy.host"));
      this.inputPort.setText((String)this.getTable(var1, "proxy.port"));
      this.inputUser.setText((String)this.getTable(var1, "proxy.user"));
      this.inputpwd.setText((String)this.getTable(var1, "proxy.pwd"));
      this.oldinputUser = this.inputUser.getText();
      this.chkProxy.setEnabled(true);
      this.chkProxy.setSelected(this.getboolean(var1, "proxy.enabled"));
      this.enableProxy(this.getboolean(var1, "proxy.enabled"));
      this.chkAuthentic.setSelected(this.getboolean(var1, "proxy.authentication"));
      this.enableAuthentic(this.getboolean(var1, "proxy.authentication"));
   }

   private void enableProxy(boolean var1) {
      this.inputHost.setEnabled(var1);
      this.inputPort.setEnabled(var1);
      this.chkAuthentic.setEnabled(var1);
      if (!var1) {
         this.chkAuthentic.setSelected(var1);
         this.enableAuthentic(var1);
      }

   }

   private void enableAuthentic(boolean var1) {
      this.inputUser.setEnabled(var1);
      this.inputpwd.setEnabled(var1);
   }

   private Object getTable(Hashtable var1, Object var2) {
      if (var1 == null) {
         return null;
      } else {
         return !var1.containsKey(var2) ? null : var1.get(var2);
      }
   }

   private boolean getboolean(Hashtable var1, Object var2) {
      if (var1 == null) {
         return false;
      } else if (!var1.containsKey(var2)) {
         return false;
      } else {
         return ((String)var1.get(var2)).compareTo("true") == 0;
      }
   }

   public void cancel() {
      this.result = false;
      this.loadInitValues(this.proxyData.getDataprovider());
   }

   public boolean save() {
      this.result = true;
      if (!this.saveData()) {
         this.result = false;
      }

      return this.result;
   }

   private boolean saveData() {
      boolean var1 = true;
      this.proxyData.setDataprovider(this.collectData());
      this.proxyData.save();
      if (this.proxyData.getErr().length() > 0) {
         if (!((SettingsDialog)this.parent).isPanelSelected(5)) {
            ((SettingsDialog)this.parent).jumpPanel(5);
         }

         GuiUtil.showMessageDialog(MainFrame.thisinstance, this.proxyData.getErr(), "Frissítés beállítási hiba", 0);
         var1 = false;
      }

      return var1;
   }

   public Hashtable getResult() {
      return this.collectData();
   }

   private Hashtable collectData() {
      Hashtable var1 = this.collectSaveData();
      return var1;
   }

   private Hashtable collectSaveData() {
      Hashtable var1 = new Hashtable();
      String var2;
      var1.put("proxy.host", (var2 = this.inputHost.getText()) == null ? "" : var2);
      var1.put("proxy.port", (var2 = this.inputPort.getText()) == null ? "" : var2);
      var1.put("proxy.user", (var2 = this.inputUser.getText()) == null ? "" : var2);
      var1.put("proxy.pwd", new String(this.inputpwd.getPassword()));
      var1.put("proxy.enabled", this.chkProxy.isSelected() ? "true" : "false");
      var1.put("proxy.authentication", this.chkAuthentic.isSelected() ? "true" : "false");
      return var1;
   }

   public boolean isResult() {
      return this.result;
   }

   public void setInset(boolean var1) {
      this.lblpwd.setVisible(var1);
      this.inputpwd.setVisible(var1);
      this.inset = var1;
   }

   public void setParent(Component var1) {
      this.parent = var1;
   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = var1.getActionCommand();
      if (var2.compareTo("Cancel") == 0) {
         this.cancel();
      } else if (var2.compareTo("Ok") == 0) {
         this.save();
      } else if (var2.compareTo("chkProxy") == 0) {
         this.enableProxy(this.chkProxy.isSelected());
      } else if (var2.compareTo("chkAuthentic") == 0) {
         this.enableAuthentic(this.chkAuthentic.isSelected());
      } else {
         this.save();
      }

   }
}
