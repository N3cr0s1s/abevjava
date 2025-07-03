package hu.piller.enykp.gui;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.checkpanel.ErrorListDialog;
import hu.piller.enykp.alogic.checkpanel.MultiErrorListDialog;
import hu.piller.enykp.alogic.fileloader.dat.DatLoader;
import hu.piller.enykp.alogic.fileloader.docinfo.DocInfoLoader;
import hu.piller.enykp.alogic.fileloader.imp.ImpLoader;
import hu.piller.enykp.alogic.fileloader.xml.XkrLoader;
import hu.piller.enykp.alogic.fileloader.xml.XmlLoader;
import hu.piller.enykp.alogic.masterdata.gui.selector.EntityBookModelConnector;
import hu.piller.enykp.alogic.masterdata.gui.selector.EntitySelection;
import hu.piller.enykp.alogic.primaryaccount.PAInfo;
import hu.piller.enykp.alogic.settingspanel.BaseSettingsPane;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.datastore.CachedCollection;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.framework.StatusPane;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldFactory;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.VisualFieldModel;
import hu.piller.enykp.gui.viewer.DefaultMultiFormViewer;
import hu.piller.enykp.interfaces.ILoadManager;
import hu.piller.enykp.interfaces.IOsHandler;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.errordialog.ErrorDialog;
import hu.piller.enykp.util.font.FontHandler;
import hu.piller.enykp.util.icon.ENYKIconSet;
import hu.piller.enykp.util.oshandler.OsFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.xml.sax.Attributes;

public class GuiUtil {
   public static final String PAR_FONT_NAME = "font_name";
   public static final String PAR_FONT_STYLE = "font_style";
   public static final String PAR_FONT_SIZE = "font_size";
   public static final int TEXTFIELD = 0;
   public static final int CHECKBOX = 1;
   public static final int COMBOBOX = 2;
   public static final int TEXTAREA = 3;
   public static final int DATEFIELD = 4;
   public static final int TAGGEDTEXTFIELD = 5;
   public static final int TAGGEDCOMBO = 6;
   public static final int FORMATTEDTEXTFIELD = 7;
   public static final int SCROLLTEXTAREA = 8;
   public static final int LABEL_FORMAT_REGULAR = 0;
   public static final int LABEL_FORMAT_BOLD = 1;
   public static final int LABEL_FORMAT_ITALIC = 2;
   public static final int LABEL_FORMAT_BOLDITALIC = 3;
   public static final int LABEL_FORMAT_COLOR = 4;
   public static final int LABEL_FORMAT_SIZE = 5;
   public static final int LABEL_FORMAT_FACE = 6;
   public static final int LABEL_FORMAT_SUBSCRIPT = 7;
   public static final int LABEL_FORMAT_SUPERSCRIPT = 8;
   public static HashMap<String, Integer> labelFormatMap;
   public static final String LABEL_FORMAT_START = "{@";
   public static final String LABEL_FORMAT_END = "@}";
   public static final String LABEL_FORMAT_CHAR = "@";
   public static final int LABEL_FORMAT_SEPARATOR_LENGTH = 2;
   private static JLabel dummyComp = new JLabel();
   public static ErrorListDialog eld;
   public static MultiErrorListDialog meld;
   public static final int DEFAULT_OPTION = -1;
   public static final int YES_NO_OPTION = 0;
   public static final int YES_NO_CANCEL_OPTION = 1;
   public static final int OK_CANCEL_OPTION = 2;
   public static final int YES_OPTION = 0;
   public static final int NO_OPTION = 1;
   public static final int CANCEL_OPTION = 2;
   public static final int OK_OPTION = 0;
   public static final int CLOSED_OPTION = -1;
   public static final int ERROR_MESSAGE = 0;
   public static final int INFORMATION_MESSAGE = 1;
   public static final int WARNING_MESSAGE = 2;
   public static final int QUESTION_MESSAGE = 3;
   public static final int PLAIN_MESSAGE = -1;
   private static Icon checkedCheckBoxIcon;
   private static Icon unCheckedCheckBoxIcon;
   private static Icon checkedRadioIcon;
   private static Icon unCheckedRadioIcon;
   private static Dimension pointsButtonDimension;

