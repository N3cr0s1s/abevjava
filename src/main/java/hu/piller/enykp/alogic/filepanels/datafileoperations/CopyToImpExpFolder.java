package hu.piller.enykp.alogic.filepanels.datafileoperations;

import hu.piller.enykp.alogic.filepanels.filepanel.FileBusiness;
import hu.piller.enykp.alogic.filepanels.filepanel.FilePanel;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.ICommandObject;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.SizeAndPositonSaveDialog;
import hu.piller.enykp.util.base.eventsupport.CloseEvent;
import java.awt.Container;
import java.io.File;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class CopyToImpExpFolder implements ICommandObject {
   private String[] filters = new String[]{"imp_data_loader_v1", "dat_data_loader_v1", "xml_data_loader_v1", "xkr_data_loader_v1"};
   private static CopyToImpExpFolder instance;
   private final Object[] update_skin = new Object[]{"work_panel", "static", "Adat állomány műveletek", "tab_close", null};
   private FilePanel file_panel;
   private FileBusiness business;
   private File enyk_path;
   private File outer_path;
   private File current_path;
   private File other_path;
   private SizeAndPositonSaveDialog dlg;
   private boolean is_first_use = true;
   private final Vector cmd_list = new Vector(Arrays.asList("showCopyToImpExpFolderDialog"));
   Boolean[] states;

   private CopyToImpExpFolder() {
      this.states = new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
      this.build();
      this.prepare();
   }

   private void build() {
      this.file_panel = new FilePanel();
      this.business = this.file_panel.getBusiness();
      this.business.setTask(12);
   }

   private void prepare() {
      CopyToImpExpFolder.ButtonActions var1 = new CopyToImpExpFolder.ButtonActions(this.file_panel);
      this.enyk_path = new File(this.getProperty("prop.usr.root"), this.getProperty("prop.usr.import"));
      this.outer_path = new File("");
      var1.b11Clicked();
      this.business.setButtonExecutor(var1);
   }

   public static CopyToImpExpFolder getInstance() {
      if (instance == null) {
         instance = new CopyToImpExpFolder();
      }

      return instance;
   }

   public void execute() {
      this.build();
      this.prepare();
      this.dlg = new SizeAndPositonSaveDialog(MainFrame.thisinstance);
      boolean var1 = true;
      Container var2 = this.dlg.getContentPane();
      this.is_first_use = false;
      this.business.addFilters(this.filters, (String[])null);
      this.business.setSelectedPath(this.outer_path);
      this.business.loadFilterSettings((String)this.cmd_list.get(0));
      if (var1) {
         this.file_panel.invalidate();
         this.file_panel.setVisible(true);
         var2.add(this.file_panel);
         this.dlg.setTitle("Másolás az Import/Export könyvtárba");
         this.dlg.setLoadedSize((String)this.cmd_list.get(0), (int)((double)GuiUtil.getScreenW() * 0.6D), (int)((double)GuiUtil.getScreenH() * 0.8D));
         this.dlg.setResizable(true);
         this.dlg.setModal(true);
         this.dlg.setVisible(true);
      }

   }

   public void setParameters(Hashtable var1) {
   }

   public ICommandObject copy() {
      return getInstance();
   }

   public boolean isCommandIdentical(String var1) {
      if (var1 != null) {
         var1 = var1.trim();
         if (var1.equalsIgnoreCase(this.cmd_list.get(0).toString())) {
            return true;
         }
      }

      return false;
   }

   public Vector getCommandList() {
      return this.cmd_list;
   }

   public Hashtable getCommandHelps() {
      return null;
   }

   private String getProperty(String var1) {
      String var4 = "";
      IPropertyList var2 = PropertyList.getInstance();
      Object var3 = var2.get(var1);
      if (var3 != null) {
         var4 = var3.toString();
      }

      return var4;
   }

   public Object getState(Object var1) {
      if (var1 instanceof Integer) {
         int var2 = (Integer)var1;
         return var2 >= 0 && var2 <= this.states.length - 1 ? this.states[var2] : Boolean.FALSE;
      } else {
         return Boolean.FALSE;
      }
   }

   private class ButtonActions extends FileBusiness.ButtonExecutor {
      private JLabel lbl_file_list_title;
      private JTextField txt_path;

      public ButtonActions(FilePanel var2) {
         super(var2);
         this.lbl_file_list_title = (JLabel)var2.getFPComponent("files_title_lbl");
         this.txt_path = (JTextField)var2.getFPComponent("path_txt");
         JTable var3 = (JTable)var2.getFPComponent("files");
         if (var3 != null) {
            var3.getSelectionModel().setSelectionMode(2);
         }

      }

      public void b12Clicked() {
         if (CopyToImpExpFolder.this.outer_path != null && CopyToImpExpFolder.this.outer_path.exists()) {
            this.lbl_file_list_title.setText("Forrás könyvtár adat állományai");
            CopyToImpExpFolder.this.business.setSelectedPath(CopyToImpExpFolder.this.outer_path);
            if (!CopyToImpExpFolder.this.is_first_use) {
               CopyToImpExpFolder.this.business.rescan();
            }

            CopyToImpExpFolder.this.current_path = CopyToImpExpFolder.this.outer_path;
            CopyToImpExpFolder.this.other_path = CopyToImpExpFolder.this.enyk_path;
         } else {
            GuiUtil.showMessageDialog(CopyToImpExpFolder.this.dlg, "Forrás könyvtár útvonal nincs kiválasztva !", "Nézet váltás", 2);
         }
      }

      public void b13Clicked() {
         Object[] var1 = Operations.getSelectedFiles(CopyToImpExpFolder.this.business);
         if (var1 == null) {
            GuiUtil.showMessageDialog(CopyToImpExpFolder.this.dlg, "Nem választott ki állományt !", "Átnevezés", 1);
         } else if (var1.length == 1) {
            Object[] var2 = (Object[])((Object[])var1[0]);
            File var3 = (File)var2[0];
            String var4 = (String)((Hashtable)var2[2]).get("state");
            Operations.renameFile(CopyToImpExpFolder.this.dlg, var3, var2[1]);
            CopyToImpExpFolder.this.business.refreshFileInfos();
         } else {
            GuiUtil.showMessageDialog(CopyToImpExpFolder.this.dlg, "Átnevezésnél csak egy állományt jelölhet ki a művelethez !", "Átnevezés", 1);
         }

      }

      public void b14Clicked() {
         Object[] var1 = Operations.getSelectedFiles(CopyToImpExpFolder.this.business);
         if (var1 == null) {
            GuiUtil.showMessageDialog(CopyToImpExpFolder.this.dlg, "Nem választott ki állományt !", "Törlés", 1);
         } else {
            Operations.deleteFiles(CopyToImpExpFolder.this.dlg, (Object[])var1);
            CopyToImpExpFolder.this.business.refreshFileInfos();
         }

      }

      public void b21Clicked() {
         Object[] var1 = Operations.getSelectedFiles(CopyToImpExpFolder.this.business);
         if (var1 == null) {
            GuiUtil.showMessageDialog(CopyToImpExpFolder.this.dlg, "Nem választott ki állományt !", "Másolás", 1);
         } else if (CopyToImpExpFolder.this.other_path == null) {
            GuiUtil.showMessageDialog(CopyToImpExpFolder.this.dlg, "Forrás könyvtár nincs kiválasztva !", "Másolás", 1);
         } else {
            Operations.copyFiles(CopyToImpExpFolder.this.dlg, (Object[])var1, CopyToImpExpFolder.this.other_path);
         }

      }

      public void b22PathClicked() {
         File var1 = Operations.getFolder(CopyToImpExpFolder.this.dlg, CopyToImpExpFolder.this.outer_path);
         if (var1 != null) {
            if (var1.equals(CopyToImpExpFolder.this.enyk_path)) {
               GuiUtil.showMessageDialog(CopyToImpExpFolder.this.dlg, "Válasszon másik mappát !\nAz Import/Export könyvtárral nem egyezhet meg a Forrás könyvtár !", "Könyvtár választás", 2);
            } else if (!var1.exists()) {
               GuiUtil.showMessageDialog(CopyToImpExpFolder.this.dlg, "Válasszon másik mappát !\nA megadott mappa nem létezik !", "Könyvtár választás", 2);
            } else {
               CopyToImpExpFolder.this.outer_path = var1;
               this.txt_path.setText(CopyToImpExpFolder.this.outer_path.getPath());
               if (CopyToImpExpFolder.this.current_path == CopyToImpExpFolder.this.enyk_path) {
                  CopyToImpExpFolder.this.other_path = CopyToImpExpFolder.this.outer_path;
               } else {
                  CopyToImpExpFolder.this.current_path = CopyToImpExpFolder.this.outer_path;
               }

               this.b12Clicked();
            }
         }

      }

      public void b32Clicked() {
         CopyToImpExpFolder.this.dlg.loadSettings((String)CopyToImpExpFolder.this.cmd_list.get(0));
         CopyToImpExpFolder.this.business.saveFilterSettings((String)CopyToImpExpFolder.this.cmd_list.get(0));
         CopyToImpExpFolder.this.dlg.setVisible(false);
         this.file_panel.fireEvent(new CloseEvent(this.file_panel));
      }
   }
}
