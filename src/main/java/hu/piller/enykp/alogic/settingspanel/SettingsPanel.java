package hu.piller.enykp.alogic.settingspanel;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.util.base.Tools;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SettingsPanel extends JPanel {
   public static final String TAG_ABEV_FILEMASK = "file_maszk";
   public static final String TAG_ABEV_USE_FILEMASK = "file_maszk_hasznalva";
   public static final String TAG_ABEV_USE_ORIGINAL_FILENAME = "import_use_original_filename";
   private String file_mask;
   private JCheckBox chk_uff;
   private JCheckBox use_original_filename;
   private JPanel mask_panel;
   private JPanel balpanel;
   private JPanel gombpanel;
   private JPanel jobbpanel;
   private JList Lista1;
   private JList Lista2;
   private DefaultListModel modellbal;
   private DefaultListModel modelljobb;
   private JButton BalraButton;
   private JButton JobbraButton;
   private JButton FelButton;
   private JButton LeButton;
   private String[] mentbal = new String[20];
   private String[] mentjobb = new String[20];
   private String[] jobbmaszk = new String[20];
   private String[] defaultmaszk = new String[20];

   public SettingsPanel() {
      this.build();
      this.prepare();
   }

   private void build() {
      this.setLayout(new BoxLayout(this, 1));
      this.add(this.getFileNameSuggestionPanel());
      this.add(this.getFileMaskPanel());
      this.add(this.getUseOriginalNamePanel());
   }

   private JPanel getFileNameSuggestionPanel() {
      this.chk_uff = GuiUtil.getANYKCheckBox("Állomány név felajánlás használata");
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout(0, 10, 10));
      var1.add(this.chk_uff);
      return var1;
   }

   private JPanel getUseOriginalNamePanel() {
      this.use_original_filename = GuiUtil.getANYKCheckBox("Importnál megőrizzük az eredeti fájlnevet");
      this.use_original_filename.setToolTipText("A fájlnév kiegészül egy 15 jegyű számmal az egyediség biztosítása miatt");
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout(0, 10, 10));
      var1.add(this.use_original_filename);
      return var1;
   }

   private JPanel getFileMaskPanel() {
      Dimension var1 = GuiUtil.getSettingsDialogDimension();
      this.modellbal = new DefaultListModel();
      this.Lista1 = new JList(this.modellbal);
      this.Lista1.setName("lista1");
      this.balpanel = new JPanel();
      this.balpanel.setLayout(new BorderLayout());
      this.balpanel.add(new JLabel("Lehetséges értékek"), "North");
      this.BalraButton = this.getButton(" < ");
      this.JobbraButton = this.getButton(" > ");
      this.FelButton = this.getButton("Fel");
      this.LeButton = this.getButton("Le ");
      this.gombpanel = new JPanel();
      this.gombpanel.setAlignmentX(0.5F);
      this.gombpanel.add(this.BalraButton);
      this.gombpanel.add(this.JobbraButton);
      this.gombpanel.add(this.FelButton);
      this.gombpanel.add(this.LeButton);
      this.gombpanel.setPreferredSize(new Dimension(1 * GuiUtil.getW(this.FelButton, this.FelButton.getText() + 10), 6 * (GuiUtil.getCommonItemHeight() + 4)));
      this.gombpanel.setMinimumSize(new Dimension(1 * GuiUtil.getW(this.FelButton, this.FelButton.getText() + 10), 6 * (GuiUtil.getCommonItemHeight() + 4)));
      this.gombpanel.setSize(new Dimension(1 * GuiUtil.getW(this.FelButton, this.FelButton.getText() + 10), 6 * (GuiUtil.getCommonItemHeight() + 4)));
      JScrollPane var2 = new JScrollPane(this.Lista1);
      this.balpanel.add(var2, "Center");
      this.modelljobb = new DefaultListModel();
      this.Lista2 = new JList(this.modelljobb);
      this.Lista2.setName("lista2");
      this.jobbpanel = new JPanel();
      this.jobbpanel.setLayout(new BorderLayout());
      this.jobbpanel.add(new JLabel("File maszk"), "North");
      JScrollPane var3 = new JScrollPane(this.Lista2);
      this.jobbpanel.add(var3, "Center");
      this.balpanel.setPreferredSize(new Dimension(Math.max(255, (int)(var1.getWidth() - this.gombpanel.getPreferredSize().getWidth() - 20.0D) / 2), 200));
      this.balpanel.setMaximumSize(new Dimension(Math.max(255, (int)(var1.getWidth() - this.gombpanel.getPreferredSize().getWidth() - 20.0D) / 2), 200));
      this.balpanel.setSize(new Dimension(Math.max(255, (int)(var1.getWidth() - this.gombpanel.getPreferredSize().getWidth() - 20.0D) / 2), 200));
      this.jobbpanel.setPreferredSize(new Dimension(Math.max(255, (int)(var1.getWidth() - this.gombpanel.getPreferredSize().getWidth() - 20.0D) / 2), 200));
      this.jobbpanel.setMaximumSize(new Dimension(Math.max(255, (int)(var1.getWidth() - this.gombpanel.getPreferredSize().getWidth() - 20.0D) / 2), 200));
      this.jobbpanel.setSize(new Dimension(Math.max(255, (int)(var1.getWidth() - this.gombpanel.getPreferredSize().getWidth() - 20.0D) / 2), 200));
      this.mask_panel = new JPanel();
      this.mask_panel.setLayout(new BorderLayout());
      this.mask_panel.add(this.balpanel, "West");
      this.mask_panel.add(this.gombpanel, "Center");
      this.mask_panel.add(this.jobbpanel, "East");
      return this.mask_panel;
   }

   private JButton getButton(String var1) {
      JButton var2 = new JButton(var1);
      var2.setMinimumSize(new Dimension(GuiUtil.getW(var2, var2.getText()), GuiUtil.getCommonItemHeight() + 4));
      var2.setMaximumSize(new Dimension(GuiUtil.getW(var2, var2.getText()), GuiUtil.getCommonItemHeight() + 4));
      var2.setPreferredSize(new Dimension(GuiUtil.getW(var2, var2.getText()), GuiUtil.getCommonItemHeight() + 4));
      return var2;
   }

   private void prepare() {
      this.chk_uff.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (var1.getSource() instanceof JCheckBox) {
               JCheckBox var2 = (JCheckBox)var1.getSource();
               SettingsPanel.this.setEnabledListsEditor(var2.isSelected());
               SettingsPanel.this.save();
            }

         }
      });
      this.chk_uff.setSelected(false);
      this.use_original_filename.setSelected(false);
      this.use_original_filename.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (var1.getSource() instanceof JCheckBox) {
               SettingsPanel.this.save();
            }

         }
      });

      int var1;
      for(var1 = 0; var1 < 20; ++var1) {
         this.defaultmaszk[var1] = "";
      }

      this.defaultmaszk[0] = "nyomtatvány azonosító";
      this.defaultmaszk[1] = "adószám vagy adóazonosító jel";
      this.defaultmaszk[2] = "név (cégnév vagy személynév)";
      this.defaultmaszk[3] = "bevallási időszak kezdete";
      this.defaultmaszk[4] = "bevallási időszak vége";
      this.defaultmaszk[5] = "Hivatali kapu azonosító";

      for(var1 = 0; var1 < 20; ++var1) {
         this.mentbal[var1] = "";
         this.mentjobb[var1] = "";
         this.jobbmaszk[var1] = "";
      }

      if (this.modellbal.size() != 0) {
         this.modellbal.copyInto(this.mentbal);
      }

      if (this.modelljobb.size() != 0) {
         this.modelljobb.copyInto(this.mentjobb);
      }

      this.BalraButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            try {
               int var2 = SettingsPanel.this.Lista2.getSelectedIndex();
               if (var2 > -1) {
                  SettingsPanel.this.modellbal.addElement(SettingsPanel.this.Lista2.getSelectedValue());
                  SettingsPanel.this.modelljobb.remove(var2);
               }

               SettingsPanel.this.composeFileMask();
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

         }
      });
      this.JobbraButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            try {
               int var2 = SettingsPanel.this.Lista1.getSelectedIndex();
               if (var2 > -1) {
                  SettingsPanel.this.modelljobb.addElement(SettingsPanel.this.Lista1.getSelectedValue());
                  SettingsPanel.this.modellbal.remove(var2);
               }

               SettingsPanel.this.composeFileMask();
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

         }
      });
      this.FelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            try {
               int var2 = SettingsPanel.this.Lista2.getSelectedIndex();
               if (var2 > 0) {
                  SettingsPanel.this.modelljobb.insertElementAt(SettingsPanel.this.Lista2.getSelectedValue(), var2 - 1);
                  SettingsPanel.this.modelljobb.remove(var2 + 1);
                  SettingsPanel.this.Lista2.setSelectedIndex(var2 - 1);
               }

               SettingsPanel.this.composeFileMask();
            } catch (Exception var3) {
               Tools.eLog(var3, 0);
            }

         }
      });
      this.LeButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            try {
               int var2 = SettingsPanel.this.modelljobb.indexOf(SettingsPanel.this.modelljobb.lastElement());
               int var3 = SettingsPanel.this.Lista2.getSelectedIndex();
               if (var3 > -1 && var3 < var2) {
                  String var4 = SettingsPanel.this.Lista2.getSelectedValue().toString();
                  SettingsPanel.this.modelljobb.remove(var3);
                  SettingsPanel.this.modelljobb.insertElementAt(var4, var3 + 1);
                  SettingsPanel.this.Lista2.setSelectedIndex(var3 + 1);
               }

               SettingsPanel.this.composeFileMask();
            } catch (Exception var5) {
               Tools.eLog(var5, 0);
            }

         }
      });
      this.setFileMask("");
      this.setUseFileMask(false);
      this.load();
      this.fillModells();
   }

   private void composeFileMask() {
      String var1 = "";
      if (this.modelljobb.size() != 0) {
         int var2;
         for(var2 = 0; var2 < 20; ++var2) {
            this.jobbmaszk[var2] = "";
         }

         this.modelljobb.copyInto(this.jobbmaszk);

         for(var2 = 0; var2 < 20; ++var2) {
            if (!this.jobbmaszk[var2].equals("")) {
               var1 = var1 + this.jobbmaszk[var2] + "#";
            }
         }
      }

      this.setFileMask(var1);
      this.save();
   }

   private void fillModells() {
      String var1 = "";
      int var3;
      if (!this.getFileMask().equals("")) {
         for(var3 = 0; var3 < this.getFileMask().length(); ++var3) {
            String var2 = this.getFileMask().substring(var3, var3 + 1);
            if (var2.equals("#")) {
               this.modelljobb.addElement(var1);
               var1 = "";
            } else {
               var1 = var1 + var2;
            }
         }

         for(var3 = 0; var3 < 20; ++var3) {
            if (!this.defaultmaszk[var3].equals("") && this.getFileMask().indexOf(this.defaultmaszk[var3]) == -1) {
               this.modellbal.addElement(this.defaultmaszk[var3]);
            }
         }
      } else {
         for(var3 = 0; var3 < 20; ++var3) {
            if (!this.defaultmaszk[var3].equals("")) {
               this.modellbal.addElement(this.defaultmaszk[var3]);
            }
         }
      }

   }

   private void setEnabledListsEditor(boolean var1) {
      this.BalraButton.setEnabled(var1);
      this.JobbraButton.setEnabled(var1);
      this.FelButton.setEnabled(var1);
      this.LeButton.setEnabled(var1);
      this.Lista1.setEnabled(var1);
      this.Lista2.setEnabled(var1);
   }

   public void setFileMask(String var1) {
      this.file_mask = var1 == null ? "" : var1;
   }

   public String getFileMask() {
      return this.file_mask;
   }

   public void setUseFileMask(boolean var1) {
      this.chk_uff.setSelected(var1);
      this.setEnabledListsEditor(var1);
   }

   public void setUseOriginalFilename(boolean var1) {
      this.use_original_filename.setSelected(var1);
   }

   public boolean isUseFileMask() {
      return this.chk_uff.isSelected();
   }

   public boolean isUseOriginalFilename() {
      return this.use_original_filename.isSelected();
   }

   public static Component getFirstRoot(Component var0) {
      Object var1 = null;
      if (var0 != null) {
         for(Object var2 = var0; var2 != null; var2 = ((Component)var2).getParent()) {
            if (var2 instanceof JFrame) {
               return (Component)var2;
            }

            if (var2 instanceof JDialog) {
               return (Component)var2;
            }
         }
      }

      return (Component)var1;
   }

   public void save() {
      SettingsStore var1 = SettingsStore.getInstance();
      String var2;
      var1.set("file_maszk", "file_maszk", (var2 = this.getFileMask()) == null ? "" : var2);
      var1.set("file_maszk", "file_maszk_hasznalva", this.isUseFileMask() ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
      var1.set("file_maszk", "import_use_original_filename", this.isUseOriginalFilename() ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
      var1.save();
   }

   public void load() {
      SettingsStore var1 = SettingsStore.getInstance();
      this.setFileMask(var1.get("file_maszk", "file_maszk"));
      this.setUseFileMask(Boolean.valueOf(var1.get("file_maszk", "file_maszk_hasznalva")));
      this.setUseOriginalFilename(Boolean.valueOf(var1.get("file_maszk", "import_use_original_filename")));
   }
}
