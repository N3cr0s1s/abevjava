package hu.piller.enykp.gui.model;

import hu.piller.enykp.alogic.fileutil.DatastoreKeyToXml;
import hu.piller.enykp.datastore.GUI_Datastore;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.print.Utils;
import hu.piller.enykp.print.simpleprint.KPrintFormFeedType;
import hu.piller.enykp.print.simpleprint.KPrintPageType;
import hu.piller.enykp.print.simpleprint.PageTitle;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class SimpleViewModel {
   BookModel bm;
   GUI_Datastore ds;
   FormModel fm;
   JTabbedPane tp;
   Vector v;
   Hashtable pagesht;
   private KPrintPageType currentPageType;

   public SimpleViewModel(BookModel var1) {
      this.currentPageType = KPrintPageType.NORMAL;
      this.bm = var1;
      this.ds = (GUI_Datastore)var1.get_datastore();
      this.v = new Vector();
   }

   public Vector getResult(Hashtable var1) {
      this.pagesht = var1;
      if (this.pagesht == null) {
         this.pagesht = new Hashtable();
      }

      this.getMainPanel();
      return this.v;
   }

   public void showDialog(MainFrame var1) {
      JPanel var2 = this.getMainPanel();
      String var3 = "Egyszerű nézet";
      final JDialog var4 = new JDialog(var1 == null ? MainFrame.thisinstance : var1, var3, true);
      var4.getContentPane().add(var2, "Center");
      JButton var5 = new JButton("Bezár");
      var5.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            try {
               var4.hide();
               var4.dispose();
            } catch (Exception var3) {
            }

         }
      });
      JButton var6 = new JButton("Nyomtat");
      var6.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            try {
               SimpleViewModel.this.tableModels2HtmlTable(SimpleViewModel.this.v);
            } catch (Exception var3) {
            }

         }
      });
      JPanel var7 = new JPanel();
      var7.add(var6);
      var7.add(var5);
      var4.getContentPane().add(var7, "South");
      var4.setSize(1100, 700);
      var4.setLocationRelativeTo(MainFrame.thisinstance);
      var4.show();
   }

   private JPanel getMainPanel() {
      JPanel var1 = new JPanel(new BorderLayout());
      this.tp = new JTabbedPane(1, 1);
      this.fm = this.bm.get();
      String var2 = "";
      String var3 = this.fm.mainlabel;
      this.currentPageType = this.fm.get(0).getKpLapTipus();
      boolean var4 = this.fm.get(0).isKpLandscape();
      String var7;
      if (var3 != null) {
         String[] var5 = var3.split("¦");

         for(int var6 = 0; var6 < var5.length; ++var6) {
            var7 = var5[var6];
            var7 = var7.toString().replaceAll("#", " ");
            var2 = var2 + var7;
         }

         if (var2.length() != 0) {
            var2 = " " + var2;
         }
      }

      if (var2.length() != 0) {
         PageTitle var13 = new PageTitle(var2, this.currentPageType, KPrintFormFeedType.NO, var4, this.fm.name);
         this.v.add(var13);
      }

      for(int var14 = 0; var14 < this.fm.size(); ++var14) {
         PageModel var15 = this.fm.get(var14);
         if (this.pagesht.containsKey(var15.name)) {
            var7 = "";
            String var8 = (String)var15.xmlht.get("mainlabel");

            try {
               if (var8 != null) {
                  String[] var9 = var8.split("¦");

                  for(int var10 = 0; var10 < var9.length; ++var10) {
                     String var11;
                     switch(var9[var10].charAt(0)) {
                     case 'A':
                        var11 = var9[var10].substring(2);
                        var11 = var11.toString().replaceAll("#13", "<br>");
                        var11 = var11.toString().replaceAll("#", " ");
                        var7 = var7 + var11;
                        break;
                     case 'K':
                        var11 = ((VisualFieldModel)this.fm.labels.get(var9[var10].substring(2))).text;
                        var11 = var11.toString().replaceAll("#13", "<br>");
                        var11 = var11.toString().replaceAll("#", " ");
                        var7 = var7 + var11;
                     }

                     if (var7.length() != 0) {
                        var7 = var7 + " ";
                     }
                  }
               }
            } catch (Exception var12) {
            }

            String var16;
            if (var7.length() == 0) {
               var16 = var15.title;
            } else {
               var16 = var7;
            }

            KPrintFormFeedType var17 = var15.getKpLapDobas();
            PageTitle var18 = new PageTitle(var16, var15.getKpLapTipus(), var17, var15.isKpLandscape(), var15.name);
            this.currentPageType = var15.getKpLapTipus();
            this.v.add(var18);
            this.tp.addTab(var15.name, this.getPanel(var15, var14));
         }
      }

      var1.add(this.tp);
      return var1;
   }

   private JComponent getPanel(PageModel var1, int var2) {
      byte var3 = 8;
      JPanel var4 = new JPanel();
      var4.setLayout(new BoxLayout(var4, 1));
      JScrollPane var5 = new JScrollPane(var4);
      String var6 = (String)var1.xmlht.get("blokksize");
      int var7 = 0;

      try {
         var7 = Integer.parseInt(var6);
      } catch (NumberFormatException var17) {
      }

      int var8 = 1;
      if (var1.dynamic) {
         int[] var9 = this.bm.get_pagecounts();
         var8 = var9[var2];
      }

      int var18 = 0;
      int var10 = 0;

      for(int var11 = 0; var11 < var8; ++var11) {
         if (0 < var11) {
            String var12 = var1.title + " ( " + (var11 + 1) + " )";
            KPrintFormFeedType var13 = var1.getKpLapDobas();
            PageTitle var14 = new PageTitle(var12, var1.getKpLapTipus(), var13, var1.isKpLandscape(), var1.name);
            this.v.add(var14);
            JLabel var15 = new JLabel("( " + (var11 + 1) + " )");
            var4.add(var15);
            var10 += var15.getHeight();
         }

         for(int var19 = -1; var19 <= var7; ++var19) {
            try {
               JTable var20 = this.getTable(var1, var19, var11);
               if (var20 != null) {
                  var4.add(var20);
                  var4.add(Box.createVerticalStrut(var3));
                  int var21 = var20.getPreferredSize().width;
                  if (var18 < var21) {
                     var18 = var21;
                  }

                  int var22 = var20.getPreferredSize().height;
                  var10 += var22 + var3;
               }
            } catch (Exception var16) {
            }
         }
      }

      var4.setPreferredSize(new Dimension(var18, var10));
      return var5;
   }

   private JTable getTable(PageModel var1, int var2, int var3) {
      if (var1.xmlht.containsKey("kpage_other")) {
         return null;
      } else {
         Vector var4 = var1.y_sorted_df;
         int var5 = 0;
         int var6 = 1;

         for(int var7 = 0; var7 < var4.size(); ++var7) {
            try {
               DataFieldModel var9 = (DataFieldModel)var4.get(var7);
               if (!var9.invisible) {
                  int var8;
                  try {
                     var8 = Integer.parseInt((String)var9.features.get("blokksorsz"));
                  } catch (NumberFormatException var33) {
                     var8 = -1;
                  }

                  if (var8 == var2) {
                     int var10 = Integer.parseInt((String)var9.features.get("col"));
                     int var11 = Integer.parseInt((String)var9.features.get("row"));
                     if (var5 < var11) {
                        var5 = var11;
                     }

                     if (var6 < var10) {
                        var6 = var10;
                     }
                  }
               }
            } catch (Exception var34) {
            }
         }

         JTable var36 = new JTable(var5 + 1, var6 + 1) {
            public boolean isCellEditable(int var1, int var2) {
               return false;
            }
         };
         Vector var37 = new Vector();
         Vector var38 = new Vector();
         Vector var39 = new Vector();
         Vector var40 = new Vector();
         var39.add("");
         var40.add("");
         Vector var12 = new Vector();

         int var13;
         for(var13 = 0; var13 < var4.size(); ++var13) {
            try {
               DataFieldModel var15 = (DataFieldModel)var4.get(var13);
               if (!var15.invisible && var15.kprintable) {
                  int var14;
                  try {
                     var14 = Integer.parseInt((String)var15.features.get("blokksorsz"));
                  } catch (NumberFormatException var32) {
                     var14 = -1;
                  }

                  if (var14 == var2) {
                     boolean var16 = false;

                     int var44;
                     try {
                        String var17 = (String)var15.features.get("vis_col");
                        if (var17 == null) {
                           var17 = (String)var15.features.get("col");
                        }

                        var44 = Integer.parseInt(var17);
                     } catch (NumberFormatException var31) {
                        var44 = 1;
                     }

                     boolean var46 = false;

                     String var18;
                     int var47;
                     try {
                        var18 = (String)var15.features.get("vis_row");
                        if (var18 == null) {
                           var18 = (String)var15.features.get("row");
                        }

                        var47 = Integer.parseInt(var18);
                     } catch (NumberFormatException var30) {
                        var47 = 0;
                     }

                     var18 = this.ds.get(var3 + "_" + var15.key);
                     if (var18 == null || "".equals(var18.trim())) {
                        if (var15.getKp_force() == DataFieldModel.KpForceType.notPresent) {
                           continue;
                        }

                        var18 = "";
                     }

                     if (var18.equals("false")) {
                        if (var15.getKp_force() == DataFieldModel.KpForceType.notPresent) {
                           continue;
                        }

                        var18 = "";
                     }

                     new Utils((BookModel)null);
                     if (var18.equals("true")) {
                        var18 = "X";
                     }

                     var18 = var15.getKprintformat(var18);
                     var18 = DatastoreKeyToXml.htmlConvert_kivonatolt(var18);
                     String var20;
                     String var23;
                     if (var44 != 0) {
                        if ("right".equals(var15.features.get("alignment"))) {
                           var18 = "<div id=\"tdr\">" + var18 + "</div>";
                        } else if ("center".equals(var15.features.get("alignment"))) {
                           var18 = "<div id=\"tdc\">" + var18 + "</div>";
                        } else {
                           var18 = "<div id=\"tdl\">" + var18 + "</div>";
                        }

                        var36.setValueAt(var18, var47 - 1 + 1, var44);
                        var20 = DatastoreKeyToXml.htmlConvert_kivonatolt(var15.geteprompt(this.fm));
                        String var21 = DatastoreKeyToXml.htmlConvert_kivonatolt(var15.getcprompt(this.fm));
                        String var22 = DatastoreKeyToXml.htmlConvert_kivonatolt(var15.getrowprompt(this.fm));
                        var23 = DatastoreKeyToXml.htmlConvert_kivonatolt(var15.getcolprompt(this.fm));
                        String var24 = (String)var36.getValueAt(var47 - 1 + 1, 0);
                        String var25 = (String)var36.getValueAt(0, var44);
                        var22 = this.cut13(var22);
                        var23 = this.cut13(var23);
                        if (var24 == null) {
                           var24 = "";
                        }

                        if (var25 == null) {
                           var25 = "";
                        }

                        if (var24.indexOf(var22) != -1) {
                           var22 = "";
                        }

                        if (var25.indexOf(var23) != -1) {
                           var23 = "";
                        }

                        String var26 = "" + var47;
                        if (var24.indexOf("./ ") == -1) {
                           (new StringBuilder()).append(var26).append("./ ").toString();
                        } else {
                           String var10000 = "";
                        }

                        var36.setValueAt(this.cut13(var24 + var22 + var20), var47 - 1 + 1, 0);
                        String var28 = this.cut13(var25 + var23);
                        var36.setValueAt(var28, 0, var44);
                        var36.setValueAt(this.cut13(var21), 0, 0);
                        Hashtable var29 = var15.getgprompt(this.fm);
                        if ((Integer)var29.get("index") != -1) {
                           var12.add(var29);
                        }
                     } else {
                        var20 = var15.geteprompt(this.fm);
                        Vector var48 = new Vector();
                        if (1 < var6) {
                           var48.add("***#" + var20);
                        } else {
                           var48.add("***" + var20);
                        }

                        if ("right".equals(var15.features.get("alignment"))) {
                           var18 = " id=\"ntdr\">" + var18;
                        } else if ("center".equals(var15.features.get("alignment"))) {
                           var18 = " id=\"ntdc\">" + var18;
                        } else {
                           var18 = " id=\"ntdl\">" + var18;
                        }

                        var48.add(var18);
                        boolean var49 = "top".equals(var15.features.get("zero_col_pos"));
                        if (var49) {
                           var37.add(var48);
                        } else {
                           var38.add(var48);
                        }

                        var23 = var15.getcprompt(this.fm);
                        if (!var23.equals("")) {
                           if (var49) {
                              var40.set(0, var23);
                           } else {
                              var39.set(0, var23);
                           }
                        }
                     }
                  }
               }
            } catch (Exception var35) {
            }
         }

         this.done_vht(var12, var36);
         if (var38.size() != 0 && var39.get(0).toString().length() != 0) {
            ((DefaultTableModel)var36.getModel()).addRow(new Vector());
            var39.set(0, "***" + var39.get(0));
            ((DefaultTableModel)var36.getModel()).addRow(var39);
         }

         for(var13 = 0; var13 < var38.size(); ++var13) {
            ((DefaultTableModel)var36.getModel()).addRow((Vector)var38.get(var13));
         }

         for(var13 = 0; var13 < var37.size(); ++var13) {
            ((DefaultTableModel)var36.getModel()).insertRow(var13, (Vector)var37.get(var13));
         }

         if (var37.size() != 0 && var40.get(0).toString().length() != 0) {
            var40.set(0, "***" + var40.get(0));
            ((DefaultTableModel)var36.getModel()).insertRow(0, var40);
         }

         for(var13 = 1; var13 < var36.getColumnCount(); ++var13) {
            boolean var41 = false;

            for(int var42 = 1; var42 < var36.getRowCount(); ++var42) {
               String var45 = (String)var36.getValueAt(var42, var13);
               if (var45 != null && !"".equals(var45)) {
                  if (var45.startsWith("<div id=\"tdc\">")) {
                     var41 = true;
                  }

                  if (var45.startsWith("<div id=\"tdr\">")) {
                     var41 = true;
                  }
               }
            }

            String var43 = (String)var36.getValueAt(0, var13);
            if (var43 != null && !"".equals(var43) && var43.indexOf(" id=\"ntd") == -1) {
               var43 = "<div id=\"tdc\">" + var43 + "</div>";
               var36.setValueAt(var43, 0, var13);
            }
         }

         return this.trimtable(var36);
      }
   }

   private void done_vht(Vector var1, JTable var2) {
      Vector var3 = new Vector();
      Hashtable var4 = new Hashtable();

      int var5;
      for(var5 = 0; var5 < var1.size(); ++var5) {
         Hashtable var6 = (Hashtable)var1.get(var5);
         Integer var7 = (Integer)var6.get("index");
         var3.add(var7);
         String var8 = (String)var6.get("gprompt");
         var4.put(var7, var8);
      }

      Collections.sort(var3);
      var5 = -1;

      for(int var9 = var3.size() - 1; 0 <= var9; --var9) {
         int var10 = (Integer)var3.get(var9);
         if (var10 != var5) {
            Vector var11 = new Vector();
            var11.add("***" + var4.get(var3.get(var9)));
            ((DefaultTableModel)var2.getModel()).insertRow(var10, var11);
            var5 = var10;
         }
      }

   }

   private String cut13(String var1) {
      if (var1 == null) {
         return var1;
      } else {
         String var2 = var1.trim();
         if (var2.startsWith("#13")) {
            var2 = var2.substring(3).trim();
         }

         return var2;
      }
   }

   private JTable trimtable(JTable var1) {
      for(int var2 = var1.getRowCount() - 1; 0 < var2; --var2) {
         if (this.isemptyrow(var1, var2)) {
            ((DefaultTableModel)var1.getModel()).removeRow(var2);
         }
      }

      if (this.isemptyrow(var1, 0)) {
         ((DefaultTableModel)var1.getModel()).removeRow(0);
      } else {
         var1.setValueAt("<th>" + var1.getValueAt(0, 0), 0, 0);
      }

      JTable var3 = this.trimcols(var1);
      if (var3.getRowCount() != 0) {
         this.v.add(var3.getModel());
      }

      return var3;
   }

   private int realsize(Vector var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < this.v.size() && this.v.get(var3) != null; ++var3) {
         ++var2;
      }

      return var2;
   }

   private JTable trimcols(JTable var1) {
      DefaultTableModel var2 = new DefaultTableModel();

      for(int var3 = 0; var3 < var1.getColumnCount(); ++var3) {
         boolean var4 = false;

         for(int var5 = 0; var5 < var1.getRowCount(); ++var5) {
            if (this.notempty(var1.getValueAt(var5, var3))) {
               var4 = true;
               break;
            }
         }

         if (var4) {
            Vector var8 = new Vector();

            for(int var6 = 0; var6 < var1.getRowCount(); ++var6) {
               var8.add(var1.getValueAt(var6, var3));
            }

            var2.addColumn(var1.getModel().getColumnName(var3), var8);
         }
      }

      JTable var7 = new JTable(var2);
      return var7;
   }

   private boolean notempty(Object var1) {
      if (var1 == null) {
         return false;
      } else {
         return !"".equals(var1);
      }
   }

   private boolean isemptyrow(JTable var1, int var2) {
      for(int var3 = 0; var3 < var1.getColumnCount(); ++var3) {
         if (var1.getValueAt(var2, var3) != null && !var1.getValueAt(var2, var3).equals("")) {
            return false;
         }
      }

      return true;
   }

   public String tableModels2HtmlTable(Vector var1) {
      StringBuffer var2 = new StringBuffer("<html><body style=\"font-size: 8px;\">");

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         if (var1.get(var3) instanceof String) {
            try {
               if (0 < var3 && var1.get(var3 + 1) instanceof String) {
                  continue;
               }
            } catch (Exception var8) {
               continue;
            }

            var2.append("<h2>").append(var1.get(var3)).append("</h2>");
         } else {
            DefaultTableModel var4 = (DefaultTableModel)var1.get(var3);
            var2.append("<table border=\"0\" cellpaddding=\"2\" cellspacing=\"0\" width=\"100%\">");

            for(int var5 = 0; var5 < var4.getRowCount(); ++var5) {
               var2.append("<tr>");
               int var6 = var4.getColumnCount();

               for(int var7 = 0; var7 < var6; ++var7) {
                  var2.append("<td valign=\"top\">").append(var4.getValueAt(var5, var7) == null ? "&nbsp;" : var4.getValueAt(var5, var7)).append("</td>");
               }
            }

            var2.append("</table>");
         }
      }

      var2.append("</body></html>");
      return var2.toString();
   }
}
