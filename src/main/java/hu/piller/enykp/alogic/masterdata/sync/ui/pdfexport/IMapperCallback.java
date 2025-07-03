package hu.piller.enykp.alogic.masterdata.sync.ui.pdfexport;

public interface IMapperCallback {
   boolean isColumnEnabled(int var1);

   Object mapValue(int var1, Object var2);
}
