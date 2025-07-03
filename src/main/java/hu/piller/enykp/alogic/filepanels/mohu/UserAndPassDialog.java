package hu.piller.enykp.alogic.filepanels.mohu;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.niszws.util.GateType;
import hu.piller.enykp.niszws.util.KauAuthHelper;
import hu.piller.enykp.niszws.util.KauSessionTimeoutHandler;
import hu.piller.enykp.util.base.PropertyList;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class UserAndPassDialog extends LoginDialog implements ActionListener, WindowListener, KeyListener {
   private KauAuthHelper kauAuthHelper;
   private JTextField userName = new JTextField();
   private JPasswordField pass = new JPasswordField();
   private JCheckBox savePass;
   private JButton authOkButton = new JButton("Rendben");
   private JButton authCancelButton = new JButton("Mégsem");
   private int state;
   private boolean listenerOff = true;
   private int avdh;
   private String oriUser = "";
   private String oriPass = "";
   private String oriAzon = "";
   private int yStep = 35;
   private static int dialogWidth = 580;

   public UserAndPassDialog(int var1, String var2) {
      super(MainFrame.thisinstance, var2, true);
      this.avdh = var1;
      this.kauAuthHelper = KauAuthHelper.getInstance();
      this.saveOri(this.kauAuthHelper.getMohuUser(), this.kauAuthHelper.getMohuPass(), this.kauAuthHelper.getAnyGateId());
      this.yStep = (int)(1.5D * (double)GuiUtil.getCommonItemHeight());
      dialogWidth = GuiUtil.getW("A listában minden nyomtatvány esetében ugyanazzal az adattal történik az azonosítás.") + 60;
      this.init();
   }

   private void init() {
      this.state = -1;
      this.setDefaultCloseOperation(0);
      Border var1 = BorderFactory.createEtchedBorder();
      TitledBorder var2 = new TitledBorder(var1, "Egyéb paraméterek");
      TitledBorder var3 = new TitledBorder(var1, "Felhasználónév és jelszó");
      var2.setTitleJustification(1);
      var3.setTitleJustification(1);
      JPanel var4 = new JPanel((LayoutManager)null);
      var4.setBorder(var3);
      var4.setPreferredSize(new Dimension(dialogWidth - 20, 7 * (GuiUtil.getCommonItemHeight() + 4)));
      var4.setSize(new Dimension(dialogWidth - 20, 7 * (GuiUtil.getCommonItemHeight() + 4)));
      var4.setMinimumSize(new Dimension(dialogWidth - 20, 7 * (GuiUtil.getCommonItemHeight() + 4)));
      int var5 = GuiUtil.getCommonItemHeight() + 15;
      JLabel var6 = new JLabel("felhasználónév : ");
      GuiUtil.setDynamicBound(var6, var6.getText(), 30, var5);
      var4.add(var6);
      int var7 = GuiUtil.getPositionFromPrevComponent(var6) + 10;
      this.userName.setBounds(var7, var5, GuiUtil.getW("WWWWWWWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 4);
      this.userName.setName("userName");
      var5 += this.yStep;
      JLabel var8 = new JLabel("jelszó : ");
      GuiUtil.setDynamicBound(var8, var8.getText(), 30, var5);
      var4.add(var8);
      this.pass.setBounds(var7, var5, GuiUtil.getW("WWWWWWWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 4);
      this.pass.setName("pass");
      var4.add(this.userName);
      var4.add(this.pass);
      var5 += this.yStep;
      JPanel var9 = new JPanel();
      this.authOkButton.addActionListener(this);
      this.authCancelButton.addActionListener(this);
      this.userName.addKeyListener(this);
      this.pass.addKeyListener(this);
      this.authOkButton.setSize(new Dimension(GuiUtil.getW(this.authOkButton, this.authOkButton.getText()), GuiUtil.getCommonItemHeight() + 4));
      this.authCancelButton.setSize(new Dimension(GuiUtil.getW(this.authCancelButton, this.authCancelButton.getText()), GuiUtil.getCommonItemHeight() + 4));
      var9.add(this.authOkButton);
      var9.add(this.authCancelButton);
      var9.setSize(new Dimension(dialogWidth - 20, 2 * this.authOkButton.getHeight()));
      var9.setPreferredSize(new Dimension(dialogWidth - 20, 2 * this.authOkButton.getHeight()));
      JPanel var10 = new JPanel(new GridLayout(1, 1));
      var10.setBorder(var2);
      this.savePass = GuiUtil.getANYKCheckBox("Jelszó megjegyzése a programból való kilépésig: ");
      this.savePass.setHorizontalTextPosition(10);
      this.savePass.setBorder(var1);
      this.savePass.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent var1) {
            if (!UserAndPassDialog.this.listenerOff) {
               UserAndPassDialog.this.kauAuthHelper.guiSetSaveUserAndPass(UserAndPassDialog.this.savePass.isSelected());
               if (UserAndPassDialog.this.savePass.isSelected()) {
                  SwingUtilities.invokeLater(new Runnable() {
                     public void run() {
                        GuiUtil.showMessageDialog(UserAndPassDialog.this, "A programból való kilépésig megjegyzésre kerül a jelszó.\nNe hagyja felügyelet nélkül a gépét, vagy ha igen előtte lépjen ki a programból!", "Figyelmeztetés", 2);
                     }
                  });
               } else {
                  SwingUtilities.invokeLater(new Runnable() {
                     public void run() {
                        if (JOptionPane.showOptionDialog(MainFrame.thisinstance, "A korábban megjegyzett bejelentkezési adatokat töröljük.\nFolytatja?", "Megjegyzett bejelentkezési adatok elfelejtése", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0) {
                           UserAndPassDialog.this.kauAuthHelper.reset();
                           KauSessionTimeoutHandler.getInstance().reset();
                        } else {
                           UserAndPassDialog.this.listenerOff = true;
                           UserAndPassDialog.this.savePass.setSelected(true);
                           UserAndPassDialog.this.listenerOff = false;
                        }

                     }
                  });
               }

            }
         }
      });
      GuiUtil.setDynamicBound(this.savePass, this.savePass.getText(), 25, var5);
      var4.add(this.savePass);
      var5 += this.yStep;
      if (this.avdh == 0) {
         this.getContentPane().add(var4, "Center");
         this.setResizable(true);
      } else {
         if (this.avdh == 1) {
            JLabel var11 = new JLabel("A listában minden nyomtatvány esetében ugyanazzal az adattal történik az azonosítás.");
            GuiUtil.setDynamicBound(var11, var11.getText(), 25, var5);
            var4.add(var11);
         }

         this.getContentPane().add(var4, "North");
         this.setResizable(true);
      }

      this.getContentPane().add(var9, "South");
      this.setSize(new Dimension(dialogWidth + 60, this.yStep * 10));
      this.setMinimumSize(new Dimension(dialogWidth + 60, this.yStep * 10));
      this.setPreferredSize(new Dimension(dialogWidth + 60, this.yStep * 10));
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
      try {
         if (this.setUandP() == -1) {
            return;
         }

         this.state = 3;
      } catch (Exception var2) {
         this.state = 1;
      }

      this.kauAuthHelper.setGateType(GateType.UGYFELKAPU);
      this.kauAuthHelper.setUgyfelkapura(true);
      this.kauAuthHelper.guiSetSaveUserAndPass(this.savePass.isSelected());
      this.userName.setText("");
      this.pass.setText("");
      this.setVisible(false);
      this.dispose();
   }

   private void authCancelAction() {
      this.state = 0;
      this.kauAuthHelper.setGateType(GateType.UGYFELKAPU);
      this.kauAuthHelper.setUgyfelkapura(true);
      this.kauAuthHelper.guiSetSaveUserAndPass(this.savePass.isSelected());
      this.kauAuthHelper.setMohuUser(this.userName.getText());
      this.kauAuthHelper.setMohuPass(this.pass.getPassword());
      this.userName.setText("");
      this.pass.setText("");
      this.setVisible(false);
      this.dispose();
   }

   private int setUandP() {
      if (this.userName.getText().length() != 0 && this.pass.getPassword().length != 0) {
         if (this.needToSave()) {
            KauSessionTimeoutHandler.getInstance().reset();
         }

         this.kauAuthHelper.setMohuUser(this.userName.getText());
         this.kauAuthHelper.setMohuPass(this.pass.getPassword());
         this.kauAuthHelper.setAnyGateId((String)null);
         return 0;
      } else {
         GuiUtil.showMessageDialog(this, "Nem adott meg felhasználónevet vagy jelszót!", "Üzenet", 0);
         this.state = 0;
         return -1;
      }
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
      this.savePass.setSelected(this.kauAuthHelper.isSaveUserAndPass());
      if (this.savePass.isSelected()) {
         this.userName.setText(this.kauAuthHelper.getMohuUser());
         this.pass.setText(this.kauAuthHelper.getMohuPass());
      } else {
         this.userName.setText("");
         this.pass.setText("");
      }

      this.listenerOff = false;
   }

   private void saveOri(String var1, String var2, String var3) {
      this.oriUser = var1 == null ? "" : var1;
      this.oriPass = var2 == null ? "" : var2;
      this.oriAzon = var3 == null ? "" : var3;
   }

   private boolean needToSave() {
      if (!this.oriUser.equals(this.userName.getText())) {
         return true;
      } else {
         return !this.oriPass.equals(new String(this.pass.getPassword()));
      }
   }
}
