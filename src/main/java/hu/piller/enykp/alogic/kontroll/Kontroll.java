package hu.piller.enykp.alogic.kontroll;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.ILoadManager;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.filelist.EnykFileList;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class Kontroll {
   static final String RESOURCE_NAME = "Kontroll";
   static final Long RESOURCE_ERROR_ID = new Long(903L);
   public static final int MAIN_WIDTH = 700;
   public static final int MAIN_HEIGHT = 480;
   public IPropertyList iplGui;
   public IPropertyList abevLI;
   public IPropertyList iplMaster = PropertyList.getInstance();
   public static ILoadManager xmlLoadManager;
   public static ILoadManager templateLoadManager;
   public static final Vector vct_files = new Vector();
   public static String sysroot;
   public static String root;
   public static String kontrollPath;
   public static String dataPath;
   public static String templatePath;
   public static String kontrollKVFPath;
   public static String abevPath;
   public static final String FKIT = "frm.enyk";
   public static final String TKIT = "tem.enyk";
   public static final String[] igenNem = new String[]{"Igen", "Nem"};
   public static final String[] igenNemMegsem = new String[]{"Igen", "Nem", "Mégsem"};
   public static final Hashtable ansiToOem = new Hashtable();

   public void init(int var1) throws Exception {
      JDialog var2 = null;

      try {
         this.iplGui = (IPropertyList)this.iplMaster.get("gui_info");
         this.abevLI = (IPropertyList)this.iplMaster.get("abev_logic_info");

         try {
            var2 = Tools.createInitDialog("Kontroll állományok", "Kontroll nyomtatványok keresése folyamatban");
            var2.setVisible(true);
         } catch (Exception var14) {
            Tools.eLog(var14, 0);
         }

         try {
            sysroot = (String)this.iplMaster.get("prop.sys.root");
            if (sysroot == null) {
               throw new Exception();
            }
         } catch (Exception var20) {
            throw new Exception("*prop.sys.root");
         }

         if (!sysroot.endsWith("\\") && !sysroot.endsWith("/")) {
            sysroot = sysroot + File.separator;
         }

         try {
            root = (String)this.iplMaster.get("prop.usr.root");
            if (root == null) {
               throw new Exception();
            }
         } catch (Exception var19) {
            throw new Exception("*prop.usr.root");
         }

         if (!root.endsWith("\\") && !root.endsWith("/")) {
            root = root + File.separator;
         }

         try {
            kontrollPath = (String)this.iplMaster.get("prop.usr.kontroll");
            if (kontrollPath == null) {
               throw new Exception();
            }

            kontrollPath = root + kontrollPath;
         } catch (Exception var18) {
            throw new Exception("*prop.usr.kontroll");
         }

         try {
            abevPath = (String)this.iplMaster.get("prop.sys.abev");
            if (abevPath == null) {
               throw new Exception();
            }

            abevPath = sysroot + abevPath;
         } catch (Exception var17) {
            throw new Exception("*prop.sys.abev");
         }

         if (!abevPath.endsWith("\\") && !abevPath.endsWith("/")) {
            abevPath = abevPath + File.separator;
         }

         try {
            kontrollKVFPath = (String)this.iplMaster.get("prop.sys.kontroll");
            if (kontrollKVFPath == null) {
               throw new Exception();
            }

            kontrollKVFPath = abevPath + kontrollKVFPath;
         } catch (Exception var16) {
            throw new Exception("*prop.sys.kontroll");
         }

         if (!kontrollKVFPath.endsWith("\\") && !kontrollKVFPath.endsWith("/")) {
            kontrollKVFPath = kontrollKVFPath + File.separator;
         }

         kontrollKVFPath = this.beauty(kontrollKVFPath);
         if (!kontrollPath.endsWith("\\") && !kontrollPath.endsWith("/")) {
            kontrollPath = kontrollPath + File.separator;
         }

         kontrollPath = this.beauty(kontrollPath);

         try {
            dataPath = (String)this.iplMaster.get("prop.usr.saves");
            if (dataPath == null) {
               throw new Exception();
            }

            dataPath = root + dataPath;
         } catch (Exception var15) {
            throw new Exception("*prop.usr.kontroll");
         }

         if (!dataPath.endsWith("\\") && !dataPath.endsWith("/")) {
            dataPath = dataPath + File.separator;
         }

         try {
            templatePath = (String)this.iplMaster.get("prop.dynamic.templates.absolutepath");
         } catch (Exception var13) {
            throw new Exception("*" + this.iplMaster.get("prop.dynamic.templates.absolutepath"));
         }

         if (!templatePath.endsWith("\\") && !templatePath.endsWith("/")) {
            templatePath = templatePath + File.separator;
         }

         this.initAnsiToOem();

         try {
            Object var3 = MainFrame.thisinstance;
            if (var3 == null) {
               var3 = new JFrame();
            }

            JDialog var4 = new JDialog((Frame)var3, "Kontroll állományok", true);
            int var5 = ((JFrame)var3).getX() + ((JFrame)var3).getWidth() / 2 - 350;
            if (var5 < 0) {
               var5 = 0;
            }

            int var6 = ((JFrame)var3).getY() + ((JFrame)var3).getHeight() / 2 - 240;
            if (var6 < 0) {
               var6 = 0;
            }

            var4.setBounds(var5, var6, 700, 480);
            if (var1 == 0) {
               DatTableModel var7 = new DatTableModel();
               vct_files.clear();
               this.fillVectorToModel2(var7);
               StartForm var8 = new StartForm(var4, var7);
               var4.getContentPane().add(var8);
            } else {
               CBTableModel var22 = this.fillKontrollFilesToModel();
               MasolasForm var23 = new MasolasForm(var4, var22);
               var23.setIplGui((BookModel)null);
               var4.getContentPane().add(var23);
            }

            try {
               var2.dispose();
            } catch (Exception var11) {
               Tools.eLog(var11, 0);
            }

            var4.pack();
            var4.setVisible(true);
         } catch (Exception var12) {
            try {
               var2.dispose();
            } catch (Exception var10) {
               Tools.eLog(var10, 0);
            }

            GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiba a kontroll nyomtatványok kezelésekor!" + (var12.getMessage().startsWith("*") ? "\n" + var12.getMessage().substring(1) : ""), "Kontroll állományok", 0);
         }

      } catch (Exception var21) {
         try {
            var2.dispose();
         } catch (Exception var9) {
            Tools.eLog(var9, 0);
         }

         throw var21;
      }
   }

   private void fillVectorToModel2(DatTableModel var1) throws Exception {
      Hashtable var2 = this.getFileData();
      if (var2.size() == 0) {
         throw new Exception("*Nem található megfelelő nyomtatvány!");
      } else {
         Enumeration var3 = var2.keys();
         boolean var4 = false;
         KTools.setPm();

         try {
            while(var3.hasMoreElements()) {
               Object var5 = var3.nextElement();
               Hashtable var6 = (Hashtable)var2.get(var5);
               if (!vct_files.contains(var6)) {
                  try {
                     Vector var7 = new Vector();
                     var7.add(((String)var6.get("id")).toLowerCase());
                     var7.add(var6.get("person_name"));
                     var7.add(var6.get("tax_number"));
                     var7.add(var6.get("account_name"));
                     var7.add(var6.get("ver"));
                     var7.add(var6.get("from_date"));
                     var7.add(var6.get("to_date"));
                     var7.add(var6.get("note"));
                     var7.add(this.formattedDate((String)var6.get("saved")));
                     var7.add(var5);
                     var7.add(var6.get("name"));
                     var1.addRow(var7);
                     vct_files.add(var6);
                  } catch (Exception var8) {
                     var4 = true;
                  }
               }
            }

            if (vct_files.size() == 0) {
               throw new Exception("*Nem található megfelelő kontroll sablon!");
            }
         } catch (Exception var9) {
            if (var4) {
               throw new Exception("Hiba a kontroll nyomtatványok feldolgozásakor");
            } else if (!var9.getMessage().startsWith("*")) {
               throw new Exception("Hiba a kontroll nyomtatványok olvasásakor");
            } else {
               throw var9;
            }
         }
      }
   }

   private CBTableModel fillKontrollFilesToModel() throws Exception {
      CBTableModel var1 = new CBTableModel();
      KontrollFilenameFilter var2 = new KontrollFilenameFilter("kif");
      File var3 = new File(kontrollPath);
      String[] var4 = var3.list(var2);
      if (var4 == null) {
         throw new Exception("*Nem található megfelelő nyomtatvány");
      } else {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            Vector var6 = this.parseKifFile(var4[var5]);
            Vector var7 = new Vector();
            var7.add(Boolean.FALSE);
            var7.add(var6.get(0));
            var7.add(var6.get(1));
            var7.add(var6.get(2));
            var7.add(var6.get(3));
            String[] var8 = new String[var6.size() - 4];

            for(int var9 = 4; var9 < var6.size(); ++var9) {
               var8[var9 - 4] = (String)var6.get(var9);
            }

            var7.add(var8);
            var1.addRow(var7);
         }

         return var1;
      }
   }

   private String formattedDate(String var1) {
      try {
         return var1.substring(0, 4) + "." + var1.substring(4, 6) + "." + var1.substring(6, 8) + " " + var1.substring(8, 10) + ":" + var1.substring(10, 12) + ":" + var1.substring(12, 14) + "." + var1.substring(14);
      } catch (Exception var3) {
         return var1;
      }
   }

   private String beauty(String var1) {
      if (File.separator.equals("\\")) {
         var1 = var1.replaceAll("/", "\\\\");
      } else {
         var1 = var1.replaceAll("\\\\", "/");
      }

      return var1;
   }

   private Vector parseKifFile(String var1) throws Exception {
      BufferedReader var2 = new BufferedReader(new FileReader(kontrollPath + var1));
      Vector var4 = new Vector();

      String var3;
      while((var3 = var2.readLine()) != null) {
         var4.add(var3);
      }

      var2.close();
      return var4;
   }

   private void initAnsiToOem() {
      ansiToOem.put("á", new Byte((byte)-96));
      ansiToOem.put("Á", new Byte((byte)-75));
      ansiToOem.put("é", new Byte((byte)-126));
      ansiToOem.put("É", new Byte((byte)-112));
      ansiToOem.put("í", new Byte((byte)-95));
      ansiToOem.put("Í", new Byte((byte)-42));
      ansiToOem.put("ó", new Byte((byte)-94));
      ansiToOem.put("Ó", new Byte((byte)-32));
      ansiToOem.put("ö", new Byte((byte)-108));
      ansiToOem.put("Ö", new Byte((byte)-103));
      ansiToOem.put("ő", new Byte((byte)-117));
      ansiToOem.put("Ő", new Byte((byte)-118));
      ansiToOem.put("ú", new Byte((byte)-93));
      ansiToOem.put("Ú", new Byte((byte)-23));
      ansiToOem.put("ü", new Byte((byte)-127));
      ansiToOem.put("Ü", new Byte((byte)-102));
      ansiToOem.put("ű", new Byte((byte)-5));
      ansiToOem.put("Ű", new Byte((byte)-21));
   }

   private Hashtable getFileData() throws Exception {
      EnykFileList var1 = EnykFileList.getInstance();
      Object[] var2 = var1.list(dataPath, new Object[]{"inner_data_loader_v1"});
      Hashtable var3 = new Hashtable(var2.length);

      for(int var4 = 0; var4 < var2.length; ++var4) {
         Hashtable var5 = (Hashtable)((Hashtable)((Object[])((Object[])var2[var4]))[1]).get("docinfo");

         try {
            var5.put("saved", ((Hashtable)((Object[])((Object[])var2[var4]))[1]).get("saved"));
            var3.put(((Object[])((Object[])var2[var4]))[0], var5);
         } catch (Exception var7) {
            Tools.eLog(var7, 0);
         }
      }

      return var3;
   }
}
