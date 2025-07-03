package hu.piller.enykp.alogic.masterdata.envelope;

import hu.piller.enykp.alogic.masterdata.envelope.gui.EnvelopePreviewPanel;
import hu.piller.enykp.alogic.masterdata.envelope.model.Address;
import hu.piller.enykp.alogic.masterdata.envelope.model.AddressOpt;
import hu.piller.enykp.alogic.masterdata.envelope.model.EnvelopeModel;
import hu.piller.enykp.alogic.masterdata.envelope.model.OrgModel;
import hu.piller.enykp.alogic.masterdata.envelope.print.EnvelopePrintJob;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class Envelope extends JDialog implements PropertyChangeListener, ActionListener {
   private EnvelopeModel model;
   private boolean cimFromCimOptions;
   private JPanel mainPanel;
   private EnvelopePreviewPanel envelopePreview;
   private JPanel envelopeAddresses;
   private JPanel addresseeOptions;
   private JPanel buttons;
   private ButtonGroup feladoOptions;
   private JRadioButton feladoLevelezesiCim;
   private JRadioButton feladoAllandoCim;
   private JComboBox cimzettOptions;
   private JComboBox orgOptions;
   private JComboBox cimOptions;
   private JButton print;
   private JButton close;

   public Envelope(JFrame var1) {
      super(var1);
      this.setName("EnvelopePrint");
      this.setTitle("Borítéknyomtatás előnézete");
      this.setDialogPosition();
      this.setMinimumSize(new Dimension(GuiUtil.getW("Levelezési címeWWWWWFeladó lehetséges illetékes címzettjeiWWWWW"), GuiUtil.getScreenH() / 2));
      this.setResizable(true);
      this.initComponents();
      this.pack();
      this.setVisible(true);
   }

   public void setModel(EnvelopeModel var1) {
      this.model = var1;
      this.firePropertyChange("model", (Object)null, (Object)null);
      this.showInfoDialog("levelezesi");
   }

   public EnvelopeModel getModel() {
      return this.model;
   }

   public AddressOpt getSelectedFelado() {
      String var2 = this.feladoOptions.getSelection().getActionCommand();
      AddressOpt var1;
      if ("levelezesi".equals(var2)) {
         var1 = this.model.getFeladoLevelezesiCim();
      } else {
         var1 = this.model.getFeladoAllandoCim();
      }

      return var1;
   }

   public AddressOpt getSelectedCimzett() {
      AddressOpt var1 = null;
      if (this.cimFromCimOptions) {
         var1 = (AddressOpt)this.cimOptions.getSelectedItem();
      } else if (this.cimzettOptions.getSelectedItem() != null) {
         String var2 = this.cimzettOptions.getSelectedItem().toString();
         ArrayList var3 = this.model.getCimzett(this.getSelectedFelado());
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            AddressOpt var5 = (AddressOpt)var4.next();
            if (var5.getTitleHint().equals(var2)) {
               var1 = var5;
               break;
            }
         }
      }

      return var1;
   }

   public boolean isOrgSelectable() {
      return this.model.isOrgSelectable();
   }

   public void propertyChange(PropertyChangeEvent var1) {
      if ("model".equals(var1.getPropertyName())) {
         this.orgOptions.removeAllItems();
         Iterator var2 = this.model.getSzervezetek().iterator();

         while(var2.hasNext()) {
            OrgModel var3 = (OrgModel)var2.next();
            this.orgOptions.addItem(var3);
            if (this.model.getCurrOrgId().equals(var3.getId())) {
               this.orgOptions.setSelectedItem(var3);
            }
         }
      }

   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = var1.getActionCommand();
      this.deactivateActionListeners();
      if (!"levelezesi".equals(var2) && !"allando".equals(var2)) {
         if ("cimzettOptions".equals(var2)) {
            this.cimzettChanged();
         } else if ("orgOptions".equals(var2)) {
            this.orgChanged();
         } else if ("cimOptions".equals(var2)) {
            this.cimlistaChanged();
            this.cimFromCimOptions = true;
         }
      } else {
         this.cimFromCimOptions = false;
         this.feladoChanged();
      }

      this.showInfoDialog(var2);
      this.envelopePreview.invalidate();
      this.envelopePreview.repaint();
      this.activateActionListeners();
   }

   private void showInfoDialog(String var1) {
      if ("levelezesi".equals(var1)) {
         if (!this.model.hasShireCode() && "".equals(this.model.getFeladoLevelezesiCim().getFormattedZip())) {
            GuiUtil.showMessageDialog(this, "A feladó levelezési címének irányítószáma nincs kitöltve, válassza ki az illetékes igazgatóságot a listából!", "Üzenet", 1);
         }
      } else if ("allando".equals(var1) && !this.model.hasShireCode() && "".equals(this.model.getFeladoAllandoCim().getFormattedZip())) {
         GuiUtil.showMessageDialog(this, "A feladó állandó címének irányítószáma nincs kitöltve, válassza ki az illetékes igazgatóságot a listából!", "Üzenet", 1);
      }

   }

   private void feladoChanged() {
      ArrayList var1 = this.model.getCimzett(this.getSelectedFelado());
      if (var1.isEmpty()) {
         var1.add((AddressOpt)this.cimOptions.getSelectedItem());
         this.cimFromCimOptions = true;
      }

      this.cimzettOptions.removeAllItems();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         AddressOpt var3 = (AddressOpt)var2.next();
         this.cimzettOptions.addItem(var3.getTitleHint());
      }

      this.cimzettOptions.setSelectedItem(var1.get(0));
      this.cimOptions.setSelectedItem(var1.get(0));
   }

   private void cimzettChanged() {
      if (!this.cimFromCimOptions) {
         ArrayList var1 = this.model.getCimzett(this.getSelectedFelado());
         String var2 = this.cimzettOptions.getSelectedItem().toString();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            AddressOpt var4 = (AddressOpt)var3.next();
            if (var2.equals(var4.getTitleHint())) {
               this.cimOptions.setSelectedItem(var4);
               break;
            }
         }
      }

   }

   private void orgChanged() {
      this.model.updateCimzettListaForOrg(((OrgModel)this.orgOptions.getSelectedItem()).getId());
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.model.getSzervezetCimei().iterator();

      while(var2.hasNext()) {
         Address var3 = (Address)var2.next();
         Iterator var4 = var3.getAddressOpts().iterator();

         while(var4.hasNext()) {
            AddressOpt var5 = (AddressOpt)var4.next();
            if (!var1.contains(var5)) {
               var1.add(var5);
            }
         }
      }

      Collections.sort(var1, new Envelope.CimOptionsComparator());
      this.cimOptions.removeAllItems();
      var2 = var1.iterator();

      while(var2.hasNext()) {
         AddressOpt var7 = (AddressOpt)var2.next();
         this.cimOptions.addItem(var7);
      }

      ArrayList var6 = this.model.getCimzett(this.getSelectedFelado());
      if (var6.size() == 0) {
         var6.add((AddressOpt)this.cimOptions.getItemAt(0));
         this.cimFromCimOptions = true;
      }

      this.cimzettOptions.removeAllItems();
      Iterator var8 = var6.iterator();

      while(var8.hasNext()) {
         AddressOpt var9 = (AddressOpt)var8.next();
         this.cimzettOptions.addItem(var9.getTitleHint());
      }

      this.cimzettOptions.setSelectedItem(var6.get(0));
      this.cimOptions.setSelectedItem(var6.get(0));
   }

   private void cimlistaChanged() {
      AddressOpt var1 = (AddressOpt)this.cimOptions.getSelectedItem();
      this.cimzettOptions.removeAllItems();
      this.cimzettOptions.addItem(var1.getTitleHint());
   }

   private void deactivateActionListeners() {
      this.feladoLevelezesiCim.removeActionListener(this);
      this.feladoAllandoCim.removeActionListener(this);
      this.cimzettOptions.removeActionListener(this);
      this.orgOptions.removeActionListener(this);
      this.cimOptions.removeActionListener(this);
   }

   private void activateActionListeners() {
      this.feladoLevelezesiCim.addActionListener(this);
      this.feladoAllandoCim.addActionListener(this);
      this.cimzettOptions.addActionListener(this);
      this.orgOptions.addActionListener(this);
      this.cimOptions.addActionListener(this);
   }

   private void setDialogPosition() {
      int var1 = this.getOwner().getLocation().x;
      int var2 = this.getOwner().getLocation().y;
      Dimension var3 = this.getOwner().getSize();
      int var4 = var1 + (int)var3.getWidth() / 2 - 320;
      int var5 = var2 + (int)var3.getHeight() / 2 - 262;
      this.setLocation(var4, var5);
   }

   private void initComponents() {
      this.add(this.getMainPanel());
      this.getMainPanel().add(this.getEnvelopePreview());
      this.getMainPanel().add(Box.createVerticalStrut(10));
      this.getMainPanel().add(this.getEnvelopeAddresses());
      this.getMainPanel().add(Box.createVerticalStrut(10));
      this.getMainPanel().add(this.getAddresseeOptions());
      this.getMainPanel().add(Box.createVerticalStrut(10));
      this.getMainPanel().add(this.getButtons());
      this.addPropertyChangeListener(this);
      this.activateActionListeners();
   }

   private JPanel getMainPanel() {
      if (this.mainPanel == null) {
         this.mainPanel = new JPanel();
         this.mainPanel.setSize(this.getWidth() - this.getInsets().left - this.getInsets().right, this.getHeight() - this.getInsets().top - this.getInsets().bottom);
         this.mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
         this.mainPanel.setLayout(new BoxLayout(this.mainPanel, 1));
      }

      return this.mainPanel;
   }

   private EnvelopePreviewPanel getEnvelopePreview() {
      if (this.envelopePreview == null) {
         this.envelopePreview = new EnvelopePreviewPanel(this);
         this.envelopePreview.setMinimumSize(new Dimension(630, (GuiUtil.getCommonItemHeight() + 2) * 25));
      }

      return this.envelopePreview;
   }

   private JPanel getEnvelopeAddresses() {
      if (this.envelopeAddresses == null) {
         this.envelopeAddresses = new JPanel();
         this.envelopeAddresses.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
         this.envelopeAddresses.setLayout(new BoxLayout(this.envelopeAddresses, 0));
         JPanel var1 = new JPanel(new GridLayout(0, 1));
         this.getFeladoOptions();
         var1.setBorder(BorderFactory.createTitledBorder("Feladó"));
         var1.setMinimumSize(this.feladoLevelezesiCim.getSize());
         var1.add(this.feladoLevelezesiCim);
         var1.add(this.feladoAllandoCim);
         var1.setMinimumSize(new Dimension((int)this.feladoLevelezesiCim.getSize().getWidth(), 4 * (GuiUtil.getCommonItemHeight() + 4)));
         JPanel var2 = new JPanel();
         var2.setBorder(BorderFactory.createTitledBorder("Feladó lehetséges illetékes címzettjei"));
         var2.setMinimumSize(new Dimension(GuiUtil.getW("Feladó lehetséges illetékes címzettjeiWWWWW"), 4 * (GuiUtil.getCommonItemHeight() + 4)));
         var2.add(this.getCimzettOptions());
         this.envelopeAddresses.setMinimumSize(new Dimension((int)(var1.getMaximumSize().getWidth() + 20.0D + var2.getMaximumSize().getWidth()), 4 * (GuiUtil.getCommonItemHeight() + 4) + 10));
         this.envelopeAddresses.setMaximumSize(new Dimension((int)(var1.getMaximumSize().getWidth() + 20.0D + var2.getMaximumSize().getWidth()), 4 * (GuiUtil.getCommonItemHeight() + 4) + 10));
         this.envelopeAddresses.add(var1);
         this.envelopeAddresses.add(Box.createHorizontalStrut(5));
         this.envelopeAddresses.add(var2);
      }

      return this.envelopeAddresses;
   }

   private ButtonGroup getFeladoOptions() {
      if (this.feladoOptions == null) {
         this.feladoOptions = new ButtonGroup();
         this.feladoLevelezesiCim = this.getRadioButton("Levelezési címe", "levelezesi", 76, true);
         this.feladoOptions.add(this.feladoLevelezesiCim);
         this.feladoAllandoCim = this.getRadioButton("Állandó címe", "allando", 65, false);
         this.feladoOptions.add(this.feladoAllandoCim);
      }

      return this.feladoOptions;
   }

   private JRadioButton getRadioButton(String var1, String var2, int var3, boolean var4) {
      JRadioButton var5 = GuiUtil.getANYKRadioButton(var1);
      var5.setActionCommand(var2);
      var5.setMnemonic(var3);
      var5.setSelected(var4);
      var5.setSize(new Dimension(GuiUtil.getW(var5, var5.getText()), GuiUtil.getCommonItemHeight() + 2));
      return var5;
   }

   private JComboBox getCimzettOptions() {
      if (this.cimzettOptions == null) {
         this.cimzettOptions = new JComboBox();
         this.cimzettOptions.setSize(new Dimension(400, GuiUtil.getCommonItemHeight() + 4));
         this.cimzettOptions.setMinimumSize(this.cimzettOptions.getSize());
         this.cimzettOptions.setPreferredSize(this.cimzettOptions.getSize());
         this.cimzettOptions.setActionCommand("cimzettOptions");
      }

      return this.cimzettOptions;
   }

   private JPanel getAddresseeOptions() {
      if (this.addresseeOptions == null) {
         this.addresseeOptions = new JPanel();
         this.addresseeOptions.setBorder(BorderFactory.createTitledBorder("A kiválasztott szervezet összes címe"));
         GridLayout var1 = new GridLayout(0, 1);
         var1.setVgap(5);
         this.addresseeOptions.setLayout(var1);
         this.addresseeOptions.setMinimumSize(new Dimension(630, 3 * (GuiUtil.getCommonItemHeight() + 6)));
         this.addresseeOptions.setPreferredSize(new Dimension(630, 3 * (GuiUtil.getCommonItemHeight() + 6)));
         this.addresseeOptions.setSize(new Dimension(630, 3 * (GuiUtil.getCommonItemHeight() + 6)));
         this.addresseeOptions.setMaximumSize(new Dimension(630, 3 * (GuiUtil.getCommonItemHeight() + 6)));
         this.addresseeOptions.add(this.getOrgOptions());
         this.addresseeOptions.add(this.getCimOptions());
      }

      return this.addresseeOptions;
   }

   private JComboBox getOrgOptions() {
      if (this.orgOptions == null) {
         this.orgOptions = new JComboBox();
         this.orgOptions.setMinimumSize(new Dimension(this.addresseeOptions.getWidth() - 2 * this.addresseeOptions.getBorder().getBorderInsets(this.addresseeOptions).left, GuiUtil.getCommonItemHeight() + 4));
         this.orgOptions.setPreferredSize(this.orgOptions.getMinimumSize());
         this.orgOptions.setMaximumSize(this.orgOptions.getMinimumSize());
         this.orgOptions.setSize(this.orgOptions.getMinimumSize());
         this.orgOptions.setActionCommand("orgOptions");
      }

      return this.orgOptions;
   }

   private JComboBox getCimOptions() {
      if (this.cimOptions == null) {
         this.cimOptions = new JComboBox();
         this.cimOptions.setMinimumSize(new Dimension(this.addresseeOptions.getWidth() - 2 * this.addresseeOptions.getBorder().getBorderInsets(this.addresseeOptions).left, GuiUtil.getCommonItemHeight() + 4));
         this.cimOptions.setPreferredSize(this.cimOptions.getMinimumSize());
         this.cimOptions.setMaximumSize(this.cimOptions.getMinimumSize());
         this.cimOptions.setSize(this.cimOptions.getMinimumSize());
         this.cimOptions.setActionCommand("cimOptions");
         this.cimOptions.setRenderer(new Envelope.CimOptionsListCellRenderer());
      }

      return this.cimOptions;
   }

   private JPanel getButtons() {
      if (this.buttons == null) {
         this.buttons = new JPanel();
         this.buttons.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
         this.buttons.setMinimumSize(new Dimension(630, GuiUtil.getCommonItemHeight() + 8));
         this.buttons.setMaximumSize(new Dimension(630, GuiUtil.getCommonItemHeight() + 8));
         this.buttons.setPreferredSize(new Dimension(630, GuiUtil.getCommonItemHeight() + 8));
         BoxLayout var1 = new BoxLayout(this.buttons, 0);
         this.buttons.setLayout(var1);
         this.buttons.add(Box.createHorizontalGlue());
         this.buttons.add(this.getPrintButton());
         this.buttons.add(Box.createHorizontalStrut(5));
         this.buttons.add(this.getCloseButton());
         this.buttons.add(Box.createHorizontalGlue());
      }

      return this.buttons;
   }

   private JButton getPrintButton() {
      if (this.print == null) {
         this.print = new JButton();
         this.print.setText("Nyomtat");
         this.print.setMinimumSize(new Dimension(GuiUtil.getW(this.print, this.print.getText()), GuiUtil.getCommonItemHeight() + 2));
         this.print.setPreferredSize(this.print.getMinimumSize());
         this.print.setMaximumSize(this.print.getMinimumSize());
         this.print.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               AddressOpt var2 = Envelope.this.getSelectedFelado();
               if (var2 != null && var2.isFeladoPrintable()) {
                  AddressOpt var3 = Envelope.this.getSelectedCimzett();
                  if (var3 != null && var3.isCimzettPrintable()) {
                     EnvelopePrintJob var4 = new EnvelopePrintJob(var2, var3);
                     (new Thread(var4)).start();
                  } else {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "A címzett címe hiányos, a bortíték nem nyomtatható ki!", "Nyomtatási hiba", 0);
                  }
               } else {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "A feladó címe hiányos, a bortíték nem nyomtatható ki!", "Nyomtatási hiba", 0);
               }
            }
         });
      }

      return this.print;
   }

   private JButton getCloseButton() {
      if (this.close == null) {
         this.close = new JButton();
         this.close.setText("Bezárás");
         this.close.setMinimumSize(new Dimension(GuiUtil.getW(this.close, this.close.getText()), GuiUtil.getCommonItemHeight() + 2));
         this.close.setPreferredSize(this.close.getMinimumSize());
         this.close.setMaximumSize(this.close.getMinimumSize());
         this.close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               Envelope.this.dispose();
            }
         });
      }

      return this.close;
   }

   class CimOptionsComparator implements Comparator<AddressOpt> {
      public int compare(AddressOpt var1, AddressOpt var2) {
         return var1.getTitle().length == var2.getTitle().length ? var1.getTitleHint().compareTo(var2.getTitleHint()) : var2.getTitle().length - var1.getTitle().length;
      }
   }

   class CimOptionsListCellRenderer extends DefaultListCellRenderer {
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         Component var6 = super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         ((DefaultListCellRenderer)var6).setText(((AddressOpt)var2).getTitleHint());
         return var6;
      }
   }
}
