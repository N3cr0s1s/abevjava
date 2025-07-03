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

public class SimpleCegHivataliKapuAzonPanel extends LoginDialog implements ActionListener, WindowListener, KeyListener {
   private JRadioButton hkckRB = GuiUtil.getANYKRadioButton("Cég/Hivatali kapu");
   private JRadioButton ukRB = GuiUtil.getANYKRadioButton("Ügyfélkapu");
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

   public SimpleCegHivataliKapuAzonPanel(int var1, boolean var2, String var3) {
      super(MainFrame.thisinstance, "Azonosítás", true);
      this.avdhMode = var1;
      this.formDataCKAzon = var3;
      this.setGroupLogin(var2);
      this.kauAuthHelper = KauAuthHelper.getInstance();
      this.savedAzon4RadioButtonStateChange = "";
      String var4 = this.getDefaultCKAzon();
      this.officeUserNameCB = null;
      this.officeUserNameTF = null;
      dialogWidth = GuiUtil.getW("A listában minden nyomtatvány esetében ugyanazzal az adattal történik az azonosítás.") + 60;
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
      int var2 = (int)(1.8D * (double)GuiUtil.getCommonItemHeight());
      this.state = -1;
      this.setDefaultCloseOperation(0);
      ItemListener var3 = new ItemListener() {
         public void itemStateChanged(ItemEvent var1) {
            if (var1.getStateChange() == 1) {
               SimpleCegHivataliKapuAzonPanel.this.radioStateChanger(((JRadioButton)var1.getSource()).getName());
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
      TitledBorder var6 = new TitledBorder(var5, "Ön a DÁP azonosítási módot választotta");
      var6.setTitleJustification(1);
      JPanel var7 = new JPanel((LayoutManager)null);
      var7.setBorder(var6);
      JLabel var8 = new JLabel("Az azonosítás megkezdése előtt kérjük adja meg a használni kívánt Cég/Hivatali kapu azonosítót:");
      int var9 = GuiUtil.getW(var8, var8.getText());
      int var10 = (int)var8.getPreferredSize().getHeight();
      var9 = Math.max(var9, dialogWidth);
      dialogWidth = var9;
      GuiUtil.setDynamicBound(var8, var8.getText(), 25, var1);
      var7.add(var8);
      var1 += var2;
      ButtonGroup var11 = new ButtonGroup();
      var11.add(this.hkckRB);
      var11.add(this.ukRB);
      var11.add(this.uniqeRB);
      this.hkckRB.setSelected(true);
      GuiUtil.setDynamicBound(this.hkckRB, this.hkckRB.getText(), 45, var1);
      JLabel var12 = new JLabel("azonosító :");
      GuiUtil.setDynamicBound(var12, var12.getText(), GuiUtil.getPositionFromPrevComponent(this.hkckRB), var1);
      int var14 = var1;
      int var13;
      if (this.officeUserNameCB != null) {
         this.officeUserNameCB.setBounds(GuiUtil.getPositionFromPrevComponent(var12), var1, GuiUtil.getW("WWWWWWWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 4);
         var13 = GuiUtil.getPositionFromPrevComponent(this.officeUserNameCB) + 10;
      } else {
         this.officeUserNameTF.setBounds(GuiUtil.getPositionFromPrevComponent(var12), var1, GuiUtil.getW("WWWWWWWWWWWWWWW"), GuiUtil.getCommonItemHeight() + 4);
         var13 = GuiUtil.getPositionFromPrevComponent(this.officeUserNameTF) + 10;
      }

      var1 += var2;
      GuiUtil.setDynamicBound(this.ukRB, this.ukRB.getText(), 45, var1);
      var1 += var2;
      if (this.isGroupLogin()) {
         var11.add(this.uniqeRB);
         GuiUtil.setDynamicBound(this.uniqeRB, this.uniqeRB.getText(), 45, var1);
         var1 += var2;
      }

      if (this.isGroupLogin()) {
         this.uniqeRB.setSelected(true);
      } else {
         this.hkckRB.setSelected(true);
      }

      int var18 = GuiUtil.getCommonItemHeight() + 2;
      int var19 = GuiUtil.getCommonItemHeight() + 2;

      Object var15;
      try {
         var15 = ENYKIconSet.getInstance().get("anyk_sugo");
      } catch (Exception var24) {
         try {
            var15 = UIManager.getIcon("OptionPane.questionIcon");
         } catch (Exception var23) {
            var15 = null;
         }
      }

      JButton var16;
      JButton var17;
      if (var15 != null) {
         var18 = Math.max(var18, ((Icon)var15).getIconWidth() + 2);
         var19 = Math.max(var19, ((Icon)var15).getIconHeight() + 2);
         var16 = new JButton((Icon)var15);
         var17 = new JButton((Icon)var15);
      } else {
         var16 = new JButton(".?.");
         var17 = new JButton(".?.");
      }

      var16.setToolTipText("Segítség a Cég/Hivatali kapu azonosítóhoz");
      var17.setToolTipText("Segítség az Egyedi Cégkapu azonosításhoz");
      var16.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            GuiUtil.showMessageDialog(SimpleCegHivataliKapuAzonPanel.this, "Cégkapu azonosító: a cég adószámának első 8 számjegye\nHivatali kapu azonosító: Hivatali kapu rövid neve" + (SimpleCegHivataliKapuAzonPanel.this.isGroupLogin() ? "\n\nFigyelem!\nA \"Nyomtatványok csoportos közvetlen beküldése a Cég/Hivatali kapun keresztül\" funkció használatakor\naz itt megadott érték felülírja a \"Cég/Hivatali kapu azonosító\" mezőben megjelent korábbi értéket!" : ""), "Cég/Hivatali kapu azonosító", 1);
         }
      });
      var17.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            GuiUtil.showMessageDialog(SimpleCegHivataliKapuAzonPanel.this, "Amennyiben ezt az opciót választja, a Cég/Hivatali kapu azonosító\naz előző ablakban - a 'Cég/Hivatali kapu azonosító' oszlopban - beállított érték lesz", "Egyedi Cég/Hivatali kapu azonosítás", 1);
         }
      });
      var16.setBounds(var13, var14, var18, var19);
      var14 += 2 * var2;
      var17.setBounds(var13, var14, var18, var19);
      var7.add(this.hkckRB);
      var7.add(this.ukRB);
      if (this.isGroupLogin()) {
         var7.add(this.uniqeRB);
      }

