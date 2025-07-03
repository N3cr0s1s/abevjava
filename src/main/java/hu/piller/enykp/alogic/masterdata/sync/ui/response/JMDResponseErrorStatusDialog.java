package hu.piller.enykp.alogic.masterdata.sync.ui.response;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class JMDResponseErrorStatusDialog extends JDialog implements ActionListener {
   JLabel lbl_header;
   JLabel lbl_lblnev;
   JLabel lbl_nev;
   JLabel lbl_lblverzio;
   JLabel lbl_verzio;
   JPanel pnl_description;
   JTextPane text_description;
   JButton button;

   public JMDResponseErrorStatusDialog(String var1, String var2, String var3) {
      super(MainFrame.thisinstance);
      this.init(var1, var2, var3);
      int var4 = Math.max(400, this.text_description.getWidth());
      this.lbl_nev = new JLabel(var1);
      this.setSize(var4, Math.max(12 * GuiUtil.getCommonItemHeight(), 350));
      this.setPreferredSize(this.getSize());
      this.setMinimumSize(this.getSize());
      if (this.getOwner() != null) {
         this.setLocationRelativeTo(this.getOwner());
      }

   }

   private void init(String var1, String var2, String var3) {
      this.setTitle("Adatlekérdezési hiba");
      this.setResizable(true);
      this.setModal(true);
      this.lbl_header = new JLabel("Adatok");
      this.lbl_lblnev = new JLabel("Adózó :");
      this.lbl_nev = new JLabel(var1);
      this.lbl_lblverzio = new JLabel("Azonosító :");
      this.lbl_verzio = new JLabel(var2);
      this.pnl_description = new JPanel(new BorderLayout());
      this.pnl_description.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Hiba részletes leírása"));
      this.text_description = new JTextPane();
      this.text_description.setSize(new Dimension((int)(1.5D * (double)GuiUtil.getW("Hiba részletes leírása")), 6 * (GuiUtil.getCommonItemHeight() + 4)));
      this.text_description.setPreferredSize(this.text_description.getSize());
      this.text_description.setMinimumSize(this.text_description.getSize());
      this.text_description.setEditable(true);

      try {
         this.text_description.getDocument().insertString(0, var3, (AttributeSet)null);
      } catch (BadLocationException var7) {
      }

      this.button = new JButton("Bezár");
      this.button.addActionListener(this);
      this.button.setMinimumSize(new Dimension(GuiUtil.getW(this.button, this.button.getText()), GuiUtil.getCommonItemHeight() + 4));
      this.button.setMaximumSize(this.button.getMinimumSize());
      this.button.setPreferredSize(this.button.getMinimumSize());
      this.getContentPane().setLayout(new BorderLayout());
      JPanel var4 = new JPanel((LayoutManager)null);
      int var5 = GuiUtil.getCommonItemHeight() / 2;
      GuiUtil.setDynamicBound(this.lbl_lblnev, this.lbl_lblnev.getText(), 10, var5);
      var4.add(this.lbl_lblnev);
      GuiUtil.setDynamicBound(this.lbl_nev, this.lbl_nev.getText(), 10 + GuiUtil.getW(this.lbl_lblverzio, this.lbl_lblverzio.getText()) + 10, var5);
      var4.add(this.lbl_nev);
      var5 = (int)((double)var5 + 1.5D * (double)GuiUtil.getCommonItemHeight());
      GuiUtil.setDynamicBound(this.lbl_lblverzio, this.lbl_lblverzio.getText(), 10, var5);
      var4.add(this.lbl_lblverzio);
      GuiUtil.setDynamicBound(this.lbl_verzio, this.lbl_verzio.getText(), 10 + GuiUtil.getW(this.lbl_lblverzio, this.lbl_lblverzio.getText()) + 10, var5);
      var4.add(this.lbl_verzio);
      var4.setSize(new Dimension(GuiUtil.getPositionFromPrevComponent(this.lbl_verzio) + GuiUtil.getW(this.lbl_verzio, this.lbl_verzio.getText() + 40), 4 * GuiUtil.getCommonItemHeight()));
      var4.setPreferredSize(var4.getSize());
      this.getContentPane().add(var4, "North");
      JScrollPane var6 = new JScrollPane(this.text_description, 20, 31);
      this.pnl_description.setSize(new Dimension((int)(1.5D * (double)GuiUtil.getW("Hiba részletes leírása")), 6 * (GuiUtil.getCommonItemHeight() + 4)));
      this.pnl_description.setPreferredSize(this.pnl_description.getSize());
      this.pnl_description.setMinimumSize(this.pnl_description.getSize());
      this.text_description.setCaretPosition(0);
      this.pnl_description.add(var6, "Center");
      this.getContentPane().add(this.pnl_description, "Center");
      this.getContentPane().add(this.button, "South");
   }

   public void actionPerformed(ActionEvent var1) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            JMDResponseErrorStatusDialog.this.dispose();
         }
      });
   }
}
