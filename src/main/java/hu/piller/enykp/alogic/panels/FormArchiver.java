package hu.piller.enykp.alogic.panels;

import hu.piller.enykp.alogic.filepanels.filepanel.FileBusiness;
import hu.piller.enykp.alogic.filepanels.filepanel.FilePanel;
import hu.piller.enykp.alogic.filepanels.filepanel.ListItem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.ICommandObject;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.Version;
import hu.piller.enykp.util.base.eventsupport.CloseEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

public class FormArchiver implements ICommandObject {
   private static FormArchiver instance;
   private String[] filters = new String[]{"template_loader_v1"};
   public static final String FORM_NAME = "FormArchiver";
   private final Object[] update_skin = new Object[]{"work_panel", "static", "Telepített sablonok", "tab_close", null};
   private FilePanel file_panel;
   private FileBusiness business;
   private JDialog dlg;
   private JButton ok;
   private JButton stop;
   private JButton exit;
   private JComboBox combo;
   private JProgressBar pb;
   private FormArchiver.ButtonActions be;
   private int db;
   private boolean need_stop;
   private boolean ready;
   private boolean user_cancelled;
   private final Vector cmd_list = new Vector(Arrays.asList("abev.showArchiverTemplatesDialog"));
   Boolean[] states;

   private Vector getfilteredlist(Vector var1) {
      Vector var2 = new Vector();

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         try {
            ListItem var4 = (ListItem)var1.get(var3);
            Hashtable var5 = (Hashtable)var4.getText();
            var5 = (Hashtable)var5.get("docinfo");
            String var6 = (String)var5.get("org");
            String var7 = (String)var5.get("name");
            String var8 = (String)var5.get("id");
            String var9 = (String)var5.get("ver");
            boolean var10 = true;

            for(int var11 = 0; var11 < var1.size(); ++var11) {
               if (var11 != var3) {
                  ListItem var12 = (ListItem)var1.get(var11);
                  Hashtable var13 = (Hashtable)var12.getText();
                  var13 = (Hashtable)var13.get("docinfo");
                  String var14 = (String)var13.get("org");
                  String var15 = (String)var13.get("name");
                  String var16 = (String)var13.get("id");
                  String var17 = (String)var13.get("ver");
                  if (var6.equals(var14) && var8.equals(var16) && var7.equals(var15)) {
                     int var18 = (new Version(var17)).compareTo(new Version(var9));
                     if (0 < var18) {
                        var10 = false;
                        break;
                     }
                  }
               }
            }

            if (var10) {
               var2.add(var4);
            }
         } catch (Exception var19) {
            Tools.eLog(var19, 0);
         }
      }

