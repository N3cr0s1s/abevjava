package hu.piller.enykp.print;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfWriter;
import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.filesaver.xml.ErrorListListener4XmlSave;
import hu.piller.enykp.alogic.fileutil.FileNameResolver;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.orghandler.OrgResource;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.alogic.settingspanel.printer.PrinterSettings;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.gui.model.SimpleViewModel;
import hu.piller.enykp.gui.viewer.FormPrinter;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IOsHandler;
import hu.piller.enykp.interfaces.IResult;
import hu.piller.enykp.print.simpleprint.ExtendedPdfHandler;
import hu.piller.enykp.print.simpleprint.PageTitle;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.Md5Hash;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import hu.piller.enykp.util.icon.ENYKIconSet;
import hu.piller.enykp.util.oshandler.OsFactory;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MainPrinter implements ItemListener {
   static final String RESOURCE_NAME = "Nyomtató";
   private static final String GOMB_SZOVEG_MIND = "Mindet kijelöl";
   private static final String GOMB_SZOVEG_EGYIKSEM = "Minden kijelölést töröl";
   private static final String GOMB_SZOVEG_NYOMTAT = "Kijelöltek nyomtatása";
   private static final String GOMB_SZOVEG_MEGSEM = "Mégsem";
   private static final String GOMB_SZOVEG_PDF = "PDF fájl készítése";
   private static final String GOMB_SZOVEG_KIVONATOLT_PDF = "Kivonatolt PDF fájl";
   private static final String GOMB_SZOVEG_KIVONATOLT_PDF_PREVIEW = "Kivonatolt nyomtatási kép";
   public static final int PRINT_TARGET_PRINTER = 0;
   public static final int PRINT_TARGET_PDF = 1;
   public static final Long PRINT_ERROR_CODE = new Long(14000L);
   public static final Long PRINT_EXCEPTION_CODE = new Long(14001L);
   public JCheckBox[] lapvalasztoCB;
   private Hashtable kiegInfoACBhoz;
   private Hashtable indexInfoACBhoz;
   private Lap[] lapok;
   private int lapszam;
   private static int cSeq = 0;
   public static int nyomtatoMargo;
   static String pdfDir;
   public String pdfFilename4Save;
   private long startL = 946718629940L;
   public String nyomtatvanyCim = "Nyomtatvány";
   public static PrinterJob printJob;
   public static PageFormat defaultPageFormat4htmlPrint;
   private Book book;
   public static boolean nyomtatvanyHibas;
   public static boolean nyomtatvanyEllenorzott;
   public static String sablonVerzio;
   public static String barkod_NyomtatvanyAdatok;
   public static String barkod_FixFejlecAdatok;
   public static boolean emptyPrint = false;
   public static boolean hasFatalError = false;
   public static boolean notVKPrint = false;
   public static boolean elektronikus = false;
   public static boolean kontroll = false;
   public static boolean betaVersion = false;
   public static int check_version = -1;
   public static String check_version_message = "";
   public static String formId = null;
   public static String mainLabel = null;
   private boolean csoport = false;
   private PrintService ps = null;
   int ciklusVege = 1;
   int ciklusEleje = 0;
   Vector csoportosHibak;
   public static BookModel bookModel;
   private FormModel formModel;
   public static String orgPrefix;
   private boolean kivonatolt = false;
   public static boolean kivonatoltBarkoddal = false;
   public static boolean nemKuldhetoBeSzoveg = false;
   public static boolean kellFejlec = false;
   public static boolean kivonatoltanBekuldheto = false;
   public static boolean papirosBekuldesTiltva = false;
   public static boolean isTemplateDisabled = false;
   public static boolean voltEEllenorzesNyomtatasElott = true;
   private static int selectedRadio;
   public boolean disableSPDialog = false;
   public boolean enableSP = true;
   public boolean fromIcon;
   public static String message4TheMasses = "";
   public static String fontName = "Arial";
   public static String autoFillPdfPrevFileName = null;
   private static HashSet pdfPreviewFilesInSession = new HashSet();
   public static final String ATTR_BR_LINE_BREAK = "line_break";
   public static final String ATTR_KV_PRINT_FONT_SIZE = "kv_fontsize";
   public static final String VALUE_KV_PRINT_FONT_SIZE = "10";
   public static final String ATTR_KV_PRINT_CELLPADDING = "kv_padding";
   public static final String VALUE_KV_PRINT_CELLPADDING = "0";
   public static String kivPrTdFontSize = "10";
   public static String kivPrTdCellPadding = "0";
   public static String kivPrCenterTdFontSize = "8";
   private String backgroundImage = "bg.gif";
   public static String thBgColor = "#cccccc";
   public static final String ONE_CELL_WIDTH = "200";
   public static String headerFromJar;
   public static String headerImageName;
   private static final String DOCINFO_ATTR_NAME_RELEASE = "release";
   private static final String DOCINFO_ATTR_VALUE_TESZT = "teszt";
   public Document document;
   PdfWriter writer;
   public boolean printerAlreadySelected = false;
   MainPrinter.TempObject4SuperPages object4UniquePrint = new MainPrinter.TempObject4SuperPages();
   boolean uniquePrint = false;
   public boolean batchPrint2Jpg = false;
   public boolean batchPrint2Pdf = false;
   public boolean batchPrint2OnePdf = false;
   public int batchPrintSimpleMode = -1;
   public Result batchJpgPrint;
   private static final Runtime s_runtime = Runtime.getRuntime();

   public static int getSeq() {
      return cSeq;
   }

   public static boolean isAutofillMode() {
      return bookModel.autofill;
   }

   public void itemStateChanged(ItemEvent var1) {
      try {
         int var2 = Integer.parseInt(((JCheckBox)var1.getSource()).getName());
         this.lapok[var2].getLma().setNyomtatando(((JCheckBox)var1.getSource()).isSelected());
         if (((JCheckBox)var1.getSource()).isSelected()) {
            this.lapok[var2].getLma().nyomtathato = true;
            this.lapok[var2].getLma().printable = true;
         }

         this.lapok[var2].getLma().forcedByUser = ((JCheckBox)var1.getSource()).isSelected();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public MainPrinter(BookModel var1, Vector var2, PrintService var3, boolean var4) {
      this.construct(var1, var4);
      this.csoportosHibak = var2;
      this.ps = var3;
      this.batchPrint2Jpg = false;
   }

   public MainPrinter(BookModel var1) {
      this.construct(var1, false);
      this.csoportosHibak = new Vector();
   }

   public static BookModel getBookModel() {
      return bookModel;
   }

   public void initFromIcon(boolean var1) throws Exception {
      this.fromIcon = true;
      this.init(var1, false);
   }

   public void initFromMenu(boolean var1) throws Exception {
      this.fromIcon = false;
      this.init(var1, false);
   }

   public void init(boolean var1, boolean var2) throws Exception {
      try {
         this.printerAlreadySelected = this.csoport;
         this.uniquePrint = false;
         autoFillPdfPrevFileName = null;
         this.kozosInit();
         emptyPrint = var1;
         hasFatalError = var2;
         this.ciklusEleje = bookModel.cc.getActiveObjectindex();
         this.ciklusVege = this.ciklusEleje + 1;
         if (this.csoport || var1) {
            this.ciklusVege = bookModel.cc.size();
            this.ciklusEleje = 0;
         }

         elektronikus = false;
         if (bookModel.epost != null && bookModel.epost.equalsIgnoreCase("onlyinternet")) {
            elektronikus = true;
         }

         this.checkAndSetPdfDir((Result)null);
         this.batchJpgPrint = new Result();
         int var3 = this.ciklusEleje;

         while(true) {
            if (var3 >= this.ciklusVege) {
               return;
            }

            if (this.csoport || var1) {
               bookModel.cc.setActiveObject(bookModel.cc.get(var3));
            }

            Elem var4 = (Elem)bookModel.cc.getActiveObject();
            IDataStore var5 = (IDataStore)var4.getRef();
            this.formModel = bookModel.get(((Elem)bookModel.cc.get(var3)).getType());
            this.setKVPrintParams();
            if (!this.csoport && !var1) {
               Result var6 = this.isSavable();
               if (!var6.isOk()) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatványt nem sikerült menteni.\n" + var6.errorList.get(0), "Mentés hiba", 1);
                  return;
               }

               nyomtatvanyEllenorzott = false;
               nyomtatvanyHibas = false;
               if (!bookModel.autofill) {
                  if (!MainFrame.thisinstance.mp.readonlymode && !MainFrame.readonlymodefromubev && !this.check()) {
                     nyomtatvanyEllenorzott = true;
                     nyomtatvanyHibas = true;
                     return;
                  }
               } else {
                  nyomtatvanyEllenorzott = true;
                  nyomtatvanyHibas = true;
                  hasFatalError = true;
                  autoFillPdfPrevFileName = "";
               }
            }

            if (this.csoport) {
               try {
                  nyomtatvanyHibas = (Boolean)this.csoportosHibak.get(var3);
                  nyomtatvanyEllenorzott = true;
               } catch (Exception var36) {
                  nyomtatvanyHibas = false;
               }
            }

            this.setConditions();
            Utils var7 = new Utils(bookModel);
            formId = this.formModel.id;
            mainLabel = this.formModel.mainlabel;
            if (formId == null) {
               throw new Exception();
            }

            Vector var44 = var7.getMetadataV(var3);
            this.lapok = new Lap[var44.size()];
            int[] var8 = (int[])((int[])((Elem)bookModel.cc.get(var3)).getEtc().get("pagecounts"));
            Vector var9 = new Vector();

            int var13;
            for(int var10 = 0; var10 < var8.length; ++var10) {
               String var11 = bookModel.get(formId).get(var10).pid;
               Object[] var12 = CalculatorManager.getInstance().check_page(var11);

               for(var13 = 0; var13 < var8[var10]; ++var13) {
                  var9.add(var12[1]);
               }
            }

            Vector var45 = new Vector();

            boolean var15;
            int var16;
            int var46;
            for(var46 = 0; var46 < var8.length; ++var46) {
               PageModel var47 = bookModel.get(formId).get(var46);
               var13 = var47.role;
               int var14 = var47.getmask();
               var15 = (var13 & var14) == 0;

               for(var16 = 0; var16 < var8[var46]; ++var16) {
                  var45.add(var15);
               }
            }

            for(var46 = 0; var46 < this.lapok.length; ++var46) {
               this.lapok[var46] = new Lap();
               this.lapok[var46].setLma((LapMetaAdat)var44.get(var46));
               if (var1) {
                  this.lapok[var46].getLma().setNyomtatando(!(Boolean)var45.get(var46));
               } else {
                  Utils.needPrint(this.lapok[var46], var5, formId);
               }

               if (this.lapok[var46].getLma().lapSzam > 0 && this.lapok[var46].getLma().isNyomtatando()) {
                  this.lapok[var46 - this.lapok[var46].getLma().lapSzam].getLma().setNyomtatando(true);
               }

               this.lapok[var46].getLma().isGuiEnabled = (Boolean)var9.get(var46);
               this.lapok[var46].getLma().disabledByRole = (Boolean)var45.get(var46);
            }

            this.lapszam = this.formModel.size();
            this.nyomtatvanyCim = this.formModel.name;
            this.disableSPDialog = false;
            if (this.fromIcon && !this.disableSPDialog && kivonatoltBarkoddal && kivonatoltanBekuldheto) {
               JDialog var48 = this.getSPSettingsDialog(this.enableSP, this.disableSPDialog);
               var48.setDefaultCloseOperation(2);
               var48.setVisible(true);
            } else {
               selectedRadio = 3;
            }

            if (this.csoport && this.batchPrintSimpleMode > -1) {
               if (!this.kivonatolt) {
                  this.deInit();
                  throw new PrinterException("A nyomtatvány nem nyomtatható kivonatolt módon!");
               }

               selectedRadio = 1;
            }

            if (this.batchPrint2OnePdf) {
               selectedRadio = 3;
            }

            if (selectedRadio == 0) {
               return;
            }

            label744: {
               if (selectedRadio < 3) {
                  if ((!this.csoport || !(this.batchPrintSimpleMode > -1 & this.kivonatolt)) && (!this.enableSP || !this.fromIcon || !kivonatoltBarkoddal || !kivonatoltanBekuldheto)) {
                     this.panelEpites(var3, true);
                  } else {
                     Vector var50 = new Vector();
                     String var49 = this.getPdfFilename(var3, true);
                     String var51 = "";
                     this.panelEpites(var3, false);
                     boolean var52 = this.handleOnlyUniqueAutoPrint();
                     var15 = this.doPrintPreview(var3, selectedRadio == 1);
                     boolean var53 = false;

                     try {
                        this.handlePrintActions(true);
                     } catch (MainPrinter.NoSelectedPageException var39) {
                        if (!var15) {
                           if (!this.csoport) {
                              GuiUtil.showMessageDialog(MainFrame.thisinstance, var39.getMessage(), "Nyomtatás", 1);
                           } else {
                              try {
                                 this.batchJpgPrint.errorList.add(" - Nincs nyomtatásra kijelölt oldal");
                              } catch (Exception var33) {
                                 Tools.eLog(var33, 0);
                              }
                           }
                           break;
                        }
                     }

                     if (!var15) {
                        var51 = "A kivonatolt nyomtatvány képe " + var49 + " néven elkészült.\n";
                     }

                     if (this.batchPrintSimpleMode > -1) {
                        if (!var15) {
                           var16 = this.handleSPPrint(var3, this.batchPrintSimpleMode, var49);
                           if (var16 == -1) {
                              return;
                           }

                           if (var16 == 1 && var3 < this.ciklusVege - 1) {
                              break label744;
                           }
                        }
                     } else if (!var15) {
                        var16 = this.handleSPPrint(var3, selectedRadio - 1, var49);
                        if (var16 == -1) {
                           return;
                        }

                        if (var16 == 1 && var3 < this.ciklusVege - 1) {
                           break label744;
                        }
                     }

                     this.handleAutoPrint();
                     int var17 = 1;
                     int var18;
                     if (this.object4UniquePrint.normalPrintAfterKPrint && autoFillPdfPrevFileName == null && this.handleKPageOther(var3) && this.object4UniquePrint.uniquePrintablePages.size() > 0) {
                        for(var18 = 0; var18 < this.object4UniquePrint.uniquePrintablePages.size(); ++var18) {
                           try {
                              String var19;
                              try {
                                 var19 = var49.substring(0, var49.lastIndexOf(".pdf")) + "_melleklet_" + (var18 + 1) + ".pdf";
                              } catch (Exception var35) {
                                 var19 = var49 + "_melleklet_" + (var18 + 1) + ".pdf";
                              }

                              var50.add(var19);
                              this.book = (Book)this.object4UniquePrint.uniquePrintablePages.elementAt(var18);
                              if (this.batchPrintSimpleMode > -1) {
                                 var17 = this.doPrint(this.batchPrintSimpleMode, var19, var3);
                              } else {
                                 var17 = this.doPrint(selectedRadio - 1, var19, var3);
                              }

                              if (var17 <= 0) {
                                 if (var17 == -2 && !this.csoport) {
                                    GuiUtil.showMessageDialog(MainFrame.thisinstance, "A fájlt nem lehet létrehozni!\nValószínűleg éppen meg van nyitva a pdf olvasóban.", "Pdf fájl készítés", 1);
                                 }

                                 return;
                              }

                              if (this.csoport && this.batchPrintSimpleMode == 1) {
                                 this.batchJpgPrint.errorList.add("Ön a(z) " + ((Lap)this.object4UniquePrint.uniqueDataPages.elementAt(var18)).getLma().lapCim + " lapot is kitöltötte, melyet csak a 'Nyomtatványképes' módon lehet kinyomtatni.\nEz a PDF fájl " + var19 + " néven elkészült.\n");
                              }
                           } catch (NumberFormatException var37) {
                              Tools.eLog(var37, 0);
                           } catch (Exception var38) {
                              var38.printStackTrace();
                              this.hiba("Nyomtatás hiba !" + (message4TheMasses.equals("") ? "" : "\n" + message4TheMasses), var38, 1);
                              message4TheMasses = "";
                           }
                        }
                     }

                     this.object4UniquePrint.normalPrintAfterKPrint = false;
                     if (var1) {
                        try {
                           this.deInit();
                        } catch (Exception var34) {
                           Tools.eLog(var34, 0);
                        }
                     }

                     if (!this.csoport && var17 > 0 && selectedRadio == 2) {
                        if (!var52) {
                           var51 = "A kivonatolt nyomtatvány képe " + var49 + " néven elkészült.\n";
                        }

                        if (var50.size() > 0) {
                           for(var18 = 0; var18 < var50.size(); ++var18) {
                              var51 = var51 + "Ön a(z) " + ((Lap)this.object4UniquePrint.uniqueDataPages.elementAt(var18)).getLma().lapCim + " lapot is kitöltötte, melyet csak a 'Nyomtatványképes' módon lehet kinyomtatni.\nEz a PDF fájl " + (String)var50.elementAt(var18) + " néven elkészült.\n";
                           }
                        }

                        var51 = var51 + "Megpróbáljuk betölteni az alapértelmezett pdf olvasóba a dokumentumot?";
                        if (this.showPdfsAreReady(var51) == 0) {
                           if (autoFillPdfPrevFileName != null) {
                              this.executePdfReader(autoFillPdfPrevFileName);
                           } else {
                              this.executePdfReader(var49);
                              if (var50.size() > 0) {
                                 for(var18 = 0; var18 < var50.size(); ++var18) {
                                    this.executePdfReader((String)var50.elementAt(var18));
                                 }
                              }
                           }
                        }
                     }
                  }
               } else {
                  this.panelEpites(var3, true);
               }

               Utils.setBookModel((BookModel)null);
               var7 = null;
            }

            ++var3;
         }
      } catch (MainPrinter.PrintCancelledException var40) {
         Tools.eLog(var40, 0);
         return;
      } catch (PrinterException var41) {
         throw var41;
      } catch (Exception var42) {
         var42.printStackTrace();
         this.hiba("panel inicializálási hiba!", var42, 0);
         throw new Exception("Inicializálási hiba (2)");
      } finally {
         printJob = null;
      }

   }

   private void handleAutoPrint() {
      this.object4UniquePrint.uniqueDataPages = new Vector();
      this.object4UniquePrint.uniquePrintablePages = new Vector();

      for(int var1 = 0; var1 < this.lapok.length; ++var1) {
         if (this.lapok[var1].getLma().lapSzam <= 0) {
            try {
               if (this.lapok[var1].getLma().uniquePrintable) {
                  this.object4UniquePrint.uniqueDataPages.add(this.lapok[var1]);
                  Book var2 = new Book();
                  FormPrinter var3 = new FormPrinter(bookModel);
                  this.lapok[var1].setPrintable(var3);
                  this.lapok[var1].setxArany(100.0D);
                  var2.append(this.lapok[var1], this.lapok[var1].getPf());
                  this.object4UniquePrint.uniquePrintablePages.add(var2);
               }
            } catch (Exception var4) {
               Tools.eLog(var4, 0);
            }
         }
      }

   }

   private void deInit() {
      bookModel = null;
      this.formModel = null;

      try {
         for(int var1 = 0; var1 < this.lapok.length; ++var1) {
            this.lapok[var1].setPrintable((FormPrinter)null);
            this.lapok[var1].setLma((LapMetaAdat)null);
            this.lapok[var1] = null;
         }
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
      }

      this.lapok = null;
      this.printerAlreadySelected = false;
      printJob = null;
      this.ps = null;
      defaultPageFormat4htmlPrint = null;
      this.delTempFiles();
   }

   private void panelEpites(final int var1, boolean var2) throws Exception {
      final MainFrame var3 = MainFrame.thisinstance;
      boolean var4 = true;
      JPanel var5 = null;
      JPanel var6 = null;
      int var7 = 0;
      int var8 = 0;
      final JDialog var9 = new JDialog(var3, "Nyomtatás", true);
      var9.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent var1x) {
            try {
               if (MainPrinter.bookModel.forms.size() <= 1 && var1 == MainPrinter.this.ciklusVege - 1) {
                  MainPrinter.this.deInit();
               }
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

            var9.dispose();
            if (MainPrinter.emptyPrint && var1 == MainPrinter.this.ciklusVege - 1) {
               MainFrame.thisinstance.mp.makeempty();
            }

         }
      });
      if (!this.csoport) {
         var7 = var3.getX() + var3.getWidth() / 2 - 250;
         if (var7 < 0) {
            var7 = 0;
         }

         var8 = var3.getY() + var3.getHeight() / 2 - 200;
         if (var8 < 0) {
            var8 = 0;
         }

         var5 = new JPanel((LayoutManager)null, true);
         var5.setLayout(new BorderLayout(0, 10));
         BevelBorder var10 = new BevelBorder(0);
         var5.setBorder(var10);
         JLabel var11 = new JLabel(this.nyomtatvanyCim);
         var6 = new JPanel();
         var6.add(var11);
      }

      JPanel var47 = new JPanel();
      var47.setLayout(new BoxLayout(var47, 1));
      this.lapvalasztoCB = new JCheckBox[this.lapszam];
      this.kiegInfoACBhoz = new Hashtable();
      this.indexInfoACBhoz = new Hashtable();
      int var48 = this.checkBoxok(var47, !var2);
      ENYKIconSet var12 = ENYKIconSet.getInstance();
      if (!this.csoport) {
         Insets var13 = new Insets(4, 4, 4, 4);
         JButton var14 = new JButton("Mindet kijelöl", var12.get("print_anyk_mindent_kijelol"));
         var14.setToolTipText("Mindet kijelöl");
         var14.setHorizontalAlignment(2);
         var14.setFocusPainted(false);
         var14.setMargin(var13);
         ActionListener var15 = new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               MainPrinter.this.mindValtozik(MainPrinter.this.lapvalasztoCB, true);
            }
         };
         var14.addActionListener(var15);
         JButton var16 = new JButton("Minden kijelölést töröl", var12.get("print_anyk_kijeloles_torlese"));
         var16.setToolTipText("Minden kijelölést töröl");
         var16.setHorizontalAlignment(2);
         var16.setFocusPainted(false);
         var16.setMargin(var13);
         ActionListener var17 = new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               MainPrinter.this.mindValtozik(MainPrinter.this.lapvalasztoCB, false);
            }
         };
         var16.addActionListener(var17);
         JButton var18 = new JButton("Kijelöltek nyomtatása", var12.get("print_anyk_kijeloltek_nyomtatasa"));
         var18.setToolTipText("Kijelöltek nyomtatása");
         var18.setHorizontalAlignment(2);
         var18.setFocusPainted(false);
         var18.setMargin(var13);
         ActionListener var20 = new ActionListener() {
            public void actionPerformed(ActionEvent var1x) {
               try {
                  MainPrinter.this.handlePrintDialog();
                  MainPrinter.this.handlePrintActions();
                  MainPrinter.logPrinting();
                  boolean var2 = MainPrinter.this.doPrintPreview(var1, true);
                  int var3x = 1;
                  if (!var2) {
                     var3x = MainPrinter.this.doPrint(0, (String)null, -1);
                  }

                  if (var3x > 0) {
                     if (var3x == 3) {
                        if (!MainPrinter.this.csoport) {
                           GuiUtil.showMessageDialog(MainFrame.thisinstance, MainPrinter.message4TheMasses, "Nyomtatás", 1);
                        }

                        MainPrinter.message4TheMasses = "";
                     }

                     if (MainPrinter.this.uniquePrint) {
                        MainPrinter.this.handleKPageOther(var1);
                        boolean var4 = MainPrinter.this.object4UniquePrint.normalPrintAfterKPrint;
                        MainPrinter.this.object4UniquePrint.normalPrintAfterKPrint = true;
                        if (MainPrinter.this.object4UniquePrint.uniquePrintablePages != null) {
                           for(int var5 = 0; var5 < MainPrinter.this.object4UniquePrint.uniquePrintablePages.size(); ++var5) {
                              MainPrinter.this.book = (Book)MainPrinter.this.object4UniquePrint.uniquePrintablePages.elementAt(var5);
                              var3x = MainPrinter.this.doPrint(0, (String)null, -1);
                              MainPrinter.this.object4UniquePrint.normalPrintAfterKPrint = var4;
                              if (var3x == 3) {
                                 if (!MainPrinter.this.csoport) {
                                    GuiUtil.showMessageDialog(MainFrame.thisinstance, MainPrinter.message4TheMasses, "Nyomtatás", 1);
                                 }

                                 MainPrinter.message4TheMasses = "";
                              }
                           }
                        }
                     }
                  } else {
                     if (var3x != -2) {
                        throw new Exception();
                     }

                     if (!MainPrinter.this.csoport) {
                        GuiUtil.showMessageDialog(MainFrame.thisinstance, "A fájlt nem lehet létrehozni!\nValószínűleg éppen meg van nyitva a pdf olvasóban.", "Pdf fájl készítés", 1);
                     }
                  }

                  if (MainPrinter.emptyPrint) {
                     try {
                        if (var1 == MainPrinter.this.ciklusVege - 1) {
                           MainPrinter.this.deInit();
                        }
                     } catch (Exception var7) {
                        Tools.eLog(var7, 0);
                     }

                     var9.dispose();
                     if (var1 == MainPrinter.this.ciklusVege - 1) {
                        MainFrame.thisinstance.mp.makeempty();
                     }
                  }
               } catch (MainPrinter.PrintCancelledException var8) {
                  Tools.eLog(var8, 0);
               } catch (MainPrinter.NoSelectedPageException var9x) {
                  if (!MainPrinter.this.csoport) {
                     GuiUtil.showMessageDialog(var3, var9x.getMessage(), "Nyomtatás", 1);
                  } else {
                     try {
                        MainPrinter.this.batchJpgPrint.errorList.add(" - Nincs nyomtatásra kijelölt oldal");
                     } catch (Exception var6) {
                        Tools.eLog(var6, 0);
                     }
                  }
               } catch (NumberFormatException var10) {
                  Tools.eLog(var10, 0);
               } catch (Exception var11) {
                  var11.printStackTrace();
                  MainPrinter.this.hiba("Nyomtatás hiba !" + (MainPrinter.message4TheMasses.equals("") ? "" : "\n" + MainPrinter.message4TheMasses), var11, 1);
                  MainPrinter.message4TheMasses = "";
               }

            }
         };
         var18.addActionListener(var20);
         JButton var21 = new JButton("Mégsem", var12.get("print_anyk_megse"));
         var21.setToolTipText("Mégsem");
         var21.setHorizontalAlignment(2);
         var21.setFocusPainted(false);
         var21.setMargin(var13);
         ActionListener var22 = new ActionListener() {
            public void actionPerformed(ActionEvent var1x) {
               try {
                  if (MainPrinter.bookModel.forms.size() <= 1 && var1 == MainPrinter.this.ciklusVege - 1) {
                     MainPrinter.this.deInit();
                  }
               } catch (Exception var3) {
                  Tools.eLog(var3, 0);
               }

               var9.dispose();
               if (MainPrinter.emptyPrint && var1 == MainPrinter.this.ciklusVege - 1) {
                  MainFrame.thisinstance.mp.makeempty();
               }

            }
         };
         var21.addActionListener(var22);
         JButton var23 = new JButton("Beállítások", var12.get("print_anyk_beallitasok"));
         var23.setToolTipText("Beállítások");
         var23.setHorizontalAlignment(2);
         var23.setFocusPainted(false);
         var23.setMargin(var13);
         JButton var24 = new JButton("Nyomtatási kép", var12.get("print_anyk_nyomtatasi kep"));
         var24.setToolTipText("Nyomtatási kép");
         var24.setHorizontalAlignment(2);
         var24.setFocusPainted(false);
         var24.setMargin(var13);
         ActionListener var25 = new ActionListener() {
            public void actionPerformed(ActionEvent var1x) {
               if (MainPrinter.this.vankijeloltCB()) {
                  try {
                     MainPrinter.this.handlePrintActions();
                  } catch (MainPrinter.NoSelectedPageException var7) {
                     Tools.eLog(var7, 0);
                  }

                  try {
                     PrintPreviewPanel var2 = MainPrinter.this.initWithMonitor(var9, var1);

                     try {
                        var2.loadedFilename = MainPrinter.bookModel.cc.getLoadedfile().getName();
                     } catch (Exception var5) {
                        var2.loadedFilename = MainPrinter.bookModel.loadedfile.getName();
                     }

                     var2.setVisible(true);
                  } catch (Exception var6) {
                     Tools.eLog(var6, 0);
                  }
               } else if (!MainPrinter.this.csoport) {
                  GuiUtil.showMessageDialog(var3, "Nincs nyomtatásra kijelölt oldal!", "Nyomtatás", 1);
               } else {
                  try {
                     MainPrinter.this.batchJpgPrint.errorList.add(" - Nincs nyomtatásra kijelölt oldal");
                  } catch (Exception var4) {
                     Tools.eLog(var4, 0);
                  }
               }

            }
         };
         var24.addActionListener(var25);
         ActionListener var26 = new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               final JDialog var2 = new JDialog(MainFrame.thisinstance, "Beállítás", true);
               var2.getContentPane().setLayout(new BorderLayout(10, 10));
               final PrinterSettings var3 = new PrinterSettings();
               int var4 = 40 + GuiUtil.getW(new JButton(), "...") + GuiUtil.getW(new JButton(), "Törlés") + GuiUtil.getW("A PDF állományok mentési könyvtára: ") + GuiUtil.getW("WWWWWWWWWWWWWWWWWWWWWWWWW");
               Dimension var5 = new Dimension(var4, 22 * (GuiUtil.getCommonItemHeight() + 4));
               var2.setPreferredSize(var5);
               var2.setSize(var5);
               var2.setLocationRelativeTo(MainFrame.thisinstance);

               try {
                  MainPrinter.this.loadSettings(var2);
               } catch (Exception var9) {
                  var9.printStackTrace();
               }

               var2.getContentPane().add(var3, "Center");
               JPanel var6 = new JPanel();
               JButton var7 = new JButton("Rendben");
               var7.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent var1) {
                     try {
                        MainPrinter.this.saveSettings(var2);
                     } catch (Exception var3x) {
                        var3x.printStackTrace();
                     }

                     var3.save();
                     var2.dispose();
                  }
               });
               int var8 = var2.getWidth() / 2 - 55;
               var7.setBounds(var8, 170, 100, 20);
               var6.add(var7);
               var2.getContentPane().add(var6, "South");
               var2.setVisible(true);
            }
         };
         var23.addActionListener(var26);
         JButton var27 = new JButton("PDF fájl készítése", var12.get("print_anyk_pdf_file_keszitese"));
         var27.setToolTipText("PDF fájl készítése");
         var27.setHorizontalAlignment(2);
         var27.setFocusPainted(false);
         var27.setMargin(var13);
         ActionListener var28 = new ActionListener() {
            public void actionPerformed(ActionEvent var1x) {
               try {
                  if (!MainPrinter.this.checkAndSetPdfDir((Result)null)) {
                     return;
                  }

                  MainPrinter.this.handlePrintActions();
                  MainPrinter.this.doPrintPreview(var1, false);
                  MainPrinter.logPrinting();
                  String var2 = MainPrinter.this.getPdfFilename(0, false);
                  int var3x = 1;
                  boolean var4 = MainPrinter.this.checkIfOnlyUniquePrintablePages();
                  if (!var4) {
                     var3x = MainPrinter.this.doPrint(1, var2, -1);
                  }

                  Vector var5 = new Vector();
                  Vector var6 = new Vector();
                  if (var3x > 0) {
                     if (!MainPrinter.this.csoport) {
                        if (var3x == 3) {
                           GuiUtil.showMessageDialog(MainFrame.thisinstance, MainPrinter.message4TheMasses, "Pdf fájl készítés", 1);
                        }

                        if (!var4) {
                           var6.add("A PDF fájl " + var2 + " néven elkészült.\n");
                           var5.add(var2);
                        }
                     }

                     String var7;
                     if (MainPrinter.this.uniquePrint) {
                        MainPrinter.this.handleKPageOther(var1);
                        boolean var8 = MainPrinter.this.object4UniquePrint.normalPrintAfterKPrint;
                        MainPrinter.this.object4UniquePrint.normalPrintAfterKPrint = true;
                        if (MainPrinter.this.object4UniquePrint.uniquePrintablePages.size() > 0) {
                           for(int var9x = 0; var9x < MainPrinter.this.object4UniquePrint.uniquePrintablePages.size(); ++var9x) {
                              MainPrinter.this.book = (Book)MainPrinter.this.object4UniquePrint.uniquePrintablePages.elementAt(var9x);
                              var7 = var2.substring(0, var2.length() - 4) + "_melleklet_" + (var9x + 1) + ".pdf";
                              var3x = MainPrinter.this.doPrint(1, var7, -1);
                              MainPrinter.this.object4UniquePrint.normalPrintAfterKPrint = var8;
                              if (!MainPrinter.this.csoport) {
                                 if (var3x == 3) {
                                    GuiUtil.showMessageDialog(MainFrame.thisinstance, MainPrinter.message4TheMasses, "Pdf fájl készítés", 1);
                                 }

                                 var6.add("A külön nyomtatandóként megjelölt lapokból a PDF fájl " + var7 + " néven elkészült.\n");
                                 var5.add(var7);
                              }
                           }
                        }
                     }

                     var7 = "";

                     int var15;
                     for(var15 = 0; var15 < var6.size(); ++var15) {
                        var7 = var7 + var6.elementAt(var15);
                     }

                     if (MainPrinter.this.showPdfsAreReady(var7 + "Megpróbáljuk betölteni az alapértelmezett pdf olvasóba a dokumentumo" + (var5.size() > 1 ? "kat" : "t") + "?") == 0) {
                        for(var15 = 0; var15 < var5.size(); ++var15) {
                           MainPrinter.this.executePdfReader((String)var5.elementAt(var15));
                        }
                     }
                  } else if (var3x == -2 && !MainPrinter.this.csoport) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "A fájlt nem lehet létrehozni!\nValószínűleg éppen meg van nyitva a pdf olvasóban.", "Pdf fájl készítés", 1);
                  }

                  if (MainPrinter.emptyPrint) {
                     try {
                        if (var1 == MainPrinter.this.ciklusVege - 1) {
                           MainPrinter.this.deInit();
                        }
                     } catch (Exception var11) {
                        Tools.eLog(var11, 0);
                     }

                     var9.dispose();
                     if (MainPrinter.emptyPrint && var1 == MainPrinter.this.ciklusVege - 1) {
                        MainFrame.thisinstance.mp.makeempty();
                     }
                  }
               } catch (MainPrinter.NoSelectedPageException var12) {
                  if (!MainPrinter.this.csoport) {
                     GuiUtil.showMessageDialog(var3, var12.getMessage(), "Nyomtatás", 1);
                  } else {
                     try {
                        MainPrinter.this.batchJpgPrint.errorList.add(" - Nincs nyomtatásra kijelölt oldal");
                     } catch (Exception var10) {
                        Tools.eLog(var10, 0);
                     }
                  }
               } catch (NumberFormatException var13) {
                  Tools.eLog(var13, 0);
               } catch (Exception var14) {
                  var14.printStackTrace();
                  MainPrinter.this.hiba("Nyomtatás hiba !" + (MainPrinter.message4TheMasses.equals("") ? "" : "\n" + MainPrinter.message4TheMasses), var14, 1);
                  MainPrinter.message4TheMasses = "";
               }

            }
         };
         var27.addActionListener(var28);
         JButton var29 = new JButton("Kivonatolt nyomtatás", var12.get("print_anyk_kijeloltek_nyomtatasa"));
         var29.setToolTipText("Kivonatolt nyomtatás");
         var29.setHorizontalAlignment(2);
         var29.setFocusPainted(false);
         var29.setMargin(var13);
         ActionListener var30 = new ActionListener() {
            public void actionPerformed(ActionEvent var1x) {
               try {
                  MainPrinter.this.handlePrintDialog();
                  MainPrinter.this.handlePrintActions();
                  boolean var2 = MainPrinter.this.doPrintPreview(var1, true);
                  MainPrinter.logPrinting();
                  byte var3x = 1;
                  if (!var2) {
                     MainPrinter.this.handleSPPrint(var1, 0, (String)null);
                  }

                  if (var3x > -1 && MainPrinter.this.object4UniquePrint.normalPrintAfterKPrint && MainPrinter.this.handleKPageOther(var1) && MainPrinter.this.object4UniquePrint.uniquePrintablePages.size() > 0) {
                     for(int var5 = 0; var5 < MainPrinter.this.object4UniquePrint.uniquePrintablePages.size(); ++var5) {
                        try {
                           MainPrinter.this.book = (Book)MainPrinter.this.object4UniquePrint.uniquePrintablePages.elementAt(var5);
                           int var4 = MainPrinter.this.doPrint(0, (String)null, var1);
                        } catch (NumberFormatException var8) {
                           Tools.eLog(var8, 0);
                        } catch (Exception var9) {
                           var9.printStackTrace();
                           MainPrinter.this.hiba("Nyomtatás hiba !" + (MainPrinter.message4TheMasses.equals("") ? "" : "\n" + MainPrinter.message4TheMasses), var9, 1);
                           MainPrinter.message4TheMasses = "";
                        }
                     }
                  }

                  MainPrinter.this.object4UniquePrint.normalPrintAfterKPrint = false;
               } catch (MainPrinter.PrintCancelledException var10) {
                  Tools.eLog(var10, 0);
               } catch (MainPrinter.NoSelectedPageException var11) {
                  if (!MainPrinter.this.csoport) {
                     GuiUtil.showMessageDialog(var3, var11.getMessage(), "Nyomtatás", 1);
                  } else {
                     try {
                        MainPrinter.this.batchJpgPrint.errorList.add(" - Nincs nyomtatásra kijelölt oldal");
                     } catch (Exception var7) {
                        Tools.eLog(var7, 0);
                     }
                  }
               } catch (Exception var12) {
                  ErrorList.getInstance().writeError(new Integer(2000), "hiba a nyomtatáskor", var12, (Object)null);
               }

            }
         };
         var29.addActionListener(var30);
         JButton var31 = new JButton("Kivonatolt PDF fájl", var12.get("print_anyk_pdf_file_keszitese"));
         var31.setToolTipText("Kivonatolt PDF fájl");
         var31.setHorizontalAlignment(2);
         var31.setFocusPainted(false);
         var31.setMargin(var13);
         ActionListener var32 = new ActionListener() {
            public void actionPerformed(ActionEvent var1x) {
               try {
                  if (!MainPrinter.this.checkAndSetPdfDir((Result)null)) {
                     return;
                  }

                  MainPrinter.this.handlePrintActions();
                  String var2 = MainPrinter.this.getPdfFilename(0, true);
                  MainPrinter.this.doPrintPreview(var1, false);
                  int var3x = MainPrinter.this.handleSPPrint(var1, 1, var2);
                  MainPrinter.logPrinting();
                  if (var3x > -1) {
                     Vector var4 = new Vector();
                     String var5 = "";
                     if (var3x != 1) {
                        var5 = "A kivonatolt nyomtatvány képe " + var2 + " néven elkészült.\n";
                     }

                     int var6;
                     if (MainPrinter.this.object4UniquePrint.normalPrintAfterKPrint && MainPrinter.this.handleKPageOther(var1) && MainPrinter.this.object4UniquePrint.uniquePrintablePages.size() > 0) {
                        for(int var7 = 0; var7 < MainPrinter.this.object4UniquePrint.uniquePrintablePages.size(); ++var7) {
                           try {
                              String var8 = var2.substring(0, var2.lastIndexOf(".pdf")) + "_melleklet_" + (var7 + 1) + ".pdf";
                              MainPrinter.this.book = (Book)MainPrinter.this.object4UniquePrint.uniquePrintablePages.elementAt(var7);
                              var6 = MainPrinter.this.doPrint(1, var8, var1);
                              var4.add(var8);
                              if (var6 <= 0) {
                                 if (var6 == -2 && !MainPrinter.this.csoport) {
                                    GuiUtil.showMessageDialog(MainFrame.thisinstance, "A fájlt nem lehet létrehozni!\nValószínűleg éppen meg van nyitva a pdf olvasóban.", "Pdf fájl készítés", 1);
                                 }

                                 MainPrinter.this.object4UniquePrint.normalPrintAfterKPrint = false;
                                 return;
                              }

                              if (!MainPrinter.this.csoport) {
                                 var5 = var5 + "Ön a(z) " + ((Lap)MainPrinter.this.object4UniquePrint.uniqueDataPages.elementAt(var7)).getLma().lapCim + " lapot is kitöltötte, melyet csak a 'Nyomtatvány képes' módon lehet kinyomtatni.\nEz a PDF fájl " + var8 + " néven elkészült.\n";
                              }
                           } catch (NumberFormatException var10) {
                              Tools.eLog(var10, 0);
                           } catch (Exception var11) {
                              var11.printStackTrace();
                              MainPrinter.this.hiba("Nyomtatás hiba !" + (MainPrinter.message4TheMasses.equals("") ? "" : "\n" + MainPrinter.message4TheMasses), var11, 1);
                              MainPrinter.message4TheMasses = "";
                           }
                        }
                     }

                     MainPrinter.this.object4UniquePrint.normalPrintAfterKPrint = false;
                     if (!MainPrinter.this.csoport) {
                        var5 = var5 + "Megpróbáljuk betölteni az alapértelmezett pdf olvasóba a dokumentumokat?";
                        if (MainPrinter.this.showPdfsAreReady(var5) == 0) {
                           MainPrinter.this.executePdfReader(var2);
                           if (var4.size() > 0) {
                              for(var6 = 0; var6 < var4.size(); ++var6) {
                                 MainPrinter.this.executePdfReader((String)var4.elementAt(var6));
                              }
                           }
                        }
                     }
                  }
               } catch (MainPrinter.NoSelectedPageException var12) {
                  if (!MainPrinter.this.csoport) {
                     GuiUtil.showMessageDialog(var3, var12.getMessage(), "Nyomtatás", 1);
                  } else {
                     try {
                        MainPrinter.this.batchJpgPrint.errorList.add(" - Nincs nyomtatásra kijelölt oldal");
                     } catch (Exception var9) {
                        Tools.eLog(var9, 0);
                     }
                  }
               } catch (Exception var13) {
                  ErrorList.getInstance().writeError(new Integer(2000), "hiba a nyomtatáskor", var13, (Object)null);
               }

            }
         };
         var31.addActionListener(var32);
         JButton var33 = new JButton("Kivonatolt nyomtatási kép", var12.get("print_anyk_nyomtatasi kep"));
         var33.setToolTipText("Kivonatolt nyomtatási kép");
         var33.setHorizontalAlignment(2);
         var33.setFocusPainted(false);
         var33.setMargin(var13);
         ActionListener var34 = new ActionListener() {
            public void actionPerformed(ActionEvent var1x) {
               String var2 = System.getProperty("java.io.tmpdir");
               if (!var2.endsWith("/") && !var2.endsWith("\\")) {
                  var2 = var2 + File.separator;
               }

               MainPrinter.autoFillPdfPrevFileName = var2 + "Kivonatolt_nyomtatási_kép_" + System.currentTimeMillis() + ".pdf";

               try {
                  if (!MainPrinter.this.vankijeloltCB()) {
                     throw MainPrinter.this.new NoSelectedPageException();
                  }

                  if (!MainPrinter.this.checkAndSetPdfDir((Result)null)) {
                     return;
                  }

                  String var3x = MainPrinter.this.getPdfFilename(0, true);
                  MainPrinter.this.object4UniquePrint = MainPrinter.this.new TempObject4SuperPages();
                  MainPrinter.this.handlePrintActions();
                  boolean var4 = MainPrinter.this.doPrintPreview(var1, false);
                  MainPrinter.logPrinting();
                  int var5 = 0;
                  if (!var4) {
                     var5 = MainPrinter.this.handleSPPrint(var1, 1, var3x);
                     if (var5 > -1) {
                        MainPrinter.this.setPdfPreviewFile();
                        MainPrinter.this.executePdfReader(MainPrinter.autoFillPdfPrevFileName);
                     }
                  }

                  if (var5 > -1 && MainPrinter.this.handleKPageOther(var1)) {
                     for(int var6 = 0; var6 < MainPrinter.this.object4UniquePrint.uniquePrintablePages.size(); ++var6) {
                        try {
                           MainPrinter.this.book = (Book)MainPrinter.this.object4UniquePrint.uniquePrintablePages.elementAt(var6);
                           MainPrinter.autoFillPdfPrevFileName = var2 + "Kivonatolt_nyomtatási_kép_" + var6 + "_" + System.currentTimeMillis() + ".pdf";
                           var5 = MainPrinter.this.doPrint(1, MainPrinter.autoFillPdfPrevFileName, var1);
                           if (var5 > 0) {
                              MainPrinter.this.setPdfPreviewFile();
                              MainPrinter.this.executePdfReader(MainPrinter.autoFillPdfPrevFileName);
                           } else if (var5 == -2) {
                              GuiUtil.showMessageDialog(MainFrame.thisinstance, "A fájlt nem lehet létrehozni!\nValószínűleg éppen meg van nyitva a pdf olvasóban.", "Pdf fájl készítés", 1);
                           }
                        } catch (NumberFormatException var9) {
                           Tools.eLog(var9, 0);
                        } catch (Exception var10) {
                           var10.printStackTrace();
                        }
                     }
                  }
               } catch (MainPrinter.NoSelectedPageException var11) {
                  if (!MainPrinter.this.csoport) {
                     GuiUtil.showMessageDialog(var3, var11.getMessage(), "Nyomtatás", 1);
                  } else {
                     try {
                        MainPrinter.this.batchJpgPrint.errorList.add(" - Nincs nyomtatásra kijelölt oldal");
                     } catch (Exception var8) {
                        Tools.eLog(var8, 0);
                     }
                  }
               } catch (Exception var12) {
                  ErrorList.getInstance().writeError(new Integer(2000), "hiba a nyomtatási kép készítésekor", var12, (Object)null);
               }

               MainPrinter.autoFillPdfPrevFileName = null;
               MainPrinter.this.object4UniquePrint.normalPrintAfterKPrint = false;
            }
         };
         var33.addActionListener(var34);
         byte var35 = 3;
         if (kivonatoltanBekuldheto) {
            var35 = 4;
         }

         JPanel var36 = new JPanel(new GridLayout(var35, 3));
         var36.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
         var36.add(var14);
         var36.add(var16);
         var36.add(var23);
         var36.add(var24);
         var36.add(var18);
         var36.add(var27);
         if (this.kivonatolt && !emptyPrint) {
            var36.add(var33);
            var36.add(var29);
            var36.add(var31);
         }

         var36.add(var21);
         var5.add(var6, "North");
         JScrollPane var37 = new JScrollPane(var47, 20, 30);
         int var46 = (this.lapszam + 2) * (GuiUtil.getCommonItemHeight() + 4);
         if (this.lapszam > 10) {
            var46 = 12 * (GuiUtil.getCommonItemHeight() + 4);
         }

         int var38 = GuiUtil.getScreenW() / 2;
         Dimension var39 = new Dimension(Math.min(GuiUtil.getW("Kivonatolt nyomtatási képWWWW"), var38), var46);
         var37.setSize(var39);
         var37.setPreferredSize(var39);
         var37.setMinimumSize(var39);
         var5.setSize(new Dimension(var48, var46 + 5 * (GuiUtil.getCommonItemHeight() + 4) + (var18.getIcon().getIconHeight() + 8) * (var35 + 1)));
         var5.setPreferredSize(var5.getSize());
         var5.setMinimumSize(var5.getSize());
         var5.add(var37, "Center");
         var5.add(var36, "South");
         var5.setVisible(true);
         var9.getContentPane().add(var5);
         var9.setDefaultCloseOperation(0);
         if (PrintServiceLookup.lookupPrintServices((DocFlavor)null, (AttributeSet)null).length == 0) {
            var18.setEnabled(false);
            var24.setEnabled(false);
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A program nem talált telepített nyomtatót!", "Nyomtatás hiba", 1);
         }

         var9.setBounds(var7, var8, var48, var5.getHeight());
         var9.setSize(new Dimension(var48, var5.getHeight()));
         var9.setPreferredSize(var9.getSize());
         var9.setMinimumSize(new Dimension(400, 400));
         int var40 = (GuiUtil.getScreenW() - var9.getWidth()) / 2;
         int var41 = (GuiUtil.getScreenH() - var9.getHeight()) / 2;
         var9.setLocation(var40, var41);
         var9.pack();
         if (var2) {
            var9.setVisible(true);
         }
      } else {
         try {
            this.nyomtatandoLapokBeallitasa();
            if (this.batchPrint2Jpg) {
               PrintPreviewPanel var49 = this.initWithMonitor(var9, var1);

               try {
                  var49.loadedFilename = bookModel.cc.getLoadedfile().getName();
               } catch (Exception var43) {
                  var49.loadedFilename = bookModel.loadedfile.getName();
               }

               this.batchJpgPrint.errorList.addAll(var49.doPrintToImage(true).errorList);
            } else if (this.batchPrintSimpleMode > -1) {
               if (this.batchPrint2OnePdf) {
                  this.batchJpgPrint.errorList.addAll(this.handleCsoportosMulti1Pdfbe(var1).errorList);
               }
            } else if (this.batchPrint2Pdf) {
               this.batchJpgPrint.errorList.addAll(this.handleCsoportosPdf(var1).errorList);
            } else {
               this.batchJpgPrint.errorList.addAll(this.handleCsoportosPrinter(var1).errorList);
            }
         } catch (MainPrinter.NoSelectedPageException var44) {
            if (!this.csoport) {
               GuiUtil.showMessageDialog(var3, var44.getMessage(), "Nyomtatás", 1);
            } else {
               try {
                  this.batchJpgPrint.errorList.add(" - Nincs nyomtatásra kijelölt oldal");
               } catch (Exception var42) {
                  Tools.eLog(var42, 0);
               }
            }
         } catch (Exception var45) {
            this.hiba("Nyomtatás hiba !" + (message4TheMasses.equals("") ? "" : "\n" + message4TheMasses), var45, 1);
            message4TheMasses = "";
         }
      }

   }

   private int checkBoxok(JPanel var1, boolean var2) throws Exception {
      int var3 = -1;
      this.object4UniquePrint.uniqueDataPages = new Vector();
      int var4 = GuiUtil.getScreenW() / 2;
      int var5 = GuiUtil.getW("WWWWWLapok :WW");

      for(int var6 = 0; var6 < this.lapok.length; ++var6) {
         if (this.lapok[var6].getLma().lapSzam <= 0) {
            ++var3;

            try {
               PageModel var7 = this.formModel.get(var3);
               this.lapMetaAdatokPontositasa(this.lapok[var6], var7.xmlht, var2, var6);
            } catch (Exception var11) {
               Tools.eLog(var11, 0);
            }

            try {
               this.lapvalasztoCB[var3] = this.egyCheckBox(this.lapok[var6].getLma(), var6);
               if (!this.lapok[var6].getLma().disabledByRole) {
                  JPanel var12 = new JPanel();
                  var12.setLayout(new BoxLayout(var12, 0));
                  var12.setAlignmentX(0.0F);
                  var12.add(this.lapvalasztoCB[var3]);
                  if (!emptyPrint && this.lapok[var6].getLma().isGuiEnabled && this.lapok[var6].getLma().dinamikusE) {
                     final JTextField var8 = new JTextField();
                     if (this.lapok[var6].getLma().maxLapszam > 1) {
                        var8.setText("1-" + this.lapok[var6].getLma().maxLapszam);
                        this.lapvalasztoCB[var3].setSelected(true);
                     }

                     var8.setToolTipText("Adja meg a nyomtatni kívánt oldalakat! pl.: 1,2,4 vagy 1-5,8-10");
                     int finalVar = var3;
                     var8.addKeyListener(new KeyListener() {
                        public void keyPressed(KeyEvent var1) {
                        }

                        public void keyReleased(KeyEvent var1) {
                           if (!var8.getText().trim().equals("")) {
                              MainPrinter.this.lapvalasztoCB[finalVar].setSelected(true);
                           }

                        }

                        public void keyTyped(KeyEvent var1) {
                        }
                     });
                     var8.setPreferredSize(new Dimension(100, GuiUtil.getCommonItemHeight() + 2));
                     var8.setMaximumSize(new Dimension(100, GuiUtil.getCommonItemHeight() + 2));
                     this.kiegInfoACBhoz.put(this.lapvalasztoCB[var3], var8);
                     var12.add(new JLabel("   Lapok :  "));
                     var12.add(var8);
                     var4 = Math.max(var4, GuiUtil.getW(this.lapvalasztoCB[var3], this.lapvalasztoCB[var3].getText()) + var5 + 100 + 40);
                  }

                  this.indexInfoACBhoz.put(this.lapvalasztoCB[var3], new Integer(var6));
                  var1.add(var12);
               }
            } catch (Exception var10) {
               this.lapvalasztoCB[var3] = null;
            }
         }
      }

      return var4;
   }

   private JCheckBox egyCheckBox(LapMetaAdat var1, int var2) throws Exception {
      JCheckBox var3 = GuiUtil.getANYKCheckBox(var1.lapCim);
      var3.setEnabled(var1.nyomtathato);
      if (var3.isEnabled()) {
         var3.setEnabled(var1.isGuiEnabled);
      }

      if (var3.isEnabled()) {
         var3.setSelected(var1.isNyomtatando());
      }

      if (!emptyPrint && !bookModel.autofill) {
         if (var1.disabledByRole) {
            var3.setEnabled(false);
         }
      } else if (!var1.disabledByRole) {
         var3.setEnabled(true);
         var3.setSelected(true);
      }

      if (bookModel.getOperationMode().equals("1") && !var1.disabledByRole) {
         var3.setEnabled(true);
      }

      var3.setName("" + var2);
      var3.addItemListener(this);
      return var3;
   }

   private void mindValtozik(JCheckBox[] var1, boolean var2) {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var1[var3] != null) {
            if (var1[var3].isEnabled()) {
               var1[var3].setSelected(var2);
            }

            for(int var4 = 0; var4 < this.lapok.length; ++var4) {
               this.lapok[var4].getLma().setNyomtatando(var2);
               if (!this.lapok[var4].getLma().isGuiEnabled) {
                  this.lapok[var4].getLma().setNyomtatando(false);
               }

               if (this.lapok[var4].getLma().disabledByRole) {
                  this.lapok[var4].getLma().setNyomtatando(false);
               }
            }
         }
      }

      if (!var2) {
         this.mindenKiegInfoTorlese();
      }

   }

   private boolean doPrintPreview(int var1, boolean var2) throws Exception {
      boolean var3 = false;
      if (!emptyPrint) {
         if (!this.vankijeloltCB()) {
            throw new MainPrinter.NoSelectedPageException();
         }

         Barkod var4 = new Barkod();
         Md5Hash var5 = new Md5Hash();
         String var6 = "";

         try {
            if (bookModel.cc.getLoadedfile() == null) {
               FileNameResolver var7 = new FileNameResolver(bookModel);
               String var8 = var7.generateFileName();
               if (!var8.endsWith(".frm.enyk")) {
                  var8 = var8 + ".frm.enyk";
               }

               var8 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.saves") + File.separator + var8;
               var6 = var5.createHash(var8.getBytes());
               bookModel.cc.setLoadedfile(new File(var8));
            } else {
               var6 = var5.createHash(bookModel.cc.getLoadedfile().getName().getBytes());
            }
         } catch (Exception var18) {
            var6 = "";
         } catch (Error var19) {
            var6 = "";
         }

         var6 = this.createBevId(var6);
         barkod_FixFejlecAdatok = var4.barkodFixFejlec(this.lapok, sablonVerzio, var6);

         try {
            var4.adatokALapokrol(this.lapok, bookModel, formId, var1);
         } catch (Exception var17) {
            ErrorList.getInstance().writeError(new Integer(2000), "hiba a bárkód adatok megszerzésekor", var17, (Object)null);
         }

         try {
            barkod_NyomtatvanyAdatok = var4.nyomtatvanyAdatok(this.lapok);
         } catch (Exception var16) {
            ErrorList.getInstance().writeError(new Integer(2000), "hiba a nyomtatványadatok megszerzésekor", var16, (Object)null);
         }

         var4 = null;
      }

      int var20 = 0;
      int var21 = -1;
      boolean var23 = false;
      int var22 = 0;

      for(int var24 = 0; var24 < this.lapok.length; ++var24) {
         if (this.lapok[var24].getLma().lapSzam <= 0) {
            ++var21;
            if (this.lapvalasztoCB[var21].isSelected()) {
               if (emptyPrint || !this.lapok[var24].getLma().uniquePrintable) {
                  ++var20;
               }

               if (this.lapok[var24].getLma().uniquePrintable) {
                  ++var22;
               }
            }
         }
      }

      if (var20 == 0) {
         if (var22 == 0) {
            throw new MainPrinter.NoSelectedPageException();
         }

         var20 = var22;
         var23 = true;
      }

      if (printJob == null) {
         printJob = PrinterJob.getPrinterJob();
      }

      if (!this.printerAlreadySelected && var2) {
         if (!printJob.printDialog()) {
            throw new MainPrinter.PrintCancelledException();
         }

         this.ps = printJob.getPrintService();
         this.printerAlreadySelected = true;
      }

      PageFormat var25 = printJob.defaultPage();
      PageFormat var9 = printJob.defaultPage();
      var25.setOrientation(1);
      var9.setOrientation(0);
      Paper var10 = var25.getPaper();

      try {
         nyomtatoMargo = Integer.parseInt(SettingsStore.getInstance().get("printer", "print.settings.margin"));
      } catch (NumberFormatException var15) {
         nyomtatoMargo = 2;
      }

      var10.setImageableArea((double)nyomtatoMargo * 2.8D, (double)nyomtatoMargo * 2.8D, var25.getWidth() - (double)nyomtatoMargo * 2.8D, var25.getHeight() - (double)nyomtatoMargo * 2.8D);
      var25.setPaper(var10);
      var9.setPaper(var10);
      defaultPageFormat4htmlPrint = var25;
      this.book = new Book();
      this.object4UniquePrint.uniquePrintablePages = new Vector();
      if (var20 > 0) {
         FormPrinter var12 = new FormPrinter(bookModel);
         var12.setIndex(var1);

         for(int var13 = 0; var13 < this.lapok.length; ++var13) {
            if ((bookModel.autofill || emptyPrint || this.lapok[var13].getLma().isGuiEnabled) && !this.lapok[var13].getLma().disabledByRole) {
               PageFormat var11;
               if (this.lapok[var13].getLma().alloLap) {
                  var11 = var25;
               } else {
                  var11 = var9;
               }

               this.lapok[var13].setPrintable(var12);
               this.lapok[var13].setPf(var11);
               this.lapok[var13].setxArany(100.0D);
               if (this.lapok[var13].getLma().isNyomtatando()) {
                  if (this.lapok[var13].getLma().uniquePrintable) {
                     Book var14 = new Book();
                     var14.append(this.lapok[var13], var11);
                     this.object4UniquePrint.uniquePrintablePages.add(var14);
                  } else {
                     this.book.append(this.lapok[var13], var11);
                  }
               }
            }
         }
      }

      if (this.object4UniquePrint.uniquePrintablePages.size() > 0) {
         this.object4UniquePrint.normalPrintAfterKPrint = true;
      }

      return var23;
   }

   private int doPrint(int var1, String var2, int var3) {
      return this.doPrint(var1, var2, var3, false, false);
   }

   private int doPrint(int var1, String var2, int var3, boolean var4, boolean var5) {
      HashPrintRequestAttributeSet var6 = new HashPrintRequestAttributeSet();
      if (printJob == null && !this.csoport) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatás hibát jelzett!\nValószínűleg nem található telepített nyomtató.", "Hiba", 0);
         return -1;
      } else {
         try {
            if (!this.csoport && var1 != 1 && !this.printerAlreadySelected) {
               return 1;
            } else {
               try {
                  try {
                     if ((this.csoport || this.object4UniquePrint.normalPrintAfterKPrint) && this.ps != null) {
                        printJob.setPrintService(this.ps);
                     }
                  } catch (Exception var34) {
                     ErrorList.getInstance().writeError(new Integer(2000), "hiba a nyomtatáskor - printService error", var34, (Object)null);
                  }

                  printJob.setPageable(this.book);
                  if (var1 != 1) {
                     SupportType var37 = this.isPostscriptSupported();
                     if (var37.psSupported & (var37.gifSupported || var37.pngSupported || var37.jpegSupported)) {
                        try {
                           this.imagePrint(var6, var37);
                        } catch (Exception var29) {
                           var29.printStackTrace();
                           System.out.println("ps printing failed try to print normal mode");
                           printJob.print(var6);
                        }
                     } else {
                        printJob.print(var6);
                     }
                  } else {
                     PdfPrint var7 = new PdfPrint(this.book, var2);
                     FileOutputStream var38 = null;
                     int var9;
                     if (this.batchPrint2OnePdf) {
                        if ((var3 == this.ciklusEleje || var3 == -1) && !var5) {
                           this.document = new Document();

                           try {
                              var38 = new FileOutputStream(var2);
                           } catch (FileNotFoundException var33) {
                              return -2;
                           }

                           this.writer = PdfWriter.getInstance(this.document, var38);
                           this.document.open();
                        }

                        var9 = var7.print2PDF(this.document, this.writer);
                        if ((var3 == this.ciklusVege - 1 || var3 == -1) && !var4) {
                           this.document.close();
                        }
                     } else {
                        this.document = new Document();

                        try {
                           var38 = new FileOutputStream(var2);
                        } catch (FileNotFoundException var32) {
                           return -2;
                        }

                        this.writer = PdfWriter.getInstance(this.document, var38);
                        this.document.open();
                        var9 = var7.print2PDF(this.document, this.writer);
                        if (PropertyList.getInstance().get("pdfxml.print.xml.silent") != null) {
                           try {
                              String var10 = (String)PropertyList.getInstance().get("pdfxml.print.xml.silent");
                              PdfFileSpecification var11 = PdfFileSpecification.fileEmbedded(this.writer, var10, (new File(var10)).getName(), (byte[])null);
                              var11.addDescription("csatolt Nyomtatvány xml fájl", false);
                              this.writer.addFileAttachment(var11);
                              String var12 = var10 + ".errorList.txt";
                              File var13 = new File(var12);
                              if (var13.exists()) {
                                 PdfFileSpecification var14 = PdfFileSpecification.fileEmbedded(this.writer, var12, var13.getName(), (byte[])null);
                                 var14.addDescription("Nyomtatvány hibalista fájl", false);
                                 this.writer.addFileAttachment(var14);
                                 boolean var15 = var13.delete();
                              }
                           } catch (Exception var30) {
                              var30.printStackTrace();
                              var9 = -2;
                           } finally {
                              PropertyList.getInstance().set("pdfxml.print.xml.silent", (Object)null);
                           }
                        }

                        this.document.close();
                     }

                     if (var9 < 0 || var9 == 3) {
                        return var9;
                     }
                  }
               } catch (PrinterException var35) {
                  try {
                     this.document.close();
                  } catch (Exception var28) {
                     Tools.eLog(var28, 0);
                  }

                  ErrorList.getInstance().writeError(new Integer(2000), "hiba a nyomtatáskor", var35, (Object)null);
                  if (var35.getMessage().startsWith("*HIBA!")) {
                     this.hiba(var35.getMessage().substring(1), var35, 1);
                     this.batchJpgPrint.setOk(false);
                     this.batchJpgPrint.errorList.add(var35.getMessage().substring(1));
                  } else {
                     this.hiba("Nyomtatás hiba !" + (message4TheMasses.equals("") ? "" : "\n" + message4TheMasses), var35, 1);
                  }

                  return -1;
               }

               return message4TheMasses.equals("") ? 1 : 3;
            }
         } catch (Exception var36) {
            if (!this.csoport) {
               ErrorList.getInstance().writeError(new Integer(2000), "Hiba a pdf nyomtatáskor", var36, (Object)null);
               if (JOptionPane.showOptionDialog(MainFrame.thisinstance, var1 == 1 ? "A PDF fájl elkészítése közben hiba lépett fel\nMegpróbáljunk az alapértelmezett nyomtatóra nyomtatni?" : "A nyomtatóválasztó panel megjelenítése hibát okozott.\nMegpróbáljunk az alapértelmezett nyomtatóra nyomtatni?", "Nyomtatás", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0) {
                  boolean var8 = true;

                  try {
                     printJob.setPageable(this.book);
                     printJob.print();
                  } catch (PrinterException var27) {
                     ErrorList.getInstance().writeError(new Integer(2000), "hiba a nyomtatáskor", var27, (Object)null);
                     this.hiba("hiba a nyomtatáskor !", var27, 1);
                     var8 = false;
                  }

                  if (var8) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatás nem jelzett hibát!", "Nyomtatás", 1);
                  } else {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatás újabb hibát jelzett!", "Hiba", 0);
                  }

                  return var8 ? 1 : -1;
               }
            }

            return -1;
         }
      }
   }

   private void parseKiegInfo(String var1, int var2, int var3, boolean var4) throws NumberFormatException {
      try {
         boolean var5 = false;
         var1 = var1.replaceAll(" ", "");
         int var6;
         if (var1.equals("")) {
            for(var6 = 0; var6 < var3; ++var6) {
               this.lapok[var6 + var2].getLma().setNyomtatando(var4);
            }

            var5 = true;
         } else {
            for(var6 = 0; var6 < var3; ++var6) {
               this.lapok[var6 + var2].getLma().setNyomtatando(false);
            }

            String[] var13 = var1.split(",");

            for(int var10 = 0; var10 < var13.length; ++var10) {
               if (var13[var10].indexOf("-") != -1) {
                  int var8 = Integer.parseInt(var13[var10].substring(0, var13[var10].indexOf("-")));
                  int var9 = Integer.parseInt(var13[var10].substring(var13[var10].indexOf("-") + 1));
                  if (var8 == 0) {
                     var8 = 1;
                  }

                  for(int var11 = var8; var11 <= var9; ++var11) {
                     if (var11 >= 1 && var11 <= var3) {
                        var5 = true;
                        this.lapok[var11 + var2 - 1].getLma().setNyomtatando(var4);
                     }
                  }
               } else {
                  int var7 = Integer.parseInt(var13[var10]);
                  if (var7 == 0) {
                     var7 = 1;
                  }

                  if (var7 <= var3) {
                     var5 = true;
                     this.lapok[var7 + var2 - 1].getLma().setNyomtatando(var4);
                  }
               }
            }
         }

         if (!var5) {
            throw new NumberFormatException();
         }
      } catch (NumberFormatException var12) {
         this.hiba("Hibás a nyomtatandó oldalak megadása !", var12, 1);
         throw new NumberFormatException("Hibás a nyomtatandó oldalak megadása !");
      }
   }

   private void hiba(String var1, Exception var2, int var3) {
      if (this.csoport) {
         var3 = 0;
      }

      if (var3 > 0) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, var1, "Nyomtatás hiba", 0);
      } else {
         System.out.println(var1);
      }

   }

   private boolean vankijeloltCB() {
      int var1 = 0;
      int var2 = -1;
      boolean var3 = false;

      while(var1 < this.lapok.length && !var3) {
         if (this.lapok[var1].getLma().lapSzam > 0) {
            ++var1;
         } else {
            ++var2;
            if (this.lapvalasztoCB[var2].isSelected()) {
               var3 = true;
            } else if (this.indexInfoACBhoz.get(this.lapvalasztoCB[var2]) != null && this.kiegInfoACBhoz.containsKey(this.lapvalasztoCB[var2])) {
               var3 = !((JTextField)this.kiegInfoACBhoz.get(this.lapvalasztoCB[var2])).getText().trim().equals("");
               if (var3) {
                  this.lapvalasztoCB[var2].setSelected(true);
               }
            }

            ++var1;
         }
      }

      return var3;
   }

   private int elsoNyomtathatoLapIndex(boolean var1) {
      return var1 ? this.elsoNyomtathatoLapIndex1() : this.elsoNyomtathatoLapIndex2();
   }

   private int utolsoNyomtathatoLapIndex(boolean var1) {
      return var1 ? this.utolsoNyomtathatoLapIndex1() : this.utolsoNyomtathatoLapIndex2();
   }

   private int elsoNyomtathatoLapIndex1() {
      int var1;
      for(var1 = 0; var1 < this.lapok.length && !this.lapok[var1].getLma().isNyomtatando(); ++var1) {
      }

      return var1 == this.lapok.length ? -1 : var1;
   }

   private int utolsoNyomtathatoLapIndex1() {
      int var1;
      for(var1 = this.lapok.length - 1; var1 > -1 && (!this.lapok[var1].getLma().isNyomtatando() || this.lapok[var1].getLma().disabledByRole || !this.lapok[var1].getLma().isGuiEnabled && !emptyPrint); --var1) {
      }

      return var1;
   }

   private int elsoNyomtathatoLapIndex2() {
      int var1;
      for(var1 = 0; var1 < this.lapok.length && (!this.lapok[var1].getLma().isNyomtatando() || this.lapok[var1].getLma().uniquePrintable); ++var1) {
      }

      return var1 == this.lapok.length ? -1 : var1;
   }

   private int utolsoNyomtathatoLapIndex2() {
      int var1;
      for(var1 = this.lapok.length - 1; var1 > -1 && (this.lapok[var1].getLma().uniquePrintable || !this.lapok[var1].getLma().isNyomtatando() || this.lapok[var1].getLma().disabledByRole || !this.lapok[var1].getLma().isGuiEnabled && !emptyPrint); --var1) {
      }

      return var1;
   }

   private void nyomtatandoLapokBeallitasa() {
      int var1;
      for(var1 = 0; var1 < this.lapok.length; ++var1) {
         if (this.lapok[var1].getLma().barkodString == null) {
            this.lapok[var1].getLma().barkodString = "";
         }
      }

      var1 = 0;

      for(int var2 = 0; var2 < this.lapvalasztoCB.length; ++var2) {
         if (!this.lapvalasztoCB[var2].isSelected()) {
            for(int var4 = 0; var4 < this.lapok[var1].getLma().maxLapszam; ++var4) {
               this.lapok[var1 + var4].getLma().setNyomtatando(false);
            }
         } else if (this.indexInfoACBhoz.get(this.lapvalasztoCB[var2]) != null) {
            if (this.kiegInfoACBhoz.containsKey(this.lapvalasztoCB[var2])) {
               String var3 = ((JTextField)this.kiegInfoACBhoz.get(this.lapvalasztoCB[var2])).getText();
               this.parseKiegInfo(var3, var1, this.lapok[var1].getLma().maxLapszam, true);
            } else {
               this.lapok[var1].getLma().setNyomtatando(true);
            }
         }

         var1 += this.lapok[var1].getLma().maxLapszam;
      }

   }

   private void mindenKiegInfoTorlese() {
      for(int var1 = 0; var1 < this.lapvalasztoCB.length; ++var1) {
         if (this.kiegInfoACBhoz.containsKey(this.lapvalasztoCB[var1])) {
            ((JTextField)this.kiegInfoACBhoz.get(this.lapvalasztoCB[var1])).setText("");
         }
      }

   }

   public PrintPreviewPanel initWithMonitor(JDialog var1, int var2) throws Exception {
      this.nyomtatandoLapokBeallitasa();
      this.doPrintPreview(var2, false);
      int var3 = 12;

      try {
         var3 = Integer.parseInt((String)SettingsStore.getInstance().get("printer").get("print.settings.colors"));
      } catch (Exception var6) {
         Tools.eLog(var6, 0);
      }

      Vector var4 = this.getLapok4preview();
      PrintPreviewPanel var5 = new PrintPreviewPanel(printJob, this.book, var4, var1, "Nyomtatási kép", true, 100.0F, var3, this.object4UniquePrint.uniquePrintablePages);
      var5.nagyitas.setValue(100);
      return var5;
   }

   private void lapMetaAdatokPontositasa(Lap var1, Hashtable var2, boolean var3, int var4) {
      if (var2.containsKey("printable") && var1.getLma().isNyomtatando() && !var1.getLma().forcedByUser && ((String)var2.get("printable")).equalsIgnoreCase("nem")) {
         var1.getLma().setNyomtatando(emptyPrint);
      }

      if (var2.containsKey("kpage_other")) {
         if (var1.getLma().isNyomtatando()) {
            var1.getLma().uniquePrintable = true;
            this.object4UniquePrint.uniqueDataPages.add(var1);
            this.uniquePrint = true;
         }

         if (var3) {
            var1.getLma().setNyomtatando(false);
         }

         this.object4UniquePrint.normalPrintAfterKPrint = true;
      }

   }

   private boolean handleKPageOther(int var1) {
      if (this.object4UniquePrint.uniqueDataPages.size() > 0) {
         this.object4UniquePrint.uniquePrintablePages = new Vector();

         for(int var2 = 0; var2 < this.object4UniquePrint.uniqueDataPages.size(); ++var2) {
            this.setPaper2Defaults((Lap)this.object4UniquePrint.uniqueDataPages.elementAt(var2));
            Book var3 = new Book();
            FormPrinter var4 = new FormPrinter(bookModel);
            var4.setIndex(var1);
            ((Lap)this.object4UniquePrint.uniqueDataPages.elementAt(var2)).setPrintable(var4);
            ((Lap)this.object4UniquePrint.uniqueDataPages.elementAt(var2)).setxArany(100.0D);
            var3.append((Printable)this.object4UniquePrint.uniqueDataPages.elementAt(var2), ((Lap)this.object4UniquePrint.uniqueDataPages.elementAt(var2)).getPf());
            this.object4UniquePrint.uniquePrintablePages.add(var3);
         }
      }

      return this.object4UniquePrint.uniqueDataPages.size() > 0;
   }

   public static void runGC() throws Exception {
      _runGC();
   }

   private static void _runGC() throws Exception {
      long var0 = usedMemory();
      long var2 = Long.MAX_VALUE;

      for(int var4 = 0; var0 < var2 && var4 < 10; ++var4) {
         s_runtime.runFinalization();
         s_runtime.gc();
         Thread.yield();
         var2 = var0;
         var0 = usedMemory();
      }

   }

   public static long usedMemory() {
      return s_runtime.totalMemory() - s_runtime.freeMemory();
   }

   public static long freeMemory() {
      return s_runtime.freeMemory() / 1024L;
   }

   private void kozosInit() {
      try {
         if (this.ps == null) {
            PrinterJob var1 = PrinterJob.getPrinterJob();
            this.ps = var1.getPrintService();
         }
      } catch (Exception var2) {
         this.ps = null;
      }

      try {
         sablonVerzio = (String)bookModel.docinfo.get("ver");
      } catch (Exception var3) {
         if (sablonVerzio == null) {
            sablonVerzio = "1.0";
         }
      }

   }

   private boolean check() {
      ErrorDialog var1 = null;
      IErrorList var2 = null;
      ErrorListListener4XmlSave var3 = null;
      boolean var4 = false;

      try {
         CalculatorManager var5 = CalculatorManager.getInstance();
         var2 = ErrorList.getInstance();
         var3 = new ErrorListListener4XmlSave(-1);
         ((IEventSupport)var2).addEventListener(var3);
         var5.do_check_all((IResult)null, var3);
         Vector var6 = var3.getErrorList();
         if (var6.size() > 0) {
            TextWithIcon var7 = (TextWithIcon)var6.get(var6.size() - 1);
            if (var7.ii == null) {
               var6.remove(var6.size() - 1);
            }
         }

         if (var3.getRealErrorExtra() > 0) {
            nyomtatvanyEllenorzott = true;
            nyomtatvanyHibas = true;
            var1 = new ErrorDialog(MainFrame.thisinstance, "Nyomtatvány hibalista!", true, true, var6, "A nyomtatvány az alábbi hibákat tartalmazza:", true);
         } else {
            var6.clear();
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatvány ellenőrzése megtörtént\nAz ellenőrzés nem talált hibát.", "Nyomtatvány ellenőrzés", 1);
         }

         hasFatalError = false;
         if (this.hasFatalError(var6)) {
            hasFatalError = true;
            boolean var20 = false;
            if (bookModel.epost != null && bookModel.epost.equalsIgnoreCase("onlyinternet")) {
               var20 = true;
            }

            if (!this.csoport && !var1.isProcessStoppped() && !var20) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatvány súlyos hibát tartalmaz!\nA nyomtatáskor nem kerül rá 2d bárkód!", "Hiba", 0);
            }
         }

         var6 = null;

         try {
            var4 = !var1.isProcessStoppped();
         } catch (Exception var17) {
            var4 = true;
         }

         var1 = null;
      } catch (HeadlessException var18) {
         Tools.eLog(var18, 0);
      } finally {
         try {
            ((IEventSupport)var2).removeEventListener(var3);
         } catch (Exception var16) {
            Tools.eLog(var16, 0);
         }

      }

      return var4;
   }

   private Result isSavable() {
      TemplateUtils var1 = TemplateUtils.getInstance();
      Result var2 = new Result();

      try {
         Object[] var3 = (Object[])((Object[])var1.isSavable(bookModel));
         var2.setOk((Boolean)var3[0]);
         if (!var2.isOk()) {
            var2.errorList.add(var3[1]);
         }

         return var2;
      } catch (Exception var4) {
         var2.setOk(false);
         var2.errorList.add(var4.toString());
         return var2;
      }
   }

   private boolean hasFatalError(Vector var1) {
      int var2 = 0;

      boolean var3;
      for(var3 = false; var2 < var1.size() && !var3; ++var2) {
         if (((TextWithIcon)var1.get(var2)).imageType == 1) {
            var3 = true;
         }
      }

      return var3;
   }

   public boolean checkAndSetPdfDir(Result var1) {
      try {
         pdfDir = (String)PropertyList.getInstance().get("prop.usr.forcedPdfPath");
         if (pdfDir == null) {
            pdfDir = SettingsStore.getInstance().get("printer", "print.settings.pdfdir");
         }

         if (pdfDir == null) {
            pdfDir = (String)PropertyList.getInstance().get("prop.usr.naplo");
         }

         if (!pdfDir.endsWith("/") && !pdfDir.equals("\\")) {
            pdfDir = pdfDir + File.separator;
         }

         File var2 = new File(pdfDir);
         if (var2.exists() && var2.isDirectory()) {
            this.setPdfFileName();
            return true;
         } else {
            if (!this.csoport) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "A PDF állományok helye (" + pdfDir + ") nem létezik!", "PDF készítés hiba", 0);
            } else {
               try {
                  var1.errorList.add("A PDF állományok helye (" + pdfDir + ") nem létezik!");
               } catch (Exception var5) {
                  Tools.eLog(var5, 0);
               }
            }

            return false;
         }
      } catch (Exception var6) {
         ErrorList.getInstance().writeError(new Integer(2000), "hiba a nyomtatáskor", var6, (Object)null);

         try {
            var1.errorList.add("A PDF állományok helye (" + pdfDir + ") nem létezik!");
         } catch (Exception var4) {
            Tools.eLog(var4, 0);
         }

         return false;
      }
   }

   private void executePdfReader(String var1) {
      IOsHandler var2 = OsFactory.getOsHandler();

      try {
         String var4 = "";

         File var3;
         try {
            var3 = new File(SettingsStore.getInstance().get("gui", "pdf_viewer"));
            if (!var3.exists()) {
               throw new Exception();
            }

            var4 = var3.getAbsolutePath();
         } catch (Exception var6) {
            var3 = new File("");
         }

         if ("".equals(var4)) {
            if (var2.getOsName().toLowerCase().contains("windows")) {
               if (var1.contains(" ")) {
                  var4 = "\"" + var1 + "\"";
               } else {
                  var4 = var1;
               }
            } else {
               var4 = var1.replaceAll(" ", "\\\\ ");
            }
         } else {
            if (!var2.getOsName().toLowerCase().contains("windows")) {
               this.executePdfviewerOnMac(var4, var1);
               return;
            }

            if (var4.contains(" ")) {
               var4 = "\"" + var4 + "\"" + " " + var1;
            } else {
               var4 = var4 + " " + var1;
            }
         }

         var2.execute(var4, (String[])null, var3.getParentFile());
      } catch (Exception var7) {
         ErrorList.getInstance().writeError(new Integer(2000), "hiba a pdf olvasó indításakor", var7, (Object)null);
      }

   }

   private void imagePrint(PrintRequestAttributeSet var1, SupportType var2) throws Exception {
      ImagePrint var3 = new ImagePrint(printJob, var1, var2);

      for(int var4 = 0; var4 < this.book.getNumberOfPages(); ++var4) {
         if (var3.printToImage((Lap)this.book.getPrintable(var4)) != 0) {
            throw new Exception("Minden Ok, kezelt hiba");
         }
      }

   }

   private SupportType isPostscriptSupported() {
      SupportType var1 = new SupportType();

      try {
         if (!SettingsStore.getInstance().get("printer", "print.settings.enableps").equalsIgnoreCase("i")) {
            return var1;
         }
      } catch (Exception var4) {
         return var1;
      }

      DocFlavor[] var2 = printJob.getPrintService().getSupportedDocFlavors();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getMimeType().equalsIgnoreCase("application/postscript")) {
            var1.psSupported = true;
         }

         if (var2[var3].getMimeType().equalsIgnoreCase("image/gif")) {
            var1.gifSupported = true;
         }

         if (var2[var3].getMimeType().equalsIgnoreCase("image/png")) {
            var1.pngSupported = true;
         }

         if (var2[var3].getMimeType().equalsIgnoreCase("image/jpeg")) {
            var1.jpegSupported = true;
         }
      }

      return var1;
   }

   private void setPdfFileName() {
      String var1 = null;
      if (bookModel.cc.getLoadedfile() == null) {
         FileNameResolver var2 = new FileNameResolver(bookModel);
         var1 = var2.generateFileName();
         if (!var1.endsWith(".frm.enyk")) {
            var1 = var1 + ".frm.enyk";
         }

         bookModel.cc.setLoadedfile(new File(PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.saves") + File.separator + var1));
      } else {
         var1 = bookModel.cc.getLoadedfile().getName();
      }

      if (var1.toLowerCase().endsWith(".frm.enyk")) {
         var1 = var1.substring(0, var1.toLowerCase().indexOf(".frm.enyk"));
      }

      if (var1.toLowerCase().endsWith(".xml")) {
         var1 = var1.substring(0, var1.toLowerCase().indexOf(".xml"));
      }

      this.pdfFilename4Save = pdfDir + var1 + ".pdf";
   }

   private Result doSimplePrint(String var1) throws Exception {
      Result var2 = new Result();
      if (printJob == null && !this.csoport) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A nyomtatás hibát jelzett!\nValószínűleg nem található telepített nyomtató.", "Hiba", 0);
         var2.errorList.insertElementAt(-1, 0);
         var2.setOk(false);
         return var2;
      } else {
         try {
            if (!this.csoport && !this.printerAlreadySelected) {
               this.object4UniquePrint.normalPrintAfterKPrint = false;
               return var2;
            } else {
               Object[] var3 = this.getData4SimplePrint2();
               if (var3 == null) {
                  if (!this.csoport) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nincs kivonatolt nyomtatásra kijelölt oldal!", "Nyomtatás", 1);
                  } else {
                     try {
                        this.batchJpgPrint.errorList.add(" - Nincs kivonatolt nyomtatásra kijelölt oldal");
                     } catch (Exception var6) {
                        Tools.eLog(var6, 0);
                     }
                  }

                  var2.errorList.insertElementAt(-1, 0);
                  var2.setOk(false);
                  return var2;
               } else {
                  String var4 = (String)var3[0];

                  try {
                     var4 = var4.replaceAll("#13", "<br>");
                  } catch (Exception var9) {
                     Tools.eLog(var9, 0);
                  }

                  try {
                     try {
                        if (this.csoport) {
                           if (this.ps != null) {
                              printJob.setPrintService(this.ps);
                           }
                        } else {
                           this.ps = printJob.getPrintService();
                        }
                     } catch (Exception var7) {
                        var7.printStackTrace();
                     }

                     this.doHtmlPrint(var4, var3, var1);
                  } catch (Exception var8) {
                     var8.printStackTrace();
                     ErrorList.getInstance().writeError(new Integer(2000), "hiba a kivonatolt nyomtatáskor", var8, (Object)null);
                     var2.errorList.insertElementAt(-1, 0);
                     var2.setOk(false);
                     return var2;
                  }

                  return var2;
               }
            }
         } catch (FileNotFoundException var10) {
            if (this.csoport) {
               ErrorList.getInstance().writeError(new Integer(2000), "hiba a nyomtatáskor - Nem sikerült fejlécet készíteni a nyomtatványhoz. Próbálja meg újra!", (Exception)null, (Object)null);
            } else {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem sikerült fejlécet készíteni a nyomtatványhoz. Próbálja meg újra!", "Nyomtatás", 0);
            }

            throw var10;
         } catch (Exception var11) {
            if (!this.csoport) {
               ErrorList.getInstance().writeError(new Integer(2000), "hiba a kivonatolt nyomtatáskor - valószínűleg hiányos a sablonban a KVPRINT tag", (Exception)null, (Object)null);
            }

            throw var11;
         }
      }
   }

   private Result doSimplePrint2Pdf(String var1, int var2, String var3) throws Exception {
      return this.doSimplePrint2Pdf(var1, var2, var3, false, false);
   }

   private Result doSimplePrint2Pdf(String var1, int var2, String var3, boolean var4, boolean var5) throws Exception {
      if (bookModel.isNewStylePageBreak()) {
         ExtendedPdfHandler var19 = new ExtendedPdfHandler(bookModel, autoFillPdfPrevFileName, this.batchPrint2OnePdf, this.document, this.writer, this.ciklusVege, this.ciklusEleje, this.getNyomtatandoLapnevek(), this.formModel, elektronikus, notVKPrint, this.backgroundImage);
         Result var20 = var19.doNewSimplePrint2Pdf(var1, var2, var3, var4, var5);
         this.handleTooManyBRException();

         try {
            if (var20.isOk() && var20.errorList.size() > 1) {
               this.document = (Document)var20.errorList.remove(0);
               this.writer = (PdfWriter)var20.errorList.remove(0);
            }
         } catch (Exception var11) {
            System.out.println("Extended PAGE attributumok hiba." + var11.toString());
            Tools.eLog(var11, 0);
         }

         return var20;
      } else {
         Result var6 = new Result();

         try {
            Object[] var7 = this.getData4SimplePrint2();
            if (var7 == null) {
               if (!this.csoport) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nincs kivonatolt nyomtatásra kijelölt oldal!", "Nyomtatás", 1);
               } else {
                  try {
                     this.batchJpgPrint.errorList.add(" - Nincs kivonatolt nyomtatásra kijelölt oldal");
                  } catch (Exception var12) {
                     Tools.eLog(var12, 0);
                  }
               }

               var6.setOk(false);
               var6.errorList.insertElementAt(-2, 0);
               return var6;
            } else {
               String var8 = (String)var7[0];

               try {
                  var8 = var8.replaceAll("#13", "<br>");
               } catch (Exception var13) {
                  Tools.eLog(var13, 0);
               }

               Jep2Pdf var9;
               try {
                  if (autoFillPdfPrevFileName != null && !autoFillPdfPrevFileName.equals("")) {
                     var3 = autoFillPdfPrevFileName;
                  }

                  var9 = new Jep2Pdf(var3, var8, var1);
                  var9.setFooterVector(((Vector[])((Vector[])var7[1]))[1]);
                  var9.setHeaderVector(((Vector[])((Vector[])var7[1]))[0]);
                  if (this.batchPrint2OnePdf) {
                     if (var2 == this.ciklusEleje) {
                        this.document = new Document();
                        this.writer = PdfWriter.getInstance(this.document, new FileOutputStream(var3));
                        this.document.open();
                     }

                     var9.printJep2Pdf(this.document, this.writer, var2 == this.ciklusVege - 1);
                     if (var2 == this.ciklusVege - 1 && !var4) {
                        this.document.close();
                        this.document = null;
                        var9 = null;
                     }
                  } else {
                     this.document = new Document();
                     this.writer = PdfWriter.getInstance(this.document, new FileOutputStream(var3));
                     this.document.open();
                     var9.printJep2Pdf(this.document, this.writer, true);
                     this.document.close();
                     this.document = null;
                     var9 = null;
                     var6.errorList.add("A nyomtatvány kivonatolt képét a(z) " + var3 + " fájlba mentettük.");
                  }
               } catch (NumberFormatException var14) {
                  if (!this.csoport) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, var14.getMessage(), "Hiba", 0);
                  }

                  PropertyList.getInstance().set("brCountError", var14.getMessage());
                  var6.errorList.insertElementAt(-3, 0);
                  var6.setOk(false);
                  return var6;
               } catch (FileNotFoundException var15) {
                  var15.printStackTrace();
                  var9 = null;
                  var6.errorList.insertElementAt(-2, 0);
                  var6.setOk(false);
                  return var6;
               } catch (Exception var16) {
                  var16.printStackTrace();
                  var9 = null;
                  var6.errorList.insertElementAt(-1, 0);
                  var6.setOk(false);
                  return var6;
               }

               return var6;
            }
         } catch (FileNotFoundException var17) {
            if (this.csoport) {
               ErrorList.getInstance().writeError(new Integer(2000), "hiba a nyomtatáskor - Nem sikerült fejlécet készíteni a nyomtatványhoz. Próbálja meg újra!", (Exception)null, (Object)null);
            } else {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem sikerült fejlécet készíteni a nyomtatványhoz. Próbálja meg újra!", "Nyomtatás", 0);
            }

            throw var17;
         } catch (Exception var18) {
            var18.printStackTrace();
            ErrorList.getInstance().writeError(new Integer(2000), "hiba a kivonatolt nyomtatáskor - valószínűleg hiányos a sablonban a KVPRINT tag", (Exception)null, (Object)null);
            throw var18;
         }
      }
   }

   private Object[] getData4SimplePrint2() throws Exception {
      SimpleViewModel var1 = new SimpleViewModel(bookModel);
      Hashtable var2 = this.getNyomtatandoLapnevek();
      if (var2.size() == 0) {
         return null;
      } else {
         Vector var3 = var1.getResult(var2);
         String var4 = this.tableModels2DummyHtmlTable(var3);
         JEditorPane var5 = new JEditorPane("text/html", var4);
         double var6 = this.getCorrection(var5.getMinimumSize().getWidth());
         var5 = null;
         cSeq = 0;
         return this.tableModels2HtmlTable(var3, var6);
      }
   }

   public String tableModels2DummyHtmlTable(Vector var1) {
      StringBuffer var2 = new StringBuffer("<html><head>");
      var2.append("<STYLE TYPE=\"text/css\">").append("#tdr {text-align:right; padding-left:2px;}").append("#tdc {text-align:center;}").append("td {").append(" padding-top: -2px;").append(" padding-bottom: -2px;").append(" padding-left: 2px;").append(" padding-right: 2px;").append(" font-size: ").append(kivPrTdFontSize).append("px;").append(" }").append("table {").append(" border-collapse: collapse;").append("}").append("</STYLE>");
      var2.append("</head><body style=\"font-size: 6px;\">");
      int var3 = -1;
      boolean var4 = false;
      String var5 = "";

      for(int var6 = 0; var6 < var1.size(); ++var6) {
         if (var1.get(var6) instanceof PageTitle) {
            if (var4) {
               var2.delete(var3, var2.length());
            }

            var3 = var2.length();
            String var13 = ((PageTitle)var1.get(var6)).getTitleString();
            var13 = GuiUtil.getPlainLabelText(var13);
            var2.append("<table width=\"100%\" style=\"padding-top:6px; padding-bottom:3px\"><tr><td><div id=\"tdc\" style=\"font-size: ").append(kivPrCenterTdFontSize).append("px;\"><b>").append(var13).append("</b></div></td></tr></table>");
            if (var6 > 0) {
               var4 = true;
            }
         } else {
            var4 = false;
            DefaultTableModel var7 = (DefaultTableModel)var1.get(var6);
            var2.append("<table border=\"1\" cellpadding=\"").append(kivPrTdCellPadding).append("\" cellspacing=\"0\" width=\"100%\">");
            int var8 = var7.getColumnCount();

            for(int var9 = 0; var9 < var7.getRowCount(); ++var9) {
               var2.append("<tr>");
               if (var9 == 0) {
                  if (var7.getValueAt(0, 0) != null && ((String)var7.getValueAt(0, 0)).startsWith("<th>")) {
                     var5 = " bgcolor=\"" + thBgColor + "\"";
                  }

                  String var14 = var7.getValueAt(0, 0) == null ? "&nbsp;" : var7.getValueAt(0, 0).toString().replaceAll("<th>", "");
                  var14 = GuiUtil.getPlainLabelText(var14);
                  var2.append("<td valign=\"top\"").append(var5).append(">").append(var14).append("</td>");

                  for(int var15 = 1; var15 < var8; ++var15) {
                     String var12 = var7.getValueAt(var9, var15) == null ? "&nbsp;" : (String)var7.getValueAt(var9, var15);
                     var12 = GuiUtil.getPlainLabelText(var12);
                     var2.append("<td valign=\"top\"").append(var5).append(">").append(var12).append("</td>");
                  }
               } else {
                  for(int var10 = 0; var10 < var8; ++var10) {
                     String var11 = var7.getValueAt(var9, var10) == null ? "&nbsp;" : (String)var7.getValueAt(var9, var10);
                     var11 = GuiUtil.getPlainLabelText(var11);
                     var5 = "";
                     var2.append("<td valign=\"top\"").append(var5).append(">").append(var11).append("</td>");
                  }
               }

               var2.append("</tr>");
            }

            var2.append("</table>");
         }
      }

      if (var4) {
         var2.delete(var3, var2.length());
      }

      var2.append("</body></html>");
      return var2.toString();
   }

   private Hashtable getNyomtatandoLapnevek() {
      Hashtable var1 = new Hashtable();

      for(int var2 = 0; var2 < this.lapok.length; ++var2) {
         if (this.lapok[var2].getLma().isNyomtatando() && !this.lapok[var2].getLma().disabledByRole && !this.object4UniquePrint.uniqueDataPages.contains(this.lapok[var2])) {
            var1.put(this.lapok[var2].getLma().lapNev, Boolean.TRUE);
         }
      }

      return var1;
   }

   private String createBevId(String var1) {
      int var2 = 0;

      try {
         if (((Elem)bookModel.cc.getActiveObject()).getEtc().containsKey("sn")) {
            var2 = (Integer)((Elem)bookModel.cc.getActiveObject()).getEtc().get("sn");
         }
      } catch (Exception var4) {
         var2 = 0;
      }

      String var3 = "000" + var2;
      var3 = var3.substring(var3.length() - 3);
      var1 = var1 + "xxxxx";
      var1 = var1.substring(0, 5) + var3;
      return var1;
   }

   public Object[] tableModels2HtmlTable(Vector var1, double var2) throws Exception {
      String var4 = "";
      Vector[] var5 = new Vector[2];
      OrgInfo var6 = OrgInfo.getInstance();
      Hashtable var7 = (Hashtable)((Hashtable)var6.getOrgInfo(bookModel.docinfo.get("org"))).get("attributes");
      String var8 = ((OrgResource)((Hashtable)var6.getOrgList()).get(bookModel.docinfo.get("org"))).getVersion().toString();
      String var9 = var7.get("prefix") + "Resources_" + var8;

      try {
         URI var10 = new URI(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
         this.backgroundImage = (new File(var10.getPath())).getParent() + "/abevjava.jar!/resources/print/bg.gif";
         this.backgroundImage = this.backgroundImage.startsWith("/") ? "file:" + this.backgroundImage : "file:/" + this.backgroundImage;
         this.backgroundImage = "jar:" + this.backgroundImage;
      } catch (URISyntaxException var28) {
         System.out.println("Kivonatolt nyomtatási kép hiba!");
         var28.printStackTrace();
      }

      String var29 = "<td width=\"1\" bgcolor=\"" + thBgColor + "\" style=\"width:1px; padding:0;\"><img src=\"" + this.backgroundImage + "\" width=\"1\" height=\"1\" border=\"0\"></td>";
      String var11 = "<td width=\"1\" bgcolor=\"white\" style=\"width:1px; padding:0;\"><img src=\"" + this.backgroundImage + "\" width=\"1\" height=\"1\" border=\"0\"></td>";
      String var12 = PropertyList.getInstance().get("prop.sys.root").toString().replaceAll("\\\\", "/");
      var12 = var12.startsWith("/") ? "file:" + var12 : "file:/" + var12;
      var12 = var12 + "/eroforrasok";
      Hashtable var13 = this.formModel.kvprintht;
      int var14 = -1;
      int var15 = -1;
      if (var13 != null) {
         var5[0] = (Vector)var13.get("hrows");
         var5[1] = (Vector)var13.get("frows");
         var14 = Integer.parseInt((String)var13.get("kiv_fejlec_height"));
         var15 = Integer.parseInt((String)var13.get("kiv_fejlec_width"));
         double var16 = this.getCorrection((double)var15);
         var14 = (int)((double)var14 * var16 / var2);
         var15 = (int)((double)var15 * var16 / var2);
      } else {
         var5 = new Vector[]{new Vector(), new Vector()};
      }

      Vector[] var30 = this.parseRows(var5);
      String var17 = null;
      if (var13 != null) {
         headerImageName = (String)var13.get("kiv_fejlec_file_name");
         headerFromJar = "jar:" + var12 + "/" + var9 + ".jar!/pictures/" + headerImageName;
         var17 = "jar:" + var12 + "/" + var9 + ".jar!/pictures/" + var13.get("kiv_fejlec_file_name");
         var4 = "<table width=\"" + var15 + "\" height=\"" + var14 + "\"><tr><td><img src=\"" + var17 + "\"  width=\"" + var15 + "\" height=\"" + var14 + "\" border=\"0\"></td></tr></table>";
      }

      StringBuffer var18 = new StringBuffer("<html><head>");
      var18.append("<STYLE TYPE=\"text/css\">").append("#tdl {text-align:left; padding-left:2px;}").append("#tdr {text-align:right; padding-left:2px;}").append("#tdc {text-align:center;}").append("#ntdl {text-align:left; padding-left:2px; width: ").append("200").append("px;}").append("#ntdr {text-align:right; padding-left:2px; width: ").append("200").append("px;}").append("#ntdc {text-align:center; width: 100px;}").append("td {").append(" padding-top: -2px;").append(" padding-bottom: -2px;").append(" padding-left: 2px;").append(" padding-right: 2px;").append(" font-size: ").append(kivPrTdFontSize).append("px;").append(" }").append("table {").append(" border-collapse: collapse;").append("}").append("</STYLE>");
      var18.append("</head><body style=\"font-size: 6px;\">");
      if (!elektronikus && !notVKPrint) {
         var18.append(var4);
      }

      int var19 = -1;
      boolean var20 = false;
      String var21 = "";

      for(int var22 = 0; var22 < var1.size(); ++var22) {
         if (var1.get(var22) instanceof PageTitle) {
            if (var20) {
               this.handleSeq(var18, var19);
               var18.delete(var19, var18.length());
            }

            var19 = var18.length();
            String var31 = GuiUtil.getPlainLabelText(((PageTitle)var1.get(var22)).getTitleString());
            var18.append("<table width=\"100%\" style=\"padding-top:6px; padding-bottom:3px\"><tr><td class=\"").append(cSeq++).append("\"><div id=\"tdc\" style=\"font-size: 8px;\">").append(var31).append("</div></td></tr></table>");
            if (var22 > 0) {
               var20 = true;
            }
         } else {
            var20 = false;
            DefaultTableModel var23 = (DefaultTableModel)var1.get(var22);
            var18.append("<table border=\"0\" cellpadding=\"1\" cellspacing=\"0\" width=\"100%\">");
            if (var23.getValueAt(0, 0) != null && ((String)var23.getValueAt(0, 0)).startsWith("<th>")) {
               var23.setValueAt(((String)var23.getValueAt(0, 0)).replaceAll("<th>", ""), 0, 0);
               var21 = " bgcolor=\"" + thBgColor + "\"";
            }

            int var24 = var23.getColumnCount();

            for(int var26 = 0; var26 < var23.getRowCount(); ++var26) {
               var18.append(this.getHBorder(2 * var24 + 1)).append("<tr>").append(var29);
               Object var25;
               int var27;
               String var32;
               if (var23.getValueAt(var26, 0) != null && var23.getValueAt(var26, 0).toString().startsWith("***")) {
                  if (var23.getValueAt(var26, 0) != null && var23.getValueAt(var26, 0).toString().startsWith("***#")) {
                     for(var27 = 0; var27 < var24; ++var27) {
                        var21 = "";
                        var18.append("<td class=\"").append(cSeq++).append("\" valign=\"top\"").append(var21).append(">");
                        var25 = var23.getValueAt(var26, var27);
                        if (var25 == null) {
                           var25 = "&nbsp;";
                        } else {
                           if (var27 == 0) {
                              var25 = GuiUtil.getPlainLabelText(var25.toString().substring(4));
                           }

                           if (var25.toString().startsWith(" id=\"ntd")) {
                              var18.deleteCharAt(var18.length() - 1);
                              var25 = GuiUtil.getPlainLabelText(var25.toString().substring(0, 5) + var25.toString().substring(6));
                           }
                        }

                        var18.append(var25.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var27 == var24 - 1 ? var29 : var11);
                     }
                  } else {
                     var21 = "";

                     for(var27 = 0; var27 < var24; ++var27) {
                        var18.append("<td class=\"").append(cSeq++).append("\" valign=\"top\"").append(var21).append(">");
                        var25 = var23.getValueAt(var26, var27);
                        if (var25 == null) {
                           var32 = "&nbsp;";
                        } else {
                           if (var27 == 0) {
                              var25 = var25.toString().substring(3);
                           }

                           if (var25.toString().startsWith(" id=\"ntd")) {
                              var18.deleteCharAt(var18.length() - 1);
                           }

                           var32 = GuiUtil.getPlainLabelText(var25.toString());
                        }

                        var18.append(var32.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var27 == var24 - 1 ? var29 : var11);
                     }
                  }
               } else if (var26 == 0) {
                  for(var27 = 0; var27 < var24; ++var27) {
                     var18.append("<td class=\"").append(cSeq++).append("\" valign=\"top\"").append(var21).append(">");
                     var25 = var23.getValueAt(var26, var27);
                     if (var25 == null) {
                        var25 = "&nbsp;";
                     } else if (var25.toString().startsWith(" id=\"ntd")) {
                        var18.deleteCharAt(var18.length() - 1);
                     }

                     var32 = GuiUtil.getPlainLabelText((String)var25);
                     var18.append(var32.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var29);
                  }
               } else {
                  for(var27 = 0; var27 < var24; ++var27) {
                     var21 = "";
                     var18.append("<td class=\"").append(cSeq++).append("\" valign=\"top\"").append(var21).append(">");
                     var25 = var23.getValueAt(var26, var27);
                     if (var25 == null) {
                        var25 = "&nbsp;";
                     } else if (var25.toString().startsWith(" id=\"ntd")) {
                        var18.deleteCharAt(var18.length() - 1);
                     }

                     var32 = GuiUtil.getPlainLabelText((String)var25);
                     var18.append(var32.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var29);
                  }
               }

               var18.append("</tr>");
            }

            var18.append(this.getHBorder(2 * var24 + 1)).append("</table>");
         }
      }

      if (var20) {
         this.handleSeq(var18, var19);
         var18.delete(var19, var18.length());
      }

      var18.append("</body></html>");
      return new Object[]{var18.toString(), var30};
   }

   private String createSimpleBarkodString() {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = 0; var2 < this.lapok.length; ++var2) {
         if (this.lapok[var2].getLma().isGuiEnabled && this.lapok[var2].getLma().isNyomtatando() && !this.lapok[var2].getLma().disabledByRole && !this.object4UniquePrint.uniqueDataPages.contains(this.lapok[var2])) {
            var1.append(this.lapok[var2].getLma().barkodString);
         }
      }

      return var1.toString();
   }

   private String getHBorder(int var1) {
      return "<tr><td colspan=\"" + var1 + "\" bgcolor=\"" + thBgColor + "\" style=\"height:1px; padding:0;\"><img src=\"" + this.backgroundImage + "\" width=\"1\" height=\"1\" border=\"0\"></td></tr>";
   }

   private double getCorrection(double var1) {
      double var3 = 1.0D;
      if (var1 > defaultPageFormat4htmlPrint.getImageableWidth()) {
         var3 = defaultPageFormat4htmlPrint.getImageableWidth() / var1;
      }

      return Math.min(var3, 1.0D);
   }

   private Vector[] parseRows(Vector[] var1) {
      Vector[] var2 = new Vector[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = new Vector();
         String var4 = var3 == 0 ? "" : "_____________________________";
         Elem var5 = (Elem)bookModel.cc.getActiveObject();
         IDataStore var6 = (IDataStore)var5.getRef();

         for(int var7 = 0; var7 < var1[var3].size(); ++var7) {
            Hashtable var8 = (Hashtable)var1[var3].elementAt(var7);
            String var9 = this.parseText((String)var8.get("txt"), var6, bookModel.get(var5.getType()), var4);
            var2[var3].add(var9);
         }
      }

      return var2;
   }

   private String parseText(String var1, IDataStore var2, FormModel var3, String var4) {
      String[] var5 = var1.split("\\{");
      Vector var6 = new Vector();

      for(int var7 = 0; var7 < var5.length; ++var7) {
         String[] var8 = var5[var7].split("\\*\\}");

         for(int var9 = 0; var9 < var8.length; ++var9) {
            var6.add(var8[var9]);
         }
      }

      StringBuilder var10 = new StringBuilder();

      for(int var11 = 0; var11 < var6.size(); ++var11) {
         if (!var6.elementAt(var11).toString().startsWith("*")) {
            var10.append(var6.elementAt(var11));
         } else if (var2.get("0_" + var6.elementAt(var11).toString().substring(1)) != null) {
            var10.append(this.getFormattedData(var6.elementAt(var11), var3, var2, var4));
         } else {
            var10.append(var4);
         }
      }

      return var10.toString();
   }

   private String getFormattedData(Object var1, FormModel var2, IDataStore var3, String var4) {
      String var5 = var1.toString().substring(1);

      try {
         String var6 = var3.get("0_" + var5);
         return ((DataFieldModel)var2.fids.get(var5)).features.get("datatype").toString().equalsIgnoreCase("date") ? var6.substring(0, 4) + "." + var6.substring(4, 6) + "." + var6.substring(6) : var6;
      } catch (Exception var8) {
         return var4;
      }
   }

   private int handleSPPrint(int var1, int var2, String var3) {
      try {
         if (this.checkIfOnlyUniquePrintablePages()) {
            return 1;
         }

         String var4 = this.createSimpleBarkodString();
         Result var5 = var2 == 0 ? this.doSimplePrint(var4) : this.doSimplePrint2Pdf(var4, var1, var3);
         if (var5.isOk()) {
            this.batchJpgPrint.errorList.addAll(var5.errorList);
         }

         int var6 = 0;

         try {
            if (!var5.isOk()) {
               var6 = (Integer)var5.errorList.elementAt(0);
            }
         } catch (Exception var9) {
            var6 = 0;
         }

         if (var6 == -2) {
            if (!this.csoport) {
               if (autoFillPdfPrevFileName != null) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem sikerült létrehozni a nyomtatási képet!\nValószínűleg éppen meg van nyitva a pdf olvasóban.", "Nyomtatási kép készítés", 1);
               } else {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "A fájlt nem lehet létrehozni!\nValószínűleg éppen meg van nyitva a pdf olvasóban.", "Pdf fájl készítés", 1);
               }
            }

            return -1;
         }

         if (var6 < 0) {
            this.handleTooManyBRException();
            return -1;
         }

         return 0;
      } catch (FileNotFoundException var10) {
         Tools.eLog(var10, 0);
      } catch (MainPrinter.NoSelectedPageException var11) {
         if (!this.csoport) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, var11.getMessage(), "Nyomtatás", 1);
         } else {
            try {
               this.batchJpgPrint.errorList.add(" - Nincs nyomtatásra kijelölt oldal");
            } catch (Exception var8) {
               Tools.eLog(var8, 0);
            }
         }

         return -1;
      } catch (NumberFormatException var12) {
         Tools.eLog(var12, 0);
         this.hiba("Nyomtatás hiba ! Hiányos sablon információ", var12, 1);
      } catch (Exception var13) {
         ErrorList.getInstance().writeError(new Integer(2000), "hiba a nyomtatáskor", var13, (Object)null);
         this.hiba("Nyomtatás hiba !", var13, 1);
      }

      return -1;
   }

   private JDialog getSPSettingsDialog(boolean var1, boolean var2) {
      final JDialog var3 = new JDialog(MainFrame.thisinstance, "Nyomtatás", true);
      var3.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent var1) {
            MainPrinter.selectedRadio = 0;
            super.windowClosing(var1);
         }
      });
      ButtonGroup var4 = new ButtonGroup();
      final JRadioButton var5 = GuiUtil.getANYKRadioButton("Kizárólag a kitöltött adattartalom nyomtatása (papír- és festéktakarékos használat)");
      final JRadioButton var6 = GuiUtil.getANYKRadioButton("Kizárólag a kitöltött adattartalom nyomtatása PDF fájlba");
      final JRadioButton var7 = GuiUtil.getANYKRadioButton("Teljes nyomtatvány képi nyomtatása");

      try {
         if (PropertyList.getInstance().get("selectedRadioOnMainPrinter") != null) {
            selectedRadio = (Integer)PropertyList.getInstance().get("selectedRadioOnMainPrinter");
            switch(selectedRadio) {
            case 2:
               var6.setSelected(true);
               break;
            case 3:
               var7.setSelected(true);
               break;
            default:
               var5.setSelected(true);
            }
         } else {
            var5.setSelected(true);
         }
      } catch (Exception var14) {
         var5.setSelected(true);
      }

      var4.add(var5);
      var4.add(var6);
      var4.add(var7);
      JPanel var8 = new JPanel();
      JButton var9 = new JButton("OK");
      var9.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (var5.isSelected()) {
               MainPrinter.selectedRadio = 1;
            }

            if (var6.isSelected()) {
               MainPrinter.selectedRadio = 2;
            }

            if (var7.isSelected()) {
               MainPrinter.selectedRadio = 3;
            }

            var3.dispose();
            PropertyList.getInstance().set("selectedRadioOnMainPrinter", MainPrinter.selectedRadio);
         }
      });
      JButton var10 = new JButton("Mégsem");
      var10.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            MainPrinter.selectedRadio = 0;
            MainPrinter.this.deInit();
            var3.dispose();
         }
      });
      var8.setBorder(BorderFactory.createTitledBorder("A nyomtatvány az alábbiak közül bármely nyomtatási formában benyújtható"));
      var8.setLayout((LayoutManager)null);
      int var11 = (int)(1.5D * (double)GuiUtil.getCommonItemHeight());
      GuiUtil.setDynamicBound(var5, var5.getText(), 10, var11);
      var11 += (int)(1.5D * (double)GuiUtil.getCommonItemHeight());
      var8.add(var5);
      GuiUtil.setDynamicBound(var6, var6.getText(), 10, var11);
      var11 += (int)(1.5D * (double)GuiUtil.getCommonItemHeight());
      var8.add(var6);
      GuiUtil.setDynamicBound(var7, var7.getText(), 10, var11);
      var8.add(var7);
      Dimension var12 = new Dimension(GuiUtil.getW(var5, var5.getText()) + 40, 7 * (GuiUtil.getCommonItemHeight() + 4));
      var8.setPreferredSize(var12);
      var8.setSize(var12);
      var3.setLayout(new BorderLayout());
      var3.add(var8, "Center");
      JPanel var13 = new JPanel();
      var13.setLayout(new BoxLayout(var13, 0));
      var13.add(Box.createGlue());
      var13.add(var9);
      var13.add(var10);
      var13.add(Box.createGlue());
      var13.setBorder(new EmptyBorder(5, 5, 5, 5));
      var3.add(var13, "South");
      var3.setSize(new Dimension((int)var12.getWidth() + 20, (int)var12.getHeight() + 3 * GuiUtil.getCommonItemHeight()));
      var3.setPreferredSize(var3.getSize());
      var3.setMinimumSize(var3.getSize());
      var3.setResizable(true);
      return var3;
   }

   public void saveSPSettings() {
      SettingsStore var1 = SettingsStore.getInstance();
      var1.set("printer", "print.settings.disablespdialog", this.disableSPDialog ? "i" : "n");
      var1.set("printer", "print.settings.enablesimpleprint", this.enableSP ? "i" : "n");
      var1.save();
   }

   private void construct(BookModel var1, boolean var2) {
      this.setStartDate();
      bookModel = var1;
      nyomtatvanyHibas = false;
      nyomtatvanyEllenorzott = false;
      kontroll = false;
      voltEEllenorzesNyomtatasElott = true;

      try {
         String var3 = (String)var1.docinfo.get("org");
         OrgInfo var4 = OrgInfo.getInstance();
         Hashtable var5 = (Hashtable)var4.getOrgInfo(var3);
         orgPrefix = ((String)((Hashtable)var5.get("attributes")).get("prefix")).toLowerCase();
      } catch (Exception var9) {
         orgPrefix = "Nem definiált";
      }

      try {
         notVKPrint = var1.docinfo.containsKey("notVKPrint");
      } catch (Exception var8) {
         notVKPrint = false;
      }

      try {
         betaVersion = var1.docinfo.containsKey("release");
         if (betaVersion) {
            betaVersion = "teszt".equals(var1.docinfo.get("release").toString());
         }
      } catch (Exception var7) {
         betaVersion = false;
      }

      check_version = -1;
      if (!emptyPrint) {
         int var10 = var1.carryOnTemplate();
         switch(var10) {
         case 0:
            betaVersion = true;
            check_version = 0;
            if (!var2) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, BookModel.CHECK_VALID_MESSAGES[var10] + "\n A nyomtatvány nyomtatható, de nem kerül rá pontkód!", "Figyelmeztetés", 2);
            }
            break;
         case 1:
            int var11 = Tools.handleTemplateCheckerResult(var1);
            if (var11 >= 4) {
               betaVersion = true;
               check_version = var11;
               if (!var2) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, BookModel.CHECK_VALID_MESSAGES[var11] + "\n A nyomtatvány nyomtatható, de nem kerül rá pontkód!", "Figyelmeztetés", 2);
               }
            }
         case 2:
         case 3:
         }
      }

      try {
         kontroll = false;
         if (var1.temtype.equals("control")) {
            kontroll = true;
         }
      } catch (Exception var6) {
         kontroll = false;
      }

      this.formModel = var1.get(((Elem)var1.cc.getActiveObject()).getType());
      if (this.formModel.kp_give == null) {
         kivonatoltanBekuldheto = false;
      } else {
         kivonatoltanBekuldheto = this.formModel.kp_give.equalsIgnoreCase("true");
      }

      kivonatoltanBekuldheto = kivonatoltanBekuldheto || var1.name.equals("1053");
      papirosBekuldesTiltva = this.isPapirosBekuldesTiltasa();
      isTemplateDisabled = var1.isDisabledTemplate();
      this.csoport = var2;
   }

   private void setConditions() {
      try {
         if (bookModel.docinfo.containsKey("kprint")) {
            this.kivonatolt = ((String)bookModel.docinfo.get("kprint")).equalsIgnoreCase("true");
         } else {
            this.kivonatolt = false;
         }
      } catch (Exception var2) {
         this.kivonatolt = false;
      }

      if (voltEEllenorzesNyomtatasElott) {
         kivonatoltBarkoddal = this.kivonatolt && !elektronikus && !notVKPrint && !hasFatalError && !kontroll && kivonatoltanBekuldheto && !bookModel.autofill;
      } else {
         kivonatoltBarkoddal = false;
      }

      nemKuldhetoBeSzoveg = elektronikus;
      kellFejlec = !elektronikus && !notVKPrint;
   }

   private int showPdfsAreReady(String var1) {
      return GuiUtil.showOptionDialog((Component)null, var1, "PDF nyomtatás", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]);
   }

   private void handleSeq(StringBuffer var1, int var2) {
      try {
         String var3 = var1.substring(var2);
         String[] var4 = var3.split("class=");
         cSeq -= var4.length - 1;
      } catch (Exception var5) {
         Tools.eLog(var5, 0);
      }

   }

   private void delTempFiles() {
      File var1 = new File(System.getProperty("java.io.tmpdir"));
      File[] var2 = var1.listFiles();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         try {
            if (var2[var3].getName().startsWith("Kivonatolt_nyomtatási_kép_")) {
               var2[var3].delete();
            }
         } catch (Exception var7) {
            Tools.eLog(var7, 0);
         }
      }

      try {
         Iterator var9 = pdfPreviewFilesInSession.iterator();

         while(var9.hasNext()) {
            File var4 = (File)var9.next();

            try {
               var4.delete();
            } catch (Exception var6) {
               Tools.eLog(var6, 0);
            }
         }
      } catch (Exception var8) {
         Tools.eLog(var8, 0);
      }

   }

   private void setKVPrintParams() {
      if (this.formModel.attribs.containsKey("kv_fontsize")) {
         kivPrTdFontSize = (String)this.formModel.attribs.get("kv_fontsize");
      } else {
         kivPrTdFontSize = "10";
      }

      if (this.formModel.attribs.containsKey("kv_padding")) {
         kivPrTdCellPadding = (String)this.formModel.attribs.get("kv_padding");
      } else {
         kivPrTdCellPadding = "0";
      }

   }

   private String getPdfFilename(int var1, boolean var2) {
      StringBuffer var3 = new StringBuffer(this.pdfFilename4Save.substring(0, this.pdfFilename4Save.lastIndexOf(".pdf")));
      String var4 = this.getIds4PdfFile(var1);
      if (!"".equals(var4)) {
         var3.append("_").append(var4);
      }

      if (var1 > 0) {
         var3.append("_").append(var1);
      } else if (bookModel.cc.getActiveObjectindex() > 0) {
         var3.append("_").append(bookModel.cc.getActiveObjectindex());
      }

      if (var3.length() > 200) {
         var3.delete(200, var3.length());
      }

      if (var2) {
         var3.append("_kivonatolt");
      }

      File var5 = new File(var3.toString() + ".pdf");
      if (var5.exists()) {
         var3.append("_").append(this.getTimeString());
      }

      var3.append(".pdf");
      return var3.toString();
   }

   private String getTimeString() {
      long var1 = System.currentTimeMillis();
      return var1 - this.startL + "";
   }

   private void setStartDate() {
      Calendar var1 = Calendar.getInstance();
      var1.set(1, 2011);
      var1.set(2, 10);
      var1.set(5, 11);
      var1.set(10, 0);
      var1.set(12, 0);
      var1.set(13, 0);
      var1.set(14, 0);
      this.startL = var1.getTimeInMillis();
   }

   private String getIds4PdfFile(int var1) {
      String var2 = "";
      Object[] var3 = null;
      if (var1 == 0) {
         if (bookModel.cc.getActiveObject() == null) {
            try {
               var3 = (Object[])((Object[])((Elem)bookModel.cc.get(var1)).getEtc().get("callgui"));
            } catch (Exception var9) {
               return "";
            }
         } else {
            try {
               var3 = (Object[])((Object[])((Elem)bookModel.cc.getActiveObject()).getEtc().get("callgui"));
            } catch (Exception var8) {
               return "";
            }
         }
      } else {
         try {
            var3 = (Object[])((Object[])((Elem)bookModel.cc.get(var1)).getEtc().get("callgui"));
         } catch (Exception var7) {
            return "";
         }
      }

      try {
         if (var3[1] != null) {
            var2 = var2 + "_" + var3[1];
         }
      } catch (Exception var6) {
         var2 = "";
      }

      try {
         if (var3[2] != null) {
            var2 = var2 + "_" + var3[2];
         }
      } catch (Exception var5) {
         Tools.eLog(var5, 0);
      }

      var2 = this.cleanFilename(var2);
      return var2.startsWith("_") ? var2.substring(1) : var2;
   }

   private String cleanFilename(String var1) {
      var1 = var1.replaceAll("\"", "_");
      var1 = var1.replaceAll("=", "_");
      var1 = var1.replaceAll("\\(", "_");
      var1 = var1.replaceAll("\\)", "_");
      var1 = var1.replaceAll("\\{", "_");
      var1 = var1.replaceAll("\\}", "_");
      var1 = var1.replaceAll(";", "_");
      var1 = var1.replaceAll("&", "_");
      var1 = var1.replaceAll(" ", "_");
      var1 = var1.replaceAll("'", "_");
      var1 = var1.replaceAll(",", "_");
      var1 = var1.replaceAll(":", "_");
      var1 = var1.replaceAll("<", "_");
      var1 = var1.replaceAll(">", "_");
      var1 = var1.replaceAll("\\|", "_");
      var1 = var1.replaceAll("/", "_");
      var1 = var1.replaceAll("\\\\", "_");
      var1 = var1.replaceAll("\\*", "_");
      var1 = var1.replaceAll("\\?", "_");
      return var1;
   }

   private String getLoggedString(File var1, String var2) {
      return var1.getName() + " -> " + (new File(var2)).getName();
   }

   private boolean isPapirosBekuldesTiltasa() {
      Hashtable var1 = MetaInfo.getInstance().getFieldAttributes(bookModel.get_main_formmodel().id, "panids", true);
      String var2 = null;
      Enumeration var3 = var1.keys();

      while(var3.hasMoreElements() && var2 == null) {
         Object var4 = var3.nextElement();
         if (var1.get(var4).equals("Papíros beküldés tiltása")) {
            var2 = (String)var4;
         }
      }

      if (var2 == null) {
         return false;
      } else {
         String var5 = bookModel.get_main_document().get("0_" + var2);
         return var5 == null ? false : var5.equalsIgnoreCase("true");
      }
   }

   private boolean checkIfOnlyUniquePrintablePages() {
      for(int var1 = 0; var1 < this.lapok.length; ++var1) {
         if (this.lapok[var1].getLma().isNyomtatando() && !this.lapok[var1].getLma().disabledByRole && !this.lapok[var1].getLma().uniquePrintable) {
            return false;
         }
      }

      return true;
   }

   private Vector<Lap> getLapok4preview() {
      Vector var1 = new Vector();

      int var2;
      for(var2 = 0; var2 < this.lapok.length; ++var2) {
         if (!this.lapok[var2].getLma().uniquePrintable && !this.lapok[var2].getLma().disabledByRole && this.lapok[var2].getLma().isNyomtatando()) {
            var1.add(this.lapok[var2]);
         }
      }

      for(var2 = 0; var2 < this.lapok.length; ++var2) {
         if (!this.lapok[var2].getLma().disabledByRole && this.lapok[var2].getLma().isNyomtatando() && this.lapok[var2].getLma().uniquePrintable) {
            var1.add(this.lapok[var2]);
         }
      }

      return var1;
   }

   private void doHtmlPrint(String var1, Object[] var2, String var3) throws Exception {
      JEditorPane var4 = new JEditorPane("text/html", var1);
      Paper var5 = defaultPageFormat4htmlPrint.getPaper();
      double var6 = var5.getImageableX();
      double var8 = var5.getImageableY();
      double var10 = var5.getImageableWidth();
      double var12 = var5.getImageableHeight();
      Vector var14 = ((Vector[])((Vector[])var2[1]))[1];
      Vector var15 = ((Vector[])((Vector[])var2[1]))[0];
      var5.setImageableArea(var6 + (double)nyomtatoMargo * 5.6D, var8 + (double)nyomtatoMargo * 5.6D, var10 - (double)nyomtatoMargo * 5.6D * 3.0D, var12 - (double)nyomtatoMargo * 5.6D * 4.0D);
      defaultPageFormat4htmlPrint.setPaper(var5);
      HtmlPrinter var16 = new HtmlPrinter(var4, printJob, defaultPageFormat4htmlPrint);
      var16.setHeaderVector(var15);
      var16.setFooterVector(var14);
      BrPrinter var17 = new BrPrinter(var3);
      var16.setBp(var17);
      var16.print();
   }

   public Vector getUniqueNames() {
      Vector var1 = new Vector();

      for(int var2 = 0; var2 < this.lapok.length; ++var2) {
         if (this.lapok[var2].getLma().isNyomtatando() && this.lapok[var2].getLma().uniquePrintable) {
            var1.add(this.lapok[var2].getLma().lapCim);
         }
      }

      return var1;
   }

   private void handlePrintActions() throws MainPrinter.NoSelectedPageException {
      this.handlePrintActions(false);
   }

   private void handlePrintActions(boolean var1) throws MainPrinter.NoSelectedPageException {
      if (!this.vankijeloltCB() && !var1) {
         throw new MainPrinter.NoSelectedPageException();
      } else {
         this.nyomtatandoLapokBeallitasa();
         this.object4UniquePrint.uniqueDataPages = new Vector();
         int var2 = 0;

         for(int var3 = 0; var3 < this.lapok.length; ++var3) {
            if (this.lapok[var3].getLma().lapSzam <= 0) {
               try {
                  PageModel var4 = this.formModel.get(var2++);
                  this.lapMetaAdatokPontositasa(this.lapok[var3], var4.xmlht, true, var3);
               } catch (Exception var5) {
                  Tools.eLog(var5, 0);
               }
            }
         }

      }
   }

   private Result handleCsoportosPrinter(int var1) {
      Result var2 = new Result();

      try {
         this.handlePrintActions();

         try {
            this.doPrintPreview(var1, false);
         } catch (Exception var7) {
         }

         int var3 = this.doPrint(0, (String)null, var1);
         if (var3 > 0) {
            if (this.uniquePrint) {
               this.handleKPageOther(var1);
               boolean var4 = this.object4UniquePrint.normalPrintAfterKPrint;
               this.object4UniquePrint.normalPrintAfterKPrint = true;
               if (this.object4UniquePrint.uniquePrintablePages != null) {
                  for(int var5 = 0; var5 < this.object4UniquePrint.uniquePrintablePages.size(); ++var5) {
                     this.book = (Book)this.object4UniquePrint.uniquePrintablePages.elementAt(var5);
                     var3 = this.doPrint(0, (String)null, -1);
                     this.object4UniquePrint.normalPrintAfterKPrint = var4;
                     if (var3 == 3) {
                        var2.errorList.add(message4TheMasses);
                        message4TheMasses = "";
                     }
                  }
               }
            }

            try {
               if (var1 == this.ciklusVege - 1) {
                  this.deInit();
               }
            } catch (Exception var6) {
               Tools.eLog(var6, 0);
            }
         } else {
            var2.setOk(false);
            var2.errorList.add("Hiba a nyomtatáskor!");
         }
      } catch (MainPrinter.NoSelectedPageException var8) {
         var2.errorList.add("Nincs nyomtatásra kijelölt oldal");
      }

      return var2;
   }

   private Result handleCsoportosPdf(int var1) {
      Result var2 = new Result();

      try {
         this.checkAndSetPdfDir(var2);
         this.handlePrintActions();
         this.doPrintPreview(var1, false);
         String var3 = this.getPdfFilename(var1, false);
         this.pdfFilename4Save = var3;
         boolean var4 = false;
         boolean var5 = this.checkIfOnlyUniquePrintablePages();
         int var6 = 0;
         if (!var5) {
            var6 = this.doPrint(1, var3, var1, this.batchPrint2OnePdf, false);
            if (var6 > -1) {
               var4 = true;
            }
         }

         if (var6 < 0) {
            var2.setOk(false);

            try {
               var2.errorList.add("Hiba a pdf fájl készítésekor (" + this.getLoggedString(bookModel.cc.getLoadedfile(), var3) + ")");
            } catch (Exception var26) {
               var26.printStackTrace();
               var2.errorList.add("Hiba a pdf fájl készítésekor!");
            }
         } else {
            if (!this.batchPrint2OnePdf && !var5) {
               var2.errorList.add("A PDF fájl " + var3 + " néven elkészült.\n");
            }

            if (this.uniquePrint) {
               this.handleKPageOther(var1);
               boolean var8 = this.object4UniquePrint.normalPrintAfterKPrint;
               this.object4UniquePrint.normalPrintAfterKPrint = true;
               if (this.object4UniquePrint.uniquePrintablePages.size() > 0) {
                  for(int var9 = 0; var9 < this.object4UniquePrint.uniquePrintablePages.size(); ++var9) {
                     this.book = (Book)this.object4UniquePrint.uniquePrintablePages.elementAt(var9);
                     String var7 = var3.substring(0, var3.length() - 4) + "_melleklet_" + (var9 + 1) + ".pdf";
                     var6 = this.doPrint(1, this.batchPrint2OnePdf ? var3 : var7, var1, this.batchPrint2OnePdf, var4);
                     if (var6 < 0) {
                        var1 = this.ciklusVege - 1;
                        throw new Exception("A nyomtatás hibát jelzett!");
                     }

                     var4 = true;
                     this.object4UniquePrint.normalPrintAfterKPrint = var8;
                     if (!this.batchPrint2OnePdf) {
                        var2.errorList.add("A külön nyomtatandóként megjelölt lapokból a PDF fájl " + var7 + " néven elkészült.\n");
                     }
                  }
               }
            }

            if (this.batchPrint2OnePdf && var1 == this.ciklusEleje && var2.isOk()) {
               var2.errorList.add("A nyomtatvány képét a " + var3 + " fájlba mentettük.");
            }
         }
      } catch (MainPrinter.NoSelectedPageException var27) {
         var2.setOk(false);
         var2.errorList.add("Nincs nyomtatásra kijelölt oldal");
      } catch (Exception var28) {
         this.hiba("Nyomtatás hiba !", var28, 1);
         var2.setOk(false);
         var2.errorList.add("Nyomtatás hiba !");
      } finally {
         try {
            if (var1 == this.ciklusVege - 1) {
               try {
                  this.document.close();
               } catch (Exception var24) {
               }

               this.deInit();
            }
         } catch (Exception var25) {
            Tools.eLog(var25, 0);
         }

      }

      return var2;
   }

   private Result handleCsoportosMulti1Pdfbe(int var1) {
      Result var2 = new Result();
      String var3 = this.getPdfFilename(0, true);

      try {
         if (!this.checkAndSetPdfDir((Result)null)) {
            var2.setOk(false);
            var2.errorList.add("Nem sikerült beállítani a szükséges környezetet!");
            Result var31 = var2;
            return var31;
         }

         this.handlePrintActions();
         this.doPrintPreview(var1, false);
         boolean var4 = this.checkIfOnlyUniquePrintablePages();
         boolean var5 = false;
         if (!var4) {
            String var6 = this.createSimpleBarkodString();
            if (var1 == this.ciklusEleje) {
               var2 = this.doSimplePrint2Pdf(var6, var1, var3, true, false);
            } else {
               var2 = this.doSimplePrint2Pdf(var6, -1, var3, true, true);
            }

            var5 = var2.isOk();
         }

         if (var2.isOk()) {
            new Vector();
            String var7 = "";
            if (this.object4UniquePrint.normalPrintAfterKPrint && this.handleKPageOther(var1) && this.object4UniquePrint.uniquePrintablePages.size() > 0) {
               for(int var9 = 0; var9 < this.object4UniquePrint.uniquePrintablePages.size(); ++var9) {
                  try {
                     this.book = (Book)this.object4UniquePrint.uniquePrintablePages.elementAt(var9);
                     this.doPrint(1, var3, -1, true, var5);
                     var5 = true;
                  } catch (NumberFormatException var26) {
                     Tools.eLog(var26, 0);
                  } catch (Exception var27) {
                     var27.printStackTrace();
                     var2.setOk(false);
                     var2.errorList.add("Nyomtatás hiba !" + (message4TheMasses.equals("") ? "" : message4TheMasses));
                     this.hiba("Nyomtatás hiba !" + (message4TheMasses.equals("") ? "" : "\n" + message4TheMasses), var27, 1);
                     message4TheMasses = "";
                  }
               }
            }

            if (emptyPrint) {
               try {
                  this.deInit();
               } catch (Exception var25) {
                  Tools.eLog(var25, 0);
               }
            }

            this.object4UniquePrint.normalPrintAfterKPrint = false;
         }
      } catch (MainPrinter.NoSelectedPageException var28) {
         this.batchJpgPrint.errorList.add(" - Nincs nyomtatásra kijelölt oldal");
      } catch (Exception var29) {
         ErrorList.getInstance().writeError(new Integer(2000), "hiba a nyomtatáskor", var29, (Object)null);
         var2.setOk(false);
         var2.errorList.add("Hiba a nyomtatáskor!");
      } finally {
         if (var1 == this.ciklusVege - 1 && var2.isOk()) {
            var2.errorList.add("A kivonatolt nyomtatvány képe " + var3 + " néven elkészült.");
         }

         try {
            if (var1 == this.ciklusVege - 1) {
               this.document.close();
            }
         } catch (Exception var24) {
            var24.printStackTrace();
         }

      }

      return var2;
   }

   private boolean handleOnlyUniqueAutoPrint() {
      boolean var1 = false;
      int var2 = 0;

      boolean var3;
      for(var3 = false; var2 < this.lapvalasztoCB.length && !var3; ++var2) {
         if (this.lapvalasztoCB[var2].isSelected()) {
            var3 = true;
         }
      }

      if (!var3) {
         for(int var4 = 0; var4 < this.lapvalasztoCB.length; ++var4) {
            var2 = (Integer)this.indexInfoACBhoz.get(this.lapvalasztoCB[var4]);
            if (this.lapok[var2].getLma().uniquePrintable) {
               this.lapok[var2].getLma().setNyomtatando(true);
               this.lapvalasztoCB[var4].setSelected(true);
               var1 = true;
            }
         }
      }

      return var1;
   }

   private void handlePrintDialog() throws MainPrinter.PrintCancelledException {
      printJob = PrinterJob.getPrinterJob();
      if (!printJob.printDialog()) {
         throw new MainPrinter.PrintCancelledException();
      } else {
         this.ps = printJob.getPrintService();
         this.printerAlreadySelected = true;
      }
   }

   private void setPaper2Defaults(Lap var1) {
      PageFormat var2 = printJob.defaultPage();
      PageFormat var3 = printJob.defaultPage();
      var2.setOrientation(1);
      var3.setOrientation(0);
      Paper var4 = var2.getPaper();

      try {
         nyomtatoMargo = Integer.parseInt(SettingsStore.getInstance().get("printer", "print.settings.margin"));
      } catch (NumberFormatException var6) {
         nyomtatoMargo = 2;
      }

      var4.setImageableArea((double)nyomtatoMargo * 2.8D, (double)nyomtatoMargo * 2.8D, var2.getWidth() - (double)nyomtatoMargo * 2.8D, var2.getHeight() - (double)nyomtatoMargo * 2.8D);
      var2.setPaper(var4);
      var3.setPaper(var4);
      if (var1.getLma().alloLap) {
         var1.setPf(var2);
      } else {
         var1.setPf(var3);
      }

   }

   private void setPdfPreviewFile() {
      try {
         File var1 = new File(autoFillPdfPrevFileName);
         var1.deleteOnExit();
         pdfPreviewFilesInSession.add(var1);
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
      }

   }

   public static String getFootText() {
      String var0 = "Kitöltő verzió:" + "v.3.44.0".substring(2) + " Nyomtatvány verzió:" + sablonVerzio;
      return var0;
   }

   public static void logPrinting() {
      if (!emptyPrint) {
         MainFrame.thisinstance.writePrintLog();
      }
   }

   private void handleHtmlCreation(String var1) {
      Object var2 = PropertyList.getInstance().get("html.create.xml.silent");
      if (var2 != null) {
         FileWriter var3 = null;

         try {
            var3 = new FileWriter((String)var2);
            var3.write(var1);
         } catch (Exception var13) {
            PropertyList.getInstance().set("html.create.xml.silent", "Hiba a html fájl létrehozásakor: " + var13.toString());
         } finally {
            try {
               var3.close();
            } catch (Exception var12) {
            }

         }

      }
   }

   private void handleTooManyBRException() {
      if (PropertyList.getInstance().get("brCountError") != null) {
         if (!this.csoport) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, PropertyList.getInstance().get("brCountError"), "Hiba", 0);
         } else {
            try {
               this.batchJpgPrint.errorList.add(PropertyList.getInstance().get("brCountError"));
            } catch (Exception var2) {
               Tools.eLog(var2, 0);
            }
         }
      }

      PropertyList.getInstance().set("brCountError", (Object)null);
   }

   private void executePdfviewerOnMac(String var1, String var2) throws Exception {
      try {
         Runtime.getRuntime().exec(new String[]{var1, var2});
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   private void saveSettings(JDialog var1) {
      SettingsStore var2 = SettingsStore.getInstance();
      var2.set("printer_settings", "width", var1.getWidth() + "");
      var2.set("printer_settings", "height", var1.getHeight() + "");
      var2.set("printer_settings", "xPos", var1.getLocation().x + "");
      var2.set("printer_settings", "yPos", var1.getLocation().y + "");
   }

   private void loadSettings(JDialog var1) {
      SettingsStore var6 = SettingsStore.getInstance();

      int var2;
      try {
         var2 = Integer.parseInt(var6.get("printer_settings", "width"));
      } catch (Exception var11) {
         var2 = var1.getWidth();
      }

      int var3;
      try {
         var3 = Integer.parseInt(var6.get("printer_settings", "height"));
      } catch (Exception var10) {
         var3 = var1.getHeight();
      }

      int var4;
      try {
         var4 = Integer.parseInt(var6.get("printer_settings", "xPos"));
      } catch (Exception var9) {
         var4 = var1.getX();
      }

      int var5;
      try {
         var5 = Integer.parseInt(var6.get("printer_settings", "yPos"));
      } catch (Exception var8) {
         var5 = var1.getY();
      }

      var1.setLocation(var4, var5);
      var1.setSize(new Dimension(var2, var3));
      var1.setPreferredSize(var1.getSize());
   }

   public class CheckResult implements IResult {
      int errorCount = -1;

      public void setResult(Object var1) {
         try {
            this.errorCount = (Integer)((Integer)((Object[])((Object[])var1))[0]);
         } catch (Exception var3) {
            var3.printStackTrace();
            this.errorCount = -1;
         }

      }
   }

   private class PrintCancelledException extends Exception {
      public PrintCancelledException() {
         super("Mégsem nyomtatunk!");
      }
   }

   private class NoSelectedPageException extends Exception {
      public NoSelectedPageException() {
         super("Nincs nyomtatásra kijelölt oldal!");
      }
   }

   public class TempObject4SuperPages {
      public boolean normalPrintAfterKPrint = false;
      public Vector<Lap> uniqueDataPages = new Vector();
      public Vector<Book> uniquePrintablePages = new Vector();
   }
}
