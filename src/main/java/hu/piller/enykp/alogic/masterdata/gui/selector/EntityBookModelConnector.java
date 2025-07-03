package hu.piller.enykp.alogic.masterdata.gui.selector;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.masterdata.core.BlockDefinition;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityException;
import hu.piller.enykp.alogic.masterdata.core.EntityHome;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryException;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryMetaFactory;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.util.base.ErrorList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

public class EntityBookModelConnector {
   private EntitySelection[] selected;
   private Hashtable<String, String> masterData = new Hashtable();

   public void setSelectedEntities(EntitySelection[] var1) {
      this.selected = var1;
   }

   public void applyOnForm(EntitySelection[] var1, String var2) {
      this.setSelectedEntities(var1);
      this.applyOnForm(var2);
   }

   public void applyOnForm(String var1) {
      FormModel var2 = Calculator.getInstance().getBookModel().get(var1);
      boolean var3 = false;
      if (this.isEntityApplyableOnForm(var1)) {
         this.fillMD();
         Vector var4 = this.getFormFieldsWithPanids(var1);

         for(int var5 = 0; var5 < var4.size(); ++var5) {
            Hashtable var6 = (Hashtable)var4.get(var5);
            String var7 = this.getString(var6.get("fid"));
            String[] var8 = ((String)var6.get("panids")).split(",");

            for(int var9 = 0; var9 < var8.length; ++var9) {
               var8[var9] = var8[var9].trim();
               if ("Adótanácsadó bizonyítvány".equals(var8[var9])) {
                  var8[var9] = "Adótanácsadó Bizonyítvány";
               }

               if (this.masterData.containsKey(var8[var9])) {
                  String var10 = (String)this.masterData.get(var8[var9]);
                  if (("Adózó adószáma".equals(var8[var9]) || "Számla-azonosító".equals(var8[var9]) || "Születési időpont".equals(var8[var9])) && var10 != null) {
                     var10 = var10.replaceAll("-", "");
                  }

                  if (var10.length() > 0) {
                     if (this.isValueValid(var1, var7, var10)) {
                        int var11 = var2.get_field_pageindex(var7);
                        String var12 = "";

                        try {
                           var12 = (String)var2.get(var11).getByCode(var7).features.get("abev_mask");
                        } catch (Exception var16) {
                           var12 = "";
                        }

                        String var13;
                        try {
                           var13 = this.needDataByMask(var10, var12);
                        } catch (Exception var15) {
                           var13 = var10;
                        }

                        if (var13 != null) {
                           Calculator.getInstance().getBookModel().set_field_value(var7, var13);
                        }
                     } else {
                        var3 = true;
                     }
                  }
               }
            }

            CalculatorManager.getInstance().do_field_calculation(var1, var7);
         }
      }

      if (var3) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "Törzsadat betöltési hiba, a részletekért kérem nézze meg az Szerviz menüpont Üzenetek almenüjét!", "Figyelmeztetés", 1);
      }

   }

   private Vector getFormFieldsWithPanids(String var1) {
      Vector var2 = new Vector(Arrays.asList("panids"));
      return MetaInfo.getInstance().getMetaStore(var1).getFilteredFieldMetas_And(var2);
   }

   private Vector getGUIFormFieldsWithPanids(String var1) {
      Vector var2 = new Vector(Arrays.asList("panids"));
      return MetaInfo.getInstance().getMetaStore(var1).getFilteredFieldMetas_And(var2);
   }

   private boolean isEntityApplyableOnForm(String var1) {
      boolean var2 = false;
      var2 = this.selected != null && this.selected.length != 0 && Calculator.getInstance().getBookModel() != null && MetaInfo.getInstance().getMetaStore(var1) != null;
      return var2;
   }

   private boolean isValueValid(String var1, String var2, String var3) {
      return TemplateUtils.getInstance().checkByDataCheckerMD(Calculator.getInstance().getBookModel(), var1, var2, var3);
   }

   private void fillMD() {
      EntitySelection[] var1 = this.selected;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EntitySelection var4 = var1[var3];
         EntityHome var5 = new EntityHome();

         try {
            Entity var6 = var5.findByID(var4.getEntityId());
            BlockDefinition[] var7 = this.getEntityDefinition(var6);
            BlockDefinition[] var8 = var7;
            int var9 = var7.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               BlockDefinition var11 = var8[var10];
               int var12 = 1;
               int[] var13 = var4.getSelectedBlockSeqs(var11.getBlockName());
               if (var13.length > 0) {
                  var12 = var13[0];
               }

               String[] var14 = var11.getMasterDataNames();
               int var15 = var14.length;

               for(int var16 = 0; var16 < var15; ++var16) {
                  String var17 = var14[var16];
                  this.masterData.put(var17, var6.getBlock(var11.getBlockName(), var12).getMasterData(var17).getValue());
               }
            }
         } catch (EntityException var18) {
            ErrorList.getInstance().writeError(ErrorList.LEVEL_SHOW_ERROR, var18.getMessage() == null ? "törzsadat betöltési hiba" : var18.getMessage(), var18, (Object)null);
         } catch (MDRepositoryException var19) {
            ErrorList.getInstance().writeError(ErrorList.LEVEL_SHOW_ERROR, var19.getMessage() == null ? "törzsadat betöltési hiba" : var19.getMessage(), var19, (Object)null);
         }
      }

      this.postProcess();
   }

   private BlockDefinition[] getEntityDefinition(Entity var1) throws MDRepositoryException {
      return MDRepositoryMetaFactory.getMDRepositoryMeta().getBlockDefinitionsForEntity(var1.getName());
   }

   private String getString(Object var1) {
      return var1 == null ? "" : var1.toString();
   }

   private void postProcess() {
      StringBuffer var1;
      if (this.masterData.containsKey("Keresztneve") && this.masterData.containsKey("Vezetékneve")) {
         var1 = new StringBuffer();
         var1.append((String)this.masterData.get("Vezetékneve"));
         var1.append(" ");
         var1.append((String)this.masterData.get("Keresztneve"));
         this.masterData.put("Adózó neve", var1.toString());
      }

      if (this.masterData.containsKey("Anyja születési családneve") && this.masterData.containsKey("Anyja születési utóneve")) {
         var1 = new StringBuffer();
         var1.append((String)this.masterData.get("Anyja születési családneve"));
         var1.append(" ");
         var1.append((String)this.masterData.get("Anyja születési utóneve"));
         this.masterData.put("Anyja születési neve", var1.toString());
      }

      if (this.masterData.containsKey("Adózó neme")) {
         String var2 = (String)this.masterData.get("Adózó neme");
         if (var2 != null && !"".equals(var2.trim())) {
            if ("Férfi".equals(var2)) {
               var2 = "1";
            } else if ("Nő".equals(var2)) {
               var2 = "2";
            }

            this.masterData.put("Adózó neme", var2);
         }
      }

   }

   private String needDataByMask(String var1, String var2) {
      try {
         if ("".equals(var1)) {
            return var1;
         } else if ("".equals(var2)) {
            return var1;
         } else if (!var2.startsWith("#") && !var2.startsWith("%")) {
            int var3 = 0;

            for(int var4 = Math.min(var1.length(), var2.length()); var3 < var4 && var1.charAt(var3) == var2.charAt(var3); ++var3) {
            }

            return var1.substring(var3).trim().length() > 0 ? var1 : null;
         } else {
            return var1;
         }
      } catch (Exception var5) {
         return var1;
      }
   }
}
