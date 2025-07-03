package hu.piller.krtitok.gui;

import hu.piller.kripto.keys.KeyWrapper;
import hu.piller.krtitok.KriptoApp;
import hu.piller.tools.TableSorter;
import hu.piller.xml.MissingKeyException;
import hu.piller.xml.abev.element.DocMetaData;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
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
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

public class FKititkositas extends JDialog implements KeyWrapperHolder {
   private int minWidth = 685;
   private int minHeight = 220;
   private int yKorrekcioPx = 0;
   private int xTextField = 175;
   private String decryptPath;
   private Hashtable encFiles;
   private MetaDataTableModel mdtm;
   private KeyWrapper keyWrapper;
   private FKulcsTarak fk;
   private JPanel PAlsoGombok;
   private JButton BMegsem;
   private JButton BKititkositas;
   private JPanel PKititkositandoFajlNevek;
   private JScrollPane SPKititkositandoFajlNevek;
   private JTable TKrAllomanyok;
   private JButton BTitkFajlHozzaad;
   private JButton BTitkFajlElvesz;
   private JButton BDecryptPath;
   private JLabel LDecryptPath;
   private JTextField TFDecryptPath;

   public FKititkositas(JFrame owner, boolean modal) {
      super(owner, modal);
      this.setMinWidth();
      this.initComponents();
      this.encFiles = new Hashtable();
      this.decryptPath = KriptoApp.getKRDIR_LETOLTOTT();
      this.TFDecryptPath.setText(this.decryptPath);
      this.TFDecryptPath.setToolTipText(this.TFDecryptPath.getText());
   }

   public void addKrFile(File file) {
      String fileName = file.getAbsolutePath();
      DocMetaData md = FKrOrMfLista.loadKrOrMf(file.getAbsolutePath());
      this.encFiles.put(fileName, md);
   }

   public void addKrFile(String fileName, DocMetaData md) {
      this.encFiles.put(fileName, md);
   }

   public void buildTKrAllomanyok() {
      this.mdtm = new MetaDataTableModel("kr");
      Enumeration keys = this.encFiles.keys();

      while(keys.hasMoreElements()) {
         String fileName = (String)keys.nextElement();
         DocMetaData md = (DocMetaData)this.encFiles.get(fileName);
         this.mdtm.addDocMetaData(fileName, md);
      }

      TableSorter sorter = new TableSorter(this.mdtm);
      sorter.setTableHeader(this.TKrAllomanyok.getTableHeader());
      this.TKrAllomanyok.setModel(sorter);
   }

   private void BDecryptPathActionPerformed(ActionEvent e) {
      JFileChooser fc = new JFileChooser();
      fc.setFileSelectionMode(1);
      fc.showOpenDialog((Component)null);
      if (fc.getSelectedFile() != null) {
         this.decryptPath = fc.getSelectedFile().getAbsolutePath();
         KriptoApp.logger.info("I4002", (Object)this.decryptPath);
         this.TFDecryptPath.setText(this.decryptPath);
         this.TFDecryptPath.setToolTipText(this.TFDecryptPath.getText());
      }

   }

   private void BMegsemActionPerformed(ActionEvent e) {
      this.setVisible(false);
   }

   private void decryptFiles() {
      Hashtable result = new Hashtable();
      KriptoApp kriptoApp = KriptoApp.getInstance();
      Enumeration en = this.encFiles.keys();

      while(en.hasMoreElements()) {
         String srcFile = (String)en.nextElement();
         String destFile = "";

         try {
            String srcFileName = srcFile.substring(srcFile.lastIndexOf(File.separator));
            DocMetaData md = (DocMetaData)this.encFiles.get(srcFile);
            if (md.getFileNev() != null && md.getFileNev().length() > 0) {
               destFile = this.decryptPath + File.separator + md.getFileNev();
            } else {
               destFile = this.decryptPath + File.separator + srcFileName;
            }

            kriptoApp.decryptToDir(this.keyWrapper.getKey((char[])null), srcFile, this.decryptPath);
            result.put(srcFile, Boolean.TRUE);
            KriptoApp.logger.info("I4005", new Object[]{srcFile, destFile});
         } catch (MissingKeyException var8) {
            result.put(srcFile, Boolean.FALSE);
            KriptoApp.logger.warning("W4006", new Object[]{srcFile, destFile});
         } catch (Exception var9) {
            result.put(srcFile, Boolean.FALSE);
            KriptoApp.logger.warning("W4006", new Object[]{srcFile, destFile});
         }
      }

      if (result.containsValue(Boolean.FALSE)) {
         JOptionPane.showMessageDialog(this, KriptoApp.resources.getString("M4006"), "Üzenet", 2);
      } else {
         JOptionPane.showMessageDialog(this, KriptoApp.resources.getString("M4005"), "Üzenet", 1);
      }

      this.setVisible(false);
   }

   private void BKititkositasActionPerformed(ActionEvent e) {
      JOptionPane.showMessageDialog(this, KriptoApp.resources.getString("M4007"), "Üzenet", 1);
      this.fk = new FKulcsTarak(this, 1, true, false, true);
      this.fk.setVisible(true);
   }

