package hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.abevfilechooser;

import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.archivefilepanel.browserpanel.BrowserPanel;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.interfaces.IFileChooser;
import hu.piller.enykp.util.base.eventsupport.DefaultEventSupport;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import hu.piller.enykp.util.icon.ENYKIconSet;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class ABEVArchivePanel extends JPanel implements IEventSupport, IFileChooser, IEventListener {
   public static final boolean debugOn = false;
   private DefaultEventSupport des = new DefaultEventSupport();
   private JCheckBox chk_read_only;
   private JButton btn_ok;
   public static ABEVArchivePanel this_;
   private int prev_read_only_selected;
   private BrowserPanel panel;

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
         if (!var2.equals("after_start")) {
            if (var2.equals("double_click_on_file")) {
               this.btn_ok.doClick(0);
            } else if (var2.equals("single_click_on_file")) {
               this.changeReadOnlySelection();
            }
         }
      } else if (var2 instanceof String) {
         String var4 = (String)var2;
         if (var4.equalsIgnoreCase("beforeopen")) {
            this.chk_read_only.setSelected(false);
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
   }

   public void showFilters(String[] var1) {
   }

   public void setSelectedPath(URI var1) {
   }

   public ABEVArchivePanel() {
      if (this_ == null) {
         this_ = this;
      }

      this.build();
      this.prepare();
   }

   private void build() {
      this.showMessage("ABEVArchivePanel.build ABEVFileChooser");
      this.panel = new BrowserPanel();
      this.showMessage("ABEVArchivePanel.build ABEVFileChooser end");
      this.panel.getFilePanel().getBusiness().setTask(2);
      this.panel.getFilePanel().getBusiness().addEventListener(this);
      this.setLayout(new BorderLayout(5, 5));
      this.add(this.panel, "Center");
      this.showMessage("ABEVArchivePanel.build end");
   }

   private void prepare() {
   }

   private void changeReadOnlySelection() {
      Object[] var1 = this.panel.getSelectedFiles();
      if (var1 != null && var1.length > 0 && var1[0] instanceof Object[]) {
         var1 = (Object[])((Object[])var1[0]);
         if (var1.length > 2 && var1[2] instanceof Hashtable) {
            Hashtable var2 = (Hashtable)var1[2];
            Object var3 = var2.get("state");
            if (var3 instanceof String) {
               String var4 = (String)var3;
               if (var4.equalsIgnoreCase("Módosítható")) {
                  this.chk_read_only.setEnabled(true);
                  if (this.prev_read_only_selected > 0) {
                     this.chk_read_only.setSelected(this.prev_read_only_selected != 1);
                     this.prev_read_only_selected = 0;
                  }
               } else {
                  this.prev_read_only_selected = this.chk_read_only.isSelected() ? 2 : 1;
                  this.chk_read_only.setEnabled(false);
                  this.chk_read_only.setSelected(true);
               }
            }
         }
      }

   }

   private JPanel getButtonsPanel() {
      this.chk_read_only = GuiUtil.getANYKCheckBox("Megnyitás csak olvasásra");
      this.btn_ok = new JButton("Megnyitás");
      JButton var2 = new JButton("Mégsem");
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout(2, 5, 5));
      var1.add(this.chk_read_only);
      var1.add(this.btn_ok);
      var1.add(var2);
      this.btn_ok.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Object[] var2 = ABEVArchivePanel.this.getSelectedFiles();
            if (var2 != null && var2.length > 0) {
               Hashtable var3 = new Hashtable();
               var3.put("file_chooser", ABEVArchivePanel.this.panel);
               var3.put("read_only", ABEVArchivePanel.this.chk_read_only.isSelected() ? Boolean.TRUE : Boolean.FALSE);
               ABEVArchivePanel.this.des.fireEvent(ABEVArchivePanel.this, "insert", "selected", var3, true);
            } else {
               GuiUtil.showMessageDialog(ABEVArchivePanel.this, "Nem választott ki nyomtatványt !", "Nyomtatvány megnyitása", 1);
            }
         }
      });
      var2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ABEVArchivePanel.this.des.fireEvent(ABEVArchivePanel.this, "insert", "cancel");
         }
      });
      this.installKeyBindingForButton(var2, 27);
      ENYKIconSet var3 = ENYKIconSet.getInstance();
      this.setButtonIcon(this.btn_ok, "anyk_ellenorzes", var3);
      this.setButtonIcon(var2, "anyk_megse", var3);
      return var1;
   }

   private void installKeyBindingForButton(final JButton var1, int var2) {
      String var3 = KeyStroke.getKeyStroke(var2, 0).toString() + "Pressed";
      this.getInputMap(2).put(KeyStroke.getKeyStroke(var2, 0), var3);
      this.getActionMap().put(var3, new AbstractAction() {
         public void actionPerformed(ActionEvent var1x) {
            if (var1.isVisible() && var1.isEnabled()) {
               var1.doClick();
            }

         }
      });
   }

   private void setButtonIcon(JButton var1, String var2, ENYKIconSet var3) {
      var1.setIcon(var3.get(var2));
   }

   public BrowserPanel getFileChooser() {
      return this.panel;
   }

   private void showMessage(String var1) {
   }

   public BrowserPanel getPanel() {
      return this.panel;
   }
}
