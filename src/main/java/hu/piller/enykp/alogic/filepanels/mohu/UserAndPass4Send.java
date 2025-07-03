package hu.piller.enykp.alogic.filepanels.mohu;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.niszws.util.KauAuthHelper;
import hu.piller.enykp.niszws.util.KauSessionTimeoutHandler;
import hu.piller.enykp.util.base.PropertyList;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class UserAndPass4Send extends LoginDialog implements ActionListener, WindowListener, KeyListener {
   private JButton authOkButton = new JButton("Rendben");
   private JButton authCancelButton = new JButton("Mégsem");
   private int state;
   private boolean listenerOff = true;
   private int avdh;
   private static int dialogWidth;
   int yStep = 35;
   KauAuthHelper kauAuthHelper;

   public UserAndPass4Send(int var1) {
      super(MainFrame.thisinstance, "Azonosítás", true);
      this.avdh = var1;
      this.yStep = (int)(1.5D * (double)GuiUtil.getCommonItemHeight());
      this.kauAuthHelper = KauAuthHelper.getInstance();
      dialogWidth = GuiUtil.getW("A listában minden nyomtatvány esetében ugyanazzal az adattal történik az azonosítás.") + 10;
      this.init();
   }

   private void init() {
      int var1 = GuiUtil.getCommonItemHeight() + 15;
      this.state = -1;
      this.setDefaultCloseOperation(0);
      Border var2 = BorderFactory.createEtchedBorder();
      TitledBorder var3 = new TitledBorder(var2, "Átirányítás");
      var3.setTitleJustification(1);
      JPanel var4 = new JPanel((LayoutManager)null);
      var4.setBorder(var3);
      JPanel var5 = new JPanel();
      GuiUtil.setDynamicBound(this.authOkButton, this.authOkButton.getText(), 0, 0);
      GuiUtil.setDynamicBound(this.authCancelButton, this.authCancelButton.getText(), GuiUtil.getPositionFromPrevComponent(this.authOkButton), 0);
      this.authOkButton.addActionListener(this);
      this.authCancelButton.addActionListener(this);
      var5.add(this.authOkButton);
      var5.add(this.authCancelButton);
      JLabel var6 = new JLabel("<html>A program az azonosítást a Központi Azonosítási Ügynökkel (KAÜ) végzi.<br>Amennyiben első alkalommal jelentkezik be, a program átirányítja önt a bejelentkezési felületre.</html>");
      GuiUtil.setDynamicBound(var6, var6.getText(), 30, var1);
      var4.add(var6);
      var1 = (int)((double)var1 + var6.getBounds().getHeight() + 10.0D);
      JLabel var7;
      if (PropertyList.getInstance().get("prop.dynamic.avdh_direct_from_menu") == null) {
         var7 = new JLabel("Beküldés helye:");
         GuiUtil.setDynamicBound(var7, var7.getText(), 30, var1);
         var4.add(var7);
         var1 = (int)((double)var1 + var7.getBounds().getHeight() + 10.0D);
         ButtonGroup var8 = new ButtonGroup();
         JRadioButton var9 = GuiUtil.getANYKRadioButton("Ügyfélkapu");
         var9.setSelected(true);
         var8.add(var9);
         GuiUtil.setDynamicBound(var9, var9.getText(), 45, var1);
         var4.add(var9);
         var1 = (int)((double)var1 + var9.getBounds().getHeight() + 10.0D);
      }

      if (this.avdh == 1) {
         var7 = new JLabel("A listában minden nyomtatvány esetében ugyanazzal az adattal történik az azonosítás.");
         var7.setBounds(30, var1, dialogWidth, GuiUtil.getCommonItemHeight() + 2);
         var4.setPreferredSize(new Dimension(dialogWidth, 465));
         var4.add(var7);
         int var10000 = var1 + this.yStep;
      }

      GuiUtil.setPanelBoundsByComponents(var4, 0, 0);
      this.getContentPane().add(var4, "Center");
      this.getContentPane().add(var5, "South");
      Dimension var10 = new Dimension(var4.getWidth(), var4.getHeight() + 10 + 100);
      this.setPreferredSize(var10);
      this.setMinimumSize(var10);
      this.setSize(var10);
   }

   public int getState() {
      return this.state;
   }

   public boolean showIfNeed() {
      this.initControls();
      this.setVisible(true);
      return true;
   }

   private void authOkAction() {
      this.state = 3;
      this.kauAuthHelper.setAnyGateId((String)null);
      KauSessionTimeoutHandler.getInstance().reset();
      this.kauAuthHelper.setUgyfelkapura(true);
      this.setVisible(false);
      this.dispose();
   }

   private void authCancelAction() {
      this.state = 0;
      this.setVisible(false);
      this.dispose();
   }

   public void actionPerformed(ActionEvent var1) {
      if (var1.getSource() == this.authOkButton) {
         this.authOkAction();
      }

      if (var1.getSource() == this.authCancelButton) {
         this.authCancelAction();
      }

   }

   public void windowOpened(WindowEvent var1) {
   }

   public void windowClosing(WindowEvent var1) {
      if (this.state < 2) {
         this.state = 0;
      }

   }

   public void windowClosed(WindowEvent var1) {
   }

   public void windowIconified(WindowEvent var1) {
   }

   public void windowDeiconified(WindowEvent var1) {
   }

   public void windowActivated(WindowEvent var1) {
   }

   public void windowDeactivated(WindowEvent var1) {
   }

   public void keyTyped(KeyEvent var1) {
   }

   public void keyPressed(KeyEvent var1) {
   }

   public void keyReleased(KeyEvent var1) {
      if (var1.getKeyCode() == 10) {
         this.authOkAction();
      }

   }

   private void initControls() {
      this.listenerOff = true;
      this.listenerOff = false;
   }
}
