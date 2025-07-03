package hu.piller.enykp.alogic.masterdata.converter;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityError;
import hu.piller.enykp.alogic.masterdata.core.EntityException;
import hu.piller.enykp.alogic.masterdata.core.EntityHome;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

public class WinABevMDCopy implements IEventListener {
   private static final String[] VPDATA = new String[]{"vpid", "regisztraciosSzam", "engedelyszam", "bejelentoadoazon"};
   private static final int TARSASAG = 0;
   private static final int EGYENI_VALLALKOZO = 1;
   private static final int MAGANSZEMELY = 2;
   private static final int ADOTANACSADO = 3;
   public static final int CONSTRAINT_ONLY_PACCOUNTS = 0;
   public static final int CONSTRAINT_ONLY_TAXEXPERTS = 1;
   private static final String[] type_keys = new String[]{"adoszam", "adoszam", "adoazonosito", "azonosito"};
   private static final String[] xml_type_keys = new String[]{"tax_number", "tax_number", "tax_id", "te_id"};
   private static final String ERR_ID = "WinABevMDCopy";
   private static final boolean is_testing = false;
   private Vector error_list;
   private Set<Entity> invalid = new HashSet();
   private boolean overwrite = true;

   public void setInvalid(Set<Entity> var1) {
      this.invalid = var1;
   }

   public void setOverwrite(boolean var1) {
      this.overwrite = var1;
   }

   public boolean isOverwrite() {
      return this.overwrite;
   }

   public void start() {
      this.start(-1);
   }

   public void start(int var1) {
      int var2 = 1;
      if (var1 == 0) {
         var2 = JOptionPane.showConfirmDialog(MainFrame.thisinstance, "Indulhat a törzsadatok átvétele ABev-ből ?", "Törzsadatok átvétele", 0);
      } else if (var1 == 1) {
         var2 = JOptionPane.showConfirmDialog(MainFrame.thisinstance, "Indulhat az adótanácsadók átvétele ABev-ből ?", "Adótanácsadók átvétele", 0);
      } else if (var1 == -1) {
         var2 = 0;
      }

      if (var2 == 0) {
         this.start_(var1);
      }

   }

   protected Vector loadWinAbevDataFile(File var1) {
      Vector var2 = new Vector();

      try {
         BufferedReader var3 = new BufferedReader(new InputStreamReader(new FileInputStream(var1), "ISO-8859-2"));
         var2.addAll(this.loadRecords(var3));
         var3.close();
      } catch (IOException var4) {
         ErrorList.getInstance().writeError("WinABevMDCopy", var4.getMessage(), var4, (Object)null);
      }

      return var2;
   }

   private Vector loadRecords(BufferedReader var1) throws IOException {
      Vector var2 = new Vector();
      var2.add(new Hashtable());
      var2.add(new Hashtable());
      var2.add(new Hashtable());
      var2.add(new Hashtable());
      Hashtable var3 = null;

      String var4;
      while((var4 = var1.readLine()) != null) {
         var4 = var4.trim();
         if (var4.length() > 0) {
            if (var4.startsWith("[")) {
               if (var3 != null) {
                  this.storeRecord(var2, var3);
               }

               var3 = new Hashtable();
            }

            if (var3 != null) {
               String[] var5 = var4.split("=", 2);
               if (var5.length == 2) {
                  var3.put(var5[0], var5[1]);
               }
            }
         }
      }

      this.storeRecord(var2, var3);
      return var2;
   }

   private void storeRecord(Vector var1, Hashtable var2) {
      if (var2 != null && var2.size() > 0) {
         Object var3 = var2.get("tipus");
         if (var3 == null) {
            this.putRecord((Hashtable)var1.get(3), var2, type_keys[3]);
         } else {
            String var4 = var3.toString().trim();
            if (var4.equals("0")) {
               this.putRecord((Hashtable)var1.get(0), var2, type_keys[0]);
            } else if (var4.equals("1")) {
               this.putRecord((Hashtable)var1.get(1), var2, type_keys[1]);
            } else if (var4.equals("2")) {
               this.putRecord((Hashtable)var1.get(2), var2, type_keys[2]);
            }
         }
      }

   }

