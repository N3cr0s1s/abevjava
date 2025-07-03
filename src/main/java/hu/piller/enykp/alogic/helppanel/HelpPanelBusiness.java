package hu.piller.enykp.alogic.helppanel;

import hu.piller.enykp.alogic.filesaver.enykinner.ENYKClipboardHandler;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.interfaces.IOsHandler;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.chardet.ENYKCharsetDetector;
import hu.piller.enykp.util.oshandler.OsFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.text.Document;

class HelpPanelBusiness {
   private HelpPanel help_panel;
   private JEditorPane help_pane;
   private JButton btn_back;
   private JButton btn_forward;
   private JButton btn_browser;
   private JLabel lbl_find;
   private JTextField txt_find;
   private JButton btn_search;
   private JButton btn_n_search;
   private JButton btn_p_search;
   private final Vector link_store = new Vector(128, 128);
   private int link_index;
   private final HelpPanelBusiness.FoundPosition found_position = new HelpPanelBusiness.FoundPosition();

   public HelpPanelBusiness(HelpPanel var1) {
      this.help_panel = var1;
      this.prepare();
   }

   private void prepare() {
      this.btn_back = (JButton)this.help_panel.getHComponent("back");
      this.btn_forward = (JButton)this.help_panel.getHComponent("forward");
      this.btn_browser = (JButton)this.help_panel.getHComponent("open_with_browser");
      this.help_pane = (JEditorPane)this.help_panel.getHComponent("editor");
      this.help_pane.putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);
      this.lbl_find = (JLabel)this.help_panel.getHComponent("find_text_lbl");
      this.txt_find = (JTextField)this.help_panel.getHComponent("find_text");
      this.btn_search = (JButton)this.help_panel.getHComponent("search");
      this.btn_n_search = (JButton)this.help_panel.getHComponent("search_n");
      this.btn_p_search = (JButton)this.help_panel.getHComponent("search_p");
      this.link_index = -1;
      this.found_position.reset();
      this.prepareBack();
      this.prepareForward();
      this.prepareOpenWithBrowser();
      this.prepareFindText();
      this.prepareSearch();
      this.prepareSearchNext();
      this.prepareSearchPrev();
      this.prepareHelpPane();
   }

   private void prepareBack() {
      this.btn_back.setEnabled(false);
      this.btn_back.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (HelpPanelBusiness.this.link_index > 0 && HelpPanelBusiness.this.link_index < HelpPanelBusiness.this.link_store.size()) {
               HelpPanelBusiness.this.updateHelpPanel((URL)HelpPanelBusiness.this.link_store.get(--HelpPanelBusiness.this.link_index), false);
               HelpPanelBusiness.this.adjustBackForwardButtons();
            }

         }
      });
   }

   private void prepareForward() {
      this.btn_forward.setEnabled(false);
      this.btn_forward.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (HelpPanelBusiness.this.link_index >= 0 && HelpPanelBusiness.this.link_index < HelpPanelBusiness.this.link_store.size() - 1) {
               HelpPanelBusiness.this.updateHelpPanel((URL)HelpPanelBusiness.this.link_store.get(++HelpPanelBusiness.this.link_index), false);
               HelpPanelBusiness.this.adjustBackForwardButtons();
            }

         }
      });
   }

   private void adjustBackForwardButtons() {
      int var1 = this.link_store.size();
      if (this.link_index > 0 && this.link_index < var1) {
         if (!this.btn_back.isEnabled()) {
            this.btn_back.setEnabled(true);
         }
      } else if (this.btn_back.isEnabled()) {
         this.btn_back.setEnabled(false);
      }

      if (this.link_index >= 0 && this.link_index < var1 - 1) {
         if (!this.btn_forward.isEnabled()) {
            this.btn_forward.setEnabled(true);
         }
      } else if (this.btn_forward.isEnabled()) {
         this.btn_forward.setEnabled(false);
      }

   }

   private void prepareOpenWithBrowser() {
      this.enableButtons(false);
      this.btn_browser.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            HelpPanelBusiness.this.openInBrowser(HelpPanelBusiness.this.help_pane.getPage());
         }
      });
   }

   private void prepareFindText() {
      this.txt_find.getInputMap(1).put(KeyStroke.getKeyStroke("ENTER"), "findNext");
      this.txt_find.getActionMap().put("findNext", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            HelpPanelBusiness.this.searchNext();
         }
      });
      String var1 = "<ide írja a lapon keresendő szöveget>";
      this.txt_find.setText(var1);
      this.txt_find.select(0, var1.length());
      this.txt_find.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent var1) {
            HelpPanelBusiness.this.txt_find.setText("");
            HelpPanelBusiness.this.txt_find.removeKeyListener(this);
         }
      });
   }

   private void prepareSearch() {
      this.btn_search.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (HelpPanelBusiness.this.isSearchable()) {
               HelpPanelBusiness.this.search();
            }

         }
      });
   }

   private void prepareSearchNext() {
      this.btn_n_search.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (HelpPanelBusiness.this.isSearchable()) {
               HelpPanelBusiness.this.searchNext();
            }

         }
      });
   }

   private void prepareSearchPrev() {
      this.btn_p_search.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (HelpPanelBusiness.this.isSearchable()) {
               HelpPanelBusiness.this.searchPrev();
            }

         }
      });
   }

   private boolean isSearchable() {
      boolean var1 = false;
      if (this.txt_find.getKeyListeners().length <= 0 && this.txt_find.getText().trim().length() != 0) {
         var1 = true;
      } else {
         GuiUtil.showMessageDialog(this.help_panel, "Nem adott meg keresendő kifejezést!", "Figyelmeztetés", 2);
      }

      return var1;
   }

   private void search() {
      this.found_position.reset();
      if (this.findText(this.found_position, 1)) {
         this.showHit();
      }

   }

   private void searchNext() {
      if (this.findText(this.found_position, 1)) {
         this.showHit();
      }

   }

   private void searchPrev() {
      if (this.findText(this.found_position, -1)) {
         this.showHit();
      }

   }

   private boolean findText(HelpPanelBusiness.FoundPosition var1, int var2) {
      Document var3 = this.help_pane.getDocument();
      byte var6;
      if (var2 > 0) {
         var6 = 1;
      } else {
         if (var2 >= 0) {
            return false;
         }

         var6 = -1;
      }

      String var5 = this.txt_find.getText();
      if (var5.length() == 0) {
         return false;
      } else {
         try {
            int var7 = var1.pos;
            String var4 = var3.getText(0, var3.getLength());
            int[] var9 = this.getPositionList(var4, var5);
            if (var9.length == 0) {
               return false;
            } else {
               if (var7 == -1 && var6 < 0) {
                  var7 = var4.length();
               }

               int var8 = -1;
               int var10;
               if (var6 > 0) {
                  var10 = 0;

                  for(int var11 = var9.length; var10 < var11; ++var10) {
                     if (var9[var10] > var7) {
                        var8 = var9[var10];
                        break;
                     }
                  }
               } else {
                  for(var10 = var9.length - 1; var10 >= 0; --var10) {
                     if (var9[var10] < var7) {
                        var8 = var9[var10];
                        break;
                     }
                  }
               }

               if (var8 == -1) {
                  return false;
               } else {
                  var1.pos = var8;
                  var1.len = var5.length();
                  return true;
               }
            }
         } catch (Exception var12) {
            var12.printStackTrace();
            return false;
         }
      }
   }

   private int[] getPositionList(String var1, String var2) {
      if (var1 != null) {
         var1 = var1.toLowerCase();
      }

      if (var2 != null) {
         var2 = var2.toLowerCase();
      }

      int var4 = 0;

      int var5;
      for(var5 = -1; (var5 = var1.indexOf(var2, var5 + 1)) >= 0; ++var4) {
      }

      int[] var3 = new int[var4];
      var4 = 0;

      for(var5 = -1; (var5 = var1.indexOf(var2, var5 + 1)) >= 0; ++var4) {
         var3[var4] = var5;
      }

      return var3;
   }

   private void showHit() {
      this.help_pane.getCaret().setSelectionVisible(true);
      this.help_pane.select(this.found_position.pos, this.found_position.pos + this.found_position.len);
   }

   private void prepareHelpPane() {
      this.help_pane.setEditable(false);
      this.help_pane.addHyperlinkListener(new HyperlinkListener() {
         public void hyperlinkUpdate(HyperlinkEvent var1) {
            if (var1.getEventType() == EventType.ACTIVATED) {
               if (var1.getURL().getProtocol().toLowerCase().startsWith("http")) {
                  HelpPanelBusiness.this.openInBrowser(var1.getURL());
               } else if (var1.getURL().toString().toLowerCase().indexOf("www") > -1) {
                  HelpPanelBusiness.this.openInBrowser(HelpPanelBusiness.this.convertUrl(var1.getURL()));
               } else {
                  HelpPanelBusiness.this.updateHelpPanel(var1.getURL());
               }
            }

         }
      });
   }

   public void updateHelpPanel(URL var1) {
      this.updateHelpPanel(var1, true);
   }

   public void updateHelpPanel(final URL var1, final boolean var2) {
      try {
         Thread var3 = new Thread(new Runnable() {
            public void run() {
               try {
                  System.out.println("HelpPanelBusiness.updateHelpPanel: " + var1);

                  try {
                     String var1x = ENYKCharsetDetector.detect(var1);
                     if (var1x != null) {
                        System.out.println("Html file charset: " + var1x);
                        HelpPanelBusiness.this.help_pane.setContentType("text/html; charset=" + var1x);
                     }

                     HelpPanelBusiness.this.help_pane.setPage(var1);
                  } catch (IOException var6) {
                     System.out.println(var6.getMessage());
                  }

                  HelpPanelBusiness.this.enableButtons(true);
               } catch (Exception var7) {
                  HelpPanelBusiness.this.enableButtons(false);
                  HelpPanelBusiness.this.writeError("Sikertelen súgó állomány betöltés !", var7);
               } finally {
                  if (var2) {
                     if (!HelpPanelBusiness.this.clearBack(var1)) {
                        HelpPanelBusiness.this.link_store.add(var1);
                        HelpPanelBusiness.this.link_index++;
                     }

                     HelpPanelBusiness.this.adjustBackForwardButtons();
                  }

               }

            }
         });
         SwingUtilities.invokeLater(var3);
      } catch (Exception var4) {
         this.writeError("Sikertelen súgó állomány betöltés !", var4);
      }

   }

   private boolean clearBack(URL var1) {
      int var2 = this.link_store.lastIndexOf(var1);
      if (var2 < 0) {
         return false;
      } else {
         this.link_store.setSize(var2 + 1);
         this.link_index = var2;
         return true;
      }
   }

   public File getFileByLocation(String var1) {
      File var2;
      try {
         var2 = new File((new URL(var1)).toString());
      } catch (Exception var6) {
         String var3 = this.getRootPath();
         String var4 = this.getHelpPath();
         if (var1.startsWith(var4)) {
            var2 = new File(this.getRootPath(), var1);
         } else if (var4.indexOf(58) >= 0) {
            var2 = new File((new File(var4, var1)).toString());
         } else {
            var2 = new File(new File(var3, var4), var1);
         }
      }

      return var2;
   }

   public URL getURL(File var1) throws Exception {
      try {
         return new URL(var1.toString());
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
         return var1.toURL();
      }
   }

   public String getHelpPath() {
      try {
         IPropertyList var1 = PropertyList.getInstance();
         if (var1 != null) {
            try {
               String var2 = (new File((String)var1.get("prop.sys.helps"))).toString();
               return var2 == null ? (new File(".")).getCanonicalPath() : var2;
            } catch (IOException var3) {
               this.writeError("Sikertelen súgó útvonal megszerzési művelet !", var3);
            }
         }
      } catch (Exception var4) {
         this.writeError("Sikertelen tulajdonság lista megszerzési művelet ! ", var4);
      }

      return ".";
   }

   public String getRootPath() {
      try {
         IPropertyList var1 = PropertyList.getInstance();
         if (var1 != null) {
            try {
               String var2 = (new File((String)var1.get("prop.sys.root"))).toString();
               return var2 == null ? (new File(".")).getCanonicalPath() : var2;
            } catch (IOException var3) {
               this.writeError("Sikertelen gyökér útvonal megszerzési művelet !", var3);
            }
         }
      } catch (Exception var4) {
         this.writeError("Sikertelen tulajdonság lista megszerzési művelet ! ", var4);
      }

      return ".";
   }

   public void writeError(String var1, Exception var2) {
      ErrorList.getInstance().writeError(new Long(0L), var1, var2, (Object)null);
   }

   public void enableButtons(boolean var1) {
      if (this.btn_browser != null) {
         this.btn_browser.setEnabled(var1);
      }

      if (this.lbl_find != null) {
         this.lbl_find.setEnabled(var1);
      }

      if (this.txt_find != null) {
         this.txt_find.setEditable(var1);
         this.txt_find.setEnabled(var1);
      }

      if (this.btn_search != null) {
         this.btn_search.setEnabled(var1);
      }

      if (this.btn_n_search != null) {
         this.btn_n_search.setEnabled(var1);
      }

      if (this.btn_p_search != null) {
         this.btn_p_search.setEnabled(var1);
      }

   }

   public void clearContent() {
      if (this.help_pane != null) {
         if (this.btn_back != null) {
            this.btn_back.setEnabled(false);
         }

         if (this.btn_forward != null) {
            this.btn_forward.setEnabled(false);
         }

         this.link_index = -1;
         this.link_store.setSize(0);
         this.help_pane.setDocument(this.help_pane.getEditorKit().createDefaultDocument());
      }

   }

   private void openInBrowser(URL var1) {
      Object var2 = null;

      try {
         IOsHandler var3 = OsFactory.getOsHandler();

         try {
            File var4;
            try {
               var4 = new File(SettingsStore.getInstance().get("gui", "internet_browser"));
               if (!var4.exists()) {
                  throw new Exception();
               }
            } catch (Exception var6) {
               var4 = new File(var3.getSystemBrowserPath());
            }

            if (var4.getName().toLowerCase().indexOf("edge") > -1) {
               ENYKClipboardHandler var5 = new ENYKClipboardHandler();
               var5.setClipboardContents(var1.toString());
               var3.execute("start Microsoft-Edge:" + var1, (String[])null, var4.getParentFile());
            } else {
               var3.execute(var4.getName() + " \"" + var1 + "\"", (String[])null, var4.getParentFile());
            }
         } catch (Exception var7) {
            var7.printStackTrace();
         }
      } catch (Exception var8) {
         GuiUtil.showMessageDialog(SwingUtilities.getRoot(this.help_panel), "A beállított internet böngésző nem érhető el!" + (var2 == null ? "" : "\n(" + var2.toString() + ")"), "Megnyitás böngészőben", 2);
      }

   }

   private URL convertUrl(URL var1) {
      try {
         File var2 = new File(var1.getFile());
         return new URL("http", var2.getName(), var1.getPort(), "");
      } catch (Exception var3) {
         return var1;
      }
   }

   private class FoundPosition {
      public int pos;
      public int len;

      private FoundPosition() {
      }

      public void reset() {
         this.pos = -1;
         this.len = 0;
      }

      // $FF: synthetic method
      FoundPosition(Object var2) {
         this();
      }
   }
}
