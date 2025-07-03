package hu.piller.enykp.alogic.panels;

import hu.piller.enykp.alogic.filepanels.filepanel.FileBusiness;
import hu.piller.enykp.alogic.filepanels.filepanel.FilePanel;
import hu.piller.enykp.alogic.filepanels.filepanel.ListItem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.ICommandObject;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.Version;
import hu.piller.enykp.util.base.eventsupport.CloseEvent;
import java.awt.Container;
import java.io.File;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class InstalledForms implements ICommandObject {
   private static InstalledForms instance;
   private String[] filters = new String[]{"template_loader_v1"};
   public static final String FORM_NAME = "InstalledForms";
   private final Object[] update_skin = new Object[]{"work_panel", "static", "Telepített sablonok", "tab_close", null};
   private FilePanel file_panel;
   private FileBusiness business;
   private JDialog dlg;
   private final Vector cmd_list = new Vector(Arrays.asList("abev.showInstalledTemplatesDialog"));
   Boolean[] states;

   private InstalledForms() {
      this.states = new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
      this.build();
      this.prepare();
   }

   private void build() {
      this.file_panel = new FilePanel();
      this.business = this.file_panel.getBusiness();
      this.business.setTask(6);
   }

   private void prepare() {
      this.business.setButtonExecutor(new InstalledForms.ButtonActions(this.file_panel));
      this.business.setSelectedPath(new File(this.getProperty("prop.dynamic.templates.absolutepath")));
      this.update_skin[4] = this.file_panel;
   }

   public static InstalledForms getInstance() {
      if (instance == null) {
         instance = new InstalledForms();
      }

      return instance;
   }

   public void execute() {
      this.build();
      this.prepare();
      this.dlg = new JDialog(MainFrame.thisinstance);
      boolean var1 = true;
      Container var2 = this.dlg.getContentPane();
      this.business.addFilters(this.filters, (String[])null);
      this.business.rescan();
      this.file_panel.getBusiness().loadFilterSettings("InstalledForms");
      if (var1) {
         this.file_panel.invalidate();
         this.file_panel.setVisible(true);
         var2.add(this.file_panel);
         this.dlg.setTitle("Telepített nyomtatványok");
         this.dlg.setSize((int)((double)GuiUtil.getScreenW() * 0.6D), (int)((double)GuiUtil.getScreenH() * 0.8D));
         this.dlg.setResizable(true);
         this.dlg.setLocationRelativeTo(MainFrame.thisinstance);
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

   public Object getState(Object var1) {
      if (var1 instanceof Integer) {
         int var2 = (Integer)var1;
         return var2 >= 0 && var2 <= this.states.length - 1 ? this.states[var2] : Boolean.FALSE;
      } else {
         return Boolean.FALSE;
      }
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

   private class ButtonActions extends FileBusiness.ButtonExecutor {
      public ButtonActions(FilePanel var2) {
         super(var2);
      }

      public void b11Clicked() {
         try {
            Object[] var1 = InstalledForms.this.business.getSelectedFiles();
            if (var1 == null) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem jelölt meg nyomtatványt!", "Üzenet", 0);
               return;
            }

            Object[] var2 = (Object[])((Object[])var1[0]);
            File var3 = (File)var2[0];
            Hashtable var4 = (Hashtable)var2[3];
            var4 = (Hashtable)var4.get("docinfo");
            String var5 = (String)var4.get("org");
            String var6 = (String)var4.get("name");
            String var7 = (String)var4.get("id");
            String var8 = (String)var4.get("ver");
            String var9 = var5 + " szervezet " + var6 + " nevű nyomtatványának összes verzióját az utolsó nélkül akarja törölni! Folytatja?";
            if (0 == JOptionPane.showConfirmDialog(MainFrame.thisinstance, var9, "Telepített nyomtatvány törlése", 0)) {
               Vector var10 = InstalledForms.this.business.getVct_files();
               int var11 = 0;
               Vector var12 = new Vector();
               String var13 = null;

               int var14;
               ListItem var15;
               File var16;
               Hashtable var17;
               String var18;
               String var19;
               String var20;
               String var21;
               for(var14 = 0; var14 < var10.size(); ++var14) {
                  try {
                     var15 = (ListItem)var10.get(var14);
                     var16 = (File)var15.getItem();
                     var17 = (Hashtable)var15.getText();
                     var17 = (Hashtable)var17.get("docinfo");
                     var18 = (String)var17.get("org");
                     var19 = (String)var17.get("name");
                     var20 = (String)var17.get("id");
                     var21 = (String)var17.get("ver");
                     if (var18.equals(var5) && var20.equals(var7) && var19.equals(var6)) {
                        var12.add(var15);
                        if (var13 == null) {
                           var13 = var21;
                        }

                        if ((new Version(var21)).compareTo(new Version(var13)) >= 1) {
                           var13 = var21;
                        }
                     }
                  } catch (Exception var23) {
                     Tools.eLog(var23, 0);
                  }
               }

               for(var14 = 0; var14 < var12.size(); ++var14) {
                  try {
                     var15 = (ListItem)var12.get(var14);
                     var16 = (File)var15.getItem();
                     var17 = (Hashtable)var15.getText();
                     var17 = (Hashtable)var17.get("docinfo");
                     var18 = (String)var17.get("org");
                     var19 = (String)var17.get("name");
                     var20 = (String)var17.get("id");
                     var21 = (String)var17.get("ver");
                     if (var18.equals(var5) && var20.equals(var7) && var19.equals(var6) && !var13.equals(var21)) {
                        var16.delete();
                        ++var11;
                     }
                  } catch (Exception var22) {
                     Tools.eLog(var22, 0);
                  }
               }

               InstalledForms.this.business.rescan();
               String var25 = var11 + " darab állomány törölve!";
               GuiUtil.showMessageDialog(MainFrame.thisinstance, var25, "Üzenet", 1);
            }
         } catch (Exception var24) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A bejegyzéshez tartozó állomány azonosítása nem sikerült!", "Üzenet", 0);
         }

      }

      public void b12Clicked() {
      }

      public void b13Clicked() {
      }

      public void b14Clicked() {
         try {
            Object[] var1 = InstalledForms.this.business.getSelectedFiles();
            if (var1 == null) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem jelölt meg nyomtatványt!", "Üzenet", 0);
               return;
            }

            Object[] var2 = (Object[])((Object[])var1[0]);
            File var3 = (File)var2[0];
            Hashtable var4 = (Hashtable)var2[3];
            var4 = (Hashtable)var4.get("docinfo");
            String var5 = (String)var4.get("org");
            String var6 = (String)var4.get("name");
            String var7 = (String)var4.get("id");
            String var8 = (String)var4.get("ver");
            String var9 = var5 + " szervezet " + var6 + " nevű nyomtatványának összes verzióját akarja törölni! Folytatja?";
            if (0 == JOptionPane.showConfirmDialog(MainFrame.thisinstance, var9, "Telepített nyomtatvány törlése", 0)) {
               Vector var10 = InstalledForms.this.business.getVct_files();
               int var11 = 0;

               for(int var12 = 0; var12 < var10.size(); ++var12) {
                  try {
                     ListItem var13 = (ListItem)var10.get(var12);
                     File var14 = (File)var13.getItem();
                     Hashtable var15 = (Hashtable)var13.getText();
                     var15 = (Hashtable)var15.get("docinfo");
                     String var16 = (String)var15.get("org");
                     String var17 = (String)var15.get("name");
                     String var18 = (String)var15.get("id");
                     if (var16.equals(var5) && var18.equals(var7) && var17.equals(var6)) {
                        var14.delete();
                        ++var11;
                     }
                  } catch (Exception var19) {
                     Tools.eLog(var19, 0);
                  }
               }

               InstalledForms.this.business.rescan();
               String var21 = var11 + " darab állomány törölve!";
               GuiUtil.showMessageDialog(MainFrame.thisinstance, var21, "Üzenet", 1);
            }
         } catch (Exception var20) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A bejegyzéshez tartozó állomány azonosítása nem sikerült!", "Üzenet", 0);
         }

      }

      public void b15Clicked() {
         try {
            Object[] var1 = InstalledForms.this.business.getSelectedFiles();
            if (var1 == null) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem jelölt meg nyomtatványt!", "Üzenet", 0);
               return;
            }

            Object[] var2 = (Object[])((Object[])var1[0]);
            File var3 = (File)var2[0];
            Hashtable var4 = (Hashtable)var2[3];
            var4 = (Hashtable)var4.get("docinfo");
            String var5 = (String)var4.get("org");
            String var6 = (String)var4.get("name");
            String var7 = (String)var4.get("id");
            String var8 = (String)var4.get("ver");
            if (var3.exists()) {
               String var9 = var5 + " szervezet " + var6 + " nevű " + var8 + " verziójú nyomtatványát akarja törölni! Folytatja?";
               if (0 == JOptionPane.showConfirmDialog(MainFrame.thisinstance, var9, "Telepített nyomtatvány törlése", 0)) {
                  boolean var10 = var3.delete();
                  if (var10) {
                     InstalledForms.this.business.rescan();
                  } else {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "A törlés nem sikerült!", "Üzenet", 0);
                  }
               }
            } else {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "A bejegyzéshez tartozó állomány már nem létezik!", "Üzenet", 0);
            }
         } catch (Exception var11) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A bejegyzéshez tartozó állomány azonosítása nem sikerült!", "Üzenet", 0);
         }

      }

      public void b21Clicked() {
      }

      public void b22PathClicked() {
         this.b14Clicked();
      }

      public void b23PathClicked() {
         this.b15Clicked();
      }

      public void b31Clicked() {
      }

      public void b32Clicked() {
         this.file_panel.getBusiness().saveFilterSettings("InstalledForms");
         InstalledForms.this.dlg.setVisible(false);
         this.file_panel.fireEvent(new CloseEvent(this.file_panel));
      }
   }
}
