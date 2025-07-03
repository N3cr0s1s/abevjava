package hu.piller.enykp.alogic.ebev;

import hu.piller.enykp.alogic.ebev.extendedsign.KrPreparation;
import hu.piller.enykp.alogic.filepanels.attachement.AttachementTool;
import hu.piller.enykp.alogic.filesaver.enykinner.EnykInnerSaver;
import hu.piller.enykp.alogic.filesaver.xml.EnykXmlSaver;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.framework.Menubar;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.interfaces.IOsHandler;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.oshandler.OsFactory;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;

public class Mark2Send {
   IPropertyList mpl = PropertyList.getInstance();
   IOsHandler oh = OsFactory.getOsHandler();
   BookModel bookModel;
   SendParams sp;
   MainFrame mainFrame;
   EnykXmlSaver exs;
   public static String kontroll;
   public static String epost;
   String mj;
   String dtv;
   public boolean csoport;
   public boolean needCheck;
   public boolean bigXml;
   public boolean processCancelledByUser;
   public boolean processCancelledByFatalError;
   private Object cmdObject;
   public static final JLabel nyomtatvanyCim = new JLabel("Nyomtatvány előkészítése elektronikus feladásra", 0);
   final JButton cancelButton;

   public Mark2Send(BookModel var1, SendParams var2, boolean var3, boolean var4) {
      this.mainFrame = MainFrame.thisinstance;
      this.csoport = false;
      this.needCheck = true;
      this.bigXml = false;
      this.processCancelledByUser = false;
      this.processCancelledByFatalError = false;
      this.cancelButton = new JButton("Mégsem");
      this.bookModel = var1;
      this.sp = var2;
      this.csoport = var3;
      this.bigXml = var4;
   }

   public Mark2Send(BookModel var1, SendParams var2) {
      this.mainFrame = MainFrame.thisinstance;
      this.csoport = false;
      this.needCheck = true;
      this.bigXml = false;
      this.processCancelledByUser = false;
      this.processCancelledByFatalError = false;
      this.cancelButton = new JButton("Mégsem");
      this.bookModel = var1;
      this.sp = var2;
   }

   public Mark2Send(BookModel var1) {
      this.mainFrame = MainFrame.thisinstance;
      this.csoport = false;
      this.needCheck = true;
      this.bigXml = false;
      this.processCancelledByUser = false;
      this.processCancelledByFatalError = false;
      this.cancelButton = new JButton("Mégsem");
      this.bookModel = var1;
      this.sp = new SendParams(this.mpl);
   }

