package hu.piller.enykp.alogic.masterdata.sync.configuration;

import hu.piller.enykp.alogic.masterdata.core.BlockDefinition;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryMetaFactory;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.ITechnicalMdHandler;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.handler.BankszamlaszamMdHandler;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.handler.KozossegiAdoszamMdHandler;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.handler.TitulusVezetekNevMdHandler;
import hu.piller.enykp.util.base.PropertyList;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ConfigService {
   public static final Set<String> forbiddenPanids;
   public static final Map<String, String> panidsToTechnicalMd;
   public static final Map<String, List<String>> technicalMdToPanids;
   public static final Map<String, ITechnicalMdHandler> technicalMdHandlers;

   public Properties loadConfig(String var1) {
      Properties var2 = new Properties();

      try {
         var2.load(new FileReader(this.getConfigFileByEntityType(var1)));
      } catch (IOException var4) {
         var2 = this.getDefaultConfigByEntityType(var1);
      }

      return var2;
   }

   public void storeConfig(String var1, Properties var2) {
      try {
         var2.store(new NecroFileOutputStream(this.getConfigFileByEntityType(var1)), (String)null);
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }

   private File getConfigFileByEntityType(String var1) {
      File var2 = new NecroFile(this.getPathToConfig());
      if (!var2.exists()) {
         var2.mkdirs();
      }

      return new NecroFile(var2, this.getConfigFileNameByEntityType(var1));
   }

   private String getConfigFileNameByEntityType(String var1) {
      String var2 = "";
      if ("Magánszemély".equals(var1)) {
         var2 = "person.properties";
      } else if ("Egyéni vállalkozó".equals(var1)) {
         var2 = "selfemployed.properties";
      } else if ("Társaság".equals(var1)) {
         var2 = "corporation.properties";
      }

      return var2;
   }

   private Properties getDefaultConfigByEntityType(String var1) {
      Properties var2 = new Properties();

      try {
         BlockDefinition[] var3 = MDRepositoryMetaFactory.getMDRepositoryMeta().getBlockDefinitionsForEntity(var1);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            BlockDefinition var6 = var3[var5];
            String[] var7 = var6.getMasterDataNames();
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String var10 = var7[var9];
               if (!forbiddenPanids.contains(var10)) {
                  var2.put(var6.getBlockName() + "." + var10, "y");
               }
            }
         }
      } catch (Exception var11) {
         var11.printStackTrace();
      }

      return var2;
   }

   private String getPathToConfig() {
      StringBuffer var1 = (new StringBuffer()).append((String)PropertyList.getInstance().get("prop.usr.primaryaccounts")).append(File.separator).append("sync").append(File.separator).append("filter");
      return var1.toString();
   }

   static {
      HashMap var0 = new HashMap();
      var0.put("Pénzintézet neve", "Bankszámlaszám");
      var0.put("Pénzintézet-azonosító", "Bankszámlaszám");
      var0.put("Számla-azonosító", "Bankszámlaszám");
      var0.put("Közösségi adószám ország kód", "Közösségi adószám");
      var0.put("Közösségi adószám", "Közösségi adószám");
      var0.put("Név titulus", "Vezetéknév");
      var0.put("Vezetékneve", "Vezetéknév");
      panidsToTechnicalMd = Collections.unmodifiableMap(var0);
      HashMap var1 = new HashMap();
      var1.put("Bankszámlaszám", Collections.unmodifiableList(Arrays.asList("Pénzintézet neve", "Pénzintézet-azonosító", "Számla-azonosító")));
      var1.put("Közösségi adószám", Collections.unmodifiableList(Arrays.asList("Közösségi adószám ország kód", "Közösségi adószám")));
      var1.put("Vezetéknév", Collections.unmodifiableList(Arrays.asList("Név titulus", "Vezetékneve")));
      technicalMdToPanids = Collections.unmodifiableMap(var1);
      HashMap var2 = new HashMap();
      var2.put("Bankszámlaszám", new BankszamlaszamMdHandler());
      var2.put("Közösségi adószám", new KozossegiAdoszamMdHandler());
      var2.put("Vezetéknév", new TitulusVezetekNevMdHandler());
      technicalMdHandlers = Collections.unmodifiableMap(var2);
      HashSet var3 = new HashSet();
      var3.add("Adózó neme");
      var3.add("E-mail címe");
      var3.add("Adózó telefonszáma");
      var3.add("Ügyintéző neve");
      var3.add("Ügyintéző telefonszáma");
      var3.add("Ügyintéző e-mail címe");
      var3.add("Regisztrációs szám");
      var3.add("Bejelentő adóazonosító jele");
      var3.add("Nyilvántartó törvényszék");
      var3.add("Szervezet típus");
      var3.add("Nyilvántartási sorszám");
      var3.add("Adózó fax száma");
      forbiddenPanids = Collections.unmodifiableSet(var3);
   }
}
