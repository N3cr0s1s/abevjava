package hu.piller.enykp.alogic.masterdata.save;

import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityError;
import hu.piller.enykp.alogic.masterdata.core.EntityException;
import hu.piller.enykp.alogic.masterdata.core.EntityHome;
import hu.piller.enykp.alogic.masterdata.core.MasterData;
import hu.piller.enykp.alogic.masterdata.gui.entityfilter.TypeSelectorPanel;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryException;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryMetaFactory;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.ICommandObject;
import hu.piller.enykp.util.base.ErrorList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class SaverCommandObject implements ICommandObject {
   private static final String[] NOT_SAVEABLE_ENTITY_TYPES = new String[]{"Adótanácsadó", "Jogi képviselő"};

   public void execute() {
      SwingWorker var1 = new SwingWorker() {
         private Map<String, String> mdValues;
         private String entityType;
         private String[] saveableEntityTypes;

         protected Object doInBackground() throws Exception {
            try {
               this.mdValues = SaverCommandObject.this.getPanidsValuesFromMaindocument();
               this.entityType = MDType.adviseEntityType(this.mdValues);
               this.saveableEntityTypes = SaverCommandObject.this.getFormSaveableEntityTypes();
               return null;
            } catch (Exception var2) {
               return var2;
            }
         }

         protected void done() {
            Object var1 = null;

            try {
               var1 = this.get();
               if (var1 != null) {
                  SaverCommandObject.this.logAndNotifyError("Törzsadat mentés", ((Exception)var1).getMessage());
                  return;
               }
            } catch (InterruptedException var3) {
               SaverCommandObject.this.logAndNotifyError("Törzsadat mentés", ((Exception)var1).getMessage());
               return;
            } catch (ExecutionException var4) {
               SaverCommandObject.this.logAndNotifyError("Törzsadat mentés", ((Exception)var1).getMessage());
               return;
            }

            this.entityType = SaverCommandObject.this.selectEntityType(this.entityType, this.saveableEntityTypes);
            if (this.entityType != null) {
               SwingWorker var2 = new SwingWorker() {
                  private List<EntityError> changed = null;
                  private boolean isNew;
                  private Entity entity;

                  protected Object doInBackground() throws Exception {
                     try {
                        Entity var1 = EntityHome.getInstance().create(entityType);
                        var1.fill(mdValues);
                        Entity[] var2 = SaverCommandObject.this.searchForMatchingEntities(var1);
                        switch(var2.length) {
                        case 1:
                           this.entity = var2[0];
                           this.changed = this.entity.merge(var1);
                           this.isNew = false;
                           break;
                        default:
                           this.entity = var1;
                           this.changed = Collections.EMPTY_LIST;
                           this.isNew = true;
                        }

                        return null;
                     } catch (EntityException var3) {
                        return var3;
                     }
                  }

                  protected void done() {
                     Object var1 = null;

                     try {
                        var1 = this.get();
                        if (var1 != null) {
                           SaverCommandObject.this.logAndNotifyError("Törzsadat mentés", ((Exception)var1).getMessage());
                           return;
                        }
                     } catch (InterruptedException var3) {
                        SaverCommandObject.this.logAndNotifyError("Törzsadat mentés", ((Exception)var1).getMessage());
                        return;
                     } catch (ExecutionException var4) {
                        SaverCommandObject.this.logAndNotifyError("Törzsadat mentés", ((Exception)var1).getMessage());
                        return;
                     }

                     new SaverMDForm(this.entity, this.isNew, this.changed);
                  }
               };
               var2.run();
            }
         }
      };
      var1.run();
   }

   public void setParameters(Hashtable var1) {
   }

   public Object getState(Object var1) {
      try {
         int var2 = (Integer)var1;
         return var2 == 1 ? true : false;
      } catch (Exception var3) {
         return false;
      }
   }

   private Map<String, String> getPanidsValuesFromMaindocument() {
      Map var1 = Calculator.getInstance().getBookModel().getPanidsValueFromDocument(Calculator.getInstance().getBookModel().main_document_id);
      this.postProcess(var1);
      return var1;
   }

   private void postProcess(Map<String, String> var1) {
      String var2;
      if (var1.containsKey("Adózó adószáma")) {
         var2 = this.formatAdoszamValue((String)var1.get("Adózó adószáma"));
         var1.put("Adózó adószáma", var2);
      }

      if (var1.containsKey("Adózó neme")) {
         var2 = (String)var1.get("Adózó neme");
         if (var2 != null && !"".equals(var2.trim())) {
            if ("1".equals(var2)) {
               var2 = "Férfi";
            } else if ("2".equals(var2)) {
               var2 = "Nő";
            }

            var1.put("Adózó neme", var2);
         }
      }

   }

   private String formatAdoszamValue(String var1) {
      if ("".equals(var1)) {
         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();

         for(int var3 = 0; var3 < var1.length(); ++var3) {
            if (var3 == 8 || var3 == 9) {
               var2.append('-');
            }

            var2.append(var1.charAt(var3));
         }

         return var2.toString();
      }
   }

   private String selectEntityType(String var1, String[] var2) {
      boolean var3 = false;
      String var4 = null;
      TypeSelectorPanel var5 = null;

      do {
         var5 = new TypeSelectorPanel(var2);
         var5.pack();
         var5.setSelected(var1);
         var5.setVisible(true);
         var4 = var5.getTypeSelected();
         if ("NONE".equals(var4)) {
            if (!this.isContinue()) {
               return null;
            }

            var4 = "";
         } else {
            var3 = true;
         }
      } while(!var3);

      return var4;
   }

   private boolean isContinue() {
      int var1 = JOptionPane.showConfirmDialog(MainFrame.thisinstance, "Nem választott típust, kilép a funkcióból?", "Törzsadat mentése", 0);
      return var1 != 0;
   }

   private void logAndNotifyError(String var1, String var2) {
      ErrorList.getInstance().store(ErrorList.LEVEL_SHOW_WARNING, "Törzsadatok mentése : " + var2, (Exception)null, (Object)null);
      GuiUtil.showMessageDialog(MainFrame.thisinstance, var1, "Törzsadat mentési hiba, részleteket megtalálja a Szerviz -> Üzenetek között!", 0);
   }

   public String[] getFormSaveableEntityTypes() throws MDRepositoryException {
      String[] var2 = MDRepositoryMetaFactory.getMDRepositoryMeta().getEntityTypes();
      String[] var1 = new String[var2.length - NOT_SAVEABLE_ENTITY_TYPES.length];
      int var3 = 0;

      for(int var4 = 0; var3 < var2.length; ++var3) {
         if (this.isSaveable(var2[var3])) {
            var1[var4++] = var2[var3];
         }
      }

      return var1;
   }

   private boolean isSaveable(String var1) {
      String[] var2 = NOT_SAVEABLE_ENTITY_TYPES;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         if (var5.equals(var1)) {
            return false;
         }
      }

      return true;
   }

   private Entity[] searchForMatchingEntities(Entity var1) throws EntityException {
      MasterData[] var2 = null;
      if ("Magánszemély".equals(var1.getName())) {
         var2 = new MasterData[]{var1.getBlock("Törzsadatok").getMasterData("Adózó adóazonosító jele")};
      } else if ("Egyéni vállalkozó".equals(var1.getName())) {
         var2 = new MasterData[]{var1.getBlock("Törzsadatok").getMasterData("Adózó adóazonosító jele"), var1.getBlock("Törzsadatok").getMasterData("Adózó adószáma")};
      } else if ("Társaság".equals(var1.getName())) {
         var2 = new MasterData[]{var1.getBlock("Törzsadatok").getMasterData("Adózó adószáma")};
      }

      return EntityHome.getInstance().findByTypeAndMasterData(var1.getName(), var2);
   }
}
