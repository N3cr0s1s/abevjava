package hu.piller.krtitok.gui;

import hu.piller.kripto.keys.StoreManager;
import hu.piller.krtitok.IniReadAndWrite;
import hu.piller.krtitok.KriptoApp;
import me.necrocore.abevjava.NecroFile;

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
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

public class FBeallitasok extends JDialog {
   private int minWidth = 595;
   private int minHeight = 460;
   private int yShift = 0;
   private int wKorrekcioPx = 0;
   private Container alapGrafikaPanel = null;
   private JScrollPane commonScrollPane;
   private JPanel panel1;
   private JButton BRendben;
   private JButton BMegsem;
   private JPanel panel2;
   private JLabel label1;
   private JTextField tFMyPublicKey;
   private JButton bMyPublicKey;
   private JLabel label2;
   private JCheckBox cbMyKeyAutomaticUse;
   private JLabel label3;
   private JTextField tFMyPrivateKey;
   private JButton bMyPrivateKey;
   private JTextField tFMyKrDir;
   private JLabel label4;
   private JTextField tFMyTitkositatlan;
   private JLabel label5;
   private JTextField tFMyLetoltott;
   private JLabel label6;
   private JTextField tFMyKuldendo;
   private JLabel label7;
   private JTextField tFMyElkuldott;

   public FBeallitasok(JFrame owner, boolean modal) {
      super(owner, modal);
      this.setMinWidth();
      this.setYShift();
      this.initComponents();
      this.setXButton();
   }

   private void BMegsemActionPerformed(ActionEvent e) {
      this.setVisible(false);
   }

   private void BRendbenActionPerformed(ActionEvent e) {
      IniReadAndWrite IRAndW = new IniReadAndWrite();
      IRAndW.writeIni(this.tFMyPrivateKey.getText(), this.tFMyPublicKey.getText(), this.cbMyKeyAutomaticUse.isSelected(), this.tFMyKrDir.getText(), this.tFMyTitkositatlan.getText(), this.tFMyLetoltott.getText(), this.tFMyKuldendo.getText(), this.tFMyElkuldott.getText());
      KriptoApp.loadConfig();
      this.setVisible(false);
   }

   private void bMyPublicKeyActionPerformed(ActionEvent e) {
      JFileChooser fc = this.getStoreChooser(false);
      String cur = this.tFMyPublicKey.getText();
      File startDir = null;
      if (cur != null) {
         File curDir = new NecroFile(cur);
         if (curDir.exists()) {
            if (curDir.isFile()) {
               startDir = new NecroFile(curDir.getParent());
            } else {
               startDir = curDir;
            }
         }
      }

      if (startDir != null) {
         fc.setCurrentDirectory(startDir);
      }

      fc.showOpenDialog(this);
      if (fc.getSelectedFile() != null) {
         this.tFMyPublicKey.setText(fc.getSelectedFile().getPath());
         this.tFMyPublicKey.setToolTipText(this.tFMyPublicKey.getText());
      }

      KriptoApp.logger.info("I7002");
   }

   private void thisWindowOpened(WindowEvent e) {
      IniReadAndWrite IRAndW = new IniReadAndWrite();
      IRAndW.readIni();
      Properties prop = IRAndW.getProperties();
      if (IRAndW.getProperties() != null) {
         this.writeIntoAllTextFields(prop);
      }

   }

   private void bMyPrivateKeyActionPerformed(ActionEvent e) {
      String cur = this.tFMyPrivateKey.getText();
      File startDir = null;
      if (cur != null) {
         File curDir = new NecroFile(cur);
         if (curDir.exists()) {
            if (curDir.isFile()) {
               startDir = new NecroFile(curDir.getParent());
            } else {
               startDir = curDir;
            }
         }
      }

      JFileChooser fc = this.getStoreChooser(true);
      if (startDir != null) {
         fc.setCurrentDirectory(startDir);
      }

      fc.showOpenDialog(this);
      if (fc.getSelectedFile() != null) {
         this.tFMyPrivateKey.setText(fc.getSelectedFile().getPath());
         this.tFMyPrivateKey.setToolTipText(this.tFMyPrivateKey.getText());
      }

      KriptoApp.logger.info("I7003");
   }