   public static void showMessageDialog(Component var0, Object var1, String var2, int var3) throws HeadlessException {
      String var4 = System.getProperty("java.awt.headless");
      if (var4 == null || !var4.equals("true")) {
         String var5 = calculateTitle(var1.toString());
         JOptionPane.showMessageDialog((Component)(var0 == null ? MainFrame.thisinstance : var0), var5, var2, var3, (Icon)null);
      }
   }

   public static void showMessageDialog(Component var0, Object var1) {
      String var2 = System.getProperty("java.awt.headless");
      if (var2 == null || !var2.equals("true")) {
         JOptionPane.showMessageDialog((Component)(var0 == null ? MainFrame.thisinstance : var0), var1);
      }
   }

   public static void showMessageDialog_checked(Component var0, Object var1, String var2, int var3) throws HeadlessException {
      String var4 = System.getProperty("java.awt.headless");
      if (var4 == null || !var4.equals("true")) {
         if (eld == null && meld == null) {
            JOptionPane.showMessageDialog((Component)(var0 == null ? MainFrame.thisinstance : var0), var1, var2, var3, (Icon)null);
         }
      }
   }

   public static int showOptionDialog(Component var0, String var1, String var2, int var3, int var4, Icon var5, String[] var6, String var7) {
      String var8 = System.getProperty("java.awt.headless");
      return var8 != null && var8.equals("true") ? -1 : JOptionPane.showOptionDialog((Component)(var0 == null ? MainFrame.thisinstance : var0), var1, var2, var3, var4, var5, var6, var7);
   }

   public static Object showInputDialog(Component var0, String var1, String var2, int var3, int var4, Icon var5, String[] var6, String var7, String[] var8, String var9) {
      String var10 = System.getProperty("java.awt.headless");
      if (var10 != null && var10.equals("true")) {
         return -1;
      } else {
         JOptionPane var11 = new JOptionPane(var1, var3, var4, var5, var8, var9);
         var11.setWantsInput(true);
         var11.setSelectionValues(var6);
         var11.setInitialSelectionValue(var7);
         if (var0 != null || MainFrame.thisinstance != null) {
            var11.setComponentOrientation(((Component)(var0 == null ? MainFrame.thisinstance : var0)).getComponentOrientation());
         }

         JDialog var12 = var11.createDialog(var0, var2);
         var11.selectInitialValue();
         var12.setVisible(true);
         var12.dispose();
         Object var13 = var11.getInputValue();
         return var13 == "uninitializedValue" ? null : var13;
      }
   }

   public static void setGlassLabel(String var0) {
      String var1 = System.getProperty("java.awt.headless");
      if (var1 == null || !var1.equals("true")) {
         MainFrame.thisinstance.setGlassLabel(var0);
      }
   }

   public static void showErrorDialog(Frame var0, String var1, boolean var2, boolean var3, Vector var4) {
      String var5 = System.getProperty("java.awt.headless");
      if (var5 == null || !var5.equals("true")) {
         if (var4 != null) {
            if (var4.size() != 0) {
               new ErrorDialog((Frame)(var0 == null ? MainFrame.thisinstance : var0), var1, var2, var3, var4);
            }
         }
      }
   }

   public static void showErrorDialog(Frame var0, String var1, boolean var2, boolean var3, Vector var4, File var5) {
      String var6 = System.getProperty("java.awt.headless");
      if (var6 == null || !var6.equals("true")) {
         new ErrorDialog((Frame)(var0 == null ? MainFrame.thisinstance : var0), var1, var2, var3, var4, var5);
      }
   }

   public static DefaultMultiFormViewer getDMFV() {
      String var0 = System.getProperty("java.awt.headless");
      return var0 != null && var0.equals("true") ? null : MainFrame.thisinstance.mp.getDMFV();
   }

   public static BookModel getDMFV_bm() {
      String var0 = System.getProperty("java.awt.headless");
      return var0 != null && var0.equals("true") ? null : MainFrame.thisinstance.mp.getDMFV().bm;
   }

   public static ImageIcon IconGet(String var0) {
      return ENYKIconSet.getInstance().get(var0);
   }

