package hu.piller.enykp.alogic.masterdata.gui;

import hu.piller.enykp.gui.component.filtercombo.ENYKFilterComboPanel;
import hu.piller.enykp.util.base.Tools;
import java.awt.AWTKeyStroke;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

public class MDGUIFieldFactory {
   private static MDGUIFieldFactory _fieldFactory = null;
   private static final char GROUP_SEPARATOR_SYMBOL = '-';
   public static final String[] OPCIOK_NEM = new String[]{"Férfi", "Nő"};
   public static final String[] OPCIOK_TITULUS = new String[]{"Dr.", "Dr. Dr.", "Id.", "Id. Dr.", "Ifj.", "Ifj. Dr.", "Özv.", "Özv. Dr.", "Prof.", "Dr. Pr.", "Pr. Dr."};
   public static final String[] OPCIOK_ORSZAG_ISO = new String[]{"AT", "BE", "BG", "CZ", "CY", "DE", "DK", "EE", "ES", "FI", "FR", "GB", "GR", "HU", "HR", "IE", "IT", "LT", "LU", "LV", "MT", "NL", "PT", "PL", "RO", "SE", "SI", "SK"};
   public static final String[] OPCIOK_KOZTERULET = new String[]{"", "utca", "út", "tér", "útja", "körtér", "körút", "köz", "postafiók", "akna", "akna-alsó", "akna-felső", "alagút", "alsórakpart", "autópálya", "arborétum", "autóút", "állat és növ.kert", "állomás", "árnyék", "árok", "átjáró", "barakképület", "barlang", "bánya", "bányatelep", "bástya", "bástyája", "bejáró", "bekötőút", "csárda", "csónakházak", "domb", "dűlő", "dűlősor", "dűlőterület", "dűlőút", "egyetemváros", "egyéb", "elágazás", "emlékút", "erdészház", "erdészlak", "erdő", "erdősor", "fasor", "fasora", "felső", "forduló", "föld", "főmérnökség", "főtér", "főút", "gát", "gátőrház", "gátsor", "gyár", "gyártelep", "gyárváros", "gyümölcsös", "hajóállomás", "határsor", "határút", "ház", "hegy", "hegyhát", "hegyhát dűlő", "hegyhát köz", "hídfő", "hrsz.", "iskola", "jav.alatt", "játszótér", "kapu", "kastély", "kert", "kertsor", "kilátó", "kioszk", "kocsiszín", "kolónia", "korzó", "kör", "körönd", "körtér", "körvasútsor", "körzet", "köz", "kultúrpark", "kunyhó", "kút", "kültelek", "külterület", "lakóház", "lakónegyed", "lakópark", "lakótelep", "lejáró", "lejtő", "lépcső", "liget", "major", "malom", "menedékház", "mélyút", "munkásszálló", "műút", "oldal", "orom", "őrház", "őrházak", "őrházlak", "park", "parkja", "parkoló", "part", "pavilon", "pálya", "pályaudvar", "piac", "pihenő", "pince", "pincesor", "puszta", "rakpart", "repülőtér", "rész", "rét", "sarok", "sánc", "sávház", "sétány", "sor", "sora", "sportpálya", "sporttelep", "stadion", "strandfürdő", "sugárút", "szállás", "szállások", "szer", "szél", "sziget", "szivattyútelep", "szőlő", "szőlőhegy", "szőlők", "tag", "tanya", "tanyák", "telep", "temető", "tere", "tető", "téli kikötő", "tömb", "turistaház", "udvar", "utak", "utcája", "útőrház", "üdülő", "üdülő-part üdülő-sor", "üdülő-telep", "vadaskert", "vadászház", "vasútállomás", "vasútimegálló", "vasútiőrház", "vasútsor", "vágóhíd", "vár", "várkerület", "várköz", "város", "vezetőút", "villasor", "vitorláskikötő", "vízmű", "völgy", "zsilip", "zug"};
   public static final String[] OPCIOK_SZERVEZET_TIPUS = new String[]{"01", "02", "03", "04", "05", "06", "07", "08"};
   public static final String[] OPCIOK_KEPVISELET_TIPUSA = new String[]{"Jogi képviselő", "Pártfogó ügyvéd"};
   public static final String[] OPCIOK_KEPVISELET_FORMAJA = new String[]{"Ügyvédi iroda", "Jogtanácsos", "Egyéb jogi képviselő", "Egyéni ügyvéd"};
   public static final String MASZK_ADOSZAM = "########-#-##";
   public static final String MASZK_ADOAZONOSITO_JEL = "##########";
   public static final String MASZK_IRANYITOSZAM = "AAAAAAA";
   public static final String MASZK_PENZINTEZET_AZON = "########";
   public static final String MASZK_SZAMLA_AZON = "########-########";
   public static final String MASZK_VPID = "'H'U##########";
   public static final String MASZK_VP_REGSZAM = "##########";
   public static final String MASZK_VP_ENGSZAM = "'H'U######";
   public static final String MASZK_DATUM = "####-##-##";
   public static final String MASZK_NYILVANTARTASI_SORSZAM = "#######";

