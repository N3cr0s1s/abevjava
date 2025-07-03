package hu.piller.krtitok.gui;

import hu.piller.krtitok.KriptoApp;
import hu.piller.krtitok.tools.log.Logger;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PropertyResourceBundle;
import java.util.Map.Entry;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.FontUIResource;

public class FKriptodsk extends JFrame {
   public FTitKulcsGen ftkg;
   public FTitkositas ftk;
   public FKititkositas fkitk;
   public static int curentFontSize = 12;
   public static JCheckBox curentCheckBox = null;
   private static ImageIcon checkboxIkonUres80;
   private static ImageIcon checkboxIkonTeli80;
   private static ImageIcon checkboxIkonUres;
   private static ImageIcon checkboxIkonTeli;
   private JPanel panel1;
   private JMenuBar menuBar1;
   private JMenu menu2;
   private JMenuItem menuItem1;
   private JMenu menu1;
   private JMenuItem mITitKulcsGen;
   private JMenuItem mITitkositas;
   private JMenuItem mIKititkositas;
   private JMenu menu5;
   private JMenuItem menuItem3;
   private JMenu menu4;
   private JMenuItem menuItem2;
   private JScrollPane scrollPane1;
   private JTextArea textArea1;

   public static ImageIcon getCheckboxIkonUres() {
      return checkboxIkonUres;
   }

   public static ImageIcon getCheckboxIkonTeli() {
      return checkboxIkonTeli;
   }

   public static URL getResourceUrl(String name) {
      URL url = null;
      Class cx = FKriptodsk.class;
      if (url == null) {
         url = cx.getResource("/resources/images/" + name);
      }

      if (url == null) {
         url = cx.getResource("/images/" + name);
      }

      if (url == null) {
         url = cx.getResource("/resources/icons/" + name);
      }

      if (url == null) {
         url = cx.getResource("/icons/" + name);
      }

      return url;
   }

   public FKriptodsk() {
      String sCurentFontSize = KriptoApp.getAnykFontSize("anykFontSize");
      if (sCurentFontSize != null && !"".equals(sCurentFontSize)) {
         curentFontSize = Integer.parseInt(sCurentFontSize);
      } else {
         curentFontSize = 12;
      }

      setCurentFont(curentFontSize);
      checkboxIkonUres80 = new ImageIcon(getResourceUrl("checkBox_80.png"));
      checkboxIkonTeli80 = new ImageIcon(getResourceUrl("checkBox_80checked.png"));
      JLabel jl = new JLabel("X");
      int fontH = jl.getFontMetrics(jl.getFont()).getHeight();
      checkboxIkonUres = new ImageIcon(checkboxIkonUres80.getImage().getScaledInstance(fontH, fontH, 4));
      checkboxIkonTeli = new ImageIcon(checkboxIkonTeli80.getImage().getScaledInstance(fontH, fontH, 4));
      this.initComponents();
      this.setDefaultCloseOperation(2);
   }

