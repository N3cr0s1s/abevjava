package hu.piller.enykp.alogic.masterdata.gui;

import hu.piller.enykp.alogic.masterdata.core.BlockDefinition;
import hu.piller.enykp.alogic.masterdata.core.EntityError;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.base.SwingWorker;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class MDForm extends JDialog implements ActionListener {
   public static final String CMD_SAVE = "SAVE";
   public static final String CMD_CANCEL = "CANCEL";
   protected String entityType;
   protected MDFormController controller;
   protected Vector<MDBlockComponent> blockComponents;
   private Hashtable<String, JPanel> panels;
   private JPanel mainPanel;
   protected JButton btnOk;
   protected JButton btnCancel;
   private final boolean[] changed = new boolean[]{false};

   public boolean isChanged() {
      return this.changed[0];
   }

   public MDForm(String var1) {
      super(MainFrame.thisinstance);
      this.entityType = var1;
      this.blockComponents = new Vector();
      this.controller = new MDFormController();
      this.panels = new Hashtable();
      this.prepareLayoutForEntity(var1);
      BlockDefinition[] var2 = this.controller.getEntityDefinition(var1);
      BlockDefinition[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BlockDefinition var6 = var3[var5];
         MDBlockComponent var7 = new MDBlockComponent(var6, this.isBordered(var6));
         this.blockComponents.add(var7);
         String var8 = this.controller.getOrgForMasterData(var6.getMasterDataNames()[0]);
         if (var8 != null) {
            ((JPanel)this.panels.get(var8)).add(var7.getLayout());
         }
      }

      Iterator var9 = this.panels.keySet().iterator();

      while(var9.hasNext()) {
         String var10 = (String)var9.next();
         ((JPanel)this.panels.get(var10)).add(Box.createVerticalGlue());
      }

   }

   public void loadEntity(long var1) {
      Vector var3 = this.controller.read(var1);
      this.controller.setEntityIDForUpdate(var1);
      this.showData(var3);
   }

   public void showData(Vector<MDBlockComponentBean> var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         MDBlockComponentBean var3 = (MDBlockComponentBean)var2.next();
         MDBlockComponent var4 = this.getBlockComponentByBlockType(var3.getBlockType());

         while(var4.getBlockComponentBean().getSize() != var3.getSize()) {
            var4.getBlockComponentBean().addEmptyDataRecord();
         }

         for(int var5 = 1; var5 <= var3.getSize(); ++var5) {
            String[] var6 = var4.getMasterDataInBlock();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String var9 = var6[var8];
               var4.getBlockComponentBean().setMDValue(var5, var9, var3.getMDValue(var5, var9));
            }
         }
      }

      this.refreshGUI();
   }

   public void refreshGUI() {
      Iterator var1 = this.blockComponents.iterator();

      while(var1.hasNext()) {
         MDBlockComponent var2 = (MDBlockComponent)var1.next();
         var2.refreshGUIState();
      }

   }

   public MDBlockComponent getBlockComponentByBlockType(String var1) {
      Iterator var2 = this.blockComponents.iterator();

      MDBlockComponent var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (MDBlockComponent)var2.next();
      } while(!var3.isComponentForBlock(var1));

      return var3;
   }

   public Vector<MDBlockComponent> getBlockcomponents() {
      return this.blockComponents;
   }

   private boolean isBordered(BlockDefinition var1) {
      return var1.getMax() > 1 || var1.getMasterDataNames().length > 1;
   }

   private void prepareLayoutForEntity(String var1) {
      String[] var2 = this.controller.getOrgsForEntity(var1);
      if (var2.length > 0) {
         var2 = this.sortByName(var2);
      }

      String[] var3 = var2;
      int var4 = var2.length;

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         JPanel var7 = new JPanel();
         var7.setLayout(new BoxLayout(var7, 1));
         this.panels.put(var6, var7);
      }

      this.mainPanel = new JPanel();
      this.add(this.mainPanel);
      this.mainPanel.setLayout(new BoxLayout(this.mainPanel, 1));
      JTabbedPane var9 = new JTabbedPane();
      this.mainPanel.add(var9);
      String[] var10 = var2;
      var5 = var2.length;

      for(int var11 = 0; var11 < var5; ++var11) {
         String var12 = var10[var11];
         String var8 = var12;
         if ("ALL".equals(var12)) {
            var8 = "Általános adatok";
         }

         var9.addTab(var8, (Icon)null, new JScrollPane((Component)this.panels.get(var12)));
      }

      this.mainPanel.add(Box.createVerticalStrut(5));
      this.mainPanel.add(this.createRowForButtons());
      this.mainPanel.add(Box.createVerticalStrut(5));
   }

   private String[] sortByName(String[] var1) {
      ArrayList var2 = new ArrayList();
      String[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         var2.add(var6);
      }

      Collections.sort(var2);
      return (String[])((String[])var2.toArray(new String[var2.size()]));
   }

   private JPanel createRowForButtons() {
      JPanel var1 = new JPanel();
      var1.setLayout(new BoxLayout(var1, 0));
      var1.add(Box.createHorizontalGlue());
      this.btnOk = this.createButton("SAVE", "Vissza a listához", this, 170, 30);
      var1.add(this.btnOk);
      var1.add(Box.createVerticalStrut(5));
      this.btnCancel = this.createButton("CANCEL", "Mégsem", this, 170, 30);
      var1.add(this.btnCancel);
      var1.add(Box.createHorizontalGlue());
      return var1;
   }

   private JButton createButton(String var1, String var2, ActionListener var3, int var4, int var5) {
      JButton var6 = new JButton(var2);
      var6.setActionCommand(var1);
      var6.setPreferredSize(new Dimension(GuiUtil.getW(var6, var2), GuiUtil.getCommonItemHeight() + 2));
      var6.setMaximumSize(var6.getPreferredSize());
      var6.addActionListener(var3);
      return var6;
   }

   protected void cleanErrors() {
      Iterator var1 = this.blockComponents.iterator();

      while(var1.hasNext()) {
         MDBlockComponent var2 = (MDBlockComponent)var1.next();
         var2.cleanErrors();
      }

   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = var1.getActionCommand();
      final EntityError[][] var3 = new EntityError[1][];
      if ("SAVE".equals(var2)) {
         final Vector var4 = new Vector();
         Iterator var5 = this.blockComponents.iterator();

         while(var5.hasNext()) {
            MDBlockComponent var6 = (MDBlockComponent)var5.next();
            var6.storeDataInFields();
            var4.add(var6.getBlockComponentBean());
         }

         SwingWorker var7 = new SwingWorker() {
            public Object construct() {
               MDForm.this.cleanErrors();
               var3[0] = MDForm.this.controller.save(MDForm.this.entityType, var4);
               EntityError[] var1 = var3[0];
               int var2 = var1.length;

               for(int var3x = 0; var3x < var2; ++var3x) {
                  EntityError var4x = var1[var3x];
                  Iterator var5 = MDForm.this.blockComponents.iterator();

                  while(var5.hasNext()) {
                     MDBlockComponent var6 = (MDBlockComponent)var5.next();
                     if (var6.isComponentForBlock(var4x.getBlockName())) {
                        var6.addError(var4x);
                        var6.setMarkerToError();
                     }
                  }
               }

               return null;
            }

            public void finished() {
               if (var3[0].length > 0) {
                  MDForm.this.refreshGUI();
               } else {
                  MDForm.this.changed[0] = MDForm.this.controller.hasChanged();
                  MDForm.this.dispose();
               }

            }
         };
         var7.start();
      } else if ("CANCEL".equals(var2)) {
         this.dispose();
      }

   }
}