   private void writeIntoAllTextFields(Properties prop) {
      this.tFMyPrivateKey.setText(prop.getProperty("SAJAT_TITKOS_KULCS"));
      this.tFMyPublicKey.setText(prop.getProperty("SAJAT_NYILVANOS_KULCS"));
      this.cbMyKeyAutomaticUse.setSelected(Boolean.valueOf(prop.getProperty("SAJAT_AUTOMATIKUS")));
      this.tFMyKrDir.setText(prop.getProperty("KRDIR"));
      this.tFMyTitkositatlan.setText(prop.getProperty("TITKOSITASRA_VARO"));
      this.tFMyLetoltott.setText(prop.getProperty("LETOLTOTT"));
      this.tFMyKuldendo.setText(prop.getProperty("KULDENDO"));
      this.tFMyElkuldott.setText(prop.getProperty("ELKULDOT"));
      this.tFMyPrivateKey.setToolTipText(this.tFMyPrivateKey.getText());
      this.tFMyPublicKey.setToolTipText(this.tFMyPublicKey.getText());
      this.tFMyKrDir.setToolTipText(this.tFMyKrDir.getText());
      this.tFMyTitkositatlan.setToolTipText(this.tFMyTitkositatlan.getText());
      this.tFMyLetoltott.setToolTipText(this.tFMyLetoltott.getText());
      this.tFMyKuldendo.setToolTipText(this.tFMyKuldendo.getText());
      this.tFMyElkuldott.setToolTipText(this.tFMyElkuldott.getText());
   }

   private JFileChooser getStoreChooser(boolean priv) {
      JFileChooser fc = new JFileChooser(new NecroFile(""));
      fc.setFileSelectionMode(0);
      fc.addChoosableFileFilter(new FileFilter() {
         public boolean accept(File f) {
            if (f.isDirectory()) {
               return true;
            } else {
               String name = f.getName().toLowerCase();
               return name.endsWith(StoreManager.EXT_P12[0]) || name.endsWith(StoreManager.EXT_P12[1]);
            }
         }

         public String getDescription() {
            return "P12 szabvány *." + StoreManager.EXT_P12[0] + ", *." + StoreManager.EXT_P12[1];
         }
      });
      if (!priv) {
         fc.addChoosableFileFilter(new FileFilter() {
            public boolean accept(File f) {
               if (f.isDirectory()) {
                  return true;
               } else {
                  String name = f.getName().toLowerCase();
                  return name.endsWith(StoreManager.EXT_CER[0]) || name.endsWith(StoreManager.EXT_CER[1]) || name.endsWith(StoreManager.EXT_CER[2]);
               }
            }

            public String getDescription() {
               return "Tanúsítvány X.509 *." + StoreManager.EXT_CER[0] + ", *." + StoreManager.EXT_CER[1] + ", *." + StoreManager.EXT_CER[2];
            }
         });
      }

      fc.addChoosableFileFilter(new FileFilter() {
         public boolean accept(File f) {
            if (f.isDirectory()) {
               return true;
            } else {
               String name = f.getName().toLowerCase();
               return name.endsWith(StoreManager.EXT_JKS[0]) || name.endsWith(StoreManager.EXT_JKS[1]);
            }
         }

         public String getDescription() {
            return "Java Key Store *." + StoreManager.EXT_JKS[0] + ", *." + StoreManager.EXT_JKS[1];
         }
      });
      fc.addChoosableFileFilter(new FileFilter() {
         public boolean accept(File f) {
            if (f.isDirectory()) {
               return true;
            } else {
               String name = f.getName().toLowerCase();
               return name.endsWith(StoreManager.EXT_PGP[0]) || name.endsWith(StoreManager.EXT_PGP[1]) || name.endsWith(StoreManager.EXT_PGP[2]);
            }
         }

         public String getDescription() {
            return "PGP kulcs *." + StoreManager.EXT_PGP[0] + ", *." + StoreManager.EXT_PGP[1] + ", *." + StoreManager.EXT_PGP[2];
         }
      });
      return fc;
   }