   public static Object getFont(Hashtable var0) {
      String var1 = System.getProperty("java.awt.headless");
      return var1 != null && var1.equals("true") ? null : FontHandler.getInstance().getFont(var0);
   }

   public static void done_menuextratext(boolean var0) {
      String var1 = System.getProperty("java.awt.headless");
      if (var1 == null || !var1.equals("true")) {
         BaseSettingsPane.done_menuextratext(var0);
      }
   }

   public static void showelem(Elem var0) {
      String var1 = System.getProperty("java.awt.headless");
      if (var1 == null || !var1.equals("true")) {
         MainFrame.thisinstance.mp.getDMFV().showelem(var0);
      }
   }

   public static void gotoField(StoreItem var0) {
      String var1 = System.getProperty("java.awt.headless");
      if (var1 == null || !var1.equals("true")) {
         MainFrame.thisinstance.mp.getDMFV().gotoField(var0);
      }
   }

   public static void gotopage(String var0, int var1) {
      MainFrame.thisinstance.mp.getDMFV().fv.gotoPage(var0, var1);
   }

   public static ILoadManager[] getLoadManagers() {
      return new ILoadManager[]{new BookModel(), new CachedCollection(), new DatLoader(), new XmlLoader(), new ImpLoader(), new DocInfoLoader(), new XkrLoader()};
   }

   public static void startErrorListDialog(CalculatorManager var0) {
      String var1 = System.getProperty("java.awt.headless");
      if (var1 == null || !var1.equals("true")) {
         ErrorListDialog var2 = new ErrorListDialog();
         eld = var2;
         eld.setCalculatorManager(var0);
         eld.execute();
      }
   }

   public static void startMultiErrorListDialog(CalculatorManager var0) {
      String var1 = System.getProperty("java.awt.headless");
      if (var1 == null || !var1.equals("true")) {
         if (eld != null) {
            eld.closeDialog();
         }

         eld = null;
         if (meld != null) {
            meld.closeDialog();
         }

         meld = null;
         MultiErrorListDialog var2 = new MultiErrorListDialog();
         meld = var2;
         meld.setCalculatorManager(var0);
         meld.execute();
      }
   }

   public static void closeDialog() {
      String var0 = System.getProperty("java.awt.headless");
      if (var0 == null || !var0.equals("true")) {
         if (eld != null) {
            eld.closeDialog();
            eld = null;
         }

         if (meld != null) {
            meld.closeDialog();
            meld = null;
         }

      }
   }

   public static void calchelper(boolean var0, BookModel var1) {
      String var2 = System.getProperty("java.awt.headless");
      if (var2 == null || !var2.equals("true")) {
         BaseSettingsPane.calchelper(var0, var1);
      }
   }

   public static void refreshdatacb() {
      String var0 = System.getProperty("java.awt.headless");
      if (var0 == null || !var0.equals("true")) {
         MainFrame.thisinstance.mp.getDMFV().refreshdatacb();
      }
   }

   public static Object getVisualFieldModel(Attributes var0, FormModel var1) {
      String var2 = System.getProperty("java.awt.headless");
      return var2 != null && var2.equals("true") && !MainFrame.xmlCalculationMode ? null : new VisualFieldModel(var0, var1);
   }

   public static Object setPa(Object var0) {
      String var1 = System.getProperty("java.awt.headless");
      if (var1 != null && var1.equals("true")) {
         return null;
      } else {
         PAInfo var2 = PAInfo.getInstance();

         try {
            var0 = var2.get_pa_record();
         } catch (Exception var4) {
         }

         return var0;
      }
   }

   public static void apply_master_data(EntitySelection[] var0, String var1) {
      String var2 = System.getProperty("java.awt.headless");
      if (var2 == null || !var2.equals("true")) {
         EntityBookModelConnector var3 = new EntityBookModelConnector();
         var3.setSelectedEntities(var0);
         var3.applyOnForm(var1);
      }
   }

   public static void apply_primary_account(Object var0, String var1) {
      String var2 = System.getProperty("java.awt.headless");
      if (var2 == null || !var2.equals("true")) {
         PAInfo var3 = PAInfo.getInstance();
         var3.apply_primary_account(var0, var1);
      }
   }