      var7.add(var12);
      if (this.officeUserNameCB != null) {
         var7.add(this.officeUserNameCB);
      } else {
         var7.add(this.officeUserNameTF);
      }

      if (this.isGroupLogin()) {
         var7.add(this.uniqeRB);
         var7.add(var17);
      }

      var7.add(var16);
      if (this.avdhMode == 1) {
         JLabel var20 = new JLabel("A listában minden nyomtatvány esetében ugyanazzal az adattal történik az azonosítás.");
         GuiUtil.setDynamicBound(var20, var20.getText(), 25, var1);
         var7.add(var20);
         var1 += var2;
      }

      int var10000 = var1 + var2;
      JPanel var25 = new JPanel(new FlowLayout(1));
      int var21 = Math.max(50, GuiUtil.getCommonItemHeight() + 8);
      var25.setSize(var9, var21);
      var25.setPreferredSize(new Dimension(var9, var21));
      var25.setMinimumSize(new Dimension(var9, var21));
      this.authOkButton.addActionListener(this);
      this.authCancelButton.addActionListener(this);
      var25.add(this.authOkButton);
      var25.add(this.authCancelButton);
      this.add(var7, "Center");
      this.add(var25, "South");
      GuiUtil.setPanelBoundsByComponents(var7, 0, 0);
      Dimension var22 = new Dimension(dialogWidth + 60, var7.getHeight() + 30 + var21);
      this.setPreferredSize(var22);
      this.setMinimumSize(var22);
      this.setSize(var22);
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
      this.kauAuthHelper.setSaveUserAndPass(false);
      if (this.officeUserNameCB != null) {
         this.kauAuthHelper.setAnyGateId(this.officeUserNameCB.getEditor().getItem().toString());
      } else if (!"".equals(this.officeUserNameTF.getText())) {
         this.kauAuthHelper.setAnyGateId(this.officeUserNameTF.getText());
      } else {
         this.kauAuthHelper.setAnyGateId((String)null);
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
