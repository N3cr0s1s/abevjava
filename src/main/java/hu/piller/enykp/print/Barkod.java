package hu.piller.enykp.print;

import com.java4less.rbarcode.BarCode2D;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.util.base.Tools;
import hu.piller.tools.bzip2.CBZip2OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.Deflater;

public class Barkod {
   public static final int BARCODE_DATA_MAX_BYTES = 500;
   public static final int MAX_BARKOD_ON_PORTRAIT = 2;
   public static final int MAX_BARKOD_ON_LANDSCAPE = 3;
   public static final int MAX_BARKOD_ON_EXTRACT = 10;
   public static final int BARKOD_HEIGHT = 165;

   public void setDocinfo_ver(String var1) {
   }

   public String nyomtatvanyAdatok(Lap[] var1) {
      String var2 = "";

      for(int var4 = 0; var4 < var1.length; ++var4) {
         LapMetaAdat var3 = var1[var4].getLma();
         if (var3.lapSzam <= 0 && !var3.onlyInBrowser && var3.printable) {
            var2 = var2 + ",";
            if (var3.printable_condition != null && !var3.printable_condition.equalsIgnoreCase("none")) {
               if (var3.printable_condition.equalsIgnoreCase("notdisabled")) {
                  if (!var3.isGuiEnabled) {
                     var2 = var2 + "0";
                  } else if (var3.dinamikusE) {
                     if (var3.maxLapszam > 1) {
                        var2 = var2 + var3.maxLapszam;
                     } else {
                        var2 = var2 + (var3.vanKitoltottMezo ? "1" : "0");
                     }
                  } else {
                     var2 = var2 + (var1[var4].getLma().vanKitoltottMezo ? "1" : "0");
                  }
               }
            } else {
               var2 = var2 + (var3.dinamikusE ? "" + var3.maxLapszam : "1");
            }
         }
      }

      var2 = var2.equals("") ? "LAPOK=" + var2 + "\r\n" : "LAPOK=" + var2.substring(1) + "\r\n";
      return var2;
   }

   public void adatokALapokrol(Lap[] var1, BookModel var2, String var3, int var4) throws Exception {
      IDataStore var5 = (IDataStore)((Elem)var2.cc.get(var4)).getRef();
      FormModel var6 = var2.get(((Elem)var2.cc.get(var4)).getType());
      int[] var7 = (int[])((int[])((Elem)var2.cc.get(var4)).getEtc().get("pagecounts"));
      Hashtable var8 = var2.get(var3).fids;
      int var9 = -1;

      for(int var10 = 0; var10 < var7.length; ++var10) {
         PageModel var11 = var6.get(var10);
         Vector var12 = var11.y_sorted_df;

         for(int var13 = 0; var13 < var7[var10]; ++var13) {
            ++var9;
            Lap var14 = var1[var9];
            var14.getLma().vanKitoltottMezo = false;
            String var15 = "";
            Vector var18 = new Vector();

            int var19;
            for(var19 = 0; var19 < var12.size(); ++var19) {
               DataFieldModel var20 = (DataFieldModel)var12.get(var19);
               var18.add(var13 + "_" + var20.key);
            }

            Collections.sort(var18);

            for(var19 = 0; var19 < var18.size() && var18.size() > 1; ++var19) {
               String var25 = var5.get(var18.get(var19));
               if (var25 != null && !var25.equals("")) {
                  String var21 = ((String)var18.get(var19)).substring((var13 + "_").length());

                  DataFieldModel var22;
                  try {
                     var22 = (DataFieldModel)var8.get(var21);
                     if (var22 == null) {
                        continue;
                     }
                  } catch (Exception var24) {
                     continue;
                  }

                  if (var6.attribs.containsKey("line_break") && "0".equals(var6.attribs.get("line_break")) && (var22.type == 3 || var22.type == 8)) {
                     var25 = var25.replaceAll("\r\n", "\n");
                     var25 = var25.replaceAll("\n", "\r\n#@+=");
                  }

                  Hashtable var23 = var22.features;
                  MezoMetaAdatok var17 = MezoMetaAdatok.getInstance();
                  Utils.mezoMetaAdatokFeltoltese(var17, var23);
                  if (var25.equals("true")) {
                     var25 = "X";
                  }

                  if (!var25.equals("false") || !((String)var23.get("datatype")).equalsIgnoreCase("check")) {
                     boolean[] var16 = this.mezoKellEABarkodba(var17, var25, var14.getLma());
                     if (var16[1]) {
                        var15 = var15 + this.getBrKey(var21, var13) + "=" + var25 + "\r\n";
                     }

                     if (var16[0]) {
                        var14.getLma().vanKitoltottMezo = true;
                     }
                  }
               }
            }

            if (!this.oldalKellEABarkodba()) {
               var15 = null;
            }

            var14.getLma().barkodString = var15;
         }
      }

   }

