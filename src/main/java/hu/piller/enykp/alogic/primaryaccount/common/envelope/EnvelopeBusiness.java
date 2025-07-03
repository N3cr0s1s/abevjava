package hu.piller.enykp.alogic.primaryaccount.common.envelope;

import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.primaryaccount.PAInfo;
import hu.piller.enykp.alogic.primaryaccount.common.DefaultEnvelope;
import hu.piller.enykp.alogic.primaryaccount.common.IBusiness;
import hu.piller.enykp.alogic.primaryaccount.common.IRecord;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.util.base.EventLog;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.icon.ENYKIconSet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import org.xml.sax.SAXException;

public class EnvelopeBusiness implements IEventListener {
   private ActionListener address_action_lisener;
   private ActionListener organization_action_lisener;
   private static boolean is_started;
   private EnvelopePanel envelope_panel;
   protected IBusiness parent_business;
   protected IRecord record;
   private JButton btn_ok;
   protected JButton btn_cancel;
   private JButton btn_stable;
   private JButton btn_mail;
   private JButton btn_find_receiver;
   private JTextField txt_s_name;
   private JTextField txt_s_settlement;
   private JTextField txt_s_street;
   private JTextField txt_s_zip;
   private JTextField txt_r_name;
   private JTextField txt_r_settlement;
   private JTextField txt_r_zip;
   private JTextField txt_r_pf;
   private JCheckBox chk_take_old;
   private JComboBox cbo_address;
   private JComboBox cbo_organizations;
   private JLabel lbl_s_title;
   private JLabel lbl_r_title;
   private JLabel lbl_r_zip;
   private JLabel lbl_r_pf;
   private AddressRecordFactory address_factory;

   public EnvelopeBusiness(EnvelopePanel var1) {
      this.envelope_panel = var1;

      try {
         this.address_factory = new AddressRecordFactory();
      } catch (SAXException var3) {
         writeLog("Hiba történt az APEH címjegyzék betöltő felkészítésekor! (" + var3 + ")");
      }

      this.prepare();
   }

   private void prepare() {
      this.btn_ok = (JButton)this.envelope_panel.getEPComponent("ok_button");
      this.btn_cancel = (JButton)this.envelope_panel.getEPComponent("cancel_button");
      this.btn_stable = (JButton)this.envelope_panel.getEPComponent("stable_button");
      this.btn_mail = (JButton)this.envelope_panel.getEPComponent("mail_button");
      this.btn_find_receiver = (JButton)this.envelope_panel.getEPComponent("find_receiver_button");
      this.txt_s_name = (JTextField)this.envelope_panel.getEPComponent("s_name");
      this.txt_s_settlement = (JTextField)this.envelope_panel.getEPComponent("s_settlement");
      this.txt_s_street = (JTextField)this.envelope_panel.getEPComponent("s_street");
      this.txt_s_zip = (JTextField)this.envelope_panel.getEPComponent("s_zip");
      this.txt_r_name = (JTextField)this.envelope_panel.getEPComponent("r_name");
      this.txt_r_settlement = (JTextField)this.envelope_panel.getEPComponent("r_settlement");
      this.txt_r_zip = (JTextField)this.envelope_panel.getEPComponent("r_street");
      this.txt_r_pf = (JTextField)this.envelope_panel.getEPComponent("r_zip");
      this.chk_take_old = (JCheckBox)this.envelope_panel.getEPComponent("take_old_chk");
      this.cbo_address = (JComboBox)this.envelope_panel.getEPComponent("addr_cbo");
      this.cbo_organizations = (JComboBox)this.envelope_panel.getEPComponent("org_cbo");
      this.lbl_s_title = (JLabel)this.envelope_panel.getEPComponent("s_title_le");
      this.lbl_r_title = (JLabel)this.envelope_panel.getEPComponent("r_title_l");
      this.lbl_r_zip = (JLabel)this.envelope_panel.getEPComponent("r_street_l");
      this.lbl_r_pf = (JLabel)this.envelope_panel.getEPComponent("r_zip_l");
      this.prepareLabels();
      this.prepareOk();
      this.prepareCancel();
      this.prepareStable();
      this.prepareMail();
      this.prepareFindReceiver();
      this.prepareOrganizations((Object)null);
      this.prepareTakeOld();
      this.prepareIcons();
      if (is_started) {
         this.setOrgCbo(false);
      } else {
         is_started = true;
      }

   }

