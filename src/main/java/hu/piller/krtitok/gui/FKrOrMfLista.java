package hu.piller.krtitok.gui;

import hu.piller.krtitok.KriptoApp;
import hu.piller.tools.TableSorter;
import hu.piller.xml.FinishException;
import hu.piller.xml.abev.element.DocMetaData;
import hu.piller.xml.abev.parser.BoritekParser3;
import me.necrocore.abevjava.NecroFile;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

public class FKrOrMfLista extends JDialog {
   public static final String LAllomanyokHelye_TEXT = "Állomanyok helye:";
   private int minWidth = 550;
   private int minHeight = 280;
   public static final String MODE_KR = "kr";
   public static final String MODE_MF = "mf";
   private static final String LABEL_MF = "Metaállományok helye: ";
   private static final String LABEL_KR = "Titkosított állományok helye: ";
   private static final String TITLE_MF = "Metaállományok";
   private static final String TITLE_KR = "Titkosított állományok";
   private JDialog owner;
   public File krOrMfFile;
   public MetaDataTableModel metaDataTableModel;
   File[] files;
   public String mukodesiMod;
   public String path;
   private JScrollPane commonScrollPane;
   private JPanel PButton;
   private JButton BMegsem;
   private JButton BRendben;
   private JPanel PFiles;
   private JScrollPane SPTKrAdatai;
   private JTable TKrAdatai;
   private JLabel LAllomanyokHelye;
   private JTextField TFAllomanyokHelye;
   private JButton BAllomanyokHelye;

   public JTextField getTFAllomanyokHelye() {
      return this.TFAllomanyokHelye;
   }

   public FKrOrMfLista(String krOrMf, JDialog owner, boolean modal) {
      super(owner, modal);
      this.owner = owner;
      this.mukodesiMod = krOrMf;
      this.setResizable(true);
      this.setMinWidth();
      this.initComponents();
      if (krOrMf.equalsIgnoreCase("kr")) {
         this.setTitle("Titkosított állományok");
         this.setPath(new NecroFile(this.path = KriptoApp.getKRDIR_LETOLTOTT()));
      } else {
         this.setTitle("Metaállományok");
         this.setPath(new NecroFile(this.path = KriptoApp.getKRDIR_TITKOSITATLAN()));
      }

   }

   private void setPath(File file) {
      if (!file.exists()) {
         file = new NecroFile(file.getParent());
      }

      this.path = file.getPath();
      this.TFAllomanyokHelye.setText(this.path);
      this.TFAllomanyokHelye.setToolTipText(this.TFAllomanyokHelye.getText());
      this.loadFiles(this.path);
   }

