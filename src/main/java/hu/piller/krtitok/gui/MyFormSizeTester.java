package hu.piller.krtitok.gui;

import hu.piller.krtitok.KriptoApp;
import hu.piller.krtitok.tools.log.Logger;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PropertyResourceBundle;
import java.util.Map.Entry;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FontUIResource;

public class MyFormSizeTester {
   JFrame frame;
   JSpinner spinner;
   JLabel lblDemoText;
   JDialog parent = new JDialog();
   JPanel midPanel;
   JDialog testDialog;
   public static int curentFontSize = 12;

   public static int getCurentFontSize() {
      return curentFontSize;
   }

   private static void setCurentFont(int iFontSize) {
      curentFontSize = iFontSize;
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

   public static void main(String[] args) {
      MyFormSizeTester fto = new MyFormSizeTester();
      fto.initComponents();
   }

   public JLabel getLabel() {
      return this.lblDemoText;
   }

   private void initComponents() {
      this.frame = new JFrame("JFrame Example");
      this.frame.setMinimumSize(new Dimension(200, 100));
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout(0, 0));
      JPanel panelH = new JPanel();
      FlowLayout flowLayout = (FlowLayout)panelH.getLayout();
      flowLayout.setAlignment(0);
      panel.add(panelH, "North");
      JLabel lblFontSize = new JLabel("Font size: ");
      panelH.add(lblFontSize);
      this.spinner = new JSpinner();
      panelH.add(this.spinner);
      this.spinner.setModel(new SpinnerNumberModel(12, 6, 64, 1));
      this.frame.getContentPane().add(panel);
      JPanel panelM = new JPanel();
      this.midPanel = panelM;
      FlowLayout flowLayout_1 = (FlowLayout)panelM.getLayout();
      flowLayout_1.setAlignment(0);
      panel.add(panelM, "Center");
      JLabel lblDemoText = new JLabel("Demo text");
      this.lblDemoText = lblDemoText;
      panelM.add(lblDemoText);
      this.frame.pack();
      this.frame.setLocationRelativeTo((Component)null);
      this.frame.setLocation(280, 860);
      this.frame.setDefaultCloseOperation(3);
      this.frame.setVisible(true);
      this.spinner.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent arg0) {
            Number value = (Number)MyFormSizeTester.this.spinner.getValue();
            String txt = "Current font size: " + value;
            MyFormSizeTester.this.getLabel().setText(txt);
            MyFormSizeTester.setCurentFont(value.intValue());
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  Point pos = MyFormSizeTester.this.testDialog.getLocation();
                  MyFormSizeTester.this.testDialog.dispose();
                  MyFormSizeTester.this.testDialog = null;
                  MyFormSizeTester.this.testDialog = MyFormSizeTester.this.getTestDialog();
                  MyFormSizeTester.this.testDialog.setLocation(pos);
                  MyFormSizeTester.this.testDialog.setVisible(true);
                  MyFormSizeTester.this.showFontParameters2(MyFormSizeTester.this.testDialog);
                  MyFormSizeTester.this.frame.requestFocus();
               }
            });
         }
      });
      this.midPanel.addMouseWheelListener(new MouseWheelListener() {
         public void mouseWheelMoved(MouseWheelEvent e) {
            int notches = e.getWheelRotation();
            SpinnerModel mdl = MyFormSizeTester.this.spinner.getModel();
            Object value;
            if (notches < 0) {
               value = mdl.getNextValue();
            } else {
               value = mdl.getPreviousValue();
            }

            try {
               mdl.setValue(value);
            } catch (Exception var6) {
            }

         }
      });
      this.midPanel.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent arg0) {
            MyFormSizeTester.this.showFontParameters2(MyFormSizeTester.this.testDialog);
         }
      });
      setCurentFont(12);
      this.testDialog = this.getTestDialog();
      this.testDialog.setLocation(100, 100);
      this.testDialog.setVisible(true);
      this.showFontParameters();
   }

   public <T extends JDialog> T getTestDialog() {
      JFrame jframe = this.initMyFKriptodsk1();
      FKititkositas parent2 = new FKititkositas(jframe, false);
      return (T) new FKulcsTarak(parent2, 11, true, false, false);
   }

   private JFrame initMyFKriptodsk0() {
      int myFontSize = 26;
      HashMap<String, String> kepek = new HashMap();
      kepek.put("valami", Integer.toString(myFontSize));

      try {
         Field field = KriptoApp.class.getDeclaredField("kepek");
         field.setAccessible(true);
         field.set((Object)null, kepek);
         FileInputStream res = new FileInputStream("resources/msgs_hu.properties");
         PropertyResourceBundle resources = new PropertyResourceBundle(res);
         field = KriptoApp.class.getDeclaredField("resources");
         field.setAccessible(true);
         field.set((Object)null, resources);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      FKriptodsk fKriptodsk = new FKriptodsk();
      return fKriptodsk;
   }

   private JFrame initMyFKriptodsk1() {
      int myFontSize = getCurentFontSize();
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

      return fKriptodsk;
   }

   public void showFontParameters() {
      JLabel jl = new JLabel("Állomanyok helye:");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      jl.setFont(fm.getFont());
      int curentFontYHeight = jl.getFontMetrics(jl.getFont()).getHeight();
   }

   public void showFontParameters2(JDialog t) {
      JLabel jl = new JLabel("Állomanyok helye:");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      jl.setFont(fm.getFont());
      int curentFontYHeight = jl.getFontMetrics(jl.getFont()).getHeight();
      System.out.printf("{ %3d,%5d,%5d },\t//f.size %5d%n", curentFontYHeight, t.getSize().width, t.getSize().height, getCurentFontSize());
   }
}