   private boolean oldalKellEABarkodba() {
      return true;
   }

   private boolean[] mezoKellEABarkodba(MezoMetaAdatok var1, Object var2, LapMetaAdat var3) {
      boolean[] var4 = new boolean[]{true, true};
      if (var2 == null) {
         var4[0] = false;
         var4[1] = false;
      } else if (var2.equals("")) {
         var4[0] = false;
         var4[1] = false;
      } else if (MezoMetaAdatok.office_fill) {
         var4[0] = false;
         var4[1] = false;
      } else {
         var4[0] = !MezoMetaAdatok.notinbrhead;
         var4[1] = !MezoMetaAdatok.notinbarkod;
         if (!MezoMetaAdatok.notinbarkod) {
            if (MezoMetaAdatok.print_on_flp) {
               if (this.utolsoDinamikusLapE(var3) && MezoMetaAdatok.fill_on_lp || this.elsoDinamikusLapE(var3) && MezoMetaAdatok.fill_on_fp) {
                  var4[0] = !MezoMetaAdatok.copy_fld && !MezoMetaAdatok.notinbrhead;
               } else {
                  var4[0] = false;
               }
            } else {
               var4[0] = !MezoMetaAdatok.copy_fld && !MezoMetaAdatok.notinbrhead;
            }
         }
      }

      return var4;
   }

   private boolean utolsoDinamikusLapE(LapMetaAdat var1) {
      if (var1.dinamikusE) {
         return var1.lapSzam == var1.maxLapszam;
      } else {
         return false;
      }
   }

   private boolean elsoDinamikusLapE(LapMetaAdat var1) {
      if (var1.dinamikusE) {
         return var1.lapSzam == 1;
      } else {
         return false;
      }
   }

   public String barkodFixFejlec(Lap[] var1, String var2, String var3) {
      String var4;
      try {
         var4 = var2.substring(0, var2.indexOf("."));
      } catch (Exception var8) {
         var4 = var2;
      }

      String var5;
      try {
         var5 = var2.substring(var2.indexOf(".") + 1);
      } catch (Exception var7) {
         var5 = var2;
      }

      var3 = (var3 + "xxxxxxxx").substring(0, 8);
      return "417IABEV21" + var3 + this.jobbraIgazitottXHosszuSztring(MainPrinter.formId, 20) + this.jobbraIgazitottXHosszuSztring(var4, 3) + this.jobbraIgazitottXHosszuSztring(var5, 3) + this.jobbraIgazitottXHosszuSztringIntbol(this.osszesKulonbozoLapSzama(var1), 2);
   }

   public String barkodValtozoFejlec(Lap var1, int var2, int var3, int var4, int var5, int var6) {
      return this.jobbraIgazitottXHosszuSztringIntbol(var1.getLma().foLapIndex + 1, 2) + this.jobbraIgazitottXHosszuSztringIntbol(var2, 4) + this.jobbraIgazitottXHosszuSztringIntbol(var1.getLma().lapSzam + 1, 4) + this.dec2hex(var3) + this.dec2hex(var4) + this.jobbraIgazitottXHosszuSztringIntbol(var5, 4) + this.jobbraIgazitottXHosszuSztringIntbol(var6 + 65, 3);
   }

   private String dec2hex(int var1) {
      if (var1 < 10) {
         return "" + var1;
      } else {
         switch(var1) {
         case 10:
            return "A";
         case 11:
            return "B";
         case 12:
            return "C";
         case 13:
            return "D";
         case 14:
            return "E";
         default:
            return "F";
         }
      }
   }

   private String jobbraIgazitottXHosszuSztring(String var1, int var2) {
      try {
         for(int var3 = 0; var3 < var2; ++var3) {
            var1 = " " + var1;
         }

         return var1.substring(var1.length() - var2);
      } catch (Exception var4) {
         return "";
      }
   }