   public static JComponent getJComponent(int var0) {
      String var1 = System.getProperty("java.awt.headless");
      return var1 != null && var1.equals("true") ? null : DataFieldFactory.getInstance().getJComponent(var0);
   }

   public static JComponent getPainter(int var0) {
      String var1 = System.getProperty("java.awt.headless");
      return var1 != null && var1.equals("true") ? null : DataFieldFactory.getInstance().getPainter(var0);
   }

   public static void resetLabels() {
      String var0 = System.getProperty("java.awt.headless");
      if (var0 == null || !var0.equals("true")) {
         StatusPane var1 = MainFrame.thisinstance.mp.getStatuspanel();
         var1.statusname.setText("");
         var1.formversion.setText("");
         var1.currentpagename.setText("");
         setGlassLabel((String)null);
         done_menuextratext(true);
      }
   }

   public static void setcurrentpagename(String var0) {
      String var1 = System.getProperty("java.awt.headless");
      if (var1 == null || !var1.equals("true")) {
         StatusPane.thisinstance.currentpagename.setText(var0);
      }
   }

   public static void setTitle(String var0) {
      String var1 = System.getProperty("java.awt.headless");
      if (var1 == null || !var1.equals("true")) {
         String var2 = MainFrame.thisinstance.getOrigTitle();
         if (var0 == null) {
            MainFrame.thisinstance.setTitle(var2);
         } else {
            MainFrame.thisinstance.setTitle(var2 + " - " + var0);
         }

      }
   }

   public static void check_csatolmany_oszlop() {
      check_csatolmany_oszlop_util("tablefilter_abev_open_panel_open", 16, 13);
      check_csatolmany_oszlop_util("tablefilter_abev_open_panel_open_import", 16, 12);
      check_csatolmany_oszlop_util("tablefilter_abev_open_panel_open_multi", 16, 12);
      check_csatolmany_oszlop_util("tablefilter_archive_archive_panel", 14, 12);
      check_csatolmany_oszlop_util("tablefilter_archive_select_panel", 14, 12);
      check_csatolmany_oszlop_util("tablefilter_showCopyFromSaveFolderDialog", 16, 13);
      check_csatolmany_oszlop_util("tablefilter_showCopyToSaveFolderDialog", 16, 13);
      check_csatolmany_oszlop_util("tablefilter_FormDataListDialog", 16, 13);
   }

   public static void check_csatolmany_oszlop_util(String var0, int var1, int var2) {
      Hashtable var3 = SettingsStore.getInstance().get(var0);
      if (var3 != null) {
         for(int var4 = 0; var4 < var1 + 1; ++var4) {
            String var5 = (String)var3.get("col_" + var4);
            if (var5 != null && var5.startsWith("Csat.db.")) {
               return;
            }
         }

         var3.put("col_" + var1, "Csat.db.|" + var1 + "|75");
         if (var2 != -1) {
            var3.put("row_" + var2, "(Nincs feltétel)");
         }

      }
   }

   public static void setLabelCommandCode() {
      labelFormatMap = new HashMap();
      labelFormatMap.put("fr", 0);
      labelFormatMap.put("fi", 2);
      labelFormatMap.put("fb", 1);
      labelFormatMap.put("fp", 3);
      labelFormatMap.put("fc", 4);
      labelFormatMap.put("fs", 5);
      labelFormatMap.put("ff", 6);
      labelFormatMap.put("fx", 7);
      labelFormatMap.put("fk", 8);
   }

   public static int getLabelCommandCode(String var0) {
      return !labelFormatMap.containsKey(var0) ? 0 : (Integer)labelFormatMap.get(var0);
   }

