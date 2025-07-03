package hu.piller.enykp.alogic.ebev;

import hu.piller.enykp.alogic.filepanels.attachement.EJFileChooser;
import hu.piller.enykp.alogic.filepanels.mohu.LoginDialog;
import hu.piller.enykp.alogic.filepanels.mohu.LoginDialogFactory;
import hu.piller.enykp.alogic.filepanels.mohu.MohuTools;
import hu.piller.enykp.alogic.templateutils.blacklist.BlacklistStore;
import hu.piller.enykp.alogic.uploader.AuthenticationException;
import hu.piller.enykp.alogic.uploader.FeltoltesValasz;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.framework.Menubar;
import hu.piller.enykp.gui.framework.StatusPane;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.kauclient.KauAuthMethod;
import hu.piller.enykp.kauclient.KauAuthMethods;
import hu.piller.enykp.kauclient.KauClientException;
import hu.piller.enykp.niszws.util.DapSessionHandler;
import hu.piller.enykp.niszws.util.GateType;
import hu.piller.enykp.niszws.util.KauSessionTimeoutHandler;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.EJList;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Base64;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicFileChooserUI;

public class Send2Mohu {
   private Object cmdObject;
   public static final JProgressBar statusBar = new JProgressBar();
   private JDialog summaDialog;
   public int resultWidth = 600;
   public int resultHeight = (int)(0.5D * (double)GuiUtil.getScreenH());
   private EJFileChooser fc;
   private static File defaultDirectory = null;
   private EJList logLista;
   private JScrollPane logSP;

   public void send(boolean var1, final SendParams var2, final String[] var3, final BookModel var4, final boolean var5) {
      final JDialog var6 = new JDialog(MainFrame.thisinstance, "Beküldés", true);
      var6.setDefaultCloseOperation(0);
      final boolean[] var7 = new boolean[]{false};
      final SwingWorker var8 = new SwingWorker() {
         public Object doInBackground() {
            try {
               Send2Mohu.this.send(var2, var3, var4, var5);
               return new Result();
            } catch (Exception var2x) {
               var2x.printStackTrace();
            } catch (Error var3x) {
               var3x.printStackTrace();
            }

            return new Result();
         }

         public void done() {
            try {
               Send2Mohu.this.cmdObject = this.get();
            } catch (Exception var3x) {
               var3x.printStackTrace();
               Send2Mohu.this.cmdObject = null;
            }

            var7[0] = true;

            try {
               var6.setVisible(false);
            } catch (Exception var2x) {
               Tools.eLog(var2x, 0);
            }

         }
      };
      var6.addWindowListener(new WindowAdapter() {
         public void windowOpened(WindowEvent var1) {
            var8.execute();
         }
      });
      JPanel var11 = new JPanel(new BorderLayout(), true);
      var11.setLayout(new BoxLayout(var11, 1));
      BevelBorder var12 = new BevelBorder(0);
      var11.setBorder(var12);
      int var13 = GuiUtil.getW("WA nyomtatvány beküldés az elkészült fájl méretétől függően hosszabb ideig is tarthat...W");
      Dimension var14 = new Dimension((int)Math.min((double)var13, 0.8D * (double)(Integer)PropertyList.getInstance().get("prop.gui.screen.maxx")), 5 * GuiUtil.getCommonItemHeight());
      var11.setSize(var14);
      var11.setPreferredSize(var11.getSize());
      int var9 = (GuiUtil.getScreenW() - var13 - 20) / 2;
      int var10 = (int)(((double)GuiUtil.getScreenH() - var14.getHeight()) / 2.0D);
      if (var9 < 0) {
         var9 = 0;
      }

      if (var10 < 0) {
         var10 = 0;
      }

      var6.setBounds(var9, var10, var13 + 20, 8 * GuiUtil.getCommonItemHeight());
      var6.setSize(var13 + 20, 8 * GuiUtil.getCommonItemHeight());
      var11.add(new JLabel("  "), "North");
      var11.add(statusBar, "Center");
      var11.add(Box.createVerticalStrut(5));
      if (var5) {
         statusBar.setString("Nyomtatvány közvetlen beküldése");
      } else {
         statusBar.setString("Nyomtatvány közvetlen beküldése az Ügyfélkapun keresztül");
      }

      statusBar.setStringPainted(true);
      statusBar.setBorderPainted(false);
      var11.setVisible(true);
      var6.getContentPane().add(var11);
      var7[0] = false;
      var6.pack();
      var6.setVisible(true);
      if (var7[0]) {
         var6.setVisible(false);
      }

   }

