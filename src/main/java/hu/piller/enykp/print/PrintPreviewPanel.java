package hu.piller.enykp.print;

import hu.piller.enykp.alogic.filepanels.attachement.EJFileChooser;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.viewer.FormPrinter;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import hu.piller.enykp.util.icon.ENYKIconSet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PrintPreviewPanel extends JDialog implements ActionListener, ChangeListener {
   private static float nagyitasiArany;
   private Book book;
   private PrinterJob printJob;
   public static int brPlus = 200;
   public static final int RES = 3;
   public static final int TEXT_SHIFT = 10;
   private JPanel foPanel = new JPanel();
   private final JPanel folsoPanel = new JPanel();
   private static final int MARGIN_IMG = 10;
   private int buttonPanelHeight;
   private int pageIndex;
   EJFileChooser fc = new EJFileChooser();
   public String loadedFilename;
   ENYKIconSet eset = ENYKIconSet.getInstance();
   final JButton elsoGomb;
   final JButton elozoGomb;
   final JButton kovetkezoGomb;
   final JButton utolsoGomb;
   final JButton visszaGomb;
   final JButton nyomtatGomb;
   final JButton printToImageFileGomb;
   final JButton teljesMeretGomb;
   final JSlider nagyitas;
   int prevSliderValue;
   private JLabel holvagyunk;
   private PrintPreviewPanel.PrintPreview printPreview;
   private Dimension screenSize;
   private Dimension ed;
   private Dimension bd;
   BufferedImage bImage;
   int pageCount;
   Hashtable ppIndex;
   private Dimension initSize;
   private Vector<Lap> lapok;
   private int szin;
   public static String datum;
   private Cursor defaultCursor;
   private Vector<Book> uniquePrintablePages;
   private EmptyBorder kisGombKeret;

   public PrintPreviewPanel(PrinterJob var1, Book var2, Vector<Lap> var3, Dialog var4, String var5, boolean var6, float var7, int var8, Vector<Book> var9) throws HeadlessException {
      super(var4, var5, var6);
      this.elsoGomb = new JButton("", this.eset.get("page_tobb_lapozas_balra"));
      this.elozoGomb = new JButton(this.eset.get("page_egy_lapozas_balra"));
      this.kovetkezoGomb = new JButton(this.eset.get("page_egy_lapozas_jobbra"));
      this.utolsoGomb = new JButton(this.eset.get("page_tobb_lapozas_jobbra"));
      this.visszaGomb = new JButton(this.eset.get("anyk_kilepes"));
      this.nyomtatGomb = new JButton(this.eset.get("anyk_nyomtatas"));
      this.printToImageFileGomb = new JButton(this.eset.get("anyk_k_jpg_mentes"));
      this.teljesMeretGomb = new JButton(this.eset.get("anyk_kitoltes_mezoertek_alapjan"));
      this.nagyitas = new JSlider(0);
      this.prevSliderValue = 1;
      this.holvagyunk = new JLabel();
      this.screenSize = new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 50, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 100);
      this.ed = new Dimension(150, 30);
      this.bd = GuiUtil.getButtonSizeByIcon(this.printToImageFileGomb);
      this.pageCount = 1;
      this.ppIndex = new Hashtable();
      this.szin = 1;
      this.defaultCursor = new Cursor(0);
      this.uniquePrintablePages = var9;
      this.szin = var8;
      this.book = var2;
      this.lapok = var3;
      if (var3.size() == 0) {
         throw new HeadlessException("nincs nyomtatható lap");
      } else {
         nagyitasiArany = var7;
         this.init();
         this.setBounds(0, 0, (int)this.initSize.getWidth() + 20, (int)this.screenSize.getHeight() - this.buttonPanelHeight - 20 - 50);
         this.setBackground(Color.WHITE);
         this.fc.setMultiSelectionEnabled(false);
         this.fc.setDialogTitle("A képfájl-ok mentési könyvtárának kijelölése");

         try {
            this.fc.setCurrentDirectory(new File((String)PropertyList.getInstance().get("prop.usr.naplo")));
         } catch (Exception var11) {
            Tools.eLog(var11, 0);
         }

      }
   }

   public Vector getUniqueNames() {
      Vector var1 = new Vector();

      for(int var2 = 0; var2 < this.lapok.size(); ++var2) {
         if (((Lap)this.lapok.elementAt(var2)).getLma().isNyomtatando() && ((Lap)this.lapok.elementAt(var2)).getLma().uniquePrintable) {
            var1.add(((Lap)this.lapok.elementAt(var2)).getLma().lapCim);
         }
      }

      return var1;
   }

   public void init() {
      this.getContentPane().add(this.foPanel);
      this.pageCount = this.lapok.size();
      JPanel var1 = new JPanel();
      var1.setLayout(new BoxLayout(var1, 0));
      int var2 = Math.max(2, (this.printToImageFileGomb.getIcon().getIconHeight() - this.elsoGomb.getIcon().getIconHeight()) / 2);
      this.kisGombKeret = new EmptyBorder(var2, var2, var2, var2);
      var1.setSize(new Dimension(400, (int)(GuiUtil.getButtonSizeByIcon(this.elsoGomb).getHeight() + 4.0D)));
      var1.setPreferredSize(var1.getSize());
      var1.setMinimumSize(var1.getSize());
      this.folsoPanel.setLayout(new BoxLayout(this.folsoPanel, 0));
      new Dimension(800, 600);
      this.foPanel.setLayout(new BorderLayout());
      this.setButtonState(this.elsoGomb, this.elozoGomb, this.kovetkezoGomb, this.utolsoGomb);
      this.elsoGomb.addActionListener(this);
      this.elsoGomb.setToolTipText("Első oldal");
      this.elozoGomb.addActionListener(this);
      this.elozoGomb.setToolTipText("Előző oldal");
      this.kovetkezoGomb.addActionListener(this);
      this.kovetkezoGomb.setToolTipText("Következő oldal");
      this.utolsoGomb.addActionListener(this);
      this.utolsoGomb.setToolTipText("Utolsó oldal");
      this.visszaGomb.addActionListener(this);
      this.visszaGomb.setToolTipText("Nyomtatási kép bezárása");
      this.teljesMeretGomb.addActionListener(this);
      this.teljesMeretGomb.setToolTipText("100%-os nézet");
      this.nyomtatGomb.addActionListener(this);
      this.nyomtatGomb.setToolTipText("Nyomtatás");
      this.printToImageFileGomb.addActionListener(this);
      this.printToImageFileGomb.setToolTipText("Nyomtatás jpg fájlba");
      int var4 = (int)((Lap)this.lapok.elementAt(0)).getLma().meret_mod.getWidth();
      int var5 = (int)((Lap)this.lapok.elementAt(0)).getLma().meret_mod.getHeight();
      double var6 = (this.screenSize.getHeight() - 20.0D - (double)this.buttonPanelHeight) / (double)var5;
      if (var6 > 1.0D) {
         var6 = 0.99D;
      }

      this.nagyitas.setMaximum(500);
      this.nagyitas.setMinimum((int)(var6 >= 0.5D ? 50.0D : var6 * 100.0D));
      this.nagyitas.addChangeListener(this);
      this.nagyitas.setPaintTicks(true);
      this.nagyitas.setPaintLabels(true);
      this.holvagyunk.setPreferredSize(this.ed);
      this.nagyitas.setPreferredSize(this.ed);
      this.nagyitas.setMinimumSize(this.ed);
      this.nagyitas.setSize(this.ed);
      this.nagyitas.setMaximumSize(this.ed);
      this.elsoGomb.setPreferredSize(this.bd);
      this.elsoGomb.setBorder(this.kisGombKeret);
      this.elozoGomb.setPreferredSize(this.bd);
      this.elozoGomb.setBorder(this.kisGombKeret);
      this.kovetkezoGomb.setPreferredSize(this.bd);
      this.kovetkezoGomb.setBorder(this.kisGombKeret);
      this.teljesMeretGomb.setPreferredSize(this.bd);
      this.nyomtatGomb.setPreferredSize(this.bd);
      this.printToImageFileGomb.setPreferredSize(this.bd);
      this.utolsoGomb.setPreferredSize(this.bd);
      this.utolsoGomb.setBorder(this.kisGombKeret);
      this.visszaGomb.setPreferredSize(this.bd);
      var1.add(this.elsoGomb);
      var1.add(this.elozoGomb);
      var1.add(this.kovetkezoGomb);
      var1.add(this.utolsoGomb);
      var1.add(this.teljesMeretGomb);
      var1.add(this.nagyitas);
      var1.add(this.nyomtatGomb);
      var1.add(this.printToImageFileGomb);
      var1.add(new JLabel(""));
      this.setHolvagyunk();
      this.buttonPanelHeight = var1.getHeight();
      Border var8 = BorderFactory.createEtchedBorder();
      this.holvagyunk.setBorder(var8);
      this.folsoPanel.add(this.holvagyunk);
      this.folsoPanel.add(var1);
      this.folsoPanel.add(this.visszaGomb);
      this.folsoPanel.setMinimumSize(new Dimension(Math.min((int)(2.0D * this.ed.getWidth() + 9.0D * this.bd.getWidth()), (int)((double)GuiUtil.getScreenW() * 0.8D)), GuiUtil.getCommonItemHeight() + 4));
      this.folsoPanel.setSize(this.folsoPanel.getMinimumSize());
      this.foPanel.add(this.folsoPanel, "North");
      this.initSize = ((Lap)this.lapok.elementAt(this.pageIndex)).getLma().meret;
      this.printPreview = new PrintPreviewPanel.PrintPreview(var6, this);
      this.createPreviewImage();
      JScrollPane var9 = new JScrollPane(this.printPreview, 20, 30);
      this.foPanel.add(var9, "Center");
      int var10 = Math.max((int)(2.0D * this.ed.getWidth() + 9.0D * this.bd.getWidth()), (int)(1.2D * (double)var4));
      this.foPanel.setSize((int)Math.min(this.screenSize.getWidth(), (double)var10), var5);
      this.foPanel.setPreferredSize(this.foPanel.getSize());
      datum = this.getTimeString();
      this.setMinimumSize(this.folsoPanel.getSize());
   }

   public void ujmeret(Dimension var1) {
      int var10000 = (int)Math.min(var1.getWidth(), this.screenSize.getWidth());
      int var3 = (int)Math.min(var1.getHeight(), this.screenSize.getHeight());
      int var4 = (int)Math.max(2.0D * this.ed.getWidth() + 9.0D * this.bd.getWidth(), var1.getWidth());
      this.foPanel.setSize((int)Math.min(this.screenSize.getWidth(), (double)var4), var3);
      this.foPanel.setPreferredSize(this.getSize());
      this.setSize((int)Math.max(this.folsoPanel.getMinimumSize().getWidth(), this.foPanel.getSize().getWidth()), var3);
      this.repaint();
      this.validate();
   }

   private void setButtonState(JButton var1, JButton var2, JButton var3, JButton var4) {
      var1.setEnabled(this.pageIndex != 0);
      var2.setEnabled(this.pageIndex != 0);
      var3.setEnabled(this.pageIndex != this.lapok.size() - 1);
      var4.setEnabled(this.pageIndex != this.lapok.size() - 1);
   }

   private void createPreviewImage() {
      int var1 = (int)((Lap)this.lapok.elementAt(this.pageIndex)).getLma().meret.getWidth();
      int var2 = (int)((Lap)this.lapok.elementAt(this.pageIndex)).getLma().meret.getHeight();
      if (this.szin == 12) {
         this.szin = 10;
      }

      if (!MainPrinter.hasFatalError && !MainPrinter.elektronikus && !MainPrinter.notVKPrint && MainPrinter.voltEEllenorzesNyomtatasElott && !MainPrinter.betaVersion && !MainPrinter.papirosBekuldesTiltva && MainPrinter.getBookModel().getOperationMode().equals("0") && !MainPrinter.isTemplateDisabled) {
         brPlus = 200;
      } else {
         brPlus = 0;
      }

      this.bImage = new BufferedImage(var1, var2 + 200, this.szin);

      try {
         ((Lap)this.lapok.elementAt(this.pageIndex)).printPreview(this.bImage);
      } catch (PrinterException var4) {
         this.hiba(var4.getMessage());
      }

   }

   public void actionPerformed(ActionEvent var1) {
      if (var1.getSource() == this.elozoGomb && this.pageIndex > 0) {
         --this.pageIndex;
         this.setHolvagyunk();
         this.createPreviewImage();
      }

      if (var1.getSource() == this.kovetkezoGomb && this.pageIndex < this.lapok.size() - 1) {
         ++this.pageIndex;
         this.setHolvagyunk();
         this.createPreviewImage();
      }

      if (var1.getSource() == this.elsoGomb) {
         this.pageIndex = 0;
         this.setHolvagyunk();
         this.createPreviewImage();
      }

      if (var1.getSource() == this.utolsoGomb) {
         this.pageIndex = this.lapok.size() - 1;
         this.setHolvagyunk();
         this.createPreviewImage();
      }

      if (var1.getSource() == this.visszaGomb) {
         this.resetMindenKep();
         this.dispose();
         this.setCursor(this.defaultCursor);
      } else {
         if (var1.getSource() == this.teljesMeretGomb) {
            this.nagyitas.setValue(100);
         }

         if (var1.getSource() == this.nyomtatGomb) {
            if (this.doPrint()) {
               MainPrinter.logPrinting();
               this.dispose();
            }

            this.setCursor(this.defaultCursor);
         } else if (var1.getSource() == this.printToImageFileGomb) {
            if (this.doPrintToImage(false).errorList.size() < 1) {
               MainPrinter.logPrinting();
            }

         } else {
            this.setButtonState(this.elsoGomb, this.elozoGomb, this.kovetkezoGomb, this.utolsoGomb);
            this.printPreview.setDummySize();
            this.printPreview.repaint();
         }
      }
   }

   public void teljesKepernyo(float var1) {
      this.printPreview.setHeightFactor((double)var1);
      this.printPreview.setDummySize();
   }

   public void stateChanged(ChangeEvent var1) {
      if (!((JSlider)var1.getSource()).getValueIsAdjusting()) {
         if (this.prevSliderValue == this.nagyitas.getMinimum()) {
            if (this.nagyitas.getValue() > this.prevSliderValue) {
               this.prevSliderValue = this.nagyitas.getValue();
               this.teljesKepernyo((float)this.prevSliderValue / 100.0F);
            }
         } else {
            this.prevSliderValue = this.nagyitas.getValue();
            this.teljesKepernyo((float)this.prevSliderValue / 100.0F);
         }

         this.nagyitas.setToolTipText("Nagyítás: " + this.prevSliderValue + "%");
      }

   }

   private void setHolvagyunk() {
      int var1 = ((Lap)this.lapok.elementAt(this.pageIndex)).getLma().lapSzam;
      this.holvagyunk.setText(" " + ((Lap)this.lapok.elementAt(this.pageIndex)).getLma().lapCim + (var1 > 0 ? " [" + (var1 + 1) + "]" : "") + "   Lap: " + (this.pageIndex + 1) + " / " + this.lapok.size());
      this.holvagyunk.setToolTipText(" " + ((Lap)this.lapok.elementAt(this.pageIndex)).getLma().lapCim + (var1 > 0 ? " [" + (var1 + 1) + "]" : "") + "   Lap: " + (this.pageIndex + 1) + " / " + this.lapok.size());
   }

   private void resetMindenKep() {
      for(int var1 = 0; var1 < this.lapok.size(); ++var1) {
         ((Lap)this.lapok.elementAt(var1)).getLma().barkodString = "";
      }

      this.ppIndex.clear();
   }

   private boolean doPrint() {
      this.printJob = PrinterJob.getPrinterJob();
      if (this.printJob.printDialog()) {
         try {
            this.printJob.setPageable(this.book);
            this.printJob.print();
            HashPrintRequestAttributeSet var1 = new HashPrintRequestAttributeSet();

            for(int var2 = 0; var2 < this.uniquePrintablePages.size(); ++var2) {
               this.book = (Book)this.uniquePrintablePages.elementAt(var2);

               try {
                  this.printJob.setPageable(this.book);
                  this.printJob.print(var1);
               } catch (PrinterException var4) {
                  this.hiba(var4.getMessage());
               }
            }

            this.resetMindenKep();
            return true;
         } catch (PrinterException var5) {
            this.hiba(var5.getMessage());
            return false;
         }
      } else {
         return false;
      }
   }

   public Result doPrintToImage(boolean var1) {
      Result var2 = new Result();
      JDialog var3 = null;

      try {
         String var4;
         if (this.loadedFilename.indexOf(".") > 0) {
            var4 = this.loadedFilename.substring(0, this.loadedFilename.indexOf("."));
         } else {
            var4 = this.loadedFilename;
         }

         String var5 = Tools.getTimeStringForFiles();
         Lap var6;
         if (this.book.getNumberOfPages() == 0) {
            var6 = (Lap)((Book)this.uniquePrintablePages.elementAt(0)).getPrintable(0);
         } else {
            var6 = (Lap)this.book.getPrintable(0);
         }

         String var7 = var6.getLma().formId + "_x_" + var5 + ".jpg";
         Vector var8 = new Vector();
         this.fc.setFileSelectionMode(1);

         boolean var9;
         do {
            var9 = true;
            String var10 = null;
            int var11;
            if (!var1) {
               var11 = this.fc.showSaveDialog(this);
            } else {
               var11 = 0;
            }

            if (var11 == 0) {
               File var12 = this.fc.getSelectedFile();
               if (var1) {
                  var12 = new File((String)PropertyList.getInstance().get("prop.usr.naplo"));
               }

               if (var12.exists()) {
                  var10 = var12 + File.separator + var4 + File.separator;
                  var12 = new File(var10);
                  if (!var12.exists()) {
                     if (!var12.mkdir()) {
                        GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem sikerült létrehozni a szükséges könyvtárat", "Nyomtatás", 0);
                        var9 = false;
                     }
                  } else {
                     var9 = var1 || JOptionPane.showOptionDialog(MainFrame.thisinstance, var12.getAbsolutePath() + "\n nevű könyvtár már létezik. Felülírjuk?", "Képfájl mentése", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0;
                     var2.errorList.add("A(z) " + var12.getAbsolutePath() + "\n nevű könyvtárt felülírtuk");
                  }
               } else {
                  if (!var1) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "Ilyen nevű könyvtár nem létezik", "Nyomtatás", 0);
                  } else {
                     var2.errorList.add("Ilyen nevű könyvtár nem létezik: " + var12);
                  }

                  var9 = false;
               }

               if (var9) {
                  if (!var1) {
                     try {
                        if (this.book.getNumberOfPages() > 0 || this.uniquePrintablePages.size() > 0) {
                           this.setCursor(new Cursor(3));
                           var3 = Tools.createInitDialog(this, "Képfájl készítése - türelem, dolgozom...", "Képfájl készítése folyamatban...");
                           var3.setVisible(true);
                        }
                     } catch (Exception var24) {
                        Tools.eLog(var24, 0);
                     }
                  }

                  int var13;
                  for(var13 = 0; var13 < this.book.getNumberOfPages(); ++var13) {
                     try {
                        var6 = (Lap)this.book.getPrintable(var13);
                        String var14 = var6.getLma().formId.replaceAll("/", "_");
                        var14 = var14.replaceAll("\\\\", "_");
                        String var15 = var6.getLma().lapNev.replaceAll("/", "_");
                        var15 = var15.replaceAll("\\\\", "_");
                        var7 = var10 + var14 + "_" + var15 + "_" + var6.getLma().lapSzam + "_" + var5 + ".jpg";
                        this.printToImage(new File(var7), var6);
                        var8.add(var7);
                     } catch (Exception var22) {
                        ErrorList.getInstance().store(MainPrinter.PRINT_EXCEPTION_CODE, "Hiba az oldalkép készítésekor", var22, (Object)null);
                        throw var22;
                     } catch (Error var23) {
                        ErrorList.getInstance().store(MainPrinter.PRINT_ERROR_CODE, "Hiba az oldalkép készítésekor", new Exception(var23.getMessage()), (Object)null);
                        throw new Exception();
                     }
                  }

                  for(var13 = 0; var13 < this.uniquePrintablePages.size(); ++var13) {
                     Book var26 = (Book)this.uniquePrintablePages.elementAt(var13);

                     for(int var27 = 0; var27 < var26.getNumberOfPages(); ++var27) {
                        try {
                           var6 = (Lap)var26.getPrintable(var27);
                           String var16 = var6.getLma().formId.replaceAll("/", "_");
                           var16 = var16.replaceAll("\\\\", "_");
                           String var17 = var6.getLma().lapNev.replaceAll("/", "_");
                           var17 = var17.replaceAll("\\\\", "_");
                           var7 = var10 + var16 + "_" + var17 + "_" + var6.getLma().lapSzam + "_" + var5 + ".jpg";
                           this.printToImage(new File(var7), var6);
                           var8.add(var7);
                        } catch (Exception var20) {
                           ErrorList.getInstance().store(MainPrinter.PRINT_EXCEPTION_CODE, "Hiba az oldalkép készítésekor", var20, (Object)null);
                           throw var20;
                        } catch (Error var21) {
                           ErrorList.getInstance().store(MainPrinter.PRINT_ERROR_CODE, "Hiba az oldalkép készítésekor", new Exception(var21.getMessage()), (Object)null);
                           throw new Exception();
                        }
                     }
                  }

                  try {
                     this.setCursor(this.defaultCursor);
                     var3.dispose();
                  } catch (Exception var19) {
                     Tools.eLog(var19, 0);
                  }

                  MainPrinter.runGC();
                  if (!var1) {
                     this.showJpegCreationLog(var8);
                  } else {
                     var2.errorList.add("A nyomtatvány képét az alábbi fájlokba mentettük:");

                     for(var13 = 0; var13 < var8.size(); ++var13) {
                        var2.errorList.add(var8.elementAt(var13));
                     }
                  }

                  return var2;
               }
            }
         } while(!var9);
      } catch (Exception var25) {
         if (!var1) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiba történt a képek készítésekor!" + (var25 instanceof PrinterException ? " " + var25.getMessage() : ""), "Nyomtatás", 0);
         } else {
            var2.setOk(false);
            var2.errorList.add("Hiba történt a képek készítésekor!" + (var25 instanceof PrinterException ? " " + var25.getMessage() : ""));
         }
      }

      try {
         this.setCursor(this.defaultCursor);
         var3.dispose();
      } catch (Exception var18) {
         Tools.eLog(var18, 0);
      }

      return var2;
   }

   public void printToImage(File var1, Lap var2) throws Exception, Error {
      int var3 = (int)var2.getLma().meret.getWidth();
      int var4 = (int)var2.getLma().meret_mod.getHeight();
      BufferedImage var5 = new BufferedImage(3 * (int)((double)var3 + (double)MainPrinter.nyomtatoMargo * 2.8D), 3 * (int)((double)var4 + (double)MainPrinter.nyomtatoMargo * 2.8D), this.szin);
      Graphics2D var6 = (Graphics2D)var5.getGraphics();
      Color var7 = var6.getColor();
      var6.setColor(Color.WHITE);
      var6.fillRect(0, 0, var5.getWidth(), var5.getHeight());
      var6.setColor(var7);
      var6.translate((double)MainPrinter.nyomtatoMargo * 2.8D, (double)MainPrinter.nyomtatoMargo * 2.8D);
      double var8 = (double)((float)var4 / (float)(var4 + brPlus));
      var6.scale(3.0D, 3.0D * var8);
      FormPrinter var10 = var2.getPrintable();
      var10.setPageindex(var2.getLma().foLapIndex);
      var10.setDynindex(var2.getLma().lapSzam);
      var10.print(var6, var2.getPf(), 0);
      boolean var11 = false;
      if (MainPrinter.getBookModel().epost != null && MainPrinter.getBookModel().epost.equalsIgnoreCase("onlyinternet")) {
         var11 = true;
      }

      if (!MainPrinter.emptyPrint) {
         try {
            var2.extraPrint(var6, Math.max(0.0D, ((Lap)this.lapok.elementAt(this.pageIndex)).getLma().meret_mod.getWidth() - ((Lap)this.lapok.elementAt(this.pageIndex)).getLma().meret.getWidth()), var2.getPf(), var11);
         } catch (Exception var13) {
            System.out.println("Barkód nyomtatás hiba!");
            if (var13 instanceof PrinterException && var13.getMessage().startsWith("*HIBA")) {
               throw (PrinterException)var13;
            }

            throw new PrinterException("Barkód nyomtatás hiba!");
         }
      } else if (var11) {
         Font var12 = var6.getFont();
         var6.setFont(new Font(var6.getFont().getFontName(), 0, 15));
         var6.setColor(Color.RED);
         var6.drawString("Ez a nyomtatvány csak elektronikusan küldhető be!", (int)((double)MainPrinter.nyomtatoMargo * 2.8D) + 10, var5.getHeight());
         var6.setFont(var12);
         var6.setColor(var7);
      }

      ImageIO.write(var5, "jpg", var1);
   }

   private String getTimeString() {
      SimpleDateFormat var1 = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
      return var1.format(Calendar.getInstance().getTime());
   }

   private void showJpegCreationLog(Vector var1) {
      new ErrorDialog(MainFrame.thisinstance, "A nyomtatvány képét az alábbi fájlokba mentettük:", true, false, var1);
   }

   private void hiba(String var1) {
      if (var1 == null) {
         var1 = "Java kivétel!";
      }

      GuiUtil.showMessageDialog(MainFrame.thisinstance, var1, "Nyomtatás hiba", 0);
   }

   private class PrintPreview extends JPanel implements Scrollable {
      PrintPreviewPanel mainParent;
      private double heightFactor;

      public void setHeightFactor(double var1) {
         this.heightFactor = var1;
      }

      public void setDummySize() {
         int var2 = (int)((((Lap)PrintPreviewPanel.this.lapok.elementAt(PrintPreviewPanel.this.pageIndex)).getLma().meret.getWidth() + 20.0D) * this.heightFactor);
         int var1 = (int)((((Lap)PrintPreviewPanel.this.lapok.elementAt(PrintPreviewPanel.this.pageIndex)).getLma().meret.getHeight() + 220.0D) * this.heightFactor);
         Dimension var3 = new Dimension(var2, var1);
         if (PrintPreviewPanel.this.printPreview != null) {
            PrintPreviewPanel.this.printPreview.setPreferredSize(var3);
            this.mainParent.setBackground(Color.WHITE);
            PrintPreviewPanel.this.printPreview.revalidate();
            this.mainParent.ujmeret(var3);
         }

      }

      private PrintPreview(double var2, PrintPreviewPanel var4) {
         this.setLayout(new GridLayout(1, 1));
         this.heightFactor = var2;
         this.mainParent = var4;
         this.setDummySize();
      }

      public void paint(Graphics var1) {
         ((Graphics2D)var1).scale(this.heightFactor, this.heightFactor);
         super.paint(var1);
         int var2 = (int)(20.0D / this.heightFactor);
         int var3 = (int)(240.0D / this.heightFactor);
         Color var4 = var1.getColor();
         var1.setColor(Color.WHITE);
         var1.fillRect(0, 0, PrintPreviewPanel.this.bImage.getWidth() + var2, PrintPreviewPanel.this.bImage.getHeight() + var3);
         var1.setColor(var4);
         String var5 = "Kitöltő verzió:" + "v.3.44.0".substring(2) + "   Nyomtatvány verzió:" + MainPrinter.sablonVerzio + (MainPrinter.nyomtatvanyHibas && MainPrinter.nyomtatvanyEllenorzott ? "   Hibás" : "");
         String var6 = "Dátum: " + PrintPreviewPanel.datum;
         boolean var7 = false;
         if (MainPrinter.getBookModel().epost != null && MainPrinter.getBookModel().epost.equalsIgnoreCase("onlyinternet")) {
            var7 = true;
         }

         if (!MainPrinter.emptyPrint) {
            try {
               if (!var7 && !MainPrinter.emptyPrint && !MainPrinter.notVKPrint && !MainPrinter.betaVersion && !MainPrinter.papirosBekuldesTiltva && MainPrinter.getBookModel().getOperationMode().equals("0") && !MainPrinter.isTemplateDisabled) {
                  this.extraPrint(PrintPreviewPanel.this.bImage.getGraphics(), 40.0D, (double)(PrintPreviewPanel.this.bImage.getHeight() - 30));
               } else {
                  this.extraPrint(PrintPreviewPanel.this.bImage.getGraphics(), 40.0D, (double)(PrintPreviewPanel.this.bImage.getHeight() - 200));
               }
            } catch (Exception var9) {
               Tools.eLog(var9, 0);
            }
         }

         var1.drawImage(PrintPreviewPanel.this.bImage, 10, 10, this);
      }

      public void extraPrint(Graphics var1, double var2, double var4) throws Exception {
         Font var6 = var1.getFont();
         Color var7 = var1.getColor();
         byte var8 = 13;
         Font var9 = new Font("Arial", 1, var8);
         var1.setColor(Color.BLACK);
         String var10 = MainPrinter.getFootText();
         String var11 = var10 + (MainPrinter.nyomtatvanyHibas && MainPrinter.nyomtatvanyEllenorzott ? "  Hibás" : "");
         String var12 = "Dátum: " + PrintPreviewPanel.datum;
         if (MainPrinter.isAutofillMode()) {
            var11 = "A nyomtatvány jelen kitöltöttség mellett nem küldhető be!";
         }

         if (MainPrinter.nemKuldhetoBeSzoveg) {
            var11 = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány papír alapon nem küldhető be!";
         }

         if (MainPrinter.papirosBekuldesTiltva) {
            var11 = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány jelen kitöltöttség mellett papír alapon nem küldhető be!";
         }

         if (MainPrinter.betaVersion) {
            var11 = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány ebben a verzióban nem küldhető be!";
         }

         if (MainPrinter.check_version > -1) {
            var11 = "Ny.v.:" + MainPrinter.sablonVerzio + " " + BookModel.CHECK_VALID_MESSAGES[MainPrinter.check_version];
         }

         if (!MainPrinter.getBookModel().getOperationMode().equals("0")) {
            var11 = "Bárkód: " + MainPrinter.getBookModel().getBarcode();
            var12 = "";
         }

         var1.setFont(var9);
         byte var13 = -100;
         var1.drawString(var11, (int)var2, (int)var4);
         int var14 = (int)var2 + SwingUtilities.computeStringWidth(var1.getFontMetrics(var9), var11);
         int var15 = (int)((Lap)PrintPreviewPanel.this.lapok.elementAt(PrintPreviewPanel.this.pageIndex)).getLma().meret_mod.getWidth() - SwingUtilities.computeStringWidth(var1.getFontMetrics(var9), var12) + var13;
         if (var14 > var15) {
            var15 = var14 + 20;
         }

         var1.drawString(var12, var15, (int)var4);
         var1.setFont(var6);
         var1.setColor(var7);
      }

      public boolean getScrollableTracksViewportHeight() {
         return false;
      }

      public boolean getScrollableTracksViewportWidth() {
         return false;
      }

      public Dimension getPreferredScrollableViewportSize() {
         return null;
      }

      public int getScrollableBlockIncrement(Rectangle var1, int var2, int var3) {
         return 50;
      }

      public int getScrollableUnitIncrement(Rectangle var1, int var2, int var3) {
         return 10;
      }

      // $FF: synthetic method
      PrintPreview(double var2, PrintPreviewPanel var4, Object var5) {
         this(var2, var4);
      }
   }
}