   public static String getPlainLabelText(String var0) {
      if (var0.indexOf("{@") == -1) {
         return var0;
      } else {
         int var1 = var0.indexOf("{@");
         StringBuffer var2 = new StringBuffer();
         int var3 = 0;
         String var4 = "";

         int var5;
         for(var5 = 0; var1 > -1 && var5 < var0.length(); ++var5) {
            var4 = var0.substring(var3, var1);
            if (!"@".equals(var4) && !"{".equals(var4) && !"}".equals(var4)) {
               var2.append(var4);
            }

            var1 = var0.indexOf("@", var3);
            if ("{@".equals(var0.substring(var1 - 1, var1 + 1))) {
               var3 = var0.indexOf(";", var1) + 1;
            } else if ("@}".equals(var0.substring(var1, var1 + 2))) {
               var3 = var0.indexOf("@}", var1) + 2;
            }

            var1 = var0.indexOf("@", var3);
            if (var1 > -1) {
               if (var0.charAt(var1 - 1) == '{') {
                  --var1;
               }

               if (var0.charAt(var1 - 1) == '}') {
                  ++var1;
               }
            } else {
               var2.append(var0.substring(var3));
            }
         }

         return var5 >= var0.length() ? var0 : var2.toString();
      }
   }

   public static void setDynamicBound(JComponent var0, String var1, int var2, int var3) {
      int var4 = (Integer)PropertyList.getInstance().get("prop.gui.item.height");
      if (var0 instanceof JLabel && var1.toLowerCase().startsWith("<html>")) {
         setHtmlLabelComp((JLabel)var0, var1, var4, var2, var3);
      } else {
         var0.setBounds(var2, var3, getW(var0, var1 + "W"), var4 + 4);
      }

   }

   public static int getW(String var0) {
      return SwingUtilities.computeStringWidth(dummyComp.getFontMetrics(dummyComp.getFont()), var0);
   }

   public static int getW(JComponent var0, String var1) {
      if (!(var0 instanceof JRadioButton) && !(var0 instanceof JCheckBox)) {
         return var0 instanceof JButton ? SwingUtilities.computeStringWidth(var0.getFontMetrics(var0.getFont()), var1) + 40 : SwingUtilities.computeStringWidth(var0.getFontMetrics(var0.getFont()), var1);
      } else {
         return SwingUtilities.computeStringWidth(var0.getFontMetrics(var0.getFont()), var1) + 20;
      }
   }

   public static int getPositionFromPrevComponent(JComponent var0) {
      return (int)(var0.getBounds().getX() + var0.getBounds().getWidth());
   }

   public static int getCommonItemHeight() {
      try {
         return (Integer)PropertyList.getInstance().get("prop.gui.item.height");
      } catch (Exception var1) {
         return 20;
      }
   }

   public static int getCommonFontSize() {
      try {
         return (Integer)PropertyList.getInstance().get("prop.gui.font.size");
      } catch (Exception var1) {
         return 12;
      }
   }

   private static void setHtmlLabelComp(JLabel var0, String var1, int var2, int var3, int var4) {
      var1 = var1.substring(6, var1.length() - 7);
      String[] var5 = var1.toLowerCase().split("<br>");
      String var6 = "";

      for(int var7 = 0; var7 < var5.length; ++var7) {
         if (var6.length() < var5[var7].length()) {
            var6 = var5[var7];
         }
      }

      var0.setBounds(var3, var4, SwingUtilities.computeStringWidth(var0.getFontMetrics(var0.getFont()), var6 + "www"), var2 * var5.length + 4);
   }

   public static void setPanelBoundsByComponents(JPanel var0, int var1, int var2) {
      int[] var3 = getComponentWAndH(var0);
      int var4 = var3[0];
      int var5 = var3[1];
      var0.setSize(new Dimension(var4 + 20, var5 + getCommonItemHeight() + 4));
      var0.setPreferredSize(var0.getSize());
      var0.setMinimumSize(var0.getSize());
   }

   private static int[] getComponentWAndH(Object var0) {
      int var1 = 0;
      int var2 = 0;
      Component[] var3;
      if (var0 instanceof JDialog) {
         var3 = ((JDialog)var0).getContentPane().getComponents();
      } else {
         var3 = ((JPanel)var0).getComponents();
      }

      Component[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Component var7 = var4[var6];
         if (var7 instanceof JPanel && ((JPanel)var7).getComponents() != null && var7.getWidth() == 0) {
            int[] var8 = getComponentWAndH((JPanel)var7);
            var1 = Math.max(var1, var8[0]);
            var2 = Math.max(var2, var7.getY() + var8[1]);
         } else {
            var1 = Math.max(var1, var7.getWidth());
            var2 = Math.max(var2, var7.getY() + var7.getHeight());
         }
      }

      return new int[]{var1, var2};
   }

