package hu.piller.enykp.kauclient.simplified.ukpemail.widget;

import hu.piller.enykp.gui.GuiUtil;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class JEmailTokenDialog extends JDialog implements ActionListener {
   private static int TOKEN_MAX_LENGTH = 6;
   private String token;
   private JTextField txtToken = new JTextField(6);
   private JButton authOkButton = new JButton("Rendben");
   private JButton authCancelButton = new JButton("Mégsem");

   public JEmailTokenDialog(Frame var1, String var2, String var3) {
      super(var1, "Ügyfélkapu+ azonosítás 2. lépés", true);
      Border var4 = BorderFactory.createEtchedBorder();
      TitledBorder var5 = new TitledBorder(var4, "E-mailben kapott hitelesítő kód megadása");
      var5.setTitleJustification(1);
      int var6 = GuiUtil.getCommonItemHeight() + 10;
      int var8 = GuiUtil.getW("WWWWWWWWW");
      var8 = Math.max(var8, GuiUtil.getW(var2));
      JPanel var9 = new JPanel((LayoutManager)null);
      Dimension var10 = new Dimension(GuiUtil.getW("WWfelhasználónév : WWWWW") + var8 + 20, 6 * GuiUtil.getCommonItemHeight());
      var9.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), var5));
      var9.setPreferredSize(var10);
      var9.setSize(var10);
      JLabel var11 = new JLabel("felhasználónév : ");
      GuiUtil.setDynamicBound(var11, var11.getText(), 30, var6);
      var9.add(var11);
      JLabel var12 = new JLabel(var2);
      int var13 = GuiUtil.getPositionFromPrevComponent(var11);
      GuiUtil.setDynamicBound(var12, var12.getText(), var13, var6);
      var6 += var6;
      JLabel var14 = new JLabel("hitelesítő kód : ");
      GuiUtil.setDynamicBound(var14, var14.getText(), 30, var6);
      var9.add(var14);
      JLabel var15 = new JLabel(var3 + " - ");
      GuiUtil.setDynamicBound(var15, var15.getText(), var13, var6);
      var9.add(var15);
      this.txtToken.setBounds(GuiUtil.getPositionFromPrevComponent(var15), var6, var8, GuiUtil.getCommonItemHeight() + 2);
      this.txtToken.setDocument(new PlainDocument() {
         public void insertString(int var1, String var2, AttributeSet var3) throws BadLocationException {
            try {
               if (this.getLength() + var2.length() <= JEmailTokenDialog.TOKEN_MAX_LENGTH) {
                  super.insertString(var1, var2, var3);
               }
            } catch (Exception var5) {
               super.insertString(var1, var2, var3);
            }

         }
      });
      this.txtToken.setName("pass");
      this.txtToken.addKeyListener(new KeyAdapter() {
         public void keyReleased(KeyEvent var1) {
            if (var1.getKeyCode() == 10) {
               JEmailTokenDialog.this.token = JEmailTokenDialog.this.txtToken.getText().trim();
               if (!"".equals(JEmailTokenDialog.this.token)) {
                  JEmailTokenDialog.this.dispose();
               }
            } else if (var1.getKeyCode() == 86 && var1.isControlDown()) {
               JEmailTokenDialog.this.txtToken.setText(JEmailTokenDialog.this.getClipboard());
            }

         }
      });
      var9.add(var12);
      var9.add(this.txtToken);
      JPanel var16 = new JPanel();
      this.authOkButton.addActionListener(this);
      this.authCancelButton.addActionListener(this);
      var16.add(this.authOkButton);
      var16.add(this.authCancelButton);
      this.getRootPane().registerKeyboardAction((var1x) -> {
         this.dispose();
      }, KeyStroke.getKeyStroke(27, 0), 2);
      this.getContentPane().add(var9, "Center");
      this.getContentPane().add(var16, "South");
      this.setDefaultCloseOperation(2);
      this.setResizable(true);
      this.pack();
      this.setLocationRelativeTo((Component)null);
   }

   public void actionPerformed(ActionEvent var1) {
      Object var2 = var1.getSource();
      if (var2 == this.authOkButton) {
         this.token = this.txtToken.getText().trim();
      } else {
         this.token = null;
      }

      this.dispose();
   }

   public String run() {
      this.setVisible(true);
      return this.token;
   }

   public String getClipboard() {
      try {
         String var1 = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
         return var1.substring(0, Math.min(var1.length(), 6));
      } catch (Exception var2) {
         return "";
      }
   }
}
