package hu.piller.enykp.alogic.filepanels.datafileoperations;

import hu.piller.enykp.alogic.ebev.SendParams;
import hu.piller.enykp.alogic.filepanels.filepanel.FileBusiness;
import hu.piller.enykp.alogic.filepanels.filepanel.FilePanel;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.ICommandObject;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.SizeAndPositonSaveDialog;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.eventsupport.CloseEvent;
import java.awt.Container;
import java.io.File;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class CopyFromSaveFolder implements ICommandObject {
   private String[] filters = new String[]{"inner_data_loader_v1"};
   private static CopyFromSaveFolder instance;
   private final Object[] update_skin = new Object[]{"work_panel", "static", "Adat állomány műveletek", "tab_close", null};
   private FilePanel file_panel;
   private FileBusiness business;
   private File enyk_path;
   private File outer_path;
   private File current_path;
   private File other_path;
   private SizeAndPositonSaveDialog dlg;
   private boolean is_first_use = true;
   private final Vector cmd_list = new Vector(Arrays.asList("showCopyFromSaveFolderDialog"));
   Boolean[] states;

   private CopyFromSaveFolder() {
      this.states = new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
      this.build();
      this.prepare();
   }

   private void build() {
      this.file_panel = new FilePanel();
      this.business = this.file_panel.getBusiness();
      this.business.setTask(11);
   }

   private void prepare() {
      CopyFromSaveFolder.ButtonActions var1 = new CopyFromSaveFolder.ButtonActions(this.file_panel);
      this.enyk_path = new File(this.getProperty("prop.usr.root"), this.getProperty("prop.usr.saves"));
      this.other_path = null;
      var1.b11Clicked();
      this.business.setButtonExecutor(var1);
   }

   public static CopyFromSaveFolder getInstance() {
      if (instance == null) {
         instance = new CopyFromSaveFolder();
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
      this.business.setSelectedPath(this.enyk_path);
      this.business.loadFilterSettings((String)this.cmd_list.get(0));
      this.current_path = this.enyk_path;
      if (var1) {
         this.file_panel.invalidate();
         this.file_panel.setVisible(true);
         var2.add(this.file_panel);
         this.dlg.setTitle("Másolás a mentés könyvtárból");
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

      public void b13Clicked() {
         Object[] var1 = Operations.getSelectedFiles(CopyFromSaveFolder.this.business);
         if (var1 == null) {
            GuiUtil.showMessageDialog(CopyFromSaveFolder.this.dlg, "Nem választott ki állományt !", "Átnevezés", 1);
         } else if (var1.length == 1) {
            Object[] var2 = (Object[])((Object[])var1[0]);
            File var3 = (File)var2[0];
            String var4 = (String)((Hashtable)var2[2]).get("state");
            if (var4.equals("Módosítható")) {
               Tools.startCopy();
               String var5 = Operations.renameFile(CopyFromSaveFolder.this.dlg, var3, var2[1]);
               if (var5 == null) {
                  return;
               }

               Tools.renameAtc(var1, var5, true);
               Tools.endCopy();
            } else {
               GuiUtil.showMessageDialog(CopyFromSaveFolder.this.dlg, "Az állomány nem 'Módosítható' állapotú, ezért nem nevezhető át !", "Átnevezés", 0);
            }

            CopyFromSaveFolder.this.business.refreshFileInfos();
         } else {
            GuiUtil.showMessageDialog(CopyFromSaveFolder.this.dlg, "Átnevezésnél csak egy állományt jelölhet ki a művelethez !", "Átnevezés", 1);
         }

      }

      public void b14Clicked() {
         Object[] var1 = Operations.getSelectedFiles(CopyFromSaveFolder.this.business);
         if (var1 == null) {
            GuiUtil.showMessageDialog(CopyFromSaveFolder.this.dlg, "Nem választott ki állományt !", "Törlés", 1);
         } else {
            Tools.startCopy();
            Operations.deleteFiles(CopyFromSaveFolder.this.dlg, (Object[])var1);
            if (((File)((Object[])((Object[])var1[0]))[0]).exists()) {
               return;
            }

            Tools.delAtc(var1, true);
            Tools.delDSIG(new SendParams(PropertyList.getInstance()), Operations.getFileArray(var1));
            Tools.delXczStatus(new SendParams(PropertyList.getInstance()), Operations.getFileArray(var1));
            Tools.endCopy();
            CopyFromSaveFolder.this.business.refreshFileInfos();
         }

      }

      public void b21Clicked() {
         Object[] var1 = Operations.getSelectedFiles(CopyFromSaveFolder.this.business);
         if (var1 == null) {
            GuiUtil.showMessageDialog(CopyFromSaveFolder.this.dlg, "Nem választott ki állományt !", "Másolás", 1);
         } else if (CopyFromSaveFolder.this.other_path == null) {
            GuiUtil.showMessageDialog(CopyFromSaveFolder.this.dlg, "Cél könyvtár nincs kiválasztva !", "Másolás", 1);
         } else {
            Tools.startCopy();
            File[] var2 = Operations.getFileArray(var1);
            int var3 = 0;
            SendParams var4 = new SendParams(PropertyList.getInstance());

            for(int var5 = 0; var5 < var2.length; ++var5) {
               File var6 = var2[var5];
               Object[] var7 = new Object[]{var1[var5]};
               if (Operations.before_copyWithAtc(var6, CopyFromSaveFolder.this.other_path)) {
                  Operations.copyFile__(var6, new File(CopyFromSaveFolder.this.other_path, var6.getName()));
                  Vector var8 = new Vector();
                  Tools.copyAtc(var7, CopyFromSaveFolder.this.other_path.getAbsolutePath(), var8);
                  String var9 = var6.getName().substring(0, var6.getName().length() - ".frm.enyk".length());
                  Tools.copyDSIGFolder(var4, var9, CopyFromSaveFolder.this.other_path.getAbsolutePath());
                  ++var3;
               }
            }

            Tools.endCopy();
            GuiUtil.showMessageDialog(CopyFromSaveFolder.this.dlg, "Művelet befejezve!\n" + var3 + " darab nyomtatvány másolva.", "Másolás", 1);
         }

      }

      public void b22PathClicked() {
         File var1 = Operations.getFolder(CopyFromSaveFolder.this.dlg, CopyFromSaveFolder.this.outer_path);
         if (var1 != null) {
            if (var1.equals(CopyFromSaveFolder.this.enyk_path)) {
               GuiUtil.showMessageDialog(CopyFromSaveFolder.this.dlg, "Válasszon másik mappát !\nA Mentés könyvtárral nem egyezhet meg a Cél könyvtár !", "Könyvtár választás", 2);
            } else if (!var1.exists()) {
               GuiUtil.showMessageDialog(CopyFromSaveFolder.this.dlg, "Válasszon másik mappát !\nA megadott mappa nem létezik !", "Könyvtár választás", 2);
            } else {
               CopyFromSaveFolder.this.outer_path = var1;
               this.txt_path.setText(CopyFromSaveFolder.this.outer_path.getPath());
               if (CopyFromSaveFolder.this.current_path == CopyFromSaveFolder.this.enyk_path) {
                  CopyFromSaveFolder.this.other_path = CopyFromSaveFolder.this.outer_path;
               } else {
                  CopyFromSaveFolder.this.current_path = CopyFromSaveFolder.this.outer_path;
               }
            }
         }

      }

      public void b32Clicked() {
         CopyFromSaveFolder.this.dlg.loadSettings((String)CopyFromSaveFolder.this.cmd_list.get(0));
         CopyFromSaveFolder.this.business.saveFilterSettings((String)CopyFromSaveFolder.this.cmd_list.get(0));
         CopyFromSaveFolder.this.dlg.setVisible(false);
         this.file_panel.fireEvent(new CloseEvent(this.file_panel));
      }
   }
}