   private MDGUIFieldFactory() {
   }

   public static synchronized MDGUIFieldFactory getInstance() {
      if (_fieldFactory == null) {
         _fieldFactory = new MDGUIFieldFactory();
      }

      return _fieldFactory;
   }

   public JComponent getInputFieldForType(String var1) {
      final Object var2;
      if ("TApehAdoszam".equals(var1)) {
         var2 = new JFormattedTextField();
         this.setFormattedTextField((JFormattedTextField)var2, "########-#-##", (String)null);
         ((JComponent)var2).addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent var1) {
               ((JFormattedTextField)var2).getCaret().setDot(0);
            }

            public void focusLost(FocusEvent var1) {
            }
         });
      } else if ("TNev".equals(var1)) {
         var2 = new JTextField();
      } else if ("TApehAdoazonosito".equals(var1)) {
         var2 = new JFormattedTextField();
         this.setFormattedTextField((JFormattedTextField)var2, "##########", (String)null);
         ((JComponent)var2).addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent var1) {
               ((JFormattedTextField)var2).getCaret().setDot(0);
            }

            public void focusLost(FocusEvent var1) {
            }
         });
      } else if ("TString".equals(var1)) {
         var2 = new JTextField();
      } else if ("TIranyitoSzam".equals(var1)) {
         var2 = new JFormattedTextField();
         this.setFormattedTextField((JFormattedTextField)var2, "AAAAAAA", (String)null);
         ((JComponent)var2).addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent var1) {
               ((JFormattedTextField)var2).getCaret().setDot(0);
            }

            public void focusLost(FocusEvent var1) {
            }
         });
      } else if ("TKozterulet".equals(var1)) {
         var2 = new ENYKFilterComboPanel();
         ((ENYKFilterComboPanel)var2).addElements(OPCIOK_KOZTERULET, false);
      } else if ("TOrszagISOLista".equals(var1)) {
         var2 = new ENYKFilterComboPanel();
         ((ENYKFilterComboPanel)var2).addElements(OPCIOK_ORSZAG_ISO, false);
      } else if ("TTel".equals(var1)) {
         var2 = new JTextField();
      } else if ("TPenzIntezet".equals(var1)) {
         var2 = new JFormattedTextField();
         this.setFormattedTextField((JFormattedTextField)var2, "########", (String)null);
         ((JComponent)var2).addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent var1) {
               ((JFormattedTextField)var2).getCaret().setDot(0);
            }

            public void focusLost(FocusEvent var1) {
            }
         });
      } else if ("TSzamlaSzam".equals(var1)) {
         var2 = new JFormattedTextField();
         this.setFormattedTextField((JFormattedTextField)var2, "########-########", (String)null);
         ((JComponent)var2).addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent var1) {
               ((JFormattedTextField)var2).getCaret().setDot(0);
            }

            public void focusLost(FocusEvent var1) {
            }
         });
      } else if ("TVpopId".equals(var1)) {
         var2 = new JFormattedTextField();
         this.setFormattedTextField((JFormattedTextField)var2, "'H'U##########", (String)null);
         ((JComponent)var2).addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent var1) {
               ((JFormattedTextField)var2).getCaret().setDot(0);
            }

            public void focusLost(FocusEvent var1) {
            }
         });
      } else if ("TVpopRegisztraciosSzam".equals(var1)) {
         var2 = new JFormattedTextField();
         this.setFormattedTextField((JFormattedTextField)var2, "##########", (String)null);
         ((JComponent)var2).addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent var1) {
               ((JFormattedTextField)var2).getCaret().setDot(0);
            }

            public void focusLost(FocusEvent var1) {
            }
         });
      } else if ("TVpopEngedelySzam".equals(var1)) {
         var2 = new JFormattedTextField();
         this.setFormattedTextField((JFormattedTextField)var2, "'H'U######", (String)null);
         ((JComponent)var2).addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent var1) {
               ((JFormattedTextField)var2).getCaret().setDot(0);
            }

            public void focusLost(FocusEvent var1) {
            }
         });
      } else if ("TDatum".equals(var1)) {
         var2 = new JFormattedTextField();
         this.setFormattedTextField((JFormattedTextField)var2, "####-##-##", (String)null);
         ((JComponent)var2).addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent var1) {
               ((JFormattedTextField)var2).getCaret().setDot(0);
            }

            public void focusLost(FocusEvent var1) {
            }
         });
      } else if ("TTitulusLista".equals(var1)) {
         var2 = new ENYKFilterComboPanel();
         ((ENYKFilterComboPanel)var2).addElements(OPCIOK_TITULUS, false);
         ((ENYKFilterComboPanel)var2).enableFreeText();
      } else if ("TAdozoNeme".equals(var1)) {
         var2 = new ENYKFilterComboPanel();
         ((ENYKFilterComboPanel)var2).addElements(OPCIOK_NEM, false);
      } else {
         Vector var3;
         Vector var4;
         Object[] var5;
         if ("TNyilvantartoTorvenyszek".equals(var1)) {
            var2 = new ENYKFilterComboPanel();
            var3 = new Vector();
            var3.add("01");
            var3.add("02");
            var3.add("03");
            var3.add("04");
            var3.add("05");
            var3.add("06");
            var3.add("07");
            var3.add("08");
            var3.add("09");
            var3.add("10");
            var3.add("11");
            var3.add("12");
            var3.add("13");
            var3.add("14");
            var3.add("15");
            var3.add("16");
            var3.add("17");
            var3.add("18");
            var3.add("19");
            var3.add("20");
            var4 = new Vector();
            var4.add("Fővárosi Törvényszék=01");
            var4.add("Pécsi Törvényszék=02");
            var4.add("Kecskeméti Törvényszék=03");
            var4.add("Gyulai Törvényszék=04");
            var4.add("Miskolci Törvényszék=05");
            var4.add("Szegedi Törvényszék=06");
            var4.add("Székesfehérvári Törvényszék=07");
            var4.add("Győri Törvényszék=08");
            var4.add("Debreceni Törvényszék=09");
            var4.add("Egri Törvényszék=10");
            var4.add("Tatabányai Törvényszék=11");
            var4.add("Balassagyarmati Törvényszék=12");
            var4.add("Budapest Környéki Törvényszék=13");
            var4.add("Kaposvári Törvényszék=14");
            var4.add("Nyíregyházi Törvényszék=15");
            var4.add("Szolnoki Törvényszék=16");
            var4.add("Szekszárdi Törvényszék=17");
            var4.add("Szombathelyi Törvényszék=18");
            var4.add("Veszprémi Törvényszék=19");
            var4.add("Zalaegerszegi Törvényszék=20");
            var5 = new Object[]{var4, var3};
            ((ENYKFilterComboPanel)var2).getCombo().setFeature("combo_data", var5);
            ((ENYKFilterComboPanel)var2).getCombo().LISTWIDTH = 300;
         } else if ("TSzervezetTipus".equals(var1)) {
            var2 = new ENYKFilterComboPanel();
            var3 = new Vector();
            var3.add("01");
            var3.add("02");
            var3.add("03");
            var3.add("04");
            var3.add("05");
            var3.add("07");
            var3.add("08");
            var4 = new Vector();
            var4.add("alapítvány, közalapítvány=01");
            var4.add("egyesület=02");
            var4.add("köztestület=03");
            var4.add("magányugdíjpénztár, önkéntes kölcsönös biztosító pénztár, vegyes pénztár=04");
            var4.add("Munkavállalói Résztulajdonosi Program szervezet=05");
            var4.add("országos sportági szakszövetség=07");
            var4.add("egyéb szervezet=08");
            var5 = new Object[]{var4, var3};
            ((ENYKFilterComboPanel)var2).getCombo().setFeature("combo_data", var5);
            ((ENYKFilterComboPanel)var2).getCombo().LISTWIDTH = 465;
         } else if ("TNyilvantartasiSorszam".equals(var1)) {
            var2 = new JFormattedTextField();
            this.setFormattedTextField((JFormattedTextField)var2, "#######", (String)null);
            ((JComponent)var2).addFocusListener(new FocusListener() {
               public void focusGained(FocusEvent var1) {
                  ((JFormattedTextField)var2).getCaret().setDot(0);
               }

               public void focusLost(FocusEvent var1) {
               }
            });
         } else if ("TKepviseletTipusa".equals(var1)) {
            var2 = new ENYKFilterComboPanel();
            ((ENYKFilterComboPanel)var2).addElements(OPCIOK_KEPVISELET_TIPUSA, false);
         } else if ("TKepviseletFormaja".equals(var1)) {
            var2 = new ENYKFilterComboPanel();
            ((ENYKFilterComboPanel)var2).addElements(OPCIOK_KEPVISELET_FORMAJA, false);
         } else {
            var2 = new JTextField();
            ((JTextField)var2).setText(var1);
         }
      }

      Set var6 = ((JComponent)var2).getFocusTraversalKeys(0);
      HashSet var7 = new HashSet(var6);
      var7.add(AWTKeyStroke.getAWTKeyStroke(10, 0));
      ((JComponent)var2).setFocusTraversalKeys(0, Collections.unmodifiableSet(var7));
      return (JComponent)var2;
   }

   public String getNameOfGetter(String var1) {
      String var2 = "";
      if ("javax.swing.JTextField".equals(var1)) {
         var2 = "getText";
      } else if ("javax.swing.JFormattedTextField".equals(var1)) {
         var2 = "getText";
      } else if ("hu.piller.enykp.gui.component.filtercombo.ENYKFilterComboPanel".equals(var1)) {
         var2 = "getText";
      }

      return var2;
   }

   public String getNameOfSetter(String var1) {
      String var2 = "";
      if ("javax.swing.JTextField".equals(var1)) {
         var2 = "setText";
      } else if ("javax.swing.JFormattedTextField".equals(var1)) {
         var2 = "setText";
      } else if ("hu.piller.enykp.gui.component.filtercombo.ENYKFilterComboPanel".equals(var1)) {
         var2 = "setText";
      }

      return var2;
   }

   public String postProcessData(Object var1, JComponent var2) {
      String var3;
      if (var2 instanceof JFormattedTextField) {
         char var4 = ((MaskFormatter)((JFormattedTextField)var2).getFormatter()).getPlaceholderCharacter();
         var3 = this.filterJFormattedTextFieldValue((String)var1, var4);
         int var5 = 0;

         for(int var6 = 0; var6 < var3.length(); ++var6) {
            if (var3.charAt(var6) == '-') {
               ++var5;
            }
         }

         if (var3.length() == var5) {
            var3 = this.filterJFormattedTextFieldValue(var3, '-');
         }
      } else {
         var3 = (String)var1;
      }

      return var3;
   }

   private void setFormattedTextField(JFormattedTextField var1, String var2, String var3) {
      try {
         MaskFormatter var4 = new MaskFormatter(var2);
         var4.setPlaceholderCharacter('_');
         var4.setAllowsInvalid(false);
         if (var3 != null) {
            var4.setValidCharacters(var3 + var4.getPlaceholderCharacter());
         }

         var1.setFormatterFactory(new DefaultFormatterFactory(var4));
         var4.install(var1);
         var1.setFocusLostBehavior(0);
      } catch (ParseException var6) {
         Tools.eLog(var6, 0);
      }

   }

   private String filterJFormattedTextFieldValue(String var1, char var2) {
      String var3 = var1;
      int var4 = 0;

      StringBuffer var5;
      for(var5 = new StringBuffer(); var4 <= var3.length() - 1; ++var4) {
         if (var3.charAt(var4) != var2) {
            var5.append(var3.charAt(var4));
         }
      }

      return var5.toString();
   }
}