   public Result mark(final boolean var1, boolean var2) {
      this.processCancelledByUser = false;
      this.processCancelledByFatalError = false;

      try {
         this.exs = new EnykXmlSaver(this.bookModel);
      } catch (Exception var13) {
         this.exs = null;
      }

      this.initGui();
      final boolean[] var3 = new boolean[]{false};
      if (!this.csoport) {
         final JDialog var4 = new JDialog(this.mainFrame, "Megjelölés", true);
         var4.setDefaultCloseOperation(0);
         final boolean[] var5 = new boolean[]{false};
         final SwingWorker var6 = new SwingWorker() {
            public Object doInBackground() {
               Result var1x;
               try {
                  PropertyList.getInstance().set("prop.dynamic.ebev_call_from_menu", Boolean.TRUE);
                  var1x = Mark2Send.this.doFelad(var1);
                  if (!var1x.isOk()) {
                     try {
                        if (((String)var1x.errorList.elementAt(0)).indexOf("XSD") > -1) {
                           var3[0] = true;
                        }
                     } catch (Exception var11) {
                        Tools.eLog(var11, 0);
                     }

                     boolean var2 = true;
                     int var15 = Tools.createErrorLogFromVector(var3[0] ? "XSD hiba" : "Hiba a megjelölés során", var1x.errorList);
                     String var3x = "A megjelölés nem sikerült:\n" + (var15 > -1 ? var1x.errorList.get(var15) : "") + "\nTovábbi üzenetek a Szerviz/Üzenetek menüpontban találhatók";
                     if (var3x.indexOf("Felhasználói megszakítás") > -1) {
                        var3x = "Felhasználó megszakítás!";
                     }

                     if (Mark2Send.this.bookModel.isAvdhModel() && !var1x.isSpecialAvdhUniqueAtcNameError()) {
                        (new KrPreparation(Mark2Send.this.bookModel)).reset(false);
                     }

                     GuiUtil.showMessageDialog(MainFrame.thisinstance, var3x, "Megjelölés", 0);
                  }

                  Mark2Send.this.exs = null;
                  Result var16 = var1x;
                  return var16;
               } catch (Exception var12) {
                  var1x = new Result();
                  var1x.setOk(false);
                  var1x.errorList.add(var12.getMessage());
                  var12.printStackTrace();
               } catch (Error var13) {
                  var1x = new Result();
                  var1x.setOk(false);
                  var1x.errorList.add("Valószínűleg kevés a memória a titkosításhoz.\nAz ÁNYK által használt memória beállítását a\nSzerviz/Beállítások/Működés fülön módosíthatja.");
                  var13.printStackTrace();
               } finally {
                  PropertyList.getInstance().set("prop.dynamic.avdhWithNoAuth", (Object)null);
               }

               if (!var1x.isOk()) {
                  if (PropertyList.getInstance().get("prop.dynamic.signWithExternalTool") == null) {
                     try {
                        if (Mark2Send.this.bookModel.isAvdhModel()) {
                           (new KrPreparation(Mark2Send.this.bookModel)).reset(false);
                        }
                     } catch (Exception var10) {
                        var10.printStackTrace();
                     }
                  }

                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "A megjelölés nem sikerült:\n" + var1x.errorList.get(0), "Megjelölés", 0);
               }

               return var1x;
            }

            public void done() {
               try {
                  Mark2Send.this.cmdObject = this.get();
               } catch (Exception var3x) {
                  var3x.printStackTrace();
                  Mark2Send.this.cmdObject = null;
               }

               var5[0] = true;

               try {
                  var4.setVisible(false);
               } catch (Exception var2) {
                  Tools.eLog(var2, 0);
               }

            }
         };
         var4.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent var1) {
               var6.execute();
            }
         });
         int var7 = MainFrame.thisinstance.getX() + MainFrame.thisinstance.getWidth() / 2 - 250;
         if (var7 < 0) {
            var7 = 0;
         }

         int var8 = MainFrame.thisinstance.getY() + MainFrame.thisinstance.getHeight() / 2 - 200;
         if (var8 < 0) {
            var8 = 0;
         }

         int var9 = GuiUtil.getW("WWWNyomtatvány előkészítése elektronikus feladásraWWW");
         var4.setBounds(var7, var8, var9, 8 * GuiUtil.getCommonItemHeight());
         var4.setSize(var9, 8 * GuiUtil.getCommonItemHeight());
         var4.setPreferredSize(var4.getSize());
         JPanel var10 = new JPanel((LayoutManager)null, true);
         var10.setLayout(new BoxLayout(var10, 1));
         BevelBorder var11 = new BevelBorder(0);
         var10.setBorder(var11);
         Dimension var12 = new Dimension(var9, 5 * GuiUtil.getCommonItemHeight());
         var10.setPreferredSize(var12);
         nyomtatvanyCim.setPreferredSize(var12);
         nyomtatvanyCim.setAlignmentX(0.5F);
         var10.add(nyomtatvanyCim);
         this.cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               Mark2Send.this.cancelButton.setEnabled(false);
            }
         });
         this.cancelButton.setAlignmentX(0.5F);
         var10.add(this.cancelButton);
         var10.add(Box.createVerticalStrut(5));
         nyomtatvanyCim.setText("Nyomtatvány előkészítése elektronikus feladásra");
         var10.setVisible(true);
         var4.getContentPane().add(var10);
         var5[0] = false;
         var4.pack();
         var4.setVisible(true);
         if (var5[0]) {
            var4.setVisible(false);
         }

         return (Result)this.cmdObject;
      } else {
         return this.doFelad(var1);
      }
   }

   public SendParams getParamsFromMPL() {
      return new SendParams(this.mpl);
   }

   private void initGui() {
      this.dtv = (String)this.bookModel.docinfo.get("ver");
      if (this.dtv == null) {
         this.dtv = "";
      }

   }

   public Result doFelad(boolean var1) {
      Result var2 = new Result();
      String var3 = "";
      String var4 = null;

      try {
         String var6;
         try {
            Vector var5 = new Vector();
            if (!this.csoport) {
               if (this.bigXml && !this.bookModel.cc.getLoadedfile().exists()) {
                  var2.setOk(false);
                  var2.errorList.add("A fájl nem létezik (" + this.bookModel.cc.getLoadedfile().getAbsolutePath() + ")");
                  return var2;
               }

               nyomtatvanyCim.setText("Titkosítás előkészítése ...");
            }

            var3 = this.bookModel.cc.getLoadedfile().getAbsolutePath();
            boolean var7 = false;
            if (!this.bigXml) {
               var2 = EbevTools.saveFile(this.bookModel, true, ".kr", this.csoport);
               var4 = null;
               if (var2.isOk()) {
                  var4 = (String)var2.errorList.remove(0);
               } else {
                  this.processCancelledByFatalError = true;
               }
            } else {
               if (this.needCheck) {
                  var2 = this.exs.check(var1, this.bigXml);
               }

               this.needCheck = true;
               if (!var2.isOk()) {
                  return var2;
               }

               var4 = this.copyFileWithReader(new NecroFile(var3), var2.errorList.size() - 1, (String)this.bookModel.docinfo.get("ver"), EbevTools.getFileEncoding(var3));
            }

            if (this.isProcessCancelled(var2)) {
               return var2;
            }

            Vector var31;
            if (!this.bookModel.isSingle()) {
               new Vector();
               Object var8 = this.bookModel.cc.getActiveObject();
               int var9 = Math.min(this.bookModel.forms.size(), this.bookModel.cc.size());

               for(int var10 = 0; var10 < var9; ++var10) {
                  try {
                     FormModel var11 = (FormModel)this.bookModel.forms.elementAt(var10);
                     if (this.isProcessCancelled(var2)) {
                        return var2;
                     }

                     Result var12 = EbevTools.checkAttachment(this.bookModel, var3, var11.id);
                     var2.errorList.addAll(var12.errorList);
                     if (!var12.isOk()) {
                        var7 = true;
                        return var12;
                     }
                  } catch (Exception var24) {
                     Tools.eLog(var24, 0);
                     var7 = true;
                  }
               }

               if (var7) {
                  var2.setOk(false);
                  var2.errorList.clear();
                  var2.errorList.add("Hiba történt a csatolmányok feldolgozásakor!");
                  return var2;
               }

               if (this.isProcessCancelled(var2)) {
                  return var2;
               }

               try {
                  var31 = AttachementTool.mergeCstFiles(var2.errorList);
               } catch (Exception var23) {
                  var2.setOk(false);
                  var2.errorList.clear();
                  var2.errorList.add(var23.getMessage());
                  return var2;
               }

               if (PropertyList.getInstance().get("prop.dynamic.avdhWithNoAuth") == null && this.bookModel.isAvdhModel()) {
                  nyomtatvanyCim.setText("A nyomtatvány és a hozzá kapcsolódó csatolmányokról készült lenyomatok elektronikus hitelesítése ...");
               } else {
                  nyomtatvanyCim.setText("Titkosítás előkészítése ...");
               }

               if (this.isProcessCancelled(var2)) {
                  return var2;
               }

               if (var31.size() > 0) {
                  var31 = EbevTools.createCstFile(this.bookModel, this.bookModel.main_document_id, var31, new NecroFile(var3), 0);
               }

               if (PropertyList.getInstance().get("prop.dynamic.ebev_call_from_menu") != null || PropertyList.getInstance().get("prop.dynamic.ebev_call_from_xmlpost") != null) {
                  AttachementTool.setAttachmentList(var2.errorList);
               }
            } else {
               if (PropertyList.getInstance().get("prop.dynamic.avdhWithNoAuth") == null && this.bookModel.isAvdhModel()) {
                  nyomtatvanyCim.setText("A nyomtatvány és a hozzá kapcsolódó csatolmányokról készült lenyomatok elektronikus hitelesítése ...");
               } else {
                  nyomtatvanyCim.setText("Titkosítás előkészítése ...");
               }

               if (this.isProcessCancelled(var2)) {
                  return var2;
               }

               var31 = EbevTools.doCheckAtcFile(this.bookModel, var3, this.bookModel.get_formid(), 0);
            }

            if (var31 == null) {
               var2.setOk(false);
               var2.errorList.add("Nem található a csatolmány-leíróban szereplő fájl!");
               return var2;
            }

            this.cancelButton.setVisible(false);
            if (this.isProcessCancelled(var2)) {
               return var2;
            }

            if (var4 == null) {
               EbevTools.delFile(this.sp.srcPath + PropertyList.USER_IN_FILENAME + var4);
               var2.setOk(false);
               var2.errorList.add("A nyomtatvány másolása nem sikerült!");
               return var2;
            }

            var2 = EbevTools.checkXSD(this.sp.srcPath + var4);
            if (!var2.isOk()) {
               EbevTools.delFile(this.sp.srcPath + var4);
               return var2;
            }

            var2 = EbevTools.checkPanids(this.bookModel);
            if (!var2.isOk()) {
               EbevTools.delFile(this.sp.srcPath + var4);
               return var2;
            }

            nyomtatvanyCim.setText("Titkosítás ...");
            Enigma var32 = new Enigma(this.bookModel);
            String var33 = EbevTools.createNewFilename(var4, this.bookModel);
            if (this.isProcessCancelled(var2)) {
               return var2;
            }

            String var34 = EbevTools.getKrTargetFilename(this.sp.destPath + var33);
            var2 = var32.doEncrypt(this.sp.srcPath + var4, var34, var31, 0);
            if (!(this.sp.destPath + var33).equals(var34)) {
               EbevTools.copyFile(var34, this.sp.destPath + var33, true);
            }

            String var35 = this.sp.srcPath + var4.substring(0, var4.length() - ".kr".length()) + ".xml";
            File var36 = new NecroFile(var35);
            if (var36.exists()) {
               var36.delete();
            }

            (new NecroFile(this.sp.srcPath + var4)).renameTo(var36);
            if (!var2.isOk()) {
               throw new Exception("Hiba a titkosításkor");
            }

            if (!this.bigXml) {
               EnykInnerSaver var13 = new EnykInnerSaver(this.bookModel);
               var13.save(this.bookModel.cc.getLoadedfile().getAbsolutePath(), true);
            }

            boolean var37 = false;

            try {
               int var38 = ((Vector)PropertyList.getInstance().get("prop.dynamic.ebev_call_from_menu")).size();
            } catch (Exception var22) {
               var37 = false;
            }

            EbevTools.delCstFile(this.bookModel);
            if (var2.isOk()) {
               if (!this.csoport) {
                  var5.add("A titkosítás befejeződött. A titkosított állomány elérhető a");
                  var5.add(" " + this.sp.destPath + " mappában.");
                  var5.add(" " + Tools.beautyPath(var33 + " néven."));
                  var5.addAll(EbevTools.getAtcListFromVectorAsVector(this.bookModel.isAvdhModel()));
                  EbevTools.showResultDialog(var5);
                  nyomtatvanyCim.setText("Titkosítás kész.");
                  EbevTools.handleClipboard(this.bookModel.cc.getLoadedfile());
               } else {
                  String var14 = "A titkosítás befejeződött. A titkosított állomány elérhető a \n" + this.sp.destPath + "\nmappában, \n" + var33 + "\n néven." + EbevTools.getAtcListFromVector(this.bookModel.isAvdhModel());
                  var2.errorList.add(Tools.beautyPath(var14));

                  try {
                     PropertyList.getInstance().set("prop.dynamic.kr_filename_4_cmd_send", Tools.beautyPath(this.sp.destPath + var33));
                  } catch (Exception var21) {
                     Tools.eLog(var21, 0);
                  }
               }
            }
         } catch (AttachementNameException var25) {
            var6 = var25.getMessage();
            var6 = "Csatolmány hiba: " + var6;
            if (!this.csoport) {
               var2.errorList.add(var6);
               var2.setOk(false);
            } else {
               var2.errorList.add("*" + var6);
               var2.setOk(false);
            }

            var2.setSpecialAvdhUniqueAtcNameError(true);
            return var2;
         } catch (AttachementException var26) {
            var26.printStackTrace();
            var6 = var26.getMessage();
            if (var6.startsWith("AVDH_")) {
               var6 = var6.substring(5);
            } else {
               var6 = "Csatolmány hiba: " + var6;
            }

            if (!this.csoport) {
               var2.errorList.add(var6);
               var2.setOk(false);
            } else {
               var2.errorList.add("*" + var6);
               var2.setOk(false);
            }

            try {
               EbevTools.delFile(this.sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
               if (var4 != null) {
                  EbevTools.delFile(this.sp.srcPath + PropertyList.USER_IN_FILENAME + var4);
               }
            } catch (Exception var20) {
               Tools.eLog(var20, 0);
            }

            return var2;
         } catch (FileNotFoundException var27) {
            var27.printStackTrace();

            try {
               EbevTools.delFile(this.sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
               if (var4 != null) {
                  EbevTools.delFile(this.sp.srcPath + PropertyList.USER_IN_FILENAME + var4);
               }
            } catch (Exception var19) {
               Tools.eLog(var19, 0);
            }

            var2.errorList.add("Fájl hiba: " + var27.getMessage());
            var2.setOk(false);
            return var2;
         } catch (IOException var28) {
            var28.printStackTrace();

            try {
               EbevTools.delFile(this.sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
               if (var4 != null) {
                  EbevTools.delFile(this.sp.srcPath + PropertyList.USER_IN_FILENAME + var4);
               }
            } catch (Exception var18) {
               Tools.eLog(var18, 0);
            }

            var2.errorList.add("Fájl hiba2: " + var28.getMessage());
            var2.setOk(false);
            return var2;
         } catch (Exception var29) {
            var29.printStackTrace();

            try {
               EbevTools.delFile(this.sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
               if (var4 != null) {
                  EbevTools.delFile(this.sp.srcPath + PropertyList.USER_IN_FILENAME + var4);
               }
            } catch (Exception var17) {
               Tools.eLog(var17, 0);
            }

            var2.errorList.add("Programhiba: " + var29.getMessage());
            var2.setOk(false);
            return var2;
         }
      } catch (Exception var30) {
         var30.printStackTrace();
      }

      if (var2.isOk()) {
         try {
            if (!this.csoport) {
               Menubar.thisinstance.setState((Object)null);
               MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("Küldésre megjelölt");
               MainFrame.thisinstance.mp.setReadonly(true);
            }
         } catch (Exception var16) {
            Tools.eLog(var16, 0);
         }
      }

      return var2;
   }

   private String copyFile(File var1, int var2, String var3, String var4) {
      FileInputStream var5 = null;
      FileOutputStream var6 = null;
      String var7 = var1.getName().toLowerCase();
      int var8 = var7.lastIndexOf(".xml");
      var7 = var7.substring(0, var8) + ".kr";
      var7 = PropertyList.USER_IN_FILENAME + var7;

      try {
         var5 = new FileInputStream(var1);
         var6 = new NecroFileOutputStream(this.sp.srcPath + var7);
         byte[] var10 = new byte[1024];
         int var9 = var5.read(var10);
         Mark2Send.ByteAndInt var11 = this.handleTemplateVersion(var10, this.dtv, var4);
         int var12 = this.getSecondCloseTagIndex(var11.b);
         if (var12 > -1) {
            var6.write(var11.b, 0, var12);
            this.saveAbevInfo(var6, var2, var3);
            var6.write(var11.b, var12, var9 - var12 + var11.i);
         } else {
            var6.write(var11.b, 0, var9 + var11.i);
         }

         while((var9 = var5.read(var10)) != -1) {
            var6.write(var10, 0, var9);
         }

         var6.flush();
         var6.close();
         var5.close();
         return var7;
      } catch (Exception var15) {
         try {
            var6.close();
         } catch (Exception var14) {
            Tools.eLog(var15, 0);
         }

         try {
            var5.close();
         } catch (Exception var13) {
            Tools.eLog(var15, 0);
         }

         return null;
      }
   }

   private String copyFileWithReader(File var1, int var2, String var3, String var4) {
      BufferedReader var5 = null;
      BufferedWriter var6 = null;
      String var7 = var1.getName().toLowerCase();
      int var8 = var7.lastIndexOf(".xml");
      var7 = var7.substring(0, var8) + ".kr";
      var7 = PropertyList.USER_IN_FILENAME + var7;

      try {
         InputStreamReader var10 = new InputStreamReader(new FileInputStream(var1), var4);
         OutputStreamWriter var11 = new OutputStreamWriter(new NecroFileOutputStream(this.sp.srcPath + var7), var4);
         var5 = new BufferedReader(var10);
         var6 = new BufferedWriter(var11);
         char[] var12 = new char[1024];
         boolean var13 = true;
         int var9 = var5.read(var12);
         Mark2Send.CharAndInt var14 = this.handleTemplateVersion(var12, this.dtv, var4);
         int var15 = this.getSecondCloseTagIndex(var14.c);
         if (var15 > -1) {
            var6.write(var14.c, 0, var15);
            this.saveAbevInfo((Writer)var6, var2, var3);
            var6.write(var14.c, var15, var9 - var15 + var14.i);
         } else {
            var6.write(var14.c, 0, var9 + var14.i);
         }

         while((var9 = var5.read(var12)) != -1) {
            var6.write(var12, 0, var9);
         }

         var6.flush();
         var6.close();
         var5.close();
         return var7;
      } catch (Exception var18) {
         try {
            var6.close();
         } catch (Exception var17) {
            Tools.eLog(var18, 0);
         }

         try {
            var5.close();
         } catch (Exception var16) {
            Tools.eLog(var18, 0);
         }

         return null;
      }
   }

   private void saveAbevInfo(FileOutputStream var1, int var2, String var3) throws IOException {
      StringBuffer var4 = new StringBuffer();
      var4.append("\r\n    <abev>\r\n").append("      <hibakszama>").append(var2).append("</hibakszama>\r\n").append("      <hash>0000000000000000000000000000000000000000</hash>\r\n").append("      <programverzio>").append(var3).append("</programverzio>\r\n").append("    </abev>");
      var1.write(var4.toString().getBytes("utf-8"));
   }

   private int getSecondCloseTagIndex(byte[] var1) {
      String var2 = new String(var1);
      if (var2.indexOf(">") == -1) {
         return -1;
      } else {
         return var2.indexOf("<abev>") > -1 ? -1 : var2.indexOf(">", var2.indexOf(">") + 1) + 1;
      }
   }

   private Mark2Send.ByteAndInt handleTemplateVersion(byte[] var1, String var2, String var3) throws UnsupportedEncodingException {
      String var4 = new String(var1, var3);
      StringBuffer var5 = new StringBuffer(var4);
      boolean var6 = false;
      int var7;
      int var10;
      if (var5.indexOf("<nyomtatvanyverzio>") == -1) {
         var7 = var5.indexOf("</nyomtatvanyazonosito>") + "</nyomtatvanyazonosito>".length();
         var5.insert(var7, "\r\n<nyomtatvanyverzio>" + var2 + "</nyomtatvanyverzio>");
         var10 = ("\r\n<nyomtatvanyverzio>" + var2 + "</nyomtatvanyverzio>").length();
      } else {
         var7 = var5.indexOf("<nyomtatvanyverzio>") + "<nyomtatvanyverzio>".length();
         var5.insert(var7, var2);
         var10 = var2.length();
         int var8 = var5.indexOf("</nyomtatvanyverzio>");
         var5.delete(var7 + var2.length(), var8);
         var10 -= var8 - var7 - var2.length();
      }

      try {
         return new Mark2Send.ByteAndInt(var5.toString().getBytes(var3), var10);
      } catch (UnsupportedEncodingException var9) {
         return new Mark2Send.ByteAndInt(var5.toString().getBytes(), var10);
      }
   }

   private Mark2Send.CharAndInt handleTemplateVersion(char[] var1, String var2, String var3) throws UnsupportedEncodingException {
      String var4 = new String(var1);
      StringBuffer var5 = new StringBuffer(var4);
      boolean var6 = false;
      int var7;
      int var9;
      if (var5.indexOf("<nyomtatvanyverzio>") == -1) {
         var7 = var5.indexOf("</nyomtatvanyazonosito>") + "</nyomtatvanyazonosito>".length();
         var5.insert(var7, "\r\n<nyomtatvanyverzio>" + var2 + "</nyomtatvanyverzio>");
         var9 = ("\r\n<nyomtatvanyverzio>" + var2 + "</nyomtatvanyverzio>").length();
      } else {
         var7 = var5.indexOf("<nyomtatvanyverzio>") + "<nyomtatvanyverzio>".length();
         var5.insert(var7, var2);
         var9 = var2.length();
         int var8 = var5.indexOf("</nyomtatvanyverzio>");
         var5.delete(var7 + var2.length(), var8);
         var9 -= var8 - var7 - var2.length();
      }

      return new Mark2Send.CharAndInt(var5.toString().toCharArray(), var9);
   }

   private int getSecondCloseTagIndex(char[] var1) {
      String var2 = new String(var1);
      if (var2.indexOf(">") == -1) {
         return -1;
      } else {
         return var2.indexOf("<abev>") > -1 ? -1 : var2.indexOf(">", var2.indexOf(">") + 1) + 1;
      }
   }

   private void saveAbevInfo(Writer var1, int var2, String var3) throws IOException {
      StringBuffer var4 = new StringBuffer();
      var4.append("\r\n    <abev>\r\n").append("      <hibakszama>").append(var2).append("</hibakszama>\r\n").append("      <hash>0000000000000000000000000000000000000000</hash>\r\n").append("      <programverzio>").append(var3).append("</programverzio>\r\n").append("    </abev>");
      var1.write(var4.toString().toCharArray());
   }

   private boolean isProcessCancelled(Result var1) {
      if (this.processCancelledByUser) {
         var1.setOk(false);
         var1.errorList.clear();
         var1.errorList.add("Felhasználói megszakítás");
         return true;
      } else if (this.processCancelledByFatalError) {
         var1.setOk(false);
         var1.errorList.clear();
         var1.errorList.add("A nyomtatvány súlyos hibát tartalmaz!");
         return true;
      } else {
         return false;
      }
   }

   private class CharAndInt {
      private char[] c;
      private int i;

      public CharAndInt(char[] var2, int var3) {
         this.c = var2;
         this.i = var3;
      }
   }

   private class ByteAndInt {
      private byte[] b;
      private int i;

      public ByteAndInt(byte[] var2, int var3) {
         this.b = var2;
         this.i = var3;
      }
   }
}
