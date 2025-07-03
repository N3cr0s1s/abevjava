package hu.piller.enykp.alogic.primaryaccount;

import hu.piller.enykp.alogic.primaryaccount.companies.CompaniesPanel;
import hu.piller.enykp.alogic.primaryaccount.people.PeoplePanel;
import hu.piller.enykp.alogic.primaryaccount.smallbusiness.SmallBusinessPanel;
import hu.piller.enykp.alogic.primaryaccount.taxexperts.TaxExpertPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class PrimaryAccountsPanel extends JPanel {
   public static final int ID_COMPANY = 0;
   public static final int ID_SMALLBUSINESS = 1;
   public static final int ID_PEOPLE = 2;
   public static final int ID_TAXEXPERT = 3;
   public static final int IDX_TITLE = 0;
   public static final int IDX_PANEL = 1;
   public static final String COMPONENT_TABS = "tabs";
   private static final long serialVersionUID = 1L;
   private JTabbedPane tbp_tabs = null;
   private final Object[][] tabs_panels = new Object[4][2];
   private final Object[][] guest_tabs_panels = new Object[4][2];
   private final int[] tabs_order = new int[]{0, 1, 2, 3};
   private PrimaryAccountsBusiness pa_business;
   private JDialog dialog;
   private JFrame frame;
   private PAInfo pa_info;

   public void firePrimaryAccountChanged() {
      this.pa_info.setLastModTime(System.currentTimeMillis());
   }

   public PrimaryAccountsPanel(JDialog var1, PAInfo var2) {
      this.dialog = var1;
      this.pa_info = var2;
      this.initialize();
      this.prepare();
   }

   public PrimaryAccountsPanel(JFrame var1, PAInfo var2) {
      this.frame = var1;
      this.pa_info = var2;
      this.initialize();
      this.prepare();
   }

   private void initialize() {
      this.setLayout(new BorderLayout());
      this.setSize(new Dimension(602, 342));
      this.add(this.getTbp_tabs(), "Center");
   }

   private void prepare() {
      this.pa_business = new PrimaryAccountsBusiness(this);
   }

   private JTabbedPane getTbp_tabs() {
      if (this.tbp_tabs == null) {
         this.tabs_panels[0][0] = "Társaságok";
         this.tabs_panels[0][1] = new CompaniesPanel(this);
         this.tabs_panels[1][0] = "Egyéni vállalkozók";
         this.tabs_panels[1][1] = new SmallBusinessPanel(this);
         this.tabs_panels[2][0] = "Magánszemélyek";
         this.tabs_panels[2][1] = new PeoplePanel(this);
         this.tabs_panels[3][0] = "Adótanácsadók";
         this.tabs_panels[3][1] = new TaxExpertPanel(this);
         this.tbp_tabs = new JTabbedPane();
         this.showPanels();
      }

      return this.tbp_tabs;
   }

   public JComponent getPAPComponent(String var1) {
      return "tabs".equalsIgnoreCase(var1) ? this.tbp_tabs : null;
   }

   public PrimaryAccountsBusiness getBusiness() {
      return this.pa_business;
   }

   public JPanel getPanel(int var1) {
      switch(var1) {
      case 0:
      case 1:
      case 2:
      case 3:
         return (JPanel)this.tabs_panels[var1][1];
      default:
         return null;
      }
   }

   public void setGuestPanel(int var1, String var2, JPanel var3) {
      switch(var1) {
      case 0:
      case 1:
      case 2:
      case 3:
         this.guest_tabs_panels[var1][0] = var2;
         this.guest_tabs_panels[var1][1] = var3;
         this.showPanels();
      default:
      }
   }

   public void restorePanel(int var1) {
      switch(var1) {
      case 0:
      case 1:
      case 2:
      case 3:
         this.guest_tabs_panels[var1][0] = null;
         this.guest_tabs_panels[var1][1] = null;
         this.showPanels();
      default:
      }
   }

   private void showPanels() {
      int var3 = 0;

      for(int var4 = this.tabs_panels.length; var3 < var4; ++var3) {
         JPanel var1 = (JPanel)this.guest_tabs_panels[this.tabs_order[var3]][1];
         String var2 = (String)this.guest_tabs_panels[this.tabs_order[var3]][0];
         if (var1 == null) {
            var1 = (JPanel)this.tabs_panels[this.tabs_order[var3]][1];
            var2 = (String)this.tabs_panels[this.tabs_order[var3]][0];
         }

         if (this.tbp_tabs.getTabCount() < var3 + 1) {
            this.tbp_tabs.addTab("?", (Component)null);
         }

         this.tbp_tabs.setComponentAt(var3, var1);
         this.tbp_tabs.setTitleAt(var3, var2);
      }

      Dimension var5 = this.tbp_tabs.getSize();
      Dimension var6 = this.tbp_tabs.getLayout().preferredLayoutSize(this.tbp_tabs);
      this.tbp_tabs.setPreferredSize(new Dimension(var5.width, var5.height > var6.height ? var5.height : var6.height));
      if (this.frame != null) {
         this.frame.pack();
         this.frame.setLocationRelativeTo((Component)null);
      } else if (this.dialog != null) {
         this.dialog.pack();
         this.dialog.setLocationRelativeTo(this.dialog.getOwner());
      }

   }

   public void back() {
      if (this.checkGuestTabPanels(this)) {
         if (this.frame != null) {
            this.frame.setVisible(false);
         } else if (this.dialog != null) {
            this.dialog.setVisible(false);
         }

         this.pa_info.closeDialog();
      }
   }

   private boolean checkGuestTabPanels(Component var1) {
      int var2 = 0;

      for(int var3 = this.guest_tabs_panels.length; var2 < var3; ++var2) {
         if (this.guest_tabs_panels[this.tabs_order[var2]][1] != null) {
            int var4 = JOptionPane.showConfirmDialog(var1, "A " + (this.tabs_order[var2] + 1) + ". fülön (" + this.tabs_panels[this.tabs_order[var2]][0] + ") befejezetlen munka van !\nBiztosan elhagyja a törzsadat kezelőt ?", "Törzsadat kezelés", 0, 2);
            if (var4 == 1) {
               return false;
            }
         }
      }

      return true;
   }
}
