package hu.piller.enykp.gui.framework;

import hu.piller.enykp.alogic.ebev.Ebev;
import hu.piller.enykp.alogic.ebev.EbevTools;
import hu.piller.enykp.alogic.fileloader.xml.XMLPost;
import hu.piller.enykp.alogic.filepanels.mohu.AVDHQuestionDialog;
import hu.piller.enykp.alogic.filepanels.mohu.MainMohuPanel;
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
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

public class UgyfelkapuMenuItem {
   Action actUKPassToDsign;
   Action actUKMarkToSend;
   Action actUKMarkToSendRevoke;
   Action actUKDirectSend;
   Action actUKDirectGroupSend;
   ICommandObject cmdUKPassToDsign;
   ICommandObject cmdUKDirectGroupSend;
   ICommandObject cmdUKMarkToSend;
   ICommandObject cmdUKMarkToSendRevoke;
   ICommandObject cmdUKDirectSend;
   private Menubar menubar;

   public UgyfelkapuMenuItem(Menubar var1) {
      this.menubar = var1;
   }

   public JMenu getMainMenuItem(Hashtable var1) {
      this.initActions();
      String var4 = "Kapcsolat a magánszemély tárhelyével";
      JMenu var5 = new JMenu(var4);
      JMenuItem var3 = new JMenuItem(this.actUKMarkToSend);
      var3.putClientProperty("code", "toebev");
      var1.put(this.cmdUKMarkToSend, var3);
      var5.add(var3);
      var3 = new JMenuItem(this.actUKMarkToSendRevoke);
      var3.putClientProperty("code", "fromebev");
      var1.put(this.cmdUKMarkToSendRevoke, var3);
      var5.add(var3);
      var5.add(new JSeparator());
      var3 = new JMenuItem(this.actUKDirectSend);
      var3.putClientProperty("code", "mohu");
      KeyStroke var2 = KeyStroke.getKeyStroke(75, 2);
      var3.setAccelerator(var2);
      var1.put(this.cmdUKDirectSend, var3);
      var5.add(var3);
      var3 = new JMenuItem(this.actUKDirectGroupSend);
      var3.putClientProperty("code", "groupsend");
      var1.put(this.cmdUKDirectGroupSend, var3);
      var5.add(var3);
      var5.add(new JSeparator());
      var3 = new JMenuItem(this.actUKPassToDsign);
      var3.putClientProperty("code", "sign");
      var1.put(this.cmdUKPassToDsign, var3);
      var5.add(var3);
      return var5;
   }

