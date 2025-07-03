package hu.piller.enykp.alogic.filepanels.mohu;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.niszws.util.GateType;
import hu.piller.enykp.niszws.util.KauAuthHelper;
import hu.piller.enykp.niszws.util.KauSessionTimeoutHandler;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.icon.ENYKIconSet;
import java.awt.Dimension;
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
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class OfficeUserAndPassDialog extends LoginDialog implements ActionListener, WindowListener, KeyListener {
   private KauAuthHelper kauAuthHelper;
   private JTextField userName = new JTextField();
   private JPasswordField pass = new JPasswordField();
   private JComboBox officeUserNameCB;
   private JTextField officeUserNameTF;
   private JCheckBox savePass;
   private JButton authOkButton = new JButton("Rendben");
   private JButton authCancelButton = new JButton("Mégsem");
   private JRadioButton ukRB = GuiUtil.getANYKRadioButton("Ügyfélkapu");
   private JRadioButton hkckRB = GuiUtil.getANYKRadioButton("Cég/Hivatali kapu azonosító : ");
   private JRadioButton uniqeRB = GuiUtil.getANYKRadioButton("Egyedi Cég/Hivatali kapu azonosítás");
   private int state;
   private boolean listenerOff = true;
   private int avdhMode;
   private String oriUser = "";
   private String oriPass = "";
   private String oriAzon = "";
   private String formDataCKAzon = "";
   private String savedAzon4RadioButtonStateChange = "";
   private static int dialogWidth = 580;
   private int yStep;
   private int xStart = 30;

   public OfficeUserAndPassDialog(int var1, boolean var2, String var3, String var4) {
      super(MainFrame.thisinstance, var4 + " - Azonosító adatok megadása", true);
      dialogWidth = GuiUtil.getW("Azonosító adatok megjegyzése a programból való kilépésig:WWWWW") + 120;
      if (var1 == 1) {
         dialogWidth = GuiUtil.getW("A listában minden nyomtatvány esetében ugyanazzal az adattal történik az azonosítás.") + 120;
      }

      this.yStep = (int)(1.5D * (double)GuiUtil.getCommonItemHeight());
      this.avdhMode = var1;
      this.formDataCKAzon = var3;
      this.setGroupLogin(var2);
      this.kauAuthHelper = KauAuthHelper.getInstance();
      this.savedAzon4RadioButtonStateChange = "";
      String var5 = this.getDefaultCKAzon();
      this.officeUserNameCB = null;
      this.officeUserNameTF = null;
      if (var3 != null) {
         if (!"".equals(var5)) {
            this.officeUserNameCB = new JComboBox(new String[]{var5, var3});
         } else {
            this.officeUserNameTF = new JTextField(var3);
         }
      } else {
         this.officeUserNameTF = new JTextField(var5);
      }

      if (this.officeUserNameCB != null) {
         this.officeUserNameCB.setEditable(true);
      } else {
         this.officeUserNameTF.setEditable(true);
      }

      this.saveOri(this.kauAuthHelper.getMohuUser(), this.kauAuthHelper.getMohuPass(), this.kauAuthHelper.getAnyGateId());
      this.init();
   }

   private void init() {
      this.state = -1;
      this.setDefaultCloseOperation(0);
      Border var1 = BorderFactory.createEtchedBorder();
      TitledBorder var2 = new TitledBorder(var1, "Válassza ki a beküldés helyét!");
      TitledBorder var3 = new TitledBorder(var1, "Ügyfélkapu azonosító");
      var3.setTitleJustification(1);
      var2.setTitleJustification(1);
      JPanel var4 = new JPanel((LayoutManager)null);
      var4.setBorder(var3);
      Dimension var5 = new Dimension(dialogWidth, 2 * (GuiUtil.getCommonItemHeight() + 2) + 2 * this.yStep);
      var4.setPreferredSize(var5);
      var4.setSize(var5);
      var4.setMinimumSize(var5);
      var4.setBounds(0, 0, dialogWidth, 2 * (GuiUtil.getCommonItemHeight() + 2) + 2 * this.yStep);
      JPanel var6 = new JPanel((LayoutManager)null);
      var6.setBorder(var2);
      var6.setPreferredSize(new Dimension(dialogWidth, 4 * (GuiUtil.getCommonItemHeight() + 2) + 3 * this.yStep));
      var6.setSize(new Dimension(dialogWidth, 4 * (GuiUtil.getCommonItemHeight() + 2) + 3 * this.yStep));
      var6.setBounds(0, (int)var4.getPreferredSize().getHeight(), dialogWidth, 4 * (GuiUtil.getCommonItemHeight() + 2) + 3 * this.yStep);
      ItemListener var7 = new ItemListener() {
         public void itemStateChanged(ItemEvent var1) {
            if (var1.getStateChange() == 1) {
               OfficeUserAndPassDialog.this.radioStateChanger(((JRadioButton)var1.getSource()).getName());
            }

         }
      };
      this.hkckRB.addItemListener(var7);
      this.hkckRB.setName("0");
      this.ukRB.addItemListener(var7);
      this.ukRB.setName("1");
      this.uniqeRB.addItemListener(var7);
      this.uniqeRB.setName("2");
      String var8 = this.kauAuthHelper.getAnyGateId();
      if (this.officeUserNameCB != null) {
         this.officeUserNameCB.setSelectedItem(var8);
         this.officeUserNameCB.getEditor().setItem(var8);
      } else {
         this.officeUserNameTF.setText(this.getNotEmptyAzon(var8));
      }

      JPanel var9 = new JPanel((LayoutManager)null);
      var9.setPreferredSize(new Dimension((int)(var4.getPreferredSize().getWidth() + var6.getPreferredSize().getWidth()), (int)(var4.getPreferredSize().getHeight() + var6.getPreferredSize().getHeight()) + this.yStep));
      var9.setSize(new Dimension((int)(var4.getPreferredSize().getWidth() + var6.getPreferredSize().getWidth()), (int)(var4.getPreferredSize().getHeight() + var6.getPreferredSize().getHeight()) + this.yStep));
      var9.setMinimumSize(new Dimension((int)(var4.getPreferredSize().getWidth() + var6.getPreferredSize().getWidth()), (int)(var4.getPreferredSize().getHeight() + var6.getPreferredSize().getHeight()) + this.yStep));
      int var10 = this.yStep;
      JLabel var11 = new JLabel("felhasználónév : ");
      GuiUtil.setDynamicBound(var11, var11.getText(), 30, this.yStep);
      this.userName.setBounds(GuiUtil.getPositionFromPrevComponent(var11), var10, GuiUtil.getW("WWWWWWWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 4);
      var10 += this.yStep;
      JLabel var12 = new JLabel("jelszó : ");
      GuiUtil.setDynamicBound(var12, var12.getText(), 30, var10);
      this.pass.setBounds(GuiUtil.getPositionFromPrevComponent(var11), var10, GuiUtil.getW("WWWWWWWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 4);
      var4.add(var11);
      var4.add(this.userName);
      var4.add(var12);
      var4.add(this.pass);
      var9.add(var4);
      var10 = this.yStep;
      GuiUtil.setDynamicBound(this.hkckRB, this.hkckRB.getText(), 30, var10);
      if (this.officeUserNameCB != null) {
         this.officeUserNameCB.setBounds(GuiUtil.getPositionFromPrevComponent(this.hkckRB), var10, GuiUtil.getW("WWWWWWWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 4);
      } else {
         this.officeUserNameTF.setBounds(GuiUtil.getPositionFromPrevComponent(this.hkckRB), var10, GuiUtil.getW("WWWWWWWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 4);
      }

      var10 += this.yStep;
      GuiUtil.setDynamicBound(this.ukRB, this.ukRB.getText(), 30, var10);
      var10 += this.yStep;
      GuiUtil.setDynamicBound(this.uniqeRB, this.uniqeRB.getText(), 30, var10);
      if (this.isGroupLogin()) {
         int var10000 = var10 + this.yStep;
      }

      var6.add(this.ukRB);
      var6.add(this.hkckRB);
      if (this.officeUserNameCB != null) {
         var6.add(this.officeUserNameCB);
      } else {
         var6.add(this.officeUserNameTF);
      }

      int var16 = GuiUtil.getCommonItemHeight() + 2;
      int var17 = GuiUtil.getCommonItemHeight() + 2;

      Object var13;
      try {
         var13 = ENYKIconSet.getInstance().get("anyk_sugo");
      } catch (Exception var22) {
         try {
            var13 = UIManager.getIcon("OptionPane.questionIcon");
         } catch (Exception var21) {
            var13 = null;
         }
      }

      JButton var14;
      JButton var15;
      if (var13 != null) {
         var16 = Math.max(var16, ((Icon)var13).getIconWidth() + 2);
         var17 = Math.max(var17, ((Icon)var13).getIconHeight() + 2);
         var14 = new JButton((Icon)var13);
         var15 = new JButton((Icon)var13);
      } else {
         var14 = new JButton(".?.");
         var15 = new JButton(".?.");
      }

      var14.setToolTipText("Segítség a Cég/Hivatali kapu azonosítóhoz...");
      var15.setToolTipText("Segítség az Egyedi Cégkapu azonosításhoz...");
      var14.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            GuiUtil.showMessageDialog(OfficeUserAndPassDialog.this, "Cégkapu azonosító: a cég adószámának első 8 számjegye\nHivatali kapu azonosító: Hivatali kapu rövid neve" + (OfficeUserAndPassDialog.this.isGroupLogin() ? "\n\nFigyelem!\nA \"Nyomtatványok csoportos közvetlen beküldése a Cég/Hivatali kapun keresztül\" funkció használatakor\naz itt megadott érték felülírja a \"Cég/Hivatali kapu azonosító\" mezőben megjelent korábbi értéket!" : ""), "Cég/Hivatali kapu azonosító", 1);
         }
      });
      var15.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            GuiUtil.showMessageDialog(OfficeUserAndPassDialog.this, "Amennyiben ezt az opciót választja, a Cég/Hivatali kapu azonosító\naz előző ablakban - a 'Cég/Hivatali kapu azonosító' oszlopban - beállított érték lesz", "Egyedi Cég/Hivatali kapu azonosítás", 1);
         }
      });
      var10 = this.yStep;
      var14.setBounds(GuiUtil.getPositionFromPrevComponent((JComponent)(this.officeUserNameTF == null ? this.officeUserNameCB : this.officeUserNameTF)), var10, var16, var17);
      var10 += 2 * this.yStep;
      var15.setBounds(GuiUtil.getPositionFromPrevComponent((JComponent)(this.officeUserNameTF == null ? this.officeUserNameCB : this.officeUserNameTF)), var10, var16, var17);
      if (this.isGroupLogin()) {
         var6.add(this.uniqeRB);
         var6.add(var15);
      }

      var6.add(var14);
      var10 += this.yStep;
      this.savePass = GuiUtil.getANYKCheckBox("Azonosító adatok megjegyzése a programból való kilépésig: ");
      this.savePass.setHorizontalTextPosition(10);
      this.savePass.setBorder(var1);
      GuiUtil.setDynamicBound(this.savePass, this.savePass.getText(), 20, var10);
      var6.add(this.savePass);
      ButtonGroup var18 = new ButtonGroup();
      var18.add(this.ukRB);
      var18.add(this.hkckRB);
      if (this.isGroupLogin()) {
         var18.add(this.uniqeRB);
      }

      if (this.isGroupLogin()) {
         this.uniqeRB.setSelected(true);
      } else {
         this.hkckRB.setSelected(true);
      }

      var9.add(var6);
      var10 += var6.getHeight();
      JPanel var19 = new JPanel();
      this.authOkButton.addActionListener(this);
      this.authCancelButton.addActionListener(this);
      this.userName.addKeyListener(this);
      this.pass.addKeyListener(this);
      this.authOkButton.setSize(new Dimension(GuiUtil.getW(this.authOkButton, this.authOkButton.getText()), GuiUtil.getCommonItemHeight() + 2));
      this.authCancelButton.setSize(new Dimension(GuiUtil.getW(this.authCancelButton, this.authCancelButton.getText()), GuiUtil.getCommonItemHeight() + 2));
      var19.add(this.authOkButton);
      var19.add(this.authCancelButton);
      var19.setBorder(new EmptyBorder(5, 5, 5, 5));
      this.savePass.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent var1) {
            if (!OfficeUserAndPassDialog.this.listenerOff) {
               OfficeUserAndPassDialog.this.kauAuthHelper.guiSetSaveUserAndPass(OfficeUserAndPassDialog.this.savePass.isSelected());
               if (OfficeUserAndPassDialog.this.savePass.isSelected()) {
                  SwingUtilities.invokeLater(new Runnable() {
                     public void run() {
                        GuiUtil.showMessageDialog(OfficeUserAndPassDialog.this, "A programból való kilépésig megjegyzésre kerül a jelszó.\nNe hagyja felügyelet nélkül a gépét, vagy ha igen előtte lépjen ki a programból!", "Figyelmeztetés", 2);
                     }
                  });
               } else {
                  SwingUtilities.invokeLater(new Runnable() {
                     public void run() {
                        if (JOptionPane.showOptionDialog(MainFrame.thisinstance, "A korábban megjegyzett bejelentkezési adatokat töröljük.\nFolytatja?", "Megjegyzett bejelentkezési adatok elfelejtése", 0, 3, (Icon)null, PropertyList.igenNem, PropertyList.igenNem[0]) == 0) {
                           OfficeUserAndPassDialog.this.kauAuthHelper.reset();
                           KauSessionTimeoutHandler.getInstance().reset();
                        } else {
                           OfficeUserAndPassDialog.this.listenerOff = true;
                           OfficeUserAndPassDialog.this.savePass.setSelected(true);
                           OfficeUserAndPassDialog.this.listenerOff = false;
                        }

                     }
                  });
               }

            }
         }
      });
      if (this.avdhMode != 0 && this.avdhMode == 1) {
         JLabel var20 = new JLabel("A listában minden nyomtatvány esetében ugyanazzal az adattal történik az azonosítás.");
         GuiUtil.setDynamicBound(var20, var20.getText(), 25, var10);
         var9.add(var20);
      }

      this.setResizable(true);
      this.getContentPane().add(var9, "Center");
      this.getContentPane().add(var19, "South");
      this.setSize(new Dimension(dialogWidth + 60, this.avdhMode == 1 ? this.yStep * 16 : this.yStep * 14));
      this.setMinimumSize(new Dimension(dialogWidth + 60, this.avdhMode == 1 ? this.yStep * 16 : this.yStep * 14));
      this.setPreferredSize(new Dimension(dialogWidth + 60, this.avdhMode == 1 ? this.yStep * 16 : this.yStep * 14));
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
         var2.printStackTrace();
         this.state = 1;
      }

      this.kauAuthHelper.setGateType(this.ukRB.isSelected() ? GateType.UGYFELKAPU : GateType.CEGKAPU_HIVATALIKAPU);
      this.kauAuthHelper.setUgyfelkapura(this.ukRB.isSelected());
      this.kauAuthHelper.guiSetSaveUserAndPass(this.savePass.isSelected());
      this.userName.setText("");
      this.pass.setText("");
      this.setVisible(false);
      this.dispose();
   }

   private void authCancelAction() {
      this.state = 0;
      this.kauAuthHelper.setGateType(this.ukRB.isSelected() ? GateType.UGYFELKAPU : GateType.CEGKAPU_HIVATALIKAPU);
      this.kauAuthHelper.setUgyfelkapura(this.ukRB.isSelected());
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
         if (this.officeUserNameCB != null) {
            if (this.hkckRB.isSelected() && this.officeUserNameCB.getEditor().getItem().toString().length() == 0) {
               GuiUtil.showMessageDialog(this, "Nem adott meg Cég/Hivatali kapu azonosítót!", "Üzenet", 0);
               this.state = 0;
               return -1;
            }
         } else if (this.hkckRB.isSelected() && this.officeUserNameTF.getText().length() == 0) {
            GuiUtil.showMessageDialog(this, "Nem adott meg Cég/Hivatali kapu azonosítót!", "Üzenet", 0);
            this.state = 0;
            return -1;
         }

         if (this.needToSave()) {
            KauSessionTimeoutHandler.getInstance().reset();
         }

         this.kauAuthHelper.setMohuUser(this.userName.getText());
         this.kauAuthHelper.setMohuPass(this.pass.getPassword());
         if (this.officeUserNameCB != null) {
            this.kauAuthHelper.setAnyGateId(this.officeUserNameCB.getEditor().getItem().toString());
         } else {
            this.kauAuthHelper.setAnyGateId(this.officeUserNameTF.getText());
         }

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
      if (this.state != 3) {
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
         if (this.officeUserNameCB != null) {
            this.officeUserNameCB.getEditor().setItem(this.kauAuthHelper.getAnyGateId());
         } else {
            this.officeUserNameTF.setText(this.kauAuthHelper.getAnyGateId());
         }
      } else {
         this.userName.setText("");
         this.pass.setText("");
         if (this.officeUserNameCB != null) {
            this.officeUserNameCB.getEditor().setItem("");
         } else {
            this.officeUserNameTF.setText(this.formDataCKAzon);
         }
      }

      this.radioStateChanger(this.hkckRB.isSelected() ? "0" : "1");
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
      } else if (!this.oriPass.equals(new String(this.pass.getPassword()))) {
         return true;
      } else {
         if (this.officeUserNameCB != null) {
            if (!this.oriAzon.equals(this.officeUserNameCB.getEditor().getItem().toString())) {
               return this.hkckRB.isSelected();
            }
         } else if (!this.oriAzon.equals(this.officeUserNameTF.getText())) {
            return this.hkckRB.isSelected();
         }

         return false;
      }
   }

   private void radioStateChanger(String var1) {
      byte var3;
      if (this.officeUserNameCB != null) {
         this.officeUserNameCB.setEnabled("0".equals(var1));
         var3 = -1;
         switch(var1.hashCode()) {
         case 48:
            if (var1.equals("0")) {
               var3 = 0;
            }
         default:
            switch(var3) {
            case 0:
               if (!this.useFormDataCKAzon()) {
                  this.officeUserNameCB.setSelectedIndex(0);
               } else {
                  this.officeUserNameCB.setSelectedIndex(this.officeUserNameCB.getItemCount() - 1);
               }

               this.officeUserNameCB.getEditor().setItem(this.officeUserNameCB.getSelectedItem());
               break;
            default:
               this.officeUserNameCB.getEditor().setItem("");
            }
         }
      } else {
         this.officeUserNameTF.setEnabled("0".equals(var1));
         var3 = -1;
         switch(var1.hashCode()) {
         case 48:
            if (var1.equals("0")) {
               var3 = 0;
            }
            break;
         case 49:
            if (var1.equals("1")) {
               var3 = 1;
            }
         }

         switch(var3) {
         case 0:
            if (!"".equals(this.savedAzon4RadioButtonStateChange)) {
               this.officeUserNameTF.setText(this.savedAzon4RadioButtonStateChange);
            } else if (!this.useFormDataCKAzon()) {
               this.officeUserNameTF.setText(this.getNotEmptyAzon(this.getDefaultCKAzon()));
            }
            break;
         case 1:
            this.savedAzon4RadioButtonStateChange = this.officeUserNameTF.getText();
            this.officeUserNameTF.setText("");
            break;
         default:
            if (!"".equals(this.savedAzon4RadioButtonStateChange)) {
               this.officeUserNameTF.setText(this.savedAzon4RadioButtonStateChange);
            } else {
               this.officeUserNameTF.setText(this.formDataCKAzon);
            }
         }
      }

   }

   private String getNotEmptyAzon(String var1) {
      return "".equals(var1) ? this.formDataCKAzon : var1;
   }
}