   public static int getScreenW() {
      try {
         return (Integer)PropertyList.getInstance().get("prop.gui.screen.maxx");
      } catch (Exception var1) {
         return 1280;
      }
   }

   public static int getScreenH() {
      try {
         return (Integer)PropertyList.getInstance().get("prop.gui.screen.maxy");
      } catch (Exception var1) {
         return 1024;
      }
   }

   public static void getScreenResolutions() {
      try {
         int var0 = Toolkit.getDefaultToolkit().getScreenResolution();
         System.out.println("Screen resolution : " + var0 + " dpi");
         GraphicsDevice var1 = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
         int var2 = var1.getDisplayMode().getWidth();
         int var3 = var1.getDisplayMode().getHeight();
         System.out.println("Screen size : " + var2 + " x " + var3);
         PropertyList.getInstance().set("prop.gui.screen.dpi", var0);
         PropertyList.getInstance().set("prop.gui.screen.maxx", var2);
         PropertyList.getInstance().set("prop.gui.screen.maxy", var3);
      } catch (Exception var4) {
         System.out.println("Screen resolution default 96 dpi");
         System.out.println("Screen size : 1280 x 800");
         PropertyList.getInstance().set("prop.gui.screen.dpi", 96);
         PropertyList.getInstance().set("prop.gui.screen.maxx", 1280);
         PropertyList.getInstance().set("prop.gui.screen.maxy", 800);
      }

   }

   public static boolean modGui() {
      return MainFrame.isModGui();
   }

   public static void setTableColWidth(JTable var0) {
      JLabel var3 = new JLabel();
      TableColumnModel var1 = var0.getColumnModel();

      for(int var4 = 0; var4 < var1.getColumnCount(); ++var4) {
         TableColumn var2 = var1.getColumn(var4);

         try {
            if ("id".equalsIgnoreCase(var2.getHeaderValue().toString())) {
               var2.setMinWidth(0);
               var2.setPreferredWidth(0);
               continue;
            }

            if ("".equalsIgnoreCase(var2.getHeaderValue().toString())) {
               var2.setMinWidth(50);
               var2.setPreferredWidth(50);
               continue;
            }
         } catch (Exception var6) {
         }

         var2.setMinWidth(getW(var3, var2.getHeaderValue() == null ? "WWWWWWWWWW" : var2.getHeaderValue().toString()) + 80);
         var2.setPreferredWidth(getW(var3, var2.getHeaderValue() == null ? "WWWWWWWWWW" : var2.getHeaderValue().toString()) + 80);
      }

   }

   public static void setTableColWidth(JTable var0, String var1, int var2) {
      JLabel var5 = new JLabel();
      TableColumnModel var3 = var0.getColumnModel();

      for(int var6 = 0; var6 < var3.getColumnCount(); ++var6) {
         TableColumn var4 = var3.getColumn(var6);
         if (var1.equalsIgnoreCase(var4.getHeaderValue().toString())) {
            var4.setMinWidth(var2);
            var4.setPreferredWidth(var2);
         } else {
            var4.setMinWidth(getW(var5, var4.getHeaderValue() == null ? "WWWWWWWWWW" : var4.getHeaderValue().toString()) + 80);
            var4.setPreferredWidth(getW(var5, var4.getHeaderValue() == null ? "WWWWWWWWWW" : var4.getHeaderValue().toString()) + 80);
         }
      }

   }

   public static int getTableWidthByColumns(JTable var0) {
      int var1 = 0;

      for(int var2 = 0; var2 < var0.getColumnModel().getColumnCount(); ++var2) {
         var1 += var0.getColumnModel().getColumn(var2).getPreferredWidth();
      }

      return var1;
   }

   public static int getTableWidthByColumns(JTable var0, int var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < Math.min(var1, var0.getColumnModel().getColumnCount()); ++var3) {
         var2 += var0.getColumnModel().getColumn(var3).getPreferredWidth();
      }