   private void loadFiles(String fileName) {
      this.files = new NecroFile[0];
      File file = new NecroFile(fileName);
      if (file.isDirectory()) {
         this.files = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
               if (FKrOrMfLista.this.mukodesiMod.equalsIgnoreCase("kr")) {
                  return name.endsWith(".kr");
               } else {
                  return FKrOrMfLista.this.mukodesiMod.equalsIgnoreCase("mf") ? name.endsWith(".mf") : false;
               }
            }
         });
      } else if (this.mukodesiMod.equalsIgnoreCase("kr") && fileName.endsWith(".kr") || this.mukodesiMod.equalsIgnoreCase("mf") && fileName.endsWith(".mf")) {
         this.files = new NecroFile[1];
         this.files[0] = file;
      }

      this.metaDataTableModel = new MetaDataTableModel(this.mukodesiMod);

      for(int i = 0; i < this.files.length; ++i) {
         DocMetaData md = loadKrOrMf(this.files[i].getAbsolutePath());
         if (md != null) {
            this.metaDataTableModel.addDocMetaData(this.files[i].getAbsolutePath(), md);
         }
      }

      this.TKrAdatai.setModel(new TableSorter(this.metaDataTableModel));
      if (this.TKrAdatai.getRowCount() > 0) {
         this.TKrAdatai.getSelectionModel().setSelectionInterval(0, 0);
      }

   }

   public static DocMetaData loadKrOrMf(String fileName) {
      try {
         FileInputStream fin = new FileInputStream(fileName);
         BoritekParser3 bp = new BoritekParser3(fin, (OutputStream)null, BoritekParser3.PARSE_HEADER);

         try {
            bp.start();
         } catch (FinishException var4) {
         }

         return bp.getMetaData();
      } catch (Exception var5) {
         KriptoApp.logger.debug((Throwable)var5);
         return null;
      }
   }

   private void BRendbenActionPerformed(ActionEvent e) {
      if (this.TKrAdatai.getRowCount() == 0) {
         JOptionPane.showMessageDialog(this, "Üres vagy hiányzó adatok!", "Figyelmeztető üzenet", 2);
      } else {
         if (this.mukodesiMod.equalsIgnoreCase("mf")) {
            int selectedRow;
            if (this.files.length == 1) {
               selectedRow = 0;
               if (this.TKrAdatai.getSelectedRow() != -1) {
                  selectedRow = this.TKrAdatai.getSelectedRow();
               }

               ((FTitkositas)this.owner).setTFMetaFajlNeve(this.files[selectedRow].getAbsolutePath());
               ((FTitkositas)this.owner).setMetaData(this.metaDataTableModel.getMetaData(selectedRow));
               KriptoApp.logger.info("I3303", (Object)this.files[selectedRow].getAbsolutePath());
            } else if (this.files.length >= 1) {
               selectedRow = this.TKrAdatai.getSelectedRow();
               if (selectedRow == -1) {
                  JOptionPane.showMessageDialog(this, "Válasszon egy metaállományt!", "Üzenet", 1);
                  return;
               }

               ((FTitkositas)this.owner).setTFMetaFajlNeve(this.files[selectedRow].getAbsolutePath());
               ((FTitkositas)this.owner).setMetaData(this.metaDataTableModel.getMetaData(selectedRow));
               KriptoApp.logger.info("I3303", (Object)this.files[selectedRow].getAbsolutePath());
            }
         }

         if (this.mukodesiMod.equalsIgnoreCase("kr")) {
            int[] rows = this.TKrAdatai.getSelectedRows();

            for(int k = 0; k < rows.length; ++k) {
               int row = rows[k];
               String fileName = (String)this.metaDataTableModel.getValueAt(row, 0);
               DocMetaData md = this.metaDataTableModel.getMetaData(row);
               ((FKititkositas)this.owner).addKrFile(fileName, md);
            }

            ((FKititkositas)this.owner).buildTKrAllomanyok();
            KriptoApp.logger.info("I4003");
         }

         this.setVisible(false);
      }
   }

   private void BMegsemActionPerformed(ActionEvent e) {
      this.setVisible(false);
   }

   private void BAllomanyokHelyeActionPerformed(ActionEvent e) {
      JFileChooser fc;
      if (this.TFAllomanyokHelye.getText() != null && this.TFAllomanyokHelye.getText().length() != 0) {
         fc = new JFileChooser(this.TFAllomanyokHelye.getText());
      } else {
         fc = new JFileChooser();
      }

      fc.setFileSelectionMode(2);
      fc.setFileFilter(new FileFilter() {
         public boolean accept(File f) {
            if (f.isDirectory()) {
               return true;
            } else {
               String name = f.getName();
               if (FKrOrMfLista.this.mukodesiMod.equalsIgnoreCase("kr")) {
                  return name.endsWith(".kr");
               } else {
                  return FKrOrMfLista.this.mukodesiMod.equalsIgnoreCase("mf") ? name.endsWith(".mf") : false;
               }
            }
         }

         public String getDescription() {
            return FKrOrMfLista.this.mukodesiMod.equalsIgnoreCase("kr") ? "Kr állományok (*.kr)" : "Metaállományok (*.mf)";
         }
      });
      fc.showOpenDialog((Component)null);
      if (fc.getSelectedFile() != null) {
         this.setPath(fc.getSelectedFile());
      }

   }

   private void initComponents() {
      this.setPreferredSize(new Dimension(570, 310));
      this.commonScrollPane = new JScrollPane();
      this.PFiles = new JPanel();
      this.SPTKrAdatai = new JScrollPane();
      this.TKrAdatai = new JTable();
      this.LAllomanyokHelye = new JLabel();
      this.TFAllomanyokHelye = new JTextField();
      this.BAllomanyokHelye = new JButton();
      this.PButton = new JPanel();
      this.BMegsem = new JButton();
      this.BRendben = new JButton();
      Container contentPane = new JPanel();
      contentPane.setLayout(new AbsoluteLayout());
      JLabel jl = new JLabel("Állomanyok helye:");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      jl.setFont(fm.getFont());
      int curentFontYHeight = jl.getFontMetrics(jl.getFont()).getHeight();
      KriptoApp.logger.info("curentFontYHeight: " + curentFontYHeight);
      this.PButton.setLayout(new AbsoluteLayout());
      this.BMegsem.setText("Mégsem");
      this.BMegsem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKrOrMfLista.this.BMegsemActionPerformed(e);
         }
      });
      this.BRendben.setText("Rendben");
      this.BRendben.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKrOrMfLista.this.BRendbenActionPerformed(e);
         }
      });
      this.PFiles.setLayout(new AbsoluteLayout());
      this.TKrAdatai.setModel(new DefaultTableModel());
      this.SPTKrAdatai.setViewportView(this.TKrAdatai);
      this.LAllomanyokHelye.setText("Állomanyok helye:");
      this.LAllomanyokHelye.setBounds(10, 10, 115, 20);
      this.TFAllomanyokHelye.setEditable(false);
      this.BAllomanyokHelye.setText("...");
      this.BAllomanyokHelye.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKrOrMfLista.this.BAllomanyokHelyeActionPerformed(e);
         }
      });
      this.TKrAdatai.setRowHeight(this.TFAllomanyokHelye.getPreferredSize().height);
      int xApozTop = 10;
      int lApozX = 10;
      int tth2 = this.TFAllomanyokHelye.getPreferredSize().height;
      int tth3 = xApozTop + tth2 + 5;
      final int tempApozTop = xApozTop;
      xApozTop = this.minmax(0, tempApozTop, (tth3 - tth2) / 2);
      int lApozY = xApozTop + 3;
      int lAsizW = fm.stringWidth("Állomanyok helye:");
      Graphics grcontext = jl.getGraphics();
      Rectangle2D xbounds = fm.getStringBounds("Állomanyok helye:", grcontext);
      int lAsizH = (int)xbounds.getHeight();
      Dimension bAllomanyPrefSize = this.BAllomanyokHelye.getPreferredSize();
      int bAsizW = bAllomanyPrefSize.width;
      int bApozX = this.minWidth - bAsizW - 10;
      int tApozX = lApozX + lAsizW + 10;
      int tAsizW = this.minWidth - 10 - lAsizW - 20 - bAsizW - 10;
      int tAsizH = this.TFAllomanyokHelye.getPreferredSize().height;
      this.PFiles.add(this.LAllomanyokHelye, new AbsoluteConstraints(lApozX, lApozY, lAsizW, lAsizH));
      this.PFiles.add(this.TFAllomanyokHelye, new AbsoluteConstraints(tApozX, xApozTop, tAsizW, tAsizH));
      this.PFiles.add(this.BAllomanyokHelye, new AbsoluteConstraints(bApozX, xApozTop, bAsizW, tAsizH));
      int sAsizH = 20 + Math.round((float)((curentFontYHeight + 1) * 9));
      this.PFiles.add(this.SPTKrAdatai, new AbsoluteConstraints(10, tth3, this.minWidth - 15, sAsizH));
      Dimension bMprefSize = this.BMegsem.getPreferredSize();
      int bMpozX = this.minWidth - bMprefSize.width - 10;
      int bMpozY = 5;
      Dimension bRprefSize = this.BRendben.getPreferredSize();
      int bRpozX = this.minWidth - bMprefSize.width - 20 - bRprefSize.width;
      this.PButton.add(this.BMegsem, new AbsoluteConstraints(bMpozX, bMpozY, bMprefSize.width, bMprefSize.height));
      this.PButton.add(this.BRendben, new AbsoluteConstraints(bRpozX, bMpozY, bRprefSize.width, bRprefSize.height));
      int pFilesSizeH = this.PFiles.getPreferredSize().height + 10;
      int pButtonPosY = pFilesSizeH + 1;
      contentPane.add(this.PFiles, new AbsoluteConstraints(0, 0, this.minWidth, pFilesSizeH));
      contentPane.add(this.PButton, new AbsoluteConstraints(0, pButtonPosY, this.minWidth, bRprefSize.height + 10));
      Dimension panelPreferredSize = contentPane.getPreferredSize();
      Dimension fixedSize = new Dimension(0, 0);
      fixedSize.width = this.minmax(550, 1700, this.minWidth);
      fixedSize.height = this.minmax(280, 900, pButtonPosY + this.PButton.getPreferredSize().height);
      this.commonScrollPane.setPreferredSize(fixedSize);
      this.commonScrollPane.setViewportView(contentPane);
      this.getContentPane().add(this.commonScrollPane, "Center");
      this.setResizable(false);
      if (panelPreferredSize.width > 500) {
         fixedSize.width = this.minmax(550, 1700, this.minWidth + 12 + (int)((double)(curentFontYHeight - 16) * 0.1D));
         fixedSize.height = this.minmax(280, 900, panelPreferredSize.height + 35 + (int)((double)(curentFontYHeight - 16) * 0.2D));
         this.setPreferredSize(fixedSize);
      }

      this.pack();
      this.setLocationRelativeTo((Component)null);
   }

   private int minmax(int a, int b, int x) {
      if (x < a) {
         x = a;
      }

      if (b < x) {
         x = b;
      }

      return x;
   }

   private void setMinWidth() {
      JLabel jl = new JLabel("Állomanyok helye:");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      fm.stringWidth(jl.getText());
      if (this.minWidth < fm.stringWidth(jl.getText()) * 5) {
         this.minWidth = fm.stringWidth(jl.getText()) * 5;
      }

   }
}