   private void prepareIcons() {
      ENYKIconSet var1 = ENYKIconSet.getInstance();
      this.setButtonIcon(this.btn_ok, "anyk_ellenorzes", var1);
      this.setButtonIcon(this.btn_cancel, "anyk_megse", var1);
   }

   private void setButtonIcon(JButton var1, String var2, ENYKIconSet var3) {
      var1.setIcon(var3.get(var2));
   }

   private void prepareLabels() {
      this.lbl_s_title.setText("Feladó");
      this.lbl_r_title.setText("Címzett");
      this.lbl_r_zip.setText("Irányítószám");
      this.lbl_r_pf.setText("Postafiók");
   }

   private void prepareOk() {
      this.btn_ok.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (EnvelopeBusiness.this.record != null) {
               DefaultEnvelope var2 = EnvelopeBusiness.this.record.getEnvelope();
               var2.put("f_feladó", EnvelopeBusiness.this.txt_s_name.getText());
               var2.put("f_város", EnvelopeBusiness.this.txt_s_settlement.getText());
               var2.put("f_utca", EnvelopeBusiness.this.txt_s_street.getText());
               var2.put("f_irsz", EnvelopeBusiness.this.txt_s_zip.getText());
               var2.put("c_címzett", EnvelopeBusiness.this.txt_r_name.getText());
               var2.put("c_város", EnvelopeBusiness.this.txt_r_settlement.getText());
               var2.put("c_irsz", EnvelopeBusiness.this.txt_r_zip.getText());
               var2.put("c_pf", EnvelopeBusiness.this.txt_r_pf.getText());
               EnvelopeBusiness.this.record.printEnvelope(EnvelopeBusiness.this.envelope_panel);
            }

            EnvelopeBusiness.this.parent_business.restorePanel();
         }
      });
   }

   protected void prepareCancel() {
      this.installKeyBinding(this.btn_cancel, 27);
      this.btn_cancel.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            EnvelopeBusiness.this.parent_business.restorePanel();
         }
      });
   }

   private void prepareStable() {
      this.installKeyBinding(this.btn_stable, 114);
      this.btn_stable.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            EnvelopeBusiness.this.showStableAddress();
         }
      });
   }

   protected void showStableAddress() {
      if (this.record != null) {
         Hashtable var1 = this.record.getEnvelope().getEnvelopeData(this.record, 0);
         if (var1.get("f_irsz") == null || "".equals(var1.get("f_irsz"))) {
            var1 = this.record.getEnvelope().getEnvelopeData(this.record, 1);
         }

         this.txt_s_name.setText(this.getString(var1.get("f_feladó")));
         this.txt_s_settlement.setText(this.getString(var1.get("f_város")));
         this.txt_s_street.setText(this.getString(var1.get("f_utca")));
         this.txt_s_zip.setText(this.getString(var1.get("f_irsz")));
         IRecord var2 = this.findReceiver();
         var1 = this.record.getEnvelope().getEnvelopeData(this.record, 0);
         this.txt_s_name.setText(this.getString(var1.get("f_feladó")));
         this.txt_s_settlement.setText(this.getString(var1.get("f_város")));
         this.txt_s_street.setText(this.getString(var1.get("f_utca")));
         this.txt_s_zip.setText(this.getString(var1.get("f_irsz")));
         if (var2 != null && ((AddressRecord)var2).getApehAddress() != null) {
            this.cbo_address.setSelectedItem(((AddressRecord)var2).getApehAddress().get(0));
         }

         if (var2 == null) {
            this.cbo_address.setEnabled(false);
         } else {
            this.cbo_address.setEnabled(true);
         }
      }

   }

   private void prepareMail() {
      this.installKeyBinding(this.btn_mail, 115);
      this.btn_mail.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (EnvelopeBusiness.this.record != null) {
               Hashtable var2 = EnvelopeBusiness.this.record.getEnvelope().getEnvelopeData(EnvelopeBusiness.this.record, 1);
               EnvelopeBusiness.this.txt_s_name.setText(EnvelopeBusiness.this.getString(var2.get("f_feladó")));
               EnvelopeBusiness.this.txt_s_settlement.setText(EnvelopeBusiness.this.getString(var2.get("f_város")));
               EnvelopeBusiness.this.txt_s_street.setText(EnvelopeBusiness.this.getString(var2.get("f_utca")));
               EnvelopeBusiness.this.txt_s_zip.setText(EnvelopeBusiness.this.getString(var2.get("f_irsz")));
               AddressRecord var3 = (AddressRecord)EnvelopeBusiness.this.findReceiver(EnvelopeBusiness.this.txt_s_zip.getText().trim());
               if (var3 != null && var3.getApehAddress() != null) {
                  EnvelopeBusiness.this.cbo_address.setSelectedItem(var3.getApehAddress().get(0));
               }

               if (var3 == null) {
                  EnvelopeBusiness.this.cbo_address.setEnabled(false);
               } else {
                  EnvelopeBusiness.this.cbo_address.setEnabled(true);
               }
            }

         }
      });
   }

   private void prepareFindReceiver() {
      this.installKeyBinding(this.btn_find_receiver, 116);
      this.btn_find_receiver.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            IRecord var2 = EnvelopeBusiness.this.findReceiver();
            if (var2 == null) {
               GuiUtil.showMessageDialog(SwingUtilities.getRoot(EnvelopeBusiness.this.envelope_panel), "Nem talált címzettet !", "Címzett keresés", 2);
            }

            EnvelopeBusiness.this.cbo_address.setSelectedItem(var2);
         }
      });
   }

   private void prepareOrganizations(Object var1) {
      Object[] var2 = (Object[])((Object[])OrgInfo.getInstance().getOrgNames());
      if (var2 != null && var2.length == 4) {
         if (this.organization_action_lisener != null) {
            this.cbo_organizations.removeActionListener(this.organization_action_lisener);
         }

         int var3 = -1;
         Vector var4 = (Vector)var2[0];
         Vector var5 = (Vector)var2[2];
         this.cbo_organizations.removeAllItems();
         int var6 = 0;

         for(int var7 = var4.size(); var6 < var7; ++var6) {
            this.cbo_organizations.addItem(new EnvelopeBusiness.OrganizationItem(var4.get(var6), var5.get(var6)));
            if (var1 != null && var3 == -1 && var1.equals(var4.get(var6))) {
               var3 = var6;
            }
         }

         if (this.organization_action_lisener == null) {
            this.organization_action_lisener = new ActionListener() {
               public void actionPerformed(ActionEvent var1) {
                  Object var2 = EnvelopeBusiness.this.cbo_organizations.getSelectedItem();
                  if (var2 instanceof EnvelopeBusiness.OrganizationItem) {
                     EnvelopeBusiness.this.txt_r_name.setText("");
                     EnvelopeBusiness.this.txt_r_settlement.setText("");
                     EnvelopeBusiness.this.txt_r_pf.setText("");
                     EnvelopeBusiness.this.txt_r_zip.setText("");
                     EnvelopeBusiness.this.prepareAddress(((EnvelopeBusiness.OrganizationItem)var2).id);
                     EnvelopeBusiness.this.showStableAddress();
                  }

               }
            };
         }

         this.cbo_organizations.addActionListener(this.organization_action_lisener);
         if (var4.size() > 0) {
            this.cbo_organizations.setSelectedIndex(var3 == -1 ? 0 : var3);
         }
      }

   }

   private void prepareAddress(Object var1) {
      if (this.address_action_lisener != null) {
         this.cbo_address.removeActionListener(this.address_action_lisener);
      }

      byte[] var2 = (byte[])((byte[])OrgInfo.getInstance().getOrgAddress(var1));
      if (var2 != null) {
         ByteArrayInputStream var4 = new ByteArrayInputStream(var2);

         try {
            JComboBox var5 = this.cbo_address;
            Vector var3 = this.address_factory.loadRecords(var4, new APEHEnvelope());
            var5.removeAllItems();

            for(int var6 = 0; var6 < var3.size(); ++var6) {
               for(int var7 = 0; var7 < ((AddressRecord)var3.get(var6)).getApehAddress().size(); ++var7) {
                  var5.addItem(((AddressRecord)var3.get(var6)).getApehAddress().get(var7));
               }
            }
         } catch (Exception var8) {
            var8.printStackTrace();
         }

         this.installKeyBinding(this.cbo_address, 117);
         if (this.address_action_lisener == null) {
            this.address_action_lisener = new ActionListener() {
               public void actionPerformed(ActionEvent var1) {
                  Object var2 = EnvelopeBusiness.this.cbo_address.getSelectedItem();
                  if (var2 instanceof AddressRecord.ApehAddress || var2 == null) {
                     EnvelopeBusiness.this.showReceiver((AddressRecord.ApehAddress)var2);
                  }

               }
            };
         }

         this.cbo_address.addActionListener(this.address_action_lisener);
      }

   }

   private void prepareTakeOld() {
      this.chk_take_old.setVisible(false);
   }

   protected IRecord findReceiver() {
      String var1 = this.txt_s_zip.getText().trim();
      IRecord var2 = this.findReceiver(var1);
      if (var2 == null) {
         IRecord var3 = this.searchSender();
         if (var3 != null) {
            this.record.getData().putAll(var3.getData());
            var1 = var1.trim().length() == 0 ? (String)var3.getData().get("s_zip") : var1;
            var2 = this.findReceiver(var1);
         }
      }

      return var2;
   }

   private IRecord findReceiver(String var1) {
      Vector var2 = this.address_factory.getRecords();
      if (this.isReceiverSelectionByTaxnumberAllowed()) {
         Object var4 = this.record.getData().get("tax_number");
         if (var4 != null && !"".equals((String)var4) && var4.toString().length() > 2) {
            try {
               String var5 = var4.toString();
               int var6 = Integer.parseInt(var5.substring(var5.length() - 2));
               var6 = var6 > 2 && var6 < 20 ? var6 + 20 : var6;
               String var7 = String.valueOf(var6);
               DefaultComboBoxModel var8 = (DefaultComboBoxModel)this.cbo_address.getModel();
               int var11 = 0;

               for(int var12 = var8.getSize(); var11 < var12; ++var11) {
                  AddressRecord.ApehAddress var9 = (AddressRecord.ApehAddress)var8.getElementAt(var11);
                  Object var10 = var9.getShire();
                  if (var10 instanceof Hashtable) {
                     var10 = ((Hashtable)var10).get("code");
                     if (var10 != null && var7.equalsIgnoreCase(var10.toString())) {
                        return var9.getAddressRecord();
                     }
                  }
               }
            } catch (Exception var14) {
               var14.printStackTrace();
            }
         }
      }

      if (var1 != null) {
         var1 = var1.trim().toLowerCase();
         if (var1.length() > 0) {
            int var15 = 0;

            for(int var16 = var2.size(); var15 < var16; ++var15) {
               Object var3 = var2.get(var15);
               if (var3 instanceof AddressRecord) {
                  AddressRecord var18 = (AddressRecord)var3;
                  Vector var19 = var18.getZips();
                  int var20 = 0;

                  for(int var21 = var19.size(); var20 < var21; ++var20) {
                     Hashtable var17 = (Hashtable)var19.get(var20);
                     if (var17 != null) {
                        String var22 = this.getString(var17.get("zip_from")).trim().toLowerCase();
                        String var23 = this.getString(var17.get("zip_to")).trim().toLowerCase();
                        if (var22.compareTo(var23) > 0) {
                           String var13 = var22;
                           var22 = var23;
                           var23 = var13;
                        }

                        if (var1.length() >= var22.length() && var1.length() <= var23.length() && var1.compareTo(var22) >= 0 && var1.compareTo(var23) <= 0) {
                           return var18;
                        }
                     }
                  }
               }
            }
         }
      }

      return null;
   }

   private boolean isReceiverSelectionByTaxnumberAllowed() {
      boolean var1 = Calculator.getInstance().getBookModel() != null && "APEH".equals(((Hashtable)Calculator.getInstance().getBookModel().get_templateheaddata().get("docinfo")).get("org"));
      boolean var2 = ((EnvelopeBusiness.OrganizationItem)this.cbo_organizations.getSelectedItem()).id.equals("APEH");
      return var1 || var2;
   }

   private IRecord searchSender() {
      String var1 = (String)this.record.getData().get("tax_number");
      String var2 = (String)this.record.getData().get("tax_id");
      Hashtable var4 = new Hashtable(2);
      var1 = var1 == null ? "" : var1.trim();
      var2 = var2 == null ? "" : var2.trim();
      byte var3;
      if (var1.length() > 0 && var2.length() > 0) {
         var3 = 2;
         var4.put("tax_number", var1);
         var4.put("tax_id", var2);
      } else if (var1.length() > 0) {
         var3 = 3;
         var4.put("tax_number", var1);
      } else {
         var3 = 6;
         var4.put("tax_id", var2);
      }

      return (IRecord)PAInfo.getInstance().search_primary_account(new Integer(var3), var4);
   }

   private void showReceiver(AddressRecord.ApehAddress var1) {
      if (var1 != null) {
         this.txt_r_name.setText(var1.getTitle());
         this.txt_r_settlement.setText(var1.getSettlement());
         this.txt_r_pf.setText(var1.getPoBox());
         this.txt_r_zip.setText(var1.getZip());
      } else if (!this.chk_take_old.isSelected()) {
         this.txt_r_name.setText("");
         this.txt_r_settlement.setText("");
         this.txt_r_pf.setText("");
         this.txt_r_zip.setText("");
      }

   }

   private void installKeyBinding(JComponent var1, int var2) {
      String var3 = KeyStroke.getKeyStroke(var2, 0).toString() + "Pressed";
      EnvelopePanel var4 = this.envelope_panel;
      if (var1 instanceof JButton) {
         final JButton var5 = (JButton)var1;
         var4.getInputMap(2).put(KeyStroke.getKeyStroke(var2, 0), var3);
         var4.getActionMap().put(var3, new AbstractAction() {
            public void actionPerformed(ActionEvent var1) {
               if (var5.isVisible() && var5.isEnabled()) {
                  var5.doClick();
               }

            }
         });
      } else if (var1 instanceof JComboBox) {
         final JComboBox var7 = (JComboBox)var1;
         var4.getInputMap(2).put(KeyStroke.getKeyStroke(var2, 0), var3);
         var4.getActionMap().put(var3, new AbstractAction() {
            public void actionPerformed(ActionEvent var1) {
               if (var7.isVisible() && var7.isEnabled() && !var7.isPopupVisible()) {
                  var7.grabFocus();
               }

               var7.showPopup();
            }
         });
         byte var6 = 27;
         var3 = KeyStroke.getKeyStroke(var6, 0).toString() + "Pressed";
         var7.getInputMap(2).put(KeyStroke.getKeyStroke(var6, 0), var3);
         var7.getActionMap().put(var3, new AbstractAction() {
            public void actionPerformed(ActionEvent var1) {
               if (var7.isVisible() && var7.isEnabled() && var7.isPopupVisible()) {
                  var7.hidePopup();
               }

            }
         });
      }

   }

   public void setRecord(IRecord var1, IBusiness var2) {
      this.record = var1;
      this.parent_business = var2;
      this.showStableAddress();
   }

   private String getString(Object var1) {
      return var1 == null ? "" : var1.toString();
   }

   private static void writeLog(Object var0) {
      try {
         EventLog.getInstance().writeLog("Törzsadat.Címjegyzék: " + var0);
      } catch (IOException var2) {
         Tools.eLog(var2, 0);
      }

   }

   public void setOrgCbo(boolean var1) {
      try {
         Object var2;
         if (!var1) {
            BookModel var3 = Calculator.getInstance().getBookModel();
            if (var3 != null) {
               Hashtable var4 = var3.get_templateheaddata();
               var4 = (Hashtable)var4.get("docinfo");
               var2 = var4.get("org");
            } else {
               var2 = null;
            }
         } else {
            var2 = null;
         }

         if (var2 == null) {
            this.cbo_organizations.setEnabled(true);
         } else {
            this.cbo_organizations.setEnabled(false);
            this.prepareOrganizations(var2);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public Object eventFired(Event var1) {
      Object var2 = var1.getUserData();
      if (!(var2 instanceof Hashtable) && var2 instanceof String) {
         String var3 = (String)var2;
         if (var3.equalsIgnoreCase("afternew")) {
            this.setOrgCbo(false);
         } else if (var3.equalsIgnoreCase("afteropen")) {
            this.setOrgCbo(false);
         } else if (var3.equalsIgnoreCase("afterclose")) {
            this.setOrgCbo(true);
         }
      }

      return null;
   }

   private static class OrganizationItem {
      Object id;
      Object text;

      OrganizationItem(Object var1, Object var2) {
         this.id = var1;
         this.text = var2;
      }

      public String toString() {
         return this.text == null ? "???" : this.text.toString();
      }
   }
}