   private String jobbraIgazitottXHosszuSztringIntbol(int var1, int var2) {
      try {
         String var3 = "";

         for(int var4 = 0; var4 < var2; ++var4) {
            var3 = var3 + " ";
         }

         var3 = var3 + var1;
         return var3.substring(var3.length() - var2);
      } catch (Exception var5) {
         return "";
      }
   }

   private int osszesKulonbozoLapSzama(Lap[] var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (!var1[var3].getLma().dinamikusE) {
            ++var2;
         } else if (var1[var3].getLma().lapSzam == 0) {
            ++var2;
         }
      }

      return var2;
   }

   public int osszesDinamikusLapSzama(Lap[] var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var1[var3].getLma().dinamikusE) {
            ++var2;
         }
      }

      return var2;
   }

   public byte[] getBZippedData(String var1) throws Exception {
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      var3.write("BZ".getBytes("ISO-8859-2"));
      CBZip2OutputStream var4 = null;
      boolean var5 = false;

      try {
         var4 = new CBZip2OutputStream(var3);
         var4.write(var1.getBytes("ISO-8859-2"));
         var4.flush();
      } catch (Exception var10) {
         System.out.println("ITT VAN AZ EXCEPTION");
         var5 = true;
      } catch (Error var11) {
         System.out.println("ITT VAN AZ ERROR");
         var5 = true;
      }

      if (var5) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A 2D pontkód előállítása sikertelen! Valószínűleg kevés a java által használható memória.", "Mentés hiba", 0);
      }

      try {
         var4.close();
      } catch (IOException var9) {
         Tools.eLog(var9, 0);
      }

      byte[] var2;
      try {
         var2 = var3.toByteArray();
      } catch (Exception var8) {
         var5 = true;
         var2 = null;
      }

      try {
         var3.close();
      } catch (IOException var7) {
         Tools.eLog(var7, 0);
      }

      var4 = null;
      return var5 ? null : var2;
   }

   public BarCode2D getB2dImg(byte[] var1, int var2, byte[] var3, byte[] var4) {
      int var6 = var2 * 500;
      int var7 = var1.length - var6 > 500 ? var6 + 500 : var1.length;
      byte[] var8 = new byte[var7 - var6 + var3.length + var4.length];
      System.arraycopy(var3, 0, var8, 0, var3.length);
      System.arraycopy(var4, 0, var8, var3.length, var4.length);

      for(int var9 = var6; var9 < var7; ++var9) {
         var8[var9 - var6 + var3.length + var4.length] = var1[var9];
      }

      BarCode2D var5 = this.getBarCode2D(var8);
      return var5;
   }

   public BarCode2D getBarCode2D(byte[] var1) {
      BarCode2D var2 = new BarCode2D();
      var2.barType = 16;
      var2.barHeightCM = 0.1D;
      var2.X = 0.03D;
      var2.PDFMode = 0;
      var2.codeBinary = var1;
      return var2;
   }

   public boolean joMezo(Object var1, MezoMetaAdatok var2) {
      if (var1 == null) {
         return false;
      } else if (var1.equals("")) {
         return false;
      } else if (MezoMetaAdatok.copy_fld) {
         return false;
      } else {
         return !MezoMetaAdatok.szamitott;
      }
   }

   public byte[] zip(String var1) {
      byte[] var2 = var1.getBytes();
      Deflater var3 = new Deflater();
      var3.setLevel(9);
      var3.setInput(var2);
      var3.finish();
      ByteArrayOutputStream var4 = new ByteArrayOutputStream(var2.length);
      byte[] var5 = new byte[1024];

      while(!var3.finished()) {
         int var6 = var3.deflate(var5);
         var4.write(var5, 0, var6);
      }

      try {
         var4.close();
      } catch (IOException var7) {
         Tools.eLog(var7, 0);
      }

      return var4.toByteArray();
   }

   private String getBrKey(String var1, int var2) {
      if (var1.toUpperCase().indexOf("XXXX") < 0) {
         return var1;
      } else {
         String var3 = "0000" + (var2 + 1);
         var3 = var3.substring(var3.length() - 4);
         return var1.substring(0, 2) + var3 + var1.substring(6);
      }
   }
}
