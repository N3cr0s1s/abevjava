package hu.piller.krtitok.gui;

import hu.piller.kripto.keys.StoreManager;
import hu.piller.kripto.keys.StoreWrapper;
import hu.piller.tools.TableSorter;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

public class FKulcsok extends JDialog {
   private int minWidth = 550;
   private int minHeight = 380;
   private int yKorrekcioPx = 0;
   public StoreWrapper sw;
   private KeysTableModel ktm;
   private KeyWrapperHolder keyHolder;
   private JPanel mainPanel;
   private JPanel kulcsokPanel;
   private JTextField helyTextField;
   private JLabel tipusLabel;
   private JTextField tipusTextField;
   private JLabel helyLabel;
   private JCheckBox priCheckBox;
   private JCheckBox pubCheckBox;
   private JButton rendbenButton;
   private JButton megseButton;
   private JScrollPane kulcsokScrollPane;
   private JTable kulcsokTable;

   public FKulcsok(KeyWrapperHolder keyHolder, StoreWrapper sw, int maxSelectedKeys, boolean showPrivate, boolean showPublic, boolean modal) {
      super((JDialog)keyHolder, modal);
      this.keyHolder = keyHolder;
      this.sw = sw;
      this.setMinWidth();
      this.initComponents();
      this.priCheckBox.setSelected(showPrivate);
      this.pubCheckBox.setSelected(showPublic);
      this.helyTextField.setText(sw.getLocation());
      this.tipusTextField.setText(StoreManager.getTypeDesc("" + sw.getType()));
      this.ktm = new KeysTableModel(sw, maxSelectedKeys, this.priCheckBox.isSelected(), this.pubCheckBox.isSelected());
      TableSorter ts = new TableSorter(this.ktm);
      ts.setTableHeader(this.kulcsokTable.getTableHeader());
      this.kulcsokTable.setModel(ts);
      this.kulcsokTable.getColumnModel().getColumn(0).setCellRenderer(new KeyImageRenderer());
      this.kulcsokTable.getColumnModel().getColumn(1).setCellRenderer(new KeyTypeNameRenderer());
      this.kulcsokTable.getColumnModel().getColumn(0).setPreferredWidth(15);
      this.kulcsokTable.getColumnModel().getColumn(1).setPreferredWidth(50);
      this.kulcsokTable.getColumnModel().getColumn(2).setPreferredWidth(40);
      this.kulcsokTable.getColumnModel().getColumn(3).setPreferredWidth(120);
      this.kulcsokTable.getColumnModel().getColumn(4).setPreferredWidth(120);
      this.kulcsokTable.getColumnModel().getColumn(5).setPreferredWidth(40);
      this.kulcsokTable.getColumnModel().getColumn(5).setCellEditor(new SizeableCBRenderer());
      this.kulcsokTable.getColumnModel().getColumn(5).setCellRenderer(new SizeableCBRenderer());
   }

   private void rendbenButtonActionPerformed(ActionEvent e) {
      if (this.ktm.getSelectedKeys().size() == 0) {
         JOptionPane.showMessageDialog(this, "Nem választott kulcsot!", "Üzenet", 2);
      } else {
         this.keyHolder.addKeyWrappers(this.ktm.getSelectedKeys());
         this.setVisible(false);
         this.dispose();
      }
   }

   private void megseButtonActionPerformed(ActionEvent e) {
      this.setVisible(false);
      this.dispose();
   }

   private void kulcsokTableMouseClicked(MouseEvent e) {
      if (e.getClickCount() >= 2) {
         this.ktm.invertSelection(this.kulcsokTable.getSelectedRow());
      }

   }

   private void priCheckBoxActionPerformed(ActionEvent e) {
      this.ktm.listKeys(this.priCheckBox.isSelected(), this.pubCheckBox.isSelected());
      this.kulcsokTable.repaint();
   }

   private void pubCheckBoxActionPerformed(ActionEvent e) {
      this.ktm.listKeys(this.priCheckBox.isSelected(), this.pubCheckBox.isSelected());
      this.kulcsokTable.repaint();
   }