   private File[] getABevPAFile(int var1) {
      final String var2;
      switch(var1) {
      case 0:
         var2 = "torzs.dtt";
         break;
      case 1:
         var2 = "atan.dtt";
         break;
      default:
         var2 = ".dtt";
      }

      File[] var3 = new File[]{new File("C:\\Program Files\\Abev 2008\\"), new File("C:\\Program Files\\Abev 2006\\"), new File("D:\\Program Files\\Abev 2008\\"), new File("d:\\Program Files\\Abev 2006\\")};
      File[] var4 = null;
      int var5 = 0;

      for(int var6 = var3.length; var5 < var6; ++var5) {
         if (var3[var5].exists()) {
            File[] var7 = var3[var5].listFiles(new FilenameFilter() {
               public boolean accept(File var1, String var2x) {
                  return WinABevMDCopy.this.checkFileName(var2x, var2);
               }
            });
            if (var7 != null && var7.length > 0) {
               if (var4 == null) {
                  var4 = var7;
               } else {
                  File[] var8 = var4;
                  var4 = new File[var4.length + var7.length];
                  System.arraycopy(var8, 0, var4, 0, var8.length);
                  System.arraycopy(var7, 0, var4, var8.length, var7.length);
               }
            }
         }
      }

      if (var4 == null) {
         String var9;
         if (var1 == 0) {
            var9 = "Törzsadatok átvétele ABev-ből";
         } else if (var1 == 1) {
            var9 = "Adótanácsadók átvétele ABev-ből";
         } else {
            var9 = "ABev törzsadat állomány kiválasztása";
         }

         JFileChooser var10 = new JFileChooser();
         var10.setDialogTitle(var9);
         var10.setDialogType(0);
         var10.setCurrentDirectory(new File("." + File.separator));
         var10.setFileSelectionMode(0);
         var10.setMultiSelectionEnabled(true);
         var10.setAcceptAllFileFilterUsed(false);
         var10.addChoosableFileFilter(new FileFilter() {
            public boolean accept(File var1) {
               return var1.isDirectory() || WinABevMDCopy.this.checkFileName(var1.getName(), var2);
            }

            public String getDescription() {
               return "ABev törzsadat file (" + var2 + ")";
            }
         });
         var10.showOpenDialog(MainFrame.thisinstance);
         var4 = var10.getSelectedFiles();
      }

      return var4;
   }

   private boolean checkFileName(String var1, String var2) {
      boolean var3;
      if (var2.startsWith(".")) {
         var3 = var1.toLowerCase().endsWith(var2);
      } else {
         var3 = var1.equalsIgnoreCase(var2);
      }

      return var3;
   }