   public static void main(String[] args) {
      int myFontSize = 12;
      HashMap<String, String> kepek = new HashMap();
      kepek.put("anykFontSize", Integer.toString(myFontSize));

      try {
         Field field = KriptoApp.class.getDeclaredField("kepek");
         field.setAccessible(true);
         field.set((Object)null, kepek);
         FileInputStream res = new FileInputStream("resources/msgs_hu.properties");
         PropertyResourceBundle resources = new PropertyResourceBundle(res);
         field = KriptoApp.class.getDeclaredField("resources");
         field.setAccessible(true);
         field.set((Object)null, resources);
      } catch (Exception var9) {
         var9.printStackTrace();
      }

      FKriptodsk fKriptodsk = new FKriptodsk();

      try {
         FileInputStream res = new FileInputStream("resources/msgs_hu.properties");
         PropertyResourceBundle resources = new PropertyResourceBundle(res);
         Field field = KriptoApp.class.getDeclaredField("resources");
         field.setAccessible(true);
         field.set((Object)null, resources);
         field = KriptoApp.class.getDeclaredField("logger");
         field.setAccessible(true);
         Logger logger = (Logger)field.get((Object)null);
         logger.setTextArea(fKriptodsk.getLogArea());
         logger.setResource(resources);
         logger.info("M4008", new Object[]{"dunno"});
         logger.info("-- started with fixed main --");
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      fKriptodsk.setVisible(true);
   }

   private void mITitKulcsGenActionPerformed(ActionEvent e) {
      if (this.ftkg == null) {
         this.ftkg = new FTitKulcsGen(this, true);
      }

      this.ftkg.setVisible(true);
   }

   private void mITitkositasActionPerformed(ActionEvent e) {
      this.ftk = new FTitkositas(this, true);
      this.ftk.setVisible(true);
   }

   private void mIKititkositasActionPerformed(ActionEvent e) {
      this.fkitk = new FKititkositas(this, true);
      this.fkitk.setVisible(true);
   }

   public JTextArea getLogArea() {
      return this.textArea1;
   }

   private void menuItem1ActionPerformed(ActionEvent e) {
      this.dispose();
   }

   private void menuItem2ActionPerformed(ActionEvent e) {
      JOptionPane.showMessageDialog(this, "<html>Kriptográfiai alkalmazás, <br>ügyfélkapun keresztül történő <br>titkosított kommunikáció biztosításához. <br><br>http://www.mo.hu/ugyfelkapu<br><br>Verzió: 1.3.1</div></html>", "Névjegy", 1);
   }

   private void menuItem3ActionPerformed(ActionEvent e) {
      FBeallitasok fbeallitas = new FBeallitasok(this, true);
      fbeallitas.setVisible(true);
   }

   private void segitseg(ActionEvent e) {
      new Segitseg();
   }

   private void initComponents() {
      this.panel1 = new JPanel();
      this.menuBar1 = new JMenuBar();
      this.menu2 = new JMenu();
      this.menuItem1 = new JMenuItem();
      this.menu1 = new JMenu();
      this.mITitKulcsGen = new JMenuItem();
      this.mITitkositas = new JMenuItem();
      this.mIKititkositas = new JMenuItem();
      this.menu5 = new JMenu();
      this.menuItem3 = new JMenuItem();
      this.menu4 = new JMenu();
      this.menuItem2 = new JMenuItem();
      JMenuItem menuItem4 = new JMenuItem();
      this.scrollPane1 = new JScrollPane();
      this.textArea1 = new JTextArea();
      this.setTitle("Krtitok");
      this.setMaximizedBounds(new Rectangle(0, 0, 800, 600));
      this.setDefaultCloseOperation(3);
      Container contentPane = this.getContentPane();
      contentPane.setLayout(new BorderLayout());
      if (12 >= curentFontSize) {
         this.panel1.setPreferredSize(new Dimension(640, 400));
      } else if (curentFontSize >= 13 && curentFontSize <= 32) {
         this.panel1.setPreferredSize(new Dimension(960, 540));
      } else {
         this.panel1.setPreferredSize(new Dimension(1920, 1080));
      }

      this.panel1.setLayout(new BorderLayout());
      this.menu2.setText("Fájl");
      this.menuItem1.setText("Kilépés");
      this.menuItem1.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKriptodsk.this.menuItem1ActionPerformed(e);
         }
      });
      this.menu2.add(this.menuItem1);
      this.menuBar1.add(this.menu2);
      this.menu1.setText("Műveletek");
      this.mITitKulcsGen.setText("Titkosító kulcspár generálás");
      this.mITitKulcsGen.setIcon((Icon)null);
      this.mITitKulcsGen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKriptodsk.this.mITitKulcsGenActionPerformed(e);
         }
      });
      this.menu1.add(this.mITitKulcsGen);
      this.mITitkositas.setText("Titkosítás");
      this.mITitkositas.setIcon((Icon)null);
      this.mITitkositas.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKriptodsk.this.mITitkositasActionPerformed(e);
         }
      });
      this.menu1.add(this.mITitkositas);
      this.mIKititkositas.setText("Kititkosítás");
      this.mIKititkositas.setIcon((Icon)null);
      this.mIKititkositas.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKriptodsk.this.mIKititkositasActionPerformed(e);
         }
      });
      this.menu1.add(this.mIKititkositas);
      this.menuBar1.add(this.menu1);
      this.menu5.setText("Eszközök");
      this.menuItem3.setText("Beállítások");
      this.menuItem3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKriptodsk.this.menuItem3ActionPerformed(e);
         }
      });
      this.menu5.add(this.menuItem3);
      this.menuBar1.add(this.menu5);
      this.menu4.setText("Súgó");
      this.menuItem2.setText("Névjegy");
      this.menuItem2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKriptodsk.this.menuItem2ActionPerformed(e);
         }
      });
      this.menu4.add(this.menuItem2);
      menuItem4.setText("Segítség");
      menuItem4.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKriptodsk.this.segitseg(e);
         }
      });
      this.menuBar1.add(this.menu4);
      this.panel1.add(this.menuBar1, "North");
      this.textArea1.setRows(20);
      this.textArea1.setEditable(false);
      this.textArea1.setFocusable(false);
      this.textArea1.setBorder(new BevelBorder(1));
      int cmdFontSize = 11;
      if (curentFontSize > cmdFontSize) {
         cmdFontSize = curentFontSize;
         System.out.println("anykFontSize beállítva:" + cmdFontSize);
      }

      this.textArea1.setFont(new Font("Courier New", 0, cmdFontSize));
      this.scrollPane1.setViewportView(this.textArea1);
      this.panel1.add(this.scrollPane1, "Center");
      contentPane.add(this.panel1, "Center");
      this.pack();
      this.setLocationRelativeTo((Component)null);
   }

   private static void setCurentFont(int iFontSize) {
      Iterator var2 = UIManager.getDefaults().entrySet().iterator();

      while(true) {
         while(var2.hasNext()) {
            Entry<Object, Object> entry = (Entry)var2.next();
            Object key = entry.getKey();
            Object value = UIManager.get(key);
            if (value != null && value instanceof FontUIResource) {
               FontUIResource fr = (FontUIResource)value;
               FontUIResource f = new FontUIResource(fr.getFamily(), fr.getStyle(), iFontSize);
               UIManager.put(key, f);
            } else if (value != null && value instanceof Font) {
               Font font = (Font)value;
               Font f = new Font(font.getFamily(), font.getStyle(), iFontSize);
               UIManager.put(key, f);
            }
         }

         return;
      }
   }
}