      return var2;
   }

   public static Dimension getSettingsDialogDimension() {
      int var0 = (Integer)PropertyList.getInstance().get("prop.gui.screen.maxx") - 200;
      int var1 = Math.min(30 * (getCommonItemHeight() + 2), (Integer)PropertyList.getInstance().get("prop.gui.screen.maxy") - 100);
      int var2 = Math.max(600, getW(new JLabel(""), "Amennyiben adatait hálózati meghajtón tárolja a csatolmányt is tartalmazó nyomtatvány feladása sokáig tarthat.WWWWW"));
      Dimension var3 = new Dimension(Math.min(var2, var0), var1);
      return var3;
   }

   public static void setDummyComp() {
      dummyComp = new JLabel();
      checkedRadioIcon = ENYKIconSet.getInstance().get("radio_button_aktiv");
      checkedCheckBoxIcon = ENYKIconSet.getInstance().get("checkbox_aktiv");
      unCheckedRadioIcon = ENYKIconSet.getInstance().get("radio_button_inaktiv");
      unCheckedCheckBoxIcon = ENYKIconSet.getInstance().get("checkbox_inaktiv");
   }

   public static String calculateTitle(String var0) {
      if (getCommonFontSize() == 12) {
         return var0;
      } else {
         String[] var1 = var0.split("\n");
         StringBuffer var2 = new StringBuffer();

         try {
            int var3 = getScreenW() - 100;

            for(int var4 = 0; var4 < var1.length; ++var4) {
               int var5 = SwingUtilities.computeStringWidth(dummyComp.getFontMetrics(dummyComp.getFont()), var1[var4]);
               if (var3 > var5) {
                  var2.append(var1[var4]).append("\n");
               } else {
                  double var6 = (double)var3 / (double)var5;
                  int var8 = (int)((double)var1[var4].length() * var6);
                  int var9 = getBreakPosition(var1[var4], var8);
                  var2.append(var1[var4].substring(0, var9)).append("\n").append(var1[var4].substring(var9)).append("\n");
               }
            }

            return var2.toString();
         } catch (Exception var10) {
            return var0;
         }
      }
   }

   private static int getBreakPosition(String var0, int var1) {
      int var2;
      for(var2 = var1; var2 > 0; --var2) {
         if (var0.charAt(var2) == ' ') {
            return var2;
         }
      }

      for(var2 = var1; var2 > 0; --var2) {
         if (var0.charAt(var2) == '\\') {
            return var2;
         }

         if (var0.charAt(var2) == '/') {
            return var2;
         }

         if (var0.charAt(var2) == ',') {
            return var2;
         }
      }

      return var1;
   }

   public static boolean isHighDpi() {
      try {
         String var0 = SettingsStore.getInstance().get("gui", "anykHighDPI");
         return var0.equalsIgnoreCase("true");
      } catch (Exception var1) {
         return false;
      }
   }

   public static JRadioButton getANYKRadioButton() {
      return getANYKRadioButton("", false);
   }

   public static JRadioButton getANYKRadioButton(String var0) {
      return getANYKRadioButton(var0, false);
   }

   public static JRadioButton getANYKRadioButton(String var0, boolean var1) {
      JRadioButton var2 = new JRadioButton(var0);
      var2.setIcon(unCheckedRadioIcon);
      var2.setSelectedIcon(checkedRadioIcon);
      var2.setSelected(var1);
      return var2;
   }

   public static JCheckBox getANYKCheckBox() {
      return getANYKCheckBox("", false);
   }

   public static JCheckBox getANYKCheckBox(String var0) {
      return getANYKCheckBox(var0, false);
   }

   public static JCheckBox getANYKCheckBox(String var0, boolean var1) {
      JCheckBox var2 = new JCheckBox(var0);
      var2.setIcon(unCheckedCheckBoxIcon);
      var2.setSelectedIcon(checkedCheckBoxIcon);
      var2.setSelected(var1);
      return var2;
   }

   public static int getToolBarHeight() {
      int var0 = getCommonFontSize();
      if (var0 < 20) {
         return 28;
      } else if (var0 < 28) {
         return 36;
      } else if (var0 < 36) {
         return 52;
      } else {
         return var0 < 48 ? 64 : 76;
      }
   }

   public static Dimension getPointsButtonDim() {
      if (pointsButtonDimension != null) {
         return pointsButtonDimension;
      } else {
         int var0 = SwingUtilities.computeStringWidth(dummyComp.getFontMetrics(dummyComp.getFont()), "...");
         pointsButtonDimension = new Dimension(var0, getCommonItemHeight() + 2);
         return pointsButtonDimension;
      }
   }

   public static int getPointsButtonWidth() {
      if (pointsButtonDimension == null) {
         pointsButtonDimension = getPointsButtonDim();
      }

      return (int)getPointsButtonDim().getWidth();
   }

   public static Dimension getButtonSizeByIcon(JButton var0) {
      return var0.getIcon() == null ? new Dimension(24, 24) : new Dimension(var0.getIcon().getIconWidth() + 4, var0.getIcon().getIconHeight() + 4);
   }

   public static Color getModifiedBGColor() {
      try {
         Color var0 = dummyComp.getBackground();
         return var0.getRed() > 130 && var0.getGreen() > 130 && var0.getBlue() > 130 ? var0.darker() : var0.brighter();
      } catch (Exception var1) {
         return new Color(0, 160, 215);
      }
   }

   public static Color getHighLightColor() {
      try {
         return (Color)UIManager.get("MenuItem.selectionBackground");
      } catch (Exception var1) {
         return new Color(0, 160, 215);
      }
   }

   public static Color getHighLightTextColor() {
      try {
         return (Color)UIManager.get("MenuItem.selectionForeground");
      } catch (Exception var1) {
         return Color.BLACK;
      }
   }

   public static void showExtendedDialog(String var0, String var1) throws IOException {
      final JDialog var2 = new JDialog(MainFrame.thisinstance, "Üzenet", true);
      String var3 = var0;
      if ("Az Online alkalmazás megnyitása".length() > var0.length()) {
         var3 = "Az Online alkalmazás megnyitása";
      }

      int var4 = getW(var3) + 40;
      int var5 = getCommonItemHeight() + 12;
      var2.setSize(new Dimension(var4, 5 * var5 + 5));
      var2.setPreferredSize(var2.getSize());
      var2.setMinimumSize(var2.getSize());
      var2.setLocationRelativeTo(MainFrame.thisinstance);
      var2.setLayout(new BorderLayout());
      JPanel var6 = new JPanel(new BorderLayout(10, 10));
      JLabel var7 = new JLabel(var0);
      setDynamicBound(var7, var0, 10, 10);
      if (!"".equals(var1)) {
         Color var8 = (new JLabel()).getBackground();
         String var9 = String.format("#%02x%02x%02x", var8.getRed(), var8.getGreen(), var8.getBlue());
         StringBuffer var10 = new StringBuffer("<html><body style=\"background:");
         var10.append(var9).append("; font:Sans-Serif; font-color: black; font-size:").append(getCommonFontSize()).append("pt;\">").append("<center>");
         var10.append("<a style=\"text-decoration: none;\"").append(" href=\"").append(var1).append("\">Az Online alkalmazás megnyitása</a></center></body></html>");
         JEditorPane var11 = new JEditorPane("text/html", var10.toString());
         var11.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent var1) {
               if (var1.getEventType() == EventType.ACTIVATED) {
                  GuiUtil.execute(var1.getURL().toString());
               }

            }
         });
         var11.setEditable(false);
         var11.setBorder((Border)null);
         var6.add(var7, "North");
         var6.add(var11, "Center");
         var6.setBorder(new EmptyBorder(10, 10, 10, 10));
      }

      JPanel var12 = new JPanel();
      JButton var13 = new JButton("OK");
      var13.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            var2.dispose();
         }
      });
      var12.add(var13, "Center");
      var2.add(var6, "Center");
      var2.add(var12, "South");
      var2.setVisible(true);
   }

   public static void execute(String var0) {
      IOsHandler var1 = OsFactory.getOsHandler();

      try {
         File var2;
         try {
            var2 = new File(SettingsStore.getInstance().get("gui", "internet_browser"));
            if (!var2.exists()) {
               throw new Exception();
            }
         } catch (Exception var4) {
            var2 = new File(var1.getSystemBrowserPath());
         }

         var1.execute(var2.getName() + " " + var0, (String[])null, var2.getParentFile());
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
