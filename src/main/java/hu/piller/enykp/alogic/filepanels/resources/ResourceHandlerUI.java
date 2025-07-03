package hu.piller.enykp.alogic.filepanels.resources;

import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.TableFilterPanel;
import hu.piller.enykp.alogic.orghandler.OrgHandler;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.orghandler.OrgResource;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.SwingWorker;
import me.necrocore.abevjava.NecroFile;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class ResourceHandlerUI extends JDialog {
   private JPanel pnlResource;
   private JPanel pnlButtons;
   private JPanel pnlText;
   private JButton btnDelete;
   private JButton btnDeleteAll;
   private JButton btnClose;
   private JTable tblResources;
   private TableFilterPanel tblFilter;
   ResourceHandlerUI thisinstance = this;

   public ResourceHandlerUI() {
      this.setTitle("Paraméterállomány karbantartó");
      this.setResizable(false);
      this.setModal(true);
      this.setSize(710, 460);
      if (this.getOwner() != null) {
         this.setLocationRelativeTo(this.getOwner());
      }

      this.setLayout(new GridBagLayout());
      GridBagConstraints var1 = new GridBagConstraints();
      var1.gridx = 0;
      var1.gridy = 0;
      this.add(this.getPnlResource(), var1);
      var1.gridy = 1;
      this.add(this.getPnlButtons(), var1);
   }

   public void fillContent() {
      int var1 = this.getTblResources().getModel().getRowCount();

      for(int var2 = var1 - 1; var2 >= 0; --var2) {
         ((DefaultTableModel)this.getTblResources().getModel()).removeRow(var2);
      }

      Set var7 = OrgInfo.getInstance().readAllResourceFiles();
      Iterator var3 = var7.iterator();

      while(var3.hasNext()) {
         OrgResource var4 = (OrgResource)var3.next();
         String[] var5 = new String[]{var4.getOrgId(), var4.getVersion(), var4.getOrgname(), null};
         String[] var6 = var4.getPath().split("\\\\");
         var5[3] = var6[var6.length - 1];
         ((DefaultTableModel)this.getTblResources().getModel()).addRow(var5);
      }

   }

   private JPanel getPnlText() {
      if (this.pnlText == null) {
         this.pnlText = new JPanel();
         this.pnlText.setLayout(new BoxLayout(this.pnlText, 0));
         this.pnlText.setMinimumSize(new Dimension(710, 25));
         this.pnlText.setPreferredSize(new Dimension(710, 25));
         this.pnlText.setMaximumSize(new Dimension(710, 25));
         JLabel var1 = new JLabel("Paraméterállományok");
         var1.setFont(((JLabel)this.tblFilter.getComponent("filter_title_lbl")).getFont());
         this.pnlText.add(var1);
      }

      return this.pnlText;
   }

   private TableFilterPanel getTblFilter() {
      if (this.tblFilter == null) {
         this.tblFilter = new TableFilterPanel((JTable)null);
         this.tblFilter.getBusinessHandler().clearFilters();
         this.tblFilter.getBusinessHandler().setFilterVisibility(true);
         this.tblFilter.getBusinessHandler().setVisible(false);
         ((JLabel)this.tblFilter.getComponent("filter_title_lbl")).setText("Szűrési feltételek");
         Vector var1 = new Vector();
         var1.add(0);
         var1.add(1);
         var1.add(2);
         var1.add(3);
         Vector var2 = new Vector();
         var2.add(0);
         var2.add(0);
         var2.add(1);
         var2.add(0);
         this.tblFilter.getBusinessHandler().initials(new Object[]{this.getTblResources(), var1, var1.size(), var2});
      }

      return this.tblFilter;
   }

   private JPanel getPnlResource() {
      if (this.pnlResource == null) {
         this.pnlResource = new JPanel();
         this.pnlResource.setMinimumSize(new Dimension(700, 270));
         this.pnlResource.setPreferredSize(new Dimension(700, 270));
         this.pnlResource.setLayout(new GridBagLayout());
         GridBagConstraints var1 = new GridBagConstraints();
         var1.anchor = 23;
         var1.fill = 2;
         var1.gridwidth = 1;
         var1.gridheight = 1;
         var1.weightx = 1.0D;
         var1.gridx = 0;
         var1.gridy = 0;
         this.pnlResource.add(this.getTblFilter(), var1);
         var1.gridy = 1;
         this.pnlResource.add(this.getPnlText(), var1);
         var1.gridy = 2;
         var1.weighty = 1.0D;
         var1.fill = 1;
         this.pnlResource.add(new JScrollPane(this.getTblResources()), var1);
      }

      return this.pnlResource;
   }

   private JTable getTblResources() {
      if (this.tblResources == null) {
         DefaultTableModel var1 = new DefaultTableModel((Object[][])null, new String[]{"Szervezet", "Verzió", "Szervezet neve", "Állomány neve"});
         this.tblResources = new JTable(var1);
         this.tblResources.setSelectionMode(0);
         this.tblResources.setAutoCreateColumnsFromModel(false);
         this.tblResources.getColumnModel().getColumn(0).setMaxWidth(100);
         this.tblResources.getColumnModel().getColumn(1).setMaxWidth(60);
         this.tblResources.getColumnModel().getColumn(2).setMaxWidth(300);
         this.tblResources.getColumnModel().getColumn(3).setMaxWidth(250);
      }

      return this.tblResources;
   }

   private JPanel getPnlButtons() {
      if (this.pnlButtons == null) {
         this.pnlButtons = new JPanel();
         this.pnlButtons.setMinimumSize(new Dimension(700, 30));
         this.pnlButtons.setPreferredSize(new Dimension(700, 30));
         this.pnlButtons.setLayout(new BoxLayout(this.pnlButtons, 0));
         this.pnlButtons.add(Box.createHorizontalGlue());
         this.pnlButtons.add(this.getBtnDelete());
         this.pnlButtons.add(Box.createHorizontalStrut(3));
         this.pnlButtons.add(this.getBtnDeleteAll());
         this.pnlButtons.add(Box.createHorizontalStrut(3));
         this.pnlButtons.add(this.getBtnClose());
         this.pnlButtons.add(Box.createHorizontalGlue());
      }

      return this.pnlButtons;
   }

   private JButton getBtnDelete() {
      if (this.btnDelete == null) {
         this.btnDelete = new JButton();
         this.btnDelete.setText("Töröl");
         this.btnDelete.setMinimumSize(new Dimension(80, 25));
         this.btnDelete.setPreferredSize(new Dimension(80, 25));
         this.btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               final int var2 = ResourceHandlerUI.this.tblResources.getSelectedRow();
               if (var2 != -1) {
                  ResourceHandlerUI.this.enableDisableButtons(false);
                  SwingWorker var3 = new SwingWorker() {
                     public Object construct() {
                        String var1 = "";
                        String var2x = (String)ResourceHandlerUI.this.tblResources.getModel().getValueAt(var2, 0);
                        if (OrgHandler.getInstance().isNotGeneralOrg(var2x)) {
                           var1 = "Ezen szervezet paraméter állományainak a törlése nem engedélyezett!";
                        } else {
                           int var3 = JOptionPane.showConfirmDialog(ResourceHandlerUI.this.thisinstance, "Valóban törölni akarja az állományt?", "Megerősítés", 0);
                           if (var3 == 0) {
                              String var4 = OrgInfo.getInstance().getResourcePath() + File.separator + ResourceHandlerUI.this.tblResources.getModel().getValueAt(var2, 3);
                              if (!(new NecroFile(var4)).delete()) {
                                 var1 = "A paraméter állomány törlése sikertelen!";
                              }

                              OrgInfo.getInstance().mountDir(OrgInfo.getInstance().getResourcePath());
                           }
                        }

                        return var1;
                     }

                     public void finished() {
                        if (!"".equals(this.get())) {
                           GuiUtil.showMessageDialog(ResourceHandlerUI.this.thisinstance, this.get(), "Hiba", 0);
                        }

                        SwingUtilities.invokeLater(new Runnable() {
                           public void run() {
                              ResourceHandlerUI.this.fillContent();
                              ResourceHandlerUI.this.enableDisableButtons(true);
                           }
                        });
                     }
                  };
                  var3.start();
               }
            }
         });
      }

      return this.btnDelete;
   }

   private JButton getBtnDeleteAll() {
      if (this.btnDeleteAll == null) {
         this.btnDeleteAll = new JButton();
         this.btnDeleteAll.setText("Összes verzió törlése");
         this.btnDeleteAll.setMinimumSize(new Dimension(180, 25));
         this.btnDeleteAll.setPreferredSize(new Dimension(180, 25));
         this.btnDeleteAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               final int var2 = ResourceHandlerUI.this.tblResources.getSelectedRow();
               if (var2 != -1) {
                  ResourceHandlerUI.this.enableDisableButtons(false);
                  SwingWorker var3 = new SwingWorker() {
                     public Object construct() {
                        String var1 = "";
                        String var2x = (String)ResourceHandlerUI.this.tblResources.getModel().getValueAt(var2, 0);
                        if (OrgHandler.getInstance().isNotGeneralOrg(var2x)) {
                           var1 = "Ezen szervezet paraméter állományainak a törlése nem engedélyezett!";
                        } else {
                           int var3 = JOptionPane.showConfirmDialog(ResourceHandlerUI.this.thisinstance, "Valóban törölni akarja az állományokat?", "Megerősítés", 0);
                           if (var3 == 0) {
                              for(int var4 = 0; var4 < ResourceHandlerUI.this.tblResources.getModel().getRowCount(); ++var4) {
                                 if (var2x.equals(ResourceHandlerUI.this.tblResources.getModel().getValueAt(var4, 0))) {
                                    String var5 = OrgInfo.getInstance().getResourcePath() + File.separator + ResourceHandlerUI.this.tblResources.getModel().getValueAt(var4, 3);
                                    if (!(new NecroFile(var5)).delete()) {
                                       StringBuilder var6 = new StringBuilder();
                                       var6.append((String)ResourceHandlerUI.this.tblResources.getModel().getValueAt(var2, 0)).append(" (").append((String)ResourceHandlerUI.this.tblResources.getModel().getValueAt(var2, 2)).append(") ").append((String)ResourceHandlerUI.this.tblResources.getModel().getValueAt(var2, 1)).append(": ").append((String)ResourceHandlerUI.this.tblResources.getModel().getValueAt(var2, 3));
                                       ErrorList.getInstance().store(ErrorList.LEVEL_SHOW_WARNING, var6.toString(), (Exception)null, (Object)null);
                                       if ("".equals(var1)) {
                                          var1 = "Egy vagy több állomány törlése sikertelen! Részletek a Szerviz->Üzenetek menüben.";
                                       }
                                    }
                                 }
                              }

                              OrgInfo.getInstance().mountDir(OrgInfo.getInstance().getResourcePath());
                           }
                        }

                        return var1;
                     }

                     public void finished() {
                        if (!"".equals(this.get())) {
                           GuiUtil.showMessageDialog(ResourceHandlerUI.this.thisinstance, this.get(), "Hiba", 0);
                        }

                        SwingUtilities.invokeLater(new Runnable() {
                           public void run() {
                              ResourceHandlerUI.this.fillContent();
                              ResourceHandlerUI.this.enableDisableButtons(true);
                           }
                        });
                     }
                  };
                  var3.start();
               }
            }
         });
      }

      return this.btnDeleteAll;
   }

   private JButton getBtnClose() {
      if (this.btnClose == null) {
         this.btnClose = new JButton();
         this.btnClose.setText("Bezár");
         this.btnClose.setMinimumSize(new Dimension(80, 25));
         this.btnClose.setPreferredSize(new Dimension(80, 25));
         this.btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               ResourceHandlerUI.this.dispose();
            }
         });
      }

      return this.btnClose;
   }

   private void enableDisableButtons(boolean var1) {
      this.btnDelete.setEnabled(var1);
      this.btnDeleteAll.setEnabled(var1);
      this.btnClose.setEnabled(var1);
   }
}
