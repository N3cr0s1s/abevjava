package hu.piller.enykp.alogic.templateutils.blacklist.provider;

@FunctionalInterface
public interface BlacklistProvider {
   String get() throws BlacklistProviderException;
}
