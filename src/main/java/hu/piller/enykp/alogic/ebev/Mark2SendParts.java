package hu.piller.enykp.alogic.ebev;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.filesaver.enykinner.EnykInnerSaver;
import hu.piller.enykp.alogic.filesaver.xml.EnykXmlSaverParts;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.framework.Menubar;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IOsHandler;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.oshandler.OsFactory;
import me.necrocore.abevjava.NecroFile;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;

public class Mark2SendParts {
   IPropertyList mpl = PropertyList.getInstance();
   IOsHandler oh = OsFactory.getOsHandler();
   BookModel bookModel;
   SendParams sp;
   MainFrame mainFrame;
   EnykXmlSaverParts exsp;
   public static String kontroll;
   public static String epost;
   String mj;
   String dtv;
   public boolean csoport;
   public boolean needCheck;
   public boolean processCanceled;
   private Object cmdObject;
   public static final JLabel nyomtatvanyCim = new JLabel("Nyomtatvány előkészítése elektronikus feladásra", 0);
   final JButton cancelButton;

   public Mark2SendParts(BookModel var1, SendParams var2, boolean var3) {
      this.mainFrame = MainFrame.thisinstance;
      this.csoport = false;
      this.needCheck = true;
      this.processCanceled = false;
      this.cancelButton = new JButton("Mégsem");
      this.bookModel = var1;
      this.sp = var2;
      this.csoport = var3;
   }

