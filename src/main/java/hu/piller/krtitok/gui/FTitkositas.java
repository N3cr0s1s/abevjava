package hu.piller.krtitok.gui;

import hu.piller.kripto.keys.KeyWrapper;
import hu.piller.kripto.keys.StoreManager;
import hu.piller.kripto.keys.StoreWrapper;
import hu.piller.krtitok.KriptoApp;
import hu.piller.tools.TableSorter;
import hu.piller.xml.FinishException;
import hu.piller.xml.abev.element.DocMetaData;
import hu.piller.xml.abev.parser.BoritekParser3;
import me.necrocore.abevjava.NecroFile;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

public class FTitkositas extends JDialog implements KeyWrapperHolder {
   private int minWidth = 630;
   private int minHeight = 280;
   private int minJTextField = 550;
   private int yShift = 0;
   private int minWidthFejlecKep = 30;
   private int wKorrekcioPx = 0;
   private int minWidthFejlecKulcstipus = 50;
   private int minWidthFejlecAlgoritmus = 40;
   private int minWidthFejlecAzonosito = 120;
   private int minWidthFejlecLetrehozas = 120;
   private Container alapGrafikaiPanel = null;
   private int curentFontYHeight = 0;
   private Vector keys;
   private int maxKeyCount;
   private String plainFilePath;
   private String encryptedFilePath;
   private DocMetaData metaData;
   private FKulcsTarak fk;
   private String lastPlainPath;
   private String lastMfPath;
   private JScrollPane commonScrollPane;
   private JPanel PMain;
   private JButton BMegsem;
   private JButton BTitkositas;
   private JLabel LTitkFajlNeve;
   private JLabel LMetaFajlNeve;
   private JLabel LTitkFajlUtvonala;
   private JPanel PCimzettekListaja;
   private JButton BCimzettHozzadasa;
   private JButton BCimzettTorlese;
   private JScrollPane SPCimzettekListaja;
   private JTable TCimzettekListaja;
   private JTextField TFTitkositandoFajlNeve;
   private JTextField TFMetaFajlNeve;
   private JTextField TFTitkositottFajlUtvonala;
   private JButton BTitkositandoFajlNeve;
   private JButton BMetaFajlNeve;
   private JButton BTitkositottFajlUtvonala;

   public FTitkositas(JFrame owner, boolean modal) {
      super(owner, modal);
      this.setMinWidth();
      this.setYShift();
      this.initComponents();
      this.keys = new Vector();
      this.maxKeyCount = 5;
      this.plainFilePath = KriptoApp.getKRDIR_TITKOSITATLAN();
      this.encryptedFilePath = KriptoApp.getKRDIR_KULDENDO();
      this.TFTitkositottFajlUtvonala.setText(this.encryptedFilePath);
      this.TFTitkositottFajlUtvonala.setToolTipText(this.TFTitkositottFajlUtvonala.getText());
      this.buildTCimzettekListaja();
   }

   private void BMetaFajlNeveActionPerformed(ActionEvent e) {
      FKrOrMfLista fkrl = new FKrOrMfLista("mf", this, true);
      if (fkrl != null) {
         this.TFMetaFajlNeve.setToolTipText(fkrl.getTFAllomanyokHelye().getText());
      }

      fkrl.setVisible(true);
   }

   private void BTitkositandoFajlNeveActionPerformed(ActionEvent e) {
      JFileChooser fc = new JFileChooser(KriptoApp.getKRDIR_TITKOSITATLAN());
      fc.setFileSelectionMode(2);
      fc.addChoosableFileFilter(new FileFilter() {
         public boolean accept(File f) {
            if (f.isDirectory()) {
               return true;
            } else {
               String name = f.getName().toLowerCase();
               return name.endsWith(".xml") || name.endsWith(".abv") || name.endsWith(".dat");
            }
         }

         public String getDescription() {
            return "Nyomtatványok *.xml, *.abv, *.dat";
         }
      });
      fc.showOpenDialog(this);
      if (fc.getSelectedFile() != null) {
         this.plainFilePath = fc.getSelectedFile().getAbsolutePath();
         if (fc.getSelectedFile().isDirectory()) {
            this.lastPlainPath = this.plainFilePath;
         } else {
            this.lastPlainPath = this.plainFilePath.substring(0, this.plainFilePath.lastIndexOf(File.separator));
         }

         KriptoApp.logger.info("I3302", (Object)this.plainFilePath);
         this.TFTitkositandoFajlNeve.setText(this.plainFilePath);
         this.seekMetaFile(fc.getSelectedFile().getName());
         this.TFTitkositandoFajlNeve.setToolTipText(this.TFTitkositandoFajlNeve.getText());
      }

   }

