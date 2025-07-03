package hu.piller.krtitok.gui;

import hu.piller.kripto.keys.StoreFilter;
import hu.piller.kripto.keys.StoreManager;
import hu.piller.kripto.keys.StoreWrapper;
import hu.piller.tools.TableSorter;
import me.necrocore.abevjava.NecroFile;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

public class FKulcsTarak extends JDialog {
   private int minWidth;
   private int minHeight;
   private int yShift;
   private int yKorrekcioPx;
   private KeyWrapperHolder parent;
   private StoreFilter sf;
   private HashSet selectedKeys;
   private int maxKeyCount;
   private boolean showPrivate;
   private boolean showPublic;
   private JPanel mainPanel;
   private JScrollPane scrollPane1;
   private JTable kulcsTarakTable;
   private JPanel panel2;
   private JCheckBox cerCheckBox;
   private JCheckBox pgpCheckBox;
   private JCheckBox jksCheckBox;
   private JCheckBox p12CheckBox;
   private JLabel helyLabel;
   private JTextField helyTextField;
   private JButton helyFileChooser;
   private JButton mégsemButton;
   private JButton rendbenButton;

   public FKulcsTarak(KeyWrapperHolder parent, boolean modal) {
      this(parent, new int[]{400, 200, 300, 120, 140, 110, 130}, System.getProperty("user.home"), 5, true, true, modal);
   }

   public FKulcsTarak(KeyWrapperHolder parent, int maxKeyCount, boolean showPrivate, boolean showPublic, boolean modal) {
      this(parent, new int[]{400, 200, 300, 120, 140, 110, 130}, System.getProperty("user.home"), maxKeyCount, showPrivate, showPublic, modal);
   }

   public FKulcsTarak(KeyWrapperHolder parent, int[] types, String defaultDir, int maxKeyCount, boolean showPrivate, boolean showPublic, boolean modal) {
      super((JDialog)parent, modal);
      this.minWidth = 525;
      this.minHeight = 350;
      this.yShift = 0;
      this.yKorrekcioPx = 0;
      this.selectedKeys = new HashSet();
      this.parent = parent;
      this.maxKeyCount = maxKeyCount;
      this.showPrivate = showPrivate;
      this.showPublic = showPublic;
      this.setMinWidth();
      this.setYShift();
      this.initComponents();
      this.setXButton();
      this.sf = new StoreFilter(types, false);
      this.helyTextField.setText(defaultDir);
      this.jksCheckBox.setSelected(false);
      this.p12CheckBox.setSelected(false);
      this.cerCheckBox.setSelected(false);
      this.pgpCheckBox.setSelected(false);

      for(int i = 0; i < types.length; ++i) {
         switch(types[i]) {
         case 110:
            this.pgpCheckBox.setSelected(true);
            break;
         case 120:
            this.pgpCheckBox.setSelected(true);
            break;
         case 130:
            this.pgpCheckBox.setSelected(true);
            break;
         case 140:
            this.pgpCheckBox.setSelected(true);
            break;
         case 200:
            this.jksCheckBox.setSelected(true);
            break;
         case 300:
            this.p12CheckBox.setSelected(true);
            break;
         case 400:
            this.cerCheckBox.setSelected(true);
         }
      }

      this.listStores();
   }

