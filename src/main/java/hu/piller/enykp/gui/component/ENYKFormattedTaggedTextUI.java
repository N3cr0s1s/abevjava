package hu.piller.enykp.gui.component;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import javax.accessibility.Accessible;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;
import javax.swing.text.Position.Bias;

public class ENYKFormattedTaggedTextUI extends TextUI {
   private TextUI tui;
   private ENYKFormattedTaggedTextField editor;
   private boolean painted;

   ENYKFormattedTaggedTextUI(TextUI var1) {
      this.setUI(var1);
   }

   public void setUI(TextUI var1) {
      this.painted = false;
      this.tui = var1;
   }

   public TextUI getUI() {
      return this.tui;
   }

   public void installUI(JComponent var1) {
      this.editor = (ENYKFormattedTaggedTextField)var1;
      this.tui.installUI(var1);
   }

   public void uninstallUI(JComponent var1) {
      this.painted = false;
      this.tui.uninstallUI(var1);
   }

   public void paint(Graphics var1, JComponent var2) {
      this.painted = true;
      this.editor.paint(var1);
   }

   public void update(Graphics var1, JComponent var2) {
      this.editor.update(var1);
   }

   public Dimension getPreferredSize(JComponent var1) {
      return this.tui.getPreferredSize(var1);
   }

   public Dimension getMinimumSize(JComponent var1) {
      return this.tui.getMinimumSize(var1);
   }

   public Dimension getMaximumSize(JComponent var1) {
      return this.tui.getMaximumSize(var1);
   }

   public boolean contains(JComponent var1, int var2, int var3) {
      return this.tui.contains(var1, var2, var3);
   }

   public static ComponentUI createUI(JComponent var0) {
      return ComponentUI.createUI(var0);
   }

   public int getAccessibleChildrenCount(JComponent var1) {
      return this.tui.getAccessibleChildrenCount(var1);
   }

   public Accessible getAccessibleChild(JComponent var1, int var2) {
      return this.tui.getAccessibleChild(var1, var2);
   }

   public Rectangle modelToView(JTextComponent var1, int var2) throws BadLocationException {
      return this.editor._modelToView(var2, (Bias)null);
   }

   public Rectangle modelToView(JTextComponent var1, int var2, Bias var3) throws BadLocationException {
      return this.editor._modelToView(var2, var3);
   }

   public int viewToModel(JTextComponent var1, Point var2) {
      return this.editor._viewToModel(var2, (Bias[])null);
   }

   public int viewToModel(JTextComponent var1, Point var2, Bias[] var3) {
      return this.editor._viewToModel(var2, var3);
   }

   public int getNextVisualPositionFrom(JTextComponent var1, int var2, Bias var3, int var4, Bias[] var5) throws BadLocationException {
      return this.editor._getNextVisualPositionFrom(var2, var3, var4, var5);
   }

   public void damageRange(JTextComponent var1, int var2, int var3) {
      if (this.painted) {
         this.editor.repaint();
      }

   }

   public void damageRange(JTextComponent var1, int var2, int var3, Bias var4, Bias var5) {
      if (this.painted) {
         this.editor.repaint();
      }

   }

   public EditorKit getEditorKit(JTextComponent var1) {
      return this.tui.getEditorKit(var1);
   }

   public View getRootView(JTextComponent var1) {
      return this.tui.getRootView(var1);
   }
}
