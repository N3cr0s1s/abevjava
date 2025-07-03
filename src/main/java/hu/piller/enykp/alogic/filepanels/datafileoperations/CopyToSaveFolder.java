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

public class CopyToSaveFolder implements ICommandObject {
   private String[] filters = new String[]{"inner_data_loader_v1"};
   private static CopyToSaveFolder instance;
   private final Object[] update_skin = new Object[]{"work_panel", "static", "Adat állomány műveletek", "tab_close", null};
   private FilePanel file_panel;
   private FileBusiness business;
   private File enyk_path;
   private File outer_path;
   private File current_path;
   private File other_path;
   private SizeAndPositonSaveDialog dlg;
   private boolean is_first_use = true;
   private final Vector cmd_list = new Vector(Arrays.asList("showCopyToSaveFolderDialog"));
   Boolean[] states;

   private CopyToSaveFolder() {
      this.states = new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
      this.build();
      this.prepare();
   }

   private void build() {
      this.file_panel = new FilePanel();
      this.business = this.file_panel.getBusiness();
      this.business.setTask(13);
   }

   private void prepare() {
      CopyToSaveFolder.ButtonActions var1 = new CopyToSaveFolder.ButtonActions(this.file_panel);
      this.enyk_path = new File(this.getProperty("prop.usr.root"), this.getProperty("prop.usr.saves"));
      this.outer_path = new File("");
      var1.b11Clicked();
      this.business.setButtonExecutor(var1);
   }

