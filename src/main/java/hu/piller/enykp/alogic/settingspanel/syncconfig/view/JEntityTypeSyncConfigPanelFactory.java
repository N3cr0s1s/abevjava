package hu.piller.enykp.alogic.settingspanel.syncconfig.view;

import hu.piller.enykp.alogic.masterdata.core.BlockDefinition;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryException;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryMetaFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.JPanel;

public class JEntityTypeSyncConfigPanelFactory {
   private Map<String, List<String>> orders = new HashMap(3);
   private Map<String, BlockDefinition[]> blockDefs;
   private Properties labels;
   private static JEntityTypeSyncConfigPanelFactory instance;

   private JEntityTypeSyncConfigPanelFactory() {
      this.orders.put("Társaság", Collections.unmodifiableList(Arrays.asList("Törzsadatok", "Egyéb adatok", "Állandó cím", "Levelezési cím", "VPOP törzsadatok")));
      this.orders.put("Egyéni vállalkozó", Collections.unmodifiableList(Arrays.asList("Törzsadatok", "Születési adatok", "Állandó cím", "Levelezési cím", "Egyéb adatok", "VPOP törzsadatok")));
      this.orders.put("Magánszemély", Collections.unmodifiableList(Arrays.asList("Törzsadatok", "Születési adatok", "Állandó cím", "Levelezési cím", "Egyéb adatok", "VPOP törzsadatok")));
      this.blockDefs = new HashMap();

      try {
         this.blockDefs.put("Társaság", MDRepositoryMetaFactory.getMDRepositoryMeta().getBlockDefinitionsForEntity("Társaság"));
      } catch (MDRepositoryException var5) {
         this.blockDefs.put("Társaság", new BlockDefinition[0]);
         var5.printStackTrace();
      }

      try {
         this.blockDefs.put("Egyéni vállalkozó", MDRepositoryMetaFactory.getMDRepositoryMeta().getBlockDefinitionsForEntity("Egyéni vállalkozó"));
      } catch (MDRepositoryException var4) {
         this.blockDefs.put("Egyéni vállalkozó", new BlockDefinition[0]);
         var4.printStackTrace();
      }

      try {
         this.blockDefs.put("Magánszemély", MDRepositoryMetaFactory.getMDRepositoryMeta().getBlockDefinitionsForEntity("Magánszemély"));
      } catch (MDRepositoryException var3) {
         this.blockDefs.put("Magánszemély", new BlockDefinition[0]);
         var3.printStackTrace();
      }

      this.labels = new Properties();

      try {
         this.labels.load(ClassLoader.getSystemClassLoader().getResourceAsStream("resources/masterdata/syncmasterdatalabel.properties"));
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static JEntityTypeSyncConfigPanelFactory getInstance() {
      if (instance == null) {
         instance = new JEntityTypeSyncConfigPanelFactory();
      }

      return instance;
   }

   public JPanel getSyncConfigPanelForEntity(String var1) {
      JEntityTypeSyncConfigPanel var2 = new JEntityTypeSyncConfigPanel();
      var2.setName(var1);
      var2.setBlockOrder((List)this.orders.get(var1));
      var2.setBlockDefinitions((BlockDefinition[])this.blockDefs.get(var1));
      var2.setLabels(this.labels);
      var2.initCheckBoxes();
      var2.setUpLayout();
      return var2;
   }
}
