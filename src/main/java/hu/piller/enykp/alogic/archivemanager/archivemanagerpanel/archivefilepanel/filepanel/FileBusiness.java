package hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.archivefilepanel.filepanel;

import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.ArchiveFileTable;
import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.Archiver;
import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.IFilterPanelBusiness;
import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.IFilterPanelLogic;
import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.IFilteredFilesRefresh;
import hu.piller.enykp.alogic.filepanels.tablesorter.TableSorter;
import hu.piller.enykp.alogic.fileutil.FileStatusChecker;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.interfaces.IFileChooser;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.eventsupport.DefaultEventSupport;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import hu.piller.enykp.util.filelist.EnykFileList;
import me.necrocore.abevjava.NecroFile;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.net.URI;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class FileBusiness extends DefaultFileBusiness implements IFileChooser, IEventSupport, IFilteredFilesRefresh {
   public static final boolean debugOn = false;
   private final DefaultEventSupport des = new DefaultEventSupport();
   private Object[] defaultFilterColumns = new Object[]{new Integer(12), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(8), new Integer(9), new Integer(10), new Integer(11), new Integer(13), new Integer(14)};
   public Object[] defaultFileColumns = new Object[]{"id", "", "Nyomtatvány neve", "Adószám", "Név", "Dátumtól", "Dátumig", "Verzió", "Információ", "Adóazonosító", "Mentve", "Megjegyzés", "Státusz", "Állomány", "Csat. db."};
   private boolean archivPanel;
   protected static final int ID_COLUMN_ID = 0;
   public static final int ID_COLUMN_SELECT = 1;
   public static final String[] ID_COLUMN_NAMES = new String[]{"id", "col_select", "form_name", "tax_number", "name", "from_date", "to_date", "version", "information", "tax_id", "save_date", "note", "state", "file", "atch"};
   private FilePanel file_panel;
   private File current_path;
   protected JList lst_file_filters;
   protected ArchiveFileTable tbl_files;
   protected IFilterPanelLogic filter_panel;
   protected IFilterPanelBusiness filter_business;
   protected JLabel lbl_form_name;
   protected JLabel lbl_name;
   protected JLabel lbl_date_from;
   protected JLabel lbl_date_to;
   protected JLabel lbl_info;
   protected JLabel lbl_note;
   protected JLabel lbl_state;
   protected JPanel buttons_panel;
   protected JPanel file_list_panel;
   protected JLabel lbl_file_filters;
   protected JScrollPane scp_file_filters;
   protected JButton btn_11;
   protected JButton btn_12;
   protected JButton btn_13;
   protected JButton btn_14;
   protected JButton btn_15;
   protected JButton btn_21;
   protected JButton btn_22;
   protected JButton btn_31;
   protected JButton btn_32;
   protected JTextField txt_path;
   protected JScrollPane scp_path;
   protected Component comp_bsep;
   private JLabel lbl_filter_title = null;
   protected JButton btn_select_all;
   protected JButton btn_unselect_all;
   protected JScrollPane scp_file_list;
   protected JPanel select_sum_panel;
   protected JLabel lbl_select_sum_all;
   protected JLabel lbl_select_sum_sel;
   protected FileBusiness.ButtonExecutor button_executor = null;
   protected Vector vct_files = new Vector(256, 256);
   protected Vector vct_filtered_files = new Vector(256, 256);
   protected int task_id;
   protected final FileBusiness.VisibilityController visibility_controller = new FileBusiness.VisibilityController();
   private ArchiveFileTable work_table;
   Object[] fileInfos = new Object[]{"filelist", null, null};
   public static final int TASK_NEW_DLG = 1;
   public static final int TASK_OPEN_DLG = 2;
   public static final int TASK_SAVE_DLG = 3;
   public static final int TASK_FORMDATALIST_DLG = 4;
   public static final int TASK_DATAFILEOPERATIONS_DLG = 5;
   public static final int TASK_INSTALLEDFORMS_DLG = 6;
   public static final int TASK_RENAMEDATAFILES_DLG = 7;

   public void addEventListener(IEventListener var1) {
      this.des.addEventListener(var1);
   }

   public void removeEventListener(IEventListener var1) {
      this.des.removeEventListener(var1);
   }

   public Vector fireEvent(Event var1) {
      return this.des.fireEvent(var1);
   }

   public void fireSingleClickOnFiles() {
      this.des.fireEvent(this, "update", "single_click_on_file");
   }

   public void fireDoubleClickOnFiles() {
      this.des.fireEvent(this, "update", "double_click_on_file");
   }

   public FileBusiness(FilePanel var1) {
      this.file_panel = var1;
      this.prepare();
   }

   private void prepare() {
      this.lst_file_filters = (JList)this.file_panel.getFPComponent("file_filter");
      this.tbl_files = (ArchiveFileTable)this.file_panel.getFPComponent("files");
      this.lbl_form_name = (JLabel)this.file_panel.getFPComponent("form_name_lbl");
      this.lbl_name = (JLabel)this.file_panel.getFPComponent("name_lbl");
      this.lbl_date_from = (JLabel)this.file_panel.getFPComponent("date_from_lbl");
      this.lbl_date_to = (JLabel)this.file_panel.getFPComponent("date_to_lbl");
      this.lbl_info = (JLabel)this.file_panel.getFPComponent("info_lbl");
      this.lbl_note = (JLabel)this.file_panel.getFPComponent("note_lbl");
      this.lbl_state = (JLabel)this.file_panel.getFPComponent("state_lbl");
      this.buttons_panel = (JPanel)this.file_panel.getFPComponent("buttons_panel");
      this.file_list_panel = (JPanel)this.file_panel.getFPComponent("file_list_panel");
      this.lbl_file_filters = (JLabel)this.file_panel.getFPComponent("file_filters_lbl");
      this.scp_file_filters = (JScrollPane)this.file_panel.getFPComponent("file_filters_scp");
      this.btn_11 = (JButton)this.file_panel.getFPComponent("select_all");
      this.btn_12 = (JButton)this.file_panel.getFPComponent("deselect_all");
      this.btn_13 = (JButton)this.file_panel.getFPComponent("replica");
      this.btn_14 = (JButton)this.file_panel.getFPComponent("rename");
      this.btn_15 = (JButton)this.file_panel.getFPComponent("delete");
      this.btn_21 = (JButton)this.file_panel.getFPComponent("copy");
      this.btn_22 = (JButton)this.file_panel.getFPComponent("choose_path");
      this.txt_path = (JTextField)this.file_panel.getFPComponent("path_txt");
      this.scp_path = (JScrollPane)this.file_panel.getFPComponent("path_csp");
      this.btn_31 = (JButton)this.file_panel.getFPComponent("cancel");
      this.btn_32 = (JButton)this.file_panel.getFPComponent("ok");
      this.comp_bsep = this.file_panel.getFPComponent("btn_line_sep");
      this.lbl_filter_title = (JLabel)this.file_panel.getFPComponent("filter_title_lbl");
      this.btn_select_all = (JButton)this.file_panel.getFPComponent("btn_select_all");
      this.btn_unselect_all = (JButton)this.file_panel.getFPComponent("btn_unselect_all");
      this.scp_file_list = (JScrollPane)this.file_panel.getFPComponent("scp_file_list");
      this.select_sum_panel = (JPanel)this.file_panel.getFPComponent("select_sum_panel");
      this.lbl_select_sum_all = (JLabel)this.file_panel.getFPComponent("lbl_select_sum_all");
      this.lbl_select_sum_sel = (JLabel)this.file_panel.getFPComponent("lbl_select_sum_sel");
      this.filter_panel = (IFilterPanelLogic)this.file_panel.getFPComponent("file_filter_panel");
      this.filter_business = this.filter_panel.getBusinessHandler();
      this.filter_business.setFileBusiness(this);
      this.prepareFiles();
      this.work_table = new ArchiveFileTable();
      this.work_table.init(this, 0, this.defaultFileColumns);
      this.prepareButtons();
      this.prepareButtons2();
      this.file_panel.addComponentListener(this.visibility_controller);
   }

   protected void prepareFiles() {
      this.tbl_files.init(this, 0, this.defaultFileColumns);
   }

   protected void prepareButtons2() {
   }

   private void prepareButtons() {
      this.btn_11.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (FileBusiness.this.button_executor != null) {
               FileBusiness.this.button_executor.b11Clicked();
            }

         }
      });
      this.btn_12.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (FileBusiness.this.button_executor != null) {
               FileBusiness.this.button_executor.b12Clicked();
            }

         }
      });
      this.btn_13.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (FileBusiness.this.button_executor != null) {
               FileBusiness.this.button_executor.b13Clicked();
            }

         }
      });
      this.btn_14.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (FileBusiness.this.button_executor != null) {
               FileBusiness.this.button_executor.b14Clicked();
            }

         }
      });
      this.btn_15.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (FileBusiness.this.button_executor != null) {
               FileBusiness.this.button_executor.b15Clicked();
            }

         }
      });
      this.btn_21.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (FileBusiness.this.button_executor != null) {
               FileBusiness.this.button_executor.b21Clicked();
            }

         }
      });
      this.btn_22.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (FileBusiness.this.button_executor != null) {
               FileBusiness.this.button_executor.b22PathClicked();
            }

         }
      });
      this.btn_31.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (FileBusiness.this.button_executor != null) {
               FileBusiness.this.button_executor.b31Clicked();
            }

         }
      });
      this.btn_32.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (FileBusiness.this.button_executor != null) {
               FileBusiness.this.button_executor.b32Clicked();
            }

         }
      });
   }

   public void setSelectedPath(File var1) {
      this.current_path = var1;
   }

   private File getSelectedPath() {
      return this.current_path;
   }

   public void setSelectedRow(File var1) {
      DefaultTableModel var2 = (DefaultTableModel)this.tbl_files.getModel();
      if (var1 != null) {
         int var4 = 0;

         for(int var5 = var2.getRowCount(); var4 < var5; ++var4) {
            Object var3 = var2.getValueAt(var4, 0);
            if (var3 instanceof ListItem) {
               ListItem var6 = (ListItem)var3;
               File var7 = (File)var6.getItem();
               if (var1.equals(var7)) {
                  this.tbl_files.getSelectionModel().setSelectionInterval(var4, var4);
                  break;
               }
            }
         }
      }

   }

   public void storeFileInfo() {
      this.filter_business.storeFileInfo(this.vct_files);
   }

   public void resetFileInfo() {
      this.filter_business.resetFileInfo(this.vct_files);
   }

   public void reLoadFileInfo() {
      this.resetFileInfo();
   }

   public void showFileInfos() {
      File var1 = this.current_path;
      System.out.println(">Scanning directory: " + var1);
      long var2 = System.currentTimeMillis();
      this.vct_files.clear();
      String[] var4 = this.getSelectedFilters();
      Object[] var5 = this.getFileInfos(var1.getPath(), var4);
      if (var5 != null) {
         for(int var6 = 0; var6 < var5.length; ++var6) {
            Object[] var7 = (Object[])((Object[])var5[var6]);
            this.vct_files.add(new ListItem(new NecroFile((String)var7[0]), (Icon)null, var7[1], var7[2]));
         }
      }

      long var8 = System.currentTimeMillis() - var2;
      System.out.println(">Load head time sum: " + var8 + " ms");
      this.showFileList(this.vct_files);
      System.out.println(" ... done.");
   }

   private Object[] getFileInfos(String var1, Object[] var2) {
      EnykFileList var3 = EnykFileList.getInstance();
      Object[] var4 = var3.list(var1, var2);
      return var4 instanceof Object[] ? (Object[])((Object[])var4) : null;
   }

   public Object[] getFileTableRow(Object var1) {
      Object[] var2 = new Object[15];
      if (var1 instanceof ListItem) {
         ListItem var3 = (ListItem)var1;
         if (var3.getText() instanceof Hashtable) {
            Hashtable var4 = (Hashtable)var3.getText();
            int var5 = 0;
            String var6 = this.getString(var4.get("type"));
            Vector var7 = new Vector();
            if (var4.get("docs") instanceof Vector) {
               var7 = (Vector)var4.get("docs");
            }

            if ("single".equalsIgnoreCase(var6)) {
               var5 = 1;
            } else if ("multi".equalsIgnoreCase(var6)) {
               var5 = 2;
            }

            if (var5 == 0) {
               var5 = var7.size();
            }

            var2[0] = var3;
            var2[1] = Boolean.FALSE;
            var2[13] = ((File)var3.getItem()).getName();
            var2[2] = "";
            var2[3] = "";
            var2[4] = "";
            var2[5] = "";
            var2[6] = "";
            var2[8] = "";
            var2[9] = "";
            var2[10] = this.formatDate(var4.get("saved"));
            var2[11] = "";
            var2[7] = "";
            var2[12] = "";
            var2[14] = "";
            Hashtable var8;
            switch(var5) {
            case 0:
               var2[8] = "(Ismeretlen dokumentum)";
               break;
            case 1:
               var1 = var4.get("docinfo");
               if (var1 instanceof Hashtable) {
                  var8 = (Hashtable)var1;
                  if (this.getString(var8.get("note")).startsWith("susi")) {
                     System.out.println("");
                  }

                  var2[2] = this.getString(var8.get("name"));
                  var2[3] = this.getString(var8.get("tax_number"));
                  var2[9] = this.getString(var8.get("account_name"));
                  var2[4] = this.getString(var8.get("person_name"));
                  var2[5] = this.formatDate(var8.get("from_date"));
                  var2[6] = this.formatDate(var8.get("to_date"));
                  var2[8] = this.getString(var8.get("info"));
                  var2[11] = this.getString(var8.get("note"));
                  var2[7] = this.getString(var8.get("ver"));
                  var2[12] = this.getFileState((File)var3.getItem(), this.getString(var8.get("krfilename")));
                  var2[14] = this.getString(var8.get("attachment_count"));
               } else {
                  var2[2] = "Egyszerű dokumentum";
                  var2[8] = "(Nincs információ)";
               }
               break;
            default:
               var1 = var4.get("docinfo");
               if (var1 instanceof Hashtable) {
                  var8 = (Hashtable)var1;
                  var2[2] = this.getString(var8.get("name"));
                  var2[8] = this.getString(var8.get("info"));
                  var2[11] = this.getString(var8.get("note"));
                  var2[7] = this.getString(var8.get("ver"));

                  try {
                     var2[5] = this.formatDate(var8.get("from_date"));
                     var2[6] = this.formatDate(var8.get("to_date"));
                     var2[4] = this.getString(var8.get("person_name"));
                     var2[3] = this.getString(var8.get("tax_number"));
                     var2[9] = this.getString(var8.get("account_name"));
                  } catch (Exception var10) {
                     Tools.eLog(var10, 0);
                  }

                  var2[12] = this.getFileState((File)var3.getItem(), this.getString(var8.get("krfilename")));
                  var2[14] = this.getString(var8.get("attachment_count"));
               } else {
                  var2[2] = "Multi dokumentum";
                  var2[8] = "(Nincs információ)";
               }
            }
         }
      }

      return var2;
   }

   private Hashtable getSelectedHeadData(int var1) {
      int[] var4 = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
      DefaultTableModel var3 = (DefaultTableModel)this.tbl_files.getModel();
      Hashtable var2 = new Hashtable(var3.getColumnCount());
      int var5 = 0;

      for(int var6 = var4.length; var5 < var6; ++var5) {
         var2.put(ID_COLUMN_NAMES[var4[var5]], var3.getValueAt(var1, var4[var5]));
      }

      return var2;
   }

   private String getFileState(File var1, String var2) {
      if (this.isArchivPanel()) {
         File var3 = var1.getParentFile();
         String var4 = Archiver.getOriginalFileName(var1);
         File var5 = new NecroFile(var3, var4);
         return this.filter_business.getFileState(var5, var2);
      } else {
         return this.filter_business.getFileState(var1, var2);
      }
   }

   private String formatDate(Object var1) {
      if (var1 == null) {
         return "";
      } else {
         char[] var3 = var1.toString().trim().toCharArray();
         String var2 = "";
         int var4 = 0;

         for(int var5 = var3.length; var4 < var5 && var4 < 14; ++var4) {
            if (var4 != 4 && var4 != 6 && var4 != 14) {
               if (var4 == 8) {
                  var2 = var2 + "   ";
               } else if (var4 == 10 || var4 == 12) {
                  var2 = var2 + ":";
               }
            } else {
               var2 = var2 + ".";
            }

            var2 = var2 + var3[var4];
         }

         return var2;
      }
   }

   public void showFileList(Vector var1) {
      TableSorter var5 = this.tbl_files.getTable_sorter();
      int var3 = var5.getSortOrder();
      int var4 = var5.getSortedColumn();
      var5.setSortEnabled(false);
      DefaultTableModel var2 = (DefaultTableModel)this.tbl_files.getModel();
      var2.getDataVector().clear();
      FileStatusChecker.getInstance().startBatchMode();
      int var6 = 0;

      for(int var7 = var1.size(); var6 < var7; ++var6) {
         var2.addRow(this.getFileTableRow(var1.get(var6)));
      }

      FileStatusChecker.getInstance().stopBatchMode();
      var5.setSortEnabled(true);
      var5.sort(var4, var3);
      this.setCounters(false);
      this.tbl_files.revalidate();
      this.tbl_files.repaint();
      this.refreshFilterSource(var1);
   }

   private void refreshFilterSource(Vector var1) {
      DefaultTableModel var2 = (DefaultTableModel)this.work_table.getModel();
      var2.getDataVector().clear();
      FileStatusChecker.getInstance().startBatchMode();
      int var3 = 0;

      for(int var4 = var1.size(); var3 < var4; ++var3) {
         var2.addRow(this.getFileTableRow(var1.get(var3)));
      }

      FileStatusChecker.getInstance().stopBatchMode();
      this.filter_business.refresh(var2);
   }

   public void showFileList(DefaultTableModel var1) {
      TableSorter var5 = this.tbl_files.getTable_sorter();
      int var3 = var5.getSortOrder();
      int var4 = var5.getSortedColumn();
      var5.setSortEnabled(false);
      DefaultTableModel var2 = (DefaultTableModel)this.tbl_files.getModel();
      var2.getDataVector().clear();
      FileStatusChecker.getInstance().startBatchMode();
      int var6 = 0;

      for(int var7 = var1.getDataVector().size(); var6 < var7; ++var6) {
         var2.addRow(this.getFileTableRow(((Vector)((Vector)var1.getDataVector().get(var6))).get(0)));
      }

      FileStatusChecker.getInstance().stopBatchMode();
      var5.setSortEnabled(true);
      var5.sort(var4, var3);
      this.setCounters(false);
      this.tbl_files.revalidate();
      this.tbl_files.repaint();
   }

   protected void setCounters(boolean var1) {
   }

   private void filterFileList() {
      this.vct_filtered_files = this.filter_business.filterFileList(this.vct_files);
   }

   public Vector getVct_filtered_files() {
      return this.vct_filtered_files;
   }

   public void setVct_filtered_files(Vector var1) {
      this.vct_filtered_files = var1;
   }

   public Vector getVct_files() {
      return this.vct_files;
   }

   public void setSelectedFiles(File[] var1) {
      int var3 = var1.length;
      Vector var4 = new Vector(var3);
      Vector var5 = this.getFileColumn();
      File var7 = this.current_path;

      int var8;
      for(var8 = 0; var8 < var3; ++var8) {
         File var6;
         if ((var6 = var1[var8]) != null && var6.getParentFile().equals(var7) && var6.exists()) {
            var4.add(var6);
         }
      }

      var8 = 0;

      int var9;
      for(var9 = var4.size(); var8 < var9; ++var8) {
         if (!var5.contains(var4.get(var8))) {
            var4.remove(var8--);
         }
      }

      int[] var2 = new int[var4.size()];
      var8 = 0;

      for(var9 = var4.size(); var8 < var9; ++var8) {
         var2[var8] = var5.indexOf(var4.get(var8));
      }

      this.tbl_files.clearSelection();
      var8 = 0;

      for(var9 = var2.length; var8 < var9; ++var8) {
         this.tbl_files.getSelectionModel().setSelectionInterval(var2[var8], var2[var8]);
      }

   }

   public Object[] getSelectedFiles() {
      int[] var2 = this.tbl_files.getSelectedRows();
      int var6 = this.tbl_files.getRowCount();
      int var3 = var2.length;
      int var5 = 0;

      int var4;
      for(var4 = 0; var5 < var3; ++var5) {
         if (var2[var5] >= 0 && var2[var5] < var6) {
            ++var4;
         }
      }

      Object[] var7 = null;
      if (var4 > 0) {
         int[] var1 = new int[var4];
         var5 = 0;

         for(var4 = 0; var5 < var3; ++var5) {
            if (var2[var5] >= 0 || var2[var5] < var6) {
               var1[var4++] = var2[var5];
            }
         }

         Vector var10 = this.getColumn(0);
         var7 = new Object[var1.length];

         for(var5 = 0; var5 < var3; ++var5) {
            try {
               ListItem var9 = (ListItem)var10.get(var1[var5]);
               Object[] var8 = new Object[3];
               if (var9 != null) {
                  var8[0] = var9.getItem();
                  var8[1] = var9.getSecondItem();
                  var8[2] = this.getSelectedHeadData(var1[var5]);
               }

               var7[var5] = var8;
            } catch (Exception var12) {
               var7[var5] = null;
            }
         }
      }

      return var7;
   }

   public void setSelectedFilters(String[] var1) {
      this.filter_business.setSelectedFilters(var1);
   }

   public String[] getSelectedFilters() {
      return this.filter_business.getSelectedFilters();
   }

   public String[] getAllFilters() {
      return this.filter_business.getAllFilters();
   }

   public void addFilters(String[] var1, String[] var2) {
      this.filter_business.addFilters(var1, var2);
   }

   public void removeFilters(String[] var1) {
      this.filter_business.removeFilters(var1);
   }

   public void rescan() {
      this.showFileInfos();
   }

   public void hideFilters(String[] var1) {
      this.filter_business.hideFilters(var1);
   }

   public void showFilters(String[] var1) {
      this.filter_business.showFilters(var1);
   }

   public void setSelectedPath(URI var1) {
   }

   private Vector getFileColumn() {
      Vector var2 = this.getColumn(0);
      Vector var3 = new Vector(var2.size());
      int var4 = 0;

      for(int var5 = var2.size(); var4 < var5; ++var4) {
         Object var1 = var2.get(var4);
         if (var1 instanceof ListItem) {
            var3.add(((ListItem)var2.get(var4)).getItem());
         } else {
            var3.add(var1);
         }
      }

      return var3;
   }

   private Vector getColumn(int var1) {
      DefaultTableModel var3 = (DefaultTableModel)this.tbl_files.getModel();
      Vector var2 = new Vector(var3.getRowCount());
      int var4 = 0;

      for(int var5 = var3.getRowCount(); var4 < var5; ++var4) {
         var2.add(var3.getValueAt(var4, var1));
      }

      return var2;
   }

   public void setTask(int var1) {
      TableColumnModel var2;
      TableColumn var3;
      switch(var1) {
      case 1:
         this.task_id = var1;
         this.sizeTableColumns();
         var2 = this.tbl_files.getColumnModel();
         var3 = var2.getColumn(5);
         var3.setMaxWidth(0);
         var3.setMinWidth(0);
         var3.setPreferredWidth(0);
         var3.setResizable(false);
         var3 = var2.getColumn(6);
         var3.setMaxWidth(0);
         var3.setMinWidth(0);
         var3.setPreferredWidth(0);
         var3.setResizable(false);
         var3 = var2.getColumn(4);
         var3.setMaxWidth(0);
         var3.setMinWidth(0);
         var3.setPreferredWidth(0);
         var3.setResizable(false);
         var3 = var2.getColumn(11);
         var3.setMaxWidth(0);
         var3.setMinWidth(0);
         var3.setPreferredWidth(0);
         var3.setResizable(false);
         var3 = var2.getColumn(9);
         var3.setMaxWidth(0);
         var3.setMinWidth(0);
         var3.setPreferredWidth(0);
         var3.setResizable(false);
         var3 = var2.getColumn(10);
         var3.setMaxWidth(0);
         var3.setMinWidth(0);
         var3.setPreferredWidth(0);
         var3.setResizable(false);
         var3 = var2.getColumn(12);
         var3.setMaxWidth(0);
         var3.setMinWidth(0);
         var3.setPreferredWidth(0);
         var3.setResizable(false);
         var3 = var2.getColumn(3);
         var3.setMaxWidth(0);
         var3.setMinWidth(0);
         var3.setPreferredWidth(0);
         var3.setResizable(false);
         this.addFilterComponentsToVisibilityControl(false);
         this.setButtonsPanelVisibility(false);
         this.visibility_controller.setVisibleAll(false);
         this.addFileTypeFilterComponentsToVisibilitiControll(true);
         break;
      case 2:
         this.task_id = var1;
         this.sizeTableColumns();
         var3 = this.tbl_files.getColumnModel().getColumn(14);
         var3.setMaxWidth(100);
         var3.setMinWidth(10);
         var3.setPreferredWidth(50);
         var3.setResizable(true);
         this.addFilterComponentsToVisibilityControl(true);
         this.addFileTypeFilterComponentsToVisibilitiControll(true);
         this.setFilterComponentsVisible(false);
         this.visibility_controller.setVisibleAll(false);
         this.btn_11.setVisible(false);
         this.btn_12.setVisible(false);
         this.btn_13.setVisible(false);
         this.btn_14.setVisible(false);
         this.btn_15.setVisible(false);
         this.btn_21.setVisible(false);
         this.btn_22.setVisible(false);
         this.scp_path.setVisible(false);
         this.btn_31.setVisible(false);
         this.btn_32.setVisible(false);
         this.comp_bsep.setVisible(false);
         break;
      case 3:
         this.task_id = var1;
         this.changeFileFilter();
         this.addFilterComponentsToVisibilityControl(false);
         this.setButtonsPanelVisibility(false);
         this.addFileListPanelToVisibilitiControl(false);
         this.visibility_controller.setVisibleAll(false);
         this.addFileTypeFilterComponentsToVisibilitiControll(false);
         break;
      case 4:
         this.task_id = var1;
         this.sizeTableColumns();
         this.addFilterComponentsToVisibilityControl(true);
         this.addFileTypeFilterComponentsToVisibilitiControll(true);
         this.visibility_controller.setVisibleAll(false);
         this.btn_14.setVisible(false);
         this.btn_15.setVisible(false);
         this.btn_21.setVisible(false);
         this.scp_path.setVisible(false);
         this.btn_22.setVisible(false);
         this.btn_31.setVisible(false);
         this.btn_11.setText("Lista szerkesztés");
         this.btn_12.setText("Nyomtatás");
         this.btn_13.setText("Mentés file-ba");
         this.btn_32.setText("Kilép");
         break;
      case 5:
         this.task_id = var1;
         this.sizeTableColumns();
         this.addFilterComponentsToVisibilityControl(true);
         this.addFileTypeFilterComponentsToVisibilitiControll(true);
         this.visibility_controller.setVisibleAll(false);
         this.btn_15.setVisible(false);
         this.btn_31.setVisible(false);
         this.btn_11.setText("ENYK mappa");
         this.btn_12.setText("Külső mappa");
         this.btn_13.setText("Átnevezés");
         this.btn_14.setText("Törlés");
         this.btn_21.setText("Másolás");
         this.btn_22.setText("Külső mappa ...");
         this.btn_32.setText("Mégsem");
         break;
      case 6:
         this.task_id = var1;
         this.sizeTableColumns();
         var2 = this.tbl_files.getColumnModel();
         var3 = var2.getColumn(5);
         var3.setMaxWidth(0);
         var3.setMinWidth(0);
         var3.setPreferredWidth(0);
         var3.setResizable(false);
         var3 = var2.getColumn(6);
         var3.setMaxWidth(0);
         var3.setMinWidth(0);
         var3.setPreferredWidth(0);
         var3.setResizable(false);
         var3 = var2.getColumn(4);
         var3.setMaxWidth(0);
         var3.setMinWidth(0);
         var3.setPreferredWidth(0);
         var3.setResizable(false);
         var3 = var2.getColumn(11);
         var3.setMaxWidth(0);
         var3.setMinWidth(0);
         var3.setPreferredWidth(0);
         var3.setResizable(false);
         var3 = var2.getColumn(9);
         var3.setMaxWidth(0);
         var3.setMinWidth(0);
         var3.setPreferredWidth(0);
         var3.setResizable(false);
         var3 = var2.getColumn(10);
         var3.setMaxWidth(0);
         var3.setMinWidth(0);
         var3.setPreferredWidth(0);
         var3.setResizable(false);
         var3 = var2.getColumn(12);
         var3.setMaxWidth(0);
         var3.setMinWidth(0);
         var3.setPreferredWidth(0);
         var3.setResizable(false);
         var3 = var2.getColumn(3);
         var3.setMaxWidth(0);
         var3.setMinWidth(0);
         var3.setPreferredWidth(0);
         var3.setResizable(false);
         this.addFilterComponentsToVisibilityControl(false);
         this.addInstalledFormsFilterComponentsToVisibilityControl(true);
         this.addFileTypeFilterComponentsToVisibilitiControll(true);
         this.visibility_controller.setVisibleAll(false);
         this.btn_11.setVisible(false);
         this.btn_12.setVisible(false);
         this.btn_13.setVisible(false);
         this.btn_14.setVisible(false);
         this.btn_15.setVisible(false);
         this.btn_21.setVisible(false);
         this.scp_path.setVisible(false);
         this.btn_22.setVisible(false);
         this.btn_31.setVisible(false);
         this.btn_32.setText("Ok");
         this.btn_32.setAlignmentX(0.5F);
         break;
      case 7:
         this.task_id = var1;
         this.sizeTableColumns();
         this.addFilterComponentsToVisibilityControl(true);
         this.addFileTypeFilterComponentsToVisibilitiControll(true);
         this.visibility_controller.setVisibleAll(false);
         this.btn_12.setVisible(false);
         this.btn_13.setVisible(false);
         this.btn_14.setVisible(false);
         this.btn_15.setVisible(false);
         this.btn_21.setVisible(false);
         this.scp_path.setVisible(false);
         this.btn_22.setVisible(false);
         this.btn_31.setVisible(false);
         this.btn_11.setText("Átnevezés");
         this.btn_32.setText("Mégsem");
      }

      this.filter_business.initials(new Object[]{this.tbl_files, this.getFilterColumns(), new Integer(8)});
      this.file_panel.revalidate();
      this.file_panel.repaint();
   }

   private Vector getFilterColumns() {
      Vector var1 = new Vector();

      for(int var2 = 0; var2 < this.defaultFilterColumns.length; ++var2) {
         var1.add(this.defaultFilterColumns[var2]);
      }

      return var1;
   }

   private void sizeTableColumns() {
      GuiUtil.setTableColWidth(this.tbl_files);
   }

   private void _sizeTableColumns() {
      TableColumnModel var1 = this.tbl_files.getColumnModel();
      TableColumn var2 = var1.getColumn(1);
      var2.setMaxWidth(18);
      var2.setMinWidth(18);
      var2.setPreferredWidth(18);
      var2.setResizable(false);
      var2 = var1.getColumn(2);
      var2.setMinWidth(120);
      var2.setPreferredWidth(120);
      var2 = var1.getColumn(3);
      var2.setMinWidth(100);
      var2.setPreferredWidth(100);
      var2 = var1.getColumn(4);
      var2.setMinWidth(150);
      var2.setPreferredWidth(150);
      var2 = var1.getColumn(5);
      var2.setMinWidth(70);
      var2.setPreferredWidth(70);
      var2 = var1.getColumn(6);
      var2.setMinWidth(70);
      var2.setPreferredWidth(70);
      var2 = var1.getColumn(7);
      var2.setMinWidth(70);
      var2.setPreferredWidth(70);
      var2 = var1.getColumn(8);
      var2.setMinWidth(150);
      var2.setPreferredWidth(150);
      var2 = var1.getColumn(9);
      var2.setMinWidth(100);
      var2.setPreferredWidth(100);
      var2 = var1.getColumn(10);
      var2.setMinWidth(140);
      var2.setPreferredWidth(130);
      var2 = var1.getColumn(11);
      var2.setMinWidth(150);
      var2.setPreferredWidth(150);
      var2 = var1.getColumn(12);
      var2.setMinWidth(70);
      var2.setPreferredWidth(70);
      var2 = var1.getColumn(13);
      var2.setMinWidth(50);
      var2.setPreferredWidth(200);
   }

   private void setFilterComponentsVisible(boolean var1) {
      this.filter_business.setVisible(var1);
   }

   private void addFilterComponentsToVisibilityControl(boolean var1) {
      this.setFilterVisibility(var1);
   }

   private void addFileTypeFilterComponentsToVisibilitiControll(boolean var1) {
      this.filter_business.setFileFilterTypeVisibility(var1);
   }

   private void addFileListPanelToVisibilitiControl(boolean var1) {
      this.file_list_panel.setVisible(var1);
   }

   private void addInstalledFormsFilterComponentsToVisibilityControl(boolean var1) {
   }

   private void setButtonsPanelVisibility(boolean var1) {
      this.buttons_panel.setVisible(var1);
   }

   private void changeFileFilter() {
      this.filter_business.setExclusiveFilterSelection(true);
      this.lbl_filter_title.setText("Mentendő állomány típusa");
      this.lbl_file_filters.setText("Állomány Típusok");
      this.scp_file_filters.setPreferredSize((Dimension)null);
      this.scp_file_filters.revalidate();
      this.lst_file_filters.repaint();
      this.scp_file_filters.repaint();
   }

   private String getString(Object var1) {
      return var1 == null ? "" : var1.toString();
   }

   public void setButtonExecutor(FileBusiness.ButtonExecutor var1) {
      this.button_executor = var1;
   }

   private void showMessage(String var1) {
   }

   public boolean getFilterVisibility() {
      return this.filter_business.getFilterVisibility();
   }

   public void setFilterVisibility(boolean var1) {
      this.filter_business.setFilterVisibility(var1);
   }

   public void setFilesTitle(String var1) {
   }

   public void setFilesTitleLocked(boolean var1) {
   }

   public void setArchivPanel(boolean var1) {
      this.archivPanel = var1;
   }

   public boolean isArchivPanel() {
      return this.archivPanel;
   }

   public void loadFilterSettings(String var1) {
      this.filter_panel.getBusinessHandler().loadLastFilterValues(var1);
   }

   public void saveFilterSettings(String var1) {
      this.filter_panel.getBusinessHandler().saveLastFilterValues(var1);
   }

   private class VisibilityController implements ComponentListener {
      private final Hashtable visibilities;
      private boolean is_visible;
      private boolean is_visible_all;

      private VisibilityController() {
         this.visibilities = new Hashtable(32);
      }

      public void setComponentVisibility(JComponent var1, boolean var2) {
         if (var1 != null) {
            this.visibilities.put(var1, var2 ? Boolean.TRUE : Boolean.FALSE);
         }

      }

      public boolean getComponentVisibility(JComponent var1) {
         if (var1 != null) {
            Object var2 = this.visibilities.get(var1);
            if (var2 instanceof Boolean) {
               return (Boolean)var2;
            }
         }

         return false;
      }

      public void setVisible(boolean var1) {
         this.setVisible(var1, false);
      }

      public void setVisibleAll(boolean var1) {
         this.setVisible(var1, true);
      }

      private void setVisible(boolean var1, boolean var2) {
         this.is_visible = var1;
         this.is_visible_all = var2;
         this.setVisibile();
      }

      public void setVisibile() {
         Iterator var2 = this.visibilities.keySet().iterator();

         while(true) {
            JComponent var1;
            do {
               if (!var2.hasNext()) {
                  this.is_visible_all = false;
                  return;
               }

               var1 = (JComponent)var2.next();
            } while(!this.is_visible_all && !this.getComponentVisibility(var1));

            var1.setVisible(this.is_visible);
         }
      }

      public boolean isVisible() {
         return this.is_visible;
      }

      public void componentHidden(ComponentEvent var1) {
      }

      public void componentMoved(ComponentEvent var1) {
      }

      public void componentResized(ComponentEvent var1) {
         this.setVisibile();
      }

      public void componentShown(ComponentEvent var1) {
      }

      // $FF: synthetic method
      VisibilityController(Object var2) {
         this();
      }
   }

   public abstract static class ButtonExecutor {
      private FilePanel file_panel;

      public ButtonExecutor(FilePanel var1) {
         this.file_panel = var1;
      }

      public void b11Clicked() {
      }

      public void b12Clicked() {
      }

      public void b13Clicked() {
      }

      public void b14Clicked() {
      }

      public void b15Clicked() {
      }

      public void b21Clicked() {
      }

      public void b22PathClicked() {
      }

      public void b31Clicked() {
      }

      public void b32Clicked() {
      }
   }
}
