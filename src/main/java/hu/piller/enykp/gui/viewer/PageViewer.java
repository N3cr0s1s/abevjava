package hu.piller.enykp.gui.viewer;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.ABEVFunctionSet;
import hu.piller.enykp.alogic.fileloader.xml.XMLPost;
import hu.piller.enykp.alogic.filesaver.enykinner.ENYKClipboardHandler;
import hu.piller.enykp.alogic.fileutil.DataChecker;
import hu.piller.enykp.alogic.kihatas.KihatasCsopDialog;
import hu.piller.enykp.alogic.kihatas.KihatasRecord;
import hu.piller.enykp.alogic.kihatas.KihatasSimpleDialog;
import hu.piller.enykp.alogic.kihatas.KihatasTableModel;
import hu.piller.enykp.alogic.kihatas.MegallapitasComboLista;
import hu.piller.enykp.alogic.kihatas.MegallapitasComboRecord;
import hu.piller.enykp.alogic.kihatas.MegallapitasRecord;
import hu.piller.enykp.alogic.kihatas.MegallapitasVector;
import hu.piller.enykp.alogic.kihatas.MultiLineTable;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.metainfo.MetaStore;
import hu.piller.enykp.alogic.panels.DetailsDialog;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.datastore.Datastore_history;
import hu.piller.enykp.datastore.Datastore_viewer;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.GUI_Datastore;
import hu.piller.enykp.datastore.Kihatasstore;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.extensions.db.DbFactory;
import hu.piller.enykp.extensions.db.IDbHandler;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.framework.Menubar;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.gui.model.VisualFieldModel;
import hu.piller.enykp.interfaces.IDataField;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.RowFilter.Entry;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class PageViewer extends JPanel implements Scrollable {
   PageModel PM;
   JComponent current_jc;
   DataFieldModel current_df;
   MouseInputAdapter mia;
   public int dynindex;
   public int lapcount;
   CalculatorManager cm;
   double z = 1.0D;
   Dimension origsize;
   SettingsStore ss;
   MegallapitasComboLista megallapitaslista;
   boolean veto_kihatas_dialog = false;
   boolean kihatas_dialog_shown = false;
   public Action hist_action;
   boolean nocheck;
   boolean nofire = false;
   boolean newtread = true;
   private String msg1 = "A mezőhöz nem tartozik egyedi segítség!";
   private String title1 = "Üzenet";
   private String msg2 = "A mezőhöz nem tartozik történet!";
   private String msg2k = "A mezőhöz nem tartozik kihatás!";
   private String optionstr1 = "Javít";
   private String optionstr2 = "Tovább";
   private String title3 = "Hibaüzenet";
   private String title4 = "Figyelmeztetés";
   private String dtitle1 = "Történet a ";
   private String dtitle2 = " mezőhöz";
   private String okstr = "Ok";
   private String ktitle1 = "Revizori módosító kihatás tételek rögzítése";
   private JPopupMenu contextMenu = new JPopupMenu();
   private JMenuItem copyItem = new JMenuItem("Tartalom másolása a vágólapra");
   private ENYKClipboardHandler clip = new ENYKClipboardHandler();
   private String copyToClipboardValue;
   private String[] fidAndVidToDebug = new String[2];
   private boolean detail_mode = false;
   private String msg_detail_main;
   private String msg_detail;
   private Result forintResult;
   private Object cmdObject;
   public static final JProgressBar progressLabel = new JProgressBar();

   public PageViewer() {
      this.setName("pv");
      this.handlePopupMenu();
   }

   public PageViewer(PageModel var1) {
      this.setName("pv");
      this.load(var1);
   }

   public void load(PageModel var1) {
      if (this.mia != null) {
         this.removeMouseListener(this.mia);
         this.removeMouseMotionListener(this.mia);
      }

      this.PM = var1;
      this.ss = SettingsStore.getInstance();
      this.init();
      this.setPreferredSize(this.PM.psize);
      this.origsize = this.PM.psize;
      InputMap var2 = this.getInputMap(1);
      ActionMap var3 = this.getActionMap();
      String var4 = "FieldHelp";
      AbstractAction var5 = new AbstractAction("Help", (Icon)null) {
         public void actionPerformed(ActionEvent var1) {
            String var2;
            if (PageViewer.this.current_df != null) {
               var2 = null;

               try {
                  var2 = ((Hashtable)PageViewer.this.PM.getFormModel().getBookModel().getMetas(PageViewer.this.PM.getFormModel().id).get(PageViewer.this.current_df.key)).get("help").toString();
               } catch (Exception var4) {
               }

               if (var2 == null || var2.equals("")) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, PageViewer.this.msg1, PageViewer.this.title1, 1);
                  return;
               }

               MainFrame.thisinstance.mp.showpanel("Kitöltési útmutató (F1)");
               MainFrame.thisinstance.mp.set_kiut_url(var2);
            } else {
               MainFrame.thisinstance.mp.showpanel("Kitöltési útmutató (F1)");
               var2 = (String)PageViewer.this.PM.xmlht.get("help");
               MainFrame.thisinstance.mp.set_kiut_url(var2);
            }

         }
      };
      String var6 = "FieldHistory";
      this.hist_action = new AbstractAction("FieldHistory", (Icon)null) {
         public void actionPerformed(ActionEvent var1) {
            if (PageViewer.this.current_df != null) {
               boolean var2 = PageViewer.this.done_fieldhistory((DataFieldModel)null);
               if (!var2) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, PageViewer.this.msg2, PageViewer.this.title1, 1);
                  return;
               }
            } else {
               Point var5 = MouseInfo.getPointerInfo().getLocation();
               SwingUtilities.convertPointFromScreen(var5, PageViewer.this);
               DataFieldModel var3 = PageViewer.this.PM.getAt2(var5);
               if (var3 == null) {
                  return;
               }

               boolean var4 = PageViewer.this.done_fieldhistory(var3);
               if (!var4) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, PageViewer.this.msg2, PageViewer.this.title1, 1);
                  return;
               }
            }

         }
      };
      String var7 = "DataStore_view";
      AbstractAction var8 = new AbstractAction("DataStore_view", (Icon)null) {
         public void actionPerformed(ActionEvent var1) {
            try {
               BookModel var2 = PageViewer.this.PM.getFormModel().getBookModel();
               Elem var3 = (Elem)var2.cc.getActiveObject();
               IDataStore var4 = (IDataStore)var3.getRef();
               Map var5 = PageViewer.this.cm.calculateVariables(var2.get().id);
               Datastore_viewer.showDialog(var2, (GUI_Datastore)var4, var5);
            } catch (Exception var6) {
            }

         }
      };
      var2.put(KeyStroke.getKeyStroke("F1"), var4);
      var3.put(var4, var5);
      String var9 = "kihatas";
      AbstractAction var10 = new AbstractAction("kihatas_dialog", (Icon)null) {
         public void actionPerformed(ActionEvent var1) {
            try {
               if (PageViewer.this.current_df != null) {
                  if (!PageViewer.this.forintos(PageViewer.this.current_df).isOk()) {
                     return;
                  }

                  boolean var2 = PageViewer.this.done_field_kihatas((DataFieldModel)null);
                  if (!var2) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, PageViewer.this.msg2k, PageViewer.this.title1, 1);
                     return;
                  }
               } else {
                  Point var6 = MouseInfo.getPointerInfo().getLocation();
                  SwingUtilities.convertPointFromScreen(var6, PageViewer.this);
                  DataFieldModel var3 = PageViewer.this.PM.getAt(var6);
                  if (var3 == null) {
                     return;
                  }

                  if (var3.readonly && "0".equals(var3.getTolerance())) {
                     return;
                  }

                  if (!PageViewer.this.forintos(var3).isOk()) {
                     return;
                  }

                  boolean var4 = PageViewer.this.done_field_kihatas(var3);
                  if (!var4) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, PageViewer.this.msg2k, PageViewer.this.title1, 1);
                     return;
                  }
               }
            } catch (Exception var5) {
            }

         }
      };
      if (!"0".equals(MainFrame.role) && MainFrame.ellvitamode) {
         var2.put(KeyStroke.getKeyStroke("F4"), var9);
         var3.put(var9, var10);
      }

      String var11 = "combolista";
      AbstractAction var12 = new AbstractAction("combolista_dialog", (Icon)null) {
         public void actionPerformed(ActionEvent var1) {
            if (MainFrame.thisinstance.mp.isReadonlyMode() || MainFrame.readonlymodefromubev) {
               try {
                  Point var2 = MouseInfo.getPointerInfo().getLocation();
                  SwingUtilities.convertPointFromScreen(var2, PageViewer.this);
                  DataFieldModel var3 = PageViewer.this.PM.getAt2(var2);
                  if (var3 == null) {
                     return;
                  }

                  if (var3.type != 2 && var3.type != 6) {
                     return;
                  }

                  Point var4 = new Point(var3.x + var3.w, var3.y + var3.h);
                  SwingUtilities.convertPointToScreen(var4, PageViewer.this);
                  boolean var5 = PageViewer.this.done_field_combolista(var3, var4);
                  if (!var5) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "A mezőhőz nincs megjeleníthető lista!", PageViewer.this.title1, 1);
                     return;
                  }
               } catch (Exception var6) {
               }

            }
         }
      };
      var2.put(KeyStroke.getKeyStroke("F5"), var11);
      var3.put(var11, var12);
      String var13 = "details";
      AbstractAction var14 = new AbstractAction("details_dialog", (Icon)null) {
         public void actionPerformed(ActionEvent var1) {
            if (!XMLPost.xmleditnemjo) {
               if (PageViewer.this.current_df != null) {
                  DataFieldModel var2 = PageViewer.this.current_df;
                  boolean var3 = PageViewer.this.done_details(PageViewer.this.current_df);
                  PageViewer.this.current_df = var2;
                  PageViewer.this.change_component(var2);
               }

            }
         }
      };
      if ("0".equals(MainFrame.role)) {
         var2.put(KeyStroke.getKeyStroke("F6"), var13);
         var3.put(var13, var14);
      }

      String var15 = "szamologep";
      AbstractAction var16 = new AbstractAction("szamologep", (Icon)null) {
         public void actionPerformed(ActionEvent var1) {
            Menubar.thisinstance.calculatorcmd.execute();
         }
      };
      var2.put(KeyStroke.getKeyStroke("F7"), var15);
      var3.put(var15, var16);
      var2.put(KeyStroke.getKeyStroke("ctrl shift F12"), var7);
      var3.put(var7, var8);
      String var17 = "up";
      AbstractAction var18 = new AbstractAction("up", (Icon)null) {
         public void actionPerformed(ActionEvent var1) {
            try {
               PageViewer.this.done_up();
            } catch (Exception var3) {
               var3.printStackTrace();
            }

         }
      };
      var2.put(KeyStroke.getKeyStroke("UP"), var17);
      var3.put(var17, var18);
      String var19 = "down";
      AbstractAction var20 = new AbstractAction("down", (Icon)null) {
         public void actionPerformed(ActionEvent var1) {
            try {
               PageViewer.this.done_down();
            } catch (Exception var3) {
               var3.printStackTrace();
            }

         }
      };
      var2.put(KeyStroke.getKeyStroke("DOWN"), var19);
      var3.put(var19, var20);
      AbstractAction var21 = new AbstractAction("esc", (Icon)null) {
         public void actionPerformed(ActionEvent var1) {
            try {
               BookModel var2 = PageViewer.this.PM.getFormModel().getBookModel();
               Elem var3 = (Elem)var2.cc.getActiveObject();
               IDataStore var4 = (IDataStore)var3.getRef();
               String var5 = ((IDataField)PageViewer.this.current_jc).getFieldValue().toString();
               String var6 = PageViewer.this.current_df.key;
               Object[] var7 = new Object[]{new Integer(PageViewer.this.dynindex), var6};
               String var8 = var4.get(var7);
               ((IDataField)PageViewer.this.current_jc).setValue(var8);
               PageViewer.this.leave_component();
            } catch (Exception var9) {
               Tools.eLog(var9, 0);
            }

         }
      };
      JTextComponent.getKeymap("default").addActionForKeyStroke(KeyStroke.getKeyStroke("ESCAPE"), var21);
   }

   private void init() {
      this.nocheck = false;
      this.dynindex = 0;
      this.lapcount = 1;
      this.setLayout((LayoutManager)null);
      this.cm = new CalculatorManager();
      this.mia = new MouseInputAdapter() {
         public void mouseClicked(MouseEvent var1) {
            Point var2 = var1.getPoint();
            DataFieldModel var3 = PageViewer.this.PM.getAt(var2);
            if (var3 != null && var1.getButton() > 1) {
               PageViewer.this.copyToClipboardValue = var3.value.toString();
               PageViewer.this.fidAndVidToDebug[0] = var3.key;
               PageViewer.this.fidAndVidToDebug[1] = (String)var3.features.get("vid");
               PageViewer.this.contextMenu.setVisible(true);
               PageViewer.this.contextMenu.show(MainFrame.thisinstance, (int)var2.getX(), (int)MainFrame.thisinstance.getMousePosition().getY());
            }

            if (var3 != null && var3.type != 8) {
               if (MainFrame.thisinstance.mp.isReadonlyMode() || MainFrame.thisinstance.mp.forceDisabledPageShowing) {
                  PageViewer.this.requestFocus();
                  return;
               }

               if (MainFrame.readonlymodefromubev) {
                  PageViewer.this.requestFocus();
                  return;
               }
            }

            if (!MainFrame.rogzitomode || var3 == null || !var3.key.equals(MainFrame.fixfidcode)) {
               if (("2".equals(MainFrame.role) || "3".equals(MainFrame.role)) && MainFrame.ellvitamode) {
                  Result var4 = PageViewer.this.forintos(var3);
                  if (var4.isOk()) {
                     PageViewer.this.leave_component();
                     if (var4.errorList.size() > 0) {
                        GuiUtil.showMessageDialog(MainFrame.thisinstance, var4.errorList.elementAt(0), "Hiba!", 0);
                     }

                     PageViewer.this.done_field_kihatas(var3);
                     return;
                  }
               }

               PageViewer.this.change_component(var3);
               if (var3 == null) {
                  PageViewer.this.requestFocus();
               }

            }
         }

         public void mouseMoved(MouseEvent var1) {
            try {
               String var2 = PageViewer.this.ss.get("gui", "mezőkódkijelzés");
               Point var3 = var1.getPoint();
               DataFieldModel var4 = PageViewer.this.PM.getAt2(var3);
               String var5 = "<HR><b>Tételek</b><table><tbody>\n    <tr width=\"30%\">\n      <td><b>Megnevezés</b></td>\n      <td> </td>\n      <td><b>Összeg</b></td>\n    </tr>";
               if (var4 != null) {
                  int var6 = ABEVFunctionSet.getInstance().getPrecisionForKihatas(var4.key);
                  String var7;
                  Elem var9;
                  IDataStore var10;
                  StoreItem var11;
                  Vector var12;
                  String var13;
                  int var14;
                  BookModel var20;
                  if (var2 != null && var2.equals("false")) {
                     var7 = null;

                     try {
                        var7 = (String)var4.features.get("tool_tip");
                        if (var7 != null) {
                           var7 = var7.replaceAll("#13", "<br>");
                           var7 = "<HTML>" + var7 + "</HTML>";
                        }
                     } catch (Exception var15) {
                     }

                     try {
                        var20 = PageViewer.this.PM.getFormModel().getBookModel();
                        var9 = (Elem)var20.cc.getActiveObject();
                        var10 = (IDataStore)var9.getRef();
                        var11 = (StoreItem)var10.getMasterCaseId(PageViewer.this.dynindex + "_" + var4.key);
                        var12 = var11.getDetails();
                        var13 = "";
                        if (var12 != null) {
                           var13 = var5;

                           for(var14 = 0; var14 < var12.size(); ++var14) {
                              var13 = var13 + "<tr width=\"30%\"><td>" + ((Vector)var12.get(var14)).get(0) + "</td><td> </td><td align=\"right\">" + MultiLineTable.formatnumber((String)((Vector)var12.get(var14)).get(1), var6) + "</td></tr>";
                           }

                           var13 = var13 + "</tbody></table></HTML>";
                           if (var7 != null) {
                              var7 = var7.substring(0, var7.indexOf("</HTML>")) + var13;
                           } else {
                              var7 = "<HTML>" + var13;
                           }
                        }
                     } catch (Exception var17) {
                     }

                     PageViewer.this.setToolTipText(var7);
                     return;
                  }

                  var7 = MetaInfo.extendedInfo(var4.key, PageViewer.this.PM.dynamic ? new Integer(PageViewer.this.dynindex) : null, PageViewer.this.PM.getFormModel().id, PageViewer.this.PM.getFormModel().bm);

                  try {
                     String var8 = (String)var4.features.get("tool_tip");
                     if (var8 != null) {
                        var8 = var8.replaceAll("#13", "<br>");
                        var8 = "<HR>" + var8 + "</HTML>";
                        var7 = var7.substring(0, var7.indexOf("</HTML>")) + var8;
                     }
                  } catch (Exception var16) {
                  }

                  try {
                     var20 = PageViewer.this.PM.getFormModel().getBookModel();
                     var9 = (Elem)var20.cc.getActiveObject();
                     var10 = (IDataStore)var9.getRef();
                     var11 = (StoreItem)var10.getMasterCaseId(PageViewer.this.dynindex + "_" + var4.key);
                     var12 = var11.getDetails();
                     var13 = "";
                     if (var12 != null) {
                        var13 = var5;

                        for(var14 = 0; var14 < var12.size(); ++var14) {
                           var13 = var13 + "<tr width=\"30%\"><td>" + ((Vector)var12.get(var14)).get(0) + "</td><td> </td><td align=\"right\">" + MultiLineTable.formatnumber((String)((Vector)var12.get(var14)).get(1), var6) + "</td></tr>";
                        }

                        var13 = var13 + "</tbody></table></HTML>";
                        var7 = var7.substring(0, var7.indexOf("</HTML>")) + var13;
                     }
                  } catch (Exception var18) {
                  }

                  if (!var4.invisible) {
                     PageViewer.this.setToolTipText(var7);
                  } else {
                     PageViewer.this.setToolTipText((String)null);
                  }
               } else {
                  PageViewer.this.setToolTipText((String)null);
               }

            } catch (Exception var19) {
            }
         }
      };
      this.handleMouseListeners();
      this.addMouseMotionListener(this.mia);
      this.setFocusTraversalPolicy(new FocusTraversalPolicy() {
         public Component getComponentAfter(Container var1, Component var2) {
            if (PageViewer.this.nofire) {
               return PageViewer.this;
            } else if (MainFrame.ellvitamode) {
               PageViewer.this.leave_component();
               return PageViewer.this;
            } else if (!PageViewer.this.newtread) {
               int var5 = PageViewer.this.PM.tab_sorted_df.indexOf(PageViewer.this.current_df);
               if (var5 == PageViewer.this.PM.tab_sorted_df.size() - 1) {
                  var5 = -1;
               }

               DataFieldModel var4 = PageViewer.this.PM.getNext(var5 + 1);
               PageViewer.this.change_component(var4);
               return PageViewer.this.current_jc;
            } else {
               Runnable var3 = new Runnable() {
                  public void run() {
                     PageViewer.this.change_component(0);
                  }
               };
               SwingUtilities.invokeLater(var3);
               return PageViewer.this;
            }
         }

         public Component getComponentBefore(Container var1, Component var2) {
            if (PageViewer.this.nofire) {
               return PageViewer.this;
            } else if (MainFrame.ellvitamode) {
               PageViewer.this.leave_component();
               return PageViewer.this;
            } else if (!PageViewer.this.newtread) {
               int var5 = PageViewer.this.PM.tab_sorted_df.indexOf(PageViewer.this.current_df);
               if (var5 == 0) {
                  var5 = PageViewer.this.PM.tab_sorted_df.size();
               }

               DataFieldModel var4 = PageViewer.this.PM.getPrev(var5 - 1);
               PageViewer.this.change_component(var4);
               return PageViewer.this.current_jc;
            } else {
               Runnable var3 = new Runnable() {
                  public void run() {
                     PageViewer.this.change_component(1);
                  }
               };
               SwingUtilities.invokeLater(var3);
               return PageViewer.this;
            }
         }

         public Component getFirstComponent(Container var1) {
            if (MainFrame.ellvitamode) {
               return PageViewer.this;
            } else {
               DataFieldModel var2 = PageViewer.this.PM.getNext(0);
               PageViewer.this.change_component(var2);
               return PageViewer.this.current_jc;
            }
         }

         public Component getLastComponent(Container var1) {
            if (MainFrame.ellvitamode) {
               return PageViewer.this;
            } else {
               DataFieldModel var2 = PageViewer.this.PM.getPrev(0);
               PageViewer.this.change_component(var2);
               return PageViewer.this.current_jc;
            }
         }

         public Component getDefaultComponent(Container var1) {
            return PageViewer.this;
         }
      });
      this.setFocusable(true);
      this.setFocusCycleRoot(true);
      HashSet var1 = new HashSet();
      Set var2 = this.getFocusTraversalKeys(0);
      var1.addAll(var2);
      var1.add(KeyStroke.getKeyStroke("ENTER"));
      this.setFocusTraversalKeys(0, var1);
      var1 = new HashSet();
      var2 = this.getFocusTraversalKeys(1);
      var1.addAll(var2);
      this.setFocusTraversalKeys(1, var1);
   }

   public boolean leave_component() {
      if (this.current_jc == null) {
         return true;
      } else {
         DataFieldModel var1 = null;
         if (!this.done_prev()) {
            var1 = this.current_df;
         }

         if (var1 != null) {
            JComponent var2 = var1.getJComponent((int)(this.z * 100.0D), this.dynindex);
            this.add(var2);
            this.current_jc = var2;
            this.current_df = var1;
            this.validate();
            this.repaint();
            var2.requestFocus();
            return false;
         } else {
            this.current_jc = null;
            this.current_df = null;
            this.validate();
            this.repaint();
            return true;
         }
      }
   }

   public void leave_component_nocheck() {
      if (this.current_jc != null) {
         DataFieldModel var1 = null;
         this.nocheck = true;
         if (!this.done_prev()) {
            var1 = this.current_df;
         }

         this.nocheck = false;
         if (var1 != null) {
            JComponent var2 = var1.getJComponent((int)(this.z * 100.0D), this.dynindex);
            this.add(var2);
            this.current_jc = var2;
            this.current_df = var1;
            this.validate();
            this.repaint();
            var2.requestFocus();
         } else {
            this.current_jc = null;
            this.current_df = null;
            this.validate();
            this.repaint();
         }
      }
   }

   private void change_component(int var1) {
      int var3 = this.PM.tab_sorted_df.indexOf(this.current_df);
      DataFieldModel var2;
      if (!this.done_prev()) {
         var2 = this.current_df;
      } else if (var1 == 0) {
         if (var3 == this.PM.tab_sorted_df.size() - 1) {
            var3 = -1;
         }

         var2 = this.PM.getNext(var3 + 1);
      } else {
         if (var3 == 0) {
            var3 = this.PM.tab_sorted_df.size();
         }

         var2 = this.PM.getPrev(var3 - 1);
      }

      if (var2 != null && var2.readonly) {
         var2 = null;
      }

      if (var2 != null) {
         final JComponent var4 = var2.getJComponent((int)(this.z * 100.0D), this.dynindex);
         this.nofire = true;
         this.add(var4);
         this.nofire = false;
         this.current_jc = var4;
         this.current_df = var2;
         Rectangle var5 = ((JViewport)this.getParent()).getViewRect();
         if (!var5.contains(var4.getBounds())) {
            this.scrollRectToVisible(var4.getBounds());
         }

         this.getParent().validate();
         this.repaint();
         Thread var6 = new Thread(new Runnable() {
            public void run() {
               var4.requestFocus();
            }
         });
         var6.start();
      } else {
         this.current_jc = null;
         this.current_df = null;
         this.validate();
         this.repaint();
      }

   }

   public void change_component(DataFieldModel var1) {
      if (!this.done_prev()) {
         var1 = this.current_df;
      }

      if (var1 != null && var1.readonly) {
         var1 = null;
      }

      if (var1 != null) {
         final JComponent var2 = var1.getJComponent((int)(this.z * 100.0D), this.dynindex);
         this.nofire = true;
         this.add(var2);
         this.nofire = false;
         this.current_jc = var2;
         this.current_df = var1;
         Rectangle var3 = ((JViewport)this.getParent()).getViewRect();
         if (!var3.contains(var2.getBounds())) {
            this.scrollRectToVisible(var2.getBounds());
         }

         this.getParent().validate();
         this.repaint();
         Thread var4 = new Thread(new Runnable() {
            public void run() {
               var2.requestFocus();
            }
         });
         var4.start();
      } else {
         this.current_jc = null;
         this.current_df = null;
         this.validate();
         this.repaint();
      }

   }

   private boolean done_prev() {
      if (this.current_jc != null) {
         try {
            InputVerifier var1 = this.current_jc.getInputVerifier();
            if (var1 != null && var1 instanceof IPropertyList) {
               ((IPropertyList)var1).get(this.current_jc);
            }
         } catch (Exception var7) {
         }

         this.current_df.value = ((IDataField)this.current_jc).getFieldValue();
         boolean var8 = this.done_calc_check();
         String var2 = this.ss.get("gui", "mezőszámítás");
         if (var2 != null && var2.equals("true") && this.cm.isFoAdatDependency(this.PM.getFormModel().id, this.current_df.key)) {
            try {
               PropertyList.getInstance().set("foadatcalculation", true);
               this.handleCalcFoadat();
               this.refresh();
            } finally {
               PropertyList.getInstance().set("foadatcalculation", false);
            }
         }

         this.nofire = true;
         this.removeAll();
         this.nofire = false;
         return var8;
      } else {
         return true;
      }
   }

   private boolean done_calc_check_detail(Frame var1, String var2, Icon var3, String var4) {
      final Object[] var5 = new Object[]{new Boolean(true)};
      this.detail_mode = false;
      String var6 = var2.replaceAll("<", "&lt;");
      var6 = var6.replaceAll(">", "&gt;");
      var6 = var6.replaceAll("\n", "<br>");
      if (var6.endsWith("<br>")) {
         var6 = var6.substring(0, var6.length() - 4);
      }

      int var7 = var6.indexOf("Mezőinformációk");
      if (var7 == -1) {
         var7 = var6.length();
      }

      this.msg_detail = "<html>" + var6.substring(var7) + "</html>";
      this.msg_detail_main = "<html>" + var6.substring(0, var7) + "</html>";
      JLabel var8 = new JLabel(this.msg_detail_main);
      final JLabel var9 = new JLabel(this.msg_detail);
      var8.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
      var8.setName("OptionPane.label");
      var9.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(10, 1, 1, 1)));
      int var10 = var9.getPreferredSize().width;
      final JDialog var11 = new JDialog(var1, true);
      var11.setTitle(var4);
      var11.setResizable(true);
      KeyStroke var12 = KeyStroke.getKeyStroke(27, 0);
      var11.getRootPane().getInputMap(2).put(var12, "kilepesescre");
      AbstractAction var13 = new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            var11.dispatchEvent(new WindowEvent(var11, 201));
         }
      };
      var11.getRootPane().getActionMap().put("kilepesescre", var13);
      JPanel var14 = new JPanel();
      var14.setLayout(new BoxLayout(var14, 2));
      var14.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
      final JButton var15 = new JButton(this.optionstr1);
      final JButton var16 = new JButton(this.optionstr2);
      final JButton var17 = new JButton("Mezőinformációk");
      var17.setIcon(UIManager.getIcon("Table.descendingSortIcon"));
      var14.add(var17);
      var14.add(Box.createHorizontalGlue());
      var14.add(var15);
      var14.add(Box.createHorizontalStrut(10));
      var14.add(var16);
      var15.setDefaultCapable(true);
      final JPanel var18 = new JPanel(new BorderLayout());
      var18.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 20));
      JScrollPane var19 = new JScrollPane(var8, 20, 30);
      var19.setSize(new Dimension((int)var8.getPreferredSize().getWidth(), Math.min(6 * GuiUtil.getCommonItemHeight(), (int)(40.0D + 1.2D * var8.getPreferredSize().getHeight()))));
      var19.setPreferredSize(var19.getSize());
      var19.setBorder((Border)null);
      var18.add(var19, "Center");
      var16.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            var11.setVisible(false);
         }
      });
      var15.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            var11.setVisible(false);
            var5[0] = new Boolean(false);
         }
      });
      var17.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            int var2;
            if (!PageViewer.this.detail_mode) {
               var18.add(var9, "South");
               var17.setIcon(UIManager.getIcon("Table.ascendingSortIcon"));
               var2 = var18.getPreferredSize().height;
               var11.setSize(var11.getWidth(), var2 + 4 * (GuiUtil.getCommonItemHeight() + 4));
            } else {
               var18.remove(var9);
               var17.setIcon(UIManager.getIcon("Table.descendingSortIcon"));
               var2 = var18.getPreferredSize().height;
               var11.setSize(var11.getWidth(), var2 + 4 * (GuiUtil.getCommonItemHeight() + 4));
            }

            PageViewer.this.detail_mode = !PageViewer.this.detail_mode;
         }
      });
      var11.getContentPane().setLayout(new BorderLayout());
      JLabel var20 = new JLabel();
      var20.setIcon(var3);
      var20.setVerticalAlignment(1);
      var20.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
      var11.getContentPane().add(var18, "Center");
      var11.getContentPane().add(var20, "West");
      var11.getContentPane().add(var14, "South");
      int var21 = var18.getPreferredSize().width;
      int var22 = var18.getPreferredSize().height;
      if (var21 < var10 + 20) {
         var21 = var10 + 20;
      }

      var11.setSize(var21 + 100, var22 + 3 * GuiUtil.getCommonItemHeight());
      var11.setLocationRelativeTo(var1);
      var11.getRootPane().setDefaultButton(var15);
      WindowAdapter var23 = new WindowAdapter() {
         private boolean gotFocus = false;

         public void windowOpened(WindowEvent var1) {
            if (!this.gotFocus) {
               var15.requestFocus();
               this.gotFocus = true;
            }

         }
      };
      var15.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent var1) {
            int var2 = var1.getKeyCode();
            if (var2 == 37) {
               var17.requestFocus();
            }

            if (var2 == 39) {
               var16.requestFocus();
            }

         }
      });
      var16.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent var1) {
            int var2 = var1.getKeyCode();
            if (var2 == 37) {
               var15.requestFocus();
            }

            if (var2 == 39) {
               var17.requestFocus();
            }

            if (var2 == 13) {
               var16.doClick();
            }

         }
      });
      var17.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent var1) {
            int var2 = var1.getKeyCode();
            if (var2 == 37) {
               var16.requestFocus();
            }

            if (var2 == 39) {
               var15.requestFocus();
            }

            if (var2 == 13) {
               var17.doClick();
            }

         }
      });
      var11.addWindowListener(var23);
      var11.setVisible(true);
      PropertyList.getInstance().set("fieldcheckdialog", Boolean.FALSE);
      return (Boolean)var5[0];
   }

   private boolean done_calc_check() {
      String var1 = this.ss.get("gui", "detail");
      boolean var2 = var1 == null ? true : "true".equals(var1);
      Element var3 = null;
      if (MainFrame.conditionvaluefunction) {
         return true;
      } else {
         BookModel var4 = this.PM.getFormModel().getBookModel();
         Elem var5 = (Elem)var4.cc.getActiveObject();
         IDataStore var6 = (IDataStore)var5.getRef();
         String var7 = ((IDataField)this.current_jc).getFieldValue().toString();
         int var8 = ((IDataField)this.current_jc).getRecordIndex();
         String var9 = this.current_df.key;
         Object[] var10 = new Object[]{new Integer(this.dynindex), var9};
         String var11 = var6.get(var10);
         String var13;
         if (MainFrame.FTRmode && MainFrame.FTRdoc != null) {
            try {
               var3 = MainFrame.FTRdoc.createElement("mezo");
               var3.setAttribute("eazon", var9);
               String var12;
               if (0 < this.dynindex) {
                  var12 = "000" + (this.dynindex + 1);
                  var13 = var9.substring(0, 2) + var12.substring(var12.length() - 4) + var9.substring(6);
                  var3.setAttribute("dyneazon", var13);
               }

               var12 = ((IDataField)this.current_jc).getFieldValueWOMask().toString();
               var13 = var12;
               if ("false".equals(var12)) {
                  var13 = "";
               }

               if ("true".equals(var13)) {
                  var13 = "X";
               }

               Text var14 = MainFrame.FTRdoc.createTextNode(var13);
               var3.appendChild(var14);
               MainFrame.FTRmezok.appendChild(var3);
            } catch (DOMException var37) {
            }
         }

         boolean var38 = false;
         if (var11 != null && var11.equals(var7)) {
            var38 = true;
         }

         if (var11 == null && "".equals(var7)) {
            var38 = true;
         }

         if (!var38) {
            var6.set(var10, var7);
         }

         if (this.cm.FillGroupFields(this.PM.getFormModel().id, var6, this.current_df, var8, this.dynindex)) {
            this.refresh();
         }

         var13 = this.dynindex + "_" + var9;
         var6.getMasterCaseId(var13);
         var4.setCalcelemindex(var4.cc.getActiveObjectindex());
         if (!var38) {
            this.done_set_history(var13, var7, var9);
            String var39 = this.ss.get("gui", "mezőszámítás");
            if (var39 != null && var39.equals("true")) {
               try {
                  ((GUI_Datastore)var6).inkihatas = true;
                  this.cm.calc_field(var5.getType(), var9, this.dynindex, var13);
                  ((GUI_Datastore)var6).inkihatas = false;
               } catch (Exception var36) {
               }

               this.refresh();
            }

            if (MainFrame.rogzitomode && this.cm.get_rogz_calc_fids_list(this.PM.getFormModel().id).containsKey(var9)) {
               try {
                  this.cm.calc_field(var5.getType(), var9, this.dynindex, var13);
               } catch (Exception var35) {
               }

               this.refresh();
            }

            MainFrame.thisinstance.mp.getDMFV().fv.setTabStatus();
         }

         if (("2".equals(MainFrame.role) || "3".equals(MainFrame.role)) && MainFrame.ellvitamode) {
            if (this.nocheck) {
               if (this.forintResult == null) {
                  this.forintResult = this.forintos(this.current_df);
               }
            } else {
               this.forintResult = this.forintos(this.current_df);
            }

            if (!this.forintResult.isOk()) {
               if (!this.veto_kihatas_dialog && !this.kihatas_dialog_shown) {
                  this.kihatas_dialog_shown = true;
                  if (!this.done_char_kihatas(this.current_df, var7)) {
                     return false;
                  }
               } else if (this.veto_kihatas_dialog && !this.nocheck && this.forintResult.errorList.size() > 0) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, this.forintResult.errorList.elementAt(0), "Hiba!", 0);
               }
            }
         }

         if (MainFrame.rogzitomode) {
            return true;
         } else {
            Boolean var40 = (Boolean)PropertyList.getInstance().get("veto_check");
            if (var40 == null) {
               var40 = Boolean.FALSE;
            }

            String var15 = this.ss.get("gui", "mezőellenőrzés");
            if (var15 != null && var15.equals("true")) {
               Object[] var16 = new Object[0];

               try {
                  var16 = this.cm.check_field(var9, this.dynindex, var13, var11);
               } catch (Exception var34) {
               }

               this.refresh();
               boolean var17 = true;
               String var18 = "";
               String var19 = "";
               Integer var20 = IErrorList.LEVEL_SHOW_ERROR;
               if (var16[2] != null && var16[2] instanceof Boolean) {
                  var17 = (Boolean)var16[2];
                  var18 = (String)var16[4];
                  var19 = var18;
                  if (var18 != null) {
                     var18 = var18.replaceAll("#13", "\n");
                  }

                  try {
                     var20 = (Integer)var16[5];
                  } catch (Exception var33) {
                     var20 = IErrorList.LEVEL_SHOW_ERROR;
                  }
               }

               if (var20 == null) {
                  var20 = IErrorList.LEVEL_SHOW_ERROR;
               }

               if (!var40 && !var17 && !this.nocheck) {
                  PropertyList.getInstance().set("fieldcheckdialog", Boolean.TRUE);
                  Container var21 = null;

                  try {
                     var21 = this.getRootPane().getTopLevelAncestor();
                  } catch (Exception var32) {
                     Tools.eLog(var32, 0);
                  }

                  if (var21 == null) {
                     PropertyList.getInstance().set("fieldcheckdialog", Boolean.FALSE);
                     return true;
                  }

                  final JButton var22 = new JButton(this.optionstr1);
                  final JButton var23 = new JButton(this.optionstr2);
                  Object[] var24 = new Object[]{var22, var23};
                  final myJOptionPane var26 = new myJOptionPane(var18, 0, !var20.equals(IErrorList.LEVEL_ERROR) && !var20.equals(IErrorList.LEVEL_SHOW_ERROR) && !var20.equals(IErrorList.LEVEL_FATAL_ERROR) && !var20.equals(IErrorList.LEVEL_SHOW_FATAL_ERROR) ? 2 : 0, (Icon)null, var24, var24[0]);
                  Element finalVar = var3;
                  var22.addActionListener(new ActionListener() {
                     public void actionPerformed(ActionEvent var1) {
                        if (finalVar != null) {
                           finalVar.setAttribute("button", "Javít");
                        }

                        var26.setValue(var1.getSource());
                     }
                  });
                  Element finalVar1 = var3;
                  var23.addActionListener(new ActionListener() {
                     public void actionPerformed(ActionEvent var1) {
                        if (finalVar1 != null) {
                           finalVar1.setAttribute("button", "Tovább");
                        }

                        var26.setValue(var1.getSource());
                     }
                  });
                  var22.addKeyListener(new KeyAdapter() {
                     public void keyPressed(KeyEvent var1) {
                        int var2 = var1.getKeyCode();
                        if (var2 == 37 || var2 == 39) {
                           var23.requestFocus();
                        }

                     }
                  });
                  var23.addKeyListener(new KeyAdapter() {
                     public void keyPressed(KeyEvent var1) {
                        int var2 = var1.getKeyCode();
                        if (var2 == 37 || var2 == 39) {
                           var22.requestFocus();
                        }

                     }
                  });
                  Icon var27 = UIManager.getIcon("OptionPane.warningIcon");
                  Icon var28 = UIManager.getIcon("OptionPane.errorIcon");
                  Icon var29 = !var20.equals(IErrorList.LEVEL_ERROR) && !var20.equals(IErrorList.LEVEL_SHOW_ERROR) && !var20.equals(IErrorList.LEVEL_FATAL_ERROR) && !var20.equals(IErrorList.LEVEL_SHOW_FATAL_ERROR) ? var27 : var28;
                  if (var3 != null) {
                     var3.setAttribute("dialog", "igen");
                     var3.setAttribute("msg", var19);
                     var3.setAttribute("msglevel", var29.equals(var27) ? "warning" : "error");
                  }

                  String var30 = !var20.equals(IErrorList.LEVEL_ERROR) && !var20.equals(IErrorList.LEVEL_SHOW_ERROR) && !var20.equals(IErrorList.LEVEL_FATAL_ERROR) && !var20.equals(IErrorList.LEVEL_SHOW_FATAL_ERROR) ? this.title4 : this.title3;
                  if (var2) {
                     return this.done_calc_check_detail((Frame)var21, var18, var29, var30);
                  }

                  int var31 = myJOptionPane.showOptionDialog(var26, var21, var18, !var20.equals(IErrorList.LEVEL_ERROR) && !var20.equals(IErrorList.LEVEL_SHOW_ERROR) && !var20.equals(IErrorList.LEVEL_FATAL_ERROR) && !var20.equals(IErrorList.LEVEL_SHOW_FATAL_ERROR) ? this.title4 : this.title3, 0, !var20.equals(IErrorList.LEVEL_ERROR) && !var20.equals(IErrorList.LEVEL_SHOW_ERROR) && !var20.equals(IErrorList.LEVEL_FATAL_ERROR) && !var20.equals(IErrorList.LEVEL_SHOW_FATAL_ERROR) ? 2 : 0, var29, var24, var24[0]);
                  PropertyList.getInstance().set("fieldcheckdialog", Boolean.FALSE);
                  if (var31 == 0) {
                     return false;
                  }
               } else if (var3 != null) {
                  var3.setAttribute("dialog", "nem");
               }
            }

            return true;
         }
      }
   }

   public void refresh() {
      this.done_fill_on();
      Vector var1 = this.PM.y_sorted_df;
      GUI_Datastore var2 = this.getDatastore();

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         DataFieldModel var4 = (DataFieldModel)var1.get(var3);
         String var5 = var2.get(this.dynindex + "_" + var4.key);

         try {
            StoreItem var6 = (StoreItem)var2.getMasterCaseId(this.dynindex + "_" + var4.key);
            if (var6.getDetails() != null) {
               var4.features.put("details", "1");
            } else {
               var4.features.put("details", "0");
            }
         } catch (Exception var7) {
         }

         if (var5 == null) {
            var5 = "";
         }

         var4.value = var5;
      }

      this.cm.page_fields_visibility_calc(this.PM.getFormModel().id, this.PM.pid, new Integer(this.dynindex));
      this.repaint();
   }

   public void done_fill_on() {
      if (!MainFrame.thisinstance.mp.isReadonlyMode()) {
         if (this.PM.dynamic) {
            boolean var1 = false;
            String var2 = this.ss.get("gui", "mezőszámítás");
            if (var2 != null && var2.equals("false")) {
               var1 = true;
            }

            Vector var3 = this.PM.y_sorted_df;

            for(int var4 = 0; var4 < var3.size(); ++var4) {
               DataFieldModel var5 = (DataFieldModel)var3.get(var4);
               if (var5.orig_readonly) {
                  if (var5.writeable && var1) {
                     var5.readonly = false;
                  }
               } else {
                  String var6 = (String)var5.features.get("fill_on_fp");
                  boolean var7 = false;

                  try {
                     var7 = var6.equals("True");
                  } catch (Exception var12) {
                  }

                  String var8 = (String)var5.features.get("fill_on_lp");
                  boolean var9 = false;

                  try {
                     var9 = var8.equals("True");
                  } catch (Exception var11) {
                  }

                  if (var9 || var7) {
                     var5.readonly = false;
                  }

                  if (!var1) {
                     if (0 < this.dynindex && this.dynindex < this.lapcount - 1) {
                        if (var9 || var7) {
                           var5.readonly = true;
                        }
                     } else {
                        if (this.dynindex == 0 && this.lapcount != 1 && var9) {
                           var5.readonly = true;
                        }

                        if (this.dynindex == this.lapcount - 1 && this.lapcount != 1 && var7) {
                           var5.readonly = true;
                        }
                     }
                  }
               }
            }

         }
      }
   }

   private GUI_Datastore getDatastore() {
      GUI_Datastore var1 = (GUI_Datastore)((Elem)this.PM.getFormModel().getBookModel().cc.getActiveObject()).getRef();
      return var1;
   }

   protected void paintComponent(Graphics var1) {
      try {
         super.paintComponent(var1);
         int var2 = this.PM.z_sorted_vf.size();

         int var3;
         for(var3 = 0; var3 < var2; ++var3) {
            ((VisualFieldModel)this.PM.z_sorted_vf.get(var3)).paint(var1);
         }

         var2 = this.PM.y_sorted_df.size();

         for(var3 = 0; var3 < var2; ++var3) {
            DataFieldModel var4 = (DataFieldModel)this.PM.y_sorted_df.get(var3);
            Color var5 = this.get_fieldbg(var4);
            var4.setBgColor(var5);
            var4.paint(var1, (int)(this.z * 100.0D), this.dynindex);
         }
      } catch (Exception var6) {
      }

   }

   public void addLap() {
      ++this.dynindex;
      ++this.lapcount;
      this.updateLapcount();
   }

   public void addLap(int var1) {
      ++this.dynindex;
      this.lapcount += var1;
      this.updateLapcount();
   }

   public void addLapafterall() {
      this.dynindex = this.lapcount++;
      this.updateLapcount();
   }

   private void updateLapcount() {
      Elem var1 = (Elem)this.PM.getFormModel().getBookModel().cc.getActiveObject();
      int[] var2 = (int[])((int[])var1.getEtc().get("pagecounts"));
      int var3 = this.PM.getFormModel().get(this.PM);
      var2[var3] = this.lapcount;
   }

   public void removeLap() {
      if (this.lapcount > 1) {
         --this.lapcount;
         this.updateLapcount();
         if (this.dynindex + 1 > this.lapcount) {
            --this.dynindex;
         }
      }

   }

   public void setActivelap(int var1) {
      this.dynindex = var1;
   }

   private int zoomCorrection(int var1) {
      int var2 = var1;
      Iterator var3 = this.PM.y_sorted_df.iterator();

      while(true) {
         DataFieldModel var5;
         String var6;
         do {
            if (!var3.hasNext()) {
               return var1;
            }

            Object var4 = var3.next();
            var5 = (DataFieldModel)var4;
            var6 = (String)var5.features.get("datatype");
         } while(!"tatext".equals(var6) && !"scrolltatext".equals(var6));

         Font var7 = (Font)var5.features.get("font");
         int var8 = var7.getSize();
         float var9 = 5.0F;
         double var10 = (double)var1 / 100.0D;
         Font var12 = var7.deriveFont((float)(var10 * (double)var7.getSize2D()));
         var12 = (float)var12.getSize() > var9 ? var12 : var7.deriveFont(var9);
         int var13 = var12.getSize();
         var1 = (int)((double)var13 / (double)var8 * 100.0D);
         System.out.println("old_size = " + var8 + ", new_size = " + var13 + " -> factor = " + var1 + " (" + var2 + ")");
      }
   }

   public void zoom(int var1) {
      this.z = (double)var1 / 100.0D;
      int var2 = (int)((double)this.origsize.width * this.z);
      int var3 = (int)((double)this.origsize.height * this.z);
      this.setSize(var2, var3);
      this.setPreferredSize(new Dimension(var2, var3));
      Vector var4 = this.PM.y_sorted_df;

      int var5;
      Rectangle var7;
      int var8;
      int var9;
      int var10;
      int var11;
      Rectangle var12;
      for(var5 = 0; var5 < var4.size(); ++var5) {
         DataFieldModel var6 = (DataFieldModel)var4.get(var5);
         var7 = var6.getOriginalBounds();
         var8 = (int)Math.ceil((double)var7.x * this.z);
         var9 = (int)Math.ceil((double)var7.y * this.z);
         var10 = (int)((double)var7.width * this.z);
         var11 = (int)((double)var7.height * this.z);
         var12 = new Rectangle(var8, var9, var10, var11);
         var6.setBounds(var12);

         try {
            Font var13 = var6.getOriginalFont();
            if (var13 == null) {
               System.out.println("df=" + var6.key + "\nfeatures=" + var6.features);
            }

            AffineTransform var14 = new AffineTransform();
            var14.scale(this.z, this.z);
            var6.setFont(var13.deriveFont(var14));
         } catch (Exception var15) {
            Tools.eLog(var15, 0);
         }
      }

      var4 = this.PM.z_sorted_vf;

      for(var5 = 0; var5 < var4.size(); ++var5) {
         VisualFieldModel var16 = (VisualFieldModel)var4.get(var5);
         var7 = var16.getOriginalBounds();
         var8 = (int)((double)var7.x * this.z);
         var9 = (int)((double)var7.y * this.z);
         var10 = (int)((double)var7.width * this.z);
         var11 = (int)((double)var7.height * this.z);
         var12 = new Rectangle(var8, var9, var10, var11);
         var16.setBounds(var12);
         AffineTransform var17 = new AffineTransform();
         var17.scale(this.z, this.z);
         var16.setFontsize(this.z);
         var16.setZoom(this.z);
      }

      if (this.current_jc != null) {
         this.current_jc.setBounds(this.current_df.r);
         ((IDataField)this.current_jc).setZoom(var1);
      }

      this.repaint();
   }

   public Dimension getPreferredScrollableViewportSize() {
      return null;
   }

   public int getScrollableUnitIncrement(Rectangle var1, int var2, int var3) {
      return 30;
   }

   public int getScrollableBlockIncrement(Rectangle var1, int var2, int var3) {
      return var1.height;
   }

   public boolean getScrollableTracksViewportWidth() {
      return false;
   }

   public boolean getScrollableTracksViewportHeight() {
      return false;
   }

   public void setFocus(DataFieldModel var1) {
      this.change_component(var1);
   }

   public int getPageIndexByFid(String var1) {
      return this.PM.getFormModel().get_field_pageindex(var1);
   }

   public void setFocus(String var1) {
      DataFieldModel var2 = this.PM.getByCode(var1);
      this.change_component(var2);
   }

   public boolean isFocusable(String var1) {
      return this.PM.getByCode(var1) != null;
   }

   public PageModel getPM() {
      return this.PM;
   }

   public void destroy() {
      if (this.PM != null) {
         this.PM.destroy();
      }

      this.PM = null;
      this.current_df = null;
      this.cm = null;
      this.ss = null;
   }

   public boolean done_details(DataFieldModel var1) {
      if (var1 == null) {
         var1 = this.current_df;
      }

      if (var1 == null) {
         return false;
      } else if (!DataChecker.getInstance().forintE(var1.features)) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A funkció nem elérhető!", "Üzenet", 0);
         return false;
      } else {
         BookModel var2 = this.PM.getFormModel().getBookModel();
         Elem var3 = (Elem)var2.cc.getActiveObject();
         IDataStore var4 = (IDataStore)var3.getRef();
         String var5 = this.dynindex + "_" + var1.key;
         String var6 = var4.get(var5);
         if (var6 == null) {
            var4.set(var5, "");
         }

         String var7 = "";

         try {
            var7 = ((IDataField)this.current_jc).getFieldValue().toString();
         } catch (Exception var10) {
            var7 = "";
         }

         var4.set(var5, var7);
         StoreItem var8 = (StoreItem)var4.getMasterCaseId(var5);
         new DetailsDialog(var1, var8);
         this.refresh();
         var8.value = var7;
         return true;
      }
   }

   private boolean done_fieldhistory(DataFieldModel var1) {
      DataFieldModel var2 = var1;
      if (var1 == null) {
         var2 = this.current_df;
         this.nocheck = true;
         this.done_calc_check();
         String var3 = this.ss.get("gui", "mezőszámítás");
         if (var3 != null && var3.equals("true") && this.cm.isFoAdatDependency(this.PM.getFormModel().id, this.current_df.key)) {
            try {
               PropertyList.getInstance().set("foadatcalculation", true);
               this.handleCalcFoadat();
               this.refresh();
            } finally {
               PropertyList.getInstance().set("foadatcalculation", false);
            }
         }

         this.nocheck = false;
      }

      BookModel var24 = this.PM.getFormModel().getBookModel();
      Elem var4 = (Elem)var24.cc.getActiveObject();
      IDataStore var5 = (IDataStore)var4.getRef();
      Datastore_history var6 = (Datastore_history)var4.getEtc().get("history");
      if (var6 == null) {
         return false;
      } else {
         String var8 = var2.key;
         String var7;
         if (var1 == null) {
            String var9 = ((IDataField)this.current_jc).getFieldValue().toString();
            Object[] var10 = new Object[]{new Integer(this.dynindex), var8};
            String var11 = var5.get(var10);
            boolean var12 = false;
            if (var11 != null && var11.equals(var9)) {
               var12 = true;
            }

            var5.set(var10, var9);
            var7 = this.dynindex + "_" + var8;
            var5.getMasterCaseId(var7);
         } else {
            var7 = this.dynindex + "_" + var8;
         }

         DefaultTableModel var25 = var6.getDTM(var7);
         if (var25 == null) {
            return false;
         } else {
            final String var26 = this.getWebTextareaContentFromHistory(var24, var6, var7, this.dynindex);
            JTable var27 = new JTable(var25);
            var27.setEnabled(false);
            TableColumn var28 = var27.getColumnModel().getColumn(1);
            var28.setCellRenderer(new DefaultTableCellRenderer() {
               public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
                  Component var7 = super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
                  if ("true".equals(var2)) {
                     ((DefaultTableCellRenderer)var7).setText("X");
                  }

                  if ("false".equals(var2)) {
                     ((DefaultTableCellRenderer)var7).setText("");
                  }

                  Color var8 = Color.WHITE;
                  if (var5 == 1) {
                     var8 = PropertyList.OFFICER_COLOR;
                  }

                  if (var5 == 2) {
                     var8 = PropertyList.REVIZOR_COLOR;
                  }

                  var7.setBackground(var8);
                  return var7;
               }
            });
            JScrollPane var13 = new JScrollPane(var27);
            var7 = var8 + "(" + this.dynindex + ")";
            final JDialog var14 = new JDialog(MainFrame.thisinstance, this.dtitle1 + var7 + this.dtitle2, true);
            var14.getContentPane().add(var13);
            JButton var15 = new JButton(this.okstr);
            var15.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent var1) {
                  var14.hide();
               }
            });
            KeyStroke var16 = KeyStroke.getKeyStroke(27, 0);
            ActionListener var17 = new ActionListener() {
               public void actionPerformed(ActionEvent var1) {
                  var14.setVisible(false);
               }
            };
            var14.getRootPane().registerKeyboardAction(var17, var16, 2);
            if (!"".equals(var26)) {
               try {
                  JPanel var18 = new JPanel();
                  var18.add(var15, "West");
                  JButton var19 = new JButton("WEB tartalom");
                  var19.addActionListener(new ActionListener() {
                     public void actionPerformed(ActionEvent var1) {
                        try {
                           PageViewer.this.showOriginalTextAreaText(var26);
                        } catch (Exception var3) {
                           var3.printStackTrace();
                        }

                     }
                  });
                  var18.add(var19, "East");
                  var14.getContentPane().add(var18, "South");
               } catch (Exception var22) {
                  var14.getContentPane().add(var15, "South");
               }
            } else {
               var14.getContentPane().add(var15, "South");
            }

            var14.setSize(400, 200);
            var14.setLocationRelativeTo(MainFrame.thisinstance);
            var14.show();
            return true;
         }
      }
   }

   private boolean done_field_combolista(DataFieldModel var1, Point var2) {
      var1.features.put("_dynindex_", this.dynindex);
      Vector var3 = Tools.getComboList(var1);
      JDialog var4 = new JDialog(MainFrame.thisinstance, "Értéklista a " + var1.key + " mezőhöz.", true);
      JPanel var5 = new JPanel(new BorderLayout());
      JList var6 = new JList(var3);
      String[] var7 = (String[])((String[])var1.features.get("spvalues"));
      int var8 = -1;

      for(int var9 = 0; var9 < var7.length; ++var9) {
         if (var7[var9].equals(var1.value.toString())) {
            var8 = var9;
            break;
         }
      }

      if (var8 != -1) {
         var6.setSelectedIndex(var8);
      }

      JScrollPane var10 = new JScrollPane(var6);
      var10.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
      var5.add(var10, "Center");
      var4.getContentPane().add(var5);
      var4.setSize(290, 150);
      var4.setLocation(var2);
      var6.ensureIndexIsVisible(var8);
      var4.setVisible(true);
      return true;
   }

   private boolean done_field_kihatas(DataFieldModel var1) {
      if (this.veto_kihatas_dialog) {
         return true;
      } else {
         DataFieldModel var2 = var1;
         if (var1 == null) {
            var2 = this.current_df;
            this.nocheck = true;
            this.done_calc_check();
            String var3 = this.ss.get("gui", "mezőszámítás");
            if (var3 != null && var3.equals("true") && this.cm.isFoAdatDependency(this.PM.getFormModel().id, this.current_df.key)) {
               try {
                  PropertyList.getInstance().set("foadatcalculation", true);
                  this.handleCalcFoadat();
                  this.refresh();
               } finally {
                  PropertyList.getInstance().set("foadatcalculation", false);
               }
            }

            this.nocheck = false;
         }

         final int var41 = ABEVFunctionSet.getInstance().getPrecisionForKihatas(var1.key);
         final Hashtable var4 = var1.features;
         BookModel var5 = this.PM.getFormModel().getBookModel();
         Elem var6 = (Elem)var5.cc.getActiveObject();
         final IDataStore var7 = (IDataStore)var6.getRef();
         Datastore_history var8 = (Datastore_history)var6.getEtc().get("history");
         Object[] var9 = (Object[])((Object[])var6.getEtc().get("dbparams"));
         String var10 = null;

         try {
            var10 = (String)var9[3];
         } catch (Exception var39) {
            var10 = null;
         }

         if (var8 == null) {
            var8 = new Datastore_history();
         }

         final Kihatasstore var12 = (Kihatasstore)var6.getEtc().get("kihatas");
         final String var14 = var2.key;
         Object[] var15 = new Object[]{new Integer(this.dynindex), var14};
         String var16 = var7.get(var15);
         final String var13;
         if (var1 == null) {
            String var17 = ((IDataField)this.current_jc).getFieldValue().toString();
            boolean var18 = false;
            if (var16 != null && var16.equals(var17)) {
               var18 = true;
            }

            var7.set(var15, var17);
            var13 = this.dynindex + "_" + var14;
            var7.getMasterCaseId(var13);
         } else {
            var13 = this.dynindex + "_" + var14;
         }

         Vector var42 = var8.get(var13);
         if (var42 == null) {
            var42 = new Vector();
            var42.setSize(4);
         }

         final String var19 = var1.getAdonem(var7, this.dynindex);
         final KihatasTableModel var20 = var12.get(var13);
         String var21 = (String)var20.getValueAt(0, 10);
         if ("C".equals(var21) && !var20.maySimple()) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem csoportos az adónem a sablonban, de csoportos kihatás tartozik a mezőhöz. Kérem javítsa!", "Üzenet", 0);
            new KihatasCsopDialog(this, this.PM, var1, this.dynindex, this.megallapitaslista, true);
            return true;
         } else {
            final KihatasTableModel var22 = KihatasTableModel.make_copy(var20);
            final JTable var23 = new JTable(var20) {
               public boolean isCellEditable(int var1, int var2) {
                  if (var2 == 0) {
                     return false;
                  } else if (var2 == 1) {
                     return true;
                  } else if (var2 == 2 && this.getValueAt(var1, 1) != null && ((MegallapitasVector)this.getValueAt(var1, 1)).vannemtorolt()) {
                     KihatasRecord var3 = (KihatasRecord)var20.get(var1);
                     String var4 = ((MegallapitasRecord)var3.getMegallapitasVector().get(0)).getMsvo_azon();
                     Vector var5 = PageViewer.this.megallapitaslista.getAdnlistByMsvoAzon(var4);
                     String var6 = "";
                     if (var5.size() != 0) {
                        var6 = (String)var5.get(0);
                     }

                     return var5.size() >= 2;
                  } else {
                     return var2 == 3 && this.getValueAt(var1, 1) != null && ((MegallapitasVector)this.getValueAt(var1, 1)).vannemtorolt();
                  }
               }

               public Component prepareRenderer(TableCellRenderer var1, int var2, int var3) {
                  Component var4 = super.prepareRenderer(var1, var2, var3);
                  DefaultTableCellRenderer var5 = (DefaultTableCellRenderer)var4;
                  String var6;
                  if (var3 == 3) {
                     var5.setHorizontalAlignment(4);

                     try {
                        var6 = var5.getText();
                        double var7 = Double.parseDouble(var6);
                        NumberFormat var9 = NumberFormat.getNumberInstance();
                        var9.setMaximumFractionDigits(var41);
                        var6 = var9.format(var7);
                        var5.setText(var6);
                     } catch (NumberFormatException var12) {
                     }
                  } else {
                     var5.setHorizontalAlignment(var3 == 1 ? 2 : 4);
                  }

                  var5.setForeground(Color.BLACK);
                  String var10;
                  if (var3 == 1) {
                     Object var13 = this.getValueAt(var2, var3);
                     if (!(var13 instanceof MegallapitasVector)) {
                        return var5;
                     }

                     MegallapitasVector var14 = (MegallapitasVector)var13;
                     if (var14.size() == 0) {
                        return var5;
                     }

                     MegallapitasRecord var8 = (MegallapitasRecord)var14.get(0);
                     String var17 = (String)this.getValueAt(var2, 2);
                     var10 = var8.getMsvo_azon();
                     if (!PageViewer.this.megallapitaslista.checkdata(var17, var10)) {
                        var5.setForeground(Color.RED);
                     }
                  }

                  if (var3 == 2) {
                     var6 = var5.getText();
                     Object var15;
                     if ("".equals(var6)) {
                        var15 = this.getValueAt(var2, 0);
                        if (!"".equals(var15)) {
                           var5.setText("Üres");
                           var5.setForeground(Color.RED);
                        }
                     }

                     var15 = this.getValueAt(var2, 1);
                     if (!(var15 instanceof MegallapitasVector)) {
                        return var5;
                     }

                     MegallapitasVector var16 = (MegallapitasVector)var15;
                     if (var16.size() == 0) {
                        return var5;
                     }

                     MegallapitasRecord var18 = (MegallapitasRecord)var16.get(0);
                     var10 = (String)this.getValueAt(var2, 2);
                     String var11 = var18.getMsvo_azon();
                     if (!PageViewer.this.megallapitaslista.checkdata(var10, var11)) {
                        var5.setForeground(Color.RED);
                     }
                  }

                  return var4;
               }

               public Component prepareEditor(TableCellEditor var1, int var2, int var3) {
                  Component var4 = super.prepareEditor(var1, var2, var3);
                  if (var3 == 1 && var4 instanceof JComboBox) {
                     JComboBox var5 = (JComboBox)var4;
                     MegallapitasVector var6 = (MegallapitasVector)this.getValueAt(var2, var3);
                     var5.putClientProperty("data", var6);

                     try {
                        MegallapitasRecord var7 = (MegallapitasRecord)var6.get(0);
                        int var8 = PageViewer.this.megallapitaslista.getIndexByMsvo(var7.getMsvo_azon());
                        if (var8 != -1) {
                           var5.setSelectedIndex(var8);
                        }
                     } catch (Exception var12) {
                     }
                  }

                  if (var3 == 3) {
                     if (var4 instanceof JTextField) {
                        JTextField var15 = (JTextField)var4;
                        String var17 = var15.getText();
                        PlainDocument var19 = new PlainDocument() {
                           public void insertString(int var1, String var2, AttributeSet var3) throws BadLocationException {
                              StringBuffer var4 = new StringBuffer(this.getText(0, this.getLength()));
                              var4.insert(var1, var2);
                              String var5 = var4.toString();

                              try {
                                 if (MultiLineTable.stringFitsToDocument(var5, var41)) {
                                    super.insertString(var1, var2, var3);
                                 }
                              } catch (NumberFormatException var7) {
                                 if ("-".equals(var5)) {
                                    super.insertString(var1, var2, var3);
                                 }
                              }

                           }
                        };
                        var15.setDocument(var19);
                        var15.setText(var17);
                     }

                     return var4;
                  } else if (var3 != 2) {
                     return var4;
                  } else {
                     try {
                        MegallapitasVector var14 = (MegallapitasVector)this.getValueAt(var2, 1);
                        JComboBox var16 = (JComboBox)((DefaultCellEditor)var1).getComponent();
                        String var18 = ((MegallapitasRecord)var14.get(0)).getMsvo_azon();
                        MegallapitasComboLista var20x = PageViewer.this.megallapitaslista;
                        Vector var9 = new Vector();

                        for(int var10 = 0; var10 < var20x.size(); ++var10) {
                           MegallapitasComboRecord var11 = (MegallapitasComboRecord)var20x.get(var10);
                           if (var11.getMsvo_azon().equals(var18)) {
                              var9 = var11.getAdonemlista();
                              break;
                           }
                        }

                        var16.setModel(new DefaultComboBoxModel(var9));
                        return super.prepareEditor(var1, var2, var3);
                     } catch (Exception var13) {
                        System.out.println("" + var13);
                        return var4;
                     }
                  }
               }
            };
            final FTKihatasDialog var24 = new FTKihatasDialog(this.PM, this, var1, this.dynindex, var14, var42, var19, var16, var23);
            TableRowSorter var25 = new TableRowSorter(var20);
            var25.setSortable(0, false);
            var25.setSortable(1, false);
            var25.setSortable(2, false);
            var25.setSortable(3, false);
            var25.setRowFilter(new RowFilter<TableModel, Integer>() {
               public boolean include(Entry<? extends TableModel, ? extends Integer> var1) {
                  if (!(var1.getValue(1) instanceof Vector)) {
                     return true;
                  } else {
                     MegallapitasVector var2 = (MegallapitasVector)var1.getValue(1);
                     if (var2.size() == 0) {
                        return true;
                     } else {
                        for(int var3 = 0; var3 < var2.size(); ++var3) {
                           MegallapitasRecord var4 = (MegallapitasRecord)var2.get(var3);
                           if (!var4.isDeleted()) {
                              return true;
                           }
                        }

                        return false;
                     }
                  }
               }
            });
            var23.setRowSorter(var25);
            var23.getColumnModel().removeColumn(var23.getColumnModel().getColumn(10));
            var23.getColumnModel().removeColumn(var23.getColumnModel().getColumn(9));
            var23.getColumnModel().removeColumn(var23.getColumnModel().getColumn(8));
            var23.getColumnModel().removeColumn(var23.getColumnModel().getColumn(7));
            var23.getColumnModel().removeColumn(var23.getColumnModel().getColumn(6));
            var23.getColumnModel().removeColumn(var23.getColumnModel().getColumn(5));
            var23.getColumnModel().removeColumn(var23.getColumnModel().getColumn(4));
            final TableModelListener var26 = new TableModelListener() {
               boolean veto = false;

               public void tableChanged(TableModelEvent var1) {
                  KihatasTableModel var2 = (KihatasTableModel)var1.getSource();
                  if (!var2.hasEmptyRec()) {
                     var2.addEmptyRec();
                  }

                  this.veto = true;
                  int var3 = var23.getSelectedRow();
                  var3 = var23.getRowSorter().convertRowIndexToModel(var3);
                  String var4x = PageViewer.this.getBtablaJel(var4);
                  String var5 = PageViewer.this.getMertekegyseg(var4);
                  var2.updateRec(var3, MultiLineTable.stripformatnumber(var24.getTf_e().getText()), var14, PageViewer.this.dynindex, PageViewer.this.PM.getFormModel().id, var19, "N", var4x, var5);
                  this.veto = false;
                  var24.getTf_ar().setText(MultiLineTable.formatnumber(var2.getSzumma(MultiLineTable.stripformatnumber(var24.getTf_e().getText())), var41));
               }
            };
            var20.addTableModelListener(var26);
            var23.getTableHeader().setReorderingAllowed(false);
            var23.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
            var23.getTableHeader().getColumnModel().getColumn(0).setWidth(0);
            var23.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
            var23.getTableHeader().getColumnModel().getColumn(0).setResizable(false);
            var23.getTableHeader().getColumnModel().getColumn(1).setMinWidth(10);
            var23.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(500);
            var23.getTableHeader().getColumnModel().getColumn(1).setWidth(290);
            var23.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(290);
            var23.setRowHeight(20);
            var23.getSelectionModel().setSelectionInterval(var23.getRowCount() - 1, var23.getRowCount() - 1);
            final MegallapitasComboLista var27 = this.megallapitaslista;
            DefaultComboBoxModel var28 = new DefaultComboBoxModel(var27);
            JComboBox var29 = new JComboBox(var28);
            var29.setEditable(false);
            var29.setRenderer(new DefaultListCellRenderer() {
               public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
                  String var6 = "";
                  if (var2 instanceof MegallapitasComboRecord) {
                     var6 = ((MegallapitasComboRecord)var2).getDisplayText();
                  }

                  return super.getListCellRendererComponent(var1, var6, var3, var4, var5);
               }
            });
            DefaultCellEditor var30 = new DefaultCellEditor(var29) {
               public Object getCellEditorValue() {
                  JComboBox var1 = (JComboBox)this.getComponent();
                  MegallapitasVector var2 = null;

                  try {
                     var2 = (MegallapitasVector)var1.getClientProperty("data");
                  } catch (Exception var6) {
                  }

                  if (var2 == null) {
                     var2 = new MegallapitasVector();
                  }

                  if (var2.size() == 0) {
                     MegallapitasRecord var3 = new MegallapitasRecord("", "", "", "M");
                     var2.add(var3);
                  }

                  MegallapitasComboRecord var7 = (MegallapitasComboRecord)super.getCellEditorValue();
                  ((MegallapitasRecord)var2.get(0)).setMsvo_azon(var7.getMsvo_azon());
                  Vector var4 = var7.getAdonemlista();
                  if (var4.size() == 1) {
                     int var5 = var23.getSelectedRow();
                     var5 = var23.getRowSorter().convertRowIndexToModel(var5);
                     ((KihatasRecord)var20.get(var5)).setAdonemKod((String)var4.get(0));
                  }

                  return var2;
               }
            };
            var23.getTableHeader().getColumnModel().getColumn(1).setCellEditor(var30);
            DefaultTableCellRenderer var31 = new DefaultTableCellRenderer() {
               public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
                  String var7 = "";
                  if (var2 instanceof MegallapitasVector) {
                     MegallapitasVector var8 = (MegallapitasVector)var2;
                     if (var8.size() != 0) {
                        String var9 = ((MegallapitasRecord)var8.get(0)).getMsvo_azon();
                        String var10 = var27.getDisplayTextByMsvoAzon(var9);
                        var7 = var10;
                     }
                  } else {
                     var7 = var27.getDisplayTextByMsvoAzon(var2.toString());
                  }

                  DefaultTableCellRenderer var11 = (DefaultTableCellRenderer)super.getTableCellRendererComponent(var1, var7, var3, var4, var5, var6);
                  return var11;
               }
            };
            var23.getTableHeader().getColumnModel().getColumn(1).setCellRenderer(var31);
            DefaultComboBoxModel var32 = new DefaultComboBoxModel();
            JComboBox var33 = new JComboBox(var32);
            var33.setEditable(false);
            DefaultCellEditor var34 = new DefaultCellEditor(var33);
            var23.getTableHeader().getColumnModel().getColumn(2).setCellEditor(var34);
            DefaultCellEditor var35 = new DefaultCellEditor(new JTextField()) {
               public Object getCellEditorValue() {
                  String var1 = ((JTextField)this.getComponent()).getText();
                  if ("-".equals(var1)) {
                     var1 = "";
                  }

                  return var1;
               }
            };
            var35.setClickCountToStart(1);
            var23.getTableHeader().getColumnModel().getColumn(3).setCellEditor(var35);
            var24.getTf_e().setText(MultiLineTable.formatnumber(var20.getEredeti(var42), var41));
            var24.getSouth().addMouseListener(new MouseAdapter() {
               public void mouseClicked(MouseEvent var1) {
                  if (var1.getClickCount() == 2) {
                     PageViewer.this.done_debug_info(var20);
                  }

               }
            });
            var24.addWindowListener(new WindowAdapter() {
               public void windowOpened(WindowEvent var1) {
                  String var2 = var20.checkBtablajel(var24.getBjel_sablon());
                  String var3 = var20.checkMertekegyseg(var24.getMe_sablon());
                  String var4 = var2 + var3;
                  String var5 = MultiLineTable.stripformatnumber(var24.getTf_e().getText());

                  try {
                     new BigDecimal(var5);
                  } catch (Exception var15) {
                     new BigDecimal(0);
                  }

                  String var7 = MultiLineTable.stripformatnumber(var24.getTf_a().getText());
                  String var8 = MultiLineTable.stripformatnumber(var24.getTf_ad().getText());

                  try {
                     new BigDecimal(var7);
                  } catch (Exception var14) {
                     try {
                        new BigDecimal(var8);
                     } catch (Exception var13) {
                        new BigDecimal(0);
                     }
                  }

                  String var10 = var20.ComputeRevValue();
                  String var11 = MultiLineTable.stripformatnumber(var24.getTf_ar().getText());
                  if (!"".equals(var10) && !PageViewer.equalsIgnoreDecimal(var10, var11)) {
                     var24.getTf_ar().setForeground(Color.RED);
                     String var12 = "A számított érték (" + var10 + ") és az alap érték (" + var11 + ") eltérést mutat!\nA különbözet automatikusan javításra kerül.";
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, var12 + var4, "Figyelmeztetés", 1);
                     var24.getTf_ar().setText(MultiLineTable.formatnumber(var10, var41));
                     var24.getTf_ar().setForeground(Color.BLACK);
                  } else if (var4.length() != 0) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, var4, "Figyelmeztetés", 1);
                  }

                  var20.setBtablajel(var24.getBjel_sablon());
                  var20.setMertekegyseg(var24.getMe_sablon());
                  var20.setAdattipusKod("N");
               }
            });
            Datastore_history finalVar = var8;
            var24.getOkbtn().addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent var1) {
                  if (var23.isEditing()) {
                     var23.getCellEditor().stopCellEditing();
                  }

                  String var2 = PageViewer.this.check_fill(var20);
                  if (var2 != null) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, var2, "Hibaüzenet", 0);
                  } else {
                     double var3 = 0.0D;

                     try {
                        var3 = Double.parseDouble(MultiLineTable.stripformatnumber(var24.getTf_ar().getText()));
                     } catch (NumberFormatException var6) {
                        return;
                     }

                     if (var3 < 0.0D && !DataChecker.getInstance().lehetEMinusz((String)var4.get("irids"))) {
                        GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az Aktuális revizori érték nem lehet negatív!", "Hibaüzenet", 0);
                     } else if (PageViewer.this.check_valid_adonem(var20, var19)) {
                        GuiUtil.showMessageDialog(MainFrame.thisinstance, "A sablon adoneme miatt hibás adonem szerepel!", "Hibaüzenet", 0);
                     } else {
                        PageViewer.this.kihatas_vege(MultiLineTable.stripformatnumber(var24.getTf_ar().getText()), var7, finalVar, var13, var20, true);
                        var20.setHistory(finalVar.get(var13));
                        var20.removeTableModelListener(var26);
                        var24.setVisible(false);
                     }
                  }
               }
            });
            var24.getCancelbtn().addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent var1) {
                  if (var23.isEditing()) {
                     var23.getCellEditor().stopCellEditing();
                  }

                  var12.set(var13, var22);
                  var20.removeTableModelListener(var26);
                  var24.setVisible(false);
               }
            });
            var24.getDelbtn().addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent var1) {
                  int var2 = var23.getSelectedRow();
                  if (var2 == -1) {
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, "Nem választott sort!", "Hibaüzenet", 0);
                  } else {
                     var2 = var23.getRowSorter().convertRowIndexToModel(var2);
                     var23.clearSelection();
                     var20.done_delete(var2);
                     var24.getTf_ar().setText(MultiLineTable.formatnumber(var20.getSzumma(MultiLineTable.stripformatnumber(var24.getTf_e().getText())), var41));
                     var23.tableChanged(new TableModelEvent(var20));
                  }
               }
            });
            var24.setVisible(true);
            return true;
         }
      }
   }

   public static String convertbjel(String var0) {
      Hashtable var1 = new Hashtable();
      var1.put("A", "Adóalap");
      var1.put("AP", "Adóalapot növelő");
      var1.put("AM", "Adóalapot csökkentő");
      var1.put("O", "Adóösszeg");
      var1.put("OP", "Adóösszeget növelő");
      var1.put("OM", "Adóösszeget csökkentő");
      var1.put("E", "Adózás előtti eredmény");
      var1.put("EP", "Adózás előtti eredményt növelő");
      var1.put("EM", "Adózás előtti eredményt csökkentő");
      var1.put("K", "Következő időszakra átvihető követelés");
      var1.put("F", "Fizetendő adó");
      var1.put("V", "Visszaigényelhető adó");
      var1.put("AT", "Adó terhére utalandó összeg");
      String var2 = (String)var1.get(var0);
      if (var2 == null) {
         var2 = "";
      }

      return var2;
   }

   private boolean check_valid_adonem(KihatasTableModel var1, String var2) {
      if ("".equals(var2)) {
         return false;
      } else {
         for(int var3 = 0; var3 < var1.size(); ++var3) {
            KihatasRecord var4 = (KihatasRecord)var1.get(var3);
            if (var4.getMegallapitasVector().isDeleted()) {
               return false;
            }

            if (var2.indexOf(var4.getAdonemKod()) == -1) {
               return true;
            }
         }

         return false;
      }
   }

   private String check_fill(KihatasTableModel var1) {
      for(int var2 = 0; var2 < var1.getRowCount(); ++var2) {
         BigDecimal var3 = ((KihatasRecord)var1.get(var2)).getModositoOsszegValue2();
         int var4 = ((KihatasRecord)var1.get(var2)).getMegallapitasVector().size();
         if (var4 != 0 && var3 == null) {
            return "Nincs kitöltve a módosító összeg!";
         }
      }

      return null;
   }

   private void done_debug_info(KihatasTableModel var1) {
      JDialog var2 = new JDialog(MainFrame.thisinstance, "Info", true);
      JTable var3 = new JTable(var1);
      var2.getContentPane().add(new JScrollPane(var3));
      var2.setSize(1000, 300);
      var2.setVisible(true);
   }

   private String find(Object var1, Vector var2) {
      String var3 = "";

      try {
         for(int var4 = 0; var4 < var2.size(); ++var4) {
            Vector var5 = (Vector)var2.get(var4);
            if (var5.contains(var1)) {
               var3 = var5.get(2) + ". " + var5.get(1);
               break;
            }
         }
      } catch (Exception var6) {
      }

      return var3;
   }

   public String GETVELEM(Vector var1, int var2) {
      try {
         return (String)var1.get(var2);
      } catch (Exception var4) {
         return null;
      }
   }

   public void kihatas_vege(String var1, IDataStore var2, Datastore_history var3, String var4, KihatasTableModel var5, boolean var6) {
      if (!"".equals(var1)) {
         if (var6 && "0".equals(var1) && !var5.vannemtorolt() && "".equals(var3.getHistoryEredeti(var4))) {
            var1 = "";
         }

         var2.set(var4, var1);
         Vector var7 = var3.get(var4);
         if (var7 == null) {
            var7 = new Vector();
            var7.setSize(4);
            if (var1 != null) {
               var7.set(2, var1);
            }

            var3.set(var4, var7);
         } else if (var1 != null) {
            var7.set(2, var1);
         }

         String var8 = this.ss.get("gui", "mezőszámítás");
         if (var8 != null && var8.equals("false")) {
            this.refresh();
         } else {
            try {
               String var9 = var4.split("_", 2)[1];
               ((GUI_Datastore)var2).inkihatas = true;
               this.cm.calc_field(this.PM.getFormModel().id, var9, this.dynindex, var4);
               ((GUI_Datastore)var2).inkihatas = false;
               Object[] var10 = new Object[0];

               try {
                  var10 = this.cm.check_field(var9, this.dynindex, var4);
               } catch (Exception var19) {
               }

               boolean var11 = true;
               String var12 = "";
               Integer var13 = IErrorList.LEVEL_SHOW_ERROR;
               if (var10[2] != null && var10[2] instanceof Boolean) {
                  var11 = (Boolean)var10[2];
                  var12 = (String)var10[4];
                  if (var12 != null) {
                     var12 = var12.replaceAll("#13", "\n");
                  }

                  try {
                     var13 = (Integer)var10[5];
                  } catch (Exception var18) {
                     var13 = IErrorList.LEVEL_SHOW_ERROR;
                  }
               }

               if (var13 == null) {
                  var13 = IErrorList.LEVEL_SHOW_ERROR;
               }

               if (this.nocheck) {
                  return;
               }

               Boolean var14 = (Boolean)PropertyList.getInstance().get("veto_check");
               if (var14 == null) {
                  var14 = Boolean.FALSE;
               }

               if (!var14 && !var11) {
                  PropertyList.getInstance().set("fieldcheckdialog", Boolean.TRUE);
                  Container var15 = null;

                  try {
                     var15 = this.getRootPane().getTopLevelAncestor();
                  } catch (Exception var17) {
                     Tools.eLog(var17, 0);
                  }

                  if (var15 == null) {
                     PropertyList.getInstance().set("fieldcheckdialog", Boolean.FALSE);
                     return;
                  }

                  GuiUtil.showMessageDialog(this, var12, "Hibaüzenet", 0);
                  PropertyList.getInstance().set("fieldcheckdialog", Boolean.FALSE);
               }
            } catch (Exception var20) {
            }

            this.refresh();
         }
      }
   }

   private String getEredeti_old(KihatasTableModel var1, Vector var2) {
      String var3 = (String)var2.get(2);
      if (var3 == null) {
         if (var1.getRowCount() < 2) {
            if (var2.get(1) != null) {
               return (String)var2.get(1);
            }

            if (var2.get(0) != null) {
               return (String)var2.get(0);
            }

            return "0";
         }

         var3 = "0";
      }

      BigDecimal var4;
      try {
         var4 = new BigDecimal(var3);
      } catch (Exception var6) {
         var4 = new BigDecimal(0);
      }

      BigDecimal var5 = var1.getSzumma();
      var5 = var4.subtract(var5);
      return var5.toString();
   }

   private Color get_fieldbg(DataFieldModel var1) {
      Color var2 = null;
      if (!"0".equals(MainFrame.role)) {
         var2 = Color.WHITE;
      }

      try {
         BookModel var3 = this.PM.getFormModel().getBookModel();
         Elem var4 = (Elem)var3.cc.getActiveObject();
         IDataStore var5 = (IDataStore)var4.getRef();
         Datastore_history var6 = (Datastore_history)var4.getEtc().get("history");
         if (var6 == null) {
            return var2;
         } else {
            String var7 = var1.key;
            Object[] var8 = new Object[]{new Integer(this.dynindex), var7};
            String var9 = this.dynindex + "_" + var7;
            String var10 = var5.get(var8);
            if (this.PM.getByCodeAll(var7).type == 1) {
               if (var10.equals("true")) {
                  var10 = "X";
               }

               if (var10.equals("false")) {
                  var10 = "";
               }
            }

            Vector var11 = var6.get(var9);
            if (var11 == null) {
               return var2;
            } else if (var10 == null) {
               return var2;
            } else if (var10.equals(var11.get(2))) {
               return this.checkRevizoriErtek(var9, var10, var6) ? PropertyList.REVIZOR_COLOR : var2;
            } else {
               return var10.equals(var11.get(1)) ? PropertyList.OFFICER_COLOR : var2;
            }
         }
      } catch (Exception var12) {
         return var2;
      }
   }

   public void done_set_history(String var1, String var2, String var3) {
      if (!MainFrame.role.equals("0")) {
         BookModel var4 = this.PM.getFormModel().getBookModel();
         Elem var5 = (Elem)var4.cc.get(var4.getCalcelemindex());
         Datastore_history var6 = (Datastore_history)var5.getEtc().get("history");
         if (var6 != null) {
            Vector var7 = var6.get(var1);
            if (var7 == null) {
               var7 = new Vector();
               var7.setSize(4);
               var6.set(var1, var7);
            }

            byte var8 = 1;
            if (MainFrame.role.equals("2") || MainFrame.role.equals("3")) {
               var8 = 2;
            }

            DataFieldModel var9 = this.PM.getByCodeAll(var3);
            if (var9 != null && var9.type == 1) {
               if (var2.equals("true")) {
                  var2 = "X";
               }

               if (var2.equals("false")) {
                  var2 = "";
               }
            }

            var7.set(var8, var2);
         }
      }
   }

   public void done_set_kihatas(String var1, String var2, String var3, String var4, int var5) {
      if (!MainFrame.role.equals("0")) {
         String var6 = var1.split("_")[1];
         DataFieldModel var7 = this.getDF(var6, var5);
         if (DataChecker.getInstance().forintE(var7.features)) {
            BookModel var8 = this.PM.getFormModel().getBookModel();
            Elem var9 = (Elem)var8.cc.get(var5);
            String var10 = var9.getType();
            Kihatasstore var11 = (Kihatasstore)var9.getEtc().get("kihatas");
            KihatasTableModel var12 = var11.get(var1);
            String var13 = var12.getKihatasEredetiErtek();
            String var14 = "".equals(var13) ? var4 : var13;

            BigDecimal var15;
            try {
               var15 = new BigDecimal(var2);
            } catch (Exception var26) {
               var15 = new BigDecimal(0);
            }

            BigDecimal var16;
            try {
               var16 = new BigDecimal(var14);
            } catch (Exception var25) {
               var16 = new BigDecimal(0);
            }

            BigDecimal var17 = var15.subtract(var16);
            String var18 = var17.toString();
            var12.calculatedfield(var18);
            boolean var19 = false;

            int var27;
            try {
               var27 = Integer.parseInt(var1.substring(0, var1.indexOf("_")));
            } catch (NumberFormatException var24) {
               var27 = 0;
            }

            String var20 = null;

            try {
               var20 = var1.split("_")[1];
            } catch (Exception var23) {
               var20 = var1;
            }

            String var21 = this.getBtablaJel(var7.features);
            String var22 = this.getMertekegyseg(var7.features);
            var12.updateRec(0, var14, var20, var27, var10, "", "N", var21, var22);
         }
      }
   }

   private DataFieldModel getDF(String var1, int var2) {
      BookModel var3 = this.PM.getFormModel().getBookModel();
      Elem var4 = (Elem)var3.cc.get(var2);
      FormModel var5 = var3.get(var4.getType());
      DataFieldModel var6 = (DataFieldModel)var5.fids.get(var1);
      return var6;
   }

   private Result forintos(DataFieldModel var1) {
      Result var2 = new Result();
      var2.setOk(true);
      if (var1 == null) {
         var2.setOk(false);
         return var2;
      } else {
         this.megallapitaslista = null;
         this.veto_kihatas_dialog = false;
         BookModel var3 = this.PM.getFormModel().getBookModel();
         Elem var4 = (Elem)var3.cc.getActiveObject();
         IDataStore var5 = (IDataStore)var4.getRef();
         Datastore_history var6 = (Datastore_history)var4.getEtc().get("history");
         if (var6 == null) {
            var6 = new Datastore_history();
         }

         String var7 = var1.getAdonem(var5, this.dynindex);
         String var8 = this.dynindex + "_" + var1.key;
         Vector var9 = var6.get(var8);
         String var10 = this.GETVELEM(var9, 0);
         String var11 = this.GETVELEM(var9, 1);
         String var12 = this.GETVELEM(var9, 2);
         Kihatasstore var16;
         if (var1.orig_readonly) {
            if (var7 != null && !"".equals(var7)) {
               IDbHandler var19 = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.DefaultDbHandler");
               String var20 = var19.getPrompt(var1.key);
               String var21 = MetaInfo.extendedInfoTxt(var1.key, this.PM.dynamic ? new Integer(this.dynindex) : null, this.PM.getFormModel().id, this.PM.getFormModel().bm);
               var16 = (Kihatasstore)var4.getEtc().get("kihatas");
               KihatasTableModel var22 = var16.get(var8);
               new KihatasSimpleDialog(this, var1, var5, var6, var8, var22, var20, var21, var7, var10, var11, var12);
               this.veto_kihatas_dialog = true;
               return var2;
            } else {
               var2.setOk(false);
               var2.errorList.add("Sablon hiba! Nincs adónem rendelve a számított mezőhöz!");
               return var2;
            }
         } else {
            boolean var13;
            if (var7 == null) {
               var13 = false;
            } else if (var7.indexOf(",") != -1) {
               var13 = true;
            } else {
               var13 = false;
            }

            IDbHandler var14 = DbFactory.getDbHandler("hu.piller.enykpdb.defaultdbhandler.DefaultDbHandler");
            Object[] var15 = var14.getMegallapitasLista(var1.features, var3, var7);
            String var17;
            KihatasTableModel var18;
            if (var15 == null) {
               var16 = (Kihatasstore)var4.getEtc().get("kihatas");
               var17 = this.dynindex + "_" + var1.key;
               var18 = var16.get(var17);
               if (var18.vannemtorolt()) {
                  this.showdeletablekihatas(var18, var5, var17, var6);
               }

               var2.setOk(false);
               return var2;
            } else if (var15[1] instanceof String) {
               var2.errorList.add(var15[1]);
               var16 = (Kihatasstore)var4.getEtc().get("kihatas");
               var17 = this.dynindex + "_" + var1.key;
               var18 = var16.get(var17);
               if (1 < var18.getRowCount() && var18.vannemtorolt()) {
                  this.showdeletablekihatas(var18, var5, var17, var6);
               }

               this.veto_kihatas_dialog = true;
               var2.setOk((Boolean)var15[0]);
               return var2;
            } else {
               if (var15[1] instanceof MegallapitasComboLista) {
                  this.megallapitaslista = (MegallapitasComboLista)var15[1];
               }

               if (var13) {
                  new KihatasCsopDialog(this, this.PM, var1, this.dynindex, this.megallapitaslista, false);
                  this.veto_kihatas_dialog = true;
                  var2.setOk((Boolean)var15[0]);
                  return var2;
               } else {
                  var2.setOk((Boolean)var15[0]);
                  return var2;
               }
            }
         }
      }
   }

   private void showdeletablekihatas(final KihatasTableModel var1, final IDataStore var2, final String var3, final Datastore_history var4) {
      final JDialog var5 = new JDialog(MainFrame.thisinstance, "Kihatások amik nem lehetnének", true);
      MultiLineTable var6 = new MultiLineTable(var1, new MegallapitasComboLista(), ABEVFunctionSet.getInstance().getPrecisionForKihatas(var3));
      JPanel var7 = new JPanel(new BorderLayout());
      var7.add(new JScrollPane(var6));
      JPanel var8 = new JPanel();
      JButton var9 = new JButton("A mezőhöz tartozó összes kihatás törlése");
      var8.add(var9);
      var9.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            for(int var2x = 0; var2x < var1.getRowCount(); ++var2x) {
               try {
                  KihatasRecord var3x = (KihatasRecord)var1.get(var2x);
                  MegallapitasVector var4x = var3x.getMegallapitasVector();
                  var4x.done_delete();
               } catch (Exception var5x) {
               }
            }

            String var6;
            if ("K".equals(var1.getAdattipusKod())) {
               var6 = "";
            } else {
               BigDecimal var7 = new BigDecimal(((KihatasRecord)var1.get(0)).getEredetiErtek());
               var6 = var7.subtract(var1.getSzumma()).toString();
            }

            PageViewer.this.kihatas_vege(var6, var2, var4, var3, var1, true);
            var5.setVisible(false);
         }
      });
      JButton var10 = new JButton("Mégsem");
      var8.add(var10);
      var10.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            var5.setVisible(false);
         }
      });
      var7.add(var8, "South");
      var5.getContentPane().add(var7);
      var5.setSize(600, 450);
      var5.setLocationRelativeTo(MainFrame.thisinstance);
      var5.setVisible(true);
   }

   public boolean vannemtorolt(DefaultTableModel var1) {
      for(int var2 = 0; var2 < var1.getRowCount(); ++var2) {
         Vector var3 = null;

         try {
            var3 = (Vector)var1.getValueAt(var2, 1);
         } catch (Exception var5) {
            continue;
         }

         for(int var4 = 0; var4 < var3.size(); ++var4) {
            if (!"T".equals(var3.get(3))) {
               return true;
            }
         }
      }

      return false;
   }

   public int done_down() {
      if (this.current_df == null) {
         return 1;
      } else {
         DataFieldModel var1 = this.current_df;
         String var2 = null;
         MetaStore var3 = MetaInfo.getInstance().getMetaStore(this.PM.getFormModel().id);
         String var4 = (String)this.current_df.features.get("row");
         String var5 = (String)this.current_df.features.get("col");
         if (var4 != null && var5 != null) {
            if (var5.equals("0")) {
               return 1;
            } else {
               int var6 = Integer.parseInt(var4);

               for(int var7 = 0; var7 < 50; ++var7) {
                  ++var6;
                  Hashtable var8 = var3.getRowMetas();
                  Hashtable var9 = var3.getColMetas();
                  boolean var10 = false;
                  Object var11 = var8.get(var6 + "");
                  Object var12 = var9.get(var5);
                  if (var11 == null || var12 == null) {
                     return 1;
                  }

                  Vector var13;
                  if (var11 instanceof Vector) {
                     var13 = (Vector)var11;
                  } else {
                     var13 = new Vector();
                     var13.add(var11);
                  }

                  for(int var14 = 0; var14 < var13.size(); ++var14) {
                     Hashtable var15 = (Hashtable)var13.get(var14);
                     if (var5.equals(var15.get("col"))) {
                        var2 = (String)var15.get("fid");
                        if (this.PM.getByCodeAll(var2) != null) {
                           this.setFocus(this.PM.getByCode(var2));
                           if (this.current_df != null) {
                              return 0;
                           }
                        }
                     }
                  }
               }

               return 1;
            }
         } else {
            return 1;
         }
      }
   }

   public int done_up() {
      if (this.current_df == null) {
         return 1;
      } else {
         String var1 = null;
         MetaStore var2 = MetaInfo.getInstance().getMetaStore(this.PM.getFormModel().id);
         String var3 = (String)this.current_df.features.get("row");
         String var4 = (String)this.current_df.features.get("col");
         if (var3 != null && var4 != null) {
            if (var4.equals("0")) {
               return 1;
            } else {
               int var5 = Integer.parseInt(var3);

               for(int var6 = 0; var6 < 50; ++var6) {
                  if (var5 == 0) {
                     return 1;
                  }

                  --var5;
                  Hashtable var7 = var2.getRowMetas();
                  Hashtable var8 = var2.getColMetas();
                  boolean var9 = false;
                  Object var10 = var7.get(var5 + "");
                  Object var11 = var8.get(var4);
                  if (var10 == null || var11 == null) {
                     return 1;
                  }

                  Vector var12;
                  if (var10 instanceof Vector) {
                     var12 = (Vector)var10;
                  } else {
                     var12 = new Vector();
                     var12.add(var10);
                  }

                  for(int var13 = 0; var13 < var12.size(); ++var13) {
                     Hashtable var14 = (Hashtable)var12.get(var13);
                     if (var4.equals(var14.get("col"))) {
                        var1 = (String)var14.get("fid");
                        if (this.PM.getByCodeAll(var1) != null) {
                           this.setFocus(this.PM.getByCode(var1));
                           if (this.current_df != null) {
                              return 0;
                           }
                        }
                     }
                  }
               }

               return 1;
            }
         } else {
            return 1;
         }
      }
   }

   protected void processKeyEvent(KeyEvent var1) {
      super.processKeyEvent(var1);
      if (var1.isShiftDown() && var1.isControlDown() && var1.isAltDown()) {
         PropertyList.showDialog();
      } else {
         if (var1.getID() == 401 && var1.isAltDown() && var1.getKeyChar() == 'l') {
            Thread var2 = new Thread(new Runnable() {
               public void run() {
                  MainFrame.thisinstance.mp.getDMFV().fv.getTp().requestFocus();
               }
            });
            var1.consume();
            var2.start();
         }

      }
   }

   public DataFieldModel getCurrent_df() {
      return this.current_df;
   }

   public void setCurrent_df(DataFieldModel var1) {
      this.current_df = var1;
   }

   private boolean checkRevizoriErtek(String var1, String var2, Datastore_history var3) {
      if (var2 != null && !"".equals(var2)) {
         return true;
      } else {
         return !var2.equals(var3.getHistoryEredeti(var1));
      }
   }

   private boolean done_char_kihatas(DataFieldModel var1, final String var2) {
      final Hashtable var4 = var1.features;
      BookModel var5 = this.PM.getFormModel().getBookModel();
      Elem var6 = (Elem)var5.cc.getActiveObject();
      IDataStore var7 = (IDataStore)var6.getRef();
      Datastore_history var8 = (Datastore_history)var6.getEtc().get("history");
      if (var8 == null) {
         var8 = new Datastore_history();
      }

      final Kihatasstore var9 = (Kihatasstore)var6.getEtc().get("kihatas");
      String var11 = var1.key;
      Object[] var12 = new Object[]{new Integer(this.dynindex), var11};
      String var13 = var7.get(var12);
      final String var10 = this.dynindex + "_" + var11;
      final KihatasTableModel var15 = var9.get(var10);
      if (this.current_df.features.get("btable") != null && ((String)this.current_df.features.get("btable")).equalsIgnoreCase("soha")) {
         String var21 = "A mező 'btable' tulajdonsága 'soha' a nyomtatványsablonban";
         KihatasRecord var22 = (KihatasRecord)var15.get(0);
         if (!var22.isEmpty()) {
            var15.deleteAll();
            var21 = var21 + "\nA mezőhöz kihatás tartozott, töröltük.";
         }

         GuiUtil.showMessageDialog(MainFrame.thisinstance, var21, "Üzenet", 0);
         this.kihatas_dialog_shown = false;
         return false;
      } else if (this.megallapitaslista != null && this.megallapitaslista.size() != 0) {
         Vector var16 = var8.get(var10);
         if (var16 == null) {
            var16 = new Vector();
            var16.setSize(4);
         }

         String var17 = var1.getAdonem(var7, this.dynindex);
         final String var18 = var15.getEredeti(var16, 1);
         final CharKihatasDialog var19 = new CharKihatasDialog(this.PM, this, var1, this.dynindex, var11, var16, var13, this.megallapitaslista, var17, var18);
         Vector finalVar1 = var16;
         var19.getOkbtn().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               try {
                  KihatasRecord var2x = null;
                  MegallapitasRecord var4x = null;
                  MegallapitasVector var3;
                  if (var15.getRowCount() == 1) {
                     var3 = new MegallapitasVector();
                  } else {
                     var2x = (KihatasRecord)var15.remove(0);
                     var3 = var2x.getMegallapitasVector();
                     var4x = (MegallapitasRecord)var3.get(0);
                  }

                  if (var4x == null) {
                     var4x = new MegallapitasRecord("", var19.getMsvo(), "", "");
                  } else {
                     var4x.setMsvo_azon(var19.getMsvo());
                     if (var2.equals(var18)) {
                        var4x.setToroltsegjel("T");
                     } else {
                        var4x.setToroltsegjel("M");
                     }
                  }

                  if (var3.size() == 0) {
                     var3.add(var4x);
                  } else {
                     var3.setElementAt(var4x, 0);
                  }

                  String var5 = PageViewer.this.getPM().getFormModel().id;
                  String var6 = var10.split("_")[0];
                  String var7 = var10.split("_")[1];
                  if (var2x == null) {
                     var2x = new KihatasRecord(var5 + "@" + var7, var3, var19.getAdonemKod(), var2, "", "", var6, "", var18, "", "N", PageViewer.this.getBtablaJel(var4), PageViewer.this.getMertekegyseg(var4), finalVar1, "", "K");
                  } else {
                     var2x.setMegallapitasVector(var3);
                     var2x.setAdonemKod(var19.getAdonemKod());
                     var2x.setModositoOsszeg(var2);
                     var2x.setMertekegyseg(PageViewer.this.getMertekegyseg(var4));
                     var2x.setBtablaJel(PageViewer.this.getBtablaJel(var4));
                     var2x.setHistory(finalVar1);
                     var2x.setHistory_revizor(var2);
                  }

                  if (!var2.equals(var18) || !"".equals(var4x.getKias_azon())) {
                     var15.insertElementAt(var2x, 0);
                     var9.set(var10, var15);
                  }
               } catch (Exception var11) {
                  GuiUtil.showMessageDialog(MainFrame.thisinstance, "Hiba a kihatás rögzítésekor", "Hiba", 64);
                  Tools.exception2SOut(var11);
               } finally {
                  var19.setVisible(false);
                  PageViewer.this.kihatas_dialog_shown = false;
               }

            }
         });
         var19.getCancelbtn().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               if (var15.getRowCount() > 1) {
                  KihatasRecord var2x = (KihatasRecord)var15.get(0);
                  if (!var2.equals(var2x.getModositoOsszeg())) {
                     GuiUtil.showMessageDialog(var19, "A mező értéke megváltozott, csak az 'OK' gombbal lehet kilépni!", "Üzenet", 0);
                     return;
                  }
               }

               PageViewer.this.kihatas_dialog_shown = false;
               var19.setVisible(false);
            }
         });
         var19.setVisible(true);
         return true;
      } else {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "A mezőhöz tartozhatna kihatás, de nincs hozzárendelhető jegyzőkönyvi pont.\nÍgy nem tudjuk kihatásként menteni.", "Üzenet", 0);
         this.kihatas_dialog_shown = false;
         return false;
      }
   }

   private String getBtablaJel(Hashtable var1) {
      String var2 = (String)var1.get("btable_jel");
      if (var2 == null) {
         var2 = "";
      }

      return var2;
   }

   private String getMertekegyseg(Hashtable var1) {
      String var2 = (String)var1.get("mertekegyseg");
      if (var2 == null) {
         var2 = (String)var1.get("mask");

         try {
            var2 = var2.split("!", 2)[1];
         } catch (Exception var4) {
            var2 = "";
         }
      }

      return var2;
   }

   public static boolean equalsIgnoreDecimal(String var0, String var1) {
      int var3 = var0.indexOf(".");
      String var2;
      if (var3 > -1) {
         var2 = var0.substring(var3 + 1);
         if (Integer.parseInt(var2) == 0) {
            var0 = var0.substring(0, var3);
         }
      }

      var3 = var1.indexOf(".");
      if (var3 > -1) {
         var2 = var1.substring(var3 + 1);
         if (Integer.parseInt(var2) == 0) {
            var1 = var1.substring(0, var3);
         }
      }

      return var0.equals(var1);
   }

   public static String cutZeroDecimal(String var0) {
      int var2 = var0.indexOf(".");
      if (var2 > -1) {
         String var1 = var0.substring(var2 + 1);
         if (Integer.parseInt(var1) == 0) {
            var0 = var0.substring(0, var2);
         }
      }

      return var0;
   }

   private void doPageErase() {
      if (!"1".equals(MainFrame.opmode)) {
         Object[] var1 = new Object[]{"Tovább", "Mégsem"};
         int var2 = JOptionPane.showOptionDialog(MainFrame.thisinstance, "Törli az aktuális lap (" + this.PM.title + ") adatait?\nA funkció használata után az újraszámítás le fog futni!", "Lap adatainak törlése", 0, 3, (Icon)null, var1, var1[1]);
         if (var2 == 0) {
            Vector var3 = this.PM.y_sorted_df;
            GUI_Datastore var4 = this.getDatastore();

            for(int var5 = 0; var5 < var3.size(); ++var5) {
               DataFieldModel var6 = (DataFieldModel)var3.elementAt(var5);
               String var7 = this.dynindex + "_" + var6.key;
               var4.set(var7, "");
            }

            CalculatorManager.getInstance().multiform_calc();
            this.refresh();
         }
      }
   }

   private void handlePopupMenu() {
      this.copyItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            PageViewer.this.clip.setClipboardContents(PageViewer.this.copyToClipboardValue);
         }
      });
      this.contextMenu.add(this.copyItem);
   }

   private void handleMouseListeners() {
      MouseListener[] var1 = this.getMouseListeners();

      int var2;
      for(var2 = 0; var2 < var1.length; ++var2) {
         this.removeMouseListener(var1[var2]);
      }

      this.addMouseListener(this.mia);

      for(var2 = 0; var2 < var1.length; ++var2) {
         this.addMouseListener(var1[var2]);
      }

   }

   private void handleCalcFoadat() {
      final JDialog var1 = new JDialog(MainFrame.thisinstance, "Mezőszámítás", true);
      var1.setDefaultCloseOperation(0);
      final boolean[] var2 = new boolean[]{false};
      final SwingWorker var3 = new SwingWorker() {
         public Object doInBackground() {
            try {
               JComponent var1x = null;
               DataFieldModel var2x = null;
               if (PageViewer.this.current_jc != null) {
                  var1x = PageViewer.this.current_jc;
               }

               if (PageViewer.this.current_df != null) {
                  var2x = PageViewer.this.current_df;
               }

               boolean var3 = PageViewer.this.done_foadat();
               if (var1x != null) {
                  PageViewer.this.current_jc = var1x;
               }

               if (var2x != null) {
                  PageViewer.this.current_df = var2x;
               }

               return var3;
            } catch (Exception var4) {
            } catch (Error var5) {
               var5.printStackTrace();
            }

            return false;
         }

         public void done() {
            try {
               PageViewer.this.cmdObject = this.get();
            } catch (Exception var3) {
               var3.printStackTrace();
               PageViewer.this.cmdObject = false;
            }

            var2[0] = true;

            try {
               var1.setVisible(false);
            } catch (Exception var2x) {
               Tools.eLog(var2x, 0);
            }

         }
      };
      var1.addWindowListener(new WindowAdapter() {
         public void windowActivated(WindowEvent var1) {
            var3.execute();
         }
      });
      int var4 = MainFrame.thisinstance.getX() + MainFrame.thisinstance.getWidth() / 2 - 150;
      if (var4 < 0) {
         var4 = 0;
      }

      int var5 = MainFrame.thisinstance.getY() + MainFrame.thisinstance.getHeight() / 2 - 200;
      if (var5 < 0) {
         var5 = 0;
      }

      var1.setBounds(var4, var5, 300, 40);
      var1.setSize(300, 40);
      JPanel var6 = new JPanel((LayoutManager)null, true);
      var6.setLayout(new BoxLayout(var6, 1));
      Dimension var7 = new Dimension(300, 30);
      var6.setPreferredSize(var7);
      progressLabel.setPreferredSize(var7);
      progressLabel.setAlignmentX(0.5F);
      progressLabel.setIndeterminate(true);
      progressLabel.setString("Mezőszámítás folyamatban...");
      progressLabel.setStringPainted(true);
      var6.add(Box.createVerticalStrut(5));
      var6.add(progressLabel);
      var6.add(Box.createVerticalStrut(5));
      var6.setVisible(true);
      var1.getContentPane().add(var6);
      var2[0] = false;
      var1.pack();
      var1.setVisible(true);
      if (var2[0]) {
         var1.setVisible(false);
      }

   }

   private boolean done_foadat() {
      String var1 = this.ss.get("gui", "detail");
      boolean var10000;
      if (var1 == null) {
         var10000 = true;
      } else {
         var10000 = "true".equals(var1);
      }

      Element var3 = null;
      BookModel var4 = this.PM.getFormModel().getBookModel();
      Elem var5 = (Elem)var4.cc.getActiveObject();
      IDataStore var6 = (IDataStore)var5.getRef();
      String var7 = ((IDataField)this.current_jc).getFieldValue().toString();
      int var8 = ((IDataField)this.current_jc).getRecordIndex();
      String var9 = this.current_df.key;
      Object[] var10 = new Object[]{new Integer(this.dynindex), var9};
      String var11;
      if (MainFrame.FTRmode && MainFrame.FTRdoc != null) {
         try {
            var3 = MainFrame.FTRdoc.createElement("mezo");
            var3.setAttribute("eazon", var9);
            String var12;
            if (0 < this.dynindex) {
               var11 = "000" + (this.dynindex + 1);
               var12 = var9.substring(0, 2) + var11.substring(var11.length() - 4) + var9.substring(6);
               var3.setAttribute("dyneazon", var12);
            }

            var11 = ((IDataField)this.current_jc).getFieldValueWOMask().toString();
            var12 = var11;
            if ("false".equals(var11)) {
               var12 = "";
            }

            if ("true".equals(var12)) {
               var12 = "X";
            }

            Text var13 = MainFrame.FTRdoc.createTextNode(var12);
            var3.appendChild(var13);
            MainFrame.FTRmezok.appendChild(var3);
         } catch (DOMException var15) {
         }
      }

      var6.set(var10, var7);
      if (var8 != -1 && this.cm.FillGroupFields(this.PM.getFormModel().id, var6, this.current_df, var8, this.dynindex)) {
         this.refresh();
      }

      var11 = this.dynindex + "_" + var9;
      var6.getMasterCaseId(var11);
      var4.setCalcelemindex(var4.cc.getActiveObjectindex());
      this.done_set_history(var11, var7, var9);

      try {
         ((GUI_Datastore)var6).inkihatas = true;
         this.cm.calcReszbizonylatFuggosegek(var5.getType(), var9);
         ((GUI_Datastore)var6).inkihatas = false;
      } catch (Exception var14) {
      }

      this.refresh();
      MainFrame.thisinstance.mp.getDMFV().fv.setTabStatus();
      return true;
   }

   private String getWebTextareaContentFromHistory(BookModel var1, Datastore_history var2, String var3, int var4) {
      try {
         ArrayList var5 = var1.getTextAreaFidList(var3);
         StringBuffer var6 = new StringBuffer();
         Iterator var7 = var5.iterator();

         while(var7.hasNext()) {
            String var8 = (String)var7.next();
            var6.append(var2.getOriginalUserHistoryData(var4 + "_" + var8));
         }

         return var6.toString();
      } catch (Exception var9) {
         return "";
      }
   }

   public void showOriginalTextAreaText(String var1) {
      final JDialog var2 = new JDialog(MainFrame.thisinstance, "A mező eredeti, weben megjelenő szövege", true);
      var2.setDefaultCloseOperation(2);
      JButton var3 = new JButton("Ok");
      JEditorPane var4 = new JEditorPane("text/plain", var1);
      var4.setFont(var2.getFont());
      var4.setEditable(false);
      var4.setSize(650, 500);
      JScrollPane var5 = new JScrollPane(var4, 20, 30);
      JPanel var6 = new JPanel(new BorderLayout());
      JPanel var7 = new JPanel();
      var6.add(var5, "Center");
      EmptyBorder var8 = new EmptyBorder(10, 10, 10, 10);
      var5.setBorder(var8);
      var3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            var2.setVisible(false);
            var2.dispose();
         }
      });
      var7.add(var3);
      var3.setEnabled(true);
      var6.add(var7, "South");
      var2.getContentPane().add(var6);
      Dimension var9 = new Dimension(700, 600);
      var2.setSize(var9);
      var2.setPreferredSize(var9);
      var2.setMinimumSize(var9);
      var2.setLocationRelativeTo(MainFrame.thisinstance);
      var2.setVisible(true);
   }
}
