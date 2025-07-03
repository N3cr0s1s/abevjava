package hu.piller.enykp.alogic.upgrademanager_v2_0.gui;

import hu.piller.enykp.alogic.upgrademanager_v2_0.components.event.ComponentProcessingEvent;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.event.ComponentProcessingEventListener;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

public class JInstallProgressDialog extends JDialog implements ComponentProcessingEventListener {
   private UpgradeFormController controller;
   private JButton btn_abort;
   private JButton btn_close;
   private JButton btn_error;
   private JProgressBar progressBar;
   private JPanel pnlProgress;
   private JLabel lblProgress;
   private NumberFormat numberFormat;
   private JTable progressTable;
   private int counter;
   final boolean testMode = true;
   private StringBuilder sb = new StringBuilder();

   public JInstallProgressDialog(UpgradeFormController var1) {
      super(MainFrame.thisinstance);
      this.setTitle("Telepítés");
      this.setResizable(false);
      this.setModal(true);
      this.init();
      this.controller = var1;
   }

   public JButton getErrorButton() {
      if (this.btn_error == null) {
         this.btn_error = new JButton("Hiba");
         this.btn_error.setName("Hiba");
         this.btn_error.setMinimumSize(new Dimension(GuiUtil.getW(this.btn_error, this.btn_error.getText()), GuiUtil.getCommonItemHeight() + 2));
         this.btn_error.setPreferredSize(new Dimension(GuiUtil.getW(this.btn_error, this.btn_error.getText()), GuiUtil.getCommonItemHeight() + 2));
         this.btn_error.setMaximumSize(new Dimension(GuiUtil.getW(this.btn_error, this.btn_error.getText()), GuiUtil.getCommonItemHeight() + 2));
         this.btn_error.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                     JErrorLogDialog var1 = new JErrorLogDialog(JInstallProgressDialog.this.sb.toString());
                     Point var2 = JInstallProgressDialog.this.getLocation();
                     var1.setLocation(new Point(var2.x + 30, var2.y - 40));
                     var1.pack();
                     var1.setVisible(true);
                  }
               });
            }
         });
      }

      return this.btn_error;
   }

   public JButton getAbortButton() {
      if (this.btn_abort == null) {
         this.btn_abort = new JButton("Megszakít");
         this.btn_abort.setName("Megszakít");
         this.btn_abort.setMinimumSize(new Dimension(GuiUtil.getW(this.btn_abort, this.btn_abort.getText()), GuiUtil.getCommonItemHeight() + 2));
         this.btn_abort.setPreferredSize(new Dimension(GuiUtil.getW(this.btn_abort, this.btn_abort.getText()), GuiUtil.getCommonItemHeight() + 2));
         this.btn_abort.setMaximumSize(new Dimension(GuiUtil.getW(this.btn_abort, this.btn_abort.getText()), GuiUtil.getCommonItemHeight() + 2));
         this.btn_abort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                     JInstallProgressDialog.this.controller.interrupt();
                  }
               });
            }
         });
      }

      return this.btn_abort;
   }

   public JButton getCloseButton() {
      if (this.btn_close == null) {
         this.btn_close = new JButton("Bezár");
         this.btn_close.setName("Bezár");
         this.btn_close.setMinimumSize(new Dimension(GuiUtil.getW(this.btn_close, this.btn_close.getText()), GuiUtil.getCommonItemHeight() + 2));
         this.btn_close.setPreferredSize(new Dimension(GuiUtil.getW(this.btn_close, this.btn_close.getText()), GuiUtil.getCommonItemHeight() + 2));
         this.btn_close.setMaximumSize(new Dimension(GuiUtil.getW(this.btn_close, this.btn_close.getText()), GuiUtil.getCommonItemHeight() + 2));
         this.btn_close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                     JInstallProgressDialog.this.dispose();
                  }
               });
            }
         });
      }

      return this.btn_close;
   }

   private JTable getProgressTable() {
      if (this.progressTable == null) {
         this.progressTable = new JTable(new InstallProgressTableModel());
         GuiUtil.setTableColWidth(this.progressTable);
         if (GuiUtil.modGui()) {
            this.progressTable.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
         }
      }

      return this.progressTable;
   }

   private void init() {
      JPanel var1 = new JPanel();
      JScrollPane var2 = new JScrollPane(var1, 20, 30);
      var1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Frissítés monitor"));
      var1.setLayout(new BorderLayout());
      JTable var3 = this.getProgressTable();
      int var4 = GuiUtil.getTableWidthByColumns(var3);
      var1.add(this.getProgressTable(), "Center");
      var1.add(this.getProgressTable().getTableHeader(), "North");
      var1.setPreferredSize(new Dimension(Math.max(var4, GuiUtil.getScreenW() / 3), 12 * (GuiUtil.getCommonItemHeight() + 2)));
      this.setLayout(new GridBagLayout());
      GridBagConstraints var5 = new GridBagConstraints();
      var5.fill = 2;
      var5.ipadx = 0;
      var5.ipady = 0;
      var5.weightx = 0.0D;
      var5.weighty = 0.0D;
      var5.gridheight = 1;
      var5.gridx = 0;
      var5.gridy = 0;
      var5.gridwidth = 3;
      var5.fill = 3;
      var5.insets = new Insets(5, 5, 5, 5);
      this.getContentPane().add(this.getProgressBarPanel(), var5);
      var5.gridx = 0;
      var5.gridy = 1;
      var5.insets = new Insets(5, 5, 15, 5);
      this.getContentPane().add(var2, var5);
      var5.fill = 0;
      var5.gridx = 0;
      var5.gridy = 2;
      var5.gridwidth = 1;
      var5.weightx = 0.5D;
      var5.insets = new Insets(0, 0, 15, 0);
      this.getContentPane().add(this.getAbortButton(), var5);
      var5.gridx = 1;
      var5.gridy = 2;
      this.getContentPane().add(this.getErrorButton(), var5);
      var5.gridx = 2;
      var5.gridy = 2;
      this.getContentPane().add(this.getCloseButton(), var5);
   }

   public void componentProcessed(final ComponentProcessingEvent var1) {
      try {
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               InstallProgressTableModel var1x = (InstallProgressTableModel)JInstallProgressDialog.this.getProgressTable().getModel();
               if (var1.getState() != 0 && var1.getState() != 1) {
                  if (var1.getState() == 2) {
                     var1x.add(var1);
                  } else if (var1.getState() == 3) {
                     var1x.update(var1);
                  } else {
                     var1x.update(var1);
                  }
               } else {
                  if (var1.getState() == 1) {
                     JInstallProgressDialog.this.sb.append(JInstallProgressDialog.this.buildMsg(var1));
                     JInstallProgressDialog.this.btn_error.setBackground(Color.RED);
                  }

                  var1x.removeEvent(var1);
                  JInstallProgressDialog.this.getProgressBar().setValue(++JInstallProgressDialog.this.counter);
                  JInstallProgressDialog.this.getProgressText().setText(JInstallProgressDialog.this.getProgressPercentFormatted(JInstallProgressDialog.this.getProgressBar().getPercentComplete()));
               }

            }
         });
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   private String buildMsg(ComponentProcessingEvent var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append("Sikertelen telepítés: ");
      var2.append(var1.getOrganization());
      var2.append(", ");
      var2.append(var1.getName());
      var2.append(", ");
      var2.append(var1.getVersion());
      if (var1.getMessage() != null && !"".equals(var1.getMessage().trim())) {
         var2.append(" hiba [ ");
         var2.append(var1.getMessage());
         var2.append(" ]");
      }

      var2.append("\n");
      return var2.toString();
   }

   public JPanel getProgressBarPanel() {
      if (this.pnlProgress == null) {
         this.pnlProgress = new JPanel();
         this.pnlProgress.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "A telepítés előrehaladása"));
         int var1 = GuiUtil.getW("WWWA telepítés előrehaladásaWWW");
         this.pnlProgress.setMinimumSize(new Dimension((int)(1.5D * (double)var1), 3 * GuiUtil.getCommonItemHeight()));
         this.pnlProgress.setPreferredSize(this.pnlProgress.getMinimumSize());
         this.pnlProgress.setMaximumSize(this.pnlProgress.getMinimumSize());
         this.progressBar = this.getProgressBar();
         this.progressBar.setMinimumSize(new Dimension(1 * var1, GuiUtil.getCommonItemHeight() + 2));
         this.progressBar.setPreferredSize(this.progressBar.getMinimumSize());
         this.progressBar.setMaximumSize(this.progressBar.getMinimumSize());
         this.pnlProgress.setLayout(new BoxLayout(this.pnlProgress, 0));
         this.pnlProgress.add(Box.createHorizontalGlue());
         this.pnlProgress.add(this.progressBar);
         this.pnlProgress.add(Box.createHorizontalStrut(5));
         this.pnlProgress.add(this.getProgressText());
         this.pnlProgress.add(Box.createHorizontalGlue());
      }

      return this.pnlProgress;
   }

   public JProgressBar getProgressBar() {
      if (this.progressBar == null) {
         this.progressBar = new JProgressBar();
         this.progressBar.setIndeterminate(true);
      }

      return this.progressBar;
   }

   public JLabel getProgressText() {
      if (this.lblProgress == null) {
         this.lblProgress = new JLabel(this.getProgressPercentFormatted(0.0D));
      }

      return this.lblProgress;
   }

   private String getProgressPercentFormatted(double var1) {
      String var3 = "0";
      if (this.numberFormat == null) {
         this.numberFormat = NumberFormat.getPercentInstance(new Locale("hu", "HU"));
      } else {
         var3 = this.numberFormat.format(var1);
      }

      return var3;
   }
}
