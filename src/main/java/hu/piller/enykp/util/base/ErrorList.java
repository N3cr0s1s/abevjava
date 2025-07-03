package hu.piller.enykp.util.base;

import hu.piller.enykp.alogic.calculator.calculator_c.GoToButton;
import hu.piller.enykp.extensions.db.DbFactory;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.util.base.eventsupport.DefaultEventSupport;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import java.awt.Component;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.SwingUtilities;

public class ErrorList extends List implements IErrorList, IEventSupport {
   private static IErrorList instance;
   private static final int ERROR_LIST_MAX_DEEP = 500;
   public static boolean debug = true;
   public static final Long MISSING_ERROR_CODE = new Long(99999L);
   public static final String MISSING_ERROR_CODE_ERR_MSG = "Hiányzó hibakód! (Errorlist) ";
   private static Vector<String> errorCodes = new Vector();
   private boolean not_null_filter = false;
   private DefaultEventSupport des = new DefaultEventSupport();
   private final ErrorList.ObjectComparator object_comparator = new ErrorList.ObjectComparator();

   public static IErrorList getInstance() {
      return getInstance(500, true);
   }

   public static IErrorList getInstance(int var0) {
      return getInstance(var0, false);
   }

   public static IErrorList getInstance(int var0, boolean var1) {
      if (!var1) {
         return new ErrorList(var0);
      } else {
         if (instance == null) {
            instance = new ErrorList(var0);
         }

         return instance;
      }
   }

   private ErrorList(int var1) {
      super(var1);
   }

   public synchronized boolean store(Object var1, String var2, Exception var3, Object var4) {
      return this.store(var1, var2, IErrorList.LEVEL_ERROR, var3, var4, (Component)null, (String)null);
   }

   public synchronized boolean store(Object var1, String var2, Integer var3, Exception var4, Object var5, Component var6, String var7) {
      return this.store(var1, var2, var3, var4, var5, var6, var7, (Object)null, (Object)null);
   }

   public boolean store(Object var1, String var2, Integer var3, Exception var4, Object var5, Component var6, String var7, Object var8, Object var9, boolean var10) {
      return this.store(var1, this.extendWithGroupErrorCode(var8, var10) + var2, var3, var4, var5, (Component)null, (String)null, var8, var9);
   }

   public synchronized boolean store(Object var1, String var2, Integer var3, Exception var4, Object var5, Component var6, String var7, Object var8, Object var9) {
      Object var10 = var1;
      String var11 = var2;
      if (var1 == null) {
         var10 = MISSING_ERROR_CODE;
         var11 = "Hiányzó hibakód! (Errorlist) " + var2;
         System.out.println(var11);
      }

      Object[] var12 = new Object[]{var10, var11, var4, var5, var3 == null ? IErrorList.LEVEL_ERROR : var3, var8, var9};
      Object[] var13 = var12;
      if (var5 instanceof GoToButton) {
         var13 = new Object[]{var10, var11, var4, null, var3 == null ? IErrorList.LEVEL_ERROR : var3};
      }

      boolean var14 = super.add(var13, false);
      if (IErrorList.LEVEL_SHOW_WARNING.equals(var3)) {
         this.showMessageDialog(var6, this.getFormattedText(var11), var7 == null ? "Figyelem !" : var7, 2);
      } else if (IErrorList.LEVEL_SHOW_ERROR.equals(var3)) {
         this.showMessageDialog(var6, this.getFormattedText(var11), var7 == null ? "Hiba !" : var7, 0);
      } else if (IErrorList.LEVEL_SHOW_MESSAGE.equals(var3)) {
         this.showMessageDialog(var6, this.getFormattedText(var11), var7 == null ? "Üzenet" : var7, 1);
      }

      this.des.fireEvent(this, "store", "insert", "item", var12);
      if (debug && var3 < 1024 && var4 != null) {
         var4.printStackTrace();
      }

      return var14;
   }

   private String getFormattedText(String var1) {
      if (var1 == null) {
         return null;
      } else {
         try {
            String var2 = var1.replaceAll("#13", "<BR>");
            return "<HTML>" + var2 + "</HTML>";
         } catch (Exception var3) {
            return var1;
         }
      }
   }