   private void seekMetaFile(String fileNev) {
      Vector paths = new Vector();
      if (this.lastPlainPath != null && this.lastPlainPath.length() > 0) {
         paths.add(this.lastPlainPath.trim());
      }

      KriptoApp.getPATH_KRDIR_TITKOSITATLAN();
      paths.add(KriptoApp.getKRDIR_TITKOSITATLAN());
      Enumeration dirs = paths.elements();

      while(true) {
         File[] mfFiles;
         do {
            if (!dirs.hasMoreElements()) {
               return;
            }

            String dir = (String)dirs.nextElement();
            mfFiles = (new NecroFile(dir)).listFiles(new FilenameFilter() {
               public boolean accept(File dir, String name) {
                  return (new NecroFile(dir, name)).isFile() && name.endsWith(".mf");
               }
            });
         } while(mfFiles == null);

         for(int i = 0; i < mfFiles.length; ++i) {
            try {
               BoritekParser3 bp = new BoritekParser3(new FileInputStream(mfFiles[i]), (OutputStream)null, BoritekParser3.PARSE_HEADER);

               try {
                  bp.start();
               } catch (FinishException var9) {
               }

               if (bp.getMetaData().getFileNev().trim().indexOf(fileNev.trim()) != -1) {
                  this.TFMetaFajlNeve.setText(mfFiles[i].getAbsolutePath());
                  this.setMetaData(bp.getMetaData());
                  return;
               }
            } catch (Exception var10) {
               KriptoApp.logger.debug((Throwable)var10);
            }
         }
      }
   }

   private void seekPlainFile(String fileNev) {
      if (this.TFTitkositandoFajlNeve.getText().trim().indexOf(fileNev.trim()) == -1) {
         Vector paths = KriptoApp.getPATH_KRDIR_TITKOSITATLAN();
         if (this.lastMfPath != null && this.lastMfPath.length() > 0) {
            paths.add(this.lastMfPath.trim());
         }

         paths.add(KriptoApp.getKRDIR_TITKOSITATLAN());
         Enumeration dirs = paths.elements();

         while(true) {
            File[] plainFiles;
            do {
               if (!dirs.hasMoreElements()) {
                  return;
               }

               String dir = (String)dirs.nextElement();
               plainFiles = (new NecroFile(dir)).listFiles(new FilenameFilter() {
                  public boolean accept(File dir, String name) {
                     return (new NecroFile(dir, name)).isFile() && !name.endsWith(".mf");
                  }
               });
            } while(plainFiles == null);

            for(int i = 0; i < plainFiles.length; ++i) {
               if (plainFiles[i].getName().trim().indexOf(fileNev.trim()) != -1) {
                  this.plainFilePath = plainFiles[i].getAbsolutePath();
                  this.TFTitkositandoFajlNeve.setText(plainFiles[i].getAbsolutePath());
                  return;
               }
            }
         }
      }
   }

   private void BTitkositottFajlUtvonalaActionPerformed(ActionEvent e) {
      JFileChooser fc = new JFileChooser();
      fc.setCurrentDirectory(new NecroFile(this.encryptedFilePath));
      fc.setFileSelectionMode(1);
      fc.showOpenDialog(this);
      if (fc.getSelectedFile() != null) {
         this.encryptedFilePath = fc.getSelectedFile().getAbsolutePath();
         KriptoApp.logger.info("I3304", (Object)this.encryptedFilePath);
         this.TFTitkositottFajlUtvonala.setText(this.encryptedFilePath);
         this.TFTitkositottFajlUtvonala.setToolTipText(this.TFTitkositottFajlUtvonala.getText());
      }

   }

   private void BCimzettHozzadasaActionPerformed(ActionEvent e) {
      if (this.getCount() <= 0) {
         JOptionPane.showMessageDialog(this, "Elérte a maximálisan megadható címzettek számát!");
      } else {
         this.fk = new FKulcsTarak(this, this.getCount(), false, true, true);
         this.fk.setVisible(true);
         KriptoApp.logger.warning("W3305");
      }

   }

