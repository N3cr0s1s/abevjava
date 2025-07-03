package hu.piller.enykp.alogic.primaryaccount.common;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ListPanel extends JPanel {
   public static final String COMPONENT_TABLE = "table";
   public static final String COMPONENT_NEW_BTN = "new_btn";
   public static final String COMPONENT_MODIFY_BTN = "modify_btn";
   public static final String COMPONENT_DELETE_BTN = "delete_btn";
   public static final String COMPONENT_ENVELOPE_BTN = "envelope_btn";
   public static final String COMPONENT_RETURN_BTN = "return_btn";
   public static final String COMPONENT_TABLE_SCROLLER = "table_scroller";
   private static final long serialVersionUID = 1L;
   private JScrollPane scp_items = null;
   private JTable tbl_items = null;
   private JPanel buttons_panel = null;
   private JButton btn_new = null;
   private JButton btn_modify = null;
   private JButton btn_delete = null;
   private JButton btn_envelope = null;
   private JButton btn_return = null;
   private Component cmp_envelope_separator = null;

   public ListPanel() {
      this.initialize();
   }

   private void initialize() {
      this.setLayout(new BoxLayout(this, 1));
      this.setSize(new Dimension(450, 282));
      this.add(this.getScp_companies(), (Object)null);
      this.add(Box.createRigidArea(new Dimension(0, 5)));
      this.add(this.getButtons_panel(), (Object)null);
   }

   private JScrollPane getScp_companies() {
      if (this.scp_items == null) {
         this.scp_items = new JScrollPane();
         this.scp_items.setViewportView(this.getTbl_companies());
         this.scp_items.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
         this.scp_items.setHorizontalScrollBarPolicy(30);
         this.scp_items.setVerticalScrollBarPolicy(20);
      }

      return this.scp_items;
   }

   private JTable getTbl_companies() {
      if (this.tbl_items == null) {
         this.tbl_items = new JTable();
         this.tbl_items.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
      }

      return this.tbl_items;
   }

   private JPanel getButtons_panel() {
      if (this.buttons_panel == null) {
         this.buttons_panel = new JPanel();
         this.buttons_panel.setLayout(new BoxLayout(this.getButtons_panel(), 0));
         this.buttons_panel.add(this.getBtn_new(), (Object)null);
         this.buttons_panel.add(this.getBtn_modify(), (Object)null);
         this.buttons_panel.add(this.getBtn_delete(), (Object)null);
         this.buttons_panel.add(this.getCmp_envelope_separator(), (Object)null);
         this.buttons_panel.add(this.getBtn_envelope(), (Object)null);
         this.buttons_panel.add(Box.createRigidArea(new Dimension(5, 0)), (Object)null);
         this.buttons_panel.add(Box.createGlue(), (Object)null);
         this.buttons_panel.add(this.getBtn_return(), (Object)null);
      }

      return this.buttons_panel;
   }

   private JButton getBtn_new() {
      if (this.btn_new == null) {
         this.btn_new = new JButton();
         this.btn_new.setText("Új");
      }

      return this.btn_new;
   }

   private JButton getBtn_modify() {
      if (this.btn_modify == null) {
         this.btn_modify = new JButton();
         this.btn_modify.setText("Módosítás");
      }

      return this.btn_modify;
   }

   private JButton getBtn_delete() {
      if (this.btn_delete == null) {
         this.btn_delete = new JButton();
         this.btn_delete.setText("Törlés");
      }

      return this.btn_delete;
   }

   private Component getCmp_envelope_separator() {
      if (this.cmp_envelope_separator == null) {
         this.cmp_envelope_separator = Box.createRigidArea(new Dimension(5, 0));
      }

      return this.cmp_envelope_separator;
   }

   private JButton getBtn_envelope() {
      if (this.btn_envelope == null) {
         this.btn_envelope = new JButton();
         this.btn_envelope.setText("Boríték");
      }

      return this.btn_envelope;
   }

   private JButton getBtn_return() {
      if (this.btn_return == null) {
         this.btn_return = new JButton();
         this.btn_return.setText("Vége");
      }

      return this.btn_return;
   }

   public void setVisibleEnvelopeButton(boolean var1) {
      this.cmp_envelope_separator.setVisible(var1);
      this.btn_envelope.setVisible(var1);
   }

   public JComponent getLPComponent(String var1) {
      if ("table".equalsIgnoreCase(var1)) {
         return this.tbl_items;
      } else if ("new_btn".equalsIgnoreCase(var1)) {
         return this.btn_new;
      } else if ("modify_btn".equalsIgnoreCase(var1)) {
         return this.btn_modify;
      } else if ("delete_btn".equalsIgnoreCase(var1)) {
         return this.btn_delete;
      } else if ("envelope_btn".equalsIgnoreCase(var1)) {
         return this.btn_envelope;
      } else if ("return_btn".equalsIgnoreCase(var1)) {
         return this.btn_return;
      } else {
         return "table_scroller".equalsIgnoreCase(var1) ? this.scp_items : null;
      }
   }
}