   private void initComponents() {
      this.mainPanel = new JPanel();
      this.kulcsokPanel = new JPanel();
      this.helyTextField = new JTextField();
      this.tipusLabel = new JLabel();
      this.tipusTextField = new JTextField();
      this.helyLabel = new JLabel();
      this.priCheckBox = new JCheckBox();
      this.pubCheckBox = new JCheckBox();
      this.rendbenButton = new JButton();
      this.megseButton = new JButton();
      this.kulcsokScrollPane = new JScrollPane();
      this.kulcsokTable = new JTable();
      this.setTitle("Kulcsok");
      int xTextField = this.minWidth / 4 + 30;
      JLabel jl = new JLabel("Kulcstár helye:");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      jl.setFont(fm.getFont());
      int curentFontYHeight = jl.getFontMetrics(jl.getFont()).getHeight();
      int yKorrekcio = curentFontYHeight * 15;
      this.yKorrekcioPx = this.minHeight;
      if (this.minHeight < yKorrekcio) {
         this.yKorrekcioPx = yKorrekcio;
      }

      this.setMinimumSize(new Dimension(this.minWidth, this.yKorrekcioPx));
      Container contentPane = this.getContentPane();
      contentPane.setLayout((LayoutManager)null);
      this.mainPanel.setLayout((LayoutManager)null);
      this.kulcsokPanel.setBorder(new EtchedBorder());
      this.kulcsokPanel.setLayout((LayoutManager)null);
      this.helyTextField.setEditable(false);
      this.kulcsokPanel.add(this.helyTextField);
      this.tipusLabel.setText("Kulcstár típusa:");
      this.kulcsokPanel.add(this.tipusLabel);
      if (60 > curentFontYHeight) {
         this.tipusLabel.setBounds(15, 52 + curentFontYHeight / 2, this.tipusLabel.getPreferredSize().width, this.tipusLabel.getPreferredSize().height);
      } else {
         this.tipusLabel.setBounds(15, 72 + curentFontYHeight / 2, this.tipusLabel.getPreferredSize().width, this.tipusLabel.getPreferredSize().height);
      }

      this.tipusTextField.setEditable(false);
      this.kulcsokPanel.add(this.tipusTextField);
      if (60 > curentFontYHeight) {
         this.tipusTextField.setBounds(this.tipusLabel.getX() + this.tipusLabel.getWidth() + 15, 52 + curentFontYHeight / 2, this.tipusLabel.getWidth() + 50, this.tipusTextField.getPreferredSize().height);
      } else {
         this.tipusTextField.setBounds(this.tipusLabel.getX() + this.tipusLabel.getWidth() + 15, 72 + curentFontYHeight / 2, this.tipusLabel.getWidth() + 100, this.tipusTextField.getPreferredSize().height);
      }

      this.helyLabel.setText("Kulcstár helye:");
      this.kulcsokPanel.add(this.helyLabel);
      this.helyLabel.setBounds(new Rectangle(new Point(15, 20), this.helyLabel.getPreferredSize()));
      this.priCheckBox.setText("magán kulcsok");
      this.priCheckBox.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKulcsok.this.priCheckBoxActionPerformed(e);
         }
      });
      this.kulcsokPanel.add(this.priCheckBox);
      this.priCheckBox.setIcon(FKriptodsk.getCheckboxIkonUres());
      this.priCheckBox.setSelectedIcon(FKriptodsk.getCheckboxIkonTeli());
      if (60 > curentFontYHeight) {
         this.priCheckBox.setBounds(new Rectangle(new Point(this.tipusTextField.getX() + this.tipusTextField.getWidth() + 10, 48 + curentFontYHeight / 2), this.priCheckBox.getPreferredSize()));
      } else {
         this.priCheckBox.setBounds(new Rectangle(new Point(this.tipusTextField.getX() + this.tipusTextField.getWidth() + 10, 68 + curentFontYHeight / 2), this.priCheckBox.getPreferredSize()));
      }

      this.pubCheckBox.setText("nyilvános kulcsok");
      this.pubCheckBox.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKulcsok.this.pubCheckBoxActionPerformed(e);
         }
      });
      this.kulcsokPanel.add(this.pubCheckBox);
      this.pubCheckBox.setIcon(FKriptodsk.getCheckboxIkonUres());
      this.pubCheckBox.setSelectedIcon(FKriptodsk.getCheckboxIkonTeli());
      if (60 > curentFontYHeight) {
         this.pubCheckBox.setBounds(new Rectangle(new Point(this.priCheckBox.getX() + this.priCheckBox.getWidth() + 5, 48 + curentFontYHeight / 2), this.pubCheckBox.getPreferredSize()));
      } else {
         this.pubCheckBox.setBounds(new Rectangle(new Point(this.priCheckBox.getX() + this.priCheckBox.getWidth() + 5, 68 + curentFontYHeight / 2), this.pubCheckBox.getPreferredSize()));
      }

      Dimension preferredSize = new Dimension();

      int i;
      Rectangle bounds;
      for(i = 0; i < this.kulcsokPanel.getComponentCount(); ++i) {
         bounds = this.kulcsokPanel.getComponent(i).getBounds();
         preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
         preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
      }

      Insets insets = this.kulcsokPanel.getInsets();
      preferredSize.width += insets.right;
      preferredSize.height += insets.bottom;
      this.kulcsokPanel.setPreferredSize(preferredSize);
      this.helyTextField.setBounds(this.helyLabel.getX() + this.helyLabel.getWidth() + 20, 20, this.minWidth - this.helyLabel.getWidth() - 80, this.helyTextField.getPreferredSize().height);
      this.mainPanel.add(this.kulcsokPanel);
      this.kulcsokPanel.setBounds(15, 10, this.minWidth - 30, (int)((double)this.helyTextField.getHeight() * 4.2D));
      this.rendbenButton.setText("Rendben");
      this.rendbenButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKulcsok.this.rendbenButtonActionPerformed(e);
         }
      });
      this.mainPanel.add(this.rendbenButton);
      this.rendbenButton.setBounds(new Rectangle(new Point(370, 300), this.rendbenButton.getPreferredSize()));
      this.megseButton.setText("Mégsem");
      this.megseButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FKulcsok.this.megseButtonActionPerformed(e);
         }
      });
      this.mainPanel.add(this.megseButton);
      this.megseButton.setBounds(new Rectangle(new Point(460, 300), this.megseButton.getPreferredSize()));
      this.kulcsokTable.setModel(new DefaultTableModel());
      this.kulcsokTable.setBorder((Border)null);
      this.kulcsokTable.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            FKulcsok.this.kulcsokTableMouseClicked(e);
         }
      });
      this.kulcsokScrollPane.setViewportView(this.kulcsokTable);
      this.mainPanel.add(this.kulcsokScrollPane);
      this.kulcsokScrollPane.setBounds(15, this.yKorrekcioPx / 3, this.minWidth - 30, this.yKorrekcioPx - this.kulcsokPanel.getHeight() - 150);
      preferredSize = new Dimension();

      for(i = 0; i < this.mainPanel.getComponentCount(); ++i) {
         bounds = this.mainPanel.getComponent(i).getBounds();
         preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
         preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
      }

      insets = this.mainPanel.getInsets();
      preferredSize.width += insets.right;
      preferredSize.height += insets.bottom;
      this.mainPanel.setPreferredSize(preferredSize);
      contentPane.add(this.mainPanel);
      this.mainPanel.setBounds(0, 0, this.minWidth, this.yKorrekcioPx - 10);
      this.kulcsokTable.setRowHeight(this.helyTextField.getHeight());
      this.rendbenButton.setBounds(this.minWidth - this.megseButton.getWidth() - 20 - this.rendbenButton.getWidth(), this.yKorrekcioPx - this.rendbenButton.getHeight() - 20, this.rendbenButton.getWidth(), this.rendbenButton.getHeight());
      this.megseButton.setBounds(this.minWidth - this.megseButton.getWidth() - 10, this.yKorrekcioPx - this.megseButton.getHeight() - 20, this.megseButton.getWidth(), this.megseButton.getHeight());
      preferredSize = new Dimension();

      for(i = 0; i < contentPane.getComponentCount(); ++i) {
         bounds = contentPane.getComponent(i).getBounds();
         preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
         preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
      }

      insets = contentPane.getInsets();
      preferredSize.width += insets.right;
      preferredSize.height += insets.bottom;
      ((JComponent)contentPane).setPreferredSize(preferredSize);
      this.pack();
      this.setLocationRelativeTo(this.getOwner());
   }

   private void setMinWidth() {
      JLabel jl = new JLabel("Kulcstár helye:");
      FontMetrics fm = jl.getFontMetrics(jl.getFont());
      if (this.minWidth < fm.stringWidth(jl.getText()) * 6) {
         this.minWidth = fm.stringWidth(jl.getText()) * 6;
      }

   }
}
