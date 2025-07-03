package hu.piller.enykp.gui.framework;

import hu.piller.enykp.alogic.ebev.Ebev;
import hu.piller.enykp.alogic.ebev.EbevTools;
import hu.piller.enykp.alogic.ebev.SendParams;
import hu.piller.enykp.alogic.ebev.extendedsign.AttachmentListDialog;
import hu.piller.enykp.alogic.ebev.extendedsign.KrPreparation;
import hu.piller.enykp.alogic.fileloader.xml.XMLPost;
import hu.piller.enykp.alogic.filepanels.mohu.AVDHQuestionDialog;
import hu.piller.enykp.alogic.filepanels.mohu.MainMohuPanel;
import hu.piller.enykp.alogic.filepanels.mohu.OfficeMainMohuPanel;
import hu.piller.enykp.alogic.fileutil.FileStatusChecker;
import hu.piller.enykp.alogic.templateutils.blacklist.BlacklistStore;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.viewer.DefaultMultiFormViewer;
import hu.piller.enykp.interfaces.ICommandObject;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.icon.ENYKIconSet;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

public class CegkapuMenuItem {
   Action actCegkapuMarkToSend;
   Action actCegKapuUnmark;
   Action actCegKapuDirectSend;
   Action actCegGroup;
   Action actCegMulti;
   Action actCegMultiAvdh;
   Action actCegMultiAvdhRevoke;
   Action actCegKapuCrypt;
   Action actCegKapuCryptRevoke;
   Action actCegKapuSaveToSignWithOwnCert;
   Action actOwnCertRevoke;
   Action actCegKapuXCZ;
   Action actCegKapuXCZRevoke;
   Action actCegKapuDirectSendOnUK;
   Action actCegKapuGroupDirectSendOnUK;
   ICommandObject cegDirectSendMenuCmd;
   ICommandObject cegMarkCmd;
   ICommandObject cegUnmarkCmd;
   ICommandObject cegDirectCmd;
   ICommandObject cegGroupCmd;
   ICommandObject cegMultiCmd;
   ICommandObject cegMultiAvdhCmd;
   ICommandObject cegGroupMohuCmd;
   ICommandObject cegMultiAvdhBCmd;
   ICommandObject cegKRCmd;
   ICommandObject cegKRBCmd;
   ICommandObject cegOwnCmd;
   ICommandObject cegOwnBCmd;
   ICommandObject cegXCZCmd;
   ICommandObject cegXCZBCmd;
   private String menuCegKapu = "Kapcsolat a Cég/Hivatali kapuval";
   private String miCegkapuMarkToSend = "Nyomtatvány megjelölése elektronikus beküldésre";
   private String miCegKapuUnmark = "Nyomtatvány megjelölésének visszavonása";
   private String miCegKapuDirectSend = "Nyomtatvány közvetlen beküldése";
   private String miCegKapuGroupDirectSend = "Nyomtatvány csoportos közvetlen beküldése";
   private String miCegKapuMultiAVDH = "Többes aláírás AVDH szolgáltatással";
   private String miCegKapuAVDH = "Nyomtatvány hitelesítése AVDH szolgáltatással";
   private String miCegkapuAVDHRevoke = "Nyomtatvány hitelesítésének visszavonása";
   private String miCegKapuSaveToSignWithOwnCert = "Nyomtatvány mentése külső aláírással történő hitelesítéshez";
   private String miOwnCertRevoke = "Külső aláírással történő hitelesítés visszavonása";
   private String miCegKapuCrypt = "Nyomtatvány titkosítása elektronikus beküldésre";
   private String miCegKapuCryptRevoke = "Nyomtatvány titkosításának visszavonása";
   private String miCegKapuXCZ = "XCZ készítése fizikai adathordozón való benyújtáshoz";
   private String muCegKapuXCZRevoke = "XCZ készítés visszavonása";
   private Menubar menubar;

   public CegkapuMenuItem(Menubar var1) {
      this.menubar = var1;
   }

