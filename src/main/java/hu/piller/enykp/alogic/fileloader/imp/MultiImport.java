package hu.piller.enykp.alogic.fileloader.imp;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.settingspanel.BaseSettingsPane;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.framework.MainPanel;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.viewer.DefaultMultiFormViewer;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import me.necrocore.abevjava.NecroFile;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Vector;

public class MultiImport {
   Vector mfiles = new Vector();
   String parent;
   Vector errlist = new Vector();

   public void display(String var1) {
      File var2 = new NecroFile(var1);
      this.parent = var2.getParent();
      this.mfiles.clear();
      this.errlist.clear();
      String var3 = this.get1row(var2);
      if (var3 != null && !"".equals(var3)) {
         File var4 = new NecroFile(var3);
         int var5;
         if (!var4.exists()) {
            var4 = new NecroFile(this.parent, var3);
            if (!var4.exists()) {
               this.errlist.add(var3);
               this.errlist.add(new TextWithIcon("    Nem található ilyen állomány!", 2));

               for(var5 = 0; var5 < this.mfiles.size(); ++var5) {
                  var4 = (File)this.mfiles.get(var5);
                  if (var4.exists()) {
                     this.mfiles.remove(var5);
                     break;
                  }

                  this.errlist.add(this.mfiles.get(var5));
                  this.errlist.add(new TextWithIcon("    Nem található ilyen állomány!", 2));
                  this.mfiles.remove(var5);
               }
            }
         }

         var5 = this.mfiles.size();
         if (var5 == 0) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A lista állomány nem tartalmaz elérhető állományt!", "Üzenet", 0);
         } else {
            MainFrame.thisinstance.setGlassLabel("Betöltés folyamatban!");
            File finalVar = var4;
            Thread var7 = new Thread(new Runnable() {
               public void run() {
                  BookModel var1 = null;

                  MainPanel var10001;
                  try {
                     ImpLoader var2 = new ImpLoader();
                     Hashtable var3 = null;

                     try {
                        var3 = var2.getHeadData(finalVar);
                        var3 = (Hashtable)var3.get("docinfo");
                     } catch (Exception var13) {
                        var3 = new Hashtable();
                        var3.put("id", "");
                        var3.put("ver", "");
                        var3.put("org", "");
                     }

                     String var4x = (String)var3.get("id");
                     String var5 = (String)var3.get("ver");
                     String var6 = (String)var3.get("org");
                     String var7 = "Módosítható";
                     var1 = var2.multiload(finalVar.getAbsolutePath(), var4x, var5, var6, MultiImport.this.mfiles, MultiImport.this.errlist);
                     if (!var1.hasError) {
                        var1.setPAInfo((Object)null, (Object)null);
                        int var8 = 1;

                        for(int var9 = 0; var9 < MultiImport.this.mfiles.size(); ++var9) {
                           if (!((File)MultiImport.this.mfiles.get(var9)).exists()) {
                              MultiImport.this.errlist.add(MultiImport.this.mfiles.get(var9));
                              MultiImport.this.errlist.add(new TextWithIcon("    Nem található ilyen állomány!", 2));
                           } else {
                              String var10 = var2.loadIntoBm(MultiImport.this.mfiles.get(var9).toString(), var1);
                              boolean var11 = false;
                              if (var1.impwarninglist != null) {
                                 var11 = true;
                                 MultiImport.this.errlist.add(MultiImport.this.mfiles.get(var9));

                                 for(int var12 = 0; var12 < var1.impwarninglist.size(); ++var12) {
                                    MultiImport.this.errlist.add(new TextWithIcon("    " + var1.impwarninglist.get(var12), 4));
                                 }

                                 var1.impwarninglist = null;
                              }

                              if (var10 != null) {
                                 if (!var11) {
                                    MultiImport.this.errlist.add(MultiImport.this.mfiles.get(var9));
                                 }

                                 MultiImport.this.errlist.add(new TextWithIcon("    " + var10, 0));
                              } else {
                                 ++var8;
                                 if (!var11) {
                                    MultiImport.this.errlist.add(MultiImport.this.mfiles.get(var9));
                                 }

                                 MultiImport.this.errlist.add(new TextWithIcon("    Sikeresen hozzáadva!", 3));
                              }

                              MainFrame.thisinstance.setGlassLabel("Betöltés folyamatban! ( " + (var9 + 2) + " )");
                           }
                        }

                        MultiImport.this.errlist.add(new TextWithIcon("--------------------------------", -1));
                        MultiImport.this.errlist.add(new TextWithIcon("  " + var8 + " db. sikeresen hozzáadva!", -1));
                        if (var1.isDisabledTemplate()) {
                           String[] var15 = var1.errormsg.split(" bl_url ");
                           MultiImport.this.errlist.add(new TextWithIcon(var15[0], -1));
                           if (var15.length > 1) {
                              MultiImport.this.errlist.add(new TextWithIcon(var15[1], -1));
                           }
                        }

                        if (MultiImport.this.errlist.size() != 0) {
                           GuiUtil.showErrorDialog((Frame)null, "Eredmény lista", true, true, MultiImport.this.errlist);
                        }

                        CalculatorManager.getInstance().doBetoltErtekCalcs(false);
                        CalculatorManager.getInstance().multiform_calc();
                        DefaultMultiFormViewer var16 = new DefaultMultiFormViewer(var1);
                        if (var16.fv.getTp().getTabCount() == 0) {
                           var1.destroy();
                           var10001 = MainFrame.thisinstance.mp;
                           MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                           MainFrame.thisinstance.setGlassLabel((String)null);
                           MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                           MainFrame.thisinstance.mp.getStatuspanel().currentpagename.setText("");
                           MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                           return;
                        }

                        var1.dirty = true;
                        MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(var7);
                        MainFrame.thisinstance.mp.intoleftside(var16);
                        if (var1.isDisabledTemplate()) {
                           TemplateUtils.getInstance().handleDisabledTemplates(var1.getTemplateId(), var1.getOrgId());
                        } else {
                           var10001 = MainFrame.thisinstance.mp;
                           MainFrame.thisinstance.mp.setstate(MainPanel.NORMAL);
                        }

                        MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("Ny:" + (String)var1.docinfo.get("ver"));
                        BaseSettingsPane.done_menuextratext(true);
                        MainFrame.thisinstance.setGlassLabel((String)null);
                     } else {
                        GuiUtil.showMessageDialog(MainFrame.thisinstance, var1.errormsg, "Hibaüzenet", 0);
                        var1.destroy();
                        MainFrame.thisinstance.setGlassLabel((String)null);
                        MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                        MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                        var10001 = MainFrame.thisinstance.mp;
                        MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                        MainFrame.thisinstance.mp.set_kiut_url((String)null);
                     }
                  } catch (Exception var14) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, var14.getMessage(), "Hibaüzenet", 0);
                     if (var1 != null) {
                        var1.destroy();
                     }

                     MainFrame.thisinstance.setGlassLabel((String)null);
                     MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("");
                     MainFrame.thisinstance.mp.getStatuspanel().formversion.setText("");
                     var10001 = MainFrame.thisinstance.mp;
                     MainFrame.thisinstance.mp.setstate(MainPanel.EMPTY);
                     MainFrame.thisinstance.mp.set_kiut_url((String)null);
                  }

               }
            });
            var7.start();
         }
      } else {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A lista állomány nem tartalmaz adatot!", "Üzenet", 0);
      }
   }

   private String get1row(File var1) {
      try {
         BufferedReader var2 = new BufferedReader(new FileReader(var1));
         String var3 = null;

         do {
            var3 = var2.readLine();
            if (var3 == null) {
               return null;
            }
         } while(var3.trim().length() == 0 || var3.startsWith(";"));

         while(true) {
            String var4 = var2.readLine();
            if (var4 == null) {
               var2.close();
               return var3;
            }

            if (!var4.startsWith(";") && var4.trim().length() != 0) {
               File var5 = new NecroFile(var4);
               if (var5.exists()) {
                  this.mfiles.add(var5);
               } else {
                  this.mfiles.add(new NecroFile(this.parent, var4));
               }
            }
         }
      } catch (Exception var6) {
         return null;
      }
   }
}