   private void initComponents() {
      this.commonScrollPane = new JScrollPane();
      this.panel1 = new JPanel();
      this.BRendben = new JButton();
      this.BMegsem = new JButton();
      this.panel2 = new JPanel();
      this.label1 = new JLabel();
      this.tFMyPublicKey = new JTextField();
      this.bMyPublicKey = new JButton();
      this.label2 = new JLabel();
      this.cbMyKeyAutomaticUse = new JCheckBox();
      this.label3 = new JLabel();
      this.tFMyPrivateKey = new JTextField();
      this.bMyPrivateKey = new JButton();
      this.tFMyKrDir = new JTextField();
      this.label4 = new JLabel();
      this.tFMyTitkositatlan = new JTextField();
      this.label5 = new JLabel();
      this.tFMyLetoltott = new JTextField();
      this.label6 = new JLabel();
      this.tFMyKuldendo = new JTextField();
      this.label7 = new JLabel();
      this.tFMyElkuldott = new JTextField();
      JLabel jl = new JLabel("Ha nem a KRDIR alapértelmezett könyvtár alá szeretne dolgozni, egyéb könyvtár megadása:");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      jl.setFont(fm.getFont());
      int curentFontYHeight = jl.getFontMetrics(jl.getFont()).getHeight();
      int yKorrekcio = curentFontYHeight * 20 + this.BRendben.getHeight();
      int yKorrekcioPx = this.minHeight;
      if (this.minHeight < yKorrekcio) {
         yKorrekcioPx = yKorrekcio;
      }

      this.setMinimumSize(new Dimension(this.minWidth + 80, yKorrekcioPx));
      this.setTitle("Beállítások");
      this.setResizable(true);
      if (800 < yKorrekcioPx) {
         this.setMinimumSize(new Dimension(1700, 900));
         this.setPreferredSize(new Dimension(1700, 900));
         this.setResizable(true);
      }

      this.addWindowListener(new WindowAdapter() {
         public void windowOpened(WindowEvent e) {
            FBeallitasok.this.thisWindowOpened(e);
         }
      });
      this.alapGrafikaPanel = new JPanel();
      this.alapGrafikaPanel.setLayout((LayoutManager)null);
      this.panel1.setLayout((LayoutManager)null);
      this.BRendben.setText("Rendben");
      this.BRendben.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FBeallitasok.this.BRendbenActionPerformed(e);
         }
      });
      this.panel1.add(this.BRendben);
      this.BRendben.setBounds(new Rectangle(new Point(385, 5), this.BRendben.getPreferredSize()));
      this.BMegsem.setText("Mégsem");
      this.BMegsem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FBeallitasok.this.BMegsemActionPerformed(e);
         }
      });
      this.panel1.add(this.BMegsem);
      this.BMegsem.setBounds(new Rectangle(new Point(475, 5), this.BMegsem.getPreferredSize()));
      Dimension preferredSize = new Dimension();

      int i;
      Rectangle bounds;
      for(i = 0; i < this.panel1.getComponentCount(); ++i) {
         bounds = this.panel1.getComponent(i).getBounds();
         preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
         preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
      }

      Insets insets = this.panel1.getInsets();
      preferredSize.width += insets.right;
      preferredSize.height += insets.bottom;
      this.panel1.setPreferredSize(preferredSize);
      this.alapGrafikaPanel.add(this.panel1);
      this.panel1.setBounds(5, yKorrekcioPx - this.BRendben.getHeight(), this.minWidth + 55, this.BRendben.getHeight() + 10);
      this.panel2.setLayout((LayoutManager)null);
      this.label1.setText("Saját nyilvános kulcsának helye:");
      this.panel2.add(this.label1);
      this.label1.setBounds(new Rectangle(new Point(10, 50 + 2 * this.yShift), this.label1.getPreferredSize()));
      this.tFMyPublicKey.setEditable(false);
      this.panel2.add(this.tFMyPublicKey);
      this.tFMyPublicKey.setBounds(10, 70 + 3 * this.yShift + 5, 475 + this.wKorrekcioPx, this.tFMyPublicKey.getPreferredSize().height);
      this.bMyPublicKey.setText("...");
      this.bMyPublicKey.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FBeallitasok.this.bMyPublicKeyActionPerformed(e);
         }
      });
      this.panel2.add(this.bMyPublicKey);
      this.bMyPublicKey.setBounds(this.tFMyPublicKey.getWidth() + 20, 67 + 3 * this.yShift, 50, this.bMyPublicKey.getPreferredSize().height);
      this.label2.setText("Saját magán kulcsának helye:");
      this.panel2.add(this.label2);
      this.label2.setBounds(new Rectangle(new Point(10, 5), this.label2.getPreferredSize()));
      this.cbMyKeyAutomaticUse.setText("Kívánja-e a saját kulcsát a titkosításhoz automatikusan használni?");
      this.cbMyKeyAutomaticUse.setHorizontalTextPosition(2);
      this.cbMyKeyAutomaticUse.setHorizontalAlignment(2);
      this.panel2.add(this.cbMyKeyAutomaticUse);
      this.cbMyKeyAutomaticUse.setIcon(FKriptodsk.getCheckboxIkonUres());
      this.cbMyKeyAutomaticUse.setSelectedIcon(FKriptodsk.getCheckboxIkonTeli());
      this.cbMyKeyAutomaticUse.setBounds(new Rectangle(new Point(10, 110 + 4 * this.yShift), this.cbMyKeyAutomaticUse.getPreferredSize()));
      this.label3.setText("Ha nem a KRDIR alapértelmezett könyvtár alá szeretne dolgozni, egyéb könyvtár megadása:");
      this.panel2.add(this.label3);
      this.label3.setBounds(new Rectangle(new Point(10, 145 + 5 * this.yShift), this.label3.getPreferredSize()));
      this.tFMyPrivateKey.setEditable(false);
      this.panel2.add(this.tFMyPrivateKey);
      this.tFMyPrivateKey.setBounds(10, 20 + this.yShift + 5, 475 + this.wKorrekcioPx, this.tFMyPrivateKey.getPreferredSize().height);
      this.bMyPrivateKey.setText("...");
      this.bMyPrivateKey.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FBeallitasok.this.bMyPrivateKeyActionPerformed(e);
         }
      });
      this.panel2.add(this.bMyPrivateKey);
      this.bMyPrivateKey.setBounds(this.tFMyPrivateKey.getWidth() + 20, 15 + this.yShift, 50, this.bMyPrivateKey.getPreferredSize().height);
      this.panel2.add(this.tFMyKrDir);
      this.tFMyKrDir.setBounds(10, 170 + 6 * this.yShift, 550, 20 + this.yShift);
      this.label4.setText("A titkosítatlan állományok elérési útvonala:");
      this.panel2.add(this.label4);
      this.label4.setBounds(new Rectangle(new Point(10, 200 + 7 * this.yShift), this.label4.getPreferredSize()));
      this.panel2.add(this.tFMyTitkositatlan);
      this.tFMyTitkositatlan.setBounds(10, 215 + 8 * this.yShift, 550, 20 + this.yShift);
      this.label5.setText("A letöltött állományok elérési útvonala:");
      this.panel2.add(this.label5);
      this.label5.setBounds(new Rectangle(new Point(10, 245 + 9 * this.yShift), this.label5.getPreferredSize()));
      this.panel2.add(this.tFMyLetoltott);
      this.tFMyLetoltott.setBounds(10, 260 + 10 * this.yShift, 550, 20 + this.yShift);
      this.label6.setText("A küldendő állományok elérési útvonala:");
      this.panel2.add(this.label6);
      this.label6.setBounds(new Rectangle(new Point(10, 290 + 11 * this.yShift), this.label6.getPreferredSize()));
      this.panel2.add(this.tFMyKuldendo);
      this.tFMyKuldendo.setBounds(10, 305 + 12 * this.yShift, 550, 20 + this.yShift);
      this.label7.setText("Az elküldött állományok elérési útvonala:");
      this.panel2.add(this.label7);
      this.label7.setBounds(new Rectangle(new Point(10, 335 + 13 * this.yShift), this.label7.getPreferredSize()));
      this.panel2.add(this.tFMyElkuldott);
      this.tFMyElkuldott.setBounds(10, 350 + 14 * this.yShift, 550, 20 + this.yShift);
      preferredSize = new Dimension();

      for(i = 0; i < this.panel2.getComponentCount(); ++i) {
         bounds = this.panel2.getComponent(i).getBounds();
         preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
         preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
      }

      insets = this.panel2.getInsets();
      preferredSize.width += insets.right;
      preferredSize.height += insets.bottom;
      this.panel2.setPreferredSize(preferredSize);
      this.alapGrafikaPanel.add(this.panel2);
      this.panel2.setBounds(5, 10, this.minWidth + 55, yKorrekcioPx);
      preferredSize = new Dimension();

      for(i = 0; i < this.alapGrafikaPanel.getComponentCount(); ++i) {
         bounds = this.alapGrafikaPanel.getComponent(i).getBounds();
         preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
         preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
      }

      insets = this.alapGrafikaPanel.getInsets();
      preferredSize.width += insets.right;
      preferredSize.height += insets.bottom;
      ((JComponent)this.alapGrafikaPanel).setPreferredSize(preferredSize);
      this.commonScrollPane.setPreferredSize(new Dimension(500, 480));
      this.commonScrollPane.setViewportView(this.alapGrafikaPanel);
      this.getContentPane().add(this.commonScrollPane);
      this.pack();
      this.setLocationRelativeTo((Component)null);
   }

   private void setMinWidth() {
      JLabel jl = new JLabel("Ha nem a KRDIR alapértelmezett könyvtár alá szeretne dolgozni, egyéb könyvtár megadása:");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      this.minWidth = fm.stringWidth(jl.getText());
   }

   private void setYShift() {
      JLabel jl = new JLabel("Ha nem a KRDIR alapértelmezett könyvtár alá szeretne dolgozni, egyéb könyvtár megadása:");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      jl.setFont(fm.getFont());
      int curentFontYHeight = jl.getFontMetrics(jl.getFont()).getHeight();
      curentFontYHeight -= 16;
      Float pontosit = 0.0F;
      if (1 <= curentFontYHeight && 5 >= curentFontYHeight) {
         pontosit = 0.6F;
      } else if (6 <= curentFontYHeight && 9 >= curentFontYHeight) {
         pontosit = 0.68F;
         this.wKorrekcioPx = 40;
      } else if (10 <= curentFontYHeight && 15 >= curentFontYHeight) {
         pontosit = 0.79F;
         this.wKorrekcioPx = 110;
      } else if (16 <= curentFontYHeight && 20 >= curentFontYHeight) {
         pontosit = 0.85F;
         this.wKorrekcioPx = 220;
      } else if (21 <= curentFontYHeight && 25 >= curentFontYHeight) {
         pontosit = 0.87F;
         this.wKorrekcioPx = 280;
      } else if (26 <= curentFontYHeight && 32 >= curentFontYHeight) {
         pontosit = 0.9F;
         this.wKorrekcioPx = 300;
      } else if (33 <= curentFontYHeight && 37 >= curentFontYHeight) {
         pontosit = 0.95F;
         this.wKorrekcioPx = 400;
      } else if (38 <= curentFontYHeight && 42 >= curentFontYHeight) {
         pontosit = 1.02F;
         this.wKorrekcioPx = 450;
      } else if (43 <= curentFontYHeight && 47 >= curentFontYHeight) {
         pontosit = 1.05F;
         this.wKorrekcioPx = 470;
      } else if (48 <= curentFontYHeight && 52 >= curentFontYHeight) {
         pontosit = 1.07F;
         this.wKorrekcioPx = 490;
      } else if (53 <= curentFontYHeight && 56 >= curentFontYHeight) {
         pontosit = 1.1F;
         this.wKorrekcioPx = 520;
      }

      this.yShift = (int)((float)curentFontYHeight * pontosit);
   }

   private void setXButton() {
      int windowWidth = this.alapGrafikaPanel.getWidth();
      int xRendben = windowWidth - this.BMegsem.getWidth() - 30 - this.BRendben.getWidth();
      int xMegseem = windowWidth - this.BMegsem.getWidth() - 30;
      this.BRendben.setBounds(xRendben, this.BRendben.getY(), this.BRendben.getWidth(), this.BRendben.getHeight());
      this.BMegsem.setBounds(xMegseem, this.BMegsem.getY(), this.BMegsem.getWidth(), this.BMegsem.getHeight());
   }
}
