package hu.piller.enykp.util.base.messageinfo;

import hu.piller.enykp.gui.GuiUtil;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class MessageInfo {
   private static MessageInfo instance;
   private MessageInfoPanel mi_panel;
   private JDialog mi_dialog;

   public static MessageInfo getInstance() {
      if (instance == null) {
         instance = new MessageInfo();
      }

      return instance;
   }

   public MessageInfoPanel getPanel() {
      if (this.mi_panel == null) {
         this.mi_panel = new MessageInfoPanel();
      }

      return this.mi_panel;
   }

   public void openDialog(JFrame var1) {
      if (this.mi_dialog == null) {
         this.mi_dialog = this.getDialog(var1);
         this.mi_dialog.setTitle("Üzenetek ...");
         this.mi_dialog.setSize(GuiUtil.getScreenW() / 2, GuiUtil.getScreenH() / 2);
         this.mi_dialog.setModal(true);
      }

      this.fill();
      this.mi_dialog.setLocationRelativeTo(var1);
      this.mi_dialog.setVisible(true);
   }

   public void closeDialog() {
      if (this.mi_dialog != null) {
         this.mi_dialog.setVisible(false);
      }

   }

   public void releaseDialog() {
      if (this.mi_dialog != null) {
         this.mi_dialog.dispose();
         this.mi_dialog = null;
      }

   }

   public JDialog getDialog(JFrame var1) {
      if (this.mi_dialog == null) {
         this.mi_dialog = new JDialog(var1);
         this.mi_dialog.getContentPane().add(this.getPanel());
         this.mi_dialog.setMinimumSize(new Dimension(600, 15 * GuiUtil.getCommonItemHeight()));
      }

      return this.mi_dialog;
   }

   public void clear() {
      this.getPanel().getBusiness().clearMessages(false);
   }

   public void install() {
      this.getPanel().getBusiness().installEventListener();
   }

   public void uninstall() {
      this.getPanel().getBusiness().uninstallEventListener();
   }

   public void fill() {
      this.getPanel().getBusiness().fillIdTable();
   }
}