      return var2;
   }

   private FormArchiver() {
      this.states = new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
      this.build();
      this.prepare();
   }

   private void build() {
      this.file_panel = new FilePanel();
      this.business = this.file_panel.getBusiness();
      this.business.setTask(15);
   }

   private void prepare() {
      this.be = new FormArchiver.ButtonActions(this.file_panel);
      this.business.setButtonExecutor(this.be);
      this.business.setSelectedPath(new File(this.getProperty("prop.dynamic.templates.absolutepath")));
      this.update_skin[4] = this.file_panel;
   }

   public static FormArchiver getInstance() {
      if (instance == null) {
         instance = new FormArchiver();
      }

      return instance;
   }

   public void execute() {
      this.build();
      this.prepare();
      this.dlg = new JDialog(MainFrame.thisinstance);
      boolean var1 = true;
      Container var2 = this.dlg.getContentPane();
      JPanel var3 = new JPanel(new BorderLayout());
      var3.add(this.file_panel, "Center");
      Object[] var4 = new Object[]{"Az összes nyomtatvány verzióinak archiválása a legutolsó kivételével", "Az összes nyomtatvány összes verziójának archiválása", "A kiválasztott nyomtatvány verzióinak archiválása a legutolsó kivételével", "A kiválasztott nyomtatvány összes verziójának archiválása", "A kiválasztott nyomtatvány archiválása"};
      this.combo = new JComboBox(var4);
      GuiUtil.setDynamicBound(this.combo, "A kiválasztott nyomtatvány verzióinak archiválása a legutolsó kivételévelWWWWW", 10, 30);
      this.combo.setBackground(Color.WHITE);
      JPanel var5 = new JPanel(new BorderLayout());
      var5.setBorder(new TitledBorder("Funkció választás"));
      var5.setPreferredSize(new Dimension(800, 4 * (GuiUtil.getCommonItemHeight() + 4)));
      var5.setMinimumSize(new Dimension(800, 4 * (GuiUtil.getCommonItemHeight() + 4)));
      var5.setSize(new Dimension(800, 4 * (GuiUtil.getCommonItemHeight() + 4)));
      var5.add(this.combo, "Center");
      this.ok = new JButton("Indítás");
      this.ok.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            FormArchiver.this.done_ok();
         }
      });
      this.stop = new JButton("Leállítás");
      this.stop.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            FormArchiver.this.user_cancelled = true;
            FormArchiver.this.done_stop();
         }
      });
      this.exit = new JButton("Kilépés");
      this.exit.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            FormArchiver.this.done_exit();
         }
      });
      this.stop.setEnabled(false);
      JPanel var6 = new JPanel();
      var6.add(this.ok);
      var6.add(this.stop);
      var6.add(this.exit);
      var6.setBounds(480, 60, 250, 35);
      var5.add(var6);
      this.pb = new JProgressBar();
      this.pb.setPreferredSize(new Dimension(800, 25));
      JPanel var7 = new JPanel(new BorderLayout());
      var7.add(var5, "North");
      var7.add(var6, "East");
      var7.add(this.pb, "South");
      var3.add(var7, "South");
      this.business.addFilters(this.filters, (String[])null);
      this.business.rescan();
      this.file_panel.getBusiness().loadFilterSettings("FormArchiver");
      if (var1) {
         this.file_panel.invalidate();
         this.file_panel.setVisible(true);
         var2.add(var3);
         this.dlg.setTitle("Nyomtatvány sablonok archiválása");

         try {
            this.dlg.setSize((int)((double)GuiUtil.getScreenW() * 0.6D), (int)((double)GuiUtil.getScreenH() * 0.8D));
         } catch (Exception var9) {
            this.dlg.setSize(708, 520);
         }

         this.dlg.setResizable(true);
         this.dlg.setLocationRelativeTo(MainFrame.thisinstance);
         this.dlg.setModal(true);
         this.dlg.setVisible(true);
      }

   }

   private void done_ok() {
      this.need_stop = false;
      this.user_cancelled = false;
      this.ready = false;
      this.ok.setEnabled(false);
      this.exit.setEnabled(false);
      if (this.check_dir()) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az archívum könyvtárat nem sikerült létrehozni!", "Üzenet", 0);
      } else {
         int var1 = this.combo.getSelectedIndex();
         boolean var2;
         switch(var1) {
         case 0:
            var2 = this.done_0();
            if (var2) {
               this.ok.setEnabled(true);
               this.exit.setEnabled(true);
               return;
            }
            break;
         case 1:
            var2 = this.done_1();
            if (var2) {
               this.ok.setEnabled(true);
               this.exit.setEnabled(true);
               return;
            }
            break;
         case 2:
            var2 = this.done_2();
            if (var2) {
               this.ok.setEnabled(true);
               this.exit.setEnabled(true);
               return;
            }
            break;
         case 3:
            var2 = this.done_3();
            if (var2) {
               this.ok.setEnabled(true);
               this.exit.setEnabled(true);
               return;
            }
            break;
         case 4:
            this.be.b22PathClicked();
            this.ok.setEnabled(true);
            this.exit.setEnabled(true);
            return;
         }

         if (!this.ready) {
            this.pb.setIndeterminate(true);
            this.stop.setEnabled(true);
         }
      }
   }

   private boolean done_0() {
      final Vector var1 = this.business.getVct_files();
      final Vector var2 = this.getfilteredlist(var1);
      int var3 = var1.size() - var2.size();
      if (var3 == 0) {
         String var7 = "Az összes nyomtatványából csak az utolsó verzió van telepítve.\n0 db az archiválandó állományok száma.";
         GuiUtil.showMessageDialog(MainFrame.thisinstance, var7, "Üzenet", 1);
         return true;
      } else {
         Object[] var4 = new Object[]{"Tovább", "Mégsem"};
         int var5 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Az összes nyomtatványsablon kivéve az utolsó verziók archiválásra kerül!\n" + var3 + " db. állomány", "Nyomtatványok archiválása", 0, 3, (Icon)null, var4, var4[0]);
         if (var5 != 0) {
            return true;
         } else {
            this.db = 0;
            this.pb.setString("Archiválás folyamatban!");
            this.pb.setStringPainted(true);
            Thread var6 = new Thread(new Runnable() {
               public void run() {
                  int var1x = 0;
                  File var2x = new File((String)PropertyList.getInstance().get("prop.sys.root"), "nyomtatvanyok_archivum");

                  for(int var3 = 0; var3 < var1.size(); ++var3) {
                     try {
                        if (FormArchiver.this.need_stop) {
                           break;
                        }

                        ListItem var4 = (ListItem)var1.get(var3);
                        if (!var2.contains(var4)) {
                           ++var1x;
                           File var5 = (File)var4.getItem();
                           boolean var6 = Tools.zipFile(var5.getAbsolutePath(), var2x.getAbsolutePath(), ".tem_enyk_zip") == 0;
                           if (var6) {
                              var5.delete();
                              FormArchiver.this.db++;
                           }

                           FormArchiver.this.pb.setString(var1x + " / " + var2.size());
                        }
                     } catch (Exception var9) {
                        Tools.eLog(var9, 0);
                     }
                  }

                  try {
                     SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                           FormArchiver.this.business.rescan();
                        }
                     });
                  } catch (InterruptedException var7) {
                     var7.printStackTrace();
                  } catch (InvocationTargetException var8) {
                     var8.printStackTrace();
                  }

                  FormArchiver.this.pb.setStringPainted(false);
                  String var10 = FormArchiver.this.db + " darab állomány archiválva!";
                  if (FormArchiver.this.user_cancelled) {
                     var10 = "Felhasználói megszakítás!\n" + var10;
                  }

                  FormArchiver.this.done_stop();
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, var10, "Üzenet", 1);
               }
            });
            var6.start();
            return false;
         }
      }
   }

   private boolean done_1() {
      try {
         final Vector var1 = this.business.getVct_files();
         if (var1.size() == 0) {
            String var6 = "Nincs archiválandó állomány.";
            GuiUtil.showMessageDialog(MainFrame.thisinstance, var6, "Üzenet", 1);
            return true;
         }

         Object[] var2 = new Object[]{"Tovább", "Mégsem"};
         int var3 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Az összes nyomtatványsablon archiválásra kerül!\n" + var1.size() + " db. állomány", "Nyomtatványok archiválása", 0, 3, (Icon)null, var2, var2[0]);
         if (var3 != 0) {
            return true;
         }

         this.pb.setString("Archiválás folyamatban!");
         this.pb.setStringPainted(true);
         Thread var4 = new Thread(new Runnable() {
            public void run() {
               FormArchiver.this.db = 0;

               for(int var1x = 0; var1x < var1.size(); ++var1x) {
                  try {
                     if (FormArchiver.this.need_stop) {
                        break;
                     }

                     ListItem var2 = (ListItem)var1.get(var1x);
                     File var3 = (File)var2.getItem();
                     File var4 = new File((String)PropertyList.getInstance().get("prop.sys.root"), "nyomtatvanyok_archivum");
                     boolean var5 = Tools.zipFile(var3.getAbsolutePath(), var4.getAbsolutePath(), ".tem_enyk_zip") == 0;
                     if (var5) {
                        var3.delete();
                        FormArchiver.this.db++;
                     }

                     FormArchiver.this.pb.setString(var1x + " / " + var1.size());
                  } catch (Exception var8) {
                     Tools.eLog(var8, 0);
                  }
               }

               try {
                  SwingUtilities.invokeAndWait(new Runnable() {
                     public void run() {
                        FormArchiver.this.business.rescan();
                     }
                  });
               } catch (InterruptedException var6) {
                  var6.printStackTrace();
               } catch (InvocationTargetException var7) {
                  var7.printStackTrace();
               }

               FormArchiver.this.pb.setStringPainted(false);
               String var9 = FormArchiver.this.db + " darab állomány archiválva!";
               if (FormArchiver.this.user_cancelled) {
                  var9 = "Felhasználói megszakítás!\n" + var9;
               }

               FormArchiver.this.done_stop();
               GuiUtil.showMessageDialog(MainFrame.thisinstance, var9, "Üzenet", 1);
            }
         });
         var4.start();
      } catch (Exception var5) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A bejegyzéshez tartozó állomány azonosítása nem sikerült!", "Üzenet", 0);
      }

      return false;
   }

   private boolean done_2() {
      try {
         Object[] var1 = this.business.getSelectedFiles();
         if (var1 == null) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem jelölt meg nyomtatványt!", "Üzenet", 0);
            return true;
         }

         Object[] var2 = (Object[])((Object[])var1[0]);
         File var3 = (File)var2[0];
         Hashtable var4 = (Hashtable)var2[3];
         var4 = (Hashtable)var4.get("docinfo");
         final String var5 = (String)var4.get("org");
         final String var6 = (String)var4.get("name");
         final String var7 = (String)var4.get("id");
         String var8 = (String)var4.get("ver");
         String var9 = var5 + " szervezet " + var6 + " nevű nyomtatványának összes verzióját az utolsó nélkül akarja archiválni! Folytatja?";
         if (0 != JOptionPane.showConfirmDialog(MainFrame.thisinstance, var9, "Telepített nyomtatvány archiválása", 0)) {
            return true;
         }

         Thread var10 = new Thread(new Runnable() {
            public void run() {
               Vector var1 = FormArchiver.this.business.getVct_files();
               FormArchiver.this.db = 0;
               FormArchiver.this.pb.setString("Archiválás folyamatban!");
               FormArchiver.this.pb.setStringPainted(true);
               Vector var2 = new Vector();
               String var3 = null;

               int var4;
               ListItem var5x;
               File var6x;
               Hashtable var7x;
               String var8;
               String var9;
               String var10;
               String var11;
               for(var4 = 0; var4 < var1.size(); ++var4) {
                  try {
                     var5x = (ListItem)var1.get(var4);
                     var6x = (File)var5x.getItem();
                     var7x = (Hashtable)var5x.getText();
                     var7x = (Hashtable)var7x.get("docinfo");
                     var8 = (String)var7x.get("org");
                     var9 = (String)var7x.get("name");
                     var10 = (String)var7x.get("id");
                     var11 = (String)var7x.get("ver");
                     if (var8.equals(var5) && var10.equals(var7) && var9.equals(var6)) {
                        var2.add(var5x);
                        if (var3 == null) {
                           var3 = var11;
                        }

                        if ((new Version(var11)).compareTo(new Version(var3)) >= 1) {
                           var3 = var11;
                        }
                     }
                  } catch (Exception var16) {
                     Tools.eLog(var16, 0);
                  }
               }

               for(var4 = 0; var4 < var2.size(); ++var4) {
                  try {
                     if (FormArchiver.this.need_stop) {
                        break;
                     }

                     var5x = (ListItem)var2.get(var4);
                     var6x = (File)var5x.getItem();
                     var7x = (Hashtable)var5x.getText();
                     var7x = (Hashtable)var7x.get("docinfo");
                     var8 = (String)var7x.get("org");
                     var9 = (String)var7x.get("name");
                     var10 = (String)var7x.get("id");
                     var11 = (String)var7x.get("ver");
                     if (var8.equals(var5) && var10.equals(var7) && var9.equals(var6) && !var3.equals(var11)) {
                        File var12 = new File((String)PropertyList.getInstance().get("prop.sys.root"), "nyomtatvanyok_archivum");
                        boolean var13 = Tools.zipFile(var6x.getAbsolutePath(), var12.getAbsolutePath(), ".tem_enyk_zip") == 0;
                        if (var13) {
                           var6x.delete();
                           FormArchiver.this.db++;
                           FormArchiver.this.pb.setString(var4 + 1 + " / " + var2.size());
                        }
                     }
                  } catch (Exception var17) {
                     Tools.eLog(var17, 0);
                  }
               }

               try {
                  SwingUtilities.invokeAndWait(new Runnable() {
                     public void run() {
                        FormArchiver.this.business.rescan();
                     }
                  });
               } catch (InterruptedException var14) {
                  var14.printStackTrace();
               } catch (InvocationTargetException var15) {
                  var15.printStackTrace();
               }

               FormArchiver.this.pb.setStringPainted(false);
               String var18 = FormArchiver.this.db + " darab állomány archiválva!";
               if (FormArchiver.this.user_cancelled) {
                  var18 = "Felhasználói megszakítás!\n" + var18;
               }

               FormArchiver.this.done_stop();
               GuiUtil.showMessageDialog(MainFrame.thisinstance, var18, "Üzenet", 1);
            }
         });
         var10.start();
      } catch (Exception var11) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A bejegyzéshez tartozó állomány azonosítása nem sikerült!", "Üzenet", 0);
      }

      return false;
   }

   private boolean done_3() {
      try {
         Object[] var1 = this.business.getSelectedFiles();
         if (var1 == null) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem jelölt meg nyomtatványt!", "Üzenet", 0);
            return true;
         }

         Object[] var2 = (Object[])((Object[])var1[0]);
         File var3 = (File)var2[0];
         Hashtable var4 = (Hashtable)var2[3];
         var4 = (Hashtable)var4.get("docinfo");
         final String var5 = (String)var4.get("org");
         final String var6 = (String)var4.get("name");
         final String var7 = (String)var4.get("id");
         String var8 = (String)var4.get("ver");
         String var9 = var5 + " szervezet " + var6 + " nevű nyomtatványának összes verzióját akarja archiválni! Folytatja?";
         if (0 != JOptionPane.showConfirmDialog(MainFrame.thisinstance, var9, "Telepített nyomtatvány archiválása", 0)) {
            return true;
         }

         Thread var10 = new Thread(new Runnable() {
            public void run() {
               Vector var1 = FormArchiver.this.business.getVct_files();
               FormArchiver.this.db = 0;
               FormArchiver.this.pb.setString("Archiválás folyamatban!");
               FormArchiver.this.pb.setStringPainted(true);

               for(int var2 = 0; var2 < var1.size(); ++var2) {
                  try {
                     if (FormArchiver.this.need_stop) {
                        break;
                     }

                     ListItem var3 = (ListItem)var1.get(var2);
                     File var4 = (File)var3.getItem();
                     Hashtable var5x = (Hashtable)var3.getText();
                     var5x = (Hashtable)var5x.get("docinfo");
                     String var6x = (String)var5x.get("org");
                     String var7x = (String)var5x.get("name");
                     String var8 = (String)var5x.get("id");
                     if (var6x.equals(var5) && var8.equals(var7) && var7x.equals(var6)) {
                        File var9 = new File((String)PropertyList.getInstance().get("prop.sys.root"), "nyomtatvanyok_archivum");
                        boolean var10 = Tools.zipFile(var4.getAbsolutePath(), var9.getAbsolutePath(), ".tem_enyk_zip") == 0;
                        if (var10) {
                           var4.delete();
                           FormArchiver.this.db++;
                        }
                     }
                  } catch (Exception var13) {
                     Tools.eLog(var13, 0);
                  }
               }

               try {
                  SwingUtilities.invokeAndWait(new Runnable() {
                     public void run() {
                        FormArchiver.this.business.rescan();
                     }
                  });
               } catch (InterruptedException var11) {
                  var11.printStackTrace();
               } catch (InvocationTargetException var12) {
                  var12.printStackTrace();
               }

               FormArchiver.this.pb.setStringPainted(false);
               String var14 = FormArchiver.this.db + " darab állomány archiválva!";
               if (FormArchiver.this.user_cancelled) {
                  var14 = "Felhasználói megszakítás!\n" + var14;
               }

               FormArchiver.this.done_stop();
               GuiUtil.showMessageDialog(MainFrame.thisinstance, var14, "Üzenet", 1);
            }
         });
         var10.start();
      } catch (Exception var11) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A bejegyzéshez tartozó állomány azonosítása nem sikerült!", "Üzenet", 0);
      }

      return false;
   }

   private void done_stop() {
      this.ready = true;
      this.need_stop = true;
      this.stop.setEnabled(false);
      this.ok.setEnabled(true);
      this.exit.setEnabled(true);
      this.pb.setIndeterminate(false);
   }

   private void done_exit() {
      this.file_panel.getBusiness().saveFilterSettings("FormArchiver");
      this.dlg.setVisible(false);
   }

   private boolean check_dir() {
      File var1 = new File((String)PropertyList.getInstance().get("prop.sys.root"), "nyomtatvanyok_archivum");
      if (!var1.exists()) {
         try {
            boolean var2 = var1.mkdir();
            if (!var2) {
               return true;
            }
         } catch (Exception var3) {
         }
      }

      return false;
   }

   public void setParameters(Hashtable var1) {
   }

   public ICommandObject copy() {
      return getInstance();
   }

   public boolean isCommandIdentical(String var1) {
      if (var1 != null) {
         var1 = var1.trim();
         if (var1.equalsIgnoreCase(this.cmd_list.get(0).toString())) {
            return true;
         }
      }

      return false;
   }

   public Vector getCommandList() {
      return this.cmd_list;
   }

   public Hashtable getCommandHelps() {
      return null;
   }

   public Object getState(Object var1) {
      if (var1 instanceof Integer) {
         int var2 = (Integer)var1;
         return var2 >= 0 && var2 <= this.states.length - 1 ? this.states[var2] : Boolean.FALSE;
      } else {
         return Boolean.FALSE;
      }
   }

   private String getProperty(String var1) {
      String var4 = "";
      IPropertyList var2 = PropertyList.getInstance();
      Object var3 = var2.get(var1);
      if (var3 != null) {
         var4 = var3.toString();
      }

      return var4;
   }

   public void done_bg() {
      if (!this.check_dir()) {
         this.business.addFilters(this.filters, (String[])null);
         this.business.rescan();
         Vector var1 = this.business.getVct_files();
         Vector var2 = this.getfilteredlist(var1);
         File var3 = new File((String)PropertyList.getInstance().get("prop.sys.root"), "nyomtatvanyok_archivum");

         for(int var4 = 0; var4 < var1.size(); ++var4) {
            try {
               ListItem var5 = (ListItem)var1.get(var4);
               if (!var2.contains(var5)) {
                  File var6 = (File)var5.getItem();
                  boolean var7 = Tools.zipFile(var6.getAbsolutePath(), var3.getAbsolutePath(), ".tem_enyk_zip") == 0;
                  if (var7) {
                     var6.delete();
                  }
               }
            } catch (Exception var8) {
               Tools.eLog(var8, 0);
            }
         }

      }
   }

   private class ButtonActions extends FileBusiness.ButtonExecutor {
      public ButtonActions(FilePanel var2) {
         super(var2);
      }

      public void b11Clicked() {
         try {
            Object[] var1 = FormArchiver.this.business.getSelectedFiles();
            if (var1 == null) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem jelölt meg nyomtatványt!", "Üzenet", 0);
               return;
            }

            Object[] var2 = (Object[])((Object[])var1[0]);
            File var3 = (File)var2[0];
            Hashtable var4 = (Hashtable)var2[3];
            var4 = (Hashtable)var4.get("docinfo");
            String var5 = (String)var4.get("org");
            String var6 = (String)var4.get("name");
            String var7 = (String)var4.get("id");
            String var8 = (String)var4.get("ver");
            String var9 = var5 + " szervezet " + var6 + " nevű nyomtatványának összes verzióját az utolsó nélkül akarja archiválni! Folytatja?";
            if (0 == JOptionPane.showConfirmDialog(MainFrame.thisinstance, var9, "Telepített nyomtatvány archiválása", 0)) {
               Vector var10 = FormArchiver.this.business.getVct_files();
               int var11 = 0;
               Vector var12 = new Vector();
               String var13 = null;

               int var14;
               ListItem var15;
               File var16;
               Hashtable var17;
               String var18;
               String var19;
               String var20;
               String var21;
               for(var14 = 0; var14 < var10.size(); ++var14) {
                  try {
                     var15 = (ListItem)var10.get(var14);
                     var16 = (File)var15.getItem();
                     var17 = (Hashtable)var15.getText();
                     var17 = (Hashtable)var17.get("docinfo");
                     var18 = (String)var17.get("org");
                     var19 = (String)var17.get("name");
                     var20 = (String)var17.get("id");
                     var21 = (String)var17.get("ver");
                     if (var18.equals(var5) && var20.equals(var7) && var19.equals(var6)) {
                        var12.add(var15);
                        if (var13 == null) {
                           var13 = var21;
                        }

                        if ((new Version(var21)).compareTo(new Version(var13)) >= 1) {
                           var13 = var21;
                        }
                     }
                  } catch (Exception var25) {
                     Tools.eLog(var25, 0);
                  }
               }

               for(var14 = 0; var14 < var12.size(); ++var14) {
                  try {
                     var15 = (ListItem)var12.get(var14);
                     var16 = (File)var15.getItem();
                     var17 = (Hashtable)var15.getText();
                     var17 = (Hashtable)var17.get("docinfo");
                     var18 = (String)var17.get("org");
                     var19 = (String)var17.get("name");
                     var20 = (String)var17.get("id");
                     var21 = (String)var17.get("ver");
                     if (var18.equals(var5) && var20.equals(var7) && var19.equals(var6) && !var13.equals(var21)) {
                        File var22 = new File((String)PropertyList.getInstance().get("prop.sys.root"), "nyomtatvanyok_archivum");
                        boolean var23 = Tools.zipFile(var16.getAbsolutePath(), var22.getAbsolutePath(), ".tem_enyk_zip") == 0;
                        if (var23) {
                           var16.delete();
                           ++var11;
                        }
                     }
                  } catch (Exception var24) {
                     Tools.eLog(var24, 0);
                  }
               }

               FormArchiver.this.business.rescan();
               String var27 = var11 + " darab állomány archiválva!";
               GuiUtil.showMessageDialog(MainFrame.thisinstance, var27, "Üzenet", 1);
            }
         } catch (Exception var26) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A bejegyzéshez tartozó állomány azonosítása nem sikerült!", "Üzenet", 0);
         }

      }

      public void b12Clicked() {
      }

      public void b13Clicked() {
      }

      public void b14Clicked() {
      }

      public void b15Clicked() {
      }

      public void b21Clicked() {
      }

      public void b23PathClicked() {
         try {
            Object[] var1 = FormArchiver.this.business.getSelectedFiles();
            if (var1 == null) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem jelölt meg nyomtatványt!", "Üzenet", 0);
               return;
            }

            Object[] var2 = (Object[])((Object[])var1[0]);
            File var3 = (File)var2[0];
            Hashtable var4 = (Hashtable)var2[3];
            var4 = (Hashtable)var4.get("docinfo");
            String var5 = (String)var4.get("org");
            String var6 = (String)var4.get("name");
            String var7 = (String)var4.get("id");
            String var8 = (String)var4.get("ver");
            String var9 = var5 + " szervezet " + var6 + " nevű nyomtatványának összes verzióját akarja archiválni! Folytatja?";
            if (0 == JOptionPane.showConfirmDialog(MainFrame.thisinstance, var9, "Telepített nyomtatvány archiválása", 0)) {
               Vector var10 = FormArchiver.this.business.getVct_files();
               int var11 = 0;

               for(int var12 = 0; var12 < var10.size(); ++var12) {
                  try {
                     ListItem var13 = (ListItem)var10.get(var12);
                     File var14 = (File)var13.getItem();
                     Hashtable var15 = (Hashtable)var13.getText();
                     var15 = (Hashtable)var15.get("docinfo");
                     String var16 = (String)var15.get("org");
                     String var17 = (String)var15.get("name");
                     String var18 = (String)var15.get("id");
                     if (var16.equals(var5) && var18.equals(var7) && var17.equals(var6)) {
                        File var19 = new File((String)PropertyList.getInstance().get("prop.sys.root"), "nyomtatvanyok_archivum");
                        boolean var20 = Tools.zipFile(var14.getAbsolutePath(), var19.getAbsolutePath(), ".tem_enyk_zip") == 0;
                        if (var20) {
                           var14.delete();
                           ++var11;
                        }
                     }
                  } catch (Exception var21) {
                     Tools.eLog(var21, 0);
                  }
               }

               FormArchiver.this.business.rescan();
               String var23 = var11 + " darab állomány archiválva!";
               GuiUtil.showMessageDialog(MainFrame.thisinstance, var23, "Üzenet", 1);
            }
         } catch (Exception var22) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A bejegyzéshez tartozó állomány azonosítása nem sikerült!", "Üzenet", 0);
         }

      }

      public void b22_1PathClicked() {
         Vector var1 = FormArchiver.this.business.getVct_files();
         Vector var2 = FormArchiver.this.getfilteredlist(var1);
         int var3 = var1.size() - var2.size();
         Object[] var4 = new Object[]{"Tovább", "Mégsem"};
         int var5 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Az összes nyomtatványsablon kivéve az utolsó verziók archiválásra kerül!\n" + var3 + " db. állomány", "Nyomtatványok archiválása", 0, 3, (Icon)null, var4, var4[0]);
         if (var5 == 0) {
            int var6 = 0;
            File var7 = new File((String)PropertyList.getInstance().get("prop.sys.root"), "nyomtatvanyok_archivum");

            for(int var8 = 0; var8 < var1.size(); ++var8) {
               try {
                  ListItem var9 = (ListItem)var1.get(var8);
                  if (!var2.contains(var9)) {
                     File var10 = (File)var9.getItem();
                     boolean var11 = Tools.zipFile(var10.getAbsolutePath(), var7.getAbsolutePath(), ".tem_enyk_zip") == 0;
                     if (var11) {
                        var10.delete();
                        ++var6;
                     }
                  }
               } catch (Exception var12) {
                  Tools.eLog(var12, 0);
               }
            }

            FormArchiver.this.business.rescan();
            String var13 = var6 + " darab állomány archiválva!";
            GuiUtil.showMessageDialog(MainFrame.thisinstance, var13, "Üzenet", 1);
         }
      }

      public void b22_2PathClicked() {
         try {
            final Vector var1 = FormArchiver.this.business.getVct_files();
            Object[] var2 = new Object[]{"Tovább", "Mégsem"};
            int var3 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Az összes nyomtatványsablon archiválásra kerül!\n" + var1.size() + " db. állomány", "Nyomtatványok archiválása", 0, 3, (Icon)null, var2, var2[0]);
            if (var3 != 0) {
               return;
            }

            MainFrame.thisinstance.setGlassLabel("Archiválás folyamatban!");
            Thread var4 = new Thread(new Runnable() {
               public void run() {
                  int var1x = 0;

                  for(int var2 = 0; var2 < var1.size(); ++var2) {
                     try {
                        ListItem var3 = (ListItem)var1.get(var2);
                        File var4 = (File)var3.getItem();
                        File var5 = new File((String)PropertyList.getInstance().get("prop.sys.root"), "nyomtatvanyok_archivum");
                        boolean var6 = Tools.zipFile(var4.getAbsolutePath(), var5.getAbsolutePath(), ".tem_enyk_zip") == 0;
                        if (var6) {
                           var4.delete();
                           ++var1x;
                        }

                        MainFrame.thisinstance.setGlassLabel("Archiválás folyamatban! " + var1x + " / " + var1.size());
                     } catch (Exception var9) {
                        Tools.eLog(var9, 0);
                     }
                  }

                  try {
                     SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                           FormArchiver.this.business.rescan();
                        }
                     });
                  } catch (InterruptedException var7) {
                     var7.printStackTrace();
                  } catch (InvocationTargetException var8) {
                     var8.printStackTrace();
                  }

                  MainFrame.thisinstance.setGlassLabel((String)null);
                  String var10 = var1x + " darab állomány archiválva!";
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, var10, "Üzenet", 1);
                  FormArchiver.this.dlg.setVisible(true);
               }
            });
            FormArchiver.this.dlg.setVisible(false);
            var4.start();
         } catch (Exception var5) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A bejegyzéshez tartozó állomány azonosítása nem sikerült!", "Üzenet", 0);
         }

      }

      public void b22PathClicked() {
         try {
            Object[] var1 = FormArchiver.this.business.getSelectedFiles();
            if (var1 == null) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem jelölt meg nyomtatványt!", "Üzenet", 0);
               return;
            }

            Object[] var2 = (Object[])((Object[])var1[0]);
            File var3 = (File)var2[0];
            Hashtable var4 = (Hashtable)var2[3];
            var4 = (Hashtable)var4.get("docinfo");
            String var5 = (String)var4.get("org");
            String var6 = (String)var4.get("name");
            String var7 = (String)var4.get("id");
            String var8 = (String)var4.get("ver");
            if (var3.exists()) {
               String var9 = var5 + " szervezet " + var6 + " nevű " + var8 + " verziójú nyomtatványát akarja archiválni! Folytatja?";
               if (0 == JOptionPane.showConfirmDialog(MainFrame.thisinstance, var9, "Telepített nyomtatvány archiválása", 0)) {
                  File var10 = new File((String)PropertyList.getInstance().get("prop.sys.root"), "nyomtatvanyok_archivum");
                  boolean var11 = Tools.zipFile(var3.getAbsolutePath(), var10.getAbsolutePath(), ".tem_enyk_zip") == 0;
                  if (var11) {
                     var3.delete();
                     FormArchiver.this.business.rescan();
                  } else {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az archiválás nem sikerült!", "Üzenet", 0);
                  }
               }
            } else {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "A bejegyzéshez tartozó állomány már nem létezik!", "Üzenet", 0);
            }
         } catch (Exception var12) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A bejegyzéshez tartozó állomány azonosítása nem sikerült!", "Üzenet", 0);
         }

      }

      public void b31Clicked() {
      }

      public void b32Clicked() {
         this.file_panel.getBusiness().saveFilterSettings("FormArchiver");
         FormArchiver.this.dlg.setVisible(false);
         this.file_panel.fireEvent(new CloseEvent(this.file_panel));
      }
   }
}
