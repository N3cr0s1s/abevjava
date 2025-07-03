package hu.piller.enykp.kauclient.simplified.ukp.widget;

import hu.piller.enykp.gui.GuiUtil;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.LayoutManager;
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

public class JTokenDialog extends JDialog implements ActionListener {
   private static int TOKEN_MAX_LENGTH = 6;
   private String token;
   private JTextField txtToken = new JTextField(6);
   private JButton authOkButton = new JButton("Rendben");
   private JButton authCancelButton = new JButton("Mégsem");

   public JTokenDialog(Frame var1, String var2) {
      super(var1, "Ügyfélkapu+ azonosítás 2. lépés", true);
      Border var3 = BorderFactory.createEtchedBorder();
      TitledBorder var4 = new TitledBorder(var3, "Mobilalkalmazásban kapott azonosító megadása");
      var4.setTitleJustification(1);
      int var5 = GuiUtil.getCommonItemHeight() + 10;
      int var7 = GuiUtil.getW("WWWWWWWWWWWWWW");
      var7 = Math.max(var7, GuiUtil.getW(var2));
      JPanel var8 = new JPanel((LayoutManager)null);
      var8.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), var4));
      Dimension var9 = new Dimension(GuiUtil.getW("WWfelhasználónév : WWWWW") + var7 + 20, 6 * GuiUtil.getCommonItemHeight());
      var8.setPreferredSize(var9);
      var8.setSize(var9);
      JLabel var10 = new JLabel("felhasználónév : ");
      GuiUtil.setDynamicBound(var10, var10.getText(), 30, var5);
      var8.add(var10);
      JLabel var11 = new JLabel(var2);
      GuiUtil.setDynamicBound(var11, var11.getText(), GuiUtil.getPositionFromPrevComponent(var10), var5);
      var5 += var5;
      JLabel var12 = new JLabel("azonosító : ");
      GuiUtil.setDynamicBound(var12, var12.getText(), 30, var5);
      var8.add(var12);
      this.txtToken.setBounds(GuiUtil.getPositionFromPrevComponent(var10), var5, var7, GuiUtil.getCommonItemHeight() + 2);
      this.txtToken.setDocument(new PlainDocument() {
         public void insertString(int var1, String var2, AttributeSet var3) throws BadLocationException {
            try {
               if (this.getLength() + var2.length() <= JTokenDialog.TOKEN_MAX_LENGTH) {
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
               JTokenDialog.this.token = JTokenDialog.this.txtToken.getText().trim();
               if (!"".equals(JTokenDialog.this.token)) {
                  JTokenDialog.this.dispose();
               }
            }

         }
      });
      var8.add(var11);
      var8.add(this.txtToken);
      JPanel var13 = new JPanel();
      this.authOkButton.addActionListener(this);
      this.authCancelButton.addActionListener(this);
      var13.add(this.authOkButton);
      var13.add(this.authCancelButton);
      this.getRootPane().registerKeyboardAction((var1x) -> {
         this.dispose();
      }, KeyStroke.getKeyStroke(27, 0), 2);
      this.getContentPane().add(var8, "Center");
      this.getContentPane().add(var13, "South");
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
}