   public void send(SendParams var1, String[] var2, BookModel var3, boolean var4) {
      FeltoltesValasz[] var5 = null;
      boolean var6 = true;
      Vector var7 = new Vector();
      boolean var8 = false;

      try {
         MohuTools var9 = new MohuTools(var1);
         LoginDialog var10 = null;
         if (var4) {
            var10 = LoginDialogFactory.create(GateType.CEGKAPU_HIVATALIKAPU, 0, false, var3.getTaxNumberHKAzonFromDocInfo());
            if (var10 == null) {
               return;
            }

            var10.setLocationRelativeTo(MainFrame.thisinstance);
            var8 = var10.showIfNeed();
            if (var8 && var10.getState() != 3) {
               System.out.println("KAU_LOG nem ok state -> return");
               return;
            }

            var8 = false;
         } else {
            var10 = LoginDialogFactory.create(GateType.UGYFELKAPU, 2, false, var3.getTaxNumberHKAzonFromDocInfo());
            if (var10 == null) {
               return;
            }

            var10.setLocationRelativeTo(MainFrame.thisinstance);
            if (KauAuthMethods.getSelected() != KauAuthMethod.KAU_DAP) {
               var8 = var10.showIfNeed();
               if (var8 && var10.getState() != 3) {
                  return;
               }

               var8 = false;
            }
         }

         statusBar.setString("A nyomtatvány beküldés az elkészült fájl méretétől függően hosszabb ideig is tarthat...");
         statusBar.setIndeterminate(true);

         while(true) {
            boolean var11 = false;

            try {
               if (BlacklistStore.getInstance().handleGuiMessage(var3.getTemplateId(), var3.getOrgId())) {
                  return;
               }

               var5 = var9.callWS(var2, var4, (String)null);
            } catch (AuthenticationException var23) {
               var5 = null;
               var7.add(new TextWithIcon("Nem sikerült kapcsolódni a fogadó szerverhez!", 1));
               var7.add(new TextWithIcon(" - Kérjük ellenőrizze a kapcsolatot és, hogy helyesen adta-e meg a bejelentkezési adatokat!", -1));
            } catch (Exception var24) {
               if (var24.getMessage() != null && var24.getMessage().indexOf("Munkamenet nem érvényes, lépjen be újra!") > -1) {
                  if (GuiUtil.showOptionDialog((Component)null, "Munkamenetének érvényességi ideje lejárt! A művelet megismétléséhez újra bejelentkezik?", "KAÜ bejelentkezés", 0, 3, (Icon)null, MohuTools.repeatCancel, MohuTools.repeatCancel[0]) == 0) {
                     var11 = true;
                     KauSessionTimeoutHandler.getInstance().reset();
                  } else {
                     var5 = null;
                     KauSessionTimeoutHandler.getInstance().reset();
                     var7.add(new TextWithIcon("A nyomtatvány beküldése sikertelen! A munkamenetének érvényességi ideje lejárt!", 1));
                     var7.add(new TextWithIcon(" - Részletes leírást a Szerviz/Üzenetek menüpontban talál.", -1));
                  }
               } else {
                  var5 = null;
                  if (var24 instanceof KauClientException) {
                     if (((KauClientException)var24).isSpecialMessage()) {
                        var7.add(new TextWithIcon("A nyomtatvány beküldése sikertelen!", 1));
                        var7.add(new TextWithIcon(" - " + var24.getMessage(), -1));
                     } else {
                        var7.add(new TextWithIcon("A nyomtatvány beküldése sikertelen!", 1));
                     }

                     var7.add(new TextWithIcon(" - Részletes leírást a Szerviz/Üzenetek menüpontban talál.", -1));
                  } else if (var24.getMessage() != null && var24.getMessage().startsWith("ANYK_KAU")) {
                     var7.add(new TextWithIcon("A nyomtatvány beküldése sikertelen!", 1));
                     var7.add(new TextWithIcon(" - " + var24.getMessage().substring("ANYK_KAU".length()), -1));
                     var7.add(new TextWithIcon(" - Részletes leírást a Szerviz/Üzenetek menüpontban talál.", -1));
                  } else {
                     var7.add(new TextWithIcon("A nyomtatvány beküldése sikertelen!", 1));
                     var7.add(new TextWithIcon(" - Részletes leírást a Szerviz/Üzenetek menüpontban talál.", -1));
                  }
               }
            } finally {
               DapSessionHandler.getInstance().reset();
               KauSessionTimeoutHandler.getInstance().reset();
            }

            if (!var11) {
               if (var4) {
                  statusBar.setString("A nyomtatvány beküldése befejeződött");
               } else {
                  statusBar.setString("A nyomtatvány beküldése az Ügyfélkapun keresztül befejeződött");
               }

               statusBar.setIndeterminate(false);
               if (var5 != null && var5.length != 0) {
                  var8 = true;

                  for(int var12 = 0; var12 < var5.length; ++var12) {
                     var7.add("[" + var5[var12].getFileName() + "]");
                     if (var5[var12].isStored()) {
                        var6 = var9.moveFile(var1.destPath + var5[var12].getFileName());
                        var7.add(" - fájl sikeresen beküldve.");
                        if (!var6) {
                           var7.add(" - az áthelyezés a " + var1.sentPath + " mappába nem sikerült. Kérjük helyezze át a fájlt más módon!");
                        }

                        if (var5[var12].getFilingNumber() == null) {
                           var7.add(new TextWithIcon("A nyomtatvány a érkeztetési számát az értesítési tárhelyére belépve tudja megnézni. A továbbiakban azon a számon hivatkozhat rá.", 3));
                        } else {
                           var7.add(new TextWithIcon("A nyomtatvány a " + var5[var12].getFilingNumber() + " érkeztetési számot kapta. A továbbiakban ezen a számon hivatkozhat rá.", 3));

                           try {
                              byte var13 = 3;
                              if (var4) {
                                 var13 = 4;
                              }

                              if (PropertyList.getInstance().get("prop.dynamic.gateTypeEPER") != null) {
                                 var13 = 5;
                              }

                              Ebev.log(var13, var3.cc.getLoadedfile(), "Érkeztetési szám: " + var5[var12].getFilingNumber());
                           } catch (Exception var22) {
                              try {
                                 System.out.println("Figyelmeztetés! Nem sikerült a feladás naplózása. A nyomtatvány a " + var5[var12].getFilingNumber() + " érkeztetési számot kapta");
                              } catch (Exception var21) {
                                 Tools.eLog(var22, 0);
                              }
                           }
                        }
                     } else {
                        var8 = false;
                        var7.add(new TextWithIcon(" - fájl küldése nem sikerült.", 0));
                        if (var5[var12].getErrorMsg() != null) {
                           var7.add(new TextWithIcon("A sikertelenség oka a fogadó oldal szerint: " + var5[var12].getErrorMsg(), 0));
                        }
                     }

                     var7.add(new TextWithIcon(" ", -1));
                  }
               }
               break;
            }
         }
      } catch (Exception var26) {
         var7.add(new TextWithIcon("A fájlok küldésekor súlyos hiba történt, a küldés megszakadt!", 1));
         ErrorList.getInstance().writeError(new Long(4001L), "Mohu küldése hiba", var26, (Object)null);
      }

      if (var4) {
         this.fillDialog("Nyomtatvány közvetlen beküldése", var7);
      } else {
         this.fillDialog("Nyomtatvány közvetlen beküldése az Ügyfélkapun keresztül", var7);
      }

      if (var8) {
         Menubar.thisinstance.setState((Object)null);
         StatusPane.thisinstance.statusname.setText("Elküldött");
      }

   }

