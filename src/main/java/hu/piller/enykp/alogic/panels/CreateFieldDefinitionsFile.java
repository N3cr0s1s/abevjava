package hu.piller.enykp.alogic.panels;

import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.metainfo.MetaStore;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.gui.model.VisualFieldModel;
import hu.piller.enykp.interfaces.ICommandObject;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicFileChooserUI;

public class CreateFieldDefinitionsFile implements ICommandObject {
   private static CreateFieldDefinitionsFile instance;
   private static final String CHAR_SET = "ISO-8859-2";
   private final Vector cmd_list = new Vector(Arrays.asList("abev.createFieldDefinitionsFile"));
   Boolean[] states;

   private CreateFieldDefinitionsFile() {
      this.states = new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
      this.build();
      this.prepare();
   }

   private void build() {
   }

   private void prepare() {
   }

   public static CreateFieldDefinitionsFile getInstance() {
      if (instance == null) {
         instance = new CreateFieldDefinitionsFile();
      }

      return instance;
   }

   public void execute() {
      JFileChooser var1 = new JFileChooser();
      var1.setDialogTitle("Mező definíciós file létrehozása");

      try {
         if (var1.getName() == null) {
            try {
               ((BasicFileChooserUI)((BasicFileChooserUI)var1.getUI())).setFileName("mdf.csv");
            } catch (ClassCastException var6) {
               try {
                  var1.setSelectedFile(new File("mdf.csv"));
               } catch (Exception var5) {
                  Tools.eLog(var5, 0);
               }
            }
         }

         var1.setCurrentDirectory(new File((String)PropertyList.getInstance().get("prop.usr.naplo")));
      } catch (Exception var7) {
         Tools.eLog(var7, 0);
      }

      while(var1.showSaveDialog(MainFrame.thisinstance) == 0) {
         File var2 = var1.getSelectedFile();
         if (var2 == null) {
            break;
         }

         if (!var2.getName().endsWith(".csv")) {
            var2 = new File(var2.getPath() + ".csv");
         }

         if (!var2.exists() || JOptionPane.showConfirmDialog(MainFrame.thisinstance, var2.getName() + " létezik !\nFelülírja ?", "Mező definíciós állomány készítése", 0) != 1) {
            try {
               this.createCSVFile(var2);
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Meződefiníciós állomány elkészült.\n(Létrehozott állomány: " + var2.getName() + ")", "Üzenet", 1);
            } catch (Exception var4) {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, "Meződefiníciós állomány készítésekor hiba történt !", "Hiba", 0);
            }
            break;
         }
      }

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

   private void createCSVFile(File var1) throws Exception {
      BufferedWriter var2 = null;

      try {
         var2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var1), "ISO-8859-2"));
         this.writeData(var2);
         var2.flush();
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var9) {
               Tools.eLog(var9, 0);
            }
         }

      }

   }

   private void writeData(BufferedWriter var1) {
      if (var1 != null) {
         try {
            var1.write("Nyomtatvány,Kód,Adóazon,Adattípus,Max_hossz,Lap,Sor,Oszlop,Dinamikus,EAzon");
            var1.newLine();
            MetaInfo var2 = MetaInfo.getInstance();
            Enumeration var3 = var2.getMetaStoresIds();
            BookModel var10 = Calculator.getInstance().getBookModel();

            while(true) {
               String var4;
               MetaStore var9;
               do {
                  if (!var3.hasMoreElements()) {
                     return;
                  }

                  var9 = var2.getMetaStore(var4 = (String)var3.nextElement());
               } while(var9 == null);

               Vector var11 = var9.getFieldIds();
               FormModel var14 = var10.get(var4);
               int var15 = 0;

               for(int var16 = var11.size(); var15 < var16; ++var15) {
                  Object var12 = var11.get(var15);
                  String var5 = ((PageModel)var14.fids_page.get(var12)).name;
                  if (var14.fids_page.get(var12) == null || ((PageModel)var14.fids_page.get(var12)).role != 0) {
                     String var7 = ((PageModel)var14.fids_page.get(var12)).dynamic ? "T" : "F";
                     DataFieldModel var17 = (DataFieldModel)var14.fids.get(var12);
                     if (var17 == null || (var17.role & VisualFieldModel.getmask()) != 0) {
                        String var8 = (String)var17.features.get("max_length");
                        Map var13 = var9.getFieldMetas(var12);
                        Hashtable var18 = (Hashtable)var9.getFieldMetas(var12);
                        String var6 = (String)var18.get("type");
                        if (var6 != null) {
                           String var20 = "";

                           int var19;
                           try {
                              var19 = Integer.parseInt(var6.toString());
                           } catch (NumberFormatException var22) {
                              var19 = 0;
                           }

                           if ((var19 & 1) == 1) {
                              var20 = var20 + "S";
                           }

                           if ((var19 & 2) == 2) {
                              var20 = var20 + "N";
                           }

                           if ((var19 & 4) == 4) {
                              var20 = var20 + "L";
                           }

                           var6 = var20;
                        }

                        var1.write(this.getString(var4, (String)null) + ",");
                        var1.write(this.getString(var13.get("did"), (String)null) + ",");
                        var1.write(this.getString(var13.get("vid"), (String)null) + ",");
                        var1.write(this.getString(var6, (String)null) + ",");
                        var1.write(this.getString("-1".equals(var8) ? "0" : var8, "") + ",");
                        var1.write(this.getString(var5, (String)null) + ",");
                        var1.write(this.getString(var13.get("row"), (String)null) + ",");
                        var1.write(this.getString(var13.get("col"), (String)null) + ",");
                        var1.write(this.getString(var7, (String)null) + ",");
                        var1.write(this.getString(var13.get("fid"), (String)null));
                        var1.newLine();
                     }
                  }
               }
            }
         } catch (IOException var23) {
            Tools.eLog(var23, 0);
         }
      }

   }

   private String getString(Object var1, String var2) {
      return var1 == null ? (var2 == null ? "" : var2) : var1.toString();
   }
}
