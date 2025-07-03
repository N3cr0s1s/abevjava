package hu.piller.enykp.extensions.db;

import java.util.Date;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public interface IDbInfo {
   DefaultTableModel getTable();

   Vector getVectors();

   Date getStartDate();

   Date getEndDate();

   String getMessage();

   void setMessage(String var1);

   String getString();

   void setString(String var1);

   int[] getInts();

   void setInts(int[] var1);
}