   private void putRecord(Hashtable var1, Hashtable var2, String var3) {
      Object var4 = var1.get(var3);
      if (var4 != null) {
         var2 = this.handleSameRecords((Hashtable)var4, var2);
      }

      if (var2 != null) {
         try {
            var1.put(var2.get(var3), var2);
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

   }

   private boolean hasValidPAConverted(String var1, MDStoreConnector var2) {
      if (this.invalid.size() == 0) {
         return true;
      } else {
         boolean var3 = true;
         Iterator var4;
         Entity var5;
         if (var2 instanceof EgyeniVallalkozoMDStoreConnector) {
            var4 = this.invalid.iterator();

            do {
               do {
                  if (!var4.hasNext()) {
                     return var3;
                  }

                  var5 = (Entity)var4.next();
               } while(!var5.getName().equals("Egyéni vállalkozó"));
            } while(!var5.getBlock("Törzsadatok").getMasterData("Adózó adóazonosító jele").getValue().equals(var1) && !var5.getBlock("Törzsadatok").getMasterData("Adózó adószáma").getValue().equals(var1.replaceAll("-", "")));

            var3 = false;
         } else if (var2 instanceof MaganszemelyMDStoreConnector) {
            var4 = this.invalid.iterator();

            while(var4.hasNext()) {
               var5 = (Entity)var4.next();
               if (var5.getName().equals("Magánszemély") && var5.getBlock("Törzsadatok").getMasterData("Adózó adóazonosító jele").getValue().equals(var1)) {
                  var3 = false;
                  break;
               }
            }
         } else if (var2 instanceof TarsasagMDStoreConnector) {
            var4 = this.invalid.iterator();

            while(var4.hasNext()) {
               var5 = (Entity)var4.next();
               if (var5.getName().equals("Társaság") && var5.getBlock("Törzsadatok").getMasterData("Adózó adószáma").getValue().equals(var1.replaceAll("-", ""))) {
                  var3 = false;
                  break;
               }
            }
         }

         return var3;
      }
   }

   private Vector<Entity> convertEntity(Hashtable var1, MDStoreConnector var2, Hashtable var3) throws EntityException {
      Vector var4 = new Vector();
      Iterator var5 = var1.keySet().iterator();

      while(true) {
         String var6;
         do {
            if (!var5.hasNext()) {
               return var4;
            }

            var6 = (String)var5.next();
         } while(!this.hasValidPAConverted(var6, var2));

         Entity var7 = var2.getEntity(var6);
         if (!var7.isEmpty() && this.isOverwrite()) {
            switch(this.isOverWriteable(var7, (Hashtable)var1.get(var6))) {
            case 0:
            default:
               break;
            case 1:
               continue;
            case 2:
               throw new EntityException("Törzsadat importálás megszakítva!");
            }
         }

         Hashtable var8 = (Hashtable)var1.get(var6);
         String var11;
         String var13;
         String var24;
         if (this.isOverwrite()) {
            Iterator var9 = var8.keySet().iterator();

            label115:
            while(true) {
               while(true) {
                  if (!var9.hasNext()) {
                     break label115;
                  }

                  String var10 = (String)var9.next();
                  var11 = (String)var8.get(var10);
                  String[] var12;
                  if ("szamlaszam".equals(var10)) {
                     var12 = var11.split("-");
                     if (var12 == null || var12.length < 2) {
                        var12 = new String[]{"", "", ""};
                     }

                     var13 = "";
                     if (var12.length == 3) {
                        var13 = var12[1] + "-" + var12[2];
                     } else {
                        var13 = var12[1] + "-00000000";
                     }

                     var7.getBlock("Egyéb adatok").getMasterData("Pénzintézet-azonosító").setValue(var12[0]);
                     var7.getBlock("Egyéb adatok").getMasterData("Számla-azonosító").setValue(var13);
                  } else {
                     var12 = var2.map(var10, false);
                     if (!"N/A".equals(var12[0])) {
                        var7.getBlock(var12[0]).getMasterData(var12[1]).setValue(var11);
                     }
                  }
               }
            }
         } else {
            String[] var17 = VPDATA;
            int var19 = var17.length;

            for(int var22 = 0; var22 < var19; ++var22) {
               var24 = var17[var22];
               if (var8.containsKey(var24)) {
                  var13 = (String)var8.get(var24);
                  String[] var14 = var2.map(var24, false);
                  if (!"N/A".equals(var14[0])) {
                     var7.getBlock(var14[0]).getMasterData(var14[1]).setValue(var13);
                  }
               }
            }
         }

         if (var3 != null && !var3.isEmpty()) {
            Hashtable var18 = new Hashtable();
            Iterator var20 = var3.keySet().iterator();

            while(var20.hasNext()) {
               var11 = (String)var20.next();
               var24 = var11.split("=")[0].trim().toLowerCase();
               if (var24.equals(var6)) {
                  var18.put(var11.split("=")[1].trim(), (Hashtable)var3.get(var11));
               }
            }

            if (!var18.isEmpty()) {
               TreeSet var21 = new TreeSet(var18.keySet());
               Iterator var23 = var21.iterator();

               while(var23.hasNext()) {
                  var24 = (String)var23.next();
                  Iterator var26 = ((Hashtable)var18.get(var24)).keySet().iterator();

                  while(var26.hasNext()) {
                     String var25 = (String)var26.next();
                     String var15 = (String)((Hashtable)var18.get(var24)).get(var25);
                     String[] var16 = var2.map(var25, true);
                     if (!"N/A".equals(var16[0])) {
                        var7.getBlock(var16[0], Integer.parseInt(var24)).getMasterData(var16[1]).setValue(var15);
                     }
                  }
               }
            }
         }

         var4.add(var7);
      }
   }

   protected Vector<Entity> convertToEntity(Vector var1, Hashtable var2) {
      Vector var3 = new Vector();

      try {
         for(int var4 = 0; var4 < var1.size(); ++var4) {
            switch(var4) {
            case 0:
               var3.addAll(this.convertEntity((Hashtable)var1.get(var4), this.getMasterDataMapperForType("Társaság"), var2));
               break;
            case 1:
               var3.addAll(this.convertEntity((Hashtable)var1.get(var4), this.getMasterDataMapperForType("Egyéni vállalkozó"), var2));
               break;
            case 2:
               var3.addAll(this.convertEntity((Hashtable)var1.get(var4), this.getMasterDataMapperForType("Magánszemély"), var2));
               break;
            case 3:
               var3.addAll(this.convertEntity((Hashtable)var1.get(var4), this.getMasterDataMapperForType("Adótanácsadó"), var2));
            }
         }
      } catch (EntityException var5) {
         ErrorList.getInstance().writeError("WinABevMDCopy", var5.getMessage(), var5, (Object)null);
      }

      if (var3.size() > 0) {
         this.saveEntities((Entity[])var3.toArray(new Entity[var3.size()]));
      }

      return var3;
   }

   private void saveEntities(Entity[] var1) {
      EntityHome var2 = null;
      boolean var3 = false;

      try {
         var2 = new EntityHome();
         var2.startSession();
         Entity[] var4 = var1;
         int var5 = var1.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Entity var7 = var4[var6];

            try {
               var2.update(var7);
            } catch (EntityException var28) {
               EntityError[] var9 = var7.getValidityStatus();
               StringBuilder var10 = new StringBuilder(var7.toString());
               EntityError[] var11 = var9;
               int var12 = var9.length;

               for(int var13 = 0; var13 < var12; ++var13) {
                  EntityError var14 = var11[var13];
                  var10.append(var14);
               }

               ErrorList.getInstance().writeError("WinABevMDCopy", var10.toString(), var28, (Object)null);
            }
         }
      } catch (EntityException var29) {
         var3 = true;
         ErrorList.getInstance().writeError("WinABevMDCopy", var29.getMessage(), var29, (Object)null);
      } finally {
         try {
            if (var2 != null && !var3) {
               var2.closeSession();
            }
         } catch (EntityException var27) {
            var3 = true;
            ErrorList.getInstance().writeError("WinABevMDCopy", var27.getMessage(), var27, (Object)null);
         }

         if (var2 != null && var3) {
            try {
               var2.abortSession();
            } catch (Exception var26) {
               var26.printStackTrace();
            }
         }

      }

   }

   private void start_(int var1) {
      this.error_list = new Vector(256);
      File[] var3 = this.getABevPAFile(var1);
      boolean var4 = false;
      if (var3 != null) {
         int var5 = 0;

         for(int var6 = var3.length; var5 < var6; ++var5) {
            File var2 = var3[var5];
            if (var2 != null) {
               try {
                  Hashtable var7 = new Hashtable();
                  Vector var8 = this.loadWinAbevDataFile(var2);
                  if (var2.getName().toLowerCase().equals("torzs.dtt")) {
                     this.loadSites(var2.getParent() + "\\thely.dtt", var7);
                  }

                  this.installErrorListener();
                  this.convertToEntity(var8, var7);
                  this.uninstallErrorListener();
               } catch (Exception var13) {
                  var13.printStackTrace();
                  this.uninstallErrorListener();
               }
            }
         }
      }

      Iterator var14 = this.invalid.iterator();

      while(var14.hasNext()) {
         Entity var15 = (Entity)var14.next();
         StringBuilder var16 = new StringBuilder();
         if (var15.getName().equals("Társaság")) {
            var16.append("Társaság [adószám:");
            var16.append(this.formattedTaxId(var15.getBlock("Törzsadatok").getMasterData("Adózó adószáma").getValue()));
            var16.append("] ");
         } else if (var15.getName().equals("Magánszemély")) {
            var16.append("Magánszemély [adóaz.jel:");
            var16.append(var15.getBlock("Törzsadatok").getMasterData("Adózó adóazonosító jele").getValue());
            var16.append("] ");
         } else if (var15.getName().equals("Egyéni vállalkozó")) {
            var16.append("Egyéni vállalkozó [adóaz.jel:");
            var16.append(var15.getBlock("Törzsadatok").getMasterData("Adózó adóazonosító jele").getValue());
            var16.append(", adószám:");
            var16.append(this.formattedTaxId(var15.getBlock("Törzsadatok").getMasterData("Adózó adószáma").getValue()));
            var16.append("] ");
         }

         this.error_list.add(var16.toString() + "A részleteket megtekintheti a Szerviz->Üzenetek menüben!");
         EntityError[] var9 = var15.getValidityStatus();
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            EntityError var12 = var9[var11];
            var16.append("\t");
            var16.append(var12.getErrorMsg());
            ErrorList.getInstance().store(ErrorList.LEVEL_SHOW_WARNING, var16.toString(), (Exception)null, (Object)null);
         }
      }

      if (this.error_list.size() > 0) {
         this.showErrorList();
      } else if (var3 != null && var3.length > 0) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A törzsadat importálás sikeresen befejeződött!");
      }

   }

   private String formattedTaxId(String var1) {
      return var1.substring(0, 8) + "-" + var1.substring(8, 9) + "-" + var1.substring(9, var1.length());
   }

   private void loadSites(String var1, Hashtable var2) {
      try {
         BufferedReader var3 = new BufferedReader(new InputStreamReader(new FileInputStream(var1), "ISO-8859-2"));
         String var5 = "";

         String var4;
         while((var4 = var3.readLine()) != null) {
            var4 = var4.trim();
            if (var4.length() > 0) {
               if (var4.startsWith("[")) {
                  var5 = var4.substring(1, var4.length() - 1);
                  var2.put(var5, new Hashtable());
               } else {
                  String[] var6 = var4.split("=", 2);
                  if (var6.length == 2) {
                     ((Hashtable)var2.get(var5)).put(var6[0], var6[1]);
                  }
               }
            }
         }

         var3.close();
      } catch (IOException var7) {
         ErrorList.getInstance().writeError("WinABevMDCopy", var7.getMessage(), var7, (Object)null);
      }

   }

   private Hashtable handleSameRecords(Hashtable var1, Hashtable var2) {
      return var2;
   }

   private MDStoreConnector getMasterDataMapperForType(String var1) {
      if ("Társaság".equals(var1)) {
         return new TarsasagMDStoreConnector();
      } else if ("Egyéni vállalkozó".equals(var1)) {
         return new EgyeniVallalkozoMDStoreConnector();
      } else if ("Magánszemély".equals(var1)) {
         return new MaganszemelyMDStoreConnector();
      } else {
         return "Adótanácsadó".equals(var1) ? new AdotanacsadoMDStoreConnector() : null;
      }
   }

   private int isOverWriteable(Entity var1, Hashtable var2) {
      return WinABevMDCopy.DOptionPane.show(var1, var2);
   }

   private void installErrorListener() {
      ((IEventSupport)ErrorList.getInstance()).addEventListener(this);
   }

   private void uninstallErrorListener() {
      ((IEventSupport)ErrorList.getInstance()).removeEventListener(this);
   }

   public Object eventFired(Event var1) {
      Object var2 = var1.getUserData();
      if (var2 instanceof Hashtable) {
         var2 = ((Hashtable)var2).get("item");
         if (var2 instanceof Object[]) {
            Object[] var3 = (Object[])((Object[])var2);
            if ("WinABevMDCopy".equals(var3[0])) {
               this.error_list.add(var3[1]);
            }
         }
      }

      return null;
   }

   private void showErrorList() {
      new ErrorDialog(MainFrame.thisinstance, "Átemelés közben történt hibák", true, false, this.error_list);
   }

   private static class DOptionPane {
      private static int a;

      static int show(Entity var0, Hashtable var1) {
         a = -1;
         JDialog var2 = getDialog(var0, var1);
         var2.pack();
         var2.setResizable(false);
         var2.setLocationRelativeTo(MainFrame.thisinstance);
         var2.setModal(true);
         var2.setVisible(true);
         var2.dispose();
         return a;
      }

      private static JDialog getDialog(Entity var0, Hashtable var1) {
         String var2 = "Átemelés során azonos bejegyzést talált a program:";
         var2 = var2 + "\n\nEredeti:";
         var2 = var2 + "\n\n" + getData(var1, var0.getName());
         var2 = var2 + "\n\nÚj:";
         var2 = var2 + "\n\n" + getData(var0);
         final JDialog var3 = new JDialog(MainFrame.thisinstance);
         JTextArea var4 = new JTextArea();
         var4.setLineWrap(false);
         var4.setWrapStyleWord(true);
         var4.setEditable(false);
         var4.setForeground((Color)UIManager.get("Label.foreground"));
         var4.setBackground((Color)UIManager.get("Label.background"));
         var4.setFont(UIManager.getFont("Button.font"));
         var4.setFocusable(false);
         var4.setText(var2);
         JPanel var5 = new JPanel();
         var5.setLayout(new BorderLayout());
         var5.add(var4, "Center");
         var5.add(Box.createVerticalStrut(10), "North");
         JLabel var6 = new JLabel("  ", UIManager.getIcon("OptionPane.questionIcon"), 2);
         var6.setVerticalAlignment(1);
         var5.add(var6, "West");
         var5.add(Box.createHorizontalStrut(10), "East");
         var5.add(Box.createVerticalStrut(5), "South");
         JButton var7 = new JButton("Felülírás");
         var7.setDefaultCapable(true);
         JButton var8 = new JButton("Kihagyás");
         JButton var9 = new JButton("Áttöltés vége");
         JPanel var10 = new JPanel();
         var10.setLayout(new FlowLayout(1, 5, 5));
         var10.add(var7);
         var10.add(var8);
         var10.add(var9);
         JPanel var11 = new JPanel();
         var11.setLayout(new BorderLayout());
         var11.add(var5, "Center");
         var11.add(var10, "South");
         Container var12 = var3.getContentPane();
         var12.setLayout(new BorderLayout());
         var12.add(var11, "Center");
         var12.add(Box.createHorizontalStrut(10), "West");
         var3.setTitle("Azonos bejegyzések kezelése ...");
         var3.getRootPane().setDefaultButton(var7);
         var7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               WinABevMDCopy.DOptionPane.handleButtonEvent(0, var3);
            }
         });
         var8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               WinABevMDCopy.DOptionPane.handleButtonEvent(1, var3);
            }
         });
         var9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               WinABevMDCopy.DOptionPane.handleButtonEvent(2, var3);
            }
         });
         return var3;
      }

      private static void handleButtonEvent(int var0, JDialog var1) {
         a = var0;
         var1.setVisible(false);
      }

      private static String getData(Entity var0) {
         StringBuffer var1 = new StringBuffer();
         if ("Társaság".equals(var0.getName())) {
            var1.append(var0.getBlock("Törzsadatok").getMasterData("Adózó neve").getValue());
            var1.append(" ");
            var1.append(var0.getBlock("Törzsadatok").getMasterData("Adózó adószáma").getValue());
            var1.append("\n");
            var1.append(var0.getBlock("Állandó cím").getMasterData("Irányítószám").getValue());
            var1.append(" ");
            var1.append(var0.getBlock("Állandó cím").getMasterData("Település").getValue());
            var1.append("\n");
            var1.append(var0.getBlock("Állandó cím").getMasterData("Közterület neve").getValue());
            var1.append(" ");
            var1.append(var0.getBlock("Állandó cím").getMasterData("Közterület jellege").getValue());
            var1.append(" ");
            var1.append(var0.getBlock("Állandó cím").getMasterData("Házszám").getValue());
            var1.append("\n");
         } else if ("Egyéni vállalkozó".equals(var0.getName())) {
            var1.append(var0.getBlock("Törzsadatok").getMasterData("Vezetékneve").getValue());
            var1.append(" ");
            var1.append(var0.getBlock("Törzsadatok").getMasterData("Keresztneve").getValue());
            var1.append(" ");
            var1.append(var0.getBlock("Törzsadatok").getMasterData("Adózó adószáma").getValue());
            var1.append(" ");
            var1.append(var0.getBlock("Törzsadatok").getMasterData("Adózó adóazonosító jele").getValue());
            var1.append("\n");
            var1.append(var0.getBlock("Állandó cím").getMasterData("Irányítószám").getValue());
            var1.append(" ");
            var1.append(var0.getBlock("Állandó cím").getMasterData("Település").getValue());
            var1.append("\n");
            var1.append(var0.getBlock("Állandó cím").getMasterData("Közterület neve").getValue());
            var1.append(" ");
            var1.append(var0.getBlock("Állandó cím").getMasterData("Közterület jellege").getValue());
            var1.append(" ");
            var1.append(var0.getBlock("Állandó cím").getMasterData("Házszám").getValue());
            var1.append("\n");
         } else if ("Magánszemély".equals(var0.getName())) {
            var1.append(var0.getBlock("Törzsadatok").getMasterData("Vezetékneve").getValue());
            var1.append(" ");
            var1.append(var0.getBlock("Törzsadatok").getMasterData("Keresztneve").getValue());
            var1.append(" ");
            var1.append(var0.getBlock("Törzsadatok").getMasterData("Adózó adóazonosító jele").getValue());
            var1.append("\n");
            var1.append(var0.getBlock("Állandó cím").getMasterData("Irányítószám").getValue());
            var1.append(" ");
            var1.append(var0.getBlock("Állandó cím").getMasterData("Település").getValue());
            var1.append("\n");
            var1.append(var0.getBlock("Állandó cím").getMasterData("Közterület neve").getValue());
            var1.append(" ");
            var1.append(var0.getBlock("Állandó cím").getMasterData("Közterület jellege").getValue());
            var1.append(" ");
            var1.append(var0.getBlock("Állandó cím").getMasterData("Házszám").getValue());
            var1.append("\n");
         } else if ("Adótanácsadó".equals(var0.getName())) {
            var1.append(var0.getBlock("Név").getMasterData("Adótanácsadó neve").getValue());
            var1.append("\n");
            var1.append(var0.getBlock("Név").getMasterData("Adótanácsadó azonositószáma").getValue());
            var1.append("\n");
            var1.append(var0.getBlock("Név").getMasterData("Adótanácsadó bizonyítvány").getValue());
            var1.append("\n");
         } else {
            var1.append("(Nincs adat)");
         }

         return var1.toString();
      }

      private static String getData(Hashtable var0, String var1) {
         StringBuffer var2 = new StringBuffer();
         if ("Társaság".equals(var1)) {
            var2.append(getString(var0.get("nev")));
            var2.append(" ");
            var2.append(getString(var0.get("adoszam")));
            var2.append("\n");
            var2.append(getString(var0.get("irszam")));
            var2.append(" ");
            var2.append(getString(var0.get("varos")));
            var2.append("\n");
            var2.append(getString(var0.get("utca")));
            var2.append(" ");
            var2.append(getString(var0.get("kozt")));
            var2.append(" ");
            var2.append(getString(var0.get("hazszam")));
            var2.append("\n");
         } else if ("Egyéni vállalkozó".equals(var1)) {
            var2.append(getString(var0.get("vnev")));
            var2.append(" ");
            var2.append(getString(var0.get("knev")));
            var2.append(" ");
            var2.append(getString(var0.get("adoszam")));
            var2.append(" ");
            var2.append(getString(var0.get("adoazonosito")));
            var2.append("\n");
            var2.append(getString(var0.get("irszam")));
            var2.append(" ");
            var2.append(getString(var0.get("varos")));
            var2.append("\n");
            var2.append(getString(var0.get("utca")));
            var2.append(" ");
            var2.append(getString(var0.get("kozt")));
            var2.append(" ");
            var2.append(getString(var0.get("hazszam")));
            var2.append("\n");
         } else if ("Magánszemély".equals(var1)) {
            var2.append(getString(var0.get("vnev")));
            var2.append(" ");
            var2.append(getString(var0.get("knev")));
            var2.append(" ");
            var2.append(getString(var0.get("adoazonosito")));
            var2.append("\n");
            var2.append(getString(var0.get("irszam")));
            var2.append(" ");
            var2.append(getString(var0.get("varos")));
            var2.append("\n");
            var2.append(getString(var0.get("utca")));
            var2.append(" ");
            var2.append(getString(var0.get("kozt")));
            var2.append(" ");
            var2.append(getString(var0.get("hazszam")));
            var2.append("\n");
         } else if ("Adótanácsadó".equals(var1)) {
            var2.append(getString(var0.get("neve")));
            var2.append("\n");
            var2.append(getString(var0.get("azonosito")));
            var2.append("\n");
            var2.append(getString(var0.get("bizonyitvany")));
            var2.append("\n");
         } else {
            var2.append("(Nincs adat)");
         }

         return var2.toString();
      }

      private static String getString(Object var0) {
         return var0 == null ? "" : var0.toString();
      }
   }
}