   private void listStores() {
      try {
         File[] stores = (new NecroFile(this.helyTextField.getText())).listFiles(this.sf);
         Vector dataVector = new Vector();

         for(int i = 0; i < stores.length; ++i) {
            if (!stores[i].isDirectory()) {
               StoreWrapper sw = StoreManager.loadStore((InputStream)(new FileInputStream(stores[i].getAbsolutePath())), (char[])null);
               if (sw != null) {
                  sw.setLocation(stores[i].getAbsolutePath());
                  Object[] row = new Object[]{new Integer(sw.getType()), new Integer(sw.getType()), stores[i].getName(), new Long(stores[i].length())};
                  dataVector.add(row);
               }
            }
         }

         Object[][] data = new Object[dataVector.size()][];

         for(int k = 0; k < dataVector.size(); ++k) {
            data[k] = (Object[])dataVector.elementAt(k);
         }

         StoresTableModel stm = new StoresTableModel(data);
         TableSorter ts = new TableSorter(stm);
         ts.setTableHeader(this.kulcsTarakTable.getTableHeader());
         this.kulcsTarakTable.setModel(ts);
         this.kulcsTarakTable.getColumnModel().getColumn(0).setCellRenderer(new StoreImageRenderer());
         this.kulcsTarakTable.getColumnModel().getColumn(1).setCellRenderer(new StoreTypeNameRenderer());
         int minWidthFejlecKep = this.getMinWidth(ts.getColumnName(0).toString()) + 40;
         this.kulcsTarakTable.getColumnModel().getColumn(0).setPreferredWidth(minWidthFejlecKep);
         this.kulcsTarakTable.getColumnModel().getColumn(0).setMaxWidth(minWidthFejlecKep);
         this.kulcsTarakTable.getColumnModel().getColumn(1).setPreferredWidth(100);
         this.kulcsTarakTable.getColumnModel().getColumn(2).setPreferredWidth(120);
         this.kulcsTarakTable.getColumnModel().getColumn(3).setPreferredWidth(80);
         if (this.kulcsTarakTable.getRowCount() > 0) {
            this.kulcsTarakTable.getSelectionModel().setSelectionInterval(0, 0);
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   private void button1ActionPerformed(ActionEvent e) {
      JFileChooser fc = new JFileChooser(this.helyTextField.getText());
      fc.setFileSelectionMode(1);
      fc.showOpenDialog(this);
      if (fc.getSelectedFile() != null) {
         this.helyTextField.setText(fc.getSelectedFile().getAbsolutePath());
         this.listStores();
      }

   }

   private void cerCheckBoxActionPerformed(ActionEvent e) {
      if (this.cerCheckBox.isSelected()) {
         this.sf.addFilterType(400);
      } else {
         this.sf.removeFilterType(400);
      }

      this.listStores();
   }

   private void pgpCheckBoxActionPerformed(ActionEvent e) {
      if (this.pgpCheckBox.isSelected()) {
         this.sf.addFilterType(120);
         this.sf.addFilterType(140);
         this.sf.addFilterType(110);
         this.sf.addFilterType(130);
      } else {
         this.sf.removeFilterType(120);
         this.sf.removeFilterType(140);
         this.sf.removeFilterType(110);
         this.sf.removeFilterType(130);
      }

      this.listStores();
   }

   private void jksCheckBoxActionPerformed(ActionEvent e) {
      if (this.jksCheckBox.isSelected()) {
         this.sf.addFilterType(200);
      } else {
         this.sf.removeFilterType(200);
      }

      this.listStores();
   }

   private void p12CheckBoxActionPerformed(ActionEvent e) {
      if (this.p12CheckBox.isSelected()) {
         this.sf.addFilterType(300);
      } else {
         this.sf.removeFilterType(300);
      }

      this.listStores();
   }

   private void mégsemButtonActionPerformed(ActionEvent e) {
      this.setVisible(false);
      this.dispose();
   }

   private void rendbenButtonActionPerformed(ActionEvent e) {
      Iterator it = this.selectedKeys.iterator();

      while(it.hasNext()) {
         System.out.println("FKulcsTarak.kulcs:\n" + it.next());
      }

      if (this.selectedKeys.size() == 0) {
         this.showKeys();
      }

      this.setVisible(false);
      this.dispose();
   }

   private void table1MouseClicked(MouseEvent e) {
      if (e.getClickCount() >= 2) {
         this.showKeys();
      }

   }

   private void showKeys() {
      try {
         String dir = this.helyTextField.getText();
         String fileName = (String)this.kulcsTarakTable.getModel().getValueAt(this.kulcsTarakTable.getSelectedRow(), 2);
         String fullPath = dir.lastIndexOf(File.separator) == dir.length() - 1 ? dir + fileName : dir + File.separator + fileName;
         int type = StoreManager.getStoreType((InputStream)(new FileInputStream(fullPath)));
         StoreWrapper sw;
         if (type != 200 && type != 300) {
            sw = StoreManager.loadStore((String)fullPath, (char[])null);
         } else {
            sw = StoreManager.loadStore(fullPath, this.askForPwd());
         }

         if (sw != null) {
            FKulcsok fk = new FKulcsok(this.parent, sw, this.parent.getCount(), this.showPrivate, this.showPublic, true);
            fk.setVisible(true);
         } else {
            JOptionPane.showMessageDialog(this, "Sikertelen a kulcstár inicializálás!", "Üzenet", 2);
         }
      } catch (NoSuchAlgorithmException var7) {
         var7.printStackTrace();
      } catch (FileNotFoundException var8) {
         var8.printStackTrace();
      }

   }

   private char[] askForPwd() {
      JPasswordField jpf = new JPasswordField(10);
      JPanel panel = new JPanel(new FlowLayout());
      panel.add(new JLabel("Kulcstárhoz tartozó jelszó: "));
      panel.add(jpf);
      (new JOptionPane(panel)).createDialog(this, "Jelszó").show();
      return jpf.getPassword();
   }

   private void helyTextFieldMouseClicked(MouseEvent e) {
      this.button1ActionPerformed(new ActionEvent(this.helyTextField, 1, "choose"));
   }

   private void initComponents() {
      this.mainPanel = new JPanel();
      this.scrollPane1 = new JScrollPane();
      this.kulcsTarakTable = new JTable();
      this.panel2 = new JPanel();
      this.cerCheckBox = new JCheckBox();
      this.pgpCheckBox = new JCheckBox();
      this.jksCheckBox = new JCheckBox();
      this.p12CheckBox = new JCheckBox();
      this.helyLabel = new JLabel();
      this.helyTextField = new JTextField();
      this.helyFileChooser = new JButton();
      this.mégsemButton = new JButton();
      this.rendbenButton = new JButton();
      this.setTitle("Kulcstárak");
      Container contentPane = this.getContentPane();
      contentPane.setLayout((LayoutManager)null);
      JLabel jl = new JLabel("X509 tanúsítványok");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      jl.setFont(fm.getFont());
      int curentFontYHeight = jl.getFontMetrics(jl.getFont()).getHeight();
      int yKorrekcio = curentFontYHeight * 16;
      this.yKorrekcioPx = this.minHeight;
      if (this.minHeight < yKorrekcio) {
         this.yKorrekcioPx = yKorrekcio;
      }

      this.setMinimumSize(new Dimension(this.minWidth + 10, this.yKorrekcioPx));
      this.mainPanel.setLayout((LayoutManager)null);
      this.kulcsTarakTable.setModel(new DefaultTableModel());
      this.kulcsTarakTable.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            FKulcsTarak.this.table1MouseClicked(e);
         }
      });
      this.scrollPane1.setViewportView(this.kulcsTarakTable);
      this.mainPanel.add(this.scrollPane1);
      this.scrollPane1.setBounds(10, 130 + 5 * this.yShift, this.minWidth - 20, 135 + 4 * this.yShift);
      this.panel2.setBorder(new EtchedBorder());
      this.panel2.setLayout((LayoutManager)null);
      this.cerCheckBox.setText("X509 tanúsítványok");
      this.cerCheckBox.setSelected(true);
      this.cerCheckBox.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKulcsTarak.this.cerCheckBoxActionPerformed(e);
         }
      });
      this.panel2.add(this.cerCheckBox);
      this.cerCheckBox.setIcon(FKriptodsk.getCheckboxIkonUres());
      this.cerCheckBox.setSelectedIcon(FKriptodsk.getCheckboxIkonTeli());
      this.cerCheckBox.setBounds(new Rectangle(new Point(25, 65 + 4 * this.yShift), this.cerCheckBox.getPreferredSize()));
      this.pgpCheckBox.setText("PGP kulcsok");
      this.pgpCheckBox.setSelected(true);
      this.pgpCheckBox.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKulcsTarak.this.pgpCheckBoxActionPerformed(e);
         }
      });
      this.panel2.add(this.pgpCheckBox);
      this.pgpCheckBox.setIcon(FKriptodsk.getCheckboxIkonUres());
      this.pgpCheckBox.setSelectedIcon(FKriptodsk.getCheckboxIkonTeli());
      this.pgpCheckBox.setBounds(new Rectangle(new Point(25, 35 + 3 * this.yShift / 2), this.pgpCheckBox.getPreferredSize()));
      this.jksCheckBox.setText("JKS (Java) kulcstárak ");
      this.jksCheckBox.setSelected(true);
      this.jksCheckBox.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKulcsTarak.this.jksCheckBoxActionPerformed(e);
         }
      });
      this.panel2.add(this.jksCheckBox);
      this.jksCheckBox.setIcon(FKriptodsk.getCheckboxIkonUres());
      this.jksCheckBox.setSelectedIcon(FKriptodsk.getCheckboxIkonTeli());
      int xJKS = this.minWidth - 2 * this.cerCheckBox.getWidth();
      this.jksCheckBox.setBounds(new Rectangle(new Point(xJKS, 65 + 4 * this.yShift), this.jksCheckBox.getPreferredSize()));
      this.p12CheckBox.setText("Pkcs12 állományok");
      this.p12CheckBox.setSelected(true);
      this.p12CheckBox.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKulcsTarak.this.p12CheckBoxActionPerformed(e);
         }
      });
      this.panel2.add(this.p12CheckBox);
      this.p12CheckBox.setIcon(FKriptodsk.getCheckboxIkonUres());
      this.p12CheckBox.setSelectedIcon(FKriptodsk.getCheckboxIkonTeli());
      int i = this.minWidth - 2 * this.cerCheckBox.getWidth();
      this.p12CheckBox.setBounds(new Rectangle(new Point(i, 35 + 3 * this.yShift / 2), this.p12CheckBox.getPreferredSize()));
      this.helyLabel.setText("Hely:");
      this.panel2.add(this.helyLabel);
      this.helyLabel.setBounds(new Rectangle(new Point(20, 10), this.pgpCheckBox.getPreferredSize()));
      this.helyTextField.setEditable(false);
      this.helyTextField.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKulcsTarak.this.button1ActionPerformed(e);
         }
      });
      this.helyTextField.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            FKulcsTarak.this.helyTextFieldMouseClicked(e);
         }
      });
      this.panel2.add(this.helyTextField);
      int xHely = this.getMinWidth("Hely:") + 5;
      this.helyTextField.setBounds(this.helyLabel.getX() + xHely, 10, this.minWidth - this.cerCheckBox.getWidth(), 20 + this.yShift);
      this.helyFileChooser.setText("...");
      this.helyFileChooser.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKulcsTarak.this.button1ActionPerformed(e);
         }
      });
      this.panel2.add(this.helyFileChooser);
      this.helyFileChooser.setBounds(this.minWidth - this.pgpCheckBox.getWidth() + 10, 10, 47, 20 + this.yShift);
      this.kulcsTarakTable.setRowHeight(this.helyTextField.getHeight());
      Dimension preferredSize = new Dimension();

      for(int ix = 0; ix < this.panel2.getComponentCount(); ++ix) {
         Rectangle bounds = this.panel2.getComponent(ix).getBounds();
         preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
         preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
      }

      Insets insets = this.panel2.getInsets();
      preferredSize.width += insets.right;
      preferredSize.height += insets.bottom;
      this.panel2.setPreferredSize(preferredSize);
      this.mainPanel.add(this.panel2);
      this.panel2.setBounds(10, 15, this.minWidth - 20, 100 + 5 * this.yShift);
      this.mégsemButton.setText("Mégsem");
      this.mégsemButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKulcsTarak.this.mégsemButtonActionPerformed(e);
         }
      });
      this.mainPanel.add(this.mégsemButton);
      this.mégsemButton.setBounds(new Rectangle(new Point(435, 275), this.mégsemButton.getPreferredSize()));
      this.rendbenButton.setText("Rendben");
      this.rendbenButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKulcsTarak.this.rendbenButtonActionPerformed(e);
         }
      });
      this.mainPanel.add(this.rendbenButton);
      this.rendbenButton.setBounds(new Rectangle(new Point(345, 275), this.rendbenButton.getPreferredSize()));
      contentPane.add(this.mainPanel);
      this.mainPanel.setBounds(0, 0, this.minWidth, this.yKorrekcioPx - 10);
      Dimension preferredSize1 = new Dimension();

      for(i = 0; i < contentPane.getComponentCount(); ++i) {
         Rectangle bounds = contentPane.getComponent(i).getBounds();
         preferredSize1.width = Math.max(bounds.x + bounds.width, preferredSize1.width);
         preferredSize1.height = Math.max(bounds.y + bounds.height, preferredSize1.height);
      }

      Insets insets1 = contentPane.getInsets();
      preferredSize1.width += insets1.right;
      preferredSize1.height += insets1.bottom;
      ((JComponent)contentPane).setPreferredSize(preferredSize1);
      this.pack();
      this.setLocationRelativeTo(this.getOwner());
      this.repaint();
      this.revalidate();
   }

   private void setMinWidth() {
      JLabel jl = new JLabel("X509 tanúsítványok JKS (Java) kulcstárak");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      JLabel jl2 = new JLabel("betumeret_betustilus_fuggo_terkoz");
      FontMetrics fm2 = jl2.getFontMetrics(jl2.getFont());
      this.minWidth = fm.stringWidth(jl.getText());
      this.minWidth = this.minWidth + fm2.stringWidth(jl2.getText()) + 80;
   }

   private void setYShift() {
      JLabel jl = new JLabel("X509 tanúsítványok");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      jl.setFont(fm.getFont());
      int curentFontYHeight = jl.getFontMetrics(jl.getFont()).getHeight();
      curentFontYHeight -= 16;
      Float pontosit = 0.0F;
      if (1 <= curentFontYHeight && 5 >= curentFontYHeight) {
         pontosit = 0.6F;
      } else if (6 <= curentFontYHeight && 8 >= curentFontYHeight) {
         pontosit = 0.65F;
      } else if (9 <= curentFontYHeight && 15 >= curentFontYHeight) {
         pontosit = 0.75F;
      } else if (16 <= curentFontYHeight && 20 >= curentFontYHeight) {
         pontosit = 0.83F;
      } else if (21 <= curentFontYHeight && 25 >= curentFontYHeight) {
         pontosit = 0.85F;
      } else if (26 <= curentFontYHeight && 32 >= curentFontYHeight) {
         pontosit = 0.9F;
      } else if (33 <= curentFontYHeight && 37 >= curentFontYHeight) {
         pontosit = 0.95F;
      } else if (38 <= curentFontYHeight && 42 >= curentFontYHeight) {
         pontosit = 1.02F;
      } else if (43 <= curentFontYHeight && 47 >= curentFontYHeight) {
         pontosit = 1.3F;
      } else if (48 <= curentFontYHeight && 52 >= curentFontYHeight) {
         pontosit = 1.5F;
      } else if (53 <= curentFontYHeight && 56 >= curentFontYHeight) {
         pontosit = 1.8F;
      }

      this.yShift = (int)((float)curentFontYHeight * pontosit);
   }

   private int getMinWidth(String aktSzoveg) {
      JLabel jl = new JLabel(aktSzoveg);
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
       return fm.stringWidth(jl.getText());
   }

   private void setXButton() {
      int windowWidth = this.getWidth();
      int xRendben = windowWidth - this.mégsemButton.getWidth() - 20 - this.rendbenButton.getWidth();
      int xMegseem = windowWidth - this.mégsemButton.getWidth() - 20;
      this.rendbenButton.setBounds(xRendben, this.yKorrekcioPx - this.rendbenButton.getHeight() - 20, this.rendbenButton.getWidth(), this.rendbenButton.getHeight());
      this.mégsemButton.setBounds(xMegseem, this.yKorrekcioPx - this.rendbenButton.getHeight() - 20, this.mégsemButton.getWidth(), this.mégsemButton.getHeight());
   }
}
