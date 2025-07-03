package hu.piller.enykp.alogic.filepanels;

import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.base.PropertyList;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ABEVSaveAsPanel {
   static Hashtable ht;
   static JDialog d;

   public static Hashtable showDialog(String var0) {
      ht = new Hashtable();
      d = new JDialog(MainFrame.thisinstance, true);
      d.setTitle("Mentés másként");
      d.setContentPane(makecontent(var0));
      d.setSize(590, 400);
      d.setLocationRelativeTo(MainFrame.thisinstance);
      d.setVisible(true);
      return ht;
   }

   private static JPanel makecontent(String var0) {
      JPanel var1 = new JPanel(new BorderLayout());
      final MultiDirChooserPanel var2 = new MultiDirChooserPanel();
      var1.add(var2);
      JPanel var3 = new JPanel((LayoutManager)null);
      var3.setPreferredSize(new Dimension(1, 120));
      JLabel var4 = new JLabel("A mentendő nyomtatvány állomány új neve:");
      var4.setBounds(10, 10, 400, 20);
      var3.add(var4);
      final JTextField var5 = new JTextField(var0);
      var5.setBounds(20, 60, 550, 20);
      var3.add(var5);
      JButton var6 = new JButton("Mentés");
      var6.setBounds(20, 90, 100, 20);
      var6.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ABEVSaveAsPanel.ht.put("fn", var5.getText());

            try {
               ABEVSaveAsPanel.ht.put("path", var2.getSelectedDir());
            } catch (Exception var3) {
            }

            ABEVSaveAsPanel.d.setVisible(false);
         }
      });
      var3.add(var6);
      JButton var7 = new JButton("Mégsem");
      var7.setBounds(150, 90, 100, 20);
      var3.add(var7);
      var7.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ABEVSaveAsPanel.d.setVisible(false);
         }
      });
      var1.add(var3, "South");
      File var8 = new File(PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.saves"));
      var2.setRootdir(var8);
      return var1;
   }
}
