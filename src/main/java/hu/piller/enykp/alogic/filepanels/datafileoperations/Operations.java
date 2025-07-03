package hu.piller.enykp.alogic.filepanels.datafileoperations;

import hu.piller.enykp.alogic.filepanels.attachement.AttachementTool;
import hu.piller.enykp.alogic.filepanels.filepanel.FileBusiness;
import hu.piller.enykp.alogic.fileutil.DatastoreKeyToXml;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.ILoadManager;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.interfaces.ISaveManager;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Operations {
   private static Vector userCancelledFiles = new Vector();

   public static Vector getUserCancelledFiles() {
      return userCancelledFiles;
   }

   public static Object[] getSelectedFile(FileBusiness var0) {
      Object[] var1 = var0.getSelectedFiles();
      return var1 != null && var1[0] instanceof Object[] ? (Object[])((Object[])var1[0]) : null;
   }

   public static Object[] getSelectedFiles(FileBusiness var0) {
      return var0.getSelectedFiles();
   }

   public static File[] getFileArray(Object[] var0) {
      if (var0 == null) {
         return null;
      } else {
         File[] var1 = new File[var0.length];
         int var2 = 0;

         for(int var3 = var1.length; var2 < var3; ++var2) {
            var1[var2] = (File)((Object[])((Object[])var0[var2]))[0];
         }

         return var1;
      }
   }

   public static void renameFiles(Component var0, File[] var1, Object var2) {
      if (var1 != null) {
         int var3 = 0;

         for(int var4 = var1.length; var3 < var4; ++var3) {
            renameFile(var0, var1[var3], var2);
         }
      }

   }

   public static String renameFile(Component var0, File var1, Object var2) {
      if (var1 != null) {
         if (var1.exists()) {
            String var3 = (String)JOptionPane.showInputDialog(var0, var1.getName() + " átnevezése.\n Új neve:", "Átnevezés", 3, (Icon)null, (Object[])null, var1.getName());
            if (var3 != null) {
               if (var2 instanceof ILoadManager) {
                  var3 = ((ILoadManager)var2).createFileName(var3);
               } else if (var2 instanceof ISaveManager) {
                  var3 = ((ISaveManager)var2).createFileName(var3);
               }

               if (!DatastoreKeyToXml.htmlCut(var3).equals(var3)) {
                  GuiUtil.showMessageDialog(var0, "A fájlnév nem megengedett karaktert tartalmaz ( &,<,>,',\" ), kérjük módosítsa!", "Hibás fájlnév", 0);
                  return null;
               }

               File var4 = new File(var1.getParent(), var3);
               if (var4.exists()) {
                  GuiUtil.showMessageDialog(var0, var4.getName() + " állomány létezik, válasszon másik nevet !", "Átnevezés", 0);
                  return null;
               }

               System.gc();

               try {
                  Thread.sleep(500L);
               } catch (InterruptedException var9) {
               }

               boolean var5 = var1.renameTo(var4);
               if (var5) {
                  IPropertyList var6 = PropertyList.getInstance();
                  if (var6 != null) {
                     Object var7 = var6.get("prop.dynamic.opened_file");
                     File var8 = new File(getSavePath(), (new File(var7 == null ? "" : var7.toString())).getName());
                     if (var1.equals(var8)) {
                        var6.set("prop.dynamic.opened_file", var4);
                     }
                  }

                  GuiUtil.showMessageDialog(var0, var1.getName() + "\n új neve:\n" + var4.getName(), "Átnevezés", 1);
                  return var4.getName();
               }

               GuiUtil.showMessageDialog(var0, var1.getName() + " -t nem lehetett átnevezni !", "Átnevezés", 0);
            }
         } else {
            GuiUtil.showMessageDialog(var0, var1.getName() + " nem létezik, nem nevezhető át !", "Átnevezés", 2);
         }
      } else {
         GuiUtil.showMessageDialog(var0, "Nem jelölt ki állományt !", "Átnevezés", 2);
      }

      return null;
   }

   public static void deleteFiles(JDialog var0, Object[] var1) {
      deleteFiles(var0, getFileArray(var1));
   }

   public static void deleteFiles(final JDialog var0, final File[] var1) {
      if (var1 != null) {
         if (var1.length == 1) {
            deleteFile(var0, var1[0]);
         } else if (0 == JOptionPane.showConfirmDialog(var0, "Törli a kijelölt állományokat ?", "Törlés", 0, 3)) {
            final Operations.Progress var2 = new Operations.Progress(var0, "Törlés folyamatban ...");
            var2.getProgressBar().setMinimum(0);
            var2.getProgressBar().setMaximum(var1.length);
            var2.setLocationRelativeTo(var0);
            Thread var3 = new Thread(new Runnable() {
               public void run() {
                  int var1x = 0;
                  int var2x = 0;

                  for(int var3 = var1.length; var2x < var3; ++var2x) {
                     if (Operations.deleteFileNoQuestion(var0, var1[var2x])) {
                        ++var1x;
                     }

                     var2.getProgressBar().setValue(var2x);
                  }

                  var2.setVisible(false);
                  var2.dispose();
                  GuiUtil.showMessageDialog(var0, "A kijelölt állományokat töröltük", "Üzenet", 1);
               }
            });
            var3.start();
            var2.setVisible(true);
         }
      }

   }

   public static boolean deleteFileNoQuestion(Component var0, File var1) {
      if (var1 != null) {
         if (var1.exists()) {
            System.gc();

            try {
               Thread.sleep(500L);
            } catch (InterruptedException var3) {
            }

            boolean var2 = var1.delete();
            return var2;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static void deleteFile(Component var0, File var1) {
      if (var1 != null) {
         if (var1.exists()) {
            if (0 == JOptionPane.showConfirmDialog(var0, "Törli a " + var1.getName() + " állományt ?", "Törlés", 0, 3)) {
               System.gc();

               try {
                  Thread.sleep(500L);
               } catch (InterruptedException var3) {
               }

               boolean var2 = var1.delete();
               if (!var2) {
                  GuiUtil.showMessageDialog(var0, var1.getName() + " -t nem lehetett törölni!", "Törlés", 0);
               } else {
                  GuiUtil.showMessageDialog(var0, var1.getName() + " -t töröltük.", "Törlés", 1);
               }
            }
         } else {
            GuiUtil.showMessageDialog(var0, var1.getName() + " nem létezik, nem törölhető !", "Törlés", 2);
         }
      } else {
         GuiUtil.showMessageDialog(var0, "Nem jelölt ki állományt !", "Törlés", 2);
      }

   }

   public static void copyFiles(JDialog var0, Object[] var1, File var2) {
      copyFiles(var0, getFileArray(var1), var2);
   }

   public static void copyFiles(final JDialog var0, final File[] var1, final File var2) {
      if (var1 != null) {
         final int[] var3 = new int[]{0};
         final Operations.Progress var4 = new Operations.Progress(var0, "Másolás folyamatban ...");
         var4.getProgressBar().setMinimum(0);
         var4.getProgressBar().setMaximum(var1.length);
         var4.setLocationRelativeTo(var0);
         Thread var5 = new Thread(new Runnable() {
            public void run() {
               Operations.userCancelledFiles.clear();
               int var1x = 0;

               for(int var2x = var1.length; var1x < var2x; ++var1x) {
                  int[] var10000 = var3;
                  var10000[0] += Operations.copyFile_(var0, var1[var1x], var2);
                  var4.getProgressBar().setValue(var1x);
               }

               var4.setVisible(false);
               var4.dispose();
               if (var3[0] > 0) {
                  GuiUtil.showMessageDialog(var0, "Másolás befejeződött.\n(" + var3[0] + " állomány került átmásolásra a\n" + var2 + "\nhelyre)", "Másolás", 1);
               } else {
                  GuiUtil.showMessageDialog(var0, "Másolás nem sikerült !", "Másolás", 0);
               }

            }
         });
         var5.start();
         var4.setVisible(true);
      }

   }

   public static void copyFile(Component var0, File var1, File var2) {
      userCancelledFiles.clear();
      if (copyFile_(var0, var1, var2) > 0) {
         GuiUtil.showMessageDialog(var0, "Másolás befejeződött.\n(" + var2 + ")", "Másolás", 1);
      } else {
         GuiUtil.showMessageDialog(var0, "Másolás nem sikerült !", "Másolás", 0);
      }

   }

   private static int copyFile_(Component var0, File var1, File var2) {
      if (var1 != null && var2 != null && !var1.getParent().equalsIgnoreCase(var2.getPath())) {
         File var3 = new File(var2.getPath(), var1.getName());
         if (var3.exists() && JOptionPane.showConfirmDialog(var0, var1.getName() + " állomány létezik !\nFelülírja?", "Állomány másolása", 0) == 1) {
            userCancelledFiles.add(var1.getName());
            return 0;
         } else {
            return copyFile__(var1, var3);
         }
      } else {
         return 0;
      }
   }

   public static int copyFile__(File var0, File var1) {
      FileInputStream var2 = null;
      FileOutputStream var3 = null;

      try {
         var2 = new FileInputStream(var0);
         var3 = new FileOutputStream(var1);
         byte[] var4 = new byte[4096];

         int var5;
         while((var5 = var2.read(var4)) > 0) {
            var3.write(var4, 0, var5);
         }

         var3.close();
         var3 = null;
         var2.close();
         var2 = null;
         return 1;
      } catch (Exception var8) {
         System.out.println("" + var8);
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var7) {
               Tools.eLog(var7, 0);
            }
         }

         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var6) {
               Tools.eLog(var6, 0);
            }
         }

         return 0;
      }
   }

   public static File getFolder(Component var0, File var1) {
      JFileChooser var2 = new JFileChooser();
      var2.setDialogTitle("Könyvtár választás");
      var2.setFileSelectionMode(1);
      var2.setCurrentDirectory(var1);
      return var2.showOpenDialog(var0) == 0 ? var2.getSelectedFile() : null;
   }

   public static File[] getFile(Component var0, String var1, int var2, File var3, String var4) {
      JFileChooser var5 = new JFileChooser();
      var5.setDialogTitle(var1);
      var5.setDialogType(var2);
      if (var3 != null) {
         var5.setCurrentDirectory(var3);
      }

      if (var4 != null) {
         var5.setSelectedFile(new File(var4));
      }

      if (var5.showDialog(var0, "OK") == 0) {
         File[] var6 = var5.getSelectedFiles();
         if (var6.length == 0) {
            var6 = new File[]{var5.getSelectedFile()};
         }

         return var6;
      } else {
         return null;
      }
   }

   public static String getSavePath() {
      try {
         IPropertyList var0 = PropertyList.getInstance();
         String var1 = (new File((String)var0.get("prop.usr.root"), (String)var0.get("prop.usr.saves"))).toString();
         return var1 == null ? (new File(".")).getCanonicalPath() : var1;
      } catch (IOException var2) {
         ErrorList.getInstance().writeError(new Long(0L), "Sikertelen mentés mappa megszerzési művelet !", var2, (Object)null);
         return ".";
      }
   }

   public static boolean before_copyWithAtc(File var0, File var1) {
      String var2 = var0.getName();
      String var3 = var2.substring(0, var2.length() - ".frm.enyk".length());
      File var4 = new File(var1, var2);
      File var5 = new File(var1, var3);
      AttachementTool.fillFileList(var1);
      String[] var6 = AttachementTool.getAtcs();
      Vector var7 = new Vector();
      if (var4.exists()) {
         var7.add(var4);
      }

      for(int var8 = 0; var8 < var6.length; ++var8) {
         if (var6[var8].indexOf(var3) == 0) {
            String var9 = var6[var8].substring(var3.length() + 1);
            var9 = var9.substring(0, var9.length() - ".atc".length());
            File var10 = new File(new File(var1, var3), var9);
            if (var10.exists()) {
               var7.add(new File(var1, var6[var8]));
            }
         }
      }

      if (var5.exists()) {
         Vector var17 = getDirContentList(var5);
         if (var17.size() != 0) {
            var7.addAll(var17);
         }
      }

      if (var7.size() == 0) {
         return true;
      } else {
         JPanel var18 = new JPanel(new BorderLayout());
         JLabel var19 = new JLabel("<html>A " + var0.getName() + " nevű nyomtatvány már létezik.<br>A művelet végrehajtásához törölni kell az alábbi állományokat!</html>");
         var18.add(var19, "North");
         JList var20 = new JList(var7);
         var20.setEnabled(false);
         StringBuffer var11 = new StringBuffer();

         for(int var12 = 0; var12 < var7.size(); ++var12) {
            var11.append(var7.get(var12).toString() + "\n");
         }

         JTextArea var21 = new JTextArea(var11.toString());
         var21.setEditable(false);
         JScrollPane var13 = new JScrollPane(var21);
         var18.add(var13);
         var18.setPreferredSize(new Dimension(400, 160));
         Object[] var14 = new Object[]{"Törlés", "Mégsem"};
         int var15 = JOptionPane.showOptionDialog(MainFrame.thisinstance, var18, "Kérdés", 2, 1, (Icon)null, var14, var14[0]);
         if (var15 != 0) {
            return false;
         } else {
            for(int var16 = 0; var16 < var7.size(); ++var16) {
               ((File)var7.get(var16)).delete();
            }

            return true;
         }
      }
   }

   public static Vector getDirContentList(File var0) {
      Vector var1 = new Vector();
      getrekdircontentlist(var0, var1);
      return var1;
   }

   private static void getrekdircontentlist(File var0, Vector var1) {
      var1.add(var0);
      File[] var2 = var0.listFiles();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         File var4 = var2[var3];
         if (var4.isDirectory()) {
            getrekdircontentlist(var4, var1);
         } else {
            var1.add(var4);
         }
      }

   }

   private static class Progress extends JDialog {
      private JProgressBar bar = null;

      public Progress(JDialog var1, String var2) {
         super(var1, true);
         this.setUndecorated(true);
         this.bar = new JProgressBar();
         this.bar.setStringPainted(true);
         this.bar.setString(var2);
         JPanel var3 = new JPanel();
         var3.setLayout(new BorderLayout());
         var3.add(this.bar, "Center");
         this.setContentPane(var3);
         this.setSize(300, 30);
      }

      public JProgressBar getProgressBar() {
         return this.bar;
      }
   }
}
