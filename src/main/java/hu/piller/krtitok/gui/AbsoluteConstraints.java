package hu.piller.krtitok.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;

public class AbsoluteConstraints implements Serializable {
   static final long serialVersionUID = 5261460716622152494L;
   public int x;
   public int y;
   public int width;
   public int height;

   public AbsoluteConstraints(Point pos) {
      this(pos.x, pos.y);
   }

   public AbsoluteConstraints(int x, int y) {
      this.width = -1;
      this.height = -1;
      this.x = x;
      this.y = y;
   }

   public AbsoluteConstraints(Point pos, Dimension size) {
      this.width = -1;
      this.height = -1;
      this.x = pos.x;
      this.y = pos.y;
      if (size != null) {
         this.width = size.width;
         this.height = size.height;
      }

   }

   public AbsoluteConstraints(int x, int y, int width, int height) {
      this.width = -1;
      this.height = -1;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public String toString() {
      return super.toString() + " [x=" + this.x + ", y=" + this.y + ", width=" + this.width + ", height=" + this.height + "]";
   }
}
