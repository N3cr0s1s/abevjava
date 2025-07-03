package hu.piller.enykp.alogic.filesaver.enykinner;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.calculator.lookup.LookupList;
import hu.piller.enykp.alogic.calculator.lookup.LookupListHandler;
import hu.piller.enykp.alogic.filesaver.xml.StoreItemComparator;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.metainfo.MetaStore;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.util.base.Tools;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EnykAutoFill {
   private BookModel bm;
   private static Random r = new Random(System.currentTimeMillis());
   private static final int MAX_CHAR_LENGTH = 10;
   private StoreItemComparator sic = new StoreItemComparator();
   private static final String ABC = "aAáÁbBcCdDeEéÉfFgGhHiIíÍjJkKlLmMnNoOóÓöÖőŐpPqQrRsStTuUúÚüÜűŰvVwWxXyYzZ0123456789";
   private static final String NUMBERS = "0123456789";
   private static final int YEAR_MIN = 1930;

   public EnykAutoFill(BookModel var1) {
      this.bm = var1;
   }

   public BookModel fill() {
      CalculatorManager var1 = CalculatorManager.getInstance();

      try {
         if (this.bm.get_main_index() == -1) {
            throw new Exception("A nyomtatványban nincs fődokumentum");
         }

         this.bm.setCalcelemindex(this.bm.get_main_index());

         for(int var2 = 0; var2 < this.bm.cc.size(); ++var2) {
            Elem var3 = (Elem)this.bm.cc.get(var2);
            this.bm.setCalcelemindex(var2);
            this.bm.cc.setActiveObject(var3);
            this.fillData(var3);
            CalculatorManager.getInstance().doBetoltErtekCalcs(true);
            CalculatorManager.getInstance().multi_form_load();

            try {
               var1.form_hidden_fields_calc();
            } catch (Exception var6) {
               Tools.eLog(var6, 0);
            }

            try {
               var1.form_notbelfeld_fields_calc();
            } catch (Exception var5) {
               Tools.eLog(var5, 0);
            }
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      return this.bm;
   }

   private void fillData(Elem var1) throws IOException {
      String var2 = var1.getType();
      Hashtable var3 = this.bm.get(var2).fids;
      MetaInfo var4 = MetaInfo.getInstance();
      MetaStore var5 = var4.getMetaStore(var2);
      Hashtable var6 = var5.getFieldMetas();
      FormModel var7 = this.bm.get(var1.getType());
      Enumeration var8 = var3.keys();

      while(true) {
         String var10;
         while(true) {
            do {
               if (!var8.hasMoreElements()) {
                  return;
               }

               var10 = (String)var8.nextElement();
            } while(var10.length() < 8);

            try {
               PageModel var9 = var7.get(var7.get_field_pageindex(var10));
               if (var9.role == 0) {
                  continue;
               }
               break;
            } catch (Exception var12) {
               System.out.println("autoFill hiba (" + var10 + "): " + var12.toString());
            }
         }

         String var11 = this.createRandomValue((Hashtable)var6.get(var10), ((DataFieldModel)var3.get(var10)).features, var7.irids, var10, this.bm.getIndex(var7));
         ((IDataStore)var1.getRef()).set("0_" + var10, var11);
      }
   }

   private String createRandomValue(Hashtable var1, Hashtable var2, Hashtable var3, String var4, int var5) {
      if (!var1.get("copy_fld").equals("")) {
         return "";
      } else {
         String var6 = (String)var2.get("datatype");
         if (var6.equals("check")) {
            return "true";
         } else {
            String[] var7 = (String[])((String[])var2.get("spvalues"));
            if (var7 != null) {
               return var7[0];
            } else {
               String var8 = (String)var2.get("type");
               String var9 = (String)var3.get(var1.get("irids"));
               String var10 = (String)var2.get("abev_mask");
               String var11 = (String)var2.get("max_length");
               byte var12 = 0;
               if (var8.equals("1")) {
                  var12 = 1;
               }

               if (var8.equals("2")) {
                  var12 = 0;
               }

               if (var6.equals("date")) {
                  var12 = 2;
               }

               if ("combo".equals(var6) || "tcombo".equals(var6)) {
                  String var13 = this.handleLookupList(var4, var5);
                  if (var13 != null) {
                     return var13;
                  }
               }

               switch(var12) {
               case 0:
                  return this.createNumber(var11, r, "-".matches(var9));
               case 2:
                  return this.createDate(var10, r);
               default:
                  return this.createString(var11, var9, var10);
               }
            }
         }
      }
   }

   private String createNumber(String var1, Random var2, boolean var3) {
      int var4 = Math.min(10, Integer.parseInt(var1));
      if (var4 < 0) {
         var4 = 10;
      }

      StringBuilder var5 = new StringBuilder(var4);

      for(int var6 = 0; var6 < var4; ++var6) {
         var5.append("0123456789".charAt(var2.nextInt("0123456789".length())));
      }

      if (var5.charAt(0) == '0') {
         if (var3) {
            var5.replace(0, 1, "-");
         } else {
            var5.replace(0, 1, "8");
         }
      }

      return var5.toString();
   }

   private String createDate(String var1, Random var2) {
      int var3 = 1930 + var2.nextInt(Calendar.getInstance().get(1) - 1930);
      int var4 = var2.nextInt(11) + 1;
      String var5 = "0" + var4;
      var5 = var5.substring(var5.length() - 2);
      int var6 = var2.nextInt(27) + 1;
      String var7 = "0" + var6;
      var7 = var7.substring(var7.length() - 2);
      String var8 = "" + var3 + var5 + var7;
      StringBuilder var9 = new StringBuilder();
      var1 = var1.replaceAll("\\.", "");
      var1 = var1.replaceAll("-", "");
      if (var8.length() < var1.length()) {
         var8 = var8 + "11111111111111111111";
      }

      for(int var10 = 0; var10 < var1.length(); ++var10) {
         if (var1.charAt(var10) == '#') {
            var9.append(var8.charAt(var10));
         } else {
            var9.append(var1.charAt(var10));
         }
      }

      return var9.toString();
   }

   private String createString(String var1, String var2, String var3) {
      StringBuilder var4 = new StringBuilder();
      var3 = var3.replaceAll("-", "");
      var3 = var3.replaceAll("\\.", "");
      var3 = var3.replaceAll("\\\\", "");
      var3 = var3.replaceAll("/", "");
      int var5;
      if (var3.indexOf("#") > -1) {
         for(var5 = 0; var5 < var3.length(); ++var5) {
            if (var3.charAt(var5) == '#') {
               var4.append(this.getCharFromABC(var2));
            } else {
               var4.append(var3.charAt(var5));
            }
         }
      } else {
         var5 = Integer.parseInt(var1);
         int var6;
         if (" ".matches(var2)) {
            for(var6 = 0; var6 < var5; ++var6) {
               var4.append(this.getCharFromABC(var2, var6));
            }
         } else {
            for(var6 = 0; var6 < var5; ++var6) {
               var4.append(this.getCharFromABC(var2));
            }
         }
      }

      return var4.toString();
   }

   private char getCharFromABC(String var1, int var2) {
      if (var2 > 0 && (var2 + "").endsWith("0")) {
         return ' ';
      } else {
         char var3;
         for(var3 = "aAáÁbBcCdDeEéÉfFgGhHiIíÍjJkKlLmMnNoOóÓöÖőŐpPqQrRsStTuUúÚüÜűŰvVwWxXyYzZ0123456789".charAt(r.nextInt("aAáÁbBcCdDeEéÉfFgGhHiIíÍjJkKlLmMnNoOóÓöÖőŐpPqQrRsStTuUúÚüÜűŰvVwWxXyYzZ0123456789".length())); !("" + var3).matches(var1); var3 = "aAáÁbBcCdDeEéÉfFgGhHiIíÍjJkKlLmMnNoOóÓöÖőŐpPqQrRsStTuUúÚüÜűŰvVwWxXyYzZ0123456789".charAt(r.nextInt("aAáÁbBcCdDeEéÉfFgGhHiIíÍjJkKlLmMnNoOóÓöÖőŐpPqQrRsStTuUúÚüÜűŰvVwWxXyYzZ0123456789".length()))) {
         }

         return var3;
      }
   }

   private char getCharFromABC(String var1) {
      char var2;
      for(var2 = "aAáÁbBcCdDeEéÉfFgGhHiIíÍjJkKlLmMnNoOóÓöÖőŐpPqQrRsStTuUúÚüÜűŰvVwWxXyYzZ0123456789".charAt(r.nextInt("aAáÁbBcCdDeEéÉfFgGhHiIíÍjJkKlLmMnNoOóÓöÖőŐpPqQrRsStTuUúÚüÜűŰvVwWxXyYzZ0123456789".length())); !("" + var2).matches(var1); var2 = "aAáÁbBcCdDeEéÉfFgGhHiIíÍjJkKlLmMnNoOóÓöÖőŐpPqQrRsStTuUúÚüÜűŰvVwWxXyYzZ0123456789".charAt(r.nextInt("aAáÁbBcCdDeEéÉfFgGhHiIíÍjJkKlLmMnNoOóÓöÖőŐpPqQrRsStTuUúÚüÜűŰvVwWxXyYzZ0123456789".length()))) {
      }

      return var2;
   }

   public static int showInfoAlert() {
      final int[] var0 = new int[]{-1};
      final String[] var1 = new String[]{"autofill.dontShowAgain"};
      final String[] var2 = new String[]{"gui"};
      String[] var3 = Tools.loadSettings(var2, var1);
      if (var3[0] != null && var3[0].equals("i")) {
         return 0;
      } else {
         final JDialog var4 = new JDialog(MainFrame.thisinstance, "Tesztadatok!", true);
         String var5 = "<html>Ezzel a művelettel, a következő lépésben kiválasztandó nyomtatványt, véletlenszerű tesztadatokkal töltheti fel<br>Folytatja a műveletet?</html>";
         JLabel var6 = new JLabel(var5);
         JPanel var7 = new JPanel(new BorderLayout(10, 10));
         var7.add(var6, "Center");
         JPanel var8 = new JPanel();
         var7.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
         final JCheckBox var9 = GuiUtil.getANYKCheckBox("Ez a figyelmeztetés többé ne jelenjen meg!");
         var8.add(var9, "North");
         var9.setSize(320, 40);
         var9.setPreferredSize(var9.getSize());
         JPanel var10 = new JPanel();
         JButton var11 = new JButton("Igen");
         var11.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1x) {
               Tools.saveSettings(var2, var1, new String[]{var9.isSelected() ? "i" : "n"});
               var4.setVisible(false);
               var4.dispose();
               var0[0] = 0;
            }
         });
         JButton var12 = new JButton("Nem");
         var12.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               var4.setVisible(false);
               var4.dispose();
               var0[0] = -1;
            }
         });
         var10.add(var11);
         var10.add(var12);
         var8.add(var10, "South");
         var8.setSize(350, 90);
         var8.setPreferredSize(var8.getSize());
         var7.add(var8, "South");
         var4.getContentPane().add(var7);
         var4.setSize(350, 220);
         var4.setLocationRelativeTo(MainFrame.thisinstance);
         var4.setResizable(false);
         var4.setDefaultCloseOperation(2);
         var4.setVisible(true);
         return var0[0];
      }
   }

   private String handleLookupList(String var1, int var2) {
      try {
         String var3 = ((Elem)this.bm.cc.get(var2)).getType();
         Hashtable var4 = this.bm.get(var3).fids;
         DataFieldModel var5 = (DataFieldModel)var4.get(var1);
         LookupList var6 = LookupListHandler.getInstance().getLookupListProvider(var5.formmodel.id, (String)var5.features.get("fid")).getSortedTableView(1);
         return (String)var6.getErtekek().get(0);
      } catch (Exception var7) {
         return null;
      }
   }
}
