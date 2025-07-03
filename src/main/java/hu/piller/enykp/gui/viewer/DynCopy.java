package hu.piller.enykp.gui.viewer;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.icon.ENYKIconSet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class DynCopy extends JPanel {
   JTextField tf;
   JButton b;
   EventListenerList el;

   public DynCopy() {
      this.setLayout(new BorderLayout());
      this.tf = new JTextField(6);
      this.tf.setText("1");
      this.tf.setHorizontalAlignment(4);
      this.tf.setInputVerifier(new InputVerifier() {
         public boolean verify(JComponent var1) {
            try {
               int var2 = Integer.parseInt(DynCopy.this.tf.getText());
            } catch (NumberFormatException var3) {
               DynCopy.this.tf.setText("1");
            }

            return true;
         }
      });
      this.tf.setPreferredSize(new Dimension((int)this.tf.getPreferredSize().getWidth(), GuiUtil.getCommonItemHeight() + 2));
      this.tf.setSize(new Dimension((int)this.tf.getPreferredSize().getWidth(), GuiUtil.getCommonItemHeight() + 2));
      ENYKIconSet var1 = ENYKIconSet.getInstance();
      this.b = new JButton();
      this.b.setToolTipText("Új lap adat átmásolással");
      this.b.setIcon(var1.get("anyk_uj_dokumentum"));
      this.b.setPreferredSize(new Dimension(GuiUtil.getToolBarHeight() - 2, GuiUtil.getCommonItemHeight() + 2));
      this.b.setMaximumSize(new Dimension(25, GuiUtil.getCommonItemHeight() + 2));
      this.b.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Object[] var2 = new Object[]{"Igen", "Nem"};
            int var3 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Indulhat " + DynCopy.this.tf.getText() + " db. lap másolása ezzel az adattartalommal?", "Kérdés", 0, 3, (Icon)null, var2, var2[0]);
            if (var3 == 0) {
               DynCopy.this.fireChange();
               DynCopy.this.tf.setText("1");
            }
         }
      });
      this.el = new EventListenerList();
      this.add(this.tf, "West");
      this.add(this.b, "East");
      Dimension var2 = new Dimension(this.tf.getPreferredSize().width + this.b.getPreferredSize().width, GuiUtil.getCommonItemHeight() + 6);
      this.setPreferredSize(var2);
      this.setMaximumSize(var2);
   }

   public void setreadonly(boolean var1) {
      this.tf.setEnabled(!var1);
      this.b.setEnabled(!var1);
   }

   public void addChangeListener(ChangeListener var1) {
      this.el.add(ChangeListener.class, var1);
   }

   public void removeChangeListener(ChangeListener var1) {
      this.el.remove(ChangeListener.class, var1);
   }

   protected void fireChange() {
      Object[] var1 = this.el.getListenerList();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] == ChangeListener.class) {
            ((ChangeListener)var1[var2 + 1]).stateChanged(new ChangeEvent(this.tf));
         }
      }

   }
}