   public JMenu getMainMenuItem(Hashtable var1) {
      this.initActions();
      JMenu var3 = new JMenu(this.menuCegKapu);
      JMenuItem var2 = new JMenuItem(this.actCegkapuMarkToSend);
      var2.putClientProperty("code", this.miCegkapuMarkToSend);
      var1.put(this.cegMarkCmd, var2);
      var3.add(var2);
      var2 = new JMenuItem(this.actCegKapuUnmark);
      var2.putClientProperty("code", this.miCegKapuUnmark);
      var1.put(this.cegUnmarkCmd, var2);
      var3.add(var2);
      var3.add(new JSeparator());
      var2 = new JMenuItem(this.actCegKapuDirectSend);
      var2.putClientProperty("code", this.miCegKapuDirectSend);
      var1.put(this.cegDirectCmd, var2);
      var3.add(var2);
      var2 = new JMenuItem(this.actCegGroup);
      var2.putClientProperty("code", this.miCegKapuGroupDirectSend);
      var1.put(this.cegGroupCmd, var2);
      var3.add(var2);
      var3.add(new JSeparator());
      JMenu var4 = new JMenu(this.miCegKapuMultiAVDH);
      var4.setIcon(ENYKIconSet.getInstance().get("develop"));
      var2 = new JMenuItem(this.actCegMultiAvdh);
      var2.putClientProperty("code", this.miCegKapuAVDH);
      var1.put(this.cegMultiAvdhCmd, var2);
      var4.add(var2);
      var2 = new JMenuItem(this.actCegMultiAvdhRevoke);
      var2.putClientProperty("code", this.miCegkapuAVDHRevoke);
      var1.put(this.cegMultiAvdhBCmd, var2);
      var4.add(var2);
      var3.add(var4);
      var3.add(new JSeparator());
      var2 = new JMenuItem(this.actCegKapuSaveToSignWithOwnCert);
      var2.putClientProperty("code", this.miCegKapuSaveToSignWithOwnCert);
      var1.put(this.cegOwnCmd, var2);
      var3.add(var2);
      var2 = new JMenuItem(this.actOwnCertRevoke);
      var2.putClientProperty("code", this.miOwnCertRevoke);
      var1.put(this.cegOwnBCmd, var2);
      var3.add(var2);
      var3.add(new JSeparator());
      var2 = new JMenuItem(this.actCegKapuCrypt);
      var2.putClientProperty("code", this.miCegKapuCrypt);
      var1.put(this.cegKRCmd, var2);
      var3.add(var2);
      var2 = new JMenuItem(this.actCegKapuCryptRevoke);
      var2.putClientProperty("code", this.miCegKapuCryptRevoke);
      var1.put(this.cegKRBCmd, var2);
      var3.add(var2);
      var3.add(new JSeparator());
      var2 = new JMenuItem(this.actCegKapuXCZ);
      var2.putClientProperty("code", this.miCegKapuXCZ);
      var1.put(this.cegXCZCmd, var2);
      var3.add(var2);
      var2 = new JMenuItem(this.actCegKapuXCZRevoke);
      var2.putClientProperty("code", this.muCegKapuXCZRevoke);
      var1.put(this.cegXCZBCmd, var2);
      var3.add(var2);
      return var3;
   }

   private void initActions() {
      ENYKIconSet var1 = ENYKIconSet.getInstance();
      this.cegMarkCmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (var1.fv.pv.pv.leave_component()) {
                  Object[] var2 = new Object[]{"Igen", "Nem"};
                  BookModel var4 = var1.bm;
                  boolean var5 = true;
                  if (var4.isAvdhModel()) {
                     var5 = false;
                  }

                  int var3;
                  if (!var5) {
                     new AVDHQuestionDialog(MainFrame.thisinstance);
                     var3 = (Integer)PropertyList.getInstance().get("prop.dynamic.AVDHQuestionDialogAnswer");
                     PropertyList.getInstance().set("prop.dynamic.AVDHQuestionDialogAnswer", (Object)null);
                  } else {
                     var3 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Indulhat a nyomtatvány megjelölése?", "Kérdés", 0, 3, (Icon)null, var2, var2[0]);
                  }

                  if (var3 == 0) {
                     if (!var5 && PropertyList.getInstance().get("prop.dynamic.avdhWithNoAuth") == null) {
                        String var6 = EbevTools.checkIfFileHasAtc(var4);
                        if (var6 != null) {
                           GuiUtil.showMessageDialog(MainFrame.thisinstance, var6, "Üzenet", 1);
                           return;
                        }
                     }

                     try {
                        PropertyList.getInstance().set("prop.dynamic.avdh_direct_from_menu", "1");
                        Ebev var12 = new Ebev(var1.bm);
                        var12.mark();
                     } catch (Exception var10) {
                        var10.printStackTrace();
                     } finally {
                        PropertyList.getInstance().set("prop.dynamic.avdh_direct_from_menu", (Object)null);
                     }

                  }
               }
            }
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  if (XMLPost.xmleditnemjo) {
                     return Boolean.FALSE;
                  }

