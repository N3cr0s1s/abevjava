package hu.piller.enykp.alogic.masterdata.envelope.provider.masterdata;

public enum EnvelopeMasterData {
   NAME("Adózó neve"),
   FNAME_TITLE("Név titulus"),
   FNAME("Vezetékneve"),
   LNAME("Keresztneve"),
   TAXNUMBER("Adózó adószáma"),
   TAXID("Adózó adóazonosító jele"),
   EU_TAXNUMBER("Közösségi adószám"),
   S_ZIP("Irányítószám"),
   S_SETTLEMENT("Település"),
   S_PUBLICPLACE("Közterület neve"),
   S_PUBLICPLACETYPE("Közterület jellege"),
   S_BUILDING("Épület"),
   S_STAIRCASE("Lépcsőház"),
   S_HOUSENUMBER("Házszám"),
   S_LEVEL("Emelet"),
   S_DOOR("Ajtó"),
   M_ZIP("L Irányítószám"),
   M_SETTLEMENT("L Település"),
   M_PUBLICPLACE("L Közterület neve"),
   M_PUBLICPLACETYPE("L Közterület jellege"),
   M_BUILDING("L Épület"),
   M_STAIRCASE("L Lépcsőház"),
   M_HOUSENUMBER("L Házszám"),
   M_LEVEL("L Emelet"),
   M_DOOR("L Ajtó");

   private String key;

   private EnvelopeMasterData(String var3) {
      this.key = var3;
   }

   public String getKey() {
      return this.key;
   }
}
