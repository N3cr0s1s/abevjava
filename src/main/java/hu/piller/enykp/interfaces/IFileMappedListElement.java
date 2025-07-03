package hu.piller.enykp.interfaces;

public interface IFileMappedListElement {
   float getLoadfactor();

   boolean noMapped();

   boolean isChanged();

   void clearChanged();

   Object getMappedObject();

   void setMappedObject(Object var1);

   void setIndex(int var1);
}
