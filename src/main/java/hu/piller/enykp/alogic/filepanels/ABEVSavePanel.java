package hu.piller.enykp.alogic.filepanels;

import hu.piller.enykp.alogic.filepanels.browserpanel.BrowserPanel;
import hu.piller.enykp.alogic.fileutil.FileNameResolver;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IFileChooser;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.interfaces.ISaveManager;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.eventsupport.DefaultEventSupport;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import hu.piller.enykp.util.filelist.EnykFileList;
import hu.piller.enykp.util.icon.ENYKIconSet;
import me.necrocore.abevjava.NecroFile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class ABEVSavePanel extends JDialog implements IEventSupport, IFileChooser, IEventListener, AncestorListener {
   public static final String FUNC_EXPORT = "export";
   public static final String FUNC_MULTI1 = "multi1";
   public static final String OPEN_DIALOG_TITLE = "Mentés";
   private boolean got_file_hint = false;
   private DefaultEventSupport des = new DefaultEventSupport();
   private BookModel gui_info;
   private JButton btn_get_mask;
   private JTextField txt_file_name;
   private JTextField txt_suffix;
   private JTextField txt_note;
   private JLabel lbl_note;
   private JButton btn_ok;
   private JButton btn_cancel;
   private JPanel mainPanel = new JPanel();
   private BrowserPanel panel;
   private FileNameResolver fnr;
   private File path = new NecroFile(this.getProperty("prop.sys.root"), this.getProperty("prop.usr.saves"));
   private String[] filters = new String[]{"inner_data_saver_v1"};
   private String mode = "open";
   private Hashtable result;
   int maxW = 800;

   public void setPath(File var1) {
      this.path = var1;
   }

   public void setFilters(String[] var1) {
      this.filters = var1;
   }

   public void setMode(String var1) {
      this.mode = var1;
   }

   public ABEVSavePanel(BookModel var1) {
      super(MainFrame.thisinstance);
      this.gui_info = var1;
      this.fnr = new FileNameResolver(var1);
      this.build();
      this.prepare();
   }

   public void addEventListener(IEventListener var1) {
      this.des.addEventListener(var1);
   }

   public void removeEventListener(IEventListener var1) {
      this.des.removeEventListener(var1);
   }

   public Object eventFired(Event var1) {
      Object var2 = var1.getUserData();
      if (var2 instanceof Hashtable) {
         Hashtable var3 = (Hashtable)var2;
         var2 = var3.get("event");
         if (var2 instanceof String) {
            this.handleEvent((String)var2, var3);
         }
      } else if (var2 instanceof String) {
         this.handleEvent((String)var2, (Hashtable)null);
      }

      return null;
   }

   private void handleEvent(String var1, Hashtable var2) {
      if (!var1.equals("before_start") && !var1.equals("after_start") && (var1.equalsIgnoreCase("afterclose") || var1.equalsIgnoreCase("aftersave"))) {
         this.txt_file_name.setText("");
         this.txt_suffix.setText("");
         this.txt_note.setText("");
      }

   }

   private String getSelectedFileName(boolean var1) {
      String var2 = this.txt_file_name.getText().trim();
      var2 = this.fnr.normalizeString(var2);
      String var3 = this.txt_suffix.getText().trim();
      if (!this.got_file_hint || var1 && var2.length() == 0) {
         var2 = this.fnr.normalizeString(this.fnr.getFileMask());
         var3 = "" + System.currentTimeMillis();
      }

      String var4 = var2 + var3;
      return var4;
   }

   private String getFileNote() {
      String var1 = null;

      try {
         var1 = (String)((Elem)this.gui_info.cc.getActiveObject()).getEtc().get("orignote");
      } catch (Exception var3) {
      }

      return var1;
   }

   private String getFileName(File var1) {
      IPropertyList var2 = this.getMasterPropertyList();
      String var3;
      File var4;
      if (var1 == null) {
         Object var5;
         if (var2 != null) {
            var5 = var2.get("prop.dynamic.opened_file");
         } else {
            var5 = null;
         }

         var3 = var5 == null ? "" : var5.toString();
         var4 = new NecroFile(var3);
      } else {
         var3 = var1.toString();
         var4 = var1;
      }

      if (var4.isDirectory()) {
         var3 = this.fnr.getFileMask();
      } else {
         var3 = (new NecroFile(var3)).getName();
      }

      return var3;
   }

   public void setSelectedFiles(File[] var1) {
      File var2 = null;
      if (var1 != null && var1.length > 0) {
         var2 = var1[0];
      }

      if (var2 != null) {
         this.got_file_hint = true;
         this.txt_file_name.setText(this.getFileName(var2));
         this.panel.setSelectedFiles(var1);
      }

      this.txt_note.setText(this.getFileNote());
   }

   public Object[] getSelectedFiles() {
      Object[] var1 = new Object[1];
      Object[] var2 = new Object[2];
      String[] var3 = this.getSelectedFilters();

      try {
         String var4 = this.fnr.removeInvalidFileChars(this.getSelectedFileName(true));
         EnykFileList var5 = EnykFileList.getInstance();
         ISaveManager var6 = (ISaveManager)var5.getFileManagerInstance(var3[0], 2);
         var2[0] = new NecroFile(this.panel.getFilePanel().getBusiness().getSelectedPath(), var6.createFileName(var4));
      } catch (Exception var7) {
         Tools.eLog(var7, 1);
      }

      if (var3.length > 0) {
         var2[1] = var3[0];
      }

      if (var2[0] != null && var2[1] != null) {
         var1[0] = var2;
      }

      this.got_file_hint = false;
      return var1;
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
      this.path = new NecroFile(var1);
   }

   public Vector fireEvent(Event var1) {
      return this.des.fireEvent(var1);
   }

   private void prepare() {
      this.setModal(true);
   }

   private void build() {
      this.panel = new BrowserPanel();
      this.panel.getFilePanel().getBusiness().setTask(3);
      this.panel.setAlignmentX(0.0F);
      this.mainPanel.setLayout(new BorderLayout());
      this.mainPanel.add(this.getExtensionPanel(), "Center");
      this.mainPanel.add(this.getButtonsPanel(), "South");
      this.mainPanel.setSize(new Dimension(this.maxW, 8 * (GuiUtil.getCommonItemHeight() + 4)));
      this.mainPanel.setPreferredSize(this.mainPanel.getSize());
      this.mainPanel.setMinimumSize(this.mainPanel.getSize());
      this.getContentPane().add(this.mainPanel);
      this.setLocationRelativeTo(MainFrame.thisinstance);
      this.setTitle("Mentés");
      this.setSize(new Dimension(this.maxW, 250));
   }

   private JPanel getExtensionPanel() {
      JPanel var1 = new JPanel();
      this.btn_get_mask = new JButton("Maszk >>");
      JLabel var2 = new JLabel("Állomány neve");
      this.txt_file_name = new JTextField();
      this.txt_file_name.setName("filename");
      int var4 = GuiUtil.getW("WWWWWWWWWW_WWWWWWWWWWWWWWWWWWWWWWWWW_WWWWWWWWWWWWW");
      this.txt_suffix = new JTextField();
      this.txt_suffix.setEditable(false);
      this.txt_suffix.setVisible(true);
      this.txt_suffix.setSize((int)(1.2D * (double)GuiUtil.getW(".frm.enyk")), GuiUtil.getCommonItemHeight() + 2);
      this.maxW = GuiUtil.getW(var2, var2.getText()) + GuiUtil.getW(this.btn_get_mask, this.btn_get_mask.getText()) + var4 + (int)(1.2D * (double)GuiUtil.getW(".frm.enyk"));
      this.maxW = (int)Math.min((double)this.maxW, 0.8D * (double)GuiUtil.getScreenW());
      this.lbl_note = new JLabel("Megjegyzés");
      this.txt_note = new JTextField();
      var1.setLayout(new GridBagLayout());
      var1.setAlignmentX(0.0F);
      GridBagConstraints var3 = new GridBagConstraints();
      var3.gridheight = 1;
      var3.gridwidth = 1;
      var3.anchor = 17;
      var3.gridx = 0;
      var3.gridy = 0;
      var3.fill = 0;
      var3.insets = new Insets(5, 5, 0, 0);
      var1.add(var2, var3);
      var3 = new GridBagConstraints();
      var3.gridheight = 1;
      var3.gridwidth = 1;
      var3.anchor = 17;
      var3.gridx = 1;
      var3.gridy = 0;
      var3.fill = 2;
      var3.insets = new Insets(5, 5, 0, 0);
      var1.add(this.btn_get_mask, var3);
      var3 = new GridBagConstraints();
      var3.gridheight = 1;
      var3.gridwidth = 1;
      var3.anchor = 17;
      var3.gridx = 2;
      var3.gridy = 0;
      var3.fill = 2;
      var3.weightx = 1.0D;
      var3.insets = new Insets(5, 5, 0, 5);
      var1.add(this.txt_file_name, var3);
      var3 = new GridBagConstraints();
      var3.gridheight = 1;
      var3.gridwidth = 1;
      var3.anchor = 17;
      var3.gridx = 3;
      var3.gridy = 0;
      var3.fill = 2;
      var3.weightx = 0.2D;
      var3.insets = new Insets(5, 5, 0, 5);
      var1.add(this.txt_suffix, var3);
      var3 = new GridBagConstraints();
      var3.gridheight = 1;
      var3.gridwidth = 1;
      var3.anchor = 17;
      var3.gridx = 0;
      var3.gridy = 1;
      var3.fill = 0;
      var3.insets = new Insets(5, 5, 5, 0);
      var1.add(this.lbl_note, var3);
      var3 = new GridBagConstraints();
      var3.gridheight = 1;
      var3.gridwidth = 3;
      var3.anchor = 17;
      var3.gridx = 1;
      var3.gridy = 1;
      var3.fill = 2;
      var3.weightx = 1.0D;
      var3.insets = new Insets(5, 5, 5, 5);
      var1.add(this.txt_note, var3);
      var1.setAlignmentX(0.0F);
      var1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
      var1.setPreferredSize(new Dimension(this.maxW, 100));
      var1.setSize(new Dimension(this.maxW, 100));
      var1.setMinimumSize(new Dimension(this.maxW, 100));
      this.btn_get_mask.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ABEVSavePanel.this.txt_file_name.setText(ABEVSavePanel.this.fnr.getFileMask() + "_" + System.currentTimeMillis());
         }
      });
      return var1;
   }

   private JPanel getButtonsPanel() {
      this.btn_ok = new JButton("Mentés");
      this.btn_cancel = new JButton("Mégsem");
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout(2, 5, 5));
      var1.add(this.btn_ok);
      var1.add(this.btn_cancel);
      var1.setAlignmentX(0.0F);
      this.btn_ok.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ABEVSavePanel.this.got_file_hint = true;
            String var2 = ABEVSavePanel.this.getSelectedFileName(false).trim();
            if (var2.length() > ABEVSavePanel.this.txt_suffix.getText().length()) {
               String var3 = ABEVSavePanel.this.panel.getFilePanel().getBusiness().getSelectedPath() + File.separator + var2;
               File var4 = new NecroFile(var3);
               if (!var4.exists() || 1 != JOptionPane.showConfirmDialog(ABEVSavePanel.this.mainPanel, "Ilyen nevű állomány már létezik !\n" + var4.getAbsolutePath() + "\nFelülírja ?", "Mentés", 0)) {
                  ABEVSavePanel.this.result = new Hashtable();
                  ABEVSavePanel.this.result.put("file_name", var3);
                  ABEVSavePanel.this.result.put("file_note", ABEVSavePanel.this.txt_note.getText());
                  ABEVSavePanel.this.setDialogVisible(false);
               }
            } else {
               GuiUtil.showMessageDialog(ABEVSavePanel.this, "Nem adott meg állomány nevet !", "Mentés", 2);
            }
         }
      });
      this.btn_cancel.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ABEVSavePanel.this.result = null;
            ABEVSavePanel.this.setDialogVisible(false);
         }
      });
      this.installKeyBindingForButton(this.btn_cancel, 27);
      ENYKIconSet var2 = ENYKIconSet.getInstance();
      this.setButtonIcon(this.btn_ok, "anyk_ellenorzes", var2);
      this.setButtonIcon(this.btn_cancel, "anyk_megse", var2);
      this.setDefaultCloseOperation(0);
      return var1;
   }

   private void installKeyBindingForButton(final JButton var1, int var2) {
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

   private void setButtonIcon(JButton var1, String var2, ENYKIconSet var3) {
      var1.setIcon(var3.get(var2));
   }

   public IFileChooser getFileChooser() {
      return this.panel;
   }

   private String getString(Object var1) {
      return var1 == null ? "" : var1.toString();
   }

   public void ancestorAdded(AncestorEvent var1) {
   }

   public void ancestorMoved(AncestorEvent var1) {
   }

   public void ancestorRemoved(AncestorEvent var1) {
   }

   private String getProperty(String var1) {
      String var4 = "";
      IPropertyList var2 = this.getMasterPropertyList();
      if (var2 != null) {
         Object var3 = var2.get(var1);
         if (var3 != null) {
            var4 = var3.toString();
         }
      }

      return var4;
   }

   private IPropertyList getMasterPropertyList() {
      return PropertyList.getInstance();
   }

   private String getExtension(String var1) {
      if (var1.equalsIgnoreCase("inner_data_saver_v1")) {
         return ".frm.enyk";
      } else {
         return var1.equalsIgnoreCase("imp_data_saver_v1") ? ".imp" : ".ismeretlen";
      }
   }

   private void setDialogVisible(boolean var1) {
      this.setVisible(var1);
   }

   public Hashtable showDialog() {
      if (this.mode.equalsIgnoreCase("export")) {
         String var1 = this.path + File.separator + "pelda.imp";
         this.result = new Hashtable();
         this.result.put("file_name", var1);
         if (this.getFileNote() != null) {
            this.result.put("file_note", this.getFileNote());
         }

         this.mainPanel.remove(this);
         return this.result;
      } else {
         if (this.mode.equalsIgnoreCase("multi1")) {
            this.setTitle("Egyedi mentés");
            this.txt_note.setVisible(true);
            this.lbl_note.setVisible(true);
            this.panel.setVisible(false);
            this.panel.getFilePanel().getBusiness().setTask(3);
         }

         this.txt_suffix.setText(this.getExtension(this.filters[0]));
         this.txt_file_name.setText(this.fnr.getFileMask() + "_" + System.currentTimeMillis());
         this.txt_note.setText(this.getFileNote());
         this.getFileChooser().setSelectedPath(this.path.toURI());
         this.btn_ok.setEnabled(true);
         this.panel.getFolderPanel().setVisible(false);
         this.pack();
         this.setVisible(true);
         this.mainPanel.remove(this);
         this.dispose();
         return this.result;
      }
   }
}