   public static CopyToSaveFolder getInstance() {
      if (instance == null) {
         instance = new CopyToSaveFolder();
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
         this.dlg.setTitle("Másolás a mentés könyvtárba");
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
         if (CopyToSaveFolder.this.outer_path != null && CopyToSaveFolder.this.outer_path.exists()) {
            this.lbl_file_list_title.setText("Forrás könyvtár adat állományai");
            CopyToSaveFolder.this.business.setSelectedPath(CopyToSaveFolder.this.outer_path);
            if (!CopyToSaveFolder.this.is_first_use) {
               CopyToSaveFolder.this.business.rescan();
            }

            CopyToSaveFolder.this.current_path = CopyToSaveFolder.this.outer_path;
            CopyToSaveFolder.this.other_path = CopyToSaveFolder.this.enyk_path;
         } else {
            GuiUtil.showMessageDialog(CopyToSaveFolder.this.dlg, "Forrás könyvtár útvonal nincs kiválasztva !", "Nézet váltás", 2);
         }
      }

      public void b13Clicked() {
         Object[] var1 = Operations.getSelectedFiles(CopyToSaveFolder.this.business);
         if (var1 == null) {
            GuiUtil.showMessageDialog(CopyToSaveFolder.this.dlg, "Nem választott ki állományt !", "Átnevezés", 1);
         } else if (var1.length == 1) {
            Object[] var2 = (Object[])((Object[])var1[0]);
            File var3 = (File)var2[0];
            String var4 = (String)((Hashtable)var2[2]).get("state");
            if (var4.equals("Módosítható")) {
               Tools.startCopy();
               String var5 = Operations.renameFile(CopyToSaveFolder.this.dlg, var3, var2[1]);
               Tools.renameAtc(var1, var5, false);
               Tools.endCopy();
            } else {
               GuiUtil.showMessageDialog(CopyToSaveFolder.this.dlg, "Az állomány nem 'Módosítható' állapotú, ezért nem nevezhető át !", "Átnevezés", 0);
            }

            CopyToSaveFolder.this.business.refreshFileInfos();
         } else {
            GuiUtil.showMessageDialog(CopyToSaveFolder.this.dlg, "Átnevezésnél csak egy állományt jelölhet ki a művelethez !", "Átnevezés", 1);
         }

      }

      public void b14Clicked() {
         Object[] var1 = Operations.getSelectedFiles(CopyToSaveFolder.this.business);
         if (var1 == null) {
            GuiUtil.showMessageDialog(CopyToSaveFolder.this.dlg, "Nem választott ki állományt !", "Törlés", 1);
         } else {
            Tools.startCopy();
            Operations.deleteFiles(CopyToSaveFolder.this.dlg, (Object[])var1);
            if (((File)((Object[])((Object[])var1[0]))[0]).exists()) {
               return;
            }

            Tools.delAtc(var1, false);
            Tools.endCopy();
            CopyToSaveFolder.this.business.refreshFileInfos();
         }

      }

      public void b21Clicked() {
         Object[] var1 = Operations.getSelectedFiles(CopyToSaveFolder.this.business);
         if (var1 == null) {
            GuiUtil.showMessageDialog(CopyToSaveFolder.this.dlg, "Nem választott ki állományt !", "Másolás", 1);
         } else if (CopyToSaveFolder.this.other_path == null) {
            GuiUtil.showMessageDialog(CopyToSaveFolder.this.dlg, "Forrás könyvtár nincs kiválasztva !", "Másolás", 1);
         } else {
            File[] var2 = Operations.getFileArray(var1);
            int var3 = 0;

            for(int var4 = 0; var4 < var2.length; ++var4) {
               File var5 = var2[var4];
               File var6 = new File(CopyToSaveFolder.this.other_path, var5.getName());
               if (var6.exists()) {
                  GuiUtil.showMessageDialog(CopyToSaveFolder.this.dlg, "A cél állomány már létezik, nem írható felül.\n" + var6.getName(), "Másolás", 0);
               } else {
                  Operations.copyFile__(var5, new File(CopyToSaveFolder.this.other_path, var5.getName()));
                  ++var3;
                  Vector var7 = new Vector();

                  String var8;
                  try {
                     var8 = CopyToSaveFolder.this.current_path + File.separator;
                     Object[] var9 = (Object[])((Object[])var1[var4]);
                     String var10 = ((File)var9[0]).getName();
                     if (var7.contains(var10)) {
                        continue;
                     }

                     Tools.resetAtc(var10, var8, false, "");
                  } catch (Exception var11) {
                     GuiUtil.showMessageDialog(CopyToSaveFolder.this.dlg, "Nem sikerült a fájlhoz tartozó csatolmányok másolása", "Másolás", 0);
                  }

                  var8 = var5.getName().substring(0, var5.getName().length() - ".frm.enyk".length());
                  Tools._copyDSIGFolder(new SendParams(PropertyList.getInstance()), var8, var5.getParent());
               }
            }

            GuiUtil.showMessageDialog(CopyToSaveFolder.this.dlg, "Művelet befejezve!\n" + var3 + " darab nyomtatvány másolva.", "Másolás", 1);
         }

      }

      public void b22PathClicked() {
         File var1 = Operations.getFolder(CopyToSaveFolder.this.dlg, CopyToSaveFolder.this.outer_path);
         if (var1 != null) {
            if (var1.equals(CopyToSaveFolder.this.enyk_path)) {
               GuiUtil.showMessageDialog(CopyToSaveFolder.this.dlg, "Válasszon másik mappát !\nAz Mentés könyvtárral nem egyezhet meg a Forrás könyvtár !", "Könyvtár választás", 2);
            } else if (!var1.exists()) {
               GuiUtil.showMessageDialog(CopyToSaveFolder.this.dlg, "Válasszon másik mappát !\nA megadott mappa nem létezik !", "Könyvtár választás", 2);
            } else {
               CopyToSaveFolder.this.outer_path = var1;
               this.txt_path.setText(CopyToSaveFolder.this.outer_path.getPath());
               if (CopyToSaveFolder.this.current_path == CopyToSaveFolder.this.enyk_path) {
                  CopyToSaveFolder.this.other_path = CopyToSaveFolder.this.outer_path;
               } else {
                  CopyToSaveFolder.this.current_path = CopyToSaveFolder.this.outer_path;
               }

               this.b12Clicked();
            }
         }

      }

      public void b32Clicked() {
         CopyToSaveFolder.this.dlg.loadSettings((String)CopyToSaveFolder.this.cmd_list.get(0));
         CopyToSaveFolder.this.business.saveFilterSettings((String)CopyToSaveFolder.this.cmd_list.get(0));
         CopyToSaveFolder.this.dlg.setVisible(false);
         this.file_panel.fireEvent(new CloseEvent(this.file_panel));
      }
   }
}
