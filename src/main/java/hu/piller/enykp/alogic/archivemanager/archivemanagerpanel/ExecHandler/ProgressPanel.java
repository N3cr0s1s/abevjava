package hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.ExecHandler;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;

public class ProgressPanel extends JPanel {
   public static final String COMPONENT_PROGRESS_ALL = "pb_all";
   public static final String COMPONENT_PROGRESS_RUN = "pb_run";
   transient JProgressBar pball = null;
   transient JProgressBar pbrun = null;
   JSeparator sep = new JSeparator(0);
   ThreadRunner ppb;

   public ProgressPanel() {
      this.createElements();
   }

   private void createElements() {
      this.setVisible(false);
      GridBagLayout var1 = new GridBagLayout();
      this.setLayout(var1);
      this.setBorder(BorderFactory.createEtchedBorder());
      GridBagConstraints var2 = new GridBagConstraints();
      var2.fill = 1;
      var2.gridy = 0;
      var2.ipady = 1;
      var2.ipadx = 1;
      var2.weighty = 0.8D;
      var2.weightx = 0.9D;
      var2.gridx = 0;
      this.add(this.getPbAll(var1, var2));
      var2.weightx = 0.1D;
      var2.gridx = 1;
      this.add(this.getPbRun(var1, var2));
   }

   private JProgressBar getPbAll(GridBagLayout var1, GridBagConstraints var2) {
      if (this.pball == null) {
         this.pball = new JProgressBar();
         this.pball.setForeground(Color.GRAY);
         this.pball.setStringPainted(true);
         this.pball.setString("");
         this.pball.setMinimumSize(new Dimension(100, 18));
         this.pball.setPreferredSize(new Dimension(Integer.MAX_VALUE, 18));
         var1.setConstraints(this.pball, var2);
      }

      return this.pball;
   }

   private JProgressBar getPbRun(GridBagLayout var1, GridBagConstraints var2) {
      if (this.pbrun == null) {
         this.pbrun = new JProgressBar();
         this.pbrun.setForeground(Color.GRAY);
         this.pbrun.setStringPainted(true);
         this.pbrun.setString("");
         this.pbrun.setMinimumSize(new Dimension(50, 18));
         this.pbrun.setPreferredSize(new Dimension(50, 18));
         var1.setConstraints(this.pbrun, var2);
      }

      return this.pbrun;
   }

   public JComponent getPBComponent(String var1) {
      if ("pb_all".equalsIgnoreCase(var1)) {
         return this.pball;
      } else {
         return "pb_run".equalsIgnoreCase(var1) ? this.pbrun : null;
      }
   }
}