   private void delXmlFileFromSignedFolder(BookModel var1, SendParams var2) {
      String var3 = var1.cc.getLoadedfile().getName();
      var3 = var3.substring(0, var3.length() - ".frm.enyk".length());
      (new NecroFile(var2.srcPath + var3 + ".xml")).delete();
   }

   private void fillDialog(String var1, Vector var2) {
      int var3 = GuiUtil.getW("WW - Részletes leírást a Szerviz/Üzenetek menüpontban talál.WW");
      this.resultWidth = Math.max(this.resultWidth, var3);
      this.resultWidth = (int)Math.min((double)this.resultWidth, 0.8D * (double)GuiUtil.getScreenW());
      this.fc = new EJFileChooser();
      this.logLista = new EJList();
      this.logSP = new JScrollPane(this.logLista, 20, 30);
      this.summaDialog = new JDialog(MainFrame.thisinstance, "", true);
      this.summaDialog.setSize(this.resultWidth, this.resultHeight);
      this.summaDialog.setLocationRelativeTo(MainFrame.thisinstance);
      JPanel var4 = new JPanel();
      JButton var5 = new JButton("Rendben");
      JButton var6 = new JButton("Lista mentése");
      var5.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Send2Mohu.this.summaDialog.setVisible(false);
            Send2Mohu.this.summaDialog.dispose();
         }
      });
      var6.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Send2Mohu.this.logSaveAction();
         }
      });
      var4.add(var5);
      var4.add(var6);
      this.summaDialog.getContentPane().add(var4, "South");
      this.summaDialog.setTitle(var1);
      this.logLista.removeAll();
      this.logLista.setListData(var2);
      this.logSP.setViewportView(this.logLista);
      Dimension var7 = new Dimension(this.resultWidth, this.resultHeight);
      this.logSP.setPreferredSize(var7);
      this.logSP.setMinimumSize(var7);
      this.summaDialog.getContentPane().add(this.logSP, "Center");
      this.summaDialog.pack();
      this.summaDialog.setVisible(true);
   }

   private void logSaveAction() {
      this.initDialog("Lista mentése");

      try {
         this.fc.setCurrentDirectory(new NecroFile((String)PropertyList.getInstance().get("prop.usr.naplo")));
      } catch (Exception var10) {
         Tools.eLog(var10, 0);
      }

      try {
         ((BasicFileChooserUI)this.fc.getUI()).setFileName("magyarorszag_hu_kuldesek_uzenetei.txt");
      } catch (ClassCastException var9) {
         try {
            this.fc.setSelectedFile(new NecroFile("magyarorszag_hu_kuldesek_uzenetei.txt"));
         } catch (Exception var8) {
            Tools.eLog(var8, 0);
         }
      }

      boolean var1;
      do {
         var1 = true;
         int var2 = this.fc.showSaveDialog(this.summaDialog);
         if (var2 == 0) {
            File var3 = this.fc.getSelectedFile();
            if (var3.exists()) {
               var1 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Ilyen nevű fájl már létezik. Felülírjuk?", "Nyomtatvány küldése", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0;
            }

            if (var1) {
               FileOutputStream var4 = null;

               try {
                  var4 = new NecroFileOutputStream(var3);

                  for(int var5 = 0; var5 < this.logLista.getModel().getSize(); ++var5) {
                     if (this.logLista.getModel().getElementAt(var5) instanceof TextWithIcon) {
                        var4.write((((TextWithIcon)((TextWithIcon)this.logLista.getModel().getElementAt(var5))).text + "\r\n").getBytes());
                     } else {
                        var4.write((this.logLista.getModel().getElementAt(var5).toString() + "\r\n").getBytes());
                     }
                  }

                  var4.close();
               } catch (Exception var11) {
                  try {
                     var4.close();
                  } catch (Exception var7) {
                     Tools.eLog(var7, 0);
                  }

                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "A lista mentése nem sikerült!", "Hiba", 0);
               }

               defaultDirectory = this.fc.getCurrentDirectory();
            }
         }
      } while(!var1);

   }

   private void initDialog(String var1) {
      this.fc.setDialogTitle(var1);
      if (defaultDirectory != null) {
         this.fc.setCurrentDirectory(defaultDirectory);
      }

      try {
         ((BasicFileChooserUI)this.fc.getUI()).setFileName("");
      } catch (ClassCastException var5) {
         try {
            this.fc.setSelectedFile(new NecroFile(""));
         } catch (Exception var4) {
            Tools.eLog(var4, 0);
         }
      }

      this.fc.setSelectedFile((File)null);
   }

   private void delXmlFileFromSignedFolder(String var1) {
      SendParams var2 = new SendParams(PropertyList.getInstance());
      String var3 = (new NecroFile(var1)).getName();
      if (var3.toLowerCase().endsWith(".kr")) {
         var3 = var3.substring(0, var3.length() - ".kr".length());
      }

      if (var3.toLowerCase().endsWith(".xml")) {
         var3 = var3.substring(0, var3.length() - ".xml".length());
      }

      (new NecroFile(var2.srcPath + var3 + ".xml")).delete();
   }

   public Object[] parseMohuFile(String var1) throws Exception {
      Object[] var2 = new Object[3];
      File var3 = new NecroFile(var1);
      BufferedReader var4 = new BufferedReader(new FileReader(var3));
      String var5 = var4.readLine();
      String[] var6 = var5.split(":");
      if (var6.length < 2) {
         throw new Exception("Hibás a felhasználónév - jelszó párost tartalmazó fájl. ':'-tal kell elválasztani egymástól a két adatot");
      } else {
         String var7 = new String(Base64.getDecoder().decode(var6[0]), "utf-8");
         String var8 = new String(Base64.getDecoder().decode(var6[1]), "utf-8");
         var2[0] = var7;
         char[] var9 = new char[var8.length()];
         var8.getChars(0, var8.length(), var9, 0);
         var2[1] = var9;
         var2[2] = "0";
         return var2;
      }
   }

   public String[] parseKAUFile(String var1) throws Exception {
      String[] var2 = new String[3];
      File var3 = new NecroFile(var1);
      BufferedReader var4 = new BufferedReader(new FileReader(var3));
      String var5 = var4.readLine();
      String[] var6 = var5.split(":");
      if (var6.length < 2) {
         throw new Exception("Hibás a felhasználónév - jelszó párost tartalmazó fájl. ':'-tal kell elválasztani egymástól a két adatot");
      } else {
         var2[0] = new String(Base64.getDecoder().decode(var6[0]), "utf-8");
         var2[1] = new String(Base64.getDecoder().decode(var6[1]), "utf-8");
         var2[2] = var6.length > 2 ? new String(Base64.getDecoder().decode(var6[2]), "utf-8") : null;
         return var2;
      }
   }
}
