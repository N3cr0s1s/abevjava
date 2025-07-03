package hu.piller.enykp.alogic.archivemanager;

import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.ArchiveManagerBusiness;
import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.ArchiveManagerPanel;
import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.ExecHandler.ProgressPanel;
import hu.piller.enykp.util.base.eventsupport.CloseEvent;
import hu.piller.enykp.util.base.eventsupport.DefaultEventSupport;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ArchiveManagerDialogPanel extends JPanel implements IEventListener, IEventSupport {
   public static final String COMPONENT_STOP_BUTTON = "stop_button";
   public static final String COMPONENT_EXIT_BUTTON = "exit_button";
   public static final String COMPONENT_ARCHIVE_MANGER_PANEL = "archive_panel";
   public static final String COMPONENT_PROGRESS_PANEL = "progress_panel";
   private ArchiveManagerDialogBusiness amd_business;
   private ArchiveManagerBusiness amb;
   private Dimension panelPreferredSize = new Dimension(800, 600);
   private Dimension panelMinSize = new Dimension(800, 600);
   private JPanel buttons_panel;
   private JPanel amp;
   private JButton btn_stop;
   private JButton btn_exit;
   private JPanel progress_panel;
   private JPanel footer_panel;
   private ArchiveManagerDialog amd;
   private DefaultEventSupport des = new DefaultEventSupport();

   public void addEventListener(IEventListener var1) {
      this.des.addEventListener(var1);
   }

   public void removeEventListener(IEventListener var1) {
      this.des.removeEventListener(var1);
   }

   public Vector fireEvent(Event var1) {
      return this.des.fireEvent(var1);
   }

   public ArchiveManagerDialogPanel(ArchiveManagerDialog var1) {
      this.amd = var1;
      this.build();
      this.prepare();
   }

   private void build() {
      this.setPreferredSize(this.panelPreferredSize);
      this.setMaximumSize(this.panelPreferredSize);
      this.setMinimumSize(this.panelMinSize);
      this.setLayout(new BorderLayout());
      this.add(this.getFooterPanel(), "South");
      this.add(this.amp = new ArchiveManagerPanel(this, this.amd), "Center");
   }

   public ArchiveManagerDialog getAmd() {
      return this.amd;
   }

   private void prepare() {
      this.amd_business = new ArchiveManagerDialogBusiness(this);
   }

   public ArchiveManagerBusiness getAmb() {
      return this.amb;
   }

   private JPanel getFooterPanel() {
      if (this.footer_panel == null) {
         this.footer_panel = new JPanel(new BorderLayout());
         this.footer_panel.add(this.getButtonsPanel(), "Center");
         this.progress_panel = new ProgressPanel();
         this.footer_panel.add(this.progress_panel, "South");
      }

      return this.footer_panel;
   }

   private JPanel getButtonsPanel() {
      if (this.buttons_panel == null) {
         FlowLayout var1 = new FlowLayout();
         var1.setAlignment(2);
         var1.setVgap(10);
         this.buttons_panel = new JPanel();
         this.buttons_panel.setLayout(var1);
         this.buttons_panel.add(this.getJButton(), (Object)null);
         this.buttons_panel.add(this.getExitButton(), (Object)null);
      }

      return this.buttons_panel;
   }

   private JButton getJButton() {
      if (this.btn_stop == null) {
         this.btn_stop = new JButton();
         this.btn_stop.setText("Leállítás");
         this.btn_stop.setEnabled(false);
      }

      return this.btn_stop;
   }

   private JButton getExitButton() {
      if (this.btn_exit == null) {
         this.btn_exit = new JButton();
         this.btn_exit.setText("Bezárás");
         this.btn_exit.setEnabled(true);
      }

      return this.btn_exit;
   }

   public JComponent getAMDComponent(String var1) {
      if ("stop_button".equalsIgnoreCase(var1)) {
         return this.btn_stop;
      } else if ("exit_button".equalsIgnoreCase(var1)) {
         return this.btn_exit;
      } else if ("archive_panel".equalsIgnoreCase(var1)) {
         return this.amp;
      } else {
         return "progress_panel".equalsIgnoreCase(var1) ? this.progress_panel : null;
      }
   }

   public Object eventFired(Event var1) {
      if (var1 instanceof CloseEvent) {
         if (this.amb != null) {
            this.amb.stopOperation();
         }

         this.amd.setVisible(false);
      }

      return null;
   }

   public void setAmb(ArchiveManagerBusiness var1) {
      this.amb = var1;
   }
}