   private void showMessageDialog(final Component var1, final Object var2, final String var3, final int var4) {
      if (SwingUtilities.isEventDispatchThread()) {
         GuiUtil.showMessageDialog_checked(var1, var2, var3, var4);
      } else {
         try {
            SwingUtilities.invokeAndWait(new Runnable() {
               public void run() {
                  GuiUtil.showMessageDialog_checked(var1, var2, var3, var4);
               }
            });
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

   }

   public synchronized Object[] getItems() {
      this.not_null_filter = true;
      Object[] var1 = super.filter((Object)null);
      this.not_null_filter = false;
      return var1;
   }

   public synchronized Object[] getItems(Object var1) {
      return super.filter(var1);
   }

   public synchronized Object[] getIdList() {
      Object[] var1 = this.getItems();
      if (var1 != null && var1.length > 0) {
         Object[] var4 = new Object[var1.length];
         int var6 = 0;

         int var7;
         for(var7 = var1.length; var6 < var7; ++var6) {
            var4[var6] = ((Object[])((Object[])var1[var6]))[0];
         }

         Arrays.sort(var4, this.object_comparator);
         Object var3 = var4[0];
         int var2 = 1;
         var6 = 0;

         for(var7 = var4.length; var6 < var7; ++var6) {
            if (!var3.equals(var4[var6])) {
               var3 = var4[var6];
               ++var2;
            }
         }

         Object[] var5 = new Object[var2];
         var2 = 0;
         var5[var2] = var4[0];
         var6 = 0;

         for(var7 = var4.length; var6 < var7; ++var6) {
            if (!var5[var2].equals(var4[var6])) {
               ++var2;
               var5[var2] = var4[var6];
            }
         }

         return var5;
      } else {
         return new Object[0];
      }
   }

   public synchronized void clear() {
      super.clear();
      errorCodes.clear();
      this.des.fireEvent(this, "clear", "delete");
   }

   public synchronized Object[] removeId(Object var1) {
      Object[] var2 = this.getItems(var1);
      int var3 = 0;

      for(int var4 = var2.length; var3 < var4; ++var3) {
         super.remove(var2[var3]);
      }

      this.des.fireEvent(this, "removeId", "delete");
      return var2;
   }

   public synchronized void remove(Object var1) {
      super.remove(var1);
      this.des.fireEvent(this, "remove", "delete");
   }

   protected synchronized boolean compare(Object var1, Object var2) {
      if (var2 == null) {
         return false;
      } else {
         return this.not_null_filter ? true : ((Object[])((Object[])var2))[0].equals(var1);
      }
   }

   public void addEventListener(IEventListener var1) {
      this.des.addEventListener(var1);
   }

   public void removeEventListener(IEventListener var1) {
      this.des.removeEventListener(var1);
   }

   public Vector fireEvent(Event var1) {
      return this.des.fireEvent(var1);
   }

   public void writeError(Object var1, String var2, Exception var3, Object var4) {
      try {
         this.store(var1, var2, var3, var4);
      } catch (Exception var6) {
         Tools.eLog(var3, 0);
      }

   }

   public void writeError(Object var1, String var2, Integer var3, Exception var4, Object var5) {
      this.store(var1, var2, var3, var4, var5, (Component)null, (String)null);
   }

   public void writeError(Object var1, String var2, Integer var3, Exception var4, Object var5, Object var6, Object var7) {
      this.writeError(var1, var2, var3, var4, var5, var6, var7, true);
   }

   public void writeError(Object var1, String var2, Integer var3, Exception var4, Object var5, Object var6, Object var7, boolean var8) {
      this.store(var1, this.extendWithGroupErrorCode(var6, var8) + var2, var3, var4, var5, (Component)null, (String)null, var6, var7);
   }

   private String extendWithGroupErrorCode(Object var1, boolean var2) {
      if (var1 == null) {
         return "";
      } else if (MainFrame.role.equals("0")) {
         return "";
      } else if (MainFrame.opmode.equals("2")) {
         return "";
      } else if (MainFrame.opmode.equals("0")) {
         return "";
      } else if (MainFrame.onyaCheckMode) {
         return "";
      } else {
         Hashtable var3 = DbFactory.getDbHandler().getErrorMessages();
         String[] var4 = (String[])((String[])var3.get(var1));
         String var5 = "";
         String var6 = "[" + var1 + ", " + (var2 ? "K" : "B") + "] ";
         if (var4 != null && var4.length > 2) {
            var5 = "[" + var4[2] + "] ";
         }

         return var5 + var6;
      }
   }

   public void writeError(Object var1, String var2, Integer var3, Exception var4, Object var5, Component var6, String var7) {
      try {
         this.store(var1, var2, var3, var4, var5, var6, var7);
      } catch (Exception var9) {
         Tools.eLog(var4, 0);
      }

   }

   public Vector<String> getErrorCodeList() {
      return errorCodes;
   }

   public void setErrorCodes(Vector<String> var1) {
      errorCodes = var1;
   }

   private class ObjectComparator implements Comparator {
      private ObjectComparator() {
      }

      public int compare(Object var1, Object var2) {
         if (var1 != null && var2 != null) {
            String var3 = var1.toString();
            String var4 = var2.toString();
            return var3.compareTo(var4);
         } else if (var1 == var2) {
            return 0;
         } else {
            return var2 == null ? 1 : -1;
         }
      }

      // $FF: synthetic method
      ObjectComparator(Object var2) {
         this();
      }
   }
}
