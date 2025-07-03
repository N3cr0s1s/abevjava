package hu.piller.enykp.util.base.errordialog;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.interfaces.IOECGetter;
import javax.swing.ImageIcon;

public class TextWithIcon implements IOECGetter {
   public String text;
   public ImageIcon ii;
   public int imageType;
   public Object storeItemObject;
   public String formId;
   public Object elem;
   public String officeErrorCode;
   public String valueToDb;
   public static final int IMAGE_NO = -1;
   public static final int IMAGE_RED = 0;
   public static final int IMAGE_BLUE = 1;
   public static final int IMAGE_BLACK = 2;
   public static final int IMAGE_GREEN = 3;
   public static final int IMAGE_YELLOW = 4;
   public static final ImageIcon IMG_REDPOINT = GuiUtil.IconGet("statusz_piros");
   public static final ImageIcon IMG_BLUEPOINT = GuiUtil.IconGet("statusz_kek");
   public static final ImageIcon IMG_GREENPOINT = GuiUtil.IconGet("statusz_zold");
   public static final ImageIcon IMG_BLACKPOINT = GuiUtil.IconGet("statusz_fekete");
   public static final ImageIcon IMG_YELLOWPOINT = GuiUtil.IconGet("statusz_sarga");

   public TextWithIcon(String var1, int var2) {
      this(var1, var2, (String)null, (String)null, (Object)null, (String)null, (Object)null);
   }

   public TextWithIcon(String var1, int var2, String var3, String var4) {
      this(var1, var2, var3, var4, (Object)null, (String)null, (Object)null);
   }

   public TextWithIcon(String var1, int var2, String var3, String var4, Object var5, String var6, Object var7) {
      this.text = var1.replaceAll("#13", " ");
      switch(var2) {
      case 0:
         this.ii = IMG_REDPOINT;
         break;
      case 1:
         this.ii = IMG_BLUEPOINT;
         break;
      case 2:
         this.ii = IMG_BLACKPOINT;
         break;
      case 3:
         this.ii = IMG_GREENPOINT;
         break;
      case 4:
         this.ii = IMG_YELLOWPOINT;
         break;
      default:
         this.ii = null;
      }

      this.imageType = var2;
      this.officeErrorCode = var3;
      this.valueToDb = var4;
      this.storeItemObject = var5;
      this.formId = var6;
      this.elem = var7;
   }

   public TextWithIcon(String var1) {
      this(var1, 0, (String)null, (String)null, (Object)null, (String)null, (Object)null);
   }

   public String toString() {
      return this.text != null ? this.text : "";
   }

   public String getOEC() {
      return this.imageType == -1 ? null : this.officeErrorCode;
   }

   public String getBKI() {
      return this.imageType == -1 ? null : this.valueToDb;
   }

   public String getMSG() {
      return this.text;
   }

   public boolean isRealError() {
      return this.imageType > -1 && this.imageType < 3;
   }
}
