package hu.piller.enykp.util.filelist;

import java.io.Serializable;

public class FileItem implements Serializable {
   String type;
   String fileName;
   long lastModTime;
   boolean checked;
   Object fileInfo;

   public FileItem(String var1, String var2, long var3, Object var5) {
      this.fileName = var2;
      this.update(var1, var3, var5);
   }

   public void update(String var1, long var2, Object var4) {
      this.type = var1;
      this.lastModTime = var2;
      this.fileInfo = var4;
      this.checked = true;
   }

   public boolean isChecked() {
      return this.checked;
   }

   public void setChecked(boolean var1) {
      this.checked = var1;
   }

   public Object getFileInfo() {
      return this.fileInfo;
   }

   public void setFileInfo(Object var1) {
      this.fileInfo = var1;
   }

   public String getFileName() {
      return this.fileName;
   }

   public void setFileName(String var1) {
      this.fileName = var1;
   }

   public long getLastModTime() {
      return this.lastModTime;
   }

   public void setLastModTime(long var1) {
      this.lastModTime = var1;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String var1) {
      this.type = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[" + this.fileName + "]");
      var1.append("\n");
      var1.append(this.lastModTime);
      var1.append("\n");
      var1.append(this.type);
      var1.append("\n");
      var1.append(this.fileInfo == null ? "" : this.fileInfo.toString());
      var1.append("\n");
      return var1.toString();
   }
}
