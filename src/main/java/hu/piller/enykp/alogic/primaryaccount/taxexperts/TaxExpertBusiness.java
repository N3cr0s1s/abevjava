package hu.piller.enykp.alogic.primaryaccount.taxexperts;

import hu.piller.enykp.alogic.filepanels.tablesorter.TableSorter;
import hu.piller.enykp.alogic.primaryaccount.Tools;
import hu.piller.enykp.alogic.primaryaccount.common.IBusiness;
import hu.piller.enykp.alogic.primaryaccount.common.IRecord;
import hu.piller.enykp.alogic.primaryaccount.common.envelope.EnvelopeBusiness;
import hu.piller.enykp.alogic.primaryaccount.common.envelope.EnvelopePanel;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.EventLog;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.icon.ENYKIconSet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.xml.sax.SAXException;

public class TaxExpertBusiness implements IBusiness {
   private static final int ID_COLUMN_ID = 0;
   private static final int ID_COLUMN_TE_ID = 1;
   private static final int ID_COLUMN_TE_NAME = 2;
   private static final int ID_COLUMN_TE_TESTIMONTAL_ID = 3;
   private static final String FILE_NAME = "pa_taxexperts.xml";
   private TaxExpertPanel taxexpert_panel;
   private TaxExpertRecordFactory record_factory;
   private File primary_account_path;
   private TaxExpertEnvelope envelope;
   private EnvelopePanel envelope_panel;
   private JTable tbl_taxexpert;
   private TableSorter table_sorter;
   private JButton btn_new;
   private JButton btn_modify;
   private JButton btn_del;
   private JButton btn_envelope;
   private JButton btn_return;
   private final Hashtable id_record_map = new Hashtable(128);
   private TaxExpertBusiness.TaxExpertTableModel table_model;
   private final TaxExpertBusiness.StateHandler state_handler = new TaxExpertBusiness.StateHandler();

   public TaxExpertBusiness(TaxExpertPanel var1) {
      try {
         Tools.checkPAFolderContent((File)null, (File)null, "pa_taxexperts.xml", new TaxExpertRecordFactory());
      } catch (SAXException var4) {
         throw new RuntimeException(var4);
      }

      this.taxexpert_panel = var1;

      try {
         this.record_factory = new TaxExpertRecordFactory();
      } catch (SAXException var3) {
         hu.piller.enykp.util.base.Tools.eLog(var3, 0);
      }

      this.primary_account_path = getPrimaryAccountPath();
      this.envelope = new TaxExpertEnvelope();
      this.envelope_panel = new EnvelopePanel();
      this.prepare(this.primary_account_path != null);
   }

   private void prepare(boolean var1) {
      this.tbl_taxexpert = (JTable)this.taxexpert_panel.getLPComponent("table");
      this.table_sorter = new TableSorter();
      this.btn_new = (JButton)this.taxexpert_panel.getLPComponent("new_btn");
      this.btn_modify = (JButton)this.taxexpert_panel.getLPComponent("modify_btn");
      this.btn_del = (JButton)this.taxexpert_panel.getLPComponent("delete_btn");
      this.btn_envelope = (JButton)this.taxexpert_panel.getLPComponent("envelope_btn");
      this.btn_return = (JButton)this.taxexpert_panel.getLPComponent("return_btn");
      if (var1) {
         this.prepareTable();
         this.prepareNew();
         this.prepareModify();
         this.prepareDel();
         this.prepareEnvelope();
         this.prepareReturn();
         this.prepareIcons();
         this.state_handler.setState(this.state_handler.analiseState());
      } else {
         this.prepareReturn();
         this.prepareIcons();
         this.btn_new.setEnabled(false);
         this.btn_modify.setEnabled(false);
         this.btn_del.setEnabled(false);
         this.btn_envelope.setEnabled(false);
         JScrollPane var2 = (JScrollPane)this.taxexpert_panel.getLPComponent("table_scroller");
         JLabel var3 = new JLabel("Hiba miatt nem használható !");
         var3.setFont(var3.getFont().deriveFont(20.0F));
         var3.setHorizontalAlignment(0);
         var2.setViewportView(var3);
      }

   }

