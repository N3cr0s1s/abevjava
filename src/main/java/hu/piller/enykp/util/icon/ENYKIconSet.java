package hu.piller.enykp.util.icon;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.util.base.ImageUtil;
import me.necrocore.abevjava.NecroFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.ImageIcon;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ENYKIconSet {

   private final static Logger logger = LoggerFactory.getLogger(ENYKIconSet.class);

   private static final byte[] EMPTY_IMAGE_DATA = new byte[] { 0 };
   public static final ImageIcon MENU_EMPTY;

   private static final ENYKIconSet instance = new ENYKIconSet();

   private final Hashtable<String, ImageIcon> icons = new Hashtable<>();
   private final ImageIcon emptyIcon = new EmptyIcon();

   public static ENYKIconSet getInstance() {
      return instance;
   }

   static {
      MENU_EMPTY = new ImageIcon(EMPTY_IMAGE_DATA);
   }

   private ENYKIconSet() {
      try {
         URL codeSourceUrl = getClass().getProtectionDomain()
                 .getCodeSource()
                 .getLocation();
         String urlString = codeSourceUrl.toString();

         if (urlString.endsWith(".jar")) {
            // Running from inside a JAR
            String path = codeSourceUrl.getPath();
            int splitIndex = urlString.indexOf(path);
            String jarPath = URLDecoder.decode(urlString.substring(splitIndex), "UTF-8");

            try(JarFile jarFile = new JarFile(jarPath)) {
               Enumeration<JarEntry> entries = jarFile.entries();
               ImageUtil imageLoader = new ImageUtil(codeSourceUrl);

               while (entries.hasMoreElements()) {
                  JarEntry entry = entries.nextElement();
                  String entryName = entry.getName();

                  if (entryName.startsWith("resources/icons") &&
                          (entryName.endsWith(".gif") || entryName.endsWith(".png"))) {

                     String iconKey = entryName
                             .substring(entryName.lastIndexOf('/') + 1, entryName.length() - 4);
                     ImageIcon icon = new ImageIcon(imageLoader.getImageResource(entryName));
                     icons.put(iconKey, icon);
                  }
               }
            }

         } else {
            // Running from filesystem (e.g. IDE)
            Enumeration<URL> dirs = ClassLoader.getSystemResources("resources/icons/");

            while (dirs.hasMoreElements()) {
               URL dirUrl = dirs.nextElement();
               File dir = new NecroFile(dirUrl.toURI());
               if (!dir.isDirectory()) {
                  continue;
               }

               for (File file : Objects.requireNonNull(dir.listFiles())) {
                  String name = file.getName();
                  if (name.endsWith(".gif") || name.endsWith(".png")) {
                     String iconKey = name.substring(0, name.length() - 4);
                     icons.put(iconKey, new ImageIcon(file.getAbsolutePath()));
                  }
               }
            }
         }
      } catch (Exception e) {
         logger.error("Error while initializing ENYKIconSet", e);
      }
   }

   /**
    * Registers a new icon under the given key.
    * @return true if the icon was added; false if the key already existed.
    */
   public boolean set(String key, ImageIcon icon) {
      if (icons.containsKey(key)) {
         return false;
      }
      icons.put(key, icon);
      return true;
   }

   /**
    * Returns the icon exactly matching the key, or an empty placeholder if not found.
    */
   public ImageIcon getExact(String key) {
      if (key == null) {
         return emptyIcon;
      }
      return icons.getOrDefault(key, emptyIcon);
   }

   /**
    * Returns an icon scaled/selected based on the current common font size.
    */
   public ImageIcon get(String key) {
      if (key == null) {
         return emptyIcon;
      }

      int fontSize = GuiUtil.getCommonFontSize();

      // special handling for the original Anyk icon
      if ("anyk_eredeti_meret".equals(key)) {
         key += (fontSize < 37 ? "_24" : "_48");
      }
      // print icons are fixed-size
      else if (key.startsWith("print_")) {
         return icons.getOrDefault(key, emptyIcon);
      }
      // status and page icons
      else if (key.startsWith("statusz") || key.startsWith("page_")) {
         if (fontSize < 23) {
            key += "_12";
         } else if (fontSize < 42) {
            key += "_24";
         } else {
            key += "_48";
         }
      }
      // checkbox and radio
      else if (key.startsWith("checkbox") || key.startsWith("radio")) {
         if (fontSize < 20) {
            key += "_12";
         } else if (fontSize < 28) {
            key += "_21";
         } else if (fontSize < 36) {
            key += "_30";
         } else if (fontSize < 48) {
            key += "_39";
         } else {
            key += "_48";
         }
      }
      // all other icons
      else {
         if (fontSize < 20) {
            key += "_24";
         } else if (fontSize < 28) {
            key += "_32";
         } else if (fontSize < 36) {
            key += "_48";
         } else if (fontSize < 48) {
            key += "_60";
         } else {
            key += "_72";
         }
      }

      return icons.getOrDefault(key, emptyIcon);
   }

   /**
    * A placeholder icon that paints nothing, but reserves consistent size.
    */
   private static class EmptyIcon extends ImageIcon {
      @Override
      public Image getImage() {
         return MENU_EMPTY.getImage();
      }

      @Override
      public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
         // no-op
      }

      @Override
      public int getIconWidth() {
         return 20;
      }

      @Override
      public int getIconHeight() {
         return 20;
      }
   }
}
