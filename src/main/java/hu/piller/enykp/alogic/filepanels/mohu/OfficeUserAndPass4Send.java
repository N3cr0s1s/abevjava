package hu.piller.enykp.alogic.filepanels.mohu;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.niszws.util.GateType;
import hu.piller.enykp.niszws.util.KauAuthHelper;
import hu.piller.enykp.niszws.util.KauSessionTimeoutHandler;
import hu.piller.enykp.util.icon.ENYKIconSet;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class OfficeUserAndPass4Send extends LoginDialog implements ActionListener, WindowListener, KeyListener {
   private JRadioButton ukRB = GuiUtil.getANYKRadioButton("Ügyfélkapu");
   private JRadioButton hkckRB = GuiUtil.getANYKRadioButton("Cég/Hivatali kapu");
   private JRadioButton uniqeRB = GuiUtil.getANYKRadioButton("Egyedi Cég/Hivatali kapu azonosítás");
   private JComboBox officeUserNameCB;
   private JTextField officeUserNameTF;
   private JButton authOkButton = new JButton("Rendben");
   private JButton authCancelButton = new JButton("Mégsem");
   private int state;
   private boolean listenerOff = true;
   private int avdhMode;
   private KauAuthHelper kauAuthHelper;
   private String formDataCKAzon = "";
   private String savedAzon4RadioButtonStateChange = "";
   private static int dialogWidth;

   public OfficeUserAndPass4Send(int var1, boolean var2, String var3) {
      super(MainFrame.thisinstance, "Azonosítás", true);
      this.avdhMode = var1;
      this.formDataCKAzon = var3;
      this.setGroupLogin(var2);
      this.kauAuthHelper = KauAuthHelper.getInstance();
      this.savedAzon4RadioButtonStateChange = "";
      String var4 = this.getDefaultCKAzon();
      this.officeUserNameCB = null;
      this.officeUserNameTF = null;
      dialogWidth = GuiUtil.getW("Azonosító adatok megjegyzése a programból való kilépésig:WWWWW") + 60;
      if (var1 == 1) {
         dialogWidth = GuiUtil.getW("A listában minden nyomtatvány esetében ugyanazzal az adattal történik az azonosítás.") + 60;
      }

      if (var3 != null) {
         if (!"".equals(var4)) {
            this.officeUserNameCB = new JComboBox(new String[]{var4, var3});
         } else {
            this.officeUserNameTF = new JTextField(var3);
         }
      } else {
         this.officeUserNameTF = new JTextField(var4);
      }

      if (this.officeUserNameCB != null) {
         this.officeUserNameCB.setEditable(true);
      } else {
         this.officeUserNameTF.setEditable(true);
      }

      this.initGui();
   }

   private void initGui() {
      int var1 = GuiUtil.getCommonItemHeight() + 10;
      int var2 = (int)(1.5D * (double)GuiUtil.getCommonItemHeight());
      this.state = -1;
      this.setDefaultCloseOperation(0);
      ItemListener var3 = new ItemListener() {
         public void itemStateChanged(ItemEvent var1) {
            if (var1.getStateChange() == 1) {
               OfficeUserAndPass4Send.this.radioStateChanger(((JRadioButton)var1.getSource()).getName());
            }

         }
      };
      this.hkckRB.addItemListener(var3);
      this.hkckRB.setName("0");
      this.ukRB.addItemListener(var3);
      this.ukRB.setName("1");
      this.uniqeRB.addItemListener(var3);
      this.uniqeRB.setName("2");
      String var4 = this.kauAuthHelper.getAnyGateId();
      if ("".equals(var4)) {
         var4 = this.getDefaultCKAzon();
      }

      if (this.officeUserNameCB != null) {
         this.officeUserNameCB.setSelectedItem(var4);
         this.officeUserNameCB.getEditor().setItem(var4);
      } else {
         this.officeUserNameTF.setText(this.getNotEmptyAzon(var4));
      }

      Border var5 = BorderFactory.createEtchedBorder();
      TitledBorder var6 = new TitledBorder(var5, "Átirányítás");
      var6.setTitleJustification(1);
      JPanel var7 = new JPanel((LayoutManager)null);
      var7.setBorder(var6);
      JLabel var8 = new JLabel("<html>A program az azonosítást a Központi Azonosítási Ügynökkel (KAÜ) végzi.<br>Amennyiben első alkalommal jelentkezik be, a program átirányítja önt a bejelentkezési felületre.</html>");
      int var9 = GuiUtil.getW(var8, "Amennyiben első alkalommal jelentkezik be, a program átirányítja önt a bejelentkezési felületre.");
      int var10 = (int)var8.getPreferredSize().getHeight();
      var9 = Math.max(var9, dialogWidth);
      dialogWidth = var9;
      var8.setBounds(25, var1, var9, var10);
      var1 += var2;
      var1 += var2;
      var7.add(var8);
      JLabel var11 = new JLabel("Válassza ki a beküldés helyét!");
      GuiUtil.setDynamicBound(var11, var11.getText(), 25, var1);
      var7.add(var11);
      var1 += var2;
      ButtonGroup var12 = new ButtonGroup();
      var12.add(this.ukRB);
      var12.add(this.hkckRB);
      this.hkckRB.setSelected(true);
      GuiUtil.setDynamicBound(this.hkckRB, this.hkckRB.getText(), 45, var1);
      JLabel var13 = new JLabel("azonosító :");
      GuiUtil.setDynamicBound(var13, var13.getText(), GuiUtil.getPositionFromPrevComponent(this.hkckRB), var1);
      int var15 = var1;
      int var14;
      if (this.officeUserNameCB != null) {
         this.officeUserNameCB.setBounds(GuiUtil.getPositionFromPrevComponent(var13), var1, GuiUtil.getW("WWWWWWWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 4);
         var14 = GuiUtil.getPositionFromPrevComponent(this.officeUserNameCB) + 10;
      } else {
         this.officeUserNameTF.setBounds(GuiUtil.getPositionFromPrevComponent(var13), var1, GuiUtil.getW("WWWWWWWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 4);
         var14 = GuiUtil.getPositionFromPrevComponent(this.officeUserNameTF) + 10;
      }

      var1 += var2;
      GuiUtil.setDynamicBound(this.ukRB, this.ukRB.getText(), 45, var1);
      var1 += var2;
      if (this.isGroupLogin()) {
         var12.add(this.uniqeRB);
         GuiUtil.setDynamicBound(this.uniqeRB, this.uniqeRB.getText(), 45, var1);
         var1 += var2;
      }

      if (this.isGroupLogin()) {
         this.uniqeRB.setSelected(true);
      } else {
         this.hkckRB.setSelected(true);
      }

      int var19 = GuiUtil.getCommonItemHeight() + 2;
      int var20 = GuiUtil.getCommonItemHeight() + 2;

      Object var16;
      try {
         var16 = ENYKIconSet.getInstance().get("anyk_sugo");
      } catch (Exception var25) {
         try {
            var16 = UIManager.getIcon("OptionPane.questionIcon");
         } catch (Exception var24) {
            var16 = null;
         }
      }

      JButton var17;
      JButton var18;
      if (var16 != null) {
         var19 = Math.max(var19, ((Icon)var16).getIconWidth() + 2);
         var20 = Math.max(var20, ((Icon)var16).getIconHeight() + 2);
         var17 = new JButton((Icon)var16);
         var18 = new JButton((Icon)var16);
      } else {
         var17 = new JButton(".?.");
         var18 = new JButton(".?.");
      }

      var17.setToolTipText("Segítség a Cég/Hivatali kapu azonosítóhoz");
      var18.setToolTipText("Segítség az Egyedi Cégkapu azonosításhoz");
      var17.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            GuiUtil.showMessageDialog(OfficeUserAndPass4Send.this, "Cégkapu azonosító: a cég adószámának első 8 számjegye\nHivatali kapu azonosító: Hivatali kapu rövid neve" + (OfficeUserAndPass4Send.this.isGroupLogin() ? "\n\nFigyelem!\nA \"Nyomtatványok csoportos közvetlen beküldése a Cég/Hivatali kapun keresztül\" funkció használatakor\naz itt megadott érték felülírja a \"Cég/Hivatali kapu azonosító\" mezőben megjelent korábbi értéket!" : ""), "Cég/Hivatali kapu azonosító", 1);
         }
      });
      var18.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            GuiUtil.showMessageDialog(OfficeUserAndPass4Send.this, "Amennyiben ezt az opciót választja, a Cég/Hivatali kapu azonosító\naz előző ablakban - a 'Cég/Hivatali kapu azonosító' oszlopban - beállított érték lesz", "Egyedi Cég/Hivatali kapu azonosítás", 1);
         }
      });
      var17.setBounds(var14, var15, var19, var20);
      var18.setBounds(var14, var15, var19, var20);
      var7.add(this.ukRB);
      var7.add(this.hkckRB);
      if (this.isGroupLogin()) {
         var7.add(this.uniqeRB);
      }

      var7.add(var13);
      if (this.officeUserNameCB != null) {
         var7.add(this.officeUserNameCB);
      } else {
         var7.add(this.officeUserNameTF);
      }

      if (this.isGroupLogin()) {
         var7.add(this.uniqeRB);
         var7.add(var18);
      }

      var7.add(var17);
      if (this.avdhMode == 1) {
         JLabel var21 = new JLabel("A listában minden nyomtatvány esetében ugyanazzal az adattal történik az azonosítás.");
         GuiUtil.setDynamicBound(var21, var21.getText(), 25, var1);
         var7.add(var21);
         int var10000 = var1 + var2;
      }

      JPanel var26 = new JPanel(new FlowLayout(1));
      int var22 = Math.max(50, GuiUtil.getCommonItemHeight() + 8);
      var26.setSize(var9, var22);
      var26.setPreferredSize(new Dimension(var9, var22));
      var26.setMinimumSize(new Dimension(var9, var22));
      this.authOkButton.addActionListener(this);
      this.authCancelButton.addActionListener(this);
      var26.add(this.authOkButton);
      var26.add(this.authCancelButton);
      this.add(var7, "Center");
      this.add(var26, "South");
      GuiUtil.setPanelBoundsByComponents(var7, 0, 0);
      Dimension var23 = new Dimension(dialogWidth + 60, var7.getHeight() + 10 + var22 + 10);
      this.setPreferredSize(var23);
      this.setMinimumSize(var23);
      this.setSize(var23);
   }

   public String getOfficeUsername() {
      return this.officeUserNameCB != null ? this.officeUserNameCB.getEditor().getItem().toString() : this.officeUserNameTF.getText();
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

      this.kauAuthHelper.setGateType(this.ukRB.isSelected() ? GateType.UGYFELKAPU : GateType.CEGKAPU_HIVATALIKAPU);
      this.kauAuthHelper.setUgyfelkapura(this.ukRB.isSelected());
      if (this.officeUserNameCB != null) {
         this.kauAuthHelper.setAnyGateId(this.officeUserNameCB.getEditor().getItem().toString());
      } else {
         this.kauAuthHelper.setAnyGateId(this.officeUserNameTF.getText());
      }

      KauSessionTimeoutHandler.getInstance().reset();
      this.setVisible(false);
      this.dispose();
   }

   private void authCancelAction() {
      this.state = 0;
      this.listenerOff = true;
      this.listenerOff = false;
      this.setVisible(false);
      this.dispose();
   }

   private int setUandP() {
      if (this.hkckRB.isSelected() && this.getOfficeUsername().length() == 0) {
         GuiUtil.showMessageDialog(this, "Hivatali kapura vagy Cégkapura történő\nnyomtatvány beküldést választotta.\nKérjük adja meg a kapu azonosítót!", "Üzenet", 0);
         this.state = 0;
         return -1;
      } else {
         return 0;
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
      if ("".equals(this.getDefaultCKAzon())) {
         if (this.officeUserNameCB != null) {
            this.officeUserNameCB.getEditor().setItem(this.formDataCKAzon);
         } else {
            this.officeUserNameTF.setText(this.formDataCKAzon);
         }
      }

      this.listenerOff = false;
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