   private void initActions() {
      ENYKIconSet var1 = ENYKIconSet.getInstance();
      String var2 = "Nyomtatvány megjelölése elektronikus beküldésre";
      this.actUKMarkToSend = new AbstractAction(var2, var1.get("toebev")) {
         public void actionPerformed(ActionEvent var1) {
            UgyfelkapuMenuItem.this.cmdUKMarkToSend.execute();
         }
      };
      this.cmdUKMarkToSend = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (!var1.fv.pv.pv.leave_component()) {
                  return;
               }

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

               if (var3 != 0) {
                  return;
               }

               if (!var5 && PropertyList.getInstance().get("prop.dynamic.avdhWithNoAuth") == null) {
                  String var6 = EbevTools.checkIfFileHasAtc(var4);
                  if (var6 != null) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, var6, "Üzenet", 1);
                     return;
                  }
               }

               try {
                  PropertyList.getInstance().set("prop.dynamic.avdh_direct_from_menu", "1");
                  Ebev var12 = new Ebev(var4);
                  var12.mark();
               } catch (Exception var10) {
                  var10.printStackTrace();
               } finally {
                  PropertyList.getInstance().set("prop.dynamic.avdh_direct_from_menu", (Object)null);
               }
            }

         }

         public void setParameters(Hashtable var1) {
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
                  } else {
                     if (UgyfelkapuMenuItem.this.menubar.isAutoFillMode()) {
                        return Boolean.FALSE;
                     }

                     return this.fileStatusCheck();
                  }
               case 2:
               default:
                  return Boolean.FALSE;
               case 3:
                  boolean var3;
                  try {
                     var3 = MainFrame.thisinstance.mp.getDMFV().bm.isDisabledTemplate();
                  } catch (Exception var5) {
                     var3 = false;
                  }

                  if (var3) {
                     return Boolean.FALSE;
                  } else {
                     return !MainFrame.thisinstance.mp.funcreadonly ? this.fileStatusCheck() : Boolean.FALSE;
                  }
               }
            } catch (Exception var6) {
               return Boolean.FALSE;
            }
         }

         private Boolean fileStatusCheck() {
            if (MainFrame.thisinstance.mp.getDMFV().bm.cc.getLoadedfile() == null) {
               return true;
            } else {
               FileStatusChecker var1 = FileStatusChecker.getInstance();

               try {
                  int var2 = var1.getStatus(MainFrame.thisinstance.mp.getDMFV().bm.cc.getLoadedfile().getAbsolutePath(), (String)((String)(MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.containsKey("krfilename") ? MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.get("krfilename") : "")));
                  return var2 == 0;
               } catch (Exception var3) {
                  return Boolean.FALSE;
               }
            }
         }
      };
      String var3 = "Nyomtatvány megjelölésének visszavonása";
      this.actUKMarkToSendRevoke = new AbstractAction(var3, var1.get("fromebev")) {
         public void actionPerformed(ActionEvent var1) {
            UgyfelkapuMenuItem.this.cmdUKMarkToSendRevoke.execute();
         }
      };
      this.cmdUKMarkToSendRevoke = new ICommandObject() {
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

         public void setParameters(Hashtable var1) {
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
                  return Boolean.FALSE;
               case 3:
                  return !MainFrame.thisinstance.mp.funcreadonly ? this.fileStatusCheck() : Boolean.FALSE;
               }
            } catch (Exception var3) {
               return Boolean.FALSE;
            }
         }

         private Boolean fileStatusCheck() {
            FileStatusChecker var1 = FileStatusChecker.getInstance();

            try {
               return var1.getStatus(MainFrame.thisinstance.mp.getDMFV().bm.cc.getLoadedfile().getAbsolutePath(), (String)((String)(MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.containsKey("krfilename") ? MainFrame.thisinstance.mp.getDMFV().bm.cc.docinfo.get("krfilename") : ""))) == 1;
            } catch (Exception var3) {
               return Boolean.FALSE;
            }
         }
      };
      String var4 = "Nyomtatvány közvetlen beküldése a magánszemély tárhelyén keresztül";
      this.actUKDirectSend = new AbstractAction(var4, var1.get("goto")) {
         public void actionPerformed(ActionEvent var1) {
            UgyfelkapuMenuItem.this.cmdUKDirectSend.execute();
         }
      };
      this.cmdUKDirectSend = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (!var1.fv.pv.pv.leave_component()) {
                  return;
               }

               BookModel var2 = var1.bm;

               try {
                  if (BlacklistStore.getInstance().handleGuiMessage(var2.getTemplateId(), var2.getOrgId())) {
                     return;
                  }

                  Ebev var3 = new Ebev(var2);
                  var3.post(false);
               } catch (Exception var4) {
                  var4.printStackTrace();
               }
            }

         }

         public void setParameters(Hashtable var1) {
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
      };
      String var5 = "Nyomtatvány csoportos közvetlen beküldése a magánszemély tárhelyén keresztül";
      this.actUKDirectGroupSend = new AbstractAction(var5, var1.get("groupsend")) {
         public void actionPerformed(ActionEvent var1) {
            UgyfelkapuMenuItem.this.cmdUKDirectGroupSend.execute();
         }
      };
      this.cmdUKDirectGroupSend = new ICommandObject() {
         public void execute() {
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
      String var6 = "Átadás digitális aláírásra";
      this.actUKPassToDsign = new AbstractAction(var6, var1.get("sign")) {
         public void actionPerformed(ActionEvent var1) {
            UgyfelkapuMenuItem.this.cmdUKPassToDsign.execute();
         }
      };
      this.cmdUKPassToDsign = new ICommandObject() {
         public void execute() {
            DefaultMultiFormViewer var1 = MainFrame.thisinstance.mp.getDMFV();
            if (var1 != null) {
               if (!var1.fv.pv.pv.leave_component()) {
                  return;
               }

               BookModel var2 = var1.bm;

               try {
                  Ebev var3 = new Ebev(var2);
                  var3.pass();
               } catch (Exception var4) {
                  var4.printStackTrace();
               }
            }

         }

         public void setParameters(Hashtable var1) {
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
                  } else {
                     if (UgyfelkapuMenuItem.this.menubar.isAutoFillMode()) {
                        return Boolean.FALSE;
                     }

                     return Boolean.TRUE;
                  }
               case 2:
               default:
                  return Boolean.FALSE;
               case 3:
                  return Boolean.FALSE;
               }
            } catch (Exception var3) {
               return Boolean.FALSE;
            }
         }
      };
   }
}
