package org.bouncycastle.crypto;

import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;

class JCEUtil {
   private static final int MAX_ALIASES = 10;

   static JCEUtil.Implementation getImplementationFromProvider(String baseName, String algorithm, Provider prov) {
      int count = 0;

      String alias;
      while((alias = prov.getProperty("Alg.Alias." + baseName + "." + algorithm)) != null) {
         algorithm = alias;
         ++count;
         if (count > 10) {
            return null;
         }
      }

      String className = prov.getProperty(baseName + "." + algorithm);
      if (className != null) {
         try {
            ClassLoader clsLoader = prov.getClass().getClassLoader();
            Class cls;
            if (clsLoader != null) {
               cls = clsLoader.loadClass(className);
            } else {
               cls = Class.forName(className);
            }

            return new JCEUtil.Implementation(cls.newInstance(), prov);
         } catch (ClassNotFoundException var8) {
            throw new IllegalStateException("algorithm " + algorithm + " in provider " + prov.getName() + " but no class \"" + className + "\" found!");
         } catch (Exception var9) {
            var9.printStackTrace();
            throw new IllegalStateException("algorithm " + algorithm + " in provider " + prov.getName() + " but class \"" + className + "\" inaccessible!");
         }
      } else {
         return null;
      }
   }

   static JCEUtil.Implementation getImplementation(String baseName, String algorithm, String provider) throws NoSuchProviderException {
      if (provider == null) {
         Provider[] prov = Security.getProviders();

         for(int i = 0; i != prov.length; ++i) {
            JCEUtil.Implementation imp = getImplementationFromProvider(baseName, algorithm.toUpperCase(), prov[i]);
            if (imp != null) {
               return imp;
            }

            imp = getImplementationFromProvider(baseName, algorithm, prov[i]);
            if (imp != null) {
               return imp;
            }
         }

         return null;
      } else {
         Provider prov = Security.getProvider(provider);
         if (prov == null) {
            throw new NoSuchProviderException("Provider " + provider + " not found");
         } else {
            JCEUtil.Implementation imp = getImplementationFromProvider(baseName, algorithm.toUpperCase(), prov);
            return imp != null ? imp : getImplementationFromProvider(baseName, algorithm, prov);
         }
      }
   }

   static class Implementation {
      Object engine;
      Provider provider;

      Implementation(Object engine, Provider provider) {
         this.engine = engine;
         this.provider = provider;
      }

      Object getEngine() {
         return this.engine;
      }

      Provider getProvider() {
         return this.provider;
      }
   }
}
