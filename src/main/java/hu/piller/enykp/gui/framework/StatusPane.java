package hu.piller.enykp.gui.framework;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.viewer.DefaultMultiFormViewer;
import hu.piller.enykp.gui.viewer.slidercombo.SizeableSliderUI;
import hu.piller.enykp.util.icon.ENYKIconSet;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class StatusPane extends JPanel {
   public static StatusPane thisinstance = null;
   private JLabel pblabel;
   public JLabel formversion;
   public JLabel currentpagename;
   public JLabel syncMessage;
   public JLabel statusname;
   public JSlider zoom_slider;
   private JButton zoom_btn;
   public JLabel l;
   private String tooltip1 = "Alkalmazás verzió";
   private String tooltip2 = "Nyomtatvány neve és verziója";
   private String tooltip3 = "Állapot";
   private String tooltip4 = "Eredeti méret";
   private String tooltip5 = "Nagyítás";

   public StatusPane() {
      thisinstance = this;
      ENYKIconSet var1 = ENYKIconSet.getInstance();
      this.setBorder(BorderFactory.createEtchedBorder());
      GridBagLayout var2 = new GridBagLayout();
      this.setLayout(var2);
      GridBagConstraints var3 = new GridBagConstraints();
      var3.fill = 2;
      this.l = new JLabel("v.3.44.0");
      this.l.setName("programversion");
      this.l.setToolTipText(this.tooltip1 + ": " + "v.3.44.0");
      var3.gridx = 0;
      var3.gridy = 0;
      var3.weightx = 0.1D;
      var2.setConstraints(this.l, var3);
      this.add(this.l);
      this.formversion = new JLabel(" ");
      this.formversion.setName("formversion");
      this.formversion.setForeground(this.formversion.getBackground());
      this.formversion.setToolTipText((String)null);
      var3.gridx = 2;
      var3.gridy = 0;
      var3.weightx = 0.1D;
      var2.setConstraints(this.formversion, var3);
      this.add(this.formversion);
      this.currentpagename = new JLabel("");
      this.currentpagename.setName("currentpagename");
      this.currentpagename.setToolTipText(this.tooltip2);
      var3.gridx = 1;
      var3.gridy = 0;
      var3.weightx = 0.5D;
      var3.ipadx = 80;
      var2.setConstraints(this.currentpagename, var3);
      this.add(this.currentpagename);
      this.syncMessage = new JLabel("");
      this.syncMessage.setName("SyncMessage");
      this.syncMessage.setToolTipText((String)null);
      this.syncMessage.addMouseListener(new MouseAdapter() {
         private Cursor cursor;

         public void mouseEntered(MouseEvent var1) {
            if (StatusPane.this.syncMessage.getText() != null && (StatusPane.this.syncMessage.getText().indexOf("Kiszolgálatlan törzsadatletöltési kérelme van!") != -1 || StatusPane.this.syncMessage.getText().indexOf("A NAV megválaszolta a törzsadat letöltési kérelmét!") != -1)) {
               this.cursor = MainFrame.thisinstance.getCursor();
               MainFrame.thisinstance.setCursor(Cursor.getPredefinedCursor(12));
            }

         }

         public void mouseExited(MouseEvent var1) {
            if (StatusPane.this.syncMessage.getText() != null && (StatusPane.this.syncMessage.getText().indexOf("Kiszolgálatlan törzsadatletöltési kérelme van!") != -1 || StatusPane.this.syncMessage.getText().indexOf("A NAV megválaszolta a törzsadat letöltési kérelmét!") != -1)) {
               MainFrame.thisinstance.setCursor(this.cursor);
            }

         }

         public void mouseClicked(MouseEvent var1) {
            StringBuffer var2;
            if (StatusPane.this.syncMessage.getText() != null && StatusPane.this.syncMessage.getText().indexOf("Kiszolgálatlan törzsadatletöltési kérelme van!") != -1) {
               var2 = new StringBuffer();
               var2.append("Az ÁNyK kiszolgálatlan törzsadatletöltési kérelmet talált.\n\n");
               var2.append("Amennyiben Ön volt a kérelem benyújtója, akkor a Szerviz\\NAV törzsadatok szinkronizálása\n");
               var2.append("menüpontban indíthatja el a kérelme kiszolgálását figyelő háttérben futó folyamatot.\n");
               GuiUtil.showMessageDialog(MainFrame.thisinstance, var2.toString(), "Tájékoztatás", 1);
            } else if (StatusPane.this.syncMessage.getText() != null && StatusPane.this.syncMessage.getText().indexOf("A NAV megválaszolta a törzsadat letöltési kérelmét!") != -1) {
               var2 = new StringBuffer();
               var2.append("A NAV megválaszolta a törzsadat letöltési kérelmét.\n\n");
               var2.append("A karbantartást a Szerviz\\NAV törzsadatok szinkronizálása menüpontra\n");
               var2.append("kattintva indíthatja el.\n");
               GuiUtil.showMessageDialog(MainFrame.thisinstance, var2.toString(), "Tájékoztatás", 1);
            }

         }
      });
      var3.gridx = 3;
      var3.gridy = 0;
      var3.weightx = 0.3D;
      var3.ipadx = 20;
      var2.setConstraints(this.syncMessage, var3);
      this.add(this.syncMessage);
      this.statusname = new JLabel("");
      this.statusname.setName("statusname");
      this.statusname.setToolTipText(this.tooltip3);
      var3.gridx = 4;
      var3.gridy = 0;
      var3.weightx = 0.2D;
      var3.ipadx = 80;
      var2.setConstraints(this.statusname, var3);
      this.add(this.statusname);
      this.zoom_btn = new JButton();
      this.zoom_btn.setIcon(var1.get("anyk_eredeti_meret"));
      Dimension var4 = GuiUtil.getButtonSizeByIcon(this.zoom_btn);
      this.zoom_btn.setPreferredSize(var4);
      this.zoom_btn.setMaximumSize(var4);
      this.zoom_btn.setSize(var4);
      this.zoom_btn.setMargin(new Insets(0, 0, 0, 0));
      this.zoom_btn.setToolTipText(this.tooltip4);
      this.zoom_btn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            StatusPane.this.zoom_slider.setValue(100);
         }
      });
      this.zoom_btn.setFocusable(false);
      this.zoom_slider = new JSlider(70, 250, 100) {
         public void updateUI() {
            this.setUI(new SizeableSliderUI(this));
         }
      };
      this.zoom_slider.setPreferredSize(new Dimension(60, 20));
      this.zoom_slider.setToolTipText(this.tooltip5 + " (100%)");
      this.zoom_slider.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent var1) {
            if (!StatusPane.this.zoom_slider.getValueIsAdjusting()) {
               StatusPane.this.zoom_slider.setToolTipText(StatusPane.this.tooltip5 + " (" + StatusPane.this.zoom_slider.getValue() + "%)");
               DefaultMultiFormViewer var2 = MainFrame.thisinstance.mp.getDMFV();
               if (var2 != null) {
                  var2.zoom(StatusPane.this.zoom_slider.getValue());
               }

            }
         }
      });
      this.zoom_slider.setFocusable(false);
      JPanel var5 = new JPanel();
      var5.setLayout(new BorderLayout());
      var5.add(this.zoom_btn, "West");
      var5.add(this.zoom_slider);
      var3.gridx = 5;
      var3.gridy = 0;
      var3.weightx = 0.01D;
      var2.setConstraints(var5, var3);
      this.add(var5);
   }

   public void resetZoom() {
      this.zoom_btn.doClick();
   }
}