   public Result mark() {
      return this.mark(true);
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

   public Result doFelad(int var1) {
      Result var2 = new Result();
      String var3 = "";

      try {
         try {
            String var4 = "";
            if (!this.csoport) {
               nyomtatvanyCim.setText("Titkosítás előkészítése ...");
            }

            var3 = this.bookModel.cc.getLoadedfile().getAbsolutePath();
            Vector var5 = new Vector();

            for(int var6 = 0; var6 < this.bookModel.cc.size(); ++var6) {
               Vector var7 = EbevTools.doCheckAtcFile(this.bookModel, var3, ((Elem)this.bookModel.cc.get(var6)).getType(), var6);
               if (var7 == null) {
                  var2.setOk(false);
                  var2.errorList.add("Nem található a csatolmány-leíróban szereplő fájl!");
                  return var2;
               }

               if (var7.size() == 0) {
                  var5.add((Object)null);
               } else {
                  var5.addAll(var7);
               }
            }

            Vector var26 = null;
            var2 = this.exsp.saveFile(this.bookModel, ".kr", this.csoport);
            if (var2.isOk()) {
               var26 = var2.errorList;
            }

            if (this.processCanceled) {
               var2.setOk(false);
               var2.errorList.clear();
               var2.errorList.add("Felhasználói megszakítás");
               return var2;
            }

            this.cancelButton.setVisible(false);
            if (var26 != null) {
               var2 = EbevTools.checkPanids(this.bookModel);
               if (!var2.isOk()) {
                  return var2;
               }

               nyomtatvanyCim.setText("Titkosítás ...");
               Enigma var27 = new Enigma(this.bookModel);
               String[] var8 = new String[var26.size()];

               int var9;
               for(var9 = 0; var9 < var26.size(); ++var9) {
                  var2 = EbevTools.checkXSD(this.sp.srcPath + var26.elementAt(var9));
                  if (!var2.isOk()) {
                     throw new XsdException();
                  }

                  var27.setFormName(this.bookModel.get(((Elem)this.bookModel.cc.get(var9)).getType()).name);
                  var8[var9] = EbevTools.createNewFilename((String)var26.elementAt(var9), this.bookModel);
                  String var10 = EbevTools.getKrTargetFilename(this.sp.destPath + var8[var9]);
                  var2 = var27.doEncrypt(this.sp.srcPath + var26.elementAt(var9), var10, var5, var9, false);
                  if (!(this.sp.destPath + var8[var9]).equals(var10)) {
                     EbevTools.copyFile(var10, this.sp.destPath + var8[var9], true);
                  }

                  if (!var2.isOk()) {
                     throw new Exception("Hiba a titkosításkor");
                  }

                  EnykInnerSaver var11 = new EnykInnerSaver(this.bookModel);
                  var11.save(this.bookModel.cc.getLoadedfile().getAbsolutePath(), true);
               }

               var4 = "A titkosítás befejeződött.\nFIGYELEM! Ez a nyomtatvány több részből áll. Az egyes nyomtatványokat külön-külön kell beküldeni az Ügyfélkapu segítségével.\nAz alábbi titkosított állományok elérhetők a \n" + this.sp.destPath + "\nmappában, \n";

               for(var9 = 0; var9 < var26.size(); ++var9) {
                  try {
                     var4 = var4 + var8[var9] + "\n";
                  } catch (Exception var19) {
                     var4 = var4 + var26.elementAt(var9) + "\n";
                  }

                  File var28 = new NecroFile(this.sp.srcPath + var26.elementAt(var9));

                  try {
                     if (!var28.delete() && !var28.delete()) {
                        ErrorList.getInstance().writeError(new Long(4001L), "Nem sikerült a fájl törlése: " + var28.getAbsolutePath(), new IOException(), (Object)null);
                     }
                  } catch (Exception var18) {
                     ErrorList.getInstance().writeError(new Long(4001L), "Nem sikerült a fájl törlése: " + var28.getAbsolutePath(), var18, (Object)null);
                  }
               }

               var4 = var4 + "néven.";
               EbevTools.delCstFile(this.bookModel);
               if (var2.isOk()) {
                  if (!this.csoport) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, Tools.beautyPath(var4), "Üzenet", 1);
                     nyomtatvanyCim.setText("Titkosítás kész.");
                     EbevTools.handleClipboard(this.bookModel.cc.getLoadedfile());
                  } else {
                     var2.errorList.add(Tools.beautyPath(var4));
                  }
               }
            }
         } catch (AttachementException var20) {
            var20.printStackTrace();
            if (!this.csoport) {
               var2.errorList.add("Csatolmány hiba: " + var20.getMessage());
               var2.setOk(false);
            } else {
               var2.errorList.add("*Csatolmány hiba: " + var20.getMessage());
               var2.setOk(false);
            }

            try {
               EbevTools.delFile(this.sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
               EbevTools.delFile(this.sp.destPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
            } catch (Exception var17) {
               Tools.eLog(var17, 0);
            }

            return var2;
         } catch (FileNotFoundException var21) {
            var21.printStackTrace();

            try {
               EbevTools.delFile(this.sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
               EbevTools.delFile(this.sp.destPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
            } catch (Exception var16) {
               Tools.eLog(var16, 0);
            }

            var2.errorList.insertElementAt("Fájl hiba: " + var21.getMessage(), 0);
            var2.setOk(false);
            return var2;
         } catch (IOException var22) {
            var22.printStackTrace();

            try {
               EbevTools.delFile(this.sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
               EbevTools.delFile(this.sp.destPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
            } catch (Exception var15) {
               Tools.eLog(var15, 0);
            }

            var2.errorList.insertElementAt("Fájl hiba2: " + var22.getMessage(), 0);
            var2.setOk(false);
            return var2;
         } catch (XsdException var23) {
            try {
               EbevTools.delFile(this.sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
               EbevTools.delFile(this.sp.destPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
            } catch (Exception var14) {
               Tools.eLog(var14, 0);
            }

            var2.errorList.add("Xsd hiba");
            var2.setOk(false);
            return var2;
         } catch (Exception var24) {
            var24.printStackTrace();

            try {
               EbevTools.delFile(this.sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
               EbevTools.delFile(this.sp.destPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
            } catch (Exception var13) {
               Tools.eLog(var13, 0);
            }

            var2.errorList.insertElementAt("Programhiba: " + var24.getMessage(), 0);
            var2.setOk(false);
            return var2;
         }
      } catch (Exception var25) {
         var25.printStackTrace();
      }

      if (var2.isOk()) {
         try {
            if (!this.csoport) {
               Menubar.thisinstance.setState((Object)null);
               MainFrame.thisinstance.mp.getStatuspanel().statusname.setText("Küldésre megjelölt");
               MainFrame.thisinstance.mp.setReadonly(true);
            }
         } catch (Exception var12) {
            Tools.eLog(var12, 0);
         }
      }

      return var2;
   }

   public Result mark(boolean var1) {
      this.processCanceled = false;

      try {
         this.exsp = new EnykXmlSaverParts(this.bookModel, 0);
      } catch (Exception var12) {
         this.exsp = null;
      }

      this.initGui();
      final boolean[] var3 = new boolean[]{false};
      if (!this.csoport) {
         final JDialog var4 = new JDialog(this.mainFrame, "Megjelölés", true);
         var4.setDefaultCloseOperation(0);
         final boolean[] var5 = new boolean[]{false};
         final SwingWorker var6 = new SwingWorker() {
            public Object doInBackground() {
               try {
                  Result var1 = Mark2SendParts.this.doFelad(0);
                  if (!var1.isOk()) {
                     try {
                        if (((String)var1.errorList.elementAt(0)).indexOf("XSD") > -1) {
                           var3[0] = true;
                        }
                     } catch (Exception var8) {
                        Tools.eLog(var8, 0);
                     }

                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "A megjelölés nem sikerült:\n" + var1.errorList.get(0), "Megjelölés", 0);
                  }

                  Mark2SendParts.this.exsp = null;
                  Result var2 = var1;
                  return var2;
               } catch (Exception var9) {
                  var9.printStackTrace();
               } catch (Error var10) {
                  var10.printStackTrace();
               } finally {
                  PropertyList.getInstance().set("prop.dynamic.avdhWithNoAuth", (Object)null);
               }

               return new Result();
            }

            public void done() {
               try {
                  Mark2SendParts.this.cmdObject = this.get();
               } catch (Exception var3x) {
                  var3x.printStackTrace();
                  Mark2SendParts.this.cmdObject = null;
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

         var4.setBounds(var7, var8, 500, 8 * GuiUtil.getCommonItemHeight());
         var4.setSize(500, 8 * GuiUtil.getCommonItemHeight());
         JPanel var9 = new JPanel((LayoutManager)null, true);
         var9.setLayout(new BoxLayout(var9, 1));
         BevelBorder var10 = new BevelBorder(0);
         var9.setBorder(var10);
         Dimension var11 = new Dimension(500, 5 * GuiUtil.getCommonItemHeight());
         var9.setPreferredSize(var11);
         nyomtatvanyCim.setPreferredSize(var11);
         nyomtatvanyCim.setAlignmentX(0.5F);
         var9.add(nyomtatvanyCim);
         this.cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               Mark2SendParts.this.processCanceled = true;
               CalculatorManager.getInstance().check_all_stop((Object)null);
               Mark2SendParts.this.cancelButton.setEnabled(false);
            }
         });
         this.cancelButton.setAlignmentX(0.5F);
         var9.add(this.cancelButton);
         var9.add(Box.createVerticalStrut(5));
         nyomtatvanyCim.setText("Nyomtatvány előkészítése elektronikus feladásra");
         var9.setVisible(true);
         var4.getContentPane().add(var9);
         var5[0] = false;
         var4.pack();
         var4.setVisible(true);
         if (var5[0]) {
            var4.setVisible(false);
         }

         return (Result)this.cmdObject;
      } else {
         return this.doFelad(0);
      }
   }
}
