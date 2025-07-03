package hu.piller.enykp.extensions.db;

import java.sql.Clob;
import java.sql.ResultSet;

public interface IDbResult {
   int getDbStatus();

   String getDbMessage();

   String[] getDbArray();

   ResultSet getDbResultSet();

   Clob getDbClob();
}
