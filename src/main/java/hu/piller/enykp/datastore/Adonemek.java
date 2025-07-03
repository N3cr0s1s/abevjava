package hu.piller.enykp.datastore;

import java.util.Hashtable;

public class Adonemek {
   private Hashtable<String, String> adonemek = new Hashtable();
   private static Adonemek instance;

   public static Adonemek getInstance() {
      if (instance == null) {
         instance = new Adonemek();
      }

      return instance;
   }

   private Adonemek() {
      this.initAdonem();
   }

   public String getAdonemMegnevezes(String var1) {
      return !this.adonemek.containsKey(var1) ? "ismeretlen adónem (" + var1 + ")" : (String)this.adonemek.get(var1);
   }

   private void initAdonem() {
      this.adonemek.put("101", "Társasági adó");
      this.adonemek.put("103", "Személyi jövedelemadó");
      this.adonemek.put("104", "Általános forgalmi adó");
      this.adonemek.put("115", "Egyszerűsített vállalkozói adó");
      this.adonemek.put("119", "Rehabilitációs hozzájárulás");
      this.adonemek.put("124", "Egészségbiztosítási Alapot megillető bevételek");
      this.adonemek.put("125", "Nyugdíjbiztosítási Alapot megillető bevételek");
      this.adonemek.put("138", "Adóztatási tevékenységgel összefüggő késedelmi pótlék");
      this.adonemek.put("144", "Munkaadói járulék");
      this.adonemek.put("145", "Munkavállalói járulék");
      this.adonemek.put("146", "Játékadó");
      this.adonemek.put("149", "Munkáltatói táppénz hozzájárulás");
      this.adonemek.put("151", "Szociálpolitikai menetdíj-támogatás");
      this.adonemek.put("152", "Egészségügyi hozzájárulás");
      this.adonemek.put("168", "Állami kezességbeváltás");
      this.adonemek.put("169", "Agrárgazdasági kezességbeváltás");
      this.adonemek.put("172", "Nemzeti kulturális járulék");
      this.adonemek.put("182", "Szakképzési hozzájárulás");
      this.adonemek.put("184", "Innovációs járulék");
      this.adonemek.put("185", "Vállalkozói járulék");
      this.adonemek.put("186", "NAV START-kártya kedvezményes járulékbefizetések");
      this.adonemek.put("187", "Korkedvezmény-biztosítási járulék");
      this.adonemek.put("188", "Egészségbiztosítási és munkaerőpiaci járulék");
      this.adonemek.put("190", "Kifizetőt terhelő ekho");
      this.adonemek.put("191", "Magánszemélyt terhelő ekho");
      this.adonemek.put("192", "Magánnyugdíjpénztári tagtól levont 11,1 %-os ekho");
      this.adonemek.put("193", "Nyugdíjas vagy járulékfizetési felsőhatár túllépés esetén fizetendő ekho");
      this.adonemek.put("194", "Ekho különadó");
      this.adonemek.put("195", "Magánnyugdíjpénztári tagtól levont 15 %-os ekho");
      this.adonemek.put("197", "EGT államban biztosított személytől levont ekho");
      this.adonemek.put("200", "Biztosítási adó");
      this.adonemek.put("202", "Hitelintézeti járadék");
      this.adonemek.put("211", "Egyéb kötelezettségek");
      this.adonemek.put("214", "Átlagadó");
      this.adonemek.put("215", "Adóztatási tevékenységgel összefüggő bírság, mulasztási bírság és önellenőrzéssel kapcsolatos befizetések");
      this.adonemek.put("218", "Baleseti adó");
      this.adonemek.put("221", "Adóhatósági eljárási illeték");
      this.adonemek.put("227", "TV-készülék üzemben tartási díj");
      this.adonemek.put("232", "Energiaellátók jövedelemadója");
      this.adonemek.put("234", "2010. évtől érvényes gyógyszertár szolidaritási díj");
      this.adonemek.put("239", "Egyszerűsített fogl. eredő közteher");
      this.adonemek.put("241", "Cégautóadó");
      this.adonemek.put("243", "Gyógyszerforgalmazók gyógyszertárban forgalmazott, közfinanszírozott gyógyszerek");
      this.adonemek.put("244", "Gyógyszer-nagykereskedők gyógyszertárak részére értékesített közfinanszírozott gyógyszerek utáni befizetések");
      this.adonemek.put("245", "Gyógyszertár szolidaritási díj");
      this.adonemek.put("246", "Gyógyszerismertetés utáni befizetések");
      this.adonemek.put("247", "Gyógyászati segédeszköz ismertetés utáni befizetések");
      this.adonemek.put("248", "Gyógyszertámogatás-többlet sávos kockázatviseléséből eredő befizetések");
      this.adonemek.put("258", "Szociális hozzájárulási adó");
      this.adonemek.put("259", "Kulturális adó");
      this.adonemek.put("283", "Gyógyszerforgalmazók gyógyszertárban forgalmazott közfinanszírozott gyógyszerek utáni kiegészítő befizetései");
      this.adonemek.put("288", "Kisadózó vállalkozások tételes adója");
      this.adonemek.put("289", "Kisvállalati adó");
      this.adonemek.put("290", "Levont személyi jövedelemadó");
      this.adonemek.put("291", "Levont Nyugdíjbiztosítási Alapot megillető járulék");
      this.adonemek.put("293", "Levont egészségbiztosítási és munkaerőpiaci járulék");
      this.adonemek.put("295", "Tűzvédelmi hozzájárulás");
      this.adonemek.put("296", "Közművezeték adó");
      this.adonemek.put("297", "Dohányipari vállalkozások 2015. évi egészségügyi hozzájárulása");
      this.adonemek.put("300", "Reklámadó");
      this.adonemek.put("301", "Jogosulatlanul igénybevett lakásépítési kedvezmény és pótléka");
      this.adonemek.put("302", "Háztartási alkalmazott utáni regisztrációs díj");
      this.adonemek.put("303", "Külföldi vállalkozásnál biztosítási kötelezettséggel járó jogviszonyban foglalkoztatott utáni járulék");
      this.adonemek.put("305", "Magánszemélyek jogviszony megszűnéséhez kapcsolódó egyes jövedelmeinek különadója");
      this.adonemek.put("310", "Turizmusfejlesztési hozzájárulás");
      this.adonemek.put("312", "Elektronikus bírósági eljárási illeték");
      this.adonemek.put("313", "Forgalmazó és befektetési alap különadója");
      this.adonemek.put("314", "Pénzügyi tranzakciós illeték");
      this.adonemek.put("315", "Hitelintézetek 2011-ben kezdődő üzleti- vagy adóévére vonatkozó különadója");
      this.adonemek.put("316", "Pénzügyi szervezetek különadója");
      this.adonemek.put("339", "Környezetterhelési díj");
      this.adonemek.put("342", "Bolti kiskereskedelmi tevékenység különadója");
      this.adonemek.put("343", "Távközlési tevékenység különadója");
      this.adonemek.put("344", "Energiaellátó vállalkozási tevékenységének különadója");
      this.adonemek.put("345", "Távközlési adó");
      this.adonemek.put("347", "Közforgalmú gyógyszertárat működtető vállalkozások működési célú támogatás");
      this.adonemek.put("352", "Egyedi támogatás");
      this.adonemek.put("354", "Szociális hozzájárulási adókedvezmény visszatérítés lebonyolítása");
      this.adonemek.put("355", "Online adatkapcsolatra képes pénztárgép beszerzés");
      this.adonemek.put("356", "Egyéb vállalati támogatás");
      this.adonemek.put("358", "2010. évtől érvényes közforgalmú gyógyszertárat működtető vállalkozások működési célú támogatása");
      this.adonemek.put("368", "Állami kezességbeváltás");
      this.adonemek.put("369", "Normatív támogatás");
      this.adonemek.put("393", "Agrártermelési támogatás");
      this.adonemek.put("394", "Agrárfinanszírozás támogatása");
      this.adonemek.put("396", "Új mezőgazdasági gépvásárláshoz/lízinghez nyújtott támogatás");
      this.adonemek.put("406", "Társadalombiztosítási járulék magánszemélyt, őstermelőt, egyéni vállalkozót, kifizetőt terhelő kötelezettség");
      this.adonemek.put("407", "Biztosítottaktól levont társadalombiztosítási járulék");
      this.adonemek.put("408", "Egészségügyi szolgáltatási járulék");
      this.adonemek.put("410", "Belföldi gépjárműadó");
      this.adonemek.put("412", "Hitelintézetek járványügyi helyzettel összefüggő különadója");
      this.adonemek.put("413", "Kiskereskedelmi adó");
      this.adonemek.put("414", "EKAER szám lezárását követő bejelentés módosítási pótlék");
      this.adonemek.put("415", "EKAER bejelentési kötelezettség nem előírásszerű teljesítése miatt kiszabott mulasztási bírság");
      this.adonemek.put("416", "Bevándorlási különadó");
      this.adonemek.put("418", "Bírósági határozatban megállapított eljárási illeték");
      this.adonemek.put("419", "Gyógyszergyártók különadója");
      this.adonemek.put("420", "NAV Szén-dioxid kvóta adó bevételi számla");
      this.adonemek.put("481", "Légitársaságok hozzájárulása");
      this.adonemek.put("488", "Kifizető által kisadózó vállalkozásnak juttatott bevétel utáni adó");
      this.adonemek.put("521", "Illetékek bevételi számla");
      this.adonemek.put("599", "Végrehajtói manuális elszámolású letéti számla");
      this.adonemek.put("703", "Civil kedvezményezetti SZJA 1% felajánlás");
      this.adonemek.put("901", "Vámbevételi számla");
      this.adonemek.put("902", "Importtermék általános forgalmi adó");
      this.adonemek.put("909", "Hulladékgazdálkodással kapcsolatos jogosulatlan támogatás visszafizetése");
      this.adonemek.put("910", "Uniós vámbevételek");
      this.adonemek.put("911", "Külföldi gépjárműadó");
      this.adonemek.put("912", "Vámletét letéti számla");
      this.adonemek.put("914", "Regisztrációs adó");
      this.adonemek.put("916", "Külföldi rendszámú járművek adóbírság");
      this.adonemek.put("918", "Szabálysértési pénzbírság, költség és rendbírság");
      this.adonemek.put("920", "Környezetvédelmi termékdíj");
      this.adonemek.put("923", "Népegészségügyi termékadó");
      this.adonemek.put("924", "Termékdíj visszaigénylés");
      this.adonemek.put("950", "Energiaadó");
   }
}
