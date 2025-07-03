package hu.piller.enykp.alogic.upgrademanager_v2_0.components;

import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Components {
   private ReentrantLock lock = new ReentrantLock();
   protected Vector<VersionData> components = new Vector();

   public VersionData getVersionDataByIndex(int var1) {
      return (VersionData)this.components.get(var1);
   }

   public void setComponents(Collection var1) {
      if (var1 != null) {
         try {
            this.lock.lock();
            this.components.clear();
            Iterator var2 = var1.iterator();

            while(var2.hasNext()) {
               Object var3 = var2.next();
               if (var3 != null && var3 instanceof VersionData) {
                  this.components.add((VersionData)var3);
               }
            }
         } finally {
            this.lock.unlock();
         }

      }
   }

   public Collection getComponents() {
      Vector var1 = new Vector();

      Vector var2;
      try {
         this.lock.lock();
         var1.addAll(this.components);
         var2 = var1;
      } finally {
         this.lock.unlock();
      }

      return var2;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 instanceof Components) {
         Components var2 = (Components)var1;
         Collection var3 = var2.getComponents();

         boolean var4;
         try {
            this.lock.lock();
            if (this.components.size() != var3.size()) {
               return false;
            }

            var4 = this.components.containsAll(var3);
         } finally {
            this.lock.unlock();
         }

         return var4;
      } else {
         return false;
      }
   }
}
