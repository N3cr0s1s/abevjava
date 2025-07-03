package hu.piller.krtitok.gui;

import hu.piller.krtitok.KriptoApp;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class FTitKulcsGen extends JDialog {
   static String keyLength;
   private int minWidth = 540;
   private int minHeight = 330;
   private int yKorrekcioPx = 0;
   private int yShift = 0;
   private int wKorrekcioPx = 0;
   private JPanel panel1;
   private JButton BMegsem;
   private JButton BKulcsparGen;
   private JPanel panel2;
   private JLabel LKulcshossz;
   private JComboBox cBKeySize;
   private JLabel LKulcstarTipus;
   private JTextField tFKeyType;
   private JLabel LKulcstarHelye;
   private JLabel LKulcsparAlneve;
   private JTextField tFPrivateKey;
   private JTextField tFKeyAlias;
   private JLabel LJelszo2;
   private JPasswordField tFPasswordPrivate;
   private JLabel LKulcstarHelye2;
   private JTextField tFPublicKey;
   private JButton BKulcstarHelyeNyilvanos;
   private JLabel label1;
   private JTextField tFKeys;

   public FTitKulcsGen(JFrame owner, boolean modal) {
      super(owner, modal);
      this.setMinWidth();
      this.setYShift();
      this.initComponents();
   }

   private void BKulcsparGenActionPerformed(ActionEvent e) {
      if (this.tFPasswordPrivate.getPassword().length == 0) {
         JOptionPane.showMessageDialog(this, KriptoApp.resources.getString("M5003"));
         KriptoApp.logger.warning("W5003");
         this.tFPasswordPrivate.requestFocus();
      } else {
         char[] pwd = this.myPasswordDialog();
         if (!Arrays.equals(this.tFPasswordPrivate.getPassword(), pwd)) {
            JOptionPane.showMessageDialog(this, KriptoApp.resources.getString("M5002"));
            KriptoApp.logger.warning("W5002");
         } else if (!this.tFKeyAlias.getText().equals("") && this.tFPasswordPrivate.getPassword().length > 0) {
            try {
               KriptoApp kriptoApp = KriptoApp.getInstance();
               kriptoApp.keyGen(this.tFKeyType.getText(), (char[])null, (char[])this.tFPasswordPrivate.getPassword(), Integer.parseInt(this.cBKeySize.getModel().getSelectedItem().toString()), this.tFPrivateKey.getText(), this.tFPublicKey.getText(), this.tFKeyAlias.getText());
               JOptionPane.showMessageDialog(this, KriptoApp.resources.getString("M5010"), "Üzenet", 1);
               KriptoApp.logger.info("I5010", (Object)("alias: " + this.tFKeyAlias.getText() + ", kulcs: " + this.tFPrivateKey.getText()));
               KriptoApp.setUSER_KEY_PATH_PUB(this.tFPublicKey.getText());
               KriptoApp.setUSER_KEY_PATH_PRI(this.tFPrivateKey.getText());
               KriptoApp.setUSER_KEY_AUTOMATIC_USE(Boolean.TRUE);
               KriptoApp.saveConfig();
               this.dispose();
            } catch (Exception var4) {
               JOptionPane.showMessageDialog(this, KriptoApp.resources.getString("M5011"), "Titkosító kulcspár generálása", 2);
               KriptoApp.logger.warning("W5011", (Object)var4.getMessage());
               KriptoApp.logger.debug((Throwable)var4);
            }
         } else {
            JOptionPane.showMessageDialog(this, KriptoApp.resources.getString("M5003"), "Titkosító kulcspár generálása", 2);
            KriptoApp.logger.info("W5003");
         }
      }

   }

   public JFileChooser createFileChooser(String currentDir) {
      JFileChooser fc = new JFileChooser(new File(currentDir));
      fc.setFileSelectionMode(1);
      fc.showOpenDialog((Component)null);
      return fc;
   }

   private void thisWindowOpened(WindowEvent e) {
      this.tFKeyAlias.setText(System.getProperty("user.name"));
      this.tFKeys.setText(System.getProperty("user.home"));
      this.setKulcstarHelyeMagan(System.getProperty("user.home") + File.separator);
      this.setKulcstarHelyeNyilvanos(System.getProperty("user.home") + File.separator);
      this.tFKeyAlias.setToolTipText(this.tFKeyAlias.getText());
      this.tFPrivateKey.setToolTipText(this.tFPrivateKey.getText());
      this.tFPublicKey.setToolTipText(this.tFPublicKey.getText());
      this.tFKeys.setToolTipText(this.tFKeys.getText());
   }

   private void setKulcstarHelyeMagan(String dir) {
      dir = dir.lastIndexOf(File.separator) == dir.length() - 1 ? dir : dir + File.separator;
      this.tFPrivateKey.setText(dir + this.tFKeyAlias.getText() + "_prv.asc");
   }

   private void setKulcstarHelyeNyilvanos(String dir) {
      dir = dir.lastIndexOf(File.separator) == dir.length() - 1 ? dir : dir + File.separator;
      this.tFPublicKey.setText(dir + this.tFKeyAlias.getText() + "_pub.asc");
   }

   private void BKulcstarHelyeNyilvanosActionPerformed(ActionEvent e) {
      String currentPath = "";
      if (this.tFKeys.getText() != null) {
         currentPath = this.tFKeys.getText().substring(0, this.tFKeys.getText().lastIndexOf(File.separator));
      }

      JFileChooser fc = this.createFileChooser(currentPath);
      if (fc.getSelectedFile() != null) {
         this.tFKeys.setText(fc.getSelectedFile().getPath());
         this.tFKeys.setToolTipText(this.tFKeys.getText());
         this.setKulcstarHelyeNyilvanos(fc.getSelectedFile().getPath());
         this.setKulcstarHelyeMagan(fc.getSelectedFile().getPath());
      }

   }

   private void BMegsemActionPerformed(ActionEvent e) {
      this.tFPasswordPrivate.setText("");
      this.setVisible(false);
   }

   private void tFKeyAliasFocusLost(FocusEvent e) {
      String curenthPathWithKeyAlias = this.tFPublicKey.getText().substring(0, this.tFPublicKey.getText().lastIndexOf(File.separator));
      this.setKulcstarHelyeMagan(curenthPathWithKeyAlias);
      this.setKulcstarHelyeNyilvanos(curenthPathWithKeyAlias);
   }

   private void tFKeyAliasKeyReleased(KeyEvent e) {
      String curenthPathWithKeyAlias = this.tFPublicKey.getText().substring(0, this.tFPublicKey.getText().lastIndexOf(File.separator));
      this.setKulcstarHelyeMagan(curenthPathWithKeyAlias);
      this.setKulcstarHelyeNyilvanos(curenthPathWithKeyAlias);
   }

   public char[] myPasswordDialog() {
      JPanel jp = new JPanel(new FlowLayout());
      JPasswordField jpf = new JPasswordField(12);
      jp.add(new JLabel("Jelszó megerősítése"));
      jp.add(jpf);
      (new JOptionPane(jp, 3)).createDialog(this, "Jelszó megerősítése").show();
      return jpf.getPassword();
   }

   private void initComponents() {
      this.panel1 = new JPanel();
      this.BMegsem = new JButton();
      this.BKulcsparGen = new JButton();
      this.panel2 = new JPanel();
      this.LKulcshossz = new JLabel();
      this.cBKeySize = new JComboBox();
      this.LKulcstarTipus = new JLabel();
      this.tFKeyType = new JTextField();
      this.LKulcstarHelye = new JLabel();
      this.LKulcsparAlneve = new JLabel();
      this.tFPrivateKey = new JTextField();
      this.tFKeyAlias = new JTextField();
      this.LJelszo2 = new JLabel();
      this.tFPasswordPrivate = new JPasswordField();
      this.LKulcstarHelye2 = new JLabel();
      this.tFPublicKey = new JTextField();
      this.BKulcstarHelyeNyilvanos = new JButton();
      this.label1 = new JLabel();
      this.tFKeys = new JTextField();
      this.setTitle("Titkosító kulcspár generálása");
      this.setModal(true);
      this.setResizable(true);
      int xTextField = this.minWidth / 4 + 30;
      JLabel jl = new JLabel("Nyilvános kulcs");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      jl.setFont(fm.getFont());
      int curentFontYHeight = jl.getFontMetrics(jl.getFont()).getHeight();
      int yKorrekcio = curentFontYHeight * 13;
      this.yKorrekcioPx = this.minHeight;
      if (this.minHeight < yKorrekcio) {
         this.yKorrekcioPx = yKorrekcio;
      }

      this.setMinimumSize(new Dimension(this.minWidth + this.wKorrekcioPx, this.yKorrekcioPx));
      this.addWindowListener(new WindowAdapter() {
         public void windowOpened(WindowEvent e) {
            FTitKulcsGen.this.thisWindowOpened(e);
         }
      });
      Container contentPane = this.getContentPane();
      contentPane.setLayout((LayoutManager)null);
      this.panel1.setAlignmentX(10.0F);
      this.panel1.setAlignmentY(10.0F);
      this.panel1.setPreferredSize(new Dimension(483, 50));
      this.panel1.setLayout((LayoutManager)null);
      this.BMegsem.setText("Mégsem");
      this.BMegsem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FTitKulcsGen.this.BMegsemActionPerformed(e);
         }
      });
      this.panel1.add(this.BMegsem);
      this.BMegsem.setBounds(new Rectangle(new Point(360, 10), this.BMegsem.getPreferredSize()));
      this.BKulcsparGen.setText("Kulcspár generálás");
      this.BKulcsparGen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FTitKulcsGen.this.BKulcsparGenActionPerformed(e);
         }
      });
      this.panel1.add(this.BKulcsparGen);
      this.BKulcsparGen.setBounds(new Rectangle(new Point(205, 10), this.BKulcsparGen.getPreferredSize()));
      this.BKulcsparGen.setBounds(this.minWidth - this.BMegsem.getWidth() - 30 - this.BKulcsparGen.getWidth(), 5, this.BKulcsparGen.getWidth(), this.BKulcsparGen.getHeight());
      this.BMegsem.setBounds(this.minWidth - this.BMegsem.getWidth() - 20, 5, this.BMegsem.getWidth(), this.BMegsem.getHeight());
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
      contentPane.add(this.panel1);
      this.panel2.setLayout((LayoutManager)null);
      this.LKulcshossz.setText("Kulcshossz:");
      this.panel2.add(this.LKulcshossz);
      this.LKulcshossz.setBounds(new Rectangle(new Point(30, 15), this.LKulcshossz.getPreferredSize()));
      this.cBKeySize.setModel(new DefaultComboBoxModel(new String[]{"1024", "2048"}));
      this.panel2.add(this.cBKeySize);
      this.cBKeySize.setBounds(xTextField, 15, 95 + this.wKorrekcioPx / 5, 25 + this.yShift / 2);
      this.LKulcstarTipus.setText("Kulcstár típusa:");
      this.panel2.add(this.LKulcstarTipus);
      int xLKulcstarTipus = this.cBKeySize.getX() + this.cBKeySize.getWidth() + 10;
      this.LKulcstarTipus.setBounds(new Rectangle(new Point(xLKulcstarTipus, 15), this.LKulcstarTipus.getPreferredSize()));
      this.tFKeyType.setEnabled(false);
      this.tFKeyType.setText("PGP");
      this.tFKeyType.setHorizontalAlignment(0);
      this.panel2.add(this.tFKeyType);
      i = this.LKulcstarTipus.getX() + this.LKulcstarTipus.getWidth() + 10;
      this.tFKeyType.setBounds(i, 15, 79 + this.wKorrekcioPx / 9, 24 + this.yShift / 2);
      this.LKulcstarHelye.setText("Magán kulcs:");
      this.panel2.add(this.LKulcstarHelye);
      this.LKulcstarHelye.setBounds(new Rectangle(new Point(30, 135 + (int)(1.8D * (double)this.yShift)), this.LKulcstarHelye.getPreferredSize()));
      this.LKulcsparAlneve.setText("Kulcspár neve:");
      this.panel2.add(this.LKulcsparAlneve);
      this.LKulcsparAlneve.setBounds(new Rectangle(new Point(30, 55 + this.yShift / 2), this.LKulcsparAlneve.getPreferredSize()));
      this.tFPrivateKey.setEditable(false);
      this.panel2.add(this.tFPrivateKey);
      this.tFPrivateKey.setBounds(xTextField, 135 + (int)(1.7D * (double)this.yShift), 305 + this.wKorrekcioPx, 25 + this.yShift / 2);
      this.tFKeyAlias.addFocusListener(new FocusAdapter() {
         public void focusLost(FocusEvent e) {
            FTitKulcsGen.this.tFKeyAliasFocusLost(e);
         }
      });
      this.tFKeyAlias.addKeyListener(new KeyAdapter() {
         public void keyReleased(KeyEvent e) {
            FTitKulcsGen.this.tFKeyAliasKeyReleased(e);
         }
      });
      this.panel2.add(this.tFKeyAlias);
      this.tFKeyAlias.setBounds(xTextField, 55 + this.yShift / 2, 305 + this.wKorrekcioPx, 25 + this.yShift / 2);
      this.LJelszo2.setText("Jelszó:");
      this.panel2.add(this.LJelszo2);
      this.LJelszo2.setBounds(new Rectangle(new Point(30, 215 + 3 * this.yShift), this.LJelszo2.getPreferredSize()));
      this.panel2.add(this.tFPasswordPrivate);
      this.tFPasswordPrivate.setBounds(xTextField, 215 + (int)(2.9D * (double)this.yShift), 305 + this.wKorrekcioPx, 25 + this.yShift / 2);
      this.LKulcstarHelye2.setText("Nyilvános kulcs:");
      this.panel2.add(this.LKulcstarHelye2);
      this.LKulcstarHelye2.setBounds(new Rectangle(new Point(30, 175 + (int)(2.4D * (double)this.yShift)), this.LKulcstarHelye2.getPreferredSize()));
      this.tFPublicKey.setEditable(false);
      this.panel2.add(this.tFPublicKey);
      this.tFPublicKey.setBounds(xTextField, 175 + (int)(2.3D * (double)this.yShift), 305 + this.wKorrekcioPx, 25 + this.yShift / 2);
      this.BKulcstarHelyeNyilvanos.setText("...");
      this.BKulcstarHelyeNyilvanos.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FTitKulcsGen.this.BKulcstarHelyeNyilvanosActionPerformed(e);
         }
      });
      this.panel2.add(this.BKulcstarHelyeNyilvanos);
      this.label1.setText("Kulcspár helye:");
      this.panel2.add(this.label1);
      this.label1.setBounds(new Rectangle(new Point(30, 95 + (int)(1.2D * (double)this.yShift)), this.label1.getPreferredSize()));
      this.tFKeys.setEditable(false);
      this.panel2.add(this.tFKeys);
      this.tFKeys.setBounds(xTextField, 95 + (int)(1.1D * (double)this.yShift), 255 + this.wKorrekcioPx, 25 + this.yShift / 2);
      int xBKulcstarHelyeNyilvanos = this.tFKeys.getX() + this.tFKeys.getWidth() + 55;
      this.BKulcstarHelyeNyilvanos.setBounds(xBKulcstarHelyeNyilvanos, 95 + (int)(1.1D * (double)this.yShift), 35 + this.yShift / 2, 25 + this.yShift / 2);
      Dimension preferredSize1 = new Dimension();

      for(int ix = 0; ix < this.panel2.getComponentCount(); ++ix) {
         Rectangle bounds1 = this.panel2.getComponent(ix).getBounds();
         preferredSize1.width = Math.max(bounds1.x + bounds1.width, preferredSize1.width);
         preferredSize1.height = Math.max(bounds1.y + bounds1.height, preferredSize1.height);
      }

      Insets insets1 = this.panel2.getInsets();
      preferredSize1.width += insets1.right;
      preferredSize1.height += insets1.bottom;
      this.panel2.setPreferredSize(preferredSize1);
      contentPane.add(this.panel2);
      this.panel2.setBounds(0, 0, this.minWidth + this.wKorrekcioPx, 255 + 4 * this.yShift);
      this.panel1.setBounds(0, this.panel2.getHeight(), this.minWidth, this.BMegsem.getHeight() + 20);
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
      this.setSize(490, 335);
      this.setLocationRelativeTo((Component)null);
   }

   private void setMinWidth() {
      JLabel jl = new JLabel("Nyilvános kulcs:");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      if (this.minWidth < fm.stringWidth(jl.getText()) * 4) {
         this.minWidth = (int)((float)fm.stringWidth(jl.getText()) * 4.5F);
      }

   }

   private void setYShift() {
      JLabel jl = new JLabel("Nyilvános kulcs:");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      jl.setFont(fm.getFont());
      int curentFontYHeight = jl.getFontMetrics(jl.getFont()).getHeight();
      curentFontYHeight -= 16;
      Float pontosit = 0.0F;
      if (1 <= curentFontYHeight && 5 >= curentFontYHeight) {
         pontosit = 0.25F;
      } else if (6 <= curentFontYHeight && 8 >= curentFontYHeight) {
         pontosit = 0.3F;
         this.wKorrekcioPx = 40;
      } else if (9 <= curentFontYHeight && 15 >= curentFontYHeight) {
         pontosit = 0.35F;
         this.wKorrekcioPx = 110;
      } else if (16 <= curentFontYHeight && 20 >= curentFontYHeight) {
         pontosit = 0.4F;
         this.wKorrekcioPx = 220;
      } else if (21 <= curentFontYHeight && 25 >= curentFontYHeight) {
         pontosit = 0.45F;
         this.wKorrekcioPx = 280;
      } else if (26 <= curentFontYHeight && 32 >= curentFontYHeight) {
         pontosit = 0.5F;
         this.wKorrekcioPx = 300;
      } else if (33 <= curentFontYHeight && 37 >= curentFontYHeight) {
         pontosit = 0.95F;
         this.wKorrekcioPx = 400;
      } else if (38 <= curentFontYHeight && 42 >= curentFontYHeight) {
         pontosit = 1.02F;
         this.wKorrekcioPx = 450;
      } else if (43 <= curentFontYHeight && 47 >= curentFontYHeight) {
         pontosit = 1.3F;
         this.wKorrekcioPx = 470;
      } else if (48 <= curentFontYHeight && 52 >= curentFontYHeight) {
         pontosit = 1.5F;
         this.wKorrekcioPx = 490;
      } else if (53 <= curentFontYHeight && 56 >= curentFontYHeight) {
         pontosit = 1.8F;
         this.wKorrekcioPx = 520;
      }

      this.yShift = (int)((float)curentFontYHeight * pontosit);
   }
}