   private void prepareIcons() {
      ENYKIconSet var1 = ENYKIconSet.getInstance();
      this.setButtonIcon(this.btn_new, "cmn_add", var1);
      this.setButtonIcon(this.btn_modify, "cmn_edit", var1);
      this.setButtonIcon(this.btn_del, "cmn_del", var1);
      this.setButtonIcon(this.btn_envelope, "cmn_print", var1);
      this.setButtonIcon(this.btn_return, "cmn_back", var1);
   }

   private void setButtonIcon(JButton var1, String var2, ENYKIconSet var3) {
      var1.setIcon(var3.get(var2));
   }

   private void prepareTable() {
      this.tbl_taxexpert.setModel(this.table_model = new TaxExpertBusiness.TaxExpertTableModel());
      this.tbl_taxexpert.getTableHeader().setReorderingAllowed(false);
      this.tbl_taxexpert.setSelectionMode(0);
      TableColumnModel var1 = this.tbl_taxexpert.getColumnModel();
      TableColumn var2 = var1.getColumn(0);
      var2.setMaxWidth(0);
      var2.setMinWidth(0);
      var2.setResizable(false);
      var2 = var1.getColumn(1);
      var2.setMinWidth(70);
      var2 = var1.getColumn(2);
      var2.setMinWidth(70);
      var2 = var1.getColumn(3);
      var2.setMinWidth(70);
      var1 = this.tbl_taxexpert.getTableHeader().getColumnModel();
      var2 = var1.getColumn(0);
      var2.setMaxWidth(0);
      var2.setMinWidth(0);
      var2.setResizable(false);
      this.loadRecords();
      this.table_sorter.attachTable(this.tbl_taxexpert);
      this.table_sorter.setSortEnabled(true);
      this.tbl_taxexpert.addMouseListener(new MouseListener() {
         public void mouseClicked(MouseEvent var1) {
            if (var1.getClickCount() == 2) {
               TaxExpertBusiness.this.modifySelected();
            }

         }

         public void mouseEntered(MouseEvent var1) {
         }

         public void mouseExited(MouseEvent var1) {
         }

         public void mousePressed(MouseEvent var1) {
         }

         public void mouseReleased(MouseEvent var1) {
         }
      });
      this.tbl_taxexpert.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         private int prev_row_idx = -2;

         public void valueChanged(ListSelectionEvent var1) {
            if (TaxExpertBusiness.this.tbl_taxexpert.getRowSelectionAllowed() && !var1.getValueIsAdjusting()) {
               int var2 = TaxExpertBusiness.this.tbl_taxexpert.getSelectedRow();
               if (var2 != this.prev_row_idx) {
                  this.prev_row_idx = var2;
                  if (var2 >= 0) {
                     TaxExpertBusiness.this.state_handler.setState(1);
                  } else {
                     TaxExpertBusiness.this.state_handler.setState(0);
                  }
               }
            }

         }
      });
      this.tbl_taxexpert.getInputMap(1).getParent().remove(KeyStroke.getKeyStroke(113, 0));
      this.tbl_taxexpert.getInputMap(1).getParent().remove(KeyStroke.getKeyStroke(10, 0));
      this.tbl_taxexpert.getInputMap(1).getParent().remove(KeyStroke.getKeyStroke(27, 0));
   }

   private void prepareNew() {
      this.installKeyBindingForButton(this.btn_new, 155);
      this.btn_new.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TaxExpertEditorPanel var2 = (TaxExpertEditorPanel)TaxExpertBusiness.this.taxexpert_panel.getEditorPanel();
            TaxExpertEditorBusiness var3 = var2.getBusiness();
            var3.setRecord(new TaxExpertRecord(TaxExpertBusiness.this.record_factory, TaxExpertBusiness.this.primary_account_path, TaxExpertBusiness.this.envelope), TaxExpertBusiness.this);
            TaxExpertBusiness.this.taxexpert_panel.getNestPanel().setGuestPanel(3, "Új adótanácsadó", var2);
         }
      });
   }

   private void prepareModify() {
      this.installKeyBindingForButton(this.btn_modify, 113);
      this.installKeyBindingForButton(this.btn_modify, 10);
      this.btn_modify.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TaxExpertBusiness.this.modifySelected();
         }
      });
   }

   private void modifySelected() {
      IRecord var1 = this.getRecord();
      if (var1 != null) {
         TaxExpertEditorPanel var2 = (TaxExpertEditorPanel)this.taxexpert_panel.getEditorPanel();
         TaxExpertEditorBusiness var3 = var2.getBusiness();
         var3.setRecord(var1, this);
         this.taxexpert_panel.getNestPanel().setGuestPanel(3, "Adótanácsadó szerkesztése", var2);
      }

   }

   private void prepareDel() {
      this.installKeyBindingForButton(this.btn_del, 127);
      this.btn_del.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            IRecord var2 = TaxExpertBusiness.this.getRecord();
            if (var2 != null) {
               String var3 = var2.getName();
               var3 = var3 == null ? "" : var3;
               if (0 == JOptionPane.showConfirmDialog(SwingUtilities.getRoot(TaxExpertBusiness.this.taxexpert_panel), "Törli '" + var3 + "' adótanácsadót a törzsadatok közül ?", "Adótanácsadó törlése törzsadatokból", 0)) {
                  int var4 = TaxExpertBusiness.this.tbl_taxexpert.getSelectedRow();
                  var2.delete();
                  TaxExpertBusiness.this.refreshView();
                  TaxExpertBusiness.this.tbl_taxexpert.revalidate();
                  TaxExpertBusiness.this.tbl_taxexpert.repaint();
                  var4 = var4 > 0 ? var4 - 1 : 0;
                  if (TaxExpertBusiness.this.tbl_taxexpert.getRowCount() > var4) {
                     TaxExpertBusiness.this.tbl_taxexpert.getSelectionModel().setSelectionInterval(var4, var4);
                  }

                  TaxExpertBusiness.this.firePrimaryAccountChanged();
               }
            }

         }
      });
   }

   private void prepareEnvelope() {
      this.installKeyBindingForButton(this.btn_envelope, 123);
      this.btn_envelope.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            IRecord var2 = TaxExpertBusiness.this.getRecord();
            if (var2 != null) {
               EnvelopeBusiness var3 = TaxExpertBusiness.this.envelope_panel.getBusiness();
               var3.setRecord(var2, TaxExpertBusiness.this);
               TaxExpertBusiness.this.taxexpert_panel.getNestPanel().setGuestPanel(3, "Boríték nyomtatás (Adótanácsadó)", TaxExpertBusiness.this.envelope_panel);
            }

         }
      });
   }

   private void prepareReturn() {
      this.btn_return.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TaxExpertBusiness.this.taxexpert_panel.getNestPanel().back();
         }
      });
   }

   private void installKeyBindingForButton(final JButton var1, int var2) {
      String var3 = KeyStroke.getKeyStroke(var2, 0).toString() + "Pressed";
      TaxExpertPanel var4 = this.taxexpert_panel;
      var4.getInputMap(2).put(KeyStroke.getKeyStroke(var2, 0), var3);
      var4.getActionMap().put(var3, new AbstractAction() {
         public void actionPerformed(ActionEvent var1x) {
            if (var1.isVisible() && var1.isEnabled()) {
               var1.doClick();
            }

         }
      });
   }

   private void loadRecords() {
      try {
         Vector var1 = this.record_factory.loadRecords(this.primary_account_path, this.envelope);
         this.createIdRecordMap(var1);
         this.showRecords(var1);
      } catch (Exception var2) {
         hu.piller.enykp.util.base.Tools.eLog(var2, 0);
      }

   }

   private void createIdRecordMap(Vector var1) {
      if (var1 != null) {
         int var3 = 0;

         for(int var4 = var1.size(); var3 < var4; ++var3) {
            Object var2 = var1.get(var3);
            if (var2 instanceof IRecord) {
               this.mapRecord((IRecord)var2);
            }
         }
      }

   }

   public void mapRecord(IRecord var1) {
      Object var2 = var1.getData().get("id");
      this.id_record_map.put(var2 == null ? "" : var2.toString(), var1);
   }

   public void refreshView() {
      int var1 = this.tbl_taxexpert.getSelectedRow();
      this.showRecords(this.record_factory.getRecords());
      if (this.tbl_taxexpert.getRowCount() > var1) {
         this.tbl_taxexpert.getSelectionModel().setSelectionInterval(var1, var1);
      }

   }

   private void showRecords(Vector var1) {
      this.table_model.getDataVector().clear();
      this.table_sorter.clearOriModel();
      int var4 = 0;

      for(int var5 = var1.size(); var4 < var5; ++var4) {
         Object var3 = var1.get(var4);
         if (var3 instanceof IRecord) {
            Hashtable var2 = ((IRecord)var3).getData();
            this.table_model.addRow(new Object[]{this.getString(var2.get("id")), this.getString(var2.get("te_id")), this.getString(var2.get("te_name")), this.getString(var2.get("te_testimontial_id"))});
         }
      }

   }

   private String getString(Object var1) {
      return var1 == null ? "" : var1.toString();
   }

   private IRecord getRecord() {
      int var1 = this.tbl_taxexpert.getSelectedRow();
      if (var1 >= 0 && var1 < this.table_model.getRowCount()) {
         Object var2 = this.table_model.getValueAt(var1, 0);
         if (var2 != null) {
            var2 = this.id_record_map.get(var2);
            if (var2 instanceof IRecord) {
               return (IRecord)var2;
            }
         }
      }

      return null;
   }

   public Object searchTaxExpert(Object var1) {
      IRecord var2 = null;
      Enumeration var3 = this.id_record_map.elements();
      if (var1 instanceof Hashtable) {
         Hashtable var6 = (Hashtable)var1;

         while(var3.hasMoreElements()) {
            int var5 = 0;
            IRecord var4 = (IRecord)var3.nextElement();
            Object var7;
            if ((var7 = var6.get("name")) != null) {
               var5 = var7.toString().equalsIgnoreCase(var4.getName()) ? 1 : -1;
            }

            if (var5 == 1) {
               var2 = var4;
               break;
            }
         }
      }

      return var2;
   }

   public void reload() {
      this.id_record_map.clear();
      this.loadRecords();
   }

   public static File getPrimaryAccountPath() {
      Object var0 = getProperty("prop.usr.primaryaccounts");
      if (var0 instanceof String) {
         try {
            File var1 = new File((String)var0);
            return new File(var1, "pa_taxexperts.xml");
         } catch (Exception var2) {
            writeLog(var2);
         }
      }

      ErrorList.getInstance().writeError("TE", "Hibás törzsadat hely !", (Exception)null, (Object)null);
      return null;
   }

   private static Object getProperty(Object var0) {
      IPropertyList var1 = PropertyList.getInstance();
      return var1 != null ? var1.get(var0) : null;
   }

   public void restorePanel() {
      this.taxexpert_panel.getNestPanel().restorePanel(3);
   }

   public void firePrimaryAccountChanged() {
      this.taxexpert_panel.getNestPanel().firePrimaryAccountChanged();
   }

   private static void writeLog(Object var0) {
      try {
         EventLog.getInstance().writeLog("Törzsadat.Adótanácsadók: " + var0);
      } catch (IOException var2) {
         hu.piller.enykp.util.base.Tools.eLog(var2, 0);
      }

   }

   private class StateHandler {
      public static final int STATE_ROW_UNSELECTED = 0;
      public static final int STATE_ROW_SELECTED = 1;
      private int prev_state;

      private StateHandler() {
         this.prev_state = -1;
      }

      public void setState(int var1) {
         if (this.prev_state != var1) {
            switch(var1) {
            case 0:
               this.prev_state = 0;
               TaxExpertBusiness.this.btn_new.setEnabled(true);
               TaxExpertBusiness.this.btn_modify.setEnabled(false);
               TaxExpertBusiness.this.btn_del.setEnabled(false);
               TaxExpertBusiness.this.btn_envelope.setEnabled(false);
               break;
            case 1:
               this.prev_state = 1;
               TaxExpertBusiness.this.btn_new.setEnabled(true);
               TaxExpertBusiness.this.btn_modify.setEnabled(true);
               TaxExpertBusiness.this.btn_del.setEnabled(true);
               TaxExpertBusiness.this.btn_envelope.setEnabled(false);
            }

         }
      }

      public int analiseState() {
         return TaxExpertBusiness.this.tbl_taxexpert.getSelectedRow() >= 0 ? 1 : 0;
      }

      // $FF: synthetic method
      StateHandler(Object var2) {
         this();
      }
   }

   private class TaxExpertTableModel extends DefaultTableModel {
      public boolean isCellEditable(int var1, int var2) {
         return false;
      }

      TaxExpertTableModel() {
         this.addColumn("Azonosító");
         this.addColumn("Adószakértő azonosító");
         this.addColumn("Adószakértő neve");
         this.addColumn("Adószakértő bizonyítvány száma");
      }
   }
}