   private void BCimzettTorleseActionPerformed(ActionEvent e) {
      if (this.TCimzettekListaja.getSelectedRow() >= 0) {
         this.keys.remove(this.TCimzettekListaja.getSelectedRow());
         JOptionPane.showMessageDialog(this, KriptoApp.resources.getString("M3307"), "Üzenet", 1);
         this.buildTCimzettekListaja();
         KriptoApp.logger.info("I3307");
      }

   }

   private void BTitkositasActionPerformed(ActionEvent e) {
      this.encryptedFilePath = this.encryptedFilePath.lastIndexOf(File.separator) == this.encryptedFilePath.length() - 1 ? this.encryptedFilePath : this.encryptedFilePath + File.separator;
      KriptoApp kriptoApp = KriptoApp.getInstance();
      KeyWrapper[] recipients = new KeyWrapper[this.keys.size()];

      for(int i = 0; i < this.keys.size(); ++i) {
         recipients[i] = (KeyWrapper)this.keys.elementAt(i);
      }

      String titkositottFileName = this.encryptedFilePath + File.separator + (new NecroFile(this.plainFilePath)).getName() + ".kr";

      try {
         this.metaData.addParam("digitalisalairas", "true");
         kriptoApp.encrypt(this.metaData, this.plainFilePath, titkositottFileName, recipients, true, false);
         KriptoApp.logger.info("I3011", new Object[]{titkositottFileName});
         JOptionPane.showMessageDialog(this, KriptoApp.resources.getString("M3011"), "Titkosítás", 1);
      } catch (Exception var6) {
         KriptoApp.logger.debug((Throwable)var6);
         KriptoApp.logger.warning("W3012", new Object[]{this.plainFilePath});
         JOptionPane.showMessageDialog(this, KriptoApp.resources.getString("M3012"), "Titkosítás", 2);
      }

      this.dispose();
   }

   public void setTFMetaFajlNeve(String mfFajlNev) {
      this.lastMfPath = mfFajlNev.substring(0, mfFajlNev.lastIndexOf(File.separator));
      this.TFMetaFajlNeve.setText(mfFajlNev);
   }

   public void setMetaData(DocMetaData md) {
      this.metaData = md;

      try {
         if (md.getCimzettNyilvanosKulcs() != null) {
            Vector keys = StoreManager.loadStore((InputStream)(new ByteArrayInputStream(md.getCimzettNyilvanosKulcs().getBytes())), (char[])null).listKeys();
            Enumeration en = keys.elements();

            for(int var4 = 0; en.hasMoreElements(); ++var4) {
               KeyWrapper kw = (KeyWrapper)en.nextElement();
               if (kw.getPgpPubKey() != null && kw.getPgpPubKey().isMasterKey()) {
                  this.addKeyWrapper(kw);
               }
            }
         }
      } catch (KeyStoreException var6) {
         var6.printStackTrace();
      } catch (NoSuchAlgorithmException var7) {
         var7.printStackTrace();
      } catch (UnrecoverableKeyException var8) {
         var8.printStackTrace();
      }

      this.seekPlainFile(this.metaData.getFileNev());
   }

   private void BMegsemActionPerformed(ActionEvent e) {
      this.setVisible(false);
      this.dispose();
   }

   private void thisWindowOpened(WindowEvent e) {
      if (KriptoApp.getUSER_KEY_AUTOMATIC_USE()) {
         try {
            StoreWrapper sw = StoreManager.loadStore(KriptoApp.getUSER_KEY_PATH_PUB(), "".toCharArray());
            Vector kws = sw.listKeys();
            if (kws.size() == 1) {
               this.addKeyWrappers(kws);
            }
         } catch (NoSuchAlgorithmException var4) {
            var4.printStackTrace();
         } catch (UnrecoverableKeyException var5) {
            var5.printStackTrace();
         } catch (KeyStoreException var6) {
            var6.printStackTrace();
         } catch (FileNotFoundException var7) {
            var7.printStackTrace();
         }
      }

   }

