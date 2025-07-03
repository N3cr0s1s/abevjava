package hu.piller.enykp.gui.framework;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.interfaces.ICommandObject;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ToolbarPane extends JPanel {
   private Hashtable items;
   private Hashtable comps;
   public static ToolbarPane thisinstance = null;

   public ToolbarPane(int var1) {
      thisinstance = this;
      this.items = new Hashtable();
      this.comps = new Hashtable();
      this.setLayout(new BoxLayout(this, var1));
      this.setBorder(BorderFactory.createEtchedBorder());
      int var2 = GuiUtil.getToolBarHeight();
      this.setPreferredSize(new Dimension(1, var2));
   }

   public void addTBItem(JComponent var1, ICommandObject var2) {
      if (var1 instanceof JButton) {
         String var3 = var1.getToolTipText();
         ((JButton)var1).setAction(this.createAction(((JButton)var1).getText(), ((JButton)var1).getIcon(), var2));
         var1.setToolTipText(var3);
         var1.setFocusable(false);
         this.items.put(var3, var1);
         this.comps.put(var1, var2);
      }

      this.add(var1);
   }

   public void removeTBItem(JComponent var1) {
      this.items.remove(var1.getToolTipText());
      this.comps.remove(var1);
      this.remove(var1);
   }

   private Action createAction(String var1, Icon var2, final ICommandObject var3) {
      AbstractAction var4 = new AbstractAction(var1, var2) {
         public void actionPerformed(ActionEvent var1) {
            CalculatorManager.getInstance().closeDialog();
            var3.execute();
         }
      };
      return var4;
   }

   public Component getTBItem(String var1) {
      return (Component)(this.items.containsKey(var1) ? (Component)this.items.get(var1) : new JButton());
   }

   public void setState(ICommandObject var1, boolean var2) {
      Enumeration var3 = this.comps.keys();

      while(var3.hasMoreElements()) {
         Component var4 = (Component)var3.nextElement();
         if (this.comps.get(var4).equals(var1)) {
            var4.setEnabled(var2);
         }
      }

   }

   public void setEnableAll(boolean var1) {
      Enumeration var2 = this.comps.keys();

      while(var2.hasMoreElements()) {
         Component var3 = (Component)var2.nextElement();
         var3.setEnabled(var1);
      }

   }

   public void setState(Integer var1) {
      Component var3;
      boolean var5;
      for(Enumeration var2 = this.comps.keys(); var2.hasMoreElements(); var3.setEnabled(var5)) {
         var3 = (Component)var2.nextElement();
         ICommandObject var4 = (ICommandObject)this.comps.get(var3);
         var5 = false;

         try {
            var5 = (Boolean)var4.getState(var1);
         } catch (Exception var7) {
         }
      }

   }
}
