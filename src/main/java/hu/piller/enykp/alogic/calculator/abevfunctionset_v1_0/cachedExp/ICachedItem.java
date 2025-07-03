package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.cachedExp;

import java.util.Vector;

public interface ICachedItem {
   void exec();

   void init();

   void releaseTmpData();

   Object[] getResult();

   Vector getErrors();

   String getFormid();

   String getFunction();

   String getParameter();
}
