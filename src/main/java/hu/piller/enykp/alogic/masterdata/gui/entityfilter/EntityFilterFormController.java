package hu.piller.enykp.alogic.masterdata.gui.entityfilter;

import hu.piller.enykp.alogic.masterdata.core.Block;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityException;
import hu.piller.enykp.alogic.masterdata.core.EntityHome;
import hu.piller.enykp.alogic.masterdata.core.MasterData;
import hu.piller.enykp.alogic.masterdata.envelope.Envelope;
import hu.piller.enykp.alogic.masterdata.envelope.model.EnvelopeModel;
import hu.piller.enykp.alogic.masterdata.envelope.provider.masterdata.EnvelopeMasterData;
import hu.piller.enykp.alogic.masterdata.gui.MDForm;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryException;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryMetaFactory;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import javax.swing.SwingUtilities;

public class EntityFilterFormController {
   private double screen_height = -1.0D;
   private boolean changed;

   public EntityFilterFormController() {
      try {
         this.screen_height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
      } catch (Exception var2) {
         this.screen_height = -1.0D;
      }

   }

   public boolean hasChanged() {
      return this.changed;
   }

   public Entity[] load() {
      Entity[] var1 = new Entity[0];

      try {
         var1 = EntityHome.getInstance().findByTypeAndMasterData("*", new MasterData[0]);
      } catch (EntityException var3) {
         this.error(var3);
      }

      return var1;
   }

   public void envelope(long var1) {
      try {
         Entity var3 = EntityHome.getInstance().findByID(var1);
         if ("Adótanácsadó".equals(var3.getName()) || "Jogi képviselő".equals(var3.getName())) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "Levelezési cím adatok hiányában boríték nem nyomtatható!", "Tájékoztatás", 1);
            return;
         }

         final HashMap var4 = new HashMap();
         EnvelopeMasterData[] var5 = EnvelopeMasterData.values();
         int var6 = var5.length;

         int var7;
         EnvelopeMasterData var8;
         for(var7 = 0; var7 < var6; ++var7) {
            var8 = var5[var7];
            Block[] var9 = var3.getAllBlocks();
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               Block var12 = var9[var11];
               if (var12.hasKey(var8.getKey())) {
                  var4.put(var8, var12.getMasterData(var8.getKey()).getValue());
               }
            }
         }

         var5 = EnvelopeMasterData.values();
         var6 = var5.length;

         for(var7 = 0; var7 < var6; ++var7) {
            var8 = var5[var7];
            if (!var4.containsKey(var8)) {
               var4.put(var8, "");
            }
         }

         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               EnvelopeModel var1 = new EnvelopeModel(var4);
               if (!var1.getFeladoAllandoCim().isFeladoPrintable() && !var1.getFeladoLevelezesiCim().isFeladoPrintable()) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "A feladónak sem levelezési, sem állandó címe nem képezhető az űrlapon szereplő adatok alapján!\nNem tud borítékot nyomtatni.", "Nyomtatási hiba", 0);
               } else {
                  Envelope var2 = new Envelope(MainFrame.thisinstance);
                  var2.setModel(var1);
               }

            }
         });
      } catch (EntityException var13) {
         this.error(var13);
      }

   }

   public void delete(long var1) {
      try {
         Entity var3 = EntityHome.getInstance().findByID(var1);
         if (var3 != null) {
            EntityHome.getInstance().remove(var3);
         }

         this.changed = true;
      } catch (EntityException var4) {
         this.error(var4);
      }

   }

   public void newEntity() {
      try {
         final CountDownLatch var1 = new CountDownLatch(1);
         final TypeSelectorPanel var2 = new TypeSelectorPanel(MDRepositoryMetaFactory.getMDRepositoryMeta().getEntityTypes());
         var2.pack();
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               var2.setVisible(true);
               var1.countDown();
            }
         });

         try {
            var1.await();
         } catch (InterruptedException var7) {
         }

         if (!"NONE".equals(var2.getTypeSelected())) {
            final CountDownLatch var3 = new CountDownLatch(1);
            final MDForm var4 = new MDForm(var2.getTypeSelected());
            var4.setTitle("Törzsadat");
            var4.setSize((int)((double)GuiUtil.getScreenW() * 0.6D), (int)((double)GuiUtil.getScreenH() * 0.5D));
            var4.setModal(true);
            var4.setLocationRelativeTo(MainFrame.thisinstance);
            var4.pack();
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  var4.setVisible(true);
                  var3.countDown();
               }
            });

            try {
               var3.await();
               this.changed = var4.isChanged();
            } catch (InterruptedException var6) {
            }
         }
      } catch (MDRepositoryException var8) {
         this.error(var8);
      }

   }

   public void modify(String var1, long var2) {
      final MDForm var4 = new MDForm(var1);
      final CountDownLatch var5 = new CountDownLatch(1);
      var4.loadEntity(var2);
      var4.setTitle("Törzsadat");
      var4.setSize((int)((double)GuiUtil.getScreenW() * 0.6D), (int)((double)GuiUtil.getScreenH() * 0.5D));
      var4.setModal(true);
      var4.setLocationRelativeTo(MainFrame.thisinstance);
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            var4.setVisible(true);
            var5.countDown();
         }
      });

      try {
         var5.await();
         this.changed = var4.isChanged();
      } catch (InterruptedException var7) {
         this.error(var7);
      }

   }

   private void error(Exception var1) {
      var1.printStackTrace();
   }
}
