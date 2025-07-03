package hu.piller.enykp.alogic.filepanels;

import hu.piller.enykp.alogic.fileloader.xml.XMLPost;
import hu.piller.enykp.alogic.fileloader.xml.XmlLoader;
import hu.piller.enykp.alogic.filepanels.attachement.EJFileChooser;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.base.PropertyList;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

public class XMLMergePanel extends JPanel {
   JList list;
   DefaultListModel listmodel;
   JTextField source;
   JTextField dest;
   JDialog dia;
   File[] f;
   String idstart;
   String lasterrormsg;

   public XMLMergePanel(JDialog var1) {
      this.dia = var1;
      this.build();
   }

   private void build() {
      this.setLayout(new BorderLayout());
      JLabel var1 = new JLabel("Kérem adja meg az összemásolandó file-okat!");
      var1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      this.add(var1, "North");
      this.add(this.buildcenter());
      this.add(this.buildbuttons(), "South");
      String var2 = "EscPressedKey";
      this.dia.getRootPane().getInputMap(2).put(KeyStroke.getKeyStroke(27, 0), var2);
      this.dia.getRootPane().getActionMap().put(var2, new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            XMLMergePanel.this.dia.setVisible(false);
         }
      });
   }

   private Component buildbuttons() {
      JPanel var1 = new JPanel();
      JButton var2 = new JButton("Összemásolás");
      var2.setName("ok");
      var2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            XMLMergePanel.this.done_ok();
         }
      });
      var1.add(var2);
      JButton var3 = new JButton("Mégsem");
      var3.setName("cancel");
      var3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (XMLMergePanel.this.dia != null) {
               XMLMergePanel.this.dia.setVisible(false);
            }

         }
      });
      var1.add(var3);
      return var1;
   }

   private Component buildcenter() {
      JPanel var1 = new JPanel();
      var1.setLayout(new BorderLayout());
      this.listmodel = new DefaultListModel();
      this.list = new JList(this.listmodel);
      JScrollPane var2 = new JScrollPane(this.list);
      var2.setSize(new Dimension(GuiUtil.getW("c:\\usesrs\\dummy.user\\abevjava\\import\\dummy\\asdfghjkl_qwertzuiop.xml"), 3 * GuiUtil.getCommonItemHeight()));
      var2.setPreferredSize(var2.getSize());
      var2.setMinimumSize(var2.getSize());
      var1.add(var2);
      var1.add(this.buildeast(), "East");
      var1.add(this.buildnorth(), "North");
      var1.add(this.buildsouth(), "South");
      var1.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
      return var1;
   }

   private Component buildeast() {
      JPanel var1 = new JPanel();
      var1.setLayout(new BoxLayout(var1, 1));
      JButton var2 = new JButton("+");
      var2.setName("plus");
      var2.setAlignmentX(0.5F);
      var2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            XMLMergePanel.this.done_add();
         }
      });
      var1.add(var2);
      var1.add(Box.createVerticalStrut(10));
      JButton var3 = new JButton("-");
      var3.setName("minus");
      var3.setAlignmentX(0.5F);
      var3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            XMLMergePanel.this.done_del();
         }
      });
      var1.add(var3);
      var1.setBorder(BorderFactory.createEmptyBorder(25, 5, 5, 5));
      return var1;
   }

   private Component buildnorth() {
      JPanel var1 = new JPanel();
      var1.setLayout(new BorderLayout());
      JLabel var2 = new JLabel("Kiinduló forrás file:");
      var1.add(var2, "North");
      this.source = new JTextField();
      this.source.setEditable(false);
      var1.add(this.source);
      JButton var3 = new JButton("...");
      var3.setName("fc1");
      var3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            XMLMergePanel.this.done_source(XMLMergePanel.this.source);
         }
      });
      var1.add(var3, "East");
      JLabel var4 = new JLabel("Egyéb forrás file:");
      var4.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
      var1.add(var4, "South");
      var1.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
      return var1;
   }

   private Component buildsouth() {
      JPanel var1 = new JPanel();
      var1.setLayout(new BorderLayout());
      JLabel var2 = new JLabel("Cél file megadása:");
      var1.add(var2, "North");
      this.dest = new JTextField();
      var1.add(this.dest);
      JButton var3 = new JButton("...");
      var3.setName("fc2");
      var3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            XMLMergePanel.this.done_source(XMLMergePanel.this.dest);
         }
      });
      var1.add(var3, "East");
      var1.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
      return var1;
   }

   private void done_add() {
      try {
         this.f = null;
         this.done_source((JTextField)null);
         if (this.f == null) {
            return;
         }

         for(int var1 = 0; var1 < this.f.length; ++var1) {
            File var2 = this.f[var1];
            if (var2.getAbsolutePath().equals(this.source.getText())) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "A kiválasztott állomány megegyezik a forrás állománnyal! ( " + var2 + " )", "Hiba", 2);
            } else if (this.listmodel.contains(var2)) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "A kiválasztott állomány már a listán van! ( " + var2 + " )", "Hiba", 2);
            } else {
               this.listmodel.addElement(var2);
            }
         }
      } catch (HeadlessException var3) {
      }

   }

   private void done_del() {
      try {
         this.listmodel.remove(this.list.getSelectedIndex());
      } catch (Exception var2) {
      }

   }

   private void done_source(JTextField var1) {
      try {
         EJFileChooser var2 = new EJFileChooser();
         String var3 = null;
         if (!this.source.equals(var1) && var1 != null) {
            var3 = SettingsStore.getInstance().get("userpaths", "prop.dynamic.userlastpath.9");
         } else {
            var3 = SettingsStore.getInstance().get("userpaths", "prop.dynamic.userlastpath.1");
         }

         File var4 = new File((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.import"));
         if (var3 != null) {
            var4 = new File(var3.toString());
         }

         var2.setCurrentDirectory(var4);
         var2.setDialogTitle("XML állomány kiválasztása");
         FileFilter var5 = new FileFilter() {
            public boolean accept(File var1) {
               if (var1.getAbsolutePath().toLowerCase().endsWith(".xml")) {
                  return true;
               } else {
                  return var1.isDirectory();
               }
            }

            public String getDescription() {
               return "XML állományok";
            }
         };
         var2.removeChoosableFileFilter(var2.getChoosableFileFilters()[0]);
         var2.addChoosableFileFilter(var5);
         if (var1 == null) {
            var2.setMultiSelectionEnabled(true);
         }

         int var6;
         if (var1 != null) {
            var6 = var2.showOpenDialog(MainFrame.thisinstance);
         } else {
            var6 = var2.showSaveDialog(MainFrame.thisinstance);
         }

         if (var6 == 0) {
            if (var1 == null) {
               this.f = var2.getSelectedFiles();
            } else {
               this.f = new File[1];
               this.f[0] = var2.getSelectedFile();
            }

            File var7 = this.f[0];
            if (var7 != null) {
               String var8;
               if (var7.exists()) {
                  if (var1 != null) {
                     var1.setText(var7.getAbsolutePath());
                  }

                  var8 = null;

                  try {
                     var8 = var7.getParent();
                  } catch (Exception var11) {
                  }

                  if (!this.source.equals(var1) && var1 != null) {
                     SettingsStore.getInstance().set("userpaths", "prop.dynamic.userlastpath.9", var8);
                  } else {
                     SettingsStore.getInstance().set("userpaths", "prop.dynamic.userlastpath.1", var8);
                  }
               } else {
                  if (var1 != null) {
                     var1.setText(var7.getAbsolutePath());
                  }

                  var8 = null;

                  try {
                     var8 = var7.getParent();
                  } catch (Exception var10) {
                  }

                  if (this.dest.equals(var1)) {
                     SettingsStore.getInstance().set("userpaths", "prop.dynamic.userlastpath.9", var8);
                  }
               }
            }
         }
      } catch (Exception var12) {
      }

   }

   private void done_ok() {
      String var1 = this.source.getText();
      if (var1 != null && var1.trim().length() != 0) {
         final File var2 = new File(var1);
         if (!var2.exists()) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A forrás file nem található! ( " + var2 + " )", "Hiba", 0);
         } else {
            var1 = this.dest.getText();
            if (var1 != null && var1.trim().length() != 0) {
               if (!var1.endsWith(".xml") && !var1.endsWith(".XML")) {
                  var1 = var1 + ".xml";
                  this.dest.setText(var1);
               }

               File var3 = null;
               if (var3.getParentFile() == null) {
                  var3 = new File(var2.getParent(), var1);
               } else {
                   var3 = new File(var1);
               }

                if (var2.equals(var3)) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "A cél file megegyezik a forrás file-val! ", "Hiba", 0);
               } else if (this.listmodel.size() == 0) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az összemásolás nem hajtható végre, mert nem adott meg több forrás file-t!", "Hiba", 0);
               } else {
                  if (var3.exists()) {
                     Object[] var5 = new Object[]{"Igen", "Nem"};
                     int var6 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "A cél file már létezik.\nFelülírja?", "Kérdés", 0, 3, (Icon)null, var5, var5[0]);
                     if (var6 == 1) {
                        return;
                     }
                  }

                  MainFrame.thisinstance.setGlassLabel("Összemásolás folyamatban!");
                   File finalVar = var3;
                   Thread var7 = new Thread(new Runnable() {
                     public void run() {
                        int var1 = XMLMergePanel.this.merge(var2, finalVar);
                        switch(var1) {
                        case -1:
                           GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az összemásolás során hiba lépett fel!\n" + (XMLMergePanel.this.lasterrormsg == null ? "" : XMLMergePanel.this.lasterrormsg), "Hiba", 0);
                           break;
                        case 0:
                           GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az összemásolás véget ért,\n de nem adtunk hozzá egyetlen nyomtatványt sem a listából!\n" + finalVar, "Üzenet", 1);
                           break;
                        default:
                           GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az összemásolás véget ért!\n" + finalVar, "Üzenet", 1);
                        }

                        MainFrame.thisinstance.setGlassLabel((String)null);
                     }
                  });
                  var7.start();
               }
            } else {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nincs megadva cél file!", "Hiba", 0);
            }
         }
      } else {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nincs megadva forrás file!", "Hiba", 0);
      }
   }

   private int merge(File var1, File var2) {
      int var3 = 0;

      try {
         long var4 = System.currentTimeMillis();
         this.lasterrormsg = null;
         XmlLoader var6 = new XmlLoader();
         String var7 = null;

         try {
            Hashtable var8 = var6.getHeadData(var1);
            var8 = (Hashtable)var8.get("docinfo");
            var7 = (String)var8.get("id");
         } catch (Exception var15) {
         }

         if (var7 == null) {
            this.lasterrormsg = "Nem sikerült a forrás file nyomtatványazonosítóját meghatározni!";
            return -1;
         } else if (!var7.endsWith("A")) {
            this.lasterrormsg = "Nem megfelelő típusú nyomtatvány van a forrás file-ban!\nNincs benne fő nyomtatvány!";
            return -1;
         } else {
            this.idstart = var7.substring(0, var7.length() - 1);
            String var17 = XMLPost.getEncoding(var1);
            BufferedReader var9 = new BufferedReader(new InputStreamReader(new FileInputStream(var1), var17));
            FileOutputStream var10 = new FileOutputStream(var2);
            OutputStreamWriter var11 = new OutputStreamWriter(var10, var17);

            while(true) {
               String var12 = var9.readLine();
               if (var12 == null) {
                  return -1;
               }

               int var13 = var12.indexOf("</nyomtatvanyok>");
               if (var13 != -1) {
                  var11.write(var12.substring(0, var13));
                  var11.write("\n");

                  for(int var18 = 0; var18 < this.listmodel.size(); ++var18) {
                     File var20 = (File)this.listmodel.getElementAt(var18);
                     int var14 = this.merge_listitem(var20, var11);
                     if (var14 == -1) {
                        var11.close();
                        var10.close();
                        var2.delete();
                        this.lasterrormsg = "Az összemásolás nem hajtható végre, mert a " + var20 + " xml-ben súlyos hiba van.\nA hiba megállapításához végezze el az XML ellenőrzést! ";
                        return -1;
                     }

                     var3 += var14;
                  }

                  var11.write("</nyomtatvanyok>");
                  var11.write("\n");
                  var11.close();
                  var10.close();
                  long var19 = System.currentTimeMillis();
                  return var3;
               }

               var11.write(var12);
               var11.write("\n");
            }
         }
      } catch (Exception var16) {
         var16.printStackTrace();
         this.lasterrormsg = "Ismeretlen hiba!";
         return -1;
      }
   }

   private int merge_listitem(File var1, OutputStreamWriter var2) {
      int var3 = 0;

      try {
         String var4 = XMLPost.getEncoding(var1);
         BufferedReader var5 = new BufferedReader(new InputStreamReader(new FileInputStream(var1), var4));

         while(true) {
            String var6 = this.listitem_item(var5);
            if (var6 == null || var6.trim().length() == 0) {
               var5.close();
               return var3;
            }

            if (this.check(var6)) {
               var2.write(var6);
               ++var3;
            }
         }
      } catch (Exception var7) {
         var7.printStackTrace();
         return -1;
      }
   }

   private String listitem_item(BufferedReader var1) {
      boolean var2 = true;
      StringBuffer var3 = new StringBuffer();

      try {
         while(true) {
            String var4 = var1.readLine();
            if (var4 == null) {
               break;
            }

            int var5 = var4.indexOf("<nyomtatvany>");
            if (var5 != -1) {
               var2 = false;
               var3.append(var4.substring(var5, var4.length()));
               var3.append("\n");
            } else {
               int var6 = var4.indexOf("</nyomtatvany>");
               if (var6 != -1) {
                  var3.append(var4.substring(0, var6 + "</nyomtatvany>".length()));
                  var3.append("\n");
                  break;
               }

               if (!var2) {
                  var3.append(var4);
                  var3.append("\n");
               }
            }
         }

         return var3.toString();
      } catch (IOException var7) {
         var7.printStackTrace();
         return null;
      }
   }

   private boolean check(String var1) {
      try {
         if (var1 == null) {
            return false;
         }

         int var2 = var1.indexOf("<nyomtatvanyazonosito>");
         if (var2 == -1) {
            return false;
         }

         var2 += "<nyomtatvanyazonosito>".length();
         int var3 = var1.indexOf("</nyomtatvanyazonosito>");
         if (var3 == -1) {
            return false;
         }

         String var4 = var1.substring(var2, var3).trim();
         if (var4.startsWith(this.idstart) && var4.endsWith("M")) {
            return true;
         }
      } catch (Exception var5) {
      }

      return false;
   }
}
