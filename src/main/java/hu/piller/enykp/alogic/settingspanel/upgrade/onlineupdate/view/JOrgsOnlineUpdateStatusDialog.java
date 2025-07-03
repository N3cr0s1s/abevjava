package hu.piller.enykp.alogic.settingspanel.upgrade.onlineupdate.view;

import hu.piller.enykp.alogic.masterdata.sync.ui.entityfilter.SizeableCBRenderer;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.settingspanel.upgrade.onlineupdate.dao.OrgsOnlineUpdateStatusSettingsStoreDAO;
import hu.piller.enykp.alogic.settingspanel.upgrade.onlineupdate.model.OrgsOnlineUpdateStatus;
import hu.piller.enykp.gui.GuiUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class JOrgsOnlineUpdateStatusDialog extends JDialog implements ActionListener {
   private JTable orgsOnlineUpdateStatusTable;

   public JOrgsOnlineUpdateStatusDialog(JFrame var1) {
      super(var1);
      this.init();
   }

   public void actionPerformed(ActionEvent var1) {
      if ("save".equals(var1.getActionCommand())) {
         OrgsOnlineUpdateStatusTableModel var2 = (OrgsOnlineUpdateStatusTableModel)this.orgsOnlineUpdateStatusTable.getModel();
         OrgsOnlineUpdateStatusSettingsStoreDAO.save(var2.buildBusinessModelFromTableModel());
         GuiUtil.showMessageDialog(this, "Beállítások elmentve!", "Tájékoztatás", 1);
         this.dispose();
      }

   }

   protected void init() {
      String[] var1 = OrgInfo.getInstance().getOrgIdsOfOrgsWithOnlineUpdate();
      OrgsOnlineUpdateStatus var2 = OrgsOnlineUpdateStatusSettingsStoreDAO.load();
      var2.merge(var1);
      OrgsOnlineUpdateStatusTableModel var3 = new OrgsOnlineUpdateStatusTableModel(var2);
      this.orgsOnlineUpdateStatusTable = new JTable(var3);
      if (GuiUtil.modGui()) {
         this.orgsOnlineUpdateStatusTable.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
      }

      GuiUtil.setTableColWidth(this.orgsOnlineUpdateStatusTable);
      if (this.orgsOnlineUpdateStatusTable.getColumnCount() > 1) {
         this.orgsOnlineUpdateStatusTable.getColumnModel().getColumn(1).setCellEditor(new SizeableCBRenderer());
         this.orgsOnlineUpdateStatusTable.getColumnModel().getColumn(1).setCellRenderer(new SizeableCBRenderer());
      }

      this.setTitle("Várakozás tiltása/engedélyezése");
      this.setName("JOrgsOnlineUpdateStatusDialog");
      this.setResizable(true);
      this.setModal(true);
      this.getContentPane().setLayout(new BorderLayout());
      this.setLocationRelativeTo(this.getOwner());
      Point var4 = this.getLocation();
      this.setLocation(var4.x - 180, var4.y - 100);
      JPanel var5 = new JPanel();
      var5.setLayout(new BorderLayout());
      var5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Beállítások"));
      JScrollPane var6 = new JScrollPane(this.orgsOnlineUpdateStatusTable, 20, 31);
      var6.setPreferredSize(new Dimension(320, 140));
      var5.add(var6, "Center");
      JButton var7 = new JButton();
      var7.setActionCommand("save");
      var7.setText("Mentés");
      var7.addActionListener(this);
      var7.setMinimumSize(new Dimension(GuiUtil.getW(var7, var7.getText()), GuiUtil.getCommonItemHeight() + 2));
      var7.setPreferredSize(var7.getMinimumSize());
      var7.setMaximumSize(var7.getMinimumSize());
      var5.add(var7, "South");
      this.getContentPane().add(var5, "Center");
      JLabel var8 = new JLabel("WWWSzervezet azonosítóWWWWWWVárakozás tiltásaWWW");
      int var9 = this.orgsOnlineUpdateStatusTable.getColumnModel().getColumn(0).getWidth() + this.orgsOnlineUpdateStatusTable.getColumnModel().getColumn(1).getWidth();
      this.setSize(new Dimension(Math.max(var9, GuiUtil.getW(var8, var8.getText())), 15 * GuiUtil.getCommonItemHeight()));
      this.setPreferredSize(this.getSize());
      this.setMinimumSize(this.getSize());
   }
}
