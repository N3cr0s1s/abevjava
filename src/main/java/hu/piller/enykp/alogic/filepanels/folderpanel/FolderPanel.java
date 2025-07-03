package hu.piller.enykp.alogic.filepanels.folderpanel;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;

public class FolderPanel extends JPanel {
   public static final String COMPONENT_DRIVES_CBO = "drives";
   public static final String COMPONENT_FOLDERS_TRE = "folders";
   public static final String COMPONENT_PATH_TXT = "path";
   public static final String COMPONENT_SPLITTER_LBL = "splitter";
   public static final String COMPONENT_FOLDER_PANEL = "folder_panel";
   public static final String COMPONENT_FOLDERS_SCP = "folders_scp";
   public static final String COMPONENT_PATH_SCP = "path_scp";
   private FolderBusiness f_business;
   private JComboBox cbo_drives;
   private JTree tre_folders;
   private JTextField txt_path;
   private JLabel lbl_splitter;
   private JScrollPane scp_folders;
   private JScrollPane scp_path;
   private JPanel folder_panel;

   public FolderPanel() {
      this.build();
      this.prepare();
   }

   private void build() {
      this.setLayout(new BoxLayout(this, 0));
      this.setBorder(BorderFactory.createEtchedBorder(0));
      this.add(this.getFolderPanel());
      this.add(this.getSplitter());
   }

   private JPanel getFolderPanel() {
      JLabel var1 = new JLabel("Meghajtók");
      var1.setAlignmentX(0.0F);
      this.cbo_drives = new JComboBox();
      this.cbo_drives.setAlignmentX(0.0F);
      JLabel var2 = new JLabel("Mappák");
      var2.setAlignmentX(0.0F);
      this.tre_folders = new JTree((TreeNode)null);
      JLabel var3 = new JLabel("Útvonal");
      var3.setAlignmentX(0.0F);
      this.txt_path = new JTextField();
      this.scp_folders = new JScrollPane(this.tre_folders);
      this.scp_folders.setMinimumSize(new Dimension(100, 100));
      this.scp_folders.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
      this.scp_folders.setAlignmentX(0.0F);
      this.scp_path = new JScrollPane(this.txt_path);
      this.scp_path.setVerticalScrollBarPolicy(21);
      this.scp_path.setHorizontalScrollBarPolicy(32);
      this.scp_path.setMinimumSize(new Dimension(100, 40));
      this.scp_path.setAlignmentX(0.0F);
      this.folder_panel = new JPanel();
      this.folder_panel.setLayout(new BoxLayout(this.folder_panel, 1));
      this.folder_panel.setMinimumSize(new Dimension(50, 100));
      this.folder_panel.setAlignmentY(0.0F);
      this.folder_panel.add(var1, (Object)null);
      this.folder_panel.add(this.cbo_drives, (Object)null);
      this.folder_panel.add(Box.createRigidArea(new Dimension(0, 5)));
      this.folder_panel.add(var2, (Object)null);
      this.folder_panel.add(this.scp_folders, (Object)null);
      this.folder_panel.add(Box.createGlue(), (Object)null);
      this.folder_panel.add(Box.createRigidArea(new Dimension(0, 5)));
      this.folder_panel.add(var3, (Object)null);
      this.folder_panel.add(this.scp_path, (Object)null);
      return this.folder_panel;
   }

   private JLabel getSplitter() {
      if (this.lbl_splitter == null) {
         this.lbl_splitter = new JLabel();
         this.lbl_splitter.setBackground(new Color(230, 230, 230));
         this.lbl_splitter.setOpaque(true);
         this.lbl_splitter.setMaximumSize(new Dimension(7, Integer.MAX_VALUE));
         this.lbl_splitter.setAlignmentY(0.0F);
      }

      return this.lbl_splitter;
   }

   private void prepare() {
      this.f_business = new FolderBusiness(this);
   }

   public FolderBusiness getBusiness() {
      return this.f_business;
   }

   public JComponent getFPComponent(String var1) {
      if ("drives".equalsIgnoreCase(var1)) {
         return this.cbo_drives;
      } else if ("folders".equalsIgnoreCase(var1)) {
         return this.tre_folders;
      } else if ("path".equalsIgnoreCase(var1)) {
         return this.txt_path;
      } else if ("splitter".equalsIgnoreCase(var1)) {
         return this.lbl_splitter;
      } else if ("folder_panel".equalsIgnoreCase(var1)) {
         return this.folder_panel;
      } else if ("folders_scp".equalsIgnoreCase(var1)) {
         return this.scp_folders;
      } else {
         return "path_scp".equalsIgnoreCase(var1) ? this.scp_path : null;
      }
   }
}