                  return this.fileStatusCheck();
               case 2:
               default:
                  break;
               case 3:
                  boolean var3;
                  try {
                     var3 = MainFrame.thisinstance.mp.getDMFV().bm.isDisabledTemplate();
                  } catch (Exception var5) {
                     var3 = false;
                  }

                  if (var3) {
                     return Boolean.FALSE;
                  }

                  return !MainFrame.thisinstance.mp.funcreadonly ? this.fileStatusCheck() : Boolean.FALSE;
               }
            } catch (Exception var6) {
            }

            return Boolean.FALSE;
         }

         private Boolean fileStatusCheck() {
            FileStatusChecker var1 = FileStatusChecker.getInstance();

            try {
               int var2 = var1.getStatus(MainFrame.thisinstance.mp.getDMFV().bm.cc.getLoadedfile().getAbsolutePath(), (String)((String)(MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.containsKey("krfilename") ? MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.get("krfilename") : "")));
               return var2 == 0;
            } catch (Exception var3) {
               return Boolean.TRUE;
            }
         }

         public void setParameters(Hashtable var1) {
         }
      };
      this.cegUnmarkCmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (!var1.fv.pv.pv.leave_component()) {
                  return;
               }

               BookModel var2 = var1.bm;

               try {
                  Ebev var3 = new Ebev(var2);
                  var3.unmark(true);
               } catch (Exception var4) {
                  var4.printStackTrace();
               }
            }

         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  if (XMLPost.xmleditnemjo) {
                     return Boolean.FALSE;
                  }

                  return this.fileStatusCheck();
               case 2:
               default:
                  break;
               case 3:
                  return !MainFrame.thisinstance.mp.funcreadonly ? this.fileStatusCheck() : Boolean.FALSE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }

         private Boolean fileStatusCheck() {
            FileStatusChecker var1 = FileStatusChecker.getInstance();

            try {
               return new Boolean(var1.getStatus(MainFrame.thisinstance.mp.getDMFV().bm.cc.getLoadedfile().getAbsolutePath(), (String)((String)(MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.containsKey("krfilename") ? MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.get("krfilename") : ""))) == 1);
            } catch (Exception var3) {
               return Boolean.FALSE;
            }
         }

         public void setParameters(Hashtable var1) {
         }
      };
      this.cegDirectSendMenuCmd = new ICommandObject() {
         public void execute() {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.TRUE;
               case 1:
                  return Boolean.FALSE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }

         public void setParameters(Hashtable var1) {
         }
      };
      this.cegDirectCmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (!var1.fv.pv.pv.leave_component()) {
                  return;
               }

               BookModel var2 = var1.bm;
               if (BlacklistStore.getInstance().handleGuiMessage(var2.getTemplateId(), var2.getOrgId())) {
                  return;
               }

               try {
                  Ebev var3 = new Ebev(var2);
                  var3.post(true);
               } catch (Exception var4) {
                  var4.printStackTrace();
               }
            }

         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  if (XMLPost.xmleditnemjo) {
                     return Boolean.FALSE;
                  }

                  return this.fileStatusCheck();
               case 2:
               default:
                  break;
               case 3:
                  return !MainFrame.thisinstance.mp.funcreadonly ? this.fileStatusCheck() : Boolean.FALSE;
               }
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }

         private Boolean fileStatusCheck() {
            FileStatusChecker var1 = FileStatusChecker.getInstance();

            try {
               return var1.getStatus(MainFrame.thisinstance.mp.getDMFV().bm.cc.getLoadedfile().getAbsolutePath(), (String)((String)(MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.containsKey("krfilename") ? MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.get("krfilename") : ""))) == 1;
            } catch (Exception var3) {
               return Boolean.FALSE;
            }
         }

         public void setParameters(Hashtable var1) {
         }
      };
      this.cegGroupMohuCmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (!var1.fv.pv.pv.leave_component()) {
                  return;
               }

               if (!var1.bm.isCegkapuModel()) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Ez a funkció nem hajtható végre ezen a nyomtatványtípuson!", "Üzenet", 0);
                  return;
               }
            }

            new MainMohuPanel();
         }

         public void setParameters(Hashtable var1) {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.TRUE;
               case 1:
                  return Boolean.FALSE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }
      };
      this.cegGroupCmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (!var1.fv.pv.pv.leave_component()) {
                  return;
               }

               if (!var1.bm.isCegkapuModel()) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Ez a funkció nem hajtható végre ezen a nyomtatványtípuson!", "Üzenet", 0);
                  return;
               }
            }

            new OfficeMainMohuPanel();
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.TRUE;
               case 1:
                  return Boolean.FALSE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }

         public void setParameters(Hashtable var1) {
         }
      };
      this.cegMultiCmd = new ICommandObject() {
         public void execute() {
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.TRUE;
               case 1:
                  return Boolean.FALSE;
               case 2:
               default:
                  break;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }

         public void setParameters(Hashtable var1) {
         }
      };
      this.cegMultiAvdhCmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (!var1.fv.pv.pv.leave_component()) {
                  return;
               }

               if (!var1.bm.isCegkapuModel()) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Ez a funkció nem hajtható végre ezen a nyomtatványtípuson!", "Üzenet", 0);
                  return;
               }

               if (CegkapuMenuItem.this.menubar.save(var1.bm)) {
                  new AttachmentListDialog(MainFrame.thisinstance, var1.bm, 0);
               }
            }

         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  if (XMLPost.xmleditnemjo) {
                     return Boolean.FALSE;
                  }

                  return this.fileStatusCheck();
               case 2:
               default:
                  break;
               case 3:
                  boolean var3;
                  try {
                     var3 = MainFrame.thisinstance.mp.getDMFV().bm.isDisabledTemplate();
                  } catch (Exception var5) {
                     var3 = false;
                  }

                  if (var3) {
                     return Boolean.FALSE;
                  }

                  return !MainFrame.thisinstance.mp.funcreadonly ? this.fileStatusCheck() : Boolean.FALSE;
               }
            } catch (Exception var6) {
               Tools.eLog(var6, 0);
            }

            return Boolean.FALSE;
         }

         private Boolean fileStatusCheck() {
            FileStatusChecker var1 = FileStatusChecker.getInstance();

            try {
               int var2 = var1.getStatus(MainFrame.thisinstance.mp.getDMFV().bm.cc.getLoadedfile().getAbsolutePath(), (String)((String)(MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.containsKey("krfilename") ? MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.get("krfilename") : "")));
               return var2 == 0 || var2 == 4;
            } catch (Exception var3) {
               return Boolean.TRUE;
            }
         }

         public void setParameters(Hashtable var1) {
         }
      };
      this.cegMultiAvdhBCmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (!var1.fv.pv.pv.leave_component()) {
                  return;
               }

               if (!var1.bm.isCegkapuModel()) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Ez a funkció nem hajtható végre ezen a nyomtatványtípuson!", "Üzenet", 0);
                  return;
               }

               if (CegkapuMenuItem.this.menubar.save(var1.bm)) {
                  new AttachmentListDialog(MainFrame.thisinstance, var1.bm, 1);
               }
            }

         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  if (XMLPost.xmleditnemjo) {
                     return Boolean.FALSE;
                  }

                  return this.fileStatusCheck();
               case 2:
               default:
                  break;
               case 3:
                  return !MainFrame.thisinstance.mp.funcreadonly ? this.fileStatusCheck() : Boolean.FALSE;
               }
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

            return Boolean.FALSE;
         }

         private Boolean fileStatusCheck() {
            FileStatusChecker var1 = FileStatusChecker.getInstance();

            try {
               return var1.getStatus(MainFrame.thisinstance.mp.getDMFV().bm.cc.getLoadedfile().getAbsolutePath(), (String)((String)(MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.containsKey("krfilename") ? MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.get("krfilename") : ""))) == 4;
            } catch (Exception var3) {
               return Boolean.FALSE;
            }
         }

         public void setParameters(Hashtable var1) {
         }
      };
      this.cegKRCmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (var1.fv.pv.pv.leave_component()) {
                  if (!var1.bm.isCegkapuModel()) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "Ez a funkció nem hajtható végre ezen a nyomtatványtípuson!", "Üzenet", 0);
                  } else {
                     try {
                        SendParams var2 = new SendParams(PropertyList.getInstance());
                        String var3 = var1.bm.cc.getLoadedfile().getName();
                        var3 = var3.substring(0, var3.length() - ".frm.enyk".length());
                        File var4 = new File(var2.srcPath + var3 + File.separator + "alairt");
                        if (!var4.exists()) {
                           GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nincs meg az aláírt nyomtatvány kivonatot tartalmazó mappa:\n" + var2.srcPath + var3 + File.separator + "alairt", "Hiba", 0);
                           return;
                        }

                        int var5 = var4.list().length;
                        if (var5 == 0) {
                           GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nincs meg az aláírt nyomtatvány kivonatot tartalmazó fájl az alábbi mappában:\n" + var2.srcPath + var3 + File.separator + "alairt", "Hiba", 0);
                        }

                        if (var5 > 1) {
                           GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem egyértelmű, hogy melyik a nyomtatvány kivonatot tartalmazó fájl az alábbi mappában:\n" + var2.srcPath + var3 + File.separator + "alairt", "Hiba", 0);
                        }

                        if (var5 != 1) {
                           return;
                        }

                        PropertyList.getInstance().set("prop.dynamic.signWithExternalTool", true);
                        Ebev var6 = new Ebev(var1.bm);
                        var6.mark();
                     } catch (Exception var10) {
                        var10.printStackTrace();
                     } finally {
                        PropertyList.getInstance().set("prop.dynamic.signWithExternalTool", (Object)null);
                     }

                  }
               }
            }
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  if (XMLPost.xmleditnemjo) {
                     return Boolean.FALSE;
                  }

                  return this.fileStatusCheck();
               case 2:
               default:
                  break;
               case 3:
                  return !MainFrame.thisinstance.mp.funcreadonly ? this.fileStatusCheck() : Boolean.FALSE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }

         private Boolean fileStatusCheck() {
            FileStatusChecker var1 = FileStatusChecker.getInstance();

            try {
               int var2 = var1.getStatus(MainFrame.thisinstance.mp.getDMFV().bm.cc.getLoadedfile().getAbsolutePath(), (String)((String)(MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.containsKey("krfilename") ? MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.get("krfilename") : "")));
               return var2 == 4 || var2 == 3;
            } catch (Exception var3) {
               return Boolean.FALSE;
            }
         }

         public void setParameters(Hashtable var1) {
         }
      };
      this.cegKRBCmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (!var1.fv.pv.pv.leave_component()) {
                  return;
               }

               if (!var1.bm.isCegkapuModel()) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Ez a funkció nem hajtható végre ezen a nyomtatványtípuson!", "Üzenet", 0);
                  return;
               }

               try {
                  Ebev var2 = new Ebev(var1.bm);
                  var2.unmark(false);
               } catch (Exception var3) {
                  var3.printStackTrace();
               }
            }

         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  if (XMLPost.xmleditnemjo) {
                     return Boolean.FALSE;
                  }

                  return this.fileStatusCheck();
               case 2:
               default:
                  break;
               case 3:
                  return !MainFrame.thisinstance.mp.funcreadonly ? this.fileStatusCheck() : Boolean.FALSE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }

         private Boolean fileStatusCheck() {
            FileStatusChecker var1 = FileStatusChecker.getInstance();

            try {
               int var2 = var1.getStatus(MainFrame.thisinstance.mp.getDMFV().bm.cc.getLoadedfile().getAbsolutePath(), (String)((String)(MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.containsKey("krfilename") ? MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.get("krfilename") : "")));
               if (var2 != 1) {
                  return false;
               } else {
                  String var3 = MainFrame.thisinstance.mp.getDMFV().bm.cc.getLoadedfile().getName();
                  var3 = var3.substring(0, var3.length() - ".frm.enyk".length());
                  int var4 = var1.getExtendedFileInfo(var3);
                  return var4 == 0 || var4 == 3 || var4 == 4;
               }
            } catch (Exception var5) {
               return Boolean.FALSE;
            }
         }

         public void setParameters(Hashtable var1) {
         }
      };
      this.cegOwnCmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (var1.fv.pv.pv.leave_component()) {
                  if (!var1.bm.isCegkapuModel()) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "Ez a funkció nem hajtható végre ezen a nyomtatványtípuson!", "Üzenet", 0);
                  } else {
                     try {
                        KrPreparation var2 = new KrPreparation(var1.bm);
                        if (var2.external()) {
                           CegkapuMenuItem.this.menubar.setState(MainPanel.READONLY);
                           MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("Külső aláírásra átadva");
                           MainFrame.thisinstance.mp.setReadonly(true);
                        }
                     } catch (Exception var3) {
                        var3.printStackTrace();
                     }

                  }
               }
            }
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  if (XMLPost.xmleditnemjo) {
                     return Boolean.FALSE;
                  }

                  return this.fileStatusCheck();
               case 2:
               default:
                  break;
               case 3:
                  return !MainFrame.thisinstance.mp.funcreadonly ? this.fileStatusCheck() : Boolean.FALSE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }

         private Boolean fileStatusCheck() {
            FileStatusChecker var1 = FileStatusChecker.getInstance();

            try {
               return new Boolean(var1.getStatus(MainFrame.thisinstance.mp.getDMFV().bm.cc.getLoadedfile().getAbsolutePath(), (String)((String)(MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.containsKey("krfilename") ? MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.get("krfilename") : ""))) == 0);
            } catch (Exception var3) {
               return Boolean.TRUE;
            }
         }

         public void setParameters(Hashtable var1) {
         }
      };
      this.cegOwnBCmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (!var1.fv.pv.pv.leave_component()) {
                  return;
               }

               if (!var1.bm.isCegkapuModel()) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Ez a funkció nem hajtható végre ezen a nyomtatványtípuson!", "Üzenet", 0);
                  return;
               }

               boolean var2 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Visszavonja a nyomtatvány aláírását?\nAmennyiben külső aláíró programmal írt alá, a visszavonás mindenki aláírására vonatkozik! (Az aláírt csomag törlését jelenti)", "Aláírások visszavonása", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0;
               if (var2) {
                  try {
                     KrPreparation var3 = new KrPreparation(var1.bm);
                     var3.reset();
                     CegkapuMenuItem.this.menubar.setState(MainPanel.NORMAL);
                     MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("Módosítható");
                     MainFrame.thisinstance.mp.setReadonly(false);
                     Ebev.log(15, var1.bm.cc.getLoadedfile());
                  } catch (Exception var4) {
                     var4.printStackTrace();
                  }
               }
            }

         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  if (XMLPost.xmleditnemjo) {
                     return Boolean.FALSE;
                  }

                  return this.fileStatusCheck();
               case 2:
               default:
                  break;
               case 3:
                  return !MainFrame.thisinstance.mp.funcreadonly ? this.fileStatusCheck() : Boolean.FALSE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }

         private Boolean fileStatusCheck() {
            FileStatusChecker var1 = FileStatusChecker.getInstance();

            try {
               return new Boolean(var1.getStatus(MainFrame.thisinstance.mp.getDMFV().bm.cc.getLoadedfile().getAbsolutePath(), (String)((String)(MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.containsKey("krfilename") ? MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.get("krfilename") : ""))) == 3);
            } catch (Exception var3) {
               return Boolean.FALSE;
            }
         }

         public void setParameters(Hashtable var1) {
         }
      };
      this.cegXCZCmd = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (!var1.fv.pv.pv.leave_component()) {
                  return;
               }

               if (!var1.bm.isCegkapuModel()) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Ez a funkció nem hajtható végre ezen a nyomtatványtípuson!", "Üzenet", 0);
                  return;
               }

               if (CegkapuMenuItem.this.menubar.save(var1.bm)) {
                  new AttachmentListDialog(MainFrame.thisinstance, var1.bm, 2);
               }
            }

         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  if (XMLPost.xmleditnemjo) {
                     return Boolean.FALSE;
                  }

                  return this.fileStatusCheck();
               case 2:
               default:
                  break;
               case 3:
                  return !MainFrame.thisinstance.mp.funcreadonly ? this.fileStatusCheck() : Boolean.FALSE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }

         private Boolean fileStatusCheck() {
            FileStatusChecker var1 = FileStatusChecker.getInstance();

            try {
               int var2 = var1.getStatus(MainFrame.thisinstance.mp.getDMFV().bm.cc.getLoadedfile().getAbsolutePath(), (String)((String)(MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.containsKey("krfilename") ? MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.get("krfilename") : "")));
               return var2 == 4 || var2 == 3;
            } catch (Exception var3) {
               return Boolean.FALSE;
            }
         }

         public void setParameters(Hashtable var1) {
         }
      };
      this.cegXCZBCmd = new ICommandObject() {
         FileStatusChecker fsc = FileStatusChecker.getInstance();

         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (var1.fv.pv.pv.leave_component()) {
                  if (!var1.bm.isCegkapuModel()) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "Ez a funkció nem hajtható végre ezen a nyomtatványtípuson!", "Üzenet", 0);
                  } else {
                     try {
                        KrPreparation var2 = new KrPreparation(var1.bm);
                        GuiUtil.showMessageDialog(MainFrame.thisinstance, var2.resetXCZ(), "Üzenet", 1);
                        String var3 = this.fsc.getExtendedFileState(MainFrame.thisinstance.mp.getDMFV().bm.cc.getLoadedfile(), (String)((String)(MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.containsKey("krfilename") ? MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.get("krfilename") : "")));
                        if ("Módosítható".equals(var3)) {
                           CegkapuMenuItem.this.menubar.setState(MainPanel.NORMAL);
                           MainFrame.thisinstance.mp.setReadonly(false);
                        } else {
                           CegkapuMenuItem.this.menubar.setState(MainPanel.READONLY);
                           MainFrame.thisinstance.mp.setReadonly(true);
                        }

                        MainFrame.thisinstance.mp.getStatuspanel().statusname.setText(this.fsc.getExtendedFileState(MainFrame.thisinstance.mp.getDMFV().bm.cc.getLoadedfile(), (String)((String)(MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.containsKey("krfilename") ? MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.get("krfilename") : ""))));
                        Ebev.log(16, var1.bm.cc.getLoadedfile());
                     } catch (Exception var4) {
                        var4.printStackTrace();
                     }

                  }
               }
            }
         }

         public Object getState(Object var1) {
            try {
               int var2 = (Integer)var1;
               switch(var2) {
               case 0:
                  return Boolean.FALSE;
               case 1:
                  if (XMLPost.xmleditnemjo) {
                     return Boolean.FALSE;
                  }

                  return this.fileStatusCheck();
               case 2:
               default:
                  break;
               case 3:
                  return !MainFrame.thisinstance.mp.funcreadonly ? this.fileStatusCheck() : Boolean.FALSE;
               }
            } catch (Exception var3) {
            }

            return Boolean.FALSE;
         }

         private Boolean fileStatusCheck() {
            try {
               int var1 = this.fsc.getStatus(MainFrame.thisinstance.mp.getDMFV().bm.cc.getLoadedfile().getAbsolutePath(), (String)((String)(MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.containsKey("krfilename") ? MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.get("krfilename") : "")));
               return new Boolean(var1 == 5);
            } catch (Exception var2) {
               return Boolean.FALSE;
            }
         }

         public void setParameters(Hashtable var1) {
         }
      };
      this.actCegkapuMarkToSend = new AbstractAction(this.miCegkapuMarkToSend, var1.get(this.miCegkapuMarkToSend)) {
         public void actionPerformed(ActionEvent var1) {
            CegkapuMenuItem.this.cegMarkCmd.execute();
         }
      };
      this.actCegKapuUnmark = new AbstractAction(this.miCegKapuUnmark, var1.get(this.miCegKapuUnmark)) {
         public void actionPerformed(ActionEvent var1) {
            CegkapuMenuItem.this.cegUnmarkCmd.execute();
         }
      };
      this.actCegKapuDirectSend = new AbstractAction(this.miCegKapuDirectSend, var1.get(this.miCegKapuDirectSend)) {
         public void actionPerformed(ActionEvent var1) {
            CegkapuMenuItem.this.cegDirectCmd.execute();
         }
      };
      this.actCegGroup = new AbstractAction(this.miCegKapuGroupDirectSend, var1.get(this.miCegKapuGroupDirectSend)) {
         public void actionPerformed(ActionEvent var1) {
            CegkapuMenuItem.this.cegGroupCmd.execute();
         }
      };
      this.actCegMulti = new AbstractAction(this.miCegKapuMultiAVDH, var1.get(this.miCegKapuMultiAVDH)) {
         public void actionPerformed(ActionEvent var1) {
            CegkapuMenuItem.this.cegMultiCmd.execute();
         }
      };
      this.actCegMultiAvdh = new AbstractAction(this.miCegKapuAVDH, var1.get(this.miCegKapuAVDH)) {
         public void actionPerformed(ActionEvent var1) {
            CegkapuMenuItem.this.cegMultiAvdhCmd.execute();
         }
      };
      this.actCegMultiAvdhRevoke = new AbstractAction(this.miCegkapuAVDHRevoke, var1.get(this.miCegkapuAVDHRevoke)) {
         public void actionPerformed(ActionEvent var1) {
            CegkapuMenuItem.this.cegMultiAvdhBCmd.execute();
         }
      };
      this.actCegKapuCrypt = new AbstractAction(this.miCegKapuCrypt, var1.get(this.miCegKapuCrypt)) {
         public void actionPerformed(ActionEvent var1) {
            CegkapuMenuItem.this.cegKRCmd.execute();
         }
      };
      this.actCegKapuCryptRevoke = new AbstractAction(this.miCegKapuCryptRevoke, var1.get(this.miCegKapuCryptRevoke)) {
         public void actionPerformed(ActionEvent var1) {
            CegkapuMenuItem.this.cegKRBCmd.execute();
         }
      };
      this.actCegKapuSaveToSignWithOwnCert = new AbstractAction(this.miCegKapuSaveToSignWithOwnCert, var1.get(this.miCegKapuSaveToSignWithOwnCert)) {
         public void actionPerformed(ActionEvent var1) {
            CegkapuMenuItem.this.cegOwnCmd.execute();
         }
      };
      this.actOwnCertRevoke = new AbstractAction(this.miOwnCertRevoke, var1.get(this.miOwnCertRevoke)) {
         public void actionPerformed(ActionEvent var1) {
            CegkapuMenuItem.this.cegOwnBCmd.execute();
         }
      };
      this.actCegKapuXCZ = new AbstractAction(this.miCegKapuXCZ, var1.get(this.miCegKapuXCZ)) {
         public void actionPerformed(ActionEvent var1) {
            CegkapuMenuItem.this.cegXCZCmd.execute();
         }
      };
      this.actCegKapuXCZRevoke = new AbstractAction(this.muCegKapuXCZRevoke, var1.get(this.muCegKapuXCZRevoke)) {
         public void actionPerformed(ActionEvent var1) {
            CegkapuMenuItem.this.cegXCZBCmd.execute();
         }
      };
   }
}
