package hu.piller.enykp.alogic.filepanels.filepanel;

import hu.piller.enykp.alogic.filepanels.attachement.AttachementTool;
import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.IFilterPanelBusiness;
import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.IFilterPanelLogic;
import hu.piller.enykp.alogic.filepanels.filepanel.filterpanel.IFilteredFilesRefresh;
import hu.piller.enykp.alogic.filepanels.tablesorter.TableSorter;
import hu.piller.enykp.alogic.fileutil.FileStatusChecker;
import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.orghandler.OrgHandler;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.interfaces.IFileChooser;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.eventsupport.DefaultEventSupport;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import hu.piller.enykp.util.filelist.EnykFileList;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.net.URI;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
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
   public static final int TYPE_UNKNOWN = 0;
   public static final int TYPE_SINGLE = 1;
   public static final int TYPE_MULTI = 2;
   public static final String[] ID_COLUMN_NAMES = new String[]{"id", "form_name", "tax_number", "name", "from_date", "to_date", "state", "information", "tax_id", "save_date", "note", "version", "ver_desc", "org", "file", "category", "templatever", "attachment", "avdh_cst"};
   private final DefaultEventSupport des = new DefaultEventSupport();
   private static final String SABLONOK = "Sablonok ";
   private static final String SABLONOK2 = "Nyomtatványok archiválása";
   private EnykFileList enykFileList = EnykFileList.getInstance();
   private TemplateChecker enykFileFilters = null;
   boolean debugOn = false;
   public static final String STATE_ALL = "(Minden állapot)";
   public static final String STATE_EDITABLE = "Módosítható";
   public static final String STATE_SIGNED_FOR_SEND = "Küldésre megjelölt";
   public static final String STATE_SENT = "Elküldött";
   public static final String STATE_UNKNOWN = "(Ismeretlen)";
   private FilePanel file_panel;
   private File current_path;
   private boolean needTemplateFilter = false;
   private boolean is_file_title_locked;
   private boolean is_selected_path_locked;
   JLabel lbl_files_title;
   private JList lst_file_filters;
   private FileTable tbl_files;
   private JPanel buttons_panel;
   private JPanel file_list_panel;
   private JLabel lbl_filter_title;
   private JLabel lbl_file_filters;
   private JScrollPane scp_file_filters;
   private JLabel lbl_toggle_filters;
   private JButton btn_11;
   private JButton btn_12;
   private JButton btn_13;
   private JButton btn_14;
   private JButton btn_15;
   private JButton btn_21;
   private JButton btn_22;
   private JButton btn_22_1;
   private JButton btn_22_2;
   private JButton btn_23;
   private JButton btn_31;
   private JButton btn_32;
   private JTextField txt_path;
   private JScrollPane scp_path;
   private Component comp_bsep;
   protected IFilterPanelLogic filter_panel;
   protected IFilterPanelBusiness filter_business;
   private final TableSorter table_sorter = new TableSorter();
   private FileBusiness.ButtonExecutor button_executor = null;
   private final Vector vct_files = new Vector(256, 256);
   private Vector vct_filtered_files = new Vector(256, 256);
   private Object[] newDlgFilterColumns = new Object[]{new Integer(1), new Integer(7), new Integer(11), new Integer(12), new Integer(13), new Integer(14), new Integer(15)};
   public static Object[] defaultFileColumns = new Object[]{"id", "Nyomtatvány neve", "Adószám", "Név", "Dátumtól", "Dátumig", "Státusz", "Információ", "Adóazonosító", "Mentve", "Megjegyzés", "Verzió", "Verzióváltás oka", "Szervezet", "Állomány", "Kategória", "Nyomtatvány verzió", "Csat.db.", "avdh_cst"};
   private Object[] filterColumns;
   private Integer filterMaxRowCount;
   private int task_id;
   private final FileBusiness.VisibilityController visibility_controller = new FileBusiness.VisibilityController();
   private FileTable work_table;
   Object[] fileInfos = new Object[]{"filelist", null, null};
   public static final int TASK_NEW_DLG = 1;
   public static final int TASK_OPEN_DLG = 2;
   public static final int TASK_SAVE_DLG = 3;
   public static final int TASK_FORMDATALIST_DLG = 4;
   public static final int TASK_DATAFILEOPERATIONS_DLG = 5;
   public static final int TASK_INSTALLEDFORMS_DLG = 6;
   public static final int TASK_RENAMEDATAFILES_DLG = 7;
   public static final int TASK_OPEN_2_DLG = 8;
   public static final int TASK_OPEN_MULTI_DLG = 9;
   public static final int TASK_COPYFROMIMPEXPFOLDER_DLG = 10;
   public static final int TASK_COPYFROMSAVEFOLDER_DLG = 11;
   public static final int TASK_COPYTOIMPEXPFOLDER_DLG = 12;
   public static final int TASK_COPYTOSAVEFOLDER_DLG = 13;
   public static final int TASK_XML_CHECK_DLG = 14;
   public static final int TASK_FORMARCHIVER_DLG = 15;
   private Hashtable<Integer, String> rowMatch;

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

   public void selectAllKeyOnFiles() {
      this.des.fireEvent(this, "update", "select_all_key_on_file");
   }

   public void fireDoubleClickOnFiles() {
      this.des.fireEvent(this, "update", "double_click_on_file");
   }

   public void setEnykFileFilters(TemplateChecker var1) {
      this.enykFileFilters = var1;
   }

   public TemplateChecker getEnykFileFilters() {
      return this.enykFileFilters;
   }

   public int getTask_id() {
      return this.task_id;
   }

   public FileBusiness(FilePanel var1) {
      this.file_panel = var1;
      this.prepare();
   }

   private void prepare() {
      this.lst_file_filters = (JList)this.file_panel.getFPComponent("file_filter");
      this.tbl_files = (FileTable)this.file_panel.getFPComponent("files");
      this.lbl_files_title = (JLabel)this.file_panel.getFPComponent("files_title_lbl");
      this.lbl_filter_title = (JLabel)this.file_panel.getFPComponent("filter_title_lbl");
      this.lbl_file_filters = (JLabel)this.file_panel.getFPComponent("file_filters_lbl");
      this.scp_file_filters = (JScrollPane)this.file_panel.getFPComponent("file_filters_scp");
      this.lbl_toggle_filters = (JLabel)this.file_panel.getFPComponent("file_filters_toggle_btn");
      this.buttons_panel = (JPanel)this.file_panel.getFPComponent("buttons_panel");
      this.file_list_panel = (JPanel)this.file_panel.getFPComponent("file_list_panel");
      this.btn_11 = (JButton)this.file_panel.getFPComponent("select_all");
      this.btn_12 = (JButton)this.file_panel.getFPComponent("deselect_all");
      this.btn_13 = (JButton)this.file_panel.getFPComponent("replica");
      this.btn_14 = (JButton)this.file_panel.getFPComponent("rename");
      this.btn_15 = (JButton)this.file_panel.getFPComponent("delete");
      this.btn_21 = (JButton)this.file_panel.getFPComponent("copy");
      this.btn_22 = (JButton)this.file_panel.getFPComponent("choose_path");
      this.btn_22_1 = (JButton)this.file_panel.getFPComponent("22_1");
      this.btn_22_2 = (JButton)this.file_panel.getFPComponent("22_2");
      this.btn_23 = (JButton)this.file_panel.getFPComponent("b23");
      this.txt_path = (JTextField)this.file_panel.getFPComponent("path_txt");
      this.scp_path = (JScrollPane)this.file_panel.getFPComponent("path_csp");
      this.btn_31 = (JButton)this.file_panel.getFPComponent("cancel");
      this.btn_32 = (JButton)this.file_panel.getFPComponent("ok");
      this.comp_bsep = this.file_panel.getFPComponent("btn_line_sep");
      this.filter_panel = (IFilterPanelLogic)this.file_panel.getFPComponent("file_filter_panel");
      this.filter_business = this.filter_panel.getBusinessHandler();
      this.filter_business.setFileBusiness(this);
      this.prepareFiles();
      this.work_table = new FileTable();
      this.work_table.init(this, 0, defaultFileColumns);
      this.prepareButtons();
      this.file_panel.addComponentListener(this.visibility_controller);
   }

   protected void prepareFiles() {
      this.tbl_files.init(this, 0, defaultFileColumns);
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
      this.btn_22_1.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (FileBusiness.this.button_executor != null) {
               FileBusiness.this.button_executor.b22_1PathClicked();
            }

         }
      });
      this.btn_22_2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (FileBusiness.this.button_executor != null) {
               FileBusiness.this.button_executor.b22_2PathClicked();
            }

         }
      });
      this.btn_23.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (FileBusiness.this.button_executor != null) {
               FileBusiness.this.button_executor.b23PathClicked();
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

   public void setNeedTemplateFilter(boolean var1) {
      this.needTemplateFilter = var1;
   }

   public void setSelectedPath(URI var1) {
      this.setSelectedPath(new File(var1.getPath()));
   }

   public void setSelectedPath(File var1) {
      this.setSelectedPath(var1, true);
   }

   public void setSelectedPath(URI var1, boolean var2) {
      if (!this.is_selected_path_locked) {
         this.setSelectedPath(new File(var1.getPath()), var2);
      }

   }

   public void setSelectedPath(File var1, boolean var2) {
      if (!this.is_selected_path_locked) {
         this.current_path = var1;
      }

      if (var2) {
         this.rescan();
      }

   }

   public void setSelectedPathLocked(boolean var1) {
      this.is_selected_path_locked = var1;
   }

   public File getSelectedPath() {
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

   public void refreshFileInfos() {
      this.showFileInfos();
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
      Object var2 = null;
      long var3 = System.currentTimeMillis();
      System.out.println(">Scanning directory: " + var1);
      long var5 = System.currentTimeMillis();
      this.vct_files.clear();
      String[] var7 = this.getSelectedFilters();
      Object[] var8 = this.enykFileList.list(var1.getPath(), var7);
      if (var8 != null && var8.length > 0 && this.needTemplateFilter) {
         var8 = (Object[])((Object[])this.enykFileFilters.getFilteredFiles(var8));
      }

      if (var8 != null && var8.length > 0) {
         for(int var9 = 0; var9 < var8.length; ++var9) {
            Object[] var10 = (Object[])((Object[])var8[var9]);
            this.vct_files.add(new ListItem(new File((String)var10[0]), (Icon)null, var10[1], var10[2]));
         }
      }

      long var11 = System.currentTimeMillis() - var5;
      this.showFileList(this.vct_files);
      if (this.debugOn) {
         System.out.println("FileBusiness time = " + (System.currentTimeMillis() - var3) + "  " + var1);
      }

   }

   public Object[] getFileTableRow(Object var1) {
      Object[] var2 = new Object[19];
      if (var1 instanceof ListItem) {
         ListItem var3 = (ListItem)var1;
         if (var3.getText() instanceof Hashtable) {
            Hashtable var4 = (Hashtable)var3.getText();
            Vector var5 = new Vector();
            if (var4.get("docs") instanceof Vector) {
               var5 = (Vector)var4.get("docs");
            }

            String var6 = this.getString(var4.get("type"));
            int var7 = 0;
            if ("single".equalsIgnoreCase(var6)) {
               var7 = 1;
            } else if ("multi".equalsIgnoreCase(var6)) {
               var7 = 2;
            }

            if (var7 == 0) {
               var7 = var5.size();
            }

            var2[0] = var3;
            var2[14] = ((File)var3.getItem()).getName();
            var2[1] = "";
            var2[2] = "";
            var2[3] = "";
            var2[4] = "";
            var2[5] = "";
            var2[7] = "";
            var2[8] = "";
            var2[9] = this.formatDate(var4.get("saved"));
            var2[10] = "";
            var2[11] = "";
            var2[6] = "";
            var2[13] = "";
            var2[16] = "";
            var2[12] = "";
            var2[17] = "";
            var2[18] = "";
            var2[15] = "";
            Hashtable var8;
            File var9;
            String var10;
            switch(var7) {
            case 0:
               var2[7] = "(Ismeretlen dokumentum)";
               break;
            case 1:
               var1 = var4.get("docinfo");
               if (var1 instanceof Hashtable) {
                  var8 = (Hashtable)var1;

                  try {
                     var9 = (File)var3.getItem();
                     if (var9.getAbsolutePath().endsWith(".tem.enyk")) {
                        var10 = OrgHandler.getInstance().getReDirectedOrgId((String)var8.get("org"));
                        var8.put("org", var10);
                     }
                  } catch (Exception var12) {
                  }

                  var2[1] = this.getString(var8.get("name"));
                  var2[2] = this.getString(var8.get("tax_number"));
                  var2[8] = this.getString(var8.get("account_name"));
                  var2[3] = this.getString(var8.get("person_name"));
                  var2[4] = this.formatDate(var8.get("from_date"));
                  var2[5] = this.formatDate(var8.get("to_date"));
                  var2[7] = this.getString(var8.get("info"));
                  var2[10] = this.getString(var8.get("note"));
                  var2[11] = this.getString(var8.get("ver"));
                  var2[13] = OrgInfo.getInstance().getOrgShortnameByOrgID(this.getString(var8.get("org")));
                  var2[16] = this.getString(var8.get("templatever"));
                  var2[12] = this.getString(var8.get("version_desc"));
                  var2[15] = this.getString(var8.get("category"));
                  var2[6] = this.getFileState((File)var3.getItem(), this.getString(var8.get("krfilename")));
                  var2[17] = this.getString(var8.get("attachment_count"));
                  var2[18] = this.getString(var8.get("avdh_cst"));
               } else {
                  var2[1] = "Egyszerű dokumentum";
                  var2[7] = "(Nincs információ)";
               }
               break;
            default:
               var1 = var4.get("docinfo");
               if (var1 instanceof Hashtable) {
                  var8 = (Hashtable)var1;

                  try {
                     var9 = (File)var3.getItem();
                     if (var9.getAbsolutePath().endsWith(".tem.enyk")) {
                        var10 = OrgHandler.getInstance().getReDirectedOrgId((String)var8.get("org"));
                        var8.put("org", var10);
                     }
                  } catch (Exception var11) {
                  }

                  var2[1] = this.getString(var8.get("name"));
                  var2[7] = this.getString(var8.get("info"));
                  var2[10] = this.getString(var8.get("note"));
                  var2[11] = this.getString(var8.get("ver"));
                  var2[4] = this.formatDate(var8.get("from_date"));
                  var2[5] = this.formatDate(var8.get("to_date"));
                  var2[3] = this.getString(var8.get("person_name"));
                  var2[2] = this.getString(var8.get("tax_number"));
                  var2[8] = this.getString(var8.get("account_name"));
                  var2[13] = OrgInfo.getInstance().getOrgShortnameByOrgID(this.getString(var8.get("org")));
                  var2[16] = this.getString(var8.get("templatever"));
                  var2[12] = this.getString(var8.get("version_desc"));
                  var2[15] = this.getString(var8.get("category"));
                  var2[6] = this.getFileState((File)var3.getItem(), this.getString(var8.get("krfilename")));
                  var2[17] = this.getString(var8.get("attachment_count"));
               } else {
                  var2[1] = "Multi dokumentum";
                  var2[7] = "(Nincs információ)";
               }
            }
         }
      }

      return this.matchRow(var2);
   }

   private Hashtable getSelectedHeadData(int var1) {
      int[] var4 = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
      DefaultTableModel var3 = (DefaultTableModel)this.tbl_files.getModel();
      Hashtable var2 = new Hashtable(var3.getColumnCount());

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var2.put(ID_COLUMN_NAMES[var4[var5]], var3.getValueAt(var1, var4[var5]));
      }

      return var2;
   }

   public String getFileState(File var1, String var2) {
      return var1.getName().toLowerCase().endsWith(".frm.enyk") ? this.filter_business.getFileState(var1, var2) : "";
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
      byte var3 = 1;
      byte var4 = 1;
      var5.setSortEnabled(false);
      TableSorter.lock = true;
      DefaultTableModel var2 = (DefaultTableModel)this.tbl_files.getModel();
      var2.getDataVector().clear();
      if (!"Sablonok ".equals(this.lbl_files_title.getText()) && !"Nyomtatványok archiválása".equals(this.lbl_files_title.getText())) {
         FileStatusChecker.getInstance().startBatchMode();

         try {
            AttachementTool.fillFileList();
            int var6 = 0;

            for(int var7 = var1.size(); var6 < var7; ++var6) {
               Object[] var8 = new Object[0];

               try {
                  var8 = this.getFileTableRow(var1.get(var6));
               } catch (Exception var13) {
                  ErrorList.getInstance().writeError(1, "FileBusiness hiba", var13, (Object)null);
               }

               if (var8 != null) {
                  var2.addRow(var8);
               }
            }
         } finally {
            AttachementTool.dropFileList();
         }
      }

      TableSorter.lock = false;
      var5.setSortEnabled(true);
      var5.sort(var4, var3);
      this.tbl_files.revalidate();
      this.tbl_files.repaint();
      this.refreshFilterSource(var1);
      if (!"Sablonok ".equals(this.lbl_files_title.getText()) && !"Nyomtatványok archiválása".equals(this.lbl_files_title.getText())) {
         FileStatusChecker.getInstance().stopBatchMode();
      }

   }

   private void refreshFilterSource(Vector var1) {
      DefaultTableModel var2 = (DefaultTableModel)this.work_table.getModel();
      var2.getDataVector().clear();
      int var3 = 0;

      for(int var4 = var1.size(); var3 < var4; ++var3) {
         Object[] var5 = new Object[0];

         try {
            var5 = this.getFileTableRow(var1.get(var3));
         } catch (Exception var7) {
            ErrorList.getInstance().writeError(1, "FileBusiness hiba (2)", var7, (Object)null);
         }

         if (var5 != null) {
            var2.addRow(var5);
         }
      }

      this.filter_business.refresh(var2);
   }

   public void showFileList(DefaultTableModel var1) {
      TableSorter var5 = this.tbl_files.getTable_sorter();
      int var3 = var5.getSortOrder();
      int var4 = var5.getSortedColumn();
      var5.setSortEnabled(false);
      TableSorter.lock = true;
      DefaultTableModel var2 = (DefaultTableModel)this.tbl_files.getModel();
      var2.getDataVector().clear();
      if (!"Sablonok ".equals(this.lbl_files_title.getText()) && !"Nyomtatványok archiválása".equals(this.lbl_files_title.getText())) {
         FileStatusChecker.getInstance().startBatchMode();
      }

      int var6 = 0;

      for(int var7 = var1.getDataVector().size(); var6 < var7; ++var6) {
         Object[] var8 = new Object[0];

         try {
            var8 = this.getFileTableRow(((Vector)((Vector)var1.getDataVector().get(var6))).get(0));
         } catch (Exception var10) {
            ErrorList.getInstance().writeError(1, "FileBusiness hiba (3)", var10, (Object)null);
         }

         if (var8 != null) {
            var2.addRow(var8);
         }
      }

      if (!"Sablonok ".equals(this.lbl_files_title.getText()) && !"Nyomtatványok archiválása".equals(this.lbl_files_title.getText())) {
         FileStatusChecker.getInstance().stopBatchMode();
      }

      TableSorter.lock = false;
      var5.setSortEnabled(true);
      var5.sort(var4, var3);
      this.tbl_files.revalidate();
      this.tbl_files.repaint();
   }

   private void filterFileList() {
      this.vct_filtered_files = this.filter_business.filterFileList(this.vct_files);
   }

   public void setSelectedFiles(File[] var1) {
      if (var1 instanceof File[]) {
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
               --var9;
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
      } else {
         this.tbl_files.getSelectionModel().clearSelection();
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
               Object[] var8 = new Object[4];
               if (var9 != null) {
                  var8[0] = var9.getItem();
                  var8[1] = var9.getSecondItem();
                  var8[2] = this.getSelectedHeadData(var1[var5]);
                  var8[3] = var9.getText();
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
      this.setTask(var1, (Hashtable)null);
   }

   public void setTask(int var1, Hashtable var2) {
      this.tbl_files.setSelectionMode(0);
      Vector var5 = new Vector();
      TableColumnModel var3;
      TableColumn var4;
      switch(var1) {
      case 1:
         this.task_id = var1;
         this.sizeTableColumns();
         var3 = this.tbl_files.getColumnModel();
         var4 = var3.getColumn(4);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(5);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(3);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(10);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(8);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(9);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(6);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(2);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(16);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(17);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         this.lbl_files_title.setText("Nyomtatványok");
         this.addFilterComponentsToVisibilityControl(true);
         this.addFileTypeFilterComponentsToVisibilitiControll(false);
         this.setFilterComponentsVisible(false);
         this.setButtonsPanelVisibility(false);
         this.visibility_controller.setVisibleAll(false);
         this.filterColumns = this.newDlgFilterColumns;
         this.filterMaxRowCount = new Integer(7);
         var5.add(new Integer(0));
         var5.add(new Integer(1));
         var5.add(new Integer(0));
         var5.add(new Integer(1));
         break;
      case 2:
         this.task_id = var1;
         this.sizeTableColumns();
         this.addFilterComponentsToVisibilityControl(true);
         this.addFileTypeFilterComponentsToVisibilitiControll(true);
         this.setFilterComponentsVisible(false);
         this.visibility_controller.setVisibleAll(false);
         this.setFilesTitle("ENYK adat állományai");
         this.btn_13.setVisible(false);
         this.btn_14.setVisible(false);
         this.btn_15.setVisible(false);
         this.btn_21.setVisible(false);
         this.btn_22.setVisible(false);
         this.btn_23.setVisible(false);
         this.scp_path.setVisible(false);
         this.btn_31.setVisible(false);
         this.btn_32.setVisible(false);
         this.comp_bsep.setVisible(false);
         this.btn_11.setText("Másolat készítése");
         this.btn_12.setText("Megnyitás csak olvasásra");
         this.filterColumns = new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(8), new Integer(9), new Integer(10), new Integer(11), new Integer(13), new Integer(14), new Integer(15), new Integer(16)};
         this.filterMaxRowCount = new Integer(9);
         var4 = this.tbl_files.getColumnModel().getColumn(12);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         break;
      case 3:
         this.task_id = var1;
         this.changeFileFilter();
         this.setButtonsPanelVisibility(false);
         this.addFileListPanelToVisibilitiControl(false);
         this.visibility_controller.setVisibleAll(false);
         this.addFilterComponentsToVisibilityControl(false);
         this.addFileTypeFilterComponentsToVisibilitiControll(true);
         this.setFilterComponentsVisible(true);
         this.filterColumns = new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(8), new Integer(9), new Integer(10), new Integer(11), new Integer(13), new Integer(14), new Integer(15)};
         this.filterMaxRowCount = new Integer(9);
         var4 = this.tbl_files.getColumnModel().getColumn(12);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = this.tbl_files.getColumnModel().getColumn(17);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         break;
      case 4:
         this.task_id = var1;
         this.sizeTableColumns();
         this.addFilterComponentsToVisibilityControl(true);
         this.addFileTypeFilterComponentsToVisibilitiControll(false);
         this.setFilterComponentsVisible(false);
         this.visibility_controller.setVisibleAll(false);
         this.btn_14.setVisible(false);
         this.btn_15.setVisible(false);
         this.btn_21.setVisible(false);
         this.scp_path.setVisible(false);
         this.btn_22.setVisible(false);
         this.btn_23.setVisible(false);
         this.btn_31.setVisible(false);
         this.btn_11.setText("Lista szerkesztés");
         this.btn_12.setText("Nyomtatás");
         this.btn_13.setText("Mentés file-ba");
         this.btn_32.setText("Kilép");
         this.filterColumns = new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(8), new Integer(9), new Integer(10), new Integer(11), new Integer(13), new Integer(14), new Integer(15), new Integer(16)};
         this.filterMaxRowCount = new Integer(9);
         var4 = this.tbl_files.getColumnModel().getColumn(12);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         break;
      case 5:
         this.task_id = var1;
         this.sizeTableColumns();
         this.addFilterComponentsToVisibilityControl(true);
         this.addFileTypeFilterComponentsToVisibilitiControll(true);
         this.setFilterComponentsVisible(false);
         this.visibility_controller.setVisibleAll(false);
         this.btn_15.setVisible(false);
         this.btn_31.setVisible(false);
         this.btn_23.setVisible(false);
         this.btn_11.setText("Saját mappa listázása");
         this.btn_12.setText("Külső mappa listázása");
         this.btn_13.setText("Átnevezés");
         this.btn_14.setText("Törlés");
         this.btn_21.setText("Másolás");
         this.btn_22.setText("Külső mappa kijelölése");
         this.btn_32.setText("Vissza");
         this.filterColumns = new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(8), new Integer(9), new Integer(10), new Integer(11), new Integer(13), new Integer(14), new Integer(15)};
         this.filterMaxRowCount = new Integer(9);
         var4 = this.tbl_files.getColumnModel().getColumn(12);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = this.tbl_files.getColumnModel().getColumn(17);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         break;
      case 6:
         this.task_id = var1;
         this.sizeTableColumns();
         var3 = this.tbl_files.getColumnModel();
         var4 = var3.getColumn(4);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(5);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(3);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(10);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(8);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(9);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(6);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(2);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = this.tbl_files.getColumnModel().getColumn(17);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         this.addInstalledFormsFilterComponentsToVisibilityControl(true);
         this.addFilterComponentsToVisibilityControl(true);
         this.addFileTypeFilterComponentsToVisibilitiControll(false);
         this.setFilterComponentsVisible(false);
         this.visibility_controller.setVisibleAll(false);
         this.lbl_files_title.setText("Sablonok ");
         this.btn_11.setVisible(true);
         this.btn_12.setVisible(false);
         this.btn_13.setVisible(false);
         this.btn_14.setVisible(false);
         this.btn_15.setVisible(false);
         this.btn_21.setVisible(false);
         this.scp_path.setVisible(false);
         this.btn_22.setVisible(true);
         this.btn_23.setVisible(false);
         this.btn_23.setVisible(true);
         this.btn_31.setVisible(false);
         this.btn_11.setText("A kiválasztott nyomtatvány összes verziójának törlése az utolsó kivételével");
         this.btn_11.setToolTipText("A kiválasztott nyomtatvány összes verziójának törlése az utolsó kivételével");
         this.btn_22.setText("A kiválasztott nyomtatvány összes verziójának törlése");
         this.btn_22.setToolTipText("A kiválasztott nyomtatvány összes verziójának törlése");
         this.btn_23.setText("A kiválasztott nyomtatvány törlése");
         this.btn_23.setToolTipText("A kiválasztott nyomtatvány törlése");
         this.btn_32.setText("Ok");
         this.btn_32.setAlignmentX(0.5F);
         this.filterColumns = this.newDlgFilterColumns;
         this.filterMaxRowCount = new Integer(7);
         var5.add(new Integer(0));
         var5.add(new Integer(1));
         var5.add(new Integer(0));
         var5.add(new Integer(1));
         break;
      case 7:
         this.task_id = var1;
         this.sizeTableColumns();
         this.addFilterComponentsToVisibilityControl(true);
         this.addFileTypeFilterComponentsToVisibilitiControll(true);
         this.setFilterComponentsVisible(false);
         this.visibility_controller.setVisibleAll(false);
         this.btn_12.setVisible(false);
         this.btn_13.setVisible(false);
         this.btn_14.setVisible(false);
         this.btn_15.setVisible(false);
         this.btn_21.setVisible(false);
         this.scp_path.setVisible(false);
         this.btn_22.setVisible(false);
         this.btn_23.setVisible(false);
         this.btn_31.setVisible(false);
         this.btn_11.setText("Átnevezés");
         this.btn_32.setText("Mégsem");
         this.filterColumns = new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(8), new Integer(9), new Integer(10), new Integer(11), new Integer(13), new Integer(14), new Integer(15)};
         this.filterMaxRowCount = new Integer(9);
         var4 = this.tbl_files.getColumnModel().getColumn(12);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = this.tbl_files.getColumnModel().getColumn(17);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         break;
      case 8:
         this.task_id = var1;
         this.sizeTableColumns();
         this.addFilterComponentsToVisibilityControl(true);
         this.addFileTypeFilterComponentsToVisibilitiControll(true);
         this.setFilterComponentsVisible(false);
         this.visibility_controller.setVisibleAll(false);
         this.btn_13.setVisible(false);
         this.btn_14.setVisible(false);
         this.btn_15.setVisible(false);
         this.btn_21.setVisible(false);
         this.btn_31.setVisible(false);
         this.btn_32.setVisible(false);
         this.btn_11.setVisible(true);
         this.btn_12.setVisible(true);
         this.btn_22.setVisible(true);
         this.btn_23.setVisible(false);
         this.scp_path.setVisible(true);
         this.setFilesTitle("ENYK adat állományai");
         this.btn_11.setText("Saját mappa listázása");
         this.btn_12.setText("Külső mappa listázása");
         this.btn_22.setText("Külső mappa kijelölése");
         this.filterColumns = new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(8), new Integer(9), new Integer(10), new Integer(11), new Integer(13), new Integer(14), new Integer(15)};
         this.filterMaxRowCount = new Integer(9);
         var4 = this.tbl_files.getColumnModel().getColumn(12);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         break;
      case 9:
         this.task_id = var1;
         this.tbl_files.setSelectionMode(2);
         this.sizeTableColumns();
         this.addFilterComponentsToVisibilityControl(true);
         this.addFileTypeFilterComponentsToVisibilitiControll(true);
         this.setFilterComponentsVisible(false);
         this.visibility_controller.setVisibleAll(false);
         this.setFilesTitle("ENYK adat állományai");
         this.btn_12.setVisible(false);
         this.btn_13.setVisible(false);
         this.btn_14.setVisible(false);
         this.btn_15.setVisible(false);
         this.btn_21.setVisible(false);
         this.btn_22.setVisible(false);
         this.btn_23.setVisible(false);
         this.scp_path.setVisible(false);
         this.btn_31.setVisible(false);
         this.btn_32.setVisible(false);
         this.comp_bsep.setVisible(false);
         this.btn_11.setVisible(false);
         this.filterColumns = new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(8), new Integer(9), new Integer(10), new Integer(11), new Integer(13), new Integer(14), new Integer(15), new Integer(16)};
         this.filterMaxRowCount = new Integer(9);
         var4 = this.tbl_files.getColumnModel().getColumn(12);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = this.tbl_files.getColumnModel().getColumn(17);
         var4.setMaxWidth(50);
         var4.setMinWidth(50);
         var4.setPreferredWidth(50);
         var4.setResizable(true);
         break;
      case 10:
         this.task_id = var1;
         this.sizeTableColumns();
         this.addFilterComponentsToVisibilityControl(true);
         this.addFileTypeFilterComponentsToVisibilitiControll(true);
         this.setFilterComponentsVisible(false);
         this.visibility_controller.setVisibleAll(false);
         this.btn_11.setVisible(false);
         this.btn_12.setVisible(false);
         this.btn_15.setVisible(false);
         this.btn_31.setVisible(false);
         this.btn_23.setVisible(false);
         this.btn_13.setText("Átnevezés");
         this.btn_14.setText("Törlés");
         this.btn_21.setText("Másolás");
         this.btn_22.setText("Cél könyvtár kiválasztása");
         this.btn_32.setText("Vissza");
         this.filterColumns = new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(8), new Integer(9), new Integer(10), new Integer(11), new Integer(13), new Integer(14), new Integer(15)};
         this.filterMaxRowCount = new Integer(8);
         var4 = this.tbl_files.getColumnModel().getColumn(12);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = this.tbl_files.getColumnModel().getColumn(17);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         break;
      case 11:
         this.task_id = var1;
         this.sizeTableColumns();
         this.addFilterComponentsToVisibilityControl(true);
         this.addFileTypeFilterComponentsToVisibilitiControll(true);
         this.setFilterComponentsVisible(false);
         this.visibility_controller.setVisibleAll(false);
         this.btn_11.setVisible(false);
         this.btn_12.setVisible(false);
         this.btn_15.setVisible(false);
         this.btn_31.setVisible(false);
         this.btn_23.setVisible(false);
         this.btn_13.setText("Átnevezés");
         this.btn_14.setText("Törlés");
         this.btn_21.setText("Másolás");
         this.btn_22.setText("Cél könyvtár kiválasztása");
         this.btn_32.setText("Vissza");
         var4 = this.tbl_files.getColumnModel().getColumn(12);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         this.filterColumns = new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(8), new Integer(9), new Integer(10), new Integer(11), new Integer(13), new Integer(14), new Integer(15), new Integer(16)};
         this.filterMaxRowCount = new Integer(9);
         break;
      case 12:
         this.task_id = var1;
         this.sizeTableColumns();
         this.addFilterComponentsToVisibilityControl(true);
         this.addFileTypeFilterComponentsToVisibilitiControll(true);
         this.setFilterComponentsVisible(false);
         this.visibility_controller.setVisibleAll(false);
         this.btn_11.setVisible(false);
         this.btn_12.setVisible(false);
         this.btn_15.setVisible(false);
         this.btn_31.setVisible(false);
         this.btn_13.setText("Átnevezés");
         this.btn_14.setText("Törlés");
         this.btn_21.setText("Másolás");
         this.btn_22.setText("Forrás könyvtár kiválasztása");
         this.btn_23.setVisible(false);
         this.btn_32.setText("Vissza");
         var4 = this.tbl_files.getColumnModel().getColumn(12);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = this.tbl_files.getColumnModel().getColumn(17);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         this.filterColumns = new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(8), new Integer(9), new Integer(10), new Integer(11), new Integer(13), new Integer(14), new Integer(15)};
         this.filterMaxRowCount = new Integer(9);
         break;
      case 13:
         this.task_id = var1;
         this.sizeTableColumns();
         this.addFilterComponentsToVisibilityControl(true);
         this.addFileTypeFilterComponentsToVisibilitiControll(true);
         this.setFilterComponentsVisible(false);
         this.visibility_controller.setVisibleAll(false);
         this.btn_11.setVisible(false);
         this.btn_12.setVisible(false);
         this.btn_15.setVisible(false);
         this.btn_31.setVisible(false);
         this.btn_13.setText("Átnevezés");
         this.btn_14.setText("Törlés");
         this.btn_21.setText("Másolás");
         this.btn_22.setText("Forrás könyvtár kiválasztása");
         this.btn_23.setVisible(false);
         this.btn_32.setText("Vissza");
         this.filterColumns = new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(8), new Integer(9), new Integer(10), new Integer(11), new Integer(13), new Integer(14), new Integer(15), new Integer(16)};
         this.filterMaxRowCount = new Integer(9);
         var4 = this.tbl_files.getColumnModel().getColumn(12);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         break;
      case 14:
         this.task_id = var1;
         this.sizeTableColumns();
         this.addFilterComponentsToVisibilityControl(true);
         this.addFileTypeFilterComponentsToVisibilitiControll(true);
         this.setFilterComponentsVisible(false);
         this.visibility_controller.setVisibleAll(false);
         this.setFilesTitle("ENYK adat állományai");
         this.btn_12.setVisible(false);
         this.btn_13.setVisible(false);
         this.btn_14.setVisible(false);
         this.btn_15.setVisible(false);
         this.btn_21.setVisible(false);
         this.btn_22.setVisible(false);
         this.btn_23.setVisible(false);
         this.scp_path.setVisible(false);
         this.btn_31.setVisible(false);
         this.btn_32.setVisible(false);
         this.comp_bsep.setVisible(false);
         this.btn_11.setVisible(false);
         this.filterColumns = new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(8), new Integer(9), new Integer(10), new Integer(11), new Integer(13), new Integer(14), new Integer(15)};
         this.filterMaxRowCount = new Integer(9);
         var4 = this.tbl_files.getColumnModel().getColumn(12);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = this.tbl_files.getColumnModel().getColumn(17);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         break;
      case 15:
         this.task_id = var1;
         this.sizeTableColumns();
         var3 = this.tbl_files.getColumnModel();
         var4 = var3.getColumn(4);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(5);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(3);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(10);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(8);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(9);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(6);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = var3.getColumn(2);
         var4.setMinWidth(0);
         var4.setMaxWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         var4 = this.tbl_files.getColumnModel().getColumn(17);
         var4.setMaxWidth(0);
         var4.setMinWidth(0);
         var4.setPreferredWidth(0);
         var4.setResizable(false);
         this.addInstalledFormsFilterComponentsToVisibilityControl(true);
         this.addFilterComponentsToVisibilityControl(true);
         this.addFileTypeFilterComponentsToVisibilitiControll(false);
         this.setFilterComponentsVisible(false);
         this.visibility_controller.setVisibleAll(false);
         this.lbl_files_title.setText("Nyomtatványok archiválása");
         this.btn_11.setVisible(false);
         this.btn_12.setVisible(false);
         this.btn_13.setVisible(false);
         this.btn_14.setVisible(false);
         this.btn_15.setVisible(false);
         this.btn_21.setVisible(false);
         this.scp_path.setVisible(false);
         this.btn_22.setVisible(false);
         this.btn_22_1.setVisible(false);
         this.btn_22_2.setVisible(false);
         this.btn_23.setVisible(false);
         this.btn_31.setVisible(false);
         this.btn_32.setVisible(false);
         this.btn_11.setText("A kiválasztott nyomtatvány verzióinak archiválása a legutolsó kivételével");
         this.btn_23.setText("A kiválasztott nyomtatvány összes verziójának archiválása");
         this.btn_22_1.setText("Az összes nyomtatvány verzióinak archiválása a legutolsó kivételével");
         this.btn_22_2.setText("Az összes nyomtatvány összes verziójának archiválása");
         this.btn_22.setText("A kiválasztott nyomtatvány archiválása");
         this.btn_32.setText("Ok");
         this.btn_32.setAlignmentX(0.5F);
         this.filterColumns = this.newDlgFilterColumns;
         this.filterMaxRowCount = new Integer(7);
         var5.add(new Integer(0));
         var5.add(new Integer(1));
         var5.add(new Integer(0));
         var5.add(new Integer(1));
      }

      this.setRowMatch(var2);
      this.filter_business.initials(new Object[]{this.tbl_files, this.getFilterColumns(), this.filterMaxRowCount, var5});
      this.file_panel.revalidate();
      this.file_panel.repaint();
   }

   public void setRowMatch(Hashtable<Integer, String> var1) {
      this.rowMatch = var1;
   }

   private Object[] matchRow(Object[] var1) {
      if (this.rowMatch != null) {
         Enumeration var2 = this.rowMatch.keys();

         while(var2.hasMoreElements()) {
            Integer var3 = (Integer)var2.nextElement();
            if (var1[var3] != null && !var1[var3].toString().matches((String)this.rowMatch.get(var3))) {
               return null;
            }
         }
      }

      return var1;
   }

   private Vector getFilterColumns() {
      Vector var1 = new Vector();

      for(int var2 = 0; var2 < this.filterColumns.length; ++var2) {
         var1.add(this.filterColumns[var2]);
      }

      return var1;
   }

   public void setDefaultFilterValues(int var1, String var2) {
      this.filter_business.setDefaultFilterValues(var1, var2);
   }

   private void _sizeTableColumns() {
      TableColumnModel var1 = this.tbl_files.getColumnModel();
      TableColumn var2 = var1.getColumn(1);
      var2.setMinWidth(170);
      var2.setPreferredWidth(170);
      var2 = var1.getColumn(2);
      var2.setMinWidth(100);
      var2.setPreferredWidth(100);
      var2 = var1.getColumn(3);
      var2.setMinWidth(150);
      var2.setPreferredWidth(150);
      var2 = var1.getColumn(4);
      var2.setMinWidth(70);
      var2.setPreferredWidth(70);
      var2 = var1.getColumn(5);
      var2.setMinWidth(70);
      var2.setPreferredWidth(70);
      var2 = var1.getColumn(11);
      var2.setMinWidth(50);
      var2.setPreferredWidth(50);
      var2 = var1.getColumn(7);
      var2.setMinWidth(200);
      var2.setPreferredWidth(300);
      var2.setMaxWidth(800);
      var2 = var1.getColumn(12);
      var2.setMinWidth(200);
      var2.setPreferredWidth(200);
      var2.setMaxWidth(300);
      var2 = var1.getColumn(8);
      var2.setMinWidth(100);
      var2.setPreferredWidth(100);
      var2 = var1.getColumn(9);
      var2.setMinWidth(140);
      var2.setPreferredWidth(130);
      var2 = var1.getColumn(10);
      var2.setMinWidth(150);
      var2.setPreferredWidth(150);
      var2 = var1.getColumn(6);
      var2.setMinWidth(70);
      var2.setPreferredWidth(70);
      var2 = var1.getColumn(14);
      var2.setMinWidth(50);
      var2.setPreferredWidth(500);
      var2 = var1.getColumn(15);
      var2.setMinWidth(50);
      var2.setPreferredWidth(80);
      var2 = var1.getColumn(16);
      var2.setMaxWidth(150);
      var2.setMinWidth(70);
      var2.setPreferredWidth(150);
      var2 = var1.getColumn(17);
      var2.setMaxWidth(100);
      var2.setMinWidth(70);
      var2.setPreferredWidth(100);
   }

   private void sizeTableColumns() {
      GuiUtil.setTableColWidth(this.tbl_files);
   }

   public boolean getFilterVisibility() {
      return this.filter_business.getFilterVisibility();
   }

   public void setFilterVisibility(boolean var1) {
      this.filter_business.setFilterVisibility(var1);
   }

   public void setFilterComponentsVisible(boolean var1) {
      this.filter_business.setVisible(var1);
   }

   public boolean getFilterComponentsVisible() {
      return this.filter_business.getVisible();
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
      this.scp_file_filters.repaint();
   }

   private String getString(Object var1) {
      return var1 == null ? "" : var1.toString();
   }

   private String getNumberOfAttachments(File var1) {
      Map var2 = AttachementTool.getAttachementList(new String[]{var1.getName()});
      Map var3 = (Map)var2.get(var1.getName());
      int var4 = 0;

      Entry var6;
      for(Iterator var5 = var3.entrySet().iterator(); var5.hasNext(); var4 += ((Vector)var6.getValue()).size()) {
         var6 = (Entry)var5.next();
      }

      return Integer.toString(var4);
   }

   public void setButtonExecutor(FileBusiness.ButtonExecutor var1) {
      this.button_executor = var1;
   }

   public void setVct_filtered_files(Vector var1) {
      this.vct_filtered_files = var1;
   }

   public Vector getVct_files() {
      return this.vct_files;
   }

   public void setFilesTitle(String var1) {
      if (!this.is_file_title_locked) {
         this.lbl_files_title.setText(var1);
         this.file_panel.repaint();
      }

   }

   public void setFilesTitleLocked(boolean var1) {
      this.is_file_title_locked = var1;
   }

   public Hashtable showDialog(String[] var1) {
      return null;
   }

   public void loadFilterSettings(String var1) {
      this.filter_panel.getBusinessHandler().loadLastFilterValues(var1);
   }

   public void saveFilterSettings(String var1) {
      this.filter_panel.getBusinessHandler().saveLastFilterValues(var1);
   }

   private int getWidth(JPanel var1) {
      int var2 = 0;
      Component[] var3 = var1.getComponents();
      Component[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Component var7 = var4[var6];
         if (var7 instanceof JButton && ((JButton)var7).isVisible()) {
            var2 += GuiUtil.getW((JButton)var7, ((JButton)var7).getText());
         }
      }

      return var2;
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
      protected FilePanel file_panel;

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

      public void b22_1PathClicked() {
      }

      public void b22_2PathClicked() {
      }

      public void b23PathClicked() {
      }

      public void b31Clicked() {
      }

      public void b32Clicked() {
      }
   }
}
