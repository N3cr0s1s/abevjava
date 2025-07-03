package hu.piller.enykp.alogic.fileutil;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.calculator.calculator_c.LookupListModel;
import hu.piller.enykp.alogic.calculator.calculator_c.MatrixSearchItem;
import hu.piller.enykp.alogic.calculator.calculator_c.MatrixSearchModel;
import hu.piller.enykp.alogic.calculator.lookup.LookupListHandler;
import hu.piller.enykp.alogic.calculator.matrices.IMatrixHandler;
import hu.piller.enykp.alogic.calculator.matrices.defaultMatrixHandler;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.metainfo.MetaStore;
import hu.piller.enykp.alogic.templateutils.FieldsGroups;
import hu.piller.enykp.alogic.templateutils.IFieldsGroupModel;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.errordialog.TextWithIcon;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class DataChecker {
   private static final String FIELD_GROUP_ID = "field_group_id";
   private static final String DIN_FIELD_GROUP_ID = "din_field_group_id";
   private static final String DIN_ERTEK_LISTA = "din_ertek_lista";
   IPropertyList iplMaster;
   public static final String NUMS = "+-.0123456789";
   static final String RESOURCE_NAME = "DataChecker";
   boolean ignoreIrid = false;
   boolean maskFormatError = false;
   Object dataType;
   private static final int DATE_MAX_LENGTH = 8;
   private Hashtable iridsTable = new Hashtable();
   private Hashtable checkedFieldGroupIds = new Hashtable();
   private static DataChecker ourInstance = new DataChecker();
   private static final String[] csoportNev = new String[]{"", "", "páros", "hármas", "négyes", "ötös"};

   public static DataChecker getInstance() {
      return ourInstance;
   }

   private DataChecker() {
      try {
         this.iplMaster = PropertyList.getInstance();
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
      }

   }

   public Result superCheck(BookModel var1, boolean var2) {
      return this.superCheck(var1, var2, -1);
   }

   public Result superCheck(BookModel var1, boolean var2, int var3) {
      return new Result();
   }

   public Result hyperCheck(BookModel var1, int var2) {
      Result var3 = new Result();
      this.checkedFieldGroupIds.clear();

      try {
         this.iridsTable = var1.get(((Elem)var1.cc.get(var2)).getType()).irids;
         boolean var4 = false;
         Elem var5 = (Elem)var1.cc.get(var2);
         String[] var6 = (String[])((String[])HeadChecker.getInstance().getHeadData(var1.get(var5.getType()).id, (IDataStore)var5.getRef()));
         String var7 = "";
         if (var1.cc.size() > 1) {
            try {
               var7 = var5.toString() + " ";
               if (var6[7] != null) {
                  var7 = var7 + var6[7];
               }
            } catch (Exception var35) {
               Tools.eLog(var35, 0);
            }
         }

         String var9 = var5.getType();
         var7 = " --- " + var9 + " " + var7;
         Hashtable var8 = var1.get(var9).fids;
         if (var8 == null) {
            if (!var4 && var1.cc.size() > 1) {
               var3.errorList.add(new TextWithIcon(var7, 1, PropertyList.ERRORLIST_PILL_SPECIAL_ERRORS[0], "nem hiba, lapdobás"));
               var4 = true;
            }

            var3.errorList.add(new TextWithIcon("Hiba : hiba a vizuális elemek feldolgozásakor", 1));
            var3.setOk(false);
            return var3;
         } else {
            IDataStore var10 = (IDataStore)((Elem)var1.cc.get(var2)).getRef();
            Iterator var11 = var10.getCaseIdIterator();
            MetaInfo var12 = MetaInfo.getInstance();
            MetaStore var13 = var12.getMetaStore(var9);
            Hashtable var14 = var13.getFieldMetas();
            String var15 = "";
            int var17 = this.getmask();

            while(true) {
               while(true) {
                  boolean var16;
                  StoreItem var18;
                  String var19;
                  String var20;
                  Hashtable var22;
                  String var24;
                  boolean var25;
                  String var39;
                  int var40;
                  while(true) {
                     String var23;
                     while(true) {
                        DataFieldModel var21;
                        do {
                           do {
                              if (!var11.hasNext()) {
                                 try {
                                    if (this.iplMaster.get("error_on_datacheck") != null && this.iplMaster.get("error_on_datacheck") instanceof Vector) {
                                       if (!var4 && var1.cc.size() > 1) {
                                          var3.errorList.add(new TextWithIcon(var7, 1, PropertyList.ERRORLIST_PILL_SPECIAL_ERRORS[0], "nem hiba, lapdobás"));
                                          var4 = true;
                                       }

                                       var3.errorList.addAll((Vector)this.iplMaster.get("error_on_datacheck"));
                                       var3.setOk(false);
                                       return var3;
                                    }
                                 } catch (Exception var27) {
                                    Tools.eLog(var27, 0);
                                 }

                                 return var3;
                              }

                              var18 = (StoreItem)var11.next();
                              var19 = var18.toString();
                           } while(var18.index < 0);

                           var21 = (DataFieldModel)var8.get(var18.code);
                        } while((var21.role & var17) == 0);

                        var16 = false;
                        var22 = var21.features;

                        try {
                           if (var22.get("validation") != null) {
                              var16 = ((String)var22.get("validation")).equalsIgnoreCase("false");
                           }
                        } catch (Exception var34) {
                           var16 = false;
                        }

                        var23 = "";
                        if (var22.get("mask") != null) {
                           var23 = (String)var22.get("mask");
                        }

                        this.ignoreIrid = false;
                        this.maskFormatError = false;

                        try {
                           var25 = ((PageModel)var1.get(var9).fids_page.get(var19.substring(var19.indexOf("_") + 1))).dynamic;
                           Integer var26 = var25 ? Integer.parseInt(var19.substring(0, var19.indexOf("_"))) : null;
                           var24 = MetaInfo.extendedInfoTxt(var19.substring(var19.indexOf("_") + 1), var26, var9, var1);
                        } catch (Exception var33) {
                           var24 = "";
                           Tools.eLog(var33, 0);
                        }

                        try {
                           if (var22 == null) {
                              throw new Exception("Hibás id : " + var19);
                           }

                           var20 = var10.get(var19);
                           if (var20 == null) {
                              if (!this.isDetailAndValueMatch(var18)) {
                                 try {
                                    var3.errorList.add(new TextWithIcon("Figyelem : a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében lévő érték () nem egyezik a tételes adatrögzítés funkció által részletezett adatok összegével. (" + var18.getDetailSum() + ") " + var24, 4, "", "a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében lévő érték () nem egyezik a tételes adatrögzítés funkció által részletezett adatok összegével. (" + var18.getDetailSum() + ")" + var24, var18, var9, var5));
                                 } catch (Exception var32) {
                                 }
                              }
                           } else if (var20.equals("")) {
                              if (!this.isDetailAndValueMatch(var18)) {
                                 try {
                                    var3.errorList.add(new TextWithIcon("Figyelem : a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében lévő érték () nem egyezik a tételes adatrögzítés funkció által részletezett adatok összegével. (" + var18.getDetailSum() + ") " + var24, 4, "", "a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében lévő érték () nem egyezik a tételes adatrögzítés funkció által részletezett adatok összegével. (" + var18.getDetailSum() + ")" + var24, var18, var9, var5));
                                 } catch (Exception var31) {
                                 }
                              }
                           } else if (var22.get("values") != null && !var16 && !this.valuesCheck(var20, (String)var22.get("values"))) {
                              if (!var4 && var1.cc.size() > 1) {
                                 var3.errorList.add(new TextWithIcon(var7, 1, PropertyList.ERRORLIST_PILL_SPECIAL_ERRORS[0], "nem hiba, lapdobás", var18, var9, var5));
                                 var4 = true;
                              }

                              var3.errorList.add(new TextWithIcon("Hiba : a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében " + var20 + " nincs a megadott értéklistában (values). " + var24, 1, "m004", "a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében " + var20 + " nincs a megadott értéklistában (values). " + var24, var18, var9, var5));
                              var3.setOk(false);
                           } else {
                              var39 = "-1";
                              if (var22.get("max_length") != null) {
                                 var39 = (String)var22.get("max_length");
                              }

                              if (var22.get("datatype") != null) {
                                 if (((String)var22.get("datatype")).equalsIgnoreCase("date")) {
                                    if (!this.dateCheck(var20, var23)) {
                                       if (!var4 && var1.cc.size() > 1) {
                                          var3.errorList.add(new TextWithIcon(var7, 1, PropertyList.ERRORLIST_PILL_SPECIAL_ERRORS[0], "nem hiba, lapdobás", var18, var9, var5));
                                          var4 = true;
                                       }

                                       var3.errorList.add(new TextWithIcon("Hiba : a nyomtatvány " + var18.code + " mezőjében " + var20 + " nem megfelelő dátum formátum. " + var24, 1, "m002", "a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében " + var20 + " nem megfelelő dátum formátum. " + var24, var18, var9, var5));
                                       var3.setOk(false);
                                       continue;
                                    }

                                    if (var20.length() > 8) {
                                       var3.errorList.add(new TextWithIcon("Hiba : a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében " + var20 + " hossza nagyobb a megengedettnél (" + 8 + "). " + var24, 1, "m002", "a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében " + var20 + " hossza nagyobb a megengedettnél (" + 8 + "). " + var24, var18, var9, var5));
                                       var3.setOk(false);
                                       continue;
                                    }
                                 } else if (((String)var22.get("datatype")).equalsIgnoreCase("check")) {
                                    this.ignoreIrid = var20.equalsIgnoreCase("true") || var20.equalsIgnoreCase("false") || var20.equalsIgnoreCase("x");
                                    if (this.ignoreIrid) {
                                       continue;
                                    }
                                 }
                              }

                              if (var22.get("type") != null && var22.get("type").equals("2")) {
                                 if (!this.numCheck(var20, var23)) {
                                    if (!var4 && var1.cc.size() > 1) {
                                       var3.errorList.add(new TextWithIcon(var7, 1, PropertyList.ERRORLIST_PILL_SPECIAL_ERRORS[0], "nem hiba, lapdobás"));
                                       var4 = true;
                                    }

                                    var3.errorList.add(new TextWithIcon("Hiba : a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében " + var20 + " számjegyekből és tizedespontból állhat. " + var24, 1, "m002", "a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében " + var20 + " számjegyekből és tizedespontból állhat. " + var24, var18, var9, var5));
                                    var3.setOk(false);
                                    continue;
                                 }

                                 if (var22.get("alignment").equals("right") && var20.indexOf("-") > 0) {
                                    var3.errorList.add(new TextWithIcon("Hiba : a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében " + var20 + " az előjel csak a szám elején lehet. " + var24, 1, "m002", "a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében " + var20 + " az előjel csak a szám elején lehet. " + var24, var18, var9, var5));
                                    var3.setOk(false);
                                    continue;
                                 }
                              }

                              if (var22.get("visible") != null && var22.get("visible").equals("no")) {
                                 try {
                                    var40 = Integer.parseInt(var39);
                                    if (var40 < 256) {
                                       var39 = "256";
                                    }
                                 } catch (Exception var30) {
                                    var39 = "256";
                                 }
                              }

                              if (!var23.equals("")) {
                                 var40 = this.maskCheck(var20, var23, var39);
                                 if (var40 != 0) {
                                    this.maskFormatError = true;
                                    switch(var40) {
                                    case 1:
                                       var15 = "#Hiba : a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében " + var20 + " számjegyekből és tizedespontból állhat. " + var24;
                                       break;
                                    case 2:
                                       var15 = "*Hiba : a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében " + var20 + " nem felel meg a megadott hossznak. (" + var39 + ") " + var24;
                                       break;
                                    case 3:
                                       var15 = "Hiba : a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében " + var20 + " nem írható bele a mezőbe. pl. túl hosszú szöveg, nem megfelelő karakter. " + var24;
                                    }
                                 }
                              }

                              this.dataType = var22.get("datatype");
                              break;
                           }
                        } catch (Exception var36) {
                           var36.printStackTrace();
                           if (!var4 && var1.cc.size() > 1) {
                              var3.errorList.add(new TextWithIcon(var7, 1, PropertyList.ERRORLIST_PILL_SPECIAL_ERRORS[0], "nem hiba, lapdobás"));
                              var4 = true;
                           }

                           var3.errorList.add(new TextWithIcon("Hiba : hiba a vizuális elemek feldolgozásakor", 1));
                           var3.setOk(false);
                        }
                     }

                     try {
                        Hashtable var41 = (Hashtable)var14.get(var18.code);
                        if (var41 != null) {
                           if (this.maskFormatError) {
                              if (var41.get("defaultvalue") == null) {
                                 this.maskFormatError = false;
                                 if (!var4 && var1.cc.size() > 1) {
                                    var3.errorList.add(new TextWithIcon(var7, 1, PropertyList.ERRORLIST_PILL_SPECIAL_ERRORS[0], "nem hiba, lapdobás"));
                                    var4 = true;
                                 }

                                 if (var15.startsWith("*")) {
                                    var3.errorList.add(new TextWithIcon(var15.substring(1), 1, "m003", var15.substring(1), var18, var9, var5));
                                 } else if (var15.startsWith("*")) {
                                    var3.errorList.add(new TextWithIcon(var15.substring(1), 1, "m002", var15.substring(1), var18, var9, var5));
                                 } else {
                                    var3.errorList.add(new TextWithIcon(var15, 1));
                                 }

                                 var3.setOk(false);
                                 continue;
                              }

                              if (!var20.equals(var41.get("defaultvalue"))) {
                                 this.maskFormatError = false;
                                 if (!var4 && var1.cc.size() > 1) {
                                    var3.errorList.add(new TextWithIcon(var7, 1, PropertyList.ERRORLIST_PILL_SPECIAL_ERRORS[0], "nem hiba, lapdobás"));
                                    var4 = true;
                                 }

                                 if (var15.startsWith("*")) {
                                    var3.errorList.add(new TextWithIcon(var15.substring(1), 1, "m003", var15.substring(1), var18, var9, var5));
                                 } else if (var15.startsWith("*")) {
                                    var3.errorList.add(new TextWithIcon(var15.substring(1), 1, "m002", var15.substring(1), var18, var9, var5));
                                 } else {
                                    var3.errorList.add(new TextWithIcon(var15, 1));
                                 }

                                 var3.setOk(false);
                                 continue;
                              }

                              this.maskFormatError = false;
                           }

                           this.maskFormatError = false;
                           if (var41.get("round") != null && !this.roundCheck(var20, (String)var41.get("round"))) {
                              if (!var4 && var1.cc.size() > 1) {
                                 var3.errorList.add(new TextWithIcon(var7, 1, PropertyList.ERRORLIST_PILL_SPECIAL_ERRORS[0], "nem hiba, lapdobás", var18, var9, var5));
                                 var4 = true;
                              }

                              var3.errorList.add(new TextWithIcon("Hiba : a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében " + var20 + " nem megfelelő formátum (kerekítés : " + var41.get("round") + " jegy). " + var24, 1, "m003", "a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében " + var20 + " nem megfelelő formátum (kerekítés : " + var41.get("round") + " jegy). " + var24, var18, var9, var5));
                              var3.setOk(false);
                           } else {
                              if (this.ignoreIrid || var41.get("irids") == null || this.iridCheck(var20, (String)var41.get("irids"), var23, var1, var18.code)) {
                                 var39 = null;
                                 if (!this.isDetailAndValueMatch(var18)) {
                                    var3.errorList.add(new TextWithIcon("Figyelem : a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében lévő " + var20 + " érték nem egyezik a tételes adatrögzítés funkció által részletezett adatok összegével. (" + var18.getDetailSum() + ") " + var24, 4, "", "a nyomtatvány " + this.getKey4Msg(var19) + " mezőjében lévő " + var20 + " érték nem egyezik a tételes adatrögzítés funkció által részletezett adatok összegével. (" + var18.getDetailSum() + ")" + var24, var18, var9, var5));
                                 }
                                 break;
                              }

                              if (!var4 && var1.cc.size() > 1) {
                                 var3.errorList.add(new TextWithIcon(var7, 1, PropertyList.ERRORLIST_PILL_SPECIAL_ERRORS[0], "nem hiba, lapdobás"));
                                 var4 = true;
                              }

                              var3.errorList.add(new TextWithIcon("Hiba : a nyomtatvány " + this.getKey4Msg(var19) + " mezőjébe " + var20 + " formai okok miatt nem írható bele. (irid=" + var41.get("irids") + "). " + var24, 1, "m002", "a nyomtatvány " + this.getKey4Msg(var19) + " mezőjébe " + var20 + " formai okok miatt nem írható bele. (irid=" + var41.get("irids") + "). " + var24, var18, var9, var5));
                              var3.setOk(false);
                           }
                        }
                     } catch (Exception var37) {
                        var37.printStackTrace();
                        if (!var4 && var1.cc.size() > 1) {
                           var3.errorList.add(new TextWithIcon(var7, 1, PropertyList.ERRORLIST_PILL_SPECIAL_ERRORS[0], "nem hiba, lapdobás"));
                           var4 = true;
                        }

                        var3.errorList.add(new TextWithIcon("Hiba : " + var9 + (var1.cc.size() > 1 ? " (" + var2 + ") " : "") + " nyomtatvány " + this.getKey4Msg(var19) + " mezőjében " + var20 + " hiba a metaadatok feldolgozásakor. " + var24, 1));
                        var3.setOk(false);
                     }
                  }

                  if (var22.containsKey("din_ertek_lista")) {
                     var39 = var19.substring(var19.indexOf("_") + 1);
                     var40 = Integer.valueOf(var19.substring(0, var19.indexOf("_")));
                     if (!this.checkDinamikusErtekLista(var3, var9, var39, var40, var20, var18, var5, var24)) {
                        var22 = null;
                        continue;
                     }
                  }

                  var25 = false;
                  if (!var22.containsKey("field_group_id")) {
                     if (!var22.containsKey("din_field_group_id")) {
                        var22 = null;
                        continue;
                     }

                     var25 = true;
                  }

                  if (!var16) {
                     if (!var25) {
                        if (!this.checkedFieldGroupIds.containsKey(var18.index + "_" + var22.get("field_group_id"))) {
                           try {
                              this.handleFriendlyFields(var3, var24, var18, var10, var9, var22, var5);
                           } catch (Exception var29) {
                              var29.printStackTrace();
                           }
                        }
                     } else if (!this.checkedFieldGroupIds.containsKey(var18.index + "_" + var22.get("din_field_group_id"))) {
                        try {
                           this.handleDynamicFriendlyFields(var3, var24, var18, var10, var9, var22, var5, var13);
                        } catch (Exception var28) {
                           var28.printStackTrace();
                        }
                     }

                     var22 = null;
                  }
               }
            }
         }
      } catch (Exception var38) {
         var3.setOk(false);
         var38.printStackTrace();
         var3.errorList.add(new TextWithIcon("Hiba az adatok ellenőrzésekor", 1));
         return var3;
      }
   }

   public boolean dateCheck(String var1, String var2) {
      if (var2.length() > 0) {
         String var3 = this.maskMinus(var1, var2);
         if (var3.trim().length() == 0) {
            return true;
         }
      }

      var1 = var1.replaceAll("\\.", "");
      var1 = var1.replaceAll("-", "");
      String var4 = var2.replaceAll("\\.", "");
      var4 = var4.replaceAll("-", "");

      try {
         SimpleDateFormat var7 = new SimpleDateFormat("yyyyMMdd");
         if (var1.trim().length() < 8) {
            System.out.println("DATUM HIBA AZ UJ MODOSITAS MIATT !");
            throw new ParseException("", 0);
         } else {
            Date var5 = var7.parse(var1);
            if (!var7.format(var5).equals(var1)) {
               throw new ParseException("", 1);
            } else {
               return var4.startsWith("#") || var1.startsWith(var4.replaceAll("#", ""));
            }
         }
      } catch (ParseException var6) {
         return false;
      }
   }

   public boolean lengthCheck(String var1, String var2, String var3) {
      if (var2.equals("")) {
         return true;
      } else if (var2.equals("-1")) {
         return true;
      } else if (this.ignoreIrid) {
         return true;
      } else {
         boolean var4 = false;

         int var7;
         try {
            var7 = Integer.parseInt(var2);
         } catch (NumberFormatException var6) {
            return false;
         }

         if (var7 < var3.length()) {
            var3 = var3.replaceAll("-", "");
            var3 = var3.replaceAll("\\.", "");
            var3 = var3.replaceAll("%", "");
            int var5 = var3.indexOf(33);
            if (var5 > -1) {
               var3 = var3.substring(0, var5);
            }

            if (var7 < var3.length()) {
               var7 = var3.length();
            }
         }

         return var1.length() <= var7;
      }
   }

   public boolean iridCheck(String var1, String var2, String var3, BookModel var4, String var5) {
      if (!this.iridsTable.containsKey(var2)) {
         return true;
      } else {
         String var6 = (String)this.iridsTable.get(var2);
         if (this.dataType != null && this.isTextArea(var4, var5)) {
            var6 = var6.replace("[", "[\\x09-\\x20][");
            var6 = "[" + var6 + "]";
         }

         if (!var6.endsWith("*")) {
            var6 = var6 + "*";
         }

         if (!var1.matches(var6)) {
            if (!var1.toUpperCase().matches(var6)) {
               var1 = this.maskMinus(var1, var3);
               return !var1.matches(var6) ? var1.toUpperCase().matches(var6) : true;
            } else {
               return true;
            }
         } else {
            return true;
         }
      }
   }

   public boolean maskCheck(String var1, String var2) {
      if (var2.startsWith("%")) {
         return var2.indexOf("!") <= -1 && var2.indexOf("\\") <= -1 ? var1.equalsIgnoreCase(var2.substring(1)) : this.numCheck(var1, var2);
      } else if (var2.startsWith("#")) {
         int var3 = this.countSubStr(var2, "#");
         return var1.length() <= var3;
      } else {
         return true;
      }
   }

   public int maskCheck(String var1, String var2, String var3) {
      if (var2.startsWith("%")) {
         if (var2.indexOf("!") <= -1 && var2.indexOf("\\") <= -1) {
            if (var2.length() == 1) {
               return this.lengthCheck(var1, var3, var2) ? 0 : 2;
            } else {
               String var8 = var2.substring(1).replaceAll("-", "\\-");
               var8 = var8.replaceAll("\\(", "\\\\(");
               var8 = var8.replaceAll("\\)", "\\\\)");
               return var1.matches(var8) ? 0 : 3;
            }
         } else {
            return this.lengthCheck(var1, var3, var2) ? 0 : 2;
         }
      } else if (var2.indexOf("#") <= -1) {
         return this.lengthCheck(var1, var3, var2) ? 0 : 2;
      } else {
         var2 = var2.replaceAll("\\\\", "");
         var2 = var2.replaceAll("\\.", "");
         var2 = var2.replaceAll(",", "");
         var2 = var2.replaceAll(" ", "");
         var2 = var2.replaceAll("/", "");
         StringBuffer var4 = new StringBuffer();

         for(int var5 = 0; var5 < var1.length(); ++var5) {
            try {
               if (var2.charAt(var5) != var1.charAt(var5)) {
                  var4.append(var1.charAt(var5));
               }
            } catch (Exception var7) {
               return 2;
            }
         }

         return this.lengthCheck(var4.toString(), var3, var2) ? 0 : 2;
      }
   }

   public boolean valuesCheck(String var1, String var2) {
      if (var1.equals("")) {
         return true;
      } else {
         var2 = var2.replaceAll(" ", "");
         var1 = var1.replaceAll(" ", "");
         if (("," + var2 + ",").toLowerCase().indexOf(("," + var1 + ",").toLowerCase()) < 0) {
            var1 = var1.replaceAll("/", "//");
            var1 = var1.replaceAll(",", "/,");
            var1 = var1.replaceAll(";", ",");
            if (var1.equals("1")) {
               if (("," + var2 + ",").toLowerCase().indexOf(("," + var1 + ",").toLowerCase()) < 0) {
                  return ("," + var2 + ",").toLowerCase().indexOf(",+1,".toLowerCase()) > -1;
               } else {
                  return true;
               }
            } else {
               return ("," + var2 + ",").toLowerCase().indexOf(("," + var1 + ",").toLowerCase()) > -1;
            }
         } else {
            return true;
         }
      }
   }

   public boolean numCheck(String var1, String var2) {
      int var3 = 0;

      boolean var4;
      for(var4 = false; var3 < var1.length() && !var4; ++var3) {
         if ("+-.0123456789".indexOf(var1.charAt(var3)) == -1) {
            var4 = true;
         }
      }

      if (var4) {
         var4 = false;
         var3 = 0;

         for(var1 = this.maskMinus(var1, var2); var3 < var1.length() && !var4; ++var3) {
            if ("+-.0123456789".indexOf(var1.charAt(var3)) == -1) {
               var4 = true;
            }
         }
      }

      if (var1.indexOf(".") != var1.lastIndexOf(".")) {
         return false;
      } else {
         return !var4;
      }
   }

   public int countSubStr(String var1, String var2) {
      int var3 = 0;

      for(int var4 = 0; var4 <= var1.length() - var2.length(); ++var4) {
         if (var1.substring(var4, var4 + var2.length()).equals(var2)) {
            ++var3;
         }
      }

      return var3;
   }

   public boolean set(Object var1, Object var2) {
      if (!var1.equals("set_irids")) {
         return false;
      } else if (!(var2 instanceof Vector)) {
         return false;
      } else if (var2 == null) {
         return false;
      } else {
         this.iridsTable.clear();
         Vector var3 = (Vector)var2;

         for(int var4 = 0; var4 < var3.size(); ++var4) {
            Hashtable var5 = (Hashtable)((Object[])((Object[])var3.elementAt(var4)))[1];
            this.iridsTable.put(var5.get("irid"), var5.get("irule"));
         }

         return true;
      }
   }

   private String maskMinus(String var1, String var2) {
      if (!var2.equals("") && !var2.startsWith("%")) {
         StringBuffer var3 = new StringBuffer();

         for(int var4 = 0; var4 < var1.length(); ++var4) {
            try {
               if (var1.charAt(var4) != var2.charAt(var4)) {
                  var3.append(var1.charAt(var4));
               }
            } catch (Exception var6) {
               Tools.eLog(var6, 0);
            }
         }

         return var3.toString();
      } else {
         return var1;
      }
   }

   public Result checkField(BookModel var1, String var2, String var3, String var4) {
      return this.checkField(var1, var2, var3, var4, var3, 0, -1);
   }

   public Result checkField(BookModel var1, String var2, String var3, String var4, int var5) {
      return this.checkField(var1, var2, var3, var4, var3, 2, var5);
   }

   public Result checkField(BookModel var1, String var2, String var3, String var4, String var5) {
      return this.checkField(var1, var2, var3, var4, var5, 0, -1);
   }

   public Result checkField(BookModel var1, String var2, String var3, String var4, String var5, int var6, int var7) {
      Result var8 = new Result();

      try {
         this.iridsTable = var1.get(var2).irids;
      } catch (Exception var23) {
         this.iridsTable = new Hashtable();
      }

      MetaInfo var9 = MetaInfo.getInstance();
      MetaStore var10 = var9.getMetaStore(var2);
      Hashtable var11 = var10.getFieldMetas();
      Hashtable var12 = var1.get(var2).fids;
      String var14 = "";
      this.checkedFieldGroupIds.clear();
      int var15 = 0;

      while(var15 == 0) {
         ++var15;
         Hashtable var16 = ((DataFieldModel)var12.get(var3)).features;
         boolean var13 = false;

         try {
            if (var16.get("validation") != null) {
               var13 = ((String)var16.get("validation")).equalsIgnoreCase("false");
            }
         } catch (Exception var22) {
            var13 = false;
         }

         String var17 = "";
         if (var16.get("mask") != null) {
            var17 = (String)var16.get("mask");
         }

         this.ignoreIrid = false;
         this.maskFormatError = false;

         String var18;
         try {
            if (var16 == null) {
               throw new Exception("Hibás id : " + var5);
            }

            if (var4 == null || var4.equals("")) {
               continue;
            }

            var18 = "-1";
            if (var16.get("max_length") != null) {
               var18 = (String)var16.get("max_length");
            }

            if (var16.get("datatype") != null) {
               if (((String)var16.get("datatype")).equalsIgnoreCase("date")) {
                  if (!this.dateCheck(var4, var17)) {
                     var8.errorList.add(new TextWithIcon("Hiba : a nyomtatvány " + this.getKey4Msg(var3) + " mezőjében " + var4 + " nem megfelelő dátum formátum. " + var5, 1, "m002", "a nyomtatvány " + this.getKey4Msg(var3) + " mezőjében " + var4 + " nem megfelelő dátum formátum. " + var5));
                     var8.setOk(false);
                     continue;
                  }

                  if (var4.length() > 8) {
                     var8.errorList.add(new TextWithIcon("Hiba : a nyomtatvány " + this.getKey4Msg(var3) + " mezőjében " + var4 + " hossza nagyobb a megengedettnél (" + 8 + "). " + var5, 1, "m002", "a nyomtatvány " + this.getKey4Msg(var3) + " mezőjében " + var4 + " hossza nagyobb a megengedettnél (" + 8 + "). " + var5));
                     var8.setOk(false);
                     continue;
                  }
               } else if (((String)var16.get("datatype")).equalsIgnoreCase("check")) {
                  this.ignoreIrid = var4.equalsIgnoreCase("true") || var4.equalsIgnoreCase("false") || var4.equalsIgnoreCase("x");
                  if (this.ignoreIrid && var6 != 0) {
                     continue;
                  }
               }
            }

            if (var16.get("type") != null && var16.get("type").equals("2")) {
               if (!this.numCheck(var4, var17)) {
                  var8.errorList.add(new TextWithIcon("Hiba : a nyomtatvány " + this.getKey4Msg(var3) + " mezőjében " + var4 + " számjegyekből és tizedespontból állhat. " + var5, 1, "m002", "a nyomtatvány " + this.getKey4Msg(var3) + " mezőjében " + var4 + " számjegyekből és tizedespontból állhat. " + var5));
                  var8.setOk(false);
                  continue;
               }

               if (var16.get("alignment").equals("right") && var4.indexOf("-") > 0) {
                  var8.errorList.add(new TextWithIcon("Hiba : a nyomtatvány " + this.getKey4Msg(var3) + " mezőjében " + var4 + " az előjel csak a szám elején lehet. " + var5, 1, "m002", "a nyomtatvány " + this.getKey4Msg(var3) + " mezőjében " + var4 + " az előjel csak a szám elején lehet " + var5));
                  var8.setOk(false);
                  continue;
               }
            }

            int var19;
            if (var16.get("visible") != null && var16.get("visible").equals("no")) {
               try {
                  var19 = Integer.parseInt(var18);
                  if (var19 < 256) {
                     var18 = "256";
                  }
               } catch (Exception var21) {
                  var18 = "256";
               }
            }

            if (!var17.equals("")) {
               var19 = this.maskCheck(var4, var17, var18);
               if (var19 != 0) {
                  this.maskFormatError = true;
                  switch(var19) {
                  case 1:
                     var14 = "#Hiba : a nyomtatvány " + this.getKey4Msg(var3) + " mezőjében " + var4 + " számjegyekből és tizedespontból állhat. " + var5;
                     break;
                  case 2:
                     var14 = "*Hiba : a nyomtatvány " + this.getKey4Msg(var3) + " mezőjében " + var4 + " nem felel meg a megadott hossznak. (" + var18 + "). " + var5;
                     break;
                  case 3:
                     var14 = "Hiba : a nyomtatvány " + this.getKey4Msg(var3) + " mezőjében " + var4 + " nem írható bele a mezőbe. pl. túl hosszú szöveg, nem megfelelő karakter. " + var5;
                  }
               }
            }

            if (var16.get("values") != null && !var13 && !this.valuesCheck(var4, (String)var16.get("values"))) {
               var8.errorList.add(new TextWithIcon("Hiba : a nyomtatvány " + this.getKey4Msg(var3) + " mezőjében " + var4 + " nincs a megadott értéklistában (values). " + var5, 1, "m004", "a nyomtatvány " + this.getKey4Msg(var3) + " mezőjében " + var4 + " nincs a megadott értéklistában (values). " + var5));
               var8.setOk(false);
               continue;
            }

            this.dataType = var16.get("datatype");
         } catch (Exception var25) {
            var25.printStackTrace();

            try {
               var8.errorList.add(new TextWithIcon("Hiba : hiba a vizuális elemek feldolgozásakor (" + var5 + ")", 1));
            } catch (Exception var20) {
               var8.errorList.add(new TextWithIcon("Hiba : hiba a vizuális elemek feldolgozásakor ()", 1));
            }

            var8.setOk(false);
            continue;
         }

         try {
            Hashtable var26 = (Hashtable)var11.get(var3);
            if (var26 == null) {
               continue;
            }

            if (this.maskFormatError) {
               if (var26.get("defaultvalue") == null) {
                  this.maskFormatError = false;
                  if (var14.startsWith("*")) {
                     var8.errorList.add(new TextWithIcon(var14.substring(1), 1, "m003", var14.substring(1)));
                  } else if (var14.startsWith("*")) {
                     var8.errorList.add(new TextWithIcon(var14.substring(1), 1, "m002", var14.substring(1)));
                  } else {
                     var8.errorList.add(new TextWithIcon(var14, 1));
                  }

                  var8.setOk(false);
                  continue;
               }

               if (!var4.equals(var26.get("defaultvalue"))) {
                  this.maskFormatError = false;
                  if (var14.startsWith("*")) {
                     var8.errorList.add(new TextWithIcon(var14.substring(1), 1, "m003", var14.substring(1)));
                  } else if (var14.startsWith("*")) {
                     var8.errorList.add(new TextWithIcon(var14.substring(1), 1, "m002", var14.substring(1)));
                  } else {
                     var8.errorList.add(new TextWithIcon(var14, 1));
                  }

                  var8.setOk(false);
                  continue;
               }

               this.maskFormatError = false;
            }

            this.maskFormatError = false;
            if (!this.ignoreIrid && var26.get("irids") != null && !this.iridCheck(var4, (String)var26.get("irids"), var17, var1, var3)) {
               var8.errorList.add(new TextWithIcon("Hiba : a nyomtatvány " + this.getKey4Msg(var3) + " mezőjébe " + var4 + " formai okok miatt nem írható bele. (irid=" + var26.get("irids") + "). " + var5, 1));
               var8.setOk(false);
               continue;
            }

            var18 = null;
         } catch (Exception var24) {
            var24.printStackTrace();
            var8.errorList.add(new TextWithIcon("Hiba : " + var2 + " nyomtatvány " + this.getKey4Msg(var3) + " mezőjében " + var4 + " hiba a metaadatok feldolgozásakor. " + var5, 1));
            var8.setOk(false);
            continue;
         }

         if (var6 != 2) {
            var16 = null;
         }
      }

      return var8;
   }

   public boolean forintE(Hashtable var1) {
      if (((String)var1.get("datatype")).equalsIgnoreCase("date")) {
         return false;
      } else if (((String)var1.get("alignment")).equalsIgnoreCase("left")) {
         return false;
      } else {
         String var2 = (String)var1.get("irids");
         return ((String)var1.get("type")).equalsIgnoreCase("2");
      }
   }

   public int getmask() {
      if (MainFrame.role.equals("0")) {
         return 1;
      } else if (MainFrame.role.equals("1")) {
         return 2;
      } else if (MainFrame.role.equals("2")) {
         return 4;
      } else {
         return MainFrame.role.equals("3") ? 8 : 1;
      }
   }

   private boolean checkDinamikusErtekLista(Result var1, String var2, String var3, int var4, String var5, StoreItem var6, Elem var7, String var8) {
      try {
         if (!LookupListHandler.getInstance().getLookupListProvider(var2, var3).validate(var4, var5)) {
            this.fillInErrorList(var1, var2, var3, var5, var6, var7, var8);
            return false;
         } else {
            return true;
         }
      } catch (Exception var10) {
         this.fillInErrorList(var1, var2, var3, var5, var6, var7, var8);
         return false;
      }
   }

   private void fillInErrorList(Result var1, String var2, String var3, String var4, StoreItem var5, Elem var6, String var7) {
      String var8 = "a nyomtatvány " + var5.code + " mezőjében " + (var4 != null && var4.length() != 0 ? var4 : "'üres'") + " érték nem adható meg. " + var7;
      var1.setOk(false);
      var1.errorList.add(new TextWithIcon("Hiba : " + var8, 1, "m004", var8, var5, var2, var6));
   }

   private boolean handleFriendlyFields(Result var1, String var2, StoreItem var3, IDataStore var4, String var5, Hashtable var6, Elem var7) {
      FieldsGroups var8 = FieldsGroups.getInstance();
      IFieldsGroupModel var9 = var8.getFieldsGroupByGroupId(FieldsGroups.GroupType.STATIC, var5, (String)var6.get("field_group_id"));
      Hashtable var10 = var9.getFidsColumns();
      Hashtable var11 = this.getFriendValues(var10, var4, var3.index);
      return this.friendlyCheck(var1, var2, var3, var7, var10, var11, var5, (String)var6.get("matrix_id"), (String)var6.get("fid"), var4, (String)var6.get("matrix_delimiter"), (String)var6.get("field_group_id"));
   }

   public boolean friendlyCheck(Result var1, String var2, StoreItem var3, Elem var4, Hashtable var5, Hashtable var6, String var7, String var8, String var9, IDataStore var10, String var11, String var12) {
      boolean var13 = true;
      boolean var14 = false;
      int var15 = var5.size();
      int[] var16 = this.getIndexes(var6, var15);
      int var17 = this.getMaxElem(var16) + 1;
      Enumeration var18 = var6.keys();

      while(var18.hasMoreElements()) {
         Object var19 = var18.nextElement();
         Vector var20 = (Vector)var6.get(var19);
         String[] var21 = new String[var17];
         ArrayList var22 = new ArrayList(var16.length);

         for(int var23 = 0; var23 < var16.length; ++var23) {
            var22.add(new MatrixSearchItem(var16[var23], "=", var20.elementAt(var23), var14));
         }

         var20.toArray(var21);
         var13 = this.isInTheMatrix(var7, var8, var22, var11);
         if (!var13) {
            String var27 = "";

            for(int var24 = 0; var24 < var21.length; ++var24) {
               var27 = var27 + ", '" + var21[var24] + "'";
            }

            if (var27.startsWith(", ")) {
               var27 = var27.substring(2);
            }

            var1.setOk(false);
            String var28 = "";

            try {
               var28 = this.getMatrixErrorMessage(var5, var2, var27, csoportNev[var21.length]);
            } catch (Exception var26) {
               var28 = "a nyomtatvány " + var3.code + " mezőjében " + var27 + " érték nem adható meg. " + var2;
            }

            var1.errorList.add(new TextWithIcon("Hiba : " + var28, 1, "m004", var28, var3, var7, var4));
            this.checkedFieldGroupIds.put(var3.index + "_" + var12, "");
         }

         if (!var13) {
            break;
         }
      }

      return var13;
   }

   private String getMatrixErrorMessage(Hashtable var1, String var2, String var3, String var4) {
      String var6 = "";
      Enumeration var7 = var1.keys();

      String var8;
      String var9;
      for(var8 = "mezőjében"; var7.hasMoreElements(); var6 = var6 + ", " + var9) {
         var9 = (String)var7.nextElement();
      }

      try {
         var6 = var6.substring(2);
      } catch (Exception var10) {
      }

      if (var1.size() > 1) {
         var8 = "mezőiben";
      }

      String var5 = "a nyomtatvány " + var6 + " " + var8 + " " + var3 + " érték" + var4 + " nem adható meg. " + var2;
      var5 = var5.replaceAll("''", "'üres'");
      return var5;
   }

   private Hashtable getFriendValues(Hashtable var1, int var2, IDataStore var3) {
      Hashtable var4 = new Hashtable();

      for(int var5 = 0; var5 < var2; ++var5) {
         Enumeration var6 = var1.keys();

         while(var6.hasMoreElements()) {
            String var7 = (String)var6.nextElement();
            this.putIntoHt(var4, var5, var3.get(var5 + "_" + var7) == null ? "" : var3.get(var5 + "_" + var7));
            this.putIntoHt(var4, "INDEXES", Integer.valueOf((String)var1.get(var7)));
         }
      }

      return var4;
   }

   private Hashtable getFriendValues(Hashtable var1, IDataStore var2, int var3) {
      Hashtable var4 = new Hashtable();
      Enumeration var5 = var1.keys();

      while(var5.hasMoreElements()) {
         String var6 = (String)var5.nextElement();
         this.putIntoHt(var4, var3, var2.get(var3 + "_" + var6) == null ? "" : var2.get(var3 + "_" + var6));
         this.putIntoHt(var4, "INDEXES", Integer.valueOf((String)var1.get(var6)));
      }

      return var4;
   }

   private boolean isInTheMatrix(String var1, String var2, List<MatrixSearchItem> var3, String var4) {
      IMatrixHandler var5 = defaultMatrixHandler.getInstance();
      MatrixSearchModel var6 = new MatrixSearchModel(var2, var4);
      Iterator var7 = var3.iterator();

      while(var7.hasNext()) {
         MatrixSearchItem var8 = (MatrixSearchItem)var7.next();
         var6.addSearchItem(var8);
      }

      Vector var9 = var5.search(var1, var6, true, false);
      return var9 != null;
   }

   private int getMaxPageNum(BookModel var1, String var2, String var3, Elem var4) {
      int var5 = var1.get(var2).get_field_pageindex(var3);
      int[] var6 = (int[])((int[])var4.getEtc().get("pagecounts"));
      return var6[var5];
   }

   private void putIntoHt(Hashtable var1, Object var2, Object var3) {
      Vector var4;
      if (var1.containsKey(var2)) {
         var4 = (Vector)var1.get(var2);
      } else {
         var4 = new Vector();
      }

      var4.add(var3);
      var1.put(var2, var4);
   }

   private int[] getIndexes(Hashtable var1, int var2) {
      Vector var3 = (Vector)var1.remove("INDEXES");
      int[] var4 = new int[var2];

      for(int var5 = 0; var5 < var4.length; ++var5) {
         try {
            var4[var5] = (Integer)var3.elementAt(var5) - 1;
         } catch (Exception var7) {
            var4[var5] = -1;
         }
      }

      return var4;
   }

   private int getMaxElem(int[] var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var1[var3] > var2) {
            var2 = var1[var3];
         }
      }

      return var2;
   }

   private boolean roundCheck(String var1, String var2) {
      return true;
   }

   private String getKey4Msg(String var1) {
      int var2 = var1.indexOf("_");
      return var2 < 0 ? var1 : var1.substring(var2);
   }

   public boolean lehetEMinusz(String var1) {
      try {
         if (!var1.endsWith("*")) {
            var1 = var1 + "*";
         }

         return "-".matches(var1);
      } catch (Exception var3) {
         return true;
      }
   }

   private boolean isDetailAndValueMatch(StoreItem var1) {
      return var1.getDetails() == null ? true : (var1.value == null ? "" : var1.value.toString()).equals(var1.getDetailSum());
   }

   private boolean handleDynamicFriendlyFields(Result var1, String var2, StoreItem var3, IDataStore var4, String var5, Hashtable var6, Elem var7, MetaStore var8) {
      FieldsGroups var9 = FieldsGroups.getInstance();
      IFieldsGroupModel var10 = var9.getFieldsGroupByGroupId(FieldsGroups.GroupType.DINAMYC, var5, (String)var6.get("din_field_group_id"));
      if (var10 == null) {
         return true;
      } else {
         Hashtable var11 = var10.getFidsColumns();
         this.getDynamicFriendValues(var11, var4, var3.index);
         return this.checkDinamycGroupFields(var5, var3.code, var11, var1, var2, var3, var7, (String)var6.get("din_field_group_id"), var8);
      }
   }

   private void getDynamicFriendValues(Hashtable<String, String> var1, IDataStore var2, int var3) {
      Enumeration var4 = var1.keys();

      while(var4.hasMoreElements()) {
         String var5 = (String)var4.nextElement();
         var1.put(var5, var2.get(var3 + "_" + var5) == null ? "" : var2.get(var3 + "_" + var5));
      }

   }

   public boolean checkDinamycGroupFields(String var1, String var2, Hashtable<String, String> var3, Result var4, String var5, StoreItem var6, Elem var7, String var8, MetaStore var9) {
      try {
         IFieldsGroupModel var10 = FieldsGroups.getInstance().getFieldsGroupByFid(FieldsGroups.GroupType.DINAMYC, var1, var2);
         if (var10 != null) {
            String var11 = "";
            String var12 = "";
            Hashtable var13;
            if (var9 != null) {
               var13 = var9.getFieldMetas();
            } else {
               var13 = new Hashtable();
            }

            String var14 = (String)var3.get(var2);
            if (var14 == null) {
               var14 = "";
            }

            String var15 = "##_Darth@Vader_&&";
            String var16 = "";
            Enumeration var17 = var3.keys();

            while(var17.hasMoreElements()) {
               String var18 = (String)var17.nextElement();
               if (!var2.equals(var18)) {
                  var15 = (String)var3.get(var18);
                  var16 = var18;
               }
            }

            Hashtable var28;
            try {
               var28 = (Hashtable)var13.get(var6.code);
               if (var28 != null && var28.get("round") != null) {
                  var11 = (String)var28.get("round");
               }
            } catch (Exception var26) {
               var11 = "";
            }

            try {
               var28 = (Hashtable)var13.get(var16);
               if (var28 != null && var28.get("round") != null) {
                  var12 = (String)var28.get("round");
               }
            } catch (Exception var25) {
               var12 = "";
            }

            if (var3.size() != 2) {
               return true;
            }

            LookupListModel var19 = CalculatorManager.getInstance().get_field_lookup_create(var2, var6.index);
            if (var19.isOverWrite()) {
               return true;
            }

            try {
               LookupListModel var20 = CalculatorManager.getInstance().get_field_lookup_create(var16, var6.index);
               if (var20.isOverWrite()) {
                  return true;
               }
            } catch (Exception var24) {
               System.out.println("friendlyField check warning");
            }

            Vector var29 = defaultMatrixHandler.getInstance().search(var1, var19.getMatrixSearchModel(), false, false);
            if (var29 == null) {
               return false;
            }

            boolean var21 = false;

            for(int var22 = 0; var22 < var29.size() && !var21; ++var22) {
               String[] var23 = (String[])((String[])var29.elementAt(var22));
               var21 = this.checkIfValuesOk(var23, var14, var11, var15, var12, var3.size());
            }

            if (!var21) {
               if ("".equals(var15)) {
                  var15 = "'üres'";
               }

               if ("".equals(var14)) {
                  var14 = "'üres'";
               }

               String var30 = "Hiba : " + var1 + ":" + " nyomtatvány " + var2 + " és " + var16 + " mezőiben " + var14 + " és " + var15 + " értékek együtt nem állhatnak! " + var5;
               var4.errorList.add(new TextWithIcon(var30, 1, "m004", var30, var6, var1, var7));
               this.checkedFieldGroupIds.put(var6.index + "_" + var8, "");
               var4.setOk(false);
            }
         }

         return false;
      } catch (Exception var27) {
         ErrorList.getInstance().writeError(new Long(0L), "Hiba történt az ellenőrzés futtatása közben (DC) !", var27, (Object)null);
         return false;
      }
   }

   private boolean checkIfValuesOk(String[] var1, String var2, String var3, String var4, String var5, int var6) {
      int[] var7 = new int[]{0, 0};

      for(int var8 = 0; var8 < var1.length; ++var8) {
         if (this.equalsIgnoreRound(var2, var1[var8], var3)) {
            var7[0] = 1;
         }

         if (this.equalsIgnoreRound(var4, var1[var8], var5)) {
            var7[1] = 1;
         }
      }

      return var7[0] + var7[1] >= var6;
   }

   private boolean equalsIgnoreRound(String var1, String var2, String var3) {
      try {
         if ("".equals(var3)) {
            return var1.equalsIgnoreCase(var2);
         } else {
            if (var1.length() > var2.length()) {
               String var4 = var1;
               var1 = var2;
               var2 = var4;
            }

            boolean var7 = false;
            if (!var1.equalsIgnoreCase(var2.substring(0, var1.length()))) {
               return false;
            } else {
               for(int var5 = var1.length(); var5 < var2.length(); ++var5) {
                  if (var2.charAt(var5) != '0' && var2.charAt(var5) != '.') {
                     var7 = true;
                  }
               }

               return !var7;
            }
         }
      } catch (Exception var6) {
         System.out.println("Hiba a kerekítés ellenőrzésekor: " + var6.toString());
         return true;
      }
   }

   private boolean isTextArea(BookModel var1, String var2) {
      return ((String)this.dataType).toLowerCase().endsWith("tatext") || var1.isInTextArea(var2);
   }
}
