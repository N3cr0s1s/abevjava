package hu.piller.enykp.alogic.primaryaccount.envelopeprinter;

import java.util.Enumeration;
import java.util.ResourceBundle;

public class serviceui_hu extends ResourceBundle {
   private static final String[][] property_pairs = new String[][]{{"a", "MediaSize.Engineering A"}, {"accepting-jobs", "Nyomtatási feladat elfogadása ..."}, {"Automatic-Feeder", "Automatic Feeder"}, {"auto-select", "Automatikusan kiválasztott"}, {"b", "MediaSize.Engineering B"}, {"border.chromaticity", "Szín megjelenítés"}, {"border.copies", "Másolatok"}, {"border.jobattributes", "Nyomtatási feladat jellemzők"}, {"border.margins", "Margók"}, {"border.media", "Média/Hordozó"}, {"border.orientation", "Helyzet"}, {"border.printrange", "Nyomtatási mennyiség"}, {"border.printservice", "Nyomtató"}, {"border.quality", "Minőség"}, {"border.sides", "Oldalak"}, {"bottom", "Alsó"}, {"button.cancel", "Mégsem"}, {"button.cancel.mnemonic", "C"}, {"button.ok", "OK"}, {"button.ok.mnemonic", "O"}, {"button.print", "Nyomtatás"}, {"button.print.mnemonic", "P"}, {"button.properties", "Tulajdonságok..."}, {"button.properties.mnemonic", "R"}, {"c", "MediaSize.Engineering C"}, {"Cassette", "Kazetta"}, {"checkbox.collate", "Szétválogatás"}, {"checkbox.collate.mnemonic", "L"}, {"checkbox.jobsheets", "Cím lap"}, {"checkbox.jobsheets.mnemonic", "B"}, {"checkbox.printtofile", "Nyomtatás állományba"}, {"checkbox.printtofile.mnemonic", "F"}, {"d", "MediaSize.Engineering D"}, {"dialog.noprintermsg", "Nincs nyomtató"}, {"dialog.overwrite", "Az állomány már létezik.  Felül akarja írni a létező állományt?"}, {"dialog.owtitle", "Nyomtatás állományba"}, {"dialog.printtitle", "Nyomtatás"}, {"dialog.printtofile", "Nyomtatás állományba"}, {"dialog.pstitle", "Lap beállítás"}, {"e", "MediaSize.Engineering E"}, {"envelope", "Boríték"}, {"error.destination", "Hibás állomány név; Kérem próbálja újra"}, {"error.pagerange", "Hibás nyomtatási mennyiség; Kérem adja meg újra (pl. 1-3,5,7-10)"}, {"executive", "Vezetői összefoglaló"}, {"folio", "Ívlap"}, {"Form-Source", "Nyomtatvány adagoló"}, {"invoice", "Számla"}, {"iso-a0", "A0 (841x1189)"}, {"iso-a1", "A1 (594x841)"}, {"iso-a10", "A10 (26x37)"}, {"iso-a2", "A2 (420x594)"}, {"iso-a3", "A3 (297x420)"}, {"iso-a4", "A4 (210x297)"}, {"iso-a5", "A5 (148x210)"}, {"iso-a6", "A6 (105x148)"}, {"iso-a7", "A7 (74x105)"}, {"iso-a8", "A8 (52x74)"}, {"iso-a9", "A9 (37x52)"}, {"iso-b0", "B0 (1000x1414)"}, {"iso-b1", "B1 (707x1000)"}, {"iso-b10", "B10 (31x44)"}, {"iso-b2", "B2 (500x707)"}, {"iso-b3", "B3 (353x500)"}, {"iso-b4", "B4 (250x353)"}, {"iso-b5", "B5 (176x250)"}, {"iso-b6", "B6 (125x176)"}, {"iso-b7", "B7 (88x125)"}, {"iso-b8", "B8 (62x88)"}, {"iso-b9", "B9 (44x62)"}, {"iso-c0", "C0 (917x1297)"}, {"iso-c1", "C1 (648x917)"}, {"iso-c2", "C2 (458x648)"}, {"iso-c3", "C3 (324x458)"}, {"iso-c4", "C4 (229x324)"}, {"iso-c5", "C5 (162x229)"}, {"iso-c6", "C6 (114x162)"}, {"iso-c7", "C7 (81x114)"}, {"iso-c8", "C8 (57x81)"}, {"iso-designated-long", "ISO szabványos hosszú"}, {"italian-envelope", "Olasz levelezőlap"}, {"japanese-postcard", "Japán levelezőlap"}, {"jis-b0", "B0 (JIS)"}, {"jis-b1", "B1 (JIS)"}, {"jis-b10", "B10 (JIS)"}, {"jis-b2", "B2 (JIS)"}, {"jis-b3", "B3 (JIS)"}, {"jis-b4", "B4 (JIS)"}, {"jis-b5", "B5 (JIS)"}, {"jis-b6", "B6 (JIS)"}, {"jis-b7", "B7 (JIS)"}, {"jis-b8", "B8 (JIS)"}, {"jis-b9", "B9 (JIS)"}, {"label.bottommargin", "alsó"}, {"label.bottommargin.mnemonic", "B"}, {"label.inches", "(in)"}, {"label.info", "Egyéb:"}, {"label.jobname", "Feladat Neve:"}, {"label.jobname.mnemonic", "J"}, {"label.leftmargin", "bal"}, {"label.leftmargin.mnemonic", "F"}, {"label.millimetres", "(mm)"}, {"label.numcopies", "Másolatok száma:"}, {"label.numcopies.mnemonic", "O"}, {"label.priority", "Elsőbbség:"}, {"label.priority.mnemonic", "R"}, {"label.psname", "Név:"}, {"label.psname.mnemonic", "N"}, {"label.pstype", "Fajta:"}, {"label.rangeto", "To"}, {"label.rightmargin", "jobb"}, {"label.rightmargin.mnemonic", "R"}, {"label.size", "Méret:"}, {"label.size.mnemonic", "Z"}, {"label.source", "Forrás:"}, {"label.source.mnemonic", "C"}, {"label.status", "Állapot:"}, {"label.topmargin", "felső"}, {"label.topmargin.mnemonic", "T"}, {"label.username", "Felhasználó neve:"}, {"label.username.mnemonic", "U"}, {"large-capacity", "Nagy kapacitású"}, {"Large-Format", "Nagy alakú"}, {"ledger", "Főkönyv"}, {"main", "Fő oldal"}, {"manual", "Kézi adagoló"}, {"Manual-Envelope", "Kézi boríték"}, {"middle", "Belső oldal"}, {"monarch-envelope", "Különleges boríték"}, {"na-10x13-envelope", "NA10x15 boríték (254x381)"}, {"na-10x14-envelope", "NA10x15 boríték (254x381)"}, {"na-10x15-envelope", "NA10x15 boríték (254x381)"}, {"na-5x7", "NA5x7 papír (127x177)"}, {"na-6x9-envelope", "NA6x9 boríték (152x228)"}, {"na-7x9-envelope", "NA6x7 boríték (152x177)"}, {"na-8x10", "NA8x10 papír (203x254)"}, {"na-9x11-envelope", "NA9x11 boríték (228x279)"}, {"na-9x12-envelope", "NA9x12 boríték (228x304)"}, {"na-legal", "NA Hivatalos"}, {"na-letter", "NA Levél"}, {"na-number-10-envelope", "NA10 boríték"}, {"na-number-11-envelope", "NA11 boríték"}, {"na-number-12-envelope", "NA12 boríték"}, {"na-number-14-envelope", "NA14 boríték"}, {"na-number-9-envelope", "NA9 boríték"}, {"not-accepting-jobs", "El nem fogadott nyomtatási feladatok"}, {"oufuko-postcard", "Dupla levelezőlap (JIS)"}, {"personal-envelope", "Személyes boríték"}, {"quarto", "Negyedes"}, {"radiobutton.color", "Színes"}, {"radiobutton.color.mnemonic", "L"}, {"radiobutton.draftq", "Vázlat"}, {"radiobutton.draftq.mnemonic", "D"}, {"radiobutton.duplex", "Kétszeres"}, {"radiobutton.duplex.mnemonic", "D"}, {"radiobutton.highq", "Jó"}, {"radiobutton.highq.mnemonic", "H"}, {"radiobutton.landscape", "Tájkép"}, {"radiobutton.landscape.mnemonic", "L"}, {"radiobutton.monochrome", "Fekete-fehér"}, {"radiobutton.monochrome.mnemonic", "M"}, {"radiobutton.normalq", "Átlagos"}, {"radiobutton.normalq.mnemonic", "N"}, {"radiobutton.oneside", "Egy oldal"}, {"radiobutton.oneside.mnemonic", "S"}, {"radiobutton.portrait", "Arckép"}, {"radiobutton.portrait.mnemonic", "R"}, {"radiobutton.rangeall", "Összes"}, {"radiobutton.rangeall.mnemonic", "A"}, {"radiobutton.rangepages", "Lapok"}, {"radiobutton.rangepages.mnemonic", "G"}, {"radiobutton.revlandscape", "Fordított tájkép"}, {"radiobutton.revlandscape.mnemonic", "N"}, {"radiobutton.revportrait", "Fordított arckép"}, {"radiobutton.revportrait.mnemonic", "I"}, {"radiobutton.tumble", "Esés"}, {"radiobutton.tumble.mnemonic", "T"}, {"side", "Oldal"}, {"Small-Format", "Kis alakú"}, {"tab.appearance", "Láthatóság"}, {"tab.appearance.vkMnemonic", "65"}, {"tab.general", "Általános beállítások"}, {"tab.general.vkMnemonic", "71"}, {"tab.pagesetup", "Lap beállítások"}, {"tab.pagesetup.vkMnemonic", "83"}, {"tabloid", "Tabloid"}, {"top", "Felső"}, {"Tractor-Feeder", "Tractor Feeder"}};

   public Enumeration getKeys() {
      return new Enumeration() {
         private int i = 0;
         private int max;

         {
            this.max = serviceui_hu.property_pairs.length;
         }

         public boolean hasMoreElements() {
            return this.i < this.max;
         }

         public Object nextElement() {
            return serviceui_hu.property_pairs[this.i++][0];
         }
      };
   }

   protected Object handleGetObject(String var1) {
      String[][] var2 = property_pairs;
      int var3 = 0;

      for(int var4 = var2.length; var3 < var4; ++var3) {
         if (var1.equals(var2[var3][0])) {
            return var2[var3][1];
         }
      }

      return null;
   }
}