   private void initComponents() {
      this.commonScrollPane = new JScrollPane();
      this.PMain = new JPanel();
      this.BMegsem = new JButton();
      this.BTitkositas = new JButton();
      this.LTitkFajlNeve = new JLabel();
      this.LMetaFajlNeve = new JLabel();
      this.LTitkFajlUtvonala = new JLabel();
      this.PCimzettekListaja = new JPanel();
      this.BCimzettHozzadasa = new JButton();
      this.BCimzettTorlese = new JButton();
      this.SPCimzettekListaja = new JScrollPane();
      this.TCimzettekListaja = new JTable();
      this.TFTitkositandoFajlNeve = new JTextField();
      this.TFMetaFajlNeve = new JTextField();
      this.TFTitkositottFajlUtvonala = new JTextField();
      this.BTitkositandoFajlNeve = new JButton();
      this.BMetaFajlNeve = new JButton();
      this.BTitkositottFajlUtvonala = new JButton();
      JLabel jl = new JLabel("Titkosítás célkönyvtára:");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      jl.setFont(fm.getFont());
      int curentFontYHeight = jl.getFontMetrics(jl.getFont()).getHeight();
      int yKorrekcio = curentFontYHeight * 14 + 30;
      int yKorrekcioPx = this.minHeight;
      if (this.minHeight < yKorrekcio) {
         yKorrekcioPx = yKorrekcio;
      }

      this.setMinimumSize(new Dimension(this.minWidth, yKorrekcioPx));
      this.setTitle("Titkosítás");
      this.setResizable(true);
      if (600 < yKorrekcioPx) {
         this.setMinimumSize(new Dimension(1700, 800));
         this.setPreferredSize(new Dimension(1700, 800));
         if (curentFontYHeight >= 48) {
            this.setMinimumSize(new Dimension(2800, yKorrekcioPx));
         }

         this.setResizable(true);
      }

      this.addWindowListener(new WindowAdapter() {
         public void windowOpened(WindowEvent e) {
            FTitkositas.this.thisWindowOpened(e);
         }
      });
      this.alapGrafikaiPanel = new JPanel();
      this.alapGrafikaiPanel.setLayout((LayoutManager)null);
      this.PMain.setLayout((LayoutManager)null);
      this.BMegsem.setText("Mégsem");
      this.BMegsem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FTitkositas.this.BMegsemActionPerformed(e);
         }
      });
      this.PMain.add(this.BMegsem);
      int kozosX = this.getMinWidth("Mégsem") + 50;
      this.BMegsem.setBounds(new Rectangle(new Point(this.minWidth - kozosX, 5), this.BMegsem.getPreferredSize()));
      this.BTitkositas.setText("Titkosítás");
      this.BTitkositas.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FTitkositas.this.BTitkositasActionPerformed(e);
         }
      });
      this.PMain.add(this.BTitkositas);
      int i = this.getMinWidth("Mégsem Titkosítás") + 100;
      this.BTitkositas.setBounds(new Rectangle(new Point(this.minWidth - i, 5), this.BTitkositas.getPreferredSize()));
      Dimension preferredSize = new Dimension();

      for(int ix = 0; ix < this.PMain.getComponentCount(); ++ix) {
         Rectangle bounds = this.PMain.getComponent(ix).getBounds();
         preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
         preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
      }

      Insets insets = this.PMain.getInsets();
      preferredSize.width += insets.right;
      preferredSize.height += insets.bottom;
      this.PMain.setPreferredSize(preferredSize);
      this.alapGrafikaiPanel.add(this.PMain);
      this.PMain.setBounds(0, yKorrekcioPx - this.BTitkositas.getHeight(), this.minWidth, this.BTitkositas.getHeight() + 10);
      this.LTitkFajlNeve.setText("Titkosítandó fájl neve:");
      this.alapGrafikaiPanel.add(this.LTitkFajlNeve);
      this.LTitkFajlNeve.setBounds(new Rectangle(new Point(25, 15), this.LTitkFajlNeve.getPreferredSize()));
      this.LMetaFajlNeve.setText("Meta fájl neve:");
      this.alapGrafikaiPanel.add(this.LMetaFajlNeve);
      this.LMetaFajlNeve.setBounds(new Rectangle(new Point(25, 50 + this.yShift), this.LMetaFajlNeve.getPreferredSize()));
      this.LTitkFajlUtvonala.setText("Titkosítás célkönyvtára:");
      this.alapGrafikaiPanel.add(this.LTitkFajlUtvonala);
      this.LTitkFajlUtvonala.setBounds(new Rectangle(new Point(25, 85 + 2 * this.yShift), this.LTitkFajlUtvonala.getPreferredSize()));
      this.PCimzettekListaja.setBorder(new BevelBorder(1));
      this.PCimzettekListaja.setLayout((LayoutManager)null);
      this.BCimzettHozzadasa.setText("+");
      this.BCimzettHozzadasa.setToolTipText("Hozzáadás címzettek listájához");
      this.BCimzettHozzadasa.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FTitkositas.this.BCimzettHozzadasaActionPerformed(e);
         }
      });
      this.PCimzettekListaja.add(this.BCimzettHozzadasa);
      if (40 > curentFontYHeight) {
         this.BCimzettHozzadasa.setBounds(this.minWidth - 110, 45, 45 + this.yShift / 3, this.BCimzettHozzadasa.getPreferredSize().height + this.yShift / 2);
      } else {
         this.BCimzettHozzadasa.setBounds(this.minWidth - 110, 45, 45 + this.yShift / 2, this.BCimzettHozzadasa.getPreferredSize().height + this.yShift / 2);
      }

      this.BCimzettTorlese.setText("-");
      this.BCimzettTorlese.setToolTipText("Törlés címzettek listájából");
      this.BCimzettTorlese.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FTitkositas.this.BCimzettTorleseActionPerformed(e);
         }
      });
      this.PCimzettekListaja.add(this.BCimzettTorlese);
      if (40 > curentFontYHeight) {
         this.BCimzettTorlese.setBounds(this.minWidth - 110, 85 + 2 * this.yShift, 45 + this.yShift / 3, this.BCimzettTorlese.getPreferredSize().height + this.yShift / 2);
      } else {
         this.BCimzettTorlese.setBounds(this.minWidth - 110, 85 + 2 * this.yShift, 45 + this.yShift / 2, this.BCimzettTorlese.getPreferredSize().height + this.yShift / 2);
      }

      this.TCimzettekListaja.setModel(new DefaultTableModel());
      this.SPCimzettekListaja.setViewportView(this.TCimzettekListaja);
      this.PCimzettekListaja.add(this.SPCimzettekListaja);
      this.SPCimzettekListaja.setBounds(0, 0, this.minWidth - 120, 140 + (int)(3.2D * (double)this.yShift));
      this.SPCimzettekListaja.setBackground(Color.YELLOW);
      Dimension preferredSize1 = new Dimension();

      for(i = 0; i < this.PCimzettekListaja.getComponentCount(); ++i) {
         Rectangle bounds = this.PCimzettekListaja.getComponent(i).getBounds();
         preferredSize1.width = Math.max(bounds.x + bounds.width, preferredSize1.width);
         preferredSize1.height = Math.max(bounds.y + bounds.height, preferredSize1.height);
      }

      Insets insets1 = this.PCimzettekListaja.getInsets();
      preferredSize1.width += insets1.right;
      preferredSize1.height += insets1.bottom;
      this.PCimzettekListaja.setPreferredSize(preferredSize1);
      this.alapGrafikaiPanel.add(this.PCimzettekListaja);
      if (40 > curentFontYHeight) {
         this.PCimzettekListaja.setBounds(25, 115 + 4 * this.yShift, this.minWidth - 40, 140 + (int)(3.3D * (double)this.yShift));
      } else {
         this.PCimzettekListaja.setBounds(25, 115 + 4 * this.yShift, this.minWidth - 10, 140 + (int)(3.3D * (double)this.yShift));
      }

      kozosX = this.getMinWidth("Titkosítás célkönyvtára:");
      this.TFTitkositandoFajlNeve.setBackground(SystemColor.text);
      this.TFTitkositandoFajlNeve.setEditable(false);
      this.alapGrafikaiPanel.add(this.TFTitkositandoFajlNeve);
      this.TFTitkositandoFajlNeve.setBounds(kozosX + 50, 15, 380 + this.wKorrekcioPx, this.TFTitkositandoFajlNeve.getPreferredSize().height);
      this.TFMetaFajlNeve.setBackground(SystemColor.text);
      this.TFMetaFajlNeve.setEditable(false);
      this.alapGrafikaiPanel.add(this.TFMetaFajlNeve);
      this.TFMetaFajlNeve.setBounds(kozosX + 50, 50 + this.yShift, 380 + this.wKorrekcioPx, this.TFMetaFajlNeve.getPreferredSize().height);
      this.TFTitkositottFajlUtvonala.setBackground(SystemColor.text);
      this.TFTitkositottFajlUtvonala.setEditable(false);
      this.alapGrafikaiPanel.add(this.TFTitkositottFajlUtvonala);
      this.TFTitkositottFajlUtvonala.setBounds(kozosX + 50, 85 + 2 * this.yShift, 380 + this.wKorrekcioPx, this.TFTitkositottFajlUtvonala.getPreferredSize().height);
      this.BTitkositandoFajlNeve.setText("...");
      this.BTitkositandoFajlNeve.setToolTipText(".xml vagy .abv");
      this.BTitkositandoFajlNeve.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FTitkositas.this.BTitkositandoFajlNeveActionPerformed(e);
         }
      });
      this.alapGrafikaiPanel.add(this.BTitkositandoFajlNeve);
      this.BTitkositandoFajlNeve.setBounds(kozosX + this.TFTitkositandoFajlNeve.getWidth() + 60, 15, 45, 20 + this.yShift);
      this.BMetaFajlNeve.setText("...");
      this.BMetaFajlNeve.setToolTipText(".mf");
      this.BMetaFajlNeve.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FTitkositas.this.BMetaFajlNeveActionPerformed(e);
         }
      });
      this.alapGrafikaiPanel.add(this.BMetaFajlNeve);
      this.BMetaFajlNeve.setBounds(kozosX + this.TFMetaFajlNeve.getWidth() + 60, 50 + this.yShift, 45, 20 + this.yShift);
      this.BTitkositottFajlUtvonala.setText("...");
      this.BTitkositottFajlUtvonala.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FTitkositas.this.BTitkositottFajlUtvonalaActionPerformed(e);
         }
      });
      this.alapGrafikaiPanel.add(this.BTitkositottFajlUtvonala);
      this.BTitkositottFajlUtvonala.setBounds(kozosX + this.TFTitkositottFajlUtvonala.getWidth() + 60, 85 + 2 * this.yShift, 45, 20 + this.yShift);
      this.TCimzettekListaja.setRowHeight(this.TFTitkositandoFajlNeve.getHeight());
      this.TFTitkositandoFajlNeve.setToolTipText(this.TFTitkositandoFajlNeve.getText());
      this.TFMetaFajlNeve.setToolTipText(this.TFMetaFajlNeve.getText());
      this.TFTitkositottFajlUtvonala.setToolTipText(this.TFTitkositottFajlUtvonala.getText());
      Dimension preferredSize2 = new Dimension();

      for(int iy = 0; iy < this.alapGrafikaiPanel.getComponentCount(); ++iy) {
         Rectangle bounds = this.alapGrafikaiPanel.getComponent(iy).getBounds();
         preferredSize2.width = Math.max(bounds.x + bounds.width, preferredSize2.width);
         preferredSize2.height = Math.max(bounds.y + bounds.height, preferredSize2.height);
      }

      Insets insets2 = this.alapGrafikaiPanel.getInsets();
      preferredSize2.width += insets2.right;
      preferredSize2.height += insets2.bottom;
      ((JComponent)this.alapGrafikaiPanel).setPreferredSize(preferredSize2);
      this.commonScrollPane.setPreferredSize(new Dimension(655, 305));
      this.commonScrollPane.setViewportView(this.alapGrafikaiPanel);
      this.getContentPane().add(this.commonScrollPane);
      this.pack();
      this.setLocationRelativeTo(this.getOwner());
   }

   public void addKeyWrappers(Vector keys) {
      if (this.keys == null) {
         this.keys = new Vector();
      }

      Enumeration en = keys.elements();

      while(en.hasMoreElements()) {
         KeyWrapper kw = (KeyWrapper)en.nextElement();
         this.addKeyWrapper(kw);
      }

   }

   public void addKeyWrapper(KeyWrapper kw) {
      boolean duplicate = false;
      Iterator it = this.keys.iterator();
      KeyWrapper kw2 = null;

      while(it.hasNext()) {
         kw2 = (KeyWrapper)it.next();
         if (kw.equals(kw2)) {
            duplicate = true;
            break;
         }
      }

      if (!duplicate) {
         this.keys.add(kw);
         this.buildTCimzettekListaja();
         KriptoApp.logger.info("I3306", (Object)("alias: " + kw.getAlias()));
         if (this.fk != null && this.fk.isVisible()) {
            this.fk.setVisible(false);
         }
      } else {
         JOptionPane.showMessageDialog(this, KriptoApp.resources.getString("M3305") + "\nA kulcs már szerepel a listában!", "Üzenet", 2);
         KriptoApp.logger.warning("W3305");
      }

   }

   public int getCount() {
      return this.maxKeyCount - this.keys.size();
   }

   private void buildTCimzettekListaja() {
      TableSorter ts = new TableSorter(new RecipientTableModel(this.keys));
      ts.setTableHeader(this.TCimzettekListaja.getTableHeader());
      this.TCimzettekListaja.setModel(ts);
      this.TCimzettekListaja.getColumnModel().getColumn(0).setCellRenderer(new KeyImageRenderer());
      this.TCimzettekListaja.getColumnModel().getColumn(1).setCellRenderer(new KeyTypeNameRenderer());
      this.minWidthFejlecKep = this.getMinWidth(ts.getColumnName(0).toString()) + 20;
      this.minWidthFejlecKulcstipus = this.getMinWidth(ts.getColumnName(1).toString()) + 20;
      this.minWidthFejlecAlgoritmus = this.getMinWidth(ts.getColumnName(2).toString()) + 20;
      this.minWidthFejlecAzonosito = this.getMinWidth(ts.getColumnName(3).toString()) + 120;
      this.minWidthFejlecLetrehozas = this.getMinWidth(ts.getColumnName(4).toString()) + 120;
      this.TCimzettekListaja.getColumnModel().getColumn(0).setPreferredWidth(this.minWidthFejlecKep);
      this.TCimzettekListaja.getColumnModel().getColumn(0).setMaxWidth(this.minWidthFejlecKep);
      this.TCimzettekListaja.getColumnModel().getColumn(1).setPreferredWidth(this.minWidthFejlecKulcstipus);
      this.TCimzettekListaja.getColumnModel().getColumn(2).setPreferredWidth(this.minWidthFejlecAlgoritmus);
      this.TCimzettekListaja.getColumnModel().getColumn(3).setPreferredWidth(this.minWidthFejlecAzonosito);
      this.TCimzettekListaja.getColumnModel().getColumn(4).setPreferredWidth(this.minWidthFejlecLetrehozas);
      this.TCimzettekListaja.setCellSelectionEnabled(false);
   }

   private void setMinWidth() {
      JLabel jl = new JLabel("Meta fájl neve:");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      this.minWidth = fm.stringWidth(jl.getText()) * 8;
   }

   private int getMinWidth(String aktSzoveg) {
      JLabel jl = new JLabel(aktSzoveg);
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
       return fm.stringWidth(jl.getText());
   }

   private void setYShift() {
      JLabel jl = new JLabel("Titkosítás célkönyvtára:");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      jl.setFont(fm.getFont());
      int curentFontYHeight = jl.getFontMetrics(jl.getFont()).getHeight();
      curentFontYHeight -= 16;
      Float pontosit = 0.0F;
      if (1 <= curentFontYHeight && 5 >= curentFontYHeight) {
         pontosit = 0.6F;
      } else if (6 <= curentFontYHeight && 8 >= curentFontYHeight) {
         pontosit = 0.65F;
         this.wKorrekcioPx = 100;
      } else if (9 <= curentFontYHeight && 15 >= curentFontYHeight) {
         pontosit = 0.75F;
         this.wKorrekcioPx = 250;
      } else if (16 <= curentFontYHeight && 20 >= curentFontYHeight) {
         pontosit = 0.83F;
         this.wKorrekcioPx = 300;
      } else if (21 <= curentFontYHeight && 25 >= curentFontYHeight) {
         pontosit = 0.85F;
         this.wKorrekcioPx = 350;
      } else if (26 <= curentFontYHeight && 32 >= curentFontYHeight) {
         pontosit = 0.9F;
         this.wKorrekcioPx = 400;
      } else if (33 <= curentFontYHeight && 37 >= curentFontYHeight) {
         pontosit = 0.95F;
         this.wKorrekcioPx = 450;
      } else if (38 <= curentFontYHeight && 42 >= curentFontYHeight) {
         pontosit = 1.02F;
         this.wKorrekcioPx = 500;
      } else if (43 <= curentFontYHeight && 47 >= curentFontYHeight) {
         pontosit = 1.3F;
         this.wKorrekcioPx = 550;
      } else if (48 <= curentFontYHeight && 52 >= curentFontYHeight) {
         pontosit = 1.5F;
         this.wKorrekcioPx = 590;
      } else if (53 <= curentFontYHeight && 56 >= curentFontYHeight) {
         pontosit = 1.8F;
         this.wKorrekcioPx = 620;
      }

      this.yShift = (int)((float)curentFontYHeight * pontosit);
   }
}
