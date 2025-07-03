package hu.piller.enykp.gui.framework;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.viewer.DefaultMultiFormViewer;
import hu.piller.enykp.util.base.Tools;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class FlowcontrolPane extends JPanel {
   private CenterPane centerpanel;
   private Hashtable items;
   private Hashtable items2;
   private Hashtable itemspos;
   private String currentflow;
   private final int dividersize = 10;

   public FlowcontrolPane(CenterPane var1) {
      this.centerpanel = var1;
      this.setLayout(new BoxLayout(this, 1));
      this.setBorder(BorderFactory.createEtchedBorder());
      this.items = new Hashtable();
      this.items2 = new Hashtable();
      this.itemspos = new Hashtable();
      this.currentflow = null;
   }

   public synchronized void addFlowItem(String var1, Icon var2, final JPanel var3) {
      try {
         this.items.put(var1, var3);
         this.itemspos.put(var1, new Integer(400));
         final VerticalButton var4 = new VerticalButton(var1, var2);
         var4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               if (FlowcontrolPane.this.centerpanel.getRightComponent() != null && FlowcontrolPane.this.currentflow != null) {
                  FlowcontrolPane.this.changeddivider();
               }

               if (FlowcontrolPane.this.centerpanel.getRightComponent() != null && ((FlowcontrolPane.TrayPanel)FlowcontrolPane.this.centerpanel.getRightComponent()).panel == var3) {
                  FlowcontrolPane.this.centerpanel.setRightComponent((Component)null);
                  FlowcontrolPane.this.centerpanel.setDividerSize(0);
                  FlowcontrolPane.this.currentflow = null;
                  Thread var2 = new Thread(new Runnable() {
                     public void run() {
                        try {
                           ((DefaultMultiFormViewer)MainFrame.thisinstance.mp.leftcomp).fv.pv.pv.requestFocus();
                        } catch (Exception var2) {
                           Tools.eLog(var2, 0);
                        }

                     }
                  });
                  var2.start();
               } else {
                  FlowcontrolPane.this.centerpanel.setRightComponent(FlowcontrolPane.this.new TrayPanel(var3, var4));
                  FlowcontrolPane.this.centerpanel.setDividerSize(10);
                  FlowcontrolPane.this.currentflow = var4.getTText();
                  FlowcontrolPane.this.centerpanel.setDividerLocation(FlowcontrolPane.this.centerpanel.getWidth() - (Integer)FlowcontrolPane.this.itemspos.get(FlowcontrolPane.this.currentflow));
               }
            }
         });
         var4.setPreferredSize(new Dimension(GuiUtil.getCommonItemHeight() + 6, var4.getHeight() + GuiUtil.getCommonItemHeight()));
         var4.setBorder(new EmptyBorder(10, 10, 10, 10));
         this.items2.put(var3, var4);
         this.add(var4);
      } catch (Exception var5) {
      }

   }

   public void showpanel(JPanel var1) {
      VerticalButton var2 = (VerticalButton)this.items2.get(var1);
      if (var2 != null) {
         var2.doClick();
      }

   }

   public void showpanel(String var1) {
      if (this.currentflow == null || !this.currentflow.equals(var1)) {
         JPanel var2 = (JPanel)this.items.get(var1);
         if (var2 != null) {
            VerticalButton var3 = (VerticalButton)this.items2.get(var2);
            if (this.currentflow == null) {
               this.currentflow = var3.getTText();
            }

            if (var3 != null) {
               var3.doClick();
            }

         }
      }
   }

   public void closepanel() {
      if (this.currentflow != null) {
         try {
            JPanel var1 = (JPanel)this.items.get(this.currentflow);
            VerticalButton var2 = (VerticalButton)this.items2.get(var1);
            if (var2 != null) {
               var2.doClick();
            }
         } catch (Exception var3) {
         }

      }
   }

   public void changeddivider() {
      if (this.currentflow != null) {
         this.itemspos.put(this.currentflow, new Integer(this.centerpanel.getWidth() - this.centerpanel.getDividerLocation()));
      }

   }

   public String getCurrentflow() {
      return this.currentflow;
   }

   class TrayPanel extends JPanel {
      JPanel panel;

      public TrayPanel(JPanel var2, final VerticalButton var3) {
         this.panel = var2;
         this.setName(var3.getTText());
         this.setLayout(new BorderLayout());
         this.add(var2, "Center");
         JPanel var4 = new JPanel();
         var4.setLayout(new BoxLayout(var4, 0));
         var4.setBackground(GuiUtil.getModifiedBGColor());
         JButton var5 = new JButton("x");
         var5.setMargin(new Insets(0, 0, 0, 0));
         var5.setSize(new Dimension(GuiUtil.getW("-"), GuiUtil.getCommonItemHeight() + 2));
         var5.setMinimumSize(var5.getSize());
         var5.setMaximumSize(var5.getSize());
         var5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               var3.doClick();
            }
         });
         JButton var6 = new JButton("_");
         var6.setMargin(new Insets(0, 0, 0, 0));
         var6.setMinimumSize(var5.getSize());
         var6.setSize(var5.getSize());
         var6.setMaximumSize(var5.getSize());
         var6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               if (FlowcontrolPane.this.centerpanel.getDividerLocation() < 3) {
                  FlowcontrolPane.this.centerpanel.setDividerLocation(FlowcontrolPane.this.centerpanel.getWidth() - (Integer)FlowcontrolPane.this.itemspos.get(FlowcontrolPane.this.currentflow));
               } else {
                  FlowcontrolPane.this.itemspos.put(FlowcontrolPane.this.currentflow, new Integer(FlowcontrolPane.this.centerpanel.getWidth() - FlowcontrolPane.this.centerpanel.getDividerLocation()));
                  FlowcontrolPane.this.centerpanel.setDividerLocation(0);
               }

            }
         });
         JLabel var7 = new JLabel(var3.getTText());
         var7.setForeground(GuiUtil.getHighLightTextColor());
         var4.add(var7);
         var4.add(Box.createHorizontalGlue());
         var4.add(var6);
         var4.add(var5);
         this.add(var4, "North");
      }
   }
}
