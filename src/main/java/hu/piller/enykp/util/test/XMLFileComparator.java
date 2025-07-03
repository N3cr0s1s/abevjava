package hu.piller.enykp.util.test;

import hu.piller.enykp.util.base.Tools;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLFileComparator implements Comparator {
   private static int NOT_EQUAL_VALUE = -1;
   private static int EQUAL_VALUE = 0;
   private XMLFileComparator.XmlHandler xml_handler;
   private StringBuffer character_buffer;

   public int compare(Object var1, Object var2) {
      int var3;
      if (var1 instanceof File && var2 instanceof File) {
         var3 = this.compareFiles((File)var1, (File)var2);
      } else {
         var3 = NOT_EQUAL_VALUE;
      }

      return var3;
   }

   public boolean logicallyEqual(File var1, File var2) {
      Hashtable[] var3 = this.getFileStructures(var1, var2);
      return var3[0].equals(var3[1]);
   }

   public int compareFiles(File var1, File var2) {
      Hashtable[] var3 = this.getFileStructures(var1, var2);
      return var3[0].equals(var3[1]) ? EQUAL_VALUE : var3[0].toString().compareTo(var3[1].toString());
   }

   public Vector differences(File var1, File var2) {
      return this.differences(var1, var2, (Vector)null);
   }

   public Vector differences(File var1, File var2, Vector var3) {
      Vector var4 = new Vector();
      Exception var5 = null;
      Hashtable[] var6 = null;

      try {
         var6 = this.getFileStructures(var1, var2);
      } catch (Exception var8) {
         var5 = var8;
      }

      var4.add((Object)null);
      var4.add(var5);
      var4.add(this.getDifferences(new Object[]{var1, var2}, var6, var3));
      if (var6 != null && var6.length == 2 && var5 == null) {
         var4.setElementAt(((Vector)var4.get(2)).size() == 0 ? Boolean.TRUE : Boolean.FALSE, 0);
      }

      return var4;
   }

   private Hashtable[] getFileStructures(File var1, File var2) {
      Hashtable var3 = this.parseXMLFile(var1);
      Hashtable var4 = this.parseXMLFile(var2);
      return new Hashtable[]{var3, var4};
   }

   private Hashtable parseXMLFile(File var1) {
      SAXParserFactory var2 = SAXParserFactory.newInstance();
      BufferedInputStream var4 = null;
      XMLFileComparator.XmlHandler var5 = null;

      try {
         var4 = new BufferedInputStream(new FileInputStream(var1));
         var5 = this.getXmlHandler();
         SAXParser var3 = var2.newSAXParser();
         var3.parse(var4, var5);
      } catch (Exception var14) {
         throw new RuntimeException(var14);
      } finally {
         try {
            if (var4 != null) {
               var4.close();
            }
         } catch (IOException var13) {
            Tools.eLog(var13, 0);
         }

      }

      return var5 == null ? new Hashtable(0) : var5.getXmlStructure();
   }

   private XMLFileComparator.XmlHandler getXmlHandler() {
      if (this.xml_handler == null) {
         this.xml_handler = new XMLFileComparator.XmlHandler(this.getCharacterBuffer());
      }

      return this.xml_handler;
   }

   private StringBuffer getCharacterBuffer() {
      if (this.character_buffer == null) {
         this.character_buffer = new StringBuffer(1024);
      }

      return this.character_buffer;
   }

   private void removeKey(Hashtable var1, String var2) {
      var1.remove(var2);
      Iterator var3 = var1.values().iterator();

      while(var3.hasNext()) {
         this.arrangeRemoveKey(var3.next(), var2);
      }

   }

   private void removeKey(Vector var1, String var2) {
      int var3 = 0;

      for(int var4 = var1.size(); var3 < var4; ++var3) {
         this.arrangeRemoveKey(var1.get(var3), var2);
      }

   }

   private void arrangeRemoveKey(Object var1, String var2) {
      if (var1 instanceof Hashtable) {
         this.removeKey((Hashtable)var1, var2);
      } else if (var1 instanceof Vector) {
         this.removeKey((Vector)var1, var2);
      }

   }

   private Vector getDifferences(Object[] var1, Hashtable[] var2, Vector var3) {
      Vector var4 = new Vector();
      XMLFileComparator.Differencer.collectDiffs(var1, var2, var3, var4);
      return var4;
   }

   private static class Differencer {
      private static final String different_value = "Érték eltérés!";
      private static final String different_attribute = "Attributum eltérés!";
      private static final String no_exist_path = "Nem létező útvonal!";
      private static final String extra_path = "Többlet útvonal!";
      private static final int FALSE_NO_MATCH = 0;
      private static final int TRUE_PARTIAL_MATCH = 1;
      private static final int TRUE_FULL_MATCH = 2;

      public static void collectDiffs(Object[] var0, Hashtable[] var1, Vector var2, Vector var3) {
         walkAndCheckOther(var1[0], var1[1], var2, var3, var0[0]);
         walkAndCheckOther(var1[1], var1[0], var2, var3, var0[1]);
      }

      private static void walkAndCheckOther(Hashtable var0, Hashtable var1, Vector var2, Vector var3, Object var4) {
         walkOnHashtable(var0, var1, var2, var3, var4, false, (XMLFileComparator.Differencer.CompareStructure)null);
      }

      private static XMLFileComparator.Differencer.ValidationValue walkOnHashtable(Hashtable var0, Hashtable var1, Vector var2, Vector var3, Object var4, boolean var5, XMLFileComparator.Differencer.CompareStructure var6) {
         String var8 = getPath((String)var0.get("path"));
         String var9 = getIndexedPath((String)var0.get("indexed_path"));
         String var10 = (String)var0.get("characters");
         Hashtable var11 = (Hashtable)var0.get("attributes");
         XMLFileComparator.Differencer.ValidationValue var7;
         if (var5) {
            byte var10001;
            label81: {
               label80: {
                  var7 = new XMLFileComparator.Differencer.ValidationValue(0, false, false);
                  if (var8.length() > 0) {
                     if (var6.path.startsWith(var8)) {
                        break label80;
                     }
                  } else if (var6.path.length() == 0) {
                     break label80;
                  }

                  var10001 = 0;
                  break label81;
               }

               var10001 = 1;
            }

            var7.path_equal = var10001;
            if (var7.path_equal == 1) {
               var7.path_equal = operatorOr(var7.path_equal, var6.path.equals(var8) ? 2 : 0);
            }

            if (var7.path_equal == 2) {
               var7.attribute_found = var11 == null ? var6.attributes == null : var11.equals(var6.attributes);
               var7.value_found = var10 == null ? var6.value == null : var10.equals(var6.value);
            }
         } else {
            var7 = isValidPath(var1, var0, var2);
            if (var7.path_equal == 0) {
               addDiffItem(var3, var4, var9, "Nem létező útvonal!", (Object)null, var11);
            } else if (var7.path_equal == 1) {
               addDiffItem(var3, var4, var9, "Nem létező útvonal!", (Object)null, var11);
            } else if (var7.path_equal == 2) {
               if (var7.is_extra_path) {
                  addDiffItem(var3, var4, var9, "Többlet útvonal!", (Object)null, var11);
               } else if (var7.is_not_exist_path) {
                  addDiffItem(var3, var4, var9, "Nem létező útvonal!", (Object)null, var11);
               } else {
                  if (!var7.attribute_found) {
                     addDiffItem(var3, var4, var9, "Attributum eltérés!", var11, var11);
                  }

                  if (!var7.value_found) {
                     addDiffItem(var3, var4, var9, "Érték eltérés!", var10, var11);
                  }
               }
            }
         }

         if (var7.path_equal == 1 || var7.path_equal == 2 && !var5) {
            Object var12 = var0.get("children");
            if (var12 instanceof Vector) {
               var7 = walkOnVector((Vector)var12, var1, var2, var3, var4, var5, var6);
            } else {
               var7 = new XMLFileComparator.Differencer.ValidationValue(0, false, false);
            }
         }

         return var7;
      }

      private static XMLFileComparator.Differencer.ValidationValue walkOnVector(Vector var0, Hashtable var1, Vector var2, Vector var3, Object var4, boolean var5, XMLFileComparator.Differencer.CompareStructure var6) {
         XMLFileComparator.Differencer.ValidationValue var7;
         if (var0.size() > 0) {
            var7 = new XMLFileComparator.Differencer.ValidationValue(0, false, false);
            int var10 = 0;

            for(int var11 = var0.size(); var10 < var11; ++var10) {
               Object var9 = var0.get(var10);
               String var13;
               int var15;
               if (var5 && var9 instanceof Hashtable) {
                  String var12 = getPath((String)((Hashtable)var9).get("path"));
                  var13 = getPath((String)((Hashtable)var9).get("indexed_path"));
                  if (var6.is_m_b_i_path && var12.startsWith(var6.m_b_i_path)) {
                     int var14 = getPathIndex(var6.m_b_ii_path);
                     var15 = getPathIndex(getMBIPathFrom(var6.m_b_i_path, var13));
                     if (var14 != var15) {
                        continue;
                     }
                  }
               }

               XMLFileComparator.Differencer.ValidationValue var8;
               if (var9 instanceof Hashtable) {
                  var8 = walkOnHashtable((Hashtable)var9, var1, var2, var3, var4, var5, var6);
               } else if (var9 instanceof Vector) {
                  var8 = walkOnVector((Vector)var9, var1, var2, var3, var4, var5, var6);
               } else {
                  var8 = new XMLFileComparator.Differencer.ValidationValue(0, false, false);
               }

               if (var5) {
                  if (var9 instanceof Hashtable) {
                     Hashtable var18 = (Hashtable)var9;
                     var13 = getPath((String)var18.get("path"));
                     String var19 = var6.path;
                     if (var13.equals(var19)) {
                        if (var8.path_equal == 2) {
                           var15 = countTags(var18);
                           int var16 = countTags(var6.ht_m);
                           if (var15 < var16) {
                              int var17 = getPathIndex(var6.indexed_path);
                              if (var17 > var15) {
                                 var8.is_extra_path = true;
                              }
                           }
                        } else if (var10 == var11 - 1) {
                           var8.is_not_exist_path = true;
                        }
                     }
                  }

                  var7.path_equal = operatorOr(var7, var8);
                  if (var7.path_equal == 2) {
                     var7.attribute_found |= var8.attribute_found;
                     var7.value_found |= var8.value_found;
                  }

                  var7.is_extra_path = var8.is_extra_path;
                  var7.is_not_exist_path = var8.is_not_exist_path;
                  if (var7.is_extra_path || var7.is_not_exist_path) {
                     break;
                  }
               }
            }
         } else {
            var7 = new XMLFileComparator.Differencer.ValidationValue(1, false, false);
         }

         return var7;
      }

      private static String getPath(String var0) {
         return var0 == null ? "/" : var0;
      }

      private static String getIndexedPath(String var0) {
         return var0 == null ? "{0}" : var0;
      }

      private static int countTags(Hashtable var0) {
         int var1 = 0;
         String var2 = (String)var0.get("tag_name");
         Vector var3 = (Vector)((Hashtable)var0.get("parent_tag")).get("children");
         if (var3 != null) {
            int var5 = 0;

            for(int var6 = var3.size(); var5 < var6; ++var5) {
               Hashtable var4 = (Hashtable)var3.get(var5);
               if (var4 != null && var2.equals(var4.get("tag_name"))) {
                  ++var1;
               }
            }
         }

         return var1;
      }

      private static int getPathIndex(String var0) {
         int var2 = var0.lastIndexOf("{");
         int var3 = var0.lastIndexOf("}");
         int var1;
         if (var2 >= 0 && var3 >= 0) {
            var1 = Integer.parseInt(var0.substring(var2 + 1, var3));
         } else {
            var1 = 0;
         }

         return var1;
      }

      private static String getMBIPathFrom(String var0, String var1) {
         String[] var2 = var0.split("/");
         String[] var3 = var1.split("/");
         String var4 = "";
         int var5 = 0;

         for(int var6 = var2.length; var5 < var6; ++var5) {
            var4 = var4 + (var5 > 0 ? "/" : "") + var3[var5];
         }

         return var4;
      }

      private static int operatorOr(int var0, int var1) {
         return var0 > var1 ? var0 : var1;
      }

      private static int operatorOr(XMLFileComparator.Differencer.ValidationValue var0, XMLFileComparator.Differencer.ValidationValue var1) {
         return operatorOr(var0.path_equal, var1.path_equal);
      }

      private static void addDiffItem(Vector var0, Object var1, String var2, String var3, Object var4, Object var5) {
         Vector var6 = new Vector(3);
         var6.add(var1);
         var6.add(var2);
         var6.add(var3);
         var6.add(var4);
         var6.add(var5);
         var0.add(var6);
      }

      private static XMLFileComparator.Differencer.ValidationValue isValidPath(Hashtable var0, Hashtable var1, Vector var2) {
         return walkOnHashtable(var0, (Hashtable)null, (Vector)null, (Vector)null, (Object)null, true, new XMLFileComparator.Differencer.CompareStructure(var1, var2));
      }

      private static class CompareStructure {
         Hashtable ht_m;
         String path;
         String value;
         Hashtable attributes;
         String indexed_path;
         boolean is_m_b_i_path;
         String m_b_i_path;
         String m_b_ii_path;

         CompareStructure(Hashtable var1, Vector var2) {
            this.ht_m = var1;
            this.path = XMLFileComparator.Differencer.getPath((String)var1.get("path"));
            this.value = (String)var1.get("characters");
            this.attributes = (Hashtable)var1.get("attributes");
            this.indexed_path = XMLFileComparator.Differencer.getIndexedPath((String)var1.get("indexed_path"));
            if (var2 != null) {
               int var3 = 0;

               for(int var4 = var2.size(); var3 < var4; ++var3) {
                  String var5 = var2.get(var3).toString();
                  if (this.path.startsWith(var5)) {
                     if (!this.is_m_b_i_path) {
                        this.m_b_i_path = var5;
                     } else if (this.path.length() - var5.length() < this.path.length() - this.m_b_i_path.length()) {
                        this.m_b_i_path = var5;
                     }

                     this.is_m_b_i_path = true;
                  }
               }

               if (this.is_m_b_i_path) {
                  String[] var7 = this.m_b_i_path.split("/");
                  String[] var8 = this.indexed_path.split("/");
                  this.m_b_ii_path = "";
                  int var9 = 0;

                  for(int var6 = var7.length; var9 < var6; ++var9) {
                     this.m_b_ii_path = this.m_b_ii_path + (var9 > 0 ? "/" : "") + var8[var9];
                  }
               }
            }

         }
      }

      private static class ValidationValue {
         public int path_equal;
         public boolean attribute_found;
         public boolean value_found;
         public boolean is_extra_path;
         public boolean is_not_exist_path;

         ValidationValue(int var1, boolean var2, boolean var3) {
            this.path_equal = var1;
            this.attribute_found = var2;
            this.value_found = var3;
            this.is_extra_path = false;
            this.is_not_exist_path = false;
         }

         public boolean isFound() {
            return this.path_equal == 2 && this.attribute_found && this.value_found;
         }

         public void set(XMLFileComparator.Differencer.ValidationValue var1) {
            this.path_equal = var1.path_equal;
            this.attribute_found = var1.attribute_found;
            this.value_found = var1.value_found;
            this.is_extra_path = var1.is_extra_path;
            this.is_not_exist_path = var1.is_not_exist_path;
         }
      }
   }

   private static class XmlHandler extends DefaultHandler {
      public static final String KEY_NAME = "tag_name";
      public static final String KEY_ATTR = "attributes";
      public static final String KEY_PRNT = "parent_tag";
      public static final String KEY_CHDN = "children";
      public static final String KEY_CHRS = "characters";
      public static final String KEY_PATH = "path";
      public static final String KEY_INDEXED_PATH = "indexed_path";
      public static final String PATH_SEPARATOR = "/";
      private Hashtable xml_structure;
      private Hashtable current_tag;
      private StringBuffer chars;
      private StringBuffer current_chars;

      public XmlHandler(StringBuffer var1) {
         this.chars = var1;
      }

      public Hashtable getXmlStructure() {
         return this.xml_structure;
      }

      private StringBuffer getStringBuffer() {
         if (this.chars == null) {
            this.chars = new StringBuffer(1024);
         }

         return this.chars;
      }

      public void startDocument() throws SAXException {
         this.xml_structure = new XMLFileComparator.XmlHandler.TagHashtable(64);
         this.current_tag = this.xml_structure;
         this.chars = this.getStringBuffer();
         this.chars.delete(0, this.chars.length());
      }

      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         XMLFileComparator.XmlHandler.TagHashtable var5 = new XMLFileComparator.XmlHandler.TagHashtable();
         var5.put("tag_name", var3);
         if (var4 != null && var4.getLength() > 0) {
            var5.put("attributes", this.getAttributes(var4, (Hashtable)null));
         }

         this.setParent(this.current_tag, var5);
         this.addToChildren(this.current_tag, var5);
         this.setCharacters(this.current_tag, this.current_chars);
         this.current_chars = null;
         this.current_tag = var5;
         this.setPathInfo(var5);
      }

      public void characters(char[] var1, int var2, int var3) throws SAXException {
         if (this.current_chars == null) {
            this.current_chars = this.getCharacters(this.current_tag);
         }

         this.current_chars.append(var1, var2, var3);
      }

      public void endElement(String var1, String var2, String var3) throws SAXException {
         this.setCharacters(this.current_tag, this.current_chars);
         this.current_chars = null;
         this.current_tag = this.getParent(this.current_tag);
      }

      private Hashtable getAttributes(Attributes var1, Hashtable var2) {
         Hashtable var3 = var2 == null ? new Hashtable() : var2;
         if (var1 != null) {
            int var4 = 0;

            for(int var5 = var1.getLength(); var4 < var5; ++var4) {
               var3.put(var1.getQName(var4), var1.getValue(var4));
            }
         }

         return var3;
      }

      private void addToChildren(Hashtable var1, Hashtable var2) {
         Object var3 = var1.get("children");
         XMLFileComparator.XmlHandler.ChildVector var4;
         if (var3 == null) {
            var4 = new XMLFileComparator.XmlHandler.ChildVector();
            var1.put("children", var4);
         } else {
            var4 = (XMLFileComparator.XmlHandler.ChildVector)var3;
         }

         var4.add(var2);
      }

      private void setParent(Hashtable var1, Hashtable var2) {
         var2.put("parent_tag", var1);
      }

      private Hashtable getParent(Hashtable var1) {
         return (Hashtable)var1.get("parent_tag");
      }

      private StringBuffer getCharacters(Hashtable var1) {
         StringBuffer var2 = this.chars;
         var2.delete(0, var2.length());
         Object var3 = var1.get("characters");
         if (var3 != null) {
            var2.append(var3.toString());
         }

         return var2;
      }

      private void setCharacters(Hashtable var1, StringBuffer var2) {
         if (var2 != null) {
            var1.put("characters", var2.toString());
         }

      }

      private void setPathInfo(Hashtable var1) {
         Object var2 = var1.get("parent_tag");
         String var3;
         String var4;
         if (var2 instanceof Hashtable) {
            var3 = (String)((Hashtable)var2).get("path");
            var3 = var3 == null ? "" : var3;
            var4 = (String)((Hashtable)var2).get("indexed_path");
            var4 = var4 == null ? "" : var4;
         } else {
            var3 = "";
            var4 = "";
         }

         String var5 = (String)var1.get("tag_name");
         var5 = var5 == null ? "" : var5;
         String var6 = var3 + "/" + var5;
         String var7 = var4 + "/" + var5;
         var1.put("path", var6);
         if (var2 instanceof Hashtable) {
            Object var8 = ((Hashtable)var2).get("children");
            if (var8 instanceof Vector) {
               Vector var9 = (Vector)var8;
               int var11 = 0;
               int var12 = 0;

               for(int var13 = var9.size(); var12 < var13; ++var12) {
                  var8 = var9.get(var12);
                  if (var8 instanceof Hashtable) {
                     String var10 = (String)((Hashtable)var8).get("tag_name");
                     if (var5 == null) {
                        if (var10 != null) {
                           continue;
                        }
                     } else if (!var5.equals(var10)) {
                        continue;
                     }

                     ++var11;
                  }
               }

               var1.put("indexed_path", var7 + "{" + var11 + "}");
            }
         } else {
            var1.put("indexed_path", var7 + "{1}");
         }

      }

      private static class ChildVector extends Vector {
         private ChildVector() {
         }

         public synchronized boolean equals(Object var1) {
            boolean var2;
            if (var1 instanceof Vector) {
               Vector var3 = (Vector)var1;
               if (this.size() == var3.size()) {
                  var2 = true;
                  Hashtable var5 = new Hashtable(this.size());
                  int var9 = 0;

                  for(int var10 = this.size(); var9 < var10; ++var9) {
                     int var6 = 0;
                     Object var7 = this.get(var9);

                     for(int var11 = 0; var11 < var10; ++var11) {
                        Object var8 = var3.get(var11);
                        boolean var4 = var7 == null ? var8 == null : var7.equals(var8);
                        if (var4) {
                           ++var6;
                           if (var6 > 1) {
                              break;
                           }

                           if (var5.get(var7) != null) {
                              var6 = 2;
                              break;
                           }

                           var5.put(var7, "");
                        }
                     }

                     if (var6 != 1) {
                        var2 = false;
                        break;
                     }
                  }
               } else {
                  var2 = false;
               }
            } else {
               var2 = false;
            }

            return var2;
         }

         // $FF: synthetic method
         ChildVector(Object var1) {
            this();
         }
      }

      private static class TagHashtable extends Hashtable {
         private boolean hcc = false;

         public TagHashtable() {
         }

         public TagHashtable(int var1) {
            super(var1);
         }

         public synchronized int hashCode() {
            int var1 = 0;
            if (this.size() != 0 && !this.hcc) {
               this.hcc = true;
               Iterator var2 = this.entrySet().iterator();

               while(var2.hasNext()) {
                  Entry var3 = (Entry)var2.next();
                  Object var4 = var3.getKey();
                  if (this.isUsableKey("indexed_path")) {
                     var1 += var4.hashCode() ^ var3.getValue().hashCode();
                  }
               }

               this.hcc = false;
               return var1;
            } else {
               return var1;
            }
         }

         public synchronized boolean equals(Object var1) {
            if (var1 == this) {
               return true;
            } else if (!(var1 instanceof Map)) {
               return false;
            } else {
               Map var2 = (Map)var1;
               if (var2.size() != this.size()) {
                  return false;
               } else {
                  try {
                     Iterator var3 = this.entrySet().iterator();

                     Object var5;
                     label48:
                     do {
                        Object var6;
                        do {
                           Entry var4;
                           do {
                              if (!var3.hasNext()) {
                                 return true;
                              }

                              var4 = (Entry)var3.next();
                              var5 = var4.getKey();
                           } while(!this.isUsableKey(var5));

                           var6 = var4.getValue();
                           if (var6 == null) {
                              continue label48;
                           }
                        } while(var6.equals(var2.get(var5)));

                        return false;
                     } while(var2.get(var5) == null && var2.containsKey(var5));

                     return false;
                  } catch (ClassCastException var7) {
                     return false;
                  } catch (NullPointerException var8) {
                     return false;
                  }
               }
            }
         }

         public synchronized String toString() {
            int var1 = this.size() - 1;
            StringBuffer var2 = new StringBuffer();
            Iterator var3 = this.entrySet().iterator();
            var2.append("{");

            for(int var4 = 0; var4 <= var1; ++var4) {
               Entry var5 = (Entry)((Entry)var3.next());
               Object var6 = var5.getKey();
               if (this.isUsableKey(var6)) {
                  Object var7 = var5.getValue();
                  var2.append(var6 == this ? "(this Map)" : var6).append("=").append(var7 == this ? "(this Map)" : var7);
                  if (var4 < var1) {
                     var2.append(", ");
                  }
               }
            }

            var2.append("}");
            return var2.toString();
         }

         private boolean isUsableKey(Object var1) {
            return !var1.equals("indexed_path") && !var1.equals("parent_tag");
         }
      }
   }
}