   private void BTitkFajlHozzaadActionPerformed(ActionEvent e) {
      FKrOrMfLista fKrOrMf = new FKrOrMfLista("kr", this, true);
      fKrOrMf.setVisible(true);
   }

   private void BTitkFajlElveszActionPerformed(ActionEvent e) {
      int[] rows = this.TKrAllomanyok.getSelectedRows();

      for(int k = 0; k < rows.length; ++k) {
         int row = rows[k];
         String fileName = (String)this.mdtm.getValueAt(row, 0);
         this.encFiles.remove(fileName);
         this.buildTKrAllomanyok();
      }

      KriptoApp.logger.info("I4004", (Object)e.getActionCommand());
   }

   private void thisWindowOpened(WindowEvent e) {
      JOptionPane.showMessageDialog(this, KriptoApp.resources.getString("M4008"), "Üzenet", 1);
      FKrOrMfLista fKrOrMf = new FKrOrMfLista("kr", this, true);
      fKrOrMf.setVisible(true);
   }

   private void initComponents() {
      this.PAlsoGombok = new JPanel();
      this.BMegsem = new JButton();
      this.BKititkositas = new JButton();
      this.PKititkositandoFajlNevek = new JPanel();
      this.SPKititkositandoFajlNevek = new JScrollPane();
      this.TKrAllomanyok = new JTable();
      this.BTitkFajlHozzaad = new JButton();
      this.BTitkFajlElvesz = new JButton();
      this.BDecryptPath = new JButton();
      this.LDecryptPath = new JLabel();
      this.TFDecryptPath = new JTextField();
      this.setTitle("Kititkosítás");
      this.setResizable(false);
      JLabel jl = new JLabel("Állomanyok helye:");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      jl.setFont(fm.getFont());
      int curentFontYHeight = jl.getFontMetrics(jl.getFont()).getHeight();
      int yKorrekcio = curentFontYHeight * 9;
      this.yKorrekcioPx = this.minHeight;
      if (this.minHeight < yKorrekcio) {
         this.yKorrekcioPx = yKorrekcio;
      }

      this.setMinimumSize(new Dimension(this.minWidth, this.yKorrekcioPx));
      this.addWindowListener(new WindowAdapter() {
         public void windowOpened(WindowEvent e) {
            FKititkositas.this.thisWindowOpened(e);
         }
      });
      Container contentPane = this.getContentPane();
      contentPane.setLayout((LayoutManager)null);
      this.PAlsoGombok.setLayout((LayoutManager)null);
      this.BMegsem.setText("Mégsem");
      this.BMegsem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKititkositas.this.BMegsemActionPerformed(e);
         }
      });
      this.PAlsoGombok.add(this.BMegsem);
      this.BMegsem.setBounds(new Rectangle(new Point(575, 10), this.BMegsem.getPreferredSize()));
      this.BKititkositas.setText("Kititkosítás");
      this.BKititkositas.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKititkositas.this.BKititkositasActionPerformed(e);
         }
      });
      this.PAlsoGombok.add(this.BKititkositas);
      this.BKititkositas.setBounds(new Rectangle(new Point(470, 10), this.BKititkositas.getPreferredSize()));
      Dimension preferredSize = new Dimension();

      int i;
      Rectangle bounds;
      for(i = 0; i < this.PAlsoGombok.getComponentCount(); ++i) {
         bounds = this.PAlsoGombok.getComponent(i).getBounds();
         preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
         preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
      }

      Insets insets = this.PAlsoGombok.getInsets();
      preferredSize.width += insets.right;
      preferredSize.height += insets.bottom;
      this.PAlsoGombok.setPreferredSize(preferredSize);
      contentPane.add(this.PAlsoGombok);
      this.PAlsoGombok.setBounds(0, this.yKorrekcioPx, this.minWidth, this.BMegsem.getHeight() + 15);
      this.BKititkositas.setBounds(this.minWidth - this.BMegsem.getWidth() - 20 - this.BKititkositas.getWidth(), 5, this.BKititkositas.getWidth(), this.BKititkositas.getHeight());
      this.BMegsem.setBounds(this.minWidth - this.BMegsem.getWidth() - 10, 5, this.BMegsem.getWidth(), this.BMegsem.getHeight());
      this.PKititkositandoFajlNevek.setOpaque(false);
      this.PKititkositandoFajlNevek.setBorder(new EtchedBorder());
      this.PKititkositandoFajlNevek.setLayout((LayoutManager)null);
      this.TKrAllomanyok.setModel(new DefaultTableModel());
      this.SPKititkositandoFajlNevek.setViewportView(this.TKrAllomanyok);
      this.PKititkositandoFajlNevek.add(this.SPKititkositandoFajlNevek);
      if (curentFontYHeight >= 48) {
         this.SPKititkositandoFajlNevek.setBounds(0, 30, this.minWidth - 25, this.yKorrekcioPx - 45 - this.BMegsem.getHeight() - 40);
      } else {
         this.SPKititkositandoFajlNevek.setBounds(0, 0, this.minWidth - 25, this.yKorrekcioPx - 45 - this.BMegsem.getHeight() - 40);
      }

      this.BTitkFajlHozzaad.setText("+");
      this.BTitkFajlHozzaad.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKititkositas.this.BTitkFajlHozzaadActionPerformed(e);
         }
      });
      this.PKititkositandoFajlNevek.add(this.BTitkFajlHozzaad);
      this.BTitkFajlHozzaad.setBounds(this.minWidth - this.BTitkFajlHozzaad.getWidth() - 85, this.yKorrekcioPx - 50 - this.BMegsem.getHeight(), 54, 17 + curentFontYHeight / 2);
      this.BTitkFajlElvesz.setText("-");
      this.BTitkFajlElvesz.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKititkositas.this.BTitkFajlElveszActionPerformed(e);
         }
      });
      this.PKititkositandoFajlNevek.add(this.BTitkFajlElvesz);
      this.BTitkFajlElvesz.setBounds(this.minWidth - this.BTitkFajlHozzaad.getWidth() - this.BTitkFajlHozzaad.getWidth() - 45, this.yKorrekcioPx - 50 - this.BMegsem.getHeight(), 54, 17 + curentFontYHeight / 2);
      preferredSize = new Dimension();

      for(i = 0; i < this.PKititkositandoFajlNevek.getComponentCount(); ++i) {
         bounds = this.PKititkositandoFajlNevek.getComponent(i).getBounds();
         preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
         preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
      }

      insets = this.PKititkositandoFajlNevek.getInsets();
      preferredSize.width += insets.right;
      preferredSize.height += insets.bottom;
      this.PKititkositandoFajlNevek.setPreferredSize(preferredSize);
      contentPane.add(this.PKititkositandoFajlNevek);
      if (curentFontYHeight >= 48) {
         this.PKititkositandoFajlNevek.setBounds(15, 44 + curentFontYHeight / 2, this.minWidth - 25, this.yKorrekcioPx - 45);
      } else {
         this.PKititkositandoFajlNevek.setBounds(15, 24 + curentFontYHeight / 2, this.minWidth - 25, this.yKorrekcioPx - 45);
      }

      this.BDecryptPath.setText("...");
      this.BDecryptPath.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKititkositas.this.BDecryptPathActionPerformed(e);
         }
      });
      contentPane.add(this.BDecryptPath);
      this.BDecryptPath.setBounds(this.minWidth - this.BTitkFajlHozzaad.getWidth() - 15, 10, 45, 12 + curentFontYHeight / 2);
      this.LDecryptPath.setText("Kititkosítás célkönyvtára:");
      contentPane.add(this.LDecryptPath);
      this.LDecryptPath.setBounds(new Rectangle(new Point(20, 10), this.LDecryptPath.getPreferredSize()));
      this.TFDecryptPath.setEditable(false);
      this.TFDecryptPath.setBackground(UIManager.getColor("text"));
      contentPane.add(this.TFDecryptPath);
      this.TFDecryptPath.setBounds(this.xTextField + 25, 10, 440, 10 + curentFontYHeight / 2);
      this.TKrAllomanyok.setRowHeight(this.TFDecryptPath.getHeight());
      preferredSize = new Dimension();

      for(i = 0; i < contentPane.getComponentCount(); ++i) {
         bounds = contentPane.getComponent(i).getBounds();
         preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
         preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
      }

      insets = contentPane.getInsets();
      preferredSize.width += insets.right;
      preferredSize.height += insets.bottom;
      ((JComponent)contentPane).setPreferredSize(preferredSize);
      this.pack();
      this.setLocationRelativeTo(this.getOwner());
   }

   public void addKeyWrappers(Vector keys) {
      KeyWrapper kw = (KeyWrapper)keys.elementAt(0);
      if (kw.getType() == 1) {
         this.keyWrapper = kw;
         JOptionPane.showMessageDialog(this, KriptoApp.resources.getString("M4009"), "Üzenet", 1);
         KriptoApp.logger.info("I4009", (Object)kw.getAlias());
         this.fk.setVisible(false);
         this.decryptFiles();
      } else {
         JOptionPane.showMessageDialog(this, KriptoApp.resources.getString("M4010") + "!\nA megadott kulcs nem privát kulcs!", "Üzenet", 2);
         KriptoApp.logger.info("W4010");
      }

   }

   public int getCount() {
      return this.keyWrapper != null ? 0 : 1;
   }

   private void setMinWidth() {
      JLabel jl = new JLabel("Kititkosítás célkönyvtára:");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      this.xTextField = fm.stringWidth(jl.getText());
      if ((double)this.minWidth < (double)fm.stringWidth(jl.getText()) * 3.2D) {
         this.minWidth = (int)((double)fm.stringWidth(jl.getText()) * 3.2D);
      }

   }
}
